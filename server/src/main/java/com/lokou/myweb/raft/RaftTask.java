package com.lokou.myweb.raft;

import java.util.TimerTask;

public class RaftTask extends TimerTask {

	@Override
	public void run() {
		if (RaftNode.getRaftNode().getNODE_STATE() == RaftNodeState.RFT_NODE_CANDIDATE_STATE) {
			/**
			 * CANDIDATE状态 拜票选举
			 */
			RaftNode.getRaftNode().sendVoteRequest(
					Servers.getInstance().getServers());
		}
		if (RaftNode.getRaftNode().getNODE_STATE() == RaftNodeState.RFT_NODE_FOLLOWER_STATE) {
			/**
			 * CANDIDATE状态 准备进入选举
			 */
			RaftNode.getRaftNode().setNODE_STATE(
					RaftNodeState.RFT_NODE_CANDIDATE_STATE);
		}
		if (RaftNode.getRaftNode().getNODE_STATE() == RaftNodeState.RFT_NODE_LEADER_STATE) {
			/**
			 * 发送心跳
			 */
			RaftNode.getRaftNode().sendHeartBeat(
					Servers.getInstance().getServers());
		}
	}

}
