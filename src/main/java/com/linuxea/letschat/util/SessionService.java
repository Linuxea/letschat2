package com.linuxea.letschat.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SessionService {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public SessionService(@Qualifier("stringRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setSession2RouteKey(String sessionId, String routeKey) {
        redisTemplate.opsForValue().set(sessionId, routeKey);
        log.info("Set session: {} to routeKey: {}", sessionId, routeKey);
    }

    public String getRouteKeyBySession(String sessionId) {
        String routeKey = redisTemplate.opsForValue().get(sessionId);
        log.info("Get routeKey: {} by session: {}", routeKey, sessionId);
        return routeKey;
    }

    public void removeSession(String sessionId) {
        redisTemplate.delete(sessionId);
        log.info("Remove session: {}", sessionId);
    }


}
