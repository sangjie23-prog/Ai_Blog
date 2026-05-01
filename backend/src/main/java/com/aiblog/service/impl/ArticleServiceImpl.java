package com.aiblog.service.impl;

import com.aiblog.entity.Article;
import com.aiblog.mapper.ArticleMapper;
import com.aiblog.service.ArticleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 文章服务实现类
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Override
    public IPage<Article> getArticlePage(int pageNum, int pageSize, Integer status) {
        // 构建分页对象
        Page<Article> page = new Page<>(pageNum, pageSize);

        // 构建查询条件
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(status != null, Article::getStatus, status)
               .orderByDesc(Article::getCreatedAt);

        return page(page, wrapper);
    }

    @Override
    public Article getArticleById(Long id) {
        Article article = getById(id);
        if (article == null) {
            throw new RuntimeException("文章不存在");
        }
        return article;
    }

    @Override
    public void incrementViewCount(Long id) {
        Article article = getById(id);
        if (article != null) {
            article.setViewCount(article.getViewCount() + 1);
            updateById(article);
        }
    }

    @Override
    public void incrementLikeCount(Long id) {
        Article article = getById(id);
        if (article != null) {
            article.setLikeCount(article.getLikeCount() + 1);
            updateById(article);
        }
    }
}
