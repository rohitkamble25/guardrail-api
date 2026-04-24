package com.example.guardrail_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Long increment(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    public Long incrementBy(String key, long value) {
        return redisTemplate.opsForValue().increment(key, value);
    }

    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void setWithTTL(String key, Object value, long minutes) {
        redisTemplate.opsForValue().set(key, value, minutes, TimeUnit.MINUTES);
    }

    public void pushToList(String key, String value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    public Long getListSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }
}