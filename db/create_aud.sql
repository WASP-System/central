-- MySQL dump 10.13  Distrib 5.1.52, for pc-linux-gnu (i686)
--
-- Host: localhost    Database: wasp
-- ------------------------------------------------------
-- Server version	5.1.52

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
-- Table structure for table `REVINFO`
--

DROP TABLE IF EXISTS `REVINFO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `REVINFO` (
  `REV` int(11) NOT NULL AUTO_INCREMENT,
  `REVTSTMP` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`REV`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `a_chipseq_arun_AUD`
--

DROP TABLE IF EXISTS `a_chipseq_arun_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `a_chipseq_arun_AUD` (
  `arunId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `endts` datetime DEFAULT NULL,
  `fileid` int(11) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `startts` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `version` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`arunId`,`REV`),
  KEY `FKE9B82DA5DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `a_chipseq_arunargs_AUD`
--

DROP TABLE IF EXISTS `a_chipseq_arunargs_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `a_chipseq_arunargs_AUD` (
  `arunargsId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `argc` int(11) DEFAULT NULL,
  `argv` varchar(255) DEFAULT NULL,
  `arunid` int(11) DEFAULT NULL,
  PRIMARY KEY (`arunargsId`,`REV`),
  KEY `FKE23C3E02DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `acct_grant_AUD`
--

DROP TABLE IF EXISTS `acct_grant_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acct_grant_AUD` (
  `grantId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `expirationdt` datetime DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`grantId`,`REV`),
  KEY `FKA198D7C1DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `acct_grantjob_AUD`
--

DROP TABLE IF EXISTS `acct_grantjob_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acct_grantjob_AUD` (
  `jobId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `grantid` int(11) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  PRIMARY KEY (`jobId`,`REV`),
  KEY `FKCEA3731EDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `acct_invoice_AUD`
--

DROP TABLE IF EXISTS `acct_invoice_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acct_invoice_AUD` (
  `invoiceId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `amount` float DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `quoteid` int(11) DEFAULT NULL,
  PRIMARY KEY (`invoiceId`,`REV`),
  KEY `FK4BD28452DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `acct_jobquotecurrent_AUD`
--

DROP TABLE IF EXISTS `acct_jobquotecurrent_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acct_jobquotecurrent_AUD` (
  `jobId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `quoteid` int(11) DEFAULT NULL,
  PRIMARY KEY (`jobId`,`REV`),
  KEY `FKF4D3825FDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `acct_ledger_AUD`
--

DROP TABLE IF EXISTS `acct_ledger_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acct_ledger_AUD` (
  `ledgerId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `amount` float DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `invoiceid` int(11) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  PRIMARY KEY (`ledgerId`,`REV`),
  KEY `FK7C500266DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `acct_quote_AUD`
--

DROP TABLE IF EXISTS `acct_quote_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acct_quote_AUD` (
  `quoteId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `amount` float DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  PRIMARY KEY (`quoteId`,`REV`),
  KEY `FK91E9A521DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `acct_quoteuser_AUD`
--

DROP TABLE IF EXISTS `acct_quoteuser_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acct_quoteuser_AUD` (
  `quoteUserId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `isapproved` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `quoteid` int(11) DEFAULT NULL,
  `roleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`quoteUserId`,`REV`),
  KEY `FKECB38B6CDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `acct_workflowcost_AUD`
--

DROP TABLE IF EXISTS `acct_workflowcost_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acct_workflowcost_AUD` (
  `workflowId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `basecost` float DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  PRIMARY KEY (`workflowId`,`REV`),
  KEY `FK99AA6A89DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `barcode_AUD`
--

DROP TABLE IF EXISTS `barcode_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `barcode_AUD` (
  `barcodeId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `barcode` varchar(255) DEFAULT NULL,
  `barcodefor` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  PRIMARY KEY (`barcodeId`,`REV`),
  KEY `FK6221D651DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `department_AUD`
--

DROP TABLE IF EXISTS `department_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `department_AUD` (
  `departmentId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `isinternal` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`departmentId`,`REV`),
  KEY `FKF0647823DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `departmentuser_AUD`
--

DROP TABLE IF EXISTS `departmentuser_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `departmentuser_AUD` (
  `departmentUserId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `departmentid` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  PRIMARY KEY (`departmentUserId`,`REV`),
  KEY `FK1BA90D6EDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `file_AUD`
--

DROP TABLE IF EXISTS `file_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file_AUD` (
  `fileId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `contenttype` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `filelocation` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `isarchived` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `md5hash` varchar(255) DEFAULT NULL,
  `sizek` int(11) DEFAULT NULL,
  PRIMARY KEY (`fileId`,`REV`),
  KEY `FKD42D054DDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `job_AUD`
--

DROP TABLE IF EXISTS `job_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `job_AUD` (
  `jobId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `createts` datetime DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `viewablebylab` int(11) DEFAULT NULL,
  `workflowid` int(11) DEFAULT NULL,
  PRIMARY KEY (`jobId`,`REV`),
  KEY `FKAA4FA30EDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jobfile_AUD`
--

DROP TABLE IF EXISTS `jobfile_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jobfile_AUD` (
  `jobFileId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `fileid` int(11) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`jobFileId`,`REV`),
  KEY `FK28E9940ADF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jobmeta_AUD`
--

DROP TABLE IF EXISTS `jobmeta_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jobmeta_AUD` (
  `jobMetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `v` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`jobMetaId`,`REV`),
  KEY `FK39E74813DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jobsample_AUD`
--

DROP TABLE IF EXISTS `jobsample_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jobsample_AUD` (
  `jobSampleId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`jobSampleId`,`REV`),
  KEY `FK206091F8DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jobsamplemeta_AUD`
--

DROP TABLE IF EXISTS `jobsamplemeta_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jobsamplemeta_AUD` (
  `jobSamplemetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `jobsampleid` int(11) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `v` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`jobSamplemetaId`,`REV`),
  KEY `FK4EB2B1FDDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `jobuser_AUD`
--

DROP TABLE IF EXISTS `jobuser_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jobuser_AUD` (
  `jobUserId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `roleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`jobUserId`,`REV`),
  KEY `FK44DECAD9DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lab_AUD`
--

DROP TABLE IF EXISTS `lab_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lab_AUD` (
  `labId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `departmentid` int(11) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `primaryuserid` int(11) DEFAULT NULL,
  PRIMARY KEY (`labId`,`REV`),
  KEY `FKFC3840DEDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `labmeta_AUD`
--

DROP TABLE IF EXISTS `labmeta_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `labmeta_AUD` (
  `labmetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  PRIMARY KEY (`labmetaId`,`REV`),
  KEY `FK7AC7DE3DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `labuser_AUD`
--

DROP TABLE IF EXISTS `labuser_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `labuser_AUD` (
  `labUserId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `roleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`labUserId`,`REV`),
  KEY `FK12A400A9DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `meta_AUD`
--

DROP TABLE IF EXISTS `meta_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meta_AUD` (
  `metaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `property` varchar(255) DEFAULT NULL,
  `v` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`metaId`,`REV`),
  KEY `FKE52AB956DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `project_AUD`
--

DROP TABLE IF EXISTS `project_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project_AUD` (
  `projectId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`projectId`,`REV`),
  KEY `FKC7FF446ADF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `resource_AUD`
--

DROP TABLE IF EXISTS `resource_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resource_AUD` (
  `resourceId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `platform` varchar(255) DEFAULT NULL,
  `typeresourceid` int(11) DEFAULT NULL,
  PRIMARY KEY (`resourceId`,`REV`),
  KEY `FKE91B3ADFDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `resourcebarcode_AUD`
--

DROP TABLE IF EXISTS `resourcebarcode_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resourcebarcode_AUD` (
  `resourceBarcodeId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `barcodeid` int(11) DEFAULT NULL,
  `resourceid` int(11) DEFAULT NULL,
  PRIMARY KEY (`resourceBarcodeId`,`REV`),
  KEY `FK7BEA9D83DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `resourcelane_AUD`
--

DROP TABLE IF EXISTS `resourcelane_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resourcelane_AUD` (
  `resourceLaneId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `resourceid` int(11) DEFAULT NULL,
  PRIMARY KEY (`resourceLaneId`,`REV`),
  KEY `FK3111BFCBDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `resourcemeta_AUD`
--

DROP TABLE IF EXISTS `resourcemeta_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resourcemeta_AUD` (
  `resourceMetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `resourceid` int(11) DEFAULT NULL,
  `v` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`resourceMetaId`,`REV`),
  KEY `FK76908F64DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role_AUD`
--

DROP TABLE IF EXISTS `role_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_AUD` (
  `roleId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `domain` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `rolename` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`roleId`,`REV`),
  KEY `FKF0208347DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `roleset_AUD`
--

DROP TABLE IF EXISTS `roleset_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roleset_AUD` (
  `rolesetId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `childroleid` int(11) DEFAULT NULL,
  `parentroleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`rolesetId`,`REV`),
  KEY `FK38BA17FDDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `run_AUD`
--

DROP TABLE IF EXISTS `run_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `run_AUD` (
  `runId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `endts` datetime DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `resourceid` int(11) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  `startts` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`runId`,`REV`),
  KEY `FK5C67AADCDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `runfile_AUD`
--

DROP TABLE IF EXISTS `runfile_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `runfile_AUD` (
  `runlanefileId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `fileid` int(11) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `runid` int(11) DEFAULT NULL,
  PRIMARY KEY (`runlanefileId`,`REV`),
  KEY `FK7CFD04D8DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `runlane_AUD`
--

DROP TABLE IF EXISTS `runlane_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `runlane_AUD` (
  `runLaneId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `resourcelaneid` int(11) DEFAULT NULL,
  `runid` int(11) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`runLaneId`,`REV`),
  KEY `FK487BE948DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `runlanefile_AUD`
--

DROP TABLE IF EXISTS `runlanefile_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `runlanefile_AUD` (
  `runLanefileId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `fileid` int(11) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `runlaneid` int(11) DEFAULT NULL,
  PRIMARY KEY (`runLanefileId`,`REV`),
  KEY `FKAEB42D44DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `runmeta_AUD`
--

DROP TABLE IF EXISTS `runmeta_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `runmeta_AUD` (
  `runMetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `runid` int(11) DEFAULT NULL,
  `v` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`runMetaId`,`REV`),
  KEY `FK8DFAB8E1DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sample_AUD`
--

DROP TABLE IF EXISTS `sample_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sample_AUD` (
  `sampleId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `isgood` int(11) DEFAULT NULL,
  `isreceived` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `receivedts` datetime DEFAULT NULL,
  `receiver_userid` int(11) DEFAULT NULL,
  `submitter_jobid` int(11) DEFAULT NULL,
  `submitter_labid` int(11) DEFAULT NULL,
  `submitter_userid` int(11) DEFAULT NULL,
  `typesampleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`sampleId`,`REV`),
  KEY `FK88CBE7BDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `samplebarcode_AUD`
--

DROP TABLE IF EXISTS `samplebarcode_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samplebarcode_AUD` (
  `sampleBarcode` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `barcodeid` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`sampleBarcode`,`REV`),
  KEY `FKD2871267DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `samplefile_AUD`
--

DROP TABLE IF EXISTS `samplefile_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samplefile_AUD` (
  `sampleFileId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `fileid` int(11) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`sampleFileId`,`REV`),
  KEY `FKA32130F7DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `samplelab_AUD`
--

DROP TABLE IF EXISTS `samplelab_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samplelab_AUD` (
  `sampleLabId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `isprimary` int(11) DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`sampleLabId`,`REV`),
  KEY `FKD99AF7F4DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `samplemeta_AUD`
--

DROP TABLE IF EXISTS `samplemeta_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samplemeta_AUD` (
  `sampleMetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  `v` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`sampleMetaId`,`REV`),
  KEY `FKB41EE500DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `samplesource_AUD`
--

DROP TABLE IF EXISTS `samplesource_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samplesource_AUD` (
  `sampleSourceId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `multiplexindex` int(11) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  `source_sampleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`sampleSourceId`,`REV`),
  KEY `FKA05CF996DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `state_AUD`
--

DROP TABLE IF EXISTS `state_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `state_AUD` (
  `stateId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `endts` datetime DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `startts` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `taskid` int(11) DEFAULT NULL,
  PRIMARY KEY (`stateId`,`REV`),
  KEY `FK83B041E2DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `state_a_chipseq_arun_AUD`
--

DROP TABLE IF EXISTS `state_a_chipseq_arun_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `state_a_chipseq_arun_AUD` (
  `stateArunId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `arunid` int(11) DEFAULT NULL,
  `stateid` int(11) DEFAULT NULL,
  PRIMARY KEY (`stateArunId`,`REV`),
  KEY `FK1FF39C13DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `statejob_AUD`
--

DROP TABLE IF EXISTS `statejob_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `statejob_AUD` (
  `statejobId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  `stateid` int(11) DEFAULT NULL,
  PRIMARY KEY (`statejobId`,`REV`),
  KEY `FK516AC95DDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `statemeta_AUD`
--

DROP TABLE IF EXISTS `statemeta_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `statemeta_AUD` (
  `statemetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `stateid` int(11) DEFAULT NULL,
  `v` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`statemetaId`,`REV`),
  KEY `FK21745CE7DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `staterun_AUD`
--

DROP TABLE IF EXISTS `staterun_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `staterun_AUD` (
  `staterunId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `runid` int(11) DEFAULT NULL,
  `stateid` int(11) DEFAULT NULL,
  PRIMARY KEY (`staterunId`,`REV`),
  KEY `FK382D12BDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `staterunlane_AUD`
--

DROP TABLE IF EXISTS `staterunlane_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `staterunlane_AUD` (
  `staterunlaneId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `runlaneid` int(11) DEFAULT NULL,
  `stateid` int(11) DEFAULT NULL,
  PRIMARY KEY (`staterunlaneId`,`REV`),
  KEY `FK5ED5017DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `statesample_AUD`
--

DROP TABLE IF EXISTS `statesample_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `statesample_AUD` (
  `statesampleId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  `stateid` int(11) DEFAULT NULL,
  PRIMARY KEY (`statesampleId`,`REV`),
  KEY `FK58FBC1CCDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `task_AUD`
--

DROP TABLE IF EXISTS `task_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_AUD` (
  `taskId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`taskId`,`REV`),
  KEY `FKE79218D6DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `typeresource_AUD`
--

DROP TABLE IF EXISTS `typeresource_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `typeresource_AUD` (
  `typeResourceId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`typeResourceId`,`REV`),
  KEY `FK82A2C319DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `typesample_AUD`
--

DROP TABLE IF EXISTS `typesample_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `typesample_AUD` (
  `typeSampleId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`typeSampleId`,`REV`),
  KEY `FK38ED0D35DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_AUD`
--

DROP TABLE IF EXISTS `user_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_AUD` (
  `UserId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `locale` varchar(255) DEFAULT NULL,
  `login` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`UserId`,`REV`),
  KEY `FKF0223C1CDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usermeta_AUD`
--

DROP TABLE IF EXISTS `usermeta_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usermeta_AUD` (
  `usermetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  PRIMARY KEY (`usermetaId`,`REV`),
  KEY `FKB38AAA21DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `userrole_AUD`
--

DROP TABLE IF EXISTS `userrole_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userrole_AUD` (
  `userroleId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `roleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`userroleId`,`REV`),
  KEY `FKBE807412DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `workflow_AUD`
--

DROP TABLE IF EXISTS `workflow_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workflow_AUD` (
  `workflowId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `createts` datetime DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`workflowId`,`REV`),
  KEY `FK5D080E10DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `workflowmeta_AUD`
--

DROP TABLE IF EXISTS `workflowmeta_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workflowmeta_AUD` (
  `workflowmetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `v` varchar(255) DEFAULT NULL,
  `workflowid` int(11) DEFAULT NULL,
  PRIMARY KEY (`workflowmetaId`,`REV`),
  KEY `FKB48E6215DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `workflowtask_AUD`
--

DROP TABLE IF EXISTS `workflowtask_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workflowtask_AUD` (
  `workflowtaskId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `taskid` int(11) DEFAULT NULL,
  `workflowid` int(11) DEFAULT NULL,
  PRIMARY KEY (`workflowtaskId`,`REV`),
  KEY `FKB6F5C195DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `workflowtasksource_AUD`
--

DROP TABLE IF EXISTS `workflowtasksource_AUD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workflowtasksource_AUD` (
  `workflowtasksourceId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `sourceworkflowtaskid` int(11) DEFAULT NULL,
  `workflowtaskid` int(11) DEFAULT NULL,
  PRIMARY KEY (`workflowtasksourceId`,`REV`),
  KEY `FK2E19C130DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-08-17 10:54:31
