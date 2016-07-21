package org.itri.ccma.tarsan.facade;

import java.util.List;

public interface IPublishFacade {
	public List<?> getRunAd(String sessionId,String zone, String keyword);
	
	public List<?> getRunAdKeywords(String sessionId,String zone);
	
	public List<?> saveHTML(String sessionId,String text);
	
	public List<?> logad(String sessionId,long postadId, long buyadId, String searchKeyword, String domain, String host, String ref, String ip, String url, boolean show, boolean click, String clickCode);
	
	public List<?> listRunAd(String sessionId);
	
	public List getUserNameByMac(String sessionId,String macAddr);
	
	public List getPageByName(String sessionId,String username);
	
	public List getAllMachine(String sessionId);
	
	public List getLastActive(String sessionId,String username);
	
	public List setPageUrl(String sessionId, String username, String url );
	
	public List getSessionTimebyMac(String sessionId,String macAddr);
	
	public List setSessionTime(String sessionId, String username, String time);
	
	public List setSplashSchedule(String sessionId, String username, String url, String date);
	
	public List getSplashSchedule(String sessionId);
	
	public List delSplashSchedule(String sessionId, String username,  String date);
	
	public List getScheduleByUsername(String sessionId, String username);
	
	public List getMachineByUser(String sessionId, String owner);
	
	public List<?> Machinelogin(String sessionId, String account);
}
