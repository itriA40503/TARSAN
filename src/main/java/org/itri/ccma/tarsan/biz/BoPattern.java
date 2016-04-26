package org.itri.ccma.tarsan.biz;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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
import org.itri.ccma.tarsan.hibernate.Pattern;
import org.itri.ccma.tarsan.hibernate.Pattern2ad;
import org.itri.ccma.tarsan.hibernate.Userevent;
import org.itri.ccma.tarsan.hibernate.Users;
import org.itri.ccma.tarsan.util.Configurations;
import org.itri.ccma.tarsan.util.HibernateUtil;
import org.itri.ccma.tarsan.util.HttpUtil;
import org.itri.ccma.tarsan.util.MessageUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BoPattern {
	/** The logger. */
	private static Logger logger = Logger.getLogger("consoleAppender");

	/* Singleton */
	private BoPattern() {
		if (Configurations.IS_DEBUG)
			logger.setLevel(Level.ALL);
	}

	public static BoPattern getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {

		private static final BoPattern INSTANCE = new BoPattern();
	}

	/****************************************************************************************/
	/*
	 * Utility function
	 */
	/****************************************************************************************/

	/**
	 * Log ads shown history <br/>
	 * Flow process: <br/>
	 * 1. Check if pattern match with some ads. If yes, go to 2(a), else go to
	 * 2(b). <br/>
	 * 2(a). Apply some algorithm to find desired ads, increment shown times,
	 * then return the ads. <br/>
	 * 2(b). For testing purpose, generate new ads based on the information
	 * here. Otherwise, return exception <br/>
	 * 3. Create new record on pattern2ad table, if both ad and pattern exist.
	 * <br/>
	 * 4. Create new record on ad2user table, by using ad and user information.
	 * <br/>
	 * 
	 * @param sessionId
	 * @param userId
	 * @param patternId
	 * @param ads_url
	 * @param keywords
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<?> logAds(String sessionId, long userId, long patternId, String ads_url, String keywords,
			String identifier, Session session) {
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

		Date currentDate = new Date();
		Transaction tx = null;
		boolean isTesting = false;
		if (session == null) {
			isTesting = true;
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
		}

		try {
			// Search for pattern object
			Pattern pattern = null;
			if (patternId > 0) {
				Criteria criteria = session.createCriteria(Pattern.class);
				criteria.add(Restrictions.eq("patternId", patternId));
				criteria.add(Restrictions.eq("deletedFlag", false));
				criteria.add(Restrictions.eq("expiredFlag", false));
				pattern = (Pattern) criteria.uniqueResult();
			}
			if (pattern == null)
				throw new LogicException("Pattern is not found");

			// Search on related pattern to ad
			Criteria criteria_pattern_ad = session.createCriteria(Pattern2ad.class);
			criteria_pattern_ad.add(Restrictions.eq("pattern", pattern));
			List<Pattern2ad> p2ad_result = ((List<Pattern2ad>) criteria_pattern_ad.list());
			// if (Configurations.IS_DEBUG)
			// logger.debug(p2ad_result.size());
			// Pick one ads using an algorithm
			Ad ad = getAds(p2ad_result);

			if (Configurations.IS_DEBUG) {
				if (ad != null) {
					logger.debug("# Ad info");
					logger.debug(ad.getAdId());
					logger.debug(ad.getUrl());
					logger.debug(ad.getKeywords());
				}
			}

			// For testing purpose
			// Create a new ad
			// If deployed, use this
			// throw new LogicException("Ad is not found");
			if (ad == null) {
				keywords = HttpUtil.getInstance().HTML_Decoding(keywords);

				List result = BoAds.getInstance().create(sessionId, keywords, ads_url, null, 1.0F);
				String status = ((Map) result.get(0)).get("status").toString();
				if (!status.equals(Configurations.CODE_OK)) {
					throw new Exception("Couldn't create new Ad : " + result.toString());
				}
				// Use the ad that is just created
				Criteria criteria_ad = session.createCriteria(Ad.class);
				criteria_ad.add(Restrictions.eq("keywords", keywords));
				criteria_ad.add(Restrictions.eq("url", ads_url));
				criteria_ad.add(Restrictions.eq("expiredFlag", false));
				criteria_ad.add(Restrictions.eq("deletedFlag", false));
				ad = (Ad) criteria_ad.uniqueResult();
				if (ad == null)
					throw new LogicException("Ad is not found", "url", ads_url, "keywords", keywords);
				logger.info(keywords);
				logger.info(ads_url);
				ad.setShownTimes(1);
			}
			if (ad != null) {
				// Check last seen by this user
				boolean ok = BoAds.getInstance().getAdsAccessAvailability(userId, ad.getAdId());

				// Increment shown times
				if (ok) {
					if (Configurations.IS_DEBUG)
						logger.debug("About to update shown times for Ad: " + ad.getAdId());
					ad.setShownTimes(ad.getShownTimes() + 1);
				} else {
					throw new LogicException("Violates ad showing policy", Configurations.CODE_FORBIDDEN, "ad",
							ad.getAdId(), "url", ad.getUrl());
				}
			}
			session.persist(ad);

			// Look up Pattern2ad instance
			Criteria cp2ad = session.createCriteria(Pattern2ad.class);
			cp2ad.add(Restrictions.eq("ad", ad));
			cp2ad.add(Restrictions.eq("pattern", pattern));
			Pattern2ad p2ad = (Pattern2ad) cp2ad.uniqueResult();
			if (p2ad == null) {
				// If no record found, then
				// Create a new Pattern2ad object and save into database
				p2ad = new Pattern2ad();
				p2ad.setAd(ad);
				p2ad.setPattern(pattern);
				session.persist(p2ad);
			}

			// Search for user object
			Criteria criteria_account = session.createCriteria(Users.class);
			criteria_account.add(Restrictions.eq("usersId", userId));
			criteria_account.add(Restrictions.eq("deletedFlag", false));
			Users user = (Users) criteria_account.uniqueResult();

			// Create a new ad2user object and save into database
			Ad2user ad2user = new Ad2user();
			ad2user.setAd(ad);
			ad2user.setUsers(user);
			ad2user.setCreatedDateTime(currentDate);
			ad2user.setClickedFlag(false);
			ad2user.setIdentifier(identifier);
			session.persist(ad2user);

			// Commit those persist (for testing by itself)
			if (isTesting)
				tx.commit();

			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, "ad has been logged", "ad", ad.getAdId(), "ad2user", ad2user.getAd2userId(), "temp_key",
					user.getTempkey());
		} catch (LogicException e) {
			if (Configurations.IS_DEBUG) {
				logger.debug("[FAILURE] methodName: " + methodName);
				logger.debug("[FAILURE] message: " + MessageUtil.getInstance().generateResponseMessage(e.getErrorCode(),
						methodName, null, e.getMessage(), e.getParams()));
				logger.debug("[FAILURE] patternId: " + patternId);
				logger.debug("[FAILURE] userId: " + userId);
			}
			resultList = MessageUtil.getInstance().generateResponseMessage(e.getErrorCode(), methodName, null,
					e.getMessage(), e.getParams());
			if (isTesting)
				tx.rollback();
		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
					"0", e.getMessage());
			if (isTesting)
				tx.rollback();
		} finally {
			if (isTesting)
				session.close();
		}

		return resultList;
	}

	/**
	 * Get ads by using algorithm Here we first use random algorithm but later
	 * more sophisticated algorithm could be used
	 * 
	 * @param list
	 * @return
	 */
	private Ad getAds(List<Pattern2ad> list) {
		Ad result = null;
		int index = -1;
		if (list.size() > 0) {
			Random r = new Random();
			while (result == null && list.size() > 0) {
				index = r.nextInt(list.size());
				result = list.get(index).getAd();
				if (result.isDeletedFlag() || result.isExpiredFlag()) {
					result = null;
					list.remove(index);
				}
			}
			return result;
		}
		return result;
	}

	/****************************************************************************************/
	/*
	 * Logical function
	 */
	/****************************************************************************************/

	@SuppressWarnings({ "rawtypes" })
	public List<?> createSearchPattern(String sessionId, String url_host, String url_path, int patternType,
			String signature, int webType) {

		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		try {
			if (url_path != null && url_path.isEmpty())
				url_path = null;
			if (signature != null && signature.isEmpty())
				signature = null;

			Criteria criteria = session.createCriteria(Pattern.class);
			criteria.add(Restrictions.eq("urlHost", url_host));
			if (url_path != null)
				criteria.add(Restrictions.eq("urlPath", url_path));
			criteria.add(Restrictions.eq("patternType", patternType));
			if (signature != null)
				criteria.add(Restrictions.eq("signature", signature));
			Pattern test = (Pattern) criteria.uniqueResult();

			if (test != null)
				throw new LogicException("search pattern has already existed on the database",
						Configurations.CODE_ERROR, "url_host", url_host, "path", url_path, "patternType", patternType,
						"signature", signature);

			Date currentDate = new Date();

			Pattern pattern = new Pattern();
			pattern.setUrlHost(url_host);
			pattern.setUrlPath(url_path);
			pattern.setPatternType(patternType);
			pattern.setWebType(webType);
			pattern.setSignature(signature);
			pattern.setCreatedDateTime(currentDate);
			pattern.setDeletedFlag(false);
			pattern.setExpiredFlag(false);
			pattern.setConfirmedFlag(false);

			session.persist(pattern);
			tx.commit();

			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, "new search pattern is successfully created", "host", pattern.getUrlHost(), "path",
					pattern.getUrlPath(), "patternType", pattern.getPatternType(), "signature", pattern.getSignature(),
					"webType", pattern.getWebType());
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
			tx.rollback();
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
					"0", e.getMessage());
		} finally {
			session.close();
		}

		return resultList;
	}

	public String generateDummyAdsLink(final String keyword, final String domain) {
		String protocol = "https";
		String adsHost = "140.96.29.153";
		int port = 8443;
		String ads_url = String.format("%s://%s:%d/TARSAN/index.jsp?kw=%s&domain=%s", protocol, adsHost, port,
				HttpUtil.getInstance().HTML_Encoding(keyword), domain);
		return ads_url;
	}

	@SuppressWarnings({ "rawtypes" })
	public List<?> getAdsLink(String sessionId, String url, String referrer, String tempKey, boolean logAction, Long parentId, Long rootId) {
		String domain = null;
		try {
			domain = HttpUtil.getInstance().getDomainName(url);
		} catch (MalformedURLException e) {
			if (Configurations.IS_DEBUG) {
				logger.error("MalformedURL - getAdsLink :" + e.getMessage());
			}
		}

		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		// Check on user table if the tempKey matches
		// If match, then proceed, give a user id in return
		// If not match, then abort this function and gives error message
		Criteria criteria_account = session.createCriteria(Users.class);
		criteria_account.add(Restrictions.eq("tempkey", tempKey));
		criteria_account.add(Restrictions.eq("deletedFlag", false));
		Users user = (Users) criteria_account.uniqueResult();

		if (user == null) {
			// Indexing error
			if (Configurations.IS_DEBUG) {
				logger.debug("Configurations.CODE_UNAUTHORIZED");
			}
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_UNAUTHORIZED, methodName,
					null, "invalid key");
			session.close();

			return resultList;
		}

		long userId = user.getUsersId();
		// if (Configurations.IS_DEBUG) {
		// logger.debug("userId: " + userId);
		// }

		Long patternId = -1L; // Default is -1 (not exist)
		String keyword = null;

		try {
			Map<String, Object> extraction = extractKeyword(session, url);
			keyword = (String) extraction.get("keyword");

			if (keyword == null) {
				// Error, keyword is not found
				// Flag pattern as expired?
				throw new LogicException("keyword couldn't be extracted", Configurations.CODE_NOT_EXIST, "url", url,
						"domain", domain);
			}

			Date maxDate = new Date(
					new Date().getTime() - TimeUnit.MINUTES.toMillis(Configurations.BROWSING_TIME_FRAME));
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.add(Restrictions.ge("createdDateTime", maxDate));
			criteria.add(Restrictions.eq("urlHost", domain));
			criteria.add(Restrictions.eq("productSearchFlag", true));
			List list = criteria.list();
			if (!list.isEmpty()) {
				System.out.println("~~~~~~~ List: "+list.size());
				patternId = ((Userevent) list.get(0)).getPattern().getPatternId();
			} else {
				patternId = (Long) extraction.get("pattern_id");
			}

			// Encoding space into html formal
			keyword = keyword.replaceAll(" ", "%20");
			keyword = keyword.replaceAll("\\+", "%20");

			List ads_result = this.logAds(sessionId, userId, patternId, generateDummyAdsLink(keyword, domain), keyword,
					tempKey, session);
			if (ads_result == null)
				throw new LogicException("Couldn't log ads", Configurations.CODE_UNKNOWN_ERR);

			String status = ((Map) ads_result.get(0)).get("status").toString();

			if (!status.equals(Configurations.CODE_OK))
				throw new LogicException("Error in retrieving ads", Configurations.CODE_NOT_EXIST, "keyword", keyword,
						"domain", domain);

			Long ad_id = Long.parseLong(((Map) ads_result.get(2)).get("ad").toString());
			Long ad2_user_id = Long.parseLong(((Map) ads_result.get(2)).get("ad2user").toString());

			criteria = session.createCriteria(Ad.class);
			criteria.add(Restrictions.eq("adId", ad_id));
			Ad ad = (Ad) criteria.uniqueResult();
			keyword = ad.getKeywords();
			// also extract domain from ads

			StringBuilder sb = new StringBuilder();
			sb.append(generateDummyAdsLink(keyword, domain));
			sb.append('&').append("a=").append(ad_id);
			sb.append('&').append("u=").append(ad2_user_id);
			sb.append('&').append("x=").append(tempKey);

			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, "ads url found", "ads_url", sb.toString(), "patternId", patternId);
			if (logAction) {
				// Save user record on the database (Userevent table)
				String ip = null;
				boolean isProductSearch = false;
				BoUser.getInstance().logUserEvent(sessionId, url, referrer, userId, patternId, ip, isProductSearch, parentId, rootId);
			}

			tx.commit();
		} catch (LogicException e) {
			if (Configurations.IS_DEBUG) {
				logger.debug("[FAILURE] methodName: " + methodName);
				logger.debug("[FAILURE] message: " + MessageUtil.getInstance().generateResponseMessage(e.getErrorCode(),
						methodName, null, e.getMessage(), e.getParams()));
			}
			resultList = MessageUtil.getInstance().generateResponseMessage(e.getErrorCode(), methodName, null,
					e.getMessage(), e.getParams());
			tx.rollback();
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<?> generatePatternList(String sessionId) {
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Criteria criteria = session.createCriteria(Pattern.class);
			criteria.add(Restrictions.eq("expiredFlag", false));
			criteria.add(Restrictions.eq("deletedFlag", false));
			criteria.add(Restrictions.eq("webType", 1)); // 1 is e-commerce
			criteria.addOrder(Order.asc("patternId"));
			List<Pattern> list = criteria.list();
			List<String> output = new ArrayList();

			ObjectMapper mapper = new ObjectMapper();
			for (Pattern p : list) {
				String s = mapper.writeValueAsString(p);
				output.add(s);
				// if (Configurations.IS_DEBUG)
				// logger.debug(s);
				// output.add(PojoMapper.toJson(p, false));
			}

			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, "Pattern list generated", "size", list.size(), "pattern", output.toString());
		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
					"0", e.getMessage());
		} finally {
			session.close();
		}

		return resultList;
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> extractKeyword(Session session, String url) throws LogicException {
		HashMap<String, Object> result = new HashMap<String, Object>();
		String keyword = null;
		/* Extract domain name from URL */
		String domain;
		try {
			domain = HttpUtil.getInstance().getDomainName(url);
		} catch (MalformedURLException e) {
			throw new LogicException("MalformedURLException: " + e.getMessage());
		}
		/* Check on the database, if we had that domain */
		Criteria criteria = session.createCriteria(Pattern.class);
		criteria.add(Restrictions.eq("urlHost", domain));
		criteria.add(Restrictions.eq("expiredFlag", false));
		criteria.add(Restrictions.eq("deletedFlag", false));
		List<Pattern> list = criteria.list();
		// No need to further proceed if record is not exist in database
		if (list.isEmpty())
			throw new LogicException("No records of this pattern in the database", Configurations.CODE_NOT_EXIST,
					"domain", domain, "URL", url);
		// HTML Normalization
		url = HttpUtil.getInstance().HTML_Decoding(url);
		// System.out.println(url);

		int idxStart = url.indexOf(domain) + domain.length();
		if (idxStart < 0)
			throw new LogicException("Indexing error", Configurations.CODE_UNKNOWN_ERR, "idxStart", idxStart);
		// url = url.substring(idxStart);

		for (Pattern p : list) {
			if (p.getSignature() != null) {
				// Type 1
				String[] split = url.split(String.format("(\\b%s\\b)", p.getSignature().replace("/", "\\/")));
				if (split.length <= 1)
					continue;
				String[] temp = split[1].split("&");
				// Normalize white spaces
				keyword = temp[0].replace("%20", " ").replace("+", " ");
				// Normalize characters
				keyword = keyword.replace("#", "").replace(";", "").replace(":", "").replace(",", " ").replace(".",
						" ");
			} else {
				// Type 2
				String[] split = url.split(String.format("(\\b%s\\b)", p.getUrlPath()));
				// System.out.println(p.getUrlPath());
				// for(String s: split)
				// System.out.println(s);
				if (split.length <= 1)
					continue;
				url = split[1];
				// Normalize
				String[] separator = { ".", "?", "/", ";" };
				for (String ss : separator) {
					int idxFinish = url.indexOf(ss);
					if (idxFinish > 0) {
						url = url.substring(0, idxFinish);
					}
				}
				// System.out.println(url);
				keyword = url;
			}
			// If keyword found proceed, otherwise error
			Date currentDate = new Date();
			// Set last access on pattern
			p.setLastAccess(currentDate);
			session.persist(p);
			result.put("keyword", keyword);
			result.put("pattern_id", p.getPatternId());
			return result;
		}
		throw new LogicException("search pattern is not exist on the database or is expired",
				Configurations.CODE_NOT_EXIST, "url", url, "domain", domain, "url", url, "patterns", list.toString());
	}
}
