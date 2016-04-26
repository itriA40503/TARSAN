package org.itri.ccma.tarsan.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The Class ListToJSON.
 */
public class ListToJSON {

	/** The array length. */
	private int arrayLength;

	/** The result list. */
	private List<?> resultList;

	/** The JSON array. */
	private JSONArray JSONArray;

	/**
	 * Instantiates a new list to json.
	 * 
	 * @param resultList
	 *            the result list
	 */
	public ListToJSON(List<?> resultList) {
		this.arrayLength = resultList.size();
		this.resultList = resultList;
		JSONArray = new JSONArray();
	}

	/**
	 * Gets the jSON format.
	 * 
	 * @return the jSON format
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("rawtypes")
	public JSONArray getJSONFormat() throws Exception {
		JSONArray sdArray = new JSONArray();
		for (int i = 0; i < arrayLength; i++) {
			if (i == 0) {
				JSONArray statusArray = new JSONArray();
				JSONObject JSONObj = new JSONObject();
				Map resultMap = new HashMap();
				resultMap = (Map) resultList.get(i);
				Iterator mapIterator = resultMap.entrySet().iterator();
				while (mapIterator.hasNext()) {
					Map.Entry pairs = (Map.Entry) mapIterator.next();
					JSONObj.put(pairs.getKey().toString(),
							(pairs.getValue() != null) ? pairs.getValue() : "");
				}
				statusArray.put(JSONObj);
				JSONArray.put(statusArray);
			} else {
				JSONObject JSONObj = new JSONObject();
				Map resultMap = new HashMap();
				resultMap = (Map) resultList.get(i);
				Iterator mapIterator = resultMap.entrySet().iterator();
				while (mapIterator.hasNext()) {
					Map.Entry pairs = (Map.Entry) mapIterator.next();
					JSONObj.put(pairs.getKey().toString(),
							(pairs.getValue() != null) ? pairs.getValue() : "");
				}
				sdArray.put(JSONObj);
			}
		}
		JSONArray.put(sdArray);
		return JSONArray;
	}
}
