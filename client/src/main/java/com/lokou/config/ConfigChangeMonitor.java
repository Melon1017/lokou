package com.lokou.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.lokou.common.Constant;
import com.lokou.listenner.ConfigListenner;

public class ConfigChangeMonitor implements Runnable {
	private JedisPool jedisPool;// 注入ShardedJedisPool
	private Map<String,ConfigListenner> changeListenner;
	static ThreadLocal<Boolean> started=new ThreadLocal<Boolean>();
	public ConfigChangeMonitor() {
		try {
			/**
			 * 获取redis配置
			 */
			String configContent = ConfigCenter.getConfig("SYSTEM",
					Constant.REDISCONFIGK_KEY);
			Map<String, String> configMap = this.configStr2Map(configContent);
			JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
			Integer maxTotal = configMap.get("maxTotal") != null ? Integer
					.parseInt(configMap.get("maxTotal")) : 5000;
			Integer maxIdle = configMap.get("maxIdle") != null ? Integer
					.parseInt(configMap.get("maxIdle")) : 5000;
			Boolean testOnBorrow = configMap.get("testOnBorrow") != null ? Boolean
					.parseBoolean(configMap.get("testOnBorrow")) : true;
			Boolean testOnReturn = configMap.get("testOnReturn") != null ? Boolean
					.parseBoolean(configMap.get("testOnReturn")) : true;
			jedisPoolConfig.setMaxTotal(maxTotal);
			jedisPoolConfig.setMaxIdle(maxIdle);
			jedisPoolConfig.setTestOnBorrow(testOnBorrow);
			jedisPoolConfig.setTestOnReturn(testOnReturn);
			if (configMap.get("host") != null && configMap.get("port") != null) {
				Integer timeout = configMap.get("port") != null ? Integer
						.parseInt(configMap.get("port")) : 30000;

				jedisPool = new JedisPool(jedisPoolConfig,
						configMap.get("host"), Integer.parseInt(configMap
								.get("port")), timeout,
						configMap.get("password"));
				System.out.println("配置变化监听器初始化");
			}
			changeListenner=new ConcurrentHashMap<String,ConfigListenner>();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		final ConfigSubPubListener listener = new ConfigSubPubListener();
		jedisPool.getResource().subscribe(listener, "config_change");
		started.set(true);
	}
	private static class ConfigMonitorHolder {
		public static ConfigChangeMonitor instance = new ConfigChangeMonitor();
		public static void startConfigMonitor(){  
			if(started.get()!=null&&!started.get()){
				Thread monitorThread=new Thread(instance);
				monitorThread.start();
			}
		}
	}
	public  void addKeyChangeListener(String key,ConfigListenner listenner){
		changeListenner.put(key,listenner);
	}
	public static void getConfigMonitor() {
		ConfigMonitorHolder.startConfigMonitor();
	}
	
	public  void notifyAllListenner(String key,String changedValue) {
		ConfigListenner listenner=changeListenner.get(key);
		listenner.recieveChangedKey(key, changedValue);
	}
	public static void addChangeListenner(String key,ConfigListenner listenner) {
		ConfigMonitorHolder.instance.addKeyChangeListener(key,listenner);
	}
	
	public static void keyChangeNotify(String key,String changedValue){
		ConfigMonitorHolder.instance.notifyAllListenner(key,changedValue);
	}
	private Map<String, String> configStr2Map(String dConfigStr) {
		String[] dbArrays = dConfigStr.split("\r\n");
		Map<String, String> configMap = new HashMap<String, String>();
		for (int i = 0; i < dbArrays.length; i++) {
			String dbSttrElement = dbArrays[i];
			String key = dbSttrElement.replaceFirst("=.*", "");
			String value = dbSttrElement.replaceFirst(key + "=", "");
			configMap.put(key, value);
		}
		return configMap;

	}
}
