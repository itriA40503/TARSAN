package org.itri.ccma.tarsan.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Standardize response message generation
 * @author 532711
 *
 */
public class MessageUtil {
	/* Singleton */
	private MessageUtil() {

	}

	public static MessageUtil getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {

		private static final MessageUtil INSTANCE = new MessageUtil();
	}

	/****************************************************************************************/
	/*
	 * Logical function
	 */
	/****************************************************************************************/

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList generateResponseMessage(String statusCode, String methodName, String sessionId, String message,
			Object... params) {
		Map statusMap = new LinkedHashMap();
		Map executionMap = new LinkedHashMap();
		Map msgMap = new LinkedHashMap();
		ArrayList resultList = new ArrayList();

		statusMap.put("status", statusCode);
		executionMap.put(methodName, sessionId);
		msgMap.put("message", message);

		if (params != null) {
			for (int i = 0; i < params.length; i += 2) {
				// Check if still possible to extract some messages
				if (i + 1 >= params.length)
					break;
				msgMap.put(params[i], params[i + 1]);
			}
		}

		resultList.add(0, statusMap);
		resultList.add(1, executionMap);
		resultList.add(2, msgMap);

		return resultList;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList NewGenerateResponseMessage(Object... params){
		Map statusMap = new LinkedHashMap();
		Map executionMap = new LinkedHashMap();
		ArrayList resultList = new ArrayList();		
		resultList.add(0, executionMap);
		if (params != null) {
			for (int i = 0; i < params.length; i += 2) {
				// Check if still possible to extract some messages
				if (i + 1 >= params.length)
					break;
				executionMap.put(params[i], params[i + 1]);
			}
		}
		return resultList;
	}

}
