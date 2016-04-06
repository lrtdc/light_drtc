package org.light.rtc.service;

import java.util.List;

import org.apache.thrift.TException;
import org.light.rtc.api.TDssService;
import org.light.rtc.job.SingleStatsJober;

public class RtdcJobService implements TDssService.Iface{

	@Override
	public int addMqinfoToAdminQu(List<String> uLogs) throws TException {
		return 0;
	}

	@Override
	public int rtcStats(List<String> userLogs) throws TException {
		return SingleStatsJober.getInstance().getStatsJobWindow().rtcStats(userLogs);
	}

	@Override
	public int batchStats(int start, int size) throws TException {
		return SingleStatsJober.getInstance().getStatsJobWindow().batchStats(start, size);
	}

	@Override
	public int getAdminNodeId() throws TException {
		return 0;
	}

	@Override
	public int getHealthStatus() throws TException {
		return 1;
	}

	@Override
	public int getJobStatus() throws TException {
		return 1;
	}

}
