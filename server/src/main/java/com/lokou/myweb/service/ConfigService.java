package com.lokou.myweb.service;

import java.util.List;

import com.lokou.myweb.entity.Config;

public interface ConfigService{
	/**增加配置*/
	public Config addConfig(Config config) throws Exception;
	/**查询配置*/
	public Config findConfigByKeyAndGroupId(Config config) throws Exception;
	/**编辑配置*/
	public Config editConfigByKeyAndGroupId(Config config);
	/**查询配置*/
	public List<Config> searchConfig(Config config);
}
