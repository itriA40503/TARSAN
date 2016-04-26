package org.itri.ccma.tarsan.test;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.itri.ccma.tarsan.biz.BoUser;
import org.itri.ccma.tarsan.hibernate.Userevent;
import org.itri.ccma.tarsan.hibernate.Users;
import org.itri.ccma.tarsan.util.Configurations;
import org.itri.ccma.tarsan.util.HibernateUtil;
import org.itri.ccma.tarsan.util.HttpUtil;

import junit.framework.TestCase;

public class BoUserTest extends TestCase {
	private Logger logger = Logger.getLogger(BoUserTest.class);

	public BoUserTest() {
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
	public void testLogin() {
		String sessionId = "1";
		BoUser user = BoUser.getInstance();
		List success = user.login(sessionId, "lalala", "lalala");
		logger.setLevel(Level.ALL);
		logger.info("##list:" + success);
		assertEquals(Configurations.CODE_OK, ((Map) success.get(0)).get("status").toString());

		// failed login attempts
		// Wrong password
		List fail1 = user.login(sessionId, "lalala", "123456");
		logger.info("##list:" + fail1);
		assertEquals(Configurations.CODE_FORBIDDEN, ((Map) fail1.get(0)).get("status").toString());
		// User does not exist
		List fail2 = user.login(sessionId, "abcdef", "lalala");
		logger.info("##list:" + fail2);
		assertEquals(Configurations.CODE_NOT_EXIST, ((Map) fail2.get(0)).get("status").toString());

		// Exceptional login attempts
		List attempt;
		attempt = user.login(sessionId, "ab%26cd", "123456");
		logger.info("##list:" + attempt);
		assertEquals(Configurations.CODE_NOT_EXIST, ((Map) attempt.get(0)).get("status").toString());

		attempt = user.login(sessionId, "abcd%5C", "123456");
		logger.info("##list:" + attempt);
		assertEquals(Configurations.CODE_NOT_EXIST, ((Map) attempt.get(0)).get("status").toString());

		attempt = user.login(sessionId, "ab&ab", "123456");
		logger.info("##list:" + attempt);
		assertEquals(Configurations.CODE_NOT_EXIST, ((Map) attempt.get(0)).get("status").toString());

		attempt = user.login(sessionId, "ab#ab", "123456");
		logger.info("##list:" + attempt);
		assertEquals(Configurations.CODE_NOT_EXIST, ((Map) attempt.get(0)).get("status").toString());

		attempt = user.login(sessionId, "ab%ab", "123456");
		logger.info("##list:" + attempt);
		assertEquals(Configurations.CODE_NOT_EXIST, ((Map) attempt.get(0)).get("status").toString());

		attempt = user.login(sessionId, "ab@ab", "123456");
		logger.info("##list:" + attempt);
		assertEquals(Configurations.CODE_NOT_EXIST, ((Map) attempt.get(0)).get("status").toString());

		attempt = user.login(sessionId, "ab!ab", "123456");
		logger.info("##list:" + attempt);
		assertEquals(Configurations.CODE_NOT_EXIST, ((Map) attempt.get(0)).get("status").toString());

		attempt = user.login(sessionId, "ab^ab", "123456");
		logger.info("##list:" + attempt);
		assertEquals(Configurations.CODE_NOT_EXIST, ((Map) attempt.get(0)).get("status").toString());

		attempt = user.login(sessionId, "ab/ab", "123456");
		logger.info("##list:" + attempt);
		assertEquals(Configurations.CODE_NOT_EXIST, ((Map) attempt.get(0)).get("status").toString());

		attempt = user.login(sessionId, "ab({[ab", "123456");
		logger.info("##list:" + attempt);
		assertEquals(Configurations.CODE_NOT_EXIST, ((Map) attempt.get(0)).get("status").toString());

		attempt = user.login(sessionId, "ab)}]ab", "123456");
		logger.info("##list:" + attempt);
		assertEquals(Configurations.CODE_NOT_EXIST, ((Map) attempt.get(0)).get("status").toString());

		attempt = user.login(sessionId, "\\//**~", "123456");
		logger.info("##list:" + attempt);
		assertEquals(Configurations.CODE_NOT_EXIST, ((Map) attempt.get(0)).get("status").toString());
	}

	@SuppressWarnings("rawtypes")
	public void testCreateUser() {
		String sessionId = "2";
		BoUser user = BoUser.getInstance();
		List attempt;
		int counter = 0;
		String test_username = "test_user_%d";

		// successful user creation -- with nickname supplied
		List success = user.createUser(sessionId, String.format(test_username, counter), "1plkw46n43f6e8jlofvdq88n");
		logger.info("##list:" + success);
		assertEquals(Configurations.CODE_OK, ((Map) success.get(0)).get("status").toString());

		// failed user creation -- missing or empty parameter
		List failExist = user.createUser(sessionId, String.format(test_username, counter), "1plkw46n43f6e8jlofvdq88n");
		logger.setLevel(Level.ALL);
		logger.info("##list:" + failExist);
		assertEquals(Configurations.CODE_ERROR, ((Map) failExist.get(0)).get("status").toString());
		
		// Add counter by 1 (as only 1 instance is created)
		counter++;

		// failed user creation -- incorrect password format (min length is 3
		// characters)
		List failPass = user.createUser(sessionId, String.format(test_username, counter), "1");
		logger.info("##list:" + failPass);
		assertEquals(Configurations.CODE_FORBIDDEN, ((Map) failPass.get(0)).get("status").toString());

		// failed user creation -- incorrect password format (max length is 128
		// characters)
		List failPass2 = user.createUser(sessionId, String.format(test_username, counter),
				"1plkw46n43f6e8jlofvdq88n1plkw46n43f6e8jlofvdq88n1plk123123w46n43f6e8jlofvdq88n1plkw46n43f6e8jlofvdq88n1plkw46n43f6e8jlofvdq88n1plkw46n43f6e8jlofvdq88n1plkw46n43f6e8jlofvdq88n1plkw46n43f6e8jlofvdq88n");
		logger.info("##list:" + failPass2);
		assertEquals(Configurations.CODE_FORBIDDEN, ((Map) failPass2.get(0)).get("status").toString());

		String[] password_ok = { "@@@@@@@@", "!!!!!!!", "~~~~~~~~~", "_____________", ".................", "??????????",
				"1234567890", "qwertyuiop1234567890" };
		String[] password_err = { "&&&&&&&", "^^^^^^^", "//////////", "*******", "\\\\\\\\\\\\\\\\\\", "((((((((",
				")))))))))", "[[[[[[[[[", "]]]]]]]]]", "-------------", "++++++++++++++++", "===============",
				"::::::::::::", ";;;;;;;;;;;;;", "''''''''''''", "<<<<<<<<<<<<<<<<", ">>>>>>>>>>>>>>", ",,,,,,,,,,,,,,",
				"`````````````", "||||||||||||||", "{{{{{{{", "}}}}}}}", "\"\"\"\"\"\"\"" };

		// Password ok, even thought it uses symbols
		for (String s : password_ok) {
			attempt = user.createUser(sessionId, String.format(test_username, counter++), s);
			logger.info("##list:" + attempt);
			assertEquals(Configurations.CODE_OK, ((Map) attempt.get(0)).get("status").toString());
		}
		// Password error because of some symbols
		for (String s : password_err) {
			attempt = user.createUser(sessionId, String.format(test_username, counter), s);
			logger.info("##list:" + attempt);
			assertEquals(Configurations.CODE_FORBIDDEN, ((Map) attempt.get(0)).get("status").toString());
		}
		// Password ok even though uses symbol, because of HTML Encoding (all
		// uses %XX)
		for (String s : password_err) {
			String code = HttpUtil.getInstance().HTML_Encoding(s);
			attempt = user.createUser(sessionId, String.format(test_username, counter++), code);
			logger.info("##list:" + attempt);
			assertEquals(Configurations.CODE_OK, ((Map) attempt.get(0)).get("status").toString());
		}

		// delete the test user
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		Criteria criteria;
		Users testUser;
		try {
			for (int i = 0; i < counter; i++) {
				criteria = session.createCriteria(Users.class);
				criteria.add(Restrictions.eq("account", String.format(test_username, i)));
				testUser = (Users) criteria.uniqueResult();
				session.delete(testUser);
			}
			logger.info("##Deleted test users:" + counter);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		}
		session.close();
	}

	@SuppressWarnings("rawtypes")
	public void testlogUserEvent() {
		String sessionId = "2testlogUserEvent2";
		BoUser bo = BoUser.getInstance();
		String url = "2testlogUserEvent2";
		String referrer = "www.lalala.com";
		Long userId = 1L; // Lalala
		Long patternId = -99999L; // Not exist
		String ip = "127.0.0.1";
		boolean isProductSearch = false;

		// successful log
		List success = bo.logUserEvent(sessionId, url, referrer, userId, patternId, ip, isProductSearch, null, null);
		logger.info("##list:" + success);
		assertEquals(Configurations.CODE_OK, ((Map) success.get(0)).get("status").toString());
		// Another successful log with valid pattern id
		patternId = 1L;
		success = bo.logUserEvent(sessionId, url, referrer, userId, patternId, ip, isProductSearch, null, null);
		logger.info("##list:" + success);
		assertEquals(Configurations.CODE_OK, ((Map) success.get(0)).get("status").toString());
		// Invalid user id
		userId = null;
		success = bo.logUserEvent(sessionId, url, referrer, userId, patternId, ip, isProductSearch, null, null);
		logger.info("##list:" + success);
		assertEquals(Configurations.CODE_OK, ((Map) success.get(0)).get("status").toString());

		// valid username
		String username = "lalala";
		success = bo.logUserEventICAP(sessionId, url, referrer, username, patternId, ip, isProductSearch, null, null);
		logger.info("##list:" + success);
		assertEquals(Configurations.CODE_OK, ((Map) success.get(0)).get("status").toString());

		username = "9d8ae1911b8c16df0a36b60e08e99b5d";
		success = bo.logUserEventICAP(sessionId, url, referrer, username, patternId, ip, isProductSearch, null, null);
		logger.info("##list:" + success);
		assertEquals(Configurations.CODE_OK, ((Map) success.get(0)).get("status").toString());

		List attempt;
		userId = 1L;
		patternId = 1L;
		username = "lalala";
		// Weird URLs
		String[] urls = { "http://buy.mi.com/tw/search_mi%20note%20pro",
				"http://www.amazon.com/s/ref=nb_sb_noss_2?url=search-alias%3Daps&field-keywords=mi+note",
				"http://search.ruten.com.tw/search/s000.php?searchfrom=indexbar&k=mi+note+pro&t=0",
				"http://www.amazon.com/Smartphone-android-Snapdragon-1920x1080-Beenle%C2%AE/dp/B00XM65SSS/ref=sr_1_1?ie=UTF8&qid=1438738909&sr=8-1&keywords=mi+note",
				"http://www.amazon.com/s/ref=nb_sb_noss_2?url=search-alias%3Daps&field-keywords=mi+note+pro",
				"http://www.amazon.com/DAYJOY-Fullbody-Protection-Aluminum-protector/dp/B00YDV59JE/ref=sr_1_1?ie=UTF8&qid=1438738977&sr=8-1&keywords=mi+note+pro",
				"http://global.rakuten.com/en/search/?tl=&k=mi+note+pro",
				"http://www.ebay.com/sch/i.html?_from=R40&_trksid=p2050601.m570.l1313.TR0.TRC0.H0.Xxiao+mi+mi+note+pro+2015.TRS0&_nkw=xiao+mi+mi+note+pro+2015&_sacat=0",
				"http://www.bestbuy.com/site/searchpage.jsp?st=xiao+mi+mi+note+pro+5.7%22&_dyncharset=UTF-8&id=pcat17071&type=page&sc=Global&cp=1&nrp=15&sp=&qp=&list=n&iht=y&usc=All+Categories&ks=960&keys=keys",
				"http://www.bestbuy.com/site/computers-pcs/laptop-computers/abcat0502000.c?id=abcat0502000",
				"http://www.ebay.com/sch/i.html?_from=R40&_trksid=p2047675.m570.l1313.TR0.TRC0.H0.Xmi+note+%26+pro+*+%23.TRS0&_nkw=mi+note+%26+pro+*+%23&_sacat=0",
				"http://www.ebay.com/sch/i.html?_odkw=mi+note+%26+pro+*+%23&_osacat=0&_from=R40&_trksid=p2045573.m570.l1313.TR0.TRC0.H0.Xmi+note+%26+pro+*+%23+~+%60+%21+%40+%24+%25+%5E+%28+-+_+%2B+%3D+%5C+%2B+%7C.TRS0&_nkw=mi+note+%26+pro+*+%23+~+%60+%21+%40+%24+%25+%5E+%28+-+_+%2B+%3D+%5C+%2B+%7C&_sacat=0",
				"http://www.ebay.com/sch/i.html?_odkw=mi+note+%26+pro+*+%23+~+%60+%21+%40+%24+%25+%5E+%28+-+_+%2B+%3D+%5C+%2B+%7C&_osacat=0&_from=R40&_trksid=p2045573.m570.l1313.TR0.TRC0.H0.Xmi+note+%26+pro+*+%23+~+%60+%21+%40+%24+%25+%5E+%28+-+_+%2B+%3D+%5C+%2B+%7C+%7D+%5B+%3B+%3A+%27+%3E+%22+%3F+%2F+.+%3C+%2C+%2B+%2B+-+*.TRS0&_nkw=mi+note+%26+pro+*+%23+~+%60+%21+%40+%24+%25+%5E+%28+-+_+%2B+%3D+%5C+%2B+%7C+%7D+%5B+%3B+%3A+%27+%3E+%22+%3F+%2F+.+%3C+%2C+%2B+%2B+-+*+%2F+4+57+5454+7+9+6+%2B7%2B+%2B+%2B+4%2B+49%2B4+%2B45%2B+&_sacat=0",
				"http://www.amazon.com/s/ref=nb_sb_noss?url=search-alias%3Daps&field-keywords=mi+note+pro+mi+note+%26+pro+*+%23+~+%60+%21+%40+%24+%25+%5E+%28+-+_+%2B+%3D+%5C+%2B+%7C+%7D+%5B+%3B+%3A+%27+++%22+%3F+%2F+.+++%2C+%2B+%2B+-+*+%2F+4+57+5454+7+9+6+%2B7%2B+%2B+%2B+4%2B+49%2B4+%2B45%2B",
				"http://www.amazon.com/s/ref=nb_sb_noss?url=search-alias%3Daps&field-keywords=mi+note+pro+mi+note+%26+pro++%23+~+%60+!+%40+%24+%25+%5E+(+-+_+%2B+%3D+%5C+%2B+%7C+%7D+%5B+%3B+%3A+%27+++%22+%3F+%2F+.+++%2C+%2B+%2B+-++%2F+4+57+5454+7+9+6+%2B7%2B+%2B+%2B+4%2B+49%2B4+%2B45%2B+%5C+%5C+1+2+%3D+%5C+-+)+)+)+(+)+(+)+%7B+%7D+%5B+%5D+adf+adsf+%3B+%27+%3B+%27%3B%27+%3B%27+%3B&rh=i%3Aaps%2Ck%3Ami+note+pro+mi+note+%26+pro++%23+~+%60+!+%40+%24+%25+%5E+(+-+_+%2B+%3D+%5C+%2B+%7C+%7D+%5B+%3B+%3A+%27+++%22+%3F+%2F+.+++%5Cc+%2B+%2B+-++%2F+4+57+5454+7+9+6+%2B7%2B+%2B+%2B+4%2B+49%2B4+%2B45%2B+%5C+%5C+1+2+%3D+%5C+-+)+)+)+(+)+(+)+%7B+%7D+%5B+%5D+adf+adsf+%3B+%27+%3B+%27%3B%27+%3B%27+%3B",
				"http://www.walmart.com/search/?query=iphone%206%207%208%209%2010%20note%20pro%20!%40%23%24%25%5E%26*)(~%60_%2B-%2B%3D%7C%5C%2F%3E%3C.%2C%2F%3F%3A%3B%27%22%5D%5B%7D%7B%20asdf%20adfd%20fas",
				"http://search-en.lego.com/?q=iphone+6+7+8+9+10+note+pro+!%40%23%24%25%5e%26*)(~%60_+-+%3d%7c%5c%2f%3E%3C.%2c%2f%3f%3a%3b%27%22%5d%5b%7d%7b+asdf+adfd+fas&cc=US",
				"http://shopap.lenovo.com/SEUILibrary/controller/e/twweb/LenovoPortal/zh_TW/site.workflow:SimpleSiteSearch?q=iphone+6+7+8+9+10+note+pro+%21%40%23%24%25%5E%26*%29%28~%60_%2B-%2B%3D%7C%5C%2F%3E%3C.%2C%2F%3F%3A%3B%27%22%5D%5B%7D%7B+asdf+adfd+fas&cc=tw&lang=zh",
				"https://www.lowes.com/webapp/wcs/stores/servlet/SearchCatalog?catalogId=10051&catalogId=10051&langId=-1&langId=-1&storeId=10151&krypto=bzkdjVg1OuuhzzjWPQ7rdo0FscUFVdgA1g3imqgc3DJ%2F17OVQpF4SO3sk%2BwKJbFCR9Sp9dppsNsltLqu6Lflh0W1mD39GDI9bTCoNQpr9WZeR8EH3%2FqhEUfb2TS6b4Y87aX08inSB6sgO4FpSFLv8P%2B2axKLjrquhcZYWc0iJEq4BaG4UHzFIGfQJkEQeV9OVNbYtIbksSTFWYq3AEKJCS9rAPxw2PU3VLcBsKnnx8PdJh7qWhqp76nyKhydQmpXAnGPTtW5zEmTtSxrbLw4m%2FVCfe30gAggfAmKu24w4IcsasMgdF4rt1TS1jnystUW84fk5%2FD0EvtJkCC6AHpSKflo3B42inqfoA33Lu3RLIcGNx3qVUekJEHMspoqG3NDU2ZxxBQKAG%2FTz%2FI70jTQNMTxOrN8G18%2B4DUKfb25w0vn49XZOtYTNRdsXniuv3J1bUgL5W7UQVaNmQp0r%2FaBlxzCatub4dv%2BtMDwqSAee3y6WvlwKuQL84%2BJ%2FjVn56NmX92oLnf%2BftUrvAnXtjnzLSRV6oQCZawUhT2T2uIWPu8%3D#$^&)(~`_+-+=|\\/><.,/?:;'\"][}{+asdf+adfd+fas",
				"http://www1.macys.com/shop/search?keyword=iphone+6+7+8+9+10+note+pro+%21%40%23%24%25%5E%26*%29%28~%60_%2B-%2B%3D%7C%5C%2F%3E%3C.%2C%2F%3F%3A%3B%27%22%5D%5B%7D%7B+asdf+adfd",
				"http://www.target.com/s?searchTerm=iphone+6+7+8+9+10+note+pro+%21%40%23%24%25%5E%26*%29%28~%60_+-+%3D%7C%5C%2F%3E%3C.&category=0%7CAll%7Cmatchallpartial%7Call+categories&lnk=snav_sbox_iphone+6+7+8+9+10+note+pro+%21%40%23%24%25%5E%26*%29%28~%60_+-+%3D%7C%5C%2F%3E%3C.",
				"http://www.sunglasshut.com/webapp/wcs/stores/servlet/ProhibitedCharacterErrorView?searchType=102&searchSource=Q&searchingFrom=global&orderBy=default&showResultsPage=true&langId=-1&beginIndex=0&sType=SimpleSearch&pageSize=18&resultCatEntryType=2&searchTerm=iphone+6+7+8+9+10+note+pro+%21%40%23%24%25%5E%26*%29%28~%60_+-+%3D%7C%5C%2F%3E%3C.%2C%2F%3F%3A%3B%27%22%5D%5B%7D%7B+asd&catalogId=10101&pageView=image&storeId=10152",
				"http://www.sunglasshut.com/SearchDisplay?storeId=10152&catalogId=10101&langId=-1&pageSize=18&beginIndex=0&searchSource=Q&sType=SimpleSearch&resultCatEntryType=2&searchType=102&searchingFrom=global&showResultsPage=true&pageView=image&orderBy=default&searchTerm=iphon+e6",
				"http://www.target.com/s?searchTerm=iphonr&category=0%7CAll%7Cmatchallpartial%7Call+categories&lnk=snav_sbox_iphonr",
				"http://www.staples.com/iphone+6+7+8+9+10+note+pro+asdf+adfd+fas/directory_iphone+6+7+8+9+10+note+pro+asdf+adfd+fas?&akamai-feo=off",
				"http://store.sony.com/search;pgid=1Y5uWvBKmyhSRqbSnldTM2S50000eJCcDJs1;sid=mcEmleHjaMtNlLQi4slPkdHpVaIYdi2ApyLk4Gad?SearchTerm=iphone+6+7+8+9+10+note+pro+%21%40%23%24%25%5E%26*%29%28~%60_+-+%3D%7C%5C%2F%3E%3C.%2C%2F%3F%3A%3B%27%22%5D%5B%7D%7B+asdf+adfd+fas",
				"http://www.sierratradingpost.com/s~noresult/iphone%206%207%208%209%2010%20note%20pro%20%40%26_%20%20%20.%20asdf%20adfd%20fas/",
				"http://www1.macys.com/shop/search?keyword=iphone+6+7+8+9+10+note+pro+%21%40%23%24%25%5E%26*%29%28~%60_+-+%3D%7C%5C%2F%3E%3C.%2C%2F%3F%3A%3B%27%22%5D%5B%7D%7B+asdf+adfd",
				"http://www.meijer.com/catalog/search_command.cmd?keyword=iphone&tierId=",
				"http://www.officedepot.com/catalog/search.do;jsessionid=00000eJZvDfEByJhJ37CNlRdCim:17h4h7ado?Ntt=iphone+6+7+8+9+10+note+pro+%21%40%23%24%25%5E%26*%29%28~%60_+-+%3D%7C%5C%2F%3E%3C.%2C%2F%3F%3A%3B%27%22%5D%5B%7D%7B+asdf+adfd+fas#$%^&*)(~`_ - =|\\/><.,/?:;'\"][}{ asdf adfd fas&Ntt=iphone 6 7 8 9 10 note pro !@#$%^&*)(~`_ - =|\\/><.,/?:;'\"][}{ asdf adfd fas",
				"http://www.origins.com/products/search/esearch.tmpl?search=iphone+6+7+8+9+10+note+pro+%21%40%23%24%25%5E%26*%29%28~%60_+-+%3D%7C%5C%2F%3E%3C.%2C%2F%3F%3A%3B%27%22%5D%5B%7D%7B+asdf+adfd+fas&x=0&y=0",
				"http://www.overstock.com/search?keywords=iphone+6+7+8+9+10+note+pro+%21%40%23%24%25%5E%26*%29%28~%60_+-+%3D%7C%5C%2F%3E%3C.%2C%2F%3F%3A%3B%27%22%5D%5B%7D%7B+asdf+adfd+fas&SearchType=Header",
				"http://www.ralphlauren.com/search/noResults.jsp?kw=iphone+6+7+8+9+10+note+pro+-+%2F.%2F+asdf+adfd+fas",
				"http://www.ritzcamera.com/searchresults?q=iphone+6+7+8+9+10+note+pro+%21%40%23%24%25%5E%26*%29%28~%60_+-+%3D%7C%5C%2F%3E%3C.%2C%2F%3F%3A%3B%27%22%5D%5B%7D%7B+asdf+adfd+fas#q=iphone 6 7 8 9 10 note pro !@#$%^&*)(~`_ - =|\\/><.,/?:;'\"][}{ asdf adfd fas&page=0&refinements=%5B%5D",
				"http://www.rei.com/search.html?ir=q%3Aiphone+6+7+8+9+10+note+pro+%21%40%23%24%25%5E%26*%29%28~%60_+-+%3D%7C%5C%2F%3E%3C.%2C%2F%3F%3A%3B%27%22%5D%5B%7D%7B+asdf+adf&page=1&q=iphone+6+7+8+9+10+note+pro+%21%40%23%24%25%5E%26*%29%28~%60_+-+%3D%7C%5C%2F%3E%3C.%2C%2F%3F%3A%3B%27%22%5D%5B%7D%7B+asdf+adf",
				"http://www.thebodyshop-usa.com/search.aspx?q=iphone%206%207%208%209%2010%20note%20pro%20!%40%23%24%25%5E*)(~%60_%20-%20%3D%7C%5C%2F%3E%3C.%2C%2F%3F%3A%3B%27%22%5D%5B%7D%7B%20asdf%20adf" };
		for (int i = 0; i < urls.length; i++) {
			attempt = bo.logUserEvent(sessionId, urls[i], referrer, userId, patternId, ip, false, null, null);
			logger.info("##list:" + attempt);
			assertEquals(Configurations.CODE_OK, ((Map) attempt.get(0)).get("status").toString());
			attempt = bo.logUserEventICAP(sessionId, urls[i], referrer, username, patternId, ip, false, null, null);
			logger.info("##list:" + attempt);
			assertEquals(Configurations.CODE_OK, ((Map) attempt.get(0)).get("status").toString());
		}

		// delete all user events which were created in the middle of testing
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Userevent.class);
			criteria.add(Restrictions.eq("sessionId", sessionId));
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
}
