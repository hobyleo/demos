package com.hoby.retry;

import cn.hutool.core.thread.ThreadUtil;
import com.hoby.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.*;
import org.springframework.retry.policy.*;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hoby
 * @since 2022-12-19
 */
@Slf4j
class SpringRetryTemplateTest extends BaseTest {

    /**
     * 重试间隔时间ms，默认1000ms
     */
    long fixedPeriodTime = 1000L;

    /**
     * 最大重试次数，默认3次
     */
    int maxRetryTimes = 3;

    /**
     * 需要重试的异常，key表示异常的字节码，value为true表示需要重试
     */
    Map<Class<? extends Throwable>, Boolean> exceptionMap = new HashMap<>();

    @Test
    void testRetry() {
        exceptionMap.put(RemoteAccessException.class, true);

        // 构建重试模板实例
        RetryTemplate retryTemplate = new RetryTemplate();

        /* 设置重试回退操作策略 */

        // 无退避算法策略，每次重试时立即重试
        NoBackOffPolicy noBackOffPolicy = new NoBackOffPolicy();

        /*
            固定时间的退避策略，需设置参数sleeper和backOffPeriod，
            sleeper指定等待策略，默认是Thread.sleep，即线程休眠，
            backOffPeriod指定休眠时间，默认1秒
        */
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        Sleeper sleeper = backOffPeriod -> ThreadUtil.sleep(backOffPeriod);
        fixedBackOffPolicy.setSleeper(sleeper);
        fixedBackOffPolicy.setBackOffPeriod(fixedPeriodTime);

        /*
            随机时间退避策略，需设置sleeper、minBackOffPeriod和maxBackOffPeriod，
            该策略在minBackOffPeriod,maxBackOffPeriod之间取一个随机休眠时间，
            minBackOffPeriod默认500毫秒，maxBackOffPeriod默认1500毫秒
        */
        UniformRandomBackOffPolicy uniformRandomBackOffPolicy = new UniformRandomBackOffPolicy();
        uniformRandomBackOffPolicy.setSleeper(sleeper);
        uniformRandomBackOffPolicy.setMinBackOffPeriod(1000L);
        uniformRandomBackOffPolicy.setMaxBackOffPeriod(3000L);

        /*
            指数退避策略，需设置参数sleeper、initialInterval、maxInterval和multiplier，
            initialInterval指定初始休眠时间，默认100毫秒，
            maxInterval指定最大休眠时间，默认30秒，
            multiplier指定乘数，即下一次休眠时间为当前休眠时间*multiplier
        */
        ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
        exponentialBackOffPolicy.setSleeper(sleeper);
        exponentialBackOffPolicy.setInitialInterval(500L);
        exponentialBackOffPolicy.setMaxInterval(10000L);
        exponentialBackOffPolicy.setMultiplier(1.5);

        // 随机指数退避策略，引入随机乘数可以实现随机乘数回退
        ExponentialRandomBackOffPolicy exponentialRandomBackOffPolicy = new ExponentialRandomBackOffPolicy();

        /* 设置重试策略，主要设置重试次数 */

        // 只允许调用RetryCallback一次，不允许重试
        NeverRetryPolicy neverRetryPolicy = new NeverRetryPolicy();

        // 允许无限重试，直到成功，此方式逻辑不当会导致死循环
        AlwaysRetryPolicy alwaysRetryPolicy = new AlwaysRetryPolicy();

        // 固定次数重试策略，默认重试最大次数为3次，RetryTemplate默认使用的策略
        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(maxRetryTimes, exceptionMap);

        // 超时时间重试策略，默认超时时间为1秒，在指定的超时时间内允许重试
        TimeoutRetryPolicy timeoutRetryPolicy = new TimeoutRetryPolicy();
        timeoutRetryPolicy.setTimeout(3000); // 默认超时时间为1s

        // 设置不同异常的重试策略
        ExceptionClassifierRetryPolicy exceptionClassifierRetryPolicy = new ExceptionClassifierRetryPolicy();
        HashMap<Class<? extends Throwable>, RetryPolicy> policyMap = new HashMap<>();
        policyMap.put(IllegalArgumentException.class, neverRetryPolicy);
        policyMap.put(RemoteAccessException.class, simpleRetryPolicy);
        exceptionClassifierRetryPolicy.setPolicyMap(policyMap);

        // 有熔断功能的重试策略，需设置3个参数openTimeout、resetTimeout和delegate
        CircuitBreakerRetryPolicy circuitBreakerRetryPolicy = new CircuitBreakerRetryPolicy(new SimpleRetryPolicy());
        circuitBreakerRetryPolicy.setOpenTimeout(5000L);
        circuitBreakerRetryPolicy.setResetTimeout(5000L);

        /*
            组合重试策略，有两种组合方式，
            乐观组合重试策略是指只要有一个策略允许即可以重试，
            悲观组合重试策略是指只要有一个策略不允许即可以重试，
            但不管哪种组合方式，组合中的每一个策略都会执行
        */
        CompositeRetryPolicy compositeRetryPolicy = new CompositeRetryPolicy();
        compositeRetryPolicy.setOptimistic(true);
        compositeRetryPolicy.setPolicies(new RetryPolicy[]{simpleRetryPolicy, timeoutRetryPolicy});

        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
        retryTemplate.setRetryPolicy(simpleRetryPolicy);

        Boolean execute = retryTemplate.execute(
                retryContext -> {
                    // RetryCallback
                    boolean b = RetryDemoTask.retryTask("abc");
                    log.info("调用的结果:{}", b);
                    return b;
                },
                retryContext -> {
                    // RecoveryCallback
                    log.info("已达到最大重试次数或抛出了不重试的异常");
                    return false;
                }
        );
        log.info("执行结果:{}", execute);
    }
}
