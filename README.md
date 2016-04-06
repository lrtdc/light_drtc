#框架说明
Light_drtc是一个轻量级分布式实时计算框架，它可以帮助你快速实现自定义的实时计算平台。它主要参考当前流行的实时计算框架Storm的任务分发和Spark Streaming的Min-Batch处理思想设计，设计目的是为了降低当前大数据时代的分布式实时计算入门门槛，方便初中级学者上手，快速实现企业所需计算实时性要求比较高的业务逻辑。它本身可以做为独立的分布式实时计算平台存在，也可以作为其它项目的基础类库存在。

#代码包结构
Light_drtc整个项目可以分为3部分：实时数据收集、任务管理和任务计算三部分，三者结合，共同完成完整的分布式实时计算系统。

---"src/main/java": 框架主体包

---"src/main/resources": 框架主体包所需配置文件

---"src/test/java": 框架测试包，也是框架三个核心部分作为一个整体，完成的一个分布式实时计算系统的实例说明。

---"src/test/resources": 框架测试包所需配置文件

#使用说明
首先下载Light_drtc，进入根目录，
使用命令：mvn package，

打包成“light_drtc-${current_version}.jar”,引入即可。

Light_drtc只提供分布式实时计算的核心开发功能，其中实时日志解析、任务计算落地实现都需要开发者自行定义。以下3个部分为开发者的开发步骤，且为3个独立进程。

	1. 数据流实时收集部分，每隔${mqDoBatchTimer}秒批量提交给任务管理节点，至于实时收集数据MQ需要学者自己开发，框架中也有RabbitMQ作为消费者的实用用例。
	   具体开发，可以参考 src/test/java/org/light/ldrtc/test/MqCollect.java，首先继承org.light.rtc.base.MqConsumer.java, 然后对所接受的每条数据，代码中调用“this.mqTimer.parseMqText(userId, logText)”即可。
	   
	2. 任务管理部分：对于所接受的来自实时数据流的数据，每隔${rtcPeriodSeconds}秒，将所收集的数据，统一加工成每条信息类似: "{uid:设备ID或通行证ID，data:{view:{docIds},collect:{docIds}}}"形式的信息列表。
	   具体开发可以参考src/test/java/org/light/ldrtc/test/AdminNodeServer.java， 这里需要开发者需要自行实现实时数据流的日志解析，需要实现“org.light.rtc.base.StreamLogParser.java”接口，具体参考实例src/test/java/org/light/ldrtc/parser/LogParser.java
	
	3. 任务计算部分： 这部分主要依靠开发者自定义实现，实现时，需要实现“org.light.rtc.base.JobStatsWindow.java”相关接口，这里建议大家参考JDK本身提供的Fork/Join并行计算框架，以更高效率的利用服务器资源，开发实例入口参考“src/test/java/org/light/ldrtc/test/JobNodeServer.java”，具体实现参考“org.light.ldrtc.jober.JobService.java及StatsTask.java”。
		
#框架配置文件说明
需要在实际中根据实际需要修改，这里需要说明的是，框架本身负责计算任务管理及资源协调。这里重点说明几个配置属性。
rtc_conf.properties

mqDoBatchTimer=3 #实时收集的数据流每隔3秒批量提交给任务管理节点

rtcPeriodSeconds=7 ＃任务管理节点中每隔7秒将所收集的数据统分发给任务计算节点

atomJobBatchNum=100  ＃默认每个任务计算节点在${rtcPeriodSeconds}秒内所处理的最大元数据条数（比如单个用户规定时间内的所有行为聚合的数据）

minJobBatchNum = 20  ＃默认每个任务计算节点在${rtcPeriodSeconds}秒内所处理的最小元数据条数

adminNodeHosts = 127.0.0.1:16401,127.0.0.1:16406  ＃给数据收集节点使用，这里规定使用2个任务管理节点，双主模式。

adminNodePort = 16401	＃任务管理节点启动服务所在端口，可以根据需要修改。同台服务器，2个任务管理节点服务端口必须区分

adminNodeId=1		＃后期功能扩展使用，任务管理节点每个都会扩展一个热备,主：1，从：0

minThreadNum=10		 ＃服务端进程，最小线程数

maxThreadNum=10000   ＃服务端进程，最大线程数

jobNodeHosts=127.0.0.1:1641,127.0.0.1:1642,127.0.0.1:1643  #任务计算节点配置，供任务管理节点管理，作者建议每个任务管理节点至少包含3个任务计算节点实例

jobNodePort=16401	＃任务计算节点启动服务所在端口，可以根据需要调整。

delayTaskDir = /Users/a/delayTask/   ＃任务计算节点不能及时处理的延迟的聚合后的用户行为数据，按时间片段命名写入指定目录，根据需要自定义

delayTaskFileSurfix = .txt		＃延迟执行的元数据文件后缀命名

maxDelayTaskNum = 6			＃任务管理节点默认只能接受的未及时执行的元数据组的个数，超过此限制，每个元数据组一个文件写入指定目录


