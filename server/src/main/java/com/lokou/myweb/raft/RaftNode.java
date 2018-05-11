package com.lokou.myweb.raft;
import java.net.ConnectException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.lokou.common.HttpClientUtil;
import com.lokou.common.Server;
public class RaftNode {
	private RaftNodeState NODE_STATE;// 节点当前状态
	public Server getLocal() {
		return local;
	}

	public void setLocal(Server local) {
		this.local = local;
	}
	private int currentTerm;// 当前的任期
	private String voteForId;// 在当前获得选票的候选人的Id
	private String leaderId; // 当选人（领导人Id）
	private Server local;
	private Logger raftLogger = LoggerFactory.getLogger("raft");
	public RaftNodeState getNODE_STATE() {
		return NODE_STATE;
	}
	public void setNODE_STATE(RaftNodeState nODE_STATE) {
		NODE_STATE = nODE_STATE;
	}

	public int getCurrentTerm() {
		return currentTerm;
	}

	public void setCurrentTerm(int currentTerm) {
		this.currentTerm = currentTerm;
	}

	public String getVoteForId() {
		return voteForId;
	}

	public void setVoteForId(String voteForId) {
		this.voteForId = voteForId;
	}

	public String getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
	}

	public void increaTerms() {

		currentTerm++;
	}

	public void init() {
		this.setNODE_STATE(RaftNodeState.RFT_NODE_FOLLOWER_STATE);
	}

	public void becomeCanditor() {
		this.setNODE_STATE(RaftNodeState.RFT_NODE_CANDIDATE_STATE);
	}

	public void sendVoteRequest(Map<String, Server> severs) {
		raftLogger.info("向其他节点发送拜票请求，当前任期"+this.currentTerm);
		if (this.getNODE_STATE() != RaftNodeState.RFT_NODE_CANDIDATE_STATE) {
			becomeCanditor();
		}
		increaTerms();
		this.setVoteForId(this.local.getId());
		RaftRequest voterquest = new RaftRequest();
		voterquest.setServerId(this.getVoteForId());
		voterquest.setType(RequestType.VOTE_FOR_REQUEST.getType());
		voterquest.setCurrentTerm(this.getCurrentTerm());
		Integer agreeNum = 1;
		Integer term = 0;
		for (String serverId : severs.keySet()) {
			if (serverId.equals(this.local.getId())) {
				continue;
			}
			Server server = severs.get(serverId);
			String voteResponsestr = "";
			try {
				voteResponsestr = HttpClientUtil.httpPost(
						RaftNodeURLS.VOTEREQUESTURL.replaceAll("hosts",
								server.getHost()).replaceAll("port",
								"" + server.getPort()), voterquest);
			} catch (ConnectException e) {
				continue;
			}
			RaftResponse response = JSON.parseObject(voteResponsestr,
					RaftResponse.class);
			raftLogger.info("["+response.getServerId()+"]节点返回拜票结果["+response.getContent()+"]以及它的任期"+"["+response.getCurrentTerm()+"]"+this.currentTerm);
			if (response.getContent() != null) {
				boolean agree = Boolean.parseBoolean(response.getContent()
						.toString());
				if (response.getCurrentTerm() <= currentTerm && agree) {
					agreeNum++;
				}
			}
			term = response.getCurrentTerm();
		}
		if (agreeNum > (severs.size() / 2 + 1)) {
			becomeLeader();
		}
		if (currentTerm >term) {
			becomeFollower();
		}

	}
	public void becomeLeader() {
		raftLogger.info("本节点["+this.local.getId()+"]成为leader节点");
		this.setLeaderId(this.local.getId());
		this.setNODE_STATE(RaftNodeState.RFT_NODE_LEADER_STATE);
	}

	public void becomeFollower() {
		this.setNODE_STATE(RaftNodeState.RFT_NODE_FOLLOWER_STATE);

	}

	public void sendHeartBeat(Map<String, Server> severs) {
		raftLogger.info("本届点向其他节点发送心跳信息");
		if (this.getNODE_STATE() == RaftNodeState.RFT_NODE_LEADER_STATE) {
			RaftRequest heartBeatRequest = new RaftRequest();
			heartBeatRequest.setType(RequestType.HEART_BEAT.getType());
			heartBeatRequest.setCurrentTerm(currentTerm);
			String voteResponsestr = "";
				for (String serverId : severs.keySet()) {
					if (serverId.equals(this.local.getId())) {
						continue;
					}
					Server server = severs.get(serverId);
					try {
						voteResponsestr = HttpClientUtil.httpPost(
								RaftNodeURLS.HEARTBEATREQUESTURL.replaceAll("hosts",
										server.getHost()).replaceAll("port",
										"" + server.getPort()), heartBeatRequest);
					} catch (ConnectException e) {
						continue;
					}
					RaftResponse response = JSON.parseObject(voteResponsestr,
							RaftResponse.class);
					Boolean issuccess=(Boolean) response.getContent();
					if (!issuccess) {
						this.setCurrentTerm(response.getCurrentTerm());
						this.setNODE_STATE(RaftNodeState.RFT_NODE_FOLLOWER_STATE);
					}
				}		
		}
	}

	public void appendEntries() {

	}

	private RaftNode() {
		local =Servers.getInstance().getLocoal();
		this.init();
	}

	static class staticRaftNodeHolder {

		public static RaftNode instance = new RaftNode();

	}

	public static synchronized RaftNode getRaftNode() {
		
		return staticRaftNodeHolder.instance;
	}

}
