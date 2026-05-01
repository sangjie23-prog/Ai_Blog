package com.aiblog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文章实体类
 */
@Data
@TableName("article")
public class Article {

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 文章标题 */
    private String title;

    /** 文章内容（Markdown格式） */
    private String content;

    /** 文章摘要 */
    private String summary;

    /** 封面图片URL */
    private String coverImage;

    /** 状态：0-草稿，1-已发布 */
    private Integer status;

    /** 阅读量 */
    private Integer viewCount;

    /** 点赞数 */
    private Integer likeCount;

    /** 分类ID */
    private Long categoryId;

    /** 标签（逗号分隔） */
    private String tags;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /** 逻辑删除：0-未删除，1-已删除 */
    @TableLogic
    private Integer deleted;
}
