package com.aiblog.controller;

import com.aiblog.common.Result;
import com.aiblog.entity.Article;
import com.aiblog.service.ArticleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台文章管理控制器
 * 需要JWT认证，提供管理员专用的文章管理接口
 */
@RestController
@RequestMapping("/api/admin/articles")
public class AdminArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 分页查询文章列表（包含草稿）
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param status 状态筛选（可选）：0-草稿，1-已发布
     * @param keyword 关键词搜索（标题）
     * @return 分页文章数据
     */
    @GetMapping
    public Result<IPage<Article>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword) {
        IPage<Article> page = articleService.getArticlePageWithKeyword(pageNum, pageSize, status, keyword);
        return Result.success(page);
    }

    /**
     * 根据ID获取文章详情
     * @param id 文章ID
     * @return 文章详情
     */
    @GetMapping("/{id}")
    public Result<Article> getById(@PathVariable Long id) {
        Article article = articleService.getArticleById(id);
        if (article == null) {
            return Result.error(404, "文章不存在");
        }
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
        if (article.getDeleted() == null) {
            article.setDeleted(0);
        }
        if (article.getIsTop() == null) {
            article.setIsTop(0); // 默认不置顶
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
        // 验证文章是否存在
        Article existing = articleService.getArticleById(id);
        if (existing == null) {
            return Result.error(404, "文章不存在");
        }
        article.setId(id);
        articleService.updateById(article);
        return Result.success("更新成功", null);
    }

    /**
     * 删除文章（物理删除）
     * @param id 文章ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        articleService.removeById(id);
        return Result.success("删除成功", null);
    }

    /**
     * 发布文章（将状态改为已发布）
     * @param id 文章ID
     * @return 发布结果
     */
    @PostMapping("/{id}/publish")
    public Result<Void> publish(@PathVariable Long id) {
        Article article = articleService.getArticleById(id);
        if (article == null) {
            return Result.error(404, "文章不存在");
        }
        article.setStatus(1); // 1-已发布
        articleService.updateById(article);
        return Result.success("发布成功", null);
    }

    /**
     * 撤回文章（将状态改为草稿）
     * @param id 文章ID
     * @return 撤回结果
     */
    @PostMapping("/{id}/unpublish")
    public Result<Void> unpublish(@PathVariable Long id) {
        Article article = articleService.getArticleById(id);
        if (article == null) {
            return Result.error(404, "文章不存在");
        }
        article.setStatus(0); // 0-草稿
        articleService.updateById(article);
        return Result.success("已撤回为草稿", null);
    }

    /**
     * 设置文章置顶
     * @param id 文章ID
     * @return 置顶结果
     */
    @PostMapping("/{id}/top")
    public Result<Void> setTop(@PathVariable Long id) {
        Article article = articleService.getArticleById(id);
        if (article == null) {
            return Result.error(404, "文章不存在");
        }
        article.setIsTop(1);
        articleService.updateById(article);
        return Result.success("置顶成功", null);
    }

    /**
     * 取消文章置顶
     * @param id 文章ID
     * @return 取消置顶结果
     */
    @PostMapping("/{id}/untop")
    public Result<Void> unsetTop(@PathVariable Long id) {
        Article article = articleService.getArticleById(id);
        if (article == null) {
            return Result.error(404, "文章不存在");
        }
        article.setIsTop(0);
        articleService.updateById(article);
        return Result.success("取消置顶成功", null);
    }
}
