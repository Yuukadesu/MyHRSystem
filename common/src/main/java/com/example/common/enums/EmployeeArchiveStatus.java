package com.example.common.enums;

import lombok.Getter;

/**
 * 员工档案状态枚举
 */
@Getter
public enum EmployeeArchiveStatus {
    /**
     * 待复核
     */
    PENDING_REVIEW("PENDING_REVIEW", "待复核"),

    /**
     * 正常
     */
    NORMAL("NORMAL", "正常"),

    /**
     * 已删除
     */
    DELETED("DELETED", "已删除");

    private final String code;
    private final String description;

    EmployeeArchiveStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static EmployeeArchiveStatus fromCode(String code) {
        for (EmployeeArchiveStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}

