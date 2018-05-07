package cn.e3mall.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

//全局异常处理器
public class GlobalExceptionResolver implements HandlerExceptionResolver{

   private static final Logger logger=LoggerFactory.getLogger(GlobalExceptionResolver.class);
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object arg2,
			Exception ex) {
		// TODO Auto-generated method stub
		//打印控制台
		ex.printStackTrace();
		//写日志
		logger.debug("测试输出的日志");
		logger.info("系统发生了异常");
		logger.error("系统发生了异常",ex);
		//发邮件，发短信
		//错误页面
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.setViewName("error/exception");
		
		return modelAndView;
	}

}
