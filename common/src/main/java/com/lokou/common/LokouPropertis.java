package com.lokou.common;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.support.PropertiesLoaderUtils;

public class LokouPropertis {
	Properties props;
	private LokouPropertis(){
		props = new Properties(); 
		 try {
			props=PropertiesLoaderUtils.loadAllProperties("configs.properties");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	 private static	class  LokouPropertisHolder{
		
		public static LokouPropertis  instance=new LokouPropertis();
	}
	 
	public static LokouPropertis getLokouPropertis(){
		
		return LokouPropertisHolder.instance;
		
	}
	public Object getProperty(String key){
		return props.get(key);
		
	}
	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}
}
