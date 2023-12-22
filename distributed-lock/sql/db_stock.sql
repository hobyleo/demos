/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50736 (5.7.36)
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50736 (5.7.36)
 File Encoding         : 65001

 Date: 27/10/2023 09:09:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for db_stock
-- ----------------------------
DROP TABLE IF EXISTS `db_stock`;
CREATE TABLE `db_stock`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品编号',
  `stock_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '仓库编号',
  `count` int(11) NOT NULL COMMENT '库存量',
  `version` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product_code`(`product_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of db_stock
-- ----------------------------
INSERT INTO `db_stock` VALUES (1, '1001', '北京仓', 0, 0);
INSERT INTO `db_stock` VALUES (2, '1001', '深圳仓', 4997, 0);
INSERT INTO `db_stock` VALUES (3, '1002', '北京仓', 4997, 0);
INSERT INTO `db_stock` VALUES (4, '1002', '上海仓', 4997, 0);
INSERT INTO `db_stock` VALUES (5, '1003', '广州仓', 4999, 0);

SET FOREIGN_KEY_CHECKS = 1;
