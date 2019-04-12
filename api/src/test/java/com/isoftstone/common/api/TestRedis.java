package com.isoftstone.common.api;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedis {
/*	@Autowired
	private RedisTemplate redisTemplate;*/
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Test
	public void s() {
		stringRedisTemplate.opsForValue().set("test2","testValue2",60, TimeUnit.SECONDS);
	}

}
