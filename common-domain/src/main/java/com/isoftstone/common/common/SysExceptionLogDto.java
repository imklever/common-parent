package com.isoftstone.common.common;

import com.isoftstone.common.IDEntity;

public class SysExceptionLogDto extends IDEntity { 

    /**
	 * 
	 */
	private static final long serialVersionUID = -1614368786827828069L;

	private String context;

    private String content;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context == null ? null : context.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}