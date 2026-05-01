package com.aiblog.service.impl;

import com.aiblog.entity.Comment;
import com.aiblog.mapper.CommentMapper;
import com.aiblog.service.CommentService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论服务实现类
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Override
    public List<Comment> getApprovedCommentsByArticleId(Long articleId) {
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getArticleId, articleId)
               .eq(Comment::getStatus, 1)
               .orderByAsc(Comment::getCreatedAt);
        return list(wrapper);
    }

    @Override
    public IPage<Comment> getCommentPage(int pageNum, int pageSize, Integer status) {
        Page<Comment> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(status != null, Comment::getStatus, status)
               .orderByDesc(Comment::getCreatedAt);
        return page(page, wrapper);
    }

    @Override
    public Comment submitComment(Comment comment) {
        if (comment.getStatus() == null) {
            comment.setStatus(0);
        }
        save(comment);
        return comment;
    }

    @Override
    public void approveComment(Long id) {
        Comment comment = getById(id);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }
        comment.setStatus(1);
        updateById(comment);
    }

    @Override
    public void rejectComment(Long id) {
        Comment comment = getById(id);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }
        comment.setStatus(2);
        updateById(comment);
    }

    @Override
    public void deleteComment(Long id) {
        removeById(id);
    }
}
