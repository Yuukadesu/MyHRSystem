package com.example.storage.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.entity.User;
import com.example.storage.mapper.UserMapper;
import com.example.storage.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户表 Service 实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return getOne(wrapper);
    }

    @Override
    public List<User> getByRole(String role) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getRole, role);
        return list(wrapper);
    }

    @Override
    public List<User> getByStatus(String status) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getStatus, status);
        return list(wrapper);
    }
}
