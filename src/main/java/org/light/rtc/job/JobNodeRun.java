package org.light.rtc.job;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.light.rtc.api.TDssService;
import org.light.rtc.base.JobStatsWindow;
import org.light.rtc.service.RtdcJobService;
import org.light.rtc.util.ConfigProperty;
import org.light.rtc.util.Constants;

public class JobNodeRun {
	/**
	 * 计算任务节点运行前，必须事先传递真正的业务实现类，否则影响运行
	 * @param sjw
	 */
	public void setStatsJobWindow(JobStatsWindow sjw){
		SingleStatsJober.getInstance().setStatsJobWindow(sjw);
	}
	
	public void run(){
		RtdcJobService rss = new RtdcJobService();
		TDssService.Processor<RtdcJobService> tp = new TDssService.Processor<RtdcJobService>(rss);
		TServerTransport serverTransport = null;
		
		try {
			serverTransport = new TServerSocket(Constants.jobNodePort);
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
		System.out.println(ConfigProperty.getCurDateTime()+"......轻量级实时计算框架任务计算节点算服务启动......"); 
		tServer.serve();
		serverTransport.close();
		tServer.stop();
		System.out.println("......轻量级实时计算框架计算任务节点服务停止......");
	}
	
}
