-- 为文章表添加 is_top 字段（用于已存在的数据库）
ALTER TABLE `article` ADD COLUMN `is_top` TINYINT NOT NULL DEFAULT 0 COMMENT '是否置顶：0-否，1-是' AFTER `status`;

-- 为 is_top 字段添加索引以提升查询性能
ALTER TABLE `article` ADD INDEX `idx_is_top` (`is_top`);
