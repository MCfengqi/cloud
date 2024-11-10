-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS cloudcity DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE cloudcity;

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '登录密码',
    email VARCHAR(100) NOT NULL COMMENT '电子邮箱',
    mobile VARCHAR(20) NOT NULL COMMENT '手机号码',
    is_admin TINYINT(1) DEFAULT 0 COMMENT '是否是管理员：0-普通用户，1-管理员',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    INDEX idx_username (username) COMMENT '用户名索引',
    INDEX idx_email (email) COMMENT '邮箱索引',
    INDEX idx_mobile (mobile) COMMENT '手机号索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表';

-- 创建数据库用户（如果不存在）并授权
CREATE USER IF NOT EXISTS 'cloudcity'@'localhost' IDENTIFIED BY 'cloudcity';
GRANT ALL PRIVILEGES ON cloudcity.* TO 'cloudcity'@'localhost';
FLUSH PRIVILEGES;

-- 插入一个默认管理员账号（可选）
INSERT INTO users (username, password, email, mobile, is_admin) 
VALUES ('admin', 'admin123', 'admin@cloudcity.com', '13800000000', 1)
ON DUPLICATE KEY UPDATE password='admin123';