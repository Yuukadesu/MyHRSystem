package com.example.common.enums;

import lombok.Getter;

/**
 * 职称枚举
 */
@Getter
public enum JobTitle {
    /**
     * 初级
     */
    JUNIOR("JUNIOR", "初级"),

    /**
     * 中级
     */
    INTERMEDIATE("INTERMEDIATE", "中级"),

    /**
     * 高级
     */
    SENIOR("SENIOR", "高级");

    private final String code;
    private final String description;

    JobTitle(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static JobTitle fromCode(String code) {
        for (JobTitle title : values()) {
            if (title.code.equals(code)) {
                return title;
            }
        }
        return null;
    }
}

