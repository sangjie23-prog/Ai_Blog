package com.aiblog.service;

import com.aiblog.entity.Comment;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 评论服务接口
 */
public interface CommentService extends IService<Comment> {

    /**
     * 获取文章已审核评论列表
     * @param articleId 文章ID
     * @return 评论列表
     */
    List<Comment> getApprovedCommentsByArticleId(Long articleId);

    /**
     * 分页获取评论列表（后台管理）
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param status 状态筛选（可选）
     * @return 分页结果
     */
    IPage<Comment> getCommentPage(int pageNum, int pageSize, Integer status);

    /**
     * 提交评论（游客）
     * @param comment 评论信息
     * @return 评论结果
     */
    Comment submitComment(Comment comment);

    /**
     * 审核评论（通过）
     * @param id 评论ID
     */
    void approveComment(Long id);

    /**
     * 拒绝评论
     * @param id 评论ID
     */
    void rejectComment(Long id);

    /**
     * 删除评论
     * @param id 评论ID
     */
    void deleteComment(Long id);
}
