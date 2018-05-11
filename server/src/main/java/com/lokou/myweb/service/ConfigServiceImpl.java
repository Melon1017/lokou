package com.lokou.myweb.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import com.lokou.myweb.entity.Config;
import com.lokou.common.Constant;
@Service("ConfigService")
public class ConfigServiceImpl implements ConfigService{
	@Autowired
    private JedisPool jedisPool;//注入ShardedJedisPool
	/* (non Javadoc)
	
	 * @Title: addConfig
	
	 * @Description: TODO
	
	 * @param config
	 * @return
	
	 * @see com.zhaojiaocheng.myweb.service.ConfigService#addConfig(com.zhaojiaocheng.myweb.entity.Config)
	
	 */
	public Config addConfig(Config config) throws Exception {
		// TODO Auto-generated method stub
		Jedis jedis = jedisPool.getResource();
		config.setConfigValue(URLEncoder.encode(config.getConfigValue(), "UTF-8"));
		jedis.hset(config.getGroupId(), config.getConfigKey(), config.getConfigValue());
		jedis.publish("config_change", config.getGroupId()+":"+config.getConfigKey()+":"+config.getConfigValue());
		return config;
	}

	/* (non Javadoc)
	
	 * @Title: findConfigByKeyAndGroupId
	
	 * @Description: TODO
	
	 * @return
	
	 * @see com.zhaojiaocheng.myweb.service.ConfigService#findConfigByKeyAndGroupId()
	
	 */
	public Config findConfigByKeyAndGroupId(Config config) throws Exception {
		// TODO Auto-generated method stub
		Jedis jedis = jedisPool.getResource();
		String configValue=jedis.hget(config.getGroupId(), config.getConfigKey());
		config.setConfigValue(URLDecoder.decode(configValue,"UTF-8"));
		return config;
	}

	/* (non Javadoc)
	
	 * @Title: editConfigByKeyAndGroupId
	
	 * @Description: TODO
	
	 * @param config
	 * @return
	
	 * @see com.zhaojiaocheng.myweb.service.ConfigService#editConfigByKeyAndGroupId(com.zhaojiaocheng.myweb.entity.Config)
	
	 */
	public Config editConfigByKeyAndGroupId(Config config) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<Config> searchConfig(Config config) {
		// TODO Auto-generated method stub
		Jedis jedis = jedisPool.getResource();
		List<Config> configs=new ArrayList<Config>();
		if(config.getGroupId()!=null&&!"".equals(config.getGroupId())){
			Set<String> keys=jedis.keys(config.getGroupId());
			for(String key:keys){
				Set<String> fileds=jedis.hkeys(key);
				for(String filed:fileds){
					Config resultConfig=new Config();
					resultConfig.setGroupId(key);
					resultConfig.setConfigKey(filed);
					configs.add(resultConfig);
				}
			}
		}else {
			Set<String> keys=jedis.keys(Constant.GROUPID_PREFIX);
			for(String key:keys){
				Set<String> fileds=jedis.hkeys(key);
				for(String filed:fileds){
					if(filed.contains(config.getConfigKey())){
						Config resultConfig=new Config();
						resultConfig.setGroupId(key);
						resultConfig.setConfigKey(filed);
						configs.add(resultConfig);
					}
				}
			}
			
		}
		return configs;
	}
	
}
