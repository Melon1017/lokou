package com.lokou.myweb.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lokou.common.Constant;
import com.lokou.common.Server;
import com.lokou.myweb.raft.RaftNode;
import com.lokou.myweb.raft.RaftNodeState;
import com.lokou.myweb.raft.RaftRequest;
import com.lokou.myweb.raft.RaftResponse;
import com.lokou.myweb.raft.RaftTimer;
import com.lokou.myweb.raft.Servers;

@Controller
@RequestMapping("raft")
public class RaftController {
	private Logger raftlogger = LoggerFactory.getLogger("raft");
	@RequestMapping("voterquest")
	public @ResponseBody
	RaftResponse voteRequest(@RequestBody RaftRequest request) {
		raftlogger.info("拜票请请求节点id["+request.getServerId()+"]请求任期["+request.getCurrentTerm()+"]当前节点数任期["+RaftNode.getRaftNode().getCurrentTerm()+"]");
		RaftResponse response = new RaftResponse();
		if (request.getCurrentTerm() >= RaftNode.getRaftNode().getCurrentTerm()) {
			// 给拜票的候选人投票
			request.getCurrentTerm();
			response.setContent(true);
			response.setCurrentTerm(request.getCurrentTerm());
			response.setType(request.getType());
			RaftNode.getRaftNode().setVoteForId(request.getServerId());
			RaftNode.getRaftNode().setLeaderId(request.getServerId());
			RaftNode.getRaftNode().setCurrentTerm(request.getCurrentTerm());
			RaftNode.getRaftNode().setNODE_STATE(RaftNodeState.RFT_NODE_FOLLOWER_STATE);
		} else {
			String serverId=request.getServerId();
			if(!Servers.getInstance().getServers().containsKey(serverId)){
				String[] serverInfo=serverId.split(Constant.LOKOSPLITSYMBOL);
				String host=serverInfo[0];
				Integer port=Integer.parseInt(serverInfo[1]);
				Server newServer=new Server();
				newServer.setHost(host);
				newServer.setId(serverId);
				newServer.setPort(port);
				Servers.getInstance().getServers().put(serverId, newServer);
			}
			request.getCurrentTerm();
			response.setContent(false);
			response.setCurrentTerm(RaftNode.getRaftNode().getCurrentTerm());
			response.setType(request.getType());
			response.setServerId(RaftNode.getRaftNode().getLeaderId());
		}
		RaftTimer.getRaftTimer().restRaftTimer();
		return response;
	}
	@RequestMapping("heartbeat")
	public @ResponseBody
	RaftResponse heartbeat(@RequestBody RaftRequest request) {
		raftlogger.info("主节点点id["+RaftNode.getRaftNode().getLeaderId()+"]");
		System.out.println("leader id is "+RaftNode.getRaftNode().getLeaderId());
		RaftResponse response = new RaftResponse();
		if(request.getCurrentTerm()>=RaftNode.getRaftNode().getCurrentTerm()){
			/**
			 * 回复心跳，并把本节点状态设置为follower
			 */
			
			request.getCurrentTerm();
			response.setContent(true);
			response.setCurrentTerm(request.getCurrentTerm());
			response.setType(request.getType());
			response.setServerId(RaftNode.getRaftNode().getLeaderId());
			RaftNode.getRaftNode().setNODE_STATE(RaftNodeState.RFT_NODE_FOLLOWER_STATE);
		}else {
			/**
			 * 如果请求的term小于本节点的term，则返回失败，并把最新的任期和leaderId发给旧的lead
			 */
			request.getCurrentTerm();
			response.setContent(false);
			response.setCurrentTerm(request.getCurrentTerm());
			response.setType(request.getType());
			response.setServerId(RaftNode.getRaftNode().getLeaderId());
		}
		RaftTimer.getRaftTimer().restRaftTimer();
		return response;
	}
	@RequestMapping("appendEntries")
	public @ResponseBody
	RaftResponse appendEntries(@RequestBody RaftRequest request) {
		RaftResponse response = new RaftResponse();
		if(request.getCurrentTerm()>=RaftNode.getRaftNode().getCurrentTerm()){
			/**
			 * 同步日志把日志写入文件当中
			 */
			response.setContent(true);
			response.setCurrentTerm(request.getCurrentTerm());
			response.setType(request.getType());
			response.setServerId(RaftNode.getRaftNode().getLeaderId());
			
		}else {
			/**
			 * 如果请求的term小于本节点的term，则认为过期消息。拒绝同步，并发送最新的leaderId给旧的leader节点
			 */
			response.setContent(false);
			response.setCurrentTerm(request.getCurrentTerm());
			response.setType(request.getType());
			response.setServerId(RaftNode.getRaftNode().getLeaderId());
		}
		return response;
	}
	
}
