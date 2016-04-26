package org.itri.ccma.tarsan.facade.impl;

import java.util.List;

import org.itri.ccma.tarsan.biz.BoUser;
import org.itri.ccma.tarsan.facade.IUserFacade;

/**
 * The Class UserFacade.
 */
public class UserFacade implements IUserFacade {

	/*
	 * (non-Javadoc)
	 * 
	 * Create a new user using account and password
	 */
	public List<?> createUser(String sessionId, String account, String password) {
		return BoUser.getInstance().createUser(sessionId, account, password);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.itri.ccma.ubike.facade.IUserFacade#login(java.lang.String,
	 * java.lang.String)
	 * 
	 * Login with account, password, email
	 */
	public List<?> login(String sessionId, String account, String password) {
		return BoUser.getInstance().login(sessionId, account, password);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.itri.ccma.tarsan.facade.IUserFacade#logUserEvent(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.Long, java.lang.Long)
	 * 
	 * Log user browsing history
	 */
	public List<?> logUserEvent(String sessionId, String url, String referrer, Long userId, Long patternId, String ip, boolean productSearchFlag, Long parentId, Long rootId) {
		return BoUser.getInstance().logUserEvent(sessionId, url, referrer, userId, patternId, ip, productSearchFlag, parentId, rootId);
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.itri.ccma.tarsan.facade.IUserFacade#logUserEventICAP(java.lang.
	 * String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.Long, java.lang.String)
	 * Log user browsing history from ICAP server
	 */
	public List<?> logUserEventICAP(String sessionId, String url, String referrer, String username, Long patternId,
			String ip, boolean productSearchFlag, Long parentId, Long rootId) {
		Long userId = BoUser.getInstance().getUserId(username);
		return logUserEvent(sessionId, url, referrer, userId, patternId, ip, productSearchFlag, parentId, rootId);
	}
}
