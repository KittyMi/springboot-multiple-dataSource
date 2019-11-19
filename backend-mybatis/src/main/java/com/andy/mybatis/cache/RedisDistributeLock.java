package com.andy.mybatis.cache;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author ly
 * @date 2018/6/25 15:47
 * EX seconds — 以秒为单位设置 key 的过期时间；
 * PX milliseconds — 以毫秒为单位设置 key 的过期时间；
 * NX — 将key 的值设为value ，当且仅当key 不存在，等效于 SETNX。
 * XX — 将key 的值设为value ，当且仅当key 存在，等效于 SETEX。
 * <p>
 * 命令 SET authentication-name anystring NX EX max-lock-time 是一种在 Redis 中实现锁的简单方法
 */
@Slf4j
@Builder
public class RedisDistributeLock implements Lock {

    @Setter
    @Getter
    private String lockKey;

    @Setter
    @Getter
    @Builder.Default
    private int expireTime = 10;

    @Setter
    @Getter
    private RedisTemplate redisTemplate;

    private String lockVal;
    @Builder.Default
    private volatile boolean locked = false;


    /**
     * 毫秒转换为nanoTime
     */
    private static final long MSEC2NANO = 1000000;
    private static final String OK = "ok";
    /**
     * 解锁的lua脚本
     */
    public static final String UNLOCK_LUA;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }


    @Override
    public void lock() {
        lockVal = UuidUtil.genUUID();
        while (true) {
            String result = set(lockKey, lockVal, expireTime);
            if (OK.equalsIgnoreCase(result)) {
                locked = true;
            }
            sleepWrap(10, 50000);
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        lockVal = UuidUtil.genUUID();
        return OK.equalsIgnoreCase(set(lockKey, lockVal, expireTime));
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        long now = System.nanoTime();
        while ((System.nanoTime() - now) <  unit.toNanos(time)) {
            if (OK.equalsIgnoreCase(set(lockKey, lockVal, expireTime))) {
                locked = true;
                // 上锁成功结束
                return true;
            }
        }
        return false;
    }

    @Override
    public void unlock() {
        if (locked) {
            redisTemplate.execute((RedisCallback<Boolean>) connection -> {
                Object nativeConnection = connection.getNativeConnection();

                List<String> keys = new ArrayList<>();
                keys.add(lockKey);

                List<String> vals = new ArrayList<>();
                vals.add(lockVal);

                Long result = 0L;
                // cluster
                if (nativeConnection instanceof JedisCluster) {
                    result = (Long) ((JedisCluster) nativeConnection).eval(UNLOCK_LUA, keys, vals);
                }

                // single host
                if (nativeConnection instanceof Jedis) {
                    result = (Long) ((Jedis) nativeConnection).eval(UNLOCK_LUA, keys, vals);
                }

                if (result == 0) {
                    log.error("解锁失败:{}", System.currentTimeMillis());
                }

                return locked = result == 0;
            });
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }


    /**
     * 重写redisTemplate的set方法
     * @param key
     * @param value
     * @param msec
     * @return
     * @throws DataAccessException
     */
    private String set(final String key, final String value, final long msec) throws DataAccessException {
        if (StringUtils.isEmpty(key)) {
            log.error("key不能为空");
            throw new IllegalArgumentException();
        }
        return (String) redisTemplate.execute((RedisCallback<String>) connection -> {
            Object nativeConnection = connection.getNativeConnection();
            String result = null;
            if (nativeConnection instanceof JedisCommands) {
                result = ((JedisCommands) nativeConnection).set(key, value, "NX", "PX", msec);
            }
            if (!StringUtils.isEmpty(result)) {
                log.info("获取锁的时间:{}", System.currentTimeMillis());
            }
            return result;
        });
    }

    /**
     * sleep休眠
     * @param msec
     * @param nanos
     */
    private void sleepWrap(long msec, int nanos) {
        try {
            Thread.sleep(msec, nanos);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("获取分布式锁休眠被中断", e);
        }
    }
}
