package org.itri.ccma.tarsan.test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.itri.ccma.tarsan.biz.BoAds;
import org.itri.ccma.tarsan.hibernate.Ad;
import org.itri.ccma.tarsan.hibernate.Ad2user;
import org.itri.ccma.tarsan.util.Configurations;
import org.itri.ccma.tarsan.util.HibernateUtil;

import junit.framework.TestCase;

public class BoAdsTest extends TestCase {
	private Logger logger = Logger.getLogger(BoAdsTest.class);

	public BoAdsTest() {
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
	public void testCreateAds() {
		String sessionId = "1testCreateAds";
		BoAds bo = BoAds.getInstance();

		// successful creation
		List success = bo.create(sessionId, "lalala", "www.lalala.com", null, 1.0F);
		logger.info("##list:" + success);
		assertEquals(Configurations.CODE_OK, ((Map) success.get(0)).get("status").toString());

		// failed creation -- duplicate
		List failed = bo.create(sessionId, "lalala", "www.lalala.com", null, 1.0F);
		logger.info("##list:" + failed);
		assertEquals(Configurations.CODE_ERROR, ((Map) failed.get(0)).get("status").toString());

		// Various test
		List attempt;
		String[] keywords = { "normal keyword", "mi note & pro *", "# ~ ` ! @ $ % ^", "( - _ + = \\ + | } [",
				"; : '   \" ? / .   , + + - *", " / 4 57 5454 7 9 6 +7+ + + 4+ 49+4 +45+" };
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
				"http://www.amazon.com/s/ref=nb_sb_noss?url=search-alias%3Daps&field-keywords=mi+note+pro+mi+note+%26+pro++%23+~+%60+!+%40+%24+%25+%5E+(+-+_+%2B+%3D+%5C+%2B+%7C+%7D+%5B+%3B+%3A+%27+++%22+%3F+%2F+.+++%2C+%2B+%2B+-++%2F+4+57+5454+7+9+6+%2B7%2B+%2B+%2B+4%2B+49%2B4+%2B45%2B+%5C+%5C+1+2+%3D+%5C+-+)+)+)+(+)+(+)+%7B+%7D+%5B+%5D+adf+adsf+%3B+%27+%3B+%27%3B%27+%3B%27+%3B&rh=i%3Aaps%2Ck%3Ami+note+pro+mi+note+%26+pro++%23+~+%60+!+%40+%24+%25+%5E+(+-+_+%2B+%3D+%5C+%2B+%7C+%7D+%5B+%3B+%3A+%27+++%22+%3F+%2F+.+++%5Cc+%2B+%2B+-++%2F+4+57+5454+7+9+6+%2B7%2B+%2B+%2B+4%2B+49%2B4+%2B45%2B+%5C+%5C+1+2+%3D+%5C+-+)+)+)+(+)+(+)+%7B+%7D+%5B+%5D+adf+adsf+%3B+%27+%3B+%27%3B%27+%3B%27+%3B" };
		for (String key : keywords) {
			for (String url : urls) {
				attempt = bo.create(sessionId, key, url, url, 1.0F);
				logger.info("##list:" + attempt);
				assertEquals(Configurations.CODE_OK, ((Map) attempt.get(0)).get("status").toString());
			}
		}

		// delete the test user
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		Criteria criteria = session.createCriteria(Ad.class);
		criteria.add(Restrictions.eq("keywords", "lalala"));
		criteria.add(Restrictions.eq("url", "www.lalala.com"));
		Ad test = (Ad) criteria.uniqueResult();
		try {
			if (test != null) {
				session.delete(test);
			}

			for (String key : keywords) {
				for (String url : urls) {
					criteria = session.createCriteria(Ad.class);
					criteria.add(Restrictions.eq("keywords", key));
					criteria.add(Restrictions.eq("url", url));
					test = (Ad) criteria.uniqueResult();
					if (test != null)
						session.delete(test);
				}
			}
			logger.info("All ads for testing has been deleted: " + keywords.length * urls.length);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		}
		session.close();
	}

	@SuppressWarnings("rawtypes")
	public void testTouch() {
		String sessionId = "testTouch123";
		BoAds bo = BoAds.getInstance();

		List trial = null;
		List list;
		Long adId = -1L;
		Long ad2userId = -1L;
		Long userId = 1L;

		Session session = HibernateUtil.getSessionFactory().openSession();
		// Take any random 1 instance from Ad and Ad2user
		list = session.createCriteria(Ad.class).list();
		if (list.isEmpty()) {
			System.err.println("Abort testing on touching ads -- No Ads in the database");
			return;
		}
//		adId = ((Ad) list.get(0)).getAdId();
		adId = 1L;

		list = session.createCriteria(Ad2user.class).list();
		if (list.isEmpty()) {
			System.err.println("Abort testing on touching ads -- No Corresponding ad2user in the database");
			return;
		}
//		ad2userId = ((Ad2user) list.get(0)).getAd2userId();
		ad2userId = 1L;

		// Successful
		Long timestamp = new Date().getTime();
		String identifier = "localhost";
		trial = bo.touch(sessionId, adId, ad2userId, userId, timestamp, identifier);
		logger.info("##list:" + trial);
		assertEquals(Configurations.CODE_OK, ((Map) trial.get(0)).get("status").toString());

		// Failed
		Long adError;
		Long ad2UserError;

		adError = adId;
		ad2UserError = -1L;
		trial = bo.touch(sessionId, adError, ad2UserError, userId, timestamp, identifier);
		logger.info("##list:" + trial);
		assertEquals(Configurations.CODE_NOT_EXIST, ((Map) trial.get(0)).get("status").toString());

		adError = -1L;
		ad2UserError = ad2userId;
		trial = bo.touch(sessionId, adError, ad2UserError, userId, timestamp, identifier);
		logger.info("##list:" + trial);
		assertEquals(Configurations.CODE_NOT_EXIST, ((Map) trial.get(0)).get("status").toString());

		adError = -1L;
		ad2UserError = -1L;
		trial = bo.touch(sessionId, adError, ad2UserError, userId, timestamp, identifier);
		logger.info("##list:" + trial);
		assertEquals(Configurations.CODE_NOT_EXIST, ((Map) trial.get(0)).get("status").toString());
	}
}
