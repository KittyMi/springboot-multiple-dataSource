package com.andy.mybatis.cache;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.decorators.LoggingCache;

/**
 * @author andy_lai
 * @date 2018/2/7 17:40
 * @desc redis cache decorators
 */
@Slf4j
public class LoggingRedisCache extends LoggingCache {

    /**
     * 构造函数，二级缓存必须提供id的构造函数
     * @param id
     */
    public LoggingRedisCache(String id) {
        super(new RedisCache(id));
    }

}
