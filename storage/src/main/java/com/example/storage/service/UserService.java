package com.example.storage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.entity.User;

import java.util.List;

/**
 * 用户表 Service 接口
 */
public interface UserService extends IService<User> {
    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getByUsername(String username);

    /**
     * 根据角色查询用户列表
     *
     * @param role 角色
     * @return 用户列表
     */
    List<User> getByRole(String role);

    /**
     * 根据状态查询用户列表
     *
     * @param status 状态
     * @return 用户列表
     */
    List<User> getByStatus(String status);
}
