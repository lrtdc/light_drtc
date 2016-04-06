package org.light.rtc.pool;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class JobClientPool {
	
	private ConcurrentHashMap<Integer,Vector<TTransport>> adminPool;
	private String adminIp;
	private int adminPort;
	
	private Vector<TTransport> jobPool;
	private String jobIp;
	private int jobPort;
	
}