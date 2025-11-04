package com.example.common.enums;

import lombok.Getter;

/**
 * 薪酬标准状态枚举
 */
@Getter
public enum SalaryStandardStatus {
    /**
     * 待复核
     */
    PENDING_REVIEW("PENDING_REVIEW", "待复核"),

    /**
     * 已通过
     */
    APPROVED("APPROVED", "已通过"),

    /**
     * 已驳回
     */
    REJECTED("REJECTED", "已驳回");

    private final String code;
    private final String description;

    SalaryStandardStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static SalaryStandardStatus fromCode(String code) {
        for (SalaryStandardStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}

