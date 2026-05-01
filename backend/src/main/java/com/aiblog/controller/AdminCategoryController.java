package com.aiblog.controller;

import com.aiblog.common.Result;
import com.aiblog.entity.Category;
import com.aiblog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台分类管理控制器
 */
@RestController
@RequestMapping("/api/admin/categories")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取分类列表（含文章数量）
     */
    @GetMapping
    public Result<List<Category>> list() {
        List<Category> categories = categoryService.listWithArticleCount();
        return Result.success(categories);
    }

    /**
     * 获取分类详情
     */
    @GetMapping("/{id}")
    public Result<Category> getById(@PathVariable Long id) {
        Category category = categoryService.getById(id);
        if (category == null) {
            return Result.error(404, "分类不存在");
        }
        return Result.success(category);
    }

    /**
     * 创建分类
     */
    @PostMapping
    public Result<Category> create(@RequestBody Category category) {
        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }
        Category created = categoryService.createCategory(category);
        return Result.success("创建成功", created);
    }

    /**
     * 更新分类
     */
    @PutMapping("/{id}")
    public Result<Category> update(@PathVariable Long id, @RequestBody Category category) {
        Category updated = categoryService.updateCategory(id, category);
        return Result.success("更新成功", updated);
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success("删除成功", null);
    }
}
