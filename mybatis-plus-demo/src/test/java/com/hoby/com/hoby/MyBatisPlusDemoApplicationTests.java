package com.hoby.com.hoby;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hoby.entity.Users;
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

    @Test
    public void testInterceptor() {
        usersService.lambdaQuery().like(Users::getEmail, "gmail").page(new Page<>(2, 1));
    }
}
