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

 Date: 25/11/2024 08:23:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gamelist
-- ----------------------------
DROP TABLE IF EXISTS `gamelist`;
CREATE TABLE `gamelist`  (
  `gameid` bigint NOT NULL AUTO_INCREMENT COMMENT '游戏内容ID',
  `gamename` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '游戏内容名',
  `gameimg` varchar(6535) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '游戏图片路径',
  `gametxt` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '游戏内容简介',
  `gamelink` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '游戏下载链接',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`gameid`) USING BTREE,
  INDEX `idx_gamename`(`gamename` ASC) USING BTREE COMMENT '游戏名称索引'
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '游戏列表信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gamelist
-- ----------------------------
INSERT INTO `gamelist` VALUES (4, '测试游戏', 'game_images/1732017249501_Image_70188365081349.jpg', 'ceshi', 'https://www.baidu.com', '2024-11-19 19:54:10', '2024-11-19 19:54:10');
INSERT INTO `gamelist` VALUES (5, '测试游戏1', 'game_images/1732017445064_Image_70188365081349.jpg', 'https://www.baidu.com', 'https://www.baidu.com', '2024-11-19 19:57:25', '2024-11-19 19:57:25');
INSERT INTO `gamelist` VALUES (6, '测试游戏2', 'game_images/1732017458795_Image_70188365081349.jpg', 'https://www.baidu.com', 'https://www.baidu.com', '2024-11-19 19:57:38', '2024-11-19 19:57:38');
INSERT INTO `gamelist` VALUES (7, 'https://www.baidu.com', 'game_images/1732017474564_Image_70188365081349.jpg', 'https://www.baidu.com', 'https://www.baidu.com', '2024-11-19 19:57:54', '2024-11-19 19:57:54');
INSERT INTO `gamelist` VALUES (8, 'https://www.baidu.com', 'game_images/1732017485824_Image_70188365081349.jpg', 'https://www.baidu.com', 'https://www.baidu.com', '2024-11-19 19:58:05', '2024-11-19 19:58:05');
INSERT INTO `gamelist` VALUES (9, 'gta5', 'game_images/1732018630903_R-C.jpg', '朱哲宇', 'http://cdn.u1.huluxia.com/g4/M01/80/54/rBAAdmc_7pqAJGCmAAGUks7-5H0489.jpg', '2024-11-19 20:17:11', '2024-11-22 10:38:45');

SET FOREIGN_KEY_CHECKS = 1;
