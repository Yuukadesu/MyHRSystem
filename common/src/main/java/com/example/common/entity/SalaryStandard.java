package com.example.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 薪酬标准表实体类
 */
@Data
@TableName("salary_standard")
public class SalaryStandard {
    /**
     * 薪酬标准ID
     */
    @TableId(type = IdType.AUTO)
    private Long standardId;

    /**
     * 薪酬标准编号（如：SAL202307001）
     */
    private String standardCode;

    /**
     * 薪酬标准名称（如：前端工程师-中级标准）
     */
    private String standardName;

    /**
     * 适用职位ID
     */
    private Long positionId;

    /**
     * 职称：JUNIOR(初级), INTERMEDIATE(中级), SENIOR(高级)
     */
    private String jobTitle;

    /**
     * 制定人ID
     */
    private Long formulatorId;

    /**
     * 登记人ID
     */
    private Long registrarId;

    /**
     * 登记时间
     */
    private LocalDateTime registrationTime;

    /**
     * 复核人ID
     */
    private Long reviewerId;

    /**
     * 复核时间
     */
    private LocalDateTime reviewTime;

    /**
     * 复核意见（大段文本）
     */
    private String reviewComments;

    /**
     * 状态：PENDING_REVIEW(待复核), APPROVED(已通过), REJECTED(已驳回)
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

