package org.light.ldrtc.test;

import java.util.LinkedList;
import java.util.List;

import org.light.rtc.base.MqConsumer;

public class MqCollect extends MqConsumer{
	
	public void run(){
		String[] tmpFields = null;
		List<String> dataQu = new LinkedList<String>();
		dataQu.add("light,yl20160401,view,1234");
		dataQu.add("light1,yl20160402,view,1234");
		dataQu.add("light,yl20160403,collect,1234");
		dataQu.add("light3,yl20160404,view,1234");
		
		dataQu.add("taotao,yl20160402,view,1234");
		dataQu.add("taotao1,yl20160403,view,1234");
		dataQu.add("taotao1,tu20160404,collect,1234");
		dataQu.add("taotao,yl20160405,view,1234");
		
		dataQu.add("momo,ty20160404,view,1234");
		dataQu.add("momo1,ty20160404,share,1234");
		dataQu.add("momo,ty20160405,view,1234");
		dataQu.add("momo1,yl20160405,view,1234");
		dataQu.add("momo,yl20160407,view,1234");
		
		dataQu.add("zhaozilong1,ty20160402,view,1234");
		dataQu.add("zhaozilong,history20160403,collect,1234");
		dataQu.add("zhaozilong,tu20160404,collect,1234");
		dataQu.add("zhaozilong1,yl20160405,view,1234");
		while(true){
			for(String log : dataQu){
				tmpFields = log.split(",");
				this.mqTimer.parseMqText(tmpFields[0], log);//提交给任务管理节点入口
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public static void main(String[] args) {
		MqCollect mcl = new MqCollect();
		mcl.run();
	}

}
