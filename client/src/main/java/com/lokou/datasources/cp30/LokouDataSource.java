package com.lokou.datasources.cp30;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Referenceable;

import com.lokou.common.Constant;
import com.lokou.config.ConfigCenter;
import com.lokou.exceptions.GetConfigException;
import com.lokou.exceptions.SetConfigException;
import com.mchange.v2.c3p0.AbstractComboPooledDataSource;
public final  class LokouDataSource extends    AbstractComboPooledDataSource implements Serializable, Referenceable{
	private String profile;
	protected volatile boolean   inited= false;
	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	/**
	 * 初始化数据库配置
	 * <property name="driverClass" value="com.mysql.jdbc.Driver" />
		<property name="jdbcUrl" value="jdbc" />
		<property name="user" value="root" />
		<property name="password" value="usbw" />
		<property name="initialPoolSize" value="40" />
		<property name="minPoolSize" value="30" />
		<property name="maxPoolSize" value="500" />
		<property name="maxIdleTime" value="5000" />
		<property name="acquireIncrement" value="3" />
		<property name="checkoutTimeout" value="60000" />
	 */
	public void init(){
		if(inited){
			return;
		}
		String dbConfig;
		try {
			if(this.getProfile()==null){
				
				throw new GetConfigException();
			}
			dbConfig = ConfigCenter.getConfig(this.getProfile(),
					Constant.DBCONFIGK_KEY);
			Map<String, String> dbConfigMap = dbConfigStr2Map(dbConfig);
			if (dbConfigMap.get("driverClass") != null
					&& dbConfigMap.get("jdbcUrl") != null
					&& dbConfigMap.get("user") != null
					&& dbConfigMap.get("password") != null) {
				this.setUser(dbConfigMap.get("user"));
				this.setJdbcUrl(dbConfigMap.get("jdbcUrl"));
				this.setDriverClass(dbConfigMap.get("driverClass"));
				this.setPassword(dbConfigMap.get("password"));
			} else {

				throw new SetConfigException();

			}
			if(dbConfigMap.get("initialPoolSize")!=null) {
				this.setInitialPoolSize(Integer.parseInt(dbConfigMap.get("initialPoolSize")));
			}
			if(dbConfigMap.get("minPoolSize")!=null) {
				this.setMinPoolSize(Integer.parseInt(dbConfigMap.get("minPoolSize")));
			}
			if(dbConfigMap.get("maxPoolSize")!=null) {
				this.setMaxPoolSize(Integer.parseInt(dbConfigMap.get("maxPoolSize")));
			}
			if(dbConfigMap.get("maxIdleTime")!=null) {
				this.setMaxIdleTime(Integer.parseInt(dbConfigMap.get("maxIdleTime")));
			}
			if(dbConfigMap.get("acquireIncrement")!=null) {
				this.setAcquireIncrement(Integer.parseInt(dbConfigMap.get("acquireIncrement")));
			}
			if(dbConfigMap.get("checkoutTimeout")!=null) {
				this.setCheckoutTimeout(Integer.parseInt(dbConfigMap.get("checkoutTimeout")));
			}
			inited=true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public LokouDataSource()
	    { super();}

	    public LokouDataSource( boolean autoregister )
	    { super( autoregister ); }

	    public LokouDataSource(String configName)
	    { super( configName );  }


	    // serialization stuff -- set up bound/constrained property event handlers on deserialization
	    private static final long serialVersionUID = 1;
	    private static final short VERSION = 0x0002;

	    private void writeObject( ObjectOutputStream oos ) throws IOException
	    {
	        oos.writeShort( VERSION );
	    }

	    private void readObject( ObjectInputStream ois ) throws IOException, ClassNotFoundException
	    {
	        short version = ois.readShort();
	        switch (version)
	        {
	        case VERSION:
		    //ok
	            break;
	        default:
	            throw new IOException("Unsupported Serialized Version: " + version);
	        }
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
}
