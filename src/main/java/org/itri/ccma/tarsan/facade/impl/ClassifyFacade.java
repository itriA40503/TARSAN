package org.itri.ccma.tarsan.facade.impl;

import java.util.List;

import org.itri.ccma.tarsan.biz.BoClassify;
import org.itri.ccma.tarsan.facade.IClassify;

public class ClassifyFacade implements IClassify {

	@Override
	public List<?> getCategory(String sessionId, String ip) {
		// TODO Auto-generated method stub
		return BoClassify.getInstance().getCategory(sessionId, ip);
	}

}
