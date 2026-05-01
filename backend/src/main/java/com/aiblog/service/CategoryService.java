package com.aiblog.service;

import com.aiblog.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 分类服务接口
 */
public interface CategoryService extends IService<Category> {

    /**
     * 获取所有分类（含文章数量）
     */
    List<Category> listWithArticleCount();

    /**
     * 创建分类
     */
    Category createCategory(Category category);

    /**
     * 更新分类
     */
    Category updateCategory(Long id, Category category);

    /**
     * 删除分类
     */
    void deleteCategory(Long id);
}
