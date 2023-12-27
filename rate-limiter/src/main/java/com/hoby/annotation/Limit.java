package com.hoby.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 限流注解
 *
 * @author hoby
 * @since 2023-12-27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface Limit {
    /**
     * 资源主键
     */
    String key() default "";
    /**
     * 每秒最多访问次数
     */
    double permitsPerSecond();
    /**
     * 最大等待时长，在timeout时间内如果能获取到令牌，则挂起等待相应时间
     */
    long timeout();
    /**
     * 等待时间单位（默认：ms）
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
    /**
     * 提示信息
     */
    String msg() default "系统繁忙,请稍后重试";
}
