package org.itri.ccma.tarsan.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;

public class HttpUtil {
	private final String USER_AGENT = "Mozilla/5.0";
	String[] CODE_HTML = { "%23", "%24", "%25", "%26", "%28", "%29", "%2F", "%3D", "%3F", "%5B", "%5D", "%7B", "%7D",
			"%5E", "%2A", "%2D", "%5F", "%2B", "%60", "%21", "%5C", "%22", "%27", "%3C", "%3E", "%40", "%3A", "%3B",
			"%2C", "%2E", "%7C", "%7E", "%7B", "%7D", "%20" };
	String[] CODE_ORIGINAL = { "#", "$", "%", "&", "(", ")", "/", "=", "?", "[", "]", "{", "}", "^", "*", "-", "_", "+",
			"`", "!", "\\", "\"", "'", "<", ">", "@", ":", ";", ",", ".", "|", "~", "{", "}", " " };

	private static Logger logger = Logger.getLogger("consoleAppender");
	/**
	 * static Singleton instance
	 */
	private static HttpUtil instance;

	/**
	 * Private constructor for singleton
	 */
	private HttpUtil() {
	}

	/**
	 * Static getter method for retrieving the singleton instance
	 */
	public static HttpUtil getInstance() {
		if (instance == null) {
			instance = new HttpUtil();
		}
		return instance;
	}

	// HTTP GET request
	public String sendGet(String url) throws Exception {
		// String url = "http://www.google.com/search?q=mkyong";
		
		// Encode some sign "{", "}", "\""
		url = url.replace("{", "%7B").replace("}", "%7D").replace("\"", "%22");
		logger.debug("#### Http Send Get URL : " + url);

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		if (Configurations.IS_DEBUG) {
			logger.debug("#### Http Send Get");
			logger.debug("\nSending 'GET' request to URL : " + url);
			logger.debug("Response Code : " + responseCode);
		}
		if (responseCode != 200)
			return null;

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		System.out.println(response.toString());
		return response.toString();
	}

	// HTTP POST request
	public String sendPost(String url, Object... params) throws Exception {
		// String url = "https://selfsolve.apple.com/wcResults.do";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		System.out.println(response.toString());
		return response.toString();
	}

	public String HTML_Encoding(String url) {
		return HTML_Transform(url, CODE_ORIGINAL, CODE_HTML);
	}

	public String HTML_Decoding(String url) {
		return HTML_Transform(url, CODE_HTML, CODE_ORIGINAL);
	}

	private String HTML_Transform(String url, String[] source, String[] target) {
		if (source.length != target.length) {
			System.err.println("CODE HTML and CODE ORIGINAL length is not equal");
			return url;
		}
		for (int i = 0; i < source.length; i++) {
			if (url.contains(source[i]))
				url = url.replace(source[i], target[i]);
		}
		return url;
	}

	public String getDomainName(String url) throws MalformedURLException {
		if (!url.startsWith("http") && !url.startsWith("https")) {
			url = "http://" + url;
		}
		URL netUrl = new URL(url);
		String host = netUrl.getHost();
//		if(host.startsWith("www")){
//			host = host.substring("www".length()+1);
//		}
		return host;
	}
}
