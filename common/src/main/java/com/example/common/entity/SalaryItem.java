package com.example.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 薪酬项目表实体类
 */
@Data
@TableName("salary_item")
public class SalaryItem {
    /**
     * 薪酬项目ID
     */
    @TableId(type = IdType.AUTO)
    private Long itemId;

    /**
     * 项目编号（如：S001）
     */
    private String itemCode;

    /**
     * 项目名称（如：基本工资、绩效奖金等）
     */
    private String itemName;

    /**
     * 项目类型：INCOME(收入项), DEDUCTION(扣除项)
     */
    private String itemType;

    /**
     * 计算规则（如：基本工资*8%），为空表示手动输入
     */
    private String calculationRule;

    /**
     * 排序顺序
     */
    private Integer sortOrder;

    /**
     * 状态：ACTIVE(激活), INACTIVE(禁用)
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

