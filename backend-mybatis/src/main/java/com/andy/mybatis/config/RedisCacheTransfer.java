package com.andy.mybatis.config;

import com.andy.mybatis.cache.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author andy_lai
 * @version 1.0
 * @date 2019/11/19 16:02
 * @desc 静态注入函数
 */
@Component
public class RedisCacheTransfer {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Serializable> redisTemplate) {
        RedisCache.setRedisTemplate(redisTemplate);
    }
}
