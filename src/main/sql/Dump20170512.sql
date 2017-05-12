-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: socketlamp
-- ------------------------------------------------------
-- Server version	5.7.15

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `business_socket`
--

DROP TABLE IF EXISTS `business_socket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_socket` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `socket_id` varchar(50) NOT NULL COMMENT '插座ID',
  `socket_name` varchar(50) DEFAULT 'tse插座' COMMENT '插座代号',
  `owner_id` varchar(20) DEFAULT '-1' COMMENT '插座所属人id:；-1代表没绑定用户',
  `status` varchar(2) DEFAULT '-1' COMMENT '开关状态：0关/1开/-1表示未使用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `socket_id` (`socket_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='业务：插座信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_socket`
--

LOCK TABLES `business_socket` WRITE;
/*!40000 ALTER TABLE `business_socket` DISABLE KEYS */;
INSERT INTO `business_socket` VALUES (1,'11303010250','tseSocket','1103010232',NULL);
/*!40000 ALTER TABLE `business_socket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `user_id` varchar(20) NOT NULL COMMENT '账户',
  `user_pass` varchar(50) NOT NULL DEFAULT '000000',
  `mail` varchar(45) DEFAULT NULL COMMENT '邮箱可有可无',
  `phone` varchar(11) DEFAULT NULL COMMENT '用户手机号',
  `user_name` varchar(45) DEFAULT 'noob' COMMENT '用户姓名',
  `sex` varchar(10) DEFAULT NULL COMMENT '性别：\n0female女性\n1male男性',
  `icon` varchar(100) DEFAULT NULL,
  `role` varchar(10) DEFAULT '1' COMMENT '角色\n1：注册用户\n0：管理用户',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'xyj222310','000000',NULL,NULL,NULL,NULL,NULL,NULL),(5,'sb','000000',NULL,NULL,NULL,NULL,NULL,NULL),(7,'sb2','000000',NULL,NULL,NULL,NULL,NULL,NULL),(9,'11303010232','888888','xyj222310@163.com','12333333333',NULL,'male',NULL,NULL),(14,'11303010229','000000',NULL,NULL,NULL,NULL,NULL,NULL),(15,'11303010231','000000',NULL,NULL,NULL,NULL,NULL,NULL),(17,'11303010203','000000',NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-05-12 20:37:04
