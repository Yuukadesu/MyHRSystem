package com.example.common.enums;

import lombok.Getter;

/**
 * 用户状态枚举
 */
@Getter
public enum UserStatus {
    /**
     * 激活
     */
    ACTIVE("ACTIVE", "激活"),

    /**
     * 禁用
     */
    INACTIVE("INACTIVE", "禁用");

    private final String code;
    private final String description;

    UserStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static UserStatus fromCode(String code) {
        for (UserStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}

