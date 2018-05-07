package cn.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;

import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;
//内容管理
@Service
public class ContentServiceImpl implements ContentService{

	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;
	@Autowired
	private TbContentMapper mapper;
	@Autowired
	JedisClient jedisClient;
	@Override
	public E3Result addContent(TbContent content) {
		// 将内容数据插入内容表
		content.setCreated(new Date());
		content.setUpdated(new Date());
		mapper.insert(content);
		//缓存同步
		jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
		return E3Result.ok();
	}
	
	
	@Override
	public List<TbContent> getContentListByCid(long cid) {
		// TODO Auto-generated method stub//
		//查询缓存
		try {
			String json = jedisClient.hget(CONTENT_LIST, cid+"");
			if (json!=null&&json!="") {
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		//如果缓存中有，则直接响应结果
		//如果没有缓存，查询数据库
		TbContentExample example=new TbContentExample();
	    Criteria criteria = example.createCriteria();
	    criteria.andCategoryIdEqualTo(cid);
		List<TbContent> list = mapper.selectByExampleWithBLOBs(example);
		//将结果添加到缓存
		try {
			jedisClient.hset(CONTENT_LIST, cid+"", JsonUtils.objectToJson(list));
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

}
