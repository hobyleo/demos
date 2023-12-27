package com.hoby.algorithm;

import com.google.common.util.concurrent.RateLimiter;

/**
 * 限流算法之<i>令牌桶</i>：
 * <p>1.系统以固定的速率向桶中添加令牌；</p>
 * <p>2.当有请求到来时，会尝试从桶中移除一个令牌，如果桶中有足够的令牌，则请求可以被处理或数据包可以被发送；</p>
 * <p>3.如果桶中没有令牌，那么请求将被拒绝；</p>
 * <p>4.桶中的令牌数不能超过桶的容量，如果新生成的令牌超过了桶的容量，那么新的令牌会被丢弃。</p>
 * <p>5.令牌桶算法的一个重要特性是，它能够应对突发流量。当桶中有足够的令牌时，可以一次性处理多个请求，这对于需要处理突发流量的应用场景非常有用。
 * 但是又不会无限制的增加处理速率导致压垮服务器，因为桶内令牌数量是有限制的。</p>
 *
 * @author hoby
 * @since 2023-12-27
 */
public class TokenBucketRateLimiter {

    private RateLimiter rateLimiter;

    public TokenBucketRateLimiter(int permitsPerSecond) {
        this.rateLimiter = RateLimiter.create(permitsPerSecond);
    }

    public boolean tryAcquire() {
        return rateLimiter.tryAcquire();
    }

}
