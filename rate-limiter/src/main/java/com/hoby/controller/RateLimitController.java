package com.hoby.controller;

import com.hoby.algorithm.FixedWindowRateLimiter;
import com.hoby.algorithm.LeakyBucketRateLimiter;
import com.hoby.algorithm.SlidingWindowRateLimiter;
import com.hoby.algorithm.TokenBucketRateLimiter;
import com.hoby.annotation.Limit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author hoby
 * @since 2023-12-27
 */
@RestController
@RequestMapping("/rate-limit")
public class RateLimitController {

    private static final FixedWindowRateLimiter FIXED_WINDOW_RATE_LIMITER = new FixedWindowRateLimiter(3000, 3);
    private static final SlidingWindowRateLimiter SLIDING_WINDOW_RATE_LIMITER = new SlidingWindowRateLimiter(3000, 3, 3);
    private static final LeakyBucketRateLimiter LEAKY_BUCKET_RATE_LIMITER = new LeakyBucketRateLimiter(3, 1);
    private static final TokenBucketRateLimiter TOKEN_BUCKET_RATE_LIMITER = new TokenBucketRateLimiter(1);

    /**
     * 固定窗口算法
     */
    @GetMapping("/fixed-window")
    public boolean tryAcquireByFixedWindow() {
        return FIXED_WINDOW_RATE_LIMITER.tryAcquire();
    }

    /**
     * 滑动窗口算法
     */
    @GetMapping("/sliding-window")
    public boolean tryAcquireBySlidingWindow() {
        return SLIDING_WINDOW_RATE_LIMITER.tryAcquire();
    }

    /**
     * 漏桶算法
     */
    @GetMapping("/leaky-bucket")
    public boolean tryAcquireByLeakyBucket() {
        return LEAKY_BUCKET_RATE_LIMITER.tryAcquire();
    }

    /**
     * 令牌桶算法
     */
    @GetMapping("/token-bucket")
    public boolean tryAcquireByTokenBucket() {
        return TOKEN_BUCKET_RATE_LIMITER.tryAcquire();
    }

    /**
     * AOP切面注解
     */
    @Limit(key = "hoby", permitsPerSecond = 1, timeout = 3, timeUnit = TimeUnit.SECONDS)
    @GetMapping("/limit-aspect")
    public boolean tryAcquireByLimitAspect() {
        return true;
    }

}
