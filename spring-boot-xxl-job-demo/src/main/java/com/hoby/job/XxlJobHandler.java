package com.hoby.job;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author liaozh
 * @since 2024-07-22
 */
@Slf4j
@Component
public class XxlJobHandler {

    @XxlJob("sleepNotice")
    public void sleepNotice() {
        String param = XxlJobHelper.getJobParam();
        log.info("sleepNotice param: {}", param);
        XxlJobHelper.log("sleepNotice param: {}", param);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.error("sleepNotice fail: {}", e.getMessage(), e);
            XxlJobHelper.log("sleepNotice fail: {}", e.getMessage(), e);
            XxlJobHelper.handleFail();
        }
        log.info("sleepNotice success");
        XxlJobHelper.log("sleepNotice success");
        XxlJobHelper.handleSuccess();
    }
}
