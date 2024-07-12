package com.hoby;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hoby.entity.UsAdmin;
import com.hoby.service.UsAdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liaozh
 * @since 2024-07-12
 */
@SpringBootTest(classes = ShardingSphereDemoApplication.class)
public class ShardingSphereDemoApplicationTests {

    @Autowired
    private UsAdminService usAdminService;

    @Test
    public void insertBatch() {
        int size = 100;
        List<UsAdmin> list = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            UsAdmin usAdmin = new UsAdmin();
            usAdmin.setUserId(i);
            usAdmin.setAddrId(i);
            usAdmin.setUserName("user_" + i);
            list.add(usAdmin);
        }
        usAdminService.saveBatch(list);
    }

    @Test
    public void selectPage() {
        LambdaQueryWrapper<UsAdmin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(UsAdmin::getUserId);
        Page<UsAdmin> page = usAdminService.page(new Page<>(1, 20), queryWrapper);
        System.out.println(page.getRecords());
    }
}
