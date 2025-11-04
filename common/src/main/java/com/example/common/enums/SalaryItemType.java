package com.example.common.enums;

import lombok.Getter;

/**
 * 薪酬项目类型枚举
 */
@Getter
public enum SalaryItemType {
    /**
     * 收入项
     */
    INCOME("INCOME", "收入项"),

    /**
     * 扣除项
     */
    DEDUCTION("DEDUCTION", "扣除项");

    private final String code;
    private final String description;

    SalaryItemType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static SalaryItemType fromCode(String code) {
        for (SalaryItemType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}

