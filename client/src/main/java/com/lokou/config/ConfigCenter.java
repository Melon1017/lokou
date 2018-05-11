package com.lokou.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lokou.common.CommonResult;
import com.lokou.common.Constant;
import com.lokou.common.Server;
import com.lokou.entity.Config;
import com.lokou.exceptions.GetConfigException;
import com.lokou.listenner.ConfigListenner;
import com.lokou.uitl.HttpClientUtil;

/**
 * 
 * @ClassName: ConfigCenter
 * @Description: 从配置中心获取配置入口
 * @author: Xing.liu
 * @date: 2018年2月7日 下午5:38:31
 */
public class ConfigCenter {
	private final String host = "http://get.lokou.net";
	private final int port = 5233;
	private Map<String, String> localConfigs;
	private List<Server> configServers;
	private Server leader;

	public Map<String, String> getLocalConfigs() {
		return localConfigs;
	}

	public void setLocalConfigs(Map<String, String> localConfigs) {
		this.localConfigs = localConfigs;
	}

	private ConfigCenter() {

		localConfigs = new HashMap<String, String>();
		try {
			getClusterInfo();
			getLeaderInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取配置服务器集群信息
	 */
	private void getClusterInfo() throws GetConfigException {
		try {
			String url = host + ":" + port + "/lokou/api/servers";
			String result = HttpClientUtil.HttpGet(url, null);
			CommonResult<List<Server>> serverResult = JSON.parseObject(result,
					new TypeReference<CommonResult<List<Server>>>() {
					});
			if (serverResult.isStatus()) {
				configServers = serverResult.getData();
			}
		} catch (Exception e) {
			throw new GetConfigException();
		}
	}

	private void getLeaderInfo() throws GetConfigException {
		try {
			String url = host + ":" + port + "/lokou/api/leader";
			String result = HttpClientUtil.HttpGet(url, null);
			CommonResult<Server> leaderResult = JSON.parseObject(result,
					new TypeReference<CommonResult<Server>>() {
					});
			if (leaderResult.isStatus()) {
				leader = leaderResult.getData();
			}
		} catch (Exception e) {
			throw new GetConfigException();
		}
	}
	/**
	 * 失效时转移到新的leader服务器
	 * @return
	 * @throws GetConfigException
	 */
	private boolean  failover() throws GetConfigException {
		
		for(Server server:configServers){
			try{
				String url = Constant.URLPREFIX + server.getHost() + ":"
					+ server.getPort() + "/lokou/api/leader";
				String result = HttpClientUtil.HttpGet(url, null);
				CommonResult<Server> leaderResult = JSON.parseObject(result,
					new TypeReference<CommonResult<Server>>() {
					});
				if (leaderResult.isStatus()) {
					leader = leaderResult.getData();
					return true;
				}
			}catch(IOException e){
				continue;
			}
			
		}
		return false;
}

	
	private static class ConfigCenterHolder {
		public static ConfigCenter instance = new ConfigCenter();

	}

	public static ConfigCenter getConfigCenter() {
		return ConfigCenterHolder.instance;
	}

	/***
	 * 获取配置内部调用方法
	 * 
	 * @param groupId
	 * @param configKey
	 * @return
	 * @throws Exception
	 */
	private String getConfigFromConfigCenter(String groupId, String configKey)
			throws Exception {
		String url = Constant.URLPREFIX + leader.getHost() + ":"
				+ leader.getPort() + "/lokou/api/getConfig";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("groupId", groupId);
		param.put("configKey", configKey);
		try {
			String result = HttpClientUtil.HttpGet(url, param);
			CommonResult<Config> configResult = JSON.parseObject(result,
					new TypeReference<CommonResult<Config>>() {
					});
			if (configResult.isStatus()) {
				Config config = configResult.getData();
				return config.getConfigValue();
			} else {

				throw new Exception();
			}
		} catch (IOException e) {
			/***
			 * 尝试从存储的服务器列表当中获取leader服务器的IP和端口号。
			 * 如果服务器列表当中所有的服务器都不可访问。则表示整个配置中心集群都不可用。或者客户端本身网络不可
			 */
			boolean failover=failover();
			if(failover){
				
				return getConfigFromConfigCenter(groupId,configKey);
			}
		}
		return null;

	}

	/**
	 * 获取配置内容
	 * 
	 * @param groupId
	 * @param configKey
	 * @return
	 * @throws Exception
	 */
	public static String getConfig(String groupId, String configKey)
			throws Exception {

		if (ConfigCenter.getConfigCenter().getLocalConfigs().get(configKey) != null) {
			return ConfigCenter.getConfigCenter().getLocalConfigs()
					.get(configKey);
		} else {
			String configContent = ConfigCenter.getConfigCenter()
					.getConfigFromConfigCenter(groupId, configKey);
			ConfigCenter.getConfigCenter().getLocalConfigs()
					.put(configKey, configContent);
			ConfigChangeMonitor.getConfigMonitor();
			return configContent;
		}

	}

	public static String getConfig(String groupId, String configKey,
			ConfigListenner changeListner) throws Exception {
		String configContent = getConfig(groupId, configKey);
		ConfigChangeMonitor.getConfigMonitor();
		ConfigChangeMonitor.addChangeListenner(configKey, changeListner);
		return configContent;

	}
}
