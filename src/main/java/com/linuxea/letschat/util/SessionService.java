package com.linuxea.letschat.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@Order(2)
public class SessionService {

    private final RedisTemplate<String, String> redisTemplate;
    private final String sessionKey = "session";

    @Autowired
    public SessionService(@Qualifier("stringRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setSession2RouteKey(String sessionId, String routeKey) {
        redisTemplate.opsForHash().put(sessionKey, sessionId, routeKey);
        log.info("Set session: {} to routeKey: {}", sessionId, routeKey);
    }

    public String getRouteKeyBySession(String sessionId) {
        Object object = redisTemplate.opsForHash().get(sessionKey, sessionId);
        if (object == null) {
            return null;
        }
        String routeKey = object.toString();
        log.info("Get routeKey: {} by session: {}", routeKey, sessionId);
        return routeKey;
    }

    public void removeSession(String sessionId) {
        Long delete = redisTemplate.opsForHash().delete(sessionKey, sessionId);
        log.info("Delete session: {} from sessionKey: {} , result : {}", sessionId, sessionKey, delete);
    }

    public List<String> getOnlineFriends() {
        Set<Object> keys = redisTemplate.opsForHash().keys(sessionKey);
        return keys.stream().map(Object::toString).toList();
    }


}
