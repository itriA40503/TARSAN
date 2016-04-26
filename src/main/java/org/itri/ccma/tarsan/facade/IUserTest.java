package org.itri.ccma.tarsan.facade;

import java.text.ParseException;
import java.util.List;

/**
 * The Interface IUserTest.
 */

public interface IUserTest {
	/**
	 * createUser.
	 * 
	 * @param username
	 *            the Account
	 * @param password
	 *            the password
	 * @return the string 
	 */
	//without encrypted text
	public String createUser(String username, String password);
	
	/**
	 * DeleteUser.
	 * 
	 * @param username
	 *            the Account
	 * @param password
	 *            the password
	 * @return the string 
	 */
	//without encrypted text
	public String DeleteUser(String username, String password);
	
	/**
	 * check password.
	 * 
	 * @param password
	 *            the password
	 * @return the string 
	 */
	//without encrypted text
	public String check_key(String password);
	
	/**
	 * check password(use MD5).
	 * 
	 * @param password
	 *            the password
	 * @param i
	 *            the algorithm 0="md5" 1="sha512"
	 * @return the string 
	 */
	//without encrypted text
	public String madehashkey(String password, int i);
	
	/**
	 * getBarcode.
	 * 
	 * @param barcodeId
	 *            the barcode id
	 * @return the string 
	 */
	//without encrypted text
	public List<?> getBarcode(String barcodeId);
	
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
	public List<?> recordUseBarcode(String sessionId,String userId, String barcodeId, String coordX, String coordY);
	
	/**
	 * Random barcode.
	 * 
	 * @return the string 
	 */
	//without encrypted text
	public List<?> randombarcode();
	
	/**
	 * Set barcode Shop.
	 * 
	 * @param shop
	 *            the shop
	 * @return the string 
	 */
	//without encrypted text
	public List<?> setBarcodeShop(String shop);
	
	/**
	 * get Shop List.
	 * 
	 * @param sessionId
	 *            the sessionId
	 * @return the string 
	 */
	//without encrypted text
	public List<?> getShopList(String sessionId);
	
	/**
	 * get keyword List.
	 * 
	 * @param sessionId
	 *            the sessionId
	 * @return the string 
	 */
	//without encrypted text
	public List<?> getKeywordList(String sessionId);
	
	/**
	 * get UserId.
	 * 
	 * @param username
	 *            the Username
	 * @return the string 
	 */
	//without encrypted text
	public List<?> getUserId(String username);
	
	/**
	 * get UrlPatternList.
	 * 
	 * @param sessionId
	 *            the Username
	 * @return the string sessionId
	 */
	//without encrypted text
	public List<?> getUrlPatternList(String sessionId);
	
	/**
	 * log every browsing history of the users
	 * @param sessionId
	 * @param url
	 * @param userId
	 * @param patternId
	 * @param keyword
	 * @return
	 */
	public List<?> logUserEvent_(String sessionId, String url, String referrer, Long userId, Long patternId, String ip, boolean productSearchFlag, Long parentId, Long rootId, String keyword);
	
	/**
	 * log every browsing history of the users from ICAP Server
	 * @param sessionId
	 * @param url
	 * @param username
	 * @param patternId
	 * @param ip
	 * @param keyword
	 * @return
	 */
	public List<?> logUserEventICAP_(String sessionId, String url, String referrer, String username, Long patternId, String ip, boolean productSearchFlag, Long parentId, Long rootId, String keyword);
	
	/**
	 * get getUserKeywordList.
	 * 
	 * @param sessionId
	 * @return the Username
	 */
	//without encrypted text
	public List<?> getUserKeywordList(String sessionId,String Username);
	
	/**
	 * get getReviewKeywordList.
	 * 
	 * @param sessionId
	 * @return  
	 */
	//without encrypted text
	public List<?> getReviewKeywordList(String sessionId);
	
	///////
	public List<?> getLastdata(String sessionId, int num);
	
	public List<?> getdatafromDate(String sessionId, String date);
	
	public List<?>getDataByDays(String sessionId);
	
	public List<?>getAllHost(String sessionId);
	
	public List<?>getAllIp(String sessionId);
	
	public List<?>getIpCountWithDays(String sessionId, String ip);
	
	public List<?>getHostCountWithDays(String sessionId, String host);
	
	public List<?>getIntervalWithHost(String sessionId);
	
	public List<?>getHoursData(String sessionId);
	
	public List<?>getRefAndURL(String sessionId);
	
	public List<?>getDailyDataByDate(String sessionId, String date);
	
	public List<?>getDailyHoursDataByHost(String sessionId, String host);
}
