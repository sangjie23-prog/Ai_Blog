package com.aiblog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 标签实体
 */
@Data
@TableName("tag")
public class Tag {

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 标签名称 */
    private String name;

    /** URL标识 */
    private String slug;

    /** 文章数量（非数据库字段） */
    @TableField(exist = false)
    private Long articleCount;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
