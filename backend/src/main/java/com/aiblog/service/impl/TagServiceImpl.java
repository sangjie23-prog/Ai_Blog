package com.aiblog.service.impl;

import com.aiblog.entity.Tag;
import com.aiblog.mapper.TagMapper;
import com.aiblog.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 标签服务实现类
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public List<Tag> listWithArticleCount() {
        List<Tag> tags = list();
        // 填充文章数量
        tags.forEach(tag -> {
            Long count = baseMapper.countArticlesByTagId(tag.getId());
            tag.setArticleCount(count);
        });
        return tags;
    }

    @Override
    public Tag createTag(Tag tag) {
        save(tag);
        return tag;
    }

    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag existing = getById(id);
        if (existing == null) {
            throw new RuntimeException("标签不存在");
        }
        tag.setId(id);
        updateById(tag);
        return getById(id);
    }

    @Override
    public void deleteTag(Long id) {
        Tag existing = getById(id);
        if (existing == null) {
            throw new RuntimeException("标签不存在");
        }
        removeById(id);
    }
}
