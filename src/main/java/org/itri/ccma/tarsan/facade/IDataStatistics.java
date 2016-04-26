package org.itri.ccma.tarsan.facade;
import java.util.List;

public interface IDataStatistics {
	/**
	 * get days of log data
	 * @param sessionId
	 * @return
	 */
	//without encrypted text
	public List<?>getDataByDays(String sessionId);
	
	/**
	 * get all host url
	 * @param sessionId
	 * @return
	 */
	//without encrypted text
	public List<?>getAllHost(String sessionId);
	
	/**
	 * get all IP
	 * @param sessionId
	 * @return
	 */
	//without encrypted text
	public List<?>getAllIp(String sessionId);
	
	/**
	 * get days of IP and IP count
	 * @param sessionId
	 * @param ip
	 * @return
	 */
	//without encrypted text
	public List<?>getIpCountWithDays(String sessionId, String ip);
	
	/**
	 * get days of host url
	 * @param sessionId
	 * @param host
	 * @return
	 */
	//without encrypted text
	public List<?>getHostCountWithDays(String sessionId, String host);
	
	/**
	 * get interval time of host
	 * @param sessionId
	 * @return
	 */
	//without encrypted text
	public List<?>getIntervalWithHost(String sessionId);
	
	/**
	 * get hours of log data
	 * @param sessionId
	 * @return
	 */
	//without encrypted text
	public List<?>getHoursData(String sessionId);
	
	/**
	 * get ref and url
	 * @param sessionId
	 * @return
	 */
	//without encrypted text
	public List<?>getRefAndURL(String sessionId);
		
	/**
	 * get date of log data
	 * @param sessionId
	 * @param date
	 * @return
	 */
	//without encrypted text
	public List<?>getDailyDataByDate(String sessionId, String date);
	
	/**
	 * get host of period days log data
	 * @param sessionId
	 * @return
	 */
	//without encrypted text
	public List<?>getDailyHoursDataByHost(String sessionId, String host);
}
