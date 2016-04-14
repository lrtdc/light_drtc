package org.light.rtc.timer;

import java.util.List;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.light.rtc.admin.AdminNodeService;
import org.light.rtc.base.StreamLogParser;
import org.light.rtc.util.ConfigProperty;
import org.light.rtc.util.Constants;
import org.light.rtc.util.FileUtil;

public class AdminNodeTimer extends TimerTask{
	protected FileUtil fu = new FileUtil();
	protected static ConcurrentLinkedQueue<String> dataQu = new ConcurrentLinkedQueue<String>();
	private static TreeMap<Long,Integer> delayTaskIdDataNums = new TreeMap<Long,Integer>();
	protected static ConcurrentLinkedQueue<List<String>> memDelayQu = new ConcurrentLinkedQueue<List<String>>();
	private static int minBathJobStatus = 0;
	private static StreamLogParser slp;
	private AdminNodeService ans = new AdminNodeService();
	private long timerNum = 0;
	
	public static void setStreamLogParser(StreamLogParser streamLogParser){
		slp = streamLogParser;
	}
	
	public static boolean addSteamData(List<String> uLogs){
		return dataQu.addAll(uLogs);
	}
	
	public static int getJobStatus(){
		return minBathJobStatus;
	}
	
	@Override
	public void run() {
		int curLogNum = dataQu.size();
		System.out.println(ConfigProperty.getCurDateTime()+" curLogNum : "+curLogNum);
		if(curLogNum>0){
			List<String> userActionList = slp.parseLogs(dataQu, curLogNum);
			int memDelayTaskNum = memDelayQu.size();
			if(minBathJobStatus==0){
				if(memDelayTaskNum>0){
					ans.setUserActions(memDelayQu.poll());
					new Thread(new AdminConsole()).start();
					if(memDelayTaskNum<Constants.maxDelayTaskNum){
						if(delayTaskIdDataNums.size()>0){
							while(memDelayQu.size()<Constants.maxDelayTaskNum && delayTaskIdDataNums.size()>0){
								String taskFile = Constants.delayTaskDir + delayTaskIdDataNums.pollFirstEntry().getKey() + Constants.delayTaskFileSurfix;
								List<String> rtJsonList = fu.readActions(taskFile);
								if(rtJsonList!=null){
									memDelayQu.add(rtJsonList);
								}
								fu.delActionFile(taskFile);
							}
						}
						if(memDelayQu.size()<Constants.maxDelayTaskNum){
							memDelayQu.add(userActionList);
						}else{
							long rtNum = ans.getCurrentTime();
							delayTaskIdDataNums.put(rtNum, curLogNum);
							fu.writeActions(userActionList, Constants.delayTaskDir+rtNum+Constants.delayTaskFileSurfix);
						}
					}else{
						long rtNum = ans.getCurrentTime();
						delayTaskIdDataNums.put(rtNum, curLogNum);
						fu.writeActions(userActionList, Constants.delayTaskDir+rtNum+Constants.delayTaskFileSurfix);
					}
				}else{
					ans.setUserActions(userActionList);
					new Thread(new AdminConsole()).start();
				}
			}else{
				if(memDelayTaskNum<Constants.maxDelayTaskNum){
					memDelayQu.add(userActionList);
				}else{
					long rtNum = ans.getCurrentTime();
					delayTaskIdDataNums.put(rtNum, curLogNum);
					fu.writeActions(userActionList, Constants.delayTaskDir+rtNum+Constants.delayTaskFileSurfix);
				}
				System.out.println(ConfigProperty.getCurDateTime()+" 上次计算任务还没有结束");
			}
		}
		
		if(++timerNum%100==0){
			System.out.println("分布式计算任务周期频率更新 " +timerNum);
			System.gc();
		}
	}
	
	public void reRun(){
		if(memDelayQu.size()>0){
			System.out.println("AdminNodeTimer reRun method ......"+memDelayQu.size());
			ans.setUserActions(memDelayQu.poll());
			new Thread(new AdminConsole()).start();
			if(delayTaskIdDataNums.size()>0){
				String taskFile = Constants.delayTaskDir + delayTaskIdDataNums.pollFirstEntry().getKey() + Constants.delayTaskFileSurfix;
				List<String> rtJsonList = fu.readActions(taskFile);
				if(rtJsonList!=null){
					memDelayQu.add(rtJsonList);
				}
				fu.delActionFile(taskFile); 
			}
		}
	}
	
	protected class AdminConsole implements Runnable{
		
		@Override
		public void run() {
			minBathJobStatus = 1;
			try{
				ans.run();
			} catch (Exception e) {
				System.err.println("分布式计算任务管理服务错误： "+e.getMessage());
			}
			minBathJobStatus = 0;
			reRun();
		}
	}

}
