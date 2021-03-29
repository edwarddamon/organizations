/*
 Navicat Premium Data Transfer

 Source Server         : aliyun
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : 39.106.206.167:3306
 Source Schema         : organizations

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 29/03/2021 15:07:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for org_activity
-- ----------------------------
DROP TABLE IF EXISTS `org_activity`;
CREATE TABLE `org_activity`  (
                                 `act_id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `act_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '活动名称',
                                 `act_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面地址',
                                 `act_begin_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
                                 `act_end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
                                 `act_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '活动地址',
                                 `act_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '活动内容',
                                 `act_views` int(11) NULL DEFAULT NULL COMMENT '浏览量',
                                 `act_organization_id` bigint(20) NULL DEFAULT NULL COMMENT '社团id',
                                 `act_limit_user_id` bigint(20) NOT NULL COMMENT '限制id',
                                 `act_funds` bigint(20) NULL DEFAULT NULL COMMENT '需要的经费',
                                 `create_at` datetime(0) NULL DEFAULT NULL,
                                 `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                 `update_at` datetime(0) NULL DEFAULT NULL,
                                 `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                 PRIMARY KEY (`act_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org_application
-- ----------------------------
DROP TABLE IF EXISTS `org_application`;
CREATE TABLE `org_application`  (
                                    `app_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '入社申请id',
                                    `app_status` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请状态（undetermined、AGREE、REFUSE）',
                                    `app_application_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请理由',
                                    `app_refuse_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '拒绝理由',
                                    `app_org_id` bigint(20) NULL DEFAULT NULL COMMENT '社团id',
                                    `app_user_id` bigint(20) NULL DEFAULT NULL COMMENT '申请用户id',
                                    `create_at` datetime(0) NULL DEFAULT NULL,
                                    `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                    `update_at` datetime(0) NULL DEFAULT NULL,
                                    `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                    PRIMARY KEY (`app_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org_boards
-- ----------------------------
DROP TABLE IF EXISTS `org_boards`;
CREATE TABLE `org_boards`  (
                               `boa_id` bigint(20) NOT NULL AUTO_INCREMENT,
                               `boa_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面地址',
                               `boa_content` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公告内容',
                               `boa_views` int(11) NULL DEFAULT NULL COMMENT '已读人数',
                               `boa_user_id` bigint(20) NULL DEFAULT NULL COMMENT '发布者id',
                               `boa_org_id` bigint(20) NULL DEFAULT NULL COMMENT '社团id',
                               `create_at` datetime(0) NULL DEFAULT NULL,
                               `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                               `update_at` datetime(0) NULL DEFAULT NULL,
                               `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                               PRIMARY KEY (`boa_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org_comments
-- ----------------------------
DROP TABLE IF EXISTS `org_comments`;
CREATE TABLE `org_comments`  (
                                 `com_id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `com_content` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评论内容',
                                 `com_good` int(11) NULL DEFAULT NULL COMMENT '点赞数',
                                 `com_user_id` bigint(20) NULL DEFAULT NULL COMMENT '发布者id',
                                 `com_target_id` bigint(20) NULL DEFAULT NULL COMMENT '目标用户id',
                                 `com_news_id` bigint(20) NULL DEFAULT NULL COMMENT '新闻id',
                                 `create_at` datetime(0) NULL DEFAULT NULL,
                                 `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                 `update_at` datetime(0) NULL DEFAULT NULL,
                                 `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                 PRIMARY KEY (`com_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org_department
-- ----------------------------
DROP TABLE IF EXISTS `org_department`;
CREATE TABLE `org_department`  (
                                   `dep_id` bigint(20) NOT NULL AUTO_INCREMENT,
                                   `dep_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '部门名',
                                   `dep_minister_id` bigint(20) NULL DEFAULT NULL COMMENT '部长id',
                                   `dep_vice_minister_id` bigint(20) NULL DEFAULT NULL COMMENT '副部长id',
                                   `dep_organization_id` bigint(20) NULL DEFAULT NULL COMMENT '社团id',
                                   `create_at` datetime(0) NULL DEFAULT NULL,
                                   `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                   `update_at` datetime(0) NULL DEFAULT NULL,
                                   `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                   PRIMARY KEY (`dep_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 76 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org_limit
-- ----------------------------
DROP TABLE IF EXISTS `org_limit`;
CREATE TABLE `org_limit`  (
                              `lim_id` bigint(20) NOT NULL AUTO_INCREMENT,
                              `lim_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '限制名称',
                              PRIMARY KEY (`lim_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of org_limit
-- ----------------------------
INSERT INTO `org_limit` VALUES (1, '本社团');
INSERT INTO `org_limit` VALUES (3, '所有社团成员');
INSERT INTO `org_limit` VALUES (4, '全校学生');

-- ----------------------------
-- Table structure for org_message
-- ----------------------------
DROP TABLE IF EXISTS `org_message`;
CREATE TABLE `org_message`  (
                                `mes_id` bigint(20) NOT NULL AUTO_INCREMENT,
                                `mes_content` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '消息内容',
                                `mes_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '消息状态（UNREAD、READ、DELETED）',
                                `mes_user_id` bigint(20) NULL DEFAULT NULL COMMENT '发布通知者id',
                                `mes_target_id` bigint(20) NULL DEFAULT NULL COMMENT '被通知者id',
                                `create_at` datetime(0) NULL DEFAULT NULL,
                                `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                `update_at` datetime(0) NULL DEFAULT NULL,
                                `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                PRIMARY KEY (`mes_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 293 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org_news
-- ----------------------------
DROP TABLE IF EXISTS `org_news`;
CREATE TABLE `org_news`  (
                             `new_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '新闻id',
                             `new_title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '新闻标题',
                             `new_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '新闻封面地址',
                             `new_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '新闻内容',
                             `new_views` int(11) NULL DEFAULT NULL COMMENT '新闻浏览量',
                             `new_organization_id` bigint(20) NULL DEFAULT NULL COMMENT '社团id',
                             `create_at` datetime(0) NULL DEFAULT NULL,
                             `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                             `update_at` datetime(0) NULL DEFAULT NULL,
                             `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                             PRIMARY KEY (`new_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org_organization
-- ----------------------------
DROP TABLE IF EXISTS `org_organization`;
CREATE TABLE `org_organization`  (
                                     `organ_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '社团id',
                                     `organ_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '社团封面地址',
                                     `organ_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '社团名',
                                     `organ_funds` bigint(20) NULL DEFAULT NULL COMMENT '社费',
                                     `organ_introduction` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '社团简介',
                                     `organ_introduction_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '社团详细介绍',
                                     `organ_introduction_detail_avatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '社团详细介绍图片地址（数组）',
                                     `organ_star` int(11) NULL DEFAULT NULL COMMENT '社团星级（3-5对应3星-5星）',
                                     `organ_status` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '社团状态（dead:申请创建失败、undetermined\n\n:申请创建待审批、normal:正常、cancel:申请注销待审批、cancelled:已经注销）',
                                     `organ_cancel_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '社团申请注销理由',
                                     `organ_refuse_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '拒绝申请理由',
                                     `create_at` datetime(0) NULL DEFAULT NULL,
                                     `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                     `update_at` datetime(0) NULL DEFAULT NULL,
                                     `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                     PRIMARY KEY (`organ_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org_role
-- ----------------------------
DROP TABLE IF EXISTS `org_role`;
CREATE TABLE `org_role`  (
                             `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色id',
                             `role_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色名称',
                             `create_at` datetime(0) NULL DEFAULT NULL,
                             `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                             `update_at` datetime(0) NULL DEFAULT NULL,
                             `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                             PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of org_role
-- ----------------------------
INSERT INTO `org_role` VALUES (1, '普通学生（非社员）', '2021-03-09 21:39:40', 'edward', '2021-03-09 21:39:44', 'edward');
INSERT INTO `org_role` VALUES (2, '社员', '2021-03-09 21:39:59', 'edward', '2021-03-09 21:40:03', 'edward');
INSERT INTO `org_role` VALUES (3, '社联管理员', '2021-03-09 21:40:13', 'edward', '2021-03-09 21:40:17', 'edward');
INSERT INTO `org_role` VALUES (4, '社联主席', '2021-03-09 21:40:27', 'edward', '2021-03-09 21:40:32', 'edward');

-- ----------------------------
-- Table structure for org_trans
-- ----------------------------
DROP TABLE IF EXISTS `org_trans`;
CREATE TABLE `org_trans`  (
                              `tra_id` bigint(20) NOT NULL AUTO_INCREMENT,
                              `tra_change_amount` bigint(20) NULL DEFAULT NULL COMMENT '变动金额',
                              `tra_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '变动原因',
                              `tra_amount` bigint(20) NULL DEFAULT NULL COMMENT '当前余额',
                              `tra_org_id` bigint(20) NULL DEFAULT NULL COMMENT '社团id',
                              `create_at` datetime(0) NULL DEFAULT NULL,
                              `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                              `update_at` datetime(0) NULL DEFAULT NULL,
                              `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                              PRIMARY KEY (`tra_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org_user
-- ----------------------------
DROP TABLE IF EXISTS `org_user`;
CREATE TABLE `org_user`  (
                             `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
                             `user_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像地址',
                             `user_username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
                             `user_sex` int(11) NULL DEFAULT NULL COMMENT '用户性别',
                             `user_password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
                             `user_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
                             `user_qq` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'QQ号',
                             `user_vx` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信',
                             `user_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户状态（NORMAL正常，CANCEL已注销）',
                             `create_at` datetime(0) NULL DEFAULT NULL,
                             `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                             `update_at` datetime(0) NULL DEFAULT NULL,
                             `update_by` varchar(0) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                             PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of org_user
-- ----------------------------
INSERT INTO `org_user` VALUES (8, 'https://lhamster-organizations-1302533254.cos.ap-nanjing.myqcloud.com/avatar/c066c718e19d465195406fac347bccc2.jpg', 'edward', 1, '131420', '13611504894', '2810315114', 'damon_edward', 'NORMAL', '2021-03-11 19:18:03', NULL, '2021-03-26 23:46:35', NULL);

-- ----------------------------
-- Table structure for org_user_organization_rel
-- ----------------------------
DROP TABLE IF EXISTS `org_user_organization_rel`;
CREATE TABLE `org_user_organization_rel`  (
                                              `rel_id` bigint(20) NOT NULL AUTO_INCREMENT,
                                              `rel_user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
                                              `rel_organization_id` bigint(20) NULL DEFAULT NULL COMMENT '社团id',
                                              `rel_role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色id',
                                              `create_at` datetime(0) NULL DEFAULT NULL,
                                              `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                              `update_at` datetime(0) NULL DEFAULT NULL,
                                              `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                              PRIMARY KEY (`rel_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org_user_role_rel
-- ----------------------------
DROP TABLE IF EXISTS `org_user_role_rel`;
CREATE TABLE `org_user_role_rel`  (
                                      `rel_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关系表id',
                                      `rel_user_id` bigint(20) NULL DEFAULT NULL,
                                      `rel_role_id` bigint(20) NULL DEFAULT NULL,
                                      `create_at` datetime(0) NULL DEFAULT NULL,
                                      `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                      `update_at` datetime(0) NULL DEFAULT NULL,
                                      `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                      PRIMARY KEY (`rel_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of org_user_role_rel
-- ----------------------------
INSERT INTO `org_user_role_rel` VALUES (18, 8, 4, '2021-03-29 13:56:48', NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
