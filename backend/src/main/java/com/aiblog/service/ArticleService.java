package com.aiblog.service;

import com.aiblog.entity.Article;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 文章服务接口
 */
public interface ArticleService extends IService<Article> {

    /**
     * 分页查询文章列表
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param status 文章状态（null表示查询全部）
     * @return 分页结果
     */
    IPage<Article> getArticlePage(int pageNum, int pageSize, Integer status);

    /**
     * 根据ID获取文章详情
     * @param id 文章ID
     * @return 文章信息
     */
    Article getArticleById(Long id);

    /**
     * 增加文章阅读量
     * @param id 文章ID
     */
    void incrementViewCount(Long id);

    /**
     * 增加文章点赞数
     * @param id 文章ID
     */
    void incrementLikeCount(Long id);
}
