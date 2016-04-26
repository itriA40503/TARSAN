package org.itri.ccma.tarsan.facade;

import java.util.List;

/**
 * The Interface IUserFacade.
 */
public interface IUserFacade {

	/**
	 * Login.
	 * 
	 * @param sessionId
	 *            the session id
	 * @param account
	 *            the account id
	 * @param password
	 *            the password
	 * @return the list 
	 */
	//without encrypted text
	public List<?> login(String sessionId, String account, String password);
	
	
	/**
	 * Create a new user account using the phone number.
	 * 
	 * @param sessionId
	 *            the session id
	 * @param account
	 *            the account id
	 * @param password
	 *            the password
	 *
	 * @return the result message
	 */
	public List<?> createUser(String sessionId, String account, String password);
	
	/**
	 * log every browsing history of the users
	 * @param sessionId
	 * @param url
	 * @param userId
	 * @param patternId
	 * @return
	 */
	public List<?> logUserEvent(String sessionId, String url, String referrer, Long userId, Long patternId, String ip, boolean productSearchFlag, Long parentId, Long rootId);
	
	/**
	 * log every browsing history of the users from ICAP Server
	 * @param sessionId
	 * @param url
	 * @param username
	 * @param patternId
	 * @param ip
	 * @return
	 */
	public List<?> logUserEventICAP(String sessionId, String url, String referrer, String username, Long patternId, String ip, boolean productSearchFlag, Long parentId, Long rootId);
}
