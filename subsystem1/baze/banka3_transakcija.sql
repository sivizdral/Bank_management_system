-- MySQL dump 10.13  Distrib 8.0.27, for Win64 (x86_64)
--
-- Host: localhost    Database: banka3
-- ------------------------------------------------------
-- Server version	8.0.27

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
-- Table structure for table `transakcija`
--

DROP TABLE IF EXISTS `transakcija`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transakcija` (
  `IdTra` int NOT NULL,
  `DatumVreme` datetime NOT NULL,
  `Vrsta` varchar(1) NOT NULL,
  `Iznos` int NOT NULL,
  `RedniBrSa` int DEFAULT NULL,
  `RedniBrKa` int DEFAULT NULL,
  `Svrha` varchar(45) DEFAULT NULL,
  `IdRacSa` int DEFAULT NULL,
  `IdRacKa` int DEFAULT NULL,
  `IdFil` int DEFAULT NULL,
  PRIMARY KEY (`IdTra`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transakcija`
--

LOCK TABLES `transakcija` WRITE;
/*!40000 ALTER TABLE `transakcija` DISABLE KEYS */;
INSERT INTO `transakcija` VALUES (1,'2022-02-03 02:55:01','u',1000000,NULL,1,'milioni',NULL,1,1),(2,'2022-02-03 02:55:39','u',2000000,NULL,1,'milioni x2',NULL,2,1),(3,'2022-02-03 02:56:25','u',11780,NULL,1,'Vucic 100 evra',NULL,3,3),(4,'2022-02-03 02:56:39','u',11780,NULL,1,'Vucic 100 evra',NULL,5,2),(5,'2022-02-03 02:56:49','u',11780,NULL,1,'Vucic 100 evra',NULL,8,2),(6,'2022-02-03 02:56:54','u',11780,NULL,1,'Vucic 100 evra',NULL,10,4),(7,'2022-02-03 02:59:05','p',5000,2,2,'Vucic 100 evra',1,10,NULL),(8,'2022-02-03 02:59:27','p',5000,3,1,'Vecera',1,9,NULL),(9,'2022-02-03 02:59:45','i',5000,4,NULL,'Kupovina',1,NULL,4),(10,'2022-02-03 03:00:09','i',100000,2,NULL,'Letovanje Maldivi',2,NULL,4);
/*!40000 ALTER TABLE `transakcija` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-02-03  3:31:34
