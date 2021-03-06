package org.itri.ccma.tarsan.facade.impl;

import java.util.List;

import org.itri.ccma.tarsan.biz.BoAdPublish;
import org.itri.ccma.tarsan.facade.IPublishFacade;

public class PublishFacade implements IPublishFacade{

	@Override
	public List<?> getRunAd(String sessionId, String zone, String keyword) {
		// TODO Auto-generated method stub
		return BoAdPublish.getInstance().getRunAd(sessionId, zone, keyword);
	}

	@Override
	public List<?> getRunAdKeywords(String sessionId, String zone) {
		// TODO Auto-generated method stub
		return BoAdPublish.getInstance().getRunAdKeywords(sessionId, zone);
	}

	@Override
	public List<?> saveHTML(String sessionId,String text) {
		// TODO Auto-generated method stub
		return BoAdPublish.getInstance().saveHTML(sessionId, text);
	}

	@Override
	public List<?> logad(String sessionId, long postadId, long buyadId,
			String searchKeyword, String domain, String host, String ref,
			String ip, String url, boolean show, boolean click, String clickCode) {
		// TODO Auto-generated method stub
		return BoAdPublish.getInstance().logad(sessionId, postadId, buyadId, searchKeyword, domain, host, ref, ip, url, show, click, clickCode);
	}

	@Override
	public List<?> listRunAd(String sessionId) {
		// TODO Auto-generated method stub
		return BoAdPublish.getInstance().listRunAd(sessionId);
	}

	@Override
	public List getUserNameByMac(String sessionId, String macAddr) {
		// TODO Auto-generated method stub
		return BoAdPublish.getInstance().getUserNameByMac(sessionId, macAddr);
	}

	@Override
	public List getPageByName(String sessionId, String username) {
		// TODO Auto-generated method stub
		return BoAdPublish.getInstance().getPageByName(sessionId, username);
	}

	@Override
	public List getAllMachine(String sessionId) {
		// TODO Auto-generated method stub
		return BoAdPublish.getInstance().getAllMachine(sessionId);
	}

	@Override
	public List getLastActive(String sessionId, String username) {
		// TODO Auto-generated method stub
		return BoAdPublish.getInstance().getLastActive(sessionId, username);		
	}

	@Override
	public List setPageUrl(String sessionId, String username, String url) {
		// TODO Auto-generated method stub
		return BoAdPublish.getInstance().setPageUrl(sessionId, username, url);
	}

	@Override
	public List getSessionTimebyMac(String sessionId, String macAddr) {
		// TODO Auto-generated method stub
		return BoAdPublish.getInstance().getSessionTimebyMac(sessionId, macAddr);
	}

	@Override
	public List setSessionTime(String sessionId, String username, String time) {
		// TODO Auto-generated method stub
		return BoAdPublish.getInstance().setSessionTime(sessionId, username, time);
	}

	@Override
	public List setSplashSchedule(String sessionId, String username, String url,
			String date) {
		// TODO Auto-generated method stub
		return BoAdPublish.getInstance().setSplashSchedule(sessionId, username, url, date);
	}

	@Override
	public List getSplashSchedule(String sessionId) {
		// TODO Auto-generated method stub
		return BoAdPublish.getInstance().getSplashSchedule(sessionId);
	}

	@Override
	public List delSplashSchedule(String sessionId, String username, String date) {
		// TODO Auto-generated method stub
		return BoAdPublish.getInstance().delSplashSchedule(sessionId, username, date);
	}

	@Override
	public List getScheduleByUsername(String sessionId, String username) {
		// TODO Auto-generated method stub
		return BoAdPublish.getInstance().getScheduleByUsername(sessionId, username);
	}

	@Override
	public List getMachineByUser(String sessionId, String owner) {
		// TODO Auto-generated method stub
		return BoAdPublish.getInstance().getMachineByUser(sessionId, owner);
	}

	@Override
	public List<?> Machinelogin(String sessionId, String account) {
		// TODO Auto-generated method stub
		return BoAdPublish.getInstance().Machinelogin(sessionId, account);
	}

	@Override
	public List connectLog(String sessionId, String ip, String type, String os,
			String browser, String brand, String mac, String ssid,
			String machine, String url) {
		// TODO Auto-generated method stub
		return BoAdPublish.getInstance().connectLog(sessionId, ip, type, os, browser, brand, mac, ssid, machine, url);
	}

	@Override
	public List getConnectLogByUsername(String sessionId, String username) {
		// TODO Auto-generated method stub
		return BoAdPublish.getInstance().getConnectLogByUsername(sessionId, username);
	}
	
	
}
