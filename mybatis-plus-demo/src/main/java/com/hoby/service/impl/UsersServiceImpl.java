package com.hoby.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hoby.mapper.UsersMapper;
import com.hoby.entity.Users;
import com.hoby.service.UsersService;
import org.springframework.stereotype.Service;

/**
 * (Users)表服务实现类
 *
 * @author liaozh
 * @since 2024-07-27
 */
@Service("usersService")
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

}

