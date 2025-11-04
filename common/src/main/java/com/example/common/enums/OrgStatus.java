package com.example.common.enums;

import lombok.Getter;

/**
 * 机构状态枚举
 */
@Getter
public enum OrgStatus {
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

    OrgStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static OrgStatus fromCode(String code) {
        for (OrgStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}

