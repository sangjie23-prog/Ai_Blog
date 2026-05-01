package com.aiblog.controller;

import com.aiblog.common.Result;
import com.aiblog.entity.Tag;
import com.aiblog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台标签管理控制器
 */
@RestController
@RequestMapping("/api/admin/tags")
public class AdminTagController {

    @Autowired
    private TagService tagService;

    /**
     * 获取标签列表（含文章数量）
     */
    @GetMapping
    public Result<List<Tag>> list() {
        List<Tag> tags = tagService.listWithArticleCount();
        return Result.success(tags);
    }

    /**
     * 获取标签详情
     */
    @GetMapping("/{id}")
    public Result<Tag> getById(@PathVariable Long id) {
        Tag tag = tagService.getById(id);
        if (tag == null) {
            return Result.error(404, "标签不存在");
        }
        return Result.success(tag);
    }

    /**
     * 创建标签
     */
    @PostMapping
    public Result<Tag> create(@RequestBody Tag tag) {
        Tag created = tagService.createTag(tag);
        return Result.success("创建成功", created);
    }

    /**
     * 更新标签
     */
    @PutMapping("/{id}")
    public Result<Tag> update(@PathVariable Long id, @RequestBody Tag tag) {
        Tag updated = tagService.updateTag(id, tag);
        return Result.success("更新成功", updated);
    }

    /**
     * 删除标签
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        tagService.deleteTag(id);
        return Result.success("删除成功", null);
    }
}
