package com.isoftstone.common.util.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.isoftstone.common.util.IdService;
@Service
public class IdServiceImpl implements IdService{

	@Override
	public String newOne() {
		return UUID.randomUUID().toString().replace("-", "");
	}

}
