package com.isoftstone.common.api.service.common.impl;

import com.isoftstone.common.api.service.cache.CacheService;
import com.isoftstone.common.api.service.cache.LocalCacheFactory;
import com.isoftstone.common.api.service.common.PermissionService;
import com.isoftstone.common.api.service.interceptor.PublicBusinessService;
import com.isoftstone.common.api.support.APIResult;
import com.isoftstone.common.common.sys.SysUserDto;
import com.isoftstone.common.util.JsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Service
public class PermissionServiceImpl implements PermissionService{
	
	@Value("${cache.type:javaCache}")
	String cacheType;
	
	@Autowired
	JsonService jsonService;

	@Autowired
	LocalCacheFactory localCacheFactory;
	@Autowired
	PublicBusinessService publicBusinessService;

	@Override
	public boolean hasPermission(HttpServletRequest request, HttpServletResponse response) throws Exception{
	
		boolean flag=true;
		//String requestUrlStr = request.getRequestURI();
		//if(requestUrlStr.contains(ApiMapperUrlConstants.VISUA_SQL))	{
			String businessCode = request.getParameter("businessCode");
			if(businessCode==null)
				return true;
			//登录后公共权限角色id=fb66f60ae0c511e88626fa163ec0ad5d
			if(publicBusinessService.checkBusinessCode(businessCode,"fb66f60ae0c511e88626fa163ec0ad5d"))return true;
			String token = request.getParameter("token");		
			if(StringUtils.isEmpty(token)){
				token = request.getHeader("token");
			}		
			if(StringUtils.isEmpty(token))
			{
				reponseResult(response);
				return false;
			}
				
			CacheService cacheService =localCacheFactory.getCacheService(cacheType);
			SysUserDto sysUserDto = (SysUserDto) cacheService.get("user", token);
			if(sysUserDto.getSysAdmin()==1) {
				return true;
			}
			//ConcurrentHashMap<String, Object> userPermission = cacheService.getByType("permission");
			if(cacheService.containsToken("permission", token)){				
				Set<String> visuaSqlExampleList = (Set<String>) cacheService.get("permission", token);;
		 		if(visuaSqlExampleList.contains(businessCode)){
		 			flag=true;
		 		}else {
		 			reponseResult(response);
		 			flag=false;
		 		}
			}
			else {
				reponseResult(response);
				flag=false;
			}		
		//}
		return flag;
	}
	
	private void reponseResult(HttpServletResponse response) throws IOException{
		APIResult apiResult =new APIResult(-3, "无权限访问此接口！");
		response.setContentType("application/json;charset=utf-8");  
		response.getWriter().print(jsonService.toJson(apiResult));
	}
	 
}
