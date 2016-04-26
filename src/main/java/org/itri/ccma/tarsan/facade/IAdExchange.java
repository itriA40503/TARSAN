package org.itri.ccma.tarsan.facade;

import java.util.Date;
import java.util.List;

public interface IAdExchange {
	
	/**
	 * createExUser
	 * @param sessionId
	 * @param account
	 * @param password
	 * @param address
	 * @param phone
	 * @return
	 */
	public List<?> createExUser(String sessionId, String account, String password, String address, String phone );
	
	/**
	 * ExUserlogin
	 * @param sessionId
	 * @param account
	 * @param password
	 * @return
	 */
	
	//e.g :http://localhost:8080/TARSAN/service/main?service=ExUserlogin&jsonPara=[("account":"itri"),("password":"123456")]
	public List<?> ExUserlogin(String sessionId, String account, String password);
	
	/**
	 * denyKeyword
	 * @param sessionId
	 * @param dKeyword
	 * @return
	 */
	public List<?> denyKeyword(String sessionId, String dKeyword);
	
	//********************************************//
	
	//public List<?>exuserLog(String sessionId, long exuserId, String ip,String activity);
	
	//public List<?>createExUser(String sessionId,long groupId, String account, String password, String name, String email, String phone, String address, String company,String timezone,String country,String currency);
	
	//public List<?>checkAccount(String sessionId, String account, String email);
	
	//public List<?>createPublishedAd(String sessionId, long vacantId, long exuserId, String title, String type, String content, String position, String size, Date startime, Date endtime, String intervaltime);
	
	//public List<?>listPublishAd(String sessionId, long exuserId);
	
	//public List<?>editPublishedAd(String sessionId, long buyadId, String title, String type, String content, String position, String size, Date startime, Date endtime, String intervaltime);
	
	//public List<?>delPublishedAd(String sessionId, long buyadId);
	
	//public List<?>createBuyAd2Loc(String sessionId, long buyadId, long locId);
	
	//public List<?>delBuyAd2Loc(String sessionId, long ad2locId);
			
	//public List<?>listBuyAd2Loc(String sessionId, long buyadId);
	
	//public List<?>createAdKeyword(String sessionId, long buyadId, String keyword);
	
	//public List<?>listAdKeyword(String sessionId, long buyadId);
	
	//public List<?>editAdKeyword(String sessionId, long adkId, String keyword);
	
	//public List<?>delAdKeyword(String sessionId, long adkId);
	
	//public List<?>createBudget(String sessionId, long buyadId, long budgetNum, String budgetUnit, long budgetCount, long budgetTotal);
	
	//public List<?>budgetLog(String sessionId, long budgetId, String counting, long budgetTatol);
	
	//public List<?>listBudget(String sessionId, long buyadId);
	
	//public List<?>EditBudget(String sessionId, long budgetId, long budgetTotal);
	
	//public List<?>listBudgetLog(String sessionId, long budgetId);
	
	//public List<?>createVacantAd(String sessionId, long exuserId, String type, String size, String position, String priceNum, String priceUnit, String priceTotal, Date startdate, Date enddate, String intervaltime);
	
	//public List<?>editVacantAd(String sessionId, long vacantId, String type, String size, String position, String priceNum, String priceUnit, String priceTotal, Date startdate, Date enddate, String intervaltime, boolean display);
	
	//public List<?>listAllVacantAd(String sessionId);
	
	//public List<?>listVacantAd(String sessionId, long exuserId);
	
	//public List<?>delVacantAd(String sessionId, long vacantId);
	
	//public List<?>listPublishAdbyVacantAd(String sessionId, long vacantId);
	
	//public List<?>listAllVacantAd(String sessionId);
	
	//public List<?>createLocation(String sessionId, long vacantId,String house, String floor, String zone);
	
	//public List<?>listLocation(String sessionId, long vacantId);
	
	//public List<?>delLocation(String sessionId, long locId);
	
	//public List<?>editLocation(String sessionId, long locId, String house, String floor, String zone);
	
	//public List<?>startPostAd(String sessionId, long buyadId);
	
	//public List<?>closePostAd(String sessionId, long buyadId);
	
	//public List<?>logAd(String session, long postadId, long buyadId, String searchKeyword, String domain, String urlhost, String urlreferer, String urlpath, String ip, boolean show, boolean click);
	
	//public List<?>listLogAd(String session, long postadId, long buyadId);
	
	//public List<?>createRunAd(String session, long postadId, long buyadId, String keywords, String type, String posistion, String size, String content, String house, String floor, String zone, Date startdate, Date enddate, String intervaltime);
}
