package org.light.ldrtc.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.light.rtc.base.StreamLogParser;

import com.alibaba.fastjson.JSONObject;

public class LogParser implements StreamLogParser {

	/**
	 * 测试：MQ日志行，本测试，多个字段以“,”隔开
	 * 比如原始日志形式： "deviceId,docId,type,timestamp",加工成：
	 * 加工成每条信息类似: "{uid:设备ID或通行证ID，data:{view:{docIds},collect:{docIds}}}"形式的信息列表
	 * 例如：[{"uid":"light","data":{"view":["yl20160401","yl20160402","yl20160403","yl20160404"]}}, {"uid":"momo","data":{"view":["ty20160404","ty20160405","yl20160405","yl20160407"],"share":["ty20160404"]}}, {"uid":"taotao","data":{"view":["yl20160402","yl20160403","yl20160405"],"collect":["tu20160404"]}}]
	 */
	@Override
	public List<String> parseLogs(ConcurrentLinkedQueue<String> dataQu, int curNum) {
		Map<String,Map<String,Set<String>>> rtMap = new HashMap<String,Map<String,Set<String>>>();
		String line = null;
		String[] fields = null;
		Map<String,Set<String>> actions = null;
		Set<String> docIds = null;
		for(int i=0; i<curNum; i++){
			line = dataQu.poll();
			fields = line.split(",");
			if(fields!=null && fields.length==4){
				actions = rtMap.remove(fields[0]);
				if(actions==null){
					actions = new HashMap<String,Set<String>>();
				}
				docIds = actions.remove(fields[2]);
				if(docIds==null){
					docIds = new HashSet<String>();
				}
				docIds.add(fields[1]);
				actions.put(fields[2], docIds);
				rtMap.put(fields[0],actions);
			}
		}
		List<String> rtList = new LinkedList<String>();
		JSONObject rtJson = null;
		for(Entry<String,Map<String,Set<String>>> item : rtMap.entrySet()){
			rtJson = new JSONObject();
			rtJson.put("uid", item.getKey());
			rtJson.put("data", item.getValue());
			rtList.add(rtJson.toJSONString());
		}
//		System.out.println(rtList);
		
//		Map<String,List<String>> tmpActions = null;
//		for(String rt : rtList){
//			rtJson = JSONObject.parseObject(rt);
//			tmpActions = (Map<String,List<String>>)rtJson.get("data");
//			System.out.println(rtJson.get("uid")+"\t"+tmpActions);
//		}
		
		return rtList;
	}
	
	public void test(){
		ConcurrentLinkedQueue<String> dataQu = new ConcurrentLinkedQueue<String>();
		dataQu.add("light,yl20160401,view,1234");
		dataQu.add("light,yl20160402,view,1234");
		dataQu.add("light,yl20160403,view,1234");
		dataQu.add("light,yl20160404,view,1234");
		
		dataQu.add("taotao,yl20160402,view,1234");
		dataQu.add("taotao,yl20160403,view,1234");
		dataQu.add("taotao,tu20160404,collect,1234");
		dataQu.add("taotao,yl20160405,view,1234");
		
		dataQu.add("momo,ty20160404,view,1234");
		dataQu.add("momo,ty20160404,share,1234");
		dataQu.add("momo,ty20160405,view,1234");
		dataQu.add("momo,yl20160405,view,1234");
		dataQu.add("momo,yl20160407,view,1234");
		
		int curNum = dataQu.size();
		this.parseLogs(dataQu, curNum);
		
	}
	
//	public static void main(String[] args){
//		LogParser lp = new LogParser();
//		lp.test();
//	}

}
