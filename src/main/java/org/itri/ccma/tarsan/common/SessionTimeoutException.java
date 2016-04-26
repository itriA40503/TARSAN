package org.itri.ccma.tarsan.common;

public class SessionTimeoutException extends Exception
{
	private static final long serialVersionUID = 4981524452812876905L;

	public SessionTimeoutException() {
	}

	public SessionTimeoutException(String message) {
		super(message);
	}

	public SessionTimeoutException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
