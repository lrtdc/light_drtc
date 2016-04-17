package org.light.rtc.admin;

import java.util.Timer;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.light.rtc.api.TDssService;
import org.light.rtc.base.StreamLogParser;
import org.light.rtc.mq.KafkaMqCollect;
import org.light.rtc.service.RtdcAdminService;
import org.light.rtc.timer.AdminNodeTimer;
import org.light.rtc.util.ConfigProperty;
import org.light.rtc.util.Constants;

public class AdminNodeKafkaRun {
	/**
	 * your self defending function for parsing your data stream logs
	 * @param slp
	 */
	public void setSteamParser(StreamLogParser slp){
		AdminNodeTimer.setStreamLogParser(slp);
	}
	
	private class DataCollect implements Runnable{
		@Override
		public void run() {
			KafkaMqCollect kmc = new KafkaMqCollect();
			kmc.collectMq();
		}
	}
	
	public void run(){
		this.adminJobTImer();
		
		new Thread(new DataCollect()).start();
		
		RtdcAdminService rss = new RtdcAdminService();
		TDssService.Processor<RtdcAdminService> tp = new TDssService.Processor<RtdcAdminService>(rss);
		TServerTransport serverTransport = null;
		try {
			serverTransport = new TServerSocket(Constants.adminNodePort);
		} catch (TTransportException e) {
			e.printStackTrace();
		}
		TThreadPoolServer.Args tArgs = new TThreadPoolServer.Args(serverTransport);
		tArgs.maxWorkerThreads(1000);
		tArgs.minWorkerThreads(10);
		tArgs.processor(tp);
		TCompactProtocol.Factory portFactory =  new TCompactProtocol.Factory();
		tArgs.protocolFactory(portFactory);
		
		TServer tServer = new TThreadPoolServer(tArgs);
		System.out.println(ConfigProperty.getCurDateTime()+"......轻量级实时计算框架任务管理节点(通过Kafka整合CN)服务启动......"); 
		tServer.serve();
		serverTransport.close();
		tServer.stop();
		System.out.println("......轻量级实时计算框架任务管理节点(通过Kafka整合CN)服务停止......");
	}
	
	public void adminJobTImer(){
		AdminNodeTimer ant = new AdminNodeTimer();
		Timer daoTimer = new Timer();
		daoTimer.schedule(ant, 100, 1000 * Constants.rtcPeriodSeconds);
		 try {
			 Thread.sleep(100);
		 } catch (InterruptedException e) {
			e.printStackTrace();
		 }
	}
}
