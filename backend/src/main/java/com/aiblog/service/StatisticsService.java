package com.aiblog.service;

import com.aiblog.entity.Article;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * 统计服务接口
 */
public interface StatisticsService {

    /**
     * 获取系统统计数据
     * @return 统计数据（文章总数、总阅读量、总点赞数、总评论数）
     */
    Map<String, Object> getSystemStatistics();

    /**
     * 获取热门文章列表（按阅读量排序）
     * @param limit 数量限制
     * @return 热门文章列表
     */
    List<Article> getHotArticles(int limit);

    /**
     * 获取文章分页列表（按阅读量排序）
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 分页结果
     */
    IPage<Article> getArticlesByViewCount(int pageNum, int pageSize);
}
