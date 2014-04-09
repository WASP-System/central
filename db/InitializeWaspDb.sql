-- Recreate Database 
-- GRANT USAGE ON *.* TO 'wasp'@'localhost';
-- DROP USER 'wasp'@'localhost';
DROP DATABASE IF EXISTS wasp;

create database wasp CHARACTER SET utf8 COLLATE utf8_general_ci;
-- create user 'wasp'@'localhost' IDENTIFIED BY 'waspV2';

-- grant all on wasp.* to 'wasp'@'localhost';

-- flush privileges;

use wasp;

# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.1.70)
# Database: wasp
# Generation Time: 2013-10-07 20:45:40 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table acct_grant
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_grant`;

CREATE TABLE `acct_grant` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `expirationdt` datetime DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKB2AF21F050566623` (`lastupdatebyuser`),
  KEY `FKB2AF21F08FE41BFE` (`labid`),
  CONSTRAINT `FKB2AF21F08FE41BFE` FOREIGN KEY (`labid`) REFERENCES `lab` (`id`),
  CONSTRAINT `FKB2AF21F050566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table acct_grantjob
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_grantjob`;

CREATE TABLE `acct_grantjob` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `grantid` int(11) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKAA5FF3CD50566623` (`lastupdatebyuser`),
  KEY `FKAA5FF3CDF2431449` (`grantid`),
  KEY `FKAA5FF3CD5E117DCB` (`jobid`),
  CONSTRAINT `FKAA5FF3CD5E117DCB` FOREIGN KEY (`jobid`) REFERENCES `acct_ledger` (`id`),
  CONSTRAINT `FKAA5FF3CD50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FKAA5FF3CDF2431449` FOREIGN KEY (`grantid`) REFERENCES `acct_grant` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table acct_invoice
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_invoice`;

CREATE TABLE `acct_invoice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `amount` float DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  `quoteid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK278FBF0150566623` (`lastupdatebyuser`),
  KEY `FK278FBF018FCE445E` (`jobid`),
  KEY `FK278FBF019B63709` (`quoteid`),
  CONSTRAINT `FK278FBF019B63709` FOREIGN KEY (`quoteid`) REFERENCES `acct_quote` (`id`),
  CONSTRAINT `FK278FBF0150566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FK278FBF018FCE445E` FOREIGN KEY (`jobid`) REFERENCES `job` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table acct_ledger
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_ledger`;

CREATE TABLE `acct_ledger` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `amount` float DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `invoiceid` int(11) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKAB07671550566623` (`lastupdatebyuser`),
  KEY `FKAB0767158FCE445E` (`jobid`),
  KEY `FKAB076715B2EEB2AB` (`invoiceid`),
  CONSTRAINT `FKAB076715B2EEB2AB` FOREIGN KEY (`invoiceid`) REFERENCES `acct_invoice` (`id`),
  CONSTRAINT `FKAB07671550566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FKAB0767158FCE445E` FOREIGN KEY (`jobid`) REFERENCES `job` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table acct_quote
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_quote`;

CREATE TABLE `acct_quote` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `amount` float DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKB33D9F5050566623` (`lastupdatebyuser`),
  KEY `FKB33D9F508FCE445E` (`jobid`),
  KEY `FKB33D9F507CFE8408` (`userid`),
  CONSTRAINT `FKB33D9F507CFE8408` FOREIGN KEY (`userid`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FKB33D9F5050566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FKB33D9F508FCE445E` FOREIGN KEY (`jobid`) REFERENCES `job` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table acct_quotemeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_quotemeta`;

CREATE TABLE `acct_quotemeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `rolevisibility` varchar(255) DEFAULT NULL,
  `v` longtext,
  `quoteid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK91F08D5550566623` (`lastupdatebyuser`),
  KEY `FK91F08D559B63709` (`quoteid`),
  KEY `FK91F08D5550566623k` (`k`),
  CONSTRAINT `FK91F08D559B63709` FOREIGN KEY (`quoteid`) REFERENCES `acct_quote` (`id`),
  CONSTRAINT `FK91F08D5550566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table acct_quoteuser
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_quoteuser`;

CREATE TABLE `acct_quoteuser` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `isapproved` int(11) DEFAULT NULL,
  `quoteid` int(11) DEFAULT NULL,
  `roleid` int(11) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK91F4631B50566623` (`lastupdatebyuser`),
  KEY `FK91F4631B77A92E9E` (`roleid`),
  KEY `FK91F4631B9B63709` (`quoteid`),
  KEY `FK91F4631B7CFE8408` (`userid`),
  CONSTRAINT `FK91F4631B7CFE8408` FOREIGN KEY (`userid`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FK91F4631B50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FK91F4631B77A92E9E` FOREIGN KEY (`roleid`) REFERENCES `wrole` (`id`),
  CONSTRAINT `FK91F4631B9B63709` FOREIGN KEY (`quoteid`) REFERENCES `acct_quote` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table acct_workflowcost
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_workflowcost`;

CREATE TABLE `acct_workflowcost` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `basecost` float DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  `workflowid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKC3C958B850566623` (`lastupdatebyuser`),
  KEY `FKC3C958B8744987E0` (`workflowid`),
  CONSTRAINT `FKC3C958B8744987E0` FOREIGN KEY (`workflowid`) REFERENCES `job` (`id`),
  CONSTRAINT `FKC3C958B850566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table adaptor
# ------------------------------------------------------------

DROP TABLE IF EXISTS `adaptor`;

CREATE TABLE `adaptor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `adaptorsetid` int(11) DEFAULT NULL,
  `barcodenumber` int(11) DEFAULT NULL,
  `barcodesequence` varchar(255) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sequence` varchar(255) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKBB6CA48550566623` (`lastupdatebyuser`),
  KEY `FKBB6CA485DA18E62C` (`adaptorsetid`),
  CONSTRAINT `FKBB6CA485DA18E62C` FOREIGN KEY (`adaptorsetid`) REFERENCES `adaptorset` (`id`),
  CONSTRAINT `FKBB6CA48550566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table adaptormeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `adaptormeta`;

CREATE TABLE `adaptormeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `rolevisibility` varchar(255) DEFAULT NULL,
  `v` longtext,
  `adaptorid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK33FFF00A50566623` (`lastupdatebyuser`),
  KEY `FK33FFF00AF88E976E` (`adaptorid`),
  KEY `FK33FFF00A50566623k` (`k`),
  CONSTRAINT `FK33FFF00AF88E976E` FOREIGN KEY (`adaptorid`) REFERENCES `adaptor` (`id`),
  CONSTRAINT `FK33FFF00A50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table adaptorset
# ------------------------------------------------------------

DROP TABLE IF EXISTS `adaptorset`;

CREATE TABLE `adaptorset` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sampletypeid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKC7DF0DBD50566623` (`lastupdatebyuser`),
  KEY `FKC7DF0DBDDBE90DA` (`sampletypeid`),
  CONSTRAINT `FKC7DF0DBDDBE90DA` FOREIGN KEY (`sampletypeid`) REFERENCES `sampletype` (`id`),
  CONSTRAINT `FKC7DF0DBD50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table adaptorsetmeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `adaptorsetmeta`;

CREATE TABLE `adaptorsetmeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `rolevisibility` varchar(255) DEFAULT NULL,
  `v` longtext,
  `adaptorsetid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK822AFD4250566623` (`lastupdatebyuser`),
  KEY `FK822AFD42DA18E62C` (`adaptorsetid`),
  KEY `FK822AFD4250566623k` (`k`),
  CONSTRAINT `FK822AFD42DA18E62C` FOREIGN KEY (`adaptorsetid`) REFERENCES `adaptorset` (`id`),
  CONSTRAINT `FK822AFD4250566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table adaptorsetresourcecategory
# ------------------------------------------------------------

DROP TABLE IF EXISTS `adaptorsetresourcecategory`;

CREATE TABLE `adaptorsetresourcecategory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `adaptorsetid` int(11) DEFAULT NULL,
  `resourcecategoryid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKEBF686C950566623` (`lastupdatebyuser`),
  KEY `FKEBF686C9FBCBFFEA` (`resourcecategoryid`),
  KEY `FKEBF686C9DA18E62C` (`adaptorsetid`),
  CONSTRAINT `FKEBF686C9DA18E62C` FOREIGN KEY (`adaptorsetid`) REFERENCES `adaptorset` (`id`),
  CONSTRAINT `FKEBF686C950566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FKEBF686C9FBCBFFEA` FOREIGN KEY (`resourcecategoryid`) REFERENCES `resourcecategory` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table barcode
# ------------------------------------------------------------

DROP TABLE IF EXISTS `barcode`;

CREATE TABLE `barcode` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `barcode` varchar(255) DEFAULT NULL,
  `barcodefor` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKEC1DE88050566623` (`lastupdatebyuser`),
  CONSTRAINT `FKEC1DE88050566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table confirmemailauth
# ------------------------------------------------------------

DROP TABLE IF EXISTS `confirmemailauth`;

CREATE TABLE `confirmemailauth` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `authcode` varchar(255) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `userpendingid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3E6FAC8450566623` (`lastupdatebyuser`),
  KEY `FK3E6FAC84FF4502DC` (`userpendingid`),
  KEY `FK3E6FAC847CFE8408` (`userid`),
  CONSTRAINT `FK3E6FAC847CFE8408` FOREIGN KEY (`userid`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FK3E6FAC8450566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FK3E6FAC84FF4502DC` FOREIGN KEY (`userpendingid`) REFERENCES `userpending` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table department
# ------------------------------------------------------------

DROP TABLE IF EXISTS `department`;

CREATE TABLE `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `isinternal` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK328E435250566623` (`lastupdatebyuser`),
  CONSTRAINT `FK328E435250566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table departmentuser
# ------------------------------------------------------------

DROP TABLE IF EXISTS `departmentuser`;

CREATE TABLE `departmentuser` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `departmentid` int(11) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKF06F361D50566623` (`lastupdatebyuser`),
  KEY `FKF06F361DC0804016` (`departmentid`),
  KEY `FKF06F361D7CFE8408` (`userid`),
  CONSTRAINT `FKF06F361D7CFE8408` FOREIGN KEY (`userid`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FKF06F361D50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FKF06F361DC0804016` FOREIGN KEY (`departmentid`) REFERENCES `department` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table file
# ------------------------------------------------------------

DROP TABLE IF EXISTS `file`;

CREATE TABLE `file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `archived` tinyint(1) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_uri` longtext,
  `md5hash` varchar(255) DEFAULT NULL,
  `sizek` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  `filetypeid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2FF57C50566623` (`lastupdatebyuser`),
  KEY `FK2FF57C175EFBBE` (`filetypeid`),
  CONSTRAINT `FK2FF57C175EFBBE` FOREIGN KEY (`filetypeid`) REFERENCES `filetype` (`id`),
  CONSTRAINT `FK2FF57C50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table filegroup
# ------------------------------------------------------------

DROP TABLE IF EXISTS `filegroup`;

CREATE TABLE `filegroup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `description` longtext,
  `filetypeid` int(11) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `isarchived` int(11) DEFAULT NULL,
  `softwaregeneratedbyid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKB1FB0E8350566623` (`lastupdatebyuser`),
  KEY `FKB1FB0E83175EFBBE` (`filetypeid`),
  KEY `FKB1FB0E83896543B8` (`softwaregeneratedbyid`),
  CONSTRAINT `FKB1FB0E83896543B8` FOREIGN KEY (`softwaregeneratedbyid`) REFERENCES `software` (`id`),
  CONSTRAINT `FKB1FB0E83175EFBBE` FOREIGN KEY (`filetypeid`) REFERENCES `filetype` (`id`),
  CONSTRAINT `FKB1FB0E8350566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table filegroup_rel
# ------------------------------------------------------------

DROP TABLE IF EXISTS `filegroup_rel`;

CREATE TABLE `filegroup_rel` (
  `childfilegroupid` int(11) NOT NULL,
  `filegroupid` int(11) NOT NULL,
  PRIMARY KEY (`filegroupid`,`childfilegroupid`),
  KEY `FK926FCDD181C758A` (`filegroupid`),
  KEY `FK926FCDD194B76EE` (`childfilegroupid`),
  CONSTRAINT `FK926FCDD194B76EE` FOREIGN KEY (`childfilegroupid`) REFERENCES `filegroup` (`id`),
  CONSTRAINT `FK926FCDD181C758A` FOREIGN KEY (`filegroupid`) REFERENCES `filegroup` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table filegroupmeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `filegroupmeta`;

CREATE TABLE `filegroupmeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `rolevisibility` varchar(255) DEFAULT NULL,
  `v` longtext,
  `filegroupid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK92D2B08181C758A` (`filegroupid`),
  KEY `FK92D2B0850566623` (`lastupdatebyuser`),
  KEY `FK92D2B08181C758Ak` (`k`),
  CONSTRAINT `FK92D2B0850566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FK92D2B08181C758A` FOREIGN KEY (`filegroupid`) REFERENCES `filegroup` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table filemeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `filemeta`;

CREATE TABLE `filemeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `rolevisibility` varchar(255) DEFAULT NULL,
  `v` longtext,
  `fileid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKD433ED8150566623` (`lastupdatebyuser`),
  KEY `FKD433ED81D1800F32` (`fileid`),
  KEY `FKD433ED8150566623k` (`k`),
  CONSTRAINT `FKD433ED81D1800F32` FOREIGN KEY (`fileid`) REFERENCES `file` (`id`),
  CONSTRAINT `FKD433ED8150566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table filetype
# ------------------------------------------------------------

DROP TABLE IF EXISTS `filetype`;

CREATE TABLE `filetype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `extension` varchar(255) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKD43766B650566623` (`lastupdatebyuser`),
  CONSTRAINT `FKD43766B650566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table filetypemeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `filetypemeta`;

CREATE TABLE `filetypemeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `rolevisibility` varchar(255) DEFAULT NULL,
  `v` longtext,
  `filetypeid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK884C31BB50566623` (`lastupdatebyuser`),
  KEY `FK884C31BB175EFBBE` (`filetypeid`),
  KEY `FK884C31BB50566623k` (`k`),
  CONSTRAINT `FK884C31BB175EFBBE` FOREIGN KEY (`filetypeid`) REFERENCES `filetype` (`id`),
  CONSTRAINT `FK884C31BB50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table groupfile
# ------------------------------------------------------------

DROP TABLE IF EXISTS `groupfile`;

CREATE TABLE `groupfile` (
  `groupid` int(11) NOT NULL,
  `fileid` int(11) NOT NULL,
  PRIMARY KEY (`groupid`,`fileid`),
  KEY `FKA7A3947BA284E86` (`groupid`),
  KEY `FKA7A3947BD1800F32` (`fileid`),
  CONSTRAINT `FKA7A3947BD1800F32` FOREIGN KEY (`fileid`) REFERENCES `file` (`id`),
  CONSTRAINT `FKA7A3947BA284E86` FOREIGN KEY (`groupid`) REFERENCES `filegroup` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table job
# ------------------------------------------------------------

DROP TABLE IF EXISTS `job`;

CREATE TABLE `job` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `createts` datetime DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `viewablebylab` int(11) DEFAULT NULL,
  `workflowid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  `current_quote` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK19BBD50566623` (`lastupdatebyuser`),
  KEY `FK19BBD8FE41BFE` (`labid`),
  KEY `FK19BBD7C243AA8` (`current_quote`),
  KEY `FK19BBD7CFE8408` (`userid`),
  KEY `FK19BBD8BDE4B70` (`workflowid`),
  CONSTRAINT `FK19BBD8BDE4B70` FOREIGN KEY (`workflowid`) REFERENCES `workflow` (`id`),
  CONSTRAINT `FK19BBD50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FK19BBD7C243AA8` FOREIGN KEY (`current_quote`) REFERENCES `acct_quote` (`id`),
  CONSTRAINT `FK19BBD7CFE8408` FOREIGN KEY (`userid`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FK19BBD8FE41BFE` FOREIGN KEY (`labid`) REFERENCES `lab` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table jobcellselection
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobcellselection`;

CREATE TABLE `jobcellselection` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `cellindex` int(11) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK926175CD50566623` (`lastupdatebyuser`),
  KEY `FK926175CD8FCE445E` (`jobid`),
  CONSTRAINT `FK926175CD8FCE445E` FOREIGN KEY (`jobid`) REFERENCES `job` (`id`),
  CONSTRAINT `FK926175CD50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table jobdraft
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobdraft`;

CREATE TABLE `jobdraft` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `createts` datetime DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `submittedjobid` int(11) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `workflowid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKA001AC0450566623` (`lastupdatebyuser`),
  KEY `FKA001AC048FE41BFE` (`labid`),
  KEY `FKA001AC045C574043` (`submittedjobid`),
  KEY `FKA001AC047CFE8408` (`userid`),
  KEY `FKA001AC048BDE4B70` (`workflowid`),
  CONSTRAINT `FKA001AC048BDE4B70` FOREIGN KEY (`workflowid`) REFERENCES `workflow` (`id`),
  CONSTRAINT `FKA001AC0450566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FKA001AC045C574043` FOREIGN KEY (`submittedjobid`) REFERENCES `job` (`id`),
  CONSTRAINT `FKA001AC047CFE8408` FOREIGN KEY (`userid`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FKA001AC048FE41BFE` FOREIGN KEY (`labid`) REFERENCES `lab` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table jobdraftcellselection
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobdraftcellselection`;

CREATE TABLE `jobdraftcellselection` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `cellindex` int(11) DEFAULT NULL,
  `jobdraftid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK737E50E650566623` (`lastupdatebyuser`),
  KEY `FK737E50E6E3C3069A` (`jobdraftid`),
  CONSTRAINT `FK737E50E6E3C3069A` FOREIGN KEY (`jobdraftid`) REFERENCES `jobdraft` (`id`),
  CONSTRAINT `FK737E50E650566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table jobdraftfile
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobdraftfile`;

CREATE TABLE `jobdraftfile` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `filegroupid` int(11) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `jobdraftid` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2FB3FF80181C758A` (`filegroupid`),
  KEY `FK2FB3FF8050566623` (`lastupdatebyuser`),
  KEY `FK2FB3FF80E3C3069A` (`jobdraftid`),
  CONSTRAINT `FK2FB3FF80E3C3069A` FOREIGN KEY (`jobdraftid`) REFERENCES `jobdraft` (`id`),
  CONSTRAINT `FK2FB3FF80181C758A` FOREIGN KEY (`filegroupid`) REFERENCES `filegroup` (`id`),
  CONSTRAINT `FK2FB3FF8050566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table jobdraftmeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobdraftmeta`;

CREATE TABLE `jobdraftmeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `rolevisibility` varchar(255) DEFAULT NULL,
  `v` longtext,
  `jobdraftid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2FB7200950566623` (`lastupdatebyuser`),
  KEY `FK2FB72009E3C3069A` (`jobdraftid`),
  KEY `FK2FB7200950566623k` (`k`),
  CONSTRAINT `FK2FB72009E3C3069A` FOREIGN KEY (`jobdraftid`) REFERENCES `jobdraft` (`id`),
  CONSTRAINT `FK2FB7200950566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table jobdraftresourcecategory
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobdraftresourcecategory`;

CREATE TABLE `jobdraftresourcecategory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `jobdraftid` int(11) DEFAULT NULL,
  `resourcecategoryid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7639B71050566623` (`lastupdatebyuser`),
  KEY `FK7639B710E3C3069A` (`jobdraftid`),
  KEY `FK7639B710FBCBFFEA` (`resourcecategoryid`),
  CONSTRAINT `FK7639B710FBCBFFEA` FOREIGN KEY (`resourcecategoryid`) REFERENCES `resourcecategory` (`id`),
  CONSTRAINT `FK7639B71050566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FK7639B710E3C3069A` FOREIGN KEY (`jobdraftid`) REFERENCES `jobdraft` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table jobdraftsoftware
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobdraftsoftware`;

CREATE TABLE `jobdraftsoftware` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `jobdraftid` int(11) DEFAULT NULL,
  `softwareid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKA94AC9AB50566623` (`lastupdatebyuser`),
  KEY `FKA94AC9AB21328540` (`softwareid`),
  KEY `FKA94AC9ABE3C3069A` (`jobdraftid`),
  CONSTRAINT `FKA94AC9ABE3C3069A` FOREIGN KEY (`jobdraftid`) REFERENCES `jobdraft` (`id`),
  CONSTRAINT `FKA94AC9AB21328540` FOREIGN KEY (`softwareid`) REFERENCES `software` (`id`),
  CONSTRAINT `FKA94AC9AB50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table jobfile
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobfile`;

CREATE TABLE `jobfile` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `filegroupid` int(11) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKAA536AB9181C758A` (`filegroupid`),
  KEY `FKAA536AB950566623` (`lastupdatebyuser`),
  KEY `FKAA536AB98FCE445E` (`jobid`),
  CONSTRAINT `FKAA536AB98FCE445E` FOREIGN KEY (`jobid`) REFERENCES `job` (`id`),
  CONSTRAINT `FKAA536AB9181C758A` FOREIGN KEY (`filegroupid`) REFERENCES `filegroup` (`id`),
  CONSTRAINT `FKAA536AB950566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table jobmeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobmeta`;

CREATE TABLE `jobmeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `rolevisibility` varchar(255) DEFAULT NULL,
  `v` longtext,
  `jobid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKAA568B4250566623` (`lastupdatebyuser`),
  KEY `FKAA568B428FCE445E` (`jobid`),
  KEY `FKAA568B4250566623k` (`k`),
  CONSTRAINT `FKAA568B428FCE445E` FOREIGN KEY (`jobid`) REFERENCES `job` (`id`),
  CONSTRAINT `FKAA568B4250566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table jobresourcecategory
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobresourcecategory`;

CREATE TABLE `jobresourcecategory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  `resourcecategoryid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKD93D14C950566623` (`lastupdatebyuser`),
  KEY `FKD93D14C98FCE445E` (`jobid`),
  KEY `FKD93D14C9FBCBFFEA` (`resourcecategoryid`),
  CONSTRAINT `FKD93D14C9FBCBFFEA` FOREIGN KEY (`resourcecategoryid`) REFERENCES `resourcecategory` (`id`),
  CONSTRAINT `FKD93D14C950566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FKD93D14C98FCE445E` FOREIGN KEY (`jobid`) REFERENCES `job` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table jobsample
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobsample`;

CREATE TABLE `jobsample` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK78E28FA7B8A37246` (`sampleid`),
  KEY `FK78E28FA750566623` (`lastupdatebyuser`),
  KEY `FK78E28FA78FCE445E` (`jobid`),
  CONSTRAINT `FK78E28FA78FCE445E` FOREIGN KEY (`jobid`) REFERENCES `job` (`id`),
  CONSTRAINT `FK78E28FA750566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FK78E28FA7B8A37246` FOREIGN KEY (`sampleid`) REFERENCES `sample` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table jobsamplemeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobsamplemeta`;

CREATE TABLE `jobsamplemeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `rolevisibility` varchar(255) DEFAULT NULL,
  `v` longtext,
  `jobsampleid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK20677A2C50566623` (`lastupdatebyuser`),
  KEY `FK20677A2C55379B12` (`jobsampleid`),
  KEY `FK20677A2C50566623k` (`k`),
  CONSTRAINT `FK20677A2C55379B12` FOREIGN KEY (`jobsampleid`) REFERENCES `jobsample` (`id`),
  CONSTRAINT `FK20677A2C50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table jobsoftware
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobsoftware`;

CREATE TABLE `jobsoftware` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  `softwareid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKA35FF06450566623` (`lastupdatebyuser`),
  KEY `FKA35FF06421328540` (`softwareid`),
  KEY `FKA35FF0648FCE445E` (`jobid`),
  CONSTRAINT `FKA35FF0648FCE445E` FOREIGN KEY (`jobid`) REFERENCES `job` (`id`),
  CONSTRAINT `FKA35FF06421328540` FOREIGN KEY (`softwareid`) REFERENCES `software` (`id`),
  CONSTRAINT `FKA35FF06450566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table jobuser
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobuser`;

CREATE TABLE `jobuser` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  `roleid` int(11) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKAA5A610850566623` (`lastupdatebyuser`),
  KEY `FKAA5A610877A92E9E` (`roleid`),
  KEY `FKAA5A61088FCE445E` (`jobid`),
  KEY `FKAA5A61087CFE8408` (`userid`),
  CONSTRAINT `FKAA5A61087CFE8408` FOREIGN KEY (`userid`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FKAA5A610850566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FKAA5A610877A92E9E` FOREIGN KEY (`roleid`) REFERENCES `wrole` (`id`),
  CONSTRAINT `FKAA5A61088FCE445E` FOREIGN KEY (`jobid`) REFERENCES `job` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table lab
# ------------------------------------------------------------

DROP TABLE IF EXISTS `lab`;

CREATE TABLE `lab` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `departmentid` int(11) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `primaryuserid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1A18D50566623` (`lastupdatebyuser`),
  KEY `FK1A18DC0804016` (`departmentid`),
  KEY `FK1A18DC985888A` (`primaryuserid`),
  CONSTRAINT `FK1A18DC985888A` FOREIGN KEY (`primaryuserid`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FK1A18D50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FK1A18DC0804016` FOREIGN KEY (`departmentid`) REFERENCES `department` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table labmeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `labmeta`;

CREATE TABLE `labmeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `rolevisibility` varchar(255) DEFAULT NULL,
  `v` longtext,
  `labid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKFC3F291250566623` (`lastupdatebyuser`),
  KEY `FKFC3F29128FE41BFE` (`labid`),
  KEY `FKFC3F291250566623k` (`k`),
  CONSTRAINT `FKFC3F29128FE41BFE` FOREIGN KEY (`labid`) REFERENCES `lab` (`id`),
  CONSTRAINT `FKFC3F291250566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table labpending
# ------------------------------------------------------------

DROP TABLE IF EXISTS `labpending`;

CREATE TABLE `labpending` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `departmentid` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `primaryuserid` int(11) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `userpendingid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKD86D7F6A50566623` (`lastupdatebyuser`),
  KEY `FKD86D7F6AC0804016` (`departmentid`),
  KEY `FKD86D7F6AFF4502DC` (`userpendingid`),
  KEY `FKD86D7F6AC985888A` (`primaryuserid`),
  CONSTRAINT `FKD86D7F6AC985888A` FOREIGN KEY (`primaryuserid`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FKD86D7F6A50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FKD86D7F6AC0804016` FOREIGN KEY (`departmentid`) REFERENCES `department` (`id`),
  CONSTRAINT `FKD86D7F6AFF4502DC` FOREIGN KEY (`userpendingid`) REFERENCES `userpending` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table labpendingmeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `labpendingmeta`;

CREATE TABLE `labpendingmeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `rolevisibility` varchar(255) DEFAULT NULL,
  `v` longtext,
  `labpendingid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKDD9ED06F50566623` (`lastupdatebyuser`),
  KEY `FKDD9ED06F7498AA26` (`labpendingid`),
  KEY `FKDD9ED06F50566623k` (`k`),
  CONSTRAINT `FKDD9ED06F7498AA26` FOREIGN KEY (`labpendingid`) REFERENCES `labpending` (`id`),
  CONSTRAINT `FKDD9ED06F50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table labuser
# ------------------------------------------------------------

DROP TABLE IF EXISTS `labuser`;

CREATE TABLE `labuser` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  `roleid` int(11) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKFC42FED850566623` (`lastupdatebyuser`),
  KEY `FKFC42FED88FE41BFE` (`labid`),
  KEY `FKFC42FED877A92E9E` (`roleid`),
  KEY `FKFC42FED87CFE8408` (`userid`),
  CONSTRAINT `FKFC42FED87CFE8408` FOREIGN KEY (`userid`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FKFC42FED850566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FKFC42FED877A92E9E` FOREIGN KEY (`roleid`) REFERENCES `wrole` (`id`),
  CONSTRAINT `FKFC42FED88FE41BFE` FOREIGN KEY (`labid`) REFERENCES `lab` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table meta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `meta`;

CREATE TABLE `meta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `rolevisibility` varchar(255) DEFAULT NULL,
  `v` longtext,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK33160550566623` (`lastupdatebyuser`),
  KEY `FK33160550566623k` (`k`),
  CONSTRAINT `FK33160550566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table project
# ------------------------------------------------------------

DROP TABLE IF EXISTS `project`;

CREATE TABLE `project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKED904B1950566623` (`lastupdatebyuser`),
  KEY `FKED904B198FE41BFE` (`labid`),
  CONSTRAINT `FKED904B198FE41BFE` FOREIGN KEY (`labid`) REFERENCES `lab` (`id`),
  CONSTRAINT `FKED904B1950566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table resource
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resource`;

CREATE TABLE `resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `resourcetypeid` int(11) DEFAULT NULL,
  `resourcecategoryid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKEBABC40E50566623` (`lastupdatebyuser`),
  KEY `FKEBABC40E84326E2` (`resourcetypeid`),
  KEY `FKEBABC40EFBCBFFEA` (`resourcecategoryid`),
  CONSTRAINT `FKEBABC40EFBCBFFEA` FOREIGN KEY (`resourcecategoryid`) REFERENCES `resourcecategory` (`id`),
  CONSTRAINT `FKEBABC40E50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FKEBABC40E84326E2` FOREIGN KEY (`resourcetypeid`) REFERENCES `resourcetype` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table resourcebarcode
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resourcebarcode`;

CREATE TABLE `resourcebarcode` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `barcodeid` int(11) DEFAULT NULL,
  `resourceid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKAEBE18B250566623` (`lastupdatebyuser`),
  KEY `FKAEBE18B2F2B00CA4` (`barcodeid`),
  KEY `FKAEBE18B23AB44C4E` (`resourceid`),
  CONSTRAINT `FKAEBE18B23AB44C4E` FOREIGN KEY (`resourceid`) REFERENCES `resource` (`id`),
  CONSTRAINT `FKAEBE18B250566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FKAEBE18B2F2B00CA4` FOREIGN KEY (`barcodeid`) REFERENCES `barcode` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table resourcecategory
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resourcecategory`;

CREATE TABLE `resourcecategory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `resourcetypeid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9468930C50566623` (`lastupdatebyuser`),
  KEY `FK9468930C84326E2` (`resourcetypeid`),
  CONSTRAINT `FK9468930C84326E2` FOREIGN KEY (`resourcetypeid`) REFERENCES `resourcetype` (`id`),
  CONSTRAINT `FK9468930C50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table resourcecategorymeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resourcecategorymeta`;

CREATE TABLE `resourcecategorymeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `rolevisibility` varchar(255) DEFAULT NULL,
  `v` longtext,
  `resourcecategoryid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK38C3431150566623` (`lastupdatebyuser`),
  KEY `FK38C34311FBCBFFEA` (`resourcecategoryid`),
  KEY `FK38C3431150566623k` (`k`),
  CONSTRAINT `FK38C34311FBCBFFEA` FOREIGN KEY (`resourcecategoryid`) REFERENCES `resourcecategory` (`id`),
  CONSTRAINT `FK38C3431150566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table resourcecell
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resourcecell`;

CREATE TABLE `resourcecell` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `resourceid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKE91D967050566623` (`lastupdatebyuser`),
  KEY `FKE91D96703AB44C4E` (`resourceid`),
  CONSTRAINT `FKE91D96703AB44C4E` FOREIGN KEY (`resourceid`) REFERENCES `resource` (`id`),
  CONSTRAINT `FKE91D967050566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table resourcemeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resourcemeta`;

CREATE TABLE `resourcemeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `rolevisibility` varchar(255) DEFAULT NULL,
  `v` longtext,
  `resourceid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKE922231350566623` (`lastupdatebyuser`),
  KEY `FKE92223133AB44C4E` (`resourceid`),
  KEY `FKE922231350566623k` (`k`),
  CONSTRAINT `FKE92223133AB44C4E` FOREIGN KEY (`resourceid`) REFERENCES `resource` (`id`),
  CONSTRAINT `FKE922231350566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table resourcetype
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resourcetype`;

CREATE TABLE `resourcetype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKE9259C4850566623` (`lastupdatebyuser`),
  CONSTRAINT `FKE9259C4850566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table roleset
# ------------------------------------------------------------

DROP TABLE IF EXISTS `roleset`;

CREATE TABLE `roleset` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `childroleid` int(11) DEFAULT NULL,
  `parentroleid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5211E02C50566623` (`lastupdatebyuser`),
  KEY `FK5211E02C22D18FC8` (`parentroleid`),
  KEY `FK5211E02C96EFDEBA` (`childroleid`),
  CONSTRAINT `FK5211E02C96EFDEBA` FOREIGN KEY (`childroleid`) REFERENCES `wrole` (`id`),
  CONSTRAINT `FK5211E02C22D18FC8` FOREIGN KEY (`parentroleid`) REFERENCES `wrole` (`id`),
  CONSTRAINT `FK5211E02C50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table run
# ------------------------------------------------------------

DROP TABLE IF EXISTS `run`;

CREATE TABLE `run` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `endts` datetime DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `resourceCategoryid` int(11) DEFAULT NULL,
  `resourceid` int(11) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  `softwareid` int(11) DEFAULT NULL,
  `startts` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1BA8BB8A37246` (`sampleid`),
  KEY `FK1BA8B50566623` (`lastupdatebyuser`),
  KEY `FK1BA8B21328540` (`softwareid`),
  KEY `FK1BA8B3AB44C4E` (`resourceid`),
  KEY `FK1BA8BFBCBFFEA` (`resourceCategoryid`),
  KEY `FK1BA8B7CFE8408` (`userid`),
  CONSTRAINT `FK1BA8B7CFE8408` FOREIGN KEY (`userid`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FK1BA8B21328540` FOREIGN KEY (`softwareid`) REFERENCES `software` (`id`),
  CONSTRAINT `FK1BA8B3AB44C4E` FOREIGN KEY (`resourceid`) REFERENCES `resource` (`id`),
  CONSTRAINT `FK1BA8B50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FK1BA8BB8A37246` FOREIGN KEY (`sampleid`) REFERENCES `sample` (`id`),
  CONSTRAINT `FK1BA8BFBCBFFEA` FOREIGN KEY (`resourceCategoryid`) REFERENCES `resourcecategory` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table runcell
# ------------------------------------------------------------

DROP TABLE IF EXISTS `runcell`;

CREATE TABLE `runcell` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `resourcecellid` int(11) DEFAULT NULL,
  `runid` int(11) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5C6A066DB8A37246` (`sampleid`),
  KEY `FK5C6A066D50566623` (`lastupdatebyuser`),
  KEY `FK5C6A066D9042067A` (`runid`),
  KEY `FK5C6A066DEA1D3132` (`resourcecellid`),
  CONSTRAINT `FK5C6A066DEA1D3132` FOREIGN KEY (`resourcecellid`) REFERENCES `resourcecell` (`id`),
  CONSTRAINT `FK5C6A066D50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FK5C6A066D9042067A` FOREIGN KEY (`runid`) REFERENCES `run` (`id`),
  CONSTRAINT `FK5C6A066DB8A37246` FOREIGN KEY (`sampleid`) REFERENCES `sample` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table runmeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `runmeta`;

CREATE TABLE `runmeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `rolevisibility` varchar(255) DEFAULT NULL,
  `v` longtext,
  `runid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5C6E931050566623` (`lastupdatebyuser`),
  KEY `FK5C6E93109042067A` (`runid`),
  KEY `FK5C6E931050566623k` (`k`),
  CONSTRAINT `FK5C6E93109042067A` FOREIGN KEY (`runid`) REFERENCES `run` (`id`),
  CONSTRAINT `FK5C6E931050566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table sample
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sample`;

CREATE TABLE `sample` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `isgood` int(11) DEFAULT NULL,
  `isreceived` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `parentid` int(11) DEFAULT NULL,
  `receivedts` datetime DEFAULT NULL,
  `receiver_userid` int(11) DEFAULT NULL,
  `samplesubtypeid` int(11) DEFAULT NULL,
  `sampletypeid` int(11) DEFAULT NULL,
  `submitter_jobid` int(11) DEFAULT NULL,
  `submitter_labid` int(11) DEFAULT NULL,
  `submitter_userid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKC9C775AA50566623` (`lastupdatebyuser`),
  KEY `FKC9C775AADBE90DA` (`sampletypeid`),
  KEY `FKC9C775AA39050DE4` (`samplesubtypeid`),
  KEY `FKC9C775AADB1865A8` (`submitter_jobid`),
  KEY `FKC9C775AA88EE4546` (`parentid`),
  KEY `FKC9C775AA9AF88BFE` (`submitter_userid`),
  KEY `FKC9C775AADB2E3D48` (`submitter_labid`),
  CONSTRAINT `FKC9C775AADB2E3D48` FOREIGN KEY (`submitter_labid`) REFERENCES `lab` (`id`),
  CONSTRAINT `FKC9C775AA39050DE4` FOREIGN KEY (`samplesubtypeid`) REFERENCES `samplesubtype` (`id`),
  CONSTRAINT `FKC9C775AA50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FKC9C775AA88EE4546` FOREIGN KEY (`parentid`) REFERENCES `sample` (`id`),
  CONSTRAINT `FKC9C775AA9AF88BFE` FOREIGN KEY (`submitter_userid`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FKC9C775AADB1865A8` FOREIGN KEY (`submitter_jobid`) REFERENCES `job` (`id`),
  CONSTRAINT `FKC9C775AADBE90DA` FOREIGN KEY (`sampletypeid`) REFERENCES `sampletype` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table samplebarcode
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplebarcode`;

CREATE TABLE `samplebarcode` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `barcodeid` int(11) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKC9809F96B8A37246` (`sampleid`),
  KEY `FKC9809F9650566623` (`lastupdatebyuser`),
  KEY `FKC9809F96F2B00CA4` (`barcodeid`),
  CONSTRAINT `FKC9809F96F2B00CA4` FOREIGN KEY (`barcodeid`) REFERENCES `barcode` (`id`),
  CONSTRAINT `FKC9809F9650566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FKC9809F96B8A37246` FOREIGN KEY (`sampleid`) REFERENCES `sample` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table sampledraft
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sampledraft`;

CREATE TABLE `sampledraft` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `filegroupid` int(11) DEFAULT NULL,
  `jobdraftid` int(11) DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `samplesubtypeid` int(11) DEFAULT NULL,
  `sampletypeid` int(11) DEFAULT NULL,
  `sourcesampleid` int(11) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK967FE37181C758A` (`filegroupid`),
  KEY `FK967FE3750566623` (`lastupdatebyuser`),
  KEY `FK967FE37DBE90DA` (`sampletypeid`),
  KEY `FK967FE378FE41BFE` (`labid`),
  KEY `FK967FE3739050DE4` (`samplesubtypeid`),
  KEY `FK967FE37E3C3069A` (`jobdraftid`),
  KEY `FK967FE377CFE8408` (`userid`),
  CONSTRAINT `FK967FE377CFE8408` FOREIGN KEY (`userid`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FK967FE37181C758A` FOREIGN KEY (`filegroupid`) REFERENCES `filegroup` (`id`),
  CONSTRAINT `FK967FE3739050DE4` FOREIGN KEY (`samplesubtypeid`) REFERENCES `samplesubtype` (`id`),
  CONSTRAINT `FK967FE3750566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FK967FE378FE41BFE` FOREIGN KEY (`labid`) REFERENCES `lab` (`id`),
  CONSTRAINT `FK967FE37DBE90DA` FOREIGN KEY (`sampletypeid`) REFERENCES `sampletype` (`id`),
  CONSTRAINT `FK967FE37E3C3069A` FOREIGN KEY (`jobdraftid`) REFERENCES `jobdraft` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table sampledraftjobdraftcellselection
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sampledraftjobdraftcellselection`;

CREATE TABLE `sampledraftjobdraftcellselection` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `jobdraftcellselectionid` int(11) DEFAULT NULL,
  `libraryindex` int(11) DEFAULT NULL,
  `sampledraftid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKB65C2B0F50566623` (`lastupdatebyuser`),
  KEY `FKB65C2B0F4E0649B2` (`sampledraftid`),
  KEY `FKB65C2B0FBB98C410` (`jobdraftcellselectionid`),
  CONSTRAINT `FKB65C2B0FBB98C410` FOREIGN KEY (`jobdraftcellselectionid`) REFERENCES `jobdraftcellselection` (`id`),
  CONSTRAINT `FKB65C2B0F4E0649B2` FOREIGN KEY (`sampledraftid`) REFERENCES `sampledraft` (`id`),
  CONSTRAINT `FKB65C2B0F50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table sampledraftmeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sampledraftmeta`;

CREATE TABLE `sampledraftmeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `rolevisibility` varchar(255) DEFAULT NULL,
  `v` longtext,
  `sampledraftid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKFC7320BC50566623` (`lastupdatebyuser`),
  KEY `FKFC7320BC4E0649B2` (`sampledraftid`),
  KEY `FKFC7320BC50566623k` (`k`),
  CONSTRAINT `FKFC7320BC4E0649B2` FOREIGN KEY (`sampledraftid`) REFERENCES `sampledraft` (`id`),
  CONSTRAINT `FKFC7320BC50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table samplefilegroup
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplefilegroup`;

CREATE TABLE `samplefilegroup` (
  `sampleid` int(11) NOT NULL,
  `filegroupid` int(11) NOT NULL,
  PRIMARY KEY (`sampleid`,`filegroupid`),
  KEY `FKC18C5819B8A37246` (`sampleid`),
  KEY `FKC18C5819181C758A` (`filegroupid`),
  CONSTRAINT `FKC18C5819181C758A` FOREIGN KEY (`filegroupid`) REFERENCES `filegroup` (`id`),
  CONSTRAINT `FKC18C5819B8A37246` FOREIGN KEY (`sampleid`) REFERENCES `sample` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table samplejobcellselection
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplejobcellselection`;

CREATE TABLE `samplejobcellselection` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `jobcellselectionid` int(11) DEFAULT NULL,
  `libraryindex` int(11) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK216C5777B8A37246` (`sampleid`),
  KEY `FK216C577750566623` (`lastupdatebyuser`),
  KEY `FK216C57774704CC` (`jobcellselectionid`),
  CONSTRAINT `FK216C57774704CC` FOREIGN KEY (`jobcellselectionid`) REFERENCES `jobcellselection` (`id`),
  CONSTRAINT `FK216C577750566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FK216C5777B8A37246` FOREIGN KEY (`sampleid`) REFERENCES `sample` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table samplelab
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplelab`;

CREATE TABLE `samplelab` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `isprimary` int(11) DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK425753A3B8A37246` (`sampleid`),
  KEY `FK425753A350566623` (`lastupdatebyuser`),
  KEY `FK425753A38FE41BFE` (`labid`),
  CONSTRAINT `FK425753A38FE41BFE` FOREIGN KEY (`labid`) REFERENCES `lab` (`id`),
  CONSTRAINT `FK425753A350566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FK425753A3B8A37246` FOREIGN KEY (`sampleid`) REFERENCES `sample` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table samplemeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplemeta`;

CREATE TABLE `samplemeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `rolevisibility` varchar(255) DEFAULT NULL,
  `v` longtext,
  `sampleid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK893A6AFB8A37246` (`sampleid`),
  KEY `FK893A6AF50566623` (`lastupdatebyuser`),
  KEY `FK893A6AFB8A37246k` (`k`),
  CONSTRAINT `FK893A6AF50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FK893A6AFB8A37246` FOREIGN KEY (`sampleid`) REFERENCES `sample` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table samplesource
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplesource`;

CREATE TABLE `samplesource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `indexvalue` int(11) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  `source_sampleid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3D0F7645B8A37246` (`sampleid`),
  KEY `FK3D0F764550566623` (`lastupdatebyuser`),
  KEY `FK3D0F76456F2C29EA` (`source_sampleid`),
  CONSTRAINT `FK3D0F76456F2C29EA` FOREIGN KEY (`source_sampleid`) REFERENCES `sample` (`id`),
  CONSTRAINT `FK3D0F764550566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FK3D0F7645B8A37246` FOREIGN KEY (`sampleid`) REFERENCES `sample` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table samplesourcefilegroup
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplesourcefilegroup`;

CREATE TABLE `samplesourcefilegroup` (
  `samplesourceid` int(11) NOT NULL,
  `filegroupid` int(11) NOT NULL,
  PRIMARY KEY (`samplesourceid`,`filegroupid`),
  KEY `FK67BBD5DE181C758A` (`filegroupid`),
  KEY `FK67BBD5DE267BC79C` (`samplesourceid`),
  CONSTRAINT `FK67BBD5DE267BC79C` FOREIGN KEY (`samplesourceid`) REFERENCES `samplesource` (`id`),
  CONSTRAINT `FK67BBD5DE181C758A` FOREIGN KEY (`filegroupid`) REFERENCES `filegroup` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table samplesourcemeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplesourcemeta`;

CREATE TABLE `samplesourcemeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `rolevisibility` varchar(255) DEFAULT NULL,
  `v` longtext,
  `samplesourceid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKA063E1CA50566623` (`lastupdatebyuser`),
  KEY `FKA063E1CA267BC79C` (`samplesourceid`),
  KEY `FKA063E1CA50566623k` (`k`),
  CONSTRAINT `FKA063E1CA267BC79C` FOREIGN KEY (`samplesourceid`) REFERENCES `samplesource` (`id`),
  CONSTRAINT `FKA063E1CA50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table samplesubtype
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplesubtype`;

CREATE TABLE `samplesubtype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `arealist` varchar(255) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sampletypeid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6E11E49050566623` (`lastupdatebyuser`),
  KEY `FK6E11E490DBE90DA` (`sampletypeid`),
  CONSTRAINT `FK6E11E490DBE90DA` FOREIGN KEY (`sampletypeid`) REFERENCES `sampletype` (`id`),
  CONSTRAINT `FK6E11E49050566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table samplesubtypemeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplesubtypemeta`;

CREATE TABLE `samplesubtypemeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `rolevisibility` varchar(255) DEFAULT NULL,
  `v` longtext,
  `samplesubtypeid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK92A0329550566623` (`lastupdatebyuser`),
  KEY `FK92A0329539050DE4` (`samplesubtypeid`),
  KEY `FK92A0329550566623k` (`k`),
  CONSTRAINT `FK92A0329539050DE4` FOREIGN KEY (`samplesubtypeid`) REFERENCES `samplesubtype` (`id`),
  CONSTRAINT `FK92A0329550566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table samplesubtyperesourcecategory
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplesubtyperesourcecategory`;

CREATE TABLE `samplesubtyperesourcecategory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `resourcecategoryid` int(11) DEFAULT NULL,
  `samplesubtypeid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK69BF579C50566623` (`lastupdatebyuser`),
  KEY `FK69BF579C39050DE4` (`samplesubtypeid`),
  KEY `FK69BF579CFBCBFFEA` (`resourcecategoryid`),
  CONSTRAINT `FK69BF579CFBCBFFEA` FOREIGN KEY (`resourcecategoryid`) REFERENCES `resourcecategory` (`id`),
  CONSTRAINT `FK69BF579C39050DE4` FOREIGN KEY (`samplesubtypeid`) REFERENCES `samplesubtype` (`id`),
  CONSTRAINT `FK69BF579C50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table sampletype
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sampletype`;

CREATE TABLE `sampletype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sampletypecategoryid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8971FE450566623` (`lastupdatebyuser`),
  KEY `FK8971FE467BEF876` (`sampletypecategoryid`),
  CONSTRAINT `FK8971FE467BEF876` FOREIGN KEY (`sampletypecategoryid`) REFERENCES `sampletypecategory` (`id`),
  CONSTRAINT `FK8971FE450566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table sampletypecategory
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sampletypecategory`;

CREATE TABLE `sampletypecategory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKDAFDB8E250566623` (`lastupdatebyuser`),
  CONSTRAINT `FKDAFDB8E250566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table software
# ------------------------------------------------------------

DROP TABLE IF EXISTS `software`;

CREATE TABLE `software` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `resourcetypeid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4EA361A750566623` (`lastupdatebyuser`),
  KEY `FK4EA361A784326E2` (`resourcetypeid`),
  CONSTRAINT `FK4EA361A784326E2` FOREIGN KEY (`resourcetypeid`) REFERENCES `resourcetype` (`id`),
  CONSTRAINT `FK4EA361A750566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table softwaremeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `softwaremeta`;

CREATE TABLE `softwaremeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `rolevisibility` varchar(255) DEFAULT NULL,
  `v` longtext,
  `softwareid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKA56F4C2C50566623` (`lastupdatebyuser`),
  KEY `FKA56F4C2C21328540` (`softwareid`),
  KEY `FKA56F4C2C50566623k` (`k`),
  CONSTRAINT `FKA56F4C2C21328540` FOREIGN KEY (`softwareid`) REFERENCES `software` (`id`),
  CONSTRAINT `FKA56F4C2C50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table uifield
# ------------------------------------------------------------

DROP TABLE IF EXISTS `uifield`;

CREATE TABLE `uifield` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `area` varchar(255) DEFAULT NULL,
  `attrname` varchar(255) DEFAULT NULL,
  `attrvalue` longtext,
  `domain` varchar(255) DEFAULT NULL,
  `locale` varchar(5) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKE6348EE650566623` (`lastupdatebyuser`),
  KEY `FKE6348EE650566624ar` (`area`),
  KEY `FKE6348EE650566625an` (`attrname`),
  KEY `FKE6348EE650566626n` (`name`),
  CONSTRAINT `FKE6348EE650566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table usermeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `usermeta`;

CREATE TABLE `usermeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `rolevisibility` varchar(255) DEFAULT NULL,
  `v` longtext,
  `userid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKF029245050566623` (`lastupdatebyuser`),
  KEY `FKF02924507CFE8408` (`userid`),
  KEY `FKF029245050566623k` (`k`),
  CONSTRAINT `FKF02924507CFE8408` FOREIGN KEY (`userid`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FKF029245050566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table userpasswordauth
# ------------------------------------------------------------

DROP TABLE IF EXISTS `userpasswordauth`;

CREATE TABLE `userpasswordauth` (
  `id` int(11) NOT NULL,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `authcode` varchar(255) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKE9E7660E50566623` (`lastupdatebyuser`),
  KEY `FKE9E7660E7CFE8408` (`userid`),
  CONSTRAINT `FKE9E7660E7CFE8408` FOREIGN KEY (`userid`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FKE9E7660E50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table userpending
# ------------------------------------------------------------

DROP TABLE IF EXISTS `userpending`;

CREATE TABLE `userpending` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `locale` varchar(5) DEFAULT NULL,
  `login` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6219D36C50566623` (`lastupdatebyuser`),
  KEY `FK6219D36C8FE41BFE` (`labid`),
  CONSTRAINT `FK6219D36C8FE41BFE` FOREIGN KEY (`labid`) REFERENCES `lab` (`id`),
  CONSTRAINT `FK6219D36C50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table userpendingmeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `userpendingmeta`;

CREATE TABLE `userpendingmeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `rolevisibility` varchar(255) DEFAULT NULL,
  `v` longtext,
  `userpendingid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK511D537150566623` (`lastupdatebyuser`),
  KEY `FK511D5371FF4502DC` (`userpendingid`),
  KEY `FK511D537150566623k` (`k`),
  CONSTRAINT `FK511D5371FF4502DC` FOREIGN KEY (`userpendingid`) REFERENCES `userpending` (`id`),
  CONSTRAINT `FK511D537150566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table userrole
# ------------------------------------------------------------

DROP TABLE IF EXISTS `userrole`;

CREATE TABLE `userrole` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `roleid` int(11) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKF02B8EC150566623` (`lastupdatebyuser`),
  KEY `FKF02B8EC177A92E9E` (`roleid`),
  KEY `FKF02B8EC17CFE8408` (`userid`),
  CONSTRAINT `FKF02B8EC17CFE8408` FOREIGN KEY (`userid`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FKF02B8EC150566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FKF02B8EC177A92E9E` FOREIGN KEY (`roleid`) REFERENCES `wrole` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table workflow
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflow`;

CREATE TABLE `workflow` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `createts` datetime DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK21BD7BF50566623` (`lastupdatebyuser`),
  CONSTRAINT `FK21BD7BF50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table workflowmeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowmeta`;

CREATE TABLE `workflowmeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `rolevisibility` varchar(255) DEFAULT NULL,
  `v` longtext,
  `workflowid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5D0EF64450566623` (`lastupdatebyuser`),
  KEY `FK5D0EF6448BDE4B70` (`workflowid`),
  KEY `FK5D0EF64450566623k` (`k`),
  CONSTRAINT `FK5D0EF6448BDE4B70` FOREIGN KEY (`workflowid`) REFERENCES `workflow` (`id`),
  CONSTRAINT `FK5D0EF64450566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table workflowresourcecategory
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowresourcecategory`;

CREATE TABLE `workflowresourcecategory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `resourcecategoryid` int(11) DEFAULT NULL,
  `workflowid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKD8B30CCB50566623` (`lastupdatebyuser`),
  KEY `FKD8B30CCBFBCBFFEA` (`resourcecategoryid`),
  KEY `FKD8B30CCB8BDE4B70` (`workflowid`),
  CONSTRAINT `FKD8B30CCB8BDE4B70` FOREIGN KEY (`workflowid`) REFERENCES `workflow` (`id`),
  CONSTRAINT `FKD8B30CCB50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FKD8B30CCBFBCBFFEA` FOREIGN KEY (`resourcecategoryid`) REFERENCES `resourcecategory` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table workflowresourcecategorymeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowresourcecategorymeta`;

CREATE TABLE `workflowresourcecategorymeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `rolevisibility` varchar(255) DEFAULT NULL,
  `v` longtext,
  `workflowresourcecategoryid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKFBACC55050566623` (`lastupdatebyuser`),
  KEY `FKFBACC550AD019488` (`workflowresourcecategoryid`),
  KEY `FKFBACC55050566623k` (`k`),
  CONSTRAINT `FKFBACC550AD019488` FOREIGN KEY (`workflowresourcecategoryid`) REFERENCES `workflowresourcecategory` (`id`),
  CONSTRAINT `FKFBACC55050566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table workflowresourcetype
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowresourcetype`;

CREATE TABLE `workflowresourcetype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `resourcetypeid` int(11) DEFAULT NULL,
  `workflowid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK98F8CD8750566623` (`lastupdatebyuser`),
  KEY `FK98F8CD8784326E2` (`resourcetypeid`),
  KEY `FK98F8CD878BDE4B70` (`workflowid`),
  CONSTRAINT `FK98F8CD878BDE4B70` FOREIGN KEY (`workflowid`) REFERENCES `workflow` (`id`),
  CONSTRAINT `FK98F8CD8750566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FK98F8CD8784326E2` FOREIGN KEY (`resourcetypeid`) REFERENCES `resourcetype` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table workflowsamplesubtype
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowsamplesubtype`;

CREATE TABLE `workflowsamplesubtype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `samplesubtypeid` int(11) DEFAULT NULL,
  `workflowid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKB8A4DB3150566623` (`lastupdatebyuser`),
  KEY `FKB8A4DB3139050DE4` (`samplesubtypeid`),
  KEY `FKB8A4DB318BDE4B70` (`workflowid`),
  CONSTRAINT `FKB8A4DB318BDE4B70` FOREIGN KEY (`workflowid`) REFERENCES `workflow` (`id`),
  CONSTRAINT `FKB8A4DB3139050DE4` FOREIGN KEY (`samplesubtypeid`) REFERENCES `samplesubtype` (`id`),
  CONSTRAINT `FKB8A4DB3150566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table workflowsoftware
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowsoftware`;

CREATE TABLE `workflowsoftware` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `softwareid` int(11) DEFAULT NULL,
  `workflowid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1E070A6650566623` (`lastupdatebyuser`),
  KEY `FK1E070A6621328540` (`softwareid`),
  KEY `FK1E070A668BDE4B70` (`workflowid`),
  CONSTRAINT `FK1E070A668BDE4B70` FOREIGN KEY (`workflowid`) REFERENCES `workflow` (`id`),
  CONSTRAINT `FK1E070A6621328540` FOREIGN KEY (`softwareid`) REFERENCES `software` (`id`),
  CONSTRAINT `FK1E070A6650566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table workflowsoftwaremeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowsoftwaremeta`;

CREATE TABLE `workflowsoftwaremeta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `rolevisibility` varchar(255) DEFAULT NULL,
  `v` longtext,
  `workflowsoftwareid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK55427D6B50566623` (`lastupdatebyuser`),
  KEY `FK55427D6B2148801E` (`workflowsoftwareid`),
  KEY `FK55427D6B50566623k` (`k`),
  CONSTRAINT `FK55427D6B2148801E` FOREIGN KEY (`workflowsoftwareid`) REFERENCES `workflowsoftware` (`id`),
  CONSTRAINT `FK55427D6B50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table wrole
# ------------------------------------------------------------

DROP TABLE IF EXISTS `wrole`;

CREATE TABLE `wrole` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `domain` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `rolename` varchar(255) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6C26D6D50566623` (`lastupdatebyuser`),
  CONSTRAINT `FK6C26D6D50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table wuser
# ------------------------------------------------------------

DROP TABLE IF EXISTS `wuser`;

CREATE TABLE `wuser` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `locale` varchar(5) DEFAULT NULL,
  `login` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6C3D8C250566623` (`lastupdatebyuser`),
  CONSTRAINT `FK6C3D8C250566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
