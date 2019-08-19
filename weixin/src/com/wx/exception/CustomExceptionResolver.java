package com.wx.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

//全局异常处理器
public class CustomExceptionResolver implements HandlerExceptionResolver{
 
	//系统抛出的异常
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		//handler就是处理器适配器要执行的Handler对象(只有method)
		//解析出异常类型。
		
	    //如果该 异常类型是系统 自定义的异常，直接取出异常信息，在错误页面展示。
		CustomException customException=null;
		if(ex instanceof CustomException){
			customException=(CustomException)ex;
	    	
	    }else{
	    	//如果该 异常类型不是系统 自定义的异常，构造一个自定义的异常类型（信息为“未知错误”）。
	    	customException=new CustomException(ex.getMessage());
	    }
 
		//错误信息
		String message=customException.getMessage();
		
		ModelAndView modelAndView=new ModelAndView();
		
		//将错误信息传到页面
		modelAndView.addObject("message",message);
		
		//指向到错误界面
		modelAndView.setViewName("/public/error.jsp");
		
		return modelAndView;
	}
	 
}

