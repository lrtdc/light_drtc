package org.light.rtc.job;

import org.light.rtc.base.JobStatsWindow;

public class SingleStatsJober {
	private static SingleStatsJober _instance;
	
	private SingleStatsJober(){}
	
	public synchronized static SingleStatsJober getInstance(){
		if(_instance==null){
			_instance = new SingleStatsJober();
		}
		return _instance;
	}
	
	private JobStatsWindow rtcJob;
	
	/**
	 * 实现业务计算方法前，首先要赋此值（每条信息为：以用户ID为key的单位时间内，聚合好的该用户的各类行为信息）
	 * @param sjb
	 */
	public void setStatsJobWindow(JobStatsWindow sjb){
		rtcJob = sjb;
	}
	
	public JobStatsWindow getStatsJobWindow(){
		return rtcJob;
	}
	
}
