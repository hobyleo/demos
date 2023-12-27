package com.hoby.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 限流算法之<i>滑动窗口</i>：
 * <p>
 * 滑动窗口在固定窗口的基础上，将时间窗口进行了更精细的分片，将一个窗口分为若干个等份的小窗口，每次仅滑动一小块的时间。
 * 每个小窗口对应不同的时间点，拥有独立的计数器，当请求的时间点大于当前窗口的最大时间点时，则将窗口向前平移一个小窗口
 * （将第一个小窗口的数据舍弃，第二个小窗口变成第一个小窗口，当前请求放在最后一个小窗口），整个窗口的所有请求数相加不能大于阈值。
 * </p>
 *
 * @author hoby
 * @since 2023-12-27
 */
public class SlidingWindowRateLimiter {

    Logger logger = LoggerFactory.getLogger(FixedWindowRateLimiter.class);
    // 时间窗口大小，单位毫秒
    long windowSize;
    // 分片窗口数
    int shardNum;
    // 允许通过的请求数
    int maxRequestCount;
    // 各个窗口内请求计数
    int[] shardRequestCount;
    // 请求总数
    int totalCount;
    // 当前窗口下标
    int shardId;
    // 每个小窗口大小，毫秒
    long tinyWindowSize;
    // 窗口右边界
    long windowBorder;

    public SlidingWindowRateLimiter(long windowSize, int shardNum, int maxRequestCount) {
        this.windowSize = windowSize;
        this.shardNum = shardNum;
        this.maxRequestCount = maxRequestCount;
        this.shardRequestCount = new int[shardNum];
        this.tinyWindowSize = windowSize / shardNum;
        this.windowBorder = System.currentTimeMillis();
    }

    public synchronized boolean tryAcquire() {
        long currentTime = System.currentTimeMillis();
        logger.info("currentTime: {}", currentTime);
        if (windowBorder < currentTime) {
            logger.info("window reset");
            do {
                shardId = (++shardId) % shardNum;
                totalCount -= shardRequestCount[shardId];
                shardRequestCount[shardId] = 0;
                windowBorder += tinyWindowSize;
            } while (windowBorder < currentTime);
            logger.info("windowBorder-{}: {}", shardId, windowBorder);
        }

        if (totalCount < maxRequestCount) {
            logger.info("tryAcquire success: {}", shardId);
            shardRequestCount[shardId]++;
            totalCount++;
            return true;
        } else {
            logger.info("tryAcquire fail");
            return false;
        }
    }

}
