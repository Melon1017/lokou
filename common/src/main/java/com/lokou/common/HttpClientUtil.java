package com.lokou.common;

import java.net.ConnectException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
public  class HttpClientUtil{
	/**
	 * 发送Get请求
	 * @Title: HttpGet
	 * @Description: TODO
	 * @param url
	 * @param param
	 * @return
	 * @return: String
	 */
	public static String HttpGet(String url,Map<String,Object>param){
		
		 CloseableHttpClient httpclient = HttpClients.createDefault();
		 String result="";
		 try {
			  if(param!=null&&!param.isEmpty()){
				  url+="?";
				  for(String key:param.keySet()){
						 url+=key+"="+param.get(key)+"&";
					 } 
				  url=url.substring(0,url.lastIndexOf("&"));
			  }
			  HttpGet request = new HttpGet(url);//这里发送get请求
			  CloseableHttpResponse  response = httpclient.execute(request);
			 	if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			 		result= EntityUtils.toString(response.getEntity(),"utf-8");
			 	} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       return result;
	}
	/**
	 * 发送post请求
	 * @Title: httpPost
	 * @Description: TODO
	 * @param url
	 * @param param
	 * @return
	 * @return: String
	 */
	public static String httpPost(String url,Map<String,Object>param){
		
		 HttpPost request = new HttpPost(url);//这里发送Post请求
		 CloseableHttpClient httpclient = HttpClients.createDefault();
		 String result="";
		 try {
			  HttpEntity entity=new StringEntity(JSON.toJSONString(param));
			  request.setEntity(entity);
			  CloseableHttpResponse  response = httpclient.execute(request);
			 	if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			 		result= EntityUtils.toString(response.getEntity(),"utf-8");
			 	} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      return result;
	}
	/**
	 * 发送post请求
	 * @Title: httpPost
	 * @Description: TODO
	 * @param url
	 * @param param
	 * @return
	 * @return: String
	 */
	public static String httpPost(String url,Object content)throws ConnectException{
		
		 HttpPost request = new HttpPost(url);//这里发送Post请求
		 CloseableHttpClient httpclient = HttpClients.createDefault();
		 String result="";
		 try {
			  HttpEntity entity=new StringEntity(JSON.toJSONString(content),"UTF-8");
			  request.setEntity(entity);
			  request.setHeader("Content-Type", "application/json");
			  request.setHeader("charset", "UTF-8");
			  CloseableHttpResponse  response = httpclient.execute(request);
			 	if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			 		result= EntityUtils.toString(response.getEntity(),"utf-8");
			 	}else {
			 		throw new ConnectException();
			 	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new ConnectException();
		}
      return result;
	}
}
