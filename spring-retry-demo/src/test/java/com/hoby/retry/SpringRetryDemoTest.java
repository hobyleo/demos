package com.hoby.retry;

import com.hoby.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @author hoby
 * @since 2022-12-19
 */
@Slf4j
class SpringRetryDemoTest extends BaseTest {

    @Resource
    SpringRetryDemo springRetryDemo;

    @Test
    void testCall() {
        boolean b = springRetryDemo.call("abc");
        log.info("--结果是:{}--", b);
    }
}
