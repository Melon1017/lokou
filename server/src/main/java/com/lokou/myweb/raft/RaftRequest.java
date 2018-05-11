package com.lokou.myweb.raft;

public class RaftRequest {
	private String  serverId;
	private Integer type;
	private String content;
	private Integer currentTerm;
	public Integer getCurrentTerm() {
		return currentTerm;
	}
	public void setCurrentTerm(Integer currentTerm) {
		this.currentTerm = currentTerm;
	}
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
