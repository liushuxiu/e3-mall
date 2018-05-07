package cn.e3mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private TbItemMapper itemMapper;
	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;
	@Override
	public E3Result addCart(long userId, long itemId,int num) {
		// TODO Auto-generated method stub
		//redis中添加购物车
		//数据类型是hash key，用户id:filed   vlaue:商品信息
		//判断商品是否存在，如果存在数量相加，反之根据商品id获取信息，
		String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
		if (json!=null &&json!="") {
			TbItem item=JsonUtils.jsonToPojo(json, TbItem.class);
			item.setNum(item.getNum()+num);
			jedisClient.hset(REDIS_CART_PRE+":"+userId, itemId+"", 
					JsonUtils.objectToJson(item));
			return E3Result.ok();
		}
		
		//不存在
		TbItem item=itemMapper.selectByPrimaryKey(itemId);
		item.setNum(num);
		String image=item.getImage();
		if (image!=null &&image!="") {
			item.setImage(image.split(",")[0]);
			
		}
		//添加到购物车列表
		jedisClient.hset(REDIS_CART_PRE+":"+userId, itemId+"", 
				JsonUtils.objectToJson(item));
		return E3Result.ok();
	}
	@Override
	public E3Result mergeCart(long userId, List<TbItem> itemList) {
		// 遍历商品列表
		// 把列表添加到购物车
		//判断购物车中是否有此商品 
		// 如果有，数量相加
		for (TbItem tbItem : itemList) {
			addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		
		return E3Result.ok();
	}
	@Override
	public List<TbItem> getCartList(long userId) {
		//根据用户id获取购物车列表
		List<String> jsonList = jedisClient.hvals(REDIS_CART_PRE+":"+userId);
		List<TbItem> itemList=new ArrayList<>();
		for (String string  : jsonList) {
			TbItem item=JsonUtils.jsonToPojo(string, TbItem.class);
			itemList.add(item);
		}
		return itemList;
	}
	@Override
	public E3Result updateCartNum(long userId, long itemId, int num) {

		String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
		TbItem tbItem=JsonUtils.jsonToPojo(json, TbItem.class);
		tbItem.setNum(num);
		jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
		
		return E3Result.ok();
	}
	@Override
	public E3Result deleteCartItem(long userId, long itemId) {
		// TODO Auto-generated method stub
		jedisClient.hdel(REDIS_CART_PRE + ":" + userId, itemId + "");
		return E3Result.ok();
	}
	@Override
	public E3Result clearCartList(long userId) {
		// TODO Auto-generated method stub
		jedisClient.del(REDIS_CART_PRE+":"+userId);
		return E3Result.ok();
	}

}
