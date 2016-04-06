package org.light.ldrtc.jober;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import org.light.rtc.base.JobStatsWindow;
import org.light.rtc.util.ConfigProperty;

public class JobService implements JobStatsWindow {

	@Override
	public int batchStats(int start, int size) {
		return 0;
	}

	@Override
	public int getHealthStatus() {
		return 1;
	}

	@Override
	public int rtcStats(List<String> userLogs) {
		System.out.println(ConfigProperty.getCurDateTime()+" 计算之初 分配的用户信息个数："+userLogs.size());
		ForkJoinPool fjPool = new ForkJoinPool(); 
		Future<Integer> fjTask = fjPool.submit(new StatsTask(userLogs));
		int rtNum = -1;
		try {
			rtNum = fjTask.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		fjPool.shutdown();
		fjPool = null;
		userLogs.clear();
		userLogs = null;
		System.out.println(ConfigProperty.getCurDateTime()+ " 计算结束 有效用户信息个数："+rtNum);
		
		return rtNum;
	}

}
