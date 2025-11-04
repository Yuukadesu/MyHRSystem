package com.example.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 薪酬发放单表实体类
 */
@Data
@TableName("salary_issuance")
public class SalaryIssuance {
    /**
     * 薪酬发放单ID
     */
    @TableId(type = IdType.AUTO)
    private Long issuanceId;

    /**
     * 薪酬单号（如：PAY202307001）
     */
    private String salarySlipNumber;

    /**
     * 三级机构ID
     */
    private Long thirdOrgId;

    /**
     * 总人数
     */
    private Integer totalEmployees;

    /**
     * 基本薪酬总额
     */
    private BigDecimal totalBasicSalary;

    /**
     * 实发薪酬总额
     */
    private BigDecimal totalNetPay;

    /**
     * 发放月份
     */
    private LocalDate issuanceMonth;

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
     * 状态：PENDING_REGISTRATION(待登记), PENDING_REVIEW(待复核), EXECUTED(执行), PAID(已付款)
     */
    private String status;

    /**
     * 付款状态（由财务系统更新）：PENDING(待付款), PAID(已付款)
     */
    private String paymentStatus;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

