package com.aiblog.mapper;

import com.aiblog.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 标签Mapper接口
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 统计标签下的文章数量
     * @param tagId 标签ID
     * @return 文章数量
     */
    @Select("SELECT COUNT(*) FROM article WHERE FIND_IN_SET(#{tagId}, tags) > 0 AND deleted = 0 AND status = 1")
    Long countArticlesByTagId(@Param("tagId") Long tagId);
}
