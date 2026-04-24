package com.example.guardrail_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class NotificationScheduler {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisService redisService;

    @Scheduled(fixedRate = 300000)
    public void processNotifications() {

        Set<String> keys = redisTemplate.keys("user:*:pending_notifs");

        if (keys == null)
            return;

        for (String key : keys) {

            Long size = redisService.getListSize(key);

            if (size != null && size > 0) {
                System.out.println("Summarized Push Notification: " + size + " interactions");
                redisService.deleteKey(key);
            }
        }
    }
}