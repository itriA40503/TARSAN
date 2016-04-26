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

}
