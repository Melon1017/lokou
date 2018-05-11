package com.lokou.myweb.raft;

public class RaftResponse {
	private Integer type;
	private Object content;
	private String serverId;
	private Integer currentTerm;
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public Integer getCurrentTerm() {
		return currentTerm;
	}
	public void setCurrentTerm(Integer currentTerm) {
		this.currentTerm = currentTerm;
	}
	
}
