package com.example.common.enums;

import lombok.Getter;

/**
 * 性别枚举
 */
@Getter
public enum Gender {
    /**
     * 男
     */
    MALE("MALE", "男"),

    /**
     * 女
     */
    FEMALE("FEMALE", "女");

    private final String code;
    private final String description;

    Gender(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static Gender fromCode(String code) {
        for (Gender gender : values()) {
            if (gender.code.equals(code)) {
                return gender;
            }
        }
        return null;
    }
}

