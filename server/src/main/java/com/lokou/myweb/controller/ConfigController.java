package com.lokou.myweb.controller;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lokou.myweb.entity.Config;
import com.lokou.myweb.service.ConfigService;

@Controller
@RequestMapping("config")
public class ConfigController {
	@Autowired
	ConfigService configService;
	private Logger logger = LoggerFactory.getLogger(ConfigController.class);
	@RequestMapping("add")
	public String addConfig(Model model,@RequestParam("groupId") String groupId,@RequestParam("configKey") String configKey,@RequestParam("configValue") String configValue ) {
		Config config=new Config(); 
		try {
			config.setGroupId(groupId);
			config.setConfigValue(configValue);
			config.setConfigKey(configKey);
			configService.addConfig(config);
			config.setConfigValue(URLDecoder.decode(config.getConfigValue(),"UTF-8"));
		} catch (Exception e) {
		    logger.error("系统出现异常 params["+"groupId="+groupId+",configKey="+configKey+",configValue="+configValue+"]", e);
			model.addAttribute("error", e.getMessage());  
			return "/error/error"; 
		}
		model.addAttribute("contentMain", "edit.jsp");
		model.addAttribute("config", config);  
		return "/console/portal"; 
	}
	
	@RequestMapping("show")
	public String show(Model model) {
		model.addAttribute("contentMain", "show.jsp");  
		return "/console/portal"; 
	}
	
	@RequestMapping("detail")
	public String detail(Model model,@RequestParam("groupId") String groupId,@RequestParam("configKey") String configKey) {
		Config config=new Config(); 
		try {
			
			config.setGroupId(groupId);
			config.setConfigKey(configKey);
			config=configService.findConfigByKeyAndGroupId(config);
		} catch (Exception e) {
			logger.error("系统出现异常 params["+"groupId="+groupId+",configKey="+configKey+"]", e);
			model.addAttribute("error", e.getMessage());  
			return "/error/error"; 
		}
		model.addAttribute("contentMain", "edit.jsp");
		model.addAttribute("config", config);  
		return "/console/portal"; 
	}
	@RequestMapping("search")
	public String search(Model model,@RequestParam(value="groupId",required=false) String groupId,@RequestParam(value="configKey",required=false) String configKey) {
		Config config=new Config(); 
		List<Config>	configs=new ArrayList<Config>();
		try {
			config.setGroupId(groupId);
			config.setConfigKey(configKey);
			if(config.getGroupId()==null&&config.getConfigKey()==null){
				model.addAttribute("error", "请至少输入一个查询条件");  
				return "/error/error"; 
			}
			configs=configService.searchConfig(config);
		} catch (Exception e) {
			logger.error("系统出现异常 params["+"groupId="+groupId+",configKey="+configKey+"]", e);
			model.addAttribute("error", e.getMessage());  
			return "/error/error"; 
		}
		model.addAttribute("contentMain", "configs.jsp");
		model.addAttribute("configs", configs);  
		return "/console/portal"; 
	}
	
	@RequestMapping("addnew")
	public String search(Model model) {
		model.addAttribute("contentMain", "edit.jsp");
		model.addAttribute("editType", "新增");
		return "/console/portal"; 
	}

}
