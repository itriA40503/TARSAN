package org.itri.ccma.tarsan.biz;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.itri.ccma.tarsan.biz.exceptions.LogicException;
import org.itri.ccma.tarsan.common.SessionMap;
import org.itri.ccma.tarsan.hibernate.Pattern;
import org.itri.ccma.tarsan.hibernate.Userevent;
import org.itri.ccma.tarsan.hibernate.Users;
import org.itri.ccma.tarsan.util.Configurations;
import org.itri.ccma.tarsan.util.HibernateUtil;
import org.itri.ccma.tarsan.util.HttpUtil;
import org.itri.ccma.tarsan.util.InputValidator;
import org.itri.ccma.tarsan.util.MessageUtil;
import org.itri.ccma.tarsan.util.SignatureUtil;

/**
 * The Class BoUser.
 */
public class BoUser {

	/** The logger. */
	private static Logger logger = Logger.getLogger("consoleAppender");

	/* Singleton */
	private BoUser() {
		if (Configurations.IS_DEBUG)
			logger.setLevel(Level.ALL);
	}

	public static BoUser getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {

		private static final BoUser INSTANCE = new BoUser();
	}

	/****************************************************************************************/
	/*
	 * Utility function
	 */
	/****************************************************************************************/

	// generate unique tmp_key
	public String createTmpKey(String password) {
		long unixTime = System.currentTimeMillis() / 1000L;
		String encodeString = Long.toString(unixTime) + password;
		String tmpKey = SignatureUtil.createHash("md5", encodeString);

		return tmpKey;
	}

	// retrieve user id from username
	public Long getUserIdFromKey(String tempKey) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		// Search for user object
		Criteria criteria_account = session.createCriteria(Users.class);
		criteria_account.add(Restrictions.eq("tempkey", tempKey));
		criteria_account.add(Restrictions.eq("deletedFlag", false));
		Users user = (Users) criteria_account.uniqueResult();

		return user == null ? -1L : user.getUsersId();
	}

	// retrieve user id from username
	public Long getUserId(String username) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		// Search for user object
		Criteria criteria_account = session.createCriteria(Users.class);
		criteria_account.add(Restrictions.eq("account", username));
		criteria_account.add(Restrictions.eq("deletedFlag", false));
		Users user = (Users) criteria_account.uniqueResult();

		return user == null ? -1L : user.getUsersId();
	}

	// retrieve tempKey from username
	public String getTempKey(String username) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		// Search for user object
		Criteria criteria_account = session.createCriteria(Users.class);
		criteria_account.add(Restrictions.eq("account", username));
		criteria_account.add(Restrictions.eq("deletedFlag", false));
		Users user = (Users) criteria_account.uniqueResult();

		return user == null ? null : user.getTempkey();
	}

	// retrieve tempKey from usersId
	public String getTempKeyFromId(String usersId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		// Search for user object
		Criteria criteria_account = session.createCriteria(Users.class);
		criteria_account.add(Restrictions.eq("usersId", usersId));
		criteria_account.add(Restrictions.eq("deletedFlag", false));
		Users user = (Users) criteria_account.uniqueResult();

		return user == null ? null : user.getTempkey();
	}

	/**
	 * 
	 * @param sessionId
	 * @param url
	 * @param domain
	 * @param username
	 * @param patternId
	 * @param ip
	 * @param productSearchFlag
	 * @return
	 */
	public List<?> logUserEventICAP(String sessionId, String url, String referrer, String username, Long patternId,
			String ip, boolean productSearchFlag, Long parentId, Long rootId) {
		Long userId = BoUser.getInstance().getUserId(username);
		return logUserEvent(sessionId, url, referrer, userId, patternId, ip, productSearchFlag, parentId, rootId);
	}

	/**
	 * Log user click event <br/>
	 * Flow process: <br/>
	 * 1. Check if pattern exist, if exist, match the pattern with user event.
	 * <br/>
	 * 2. Create new record on userevent table, by using pattern (if applicable)
	 * and user information. <br/>
	 * 
	 * @param sessionId
	 * @param url
	 * @param domain
	 * @param userId
	 * @param patternId
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public List<?> logUserEvent(String sessionId, String url, String referrer, Long userId, Long patternId, String ip,
			boolean productSearchFlag, Long parentId, Long rootId) {
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

		Date currentDate = new Date();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		try {
			// Extract domain from URL
			String domain;
			try {
				domain = HttpUtil.getInstance().getDomainName(url);	
				
			} catch (MalformedURLException e) {
				throw new LogicException("MalformedURLException: " + e.getMessage());
			}
			// Search for user object
			Criteria criteria_account = session.createCriteria(Users.class);
			criteria_account.add(Restrictions.eq("usersId", userId));
			criteria_account.add(Restrictions.eq("deletedFlag", false));
			Users user = (Users) criteria_account.uniqueResult();

			// Search for pattern object
			Pattern pattern = null;
			if (patternId > 0) {
				Criteria criteria = session.createCriteria(Pattern.class);
				criteria.add(Restrictions.eq("patternId", patternId));
				criteria.add(Restrictions.eq("deletedFlag", false));
				criteria.add(Restrictions.eq("expiredFlag", false));
				pattern = (Pattern) criteria.uniqueResult();
			}
			
			// Check if previous event in this website is a product search mode
			// Using TIME FRAME to limit (in MINUTES)
//			Date maxDate = new Date(currentDate.getTime() - TimeUnit.MINUTES.toMillis(Configurations.BROWSING_TIME_FRAME));
//			Criteria criteria = session.createCriteria(Userevent.class);
//			criteria.add(Restrictions.ge("createdDateTime", maxDate));
//			criteria.add(Restrictions.eq("urlHost", domain));
////			criteria.add(Restrictions.eq("sessionId", sessionId));
//			criteria.add(Restrictions.eq("productSearchFlag", true));
//			List list = criteria.list();
//			System.out.println("~~~~~~~ List: " + list.size());
//			if(!list.isEmpty()) {
//				productSearchFlag = true;
//			}
			
			// Remove " because it would create a lot of errors
			url = url.replaceAll("(\\\"|\\%22|\\%2522)+", "");

			// URL Normalization (HTML encoding back to normal string)
			url = HttpUtil.getInstance().HTML_Decoding(url);
			
			// Change Session ID to User's tempkey
//			sessionId = user.getTempkey();

			// Create the object to be saved
			Userevent ue = new Userevent();
			ue.setCreatedDateTime(currentDate);
			ue.setSessionId(sessionId);
			ue.setUrlPath(url);
			ue.setUrlHost(domain);
			ue.setUsers(user);
			ue.setPattern(pattern);
			ue.setIp(ip);
			ue.setProductSearchFlag(productSearchFlag);
			ue.setUrlReferer(referrer);
			ue.setParentId(parentId);
			ue.setRootId(rootId);
			session.persist(ue);

			// Commit those persist
			tx.commit();

			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, "user event has been logged", "url", url, "domain", domain);
		} catch (LogicException e) {
			if (Configurations.IS_DEBUG) {
				logger.debug("[FAILURE] methodName: " + methodName);
				logger.debug("[FAILURE] message: " + MessageUtil.getInstance().generateResponseMessage(e.getErrorCode(),
						methodName, null, e.getMessage(), e.getParams()));
			}
			resultList = MessageUtil.getInstance().generateResponseMessage(e.getErrorCode(), methodName, null,
					e.getMessage(), e.getParams());
		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
					"0", e.getMessage());
			tx.rollback();
		} finally {
			session.close();
		}

		return resultList;
	}

	/****************************************************************************************/
	/*
	 * Logical function
	 */
	/****************************************************************************************/

	/**
	 * Login with account, password, email
	 */
	@SuppressWarnings({ "rawtypes" })
	public List<?> login(String sessionId, String account, String password) {
		// public JSONObject login(String sessionId, String encryptedText) {
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		account = HttpUtil.getInstance().HTML_Decoding(account);

		try {
			Date currentDate = new Date();

			Criteria criteria_account = session.createCriteria(Users.class);
			criteria_account.add(Restrictions.eq("account", account));
			criteria_account.add(Restrictions.eq("deletedFlag", false));
			Users user = (Users) criteria_account.uniqueResult();

			// password = SignatureUtil.createHash("md5", password);
			if (user == null)
				throw new LogicException("Account does not exist", Configurations.CODE_NOT_EXIST, "account", account);

			if (!user.getPassword().equals(password))
				throw new LogicException("Invalid password", Configurations.CODE_FORBIDDEN);

			String tmpKey = createTmpKey(password);
			// String tmpPwd = SignatureUtil.createHash("md5",
			// Long.toString((System.currentTimeMillis() / 1000L)));
			user.setTempkey(tmpKey);
			// user.setTmpPwd(tmpPwd);
			// user.setTmpKeyLastUpdated(currentDate);
			user.setLastActiveDateTime(currentDate);
			session.persist(user);
			//session.update(user);
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, "User " + user.getAccount() + " successfully logged in", "tmp_key", tmpKey);

			SessionMap.addSession(sessionId, account);

			tx.commit();
		} catch (LogicException e) {
			if (Configurations.IS_DEBUG) {
				logger.debug("[FAILURE] methodName: " + methodName);
				logger.debug("[FAILURE] message: " + MessageUtil.getInstance().generateResponseMessage(e.getErrorCode(),
						methodName, null, e.getMessage(), e.getParams()));
			}
			tx.rollback();
			resultList = MessageUtil.getInstance().generateResponseMessage(e.getErrorCode(), methodName, null,
					e.getMessage(), e.getParams());
		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
			tx.rollback();
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
					"0", e.getMessage());
		} finally {
			session.close();
		}
		return resultList;
	}

	@SuppressWarnings({ "rawtypes" })
	public List<?> createUser(String sessionId, String account, String password) {
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

		boolean isPasswordOk = InputValidator.validator("password", password);
		boolean isAccountOK = InputValidator.validator("password", account);

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		try {
			if (!isAccountOK)
				throw new LogicException(
						"Account format is incorrect. Account must only contains alphanumerical character"
								+ " and some special characters (?, !, @, _, $, ~)."
								+ " Account length need to be at least 3 character and can't be very long.",
						Configurations.CODE_FORBIDDEN, "account", account);
			if (!isPasswordOk)
				throw new LogicException(
						"Password format is incorrect. Password must only contains alphanumerical character"
								+ " and some special characters (?, !, @, _, $, ~)."
								+ " Password length need to be at least 3 character and can't be very long.",
						Configurations.CODE_FORBIDDEN, "account", account);
			// check if user account already exist,
			// and the isDeleted tag is false
			Criteria criteria_account = session.createCriteria(Users.class);
			criteria_account.add(Restrictions.eq("account", account));
			criteria_account.add(Restrictions.eq("deletedFlag", false));
			List result_account = criteria_account.list();

			if (result_account.size() != 0)
				throw new LogicException(String.format("Account with name [%s] is already registered", account),
						Configurations.CODE_ERROR);

			// Create the new user
			Date currentDate = new Date();
			Users user = new Users();
			user.setAccount(account);
			user.setPassword(password);
			user.setTempkey(account);
			user.setDeletedFlag(false);
			user.setCreatedDateTime(currentDate);
			user.setLastActiveDateTime(currentDate);
			session.persist(user);

			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, "New user is successfully created", "account", account);
			tx.commit();
		} catch (LogicException e) {
			if (Configurations.IS_DEBUG) {
				logger.debug("[FAILURE] methodName: " + methodName);
				logger.debug("[FAILURE] message: " + MessageUtil.getInstance().generateResponseMessage(e.getErrorCode(),
						methodName, null, e.getMessage(), e.getParams()));
			}
			tx.rollback();
			resultList = MessageUtil.getInstance().generateResponseMessage(e.getErrorCode(), methodName, null,
					e.getMessage(), e.getParams());
		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
			tx.rollback();
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
					"0", e.getMessage());
		} finally {
			session.close();
		}
		return resultList;
	}
}
