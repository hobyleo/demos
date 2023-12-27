package com.hoby.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限流算法之<i>固定窗口</i>：
 * <p>在指定周期内累加访问次数，当访问次数达到设定的阈值时，触发限流策略，当进入下一个时间周期时进行访问次数的清零。</p>
 *
 * @author hoby
 * @since 2023-12-27
 */
public class FixedWindowRateLimiter {

    Logger logger = LoggerFactory.getLogger(FixedWindowRateLimiter.class);
    // 时间窗口大小，单位毫秒
    long windowSize;
    // 允许通过的请求数
    int maxRequestCount;
    // 当前窗口通过的请求数
    AtomicInteger counter = new AtomicInteger(0);
    // 窗口右边界
    long windowBorder;

    public FixedWindowRateLimiter(long windowSize, int maxRequestCount) {
        this.windowSize = windowSize;
        this.maxRequestCount = maxRequestCount;
        this.windowBorder = System.currentTimeMillis() + windowSize;
    }

    public synchronized boolean tryAcquire() {
        long currentTime = System.currentTimeMillis();
        logger.info("currentTime: {}", currentTime);
        if (windowBorder < currentTime) {
            logger.info("window reset");
            do {
                windowBorder += windowSize;
            } while (windowBorder < currentTime);
            counter = new AtomicInteger(0);
            logger.info("windowBorder: {}", windowBorder);
        }

        if (counter.intValue() < maxRequestCount) {
            counter.incrementAndGet();
            logger.info("tryAcquire success");
            return true;
        } else {
            logger.info("tryAcquire fail");
            return false;
        }
    }

}
