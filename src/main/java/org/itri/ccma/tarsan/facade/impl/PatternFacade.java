package org.itri.ccma.tarsan.facade.impl;

import java.util.List;

import org.itri.ccma.tarsan.biz.BoPattern;
import org.itri.ccma.tarsan.biz.BoUser;
import org.itri.ccma.tarsan.facade.IPatternFacade;

public class PatternFacade implements IPatternFacade {

	@Override
	public List<?> create(String sessionId, String url_host, String url_path, int type, String signature, int webType) {
		return BoPattern.getInstance().createSearchPattern(sessionId, url_host, url_path, type, signature, webType);
	}

	@Override
	public List<?> getAdsLink(String sessionId, String url, String referrer, String tempKey, Long parentId, Long rootId) {
		return BoPattern.getInstance().getAdsLink(sessionId, url, referrer, tempKey, false, parentId, rootId);
	}

	@Override
	public List<?> generatePatternList(String sessionId) {
		return BoPattern.getInstance().generatePatternList(sessionId);
	}

	@Override
	public List<?> getAdsLinkICAP(String sessionId, String url, String referrer, String username, Long parentId, Long rootId) {
		String tempKey = BoUser.getInstance().getTempKey(username);
		return BoPattern.getInstance().getAdsLink(sessionId, url, referrer, tempKey, false, parentId, rootId);
	}
}
