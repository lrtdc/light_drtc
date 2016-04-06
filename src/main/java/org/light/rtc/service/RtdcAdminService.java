package org.light.rtc.service;

import java.util.List;

import org.apache.thrift.TException;
import org.light.rtc.api.TDssService.Iface;
import org.light.rtc.timer.AdminNodeTimer;

public class RtdcAdminService implements Iface {

	@Override
	public int addMqinfoToAdminQu(List<String> uLogs) throws TException {
		boolean rt =  AdminNodeTimer.addSteamData(uLogs);
		if(rt)
			return 1;
		else
			return 0;
	}

	@Override
	public int rtcStats(List<String> userLogs) throws TException {
		
		return 0;
	}

	@Override
	public int batchStats(int start, int size) throws TException {
		// TODO Auto-generated method stub
		return 0;
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
		// TODO Auto-generated method stub
		return 0;
	}

}
