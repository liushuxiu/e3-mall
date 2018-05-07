package cn.e3mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.event.CaretListener;

import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;

@Controller
public class CartController {

	@Value("${COOKIE_CART_EXPIRE}")
	private Integer COOKIE_CART_EXPIRE;
	@Autowired
	CartService cartService;
	@Autowired
	private ItemService itemService;
	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId,@RequestParam(defaultValue="1") Integer num,
			HttpServletRequest request,HttpServletResponse response) {
		//判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		//如果是，把购物车写入redis
		if (user!=null) {
			//保存到服务器
			cartService.addCart(user.getId(), itemId, num);
			//返回逻辑试图
			
			return "cartSuccess";
			
		}
		
		
		List<TbItem> cartList=getCartListFromCookie(request);
		boolean flag=false;
		for (TbItem tbItem : cartList) {
			if (tbItem .getId()==itemId.longValue()) {
				flag=true;
				tbItem.setNum(num+tbItem.getNum());
				break;
			}
		}

		if (!flag) {
			TbItem tbItem=itemService.getItemById(itemId);
			tbItem.setNum(num);
			String image=tbItem.getImage();
			if (image!=null &&image!="") {
				tbItem.setImage(image.split(",")[0]);
			}
			cartList.add(tbItem);
		}

		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList),COOKIE_CART_EXPIRE,true);
		return "cartSuccess";
	}


	public List<TbItem> getCartListFromCookie(HttpServletRequest request){
		String json=CookieUtils .getCookieValue(request, "cart", true);
		if (json==null || json=="") {
			return new ArrayList<>();
		}
		List<TbItem> list = JsonUtils .jsonToList(json, TbItem .class);
		return list;
	}

	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request,HttpServletResponse response) {
		List<TbItem> cartList = getCartListFromCookie(request);
		//判断是否登录，如果登录
		TbUser user = (TbUser) request.getAttribute("user");
		if (user!=null) {
			//从cookie中获取购物车列表
			//如果不为空，把cookie中的购物车商品和服务端的购物车合并
			cartService.mergeCart(user.getId(), cartList);
			//把cookie中的购物车删除
			CookieUtils.deleteCookie(request, response, "cart");
			//从服务端获取购物车列表
			cartList = cartService.getCartList(user.getId());
		}
		
		
		//如果没有登录
		
		request.setAttribute("cartList", cartList);
		return "cart";
	}

	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updateCartNum(@PathVariable Long itemId,@PathVariable Integer num,
			HttpServletRequest request,HttpServletResponse response) {
		TbUser user=(TbUser) request.getAttribute("user");
		if (user!=null) {
			cartService.updateCartNum(user.getId(), itemId, num);
			return E3Result.ok();
					
		}
		
		
		List<TbItem> cartList = getCartListFromCookie(request);
		for (TbItem tbItem : cartList) {
			if (tbItem.getId()==itemId.longValue()) {
				tbItem.setNum(num);
				break;
				
			}
			
		}
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList),COOKIE_CART_EXPIRE,true);
		return E3Result.ok();
	}

	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId,
			HttpServletRequest request,HttpServletResponse response) {
		
		
		TbUser user=(TbUser) request.getAttribute("user");
		if (user!=null) {
			cartService.deleteCartItem(user.getId(), itemId);
			return "redirect:/cart/cart.html";
					
		}
		
		
		List<TbItem> cartList = getCartListFromCookie(request);
		for (TbItem tbItem : cartList) {
			if (tbItem.getId()==itemId.longValue()) {
				cartList.remove(tbItem);
				break;
				
			}
			
		}
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList),COOKIE_CART_EXPIRE,true);
		return "redirect:/cart/cart.html";

}
}
