/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50617
Source Host           : localhost:3306
Source Database       : javangarch

Target Server Type    : MYSQL
Target Server Version : 50617
File Encoding         : 65001

Date: 2016-05-11 11:30:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `gn_corp`
-- ----------------------------
DROP TABLE IF EXISTS `gn_corp`;
CREATE TABLE `gn_corp` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Code` varchar(10) NOT NULL,
  `Name` varchar(50) NOT NULL,
  `Phone` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PKUQ_GN_Corp` (`ID`),
  UNIQUE KEY `UQ_GN_Corp` (`Name`),
  UNIQUE KEY `UQ_GN_Corp_Code` (`Code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gn_corp
-- ----------------------------
INSERT INTO `gn_corp` VALUES ('1', '1', 'RainyArch产品展示', '13920846704');

-- ----------------------------
-- Table structure for `gn_corpfunc`
-- ----------------------------
DROP TABLE IF EXISTS `gn_corpfunc`;
CREATE TABLE `gn_corpfunc` (
  `CorpID` int(11) NOT NULL,
  `FuncID` int(11) NOT NULL,
  PRIMARY KEY (`CorpID`,`FuncID`),
  UNIQUE KEY `PKUQ_GN_CorpFunc` (`CorpID`,`FuncID`),
  KEY `FK_GN_CorpFunc_GN_Func` (`FuncID`),
  CONSTRAINT `FK_GN_CorpFunc_GN_Corp` FOREIGN KEY (`CorpID`) REFERENCES `gn_corp` (`ID`),
  CONSTRAINT `FK_GN_CorpFunc_GN_Func` FOREIGN KEY (`FuncID`) REFERENCES `gn_func` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gn_corpfunc
-- ----------------------------

-- ----------------------------
-- Table structure for `gn_dept`
-- ----------------------------
DROP TABLE IF EXISTS `gn_dept`;
CREATE TABLE `gn_dept` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CorpID` int(11) NOT NULL,
  `Code` varchar(10) NOT NULL,
  `Name` varchar(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PKUQ_GN_Dept` (`ID`),
  UNIQUE KEY `UQ_GN_Dept` (`CorpID`,`Code`),
  CONSTRAINT `FK_GN_Dept_GN_Corp` FOREIGN KEY (`CorpID`) REFERENCES `gn_corp` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gn_dept
-- ----------------------------
INSERT INTO `gn_dept` VALUES ('1', '1', 'Admin', '系统管理');
INSERT INTO `gn_dept` VALUES ('3', '1', 'Demo', '产品演示');

-- ----------------------------
-- Table structure for `gn_deptfunc`
-- ----------------------------
DROP TABLE IF EXISTS `gn_deptfunc`;
CREATE TABLE `gn_deptfunc` (
  `DeptID` int(11) NOT NULL,
  `FuncID` int(11) NOT NULL,
  PRIMARY KEY (`DeptID`,`FuncID`),
  UNIQUE KEY `PKUQ_GN_DeptFunc` (`DeptID`,`FuncID`),
  KEY `FK_GN_DeptFunc_GN_Func` (`FuncID`),
  CONSTRAINT `FK_GN_DeptFunc_GN_Dept` FOREIGN KEY (`DeptID`) REFERENCES `gn_dept` (`ID`),
  CONSTRAINT `FK_GN_DeptFunc_GN_Func` FOREIGN KEY (`FuncID`) REFERENCES `gn_func` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gn_deptfunc
-- ----------------------------
INSERT INTO `gn_deptfunc` VALUES ('3', '10');
INSERT INTO `gn_deptfunc` VALUES ('3', '15');
INSERT INTO `gn_deptfunc` VALUES ('3', '16');
INSERT INTO `gn_deptfunc` VALUES ('3', '17');
INSERT INTO `gn_deptfunc` VALUES ('3', '22');
INSERT INTO `gn_deptfunc` VALUES ('3', '23');
INSERT INTO `gn_deptfunc` VALUES ('3', '24');
INSERT INTO `gn_deptfunc` VALUES ('3', '33');
INSERT INTO `gn_deptfunc` VALUES ('3', '34');
INSERT INTO `gn_deptfunc` VALUES ('3', '39');
INSERT INTO `gn_deptfunc` VALUES ('3', '40');

-- ----------------------------
-- Table structure for `gn_dict`
-- ----------------------------
DROP TABLE IF EXISTS `gn_dict`;
CREATE TABLE `gn_dict` (
  `ID` int(11) NOT NULL,
  `Name` varchar(10) NOT NULL,
  `ConstName` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PKUQ_GN_Dict` (`ID`),
  UNIQUE KEY `UQ_GN_Dict` (`Name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gn_dict
-- ----------------------------
INSERT INTO `gn_dict` VALUES ('1', '测试字典', 'DOName_PropNameEnum');
INSERT INTO `gn_dict` VALUES ('2', '会员留言处理', 'MemberNote_ResultEnum');
INSERT INTO `gn_dict` VALUES ('3', '留言分类', 'MemberNote_NoteClassEnum');

-- ----------------------------
-- Table structure for `gn_dictitem`
-- ----------------------------
DROP TABLE IF EXISTS `gn_dictitem`;
CREATE TABLE `gn_dictitem` (
  `DictID` int(11) NOT NULL,
  `ItemID` smallint(6) NOT NULL,
  `ItemName` varchar(30) NOT NULL,
  `ConstName` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`DictID`,`ItemID`),
  UNIQUE KEY `PKUQ_GN_DictItem` (`DictID`,`ItemID`),
  CONSTRAINT `FK_GN_DictItem_GN_Dict` FOREIGN KEY (`DictID`) REFERENCES `gn_dict` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gn_dictitem
-- ----------------------------
INSERT INTO `gn_dictitem` VALUES ('1', '1', '测试字典项', 'Test');
INSERT INTO `gn_dictitem` VALUES ('2', '0', '未处理', 'Untreated');
INSERT INTO `gn_dictitem` VALUES ('2', '1', '审核中', 'Processing');
INSERT INTO `gn_dictitem` VALUES ('2', '2', '已记录', 'Recorded');
INSERT INTO `gn_dictitem` VALUES ('3', '0', '暂存', 'Saved');
INSERT INTO `gn_dictitem` VALUES ('3', '1', '重要1', 'Important1');

-- ----------------------------
-- Table structure for `gn_errormsgmap`
-- ----------------------------
DROP TABLE IF EXISTS `gn_errormsgmap`;
CREATE TABLE `gn_errormsgmap` (
  `Code` varchar(50) NOT NULL,
  `Message` varchar(50) NOT NULL,
  PRIMARY KEY (`Code`),
  UNIQUE KEY `PKUQ_GN_ErrorMsgMap` (`Code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gn_errormsgmap
-- ----------------------------

-- ----------------------------
-- Table structure for `gn_func`
-- ----------------------------
DROP TABLE IF EXISTS `gn_func`;
CREATE TABLE `gn_func` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(20) NOT NULL,
  `Level` varchar(20) NOT NULL,
  `Code` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PKUQ_GN_Func` (`ID`),
  UNIQUE KEY `UQ_GN_Func` (`Level`),
  UNIQUE KEY `UQ_GN_Func_Code` (`Code`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gn_func
-- ----------------------------
INSERT INTO `gn_func` VALUES ('1', '菜单可用性', '01', 'M_Menu');
INSERT INTO `gn_func` VALUES ('2', '系统', '0180', 'M_System');
INSERT INTO `gn_func` VALUES ('3', '公司信息', '018001', 'M_Corp');
INSERT INTO `gn_func` VALUES ('5', '部门', '018002', 'M_Dept');
INSERT INTO `gn_func` VALUES ('6', '系统', '80', 'System');
INSERT INTO `gn_func` VALUES ('7', '公司信息', '8001', 'Corp');
INSERT INTO `gn_func` VALUES ('8', '编辑', '800101', 'Corp.Edit');
INSERT INTO `gn_func` VALUES ('9', '用户', '018003', 'M_User');
INSERT INTO `gn_func` VALUES ('10', '修改', '800102', 'Corp.Save');
INSERT INTO `gn_func` VALUES ('11', '部门', '8002', 'Dept');
INSERT INTO `gn_func` VALUES ('12', '查询', '800201', 'Dept.List');
INSERT INTO `gn_func` VALUES ('13', '编辑', '800203', 'Dept.Edit');
INSERT INTO `gn_func` VALUES ('14', '新增', '800202', 'Dept.Add');
INSERT INTO `gn_func` VALUES ('15', '保存', '800204', 'Dept.Save');
INSERT INTO `gn_func` VALUES ('16', '删除', '800205', 'Dept.Delete');
INSERT INTO `gn_func` VALUES ('17', '修改权限', '800206', '_ChangeDeptFunc');
INSERT INTO `gn_func` VALUES ('18', '用户', '8003', 'User');
INSERT INTO `gn_func` VALUES ('19', '查询', '800301', 'User.List');
INSERT INTO `gn_func` VALUES ('20', '新增', '800302', 'User.Add');
INSERT INTO `gn_func` VALUES ('21', '编辑', '800303', 'User.Edit');
INSERT INTO `gn_func` VALUES ('22', '保存', '800304', 'User.Save');
INSERT INTO `gn_func` VALUES ('23', '删除', '800305', 'User.Delete');
INSERT INTO `gn_func` VALUES ('24', '修改权限', '800306', '_ChangeUserFunc');
INSERT INTO `gn_func` VALUES ('25', '自动样例源码', '0110', 'M_Sample');
INSERT INTO `gn_func` VALUES ('26', '任务', '011001', 'M_Task');
INSERT INTO `gn_func` VALUES ('27', '任务细项', '011002', 'M_TaskItem');
INSERT INTO `gn_func` VALUES ('28', '任务管理', '90', 'Ta');
INSERT INTO `gn_func` VALUES ('29', '任务', '9010', 'Task');
INSERT INTO `gn_func` VALUES ('30', '查询', '901001', 'Task.List');
INSERT INTO `gn_func` VALUES ('31', '新增', '901002', 'Task.Add');
INSERT INTO `gn_func` VALUES ('32', '编辑', '901003', 'Task.Edit');
INSERT INTO `gn_func` VALUES ('33', '保存', '901004', 'Task.Save');
INSERT INTO `gn_func` VALUES ('34', '删除', '901005', 'Task.Delete');
INSERT INTO `gn_func` VALUES ('35', '任务细项', '9020', 'TaskItem');
INSERT INTO `gn_func` VALUES ('36', '查询', '902001', 'TaskItem.List');
INSERT INTO `gn_func` VALUES ('37', '新增', '902002', 'TaskItem.Add');
INSERT INTO `gn_func` VALUES ('38', '编辑', '902003', 'TaskItem.Edit');
INSERT INTO `gn_func` VALUES ('39', '保存', '902004', 'TaskItem.Save');
INSERT INTO `gn_func` VALUES ('40', '删除', '902005', 'TaskItem.Delete');

-- ----------------------------
-- Table structure for `gn_user`
-- ----------------------------
DROP TABLE IF EXISTS `gn_user`;
CREATE TABLE `gn_user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CorpID` int(11) NOT NULL,
  `DeptID` int(11) NOT NULL,
  `Code` varchar(20) NOT NULL,
  `Name` varchar(10) NOT NULL,
  `CellPhone` varchar(20) DEFAULT NULL,
  `LoginMark` varchar(50) DEFAULT NULL,
  `IsActive` bit(1) NOT NULL DEFAULT b'1',
  `Password` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PKUQ_GN_User` (`ID`),
  UNIQUE KEY `UQ_GN_User` (`CorpID`,`Code`),
  KEY `FK_GN_User_GN_Dept` (`DeptID`),
  CONSTRAINT `FK_GN_User_GN_Corp` FOREIGN KEY (`CorpID`) REFERENCES `gn_corp` (`ID`),
  CONSTRAINT `FK_GN_User_GN_Dept` FOREIGN KEY (`DeptID`) REFERENCES `gn_dept` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gn_user
-- ----------------------------
INSERT INTO `gn_user` VALUES ('1', '1', '1', 'admin', '朱艳萍', '13920846704', null, '', '281b0f4a02824d37a13529b4bd85e5b9');
INSERT INTO `gn_user` VALUES ('2', '1', '3', 'test', '产品演示账号', null, null, '', 'c4ca4238a0b923820dcc509a6f75849b');

-- ----------------------------
-- Table structure for `gn_userfunc`
-- ----------------------------
DROP TABLE IF EXISTS `gn_userfunc`;
CREATE TABLE `gn_userfunc` (
  `UserID` int(11) NOT NULL,
  `FuncID` int(11) NOT NULL,
  PRIMARY KEY (`UserID`,`FuncID`),
  UNIQUE KEY `PKUQ_GN_UserFunc` (`UserID`,`FuncID`),
  KEY `FK_GN_UserFunc_GN_Func` (`FuncID`),
  CONSTRAINT `FK_GN_UserFunc_GN_Func` FOREIGN KEY (`FuncID`) REFERENCES `gn_func` (`ID`),
  CONSTRAINT `FK_GN_UserFunc_GN_User` FOREIGN KEY (`UserID`) REFERENCES `gn_user` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gn_userfunc
-- ----------------------------

-- ----------------------------
-- Table structure for `gn_userlog`
-- ----------------------------
DROP TABLE IF EXISTS `gn_userlog`;
CREATE TABLE `gn_userlog` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `UserCode` varchar(20) NOT NULL,
  `ActionTime` datetime NOT NULL,
  `Action` varchar(20) DEFAULT NULL,
  `UserName` varchar(10) NOT NULL,
  `memo` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PKUQ_GN_UserLog` (`ID`),
  UNIQUE KEY `UQ_GN_UserLog` (`UserCode`,`ActionTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gn_userlog
-- ----------------------------

-- ----------------------------
-- Table structure for `ta_task`
-- ----------------------------
DROP TABLE IF EXISTS `ta_task`;
CREATE TABLE `ta_task` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `UserID` int(11) NOT NULL,
  `CreateDate` datetime NOT NULL,
  `PlanBeginDate` datetime DEFAULT NULL,
  `PlanEndDate` datetime DEFAULT NULL,
  `BeginDate` datetime DEFAULT NULL,
  `EndDate` datetime DEFAULT NULL,
  `Status` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PKUQ_TA_Task` (`ID`),
  UNIQUE KEY `UQ_TA_Task` (`Name`),
  KEY `FK_TA_Task_GN_User` (`UserID`),
  CONSTRAINT `FK_TA_Task_GN_User` FOREIGN KEY (`UserID`) REFERENCES `gn_user` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ta_task
-- ----------------------------
INSERT INTO `ta_task` VALUES ('3', '纯真年代青少年身心健康合作社寻址', '1', '2016-05-01 00:00:00', null, null, null, null, '0');
INSERT INTO `ta_task` VALUES ('4', 'RainyArch产品义卖', '1', '2016-05-01 00:00:00', null, null, null, null, '0');
INSERT INTO `ta_task` VALUES ('6', '公益活动', '1', '2016-04-03 00:00:00', '2016-04-05 00:00:00', null, null, null, '0');

-- ----------------------------
-- Table structure for `ta_taskitem`
-- ----------------------------
DROP TABLE IF EXISTS `ta_taskitem`;
CREATE TABLE `ta_taskitem` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TaskID` int(11) NOT NULL,
  `Brief` varchar(30) NOT NULL,
  `UserID` int(11) NOT NULL,
  `Requirement` varchar(200) DEFAULT NULL,
  `Record` varchar(1000) DEFAULT NULL,
  `KeyInfo` varchar(200) DEFAULT NULL,
  `CreateDate` datetime NOT NULL,
  `ActionDate` datetime DEFAULT NULL,
  `Status` int(11) NOT NULL,
  `OrderNum` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PKUQ_TA_TaskItem` (`ID`),
  UNIQUE KEY `UQ_TA_TaskItem` (`TaskID`,`Brief`),
  KEY `FK_TA_TaskItem_GN_User` (`UserID`),
  CONSTRAINT `FK_TA_TaskItem_GN_User` FOREIGN KEY (`UserID`) REFERENCES `gn_user` (`ID`),
  CONSTRAINT `FK_TA_TaskItem_TA_Task` FOREIGN KEY (`TaskID`) REFERENCES `ta_task` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ta_taskitem
-- ----------------------------
INSERT INTO `ta_taskitem` VALUES ('1', '3', '华苑产业区内寻址', '1', '在华苑产业区（环内环外）寻找适合的场地', '产业区环内已挨家上门询问', '联系到某负责人', '2016-02-23 00:00:00', '2016-02-23 00:00:00', '1', '0');
INSERT INTO `ta_taskitem` VALUES ('2', '3', '环外寻址', '1', '靠近外环线位置', '已考察', '作为备选方案', '2016-04-04 00:00:00', null, '1', '1');
INSERT INTO `ta_taskitem` VALUES ('3', '4', '准备', '1', '准备源码、文档，本地运行', null, null, '2016-04-08 00:00:00', '2016-05-08 00:00:00', '1', '1');
INSERT INTO `ta_taskitem` VALUES ('4', '4', '上线', '1', '发布到网站www.51chunzhen.com', null, null, '2016-05-08 00:00:00', '2016-05-08 00:00:00', '1', '2');
INSERT INTO `ta_taskitem` VALUES ('5', '6', '联系公益组织', '1', '联系公益组织', null, null, '2016-05-08 00:00:00', null, '0', '1');
INSERT INTO `ta_taskitem` VALUES ('6', '6', '活动页面上线', '1', null, null, null, '2016-05-08 00:00:00', null, '0', '2');
