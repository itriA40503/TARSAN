package org.itri.ccma.tarsan.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.itri.ccma.tarsan.biz.exceptions.LogicException;
import org.itri.ccma.tarsan.hibernate.Ad;
import org.itri.ccma.tarsan.hibernate.Ad2user;
import org.itri.ccma.tarsan.hibernate.Users;
import org.itri.ccma.tarsan.util.Configurations;
import org.itri.ccma.tarsan.util.HibernateUtil;
import org.itri.ccma.tarsan.util.HttpUtil;
import org.itri.ccma.tarsan.util.MessageUtil;

public class BoAds {
	/** The logger. */
	private static Logger logger = Logger.getLogger("consoleAppender");

	/* Singleton */
	private BoAds() {
		if (Configurations.IS_DEBUG)
			logger.setLevel(Level.ALL);
	}

	public static BoAds getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {

		private static final BoAds INSTANCE = new BoAds();
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

	@SuppressWarnings({ "rawtypes" })
	public List<?> create(String sessionId, String keywords, String url, String img, float weight) {

		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		try {
			keywords = HttpUtil.getInstance().HTML_Decoding(keywords);

			Criteria criteria = session.createCriteria(Ad.class);
			criteria.add(Restrictions.eq("keywords", keywords));
			criteria.add(Restrictions.eq("url", url));
			Ad test = (Ad) criteria.uniqueResult();

			if (test != null)
				throw new LogicException("ads has already existed on the database", Configurations.CODE_ERROR);

			Date currentDate = new Date();

			Ad ads = new Ad();
			ads.setKeywords(keywords);
			ads.setUrl(url);
			ads.setImg(img);
			ads.setClickedTimes(0);
			ads.setShownTimes(0);
			ads.setWeight(weight);
			ads.setCreatedDateTime(currentDate);
			ads.setExpiredFlag(false);
			ads.setDeletedFlag(false);

			session.persist(ads);
			tx.commit();

			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, "new ads is successfully created", "ad_id", ads.getAdId());
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<?> touch(String sessionId, Long adId, Long ad2userId, Long userId, Long timestamp, String identifier) {
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		try {
			Criteria criteria = session.createCriteria(Ad.class);
			criteria.add(Restrictions.eq("adId", adId));
			Ad ad = (Ad) criteria.uniqueResult();

			if (ad == null)
				throw new LogicException("ads doesn't exist on the database", Configurations.CODE_NOT_EXIST);

			if ((ad.getClickedTimes() + 1) > ad.getShownTimes())
				throw new LogicException("Not allowed to add more click rate", Configurations.CODE_FORBIDDEN);

			if (ad != null) {
				// Search for corresponding user instance
				criteria = session.createCriteria(Users.class);
				criteria.add(Restrictions.eq("usersId", userId));
				Users user = (Users) criteria.uniqueResult();
				// Search for corresponding ad2user instance
				criteria = session.createCriteria(Ad2user.class);
				criteria.add(Restrictions.eq("ad", ad));
				criteria.add(Restrictions.eq("users", user));
				criteria.add(Restrictions.eq("clickedFlag", true));
				criteria.addOrder(Order.desc("createdDateTime"));
				List<Ad2user> list = (List<Ad2user>) criteria.list();
				// If can't found, then it would be bigger than threshold
				long lastTime = 0;
				if (!list.isEmpty()) {
					Ad2user last = list.get(0);
					lastTime = last.getCreatedDateTime().getTime();
				}

				// Check on time gap between ads click for the same user
				Date date = new Date();
				long now = date.getTime();
				if (Configurations.IS_DEBUG) {
					logger.debug("#### Time different between clicks");
					logger.debug(lastTime);
					logger.debug(now);
					logger.debug(now - lastTime);
				}

				if ((now - lastTime) < Configurations.ADS_DURATION) {
					// CODE_FORBIDDEN
					logger.debug("Configurations.CODE_FORBIDDEN");
				}

				// Search for corresponding ad2user instance
				criteria = session.createCriteria(Ad2user.class);
				criteria.add(Restrictions.eq("ad2userId", ad2userId));
				Ad2user ad2user = (Ad2user) criteria.uniqueResult();

				if (ad2user == null)
					throw new LogicException("No ad2user correspond to this ads", Configurations.CODE_NOT_EXIST);

				// Increase clicked times
				ad.setClickedTimes(ad.getClickedTimes() + 1);
				session.persist(ad);
				// Set flag of corresponding ads to be true
				ad2user.setClickedFlag(true);
				ad2user.setIdentifier(identifier);
				session.persist(ad2user);
				// Commit all actions
				tx.commit();

				resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
						sessionId, "ads is successfully touched", "ad_id", ad.getAdId(), "click", ad.getClickedTimes(), "identifier", identifier);

				if (Configurations.IS_DEBUG)
					logger.debug(resultList.toString());
			}
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

	/**
	 * 
	 * @param userId
	 * @param adsId
	 * @return OK if true.
	 */
	@SuppressWarnings("unchecked")
	public boolean getAdsAccessAvailability(Long userId, Long adsId) {
		boolean result = true;
		Session session = HibernateUtil.getSessionFactory().openSession();
		// Search for corresponding user instance
		Criteria criteria = session.createCriteria(Users.class);
		criteria.add(Restrictions.eq("usersId", userId));
		Users user = (Users) criteria.uniqueResult();
		// Search for corresponding ad instance
		criteria = session.createCriteria(Ad.class);
		criteria.add(Restrictions.eq("adId", adsId));
		Ad ad = (Ad) criteria.uniqueResult();
		// Search for corresponding ad2user instance
		criteria = session.createCriteria(Ad2user.class);
		criteria.add(Restrictions.eq("ad", ad));
		criteria.add(Restrictions.eq("users", user));
		criteria.addOrder(Order.desc("createdDateTime"));

		List<Ad2user> list = (List<Ad2user>) criteria.list();
		// If can't found, then it would be bigger than threshold
		Date date = new Date();
		long lastTime = 0;
		if (!list.isEmpty()) {
			Ad2user last = list.get(0);
			lastTime = last.getCreatedDateTime().getTime();
		}

		// Check on time gap between ads click for the same user
		long now = date.getTime();
		// if (Configurations.IS_DEBUG) {
		// logger.debug("#### Time different between clicks");
		// logger.debug("Last:" + lastTime + ":" + new Date(lastTime));
		// logger.debug("Now :" + now + ":" + new Date(now));
		// logger.debug(now - lastTime);
		// }

		if ((now - lastTime) < Configurations.ADS_DURATION)
			result = false;
		session.close();
		return result;
	}
}
