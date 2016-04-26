package org.itri.ccma.tarsan.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.log4j.Logger;

public class SignatureUtil {
		
	private static Logger logger = Logger.getLogger("consoleAppender");
	
	public static String createHash(String algorithm, String original) {
		String encode = original;
		MessageDigest md = null;
		
		try {
			if (algorithm.equals("md5"))
				md = MessageDigest.getInstance("MD5");
			else if (algorithm.equals("sha512"))
				md = MessageDigest.getInstance("SHA-512");
			
			md.update(encode.getBytes());
			
			byte byteData[] = md.digest();
			
			StringBuffer sbuffer = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				 sbuffer.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}				
			
			encode = sbuffer.toString();	
			//System.out.println("MD5: " + encode);
			
		} catch (NoSuchAlgorithmException e) {
			logger.error("[ERROR] message: " + e.getMessage(), e);
			e.printStackTrace();
		}
				
		return encode;
	}
}
