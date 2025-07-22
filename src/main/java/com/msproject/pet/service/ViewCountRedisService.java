package com.msproject.pet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViewCountRedisService {

    private final RedisTemplate<String, String> redisTemplate;


    public Long getCachedViewCount(Long boardId) {
        String redisKey = "view:board:" + boardId;
        String cached = redisTemplate.opsForValue().get(redisKey);
        return cached == null ? 0L : Long.parseLong(cached);
    }
}
