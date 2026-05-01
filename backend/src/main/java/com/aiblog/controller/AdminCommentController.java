package com.aiblog.controller;

import com.aiblog.common.Result;
import com.aiblog.entity.Comment;
import com.aiblog.service.CommentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台评论管理控制器（评论审核）
 */
@RestController
@RequestMapping("/api/admin/comments")
public class AdminCommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 分页查询评论列表
     * @param pageNum 页码，默认1
     * @param pageSize 每页数量，默认10
     * @param status 评论状态（可选）：0-待审核，1-已通过，2-已拒绝
     * @return 分页评论数据
     */
    @GetMapping
    public Result<IPage<Comment>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Integer status) {
        IPage<Comment> page = commentService.getCommentPage(pageNum, pageSize, status);
        return Result.success(page);
    }

    /**
     * 审核评论（通过）
     * @param id 评论ID
     * @return 操作结果
     */
    @PutMapping("/{id}/approve")
    public Result<Void> approve(@PathVariable Long id) {
        commentService.approveComment(id);
        return Result.success("审核通过", null);
    }

    /**
     * 拒绝评论
     * @param id 评论ID
     * @return 操作结果
     */
    @PutMapping("/{id}/reject")
    public Result<Void> reject(@PathVariable Long id) {
        commentService.rejectComment(id);
        return Result.success("已拒绝", null);
    }

    /**
     * 删除评论
     * @param id 评论ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        commentService.deleteComment(id);
        return Result.success("删除成功", null);
    }
}
