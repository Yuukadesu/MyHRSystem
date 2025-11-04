package com.example.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 机构表实体类
 */
@Data
@TableName("organization")
public class Organization {
    /**
     * 机构ID
     */
    @TableId(type = IdType.AUTO)
    private Long orgId;

    /**
     * 机构编号（用于生成档案编号）
     */
    private String orgCode;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 机构级别：1(一级机构), 2(二级机构), 3(三级机构)
     */
    private Integer orgLevel;

    /**
     * 父机构ID（一级机构的parentId为NULL）
     */
    private Long parentId;

    /**
     * 描述
     */
    private String description;

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

