package com.isoftstone.common.util;

public class ServiceException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8718765790953621650L;
	  public ErrorCode errorCode;

	    public ServiceException(String message, ErrorCode errorCode) {
	        super(message);
	        this.errorCode = errorCode;
	    }

	    public ServiceException(String message) {
	        super(message);
	        this.errorCode = ErrorCode.COMMON;
	    }

	    public ServiceException(ErrorCode errorCode) {
	        this.errorCode = errorCode;
	    }

}
