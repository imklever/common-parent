package com.isoftstone.common.api.service.common;

public interface ExceptionLogService {

    void log(Throwable throwable);

    void log(Throwable throwable, Object context);
}
