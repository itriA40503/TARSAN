package org.itri.ccma.tarsan.common;

/**
 * The Class Language.
 */
public class Language
{

	/** Error Codes. */
	public static final Integer SUCCESS = 200;
	public static final Integer ERROR = 400;
	public static final Integer INVALID_USER = 401;
	public static final Integer SESSION_TIME_OUT = 402;

	/** The Constant WILDCARD. */
	public static final String WILDCARD = "-1";

	/** The Constant WILDCARD_INTEGER. */
	public static final int WILDCARD_INTEGER = -1;

	/** The Constant WILDCARD_LONG. */
	public static final Long WILDCARD_LONG = -1L;

	/** The Constant NULL_STRING. */
	public static final String NULL_STRING = "";

	// LDAP Authentication
	/** The Constant SEED. */
	//public static final String SEED = "abcde";
	public static final String SEED = "regpin";

	/** The Constant TEST_ACCOUNT. */
	public static final String TEST_ACCOUNT = "123456";

	/** The Constant TEST_PASSWORD. */
	public static final String TEST_PASSWORD = "ccma_icl";
	
	/** The Constant TEST_EMAIL. */
	public static final String TEST_EMAIL = "ccma@itri.org.tw";
	
//	/** The Constant TEST_ACCOUNT. */
//	public static final String TEST_ACCOUNT = "test";
//
//	/** The Constant TEST_PASSWORD. */
//	public static final String TEST_PASSWORD = "password";
	
	public static final String BASE_URL_INV = "https://www.einvoice.nat.gov.tw/PB2CAPIVAN/invapp/InvApp";
	
	public static final String BASE_URL_CARD = "https://www.einvoice.nat.gov.tw/PB2CAPIVAN/invServ/InvServ";
	
	public static final String BASE_URL_LOVE = "https://www.einvoice.nat.gov.tw/PB2CAPIVAN/loveCodeapp/qryLoveCode";
	
	public static final String BASE_URL_DONATE = "https://www.einvoice.nat.gov.tw/PB2CAPIVAN/CarInv/Donate";
	
	public static final int NUM_OF_RETRY = 10;
	
	public static final int TIMEOUT = 10000;
}
