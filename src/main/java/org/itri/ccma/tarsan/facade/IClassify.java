package org.itri.ccma.tarsan.facade;

import java.util.List;

public interface IClassify {
	/**
	 * getCategory
	 * @param sessionId
	 * @param ip
	 * @return
	 */
	public List<?> getCategory(String sessionId, String ip);
}