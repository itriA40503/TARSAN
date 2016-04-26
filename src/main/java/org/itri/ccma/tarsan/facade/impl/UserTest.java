package org.itri.ccma.tarsan.facade.impl;

import java.text.ParseException;
import java.util.List;

import org.itri.ccma.tarsan.biz.BoTest;
import org.itri.ccma.tarsan.biz.BoUser;
import org.itri.ccma.tarsan.facade.IUserTest;

public class UserTest implements IUserTest {
	@Override
	public String createUser(String username, String password) {
		return BoTest.getInstance().createUser(username, password);
	}

	@Override
	public String DeleteUser(String username, String password) {
		return BoTest.getInstance().DeleteUser(username, password);
	}

	@Override
	public String check_key(String password) {

		return BoTest.getInstance().check_key(password);
	}

	@Override
	public String madehashkey(String password, int i) {
		return BoTest.getInstance().madehashkey(password, i);
	}

	@Override
	public List<?> getBarcode(String barcodeId) {
		return BoTest.getInstance().getBarcode(barcodeId);
	}

	@Override
	public List<?> recordUseBarcode(String sessionId,String userId, String barcodeId, String coordX, String coordY) {
		return BoTest.getInstance().recordUseBarcode(sessionId, userId,
				barcodeId, coordX, coordY);
	}

	@Override
	public List<?> randombarcode() {
		return BoTest.getInstance().randombarcode();
	}

	@Override
	public List<?> setBarcodeShop(String shop) {
		return BoTest.getInstance().setBarcodeShop(shop);
	}

	@Override
	public List<?> getShopList(String sessionId) {
		// TODO Auto-generated method stub
		return BoTest.getInstance().getShopList(sessionId);
	}

	@Override
	public List<?> getKeywordList(String sessionId) {
		// TODO Auto-generated method stub
		return BoTest.getInstance().getKeywordList(sessionId);
	}

	@Override
	public List<?> getUserId(String username) {
		// TODO Auto-generated method stub
		return BoTest.getInstance().getUserId(username);
	}

	@Override
	public List<?> getUrlPatternList(String sessionId) {
		// TODO Auto-generated method stub
		return BoTest.getInstance().getUrlPatternList(sessionId);
	}

	@Override
	public List<?> logUserEvent_(String sessionId, String url, String referrer,
			Long userId, Long patternId, String ip, boolean productSearchFlag,
			Long parentId, Long rootId, String keyword) {
		// TODO Auto-generated method stub
		return BoTest.getInstance().logUserEvent_(sessionId, url, referrer, userId, patternId, ip, productSearchFlag, parentId, rootId, keyword);
	}

	@Override
	public List<?> logUserEventICAP_(String sessionId, String url,
			String referrer, String username, Long patternId, String ip,
			boolean productSearchFlag, Long parentId, Long rootId,
			String keyword) {
		// TODO Auto-generated method stub
		Long userId = BoUser.getInstance().getUserId(username);
		return BoTest.getInstance().logUserEvent_(sessionId, url, referrer, userId, patternId, ip, productSearchFlag, parentId, rootId, keyword);
	}

	@Override
	public List<?> getUserKeywordList(String sessionId, String Username) {
		// TODO Auto-generated method stub
		Long userId = BoUser.getInstance().getUserId(Username);;
		return BoTest.getInstance().getUserKeywordList(sessionId, userId);
	}

	@Override
	public List<?> getReviewKeywordList(String sessionId) {
		// TODO Auto-generated method stub
		return BoTest.getInstance().getReviewKeywordList(sessionId);
	}

	@Override
	public List<?> getLastdata(String sessionId, int num) {
		// TODO Auto-generated method stub
		return BoTest.getInstance().getLastdata(sessionId, num);
	}

	public List<?> getdatafromDate(String sessionId, String date) {
		// TODO Auto-generated method stub
		return BoTest.getInstance().getdatafromDate(sessionId, date);
	}

	@Override
	public List<?> getDataByDays(String sessionId) {
		// TODO Auto-generated method stub
		return BoTest.getInstance().getDataByDays(sessionId);
	}

	@Override
	public List<?> getAllHost(String sessionId) {
		// TODO Auto-generated method stub
		return BoTest.getInstance().getAllHost(sessionId);
	}

	@Override
	public List<?> getAllIp(String sessionId) {
		// TODO Auto-generated method stub
		return BoTest.getInstance().getAllIp(sessionId);
	}

	@Override
	public List<?> getIpCountWithDays(String sessionId, String ip) {
		// TODO Auto-generated method stub
		return BoTest.getInstance().getIpCountWithDays(sessionId, ip);
	}

	@Override
	public List<?> getHostCountWithDays(String sessionId, String host) {
		// TODO Auto-generated method stub
		return BoTest.getInstance().getHostCountWithDays(sessionId, host);
	}

	@Override
	public List<?> getIntervalWithHost(String sessionId) {
		// TODO Auto-generated method stub
		return BoTest.getInstance().getIntervalWithHost(sessionId);
	}

	@Override
	public List<?> getHoursData(String sessionId) {
		// TODO Auto-generated method stub
		return BoTest.getInstance().getHoursData(sessionId);
	}

	@Override
	public List<?> getRefAndURL(String sessionId) {
		// TODO Auto-generated method stub
		return BoTest.getInstance().getRefAndURL(sessionId);
	}

	@Override
	public List<?> getDailyDataByDate(String sessionId, String date) {
		// TODO Auto-generated method stub
		return BoTest.getInstance().getDailyDataByDate(sessionId, date);
	}

	@Override
	public List<?> getDailyHoursDataByHost(String sessionId, String host) {
		// TODO Auto-generated method stub
		return BoTest.getInstance().getDailyHoursDataByHost(sessionId, host);
	}
	
	



}
