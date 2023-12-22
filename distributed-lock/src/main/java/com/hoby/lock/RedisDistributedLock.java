package com.hoby.lock;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 使用redis实现的可重入分布式锁
 */
public class RedisDistributedLock implements Lock {

    private RedisTemplate<String, Object> redisTemplate;
    private String lockKey;
    private String uuid;
    private long expireTime = 30;

    public RedisDistributedLock(RedisTemplate<String, Object> redisTemplate, String lockKey, String uuid) {
        this.redisTemplate = redisTemplate;
        this.lockKey = lockKey;
        this.uuid = uuid + ":" + Thread.currentThread().getId();
    }

    @Override
    public void lock() {
        this.tryLock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        try {
            return this.tryLock(-1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 加锁方法
     *
     * @param time the maximum time to wait for the lock
     * @param unit the time unit of the {@code time} argument
     * @return
     * @throws InterruptedException
     */
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        if (time != -1) {
            this.expireTime = unit.toSeconds(time);
        }
        String script = "if redis.call('exists', KEYS[1]) == 0 or redis.call('hexists', KEYS[1], ARGV[1]) == 1" +
                "then " +
                "   redis.call('hincrby', KEYS[1], ARGV[1], 1) " +
                "   redis.call('expire', KEYS[1], ARGV[2]) " +
                "   return 1" +
                "else " +
                "   return 0 " +
                "end";

        while (!redisTemplate.execute(new DefaultRedisScript<>(script, Boolean.class), Arrays.asList(lockKey), uuid, expireTime)) {
            Thread.sleep(50);
        }
        // 加锁成功，返回之前，开启定时器自动续期
        this.renewExpire();
        return true;
    }

    /**
     * 解锁方法
     */
    @Override
    public void unlock() {
        String script = "if redis.call('hexists', KEYS[1], ARGV[1]) == 0" +
                "then" +
                "    return nil" +
                "elseif redis.call('hincrby', KEYS[1], ARGV[1], -1) == 0" +
                "then" +
                "    return redis.call('del', KEYS[1])" +
                "else " +
                "    return 0" +
                "end";
        Long result = redisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Arrays.asList(lockKey), uuid);
        if (result == null) {
            throw new IllegalMonitorStateException("this lock doesn't belong to you");
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    /**
     * 自动续期过期时间
     */
    private void renewExpire() {
        String script = "if redis.call('hexists', KEYS[1], ARGV[1]) == 0" +
                "then" +
                "    return redis.call('expire', KEYS[1], ARGV[2])" +
                "else" +
                "    return 0";
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (redisTemplate.execute(new DefaultRedisScript<>(script, Boolean.class), Arrays.asList(lockKey), uuid, expireTime)) {
                    renewExpire();
                }
            }
        }, expireTime * 1000 / 3);
    }
}
