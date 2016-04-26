<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.Date"%>
<%@ page import="org.itri.ccma.tarsan.util.HttpUtil"%>
<%
	boolean isDebug = false;
	Long ads_id = null;
	Long ad2user_id = null;
	String user_id = null;
	Long timestamp = new Date().getTime();
	String identifier = null;
	if (request.getParameter("a") != null) {
		try {
			ads_id = Long.parseLong(request.getParameter("a"));
		} catch (Exception e) {
			ads_id = -1L;
		}
	}
	if (request.getParameter("u") != null) {
		try {
			ad2user_id = Long.parseLong(request.getParameter("u"));
		} catch (Exception e) {
			ad2user_id = -1L;
		}
	}
	if (request.getParameter("x") != null) {
		user_id = request.getParameter("x");
	}
	if (request.getParameter("i") != null) {
		identifier = request.getParameter("i");
	}
%>
<%
	String errorMessage = null;
	String url = null;
	try {
		String protocol = "http";
		String host = "140.96.29.153"; // 140.96.29.153 // 240 (localhost)
		int port = 8080;
		String format = "%s://%s:%d/TARSAN/service/main?service=touch&jsonPara=[(\"adsId\":\"%d\"),(\"ad2userId\":\"%d\"),(\"tempkey\":\"%s\"),(\"timestamp\":\"%d\"),(\"identifier\":\"%s\")]";
		url = String.format(format, protocol, host, port, ads_id, ad2user_id, user_id, timestamp, identifier);
		HttpUtil.getInstance().sendGet(url);
	} catch (Exception e) {
		errorMessage = e.getMessage();
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>ITRI Ads Testing</title>
<link rel="stylesheet" href="itri.css" type="text/css" />
</head>
<body style="text-align: center;">

	<div class="itribanner">
		<div class="textbox">
			<p align="left">Thank you for your feedback!</p>
			<%
				if (isDebug) {
			%>
			<p><%=url%></p>
			<p><%=errorMessage%></p>
			<%
				}
			%>
		</div>
	</div>
</body>
</html>