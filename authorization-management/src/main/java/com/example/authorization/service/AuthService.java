package com.example.authorization.service;

import com.example.authorization.dto.LoginRequest;
import com.example.authorization.dto.LoginResponse;

/**
 * 认证服务接口
 */
public interface AuthService {
    /**
     * 用户登录
     *
     * @param request 登录请求
     * @return 登录响应（包含Token）
     */
    LoginResponse login(LoginRequest request);

    /**
     * 用户登出
     *
     * @param token Token
     */
    void logout(String token);

    /**
     * 刷新Token
     *
     * @param token 旧Token
     * @return 新Token
     */
    LoginResponse refreshToken(String token);

    /**
     * 验证Token是否有效
     *
     * @param token Token
     * @return 是否有效
     */
    boolean validateToken(String token);
}

