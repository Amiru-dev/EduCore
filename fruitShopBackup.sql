-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: corevisi_fruit_shop_app
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `corevisi_fruit_shop_app`
--

/*!40000 DROP DATABASE IF EXISTS `corevisi_fruit_shop_app`*/;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `corevisi_fruit_shop_app` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `corevisi_fruit_shop_app`;

--
-- Table structure for table `brand`
--

DROP TABLE IF EXISTS `brand`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `brand` (
  `brand_id` int NOT NULL AUTO_INCREMENT,
  `brand_name` varchar(45) NOT NULL,
  `Description` text NOT NULL,
  PRIMARY KEY (`brand_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `brand`
--

LOCK TABLES `brand` WRITE;
/*!40000 ALTER TABLE `brand` DISABLE KEYS */;
INSERT INTO `brand` VALUES (1,'Agriessence','Essence of quality agri'),(2,'Bloombase','Foundation for fruitful growth'),(3,'RootRise ','Strong nurturing roots'),(4,'GoldenSprout ','Nurtured seedlings'),(5,'Rajarata','Laksiri');
/*!40000 ALTER TABLE `brand` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campaign_status`
--

DROP TABLE IF EXISTS `campaign_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campaign_status` (
  `campaign_status_id` int NOT NULL AUTO_INCREMENT,
  `campaign_status` varchar(45) NOT NULL,
  PRIMARY KEY (`campaign_status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campaign_status`
--

LOCK TABLES `campaign_status` WRITE;
/*!40000 ALTER TABLE `campaign_status` DISABLE KEYS */;
INSERT INTO `campaign_status` VALUES (1,'Active'),(2,'Inactive');
/*!40000 ALTER TABLE `campaign_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `campaigns`
--

DROP TABLE IF EXISTS `campaigns`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campaigns` (
  `campaign_id` bigint NOT NULL DEFAULT '0',
  `campaign_name` varchar(45) NOT NULL,
  `added_date` datetime DEFAULT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `budget` double NOT NULL,
  `target_audience` text NOT NULL,
  `campaign_status_id` int NOT NULL,
  `employee_NIC` varchar(30) NOT NULL,
  PRIMARY KEY (`campaign_id`),
  KEY `fk_campaigns_campaign_status1_idx` (`campaign_status_id`),
  KEY `fk_campaigns_employee1_idx` (`employee_NIC`),
  CONSTRAINT `fk_campaigns_campaign_status1` FOREIGN KEY (`campaign_status_id`) REFERENCES `campaign_status` (`campaign_status_id`),
  CONSTRAINT `fk_campaigns_employee1` FOREIGN KEY (`employee_NIC`) REFERENCES `employee` (`NIC`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campaigns`
--

LOCK TABLES `campaigns` WRITE;
/*!40000 ALTER TABLE `campaigns` DISABLE KEYS */;
INSERT INTO `campaigns` VALUES (1733491154260,'Black Friday','2024-12-06 19:23:23','2024-11-24','2024-11-30',1000,'Black Friday sales for social media',1,'200401910200');
/*!40000 ALTER TABLE `campaigns` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `city`
--

DROP TABLE IF EXISTS `city`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `city` (
  `city_id` int NOT NULL AUTO_INCREMENT,
  `city_name` varchar(45) NOT NULL,
  `district_district_id` int NOT NULL,
  PRIMARY KEY (`city_id`),
  KEY `fk_city_district1_idx` (`district_district_id`),
  CONSTRAINT `fk_city_district1` FOREIGN KEY (`district_district_id`) REFERENCES `district` (`district_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `city`
--

LOCK TABLES `city` WRITE;
/*!40000 ALTER TABLE `city` DISABLE KEYS */;
INSERT INTO `city` VALUES (1,'Kochchikade',1),(2,'Kadawatha',2);
/*!40000 ALTER TABLE `city` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `customer_nic` varchar(30) NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `email` varchar(100) NOT NULL,
  `mobile` varchar(10) NOT NULL,
  `customer_status_id` int NOT NULL,
  `employee_NIC` varchar(30) NOT NULL,
  PRIMARY KEY (`customer_nic`),
  KEY `fk_customer_customer_status1_idx` (`customer_status_id`),
  KEY `fk_customer_employee1_idx` (`employee_NIC`),
  CONSTRAINT `fk_customer_customer_status1` FOREIGN KEY (`customer_status_id`) REFERENCES `customer_status` (`customer_status_id`),
  CONSTRAINT `fk_customer_employee1` FOREIGN KEY (`employee_NIC`) REFERENCES `employee` (`NIC`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES ('200301910205','Sahan','Rasika','sahan@gmail.com','0775916288',1,'200401910200'),('200323458909','Danukasha','Sathsara','abcd@gmail.com','0715916290',1,'200401910200'),('200401910200','sheshan','danuksha','sheshandanuksha19@gmail.com','0715916293',2,'200401910200'),('200401910205','Senal','Dewmina','dewminaududeniya@gmail.com','0715916299',1,'200401910200'),('200510302774','Nayana','Amiru','nayana11@gmail.com','0776854375',1,'200401910200'),('200512501810','Dulanganaa','Dhamsara','isirassi@gmail.com','0704422502',1,'200401910200');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_address`
--

DROP TABLE IF EXISTS `customer_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer_address` (
  `customer_address_id` int NOT NULL AUTO_INCREMENT,
  `line1` varchar(45) NOT NULL,
  `line2` varchar(45) NOT NULL,
  `longtitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `city_city_id` int NOT NULL,
  `customer_nic` varchar(30) NOT NULL,
  PRIMARY KEY (`customer_address_id`),
  KEY `fk_supplier_address_city1_idx` (`city_city_id`),
  KEY `fk_customer_address_customer1_idx` (`customer_nic`),
  CONSTRAINT `fk_customer_address_customer1` FOREIGN KEY (`customer_nic`) REFERENCES `customer` (`customer_nic`),
  CONSTRAINT `fk_supplier_address_city10` FOREIGN KEY (`city_city_id`) REFERENCES `city` (`city_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_address`
--

LOCK TABLES `customer_address` WRITE;
/*!40000 ALTER TABLE `customer_address` DISABLE KEYS */;
INSERT INTO `customer_address` VALUES (2,'testline1','testline2',80.6561279296875,7.688536655726007,1,'200401910200'),(3,'testline1','asdasdsd',81.01318359375,8.053790678898539,1,'200401910200'),(5,'line1','line2',81.2274169921875,8.021155456563902,1,'200512501810'),(7,'test1','test2',80.73097229003906,7.90079051371585,1,'200401910205'),(8,'52/A','Obberiya',79.86227989196777,7.263883474586242,1,'200401910200'),(9,'Ab','AA',80.6121826171875,7.836173352103869,1,'200510302774');
/*!40000 ALTER TABLE `customer_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_status`
--

DROP TABLE IF EXISTS `customer_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer_status` (
  `customer_status_id` int NOT NULL AUTO_INCREMENT,
  `customer_status` varchar(45) NOT NULL,
  PRIMARY KEY (`customer_status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_status`
--

LOCK TABLES `customer_status` WRITE;
/*!40000 ALTER TABLE `customer_status` DISABLE KEYS */;
INSERT INTO `customer_status` VALUES (1,'Active'),(2,'Deactive');
/*!40000 ALTER TABLE `customer_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `delivery`
--

DROP TABLE IF EXISTS `delivery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `delivery` (
  `delivery_id` int NOT NULL AUTO_INCREMENT,
  `delivery_date` date NOT NULL,
  `placed_date` date NOT NULL,
  `driver_NIC` varchar(30) NOT NULL,
  `assign_manager_nic` varchar(30) NOT NULL,
  `delivery_status_id` int NOT NULL,
  `orders_order_id` bigint NOT NULL,
  `customer_address_id` int NOT NULL,
  PRIMARY KEY (`delivery_id`),
  KEY `fk_delivery_delivery_status1_idx` (`delivery_status_id`),
  KEY `fk_delivery_employee1_idx` (`driver_NIC`),
  KEY `fk_delivery_employee2_idx` (`assign_manager_nic`),
  KEY `fk_delivery_orders1_idx` (`orders_order_id`),
  KEY `fk_delivery_customer_address1_idx` (`customer_address_id`),
  CONSTRAINT `fk_delivery_customer_address1` FOREIGN KEY (`customer_address_id`) REFERENCES `customer_address` (`customer_address_id`),
  CONSTRAINT `fk_delivery_delivery_status1` FOREIGN KEY (`delivery_status_id`) REFERENCES `delivery_status` (`delivery_status_id`),
  CONSTRAINT `fk_delivery_employee1` FOREIGN KEY (`driver_NIC`) REFERENCES `employee` (`NIC`),
  CONSTRAINT `fk_delivery_employee2` FOREIGN KEY (`assign_manager_nic`) REFERENCES `employee` (`NIC`),
  CONSTRAINT `fk_delivery_orders1` FOREIGN KEY (`orders_order_id`) REFERENCES `orders` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `delivery`
--

LOCK TABLES `delivery` WRITE;
/*!40000 ALTER TABLE `delivery` DISABLE KEYS */;
INSERT INTO `delivery` VALUES (1,'2025-05-22','2025-05-21','200512501810','200401910200',2,1734360482490,3),(3,'2025-05-26','2025-05-25','200512501810','200401910200',3,1747838529799,7);
/*!40000 ALTER TABLE `delivery` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `delivery_status`
--

DROP TABLE IF EXISTS `delivery_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `delivery_status` (
  `delivery_status_id` int NOT NULL AUTO_INCREMENT,
  `delivery_status` varchar(45) NOT NULL,
  PRIMARY KEY (`delivery_status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `delivery_status`
--

LOCK TABLES `delivery_status` WRITE;
/*!40000 ALTER TABLE `delivery_status` DISABLE KEYS */;
INSERT INTO `delivery_status` VALUES (1,'In Progress'),(2,'Out Of Delivery'),(3,'Delivered'),(4,'Canceled');
/*!40000 ALTER TABLE `delivery_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `district`
--

DROP TABLE IF EXISTS `district`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `district` (
  `district_id` int NOT NULL AUTO_INCREMENT,
  `district_name` varchar(45) NOT NULL,
  `province_province_id` int NOT NULL,
  PRIMARY KEY (`district_id`),
  KEY `fk_district_province1_idx` (`province_province_id`),
  CONSTRAINT `fk_district_province1` FOREIGN KEY (`province_province_id`) REFERENCES `province` (`province_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `district`
--

LOCK TABLES `district` WRITE;
/*!40000 ALTER TABLE `district` DISABLE KEYS */;
INSERT INTO `district` VALUES (1,'Colombo',1),(2,'Gampaha',1);
/*!40000 ALTER TABLE `district` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `NIC` varchar(30) NOT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `email` varchar(100) NOT NULL,
  `mobile` varchar(10) NOT NULL,
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(30) DEFAULT NULL,
  `birthday` date NOT NULL,
  `registered_date` datetime NOT NULL,
  `biometrics_id` varchar(255) DEFAULT NULL,
  `employee_status_id` int NOT NULL,
  `gender_gender_id` int NOT NULL,
  `employee_type_employee_type_id` int NOT NULL,
  PRIMARY KEY (`NIC`),
  KEY `fk_employee_employee_status_idx` (`employee_status_id`),
  KEY `fk_employee_gender1_idx` (`gender_gender_id`),
  KEY `fk_employee_employee_type1_idx` (`employee_type_employee_type_id`),
  CONSTRAINT `fk_employee_employee_status` FOREIGN KEY (`employee_status_id`) REFERENCES `employee_status` (`employee_status_id`),
  CONSTRAINT `fk_employee_employee_type1` FOREIGN KEY (`employee_type_employee_type_id`) REFERENCES `employee_type` (`employee_type_id`),
  CONSTRAINT `fk_employee_gender1` FOREIGN KEY (`gender_gender_id`) REFERENCES `gender` (`gender_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES ('197480045210','Nihal','Perera','nihal@gmail.com','0765804167',NULL,NULL,'2024-12-22','2024-12-22 14:43:04',NULL,1,1,11),('197820045613','Supun','Gimhana','supun@gmail.com','0714500125',NULL,NULL,'2024-12-22','2024-12-22 14:38:41',NULL,1,1,11),('200019504968','yomal','kavinda','yomal@gmail.com','0704496947','yomal','123456','2024-11-15','2024-11-15 20:11:45',NULL,1,1,1),('200140191020','Rashintha','Jerom','rasi@gmail.com','0767048830','rashintha','123456','2024-11-15','2024-11-15 20:10:32',NULL,1,1,8),('200237894501','Kamal','Fernando','kamal@gmail.com','0768543015','kamal','123456','2015-11-24','2024-11-25 00:00:00',NULL,1,1,4),('200301828300','Senal','Dewinda','senal@gmail.com','0711112345','senal123','Sen@2342','2018-05-10','2025-05-24 00:00:00',NULL,1,1,8),('200301910200','PSD','Fernando','sheshandanuksha19@gmail.com','0715916299','sheshandf','123456','2025-05-18','2025-05-18 18:30:28',NULL,1,1,4),('200338403789','Dulasha','Perera','dulasha@gmail.com','0775894201','dulasha','123456','2001-11-16','2024-11-25 00:00:00',NULL,1,2,9),('200401910200','amiru','upek','amirupekw@gmail.com','0767048830','amiru','123456','2024-11-15','2024-11-15 20:08:02',NULL,1,1,1),('200401910201','Deshan','Suraweera','deshan@gmail.com','0775916299','desha','Deshan@33','2025-05-06','2025-05-20 00:00:00',NULL,1,1,10),('200403110370','sheshan','danuksha','sheshan@gmail.com','0784766700','sheshan','123456','2024-11-17','2024-11-17 07:59:37',NULL,1,1,2),('200403110371','siril','herath','siril@gmail.com','0785544500','siril','Secret1@','2026-07-03','2026-07-08 00:00:00',NULL,1,1,11),('200512501810','Dulangana','Dhamsara','dulanganadhamsara@gmail.com','0704422502','dulangana','123456','2024-11-25','2024-11-25 09:43:44',NULL,1,1,12);
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_address`
--

DROP TABLE IF EXISTS `employee_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_address` (
  `employee_address_id` int NOT NULL AUTO_INCREMENT,
  `line1` varchar(60) NOT NULL,
  `line2` varchar(60) NOT NULL,
  `postal_code` varchar(10) NOT NULL,
  `city_city_id` int NOT NULL,
  `employee_NIC` varchar(30) NOT NULL,
  PRIMARY KEY (`employee_address_id`),
  KEY `fk_employee_address_city1_idx` (`city_city_id`),
  KEY `fk_employee_address_employee1_idx` (`employee_NIC`),
  CONSTRAINT `fk_employee_address_city1` FOREIGN KEY (`city_city_id`) REFERENCES `city` (`city_id`),
  CONSTRAINT `fk_employee_address_employee1` FOREIGN KEY (`employee_NIC`) REFERENCES `employee` (`NIC`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_address`
--

LOCK TABLES `employee_address` WRITE;
/*!40000 ALTER TABLE `employee_address` DISABLE KEYS */;
INSERT INTO `employee_address` VALUES (1,'52/A','Obberiyaa','11540',1,'200301910200');
/*!40000 ALTER TABLE `employee_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_attendance`
--

DROP TABLE IF EXISTS `employee_attendance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_attendance` (
  `employee_attendance_id` int NOT NULL AUTO_INCREMENT,
  `date_time` date NOT NULL,
  `checkin_time` time NOT NULL,
  `checkout_time` time DEFAULT NULL,
  `employee_attendance_status_employee_attendance_status_id` int NOT NULL,
  `employee_attendance_type_employee_attendance_type_id` int NOT NULL,
  `employee_NIC` varchar(30) NOT NULL,
  PRIMARY KEY (`employee_attendance_id`),
  KEY `fk_employee_attendance_employee_attendance_status1_idx` (`employee_attendance_status_employee_attendance_status_id`),
  KEY `fk_employee_attendance_employee_attendance_type1_idx` (`employee_attendance_type_employee_attendance_type_id`),
  KEY `fk_employee_attendance_employee1_idx` (`employee_NIC`),
  CONSTRAINT `fk_employee_attendance_employee1` FOREIGN KEY (`employee_NIC`) REFERENCES `employee` (`NIC`),
  CONSTRAINT `fk_employee_attendance_employee_attendance_status1` FOREIGN KEY (`employee_attendance_status_employee_attendance_status_id`) REFERENCES `employee_attendance_status` (`employee_attendance_status_id`),
  CONSTRAINT `fk_employee_attendance_employee_attendance_type1` FOREIGN KEY (`employee_attendance_type_employee_attendance_type_id`) REFERENCES `employee_attendance_type` (`employee_attendance_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_attendance`
--

LOCK TABLES `employee_attendance` WRITE;
/*!40000 ALTER TABLE `employee_attendance` DISABLE KEYS */;
INSERT INTO `employee_attendance` VALUES (6,'2025-05-21','00:47:41','00:48:24',1,1,'200401910200'),(7,'2025-05-24','23:12:22','23:12:28',1,1,'200301828300'),(8,'2026-07-08','13:21:06',NULL,1,1,'200401910200');
/*!40000 ALTER TABLE `employee_attendance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_attendance_status`
--

DROP TABLE IF EXISTS `employee_attendance_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_attendance_status` (
  `employee_attendance_status_id` int NOT NULL AUTO_INCREMENT,
  `employee_attendance_status` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`employee_attendance_status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_attendance_status`
--

LOCK TABLES `employee_attendance_status` WRITE;
/*!40000 ALTER TABLE `employee_attendance_status` DISABLE KEYS */;
INSERT INTO `employee_attendance_status` VALUES (1,'Present'),(2,'Absent'),(3,'Late');
/*!40000 ALTER TABLE `employee_attendance_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_attendance_type`
--

DROP TABLE IF EXISTS `employee_attendance_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_attendance_type` (
  `employee_attendance_type_id` int NOT NULL AUTO_INCREMENT,
  `employee_attendance_type` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`employee_attendance_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_attendance_type`
--

LOCK TABLES `employee_attendance_type` WRITE;
/*!40000 ALTER TABLE `employee_attendance_type` DISABLE KEYS */;
INSERT INTO `employee_attendance_type` VALUES (1,'Manual'),(2,'Fingerprint');
/*!40000 ALTER TABLE `employee_attendance_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_leave`
--

DROP TABLE IF EXISTS `employee_leave`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_leave` (
  `employee_leave_id` int NOT NULL AUTO_INCREMENT,
  `employee_leave_type_id` int NOT NULL,
  `leave_start` date NOT NULL,
  `leave_end` date NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `reason` text NOT NULL,
  `employee_leave_status_id` int DEFAULT NULL,
  `employee_NIC` varchar(30) NOT NULL,
  PRIMARY KEY (`employee_leave_id`),
  KEY `fk_employee_leave_employee_leave_status1_idx` (`employee_leave_status_id`),
  KEY `fk_employee_leave_employee1_idx` (`employee_NIC`),
  KEY `fk_employee_leave_employee_leave_type1_idx` (`employee_leave_type_id`),
  CONSTRAINT `fk_employee_leave_employee1` FOREIGN KEY (`employee_NIC`) REFERENCES `employee` (`NIC`),
  CONSTRAINT `fk_employee_leave_employee_leave_status1` FOREIGN KEY (`employee_leave_status_id`) REFERENCES `employee_leave_status` (`employee_leave_status_id`),
  CONSTRAINT `fk_employee_leave_employee_leave_type1` FOREIGN KEY (`employee_leave_type_id`) REFERENCES `employee_leave_type` (`employee_leave_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_leave`
--

LOCK TABLES `employee_leave` WRITE;
/*!40000 ALTER TABLE `employee_leave` DISABLE KEYS */;
INSERT INTO `employee_leave` VALUES (2,1,'2025-05-06','2025-05-06','08:00:00','08:00:00','Sick',1,'200401910200'),(3,1,'2025-05-25','2025-05-31','08:00:00','08:00:00','Sick',3,'200301828300');
/*!40000 ALTER TABLE `employee_leave` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_leave_status`
--

DROP TABLE IF EXISTS `employee_leave_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_leave_status` (
  `employee_leave_status_id` int NOT NULL AUTO_INCREMENT,
  `employee_leave_status` varchar(45) NOT NULL,
  PRIMARY KEY (`employee_leave_status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_leave_status`
--

LOCK TABLES `employee_leave_status` WRITE;
/*!40000 ALTER TABLE `employee_leave_status` DISABLE KEYS */;
INSERT INTO `employee_leave_status` VALUES (1,'Approved'),(2,'Pending'),(3,'Rejected');
/*!40000 ALTER TABLE `employee_leave_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_leave_type`
--

DROP TABLE IF EXISTS `employee_leave_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_leave_type` (
  `employee_leave_type_id` int NOT NULL AUTO_INCREMENT,
  `employee_leave` varchar(45) NOT NULL,
  PRIMARY KEY (`employee_leave_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_leave_type`
--

LOCK TABLES `employee_leave_type` WRITE;
/*!40000 ALTER TABLE `employee_leave_type` DISABLE KEYS */;
INSERT INTO `employee_leave_type` VALUES (1,'Manual'),(2,'Fingerprint');
/*!40000 ALTER TABLE `employee_leave_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_salary`
--

DROP TABLE IF EXISTS `employee_salary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_salary` (
  `employee_salary_id` int NOT NULL AUTO_INCREMENT,
  `pay_date` datetime NOT NULL,
  `nopay` double DEFAULT NULL,
  `ot_hours` int DEFAULT NULL,
  `total_overtime` double DEFAULT NULL,
  `total_salary` double NOT NULL,
  `employee_NIC` varchar(30) NOT NULL,
  PRIMARY KEY (`employee_salary_id`),
  KEY `fk_employee_salary_employee1_idx` (`employee_NIC`),
  CONSTRAINT `fk_employee_salary_employee1` FOREIGN KEY (`employee_NIC`) REFERENCES `employee` (`NIC`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_salary`
--

LOCK TABLES `employee_salary` WRITE;
/*!40000 ALTER TABLE `employee_salary` DISABLE KEYS */;
INSERT INTO `employee_salary` VALUES (2,'2025-05-25 00:03:03',0,5,20000,281000,'200338403789'),(3,'2024-12-17 11:05:54',0,2,8000,269000,'200237894501'),(4,'2024-12-17 11:05:54',0,8,20000,159000,'200512501810'),(5,'2024-12-17 11:05:54',0,2,10000,390000,'200019504968'),(6,'2024-12-17 11:05:54',0,5,25000,405000,'200019504968'),(7,'2024-12-17 11:05:54',0,2,8000,172400,'200401910200'),(8,'2025-05-25 12:05:20',0,4,16000,180400,'200301828300');
/*!40000 ALTER TABLE `employee_salary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_salary_option`
--

DROP TABLE IF EXISTS `employee_salary_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_salary_option` (
  `id` int NOT NULL AUTO_INCREMENT,
  `basic_salary` double DEFAULT NULL,
  `medical_allowance` double DEFAULT NULL,
  `travel_allowance` double DEFAULT NULL,
  `ot_rate` double DEFAULT NULL,
  `epf` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_salary_option`
--

LOCK TABLES `employee_salary_option` WRITE;
/*!40000 ALTER TABLE `employee_salary_option` DISABLE KEYS */;
INSERT INTO `employee_salary_option` VALUES (1,250000,250000,250000,10000,8),(2,150000,70000,80000,7000,8),(3,70000,50000,50000,4000,8),(4,70000,50000,50000,4000,8),(5,70000,50000,50000,4000,8),(6,70000,50000,50000,4000,8),(7,170000,50000,50000,4000,8),(8,70000,50000,50000,4000,8),(9,70000,50000,50000,4000,8),(10,70000,50000,50000,4000,8),(11,70000,50000,50000,4000,8),(12,65000,50000,50000,4000,8);
/*!40000 ALTER TABLE `employee_salary_option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_status`
--

DROP TABLE IF EXISTS `employee_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_status` (
  `employee_status_id` int NOT NULL AUTO_INCREMENT,
  `employee_status_name` varchar(45) NOT NULL,
  PRIMARY KEY (`employee_status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_status`
--

LOCK TABLES `employee_status` WRITE;
/*!40000 ALTER TABLE `employee_status` DISABLE KEYS */;
INSERT INTO `employee_status` VALUES (1,'Active'),(2,'Deactive');
/*!40000 ALTER TABLE `employee_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_type`
--

DROP TABLE IF EXISTS `employee_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_type` (
  `employee_type_id` int NOT NULL AUTO_INCREMENT,
  `employee_type` varchar(45) NOT NULL,
  `employee_salary_option_id` int NOT NULL,
  PRIMARY KEY (`employee_type_id`),
  KEY `fk_employee_type_employee_salary_option1_idx` (`employee_salary_option_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_type`
--

LOCK TABLES `employee_type` WRITE;
/*!40000 ALTER TABLE `employee_type` DISABLE KEYS */;
INSERT INTO `employee_type` VALUES (1,'Admin',1),(2,'Production Manager',2),(3,'Quality Manager',3),(4,'Sales Manager',4),(5,'Supplier Manager',5),(6,'Trasport Manager',6),(7,'Marketing Manager',7),(8,'HR Manager',8),(9,'Finance Manager',9),(10,'Cashair',10),(11,'Worker',11),(12,'Driver',12);
/*!40000 ALTER TABLE `employee_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `finance`
--

DROP TABLE IF EXISTS `finance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `finance` (
  `transaction_id` bigint NOT NULL,
  `transaction_type_id` int NOT NULL,
  `amount` double NOT NULL,
  `Description` text NOT NULL,
  `date` datetime DEFAULT NULL,
  `payment_method_id` int NOT NULL,
  PRIMARY KEY (`transaction_id`),
  KEY `fk_finance_payment_method1_idx` (`payment_method_id`),
  KEY `fk_finance_transaction_type1_idx` (`transaction_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `finance`
--

LOCK TABLES `finance` WRITE;
/*!40000 ALTER TABLE `finance` DISABLE KEYS */;
INSERT INTO `finance` VALUES (1,1,1200,'Transport','2024-12-14 14:29:21',1),(1747834290062,1,300,'Travel','2025-05-21 19:01:27',1);
/*!40000 ALTER TABLE `finance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fruit`
--

DROP TABLE IF EXISTS `fruit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fruit` (
  `fruit_id` int NOT NULL AUTO_INCREMENT,
  `fruit_name` varchar(45) NOT NULL,
  PRIMARY KEY (`fruit_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fruit`
--

LOCK TABLES `fruit` WRITE;
/*!40000 ALTER TABLE `fruit` DISABLE KEYS */;
INSERT INTO `fruit` VALUES (1,'mango'),(2,'guava'),(3,'banana'),(4,'pineapple'),(5,'papaya'),(9,'apple'),(10,'Berry');
/*!40000 ALTER TABLE `fruit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gender`
--

DROP TABLE IF EXISTS `gender`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gender` (
  `gender_id` int NOT NULL AUTO_INCREMENT,
  `gender_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`gender_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gender`
--

LOCK TABLES `gender` WRITE;
/*!40000 ALTER TABLE `gender` DISABLE KEYS */;
INSERT INTO `gender` VALUES (1,'Male'),(2,'Female');
/*!40000 ALTER TABLE `gender` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grade`
--

DROP TABLE IF EXISTS `grade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grade` (
  `grade_id` int NOT NULL,
  `grade_type` varchar(45) NOT NULL,
  PRIMARY KEY (`grade_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grade`
--

LOCK TABLES `grade` WRITE;
/*!40000 ALTER TABLE `grade` DISABLE KEYS */;
INSERT INTO `grade` VALUES (1,'Good Condition'),(2,'Average'),(3,'Not Good');
/*!40000 ALTER TABLE `grade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grn`
--

DROP TABLE IF EXISTS `grn`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grn` (
  `grn_id` bigint NOT NULL,
  `date` datetime NOT NULL,
  `paid_amount` double NOT NULL,
  `payment_status_id` int NOT NULL,
  `supplier_id` int NOT NULL,
  `employee_NIC` varchar(30) NOT NULL,
  `grn_status_id` int NOT NULL,
  PRIMARY KEY (`grn_id`),
  KEY `fk_grn_payment_status1_idx` (`payment_status_id`),
  KEY `fk_grn_supplier1_idx` (`supplier_id`),
  KEY `fk_grn_employee1_idx` (`employee_NIC`),
  KEY `fk_grn_grn_status1_idx` (`grn_status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grn`
--

LOCK TABLES `grn` WRITE;
/*!40000 ALTER TABLE `grn` DISABLE KEYS */;
INSERT INTO `grn` VALUES (12172024,'2024-12-17 11:05:54',1500,1,1,'200512501810',2),(12172025,'2024-12-17 11:08:37',200,2,1,'200237894501',1),(1747763516288,'2025-05-20 23:22:08',1100,1,1,'200401910200',1),(1747834710245,'2025-05-21 19:08:37',11400,1,1,'200401910200',1),(1748111900258,'2025-05-25 00:09:27',170,1,4,'200401910200',2);
/*!40000 ALTER TABLE `grn` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grn_items`
--

DROP TABLE IF EXISTS `grn_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grn_items` (
  `grn_items_id` int NOT NULL AUTO_INCREMENT,
  `grn_grn_id` bigint NOT NULL,
  `qty` double NOT NULL,
  `unit_price` double NOT NULL,
  `raw_material_id` bigint NOT NULL,
  PRIMARY KEY (`grn_items_id`),
  KEY `fk_grn_items_grn1_idx` (`grn_grn_id`),
  KEY `fk_grn_items_raw_material1_idx` (`raw_material_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grn_items`
--

LOCK TABLES `grn_items` WRITE;
/*!40000 ALTER TABLE `grn_items` DISABLE KEYS */;
INSERT INTO `grn_items` VALUES (1,12172024,2,500,1731819898392),(2,12172024,2,250,1731818214156),(3,12172025,2,100,1731818300873),(4,1747763516288,100,10,1731818214156),(5,1747834710245,54,200,1731818214156),(6,1747834710245,20,30,1731818300873),(7,1748111900258,12,3,1748111760816),(8,1748111900258,33,4,1731818214156);
/*!40000 ALTER TABLE `grn_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grn_status`
--

DROP TABLE IF EXISTS `grn_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grn_status` (
  `grn_status_id` int NOT NULL AUTO_INCREMENT,
  `grn_status` varchar(45) NOT NULL,
  PRIMARY KEY (`grn_status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grn_status`
--

LOCK TABLES `grn_status` WRITE;
/*!40000 ALTER TABLE `grn_status` DISABLE KEYS */;
INSERT INTO `grn_status` VALUES (1,'Process'),(2,'Complete');
/*!40000 ALTER TABLE `grn_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `humidity`
--

DROP TABLE IF EXISTS `humidity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `humidity` (
  `humidity_id` int NOT NULL AUTO_INCREMENT,
  `humidity` varchar(45) NOT NULL,
  PRIMARY KEY (`humidity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `humidity`
--

LOCK TABLES `humidity` WRITE;
/*!40000 ALTER TABLE `humidity` DISABLE KEYS */;
INSERT INTO `humidity` VALUES (1,'30');
/*!40000 ALTER TABLE `humidity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory`
--

DROP TABLE IF EXISTS `inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory` (
  `inventory_id` bigint NOT NULL,
  `qty` int DEFAULT NULL,
  `total_weight` double NOT NULL,
  `date` date NOT NULL,
  `employee_NIC` varchar(30) NOT NULL,
  `plantation_id` bigint NOT NULL,
  PRIMARY KEY (`inventory_id`),
  KEY `fk_inventory_employee1_idx` (`employee_NIC`),
  KEY `fk_inventory_plantation1_idx` (`plantation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory`
--

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
INSERT INTO `inventory` VALUES (1731817924261,20,30,'2024-12-23','200403110370',1731817870417),(1731818032877,50,20,'2024-11-17','200403110370',1731817635998),(1731818068860,20,20,'2024-12-22','200403110370',1731817775893),(1731818163599,3,25,'2024-11-17','200403110370',1731817778785),(1747765391394,200,300,'2025-05-20','200401910200',1731817635998),(1747765761352,20,50,'2025-05-20','200401910200',1731817775893),(1748112217462,45,100,'2025-05-25','200401910200',1731817635998);
/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory_audit`
--

DROP TABLE IF EXISTS `inventory_audit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory_audit` (
  `inventory_audit_id` int NOT NULL AUTO_INCREMENT,
  `odit_date` datetime NOT NULL,
  `audit_reason` text NOT NULL,
  `audit_description` text NOT NULL,
  `inventory_inventory_id` bigint NOT NULL,
  `employee_NIC` varchar(30) NOT NULL,
  PRIMARY KEY (`inventory_audit_id`),
  KEY `fk_inventory_audit_inventory1_idx` (`inventory_inventory_id`),
  KEY `fk_inventory_audit_employee1_idx` (`employee_NIC`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory_audit`
--

LOCK TABLES `inventory_audit` WRITE;
/*!40000 ALTER TABLE `inventory_audit` DISABLE KEYS */;
INSERT INTO `inventory_audit` VALUES (1,'2024-11-30 03:05:15','test 345','test2222',1731817924261,'200401910200'),(2,'2025-05-16 00:00:00','Test test','Test test',1731818032877,'200401910200'),(5,'2025-05-24 00:00:00','test 333','test test',1731818032877,'200401910200');
/*!40000 ALTER TABLE `inventory_audit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `issue`
--

DROP TABLE IF EXISTS `issue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `issue` (
  `issue_id` int NOT NULL AUTO_INCREMENT,
  `description_of_issue` text NOT NULL,
  `issue_date` date NOT NULL,
  `employee_NIC` varchar(30) NOT NULL,
  `issue_type_issue_type_id` int NOT NULL,
  PRIMARY KEY (`issue_id`),
  KEY `fk_issue_employee1_idx` (`employee_NIC`),
  KEY `fk_issue_issue_type1_idx` (`issue_type_issue_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `issue`
--

LOCK TABLES `issue` WRITE;
/*!40000 ALTER TABLE `issue` DISABLE KEYS */;
INSERT INTO `issue` VALUES (16,'test','2025-04-12','200401910200',4),(17,'fire','2025-05-20','200401910200',4),(18,'test issue','2025-05-25','200401910200',4);
/*!40000 ALTER TABLE `issue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `issue_type`
--

DROP TABLE IF EXISTS `issue_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `issue_type` (
  `issue_type_id` int NOT NULL AUTO_INCREMENT,
  `issue_type` varchar(45) NOT NULL,
  PRIMARY KEY (`issue_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `issue_type`
--

LOCK TABLES `issue_type` WRITE;
/*!40000 ALTER TABLE `issue_type` DISABLE KEYS */;
INSERT INTO `issue_type` VALUES (4,'emergency');
/*!40000 ALTER TABLE `issue_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_item`
--

DROP TABLE IF EXISTS `order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_item` (
  `order_item_id` int NOT NULL AUTO_INCREMENT,
  `orders_order_id` bigint NOT NULL,
  `qty` double NOT NULL,
  `price` double NOT NULL,
  `packing_packing_id` bigint NOT NULL,
  PRIMARY KEY (`order_item_id`),
  KEY `fk_order_item_orders1_idx` (`orders_order_id`),
  KEY `fk_order_item_packing1_idx` (`packing_packing_id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item`
--

LOCK TABLES `order_item` WRITE;
/*!40000 ALTER TABLE `order_item` DISABLE KEYS */;
INSERT INTO `order_item` VALUES (14,1733166354532,5,100,1234),(15,1733166421323,7,100,1234),(16,1733491605124,5,100,1234),(17,1733517854532,5,100,1234),(18,1734360482490,6,200,1733686301638),(19,1734360482490,8,100,1234),(20,1738435221655,1,200,1733686301638),(21,1738435221655,2,99,1234),(22,1744746453047,1,180,1733686301638),(23,1744746453047,3,90,1234),(24,1747545440128,3,200,1733686301638),(25,1747545440128,2,180,1735921009481),(26,1747687474448,2,180,1733686301638),(27,1747687474448,2,180,1234),(28,1747766059356,2,180,1733686301638),(29,1747766059356,3,90,1234),(30,1747838315712,3,180,1733686301638),(31,1747838315712,5,90,1234),(32,1747838529799,2,90,1234),(33,1747838529799,3,180,1735921009481),(34,1748017145985,2,180,1733686301638),(35,1748017145985,3,180,1234),(36,1748090777700,3,200,1733686301638),(37,1748090777700,11,90,1234),(38,1748113123221,4,90,1234),(39,1748113123221,5,200,1735921009481),(40,1748113157102,5,90,1234),(41,1748118600251,2,200,1748112908956),(42,1748118600251,2,100,1234);
/*!40000 ALTER TABLE `order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_status`
--

DROP TABLE IF EXISTS `order_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_status` (
  `order_status_id` int NOT NULL AUTO_INCREMENT,
  `order_status` varchar(45) NOT NULL,
  PRIMARY KEY (`order_status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_status`
--

LOCK TABLES `order_status` WRITE;
/*!40000 ALTER TABLE `order_status` DISABLE KEYS */;
INSERT INTO `order_status` VALUES (1,'Process'),(2,'Complete'),(3,'On Delivery');
/*!40000 ALTER TABLE `order_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `order_id` bigint NOT NULL,
  `customer_nic` varchar(30) NOT NULL,
  `date` datetime NOT NULL,
  `paid_amount` double DEFAULT NULL,
  `payment_date` date DEFAULT NULL,
  `order_status_id` int NOT NULL,
  `payment_method_id` int DEFAULT NULL,
  `payment_status_id` int NOT NULL,
  `employee_NIC` varchar(30) NOT NULL,
  PRIMARY KEY (`order_id`),
  KEY `fk_orders_customer1_idx` (`customer_nic`),
  KEY `fk_orders_order_status1_idx` (`order_status_id`),
  KEY `fk_orders_payment_status1_idx` (`payment_status_id`),
  KEY `fk_orders_employee1_idx` (`employee_NIC`),
  KEY `FK_orders_payment_method` (`payment_method_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1733166354532,'200401910200','2024-12-03 00:36:43',NULL,NULL,1,NULL,2,'200401910200'),(1733166421323,'200512501810','2024-12-03 00:37:13',NULL,NULL,2,NULL,2,'200401910200'),(1733491605124,'200401910200','2024-12-06 18:56:58',600,'2024-12-06',2,3,1,'200401910200'),(1733517854532,'200401910200','2024-12-07 02:14:39',NULL,NULL,1,NULL,2,'200401910200'),(1734360482490,'200401910200','2024-12-16 20:18:54',2000,'2024-12-23',3,3,1,'200401910200'),(1738435221655,'200401910205','2025-02-02 00:10:43',400,'2025-02-02',2,1,1,'200401910200'),(1744746453047,'200401910200','2025-04-16 01:18:30',450,'2025-04-16',3,1,1,'200401910200'),(1747545440128,'200401910200','2025-05-18 10:47:56',960,'2025-05-18',2,1,1,'200401910200'),(1747687474448,'200401910200','2025-05-20 02:15:12',800,'2025-05-20',3,2,1,'200401910200'),(1747766059356,'200401910200','2025-05-21 00:05:29',700,'2025-05-21',3,3,1,'200401910200'),(1747838315712,'200323458909','2025-05-21 20:09:49',NULL,NULL,1,NULL,2,'200401910200'),(1747838529799,'200401910205','2025-05-21 20:12:36',800,'2025-05-21',3,1,1,'200401910200'),(1748017145985,'200401910200','2025-05-23 21:51:02',1000,'2025-06-23',3,2,1,'200401910200'),(1748090777700,'200323458909','2025-05-24 18:16:52',1600,'2025-05-24',3,1,1,'200401910200'),(1748113123221,'200323458909','2025-05-25 00:29:17',NULL,NULL,1,NULL,2,'200401910200'),(1748113157102,'200401910200','2025-05-25 00:29:43',500,'2025-05-25',3,2,1,'200401910200'),(1748118600251,'200401910200','2025-05-25 02:02:24',700,'2025-05-25',3,1,1,'200301910200');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `overtime`
--

DROP TABLE IF EXISTS `overtime`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `overtime` (
  `overtime_id` int NOT NULL AUTO_INCREMENT,
  `overtime_date` date NOT NULL,
  `hours_worked` double DEFAULT NULL,
  `overtime_rate` double DEFAULT NULL,
  `employee_NIC` varchar(30) NOT NULL,
  PRIMARY KEY (`overtime_id`),
  KEY `fk_overtime_employee1_idx` (`employee_NIC`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `overtime`
--

LOCK TABLES `overtime` WRITE;
/*!40000 ALTER TABLE `overtime` DISABLE KEYS */;
/*!40000 ALTER TABLE `overtime` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `packing`
--

DROP TABLE IF EXISTS `packing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `packing` (
  `packing_id` bigint NOT NULL,
  `qty` int NOT NULL,
  `date` datetime NOT NULL,
  `packing_status` int NOT NULL,
  `price` double NOT NULL,
  `employee_NIC` varchar(30) NOT NULL,
  `product_quality_id` int NOT NULL,
  `storage_section_id` int NOT NULL,
  `packing_type_id` int NOT NULL,
  PRIMARY KEY (`packing_id`),
  KEY `fk_packing_packing_status1_idx` (`packing_status`),
  KEY `fk_packing_employee1_idx` (`employee_NIC`),
  KEY `fk_packing_product_quality1_idx` (`product_quality_id`),
  KEY `fk_packing_storage_section1_idx` (`storage_section_id`),
  KEY `fk_packing_packing_type1_idx` (`packing_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `packing`
--

LOCK TABLES `packing` WRITE;
/*!40000 ALTER TABLE `packing` DISABLE KEYS */;
INSERT INTO `packing` VALUES (1733686301638,10,'2024-12-09 01:02:52',1,200,'200019504968',2,5,2),(1735921009481,5,'2025-01-03 09:48:15',1,200,'200019504968',2,1,1),(1748112908956,43,'2025-05-25 12:28:05',1,250,'200401910200',7,5,3),(1748143437214,70,'2025-05-25 08:54:20',1,50,'200401910200',2,7,1),(1748143461429,50,'2025-05-25 08:54:56',1,300,'200401910200',7,7,2),(1748143519189,50,'2025-05-25 08:56:06',1,400,'200401910200',8,7,1);
/*!40000 ALTER TABLE `packing` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `packing_status`
--

DROP TABLE IF EXISTS `packing_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `packing_status` (
  `packing_status` int NOT NULL AUTO_INCREMENT,
  `packing_status_type` varchar(45) NOT NULL,
  PRIMARY KEY (`packing_status`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `packing_status`
--

LOCK TABLES `packing_status` WRITE;
/*!40000 ALTER TABLE `packing_status` DISABLE KEYS */;
INSERT INTO `packing_status` VALUES (1,'Complete'),(2,'Not Complete');
/*!40000 ALTER TABLE `packing_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `packing_type`
--

DROP TABLE IF EXISTS `packing_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `packing_type` (
  `packing_type_id` int NOT NULL AUTO_INCREMENT,
  `packing_type` varchar(45) NOT NULL,
  PRIMARY KEY (`packing_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `packing_type`
--

LOCK TABLES `packing_type` WRITE;
/*!40000 ALTER TABLE `packing_type` DISABLE KEYS */;
INSERT INTO `packing_type` VALUES (1,'1KG'),(2,'5Kg'),(3,'10KG'),(4,'1 Item'),(5,'5 Item'),(6,'10 Item');
/*!40000 ALTER TABLE `packing_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `payment_id` int NOT NULL AUTO_INCREMENT,
  `amount` double NOT NULL,
  `date` datetime NOT NULL,
  `payment_status_id` int NOT NULL,
  `grn_grn_id` bigint NOT NULL,
  `payment_method_id` int NOT NULL,
  PRIMARY KEY (`payment_id`),
  KEY `fk_payment_payment_status1_idx` (`payment_status_id`),
  KEY `fk_payment_grn1_idx` (`grn_grn_id`),
  KEY `fk_payment_payment_method1_idx` (`payment_method_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
INSERT INTO `payment` VALUES (1,1100,'2025-05-20 23:22:08',1,1747763516288,1),(2,11400,'2025-05-21 19:08:37',1,1747834710245,2),(3,170,'2025-05-25 00:00:00',1,1748111900258,1);
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_method`
--

DROP TABLE IF EXISTS `payment_method`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_method` (
  `payment_method_id` int NOT NULL AUTO_INCREMENT,
  `payment_method` varchar(45) NOT NULL,
  PRIMARY KEY (`payment_method_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_method`
--

LOCK TABLES `payment_method` WRITE;
/*!40000 ALTER TABLE `payment_method` DISABLE KEYS */;
INSERT INTO `payment_method` VALUES (1,'Cash'),(2,'Card'),(3,'Check');
/*!40000 ALTER TABLE `payment_method` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment_status`
--

DROP TABLE IF EXISTS `payment_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_status` (
  `payment_status_id` int NOT NULL AUTO_INCREMENT,
  `payment_status` varchar(45) NOT NULL,
  PRIMARY KEY (`payment_status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_status`
--

LOCK TABLES `payment_status` WRITE;
/*!40000 ALTER TABLE `payment_status` DISABLE KEYS */;
INSERT INTO `payment_status` VALUES (1,'Paid'),(2,'Not paid');
/*!40000 ALTER TABLE `payment_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `plantation`
--

DROP TABLE IF EXISTS `plantation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `plantation` (
  `plantation_id` bigint NOT NULL,
  `date` datetime NOT NULL,
  `qty` int NOT NULL DEFAULT '0',
  `fruit_fruit_id` int NOT NULL,
  `yield_location_id` int NOT NULL,
  PRIMARY KEY (`plantation_id`),
  KEY `fk_plantation_fruit1_idx` (`fruit_fruit_id`),
  KEY `fk_plantation_yield_location1_idx` (`yield_location_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `plantation`
--

LOCK TABLES `plantation` WRITE;
/*!40000 ALTER TABLE `plantation` DISABLE KEYS */;
INSERT INTO `plantation` VALUES (1731817635998,'2024-12-22 16:03:22',20,1,2),(1731817775893,'2024-12-22 16:03:38',25,5,3),(1731817778785,'2024-11-17 10:05:55',5,3,4),(1731817870417,'2024-12-23 11:49:37',30,4,5),(1747765111933,'2025-05-20 23:48:46',40,1,2),(1748112141586,'2025-05-25 00:13:03',38,10,7);
/*!40000 ALTER TABLE `plantation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `plantation_has_raw_material`
--

DROP TABLE IF EXISTS `plantation_has_raw_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `plantation_has_raw_material` (
  `plantation_plantation_id` bigint NOT NULL,
  `raw_material_raw_material_id` bigint NOT NULL,
  PRIMARY KEY (`raw_material_raw_material_id`,`plantation_plantation_id`),
  KEY `fk_plantation_has_raw material_raw material1_idx` (`raw_material_raw_material_id`),
  KEY `fk_plantation_has_raw material_plantation1_idx` (`plantation_plantation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `plantation_has_raw_material`
--

LOCK TABLES `plantation_has_raw_material` WRITE;
/*!40000 ALTER TABLE `plantation_has_raw_material` DISABLE KEYS */;
INSERT INTO `plantation_has_raw_material` VALUES (1731817635998,1731818214156),(1731817778785,1731818214156),(1731817635998,1731818300873),(1731817778785,1731818393271),(1731817778785,1731818449023),(1748112141586,1731818449023),(1731817775893,1731819945549),(1731817778785,1731819945549),(1731817775893,1731820075562);
/*!40000 ALTER TABLE `plantation_has_raw_material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_quality`
--

DROP TABLE IF EXISTS `product_quality`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_quality` (
  `product_quality_id` int NOT NULL AUTO_INCREMENT,
  `inventory_inventory_id` bigint NOT NULL,
  `date` datetime NOT NULL,
  `grade_grade_id` int NOT NULL,
  `employee_NIC` varchar(30) NOT NULL,
  `count` double NOT NULL,
  PRIMARY KEY (`product_quality_id`),
  KEY `fk_product_quality_inventory1_idx` (`inventory_inventory_id`),
  KEY `fk_product_quality_grade1_idx` (`grade_grade_id`),
  KEY `fk_product_quality_employee1_idx` (`employee_NIC`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_quality`
--

LOCK TABLES `product_quality` WRITE;
/*!40000 ALTER TABLE `product_quality` DISABLE KEYS */;
INSERT INTO `product_quality` VALUES (2,1731817924261,'2024-12-06 00:11:08',1,'200019504968',40),(3,1731818032877,'2024-12-04 16:01:01',1,'200019504968',20),(6,1731817924261,'2024-12-07 09:55:44',1,'200019504968',2),(7,1731818032877,'2025-05-25 00:25:38',1,'200301910200',45),(8,1731818163599,'2025-05-25 08:55:44',1,'200301910200',40);
/*!40000 ALTER TABLE `product_quality` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_returns`
--

DROP TABLE IF EXISTS `product_returns`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_returns` (
  `return_id` bigint NOT NULL,
  `return_date` datetime NOT NULL,
  `refund_amount` double NOT NULL,
  `reason` text,
  `customer_nic` varchar(30) NOT NULL,
  `employee_NIC` varchar(30) NOT NULL,
  PRIMARY KEY (`return_id`),
  KEY `fk_product_returns_customer1_idx` (`customer_nic`),
  KEY `fk_product_returns_employee1_idx` (`employee_NIC`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_returns`
--

LOCK TABLES `product_returns` WRITE;
/*!40000 ALTER TABLE `product_returns` DISABLE KEYS */;
INSERT INTO `product_returns` VALUES (1733515590778,'2024-12-07 01:36:48',700,'','200401910200','200401910200'),(1733515646491,'2024-12-07 01:37:42',600,'abcd','200401910200','200401910200'),(1733516209590,'2024-12-07 01:47:15',1300,'','200401910200','200401910200'),(1733516382406,'2024-12-07 01:49:59',600,'','200401910200','200401910200'),(1733516399807,'2024-12-07 01:50:09',100,'','200401910200','200401910200'),(1733689358630,'2024-12-09 01:53:15',1600,'','200401910200','200401910200'),(1747545965735,'2025-05-18 10:56:29',2000,'','200512501810','200401910200'),(1747687474450,'2025-05-20 02:17:15',1600,'','200401910200','200401910200'),(1747766059358,'2025-05-21 00:07:37',1200,'','200401910200','200401910200'),(1748017145986,'2025-05-23 21:53:42',400,'','200323458909','200401910200'),(1748090777701,'2025-05-24 18:18:04',6000,'','200401910200','200401910200'),(1748113123222,'2025-05-25 00:30:47',800,'not good','200323458909','200401910200'),(1748118600253,'2025-05-25 02:05:44',1750,'not good','200323458909','200301910200');
/*!40000 ALTER TABLE `product_returns` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `province`
--

DROP TABLE IF EXISTS `province`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `province` (
  `province_id` int NOT NULL AUTO_INCREMENT,
  `province_name` varchar(45) NOT NULL,
  PRIMARY KEY (`province_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `province`
--

LOCK TABLES `province` WRITE;
/*!40000 ALTER TABLE `province` DISABLE KEYS */;
INSERT INTO `province` VALUES (1,'Western Province');
/*!40000 ALTER TABLE `province` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `raw_material`
--

DROP TABLE IF EXISTS `raw_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `raw_material` (
  `raw_material_id` bigint NOT NULL,
  `raw_material_name` varchar(45) NOT NULL,
  `unit_price` double DEFAULT NULL,
  `qty` double DEFAULT NULL,
  `brand_brand_id` int DEFAULT NULL,
  `supplier_supplier_id` int DEFAULT NULL,
  PRIMARY KEY (`raw_material_id`),
  KEY `fk_raw material_brand1_idx` (`brand_brand_id`),
  KEY `fk_raw material_supplier1_idx` (`supplier_supplier_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `raw_material`
--

LOCK TABLES `raw_material` WRITE;
/*!40000 ALTER TABLE `raw_material` DISABLE KEYS */;
INSERT INTO `raw_material` VALUES (1731818214156,'mango seeds',NULL,NULL,NULL,NULL),(1731818300873,'nitrogen fertilizers',NULL,NULL,NULL,NULL),(1731818393271,'compost',NULL,NULL,NULL,NULL),(1731818449023,'seedlings',NULL,NULL,NULL,NULL),(1731818606011,'fungicides',NULL,NULL,NULL,NULL),(1731819898392,'gypsum',NULL,NULL,NULL,NULL),(1731819945549,'mulch',NULL,NULL,NULL,NULL),(1731820075562,'papaya seeds',NULL,NULL,NULL,NULL),(1747763374903,'Beans seeds',10,100,1,1),(1748111760816,'Maze',35,42,5,1);
/*!40000 ALTER TABLE `raw_material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `return_item`
--

DROP TABLE IF EXISTS `return_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `return_item` (
  `return_item_id` int NOT NULL AUTO_INCREMENT,
  `return_id` bigint NOT NULL,
  `qty` double NOT NULL,
  `packing_packing_id` bigint NOT NULL,
  PRIMARY KEY (`return_item_id`),
  KEY `fk_return_item_product_returns1_idx` (`return_id`),
  KEY `fk_return_item_packing1_idx` (`packing_packing_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `return_item`
--

LOCK TABLES `return_item` WRITE;
/*!40000 ALTER TABLE `return_item` DISABLE KEYS */;
INSERT INTO `return_item` VALUES (1,1733515590778,7,1234),(2,1733515646491,6,1234),(3,1733516209590,13,1234),(4,1733516382406,6,1234),(5,1733516399807,1,1234),(6,1733689358630,8,1733686301638),(7,1747545965735,10,1735921009481),(8,1747687474450,6,1733686301638),(9,1747687474450,4,1234),(10,1747766059358,3,1733686301638),(11,1747766059358,3,1735921009481),(12,1748017145986,2,1733686301638),(13,1748090777701,60,1234),(14,1748113123222,4,1733686301638),(15,1748118600253,5,1748112908956),(16,1748118600253,5,1234);
/*!40000 ALTER TABLE `return_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `storage_section`
--

DROP TABLE IF EXISTS `storage_section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `storage_section` (
  `storage_section_id` int NOT NULL AUTO_INCREMENT,
  `storage_section` varchar(45) NOT NULL,
  `humidity_id` int NOT NULL,
  `temperature_id` int NOT NULL,
  PRIMARY KEY (`storage_section_id`),
  KEY `fk_storage_section_humidity1_idx` (`humidity_id`),
  KEY `fk_storage_section_temperature1_idx` (`temperature_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `storage_section`
--

LOCK TABLES `storage_section` WRITE;
/*!40000 ALTER TABLE `storage_section` DISABLE KEYS */;
INSERT INTO `storage_section` VALUES (1,'banana',1,2),(5,'pineapple',1,2),(6,'apple',1,1),(7,'SectionB',1,1);
/*!40000 ALTER TABLE `storage_section` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier`
--

DROP TABLE IF EXISTS `supplier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier` (
  `supplier_id` int NOT NULL AUTO_INCREMENT,
  `fname` varchar(45) DEFAULT NULL,
  `lname` varchar(45) DEFAULT NULL,
  `contact` varchar(10) DEFAULT NULL,
  `email` varchar(80) DEFAULT NULL,
  `supplier_status_id` int NOT NULL,
  PRIMARY KEY (`supplier_id`),
  KEY `fk_supplier_supplier_status1_idx` (`supplier_status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier`
--

LOCK TABLES `supplier` WRITE;
/*!40000 ALTER TABLE `supplier` DISABLE KEYS */;
INSERT INTO `supplier` VALUES (1,'Dulasha','Perera','0770695026','dulashskauamdi@gmail.com',1),(2,'Palitha','Wasantha','0771234523','palitha@gmail.com',2),(3,'PSD','Fernando','0715916293','psd@gmail.com',1),(4,'Sheshan','Danuksha','0715916293','Sheshandanuksha19@gmail.com',1);
/*!40000 ALTER TABLE `supplier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier_address`
--

DROP TABLE IF EXISTS `supplier_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier_address` (
  `supplier_address_id` int NOT NULL AUTO_INCREMENT,
  `line1` varchar(45) NOT NULL,
  `line2` varchar(45) NOT NULL,
  `city_city_id` int NOT NULL,
  `supplier_id` int NOT NULL,
  PRIMARY KEY (`supplier_address_id`),
  KEY `fk_supplier_address_city1_idx` (`city_city_id`),
  KEY `fk_supplier_address_supplier1_idx` (`supplier_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier_address`
--

LOCK TABLES `supplier_address` WRITE;
/*!40000 ALTER TABLE `supplier_address` DISABLE KEYS */;
INSERT INTO `supplier_address` VALUES (1,'274/C','Colombo',1,1),(2,'500/D','Gampaha',2,2),(3,'52/A','Obberiyaa',1,3),(4,'52/A','Obberiya',1,4);
/*!40000 ALTER TABLE `supplier_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier_has_brand`
--

DROP TABLE IF EXISTS `supplier_has_brand`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier_has_brand` (
  `supplier_has_brand_id` int NOT NULL AUTO_INCREMENT,
  `supplier_supplier_id` int NOT NULL,
  `brand_brand_id` int NOT NULL,
  PRIMARY KEY (`supplier_has_brand_id`) USING BTREE,
  KEY `fk_supplier_has_brand_brand1_idx` (`brand_brand_id`),
  KEY `fk_supplier_has_brand_supplier1_idx` (`supplier_supplier_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier_has_brand`
--

LOCK TABLES `supplier_has_brand` WRITE;
/*!40000 ALTER TABLE `supplier_has_brand` DISABLE KEYS */;
INSERT INTO `supplier_has_brand` VALUES (1,1,1),(2,2,2),(3,2,4),(4,1,3),(5,2,5);
/*!40000 ALTER TABLE `supplier_has_brand` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier_status`
--

DROP TABLE IF EXISTS `supplier_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier_status` (
  `supplier_status_id` int NOT NULL AUTO_INCREMENT,
  `supplier_status` varchar(45) NOT NULL,
  PRIMARY KEY (`supplier_status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier_status`
--

LOCK TABLES `supplier_status` WRITE;
/*!40000 ALTER TABLE `supplier_status` DISABLE KEYS */;
INSERT INTO `supplier_status` VALUES (1,'Active'),(2,'Inactive');
/*!40000 ALTER TABLE `supplier_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task` (
  `task_id` int NOT NULL AUTO_INCREMENT,
  `employee_NIC` varchar(30) NOT NULL,
  `task_name_id` int NOT NULL,
  `task_description` text NOT NULL,
  `assigned_date` datetime NOT NULL,
  `due_date` datetime NOT NULL,
  `task_status_id` int NOT NULL,
  `completion_date` datetime DEFAULT NULL,
  `tasak_assigned_by` varchar(30) NOT NULL,
  PRIMARY KEY (`task_id`),
  KEY `fk_task_employee1_idx` (`employee_NIC`),
  KEY `fk_task_task_status1_idx` (`task_status_id`),
  KEY `fk_task_employee2_idx` (`tasak_assigned_by`),
  KEY `fk_task_task_name1_idx` (`task_name_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` VALUES (9,'197350012890',3,'inspect plantation','2025-01-03 16:55:51','2025-01-05 10:00:00',1,'2025-01-05 12:00:00','200401910200'),(10,'197452148720',4,'harvest fruits','2025-01-03 16:59:30','2025-01-06 08:00:00',2,'2025-01-07 12:10:00','200401910200'),(11,'197350012890',5,'digging','2025-01-03 17:02:06','2025-01-07 09:00:00',3,'2025-01-07 17:29:00','200401910200'),(12,'197480045210',6,'cleaning yard','2025-01-03 17:07:40','2025-01-08 14:00:00',1,NULL,'200401910200'),(13,'197820045613',7,'sorting yields','2025-01-03 17:09:11','2025-01-09 16:00:00',1,'2025-01-10 09:29:00','200401910200'),(14,'197820045613',3,'ongoing under supervision','2025-01-22 19:13:08','2025-01-22 19:12:00',1,'2025-01-22 20:13:00','200401910200'),(15,'197820045613',4,'supervising strictly','2025-01-22 19:14:59','2025-01-27 19:13:00',2,'2025-01-27 20:16:00','200401910200'),(16,'197452148720',3,'clean','2025-05-20 23:51:05','2025-05-21 23:48:00',1,'2025-05-21 23:51:00','200401910200'),(17,'197350012890',3,'task to do','2025-05-25 00:18:22','2025-05-20 00:10:00',1,NULL,'200401910200');
/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_name`
--

DROP TABLE IF EXISTS `task_name`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_name` (
  `task_name_id` int NOT NULL AUTO_INCREMENT,
  `task_name` varchar(45) NOT NULL,
  PRIMARY KEY (`task_name_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_name`
--

LOCK TABLES `task_name` WRITE;
/*!40000 ALTER TABLE `task_name` DISABLE KEYS */;
INSERT INTO `task_name` VALUES (3,'planting'),(4,'harvesting'),(5,'checking'),(6,'cleaning'),(7,'sorting');
/*!40000 ALTER TABLE `task_name` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_status`
--

DROP TABLE IF EXISTS `task_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task_status` (
  `task_status_id` int NOT NULL AUTO_INCREMENT,
  `task_status` varchar(45) NOT NULL,
  PRIMARY KEY (`task_status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_status`
--

LOCK TABLES `task_status` WRITE;
/*!40000 ALTER TABLE `task_status` DISABLE KEYS */;
INSERT INTO `task_status` VALUES (1,'ongoing'),(2,'half completed'),(3,'did not started');
/*!40000 ALTER TABLE `task_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `temperature`
--

DROP TABLE IF EXISTS `temperature`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `temperature` (
  `temperature_id` int NOT NULL AUTO_INCREMENT,
  `temperature` varchar(45) NOT NULL,
  PRIMARY KEY (`temperature_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `temperature`
--

LOCK TABLES `temperature` WRITE;
/*!40000 ALTER TABLE `temperature` DISABLE KEYS */;
INSERT INTO `temperature` VALUES (1,'25-30'),(2,'20-40');
/*!40000 ALTER TABLE `temperature` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_table`
--

DROP TABLE IF EXISTS `test_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_table` (
  `id` int NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_table`
--

LOCK TABLES `test_table` WRITE;
/*!40000 ALTER TABLE `test_table` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tool`
--

DROP TABLE IF EXISTS `tool`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tool` (
  `tool_id` int NOT NULL AUTO_INCREMENT,
  `tool_neme` varchar(45) NOT NULL,
  `purchase_date` date NOT NULL,
  `price` double NOT NULL,
  `work` text NOT NULL,
  PRIMARY KEY (`tool_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tool`
--

LOCK TABLES `tool` WRITE;
/*!40000 ALTER TABLE `tool` DISABLE KEYS */;
INSERT INTO `tool` VALUES (1,'Rakes','2024-11-25',600,'for soil preparation'),(2,'Hose','2024-11-25',300,'textarea'),(3,'Ladders','2024-11-29',6000,'For reaching to difficult palces.'),(4,'RakeA','2025-05-25',3666,'Raking');
/*!40000 ALTER TABLE `tool` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tool_quality_check`
--

DROP TABLE IF EXISTS `tool_quality_check`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tool_quality_check` (
  `tool_quality_check` int NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `comment` text NOT NULL,
  `tool_quality_check_status_id` int NOT NULL,
  `tool_tool_id` int NOT NULL,
  `employee_NIC` varchar(30) NOT NULL,
  PRIMARY KEY (`tool_quality_check`),
  KEY `fk_tool_quality_check_tool_quality_check_status1_idx` (`tool_quality_check_status_id`),
  KEY `fk_tool_quality_check_tool1_idx` (`tool_tool_id`),
  KEY `fk_tool_quality_check_employee1_idx` (`employee_NIC`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tool_quality_check`
--

LOCK TABLES `tool_quality_check` WRITE;
/*!40000 ALTER TABLE `tool_quality_check` DISABLE KEYS */;
INSERT INTO `tool_quality_check` VALUES (1,'2024-11-28','broken ',3,1,'200019504968'),(2,'2024-11-28','broken soon',2,2,'200019504968');
/*!40000 ALTER TABLE `tool_quality_check` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tool_quality_check_status`
--

DROP TABLE IF EXISTS `tool_quality_check_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tool_quality_check_status` (
  `tool_quality_check_status_id` int NOT NULL AUTO_INCREMENT,
  `tool_quality_check_status_type` varchar(45) NOT NULL,
  PRIMARY KEY (`tool_quality_check_status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tool_quality_check_status`
--

LOCK TABLES `tool_quality_check_status` WRITE;
/*!40000 ALTER TABLE `tool_quality_check_status` DISABLE KEYS */;
INSERT INTO `tool_quality_check_status` VALUES (1,'Good'),(2,'Average'),(3,'Broken');
/*!40000 ALTER TABLE `tool_quality_check_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction_type`
--

DROP TABLE IF EXISTS `transaction_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction_type` (
  `transaction_type_id` int NOT NULL AUTO_INCREMENT,
  `transaction_type_name` varchar(45) NOT NULL,
  PRIMARY KEY (`transaction_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction_type`
--

LOCK TABLES `transaction_type` WRITE;
/*!40000 ALTER TABLE `transaction_type` DISABLE KEYS */;
INSERT INTO `transaction_type` VALUES (1,'Credit'),(2,'Debit');
/*!40000 ALTER TABLE `transaction_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicle`
--

DROP TABLE IF EXISTS `vehicle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicle` (
  `licens_plate` varchar(45) NOT NULL,
  `year_of_manufacture` date NOT NULL,
  `insuarance_date` date NOT NULL,
  `purchase_date` date NOT NULL,
  `mileage` double NOT NULL,
  `capacity` double NOT NULL,
  `brand` varchar(45) NOT NULL,
  `model` varchar(45) NOT NULL,
  `vehicle_status_id` int NOT NULL,
  `vehicle_type_id` int NOT NULL,
  `employee_NIC` varchar(30) NOT NULL,
  PRIMARY KEY (`licens_plate`),
  KEY `fk_vehicle_vehicle_status1_idx` (`vehicle_status_id`),
  KEY `fk_vehicle_vehicle_type1_idx` (`vehicle_type_id`),
  KEY `fk_vehicle_employee1_idx` (`employee_NIC`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle`
--

LOCK TABLES `vehicle` WRITE;
/*!40000 ALTER TABLE `vehicle` DISABLE KEYS */;
INSERT INTO `vehicle` VALUES ('1234567','2025-05-06','2025-05-20','2025-05-21',300,112,'Tata','weel 3',1,4,'200401910200'),('9287465438','2025-05-06','2025-05-07','2025-05-25',567,456,'TATA','TATA 3',2,4,'200401910200');
/*!40000 ALTER TABLE `vehicle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicle_status`
--

DROP TABLE IF EXISTS `vehicle_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicle_status` (
  `vehicle_status_id` int NOT NULL AUTO_INCREMENT,
  `vehicle_status` varchar(45) NOT NULL,
  PRIMARY KEY (`vehicle_status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle_status`
--

LOCK TABLES `vehicle_status` WRITE;
/*!40000 ALTER TABLE `vehicle_status` DISABLE KEYS */;
INSERT INTO `vehicle_status` VALUES (1,'Free'),(2,'Working');
/*!40000 ALTER TABLE `vehicle_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicle_type`
--

DROP TABLE IF EXISTS `vehicle_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicle_type` (
  `vehicle_type_id` int NOT NULL AUTO_INCREMENT,
  `vehicle_type` varchar(45) NOT NULL,
  PRIMARY KEY (`vehicle_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle_type`
--

LOCK TABLES `vehicle_type` WRITE;
/*!40000 ALTER TABLE `vehicle_type` DISABLE KEYS */;
INSERT INTO `vehicle_type` VALUES (1,'Lorry'),(2,'loree'),(3,'test3'),(4,'Weel');
/*!40000 ALTER TABLE `vehicle_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `yield_location`
--

DROP TABLE IF EXISTS `yield_location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `yield_location` (
  `yield_location_id` int NOT NULL AUTO_INCREMENT,
  `yield_location` varchar(45) NOT NULL,
  PRIMARY KEY (`yield_location_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `yield_location`
--

LOCK TABLES `yield_location` WRITE;
/*!40000 ALTER TABLE `yield_location` DISABLE KEYS */;
INSERT INTO `yield_location` VALUES (1,'kottawa'),(2,'ampara'),(3,'kurunegala'),(4,'anuradhapura'),(5,'gampaha'),(7,'Kochchikade');
/*!40000 ALTER TABLE `yield_location` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-11 20:26:45
