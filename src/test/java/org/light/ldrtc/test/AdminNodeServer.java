package org.light.ldrtc.test;

import org.light.ldrtc.parser.LogParser;
import org.light.rtc.admin.AdminNodeRun;

public class AdminNodeServer {

	public void run(){
		AdminNodeRun anr = new AdminNodeRun();
		anr.setSteamParser(new LogParser());//配置解析日志数据流
		anr.run();
	}
	
	public static void main(String[] args) {
		AdminNodeServer adminServer = new AdminNodeServer();
		adminServer.run();
	}

}
