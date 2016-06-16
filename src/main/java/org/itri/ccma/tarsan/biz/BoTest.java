package org.itri.ccma.tarsan.biz;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.itri.ccma.tarsan.biz.exceptions.LogicException;
import org.itri.ccma.tarsan.hibernate.Ad;
import org.itri.ccma.tarsan.hibernate.Barcode;
import org.itri.ccma.tarsan.hibernate.Budgetlog;
import org.itri.ccma.tarsan.hibernate.Budgetpool;
import org.itri.ccma.tarsan.hibernate.Buyad;
import org.itri.ccma.tarsan.hibernate.Pattern;
import org.itri.ccma.tarsan.hibernate.Postad;
import org.itri.ccma.tarsan.hibernate.PostadId;
import org.itri.ccma.tarsan.hibernate.Record;
import org.itri.ccma.tarsan.hibernate.RecordId;
import org.itri.ccma.tarsan.hibernate.ReviewPattern;
import org.itri.ccma.tarsan.hibernate.Userevent;
import org.itri.ccma.tarsan.hibernate.Users;
import org.itri.ccma.tarsan.hibernate.Vacantad;
import org.itri.ccma.tarsan.util.Configurations;
import org.itri.ccma.tarsan.util.HibernateUtil;
import org.itri.ccma.tarsan.util.HttpUtil;
import org.itri.ccma.tarsan.util.MessageUtil;
import org.itri.ccma.tarsan.util.SignatureUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class BoTest.
 */
public class BoTest {

	/** The logger. */
	private static Logger logger = Logger.getLogger("consoleAppender");

	/* Singleton */
	private BoTest() {
		if (Configurations.IS_DEBUG)
			logger.setLevel(Level.ALL);
	}

	public static BoTest getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {

		private static final BoTest INSTANCE = new BoTest();
	}

	/****************************************************************************************/
	/*
	 * Utility function
	 */
	/****************************************************************************************/

	@SuppressWarnings("rawtypes")
	public List<?> randombarcode(){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
//		Transaction tx = session.beginTransaction();
		try {
			List list;
			Random ran = new Random();
			// # list of barcode
			list = session.createCriteria(Barcode.class).list(); // create list
			int i = ran.nextInt((list.size()))+1; // random(size)= 0~size-1
			resultList=MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					String.valueOf(i), "The Random barcode : " + String.valueOf(i));
			return resultList;			
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		}finally{
			session.close();
		}
		
		return resultList;
	}
	
	@SuppressWarnings("rawtypes")
	public List<?> getBarcode(String barcodeId) {
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
//		Transaction tx = session.beginTransaction();

		try {
			Criteria criteria = session.createCriteria(Barcode.class);
			criteria.add(Restrictions.eq("barcodeId", Long.parseLong(barcodeId)));
			Barcode barcode = (Barcode) criteria.uniqueResult();
			String shop = barcode.getShop().toString();
			resultList=MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					"", "This barcode : " + barcodeId + " from " + shop);
			return resultList;

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
	
	@SuppressWarnings("rawtypes")
	public List<?> getUserId(String username) {
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		

		try {
			Criteria criteria = session.createCriteria(Users.class);
			criteria.add(Restrictions.eq("account", username));
			Users users = (Users) criteria.uniqueResult();
			long user = users.getUsersId();
			resultList=MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					"", "User : " + username + " Id is " + user,"UserId",user);
			return resultList;

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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<?> getShopList(String sessionId) {
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Criteria criteria = session.createCriteria(Barcode.class);
			criteria.addOrder(Order.asc("barcodeId"));
			List<Barcode> list = criteria.list();
			List<String> output = new ArrayList();

			ObjectMapper mapper = new ObjectMapper();
			for (Barcode p : list) {
				String s = mapper.writeValueAsString(p);
				output.add(s);
				// if (Configurations.IS_DEBUG)
				// logger.debug(s);
				// output.add(PojoMapper.toJson(p, false));
			}

			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, "Shop list generated", "size", list.size(), "shop", output.toString());
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<?> getKeywordList(String sessionId) {
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Criteria criteria = session.createCriteria(Ad.class);
			criteria.addOrder(Order.asc("adId"));
			List<Ad> list = criteria.list();
			List<String> output = new ArrayList();

			ObjectMapper mapper = new ObjectMapper();
			for (Ad p : list) {
				String s = mapper.writeValueAsString(p);
				output.add(s);
				// if (Configurations.IS_DEBUG)
				// logger.debug(s);
				// output.add(PojoMapper.toJson(p, false));
			}

			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, "Keyword list generated", "size", list.size(), "keywords", output.toString());
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<?> getUrlPatternList(String sessionId) {
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Criteria criteria = session.createCriteria(Pattern.class);
			List<Pattern> list = criteria.list();
			List<String> output = new ArrayList();

			ObjectMapper mapper = new ObjectMapper();
			for (Pattern p : list) {
				String s = mapper.writeValueAsString(p);
//				logger.info(s);
				output.add(s);
				// if (Configurations.IS_DEBUG)
				// logger.debug(s);
				// output.add(PojoMapper.toJson(p, false));
			}

			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, "Url list generated", "size", list.size(), "URL", output.toString());
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<?> getReviewKeywordList(String sessionId) {
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Criteria criteria = session.createCriteria(ReviewPattern.class);
			criteria.addOrder(Order.asc("keywordId"));
			List<ReviewPattern> list = criteria.list();
			List<String> output = new ArrayList();

			ObjectMapper mapper = new ObjectMapper();
			for (ReviewPattern p : list) {
				String s = mapper.writeValueAsString(p);
				output.add(s);
				// if (Configurations.IS_DEBUG)
				// logger.debug(s);
				// output.add(PojoMapper.toJson(p, false));
			}

			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, "ReviewKeyword list generated", "size", list.size(), "keywords", output.toString());
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<?> getUserKeywordList(String sessionId,long userId) {
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.add(Restrictions.eq("users.usersId", userId));
			criteria.addOrder(Order.desc("usereventId"));
//			criteria.setProjection(Projections.property("searchKeyword"));
			List<Userevent> list = criteria.list();
			List<String> output = new ArrayList();
//			logger.info(list);
			int i =0;
			ObjectMapper mapper = new ObjectMapper();
			for (Userevent p : list) {
				String s = mapper.writeValueAsString(p.getSearchKeyword());
				output.add(s);
				i++;
				if(i==20)
					break;
				// if (Configurations.IS_DEBUG)
				// logger.debug(s);
				// output.add(PojoMapper.toJson(p, false));
			}

			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, "Keyword list generated", "size", list.size(), "keywords", output.toString());
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
	public List<?> getLastdata(String sessionId, int num){
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.addOrder(Order.desc("usereventId"));
			criteria.setMaxResults(num);
//			criteria.setProjection(Projections.property("searchKeyword"));
			List<Userevent> list = criteria.list();
			List<String> output = new ArrayList();
			Map<String, Integer> lastdata = new HashMap<String, Integer>();
//			logger.info(list);
			String keytmp="";
			int i =0;
			ObjectMapper mapper = new ObjectMapper();
			for (Userevent p : list) {
				String s = mapper.writeValueAsString(p.getUrlHost());				
				//output.add(s);
				i++;
				if(!lastdata.containsKey(s)){
					lastdata.put(s, 1);
					//logger.info(s+":"+lastdata.get(s));
				}else if(lastdata.containsKey(s)){
					lastdata.put(s, lastdata.get(s)+1);
					//logger.info(s+":"+lastdata.get(s));
				}
				if(i==num){
					int max=0;					
					for(Entry<String, Integer> entry : lastdata.entrySet()){
						Integer value = entry.getValue();
						String key = entry.getKey();
						if(max<value){
							max=value;
							keytmp=key;
						}		
					}
					output.add(keytmp);
					break;
				}					
			}
			Criteria criteria2 = session.createCriteria(Userevent.class);			
			criteria2.addOrder(Order.desc("usereventId"));
			criteria2.setMaxResults(num);
			List<Userevent> list2 = criteria2.list();
			Map<String, Integer> ipdata = new HashMap<String, Integer>();
			ObjectMapper mapper2 = new ObjectMapper();
			i =0;
			for (Userevent p : list) {
				String u = mapper2.writeValueAsString(p.getIp());
				String s = mapper.writeValueAsString(p.getUrlHost());				
				i ++;
				if(s.equals(keytmp)){
					if(!ipdata.containsKey(u)){
						ipdata.put(u, 1);
						logger.info(u+":"+ipdata.get(u));
					}else if(ipdata.containsKey(u)){
						ipdata.put(u, ipdata.get(u)+1);
						logger.info(u+":"+ipdata.get(u));
					}
				}				
			}
			for(Entry<String, Integer> entry : ipdata.entrySet()){
				Integer value = entry.getValue();
				String key = entry.getKey();
				output.add(key);
			}

			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, "Keyword list generated", "size", list.size(), "keywords", output.toString());
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
	
	public List<?> getdatafromDate(String sessionId, String date){
		ArrayList resultList = new ArrayList();
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		

		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(originalFormat.parse(date));
			cal.add(Calendar.DAY_OF_YEAR, 1);
			Date date1 = originalFormat.parse(date.toString());
			Date date2 = cal.getTime();
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.addOrder(Order.desc("usereventId"));
			criteria.add(Restrictions.ge("createdDateTime", date1));
			criteria.add(Restrictions.lt("createdDateTime", date2));
			criteria.setMaxResults(100);
//			criteria.setProjection(Projections.property("searchKeyword"));
			List<Userevent> list = criteria.list();
			List<String> output = new ArrayList();
//			logger.info(list);

			ObjectMapper mapper = new ObjectMapper();
			for (Userevent p : list) {
				String id = mapper.writeValueAsString(p.getUsereventId());
				String ip = mapper.writeValueAsString(p.getIp());
				String ref = mapper.writeValueAsString(p.getUrlReferer());
				String host = mapper.writeValueAsString(p.getUrlHost());
				String time = mapper.writeValueAsString(p.getCreatedDateTime());
				//logger.info(p.getCreatedDateTime());
				output.add(id);
				output.add(ip);
				output.add(ref);
				logger.info(p.getUrlReferer());
				output.add(host);									
			}
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, "Event list generated", "size", list.size(), "list", output.toString());
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
	/**
	 * 
	 * @param sessionId
	 * @param url
	 * @param domain
	 * @param username
	 * @param patternId
	 * @param ip
	 * @param productSearchFlag
	 * @param keyword
	 * @return
	 */
	public List<?> logUserEventICAP_(String sessionId, String url, String referrer, String username, Long patternId,
			String ip, boolean productSearchFlag, Long parentId, Long rootId, String keyword) {
		Long userId = BoUser.getInstance().getUserId(username);
		return logUserEvent_(sessionId, url, referrer, userId, patternId, ip, productSearchFlag, parentId, rootId, keyword);
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
	 * @param keyword
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public List<?> logUserEvent_(String sessionId, String url, String referrer, Long userId, Long patternId, String ip,
			boolean productSearchFlag, Long parentId, Long rootId, String keyword) {
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
			ue.setSearchKeyword(keyword);
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
	
	public String createUser(String username, String password) {

		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		try {

//			Criteria criteria_account = session.createCriteria(Users.class);
//			criteria_account.add(Restrictions.eq("account", username));

//			List result_account = criteria_account.list();

//			if (result_account.size() != 0) {
//				throw new LogicException(String.format(
//						"Account with name [%s] is already registered",
//						username), Configurations.CODE_ERROR);
//			}

			Date currentDate = new Date();
			Users user = new Users();
			user.setAccount(username);
			user.setPassword(password);
			user.setCreatedDateTime(currentDate);
			user.setLastActiveDateTime(currentDate);
			session.save(user);
			tx.commit();
			logger.info("User already been created.");

		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}

		} finally {
			session.close();
		}
		return "DONE!";

	}
	@SuppressWarnings({ "rawtypes" })
	public String DeleteUser(String username, String password) {

		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		try {
			Criteria criteria_account = session.createCriteria(Users.class);
			criteria_account.add(Restrictions.eq("account", username));
			criteria_account.add(Restrictions.eq("password", password));
			List result_account = criteria_account.list();
			Users user = (Users) criteria_account.uniqueResult();
			if (result_account.size() == 0) {
				throw new LogicException(String.format(
						"Database don't have this account : [%s] ", username),
						Configurations.CODE_ERROR);
			}

			session.delete(user);
			tx.commit();

			return "Del DONE!";

		} catch (Exception e) {
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}

		} finally {
			session.close();
		}

		return "DONE";

	}

	public String madehashkey(String password, int i) {
		String algorithm = null;
		if(i==0){
			algorithm = "md5";
		}
		else if(i==1){
			algorithm = "sha512";
		}		
		String hashkey = SignatureUtil.createHash(algorithm, password);
		return hashkey;
	}
	@SuppressWarnings({ "rawtypes" })
	public String check_key(String password){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();

		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			Criteria criteria_account = session.createCriteria(Users.class);
			criteria_account.add(Restrictions.eq("password", password));

			List result_account = criteria_account.list();
			
			if(result_account.size()==0){
				throw new LogicException(String.format(
						"PASSWORD NOT EXIST"), Configurations.CODE_ERROR);
			}else{
				return "PASSWORD EXIST";
			}			
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}			
		}finally{
			session.close();
		}		
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public List<?> recordUseBarcode(String sessionId,String userId, String barcodeId, String coordX, String coordY){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			RecordId recordId = new RecordId();
			recordId.setBarcodeId(Long.parseLong(barcodeId));
			recordId.setUsersId(Long.parseLong(userId));			
			Date currentDate = new Date();
			Record record = new Record();
			record.setId(recordId);
			record.setRecordDateTime(currentDate);
			record.setCoordinateX(coordX);
			record.setCoordinateY(coordY);
			session.save(record);
			tx.commit();
			resultList=MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					sessionId, "User(" + userId + ") use barcode:"+barcodeId+" in x:"+coordX+" y:"+coordY);
			return resultList;
//			return "User("+user_id+") use barcode("+barcode_id+") in x:"+coor_x+" y:"+coor_y;
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
			tx.rollback();
			resultList = MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_EXCEPTION, methodName,
					"0", e.getMessage());
		}finally{
			session.close();

		}		
		return resultList;
		
	}
	@SuppressWarnings("rawtypes")
	public List<?> setBarcodeShop(String shop){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try{			
			List list = session.createCriteria(Barcode.class).list(); // create list
			list.size();
			Barcode barcode = new Barcode();
			barcode.setShop(shop);
			barcode.setBarcodeId(list.size()+1);
			session.save(barcode);
			tx.commit();
			resultList=MessageUtil.getInstance().generateResponseMessage(Configurations.CODE_OK, methodName,
					"", "The shop:"+shop+" saved!");
			return resultList;
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		}finally{
			session.close();
		}		
		return resultList;		
	}
	
	
	@SuppressWarnings("unchecked")
	public List<?>getDataByDays(String sessionId){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.addOrder(Order.desc("createdDateTime"));
			//criteria.setMaxResults(2000);
			List<Userevent> list = criteria.list();
			List<String> output = new ArrayList();
			ObjectMapper mapper = new ObjectMapper();
			Map executionMap = new LinkedHashMap();
			HashMap dateList = new HashMap();
			logger.info(list.size());
			for (Userevent p : list) {
				String day = mapper.writeValueAsString(p.getCreatedDateTime().getDate());
				String month = mapper.writeValueAsString(p.getCreatedDateTime().getMonth());
				//String urlhost = mapper.writeValueAsString(p.getUrlHost());
				
//				output.add(month);
//				output.add(day);
//				output.add(urlhost);
				if(Integer.parseInt(day)<10){
					if(!dateList.containsKey(month+"-0"+day)){
						dateList.put(month+"-0"+day,1);
					}else{
						int value = (Integer)dateList.get(month+"-0"+day);
						dateList.put(month+"-0"+day,value+1);
					}					
				}else{
					if(!dateList.containsKey(month+"-"+day)){
						dateList.put(month+"-"+day,1);
					}else{
						int value = (Integer)dateList.get(month+"-"+day);
						dateList.put(month+"-"+day,value+1);
					}
					
				}
				
			}
			
			SortedSet<String> keys = new TreeSet<String>(dateList.keySet());
			for (String key : keys) { 
			   String value = dateList.get(key).toString();
			   String[] tmp = key.toString().split("-");
				 output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("Month",tmp[0],"Day",tmp[1],"Count",
						 dateList.get(key).toString()));
				 //logger.info(key.toString()+":"+value);

			}
//			for(Object key : dateList.keySet()){
//				 System.out.println(key + " : " + dateList.get(key));
//				 String[] tmp = key.toString().split("-");
//				 output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("Month",tmp[0],"Day",tmp[1],"Count",
//						 dateList.get(key).toString()));
//			}
			executionMap.put("days", output);
			resultList.add(executionMap);
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		}finally{
			session.close();
		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public List<?>getAllHost(String sessionId){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.addOrder(Order.desc("createdDateTime"));
			//criteria.setMaxResults(2000);
			List<Userevent> list = criteria.list();
			List<String> output = new ArrayList();
			ObjectMapper mapper = new ObjectMapper();
			Map executionMap = new LinkedHashMap();
			//HashMap hostList = new HashMap();
			TreeMap hostList = new TreeMap();
			logger.info(list.size());
			for (Userevent p : list) {
				String day = mapper.writeValueAsString(p.getCreatedDateTime().getDate());
				String month = mapper.writeValueAsString(p.getCreatedDateTime().getMonth());
				String urlhost = mapper.writeValueAsString(p.getUrlHost());

				if(!hostList.containsKey(urlhost)){
					hostList.put(urlhost,1);
				}else{
					int value = (Integer)hostList.get(urlhost);
					hostList.put(urlhost,value+1);
				}
			}
			
			Map sortedMap = sortByValues(hostList);
			 
		    // Get a set of the entries on the sorted map
		    Set set = sortedMap.entrySet();
		 
		    // Get an iterator
		    Iterator i = set.iterator();
		 
		    // Display elements
		    while(i.hasNext()) {
		      Map.Entry me = (Map.Entry)i.next();
		      //logger.info(me.getKey() + ": "+me.getValue());
		      output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("Host",me.getKey(),"Count",
		    		  me.getValue().toString()));
		    }
			executionMap.put("hostData", output);
			resultList.add(executionMap);
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		}finally{
			session.close();
		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public List<?>getHostByday(String sessionId){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.addOrder(Order.desc("createdDateTime"));
			//criteria.setMaxResults(2000);
			List<Userevent> list = criteria.list();
			List<String> output = new ArrayList();
			ObjectMapper mapper = new ObjectMapper();
			Map executionMap = new LinkedHashMap();
			HashMap hostList = new HashMap();
			logger.info(list.size());
			for (Userevent p : list) {
				String day = mapper.writeValueAsString(p.getCreatedDateTime().getDate());
				String month = mapper.writeValueAsString(p.getCreatedDateTime().getMonth());
				String urlhost = mapper.writeValueAsString(p.getUrlHost());

				if(!hostList.containsKey(urlhost)){
					hostList.put(urlhost,1);
				}else{
					int value = (Integer)hostList.get(urlhost);
					hostList.put(urlhost,value+1);
				}					
				
			}
			
			SortedSet<String> keys = new TreeSet<String>(hostList.keySet());
			for (String key : keys) { 
			   String value = hostList.get(key).toString();
			   String[] tmp = key.toString().split("-");
				 output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("Host",key,"Count",
						 hostList.get(key).toString()));
				 //logger.info(key.toString()+":"+value);

			}
			executionMap.put("hostData", output);
			resultList.add(executionMap);
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		}finally{
			session.close();
		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public List<?>getAllIp(String sessionId){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.addOrder(Order.desc("createdDateTime"));
			//criteria.setMaxResults(2000);
			List<Userevent> list = criteria.list();
			List<String> output = new ArrayList();
			ObjectMapper mapper = new ObjectMapper();
			Map executionMap = new LinkedHashMap();
			//HashMap hostList = new HashMap();
			TreeMap ipList = new TreeMap();
			logger.info(list.size());
			for (Userevent p : list) {
				String day = mapper.writeValueAsString(p.getCreatedDateTime().getDate());
				String month = mapper.writeValueAsString(p.getCreatedDateTime().getMonth());
				String ip = mapper.writeValueAsString(p.getIp());

				if(!ipList.containsKey(ip)){
					ipList.put(ip,1);
				}else{
					int value = (Integer)ipList.get(ip);
					ipList.put(ip,value+1);
				}
			}
			
			Map sortedMap = sortByValues(ipList);
			 
		    // Get a set of the entries on the sorted map
		    Set set = sortedMap.entrySet();
		 
		    // Get an iterator
		    Iterator i = set.iterator();
		 
		    // Display elements
		    while(i.hasNext()) {
		      Map.Entry me = (Map.Entry)i.next();
		      //logger.info(me.getKey() + ": "+me.getValue());
		      output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("Ip",me.getKey(),"Count",
		    		  me.getValue().toString()));
		    }
			executionMap.put("IpData", output);
			resultList.add(executionMap);
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		}finally{
			session.close();
		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public List<?>getIpCountWithDays(String sessionId, String ip){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.addOrder(Order.desc("createdDateTime"));
			criteria.add(Restrictions.eq("ip", ip));
			//criteria.setMaxResults(2000);
			List<Userevent> list = criteria.list();
			List<String> output = new ArrayList();
			ObjectMapper mapper = new ObjectMapper();
			Map executionMap = new LinkedHashMap();
			HashMap dateList = new HashMap();
			logger.info(list.size());
			for (Userevent p : list) {
				String day = mapper.writeValueAsString(p.getCreatedDateTime().getDate());
				String month = mapper.writeValueAsString(p.getCreatedDateTime().getMonth());
				if(Integer.parseInt(day)<10){
					if(!dateList.containsKey(month+"-0"+day)){
						dateList.put(month+"-0"+day,1);
					}else{
						int value = (Integer)dateList.get(month+"-0"+day);
						dateList.put(month+"-0"+day,value+1);
					}					
				}else{
					if(!dateList.containsKey(month+"-"+day)){
						dateList.put(month+"-"+day,1);
					}else{
						int value = (Integer)dateList.get(month+"-"+day);
						dateList.put(month+"-"+day,value+1);
					}					
				}				
			}			
			SortedSet<String> keys = new TreeSet<String>(dateList.keySet());
			for (String key : keys) { 
			   String value = dateList.get(key).toString();
			   String[] tmp = key.toString().split("-");
				 output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("Month",tmp[0],"Day",tmp[1],"Count",
						 dateList.get(key).toString()));
				 //logger.info(key.toString()+":"+value);
			}
			executionMap.put("IP", output);
			resultList.add(executionMap);
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		}finally{
			session.close();
		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public List<?>getHostCountWithDays(String sessionId, String host){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.addOrder(Order.desc("createdDateTime"));
			criteria.add(Restrictions.eq("urlHost", host));
			//criteria.setMaxResults(2000);
			List<Userevent> list = criteria.list();
			List<String> output = new ArrayList();
			ObjectMapper mapper = new ObjectMapper();
			Map executionMap = new LinkedHashMap();
			HashMap dateList = new HashMap();
			logger.info(list.size());
			for (Userevent p : list) {
				String day = mapper.writeValueAsString(p.getCreatedDateTime().getDate());
				String month = mapper.writeValueAsString(p.getCreatedDateTime().getMonth());
				if(Integer.parseInt(day)<10){
					if(!dateList.containsKey(month+"-0"+day)){
						dateList.put(month+"-0"+day,1);
					}else{
						int value = (Integer)dateList.get(month+"-0"+day);
						dateList.put(month+"-0"+day,value+1);
					}					
				}else{
					if(!dateList.containsKey(month+"-"+day)){
						dateList.put(month+"-"+day,1);
					}else{
						int value = (Integer)dateList.get(month+"-"+day);
						dateList.put(month+"-"+day,value+1);
					}					
				}				
			}			
			SortedSet<String> keys = new TreeSet<String>(dateList.keySet());
			for (String key : keys) { 
			   String value = dateList.get(key).toString();
			   String[] tmp = key.toString().split("-");
				 output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("Month",tmp[0],"Day",tmp[1],"Count",
						 dateList.get(key).toString()));
				 //logger.info(key.toString()+":"+value);
			}
			executionMap.put("HOST", output);
			resultList.add(executionMap);
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		}finally{
			session.close();
		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	public List<?>getIntervalWithHost(String sessionId){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.addOrder(Order.desc("createdDateTime"));
			//criteria.setMaxResults(2000);
			List<Userevent> list = criteria.list();
			List<String> output = new ArrayList();
			ObjectMapper mapper = new ObjectMapper();
			Map executionMap = new LinkedHashMap();
			HashMap StartDateList = new HashMap();
			HashMap EndDateList = new HashMap();
			HashMap hostList = new HashMap();
			logger.info(list.size());
			for (Userevent p : list) {
				String host = mapper.writeValueAsString(p.getUrlHost());
				String day = mapper.writeValueAsString(p.getCreatedDateTime().getDate());
				String month = mapper.writeValueAsString(p.getCreatedDateTime().getMonth());
				//String year = mapper.writeValueAsString(p.getCreatedDateTime().getYear());
				String date="";
				if(Integer.parseInt(day)<10){
					date=month+"-0"+day;
				}else{
					date=month+"-"+day;
				}	
				
				if(!hostList.containsKey(host)){					
					hostList.put(host,date+"@"+date);					
				}else{
					SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
					String[] cmp = hostList.get(host).toString().split("@");
					String[] tmp = cmp[0].split("-");
					int mon=Integer.parseInt(tmp[0]);					
					String[] tmp2 = cmp[1].split("-");
					int mon2=Integer.parseInt(tmp2[0]);
					Date d = formatter.parse(date);
					Date t = formatter.parse(Integer.toString(mon+1)+"-"+tmp[1]);
					Date t2 = formatter.parse(Integer.toString(mon2+1)+"-"+tmp2[1]);
					if(d.before(t)&&d.before(t2)){
						hostList.put(host,date+"@"+cmp[1]);
					}else if(d.after(t2)&&d.after(t)){
						hostList.put(host,cmp[0]+"@"+date);
					}
				}				
			}
			TreeMap orderList = new TreeMap();
			SortedSet<String> keys = new TreeSet<String>(hostList.keySet());
			for (String key : keys) { 
			   String value = hostList.get(key).toString();
			   String[] tmp = value.toString().split("@");
			   SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
			   Date startDate = formatter.parse(tmp[0]);
			   Date endDate = formatter.parse(tmp[1]);
			   long dif = startDate.getTime()-endDate.getTime();
				 //output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("Month",tmp[0],"Day",tmp[1],"Count",
					//	 hostList.get(key).toString()));
			   if(dif!=0){
				   orderList.put(key, dif);
			   }				 
			}
			Map sortedMap = sortByValues(orderList);			 
		    // Get a set of the entries on the sorted map
		    Set set = sortedMap.entrySet();		 
		    // Get an iterator
		    Iterator i = set.iterator();		 
		    // Display elements
		    while(i.hasNext()) {
		      Map.Entry me = (Map.Entry)i.next();
		      //logger.info(me.getKey() + ": "+hostList.get(me.getKey()).toString());
		      String[] tmp = hostList.get(me.getKey()).toString().split("@");
		      String[] sDate= tmp[0].split("-");
		      String[] eDate= tmp[1].split("-");
		      output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("host",me.getKey(),"sMonth",
		    		  sDate[0],"sDay",sDate[1],"eMonth",eDate[0],"eDay",eDate[1]));
		    }
			
			executionMap.put("Interval", output);
			resultList.add(executionMap);
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		}finally{
			session.close();
		}
		return resultList;
	}
	
	public List<?>getHoursData(String sessionId){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.addOrder(Order.asc("createdDateTime"));
//			criteria.add(Restrictions.eq("urlHost", host));
			//criteria.setMaxResults(2000);
			List<Userevent> list = criteria.list();
			List<String> output = new ArrayList();
			ObjectMapper mapper = new ObjectMapper();
			Map executionMap = new LinkedHashMap();

			logger.info(list.size());
			String tmpTime="";
			String hostStr="";
			int count=0;
			for (Userevent p : list) {
				String host = mapper.writeValueAsString(p.getUrlHost());
				String day = mapper.writeValueAsString(p.getCreatedDateTime().getDate());
				String month = mapper.writeValueAsString(p.getCreatedDateTime().getMonth());
				String hour = mapper.writeValueAsString(p.getCreatedDateTime().getHours());
				String nowTime=month+"-"+day+"-"+hour;
				if(tmpTime.equals(nowTime)){
					count=count+1;	
					hostStr = hostStr + ","+ host;
				}else if(!tmpTime.equals("")){
					output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("Month",month,"Day",day,"Hour",hour,"Count",count));
					//logger.info(nowTime+":"+count+":"+hostStr);
					tmpTime=nowTime;
					count=1;
					hostStr = host;
				}else{
					tmpTime=nowTime;
					count=1;
					hostStr = host;
				}
			}			
			executionMap.put("HOURS", output);
			resultList.add(executionMap);
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		}finally{
			session.close();
		}
		return resultList;
	}
	
	public List<?>getRefAndURL(String sessionId){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.addOrder(Order.asc("createdDateTime"));
//			criteria.add(Restrictions.eq("urlHost", host));
			//criteria.setMaxResults(2000);
			List<Userevent> list = criteria.list();
			List<String> output = new ArrayList();
			ObjectMapper mapper = new ObjectMapper();
			Map executionMap = new LinkedHashMap();
			HashMap HostRefList = new HashMap();
			HashMap LinkCount = new HashMap();
			logger.info(list.size());
			String tmpTime="";
			String hostStr="";

			for (Userevent p : list) {
				String host = mapper.writeValueAsString(p.getUrlHost());
				String url = mapper.writeValueAsString(p.getUrlPath());
				String ref = mapper.writeValueAsString(p.getUrlReferer());
				ref=ref.replace("\"", "");
				host=host.replace("\"", "");
				//logger.info(host);
				if(ref.startsWith("http")){
					URL refUrl = new URL(ref);					
					if(!HostRefList.containsKey(refUrl.getHost())){
						HostRefList.put(refUrl.getHost(), host);
					}else{
						String urls = (String)HostRefList.get(refUrl.getHost());						
						if(!urls.contains(host)){
							urls = urls +","+host;
							HostRefList.put(refUrl.getHost(), urls);
						}						
					}
					if((!LinkCount.containsKey(refUrl.getHost()))&&(!LinkCount.containsKey(host))){
						if(!refUrl.getHost().equals(host)){
							LinkCount.put(refUrl.getHost(), 1);
							LinkCount.put(host, 1);
						}else{
							LinkCount.put(host, 2);
						}
					}else if((LinkCount.containsKey(refUrl.getHost()))&&(LinkCount.containsKey(host))){
						int val = (Integer)LinkCount.get(refUrl.getHost());
						LinkCount.put(refUrl.getHost(), val+2);						
					}else if(LinkCount.containsKey(refUrl.getHost())){
						LinkCount.put(host,1);
						int val = (Integer)LinkCount.get(refUrl.getHost());
						LinkCount.put(refUrl.getHost(), val+1);
					}else if(LinkCount.containsKey(host)){
						LinkCount.put(refUrl.getHost(),1);
						int val = (Integer)LinkCount.get(host);
						LinkCount.put(host, val+1);
					}
				}				
			}
//			for (Object key : LinkCount.keySet()) {
//				logger.info(key + " : " + LinkCount.get(key));
//			}
			for (Object key : HostRefList.keySet()) {
	            //logger.info(key + " : " + HostRefList.get(key));
	            String urls = (String)HostRefList.get(key);
	            String[] url = urls.split(",");
	            int countURL = url.length;
	            if(urls.contains(key.toString())){
	            	countURL = countURL-1;
	            }
	            if(countURL!=0){
	            	output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("REF",key,"sub",countURL,"count",LinkCount.get(key)));
	            	for(String s : url){	
		            	if(!s.equals(key.toString())){
		            		output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("url",s,"ref",key,"UrlCount",LinkCount.get(s),"RefCount",LinkCount.get(key)));
		            	}
		            }
	            }	            
	            
	        }

			executionMap.put("RefUrl", output);
			resultList.add(executionMap);
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		}finally{
			session.close();
		}
		return resultList;
	}
	
	public List<?>getDailyDataByDate(String sessionId, String date){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(originalFormat.parse(date));
			cal.add(Calendar.DAY_OF_YEAR, 1);
			Date date1 = originalFormat.parse(date.toString());
			Date date2 = cal.getTime();
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.addOrder(Order.desc("usereventId"));
			criteria.add(Restrictions.ge("createdDateTime", date1));
			criteria.add(Restrictions.lt("createdDateTime", date2));
			List<Userevent> list = criteria.list();
			List<String> output = new ArrayList();
			Map executionMap = new LinkedHashMap();
			ObjectMapper mapper = new ObjectMapper();
			TreeMap orderList = new TreeMap();
			for (Userevent p : list) {
				String host = mapper.writeValueAsString(p.getUrlHost());
				host = host.replace("\"", "");
				if(!orderList.containsKey(host)){
					orderList.put(host, 1);
				}else{
					int value = (Integer)orderList.get(host);
					orderList.put(host, value+1);
				}
			}
			Map sortedMap = sortByValues(orderList);

		    // Get a set of the entries on the sorted map
		    Set set = sortedMap.entrySet();		 
		    // Get an iterator
		    Iterator i = set.iterator();		 
		    // Display elements
		    while(i.hasNext()) {
		      Map.Entry me = (Map.Entry)i.next();
		      //logger.info(me.getKey() + ": "+me.getValue());
		      output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("host",me.getKey(),"Count",
		    		  me.getValue().toString()));
		    }
			executionMap.put("DailyData", output);
			resultList.add(executionMap);
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		}finally{
			session.close();
		}
		return resultList;
	}
	
	public List<?>getDailyHoursDataByHost(String sessionId, String host){
		String methodName = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		ArrayList resultList = new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.addOrder(Order.asc("createdDateTime"));
			criteria.add(Restrictions.eq("urlHost", host));
			//criteria.setMaxResults(2000);
			List<Userevent> list = criteria.list();
			List<String> output = new ArrayList();			
			HashMap hourCount = new HashMap();
			for(int h=0;h<24;h++){
				if(h<10){
					hourCount.put("0"+Integer.toString(h), 0);
				}else{
					hourCount.put(Integer.toString(h), 0);
				}
			}
			ObjectMapper mapper = new ObjectMapper();
			Map executionMap = new LinkedHashMap();
			logger.info(list.size());
			for (Userevent p : list) {
				String hour = mapper.writeValueAsString(p.getCreatedDateTime().getHours());
				if(Integer.parseInt(hour)<10)hour="0"+hour;
				
				if(hourCount.containsKey(hour)){
					int count=(Integer)hourCount.get(hour);
					hourCount.put(hour, count+1);
				}else{
					hourCount.put(hour, 1);
				}
			}
			SortedSet<String> keys = new TreeSet<String>(hourCount.keySet());
			String hours="";
			for (String key : keys){
				//logger.info(key+":"+hourCount.get(key).toString());				
				hours=hours+","+hourCount.get(key).toString();
			}
			hours=hours.substring(1);
			//logger.info(hours);
			output.addAll(MessageUtil.getInstance().NewGenerateResponseMessage("hours",hours));
			executionMap.put("WebHours", output);
			resultList.add(executionMap);
		}catch(Exception e){
			if (Configurations.IS_DEBUG) {
				logger.error("[ERROR] methodName: " + methodName);
				logger.error("[ERROR] message: " + e.getMessage(), e);
			}
		}finally{
			session.close();
		}
		return resultList;		
	}
	
	
	//Method for sorting the TreeMap based on values
	  public static <K, V extends Comparable<V>> Map<K, V>sortByValues(final Map<K, V> map) {
	    Comparator<K> valueComparator = 
	             new Comparator<K>() {
	      public int compare(K k1, K k2) {
	        int compare = 
	              map.get(k1).compareTo(map.get(k2));
	        if (compare == 0) 
	          return 1;
	        else 
	          return compare;
	      }
	    };
	 
	    Map<K, V> sortedByValues = 
	      new TreeMap<K, V>(valueComparator);
	    sortedByValues.putAll(map);
	    return sortedByValues;
	  }
	  
//	public List<?> mappingUrlTitle (String url, String title){
//		ArrayList resultList = new ArrayList();
//		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
//		Session session = HibernateUtil.getSessionFactory().openSession();
//		try{
//			Criteria criteria = session.createCriteria(Barcode.class);
//			criteria.add(Restrictions.eq("barcodeId", Long.parseLong(barcodeId)));
//			Barcode barcode = (Barcode) criteria.uniqueResult();
//			String shop = barcode.getShop().toString();
//			return resultList;
//		}catch(Exception e){
//			if (Configurations.IS_DEBUG) {
//				logger.error("[ERROR] methodName: " + methodName);
//				logger.error("[ERROR] message: " + e.getMessage(), e);
//			}
//		}finally{
//			session.close();
//		}
//		
//	}
	
	  
	  public ArrayList createPostad(String sessionId) {
		  	ArrayList resultList = new ArrayList();
			String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();		
			Session session = HibernateUtil.getSessionFactory().openSession();
			Date currentDate = new Date();
			Transaction tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Buyad.class);
			Buyad bAd = new Buyad();
			 
			try {
				Session session2 = HibernateUtil.getSessionFactory().openSession();
				Criteria criteria2 = session2.createCriteria(Vacantad.class);
				long i = 5;
				criteria2.add(Restrictions.eq("buyadId", i));
				Buyad buyad = (Buyad)criteria2.uniqueResult();

				bAd.setContent("1321321313");
				session.save(bAd);		
				tx.commit();
				logger.info("postAd");
				logger.info(currentDate);
				
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
	

}
