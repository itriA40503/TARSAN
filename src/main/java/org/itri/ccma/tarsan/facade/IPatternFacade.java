package org.itri.ccma.tarsan.facade;

import java.util.List;

public interface IPatternFacade {
	/**
	 * Create a new search query pattern
	 * @param sessionId
	 * @param url_host hostname
	 * @param url_path path inside hostname
	 * @param type search query type (1. Normal, 2. Filename, 3. No path, 6. AJAX, 7. Referer, etc.)
	 * @param signature parameter name which indicates search keyword
	 * @param webType indicating the website as E-Commerce website (whitelist), search engine, news, normal website, etc.
	 * @return Result message
	 */
	public List<?> create(String sessionId, String url_host, String url_path, int type, String signature, int webType);
	
	/**
	 * Retrieve ads URL by providing website's URL and domain
	 * @param sessionId
	 * @param url
	 * @param tempKey
	 * @return
	 */
	public List<?> getAdsLink(String sessionId, String url, String referrer, String tempKey, Long parentId, Long rootId);
	
	/**
	 * Retrieve ads URL by providing website's URL and domain
	 * @param sessionId
	 * @param url
	 * @param username
	 * @return
	 */
	public List<?> getAdsLinkICAP(String sessionId, String url, String referrer, String username, Long parentId, Long rootId);
	
	/**
	 * Generate list of all pattern in the database
	 * @param sessionId
	 * @return
	 */
	public List<?> generatePatternList(String sessionId);
	
	

}
