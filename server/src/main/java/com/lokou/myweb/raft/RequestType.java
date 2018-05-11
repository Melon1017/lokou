package com.lokou.myweb.raft;

public enum RequestType {
	VOTE_FOR_REQUEST("vote request",1),HEART_BEAT("heart beart",2),LOGENTRIS_APPEND("log entries append",3);
	
	public String getDesc() {
		return desc;
	}
	public int getType() {
		return type;
	}
	private RequestType(String desc,int type){
		this.desc=desc;
		this.type=type;
		
	}
	private final String desc;
	private final int type;
}
