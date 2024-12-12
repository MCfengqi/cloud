/*
 Navicat Premium Dump SQL

 Source Server         : 本地连接
 Source Server Type    : MySQL
 Source Server Version : 80036 (8.0.36)
 Source Host           : localhost:3306
 Source Schema         : cloudcity

 Target Server Type    : MySQL
 Target Server Version : 80036 (8.0.36)
 File Encoding         : 65001

 Date: 12/12/2024 10:05:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for operation_logs
-- ----------------------------
DROP TABLE IF EXISTS `operation_logs`;
CREATE TABLE `operation_logs`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `operation_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作类型',
  `operation_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '操作内容',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作用户名',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作IP地址',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `result` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作结果',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE COMMENT '创建时间索引',
  INDEX `idx_username`(`username` ASC) USING BTREE COMMENT '用户名索引'
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of operation_logs
-- ----------------------------
INSERT INTO `operation_logs` VALUES (1, 'LOGIN', '用户 adminFQ 登录系统', 'adminFQ', '0:0:0:0:0:0:0:1', '2024-12-12 09:15:01', 'SUCCESS');
INSERT INTO `operation_logs` VALUES (2, 'LOGIN', '用户 adminFQ 登录系统', 'adminFQ', '0:0:0:0:0:0:0:1', '2024-12-12 09:18:35', 'SUCCESS');
INSERT INTO `operation_logs` VALUES (3, '登录', '用户 adminFQ 登录系统', 'adminFQ', '0:0:0:0:0:0:0:1', '2024-12-12 09:23:17', '成功');
INSERT INTO `operation_logs` VALUES (4, '登录', '用户 adminFQ 登录系统', 'adminFQ', '0:0:0:0:0:0:0:1', '2024-12-12 09:27:54', '成功');
INSERT INTO `operation_logs` VALUES (5, '登录', '用户 adminFQ 登录系统', 'adminFQ', '0:0:0:0:0:0:0:1', '2024-12-12 09:32:44', '成功');
INSERT INTO `operation_logs` VALUES (6, '新增', '新增普通用户：23r3543425', 'adminFQ', '0:0:0:0:0:0:0:1', '2024-12-12 09:33:00', '成功');
INSERT INTO `operation_logs` VALUES (7, '登录', '用户 adminFQ 登录系统', 'adminFQ', '0:0:0:0:0:0:0:1', '2024-12-12 09:35:51', '成功');
INSERT INTO `operation_logs` VALUES (8, '登录', '用户 adminFQ 登录系统', 'adminFQ', '0:0:0:0:0:0:0:1', '2024-12-12 09:37:21', '成功');
INSERT INTO `operation_logs` VALUES (9, '登录', '用户 adminFQ 登录系统', 'adminFQ', '0:0:0:0:0:0:0:1', '2024-12-12 09:38:52', '成功');
INSERT INTO `operation_logs` VALUES (10, '登录', '用户 adminFQ 登录系统', 'adminFQ', '0:0:0:0:0:0:0:1', '2024-12-12 09:40:33', '成功');
INSERT INTO `operation_logs` VALUES (11, '登录', '用户 adminFQ 登录系统', 'adminFQ', '0:0:0:0:0:0:0:1', '2024-12-12 09:42:35', '成功');
INSERT INTO `operation_logs` VALUES (12, '登录', '用户 adminFQ 登录系统', 'adminFQ', '0:0:0:0:0:0:0:1', '2024-12-12 09:44:00', '成功');
INSERT INTO `operation_logs` VALUES (13, '登录', '用户 adminFQ 登录系统', 'adminFQ', '0:0:0:0:0:0:0:1', '2024-12-12 09:45:41', '成功');
INSERT INTO `operation_logs` VALUES (14, '登录', '用户 adminFQ 登录系统', 'adminFQ', '0:0:0:0:0:0:0:1', '2024-12-12 09:46:18', '成功');
INSERT INTO `operation_logs` VALUES (15, '登录', '用户 adminFQ 登录系统', 'adminFQ', '0:0:0:0:0:0:0:1', '2024-12-12 09:49:15', '成功');
INSERT INTO `operation_logs` VALUES (16, '登录', '用户 adminFQ 登录系统', 'adminFQ', '0:0:0:0:0:0:0:1', '2024-12-12 09:49:45', '成功');
INSERT INTO `operation_logs` VALUES (17, '登录', '用户 adminFQ 登录系统', 'adminFQ', '0:0:0:0:0:0:0:1', '2024-12-12 09:54:41', '成功');

SET FOREIGN_KEY_CHECKS = 1;
