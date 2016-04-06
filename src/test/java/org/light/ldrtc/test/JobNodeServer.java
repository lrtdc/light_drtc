package org.light.ldrtc.test;

import org.light.ldrtc.jober.JobService;
import org.light.rtc.job.JobNodeRun;

public class JobNodeServer {
	
	public void run(){
		JobNodeRun jnr = new JobNodeRun();
		jnr.setStatsJobWindow(new JobService());//配置计算任务具体实现类
		jnr.run();
	}
	
	public static void main(String[] args) {
		JobNodeServer jobServer = new JobNodeServer();
		jobServer.run();
	}
	
}
