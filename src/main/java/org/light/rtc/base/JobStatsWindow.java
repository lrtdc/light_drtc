package org.light.rtc.base;

import java.util.List;

public interface JobStatsWindow {
	/**
	 * 根据接受的实时数据流，完成相关实时计算业务需求。
	 * @param userLogs
	 * @return
	 */
	int rtcStats(List<String> userLogs);
	/**
	 * 适合做线下离线批量处理计算
	 * @param start
	 * @param size
	 * @return
	 */
	int batchStats(int start, int size);
	/**
	 * 返回服务健康状态
	 * @return
	 */
	int getHealthStatus();
}
