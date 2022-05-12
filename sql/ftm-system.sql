/*
SQLyog Ultimate v8.32 
MySQL - 5.7.30 : Database - ftm-system
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`ftm-system` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `ftm-system`;

/*Table structure for table `company` */

DROP TABLE IF EXISTS `company`;

CREATE TABLE `company` (
  `id` varchar(40) DEFAULT NULL,
  `name` varchar(400) DEFAULT NULL COMMENT '公司名称',
  `address` varchar(400) DEFAULT NULL COMMENT '公司地址',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `introduction` varchar(300) DEFAULT NULL COMMENT '公司简介',
  `logo_image` varchar(300) DEFAULT NULL COMMENT 'logo',
  `parent_id` varchar(40) DEFAULT NULL,
  `level` varchar(10) DEFAULT NULL COMMENT '级别0,1,2',
  `label` varchar(40) DEFAULT NULL COMMENT '根节点，母公司，子公司',
  `origin` varchar(40) DEFAULT NULL COMMENT '祖先，母，子，孙子都有共同的祖先',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_data` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `company` */

insert  into `company`(`id`,`name`,`address`,`email`,`introduction`,`logo_image`,`parent_id`,`level`,`label`,`origin`,`create_date`,`update_data`) values ('1','汽零零',NULL,NULL,NULL,NULL,NULL,'0','根组织',NULL,'2022-02-16 10:21:15','0000-00-00 00:00:00'),('2','汽零零工业互联网',NULL,NULL,NULL,NULL,'1','1','母公司','2','2022-02-21 14:24:29','0000-00-00 00:00:00'),('3','汽零零工业互联网产业集团',NULL,NULL,NULL,NULL,'2','2','子公司','2','2022-02-21 17:32:16','0000-00-00 00:00:00');

/*Table structure for table `department` */

DROP TABLE IF EXISTS `department`;

CREATE TABLE `department` (
  `id` varchar(40) NOT NULL,
  `company_id` varchar(40) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL COMMENT '部门名称',
  `code` varchar(100) DEFAULT NULL COMMENT '部门编号',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `department` */

insert  into `department`(`id`,`company_id`,`name`,`code`,`remark`,`created_date`,`update_date`) values ('1','1','技术部',NULL,NULL,'2022-02-21 12:59:22','0000-00-00 00:00:00'),('2','1','销售部',NULL,NULL,'2022-02-21 13:06:49','0000-00-00 00:00:00'),('3',NULL,NULL,NULL,NULL,'2022-02-21 14:43:09','0000-00-00 00:00:00');

/*Table structure for table `sys_menu` */

DROP TABLE IF EXISTS `sys_menu`;

CREATE TABLE `sys_menu` (
  `id` varchar(40) NOT NULL COMMENT '菜单 ID',
  `parent_id` varchar(40) DEFAULT NULL COMMENT '父菜单 ID (0为顶级菜单)',
  `name` varchar(60) DEFAULT NULL COMMENT 'vue菜单名称',
  `path` varchar(100) DEFAULT NULL COMMENT 'vue相对路径',
  `url` varchar(255) DEFAULT NULL COMMENT '请求地址',
  `component` varchar(255) DEFAULT NULL COMMENT 'vue中对应页面所在位置',
  `type` int(3) DEFAULT '1' COMMENT '类型(1目录，2菜单，3按钮)',
  `code` varchar(60) DEFAULT NULL COMMENT '授权标识符',
  `icon` varchar(200) DEFAULT NULL COMMENT '图标',
  `sort` int(11) DEFAULT '1' COMMENT '排序',
  `meta_title` varchar(50) DEFAULT NULL,
  `meta_icon` varchar(50) DEFAULT NULL,
  `meta_breadcrumb_hidden` tinyint(4) DEFAULT NULL,
  `meta_no_closable` tinyint(4) DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单信息表';

/*Data for the table `sys_menu` */

insert  into `sys_menu`(`id`,`parent_id`,`name`,`path`,`url`,`component`,`type`,`code`,`icon`,`sort`,`meta_title`,`meta_icon`,`meta_breadcrumb_hidden`,`meta_no_closable`,`remark`,`create_date`,`update_date`) values ('1',NULL,'Root','/',NULL,'Layout',1,NULL,NULL,1,'首页','home-2-line',1,NULL,NULL,'2022-02-16 16:10:12','2022-02-16 16:10:12'),('2','1','Index','index',NULL,'@/views/index',1,NULL,NULL,1,'首页','home-2-line',NULL,1,NULL,'2022-02-16 16:11:14','2022-02-16 16:11:14'),('3','1','Dashboard','dashboard',NULL,'@/views/index/dashboard',1,NULL,NULL,1,'看板','dashboard-line',NULL,NULL,NULL,'2022-02-16 16:11:48','2022-02-16 16:11:48'),('4','1','System','system',NULL,'@/views/system',1,NULL,NULL,1,'系统中心','home-2-line',NULL,NULL,NULL,'2022-02-17 12:58:15','2022-02-17 12:58:15'),('5','4','User','user',NULL,'@/views/system/user/index',1,NULL,NULL,1,'用户管理','dashboard-line',NULL,NULL,NULL,'2022-02-17 12:59:22','2022-02-17 12:59:22'),('6','4','Company','company',NULL,'@/views/system/company/index',1,NULL,NULL,1,'组织管理','dashboard-line',NULL,NULL,NULL,'2022-02-18 13:39:52','2022-02-18 13:39:52'),('7','4','Department','dept',NULL,'@/views/system/dept/index',1,NULL,NULL,1,'部门管理','dashboard-line',NULL,NULL,NULL,'2022-02-18 13:41:07','2022-02-18 13:41:07');

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `id` varchar(40) NOT NULL COMMENT '角色 ID',
  `dept_id` varchar(40) DEFAULT NULL,
  `name` varchar(60) DEFAULT NULL COMMENT '角色名称',
  `code` varchar(60) DEFAULT NULL COMMENT '角色编码',
  `remark` varchar(200) DEFAULT NULL COMMENT '角色说明',
  `create_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色信息表';

/*Data for the table `sys_role` */

insert  into `sys_role`(`id`,`dept_id`,`name`,`code`,`remark`,`create_date`,`update_date`) values ('10',NULL,'普通管理员',NULL,'拥有查看权限','2023-08-08 11:11:11','2023-08-08 11:11:11'),('9',NULL,'超级管理员',NULL,'拥有所有的权限','2023-08-08 11:11:11','2023-08-08 11:11:11');

/*Table structure for table `sys_role_menu` */

DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
  `id` varchar(40) NOT NULL COMMENT '主键 ID',
  `role_id` varchar(40) DEFAULT NULL COMMENT '角色 ID',
  `menu_id` varchar(40) DEFAULT NULL COMMENT '菜单 ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色权限表';

/*Data for the table `sys_role_menu` */

insert  into `sys_role_menu`(`id`,`role_id`,`menu_id`) values ('1287965925087313921','9','11'),('1287965925087313922','9','1251060960026705921'),('1287965925087313923','9','1251061228965478402'),('1287965925087313924','9','1251061280744161281'),('1287965925091508225','9','1253512582655021057'),('1287965925091508226','9','1251061460868546562'),('1287965925091508227','9','1251061397639413762'),('1287965925091508228','9','1251061181913776129'),('1287965925091508229','9','1251070494405349377'),('1287965925091508230','9','1251070561216417794'),('1287965925091508231','9','1251070617537531905'),('1287965925091508232','9','1251070746856312833'),('1287965925091508233','9','1251061016268128258'),('1287965925091508234','9','1251071087492517890'),('1287965925091508235','9','1251071167758913538'),('1287965925091508236','9','1251071270213177345'),('1287965925091508237','9','1251071220363874305'),('1287965925091508238','9','1251071493878632450'),('1287965925091508239','9','1281504383378644993'),('1287965925091508240','9','1281490789589049345'),('1287965925091508241','9','1281504074870808577'),('1287965925091508242','9','1281503969723801601'),('1287965925091508243','9','17'),('1287965925091508244','9','18'),('1287965925091508245','9','19'),('1287965925091508246','9','20'),('1287965925091508247','9','21'),('1287965925091508248','9','22'),('1287965925091508249','9','1251071340815896577'),('1287965925095702529','9','1251071383350333442'),('1287965925095702530','9','23'),('1287965925095702531','9','24'),('1287965925095702532','9','25'),('1287965925095702533','9','26'),('1287965925095702534','9','27'),('1287965925095702535','9','1253592898971275265'),('1287965925095702536','9','28'),('1287965925095702537','9','29'),('1287965925095702538','9','30'),('1287965925095702539','9','31'),('1287965925095702540','9','32'),('1287965925095702541','9','1253513166296616961');

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `id` varchar(40) NOT NULL COMMENT '用户 ID',
  `username` varchar(60) DEFAULT NULL COMMENT '用户名',
  `password` varchar(60) DEFAULT NULL COMMENT '密码，加密存储, admin/1234',
  `is_account_non_expired` tinyint(2) DEFAULT '1' COMMENT '帐户是否过期(1 未过期，0已过期)',
  `is_account_non_locked` tinyint(2) DEFAULT '1' COMMENT '帐户是否被锁定(1 未过期，0已过期)',
  `is_credentials_non_expired` tinyint(2) DEFAULT '1' COMMENT '密码是否过期(1 未过期，0已过期)',
  `is_enabled` tinyint(2) DEFAULT '1' COMMENT '帐户是否可用(1 可用，0 删除用户)',
  `nick_name` varchar(60) DEFAULT NULL COMMENT '昵称',
  `image_url` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '头像url',
  `mobile` varchar(20) DEFAULT NULL COMMENT '注册手机号',
  `email` varchar(50) DEFAULT NULL COMMENT '注册邮箱',
  `create_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `pwd_update_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '密码更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `username` (`username`) USING BTREE,
  UNIQUE KEY `mobile` (`mobile`) USING BTREE,
  UNIQUE KEY `email` (`email`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息表';

/*Data for the table `sys_user` */

insert  into `sys_user`(`id`,`username`,`password`,`is_account_non_expired`,`is_account_non_locked`,`is_credentials_non_expired`,`is_enabled`,`nick_name`,`image_url`,`mobile`,`email`,`create_date`,`update_date`,`pwd_update_date`) values ('10','test','$2a$10$uA51hWL5yusFBoEvZOAZbeaYYqUaFV7xjdDB8GA.4iViNiCSK9xKO',1,1,1,1,'测试','https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif','16888886666','test1@qq.com','2023-08-08 11:11:11','2020-05-22 15:05:57','2020-04-10 09:41:51'),('11','string','$2a$10$uA51hWL5yusFBoEvZOAZbeaYYqUaFV7xjdDB8GA.4iViNiCSK9xKO',0,1,0,0,'string','https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif','string','string','2020-04-11 21:37:25','2020-04-17 16:43:19','2020-04-11 21:37:25'),('1253583309139775489','root','$2a$10$qci2y9rouzWgEP/injjeDeUAFbWIGP7wQILjqmDE61S1ZMwWqiGqi',0,1,1,1,'root','https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif','15888888888',NULL,'2020-04-24 15:15:26','2020-07-25 09:16:36','2020-04-24 15:15:26'),('1495674038577954818','rre','eee',1,1,1,1,NULL,NULL,'eeeeee','eeeeee','2022-02-21 16:17:33','2022-02-21 16:17:33','2022-02-21 16:17:33'),('1495684395434405889','874','rfr',1,1,1,1,NULL,NULL,'ew','3333','2022-02-21 16:58:43','2022-02-21 16:58:43','2022-02-21 16:58:43'),('1496002059440689153','谭洋波','sss',1,1,1,1,NULL,NULL,'15587491751','1632414557@qq.com','2022-02-22 14:01:00','2022-02-22 14:01:00','2022-02-22 14:01:00'),('1496003613904281601','曾天涕','sss',1,1,1,1,NULL,NULL,'15587491752','1632414558@qq.com','2022-02-22 14:07:10','2022-02-22 14:07:10','2022-02-22 14:07:10'),('1496009075370434562','吕开朗','17601571791',1,1,1,1,NULL,NULL,'17601571791','15289584695','2022-02-22 14:28:52','2022-02-22 14:28:52','2022-02-22 14:28:52'),('9','admin','$2a$10$lxrzO.wnypXdhOxL44p4WOOMhLqRZybs9RyXY.9YZQEYfOWOFfQIS',1,1,1,1,'梦学谷','https://mengxuegu.oss-cn-shenzhen.aliyuncs.com/article/20200522/8665d73ae2484bd28bc2ff4103538385.png','18888888888','mengxuegu888@163.com','2023-08-08 11:11:11','2020-05-22 22:30:14','2020-04-10 09:41:51');

/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `id` varchar(40) NOT NULL COMMENT '主键 ID',
  `user_id` varchar(40) DEFAULT NULL COMMENT '用户 ID',
  `role_id` varchar(40) DEFAULT NULL COMMENT '角色 ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关系表';

/*Data for the table `sys_user_role` */

insert  into `sys_user_role`(`id`,`user_id`,`role_id`) values ('1287965845408120834','9','9'),('2','10','10');

/*Table structure for table `user_company` */

DROP TABLE IF EXISTS `user_company`;

CREATE TABLE `user_company` (
  `id` varchar(40) NOT NULL,
  `uid` varchar(40) DEFAULT NULL,
  `company_id` varchar(40) DEFAULT NULL,
  `is_enable` tinyint(4) DEFAULT NULL,
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_company` */

insert  into `user_company`(`id`,`uid`,`company_id`,`is_enable`,`create_date`,`update_date`) values ('1','9','1',1,'2022-02-16 10:20:48','0000-00-00 00:00:00'),('1493324522456634322','1495674038577954818','2',NULL,'2022-02-22 11:10:55','0000-00-00 00:00:00'),('1495674039207100418','1495674038577954818','1',1,'2022-02-21 16:20:53','0000-00-00 00:00:00'),('1495684395711229954','1495684395434405889','2',NULL,'2022-02-21 16:58:43','0000-00-00 00:00:00'),('1496002060006920193','1496002059440689153','1',NULL,'2022-02-22 14:01:00','0000-00-00 00:00:00'),('1496003614168522754','1496003613904281601','1',NULL,'2022-02-22 14:07:10','0000-00-00 00:00:00'),('1496009075638870017','1496009075370434562','1',NULL,'2022-02-22 14:28:52','0000-00-00 00:00:00');

/*Table structure for table `user_dept` */

DROP TABLE IF EXISTS `user_dept`;

CREATE TABLE `user_dept` (
  `id` varchar(40) NOT NULL,
  `uid` varchar(40) DEFAULT NULL,
  `company_id` varchar(40) DEFAULT NULL,
  `dept_id` varchar(40) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_dept` */

insert  into `user_dept`(`id`,`uid`,`company_id`,`dept_id`,`create_date`,`update_date`) values ('1495670533838516226',NULL,'1','1',NULL,NULL),('1495674039672668162','1495674038577954818','1','1',NULL,NULL),('1495684395979665409','1495684395434405889',NULL,'2',NULL,NULL),('1496002060342464514','1496002059440689153',NULL,'1',NULL,NULL),('1496003614420180994','1496003613904281601','1','2',NULL,NULL),('1496009075705978881','1496009075370434562','1','1',NULL,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
