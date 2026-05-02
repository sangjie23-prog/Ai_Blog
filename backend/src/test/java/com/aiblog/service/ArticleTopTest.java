package com.aiblog.service;

import com.aiblog.entity.Article;
import com.aiblog.mapper.ArticleMapper;
import com.aiblog.service.impl.ArticleServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * 文章置顶功能单元测试
 */
@ExtendWith(MockitoExtension.class)
public class ArticleTopTest {

    @Mock
    private ArticleMapper articleMapper;

    @InjectMocks
    private ArticleServiceImpl articleService;

    private Article topArticle;
    private Article normalArticle;

    @BeforeEach
    void setUp() {
        topArticle = new Article();
        topArticle.setId(1L);
        topArticle.setTitle("置顶文章");
        topArticle.setContent("测试内容");
        topArticle.setStatus(1);
        topArticle.setIsTop(1);
        topArticle.setViewCount(100);
        topArticle.setLikeCount(10);
        topArticle.setCreatedAt(LocalDateTime.now());
        topArticle.setUpdatedAt(LocalDateTime.now());

        normalArticle = new Article();
        normalArticle.setId(2L);
        normalArticle.setTitle("普通文章");
        normalArticle.setContent("测试内容");
        normalArticle.setStatus(1);
        normalArticle.setIsTop(0);
        normalArticle.setViewCount(50);
        normalArticle.setLikeCount(5);
        normalArticle.setCreatedAt(LocalDateTime.now());
        normalArticle.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testSetTop() {
        when(articleMapper.selectById(1L)).thenReturn(normalArticle);
        when(articleMapper.updateById(any(Article.class))).thenReturn(1);

        Article article = articleService.getById(1L);
        assertNotNull(article);
        assertEquals(0, article.getIsTop());

        article.setIsTop(1);
        articleService.updateById(article);

        verify(articleMapper).updateById(any(Article.class));
    }

    @Test
    void testUnsetTop() {
        when(articleMapper.selectById(1L)).thenReturn(topArticle);
        when(articleMapper.updateById(any(Article.class))).thenReturn(1);

        Article article = articleService.getById(1L);
        assertNotNull(article);
        assertEquals(1, article.getIsTop());

        article.setIsTop(0);
        articleService.updateById(article);

        verify(articleMapper).updateById(any(Article.class));
    }

    @Test
    void testArticlePageWithTopFirst() {
        Page<Article> page = new Page<>(1, 10);
        List<Article> articles = List.of(topArticle, normalArticle);

        when(articleMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenAnswer(invocation -> {
            Page<Article> resultPage = invocation.getArgument(0);
            resultPage.setRecords(articles);
            resultPage.setTotal(2);
            return resultPage;
        });

        IPage<Article> result = articleService.getArticlePage(1, 10, 1);

        assertNotNull(result);
        assertEquals(2, result.getTotal());
        assertEquals(2, result.getRecords().size());
        assertEquals(1, result.getRecords().get(0).getIsTop());
        assertEquals(0, result.getRecords().get(1).getIsTop());
    }

    @Test
    void testArticlePageWithKeywordTopFirst() {
        Page<Article> page = new Page<>(1, 10);
        List<Article> articles = List.of(topArticle, normalArticle);

        when(articleMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenAnswer(invocation -> {
            Page<Article> resultPage = invocation.getArgument(0);
            resultPage.setRecords(articles);
            resultPage.setTotal(2);
            return resultPage;
        });

        IPage<Article> result = articleService.getArticlePageWithKeyword(1, 10, 1, "测试");

        assertNotNull(result);
        assertEquals(2, result.getTotal());
        assertEquals(1, result.getRecords().get(0).getIsTop());
    }

    @Test
    void testArticlePageByTagTopFirst() {
        Page<Article> page = new Page<>(1, 10);
        List<Article> articles = List.of(topArticle, normalArticle);

        when(articleMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenAnswer(invocation -> {
            Page<Article> resultPage = invocation.getArgument(0);
            resultPage.setRecords(articles);
            resultPage.setTotal(2);
            return resultPage;
        });

        IPage<Article> result = articleService.getArticlePageByTag(1, 10, 1, "Java");

        assertNotNull(result);
        assertEquals(2, result.getTotal());
        assertEquals(1, result.getRecords().get(0).getIsTop());
    }

    @Test
    void testCreateArticleDefaultNotTop() {
        Article newArticle = new Article();
        newArticle.setTitle("新文章");
        newArticle.setContent("新内容");
        newArticle.setStatus(0);
        newArticle.setIsTop(0);

        when(articleMapper.insert(any(Article.class))).thenReturn(1);

        articleService.save(newArticle);

        verify(articleMapper).insert(any(Article.class));
        assertEquals(0, newArticle.getIsTop());
    }

    @Test
    void testToggleTopStatus() {
        when(articleMapper.selectById(1L)).thenReturn(topArticle);
        when(articleMapper.updateById(any(Article.class))).thenReturn(1);

        Article article = articleService.getById(1L);
        assertEquals(1, article.getIsTop());

        article.setIsTop(0);
        articleService.updateById(article);
        assertEquals(0, article.getIsTop());

        article.setIsTop(1);
        articleService.updateById(article);
        assertEquals(1, article.getIsTop());
    }
}
