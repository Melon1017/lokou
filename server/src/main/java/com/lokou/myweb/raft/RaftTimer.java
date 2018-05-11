package com.lokou.myweb.raft;

import java.util.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RaftTimer {
	Timer timer;
	private Logger raftLogger = LoggerFactory.getLogger("raft");
	private RaftTimer(){
		timer=new Timer();	
	}
	
	static class  RaftTimerHolder{
		
		public static RaftTimer instance=new RaftTimer();
	}
	public static RaftTimer getRaftTimer(){
		
		return RaftTimerHolder.instance;
	}
	public synchronized void restRaftTimer(){
		raftLogger.info("重新启动一轮选举");
		timer.cancel();
		timer=new Timer();
		timer.schedule(new RaftTask(), resetElectionTimeOut(), resetElectionTimeOut());
		
		
	}
	 public static Long resetElectionTimeOut() {
		    
			int electionTimeOut=150 + (int) Math.round(Math.random() * 150);
			return electionTimeOut+0L;
	}
	
}
