package com.hoby;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author hoby
 * @since 2022-12-19
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringRetryApplication.class)
public class BaseTest {

    @BeforeEach
    public void init() {
        log.info("==============测试开始==============");
    }

    @AfterEach
    public void after() {
        log.info("==============测试结束==============");
    }
}
