package com.lokou.config;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import redis.clients.jedis.JedisPubSub;

public class ConfigSubPubListener extends JedisPubSub {
	@Override
	public void onMessage(String channel, String message) {
		// TODO Auto-generated method stub
		System.out.println(message);
		String[] changesArray=message.split(":");
		try {
			ConfigChangeMonitor.keyChangeNotify(changesArray[1], URLDecoder.decode(changesArray[2], "UTF-8"));
			super.onMessage(channel, message);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void onPMessage(String pattern, String channel, String message) {
		// TODO Auto-generated method stub
		super.onPMessage(pattern, channel, message);
	}
	
}
