package com.lokou.myweb.listener;

import java.io.InterruptedIOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.lokou.myweb.raft.RaftNode;
import com.lokou.myweb.raft.RaftNodeState;
import com.lokou.myweb.raft.RaftTimer;
import com.lokou.myweb.raft.Servers;

public class RaftListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		//RaftTimer.getRaftTimer().restRaftTimer();
		
	}

	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

	public Integer resetElectionTimeOut() {
		int electionTimeOut=150 + (int) Math.round(Math.random() * 150);
		return electionTimeOut;
	}
}
