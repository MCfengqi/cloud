/*
 Navicat Premium Data Transfer

 Source Server         : CloudCity
 Source Server Type    : MySQL
 Source Server Version : 80037 (8.0.37)
 Source Host           : localhost:3306
 Source Schema         : cloudcity

 Target Server Type    : MySQL
 Target Server Version : 80037 (8.0.37)
 File Encoding         : 65001

 Date: 10/11/2024 17:10:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '登录密码',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '电子邮箱',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '手机号码',
  `is_admin` tinyint(1) NULL DEFAULT 0 COMMENT '是否是管理员：0-普通用户，1-管理员',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  INDEX `idx_username`(`username` ASC) USING BTREE COMMENT '用户名索引',
  INDEX `idx_email`(`email` ASC) USING BTREE COMMENT '邮箱索引',
  INDEX `idx_mobile`(`mobile` ASC) USING BTREE COMMENT '手机号索引'
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ---------------------------
INSERT INTO `users` VALUES (1, 'adminFQ', '123456', 'qxsj@vip.qq.com', '15529400971', 1, '2024-11-10 10:25:08', '2024-11-10 17:00:44');
INSERT INTO `users` VALUES (3, 'test', '123', 'mcfengqi123@2925.com', '15529400972', 0, '2024-11-10 11:10:48', '2024-11-10 16:44:33');
INSERT INTO `users` VALUES (5, '123456', '111111', 'mcfengqi123@2925.com', '15529400973', 1, '2024-11-10 12:49:12', '2024-11-10 16:43:32');
INSERT INTO `users` VALUES (10, 'admin', '123456', 'qxsj@vip.qq.com', '15529400972', 0, '2024-11-10 15:53:38', '2024-11-10 15:53:38');
INSERT INTO `users` VALUES (14, 'adminQF', '123456', 'mcfengqi123@2925.com', '15529400972', 1, '2024-11-10 16:44:53', '2024-11-10 16:44:53');

SET FOREIGN_KEY_CHECKS = 1;