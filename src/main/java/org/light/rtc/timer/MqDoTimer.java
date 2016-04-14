package org.light.rtc.timer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.light.rtc.client.LrtdcClient;
import org.light.rtc.util.ConfigProperty;
import org.light.rtc.util.Constants;
import org.light.rtc.util.Murmurs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimerTask;
import java.util.Map.Entry;

public class MqDoTimer extends TimerTask {
	protected List<ConcurrentLinkedQueue<String>> dataStreamList = new ArrayList<ConcurrentLinkedQueue<String>>();
	
	private String[] adminNodeHosts = Constants.adminNodeHosts.split(",");
	private int adminNodeNum;
	private ConcurrentHashMap<Integer,LrtdcClient> adminNodeClientMap;
	private long timerNum = 0;
	
	public MqDoTimer(){
		if(adminNodeHosts!=null){
			adminNodeNum = adminNodeHosts.length;
			adminNodeClientMap = new ConcurrentHashMap<Integer,LrtdcClient>();
			for(int i=0; i<adminNodeNum; i++){
				String[] tmpHostIp = adminNodeHosts[i].split(":");
				adminNodeClientMap.put(i, new LrtdcClient(tmpHostIp[0], Integer.parseInt(tmpHostIp[1])));
				dataStreamList.add(new ConcurrentLinkedQueue<String>());
			}
		}
	}
	
	public void parseMqText(String routKey, String mqText){
		int adminId = (int)(Murmurs.getGuavaMurmurs(routKey)%adminNodeNum);
		this.dataStreamList.get(adminId).add(mqText);
	}
	
	public List<Integer> getAbleAdminNodeIds(){
		List<Integer> ableJobNodeIds = new ArrayList<Integer>();
		for(Entry<Integer,LrtdcClient> item : adminNodeClientMap.entrySet()){
			if(item.getValue().getHealthStatus()==1){
				ableJobNodeIds.add(item.getKey());
			}
		}
		return ableJobNodeIds;
	}

	@Override
	public void run() {
		try{
			int tmpNum = -1;
			Map<Integer,Integer> dataQuNums = new HashMap<Integer,Integer>();
			for(int i=0; i<dataStreamList.size(); i++){
				tmpNum = dataStreamList.get(i).size();
				if(tmpNum>0){
					dataQuNums.put(i, tmpNum);
				}
			}
			if(dataQuNums.size()>0){
				System.out.println(ConfigProperty.getCurDateTime()+" currently will be doing adminNode and logNum : "+dataQuNums);
				List<Integer> enableANIds = this.getAbleAdminNodeIds();
				if(enableANIds.size()>0){
					for(Entry<Integer,Integer> dqnItem : dataQuNums.entrySet()){
						new Thread(new BatchDoLog(dqnItem.getKey(), dqnItem.getValue(), enableANIds)).start();
					}
				}else{
					System.out.println(ConfigProperty.getCurDateTime()+" currently there is no enable adminNode for receving stream logs");
				}
			}else{
				System.out.println(ConfigProperty.getCurDateTime()+"\t 当前时刻无消息 ");
			}
		} catch (Exception e) {
			System.err.println("用户行为日志收集错误 "+e.getMessage());
		}
		if(++timerNum%1000==0){
			System.out.println("实时数据流按固定周期提交 " +timerNum+" 次");
			System.gc();
		}
	}
	
	protected class BatchDoLog implements Runnable{
		private int adminId;
		private int logNum;
		private List<Integer> enableAnIds;
		
		public BatchDoLog(int adminId, int logNum, List<Integer> enableAdminIds){
			this.adminId = adminId;
			this.logNum = logNum;
			this.enableAnIds = enableAdminIds;
		}

		private Random rand = new Random();
		
		@Override
		public void run() {
			List<String> tmpLogs = new LinkedList<String>();
			ConcurrentLinkedQueue<String> tmpQu = dataStreamList.get(adminId);
			for(int i=0;i<logNum;i++){
				tmpLogs.add(tmpQu.poll());
			}
			if(!enableAnIds.contains(adminId)){
				adminId = enableAnIds.get(rand.nextInt(enableAnIds.size()));
			}
			adminNodeClientMap.get(adminId).addMqinfoToAdminQu(tmpLogs);
		}
	}

//	public static void main(String[] args){
//		MqDoTimer mdt = new MqDoTimer();
//		mdt.test();
//	}
	
}
