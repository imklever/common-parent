package com.isoftstone.common.backup.service;


import java.lang.reflect.ParameterizedType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.isoftstone.common.IDEntity;
import com.isoftstone.common.mapper.BaseMapper;
import com.isoftstone.common.util.IdService;
import com.isoftstone.common.util.JsonService;


public abstract class AbstractBaseService<T extends IDEntity, ID extends String>{
	@Autowired
	private JsonService jsonService;

    @Autowired
	private IdService idService;
	 
	protected abstract BaseMapper<T, ID> getMapper();
	
	public T save(String toJosn) {
		  final java.lang.reflect.Type type = ((ParameterizedType) this.getClass().getGenericSuperclass())
	                .getActualTypeArguments()[0];
	    T entity = null;
	    try {
	            entity = (T) jsonService.parseObject(toJosn, Class.forName(type.getTypeName()));
	     } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	     }
	    if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(idService.newOne());
            getMapper().insert(entity);
        }else{
        	T oldEntity=  getMapper().selectByPrimaryKey(entity.getId());
			 if(oldEntity!=null){
				 getMapper().updateByPrimaryKeySelective(entity);
			 }else{
				 getMapper().insert(entity);
			 }
        }
		return entity;
	}
	public T findById(ID id) {
		return getMapper().selectByPrimaryKey(id);
	}
	public T deleteById(ID id){
		T t=getMapper().selectByPrimaryKey(id);
		getMapper().deleteByPrimaryKey(id);
		return t;
	}
	
	public T updateById(T t){
		getMapper().updateByPrimaryKey(t);
		return t;
	}

}
