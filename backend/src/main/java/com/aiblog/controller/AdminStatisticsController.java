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
 * 后台统计控制器
 * 提供后台管理系统的统计数据接口
 */
@RestController
@RequestMapping("/api/admin/statistics")
public class AdminStatisticsController {

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
}
