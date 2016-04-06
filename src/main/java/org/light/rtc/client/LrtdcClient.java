package org.light.rtc.client;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import org.light.rtc.api.TDssService;

public class LrtdcClient {
	private String serverIp;
	private int serverPort;
	
	public LrtdcClient(String serverIp, int serverPort){
		this.serverIp = serverIp;
		this.serverPort = serverPort;
	}
	
	public String getCurDateTime(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//"yyyyMMddHHmmss"
		return df.format(new Date());
	}
	
	/**
	 * result: 1:ok, -1: error or net error, 0: exception
	 * @param datas
	 * @return
	 */
	public int getRtcStatsResult(List<String> datas){
		TTransport transport = new TSocket(this.serverIp, this.serverPort); 
		TCompactProtocol protocol = new TCompactProtocol(transport); 
		TDssService.Client tClient = new TDssService.Client(protocol);
	    try {
			transport.open();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
	    int rtResult = 0;
	    if(transport.isOpen()){
	    	try {
	    		rtResult = tClient.rtcStats(datas);
			} catch (TException e) {
				e.printStackTrace();
				System.err.println(this.getCurDateTime()+" 轻量级分布式实时计算异常!");
			}
	    }else{
	    	System.err.println(this.getCurDateTime()+" 轻量级分布式实时计算服务客户端连接失败!");
	    }
	    return rtResult;
	}
	
	public int getJobStatus(){
		TTransport transport = new TSocket(this.serverIp, this.serverPort); 
		TCompactProtocol protocol = new TCompactProtocol(transport); 
		TDssService.Client tClient = new TDssService.Client(protocol);
	    try {
			transport.open();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
	    int rtResult = 0;
	    if(transport.isOpen()){
	    	try {
	    		rtResult = tClient.getJobStatus();
			} catch (TException e) {
				e.printStackTrace();
				System.err.println(this.getCurDateTime()+" 轻量级分布式实时计算节点工作状态异常!");
			}
	    }else{
	    	System.err.println(this.getCurDateTime()+" 轻量级分布式实时计算节点工作状态客户端连接失败!");
	    }
	    return rtResult;
	}
	
	public int addMqinfoToAdminQu(List<String> datas){
		TTransport transport = new TSocket(this.serverIp, this.serverPort); 
		TCompactProtocol protocol = new TCompactProtocol(transport); 
		TDssService.Client tClient = new TDssService.Client(protocol);
	    try {
			transport.open();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
	    int rtResult = -1;
	    if(transport.isOpen()){
	    	try {
	    		rtResult = tClient.addMqinfoToAdminQu(datas);
			} catch (TException e) {
				e.printStackTrace();
				System.err.println(this.getCurDateTime()+" 轻量级分布式实时计算数据收集节点向任务管理节点传输任务 异常!");
			}
	    }else{
	    	System.err.println(this.getCurDateTime()+" 轻量级分布式实时计算数据收集节点向任务管理节点传输 网络连接失败!");
	    }
	    return rtResult;
	}
	
	public int getBatchStatsResult(int start, int size){
		TTransport transport = new TSocket(this.serverIp, this.serverPort); 
		TCompactProtocol protocol = new TCompactProtocol(transport); 
		TDssService.Client tClient = new TDssService.Client(protocol);
	    try {
			transport.open();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
	    int rtResult = -1;
	    if(transport.isOpen()){
	    	try {
	    		rtResult = tClient.batchStats(start, size);
			} catch (TException e) {
				e.printStackTrace();
				System.err.println(this.getCurDateTime()+" 轻量级分布式实时计算大数据批量处理 异常!");
			}
	    }else{
	    	System.err.println(this.getCurDateTime()+" 轻量级分布式实时计算 大数据批量处理 客户端连接失败!");
	    }
	    return rtResult;
	}
	
	public int getAdminNodeId(){
		TTransport transport = new TSocket(this.serverIp, this.serverPort); 
		TCompactProtocol protocol = new TCompactProtocol(transport); 
		TDssService.Client tClient = new TDssService.Client(protocol);
	    try {
			transport.open();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
	    int rtResult = -1;
	    if(transport.isOpen()){
	    	try {
	    		rtResult = tClient.getAdminNodeId();
			} catch (TException e) {
				e.printStackTrace();
				System.err.println(this.getCurDateTime()+" 轻量级分布式实时计算获取任务管理主节点ID异常!");
			}
	    }else{
	    	System.err.println(this.getCurDateTime()+" 轻量级分布式实时计算获取任务管理主节点ID客户端连接失败!");
	    }
	    return rtResult;
	}
	
	public int getHealthStatus(){
		TTransport transport = new TSocket(this.serverIp, this.serverPort); 
		TCompactProtocol protocol = new TCompactProtocol(transport); 
		TDssService.Client tClient = new TDssService.Client(protocol);
	    try {
			transport.open();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
	    int rtResult = 0;
	    if(transport.isOpen()){
	    	try {
	    		rtResult = tClient.getHealthStatus();
			} catch (TException e) {
				e.printStackTrace();
				System.err.println(this.getCurDateTime()+" 轻量级分布式实时计算服务健康状况监测异常!");
			}
	    }else{
	    	System.err.println(this.getCurDateTime()+" 轻量级分布式实时计算健康状况监测客户端连接失败!");
	    }
	    return rtResult;
	}
	
}
