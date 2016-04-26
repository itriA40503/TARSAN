package org.itri.ccma.tarsan.util;

@SuppressWarnings("unused")
public class Configurations {
	/* --- Development status variables --- */
	public static final boolean IS_DEBUG = true;

	private static final long SECOND = 1000;
	private static final long MINUTE = 60 * SECOND;
	private static final long HOUR = 60 * MINUTE;
	private static final long DAY = 24 * HOUR;
	private static final long WEEK = 7 * DAY;
	private static final long MONTH = 30 * DAY;
	private static final long YEAR = 12 * MONTH;

	/* Minimum time gap between two consecutive ads click */
	public static final long ADS_DURATION = 0 * MINUTE;
	/*
	 * Minimum time gap to identify two consecutives browsing behavior
	 * (identifying if it is still in the product search mode
	 */
	public static final long BROWSING_TIME_FRAME = 30;

	/*
	 * Status codes (adapted from HTTP Status Codes)
	 * https://en.wikipedia.org/wiki/List_of_HTTP_status_codes
	 */
	public static final String CODE_EXCEPTION = "000";
	public static final String CODE_OK = "200";
	public static final String CODE_ERROR = "400";
	public static final String CODE_UNAUTHORIZED = "401";
	public static final String CODE_NOT_EXIST = "402";
	public static final String CODE_FORBIDDEN = "403";
	public static final String CODE_NOT_ALLOWED = "405";
	public static final String CODE_UNKNOWN_ERR = "520";

	public String API_HOST;

	/**
	 * static Singleton instance
	 */
	private static Configurations instance;

	/**
	 * Private constructor for singleton
	 */
	private Configurations() {
		initApiHost();
	}

	/**
	 * Set API_HOST by looking up on the server.txt file in the resource folder
	 */
	private void initApiHost() {

	}

	/**
	 * Static getter method for retrieving the singleton instance
	 */
	public static Configurations getInstance() {
		if (instance == null) {
			instance = new Configurations();
		}
		return instance;
	}
}
