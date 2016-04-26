package org.itri.ccma.tarsan.facade.impl;

import java.util.List;

import org.itri.ccma.tarsan.biz.BoAds;
import org.itri.ccma.tarsan.facade.IAdsFacade;

public class AdsFacade implements IAdsFacade {

	@Override
	public List<?> createAds(String sessionId, String keywords, String url, String img, Float weight) {
		return BoAds.getInstance().create(sessionId, keywords, url, img, weight);
	}

	@Override
	public List<?> touch(String sessionId, Long adsId, Long ad2userId, Long userId, Long timestamp, String identifier) {
		return BoAds.getInstance().touch(sessionId, adsId, ad2userId, userId, timestamp, identifier);
	}
}
