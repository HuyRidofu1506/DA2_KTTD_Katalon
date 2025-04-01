-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: sb_manage_apartment
-- ------------------------------------------------------
-- Server version	8.0.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `apartment`
--

DROP TABLE IF EXISTS `apartment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `apartment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `number_of_apartment` int NOT NULL,
  `resident_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `status` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `resident_id` bigint DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK36b17lltkg5jkofk80cd5qc71` (`resident_id`),
  CONSTRAINT `FK36b17lltkg5jkofk80cd5qc71` FOREIGN KEY (`resident_id`) REFERENCES `resident` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `apartment`
--

LOCK TABLES `apartment` WRITE;
/*!40000 ALTER TABLE `apartment` DISABLE KEYS */;
INSERT INTO `apartment` VALUES (14,101,'Nguyễn Văn A','available',NULL,'2024-12-23 12:35:18.000000','2024-12-23 12:39:11.000000'),(15,102,'Nguyễn Văn A','inAvailable',10,'2024-12-23 12:35:47.000000','2024-12-23 12:35:47.000000'),(18,933,'Phạm Trang','inAvailable',NULL,'2024-12-23 12:53:23.000000','2025-03-31 18:02:13.213000'),(23,839,'Cao Thị T','inAvailable',11,'2025-03-30 23:27:10.292000','2025-03-30 23:27:10.292000'),(24,941,'Ngô An','available',NULL,'2025-03-31 18:06:19.483000','2025-03-31 18:07:56.306000'),(25,202,'Cao Văn C','available',12,'2025-03-31 18:27:52.061000','2025-03-31 18:27:52.061000');
/*!40000 ALTER TABLE `apartment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bill`
--

DROP TABLE IF EXISTS `bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bill` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code_bill` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `date` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `number_electric` int NOT NULL,
  `number_of_apartment` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `number_water` int NOT NULL,
  `resident_name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `status` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `toilet_money` int NOT NULL,
  `total` bigint DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `apartment_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKk38rb34qni187un26nugmay7f` (`apartment_id`),
  CONSTRAINT `FKk38rb34qni187un26nugmay7f` FOREIGN KEY (`apartment_id`) REFERENCES `apartment` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill`
--

LOCK TABLES `bill` WRITE;
/*!40000 ALTER TABLE `bill` DISABLE KEYS */;
INSERT INTO `bill` VALUES (9,'HD #1350','0024-12-05',58,'102',50,'Cao Thị T','NotPaid',20000,523000,'2024-12-23 13:04:11.000000','2024-12-23 13:04:40.000000',NULL),(10,'HD #6414','2025-01-01',50,'102',20,'Cao Thị T','NotPaid',20000,315000,'2025-01-01 03:44:30.000000','2025-01-01 03:44:30.000000',NULL),(11,'HD #9922','2025-01-01',35,'202',50,'tu','Pay',20000,442500,'2025-01-07 07:19:47.000000','2025-01-07 07:20:09.000000',NULL),(12,'HD #894','1988-12-08',300,'941',20,'Vũ Nam','NotPaid',20000,1190000,'2025-03-31 18:52:54.930000','2025-03-31 18:52:54.930000',NULL),(13,'HD #5223','2009-12-04',300,'941',20,'tu','NotPaid',20000,1190000,'2025-03-31 18:54:48.503000','2025-03-31 18:54:48.503000',NULL),(14,'HD #9974','2009-12-11',300,'202',20,'Nguyễn Trang','Pay',20000,1190000,'2025-03-31 19:07:26.036000','2025-03-31 19:07:26.036000',NULL),(15,'HD #1779','12003-02-04',300,'941',20,'Cao Văn D','Pay',20000,1190000,'2025-03-31 19:10:35.703000','2025-03-31 19:10:35.703000',NULL),(16,'HD #2244','1974-09-12',300,'101',20,'Đỗ Khoa','Pay',20000,1190000,'2025-03-31 19:11:48.719000','2025-03-31 19:11:48.719000',NULL),(17,'HD #3342','51998-02-10',300,'101',20,'Đỗ Minh','Pay',20000,1190000,'2025-03-31 19:35:18.683000','2025-03-31 19:35:18.683000',NULL);
/*!40000 ALTER TABLE `bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cccd` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `dob` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `gender` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `phone_number` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `position` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `working_time` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKmpps3d3r9pdvyjx3iqixi96fi` (`user_id`),
  CONSTRAINT `FK6lk0xml9r7okjdq0onka4ytju` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (17,'1234560789','1989-03-01','male','Nhân Viên C','0558922144','guard','15h - 22h','2025-01-01 03:31:46.000000','2025-01-01 03:31:46.000000',NULL),(18,'1234567890','1999-01-01','female','Nhân Viên D','0998821477','cleaner','8h - 16h','2025-01-07 07:16:19.000000','2025-01-07 07:16:19.000000',NULL),(19,'768676021','1999-05-07','female','Dương Khoa','0999297910','cleaner','21h - 23h','2025-03-30 23:34:41.758000','2025-03-30 23:34:41.758000',NULL),(20,'123456789','1981-05-05','female','Lý Nam','0123456789','guard','15h - 22h','2025-03-30 23:34:55.875000','2025-03-30 23:34:55.875000',NULL);
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report`
--

DROP TABLE IF EXISTS `report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `report` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `date` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `apartment_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqlc4qh4heptv72rhhlsgjmdxc` (`apartment_id`),
  KEY `FKj62onw73yx1qnmd57tcaa9q3a` (`user_id`),
  CONSTRAINT `FKj62onw73yx1qnmd57tcaa9q3a` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKqlc4qh4heptv72rhhlsgjmdxc` FOREIGN KEY (`apartment_id`) REFERENCES `apartment` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report`
--

LOCK TABLES `report` WRITE;
/*!40000 ALTER TABLE `report` DISABLE KEYS */;
INSERT INTO `report` VALUES (5,'Báo cáo mới','2024-12-01','Báo cáo tháng 12','2024-12-23 12:36:58.000000','2024-12-23 12:36:58.000000',NULL,NULL),(7,'test báo cáo','2024-05-03','Báo cáo test','2024-12-23 13:05:22.000000','2024-12-23 13:05:22.000000',NULL,NULL),(8,'Báo cáo tháng 01','2025-01-01','Báo cáo tháng 01 năm 2025','2025-01-07 07:21:23.000000','2025-01-07 07:21:23.000000',NULL,NULL),(9,'Test báo cáo','1999-11-10','Báo cáo 51','2025-03-31 22:44:41.226000','2025-03-31 22:44:41.226000',NULL,NULL),(10,'Test báo cáo','1993-12-04','Báo cáo 71','2025-03-31 22:45:42.231000','2025-03-31 22:45:42.231000',NULL,NULL),(11,'Test báo cáo','121998-02-05','Báo cáo 11','2025-03-31 22:49:54.144000','2025-03-31 22:49:54.144000',NULL,NULL),(12,'Test báo cáo','1993-06-07','Báo cáo 01','2025-03-31 22:51:51.115000','2025-03-31 22:51:51.115000',NULL,NULL),(13,'Test báo cáo','82001-02-01','Báo cáo 31','2025-03-31 22:52:48.414000','2025-03-31 22:52:48.414000',NULL,NULL),(14,'Test báo cáo','2009-12-03','Báo cáo 51','2025-03-31 22:56:03.068000','2025-03-31 22:56:03.068000',NULL,NULL),(15,'Test báo cáo','71993-02-01','Báo cáo 51','2025-03-31 22:58:15.751000','2025-03-31 22:58:15.751000',NULL,NULL),(16,'Test báo cáo','1975-01-01','Báo cáo 11','2025-03-31 22:59:42.041000','2025-03-31 22:59:42.041000',NULL,NULL),(17,'Test báo cáo','2000-07-03','Báo cáo 31','2025-03-31 23:00:27.595000','2025-03-31 23:00:27.595000',NULL,NULL),(18,'Test báo cáo','1985-09-11','Báo cáo 31','2025-04-01 13:07:43.904000','2025-04-01 13:07:43.904000',NULL,NULL);
/*!40000 ALTER TABLE `report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resident`
--

DROP TABLE IF EXISTS `resident`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `resident` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cccd` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `dob` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `gender` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `phone_number` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resident`
--

LOCK TABLES `resident` WRITE;
/*!40000 ALTER TABLE `resident` DISABLE KEYS */;
INSERT INTO `resident` VALUES (10,'12345678912','2001-02-10','male','Nguyễn Văn A','0987654321','2024-12-23 12:30:56.000000','2025-01-01 03:25:54.000000'),(11,'123456789','2001-03-01','female','Cao Thị T','0136848021','2024-12-23 12:34:44.000000','2024-12-23 12:34:44.000000'),(12,'1234567890','1989-03-20','male','Cao Văn C','0123456789','2024-12-23 13:06:42.000000','2024-12-23 13:06:42.000000'),(14,'12345678901','2025-01-10','male','tu','0998821045','2025-01-01 03:24:10.000000','2025-01-01 03:24:10.000000'),(19,'0123456456','1992-06-15','male','Cao Văn D','0147147258','2025-03-16 15:28:16.000000','2025-03-16 15:28:16.000000'),(20,'321761601','1997-09-08','male','Nguyễn Trang','0082481061','2025-03-29 22:02:56.095000','2025-03-29 22:02:56.095000'),(21,'0052563692','91986-02-20','male','Đỗ Khoa','0435281858','2025-03-30 21:46:18.336000','2025-03-30 21:46:18.336000'),(22,'098218388','11983-02-08','female','Lê Bình','0339559240','2025-03-30 21:48:13.113000','2025-03-30 21:48:13.113000'),(23,'635754998776','2014-05-03','female','Hoàng Chi','0925403155','2025-03-30 21:50:07.191000','2025-03-30 21:50:07.191000'),(24,'4015551807','1993-12-03','female','Đặng Chi','0043504553','2025-03-30 21:53:20.395000','2025-03-30 21:53:20.395000'),(25,'121332346508','61982-02-20','female','Đỗ Phương','0167326306','2025-03-30 22:01:40.650000','2025-03-30 22:01:40.650000'),(26,'4966799394','51994-02-07','male','Ngô An','0296926372','2025-03-30 22:16:59.723000','2025-03-30 22:16:59.723000'),(27,'3025574352','2012-08-10','female','Đỗ Linh','0234621588','2025-03-30 22:17:53.391000','2025-03-30 22:17:53.391000'),(28,'878515054168','1980-02-08','female','Lý Khoa','0906402540','2025-03-30 22:22:01.131000','2025-03-30 22:22:01.131000'),(29,'04732023539','1997-07-08','male','Phạm Trang','0349978452','2025-03-30 22:28:18.404000','2025-03-30 22:28:18.404000'),(30,'252310905270','1978-03-03','male','Vũ Nam','0719134793','2025-03-30 22:30:35.241000','2025-03-30 22:30:35.241000'),(31,'92973045440','81988-02-07','male','Lý Trang','0376389422','2025-03-30 22:33:51.789000','2025-03-30 22:33:51.789000'),(32,'71911811765','81987-02-04','male','Dương Phương','0159857225','2025-03-30 22:35:11.019000','2025-03-30 22:35:11.019000'),(33,'413223894','2009-08-11','female','Lê Chi','0268008631','2025-03-30 22:37:39.578000','2025-03-30 22:37:39.578000'),(34,'1329598303','1987-09-12','male','Đỗ Minh','0300216993','2025-03-30 22:38:20.704000','2025-03-30 22:38:20.704000'),(35,'865734924900','12013-02-10','male','Phạm Dũng','0857046608','2025-03-30 22:39:58.676000','2025-03-30 22:39:58.676000'),(36,'382084620943','1986-12-07','male','Đặng Trang','0482029908','2025-03-30 22:43:58.704000','2025-03-30 22:43:58.704000');
/*!40000 ALTER TABLE `resident` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `password` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `role` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `username` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (4,'123456','Admin','userAdmin',NULL,NULL),(8,'123456',NULL,'Hy',NULL,NULL),(9,'123123zzz','admin','userHuy1',NULL,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-01 13:15:02
