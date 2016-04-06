package org.light.rtc.base;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface StreamLogParser {
	/**
	 * 自定义实现：从指定队列dataQu中，获取指定数目curNum的单条日志信息, 
	 * 加工成每条信息类似: {uid:设备ID或通行证ID，data:{view:{docIds},collect:{docIds}}}形式的信息列表
	 * @param dataQu
	 * @param curNum
	 * @return
	 */
	List<String> parseLogs(ConcurrentLinkedQueue<String> dataQu, int curNum);
}
