-- Recreate Database 
GRANT USAGE ON *.* TO 'wasp'@'localhost';
DROP USER 'wasp'@'localhost';
DROP DATABASE IF EXISTS wasp;

create database wasp CHARACTER SET utf8 COLLATE utf8_general_ci;
create user 'wasp'@'localhost' IDENTIFIED BY 'waspV2';

grant all on wasp.* to 'wasp'@'localhost';

flush privileges;

use wasp;


# ************************************************************
# Sequel Pro SQL dump
# Version 4004
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.1.66-log)
# Database: wasp
# Generation Time: 2013-03-15 19:55:47 +0000
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



# Dump of table acct_grant_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_grant_AUD`;

CREATE TABLE `acct_grant_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `expirationdt` datetime DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKA198D7C1DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table acct_grantjob_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_grantjob_AUD`;

CREATE TABLE `acct_grantjob_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `grantid` int(11) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKCEA3731EDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table acct_invoice_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_invoice_AUD`;

CREATE TABLE `acct_invoice_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `amount` float DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  `quoteid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK4BD28452DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table acct_ledger_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_ledger_AUD`;

CREATE TABLE `acct_ledger_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `amount` float DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `invoiceid` int(11) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK7C500266DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table acct_quote_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_quote_AUD`;

CREATE TABLE `acct_quote_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `amount` float DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK91E9A521DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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
  CONSTRAINT `FK91F08D559B63709` FOREIGN KEY (`quoteid`) REFERENCES `acct_quote` (`id`),
  CONSTRAINT `FK91F08D5550566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table acct_quotemeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_quotemeta_AUD`;

CREATE TABLE `acct_quotemeta_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `quoteid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKE1BC08A6DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table acct_quoteuser_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_quoteuser_AUD`;

CREATE TABLE `acct_quoteuser_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `isapproved` int(11) DEFAULT NULL,
  `quoteid` int(11) DEFAULT NULL,
  `roleid` int(11) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKECB38B6CDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table acct_workflowcost_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_workflowcost_AUD`;

CREATE TABLE `acct_workflowcost_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `basecost` float DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK99AA6A89DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table adaptor_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `adaptor_AUD`;

CREATE TABLE `adaptor_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `adaptorsetid` int(11) DEFAULT NULL,
  `barcodenumber` int(11) DEFAULT NULL,
  `barcodesequence` varchar(255) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sequence` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK33F907D6DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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
  CONSTRAINT `FK33FFF00AF88E976E` FOREIGN KEY (`adaptorid`) REFERENCES `adaptor` (`id`),
  CONSTRAINT `FK33FFF00A50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table adaptormeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `adaptormeta_AUD`;

CREATE TABLE `adaptormeta_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `adaptorid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK534108DBDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table adaptorset_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `adaptorset_AUD`;

CREATE TABLE `adaptorset_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sampletypeid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK8224150EDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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
  CONSTRAINT `FK822AFD42DA18E62C` FOREIGN KEY (`adaptorsetid`) REFERENCES `adaptorset` (`id`),
  CONSTRAINT `FK822AFD4250566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table adaptorsetmeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `adaptorsetmeta_AUD`;

CREATE TABLE `adaptorsetmeta_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `adaptorsetid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK4E32BA13DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table adaptorsetresourcecategory_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `adaptorsetresourcecategory_AUD`;

CREATE TABLE `adaptorsetresourcecategory_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `adaptorsetid` int(11) DEFAULT NULL,
  `resourcecategoryid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK6C80281ADF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table barcode_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `barcode_AUD`;

CREATE TABLE `barcode_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `barcode` varchar(255) DEFAULT NULL,
  `barcodefor` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK6221D651DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table BATCH_JOB_EXECUTION
# ------------------------------------------------------------

DROP TABLE IF EXISTS `BATCH_JOB_EXECUTION`;

CREATE TABLE `BATCH_JOB_EXECUTION` (
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



# Dump of table BATCH_JOB_EXECUTION_CONTEXT
# ------------------------------------------------------------

DROP TABLE IF EXISTS `BATCH_JOB_EXECUTION_CONTEXT`;

CREATE TABLE `BATCH_JOB_EXECUTION_CONTEXT` (
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `SHORT_CONTEXT` varchar(2500) NOT NULL,
  `SERIALIZED_CONTEXT` text,
  PRIMARY KEY (`JOB_EXECUTION_ID`),
  CONSTRAINT `JOB_EXEC_CTX_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `batch_job_execution` (`JOB_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table BATCH_JOB_EXECUTION_SEQ
# ------------------------------------------------------------

DROP TABLE IF EXISTS `BATCH_JOB_EXECUTION_SEQ`;

CREATE TABLE `BATCH_JOB_EXECUTION_SEQ` (
  `ID` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table BATCH_JOB_INSTANCE
# ------------------------------------------------------------

DROP TABLE IF EXISTS `BATCH_JOB_INSTANCE`;

CREATE TABLE `BATCH_JOB_INSTANCE` (
  `JOB_INSTANCE_ID` bigint(20) NOT NULL,
  `VERSION` bigint(20) DEFAULT NULL,
  `JOB_NAME` varchar(100) NOT NULL,
  `JOB_KEY` varchar(32) NOT NULL,
  PRIMARY KEY (`JOB_INSTANCE_ID`),
  UNIQUE KEY `JOB_INST_UN` (`JOB_NAME`,`JOB_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table BATCH_JOB_PARAMS
# ------------------------------------------------------------

DROP TABLE IF EXISTS `BATCH_JOB_PARAMS`;

CREATE TABLE `BATCH_JOB_PARAMS` (
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



# Dump of table BATCH_JOB_SEQ
# ------------------------------------------------------------

DROP TABLE IF EXISTS `BATCH_JOB_SEQ`;

CREATE TABLE `BATCH_JOB_SEQ` (
  `ID` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table BATCH_STEP_EXECUTION
# ------------------------------------------------------------

DROP TABLE IF EXISTS `BATCH_STEP_EXECUTION`;

CREATE TABLE `BATCH_STEP_EXECUTION` (
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



# Dump of table BATCH_STEP_EXECUTION_CONTEXT
# ------------------------------------------------------------

DROP TABLE IF EXISTS `BATCH_STEP_EXECUTION_CONTEXT`;

CREATE TABLE `BATCH_STEP_EXECUTION_CONTEXT` (
  `STEP_EXECUTION_ID` bigint(20) NOT NULL,
  `SHORT_CONTEXT` varchar(2500) NOT NULL,
  `SERIALIZED_CONTEXT` text,
  PRIMARY KEY (`STEP_EXECUTION_ID`),
  CONSTRAINT `STEP_EXEC_CTX_FK` FOREIGN KEY (`STEP_EXECUTION_ID`) REFERENCES `batch_step_execution` (`STEP_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table BATCH_STEP_EXECUTION_SEQ
# ------------------------------------------------------------

DROP TABLE IF EXISTS `BATCH_STEP_EXECUTION_SEQ`;

CREATE TABLE `BATCH_STEP_EXECUTION_SEQ` (
  `ID` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table confirmemailauth_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `confirmemailauth_AUD`;

CREATE TABLE `confirmemailauth_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `authcode` varchar(255) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `userpendingid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKEE29F855DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table department_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `department_AUD`;

CREATE TABLE `department_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `isinternal` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKF0647823DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table departmentuser_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `departmentuser_AUD`;

CREATE TABLE `departmentuser_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `departmentid` int(11) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK1BA90D6EDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table file_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `file_AUD`;

CREATE TABLE `file_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `archived` tinyint(1) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_uri` longtext,
  `md5hash` varchar(255) DEFAULT NULL,
  `sizek` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKD42D054DDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table filegroup_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `filegroup_AUD`;

CREATE TABLE `filegroup_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `description` longtext,
  `filetypeid` int(11) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `isarchived` int(11) DEFAULT NULL,
  `softwaregeneratedbyid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK92642D4DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table filegroup_rel_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `filegroup_rel_AUD`;

CREATE TABLE `filegroup_rel_AUD` (
  `REV` int(11) NOT NULL,
  `filegroupid` int(11) NOT NULL,
  `childfilegroupid` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`REV`,`childfilegroupid`,`filegroupid`),
  KEY `FKF19F742EDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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
  CONSTRAINT `FK92D2B0850566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FK92D2B08181C758A` FOREIGN KEY (`filegroupid`) REFERENCES `filegroup` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table filegroupmeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `filegroupmeta_AUD`;

CREATE TABLE `filegroupmeta_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `filegroupid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK93C94D9DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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
  CONSTRAINT `FKD433ED81D1800F32` FOREIGN KEY (`fileid`) REFERENCES `file` (`id`),
  CONSTRAINT `FKD433ED8150566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table filemeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `filemeta_AUD`;

CREATE TABLE `filemeta_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `fileid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK95BB72D2DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table filetype
# ------------------------------------------------------------

DROP TABLE IF EXISTS `filetype`;

CREATE TABLE `filetype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKD43766B650566623` (`lastupdatebyuser`),
  CONSTRAINT `FKD43766B650566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table filetype_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `filetype_AUD`;

CREATE TABLE `filetype_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK88454987DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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
  CONSTRAINT `FK884C31BB175EFBBE` FOREIGN KEY (`filetypeid`) REFERENCES `filetype` (`id`),
  CONSTRAINT `FK884C31BB50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table filetypemeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `filetypemeta_AUD`;

CREATE TABLE `filetypemeta_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `filetypeid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK3F430A0CDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table groupfile_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `groupfile_AUD`;

CREATE TABLE `groupfile_AUD` (
  `REV` int(11) NOT NULL,
  `groupid` int(11) NOT NULL,
  `fileid` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`REV`,`groupid`,`fileid`),
  KEY `FK4AAB0CCCDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table job_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `job_AUD`;

CREATE TABLE `job_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `createts` datetime DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `viewablebylab` int(11) DEFAULT NULL,
  `workflowid` int(11) DEFAULT NULL,
  `current_quote` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKAA4FA30EDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table jobcellselection_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobcellselection_AUD`;

CREATE TABLE `jobcellselection_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `cellindex` int(11) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKF613F51EDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table jobdraft_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobdraft_AUD`;

CREATE TABLE `jobdraft_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `createts` datetime DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `submittedjobid` int(11) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `workflowid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK2FB037D5DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table jobdraftcellselection_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobdraftcellselection_AUD`;

CREATE TABLE `jobdraftcellselection_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `cellindex` int(11) DEFAULT NULL,
  `jobdraftid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKF8AB9BB7DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table jobdraftfile_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobdraftfile_AUD`;

CREATE TABLE `jobdraftfile_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `filegroupid` int(11) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `jobdraftid` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK2ED46D51DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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
  CONSTRAINT `FK2FB72009E3C3069A` FOREIGN KEY (`jobdraftid`) REFERENCES `jobdraft` (`id`),
  CONSTRAINT `FK2FB7200950566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table jobdraftmeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobdraftmeta_AUD`;

CREATE TABLE `jobdraftmeta_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `jobdraftid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK3FD2215ADF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table jobdraftresourcecategory_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobdraftresourcecategory_AUD`;

CREATE TABLE `jobdraftresourcecategory_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `jobdraftid` int(11) DEFAULT NULL,
  `resourcecategoryid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKC593DCE1DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table jobdraftsoftware_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobdraftsoftware_AUD`;

CREATE TABLE `jobdraftsoftware_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `jobdraftid` int(11) DEFAULT NULL,
  `softwareid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKE5429FCDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table jobfile_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobfile_AUD`;

CREATE TABLE `jobfile_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `filegroupid` int(11) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK28E9940ADF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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
  CONSTRAINT `FKAA568B428FCE445E` FOREIGN KEY (`jobid`) REFERENCES `job` (`id`),
  CONSTRAINT `FKAA568B4250566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table jobmeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobmeta_AUD`;

CREATE TABLE `jobmeta_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK39E74813DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table jobresourcecategory_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobresourcecategory_AUD`;

CREATE TABLE `jobresourcecategory_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  `resourcecategoryid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK17CFB61ADF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table jobsample_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobsample_AUD`;

CREATE TABLE `jobsample_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK206091F8DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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
  CONSTRAINT `FK20677A2C55379B12` FOREIGN KEY (`jobsampleid`) REFERENCES `jobsample` (`id`),
  CONSTRAINT `FK20677A2C50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table jobsamplemeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobsamplemeta_AUD`;

CREATE TABLE `jobsamplemeta_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `jobsampleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK4EB2B1FDDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table jobsoftware_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobsoftware_AUD`;

CREATE TABLE `jobsoftware_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  `softwareid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK17954C35DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table jobuser_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobuser_AUD`;

CREATE TABLE `jobuser_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  `roleid` int(11) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK44DECAD9DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table lab_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `lab_AUD`;

CREATE TABLE `lab_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `departmentid` int(11) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `primaryuserid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKFC3840DEDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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
  CONSTRAINT `FKFC3F29128FE41BFE` FOREIGN KEY (`labid`) REFERENCES `lab` (`id`),
  CONSTRAINT `FKFC3F291250566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table labmeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `labmeta_AUD`;

CREATE TABLE `labmeta_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK7AC7DE3DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table labpending_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `labpending_AUD`;

CREATE TABLE `labpending_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `departmentid` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `primaryuserid` int(11) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `userpendingid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKDD97E83BDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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
  CONSTRAINT `FKDD9ED06F7498AA26` FOREIGN KEY (`labpendingid`) REFERENCES `labpending` (`id`),
  CONSTRAINT `FKDD9ED06F50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table labpendingmeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `labpendingmeta_AUD`;

CREATE TABLE `labpendingmeta_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `labpendingid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK57FF2EC0DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table labuser_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `labuser_AUD`;

CREATE TABLE `labuser_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  `roleid` int(11) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK12A400A9DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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
  CONSTRAINT `FK33160550566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table meta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `meta_AUD`;

CREATE TABLE `meta_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKE52AB956DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table project_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `project_AUD`;

CREATE TABLE `project_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKC7FF446ADF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table resource_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resource_AUD`;

CREATE TABLE `resource_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `resourcetypeid` int(11) DEFAULT NULL,
  `resourcecategoryid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKE91B3ADFDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table resourcebarcode_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resourcebarcode_AUD`;

CREATE TABLE `resourcebarcode_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `barcodeid` int(11) DEFAULT NULL,
  `resourceid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK7BEA9D83DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table resourcecategory_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resourcecategory_AUD`;

CREATE TABLE `resourcecategory_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `resourcetypeid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK38BC5ADDDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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
  CONSTRAINT `FK38C34311FBCBFFEA` FOREIGN KEY (`resourcecategoryid`) REFERENCES `resourcecategory` (`id`),
  CONSTRAINT `FK38C3431150566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table resourcecategorymeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resourcecategorymeta_AUD`;

CREATE TABLE `resourcecategorymeta_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `resourcecategoryid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKD0858062DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table resourcecell_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resourcecell_AUD`;

CREATE TABLE `resourcecell_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `resourceid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK5AB90C41DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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
  CONSTRAINT `FKE92223133AB44C4E` FOREIGN KEY (`resourceid`) REFERENCES `resource` (`id`),
  CONSTRAINT `FKE922231350566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table resourcemeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resourcemeta_AUD`;

CREATE TABLE `resourcemeta_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `resourceid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK76908F64DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table resourcetype_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resourcetype_AUD`;

CREATE TABLE `resourcetype_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK691A6619DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table REVINFO
# ------------------------------------------------------------

DROP TABLE IF EXISTS `REVINFO`;

CREATE TABLE `REVINFO` (
  `REV` int(11) NOT NULL AUTO_INCREMENT,
  `REVTSTMP` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table roleset_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `roleset_AUD`;

CREATE TABLE `roleset_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `childroleid` int(11) DEFAULT NULL,
  `parentroleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK38BA17FDDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table run_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `run_AUD`;

CREATE TABLE `run_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
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
  PRIMARY KEY (`id`,`REV`),
  KEY `FK5C67AADCDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table runcell_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `runcell_AUD`;

CREATE TABLE `runcell_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `resourcecellid` int(11) DEFAULT NULL,
  `runid` int(11) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK722335BEDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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
  CONSTRAINT `FK5C6E93109042067A` FOREIGN KEY (`runid`) REFERENCES `run` (`id`),
  CONSTRAINT `FK5C6E931050566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table runmeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `runmeta_AUD`;

CREATE TABLE `runmeta_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `runid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK8DFAB8E1DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table sample_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sample_AUD`;

CREATE TABLE `sample_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
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
  PRIMARY KEY (`id`,`REV`),
  KEY `FK88CBE7BDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table samplebarcode_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplebarcode_AUD`;

CREATE TABLE `samplebarcode_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `barcodeid` int(11) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKD2871267DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table sampledraft_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sampledraft_AUD`;

CREATE TABLE `sampledraft_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `filegroupid` int(11) DEFAULT NULL,
  `jobdraftid` int(11) DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `samplesubtypeid` int(11) DEFAULT NULL,
  `sampletypeid` int(11) DEFAULT NULL,
  `sourcesampleid` int(11) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKFC6C3888DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table sampledraftjobdraftcellselection_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sampledraftjobdraftcellselection_AUD`;

CREATE TABLE `sampledraftjobdraftcellselection_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `jobdraftcellselectionid` int(11) DEFAULT NULL,
  `libraryindex` int(11) DEFAULT NULL,
  `sampledraftid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK874E3960DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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
  CONSTRAINT `FKFC7320BC4E0649B2` FOREIGN KEY (`sampledraftid`) REFERENCES `sampledraft` (`id`),
  CONSTRAINT `FKFC7320BC50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table sampledraftmeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sampledraftmeta_AUD`;

CREATE TABLE `sampledraftmeta_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `sampledraftid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK5868908DDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table samplefilegroup_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplefilegroup_AUD`;

CREATE TABLE `samplefilegroup_AUD` (
  `REV` int(11) NOT NULL,
  `sampleid` int(11) NOT NULL,
  `filegroupid` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`REV`,`sampleid`,`filegroupid`),
  KEY `FKF52CD16ADF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table samplejobcellselection_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplejobcellselection_AUD`;

CREATE TABLE `samplejobcellselection_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `jobcellselectionid` int(11) DEFAULT NULL,
  `libraryindex` int(11) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK5C21F1C8DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table samplelab_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplelab_AUD`;

CREATE TABLE `samplelab_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `isprimary` int(11) DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKD99AF7F4DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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
  CONSTRAINT `FK893A6AF50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FK893A6AFB8A37246` FOREIGN KEY (`sampleid`) REFERENCES `sample` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table samplemeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplemeta_AUD`;

CREATE TABLE `samplemeta_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKB41EE500DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table samplesource_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplesource_AUD`;

CREATE TABLE `samplesource_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `indexvalue` int(11) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  `source_sampleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKA05CF996DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table samplesourcefilegroup_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplesourcefilegroup_AUD`;

CREATE TABLE `samplesourcefilegroup_AUD` (
  `REV` int(11) NOT NULL,
  `samplesourceid` int(11) NOT NULL,
  `filegroupid` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`REV`,`samplesourceid`,`filegroupid`),
  KEY `FKD82DE4AFDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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
  CONSTRAINT `FKA063E1CA267BC79C` FOREIGN KEY (`samplesourceid`) REFERENCES `samplesource` (`id`),
  CONSTRAINT `FKA063E1CA50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table samplesourcemeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplesourcemeta_AUD`;

CREATE TABLE `samplesourcemeta_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `samplesourceid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK24D61A9BDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table samplesubtype_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplesubtype_AUD`;

CREATE TABLE `samplesubtype_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `arealist` varchar(255) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sampletypeid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK92994A61DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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
  CONSTRAINT `FK92A0329539050DE4` FOREIGN KEY (`samplesubtypeid`) REFERENCES `samplesubtype` (`id`),
  CONSTRAINT `FK92A0329550566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table samplesubtypemeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplesubtypemeta_AUD`;

CREATE TABLE `samplesubtypemeta_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `samplesubtypeid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKB970DE6DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table samplesubtyperesourcecategory_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplesubtyperesourcecategory_AUD`;

CREATE TABLE `samplesubtyperesourcecategory_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `resourcecategoryid` int(11) DEFAULT NULL,
  `samplesubtypeid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK44FE576DDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table sampletype_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sampletype_AUD`;

CREATE TABLE `sampletype_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sampletypecategoryid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKA6A8BBB5DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table sampletypecategory_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sampletypecategory_AUD`;

CREATE TABLE `sampletypecategory_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK41FEA5B3DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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
  `resourcetypeid` int(11) DEFAULT NULL,
  `lastupdatebyuser` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4EA361A750566623` (`lastupdatebyuser`),
  KEY `FK4EA361A784326E2` (`resourcetypeid`),
  CONSTRAINT `FK4EA361A784326E2` FOREIGN KEY (`resourcetypeid`) REFERENCES `resourcetype` (`id`),
  CONSTRAINT `FK4EA361A750566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table software_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `software_AUD`;

CREATE TABLE `software_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `resourcetypeid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKA56863F8DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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
  CONSTRAINT `FKA56F4C2C21328540` FOREIGN KEY (`softwareid`) REFERENCES `software` (`id`),
  CONSTRAINT `FKA56F4C2C50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table softwaremeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `softwaremeta_AUD`;

CREATE TABLE `softwaremeta_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `softwareid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK878183FDDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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
  CONSTRAINT `FKF02924507CFE8408` FOREIGN KEY (`userid`) REFERENCES `wuser` (`id`),
  CONSTRAINT `FKF029245050566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table usermeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `usermeta_AUD`;

CREATE TABLE `usermeta_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKB38AAA21DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table userpasswordauth_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `userpasswordauth_AUD`;

CREATE TABLE `userpasswordauth_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `authcode` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK3CB5DCDFDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table userpending_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `userpending_AUD`;

CREATE TABLE `userpending_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `locale` varchar(5) DEFAULT NULL,
  `login` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK51166B3DDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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
  CONSTRAINT `FK511D5371FF4502DC` FOREIGN KEY (`userpendingid`) REFERENCES `userpending` (`id`),
  CONSTRAINT `FK511D537150566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table userpendingmeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `userpendingmeta_AUD`;

CREATE TABLE `userpendingmeta_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `userpendingid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK12A060C2DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table userrole_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `userrole_AUD`;

CREATE TABLE `userrole_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `roleid` int(11) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKBE807412DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table workflow_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflow_AUD`;

CREATE TABLE `workflow_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `createts` datetime DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK5D080E10DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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
  CONSTRAINT `FK5D0EF6448BDE4B70` FOREIGN KEY (`workflowid`) REFERENCES `workflow` (`id`),
  CONSTRAINT `FK5D0EF64450566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table workflowmeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowmeta_AUD`;

CREATE TABLE `workflowmeta_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `workflowid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKB48E6215DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table workflowresourcecategory_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowresourcecategory_AUD`;

CREATE TABLE `workflowresourcecategory_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `resourcecategoryid` int(11) DEFAULT NULL,
  `workflowid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKFBA5DD1CDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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
  CONSTRAINT `FKFBACC550AD019488` FOREIGN KEY (`workflowresourcecategoryid`) REFERENCES `workflowresourcecategory` (`id`),
  CONSTRAINT `FKFBACC55050566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table workflowresourcecategorymeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowresourcecategorymeta_AUD`;

CREATE TABLE `workflowresourcecategorymeta_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `workflowresourcecategoryid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK2255CB21DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table workflowresourcetype_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowresourcetype_AUD`;

CREATE TABLE `workflowresourcetype_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `resourcetypeid` int(11) DEFAULT NULL,
  `workflowid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKAD64DFD8DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table workflowsamplesubtype_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowsamplesubtype_AUD`;

CREATE TABLE `workflowsamplesubtype_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `samplesubtypeid` int(11) DEFAULT NULL,
  `workflowid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKD79E0882DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table workflowsoftware_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowsoftware_AUD`;

CREATE TABLE `workflowsoftware_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `softwareid` int(11) DEFAULT NULL,
  `workflowid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FK553B9537DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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
  CONSTRAINT `FK55427D6B2148801E` FOREIGN KEY (`workflowsoftwareid`) REFERENCES `workflowsoftware` (`id`),
  CONSTRAINT `FK55427D6B50566623` FOREIGN KEY (`lastupdatebyuser`) REFERENCES `wuser` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table workflowsoftwaremeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowsoftwaremeta_AUD`;

CREATE TABLE `workflowsoftwaremeta_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `workflowsoftwareid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKCBCBFDBCDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table wrole_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `wrole_AUD`;

CREATE TABLE `wrole_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `domain` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `rolename` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKDBF01CBEDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



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



# Dump of table wuser_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `wuser_AUD`;

CREATE TABLE `wuser_AUD` (
  `id` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `locale` varchar(5) DEFAULT NULL,
  `login` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`,`REV`),
  KEY `FKDBF1D593DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
