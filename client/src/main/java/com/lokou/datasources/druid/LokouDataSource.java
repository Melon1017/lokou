package com.lokou.datasources.druid;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.druid.pool.DruidDataSource;
import com.lokou.common.Constant;
import com.lokou.config.ConfigCenter;
import com.lokou.exceptions.GetConfigException;
import com.lokou.exceptions.SetConfigException;
import com.lokou.listenner.ConfigListenner;

public class LokouDataSource extends DruidDataSource implements ConfigListenner {
	private String profile;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	@Override
	/**
	 * 初始化数据库连接池
	 */
	public void init() throws SQLException {
		if(inited){
			
			return;
		}
		String dbConfig;
		try {
			if(this.getProfile()==null){
				
				throw new GetConfigException();
			}
			dbConfig = ConfigCenter.getConfig(this.getProfile(),
					Constant.DBCONFIGK_KEY,this);
			Map<String, String> dbConfigMap = dbConfigStr2Map(dbConfig);
			if (dbConfig != null) {
				if (dbConfigMap.get("driverClass") != null
						&& dbConfigMap.get("jdbcUrl") != null
						&& dbConfigMap.get("username") != null
						&& dbConfigMap.get("password") != null) {
					setDriverClassName(dbConfigMap.get("driverClass"));
					setUrl(dbConfigMap.get("jdbcUrl"));
					setUsername(dbConfigMap.get("username"));
					setPassword(dbConfigMap.get("password"));
				} else {

					throw new SetConfigException();

				}
				if (dbConfigMap.get("initialSize") != null) {
					setInitialSize(Integer.parseInt(dbConfigMap
							.get("initialSize")));
				}
				if (dbConfigMap.get("minIdle") != null) {
					setMinIdle(Integer.parseInt(dbConfigMap.get("minIdle")));
				}
				if (dbConfigMap.get("maxActive") != null) {
					setMaxActive(Integer.parseInt(dbConfigMap.get("maxActive")));
				}
				if (dbConfigMap.get("maxWait") != null) {
					setMaxWait(Long.parseLong(dbConfigMap.get("maxWait")));
				}
				if (dbConfigMap.get("timeBetweenEvictionRunsMillis") != null) {
					setTimeBetweenEvictionRunsMillis(Long.parseLong(dbConfigMap
							.get("timeBetweenEvictionRunsMillis")));
				}
				if (dbConfigMap.get("minEvictableIdleTimeMillis") != null) {
					setMinEvictableIdleTimeMillis(Long.parseLong(dbConfigMap
							.get("minEvictableIdleTimeMillis")));
				}
				if (dbConfigMap.get("validationQuery") != null) {
					setValidationQuery(dbConfigMap.get("validationQuery"));
				}
				if (dbConfigMap.get("testWhileIdle") != null) {
					setTestWhileIdle(Boolean.parseBoolean(dbConfigMap
							.get("testWhileIdle")));
				}
				if (dbConfigMap.get("testOnBorrow") != null) {
					setTestOnBorrow(Boolean.parseBoolean(dbConfigMap
							.get("testOnBorrow")));
				}
				if (dbConfigMap.get("testOnReturn") != null) {
					setTestOnReturn(Boolean.parseBoolean(dbConfigMap
							.get("testOnReturn")));
				}
				if (dbConfigMap.get("poolPreparedStatements") != null) {
					setPoolPreparedStatements(Boolean.parseBoolean(dbConfigMap
							.get("poolPreparedStatements")));
				}
				if (dbConfigMap
						.get("maxPoolPreparedStatementPerConnectionSize") != null) {
					setMaxPoolPreparedStatementPerConnectionSize(Integer
							.parseInt(dbConfigMap
									.get("maxPoolPreparedStatementPerConnectionSize")));
				}
				if (dbConfigMap
						.get("filters") != null) {
					setFilters(dbConfigMap
							.get("filters"));
				}
				super.init();
			} else {
				throw new GetConfigException();

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		super.close();
	}

	private Map<String, String> dbConfigStr2Map(String dbConfigStr) {
		String[] dbArrays = dbConfigStr.split("\r\n");
		Map<String, String> configMap = new HashMap<String, String>();
		for (int i = 0; i < dbArrays.length; i++) {
			String dbSttrElement = dbArrays[i];
			 String key=dbSttrElement.replaceFirst("=.*", "");
			    String value=dbSttrElement.replaceFirst(key+"=", "");
			configMap.put(key, value);
		}
		return configMap;

	}

	@Override
	public void recieveChangedKey(String key, String changedValue) {
		// TODO Auto-generated method stub
		inited=false;
		try {
			init();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
