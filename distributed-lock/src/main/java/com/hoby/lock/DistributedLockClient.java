package com.hoby.lock;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 分布式锁工厂
 */
@Component
@AllArgsConstructor
public class DistributedLockClient {

    private RedisTemplate<String, Object> redisTemplate;
    private String uuid;

    public DistributedLockClient() {
        this.uuid = UUID.randomUUID().toString();
    }

    public RedisDistributedLock getRedisLock(String lockKey) {
        return new RedisDistributedLock(redisTemplate, lockKey, uuid);
    }


}
