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

 Date: 18/12/2024 10:09:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for game_order
-- ----------------------------
DROP TABLE IF EXISTS `game_order`;
CREATE TABLE `game_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `total` decimal(10, 2) NOT NULL COMMENT '订单总金额',
  `amount` decimal(10, 2) NOT NULL COMMENT '实际支付金额',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单状态',
  `paytype` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付方式',
  `gamename` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '游戏名\r\n',
  `gameimg` varchar(6535) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '游戏照片',
  `gametxt` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '游戏介绍',
  `datetime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '订单创建时间',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '游戏订单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of game_order
-- ----------------------------
INSERT INTO `game_order` VALUES (1, 196.96, 196.96, '已支付', 'alipay', '测试用户', '13800138000', '测试地址', '2024-12-18 09:06:47', 1);
INSERT INTO `game_order` VALUES (2, 196.96, 196.96, '已支付', 'alipay', '测试用户', '13800138000', '测试地址', '2024-12-18 09:08:51', 1);
INSERT INTO `game_order` VALUES (3, 196.96, 196.96, '已支付', 'alipay', '测试用户', '13800138000', '测试地址', '2024-12-18 09:09:41', 1);

SET FOREIGN_KEY_CHECKS = 1;
