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

 Date: 09/12/2024 07:50:40
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
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '游戏列表信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gamelist
-- ----------------------------
INSERT INTO `gamelist` VALUES (12, '敢敢和奶龙', 'https://img.mcfengqi.icu/LightPicture/2024/12/31538f2058e5928a.jpg', '这是敢敢，首席宣传大使', 'https://img.mcfengqi.icu/LightPicture/2024/12/31538f2058e5928a.jpg', '2024-11-26 22:28:17', '2024-12-06 11:35:19');
INSERT INTO `gamelist` VALUES (13, '小肚小肚', 'https://img.mcfengqi.icu/LightPicture/2024/12/47192fae4356cb49.png', '这是小肚小肚', 'https://img.mcfengqi.icu/LightPicture/2024/12/47192fae4356cb49.png', '2024-11-26 22:36:57', '2024-12-06 11:51:52');
INSERT INTO `gamelist` VALUES (14, '苗', 'https://img.mcfengqi.icu/LightPicture/2024/12/ad81b155043e66ad.jpg', '这是苗', 'https://img.mcfengqi.icu/LightPicture/2024/12/ad81b155043e66ad.jpg', '2024-11-27 00:40:18', '2024-12-06 11:42:57');
INSERT INTO `gamelist` VALUES (15, '糕糕', 'https://img.mcfengqi.icu/LightPicture/2024/12/989d786430a14afa.jpg', '这是糕糕', 'https://img.mcfengqi.icu/LightPicture/2024/12/989d786430a14afa.jpg', '2024-11-27 13:55:36', '2024-12-06 11:33:03');
INSERT INTO `gamelist` VALUES (16, '宋小睿', 'https://img.mcfengqi.icu/LightPicture/2024/12/d3b164437abf8e16.jpg', '这是宋小睿', 'https://img.mcfengqi.icu/LightPicture/2024/11/7539d5c023a9d7b9.jpg', '2024-11-27 20:10:28', '2024-12-08 21:01:16');
INSERT INTO `gamelist` VALUES (17, 'MC风启', 'https://img.mcfengqi.icu/LightPicture/2024/11/6066acbf4e8f949f.jpg', '这是一个介绍', 'https://img.mcfengqi.icu/LightPicture/2024/11/5577de0a24c7a2a4.jpg', '2024-11-27 20:10:28', '2024-12-06 11:26:19');

SET FOREIGN_KEY_CHECKS = 1;
