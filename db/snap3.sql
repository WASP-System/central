-- MySQL dump 10.13  Distrib 5.5.12, for Win32 (x86)
--
-- Host: localhost    Database: wasp
-- ------------------------------------------------------
-- Server version	5.5.12

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
-- Table structure for table `acct_grant`
--

DROP TABLE IF EXISTS `acct_grant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acct_grant` (
  `grantid` int(10) NOT NULL AUTO_INCREMENT,
  `labid` int(10) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `code` varchar(250) DEFAULT NULL,
  `expirationdt` datetime DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`grantid`),
  KEY `fk_grant_lid` (`labid`),
  CONSTRAINT `acct_grant_ibfk_1` FOREIGN KEY (`labid`) REFERENCES `lab` (`labid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acct_grant`
--

LOCK TABLES `acct_grant` WRITE;
/*!40000 ALTER TABLE `acct_grant` DISABLE KEYS */;
/*!40000 ALTER TABLE `acct_grant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `acct_grantjob`
--

DROP TABLE IF EXISTS `acct_grantjob`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acct_grantjob` (
  `jobid` int(10) NOT NULL AUTO_INCREMENT,
  `grantid` int(10) DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`jobid`),
  KEY `fk_acct_ledgergrant_gid` (`grantid`),
  CONSTRAINT `acct_grantjob_ibfk_1` FOREIGN KEY (`jobid`) REFERENCES `acct_ledger` (`jobid`),
  CONSTRAINT `acct_grantjob_ibfk_2` FOREIGN KEY (`grantid`) REFERENCES `acct_grant` (`grantid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acct_grantjob`
--

LOCK TABLES `acct_grantjob` WRITE;
/*!40000 ALTER TABLE `acct_grantjob` DISABLE KEYS */;
/*!40000 ALTER TABLE `acct_grantjob` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `acct_invoice`
--

DROP TABLE IF EXISTS `acct_invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acct_invoice` (
  `invoiceid` int(10) NOT NULL AUTO_INCREMENT,
  `quoteid` int(10) DEFAULT NULL,
  `jobid` int(10) DEFAULT NULL,
  `amount` float(10,2) DEFAULT NULL,
  `comment` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`invoiceid`),
  KEY `fk_acct_invoice_qid` (`quoteid`),
  KEY `fk_acct_invoice_jid` (`jobid`),
  CONSTRAINT `acct_invoice_ibfk_1` FOREIGN KEY (`quoteid`) REFERENCES `acct_quote` (`quoteid`),
  CONSTRAINT `acct_invoice_ibfk_2` FOREIGN KEY (`jobid`) REFERENCES `job` (`jobid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acct_invoice`
--

LOCK TABLES `acct_invoice` WRITE;
/*!40000 ALTER TABLE `acct_invoice` DISABLE KEYS */;
/*!40000 ALTER TABLE `acct_invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `acct_jobquotecurrent`
--

DROP TABLE IF EXISTS `acct_jobquotecurrent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acct_jobquotecurrent` (
  `jobid` int(10) NOT NULL,
  `quoteid` int(10) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`jobid`),
  KEY `fk_acct_jobquotecurrent_qid` (`quoteid`),
  CONSTRAINT `acct_jobquotecurrent_ibfk_1` FOREIGN KEY (`jobid`) REFERENCES `job` (`jobid`),
  CONSTRAINT `acct_jobquotecurrent_ibfk_2` FOREIGN KEY (`quoteid`) REFERENCES `acct_quote` (`quoteid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acct_jobquotecurrent`
--

LOCK TABLES `acct_jobquotecurrent` WRITE;
/*!40000 ALTER TABLE `acct_jobquotecurrent` DISABLE KEYS */;
/*!40000 ALTER TABLE `acct_jobquotecurrent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `acct_ledger`
--

DROP TABLE IF EXISTS `acct_ledger`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acct_ledger` (
  `ledgerid` int(10) NOT NULL AUTO_INCREMENT,
  `invoiceid` int(10) DEFAULT NULL,
  `jobid` int(10) DEFAULT NULL,
  `amount` float(10,2) DEFAULT NULL,
  `comment` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`ledgerid`),
  KEY `fk_acct_ledger_iid` (`invoiceid`),
  KEY `fk_acct_ledger_jid` (`jobid`),
  CONSTRAINT `acct_ledger_ibfk_1` FOREIGN KEY (`invoiceid`) REFERENCES `acct_invoice` (`invoiceid`),
  CONSTRAINT `acct_ledger_ibfk_2` FOREIGN KEY (`jobid`) REFERENCES `job` (`jobid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acct_ledger`
--

LOCK TABLES `acct_ledger` WRITE;
/*!40000 ALTER TABLE `acct_ledger` DISABLE KEYS */;
/*!40000 ALTER TABLE `acct_ledger` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `acct_quote`
--

DROP TABLE IF EXISTS `acct_quote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acct_quote` (
  `quoteid` int(10) NOT NULL AUTO_INCREMENT,
  `jobid` int(10) DEFAULT NULL,
  `amount` float(10,2) DEFAULT NULL,
  `userid` int(10) DEFAULT NULL,
  `comment` varchar(250) DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`quoteid`),
  KEY `fk_acct_quote_jid` (`jobid`),
  KEY `fk_acct_quote_uid` (`userid`),
  CONSTRAINT `acct_quote_ibfk_1` FOREIGN KEY (`jobid`) REFERENCES `job` (`jobid`),
  CONSTRAINT `acct_quote_ibfk_2` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acct_quote`
--

LOCK TABLES `acct_quote` WRITE;
/*!40000 ALTER TABLE `acct_quote` DISABLE KEYS */;
/*!40000 ALTER TABLE `acct_quote` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `acct_quoteuser`
--

DROP TABLE IF EXISTS `acct_quoteuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acct_quoteuser` (
  `quoteuserid` int(10) NOT NULL AUTO_INCREMENT,
  `quoteid` int(10) DEFAULT NULL,
  `userid` int(10) DEFAULT NULL,
  `roleid` int(10) DEFAULT NULL,
  `isapproved` int(1) DEFAULT NULL,
  `comment` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`quoteuserid`),
  KEY `fk_acct_quote_jid` (`quoteid`),
  KEY `fk_acct_quote_uid` (`userid`),
  KEY `fk_acct_quote_rid` (`roleid`),
  CONSTRAINT `acct_quoteuser_ibfk_1` FOREIGN KEY (`quoteid`) REFERENCES `acct_quote` (`quoteid`),
  CONSTRAINT `acct_quoteuser_ibfk_2` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`),
  CONSTRAINT `acct_quoteuser_ibfk_3` FOREIGN KEY (`roleid`) REFERENCES `role` (`roleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acct_quoteuser`
--

LOCK TABLES `acct_quoteuser` WRITE;
/*!40000 ALTER TABLE `acct_quoteuser` DISABLE KEYS */;
/*!40000 ALTER TABLE `acct_quoteuser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `acct_workflowcost`
--

DROP TABLE IF EXISTS `acct_workflowcost`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acct_workflowcost` (
  `workflowid` int(10) NOT NULL,
  `basecost` float(10,2) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`workflowid`),
  CONSTRAINT `acct_workflowcost_ibfk_1` FOREIGN KEY (`workflowid`) REFERENCES `job` (`workflowid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acct_workflowcost`
--

LOCK TABLES `acct_workflowcost` WRITE;
/*!40000 ALTER TABLE `acct_workflowcost` DISABLE KEYS */;
/*!40000 ALTER TABLE `acct_workflowcost` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `adaptor`
--

DROP TABLE IF EXISTS `adaptor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `adaptor` (
  `adaptorid` int(10) NOT NULL AUTO_INCREMENT,
  `adaptorsetid` int(10) DEFAULT NULL,
  `iname` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `sequence` varchar(250) DEFAULT NULL,
  `barcodesequence` varchar(250) DEFAULT NULL,
  `barcodenumber` int(10) DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  PRIMARY KEY (`adaptorid`),
  UNIQUE KEY `u_adaptor_k_iid` (`iname`),
  UNIQUE KEY `u_adaptor_k_nid` (`name`),
  KEY `fk_adaptor_aid` (`adaptorsetid`),
  CONSTRAINT `adaptor_ibfk_1` FOREIGN KEY (`adaptorsetid`) REFERENCES `adaptorset` (`adaptorsetid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adaptor`
--

LOCK TABLES `adaptor` WRITE;
/*!40000 ALTER TABLE `adaptor` DISABLE KEYS */;
INSERT INTO `adaptor` VALUES (1,1,'illuminaTrueseqDnaIndexed1','Illumina DNA Indexed adaptor 1','AGATCGGAAGAGCGGTTCAGC','ATCACG',1,1),(2,1,'illuminaTrueseqDnaIndexed2','Illumina DNA Indexed adaptor 2','AGATCGGAAGAGCGGTTCAGC','CGATGT',2,0),(3,1,'illuminaTrueseqDnaIndexed3','Illumina DNA Indexed adaptor 3','AGATCGGAAGAGCGGTTCAGC','TTAGGC',3,0),(4,1,'illuminaTrueseqDnaIndexed4','Illumina DNA Indexed adaptor 4','AGATCGGAAGAGCGGTTCAGC','TGACCA',4,0);
/*!40000 ALTER TABLE `adaptor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `adaptormeta`
--

DROP TABLE IF EXISTS `adaptormeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `adaptormeta` (
  `adaptormetaid` int(10) NOT NULL AUTO_INCREMENT,
  `adaptorid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` varchar(250) DEFAULT NULL,
  `position` int(10) DEFAULT '0',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`adaptormetaid`),
  UNIQUE KEY `u_adaptormeta_k_aid` (`k`,`adaptorid`),
  KEY `fk_adaptormeta_runid` (`adaptorid`),
  CONSTRAINT `adaptormeta_ibfk_1` FOREIGN KEY (`adaptorid`) REFERENCES `adaptor` (`adaptorid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adaptormeta`
--

LOCK TABLES `adaptormeta` WRITE;
/*!40000 ALTER TABLE `adaptormeta` DISABLE KEYS */;
/*!40000 ALTER TABLE `adaptormeta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `adaptorset`
--

DROP TABLE IF EXISTS `adaptorset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `adaptorset` (
  `adaptorsetid` int(10) NOT NULL AUTO_INCREMENT,
  `iname` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `typesampleid` int(10) DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  PRIMARY KEY (`adaptorsetid`),
  UNIQUE KEY `u_adaptorset_k_iid` (`iname`),
  UNIQUE KEY `u_adaptorset_k_nid` (`name`),
  KEY `fk_adaptorset_tid` (`typesampleid`),
  CONSTRAINT `adaptorset_ibfk_1` FOREIGN KEY (`typesampleid`) REFERENCES `typesample` (`typesampleid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adaptorset`
--

LOCK TABLES `adaptorset` WRITE;
/*!40000 ALTER TABLE `adaptorset` DISABLE KEYS */;
INSERT INTO `adaptorset` VALUES (1,'truseqIndexedDna','TruSEQ INDEXED DNA',1,1);
/*!40000 ALTER TABLE `adaptorset` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `adaptorsetmeta`
--

DROP TABLE IF EXISTS `adaptorsetmeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `adaptorsetmeta` (
  `adaptorsetmetaid` int(10) NOT NULL AUTO_INCREMENT,
  `adaptorsetid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` varchar(250) DEFAULT NULL,
  `position` int(10) DEFAULT '0',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`adaptorsetmetaid`),
  UNIQUE KEY `u_adaptorsetmeta_k_aid` (`k`,`adaptorsetid`),
  KEY `fk_adaptorsetmeta_runid` (`adaptorsetid`),
  CONSTRAINT `adaptorsetmeta_ibfk_1` FOREIGN KEY (`adaptorsetid`) REFERENCES `adaptorset` (`adaptorsetid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adaptorsetmeta`
--

LOCK TABLES `adaptorsetmeta` WRITE;
/*!40000 ALTER TABLE `adaptorsetmeta` DISABLE KEYS */;
/*!40000 ALTER TABLE `adaptorsetmeta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `adaptorsetresourcecategory`
--

DROP TABLE IF EXISTS `adaptorsetresourcecategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `adaptorsetresourcecategory` (
  `adaptorsetresourcecategoryid` int(10) NOT NULL AUTO_INCREMENT,
  `adaptorsetid` int(10) DEFAULT NULL,
  `resourcecategoryid` int(10) DEFAULT NULL,
  PRIMARY KEY (`adaptorsetresourcecategoryid`),
  UNIQUE KEY `u_adaptorsetresourcecategory_aid_rid` (`adaptorsetid`,`resourcecategoryid`),
  KEY `fk_adaptorsetresourcecategory_rid` (`resourcecategoryid`),
  CONSTRAINT `adaptorsetresourcecategory_ibfk_1` FOREIGN KEY (`resourcecategoryid`) REFERENCES `resourcecategory` (`resourcecategoryid`),
  CONSTRAINT `adaptorsetresourcecategory_ibfk_2` FOREIGN KEY (`adaptorsetid`) REFERENCES `adaptorset` (`adaptorsetid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adaptorsetresourcecategory`
--

LOCK TABLES `adaptorsetresourcecategory` WRITE;
/*!40000 ALTER TABLE `adaptorsetresourcecategory` DISABLE KEYS */;
INSERT INTO `adaptorsetresourcecategory` VALUES (1,1,1);
/*!40000 ALTER TABLE `adaptorsetresourcecategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `barcode`
--

DROP TABLE IF EXISTS `barcode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `barcode` (
  `barcodeid` int(10) NOT NULL AUTO_INCREMENT,
  `barcode` varchar(250) DEFAULT NULL,
  `barcodefor` varchar(250) DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`barcodeid`),
  UNIQUE KEY `u_barcode_bc` (`barcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `barcode`
--

LOCK TABLES `barcode` WRITE;
/*!40000 ALTER TABLE `barcode` DISABLE KEYS */;
/*!40000 ALTER TABLE `barcode` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `batch_job_execution`
--

DROP TABLE IF EXISTS `batch_job_execution`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `batch_job_execution` (
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `VERSION` bigint(20) DEFAULT NULL,
  `JOB_INSTANCE_ID` bigint(20) NOT NULL,
  `CREATE_TIME` datetime NOT NULL,
  `START_TIME` datetime DEFAULT NULL,
  `END_TIME` datetime DEFAULT NULL,
  `STATUS` varchar(10) DEFAULT NULL,
  `EXIT_CODE` varchar(100) DEFAULT NULL,
  `EXIT_MESSAGE` varchar(2500) DEFAULT NULL,
  `LAST_UPDATED` datetime DEFAULT NULL,
  PRIMARY KEY (`JOB_EXECUTION_ID`),
  KEY `JOB_INST_EXEC_FK` (`JOB_INSTANCE_ID`),
  CONSTRAINT `JOB_INST_EXEC_FK` FOREIGN KEY (`JOB_INSTANCE_ID`) REFERENCES `batch_job_instance` (`JOB_INSTANCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `batch_job_execution`
--

LOCK TABLES `batch_job_execution` WRITE;
/*!40000 ALTER TABLE `batch_job_execution` DISABLE KEYS */;
INSERT INTO `batch_job_execution` VALUES (1,1,1,'2012-01-29 18:03:00','2012-01-29 18:03:00',NULL,'STARTED','UNKNOWN','','2012-01-29 18:03:00'),(2,2,2,'2012-01-29 18:03:02','2012-01-29 18:03:02','2012-01-29 18:05:45','COMPLETED','COMPLETED','','2012-01-29 18:05:45'),(3,1,3,'2012-01-29 18:05:45','2012-01-29 18:05:45',NULL,'STARTED','UNKNOWN','','2012-01-29 18:05:45'),(4,1,4,'2012-01-29 18:09:23','2012-01-29 18:09:23',NULL,'STARTED','UNKNOWN','','2012-01-29 18:09:23');
/*!40000 ALTER TABLE `batch_job_execution` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `batch_job_execution_context`
--

DROP TABLE IF EXISTS `batch_job_execution_context`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `batch_job_execution_context` (
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `SHORT_CONTEXT` varchar(2500) NOT NULL,
  `SERIALIZED_CONTEXT` text,
  PRIMARY KEY (`JOB_EXECUTION_ID`),
  CONSTRAINT `JOB_EXEC_CTX_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `batch_job_execution` (`JOB_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `batch_job_execution_context`
--

LOCK TABLES `batch_job_execution_context` WRITE;
/*!40000 ALTER TABLE `batch_job_execution_context` DISABLE KEYS */;
INSERT INTO `batch_job_execution_context` VALUES (1,'{\"map\":\"\"}',NULL),(2,'{\"map\":\"\"}',NULL),(3,'{\"map\":\"\"}',NULL),(4,'{\"map\":\"\"}',NULL);
/*!40000 ALTER TABLE `batch_job_execution_context` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `batch_job_execution_seq`
--

DROP TABLE IF EXISTS `batch_job_execution_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `batch_job_execution_seq` (
  `ID` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `batch_job_execution_seq`
--

LOCK TABLES `batch_job_execution_seq` WRITE;
/*!40000 ALTER TABLE `batch_job_execution_seq` DISABLE KEYS */;
INSERT INTO `batch_job_execution_seq` VALUES (4);
/*!40000 ALTER TABLE `batch_job_execution_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `batch_job_instance`
--

DROP TABLE IF EXISTS `batch_job_instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `batch_job_instance` (
  `JOB_INSTANCE_ID` bigint(20) NOT NULL,
  `VERSION` bigint(20) DEFAULT NULL,
  `JOB_NAME` varchar(100) NOT NULL,
  `JOB_KEY` varchar(32) NOT NULL,
  PRIMARY KEY (`JOB_INSTANCE_ID`),
  UNIQUE KEY `JOB_INST_UN` (`JOB_NAME`,`JOB_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `batch_job_instance`
--

LOCK TABLES `batch_job_instance` WRITE;
/*!40000 ALTER TABLE `batch_job_instance` DISABLE KEYS */;
INSERT INTO `batch_job_instance` VALUES (1,0,'chipSeqStartJob','d3736548b57443f165dbf05f887cc42e'),(2,0,'chipSeqDna.workflow','e3b369a7e2cbc3678eceafce8ce5a6a1'),(3,0,'chipSeqLibrary.workflow','58842e2b4b651815314814569b991fd8'),(4,0,'illuminaFlowcellV3Sample.workflow','9e42aa6771256a7dba6baa869acf97f1');
/*!40000 ALTER TABLE `batch_job_instance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `batch_job_params`
--

DROP TABLE IF EXISTS `batch_job_params`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `batch_job_params` (
  `JOB_INSTANCE_ID` bigint(20) NOT NULL,
  `TYPE_CD` varchar(6) NOT NULL,
  `KEY_NAME` varchar(100) NOT NULL,
  `STRING_VAL` varchar(250) DEFAULT NULL,
  `DATE_VAL` datetime DEFAULT NULL,
  `LONG_VAL` bigint(20) DEFAULT NULL,
  `DOUBLE_VAL` double DEFAULT NULL,
  KEY `JOB_INST_PARAMS_FK` (`JOB_INSTANCE_ID`),
  CONSTRAINT `JOB_INST_PARAMS_FK` FOREIGN KEY (`JOB_INSTANCE_ID`) REFERENCES `batch_job_instance` (`JOB_INSTANCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `batch_job_params`
--

LOCK TABLES `batch_job_params` WRITE;
/*!40000 ALTER TABLE `batch_job_params` DISABLE KEYS */;
INSERT INTO `batch_job_params` VALUES (1,'STRING','state','1','1969-12-31 19:00:00',0,0),(2,'STRING','state','2','1969-12-31 19:00:00',0,0),(3,'STRING','state','11','1969-12-31 19:00:00',0,0),(4,'STRING','state','1010','1969-12-31 19:00:00',0,0);
/*!40000 ALTER TABLE `batch_job_params` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `batch_job_seq`
--

DROP TABLE IF EXISTS `batch_job_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `batch_job_seq` (
  `ID` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `batch_job_seq`
--

LOCK TABLES `batch_job_seq` WRITE;
/*!40000 ALTER TABLE `batch_job_seq` DISABLE KEYS */;
INSERT INTO `batch_job_seq` VALUES (4);
/*!40000 ALTER TABLE `batch_job_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `batch_step_execution`
--

DROP TABLE IF EXISTS `batch_step_execution`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `batch_step_execution` (
  `STEP_EXECUTION_ID` bigint(20) NOT NULL,
  `VERSION` bigint(20) NOT NULL,
  `STEP_NAME` varchar(100) NOT NULL,
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `START_TIME` datetime NOT NULL,
  `END_TIME` datetime DEFAULT NULL,
  `STATUS` varchar(10) DEFAULT NULL,
  `COMMIT_COUNT` bigint(20) DEFAULT NULL,
  `READ_COUNT` bigint(20) DEFAULT NULL,
  `FILTER_COUNT` bigint(20) DEFAULT NULL,
  `WRITE_COUNT` bigint(20) DEFAULT NULL,
  `READ_SKIP_COUNT` bigint(20) DEFAULT NULL,
  `WRITE_SKIP_COUNT` bigint(20) DEFAULT NULL,
  `PROCESS_SKIP_COUNT` bigint(20) DEFAULT NULL,
  `ROLLBACK_COUNT` bigint(20) DEFAULT NULL,
  `EXIT_CODE` varchar(100) DEFAULT NULL,
  `EXIT_MESSAGE` varchar(2500) DEFAULT NULL,
  `LAST_UPDATED` datetime DEFAULT NULL,
  PRIMARY KEY (`STEP_EXECUTION_ID`),
  KEY `JOB_EXEC_STEP_FK` (`JOB_EXECUTION_ID`),
  CONSTRAINT `JOB_EXEC_STEP_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `batch_job_execution` (`JOB_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `batch_step_execution`
--

LOCK TABLES `batch_step_execution` WRITE;
/*!40000 ALTER TABLE `batch_step_execution` DISABLE KEYS */;
INSERT INTO `batch_step_execution` VALUES (1,4,'chipSeq.startJob0',1,'2012-01-29 18:03:00','2012-01-29 18:03:00','COMPLETED',2,1,0,1,0,0,0,0,'COMPLETED','','2012-01-29 18:03:00'),(2,4,'piApprovalStep0',1,'2012-01-29 18:03:01','2012-01-29 18:03:01','COMPLETED',2,1,0,1,0,0,0,0,'COMPLETED','','2012-01-29 18:03:01'),(3,4,'quoteStep0',1,'2012-01-29 18:03:01','2012-01-29 18:03:01','COMPLETED',2,1,0,1,0,0,0,0,'COMPLETED','','2012-01-29 18:03:01'),(4,4,'daApprovalStep0',1,'2012-01-29 18:03:01','2012-01-29 18:03:01','COMPLETED',2,1,0,1,0,0,0,0,'COMPLETED','','2012-01-29 18:03:01'),(5,4,'boneShakerApprovalStep0',1,'2012-01-29 18:03:01','2012-01-29 18:03:01','COMPLETED',2,1,0,1,0,0,0,0,'COMPLETED','','2012-01-29 18:03:01'),(6,4,'piApprovalStep1',1,'2012-01-29 18:03:01','2012-01-29 18:03:42','COMPLETED',2,1,0,1,0,0,0,2,'COMPLETED','','2012-01-29 18:03:42'),(7,4,'daApprovalStep1',1,'2012-01-29 18:03:01','2012-01-29 18:03:42','COMPLETED',2,1,0,1,0,0,0,2,'COMPLETED','','2012-01-29 18:03:42'),(8,4,'quoteStep1',1,'2012-01-29 18:03:01','2012-01-29 18:03:42','COMPLETED',2,1,0,1,0,0,0,2,'COMPLETED','','2012-01-29 18:03:42'),(9,4,'boneShakerApprovalStep1',1,'2012-01-29 18:03:01','2012-01-29 18:03:42','COMPLETED',2,1,0,1,0,0,0,2,'COMPLETED','','2012-01-29 18:03:42'),(10,4,'wasp.sample.sampleReceived0',2,'2012-01-29 18:03:02','2012-01-29 18:03:02','COMPLETED',2,1,0,1,0,0,0,0,'COMPLETED','','2012-01-29 18:03:02'),(11,4,'wasp.sample.sampleReceived1',2,'2012-01-29 18:03:02','2012-01-29 18:03:43','COMPLETED',2,1,0,1,0,0,0,2,'COMPLETED','','2012-01-29 18:03:43'),(12,4,'invoiceStep0',1,'2012-01-29 18:03:42','2012-01-29 18:03:43','COMPLETED',2,1,0,1,0,0,0,0,'COMPLETED','','2012-01-29 18:03:43'),(13,4,'wasp.sample.waitForJobInvoiceSent0',2,'2012-01-29 18:03:43','2012-01-29 18:04:03','COMPLETED',2,1,0,1,0,0,0,1,'COMPLETED','','2012-01-29 18:04:03'),(14,4,'invoiceStep1',1,'2012-01-29 18:03:43','2012-01-29 18:03:43','COMPLETED',2,1,0,1,0,0,0,0,'COMPLETED','','2012-01-29 18:03:43'),(15,4,'invoiceStep2',1,'2012-01-29 18:03:43','2012-01-29 18:03:43','COMPLETED',2,1,0,1,0,0,0,0,'COMPLETED','','2012-01-29 18:03:43'),(16,4,'allSamplesReceivedStep0',1,'2012-01-29 18:03:44','2012-01-29 18:03:44','COMPLETED',2,1,0,1,0,0,0,0,'COMPLETED','','2012-01-29 18:03:44'),(17,1,'allSamplesReceivedStep1',1,'2012-01-29 18:03:44',NULL,'STARTED',0,0,0,0,0,0,0,0,'EXECUTING','','2012-01-29 18:03:44'),(18,4,'wasp.sample.createLibrary0',2,'2012-01-29 18:04:03','2012-01-29 18:04:03','COMPLETED',2,1,0,1,0,0,0,0,'COMPLETED','','2012-01-29 18:04:03'),(19,4,'wasp.sample.createLibrary1',2,'2012-01-29 18:04:04','2012-01-29 18:05:44','COMPLETED',2,1,0,1,0,0,0,5,'COMPLETED','','2012-01-29 18:05:44'),(20,4,'wasp.sample.linkLibrary0',2,'2012-01-29 18:05:44','2012-01-29 18:05:45','COMPLETED',2,1,0,1,0,0,0,0,'COMPLETED','','2012-01-29 18:05:45'),(21,4,'finishJobStep',2,'2012-01-29 18:05:45','2012-01-29 18:05:45','COMPLETED',2,1,0,1,0,0,0,0,'COMPLETED','','2012-01-29 18:05:45'),(22,4,'wasp.sample.sampleReceived0',3,'2012-01-29 18:05:46','2012-01-29 18:05:46','COMPLETED',2,1,0,1,0,0,0,0,'COMPLETED','','2012-01-29 18:05:46'),(23,4,'wasp.sample.sampleReceived1',3,'2012-01-29 18:05:46','2012-01-29 18:07:26','COMPLETED',2,1,0,1,0,0,0,5,'COMPLETED','','2012-01-29 18:07:26'),(24,4,'wasp.sample.waitForJobInvoiceSent0',3,'2012-01-29 18:07:27','2012-01-29 18:07:27','COMPLETED',2,1,0,1,0,0,0,0,'COMPLETED','','2012-01-29 18:07:27'),(25,4,'wasp.sample.libraryPlatformUnitAssign0',3,'2012-01-29 18:07:27','2012-01-29 18:07:27','COMPLETED',2,1,0,1,0,0,0,0,'COMPLETED','','2012-01-29 18:07:27'),(26,1,'wasp.sample.libraryPlatformUnitAssign1',3,'2012-01-29 18:07:27',NULL,'STARTED',0,0,0,0,0,0,0,0,'EXECUTING','','2012-01-29 18:07:27'),(27,4,'wasp.sample.sampleReceived0',4,'2012-01-29 18:09:24','2012-01-29 18:09:24','COMPLETED',2,1,0,1,0,0,0,0,'COMPLETED','','2012-01-29 18:09:24'),(28,4,'wasp.sample.sampleReceived1',4,'2012-01-29 18:09:24','2012-01-29 18:10:04','COMPLETED',2,1,0,1,0,0,0,2,'COMPLETED','','2012-01-29 18:10:04'),(29,4,'wasp.sample.libraryPlatformUnitAssign0',4,'2012-01-29 18:10:04','2012-01-29 18:10:05','COMPLETED',2,1,0,1,0,0,0,0,'COMPLETED','','2012-01-29 18:10:05'),(30,1,'wasp.sample.libraryPlatformUnitAssign1',4,'2012-01-29 18:10:05',NULL,'STARTED',0,0,0,0,0,0,0,0,'EXECUTING','','2012-01-29 18:10:05');
/*!40000 ALTER TABLE `batch_step_execution` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `batch_step_execution_context`
--

DROP TABLE IF EXISTS `batch_step_execution_context`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `batch_step_execution_context` (
  `STEP_EXECUTION_ID` bigint(20) NOT NULL,
  `SHORT_CONTEXT` varchar(2500) NOT NULL,
  `SERIALIZED_CONTEXT` text,
  PRIMARY KEY (`STEP_EXECUTION_ID`),
  CONSTRAINT `STEP_EXEC_CTX_FK` FOREIGN KEY (`STEP_EXECUTION_ID`) REFERENCES `batch_step_execution` (`STEP_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `batch_step_execution_context`
--

LOCK TABLES `batch_step_execution_context` WRITE;
/*!40000 ALTER TABLE `batch_step_execution_context` DISABLE KEYS */;
INSERT INTO `batch_step_execution_context` VALUES (1,'{\"map\":\"\"}',NULL),(2,'{\"map\":\"\"}',NULL),(3,'{\"map\":\"\"}',NULL),(4,'{\"map\":\"\"}',NULL),(5,'{\"map\":\"\"}',NULL),(6,'{\"map\":\"\"}',NULL),(7,'{\"map\":\"\"}',NULL),(8,'{\"map\":\"\"}',NULL),(9,'{\"map\":\"\"}',NULL),(10,'{\"map\":\"\"}',NULL),(11,'{\"map\":\"\"}',NULL),(12,'{\"map\":\"\"}',NULL),(13,'{\"map\":\"\"}',NULL),(14,'{\"map\":\"\"}',NULL),(15,'{\"map\":\"\"}',NULL),(16,'{\"map\":\"\"}',NULL),(17,'{\"map\":\"\"}',NULL),(18,'{\"map\":\"\"}',NULL),(19,'{\"map\":\"\"}',NULL),(20,'{\"map\":\"\"}',NULL),(21,'{\"map\":\"\"}',NULL),(22,'{\"map\":\"\"}',NULL),(23,'{\"map\":\"\"}',NULL),(24,'{\"map\":\"\"}',NULL),(25,'{\"map\":\"\"}',NULL),(26,'{\"map\":\"\"}',NULL),(27,'{\"map\":\"\"}',NULL),(28,'{\"map\":\"\"}',NULL),(29,'{\"map\":\"\"}',NULL),(30,'{\"map\":\"\"}',NULL);
/*!40000 ALTER TABLE `batch_step_execution_context` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `batch_step_execution_seq`
--

DROP TABLE IF EXISTS `batch_step_execution_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `batch_step_execution_seq` (
  `ID` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `batch_step_execution_seq`
--

LOCK TABLES `batch_step_execution_seq` WRITE;
/*!40000 ALTER TABLE `batch_step_execution_seq` DISABLE KEYS */;
INSERT INTO `batch_step_execution_seq` VALUES (30);
/*!40000 ALTER TABLE `batch_step_execution_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `confirmemailauth`
--

DROP TABLE IF EXISTS `confirmemailauth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `confirmemailauth` (
  `confirmemailauthid` int(10) NOT NULL AUTO_INCREMENT,
  `userpendingid` int(10) DEFAULT NULL,
  `userid` int(10) DEFAULT NULL,
  `authcode` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`confirmemailauthid`),
  UNIQUE KEY `u_confirmemailauth` (`authcode`),
  KEY `fk_labpending_uid` (`userid`),
  KEY `fk_labpending_peuid` (`userpendingid`),
  CONSTRAINT `confirmemailauth_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`),
  CONSTRAINT `confirmemailauth_ibfk_2` FOREIGN KEY (`userpendingid`) REFERENCES `userpending` (`userpendingid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `confirmemailauth`
--

LOCK TABLES `confirmemailauth` WRITE;
/*!40000 ALTER TABLE `confirmemailauth` DISABLE KEYS */;
/*!40000 ALTER TABLE `confirmemailauth` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `department` (
  `departmentid` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(250) DEFAULT NULL,
  `isinternal` int(1) DEFAULT '1',
  `isactive` int(1) DEFAULT '1',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`departmentid`),
  UNIQUE KEY `u_department_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES (1,'Internal - Default Department',1,1,'2012-01-29 22:48:42',1),(2,'External - Default Department',0,1,'2012-01-29 22:48:42',1);
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `departmentuser`
--

DROP TABLE IF EXISTS `departmentuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `departmentuser` (
  `departmentuserid` int(10) NOT NULL AUTO_INCREMENT,
  `departmentid` int(10) DEFAULT NULL,
  `userid` int(10) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`departmentuserid`),
  UNIQUE KEY `u_departmentuser_did_uid` (`departmentid`,`userid`),
  KEY `fk_departmentuser_uid` (`userid`),
  CONSTRAINT `departmentuser_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`),
  CONSTRAINT `departmentuser_ibfk_2` FOREIGN KEY (`departmentid`) REFERENCES `department` (`departmentid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `departmentuser`
--

LOCK TABLES `departmentuser` WRITE;
/*!40000 ALTER TABLE `departmentuser` DISABLE KEYS */;
/*!40000 ALTER TABLE `departmentuser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file`
--

DROP TABLE IF EXISTS `file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file` (
  `fileid` int(10) NOT NULL AUTO_INCREMENT,
  `filelocation` varchar(2048) DEFAULT NULL,
  `contenttype` varchar(250) DEFAULT NULL,
  `sizek` int(10) DEFAULT NULL,
  `md5hash` varchar(250) DEFAULT NULL,
  `description` varchar(250) DEFAULT NULL,
  `isarchived` int(1) DEFAULT '0',
  `isactive` int(1) DEFAULT '1',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`fileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file`
--

LOCK TABLES `file` WRITE;
/*!40000 ALTER TABLE `file` DISABLE KEYS */;
/*!40000 ALTER TABLE `file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job`
--

DROP TABLE IF EXISTS `job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `job` (
  `jobid` int(10) NOT NULL AUTO_INCREMENT,
  `labid` int(10) DEFAULT NULL,
  `userid` int(10) DEFAULT NULL,
  `workflowid` int(10) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `createts` datetime DEFAULT NULL,
  `viewablebylab` int(1) DEFAULT '0',
  `isactive` int(1) DEFAULT '1',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`jobid`),
  UNIQUE KEY `u_job_name_lid` (`name`,`labid`),
  KEY `fk_job_lid` (`labid`),
  KEY `fk_job_uid` (`userid`),
  KEY `fk_job_wid` (`workflowid`),
  CONSTRAINT `job_ibfk_1` FOREIGN KEY (`labid`) REFERENCES `lab` (`labid`),
  CONSTRAINT `job_ibfk_2` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`),
  CONSTRAINT `job_ibfk_3` FOREIGN KEY (`workflowid`) REFERENCES `workflow` (`workflowid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job`
--

LOCK TABLES `job` WRITE;
/*!40000 ALTER TABLE `job` DISABLE KEYS */;
INSERT INTO `job` VALUES (1,1,1,1,'first job','2012-01-29 18:02:58',NULL,1,'2012-01-29 23:02:58',NULL);
/*!40000 ALTER TABLE `job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jobcell`
--

DROP TABLE IF EXISTS `jobcell`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jobcell` (
  `jobcellid` int(10) NOT NULL AUTO_INCREMENT,
  `jobid` int(10) DEFAULT NULL,
  `cellindex` int(10) DEFAULT NULL,
  PRIMARY KEY (`jobcellid`),
  UNIQUE KEY `u_jobcell_jdid_ci` (`jobid`,`cellindex`),
  CONSTRAINT `jobcell_ibfk_1` FOREIGN KEY (`jobid`) REFERENCES `job` (`jobid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jobcell`
--

LOCK TABLES `jobcell` WRITE;
/*!40000 ALTER TABLE `jobcell` DISABLE KEYS */;
/*!40000 ALTER TABLE `jobcell` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jobdraft`
--

DROP TABLE IF EXISTS `jobdraft`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jobdraft` (
  `jobdraftid` int(10) NOT NULL AUTO_INCREMENT,
  `labid` int(10) DEFAULT NULL,
  `userid` int(10) DEFAULT NULL,
  `workflowid` int(10) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `createts` datetime DEFAULT NULL,
  `submittedjobid` int(10) DEFAULT NULL,
  `status` varchar(50) DEFAULT '1',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`jobdraftid`),
  KEY `fk_jobdraft_lid` (`labid`),
  KEY `fk_jobdraft_uid` (`userid`),
  KEY `fk_jobdraft_wid` (`workflowid`),
  KEY `fk_jobdraft_sjid` (`submittedjobid`),
  CONSTRAINT `jobdraft_ibfk_1` FOREIGN KEY (`labid`) REFERENCES `lab` (`labid`),
  CONSTRAINT `jobdraft_ibfk_2` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`),
  CONSTRAINT `jobdraft_ibfk_3` FOREIGN KEY (`workflowid`) REFERENCES `workflow` (`workflowid`),
  CONSTRAINT `jobdraft_ibfk_4` FOREIGN KEY (`submittedjobid`) REFERENCES `job` (`jobid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jobdraft`
--

LOCK TABLES `jobdraft` WRITE;
/*!40000 ALTER TABLE `jobdraft` DISABLE KEYS */;
/*!40000 ALTER TABLE `jobdraft` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jobdraftcell`
--

DROP TABLE IF EXISTS `jobdraftcell`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jobdraftcell` (
  `jobdraftcellid` int(10) NOT NULL AUTO_INCREMENT,
  `jobdraftid` int(10) DEFAULT NULL,
  `cellindex` int(10) DEFAULT NULL,
  PRIMARY KEY (`jobdraftcellid`),
  UNIQUE KEY `u_jobdraftcell_jdid_ci` (`jobdraftid`,`cellindex`),
  CONSTRAINT `jobdraftcell_ibfk_1` FOREIGN KEY (`jobdraftid`) REFERENCES `jobdraft` (`jobdraftid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jobdraftcell`
--

LOCK TABLES `jobdraftcell` WRITE;
/*!40000 ALTER TABLE `jobdraftcell` DISABLE KEYS */;
/*!40000 ALTER TABLE `jobdraftcell` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jobdraftmeta`
--

DROP TABLE IF EXISTS `jobdraftmeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jobdraftmeta` (
  `jobdraftmetaid` int(10) NOT NULL AUTO_INCREMENT,
  `jobdraftid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` varchar(250) DEFAULT NULL,
  `position` int(10) DEFAULT '0',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`jobdraftmetaid`),
  UNIQUE KEY `u_jobdraftmeta_k_jdid` (`k`,`jobdraftid`),
  KEY `fk_jobdraftmeta_jdid` (`jobdraftid`),
  CONSTRAINT `jobdraftmeta_ibfk_1` FOREIGN KEY (`jobdraftid`) REFERENCES `jobdraft` (`jobdraftid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jobdraftmeta`
--

LOCK TABLES `jobdraftmeta` WRITE;
/*!40000 ALTER TABLE `jobdraftmeta` DISABLE KEYS */;
/*!40000 ALTER TABLE `jobdraftmeta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jobdraftresourcecategory`
--

DROP TABLE IF EXISTS `jobdraftresourcecategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jobdraftresourcecategory` (
  `jobdraftresourcecategoryid` int(10) NOT NULL AUTO_INCREMENT,
  `jobdraftid` int(10) DEFAULT NULL,
  `resourcecategoryid` int(10) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`jobdraftresourcecategoryid`),
  UNIQUE KEY `u_jobdraftresourcecategory_rcid_jdid` (`resourcecategoryid`,`jobdraftid`),
  KEY `fk_jobdraftresourcecategory_jdid` (`jobdraftid`),
  CONSTRAINT `jobdraftresourcecategory_ibfk_1` FOREIGN KEY (`jobdraftid`) REFERENCES `jobdraft` (`jobdraftid`),
  CONSTRAINT `jobdraftresourcecategory_ibfk_2` FOREIGN KEY (`resourcecategoryid`) REFERENCES `resourcecategory` (`resourcecategoryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jobdraftresourcecategory`
--

LOCK TABLES `jobdraftresourcecategory` WRITE;
/*!40000 ALTER TABLE `jobdraftresourcecategory` DISABLE KEYS */;
/*!40000 ALTER TABLE `jobdraftresourcecategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jobdraftsoftware`
--

DROP TABLE IF EXISTS `jobdraftsoftware`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jobdraftsoftware` (
  `jobdraftsoftwareid` int(10) NOT NULL AUTO_INCREMENT,
  `jobdraftid` int(10) DEFAULT NULL,
  `softwareid` int(10) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`jobdraftsoftwareid`),
  UNIQUE KEY `u_jobdraftsoftware_sid_jdid` (`softwareid`,`jobdraftid`),
  KEY `fk_jobdraftsoftware_jdid` (`jobdraftid`),
  CONSTRAINT `jobdraftsoftware_ibfk_1` FOREIGN KEY (`jobdraftid`) REFERENCES `jobdraft` (`jobdraftid`),
  CONSTRAINT `jobdraftsoftware_ibfk_2` FOREIGN KEY (`softwareid`) REFERENCES `software` (`softwareid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jobdraftsoftware`
--

LOCK TABLES `jobdraftsoftware` WRITE;
/*!40000 ALTER TABLE `jobdraftsoftware` DISABLE KEYS */;
/*!40000 ALTER TABLE `jobdraftsoftware` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jobfile`
--

DROP TABLE IF EXISTS `jobfile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jobfile` (
  `jobfileid` int(10) NOT NULL AUTO_INCREMENT,
  `jobid` int(10) DEFAULT NULL,
  `fileid` int(10) DEFAULT NULL,
  `iname` varchar(2048) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `description` varchar(2048) DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`jobfileid`),
  KEY `fk_jobfile_jid` (`jobid`),
  KEY `fk_jobfile_fid` (`fileid`),
  CONSTRAINT `jobfile_ibfk_1` FOREIGN KEY (`jobid`) REFERENCES `job` (`jobid`),
  CONSTRAINT `jobfile_ibfk_2` FOREIGN KEY (`fileid`) REFERENCES `file` (`fileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jobfile`
--

LOCK TABLES `jobfile` WRITE;
/*!40000 ALTER TABLE `jobfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `jobfile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jobmeta`
--

DROP TABLE IF EXISTS `jobmeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jobmeta` (
  `jobmetaid` int(10) NOT NULL AUTO_INCREMENT,
  `jobid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` text,
  `position` int(10) DEFAULT '0',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`jobmetaid`),
  UNIQUE KEY `u_jobmeta_k_jid` (`k`,`jobid`),
  KEY `fk_jobmeta_jobid` (`jobid`),
  CONSTRAINT `jobmeta_ibfk_1` FOREIGN KEY (`jobid`) REFERENCES `job` (`jobid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jobmeta`
--

LOCK TABLES `jobmeta` WRITE;
/*!40000 ALTER TABLE `jobmeta` DISABLE KEYS */;
/*!40000 ALTER TABLE `jobmeta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jobresource`
--

DROP TABLE IF EXISTS `jobresource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jobresource` (
  `jobresourceid` int(10) NOT NULL AUTO_INCREMENT,
  `jobid` int(10) DEFAULT NULL,
  `resourceid` int(10) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`jobresourceid`),
  UNIQUE KEY `u_jobresource_rid_jdid` (`resourceid`,`jobid`),
  KEY `fk_jobresource_jdid` (`jobid`),
  CONSTRAINT `jobresource_ibfk_1` FOREIGN KEY (`jobid`) REFERENCES `job` (`jobid`),
  CONSTRAINT `jobresource_ibfk_2` FOREIGN KEY (`resourceid`) REFERENCES `resource` (`resourceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jobresource`
--

LOCK TABLES `jobresource` WRITE;
/*!40000 ALTER TABLE `jobresource` DISABLE KEYS */;
/*!40000 ALTER TABLE `jobresource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jobresourcecategory`
--

DROP TABLE IF EXISTS `jobresourcecategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jobresourcecategory` (
  `jobresourcecategoryid` int(10) NOT NULL AUTO_INCREMENT,
  `jobid` int(10) DEFAULT NULL,
  `resourcecategoryid` int(10) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`jobresourcecategoryid`),
  UNIQUE KEY `u_jobresource_rcid_jdid` (`resourcecategoryid`,`jobid`),
  KEY `fk_jobresourcecategory_jdid` (`jobid`),
  CONSTRAINT `jobresourcecategory_ibfk_1` FOREIGN KEY (`jobid`) REFERENCES `job` (`jobid`),
  CONSTRAINT `jobresourcecategory_ibfk_2` FOREIGN KEY (`resourcecategoryid`) REFERENCES `resourcecategory` (`resourcecategoryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jobresourcecategory`
--

LOCK TABLES `jobresourcecategory` WRITE;
/*!40000 ALTER TABLE `jobresourcecategory` DISABLE KEYS */;
/*!40000 ALTER TABLE `jobresourcecategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jobsample`
--

DROP TABLE IF EXISTS `jobsample`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jobsample` (
  `jobsampleid` int(10) NOT NULL AUTO_INCREMENT,
  `jobid` int(10) DEFAULT NULL,
  `sampleid` int(10) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`jobsampleid`),
  UNIQUE KEY `u_jobsample_jid_sid` (`jobid`,`sampleid`),
  KEY `fk_jobsample_sid` (`sampleid`),
  CONSTRAINT `jobsample_ibfk_1` FOREIGN KEY (`jobid`) REFERENCES `job` (`jobid`),
  CONSTRAINT `jobsample_ibfk_2` FOREIGN KEY (`sampleid`) REFERENCES `sample` (`sampleid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jobsample`
--

LOCK TABLES `jobsample` WRITE;
/*!40000 ALTER TABLE `jobsample` DISABLE KEYS */;
INSERT INTO `jobsample` VALUES (1,1,1,'2012-01-29 23:02:58',1),(4,1,3,'2012-01-29 23:05:24',1);
/*!40000 ALTER TABLE `jobsample` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jobsamplemeta`
--

DROP TABLE IF EXISTS `jobsamplemeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jobsamplemeta` (
  `jobsamplemetaid` int(10) NOT NULL AUTO_INCREMENT,
  `jobsampleid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` varchar(250) DEFAULT NULL,
  `position` int(10) DEFAULT '0',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`jobsamplemetaid`),
  UNIQUE KEY `u_jobsamplemeta_k_jsid` (`k`,`jobsampleid`),
  KEY `fk_jobsamplemeta_jsid` (`jobsampleid`),
  CONSTRAINT `jobsamplemeta_ibfk_1` FOREIGN KEY (`jobsampleid`) REFERENCES `jobsample` (`jobsampleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jobsamplemeta`
--

LOCK TABLES `jobsamplemeta` WRITE;
/*!40000 ALTER TABLE `jobsamplemeta` DISABLE KEYS */;
/*!40000 ALTER TABLE `jobsamplemeta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jobsoftware`
--

DROP TABLE IF EXISTS `jobsoftware`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jobsoftware` (
  `jobsoftwareid` int(10) NOT NULL AUTO_INCREMENT,
  `jobid` int(10) DEFAULT NULL,
  `softwareid` int(10) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`jobsoftwareid`),
  UNIQUE KEY `u_jobsoftware_rid_jdid` (`softwareid`,`jobid`),
  KEY `fk_jobsoftware_jdid` (`jobid`),
  CONSTRAINT `jobsoftware_ibfk_1` FOREIGN KEY (`jobid`) REFERENCES `job` (`jobid`),
  CONSTRAINT `jobsoftware_ibfk_2` FOREIGN KEY (`softwareid`) REFERENCES `software` (`softwareid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jobsoftware`
--

LOCK TABLES `jobsoftware` WRITE;
/*!40000 ALTER TABLE `jobsoftware` DISABLE KEYS */;
/*!40000 ALTER TABLE `jobsoftware` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jobuser`
--

DROP TABLE IF EXISTS `jobuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jobuser` (
  `jobuserid` int(10) NOT NULL AUTO_INCREMENT,
  `jobid` int(10) DEFAULT NULL,
  `userid` int(10) DEFAULT NULL,
  `roleid` int(10) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`jobuserid`),
  UNIQUE KEY `u_jobuser_jid_uid` (`jobid`,`userid`),
  KEY `fk_jobuser_uid` (`userid`),
  KEY `fk_jobuser_rid` (`roleid`),
  CONSTRAINT `jobuser_ibfk_1` FOREIGN KEY (`jobid`) REFERENCES `job` (`jobid`),
  CONSTRAINT `jobuser_ibfk_2` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`),
  CONSTRAINT `jobuser_ibfk_3` FOREIGN KEY (`roleid`) REFERENCES `role` (`roleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jobuser`
--

LOCK TABLES `jobuser` WRITE;
/*!40000 ALTER TABLE `jobuser` DISABLE KEYS */;
/*!40000 ALTER TABLE `jobuser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lab`
--

DROP TABLE IF EXISTS `lab`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lab` (
  `labid` int(10) NOT NULL AUTO_INCREMENT,
  `departmentid` int(10) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `primaryuserid` int(10) DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`labid`),
  KEY `fk_lab_did` (`departmentid`),
  KEY `fk_lab_puid` (`primaryuserid`),
  CONSTRAINT `lab_ibfk_1` FOREIGN KEY (`departmentid`) REFERENCES `department` (`departmentid`),
  CONSTRAINT `lab_ibfk_2` FOREIGN KEY (`primaryuserid`) REFERENCES `user` (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lab`
--

LOCK TABLES `lab` WRITE;
/*!40000 ALTER TABLE `lab` DISABLE KEYS */;
INSERT INTO `lab` VALUES (1,1,'default lab',1,1,'2012-01-29 22:51:52',1);
/*!40000 ALTER TABLE `lab` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `labmeta`
--

DROP TABLE IF EXISTS `labmeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `labmeta` (
  `labmetaid` int(10) NOT NULL AUTO_INCREMENT,
  `labid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` varchar(250) DEFAULT NULL,
  `position` int(10) DEFAULT '0',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`labmetaid`),
  UNIQUE KEY `u_labmeta_k_lid` (`k`,`labid`),
  KEY `fk_labmeta_labid` (`labid`),
  CONSTRAINT `labmeta_ibfk_1` FOREIGN KEY (`labid`) REFERENCES `lab` (`labid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `labmeta`
--

LOCK TABLES `labmeta` WRITE;
/*!40000 ALTER TABLE `labmeta` DISABLE KEYS */;
/*!40000 ALTER TABLE `labmeta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `labpending`
--

DROP TABLE IF EXISTS `labpending`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `labpending` (
  `labpendingid` int(10) NOT NULL AUTO_INCREMENT,
  `departmentid` int(10) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `primaryuserid` int(10) DEFAULT NULL,
  `userpendingid` int(10) DEFAULT NULL,
  `status` varchar(10) DEFAULT 'PENDING',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`labpendingid`),
  KEY `fk_labpending_did` (`departmentid`),
  KEY `fk_labpending_pruid` (`primaryuserid`),
  KEY `fk_labpending_peuid` (`userpendingid`),
  KEY `status` (`status`,`name`),
  CONSTRAINT `labpending_ibfk_1` FOREIGN KEY (`departmentid`) REFERENCES `department` (`departmentid`),
  CONSTRAINT `labpending_ibfk_2` FOREIGN KEY (`primaryuserid`) REFERENCES `user` (`userid`),
  CONSTRAINT `labpending_ibfk_3` FOREIGN KEY (`userpendingid`) REFERENCES `userpending` (`userpendingid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `labpending`
--

LOCK TABLES `labpending` WRITE;
/*!40000 ALTER TABLE `labpending` DISABLE KEYS */;
/*!40000 ALTER TABLE `labpending` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `labpendingmeta`
--

DROP TABLE IF EXISTS `labpendingmeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `labpendingmeta` (
  `labpendingmetaid` int(10) NOT NULL AUTO_INCREMENT,
  `labpendingid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` varchar(250) DEFAULT NULL,
  `position` int(10) DEFAULT '0',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`labpendingmetaid`),
  UNIQUE KEY `u_labpendingmeta_k_lid` (`k`,`labpendingid`),
  KEY `fk_labpendingmeta_labpendingid` (`labpendingid`),
  CONSTRAINT `labpendingmeta_ibfk_1` FOREIGN KEY (`labpendingid`) REFERENCES `labpending` (`labpendingid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `labpendingmeta`
--

LOCK TABLES `labpendingmeta` WRITE;
/*!40000 ALTER TABLE `labpendingmeta` DISABLE KEYS */;
/*!40000 ALTER TABLE `labpendingmeta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `labuser`
--

DROP TABLE IF EXISTS `labuser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `labuser` (
  `labuserid` int(10) NOT NULL AUTO_INCREMENT,
  `labid` int(10) DEFAULT NULL,
  `userid` int(10) DEFAULT NULL,
  `roleid` int(10) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`labuserid`),
  UNIQUE KEY `u_labuser_lid_uid` (`labid`,`userid`),
  KEY `fk_labuser_uid` (`userid`),
  KEY `fk_labuser_rid` (`roleid`),
  CONSTRAINT `labuser_ibfk_1` FOREIGN KEY (`labid`) REFERENCES `lab` (`labid`),
  CONSTRAINT `labuser_ibfk_2` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`),
  CONSTRAINT `labuser_ibfk_3` FOREIGN KEY (`roleid`) REFERENCES `role` (`roleid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `labuser`
--

LOCK TABLES `labuser` WRITE;
/*!40000 ALTER TABLE `labuser` DISABLE KEYS */;
INSERT INTO `labuser` VALUES (1,1,1,6,'2012-01-29 22:51:52',1);
/*!40000 ALTER TABLE `labuser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meta`
--

DROP TABLE IF EXISTS `meta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meta` (
  `metaid` int(10) NOT NULL AUTO_INCREMENT,
  `property` varchar(250) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` varchar(250) DEFAULT NULL,
  `position` int(10) DEFAULT '0',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`metaid`),
  UNIQUE KEY `u_meta_p_k` (`property`,`k`),
  UNIQUE KEY `u_meta_p_v` (`property`,`v`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meta`
--

LOCK TABLES `meta` WRITE;
/*!40000 ALTER TABLE `meta` DISABLE KEYS */;
/*!40000 ALTER TABLE `meta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project` (
  `projectid` int(10) NOT NULL AUTO_INCREMENT,
  `labid` int(10) DEFAULT NULL,
  `userid` int(10) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`projectid`),
  UNIQUE KEY `u_project_name_lid` (`name`,`labid`),
  KEY `fk_project_lid` (`labid`),
  CONSTRAINT `project_ibfk_1` FOREIGN KEY (`labid`) REFERENCES `lab` (`labid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resource`
--

DROP TABLE IF EXISTS `resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resource` (
  `resourceid` int(10) NOT NULL AUTO_INCREMENT,
  `resourcecategoryid` int(10) DEFAULT NULL,
  `typeresourceid` int(10) DEFAULT NULL,
  `iname` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`resourceid`),
  UNIQUE KEY `u_resource_i` (`iname`),
  UNIQUE KEY `u_resource_n` (`name`),
  KEY `fk_resource_trid` (`typeresourceid`),
  KEY `fk_resource_rid` (`resourcecategoryid`),
  CONSTRAINT `resource_ibfk_1` FOREIGN KEY (`typeresourceid`) REFERENCES `typeresource` (`typeresourceid`),
  CONSTRAINT `resource_ibfk_2` FOREIGN KEY (`resourcecategoryid`) REFERENCES `resourcecategory` (`resourcecategoryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resource`
--

LOCK TABLES `resource` WRITE;
/*!40000 ALTER TABLE `resource` DISABLE KEYS */;
/*!40000 ALTER TABLE `resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resourcebarcode`
--

DROP TABLE IF EXISTS `resourcebarcode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resourcebarcode` (
  `resourcebarcodeid` int(10) NOT NULL AUTO_INCREMENT,
  `resourceid` int(10) DEFAULT NULL,
  `barcodeid` int(10) DEFAULT NULL,
  PRIMARY KEY (`resourcebarcodeid`),
  UNIQUE KEY `u_resourcebarcode_rid` (`resourceid`),
  UNIQUE KEY `u_resourcebarcode_bcid` (`barcodeid`),
  CONSTRAINT `resourcebarcode_ibfk_1` FOREIGN KEY (`resourceid`) REFERENCES `resource` (`resourceid`),
  CONSTRAINT `resourcebarcode_ibfk_2` FOREIGN KEY (`barcodeid`) REFERENCES `barcode` (`barcodeid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resourcebarcode`
--

LOCK TABLES `resourcebarcode` WRITE;
/*!40000 ALTER TABLE `resourcebarcode` DISABLE KEYS */;
/*!40000 ALTER TABLE `resourcebarcode` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resourcecategory`
--

DROP TABLE IF EXISTS `resourcecategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resourcecategory` (
  `resourcecategoryid` int(10) NOT NULL AUTO_INCREMENT,
  `typeresourceid` int(10) NOT NULL,
  `iname` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`resourcecategoryid`),
  UNIQUE KEY `u_resourcecategory_i` (`iname`),
  UNIQUE KEY `u_resourcecategory_n` (`name`),
  KEY `fk_resourccategory_typeresourceid` (`typeresourceid`),
  CONSTRAINT `resourcecategory_ibfk_1` FOREIGN KEY (`typeresourceid`) REFERENCES `typeresource` (`typeresourceid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resourcecategory`
--

LOCK TABLES `resourcecategory` WRITE;
/*!40000 ALTER TABLE `resourcecategory` DISABLE KEYS */;
INSERT INTO `resourcecategory` VALUES (1,1,'illuminaHiSeq2000','Illumina HiSeq 2000','2012-01-29 22:50:21',0);
/*!40000 ALTER TABLE `resourcecategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resourcecategorymeta`
--

DROP TABLE IF EXISTS `resourcecategorymeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resourcecategorymeta` (
  `resourcecategorymetaid` int(10) NOT NULL AUTO_INCREMENT,
  `resourcecategoryid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` varchar(250) DEFAULT NULL,
  `position` int(10) DEFAULT '0',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`resourcecategorymetaid`),
  UNIQUE KEY `u_resourcecategorymeta_k_rid` (`k`,`resourcecategoryid`),
  KEY `fk_resourccategoryemeta_resourcecategoryid` (`resourcecategoryid`),
  CONSTRAINT `resourcecategorymeta_ibfk_1` FOREIGN KEY (`resourcecategoryid`) REFERENCES `resourcecategory` (`resourcecategoryid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resourcecategorymeta`
--

LOCK TABLES `resourcecategorymeta` WRITE;
/*!40000 ALTER TABLE `resourcecategorymeta` DISABLE KEYS */;
INSERT INTO `resourcecategorymeta` VALUES (1,1,'resource.allowableUiField.readlength','50:50;75:75;100:100;150:150',1,'2012-01-29 22:50:21',0),(2,1,'resource.allowableUiField.readType','single:single;paired:paired',2,'2012-01-29 22:50:21',0);
/*!40000 ALTER TABLE `resourcecategorymeta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resourcelane`
--

DROP TABLE IF EXISTS `resourcelane`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resourcelane` (
  `resourcelaneid` int(10) NOT NULL AUTO_INCREMENT,
  `resourceid` int(10) DEFAULT NULL,
  `iname` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  PRIMARY KEY (`resourcelaneid`),
  UNIQUE KEY `u_resourcelane_iname_rid` (`iname`,`resourceid`),
  UNIQUE KEY `u_resourcelane_name_rid` (`name`,`resourceid`),
  KEY `fk_resourcelane_rid` (`resourceid`),
  CONSTRAINT `resourcelane_ibfk_1` FOREIGN KEY (`resourceid`) REFERENCES `resource` (`resourceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resourcelane`
--

LOCK TABLES `resourcelane` WRITE;
/*!40000 ALTER TABLE `resourcelane` DISABLE KEYS */;
/*!40000 ALTER TABLE `resourcelane` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resourcemeta`
--

DROP TABLE IF EXISTS `resourcemeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resourcemeta` (
  `resourcemetaid` int(10) NOT NULL AUTO_INCREMENT,
  `resourceid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` varchar(250) DEFAULT NULL,
  `position` int(10) DEFAULT '0',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`resourcemetaid`),
  UNIQUE KEY `u_resourcemeta_k_rid` (`k`,`resourceid`),
  KEY `fk_resourcemeta_resourceid` (`resourceid`),
  CONSTRAINT `resourcemeta_ibfk_1` FOREIGN KEY (`resourceid`) REFERENCES `resource` (`resourceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resourcemeta`
--

LOCK TABLES `resourcemeta` WRITE;
/*!40000 ALTER TABLE `resourcemeta` DISABLE KEYS */;
/*!40000 ALTER TABLE `resourcemeta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `roleid` int(10) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `domain` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`roleid`),
  UNIQUE KEY `u_role_rname` (`rolename`),
  UNIQUE KEY `u_role_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'fm','Facilities Manager','system'),(2,'sa','System Administrator','system'),(3,'ga','General Administrator','system'),(4,'da','Department Administrator','department'),(5,'ft','Facilities Tech','system'),(6,'pi','Primary Investigator','lab'),(7,'lm','Lab Manager','lab'),(8,'lu','Lab Member','lab'),(9,'js','Job Submitter','job'),(10,'jv','Job Viewer','job'),(11,'su','Super user','system'),(12,'lx','Lab Member Inactive','lab'),(13,'lp','Lab Member Pending','lab'),(14,'jd','Job Drafter','jobdraft'),(15,'u','User','user'),(16,'god','God','system');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roleset`
--

DROP TABLE IF EXISTS `roleset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roleset` (
  `rolesetid` int(10) NOT NULL AUTO_INCREMENT,
  `parentroleid` int(10) DEFAULT NULL,
  `childroleid` int(10) DEFAULT NULL,
  PRIMARY KEY (`rolesetid`),
  UNIQUE KEY `u_role_rname` (`parentroleid`,`childroleid`),
  KEY `fk_roleset_crid` (`childroleid`),
  CONSTRAINT `roleset_ibfk_1` FOREIGN KEY (`parentroleid`) REFERENCES `role` (`roleid`),
  CONSTRAINT `roleset_ibfk_2` FOREIGN KEY (`childroleid`) REFERENCES `role` (`roleid`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roleset`
--

LOCK TABLES `roleset` WRITE;
/*!40000 ALTER TABLE `roleset` DISABLE KEYS */;
INSERT INTO `roleset` VALUES (2,1,1),(32,1,5),(14,2,2),(4,3,3),(1,4,4),(3,5,5),(13,6,6),(33,6,7),(34,6,8),(9,7,7),(35,7,8),(11,8,8),(7,9,9),(36,9,10),(8,10,10),(37,11,1),(38,11,2),(39,11,3),(40,11,5),(15,11,11),(41,11,16),(12,12,12),(10,13,13),(6,14,14),(16,15,15),(42,16,1),(43,16,2),(44,16,3),(46,16,5),(45,16,11),(5,16,16);
/*!40000 ALTER TABLE `roleset` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `run`
--

DROP TABLE IF EXISTS `run`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `run` (
  `runid` int(10) NOT NULL AUTO_INCREMENT,
  `resourceid` int(10) DEFAULT NULL,
  `resourcecategoryid` int(10) DEFAULT NULL,
  `softwareid` int(10) DEFAULT NULL,
  `userid` int(10) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `sampleid` int(10) DEFAULT NULL,
  `startts` datetime DEFAULT NULL,
  `endts` datetime DEFAULT NULL,
  `status` varchar(250) DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`runid`),
  KEY `fk_run_rid` (`resourceid`),
  KEY `fk_run_rcid` (`resourcecategoryid`),
  KEY `fk_run_swid` (`softwareid`),
  KEY `fk_run_sid` (`sampleid`),
  KEY `fk_run_userid` (`userid`),
  CONSTRAINT `run_ibfk_1` FOREIGN KEY (`resourceid`) REFERENCES `resource` (`resourceid`),
  CONSTRAINT `run_ibfk_2` FOREIGN KEY (`resourcecategoryid`) REFERENCES `resourcecategory` (`resourcecategoryid`),
  CONSTRAINT `run_ibfk_3` FOREIGN KEY (`softwareid`) REFERENCES `software` (`softwareid`),
  CONSTRAINT `run_ibfk_4` FOREIGN KEY (`sampleid`) REFERENCES `sample` (`sampleid`),
  CONSTRAINT `run_ibfk_5` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `run`
--

LOCK TABLES `run` WRITE;
/*!40000 ALTER TABLE `run` DISABLE KEYS */;
/*!40000 ALTER TABLE `run` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `runfile`
--

DROP TABLE IF EXISTS `runfile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `runfile` (
  `runlanefileid` int(10) NOT NULL AUTO_INCREMENT,
  `runid` int(10) DEFAULT NULL,
  `fileid` int(10) DEFAULT NULL,
  `iname` varchar(2048) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`runlanefileid`),
  UNIQUE KEY `u_rlfile_fileid` (`fileid`),
  KEY `fk_rfile_rid` (`runid`),
  CONSTRAINT `runfile_ibfk_1` FOREIGN KEY (`runid`) REFERENCES `run` (`runid`),
  CONSTRAINT `runfile_ibfk_2` FOREIGN KEY (`fileid`) REFERENCES `file` (`fileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `runfile`
--

LOCK TABLES `runfile` WRITE;
/*!40000 ALTER TABLE `runfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `runfile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `runlane`
--

DROP TABLE IF EXISTS `runlane`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `runlane` (
  `runlaneid` int(10) NOT NULL AUTO_INCREMENT,
  `runid` int(10) DEFAULT NULL,
  `resourcelaneid` int(10) DEFAULT NULL,
  `sampleid` int(10) DEFAULT NULL,
  PRIMARY KEY (`runlaneid`),
  UNIQUE KEY `u_runlane_rid_lid` (`runid`,`resourcelaneid`),
  UNIQUE KEY `u_runlane_sid_rid` (`sampleid`,`runid`),
  KEY `fk_runlane_lid` (`resourcelaneid`),
  CONSTRAINT `runlane_ibfk_1` FOREIGN KEY (`runid`) REFERENCES `run` (`runid`),
  CONSTRAINT `runlane_ibfk_2` FOREIGN KEY (`resourcelaneid`) REFERENCES `resourcelane` (`resourcelaneid`),
  CONSTRAINT `runlane_ibfk_3` FOREIGN KEY (`sampleid`) REFERENCES `sample` (`sampleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `runlane`
--

LOCK TABLES `runlane` WRITE;
/*!40000 ALTER TABLE `runlane` DISABLE KEYS */;
/*!40000 ALTER TABLE `runlane` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `runlanefile`
--

DROP TABLE IF EXISTS `runlanefile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `runlanefile` (
  `runlanefileid` int(10) NOT NULL AUTO_INCREMENT,
  `runlaneid` int(10) DEFAULT NULL,
  `fileid` int(10) DEFAULT NULL,
  `iname` varchar(2048) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`runlanefileid`),
  UNIQUE KEY `u_rlfile_fileid` (`fileid`),
  KEY `fk_rlfile_rlid` (`runlaneid`),
  CONSTRAINT `runlanefile_ibfk_1` FOREIGN KEY (`runlaneid`) REFERENCES `runlane` (`runlaneid`),
  CONSTRAINT `runlanefile_ibfk_2` FOREIGN KEY (`fileid`) REFERENCES `file` (`fileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `runlanefile`
--

LOCK TABLES `runlanefile` WRITE;
/*!40000 ALTER TABLE `runlanefile` DISABLE KEYS */;
/*!40000 ALTER TABLE `runlanefile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `runmeta`
--

DROP TABLE IF EXISTS `runmeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `runmeta` (
  `runmetaid` int(10) NOT NULL AUTO_INCREMENT,
  `runid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` varchar(250) DEFAULT NULL,
  `position` int(10) DEFAULT '0',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`runmetaid`),
  UNIQUE KEY `u_runmeta_k_rid` (`k`,`runid`),
  KEY `fk_runmeta_runid` (`runid`),
  CONSTRAINT `runmeta_ibfk_1` FOREIGN KEY (`runid`) REFERENCES `run` (`runid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `runmeta`
--

LOCK TABLES `runmeta` WRITE;
/*!40000 ALTER TABLE `runmeta` DISABLE KEYS */;
/*!40000 ALTER TABLE `runmeta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sample`
--

DROP TABLE IF EXISTS `sample`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sample` (
  `sampleid` int(10) NOT NULL AUTO_INCREMENT,
  `typesampleid` int(10) DEFAULT NULL,
  `subtypesampleid` int(10) DEFAULT NULL,
  `submitter_labid` int(10) DEFAULT NULL,
  `submitter_userid` int(10) DEFAULT NULL,
  `submitter_jobid` int(10) DEFAULT NULL,
  `isreceived` int(1) DEFAULT '0',
  `receiver_userid` int(10) DEFAULT NULL,
  `receivedts` datetime DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `isgood` int(1) DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`sampleid`),
  KEY `fk_sample_tsid` (`typesampleid`),
  KEY `fk_sample_stsid` (`subtypesampleid`),
  KEY `fk_sample_sjid` (`submitter_jobid`),
  KEY `fk_sample_slid` (`submitter_labid`),
  KEY `fk_sample_suid` (`submitter_userid`),
  CONSTRAINT `sample_ibfk_1` FOREIGN KEY (`typesampleid`) REFERENCES `typesample` (`typesampleid`),
  CONSTRAINT `sample_ibfk_2` FOREIGN KEY (`subtypesampleid`) REFERENCES `subtypesample` (`subtypesampleid`),
  CONSTRAINT `sample_ibfk_3` FOREIGN KEY (`submitter_jobid`) REFERENCES `job` (`jobid`),
  CONSTRAINT `sample_ibfk_4` FOREIGN KEY (`submitter_labid`) REFERENCES `lab` (`labid`),
  CONSTRAINT `sample_ibfk_5` FOREIGN KEY (`submitter_userid`) REFERENCES `user` (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sample`
--

LOCK TABLES `sample` WRITE;
/*!40000 ALTER TABLE `sample` DISABLE KEYS */;
INSERT INTO `sample` VALUES (1,1,1,1,1,1,1,NULL,NULL,'dna A',1,1,'2012-01-29 23:02:58',NULL),(3,3,2,1,1,1,1,1,'2012-01-29 18:04:56','library A',1,1,'2012-01-29 23:04:56',NULL),(14,5,3,NULL,1,NULL,1,1,'2012-01-29 18:07:51','flowcell A',1,1,'2012-01-29 23:07:51',NULL),(15,4,NULL,NULL,1,NULL,1,1,'2012-01-29 18:07:51','flowcell lane A/1',1,1,'2012-01-29 23:07:51',NULL),(16,4,NULL,NULL,1,NULL,1,1,'2012-01-29 18:07:51','flowcell lane A/2',1,1,'2012-01-29 23:07:51',NULL),(17,4,NULL,NULL,1,NULL,1,1,'2012-01-29 18:07:51','flowcell lane A/3',1,1,'2012-01-29 23:07:51',NULL),(18,4,NULL,NULL,1,NULL,1,1,'2012-01-29 18:07:51','flowcell lane A/4',1,1,'2012-01-29 23:07:51',NULL),(19,4,NULL,NULL,1,NULL,1,1,'2012-01-29 18:07:51','flowcell lane A/5',1,1,'2012-01-29 23:07:51',NULL),(20,4,NULL,NULL,1,NULL,1,1,'2012-01-29 18:07:51','flowcell lane A/6',1,1,'2012-01-29 23:07:51',NULL),(21,4,NULL,NULL,1,NULL,1,1,'2012-01-29 18:07:51','flowcell lane A/7',1,1,'2012-01-29 23:07:51',NULL),(22,4,NULL,NULL,1,NULL,1,1,'2012-01-29 18:07:51','flowcell lane A/8',1,1,'2012-01-29 23:07:51',NULL);
/*!40000 ALTER TABLE `sample` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `samplebarcode`
--

DROP TABLE IF EXISTS `samplebarcode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samplebarcode` (
  `samplebarcode` int(10) NOT NULL AUTO_INCREMENT,
  `sampleid` int(10) DEFAULT NULL,
  `barcodeid` int(10) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`samplebarcode`),
  UNIQUE KEY `u_samplebarcode_sid` (`sampleid`),
  UNIQUE KEY `u_samplebarcode_bcid` (`barcodeid`),
  CONSTRAINT `samplebarcode_ibfk_1` FOREIGN KEY (`sampleid`) REFERENCES `sample` (`sampleid`),
  CONSTRAINT `samplebarcode_ibfk_2` FOREIGN KEY (`barcodeid`) REFERENCES `barcode` (`barcodeid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `samplebarcode`
--

LOCK TABLES `samplebarcode` WRITE;
/*!40000 ALTER TABLE `samplebarcode` DISABLE KEYS */;
/*!40000 ALTER TABLE `samplebarcode` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `samplecell`
--

DROP TABLE IF EXISTS `samplecell`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samplecell` (
  `samplecellid` int(10) NOT NULL AUTO_INCREMENT,
  `sampleid` int(10) DEFAULT NULL,
  `jobcellid` int(10) DEFAULT NULL,
  `libraryindex` int(10) DEFAULT NULL,
  PRIMARY KEY (`samplecellid`),
  UNIQUE KEY `u_samplecell_jdcid_li` (`jobcellid`,`libraryindex`),
  KEY `fk_samplecell_sdid` (`sampleid`),
  CONSTRAINT `samplecell_ibfk_1` FOREIGN KEY (`sampleid`) REFERENCES `sample` (`sampleid`),
  CONSTRAINT `samplecell_ibfk_2` FOREIGN KEY (`jobcellid`) REFERENCES `jobcell` (`jobcellid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `samplecell`
--

LOCK TABLES `samplecell` WRITE;
/*!40000 ALTER TABLE `samplecell` DISABLE KEYS */;
/*!40000 ALTER TABLE `samplecell` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sampledraft`
--

DROP TABLE IF EXISTS `sampledraft`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sampledraft` (
  `sampledraftid` int(10) NOT NULL AUTO_INCREMENT,
  `typesampleid` int(10) DEFAULT NULL,
  `subtypesampleid` int(10) DEFAULT NULL,
  `labid` int(10) DEFAULT NULL,
  `userid` int(10) DEFAULT NULL,
  `jobdraftid` int(10) DEFAULT NULL,
  `sourcesampleid` int(10) DEFAULT NULL,
  `fileid` int(10) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`sampledraftid`),
  KEY `fk_sampledraft_tsid` (`typesampleid`),
  KEY `fk_sampledraft_stsid` (`subtypesampleid`),
  KEY `fk_sampledraft_sjid` (`jobdraftid`),
  KEY `fk_sampledraft_slid` (`labid`),
  KEY `fk_sampledraft_suid` (`userid`),
  KEY `fk_sampledraft_fid` (`fileid`),
  CONSTRAINT `sampledraft_ibfk_1` FOREIGN KEY (`typesampleid`) REFERENCES `typesample` (`typesampleid`),
  CONSTRAINT `sampledraft_ibfk_2` FOREIGN KEY (`subtypesampleid`) REFERENCES `subtypesample` (`subtypesampleid`),
  CONSTRAINT `sampledraft_ibfk_3` FOREIGN KEY (`jobdraftid`) REFERENCES `jobdraft` (`jobdraftid`),
  CONSTRAINT `sampledraft_ibfk_4` FOREIGN KEY (`labid`) REFERENCES `lab` (`labid`),
  CONSTRAINT `sampledraft_ibfk_5` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`),
  CONSTRAINT `sampledraft_ibfk_6` FOREIGN KEY (`fileid`) REFERENCES `file` (`fileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sampledraft`
--

LOCK TABLES `sampledraft` WRITE;
/*!40000 ALTER TABLE `sampledraft` DISABLE KEYS */;
/*!40000 ALTER TABLE `sampledraft` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sampledraftcell`
--

DROP TABLE IF EXISTS `sampledraftcell`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sampledraftcell` (
  `sampledraftcellid` int(10) NOT NULL AUTO_INCREMENT,
  `sampledraftid` int(10) DEFAULT NULL,
  `jobdraftcellid` int(10) DEFAULT NULL,
  `libraryindex` int(10) DEFAULT NULL,
  PRIMARY KEY (`sampledraftcellid`),
  UNIQUE KEY `u_sampledraftcell_jdcid_li` (`jobdraftcellid`,`libraryindex`),
  KEY `fk_sampledraftcell_sdid` (`sampledraftid`),
  CONSTRAINT `sampledraftcell_ibfk_1` FOREIGN KEY (`sampledraftid`) REFERENCES `sampledraft` (`sampledraftid`),
  CONSTRAINT `sampledraftcell_ibfk_2` FOREIGN KEY (`jobdraftcellid`) REFERENCES `jobdraftcell` (`jobdraftcellid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sampledraftcell`
--

LOCK TABLES `sampledraftcell` WRITE;
/*!40000 ALTER TABLE `sampledraftcell` DISABLE KEYS */;
/*!40000 ALTER TABLE `sampledraftcell` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sampledraftmeta`
--

DROP TABLE IF EXISTS `sampledraftmeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sampledraftmeta` (
  `sampledraftmetaid` int(10) NOT NULL AUTO_INCREMENT,
  `sampledraftid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` varchar(250) DEFAULT NULL,
  `position` int(10) DEFAULT '0',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`sampledraftmetaid`),
  UNIQUE KEY `u_sampledraftmeta_k_sid` (`k`,`sampledraftid`),
  KEY `fk_sampledraftmeta_sdid` (`sampledraftid`),
  CONSTRAINT `sampledraftmeta_ibfk_1` FOREIGN KEY (`sampledraftid`) REFERENCES `sampledraft` (`sampledraftid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sampledraftmeta`
--

LOCK TABLES `sampledraftmeta` WRITE;
/*!40000 ALTER TABLE `sampledraftmeta` DISABLE KEYS */;
/*!40000 ALTER TABLE `sampledraftmeta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `samplefile`
--

DROP TABLE IF EXISTS `samplefile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samplefile` (
  `samplefileid` int(10) NOT NULL AUTO_INCREMENT,
  `sampleid` int(10) DEFAULT NULL,
  `fileid` int(10) DEFAULT NULL,
  `iname` varchar(2048) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `description` varchar(2048) DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`samplefileid`),
  KEY `fk_samplefile_sid` (`sampleid`),
  KEY `fk_samplefile_fid` (`fileid`),
  CONSTRAINT `samplefile_ibfk_1` FOREIGN KEY (`sampleid`) REFERENCES `sample` (`sampleid`),
  CONSTRAINT `samplefile_ibfk_2` FOREIGN KEY (`fileid`) REFERENCES `file` (`fileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `samplefile`
--

LOCK TABLES `samplefile` WRITE;
/*!40000 ALTER TABLE `samplefile` DISABLE KEYS */;
/*!40000 ALTER TABLE `samplefile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `samplelab`
--

DROP TABLE IF EXISTS `samplelab`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samplelab` (
  `samplelabid` int(10) NOT NULL AUTO_INCREMENT,
  `sampleid` int(10) DEFAULT NULL,
  `labid` int(10) DEFAULT NULL,
  `isprimary` int(1) DEFAULT '0',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`samplelabid`),
  UNIQUE KEY `u_samplelab_sid_lid` (`sampleid`,`labid`),
  KEY `fk_samplelab_lid` (`labid`),
  CONSTRAINT `samplelab_ibfk_1` FOREIGN KEY (`sampleid`) REFERENCES `sample` (`sampleid`),
  CONSTRAINT `samplelab_ibfk_2` FOREIGN KEY (`labid`) REFERENCES `lab` (`labid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `samplelab`
--

LOCK TABLES `samplelab` WRITE;
/*!40000 ALTER TABLE `samplelab` DISABLE KEYS */;
/*!40000 ALTER TABLE `samplelab` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `samplemeta`
--

DROP TABLE IF EXISTS `samplemeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samplemeta` (
  `samplemetaid` int(10) NOT NULL AUTO_INCREMENT,
  `sampleid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` varchar(250) DEFAULT NULL,
  `position` int(10) DEFAULT '0',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`samplemetaid`),
  UNIQUE KEY `u_samplemeta_k_sid` (`k`,`sampleid`),
  KEY `fk_samplemeta_sampleid` (`sampleid`),
  CONSTRAINT `samplemeta_ibfk_1` FOREIGN KEY (`sampleid`) REFERENCES `sample` (`sampleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `samplemeta`
--

LOCK TABLES `samplemeta` WRITE;
/*!40000 ALTER TABLE `samplemeta` DISABLE KEYS */;
/*!40000 ALTER TABLE `samplemeta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `samplesource`
--

DROP TABLE IF EXISTS `samplesource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samplesource` (
  `samplesourceid` int(10) NOT NULL AUTO_INCREMENT,
  `sampleid` int(10) DEFAULT NULL,
  `multiplexindex` int(10) DEFAULT '0',
  `source_sampleid` int(10) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`samplesourceid`),
  UNIQUE KEY `u_samplesource_sid` (`sampleid`,`multiplexindex`),
  KEY `fk_samplesource_ssid` (`source_sampleid`),
  CONSTRAINT `samplesource_ibfk_1` FOREIGN KEY (`sampleid`) REFERENCES `sample` (`sampleid`),
  CONSTRAINT `samplesource_ibfk_2` FOREIGN KEY (`source_sampleid`) REFERENCES `sample` (`sampleid`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `samplesource`
--

LOCK TABLES `samplesource` WRITE;
/*!40000 ALTER TABLE `samplesource` DISABLE KEYS */;
INSERT INTO `samplesource` VALUES (1,3,1,1,'2012-01-29 23:05:24',1),(2,14,1,15,'2012-01-29 23:08:05',1),(3,14,2,16,'2012-01-29 23:08:05',1),(4,14,3,17,'2012-01-29 23:08:05',1),(5,14,4,18,'2012-01-29 23:08:05',1),(6,14,5,19,'2012-01-29 23:08:05',1),(7,14,6,20,'2012-01-29 23:08:05',1),(8,14,7,21,'2012-01-29 23:08:05',1),(9,14,8,22,'2012-01-29 23:08:05',1),(17,15,0,3,'2012-01-29 23:11:18',0);
/*!40000 ALTER TABLE `samplesource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `samplesourcemeta`
--

DROP TABLE IF EXISTS `samplesourcemeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samplesourcemeta` (
  `samplesourcemetaid` int(10) NOT NULL AUTO_INCREMENT,
  `samplesourceid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` varchar(250) DEFAULT NULL,
  `position` int(10) DEFAULT '0',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`samplesourcemetaid`),
  UNIQUE KEY `u_samplesourcemeta_k_sid` (`k`,`samplesourceid`),
  KEY `fk_samplesourcemeta_sampleid` (`samplesourceid`),
  CONSTRAINT `samplesourcemeta_ibfk_1` FOREIGN KEY (`samplesourceid`) REFERENCES `samplesource` (`samplesourceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `samplesourcemeta`
--

LOCK TABLES `samplesourcemeta` WRITE;
/*!40000 ALTER TABLE `samplesourcemeta` DISABLE KEYS */;
/*!40000 ALTER TABLE `samplesourcemeta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `software`
--

DROP TABLE IF EXISTS `software`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `software` (
  `softwareid` int(10) NOT NULL AUTO_INCREMENT,
  `typeresourceid` int(10) NOT NULL,
  `iname` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`softwareid`),
  UNIQUE KEY `u_software_i` (`iname`),
  UNIQUE KEY `u_software_n` (`name`),
  KEY `fk_software_typeresourceid` (`typeresourceid`),
  CONSTRAINT `software_ibfk_1` FOREIGN KEY (`typeresourceid`) REFERENCES `typeresource` (`typeresourceid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `software`
--

LOCK TABLES `software` WRITE;
/*!40000 ALTER TABLE `software` DISABLE KEYS */;
INSERT INTO `software` VALUES (1,3,'bowtieAlignerV0_12_7','Bowtie Aligner','2012-01-29 22:50:21',0),(2,4,'macsPeakcallerV4_1','MACS Peakcaller','2012-01-29 22:50:21',0);
/*!40000 ALTER TABLE `software` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `softwaremeta`
--

DROP TABLE IF EXISTS `softwaremeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `softwaremeta` (
  `softwaremetaid` int(10) NOT NULL AUTO_INCREMENT,
  `softwareid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` varchar(250) DEFAULT NULL,
  `position` int(10) DEFAULT '0',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`softwaremetaid`),
  UNIQUE KEY `u_softwaremeta_k_rid` (`k`,`softwareid`),
  KEY `fk_softwaremeta_sid` (`softwareid`),
  CONSTRAINT `softwaremeta_ibfk_1` FOREIGN KEY (`softwareid`) REFERENCES `software` (`softwareid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `softwaremeta`
--

LOCK TABLES `softwaremeta` WRITE;
/*!40000 ALTER TABLE `softwaremeta` DISABLE KEYS */;
INSERT INTO `softwaremeta` VALUES (1,1,'bowtieAlignerV0_12_7.version','0.12.7',1,'2012-01-29 22:50:21',0),(2,2,'macsPeakcallerV4_1.version','4.1',1,'2012-01-29 22:50:21',0);
/*!40000 ALTER TABLE `softwaremeta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `state`
--

DROP TABLE IF EXISTS `state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `state` (
  `stateid` int(10) NOT NULL AUTO_INCREMENT,
  `taskid` int(10) NOT NULL,
  `name` varchar(250) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `source_stateid` int(10) DEFAULT NULL,
  `startts` datetime DEFAULT NULL,
  `endts` datetime DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`stateid`),
  KEY `fk_state_tid` (`taskid`),
  KEY `fk_state_ssid` (`source_stateid`),
  CONSTRAINT `state_ibfk_1` FOREIGN KEY (`taskid`) REFERENCES `task` (`taskid`),
  CONSTRAINT `state_ibfk_2` FOREIGN KEY (`source_stateid`) REFERENCES `state` (`stateid`)
) ENGINE=InnoDB AUTO_INCREMENT=1013 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `state`
--

LOCK TABLES `state` WRITE;
/*!40000 ALTER TABLE `state` DISABLE KEYS */;
INSERT INTO `state` VALUES (1,18,'Start Job','CREATED',NULL,'2012-01-29 18:02:58',NULL,'2012-01-29 23:02:58',1),(2,4,'Sample Task','FINAL',1,'2012-01-29 18:03:00','2012-01-29 18:05:45','2012-01-29 23:05:45',0),(3,14,'PI Approval','FINAL',1,'2012-01-29 18:03:01','2012-01-29 18:03:42','2012-01-29 23:03:42',0),(4,10,'DA Approval XXX','FINAL',1,'2012-01-29 18:03:01','2012-01-29 18:03:42','2012-01-29 23:03:42',0),(5,15,'Quote Job','FINAL',1,'2012-01-29 18:03:01','2012-01-29 18:03:42','2012-01-29 23:03:42',0),(6,1,'Bone Shaker Approval','FINAL',1,'2012-01-29 18:03:01','2012-01-29 18:03:42','2012-01-29 23:03:42',0),(7,16,'Receive Sample 2','FINAL',2,'2012-01-29 18:03:02','2012-01-29 18:03:42','2012-01-29 23:03:42',0),(8,17,'Send Invoice','FINAL',1,'2012-01-29 18:03:42','2012-01-29 18:03:43','2012-01-29 23:03:43',0),(9,6,'Wait for All Job Sample Task','CREATED',1,'2012-01-29 18:03:44',NULL,'2012-01-29 23:03:44',0),(10,8,'Create Library 2','FINAL',2,'2012-01-29 18:04:03','2012-01-29 18:05:44','2012-01-29 23:05:44',0),(11,4,'Sample Task','CREATED',1,'2012-01-29 18:05:45',NULL,'2012-01-29 23:05:45',0),(12,16,'Receive Sample 11','FINAL',11,'2012-01-29 18:05:46','2012-01-29 18:07:26','2012-01-29 23:07:26',0),(13,7,'Assign Library To Platform Unit 11','CREATED',11,'2012-01-29 18:07:27',NULL,'2012-01-29 23:07:27',0),(1010,4,'Sample Flowcell Task','CREATED',11,'2012-01-29 18:09:23',NULL,'2012-01-29 23:09:23',1),(1011,16,'Receive Sample 1010','FINAL',1010,'2012-01-29 18:09:24','2012-01-29 18:10:04','2012-01-29 23:10:04',0),(1012,7,'Assign Library To Platform Unit 1010','CREATED',1010,'2012-01-29 18:10:04',NULL,'2012-01-29 23:10:04',0);
/*!40000 ALTER TABLE `state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `statejob`
--

DROP TABLE IF EXISTS `statejob`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `statejob` (
  `statejobid` int(10) NOT NULL AUTO_INCREMENT,
  `stateid` int(10) DEFAULT NULL,
  `jobid` int(10) DEFAULT NULL,
  PRIMARY KEY (`statejobid`),
  KEY `fk_statejob_sid` (`stateid`),
  KEY `fk_statejob_jid` (`jobid`),
  CONSTRAINT `statejob_ibfk_1` FOREIGN KEY (`stateid`) REFERENCES `state` (`stateid`),
  CONSTRAINT `statejob_ibfk_2` FOREIGN KEY (`jobid`) REFERENCES `job` (`jobid`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statejob`
--

LOCK TABLES `statejob` WRITE;
/*!40000 ALTER TABLE `statejob` DISABLE KEYS */;
INSERT INTO `statejob` VALUES (1,1,1),(2,2,1),(3,3,1),(4,4,1),(5,6,1),(6,5,1),(7,7,1),(8,8,1),(9,9,1),(10,10,1),(11,11,1),(12,12,1),(13,13,1);
/*!40000 ALTER TABLE `statejob` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `statemeta`
--

DROP TABLE IF EXISTS `statemeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `statemeta` (
  `statemetaid` int(10) NOT NULL AUTO_INCREMENT,
  `stateid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` varchar(250) DEFAULT NULL,
  `position` int(10) DEFAULT '0',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`statemetaid`),
  UNIQUE KEY `u_statemeta_k_pid` (`k`,`stateid`),
  KEY `fk_statemeta_sid` (`stateid`),
  CONSTRAINT `statemeta_ibfk_1` FOREIGN KEY (`stateid`) REFERENCES `state` (`stateid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statemeta`
--

LOCK TABLES `statemeta` WRITE;
/*!40000 ALTER TABLE `statemeta` DISABLE KEYS */;
/*!40000 ALTER TABLE `statemeta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `staterun`
--

DROP TABLE IF EXISTS `staterun`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `staterun` (
  `staterunid` int(10) NOT NULL AUTO_INCREMENT,
  `stateid` int(10) DEFAULT NULL,
  `runid` int(10) DEFAULT NULL,
  PRIMARY KEY (`staterunid`),
  KEY `fk_staterun_sid` (`stateid`),
  KEY `fk_staterun_rid` (`runid`),
  CONSTRAINT `staterun_ibfk_1` FOREIGN KEY (`stateid`) REFERENCES `state` (`stateid`),
  CONSTRAINT `staterun_ibfk_2` FOREIGN KEY (`runid`) REFERENCES `run` (`runid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staterun`
--

LOCK TABLES `staterun` WRITE;
/*!40000 ALTER TABLE `staterun` DISABLE KEYS */;
/*!40000 ALTER TABLE `staterun` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `staterunlane`
--

DROP TABLE IF EXISTS `staterunlane`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `staterunlane` (
  `staterunlaneid` int(10) NOT NULL AUTO_INCREMENT,
  `stateid` int(10) DEFAULT NULL,
  `runlaneid` int(10) DEFAULT NULL,
  PRIMARY KEY (`staterunlaneid`),
  KEY `fk_staterunlane_sid` (`stateid`),
  KEY `fk_staterunlane_rlid` (`runlaneid`),
  CONSTRAINT `staterunlane_ibfk_1` FOREIGN KEY (`stateid`) REFERENCES `state` (`stateid`),
  CONSTRAINT `staterunlane_ibfk_2` FOREIGN KEY (`runlaneid`) REFERENCES `runlane` (`runlaneid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staterunlane`
--

LOCK TABLES `staterunlane` WRITE;
/*!40000 ALTER TABLE `staterunlane` DISABLE KEYS */;
/*!40000 ALTER TABLE `staterunlane` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `statesample`
--

DROP TABLE IF EXISTS `statesample`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `statesample` (
  `statesampleid` int(10) NOT NULL AUTO_INCREMENT,
  `stateid` int(10) DEFAULT NULL,
  `sampleid` int(10) DEFAULT NULL,
  PRIMARY KEY (`statesampleid`),
  KEY `fk_statesample_sid` (`stateid`),
  KEY `fk_statesample_sampleid` (`sampleid`),
  CONSTRAINT `statesample_ibfk_1` FOREIGN KEY (`stateid`) REFERENCES `state` (`stateid`),
  CONSTRAINT `statesample_ibfk_2` FOREIGN KEY (`sampleid`) REFERENCES `sample` (`sampleid`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `statesample`
--

LOCK TABLES `statesample` WRITE;
/*!40000 ALTER TABLE `statesample` DISABLE KEYS */;
INSERT INTO `statesample` VALUES (1,2,1),(2,7,1),(3,10,1),(4,11,3),(5,12,3),(6,13,3),(8,1010,14),(9,1011,14),(10,1012,14);
/*!40000 ALTER TABLE `statesample` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subtypesample`
--

DROP TABLE IF EXISTS `subtypesample`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subtypesample` (
  `subtypesampleid` int(10) NOT NULL AUTO_INCREMENT,
  `typesampleid` int(10) DEFAULT NULL,
  `iname` varchar(50) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`subtypesampleid`),
  UNIQUE KEY `u_subtypesample_iname` (`iname`),
  KEY `fk_subtypesample_tsid` (`typesampleid`),
  CONSTRAINT `subtypesample_ibfk_1` FOREIGN KEY (`typesampleid`) REFERENCES `typesample` (`typesampleid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subtypesample`
--

LOCK TABLES `subtypesample` WRITE;
/*!40000 ALTER TABLE `subtypesample` DISABLE KEYS */;
INSERT INTO `subtypesample` VALUES (1,1,'chipseqDnaSample','ChIP-seq DNA'),(2,3,'chipseqLibrarySample','ChIP-seq Library'),(3,5,'illuminaFlowcellV3','Illumina Flow Cell Version 3');
/*!40000 ALTER TABLE `subtypesample` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subtypesamplemeta`
--

DROP TABLE IF EXISTS `subtypesamplemeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subtypesamplemeta` (
  `subtypesamplemetaid` int(10) NOT NULL AUTO_INCREMENT,
  `subtypesampleid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` varchar(250) DEFAULT NULL,
  `position` int(10) DEFAULT '0',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`subtypesamplemetaid`),
  UNIQUE KEY `u_subtypesamplemeta_k_sid` (`k`,`subtypesampleid`),
  KEY `fk_subtypesamplemeta_sampleid` (`subtypesampleid`),
  CONSTRAINT `subtypesamplemeta_ibfk_1` FOREIGN KEY (`subtypesampleid`) REFERENCES `subtypesample` (`subtypesampleid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subtypesamplemeta`
--

LOCK TABLES `subtypesamplemeta` WRITE;
/*!40000 ALTER TABLE `subtypesamplemeta` DISABLE KEYS */;
INSERT INTO `subtypesamplemeta` VALUES (1,3,'illuminaFlowcellV3.maxCellNumber','8',1,'2012-01-29 22:50:22',0);
/*!40000 ALTER TABLE `subtypesamplemeta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subtypesampleresourcecategory`
--

DROP TABLE IF EXISTS `subtypesampleresourcecategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subtypesampleresourcecategory` (
  `subtypesampleresourcecategoryid` int(10) NOT NULL AUTO_INCREMENT,
  `subtypesampleid` int(10) DEFAULT NULL,
  `resourcecategoryid` int(10) DEFAULT NULL,
  PRIMARY KEY (`subtypesampleresourcecategoryid`),
  KEY `fk_subtypesampleresourcecategory_stscid` (`subtypesampleid`),
  KEY `fk_subtypesampleresourcecategory_rcid` (`resourcecategoryid`),
  CONSTRAINT `subtypesampleresourcecategory_ibfk_1` FOREIGN KEY (`subtypesampleid`) REFERENCES `subtypesample` (`subtypesampleid`),
  CONSTRAINT `subtypesampleresourcecategory_ibfk_2` FOREIGN KEY (`resourcecategoryid`) REFERENCES `resourcecategory` (`resourcecategoryid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subtypesampleresourcecategory`
--

LOCK TABLES `subtypesampleresourcecategory` WRITE;
/*!40000 ALTER TABLE `subtypesampleresourcecategory` DISABLE KEYS */;
INSERT INTO `subtypesampleresourcecategory` VALUES (1,3,1);
/*!40000 ALTER TABLE `subtypesampleresourcecategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task` (
  `taskid` int(10) NOT NULL AUTO_INCREMENT,
  `iname` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`taskid`),
  UNIQUE KEY `u_task_iname` (`iname`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` VALUES (1,'boneShakerApprovalTask','Bone Shaker Approval'),(2,'boneShakerTask','Bone Shaker Task'),(3,'jobWrapTask','Job Task'),(4,'sampleWrapTask','Sample Task'),(5,'runWrapTask','Run Task'),(6,'waitForAllJobSamples','Wait for All Job Sample Task'),(7,'assignLibraryToPlatformUnit','Assign Library To Platform Unit'),(8,'Create Library','Create Library'),(9,'createRunTask','Create Run'),(10,'DA Approval','DA Approval XXX'),(11,'getSampleResults','Get Sample Results'),(12,'placeAmplicon','Place Amplicon'),(13,'returnAmplicon','Return from Amplicon'),(14,'PI Approval','PI Approval'),(15,'Quote Job','Quote Job'),(16,'Receive Sample','Receive Sample'),(17,'Send Invoice','Send Invoice'),(18,'Start Job','Start Job');
/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `taskmapping`
--

DROP TABLE IF EXISTS `taskmapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `taskmapping` (
  `taskmappingid` int(10) NOT NULL AUTO_INCREMENT,
  `taskid` int(10) NOT NULL,
  `status` varchar(50) NOT NULL,
  `listmap` varchar(255) DEFAULT NULL,
  `detailmap` varchar(255) DEFAULT NULL,
  `permission` varchar(50) NOT NULL,
  `dashboardsortorder` int(10) DEFAULT NULL,
  PRIMARY KEY (`taskmappingid`),
  UNIQUE KEY `u_taskmapping_t_s` (`taskid`,`status`),
  CONSTRAINT `taskmapping_ibfk_1` FOREIGN KEY (`taskid`) REFERENCES `task` (`taskid`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `taskmapping`
--

LOCK TABLES `taskmapping` WRITE;
/*!40000 ALTER TABLE `taskmapping` DISABLE KEYS */;
INSERT INTO `taskmapping` VALUES (13,7,'CREATED','/task/platformunit/list.do','/def.do','hasRole(\'su\')',1),(14,8,'CREATED','/lab/pendinglmapproval/list.do','/def.do','hasRole(\'su\')',1),(15,9,'CREATED','/lab/pendinglmapproval/list.do','/def.do','hasRole(\'su\')',1),(16,10,'CREATED','/department/dapendingtasklist.do','/def.do','hasRole(\'su\')',1),(17,11,'CREATED','/lab/pendinglmapproval/list.do','/def.do','hasRole(\'su\')',1),(18,14,'CREATED','/lab/pendinglmapproval/list.do','/def.do','hasRole(\'su\')',1),(19,15,'CREATED','/task/quoteJob/list.do','/def.do','hasRole(\'su\')',1),(20,16,'CREATED','/lab/pendinglmapproval/list.do','/def.do','hasRole(\'su\')',1);
/*!40000 ALTER TABLE `taskmapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `typeresource`
--

DROP TABLE IF EXISTS `typeresource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `typeresource` (
  `typeresourceid` int(10) NOT NULL AUTO_INCREMENT,
  `iname` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`typeresourceid`),
  UNIQUE KEY `u_typeresource_iname` (`iname`),
  UNIQUE KEY `u_typeresource_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `typeresource`
--

LOCK TABLES `typeresource` WRITE;
/*!40000 ALTER TABLE `typeresource` DISABLE KEYS */;
INSERT INTO `typeresource` VALUES (1,'mps','Massively Parallel DNA Sequencer'),(2,'amplicon','DNA Amplicon'),(3,'aligner','Aligner'),(4,'peakcaller','Peak Caller'),(5,'sanger','Sanger DNA Sequencer');
/*!40000 ALTER TABLE `typeresource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `typesample`
--

DROP TABLE IF EXISTS `typesample`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `typesample` (
  `typesampleid` int(10) NOT NULL AUTO_INCREMENT,
  `typesamplecategoryid` int(10) DEFAULT NULL,
  `iname` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`typesampleid`),
  UNIQUE KEY `u_typesample_iname` (`iname`),
  UNIQUE KEY `u_typesample_name` (`name`),
  KEY `fk_typesample_tscid` (`typesamplecategoryid`),
  CONSTRAINT `typesample_ibfk_1` FOREIGN KEY (`typesamplecategoryid`) REFERENCES `typesamplecategory` (`typesamplecategoryid`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `typesample`
--

LOCK TABLES `typesample` WRITE;
/*!40000 ALTER TABLE `typesample` DISABLE KEYS */;
INSERT INTO `typesample` VALUES (1,1,'dna','DNA'),(2,1,'rna','RNA'),(3,1,'library','Library'),(4,2,'cell','Cell'),(5,2,'platformunit','Platform Unit'),(6,1,'tissue','Tissue'),(7,1,'protein','Protein'),(8,1,'cellPrimary','Primary Cell'),(9,1,'cellLine','Cell Line');
/*!40000 ALTER TABLE `typesample` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `typesamplecategory`
--

DROP TABLE IF EXISTS `typesamplecategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `typesamplecategory` (
  `typesamplecategoryid` int(10) NOT NULL AUTO_INCREMENT,
  `iname` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`typesamplecategoryid`),
  UNIQUE KEY `u_typesamplecategory_iname` (`iname`),
  UNIQUE KEY `u_typesamplecategory_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `typesamplecategory`
--

LOCK TABLES `typesamplecategory` WRITE;
/*!40000 ALTER TABLE `typesamplecategory` DISABLE KEYS */;
INSERT INTO `typesamplecategory` VALUES (1,'biomaterial','Biomaterial'),(2,'hardware','Hardware');
/*!40000 ALTER TABLE `typesamplecategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `uifield`
--

DROP TABLE IF EXISTS `uifield`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `uifield` (
  `uifieldid` int(10) NOT NULL AUTO_INCREMENT,
  `locale` varchar(5) DEFAULT NULL,
  `area` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `attrname` varchar(50) DEFAULT NULL,
  `attrvalue` text,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  PRIMARY KEY (`uifieldid`),
  UNIQUE KEY `u_uifield_laaa` (`locale`,`area`,`name`,`attrname`)
) ENGINE=InnoDB AUTO_INCREMENT=1055 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `uifield`
--

LOCK TABLES `uifield` WRITE;
/*!40000 ALTER TABLE `uifield` DISABLE KEYS */;
INSERT INTO `uifield` VALUES (1,'en_US','user','login','label','Login',NULL,1),(2,'en_US','user','login','error','Login cannot be empty',NULL,1),(3,'en_US','user','password','label','Password',NULL,1),(4,'en_US','user','password','error','Password cannot be empty',NULL,1),(5,'en_US','user','firstName','label','First Name',NULL,1),(6,'en_US','user','firstName','error','First Name cannot be empty',NULL,1),(7,'en_US','user','lastName','label','Last Name',NULL,1),(8,'en_US','user','lastName','error','Last Name cannot be empty',NULL,1),(9,'en_US','user','email','label','Email',NULL,1),(10,'en_US','user','email','error','Wrong email address format',NULL,1),(11,'en_US','user','email_exists','error','Email already exists in the database',NULL,1),(12,'en_US','user','locale','label','Locale',NULL,1),(13,'en_US','user','locale','error','Locale cannot be empty',NULL,1),(14,'en_US','user','isActive','label','Active',NULL,1),(15,'en_US','user','title','label','Title',NULL,1),(16,'en_US','user','title','error','Title cannot be empty',NULL,1),(17,'en_US','user','title','constraint','NotEmpty',NULL,1),(18,'en_US','user','title','control','select:Prof:Prof;Dr:Dr;Mr:Mr;Ms:Ms;Mrs:Mrs;Miss:Miss',NULL,1),(19,'en_US','user','title','metaposition','1',NULL,1),(20,'en_US','user','institution','label','Institution',NULL,1),(21,'en_US','user','institution','error','Institution cannot be empty',NULL,1),(22,'en_US','user','institution','constraint','NotEmpty',NULL,1),(23,'en_US','user','institution','metaposition','10',NULL,1),(24,'en_US','user','departmentId','label','Department',NULL,1),(25,'en_US','user','departmentId','metaposition','20',NULL,1),(26,'en_US','user','building_room','label','Room',NULL,1),(27,'en_US','user','building_room','metaposition','30',NULL,1),(28,'en_US','user','building_room','error','Room cannot be empty',NULL,1),(29,'en_US','user','building_room','constraint','NotEmpty',NULL,1),(30,'en_US','user','address','label','Address',NULL,1),(31,'en_US','user','address','metaposition','40',NULL,1),(32,'en_US','user','city','label','City',NULL,1),(33,'en_US','user','city','error','City cannot be empty',NULL,1),(34,'en_US','user','city','constraint','NotEmpty',NULL,1),(35,'en_US','user','city','metaposition','50',NULL,1),(36,'en_US','user','state','label','State',NULL,1),(37,'en_US','user','state','error','State cannot be empty',NULL,1),(38,'en_US','user','state','control','select:${states}:code:name',NULL,1),(39,'en_US','user','state','constraint','NotEmpty',NULL,1),(40,'en_US','user','state','metaposition','60',NULL,1),(41,'en_US','user','country','label','Country',NULL,1),(42,'en_US','user','country','error','Country cannot be empty',NULL,1),(43,'en_US','user','country','control','select:${countries}:code:name',NULL,1),(44,'en_US','user','country','constraint','NotEmpty',NULL,1),(45,'en_US','user','country','metaposition','70',NULL,1),(46,'en_US','user','zip','label','Zip',NULL,1),(47,'en_US','user','zip','error','Zip cannot be empty',NULL,1),(48,'en_US','user','zip','metaposition','80',NULL,1),(49,'en_US','user','phone','label','Phone',NULL,1),(50,'en_US','user','phone','error','Phone cannot be empty',NULL,1),(51,'en_US','user','phone','constraint','NotEmpty',NULL,1),(52,'en_US','user','phone','metaposition','90',NULL,1),(53,'en_US','user','fax','label','Fax',NULL,1),(54,'en_US','user','fax','metaposition','100',NULL,1),(55,'en_US','user','labusers','label','Lab Users',NULL,1),(56,'en_US','user','samples','label','Samples',NULL,1),(57,'en_US','user','jobs','label','Jobs',NULL,1),(58,'en_US','user','login_exists','error','Login name already exists in the database',NULL,1),(59,'en_US','user','email_changed_p1','label','Your email address has changed. An email has been sent to your new email address requesting confirmation. Please confirm by clicking the link in the email then <a href=\"../login.do\"/>click here to login</a>',NULL,1),(60,'en_US','user','email_changed_p2','label','If you have not received an email, requesting confirmation, within a reasonable time period and suspect your email address may have been mis-typed, you may reset your email address by clicking <a href=\"requestEmailChange.do\">here</a>',NULL,1),(61,'en_US','user','email_change_confirmed','label','Your email address change has been confirmed. Please click to <a href=\"../login.do\"/>Login</a>',NULL,1),(62,'en_US','user','userlogin_changed_p1','label','Your login has changed. Please <a href=\"../login.do\"/>click here to login</a>',NULL,1),(63,'en_US','user','userloginandemail_changed_p1','label','Your login and email address has changed. An email has been sent to your new email address requesting confirmation. Please confirm by clicking the link in the email then <a href=\"../login.do\"/>click here to login</a>',NULL,1),(64,'en_US','user','userloginandemail_changed_p2','label','If you have not received an email, requesting confirmation, within a reasonable time period and suspect your email address may have been mis-typed, you may reset your email address by clicking <a href=\"requestEmailChange.do\">here</a>',NULL,1),(65,'en_US','userPending','login','label','Login',NULL,1),(66,'en_US','userPending','login','error','Login cannot be empty',NULL,1),(67,'en_US','userPending','page','label','New User',NULL,1),(68,'en_US','userPending','password','label','Password',NULL,1),(69,'en_US','userPending','password','error','Password cannot be empty',NULL,1),(70,'en_US','userPending','password_mismatch','error','The two entries for your password are NOT identical',NULL,1),(71,'en_US','userPending','password_invalid','error','Password must be at least 8 characters, containing only letters and numbers, with at least one letter and number',NULL,1),(72,'en_US','userPending','password2','label','Re-confirm Password',NULL,1),(73,'en_US','userPending','password2','error','Re-confirm password cannot be empty',NULL,1),(74,'en_US','userPending','firstName','label','First Name',NULL,1),(75,'en_US','userPending','firstName','error','First Name cannot be empty',NULL,1),(76,'en_US','userPending','lastName','label','Last Name',NULL,1),(77,'en_US','userPending','lastName','error','Last Name cannot be empty',NULL,1),(78,'en_US','userPending','email','label','Email',NULL,1),(79,'en_US','userPending','email','error','Must be correctly formatted',NULL,1),(80,'en_US','userPending','email_exists','error','Email already exists in the database',NULL,1),(81,'en_US','userPending','login_exists','error','Login name already exists in the database',NULL,1),(82,'en_US','userPending','login_malformed','error','Contains invalid characters',NULL,1),(83,'en_US','userPending','locale','label','Locale',NULL,1),(84,'en_US','userPending','locale','error','A locale must be selected',NULL,1),(85,'en_US','userPending','primaryuserid','label','PI Wasp Username',NULL,1),(86,'en_US','userPending','primaryuserid','error','PI Wasp Username cannot be empty',NULL,1),(87,'en_US','userPending','primaryuserid_notvalid','error','Not an active registered PI Username',NULL,1),(88,'en_US','userPending','primaryuserid','constraint','isValidPiId',NULL,1),(89,'en_US','userPending','primaryuserid','metaposition','1',NULL,1),(90,'en_US','userPending','title','label','Title',NULL,1),(91,'en_US','userPending','title','error','Title cannot be empty',NULL,1),(92,'en_US','userPending','title','constraint','NotEmpty',NULL,1),(93,'en_US','userPending','title','control','select:Prof:Prof;Dr:Dr;Mr:Mr;Ms:Ms;Mrs:Mrs;Miss:Miss',NULL,1),(94,'en_US','userPending','title','metaposition','5',NULL,1),(95,'en_US','userPending','building_room','label','Building / Room',NULL,1),(96,'en_US','userPending','building_room','metaposition','30',NULL,1),(97,'en_US','userPending','address','label','Street',NULL,1),(98,'en_US','userPending','address','metaposition','40',NULL,1),(99,'en_US','userPending','phone','label','Phone',NULL,1),(100,'en_US','userPending','phone','error','Phone cannot be empty',NULL,1),(101,'en_US','userPending','phone','constraint','NotEmpty',NULL,1),(102,'en_US','userPending','phone','metaposition','90',NULL,1),(103,'en_US','userPending','fax','label','Fax',NULL,1),(104,'en_US','userPending','fax','metaposition','100',NULL,1),(105,'en_US','userPending','approved','label','User account application sucessfully approved',NULL,1),(106,'en_US','userPending','rejected','label','User account application sucessfully rejected',NULL,1),(107,'en_US','userPending','status_not_pending','error','Pending user is already approved or rejected',NULL,1),(108,'en_US','userPending','no_pending_users','label','There are currently no pending users awaiting approval',NULL,1),(109,'en_US','userPending','external_authentication','error','Authentication Failed (Login Name or Password incorrect)',NULL,1),(110,'en_US','piPending','login','label','Login',NULL,1),(111,'en_US','piPending','login','error','Login cannot be empty',NULL,1),(112,'en_US','piPending','password','label','Password',NULL,1),(113,'en_US','piPending','password','error','Password cannot be empty',NULL,1),(114,'en_US','piPending','password_mismatch','error','The two entries for your password are NOT identical',NULL,1),(115,'en_US','piPending','password_invalid','error','Password must be at least 8 characters, containing only letters and numbers, with at least one letter and number',NULL,1),(116,'en_US','piPending','password2','label','Re-confirm Password',NULL,1),(117,'en_US','piPending','password2','error','Re-confirm password cannot be empty',NULL,1),(118,'en_US','piPending','firstName','label','First Name',NULL,1),(119,'en_US','piPending','firstName','error','First Name cannot be empty',NULL,1),(120,'en_US','piPending','lastName','label','Last Name',NULL,1),(121,'en_US','piPending','lastName','error','Last Name cannot be empty',NULL,1),(122,'en_US','piPending','email','label','Email',NULL,1),(123,'en_US','piPending','email','error','Wrong email address format',NULL,1),(124,'en_US','piPending','email_exists','error','Email already exists in the database',NULL,1),(125,'en_US','piPending','locale','label','Locale',NULL,1),(126,'en_US','piPending','locale','error','Locale cannot be empty',NULL,1),(127,'en_US','piPending','labName','label','Lab Name',NULL,1),(128,'en_US','piPending','labName','error','Lab Name cannot be empty',NULL,1),(129,'en_US','piPending','labName','constraint','NotEmpty',NULL,1),(130,'en_US','piPending','labName','metaposition','1',NULL,1),(131,'en_US','piPending','title','label','Title',NULL,1),(132,'en_US','piPending','title','error','Title cannot be empty',NULL,1),(133,'en_US','piPending','title','constraint','NotEmpty',NULL,1),(134,'en_US','piPending','title','control','select:Prof:Prof;Dr:Dr;Mr:Mr;Ms:Ms;Mrs:Mrs;Miss:Miss',NULL,1),(135,'en_US','piPending','title','metaposition','5',NULL,1),(136,'en_US','piPending','institution','label','Institution',NULL,1),(137,'en_US','piPending','institution','error','Institution cannot be empty',NULL,1),(138,'en_US','piPending','institution','constraint','NotEmpty',NULL,1),(139,'en_US','piPending','institution','metaposition','10',NULL,1),(140,'en_US','piPending','departmentId','error','Department cannot be empty',NULL,1),(141,'en_US','piPending','departmentId','label','Department',NULL,1),(142,'en_US','piPending','departmentId','constraint','NotEmpty',NULL,1),(143,'en_US','piPending','departmentId','control','select:${departments}:departmentId:name',NULL,1),(144,'en_US','piPending','departmentId','metaposition','20',NULL,1),(145,'en_US','piPending','building_room','label','Building / Room',NULL,1),(146,'en_US','piPending','building_room','metaposition','30',NULL,1),(147,'en_US','piPending','address','label','Address',NULL,1),(148,'en_US','piPending','address','error','Address cannot be empty',NULL,1),(149,'en_US','piPending','address','constraint','NotEmpty',NULL,1),(150,'en_US','piPending','address','metaposition','40',NULL,1),(151,'en_US','piPending','city','label','City',NULL,1),(152,'en_US','piPending','city','error','City cannot be empty',NULL,1),(153,'en_US','piPending','city','constraint','NotEmpty',NULL,1),(154,'en_US','piPending','city','metaposition','50',NULL,1),(155,'en_US','piPending','state','label','State',NULL,1),(156,'en_US','piPending','state','control','select:${states}:code:name',NULL,1),(157,'en_US','piPending','state','metaposition','60',NULL,1),(158,'en_US','piPending','country','label','Country',NULL,1),(159,'en_US','piPending','country','error','Country cannot be empty',NULL,1),(160,'en_US','piPending','country','control','select:${countries}:code:name',NULL,1),(161,'en_US','piPending','country','constraint','NotEmpty',NULL,1),(162,'en_US','piPending','country','metaposition','70',NULL,1),(163,'en_US','piPending','zip','label','Zip',NULL,1),(164,'en_US','piPending','zip','error','Zip cannot be empty',NULL,1),(165,'en_US','piPending','zip','constraint','NotEmpty',NULL,1),(166,'en_US','piPending','zip','metaposition','80',NULL,1),(167,'en_US','piPending','phone','label','Phone',NULL,1),(168,'en_US','piPending','phone','error','Phone cannot be empty',NULL,1),(169,'en_US','piPending','phone','constraint','NotEmpty',NULL,1),(170,'en_US','piPending','phone','metaposition','90',NULL,1),(171,'en_US','piPending','fax','label','Fax',NULL,1),(172,'en_US','piPending','fax','metaposition','100',NULL,1),(173,'en_US','piPending','select_institute_message','label','Please select your institute or choose \"other\"',NULL,1),(174,'en_US','piPending','select_institute_submit','label','Submit',NULL,1),(175,'en_US','piPending','select_institute','label','Institute',NULL,1),(176,'en_US','piPending','other_institute','label','If \'Other\' Please specify',NULL,1),(177,'en_US','piPending','internal_institute_list','data','Einstein;Montifiore',NULL,1),(178,'en_US','piPending','institute_not_selected','error','You must select an institute',NULL,1),(179,'en_US','piPending','institute_multi_select','error','You cannot select more than one institute',NULL,1),(180,'en_US','lab','name','label','Lab Name',NULL,1),(181,'en_US','lab','name','error','Lab Name cannot be empty',NULL,1),(182,'en_US','lab','primaryUserId','label','Primary User',NULL,1),(183,'en_US','lab','primaryUserId','error','Please select Primary User',NULL,1),(184,'en_US','lab','departmentId','label','Department',NULL,1),(185,'en_US','lab','departmentId','error','Please select department',NULL,1),(186,'en_US','lab','isActive','label','Active',NULL,1),(187,'en_US','lab','internal_external_lab','label','External/Internal',NULL,1),(188,'en_US','lab','internal_external_lab','error','Please specify lab type (External/Internal)',NULL,1),(189,'en_US','lab','internal_external_lab','control','select:external:External;internal:Internal',NULL,1),(190,'en_US','lab','internal_external_lab','constraint','NotEmpty',NULL,1),(191,'en_US','lab','internal_external_lab','metaposition','10',NULL,1),(192,'en_US','lab','phone','label','Phone',NULL,1),(193,'en_US','lab','phone','error','Phone cannot be empty',NULL,1),(194,'en_US','lab','phone','constraint','NotEmpty',NULL,1),(195,'en_US','lab','phone','metaposition','20',NULL,1),(196,'en_US','lab','building_room','label','Room',NULL,1),(197,'en_US','lab','building_room','metaposition','30',NULL,1),(198,'en_US','lab','billing_contact','label','Billing Contact',NULL,1),(199,'en_US','lab','billing_contact','error','Billing Contact cannot be empty',NULL,1),(200,'en_US','lab','billing_contact','constraint','NotEmpty',NULL,1),(201,'en_US','lab','billing_contact','metaposition','40',NULL,1),(202,'en_US','lab','billing_institution','label','Billing Institution',NULL,1),(203,'en_US','lab','billing_institution','error','Institution cannot be empty',NULL,1),(204,'en_US','lab','billing_institution','constraint','NotEmpty',NULL,1),(205,'en_US','lab','billing_institution','metaposition','50',NULL,1),(206,'en_US','lab','billing_departmentId','error','Billing Department cannot be empty',NULL,1),(207,'en_US','lab','billing_departmentId','label','Billing Department',NULL,1),(208,'en_US','lab','billing_departmentId','constraint','NotEmpty',NULL,1),(209,'en_US','lab','billing_departmentId','metaposition','60',NULL,1),(210,'en_US','lab','billing_departmentId','control','select:${departments}:departmentId:name',NULL,1),(211,'en_US','lab','billing_building_room','label','Room',NULL,1),(212,'en_US','lab','billing_building_room','metaposition','70',NULL,1),(213,'en_US','lab','billing_address','label','Billing Address',NULL,1),(214,'en_US','lab','billing_address','error','Billing Address cannot be empty',NULL,1),(215,'en_US','lab','billing_address','constraint','NotEmpty',NULL,1),(216,'en_US','lab','billing_address','metaposition','80',NULL,1),(217,'en_US','lab','billing_city','label','Billing City',NULL,1),(218,'en_US','lab','billing_city','error','Billing City cannot be empty',NULL,1),(219,'en_US','lab','billing_city','constraint','NotEmpty',NULL,1),(220,'en_US','lab','billing_city','metaposition','90',NULL,1),(221,'en_US','lab','billing_state','label','Billing State',NULL,1),(222,'en_US','lab','billing_state','control','select:${states}:code:name',NULL,1),(223,'en_US','lab','billing_state','metaposition','100',NULL,1),(224,'en_US','lab','billing_country','label','Billing Country',NULL,1),(225,'en_US','lab','billing_country','error','Billing Country cannot be empty',NULL,1),(226,'en_US','lab','billing_country','control','select:${countries}:code:name',NULL,1),(227,'en_US','lab','billing_country','constraint','NotEmpty',NULL,1),(228,'en_US','lab','billing_country','metaposition','110',NULL,1),(229,'en_US','lab','billing_zip','label','Billing Zip',NULL,1),(230,'en_US','lab','billing_zip','error','Billing Zip cannot be empty',NULL,1),(231,'en_US','lab','billing_zip','constraint','NotEmpty',NULL,1),(232,'en_US','lab','billing_zip','metaposition','120',NULL,1),(233,'en_US','lab','billing_phone','label','Billing Phone',NULL,1),(234,'en_US','lab','billing_phone','error','Billing Phone cannot be empty',NULL,1),(235,'en_US','lab','billing_phone','constraint','NotEmpty',NULL,1),(236,'en_US','lab','billing_phone','metaposition','130',NULL,1),(237,'en_US','lab','updated_success','label','Lab was updated',NULL,1),(238,'en_US','lab','created_success','label','Lab was created',NULL,1),(239,'en_US','lab','updated','error','Lab was NOT updated. Please fill in required fields.',NULL,1),(240,'en_US','lab','created','error','Lab was NOT created. Please fill in required fields.',NULL,1),(241,'en_US','lab','list_pi','label','Principal Investigator',NULL,1),(242,'en_US','lab','list_users','label','Lab Members',NULL,1),(243,'en_US','user','updated_success','label','User was updated',NULL,1),(244,'en_US','user','created_success','label','User was created',NULL,1),(245,'en_US','user','updated','error','User was NOT updated. Please fill in required fields.',NULL,1),(246,'en_US','user','created','error','User was NOT created. Please fill in required fields.',NULL,1),(247,'en_US','user','updated_fail','error','User was NOT updated.',NULL,1),(248,'en_US','labuser','current','label','Current Lab Members',NULL,1),(249,'en_US','labuser','request','label','Pending Lab Members',NULL,1),(250,'en_US','labuser','labUserNote','label','Request subject to principal investigator acceptance.',NULL,1),(251,'en_US','labuser','request_primaryuser','label','Primary Investigator',NULL,1),(252,'en_US','labuser','request_primaryuser','error','Invalid Primary User',NULL,1),(253,'en_US','labuser','request_submit','label','Request Access',NULL,1),(254,'en_US','labuser','request_success','label','Your request for access has been submitted.',NULL,1),(255,'en_US','labuser','request_alreadypending','error','You are already a pending user for the requested lab.',NULL,1),(256,'en_US','labuser','request_alreadyaccess','error','You are currently a member of the requested lab.',NULL,1),(257,'en_US','labPending','newPiNote','label','Request subject to WASP administrator confirmation of principal investigator status.',NULL,1),(258,'en_US','labPending','newLabSubmit','label','Submit',NULL,1),(259,'en_US','labPending','createNewLab','label','Create New Lab Request',NULL,1),(260,'en_US','labPending','name','label','Lab Name',NULL,1),(261,'en_US','labPending','name','error','Lab Name cannot be empty',NULL,1),(262,'en_US','labPending','primaryUserId','label','Primary User',NULL,1),(263,'en_US','labPending','primaryUserId','error','Please select Primary User',NULL,1),(264,'en_US','labPending','departmentId','label','Department',NULL,1),(265,'en_US','labPending','departmentId','error','Please select department',NULL,1),(266,'en_US','labPending','internal_external_lab','label','External/Internal',NULL,1),(267,'en_US','labPending','internal_external_lab','error','Please specify lab type (External/Internal)',NULL,1),(268,'en_US','labPending','internal_external_lab','control','select:external:External;internal:Internal',NULL,1),(269,'en_US','labPending','internal_external_lab','constraint','NotEmpty',NULL,1),(270,'en_US','labPending','internal_external_lab','metaposition','10',NULL,1),(271,'en_US','labPending','phone','label','Phone',NULL,1),(272,'en_US','labPending','phone','error','Phone cannot be empty',NULL,1),(273,'en_US','labPending','phone','constraint','NotEmpty',NULL,1),(274,'en_US','labPending','phone','metaposition','20',NULL,1),(275,'en_US','labPending','building_room','label','Room',NULL,1),(276,'en_US','labPending','building_room','metaposition','30',NULL,1),(277,'en_US','labPending','billing_contact','label','Billing Contact',NULL,1),(278,'en_US','labPending','billing_contact','error','Billing Contact cannot be empty',NULL,1),(279,'en_US','labPending','billing_contact','constraint','NotEmpty',NULL,1),(280,'en_US','labPending','billing_contact','metaposition','40',NULL,1),(281,'en_US','labPending','billing_institution','label','Billing Institution',NULL,1),(282,'en_US','labPending','billing_institution','error','Institution cannot be empty',NULL,1),(283,'en_US','labPending','billing_institution','constraint','NotEmpty',NULL,1),(284,'en_US','labPending','billing_institution','metaposition','50',NULL,1),(285,'en_US','labPending','billing_departmentId','error','Billing Department cannot be empty',NULL,1),(286,'en_US','labPending','billing_departmentId','label','Billing Department',NULL,1),(287,'en_US','labPending','billing_departmentId','constraint','NotEmpty',NULL,1),(288,'en_US','labPending','billing_departmentId','metaposition','60',NULL,1),(289,'en_US','labPending','billing_departmentId','control','select:${departments}:departmentId:name',NULL,1),(290,'en_US','labPending','billing_building_room','label','Room',NULL,1),(291,'en_US','labPending','billing_building_room','metaposition','70',NULL,1),(292,'en_US','labPending','billing_address','label','Billing Address',NULL,1),(293,'en_US','labPending','billing_address','error','Billing Address cannot be empty',NULL,1),(294,'en_US','labPending','billing_address','constraint','NotEmpty',NULL,1),(295,'en_US','labPending','billing_address','metaposition','80',NULL,1),(296,'en_US','labPending','billing_city','label','Billing City',NULL,1),(297,'en_US','labPending','billing_city','error','Billing City cannot be empty',NULL,1),(298,'en_US','labPending','billing_city','constraint','NotEmpty',NULL,1),(299,'en_US','labPending','billing_city','metaposition','90',NULL,1),(300,'en_US','labPending','billing_state','label','Billing State',NULL,1),(301,'en_US','labPending','billing_state','control','select:${states}:code:name',NULL,1),(302,'en_US','labPending','billing_state','metaposition','100',NULL,1),(303,'en_US','labPending','billing_country','label','Billing Country',NULL,1),(304,'en_US','labPending','billing_country','error','Billing Country cannot be empty',NULL,1),(305,'en_US','labPending','billing_country','control','select:${countries}:code:name',NULL,1),(306,'en_US','labPending','billing_country','constraint','NotEmpty',NULL,1),(307,'en_US','labPending','billing_country','metaposition','110',NULL,1),(308,'en_US','labPending','billing_zip','label','Billing Zip',NULL,1),(309,'en_US','labPending','billing_zip','error','Billing Zip cannot be empty',NULL,1),(310,'en_US','labPending','billing_zip','constraint','NotEmpty',NULL,1),(311,'en_US','labPending','billing_zip','metaposition','120',NULL,1),(312,'en_US','labPending','billing_phone','label','Billing Phone',NULL,1),(313,'en_US','labPending','billing_phone','error','Billing Phone cannot be empty',NULL,1),(314,'en_US','labPending','billing_phone','constraint','NotEmpty',NULL,1),(315,'en_US','labPending','billing_phone','metaposition','130',NULL,1),(316,'en_US','labPending','updated','error','Pending Lab was NOT updated. Please fill in required fields.',NULL,1),(317,'en_US','labPending','created','error','Pending Lab was NOT created. Please fill in required fields.',NULL,1),(318,'en_US','labPending','status_not_pending','error','Pending lab is already approved or rejected',NULL,1),(319,'en_US','labPending','updated_success','label','Pending Lab updated sucessfully.',NULL,1),(320,'en_US','labPending','action','error','Invalid action. Must be approve or reject only',NULL,1),(321,'en_US','labPending','departmentid_mismatch','error','Deparment id mismatch with lab-pending id',NULL,1),(322,'en_US','labPending','status_mismatch','error','Status must be pending with lab-pending id',NULL,1),(323,'en_US','labPending','labpendingid_notexist','error','Lab-pending id does not exist',NULL,1),(324,'en_US','labPending','save','label','Save Changes',NULL,1),(325,'en_US','labPending','cancel','label','Cancel',NULL,1),(326,'en_US','labPending','heading','label','Pending Lab Details:',NULL,1),(327,'en_US','labPending','approved','label','New lab application sucessfully approved',NULL,1),(328,'en_US','labPending','rejected','label','New lab application sucessfully rejected',NULL,1),(329,'en_US','labPending','could_not_create_lab','error','Failed to process new lab request',NULL,1),(330,'en_US','user','auth_login_validate','error','Please provide your user login name AND password.',NULL,1),(331,'en_US','auth','login_failed','error','Your login attempt was not successful. Try again.',NULL,1),(332,'en_US','auth','login_reason','label','Reason',NULL,1),(333,'en_US','auth','login_user','label','User',NULL,1),(334,'en_US','auth','login_password','label','Password',NULL,1),(335,'en_US','auth','login','data','Login',NULL,1),(336,'en_US','auth','login_anchor_forgotpass','label','Forgot Password',NULL,1),(337,'en_US','auth','login_anchor_newuser','label','New User',NULL,1),(338,'en_US','auth','login_anchor_newpi','label','New PI',NULL,1),(339,'en_US','auth','login_anchor_about','label','About',NULL,1),(340,'en_US','auth','resetpasswordRequest_user','label','Username',NULL,1),(341,'en_US','auth','resetpasswordRequest','data','Submit',NULL,1),(342,'en_US','auth','resetpasswordRequest_missingparam','error','Please provide values for all fields',NULL,1),(343,'en_US','auth','resetpasswordRequest_captcha','error','Captcha text incorrect',NULL,1),(344,'en_US','auth','resetpasswordRequest_username','error','A user with the supplied username does not exist',NULL,1),(345,'en_US','auth','resetpasswordRequest_captcha','label','Captcha text',NULL,1),(346,'en_US','auth','resetpassword_user','label','Username',NULL,1),(347,'en_US','auth','resetpassword_authcode','label','Auth Code',NULL,1),(348,'en_US','auth','resetpassword_password1','label','New Password',NULL,1),(349,'en_US','auth','resetpassword_password2','label','Confirm New Password',NULL,1),(350,'en_US','auth','resetpassword_captcha','label','Captcha text',NULL,1),(351,'en_US','auth','resetpassword','data','Submit',NULL,1),(352,'en_US','auth','resetpassword_noauthcode','error','No authorization code provided',NULL,1),(353,'en_US','auth','resetpassword_missingparam','error','Please provide values for all fields',NULL,1),(354,'en_US','auth','resetpassword_badauthcode','error','Invalid authorization code provided',NULL,1),(355,'en_US','auth','resetpassword_captcha','error','Captcha text incorrect',NULL,1),(356,'en_US','auth','resetpassword_username','error','A user with the supplied username does not exist',NULL,1),(357,'en_US','auth','resetpassword_wronguser','error','Username and authorization code provided do not match',NULL,1),(358,'en_US','auth','resetpassword_new_mismatch','error','The two entries for your NEW password are NOT identical',NULL,1),(359,'en_US','auth','resetpassword_new_invalid','error','New password must be at least 8 characters, containing only letters and numbers, with at least one letter and number',NULL,1),(360,'en_US','auth','resetpassword_instructions','label','New Password Requirements:<br />At least 8 characters in length<br />Only letters & numbers permitted (no spaces, etc)<br />At least one letter and one number<br />',NULL,1),(361,'en_US','auth','resetpasswordok_title','label','Reset Password: Complete',NULL,1),(362,'en_US','auth','resetpasswordok','label','Your password has been reset. Please click to <a href=\"../login.do\"/>Login</a>',NULL,1),(363,'en_US','user','mypassword_instructions','label','New Password Requirements:<br />At least 8 characters in length<br />Only letters & numbers permitted (no spaces, etc)<br />At least one letter and one number<br />',NULL,1),(364,'en_US','user','mypassword_oldpassword','label','Old Password',NULL,1),(365,'en_US','user','mypassword_newpassword1','label','New Password',NULL,1),(366,'en_US','user','mypassword_newpassword2','label','Confirm New Password',NULL,1),(367,'en_US','user','mypassword_cannotChange','error','Externally authenticated user cannot change password in WASP',NULL,1),(368,'en_US','user','mypassword','data','Submit',NULL,1),(369,'en_US','user','mypassword_missingparam','error','Please provide values for all fields',NULL,1),(370,'en_US','user','mypassword_cur_mismatch','error','Your old password does NOT match the password in our database',NULL,1),(371,'en_US','user','mypassword_new_mismatch','error','The two entries for your NEW password are NOT identical',NULL,1),(372,'en_US','user','mypassword_nodiff','error','Your old and new passwords may NOT be the same',NULL,1),(373,'en_US','user','mypassword_new_invalid','error','New password must be at least 8 characters, containing only letters and numbers, with at least one letter and number',NULL,1),(374,'en_US','user','mypassword_ok','label','Password Has Been Changed',NULL,1),(375,'en_US','auth','confirmemail_badauthcode','error','Invalid authorization code provided',NULL,1),(376,'en_US','auth','confirmemail_corruptemail','error','email address in url cannot be decoded',NULL,1),(377,'en_US','auth','confirmemail_bademail','error','email address is incorrect',NULL,1),(378,'en_US','auth','confirmemail_wronguser','error','User email address and authorization code provided do not match',NULL,1),(379,'en_US','auth','confirmemail_captcha','error','Captcha text incorrect',NULL,1),(380,'en_US','auth','confirmemail_email','label','Email Address',NULL,1),(381,'en_US','auth','confirmemail_authcode','label','Auth code',NULL,1),(382,'en_US','auth','confirmemail_captcha','label','Captcha text',NULL,1),(383,'en_US','auth','confirmemail','data','Submit',NULL,1),(384,'en_US','auth','requestEmailChange_email','label','New Email Address',NULL,1),(385,'en_US','auth','requestEmailChange_submit','label','Submit',NULL,1),(386,'en_US','auth','requestEmailChange_bademail','error','email address is of incorrect format',NULL,1),(387,'en_US','auth','requestEmailChange_captcha','error','Captcha text incorrect',NULL,1),(388,'en_US','auth','requestEmailChange_badcredentials','error','Failed to authenticate with supplied login credentials',NULL,1),(389,'en_US','department','list_create','label','Create Department',NULL,1),(390,'en_US','department','list_department','label','Department Name',NULL,1),(391,'en_US','department','list','data','Submit',NULL,1),(392,'en_US','department','list_invalid','error','Invalid department name',NULL,1),(393,'en_US','department','list_missingparam','error','Please provide a department name',NULL,1),(394,'en_US','department','list_department_exists','error','Department already exists',NULL,1),(395,'en_US','department','list_ok','label','New department has been created',NULL,1),(396,'en_US','jobDraft','name','label','Job Name',NULL,1),(397,'en_US','jobDraft','name','error','Job Name Must Not Be Empty',NULL,1),(398,'en_US','jobDraft','name_exists','error','Job name chosen already exists',NULL,1),(399,'en_US','jobDraft','changeResource','error','You must select a job resource',NULL,1),(400,'en_US','jobDraft','labId','label','Lab',NULL,1),(401,'en_US','jobDraft','labId','error','Lab Must Not Be Empty',NULL,1),(402,'en_US','jobDraft','workflowId','label','Assay Workflow',NULL,1),(403,'en_US','jobDraft','workflowId','error','You must select an Assay Workflow',NULL,1),(404,'en_US','jobDraft','instructions','label','To create a job, please provide a name, select the lab from which you are submitting and choose the assay workflow most suited to your experiment.',NULL,1),(405,'en_US','jobDraft','page_footer','label','',NULL,1),(406,'en_US','ampliconSeq','samples','data','samplefile;ampliconSample',NULL,1),(407,'en_US','ampliconSeq','platform','label','Platform',NULL,1),(408,'en_US','ampliconSeq','platform','constraint','NotEmpty',NULL,1),(409,'en_US','ampliconSeq','platform','error','Platform cannot be empty',NULL,1),(410,'en_US','ampliconSeq','platform','control','select:HISeq2000:HISeq2000',NULL,1),(411,'en_US','ampliconSeq','platform','metaposition','100',NULL,1),(412,'en_US','ampliconSeq','readlength','label','Read Length',NULL,1),(413,'en_US','ampliconSeq','readlength','constraint','NotEmpty',NULL,1),(414,'en_US','ampliconSeq','readlength','error','readlength cannot be empty',NULL,1),(415,'en_US','ampliconSeq','readlength','control','select:100:100 bp; 150: 150bp',NULL,1),(416,'en_US','ampliconSeq','readlength','metaposition','110',NULL,1),(417,'en_US','ampliconSeq','readtype','label','Read Type',NULL,1),(418,'en_US','ampliconSeq','readtype','constraint','NotEmpty',NULL,1),(419,'en_US','ampliconSeq','readtype','error','readtype cannot be empty',NULL,1),(420,'en_US','ampliconSeq','readtype','control','select:single:Single-End Read; pair:Pair-End Read',NULL,1),(421,'en_US','ampliconSeq','readtype','metaposition','120',NULL,1),(422,'en_US','ampliconSeq','antibody','label','Anti Body',NULL,1),(423,'en_US','ampliconSeq','antibody','constraint','NotEmpty',NULL,1),(424,'en_US','ampliconSeq','antibody','error','antibody cannot be empty',NULL,1),(425,'en_US','ampliconSeq','antibody','control','select:abc:abc;def:def;ghi:ghi',NULL,1),(426,'en_US','ampliconSeq','antibody','metaposition','130',NULL,1),(427,'en_US','ampliconSeq','pcrprimers','label','PCR Primers',NULL,1),(428,'en_US','ampliconSeq','pcrprimers','constraint','NotEmpty',NULL,1),(429,'en_US','ampliconSeq','pcrprimers','error','pcrprimers cannot be empty',NULL,1),(430,'en_US','ampliconSeq','pcrprimers','control','select:xyz:xyz',NULL,1),(431,'en_US','ampliconSeq','pcrprimers','metaposition','140',NULL,1),(432,'en_US','sampleDraft','name','label','Name',NULL,1),(433,'en_US','sampleDraft','name','constraint','NotEmpty',NULL,1),(434,'en_US','sampleDraft','name','error','Please specify sample name',NULL,1),(435,'en_US','sampleDraft','typeSampleId','label','Type',NULL,1),(436,'en_US','sampleDraft','typeSampleId','constraint','NotEmpty',NULL,1),(437,'en_US','sampleDraft','typeSampleId','error','Please specify type',NULL,1),(438,'en_US','sampleDraft','status','label','Status',NULL,1),(439,'en_US','sampleDraft','status','constraint','NotEmpty',NULL,1),(440,'en_US','sampleDraft','status','error','Please specify status',NULL,1),(441,'en_US','sampleDraft','fileData','label','Sample File',NULL,1),(442,'en_US','sampleDraft','fileData','suffix','(10Mb Max)',NULL,1),(443,'en_US','sampleDraft','created','data','Sample Created.',NULL,1),(444,'en_US','sampleDraft','updated','data','Sample Updated.',NULL,1),(445,'en_US','sampleDraft','removed','data','Sample Removed.',NULL,1),(446,'en_US','sampleDraft','fileupload_wait','data','File Upload Started. Please Wait ...',NULL,1),(447,'en_US','sampleDraft','fileupload_done','data','File Upload Done.',NULL,1),(448,'en_US','sampleDraft','fileupload_nofile','data','No File Selected.',NULL,1),(449,'en_US','sampleDraft','material_provided','label','material_provided',NULL,1),(450,'en_US','sampleDraft','reference_genome_id','label','reference_genome_id',NULL,1),(451,'en_US','sampleDraft','species_id','label','species_id',NULL,1),(452,'en_US','sampleDraft','fragment_size','label','fragment_size',NULL,1),(453,'en_US','sampleDraft','amount','label','amount',NULL,1),(454,'en_US','sampleDraft','concentration','label','concentration',NULL,1),(455,'en_US','sampleDraft','260_280','label','260_280',NULL,1),(456,'en_US','sampleDraft','260_230','label','260_230',NULL,1),(457,'en_US','sampleDraft','volume','label','volume',NULL,1),(458,'en_US','sampleDraft','buffer','label','buffer',NULL,1),(459,'en_US','sampleDraft','sample_type','label','sample_type',NULL,1),(460,'en_US','sampleDraft','antibody_id','label','antibody_id',NULL,1),(461,'en_US','sampleDraft','enrich_primer_pair_id','label','enrich_primer_pair_id',NULL,1),(462,'en_US','sampleDraft','material_provided','metaposition','10',NULL,1),(463,'en_US','sampleDraft','reference_genome_id','metaposition','20',NULL,1),(464,'en_US','sampleDraft','species_id','metaposition','30',NULL,1),(465,'en_US','sampleDraft','fragment_size','metaposition','40',NULL,1),(466,'en_US','sampleDraft','amount','metaposition','50',NULL,1),(467,'en_US','sampleDraft','concentration','metaposition','60',NULL,1),(468,'en_US','sampleDraft','260_280','metaposition','70',NULL,1),(469,'en_US','sampleDraft','260_230','metaposition','80',NULL,1),(470,'en_US','sampleDraft','volume','metaposition','90',NULL,1),(471,'en_US','sampleDraft','buffer','metaposition','100',NULL,1),(472,'en_US','sampleDraft','sample_type','metaposition','110',NULL,1),(473,'en_US','sampleDraft','antibody_id','metaposition','120',NULL,1),(474,'en_US','sampleDraft','enrich_primer_pair_id','metaposition','130',NULL,1),(475,'en_US','hello','error=Hello World','','',NULL,1),(476,'en_US','sample','name','label','Name',NULL,1),(477,'en_US','sample','type','label','Type',NULL,1),(478,'en_US','sample','subtype','label','Subtype',NULL,1),(479,'en_US','sample','job','label','Job',NULL,1),(480,'en_US','sample','submitterUserId','label','Submitter',NULL,1),(481,'en_US','sample','isreceived','label','Is Received',NULL,1),(482,'en_US','sample','runs','label','Runs',NULL,1),(483,'en_US','sample','name','error','Name cannot be null',NULL,1),(484,'en_US','platformunit','name','label','Name',NULL,1),(485,'en_US','platformunit','name','error','Name cannot be empty',NULL,1),(486,'en_US','platformunit','barcode','label','Barcode',NULL,1),(487,'en_US','platformunit','barcode','constraint','NotEmpty',NULL,1),(488,'en_US','platformunit','barcode','error','Barcode cannot be empty',NULL,1),(489,'en_US','platformunit','barcode','metaposition','10',NULL,1),(490,'en_US','platformunit','comment','label','Comment',NULL,1),(491,'en_US','platformunit','comment','error','Comment',NULL,1),(492,'en_US','platformunit','comment','metaposition','20',NULL,1),(493,'en_US','platformunit','version','label','Version',NULL,1),(494,'en_US','platformunit','version','error','VErsioN',NULL,1),(495,'en_US','platformunit','version','metaposition','30',NULL,1),(496,'en_US','platformunit','lanecount','label','Lane Count',NULL,1),(497,'en_US','platformunit','lanecount','constraint','NotEmpty',NULL,1),(498,'en_US','platformunit','lanecount','error','Lane Count cannot be empty',NULL,1),(499,'en_US','platformunit','lanecount','control','select:1:1;8:8',NULL,1),(500,'en_US','platformunit','lanecount','metaposition','40',NULL,1),(501,'en_US','fmpayment','amount','label','Amount',NULL,1),(502,'en_US','fmpayment','amount','constraint','NotEmpty',NULL,1),(503,'en_US','fmpayment','amount','metaposition','10',NULL,1),(504,'en_US','fmpayment','amount','error','AmounT cannot be Empty',NULL,1),(505,'en_US','fmpayment','comment','label','Comment',NULL,1),(506,'en_US','fmpayment','comment','constraint','NotEmpty',NULL,1),(507,'en_US','fmpayment','comment','metaposition','20',NULL,1),(508,'en_US','fmpayment','comment','error','Comment cannot be Empty',NULL,1),(509,'en_US','uiField','updated','label','Field Updated',NULL,1),(510,'en_US','uiField','added','label','Field Added',NULL,1),(511,'en_US','uiField','removed','data','Field Deleted',NULL,1),(512,'en_US','uiField','name','label','Field Name',NULL,1),(513,'en_US','uiField','area','label','Area',NULL,1),(514,'en_US','uiField','locale','label','Locale',NULL,1),(515,'en_US','uiField','attrName','label','Attribute Name',NULL,1),(516,'en_US','uiField','attrValue','label','Attribute Value',NULL,1),(517,'en_US','uiField','locale','error','Locale not specified',NULL,1),(518,'en_US','uiField','attrName','suffix','<font color=\"blue\"> see footnote<sup>1</sup> </font>',NULL,1),(519,'en_US','auth','login_submit','label','Login',NULL,1),(520,'en_US','userPending','submit','label','Apply for Account',NULL,1),(521,'en_US','auth','confirmemail_submit','label','Submit',NULL,1),(522,'en_US','auth','resetpasswordemailsent','label','An email has been sent to your registered email address containing an authorization code. Please click the link within this email or alternatively <a href=\"form.do\">click here</a> and enter the authorization code provided. ',NULL,1),(523,'en_US','auth','resetpasswordRequest_submit','label','Submit',NULL,1),(524,'en_US','userPending','emailsent','label','Thank you for your account request. You have been sent an email with instructions as to how to confirm your email address. Please click to <a href=\"../login.do\"/>Login</a>',NULL,1),(525,'en_US','auth','resetpassword_submit','label','Submit',NULL,1),(526,'en_US','department','detail','label','Department',NULL,1),(527,'en_US','department','detail_update_missingparam','error','Deaprtment name must be provided',NULL,1),(528,'en_US','department','detail_update_ok','label','Department has been updated',NULL,1),(529,'en_US','department','detail_missingparam','error','Administrator name is missing',NULL,1),(530,'en_US','department','detail_invalidDept','error','Specified department does Not exist',NULL,1),(531,'en_US','department','detail_adminAlreadyExists','error','Selected person is already an administrator for this department',NULL,1),(532,'en_US','department','detail_administrators','label','Administrators',NULL,1),(533,'en_US','user','mypassword_submit','label','Submit',NULL,1),(534,'en_US','uiField','not_unique','error','Property already exists',NULL,1),(535,'en_US','userPending','captcha','error','Captcha text incorrect',NULL,1),(536,'en_US','userPending','captcha','label','Captcha text',NULL,1),(537,'en_US','hello','default','error','Hello World',NULL,1),(538,'en_US','userPending','action_approve','label','APPROVE',NULL,1),(539,'en_US','userPending','action_reject','label','REJECT',NULL,1),(540,'en_US','labuser','status_activate','label','Activate',NULL,1),(541,'en_US','labuser','status_deactivate','label','Deactivate',NULL,1),(542,'en_US','labuser','status_promoteLM','label','PROMOTE to LM',NULL,1),(543,'en_US','labuser','status_demoteLU','label','DEMOTE to LU',NULL,1),(544,'en_US','labuser','labUserNotFoundInLab','error','Cannot locate specified lab user in specified lab',NULL,1),(545,'en_US','userDetail','edit','label','Edit',NULL,1),(546,'en_US','userDetail','edit_as_other','label','Edit (as other)',NULL,1),(547,'en_US','userDetail','change_password','label','Change Password',NULL,1),(548,'en_US','userDetail','cancel','label','Cancel',NULL,1),(549,'en_US','userDetail','save','label','Save Changes',NULL,1),(550,'en_US','userDetail','lab_users','label','Lab Users',NULL,1),(551,'en_US','userDetail','samples','label','Samples',NULL,1),(552,'en_US','userDetail','jobs','label','Jobs',NULL,1),(553,'en_US','department','detail_createadmin','label','Create Administrator',NULL,1),(554,'en_US','department','detail_submit','label','Submit',NULL,1),(555,'en_US','department','detail_email','label','Administrator\'s Email',NULL,1),(556,'en_US','department','detail_existingadmin','label','Current Administrators',NULL,1),(557,'en_US','department','detail_administrator_name','label','Administrator\'s Name',NULL,1),(558,'en_US','department','detail_labs','label','Labs',NULL,1),(559,'en_US','department','detail_pendinglabs','label','Pending Labs',NULL,1),(560,'en_US','department','detail_emailnotfound','error','Email not found',NULL,1),(561,'en_US','department','detail_ok','label','New Administrator Created',NULL,1),(562,'en_US','department','detail_formatting','error','Formatting Error',NULL,1),(563,'en_US','department','detail_missinglogin','error','Login is empty',NULL,1),(564,'en_US','department','detail_usernotfound','error','User not found in database',NULL,1),(565,'en_US','department','detail_remove','label','Remove',NULL,1),(566,'en_US','department','create','label','Create New Department',NULL,1),(567,'en_US','department','list','label','List &amp; Manage Departments',NULL,1),(568,'en_US','userPending','building_room','constraint','NotEmpty',NULL,1),(569,'en_US','userPending','building_room','error','Room cannot be empty',NULL,1),(570,'en_US','userPending','locale','constraint','NotEmpty',NULL,1),(571,'en_US','user','departmentId','control','select:${departments}:departmentId:name',NULL,1),(572,'en_US','user','departmentId','constraint','NotEmpty',NULL,1),(573,'en_US','user','departmentId','error','A department must be selected',NULL,1),(574,'en_US','userPending','emailconfirmed','label','Your email address is confirmed and your principal investigator has been emailed to request confirmation of your eligibility to join their lab. You are advised to contact them to request they do this if your account does not become activated in good time.',NULL,1),(575,'en_US','sampleDraft','subtypeSampleId','label','Subtype',NULL,1),(576,'en_US','job','name','label','Name',NULL,1),(577,'en_US','job','name','constraint','NotEmpty',NULL,1),(578,'en_US','job','name','error','Name cannot be empty',NULL,1),(579,'en_US','job','submitter','label','Submitter',NULL,1),(580,'en_US','job','lab','label','Lab',NULL,1),(581,'en_US','job','isActive','label','Active',NULL,1),(582,'en_US','contentTypeMap','etx','data','text/x-setext',NULL,1),(583,'en_US','contentTypeMap','me','data','application/x-troff-me',NULL,1),(584,'en_US','contentTypeMap','xbm','data','image/x-xbitmap',NULL,1),(585,'en_US','contentTypeMap','xpm','data','image/x-xbitmap',NULL,1),(586,'en_US','contentTypeMap','texinfo','data','application/x-texinfo',NULL,1),(587,'en_US','contentTypeMap','texi','data','application/x-texinfo',NULL,1),(588,'en_US','contentTypeMap','ief','data','image/ief',NULL,1),(589,'en_US','contentTypeMap','pbm','data','image/x-portable-bitmap',NULL,1),(590,'en_US','contentTypeMap','dvi','data','application/x-dvi',NULL,1),(591,'en_US','contentTypeMap','rgb','data','image/x-rgb',NULL,1),(592,'en_US','contentTypeMap','src','data','application/x-wais-source',NULL,1),(593,'en_US','contentTypeMap','wsrc','data','application/x-wais-source',NULL,1),(594,'en_US','contentTypeMap','man','data','application/x-troff-man',NULL,1),(595,'en_US','contentTypeMap','pnm','data','image/x-portable-anymap',NULL,1),(596,'en_US','contentTypeMap','xwd','data','image/x-xwindowdump',NULL,1),(597,'en_US','contentTypeMap','ras','data','image/x-cmu-rast',NULL,1),(598,'en_US','contentTypeMap','saveme','data','application/octet-stream',NULL,1),(599,'en_US','contentTypeMap','dump','data','application/octet-stream',NULL,1),(600,'en_US','contentTypeMap','hqx','data','application/octet-stream',NULL,1),(601,'en_US','contentTypeMap','arc','data','application/octet-stream',NULL,1),(602,'en_US','contentTypeMap','obj','data','application/octet-stream',NULL,1),(603,'en_US','contentTypeMap','lib','data','application/octet-stream',NULL,1),(604,'en_US','contentTypeMap','bin','data','application/octet-stream',NULL,1),(605,'en_US','contentTypeMap','exe','data','application/octet-stream',NULL,1),(606,'en_US','contentTypeMap','zip','data','application/octet-stream',NULL,1),(607,'en_US','contentTypeMap','gz','data','application/octet-stream',NULL,1),(608,'en_US','contentTypeMap','latex','data','application/x-latex',NULL,1),(609,'en_US','contentTypeMap','pdf','data','application/pdf',NULL,1),(610,'en_US','contentTypeMap','xml','data','application/xml',NULL,1),(611,'en_US','contentTypeMap','tex','data','application/x-tex',NULL,1),(612,'en_US','contentTypeMap','mov','data','video/quicktime',NULL,1),(613,'en_US','contentTypeMap','qt','data','video/quicktime',NULL,1),(614,'en_US','contentTypeMap','mime','data','message/rfc822',NULL,1),(615,'en_US','contentTypeMap','fpx','data','image/vnd.fpx',NULL,1),(616,'en_US','contentTypeMap','fpix','data','image/vnd.fpx',NULL,1),(617,'en_US','contentTypeMap','oda','data','application/oda',NULL,1),(618,'en_US','contentTypeMap','t','data','application/x-troff',NULL,1),(619,'en_US','contentTypeMap','tr','data','application/x-troff',NULL,1),(620,'en_US','contentTypeMap','roff','data','application/x-troff',NULL,1),(621,'en_US','contentTypeMap','pgm','data','image/x-portable-graymap',NULL,1),(622,'en_US','contentTypeMap','ms','data','application/x-troff-ms',NULL,1),(623,'en_US','contentTypeMap','movie','data','video/x-sgi-movie',NULL,1),(624,'en_US','contentTypeMap','mv','data','video/x-sgi-movie',NULL,1),(625,'en_US','contentTypeMap','tsv','data','text/tab-separated-values',NULL,1),(626,'en_US','contentTypeMap','ppm','data','image/x-portable-pixmap',NULL,1),(627,'en_US','pageTitle','auth/login','label','Login',NULL,1),(628,'en_US','pageTitle','auth/newpi/form','label','New Principal Investigator',NULL,1),(629,'en_US','pageTitle','auth/newpi/institute','label','New Principal Investigator',NULL,1),(630,'en_US','pageTitle','auth/newpi/emailok','label','Email Confirmed',NULL,1),(631,'en_US','pageTitle','auth/confirmemail/emailupdateok','label','Email Update Confirmed',NULL,1),(632,'en_US','pageTitle','auth/confirmemail/requestEmailChange','label','Request to Change Email',NULL,1),(633,'en_US','pageTitle','auth/confirmemail/emailchanged','label','Confirm New Email Address',NULL,1),(634,'en_US','pageTitle','auth/confirmemail/userloginchanged','label','User Login Changed',NULL,1),(635,'en_US','pageTitle','auth/confirmemail/userloginandemailchanged','label','Confirm New Email Address',NULL,1),(636,'en_US','pageTitle','auth/confirmemail/useridchanged','label','Confirm New User Id',NULL,1),(637,'en_US','pageTitle','auth/newpi/created','label','Application Submitted',NULL,1),(638,'en_US','pageTitle','auth/newuser/form','label','New User',NULL,1),(639,'en_US','pageTitle','auth/newuser/emailok','label','Email Confirmed',NULL,1),(640,'en_US','pageTitle','auth/newuser/created','label','Application Submitted',NULL,1),(641,'en_US','pageTitle','auth/resetpassword/authcodeform','label','Reset Password',NULL,1),(642,'en_US','pageTitle','auth/resetpassword/form','label','Reset Password',NULL,1),(643,'en_US','pageTitle','auth/resetpassword/ok','label','Password Was Reset',NULL,1),(644,'en_US','pageTitle','auth/confirmemail/authcodeform','label','Confirm Email Address',NULL,1),(645,'en_US','pageTitle','auth/resetpassword/request','label','Reset Password Request',NULL,1),(646,'en_US','pageTitle','auth/resetpassword/email','label','Email Sent',NULL,1),(647,'en_US','pageTitle','dashboard','label','WASP',NULL,1),(648,'en_US','pageTitle','user/detail_rw','label','Update User Detail',NULL,1),(649,'en_US','pageTitle','user/detail_ro','label','User Detail',NULL,1),(650,'en_US','pageTitle','user/list','label','User List',NULL,1),(651,'en_US','pageTitle','lab/list','label','Lab List',NULL,1),(652,'en_US','pageTitle','lab/detail_ro','label','Lab Detail',NULL,1),(653,'en_US','pageTitle','lab/detail_rw','label','Update Lab Detail',NULL,1),(654,'en_US','pageTitle','lab/pending/detail_ro','label','Pending Lab Detail',NULL,1),(655,'en_US','pageTitle','lab/pending/detail_rw','label','Update Pending Lab Detail',NULL,1),(656,'en_US','pageTitle','lab/pendinguser/list','label','Pending Users',NULL,1),(657,'en_US','pageTitle','lab/user_manager','label','Lab User Manager',NULL,1),(658,'en_US','pageTitle','lab/user_list','label','Lab Member List',NULL,1),(659,'en_US','pageTitle','user/mypassword','label','Change Password',NULL,1),(660,'en_US','pageTitle','department/list','label','Department List',NULL,1),(661,'en_US','pageTitle','department/detail','label','Department Detail',NULL,1),(662,'en_US','pageTitle','resource/list','label','Resource List',NULL,1),(663,'en_US','pageTitle','resource/detail_ro','label','Resource Detail',NULL,1),(664,'en_US','pageTitle','resource/detail_rw','label','Update Resource Detail',NULL,1),(665,'en_US','pageTitle','resource/create','label','Create New Resource',NULL,1),(666,'en_US','pageTitle','run/detail','label','Run Detail',NULL,1),(667,'en_US','pageTitle','run/list','label','Run List',NULL,1),(668,'en_US','pageTitle','jobsubmit/list','label','New Job',NULL,1),(669,'en_US','pageTitle','jobsubmit/create','label','New Job',NULL,1),(670,'en_US','pageTitle','jobsubmit/metaform','label','New Job',NULL,1),(671,'en_US','pageTitle','jobsubmit/sample','label','Submit Samples',NULL,1),(672,'en_US','pageTitle','uifield/list','label','Properties',NULL,1),(673,'en_US','pageTitle','lab/newrequest','label','Request Access to Lab',NULL,1),(674,'en_US','pageTitle','jobsubmit/cell','label','Cells',NULL,1),(675,'en_US','pageTitle','sysrole/list','label','System User Management',NULL,1),(676,'en_US','pageTitle','sample/list','label','Sample Utilities',NULL,1),(677,'en_US','pageTitle','job/list','label','Job List',NULL,1),(678,'en_US','pageTitle','facility/platformunit/list','label','Platform Unit List',NULL,1),(679,'en_US','uiField','updated','data','Attribute Updated',NULL,1),(680,'en_US','piPending','captcha','error','Captcha text incorrect',NULL,1),(681,'en_US','piPending','captcha','label','Captcha text',NULL,1),(682,'en_US','piPending','submit','label','Apply for Account',NULL,1),(683,'en_US','piPending','select_default','label','-- select --',NULL,1),(684,'en_US','piPending','emailconfirmed','label','Your email address has been confirmed and your department administrator has been emailed to request confirmation of your eligibility to register your lab. You are advised to contact them to request they do this if your account does not become activated in good time.',NULL,1),(685,'en_US','piPending','emailsent','label','Thank you for your account request. You have been sent an email with instructions as to how to confirm your email address. Please click to <a href=\"../login.do\"/>Login</a>',NULL,1),(686,'en_US','userPending','action','error','Invalid action. Must be approve or reject only',NULL,1),(687,'en_US','userPending','labid_mismatch','error','Lab id mismatch with user-pending id',NULL,1),(688,'en_US','sampleDraft','cloned','label','Locked',NULL,1),(689,'en_US','sampleDraft','jobId','label','Existing Job',NULL,1),(690,'en_US','sampleDraft','sourceSampleId','label','Existing Sample',NULL,1),(691,'en_US','job','jobViewerUserRoleAdd','error1','JobId or LabId Error',NULL,1),(692,'en_US','job','jobViewerUserRoleAdd','error2','Login Not Found',NULL,1),(693,'en_US','job','jobViewerUserRoleAdd','error3','User is submitter, thus already has access to this job',NULL,1),(694,'en_US','job','jobViewerUserRoleAdd','error4','User already has access to this job',NULL,1),(695,'en_US','lab','adduser','label','Add New LabUser',NULL,1),(696,'en_US','lab','detail','label','Lab Details',NULL,1),(697,'en_US','labuser','active','label','Active',NULL,1),(698,'en_US','labuser','inactive','label','Inactive',NULL,1),(699,'en_US','resource','name','label','Name',NULL,1),(700,'en_US','resource','name','error','Resource name cannot be empty',NULL,1),(701,'en_US','resource','typeResourceId','label','Resource Type',NULL,1),(702,'en_US','resource','typeResourceId','error','Must select type of resource',NULL,1),(703,'en_US','resource','assay_platform','label','Assay Platform',NULL,1),(704,'en_US','resource','assay_platform','error','Must select assay platform',NULL,1),(705,'en_US','resource','assay_platform','control','select:ROCHE:ROCHE;ILLUMINA:ILLUMINA',NULL,1),(706,'en_US','resource','assay_platform','constraint','NotEmpty',NULL,1),(707,'en_US','resource','assay_platform','metaposition','20',NULL,1),(708,'en_US','resource','machine_type','label','Machine Type',NULL,1),(709,'en_US','resource','machine_type','error','Must select machine type',NULL,1),(710,'en_US','resource','machine_type','constraint','NotEmpty',NULL,1),(711,'en_US','resource','machine_type','control','select:FLX:FLX;GAIIx:GAIIx;HISEQ2000:HISEQ2000',NULL,1),(712,'en_US','resource','machine_type','metaposition','30',NULL,1),(713,'en_US','resource','commission_date','label','Commission Date',NULL,1),(714,'en_US','resource','commission_date','error','Commission date cannot be empty',NULL,1),(715,'en_US','resource','commission_date','constraint','NotEmpty',NULL,1),(716,'en_US','resource','commission_date','metaposition','40',NULL,1),(717,'en_US','resource','decommission_date','label','Decommission Date',NULL,1),(718,'en_US','resource','decommission_date','error','Decommission date cannot be empty',NULL,1),(719,'en_US','resource','decommission_date','metaposition','50',NULL,1),(720,'en_US','resource','save','label','Save',NULL,1),(721,'en_US','resource','cancel','label','Cancel',NULL,1),(722,'en_US','resource','updated','error','Resource was NOT updated. Please fill in required fields.',NULL,1),(723,'en_US','resource','created','error','Resource was NOT created. Please fill in required fields.',NULL,1),(724,'en_US','resource','updated_success','label','Resource updated sucessfully.',NULL,1),(725,'en_US','resource','created_success','label','Resource created sucessfully.',NULL,1),(726,'en_US','resource','isActive','label','Active',NULL,1),(727,'en_US','resource','resourceId','label','ResourceId',NULL,1),(728,'en_US','run','name','label','Name',NULL,1),(729,'en_US','run','name','constraint','NotEmpty',NULL,1),(730,'en_US','run','name','error','Run name cannot be empty',NULL,1),(731,'en_US','run','flow_cell_name','label','Flowcell',NULL,1),(732,'en_US','run','start_esf_staff','label','Start ESF Staff',NULL,1),(733,'en_US','run','start_datetime','label','Start Time',NULL,1),(734,'en_US','run','end_datetime','label','End Time',NULL,1),(735,'en_US','run','machine_name','label','Machine',NULL,1),(736,'en_US','run','path_to_data','label','Data Path',NULL,1),(737,'en_US','run','run_success','label','Success',NULL,1),(738,'en_US','run','isActive','label','Active',NULL,1),(739,'en_US','sysrole','noUserSpecified','error','No user specified',NULL,1),(740,'en_US','sysrole','userNonexistant','error','The user specified does not exist in the database',NULL,1),(741,'en_US','sysrole','noRoleSpecified','error','No role specified',NULL,1),(742,'en_US','sysrole','invalidRoleSpecified','error','Invalid role specified',NULL,1),(743,'en_US','sysrole','wrongUserRoleCombination','error','Specified user doesn\'t have specified role',NULL,1),(744,'en_US','sysrole','userRoleExists','error','Specified user already has the selected role',NULL,1),(745,'en_US','sysrole','onlyUserWithRole','error','Cannot remove role from user. The role must be granted to another user first.',NULL,1),(746,'en_US','sysrole','success','label','Update completed successfully',NULL,1),(747,'en_US','sysrole','list_sysuser_name','label','Existing User',NULL,1),(748,'en_US','sysrole','list_sysuser_role','label','New Role',NULL,1),(749,'en_US','sysrole','list_submit','label','Submit',NULL,1),(750,'en_US','sysrole','list_create','label','Add System Role to User',NULL,1),(751,'en_US','sysrole','list_current','label','Current Users with System Roles',NULL,1),(752,'en_US','labDetail','create_job','label','create job',NULL,1),(753,'en_US','labDetail','edit','label','Edit',NULL,1),(754,'en_US','labDetail','cancel','label','Cancel',NULL,1),(755,'en_US','labDetail','save','label','Save Changes',NULL,1),(756,'en_US','wasp','default_select','label','-- select --',NULL,1),(757,'en_US','wasp','isAuthenticationExternal','data','FALSE',NULL,1),(758,'en_US','wasp','authentication','label','WASP',NULL,1),(759,'en_US','metadata','rangeMax','error','Value exceeds maximum permitted',NULL,1),(760,'en_US','metadata','rangeMin','error','Value less than minimum permitted',NULL,1),(761,'en_US','metadata','lengthMax','error','Length exceeds maximum permitted',NULL,1),(762,'en_US','metadata','lengthMin','error','Length less than minimum permitted',NULL,1),(763,'en_US','metadata','metaType','error','Value is not of expected type',NULL,1),(764,'en_US','job','approval','approved','Job has been approved',NULL,1),(765,'en_US','job','approval','rejected','Job has been rejected',NULL,1),(766,'en_US','job','approval','error','Error - Update Failed',NULL,1),(767,'en_US','task','samplereceive','error_state_sample_conflict','Error - StateId and SampleId Mismatch',NULL,1),(768,'en_US','task','samplereceive','error_state_task_conflict','Error - StateId and TaskId Mismatch',NULL,1),(769,'en_US','task','samplereceive','update_success','Update completed successfully',NULL,1),(770,'en_US','task','samplereceive','title_label','Incoming Sample Manager',NULL,1),(771,'en_US','task','samplereceive','error_receivedstatus_empty','Update Failed: Please select Received or Never Coming',NULL,1),(772,'en_US','task','samplereceive','error_receivedstatus_invalid','Update Failed: Action Invalid',NULL,1),(773,'en_US','jobDraft','user_incorrect','error','You are not authorized to edit this jobdraft',NULL,1),(774,'en_US','jobDraft','not_pending','error','This jobdraft has already been submitted',NULL,1),(775,'en_US','jobDraft','form','error','Please address errors on this page',NULL,1),(776,'en_US','workflow','listname','label','List of Workflows',NULL,1),(777,'en_US','workflow','workflowId','label','Workflow Id',NULL,1),(778,'en_US','workflow','isActive','label','Is Active?',NULL,1),(779,'en_US','workflow','configure','label','Resources and Software',NULL,1),(780,'en_US','workflow','name','label','Workflow Name',NULL,1),(918,'en_US','illuminaHiSeq2000','readLength','metaposition','10','2012-01-29 18:02:37',0),(919,'en_US','illuminaHiSeq2000','readLength','label','Read Length','2012-01-29 18:02:38',0),(920,'en_US','illuminaHiSeq2000','readLength','control','select:${resourceOptions.get(readlength)}:value:label','2012-01-29 18:02:38',0),(921,'en_US','illuminaHiSeq2000','readLength','constraint','NotEmpty','2012-01-29 18:02:38',0),(922,'en_US','illuminaHiSeq2000','readLength','error','You must choose a read length','2012-01-29 18:02:38',0),(923,'en_US','illuminaHiSeq2000','readType','metaposition','20','2012-01-29 18:02:38',0),(924,'en_US','illuminaHiSeq2000','readType','label','Read Type','2012-01-29 18:02:38',0),(925,'en_US','illuminaHiSeq2000','readType','control','select:${resourceOptions.get(readType)}:value:label','2012-01-29 18:02:38',0),(926,'en_US','illuminaHiSeq2000','readType','constraint','NotEmpty','2012-01-29 18:02:38',0),(927,'en_US','illuminaHiSeq2000','readType','error','You must choose a read type','2012-01-29 18:02:38',0),(928,'en_US','bowtieAlignerV0_12_7','mismatches','metaposition','10','2012-01-29 18:02:38',0),(929,'en_US','bowtieAlignerV0_12_7','mismatches','label','Number of mismatches (0-3)','2012-01-29 18:02:38',0),(930,'en_US','bowtieAlignerV0_12_7','mismatches','type','INTEGER','2012-01-29 18:02:38',0),(931,'en_US','bowtieAlignerV0_12_7','mismatches','range','0:3','2012-01-29 18:02:38',0),(932,'en_US','bowtieAlignerV0_12_7','seedLength','metaposition','20','2012-01-29 18:02:38',0),(933,'en_US','bowtieAlignerV0_12_7','seedLength','label','Seed Length','2012-01-29 18:02:38',0),(934,'en_US','bowtieAlignerV0_12_7','seedLength','type','NUMBER','2012-01-29 18:02:38',0),(935,'en_US','bowtieAlignerV0_12_7','seedLength','range','5:1000','2012-01-29 18:02:38',0),(936,'en_US','bowtieAlignerV0_12_7','seedLength','default','32','2012-01-29 18:02:38',0),(937,'en_US','bowtieAlignerV0_12_7','reportAlignmentNum','metaposition','30','2012-01-29 18:02:38',0),(938,'en_US','bowtieAlignerV0_12_7','reportAlignmentNum','label','Number of Alignments to Report','2012-01-29 18:02:38',0),(939,'en_US','bowtieAlignerV0_12_7','reportAlignmentNum','type','INTEGER','2012-01-29 18:02:38',0),(940,'en_US','bowtieAlignerV0_12_7','reportAlignmentNum','default','1','2012-01-29 18:02:38',0),(941,'en_US','bowtieAlignerV0_12_7','discardThreshold','metaposition','40','2012-01-29 18:02:38',0),(942,'en_US','bowtieAlignerV0_12_7','discardThreshold','label','Discard if more than how many alignments?','2012-01-29 18:02:38',0),(943,'en_US','bowtieAlignerV0_12_7','discardThreshold','type','INTEGER','2012-01-29 18:02:38',0),(944,'en_US','bowtieAlignerV0_12_7','isBest','metaposition','50','2012-01-29 18:02:38',0),(945,'en_US','bowtieAlignerV0_12_7','isBest','label','report only best alignments?','2012-01-29 18:02:38',0),(946,'en_US','bowtieAlignerV0_12_7','isBest','control','select:yes:yes;no;no','2012-01-29 18:02:38',0),(947,'en_US','macsPeakcallerV4_1','pValueCutoff','metaposition','10','2012-01-29 18:02:38',0),(948,'en_US','macsPeakcallerV4_1','pValueCutoff','label','p Value Cutoff','2012-01-29 18:02:38',0),(949,'en_US','macsPeakcallerV4_1','pValueCutoff','default','1.0e+5','2012-01-29 18:02:38',0),(950,'en_US','macsPeakcallerV4_1','pValueCutoff','type','NUMBER','2012-01-29 18:02:38',0),(951,'en_US','macsPeakcallerV4_1','bandwidth','metaposition','20','2012-01-29 18:02:38',0),(952,'en_US','macsPeakcallerV4_1','bandwidth','label','Bandwidth','2012-01-29 18:02:38',0),(953,'en_US','macsPeakcallerV4_1','bandwidth','type','NUMBER','2012-01-29 18:02:38',0),(954,'en_US','macsPeakcallerV4_1','bandwidth','range','0:5000','2012-01-29 18:02:38',0),(955,'en_US','macsPeakcallerV4_1','bandwidth','default','300','2012-01-29 18:02:38',0),(956,'en_US','macsPeakcallerV4_1','genomeSize','metaposition','30','2012-01-29 18:02:38',0),(957,'en_US','macsPeakcallerV4_1','genomeSize','label','Effective Genome Size','2012-01-29 18:02:38',0),(958,'en_US','macsPeakcallerV4_1','genomeSize','default','1.0e+9','2012-01-29 18:02:38',0),(959,'en_US','macsPeakcallerV4_1','genomeSize','type','NUMBER','2012-01-29 18:02:38',0),(960,'en_US','macsPeakcallerV4_1','keepDup','metaposition','40','2012-01-29 18:02:38',0),(961,'en_US','macsPeakcallerV4_1','keepDup','label','Keep Duplicates?','2012-01-29 18:02:38',0),(962,'en_US','macsPeakcallerV4_1','keepDup','control','select:yes:yes;no:no','2012-01-29 18:02:38',0),(963,'en_US','macsPeakcallerV4_1','keepDup','default','no','2012-01-29 18:02:38',0),(964,'en_US','chipseqDnaSample','concentration','label','Concentration','2012-01-29 18:02:38',0),(965,'en_US','chipseqDnaSample','concentration','metaposition','10','2012-01-29 18:02:38',0),(966,'en_US','chipseqDnaSample','concentration','constraint','NotEmpty','2012-01-29 18:02:38',0),(967,'en_US','chipseqDnaSample','concentration','error','You must provice a concentration','2012-01-29 18:02:38',0),(968,'en_US','chipseqDnaSample','concentration','type','NUMBER','2012-01-29 18:02:38',0),(969,'en_US','chipseqDnaSample','volume','label','volume','2012-01-29 18:02:38',0),(970,'en_US','chipseqDnaSample','volume','metaposition','20','2012-01-29 18:02:38',0),(971,'en_US','chipseqDnaSample','volume','constraint','NotEmpty','2012-01-29 18:02:38',0),(972,'en_US','chipseqDnaSample','buffer','label','buffer','2012-01-29 18:02:38',0),(973,'en_US','chipseqDnaSample','buffer','metaposition','30','2012-01-29 18:02:38',0),(974,'en_US','chipseqDnaSample','buffer','control','select:TE:TE;Water:Water','2012-01-29 18:02:38',0),(975,'en_US','chipseqDnaSample','buffer','constraint','NotEmpty','2012-01-29 18:02:38',0),(976,'en_US','chipseqDnaSample','buffer','error','You must select a buffer type','2012-01-29 18:02:38',0),(977,'en_US','chipseqDnaSample','species','label','Species','2012-01-29 18:02:38',0),(978,'en_US','chipseqDnaSample','species','metaposition','40','2012-01-29 18:02:38',0),(979,'en_US','chipseqDnaSample','species','control','select:a:a; b:b','2012-01-29 18:02:38',0),(980,'en_US','chipseqDnaSample','species','constraint','NotEmpty','2012-01-29 18:02:38',0),(981,'en_US','chipseqDnaSample','species','error','You must select a species','2012-01-29 18:02:38',0),(982,'en_US','chipseqDnaSample','fragmentSize','label','Average Fragmentation Size','2012-01-29 18:02:38',0),(983,'en_US','chipseqDnaSample','fragmentSize','metaposition','150','2012-01-29 18:02:38',0),(984,'en_US','chipseqDnaSample','fragmentSize','constraint','NotEmpty','2012-01-29 18:02:38',0),(985,'en_US','chipseqDnaSample','fragmentSize','error','You must provide a constraint','2012-01-29 18:02:39',0),(986,'en_US','chipseqDnaSample','fragmentSize','type','NUMBER','2012-01-29 18:02:39',0),(987,'en_US','chipseqDnaSample','fragmentSizeSD','label','Fragmentation Size Std. Dev.','2012-01-29 18:02:39',0),(988,'en_US','chipseqDnaSample','fragmentSizeSD','metaposition','160','2012-01-29 18:02:39',0),(989,'en_US','chipseqDnaSample','fragmentSizeSD','constraint','NotEmpty','2012-01-29 18:02:39',0),(990,'en_US','chipseqDnaSample','fragmentSizeSD','type','NUMBER','2012-01-29 18:02:39',0),(991,'en_US','chipseqDnaSample','fragmentSizeSD','error','You must provide a standard deviation','2012-01-29 18:02:39',0),(992,'en_US','chipseqDnaSample','antibody','label','Antibody','2012-01-29 18:02:39',0),(993,'en_US','chipseqDnaSample','antibody','metaposition','170','2012-01-29 18:02:39',0),(994,'en_US','chipseqLibrarySample','concentration','label','Concentration','2012-01-29 18:02:39',0),(995,'en_US','chipseqLibrarySample','concentration','metaposition','10','2012-01-29 18:02:39',0),(996,'en_US','chipseqLibrarySample','concentration','constraint','NotEmpty','2012-01-29 18:02:39',0),(997,'en_US','chipseqLibrarySample','concentration','error','You must provice a concentration','2012-01-29 18:02:39',0),(998,'en_US','chipseqLibrarySample','concentration','type','NUMBER','2012-01-29 18:02:39',0),(999,'en_US','chipseqLibrarySample','volume','label','volume','2012-01-29 18:02:39',0),(1000,'en_US','chipseqLibrarySample','volume','metaposition','20','2012-01-29 18:02:39',0),(1001,'en_US','chipseqLibrarySample','volume','constraint','NotEmpty','2012-01-29 18:02:39',0),(1002,'en_US','chipseqLibrarySample','buffer','label','buffer','2012-01-29 18:02:39',0),(1003,'en_US','chipseqLibrarySample','buffer','metaposition','30','2012-01-29 18:02:39',0),(1004,'en_US','chipseqLibrarySample','buffer','control','select:TE:TE;Water:Water','2012-01-29 18:02:39',0),(1005,'en_US','chipseqLibrarySample','buffer','constraint','NotEmpty','2012-01-29 18:02:39',0),(1006,'en_US','chipseqLibrarySample','buffer','error','You must select a buffer type','2012-01-29 18:02:39',0),(1007,'en_US','chipseqLibrarySample','species','label','Species','2012-01-29 18:02:39',0),(1008,'en_US','chipseqLibrarySample','species','metaposition','40','2012-01-29 18:02:39',0),(1009,'en_US','chipseqLibrarySample','species','control','select:a:a; b:b','2012-01-29 18:02:39',0),(1010,'en_US','chipseqLibrarySample','species','constraint','NotEmpty','2012-01-29 18:02:39',0),(1011,'en_US','chipseqLibrarySample','species','error','You must select a species','2012-01-29 18:02:39',0),(1012,'en_US','chipseqLibrarySample','fragmentSize','label','Average Fragmentation Size','2012-01-29 18:02:39',0),(1013,'en_US','chipseqLibrarySample','fragmentSize','metaposition','200','2012-01-29 18:02:39',0),(1014,'en_US','chipseqLibrarySample','fragmentSize','constraint','NotEmpty','2012-01-29 18:02:39',0),(1015,'en_US','chipseqLibrarySample','fragmentSize','error','You must provide a constraint','2012-01-29 18:02:39',0),(1016,'en_US','chipseqLibrarySample','fragmentSize','type','NUMBER','2012-01-29 18:02:39',0),(1017,'en_US','chipseqLibrarySample','fragmentSizeSD','label','Fragmentation Size Std. Dev.','2012-01-29 18:02:39',0),(1018,'en_US','chipseqLibrarySample','fragmentSizeSD','metaposition','210','2012-01-29 18:02:39',0),(1019,'en_US','chipseqLibrarySample','fragmentSizeSD','constraint','NotEmpty','2012-01-29 18:02:39',0),(1020,'en_US','chipseqLibrarySample','fragmentSizeSD','type','NUMBER','2012-01-29 18:02:39',0),(1021,'en_US','chipseqLibrarySample','fragmentSizeSD','error','You must provide a standard deviation','2012-01-29 18:02:39',0),(1022,'en_US','chipseqLibrarySample','antibody','label','Antibody','2012-01-29 18:02:39',0),(1023,'en_US','chipseqLibrarySample','antibody','metaposition','220','2012-01-29 18:02:39',0),(1024,'en_US','chipseqLibrarySample','adaptor','label','Adaptorset','2012-01-29 18:02:39',0),(1025,'en_US','chipseqLibrarySample','adaptor','metaposition','220','2012-01-29 18:02:39',0),(1026,'en_US','chipseqLibrarySample','adaptor','control','select:truseqIndexedDna:TruSEQ INDEXED DNA','2012-01-29 18:02:39',0),(1027,'en_US','chipseqLibrarySample','adaptorindex','label','Adaptor Index','2012-01-29 18:02:39',0),(1028,'en_US','chipseqLibrarySample','adaptorindex','metaposition','230','2012-01-29 18:02:39',0),(1029,'en_US','chipseqLibrarySample','adaptorindex','control','select:1:1;2:2;3:3;4:4;5:5;6:6;7:7;8:8','2012-01-29 18:02:39',0),(1030,'en_US','chipseqLibrarySample','size','label','Library Size','2012-01-29 18:02:39',0),(1031,'en_US','chipseqLibrarySample','size','metaposition','240','2012-01-29 18:02:39',0),(1032,'en_US','chipseqLibrarySample','size','control','select:1:1;2:2;3:3;4:4;5:5;6:6;7:7;8:8','2012-01-29 18:02:39',0),(1033,'en_US','illuminaFlowcellV3','cellName','label','Lane','2012-01-29 18:02:39',0),(1034,'en_US','illuminaFlowcellV3','platformUnitName','label','Flow Cell','2012-01-29 18:02:39',0),(1035,'en_US','boneShakerApprovalTask','canWeShake','metaposition','10','2012-01-29 18:02:39',0),(1036,'en_US','boneShakerApprovalTask','canWeShake','label','Can We Shake?','2012-01-29 18:02:39',0),(1037,'en_US','boneShakerTask','ricther','metaposition','10','2012-01-29 18:02:39',0),(1038,'en_US','boneShakerTask','ricther','label','Ricther Scale','2012-01-29 18:02:40',0),(1045,'en_US','quoteJob','tmpl','tmpl','Tmpl','2012-01-29 18:02:40',0),(1047,'en_US','boneShakerTask','tmpl','tmpl','Tmpl','2012-01-29 18:02:40',0),(1048,'en_US','startJob','tmpl','tmpl','Tmpl','2012-01-29 18:02:40',0),(1049,'en_US','chipSeq','workflow','label','Chip Seq','2012-01-29 18:02:40',0),(1050,'en_US','chipSeq','jobsubmit/modifymeta','label','ModifyChipSeq Metadata','2012-01-29 18:02:40',0),(1051,'en_US','chipSeq','jobsubmit/resource/mps','label','MPS Sequencer Options','2012-01-29 18:02:40',0),(1052,'en_US','chipSeq','aligner','label','Chip Seq aligner Options','2012-01-29 18:02:40',0),(1053,'en_US','chipSeq','jobsubmit/resource/samples','label','DNA Sequencer Samples','2012-01-29 18:02:40',0),(1054,'en_US','chipSeq','jobsubmit/resource/cells','label','DNA Sequencer Cells','2012-01-29 18:02:40',0);
/*!40000 ALTER TABLE `uifield` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `userid` int(10) NOT NULL AUTO_INCREMENT,
  `login` varchar(250) DEFAULT NULL,
  `email` varchar(250) DEFAULT NULL,
  `password` varchar(250) DEFAULT NULL,
  `firstname` varchar(250) DEFAULT NULL,
  `lastname` varchar(250) DEFAULT NULL,
  `locale` varchar(5) DEFAULT 'en_US',
  `isactive` int(1) DEFAULT '1',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`userid`),
  UNIQUE KEY `u_user_login` (`login`),
  UNIQUE KEY `u_user_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'super','super@super.com','86f7e437faa5a7fce15d1ddcb9eaeaea377667b8','f','l','en_US',1,'2012-01-29 22:51:52',1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usermeta`
--

DROP TABLE IF EXISTS `usermeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usermeta` (
  `usermetaid` int(10) NOT NULL AUTO_INCREMENT,
  `userid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` varchar(250) DEFAULT NULL,
  `position` int(10) DEFAULT '0',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`usermetaid`),
  UNIQUE KEY `u_usermeta_k_uid` (`k`,`userid`),
  KEY `fk_usermeta_userid` (`userid`),
  CONSTRAINT `usermeta_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usermeta`
--

LOCK TABLES `usermeta` WRITE;
/*!40000 ALTER TABLE `usermeta` DISABLE KEYS */;
/*!40000 ALTER TABLE `usermeta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userpasswordauth`
--

DROP TABLE IF EXISTS `userpasswordauth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userpasswordauth` (
  `userid` int(10) NOT NULL,
  `authcode` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`userid`),
  UNIQUE KEY `u_userpasswordauth` (`authcode`),
  CONSTRAINT `userpasswordauth_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userpasswordauth`
--

LOCK TABLES `userpasswordauth` WRITE;
/*!40000 ALTER TABLE `userpasswordauth` DISABLE KEYS */;
/*!40000 ALTER TABLE `userpasswordauth` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userpending`
--

DROP TABLE IF EXISTS `userpending`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userpending` (
  `userpendingid` int(10) NOT NULL AUTO_INCREMENT,
  `email` varchar(250) DEFAULT NULL,
  `password` varchar(250) DEFAULT NULL,
  `login` varchar(250) DEFAULT NULL,
  `firstname` varchar(250) DEFAULT NULL,
  `lastname` varchar(250) DEFAULT NULL,
  `locale` varchar(5) DEFAULT 'en_US',
  `labid` int(10) DEFAULT NULL,
  `status` varchar(10) DEFAULT 'PENDING',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`userpendingid`),
  KEY `fk_userpending_lid` (`labid`),
  KEY `i_userpending_status` (`status`,`email`),
  CONSTRAINT `userpending_ibfk_1` FOREIGN KEY (`labid`) REFERENCES `lab` (`labid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userpending`
--

LOCK TABLES `userpending` WRITE;
/*!40000 ALTER TABLE `userpending` DISABLE KEYS */;
/*!40000 ALTER TABLE `userpending` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userpendingmeta`
--

DROP TABLE IF EXISTS `userpendingmeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userpendingmeta` (
  `userpendingmetaid` int(10) NOT NULL AUTO_INCREMENT,
  `userpendingid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` varchar(250) DEFAULT NULL,
  `position` int(10) DEFAULT '0',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`userpendingmetaid`),
  UNIQUE KEY `u_userpendingmeta_k_lid` (`k`,`userpendingid`),
  KEY `fk_userpendingmeta_userpendingid` (`userpendingid`),
  CONSTRAINT `userpendingmeta_ibfk_1` FOREIGN KEY (`userpendingid`) REFERENCES `userpending` (`userpendingid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userpendingmeta`
--

LOCK TABLES `userpendingmeta` WRITE;
/*!40000 ALTER TABLE `userpendingmeta` DISABLE KEYS */;
/*!40000 ALTER TABLE `userpendingmeta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userrole`
--

DROP TABLE IF EXISTS `userrole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userrole` (
  `userroleid` int(10) NOT NULL AUTO_INCREMENT,
  `userid` int(10) DEFAULT NULL,
  `roleid` int(10) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`userroleid`),
  UNIQUE KEY `userrole_uid_rid` (`userid`,`roleid`),
  KEY `fk_userrole_rid` (`roleid`),
  CONSTRAINT `userrole_ibfk_1` FOREIGN KEY (`roleid`) REFERENCES `role` (`roleid`),
  CONSTRAINT `userrole_ibfk_2` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userrole`
--

LOCK TABLES `userrole` WRITE;
/*!40000 ALTER TABLE `userrole` DISABLE KEYS */;
INSERT INTO `userrole` VALUES (1,1,11,'2012-01-29 22:51:52',1);
/*!40000 ALTER TABLE `userrole` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workflow`
--

DROP TABLE IF EXISTS `workflow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workflow` (
  `workflowid` int(10) NOT NULL AUTO_INCREMENT,
  `iname` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `createts` datetime DEFAULT NULL,
  `isactive` int(1) DEFAULT '0',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`workflowid`),
  UNIQUE KEY `u_workflow_iname` (`iname`),
  UNIQUE KEY `u_workflow_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workflow`
--

LOCK TABLES `workflow` WRITE;
/*!40000 ALTER TABLE `workflow` DISABLE KEYS */;
INSERT INTO `workflow` VALUES (1,'chipSeq','Chip Seq','2012-01-29 17:50:23',0,'2012-01-29 23:02:40',0);
/*!40000 ALTER TABLE `workflow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workflowmeta`
--

DROP TABLE IF EXISTS `workflowmeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workflowmeta` (
  `workflowmetaid` int(10) NOT NULL AUTO_INCREMENT,
  `workflowid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` text,
  `position` int(10) DEFAULT '0',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`workflowmetaid`),
  UNIQUE KEY `u_workflowmeta_k_wid` (`k`,`workflowid`),
  KEY `fk_workflowmeta_workflowid` (`workflowid`),
  CONSTRAINT `workflowmeta_ibfk_1` FOREIGN KEY (`workflowid`) REFERENCES `workflow` (`workflowid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workflowmeta`
--

LOCK TABLES `workflowmeta` WRITE;
/*!40000 ALTER TABLE `workflowmeta` DISABLE KEYS */;
INSERT INTO `workflowmeta` VALUES (2,1,'workflow.submitpageflow','/jobsubmit/modifymeta/{n};/jobsubmit/resource/mps/{n};/jobsubmit/samples/{n};/jobsubmit/cells/{n};/jobsubmit/chipSeq/pair/{n};/jobsubmit/software/aligner/{n};/jobsubmit/software/peakcaller/{n};/jobsubmit/verify/{n};/jobsubmit/submit/{n};/jobsubmit/ok',0,'2012-01-29 23:02:40',0);
/*!40000 ALTER TABLE `workflowmeta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workflowresourcecategory`
--

DROP TABLE IF EXISTS `workflowresourcecategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workflowresourcecategory` (
  `workflowresourcecategoryid` int(10) NOT NULL AUTO_INCREMENT,
  `workflowid` int(10) DEFAULT NULL,
  `resourcecategoryid` int(10) DEFAULT NULL,
  PRIMARY KEY (`workflowresourcecategoryid`),
  UNIQUE KEY `u_workflowresource_wid_rcid` (`workflowid`,`resourcecategoryid`),
  KEY `fk_workflowresourcecategory_rcid` (`resourcecategoryid`),
  CONSTRAINT `workflowresourcecategory_ibfk_1` FOREIGN KEY (`resourcecategoryid`) REFERENCES `resourcecategory` (`resourcecategoryid`),
  CONSTRAINT `workflowresourcecategory_ibfk_2` FOREIGN KEY (`workflowid`) REFERENCES `workflow` (`workflowid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workflowresourcecategory`
--

LOCK TABLES `workflowresourcecategory` WRITE;
/*!40000 ALTER TABLE `workflowresourcecategory` DISABLE KEYS */;
INSERT INTO `workflowresourcecategory` VALUES (1,1,1);
/*!40000 ALTER TABLE `workflowresourcecategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workflowresourcecategorymeta`
--

DROP TABLE IF EXISTS `workflowresourcecategorymeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workflowresourcecategorymeta` (
  `workflowresourcecategorymetaid` int(10) NOT NULL AUTO_INCREMENT,
  `workflowresourcecategoryid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` text,
  `position` int(10) DEFAULT '0',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`workflowresourcecategorymetaid`),
  UNIQUE KEY `u_wro_wrcid_k` (`workflowresourcecategoryid`,`k`),
  CONSTRAINT `workflowresourcecategorymeta_ibfk_1` FOREIGN KEY (`workflowresourcecategoryid`) REFERENCES `workflowresourcecategory` (`workflowresourcecategoryid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workflowresourcecategorymeta`
--

LOCK TABLES `workflowresourcecategorymeta` WRITE;
/*!40000 ALTER TABLE `workflowresourcecategorymeta` DISABLE KEYS */;
INSERT INTO `workflowresourcecategorymeta` VALUES (1,1,'illuminaH2000.allowableUiField.readlength','10:10;20:20;',0,'2012-01-29 22:51:52',0),(2,1,'illuminaH2000.allowableUiField.readType','single:single;pair:pair;',0,'2012-01-29 22:51:52',0);
/*!40000 ALTER TABLE `workflowresourcecategorymeta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workflowsoftware`
--

DROP TABLE IF EXISTS `workflowsoftware`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workflowsoftware` (
  `workflowsoftwareid` int(10) NOT NULL AUTO_INCREMENT,
  `workflowid` int(10) DEFAULT NULL,
  `softwareid` int(10) DEFAULT NULL,
  PRIMARY KEY (`workflowsoftwareid`),
  UNIQUE KEY `u_workflowsoftware_wid_sid` (`workflowid`,`softwareid`),
  KEY `fk_workflowsoftware_sid` (`softwareid`),
  CONSTRAINT `workflowsoftware_ibfk_1` FOREIGN KEY (`softwareid`) REFERENCES `software` (`softwareid`),
  CONSTRAINT `workflowsoftware_ibfk_2` FOREIGN KEY (`workflowid`) REFERENCES `workflow` (`workflowid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workflowsoftware`
--

LOCK TABLES `workflowsoftware` WRITE;
/*!40000 ALTER TABLE `workflowsoftware` DISABLE KEYS */;
INSERT INTO `workflowsoftware` VALUES (1,1,1),(2,1,2);
/*!40000 ALTER TABLE `workflowsoftware` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workflowsoftwaremeta`
--

DROP TABLE IF EXISTS `workflowsoftwaremeta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workflowsoftwaremeta` (
  `workflowsoftwaremetaid` int(10) NOT NULL AUTO_INCREMENT,
  `workflowsoftwareid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` text,
  `position` int(10) DEFAULT '0',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`workflowsoftwaremetaid`),
  UNIQUE KEY `u_wro_wrcid_k` (`workflowsoftwareid`,`k`),
  CONSTRAINT `workflowsoftwaremeta_ibfk_1` FOREIGN KEY (`workflowsoftwareid`) REFERENCES `workflowsoftware` (`workflowsoftwareid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workflowsoftwaremeta`
--

LOCK TABLES `workflowsoftwaremeta` WRITE;
/*!40000 ALTER TABLE `workflowsoftwaremeta` DISABLE KEYS */;
/*!40000 ALTER TABLE `workflowsoftwaremeta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workflowsubtypesample`
--

DROP TABLE IF EXISTS `workflowsubtypesample`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workflowsubtypesample` (
  `workflowsubtypesampleid` int(10) NOT NULL AUTO_INCREMENT,
  `workflowid` int(10) DEFAULT NULL,
  `subtypesampleid` int(10) DEFAULT NULL,
  PRIMARY KEY (`workflowsubtypesampleid`),
  UNIQUE KEY `u_subtypesample_wid_stsid` (`workflowid`,`subtypesampleid`),
  KEY `fk_workflowsubtypesample_stsid` (`subtypesampleid`),
  CONSTRAINT `workflowsubtypesample_ibfk_1` FOREIGN KEY (`subtypesampleid`) REFERENCES `subtypesample` (`subtypesampleid`),
  CONSTRAINT `workflowsubtypesample_ibfk_2` FOREIGN KEY (`workflowid`) REFERENCES `workflow` (`workflowid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workflowsubtypesample`
--

LOCK TABLES `workflowsubtypesample` WRITE;
/*!40000 ALTER TABLE `workflowsubtypesample` DISABLE KEYS */;
INSERT INTO `workflowsubtypesample` VALUES (3,1,1),(4,1,2);
/*!40000 ALTER TABLE `workflowsubtypesample` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workflowtask`
--

DROP TABLE IF EXISTS `workflowtask`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workflowtask` (
  `workflowtaskid` int(10) NOT NULL AUTO_INCREMENT,
  `workflowid` int(10) DEFAULT NULL,
  `taskid` int(10) DEFAULT NULL,
  `iname` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`workflowtaskid`),
  UNIQUE KEY `u_workflowtask_iname` (`iname`),
  KEY `fk_workflowtask_wid` (`workflowid`),
  KEY `fk_workflowtask_tid` (`taskid`),
  CONSTRAINT `workflowtask_ibfk_1` FOREIGN KEY (`workflowid`) REFERENCES `workflow` (`workflowid`),
  CONSTRAINT `workflowtask_ibfk_2` FOREIGN KEY (`taskid`) REFERENCES `task` (`taskid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workflowtask`
--

LOCK TABLES `workflowtask` WRITE;
/*!40000 ALTER TABLE `workflowtask` DISABLE KEYS */;
/*!40000 ALTER TABLE `workflowtask` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workflowtasksource`
--

DROP TABLE IF EXISTS `workflowtasksource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workflowtasksource` (
  `workflowtasksourceid` int(10) NOT NULL AUTO_INCREMENT,
  `workflowtaskid` int(10) DEFAULT NULL,
  `sourceworkflowtaskid` int(10) DEFAULT NULL,
  PRIMARY KEY (`workflowtasksourceid`),
  KEY `fk_workflowtasksource_wid` (`workflowtaskid`),
  KEY `fk_workflowtasksource_sid` (`sourceworkflowtaskid`),
  CONSTRAINT `workflowtasksource_ibfk_1` FOREIGN KEY (`workflowtaskid`) REFERENCES `workflowtask` (`workflowtaskid`),
  CONSTRAINT `workflowtasksource_ibfk_2` FOREIGN KEY (`sourceworkflowtaskid`) REFERENCES `workflowtask` (`workflowtaskid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workflowtasksource`
--

LOCK TABLES `workflowtasksource` WRITE;
/*!40000 ALTER TABLE `workflowtasksource` DISABLE KEYS */;
/*!40000 ALTER TABLE `workflowtasksource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workflowtyperesource`
--

DROP TABLE IF EXISTS `workflowtyperesource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workflowtyperesource` (
  `workflowtyperesourceid` int(10) NOT NULL AUTO_INCREMENT,
  `workflowid` int(10) DEFAULT NULL,
  `typeresourceid` int(10) DEFAULT NULL,
  PRIMARY KEY (`workflowtyperesourceid`),
  UNIQUE KEY `u_workflowtyperesource_wid_trid` (`workflowid`,`typeresourceid`),
  KEY `fk_workflowtyperesource_trid` (`typeresourceid`),
  CONSTRAINT `workflowtyperesource_ibfk_1` FOREIGN KEY (`typeresourceid`) REFERENCES `typeresource` (`typeresourceid`),
  CONSTRAINT `workflowtyperesource_ibfk_2` FOREIGN KEY (`workflowid`) REFERENCES `workflow` (`workflowid`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workflowtyperesource`
--

LOCK TABLES `workflowtyperesource` WRITE;
/*!40000 ALTER TABLE `workflowtyperesource` DISABLE KEYS */;
INSERT INTO `workflowtyperesource` VALUES (4,1,1),(5,1,3),(6,1,4);
/*!40000 ALTER TABLE `workflowtyperesource` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-01-29 18:48:30
