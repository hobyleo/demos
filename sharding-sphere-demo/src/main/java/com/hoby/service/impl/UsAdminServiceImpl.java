package com.hoby.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hoby.entity.UsAdmin;
import com.hoby.mapper.UsAdminMapper;
import com.hoby.service.UsAdminService;
import org.springframework.stereotype.Service;

/**
 * 后台用户表(UsAdmin0)表服务实现类
 *
 * @author liaozh
 * @since 2024-07-12
 */
@Service("usAdminService")
public class UsAdminServiceImpl extends ServiceImpl<UsAdminMapper, UsAdmin> implements UsAdminService {

}

