package org.itri.ccma.tarsan.biz.exceptions;

public class LogicException extends Exception {
	private String errorCode;
	private Object[] params;

	/**
	 * 
	 */
	private static final long serialVersionUID = -6294689594006251945L;
	
	public LogicException(String message) {
        super(message);
    }
	
	public LogicException(Throwable cause) {
        super(cause);
    }
	
	public LogicException(String message, Throwable cause) {
        super(message, cause);
    }
	
	public LogicException(String message, String errorCode, Object... params)
	{
		super(message);
		this.errorCode = errorCode;
		this.params = params;
	}
	
	public String getMessage()
    {
		StringBuilder sb = new StringBuilder();
		sb.append(super.getMessage());
//		sb.append('[').append(this.getErrorCode()).append(']');
		return sb.toString();
    }
	
	public String getErrorCode()
	{
		return this.errorCode;
	}
	
	public Object[] getParams()
	{
		return this.params;
	}
}
