package com.example.common.enums;

import lombok.Getter;

/**
 * 薪酬发放单状态枚举
 */
@Getter
public enum SalaryIssuanceStatus {
    /**
     * 待登记
     */
    PENDING_REGISTRATION("PENDING_REGISTRATION", "待登记"),

    /**
     * 待复核
     */
    PENDING_REVIEW("PENDING_REVIEW", "待复核"),

    /**
     * 执行
     */
    EXECUTED("EXECUTED", "执行"),

    /**
     * 已付款
     */
    PAID("PAID", "已付款"),

    /**
     * 已驳回
     */
    REJECTED("REJECTED", "已驳回");

    private final String code;
    private final String description;

    SalaryIssuanceStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static SalaryIssuanceStatus fromCode(String code) {
        for (SalaryIssuanceStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}

