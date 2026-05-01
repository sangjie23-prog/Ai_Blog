package com.aiblog.mapper;

import com.aiblog.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 分类Mapper接口
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 统计分类下的文章数量
     * @param categoryId 分类ID
     * @return 文章数量
     */
    @Select("SELECT COUNT(*) FROM article WHERE category_id = #{categoryId} AND deleted = 0 AND status = 1")
    Long countArticlesByCategoryId(@Param("categoryId") Long categoryId);
}
