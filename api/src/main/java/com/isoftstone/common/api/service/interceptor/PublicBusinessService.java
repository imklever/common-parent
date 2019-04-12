package com.isoftstone.common.api.service.interceptor;

public interface PublicBusinessService {

	boolean checkBusinessCode(String businessCode);

	boolean checkBusinessCode(String businessCode,String role_id);

}
