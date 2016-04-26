package org.itri.ccma.tarsan.facade.impl;

import java.util.List;

import org.itri.ccma.tarsan.biz.BoExchange;
import org.itri.ccma.tarsan.facade.IAdExchange;



public class AdExchangeFacade implements IAdExchange{

	@Override
	public List<?> createExUser(String sessionId, String account,
			String password, String address, String phone) {
		// TODO Auto-generated method stub
		return BoExchange.getInstance().createExUser(sessionId, account, password, address, phone);
	}

	@Override
	public List<?> ExUserlogin(String sessionId, String account, String password) {
		// TODO Auto-generated method stub
		return BoExchange.getInstance().ExUserlogin(sessionId, account, password);
	}

	@Override
	public List<?> denyKeyword(String sessionId, String dKeyword) {
		// TODO Auto-generated method stub
		return BoExchange.getInstance().denyKeyword(sessionId, dKeyword);
	}

	@Override
	public List<?> ExUserloginForTest(String sessionId, String account,
			String password) {
		// TODO Auto-generated method stub
		return BoExchange.getInstance().ExUserloginForTest(sessionId, account, password);
	}


}
