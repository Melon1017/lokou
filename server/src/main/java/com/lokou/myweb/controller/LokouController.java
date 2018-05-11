package com.lokou.myweb.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lokou.common.CommonResult;
import com.lokou.common.LokouUsers;
import com.lokou.common.User;

@Controller
@RequestMapping(value = "/", method = {RequestMethod.GET,RequestMethod.POST})
public class LokouController{
	@RequestMapping("console/portal")
	public String show(Model model,HttpServletRequest request) {
		model.addAttribute("contentMain", "show.jsp");
		String userName=(String) request.getSession().getAttribute("userName");
		model.addAttribute("userName", userName); 
		return "/console/portal"; 
	}
	@RequestMapping("login")
	public String login(Model model) {  
		return "/login"; 
	}
	@RequestMapping("loginAuth")
	public @ResponseBody CommonResult<User> loginAuth(Model model,HttpServletRequest request) { 
		String name=request.getParameter("name");
		String password=request.getParameter("password");
		CommonResult<User> result=new CommonResult<User>();
		if(!LokouUsers.userExists(name)){
			result.setMsg("用户不存在");
			result.setStatus(false);
			return result;
		}
		User user=LokouUsers.getUser(name);
		if(password.equals(user.getPassword())){
			request.getSession().setAttribute("userName", user.getUserName());
			result.setData(user);
			result.setMsg("登录成功");
			result.setStatus(true);
			
		}else {
			result.setMsg("登录失败用户名或密码错误");
			result.setStatus(false);
		}
		return result;
	}
}
