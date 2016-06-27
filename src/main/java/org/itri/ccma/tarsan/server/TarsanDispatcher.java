package org.itri.ccma.tarsan.server;

import java.text.ParseException;
import java.util.List;

import javassist.compiler.ast.Keyword;

import org.apache.log4j.Logger;
import org.itri.ccma.tarsan.biz.BoTest;
import org.itri.ccma.tarsan.biz.BoUser;
import org.itri.ccma.tarsan.facade.IAdExchange;
import org.itri.ccma.tarsan.facade.IAdsFacade;
import org.itri.ccma.tarsan.facade.IDataStatistics;
import org.itri.ccma.tarsan.facade.IPatternFacade;
import org.itri.ccma.tarsan.facade.IPublishFacade;
import org.itri.ccma.tarsan.facade.IUserFacade;
import org.itri.ccma.tarsan.facade.IUserTest;
import org.itri.ccma.tarsan.util.Configurations;

/**
 * The Class TarsanDispatcher.
 */
public class TarsanDispatcher {

	private static Logger logger = Logger.getLogger("consoleAppender");
	// /** The parking lot. */
	// private IParkingLotFacade parkingLot;
	//
	// /** The reservation. */
	// private IReservationFacade reservation;

	/*-*************************************************************************************************
	 *                                     Tarsan User API                                             *
	 ***************************************************************************************************/

	/** User Business Object */
	private IUserFacade user;

	/** Ads Business Object */
	private IAdsFacade ads;

	/** Search Query Pattern Business Object */
	private IPatternFacade pattern;
	
	/** Test Object */
	private IUserTest test;
	
	/** AdExchange Object */
	private IAdExchange exchange;
	
	/** PublishAd Object */
	private IPublishFacade publishAd;
	
	/** DataStatistics Object */
	private IDataStatistics dataStatistic;

	/**
	 * Instantiates a new u bike dispatcher.
	 */
	public TarsanDispatcher() {
		super();
		try {
			Class<?> clz = Class.forName("org.itri.ccma.tarsan.facade.impl.UserFacade");
			user = (IUserFacade) clz.newInstance();

			clz = Class.forName("org.itri.ccma.tarsan.facade.impl.AdsFacade");
			ads = (IAdsFacade) clz.newInstance();

			clz = Class.forName("org.itri.ccma.tarsan.facade.impl.PatternFacade");
			pattern = (IPatternFacade) clz.newInstance();
			
			clz = Class.forName("org.itri.ccma.tarsan.facade.impl.UserTest");
			test = (IUserTest) clz.newInstance();
			
			clz = Class.forName("org.itri.ccma.tarsan.facade.impl.AdExchangeFacade");
			exchange = (IAdExchange) clz.newInstance();
			
			clz = Class.forName("org.itri.ccma.tarsan.facade.impl.PublishFacade");
			publishAd = (IPublishFacade) clz.newInstance();
			
			clz = Class.forName("org.itri.ccma.tarsan.facade.impl.DataStatisticsFacade");
			dataStatistic = (IDataStatistics) clz.newInstance();
			
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/*-*************************************************************************************************
	 *                                           User                                                  *
	 ***************************************************************************************************/
	/**
	 * Create a new user account using the phone number.
	 * 
	 * @param sessionId
	 *            the session id
	 * @param email
	 *            the email
	 * @param phone
	 *            the phone number
	 * @param pin
	 *            the pin code
	 * @param password
	 *            the password
	 *
	 * @return the result message
	 */
	public List<?> createUser(String sessionId, String account, String password) {
		return user.createUser(sessionId, account, password);
	}

	/**
	 * Login.
	 * 
	 * @param sessionId
	 *            the session id
	 * @param account
	 *            Account name
	 * @param password
	 *            Password
	 * @return the list
	 */
	// Without encrypted text
	public List<?> login(String sessionId, String account, String password) {
		return user.login(sessionId, account, password);
	}


	/**
	 * 
	 * @param sessionId
	 * @param url
	 * @param userId
	 * @param patternId
	 * @param ip
	 * @param productSearchFlag
	 * @return
	 */
	public List<?> logUserEvent(String sessionId, String url, String referrer, Long userId, Long patternId, String ip,
			boolean productSearchFlag, Long parentId, Long rootId) {
		if(parentId==null) parentId = 0L;
		if(rootId==null) rootId = 0L;
		return user.logUserEvent(sessionId, url, referrer, userId, patternId, ip, productSearchFlag, parentId, rootId);
	}

	/**
	 * 
	 * @param sessionId
	 * @param url
	 * @param username
	 * @param patternId
	 * @param ip
	 * @param productSearchFlag
	 * @return
	 */
	public List<?> logUserEventICAP(String sessionId, String url, String referrer, String username, Long patternId,
			String ip, boolean productSearchFlag, Long parentId, Long rootId) {
		String tempkey = BoUser.getInstance().getTempKey(username);
		// Use tempkey as session ID
		if(parentId==null) parentId = 0L;
		if(rootId==null) rootId = 0L;
		return user.logUserEventICAP(tempkey, url, referrer, username, patternId, ip, productSearchFlag, parentId, rootId);
	}
	/*-*************************************************************************************************
	 *                                          Search Pattern                                         *
	 ***************************************************************************************************/
	/**
	 * Create new search pattern
	 * 
	 * @param sessionId
	 * @param url_host
	 * @param url_path
	 * @param type
	 * @param signature
	 * @return
	 */
	public List<?> createPattern(String sessionId, String url_host, String url_path, int type, String signature,
			int webType) {
		return pattern.create(sessionId, url_host, url_path, type, signature, webType);
	}	
	

	/**
	 * Retrieve ads URL by providing website's URL and domain
	 * 
	 * @param sessionId
	 *            generated session ID
	 * @param url
	 *            full url
	 * @param domain
	 *            host url (e.g., www.apple.com)
	 * @param tempKey
	 *            temporary key from user's login
	 * @return
	 */
	public List<?> getAdsLink(String sessionId, String url, String referrer, String tempKey, Long parentId, Long rootId) {
		if(parentId==null) parentId = 0L;
		if(rootId==null) rootId = 0L;
		return pattern.getAdsLink(sessionId, url, referrer, tempKey, parentId, rootId);
	}

	/**
	 * Retrieve ads URL by providing website's URL and domain
	 * 
	 * @param sessionId
	 *            generated session ID
	 * @param url
	 *            full url
	 * @param domain
	 *            host url (e.g., www.apple.com)
	 * @param tempKey
	 *            temporary key from user's login
	 * @return
	 */
	public List<?> getAdsLinkICAP(String sessionId, String url, String referrer, String username, Long parentId, Long rootId) {
		if(parentId==null) parentId = 0L;
		if(rootId==null) rootId = 0L;
		return pattern.getAdsLinkICAP(sessionId, url, referrer, username, parentId, rootId);
	}
	
	/**
	 * Generate list of all pattern in the database
	 * 
	 * @param sessionId
	 * @return
	 */
	public List<?> generatePatternList(String sessionId) {
		return pattern.generatePatternList(sessionId);
	}

	/*-*************************************************************************************************
	 *                                             Ads                                                 *
	 ***************************************************************************************************/
	/**
	 * Create new ads
	 * 
	 * @param sessionId
	 * @param keywords
	 * @param url
	 * @param img
	 * @param weight
	 * @return
	 */
	public List<?> createAds(String sessionId, String keywords, String url, String img, Float weight) {
		return ads.createAds(sessionId, keywords, url, img, weight);
	}

	/**
	 * Handle ads clicking event
	 * 
	 * @param sessionId
	 * @param adsId
	 * @param ad2userId
	 * @param username
	 * @param timestamp
	 * @param identifier
	 * @return
	 */
	public List<?> touch(String sessionId, Long adsId, Long ad2userId, String tempkey, Long timestamp,
			String identifier) {
		if (Configurations.IS_DEBUG)
			logger.debug("Touch - ad2userId :" + ad2userId);
		Long userId = BoUser.getInstance().getUserIdFromKey(tempkey);
		return ads.touch(sessionId, adsId, ad2userId, userId, timestamp, identifier);
	}
	
	/*-*************************************************************************************************
	 *                                             Test                                                 *
	 ***************************************************************************************************/
	/**
	 * getbarcode.
	 * 
	 * @param code_id
	 *            the barcode id
	 * @return the string 
	 */
	//without encrypted text
	public List<?> getBarcode(String sessionId,String barcodeId){
		return test.getBarcode(barcodeId);
	}
	
	/**
	 * record_use_barcode.
	 * 
	 * @param user_id
	 *            the user id
	 * @param barcode_id
	 *            the barcode id
	 * @param coor_x
	 *            the coordinate x
	 * @param coor_y
	 *            the coordinate y
	 * @return the string 
	 */
	//without encrypted text
	public List<?> recordUseBarcode(String sessionId,String userId, String barcodeId, String coordX, String coordY){
		return test.recordUseBarcode(sessionId, userId, barcodeId, coordX, coordY);
	}
	
	/**
	 * Random barcode.
	 * 
	 * @return the string 
	 */
	public List<?> randombarcode(String sessionId){
		return test.randombarcode();
	}
	
	/**
	 * Set barcose Shop.
	 * 
	 * @param shop
	 *            the shop
	 * @return the string 
	 */
	public List<?> setBarcodeShop(String sessionId,String shop){
		return test.setBarcodeShop(shop);
	}
	
	/**
	 * Get list of all shop in the database
	 * 
	 * @param sessionId
	 * @return
	 */
	public List<?> getShopList(String sessionId) {
		return test.getShopList(sessionId);
	}
	
	/**
	 * Get list of all keyword list in the database
	 * 
	 * @param sessionId
	 * @return
	 */
	public List<?> getKeywordList(String sessionId) {
		return test.getKeywordList(sessionId);
	}
	/**
	 * Get userId by username
	 * 
	 * @param username
	 * @return
	 */
	public List<?> getUserId(String sessionId, String username){
		return test.getUserId(username);
	}
	/**
	 * Get userId by UrlPatternList
	 * 
	 * @param sessionId
	 * @return
	 */
	public List<?> getUrlPatternList(String sessionId){
		return test.getUrlPatternList(sessionId);
	}
	/**
	 * 
	 * @param sessionId
	 * @param url
	 * @param userId
	 * @param patternId
	 * @param ip
	 * @param productSearchFlag
	 * @param keyword
	 * @return
	 */
	public List<?> logUserEvent_(String sessionId, String url, String referrer, Long userId, Long patternId, String ip,
			boolean productSearchFlag, Long parentId, Long rootId, String keyword) {
		if(parentId==null) parentId = 0L;
		if(rootId==null) rootId = 0L;
		return test.logUserEvent_(sessionId, url, referrer, userId, patternId, ip, productSearchFlag, parentId, rootId, keyword);
	}

	/**
	 * 
	 * @param sessionId
	 * @param url
	 * @param username
	 * @param patternId
	 * @param ip
	 * @param productSearchFlag
	 * @param keyword
	 * @return
	 */
	public List<?> logUserEventICAP_(String sessionId, String url, String referrer, String username, Long patternId,
			String ip, boolean productSearchFlag, Long parentId, Long rootId, String keyword) {
		String tempkey = BoUser.getInstance().getTempKey(username);
		// Use tempkey as session ID
		if(parentId==null) parentId = 0L;
		if(rootId==null) rootId = 0L;
		return test.logUserEventICAP_(tempkey, url, referrer, username, patternId, ip, productSearchFlag, parentId, rootId, keyword);
	}
	/**
	 * 
	 * @param sessionId
	 * @param username
	 * @return
	 */
	public List<?> getUserKeywordList(String sessionId, String Username) {		
		return test.getUserKeywordList(sessionId, Username);
	}
	/**
	 * 
	 * @param sessionId
	 * @return
	 */
	public List<?> getReviewKeywordList(String sessionId) {
		// TODO Auto-generated method stub
		return test.getReviewKeywordList(sessionId);
	}
	
	public List<?> getLastdata(String sessionId, int num){
		return test.getLastdata(sessionId, num);		
	}
	
	public List<?> getdatafromDate(String sessionId, String date){
		return test.getdatafromDate(sessionId, date);
	}
	
	public List<?> createPostad(String sessionId){
		return test.createPostad(sessionId);
	}
	
	/*-*************************************************************************************************
	 *                                             AdExchange                                                 *
	 ***************************************************************************************************/

	public List<?> createExUser(String sessionId, String account,String password, String address, String phone){
		return exchange.createExUser(sessionId, account, password, address, phone);
	}
	
	public List<?> ExUserlogin(String sessionId, String account, String password){
		return exchange.ExUserlogin(sessionId, account, password);
	}
	
	public List<?> denyKeyword(String sessionId, String dKeyword){
		return exchange.denyKeyword(sessionId, dKeyword);
	}
	
	/*-*************************************************************************************************
	 *                                             AdPubilsh                                            *
	 ***************************************************************************************************/

	public List<?> listRunAd(String sessionId){
		return publishAd.listRunAd(sessionId);
	}
	public List<?> getRunAd(String sessionId,String zone, String keyword){
		return publishAd.getRunAd(sessionId, zone, keyword);
	}
	
	public List<?> getRunAdKeywords(String sessionId,String zone){
		return publishAd.getRunAdKeywords(sessionId, zone);
	}
	
	public List<?> saveHTML(String sessionId, String text){
		return publishAd.saveHTML(sessionId, text);
	}
	
	public List<?> logad(String sessionId, long postadId, long buyadId,
			String searchKeyword, String domain, String host, String ref,
			String ip, String url, boolean show, boolean click, String clickCode){
		return publishAd.logad(sessionId, postadId, buyadId, searchKeyword, domain, host, ref, ip, url, show, click, clickCode);
	}
	
	public List getUserNameByMac(String sessionId,String macAddr){
		return publishAd.getUserNameByMac(sessionId, macAddr);
	}
	
	public List getPageByName(String sessionId,String username){
		return publishAd.getPageByName(sessionId, username);
	}
	
	public List getAllMachine(String sessionId){
		return publishAd.getAllMachine(sessionId);
	}
	
	public List getLastActive(String sessionId,String username){
		return publishAd.getLastActive(sessionId, username);
	}
	
	public List setPageUrl(String sessionId, String username, String url ){
		return publishAd.setPageUrl(sessionId, username, url);
	}
	
	public List getSessionTimebyMac(String sessionId,String macAddr){
		return publishAd.getSessionTimebyMac(sessionId, macAddr);
	}
	
	/*-*************************************************************************************************
	 *                                             DataStatistics                                       *
	 ***************************************************************************************************/
	public List<?>getDataByDays(String sessionId){
		return dataStatistic.getDataByDays(sessionId);
	}
	
	public List<?>getAllHost(String sessionId){
		return dataStatistic.getAllHost(sessionId);
	}
	
	public List<?>getAllIp(String sessionId){
		return dataStatistic.getAllIp(sessionId);
	}
	
	public List<?>getIpCountWithDays(String sessionId, String ip){
		return dataStatistic.getIpCountWithDays(sessionId, ip);
	}
	
	public List<?> getHostCountWithDays(String sessionId, String host){
		return dataStatistic.getHostCountWithDays(sessionId, host);
	}
	
	public List<?>getIntervalWithHost(String sessionId){
		return dataStatistic.getIntervalWithHost(sessionId);
	}
	
	public List<?>getHoursData(String sessionId){
		return dataStatistic.getHoursData(sessionId);
	}
	
	public List<?>getRefAndURL(String sessionId){
		return dataStatistic.getRefAndURL(sessionId);
	}
	
	public List<?>getDailyDataByDate(String sessionId, String date){
		return dataStatistic.getDailyDataByDate(sessionId, date);
	}
	
	public List<?>getDailyHoursDataByHost(String sessionId, String host){
		return dataStatistic.getDailyHoursDataByHost(sessionId, host);
	}
}
