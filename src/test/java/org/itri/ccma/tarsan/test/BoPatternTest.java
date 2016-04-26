package org.itri.ccma.tarsan.test;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.itri.ccma.tarsan.biz.BoPattern;
import org.itri.ccma.tarsan.hibernate.Pattern;
import org.itri.ccma.tarsan.hibernate.Userevent;
import org.itri.ccma.tarsan.hibernate.Users;
import org.itri.ccma.tarsan.util.Configurations;
import org.itri.ccma.tarsan.util.HibernateUtil;

import junit.framework.TestCase;

public class BoPatternTest extends TestCase {
	private Logger logger = Logger.getLogger(BoPatternTest.class);

	public BoPatternTest() {
		super();
		logger.setLevel(Level.ALL);
	}

	protected static void setUpBeforeClass() throws Exception {
	}

	protected static void tearDownAfterClass() throws Exception {
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@SuppressWarnings("rawtypes")
	public void testCreateSearchPattern() {
		String sessionId = "1testCreateSearchPattern1";
		BoPattern bo = BoPattern.getInstance();

		String[] hosts = { "www.test_lalala.com", "www.amazon.com", "www.officedepot.com", "www.testing.com:8088" };
		String[] paths = { "/lalala_test/", "/s/ref=nb_sb_noss_1232?",
				"/catalog/search.do;jsessionid=0123120000eJZvDfEByJhJ37CNlRdCim:17h4h7ado?",
				"][}{/\\=-+_!@#$%^&*)(;':\"><.,??/~`" };
		String[] signatures = { "test__lalala=", "field-keywords=", "Ntt=", "][}{/\\=-+_!@#$%^&*)(;':\"><.,??/~`" };
		int test_type = -1;
		int web_type = 1;

		// successful creation
		List success = bo.createSearchPattern(sessionId, hosts[0], paths[0], test_type, signatures[0], web_type);
		logger.info("##list:" + success);
		assertEquals(Configurations.CODE_OK, ((Map) success.get(0)).get("status").toString());

		// failed creation -- duplicate
		List failed = bo.createSearchPattern(sessionId, hosts[0], paths[0], test_type, signatures[0], web_type);
		logger.info("##list:" + failed);
		assertEquals(Configurations.CODE_ERROR, ((Map) failed.get(0)).get("status").toString());

		// Various test
		List attempt;
		for (int i = 1; i < hosts.length; i++) {
			attempt = bo.createSearchPattern(sessionId, hosts[i], paths[i], test_type, signatures[i], web_type);
			logger.info("##list:" + attempt);
			assertEquals(Configurations.CODE_OK, ((Map) attempt.get(0)).get("status").toString());
		}

		// delete the test user
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		for (int i = 0; i < hosts.length; i++) {
			Criteria criteria = session.createCriteria(Pattern.class);
			criteria.add(Restrictions.eq("urlHost", hosts[i]));
			criteria.add(Restrictions.eq("urlPath", paths[i]));
			criteria.add(Restrictions.eq("patternType", test_type));
			criteria.add(Restrictions.eq("signature", signatures[i]));
			Pattern test = (Pattern) criteria.uniqueResult();
			if (test != null)
				session.delete(test);
		}

		try {
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		}
		session.close();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void testGetAdsLink() {
		String sessionId = "1testGetAdsLink1";
		BoPattern bo = BoPattern.getInstance();

		// Search for one random user to test the ads recording
		Users user = null;
		String tempKey = null;

		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria criteria_account = session.createCriteria(Users.class);
		criteria_account.add(Restrictions.isNotNull("tempkey"));
		criteria_account.add(Restrictions.eq("deletedFlag", false));

		List<Users> list = (List<Users>) criteria_account.list();
		// Close the session
		session.close();
		// Error on testing, abort testing
		if (list.isEmpty()) {
			return;
		}
		// Get the information needed
		user = list.get(0);
		tempKey = user.getTempkey();

		String url;
		String referrer = "www.lalala.com";
		List result;
		// Success - (type 1)
		url = "http://www.apple.com/search/?q=iphone&x=0&y=0";
		result = bo.getAdsLink(sessionId, url, referrer, tempKey, false, null, null);
		logger.info("##list:" + result);
		assertEquals(Configurations.CODE_OK, ((Map) result.get(0)).get("status").toString());

		// Success - (type 3)
		url = "http://www.rakuten.com/sr/searchresults.aspx?qu=nokia";
		result = bo.getAdsLink(sessionId, url, referrer, tempKey, false, null, null);
		logger.info("##list:" + result);
		assertEquals(Configurations.CODE_OK, ((Map) result.get(0)).get("status").toString());

		// Failed - Refer to normal website
		// (not in the search mode or query isn't match)
		url = "http://www.apple.com/macbook-air/specs.html";
		result = bo.getAdsLink(sessionId, url, referrer, tempKey, false, null, null);
		logger.info("##list:" + result);
		assertEquals(Configurations.CODE_NOT_EXIST, ((Map) result.get(0)).get("status").toString());

		// Failed - Refer to a website which is not in the database
		url = "https://www.google.com.tw/?gfe_rd=cr&ei=dFmoVbjFMYqxmQXU-ZPQDQ&gws_rd=ssl#q=apple";
		result = bo.getAdsLink(sessionId, url, referrer, tempKey, false, null, null);
		logger.info("##list:" + result);
		assertEquals(Configurations.CODE_NOT_EXIST, ((Map) result.get(0)).get("status").toString());

		// Failed - Refer to a website whose query pattern is different
		url = "http://www.apple.com/search/?section=global&geo=us&qq=iphone%206";
		result = bo.getAdsLink(sessionId, url, referrer, tempKey, false, null, null);
		logger.info("##list:" + result);
		assertEquals(Configurations.CODE_NOT_EXIST, ((Map) result.get(0)).get("status").toString());

		// OK - (type 2)
		url = "http://www.crutchfield.com/shopsearch/dock.html";
		result = bo.getAdsLink(sessionId, url, referrer, tempKey, false, null, null);
		logger.info("##list:" + result);
		assertEquals(Configurations.CODE_OK, ((Map) result.get(0)).get("status").toString());

		// Failed - Refer to invalid tempKey
		url = "http://www.apple.com/search/%3Fsection=global%26geo=us%26q=iphone%206";
		String false_tempkey = "false_lalala";
		result = bo.getAdsLink(sessionId, url, referrer, false_tempkey, false, null, null);
		logger.info("##list:" + result);
		assertEquals(Configurations.CODE_UNAUTHORIZED, ((Map) result.get(0)).get("status").toString());

		// Check on various cases
		// Symbol in searching keyword
		referrer = null;
		url = "http://www.apple.com/search/?q=www%2Fgoogle%2F%5C-)(*%7D%7B%5D%5B!%40%23%24%25%5E%26*%3B%27%22%3A%3E%3C.%2C%3F-_%2B%3D&x=0&y=0";
		result = bo.getAdsLink(sessionId, url, referrer, tempKey, false, null, null);
		logger.info("##list:" + result);
		assertEquals(Configurations.CODE_OK, ((Map) result.get(0)).get("status").toString());
		// long url
		url = "http://www.apple.com/search/?q=www%2Fgoogle%2F%5C-)(*%7D%7B%5D%5B!%40%23%24%25%5E%26*%3B%27%22%3A%3E%3C.%2C%3F-_%2B%3D%20www.google.com%20www.facebook.com%20www.apple.com%20www.youtube.com%20mi%20mi%20note%20note%20samsung%20apple%20android%20www.google.com%20www.facebook.com%20www.apple.com%20www.youtube.com%20mi%20mi%20note%20note%20samsung%20apple%20android%20www.google.com%20www.facebook.com%20www.apple.com%20www.youtube.com%20mi%20mi%20note%20note%20samsung%20apple%20android%20www.google.com%20www.facebook.com%20www.apple.com%20www.youtube.com%20mi%20mi%20note%20note%20samsung%20apple%20android%20www.google.com%20www.facebook.com%20www.apple.com%20www.youtube.com%20mi%20mi%20note%20note%20samsung%20apple%20android%20www.google.com%20www.facebook.com%20www.apple.com%20www.youtube.com%20mi%20mi%20note%20note%20samsung%20apple%20android%20www.google.com%20www.facebook.com%20www.apple.com%20www.youtube.com%20mi%20mi%20note%20note%20samsung%20apple%20android";
		result = bo.getAdsLink(sessionId, url, referrer, tempKey, false, null, null);
		logger.info("##list:" + result);
		assertEquals(Configurations.CODE_OK, ((Map) result.get(0)).get("status").toString());

		// delete all user events which were created in the middle of testing
		session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.add(Restrictions.eq("sessionId", sessionId));
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testOnVariousSites() {
		String sessionId = "testOnVariousSites12345";
		BoPattern bo = BoPattern.getInstance();

		// Search for one random user to test the ads recording
		Users user = null;
		String tempKey = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria criteria_account = session.createCriteria(Users.class);
		criteria_account.add(Restrictions.isNotNull("tempkey"));
		criteria_account.add(Restrictions.eq("deletedFlag", false));

		List<Users> list = (List<Users>) criteria_account.list();
		// Error on testing, abort testing
		if (list.isEmpty()) {
			return;
		}
		// Get the information needed
		user = list.get(0);
		tempKey = user.getTempkey();

		List result;
		// Test on various websites with uncommon input
		String[] urls = {
				// "http://www.walmart.com/search/?query=iphone%206%207%208%209%2010%20note%20pro%20!%40%23%24%25%5E%26*)(~%60_%2B-%2B%3D%7C%5C%2F%3E%3C.%2C%2F%3F%3A%3B%27%22%5D%5B%7D%7B%20asdf%20adfd%20fas",
				// "http://search-en.lego.com/?q=iphone+6+7+8+9+10+note+pro+!%40%23%24%25%5e%26*)(~%60_+-+%3d%7c%5c%2f%3E%3C.%2c%2f%3f%3a%3b%27%22%5d%5b%7d%7b+asdf+adfd+fas&cc=US",
				// "http://shopap.lenovo.com/SEUILibrary/controller/e/twweb/LenovoPortal/zh_TW/site.workflow:SimpleSiteSearch?q=iphone+6+7+8+9+10+note+pro+%21%40%23%24%25%5E%26*%29%28~%60_%2B-%2B%3D%7C%5C%2F%3E%3C.%2C%2F%3F%3A%3B%27%22%5D%5B%7D%7B+asdf+adfd+fas&cc=tw&lang=zh",
				// "https://www.lowes.com/webapp/wcs/stores/servlet/SearchCatalog?catalogId=10051&catalogId=10051&langId=-1&langId=-1&storeId=10151&krypto=bzkdjVg1OuuhzzjWPQ7rdo0FscUFVdgA1g3imqgc3DJ%2F17OVQpF4SO3sk%2BwKJbFCR9Sp9dppsNsltLqu6Lflh0W1mD39GDI9bTCoNQpr9WZeR8EH3%2FqhEUfb2TS6b4Y87aX08inSB6sgO4FpSFLv8P%2B2axKLjrquhcZYWc0iJEq4BaG4UHzFIGfQJkEQeV9OVNbYtIbksSTFWYq3AEKJCS9rAPxw2PU3VLcBsKnnx8PdJh7qWhqp76nyKhydQmpXAnGPTtW5zEmTtSxrbLw4m%2FVCfe30gAggfAmKu24w4IcsasMgdF4rt1TS1jnystUW84fk5%2FD0EvtJkCC6AHpSKflo3B42inqfoA33Lu3RLIcGNx3qVUekJEHMspoqG3NDU2ZxxBQKAG%2FTz%2FI70jTQNMTxOrN8G18%2B4DUKfb25w0vn49XZOtYTNRdsXniuv3J1bUgL5W7UQVaNmQp0r%2FaBlxzCatub4dv%2BtMDwqSAee3y6WvlwKuQL84%2BJ%2FjVn56NmX92oLnf%2BftUrvAnXtjnzLSRV6oQCZawUhT2T2uIWPu8%3D#$^&)(~`_+-+=|\\/><.,/?:;'\"][}{+asdf+adfd+fas",
				// "http://www1.macys.com/shop/search?keyword=iphone+6+7+8+9+10+note+pro+%21%40%23%24%25%5E%26*%29%28~%60_%2B-%2B%3D%7C%5C%2F%3E%3C.%2C%2F%3F%3A%3B%27%22%5D%5B%7D%7B+asdf+adfd",
				// "http://www.target.com/s?searchTerm=iphone+6+7+8+9+10+note+pro+%21%40%23%24%25%5E%26*%29%28~%60_+-+%3D%7C%5C%2F%3E%3C.&category=0%7CAll%7Cmatchallpartial%7Call+categories&lnk=snav_sbox_iphone+6+7+8+9+10+note+pro+%21%40%23%24%25%5E%26*%29%28~%60_+-+%3D%7C%5C%2F%3E%3C.",
				// "http://www.sunglasshut.com/webapp/wcs/stores/servlet/ProhibitedCharacterErrorView?searchType=102&searchSource=Q&searchingFrom=global&orderBy=default&showResultsPage=true&langId=-1&beginIndex=0&sType=SimpleSearch&pageSize=18&resultCatEntryType=2&searchTerm=iphone+6+7+8+9+10+note+pro+%21%40%23%24%25%5E%26*%29%28~%60_+-+%3D%7C%5C%2F%3E%3C.%2C%2F%3F%3A%3B%27%22%5D%5B%7D%7B+asd&catalogId=10101&pageView=image&storeId=10152",
				// "http://www.sunglasshut.com/SearchDisplay?storeId=10152&catalogId=10101&langId=-1&pageSize=18&beginIndex=0&searchSource=Q&sType=SimpleSearch&resultCatEntryType=2&searchType=102&searchingFrom=global&showResultsPage=true&pageView=image&orderBy=default&searchTerm=iphon+e6",
				// "http://www.target.com/s?searchTerm=iphonr&category=0%7CAll%7Cmatchallpartial%7Call+categories&lnk=snav_sbox_iphonr",
				// "http://www.staples.com/iphone+6+7+8+9+10+note+pro+asdf+adfd+fas/directory_iphone+6+7+8+9+10+note+pro+asdf+adfd+fas?&akamai-feo=off",
				// "http://store.sony.com/search;pgid=1Y5uWvBKmyhSRqbSnldTM2S50000eJCcDJs1;sid=mcEmleHjaMtNlLQi4slPkdHpVaIYdi2ApyLk4Gad?SearchTerm=iphone+6+7+8+9+10+note+pro+%21%40%23%24%25%5E%26*%29%28~%60_+-+%3D%7C%5C%2F%3E%3C.%2C%2F%3F%3A%3B%27%22%5D%5B%7D%7B+asdf+adfd+fas",
				// "http://www.sierratradingpost.com/s~noresult/iphone%206%207%208%209%2010%20note%20pro%20%40%26_%20%20%20.%20asdf%20adfd%20fas/",
				// "http://www1.macys.com/shop/search?keyword=iphone+6+7+8+9+10+note+pro+%21%40%23%24%25%5E%26*%29%28~%60_+-+%3D%7C%5C%2F%3E%3C.%2C%2F%3F%3A%3B%27%22%5D%5B%7D%7B+asdf+adfd",
				// "http://www.meijer.com/catalog/search_command.cmd?keyword=iphone&tierId=",
				// "http://www.officedepot.com/catalog/search.do;jsessionid=00000eJZvDfEByJhJ37CNlRdCim:17h4h7ado?Ntt=iphone+6+7+8+9+10+note+pro+%21%40%23%24%25%5E%26*%29%28~%60_+-+%3D%7C%5C%2F%3E%3C.%2C%2F%3F%3A%3B%27%22%5D%5B%7D%7B+asdf+adfd+fas#$%^&*)(~`_
				// - =|\\/><.,/?:;'\"][}{ asdf adfd fas&Ntt=iphone 6 7 8 9 10
				// note pro !@#$%^&*)(~`_ - =|\\/><.,/?:;'\"][}{ asdf adfd fas",
				// "http://www.origins.com/products/search/esearch.tmpl?search=iphone+6+7+8+9+10+note+pro+%21%40%23%24%25%5E%26*%29%28~%60_+-+%3D%7C%5C%2F%3E%3C.%2C%2F%3F%3A%3B%27%22%5D%5B%7D%7B+asdf+adfd+fas&x=0&y=0",
				// "http://www.overstock.com/search?keywords=iphone+6+7+8+9+10+note+pro+%21%40%23%24%25%5E%26*%29%28~%60_+-+%3D%7C%5C%2F%3E%3C.%2C%2F%3F%3A%3B%27%22%5D%5B%7D%7B+asdf+adfd+fas&SearchType=Header",
				// "http://www.ralphlauren.com/search/noResults.jsp?kw=iphone+6+7+8+9+10+note+pro+-+%2F.%2F+asdf+adfd+fas",
				// "http://www.ritzcamera.com/searchresults?q=iphone+6+7+8+9+10+note+pro+%21%40%23%24%25%5E%26*%29%28~%60_+-+%3D%7C%5C%2F%3E%3C.%2C%2F%3F%3A%3B%27%22%5D%5B%7D%7B+asdf+adfd+fas#q=iphone
				// 6 7 8 9 10 note pro !@#$%^&*)(~`_ - =|\\/><.,/?:;'\"][}{ asdf
				// adfd fas&page=0&refinements=%5B%5D",
				// "http://www.rei.com/search.html?ir=q%3Aiphone+6+7+8+9+10+note+pro+%21%40%23%24%25%5E%26*%29%28~%60_+-+%3D%7C%5C%2F%3E%3C.%2C%2F%3F%3A%3B%27%22%5D%5B%7D%7B+asdf+adf&page=1&q=iphone+6+7+8+9+10+note+pro+%21%40%23%24%25%5E%26*%29%28~%60_+-+%3D%7C%5C%2F%3E%3C.%2C%2F%3F%3A%3B%27%22%5D%5B%7D%7B+asdf+adf",
				// "http://www.thebodyshop-usa.com/search.aspx?q=iphone%206%207%208%209%2010%20note%20pro%20!%40%23%24%25%5E*)(~%60_%20-%20%3D%7C%5C%2F%3E%3C.%2C%2F%3F%3A%3B%27%22%5D%5B%7D%7B%20asdf%20adf"
		};
		boolean ok = true;
		Transaction tx = session.beginTransaction();
		String referrer = null;
		try {
			do {
				ok = true;
				for (String u : urls) {
					result = bo.getAdsLink(sessionId, u, referrer, tempKey, false, null, null);
					logger.info("##list:" + result);
					String code = ((Map) result.get(0)).get("status").toString();
					if (code.equals(Configurations.CODE_NOT_EXIST)) {
						// ok = false;
						String list_pattern_id = ((Map) result.get(2)).get("pattern_id").toString();
						String[] split = list_pattern_id.split(";");
						for (String x : split) {
							if (x != null && !x.isEmpty()) {
								Long pattern_id = Long.parseLong(x.toString());
								if (pattern_id != -1L) {
									// Update current pattern to expired
									Criteria criteria = session.createCriteria(Pattern.class);
									criteria.add(Restrictions.eq("patternId", pattern_id));
									Pattern p = (Pattern) criteria.uniqueResult();
									p.setExpiredFlag(true);

									session.persist(p);
								}
								// Create new Pattern
								// Extract pattern from given URL
								// String signature = null;
								// bo.createSearchPattern(sessionId, d, u, 1,
								// signature,
								// true);
							}
						}
					}
				}
			} while (!ok);
			// Asserting all patterns again
			// for (String u : urls) {
			// try {
			// d = HttpUtil.getInstance().getDomainName(u);
			// } catch (MalformedURLException e) {
			// logger.error("MalformedURLException" + e.getMessage());
			// continue;
			// }
			// result = bo.getAdsLink(sessionId, u, d, tempKey, false);
			// logger.info("##list:" + result);
			// assertEquals(Configurations.CODE_OK, ((Map)
			// result.get(0)).get("status").toString());
			// }
			// Close the session
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		}
		session.close();
	}

	@SuppressWarnings("rawtypes")
	public void testGeneratePattern() {
		String sessionId = "1testGeneratePattern1";
		BoPattern bo = BoPattern.getInstance();

		// successful creation
		List success = bo.generatePatternList(sessionId);
		logger.info("##list:" + success);
		assertEquals(Configurations.CODE_OK, ((Map) success.get(0)).get("status").toString());
	}

	/**
	 * Only a helper function
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public void testlogAds() {
		String sessionId = "testlogAds123";
		BoPattern bo = BoPattern.getInstance();

		long userId = 1L;
		long patternId = 1L;
		String keyword = "iphone 99";
		String domain = "www.apple.com";
		String ads_url = bo.generateDummyAdsLink(keyword, domain);
		String identifier = "testlogAds123";

		List success = bo.logAds(sessionId, userId, patternId, ads_url, keyword, identifier, null);
		logger.info("##list:" + success);
		assertEquals(Configurations.CODE_OK, ((Map) success.get(0)).get("status").toString());
	}
}
