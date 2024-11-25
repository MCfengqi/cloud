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

 Date: 25/11/2024 08:27:44
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
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'adminFQ', '123456', 'qxsj@vip.qq.com', '15529400971', 1, '2024-11-10 10:25:08', '2024-11-25 08:02:51');
INSERT INTO `users` VALUES (3, 'test', '123', 'mcfengqi123@2925.com', '15529400972', 0, '2024-11-10 11:10:48', '2024-11-10 16:44:33');
INSERT INTO `users` VALUES (5, '123456', '111111', 'mcfengqi123@2925.com', '15529400973', 1, '2024-11-10 12:49:12', '2024-11-10 16:43:32');
INSERT INTO `users` VALUES (14, 'adminQF', '123456', 'mcfengqi123@2925.com', '15529400972', 1, '2024-11-10 16:44:53', '2024-11-10 16:44:53');
INSERT INTO `users` VALUES (18, 'yonghu123', '123456', 'qxsj@vip.qq.com', '15529400972', 1, '2024-11-19 20:49:33', '2024-11-19 20:49:33');
INSERT INTO `users` VALUES (19, '666666', '123456', 'q@qq.com', 'q@qq.com', 0, '2024-11-19 20:50:20', '2024-11-19 20:50:20');
INSERT INTO `users` VALUES (20, 'q@qq.com', 'q@qq.com', 'q@qq.com', 'q@qq.com', 0, '2024-11-19 20:50:39', '2024-11-19 20:50:39');
INSERT INTO `users` VALUES (21, 'admin1', '123456', 'admin1@qq.cpm', 'admin1', 1, '2024-11-19 20:58:02', '2024-11-19 21:23:45');
INSERT INTO `users` VALUES (22, 'yonghu1', 'yonghu1', 'yonghu1@qq.com', 'yonghu1', 0, '2024-11-19 21:23:09', '2024-11-19 21:23:09');
INSERT INTO `users` VALUES (24, 'admini222', '123456', 'yonghu1@qq.com', 'yonghu1@qq.com', 1, '2024-11-19 21:23:37', '2024-11-19 21:23:37');

SET FOREIGN_KEY_CHECKS = 1;
