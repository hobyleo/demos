package com.hoby.com.hoby;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.hoby.entity.UploadRecord;
import com.hoby.entity.Users;
import com.hoby.service.UploadRecordService;
import com.hoby.service.UsersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author liaozh
 * @since 2024-07-27
 */
@SpringBootTest
public class MyBatisPlusDemoApplicationTests {

    @Autowired
    private UsersService usersService;

    @Autowired
    private UploadRecordService uploadRecordService;

    @Test
    public void testMysql() {
        usersService.lambdaQuery().like(Users::getEmail, "gmail").page(new Page<>(2, 1));
    }

    @Test
    public void testOracle() {
        uploadRecordService.lambdaQuery().eq(UploadRecord::getCommand, "SALE_OR_REFUND_SALE").page(new Page<>(2, 2));
    }

    @Test
    public void testPageHelper() {
        PageHelper.startPage(2, 2);
        uploadRecordService.lambdaQuery().eq(UploadRecord::getCommand, "SALE_OR_REFUND_SALE").list();
    }
}
