/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3307
 Source Server Type    : MySQL
 Source Server Version : 80036
 Source Host           : localhost:3307
 Source Schema         : house_rental

 Target Server Type    : MySQL
 Target Server Version : 80036
 File Encoding         : 65001

 Date: 24/05/2025 09:28:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for appeal
-- ----------------------------
DROP TABLE IF EXISTS `appeal`;
CREATE TABLE `appeal`  (
  `appealid` int(0) NOT NULL AUTO_INCREMENT,
  `reportid` int(0) NOT NULL COMMENT '关联的举报ID',
  `houseid` int(0) NOT NULL,
  `appealer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '申诉人',
  `appealcontent` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '申诉内容',
  `appealdate` datetime(0) NULL DEFAULT NULL COMMENT '申诉时间',
  `status` enum('pending','approved','rejected') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'pending' COMMENT '申诉状态',
  `appealtype` enum('举报不实','已整改') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '举报不实' COMMENT '申诉类型',
  PRIMARY KEY (`appealid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of appeal
-- ----------------------------
INSERT INTO `appeal` VALUES (23, 68, 102, '房东宋城楠', '我已降低价格，不再欺骗消费者', '2025-03-21 18:08:34', 'approved', '已整改');

-- ----------------------------
-- Table structure for buyrequest
-- ----------------------------
DROP TABLE IF EXISTS `buyrequest`;
CREATE TABLE `buyrequest`  (
  `requestid` int(0) NOT NULL AUTO_INCREMENT,
  `houseid` int(0) NOT NULL,
  `buyer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `buyContent` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `requestDate` datetime(0) NULL DEFAULT NULL,
  `status` enum('pending','approved','rejected') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'pending',
  `landlordname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '房东名称',
  PRIMARY KEY (`requestid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of buyrequest
-- ----------------------------
INSERT INTO `buyrequest` VALUES (23, 103, '宋城楠', '我想买你的房子', '2025-03-21 17:49:39', 'approved', 'song2');

-- ----------------------------
-- Table structure for contract
-- ----------------------------
DROP TABLE IF EXISTS `contract`;
CREATE TABLE `contract`  (
  `contractid` int(0) NOT NULL AUTO_INCREMENT,
  `houseid` int(0) NOT NULL COMMENT '房屋ID',
  `buyer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '买家',
  `seller` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '卖家',
  `contractDate` datetime(0) NOT NULL COMMENT '合同日期',
  `status` enum('pending','approved','rejected') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'pending' COMMENT '合同状态',
  `order` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '用户申请内容',
  `adminstatus` enum('pending','approved','rejected') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'pending' COMMENT '管理员审核状态',
  PRIMARY KEY (`contractid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of contract
-- ----------------------------
INSERT INTO `contract` VALUES (17, 103, '宋城楠', 'song2', '2025-03-21 18:16:49', 'approved', '买家已付款，房产证：XXXXX', 'approved');

-- ----------------------------
-- Table structure for house
-- ----------------------------
DROP TABLE IF EXISTS `house`;
CREATE TABLE `house`  (
  `houseid` int(0) NOT NULL AUTO_INCREMENT COMMENT '出租单主键id',
  `statue` int(0) NULL DEFAULT 0 COMMENT '房屋状态：0-未出租，1-已出租，2-需整改',
  `area` int(0) NULL DEFAULT NULL COMMENT '房屋面积',
  `floor` int(0) NULL DEFAULT NULL COMMENT '房屋楼层',
  `price` int(0) NULL DEFAULT NULL COMMENT '房屋租金/月',
  `community` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '房子小区',
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '房屋地址',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '房屋房型',
  `toward` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '房屋朝向',
  `surrounding` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '房屋周围环境',
  `decoration` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '房屋装修',
  `housename` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '房屋出租人',
  `housephone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '房屋出租人电话',
  `tenantname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租客姓名',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片',
  PRIMARY KEY (`houseid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 104 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of house
-- ----------------------------
INSERT INTO `house` VALUES (102, 0, 100, 2, 800, '汇通小区', '江苏省南通市启东市', '两室一厅', '北', '靠近夜市', '普通装修', '房东宋城楠', '12345678956', '', '4c4fd9b7-57c3-42be-bdd0-4c6c18638a0d.jpg');
INSERT INTO `house` VALUES (103, 3, 114, 2, 1500, '汇景小区', '江苏省南通市崇川区', '两室一厅', '北', '离学校近', '普通装修', '宋城楠', '11451419198', '宋城楠', 'da25861d-9ed8-481e-bcc8-7e44d2ce30e8.jpg');

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `messageid` int(0) NOT NULL AUTO_INCREMENT,
  `messagename` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `messagetitle` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`messageid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message
-- ----------------------------
INSERT INTO `message` VALUES (1, 'admin', '欢迎使用本系统', '这是留言板', '2025-03-05 14:48:19');
INSERT INTO `message` VALUES (2, 'scn', '你好', '你好', '2025-03-05 14:49:51');
INSERT INTO `message` VALUES (6, 'song', '我是房东', '我这边有房', '2025-03-11 16:26:09');
INSERT INTO `message` VALUES (15, 'song2', '我这有房子', '如题', '2025-03-20 17:33:40');
INSERT INTO `message` VALUES (16, '房东宋城楠', '房子便宜', '诚信为先', '2025-03-20 17:36:24');
INSERT INTO `message` VALUES (17, '宋城楠', '我需要房子', '我想租房', '2025-03-20 17:43:48');

-- ----------------------------
-- Table structure for report
-- ----------------------------
DROP TABLE IF EXISTS `report`;
CREATE TABLE `report`  (
  `reportid` int(0) NOT NULL AUTO_INCREMENT,
  `reporter` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `reportee` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `reporttitle` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `reportcontent` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `reportdate` datetime(0) NULL DEFAULT NULL,
  `status` enum('pending','approved','rejected') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'pending',
  `houseid` int(0) NULL DEFAULT NULL COMMENT '被举报的房屋ID',
  PRIMARY KEY (`reportid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 69 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of report
-- ----------------------------
INSERT INTO `report` VALUES (68, '宋城楠', '房东宋城楠', '与实际租金不符', '恶意抬价', '2025-03-21 17:43:17', 'approved', 102);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `userid` int(0) NOT NULL AUTO_INCREMENT,
  `admin` int(0) NULL DEFAULT NULL COMMENT '是否是管理员',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户姓名\r\n',
  `password` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户密码',
  `number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户身份证号',
  `userphone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户手机号',
  PRIMARY KEY (`userid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (19, 1, 'admin', '123456', '333322114511451145', '12345678910');
INSERT INTO `user` VALUES (20, 2, 'scn', '114514', '114874121222222700', '12365478901');
INSERT INTO `user` VALUES (25, 3, 'song', '114514', '115521789000001170', '11144111444');
INSERT INTO `user` VALUES (29, 3, 'song2', '114514', '114514114511111100', '12196521234');
INSERT INTO `user` VALUES (30, 2, '宋城楠', '114514', '114514599111114510', '11451419198');
INSERT INTO `user` VALUES (31, 3, '房东宋城楠', '114514', '114514191981010101', '12345678956');

SET FOREIGN_KEY_CHECKS = 1;
