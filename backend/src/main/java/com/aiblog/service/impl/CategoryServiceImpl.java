package com.aiblog.service.impl;

import com.aiblog.entity.Category;
import com.aiblog.mapper.CategoryMapper;
import com.aiblog.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类服务实现类
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public List<Category> listWithArticleCount() {
        List<Category> categories = list(new LambdaQueryWrapper<Category>()
                .orderByAsc(Category::getSortOrder));
        // 填充文章数量
        categories.forEach(category -> {
            Long count = baseMapper.countArticlesByCategoryId(category.getId());
            category.setArticleCount(count);
        });
        return categories;
    }

    @Override
    public Category createCategory(Category category) {
        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }
        save(category);
        return category;
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        Category existing = getById(id);
        if (existing == null) {
            throw new RuntimeException("分类不存在");
        }
        category.setId(id);
        updateById(category);
        return getById(id);
    }

    @Override
    public void deleteCategory(Long id) {
        Category existing = getById(id);
        if (existing == null) {
            throw new RuntimeException("分类不存在");
        }
        // 检查是否有文章使用该分类
        Long count = baseMapper.countArticlesByCategoryId(id);
        if (count > 0) {
            throw new RuntimeException("该分类下还有文章，无法删除");
        }
        removeById(id);
    }
}
