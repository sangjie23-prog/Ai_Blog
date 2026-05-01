package com.aiblog.controller;

import com.aiblog.common.Result;
import com.aiblog.entity.Article;
import com.aiblog.service.StatisticsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 统计控制器
 * 提供系统统计数据、热门文章等接口
 */
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 获取系统统计数据
     * @return 统计数据
     */
    @GetMapping("/system")
    public Result<Map<String, Object>> getSystemStatistics() {
        Map<String, Object> statistics = statisticsService.getSystemStatistics();
        return Result.success(statistics);
    }

    /**
     * 获取热门文章列表
     * @param limit 数量限制，默认10
     * @return 热门文章列表
     */
    @GetMapping("/hot")
    public Result<List<Article>> getHotArticles(
            @RequestParam(defaultValue = "10") int limit) {
        List<Article> articles = statisticsService.getHotArticles(limit);
        return Result.success(articles);
    }

    /**
     * 获取文章分页列表（按阅读量排序）
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    @GetMapping("/articles")
    public Result<IPage<Article>> getArticlesByViewCount(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        IPage<Article> page = statisticsService.getArticlesByViewCount(pageNum, pageSize);
        return Result.success(page);
    }
}
