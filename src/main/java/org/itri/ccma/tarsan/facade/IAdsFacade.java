package org.itri.ccma.tarsan.facade;

import java.util.List;

public interface IAdsFacade {
	/**
	 * 
	 * @param sessionId
	 * @param keywords
	 * @param url
	 * @param img
	 * @param weight
	 * @return
	 */
	public List<?> createAds(String sessionId, String keywords, String url, String img, Float weight);
	
	/**
	 * Handle ads click event
	 * @param sessionId
	 * @param adsId
	 * @param ad2userId
	 * @return
	 */
	public List<?> touch(String sessionId, Long adsId, Long ad2userId, Long userId, Long timestamp, String identifier);
}
