package cn.e3mall.order.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;

@Controller
public class OrderController {

	
	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;
    @RequestMapping("/order/order-cart")
    public String showOrderCart(HttpServletRequest request) {
    	TbUser user=(TbUser) request.getAttribute("user");
        //根据用户id获取收货地址列表，取静态数据
    	
    	//取支付方式，取静态数据
    	List<TbItem> cartList = cartService.getCartList(user.getId());
        request.setAttribute("cartList", cartList);
    
        return "order-cart";
    
    }
    
    @RequestMapping(value="/order/create",method=RequestMethod.POST)
    public String createOrder(OrderInfo orderInfo,HttpServletRequest request) {
    	//取用户信息，并添加到orderinfo
    	TbUser user = (TbUser) request.getAttribute("user");
    	orderInfo.setUserId(user.getId());
    	orderInfo.setBuyerNick(user.getUsername());
    	//调用服务生成订单
    	E3Result e3Result = orderService.createOrder(orderInfo);
    	//如果订单生成成功，删除购物车
    	if (e3Result.getStatus()==200) {
			cartService.clearCartList(user.getId());
		}
    	//把订单号传给页面
    	request.setAttribute("orderId", e3Result.getData());
    	request.setAttribute("payment",orderInfo.getPayment());
    	//返回逻辑视图
    	return "success";
    }
   
    
    
    
    
    
    
    
    
}
