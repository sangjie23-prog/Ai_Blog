package com.aiblog.controller;

import com.aiblog.common.Result;
import com.aiblog.entity.Article;
import com.aiblog.service.ArticleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文章控制器
 * 提供文章的增删改查、分页列表、阅读量、点赞等接口
 */
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 分页查询文章列表
     * @param pageNum 页码，默认1
     * @param pageSize 每页数量，默认10
     * @param status 文章状态（可选）：0-草稿，1-已发布
     * @param keyword 搜索关键词（可选，按标题搜索）
     * @param tag 标签名称（可选，按标签搜索）
     * @return 分页文章数据
     */
    @GetMapping
    public Result<IPage<Article>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String tag) {
        IPage<Article> page;
        if (tag != null && !tag.isEmpty()) {
            // 按标签搜索
            page = articleService.getArticlePageByTag(pageNum, pageSize, status, tag);
        } else if (keyword != null && !keyword.isEmpty()) {
            // 按标题关键词搜索
            page = articleService.getArticlePageWithKeyword(pageNum, pageSize, status, keyword);
        } else {
            // 普通分页查询
            page = articleService.getArticlePage(pageNum, pageSize, status);
        }
        return Result.success(page);
    }

    /**
     * 根据ID获取文章详情
     * @param id 文章ID
     * @return 文章详情
     */
    @GetMapping("/{id}")
    public Result<Article> getById(@PathVariable Long id) {
        // 增加阅读量
        articleService.incrementViewCount(id);
        Article article = articleService.getArticleById(id);
        return Result.success(article);
    }

    /**
     * 创建文章
     * @param article 文章信息
     * @return 创建结果
     */
    @PostMapping
    public Result<Void> create(@RequestBody Article article) {
        // 设置默认值
        if (article.getViewCount() == null) {
            article.setViewCount(0);
        }
        if (article.getLikeCount() == null) {
            article.setLikeCount(0);
        }
        if (article.getStatus() == null) {
            article.setStatus(0); // 默认草稿
        }
        articleService.save(article);
        return Result.success("创建成功", null);
    }

    /**
     * 更新文章
     * @param id 文章ID
     * @param article 更新的文章信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Article article) {
        article.setId(id);
        articleService.updateById(article);
        return Result.success("更新成功", null);
    }

    /**
     * 删除文章（逻辑删除）
     * @param id 文章ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        articleService.removeById(id);
        return Result.success("删除成功", null);
    }

    /**
     * 点赞文章
     * @param id 文章ID
     * @return 点赞结果
     */
    @PostMapping("/{id}/like")
    public Result<Void> like(@PathVariable Long id) {
        articleService.incrementLikeCount(id);
        return Result.success("点赞成功", null);
    }
}
