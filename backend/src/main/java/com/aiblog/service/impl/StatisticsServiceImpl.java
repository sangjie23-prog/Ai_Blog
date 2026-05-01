package com.aiblog.service.impl;

import com.aiblog.entity.Article;
import com.aiblog.entity.Comment;
import com.aiblog.mapper.ArticleMapper;
import com.aiblog.mapper.CommentMapper;
import com.aiblog.service.StatisticsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计服务实现类
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public Map<String, Object> getSystemStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // 文章总数
        long totalArticles = articleMapper.selectCount(new LambdaQueryWrapper<Article>().eq(Article::getDeleted, 0));
        statistics.put("totalArticles", totalArticles);

        // 总阅读量
        List<Article> allArticles = articleMapper.selectList(new LambdaQueryWrapper<Article>().eq(Article::getDeleted, 0));
        long totalViews = allArticles.stream().mapToLong(a -> a.getViewCount() != null ? a.getViewCount() : 0).sum();
        statistics.put("totalViews", totalViews);

        // 总点赞数
        long totalLikes = allArticles.stream().mapToLong(a -> a.getLikeCount() != null ? a.getLikeCount() : 0).sum();
        statistics.put("totalLikes", totalLikes);

        // 总评论数
        long totalComments = commentMapper.selectCount(null);
        statistics.put("totalComments", totalComments);

        return statistics;
    }

    @Override
    public List<Article> getHotArticles(int limit) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getDeleted, 0)
               .eq(Article::getStatus, 1)
               .orderByDesc(Article::getViewCount)
               .last("LIMIT " + limit);
        return articleMapper.selectList(wrapper);
    }

    @Override
    public IPage<Article> getArticlesByViewCount(int pageNum, int pageSize) {
        Page<Article> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getDeleted, 0)
               .eq(Article::getStatus, 1)
               .orderByDesc(Article::getViewCount);
        return articleMapper.selectPage(page, wrapper);
    }
}
