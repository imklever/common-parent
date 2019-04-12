package com.isoftstone.common.util;

public enum ErrorCode {
	
	COMMON(-1, 200);

    public int code;
    public int httpStatus;

    ErrorCode(int code, int httpStatus) {
    	this.code =code;
        this.httpStatus = httpStatus;
    }
}
