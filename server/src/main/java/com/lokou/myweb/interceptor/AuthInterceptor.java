package com.lokou.myweb.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AuthInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		 if(request.getSession().getAttribute("userName") != null) {  
		        //更好的实现方式的使用cookie  
		        return true;  
		   }
		 response.sendRedirect(request.getContextPath() + "/login");  
		 return false;  
	}
}
