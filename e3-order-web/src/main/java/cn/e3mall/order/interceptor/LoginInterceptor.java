package cn.e3mall.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

public class LoginInterceptor implements HandlerInterceptor{

	
	@Value("${SSO_URL}")
	private String SSO_URL;
	
	@Autowired
	private TokenService tokenService;
	@Autowired
	private CartService catService;
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean preHandle(HttpServletRequest res, HttpServletResponse resp, Object handler) throws Exception {
		// TODO Auto-generated method stub
		String token=CookieUtils.getCookieValue(res, "token");
		if (token==null ||token==null) {
			resp.sendRedirect(SSO_URL + "/page/login?redirect=" + res.getRequestURL());
			return false;
		}
		
		E3Result e3Result=tokenService.getUserByToken(token);
		if (e3Result.getStatus()!=200) {
			resp.sendRedirect(SSO_URL + "/page/login?redirect=" + res.getRequestURL());
			return false;
		}
		
		TbUser user=(TbUser) e3Result.getData();
		res.setAttribute("user", user);
		String jsonCartList = CookieUtils.getCookieValue(res, "cart",true);
		if (jsonCartList!=null&&jsonCartList!="") {
			catService.mergeCart(user.getId(),JsonUtils.jsonToList(jsonCartList, TbItem.class));
		}
		
		
		return true;
	}

}
