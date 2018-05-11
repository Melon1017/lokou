package com.lokou.myweb.controller.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lokou.common.CommonResult;
import com.lokou.common.Server;
import com.lokou.myweb.entity.Config;
import com.lokou.myweb.raft.RaftNode;
import com.lokou.myweb.raft.Servers;
import com.lokou.myweb.service.ConfigService;
@Controller
@RequestMapping("api")
public class ConfigApiController {
	@Autowired
	ConfigService configService;
	@RequestMapping("getConfig")
	/**
	 * 获取配置内容接口
	 * @param model
	 * @param groupId
	 * @param configKey
	 * @return
	 */
	public @ResponseBody CommonResult<Config> detail(Model model,@RequestParam("groupId") String groupId,@RequestParam("configKey") String configKey) {
		CommonResult<Config> result=new CommonResult<Config>();
		Config config=new Config(); 
		try {
			
			config.setGroupId(groupId);
			config.setConfigKey(configKey);
			config=configService.findConfigByKeyAndGroupId(config);
			result.setStatus(true);
			result.setMsg("成功");
			result.setData(config);
		} catch (Exception e) {
			result.setStatus(false);
			result.setMsg("失败"+e.getMessage());
			result.setData(null);
		} 
		return result; 
	}
	/**
	 * 获取所有配置服务器的接口
	 * @param model
	 * @param groupId
	 * @param configKey
	 * @return
	 */
	@RequestMapping("servers")
	public @ResponseBody CommonResult<List<Server>> getAllServers(){
		CommonResult<List<Server>> result=new CommonResult<List<Server>>();
		List<Server> servers=new ArrayList<Server>();
		try{
			
			for(String serverId:Servers.getInstance().getServers().keySet()){
				Server server=Servers.getInstance().getServers().get(serverId);
				servers.add(server);
			};
			result.setStatus(true);
			result.setMsg("成功");
			result.setData(servers);
		}catch(Exception e){
			result.setStatus(false);
			result.setMsg("失败"+e.getMessage());
			result.setData(null);
		}
		
		return result;
	}
	/**
	 * 获取leader服务器信息的接口
	 * @param model
	 * @param groupId
	 * @param configKey
	 * @return
	 */
	@RequestMapping("leader")
	public @ResponseBody CommonResult<Server> leaderServer(){
		CommonResult<Server> result=new CommonResult<Server>();
		try{
			String leaderId=RaftNode.getRaftNode().getLeaderId();
			Server server=Servers.getInstance().getServers().get(leaderId);
			result.setStatus(true);
			result.setMsg("成功");
			result.setData(server);
		}catch(Exception e){
			result.setStatus(false);
			result.setMsg("失败"+e.getMessage());
			result.setData(null);
			
		}
		return result;
	}
	
	
}
