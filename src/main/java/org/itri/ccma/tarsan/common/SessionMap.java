package org.itri.ccma.tarsan.common;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.itri.ccma.tarsan.hibernate.Users;
import org.itri.ccma.tarsan.util.HibernateUtil;

/**
 * Store the mapping of account and session
 * 
 * @author User
 */
public class SessionMap
{

	private static Logger logger = Logger.getLogger(SessionMap.class);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Map<String, String> sessionMap = new HashMap();

	private SessionMap() {
	}

	/**
	 * store account and sessionId in SessionMap
	 * 
	 * @param sessionId
	 * @param account
	 * @param password
	 */
	public static void addSession(String sessionId, String account) {

		// check if the records exists in the users table
		// if not--> insert new one
		// put the sessionID, accoutId in static Map

//		if (!checkAccount(account)) {
//			createAccount(account, password);
//		} else {
//			updateAccount(account);
//		}
		
		//sessionId becomes the account once it's varified
		sessionMap.put(sessionId, account);
	}

	/**
	 * get the account by sessionId
	 * 
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	public static String getAccount(String sessionId) throws SessionTimeoutException {
		String account = sessionMap.get(sessionId);
		if (account == null) {
			throw new SessionTimeoutException("Session not found, please login first.");
		} else {
			return account;
		}
	}

	/**
	 * insert new record into users table
	 * 
	 * @param account
	 * @param password
	 */
	private static void createAccount(String account, String password) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		
		try {
			Date currentDate = new Date();
			//02/17/2015 This still points to the User class in ccma.ubike, instead of ccma.msb
			Users user = new Users();
			user.setAccount(account);
			// Don't record password
			user.setPassword("XXXX");
			
			//commented out on 02/17/2015 since there are no such fields for Tarsan
			//user.setLastActiveTime(currentDate);
			//user.setCreatedTime(currentDate);
			
			//additional info for Tarsan
			user.setLastActiveDateTime(currentDate);
			user.setCreatedDateTime(currentDate);
			
			tx = session.beginTransaction();
			session.save(user);
			tx.commit();
		} finally {
			HibernateUtil.getSessionFactory().getCurrentSession().close();
		}

	}

	/**
	 * update active_last_time
	 * 
	 * @param account
	 * @param password
	 */
	private static void updateAccount(String account) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			Date currentDate = new Date();

			String updateSQL = "update Users  set last_updated = :last_updated  where account = :account";
			Query query = session.createQuery(updateSQL);
			query.setTimestamp("last_updated", currentDate);
			query.setString("account", account);
			logger.debug("updateSQL:" + updateSQL);
			query.executeUpdate();

			tx.commit();
		} finally {
			HibernateUtil.getSessionFactory().getCurrentSession().close();
		}

	}

	/**
	 * check if the account exists in users table
	 * 
	 * @param account
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static boolean checkAccount(String account) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Criteria criteria = session.createCriteria(Users.class);
			criteria.add(Restrictions.eq("account", account));
			List result = criteria.list();
			return (result.size() > 0);
		} finally {
			HibernateUtil.getSessionFactory().getCurrentSession().close();
		}

	}
}
