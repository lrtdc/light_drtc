package org.light.ldrtc.jober;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.RecursiveTask;

import org.light.rtc.util.Constants;

import com.alibaba.fastjson.JSONObject;

public class StatsTask extends RecursiveTask<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> uActions;
	
	/**
	 * @param userLogs
	 * userLogs中每条数据都是事先加工好的类似: "{'uid' : 设备ID或通行证ID， 'data':{'view':{docIds},'collect':{docIds}}}"形式的信息列表
	 * 例如：userLogs值为：[{"uid":"light","data":{"view":["yl20160401","yl20160402","yl20160403","yl20160404"]}}, {"uid":"momo","data":{"view":["ty20160404","ty20160405","yl20160405","yl20160407"],"share":["ty20160404"]}}, {"uid":"taotao","data":{"view":["yl20160402","yl20160403","yl20160405"],"collect":["tu20160404"]}}]
	 */
	public StatsTask(List<String> userLogs){
		this.uActions = userLogs;
	}
	
	@Override
	public Integer compute(){
		int rtNum = -1;
		if(this.uActions.size()<Constants.minJobBatchNum){
			Map<String,List<String>> tmpActions = null;
			JSONObject rtJson = null;
			Map<String,Map<String,List<String>>> uActions = new HashMap<String,Map<String,List<String>>>();
			for(String rt : this.uActions){
				rtJson = JSONObject.parseObject(rt);
				tmpActions = (Map<String,List<String>>)rtJson.get("data");
				uActions.put(rtJson.getString("uid"), tmpActions); 
			}
		    System.out.println("加工后待计算的初始数据："+uActions.size());
		    for(Entry<String,Map<String,List<String>>> item : uActions.entrySet()){
		    	System.out.println(item);
		    }
		    rtNum = uActions.size();
		}else{
			int middle = this.uActions.size()/2;
			StatsTask left = new StatsTask(this.uActions.subList(0, middle));
			StatsTask right = new StatsTask(this.uActions.subList(middle,  this.uActions.size()));
			left.fork();
			int leftNum = left.join();
			int rightNum = right.compute();
			rtNum = leftNum + rightNum;
		}
		return rtNum;
	} 

}
