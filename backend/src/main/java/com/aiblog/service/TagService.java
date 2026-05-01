package com.aiblog.service;

import com.aiblog.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 标签服务接口
 */
public interface TagService extends IService<Tag> {

    /**
     * 获取所有标签（含文章数量）
     */
    List<Tag> listWithArticleCount();

    /**
     * 创建标签
     */
    Tag createTag(Tag tag);

    /**
     * 更新标签
     */
    Tag updateTag(Long id, Tag tag);

    /**
     * 删除标签
     */
    void deleteTag(Long id);
}
