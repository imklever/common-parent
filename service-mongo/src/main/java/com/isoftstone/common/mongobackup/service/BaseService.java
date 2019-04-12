package com.isoftstone.common.mongobackup.service;

import com.isoftstone.common.IDEntity;

public interface BaseService <T extends IDEntity, ID extends String>{

	public T save(String toJosn);
	public T findById(ID id);
	public T deleteById(ID id);
	
	public T updateById(T t);
}
