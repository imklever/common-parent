package com.isoftstone.common.zuul.conf;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
@Component
public class XForwardedForFilter extends ZuulFilter {
	 private static final String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";
	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String remoteAddr = request.getRemoteAddr();
        String xff = request.getHeader("x-forwarded-for");
        if (xff != null) {
            int index = xff.indexOf(',');
            if (index != -1) {
                xff = xff.substring(0, index);
            }
            remoteAddr=xff.trim();
        }
        ctx.getZuulRequestHeaders().put(HTTP_X_FORWARDED_FOR, remoteAddr);
		return null;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public String filterType() {
		return  "pre";
	}

}
