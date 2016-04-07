package org.light.rtc.timer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.light.rtc.client.LrtdcClient;
import org.light.rtc.util.ConfigProperty;
import org.light.rtc.util.Constants;
import org.light.rtc.util.Murmurs;

import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

public class MqDoTimer extends TimerTask {
	protected static ConcurrentLinkedQueue<String> firstStreamLogs = new ConcurrentLinkedQueue<String>();
	protected static ConcurrentLinkedQueue<String> secondStreamLogs = new ConcurrentLinkedQueue<String>();
	
	private String[] adminNodeHosts = Constants.adminNodeHosts.split(",");
	private int adminNodeNum = adminNodeHosts.length;
	private ConcurrentHashMap<Integer,LrtdcClient> adminNodeClientMap;
	private Murmurs mmHash = new Murmurs();
	
	public MqDoTimer(){
		if(adminNodeHosts!=null){
			adminNodeClientMap = new ConcurrentHashMap<Integer,LrtdcClient>();
			for(int i=0; i<adminNodeNum; i++){
				String[] tmpHostIp = adminNodeHosts[i].split(":");
				adminNodeClientMap.put(i, new LrtdcClient(tmpHostIp[0], Integer.parseInt(tmpHostIp[1])));
			}
		}
	}
	
	public void parseMqText(String routKey, String mqText){
		int adminId = (int)(mmHash.getGuavaMurmurs(routKey)%adminNodeNum);
		if(adminId==0){
			firstStreamLogs.add(mqText);
		}else{
			secondStreamLogs.add(mqText);
		}
	}

	@Override
	public void run() {
		try{
			int firstQuNum = firstStreamLogs.size();
			int secondQuNum = secondStreamLogs.size();
			if(firstQuNum>0 || secondQuNum>0){
				System.out.println("AdminNodeId_1: "+firstQuNum+" AdminNodeId_2: "+secondQuNum);
				new Thread(new BatchDoLog(firstQuNum, secondQuNum)).start();
			}else{
				System.out.println(ConfigProperty.getCurDateTime()+"\t 当前时刻无消息 ");
			}
		} catch (Exception e) {
			System.err.println("用户行为日志收集错误 "+e.getMessage());
		}
	}
	
	protected class BatchDoLog implements Runnable{
		private int firstNum;
		private int secondNum;
		
		public BatchDoLog(int firstNum, int secondNum){
			this.firstNum = firstNum;
			this.secondNum = secondNum;
		}

		@Override
		public void run() {
			List<String> tmpLogs = null;
			if(firstNum>0){
				tmpLogs = new LinkedList<String>();
				for(int i=0;i<firstNum;i++){
					tmpLogs.add(firstStreamLogs.poll());
				}
				if(adminNodeClientMap.get(0).getHealthStatus()==1){
					adminNodeClientMap.get(0).addMqinfoToAdminQu(tmpLogs);
				}else{
					adminNodeClientMap.get(1).addMqinfoToAdminQu(tmpLogs);
				}
			}
			if(secondNum>0){
				tmpLogs = new LinkedList<String>();
				for(int i=0;i<secondNum;i++){
					tmpLogs.add(secondStreamLogs.poll());
				}
				if(adminNodeClientMap.get(1).getHealthStatus()==1){
					adminNodeClientMap.get(1).addMqinfoToAdminQu(tmpLogs);
				}else{
					adminNodeClientMap.get(0).addMqinfoToAdminQu(tmpLogs);
				}
			}
		}
	}

//	public static void main(String[] args){
//		MqDoTimer mdt = new MqDoTimer();
//		mdt.test();
//	}
	
}
