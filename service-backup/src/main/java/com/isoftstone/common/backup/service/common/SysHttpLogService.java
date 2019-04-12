package com.isoftstone.common.backup.service.common;

import com.isoftstone.common.backup.service.BaseService;
import com.isoftstone.common.common.SysHttpLogDto;

public interface SysHttpLogService  extends BaseService<SysHttpLogDto, String>{
   int insert(SysHttpLogDto sysHttpLogDto);
}
