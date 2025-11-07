package com.example.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工档案表实体类
 */
@Data
@TableName("employee_archive")
public class EmployeeArchive {
    /**
     * 档案ID
     */
    @TableId(type = IdType.AUTO)
    private Long archiveId;

    /**
     * 档案编号（年份4位+一级机构2位+二级机构2位+三级机构2位+编号2位）
     */
    private String archiveNumber;

    // ========== 基本信息 ==========
    /**
     * 姓名
     */
    private String name;

    /**
     * 性别：MALE(男), FEMALE(女)
     */
    private String gender;

    /**
     * 身份证号码
     */
    private String idNumber;

    /**
     * 出生日期
     */
    private LocalDate birthday;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 国籍
     */
    private String nationality;

    /**
     * 出生地
     */
    private String placeOfBirth;

    /**
     * 民族
     */
    private String ethnicity;

    /**
     * 宗教信仰
     */
    private String religiousBelief;

    /**
     * 政治面貌
     */
    private String politicalStatus;

    /**
     * 学历
     */
    private String educationLevel;

    /**
     * 学历专业
     */
    private String major;

    // ========== 联系信息 ==========
    /**
     * Email
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * QQ
     */
    private String qq;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 住址
     */
    private String address;

    /**
     * 邮编
     */
    private String postalCode;

    // ========== 其他信息 ==========
    /**
     * 爱好
     */
    private String hobby;

    /**
     * 个人履历（大段文本）
     */
    private String personalResume;

    /**
     * 家庭关系信息（大段文本）
     */
    private String familyRelationship;

    /**
     * 备注（大段文本）
     */
    private String remarks;

    /**
     * 照片URL
     */
    private String photoUrl;

    // ========== 机构职位信息 ==========
    /**
     * 一级机构ID
     */
    private Long firstOrgId;

    /**
     * 二级机构ID
     */
    private Long secondOrgId;

    /**
     * 三级机构ID
     */
    private Long thirdOrgId;

    /**
     * 职位ID
     */
    private Long positionId;

    /**
     * 职称：JUNIOR(初级), INTERMEDIATE(中级), SENIOR(高级)
     */
    private String jobTitle;

    // ========== 薪酬信息 ==========
    /**
     * 薪酬标准ID
     */
    private Long salaryStandardId;

    // ========== 流程信息 ==========
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
     * 状态：PENDING_REVIEW(待复核), NORMAL(正常), DELETED(已删除)
     */
    private String status;

    /**
     * 删除时间
     */
    private LocalDateTime deleteTime;

    /**
     * 删除原因
     */
    private String deleteReason;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

