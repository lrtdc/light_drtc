package org.light.rtc.util;

import java.util.List;

public class Constants {
	// es cluster config
	public static String esClusterName = ConfigProperty.getProps().getProperty("cluterName");
	public static String esClusterHosts = ConfigProperty.getProps().getProperty("clusterHosts");
	
	//mq config
	public final static String mqHost = ConfigProperty.getProps().getProperty("mq.host");
	public final static int mqPort = Integer.parseInt(ConfigProperty.getProps().getProperty("mq.port"));
	public final static String mqUser = ConfigProperty.getProps().getProperty("mq.user");
	public final static String mqPswd = ConfigProperty.getProps().getProperty("mq.pswd");
	public final static String mqVhost = ConfigProperty.getProps().getProperty("mq.vhost");
	public final static String mqExchange = ConfigProperty.getProps().getProperty("mq.exchange");
	public final static String mqClickKey = ConfigProperty.getProps().getProperty("mq.clickRoutKey");
	public final static String mqClickFirstQu = ConfigProperty.getProps().getProperty("mq.clickQueue1");
	public final static String mqClickSecondQu = ConfigProperty.getProps().getProperty("mq.clickQueue2");
	
	//Timer config (unit : seconds)
	public final static int mqDoBatchTimer = Integer.parseInt(ConfigProperty.getProps().getProperty("mqDoBatchTimer"));
	
	//AdminNode hosts and ports for DataCollectNode using
	public final static String adminNodeHosts = ConfigProperty.getProps().getProperty("adminNodeHosts");
	
	//AdminNode server port
	public final static int adminNodePort = Integer.parseInt(ConfigProperty.getProps().getProperty("adminNodePort"));
	
	//JobNode hosts and ports  for AdminNode using
	public final static String jobNodeHosts = ConfigProperty.getProps().getProperty("jobNodeHosts");
	
	//JobNode server port
	public final static int jobNodePort = Integer.parseInt(ConfigProperty.getProps().getProperty("jobNodePort"));
	
	//rtc timer
	public final static int rtcPeriodSeconds = Integer.parseInt(ConfigProperty.getProps().getProperty("rtcPeriodSeconds"));
	
	public final static int adminNodeId = Integer.parseInt(ConfigProperty.getProps().getProperty("adminNodeId"));
	
	public final static int minJobBatchNum = Integer.parseInt(ConfigProperty.getProps().getProperty("minJobBatchNum"));
	public final static int atomJobBatchNum = Integer.parseInt(ConfigProperty.getProps().getProperty("atomJobBatchNum"));
	
	//redis cluster config
	public final static String redisHosts = ConfigProperty.getProps().getProperty("redisHosts");
	public final static String redisPswd = ConfigProperty.getProps().getProperty("redisPswd");
	
	public final static String delayTaskDir = ConfigProperty.getProps().getProperty("delayTaskDir");
	public final static String delayTaskFileSurfix = ConfigProperty.getProps().getProperty("delayTaskFileSurfix");
	public final static int maxDelayTaskNum = Integer.parseInt(ConfigProperty.getProps().getProperty("maxDelayTaskNum"));
	
}
