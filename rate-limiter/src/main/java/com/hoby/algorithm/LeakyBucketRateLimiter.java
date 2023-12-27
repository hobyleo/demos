package com.hoby.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限流算法之<i>漏桶</i>：
 * <p>a.一个固定容量的漏桶，按照固定速率出水（处理请求）；</p>
 * <p>b.当流入水（请求数量）的速度过大会直接溢出（请求数量超过限制则直接拒绝）。</p>
 * <p>c.桶里的水（请求）不够则无法出水（桶内没有请求则不处理）。</p>
 *
 * @author hoby
 * @since 2023-12-27
 */
public class LeakyBucketRateLimiter {

    Logger logger = LoggerFactory.getLogger(LeakyBucketRateLimiter.class);
    // 桶的容量
    int capacity;
    // 桶中现存水量
    AtomicInteger water = new AtomicInteger();
    // 开始漏水时间
    long leakTimestamp;
    // 水流出的速率，即每秒允许通过的请求数
    int leakRate;

    public LeakyBucketRateLimiter(int capacity, int leakRate) {
        this.capacity = capacity;
        this.leakRate = leakRate;
    }

    public synchronized boolean tryAcquire() {
        // 桶中没有水，重新开始计算
        if (water.get() == 0) {
            logger.info("start leaking");
            leakTimestamp = System.currentTimeMillis();
            water.incrementAndGet();
            return water.get() < capacity;
        }
        // 先漏水，计算剩余水量
        long currentTime = System.currentTimeMillis();
        int leakedWater = (int) ((currentTime - leakTimestamp) / 1000 * leakRate);
        logger.info("leakTime:{}, currentTime:{}. leakedWater:{}", leakTimestamp, currentTime, leakedWater);
        // 可能时间不足，则先不漏水
        if (leakedWater != 0) {
            int leftWater = water.get() - leakedWater;
            // 可能水已漏光，设为0
            water.set(Math.max(0, leftWater));
            leakTimestamp = System.currentTimeMillis();
        }
        logger.info("剩余容量:{}", capacity - water.get());
        if (water.get() < capacity) {
            logger.info("tryAcquire success");
            water.incrementAndGet();
            return true;
        } else {
            logger.info("tryAcquire fail");
            return false;
        }
    }

}
