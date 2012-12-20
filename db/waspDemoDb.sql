# ************************************************************
# Sequel Pro SQL dump
# Version 3408
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.1.66)
# Database: wasp
# Generation Time: 2012-12-20 16:34:11 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

DROP DATABASE IF EXISTS wasp;
create database wasp CHARACTER SET utf8 COLLATE utf8_general_ci;
use wasp;

# Dump of table acct_grant
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_grant`;

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



# Dump of table acct_grant_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_grant_AUD`;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table acct_grantjob
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_grantjob`;

CREATE TABLE `acct_grantjob` (
  `jobid` int(10) NOT NULL AUTO_INCREMENT,
  `grantid` int(10) DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`jobid`),
  KEY `fk_acct_ledgergrant_gid` (`grantid`),
  KEY `FKAA5FF3CD5E117DCB` (`jobid`),
  CONSTRAINT `FKAA5FF3CD5E117DCB` FOREIGN KEY (`jobid`) REFERENCES `acct_ledger` (`ledgerid`),
  CONSTRAINT `acct_grantjob_ibfk_1` FOREIGN KEY (`jobid`) REFERENCES `acct_ledger` (`jobid`),
  CONSTRAINT `acct_grantjob_ibfk_2` FOREIGN KEY (`grantid`) REFERENCES `acct_grant` (`grantid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table acct_grantjob_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_grantjob_AUD`;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table acct_invoice
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_invoice`;

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



# Dump of table acct_invoice_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_invoice_AUD`;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table acct_jobquotecurrent
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_jobquotecurrent`;

CREATE TABLE `acct_jobquotecurrent` (
  `currentid` int(10) NOT NULL AUTO_INCREMENT,
  `jobid` int(10) DEFAULT NULL,
  `quoteid` int(10) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`currentid`),
  UNIQUE KEY `u_acct_jobquotecurrent_jid` (`jobid`),
  UNIQUE KEY `u_acct_jobquotecurrent_qid` (`quoteid`),
  CONSTRAINT `acct_jobquotecurrent_ibfk_1` FOREIGN KEY (`jobid`) REFERENCES `job` (`jobid`),
  CONSTRAINT `acct_jobquotecurrent_ibfk_2` FOREIGN KEY (`quoteid`) REFERENCES `acct_quote` (`quoteid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `acct_jobquotecurrent` WRITE;
/*!40000 ALTER TABLE `acct_jobquotecurrent` DISABLE KEYS */;

INSERT INTO `acct_jobquotecurrent` (`currentid`, `jobid`, `quoteid`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,1,'2012-12-20 11:22:17',1);

/*!40000 ALTER TABLE `acct_jobquotecurrent` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table acct_jobquotecurrent_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_jobquotecurrent_AUD`;

CREATE TABLE `acct_jobquotecurrent_AUD` (
  `currentId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `quoteid` int(11) DEFAULT NULL,
  PRIMARY KEY (`currentId`,`REV`),
  KEY `FKF4D3825FDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `acct_jobquotecurrent_AUD` WRITE;
/*!40000 ALTER TABLE `acct_jobquotecurrent_AUD` DISABLE KEYS */;

INSERT INTO `acct_jobquotecurrent_AUD` (`currentId`, `REV`, `REVTYPE`, `jobid`, `lastupdts`, `lastupduser`, `quoteid`)
VALUES
	(1,64,0,1,'2012-12-20 11:22:17',1,1);

/*!40000 ALTER TABLE `acct_jobquotecurrent_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table acct_ledger
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_ledger`;

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



# Dump of table acct_ledger_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_ledger_AUD`;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table acct_quote
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_quote`;

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

LOCK TABLES `acct_quote` WRITE;
/*!40000 ALTER TABLE `acct_quote` DISABLE KEYS */;

INSERT INTO `acct_quote` (`quoteid`, `jobid`, `amount`, `userid`, `comment`, `isactive`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,2600.00,NULL,NULL,NULL,'2012-12-20 11:22:17',1);

/*!40000 ALTER TABLE `acct_quote` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table acct_quote_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_quote_AUD`;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `acct_quote_AUD` WRITE;
/*!40000 ALTER TABLE `acct_quote_AUD` DISABLE KEYS */;

INSERT INTO `acct_quote_AUD` (`quoteId`, `REV`, `REVTYPE`, `userid`, `amount`, `comment`, `isactive`, `jobid`, `lastupdts`, `lastupduser`)
VALUES
	(1,64,0,NULL,2600,NULL,NULL,1,'2012-12-20 11:22:17',1);

/*!40000 ALTER TABLE `acct_quote_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table acct_quotemeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_quotemeta`;

CREATE TABLE `acct_quotemeta` (
  `quotemetaid` int(10) NOT NULL AUTO_INCREMENT,
  `quoteid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` text,
  `position` int(10) DEFAULT '0',
  `rolevisibility` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`quotemetaid`),
  UNIQUE KEY `u_quotemeta_k_qid` (`k`,`quoteid`),
  KEY `fk_quotemeta_qid` (`quoteid`),
  CONSTRAINT `acct_quotemeta_ibfk_1` FOREIGN KEY (`quoteid`) REFERENCES `acct_quote` (`quoteid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `acct_quotemeta` WRITE;
/*!40000 ALTER TABLE `acct_quotemeta` DISABLE KEYS */;

INSERT INTO `acct_quotemeta` (`quotemetaid`, `quoteid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,'acctQuote.library_cost','600',0,NULL,'2012-12-20 11:22:17',NULL),
	(2,1,'acctQuote.sample_cost','0',0,NULL,'2012-12-20 11:22:17',NULL),
	(3,1,'acctQuote.lane_cost','2000',0,NULL,'2012-12-20 11:22:17',NULL);

/*!40000 ALTER TABLE `acct_quotemeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table acct_quotemeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_quotemeta_AUD`;

CREATE TABLE `acct_quotemeta_AUD` (
  `quotemetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `quoteid` int(11) DEFAULT NULL,
  PRIMARY KEY (`quotemetaId`,`REV`),
  KEY `FKE1BC08A6DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `acct_quotemeta_AUD` WRITE;
/*!40000 ALTER TABLE `acct_quotemeta_AUD` DISABLE KEYS */;

INSERT INTO `acct_quotemeta_AUD` (`quotemetaId`, `REV`, `REVTYPE`, `quoteid`)
VALUES
	(1,64,0,1),
	(2,64,0,1),
	(3,64,0,1);

/*!40000 ALTER TABLE `acct_quotemeta_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table acct_quoteuser
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_quoteuser`;

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



# Dump of table acct_quoteuser_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_quoteuser_AUD`;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table acct_workflowcost
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_workflowcost`;

CREATE TABLE `acct_workflowcost` (
  `workflowid` int(10) NOT NULL,
  `basecost` float(10,2) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`workflowid`),
  KEY `FKC3C958B8744987E0` (`workflowid`),
  CONSTRAINT `FKC3C958B8744987E0` FOREIGN KEY (`workflowid`) REFERENCES `job` (`jobid`),
  CONSTRAINT `acct_workflowcost_ibfk_1` FOREIGN KEY (`workflowid`) REFERENCES `job` (`workflowid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table acct_workflowcost_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `acct_workflowcost_AUD`;

CREATE TABLE `acct_workflowcost_AUD` (
  `workflowId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `basecost` float DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  PRIMARY KEY (`workflowId`,`REV`),
  KEY `FK99AA6A89DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table adaptor
# ------------------------------------------------------------

DROP TABLE IF EXISTS `adaptor`;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `adaptor` WRITE;
/*!40000 ALTER TABLE `adaptor` DISABLE KEYS */;

INSERT INTO `adaptor` (`adaptorid`, `adaptorsetid`, `iname`, `name`, `sequence`, `barcodesequence`, `barcodenumber`, `isactive`)
VALUES
	(1,1,'illuminaHelptagLibrary1','helptag Adaptor','CGCTGCTG','CGCTGCTG',1,1),
	(2,2,'illuminaTrueseqDnaIndexed1','TruSeq Adaptor, Index 1 (ATCACG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','ATCACG',1,1),
	(3,2,'illuminaTrueseqDnaIndexed2','TruSeq Adaptor, Index 2 (CGATGT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','CGATGT',2,1),
	(4,2,'illuminaTrueseqDnaIndexed3','TruSeq Adaptor, Index 3 (TTAGGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','TTAGGC',3,1),
	(5,2,'illuminaTrueseqDnaIndexed4','TruSeq Adaptor, Index 4 (TGACCA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','TGACCA',4,1),
	(6,2,'illuminaTrueseqDnaIndexed5','TruSeq Adaptor, Index 5 (ACAGTG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','ACAGTG',5,1),
	(7,2,'illuminaTrueseqDnaIndexed6','TruSeq Adaptor, Index 6 (GCCAAT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','GCCAAT',6,1),
	(8,2,'illuminaTrueseqDnaIndexed7','TruSeq Adaptor, Index 7 (CAGATC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','CAGATC',7,1),
	(9,2,'illuminaTrueseqDnaIndexed8','TruSeq Adaptor, Index 8 (ACTTGA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','ACTTGA',8,1),
	(10,2,'illuminaTrueseqDnaIndexed9','TruSeq Adaptor, Index 9 (GATCAG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','GATCAG',9,1),
	(11,2,'illuminaTrueseqDnaIndexed10','TruSeq Adaptor, Index 10 (TAGCTT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','TAGCTT',10,1),
	(12,2,'illuminaTrueseqDnaIndexed11','TruSeq Adaptor, Index 11 (GGCTAC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','GGCTAC',11,1),
	(13,2,'illuminaTrueseqDnaIndexed12','TruSeq Adaptor, Index 12 (CTTGTA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','CTTGTA',12,1),
	(14,2,'illuminaTrueseqDnaIndexed13','TruSeq Adaptor, Index 13 (AGTCAA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','AGTCAA',13,1),
	(15,2,'illuminaTrueseqDnaIndexed14','TruSeq Adaptor, Index 14 (AGTTCC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','AGTTCC',14,1),
	(16,2,'illuminaTrueseqDnaIndexed15','TruSeq Adaptor, Index 15 (ATGTCA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','ATGTCA',15,1),
	(17,2,'illuminaTrueseqDnaIndexed16','TruSeq Adaptor, Index 16 (CCGTCC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','CCGTCC',16,1),
	(18,2,'illuminaTrueseqDnaIndexed17','TruSeq Adaptor, Index 17 (GTAGAG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','GTAGAG',17,1),
	(19,2,'illuminaTrueseqDnaIndexed18','TruSeq Adaptor, Index 18 (GTCCGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','GTCCGC',18,1),
	(20,2,'illuminaTrueseqDnaIndexed19','TruSeq Adaptor, Index 19 (GTGAAA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','GTGAAA',19,1),
	(21,2,'illuminaTrueseqDnaIndexed20','TruSeq Adaptor, Index 20 (GTGGCC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','GTGGCC',20,1),
	(22,2,'illuminaTrueseqDnaIndexed21','TruSeq Adaptor, Index 21 (GTTTCG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','GTTTCG',21,1),
	(23,2,'illuminaTrueseqDnaIndexed22','TruSeq Adaptor, Index 22 (CGTACG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','CGTACG',22,1),
	(24,2,'illuminaTrueseqDnaIndexed23','TruSeq Adaptor, Index 23 (GAGTGG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','GAGTGG',23,1),
	(25,2,'illuminaTrueseqDnaIndexed24','TruSeq Adaptor, Index 24 (GGTAGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','GGTAGC',24,1),
	(26,2,'illuminaTrueseqDnaIndexed25','TruSeq Adaptor, Index 25 (ACTGAT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','ACTGAT',25,1),
	(27,2,'illuminaTrueseqDnaIndexed26','TruSeq Adaptor, Index 26 (ATGAGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','ATGAGC',26,1),
	(28,2,'illuminaTrueseqDnaIndexed27','TruSeq Adaptor, Index 27 (ATTCCT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','ATTCCT',27,1),
	(29,2,'illuminaTrueseqDnaIndexed28','TruSeq Adaptor, Index 28 (CAAAAG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','CAAAAG',28,1),
	(30,2,'illuminaTrueseqDnaIndexed29','TruSeq Adaptor, Index 29 (CAACTA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','CAACTA',29,1),
	(31,2,'illuminaTrueseqDnaIndexed30','TruSeq Adaptor, Index 30 (CACCGG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','CACCGG',30,1),
	(32,2,'illuminaTrueseqDnaIndexed31','TruSeq Adaptor, Index 31 (CACGAT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','CACGAT',31,1),
	(33,2,'illuminaTrueseqDnaIndexed32','TruSeq Adaptor, Index 32 (CACTCA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','CACTCA',32,1),
	(34,2,'illuminaTrueseqDnaIndexed33','TruSeq Adaptor, Index 33 (CAGGCG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','CAGGCG',33,1),
	(35,2,'illuminaTrueseqDnaIndexed34','TruSeq Adaptor, Index 34 (CATGGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','CATGGC',34,1),
	(36,2,'illuminaTrueseqDnaIndexed35','TruSeq Adaptor, Index 35 (CATTTT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','CATTTT',35,1),
	(37,2,'illuminaTrueseqDnaIndexed36','TruSeq Adaptor, Index 36 (CCAACA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','CCAACA',36,1),
	(38,2,'illuminaTrueseqDnaIndexed37','TruSeq Adaptor, Index 37 (CGGAAT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','CGGAAT',37,1),
	(39,2,'illuminaTrueseqDnaIndexed38','TruSeq Adaptor, Index 38 (CTAGCT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','CTAGCT',38,1),
	(40,2,'illuminaTrueseqDnaIndexed39','TruSeq Adaptor, Index 39 (CTATAC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','CTATAC',39,1),
	(41,2,'illuminaTrueseqDnaIndexed40','TruSeq Adaptor, Index 40 (CTCAGA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','CTCAGA',40,1),
	(42,2,'illuminaTrueseqDnaIndexed41','TruSeq Adaptor, Index 41 (GACGAC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','GACGAC',41,1),
	(43,2,'illuminaTrueseqDnaIndexed42','TruSeq Adaptor, Index 42 (TAATCG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','TAATCG',42,1),
	(44,2,'illuminaTrueseqDnaIndexed43','TruSeq Adaptor, Index 43 (TACAGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','TACAGC',43,1),
	(45,2,'illuminaTrueseqDnaIndexed44','TruSeq Adaptor, Index 44 (TATAAT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','TATAAT',44,1),
	(46,2,'illuminaTrueseqDnaIndexed45','TruSeq Adaptor, Index 45 (TCATTC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','TCATTC',45,1),
	(47,2,'illuminaTrueseqDnaIndexed46','TruSeq Adaptor, Index 46 (TCCCGA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','TCCCGA',46,1),
	(48,2,'illuminaTrueseqDnaIndexed47','TruSeq Adaptor, Index 47 (TCGAAG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','TCGAAG',47,1),
	(49,2,'illuminaTrueseqDnaIndexed48','TruSeq Adaptor, Index 48 (TCGGCA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC','TCGGCA',48,1);

/*!40000 ALTER TABLE `adaptor` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table adaptor_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `adaptor_AUD`;

CREATE TABLE `adaptor_AUD` (
  `adaptorId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `adaptorsetid` int(11) DEFAULT NULL,
  `barcodenumber` int(11) DEFAULT NULL,
  `barcodesequence` varchar(255) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sequence` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`adaptorId`,`REV`),
  KEY `FK33F907D6DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `adaptor_AUD` WRITE;
/*!40000 ALTER TABLE `adaptor_AUD` DISABLE KEYS */;

INSERT INTO `adaptor_AUD` (`adaptorId`, `REV`, `REVTYPE`, `adaptorsetid`, `barcodenumber`, `barcodesequence`, `iname`, `isactive`, `name`, `sequence`)
VALUES
	(1,27,0,1,1,'CGCTGCTG','illuminaHelptagLibrary1',1,'helptag Adaptor','CGCTGCTG'),
	(2,32,0,2,1,'ATCACG','illuminaTrueseqDnaIndexed1',1,'TruSeq Adaptor, Index 1 (ATCACG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(3,32,0,2,2,'CGATGT','illuminaTrueseqDnaIndexed2',1,'TruSeq Adaptor, Index 2 (CGATGT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(4,32,0,2,3,'TTAGGC','illuminaTrueseqDnaIndexed3',1,'TruSeq Adaptor, Index 3 (TTAGGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(5,32,0,2,4,'TGACCA','illuminaTrueseqDnaIndexed4',1,'TruSeq Adaptor, Index 4 (TGACCA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(6,32,0,2,5,'ACAGTG','illuminaTrueseqDnaIndexed5',1,'TruSeq Adaptor, Index 5 (ACAGTG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(7,32,0,2,6,'GCCAAT','illuminaTrueseqDnaIndexed6',1,'TruSeq Adaptor, Index 6 (GCCAAT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(8,32,0,2,7,'CAGATC','illuminaTrueseqDnaIndexed7',1,'TruSeq Adaptor, Index 7 (CAGATC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(9,32,0,2,8,'ACTTGA','illuminaTrueseqDnaIndexed8',1,'TruSeq Adaptor, Index 8 (ACTTGA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(10,32,0,2,9,'GATCAG','illuminaTrueseqDnaIndexed9',1,'TruSeq Adaptor, Index 9 (GATCAG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(11,32,0,2,10,'TAGCTT','illuminaTrueseqDnaIndexed10',1,'TruSeq Adaptor, Index 10 (TAGCTT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(12,32,0,2,11,'GGCTAC','illuminaTrueseqDnaIndexed11',1,'TruSeq Adaptor, Index 11 (GGCTAC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(13,32,0,2,12,'CTTGTA','illuminaTrueseqDnaIndexed12',1,'TruSeq Adaptor, Index 12 (CTTGTA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(14,32,0,2,13,'AGTCAA','illuminaTrueseqDnaIndexed13',1,'TruSeq Adaptor, Index 13 (AGTCAA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(15,32,0,2,14,'AGTTCC','illuminaTrueseqDnaIndexed14',1,'TruSeq Adaptor, Index 14 (AGTTCC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(16,32,0,2,15,'ATGTCA','illuminaTrueseqDnaIndexed15',1,'TruSeq Adaptor, Index 15 (ATGTCA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(17,32,0,2,16,'CCGTCC','illuminaTrueseqDnaIndexed16',1,'TruSeq Adaptor, Index 16 (CCGTCC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(18,32,0,2,17,'GTAGAG','illuminaTrueseqDnaIndexed17',1,'TruSeq Adaptor, Index 17 (GTAGAG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(19,32,0,2,18,'GTCCGC','illuminaTrueseqDnaIndexed18',1,'TruSeq Adaptor, Index 18 (GTCCGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(20,32,0,2,19,'GTGAAA','illuminaTrueseqDnaIndexed19',1,'TruSeq Adaptor, Index 19 (GTGAAA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(21,32,0,2,20,'GTGGCC','illuminaTrueseqDnaIndexed20',1,'TruSeq Adaptor, Index 20 (GTGGCC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(22,32,0,2,21,'GTTTCG','illuminaTrueseqDnaIndexed21',1,'TruSeq Adaptor, Index 21 (GTTTCG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(23,32,0,2,22,'CGTACG','illuminaTrueseqDnaIndexed22',1,'TruSeq Adaptor, Index 22 (CGTACG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(24,32,0,2,23,'GAGTGG','illuminaTrueseqDnaIndexed23',1,'TruSeq Adaptor, Index 23 (GAGTGG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(25,32,0,2,24,'GGTAGC','illuminaTrueseqDnaIndexed24',1,'TruSeq Adaptor, Index 24 (GGTAGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(26,32,0,2,25,'ACTGAT','illuminaTrueseqDnaIndexed25',1,'TruSeq Adaptor, Index 25 (ACTGAT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(27,32,0,2,26,'ATGAGC','illuminaTrueseqDnaIndexed26',1,'TruSeq Adaptor, Index 26 (ATGAGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(28,32,0,2,27,'ATTCCT','illuminaTrueseqDnaIndexed27',1,'TruSeq Adaptor, Index 27 (ATTCCT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(29,32,0,2,28,'CAAAAG','illuminaTrueseqDnaIndexed28',1,'TruSeq Adaptor, Index 28 (CAAAAG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(30,32,0,2,29,'CAACTA','illuminaTrueseqDnaIndexed29',1,'TruSeq Adaptor, Index 29 (CAACTA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(31,32,0,2,30,'CACCGG','illuminaTrueseqDnaIndexed30',1,'TruSeq Adaptor, Index 30 (CACCGG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(32,32,0,2,31,'CACGAT','illuminaTrueseqDnaIndexed31',1,'TruSeq Adaptor, Index 31 (CACGAT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(33,32,0,2,32,'CACTCA','illuminaTrueseqDnaIndexed32',1,'TruSeq Adaptor, Index 32 (CACTCA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(34,32,0,2,33,'CAGGCG','illuminaTrueseqDnaIndexed33',1,'TruSeq Adaptor, Index 33 (CAGGCG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(35,32,0,2,34,'CATGGC','illuminaTrueseqDnaIndexed34',1,'TruSeq Adaptor, Index 34 (CATGGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(36,32,0,2,35,'CATTTT','illuminaTrueseqDnaIndexed35',1,'TruSeq Adaptor, Index 35 (CATTTT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(37,32,0,2,36,'CCAACA','illuminaTrueseqDnaIndexed36',1,'TruSeq Adaptor, Index 36 (CCAACA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(38,32,0,2,37,'CGGAAT','illuminaTrueseqDnaIndexed37',1,'TruSeq Adaptor, Index 37 (CGGAAT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(39,32,0,2,38,'CTAGCT','illuminaTrueseqDnaIndexed38',1,'TruSeq Adaptor, Index 38 (CTAGCT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(40,32,0,2,39,'CTATAC','illuminaTrueseqDnaIndexed39',1,'TruSeq Adaptor, Index 39 (CTATAC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(41,32,0,2,40,'CTCAGA','illuminaTrueseqDnaIndexed40',1,'TruSeq Adaptor, Index 40 (CTCAGA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(42,32,0,2,41,'GACGAC','illuminaTrueseqDnaIndexed41',1,'TruSeq Adaptor, Index 41 (GACGAC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(43,32,0,2,42,'TAATCG','illuminaTrueseqDnaIndexed42',1,'TruSeq Adaptor, Index 42 (TAATCG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(44,32,0,2,43,'TACAGC','illuminaTrueseqDnaIndexed43',1,'TruSeq Adaptor, Index 43 (TACAGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(45,32,0,2,44,'TATAAT','illuminaTrueseqDnaIndexed44',1,'TruSeq Adaptor, Index 44 (TATAAT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(46,32,0,2,45,'TCATTC','illuminaTrueseqDnaIndexed45',1,'TruSeq Adaptor, Index 45 (TCATTC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(47,32,0,2,46,'TCCCGA','illuminaTrueseqDnaIndexed46',1,'TruSeq Adaptor, Index 46 (TCCCGA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(48,32,0,2,47,'TCGAAG','illuminaTrueseqDnaIndexed47',1,'TruSeq Adaptor, Index 47 (TCGAAG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC'),
	(49,32,0,2,48,'TCGGCA','illuminaTrueseqDnaIndexed48',1,'TruSeq Adaptor, Index 48 (TCGGCA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC');

/*!40000 ALTER TABLE `adaptor_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table adaptormeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `adaptormeta`;

CREATE TABLE `adaptormeta` (
  `adaptormetaid` int(10) NOT NULL AUTO_INCREMENT,
  `adaptorid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` text,
  `position` int(10) DEFAULT '0',
  `rolevisibility` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`adaptormetaid`),
  UNIQUE KEY `u_adaptormeta_k_aid` (`k`,`adaptorid`),
  KEY `fk_adaptormeta_runid` (`adaptorid`),
  CONSTRAINT `adaptormeta_ibfk_1` FOREIGN KEY (`adaptorid`) REFERENCES `adaptor` (`adaptorid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table adaptormeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `adaptormeta_AUD`;

CREATE TABLE `adaptormeta_AUD` (
  `adaptorMetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `adaptorid` int(11) DEFAULT NULL,
  PRIMARY KEY (`adaptorMetaId`,`REV`),
  KEY `FK534108DBDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table adaptorset
# ------------------------------------------------------------

DROP TABLE IF EXISTS `adaptorset`;

CREATE TABLE `adaptorset` (
  `adaptorsetid` int(10) NOT NULL AUTO_INCREMENT,
  `iname` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `sampletypeid` int(10) DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  PRIMARY KEY (`adaptorsetid`),
  UNIQUE KEY `u_adaptorset_k_iid` (`iname`),
  UNIQUE KEY `u_adaptorset_k_nid` (`name`),
  KEY `fk_adaptorset_tid` (`sampletypeid`),
  CONSTRAINT `adaptorset_ibfk_1` FOREIGN KEY (`sampletypeid`) REFERENCES `sampletype` (`sampletypeid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `adaptorset` WRITE;
/*!40000 ALTER TABLE `adaptorset` DISABLE KEYS */;

INSERT INTO `adaptorset` (`adaptorsetid`, `iname`, `name`, `sampletypeid`, `isactive`)
VALUES
	(1,'helptagLibrary','HELP-tag Library',1,1),
	(2,'truseqIndexedDna','TruSEQ INDEXED DNA',2,1);

/*!40000 ALTER TABLE `adaptorset` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table adaptorset_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `adaptorset_AUD`;

CREATE TABLE `adaptorset_AUD` (
  `adaptorsetId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sampletypeid` int(11) DEFAULT NULL,
  PRIMARY KEY (`adaptorsetId`,`REV`),
  KEY `FK8224150EDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `adaptorset_AUD` WRITE;
/*!40000 ALTER TABLE `adaptorset_AUD` DISABLE KEYS */;

INSERT INTO `adaptorset_AUD` (`adaptorsetId`, `REV`, `REVTYPE`, `iname`, `isactive`, `name`, `sampletypeid`)
VALUES
	(1,27,0,'helptagLibrary',1,'HELP-tag Library',1),
	(2,32,0,'truseqIndexedDna',1,'TruSEQ INDEXED DNA',2);

/*!40000 ALTER TABLE `adaptorset_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table adaptorsetmeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `adaptorsetmeta`;

CREATE TABLE `adaptorsetmeta` (
  `adaptorsetmetaid` int(10) NOT NULL AUTO_INCREMENT,
  `adaptorsetid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` text,
  `position` int(10) DEFAULT '0',
  `rolevisibility` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`adaptorsetmetaid`),
  UNIQUE KEY `u_adaptorsetmeta_k_aid` (`k`,`adaptorsetid`),
  KEY `fk_adaptorsetmeta_runid` (`adaptorsetid`),
  CONSTRAINT `adaptorsetmeta_ibfk_1` FOREIGN KEY (`adaptorsetid`) REFERENCES `adaptorset` (`adaptorsetid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `adaptorsetmeta` WRITE;
/*!40000 ALTER TABLE `adaptorsetmeta` DISABLE KEYS */;

INSERT INTO `adaptorsetmeta` (`adaptorsetmetaid`, `adaptorsetid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
VALUES
	(1,2,'truseqIndexedDna.truseq','1',1,NULL,'2012-12-20 11:03:31',0);

/*!40000 ALTER TABLE `adaptorsetmeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table adaptorsetmeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `adaptorsetmeta_AUD`;

CREATE TABLE `adaptorsetmeta_AUD` (
  `adaptorsetMetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `adaptorsetid` int(11) DEFAULT NULL,
  PRIMARY KEY (`adaptorsetMetaId`,`REV`),
  KEY `FK4E32BA13DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `adaptorsetmeta_AUD` WRITE;
/*!40000 ALTER TABLE `adaptorsetmeta_AUD` DISABLE KEYS */;

INSERT INTO `adaptorsetmeta_AUD` (`adaptorsetMetaId`, `REV`, `REVTYPE`, `adaptorsetid`)
VALUES
	(1,32,0,2);

/*!40000 ALTER TABLE `adaptorsetmeta_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table adaptorsetresourcecategory
# ------------------------------------------------------------

DROP TABLE IF EXISTS `adaptorsetresourcecategory`;

CREATE TABLE `adaptorsetresourcecategory` (
  `adaptorsetresourcecategoryid` int(10) NOT NULL AUTO_INCREMENT,
  `adaptorsetid` int(10) DEFAULT NULL,
  `resourcecategoryid` int(10) DEFAULT NULL,
  PRIMARY KEY (`adaptorsetresourcecategoryid`),
  UNIQUE KEY `u_adaptorsetresourcecategory_aid_rid` (`adaptorsetid`,`resourcecategoryid`),
  KEY `fk_adaptorsetresourcecategory_rid` (`resourcecategoryid`),
  CONSTRAINT `adaptorsetresourcecategory_ibfk_1` FOREIGN KEY (`resourcecategoryid`) REFERENCES `resourcecategory` (`resourcecategoryid`),
  CONSTRAINT `adaptorsetresourcecategory_ibfk_2` FOREIGN KEY (`adaptorsetid`) REFERENCES `adaptorset` (`adaptorsetid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `adaptorsetresourcecategory` WRITE;
/*!40000 ALTER TABLE `adaptorsetresourcecategory` DISABLE KEYS */;

INSERT INTO `adaptorsetresourcecategory` (`adaptorsetresourcecategoryid`, `adaptorsetid`, `resourcecategoryid`)
VALUES
	(1,1,1),
	(2,2,1);

/*!40000 ALTER TABLE `adaptorsetresourcecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table adaptorsetresourcecategory_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `adaptorsetresourcecategory_AUD`;

CREATE TABLE `adaptorsetresourcecategory_AUD` (
  `adaptorsetresourcecategoryid` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `adaptorsetid` int(11) DEFAULT NULL,
  `resourcecategoryid` int(11) DEFAULT NULL,
  PRIMARY KEY (`adaptorsetresourcecategoryid`,`REV`),
  KEY `FK6C80281ADF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `adaptorsetresourcecategory_AUD` WRITE;
/*!40000 ALTER TABLE `adaptorsetresourcecategory_AUD` DISABLE KEYS */;

INSERT INTO `adaptorsetresourcecategory_AUD` (`adaptorsetresourcecategoryid`, `REV`, `REVTYPE`, `adaptorsetid`, `resourcecategoryid`)
VALUES
	(1,27,0,1,1),
	(2,32,0,2,1);

/*!40000 ALTER TABLE `adaptorsetresourcecategory_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table barcode
# ------------------------------------------------------------

DROP TABLE IF EXISTS `barcode`;

CREATE TABLE `barcode` (
  `barcodeid` int(10) NOT NULL AUTO_INCREMENT,
  `barcode` varchar(250) DEFAULT NULL,
  `barcodefor` varchar(250) DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`barcodeid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `barcode` WRITE;
/*!40000 ALTER TABLE `barcode` DISABLE KEYS */;

INSERT INTO `barcode` (`barcodeid`, `barcode`, `barcodefor`, `isactive`, `lastupdts`, `lastupduser`)
VALUES
	(1,'QER344555',NULL,NULL,'2012-12-20 11:07:21',1),
	(2,'QWE34555',NULL,NULL,'2012-12-20 11:07:41',1),
	(3,'FRE45676678',NULL,NULL,'2012-12-20 11:08:04',1),
	(4,'C126NACXX','WASP',1,'2012-12-20 11:09:20',1),
	(5,'D1884ACXX','WASP',1,'2012-12-20 11:09:48',1),
	(6,'634H7AAXX','WASP',1,'2012-12-20 11:10:27',1);

/*!40000 ALTER TABLE `barcode` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table barcode_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `barcode_AUD`;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `barcode_AUD` WRITE;
/*!40000 ALTER TABLE `barcode_AUD` DISABLE KEYS */;

INSERT INTO `barcode_AUD` (`barcodeId`, `REV`, `REVTYPE`, `barcode`, `barcodefor`, `isactive`, `lastupdts`, `lastupduser`)
VALUES
	(1,40,0,'QER344555',NULL,NULL,'2012-12-20 11:07:21',1),
	(2,41,0,'QWE34555',NULL,NULL,'2012-12-20 11:07:41',1),
	(3,42,0,'FRE45676678',NULL,NULL,'2012-12-20 11:08:04',1),
	(4,43,0,'C126NACXX','WASP',1,'2012-12-20 11:09:20',1),
	(5,44,0,'D1884ACXX','WASP',1,'2012-12-20 11:09:48',1),
	(6,45,0,'634H7AAXX','WASP',1,'2012-12-20 11:10:27',1);

/*!40000 ALTER TABLE `barcode_AUD` ENABLE KEYS */;
UNLOCK TABLES;


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
  CONSTRAINT `JOB_INST_EXEC_FK` FOREIGN KEY (`JOB_INSTANCE_ID`) REFERENCES `BATCH_JOB_INSTANCE` (`JOB_INSTANCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `BATCH_JOB_EXECUTION` WRITE;
/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION` DISABLE KEYS */;

INSERT INTO `BATCH_JOB_EXECUTION` (`JOB_EXECUTION_ID`, `VERSION`, `JOB_INSTANCE_ID`, `CREATE_TIME`, `START_TIME`, `END_TIME`, `STATUS`, `EXIT_CODE`, `EXIT_MESSAGE`, `LAST_UPDATED`)
VALUES
	(1,1,1,'2012-12-20 11:20:04','2012-12-20 11:20:05',NULL,'STARTED','UNKNOWN','','2012-12-20 11:20:05'),
	(2,2,2,'2012-12-20 11:20:05','2012-12-20 11:20:05','2012-12-20 11:23:44','COMPLETED','COMPLETED','','2012-12-20 11:23:44'),
	(3,2,3,'2012-12-20 11:20:05','2012-12-20 11:20:05','2012-12-20 11:23:47','COMPLETED','COMPLETED','','2012-12-20 11:23:47'),
	(4,2,4,'2012-12-20 11:20:05','2012-12-20 11:20:05','2012-12-20 11:23:34','COMPLETED','COMPLETED','','2012-12-20 11:23:34'),
	(5,1,5,'2012-12-20 11:24:31','2012-12-20 11:24:31',NULL,'STARTED','UNKNOWN','','2012-12-20 11:24:31'),
	(6,1,6,'2012-12-20 11:27:07','2012-12-20 11:27:07',NULL,'STARTED','UNKNOWN','','2012-12-20 11:27:07'),
	(7,1,7,'2012-12-20 11:27:07','2012-12-20 11:27:07',NULL,'STARTED','UNKNOWN','','2012-12-20 11:27:07'),
	(8,1,8,'2012-12-20 11:27:07','2012-12-20 11:27:07',NULL,'STARTED','UNKNOWN','','2012-12-20 11:27:07');

/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table BATCH_JOB_EXECUTION_CONTEXT
# ------------------------------------------------------------

DROP TABLE IF EXISTS `BATCH_JOB_EXECUTION_CONTEXT`;

CREATE TABLE `BATCH_JOB_EXECUTION_CONTEXT` (
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `SHORT_CONTEXT` varchar(2500) NOT NULL,
  `SERIALIZED_CONTEXT` text,
  PRIMARY KEY (`JOB_EXECUTION_ID`),
  CONSTRAINT `JOB_EXEC_CTX_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `BATCH_JOB_EXECUTION` (`JOB_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `BATCH_JOB_EXECUTION_CONTEXT` WRITE;
/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION_CONTEXT` DISABLE KEYS */;

INSERT INTO `BATCH_JOB_EXECUTION_CONTEXT` (`JOB_EXECUTION_ID`, `SHORT_CONTEXT`, `SERIALIZED_CONTEXT`)
VALUES
	(1,'{\"map\":\"\"}',NULL),
	(2,'{\"map\":\"\"}',NULL),
	(3,'{\"map\":\"\"}',NULL),
	(4,'{\"map\":\"\"}',NULL),
	(5,'{\"map\":\"\"}',NULL),
	(6,'{\"map\":\"\"}',NULL),
	(7,'{\"map\":\"\"}',NULL),
	(8,'{\"map\":\"\"}',NULL);

/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION_CONTEXT` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table BATCH_JOB_EXECUTION_SEQ
# ------------------------------------------------------------

DROP TABLE IF EXISTS `BATCH_JOB_EXECUTION_SEQ`;

CREATE TABLE `BATCH_JOB_EXECUTION_SEQ` (
  `ID` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `BATCH_JOB_EXECUTION_SEQ` WRITE;
/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION_SEQ` DISABLE KEYS */;

INSERT INTO `BATCH_JOB_EXECUTION_SEQ` (`ID`)
VALUES
	(8);

/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION_SEQ` ENABLE KEYS */;
UNLOCK TABLES;


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

LOCK TABLES `BATCH_JOB_INSTANCE` WRITE;
/*!40000 ALTER TABLE `BATCH_JOB_INSTANCE` DISABLE KEYS */;

INSERT INTO `BATCH_JOB_INSTANCE` (`JOB_INSTANCE_ID`, `VERSION`, `JOB_NAME`, `JOB_KEY`)
VALUES
	(1,0,'default.waspJob.jobflow.v1','3f794b8fbab28919466a7935e6eca9de'),
	(2,0,'wasp.sample.jobflow.v1','51334d32df46a49f5100d2f4b05a3075'),
	(3,0,'wasp.sample.jobflow.v1','8bbc65792cb26a927825ca2b3e8b6013'),
	(4,0,'wasp.sample.jobflow.v1','bc9287957155fa18e0927a383947448d'),
	(5,0,'wasp.facilityLibrary.jobflow.v1','734d58c5ddb306e1be1713c8a1f584bd'),
	(6,0,'default.waspJob.jobflow.v1','51ced8531d8f43d8574461e2ab140593'),
	(7,0,'wasp.userLibrary.jobflow.v1','50f1d00f0e7c89f7dbc0da1db6bcc79f'),
	(8,0,'wasp.userLibrary.jobflow.v1','5e0221792fa693d721979abaeb0a073f');

/*!40000 ALTER TABLE `BATCH_JOB_INSTANCE` ENABLE KEYS */;
UNLOCK TABLES;


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
  CONSTRAINT `JOB_INST_PARAMS_FK` FOREIGN KEY (`JOB_INSTANCE_ID`) REFERENCES `BATCH_JOB_INSTANCE` (`JOB_INSTANCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `BATCH_JOB_PARAMS` WRITE;
/*!40000 ALTER TABLE `BATCH_JOB_PARAMS` DISABLE KEYS */;

INSERT INTO `BATCH_JOB_PARAMS` (`JOB_INSTANCE_ID`, `TYPE_CD`, `KEY_NAME`, `STRING_VAL`, `DATE_VAL`, `LONG_VAL`, `DOUBLE_VAL`)
VALUES
	(1,'STRING','jobId','1','1969-12-31 19:00:00',0,0),
	(2,'STRING','jobId','1','1969-12-31 19:00:00',0,0),
	(2,'STRING','sampleId','21','1969-12-31 19:00:00',0,0),
	(3,'STRING','jobId','1','1969-12-31 19:00:00',0,0),
	(3,'STRING','sampleId','22','1969-12-31 19:00:00',0,0),
	(4,'STRING','jobId','1','1969-12-31 19:00:00',0,0),
	(4,'STRING','sampleId','23','1969-12-31 19:00:00',0,0),
	(5,'STRING','jobId','1','1969-12-31 19:00:00',0,0),
	(5,'STRING','sampleId','24','1969-12-31 19:00:00',0,0),
	(6,'STRING','jobId','2','1969-12-31 19:00:00',0,0),
	(7,'STRING','jobId','2','1969-12-31 19:00:00',0,0),
	(7,'STRING','sampleId','25','1969-12-31 19:00:00',0,0),
	(8,'STRING','jobId','2','1969-12-31 19:00:00',0,0),
	(8,'STRING','sampleId','26','1969-12-31 19:00:00',0,0);

/*!40000 ALTER TABLE `BATCH_JOB_PARAMS` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table BATCH_JOB_SEQ
# ------------------------------------------------------------

DROP TABLE IF EXISTS `BATCH_JOB_SEQ`;

CREATE TABLE `BATCH_JOB_SEQ` (
  `ID` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `BATCH_JOB_SEQ` WRITE;
/*!40000 ALTER TABLE `BATCH_JOB_SEQ` DISABLE KEYS */;

INSERT INTO `BATCH_JOB_SEQ` (`ID`)
VALUES
	(8);

/*!40000 ALTER TABLE `BATCH_JOB_SEQ` ENABLE KEYS */;
UNLOCK TABLES;


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
  CONSTRAINT `JOB_EXEC_STEP_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `BATCH_JOB_EXECUTION` (`JOB_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `BATCH_STEP_EXECUTION` WRITE;
/*!40000 ALTER TABLE `BATCH_STEP_EXECUTION` DISABLE KEYS */;

INSERT INTO `BATCH_STEP_EXECUTION` (`STEP_EXECUTION_ID`, `VERSION`, `STEP_NAME`, `JOB_EXECUTION_ID`, `START_TIME`, `END_TIME`, `STATUS`, `COMMIT_COUNT`, `READ_COUNT`, `FILTER_COUNT`, `WRITE_COUNT`, `READ_SKIP_COUNT`, `WRITE_SKIP_COUNT`, `PROCESS_SKIP_COUNT`, `ROLLBACK_COUNT`, `EXIT_CODE`, `EXIT_MESSAGE`, `LAST_UPDATED`)
VALUES
	(1,85225,'default.waspJob.step.listenForExitCondition',1,'2012-12-20 11:20:05',NULL,'STARTED',85224,0,0,0,0,0,0,0,'EXECUTING','','2012-12-20 11:34:11'),
	(2,5798,'default.waspJob.step.adminApprove',1,'2012-12-20 11:20:05','2012-12-20 11:21:12','COMPLETED',5796,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:21:12'),
	(3,13243,'default.waspJob.step.quote',1,'2012-12-20 11:20:05','2012-12-20 11:22:17','COMPLETED',13241,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:22:17'),
	(4,14330,'default.waspJob.step.piApprove',1,'2012-12-20 11:20:05','2012-12-20 11:22:25','COMPLETED',14328,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:22:25'),
	(5,24717,'wasp.sample.step.listenForExitCondition',2,'2012-12-20 11:20:05','2012-12-20 11:23:44','COMPLETED',24715,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:23:44'),
	(6,14375,'wasp.sample.step.listenForJobApproved',2,'2012-12-20 11:20:05','2012-12-20 11:22:25','COMPLETED',14373,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:22:25'),
	(7,4342,'wasp.sample.step.listenForSampleReceived',2,'2012-12-20 11:20:05','2012-12-20 11:20:58','COMPLETED',4340,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:20:58'),
	(8,4462,'wasp.sample.step.listenForSampleReceived',3,'2012-12-20 11:20:05','2012-12-20 11:20:59','COMPLETED',4460,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:20:59'),
	(9,14385,'wasp.sample.step.listenForJobApproved',3,'2012-12-20 11:20:05','2012-12-20 11:22:25','COMPLETED',14383,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:22:25'),
	(10,25070,'wasp.sample.step.listenForExitCondition',3,'2012-12-20 11:20:05','2012-12-20 11:23:47','COMPLETED',25068,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:23:47'),
	(11,14367,'wasp.sample.step.listenForJobApproved',4,'2012-12-20 11:20:05','2012-12-20 11:22:25','COMPLETED',14365,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:22:25'),
	(12,23271,'wasp.sample.step.listenForExitCondition',4,'2012-12-20 11:20:05','2012-12-20 11:23:34','COMPLETED',23269,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:23:34'),
	(13,4529,'wasp.sample.step.listenForSampleReceived',4,'2012-12-20 11:20:05','2012-12-20 11:21:00','COMPLETED',4527,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:21:00'),
	(14,3,'default.waspJob.step.approved',1,'2012-12-20 11:22:25','2012-12-20 11:22:25','COMPLETED',1,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:22:25'),
	(15,10745,'wasp.sample.step.sampleQC',3,'2012-12-20 11:22:25','2012-12-20 11:23:47','COMPLETED',10743,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:23:47'),
	(16,10354,'wasp.sample.step.sampleQC',2,'2012-12-20 11:22:25','2012-12-20 11:23:44','COMPLETED',10352,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:23:44'),
	(17,8953,'wasp.sample.step.sampleQC',4,'2012-12-20 11:22:25','2012-12-20 11:23:34','COMPLETED',8951,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:23:34'),
	(18,3,'wasp.sample.step.notifySampleAccepted',4,'2012-12-20 11:23:34','2012-12-20 11:23:34','COMPLETED',1,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:23:34'),
	(19,3,'wasp.sample.step.notifySampleAccepted',2,'2012-12-20 11:23:44','2012-12-20 11:23:44','COMPLETED',1,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:23:44'),
	(20,3,'wasp.sample.step.notifySampleAccepted',3,'2012-12-20 11:23:47','2012-12-20 11:23:47','COMPLETED',1,0,0,0,0,0,0,0,'COMPLETED','','2012-12-20 11:23:47'),
	(21,54261,'wasp.library.step.listenForExitCondition',5,'2012-12-20 11:24:31',NULL,'STARTED',54260,0,0,0,0,0,0,0,'EXECUTING','','2012-12-20 11:34:11'),
	(22,54271,'wasp.library.step.libraryQC',5,'2012-12-20 11:24:31',NULL,'STARTED',54270,0,0,0,0,0,0,0,'EXECUTING','','2012-12-20 11:34:11'),
	(23,33286,'default.waspJob.step.listenForExitCondition',6,'2012-12-20 11:27:07',NULL,'STARTED',33285,0,0,0,0,0,0,0,'EXECUTING','','2012-12-20 11:34:11'),
	(24,33356,'default.waspJob.step.quote',6,'2012-12-20 11:27:07',NULL,'STARTED',33355,0,0,0,0,0,0,0,'EXECUTING','','2012-12-20 11:34:11'),
	(25,33271,'default.waspJob.step.piApprove',6,'2012-12-20 11:27:07',NULL,'STARTED',33270,0,0,0,0,0,0,0,'EXECUTING','','2012-12-20 11:34:11'),
	(26,33273,'default.waspJob.step.adminApprove',6,'2012-12-20 11:27:07',NULL,'STARTED',33272,0,0,0,0,0,0,0,'EXECUTING','','2012-12-20 11:34:11'),
	(27,33376,'wasp.library.step.listenForExitCondition',7,'2012-12-20 11:27:07',NULL,'STARTED',33375,0,0,0,0,0,0,0,'EXECUTING','','2012-12-20 11:34:11'),
	(28,33212,'wasp.library.step.listenForJobApproved',7,'2012-12-20 11:27:07',NULL,'STARTED',33211,0,0,0,0,0,0,0,'EXECUTING','','2012-12-20 11:34:11'),
	(29,33291,'wasp.library.step.listenForLibraryReceived',7,'2012-12-20 11:27:07',NULL,'STARTED',33290,0,0,0,0,0,0,0,'EXECUTING','','2012-12-20 11:34:11'),
	(30,33220,'wasp.library.step.listenForJobApproved',8,'2012-12-20 11:27:07',NULL,'STARTED',33219,0,0,0,0,0,0,0,'EXECUTING','','2012-12-20 11:34:11'),
	(31,33173,'wasp.library.step.listenForLibraryReceived',8,'2012-12-20 11:27:07',NULL,'STARTED',33172,0,0,0,0,0,0,0,'EXECUTING','','2012-12-20 11:34:11'),
	(32,33200,'wasp.library.step.listenForExitCondition',8,'2012-12-20 11:27:07',NULL,'STARTED',33199,0,0,0,0,0,0,0,'EXECUTING','','2012-12-20 11:34:11');

/*!40000 ALTER TABLE `BATCH_STEP_EXECUTION` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table BATCH_STEP_EXECUTION_CONTEXT
# ------------------------------------------------------------

DROP TABLE IF EXISTS `BATCH_STEP_EXECUTION_CONTEXT`;

CREATE TABLE `BATCH_STEP_EXECUTION_CONTEXT` (
  `STEP_EXECUTION_ID` bigint(20) NOT NULL,
  `SHORT_CONTEXT` varchar(2500) NOT NULL,
  `SERIALIZED_CONTEXT` text,
  PRIMARY KEY (`STEP_EXECUTION_ID`),
  CONSTRAINT `STEP_EXEC_CTX_FK` FOREIGN KEY (`STEP_EXECUTION_ID`) REFERENCES `BATCH_STEP_EXECUTION` (`STEP_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `BATCH_STEP_EXECUTION_CONTEXT` WRITE;
/*!40000 ALTER TABLE `BATCH_STEP_EXECUTION_CONTEXT` DISABLE KEYS */;

INSERT INTO `BATCH_STEP_EXECUTION_CONTEXT` (`STEP_EXECUTION_ID`, `SHORT_CONTEXT`, `SERIALIZED_CONTEXT`)
VALUES
	(1,'{\"map\":\"\"}',NULL),
	(2,'{\"map\":\"\"}',NULL),
	(3,'{\"map\":\"\"}',NULL),
	(4,'{\"map\":\"\"}',NULL),
	(5,'{\"map\":\"\"}',NULL),
	(6,'{\"map\":\"\"}',NULL),
	(7,'{\"map\":\"\"}',NULL),
	(8,'{\"map\":\"\"}',NULL),
	(9,'{\"map\":\"\"}',NULL),
	(10,'{\"map\":\"\"}',NULL),
	(11,'{\"map\":\"\"}',NULL),
	(12,'{\"map\":\"\"}',NULL),
	(13,'{\"map\":\"\"}',NULL),
	(14,'{\"map\":\"\"}',NULL),
	(15,'{\"map\":\"\"}',NULL),
	(16,'{\"map\":\"\"}',NULL),
	(17,'{\"map\":\"\"}',NULL),
	(18,'{\"map\":\"\"}',NULL),
	(19,'{\"map\":\"\"}',NULL),
	(20,'{\"map\":\"\"}',NULL),
	(21,'{\"map\":\"\"}',NULL),
	(22,'{\"map\":\"\"}',NULL),
	(23,'{\"map\":\"\"}',NULL),
	(24,'{\"map\":\"\"}',NULL),
	(25,'{\"map\":\"\"}',NULL),
	(26,'{\"map\":\"\"}',NULL),
	(27,'{\"map\":\"\"}',NULL),
	(28,'{\"map\":\"\"}',NULL),
	(29,'{\"map\":\"\"}',NULL),
	(30,'{\"map\":\"\"}',NULL),
	(31,'{\"map\":\"\"}',NULL),
	(32,'{\"map\":\"\"}',NULL);

/*!40000 ALTER TABLE `BATCH_STEP_EXECUTION_CONTEXT` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table BATCH_STEP_EXECUTION_SEQ
# ------------------------------------------------------------

DROP TABLE IF EXISTS `BATCH_STEP_EXECUTION_SEQ`;

CREATE TABLE `BATCH_STEP_EXECUTION_SEQ` (
  `ID` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `BATCH_STEP_EXECUTION_SEQ` WRITE;
/*!40000 ALTER TABLE `BATCH_STEP_EXECUTION_SEQ` DISABLE KEYS */;

INSERT INTO `BATCH_STEP_EXECUTION_SEQ` (`ID`)
VALUES
	(32);

/*!40000 ALTER TABLE `BATCH_STEP_EXECUTION_SEQ` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table confirmemailauth
# ------------------------------------------------------------

DROP TABLE IF EXISTS `confirmemailauth`;

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



# Dump of table confirmemailauth_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `confirmemailauth_AUD`;

CREATE TABLE `confirmemailauth_AUD` (
  `confirmEmailAuthId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `authcode` varchar(255) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `userpendingid` int(11) DEFAULT NULL,
  PRIMARY KEY (`confirmEmailAuthId`,`REV`),
  KEY `FKEE29F855DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table department
# ------------------------------------------------------------

DROP TABLE IF EXISTS `department`;

CREATE TABLE `department` (
  `departmentid` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(250) DEFAULT NULL,
  `isinternal` int(1) DEFAULT '1',
  `isactive` int(1) DEFAULT '1',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`departmentid`),
  UNIQUE KEY `u_department_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;

INSERT INTO `department` (`departmentid`, `name`, `isinternal`, `isactive`, `lastupdts`, `lastupduser`)
VALUES
	(1,'Internal - Default Department',1,1,'2012-05-23 15:55:35',1),
	(2,'External - Default Department',0,1,'2012-05-23 15:55:35',1),
	(3,'Genetics',1,1,'2012-06-14 13:47:08',1);

/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table department_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `department_AUD`;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table departmentuser
# ------------------------------------------------------------

DROP TABLE IF EXISTS `departmentuser`;

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

LOCK TABLES `departmentuser` WRITE;
/*!40000 ALTER TABLE `departmentuser` DISABLE KEYS */;

INSERT INTO `departmentuser` (`departmentuserid`, `departmentid`, `userid`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,3,'2012-05-30 19:57:15',1),
	(2,3,3,'2012-06-14 13:47:08',1),
	(3,2,13,'2012-06-14 14:13:04',1);

/*!40000 ALTER TABLE `departmentuser` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table departmentuser_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `departmentuser_AUD`;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table file
# ------------------------------------------------------------

DROP TABLE IF EXISTS `file`;

CREATE TABLE `file` (
  `fileid` int(10) NOT NULL AUTO_INCREMENT,
  `file_uri` varchar(2048) DEFAULT NULL,
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



# Dump of table file_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `file_AUD`;

CREATE TABLE `file_AUD` (
  `fileId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `contenttype` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `file_uri` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `isarchived` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `md5hash` varchar(255) DEFAULT NULL,
  `sizek` int(11) DEFAULT NULL,
  PRIMARY KEY (`fileId`,`REV`),
  KEY `FKD42D054DDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table filemeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `filemeta`;

CREATE TABLE `filemeta` (
  `filemetaid` int(10) NOT NULL AUTO_INCREMENT,
  `fileid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` text,
  `position` int(10) DEFAULT '0',
  `rolevisibility` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`filemetaid`),
  UNIQUE KEY `u_filemeta_k_jid` (`k`,`fileid`),
  KEY `fk_filemeta_fileid` (`fileid`),
  CONSTRAINT `filemeta_ibfk_1` FOREIGN KEY (`fileid`) REFERENCES `file` (`fileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table filemeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `filemeta_AUD`;

CREATE TABLE `filemeta_AUD` (
  `fileMetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `fileid` int(11) DEFAULT NULL,
  PRIMARY KEY (`fileMetaId`,`REV`),
  KEY `FK95BB72D2DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table job
# ------------------------------------------------------------

DROP TABLE IF EXISTS `job`;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `job` WRITE;
/*!40000 ALTER TABLE `job` DISABLE KEYS */;

INSERT INTO `job` (`jobid`, `labid`, `userid`, `workflowid`, `name`, `createts`, `viewablebylab`, `isactive`, `lastupdts`, `lastupduser`)
VALUES
	(1,2,10,2,'chipseq1','2012-12-20 11:20:04',0,1,'2012-12-20 11:20:04',10),
	(2,2,10,2,'chipseq2','2012-12-20 11:27:07',0,1,'2012-12-20 11:27:07',10);

/*!40000 ALTER TABLE `job` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table job_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `job_AUD`;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `job_AUD` WRITE;
/*!40000 ALTER TABLE `job_AUD` DISABLE KEYS */;

INSERT INTO `job_AUD` (`jobId`, `REV`, `REVTYPE`, `userid`, `createts`, `isactive`, `labid`, `lastupdts`, `lastupduser`, `name`, `viewablebylab`, `workflowid`)
VALUES
	(1,63,0,10,'2012-12-20 11:20:04',1,2,'2012-12-20 11:20:04',10,'chipseq1',0,2),
	(2,78,0,10,'2012-12-20 11:27:07',1,2,'2012-12-20 11:27:07',10,'chipseq2',0,2);

/*!40000 ALTER TABLE `job_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobcellselection
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobcellselection`;

CREATE TABLE `jobcellselection` (
  `jobcellselectionid` int(10) NOT NULL AUTO_INCREMENT,
  `jobid` int(10) DEFAULT NULL,
  `cellindex` int(10) DEFAULT NULL,
  PRIMARY KEY (`jobcellselectionid`),
  UNIQUE KEY `u_jobcell_jdid_ci` (`jobid`,`cellindex`),
  KEY `FK926175CD8FCE445E` (`jobid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `jobcellselection` WRITE;
/*!40000 ALTER TABLE `jobcellselection` DISABLE KEYS */;

INSERT INTO `jobcellselection` (`jobcellselectionid`, `jobid`, `cellindex`)
VALUES
	(1,1,1),
	(2,2,1),
	(3,2,2);

/*!40000 ALTER TABLE `jobcellselection` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobcellselection_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobcellselection_AUD`;

CREATE TABLE `jobcellselection_AUD` (
  `jobCellSelectionId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `cellindex` int(11) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  PRIMARY KEY (`jobCellSelectionId`,`REV`),
  KEY `FKF613F51EDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `jobcellselection_AUD` WRITE;
/*!40000 ALTER TABLE `jobcellselection_AUD` DISABLE KEYS */;

INSERT INTO `jobcellselection_AUD` (`jobCellSelectionId`, `REV`, `REVTYPE`, `cellindex`, `jobid`)
VALUES
	(1,63,0,1,1),
	(2,78,0,1,2),
	(3,78,0,2,2);

/*!40000 ALTER TABLE `jobcellselection_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobdraft
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobdraft`;

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

LOCK TABLES `jobdraft` WRITE;
/*!40000 ALTER TABLE `jobdraft` DISABLE KEYS */;

INSERT INTO `jobdraft` (`jobdraftid`, `labid`, `userid`, `workflowid`, `name`, `createts`, `submittedjobid`, `status`, `lastupdts`, `lastupduser`)
VALUES
	(1,2,10,2,'chipseq1','2012-12-20 11:15:26',1,'SUBMITTED','2012-12-20 11:20:04',10),
	(2,2,10,2,'chipseq2','2012-12-20 11:25:19',2,'SUBMITTED','2012-12-20 11:27:07',10);

/*!40000 ALTER TABLE `jobdraft` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobdraft_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobdraft_AUD`;

CREATE TABLE `jobdraft_AUD` (
  `jobDraftId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `createts` datetime DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `submittedjobid` int(11) DEFAULT NULL,
  `workflowid` int(11) DEFAULT NULL,
  PRIMARY KEY (`jobDraftId`,`REV`),
  KEY `FK2FB037D5DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `jobdraft_AUD` WRITE;
/*!40000 ALTER TABLE `jobdraft_AUD` DISABLE KEYS */;

INSERT INTO `jobdraft_AUD` (`jobDraftId`, `REV`, `REVTYPE`, `userid`, `createts`, `labid`, `lastupdts`, `lastupduser`, `name`, `status`, `submittedjobid`, `workflowid`)
VALUES
	(1,46,0,10,'2012-12-20 11:15:26',2,'2012-12-20 11:15:26',10,'chipseq1','PENDING',NULL,2),
	(1,63,1,10,'2012-12-20 11:15:26',2,'2012-12-20 11:20:04',10,'chipseq1','SUBMITTED',1,2),
	(2,67,0,10,'2012-12-20 11:25:19',2,'2012-12-20 11:25:19',10,'chipseq2','PENDING',NULL,2),
	(2,78,1,10,'2012-12-20 11:25:19',2,'2012-12-20 11:27:07',10,'chipseq2','SUBMITTED',2,2);

/*!40000 ALTER TABLE `jobdraft_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobdraftcellselection
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobdraftcellselection`;

CREATE TABLE `jobdraftcellselection` (
  `jobdraftcellselectionid` int(10) NOT NULL AUTO_INCREMENT,
  `jobdraftid` int(10) DEFAULT NULL,
  `cellindex` int(10) DEFAULT NULL,
  PRIMARY KEY (`jobdraftcellselectionid`),
  UNIQUE KEY `u_jobdraftcell_jdid_ci` (`jobdraftid`,`cellindex`),
  KEY `FK737E50E6E3C3069A` (`jobdraftid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `jobdraftcellselection` WRITE;
/*!40000 ALTER TABLE `jobdraftcellselection` DISABLE KEYS */;

INSERT INTO `jobdraftcellselection` (`jobdraftcellselectionid`, `jobdraftid`, `cellindex`)
VALUES
	(1,1,1),
	(2,2,1),
	(3,2,2);

/*!40000 ALTER TABLE `jobdraftcellselection` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobdraftcellselection_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobdraftcellselection_AUD`;

CREATE TABLE `jobdraftcellselection_AUD` (
  `jobDraftCellSelectionId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `cellindex` int(11) DEFAULT NULL,
  `jobdraftid` int(11) DEFAULT NULL,
  PRIMARY KEY (`jobDraftCellSelectionId`,`REV`),
  KEY `FKF8AB9BB7DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `jobdraftcellselection_AUD` WRITE;
/*!40000 ALTER TABLE `jobdraftcellselection_AUD` DISABLE KEYS */;

INSERT INTO `jobdraftcellselection_AUD` (`jobDraftCellSelectionId`, `REV`, `REVTYPE`, `cellindex`, `jobdraftid`)
VALUES
	(1,56,0,1,1),
	(2,72,0,1,2),
	(3,72,0,2,2);

/*!40000 ALTER TABLE `jobdraftcellselection_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobdraftfile
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobdraftfile`;

CREATE TABLE `jobdraftfile` (
  `jobdraftfileid` int(10) NOT NULL AUTO_INCREMENT,
  `jobdraftid` int(10) DEFAULT NULL,
  `fileid` int(10) DEFAULT NULL,
  `iname` varchar(2048) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `description` varchar(2048) DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`jobdraftfileid`),
  KEY `fk_jobdraftfile_jid` (`jobdraftid`),
  KEY `fk_jobdraftfile_fid` (`fileid`),
  CONSTRAINT `jobdraftfile_ibfk_1` FOREIGN KEY (`jobdraftid`) REFERENCES `jobdraft` (`jobdraftid`),
  CONSTRAINT `jobdraftfile_ibfk_2` FOREIGN KEY (`fileid`) REFERENCES `file` (`fileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table jobdraftfile_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobdraftfile_AUD`;

CREATE TABLE `jobdraftfile_AUD` (
  `jobDraftFileId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `fileid` int(11) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `jobdraftid` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`jobDraftFileId`,`REV`),
  KEY `FK2ED46D51DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table jobdraftmeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobdraftmeta`;

CREATE TABLE `jobdraftmeta` (
  `jobdraftmetaid` int(10) NOT NULL AUTO_INCREMENT,
  `jobdraftid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` text,
  `position` int(10) DEFAULT '0',
  `rolevisibility` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`jobdraftmetaid`),
  UNIQUE KEY `u_jobdraftmeta_k_jdid` (`k`,`jobdraftid`),
  KEY `fk_jobdraftmeta_jdid` (`jobdraftid`),
  CONSTRAINT `jobdraftmeta_ibfk_1` FOREIGN KEY (`jobdraftid`) REFERENCES `jobdraft` (`jobdraftid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `jobdraftmeta` WRITE;
/*!40000 ALTER TABLE `jobdraftmeta` DISABLE KEYS */;

INSERT INTO `jobdraftmeta` (`jobdraftmetaid`, `jobdraftid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,'illuminaHiSeq2000.readLength','100',0,NULL,'2012-12-20 11:15:35',NULL),
	(2,1,'illuminaHiSeq2000.readType','paired',0,NULL,'2012-12-20 11:15:35',NULL),
	(3,1,'chipSeqPlugin.samplePairsTvsC','1:3;2:3;',0,NULL,'2012-12-20 11:18:37',10),
	(4,1,'bowtieAligner.mismatches','2',0,NULL,'2012-12-20 11:18:41',NULL),
	(5,1,'bowtieAligner.seedLength','32',0,NULL,'2012-12-20 11:18:41',NULL),
	(6,1,'bowtieAligner.reportAlignmentNum','1',0,NULL,'2012-12-20 11:18:41',NULL),
	(7,1,'bowtieAligner.discardThreshold','1',0,NULL,'2012-12-20 11:18:41',NULL),
	(8,1,'bowtieAligner.isBest','yes',0,NULL,'2012-12-20 11:18:41',NULL),
	(9,1,'macsPeakcaller.pValueCutoff','100000',0,NULL,'2012-12-20 11:19:39',NULL),
	(10,1,'macsPeakcaller.bandwidth','300',0,NULL,'2012-12-20 11:19:39',NULL),
	(11,1,'macsPeakcaller.genomeSize','1000000000',0,NULL,'2012-12-20 11:19:39',NULL),
	(12,1,'macsPeakcaller.keepDup','no',0,NULL,'2012-12-20 11:19:39',NULL),
	(13,1,'statusMessage.userSubmittedJobComment::0982004b-2bcf-490e-83a0-b0e86fb8293f','User-submitted Job Comment::Please expedite. Grant deadline approaching!',0,NULL,'2012-12-20 11:20:03',10),
	(14,2,'illuminaHiSeq2000.readLength','100',0,NULL,'2012-12-20 11:25:27',NULL),
	(15,2,'illuminaHiSeq2000.readType','paired',0,NULL,'2012-12-20 11:25:27',NULL),
	(16,2,'chipSeqPlugin.samplePairsTvsC','5:4;',0,NULL,'2012-12-20 11:26:56',10),
	(17,2,'bowtieAligner.mismatches','2',0,NULL,'2012-12-20 11:26:59',NULL),
	(18,2,'bowtieAligner.seedLength','32',0,NULL,'2012-12-20 11:26:59',NULL),
	(19,2,'bowtieAligner.reportAlignmentNum','1',0,NULL,'2012-12-20 11:26:59',NULL),
	(20,2,'bowtieAligner.discardThreshold','1',0,NULL,'2012-12-20 11:26:59',NULL),
	(21,2,'bowtieAligner.isBest','yes',0,NULL,'2012-12-20 11:26:59',NULL),
	(22,2,'macsPeakcaller.pValueCutoff','100000',0,NULL,'2012-12-20 11:27:02',NULL),
	(23,2,'macsPeakcaller.bandwidth','300',0,NULL,'2012-12-20 11:27:02',NULL),
	(24,2,'macsPeakcaller.genomeSize','1000000000',0,NULL,'2012-12-20 11:27:02',NULL),
	(25,2,'macsPeakcaller.keepDup','no',0,NULL,'2012-12-20 11:27:02',NULL);

/*!40000 ALTER TABLE `jobdraftmeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobdraftmeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobdraftmeta_AUD`;

CREATE TABLE `jobdraftmeta_AUD` (
  `jobDraftMetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `jobdraftid` int(11) DEFAULT NULL,
  PRIMARY KEY (`jobDraftMetaId`,`REV`),
  KEY `FK3FD2215ADF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `jobdraftmeta_AUD` WRITE;
/*!40000 ALTER TABLE `jobdraftmeta_AUD` DISABLE KEYS */;

INSERT INTO `jobdraftmeta_AUD` (`jobDraftMetaId`, `REV`, `REVTYPE`, `jobdraftid`)
VALUES
	(1,48,0,1),
	(2,48,0,1),
	(3,57,0,1),
	(4,59,0,1),
	(5,59,0,1),
	(6,59,0,1),
	(7,59,0,1),
	(8,59,0,1),
	(9,61,0,1),
	(10,61,0,1),
	(11,61,0,1),
	(12,61,0,1),
	(13,62,0,1),
	(14,69,0,2),
	(15,69,0,2),
	(16,73,0,2),
	(17,75,0,2),
	(18,75,0,2),
	(19,75,0,2),
	(20,75,0,2),
	(21,75,0,2),
	(22,77,0,2),
	(23,77,0,2),
	(24,77,0,2),
	(25,77,0,2);

/*!40000 ALTER TABLE `jobdraftmeta_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobdraftresourcecategory
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobdraftresourcecategory`;

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

LOCK TABLES `jobdraftresourcecategory` WRITE;
/*!40000 ALTER TABLE `jobdraftresourcecategory` DISABLE KEYS */;

INSERT INTO `jobdraftresourcecategory` (`jobdraftresourcecategoryid`, `jobdraftid`, `resourcecategoryid`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,1,'2012-12-20 11:15:30',10),
	(2,2,1,'2012-12-20 11:25:22',10);

/*!40000 ALTER TABLE `jobdraftresourcecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobdraftresourcecategory_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobdraftresourcecategory_AUD`;

CREATE TABLE `jobdraftresourcecategory_AUD` (
  `jobDraftresourcecategoryId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `jobdraftid` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `resourcecategoryid` int(11) DEFAULT NULL,
  PRIMARY KEY (`jobDraftresourcecategoryId`,`REV`),
  KEY `FKC593DCE1DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `jobdraftresourcecategory_AUD` WRITE;
/*!40000 ALTER TABLE `jobdraftresourcecategory_AUD` DISABLE KEYS */;

INSERT INTO `jobdraftresourcecategory_AUD` (`jobDraftresourcecategoryId`, `REV`, `REVTYPE`, `jobdraftid`, `lastupdts`, `lastupduser`, `resourcecategoryid`)
VALUES
	(1,47,0,1,'2012-12-20 11:15:30',10,1),
	(2,68,0,2,'2012-12-20 11:25:22',10,1);

/*!40000 ALTER TABLE `jobdraftresourcecategory_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobdraftsoftware
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobdraftsoftware`;

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

LOCK TABLES `jobdraftsoftware` WRITE;
/*!40000 ALTER TABLE `jobdraftsoftware` DISABLE KEYS */;

INSERT INTO `jobdraftsoftware` (`jobdraftsoftwareid`, `jobdraftid`, `softwareid`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,1,'2012-12-20 11:18:39',10),
	(2,1,2,'2012-12-20 11:18:43',10),
	(3,2,1,'2012-12-20 11:26:58',10),
	(4,2,2,'2012-12-20 11:27:01',10);

/*!40000 ALTER TABLE `jobdraftsoftware` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobdraftsoftware_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobdraftsoftware_AUD`;

CREATE TABLE `jobdraftsoftware_AUD` (
  `jobDraftSoftwareId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `jobdraftid` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `softwareid` int(11) DEFAULT NULL,
  PRIMARY KEY (`jobDraftSoftwareId`,`REV`),
  KEY `FKE5429FCDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `jobdraftsoftware_AUD` WRITE;
/*!40000 ALTER TABLE `jobdraftsoftware_AUD` DISABLE KEYS */;

INSERT INTO `jobdraftsoftware_AUD` (`jobDraftSoftwareId`, `REV`, `REVTYPE`, `jobdraftid`, `lastupdts`, `lastupduser`, `softwareid`)
VALUES
	(1,58,0,1,'2012-12-20 11:18:39',10,1),
	(2,60,0,1,'2012-12-20 11:18:43',10,2),
	(3,74,0,2,'2012-12-20 11:26:58',10,1),
	(4,76,0,2,'2012-12-20 11:27:01',10,2);

/*!40000 ALTER TABLE `jobdraftsoftware_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobfile
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobfile`;

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



# Dump of table jobfile_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobfile_AUD`;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table jobmeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobmeta`;

CREATE TABLE `jobmeta` (
  `jobmetaid` int(10) NOT NULL AUTO_INCREMENT,
  `jobid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` text,
  `position` int(10) DEFAULT '0',
  `rolevisibility` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`jobmetaid`),
  UNIQUE KEY `u_jobmeta_k_jid` (`k`,`jobid`),
  KEY `fk_jobmeta_jobid` (`jobid`),
  CONSTRAINT `jobmeta_ibfk_1` FOREIGN KEY (`jobid`) REFERENCES `job` (`jobid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `jobmeta` WRITE;
/*!40000 ALTER TABLE `jobmeta` DISABLE KEYS */;

INSERT INTO `jobmeta` (`jobmetaid`, `jobid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,'illuminaHiSeq2000.readLength','100',0,NULL,'2012-12-20 11:20:04',10),
	(2,1,'illuminaHiSeq2000.readType','paired',0,NULL,'2012-12-20 11:20:04',10),
	(3,1,'chipSeqPlugin.samplePairsTvsC','1:3;2:3;',0,NULL,'2012-12-20 11:20:04',10),
	(4,1,'bowtieAligner.mismatches','2',0,NULL,'2012-12-20 11:20:04',10),
	(5,1,'bowtieAligner.seedLength','32',0,NULL,'2012-12-20 11:20:04',10),
	(6,1,'bowtieAligner.reportAlignmentNum','1',0,NULL,'2012-12-20 11:20:04',10),
	(7,1,'bowtieAligner.discardThreshold','1',0,NULL,'2012-12-20 11:20:04',10),
	(8,1,'bowtieAligner.isBest','yes',0,NULL,'2012-12-20 11:20:04',10),
	(9,1,'macsPeakcaller.pValueCutoff','100000',0,NULL,'2012-12-20 11:20:04',10),
	(10,1,'macsPeakcaller.bandwidth','300',0,NULL,'2012-12-20 11:20:04',10),
	(11,1,'macsPeakcaller.genomeSize','1000000000',0,NULL,'2012-12-20 11:20:04',10),
	(12,1,'macsPeakcaller.keepDup','no',0,NULL,'2012-12-20 11:20:04',10),
	(13,1,'statusMessage.userSubmittedJobComment::0982004b-2bcf-490e-83a0-b0e86fb8293f','User-submitted Job Comment::Please expedite. Grant deadline approaching!',0,NULL,'2012-12-20 11:20:04',10),
	(14,2,'illuminaHiSeq2000.readLength','100',0,NULL,'2012-12-20 11:27:07',10),
	(15,2,'illuminaHiSeq2000.readType','paired',0,NULL,'2012-12-20 11:27:07',10),
	(16,2,'chipSeqPlugin.samplePairsTvsC','5:4;',0,NULL,'2012-12-20 11:27:07',10),
	(17,2,'bowtieAligner.mismatches','2',0,NULL,'2012-12-20 11:27:07',10),
	(18,2,'bowtieAligner.seedLength','32',0,NULL,'2012-12-20 11:27:07',10),
	(19,2,'bowtieAligner.reportAlignmentNum','1',0,NULL,'2012-12-20 11:27:07',10),
	(20,2,'bowtieAligner.discardThreshold','1',0,NULL,'2012-12-20 11:27:07',10),
	(21,2,'bowtieAligner.isBest','yes',0,NULL,'2012-12-20 11:27:07',10),
	(22,2,'macsPeakcaller.pValueCutoff','100000',0,NULL,'2012-12-20 11:27:07',10),
	(23,2,'macsPeakcaller.bandwidth','300',0,NULL,'2012-12-20 11:27:07',10),
	(24,2,'macsPeakcaller.genomeSize','1000000000',0,NULL,'2012-12-20 11:27:07',10),
	(25,2,'macsPeakcaller.keepDup','no',0,NULL,'2012-12-20 11:27:07',10);

/*!40000 ALTER TABLE `jobmeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobmeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobmeta_AUD`;

CREATE TABLE `jobmeta_AUD` (
  `jobMetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  PRIMARY KEY (`jobMetaId`,`REV`),
  KEY `FK39E74813DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `jobmeta_AUD` WRITE;
/*!40000 ALTER TABLE `jobmeta_AUD` DISABLE KEYS */;

INSERT INTO `jobmeta_AUD` (`jobMetaId`, `REV`, `REVTYPE`, `jobid`)
VALUES
	(1,63,0,1),
	(2,63,0,1),
	(3,63,0,1),
	(4,63,0,1),
	(5,63,0,1),
	(6,63,0,1),
	(7,63,0,1),
	(8,63,0,1),
	(9,63,0,1),
	(10,63,0,1),
	(11,63,0,1),
	(12,63,0,1),
	(13,63,0,1),
	(14,78,0,2),
	(15,78,0,2),
	(16,78,0,2),
	(17,78,0,2),
	(18,78,0,2),
	(19,78,0,2),
	(20,78,0,2),
	(21,78,0,2),
	(22,78,0,2),
	(23,78,0,2),
	(24,78,0,2),
	(25,78,0,2);

/*!40000 ALTER TABLE `jobmeta_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobresourcecategory
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobresourcecategory`;

CREATE TABLE `jobresourcecategory` (
  `jobresourcecategoryid` int(10) NOT NULL AUTO_INCREMENT,
  `jobid` int(10) DEFAULT NULL,
  `resourcecategoryid` int(10) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`jobresourcecategoryid`),
  UNIQUE KEY `u_jobresourcecategory_rcid_jid` (`resourcecategoryid`,`jobid`),
  KEY `fk_jobresourcecategory_jid` (`jobid`),
  CONSTRAINT `jobresourcecategory_ibfk_1` FOREIGN KEY (`jobid`) REFERENCES `job` (`jobid`),
  CONSTRAINT `jobresourcecategory_ibfk_2` FOREIGN KEY (`resourcecategoryid`) REFERENCES `resourcecategory` (`resourcecategoryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `jobresourcecategory` WRITE;
/*!40000 ALTER TABLE `jobresourcecategory` DISABLE KEYS */;

INSERT INTO `jobresourcecategory` (`jobresourcecategoryid`, `jobid`, `resourcecategoryid`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,1,'2012-12-20 11:20:04',10),
	(2,2,1,'2012-12-20 11:27:07',10);

/*!40000 ALTER TABLE `jobresourcecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobresourcecategory_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobresourcecategory_AUD`;

CREATE TABLE `jobresourcecategory_AUD` (
  `jobResourcecategoryId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `resourcecategoryid` int(11) DEFAULT NULL,
  PRIMARY KEY (`jobResourcecategoryId`,`REV`),
  KEY `FK17CFB61ADF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `jobresourcecategory_AUD` WRITE;
/*!40000 ALTER TABLE `jobresourcecategory_AUD` DISABLE KEYS */;

INSERT INTO `jobresourcecategory_AUD` (`jobResourcecategoryId`, `REV`, `REVTYPE`, `jobid`, `lastupdts`, `lastupduser`, `resourcecategoryid`)
VALUES
	(1,63,0,1,'2012-12-20 11:20:04',10,1),
	(2,78,0,2,'2012-12-20 11:27:07',10,1);

/*!40000 ALTER TABLE `jobresourcecategory_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobsample
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobsample`;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `jobsample` WRITE;
/*!40000 ALTER TABLE `jobsample` DISABLE KEYS */;

INSERT INTO `jobsample` (`jobsampleid`, `jobid`, `sampleid`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,21,'2012-12-20 11:20:04',10),
	(2,1,22,'2012-12-20 11:20:04',10),
	(3,1,23,'2012-12-20 11:20:04',10),
	(4,1,24,'2012-12-20 11:24:31',1),
	(5,2,25,'2012-12-20 11:27:07',10),
	(6,2,26,'2012-12-20 11:27:07',10);

/*!40000 ALTER TABLE `jobsample` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobsample_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobsample_AUD`;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `jobsample_AUD` WRITE;
/*!40000 ALTER TABLE `jobsample_AUD` DISABLE KEYS */;

INSERT INTO `jobsample_AUD` (`jobSampleId`, `REV`, `REVTYPE`, `jobid`, `lastupdts`, `lastupduser`, `sampleid`)
VALUES
	(1,63,0,1,'2012-12-20 11:20:04',10,21),
	(2,63,0,1,'2012-12-20 11:20:04',10,22),
	(3,63,0,1,'2012-12-20 11:20:04',10,23),
	(4,66,0,1,'2012-12-20 11:24:31',1,24),
	(5,78,0,2,'2012-12-20 11:27:07',10,25),
	(6,78,0,2,'2012-12-20 11:27:07',10,26);

/*!40000 ALTER TABLE `jobsample_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobsamplemeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobsamplemeta`;

CREATE TABLE `jobsamplemeta` (
  `jobsamplemetaid` int(10) NOT NULL AUTO_INCREMENT,
  `jobsampleid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` text,
  `position` int(10) DEFAULT '0',
  `rolevisibility` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`jobsamplemetaid`),
  UNIQUE KEY `u_jobsamplemeta_k_jsid` (`k`,`jobsampleid`),
  KEY `fk_jobsamplemeta_jsid` (`jobsampleid`),
  CONSTRAINT `jobsamplemeta_ibfk_1` FOREIGN KEY (`jobsampleid`) REFERENCES `jobsample` (`jobsampleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table jobsamplemeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobsamplemeta_AUD`;

CREATE TABLE `jobsamplemeta_AUD` (
  `jobSampleMetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `jobsampleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`jobSampleMetaId`,`REV`),
  KEY `FK4EB2B1FDDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table jobsoftware
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobsoftware`;

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

LOCK TABLES `jobsoftware` WRITE;
/*!40000 ALTER TABLE `jobsoftware` DISABLE KEYS */;

INSERT INTO `jobsoftware` (`jobsoftwareid`, `jobid`, `softwareid`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,1,'2012-12-20 11:20:04',10),
	(2,1,2,'2012-12-20 11:20:04',10),
	(3,2,1,'2012-12-20 11:27:07',10),
	(4,2,2,'2012-12-20 11:27:07',10);

/*!40000 ALTER TABLE `jobsoftware` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobsoftware_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobsoftware_AUD`;

CREATE TABLE `jobsoftware_AUD` (
  `jobSoftwareId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `softwareid` int(11) DEFAULT NULL,
  PRIMARY KEY (`jobSoftwareId`,`REV`),
  KEY `FK17954C35DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `jobsoftware_AUD` WRITE;
/*!40000 ALTER TABLE `jobsoftware_AUD` DISABLE KEYS */;

INSERT INTO `jobsoftware_AUD` (`jobSoftwareId`, `REV`, `REVTYPE`, `jobid`, `lastupdts`, `lastupduser`, `softwareid`)
VALUES
	(1,63,0,1,'2012-12-20 11:20:04',10,1),
	(2,63,0,1,'2012-12-20 11:20:04',10,2),
	(3,78,0,2,'2012-12-20 11:27:07',10,1),
	(4,78,0,2,'2012-12-20 11:27:07',10,2);

/*!40000 ALTER TABLE `jobsoftware_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobuser
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobuser`;

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

LOCK TABLES `jobuser` WRITE;
/*!40000 ALTER TABLE `jobuser` DISABLE KEYS */;

INSERT INTO `jobuser` (`jobuserid`, `jobid`, `userid`, `roleid`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,10,9,'2012-12-20 11:20:04',10),
	(2,1,5,10,'2012-12-20 11:20:04',10),
	(3,2,10,9,'2012-12-20 11:27:07',10),
	(4,2,5,10,'2012-12-20 11:27:07',10);

/*!40000 ALTER TABLE `jobuser` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobuser_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `jobuser_AUD`;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `jobuser_AUD` WRITE;
/*!40000 ALTER TABLE `jobuser_AUD` DISABLE KEYS */;

INSERT INTO `jobuser_AUD` (`jobUserId`, `REV`, `REVTYPE`, `userid`, `jobid`, `lastupdts`, `lastupduser`, `roleid`)
VALUES
	(1,63,0,10,1,'2012-12-20 11:20:04',10,9),
	(2,63,0,5,1,'2012-12-20 11:20:04',10,10),
	(3,78,0,10,2,'2012-12-20 11:27:07',10,9),
	(4,78,0,5,2,'2012-12-20 11:27:07',10,10);

/*!40000 ALTER TABLE `jobuser_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table lab
# ------------------------------------------------------------

DROP TABLE IF EXISTS `lab`;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `lab` WRITE;
/*!40000 ALTER TABLE `lab` DISABLE KEYS */;

INSERT INTO `lab` (`labid`, `departmentid`, `name`, `primaryuserid`, `isactive`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,'Default lab',1,1,'2012-06-14 14:08:21',1),
	(2,3,'Cancer Genetics',5,1,'2012-06-14 14:07:33',1),
	(3,3,'Godwin Lab',7,1,'2012-06-14 14:07:27',1),
	(4,3,'Williams Lab',8,1,'2012-06-14 14:06:49',1),
	(5,2,'Zebra Fish Lab',9,1,'2012-06-14 14:05:52',1);

/*!40000 ALTER TABLE `lab` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table lab_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `lab_AUD`;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table labmeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `labmeta`;

CREATE TABLE `labmeta` (
  `labmetaid` int(10) NOT NULL AUTO_INCREMENT,
  `labid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` text,
  `position` int(10) DEFAULT '0',
  `rolevisibility` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`labmetaid`),
  UNIQUE KEY `u_labmeta_k_lid` (`k`,`labid`),
  KEY `fk_labmeta_labid` (`labid`),
  CONSTRAINT `labmeta_ibfk_1` FOREIGN KEY (`labid`) REFERENCES `lab` (`labid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `labmeta` WRITE;
/*!40000 ALTER TABLE `labmeta` DISABLE KEYS */;

INSERT INTO `labmeta` (`labmetaid`, `labid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
VALUES
	(1,2,'lab.internal_external_lab','internal',0,NULL,'2012-05-30 20:22:24',NULL),
	(2,2,'lab.phone','718-123-4567',0,NULL,'2012-05-30 20:22:24',NULL),
	(3,2,'lab.building_room','Price 220',0,NULL,'2012-05-30 20:22:24',NULL),
	(4,2,'lab.billing_contact','John Greally',0,NULL,'2012-05-30 20:22:24',NULL),
	(5,2,'lab.billing_institution','Einstein',0,NULL,'2012-05-30 20:22:24',NULL),
	(6,2,'lab.billing_departmentId','3',0,NULL,'2012-05-30 20:22:24',NULL),
	(7,2,'lab.billing_building_room','Price 220',0,NULL,'2012-05-30 20:22:24',NULL),
	(8,2,'lab.billing_address','1301 Morris Park Ave.',0,NULL,'2012-05-30 20:22:24',NULL),
	(9,2,'lab.billing_city','Bronx',0,NULL,'2012-05-30 20:22:24',NULL),
	(10,2,'lab.billing_state','NY',0,NULL,'2012-05-30 20:22:24',NULL),
	(11,2,'lab.billing_country','US',0,NULL,'2012-05-30 20:22:24',NULL),
	(12,2,'lab.billing_zip','10461',0,NULL,'2012-05-30 20:22:24',NULL),
	(13,2,'lab.billing_phone','718-123-4567',0,NULL,'2012-05-30 20:22:24',NULL),
	(14,3,'lab.internal_external_lab','internal',0,NULL,'2012-05-30 22:03:56',NULL),
	(15,3,'lab.phone','718-678-1112',0,NULL,'2012-05-30 22:03:56',NULL),
	(16,3,'lab.building_room','Price 353',0,NULL,'2012-05-30 22:03:56',NULL),
	(17,3,'lab.billing_contact','Aaron Goldin',0,NULL,'2012-05-30 22:03:56',NULL),
	(18,3,'lab.billing_institution','Einstein',0,NULL,'2012-05-30 22:03:56',NULL),
	(19,3,'lab.billing_departmentId','3',0,NULL,'2012-05-30 22:03:56',NULL),
	(20,3,'lab.billing_building_room','Price 353',0,NULL,'2012-05-30 22:03:56',NULL),
	(21,3,'lab.billing_address','1301 Morris Park Ave.',0,NULL,'2012-05-30 22:03:56',NULL),
	(22,3,'lab.billing_city','Bronx',0,NULL,'2012-05-30 22:03:56',NULL),
	(23,3,'lab.billing_state','NY',0,NULL,'2012-05-30 22:03:56',NULL),
	(24,3,'lab.billing_country','US',0,NULL,'2012-05-30 22:03:56',NULL),
	(25,3,'lab.billing_zip','10461',0,NULL,'2012-05-30 22:03:56',NULL),
	(26,3,'lab.billing_phone','718-678-1112',0,NULL,'2012-05-30 22:03:56',NULL),
	(27,4,'lab.internal_external_lab','internal',0,NULL,'2012-05-31 13:59:23',NULL),
	(28,4,'lab.phone','718-678-1019',0,NULL,'2012-05-31 13:59:23',NULL),
	(29,4,'lab.building_room','Price 321',0,NULL,'2012-05-31 13:59:23',NULL),
	(30,4,'lab.billing_contact','Adam Auton',0,NULL,'2012-05-31 13:59:23',NULL),
	(31,4,'lab.billing_institution','Einstein',0,NULL,'2012-05-31 13:59:23',NULL),
	(32,4,'lab.billing_departmentId','3',0,NULL,'2012-05-31 13:59:23',NULL),
	(33,4,'lab.billing_building_room','Price 321',0,NULL,'2012-05-31 13:59:23',NULL),
	(34,4,'lab.billing_address','1301 Morris Park Ave.',0,NULL,'2012-05-31 13:59:23',NULL),
	(35,4,'lab.billing_city','Bronx',0,NULL,'2012-05-31 13:59:23',NULL),
	(36,4,'lab.billing_state','NY',0,NULL,'2012-05-31 13:59:23',NULL),
	(37,4,'lab.billing_country','US',0,NULL,'2012-05-31 13:59:23',NULL),
	(38,4,'lab.billing_zip','10461',0,NULL,'2012-05-31 13:59:23',NULL),
	(39,4,'lab.billing_phone','718-678-1019',0,NULL,'2012-05-31 13:59:23',NULL),
	(40,5,'lab.internal_external_lab','external',0,NULL,'2012-05-31 14:00:01',NULL),
	(41,5,'lab.phone','212-321-1091',0,NULL,'2012-05-31 14:00:01',NULL),
	(42,5,'lab.building_room','Hammer 1101',0,NULL,'2012-05-31 14:00:01',NULL),
	(43,5,'lab.billing_contact','Leslie Trokie',0,NULL,'2012-05-31 14:00:01',NULL),
	(44,5,'lab.billing_institution','NYU Medical',0,NULL,'2012-05-31 14:00:02',NULL),
	(45,5,'lab.billing_departmentId','3',0,NULL,'2012-05-31 14:00:02',NULL),
	(46,5,'lab.billing_building_room','Hammer 1101',0,NULL,'2012-05-31 14:00:02',NULL),
	(47,5,'lab.billing_address','16-50 32nd Street',0,NULL,'2012-05-31 14:00:02',NULL),
	(48,5,'lab.billing_city','New York',0,NULL,'2012-05-31 14:00:02',NULL),
	(49,5,'lab.billing_state','NY',0,NULL,'2012-05-31 14:00:02',NULL),
	(50,5,'lab.billing_country','US',0,NULL,'2012-05-31 14:00:02',NULL),
	(51,5,'lab.billing_zip','10002',0,NULL,'2012-05-31 14:00:02',NULL),
	(52,5,'lab.billing_phone','212-321-1091',0,NULL,'2012-05-31 14:00:02',NULL),
	(53,1,'lab.internal_external_lab','internal',0,NULL,'2012-06-14 14:08:21',NULL),
	(54,1,'lab.phone','N/A',0,NULL,'2012-06-14 14:08:21',NULL),
	(55,1,'lab.building_room','N/A',0,NULL,'2012-06-14 14:08:21',NULL),
	(56,1,'lab.billing_contact','N/A',0,NULL,'2012-06-14 14:08:21',NULL),
	(57,1,'lab.billing_institution','N/A',0,NULL,'2012-06-14 14:08:21',NULL),
	(58,1,'lab.billing_departmentId','1',0,NULL,'2012-06-14 14:08:21',NULL),
	(59,1,'lab.billing_building_room','N/A',0,NULL,'2012-06-14 14:08:21',NULL),
	(60,1,'lab.billing_address','N/A',0,NULL,'2012-06-14 14:08:21',NULL),
	(61,1,'lab.billing_city','N/A',0,NULL,'2012-06-14 14:08:21',NULL),
	(62,1,'lab.billing_state','NY',0,NULL,'2012-06-14 14:08:21',NULL),
	(63,1,'lab.billing_country','US',0,NULL,'2012-06-14 14:08:21',NULL),
	(64,1,'lab.billing_zip','N/A',0,NULL,'2012-06-14 14:08:21',NULL),
	(65,1,'lab.billing_phone','N/A',0,NULL,'2012-06-14 14:08:21',NULL);

/*!40000 ALTER TABLE `labmeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table labmeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `labmeta_AUD`;

CREATE TABLE `labmeta_AUD` (
  `labMetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  PRIMARY KEY (`labMetaId`,`REV`),
  KEY `FK7AC7DE3DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table labpending
# ------------------------------------------------------------

DROP TABLE IF EXISTS `labpending`;

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



# Dump of table labpending_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `labpending_AUD`;

CREATE TABLE `labpending_AUD` (
  `labPendingId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `departmentid` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `primaryuserid` int(11) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `userpendingid` int(11) DEFAULT NULL,
  PRIMARY KEY (`labPendingId`,`REV`),
  KEY `FKDD97E83BDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table labpendingmeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `labpendingmeta`;

CREATE TABLE `labpendingmeta` (
  `labpendingmetaid` int(10) NOT NULL AUTO_INCREMENT,
  `labpendingid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` text,
  `position` int(10) DEFAULT '0',
  `rolevisibility` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`labpendingmetaid`),
  UNIQUE KEY `u_labpendingmeta_k_lid` (`k`,`labpendingid`),
  KEY `fk_labpendingmeta_labpendingid` (`labpendingid`),
  CONSTRAINT `labpendingmeta_ibfk_1` FOREIGN KEY (`labpendingid`) REFERENCES `labpending` (`labpendingid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table labpendingmeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `labpendingmeta_AUD`;

CREATE TABLE `labpendingmeta_AUD` (
  `labPendingMetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `labpendingid` int(11) DEFAULT NULL,
  PRIMARY KEY (`labPendingMetaId`,`REV`),
  KEY `FK57FF2EC0DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table labuser
# ------------------------------------------------------------

DROP TABLE IF EXISTS `labuser`;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `labuser` WRITE;
/*!40000 ALTER TABLE `labuser` DISABLE KEYS */;

INSERT INTO `labuser` (`labuserid`, `labid`, `userid`, `roleid`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,1,6,'2012-05-23 15:55:46',1),
	(2,2,5,6,'2012-05-30 20:22:24',3),
	(3,2,6,8,'2012-05-30 21:13:54',5),
	(4,3,7,6,'2012-05-30 22:03:56',1),
	(5,4,8,6,'2012-05-31 13:59:23',3),
	(6,5,9,6,'2012-05-31 14:00:02',1),
	(7,2,10,7,'2012-05-31 14:02:38',5),
	(8,3,12,8,'2012-05-31 14:15:29',7);

/*!40000 ALTER TABLE `labuser` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table labuser_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `labuser_AUD`;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table meta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `meta`;

CREATE TABLE `meta` (
  `metaid` int(10) NOT NULL AUTO_INCREMENT,
  `property` varchar(250) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` text,
  `position` int(10) DEFAULT '0',
  `rolevisibility` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`metaid`),
  UNIQUE KEY `u_meta_p_k` (`property`,`k`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table meta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `meta_AUD`;

CREATE TABLE `meta_AUD` (
  `metaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `k` varchar(255) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `property` varchar(255) DEFAULT NULL,
  `rolevisibility` varchar(255) DEFAULT NULL,
  `v` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`metaId`,`REV`),
  KEY `FKE52AB956DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table project
# ------------------------------------------------------------

DROP TABLE IF EXISTS `project`;

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



# Dump of table project_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `project_AUD`;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table resource
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resource`;

CREATE TABLE `resource` (
  `resourceid` int(10) NOT NULL AUTO_INCREMENT,
  `resourcecategoryid` int(10) DEFAULT NULL,
  `resourcetypeid` int(10) DEFAULT NULL,
  `iname` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`resourceid`),
  UNIQUE KEY `u_resource_i` (`iname`),
  UNIQUE KEY `u_resource_n` (`name`),
  KEY `fk_resource_trid` (`resourcetypeid`),
  KEY `fk_resource_rid` (`resourcecategoryid`),
  CONSTRAINT `resource_ibfk_1` FOREIGN KEY (`resourcetypeid`) REFERENCES `resourcetype` (`resourcetypeid`),
  CONSTRAINT `resource_ibfk_2` FOREIGN KEY (`resourcecategoryid`) REFERENCES `resourcecategory` (`resourcecategoryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `resource` WRITE;
/*!40000 ALTER TABLE `resource` DISABLE KEYS */;

INSERT INTO `resource` (`resourceid`, `resourcecategoryid`, `resourcetypeid`, `iname`, `name`, `isactive`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,2,'Basil','Basil',1,'2012-12-20 11:07:21',1),
	(2,1,2,'Sybil','Sybil',1,'2012-12-20 11:07:41',1),
	(3,2,2,'Manuel','Manuel',1,'2012-12-20 11:08:04',1);

/*!40000 ALTER TABLE `resource` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resource_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resource_AUD`;

CREATE TABLE `resource_AUD` (
  `resourceId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `resourcetypeid` int(11) DEFAULT NULL,
  `resourcecategoryid` int(11) DEFAULT NULL,
  PRIMARY KEY (`resourceId`,`REV`),
  KEY `FKE91B3ADFDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `resource_AUD` WRITE;
/*!40000 ALTER TABLE `resource_AUD` DISABLE KEYS */;

INSERT INTO `resource_AUD` (`resourceId`, `REV`, `REVTYPE`, `iname`, `isactive`, `lastupdts`, `lastupduser`, `name`, `resourcetypeid`, `resourcecategoryid`)
VALUES
	(1,40,0,'Basil',1,'2012-12-20 11:07:21',1,'Basil',2,1),
	(2,41,0,'Sybil',1,'2012-12-20 11:07:41',1,'Sybil',2,1),
	(3,42,0,'Manuel',1,'2012-12-20 11:08:04',1,'Manuel',2,2);

/*!40000 ALTER TABLE `resource_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcebarcode
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resourcebarcode`;

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

LOCK TABLES `resourcebarcode` WRITE;
/*!40000 ALTER TABLE `resourcebarcode` DISABLE KEYS */;

INSERT INTO `resourcebarcode` (`resourcebarcodeid`, `resourceid`, `barcodeid`)
VALUES
	(1,1,1),
	(2,2,2),
	(3,3,3);

/*!40000 ALTER TABLE `resourcebarcode` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcebarcode_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resourcebarcode_AUD`;

CREATE TABLE `resourcebarcode_AUD` (
  `resourceBarcodeId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `barcodeid` int(11) DEFAULT NULL,
  `resourceid` int(11) DEFAULT NULL,
  PRIMARY KEY (`resourceBarcodeId`,`REV`),
  KEY `FK7BEA9D83DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `resourcebarcode_AUD` WRITE;
/*!40000 ALTER TABLE `resourcebarcode_AUD` DISABLE KEYS */;

INSERT INTO `resourcebarcode_AUD` (`resourceBarcodeId`, `REV`, `REVTYPE`, `barcodeid`, `resourceid`)
VALUES
	(1,40,0,1,1),
	(2,41,0,2,2),
	(3,42,0,3,3);

/*!40000 ALTER TABLE `resourcebarcode_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcecategory
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resourcecategory`;

CREATE TABLE `resourcecategory` (
  `resourcecategoryid` int(10) NOT NULL AUTO_INCREMENT,
  `resourcetypeid` int(10) NOT NULL,
  `iname` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`resourcecategoryid`),
  UNIQUE KEY `u_resourcecategory_i` (`iname`),
  UNIQUE KEY `u_resourcecategory_n` (`name`),
  KEY `fk_resourccategory_resourcetypeid` (`resourcetypeid`),
  CONSTRAINT `resourcecategory_ibfk_1` FOREIGN KEY (`resourcetypeid`) REFERENCES `resourcetype` (`resourcetypeid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `resourcecategory` WRITE;
/*!40000 ALTER TABLE `resourcecategory` DISABLE KEYS */;

INSERT INTO `resourcecategory` (`resourcecategoryid`, `resourcetypeid`, `iname`, `name`, `isactive`, `lastupdts`, `lastupduser`)
VALUES
	(1,2,'illuminaHiSeq2000','Illumina HiSeq 2000',1,'2012-12-20 11:03:31',0),
	(2,2,'illuminaMiSeqPersonalSequencer','Illumina MiSeq Personal Sequencer',1,'2012-12-20 11:03:32',0);

/*!40000 ALTER TABLE `resourcecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcecategory_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resourcecategory_AUD`;

CREATE TABLE `resourcecategory_AUD` (
  `resourceCategoryId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `resourcetypeid` int(11) DEFAULT NULL,
  PRIMARY KEY (`resourceCategoryId`,`REV`),
  KEY `FK38BC5ADDDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `resourcecategory_AUD` WRITE;
/*!40000 ALTER TABLE `resourcecategory_AUD` DISABLE KEYS */;

INSERT INTO `resourcecategory_AUD` (`resourceCategoryId`, `REV`, `REVTYPE`, `iname`, `isactive`, `lastupdts`, `lastupduser`, `name`, `resourcetypeid`)
VALUES
	(1,26,0,'illuminaHiSeq2000',1,'2012-12-20 11:03:31',0,'Illumina HiSeq 2000',2),
	(2,33,0,'illuminaMiSeqPersonalSequencer',1,'2012-12-20 11:03:32',0,'Illumina MiSeq Personal Sequencer',2);

/*!40000 ALTER TABLE `resourcecategory_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcecategorymeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resourcecategorymeta`;

CREATE TABLE `resourcecategorymeta` (
  `resourcecategorymetaid` int(10) NOT NULL AUTO_INCREMENT,
  `resourcecategoryid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` text,
  `position` int(10) DEFAULT '0',
  `rolevisibility` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`resourcecategorymetaid`),
  UNIQUE KEY `u_resourcecategorymeta_k_rid` (`k`,`resourcecategoryid`),
  KEY `fk_resourccategoryemeta_resourcecategoryid` (`resourcecategoryid`),
  CONSTRAINT `resourcecategorymeta_ibfk_1` FOREIGN KEY (`resourcecategoryid`) REFERENCES `resourcecategory` (`resourcecategoryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `resourcecategorymeta` WRITE;
/*!40000 ALTER TABLE `resourcecategorymeta` DISABLE KEYS */;

INSERT INTO `resourcecategorymeta` (`resourcecategorymetaid`, `resourcecategoryid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,'illuminaHiSeq2000.allowableUiField.readType','single:single;paired:paired',1,NULL,'2012-12-20 11:03:31',0),
	(2,1,'illuminaHiSeq2000.allowableUiField.readlength','50:50;75:75;100:100;150:150',2,NULL,'2012-12-20 11:03:31',0),
	(3,1,'illuminaHiSeq2000.platformUnitSelector','A:A;B:B',3,NULL,'2012-12-20 11:03:31',0),
	(4,2,'illuminaMiSeqPersonalSequencer.allowableUiField.readType','single:single;paired:paired',1,NULL,'2012-12-20 11:03:32',0),
	(5,2,'illuminaMiSeqPersonalSequencer.allowableUiField.readlength','25:25;36:36;50:50;100:100;150:150',2,NULL,'2012-12-20 11:03:32',0);

/*!40000 ALTER TABLE `resourcecategorymeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcecategorymeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resourcecategorymeta_AUD`;

CREATE TABLE `resourcecategorymeta_AUD` (
  `resourceCategoryMetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `resourcecategoryid` int(11) DEFAULT NULL,
  PRIMARY KEY (`resourceCategoryMetaId`,`REV`),
  KEY `FKD0858062DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `resourcecategorymeta_AUD` WRITE;
/*!40000 ALTER TABLE `resourcecategorymeta_AUD` DISABLE KEYS */;

INSERT INTO `resourcecategorymeta_AUD` (`resourceCategoryMetaId`, `REV`, `REVTYPE`, `resourcecategoryid`)
VALUES
	(1,26,0,1),
	(2,26,0,1),
	(3,26,0,1),
	(4,33,0,2),
	(5,33,0,2);

/*!40000 ALTER TABLE `resourcecategorymeta_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcecell
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resourcecell`;

CREATE TABLE `resourcecell` (
  `resourcecellid` int(10) NOT NULL AUTO_INCREMENT,
  `resourceid` int(10) DEFAULT NULL,
  `iname` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  PRIMARY KEY (`resourcecellid`),
  UNIQUE KEY `u_resourcecell_iname_rid` (`iname`,`resourceid`),
  UNIQUE KEY `u_resourcecell_name_rid` (`name`,`resourceid`),
  KEY `fk_resourcecell_rid` (`resourceid`),
  CONSTRAINT `resourcecell_ibfk_1` FOREIGN KEY (`resourceid`) REFERENCES `resource` (`resourceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table resourcecell_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resourcecell_AUD`;

CREATE TABLE `resourcecell_AUD` (
  `resourceCellId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `resourceid` int(11) DEFAULT NULL,
  PRIMARY KEY (`resourceCellId`,`REV`),
  KEY `FK5AB90C41DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table resourcemeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resourcemeta`;

CREATE TABLE `resourcemeta` (
  `resourcemetaid` int(10) NOT NULL AUTO_INCREMENT,
  `resourceid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` text,
  `position` int(10) DEFAULT '0',
  `rolevisibility` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`resourcemetaid`),
  UNIQUE KEY `u_resourcemeta_k_rid` (`k`,`resourceid`),
  KEY `fk_resourcemeta_resourceid` (`resourceid`),
  CONSTRAINT `resourcemeta_ibfk_1` FOREIGN KEY (`resourceid`) REFERENCES `resource` (`resourceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `resourcemeta` WRITE;
/*!40000 ALTER TABLE `resourcemeta` DISABLE KEYS */;

INSERT INTO `resourcemeta` (`resourcemetaid`, `resourceid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,'resource.commission_date','2012-10-03',0,NULL,'2012-12-20 11:07:21',NULL),
	(2,1,'resource.decommission_date','',0,NULL,'2012-12-20 11:07:21',NULL),
	(3,2,'resource.commission_date','2012-08-02',0,NULL,'2012-12-20 11:07:41',NULL),
	(4,2,'resource.decommission_date','',0,NULL,'2012-12-20 11:07:41',NULL),
	(5,3,'resource.commission_date','2012-05-24',0,NULL,'2012-12-20 11:08:04',NULL),
	(6,3,'resource.decommission_date','',0,NULL,'2012-12-20 11:08:04',NULL);

/*!40000 ALTER TABLE `resourcemeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcemeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resourcemeta_AUD`;

CREATE TABLE `resourcemeta_AUD` (
  `resourceMetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `resourceid` int(11) DEFAULT NULL,
  PRIMARY KEY (`resourceMetaId`,`REV`),
  KEY `FK76908F64DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `resourcemeta_AUD` WRITE;
/*!40000 ALTER TABLE `resourcemeta_AUD` DISABLE KEYS */;

INSERT INTO `resourcemeta_AUD` (`resourceMetaId`, `REV`, `REVTYPE`, `resourceid`)
VALUES
	(1,40,0,1),
	(2,40,0,1),
	(3,41,0,2),
	(4,41,0,2),
	(5,42,0,3),
	(6,42,0,3);

/*!40000 ALTER TABLE `resourcemeta_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcetype
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resourcetype`;

CREATE TABLE `resourcetype` (
  `resourcetypeid` int(10) NOT NULL AUTO_INCREMENT,
  `iname` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  PRIMARY KEY (`resourcetypeid`),
  UNIQUE KEY `u_resourcetype_iname` (`iname`),
  UNIQUE KEY `u_resourcetype_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `resourcetype` WRITE;
/*!40000 ALTER TABLE `resourcetype` DISABLE KEYS */;

INSERT INTO `resourcetype` (`resourcetypeid`, `iname`, `name`, `isactive`)
VALUES
	(1,'aligner','Aligner',1),
	(2,'mps','Massively Parallel DNA Sequencer',1),
	(3,'amplicon','DNA Amplicon',1),
	(4,'peakcaller','Peak Caller',1),
	(5,'sequenceRunProcessor','Sequence Run Processor',1),
	(6,'bisulseqPipeline','Bi-sulphite-seq Pipeline',1),
	(7,'helptagPipeline','HELP-tag Pipeline',1);

/*!40000 ALTER TABLE `resourcetype` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcetype_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `resourcetype_AUD`;

CREATE TABLE `resourcetype_AUD` (
  `resourceTypeId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`resourceTypeId`,`REV`),
  KEY `FK691A6619DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `resourcetype_AUD` WRITE;
/*!40000 ALTER TABLE `resourcetype_AUD` DISABLE KEYS */;

INSERT INTO `resourcetype_AUD` (`resourceTypeId`, `REV`, `REVTYPE`, `iname`, `isactive`, `name`)
VALUES
	(1,1,0,'aligner',1,'Aligner'),
	(2,5,0,'mps',1,'Massively Parallel DNA Sequencer'),
	(3,6,0,'amplicon',1,'DNA Amplicon'),
	(4,7,0,'peakcaller',1,'Peak Caller'),
	(5,8,0,'sequenceRunProcessor',1,'Sequence Run Processor'),
	(6,16,0,'bisulseqPipeline',1,'Bi-sulphite-seq Pipeline'),
	(7,28,0,'helptagPipeline',1,'HELP-tag Pipeline');

/*!40000 ALTER TABLE `resourcetype_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table REVINFO
# ------------------------------------------------------------

DROP TABLE IF EXISTS `REVINFO`;

CREATE TABLE `REVINFO` (
  `REV` int(11) NOT NULL AUTO_INCREMENT,
  `REVTSTMP` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `REVINFO` WRITE;
/*!40000 ALTER TABLE `REVINFO` DISABLE KEYS */;

INSERT INTO `REVINFO` (`REV`, `REVTSTMP`)
VALUES
	(1,1356019409770),
	(2,1356019409885),
	(3,1356019410082),
	(4,1356019410165),
	(5,1356019410317),
	(6,1356019410324),
	(7,1356019410331),
	(8,1356019410337),
	(9,1356019410345),
	(10,1356019410352),
	(11,1356019410359),
	(12,1356019410366),
	(13,1356019410374),
	(14,1356019410382),
	(15,1356019410411),
	(16,1356019410497),
	(17,1356019410586),
	(18,1356019410730),
	(19,1356019410788),
	(20,1356019410912),
	(21,1356019410966),
	(22,1356019411040),
	(23,1356019411088),
	(24,1356019411159),
	(25,1356019411254),
	(26,1356019411342),
	(27,1356019411443),
	(28,1356019411467),
	(29,1356019411535),
	(30,1356019411633),
	(31,1356019411801),
	(32,1356019412163),
	(33,1356019412205),
	(34,1356019412278),
	(35,1356019412318),
	(36,1356019542041),
	(37,1356019574684),
	(38,1356019590193),
	(39,1356019603870),
	(40,1356019641948),
	(41,1356019661598),
	(42,1356019684925),
	(43,1356019760584),
	(44,1356019788853),
	(45,1356019828001),
	(46,1356020126420),
	(47,1356020130442),
	(48,1356020135719),
	(49,1356020176390),
	(50,1356020201856),
	(51,1356020227714),
	(52,1356020237153),
	(53,1356020247884),
	(54,1356020254580),
	(55,1356020261077),
	(56,1356020283645),
	(57,1356020317201),
	(58,1356020319738),
	(59,1356020321490),
	(60,1356020323735),
	(61,1356020379874),
	(62,1356020403213),
	(63,1356020405122),
	(64,1356020538625),
	(65,1356020615257),
	(66,1356020671711),
	(67,1356020719493),
	(68,1356020722606),
	(69,1356020727719),
	(70,1356020782223),
	(71,1356020797891),
	(72,1356020810136),
	(73,1356020816042),
	(74,1356020818187),
	(75,1356020819464),
	(76,1356020821202),
	(77,1356020822563),
	(78,1356020827439);

/*!40000 ALTER TABLE `REVINFO` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table role
# ------------------------------------------------------------

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `roleid` int(10) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `domain` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`roleid`),
  UNIQUE KEY `u_role_rname` (`rolename`),
  UNIQUE KEY `u_role_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;

INSERT INTO `role` (`roleid`, `rolename`, `name`, `domain`)
VALUES
	(1,'fm','Facilities Manager','system'),
	(2,'sa','System Administrator','system'),
	(3,'ga','General Administrator','system'),
	(4,'da','Department Administrator','department'),
	(5,'ft','Facilities Tech','system'),
	(6,'pi','Primary Investigator','lab'),
	(7,'lm','Lab Manager','lab'),
	(8,'lu','Lab Member','lab'),
	(9,'js','Job Submitter','job'),
	(10,'jv','Job Viewer','job'),
	(11,'su','Super User','system'),
	(12,'lx','Lab Member Inactive','lab'),
	(13,'lp','Lab Member Pending','lab'),
	(14,'jd','Job Drafter','jobdraft'),
	(15,'u','User','user');

/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table role_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `role_AUD`;

CREATE TABLE `role_AUD` (
  `roleId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `domain` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `rolename` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`roleId`,`REV`),
  KEY `FKF0208347DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table roleset
# ------------------------------------------------------------

DROP TABLE IF EXISTS `roleset`;

CREATE TABLE `roleset` (
  `rolesetid` int(10) NOT NULL AUTO_INCREMENT,
  `parentroleid` int(10) DEFAULT NULL,
  `childroleid` int(10) DEFAULT NULL,
  PRIMARY KEY (`rolesetid`),
  UNIQUE KEY `u_role_rname` (`parentroleid`,`childroleid`),
  KEY `fk_roleset_crid` (`childroleid`),
  CONSTRAINT `roleset_ibfk_1` FOREIGN KEY (`parentroleid`) REFERENCES `role` (`roleid`),
  CONSTRAINT `roleset_ibfk_2` FOREIGN KEY (`childroleid`) REFERENCES `role` (`roleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `roleset` WRITE;
/*!40000 ALTER TABLE `roleset` DISABLE KEYS */;

INSERT INTO `roleset` (`rolesetid`, `parentroleid`, `childroleid`)
VALUES
	(2,1,1),
	(16,1,5),
	(13,2,2),
	(4,3,3),
	(1,4,4),
	(3,5,5),
	(12,6,6),
	(17,6,7),
	(18,6,8),
	(8,7,7),
	(19,7,8),
	(10,8,8),
	(6,9,9),
	(20,9,10),
	(7,10,10),
	(21,11,1),
	(22,11,2),
	(23,11,3),
	(24,11,5),
	(14,11,11),
	(11,12,12),
	(9,13,13),
	(5,14,14),
	(15,15,15);

/*!40000 ALTER TABLE `roleset` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table roleset_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `roleset_AUD`;

CREATE TABLE `roleset_AUD` (
  `rolesetId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `childroleid` int(11) DEFAULT NULL,
  `parentroleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`rolesetId`,`REV`),
  KEY `FK38BA17FDDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table run
# ------------------------------------------------------------

DROP TABLE IF EXISTS `run`;

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



# Dump of table run_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `run_AUD`;

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
  `resourceCategoryid` int(11) DEFAULT NULL,
  `resourceid` int(11) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  `softwareid` int(11) DEFAULT NULL,
  `startts` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`runId`,`REV`),
  KEY `FK5C67AADCDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table runcell
# ------------------------------------------------------------

DROP TABLE IF EXISTS `runcell`;

CREATE TABLE `runcell` (
  `runcellid` int(10) NOT NULL AUTO_INCREMENT,
  `runid` int(10) DEFAULT NULL,
  `resourcecellid` int(10) DEFAULT NULL,
  `sampleid` int(10) DEFAULT NULL,
  PRIMARY KEY (`runcellid`),
  UNIQUE KEY `u_runcell_rid_lid` (`runid`,`resourcecellid`),
  UNIQUE KEY `u_runcell_sid_rid` (`sampleid`,`runid`),
  KEY `fk_runcell_lid` (`resourcecellid`),
  CONSTRAINT `runcell_ibfk_1` FOREIGN KEY (`runid`) REFERENCES `run` (`runid`),
  CONSTRAINT `runcell_ibfk_2` FOREIGN KEY (`resourcecellid`) REFERENCES `resourcecell` (`resourcecellid`),
  CONSTRAINT `runcell_ibfk_3` FOREIGN KEY (`sampleid`) REFERENCES `sample` (`sampleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table runcell_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `runcell_AUD`;

CREATE TABLE `runcell_AUD` (
  `runCellId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `resourcecellid` int(11) DEFAULT NULL,
  `runid` int(11) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`runCellId`,`REV`),
  KEY `FK722335BEDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table runcellfile
# ------------------------------------------------------------

DROP TABLE IF EXISTS `runcellfile`;

CREATE TABLE `runcellfile` (
  `runcellfileid` int(10) NOT NULL AUTO_INCREMENT,
  `runcellid` int(10) DEFAULT NULL,
  `fileid` int(10) DEFAULT NULL,
  `iname` varchar(2048) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`runcellfileid`),
  UNIQUE KEY `u_rlfile_fileid` (`fileid`),
  KEY `fk_rlfile_rlid` (`runcellid`),
  CONSTRAINT `runcellfile_ibfk_1` FOREIGN KEY (`runcellid`) REFERENCES `runcell` (`runcellid`),
  CONSTRAINT `runcellfile_ibfk_2` FOREIGN KEY (`fileid`) REFERENCES `file` (`fileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table runcellfile_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `runcellfile_AUD`;

CREATE TABLE `runcellfile_AUD` (
  `runCellfileId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `fileid` int(11) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `runcellid` int(11) DEFAULT NULL,
  PRIMARY KEY (`runCellfileId`,`REV`),
  KEY `FKE2544EBADF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table runfile
# ------------------------------------------------------------

DROP TABLE IF EXISTS `runfile`;

CREATE TABLE `runfile` (
  `runcellfileid` int(10) NOT NULL AUTO_INCREMENT,
  `runid` int(10) DEFAULT NULL,
  `fileid` int(10) DEFAULT NULL,
  `iname` varchar(2048) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`runcellfileid`),
  UNIQUE KEY `u_rlfile_fileid` (`fileid`),
  KEY `fk_rfile_rid` (`runid`),
  CONSTRAINT `runfile_ibfk_1` FOREIGN KEY (`runid`) REFERENCES `run` (`runid`),
  CONSTRAINT `runfile_ibfk_2` FOREIGN KEY (`fileid`) REFERENCES `file` (`fileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table runfile_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `runfile_AUD`;

CREATE TABLE `runfile_AUD` (
  `runcellfileId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `fileid` int(11) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `runid` int(11) DEFAULT NULL,
  PRIMARY KEY (`runcellfileId`,`REV`),
  KEY `FK7CFD04D8DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table runmeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `runmeta`;

CREATE TABLE `runmeta` (
  `runmetaid` int(10) NOT NULL AUTO_INCREMENT,
  `runid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` text,
  `position` int(10) DEFAULT '0',
  `rolevisibility` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`runmetaid`),
  UNIQUE KEY `u_runmeta_k_rid` (`k`,`runid`),
  KEY `fk_runmeta_runid` (`runid`),
  CONSTRAINT `runmeta_ibfk_1` FOREIGN KEY (`runid`) REFERENCES `run` (`runid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table runmeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `runmeta_AUD`;

CREATE TABLE `runmeta_AUD` (
  `runMetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `runid` int(11) DEFAULT NULL,
  PRIMARY KEY (`runMetaId`,`REV`),
  KEY `FK8DFAB8E1DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table sample
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sample`;

CREATE TABLE `sample` (
  `sampleid` int(10) NOT NULL AUTO_INCREMENT,
  `parentid` int(10) DEFAULT NULL,
  `sampletypeid` int(10) DEFAULT NULL,
  `samplesubtypeid` int(10) DEFAULT NULL,
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
  KEY `fk_sample_sampleid` (`parentid`),
  KEY `fk_sample_tsid` (`sampletypeid`),
  KEY `fk_sample_stsid` (`samplesubtypeid`),
  KEY `fk_sample_sjid` (`submitter_jobid`),
  KEY `fk_sample_slid` (`submitter_labid`),
  KEY `fk_sample_suid` (`submitter_userid`),
  CONSTRAINT `sample_ibfk_1` FOREIGN KEY (`parentid`) REFERENCES `sample` (`sampleid`),
  CONSTRAINT `sample_ibfk_2` FOREIGN KEY (`sampletypeid`) REFERENCES `sampletype` (`sampletypeid`),
  CONSTRAINT `sample_ibfk_3` FOREIGN KEY (`samplesubtypeid`) REFERENCES `samplesubtype` (`samplesubtypeid`),
  CONSTRAINT `sample_ibfk_4` FOREIGN KEY (`submitter_jobid`) REFERENCES `job` (`jobid`),
  CONSTRAINT `sample_ibfk_5` FOREIGN KEY (`submitter_labid`) REFERENCES `lab` (`labid`),
  CONSTRAINT `sample_ibfk_6` FOREIGN KEY (`submitter_userid`) REFERENCES `user` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `sample` WRITE;
/*!40000 ALTER TABLE `sample` DISABLE KEYS */;

INSERT INTO `sample` (`sampleid`, `parentid`, `sampletypeid`, `samplesubtypeid`, `submitter_labid`, `submitter_userid`, `submitter_jobid`, `isreceived`, `receiver_userid`, `receivedts`, `name`, `isgood`, `isactive`, `lastupdts`, `lastupduser`)
VALUES
	(1,NULL,5,10,1,1,NULL,1,NULL,'2012-12-20 11:09:20','C126NACXX',1,1,'2012-12-20 11:09:20',1),
	(2,NULL,4,NULL,1,1,NULL,1,1,'2012-12-20 11:09:20','C126NACXX/1',1,1,'2012-12-20 11:09:20',1),
	(3,NULL,4,NULL,1,1,NULL,1,1,'2012-12-20 11:09:20','C126NACXX/2',1,1,'2012-12-20 11:09:20',1),
	(4,NULL,4,NULL,1,1,NULL,1,1,'2012-12-20 11:09:20','C126NACXX/3',1,1,'2012-12-20 11:09:20',1),
	(5,NULL,4,NULL,1,1,NULL,1,1,'2012-12-20 11:09:20','C126NACXX/4',1,1,'2012-12-20 11:09:20',1),
	(6,NULL,4,NULL,1,1,NULL,1,1,'2012-12-20 11:09:20','C126NACXX/5',1,1,'2012-12-20 11:09:20',1),
	(7,NULL,4,NULL,1,1,NULL,1,1,'2012-12-20 11:09:20','C126NACXX/6',1,1,'2012-12-20 11:09:20',1),
	(8,NULL,4,NULL,1,1,NULL,1,1,'2012-12-20 11:09:20','C126NACXX/7',1,1,'2012-12-20 11:09:20',1),
	(9,NULL,4,NULL,1,1,NULL,1,1,'2012-12-20 11:09:20','C126NACXX/8',1,1,'2012-12-20 11:09:20',1),
	(10,NULL,5,10,1,1,NULL,1,NULL,'2012-12-20 11:09:48','D1884ACXX',1,1,'2012-12-20 11:09:48',1),
	(11,NULL,4,NULL,1,1,NULL,1,1,'2012-12-20 11:09:48','D1884ACXX/1',1,1,'2012-12-20 11:09:48',1),
	(12,NULL,4,NULL,1,1,NULL,1,1,'2012-12-20 11:09:48','D1884ACXX/2',1,1,'2012-12-20 11:09:48',1),
	(13,NULL,4,NULL,1,1,NULL,1,1,'2012-12-20 11:09:48','D1884ACXX/3',1,1,'2012-12-20 11:09:48',1),
	(14,NULL,4,NULL,1,1,NULL,1,1,'2012-12-20 11:09:48','D1884ACXX/4',1,1,'2012-12-20 11:09:48',1),
	(15,NULL,4,NULL,1,1,NULL,1,1,'2012-12-20 11:09:48','D1884ACXX/5',1,1,'2012-12-20 11:09:48',1),
	(16,NULL,4,NULL,1,1,NULL,1,1,'2012-12-20 11:09:48','D1884ACXX/6',1,1,'2012-12-20 11:09:48',1),
	(17,NULL,4,NULL,1,1,NULL,1,1,'2012-12-20 11:09:48','D1884ACXX/7',1,1,'2012-12-20 11:09:48',1),
	(18,NULL,4,NULL,1,1,NULL,1,1,'2012-12-20 11:09:48','D1884ACXX/8',1,1,'2012-12-20 11:09:48',1),
	(19,NULL,5,9,1,1,NULL,1,NULL,'2012-12-20 11:10:27','634H7AAXX',1,1,'2012-12-20 11:10:27',1),
	(20,NULL,4,NULL,1,1,NULL,1,1,'2012-12-20 11:10:27','634H7AAXX/1',1,1,'2012-12-20 11:10:27',1),
	(21,NULL,2,5,2,10,1,0,NULL,NULL,'sIP1',NULL,1,'2012-12-20 11:20:04',10),
	(22,NULL,2,5,2,10,1,0,NULL,NULL,'sIP2',NULL,1,'2012-12-20 11:20:04',10),
	(23,NULL,2,5,2,10,1,0,NULL,NULL,'sINPUT',NULL,1,'2012-12-20 11:20:04',10),
	(24,21,7,7,2,10,1,NULL,NULL,NULL,'sIP1_lib',NULL,1,'2012-12-20 11:24:31',1),
	(25,NULL,1,6,2,10,2,0,NULL,NULL,'l1INPUT',NULL,1,'2012-12-20 11:27:07',10),
	(26,NULL,1,6,2,10,2,0,NULL,NULL,'lIP1',NULL,1,'2012-12-20 11:27:07',10);

/*!40000 ALTER TABLE `sample` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sample_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sample_AUD`;

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
  `parentid` int(11) DEFAULT NULL,
  `receivedts` datetime DEFAULT NULL,
  `receiver_userid` int(11) DEFAULT NULL,
  `samplesubtypeid` int(11) DEFAULT NULL,
  `sampletypeid` int(11) DEFAULT NULL,
  `submitter_jobid` int(11) DEFAULT NULL,
  `submitter_labid` int(11) DEFAULT NULL,
  `submitter_userid` int(11) DEFAULT NULL,
  PRIMARY KEY (`sampleId`,`REV`),
  KEY `FK88CBE7BDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `sample_AUD` WRITE;
/*!40000 ALTER TABLE `sample_AUD` DISABLE KEYS */;

INSERT INTO `sample_AUD` (`sampleId`, `REV`, `REVTYPE`, `isactive`, `isgood`, `isreceived`, `lastupdts`, `lastupduser`, `name`, `parentid`, `receivedts`, `receiver_userid`, `samplesubtypeid`, `sampletypeid`, `submitter_jobid`, `submitter_labid`, `submitter_userid`)
VALUES
	(1,43,0,1,1,1,'2012-12-20 11:09:20',1,'C126NACXX',NULL,'2012-12-20 11:09:20',NULL,10,5,NULL,1,1),
	(2,43,0,1,1,1,'2012-12-20 11:09:20',1,'C126NACXX/1',NULL,'2012-12-20 11:09:20',1,NULL,4,NULL,1,1),
	(3,43,0,1,1,1,'2012-12-20 11:09:20',1,'C126NACXX/2',NULL,'2012-12-20 11:09:20',1,NULL,4,NULL,1,1),
	(4,43,0,1,1,1,'2012-12-20 11:09:20',1,'C126NACXX/3',NULL,'2012-12-20 11:09:20',1,NULL,4,NULL,1,1),
	(5,43,0,1,1,1,'2012-12-20 11:09:20',1,'C126NACXX/4',NULL,'2012-12-20 11:09:20',1,NULL,4,NULL,1,1),
	(6,43,0,1,1,1,'2012-12-20 11:09:20',1,'C126NACXX/5',NULL,'2012-12-20 11:09:20',1,NULL,4,NULL,1,1),
	(7,43,0,1,1,1,'2012-12-20 11:09:20',1,'C126NACXX/6',NULL,'2012-12-20 11:09:20',1,NULL,4,NULL,1,1),
	(8,43,0,1,1,1,'2012-12-20 11:09:20',1,'C126NACXX/7',NULL,'2012-12-20 11:09:20',1,NULL,4,NULL,1,1),
	(9,43,0,1,1,1,'2012-12-20 11:09:20',1,'C126NACXX/8',NULL,'2012-12-20 11:09:20',1,NULL,4,NULL,1,1),
	(10,44,0,1,1,1,'2012-12-20 11:09:48',1,'D1884ACXX',NULL,'2012-12-20 11:09:48',NULL,10,5,NULL,1,1),
	(11,44,0,1,1,1,'2012-12-20 11:09:48',1,'D1884ACXX/1',NULL,'2012-12-20 11:09:48',1,NULL,4,NULL,1,1),
	(12,44,0,1,1,1,'2012-12-20 11:09:48',1,'D1884ACXX/2',NULL,'2012-12-20 11:09:48',1,NULL,4,NULL,1,1),
	(13,44,0,1,1,1,'2012-12-20 11:09:48',1,'D1884ACXX/3',NULL,'2012-12-20 11:09:48',1,NULL,4,NULL,1,1),
	(14,44,0,1,1,1,'2012-12-20 11:09:48',1,'D1884ACXX/4',NULL,'2012-12-20 11:09:48',1,NULL,4,NULL,1,1),
	(15,44,0,1,1,1,'2012-12-20 11:09:48',1,'D1884ACXX/5',NULL,'2012-12-20 11:09:48',1,NULL,4,NULL,1,1),
	(16,44,0,1,1,1,'2012-12-20 11:09:48',1,'D1884ACXX/6',NULL,'2012-12-20 11:09:48',1,NULL,4,NULL,1,1),
	(17,44,0,1,1,1,'2012-12-20 11:09:48',1,'D1884ACXX/7',NULL,'2012-12-20 11:09:48',1,NULL,4,NULL,1,1),
	(18,44,0,1,1,1,'2012-12-20 11:09:48',1,'D1884ACXX/8',NULL,'2012-12-20 11:09:48',1,NULL,4,NULL,1,1),
	(19,45,0,1,1,1,'2012-12-20 11:10:27',1,'634H7AAXX',NULL,'2012-12-20 11:10:27',NULL,9,5,NULL,1,1),
	(20,45,0,1,1,1,'2012-12-20 11:10:27',1,'634H7AAXX/1',NULL,'2012-12-20 11:10:27',1,NULL,4,NULL,1,1),
	(21,63,0,1,NULL,0,'2012-12-20 11:20:04',10,'sIP1',NULL,NULL,NULL,5,2,1,2,10),
	(22,63,0,1,NULL,0,'2012-12-20 11:20:04',10,'sIP2',NULL,NULL,NULL,5,2,1,2,10),
	(23,63,0,1,NULL,0,'2012-12-20 11:20:04',10,'sINPUT',NULL,NULL,NULL,5,2,1,2,10),
	(24,66,0,1,NULL,NULL,'2012-12-20 11:24:31',1,'sIP1_lib',21,NULL,NULL,7,7,1,2,10),
	(25,78,0,1,NULL,0,'2012-12-20 11:27:07',10,'l1INPUT',NULL,NULL,NULL,6,1,2,2,10),
	(26,78,0,1,NULL,0,'2012-12-20 11:27:07',10,'lIP1',NULL,NULL,NULL,6,1,2,2,10);

/*!40000 ALTER TABLE `sample_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplebarcode
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplebarcode`;

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

LOCK TABLES `samplebarcode` WRITE;
/*!40000 ALTER TABLE `samplebarcode` DISABLE KEYS */;

INSERT INTO `samplebarcode` (`samplebarcode`, `sampleid`, `barcodeid`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,4,'2012-12-20 11:09:20',1),
	(2,10,5,'2012-12-20 11:09:48',1),
	(3,19,6,'2012-12-20 11:10:27',1);

/*!40000 ALTER TABLE `samplebarcode` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplebarcode_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplebarcode_AUD`;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `samplebarcode_AUD` WRITE;
/*!40000 ALTER TABLE `samplebarcode_AUD` DISABLE KEYS */;

INSERT INTO `samplebarcode_AUD` (`sampleBarcode`, `REV`, `REVTYPE`, `barcodeid`, `lastupdts`, `lastupduser`, `sampleid`)
VALUES
	(1,43,0,4,'2012-12-20 11:09:20',1,1),
	(2,44,0,5,'2012-12-20 11:09:48',1,10),
	(3,45,0,6,'2012-12-20 11:10:27',1,19);

/*!40000 ALTER TABLE `samplebarcode_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sampledraft
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sampledraft`;

CREATE TABLE `sampledraft` (
  `sampledraftid` int(10) NOT NULL AUTO_INCREMENT,
  `sampletypeid` int(10) DEFAULT NULL,
  `samplesubtypeid` int(10) DEFAULT NULL,
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
  KEY `fk_sampledraft_tsid` (`sampletypeid`),
  KEY `fk_sampledraft_stsid` (`samplesubtypeid`),
  KEY `fk_sampledraft_sjid` (`jobdraftid`),
  KEY `fk_sampledraft_slid` (`labid`),
  KEY `fk_sampledraft_suid` (`userid`),
  KEY `fk_sampledraft_fid` (`fileid`),
  CONSTRAINT `sampledraft_ibfk_1` FOREIGN KEY (`sampletypeid`) REFERENCES `sampletype` (`sampletypeid`),
  CONSTRAINT `sampledraft_ibfk_2` FOREIGN KEY (`samplesubtypeid`) REFERENCES `samplesubtype` (`samplesubtypeid`),
  CONSTRAINT `sampledraft_ibfk_3` FOREIGN KEY (`jobdraftid`) REFERENCES `jobdraft` (`jobdraftid`),
  CONSTRAINT `sampledraft_ibfk_4` FOREIGN KEY (`labid`) REFERENCES `lab` (`labid`),
  CONSTRAINT `sampledraft_ibfk_5` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`),
  CONSTRAINT `sampledraft_ibfk_6` FOREIGN KEY (`fileid`) REFERENCES `file` (`fileid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `sampledraft` WRITE;
/*!40000 ALTER TABLE `sampledraft` DISABLE KEYS */;

INSERT INTO `sampledraft` (`sampledraftid`, `sampletypeid`, `samplesubtypeid`, `labid`, `userid`, `jobdraftid`, `sourcesampleid`, `fileid`, `name`, `status`, `lastupdts`, `lastupduser`)
VALUES
	(1,2,5,2,10,1,NULL,NULL,'sIP1',NULL,'2012-12-20 11:17:27',10),
	(2,2,5,2,10,1,NULL,NULL,'sIP2',NULL,'2012-12-20 11:17:34',10),
	(3,2,5,2,10,1,NULL,NULL,'sINPUT',NULL,'2012-12-20 11:17:41',10),
	(4,1,6,2,10,2,NULL,NULL,'l1INPUT',NULL,'2012-12-20 11:26:22',10),
	(5,1,6,2,10,2,NULL,NULL,'lIP1',NULL,'2012-12-20 11:26:37',10);

/*!40000 ALTER TABLE `sampledraft` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sampledraft_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sampledraft_AUD`;

CREATE TABLE `sampledraft_AUD` (
  `sampleDraftId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `fileid` int(11) DEFAULT NULL,
  `jobdraftid` int(11) DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `samplesubtypeid` int(11) DEFAULT NULL,
  `sampletypeid` int(11) DEFAULT NULL,
  `sourcesampleid` int(11) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`sampleDraftId`,`REV`),
  KEY `FKFC6C3888DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `sampledraft_AUD` WRITE;
/*!40000 ALTER TABLE `sampledraft_AUD` DISABLE KEYS */;

INSERT INTO `sampledraft_AUD` (`sampleDraftId`, `REV`, `REVTYPE`, `userid`, `fileid`, `jobdraftid`, `labid`, `lastupdts`, `lastupduser`, `name`, `samplesubtypeid`, `sampletypeid`, `sourcesampleid`, `status`)
VALUES
	(1,49,0,10,NULL,1,2,'2012-12-20 11:16:16',10,'s1',5,2,NULL,NULL),
	(2,50,0,10,NULL,1,2,'2012-12-20 11:16:41',10,'s2',5,2,NULL,NULL),
	(3,51,0,10,NULL,1,2,'2012-12-20 11:17:07',10,'s3',5,2,NULL,NULL),
	(3,52,1,10,NULL,1,2,'2012-12-20 11:17:17',10,'i1',5,2,NULL,NULL),
	(1,53,1,10,NULL,1,2,'2012-12-20 11:17:27',10,'sIP1',5,2,NULL,NULL),
	(2,54,1,10,NULL,1,2,'2012-12-20 11:17:34',10,'sIP2',5,2,NULL,NULL),
	(3,55,1,10,NULL,1,2,'2012-12-20 11:17:41',10,'sINPUT',5,2,NULL,NULL),
	(4,70,0,10,NULL,2,2,'2012-12-20 11:26:22',10,'l1INPUT',6,1,NULL,NULL),
	(5,71,0,10,NULL,2,2,'2012-12-20 11:26:37',10,'lIP1',6,1,NULL,NULL);

/*!40000 ALTER TABLE `sampledraft_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sampledraftjobdraftcellselection
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sampledraftjobdraftcellselection`;

CREATE TABLE `sampledraftjobdraftcellselection` (
  `sampledraftjobdraftcellselectionid` int(10) NOT NULL AUTO_INCREMENT,
  `sampledraftid` int(10) DEFAULT NULL,
  `jobdraftcellselectionid` int(10) DEFAULT NULL,
  `libraryindex` int(10) DEFAULT NULL,
  PRIMARY KEY (`sampledraftjobdraftcellselectionid`),
  UNIQUE KEY `u_sampledraftcell_jdcid_li` (`jobdraftcellselectionid`,`libraryindex`),
  KEY `FKB65C2B0F4E0649B2` (`sampledraftid`),
  KEY `FKB65C2B0FBB98C410` (`jobdraftcellselectionid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `sampledraftjobdraftcellselection` WRITE;
/*!40000 ALTER TABLE `sampledraftjobdraftcellselection` DISABLE KEYS */;

INSERT INTO `sampledraftjobdraftcellselection` (`sampledraftjobdraftcellselectionid`, `sampledraftid`, `jobdraftcellselectionid`, `libraryindex`)
VALUES
	(1,1,1,1),
	(2,2,1,2),
	(3,3,1,3),
	(4,4,2,1),
	(5,5,3,1);

/*!40000 ALTER TABLE `sampledraftjobdraftcellselection` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sampledraftjobdraftcellselection_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sampledraftjobdraftcellselection_AUD`;

CREATE TABLE `sampledraftjobdraftcellselection_AUD` (
  `sampleDraftJobDraftCellSelectionId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `jobdraftcellselectionid` int(11) DEFAULT NULL,
  `libraryindex` int(11) DEFAULT NULL,
  `sampledraftid` int(11) DEFAULT NULL,
  PRIMARY KEY (`sampleDraftJobDraftCellSelectionId`,`REV`),
  KEY `FK874E3960DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `sampledraftjobdraftcellselection_AUD` WRITE;
/*!40000 ALTER TABLE `sampledraftjobdraftcellselection_AUD` DISABLE KEYS */;

INSERT INTO `sampledraftjobdraftcellselection_AUD` (`sampleDraftJobDraftCellSelectionId`, `REV`, `REVTYPE`, `jobdraftcellselectionid`, `libraryindex`, `sampledraftid`)
VALUES
	(1,56,0,1,1,1),
	(2,56,0,1,2,2),
	(3,56,0,1,3,3),
	(4,72,0,2,1,4),
	(5,72,0,3,1,5);

/*!40000 ALTER TABLE `sampledraftjobdraftcellselection_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sampledraftmeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sampledraftmeta`;

CREATE TABLE `sampledraftmeta` (
  `sampledraftmetaid` int(10) NOT NULL AUTO_INCREMENT,
  `sampledraftid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` text,
  `position` int(10) DEFAULT '0',
  `rolevisibility` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`sampledraftmetaid`),
  UNIQUE KEY `u_sampledraftmeta_k_sid` (`k`,`sampledraftid`),
  KEY `fk_sampledraftmeta_sdid` (`sampledraftid`),
  CONSTRAINT `sampledraftmeta_ibfk_1` FOREIGN KEY (`sampledraftid`) REFERENCES `sampledraft` (`sampledraftid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `sampledraftmeta` WRITE;
/*!40000 ALTER TABLE `sampledraftmeta` DISABLE KEYS */;

INSERT INTO `sampledraftmeta` (`sampledraftmetaid`, `sampledraftid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,'genericBiomolecule.species','Human',0,NULL,'2012-12-20 11:16:16',NULL),
	(2,1,'genericDna.concentration','26',0,NULL,'2012-12-20 11:16:16',NULL),
	(3,1,'genericDna.volume','10',0,NULL,'2012-12-20 11:16:16',NULL),
	(4,1,'genericDna.buffer','TE',0,NULL,'2012-12-20 11:16:16',NULL),
	(5,1,'genericDna.A260_280','1.76',0,NULL,'2012-12-20 11:16:16',NULL),
	(6,1,'genericDna.A260_230','1.9',0,NULL,'2012-12-20 11:16:16',NULL),
	(7,1,'chipseqDna.fragmentSize','250',0,NULL,'2012-12-20 11:16:16',NULL),
	(8,1,'chipseqDna.fragmentSizeSD','25',0,NULL,'2012-12-20 11:16:16',NULL),
	(9,1,'chipseqDna.antibody','goat',0,NULL,'2012-12-20 11:16:16',NULL),
	(10,2,'genericBiomolecule.species','Human',0,NULL,'2012-12-20 11:16:41',NULL),
	(11,2,'genericDna.concentration','23',0,NULL,'2012-12-20 11:16:41',NULL),
	(12,2,'genericDna.volume','10',0,NULL,'2012-12-20 11:16:41',NULL),
	(13,2,'genericDna.buffer','TE',0,NULL,'2012-12-20 11:16:41',NULL),
	(14,2,'genericDna.A260_280','1.74',0,NULL,'2012-12-20 11:16:41',NULL),
	(15,2,'genericDna.A260_230','1.86',0,NULL,'2012-12-20 11:16:41',NULL),
	(16,2,'chipseqDna.fragmentSize','250',0,NULL,'2012-12-20 11:16:41',NULL),
	(17,2,'chipseqDna.fragmentSizeSD','25',0,NULL,'2012-12-20 11:16:41',NULL),
	(18,2,'chipseqDna.antibody','goat',0,NULL,'2012-12-20 11:16:41',NULL),
	(19,3,'genericBiomolecule.species','Human',0,NULL,'2012-12-20 11:17:07',NULL),
	(20,3,'genericDna.concentration','21',0,NULL,'2012-12-20 11:17:07',NULL),
	(21,3,'genericDna.volume','10',0,NULL,'2012-12-20 11:17:07',NULL),
	(22,3,'genericDna.buffer','TE',0,NULL,'2012-12-20 11:17:07',NULL),
	(23,3,'genericDna.A260_280','1.65',0,NULL,'2012-12-20 11:17:07',NULL),
	(24,3,'genericDna.A260_230','1.83',0,NULL,'2012-12-20 11:17:07',NULL),
	(25,3,'chipseqDna.fragmentSize','250',0,NULL,'2012-12-20 11:17:07',NULL),
	(26,3,'chipseqDna.fragmentSizeSD','25',0,NULL,'2012-12-20 11:17:07',NULL),
	(27,3,'chipseqDna.antibody','goat',0,NULL,'2012-12-20 11:17:07',NULL),
	(28,4,'genericBiomolecule.species','Human',0,NULL,'2012-12-20 11:26:22',NULL),
	(29,4,'chipseqDna.fragmentSize','200',0,NULL,'2012-12-20 11:26:22',NULL),
	(30,4,'chipseqDna.fragmentSizeSD','10',0,NULL,'2012-12-20 11:26:22',NULL),
	(31,4,'chipseqDna.antibody','sheep',0,NULL,'2012-12-20 11:26:22',NULL),
	(32,4,'genericLibrary.concentration','34',0,NULL,'2012-12-20 11:26:22',NULL),
	(33,4,'genericLibrary.volume','10',0,NULL,'2012-12-20 11:26:22',NULL),
	(34,4,'genericLibrary.buffer','Water',0,NULL,'2012-12-20 11:26:22',NULL),
	(35,4,'genericLibrary.adaptorset','2',0,NULL,'2012-12-20 11:26:22',NULL),
	(36,4,'genericLibrary.adaptor','3',0,NULL,'2012-12-20 11:26:22',NULL),
	(37,4,'genericLibrary.size','500',0,NULL,'2012-12-20 11:26:22',NULL),
	(38,4,'genericLibrary.sizeSd','55',0,NULL,'2012-12-20 11:26:22',NULL),
	(39,5,'genericBiomolecule.species','Human',0,NULL,'2012-12-20 11:26:37',NULL),
	(40,5,'chipseqDna.fragmentSize','200',0,NULL,'2012-12-20 11:26:37',NULL),
	(41,5,'chipseqDna.fragmentSizeSD','10',0,NULL,'2012-12-20 11:26:37',NULL),
	(42,5,'chipseqDna.antibody','sheep',0,NULL,'2012-12-20 11:26:37',NULL),
	(43,5,'genericLibrary.concentration','34',0,NULL,'2012-12-20 11:26:37',NULL),
	(44,5,'genericLibrary.volume','10',0,NULL,'2012-12-20 11:26:37',NULL),
	(45,5,'genericLibrary.buffer','Water',0,NULL,'2012-12-20 11:26:37',NULL),
	(46,5,'genericLibrary.adaptorset','2',0,NULL,'2012-12-20 11:26:37',NULL),
	(47,5,'genericLibrary.adaptor','3',0,NULL,'2012-12-20 11:26:37',NULL),
	(48,5,'genericLibrary.size','500',0,NULL,'2012-12-20 11:26:37',NULL),
	(49,5,'genericLibrary.sizeSd','55',0,NULL,'2012-12-20 11:26:37',NULL);

/*!40000 ALTER TABLE `sampledraftmeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sampledraftmeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sampledraftmeta_AUD`;

CREATE TABLE `sampledraftmeta_AUD` (
  `sampleDraftMetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `sampledraftid` int(11) DEFAULT NULL,
  PRIMARY KEY (`sampleDraftMetaId`,`REV`),
  KEY `FK5868908DDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `sampledraftmeta_AUD` WRITE;
/*!40000 ALTER TABLE `sampledraftmeta_AUD` DISABLE KEYS */;

INSERT INTO `sampledraftmeta_AUD` (`sampleDraftMetaId`, `REV`, `REVTYPE`, `sampledraftid`)
VALUES
	(1,49,0,1),
	(2,49,0,1),
	(3,49,0,1),
	(4,49,0,1),
	(5,49,0,1),
	(6,49,0,1),
	(7,49,0,1),
	(8,49,0,1),
	(9,49,0,1),
	(10,50,0,2),
	(11,50,0,2),
	(12,50,0,2),
	(13,50,0,2),
	(14,50,0,2),
	(15,50,0,2),
	(16,50,0,2),
	(17,50,0,2),
	(18,50,0,2),
	(19,51,0,3),
	(20,51,0,3),
	(21,51,0,3),
	(22,51,0,3),
	(23,51,0,3),
	(24,51,0,3),
	(25,51,0,3),
	(26,51,0,3),
	(27,51,0,3),
	(28,70,0,4),
	(29,70,0,4),
	(30,70,0,4),
	(31,70,0,4),
	(32,70,0,4),
	(33,70,0,4),
	(34,70,0,4),
	(35,70,0,4),
	(36,70,0,4),
	(37,70,0,4),
	(38,70,0,4),
	(39,71,0,5),
	(40,71,0,5),
	(41,71,0,5),
	(42,71,0,5),
	(43,71,0,5),
	(44,71,0,5),
	(45,71,0,5),
	(46,71,0,5),
	(47,71,0,5),
	(48,71,0,5),
	(49,71,0,5);

/*!40000 ALTER TABLE `sampledraftmeta_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplefile
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplefile`;

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



# Dump of table samplefile_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplefile_AUD`;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table samplejobcellselection
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplejobcellselection`;

CREATE TABLE `samplejobcellselection` (
  `samplejobcellselectionid` int(10) NOT NULL AUTO_INCREMENT,
  `sampleid` int(10) DEFAULT NULL,
  `jobcellselectionid` int(10) DEFAULT NULL,
  `libraryindex` int(10) DEFAULT NULL,
  PRIMARY KEY (`samplejobcellselectionid`),
  UNIQUE KEY `u_samplecell_jdcid_li` (`jobcellselectionid`,`libraryindex`),
  KEY `FK216C5777B8A37246` (`sampleid`),
  KEY `FK216C57774704CC` (`jobcellselectionid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `samplejobcellselection` WRITE;
/*!40000 ALTER TABLE `samplejobcellselection` DISABLE KEYS */;

INSERT INTO `samplejobcellselection` (`samplejobcellselectionid`, `sampleid`, `jobcellselectionid`, `libraryindex`)
VALUES
	(1,21,1,1),
	(2,22,1,2),
	(3,23,1,3),
	(4,25,2,1),
	(5,26,3,1);

/*!40000 ALTER TABLE `samplejobcellselection` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplejobcellselection_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplejobcellselection_AUD`;

CREATE TABLE `samplejobcellselection_AUD` (
  `sampleJobCellSelectionId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `jobcellselectionid` int(11) DEFAULT NULL,
  `libraryindex` int(11) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`sampleJobCellSelectionId`,`REV`),
  KEY `FK5C21F1C8DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `samplejobcellselection_AUD` WRITE;
/*!40000 ALTER TABLE `samplejobcellselection_AUD` DISABLE KEYS */;

INSERT INTO `samplejobcellselection_AUD` (`sampleJobCellSelectionId`, `REV`, `REVTYPE`, `jobcellselectionid`, `libraryindex`, `sampleid`)
VALUES
	(1,63,0,1,1,21),
	(2,63,0,1,2,22),
	(3,63,0,1,3,23),
	(4,78,0,2,1,25),
	(5,78,0,3,1,26);

/*!40000 ALTER TABLE `samplejobcellselection_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplelab
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplelab`;

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



# Dump of table samplelab_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplelab_AUD`;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table samplemeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplemeta`;

CREATE TABLE `samplemeta` (
  `samplemetaid` int(10) NOT NULL AUTO_INCREMENT,
  `sampleid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` text,
  `position` int(10) DEFAULT '0',
  `rolevisibility` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`samplemetaid`),
  UNIQUE KEY `u_samplemeta_k_sid` (`k`,`sampleid`),
  KEY `fk_samplemeta_sampleid` (`sampleid`),
  CONSTRAINT `samplemeta_ibfk_1` FOREIGN KEY (`sampleid`) REFERENCES `sample` (`sampleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `samplemeta` WRITE;
/*!40000 ALTER TABLE `samplemeta` DISABLE KEYS */;

INSERT INTO `samplemeta` (`samplemetaid`, `sampleid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,'platformunitInstance.readType','paired',0,NULL,'2012-12-20 11:09:20',NULL),
	(2,1,'platformunitInstance.readlength','100',0,NULL,'2012-12-20 11:09:20',NULL),
	(3,1,'platformunitInstance.comment','',0,NULL,'2012-12-20 11:09:20',NULL),
	(4,10,'platformunitInstance.readType','single',0,NULL,'2012-12-20 11:09:48',NULL),
	(5,10,'platformunitInstance.readlength','75',0,NULL,'2012-12-20 11:09:48',NULL),
	(6,10,'platformunitInstance.comment','',0,NULL,'2012-12-20 11:09:48',NULL),
	(7,19,'platformunitInstance.readType','paired',0,NULL,'2012-12-20 11:10:27',NULL),
	(8,19,'platformunitInstance.readlength','150',0,NULL,'2012-12-20 11:10:27',NULL),
	(9,19,'platformunitInstance.comment','',0,NULL,'2012-12-20 11:10:27',NULL),
	(10,21,'genericBiomolecule.species','Human',0,NULL,'2012-12-20 11:20:04',10),
	(11,21,'genericDna.concentration','26',0,NULL,'2012-12-20 11:20:04',10),
	(12,21,'genericDna.volume','10',0,NULL,'2012-12-20 11:20:04',10),
	(13,21,'genericDna.buffer','TE',0,NULL,'2012-12-20 11:20:04',10),
	(14,21,'genericDna.A260_280','1.76',0,NULL,'2012-12-20 11:20:04',10),
	(15,21,'genericDna.A260_230','1.9',0,NULL,'2012-12-20 11:20:04',10),
	(16,21,'chipseqDna.fragmentSize','250',0,NULL,'2012-12-20 11:20:04',10),
	(17,21,'chipseqDna.fragmentSizeSD','25',0,NULL,'2012-12-20 11:20:04',10),
	(18,21,'chipseqDna.antibody','goat',0,NULL,'2012-12-20 11:20:04',10),
	(19,22,'genericBiomolecule.species','Human',0,NULL,'2012-12-20 11:20:04',10),
	(20,22,'genericDna.concentration','23',0,NULL,'2012-12-20 11:20:04',10),
	(21,22,'genericDna.volume','10',0,NULL,'2012-12-20 11:20:04',10),
	(22,22,'genericDna.buffer','TE',0,NULL,'2012-12-20 11:20:04',10),
	(23,22,'genericDna.A260_280','1.74',0,NULL,'2012-12-20 11:20:04',10),
	(24,22,'genericDna.A260_230','1.86',0,NULL,'2012-12-20 11:20:04',10),
	(25,22,'chipseqDna.fragmentSize','250',0,NULL,'2012-12-20 11:20:04',10),
	(26,22,'chipseqDna.fragmentSizeSD','25',0,NULL,'2012-12-20 11:20:04',10),
	(27,22,'chipseqDna.antibody','goat',0,NULL,'2012-12-20 11:20:04',10),
	(28,23,'genericBiomolecule.species','Human',0,NULL,'2012-12-20 11:20:04',10),
	(29,23,'genericDna.concentration','21',0,NULL,'2012-12-20 11:20:04',10),
	(30,23,'genericDna.volume','10',0,NULL,'2012-12-20 11:20:04',10),
	(31,23,'genericDna.buffer','TE',0,NULL,'2012-12-20 11:20:04',10),
	(32,23,'genericDna.A260_280','1.65',0,NULL,'2012-12-20 11:20:04',10),
	(33,23,'genericDna.A260_230','1.83',0,NULL,'2012-12-20 11:20:04',10),
	(34,23,'chipseqDna.fragmentSize','250',0,NULL,'2012-12-20 11:20:04',10),
	(35,23,'chipseqDna.fragmentSizeSD','25',0,NULL,'2012-12-20 11:20:04',10),
	(36,23,'chipseqDna.antibody','goat',0,NULL,'2012-12-20 11:20:04',10),
	(37,23,'statusMessage.sampleQCComment::4e42daec-91c3-484f-8cf8-1dad722fc4f8','Sample QC Comment::slightly concerned about size range but going to proceed anyway.',0,NULL,'2012-12-20 11:23:35',1),
	(38,24,'genericLibrary.concentration','25',0,NULL,'2012-12-20 11:24:31',NULL),
	(39,24,'genericLibrary.adaptor','2',0,NULL,'2012-12-20 11:24:31',NULL),
	(40,24,'genericLibrary.volume','50',0,NULL,'2012-12-20 11:24:31',NULL),
	(41,24,'genericLibrary.adaptorset','2',0,NULL,'2012-12-20 11:24:31',NULL),
	(42,24,'genericLibrary.sizeSd','20',0,NULL,'2012-12-20 11:24:31',NULL),
	(43,24,'genericLibrary.size','400',0,NULL,'2012-12-20 11:24:31',NULL),
	(44,24,'genericLibrary.buffer','TE',0,NULL,'2012-12-20 11:24:31',NULL),
	(45,25,'genericBiomolecule.species','Human',0,NULL,'2012-12-20 11:27:07',10),
	(46,25,'chipseqDna.fragmentSize','200',0,NULL,'2012-12-20 11:27:07',10),
	(47,25,'chipseqDna.fragmentSizeSD','10',0,NULL,'2012-12-20 11:27:07',10),
	(48,25,'chipseqDna.antibody','sheep',0,NULL,'2012-12-20 11:27:07',10),
	(49,25,'genericLibrary.concentration','34',0,NULL,'2012-12-20 11:27:07',10),
	(50,25,'genericLibrary.volume','10',0,NULL,'2012-12-20 11:27:07',10),
	(51,25,'genericLibrary.buffer','Water',0,NULL,'2012-12-20 11:27:07',10),
	(52,25,'genericLibrary.adaptorset','2',0,NULL,'2012-12-20 11:27:07',10),
	(53,25,'genericLibrary.adaptor','3',0,NULL,'2012-12-20 11:27:07',10),
	(54,25,'genericLibrary.size','500',0,NULL,'2012-12-20 11:27:07',10),
	(55,25,'genericLibrary.sizeSd','55',0,NULL,'2012-12-20 11:27:07',10),
	(56,26,'genericBiomolecule.species','Human',0,NULL,'2012-12-20 11:27:07',10),
	(57,26,'chipseqDna.fragmentSize','200',0,NULL,'2012-12-20 11:27:07',10),
	(58,26,'chipseqDna.fragmentSizeSD','10',0,NULL,'2012-12-20 11:27:07',10),
	(59,26,'chipseqDna.antibody','sheep',0,NULL,'2012-12-20 11:27:07',10),
	(60,26,'genericLibrary.concentration','34',0,NULL,'2012-12-20 11:27:07',10),
	(61,26,'genericLibrary.volume','10',0,NULL,'2012-12-20 11:27:07',10),
	(62,26,'genericLibrary.buffer','Water',0,NULL,'2012-12-20 11:27:07',10),
	(63,26,'genericLibrary.adaptorset','2',0,NULL,'2012-12-20 11:27:07',10),
	(64,26,'genericLibrary.adaptor','3',0,NULL,'2012-12-20 11:27:07',10),
	(65,26,'genericLibrary.size','500',0,NULL,'2012-12-20 11:27:07',10),
	(66,26,'genericLibrary.sizeSd','55',0,NULL,'2012-12-20 11:27:07',10);

/*!40000 ALTER TABLE `samplemeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplemeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplemeta_AUD`;

CREATE TABLE `samplemeta_AUD` (
  `sampleMetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`sampleMetaId`,`REV`),
  KEY `FKB41EE500DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `samplemeta_AUD` WRITE;
/*!40000 ALTER TABLE `samplemeta_AUD` DISABLE KEYS */;

INSERT INTO `samplemeta_AUD` (`sampleMetaId`, `REV`, `REVTYPE`, `sampleid`)
VALUES
	(1,43,0,1),
	(2,43,0,1),
	(3,43,0,1),
	(4,44,0,10),
	(5,44,0,10),
	(6,44,0,10),
	(7,45,0,19),
	(8,45,0,19),
	(9,45,0,19),
	(10,63,0,21),
	(11,63,0,21),
	(12,63,0,21),
	(13,63,0,21),
	(14,63,0,21),
	(15,63,0,21),
	(16,63,0,21),
	(17,63,0,21),
	(18,63,0,21),
	(19,63,0,22),
	(20,63,0,22),
	(21,63,0,22),
	(22,63,0,22),
	(23,63,0,22),
	(24,63,0,22),
	(25,63,0,22),
	(26,63,0,22),
	(27,63,0,22),
	(28,63,0,23),
	(29,63,0,23),
	(30,63,0,23),
	(31,63,0,23),
	(32,63,0,23),
	(33,63,0,23),
	(34,63,0,23),
	(35,63,0,23),
	(36,63,0,23),
	(37,65,0,23),
	(38,66,0,24),
	(39,66,0,24),
	(40,66,0,24),
	(41,66,0,24),
	(42,66,0,24),
	(43,66,0,24),
	(44,66,0,24),
	(45,78,0,25),
	(46,78,0,25),
	(47,78,0,25),
	(48,78,0,25),
	(49,78,0,25),
	(50,78,0,25),
	(51,78,0,25),
	(52,78,0,25),
	(53,78,0,25),
	(54,78,0,25),
	(55,78,0,25),
	(56,78,0,26),
	(57,78,0,26),
	(58,78,0,26),
	(59,78,0,26),
	(60,78,0,26),
	(61,78,0,26),
	(62,78,0,26),
	(63,78,0,26),
	(64,78,0,26),
	(65,78,0,26),
	(66,78,0,26);

/*!40000 ALTER TABLE `samplemeta_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplesource
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplesource`;

CREATE TABLE `samplesource` (
  `samplesourceid` int(10) NOT NULL AUTO_INCREMENT,
  `sampleid` int(10) DEFAULT NULL,
  `indexvalue` int(10) DEFAULT '0',
  `source_sampleid` int(10) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`samplesourceid`),
  UNIQUE KEY `u_samplesource_sid` (`sampleid`,`indexvalue`),
  KEY `fk_samplesource_ssid` (`source_sampleid`),
  CONSTRAINT `samplesource_ibfk_1` FOREIGN KEY (`sampleid`) REFERENCES `sample` (`sampleid`),
  CONSTRAINT `samplesource_ibfk_2` FOREIGN KEY (`source_sampleid`) REFERENCES `sample` (`sampleid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `samplesource` WRITE;
/*!40000 ALTER TABLE `samplesource` DISABLE KEYS */;

INSERT INTO `samplesource` (`samplesourceid`, `sampleid`, `indexvalue`, `source_sampleid`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,1,2,'2012-12-20 11:09:20',1),
	(2,1,2,3,'2012-12-20 11:09:20',1),
	(3,1,3,4,'2012-12-20 11:09:20',1),
	(4,1,4,5,'2012-12-20 11:09:20',1),
	(5,1,5,6,'2012-12-20 11:09:20',1),
	(6,1,6,7,'2012-12-20 11:09:20',1),
	(7,1,7,8,'2012-12-20 11:09:20',1),
	(8,1,8,9,'2012-12-20 11:09:20',1),
	(9,10,1,11,'2012-12-20 11:09:48',1),
	(10,10,2,12,'2012-12-20 11:09:48',1),
	(11,10,3,13,'2012-12-20 11:09:48',1),
	(12,10,4,14,'2012-12-20 11:09:48',1),
	(13,10,5,15,'2012-12-20 11:09:48',1),
	(14,10,6,16,'2012-12-20 11:09:48',1),
	(15,10,7,17,'2012-12-20 11:09:48',1),
	(16,10,8,18,'2012-12-20 11:09:48',1),
	(17,19,1,20,'2012-12-20 11:10:27',1);

/*!40000 ALTER TABLE `samplesource` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplesource_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplesource_AUD`;

CREATE TABLE `samplesource_AUD` (
  `sampleSourceId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `indexvalue` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `sampleid` int(11) DEFAULT NULL,
  `source_sampleid` int(11) DEFAULT NULL,
  PRIMARY KEY (`sampleSourceId`,`REV`),
  KEY `FKA05CF996DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `samplesource_AUD` WRITE;
/*!40000 ALTER TABLE `samplesource_AUD` DISABLE KEYS */;

INSERT INTO `samplesource_AUD` (`sampleSourceId`, `REV`, `REVTYPE`, `indexvalue`, `lastupdts`, `lastupduser`, `sampleid`, `source_sampleid`)
VALUES
	(1,43,0,1,'2012-12-20 11:09:20',1,1,2),
	(2,43,0,2,'2012-12-20 11:09:20',1,1,3),
	(3,43,0,3,'2012-12-20 11:09:20',1,1,4),
	(4,43,0,4,'2012-12-20 11:09:20',1,1,5),
	(5,43,0,5,'2012-12-20 11:09:20',1,1,6),
	(6,43,0,6,'2012-12-20 11:09:20',1,1,7),
	(7,43,0,7,'2012-12-20 11:09:20',1,1,8),
	(8,43,0,8,'2012-12-20 11:09:20',1,1,9),
	(9,44,0,1,'2012-12-20 11:09:48',1,10,11),
	(10,44,0,2,'2012-12-20 11:09:48',1,10,12),
	(11,44,0,3,'2012-12-20 11:09:48',1,10,13),
	(12,44,0,4,'2012-12-20 11:09:48',1,10,14),
	(13,44,0,5,'2012-12-20 11:09:48',1,10,15),
	(14,44,0,6,'2012-12-20 11:09:48',1,10,16),
	(15,44,0,7,'2012-12-20 11:09:48',1,10,17),
	(16,44,0,8,'2012-12-20 11:09:48',1,10,18),
	(17,45,0,1,'2012-12-20 11:10:27',1,19,20);

/*!40000 ALTER TABLE `samplesource_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplesourcemeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplesourcemeta`;

CREATE TABLE `samplesourcemeta` (
  `samplesourcemetaid` int(10) NOT NULL AUTO_INCREMENT,
  `samplesourceid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` text,
  `position` int(10) DEFAULT '0',
  `rolevisibility` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`samplesourcemetaid`),
  UNIQUE KEY `u_samplesourcemeta_k_sid` (`k`,`samplesourceid`),
  KEY `fk_samplesourcemeta_sampleid` (`samplesourceid`),
  CONSTRAINT `samplesourcemeta_ibfk_1` FOREIGN KEY (`samplesourceid`) REFERENCES `samplesource` (`samplesourceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table samplesourcemeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplesourcemeta_AUD`;

CREATE TABLE `samplesourcemeta_AUD` (
  `sampleSourceMetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `samplesourceid` int(11) DEFAULT NULL,
  PRIMARY KEY (`sampleSourceMetaId`,`REV`),
  KEY `FK24D61A9BDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table samplesubtype
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplesubtype`;

CREATE TABLE `samplesubtype` (
  `samplesubtypeid` int(10) NOT NULL AUTO_INCREMENT,
  `sampletypeid` int(10) DEFAULT NULL,
  `iname` varchar(50) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  `arealist` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`samplesubtypeid`),
  UNIQUE KEY `u_samplesubtype_iname` (`iname`),
  KEY `fk_samplesubtype_tsid` (`sampletypeid`),
  CONSTRAINT `samplesubtype_ibfk_1` FOREIGN KEY (`sampletypeid`) REFERENCES `sampletype` (`sampletypeid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `samplesubtype` WRITE;
/*!40000 ALTER TABLE `samplesubtype` DISABLE KEYS */;

INSERT INTO `samplesubtype` (`samplesubtypeid`, `sampletypeid`, `iname`, `name`, `isactive`, `arealist`)
VALUES
	(1,1,'controlLibrarySample','Control Library',1,'genericBiomolecule,genericLibrary'),
	(2,2,'bisulseqDnaSample','BISUL-seq DNA',1,'genericBiomolecule,genericDna,bisulseqDna'),
	(3,1,'bisulseqLibrarySample','BISUL-seq Library',1,'genericBiomolecule,bisulseqDna,genericLibrary'),
	(4,1,'bisulseqFacilityLibrarySample','BISUL-seq Facility Library',1,'genericLibrary'),
	(5,2,'chipseqDnaSample','ChIP-seq DNA',1,'genericBiomolecule,genericDna,chipseqDna'),
	(6,1,'chipseqLibrarySample','ChIP-seq Library',1,'genericBiomolecule,chipseqDna,genericLibrary'),
	(7,1,'chipseqFacilityLibrarySample','ChIP-seq Facility Library',1,'genericLibrary'),
	(8,1,'helptagLibrarySample','HELP-tag Library',1,'genericBiomolecule,genericLibrary,helptagLibrary'),
	(9,5,'illuminaFlowcellMiSeqV1','Illumina Flow Cell MiSeq V1',1,'illuminaFlowcellMiSeqV1'),
	(10,5,'illuminaFlowcellV3','Illumina Flow Cell Version 3',1,'illuminaFlowcellV3');

/*!40000 ALTER TABLE `samplesubtype` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplesubtype_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplesubtype_AUD`;

CREATE TABLE `samplesubtype_AUD` (
  `sampleSubtypeId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `arealist` varchar(255) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sampletypeid` int(11) DEFAULT NULL,
  PRIMARY KEY (`sampleSubtypeId`,`REV`),
  KEY `FK92994A61DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `samplesubtype_AUD` WRITE;
/*!40000 ALTER TABLE `samplesubtype_AUD` DISABLE KEYS */;

INSERT INTO `samplesubtype_AUD` (`sampleSubtypeId`, `REV`, `REVTYPE`, `arealist`, `iname`, `isactive`, `name`, `sampletypeid`)
VALUES
	(1,4,0,'genericBiomolecule,genericLibrary','controlLibrarySample',1,'Control Library',1),
	(2,17,0,'genericBiomolecule,genericDna,bisulseqDna','bisulseqDnaSample',1,'BISUL-seq DNA',2),
	(3,18,0,'genericBiomolecule,bisulseqDna,genericLibrary','bisulseqLibrarySample',1,'BISUL-seq Library',1),
	(4,19,0,'genericLibrary','bisulseqFacilityLibrarySample',1,'BISUL-seq Facility Library',1),
	(5,21,0,'genericBiomolecule,genericDna,chipseqDna','chipseqDnaSample',1,'ChIP-seq DNA',2),
	(6,22,0,'genericBiomolecule,chipseqDna,genericLibrary','chipseqLibrarySample',1,'ChIP-seq Library',1),
	(7,23,0,'genericLibrary','chipseqFacilityLibrarySample',1,'ChIP-seq Facility Library',1),
	(8,29,0,'genericBiomolecule,genericLibrary,helptagLibrary','helptagLibrarySample',1,'HELP-tag Library',1),
	(9,34,0,'illuminaFlowcellMiSeqV1','illuminaFlowcellMiSeqV1',1,'Illumina Flow Cell MiSeq V1',5),
	(10,35,0,'illuminaFlowcellV3','illuminaFlowcellV3',1,'Illumina Flow Cell Version 3',5);

/*!40000 ALTER TABLE `samplesubtype_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplesubtypemeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplesubtypemeta`;

CREATE TABLE `samplesubtypemeta` (
  `samplesubtypemetaid` int(10) NOT NULL AUTO_INCREMENT,
  `samplesubtypeid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` text,
  `position` int(10) DEFAULT '0',
  `rolevisibility` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`samplesubtypemetaid`),
  UNIQUE KEY `u_samplesubtypemeta_k_sid` (`k`,`samplesubtypeid`),
  KEY `fk_samplesubtypemeta_sampleid` (`samplesubtypeid`),
  CONSTRAINT `samplesubtypemeta_ibfk_1` FOREIGN KEY (`samplesubtypeid`) REFERENCES `samplesubtype` (`samplesubtypeid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `samplesubtypemeta` WRITE;
/*!40000 ALTER TABLE `samplesubtypemeta` DISABLE KEYS */;

INSERT INTO `samplesubtypemeta` (`samplesubtypemetaid`, `samplesubtypeid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,'controlLibrarySample.includeRoles','ft,su',1,NULL,'2012-12-20 11:03:30',0),
	(2,2,'bisulseqDnaSample.includeRoles','ft,lu',1,NULL,'2012-12-20 11:03:30',0),
	(3,3,'bisulseqLibrarySample.includeRoles','lu',1,NULL,'2012-12-20 11:03:30',0),
	(4,4,'bisulseqFacilityLibrarySample.includeRoles','ft',1,NULL,'2012-12-20 11:03:30',0),
	(5,5,'chipseqDnaSample.includeRoles','ft,lu',1,NULL,'2012-12-20 11:03:30',0),
	(6,6,'chipseqLibrarySample.includeRoles','lu',1,NULL,'2012-12-20 11:03:31',0),
	(7,7,'chipseqFacilityLibrarySample.includeRoles','ft',1,NULL,'2012-12-20 11:03:31',0),
	(8,8,'helptagLibrarySample.includeRoles','ft,lu',1,NULL,'2012-12-20 11:03:31',0),
	(9,9,'illuminaFlowcellMiSeqV1.maxCellNumber','1',1,NULL,'2012-12-20 11:03:32',0),
	(10,9,'illuminaFlowcellMiSeqV1.multiplicationFactor','1',2,NULL,'2012-12-20 11:19:23',0),
	(11,10,'illuminaFlowcellV3.maxCellNumber','8',1,NULL,'2012-12-20 11:03:32',0),
	(12,10,'illuminaFlowcellV3.multiplicationFactor','2',2,NULL,'2012-12-20 11:19:23',0);

/*!40000 ALTER TABLE `samplesubtypemeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplesubtypemeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplesubtypemeta_AUD`;

CREATE TABLE `samplesubtypemeta_AUD` (
  `sampleSubtypeMetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `samplesubtypeid` int(11) DEFAULT NULL,
  PRIMARY KEY (`sampleSubtypeMetaId`,`REV`),
  KEY `FKB970DE6DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `samplesubtypemeta_AUD` WRITE;
/*!40000 ALTER TABLE `samplesubtypemeta_AUD` DISABLE KEYS */;

INSERT INTO `samplesubtypemeta_AUD` (`sampleSubtypeMetaId`, `REV`, `REVTYPE`, `samplesubtypeid`)
VALUES
	(1,4,0,1),
	(2,17,0,2),
	(3,18,0,3),
	(4,19,0,4),
	(5,21,0,5),
	(6,22,0,6),
	(7,23,0,7),
	(8,29,0,8),
	(9,34,0,9),
	(10,34,0,9),
	(11,35,0,10),
	(12,35,0,10);

/*!40000 ALTER TABLE `samplesubtypemeta_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplesubtyperesourcecategory
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplesubtyperesourcecategory`;

CREATE TABLE `samplesubtyperesourcecategory` (
  `samplesubtyperesourcecategoryid` int(10) NOT NULL AUTO_INCREMENT,
  `samplesubtypeid` int(10) DEFAULT NULL,
  `resourcecategoryid` int(10) DEFAULT NULL,
  PRIMARY KEY (`samplesubtyperesourcecategoryid`),
  KEY `fk_samplesubtyperesourcecategory_stscid` (`samplesubtypeid`),
  KEY `fk_samplesubtyperesourcecategory_rcid` (`resourcecategoryid`),
  CONSTRAINT `samplesubtyperesourcecategory_ibfk_1` FOREIGN KEY (`samplesubtypeid`) REFERENCES `samplesubtype` (`samplesubtypeid`),
  CONSTRAINT `samplesubtyperesourcecategory_ibfk_2` FOREIGN KEY (`resourcecategoryid`) REFERENCES `resourcecategory` (`resourcecategoryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `samplesubtyperesourcecategory` WRITE;
/*!40000 ALTER TABLE `samplesubtyperesourcecategory` DISABLE KEYS */;

INSERT INTO `samplesubtyperesourcecategory` (`samplesubtyperesourcecategoryid`, `samplesubtypeid`, `resourcecategoryid`)
VALUES
	(1,9,2),
	(2,10,1);

/*!40000 ALTER TABLE `samplesubtyperesourcecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplesubtyperesourcecategory_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `samplesubtyperesourcecategory_AUD`;

CREATE TABLE `samplesubtyperesourcecategory_AUD` (
  `sampleSubtypeResourceCategoryId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `resourcecategoryid` int(11) DEFAULT NULL,
  `samplesubtypeid` int(11) DEFAULT NULL,
  PRIMARY KEY (`sampleSubtypeResourceCategoryId`,`REV`),
  KEY `FK44FE576DDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `samplesubtyperesourcecategory_AUD` WRITE;
/*!40000 ALTER TABLE `samplesubtyperesourcecategory_AUD` DISABLE KEYS */;

INSERT INTO `samplesubtyperesourcecategory_AUD` (`sampleSubtypeResourceCategoryId`, `REV`, `REVTYPE`, `resourcecategoryid`, `samplesubtypeid`)
VALUES
	(1,34,0,2,9),
	(2,35,0,1,10);

/*!40000 ALTER TABLE `samplesubtyperesourcecategory_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sampletype
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sampletype`;

CREATE TABLE `sampletype` (
  `sampletypeid` int(10) NOT NULL AUTO_INCREMENT,
  `sampletypecategoryid` int(10) DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  `iname` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`sampletypeid`),
  UNIQUE KEY `u_sampletype_iname` (`iname`),
  KEY `fk_sampletype_tscid` (`sampletypecategoryid`),
  CONSTRAINT `sampletype_ibfk_1` FOREIGN KEY (`sampletypecategoryid`) REFERENCES `sampletypecategory` (`sampletypecategoryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `sampletype` WRITE;
/*!40000 ALTER TABLE `sampletype` DISABLE KEYS */;

INSERT INTO `sampletype` (`sampletypeid`, `sampletypecategoryid`, `isactive`, `iname`, `name`)
VALUES
	(1,1,1,'library','Library'),
	(2,1,1,'dna','DNA'),
	(3,1,1,'rna','RNA'),
	(4,2,1,'cell','Cell'),
	(5,2,1,'platformunit','Platform Unit'),
	(6,1,1,'tissue','Tissue'),
	(7,1,1,'facilityLibrary','Facilitylibrary');

/*!40000 ALTER TABLE `sampletype` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sampletype_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sampletype_AUD`;

CREATE TABLE `sampletype_AUD` (
  `sampleTypeId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sampletypecategoryid` int(11) DEFAULT NULL,
  PRIMARY KEY (`sampleTypeId`,`REV`),
  KEY `FKA6A8BBB5DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `sampletype_AUD` WRITE;
/*!40000 ALTER TABLE `sampletype_AUD` DISABLE KEYS */;

INSERT INTO `sampletype_AUD` (`sampleTypeId`, `REV`, `REVTYPE`, `iname`, `isactive`, `name`, `sampletypecategoryid`)
VALUES
	(1,3,0,'library',1,'Library',1),
	(2,9,0,'dna',1,'DNA',1),
	(3,10,0,'rna',1,'RNA',1),
	(4,11,0,'cell',1,'Cell',2),
	(5,12,0,'platformunit',1,'Platform Unit',2),
	(6,13,0,'tissue',1,'Tissue',1),
	(7,14,0,'facilityLibrary',1,'Facilitylibrary',1);

/*!40000 ALTER TABLE `sampletype_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sampletypecategory
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sampletypecategory`;

CREATE TABLE `sampletypecategory` (
  `sampletypecategoryid` int(10) NOT NULL AUTO_INCREMENT,
  `iname` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`sampletypecategoryid`),
  UNIQUE KEY `u_sampletypecategory_iname` (`iname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `sampletypecategory` WRITE;
/*!40000 ALTER TABLE `sampletypecategory` DISABLE KEYS */;

INSERT INTO `sampletypecategory` (`sampletypecategoryid`, `iname`, `name`)
VALUES
	(1,'biomaterial','Biomaterial'),
	(2,'hardware','Hardware');

/*!40000 ALTER TABLE `sampletypecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sampletypecategory_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `sampletypecategory_AUD`;

CREATE TABLE `sampletypecategory_AUD` (
  `sampleTypecategoryId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`sampleTypecategoryId`,`REV`),
  KEY `FK41FEA5B3DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table software
# ------------------------------------------------------------

DROP TABLE IF EXISTS `software`;

CREATE TABLE `software` (
  `softwareid` int(10) NOT NULL AUTO_INCREMENT,
  `resourcetypeid` int(10) NOT NULL,
  `iname` varchar(250) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `isactive` int(1) DEFAULT '1',
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`softwareid`),
  UNIQUE KEY `u_software_i` (`iname`),
  KEY `fk_software_resourcetypeid` (`resourcetypeid`),
  CONSTRAINT `software_ibfk_1` FOREIGN KEY (`resourcetypeid`) REFERENCES `resourcetype` (`resourcetypeid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `software` WRITE;
/*!40000 ALTER TABLE `software` DISABLE KEYS */;

INSERT INTO `software` (`softwareid`, `resourcetypeid`, `iname`, `name`, `isactive`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,'bowtieAligner','Bowtie Aligner',1,'2012-12-20 11:03:29',0),
	(2,4,'macsPeakcaller','MACS Peakcaller',1,'2012-12-20 11:03:30',0),
	(3,6,'bisulseqPipeline','BISUL-seq Pipeline',1,'2012-12-20 11:03:30',0),
	(4,7,'helptagPipeline','HELP-tag Pipeline',1,'2012-12-20 11:03:31',0),
	(5,5,'casava','CASAVA',1,'2012-12-20 11:03:31',0);

/*!40000 ALTER TABLE `software` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table software_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `software_AUD`;

CREATE TABLE `software_AUD` (
  `softwareId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `resourcetypeid` int(11) DEFAULT NULL,
  PRIMARY KEY (`softwareId`,`REV`),
  KEY `FKA56863F8DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `software_AUD` WRITE;
/*!40000 ALTER TABLE `software_AUD` DISABLE KEYS */;

INSERT INTO `software_AUD` (`softwareId`, `REV`, `REVTYPE`, `iname`, `isactive`, `lastupdts`, `lastupduser`, `name`, `resourcetypeid`)
VALUES
	(1,2,0,'bowtieAligner',1,'2012-12-20 11:03:29',0,'Bowtie Aligner',1),
	(2,15,0,'macsPeakcaller',1,'2012-12-20 11:03:30',0,'MACS Peakcaller',4),
	(3,16,0,'bisulseqPipeline',1,'2012-12-20 11:03:30',0,'BISUL-seq Pipeline',6),
	(4,28,0,'helptagPipeline',1,'2012-12-20 11:03:31',0,'HELP-tag Pipeline',7),
	(5,31,0,'casava',1,'2012-12-20 11:03:31',0,'CASAVA',5);

/*!40000 ALTER TABLE `software_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table softwaremeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `softwaremeta`;

CREATE TABLE `softwaremeta` (
  `softwaremetaid` int(10) NOT NULL AUTO_INCREMENT,
  `softwareid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` text,
  `position` int(10) DEFAULT '0',
  `rolevisibility` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`softwaremetaid`),
  UNIQUE KEY `u_softwaremeta_k_rid` (`k`,`softwareid`),
  KEY `fk_softwaremeta_sid` (`softwareid`),
  CONSTRAINT `softwaremeta_ibfk_1` FOREIGN KEY (`softwareid`) REFERENCES `software` (`softwareid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `softwaremeta` WRITE;
/*!40000 ALTER TABLE `softwaremeta` DISABLE KEYS */;

INSERT INTO `softwaremeta` (`softwaremetaid`, `softwareid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,'bowtieAligner.currentVersion','0.12.7',1,NULL,'2012-12-20 11:03:29',0),
	(2,1,'bowtieAligner.priorVersions','',2,NULL,'2012-12-20 11:03:29',0),
	(3,2,'macsPeakcaller.currentVersion','4.1',1,NULL,'2012-12-20 11:03:30',0),
	(4,2,'macsPeakcaller.priorVersions','',2,NULL,'2012-12-20 11:03:30',0),
	(5,3,'bisulseqPipeline.currentVersion','1.0',1,NULL,'2012-12-20 11:03:30',0),
	(6,3,'bisulseqPipeline.priorVersions','',2,NULL,'2012-12-20 11:03:30',0),
	(7,4,'helptagPipeline.currentVersion','1.0',1,NULL,'2012-12-20 11:03:31',0),
	(8,4,'helptagPipeline.priorVersions','',2,NULL,'2012-12-20 11:03:31',0),
	(9,5,'casava.currentVersion','1.8.2',1,NULL,'2012-12-20 11:03:31',0),
	(10,5,'casava.priorVersions','',2,NULL,'2012-12-20 11:03:31',0);

/*!40000 ALTER TABLE `softwaremeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table softwaremeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `softwaremeta_AUD`;

CREATE TABLE `softwaremeta_AUD` (
  `softwareMetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `softwareid` int(11) DEFAULT NULL,
  PRIMARY KEY (`softwareMetaId`,`REV`),
  KEY `FK878183FDDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `softwaremeta_AUD` WRITE;
/*!40000 ALTER TABLE `softwaremeta_AUD` DISABLE KEYS */;

INSERT INTO `softwaremeta_AUD` (`softwareMetaId`, `REV`, `REVTYPE`, `softwareid`)
VALUES
	(1,2,0,1),
	(2,2,0,1),
	(3,15,0,2),
	(4,15,0,2),
	(5,16,0,3),
	(6,16,0,3),
	(7,28,0,4),
	(8,28,0,4),
	(9,31,0,5),
	(10,31,0,5);

/*!40000 ALTER TABLE `softwaremeta_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table taskmapping
# ------------------------------------------------------------

DROP TABLE IF EXISTS `taskmapping`;

CREATE TABLE `taskmapping` (
  `taskmappingid` int(11) NOT NULL AUTO_INCREMENT,
  `dashboardsortorder` int(11) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `listmap` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `permission` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `stepname` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`taskmappingid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table taskmapping_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `taskmapping_AUD`;

CREATE TABLE `taskmapping_AUD` (
  `taskmappingid` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `dashboardsortorder` int(11) DEFAULT NULL,
  `iname` varchar(255) DEFAULT NULL,
  `isactive` int(11) DEFAULT NULL,
  `listmap` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `permission` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `stepname` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`taskmappingid`,`REV`),
  KEY `FK6234C4FADF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table uifield
# ------------------------------------------------------------

DROP TABLE IF EXISTS `uifield`;

CREATE TABLE `uifield` (
  `uifieldid` int(10) NOT NULL AUTO_INCREMENT,
  `locale` varchar(5) DEFAULT NULL,
  `domain` varchar(100) DEFAULT NULL,
  `area` varchar(50) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `attrname` varchar(50) DEFAULT NULL,
  `attrvalue` text,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  PRIMARY KEY (`uifieldid`),
  UNIQUE KEY `u_uifield_laaa` (`locale`,`area`,`name`,`attrname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `uifield` WRITE;
/*!40000 ALTER TABLE `uifield` DISABLE KEYS */;

INSERT INTO `uifield` (`uifieldid`, `locale`, `domain`, `area`, `name`, `attrname`, `attrvalue`, `lastupdts`, `lastupduser`)
VALUES
	(1,'en_US','','acctQuote','acctquote_list','label','List of Job Quotes',NULL,1),
	(2,'en_US','','acctQuote','amount','label','Quote Amount',NULL,1),
	(3,'en_US','','acctQuote','created_success','label','Quote created successfully.',NULL,1),
	(4,'en_US','','acctQuote','lab','label','PI',NULL,1),
	(5,'en_US','','acctQuote','lane_cost','label','Cell Cost',NULL,1),
	(6,'en_US','','acctQuote','lane_cost','metaposition','40',NULL,1),
	(7,'en_US','','acctQuote','library_cost','label','Library Cost',NULL,1),
	(8,'en_US','','acctQuote','library_cost','metaposition','20',NULL,1),
	(9,'en_US','','acctQuote','jobId','label','Job ID',NULL,1),
	(10,'en_US','','acctQuote','name','label','Job Name',NULL,1),
	(11,'en_US','','acctQuote','sample_cost','label','Sample Cost',NULL,1),
	(12,'en_US','','acctQuote','sample_cost','metaposition','30',NULL,1),
	(13,'en_US','','acctQuote','submitted_on','label','Submitted On',NULL,1),
	(14,'en_US','','acctQuote','submitter','label','Submitted By',NULL,1),
	(15,'en_US','','activePlatformUnit','tableHeader','label','Active Platform Units Awaiting Libraries',NULL,1),
	(16,'en_US','','activePlatformUnit','name','label','Name',NULL,1),
	(17,'en_US','','activePlatformUnit','barcode','label','Barcode',NULL,1),
	(18,'en_US','','activePlatformUnit','type','label','Type',NULL,1),
	(19,'en_US','','jobListAssignLibrary','numberLanes','label','Cells',NULL,1),
	(20,'en_US','','auth','confirmemail','data','Submit',NULL,1),
	(21,'en_US','','auth','confirmemail_authcode','label','Auth code',NULL,1),
	(22,'en_US','','auth','confirmemail_badauthcode','error','Invalid authorization code provided',NULL,1),
	(23,'en_US','','auth','confirmemail_bademail','error','email address is incorrect',NULL,1),
	(24,'en_US','','auth','confirmemail_captcha','error','Captcha text incorrect',NULL,1),
	(25,'en_US','','auth','confirmemail_captcha','label','Captcha text',NULL,1),
	(26,'en_US','','auth','confirmemail_corruptemail','error','email address in url cannot be decoded',NULL,1),
	(27,'en_US','','auth','confirmemail_email','label','Email Address',NULL,1),
	(28,'en_US','','auth','confirmemail_submit','label','Submit',NULL,1),
	(29,'en_US','','auth','confirmemail_wronguser','error','User email address and authorization code provided do not match',NULL,1),
	(30,'en_US','','auth','get_email_instructions','label','Please enter a valid email address. This is required for demonstrating functionality which results in an email being sent by the system. This email address will be stored for 24h in a cookie on your personal computer only - we do NOT store your email address or any sent mail on our server. For demonstration purposes it is used to override the send-to email address of all demonstration user accounts and mock account applications you might make.',NULL,1),
	(31,'en_US','','auth','demo_email','label','Please enter your email address',NULL,1),
	(32,'en_US','','auth','demo_email','error','You must supply an email address',NULL,1),
	(33,'en_US','','auth','demo_email_submit','label','Submit',NULL,1),
	(34,'en_US','','auth','directusertologin','label','Please click to <a href=\"../login.do\"/>Login</a>',NULL,1),
	(35,'en_US','','auth','login','data','Login',NULL,1),
	(36,'en_US','','auth','login_anchor_about','label','About',NULL,1),
	(37,'en_US','','auth','login_anchor_forgotpass','label','Forgot Password',NULL,1),
	(38,'en_US','','auth','login_anchor_newpi','label','New PI',NULL,1),
	(39,'en_US','','auth','login_anchor_newuser','label','New User',NULL,1),
	(40,'en_US','','auth','login_failed','error','Your login attempt was not successful. Try again.',NULL,1),
	(41,'en_US','','auth','login_instructions','label','Please login to the WASP System using your username and password. If you have forgotton your password, or are currently unregistered and wish to create an account please select from the links below.',NULL,1),
	(42,'en_US','','auth','login_password','label','Password',NULL,1),
	(43,'en_US','','auth','login_reason','label','Reason',NULL,1),
	(44,'en_US','','auth','login_submit','label','Login',NULL,1),
	(45,'en_US','','auth','login_user','label','User',NULL,1),
	(46,'en_US','','auth','newuser_confirmemail_wronguser','error','There is a problem confirming your email address as a new user. Please try again or contact a WASP administrator.',NULL,1),
	(47,'en_US','','auth','requestEmailChange_badcredentials','error','Failed to authenticate with supplied login credentials',NULL,1),
	(48,'en_US','','auth','requestEmailChange_bademail','error','email address is of incorrect format',NULL,1),
	(49,'en_US','','auth','requestEmailChange_captcha','error','Captcha text incorrect',NULL,1),
	(50,'en_US','','auth','requestEmailChange_email','label','New Email Address',NULL,1),
	(51,'en_US','','auth','requestEmailChange_submit','label','Submit',NULL,1),
	(52,'en_US','','auth','resetpassword','data','Submit',NULL,1),
	(53,'en_US','','auth','resetpasswordemailsent','label','An email has been sent to your registered email address containing an authorization code. Please click the link within this email or alternatively <a href=\"form.do\">click here</a> and enter the authorization code provided. ',NULL,1),
	(54,'en_US','','auth','resetpasswordok','label','Your password has been reset. Please click to <a href=\"../login.do\"/>Login</a>',NULL,1),
	(55,'en_US','','auth','resetpasswordok_title','label','Reset Password: Complete',NULL,1),
	(56,'en_US','','auth','resetpasswordRequest','data','Submit',NULL,1),
	(57,'en_US','','auth','resetpasswordRequest_captcha','error','Captcha text incorrect',NULL,1),
	(58,'en_US','','auth','resetpasswordRequest_captcha','label','Captcha text',NULL,1),
	(59,'en_US','','auth','resetpasswordRequest_missingparam','error','Please provide values for all fields',NULL,1),
	(60,'en_US','','auth','resetpasswordRequest_submit','label','Submit',NULL,1),
	(61,'en_US','','auth','resetpasswordRequest_user','label','Username',NULL,1),
	(62,'en_US','','auth','resetpasswordRequest_username','error','A user with the supplied username does not exist',NULL,1),
	(63,'en_US','','auth','resetpassword_authcode','label','Auth Code',NULL,1),
	(64,'en_US','','auth','resetpassword_badauthcode','error','Invalid authorization code provided',NULL,1),
	(65,'en_US','','auth','resetpassword_captcha','label','Captcha text',NULL,1),
	(66,'en_US','','auth','resetpassword_captcha','error','Captcha text incorrect',NULL,1),
	(67,'en_US','','auth','resetpassword_instructions','label','New Password Requirements:<br />At least 8 characters in length<br />Only letters & numbers permitted (no spaces, etc)<br />At least one letter and one number<br />',NULL,1),
	(68,'en_US','','auth','resetpassword_missingparam','error','Please provide values for all fields',NULL,1),
	(69,'en_US','','auth','resetpassword_new_invalid','error','New password must be at least 8 characters, containing only letters and numbers, with at least one letter and number',NULL,1),
	(70,'en_US','','auth','resetpassword_new_mismatch','error','The two entries for your NEW password are NOT identical',NULL,1),
	(71,'en_US','','auth','resetpassword_noauthcode','error','No authorization code provided',NULL,1),
	(72,'en_US','','auth','resetpassword_password1','label','New Password',NULL,1),
	(73,'en_US','','auth','resetpassword_password2','label','Confirm New Password',NULL,1),
	(74,'en_US','','auth','resetpassword_start_instructions','label','To reset your password you must first supply your WASP username below. You will then be sent an email to your confirmed email address with further instructions',NULL,1),
	(75,'en_US','','auth','resetpassword_submit','label','Submit',NULL,1),
	(76,'en_US','','auth','resetpassword_user','label','Username',NULL,1),
	(77,'en_US','','auth','resetpassword_username','error','A user with the supplied username does not exist',NULL,1),
	(78,'en_US','','auth','resetpassword_wronguser','error','Username and authorization code provided do not match',NULL,1),
	(79,'en_US','','createLibrary','cancel','label','Cancel',NULL,1),
	(80,'en_US','','createLibrary','libraryDetails','label','Library Details',NULL,1),
	(81,'en_US','','createLibrary','libraryName','label','Library Name',NULL,1),
	(82,'en_US','','createLibrary','librarySubtype','label','Library Subtype',NULL,1),
	(83,'en_US','','createLibrary','libraryType','label','Library Type',NULL,1),
	(84,'en_US','','createLibrary','primarySampleName','label','Primary Sample Name',NULL,1),
	(85,'en_US','','createLibrary','primarySampleSpecies','label','Species',NULL,1),
	(86,'en_US','','createLibrary','primarySampleType','label','Primary Sample Type',NULL,1),
	(87,'en_US','','createLibrary','save','label','Save',NULL,1),
	(88,'en_US','','createLibrary','selectNewAdaptorSet','label','--SELECT NEW ADAPTOR SET--',NULL,1),
	(89,'en_US','','dapendingtask','approve','label','APPROVE',NULL,1),
	(90,'en_US','','dapendingtask','department','label','Department',NULL,1),
	(91,'en_US','','dapendingtask','email','label','Email',NULL,1),
	(92,'en_US','','dapendingtask','jobID','label','Job ID',NULL,1),
	(93,'en_US','','dapendingtask','jobName','label','Job Name',NULL,1),
	(94,'en_US','','dapendingtask','newPI','label','New PI',NULL,1),
	(95,'en_US','','dapendingtask','pi','label','PI',NULL,1),
	(96,'en_US','','dapendingtask','reject','label','REJECT',NULL,1),
	(97,'en_US','','dapendingtask','samples','label','Samples',NULL,1),
	(98,'en_US','','dapendingtask','submitter','label','Submitter',NULL,1),
	(99,'en_US','','dapendingtask','subtitle1','label','Pending Principal Investigators',NULL,1),
	(100,'en_US','','dapendingtask','subtitle1_none','label','No Pending Principal Investigators',NULL,1),
	(101,'en_US','','dapendingtask','subtitle2','label','Pending Jobs',NULL,1),
	(102,'en_US','','dapendingtask','subtitle2_none','label','No Pending Jobs',NULL,1),
	(103,'en_US','','dapendingtask','title','label','Department Administrator Pending Tasks',NULL,1),
	(104,'en_US','','dapendingtask','unknown','label','Unknown',NULL,1),
	(105,'en_US','','dapendingtask','workflow','label','Workflow',NULL,1),
	(106,'en_US','','dashboard','allPlatformUnits','label','All Platform Units',NULL,1),
	(107,'en_US','','dashboard','assignLibrariesToPU','label','Assign Libraries To Platform Unit',NULL,1),
	(108,'en_US','','dashboard','controlLibraries','label','Control Libraries',NULL,1),
	(109,'en_US','','dashboard','dashboard','label','Dashboard',NULL,1),
	(110,'en_US','','dashboard','deptAdmin','label','Dept Admin',NULL,1),
	(111,'en_US','','dashboard','deptManagement','label','Department Management',NULL,1),
	(112,'en_US','','dashboard','draftedJobs','label','Drafted Jobs',NULL,1),
	(113,'en_US','','dashboard','facilityUtils','label','Facility Utils',NULL,1),
	(114,'en_US','','dashboard','jobQuotes','label','Job Quotes',NULL,1),
	(115,'en_US','','dashboard','jobQuoting','label','Job Quoting',NULL,1),
	(116,'en_US','','dashboard','jobUtils','label','Job Utils',NULL,1),
	(117,'en_US','','dashboard','labDetails','label','Lab Details',NULL,1),
	(118,'en_US','','dashboard','labMembers','label','Lab Members',NULL,1),
	(119,'en_US','','dashboard','labUtils','label','Lab Utils',NULL,1),
	(120,'en_US','','dashboard','listAllJobQuotes','label','List All Job Quotes',NULL,1),
	(121,'en_US','','dashboard','listOfAllMachines','label','List Of All Machines',NULL,1),
	(122,'en_US','','dashboard','listOfAllRuns','label','List Of All Runs',NULL,1),
	(123,'en_US','','dashboard','listOfAllSamples','label','List Of All Samples',NULL,1),
	(124,'en_US','','dashboard','miscUtils','label','Misc Utils',NULL,1),
	(125,'en_US','','dashboard','myAccount','label','My Account',NULL,1),
	(126,'en_US','','dashboard','myPassword','label','My Password',NULL,1),
	(127,'en_US','','dashboard','myProfile','label','My Profile',NULL,1),
	(128,'en_US','','dashboard','myViewableJobs','label','My Viewable Jobs',NULL,1),
	(129,'en_US','','dashboard','newPlatformUnit','label','New Platform Unit',NULL,1),
	(130,'en_US','','dashboard','platformUnitUtils','label','Platform Unit Utils',NULL,1),
	(131,'en_US','','dashboard','refreshAuth','label','Refresh Auth',NULL,1),
	(132,'en_US','','dashboard','requestAccessNote','label','Note: requests subject to verification',NULL,1),
	(133,'en_US','','dashboard','requestAccessToLab','label','Request Access To A Lab',NULL,1),
	(134,'en_US','','dashboard','sampleReceiver','label','Sample Receiver',NULL,1),
	(135,'en_US','','dashboard','sampleUtils','label','Sample Utils',NULL,1),
	(136,'en_US','','dashboard','submitJob','label','Submit A Job',NULL,1),
	(137,'en_US','','dashboard','superuserUtils','label','Superuser Utils',NULL,1),
	(138,'en_US','','dashboard','systemUsers','label','System Users',NULL,1),
	(139,'en_US','','dashboard','tasks','label','Tasks',NULL,1),
	(140,'en_US','','dashboard','userManager','label','User Manager',NULL,1),
	(141,'en_US','','dashboard','userUtils','label','User Utils',NULL,1),
	(142,'en_US','','dashboard','viewAllJobs','label','View All Jobs',NULL,1),
	(143,'en_US','','dashboard','workflowUtils','label','Workflow Utils',NULL,1),
	(144,'en_US','','department','active','label','Active',NULL,1),
	(145,'en_US','','department','inactive','label','Inactive',NULL,1),
	(146,'en_US','','department','create','label','Create New Department',NULL,1),
	(147,'en_US','','department','create_instructions','label','A new department can be created by adding its name and an administrator in the form below. More administrators can be added later if desired.',NULL,1),
	(148,'en_US','','department','detail','label','Department',NULL,1),
	(149,'en_US','','department','detail_active','label','Active',NULL,1),
	(150,'en_US','','department','detail_adminAlreadyExists','error','Selected person is already an administrator for this department',NULL,1),
	(151,'en_US','','department','detail_administrators','label','Administrators',NULL,1),
	(152,'en_US','','department','detail_administrator_name','label','Administrator Name',NULL,1),
	(153,'en_US','','department','detail_createadmin','label','Create Administrator',NULL,1),
	(154,'en_US','','department','detail_email','label','Administrator Email',NULL,1),
	(155,'en_US','','department','detail_emailnotfound','error','Email not found',NULL,1),
	(156,'en_US','','department','detail_existingadmin','label','Current Administrators',NULL,1),
	(157,'en_US','','department','detail_formatting','error','Formatting Error',NULL,1),
	(158,'en_US','','department','detail_inactive','label','Inactive',NULL,1),
	(159,'en_US','','department','detail_invalidDept','error','Specified department does Not exist',NULL,1),
	(160,'en_US','','department','detail_labs','label','Labs',NULL,1),
	(161,'en_US','','department','detail_missinglogin','error','No new administrator username provided',NULL,1),
	(162,'en_US','','department','detail_missingparam','error','Administrator name is missing',NULL,1),
	(163,'en_US','','department','detail_ok','label','New Administrator Created',NULL,1),
	(164,'en_US','','department','detail_pendingjobs','label','Pending Jobs',NULL,1),
	(165,'en_US','','department','detail_pendinglabs','label','Pending Labs',NULL,1),
	(166,'en_US','','department','detail_remove','label','Remove',NULL,1),
	(167,'en_US','','department','detail_submit','label','Submit',NULL,1),
	(168,'en_US','','department','detail_update','label','Update Department',NULL,1),
	(169,'en_US','','department','detail_update_admin','label','Type in the name of an existing WASP user to make them an administrator of this department',NULL,1),
	(170,'en_US','','department','detail_update_instructions','label','Use the following form to change the status of a department between Active and Inactive',NULL,1),
	(171,'en_US','','department','detail_update_missingparam','error','Deaprtment name must be provided',NULL,1),
	(172,'en_US','','department','detail_update_ok','label','Department has been updated',NULL,1),
	(173,'en_US','','department','detail_usernotfound','error','User not found in database',NULL,1),
	(174,'en_US','','department','lab_list','label','List of Labs',NULL,1),
	(175,'en_US','','department','list','label','List &amp; Manage Departments',NULL,1),
	(176,'en_US','','department','list','data','Submit',NULL,1),
	(177,'en_US','','department','list_create','label','Create Department',NULL,1),
	(178,'en_US','','department','list_department','label','Department Name',NULL,1),
	(179,'en_US','','department','list_department_exists','error','Department already exists',NULL,1),
	(180,'en_US','','department','list_instructions','label','The current departments registered in the system is listed below. Please click on a department name to view / edit its details.',NULL,1),
	(181,'en_US','','department','list_invalid','error','Invalid department name',NULL,1),
	(182,'en_US','','department','list_missingparam','error','Please provide a department name',NULL,1),
	(183,'en_US','','department','list_ok','label','New department has been created',NULL,1),
	(184,'en_US','','extraJobDetails','machine','label','Machine',NULL,1),
	(185,'en_US','','extraJobDetails','readLength','label','Read Length',NULL,1),
	(186,'en_US','','extraJobDetails','readType','label','Read Type',NULL,1),
	(187,'en_US','','extraJobDetails','quote','label','Quote',NULL,1),
	(188,'en_US','','file','not_found','error','File not found',NULL,1),
	(189,'en_US','','fmpayment','amount','label','Amount',NULL,1),
	(190,'en_US','','fmpayment','amount','constraint','NotEmpty',NULL,1),
	(191,'en_US','','fmpayment','amount','metaposition','10',NULL,1),
	(192,'en_US','','fmpayment','amount','error','AmounT cannot be Empty',NULL,1),
	(193,'en_US','','fmpayment','comment','label','Comment',NULL,1),
	(194,'en_US','','fmpayment','comment','constraint','NotEmpty',NULL,1),
	(195,'en_US','','fmpayment','comment','metaposition','20',NULL,1),
	(196,'en_US','','fmpayment','comment','error','Comment cannot be Empty',NULL,1),
	(197,'en_US','','fmpayment','name','label','Receive Payment for Jobs',NULL,1),
	(198,'en_US','','grid','dateFormat','label','Required date format: MM/DD/YYYY. It is best to use calendar to select date.',NULL,1),
	(199,'en_US','','grid','icon_new','label','New',NULL,1),
	(200,'en_US','','grid','icon_reload','label','Reload Grid',NULL,1),
	(201,'en_US','','grid','icon_search','label','Search',NULL,1),
	(202,'en_US','','grid','jobIdFormat','label','Required jobId format: all digits',NULL,1),
	(203,'en_US','','grid','lanesInteger','label','Cells must be an integer',NULL,1),
	(204,'en_US','','grid','piFormat','label','Required PI format: firstname lastname (login). It is best to select name from list.',NULL,1),
	(205,'en_US','','grid','readLengthInteger','label','ReadLength must be an integer',NULL,1),
	(206,'en_US','','grid','submitterFormat','label','Required Submitter format: firstname lastname (login). It is best to select name from list.',NULL,1),
	(207,'en_US','','job','amount','label','Quote Amount',NULL,1),
	(208,'en_US','','job','approval','approved','Job has been approved',NULL,1),
	(209,'en_US','','job','approval','rejected','Job has been rejected',NULL,1),
	(210,'en_US','','job','approval','error','Error - Update Failed',NULL,1),
	(211,'en_US','','jobComment','addNewJobComment','label','Add New Job Comment',NULL,1),
	(212,'en_US','','jobComment','alert','label','Please provide a comment',NULL,1),
	(213,'en_US','','jobComment','facilityComment','label','Facility-Generated Comments',NULL,1),
	(214,'en_US','','jobComment','job','error','Unable to find job in database',NULL,1),
	(215,'en_US','','jobComment','jobCommentEmpty','error','Comment may not be empty',NULL,1),
	(216,'en_US','','jobComment','jobCommentAdded','label','Comment added to job comments list',NULL,1),
	(217,'en_US','','jobComment','jobCommentAuth','error','Unable to properly authorize user',NULL,1),
	(218,'en_US','','jobComment','jobCommentCreat','error','Unexpectedly unable to create comment',NULL,1),
	(219,'en_US','','jobComment','jobCommentDate','label','Date',NULL,1),
	(220,'en_US','','jobComment','submitNewComment','label','Submit New Comment',NULL,1),
	(221,'en_US','','jobComment','jobSubmitterComment','label','Job-Submitter Comment',NULL,1),
	(222,'en_US','','jobMeta','base','label','Base',NULL,1),
	(223,'en_US','','jobMeta','resource','label','Resource',NULL,1),
	(224,'en_US','','jobMeta','software','label','Software',NULL,1),
	(225,'en_US','','job','createts','label','Submitted On',NULL,1),
	(226,'en_US','','job','detail_addJobViewer','label','Add Job Viewer',NULL,1),
	(227,'en_US','','job','detail_files','label','Files',NULL,1),
	(228,'en_US','','job','detail_jobViewer','label','Job Viewer',NULL,1),
	(229,'en_US','','job','detail_lab','label','Lab',NULL,1),
	(230,'en_US','','job','detail_loginName','label','Login Name',NULL,1),
	(231,'en_US','','job','detail_remove','label','Remove',NULL,1),
	(232,'en_US','','job','detail_samples','label','Samples',NULL,1),
	(233,'en_US','','job','detail_submittingUser','label','Submitting User',NULL,1),
	(234,'en_US','','job','jobId','label','JobID',NULL,1),
	(235,'en_US','','job','jobViewerUserRoleAdd','error1','JobId or LabId Error',NULL,1),
	(236,'en_US','','job','jobViewerUserRoleAdd','error2','Login Not Found',NULL,1),
	(237,'en_US','','job','jobViewerUserRoleAdd','error3','User is submitter, thus already has access to this job',NULL,1),
	(238,'en_US','','job','jobViewerUserRoleAdd','error4','User already has access to this job',NULL,1),
	(239,'en_US','','job','job_list','label','List of Jobs',NULL,1),
	(240,'en_US','','job','lab','label','Lab',NULL,1),
	(241,'en_US','','job','name','label','Job Name',NULL,1),
	(242,'en_US','','job','name','constraint','NotEmpty',NULL,1),
	(243,'en_US','','job','name','error','Name cannot be empty',NULL,1),
	(244,'en_US','','job','pi','label','PI',NULL,1),
	(245,'en_US','','job','submitter','label','Submitter',NULL,1),
	(246,'en_US','','job','UserId','label','Submitter',NULL,1),
	(247,'en_US','','job','viewfiles','label','Result',NULL,1),
	(248,'en_US','','job2quote','job2quote_list','label','List of Quotes',NULL,1),
	(249,'en_US','','job2quote','job2quote_note','label','Sort By Quote Amount To Easily Identify Jobs Requiring A Quote',NULL,1),
	(250,'en_US','','jobdetail_for_import','files','label','Submitted Files',NULL,1),
	(251,'en_US','','jobdetail_for_import','jobComments','label','Job Comments',NULL,1),
	(252,'en_US','','jobdetail_for_import','jobCommentsView','label','View',NULL,1),
	(253,'en_US','','jobdetail_for_import','jobCommentsPlusAddEdit','label','-Add-Edit',NULL,1),
	(254,'en_US','','jobdetail_for_import','jobId','label','Job ID',NULL,1),
	(255,'en_US','','jobdetail_for_import','jobName','label','Job Name',NULL,1),
	(256,'en_US','','jobdetail_for_import','jobPI','label','PI',NULL,1),
	(257,'en_US','','jobdetail_for_import','jobSubmissionDate','label','Submission Date',NULL,1),
	(258,'en_US','','jobdetail_for_import','jobSubmitter','label','Submitter',NULL,1),
	(259,'en_US','','jobdetail_for_import','jobWorkflow','label','Workflow',NULL,1),
	(260,'en_US','','jobdetail_for_import','Machine','label','Machine',NULL,1),
	(261,'en_US','','jobdetail_for_import','Read_Length','label','Read Length',NULL,1),
	(262,'en_US','','jobdetail_for_import','Read_Type','label','Read Type',NULL,1),
	(263,'en_US','','jobdetail_for_import','PI_Approval','label','PI Approval',NULL,1),
	(264,'en_US','','jobdetail_for_import','DA_Approval','label','Departmental Approval',NULL,1),
	(265,'en_US','','jobdetail_for_import','Quote_Job_Price','label','Anticipated Cost',NULL,1),
	(266,'en_US','','jobDraft','cancel','label','Cancel',NULL,1),
	(267,'en_US','','jobDraft','cell','label','Cell',NULL,1),
	(268,'en_US','','jobDraft','cell_error','label','Each sample must be placed on at least one cell.',NULL,1),
	(269,'en_US','','jobDraft','cell_adaptor_error','label','Unexpected error with library barcode.',NULL,1),
	(270,'en_US','','jobDraft','cell_barcode_error','label','Libraries containing identical barcodes may NOT reside on the same cell.',NULL,1),
	(271,'en_US','','jobDraft','cell_barcode_NONE_error','label','Libraries with the NONE barcode sequence (Index 0) CANNOT be multiplexed with other samples.',NULL,1),
	(272,'en_US','','jobDraft','cell_instructions','label','Please choose the number of sequencing cells you wish to order and then indicate which samples you wish to combine (multiplex) in the same cell',NULL,1),
	(273,'en_US','','jobDraft','cell_unexpected_error','label','Unexpected error determining number of cells selected.',NULL,1),
	(274,'en_US','','jobDraft','changeResource','error','You must select a job resource',NULL,1),
	(275,'en_US','','jobDraft','changeSoftwareResource','error','You must select a software resource',NULL,1),
	(276,'en_US','','jobDraft','clone','error','Unable to clone sample draft',NULL,1),
	(277,'en_US','','jobDraft','commentCreate','error','Unexpectedly unable to record your comment',NULL,1),
	(278,'en_US','','jobDraft','commentFetch','error','Unexpectedly unable to fetch your comment',NULL,1),
	(279,'en_US','','jobDraft','comment_instructions','label','If you wish, you may provide comments below that may be useful for this job submission. ',NULL,1),
	(280,'en_US','','jobDraft','comment_optional','label','Comments (optional) ',NULL,1),
	(281,'en_US','','jobDraft','create','label','Create A Job',NULL,1),
	(282,'en_US','','jobDraft','createJobFromJobDraft','error','Unable to create a Job from current Job Draft',NULL,1),
	(283,'en_US','','jobDraft','create_instructions','label','To create a job, please provide a name, select the lab from which you are submitting and choose the assay workflow most suited to your experiment.',NULL,1),
	(284,'en_US','','jobDraft','edit','label','Edit',NULL,1),
	(285,'en_US','','jobDraft','form','error','Please address errors on this page',NULL,1),
	(286,'en_US','','jobDraft','info_job','label','Job',NULL,1),
	(287,'en_US','','jobDraft','info_lab','label','Lab',NULL,1),
	(288,'en_US','','jobDraft','info_workflow','label','Workflow',NULL,1),
	(289,'en_US','','jobDraft','jobDraft_list','label','List of Job Drafts (Click Job Name to Continue on Submission)',NULL,1),
	(290,'en_US','','jobDraft','jobDraft_null','error','This jobdraft identifier does not return a valid job draft',NULL,1),
	(291,'en_US','','jobDraft','lab','label','Lab',NULL,1),
	(292,'en_US','','jobDraft','labId','label','Lab',NULL,1),
	(293,'en_US','','jobDraft','labId','error','Lab Must Not Be Empty',NULL,1),
	(294,'en_US','','jobDraft','last_modify_date','label','Last Modified On',NULL,1),
	(295,'en_US','','jobDraft','last_modify_user','label','Last Modified By',NULL,1),
	(296,'en_US','','jobDraft','libIndex','label','Lib. Index',NULL,1),
	(297,'en_US','','jobDraft','meta_instructions','label','Please provide the metadata requested below.',NULL,1),
	(298,'en_US','','jobDraft','name','error','You must provide a name for this job',NULL,1),
	(299,'en_US','','jobDraft','name','label','Job Name',NULL,1),
	(300,'en_US','','jobDraft','name_exists','error','Job name chosen already exists',NULL,1),
	(301,'en_US','','jobDraft','next','label','Next',NULL,1),
	(302,'en_US','','jobDraft','noSamples','error','You must create entries for at least one sample',NULL,1),
	(303,'en_US','','jobDraft','not_pending','error','This jobdraft has already been submitted',NULL,1),
	(304,'en_US','','jobDraft','no_draft_samples','label','No draft samples to display',NULL,1),
	(305,'en_US','','jobDraft','no_file','label','None Uploaded',NULL,1),
	(306,'en_US','','jobDraft','no_lab','error','You are not registered as a lab user but must be one to submit a job',NULL,1),
	(307,'en_US','','jobDraft','no_resources','error','No resources of the requested type have been assigned to the current workflow',NULL,1),
	(308,'en_US','','jobDraft','no_workflows','error','There are currently no active workflows to create a job for.',NULL,1),
	(309,'en_US','','jobDraft','numberofcells','label','Select number of sequencing cells required',NULL,1),
	(310,'en_US','','jobDraft','page_footer','label','',NULL,1),
	(311,'en_US','','jobDraft','remove_confirm','label','Are you sure you wish to remove this sample?',NULL,1),
	(312,'en_US','','jobDraft','resourceCategories_not_configured','error','Resource options for this workflow have not been properly configured',NULL,1),
	(313,'en_US','','jobDraft','resource_instructions','label','Please select from the platforms available for this assay workflow. If there are options available for the platform and assay workflow, you will be prompted to choose those applicable to your experimental design.',NULL,1),
	(314,'en_US','','jobDraft','sample','label','Sample',NULL,1),
	(315,'en_US','','jobDraft','sampleDraft_null','label','No sample draft matches supplied id',NULL,1),
	(316,'en_US','','jobDraft','sampleSubtype_null','label','No sample subtype matches supplied id',NULL,1),
	(317,'en_US','','jobDraft','sample_action','label','Action',NULL,1),
	(318,'en_US','','jobDraft','sample_add_existing','label','Existing',NULL,1),
	(319,'en_US','','jobDraft','sample_add_heading','label','Add a new Sample / Library',NULL,1),
	(320,'en_US','','jobDraft','sample_class','label','Class',NULL,1),
	(321,'en_US','','jobDraft','sample_clone','label','Clone',NULL,1),
	(322,'en_US','','jobDraft','sample_clone_heading','label','Clone Sample / Library',NULL,1),
	(323,'en_US','','jobDraft','sample_edit','label','Edit',NULL,1),
	(324,'en_US','','jobDraft','sample_edit_heading','label','Edit Sample / Library',NULL,1),
	(325,'en_US','','jobDraft','sample_file','label','File',NULL,1),
	(326,'en_US','','jobDraft','sample_instructions','label','Please add details for each of your samples. Choose from the sample sub-types available by clicking the appropriate link on the bottom bar of the table below. You can enter information about new samples or select from previously submitted samples.',NULL,1),
	(327,'en_US','','jobDraft','sample_name','label','Sample Name',NULL,1),
	(328,'en_US','','jobDraft','sample_number','label','Sample #',NULL,1),
	(329,'en_US','','jobDraft','sample_remove','label','Remove',NULL,1),
	(330,'en_US','','jobDraft','sample_ro_heading','label','Sample Draft View',NULL,1),
	(331,'en_US','','jobDraft','sample_subtype','label','Sample Subtype',NULL,1),
	(332,'en_US','','jobDraft','sample_subtype_null','error','This sample subtype identifier does not return a valid sample subtype',NULL,1),
	(333,'en_US','','jobDraft','sample_type','label','Sample Type',NULL,1),
	(334,'en_US','','jobDraft','sample_view','label','View',NULL,1),
	(335,'en_US','','jobDraft','software_instructions','label','Please select from the software options available for this assay workflow. If there are settable parameters, you will be prompted to choose those applicable to your experimental design or you may leave the pre-populated default values.',NULL,1),
	(336,'en_US','','jobDraft','software_not_configured','error','Software options for this workflow have not been properly configured',NULL,1),
	(337,'en_US','','jobDraft','submission_complete','label','Thank you for your submission. You may track the progress of submitted jobs and review data after job completion by navigating to the \"Job Utils\" tab on the dashboard',NULL,1),
	(338,'en_US','','jobDraft','submission_failed','label','Submission of this job unexpectedly failed. Please try again or contact a WASP administrator.',NULL,1),
	(339,'en_US','','jobDraft','submit','label','Save Changes',NULL,1),
	(340,'en_US','','jobDraft','submit_button','label','Submit Job',NULL,1),
	(341,'en_US','','jobDraft','submit_later_button','label','Submit Later',NULL,1),
	(342,'en_US','','jobDraft','submit_retry_button','label','Retry',NULL,1),
	(343,'en_US','','jobDraft','submitter','label','Submitted By',NULL,1),
	(344,'en_US','','jobDraft','subtype_select','label','Add samples of subtype',NULL,1),
	(345,'en_US','','jobDraft','upload_file','error','File Upload failed',NULL,1),
	(346,'en_US','','jobDraft','upload_file_description','label','If you wish you may upload one or more files, e.g. quality control data, to associate with this sample submission.',NULL,1),
	(347,'en_US','','jobDraft','upload_file_heading','label','Upload Files',NULL,1),
	(348,'en_US','','jobDraft','user_incorrect','error','You are not authorized to edit this jobdraft',NULL,1),
	(349,'en_US','','jobDraft','verify_instructions','label','Please review your submission by selecting the links immediately above. Once satisfied with your entries, you may submit this request by clicking the Submit Job button.',NULL,1),
	(350,'en_US','','jobDraft','workflowId','label','Assay Workflow',NULL,1),
	(351,'en_US','','jobDraft','workflowId','error','You must select an Assay Workflow',NULL,1),
	(352,'en_US','','jobListAssignLibrary','tableHeader','label','Active Jobs With Libraries To Be Run',NULL,1),
	(353,'en_US','','jobListAssignLibrary','jobId','label','Job',NULL,1),
	(354,'en_US','','jobListAssignLibrary','jobName','label','Job Name',NULL,1),
	(355,'en_US','','jobListAssignLibrary','none','label','None',NULL,1),
	(356,'en_US','','jobListAssignLibrary','submitter','label','Submitter',NULL,1),
	(357,'en_US','','jobListAssignLibrary','view','label','view',NULL,1),
	(358,'en_US','','jobListAssignLibrary','pi','label','PI',NULL,1),
	(359,'en_US','','jobListCreateLibrary','noneNeeded','label','No jobs with libraries to be created',NULL,1),
	(360,'en_US','','jobListCreateLibrary','jobId','label','Job',NULL,1),
	(361,'en_US','','jobListCreateLibrary','jobName','label','Job Name',NULL,1),
	(362,'en_US','','jobListCreateLibrary','submitter','label','Submitter',NULL,1),
	(363,'en_US','','jobListCreateLibrary','pi','label','PI',NULL,1),
	(364,'en_US','','lab','adduser','label','Add New LabUser',NULL,1),
	(365,'en_US','','lab','billing_address','label','Billing Address',NULL,1),
	(366,'en_US','','lab','billing_address','error','Billing Address cannot be empty',NULL,1),
	(367,'en_US','','lab','billing_address','constraint','NotEmpty',NULL,1),
	(368,'en_US','','lab','billing_address','metaposition','80',NULL,1),
	(369,'en_US','','lab','billing_building_room','label','Room',NULL,1),
	(370,'en_US','','lab','billing_building_room','metaposition','70',NULL,1),
	(371,'en_US','','lab','billing_city','label','Billing City',NULL,1),
	(372,'en_US','','lab','billing_city','error','Billing City cannot be empty',NULL,1),
	(373,'en_US','','lab','billing_city','constraint','NotEmpty',NULL,1),
	(374,'en_US','','lab','billing_city','metaposition','90',NULL,1),
	(375,'en_US','','lab','billing_contact','label','Billing Contact',NULL,1),
	(376,'en_US','','lab','billing_contact','error','Billing Contact cannot be empty',NULL,1),
	(377,'en_US','','lab','billing_contact','constraint','NotEmpty',NULL,1),
	(378,'en_US','','lab','billing_contact','metaposition','40',NULL,1),
	(379,'en_US','','lab','billing_country','label','Billing Country',NULL,1),
	(380,'en_US','','lab','billing_country','error','Billing Country cannot be empty',NULL,1),
	(381,'en_US','','lab','billing_country','control','select:${countries}:code:name',NULL,1),
	(382,'en_US','','lab','billing_country','constraint','NotEmpty',NULL,1),
	(383,'en_US','','lab','billing_country','metaposition','110',NULL,1),
	(384,'en_US','','lab','billing_departmentId','error','Billing Department cannot be empty',NULL,1),
	(385,'en_US','','lab','billing_departmentId','label','Billing Department',NULL,1),
	(386,'en_US','','lab','billing_departmentId','constraint','NotEmpty',NULL,1),
	(387,'en_US','','lab','billing_departmentId','metaposition','60',NULL,1),
	(388,'en_US','','lab','billing_departmentId','control','select:${departments}:departmentId:name',NULL,1),
	(389,'en_US','','lab','billing_institution','label','Billing Institution',NULL,1),
	(390,'en_US','','lab','billing_institution','error','Institution cannot be empty',NULL,1),
	(391,'en_US','','lab','billing_institution','constraint','NotEmpty',NULL,1),
	(392,'en_US','','lab','billing_institution','metaposition','50',NULL,1),
	(393,'en_US','','lab','billing_phone','label','Billing Phone',NULL,1),
	(394,'en_US','','lab','billing_phone','error','Billing Phone cannot be empty',NULL,1),
	(395,'en_US','','lab','billing_phone','constraint','NotEmpty',NULL,1),
	(396,'en_US','','lab','billing_phone','metaposition','130',NULL,1),
	(397,'en_US','','lab','billing_state','label','Billing State',NULL,1),
	(398,'en_US','','lab','billing_state','control','select:${states}:code:name',NULL,1),
	(399,'en_US','','lab','billing_state','metaposition','100',NULL,1),
	(400,'en_US','','lab','billing_zip','label','Billing Zip',NULL,1),
	(401,'en_US','','lab','billing_zip','error','Billing Zip cannot be empty',NULL,1),
	(402,'en_US','','lab','billing_zip','constraint','NotEmpty',NULL,1),
	(403,'en_US','','lab','billing_zip','metaposition','120',NULL,1),
	(404,'en_US','','lab','building_room','label','Room',NULL,1),
	(405,'en_US','','lab','building_room','metaposition','30',NULL,1),
	(406,'en_US','','lab','created','error','Lab was NOT created. Please fill in required fields.',NULL,1),
	(407,'en_US','','lab','created_success','label','Lab was created',NULL,1),
	(408,'en_US','','lab','departmentId','label','Department',NULL,1),
	(409,'en_US','','lab','departmentId','error','Please select department',NULL,1),
	(410,'en_US','','lab','detail','label','Lab Details',NULL,1),
	(411,'en_US','','lab','internal_external_lab','label','External/Internal',NULL,1),
	(412,'en_US','','lab','internal_external_lab','error','Please specify lab type (External/Internal)',NULL,1),
	(413,'en_US','','lab','internal_external_lab','control','select:external:External;internal:Internal',NULL,1),
	(414,'en_US','','lab','internal_external_lab','constraint','NotEmpty',NULL,1),
	(415,'en_US','','lab','internal_external_lab','metaposition','10',NULL,1),
	(416,'en_US','','lab','isActive','label','Active',NULL,1),
	(417,'en_US','','lab','jobs','label','Jobs',NULL,1),
	(418,'en_US','','lab','lab_list','label','List of Labs',NULL,1),
	(419,'en_US','','lab','list_pi','label','Principal Investigator',NULL,1),
	(420,'en_US','','lab','list_users','label','Lab Members',NULL,1),
	(421,'en_US','','lab','name','label','Lab Name',NULL,1),
	(422,'en_US','','lab','name','error','Lab Name cannot be empty',NULL,1),
	(423,'en_US','','lab','newPassword','label','New Password',NULL,1),
	(424,'en_US','','lab','newPasswordConfirm','label','Confirm New Password',NULL,1),
	(425,'en_US','','lab','oldPassword','label','Old Password',NULL,1),
	(426,'en_US','','lab','phone','label','Phone',NULL,1),
	(427,'en_US','','lab','phone','error','Phone cannot be empty',NULL,1),
	(428,'en_US','','lab','phone','constraint','NotEmpty',NULL,1),
	(429,'en_US','','lab','phone','metaposition','20',NULL,1),
	(430,'en_US','','lab','primaryUser','label','Principal Investigator',NULL,1),
	(431,'en_US','','lab','primaryUserId','label','Principal Investigator',NULL,1),
	(432,'en_US','','lab','primaryUserId','error','Please select Principal Investigator',NULL,1),
	(433,'en_US','','lab','updated','error','Lab was NOT updated. Please fill in required fields.',NULL,1),
	(434,'en_US','','lab','updated_success','label','Lab was updated',NULL,1),
	(435,'en_US','','labDetail','cancel','label','Cancel',NULL,1),
	(436,'en_US','','labDetail','create_job','label','create job',NULL,1),
	(437,'en_US','','labDetail','edit','label','Edit',NULL,1),
	(438,'en_US','','labDetail','save','label','Save Changes',NULL,1),
	(439,'en_US','','labPending','action','error','Invalid action. Must be approve or reject only',NULL,1),
	(440,'en_US','','labPending','approve','label','Approve',NULL,1),
	(441,'en_US','','labPending','approved','label','New lab application successfully approved',NULL,1),
	(442,'en_US','','labPending','billing_address','label','Billing Address',NULL,1),
	(443,'en_US','','labPending','billing_address','error','Billing Address cannot be empty',NULL,1),
	(444,'en_US','','labPending','billing_address','constraint','NotEmpty',NULL,1),
	(445,'en_US','','labPending','billing_address','metaposition','80',NULL,1),
	(446,'en_US','','labPending','billing_building_room','label','Room',NULL,1),
	(447,'en_US','','labPending','billing_building_room','metaposition','70',NULL,1),
	(448,'en_US','','labPending','billing_city','label','Billing City',NULL,1),
	(449,'en_US','','labPending','billing_city','error','Billing City cannot be empty',NULL,1),
	(450,'en_US','','labPending','billing_city','constraint','NotEmpty',NULL,1),
	(451,'en_US','','labPending','billing_city','metaposition','90',NULL,1),
	(452,'en_US','','labPending','billing_contact','label','Billing Contact',NULL,1),
	(453,'en_US','','labPending','billing_contact','error','Billing Contact cannot be empty',NULL,1),
	(454,'en_US','','labPending','billing_contact','constraint','NotEmpty',NULL,1),
	(455,'en_US','','labPending','billing_contact','metaposition','40',NULL,1),
	(456,'en_US','','labPending','billing_country','label','Billing Country',NULL,1),
	(457,'en_US','','labPending','billing_country','error','Billing Country cannot be empty',NULL,1),
	(458,'en_US','','labPending','billing_country','control','select:${countries}:code:name',NULL,1),
	(459,'en_US','','labPending','billing_country','constraint','NotEmpty',NULL,1),
	(460,'en_US','','labPending','billing_country','metaposition','110',NULL,1),
	(461,'en_US','','labPending','billing_departmentId','error','Billing Department cannot be empty',NULL,1),
	(462,'en_US','','labPending','billing_departmentId','label','Billing Department',NULL,1),
	(463,'en_US','','labPending','billing_departmentId','constraint','NotEmpty',NULL,1),
	(464,'en_US','','labPending','billing_departmentId','metaposition','60',NULL,1),
	(465,'en_US','','labPending','billing_departmentId','control','select:${departments}:departmentId:name',NULL,1),
	(466,'en_US','','labPending','billing_institution','label','Billing Institution',NULL,1),
	(467,'en_US','','labPending','billing_institution','error','Institution cannot be empty',NULL,1),
	(468,'en_US','','labPending','billing_institution','constraint','NotEmpty',NULL,1),
	(469,'en_US','','labPending','billing_institution','metaposition','50',NULL,1),
	(470,'en_US','','labPending','billing_phone','label','Billing Phone',NULL,1),
	(471,'en_US','','labPending','billing_phone','error','Billing Phone cannot be empty',NULL,1),
	(472,'en_US','','labPending','billing_phone','constraint','NotEmpty',NULL,1),
	(473,'en_US','','labPending','billing_phone','metaposition','130',NULL,1),
	(474,'en_US','','labPending','billing_state','label','Billing State',NULL,1),
	(475,'en_US','','labPending','billing_state','control','select:${states}:code:name',NULL,1),
	(476,'en_US','','labPending','billing_state','metaposition','100',NULL,1),
	(477,'en_US','','labPending','billing_zip','label','Billing Zip',NULL,1),
	(478,'en_US','','labPending','billing_zip','error','Billing Zip cannot be empty',NULL,1),
	(479,'en_US','','labPending','billing_zip','constraint','NotEmpty',NULL,1),
	(480,'en_US','','labPending','billing_zip','metaposition','120',NULL,1),
	(481,'en_US','','labPending','building_room','label','Room',NULL,1),
	(482,'en_US','','labPending','building_room','metaposition','30',NULL,1),
	(483,'en_US','','labPending','cancel','label','Cancel',NULL,1),
	(484,'en_US','','labPending','could_not_create_lab','error','Failed to process new lab request',NULL,1),
	(485,'en_US','','labPending','created','error','Pending Lab was NOT created. Please fill in required fields.',NULL,1),
	(486,'en_US','','labPending','createNewLab','label','Create New Lab Request',NULL,1),
	(487,'en_US','','labPending','departmentId','label','Department',NULL,1),
	(488,'en_US','','labPending','departmentId','error','Please select department',NULL,1),
	(489,'en_US','','labPending','departmentid_mismatch','error','Deparment id mismatch with lab-pending id',NULL,1),
	(490,'en_US','','labPending','edit','label','Edit',NULL,1),
	(491,'en_US','','labPending','heading','label','Pending Lab Details:',NULL,1),
	(492,'en_US','','labPending','internal_external_lab','label','External/Internal',NULL,1),
	(493,'en_US','','labPending','internal_external_lab','error','Please specify lab type (External/Internal)',NULL,1),
	(494,'en_US','','labPending','internal_external_lab','control','select:external:External;internal:Internal',NULL,1),
	(495,'en_US','','labPending','internal_external_lab','constraint','NotEmpty',NULL,1),
	(496,'en_US','','labPending','internal_external_lab','metaposition','10',NULL,1),
	(497,'en_US','','labPending','labpendingid_notexist','error','Lab-pending id does not exist',NULL,1),
	(498,'en_US','','labPending','name','label','Lab Name',NULL,1),
	(499,'en_US','','labPending','name','error','Lab Name cannot be empty',NULL,1),
	(500,'en_US','','labPending','newLabSubmit','label','Submit',NULL,1),
	(501,'en_US','','labPending','newPiNote','label','Request subject to WASP administrator confirmation of principal investigator status.',NULL,1),
	(502,'en_US','','labPending','pendingLabDetails','label','Pending Lab Details',NULL,1),
	(503,'en_US','','labPending','phone','label','Phone',NULL,1),
	(504,'en_US','','labPending','phone','error','Phone cannot be empty',NULL,1),
	(505,'en_US','','labPending','phone','constraint','NotEmpty',NULL,1),
	(506,'en_US','','labPending','phone','metaposition','20',NULL,1),
	(507,'en_US','','labPending','primaryUserId','label','Primary User',NULL,1),
	(508,'en_US','','labPending','primaryUserId','error','Please select Primary User',NULL,1),
	(509,'en_US','','labPending','reject','label','Reject',NULL,1),
	(510,'en_US','','labPending','rejected','label','New lab application successfully rejected',NULL,1),
	(511,'en_US','','labPending','save','label','Save Changes',NULL,1),
	(512,'en_US','','labPending','status_mismatch','error','Status must be pending with lab-pending id',NULL,1),
	(513,'en_US','','labPending','status_not_pending','error','Pending lab is already approved or rejected',NULL,1),
	(514,'en_US','','labPending','updated','error','Pending Lab was NOT updated. Please fill in required fields.',NULL,1),
	(515,'en_US','','labPending','updated_success','label','Pending Lab updated successfully.',NULL,1),
	(516,'en_US','','labuser','actions','label','Actions',NULL,1),
	(517,'en_US','','labuser','active','label','Active',NULL,1),
	(518,'en_US','','labuser','current','label','Current Lab Members',NULL,1),
	(519,'en_US','','labuser','email','label','Email',NULL,1),
	(520,'en_US','','labuser','inactive','label','Inactive',NULL,1),
	(521,'en_US','','labuser','labUserNote','label','Request subject to principal investigator acceptance.',NULL,1),
	(522,'en_US','','labuser','labUserNotFoundInLab','error','Cannot locate specified lab user in specified lab',NULL,1),
	(523,'en_US','','labuser','loginName','label','Login Name',NULL,1),
	(524,'en_US','','labuser','name','label','Name',NULL,1),
	(525,'en_US','','labuser','request','label','Pending Lab Members',NULL,1),
	(526,'en_US','','labuser','request_alreadyaccess','error','You are currently a member of the requested lab.',NULL,1),
	(527,'en_US','','labuser','request_alreadypending','error','You are already a pending user for the requested lab.',NULL,1),
	(528,'en_US','','labuser','request_primaryuser','label','WASP Login Name of Primary Investigator',NULL,1),
	(529,'en_US','','labuser','request_primaryuser','error','Invalid Primary User',NULL,1),
	(530,'en_US','','labuser','request_submit','label','Request Access',NULL,1),
	(531,'en_US','','labuser','request_success','label','Your request for access has been submitted.',NULL,1),
	(532,'en_US','','labuser','role','label','Role',NULL,1),
	(533,'en_US','','labuser','role_change_request','label','Roles updated successfully',NULL,1),
	(534,'en_US','','labuser','status','label','Status',NULL,1),
	(535,'en_US','','labuser','status_activate','label','Activate',NULL,1),
	(536,'en_US','','labuser','status_deactivate','label','Deactivate',NULL,1),
	(537,'en_US','','labuser','status_demoteLU','label','DEMOTE to LU',NULL,1),
	(538,'en_US','','labuser','status_promoteLM','label','PROMOTE to LM',NULL,1),
	(539,'en_US','','libraryCreated','created_success','label','Library successfully created.',NULL,1),
	(540,'en_US','','libraryCreated','message_fail','error','Failure to send status message to wasp-daemon',NULL,1),
	(541,'en_US','','libraryCreated','sample_problem','error','Problem occurred with the preparing the library for saving',NULL,1),
	(542,'en_US','','librarydetail_ro','cancel','label','Cancel',NULL,1),
	(543,'en_US','','librarydetail_ro','edit','label','Edit',NULL,1),
	(544,'en_US','','librarydetail_ro','libraryDetails','label','Library Details',NULL,1),
	(545,'en_US','','librarydetail_ro','libraryName','label','Library Name',NULL,1),
	(546,'en_US','','librarydetail_ro','librarySampleType','label','Library Type',NULL,1),
	(547,'en_US','','librarydetail_rw','cancel','label','Cancel',NULL,1),
	(548,'en_US','','librarydetail_rw','libraryDetails','label','Library Details',NULL,1),
	(549,'en_US','','librarydetail_rw','libraryName','label','Library Name',NULL,1),
	(550,'en_US','','librarydetail_rw','librarySubtype','label','Library Subtype',NULL,1),
	(551,'en_US','','librarydetail_rw','libraryType','label','Library Type',NULL,1),
	(552,'en_US','','librarydetail_rw','save','label','Save',NULL,1),
	(553,'en_US','','librarydetail_rw','selectNewAdaptorSet','label','--SELECT NEW ADAPTOR SET--',NULL,1),
	(554,'en_US','','listJobSamples','adaptor','label','Adaptor',NULL,1),
	(555,'en_US','','listJobSamples','addLibraryToPlatformUnit','label','Add Library To Platform Unit',NULL,1),
	(556,'en_US','','listJobSamples','addNewViewer','label','Add New Viewer',NULL,1),
	(557,'en_US','','listJobSamples','alreadyIsViewerOfThisJob','label','Person is already a viewer for this job.',NULL,1),
	(558,'en_US','','listJobSamples','arrivalStatus','label','Arrival Status',NULL,1),
	(559,'en_US','','listJobSamples','cancel','label','Cancel',NULL,1),
	(560,'en_US','','listJobSamples','cell','label','Cell',NULL,1),
	(561,'en_US','','listJobSamples','createLibrary','label','Create Library',NULL,1),
	(562,'en_US','','listJobSamples','finalConcentrationPM','label','Final Concentration (pM)',NULL,1),
	(563,'en_US','','listJobSamples','hideJobFiles','label','Hide Files',NULL,1),
	(564,'en_US','','listJobSamples','hideUserRequestedCoverage','label','Hide User-Requested Coverage',NULL,1),
	(565,'en_US','','listJobSamples','index','label','Index',NULL,1),
	(566,'en_US','','listJobSamples','initialMacromolecules','label','Initial Macromolecules',NULL,1),
	(567,'en_US','','listJobSamples','illegalOperation','label','You may NOT perform this operation.',NULL,1),
	(568,'en_US','','listJobSamples','invalidFormatEmailAddress','label','Email format invalid.',NULL,1),
	(569,'en_US','','listJobSamples','jobNotFound','label','Job Not Found In Database',NULL,1),
	(570,'en_US','','listJobSamples','jobViewerAdded','label','New Job Viewer Added',NULL,1),
	(571,'en_US','','listJobSamples','jobViewerRemoved','label','Selected Viewer Has Been Removed',NULL,1),
	(572,'en_US','','listJobSamples','jobViewers','label','Job Viewers',NULL,1),
	(573,'en_US','','listJobSamples','libraries','label','Libraries',NULL,1),
	(574,'en_US','','listJobSamples','library','label','Library',NULL,1),
	(575,'en_US','','listJobSamples','libraryControl','label','Library (Control)',NULL,1),
	(576,'en_US','','listJobSamples','logSample','label','log sample',NULL,1),
	(577,'en_US','','listJobSamples','missingEmailAddress','label','Please provide an email address',NULL,1),
	(578,'en_US','','listJobSamples','name','label','Name',NULL,1),
	(579,'en_US','','listJobSamples','newViewerEmailAddress','label','Email Address Of Viewer',NULL,1),
	(580,'en_US','','listJobSamples','noLibrariesCreated','label','No Libraries Created',NULL,1),
	(581,'en_US','','listJobSamples','noPlatformUnitsAndRuns','label','No Platform Units / Runs',NULL,1),
	(582,'en_US','','listJobSamples','numValFinalConc_alert','label','Please provide a numeric value for Final Concentration in the cell (pM)',NULL,1),
	(583,'en_US','','listJobSamples','platformUnit','label','Platform Unit',NULL,1),
	(584,'en_US','','listJobSamples','platformUnitsAndRuns','label','Platform Units / Runs',NULL,1),
	(585,'en_US','','listJobSamples','piRemovalIllegal','label','Job PI may NOT be removed',NULL,1),
	(586,'en_US','','listJobSamples','qcStatus','label','QC Status',NULL,1),
	(587,'en_US','','listJobSamples','roleNotFound','label','Role Not Found. Unable To Proceed.',NULL,1),
	(588,'en_US','','listJobSamples','remove','label','remove',NULL,1),
	(589,'en_US','','listJobSamples','selectAdaptorSetForNewLibrary','label','--SELECT ADAPTOR SET FOR NEW LIBRARY--',NULL,1),
	(590,'en_US','','listJobSamples','selectPlatformUnitCell','label','--SELECT A PLATFORM UNIT CELL--',NULL,1),
	(591,'en_US','','listJobSamples','showJobFiles','label','Show Files',NULL,1),
	(592,'en_US','','listJobSamples','showUserRequestedCoverage','label','Show User-Requested Coverage',NULL,1),
	(593,'en_US','','listJobSamples','species','label','Species',NULL,1),
	(594,'en_US','','listJobSamples','submit','label','Submit',NULL,1),
	(595,'en_US','','listJobSamples','submitterRemovalIllegal','label','Job Submitter may NOT be removed',NULL,1),
	(596,'en_US','','listJobSamples','type','label','Type',NULL,1),
	(597,'en_US','','listJobSamples','userSubmittedLibraries','label','User-Submitted Libraries',NULL,1),
	(598,'en_US','','listJobSamples','userNotFound','label','User not found in database.',NULL,1),
	(599,'en_US','','listJobSamples','userNotFoundByEmailAddress','label','Email address not found in database.',NULL,1),
	(600,'en_US','','listJobSamples','userNotViewerOfThisJob','label','User is not a registered viewer of this job.',NULL,1),
	(601,'en_US','','listJobSamples','valFinalConc_alert','label','Please provide a value for Final Concentration in the cell (pM)',NULL,1),
	(602,'en_US','','listJobSamples','view','label','View',NULL,1),
	(603,'en_US','','listJobSamples','youMustSelectCell_alert','label','You must select a cell',NULL,1),
	(604,'en_US','','lmpendingtask','approve','label','APPROVE',NULL,1),
	(605,'en_US','','lmpendingtask','email','label','Email',NULL,1),
	(606,'en_US','','lmpendingtask','jobId','label','Job ID',NULL,1),
	(607,'en_US','','lmpendingtask','jobName','label','Job Name',NULL,1),
	(608,'en_US','','lmpendingtask','name','label','Name',NULL,1),
	(609,'en_US','','lmpendingtask','pi','label','PI',NULL,1),
	(610,'en_US','','lmpendingtask','reject','label','REJECT',NULL,1),
	(611,'en_US','','lmpendingtask','samples','label','Samples',NULL,1),
	(612,'en_US','','lmpendingtask','submitter','label','Submitter',NULL,1),
	(613,'en_US','','lmpendingtask','subtitle1','label','Pending Users',NULL,1),
	(614,'en_US','','lmpendingtask','subtitle1_none','label','No Pending Users',NULL,1),
	(615,'en_US','','lmpendingtask','subtitle2','label','Pending Jobs',NULL,1),
	(616,'en_US','','lmpendingtask','subtitle2_none','label','No Pending Jobs',NULL,1),
	(617,'en_US','','lmpendingtask','workflow','label','Workflow',NULL,1),
	(618,'en_US','','lmpendingtask','title','label','PI/Lab Manager Pending Tasks',NULL,1),
	(619,'en_US','','metadata','lengthMax','error','Length exceeds maximum permitted',NULL,1),
	(620,'en_US','','metadata','lengthMin','error','Length less than minimum permitted',NULL,1),
	(621,'en_US','','metadata','metaType','error','Value is not of expected type',NULL,1),
	(622,'en_US','','metadata','rangeMax','error','Value exceeds maximum permitted',NULL,1),
	(623,'en_US','','metadata','rangeMin','error','Value less than minimum permitted',NULL,1),
	(624,'en_US','','pageTitle','acctquote/list','label','List of Job Quotes',NULL,1),
	(625,'en_US','','pageTitle','auth/confirmemail/authcodeform','label','Confirm Email Address',NULL,1),
	(626,'en_US','','pageTitle','auth/confirmemail/confirmemailerror','label','Unexpected Email Error',NULL,1),
	(627,'en_US','','pageTitle','auth/confirmemail/emailchanged','label','Confirm New Email Address',NULL,1),
	(628,'en_US','','pageTitle','auth/confirmemail/requestEmailChange','label','Request to Change Email',NULL,1),
	(629,'en_US','','pageTitle','auth/confirmemail/useridchanged','label','Confirm New User Id',NULL,1),
	(630,'en_US','','pageTitle','auth/confirmemail/userloginandemailchanged','label','Confirm New Email Address',NULL,1),
	(631,'en_US','','pageTitle','auth/confirmemail/userloginchanged','label','User Login Changed',NULL,1),
	(632,'en_US','','pageTitle','auth/getEmailForDemoForm','label','Email for Demo',NULL,1),
	(633,'en_US','','pageTitle','auth/directusertologin','label','Proceed To Login',NULL,1),
	(634,'en_US','','pageTitle','auth/login','label','Login',NULL,1),
	(635,'en_US','','pageTitle','auth/newpi/created','label','Application Submitted',NULL,1),
	(636,'en_US','','pageTitle','auth/newpi/emailok','label','Email Confirmed',NULL,1),
	(637,'en_US','','pageTitle','auth/newpi/form','label','New Principal Investigator',NULL,1),
	(638,'en_US','','pageTitle','auth/newpi/institute','label','New Principal Investigator',NULL,1),
	(639,'en_US','','pageTitle','auth/newuser/created','label','Application Submitted',NULL,1),
	(640,'en_US','','pageTitle','auth/newuser/emailok','label','Email Confirmed',NULL,1),
	(641,'en_US','','pageTitle','auth/newuser/form','label','New User',NULL,1),
	(642,'en_US','','pageTitle','auth/resetpassword/authcodeform','label','Reset Password',NULL,1),
	(643,'en_US','','pageTitle','auth/resetpassword/email','label','Email Sent',NULL,1),
	(644,'en_US','','pageTitle','auth/resetpassword/form','label','Reset Password',NULL,1),
	(645,'en_US','','pageTitle','auth/resetpassword/ok','label','Password Was Reset',NULL,1),
	(646,'en_US','','pageTitle','auth/resetpassword/request','label','Reset Password Request',NULL,1),
	(647,'en_US','','pageTitle','dashboard','label','Dashboard',NULL,1),
	(648,'en_US','','pageTitle','department/dapendingtasks','label','Department Administrator Pending Tasks',NULL,1),
	(649,'en_US','','pageTitle','department/detail','label','Department Detail',NULL,1),
	(650,'en_US','','pageTitle','department/list','label','Department List',NULL,1),
	(651,'en_US','','pageTitle','facility/platformunit/assign','label','Assign Library To Platform Unit',NULL,1),
	(652,'en_US','','pageTitle','facility/platformunit/createUpdatePlatformUnit','label','Create/Update Platform Unit',NULL,1),
	(653,'en_US','','pageTitle','facility/platformunit/updatePlatformUnit','label','Update Platform Unit',NULL,1),
	(654,'en_US','','pageTitle','facility/platformunit/instance/list','label','Platform Unit Instance List',NULL,1),
	(655,'en_US','','pageTitle','facility/platformunit/limitPriorToAssign','label','Machine Type Assignment',NULL,1),
	(656,'en_US','','pageTitle','facility/platformunit/list','label','Platform Unit List',NULL,1),
	(657,'en_US','','pageTitle','facility/platformunit/showPlatformUnit','label','Platform Unit / Sequence Run',NULL,1),
	(658,'en_US','','pageTitle','job/comments','label','Job Comments',NULL,1),
	(659,'en_US','','pageTitle','job/list','label','Job List',NULL,1),
	(660,'en_US','','pageTitle','job/jobsAwaitingLibraryCreation/jobsAwaitingLibraryCreationList','label','Active Jobs With Libraries To Be Created',NULL,1),
	(661,'en_US','','pageTitle','job2quote/list','label','List of Jobs to Quote',NULL,1),
	(662,'en_US','','pageTitle','job2quote/list_all','label','Quotes',NULL,1),
	(663,'en_US','','pageTitle','jobsubmit/cell','label','Cells',NULL,1),
	(664,'en_US','','pageTitle','jobsubmit/create','label','New Job',NULL,1),
	(665,'en_US','','pageTitle','jobsubmit/comment','label','Add A Comment',NULL,1),
	(666,'en_US','','pageTitle','jobsubmit/list','label','List of Job Drafts',NULL,1),
	(667,'en_US','','pageTitle','jobsubmit/metaform','label','New Job',NULL,1),
	(668,'en_US','','pageTitle','jobsubmit/ok','label','Job Successfully Submitted',NULL,1),
	(669,'en_US','','pageTitle','jobsubmit/failed','label','Job Submission Failed',NULL,1),
	(670,'en_US','','pageTitle','jobsubmit/sample','label','Submit Samples',NULL,1),
	(671,'en_US','','pageTitle','jobsubmit/sample/sampledetail_ro','label','View Sample Draft',NULL,1),
	(672,'en_US','','pageTitle','jobsubmit/sample/sampledetail_rw','label','Edit Sample Draft',NULL,1),
	(673,'en_US','','pageTitle','jobsubmit/verify','label','Verify New Job',NULL,1),
	(674,'en_US','','pageTitle','lab/allpendinglmapproval/list','label','PI/Lab Manager Pending Tasks',NULL,1),
	(675,'en_US','','pageTitle','lab/detail_ro','label','Lab Detail',NULL,1),
	(676,'en_US','','pageTitle','lab/detail_rw','label','Update Lab Detail',NULL,1),
	(677,'en_US','','pageTitle','lab/list','label','Lab List',NULL,1),
	(678,'en_US','','pageTitle','lab/newrequest','label','Request Access to Lab',NULL,1),
	(679,'en_US','','pageTitle','lab/pending/detail_ro','label','Pending Lab Detail',NULL,1),
	(680,'en_US','','pageTitle','lab/pending/detail_rw','label','Update Pending Lab Detail',NULL,1),
	(681,'en_US','','pageTitle','lab/pendinglmapproval/list','label','PI/Lab Manager Pending Tasks',NULL,1),
	(682,'en_US','','pageTitle','lab/pendinguser/list','label','Pending Users',NULL,1),
	(683,'en_US','','pageTitle','lab/user_list','label','Lab Member List',NULL,1),
	(684,'en_US','','pageTitle','lab/user_manager','label','Lab User Manager',NULL,1),
	(685,'en_US','','pageTitle','resource/create','label','Create New Resource',NULL,1),
	(686,'en_US','','pageTitle','resource/detail_ro','label','Resource Detail',NULL,1),
	(687,'en_US','','pageTitle','resource/detail_rw','label','Update Resource Detail',NULL,1),
	(688,'en_US','','pageTitle','resource/list','label','Machine List',NULL,1),
	(689,'en_US','','pageTitle','run/detail','label','Run Detail',NULL,1),
	(690,'en_US','','pageTitle','run/list','label','Run List',NULL,1),
	(691,'en_US','','pageTitle','run/createUpdateRun','label','Create/Update Sequence Run',NULL,1),
	(692,'en_US','','pageTitle','sample/list','label','Sample Utilities',NULL,1),
	(693,'en_US','','pageTitle','sample/controlLibraries/list','label','Control Libraries',NULL,1),
	(694,'en_US','','pageTitle','sample/controlLibraries/createUpdate','label','Create/Update Control Libraries',NULL,1),
	(695,'en_US','','pageTitle','sampleDnaToLibrary/createLibrary','label','New Library',NULL,1),
	(696,'en_US','','pageTitle','sampleDnaToLibrary/librarydetail_ro','label','Library Details',NULL,1),
	(697,'en_US','','pageTitle','sampleDnaToLibrary/librarydetail_rw','label','Update Library',NULL,1),
	(698,'en_US','','pageTitle','sampleDnaToLibrary/listJobSamples','label','Samples And Libraries',NULL,1),
	(699,'en_US','','pageTitle','sampleDnaToLibrary/sampledetail_ro','label','Sample Details',NULL,1),
	(700,'en_US','','pageTitle','sampleDnaToLibrary/sampledetail_rw','label','Update Sample Details',NULL,1),
	(701,'en_US','','pageTitle','sysrole/list','label','System User Management',NULL,1),
	(702,'en_US','','pageTitle','task/assignLibraries/lists','label','Assign Libraries',NULL,1),
	(703,'en_US','','pageTitle','task/detail','label','Requote Pending Jobs',NULL,1),
	(704,'en_US','','pageTitle','task/fmpayment/list','label','Receive Payment for Jobs',NULL,1),
	(705,'en_US','','pageTitle','task/fmrequote/list','label','Requote Pending Jobs',NULL,1),
	(706,'en_US','','pageTitle','task/libraryqc/list','label','Library QC Manager',NULL,1),
	(707,'en_US','','pageTitle','task/samplereceive/list','label','Incoming Sample Manager',NULL,1),
	(708,'en_US','','pageTitle','task/sampleqc/list','label','Sample QC Manager',NULL,1),
	(709,'en_US','','pageTitle','uifield/list','label','Properties',NULL,1),
	(710,'en_US','','pageTitle','user/detail_ro','label','User Detail',NULL,1),
	(711,'en_US','','pageTitle','user/detail_rw','label','Update User Detail',NULL,1),
	(712,'en_US','','pageTitle','user/list','label','User List',NULL,1),
	(713,'en_US','','pageTitle','user/mypassword','label','Change Password',NULL,1),
	(714,'en_US','','pageTitle','workflow/list','label','Workflow List',NULL,1),
	(715,'en_US','','pageTitle','workflow/resource/configure','label','Workflow Resource Assignment',NULL,1),
	(716,'en_US','','pendingJob','detailRO_approve','label','Approve',NULL,1),
	(717,'en_US','','pendingJob','detailRO_job','label','Job',NULL,1),
	(718,'en_US','','pendingJob','detailRO_pi','label','PI',NULL,1),
	(719,'en_US','','pendingJob','detailRO_reject','label','Reject',NULL,1),
	(720,'en_US','','pendingJob','detailRO_submittingUser','label','Submitting User',NULL,1),
	(721,'en_US','','piPending','address','label','Address',NULL,1),
	(722,'en_US','','piPending','address','error','Address cannot be empty',NULL,1),
	(723,'en_US','','piPending','address','constraint','NotEmpty',NULL,1),
	(724,'en_US','','piPending','address','metaposition','40',NULL,1),
	(725,'en_US','','piPending','building_room','label','Building / Room',NULL,1),
	(726,'en_US','','piPending','building_room','metaposition','30',NULL,1),
	(727,'en_US','','piPending','captcha','error','Captcha text incorrect',NULL,1),
	(728,'en_US','','piPending','captcha','label','Captcha text',NULL,1),
	(729,'en_US','','piPending','city','label','City',NULL,1),
	(730,'en_US','','piPending','city','error','City cannot be empty',NULL,1),
	(731,'en_US','','piPending','city','constraint','NotEmpty',NULL,1),
	(732,'en_US','','piPending','city','metaposition','50',NULL,1),
	(733,'en_US','','piPending','country','label','Country',NULL,1),
	(734,'en_US','','piPending','country','error','Country cannot be empty',NULL,1),
	(735,'en_US','','piPending','country','control','select:${countries}:code:name',NULL,1),
	(736,'en_US','','piPending','country','constraint','NotEmpty',NULL,1),
	(737,'en_US','','piPending','country','metaposition','70',NULL,1),
	(738,'en_US','','piPending','departmentId','error','Department cannot be empty',NULL,1),
	(739,'en_US','','piPending','departmentId','label','Department',NULL,1),
	(740,'en_US','','piPending','departmentId','constraint','NotEmpty',NULL,1),
	(741,'en_US','','piPending','departmentId','control','select:${departments}:departmentId:name',NULL,1),
	(742,'en_US','','piPending','departmentId','metaposition','20',NULL,1),
	(743,'en_US','','piPending','email','label','Email',NULL,1),
	(744,'en_US','','piPending','email','error','Wrong email address format',NULL,1),
	(745,'en_US','','piPending','emailconfirmed','label','Your email address has been confirmed and your department administrator has been emailed to request confirmation of your eligibility to register your lab. Once your Department Administrator approves your request, you will receive an email informing you of this fact and you will be permitted to log in. You are advised to contact them to request they do this if your account does not become activated in good time.',NULL,1),
	(746,'en_US','','piPending','emailsent','label','Thank you for your account request. You have been sent an email with instructions as to how to confirm your email address. You will be unable to log in until you confirm your email address.',NULL,1),
	(747,'en_US','','piPending','email_exists','error','Email already exists in the database',NULL,1),
	(748,'en_US','','piPending','fax','label','Fax',NULL,1),
	(749,'en_US','','piPending','fax','metaposition','100',NULL,1),
	(750,'en_US','','piPending','firstName','label','First Name',NULL,1),
	(751,'en_US','','piPending','firstName','error','First Name cannot be empty',NULL,1),
	(752,'en_US','','piPending','form_instructions','label','Please fill out the form below, making sure to complete all required fields marked with an asterisk',NULL,1),
	(753,'en_US','','piPending','institute_multi_select','error','You cannot select more than one institute',NULL,1),
	(754,'en_US','','piPending','institute_not_selected','error','You must select an institute',NULL,1),
	(755,'en_US','','piPending','institution','label','Institution',NULL,1),
	(756,'en_US','','piPending','institution','error','Institution cannot be empty',NULL,1),
	(757,'en_US','','piPending','institution','constraint','NotEmpty',NULL,1),
	(758,'en_US','','piPending','institution','metaposition','10',NULL,1),
	(759,'en_US','','piPending','internal_institute_list','data','Einstein;Montifiore',NULL,1),
	(760,'en_US','','piPending','labName','label','Lab Name',NULL,1),
	(761,'en_US','','piPending','labName','error','Lab Name cannot be empty',NULL,1),
	(762,'en_US','','piPending','labName','constraint','NotEmpty',NULL,1),
	(763,'en_US','','piPending','labName','metaposition','1',NULL,1),
	(764,'en_US','','piPending','lastName','label','Last Name',NULL,1),
	(765,'en_US','','piPending','lastName','error','Last Name cannot be empty',NULL,1),
	(766,'en_US','','piPending','locale','label','Locale',NULL,1),
	(767,'en_US','','piPending','locale','constraint','NotEmpty',NULL,1),
	(768,'en_US','','piPending','locale','error','Locale cannot be empty',NULL,1),
	(769,'en_US','','piPending','login','label','Login',NULL,1),
	(770,'en_US','','piPending','login','error','Login cannot be empty',NULL,1),
	(771,'en_US','','piPending','password','label','Password',NULL,1),
	(772,'en_US','','piPending','password','error','Password cannot be empty',NULL,1),
	(773,'en_US','','piPending','password2','label','Re-confirm Password',NULL,1),
	(774,'en_US','','piPending','password2','error','Re-confirm password cannot be empty',NULL,1),
	(775,'en_US','','piPending','password_invalid','error','Password must be at least 8 characters, containing only letters and numbers, with at least one letter and number',NULL,1),
	(776,'en_US','','piPending','password_mismatch','error','The two entries for your password are NOT identical',NULL,1),
	(777,'en_US','','piPending','phone','label','Phone',NULL,1),
	(778,'en_US','','piPending','phone','error','Phone cannot be empty',NULL,1),
	(779,'en_US','','piPending','phone','constraint','NotEmpty',NULL,1),
	(780,'en_US','','piPending','phone','metaposition','90',NULL,1),
	(781,'en_US','','piPending','select_default','label','-- select --',NULL,1),
	(782,'en_US','','piPending','select_institute','label','Institute',NULL,1),
	(783,'en_US','','piPending','select_institute_message','label','Please select your institute or choose \"other\"',NULL,1),
	(784,'en_US','','piPending','select_institute_other','label','Other',NULL,1),
	(785,'en_US','','piPending','select_institute_submit','label','Submit',NULL,1),
	(786,'en_US','','piPending','specify_other_institute','label','If \"Other\" Please specify',NULL,1),
	(787,'en_US','','piPending','state','label','State',NULL,1),
	(788,'en_US','','piPending','state','control','select:${states}:code:name',NULL,1),
	(789,'en_US','','piPending','state','metaposition','60',NULL,1),
	(790,'en_US','','piPending','submit','label','Apply for Account',NULL,1),
	(791,'en_US','','piPending','title','label','Title',NULL,1),
	(792,'en_US','','piPending','title','error','Title cannot be empty',NULL,1),
	(793,'en_US','','piPending','title','constraint','NotEmpty',NULL,1),
	(794,'en_US','','piPending','title','control','select:Prof:Prof;Dr:Dr;Mr:Mr;Ms:Ms;Mrs:Mrs;Miss:Miss',NULL,1),
	(795,'en_US','','piPending','title','metaposition','5',NULL,1),
	(796,'en_US','','piPending','zip','label','Zip',NULL,1),
	(797,'en_US','','piPending','zip','error','Zip cannot be empty',NULL,1),
	(798,'en_US','','piPending','zip','constraint','NotEmpty',NULL,1),
	(799,'en_US','','piPending','zip','metaposition','80',NULL,1),
	(800,'en_US','','platformunit_assign','adaptor','label','Adaptor',NULL,1),
	(801,'en_US','','platformunit_assign','analysis','label','Analysis',NULL,1),
	(802,'en_US','','platformunit_assign','assignLibToRun','label','Assign Libraries For A Run On',NULL,1),
	(803,'en_US','','platformunit_assign','addToLane','label','[Add To Cell]',NULL,1),
	(804,'en_US','','platformunit_assign','close','label','[Close]',NULL,1),
	(805,'en_US','','platformunit_assign','finalConc','label','Final Conc.',NULL,1),
	(806,'en_US','','platformunit_assign','job','label','Job',NULL,1),
	(807,'en_US','','platformunit_assign','jobCell','label','Job Cell',NULL,1),
	(808,'en_US','','platformunit_assign','lane','label','Cell',NULL,1),
	(809,'en_US','','platformunit_assign','library','label','Library',NULL,1),
	(810,'en_US','','platformunit_assign','noLibWaitingFor','label','No Libraries Waiting For',NULL,1),
	(811,'en_US','','platformunit_assign','noPUWaitingFor','label','No Platform Units Waiting For',NULL,1),
	(812,'en_US','','platformunit_assign','platformUnit','label','Platform Unit',NULL,1),
	(813,'en_US','','platformunit_assign','removeFromLane','label','[Remove From Cell]',NULL,1),
	(814,'en_US','','platformunit_assign','sampleCell','label','Sample Cell',NULL,1),
	(815,'en_US','','platformunit_assign','showRequestedCoverage','label','[Show Requested Coverage]',NULL,1),
	(816,'en_US','','platformunit_assign','recievedStatus','label','Status',NULL,1),
	(817,'en_US','','platformunit_assign','qcStatus','label','QC Status',NULL,1),
	(818,'en_US','','platformunit_assign','theConcUnits','label','pM',NULL,1),
	(819,'en_US','','platformunit_assign','userSubmitted','label','User-submitted',NULL,1),
	(820,'en_US','','platformunit','adaptorBarcodeNotFound','error','Action Failed. Libary adaptor barcode NOT found',NULL,1),
	(821,'en_US','','platformunit','adaptorNotFound','error','Action Failed. Libary adaptor NOT found',NULL,1),
	(822,'en_US','','platformunit','barcode','label','Barcode',NULL,1),
	(823,'en_US','','platformunit','date','label','Created',NULL,1),
	(824,'en_US','','platformunit','notFoundOrNotCorrectType','error','Action Failed. Platform unit record not found or record is not a platform unit',NULL,1),
	(825,'en_US','','platformunit','flowcellNotFoundNotUnique','error','Action Failed. Platform Unit record not found or not unique',NULL,1),
	(826,'en_US','','platformunit','flowcellStateError','error','Action Failed. Platform Unit state not compatible with adding libraries',NULL,1),
	(827,'en_US','','platformunit','jobIdNotFound','error','Action Failed. Value for jobId is unexpectedly NOT valid',NULL,1),
	(828,'en_US','','platformunit','jobIdNotSelected','error','Select A Job',NULL,1),
	(829,'en_US','','platformunit','jobNotFound','error','Selected Job Not Found',NULL,1),
	(830,'en_US','','platformunit','jobResourceCategoryMismatch','error','Job and resource unexpectedly do not match',NULL,1),
	(831,'en_US','','platformunit','lanecount','label','Cells',NULL,1),
	(832,'en_US','','platformunit','laneIdNotFound','error','Action Failed. Value for cell is unexpectedly NOT valid',NULL,1),
	(833,'en_US','','platformunit','laneIsFlowCell','error','Action Failed. You mistakenly selected a Platform Unit',NULL,1),
	(834,'en_US','','platformunit','laneIsNotLane','error','Action Failed. Cell selected is unexpectedly NOT a Cell',NULL,1),
	(835,'en_US','','platformunit','libAdded','success','Update Complete. Library added to platform unit.',NULL,1),
	(836,'en_US','','platformunit','libJobClash','error','Action Failed. Libraries on a cell must be from a single job',NULL,1),
	(837,'en_US','','platformunit','libraryIdNotFound','error','Action Failed. Value for libraryId is unexpectedly NOT valid',NULL,1),
	(838,'en_US','','platformunit','libraryIsNotLibrary','error','Action Failed. Library selected is unexpectedly NOT a Library',NULL,1),
	(839,'en_US','','platformunit','libraryJobMismatch','error','Action Failed. Libary does NOT appear to be part of the specified Job',NULL,1),
	(840,'en_US','','platformunit','libraryRemoved','success','Selected Library Removed From Platform Unit',NULL,1),
	(841,'en_US','','platformunit','lock_status','error','Invalid lock status selected',NULL,1),
	(842,'en_US','','platformunit','multiplex','error','Action Failed. Barcode on the library is not compatible with other libraries on the cell',NULL,1),
	(843,'en_US','','platformunit','name','label','Name',NULL,1),
	(844,'en_US','','platformunit','platformunit_list','label','Platform Unit List',NULL,1),
	(845,'en_US','','platformunit','pmoleAddedInvalidValue','error','Action Failed. Value for pmol is NOT valid',NULL,1),
	(846,'en_US','','platformunit','readlength','label','Read Length',NULL,1),
	(847,'en_US','','platformunit','readType','label','Read Type',NULL,1),
	(848,'en_US','','platformunit','resourceCategoryName','label','Machine Type',NULL,1),
	(849,'en_US','','platformunit','resourceCategoryInvalidValue','error','Resource Not Found',NULL,1),
	(850,'en_US','','platformunit','parameter','error','Parameter error occurred',NULL,1),
	(851,'en_US','','platformunit','resourceTypeNotFound','error','Type Resource unexpectedly not found',NULL,1),
	(852,'en_US','','platformunit','sampleSourceNotExist','error','Action Failed: Selected Library Not Found',NULL,1),
	(853,'en_US','','platformunit','samplesourceTypeError','error','Action Failed: Selected samplesource missing cell or library',NULL,1),
	(854,'en_US','','platformunit','sampleType','error','Action Failed. Cell or Library type exception raised',NULL,1),
	(855,'en_US','','platformunit','sampleSubtypeName','label','Subtype',NULL,1),
	(856,'en_US','','platformunit','subtype_list','label','Subtype Sample List',NULL,1),
	(857,'en_US','','platformunit','taskNotFound','error','Task unexpectedly not found',NULL,1),
	(858,'en_US','','platformunit','TESTING','success','TESTING.',NULL,1),
	(859,'en_US','','platformunitById','name','label','Name',NULL,1),
	(860,'en_US','','platformunitById','platformunitbyid_list','label','Platform Unit List',NULL,1),
	(861,'en_US','','platformunitById','submitter','label','Submitter',NULL,1),
	(862,'en_US','','platformunitInstance','barcode','label','Barcode',NULL,1),
	(863,'en_US','','platformunitInstance','barcode','constraint','NotEmpty',NULL,1),
	(864,'en_US','','platformunitInstance','barcode','error','Barcode cannot be empty',NULL,1),
	(865,'en_US','','platformunitInstance','barcode_exists','error','Barcode already exists in the database',NULL,1),
	(866,'en_US','','platformunitInstance','cancel','label','Cancel',NULL,1),
	(867,'en_US','','platformunitInstance','comment','label','Comment',NULL,1),
	(868,'en_US','','platformunitInstance','comment','error','Comment',NULL,1),
	(869,'en_US','','platformunitInstance','comment','metaposition','60',NULL,1),
	(870,'en_US','','platformunitInstance','created','error','Platformunit was NOT created. Please fill in required fields.',NULL,1),
	(871,'en_US','','platformunitInstance','created_success','label','Platformunit created successfully.',NULL,1),
	(872,'en_US','','platformunitInstance','deleted_success','label','Platformunit deleted successfully.',NULL,1),
	(873,'en_US','','platformunitInstance','headerCreate','label','Create New Platform Unit',NULL,1),
	(874,'en_US','','platformunitInstance','headerUpdate','label','Update Platform Unit',NULL,1),
	(875,'en_US','','platformunitInstance','lanecount','label','Cell Count',NULL,1),
	(876,'en_US','','platformunitInstance','lanecount','constraint','NotEmpty',NULL,1),
	(877,'en_US','','platformunitInstance','lanecount','type','INTEGER',NULL,1),
	(878,'en_US','','platformunitInstance','lanecount','range','1:1000',NULL,1),
	(879,'en_US','','platformunitInstance','lanecount','error','Cell Count cannot be empty',NULL,1),
	(880,'en_US','','platformunitInstance','lanecount_notfound','error','Cell Count value unexpectedly cannot be determined',NULL,1),
	(881,'en_US','','platformunitInstance','lanecount_valueinvalid','error','Cell Count value not valid',NULL,1),
	(882,'en_US','','platformunitInstance','lanecount_valuealteredconflicting','error','Action not permitted at this time. To reduce the number of cells, remove libraries on the cells that will be lost.',NULL,1),
	(883,'en_US','','platformunitInstance','lanecount','control','select:${lanes}:valuePassedBack:valueVisible',NULL,1),
	(884,'en_US','','platformunitInstance','platUnitType','label','Select Platform Unit Type',NULL,1),
	(885,'en_US','','platformunitInstance','resourceCategoryName','label','Machine Type',NULL,1),
	(886,'en_US','','platformunitInstance','readType','label','Read Type',NULL,1),
	(887,'en_US','','platformunitInstance','readType','constraint','NotEmpty',NULL,1),
	(888,'en_US','','platformunitInstance','readType','error','Read Type cannot be empty',NULL,1),
	(889,'en_US','','platformunitInstance','readType','control','select:${readTypes}:valuePassedBack:valueVisible',NULL,1),
	(890,'en_US','','platformunitInstance','readType','metaposition','10',NULL,1),
	(891,'en_US','','platformunitInstance','readlength','label','Read Length',NULL,1),
	(892,'en_US','','platformunitInstance','readlength','constraint','NotEmpty',NULL,1),
	(893,'en_US','','platformunitInstance','readlength','error','Read Length cannot be empty',NULL,1),
	(894,'en_US','','platformunitInstance','readlength','control','select:${readlengths}:valuePassedBack:valueVisible',NULL,1),
	(895,'en_US','','platformunitInstance','readlength','metaposition','15',NULL,1),
	(896,'en_US','','platformunitInstance','lanecountForEditBox','label','Cell Count',NULL,1),
	(897,'en_US','','platformunitInstance','lanecount_empty','error','Please select a number of cells',NULL,1),
	(898,'en_US','','platformunitInstance','name','label','Name',NULL,1),
	(899,'en_US','','platformunitInstance','name','constraint','NotEmpty',NULL,1),
	(900,'en_US','','platformunitInstance','name','error','Name cannot be empty',NULL,1),
	(901,'en_US','','platformunitInstance','name_exists','error','Name already exists in the database',NULL,1),
	(902,'en_US','','platformunitInstance','numberOfLanesRequested','label','Cell Count',NULL,1),
	(903,'en_US','','platformunitInstance','numberOfLanesRequested','error','Cell Count cannot be empty',NULL,1),
	(904,'en_US','','platformunitInstance','numberOfLanesRequested_conflict','error','Action not permitted at this time. To reduce the number of cells, remove libraries on the cells that will be lost.',NULL,1),
	(905,'en_US','','platformunitInstance','platformunitinstance_list','label','Platform Unit Instance List',NULL,1),
	(906,'en_US','','platformunitInstance','reset','label','Reset',NULL,1),
	(907,'en_US','','platformunitInstance','submitter','label','Submitter',NULL,1),
	(908,'en_US','','platformunitInstance','submitter','constraint','NotEmpty',NULL,1),
	(909,'en_US','','platformunitInstance','submitter','error','Submitter cannot be empty',NULL,1),
	(910,'en_US','','platformunitInstance','subtype','label','Subtype',NULL,1),
	(911,'en_US','','platformunitInstance','subtype','constraint','NotEmpty',NULL,1),
	(912,'en_US','','platformunitInstance','subtype','error','Subtype cannot be empty',NULL,1),
	(913,'en_US','','platformunitInstance','updated','error','Platformunit was NOT updated. Please fill in required fields.',NULL,1),
	(914,'en_US','','platformunitInstance','updated_success','label','Platformunit updated successfully.',NULL,1),
	(915,'en_US','','platformunitInstance','submit','label','Submit',NULL,1),
	(916,'en_US','','platformunitShow','action','label','Action',NULL,1),
	(917,'en_US','','platformunitShow','addToRun','label','Add To Run',NULL,1),
	(918,'en_US','','platformunitShow','barcodeName','label','Barcode',NULL,1),
	(919,'en_US','','platformunitShow','comment','label','Comments',NULL,1),
	(920,'en_US','','platformunitShow','delete','label','Delete',NULL,1),
	(921,'en_US','','platformunitShow','deleteSmall','label','delete',NULL,1),
	(922,'en_US','','platformunitShow','edit','label','Edit',NULL,1),
	(923,'en_US','','platformunitShow','editSmall','label','edit',NULL,1),
	(924,'en_US','','platformunitShow','end','label','End',NULL,1),
	(925,'en_US','','platformunitShow','length','label','Length',NULL,1),
	(926,'en_US','','platformunitShow','machine','label','Machine',NULL,1),
	(927,'en_US','','platformunitShow','numberOfCellsOnThisPlatformUnit','label','Cell Count',NULL,1),
	(928,'en_US','','platformunitShow','readType','label','Read Type',NULL,1),
	(929,'en_US','','platformunitShow','readlength','label','Read Length',NULL,1),
	(930,'en_US','','showPlatformUnit','removeLibrary','label','Remove Library',NULL,1),
	(931,'en_US','','platformunitShow','run','label','Run',NULL,1),
	(932,'en_US','','platformunitShow','start','label','start',NULL,1),
	(933,'en_US','','platformunitShow','status','label','Status',NULL,1),
	(934,'en_US','','platformunitShow','type','label','Type',NULL,1),
	(935,'en_US','','platformunitShow','typeOfPlatformUnit','label','Type',NULL,1),
	(936,'en_US','','platformunitShow','wantToDeletePU','label','Do you really want to delete this platform unit record?',NULL,1),
	(937,'en_US','','platformunitShow','wantToDeleteRun','label','Do you really want to delete this run record?',NULL,1),
	(938,'en_US','','puLimitPriorToAssign','allAvailableJobs','label','All Available Jobs',NULL,1),
	(939,'en_US','','puLimitPriorToAssign','chooseJob','label','Choose A Job',NULL,1),
	(940,'en_US','','puLimitPriorToAssign','chooseMachine','label','Choose A Machine',NULL,1),
	(941,'en_US','','puLimitPriorToAssign','job','label','Job',NULL,1),
	(942,'en_US','','puLimitPriorToAssign','selectJob','label','Select A Job',NULL,1),
	(943,'en_US','','puLimitPriorToAssign','selectMachine','label','Select A Machine',NULL,1),
	(944,'en_US','','puLimitPriorToPUList','noMPSResourcesAvailable','label','No MPS Resources Available',NULL,1),
	(945,'en_US','','puLimitPriorToPUList','selectMachine','label','Select A Machine',NULL,1),
	(946,'en_US','','resource','barcode','label','Barcode',NULL,1),
	(947,'en_US','','resource','barcode_exists','error','Barcode already exists in the database',NULL,1),
	(948,'en_US','','resource','cancel','label','Cancel',NULL,1),
	(949,'en_US','','resource','commission_date','label','Commission Date',NULL,1),
	(950,'en_US','','resource','commission_date','error','Commission date cannot be empty',NULL,1),
	(951,'en_US','','resource','commission_date','constraint','NotEmpty',NULL,1),
	(952,'en_US','','resource','commission_date','metaposition','40',NULL,1),
	(953,'en_US','','resource','created','error','Resource was NOT created. Please fill in required fields.',NULL,1),
	(954,'en_US','','resource','created_success','label','Resource created successfully.',NULL,1),
	(955,'en_US','','resource','decommission_date','label','Decommission Date',NULL,1),
	(956,'en_US','','resource','decommission_date','error','Decommission date cannot be empty',NULL,1),
	(957,'en_US','','resource','decommission_date','metaposition','50',NULL,1),
	(958,'en_US','','resource','isActive','label','Active',NULL,1),
	(959,'en_US','','resource','machineType','label','Resource Category',NULL,1),
	(960,'en_US','','resource','name','label','Name',NULL,1),
	(961,'en_US','','resource','name','error','Resource name cannot be empty',NULL,1),
	(962,'en_US','','resource','name','constraint','NotEmpty',NULL,1),
	(963,'en_US','','resource','resourceCategoryId','label','Resource Category',NULL,1),
	(964,'en_US','','resource','resourceCategoryId','error','Must select category of resource',NULL,1),
	(965,'en_US','','resource','resourceCategoryId','constraint','NotEmpty',NULL,1),
	(966,'en_US','','resource','resourceTypeId','label','Resource Type',NULL,1),
	(967,'en_US','','resource','resourceTypeId','error','Must select assay platform',NULL,1),
	(968,'en_US','','resource','resourceTypeId','constraint','NotEmpty',NULL,1),
	(969,'en_US','','resource','resource_exists','error','Resource name already exists in the database',NULL,1),
	(970,'en_US','','resource','resource_list','label','List of Machines',NULL,1),
	(971,'en_US','','resource','save','label','Save',NULL,1),
	(972,'en_US','','resource','updated','error','Resource was NOT updated. Please fill in required fields.',NULL,1),
	(973,'en_US','','resource','updateResourceDetails','label','Update Resource Details',NULL,1),
	(974,'en_US','','resource','updated_success','label','Resource updated successfully.',NULL,1),
	(975,'en_US','','run','dateRunStarted','label','Start',NULL,1),
	(976,'en_US','','run','dateRunEnded','label','End',NULL,1),
	(977,'en_US','','run','detailFiles','label','Files',NULL,1),
	(978,'en_US','','run','detailResource','label','Resource',NULL,1),
	(979,'en_US','','run','detailResourceCells','label','Resource Cells',NULL,1),
	(980,'en_US','','run','detailRunLanes','label','Run Cells',NULL,1),
	(981,'en_US','','run','detailSample','label','Sample',NULL,1),
	(982,'en_US','','run','detailSamples','label','Samples',NULL,1),
	(983,'en_US','','run','lanedetailFiles','label','Files',NULL,1),
	(984,'en_US','','run','lanedetailResource','label','Resource',NULL,1),
	(985,'en_US','','run','lanedetailSample','label','Sample',NULL,1),
	(986,'en_US','','run','machine','label','Machine',NULL,1),
	(987,'en_US','','run','name','label','Run Name',NULL,1),
	(988,'en_US','','run','path_to_data','label','Data Path',NULL,1),
	(989,'en_US','','run','platformUnitBarcode','label','PU Barcode',NULL,1),
	(990,'en_US','','run','readlength','label','Length',NULL,1),
	(991,'en_US','','run','readType','label','Type',NULL,1),
	(992,'en_US','','run','run_list','label','List of Runs',NULL,1),
	(993,'en_US','','run','statusForRun','label','Status',NULL,1),
	(994,'en_US','','run','updated_success','label','Run updated successfully.',NULL,1),
	(995,'en_US','','runInstance','cancel','label','Cancel',NULL,1),
	(996,'en_US','','runInstance','chooseResource','label','Choose A Resource',NULL,1),
	(997,'en_US','','runInstance','created_success','label','Run created successfully.',NULL,1),
	(998,'en_US','','runInstance','headerCreate','label','Create New Sequence Run',NULL,1),
	(999,'en_US','','runInstance','headerUpdate','label','Update Sequence Run',NULL,1),
	(1000,'en_US','','runInstance','dateRunEnded','label','Run Ended',NULL,1),
	(1001,'en_US','','runInstance','dateRunStarted','label','Run Started (mm/dd/yyyy)',NULL,1),
	(1002,'en_US','','runInstance','dateRunStarted','constraint','NotEmpty',NULL,1),
	(1003,'en_US','','runInstance','dateRunStarted','error','Cannot be empty',NULL,1),
	(1004,'en_US','','runInstance','dateRunStartedFormat','error','Incorrect Format',NULL,1),
	(1005,'en_US','','runInstance','name','label','Name',NULL,1),
	(1006,'en_US','','runInstance','name','constraint','NotEmpty',NULL,1),
	(1007,'en_US','','runInstance','name','error','Name cannot be empty',NULL,1),
	(1008,'en_US','','runInstance','name_exists','error','Run name already exists',NULL,1),
	(1009,'en_US','','runInstance','readType','label','Read Type',NULL,1),
	(1010,'en_US','','runInstance','readType','constraint','NotEmpty',NULL,1),
	(1011,'en_US','','runInstance','readType','error','Read Type cannot be empty',NULL,1),
	(1012,'en_US','','runInstance','readType','control','select:${readTypes}:valuePassedBack:valueVisible',NULL,1),
	(1013,'en_US','','runInstance','readType','metaposition','10',NULL,1),
	(1014,'en_US','','runInstance','readlength','label','Read Length',NULL,1),
	(1015,'en_US','','runInstance','readlength','constraint','NotEmpty',NULL,1),
	(1016,'en_US','','runInstance','readlength','error','Read Length cannot be empty',NULL,1),
	(1017,'en_US','','runInstance','readlength','control','select:${readlengths}:valuePassedBack:valueVisible',NULL,1),
	(1018,'en_US','','runInstance','readlength','metaposition','15',NULL,1),
	(1019,'en_US','','runInstance','reset','label','Reset',NULL,1),
	(1020,'en_US','','runInstance','submit','label','Submit',NULL,1),
	(1021,'en_US','','runInstance','technician','label','Technician',NULL,1),
	(1022,'en_US','','runInstance','technician','constraint','NotEmpty',NULL,1),
	(1023,'en_US','','runInstance','technician','error','Technician cannot be empty',NULL,1),
	(1024,'en_US','','runInstance','updated_success','label','Run updated successfully.',NULL,1),
	(1025,'en_US','','sample','controlLib_adaptor','label','Adaptor',NULL,1),
	(1026,'en_US','','sample','controlLib_adaptorSet','label','Adaptor Set',NULL,1),
	(1027,'en_US','','sample','controlLib_createButton','label','Create New Control',NULL,1),
	(1028,'en_US','','sample','controlLib_controlLibraries','label','Control Libraries',NULL,1),
	(1029,'en_US','','sample','controlLib_edit','label','Edit',NULL,1),
	(1030,'en_US','','sample','controlLib_index','label','Index',NULL,1),
	(1031,'en_US','','sample','controlLib_isActive','label','Is Active?',NULL,1),
	(1032,'en_US','','sample','controlLib_name','label','Control Name',NULL,1),
	(1033,'en_US','','sample','controlLib_noneExist','label','No Control Libraries Exist',NULL,1),
	(1034,'en_US','','sample','detail_children','label','',NULL,1),
	(1035,'en_US','','sample','detail_facManSampleToLib','label','Facility Manager Sample To Library',NULL,1),
	(1036,'en_US','','sample','detail_files','label','Files',NULL,1),
	(1037,'en_US','','sample','detail_jobs','label','Jobs',NULL,1),
	(1038,'en_US','','sample','detail_parents','label','Parents',NULL,1),
	(1039,'en_US','','sample','detail_relations','label','Relations',NULL,1),
	(1040,'en_US','','sample','receivedStatus','label','Received?',NULL,1),
	(1041,'en_US','','sample','jobId','label','Job',NULL,1),
	(1042,'en_US','','sample','name','label','Sample Name',NULL,1),
	(1043,'en_US','','sample','name','error','Name cannot be null',NULL,1),
	(1044,'en_US','','sample','pi','label','PI',NULL,1),
	(1045,'en_US','','sample','runs','label','Runs',NULL,1),
	(1046,'en_US','','sample','sample_list','label','List of Samples',NULL,1),
	(1047,'en_US','','sample','submitter','label','Submitter',NULL,1),
	(1048,'en_US','','sample','subtype','label','Subtype',NULL,1),
	(1049,'en_US','','sample','type','label','Type',NULL,1),
	(1050,'en_US','','sample','updateControlLib_active','label','Active',NULL,1),
	(1051,'en_US','','sample','updateControlLib_activeAlert','label','Please select active or inactive for this control',NULL,1),
	(1052,'en_US','','sample','updateControlLib_adaptorSet','label','Adaptor Set',NULL,1),
	(1053,'en_US','','sample','updateControlLib_adaptorAlert','label','Please select an adaptor',NULL,1),
	(1054,'en_US','','sample','updateControlLib_adaptorSetAlert','label','Please select an adaptor set',NULL,1),
	(1055,'en_US','','sample','updateControlLib_adaptor','label','Adaptor',NULL,1),
	(1056,'en_US','','sample','updateControlLib_cancel','label','Cancel',NULL,1),
	(1057,'en_US','','sample','updateControlLib_create','label','Create New Library Control',NULL,1),
	(1058,'en_US','','sample','updateControlLib_inactive','label','Inactive',NULL,1),
	(1059,'en_US','','sample','updateControlLib_index','label','Index',NULL,1),
	(1060,'en_US','','sample','updateControlLib_isActive','label','Is Active?',NULL,1),
	(1061,'en_US','','sample','updateControlLib_name','label','Control Name',NULL,1),
	(1062,'en_US','','sample','updateControlLib_nameAlert','label','Please provide a name for this control',NULL,1),
	(1063,'en_US','','sample','updateControlLib_reset','label','Reset',NULL,1),
	(1064,'en_US','','sample','updateControlLib_selectAdaptor','label','SELECT AN ADAPTOR',NULL,1),
	(1065,'en_US','','sample','updateControlLib_selectASet','label','SELECT AN ADAPTOR SET',NULL,1),
	(1066,'en_US','','sample','updateControlLib_submit','label','Submit',NULL,1),
	(1067,'en_US','','sample','updateControlLib_update','label','Update Library Control',NULL,1),
	(1068,'en_US','','sampleDetail','adaptorsetParameter','error','No adaptorset matches supplied adaptorset parameter',NULL,1),
	(1069,'en_US','','sampleDetail','jobNotFound','error','Job not found in the database',NULL,1),
	(1070,'en_US','','sampleDetail','jobParameter','error','No job matches supplied job parameter',NULL,1),
	(1071,'en_US','','sampleDetail','jobSampleMismatch','error','Supplied job and sample parameters do not refer to a valid object',NULL,1),
	(1072,'en_US','','sampleDetail','libraryNotFound','error','Library not found in the database',NULL,1),
	(1073,'en_US','','sampleDetail','nameClash','error','Name already exists in database!',NULL,1),
	(1074,'en_US','','sampleDetail','sampleNotFound','error','Sample not found in the database',NULL,1),
	(1075,'en_US','','sampleDetail','sampleParameter','error','No sample matches supplied sample parameter',NULL,1),
	(1076,'en_US','','sampleDetail','sampleSubtypeNotFound','error','Cannot find requested sample subtype in the database',NULL,1),
	(1077,'en_US','','sampleDetail','unexpected','error','Sample NOT updated. Unexpected error!',NULL,1),
	(1078,'en_US','','sampleDetail','updated','error','Sample NOT updated. Fill in required fields or cancel to restore.',NULL,1),
	(1079,'en_US','','sampleDetail','updated_success','label','Sample sucessfully updated.',NULL,1),
	(1080,'en_US','','sampledetail_ro','cancel','label','Cancel',NULL,1),
	(1081,'en_US','','sampledetail_ro','edit','label','Edit',NULL,1),
	(1082,'en_US','','sampledetail_ro','sampleName','label','Sample Name',NULL,1),
	(1083,'en_US','','sampledetail_ro','sampleType','label','Sample Type',NULL,1),
	(1084,'en_US','','sampledetail_rw','cancel','label','Cancel',NULL,1),
	(1085,'en_US','','sampledetail_rw','sampleName','label','Sample Name',NULL,1),
	(1086,'en_US','','sampledetail_rw','sampleType','label','Sample Type',NULL,1),
	(1087,'en_US','','sampledetail_rw','save','label','Save',NULL,1),
	(1088,'en_US','','sampleDraft','name','error','You must provide a sample name',NULL,1),
	(1089,'en_US','','samplereceivetask','subtitle_none','label','No Pending Samples',NULL,1),
	(1090,'en_US','','sections','banner_dashboard','label','Dashboard',NULL,1),
	(1091,'en_US','','sections','banner_logout','label','Logout',NULL,1),
	(1092,'en_US','','sections','footer_albert','label','Albert Einstein College of Medicine (2012). Distributed freely under the terms of the',NULL,1),
	(1093,'en_US','','sections','footer_gnu','label','GNU AFFERO General Public License',NULL,1),
	(1094,'en_US','','sections','footer_maintainedBy','label','Core WASP System and plugin repository maintained by Albert Einstein College of Medicine Computational Epigenomics and Genomics Cores',NULL,1),
	(1095,'en_US','','sections','footer_waspsystem','label','waspsystem.org',NULL,1),
	(1096,'en_US','','showPlatformUnit','add','label','Add',NULL,1),
	(1097,'en_US','','showPlatformUnit','addControl','label','Add Control',NULL,1),
	(1098,'en_US','','showPlatformUnit','barcode','label','Barcode',NULL,1),
	(1099,'en_US','','showPlatformUnit','cancel','label','Cancel',NULL,1),
	(1100,'en_US','','showPlatformUnit','cell','label','Cell',NULL,1),
	(1101,'en_US','','showPlatformUnit','concOnCell','label','Conc. On Cell',NULL,1),
	(1102,'en_US','','showPlatformUnit','createNewRun','label','Create New Run',NULL,1),
	(1103,'en_US','','showPlatformUnit','currentConcPM','label','Current Conc. (pM)',NULL,1),
	(1104,'en_US','','showPlatformUnit','edit','label','Edit',NULL,1),
	(1105,'en_US','','showPlatformUnit','finalConcPM','label','Final Conc. (pM)',NULL,1),
	(1106,'en_US','','showPlatformUnit','index','label','Index',NULL,1),
	(1107,'en_US','','showPlatformUnit','jobJ','label','Job J',NULL,1),
	(1108,'en_US','','showPlatformUnit','locked','label','Locked',NULL,1),
	(1109,'en_US','','showPlatformUnit','machine','label','Machine',NULL,1),
	(1110,'en_US','','showPlatformUnit','newConcPM','label','New Conc. (pM)',NULL,1),
	(1111,'en_US','','showPlatformUnit','noControlOnCell','label','No Control On Cell',NULL,1),
	(1112,'en_US','','showPlatformUnit','noLibrariesOnCell','label','No Libraries On Cell',NULL,1),
	(1113,'en_US','','showPlatformUnit','platformUnit','label','Platform Unit Name',NULL,1),
	(1114,'en_US','','showPlatformUnit','platformUnitStatus','label','Platform Unit Status',NULL,1),
	(1115,'en_US','','showPlatformUnit','pleasePorvideValue_alert','label','Please provide a value',NULL,1),
	(1116,'en_US','','showPlatformUnit','pleaseProvideControlConc_alert','label','Please provide a final concentration for this control',NULL,1),
	(1117,'en_US','','showPlatformUnit','pleaseProvideStartDate_alert','label','Please provide a start date',NULL,1),
	(1118,'en_US','','showPlatformUnit','pleaseProvideValidName_alert','label','Please provide a valid name for this run',NULL,1),
	(1119,'en_US','','showPlatformUnit','pleaseProvideValidStartDate_alert','label','Please provide a start date in the proper format',NULL,1),
	(1120,'en_US','','showPlatformUnit','pleaseSelectControl_alert','label','Please select a control',NULL,1),
	(1121,'en_US','','showPlatformUnit','pleaseSelectMachine_alert','label','Please select a machine',NULL,1),
	(1122,'en_US','','showPlatformUnit','pleaseSelectReadLength_alert','label','Please select a read length',NULL,1),
	(1123,'en_US','','showPlatformUnit','pleaseSelectReadType_alert','label','Please select a read type',NULL,1),
	(1124,'en_US','','showPlatformUnit','pleaseSelectTechnician_alert','label','Please select a technician',NULL,1),
	(1125,'en_US','','showPlatformUnit','pM','label','pM',NULL,1),
	(1126,'en_US','','showPlatformUnit','readLength','label','Read Length',NULL,1),
	(1127,'en_US','','showPlatformUnit','readType','label','Read Type',NULL,1),
	(1128,'en_US','','showPlatformUnit','removeControl','label','Remove Control',NULL,1),
	(1129,'en_US','','showPlatformUnit','removeControlFromThisCell','label','Remove Control From This Cell?',NULL,1),
	(1130,'en_US','','showPlatformUnit','removeLibFromCell_alert','label','Remove Library From This Cell?',NULL,1),
	(1131,'en_US','','showPlatformUnit','reset','label','Reset',NULL,1),
	(1132,'en_US','','showPlatformUnit','runName','label','Run Name',NULL,1),
	(1133,'en_US','','showPlatformUnit','runTechnician','label','Run Technician',NULL,1),
	(1134,'en_US','','showPlatformUnit','selectControl','label','--SELECT A CONTROL--',NULL,1),
	(1135,'en_US','','showPlatformUnit','selectMachine','label','--SELECT A MACHINE--',NULL,1),
	(1136,'en_US','','showPlatformUnit','selectTechnician','label','--SELECT A TECHNICIAN--',NULL,1),
	(1137,'en_US','','showPlatformUnit','startDate','label','Start Date (mm/dd/yyyy)',NULL,1),
	(1138,'en_US','','showPlatformUnit','submit','label','Submit',NULL,1),
	(1139,'en_US','','showPlatformUnit','submit_alert','label','Submit?',NULL,1),
	(1140,'en_US','','showPlatformUnit','type','label','Type',NULL,1),
	(1141,'en_US','','showPlatformUnit','unlocked','label','Unlocked',NULL,1),
	(1142,'en_US','','showPlatformUnit','update','label','Update',NULL,1),
	(1143,'en_US','','showPlatformUnit','warning1','label','Once You Create This Run Record,<br />Adding User Libraries To This Platform Unit Will Be Prohibited',NULL,1),
	(1144,'en_US','','showPlatformUnit','warning2','label','Adding Additional User Libraries To This Platform Unit<br />Is Now Prohibited',NULL,1),
	(1145,'en_US','','status','piApproval','label','PI Approval',NULL,1),
	(1146,'en_US','','status','daApproval','label','DA Approval',NULL,1),
	(1147,'en_US','','status','notYetSet','label','Not Yet Set',NULL,1),
	(1148,'en_US','','status','awaitingResponse','label','Awaiting Response',NULL,1),
	(1149,'en_US','','status','approved','label','Approved',NULL,1),
	(1150,'en_US','','status','rejected','label','Rejected',NULL,1),
	(1151,'en_US','','status','abandoned','label','Abandoned',NULL,1),
	(1152,'en_US','','status','unknown','label','Unknown',NULL,1),
	(1153,'en_US','','sysrole','invalidRoleSpecified','error','Invalid role specified',NULL,1),
	(1154,'en_US','','sysrole','list_action','label','Action',NULL,1),
	(1155,'en_US','','sysrole','list_add_role','label','Use the form below to add system roles to existing WASP users',NULL,1),
	(1156,'en_US','','sysrole','list_create','label','Add System Role to User',NULL,1),
	(1157,'en_US','','sysrole','list_current','label','Current Users with System Roles',NULL,1),
	(1158,'en_US','','sysrole','list_name','label','Name (Login)',NULL,1),
	(1159,'en_US','','sysrole','list_remove','label','Remove',NULL,1),
	(1160,'en_US','','sysrole','list_role','label','Role',NULL,1),
	(1161,'en_US','','sysrole','list_submit','label','Submit',NULL,1),
	(1162,'en_US','','sysrole','list_sysuser_name','label','Existing User',NULL,1),
	(1163,'en_US','','sysrole','list_sysuser_role','label','New Role',NULL,1),
	(1164,'en_US','','sysrole','list_unchangeable','label','Unchangeable',NULL,1),
	(1165,'en_US','','sysrole','noRoleSpecified','error','No role specified',NULL,1),
	(1166,'en_US','','sysrole','noUserSpecified','error','No user specified',NULL,1),
	(1167,'en_US','','sysrole','onlyUserWithRole','error','Cannot remove role from user. The role must be granted to another user first.',NULL,1),
	(1168,'en_US','','sysrole','success','label','Update completed successfully',NULL,1),
	(1169,'en_US','','sysrole','userNonexistant','error','The user specified does not exist in the database',NULL,1),
	(1170,'en_US','','sysrole','userRoleExists','error','Specified user already has the selected role',NULL,1),
	(1171,'en_US','','sysrole','wrongUserRoleCombination','error','Specified user does not have specified role',NULL,1),
	(1172,'en_US','','task','departmentAdmin','label','Department Administration Tasks',NULL,1),
	(1173,'en_US','','task','instructions','label','If the system has determined that there are tasks requiring your attention, links will be posted below. Please click on the links and address the tasks assigned to you ASAP.',NULL,1),
	(1174,'en_US','','task','jobQuote','label','Job Quote Tasks',NULL,1),
	(1175,'en_US','','task','labManager','label','Lab Management Tasks',NULL,1),
	(1176,'en_US','','task','libraryqc_action','label','QC Result',NULL,1),
	(1177,'en_US','','task','libraryqc_comment_empty','error','Update Failed: Please provide an explanation for this failure',NULL,1),
	(1178,'en_US','','task','libraryqc_failed','label','Failed',NULL,1),
	(1179,'en_US','','task','libraryqc_invalid_sample','error','Library has no record in the database',NULL,1),
	(1180,'en_US','','task','libraryqc_jobId','label','JobID',NULL,1),
	(1181,'en_US','','task','libraryqc_jobName','label','Job Name',NULL,1),
	(1182,'en_US','','task','libraryqc_molecule','label','Molecule',NULL,1),
	(1183,'en_US','','task','libraryqc_passed','label','Passed',NULL,1),
	(1184,'en_US','','task','libraryqc_qcStatus_invalid','error','Update Failed: You must select either Passed or Failed',NULL,1),
	(1185,'en_US','','task','libraryqc_reset','label','Reset',NULL,1),
	(1186,'en_US','','task','libraryqc_sample','label','Library',NULL,1),
	(1187,'en_US','','task','libraryqc_status_invalid','error','Update Failed: Invalid Status',NULL,1),
	(1188,'en_US','','task','libraryqc_submit','label','Submit',NULL,1),
	(1189,'en_US','','task','libraryqc_submitter','label','Submitter',NULL,1),
	(1190,'en_US','','task','libraryqc_subtitle_none','label','No Pending Library QC Tasks',NULL,1),
	(1191,'en_US','','task','libraryqc_title','label','Library QC Manager',NULL,1),
	(1192,'en_US','','task','libraryqc_update_success','label','Update completed successfully',NULL,1),
	(1193,'en_US','','task','libraryqc_validateAlert','label','Please select either Library Passed or Failed',NULL,1),
	(1194,'en_US','','task','libraryqc_receivedstatus_empty','error','Update Failed: Please select Passed or Failed',NULL,1),
	(1195,'en_US','','task','libraryqc_receivedstatus_invalid','error','Update Failed: Action Invalid',NULL,1),
	(1196,'en_US','','task','libraryqc_message','error','Problem occurred updating status',NULL,1),
	(1197,'en_US','','task','libraryqc_validateCommentAlert','label','Please provide a reason for this library failing QC',NULL,1),
	(1198,'en_US','','task','libraryqc_validatePassFailAlert','label','Please select either Passed or Failed',NULL,1),
	(1199,'en_US','','task','none','label','There are currently no tasks requiring your attention.',NULL,1),
	(1200,'en_US','','task','samplereceive_action','label','Action',NULL,1),
	(1201,'en_US','','task','samplereceive_invalid_sample','error','Sample has no record in the database',NULL,1),
	(1202,'en_US','','task','samplereceive_jobId','label','JobID',NULL,1),
	(1203,'en_US','','task','samplereceive_jobName','label','Job Name',NULL,1),
	(1204,'en_US','','task','samplereceive_message','error','Problem occurred updating status',NULL,1),
	(1205,'en_US','','task','samplereceive_molecule','label','Molecule',NULL,1),
	(1206,'en_US','','task','samplereceive_received','label','Received',NULL,1),
	(1207,'en_US','','task','samplereceive_receivedstatus_empty','error','Update Failed: Please select Received or Withdrawn for at least one sample',NULL,1),
	(1208,'en_US','','task','samplereceive_receivedstatus_invalid','error','Update Failed: Action Invalid',NULL,1),
	(1209,'en_US','','task','samplereceive_receivedstatus_unexpected','error','Update Failed: Unexpected Error',NULL,1),
	(1210,'en_US','','task','samplereceive_reset','label','Reset',NULL,1),
	(1211,'en_US','','task','samplereceive_sample','label','Sample',NULL,1),
	(1212,'en_US','','task','samplereceive_select','label','--SELECT--',NULL,1),
	(1213,'en_US','','task','samplereceive_setAllReceived','label','set all received',NULL,1),
	(1214,'en_US','','task','samplereceive_setAllWithdrawn','label','set all withdrawn',NULL,1),
	(1215,'en_US','','task','samplereceive_submit','label','Submit',NULL,1),
	(1216,'en_US','','task','samplereceive_submitter','label','Submitter',NULL,1),
	(1217,'en_US','','task','samplereceive_title','label','Sample Receiver Manager',NULL,1),
	(1218,'en_US','','task','samplereceive_update_success','label','Update completed successfully',NULL,1),
	(1219,'en_US','','task','samplereceive_validateAlert','label','Please select either Sample Received or Withdrawn for at least one sample',NULL,1),
	(1220,'en_US','','task','samplereceive_withdrawn','label','Withdrawn',NULL,1),
	(1221,'en_US','','task','sampleqc_action','label','QC Result',NULL,1),
	(1222,'en_US','','task','sampleqc_comment_empty','error','Update Failed: Please provide an explanation for this failure',NULL,1),
	(1223,'en_US','','task','sampleqc_failed','label','Failed',NULL,1),
	(1224,'en_US','','task','sampleqc_invalid_sample','error','Record not found in database',NULL,1),
	(1225,'en_US','','task','sampleqc_jobId','label','JobID',NULL,1),
	(1226,'en_US','','task','sampleqc_jobName','label','Job Name',NULL,1),
	(1227,'en_US','','task','sampleqc_message','error','Problem occurred updating status',NULL,1),
	(1228,'en_US','','task','sampleqc_molecule','label','Molecule',NULL,1),
	(1229,'en_US','','task','sampleqc_passed','label','Passed',NULL,1),
	(1230,'en_US','','task','sampleqc_qcStatus_invalid','error','Update Failed: You must select either Passed or Failed',NULL,1),
	(1231,'en_US','','task','sampleqc_reset','label','Reset',NULL,1),
	(1232,'en_US','','task','sampleqc_sample','label','Sample',NULL,1),
	(1233,'en_US','','task','sampleqc_status_invalid','error','Update Failed: Invalid Status',NULL,1),
	(1234,'en_US','','task','sampleqc_submit','label','Submit',NULL,1),
	(1235,'en_US','','task','sampleqc_submitter','label','Submitter',NULL,1),
	(1236,'en_US','','task','sampleqc_subtitle_none','label','No Pending Sample QC Tasks',NULL,1),
	(1237,'en_US','','task','sampleqc_title','label','Sample QC Manager',NULL,1),
	(1238,'en_US','','task','sampleqc_update_success','label','Update completed successfully',NULL,1),
	(1239,'en_US','','task','sampleqc_receivedstatus_empty','error','Update Failed: Please select Passed or Failed',NULL,1),
	(1240,'en_US','','task','sampleqc_receivedstatus_invalid','error','Update Failed: Action Invalid',NULL,1),
	(1241,'en_US','','task','sampleqc_validateCommentAlert','label','Please provide a reason for this sample failing QC',NULL,1),
	(1242,'en_US','','task','sampleqc_validatePassFailAlert','label','Please select either Passed or Failed',NULL,1),
	(1243,'en_US','','uiField','added','label','Field Added',NULL,1),
	(1244,'en_US','','uiField','area','label','Area',NULL,1),
	(1245,'en_US','','uiField','attrName','label','Attribute Name',NULL,1),
	(1246,'en_US','','uiField','attrName','suffix','<font color=\"blue\"> see footnote<sup>1</sup> </font>',NULL,1),
	(1247,'en_US','','uiField','attrValue','label','Attribute Value',NULL,1),
	(1248,'en_US','','uiField','locale','label','Locale',NULL,1),
	(1249,'en_US','','uiField','locale','error','Locale not specified',NULL,1),
	(1250,'en_US','','uiField','name','label','Field Name',NULL,1),
	(1251,'en_US','','uiField','not_unique','error','Property already exists',NULL,1),
	(1252,'en_US','','uiField','removed','data','Field Deleted',NULL,1),
	(1253,'en_US','','uiField','updated','data','Attribute Updated',NULL,1),
	(1254,'en_US','','uiField','updated','label','Field Updated',NULL,1),
	(1255,'en_US','','user','address','label','Address',NULL,1),
	(1256,'en_US','','user','address','metaposition','40',NULL,1),
	(1257,'en_US','','user','auth_login_validate','error','Login Failure. Please provide valid login credentials.',NULL,1),
	(1258,'en_US','','user','building_room','label','Room',NULL,1),
	(1259,'en_US','','user','building_room','metaposition','30',NULL,1),
	(1260,'en_US','','user','building_room','error','Room cannot be empty',NULL,1),
	(1261,'en_US','','user','building_room','constraint','NotEmpty',NULL,1),
	(1262,'en_US','','user','city','label','City',NULL,1),
	(1263,'en_US','','user','city','error','City cannot be empty',NULL,1),
	(1264,'en_US','','user','city','constraint','NotEmpty',NULL,1),
	(1265,'en_US','','user','city','metaposition','50',NULL,1),
	(1266,'en_US','','user','country','label','Country',NULL,1),
	(1267,'en_US','','user','country','error','Country cannot be empty',NULL,1),
	(1268,'en_US','','user','country','control','select:${countries}:code:name',NULL,1),
	(1269,'en_US','','user','country','constraint','NotEmpty',NULL,1),
	(1270,'en_US','','user','country','metaposition','70',NULL,1),
	(1271,'en_US','','user','created','error','User was NOT created. Please fill in required fields.',NULL,1),
	(1272,'en_US','','user','created_success','label','User was created. Consider assigning a role to this new user.',NULL,1),
	(1273,'en_US','','user','departmentId','control','select:${departments}:departmentId:name',NULL,1),
	(1274,'en_US','','user','departmentId','constraint','NotEmpty',NULL,1),
	(1275,'en_US','','user','departmentId','error','A department must be selected',NULL,1),
	(1276,'en_US','','user','departmentId','label','Department',NULL,1),
	(1277,'en_US','','user','departmentId','metaposition','20',NULL,1),
	(1278,'en_US','','user','email','label','Email',NULL,1),
	(1279,'en_US','','user','email','constraint','NotEmpty',NULL,1),
	(1280,'en_US','','user','email','error','Wrong email address format',NULL,1),
	(1281,'en_US','','user','email_changed_p1','label','Your email address has changed. An email has been sent to your new email address requesting confirmation. Please confirm by clicking the link in the email then <a href=\"../login.do\"/>click here to login</a>',NULL,1),
	(1282,'en_US','','user','email_changed_p2','label','If you have not received an email, requesting confirmation, within a reasonable time period and suspect your email address may have been mis-typed, you may reset your email address by clicking <a href=\"requestEmailChange.do\">here</a>',NULL,1),
	(1283,'en_US','','user','email_change_confirmed','label','Your email address change has been confirmed successfully.',NULL,1),
	(1284,'en_US','','user','email_exists','error','Email already exists in the database',NULL,1),
	(1285,'en_US','','user','fax','label','Fax',NULL,1),
	(1286,'en_US','','user','fax','metaposition','100',NULL,1),
	(1287,'en_US','','user','firstName','label','First Name',NULL,1),
	(1288,'en_US','','user','firstName','constraint','NotEmpty',NULL,1),
	(1289,'en_US','','user','firstName','error','First Name cannot be empty',NULL,1),
	(1290,'en_US','','user','institution','label','Institution',NULL,1),
	(1291,'en_US','','user','institution','error','Institution cannot be empty',NULL,1),
	(1292,'en_US','','user','institution','constraint','NotEmpty',NULL,1),
	(1293,'en_US','','user','institution','metaposition','10',NULL,1),
	(1294,'en_US','','user','isActive','label','Active',NULL,1),
	(1295,'en_US','','user','jobs','label','Jobs',NULL,1),
	(1296,'en_US','','user','job_list','label','View jobs belonging to user in these labs ',NULL,1),
	(1297,'en_US','','user','labusers','label','Lab Users',NULL,1),
	(1298,'en_US','','user','lastName','label','Last Name',NULL,1),
	(1299,'en_US','','user','lastName','constraint','NotEmpty',NULL,1),
	(1300,'en_US','','user','lastName','error','Last Name cannot be empty',NULL,1),
	(1301,'en_US','','user','locale','label','Locale',NULL,1),
	(1302,'en_US','','user','locale','constraint','NotEmpty',NULL,1),
	(1303,'en_US','','user','locale','error','Locale cannot be empty',NULL,1),
	(1304,'en_US','','user','login','label','Login',NULL,1),
	(1305,'en_US','','user','login','constraint','NotEmpty',NULL,1),
	(1306,'en_US','','user','login','error','Login cannot be empty',NULL,1),
	(1307,'en_US','','user','login_exists','error','Login name already exists in the database',NULL,1),
	(1308,'en_US','','user','mypassword','data','Submit',NULL,1),
	(1309,'en_US','','user','mypassword_cannotChange','error','Externally authenticated user cannot change password in WASP',NULL,1),
	(1310,'en_US','','user','mypassword_cur_mismatch','error','Your old password does NOT match the password in our database',NULL,1),
	(1311,'en_US','','user','mypassword_instructions','label','New Password Requirements:<br />At least 8 characters in length<br />Only letters & numbers permitted (no spaces, etc)<br />At least one letter and one number<br />',NULL,1),
	(1312,'en_US','','user','mypassword_missingparam','error','Please provide values for all fields',NULL,1),
	(1313,'en_US','','user','mypassword_newpassword1','label','New Password',NULL,1),
	(1314,'en_US','','user','mypassword_newpassword2','label','Confirm New Password',NULL,1),
	(1315,'en_US','','user','mypassword_new_invalid','error','New password must be at least 8 characters, containing only letters and numbers, with at least one letter and number',NULL,1),
	(1316,'en_US','','user','mypassword_new_mismatch','error','The two entries for your NEW password are NOT identical',NULL,1),
	(1317,'en_US','','user','mypassword_nodiff','error','Your old and new passwords may NOT be the same',NULL,1),
	(1318,'en_US','','user','mypassword_ok','label','Password Has Been Changed',NULL,1),
	(1319,'en_US','','user','mypassword_oldpassword','label','Old Password',NULL,1),
	(1320,'en_US','','user','mypassword_submit','label','Submit',NULL,1),
	(1321,'en_US','','user','password','label','Password',NULL,1),
	(1322,'en_US','','user','password','constraint','NotEmpty',NULL,1),
	(1323,'en_US','','user','password','error','Password cannot be empty',NULL,1),
	(1324,'en_US','','user','phone','label','Phone',NULL,1),
	(1325,'en_US','','user','phone','error','Phone cannot be empty',NULL,1),
	(1326,'en_US','','user','phone','constraint','NotEmpty',NULL,1),
	(1327,'en_US','','user','phone','metaposition','90',NULL,1),
	(1328,'en_US','','user','roles','label','Roles',NULL,1),
	(1329,'en_US','','user','samples','label','Samples',NULL,1),
	(1330,'en_US','','user','state','label','State',NULL,1),
	(1331,'en_US','','user','state','error','State cannot be empty',NULL,1),
	(1332,'en_US','','user','state','control','select:${states}:code:name',NULL,1),
	(1333,'en_US','','user','state','constraint','NotEmpty',NULL,1),
	(1334,'en_US','','user','state','metaposition','60',NULL,1),
	(1335,'en_US','','user','title','label','Title',NULL,1),
	(1336,'en_US','','user','title','error','Title cannot be empty',NULL,1),
	(1337,'en_US','','user','title','constraint','NotEmpty',NULL,1),
	(1338,'en_US','','user','title','control','select:Prof:Prof;Dr:Dr;Mr:Mr;Ms:Ms;Mrs:Mrs;Miss:Miss',NULL,1),
	(1339,'en_US','','user','title','metaposition','1',NULL,1),
	(1340,'en_US','','user','updated','error','User was NOT updated. Please fill in required fields.',NULL,1),
	(1341,'en_US','','user','updated_fail','error','User was NOT updated.',NULL,1),
	(1342,'en_US','','user','updated_success','label','User was updated',NULL,1),
	(1343,'en_US','','user','userloginandemail_changed_p1','label','Your login and email address has changed. An email has been sent to your new email address requesting confirmation. Please confirm by clicking the link in the email then <a href=\"../login.do\"/>click here to login</a>',NULL,1),
	(1344,'en_US','','user','userloginandemail_changed_p2','label','If you have not received an email, requesting confirmation, within a reasonable time period and suspect your email address may have been mis-typed, you may reset your email address by clicking <a href=\"requestEmailChange.do\">here</a>',NULL,1),
	(1345,'en_US','','user','userlogin_changed_p1','label','Your login has changed. Please <a href=\"../login.do\"/>click here to login</a>',NULL,1),
	(1346,'en_US','','user','user_list','label','List of Users',NULL,1),
	(1347,'en_US','','user','lab_member_list','label','Lab Members',NULL,1),
	(1348,'en_US','','user','zip','label','Zip',NULL,1),
	(1349,'en_US','','user','zip','error','Zip cannot be empty',NULL,1),
	(1350,'en_US','','user','zip','metaposition','80',NULL,1),
	(1351,'en_US','','userDetail','cancel','label','Cancel',NULL,1),
	(1352,'en_US','','userDetail','change_password','label','Change Password',NULL,1),
	(1353,'en_US','','userDetail','edit','label','Edit',NULL,1),
	(1354,'en_US','','userDetail','edit_as_other','label','Edit (as other)',NULL,1),
	(1355,'en_US','','userDetail','jobs','label','Jobs',NULL,1),
	(1356,'en_US','','userDetail','lab_users','label','Lab Users',NULL,1),
	(1357,'en_US','','userDetail','samples','label','Samples',NULL,1),
	(1358,'en_US','','userDetail','save','label','Save Changes',NULL,1),
	(1359,'en_US','','userPending','action','error','Invalid action. Must be approve or reject only',NULL,1),
	(1360,'en_US','','userPending','action_approve','label','APPROVE',NULL,1),
	(1361,'en_US','','userPending','action_reject','label','REJECT',NULL,1),
	(1362,'en_US','','userPending','address','label','Street',NULL,1),
	(1363,'en_US','','userPending','address','metaposition','40',NULL,1),
	(1364,'en_US','','userPending','approved','label','User account application successfully approved',NULL,1),
	(1365,'en_US','','userPending','building_room','constraint','NotEmpty',NULL,1),
	(1366,'en_US','','userPending','building_room','error','Room cannot be empty',NULL,1),
	(1367,'en_US','','userPending','building_room','label','Building / Room',NULL,1),
	(1368,'en_US','','userPending','building_room','metaposition','30',NULL,1),
	(1369,'en_US','','userPending','captcha','error','Captcha text incorrect',NULL,1),
	(1370,'en_US','','userPending','captcha','label','Captcha text',NULL,1),
	(1371,'en_US','','userPending','email','label','Email',NULL,1),
	(1372,'en_US','','userPending','email','error','Must be correctly formatted',NULL,1),
	(1373,'en_US','','userPending','emailconfirmed','label','Your email address is confirmed and your principal investigator has been emailed to request confirmation of your eligibility to join their lab. Once your PI approves your request, you will receive an email informing you of this fact and you will be permitted to log in. You are advised to contact them to request they do this if your account does not become activated in good time.',NULL,1),
	(1374,'en_US','','userPending','emailsent','label','Thank you for your account request. You have been sent an email with instructions as to how to confirm your email address. You will be unable to log in until you confirm your email address.',NULL,1),
	(1375,'en_US','','userPending','email_exists','error','Email already exists in the database',NULL,1),
	(1376,'en_US','','userPending','external_authentication','error','Authentication Failed (Login Name or Password incorrect)',NULL,1),
	(1377,'en_US','','userPending','fax','label','Fax',NULL,1),
	(1378,'en_US','','userPending','fax','metaposition','100',NULL,1),
	(1379,'en_US','','userPending','firstName','label','First Name',NULL,1),
	(1380,'en_US','','userPending','firstName','error','First Name cannot be empty',NULL,1),
	(1381,'en_US','','userPending','form_instructions','label','Please fill out the form below, making sure to complete all required fields marked with an asterisk',NULL,1),
	(1382,'en_US','','userPending','labid_mismatch','error','Lab id mismatch with user-pending id',NULL,1),
	(1383,'en_US','','userPending','lastName','label','Last Name',NULL,1),
	(1384,'en_US','','userPending','lastName','error','Last Name cannot be empty',NULL,1),
	(1385,'en_US','','userPending','locale','constraint','NotEmpty',NULL,1),
	(1386,'en_US','','userPending','locale','label','Locale',NULL,1),
	(1387,'en_US','','userPending','locale','error','A locale must be selected',NULL,1),
	(1388,'en_US','','userPending','login','label','Login',NULL,1),
	(1389,'en_US','','userPending','login','error','Login cannot be empty',NULL,1),
	(1390,'en_US','','userPending','login_exists','error','Login name already exists in the database',NULL,1),
	(1391,'en_US','','userPending','login_malformed','error','Contains invalid characters',NULL,1),
	(1392,'en_US','','userPending','no_pending_users','label','There are currently no pending users awaiting approval',NULL,1),
	(1393,'en_US','','userPending','page','label','New User',NULL,1),
	(1394,'en_US','','userPending','password','label','Password',NULL,1),
	(1395,'en_US','','userPending','password','error','Password cannot be empty',NULL,1),
	(1396,'en_US','','userPending','password2','label','Re-confirm Password',NULL,1),
	(1397,'en_US','','userPending','password2','error','Re-confirm password cannot be empty',NULL,1),
	(1398,'en_US','','userPending','password_invalid','error','Password must be at least 8 characters, containing only letters and numbers, with at least one letter and number',NULL,1),
	(1399,'en_US','','userPending','password_mismatch','error','The two entries for your password are NOT identical',NULL,1),
	(1400,'en_US','','userPending','phone','label','Phone',NULL,1),
	(1401,'en_US','','userPending','phone','error','Phone cannot be empty',NULL,1),
	(1402,'en_US','','userPending','phone','constraint','NotEmpty',NULL,1),
	(1403,'en_US','','userPending','phone','metaposition','90',NULL,1),
	(1404,'en_US','','userPending','primaryuserid','label','PI Wasp Username',NULL,1),
	(1405,'en_US','','userPending','primaryuserid','error','PI Wasp Username cannot be empty',NULL,1),
	(1406,'en_US','','userPending','primaryuserid','constraint','isValidPiId',NULL,1),
	(1407,'en_US','','userPending','primaryuserid','metaposition','1',NULL,1),
	(1408,'en_US','','userPending','primaryuserid_notvalid','error','Not an active registered PI Username',NULL,1),
	(1409,'en_US','','userPending','rejected','label','User account application successfully rejected',NULL,1),
	(1410,'en_US','','userPending','status_not_pending','error','Pending user is already approved or rejected',NULL,1),
	(1411,'en_US','','userPending','submit','label','Apply for Account',NULL,1),
	(1412,'en_US','','userPending','title','label','Title',NULL,1),
	(1413,'en_US','','userPending','title','error','Title cannot be empty',NULL,1),
	(1414,'en_US','','userPending','title','constraint','NotEmpty',NULL,1),
	(1415,'en_US','','userPending','title','control','select:Prof:Prof;Dr:Dr;Mr:Mr;Ms:Ms;Mrs:Mrs;Miss:Miss',NULL,1),
	(1416,'en_US','','userPending','title','metaposition','5',NULL,1),
	(1417,'en_US','','wasp','authentication','label','WASP',NULL,1),
	(1418,'en_US','','wasp','default_select','label','-- select --',NULL,1),
	(1419,'en_US','','wasp','unexpected_error','error','Unexpected error. Last command was NOT executed.',NULL,1),
	(1420,'en_US','','wasp','parseint_error','error','Parameter was unexpectedly not an integer.',NULL,1),
	(1421,'en_US','','wasp','permission_error','error','Insufficient privileges.',NULL,1),
	(1422,'en_US','','wasp','integration_message_send','error','Failed to send update message',NULL,1),
	(1423,'en_US','','workflow','cancel','label','Cancel',NULL,1),
	(1424,'en_US','','workflow','configure','label','Resources and Software',NULL,1),
	(1425,'en_US','','workflow','isActive','label','Is Active?',NULL,1),
	(1426,'en_US','','workflow','listname','label','List of Workflows',NULL,1),
	(1427,'en_US','','workflow','missing_resource_type','error','At least one option must be checked for each resource or software type',NULL,1),
	(1428,'en_US','','workflow','name','label','Workflow Name',NULL,1),
	(1429,'en_US','','workflow','non-configured_parameter','error','At least one option must be checked for each parameter presented for a selected resource',NULL,1),
	(1430,'en_US','','workflow','submit','label','Save Choices',NULL,1),
	(1431,'en_US','','workflow','workflowId','label','Workflow Id',NULL,1),
	(1432,'en_US','','contentTypeMap','arc','data','application/octet-stream',NULL,1),
	(1433,'en_US','','contentTypeMap','bin','data','application/octet-stream',NULL,1),
	(1434,'en_US','','contentTypeMap','dump','data','application/octet-stream',NULL,1),
	(1435,'en_US','','contentTypeMap','dvi','data','application/x-dvi',NULL,1),
	(1436,'en_US','','contentTypeMap','etx','data','text/x-setext',NULL,1),
	(1437,'en_US','','contentTypeMap','exe','data','application/octet-stream',NULL,1),
	(1438,'en_US','','contentTypeMap','fpix','data','image/vnd.fpx',NULL,1),
	(1439,'en_US','','contentTypeMap','fpx','data','image/vnd.fpx',NULL,1),
	(1440,'en_US','','contentTypeMap','gz','data','application/octet-stream',NULL,1),
	(1441,'en_US','','contentTypeMap','hqx','data','application/octet-stream',NULL,1),
	(1442,'en_US','','contentTypeMap','ief','data','image/ief',NULL,1),
	(1443,'en_US','','contentTypeMap','latex','data','application/x-latex',NULL,1),
	(1444,'en_US','','contentTypeMap','lib','data','application/octet-stream',NULL,1),
	(1445,'en_US','','contentTypeMap','man','data','application/x-troff-man',NULL,1),
	(1446,'en_US','','contentTypeMap','me','data','application/x-troff-me',NULL,1),
	(1447,'en_US','','contentTypeMap','mime','data','message/rfc822',NULL,1),
	(1448,'en_US','','contentTypeMap','mov','data','video/quicktime',NULL,1),
	(1449,'en_US','','contentTypeMap','movie','data','video/x-sgi-movie',NULL,1),
	(1450,'en_US','','contentTypeMap','ms','data','application/x-troff-ms',NULL,1),
	(1451,'en_US','','contentTypeMap','mv','data','video/x-sgi-movie',NULL,1),
	(1452,'en_US','','contentTypeMap','obj','data','application/octet-stream',NULL,1),
	(1453,'en_US','','contentTypeMap','oda','data','application/oda',NULL,1),
	(1454,'en_US','','contentTypeMap','pbm','data','image/x-portable-bitmap',NULL,1),
	(1455,'en_US','','contentTypeMap','pdf','data','application/pdf',NULL,1),
	(1456,'en_US','','contentTypeMap','pgm','data','image/x-portable-graymap',NULL,1),
	(1457,'en_US','','contentTypeMap','pnm','data','image/x-portable-anymap',NULL,1),
	(1458,'en_US','','contentTypeMap','ppm','data','image/x-portable-pixmap',NULL,1),
	(1459,'en_US','','contentTypeMap','qt','data','video/quicktime',NULL,1),
	(1460,'en_US','','contentTypeMap','ras','data','image/x-cmu-rast',NULL,1),
	(1461,'en_US','','contentTypeMap','rgb','data','image/x-rgb',NULL,1),
	(1462,'en_US','','contentTypeMap','roff','data','application/x-troff',NULL,1),
	(1463,'en_US','','contentTypeMap','saveme','data','application/octet-stream',NULL,1),
	(1464,'en_US','','contentTypeMap','src','data','application/x-wais-source',NULL,1),
	(1465,'en_US','','contentTypeMap','t','data','application/x-troff',NULL,1),
	(1466,'en_US','','contentTypeMap','tex','data','application/x-tex',NULL,1),
	(1467,'en_US','','contentTypeMap','texi','data','application/x-texinfo',NULL,1),
	(1468,'en_US','','contentTypeMap','texinfo','data','application/x-texinfo',NULL,1),
	(1469,'en_US','','contentTypeMap','tr','data','application/x-troff',NULL,1),
	(1470,'en_US','','contentTypeMap','tsv','data','text/tab-separated-values',NULL,1),
	(1471,'en_US','','contentTypeMap','wsrc','data','application/x-wais-source',NULL,1),
	(1472,'en_US','','contentTypeMap','xbm','data','image/x-xbitmap',NULL,1),
	(1473,'en_US','','contentTypeMap','xml','data','application/xml',NULL,1),
	(1474,'en_US','','contentTypeMap','xpm','data','image/x-xbitmap',NULL,1),
	(1475,'en_US','','contentTypeMap','xwd','data','image/x-xwindowdump',NULL,1),
	(1476,'en_US','','contentTypeMap','zip','data','application/octet-stream',NULL,1),
	(1477,'en_US','','wasp','isAuthenticationExternal','data','FALSE',NULL,1),
	(1478,'en_US','','pageTitle','genericDnaSeq/description','label','Generic DNA Seq Plugin Description',NULL,1),
	(1479,'en_US','','genericDnaSeq','maintext','label','If you can read this text your plugin is successfully installed',NULL,1),
	(1480,'en_US',NULL,'bowtieAligner','mismatches','constraint','NotEmpty','2012-12-20 11:03:29',0),
	(1481,'en_US',NULL,'bowtieAligner','mismatches','metaposition','10','2012-12-20 11:03:29',0),
	(1482,'en_US',NULL,'bowtieAligner','mismatches','type','INTEGER','2012-12-20 11:03:29',0),
	(1483,'en_US',NULL,'bowtieAligner','mismatches','range','0:3','2012-12-20 11:03:29',0),
	(1484,'en_US',NULL,'bowtieAligner','mismatches','default','2','2012-12-20 11:03:29',0),
	(1485,'en_US',NULL,'bowtieAligner','mismatches','label','Number of mismatches (0-3)','2012-12-20 11:03:29',0),
	(1486,'en_US',NULL,'bowtieAligner','mismatches','error','A value for number of mismatches must be specified','2012-12-20 11:03:29',0),
	(1487,'en_US',NULL,'bowtieAligner','seedLength','constraint','NotEmpty','2012-12-20 11:03:29',0),
	(1488,'en_US',NULL,'bowtieAligner','seedLength','metaposition','20','2012-12-20 11:03:29',0),
	(1489,'en_US',NULL,'bowtieAligner','seedLength','type','NUMBER','2012-12-20 11:03:29',0),
	(1490,'en_US',NULL,'bowtieAligner','seedLength','range','5:1000','2012-12-20 11:03:29',0),
	(1491,'en_US',NULL,'bowtieAligner','seedLength','default','32','2012-12-20 11:03:29',0),
	(1492,'en_US',NULL,'bowtieAligner','seedLength','label','Seed Length','2012-12-20 11:03:29',0),
	(1493,'en_US',NULL,'bowtieAligner','seedLength','error','A value for seed length must be specified','2012-12-20 11:03:29',0),
	(1494,'en_US',NULL,'bowtieAligner','reportAlignmentNum','constraint','NotEmpty','2012-12-20 11:03:29',0),
	(1495,'en_US',NULL,'bowtieAligner','reportAlignmentNum','metaposition','30','2012-12-20 11:03:29',0),
	(1496,'en_US',NULL,'bowtieAligner','reportAlignmentNum','type','INTEGER','2012-12-20 11:03:29',0),
	(1497,'en_US',NULL,'bowtieAligner','reportAlignmentNum','default','1','2012-12-20 11:03:29',0),
	(1498,'en_US',NULL,'bowtieAligner','reportAlignmentNum','label','Number of Alignments to Report','2012-12-20 11:03:29',0),
	(1499,'en_US',NULL,'bowtieAligner','reportAlignmentNum','error','A value for number of alignments must be specified','2012-12-20 11:03:29',0),
	(1500,'en_US',NULL,'bowtieAligner','discardThreshold','constraint','NotEmpty','2012-12-20 11:03:29',0),
	(1501,'en_US',NULL,'bowtieAligner','discardThreshold','metaposition','40','2012-12-20 11:03:29',0),
	(1502,'en_US',NULL,'bowtieAligner','discardThreshold','type','INTEGER','2012-12-20 11:03:29',0),
	(1503,'en_US',NULL,'bowtieAligner','discardThreshold','default','1','2012-12-20 11:03:29',0),
	(1504,'en_US',NULL,'bowtieAligner','discardThreshold','label','Discard if more than how many alignments?','2012-12-20 11:03:30',0),
	(1505,'en_US',NULL,'bowtieAligner','discardThreshold','error','A value for the discard threshold must be specified','2012-12-20 11:03:30',0),
	(1506,'en_US',NULL,'bowtieAligner','isBest','constraint','NotEmpty','2012-12-20 11:03:30',0),
	(1507,'en_US',NULL,'bowtieAligner','isBest','metaposition','50','2012-12-20 11:03:30',0),
	(1508,'en_US',NULL,'bowtieAligner','isBest','default','yes','2012-12-20 11:03:30',0),
	(1509,'en_US',NULL,'bowtieAligner','isBest','label','report only best alignments?','2012-12-20 11:03:30',0),
	(1510,'en_US',NULL,'bowtieAligner','isBest','control','select:yes:yes;no:no','2012-12-20 11:03:30',0),
	(1511,'en_US',NULL,'bowtieAligner','isBest','error','A value must be selected','2012-12-20 11:03:30',0),
	(1512,'en_US',NULL,'genericBiomolecule','species','constraint','NotEmpty','2012-12-20 11:03:30',0),
	(1513,'en_US',NULL,'genericBiomolecule','species','metaposition','10','2012-12-20 11:03:30',0),
	(1514,'en_US',NULL,'genericBiomolecule','species','label','Species','2012-12-20 11:03:30',0),
	(1515,'en_US',NULL,'genericBiomolecule','species','control','select:Human:Human;Mouse:Mouse','2012-12-20 11:03:30',0),
	(1516,'en_US',NULL,'genericBiomolecule','species','error','You must select a species','2012-12-20 11:03:30',0),
	(1517,'en_US',NULL,'genericLibrary','concentration','constraint','NotEmpty','2012-12-20 11:03:30',0),
	(1518,'en_US',NULL,'genericLibrary','concentration','metaposition','10','2012-12-20 11:03:30',0),
	(1519,'en_US',NULL,'genericLibrary','concentration','type','NUMBER','2012-12-20 11:03:30',0),
	(1520,'en_US',NULL,'genericLibrary','concentration','label','Concentration (ng/?l)','2012-12-20 11:19:22',0),
	(1521,'en_US',NULL,'genericLibrary','concentration','error','You must provide a concentration','2012-12-20 11:03:30',0),
	(1522,'en_US',NULL,'genericLibrary','volume','constraint','NotEmpty','2012-12-20 11:03:30',0),
	(1523,'en_US',NULL,'genericLibrary','volume','metaposition','20','2012-12-20 11:03:30',0),
	(1524,'en_US',NULL,'genericLibrary','volume','type','NUMBER','2012-12-20 11:03:30',0),
	(1525,'en_US',NULL,'genericLibrary','volume','label','Volume (?l)','2012-12-20 11:19:22',0),
	(1526,'en_US',NULL,'genericLibrary','volume','error','You must provide a volume','2012-12-20 11:03:30',0),
	(1527,'en_US',NULL,'genericLibrary','buffer','constraint','NotEmpty','2012-12-20 11:03:30',0),
	(1528,'en_US',NULL,'genericLibrary','buffer','metaposition','30','2012-12-20 11:03:30',0),
	(1529,'en_US',NULL,'genericLibrary','buffer','label','Buffer','2012-12-20 11:03:30',0),
	(1530,'en_US',NULL,'genericLibrary','buffer','control','select:TE:TE;Water:Water','2012-12-20 11:03:30',0),
	(1531,'en_US',NULL,'genericLibrary','buffer','error','You must select a buffer type','2012-12-20 11:03:30',0),
	(1532,'en_US',NULL,'genericLibrary','adaptorset','constraint','NotEmpty','2012-12-20 11:03:30',0),
	(1533,'en_US',NULL,'genericLibrary','adaptorset','metaposition','40','2012-12-20 11:03:30',0),
	(1534,'en_US',NULL,'genericLibrary','adaptorset','label','Adaptor Set','2012-12-20 11:03:30',0),
	(1535,'en_US',NULL,'genericLibrary','adaptorset','control','select:${adaptorsets}:adaptorsetId:name','2012-12-20 11:03:30',0),
	(1536,'en_US',NULL,'genericLibrary','adaptorset','error','You must choose an adaptor set','2012-12-20 11:03:30',0),
	(1537,'en_US',NULL,'genericLibrary','adaptor','constraint','NotEmpty','2012-12-20 11:03:30',0),
	(1538,'en_US',NULL,'genericLibrary','adaptor','metaposition','50','2012-12-20 11:03:30',0),
	(1539,'en_US',NULL,'genericLibrary','adaptor','label','Adaptor','2012-12-20 11:03:30',0),
	(1540,'en_US',NULL,'genericLibrary','adaptor','control','select:${adaptors}:adaptorId:name','2012-12-20 11:03:30',0),
	(1541,'en_US',NULL,'genericLibrary','adaptor','error','You must choose an adaptor','2012-12-20 11:03:30',0),
	(1542,'en_US',NULL,'genericLibrary','size','constraint','NotEmpty','2012-12-20 11:03:30',0),
	(1543,'en_US',NULL,'genericLibrary','size','metaposition','60','2012-12-20 11:03:30',0),
	(1544,'en_US',NULL,'genericLibrary','size','type','NUMBER','2012-12-20 11:03:30',0),
	(1545,'en_US',NULL,'genericLibrary','size','label','Library Size','2012-12-20 11:03:30',0),
	(1546,'en_US',NULL,'genericLibrary','size','error','You must specify the library size','2012-12-20 11:03:30',0),
	(1547,'en_US',NULL,'genericLibrary','sizeSd','constraint','NotEmpty','2012-12-20 11:03:30',0),
	(1548,'en_US',NULL,'genericLibrary','sizeSd','metaposition','70','2012-12-20 11:03:30',0),
	(1549,'en_US',NULL,'genericLibrary','sizeSd','type','NUMBER','2012-12-20 11:03:30',0),
	(1550,'en_US',NULL,'genericLibrary','sizeSd','label','Library Size SD','2012-12-20 11:03:30',0),
	(1551,'en_US',NULL,'genericLibrary','sizeSd','error','You must specify the library size standard deviation','2012-12-20 11:03:30',0),
	(1552,'en_US',NULL,'macsPeakcaller','pValueCutoff','metaposition','10','2012-12-20 11:03:30',0),
	(1553,'en_US',NULL,'macsPeakcaller','pValueCutoff','type','NUMBER','2012-12-20 11:03:30',0),
	(1554,'en_US',NULL,'macsPeakcaller','pValueCutoff','default','100000','2012-12-20 11:03:30',0),
	(1555,'en_US',NULL,'macsPeakcaller','pValueCutoff','label','p Value Cutoff','2012-12-20 11:03:30',0),
	(1556,'en_US',NULL,'macsPeakcaller','bandwidth','metaposition','20','2012-12-20 11:03:30',0),
	(1557,'en_US',NULL,'macsPeakcaller','bandwidth','type','NUMBER','2012-12-20 11:03:30',0),
	(1558,'en_US',NULL,'macsPeakcaller','bandwidth','range','0:5000','2012-12-20 11:03:30',0),
	(1559,'en_US',NULL,'macsPeakcaller','bandwidth','default','300','2012-12-20 11:03:30',0),
	(1560,'en_US',NULL,'macsPeakcaller','bandwidth','label','Bandwidth','2012-12-20 11:03:30',0),
	(1561,'en_US',NULL,'macsPeakcaller','genomeSize','metaposition','30','2012-12-20 11:03:30',0),
	(1562,'en_US',NULL,'macsPeakcaller','genomeSize','type','NUMBER','2012-12-20 11:03:30',0),
	(1563,'en_US',NULL,'macsPeakcaller','genomeSize','default','1000000000','2012-12-20 11:03:30',0),
	(1564,'en_US',NULL,'macsPeakcaller','genomeSize','label','Effective Genome Size','2012-12-20 11:03:30',0),
	(1565,'en_US',NULL,'macsPeakcaller','keepDup','metaposition','40','2012-12-20 11:03:30',0),
	(1566,'en_US',NULL,'macsPeakcaller','keepDup','default','no','2012-12-20 11:03:30',0),
	(1567,'en_US',NULL,'macsPeakcaller','keepDup','label','Keep Duplicates?','2012-12-20 11:03:30',0),
	(1568,'en_US',NULL,'macsPeakcaller','keepDup','control','select:yes:yes;no:no','2012-12-20 11:03:30',0),
	(1569,'en_US',NULL,'bisulseqPipeline','SeqMode','metaposition','10','2012-12-20 11:03:30',0),
	(1570,'en_US',NULL,'bisulseqPipeline','SeqMode','default','no','2012-12-20 11:03:30',0),
	(1571,'en_US',NULL,'bisulseqPipeline','SeqMode','label','Is Reduced Representation Bisul-Seq?','2012-12-20 11:03:30',0),
	(1572,'en_US',NULL,'bisulseqPipeline','SeqMode','control','select:yes:yes;no:no','2012-12-20 11:03:30',0),
	(1573,'en_US',NULL,'bisulseqPipeline','NumMismatch','metaposition','20','2012-12-20 11:03:30',0),
	(1574,'en_US',NULL,'bisulseqPipeline','NumMismatch','type','NUMBER','2012-12-20 11:03:30',0),
	(1575,'en_US',NULL,'bisulseqPipeline','NumMismatch','range','2:15','2012-12-20 11:03:30',0),
	(1576,'en_US',NULL,'bisulseqPipeline','NumMismatch','default','2','2012-12-20 11:03:30',0),
	(1577,'en_US',NULL,'bisulseqPipeline','NumMismatch','label','Number of mismatch allowed','2012-12-20 11:03:30',0),
	(1578,'en_US',NULL,'bisulseqPipeline','ReportMode','metaposition','30','2012-12-20 11:03:30',0),
	(1579,'en_US',NULL,'bisulseqPipeline','ReportMode','default','yes','2012-12-20 11:03:30',0),
	(1580,'en_US',NULL,'bisulseqPipeline','ReportMode','label','Only report unique hits?','2012-12-20 11:03:30',0),
	(1581,'en_US',NULL,'bisulseqPipeline','ReportMode','control','select:yes:yes;no:no','2012-12-20 11:03:30',0),
	(1582,'en_US',NULL,'bisulseqPipeline','TrimQ','metaposition','40','2012-12-20 11:03:30',0),
	(1583,'en_US',NULL,'bisulseqPipeline','TrimQ','type','NUMBER','2012-12-20 11:03:30',0),
	(1584,'en_US',NULL,'bisulseqPipeline','TrimQ','range','0:30','2012-12-20 11:03:30',0),
	(1585,'en_US',NULL,'bisulseqPipeline','TrimQ','default','0','2012-12-20 11:03:30',0),
	(1586,'en_US',NULL,'bisulseqPipeline','TrimQ','label','Quality threshold in trimming','2012-12-20 11:03:30',0),
	(1587,'en_US',NULL,'bisulseqPipeline','MapFormard','metaposition','50','2012-12-20 11:03:30',0),
	(1588,'en_US',NULL,'bisulseqPipeline','MapFormard','default','yes','2012-12-20 11:03:30',0),
	(1589,'en_US',NULL,'bisulseqPipeline','MapFormard','label','Only map to forward strands?','2012-12-20 11:03:30',0),
	(1590,'en_US',NULL,'bisulseqPipeline','MapFormard','control','select:yes:yes;no:no','2012-12-20 11:03:30',0),
	(1591,'en_US',NULL,'genericDna','concentration','constraint','NotEmpty','2012-12-20 11:03:30',0),
	(1592,'en_US',NULL,'genericDna','concentration','metaposition','10','2012-12-20 11:03:30',0),
	(1593,'en_US',NULL,'genericDna','concentration','type','NUMBER','2012-12-20 11:03:30',0),
	(1594,'en_US',NULL,'genericDna','concentration','label','Concentration (ng/?l)','2012-12-20 11:19:22',0),
	(1595,'en_US',NULL,'genericDna','concentration','error','You must provide a concentration','2012-12-20 11:03:30',0),
	(1596,'en_US',NULL,'genericDna','volume','constraint','NotEmpty','2012-12-20 11:03:30',0),
	(1597,'en_US',NULL,'genericDna','volume','metaposition','20','2012-12-20 11:03:30',0),
	(1598,'en_US',NULL,'genericDna','volume','type','NUMBER','2012-12-20 11:03:30',0),
	(1599,'en_US',NULL,'genericDna','volume','label','Volume (?l)','2012-12-20 11:19:22',0),
	(1600,'en_US',NULL,'genericDna','volume','error','You must provide a volume','2012-12-20 11:03:30',0),
	(1601,'en_US',NULL,'genericDna','buffer','constraint','NotEmpty','2012-12-20 11:03:30',0),
	(1602,'en_US',NULL,'genericDna','buffer','metaposition','30','2012-12-20 11:03:30',0),
	(1603,'en_US',NULL,'genericDna','buffer','label','Buffer','2012-12-20 11:03:30',0),
	(1604,'en_US',NULL,'genericDna','buffer','control','select:TE:TE;Water:Water','2012-12-20 11:03:30',0),
	(1605,'en_US',NULL,'genericDna','buffer','error','You must select a buffer type','2012-12-20 11:03:30',0),
	(1606,'en_US',NULL,'genericDna','A260_280','constraint','NotEmpty','2012-12-20 11:03:30',0),
	(1607,'en_US',NULL,'genericDna','A260_280','metaposition','40','2012-12-20 11:03:30',0),
	(1608,'en_US',NULL,'genericDna','A260_280','type','NUMBER','2012-12-20 11:03:30',0),
	(1609,'en_US',NULL,'genericDna','A260_280','label','A260/280','2012-12-20 11:03:30',0),
	(1610,'en_US',NULL,'genericDna','A260_280','error','You must provide an A260/280 reading','2012-12-20 11:03:30',0),
	(1611,'en_US',NULL,'genericDna','A260_230','constraint','NotEmpty','2012-12-20 11:03:30',0),
	(1612,'en_US',NULL,'genericDna','A260_230','metaposition','50','2012-12-20 11:03:30',0),
	(1613,'en_US',NULL,'genericDna','A260_230','type','NUMBER','2012-12-20 11:03:30',0),
	(1614,'en_US',NULL,'genericDna','A260_230','label','A260/230','2012-12-20 11:03:30',0),
	(1615,'en_US',NULL,'genericDna','A260_230','error','You must provide an A260/230 reading','2012-12-20 11:03:30',0),
	(1616,'en_US',NULL,'bisulseqDna','fragmentSize','constraint','NotEmpty','2012-12-20 11:03:30',0),
	(1617,'en_US',NULL,'bisulseqDna','fragmentSize','metaposition','10','2012-12-20 11:03:30',0),
	(1618,'en_US',NULL,'bisulseqDna','fragmentSize','type','NUMBER','2012-12-20 11:03:30',0),
	(1619,'en_US',NULL,'bisulseqDna','fragmentSize','label','Average Fragmentation Size','2012-12-20 11:03:30',0),
	(1620,'en_US',NULL,'bisulseqDna','fragmentSize','error','You must provide a fragmentSize','2012-12-20 11:03:30',0),
	(1621,'en_US',NULL,'bisulseqDna','fragmentSizeSD','constraint','NotEmpty','2012-12-20 11:03:30',0),
	(1622,'en_US',NULL,'bisulseqDna','fragmentSizeSD','metaposition','20','2012-12-20 11:03:30',0),
	(1623,'en_US',NULL,'bisulseqDna','fragmentSizeSD','type','NUMBER','2012-12-20 11:03:30',0),
	(1624,'en_US',NULL,'bisulseqDna','fragmentSizeSD','label','Fragmentation Size Std. Dev.','2012-12-20 11:03:30',0),
	(1625,'en_US',NULL,'bisulseqDna','fragmentSizeSD','error','You must provide a standard deviation','2012-12-20 11:03:30',0),
	(1626,'en_US',NULL,'bisulseqDna','antibody','metaposition','30','2012-12-20 11:03:30',0),
	(1627,'en_US',NULL,'bisulseqDna','antibody','label','Antibody','2012-12-20 11:03:30',0),
	(1628,'en_US',NULL,'bisulSeq','workflow','label','BISUL Seq','2012-12-20 11:03:30',0),
	(1629,'en_US',NULL,'bisulSeq','jobsubmit/modifymeta','label','Modify BisulSeq Metadata','2012-12-20 11:03:30',0),
	(1630,'en_US',NULL,'bisulSeq','jobsubmit/resource/mps','label','MPS Sequencer Options','2012-12-20 11:03:30',0),
	(1631,'en_US',NULL,'bisulSeq','jobsubmit/samples','label','Samples','2012-12-20 11:03:30',0),
	(1632,'en_US',NULL,'bisulSeq','jobsubmit/cells','label','DNA Sequencer Cells','2012-12-20 11:03:30',0),
	(1633,'en_US',NULL,'bisulSeq','jobsubmit/software/bisulseqPipeline','label','BISUL-seq Pipeline Selection','2012-12-20 11:03:30',0),
	(1634,'en_US',NULL,'bisulSeq','jobsubmit/verify','label','Verify Submission','2012-12-20 11:03:30',0),
	(1635,'en_US',NULL,'chipseqDna','fragmentSize','constraint','NotEmpty','2012-12-20 11:03:30',0),
	(1636,'en_US',NULL,'chipseqDna','fragmentSize','metaposition','10','2012-12-20 11:03:30',0),
	(1637,'en_US',NULL,'chipseqDna','fragmentSize','type','NUMBER','2012-12-20 11:03:30',0),
	(1638,'en_US',NULL,'chipseqDna','fragmentSize','label','Average Fragmentation Size','2012-12-20 11:03:31',0),
	(1639,'en_US',NULL,'chipseqDna','fragmentSize','error','You must provide a fragmentSize','2012-12-20 11:03:31',0),
	(1640,'en_US',NULL,'chipseqDna','fragmentSizeSD','constraint','NotEmpty','2012-12-20 11:03:31',0),
	(1641,'en_US',NULL,'chipseqDna','fragmentSizeSD','metaposition','20','2012-12-20 11:03:31',0),
	(1642,'en_US',NULL,'chipseqDna','fragmentSizeSD','type','NUMBER','2012-12-20 11:03:31',0),
	(1643,'en_US',NULL,'chipseqDna','fragmentSizeSD','label','Fragmentation Size Std. Dev.','2012-12-20 11:03:31',0),
	(1644,'en_US',NULL,'chipseqDna','fragmentSizeSD','error','You must provide a standard deviation','2012-12-20 11:03:31',0),
	(1645,'en_US',NULL,'chipseqDna','antibody','metaposition','30','2012-12-20 11:03:31',0),
	(1646,'en_US',NULL,'chipseqDna','antibody','label','Antibody','2012-12-20 11:03:31',0),
	(1647,'en_US',NULL,'chipSeqPlugin','workflow','label','Chip Seq','2012-12-20 11:03:31',0),
	(1648,'en_US',NULL,'chipSeqPlugin','jobsubmit/modifymeta','label','ModifyChipSeq Metadata','2012-12-20 11:03:31',0),
	(1649,'en_US',NULL,'chipSeqPlugin','jobsubmit/resource/mps','label','MPS Sequencer Options','2012-12-20 11:03:31',0),
	(1650,'en_US',NULL,'chipSeqPlugin','jobsubmit/samples','label','Samples','2012-12-20 11:03:31',0),
	(1651,'en_US',NULL,'chipSeqPlugin','jobsubmit/cells','label','DNA Sequencer Cells','2012-12-20 11:03:31',0),
	(1652,'en_US',NULL,'chipSeqPlugin','jobsubmit/chipSeq/pair','label','IP vs Input Pairings','2012-12-20 11:03:31',0),
	(1653,'en_US',NULL,'chipSeqPlugin','jobsubmit/software/aligner','label','Aligner Selection','2012-12-20 11:03:31',0),
	(1654,'en_US',NULL,'chipSeqPlugin','jobsubmit/software/peakcaller','label','Peak Calling Software Selection','2012-12-20 11:03:31',0),
	(1655,'en_US',NULL,'chipSeqPlugin','jobsubmit/comment','label','Comments','2012-12-20 11:03:31',0),
	(1656,'en_US',NULL,'chipSeqPlugin','jobsubmit/verify','label','Verify Submission','2012-12-20 11:03:31',0),
	(1657,'en_US',NULL,'chipSeqPlugin','pairing_instructions','label','Please select test vs control for all samples to be analyzed in pairs after sequencing.','2012-12-20 11:03:31',0),
	(1658,'en_US',NULL,'chipSeqPlugin','test','label','Test','2012-12-20 11:03:31',0),
	(1659,'en_US',NULL,'chipSeqPlugin','control','label','Control','2012-12-20 11:03:31',0),
	(1660,'en_US',NULL,'genericDnaSeqPlugin','workflow','label','Generic DNA Seq','2012-12-20 11:03:31',0),
	(1661,'en_US',NULL,'genericDnaSeqPlugin','jobsubmit/modifymeta','label','Modify Generic DNA Seq Metadata','2012-12-20 11:03:31',0),
	(1662,'en_US',NULL,'genericDnaSeqPlugin','jobsubmit/resource/mps','label','MPS Sequencer Options','2012-12-20 11:03:31',0),
	(1663,'en_US',NULL,'genericDnaSeqPlugin','jobsubmit/samples','label','Samples','2012-12-20 11:03:31',0),
	(1664,'en_US',NULL,'genericDnaSeqPlugin','jobsubmit/cells','label','DNA Sequencer Cells','2012-12-20 11:03:31',0),
	(1665,'en_US',NULL,'genericDnaSeqPlugin','jobsubmit/software/aligner','label','Aligner Selection','2012-12-20 11:03:31',0),
	(1666,'en_US',NULL,'genericDnaSeqPlugin','jobsubmit/comment','label','Comments','2012-12-20 11:03:31',0),
	(1667,'en_US',NULL,'genericDnaSeqPlugin','jobsubmit/verify','label','Verify Submission','2012-12-20 11:03:31',0),
	(1668,'en_US',NULL,'illuminaHiSeq2000','readLength','constraint','NotEmpty','2012-12-20 11:03:31',0),
	(1669,'en_US',NULL,'illuminaHiSeq2000','readLength','metaposition','10','2012-12-20 11:03:31',0),
	(1670,'en_US',NULL,'illuminaHiSeq2000','readLength','label','Read Length','2012-12-20 11:03:31',0),
	(1671,'en_US',NULL,'illuminaHiSeq2000','readLength','control','select:${resourceOptions.get(readlength)}:value:label','2012-12-20 11:03:31',0),
	(1672,'en_US',NULL,'illuminaHiSeq2000','readLength','error','You must choose a read length','2012-12-20 11:03:31',0),
	(1673,'en_US',NULL,'illuminaHiSeq2000','readType','constraint','NotEmpty','2012-12-20 11:03:31',0),
	(1674,'en_US',NULL,'illuminaHiSeq2000','readType','metaposition','20','2012-12-20 11:03:31',0),
	(1675,'en_US',NULL,'illuminaHiSeq2000','readType','label','Read Type','2012-12-20 11:03:31',0),
	(1676,'en_US',NULL,'illuminaHiSeq2000','readType','control','select:${resourceOptions.get(readType)}:value:label','2012-12-20 11:03:31',0),
	(1677,'en_US',NULL,'illuminaHiSeq2000','readType','error','You must choose a read type','2012-12-20 11:03:31',0),
	(1678,'en_US',NULL,'helptagPipeline','pValueCutoff','metaposition','10','2012-12-20 11:03:31',0),
	(1679,'en_US',NULL,'helptagPipeline','pValueCutoff','type','NUMBER','2012-12-20 11:03:31',0),
	(1680,'en_US',NULL,'helptagPipeline','pValueCutoff','default','100000','2012-12-20 11:03:31',0),
	(1681,'en_US',NULL,'helptagPipeline','pValueCutoff','label','p Value Cutoff','2012-12-20 11:03:31',0),
	(1682,'en_US',NULL,'helptagPipeline','bandwidth','metaposition','20','2012-12-20 11:03:31',0),
	(1683,'en_US',NULL,'helptagPipeline','bandwidth','type','NUMBER','2012-12-20 11:03:31',0),
	(1684,'en_US',NULL,'helptagPipeline','bandwidth','range','0:5000','2012-12-20 11:03:31',0),
	(1685,'en_US',NULL,'helptagPipeline','bandwidth','default','300','2012-12-20 11:03:31',0),
	(1686,'en_US',NULL,'helptagPipeline','bandwidth','label','Bandwidth','2012-12-20 11:03:31',0),
	(1687,'en_US',NULL,'helptagPipeline','genomeSize','metaposition','30','2012-12-20 11:03:31',0),
	(1688,'en_US',NULL,'helptagPipeline','genomeSize','type','NUMBER','2012-12-20 11:03:31',0),
	(1689,'en_US',NULL,'helptagPipeline','genomeSize','default','1000000000','2012-12-20 11:03:31',0),
	(1690,'en_US',NULL,'helptagPipeline','genomeSize','label','Effective Genome Size','2012-12-20 11:03:31',0),
	(1691,'en_US',NULL,'helptagPipeline','keepDup','metaposition','40','2012-12-20 11:03:31',0),
	(1692,'en_US',NULL,'helptagPipeline','keepDup','default','no','2012-12-20 11:03:31',0),
	(1693,'en_US',NULL,'helptagPipeline','keepDup','label','Keep Duplicates?','2012-12-20 11:03:31',0),
	(1694,'en_US',NULL,'helptagPipeline','keepDup','control','select:yes:yes;no:no','2012-12-20 11:03:31',0),
	(1695,'en_US',NULL,'helptagLibrary','fragmentSize','constraint','NotEmpty','2012-12-20 11:03:31',0),
	(1696,'en_US',NULL,'helptagLibrary','fragmentSize','metaposition','10','2012-12-20 11:03:31',0),
	(1697,'en_US',NULL,'helptagLibrary','fragmentSize','type','NUMBER','2012-12-20 11:03:31',0),
	(1698,'en_US',NULL,'helptagLibrary','fragmentSize','label','Average Fragmentation Size','2012-12-20 11:03:31',0),
	(1699,'en_US',NULL,'helptagLibrary','fragmentSize','error','You must provide a fragmentSize','2012-12-20 11:03:31',0),
	(1700,'en_US',NULL,'helptagLibrary','fragmentSizeSD','constraint','NotEmpty','2012-12-20 11:03:31',0),
	(1701,'en_US',NULL,'helptagLibrary','fragmentSizeSD','metaposition','20','2012-12-20 11:03:31',0),
	(1702,'en_US',NULL,'helptagLibrary','fragmentSizeSD','type','NUMBER','2012-12-20 11:03:31',0),
	(1703,'en_US',NULL,'helptagLibrary','fragmentSizeSD','label','Fragmentation Size Std. Dev.','2012-12-20 11:03:31',0),
	(1704,'en_US',NULL,'helptagLibrary','fragmentSizeSD','error','You must provide a standard deviation','2012-12-20 11:03:31',0),
	(1705,'en_US',NULL,'helptagLibrary','antibody','metaposition','30','2012-12-20 11:03:31',0),
	(1706,'en_US',NULL,'helptagLibrary','antibody','label','Antibody','2012-12-20 11:03:31',0),
	(1707,'en_US',NULL,'helpTagPlugin','workflow','label','HELP Tagging','2012-12-20 11:03:31',0),
	(1708,'en_US',NULL,'helpTagPlugin','jobsubmit/modifymeta','label','Modify HelpTag Metadata','2012-12-20 11:03:31',0),
	(1709,'en_US',NULL,'helpTagPlugin','jobsubmit/resource/mps','label','MPS Sequencer Options','2012-12-20 11:03:31',0),
	(1710,'en_US',NULL,'helpTagPlugin','jobsubmit/samples','label','Samples','2012-12-20 11:03:31',0),
	(1711,'en_US',NULL,'helpTagPlugin','jobsubmit/cells','label','DNA Sequencer Cells','2012-12-20 11:03:31',0),
	(1712,'en_US',NULL,'helpTagPlugin','jobsubmit/helpTag/pair','label','HpaII vs MspI Pairings','2012-12-20 11:03:31',0),
	(1713,'en_US',NULL,'helpTagPlugin','jobsubmit/software/aligner','label','Aligner Selection','2012-12-20 11:03:31',0),
	(1714,'en_US',NULL,'helpTagPlugin','jobsubmit/software/helptagPipeline','label','HELP-tag Pipeline Selection','2012-12-20 11:03:31',0),
	(1715,'en_US',NULL,'helpTagPlugin','jobsubmit/verify','label','Verify Submission','2012-12-20 11:03:31',0),
	(1716,'en_US',NULL,'helpTagPlugin','pairing_instructions','label','Please select HpaII vs MspI for all samples to be analyzed in pairs after sequencing.','2012-12-20 11:03:31',0),
	(1717,'en_US',NULL,'helpTagPlugin','test','label','HpaII','2012-12-20 11:03:31',0),
	(1718,'en_US',NULL,'helpTagPlugin','control','label','MspI','2012-12-20 11:03:31',0),
	(1719,'en_US',NULL,'illuminaMiSeqPersonalSequencer','readLength','constraint','NotEmpty','2012-12-20 11:03:32',0),
	(1720,'en_US',NULL,'illuminaMiSeqPersonalSequencer','readLength','metaposition','10','2012-12-20 11:03:32',0),
	(1721,'en_US',NULL,'illuminaMiSeqPersonalSequencer','readLength','label','Read Length','2012-12-20 11:03:32',0),
	(1722,'en_US',NULL,'illuminaMiSeqPersonalSequencer','readLength','control','select:${resourceOptions.get(readlength)}:value:label','2012-12-20 11:03:32',0),
	(1723,'en_US',NULL,'illuminaMiSeqPersonalSequencer','readLength','error','You must choose a read length','2012-12-20 11:03:32',0),
	(1724,'en_US',NULL,'illuminaMiSeqPersonalSequencer','readType','constraint','NotEmpty','2012-12-20 11:03:32',0),
	(1725,'en_US',NULL,'illuminaMiSeqPersonalSequencer','readType','metaposition','20','2012-12-20 11:03:32',0),
	(1726,'en_US',NULL,'illuminaMiSeqPersonalSequencer','readType','label','Read Type','2012-12-20 11:03:32',0),
	(1727,'en_US',NULL,'illuminaMiSeqPersonalSequencer','readType','control','select:${resourceOptions.get(readType)}:value:label','2012-12-20 11:03:32',0),
	(1728,'en_US',NULL,'illuminaMiSeqPersonalSequencer','readType','error','You must choose a read type','2012-12-20 11:03:32',0),
	(1729,'en_US',NULL,'illuminaFlowcellMiSeqV1','cellName','label','Lane','2012-12-20 11:03:32',0),
	(1730,'en_US',NULL,'illuminaFlowcellMiSeqV1','platformUnitName','label','Flow Cell','2012-12-20 11:03:32',0),
	(1731,'en_US',NULL,'illuminaFlowcellV3','cellName','label','Lane','2012-12-20 11:03:32',0),
	(1732,'en_US',NULL,'illuminaFlowcellV3','platformUnitName','label','Flow Cell','2012-12-20 11:03:32',0);

/*!40000 ALTER TABLE `uifield` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user`;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;

INSERT INTO `user` (`userid`, `login`, `email`, `password`, `firstname`, `lastname`, `locale`, `isactive`, `lastupdts`, `lastupduser`)
VALUES
	(1,'super','super@super.com','12dea96fec20593566ab75692c9949596833adc9','Super','User','en_US',1,'2012-06-14 13:49:39',1),
	(2,'jsmith','john.smithl@abc.edu','12dea96fec20593566ab75692c9949596833adc9','John','Smith','en_US',1,'2012-06-14 13:49:50',1),
	(3,'jdoe','j.doe@abc.com','12dea96fec20593566ab75692c9949596833adc9','Joe','Doe','en_US',1,'2012-06-14 13:49:57',1),
	(4,'pwalters','peter@abc2.com','a609590597a1907002ddaa51054df6d6d7758005','Peter','Walters','en_US',1,'2012-06-14 13:50:12',1),
	(5,'ssmythe','ss@abc.com','12dea96fec20593566ab75692c9949596833adc9','Sally','Smythe','en_US',1,'2012-06-14 13:47:46',1),
	(6,'npeters','npeters@abc.com','12dea96fec20593566ab75692c9949596833adc9','Nancy','Peters','en_US',1,'2012-06-14 13:47:53',1),
	(7,'agodwin','andrew.godwin@abc.com','12dea96fec20593566ab75692c9949596833adc9','Andrew','Godwin','en_US',1,'2012-06-14 13:50:32',1),
	(8,'fwilliams','franny@abc.com','12dea96fec20593566ab75692c9949596833adc9','Fran','Williams','en_US',1,'2012-06-14 13:48:05',1),
	(9,'bfish','fishyface@abc.com','12dea96fec20593566ab75692c9949596833adc9','Barrry','Fish','en_US',1,'2012-06-14 14:07:09',1),
	(10,'pliu','Liu@abc.com','12dea96fec20593566ab75692c9949596833adc9','Percy','Liu','en_US',1,'2012-06-14 13:48:31',1),
	(11,'robin','robin@abc.com','12dea96fec20593566ab75692c9949596833adc9','Robin','Lister','en_US',1,'2012-06-13 22:03:43',1),
	(12,'mac','mac@abc.com','12dea96fec20593566ab75692c9949596833adc9','Simon','McDonald','en_US',1,'2012-06-14 13:48:40',1),
	(13,'gdon','gd@abc.com','12dea96fec20593566ab75692c9949596833adc9','Grainne','O\'Donovan','en_US',1,'2012-06-14 14:12:28',0);

/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table user_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user_AUD`;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table usermeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `usermeta`;

CREATE TABLE `usermeta` (
  `usermetaid` int(10) NOT NULL AUTO_INCREMENT,
  `userid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` text,
  `position` int(10) DEFAULT '0',
  `rolevisibility` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`usermetaid`),
  UNIQUE KEY `u_usermeta_k_uid` (`k`,`userid`),
  KEY `fk_usermeta_userid` (`userid`),
  CONSTRAINT `usermeta_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `usermeta` WRITE;
/*!40000 ALTER TABLE `usermeta` DISABLE KEYS */;

INSERT INTO `usermeta` (`usermetaid`, `userid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
VALUES
	(1,2,'user.title','Dr',0,NULL,'2012-05-23 17:23:00',NULL),
	(2,2,'user.institution','Einstein',0,NULL,'2012-05-23 17:23:00',NULL),
	(3,2,'user.departmentId','1',0,NULL,'2012-05-23 17:23:00',NULL),
	(4,2,'user.building_room','Price 954',0,NULL,'2012-05-23 17:23:00',NULL),
	(5,2,'user.address','1301 Morris Park Ave',0,NULL,'2012-05-23 17:23:00',NULL),
	(6,2,'user.city','Bronx',0,NULL,'2012-05-23 17:23:00',NULL),
	(7,2,'user.state','NY',0,NULL,'2012-05-23 17:23:00',NULL),
	(8,2,'user.country','US',0,NULL,'2012-05-23 17:23:00',NULL),
	(9,2,'user.zip','10461',0,NULL,'2012-05-23 17:23:00',NULL),
	(10,2,'user.phone','718-600-1985',0,NULL,'2012-05-23 17:23:00',NULL),
	(11,2,'user.fax','',0,NULL,'2012-05-23 17:23:00',NULL),
	(12,3,'user.title','Ms',0,NULL,'2012-05-30 16:15:28',NULL),
	(13,3,'user.institution','Einstein',0,NULL,'2012-05-30 16:15:28',NULL),
	(14,3,'user.departmentId','1',0,NULL,'2012-05-30 16:15:28',NULL),
	(15,3,'user.building_room','Price 1003',0,NULL,'2012-05-30 16:15:28',NULL),
	(16,3,'user.address','1301 Morris Park Ave.',0,NULL,'2012-05-30 16:15:28',NULL),
	(17,3,'user.city','Bronx',0,NULL,'2012-05-30 16:15:28',NULL),
	(18,3,'user.state','NY',0,NULL,'2012-05-30 16:15:28',NULL),
	(19,3,'user.country','US',0,NULL,'2012-05-30 16:15:28',NULL),
	(20,3,'user.zip','10461',0,NULL,'2012-05-30 16:15:28',NULL),
	(21,3,'user.phone','718-600-3465',0,NULL,'2012-05-30 16:15:28',NULL),
	(22,3,'user.fax','',0,NULL,'2012-05-30 16:15:28',NULL),
	(23,4,'user.title','Ms',0,NULL,'2012-05-30 16:31:54',NULL),
	(24,4,'user.institution','Einstein',0,NULL,'2012-05-30 16:31:54',NULL),
	(25,4,'user.departmentId','1',0,NULL,'2012-05-30 16:31:54',NULL),
	(26,4,'user.building_room','Price 201',0,NULL,'2012-05-30 16:31:54',NULL),
	(27,4,'user.address','1301 Morris Park Ave.',0,NULL,'2012-05-30 16:31:54',NULL),
	(28,4,'user.city','Bronx',0,NULL,'2012-05-30 16:31:54',NULL),
	(29,4,'user.state','NY',0,NULL,'2012-05-30 16:31:54',NULL),
	(30,4,'user.country','US',0,NULL,'2012-05-30 16:31:54',NULL),
	(31,4,'user.zip','10461',0,NULL,'2012-05-30 16:31:54',NULL),
	(32,4,'user.phone','718-600-1100',0,NULL,'2012-05-30 16:31:54',NULL),
	(33,4,'user.fax','',0,NULL,'2012-05-30 16:31:54',NULL),
	(34,5,'user.title','Dr',0,NULL,'2012-05-30 20:22:24',NULL),
	(35,5,'user.institution','Einstein',0,NULL,'2012-05-30 20:22:24',NULL),
	(36,5,'user.departmentId','3',0,NULL,'2012-05-30 20:22:24',NULL),
	(37,5,'user.building_room','Price 2200',0,NULL,'2012-05-30 20:22:24',NULL),
	(38,5,'user.address','1301 Morris Park Ave.',0,NULL,'2012-05-30 20:22:24',NULL),
	(39,5,'user.city','Bronx',0,NULL,'2012-05-30 20:22:24',NULL),
	(40,5,'user.state','NY',0,NULL,'2012-05-30 20:22:24',NULL),
	(41,5,'user.country','US',0,NULL,'2012-05-30 20:22:24',NULL),
	(42,5,'user.zip','10461',0,NULL,'2012-05-30 20:22:24',NULL),
	(43,5,'user.phone','718-123-4567',0,NULL,'2012-05-30 20:22:24',NULL),
	(44,5,'user.fax','',0,NULL,'2012-05-30 20:22:24',NULL),
	(45,5,'user.labName','Greally Lab',0,NULL,'2012-05-30 20:22:24',NULL),
	(46,6,'user.title','Ms',0,NULL,'2012-05-30 21:13:54',NULL),
	(47,6,'user.institution','Einstein',0,NULL,'2012-05-30 21:13:54',NULL),
	(48,6,'user.departmentId','3',0,NULL,'2012-05-30 21:13:54',NULL),
	(49,6,'user.building_room','Price 2220',0,NULL,'2012-05-30 21:13:54',NULL),
	(50,6,'user.address','1301 Morris Park Ave.',0,NULL,'2012-05-30 21:13:54',NULL),
	(51,6,'user.city','Bronx',0,NULL,'2012-05-30 21:13:54',NULL),
	(52,6,'user.state','NY',0,NULL,'2012-05-30 21:13:54',NULL),
	(53,6,'user.country','US',0,NULL,'2012-05-30 21:13:54',NULL),
	(54,6,'user.zip','10461',0,NULL,'2012-05-30 21:13:54',NULL),
	(55,6,'user.phone','718-608-0000',0,NULL,'2012-05-30 21:13:54',NULL),
	(56,6,'user.fax','',0,NULL,'2012-05-30 21:13:54',NULL),
	(57,6,'user.primaryuserid','jgreally',0,NULL,'2012-05-30 21:13:54',NULL),
	(58,7,'user.title','Prof',0,NULL,'2012-05-30 22:03:56',NULL),
	(59,7,'user.institution','Einstein',0,NULL,'2012-05-30 22:03:56',NULL),
	(60,7,'user.departmentId','3',0,NULL,'2012-05-30 22:03:56',NULL),
	(61,7,'user.building_room','Price 933',0,NULL,'2012-05-30 22:03:56',NULL),
	(62,7,'user.address','1301 Morris Park Ave.',0,NULL,'2012-05-30 22:03:56',NULL),
	(63,7,'user.city','Bronx',0,NULL,'2012-05-30 22:03:56',NULL),
	(64,7,'user.state','NY',0,NULL,'2012-05-30 22:03:56',NULL),
	(65,7,'user.country','US',0,NULL,'2012-05-30 22:03:56',NULL),
	(66,7,'user.zip','10461',0,NULL,'2012-05-30 22:03:56',NULL),
	(67,7,'user.phone','718-600-1192',0,NULL,'2012-05-30 22:03:56',NULL),
	(68,7,'user.fax','',0,NULL,'2012-05-30 22:03:56',NULL),
	(69,7,'user.labName','Goldin Lab',0,NULL,'2012-05-30 22:03:56',NULL),
	(70,8,'user.title','Dr',0,NULL,'2012-05-31 13:59:22',NULL),
	(71,8,'user.institution','Einstein',0,NULL,'2012-05-31 13:59:22',NULL),
	(72,8,'user.departmentId','3',0,NULL,'2012-05-31 13:59:22',NULL),
	(73,8,'user.building_room','Price 654',0,NULL,'2012-05-31 13:59:22',NULL),
	(74,8,'user.address','1301 Morris Park Ave.',0,NULL,'2012-05-31 13:59:22',NULL),
	(75,8,'user.city','Bronx',0,NULL,'2012-05-31 13:59:22',NULL),
	(76,8,'user.state','NY',0,NULL,'2012-05-31 13:59:22',NULL),
	(77,8,'user.country','US',0,NULL,'2012-05-31 13:59:22',NULL),
	(78,8,'user.zip','10461',0,NULL,'2012-05-31 13:59:22',NULL),
	(79,8,'user.phone','718-600-0019',0,NULL,'2012-05-31 13:59:22',NULL),
	(80,8,'user.fax','718-600-0020',0,NULL,'2012-05-31 13:59:22',NULL),
	(81,8,'user.labName','Auton Lab',0,NULL,'2012-05-31 13:59:22',NULL),
	(82,9,'user.title','Dr',0,NULL,'2012-05-31 14:00:01',NULL),
	(83,9,'user.institution','NYU Medical',0,NULL,'2012-05-31 14:00:01',NULL),
	(84,9,'user.departmentId','2',0,NULL,'2012-05-31 14:00:01',NULL),
	(85,9,'user.building_room','Hammer 1406',0,NULL,'2012-05-31 14:00:01',NULL),
	(86,9,'user.address','16-50 54th Street',0,NULL,'2012-05-31 14:00:01',NULL),
	(87,9,'user.city','New York',0,NULL,'2012-05-31 14:00:01',NULL),
	(88,9,'user.state','NY',0,NULL,'2012-05-31 14:00:01',NULL),
	(89,9,'user.country','US',0,NULL,'2012-05-31 14:00:01',NULL),
	(90,9,'user.zip','10002',0,NULL,'2012-05-31 14:00:01',NULL),
	(91,9,'user.phone','212-445-2345',0,NULL,'2012-05-31 14:00:01',NULL),
	(92,9,'user.fax','',0,NULL,'2012-05-31 14:00:01',NULL),
	(93,9,'user.labName','Trokie Lab',0,NULL,'2012-05-31 14:00:01',NULL),
	(94,10,'user.title','Dr',0,NULL,'2012-05-31 14:02:26',NULL),
	(95,10,'user.institution','Einstein',0,NULL,'2012-05-31 14:02:26',NULL),
	(96,10,'user.departmentId','3',0,NULL,'2012-05-31 14:02:26',NULL),
	(97,10,'user.building_room','Price 2222',0,NULL,'2012-05-31 14:02:26',NULL),
	(98,10,'user.address','1301 Morris Park Ave',0,NULL,'2012-05-31 14:02:26',NULL),
	(99,10,'user.city','Bronx',0,NULL,'2012-05-31 14:02:26',NULL),
	(100,10,'user.state','NY',0,NULL,'2012-05-31 14:02:26',NULL),
	(101,10,'user.country','US',0,NULL,'2012-05-31 14:02:26',NULL),
	(102,10,'user.zip','10461',0,NULL,'2012-05-31 14:02:26',NULL),
	(103,10,'user.phone','718-500-6696',0,NULL,'2012-05-31 14:02:26',NULL),
	(104,10,'user.fax','718-500-6697',0,NULL,'2012-05-31 14:02:26',NULL),
	(105,10,'user.primaryuserid','jgreally',0,NULL,'2012-05-31 14:02:26',NULL),
	(106,11,'user.title','Mr',0,NULL,'2012-05-31 14:07:26',NULL),
	(107,11,'user.institution','Einstein',0,NULL,'2012-05-31 14:07:26',NULL),
	(108,11,'user.departmentId','1',0,NULL,'2012-05-31 14:07:26',NULL),
	(109,11,'user.building_room','Price 1357',0,NULL,'2012-05-31 14:07:26',NULL),
	(110,11,'user.address','1301 Morris Park Ave.',0,NULL,'2012-05-31 14:07:26',NULL),
	(111,11,'user.city','Bronx',0,NULL,'2012-05-31 14:07:26',NULL),
	(112,11,'user.state','NY',0,NULL,'2012-05-31 14:07:26',NULL),
	(113,11,'user.country','US',0,NULL,'2012-05-31 14:07:26',NULL),
	(114,11,'user.zip','10461',0,NULL,'2012-05-31 14:07:26',NULL),
	(115,11,'user.phone','718-600-4533',0,NULL,'2012-05-31 14:07:26',NULL),
	(116,11,'user.fax','',0,NULL,'2012-05-31 14:07:26',NULL),
	(117,12,'user.title','Prof',0,NULL,'2012-05-31 14:15:29',NULL),
	(118,12,'user.institution','Einstein',0,NULL,'2012-05-31 14:15:29',NULL),
	(119,12,'user.departmentId','3',0,NULL,'2012-05-31 14:15:29',NULL),
	(120,12,'user.building_room','Price 222',0,NULL,'2012-05-31 14:15:29',NULL),
	(121,12,'user.address','1301 Morris Park Ave',0,NULL,'2012-05-31 14:15:29',NULL),
	(122,12,'user.city','Bronx',0,NULL,'2012-05-31 14:15:29',NULL),
	(123,12,'user.state','NY',0,NULL,'2012-05-31 14:15:29',NULL),
	(124,12,'user.country','US',0,NULL,'2012-05-31 14:15:29',NULL),
	(125,12,'user.zip','10461',0,NULL,'2012-05-31 14:15:29',NULL),
	(126,12,'user.phone','617-600-1313',0,NULL,'2012-05-31 14:15:29',NULL),
	(127,12,'user.fax','',0,NULL,'2012-05-31 14:15:29',NULL),
	(128,12,'user.primaryuserid','agoldin',0,NULL,'2012-05-31 14:15:29',NULL),
	(129,1,'user.title','Dr',0,NULL,'2012-06-14 13:49:39',NULL),
	(130,1,'user.institution','Einstein',0,NULL,'2012-06-14 13:49:39',NULL),
	(131,1,'user.departmentId','1',0,NULL,'2012-06-14 13:49:39',NULL),
	(132,1,'user.building_room','N/A',0,NULL,'2012-06-14 13:49:39',NULL),
	(133,1,'user.address','N/A',0,NULL,'2012-06-14 13:49:39',NULL),
	(134,1,'user.city','N/A',0,NULL,'2012-06-14 13:49:39',NULL),
	(135,1,'user.state','NY',0,NULL,'2012-06-14 13:49:39',NULL),
	(136,1,'user.country','US',0,NULL,'2012-06-14 13:49:39',NULL),
	(137,1,'user.zip','N/A',0,NULL,'2012-06-14 13:49:39',NULL),
	(138,1,'user.phone','N/A',0,NULL,'2012-06-14 13:49:39',NULL),
	(139,1,'user.fax','N/A',0,NULL,'2012-06-14 13:49:39',NULL),
	(140,13,'user.title','Miss',0,NULL,'2012-06-14 14:11:40',NULL),
	(141,13,'user.institution','Einstein',0,NULL,'2012-06-14 14:11:40',NULL),
	(142,13,'user.departmentId','1',0,NULL,'2012-06-14 14:11:40',NULL),
	(143,13,'user.building_room','4333',0,NULL,'2012-06-14 14:11:40',NULL),
	(144,13,'user.address','Chanin',0,NULL,'2012-06-14 14:11:40',NULL),
	(145,13,'user.city','Bronx',0,NULL,'2012-06-14 14:11:40',NULL),
	(146,13,'user.state','NY',0,NULL,'2012-06-14 14:11:40',NULL),
	(147,13,'user.country','US',0,NULL,'2012-06-14 14:11:40',NULL),
	(148,13,'user.zip','10461',0,NULL,'2012-06-14 14:11:40',NULL),
	(149,13,'user.phone','718-600-4455',0,NULL,'2012-06-14 14:11:40',NULL),
	(150,13,'user.fax','',0,NULL,'2012-06-14 14:11:40',NULL);

/*!40000 ALTER TABLE `usermeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table usermeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `usermeta_AUD`;

CREATE TABLE `usermeta_AUD` (
  `userMetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  PRIMARY KEY (`userMetaId`,`REV`),
  KEY `FKB38AAA21DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table userpasswordauth
# ------------------------------------------------------------

DROP TABLE IF EXISTS `userpasswordauth`;

CREATE TABLE `userpasswordauth` (
  `userid` int(10) NOT NULL,
  `authcode` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`userid`),
  UNIQUE KEY `u_userpasswordauth` (`authcode`),
  CONSTRAINT `userpasswordauth_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `user` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table userpasswordauth_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `userpasswordauth_AUD`;

CREATE TABLE `userpasswordauth_AUD` (
  `UserId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `authcode` varchar(255) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  PRIMARY KEY (`UserId`,`REV`),
  KEY `FK3CB5DCDFDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table userpending
# ------------------------------------------------------------

DROP TABLE IF EXISTS `userpending`;

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



# Dump of table userpending_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `userpending_AUD`;

CREATE TABLE `userpending_AUD` (
  `userPendingId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `labid` int(11) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  `locale` varchar(255) DEFAULT NULL,
  `login` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`userPendingId`,`REV`),
  KEY `FK51166B3DDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table userpendingmeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `userpendingmeta`;

CREATE TABLE `userpendingmeta` (
  `userpendingmetaid` int(10) NOT NULL AUTO_INCREMENT,
  `userpendingid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` text,
  `position` int(10) DEFAULT '0',
  `rolevisibility` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`userpendingmetaid`),
  UNIQUE KEY `u_userpendingmeta_k_lid` (`k`,`userpendingid`),
  KEY `fk_userpendingmeta_userpendingid` (`userpendingid`),
  CONSTRAINT `userpendingmeta_ibfk_1` FOREIGN KEY (`userpendingid`) REFERENCES `userpending` (`userpendingid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table userpendingmeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `userpendingmeta_AUD`;

CREATE TABLE `userpendingmeta_AUD` (
  `userPendingMetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `userpendingid` int(11) DEFAULT NULL,
  PRIMARY KEY (`userPendingMetaId`,`REV`),
  KEY `FK12A060C2DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table userrole
# ------------------------------------------------------------

DROP TABLE IF EXISTS `userrole`;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `userrole` WRITE;
/*!40000 ALTER TABLE `userrole` DISABLE KEYS */;

INSERT INTO `userrole` (`userroleid`, `userid`, `roleid`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,11,'2012-05-23 15:55:46',1),
	(2,2,1,'2012-05-23 19:25:50',1),
	(4,11,3,'2012-06-14 13:43:46',1),
	(5,4,5,'2012-06-14 13:44:54',1);

/*!40000 ALTER TABLE `userrole` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table userrole_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `userrole_AUD`;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;



# Dump of table workflow
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflow`;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `workflow` WRITE;
/*!40000 ALTER TABLE `workflow` DISABLE KEYS */;

INSERT INTO `workflow` (`workflowid`, `iname`, `name`, `createts`, `isactive`, `lastupdts`, `lastupduser`)
VALUES
	(1,'bisulSeq','BISUL Seq','2012-12-20 11:03:30',1,'2012-12-20 11:03:30',0),
	(2,'chipSeqPlugin','ChIP Seq','2012-12-20 11:03:31',1,'2012-12-20 11:03:31',0),
	(3,'genericDnaSeqPlugin','Generic DNA Seq','2012-12-20 11:03:31',1,'2012-12-20 11:03:31',0),
	(4,'helpTagPlugin','HELP Tagging','2012-12-20 11:03:31',1,'2012-12-20 11:03:31',0);

/*!40000 ALTER TABLE `workflow` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflow_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflow_AUD`;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `workflow_AUD` WRITE;
/*!40000 ALTER TABLE `workflow_AUD` DISABLE KEYS */;

INSERT INTO `workflow_AUD` (`workflowId`, `REV`, `REVTYPE`, `createts`, `iname`, `isactive`, `lastupdts`, `lastupduser`, `name`)
VALUES
	(1,20,0,'2012-12-20 11:03:30','bisulSeq',1,'2012-12-20 11:03:30',0,'BISUL Seq'),
	(2,24,0,'2012-12-20 11:03:31','chipSeqPlugin',1,'2012-12-20 11:03:31',0,'ChIP Seq'),
	(3,25,0,'2012-12-20 11:03:31','genericDnaSeqPlugin',1,'2012-12-20 11:03:31',0,'Generic DNA Seq'),
	(4,30,0,'2012-12-20 11:03:31','helpTagPlugin',1,'2012-12-20 11:03:31',0,'HELP Tagging');

/*!40000 ALTER TABLE `workflow_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowmeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowmeta`;

CREATE TABLE `workflowmeta` (
  `workflowmetaid` int(10) NOT NULL AUTO_INCREMENT,
  `workflowid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` text,
  `position` int(10) DEFAULT '0',
  `rolevisibility` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`workflowmetaid`),
  UNIQUE KEY `u_workflowmeta_k_wid` (`k`,`workflowid`),
  KEY `fk_workflowmeta_workflowid` (`workflowid`),
  CONSTRAINT `workflowmeta_ibfk_1` FOREIGN KEY (`workflowid`) REFERENCES `workflow` (`workflowid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `workflowmeta` WRITE;
/*!40000 ALTER TABLE `workflowmeta` DISABLE KEYS */;

INSERT INTO `workflowmeta` (`workflowmetaid`, `workflowid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,'workflow.jobFlowBatchJob','default.waspJob.jobflow.v1',0,NULL,'2012-12-20 11:03:30',NULL),
	(2,1,'workflow.submitpageflow','/jobsubmit/modifymeta/{n};/jobsubmit/resource/mps/{n};/jobsubmit/samples/{n};/jobsubmit/cells/{n};/jobsubmit/software/bisulseqPipeline/{n};/jobsubmit/submit/{n};/jobsubmit/ok',0,NULL,'2012-12-20 11:03:30',NULL),
	(3,2,'workflow.jobFlowBatchJob','default.waspJob.jobflow.v1',0,NULL,'2012-12-20 11:03:31',NULL),
	(4,2,'workflow.submitpageflow','/jobsubmit/modifymeta/{n};/jobsubmit/resource/mps/{n};/jobsubmit/samples/{n};/jobsubmit/cells/{n};/jobsubmit/chipSeq/pair/{n};/jobsubmit/software/aligner/{n};/jobsubmit/software/peakcaller/{n};/jobsubmit/comment/{n};/jobsubmit/verify/{n};/jobsubmit/submit/{n};/jobsubmit/ok',0,NULL,'2012-12-20 11:03:31',NULL),
	(5,3,'workflow.jobFlowBatchJob','default.waspJob.jobflow.v1',0,NULL,'2012-12-20 11:03:31',NULL),
	(6,3,'workflow.submitpageflow','/jobsubmit/modifymeta/{n};/jobsubmit/resource/mps/{n};/jobsubmit/samples/{n};/jobsubmit/cells/{n};/jobsubmit/software/aligner/{n};/jobsubmit/comment/{n};/jobsubmit/verify/{n};/jobsubmit/submit/{n};/jobsubmit/ok',0,NULL,'2012-12-20 11:03:31',NULL),
	(7,4,'workflow.jobFlowBatchJob','default.waspJob.jobflow.v1',0,NULL,'2012-12-20 11:03:31',NULL),
	(8,4,'workflow.submitpageflow','/jobsubmit/modifymeta/{n};/jobsubmit/resource/mps/{n};/jobsubmit/samples/{n};/jobsubmit/cells/{n};/jobsubmit/helpTag/pair/{n};/jobsubmit/software/aligner/{n};/jobsubmit/software/helptagPipeline/{n};/jobsubmit/verify/{n};/jobsubmit/submit/{n};/jobsubmit/ok',0,NULL,'2012-12-20 11:03:31',NULL);

/*!40000 ALTER TABLE `workflowmeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowmeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowmeta_AUD`;

CREATE TABLE `workflowmeta_AUD` (
  `workflowMetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `workflowid` int(11) DEFAULT NULL,
  PRIMARY KEY (`workflowMetaId`,`REV`),
  KEY `FKB48E6215DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `workflowmeta_AUD` WRITE;
/*!40000 ALTER TABLE `workflowmeta_AUD` DISABLE KEYS */;

INSERT INTO `workflowmeta_AUD` (`workflowMetaId`, `REV`, `REVTYPE`, `workflowid`)
VALUES
	(1,20,0,1),
	(2,20,0,1),
	(3,24,0,2),
	(4,24,0,2),
	(5,25,0,3),
	(6,25,0,3),
	(7,30,0,4),
	(8,30,0,4);

/*!40000 ALTER TABLE `workflowmeta_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowresourcecategory
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowresourcecategory`;

CREATE TABLE `workflowresourcecategory` (
  `workflowresourcecategoryid` int(10) NOT NULL AUTO_INCREMENT,
  `workflowid` int(10) DEFAULT NULL,
  `resourcecategoryid` int(10) DEFAULT NULL,
  PRIMARY KEY (`workflowresourcecategoryid`),
  UNIQUE KEY `u_workflowresource_wid_rcid` (`workflowid`,`resourcecategoryid`),
  KEY `fk_workflowresourcecategory_rcid` (`resourcecategoryid`),
  CONSTRAINT `workflowresourcecategory_ibfk_1` FOREIGN KEY (`resourcecategoryid`) REFERENCES `resourcecategory` (`resourcecategoryid`),
  CONSTRAINT `workflowresourcecategory_ibfk_2` FOREIGN KEY (`workflowid`) REFERENCES `workflow` (`workflowid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `workflowresourcecategory` WRITE;
/*!40000 ALTER TABLE `workflowresourcecategory` DISABLE KEYS */;

INSERT INTO `workflowresourcecategory` (`workflowresourcecategoryid`, `workflowid`, `resourcecategoryid`)
VALUES
	(1,1,1),
	(2,1,2),
	(3,2,1),
	(4,2,2),
	(5,3,1),
	(6,3,2),
	(7,4,1),
	(8,4,2);

/*!40000 ALTER TABLE `workflowresourcecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowresourcecategory_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowresourcecategory_AUD`;

CREATE TABLE `workflowresourcecategory_AUD` (
  `workflowresourcecategoryId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `resourcecategoryid` int(11) DEFAULT NULL,
  `workflowid` int(11) DEFAULT NULL,
  PRIMARY KEY (`workflowresourcecategoryId`,`REV`),
  KEY `FKFBA5DD1CDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `workflowresourcecategory_AUD` WRITE;
/*!40000 ALTER TABLE `workflowresourcecategory_AUD` DISABLE KEYS */;

INSERT INTO `workflowresourcecategory_AUD` (`workflowresourcecategoryId`, `REV`, `REVTYPE`, `resourcecategoryid`, `workflowid`)
VALUES
	(1,36,0,1,1),
	(2,36,0,2,1),
	(3,37,0,1,2),
	(4,37,0,2,2),
	(5,38,0,1,3),
	(6,38,0,2,3),
	(7,39,0,1,4),
	(8,39,0,2,4);

/*!40000 ALTER TABLE `workflowresourcecategory_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowresourcecategorymeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowresourcecategorymeta`;

CREATE TABLE `workflowresourcecategorymeta` (
  `workflowresourcecategorymetaid` int(10) NOT NULL AUTO_INCREMENT,
  `workflowresourcecategoryid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` text,
  `position` int(10) DEFAULT '0',
  `rolevisibility` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`workflowresourcecategorymetaid`),
  UNIQUE KEY `u_wro_wrcid_k` (`workflowresourcecategoryid`,`k`),
  CONSTRAINT `workflowresourcecategorymeta_ibfk_1` FOREIGN KEY (`workflowresourcecategoryid`) REFERENCES `workflowresourcecategory` (`workflowresourcecategoryid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `workflowresourcecategorymeta` WRITE;
/*!40000 ALTER TABLE `workflowresourcecategorymeta` DISABLE KEYS */;

INSERT INTO `workflowresourcecategorymeta` (`workflowresourcecategorymetaid`, `workflowresourcecategoryid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,'illuminaHiSeq2000.allowableUiField.readType','single:single;paired:paired;',1,NULL,'2012-12-20 11:05:42',1),
	(2,1,'illuminaHiSeq2000.allowableUiField.readlength','100:100;150:150;',2,NULL,'2012-12-20 11:05:42',1),
	(3,2,'illuminaMiSeqPersonalSequencer.allowableUiField.readlength','100:100;150:150;',1,NULL,'2012-12-20 11:05:42',1),
	(4,2,'illuminaMiSeqPersonalSequencer.allowableUiField.readType','single:single;paired:paired;',2,NULL,'2012-12-20 11:05:42',1),
	(5,3,'illuminaHiSeq2000.allowableUiField.readType','single:single;paired:paired;',1,NULL,'2012-12-20 11:06:14',1),
	(6,3,'illuminaHiSeq2000.allowableUiField.readlength','50:50;75:75;100:100;150:150;',2,NULL,'2012-12-20 11:06:14',1),
	(7,4,'illuminaMiSeqPersonalSequencer.allowableUiField.readlength','36:36;50:50;100:100;150:150;',1,NULL,'2012-12-20 11:06:14',1),
	(8,4,'illuminaMiSeqPersonalSequencer.allowableUiField.readType','single:single;paired:paired;',2,NULL,'2012-12-20 11:06:14',1),
	(9,5,'illuminaHiSeq2000.allowableUiField.readType','paired:paired;',1,NULL,'2012-12-20 11:06:30',1),
	(10,5,'illuminaHiSeq2000.allowableUiField.readlength','50:50;75:75;100:100;150:150;',2,NULL,'2012-12-20 11:06:30',1),
	(11,6,'illuminaMiSeqPersonalSequencer.allowableUiField.readlength','25:25;36:36;50:50;100:100;150:150;',1,NULL,'2012-12-20 11:06:30',1),
	(12,6,'illuminaMiSeqPersonalSequencer.allowableUiField.readType','single:single;paired:paired;',2,NULL,'2012-12-20 11:06:30',1),
	(13,7,'illuminaHiSeq2000.allowableUiField.readType','single:single;',1,NULL,'2012-12-20 11:06:43',1),
	(14,7,'illuminaHiSeq2000.allowableUiField.readlength','50:50;75:75;',2,NULL,'2012-12-20 11:06:43',1),
	(15,8,'illuminaMiSeqPersonalSequencer.allowableUiField.readlength','36:36;50:50;',1,NULL,'2012-12-20 11:06:43',1),
	(16,8,'illuminaMiSeqPersonalSequencer.allowableUiField.readType','single:single;',2,NULL,'2012-12-20 11:06:43',1);

/*!40000 ALTER TABLE `workflowresourcecategorymeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowresourcecategorymeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowresourcecategorymeta_AUD`;

CREATE TABLE `workflowresourcecategorymeta_AUD` (
  `workflowresourcecategoryMetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `workflowresourcecategoryid` int(11) DEFAULT NULL,
  PRIMARY KEY (`workflowresourcecategoryMetaId`,`REV`),
  KEY `FK2255CB21DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `workflowresourcecategorymeta_AUD` WRITE;
/*!40000 ALTER TABLE `workflowresourcecategorymeta_AUD` DISABLE KEYS */;

INSERT INTO `workflowresourcecategorymeta_AUD` (`workflowresourcecategoryMetaId`, `REV`, `REVTYPE`, `workflowresourcecategoryid`)
VALUES
	(1,36,0,1),
	(2,36,0,1),
	(3,36,0,2),
	(4,36,0,2),
	(5,37,0,3),
	(6,37,0,3),
	(7,37,0,4),
	(8,37,0,4),
	(9,38,0,5),
	(10,38,0,5),
	(11,38,0,6),
	(12,38,0,6),
	(13,39,0,7),
	(14,39,0,7),
	(15,39,0,8),
	(16,39,0,8);

/*!40000 ALTER TABLE `workflowresourcecategorymeta_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowresourcetype
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowresourcetype`;

CREATE TABLE `workflowresourcetype` (
  `workflowresourcetypeid` int(10) NOT NULL AUTO_INCREMENT,
  `workflowid` int(10) DEFAULT NULL,
  `resourcetypeid` int(10) DEFAULT NULL,
  PRIMARY KEY (`workflowresourcetypeid`),
  UNIQUE KEY `u_workflowresourcetype_wid_trid` (`workflowid`,`resourcetypeid`),
  KEY `fk_workflowresourcetype_trid` (`resourcetypeid`),
  CONSTRAINT `workflowresourcetype_ibfk_1` FOREIGN KEY (`resourcetypeid`) REFERENCES `resourcetype` (`resourcetypeid`),
  CONSTRAINT `workflowresourcetype_ibfk_2` FOREIGN KEY (`workflowid`) REFERENCES `workflow` (`workflowid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `workflowresourcetype` WRITE;
/*!40000 ALTER TABLE `workflowresourcetype` DISABLE KEYS */;

INSERT INTO `workflowresourcetype` (`workflowresourcetypeid`, `workflowid`, `resourcetypeid`)
VALUES
	(1,1,2),
	(2,1,6),
	(4,2,1),
	(3,2,2),
	(5,2,4),
	(7,3,1),
	(6,3,2),
	(9,4,1),
	(8,4,2),
	(10,4,7);

/*!40000 ALTER TABLE `workflowresourcetype` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowresourcetype_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowresourcetype_AUD`;

CREATE TABLE `workflowresourcetype_AUD` (
  `workflowresourcetypeId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `resourcetypeid` int(11) DEFAULT NULL,
  `workflowid` int(11) DEFAULT NULL,
  PRIMARY KEY (`workflowresourcetypeId`,`REV`),
  KEY `FKAD64DFD8DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `workflowresourcetype_AUD` WRITE;
/*!40000 ALTER TABLE `workflowresourcetype_AUD` DISABLE KEYS */;

INSERT INTO `workflowresourcetype_AUD` (`workflowresourcetypeId`, `REV`, `REVTYPE`, `resourcetypeid`, `workflowid`)
VALUES
	(1,20,0,2,1),
	(2,20,0,6,1),
	(3,24,0,2,2),
	(4,24,0,1,2),
	(5,24,0,4,2),
	(6,25,0,2,3),
	(7,25,0,1,3),
	(8,30,0,2,4),
	(9,30,0,1,4),
	(10,30,0,7,4);

/*!40000 ALTER TABLE `workflowresourcetype_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowsamplesubtype
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowsamplesubtype`;

CREATE TABLE `workflowsamplesubtype` (
  `workflowsamplesubtypeid` int(10) NOT NULL AUTO_INCREMENT,
  `workflowid` int(10) DEFAULT NULL,
  `samplesubtypeid` int(10) DEFAULT NULL,
  PRIMARY KEY (`workflowsamplesubtypeid`),
  UNIQUE KEY `u_samplesubtype_wid_stsid` (`workflowid`,`samplesubtypeid`),
  KEY `fk_workflowsamplesubtype_stsid` (`samplesubtypeid`),
  CONSTRAINT `workflowsamplesubtype_ibfk_1` FOREIGN KEY (`samplesubtypeid`) REFERENCES `samplesubtype` (`samplesubtypeid`),
  CONSTRAINT `workflowsamplesubtype_ibfk_2` FOREIGN KEY (`workflowid`) REFERENCES `workflow` (`workflowid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `workflowsamplesubtype` WRITE;
/*!40000 ALTER TABLE `workflowsamplesubtype` DISABLE KEYS */;

INSERT INTO `workflowsamplesubtype` (`workflowsamplesubtypeid`, `workflowid`, `samplesubtypeid`)
VALUES
	(1,1,2),
	(2,1,3),
	(3,1,4),
	(4,2,5),
	(5,2,6),
	(6,2,7),
	(7,4,8);

/*!40000 ALTER TABLE `workflowsamplesubtype` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowsamplesubtype_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowsamplesubtype_AUD`;

CREATE TABLE `workflowsamplesubtype_AUD` (
  `workflowsamplesubtypeId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `samplesubtypeid` int(11) DEFAULT NULL,
  `workflowid` int(11) DEFAULT NULL,
  PRIMARY KEY (`workflowsamplesubtypeId`,`REV`),
  KEY `FKD79E0882DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `workflowsamplesubtype_AUD` WRITE;
/*!40000 ALTER TABLE `workflowsamplesubtype_AUD` DISABLE KEYS */;

INSERT INTO `workflowsamplesubtype_AUD` (`workflowsamplesubtypeId`, `REV`, `REVTYPE`, `samplesubtypeid`, `workflowid`)
VALUES
	(1,20,0,2,1),
	(2,20,0,3,1),
	(3,20,0,4,1),
	(4,24,0,5,2),
	(5,24,0,6,2),
	(6,24,0,7,2),
	(7,30,0,8,4);

/*!40000 ALTER TABLE `workflowsamplesubtype_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowsoftware
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowsoftware`;

CREATE TABLE `workflowsoftware` (
  `workflowsoftwareid` int(10) NOT NULL AUTO_INCREMENT,
  `workflowid` int(10) DEFAULT NULL,
  `softwareid` int(10) DEFAULT NULL,
  PRIMARY KEY (`workflowsoftwareid`),
  UNIQUE KEY `u_workflowsoftware_wid_sid` (`workflowid`,`softwareid`),
  KEY `fk_workflowsoftware_sid` (`softwareid`),
  CONSTRAINT `workflowsoftware_ibfk_1` FOREIGN KEY (`softwareid`) REFERENCES `software` (`softwareid`),
  CONSTRAINT `workflowsoftware_ibfk_2` FOREIGN KEY (`workflowid`) REFERENCES `workflow` (`workflowid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `workflowsoftware` WRITE;
/*!40000 ALTER TABLE `workflowsoftware` DISABLE KEYS */;

INSERT INTO `workflowsoftware` (`workflowsoftwareid`, `workflowid`, `softwareid`)
VALUES
	(1,1,3),
	(2,2,1),
	(3,2,2),
	(4,3,1),
	(5,4,1),
	(6,4,4);

/*!40000 ALTER TABLE `workflowsoftware` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowsoftware_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowsoftware_AUD`;

CREATE TABLE `workflowsoftware_AUD` (
  `workflowSoftwareId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `softwareid` int(11) DEFAULT NULL,
  `workflowid` int(11) DEFAULT NULL,
  PRIMARY KEY (`workflowSoftwareId`,`REV`),
  KEY `FK553B9537DF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

LOCK TABLES `workflowsoftware_AUD` WRITE;
/*!40000 ALTER TABLE `workflowsoftware_AUD` DISABLE KEYS */;

INSERT INTO `workflowsoftware_AUD` (`workflowSoftwareId`, `REV`, `REVTYPE`, `softwareid`, `workflowid`)
VALUES
	(1,36,0,3,1),
	(2,37,0,1,2),
	(3,37,0,2,2),
	(4,38,0,1,3),
	(5,39,0,1,4),
	(6,39,0,4,4);

/*!40000 ALTER TABLE `workflowsoftware_AUD` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowsoftwaremeta
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowsoftwaremeta`;

CREATE TABLE `workflowsoftwaremeta` (
  `workflowsoftwaremetaid` int(10) NOT NULL AUTO_INCREMENT,
  `workflowsoftwareid` int(10) DEFAULT NULL,
  `k` varchar(250) DEFAULT NULL,
  `v` text,
  `position` int(10) DEFAULT '0',
  `rolevisibility` varchar(250) DEFAULT NULL,
  `lastupdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastupduser` int(10) DEFAULT '0',
  PRIMARY KEY (`workflowsoftwaremetaid`),
  UNIQUE KEY `u_wro_wrcid_k` (`workflowsoftwareid`,`k`),
  CONSTRAINT `workflowsoftwaremeta_ibfk_1` FOREIGN KEY (`workflowsoftwareid`) REFERENCES `workflowsoftware` (`workflowsoftwareid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table workflowsoftwaremeta_AUD
# ------------------------------------------------------------

DROP TABLE IF EXISTS `workflowsoftwaremeta_AUD`;

CREATE TABLE `workflowsoftwaremeta_AUD` (
  `workflowsoftwareMetaId` int(11) NOT NULL,
  `REV` int(11) NOT NULL,
  `REVTYPE` tinyint(4) DEFAULT NULL,
  `workflowsoftwareid` int(11) DEFAULT NULL,
  PRIMARY KEY (`workflowsoftwareMetaId`,`REV`),
  KEY `FKCBCBFDBCDF74E053` (`REV`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
