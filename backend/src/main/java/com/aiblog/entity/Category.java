package com.aiblog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 分类实体
 */
@Data
@TableName("category")
public class Category {

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 分类名称 */
    private String name;

    /** URL标识 */
    private String slug;

    /** 描述 */
    private String description;

    /** 排序 */
    private Integer sortOrder;

    /** 文章数量（非数据库字段） */
    @TableField(exist = false)
    private Long articleCount;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
