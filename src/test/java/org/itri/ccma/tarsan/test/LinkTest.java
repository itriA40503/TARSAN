package org.itri.ccma.tarsan.test;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.itri.ccma.tarsan.hibernate.Userevent;
import org.itri.ccma.tarsan.hibernate.Users;
import org.itri.ccma.tarsan.util.Configurations;
import org.itri.ccma.tarsan.util.HibernateUtil;
import org.itri.ccma.tarsan.util.HttpUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Testing all links in the API server by using HTTP GET Method
 * Unused
 * @deprecated
 * @author 532711
 *
 */
public class LinkTest {

	String SERVER = "140.96.29.240"; // 140.96.29.153 // 140.96.29.240
	ObjectMapper mapper;
	JsonFactory factory;
	private Logger logger;

	String loginFormat = "http://%s:8080/TARSAN/service/main?service=login&jsonPara=[(\"account\":\"%s\"),(\"password\":\"%s\")]";
	String createUserFormat = "http://%s:8080/TARSAN/service/main?service=createUser&jsonPara=[(\"account\":\"%s\"),(\"password\":\"%s\")]";
	String logUserEventFormat = "http://%s:8080/TARSAN/service/main?service=logUserEvent&jsonPara=[(\"url\":\"%s\"),(\"referrer\":\"%s\"),(\"userId\":%d),(\"patternId\":%d),(\"ip\":\"%s\"),(\"productSearchFlag\":%b)]";
	String logUserEventICAPFormat = "http://%s:8080/TARSAN/service/main?service=logUserEventICAP&jsonPara=[(\"url\":\"%s\"),(\"referrer\":\"%s\"),(\"username\":\"%s\"),(\"patternId\":%d),(\"ip\":\"%s\"),(\"productSearchFlag\":%b)]";

	@Before
	public void setUp() throws Exception {
		mapper = new ObjectMapper();
		factory = mapper.getFactory();
		logger = Logger.getLogger(LinkTest.class);
		logger.setLevel(Level.ALL);
	}

	@After
	public void tearDown() throws Exception {
	}

	String[] symbol_works = { "&&&&&&&", "@@@@@@@@", "!!!!!!!", "^^^^^^^", "//////////", "*******", "~~~~~~~~~",
			"\\\\\\\\\\\\\\\\\\", "((((((((", ")))))))))", "[[[[[[[[[", "]]]]]]]]]", "-------------", "_____________",
			"++++++++++++++++", "===============", "::::::::::::", ";;;;;;;;;;;;;", "''''''''''''", "<<<<<<<<<<<<<<<<",
			">>>>>>>>>>>>>>", ",,,,,,,,,,,,,,", ".................", "??????????", "`````````````", "||||||||||||||" };
	String[] symbol_err = { "{{{{{{{", "}}}}}}}", "%%%%%%%", "\"\"\"\"\"\"\"" };

	// @Test
	public void testLoginUser() {
		String status;

		String EXIST_USER = "lalala";
		String EXIST_PASS = "lalala";
		String NORMAL_INP = "normal_input";
		// For trial purpose
		String[] u_trial = { "ab&ab", "ab#ab", "ab%ab", "ab@ab", "ab!ab", "ab^ab", "ab/ab", "ab({[ab", "ab)}]ab",
				"\\//**~abcde" };

		String user, pass;
		String url;
		/* --------------------- Test on login ------------------- */
		/* Test for success login */
		user = EXIST_USER;
		pass = EXIST_PASS;
		url = String.format(loginFormat, SERVER, user, pass);
		status = getStatus(url);
		logger.info("## Test for success login");
		logger.info(generateLog("status", status, "username", user, "password", pass));
		assertEquals(Configurations.CODE_OK, status);

		/* Test for failed login because of wrong username */
		user = NORMAL_INP;
		pass = EXIST_PASS;
		url = String.format(loginFormat, SERVER, user, pass);
		status = getStatus(url);
		logger.info("## Test for failed login because of wrong username");
		logger.info(generateLog("status", status, "username", user, "password", pass));
		assertEquals(Configurations.CODE_NOT_EXIST, status);

		/* Test for failed login because of wrong password */
		user = EXIST_USER;
		pass = NORMAL_INP;
		url = String.format(loginFormat, SERVER, user, pass);
		status = getStatus(url);
		logger.info("## Test for failed login because of wrong password");
		logger.info(generateLog("status", status, "username", user, "password", pass));
		assertEquals(Configurations.CODE_FORBIDDEN, status);

		/*
		 * Test for failed login because user is not exist (using symbol)
		 */
		for (String s : u_trial) {
			user = HttpUtil.getInstance().HTML_Encoding(s);
			pass = EXIST_PASS;
			url = String.format(loginFormat, SERVER, user, EXIST_PASS);
			status = getStatus(url);
			logger.info("## Test for failed login because user is not exist");
			logger.info(generateLog("status", status, "username", user, "password", pass));
			assertEquals(Configurations.CODE_NOT_EXIST, status);
		}

		/*
		 * Test for symbols
		 */

		for (String s : symbol_works) {
			user = EXIST_USER;
			pass = s;
			url = String.format(loginFormat, SERVER, user, pass);
			status = getStatus(url);
			logger.info("## Symbol works (Non HTML Normalized)");
			logger.info(generateLog("status", status, "username", user, "password", pass));
			assertEquals(Configurations.CODE_FORBIDDEN, status);
		}

		for (String s : symbol_err) {
			user = EXIST_USER;
			pass = s;
			url = String.format(loginFormat, SERVER, user, pass);
			status = getStatus(url);
			logger.info("## Symbol error (Non HTML Normalized) --> Raise a null status (couldn't execute API)");
			logger.info(generateLog("status", status, "username", user, "password", pass));
			assertEquals(null, status);
		}
	}

	// @Test
	public void testCreateUser() {
		String[] password_ok = { "@@@@@@@@", "!!!!!!!", "~~~~~~~~~", "_____________", ".................", "??????????",
				"1234567890", "qwertyuiop1234567890" };
		String[] password_err = { "&&&&&&&", "^^^^^^^", "//////////", "*******", "\\\\\\\\\\\\\\\\\\", "((((((((",
				")))))))))", "[[[[[[[[[", "]]]]]]]]]", "-------------", "++++++++++++++++", "===============",
				"::::::::::::", ";;;;;;;;;;;;;", "''''''''''''", "<<<<<<<<<<<<<<<<", ">>>>>>>>>>>>>>", ",,,,,,,,,,,,,,",
				"`````````````", "||||||||||||||", "{{{{{{{", "}}}}}}}", "\"\"\"\"\"\"\"" };
		String status;
		int counter = 0;
		String test_username = "test_user_%d";
		String url;
		String user;

		// Password error because of some symbols
		for (String s : password_err) {
			user = String.format(test_username, counter);
			url = String.format(createUserFormat, SERVER, user, s);
			status = getStatus(url);
			logger.info("## Password error because of some symbols");
			logger.info(generateLog("status", status, "username", user, "password", s));
			logger.debug("## URL: " + url);
			assertEquals(Configurations.CODE_FORBIDDEN, status);
		}

		// Password ok, even thought it uses symbols
		for (String s : password_ok) {
			user = String.format(test_username, counter++);
			url = String.format(createUserFormat, SERVER, user, s);
			status = getStatus(url);
			logger.info("## Password ok, even thought it uses symbols");
			logger.info(generateLog("status", status, "username", user, "password", s));
			assertEquals(Configurations.CODE_OK, status);
		}

		// delete the test user
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		Criteria criteria;
		Users testUser;

		for (int i = 0; i < counter; i++) {
			criteria = session.createCriteria(Users.class);
			criteria.add(Restrictions.eq("account", String.format(test_username, i)));
			testUser = (Users) criteria.uniqueResult();
			session.delete(testUser);
		}
		logger.info("##Deleted test users:" + counter);
		tx.commit();
		session.close();
	}

//	@Test
	public void testLogUserEvent() {
		Long userId = 1L;
		Long patternId = 1L;
		String status;
		String referrer = "localhost";
		String call_url;
		String ip = "testing_ip";
		boolean search = true;
		String[] urls = { "http://buy.mi.com/tw/search_mi%20note%20pro",
				"http://www.amazon.com/s/ref=nb_sb_noss_2?url=search-alias%3Daps&field-keywords=mi+note",
				"http://search.ruten.com.tw/search/s000.php?searchfrom=indexbar&k=mi+note+pro&t=0",
				"http://www.amazon.com/Smartphone-android-Snapdragon-1920x1080-Beenle%C2%AE/dp/B00XM65SSS/ref=sr_1_1?ie=UTF8&qid=1438738909&sr=8-1&keywords=mi+note",
				"http://www.amazon.com/s/ref=nb_sb_noss_2?url=search-alias%3Daps&field-keywords=mi+note+pro",
				"http://www.amazon.com/DAYJOY-Fullbody-Protection-Aluminum-protector/dp/B00YDV59JE/ref=sr_1_1?ie=UTF8&qid=1438738977&sr=8-1&keywords=mi+note+pro",
				"http://global.rakuten.com/en/search/?tl=&k=mi+note+pro" };
		for (int i = 0; i < urls.length; i++) {
			String url = HttpUtil.getInstance().HTML_Encoding(urls[i]);
			call_url = String.format(logUserEventFormat, SERVER, url, referrer, userId, patternId, ip, search);
			status = getStatus(call_url);
			logger.info("## Call url: " + call_url);
			logger.info("## Test logging user event (normal API)");
			logger.info(generateLog("status", status, "url", urls[i]));
			assertEquals(Configurations.CODE_OK, status);
		}

		// delete all user events which were created in the middle of testing
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.add(Restrictions.eq("ip", ip));
			@SuppressWarnings("unchecked")
			List<Userevent> list_delete = criteria.list();
			for (Userevent ue : list_delete) {
				session.delete(ue);
			}
			tx.commit();
			logger.info("## Delete all user events on the testing: " + list_delete.size() + " rows");
		} catch (Exception e) {
			tx.rollback();
		} finally {
			session.close();
		}
	}

//	@Test
	public void testLogUserEventICAP() {
		String username = "lalala";
		Long patternId = 1L;
		String status;
		String referrer = "localhost";
		String call_url;
		String ip = "testing_ip";
		boolean search = true;
		String[] urls = { "http://buy.mi.com/tw/search_mi%20note%20pro",
				"http://www.amazon.com/s/ref=nb_sb_noss_2?url=search-alias%3Daps&field-keywords=mi+note",
				"http://search.ruten.com.tw/search/s000.php?searchfrom=indexbar&k=mi+note+pro&t=0",
				"http://www.amazon.com/Smartphone-android-Snapdragon-1920x1080-Beenle%C2%AE/dp/B00XM65SSS/ref=sr_1_1?ie=UTF8&qid=1438738909&sr=8-1&keywords=mi+note",
				"http://www.amazon.com/s/ref=nb_sb_noss_2?url=search-alias%3Daps&field-keywords=mi+note+pro",
				"http://www.amazon.com/DAYJOY-Fullbody-Protection-Aluminum-protector/dp/B00YDV59JE/ref=sr_1_1?ie=UTF8&qid=1438738977&sr=8-1&keywords=mi+note+pro",
				"http://global.rakuten.com/en/search/?tl=&k=mi+note+pro" };
		for (int i = 0; i < urls.length; i++) {
			String url = HttpUtil.getInstance().HTML_Encoding(urls[i]);
			call_url = String.format(logUserEventICAPFormat, SERVER, url, referrer, username, patternId, ip, search);
			status = getStatus(call_url);
			logger.info("## Call url: " + call_url);
			logger.info("## Test logging user event (normal API)");
			logger.info(generateLog("status", status, "url", urls[i]));
			assertEquals(Configurations.CODE_OK, status);
		}

		// delete all user events which were created in the middle of testing
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.add(Restrictions.eq("ip", ip));
			@SuppressWarnings("unchecked")
			List<Userevent> list_delete = criteria.list();
			for (Userevent ue : list_delete) {
				session.delete(ue);
			}
			tx.commit();
			logger.info("## Delete all user events on the testing: " + list_delete.size() + " rows");
		} catch (Exception e) {
			tx.rollback();
		} finally {
			session.close();
		}
	}

	/*- ************ Utility function ************ -*/
	private String getStatus(String url) {
		try {
			String json = HttpUtil.getInstance().sendGet(url);
			JsonParser jp = factory.createParser(json);
			JsonNode actualObj = mapper.readTree(jp);
			Iterator<JsonNode> iter = actualObj.elements();
			int idx = 0;
			while (iter.hasNext()) {
				JsonNode temp = iter.next();
				if (idx == 0) {
					// Check status
					String status = temp.get("status").asText();
					return status;
				}
				// System.out.println(iter.next());
				idx++;
			}
		} catch (Exception e) {
		}
		return null;
	}

	private String generateLog(String... params) {
		StringBuilder sb = new StringBuilder();
		if (params.length > 0)
			sb.append(params[0]);
		for (int i = 1; i < params.length; i++) {
			if (i % 2 == 1)
				sb.append(" : ");
			sb.append(params[i]);
			if (i % 2 == 1)
				sb.append("\n");
		}
		return sb.toString();
	}
}
