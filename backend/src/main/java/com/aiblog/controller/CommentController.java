package com.aiblog.controller;

import com.aiblog.common.Result;
import com.aiblog.entity.Comment;
import com.aiblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前台评论控制器（游客评论）
 */
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 获取文章已审核评论列表
     * @param articleId 文章ID
     * @return 评论列表
     */
    @GetMapping
    public Result<List<Comment>> list(@RequestParam Long articleId) {
        List<Comment> comments = commentService.getApprovedCommentsByArticleId(articleId);
        return Result.success(comments);
    }

    /**
     * 提交评论（游客）
     * @param comment 评论信息
     * @return 提交结果
     */
    @PostMapping
    public Result<Comment> submit(@RequestBody Comment comment) {
        Comment result = commentService.submitComment(comment);
        return Result.success("评论已提交，等待审核", result);
    }
}
