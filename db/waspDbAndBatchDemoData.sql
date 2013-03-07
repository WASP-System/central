# ************************************************************
# Sequel Pro SQL dump
# Version 4004
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.1.66)
# Database: wasp
# Generation Time: 2013-03-05 17:43:30 +0000
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



# Dump of table acct_grantjob
# ------------------------------------------------------------



# Dump of table acct_invoice
# ------------------------------------------------------------



# Dump of table acct_jobquotecurrent
# ------------------------------------------------------------

LOCK TABLES `acct_jobquotecurrent` WRITE;
/*!40000 ALTER TABLE `acct_jobquotecurrent` DISABLE KEYS */;

INSERT INTO `acct_jobquotecurrent` (`id`, `jobid`, `quoteid`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,1,'2012-12-20 11:22:17',1);

/*!40000 ALTER TABLE `acct_jobquotecurrent` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table acct_ledger
# ------------------------------------------------------------



# Dump of table acct_quote
# ------------------------------------------------------------

LOCK TABLES `acct_quote` WRITE;
/*!40000 ALTER TABLE `acct_quote` DISABLE KEYS */;

INSERT INTO `acct_quote` (`id`, `jobid`, `amount`, `userid`, `comment`, `isactive`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,2600.00,NULL,NULL,NULL,'2012-12-20 11:22:17',1);

/*!40000 ALTER TABLE `acct_quote` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table acct_quotemeta
# ------------------------------------------------------------

LOCK TABLES `acct_quotemeta` WRITE;
/*!40000 ALTER TABLE `acct_quotemeta` DISABLE KEYS */;

INSERT INTO `acct_quotemeta` (`id`, `quoteid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,'acctQuote.library_cost','600',0,NULL,'2012-12-20 11:22:17',NULL),
	(2,1,'acctQuote.sample_cost','0',0,NULL,'2012-12-20 11:22:17',NULL),
	(3,1,'acctQuote.lane_cost','2000',0,NULL,'2012-12-20 11:22:17',NULL);

/*!40000 ALTER TABLE `acct_quotemeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table acct_quoteuser
# ------------------------------------------------------------



# Dump of table acct_workflowcost
# ------------------------------------------------------------



# Dump of table adaptor
# ------------------------------------------------------------

LOCK TABLES `adaptor` WRITE;
/*!40000 ALTER TABLE `adaptor` DISABLE KEYS */;

INSERT INTO `adaptor` (`id`, `adaptorsetid`, `iname`, `name`, `sequence`, `barcodesequence`, `barcodenumber`, `isactive`)
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


# Dump of table adaptormeta
# ------------------------------------------------------------



# Dump of table adaptorset
# ------------------------------------------------------------

LOCK TABLES `adaptorset` WRITE;
/*!40000 ALTER TABLE `adaptorset` DISABLE KEYS */;

INSERT INTO `adaptorset` (`id`, `iname`, `name`, `sampletypeid`, `isactive`)
VALUES
	(1,'helptagLibrary','HELP-tag Library',1,1),
	(2,'truseqIndexedDna','TruSEQ INDEXED DNA',2,1);

/*!40000 ALTER TABLE `adaptorset` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table adaptorsetmeta
# ------------------------------------------------------------

LOCK TABLES `adaptorsetmeta` WRITE;
/*!40000 ALTER TABLE `adaptorsetmeta` DISABLE KEYS */;

INSERT INTO `adaptorsetmeta` (`id`, `adaptorsetid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
VALUES
	(1,2,'truseqIndexedDna.truseq','1',1,NULL,'2012-12-20 11:03:31',0);

/*!40000 ALTER TABLE `adaptorsetmeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table adaptorsetresourcecategory
# ------------------------------------------------------------

LOCK TABLES `adaptorsetresourcecategory` WRITE;
/*!40000 ALTER TABLE `adaptorsetresourcecategory` DISABLE KEYS */;

INSERT INTO `adaptorsetresourcecategory` (`id`, `adaptorsetid`, `resourcecategoryid`)
VALUES
	(1,1,1),
	(2,2,1);

/*!40000 ALTER TABLE `adaptorsetresourcecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table barcode
# ------------------------------------------------------------

LOCK TABLES `barcode` WRITE;
/*!40000 ALTER TABLE `barcode` DISABLE KEYS */;

INSERT INTO `barcode` (`id`, `barcode`, `barcodefor`, `isactive`, `lastupdts`, `lastupduser`)
VALUES
	(1,'QER344555',NULL,NULL,'2012-12-20 11:07:21',1),
	(2,'QWE34555',NULL,NULL,'2012-12-20 11:07:41',1),
	(3,'FRE45676678',NULL,NULL,'2012-12-20 11:08:04',1),
	(4,'C126NACXX','WASP',1,'2012-12-20 11:09:20',1),
	(5,'D1884ACXX','WASP',1,'2012-12-20 11:09:48',1),
	(6,'634H7AAXX','WASP',1,'2012-12-20 11:10:27',1);

/*!40000 ALTER TABLE `barcode` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table BATCH_JOB_EXECUTION
# ------------------------------------------------------------

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

LOCK TABLES `BATCH_JOB_EXECUTION_SEQ` WRITE;
/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION_SEQ` DISABLE KEYS */;

INSERT INTO `BATCH_JOB_EXECUTION_SEQ` (`ID`)
VALUES
	(8);

/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION_SEQ` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table BATCH_JOB_INSTANCE
# ------------------------------------------------------------

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

LOCK TABLES `BATCH_JOB_SEQ` WRITE;
/*!40000 ALTER TABLE `BATCH_JOB_SEQ` DISABLE KEYS */;

INSERT INTO `BATCH_JOB_SEQ` (`ID`)
VALUES
	(8);

/*!40000 ALTER TABLE `BATCH_JOB_SEQ` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table BATCH_STEP_EXECUTION
# ------------------------------------------------------------

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

LOCK TABLES `BATCH_STEP_EXECUTION_SEQ` WRITE;
/*!40000 ALTER TABLE `BATCH_STEP_EXECUTION_SEQ` DISABLE KEYS */;

INSERT INTO `BATCH_STEP_EXECUTION_SEQ` (`ID`)
VALUES
	(32);

/*!40000 ALTER TABLE `BATCH_STEP_EXECUTION_SEQ` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table confirmemailauth
# ------------------------------------------------------------



# Dump of table department
# ------------------------------------------------------------

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;

INSERT INTO `department` (`id`, `name`, `isinternal`, `isactive`, `lastupdts`, `lastupduser`)
VALUES
	(1,'Internal - Default Department',1,1,'2012-05-23 15:55:35',1),
	(2,'External - Default Department',0,1,'2012-05-23 15:55:35',1),
	(3,'Genetics',1,1,'2012-06-14 13:47:08',1);

/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table departmentuser
# ------------------------------------------------------------

LOCK TABLES `departmentuser` WRITE;
/*!40000 ALTER TABLE `departmentuser` DISABLE KEYS */;

INSERT INTO `departmentuser` (`id`, `departmentid`, `userid`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,3,'2012-05-30 19:57:15',1),
	(2,3,3,'2012-06-14 13:47:08',1),
	(3,2,13,'2012-06-14 14:13:04',1);

/*!40000 ALTER TABLE `departmentuser` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table file
# ------------------------------------------------------------



# Dump of table filemeta
# ------------------------------------------------------------



# Dump of table filetype
# ------------------------------------------------------------



# Dump of table filetypemeta
# ------------------------------------------------------------



# Dump of table job
# ------------------------------------------------------------

LOCK TABLES `job` WRITE;
/*!40000 ALTER TABLE `job` DISABLE KEYS */;

INSERT INTO `job` (`id`, `labid`, `userid`, `workflowid`, `name`, `createts`, `viewablebylab`, `isactive`, `lastupdts`, `lastupduser`)
VALUES
	(1,2,10,2,'chipseq1','2012-12-20 11:20:04',0,1,'2012-12-20 11:20:04',10),
	(2,2,10,2,'chipseq2','2012-12-20 11:27:07',0,1,'2012-12-20 11:27:07',10);

/*!40000 ALTER TABLE `job` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobcellselection
# ------------------------------------------------------------

LOCK TABLES `jobcellselection` WRITE;
/*!40000 ALTER TABLE `jobcellselection` DISABLE KEYS */;

INSERT INTO `jobcellselection` (`id`, `jobid`, `cellindex`)
VALUES
	(1,1,1),
	(2,2,1),
	(3,2,2);

/*!40000 ALTER TABLE `jobcellselection` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobdraft
# ------------------------------------------------------------

LOCK TABLES `jobdraft` WRITE;
/*!40000 ALTER TABLE `jobdraft` DISABLE KEYS */;

INSERT INTO `jobdraft` (`id`, `labid`, `userid`, `workflowid`, `name`, `createts`, `submittedjobid`, `status`, `lastupdts`, `lastupduser`)
VALUES
	(1,2,10,2,'chipseq1','2012-12-20 11:15:26',1,'SUBMITTED','2012-12-20 11:20:04',10),
	(2,2,10,2,'chipseq2','2012-12-20 11:25:19',2,'SUBMITTED','2012-12-20 11:27:07',10);

/*!40000 ALTER TABLE `jobdraft` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobdraftcellselection
# ------------------------------------------------------------

LOCK TABLES `jobdraftcellselection` WRITE;
/*!40000 ALTER TABLE `jobdraftcellselection` DISABLE KEYS */;

INSERT INTO `jobdraftcellselection` (`id`, `jobdraftid`, `cellindex`)
VALUES
	(1,1,1),
	(2,2,1),
	(3,2,2);

/*!40000 ALTER TABLE `jobdraftcellselection` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobdraftfile
# ------------------------------------------------------------



# Dump of table jobdraftmeta
# ------------------------------------------------------------

LOCK TABLES `jobdraftmeta` WRITE;
/*!40000 ALTER TABLE `jobdraftmeta` DISABLE KEYS */;

INSERT INTO `jobdraftmeta` (`id`, `jobdraftid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
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


# Dump of table jobdraftresourcecategory
# ------------------------------------------------------------

LOCK TABLES `jobdraftresourcecategory` WRITE;
/*!40000 ALTER TABLE `jobdraftresourcecategory` DISABLE KEYS */;

INSERT INTO `jobdraftresourcecategory` (`id`, `jobdraftid`, `resourcecategoryid`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,1,'2012-12-20 11:15:30',10),
	(2,2,1,'2012-12-20 11:25:22',10);

/*!40000 ALTER TABLE `jobdraftresourcecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobdraftsoftware
# ------------------------------------------------------------

LOCK TABLES `jobdraftsoftware` WRITE;
/*!40000 ALTER TABLE `jobdraftsoftware` DISABLE KEYS */;

INSERT INTO `jobdraftsoftware` (`id`, `jobdraftid`, `softwareid`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,1,'2012-12-20 11:18:39',10),
	(2,1,2,'2012-12-20 11:18:43',10),
	(3,2,1,'2012-12-20 11:26:58',10),
	(4,2,2,'2012-12-20 11:27:01',10);

/*!40000 ALTER TABLE `jobdraftsoftware` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobfile
# ------------------------------------------------------------



# Dump of table jobmeta
# ------------------------------------------------------------

LOCK TABLES `jobmeta` WRITE;
/*!40000 ALTER TABLE `jobmeta` DISABLE KEYS */;

INSERT INTO `jobmeta` (`id`, `jobid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
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


# Dump of table jobresourcecategory
# ------------------------------------------------------------

LOCK TABLES `jobresourcecategory` WRITE;
/*!40000 ALTER TABLE `jobresourcecategory` DISABLE KEYS */;

INSERT INTO `jobresourcecategory` (`id`, `jobid`, `resourcecategoryid`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,1,'2012-12-20 11:20:04',10),
	(2,2,1,'2012-12-20 11:27:07',10);

/*!40000 ALTER TABLE `jobresourcecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobsample
# ------------------------------------------------------------

LOCK TABLES `jobsample` WRITE;
/*!40000 ALTER TABLE `jobsample` DISABLE KEYS */;

INSERT INTO `jobsample` (`id`, `jobid`, `sampleid`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,21,'2012-12-20 11:20:04',10),
	(2,1,22,'2012-12-20 11:20:04',10),
	(3,1,23,'2012-12-20 11:20:04',10),
	(4,1,24,'2012-12-20 11:24:31',1),
	(5,2,25,'2012-12-20 11:27:07',10),
	(6,2,26,'2012-12-20 11:27:07',10);

/*!40000 ALTER TABLE `jobsample` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobsamplemeta
# ------------------------------------------------------------



# Dump of table jobsoftware
# ------------------------------------------------------------

LOCK TABLES `jobsoftware` WRITE;
/*!40000 ALTER TABLE `jobsoftware` DISABLE KEYS */;

INSERT INTO `jobsoftware` (`id`, `jobid`, `softwareid`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,1,'2012-12-20 11:20:04',10),
	(2,1,2,'2012-12-20 11:20:04',10),
	(3,2,1,'2012-12-20 11:27:07',10),
	(4,2,2,'2012-12-20 11:27:07',10);

/*!40000 ALTER TABLE `jobsoftware` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobuser
# ------------------------------------------------------------

LOCK TABLES `jobuser` WRITE;
/*!40000 ALTER TABLE `jobuser` DISABLE KEYS */;

INSERT INTO `jobuser` (`id`, `jobid`, `userid`, `roleid`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,10,9,'2012-12-20 11:20:04',10),
	(2,1,5,10,'2012-12-20 11:20:04',10),
	(3,2,10,9,'2012-12-20 11:27:07',10),
	(4,2,5,10,'2012-12-20 11:27:07',10);

/*!40000 ALTER TABLE `jobuser` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table lab
# ------------------------------------------------------------

LOCK TABLES `lab` WRITE;
/*!40000 ALTER TABLE `lab` DISABLE KEYS */;

INSERT INTO `lab` (`id`, `departmentid`, `name`, `primaryuserid`, `isactive`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,'Default lab',1,1,'2012-06-14 14:08:21',1),
	(2,3,'Cancer Genetics',5,1,'2012-06-14 14:07:33',1),
	(3,3,'Godwin Lab',7,1,'2012-06-14 14:07:27',1),
	(4,3,'Williams Lab',8,1,'2012-06-14 14:06:49',1),
	(5,2,'Zebra Fish Lab',9,1,'2012-06-14 14:05:52',1);

/*!40000 ALTER TABLE `lab` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table labmeta
# ------------------------------------------------------------

LOCK TABLES `labmeta` WRITE;
/*!40000 ALTER TABLE `labmeta` DISABLE KEYS */;

INSERT INTO `labmeta` (`id`, `labid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
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


# Dump of table labpending
# ------------------------------------------------------------



# Dump of table labpendingmeta
# ------------------------------------------------------------



# Dump of table labuser
# ------------------------------------------------------------

LOCK TABLES `labuser` WRITE;
/*!40000 ALTER TABLE `labuser` DISABLE KEYS */;

INSERT INTO `labuser` (`id`, `labid`, `userid`, `roleid`, `lastupdts`, `lastupduser`)
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


# Dump of table meta
# ------------------------------------------------------------



# Dump of table project
# ------------------------------------------------------------



# Dump of table resource
# ------------------------------------------------------------

LOCK TABLES `resource` WRITE;
/*!40000 ALTER TABLE `resource` DISABLE KEYS */;

INSERT INTO `resource` (`id`, `resourcecategoryid`, `resourcetypeid`, `iname`, `name`, `isactive`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,2,'Basil','Basil',1,'2012-12-20 11:07:21',1),
	(2,1,2,'Sybil','Sybil',1,'2012-12-20 11:07:41',1),
	(3,2,2,'Manuel','Manuel',1,'2012-12-20 11:08:04',1);

/*!40000 ALTER TABLE `resource` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcebarcode
# ------------------------------------------------------------

LOCK TABLES `resourcebarcode` WRITE;
/*!40000 ALTER TABLE `resourcebarcode` DISABLE KEYS */;

INSERT INTO `resourcebarcode` (`id`, `resourceid`, `barcodeid`)
VALUES
	(1,1,1),
	(2,2,2),
	(3,3,3);

/*!40000 ALTER TABLE `resourcebarcode` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcecategory
# ------------------------------------------------------------

LOCK TABLES `resourcecategory` WRITE;
/*!40000 ALTER TABLE `resourcecategory` DISABLE KEYS */;

INSERT INTO `resourcecategory` (`id`, `resourcetypeid`, `iname`, `name`, `isactive`, `lastupdts`, `lastupduser`)
VALUES
	(1,2,'illuminaHiSeq2000','Illumina HiSeq 2000',1,'2012-12-20 11:03:31',0),
	(2,2,'illuminaMiSeqPersonalSequencer','Illumina MiSeq Personal Sequencer',1,'2012-12-20 11:03:32',0);

/*!40000 ALTER TABLE `resourcecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcecategorymeta
# ------------------------------------------------------------

LOCK TABLES `resourcecategorymeta` WRITE;
/*!40000 ALTER TABLE `resourcecategorymeta` DISABLE KEYS */;

INSERT INTO `resourcecategorymeta` (`id`, `resourcecategoryid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,'illuminaHiSeq2000.allowableUiField.readType','single:single;paired:paired',1,NULL,'2012-12-20 11:03:31',0),
	(2,1,'illuminaHiSeq2000.allowableUiField.readlength','50:50;75:75;100:100;150:150',2,NULL,'2012-12-20 11:03:31',0),
	(3,1,'illuminaHiSeq2000.platformUnitSelector','A:A;B:B',3,NULL,'2012-12-20 11:03:31',0),
	(4,2,'illuminaMiSeqPersonalSequencer.allowableUiField.readType','single:single;paired:paired',1,NULL,'2012-12-20 11:03:32',0),
	(5,2,'illuminaMiSeqPersonalSequencer.allowableUiField.readlength','25:25;36:36;50:50;100:100;150:150',2,NULL,'2012-12-20 11:03:32',0);

/*!40000 ALTER TABLE `resourcecategorymeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcecell
# ------------------------------------------------------------



# Dump of table resourcemeta
# ------------------------------------------------------------

LOCK TABLES `resourcemeta` WRITE;
/*!40000 ALTER TABLE `resourcemeta` DISABLE KEYS */;

INSERT INTO `resourcemeta` (`id`, `resourceid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,'resource.commission_date','2012-10-03',0,NULL,'2012-12-20 11:07:21',NULL),
	(2,1,'resource.decommission_date','',0,NULL,'2012-12-20 11:07:21',NULL),
	(3,2,'resource.commission_date','2012-08-02',0,NULL,'2012-12-20 11:07:41',NULL),
	(4,2,'resource.decommission_date','',0,NULL,'2012-12-20 11:07:41',NULL),
	(5,3,'resource.commission_date','2012-05-24',0,NULL,'2012-12-20 11:08:04',NULL),
	(6,3,'resource.decommission_date','',0,NULL,'2012-12-20 11:08:04',NULL);

/*!40000 ALTER TABLE `resourcemeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcetype
# ------------------------------------------------------------

LOCK TABLES `resourcetype` WRITE;
/*!40000 ALTER TABLE `resourcetype` DISABLE KEYS */;

INSERT INTO `resourcetype` (`id`, `iname`, `name`, `isactive`)
VALUES
	(1,'aligner','Aligner',1),
	(2,'mps','Massively Parallel DNA Sequencer',1),
	(3,'amplicon','DNA Amplicon',1),
	(4,'peakcaller','Peak Caller',1),
	(5,'sequenceRunProcessor','Sequence Run Processor',1),
	(6,'bisulfiteSeqPipeline','Bi-sulphite-seq Pipeline',1),
	(7,'helptagPipeline','HELP-tag Pipeline',1);

/*!40000 ALTER TABLE `resourcetype` ENABLE KEYS */;
UNLOCK TABLES;




# Dump of table role
# ------------------------------------------------------------

LOCK TABLES `wrole` WRITE;
/*!40000 ALTER TABLE `wrole` DISABLE KEYS */;

INSERT INTO `wrole` (`id`, `rolename`, `name`, `domain`)
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

/*!40000 ALTER TABLE `wrole` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table roleset
# ------------------------------------------------------------

LOCK TABLES `roleset` WRITE;
/*!40000 ALTER TABLE `roleset` DISABLE KEYS */;

INSERT INTO `roleset` (`id`, `parentroleid`, `childroleid`)
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


# Dump of table run
# ------------------------------------------------------------



# Dump of table runcell
# ------------------------------------------------------------



# Dump of table runcellfile
# ------------------------------------------------------------



# Dump of table runfile
# ------------------------------------------------------------



# Dump of table runmeta
# ------------------------------------------------------------



# Dump of table sample
# ------------------------------------------------------------

LOCK TABLES `sample` WRITE;
/*!40000 ALTER TABLE `sample` DISABLE KEYS */;

INSERT INTO `sample` (`id`, `parentid`, `sampletypeid`, `samplesubtypeid`, `submitter_labid`, `submitter_userid`, `submitter_jobid`, `isreceived`, `receiver_userid`, `receivedts`, `name`, `isgood`, `isactive`, `lastupdts`, `lastupduser`)
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


# Dump of table samplebarcode
# ------------------------------------------------------------

LOCK TABLES `samplebarcode` WRITE;
/*!40000 ALTER TABLE `samplebarcode` DISABLE KEYS */;

INSERT INTO `samplebarcode` (`id`, `sampleid`, `barcodeid`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,4,'2012-12-20 11:09:20',1),
	(2,10,5,'2012-12-20 11:09:48',1),
	(3,19,6,'2012-12-20 11:10:27',1);

/*!40000 ALTER TABLE `samplebarcode` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sampledraft
# ------------------------------------------------------------

LOCK TABLES `sampledraft` WRITE;
/*!40000 ALTER TABLE `sampledraft` DISABLE KEYS */;

INSERT INTO `sampledraft` (`id`, `sampletypeid`, `samplesubtypeid`, `labid`, `userid`, `jobdraftid`, `sourcesampleid`, `filegroupid`, `name`, `status`, `lastupdts`, `lastupduser`)
VALUES
	(1,2,5,2,10,1,NULL,NULL,'sIP1',NULL,'2012-12-20 11:17:27',10),
	(2,2,5,2,10,1,NULL,NULL,'sIP2',NULL,'2012-12-20 11:17:34',10),
	(3,2,5,2,10,1,NULL,NULL,'sINPUT',NULL,'2012-12-20 11:17:41',10),
	(4,1,6,2,10,2,NULL,NULL,'l1INPUT',NULL,'2012-12-20 11:26:22',10),
	(5,1,6,2,10,2,NULL,NULL,'lIP1',NULL,'2012-12-20 11:26:37',10);

/*!40000 ALTER TABLE `sampledraft` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sampledraftjobdraftcellselection
# ------------------------------------------------------------

LOCK TABLES `sampledraftjobdraftcellselection` WRITE;
/*!40000 ALTER TABLE `sampledraftjobdraftcellselection` DISABLE KEYS */;

INSERT INTO `sampledraftjobdraftcellselection` (`id`, `sampledraftid`, `jobdraftcellselectionid`, `libraryindex`)
VALUES
	(1,1,1,1),
	(2,2,1,2),
	(3,3,1,3),
	(4,4,2,1),
	(5,5,3,1);

/*!40000 ALTER TABLE `sampledraftjobdraftcellselection` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sampledraftmeta
# ------------------------------------------------------------

LOCK TABLES `sampledraftmeta` WRITE;
/*!40000 ALTER TABLE `sampledraftmeta` DISABLE KEYS */;

INSERT INTO `sampledraftmeta` (`id`, `sampledraftid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
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


# Dump of table samplefile
# ------------------------------------------------------------



# Dump of table samplejobcellselection
# ------------------------------------------------------------

LOCK TABLES `samplejobcellselection` WRITE;
/*!40000 ALTER TABLE `samplejobcellselection` DISABLE KEYS */;

INSERT INTO `samplejobcellselection` (`id`, `sampleid`, `jobcellselectionid`, `libraryindex`)
VALUES
	(1,21,1,1),
	(2,22,1,2),
	(3,23,1,3),
	(4,25,2,1),
	(5,26,3,1);

/*!40000 ALTER TABLE `samplejobcellselection` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplelab
# ------------------------------------------------------------



# Dump of table samplemeta
# ------------------------------------------------------------

LOCK TABLES `samplemeta` WRITE;
/*!40000 ALTER TABLE `samplemeta` DISABLE KEYS */;

INSERT INTO `samplemeta` (`id`, `sampleid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
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


# Dump of table samplesource
# ------------------------------------------------------------

LOCK TABLES `samplesource` WRITE;
/*!40000 ALTER TABLE `samplesource` DISABLE KEYS */;

INSERT INTO `samplesource` (`id`, `sampleid`, `indexvalue`, `source_sampleid`, `lastupdts`, `lastupduser`)
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


# Dump of table samplesourcemeta
# ------------------------------------------------------------



# Dump of table samplesubtype
# ------------------------------------------------------------

LOCK TABLES `samplesubtype` WRITE;
/*!40000 ALTER TABLE `samplesubtype` DISABLE KEYS */;

INSERT INTO `samplesubtype` (`id`, `sampletypeid`, `iname`, `name`, `isactive`, `arealist`)
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


# Dump of table samplesubtypemeta
# ------------------------------------------------------------

LOCK TABLES `samplesubtypemeta` WRITE;
/*!40000 ALTER TABLE `samplesubtypemeta` DISABLE KEYS */;

INSERT INTO `samplesubtypemeta` (`id`, `samplesubtypeid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
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


# Dump of table samplesubtyperesourcecategory
# ------------------------------------------------------------

LOCK TABLES `samplesubtyperesourcecategory` WRITE;
/*!40000 ALTER TABLE `samplesubtyperesourcecategory` DISABLE KEYS */;

INSERT INTO `samplesubtyperesourcecategory` (`id`, `samplesubtypeid`, `resourcecategoryid`)
VALUES
	(1,9,2),
	(2,10,1);

/*!40000 ALTER TABLE `samplesubtyperesourcecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sampletype
# ------------------------------------------------------------

LOCK TABLES `sampletype` WRITE;
/*!40000 ALTER TABLE `sampletype` DISABLE KEYS */;

INSERT INTO `sampletype` (`id`, `sampletypecategoryid`, `isactive`, `iname`, `name`)
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


# Dump of table sampletypecategory
# ------------------------------------------------------------

LOCK TABLES `sampletypecategory` WRITE;
/*!40000 ALTER TABLE `sampletypecategory` DISABLE KEYS */;

INSERT INTO `sampletypecategory` (`id`, `iname`, `name`)
VALUES
	(1,'biomaterial','Biomaterial'),
	(2,'hardware','Hardware');

/*!40000 ALTER TABLE `sampletypecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table software
# ------------------------------------------------------------

LOCK TABLES `software` WRITE;
/*!40000 ALTER TABLE `software` DISABLE KEYS */;

INSERT INTO `software` (`id`, `resourcetypeid`, `iname`, `name`, `isactive`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,'bowtieAligner','Bowtie Aligner',1,'2012-12-20 11:03:29',0),
	(2,4,'macsPeakcaller','MACS Peakcaller',1,'2012-12-20 11:03:30',0),
	(3,6,'bisulfiteSeqPipeline','BISUL-seq Pipeline',1,'2012-12-20 11:03:30',0),
	(4,7,'helptagPipeline','HELP-tag Pipeline',1,'2012-12-20 11:03:31',0),
	(5,5,'casava','CASAVA',1,'2012-12-20 11:03:31',0);

/*!40000 ALTER TABLE `software` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table softwaremeta
# ------------------------------------------------------------

LOCK TABLES `softwaremeta` WRITE;
/*!40000 ALTER TABLE `softwaremeta` DISABLE KEYS */;

INSERT INTO `softwaremeta` (`id`, `softwareid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,'bowtieAligner.currentVersion','0.12.7',1,NULL,'2012-12-20 11:03:29',0),
	(2,1,'bowtieAligner.priorVersions','',2,NULL,'2012-12-20 11:03:29',0),
	(3,2,'macsPeakcaller.currentVersion','4.1',1,NULL,'2012-12-20 11:03:30',0),
	(4,2,'macsPeakcaller.priorVersions','',2,NULL,'2012-12-20 11:03:30',0),
	(5,3,'bisulfiteSeqPipeline.currentVersion','1.0',1,NULL,'2012-12-20 11:03:30',0),
	(6,3,'bisulfiteSeqPipeline.priorVersions','',2,NULL,'2012-12-20 11:03:30',0),
	(7,4,'helptagPipeline.currentVersion','1.0',1,NULL,'2012-12-20 11:03:31',0),
	(8,4,'helptagPipeline.priorVersions','',2,NULL,'2012-12-20 11:03:31',0),
	(9,5,'casava.currentVersion','1.8.2',1,NULL,'2012-12-20 11:03:31',0),
	(10,5,'casava.priorVersions','',2,NULL,'2012-12-20 11:03:31',0);

/*!40000 ALTER TABLE `softwaremeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table taskmapping
# ------------------------------------------------------------



# Dump of table uifield
# ------------------------------------------------------------



# Dump of table user
# ------------------------------------------------------------

LOCK TABLES `wuser` WRITE;
/*!40000 ALTER TABLE `wuser` DISABLE KEYS */;

INSERT INTO `wuser` (`id`, `login`, `email`, `password`, `firstname`, `lastname`, `locale`, `isactive`, `lastupdts`, `lastupduser`)
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

/*!40000 ALTER TABLE `wuser` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table usermeta
# ------------------------------------------------------------

LOCK TABLES `usermeta` WRITE;
/*!40000 ALTER TABLE `usermeta` DISABLE KEYS */;

INSERT INTO `usermeta` (`id`, `userid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
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


# Dump of table userpasswordauth
# ------------------------------------------------------------



# Dump of table userpending
# ------------------------------------------------------------



# Dump of table userpendingmeta
# ------------------------------------------------------------



# Dump of table userrole
# ------------------------------------------------------------

LOCK TABLES `userrole` WRITE;
/*!40000 ALTER TABLE `userrole` DISABLE KEYS */;

INSERT INTO `userrole` (`id`, `userid`, `roleid`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,11,'2012-05-23 15:55:46',1),
	(2,2,1,'2012-05-23 19:25:50',1),
	(4,11,3,'2012-06-14 13:43:46',1),
	(5,4,5,'2012-06-14 13:44:54',1);

/*!40000 ALTER TABLE `userrole` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflow
# ------------------------------------------------------------

LOCK TABLES `workflow` WRITE;
/*!40000 ALTER TABLE `workflow` DISABLE KEYS */;

INSERT INTO `workflow` (`id`, `iname`, `name`, `createts`, `isactive`, `lastupdts`, `lastupduser`)
VALUES
	(1,'wasp-bisulfiteSeqPlugin','BISUL Seq','2012-12-20 11:03:30',1,'2012-12-20 11:03:30',0),
	(2,'wasp-chipSeqPlugin','ChIP Seq','2012-12-20 11:03:31',1,'2012-12-20 11:03:31',0),
	(3,'wasp-genericDnaSeqPlugin','Generic DNA Seq','2012-12-20 11:03:31',1,'2012-12-20 11:03:31',0),
	(4,'wasp-helpTagPlugin','HELP Tagging','2012-12-20 11:03:31',1,'2012-12-20 11:03:31',0);

/*!40000 ALTER TABLE `workflow` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowmeta
# ------------------------------------------------------------

LOCK TABLES `workflowmeta` WRITE;
/*!40000 ALTER TABLE `workflowmeta` DISABLE KEYS */;

INSERT INTO `workflowmeta` (`id`, `workflowid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
VALUES
	(1,1,'workflow.jobFlowBatchJob','default.waspJob.jobflow.v1',0,NULL,'2012-12-20 11:03:30',NULL),
	(2,1,'workflow.submitpageflow','/jobsubmit/modifymeta/{n};/jobsubmit/resource/mps/{n};/jobsubmit/samples/{n};/jobsubmit/cells/{n};/jobsubmit/software/bisulfiteSeqPipeline/{n};/jobsubmit/submit/{n};/jobsubmit/ok',0,NULL,'2012-12-20 11:03:30',NULL),
	(3,2,'workflow.jobFlowBatchJob','default.waspJob.jobflow.v1',0,NULL,'2012-12-20 11:03:31',NULL),
	(4,2,'workflow.submitpageflow','/jobsubmit/modifymeta/{n};/jobsubmit/resource/mps/{n};/jobsubmit/samples/{n};/jobsubmit/cells/{n};/jobsubmit/chipSeq/pair/{n};/jobsubmit/software/aligner/{n};/jobsubmit/software/peakcaller/{n};/jobsubmit/comment/{n};/jobsubmit/verify/{n};/jobsubmit/submit/{n};/jobsubmit/ok',0,NULL,'2012-12-20 11:03:31',NULL),
	(5,3,'workflow.jobFlowBatchJob','default.waspJob.jobflow.v1',0,NULL,'2012-12-20 11:03:31',NULL),
	(6,3,'workflow.submitpageflow','/jobsubmit/modifymeta/{n};/jobsubmit/resource/mps/{n};/jobsubmit/samples/{n};/jobsubmit/cells/{n};/jobsubmit/software/aligner/{n};/jobsubmit/comment/{n};/jobsubmit/verify/{n};/jobsubmit/submit/{n};/jobsubmit/ok',0,NULL,'2012-12-20 11:03:31',NULL),
	(7,4,'workflow.jobFlowBatchJob','default.waspJob.jobflow.v1',0,NULL,'2012-12-20 11:03:31',NULL),
	(8,4,'workflow.submitpageflow','/jobsubmit/modifymeta/{n};/jobsubmit/resource/mps/{n};/jobsubmit/samples/{n};/jobsubmit/cells/{n};/jobsubmit/helpTag/pair/{n};/jobsubmit/software/aligner/{n};/jobsubmit/software/helptagPipeline/{n};/jobsubmit/verify/{n};/jobsubmit/submit/{n};/jobsubmit/ok',0,NULL,'2012-12-20 11:03:31',NULL);

/*!40000 ALTER TABLE `workflowmeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowresourcecategory
# ------------------------------------------------------------

LOCK TABLES `workflowresourcecategory` WRITE;
/*!40000 ALTER TABLE `workflowresourcecategory` DISABLE KEYS */;

INSERT INTO `workflowresourcecategory` (`id`, `workflowid`, `resourcecategoryid`)
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


# Dump of table workflowresourcecategorymeta
# ------------------------------------------------------------

LOCK TABLES `workflowresourcecategorymeta` WRITE;
/*!40000 ALTER TABLE `workflowresourcecategorymeta` DISABLE KEYS */;

INSERT INTO `workflowresourcecategorymeta` (`id`, `workflowresourcecategoryid`, `k`, `v`, `position`, `rolevisibility`, `lastupdts`, `lastupduser`)
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


# Dump of table workflowresourcetype
# ------------------------------------------------------------

LOCK TABLES `workflowresourcetype` WRITE;
/*!40000 ALTER TABLE `workflowresourcetype` DISABLE KEYS */;

INSERT INTO `workflowresourcetype` (`id`, `workflowid`, `resourcetypeid`)
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


# Dump of table workflowsamplesubtype
# ------------------------------------------------------------

LOCK TABLES `workflowsamplesubtype` WRITE;
/*!40000 ALTER TABLE `workflowsamplesubtype` DISABLE KEYS */;

INSERT INTO `workflowsamplesubtype` (`id`, `workflowid`, `samplesubtypeid`)
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


# Dump of table workflowsoftware
# ------------------------------------------------------------

LOCK TABLES `workflowsoftware` WRITE;
/*!40000 ALTER TABLE `workflowsoftware` DISABLE KEYS */;

INSERT INTO `workflowsoftware` (`id`, `workflowid`, `softwareid`)
VALUES
	(1,1,3),
	(2,2,1),
	(3,2,2),
	(4,3,1),
	(5,4,1),
	(6,4,4);

/*!40000 ALTER TABLE `workflowsoftware` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowsoftwaremeta
# ------------------------------------------------------------




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
