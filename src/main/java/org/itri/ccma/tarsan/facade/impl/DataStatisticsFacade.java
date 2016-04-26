package org.itri.ccma.tarsan.facade.impl;

import java.util.List;

import org.itri.ccma.tarsan.biz.BoDataStatistics;
import org.itri.ccma.tarsan.facade.IDataStatistics;

public class DataStatisticsFacade implements IDataStatistics {

	@Override
	public List<?> getDataByDays(String sessionId) {
		// TODO Auto-generated method stub
		return BoDataStatistics.getInstance().getDataByDays(sessionId);
	}

	@Override
	public List<?> getAllHost(String sessionId) {
		// TODO Auto-generated method stub
		return BoDataStatistics.getInstance().getAllHost(sessionId);
	}

	@Override
	public List<?> getAllIp(String sessionId) {
		// TODO Auto-generated method stub
		return BoDataStatistics.getInstance().getAllIp(sessionId);
	}

	@Override
	public List<?> getIpCountWithDays(String sessionId, String ip) {
		// TODO Auto-generated method stub
		return BoDataStatistics.getInstance().getIpCountWithDays(sessionId, ip);
	}

	@Override
	public List<?> getHostCountWithDays(String sessionId, String host) {
		// TODO Auto-generated method stub
		return BoDataStatistics.getInstance().getHostCountWithDays(sessionId, host);
	}

	@Override
	public List<?> getIntervalWithHost(String sessionId) {
		// TODO Auto-generated method stub
		return BoDataStatistics.getInstance().getIntervalWithHost(sessionId);
	}

	@Override
	public List<?> getHoursData(String sessionId) {
		// TODO Auto-generated method stub
		return BoDataStatistics.getInstance().getHoursData(sessionId);
	}

	@Override
	public List<?> getRefAndURL(String sessionId) {
		// TODO Auto-generated method stub
		return BoDataStatistics.getInstance().getRefAndURL(sessionId);
	}

	@Override
	public List<?> getDailyDataByDate(String sessionId, String date) {
		// TODO Auto-generated method stub
		return BoDataStatistics.getInstance().getDailyDataByDate(sessionId, date);
	}

	@Override
	public List<?> getDailyHoursDataByHost(String sessionId, String host) {
		// TODO Auto-generated method stub
		return BoDataStatistics.getInstance().getDailyHoursDataByHost(sessionId, host);
	}

}
