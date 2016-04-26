package org.itri.ccma.tarsan.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.httpclient.util.URIUtil;
import org.apache.log4j.Logger;
import org.itri.ccma.tarsan.util.Configurations;
import org.itri.ccma.tarsan.util.HttpUtil;
import org.itri.ccma.tarsan.util.SignatureUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.itri.ccma.tarsan.biz.BoUser;
import org.itri.ccma.tarsan.facade.impl.*;

/**
 * The Class UBike.
 */
@Path("/main")
public class Tarsan {

	private static final String SERVICE_CHAR = "service";
	private static final String JSON_PARA_CHAR = "jsonPara";
	/** The para json array. */
	private static JSONArray paraJSONArray;

	/** The logger. */
	private static Logger logger = Logger.getLogger("consoleAppender");

	/**
	 * Instantiates a new u bike.
	 */
	public Tarsan() {
	}

	/**
	 * Gets the service entry.
	 * 
	 * @param service
	 *            the service
	 * @param jsonPara
	 *            the jSON para
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @return the service entry
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings({ "unchecked" })
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes("application/JSON")
	public String getServiceEntry(@QueryParam(SERVICE_CHAR) String service, @QueryParam(JSON_PARA_CHAR) String jsonPara,
			@QueryParam("callback") String callbackString, @Context HttpServletRequest request,
			@Context HttpServletResponse response) throws Exception {

		response.setCharacterEncoding("UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		String uri = URIUtil.decode(request.getQueryString(), "UTF-8");

		// Transform URI into correct format
		// Encode everything after "jsonPara"
		String param = uri.substring(uri.indexOf(JSON_PARA_CHAR) + JSON_PARA_CHAR.length() + 1);
		param = HttpUtil.getInstance().HTML_Encoding(param);
		logger.info(uri);
		logger.info(JSON_PARA_CHAR);
		logger.info(param);
		uri = uri.substring(0, uri.indexOf(JSON_PARA_CHAR)) + JSON_PARA_CHAR + "=" + param;

		// Mapping the URI into service and json Parameter
		Map<String, String> map = getQueryMap(uri);

		// if (jsonPara == null)
		{
			service = map.get(SERVICE_CHAR);
			jsonPara = map.get(JSON_PARA_CHAR);
		}

		// if (Configurations.IS_DEBUG) {
		// logger.debug("@@@ service: " + service);
		// logger.debug("@@@ jsonPara: " + jsonPara);
		// }

		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

		String JSONParaII = jsonPara.replace("(", "{");
		JSONParaII = JSONParaII.replace(")", "}");
		// Normalize escape character
		JSONParaII = JSONParaII.replace("\\\"", "\"");
		JSONParaII = JSONParaII.replace("\\", "\\\\");
		JSONParaII = JSONParaII.replace(" ", "+");
		// Uncommented on Aug 13 04:10PM -- because of input &&& in the
		// parameter
		// JSONParaII = JSONParaII.replace("{", "%7B").replace("}", "%7D");
		// Normalize { } "
		// JSONParaII = JSONParaII.replaceAll("(\\%22|\\\"){2,}", "\"\"");
		JSONParaII = JSONParaII.replaceAll("(\\\"|\\%22)+(\\&)*(\\{)*(\\})*(\\\"|\\%22)+", "\"\"");
		// Normalize some error because of the URL input
		JSONParaII = JSONParaII.replaceAll("(\\%2522)+", "");

		logger.debug(JSONParaII);

		JSONArray JSONArray = new JSONArray(JSONParaII);

		// if (!service.equals("createUser") && !service.equals("login")) {
		// System.out.println("validating signature");
		// String tmpKey = null;
		// String timeStamp = null;
		// for (int i=0; i<JSONArray.length(); i++) {
		// String key =
		// JSONArray.getJSONObject(i).names().getString(0).toString();
		// if (key.equals("tmpKey"))
		// tmpKey = JSONArray.getJSONObject(i).getString("tmpKey").toString();
		// else if (key.equals("timeStamp"))
		// timeStamp =
		// JSONArray.getJSONObject(i).getString("timeStamp").toString();
		// }
		//
		// //pre-filter timeStamp smaller than current timeStamp
		// Long currentTimeStamp = System.currentTimeMillis()/1000L;
		// if (Long.parseLong(timeStamp) < currentTimeStamp) {
		// JSONArray result = new JSONArray();
		// result.put(new JSONObject("{\"status\":\"400\"}"));
		// result.put(new JSONObject("{\"getServiceEntry\":\"\"}"));
		// result.put(new JSONObject("{\"message\":\"Invalid or expired Time
		// Stamp\"}"));
		// return result.toString();
		// }
		// else {
		// String jpara = map.get("jsonPara");
		// String se = map.get("service");
		// StringBuffer sb = request.getRequestURL();
		// String firstHalf = sb + "?service=" + se + "&jsonPara=";
		//
		// Session sess = HibernateUtil.getSessionFactory().openSession();
		// Transaction tx = sess.beginTransaction();
		// Criteria crit = sess.createCriteria(Users.class);
		// crit.add(Restrictions.eq("tmpKey", tmpKey));
		// Users user = (Users)crit.uniqueResult();
		// tx.rollback();
		// sess.close();
		//
		// System.out.println("sb: " + sb);
		// System.out.println("firsthalf is: " + firstHalf);
		// System.out.println("jpara is: " + jpara);
		//
		// String server = firstHalf + jpara + user.getTmpPwd() + "GET";
		// System.out.println("string from server is: " + server);
		// String client = map.get("signature");
		// System.out.println("client md5 is: " + client);
		// System.out.println("server md5 is: " +
		// SignatureUtil.createMD5(server));
		// if (!SignatureUtil.createMD5(server).equals(client)) {
		// JSONArray result = new JSONArray();
		// result.put(new JSONObject("{\"status\":\"400\"}"));
		// result.put(new JSONObject("{\"getServiceEntry\":\"\"}"));
		// result.put(new JSONObject("{\"message\":\"Authentication
		// Failed\"}"));
		// return result.toString();
		// }
		// }
		// }

		// all web-portal APIs have prefix "web"
		String _service = service.replace("web", "");

		// get session ID
		HttpSession session = request.getSession(true);
		String sessionId = session.getId();
		JSONParaII = addSessionIdPara(sessionId, JSONArray, methodName);

		// task delegation
		List<String> imp = (List<String>) serviceImpl(_service, JSONParaII);
		// wrap as JSONArray
		JSONArray arrayList = new JSONArray(imp);

		if (service.contains("web") || service.contentEquals("sslLogin")) {
			System.out.println("THE CALLBACKSTRING IS: " + callbackString);
			return callbackString + "(" + arrayList.toString() + ")";
		}

		return arrayList.toString();
	}

	// programmatically add sessionId
	public String addSessionIdPara(String sessionId, JSONArray jarray, String method) {
		JSONArray resultArray = new JSONArray();

		try {
			String key = null;
			String value = null;

			resultArray.put(new JSONObject().put("sessionId", sessionId));

			for (int i = 0; i < jarray.length(); i++) {
				key = jarray.getJSONObject(i).names().getString(0).toString();
				value = jarray.getJSONObject(i).getString(key).toString();
				if (!key.equals("timeStamp"))
					resultArray.put(new JSONObject().put(key, value));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("[ERROR] methodName: " + method);
			logger.error("[ERROR] message: " + e.getMessage(), e);
		}

		String finalPara = resultArray.toString();
		return finalPara;
	}

	/**
	 * Post service entry.
	 * 
	 * @param service
	 *            the service
	 * @param jsonPara
	 *            the jSON para
	 * @param callbackString
	 *            the callback string
	 * @param request
	 *            the request
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings({ "unchecked" })
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes("application/JSON")
	public String postServiceEntry(@QueryParam("service") String service, @QueryParam("jsonPara") String jsonPara,
			@QueryParam("callback") String callbackString, @Context HttpServletRequest request) throws Exception {

		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

		JSONArray arrayList = new JSONArray();
		try {
			List<String> imp = (List<String>) serviceImpl(service, jsonPara);
			arrayList = new JSONArray(imp);
		} catch (Exception e) {
			logger.error("[ERROR] methodName: " + methodName);
			logger.error("[ERROR] message: " + e.getMessage(), e);
		}

		logger.debug("[RESULT]" + arrayList);
		return arrayList.toString();
	}

	/**
	 * Service impl.
	 * 
	 * @param service
	 *            the service
	 * @param JSONPara
	 *            the jSON para
	 * @return the object
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object serviceImpl(String service, String JSONPara) throws Exception {

		Class<?> cls = Class.forName("org.itri.ccma.tarsan.server.TarsanDispatcher");
		Object invoker = cls.newInstance();
		Method serviceList[] = cls.getDeclaredMethods();
		paraJSONArray = new JSONArray(JSONPara);
		List result = new ArrayList();

		System.out.println("service:" + service);

		// Scan all functions
		for (int i = 0; i < serviceList.length; i++) {
			Method method = serviceList[i];

			Class<?>[] pType = method.getParameterTypes();
			Class[] para = new Class[pType.length];
			if (method.getName().equals(service)) {
				int paraLength = pType.length;
				int paraJSONLength = paraJSONArray.length();
				if (paraLength != paraJSONLength) {
					System.out.println("paraLength:" + paraLength);
					System.out.println("paraJSONLength:" + paraJSONLength);
					throw new Exception("Number of Parameters Mismatch");
				}
				int maxRetries = 20;
				Object[] arrayObj = new Object[paraLength];
				for (int p = 0; p < paraJSONLength; p++) {
					int retry = 0;
					boolean ok = false;
					JSONObject eachJSONObj = new JSONObject(paraJSONArray.get(p).toString());
					Iterator JSONItr = eachJSONObj.keys();
					String paraName = JSONItr.next().toString();
					String paraValue = eachJSONObj.get(paraName).toString();
					String[] element = pType[p].toString().split("\\.");
					String paraType = element[element.length - 1];

					System.out.println("**********" + paraName + ": " + paraValue);
//					 if (Configurations.IS_DEBUG)
//					 logger.debug("\t" + paraType);
					while (retry < maxRetries && !ok) {
						try {
							if ("String".equals(paraType)) {
								para[p] = String.class;
								arrayObj[p] = paraValue;
								if (paraValue.isEmpty() || paraValue.equals("null"))
									arrayObj[p] = null;
							} else if ("Integer".equals(paraType)) {
								para[p] = Integer.class;
								arrayObj[p] = Integer.parseInt(paraValue);
							} else if ("Long".equals(paraType)) {
								para[p] = Long.class;
								arrayObj[p] = Long.parseLong(paraValue);
							} else if ("Double".equals(paraType)) {
								para[p] = Double.class;
								arrayObj[p] = Double.parseDouble(paraValue);
							} else if ("double".equals(paraType)) {
								para[p] = double.class;
								arrayObj[p] = Double.parseDouble(paraValue);
							} else if ("Float".equals(paraType)) {
								para[p] = Float.class;
								arrayObj[p] = Float.parseFloat(paraValue);
							} else if ("float".equals(paraType)) {
								para[p] = float.class;
								arrayObj[p] = Float.parseFloat(paraValue);
							} else if ("int".equals(paraType)) {
								para[p] = int.class;
								arrayObj[p] = Integer.parseInt(paraValue);
							} else if ("long".equals(paraType)) {
								para[p] = long.class;
								arrayObj[p] = Long.parseLong(paraValue);
							} else if ("boolean".equals(paraType)) {
								para[p] = boolean.class;
								arrayObj[p] = Boolean.parseBoolean(paraValue);
							}
							ok = true;
						} catch (Exception e) {
							if (Configurations.IS_DEBUG)
								logger.error("Conversion error: " + e.getMessage());
						}
						retry++;
					}
					if (retry >= maxRetries && !ok) {
						para[p] = Object.class;
						arrayObj[p] = paraValue;
					}
				}

//				 if (Configurations.IS_DEBUG) {
//				 logger.debug(method.getName());
//				 logger.debug(method);
//				 for (Object o : para)
//				 logger.debug(o);
//				 }
				Method invokeMethod = cls.getMethod(method.getName(), para);
				result = (List) invokeMethod.invoke(invoker, arrayObj);

				return result;
			}
		}
		result.add("No Such Service");

		return result;
	}

	/**
	 * Gets the query map.
	 * 
	 * @param query
	 *            the query
	 * @return the query map
	 */
	public static Map<String, String> getQueryMap(String query) {
		String[] params = query.split("&");
		Map<String, String> map = new HashMap<String, String>();
		for (String param : params) {
			String name = param.split("=")[0];
			String value = param.split("=")[1];
			value = HttpUtil.getInstance().HTML_Decoding(value);
			map.put(name, value);
		}
		return map;
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws Exception
	 *             the exception
	 */
	public static void main(String args[]) throws Exception {
	
//		newer_test newer = new newer_test();
//		newer.add_test("tarsan_test", "123456");
//		newer.list();
//		newer.update2(344, "try");
//		newer.user_delete(341);
//		newer.search("fresher");
//		newer.check("fresher");
//		newer.list();
//		UserFacade bouser = new UserFacade();
//		bouser.createUser("3", "accc", "paaaa");
//		
//		UserTest utest = new UserTest();
//		utest.createUser("test_it", utest.madehashkey("test",1));
//		String tmp=utest.madehashkey(utest.madehashkey("test",1),0);
		
//		logger.info(utest.getbarcode("3"));
//		logger.info(utest.getbarcode(utest.randombarcode()));
//		logger.info(utest.setBarcodeShop("benq"));
//		logger.info(utest.record_use_barcode("10","8", "3.555", "6.255"));

		
//		logger.info(utest.madehashkey("test",1));
//		logger.info(tmp);
//		logger.info(utest.check_key(tmp));

		

		
		
		// <-- Spring task schedule starts-->
		// ApplicationContext applicationContext = new
		// ClassPathXmlApplicationContext("/applicationContext.xml");
		// <-- Spring task schedule ends -->
		
		// BoUser bo = new BoUser();
		// BoEvent be = new BoEvent();
		// BoUsergroup bu = new BoUsergroup();
		// JSONArray arrayList = new JSONArray(bo.createUser("klsdfjf234230",
		// "TEST@hello.com", "0965235987", "TESTBARCODE", "1057", "password",
		// "null"));
		// JSONArray arrayList = new JSONArray(bo.getWinningList("werwkr234123",
		// "0.2", "EINV4201501170825", "10402", "1"));
		// JSONArray arrayList = new
		// JSONArray(bo.getWinningListDB("werwkr234123", "10406",
		// "b47e8bbd1bdc9d3690d888f631188f75"));
		// JSONArray arrayList = new
		// JSONArray(bo.getInvoiceHeader("werwkr234123", "QRCode", "TF63021595",
		// "20150109", "bf95a9f2a91cf3e631c4a472948000ad", "", ""));
		// JSONArray arrayList = new JSONArray(bo.getInvDetail("werwkr234123",
		// "QRCode", "MZ08511905", "10312", "20141126",
		// "10653009xFKSgLYIDPaLHjU3", "10653009", "0001", "EINV4201501170825",
		// "a13adf9c-3a97-408c-8750-caba49b800ae").toString());
		// JSONArray arrayList = new
		// JSONArray(bo.getCardInvHeader("werwkr234123",
		// "bc8eac1de60f3e3a25726496b61bc143", "3J0002", "/YBV7PGR", "20150201",
		// "20150330", "N", "EINV5000000000076",
		// "a13adf9c-3a97-408c-8750-caba49b800ae", "a19f"));
		//
		// JSONArray arrayList = new JSONArray(bo.queryMsbInvList("dsfsfwer",
		// "detail", "5294be1f689923d4d06927668c7803ff", "20120501",
		// "20151131"));
		// bo.getInvoiceHeaderWatchDog();
		// JSONArray arrayList = new JSONArray(bo.createMsbInvoice("", "QRCode",
		// "TQ12195858", "20141212",
		// "", "53102337LZoDtnwQJmXZa2ew", "53102337", "", "1478",
		// "35cdeb7d433ff6f001d12a13fa2d3a42"));
		// JSONArray arrayList = new JSONArray(bo.createMsbInvoice("", "QRCode",
		// "UN62223839", "20150317",
		// "", "m07wh/QAE3sub6/yySbhoQ==", "10653009", "", "0027",
		// "b7e5c1aad50f25374b3000df572c8174"));
		// String detail =
		// "[{\"amount\":\"30.00\",\"description\":\"CoffeeMilk\",\"quantity\":\"1\",\"unitPrice\":\"30\",\"rowNum\":\"1\"}]";
		// String detail = "[";
		// JSONArray arrayList = new JSONArray(bo.createMsbInvoice("", "Trad",
		// "QQ0001112", "20141126",
		// detail, "", "", "FamilyMart", "",
		// "e431927d7e85a15b231f42e92de0fba6"));
		// JSONArray arrayList = new JSONArray(bo.createMsbInvoice("", "QRCode",
		// "wf29731850", "20150513",
		// "0", "", "", "10653009", "", "4115",
		// "d96a1380e52d59f2d05c91909374f2a6"));
		// JSONArray arrayList = new JSONArray(bo.manageMsbUserInfo("qweqadsd",
		// "set", "", "", "1000", "59dc13eaee6e3f45ea54273ef9da8025"));
		// JSONArray arrayList = new JSONArray(bo.deleteMsbInvoice("testes",
		// "KB12850646", "2d9a0fe53ee8adaf55bd86a040a0853a"));
		// JSONArray arrayList = new JSONArray(be.getUserInventory("haha",
		// "46e253fe1adbb5a96239e6b4f655ad76"));
		// JSONArray arrayList = new JSONArray(be.getEventGroup("hehe",
		// "b47e8bbd1bdc9d3690d888f631188f75"));
		// JSONArray arrayList = new
		// JSONArray(be.chkEventGroupCompletion("sessionId", "1",
		// "b47e8bbd1bdc9d3690d888f631188f75"));
		// JSONArray arrayList = new JSONArray(be.getAccomplishment("sessionId",
		// "3cb32993dbe2e8d3ab3ffd2354419756"));
		// JSONArray arrayList = new JSONArray(bu.createUsergroup("", "Test
		// Personal Usergroup", true, true,
		// "f7301a41f2ca1f280520ffe4eedd1383"));
		// JSONArray arrayList = new JSONArray(bu.getUsergroup("",
		// "a0d47eac10b75dd40e31483a503ed067"));
		// JSONArray arrayList = new JSONArray(bu.getUsergroupMember("", "9",
		// "46e253fe1adbb5a96239e6b4f655ad76"));
		// JSONArray arrayList = new JSONArray(bu.addToUsergroup("", "84", "3",
		// "5a49d94a21ab50c73ef568db882ec4b1"));
		// JSONArray arrayList = new JSONArray(bu.removeFromUsergroup("", "10",
		// true, "6,7", "b47e8bbd1bdc9d3690d888f631188f75"));
		// JSONArray arrayList = new JSONArray(bu.ownerManageUsergroup("",
		// "update", "af178dca2c33592ff157266b0f57512d", "$",
		// true, "b47e8bbd1bdc9d3690d888f631188f75"));
		// JSONArray arrayList = new JSONArray(bu.manageFriendList("", "create",
		// "/PZ82OV2", "3rdTestFriend", "null",
		// "f7301a41f2ca1f280520ffe4eedd1383"));
		// String itemString = "[{\"sourceUsergroupId\":\"9\",
		// \"to\":[{\"targetUsergroupId\":\"10\",
		// \"item\":[{\"itemQRCode\":\"2faaf17d8f0c068e0dac86d7991341fe7a466281ba0d6e200a26b5d450cfe821d4973fe17ffbbacfce7bbdd306f8d7a9ec5ab8fa217746f1b6b72c5b3bce5656\",\"itemQuantity\":\"3\"},{\"itemQRCode\":\"94808b16bfcd035774419b670a8ef254fb064a0d24a02b1a1bf10cdda8aa62745e019e7724ddd9217e3024def57f4172d00c67e5ff900de19373f21a40d0ecbb\",\"itemQuantity\":\"1\"}]}]}]";
		// JSONArray arrayList = new JSONArray(bu.moveReservedItem("",
		// itemString, "b47e8bbd1bdc9d3690d888f631188f75"));
		// JSONArray arrayList = new JSONArray(bu.leaveUsergroup("",
		// "af178dca2c33592ff157266b0f57512d",
		// "fa95eaaa4f50ee3814413ce8e5ae8fa5"));
		// JSONArray arrayList = new JSONArray(bu.createReservedItem("",
		// "10653009", "testItem2", "TestBarcode2",
		// "6", "17087e26d4ece49927cf37f13efdd16a", "14", "null",
		// "f7301a41f2ca1f280520ffe4eedd1383"));
		// JSONArray arrayList = new JSONArray(bu.getReservedItem("", "all",
		// "100", "b47e8bbd1bdc9d3690d888f631188f75"));
		// bo.syncAllCardInvWatchDog();
		// bo.createCInvH();
		// be.addPoint("hashCode", "barcode", 13, 6, 7, 14,
		// "b47e8bbd1bdc9d3690d888f631188f75");
		// bo.syncAllInvWatchDog();
		// bo.getWinningListWatchDog();
		// bo.checkWinningWatchDog();
		// bo.validateInvDate("20150299");
		// be.chkEvtItemMatchForPt(14);
		// be.resetPoint("dde8e07aaf121806eb28fa596f549610");
		// System.out.println("date is: " +
		// arrayList.getJSONObject(0).getJSONObject("timeStamp").getString("date"));
		// System.out.println("firstPrizeAmt is: " +
		// arrayList.getJSONObject(0).getString("firstPrizeAmt"));
		// System.out.println("result is: " + arrayList.toString());
		// System.out.println("status is: " +
		// arrayList.getJSONObject(0).getString("status"));
		// UBike testService = new UBike();
		// String JSONPara = "%5B(sessionId%3AABCDE)%5D";
		// System.out.println(testService.getServiceEntry("getMapInfo",
		// JSONPara, null, null, null));
		// String JSONPara =
		// "[(page:-1),(rp:-1),(sortname:-1),(sortorder:-1),(query:SERVICE),(qtype:message)]";
		// System.out.println(testService.getServiceEntry("queryLogs", JSONPara,
		// "none", null, null) + "end of test.\n");
		// String JSONPara =
		// "[{'IP':'11.11.11.11'},{'CPU':'3.2'},{'ram':'11'},{'isClosed':'true'}]";
		// System.out.println("The invoke result is " + serviceImpl("testIV",
		// JSONPara));
		// String baseUrl = "http://140.96.29.89:8080/UBike/service/main";
		// String test = UriBuilder.fromPath(baseUrl).queryParam("service",
		// "getMapInfo")
		// .queryParam("jsonPara",
		// "[(sessionId:ABCDE)]").build().toURL().toString();
		// System.out.println(test);
		// String baseUrl = "http://140.96.29.89:8080/UBike/service/main";
		// String test = UriBuilder.fromPath(baseUrl).queryParam("service",
		// "getMapInfo")
		// .queryParam("jsonPara",
		// "[(sessionId:ABCDE)]").build().toURL().toString();
		// System.out.println(test);
		// HttpClient client = new HttpClient();
		// GetMethod method = new GetMethod(test);
		// method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new
		// DefaultHttpMethodRetryHandler(3, false));
		// try {
		// // Execute the method.
		// int statusCode = client.executeMethod(method);
		//
		// if (statusCode != HttpStatus.SC_OK) {
		// System.err.println("Method failed: " + method.getStatusLine());
		// }
		//
		// // Read the response body.
		// byte[] responseBody = method.getResponseBody();
		//
		// // Deal with the response.
		// // Use caution: ensure correct character encoding and is not binary
		// // data
		// System.out.println(new String(responseBody));
		//
		// } catch (HttpException e) {
		// System.err.println("Fatal protocol violation: " + e.getMessage());
		// e.printStackTrace();
		// } catch (IOException e) {
		// System.err.println("Fatal transport error: " + e.getMessage());
		// e.printStackTrace();
		// } finally {
		// // Release the connection.
		// method.releaseConnection();
		// }
	}
}