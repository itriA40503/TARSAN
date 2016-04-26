<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*"%>
<%@ page import="org.itri.ccma.tarsan.util.*"%>
<% 
	String keyword = null;
	String domain = null;
	Long ads_id = null;
	Long ad2user_id = null;
	String user_id = null;
	String identifier = request.getRemoteAddr();	// or request.getRemoteHost()
	if (request.getParameter("kw") != null) {
		keyword = request.getParameter("kw");
		keyword = HttpUtil.getInstance().HTML_Decoding(keyword);
	}
	if (request.getParameter("domain") != null) {
		domain = request.getParameter("domain");
	}
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
%>
<%
	if (keyword != null && domain != null) {
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
			<p align="left">
				Please help to test our product and answer the question. <br />Have
				you attempt product search with keyword <b> <%=keyword%>
				</b> at <i> <%=domain%></i>? <br />
			</p>
			<form name="form1" id="form1" action="result.jsp" method="get">
				<input type="submit" value="No, I haven`t" /> <input type="hidden"
					name="kw" value="<%=keyword%>" /> <input type="hidden"
					name="domain" value="<%=domain%>" /> <input type="hidden" name="a"
					value="<%=ads_id%>" /><input type="hidden" name="u"
					value="<%=ad2user_id%>" /><input type="hidden" name="x"
					value="<%=user_id%>" /><input type="hidden" name="i"
					value="<%=identifier%>" />
			</form>
		</div>
	</div>
</body>
</html>
<%
	}
%>