-- 创建数据库
CREATE DATABASE IF NOT EXISTS `ai_agent` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

USE `ai_agent`;

-- 创建聊天会话表
CREATE TABLE IF NOT EXISTS `chat_conversation` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `conversationId` varchar(64) NOT NULL COMMENT '会话ID',
    `title` varchar(255) DEFAULT NULL COMMENT '会话标题',
    `userId` varchar(64) DEFAULT NULL COMMENT '用户ID',
    `modelType` varchar(32) NOT NULL COMMENT '模型类型',
    `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
    `isDelete` tinyint DEFAULT '0' NOT NULL COMMENT '是否删除',
    `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_conversation_id` (`conversationId`),
    KEY `idx_user_id` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='聊天会话表';

-- 创建聊天消息表
CREATE TABLE IF NOT EXISTS `chat_message` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `conversationId` varchar(64) NOT NULL COMMENT '会话ID',
    `content` text NOT NULL COMMENT '消息内容',
    `role` varchar(32) NOT NULL COMMENT '角色：user/assistant/system',
    `tokens` int DEFAULT '0' COMMENT '消息token数',
    `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete` tinyint DEFAULT '0' NOT NULL COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_conversation_id` (`conversationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='聊天消息表';

-- 创建API密钥表
CREATE TABLE IF NOT EXISTS `api_key` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `userId` varchar(64) NOT NULL COMMENT '用户ID',
    `apiKey` varchar(128) NOT NULL COMMENT 'API密钥',
    `name` varchar(64) NOT NULL COMMENT '密钥名称',
    `modelType` varchar(32) NOT NULL COMMENT '模型类型',
    `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
    `expireTime` datetime DEFAULT NULL COMMENT '过期时间',
    `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete` tinyint DEFAULT '0' NOT NULL COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_api_key` (`apiKey`),
    KEY `idx_user_id` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='API密钥表';

-- 创建系统配置表
CREATE TABLE IF NOT EXISTS `system_config` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `configKey` varchar(64) NOT NULL COMMENT '配置键',
    `configValue` text NOT NULL COMMENT '配置值',
    `description` varchar(255) DEFAULT NULL COMMENT '配置描述',
    `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete` tinyint DEFAULT '0' NOT NULL COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`configKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统配置表';
