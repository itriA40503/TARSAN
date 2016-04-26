package org.itri.ccma.tarsan.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.itri.ccma.tarsan.biz.exceptions.LogicException;
import org.itri.ccma.tarsan.common.SessionMap;
import org.itri.ccma.tarsan.hibernate.Denykeyword;
import org.itri.ccma.tarsan.hibernate.Exuser;
import org.itri.ccma.tarsan.hibernate.Exusergroup;
import org.itri.ccma.tarsan.hibernate.Exuserlog;
import org.itri.ccma.tarsan.hibernate.Users;
import org.itri.ccma.tarsan.util.Configurations;
import org.itri.ccma.tarsan.util.HibernateUtil;
import org.itri.ccma.tarsan.util.HttpUtil;
import org.itri.ccma.tarsan.util.MessageUtil;

public class BoExchange {
	/** The logger. */
	private static Logger logger = Logger.getLogger("consoleAppender");

	/* Singleton */
	private BoExchange() {
		if (Configurations.IS_DEBUG)
			logger.setLevel(Level.ALL);
	}

	public static BoExchange getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {

		private static final BoExchange INSTANCE = new BoExchange();
	}
	
	/****************************************************************************************/
	/*
	 * Utility function
	 */
	/****************************************************************************************/

	
	
	/****************************************************************************************/
	/*
	 * Logical function
	 */
	/****************************************************************************************/
	
	public List<?> createExUser(String sessionId, String account, String password, String address, String phone ) {
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		try {

			Date currentDate = new Date();
			Exuser exuser = new Exuser();
			exuser.setAccount(account);
			exuser.setAddress(address);
			exuser.setPassword(password);
			exuser.setPhone(phone);
			exuser.setCreatedate(currentDate);
			session.save(exuser);
			tx.commit();
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, "New ExUser is successfully created", "account", account);

		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		} finally {
			session.close();
		}
		return resultList;
	}
	
	public List<?> ExUserlogin(String sessionId, String account, String password) {
		// public JSONObject login(String sessionId, String encryptedText) {
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		account = HttpUtil.getInstance().HTML_Decoding(account);

		try {
			Date currentDate = new Date();

			Criteria criteria_account = session.createCriteria(Exuser.class);
			criteria_account.add(Restrictions.eq("account", account));

			Exuser user = (Exuser) criteria_account.uniqueResult();
			Exuserlog log = new Exuserlog(); 
			Exusergroup group = new Exusergroup();
			long userID = user.getExuserId();
			// password = SignatureUtil.createHash("md5", password);
			if (user == null)
				throw new LogicException("Account does not exist", Configurations.CODE_NOT_EXIST, "account", account);

			if (!user.getPassword().equals(password))
				throw new LogicException("Invalid password", Configurations.CODE_FORBIDDEN);
			group.setRemark("OK");
			user.setLastlogin(currentDate);
			log.setActivity("Login");
			log.setExuser(user);
			log.setCreatedate(currentDate);
			session.save(log);
			session.persist(user);
			//session.update(user);
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, "ExUser " + user.getAccount() + " successfully logged in","ExuserId",userID);

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
	
	public List<?> denyKeyword(String sessionId, String dKeyword){
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		try {
			Denykeyword kw =new Denykeyword();
			kw.setDKeyword(dKeyword);
			session.save(kw);
			tx.commit();
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, "New Denykeyword is successfully Added", "Denykeyword", dKeyword);

		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		} finally {
			session.close();
		}
		return resultList;
	}	
	
	public List<?> ExUserloginForTest(String sessionId, String account, String password) {
		// public JSONObject login(String sessionId, String encryptedText) {
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		account = HttpUtil.getInstance().HTML_Decoding(account);

		try {
			Date currentDate = new Date();

			Criteria criteria_account = session.createCriteria(Exuser.class);
			criteria_account.add(Restrictions.eq("account", account));

			Exuser user = (Exuser) criteria_account.uniqueResult();
			Exuserlog log = new Exuserlog(); 
			Exusergroup group = new Exusergroup();
			long userID = user.getExuserId();
			// password = SignatureUtil.createHash("md5", password);
			if (user == null)
				throw new LogicException("Account does not exist", Configurations.CODE_NOT_EXIST, "account", account);

			if (!user.getPassword().equals(password))
				throw new LogicException("Invalid password", Configurations.CODE_FORBIDDEN);
			group.setRemark("OK");
			user.setLastlogin(currentDate);
			log.setActivity("Login");
			log.setExuser(user);
			log.setCreatedate(currentDate);
			session.save(log);
			session.persist(user);
			//session.update(user);
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, "ExUser " + user.getAccount() + " successfully logged in","ExuserId",userID);

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
	
}
