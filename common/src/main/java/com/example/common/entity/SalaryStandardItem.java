package com.example.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 薪酬标准明细表实体类
 */
@Data
@TableName("salary_standard_item")
public class SalaryStandardItem {
    /**
     * 薪酬标准明细ID
     */
    @TableId(type = IdType.AUTO)
    private Long standardItemId;

    /**
     * 薪酬标准ID
     */
    private Long standardId;

    /**
     * 薪酬项目ID
     */
    private Long itemId;

    /**
     * 金额（保留两位小数）
     */
    private BigDecimal amount;

    /**
     * 是否根据计算规则计算：0(手动输入), 1(自动计算)
     */
    private Boolean isCalculated;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

