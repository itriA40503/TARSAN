package org.itri.ccma.tarsan.common;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * The Class AES.
 */
public class AES
{

	/**
	 * Instantiates a new aES.
	 */
	public AES() {

	}

	/**
	 * Encrypt.
	 * 
	 * @param seed
	 *            the seed
	 * @param cleartext
	 *            the cleartext
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
//	public static String encrypt(String seed, String cleartext) throws Exception {
//		byte[] rawKey = getRawKey(seed.getBytes());
//		byte[] result = encrypt(rawKey, cleartext.getBytes());
//		return toHex(result);
//	}
	
	public static byte[] encrypt(String seed, String cleartext) throws Exception {
		byte[] rawKey = getRawKey(seed.getBytes("UTF-8"));
		byte[] result = encrypt(rawKey, cleartext.getBytes("UTF-8"));
		byte[] encode64 = Base64.encodeBase64(result);
		return encode64;
	}

	/**
	 * Decrypt.
	 * 
	 * @param seed
	 *            the seed
	 * @param encrypted
	 *            the encrypted
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
//	public static String decrypt(String seed, String encrypted) throws Exception {
//		byte[] rawKey = getRawKey(seed.getBytes());
//		byte[] enc = toByte(encrypted);
//		byte[] result = decrypt(rawKey, enc);
//		return new String(result);
//	}
	
	public static String decrypt(String seed, String encrypted) throws Exception {
		byte[] rawKey = getRawKey(seed.getBytes());
		byte[] enc = Base64.decodeBase64(encrypted.getBytes());
		byte[] result = decrypt(rawKey, enc);
		return new String(result);
	}

	/**
	 * Gets the raw key.
	 * 
	 * @param seed
	 *            the seed
	 * @return the raw key
	 * @throws Exception
	 *             the exception
	 */
	private static byte[] getRawKey(byte[] seed) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.setSeed(seed);
		kgen.init(128, sr); // 192 and 256 bits may not be available
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();
		return raw;
	}

	/**
	 * Encrypt.
	 * 
	 * @param raw
	 *            the raw
	 * @param clear
	 *            the clear
	 * @return the byte[]
	 * @throws Exception
	 *             the exception
	 */
	private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);	
		
		//*********
//		AlgorithmParameters params = cipher.getParameters();
//		byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
//		System.out.println("the iv is: " + iv.toString());
		//*********
		
		byte[] encrypted = cipher.doFinal(clear);
		return encrypted;
	}

	/**
	 * Decrypt.
	 * 
	 * @param raw
	 *            the raw
	 * @param encrypted
	 *            the encrypted
	 * @return the byte[]
	 * @throws Exception
	 *             the exception
	 */
	private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}

	/**
	 * To hex.
	 * 
	 * @param txt
	 *            the txt
	 * @return the string
	 */
	public static String toHex(String txt) {
		return toHex(txt.getBytes());
	}

	/**
	 * From hex.
	 * 
	 * @param hex
	 *            the hex
	 * @return the string
	 */
	public static String fromHex(String hex) {
		return new String(toByte(hex));
	}

	/**
	 * To byte.
	 * 
	 * @param hexString
	 *            the hex string
	 * @return the byte[]
	 */
	public static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++) {
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
		}
		return result;
	}

	/**
	 * To hex.
	 * 
	 * @param buf
	 *            the buf
	 * @return the string
	 */
	public static String toHex(byte[] buf) {
		if (buf == null) {
			return "";
		}
		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
		}
		return result.toString();
	}

	/** The Constant HEX. */
	private final static String HEX = "0123456789ABCDEF";

	/**
	 * Append hex.
	 * 
	 * @param sb
	 *            the sb
	 * @param b
	 *            the b
	 */
	private static void appendHex(StringBuffer sb, byte b) {
		sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws Exception
	 *             the exception
	 */
	public static void main(String[] args) throws Exception {
		 String seed = "abcde";
		// System.out.println(AES.toHex(AES.getRawKey(seed.getBytes())));
		// System.out.println(bytes);
		// String s = new String(bytes);
		// System.out.println(s);
		// String clearText = "account: 123456, password: ccma_icl";
		 String clearText =
		 "{\"account\":\"123456\",\"password\":\"ccma_icl\"}";
		// String encrypted = AES.encrypt(seed, clearText);
		 //System.out.println(encrypted);
		// String decrypted = AES.decrypt("abcde", encrypted);
		// System.out.println(decrypted);
		// JSONObject JSONObj = new JSONObject(decrypted);
		// System.out.println(JSONObj.get("account"));
		// System.out.println(JSONObj.get("password"));

		// String clearText = "account: 800471, password: kmsp9xng7";
		// String clearText =
		// "{\"account\":\"\",\"password\":\"\"}";
		// String encrypted = AES.encrypt(seed, clearText);
		// System.out.println(encrypted);
	}
}
