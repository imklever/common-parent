package com.isoftstone.common.plugins.visua.vo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class VisuaContextVo implements Serializable, Map{
	private static final long serialVersionUID = -3553422299065421808L;
    ConcurrentHashMap  concurrentHashMap= new  ConcurrentHashMap<Object,Object>();;

	@Override
	public void clear() {
		concurrentHashMap.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return concurrentHashMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return concurrentHashMap.containsValue(value);
	}

	@Override
	public Set entrySet() {
		return concurrentHashMap.entrySet();
	}

	@Override
	public Object get(Object key) {
		return concurrentHashMap.get(key);
	}

	@Override
	public boolean isEmpty() {
		return concurrentHashMap.isEmpty();
	}

	@Override
	public Set keySet() {
		return concurrentHashMap.keySet();
	}

	@Override
	public Object put(Object key, Object value) {
		return concurrentHashMap.put(key, value);
	}

	@Override
	public void putAll(Map m) {
		concurrentHashMap.putAll(m);
	}

	@Override
	public Object remove(Object key) {
		return concurrentHashMap.remove(key);
	}

	@Override
	public int size() {
		return concurrentHashMap.size();
	}

	@Override
	public Collection values() {
		return concurrentHashMap.values();
	}

}
