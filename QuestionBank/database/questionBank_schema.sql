CREATE DATABASE  IF NOT EXISTS `questionbank_dev` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `questionbank_dev`;
-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: localhost    Database: questionbank_dev
-- ------------------------------------------------------
-- Server version	5.5.8

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
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `department` (
  `DEPARTMENT_ID` int(11) NOT NULL AUTO_INCREMENT,
  `DEPARTMENT_NAME` varchar(45) DEFAULT NULL,
  `ASSOCIATED_COURSE` varchar(45) NOT NULL,
  `CREATED_BY` varchar(45) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`DEPARTMENT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `question` (
  `QUESTION_ID` int(11) NOT NULL AUTO_INCREMENT,
  `QUESTION_TYPE_ID` int(11) NOT NULL,
  `SUBJECT_ID` int(11) NOT NULL,
  `YEAR` varchar(10) NOT NULL,
  `SEMESTER` varchar(10) NOT NULL,
  `QUESTION` varchar(3000) NOT NULL,
  `UNIT` varchar(10) DEFAULT NULL,
  `CREATED_BY` varchar(10) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `OPTION_A` varchar(1000) DEFAULT NULL,
  `OPTION_B` varchar(1000) DEFAULT NULL,
  `OPTION_C` varchar(1000) DEFAULT NULL,
  `OPTION_D` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`QUESTION_ID`),
  KEY `SUBJECT_ID_FK_idx` (`SUBJECT_ID`),
  KEY `QUESTION_TYPE_ID_FK_idx` (`QUESTION_TYPE_ID`),
  CONSTRAINT `QUESTION_TYPE_ID_FK` FOREIGN KEY (`QUESTION_TYPE_ID`) REFERENCES `questiontype` (`QUESTION_TYPE_ID`),
  CONSTRAINT `SUBJECT_ID_FK` FOREIGN KEY (`SUBJECT_ID`) REFERENCES `subject` (`SUBJECT_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=89277 DEFAULT CHARSET=latin1 COMMENT='								';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `questiontype`
--

DROP TABLE IF EXISTS `questiontype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `questiontype` (
  `QUESTION_TYPE_ID` int(11) NOT NULL AUTO_INCREMENT,
  `MARK` decimal(10,4) NOT NULL,
  `NO_OF_QUESTIONS` int(11) NOT NULL,
  `CREATED_BY` varchar(45) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `SUBJECT_ID` int(11) DEFAULT NULL,
  `Q_TYPE` varchar(20) DEFAULT NULL,
  `questiontypecol` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`QUESTION_TYPE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=758 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `subject`
--

DROP TABLE IF EXISTS `subject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subject` (
  `SUBJECT_ID` int(11) NOT NULL AUTO_INCREMENT,
  `DEPARTMENT_ID` int(11) NOT NULL,
  `SUBJECT_TYPE` varchar(45) DEFAULT NULL,
  `SUBJECT_NAME` varchar(45) DEFAULT NULL,
  `CREATED_BY` varchar(45) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  PRIMARY KEY (`SUBJECT_ID`),
  KEY `department_id_idx` (`DEPARTMENT_ID`),
  CONSTRAINT `department_id` FOREIGN KEY (`DEPARTMENT_ID`) REFERENCES `department` (`DEPARTMENT_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=255 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `USER_ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_NAME` varchar(45) NOT NULL,
  `PASSWORD` varchar(45) NOT NULL,
  `USER_ROLE` varchar(10) NOT NULL,
  PRIMARY KEY (`USER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (7,'admin','admin@123','ADMIN'),(8,'temp','temp','STAFF');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

-- Dump completed on 2017-01-05  1:13:54
