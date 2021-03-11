/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80013
 Source Host           : localhost:3306
 Source Schema         : organizations

 Target Server Type    : MySQL
 Target Server Version : 80013
 File Encoding         : 65001

 Date: 09/03/2021 12:52:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for org_activity
-- ----------------------------
DROP TABLE IF EXISTS `org_activity`;
CREATE TABLE `org_activity`
(
    `act_id`              bigint(20)                                                    NOT NULL AUTO_INCREMENT,
    `act_name`            varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '活动名称',
    `act_avatar`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面地址',
    `act_begin_time`      datetime(0)                                                   NULL DEFAULT NULL COMMENT '开始时间',
    `act_end_time`        datetime(0)                                                   NULL DEFAULT NULL COMMENT '结束时间',
    `act_address`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '活动地址',
    `act_content`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         NULL COMMENT '活动内容',
    `act_views`           int(11)                                                       NULL DEFAULT NULL COMMENT '浏览量',
    `act_organization_id` bigint(20)                                                    NULL DEFAULT NULL COMMENT '社团id',
    `act_limit_user_id`   bigint(20) UNSIGNED ZEROFILL                                  NULL DEFAULT NULL COMMENT '人员限制id',
    `act_limit_org_ids`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '多个社团联和举办时存储可参加社团的id',
    `create_at`           datetime(0)                                                   NULL DEFAULT NULL,
    `create_by`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `update_at`           datetime(0)                                                   NULL DEFAULT NULL,
    `update_by`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    PRIMARY KEY (`act_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org_application
-- ----------------------------
DROP TABLE IF EXISTS `org_application`;
CREATE TABLE `org_application`
(
    `app_id`                 bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '入社申请id',
    `app_status`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '申请状态（undetermined、AGREE、REFUSE）',
    `app_application_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请理由',
    `app_refuse_reason`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '拒绝理由',
    `app_user_id`            bigint(20)                                                    NULL DEFAULT NULL COMMENT '申请用户id',
    `create_at`              datetime(0)                                                   NULL DEFAULT NULL,
    `create_by`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `update_at`              datetime(0)                                                   NULL DEFAULT NULL,
    `update_by`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    PRIMARY KEY (`app_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org_boards
-- ----------------------------
DROP TABLE IF EXISTS `org_boards`;
CREATE TABLE `org_boards`
(
    `boa_id`      bigint(20)                                                    NOT NULL AUTO_INCREMENT,
    `boa_avatar`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面地址',
    `boa_content` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公告内容',
    `boa_views`   int(11)                                                       NULL DEFAULT NULL COMMENT '已读人数',
    `boa_user_id` bigint(20)                                                    NULL DEFAULT NULL COMMENT '发布者id',
    `create_at`   datetime(0)                                                   NULL DEFAULT NULL,
    `create_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `update_at`   datetime(0)                                                   NULL DEFAULT NULL,
    `update_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    PRIMARY KEY (`boa_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org_comments
-- ----------------------------
DROP TABLE IF EXISTS `org_comments`;
CREATE TABLE `org_comments`
(
    `com_id`        bigint(20)                                                    NOT NULL AUTO_INCREMENT,
    `com_content`   varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评论内容',
    `com_good`      int(11)                                                       NULL DEFAULT NULL COMMENT '点赞数',
    `com_user_id`   bigint(20)                                                    NULL DEFAULT NULL COMMENT '发布者id',
    `com_target_id` bigint(20)                                                    NULL DEFAULT NULL COMMENT '目标用户id',
    `com_news_id`   bigint(20)                                                    NULL DEFAULT NULL COMMENT '新闻id',
    `create_at`     datetime(0)                                                   NULL DEFAULT NULL,
    `create_by`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `update_at`     datetime(0)                                                   NULL DEFAULT NULL,
    `update_by`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    PRIMARY KEY (`com_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org_department
-- ----------------------------
DROP TABLE IF EXISTS `org_department`;
CREATE TABLE `org_department`
(
    `dep_id`               bigint(20)                                                    NOT NULL AUTO_INCREMENT,
    `dep_name`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '部门名',
    `dep_minister_id`      bigint(20)                                                    NULL DEFAULT NULL COMMENT '部长id',
    `dep_vice_minister_id` bigint(20)                                                    NULL DEFAULT NULL COMMENT '副部长id',
    `dep_organization_id`  bigint(20)                                                    NULL DEFAULT NULL COMMENT '社团id',
    `create_at`            datetime(0)                                                   NULL DEFAULT NULL,
    `create_by`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `update_at`            datetime(0)                                                   NULL DEFAULT NULL,
    `update_by`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    PRIMARY KEY (`dep_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org_limit
-- ----------------------------
DROP TABLE IF EXISTS `org_limit`;
CREATE TABLE `org_limit`
(
    `lim_id`   bigint(20)                                                   NOT NULL AUTO_INCREMENT,
    `lim_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '限制名称',
    PRIMARY KEY (`lim_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of org_limit
-- ----------------------------
INSERT INTO `org_limit`
VALUES (1, '本社团');
INSERT INTO `org_limit`
VALUES (2, '多社团联和');
INSERT INTO `org_limit`
VALUES (3, '所有社团成员');
INSERT INTO `org_limit`
VALUES (4, '全校学生');

-- ----------------------------
-- Table structure for org_message
-- ----------------------------
DROP TABLE IF EXISTS `org_message`;
CREATE TABLE `org_message`
(
    `mes_id`        bigint(20)                                                    NOT NULL AUTO_INCREMENT,
    `mes_content`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '消息内容',
    `mes_status`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '消息状态（UNREAD、READ、DELETED）',
    `mes_user_id`   bigint(20)                                                    NULL DEFAULT NULL COMMENT '发布通知者id',
    `mes_target_id` bigint(20)                                                    NULL DEFAULT NULL COMMENT '被通知者id',
    `create_at`     datetime(0)                                                   NULL DEFAULT NULL,
    `create_by`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `update_at`     datetime(0)                                                   NULL DEFAULT NULL,
    `update_by`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    PRIMARY KEY (`mes_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org_news
-- ----------------------------
DROP TABLE IF EXISTS `org_news`;
CREATE TABLE `org_news`
(
    `new_id`              bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '新闻id',
    `new_title`           varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '新闻标题',
    `new_avatar`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '新闻封面地址',
    `new_content`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         NULL COMMENT '新闻内容',
    `new_views`           int(11)                                                       NULL DEFAULT NULL COMMENT '新闻浏览量',
    `new_organization_id` bigint(20)                                                    NULL DEFAULT NULL COMMENT '社团id',
    `create_at`           datetime(0)                                                   NULL DEFAULT NULL,
    `create_by`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `update_at`           datetime(0)                                                   NULL DEFAULT NULL,
    `update_by`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    PRIMARY KEY (`new_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org_organization
-- ----------------------------
DROP TABLE IF EXISTS `org_organization`;
CREATE TABLE `org_organization`
(
    `organ_id`                         bigint(20)                                                     NOT NULL AUTO_INCREMENT COMMENT '社团id',
    `organ_avatar`                     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '社团封面地址',
    `organ_name`                       varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NULL DEFAULT NULL COMMENT '社团名',
    `organ_funds`                      bigint(20)                                                     NULL DEFAULT NULL COMMENT '社费',
    `organ_introduction`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '社团简介',
    `organ_introduction_detail`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          NULL COMMENT '社团详细介绍',
    `organ_introduction_detail_avatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '社团详细介绍图片地址（数组）',
    `organ_star`                       int(11)                                                        NULL DEFAULT NULL COMMENT '社团星级（3-5对应3星-5星）',
    `organ_status`                     varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NULL DEFAULT NULL COMMENT '社团状态（undetermined\n\n、normal、Cancelled）',
    `organ_cancel_reason`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '社团申请注销理由',
    `organ_refuse_reason`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '拒绝申请理由',
    `create_at`                        datetime(0)                                                    NULL DEFAULT NULL,
    `create_by`                        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL,
    `update_at`                        datetime(0)                                                    NULL DEFAULT NULL,
    `update_by`                        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL,
    PRIMARY KEY (`organ_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org_role
-- ----------------------------
DROP TABLE IF EXISTS `org_role`;
CREATE TABLE `org_role`
(
    `role_id`   bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '角色id',
    `role_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色名称',
    `create_at` datetime(0)                                                   NULL DEFAULT NULL,
    `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `update_at` datetime(0)                                                   NULL DEFAULT NULL,
    `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org_trans
-- ----------------------------
DROP TABLE IF EXISTS `org_trans`;
CREATE TABLE `org_trans`
(
    `tra_id`            bigint(20)                                                    NOT NULL AUTO_INCREMENT,
    `tra_change_amount` bigint(20)                                                    NULL DEFAULT NULL COMMENT '变动金额',
    `tra_reason`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '变动原因',
    `tra_amount`        bigint(20)                                                    NULL DEFAULT NULL COMMENT '当前余额',
    `create_at`         datetime(0)                                                   NULL DEFAULT NULL,
    `create_by`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `update_at`         datetime(0)                                                   NULL DEFAULT NULL,
    `update_by`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    PRIMARY KEY (`tra_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org_user
-- ----------------------------
DROP TABLE IF EXISTS `org_user`;
CREATE TABLE `org_user`
(
    `user_id`       bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '用户id',
    `user_avatar`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像地址',
    `user_username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '用户名',
    `user_sex`      int(11)                                                       NULL DEFAULT NULL COMMENT '用户性别',
    `user_password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '密码',
    `user_phone`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
    `user_qq`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'QQ号',
    `user_vx`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信',
    `create_at`     datetime(0)                                                   NULL DEFAULT NULL,
    `create_by`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `update_at`     datetime(0)                                                   NULL DEFAULT NULL,
    `update_by`     varchar(0) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NULL DEFAULT NULL,
    PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org_user_organization_rel
-- ----------------------------
DROP TABLE IF EXISTS `org_user_organization_rel`;
CREATE TABLE `org_user_organization_rel`
(
    `rel_id`              bigint(20)                                                    NOT NULL AUTO_INCREMENT,
    `rel_user_id`         bigint(20)                                                    NULL DEFAULT NULL COMMENT '用户id',
    `rel_organization_id` bigint(20)                                                    NULL DEFAULT NULL COMMENT '社团id',
    `rel_role_id`         bigint(20)                                                    NULL DEFAULT NULL COMMENT '角色id',
    `create_at`           datetime(0)                                                   NULL DEFAULT NULL,
    `create_by`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `update_at`           datetime(0)                                                   NULL DEFAULT NULL,
    `update_by`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    PRIMARY KEY (`rel_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org_user_role_rel
-- ----------------------------
DROP TABLE IF EXISTS `org_user_role_rel`;
CREATE TABLE `org_user_role_rel`
(
    `rel_id`      bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '关系表id',
    `rel_user_id` bigint(20)                                                    NULL DEFAULT NULL,
    `rel_role_id` bigint(20)                                                    NULL DEFAULT NULL,
    `create_at`   datetime(0)                                                   NULL DEFAULT NULL,
    `create_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `update_at`   datetime(0)                                                   NULL DEFAULT NULL,
    `update_by`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    PRIMARY KEY (`rel_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

# 修改的sql
ALTER TABLE `organizations`.`org_organization`
    MODIFY COLUMN `organ_status` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '社团状态（undetermined\n\n、normal、cancel、cancelled）' AFTER `organ_star`;
