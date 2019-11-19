package com.andy.mybatis.cache;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Administrator
 */
@Slf4j
public class RedisCache implements Cache {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
//    private final ReadWriteLock readWriteLock = new DummyReadWriteLock();

    /**
     * cache instance id
      */
    private final String id;

    /**
     * 静态成员通过RedisCacheTransfer类注入RedisTemplate
     */
    private static RedisTemplate<String, Serializable> redisTemplate;
    public static void setRedisTemplate(RedisTemplate<String, Serializable> redisTemplate) {
        RedisCache.redisTemplate = redisTemplate;
    }

    private final HashOperations<String, String, Object> hashOpt;

    /**
     * 缓存刷新间隔，单位为毫秒
     * flushInterval 参数(自定义cache无法使用默认的flushInterval)
     */
    private long flushInterval = 0L;

    /**
     * @param id
     */
    public RedisCache(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
        hashOpt = redisTemplate.opsForHash();
    }

    @Override
    public String getId() {
        return id;
    }

    /**
     * Put query result to redis
     *
     * @param key
     * @param value
     */
    @Override
    @SuppressWarnings("unchecked")
    public void putObject(Object key, Object value) {
        try {
            hashOpt.put(getId(), key.toString(), value);
            // key存在但木有设置生存时间
            if( flushInterval > 0L && redisTemplate.getExpire(getId()) == -1L ) {
                redisTemplate.expire(getId(), flushInterval, TimeUnit.MILLISECONDS);
            }

            log.debug("Put query result to redis");
        }
        catch (Throwable t) {
            log.error("Redis put failed", t);
        }
    }

    /**
     * Get cached query result from redis
     *
     * @param key
     * @return
     */
    @Override
    public Object getObject(Object key) {
        try {
            log.debug("Get cached query result from redis");
            return hashOpt.get(getId(),key.toString());
        }
        catch (Throwable t) {
            log.error("Redis get failed, fail over to db", t);
            return null;
        }
    }

    /**
     * Remove cached query result from redis
     *
     * @param key
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object removeObject(Object key) {
        try {
            hashOpt.delete(getId(), key.toString());
            log.debug("Remove cached query result from redis");
        }
        catch (Throwable t) {
            log.error("Redis remove failed", t);
        }
        return null;
    }

    /**
     * Clears this cache instance
     */
    @Override
    public void clear() {
        redisTemplate.execute((RedisCallback<Object>) redisConnection-> {
            redisConnection.del(new byte[][]{getId().getBytes()});
            return null;
        });

        log.debug("Clear the cached id instance query result from redis");


////        RedisTemplate redisTemplate = getRedisTemplate();
//        redisTemplate.execute((RedisCallback) connection -> {
//            connection.flushDb();
//            return null;
//        });
//        logger.debug("Clear all the cached query result from redis");
    }

    /**
     * This method is not used
     *
     * @return
     */
    @Override
    public int getSize() {
        return hashOpt.size(getId()).intValue();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    /**
     * @param flushInterval
     */
    public void setFlushInterval(long flushInterval) {
        this.flushInterval = flushInterval;
    }
}
