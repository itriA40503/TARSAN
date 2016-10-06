package org.itri.ccma.tarsan.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
	
	public static boolean validator(String type, String value) {
		
		String regex = null;
		Pattern pattern = null;
		Matcher matcher = null;
		
		if (value.equals("null")) {
			return true;
		}
		else if (!value.equals("null") || value.length() > 0 ) {
			
			if (type.equals("email"))
				regex = "^(.+)@(.+)$";
			else if(type.equals("ipv4"))
				regex = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
			else if (type.equals("phone"))
				regex ="^\\(?([0-9]{4})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{3})$";
			else if (type.equals("password"))
				regex = "([a-zA-Z0-9\\?\\!\\@\\_\\$\\~\\%\\.]{3,128})";
			else if (type.equals("pin"))
				regex = "([a-zA-Z0-9]{4})";
			else if (type.equals("invNum"))
				regex = "^\\(?([a-zA-Z]{2})\\)?([0-9]{8})$";
			else if (type.equals("invDate"))
				regex = "([0-9]{8})";
			else if (type.equals("digit"))
				regex = "([-.0-9]+)";
			else if (type.equals("number"))
				regex = "([0-9]+)";
			else if (type.equals("number_list"))
				regex = "([\\,0-9]+)";
			else if (type.equals("general"))
				regex = "[\\'\\@\\*\\_\\-\\+\\d\\p{L}\\p{No}\\p{Space}]+";
			else if (type.equals("barcode"))
				regex ="^\\(?([\\/]{1})\\)?([0-9a-zA-Z]{7})$";
			else if (type.equals("normal"))
				regex = "([a-zA-Z0-9]+)";
			
			pattern = Pattern.compile(regex);
			matcher = pattern.matcher(value);
		}
		else if (!value.equals("null") || value.length() == 0 ) {
			return false;
		}

		//the general mode contains blacklisted characters
//		if (type.equals("general") && matcher.matches())
//			return false;
//		else if (type.equals("general") && !matcher.matches())
//			return true;
//		else
			return matcher.matches();
	}
}
