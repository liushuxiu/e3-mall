package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;

/**
 * 商品管理Service
 * <p>Title: ItemServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired 
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private  Destination topicDestination;
	@Autowired
	private JedisClient jedisClient;

	@Autowired
	private  TbItemDescMapper tbItemDescMapper;

	@Value("${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;
	@Value("${ITEM_CACHE_EXPIRE}")
	private Integer ITEM_CACHE_EXPIRE;
	@Override
	public TbItem getItemById(long itemId) {

		try {
			String json=jedisClient.get(REDIS_ITEM_PRE+":"+itemId+":BASE");
			if (json!=null&&json!="") {
				TbItem tbItem=JsonUtils.jsonToPojo(json, TbItem.class );
				return tbItem;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		//根据主键查询
		//TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andIdEqualTo(itemId);
		//执行查询
		List<TbItem> list = itemMapper.selectByExample(example);

		if (list != null && list.size() > 0) {

			try {
				jedisClient .set(REDIS_ITEM_PRE+":"+itemId+":BASE",
						JsonUtils.objectToJson(list.get(0)));
				jedisClient.expire(REDIS_ITEM_PRE+":"+itemId+":BASE", ITEM_CACHE_EXPIRE);
			} catch (Exception e) {
				// TODO: handle exception
			}
			return list.get(0);
		}
		return null;
	}

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		// TODO Auto-generated method stub
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example=new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		EasyUIDataGridResult result=new EasyUIDataGridResult();
		result.setRows(list);
		//取分页信息
		PageInfo<TbItem> pageInfo=new PageInfo<>(list);
		long total=pageInfo.getTotal();
		result.setTotal(total);
		return result;
	}

	@Override
	public E3Result addItem(TbItem item, String desc) {
		// TODO Auto-generated method stub
		//生成商品Id
		final long itemId=IDUtils.genItemId();
		//补全item的属性
		item.setId(itemId);
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//商品表插入
		itemMapper.insert(item);
		//创建商品描述表对应的pojo
		TbItemDesc itemDesc=new TbItemDesc();
		//补全属性
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());

		//商品描述表插入数据
		tbItemDescMapper.insert(itemDesc);
	

		jmsTemplate .send(topicDestination,new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {


				TextMessage textMessage = session .createTextMessage(itemId+"");
				System.err.println("itemId-fasong --------"+itemId);
				return textMessage;
			}
		});

		//返回成功

		return E3Result.ok();
	}

	@Override
	public TbItemDesc getItemDescById(long itemId) {
		// TODO Auto-generated method stub
		try {

			String json=jedisClient.get(REDIS_ITEM_PRE+":"+itemId+":DESC");
			if (json!=null&&json!="") {
				TbItemDesc tbItemDesc=JsonUtils.jsonToPojo(json, TbItemDesc.class );
				return tbItemDesc;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		try {
			jedisClient .set(REDIS_ITEM_PRE+":"+itemId+":DESC",
					JsonUtils.objectToJson(itemDesc));
					jedisClient.expire(REDIS_ITEM_PRE+":"+itemId+":DESC", ITEM_CACHE_EXPIRE);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return itemDesc;
	}

}
