package com.example.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 职位表实体类
 */
@Data
@TableName("position")
public class Position {
    /**
     * 职位ID
     */
    @TableId(type = IdType.AUTO)
    private Long positionId;

    /**
     * 职位名称
     */
    private String positionName;

    /**
     * 所属三级机构ID
     */
    private Long thirdOrgId;

    /**
     * 职位描述
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

