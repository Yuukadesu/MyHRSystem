package com.example.common.enums;

import lombok.Getter;

/**
 * 付款状态枚举
 */
@Getter
public enum PaymentStatus {
    /**
     * 待付款
     */
    PENDING("PENDING", "待付款"),

    /**
     * 已付款
     */
    PAID("PAID", "已付款");

    private final String code;
    private final String description;

    PaymentStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static PaymentStatus fromCode(String code) {
        for (PaymentStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}

