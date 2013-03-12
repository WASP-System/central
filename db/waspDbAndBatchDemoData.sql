# ************************************************************
# Sequel Pro SQL dump
# Version 4004
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.1.66-log)
# Database: wasp
# Generation Time: 2013-03-12 22:11:35 +0000
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



# Dump of table acct_ledger
# ------------------------------------------------------------



# Dump of table acct_quote
# ------------------------------------------------------------

LOCK TABLES `acct_quote` WRITE;
/*!40000 ALTER TABLE `acct_quote` DISABLE KEYS */;

INSERT INTO `acct_quote` (`id`, `created`, `updated`, `uuid`, `amount`, `comment`, `isactive`, `jobid`, `userid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:22:17','2013-03-12 12:39:41',X'8E7D5D4224494BFAB5EBAC102DC6EB31',2600,NULL,NULL,1,NULL,1);

/*!40000 ALTER TABLE `acct_quote` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table acct_quotemeta
# ------------------------------------------------------------

LOCK TABLES `acct_quotemeta` WRITE;
/*!40000 ALTER TABLE `acct_quotemeta` DISABLE KEYS */;

INSERT INTO `acct_quotemeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `quoteid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:22:17','2013-03-12 18:10:00',X'4183111F83BA4A80B84B6FAC2675E7FD','acctQuote.library_cost',0,NULL,'600',1,NULL),
	(2,'2012-12-20 11:22:17','2013-03-12 18:10:00',X'4CAC1868E9D64F3FBB842A3FE7885D26','acctQuote.sample_cost',0,NULL,'0',1,NULL),
	(3,'2012-12-20 11:22:17','2013-03-12 18:10:00',X'2247B2E13C3B4E20995274F46BB12B63','acctQuote.lane_cost',0,NULL,'2000',1,NULL);

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

INSERT INTO `adaptor` (`id`, `created`, `updated`, `uuid`, `adaptorsetid`, `barcodenumber`, `barcodesequence`, `iname`, `isactive`, `name`, `sequence`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'74CE3F2253344D8E85031666095358F5',1,1,'CGCTGCTG','illuminaHelptagLibrary1',1,'helptag Adaptor','CGCTGCTG',NULL),
	(2,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'A837C8CC868D41FA87A2D0A612C9912B',2,1,'ATCACG','illuminaTrueseqDnaIndexed1',1,'TruSeq Adaptor, Index 1 (ATCACG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(3,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'1ED57115281E490F9D6ADFCDC287EDD1',2,2,'CGATGT','illuminaTrueseqDnaIndexed2',1,'TruSeq Adaptor, Index 2 (CGATGT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(4,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'DD24F801802E40059160FEA94CDA878E',2,3,'TTAGGC','illuminaTrueseqDnaIndexed3',1,'TruSeq Adaptor, Index 3 (TTAGGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(5,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'75C5A4D3C3F546CB9F33B26150CE8EBC',2,4,'TGACCA','illuminaTrueseqDnaIndexed4',1,'TruSeq Adaptor, Index 4 (TGACCA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(6,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'46C2438D72644CD3B3AE4D0177891551',2,5,'ACAGTG','illuminaTrueseqDnaIndexed5',1,'TruSeq Adaptor, Index 5 (ACAGTG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(7,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'ACB825EC1CEF4EA19E3B7B4BEDEFFF44',2,6,'GCCAAT','illuminaTrueseqDnaIndexed6',1,'TruSeq Adaptor, Index 6 (GCCAAT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(8,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'7E2A7308F5B7416A92DABE5EEFFE95AF',2,7,'CAGATC','illuminaTrueseqDnaIndexed7',1,'TruSeq Adaptor, Index 7 (CAGATC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(9,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'BFC01DAF7CA449A8A8C6A922D56171B9',2,8,'ACTTGA','illuminaTrueseqDnaIndexed8',1,'TruSeq Adaptor, Index 8 (ACTTGA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(10,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'775649FC55FC43B3AA42E46C89519F22',2,9,'GATCAG','illuminaTrueseqDnaIndexed9',1,'TruSeq Adaptor, Index 9 (GATCAG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(11,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'DFB519B5DAEC4489BE2C72F43A997A18',2,10,'TAGCTT','illuminaTrueseqDnaIndexed10',1,'TruSeq Adaptor, Index 10 (TAGCTT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(12,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'1F5A641100534A7EA3108CD0CDE121DA',2,11,'GGCTAC','illuminaTrueseqDnaIndexed11',1,'TruSeq Adaptor, Index 11 (GGCTAC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(13,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'C2E47D1ABF1045E7A8329374E85DB2D1',2,12,'CTTGTA','illuminaTrueseqDnaIndexed12',1,'TruSeq Adaptor, Index 12 (CTTGTA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(14,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'29C4FCCBA0154ACEB6C16B5ECAB9F0DA',2,13,'AGTCAA','illuminaTrueseqDnaIndexed13',1,'TruSeq Adaptor, Index 13 (AGTCAA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(15,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'429A5ECE605B4ED3AEA98F825A6F8102',2,14,'AGTTCC','illuminaTrueseqDnaIndexed14',1,'TruSeq Adaptor, Index 14 (AGTTCC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(16,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'65F7417AA9AA443A90A6AEAB0A777470',2,15,'ATGTCA','illuminaTrueseqDnaIndexed15',1,'TruSeq Adaptor, Index 15 (ATGTCA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(17,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'663AF2664CEC4907B1733A69B42F5830',2,16,'CCGTCC','illuminaTrueseqDnaIndexed16',1,'TruSeq Adaptor, Index 16 (CCGTCC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(18,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'54AF4AF9A596433FB6BEF530B0347DC9',2,17,'GTAGAG','illuminaTrueseqDnaIndexed17',1,'TruSeq Adaptor, Index 17 (GTAGAG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(19,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'976447EFE6744C22AD4905AED287D209',2,18,'GTCCGC','illuminaTrueseqDnaIndexed18',1,'TruSeq Adaptor, Index 18 (GTCCGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(20,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'289674BD9F824956887106C292989C68',2,19,'GTGAAA','illuminaTrueseqDnaIndexed19',1,'TruSeq Adaptor, Index 19 (GTGAAA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(21,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'4618F8F9BC6744248BE8C74DD14629B8',2,20,'GTGGCC','illuminaTrueseqDnaIndexed20',1,'TruSeq Adaptor, Index 20 (GTGGCC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(22,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'3C20CB6CAB544DD980FED6243CC072B3',2,21,'GTTTCG','illuminaTrueseqDnaIndexed21',1,'TruSeq Adaptor, Index 21 (GTTTCG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(23,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'2185A85583B44B79935B5CCF2C4FE5FC',2,22,'CGTACG','illuminaTrueseqDnaIndexed22',1,'TruSeq Adaptor, Index 22 (CGTACG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(24,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'F6AD3ECC38A549958710059CFB2BE9CB',2,23,'GAGTGG','illuminaTrueseqDnaIndexed23',1,'TruSeq Adaptor, Index 23 (GAGTGG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(25,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'60A2CFC1592B4FF3AC95AD8AF956934C',2,24,'GGTAGC','illuminaTrueseqDnaIndexed24',1,'TruSeq Adaptor, Index 24 (GGTAGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(26,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'D1F370E031B144FABEC4AB0005D7919B',2,25,'ACTGAT','illuminaTrueseqDnaIndexed25',1,'TruSeq Adaptor, Index 25 (ACTGAT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(27,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'CB50600D2D2D4F4E83E102F7E46A2324',2,26,'ATGAGC','illuminaTrueseqDnaIndexed26',1,'TruSeq Adaptor, Index 26 (ATGAGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(28,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'533B15B322334435B573156477239FA1',2,27,'ATTCCT','illuminaTrueseqDnaIndexed27',1,'TruSeq Adaptor, Index 27 (ATTCCT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(29,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'F24F17CA762C4BC0A01B784806CA4BF2',2,28,'CAAAAG','illuminaTrueseqDnaIndexed28',1,'TruSeq Adaptor, Index 28 (CAAAAG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(30,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'B74DAB4CF9BC497FB2E980B0F6B4879E',2,29,'CAACTA','illuminaTrueseqDnaIndexed29',1,'TruSeq Adaptor, Index 29 (CAACTA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(31,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'1960178394D74B3695E77F493E9B22FC',2,30,'CACCGG','illuminaTrueseqDnaIndexed30',1,'TruSeq Adaptor, Index 30 (CACCGG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(32,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'3F549C17B8744FF1B2817A34B0C6B3B0',2,31,'CACGAT','illuminaTrueseqDnaIndexed31',1,'TruSeq Adaptor, Index 31 (CACGAT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(33,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'6DD39FCB02104E2C8A4A8A2870EE051A',2,32,'CACTCA','illuminaTrueseqDnaIndexed32',1,'TruSeq Adaptor, Index 32 (CACTCA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(34,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'90A3FB32443647DEB082FA43D2B9BBF0',2,33,'CAGGCG','illuminaTrueseqDnaIndexed33',1,'TruSeq Adaptor, Index 33 (CAGGCG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(35,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'9797718DCE15418AA267CDC19C576BB3',2,34,'CATGGC','illuminaTrueseqDnaIndexed34',1,'TruSeq Adaptor, Index 34 (CATGGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(36,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'A5D2DEC7E15E4CEE921CB0376DEBD334',2,35,'CATTTT','illuminaTrueseqDnaIndexed35',1,'TruSeq Adaptor, Index 35 (CATTTT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(37,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'095D77EFEDA0474AA40F74B52F95C397',2,36,'CCAACA','illuminaTrueseqDnaIndexed36',1,'TruSeq Adaptor, Index 36 (CCAACA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(38,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'4F2363E3D68C41B2A21A48FEB470538E',2,37,'CGGAAT','illuminaTrueseqDnaIndexed37',1,'TruSeq Adaptor, Index 37 (CGGAAT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(39,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'420A960043E843F587840ECE72F4C539',2,38,'CTAGCT','illuminaTrueseqDnaIndexed38',1,'TruSeq Adaptor, Index 38 (CTAGCT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(40,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'1C92E9D1CBCA42A88B490DF343D448D0',2,39,'CTATAC','illuminaTrueseqDnaIndexed39',1,'TruSeq Adaptor, Index 39 (CTATAC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(41,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'7C9FF8E58255497A907EBC900E3A37FE',2,40,'CTCAGA','illuminaTrueseqDnaIndexed40',1,'TruSeq Adaptor, Index 40 (CTCAGA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(42,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'A3CE814C316D4FF7B4A0A81E249BF234',2,41,'GACGAC','illuminaTrueseqDnaIndexed41',1,'TruSeq Adaptor, Index 41 (GACGAC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(43,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'F0FF35CA396B4D82B58B47144A1D2D0A',2,42,'TAATCG','illuminaTrueseqDnaIndexed42',1,'TruSeq Adaptor, Index 42 (TAATCG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(44,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'B2693BD950A644DFBF79AD4A8A43AF17',2,43,'TACAGC','illuminaTrueseqDnaIndexed43',1,'TruSeq Adaptor, Index 43 (TACAGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(45,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'A3AFD8440BCB4EC3AE1101385EB6F385',2,44,'TATAAT','illuminaTrueseqDnaIndexed44',1,'TruSeq Adaptor, Index 44 (TATAAT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(46,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'0AC9EFD65DBE41DBB68E11654643F140',2,45,'TCATTC','illuminaTrueseqDnaIndexed45',1,'TruSeq Adaptor, Index 45 (TCATTC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(47,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'FCB575F91A154978B3F902D1888391E0',2,46,'TCCCGA','illuminaTrueseqDnaIndexed46',1,'TruSeq Adaptor, Index 46 (TCCCGA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(48,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'01B47218078741528C565AFEFC7253CA',2,47,'TCGAAG','illuminaTrueseqDnaIndexed47',1,'TruSeq Adaptor, Index 47 (TCGAAG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(49,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'CD9B9B90F34F4181B24501C730C24BD5',2,48,'TCGGCA','illuminaTrueseqDnaIndexed48',1,'TruSeq Adaptor, Index 48 (TCGGCA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL);

/*!40000 ALTER TABLE `adaptor` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table adaptormeta
# ------------------------------------------------------------



# Dump of table adaptorset
# ------------------------------------------------------------

LOCK TABLES `adaptorset` WRITE;
/*!40000 ALTER TABLE `adaptorset` DISABLE KEYS */;

INSERT INTO `adaptorset` (`id`, `created`, `updated`, `uuid`, `iname`, `isactive`, `name`, `sampletypeid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:39','2013-03-12 18:10:04',X'D433BC2F91F9407697F4CBCAC284B511','helptagLibrary',1,'HELP-tag Library',1,NULL),
	(2,'2013-03-12 12:39:39','2013-03-12 18:10:04',X'A62D573D5D454A76B44A9E1CF6263CDE','truseqIndexedDna',1,'TruSEQ INDEXED DNA',2,NULL);

/*!40000 ALTER TABLE `adaptorset` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table adaptorsetmeta
# ------------------------------------------------------------

LOCK TABLES `adaptorsetmeta` WRITE;
/*!40000 ALTER TABLE `adaptorsetmeta` DISABLE KEYS */;

INSERT INTO `adaptorsetmeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `adaptorsetid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:03:31','2013-03-12 12:39:41',X'DB9FDDF3768B455383FB42FBDC215923','truseqIndexedDna.truseq',1,NULL,'1',2,0);

/*!40000 ALTER TABLE `adaptorsetmeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table adaptorsetresourcecategory
# ------------------------------------------------------------

LOCK TABLES `adaptorsetresourcecategory` WRITE;
/*!40000 ALTER TABLE `adaptorsetresourcecategory` DISABLE KEYS */;

INSERT INTO `adaptorsetresourcecategory` (`id`, `created`, `updated`, `uuid`, `adaptorsetid`, `resourcecategoryid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'46BB40EAFEEE4B11BCA6234A272FB9B9',1,1,NULL),
	(2,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'80BE51B417F748188FDD86889B126C86',2,1,NULL),
	(3,'2013-03-12 12:39:46','2013-03-12 18:10:00',X'040B0013AB2E48659D87E672469D6DD4',2,2,NULL);

/*!40000 ALTER TABLE `adaptorsetresourcecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table barcode
# ------------------------------------------------------------

LOCK TABLES `barcode` WRITE;
/*!40000 ALTER TABLE `barcode` DISABLE KEYS */;

INSERT INTO `barcode` (`id`, `created`, `updated`, `uuid`, `barcode`, `barcodefor`, `isactive`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:07:21','2013-03-12 12:39:41',X'E6D4F2E79BEE4C77A21DBA3FC1BF1E1A','QER344555',NULL,NULL,1),
	(2,'2012-12-20 11:07:41','2013-03-12 12:39:41',X'4A6455F0D69E4684A358A9B07188FFEB','QWE34555',NULL,NULL,1),
	(3,'2012-12-20 11:08:04','2013-03-12 12:39:41',X'69CC29C7F5A04B759AF76D34BC9800AD','FRE45676678',NULL,NULL,1),
	(4,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'E9C0F27C4EFF47109210131F8B986CB6','C126NACXX','WASP',1,1),
	(5,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'3CE4D4DD81FA4E439D42F3DA73CB147B','D1884ACXX','WASP',1,1),
	(6,'2012-12-20 11:10:27','2013-03-12 12:39:41',X'0B6E56EC9A5D49F6AF37D6F8BF5213C8','634H7AAXX','WASP',1,1);

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
	(0),
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
	(0),
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
	(0);

/*!40000 ALTER TABLE `BATCH_STEP_EXECUTION_SEQ` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table confirmemailauth
# ------------------------------------------------------------



# Dump of table department
# ------------------------------------------------------------

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;

INSERT INTO `department` (`id`, `created`, `updated`, `uuid`, `isactive`, `isinternal`, `name`, `lastupdatebyuser`)
VALUES
	(1,'2012-05-23 15:55:35','2013-03-12 12:39:41',X'0600D7F8266244A1B6859FB093D53732',1,1,'Internal - Default Department',1),
	(2,'2012-05-23 15:55:35','2013-03-12 12:39:41',X'4F8F79D5CC3A4A63BE2639D92BCF65DC',1,0,'External - Default Department',1),
	(3,'2012-06-14 13:47:08','2013-03-12 12:39:41',X'3FC1CF2A815B40FE924EF12048C3EE3B',1,1,'Genetics',1);

/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table departmentuser
# ------------------------------------------------------------

LOCK TABLES `departmentuser` WRITE;
/*!40000 ALTER TABLE `departmentuser` DISABLE KEYS */;

INSERT INTO `departmentuser` (`id`, `created`, `updated`, `uuid`, `departmentid`, `userid`, `lastupdatebyuser`)
VALUES
	(1,'2012-05-30 19:57:15','2013-03-12 12:39:41',X'B6C57D2B806E4450BB0F8DE05271090D',1,3,1),
	(2,'2012-06-14 13:47:08','2013-03-12 12:39:41',X'A881201ACDE745439CBADDC805D4D8D2',3,3,1),
	(3,'2012-06-14 14:13:04','2013-03-12 12:39:41',X'24F8A6B1AED74C46898601C832289071',2,13,1);

/*!40000 ALTER TABLE `departmentuser` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table file
# ------------------------------------------------------------



# Dump of table filegroup
# ------------------------------------------------------------



# Dump of table filegroup_rel
# ------------------------------------------------------------



# Dump of table filegroupmeta
# ------------------------------------------------------------



# Dump of table filemeta
# ------------------------------------------------------------



# Dump of table filetype
# ------------------------------------------------------------

LOCK TABLES `filetype` WRITE;
/*!40000 ALTER TABLE `filetype` DISABLE KEYS */;

INSERT INTO `filetype` (`id`, `created`, `updated`, `uuid`, `description`, `iname`, `isactive`, `name`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:44','2013-03-12 18:10:03',X'01D299359B9D466F873071F50AA0B326','A human-readable text based file format for storing DNA sequences alongside their quality scores','fastq',1,'FASTQ',NULL),
	(2,'2013-03-12 12:39:44','2013-03-12 18:10:03',X'1720F1FE13DC4F8E837A112B440B0399','Binary version of a Sequence Alignment/MAP (SAM) file. A generic format for storing large nucleotide sequence alignnments','bam',1,'BAM',NULL),
	(3,'2013-03-12 12:39:46','2013-03-12 18:10:05',X'DE177E2D436143CC92FE5313844EBACE','QC files generated by CASAVA for assessing HiSeq flowcell / sequencing quality','waspIlluminaHiseqQcMetrics',1,'Illumina HiSeq QC Metrics',NULL);

/*!40000 ALTER TABLE `filetype` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table filetypemeta
# ------------------------------------------------------------



# Dump of table job
# ------------------------------------------------------------

LOCK TABLES `job` WRITE;
/*!40000 ALTER TABLE `job` DISABLE KEYS */;

INSERT INTO `job` (`id`, `created`, `updated`, `uuid`, `createts`, `isactive`, `labid`, `name`, `userid`, `viewablebylab`, `workflowid`, `lastupdatebyuser`, `current_quote`)
VALUES
	(1,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'36D0AF55F9B74DA9AB8DBA837D8EC613','2012-12-20 11:20:04',1,2,'chipseq1',10,0,2,10,NULL),
	(2,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'30F0DEEE5F1242FCAFA69093D07E7572','2012-12-20 11:27:07',1,2,'chipseq2',10,0,2,10,NULL);

/*!40000 ALTER TABLE `job` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobcellselection
# ------------------------------------------------------------

LOCK TABLES `jobcellselection` WRITE;
/*!40000 ALTER TABLE `jobcellselection` DISABLE KEYS */;

INSERT INTO `jobcellselection` (`id`, `created`, `updated`, `uuid`, `cellindex`, `jobid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'33403B8BEA284B24A0C2B73288FFFFC2',1,1,NULL),
	(2,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'30E5A435E80048A7A9112D7F96570D47',1,2,NULL),
	(3,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'459304390DE14BA0BF6435DDBBDF7683',2,2,NULL);

/*!40000 ALTER TABLE `jobcellselection` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobdraft
# ------------------------------------------------------------

LOCK TABLES `jobdraft` WRITE;
/*!40000 ALTER TABLE `jobdraft` DISABLE KEYS */;

INSERT INTO `jobdraft` (`id`, `created`, `updated`, `uuid`, `createts`, `labid`, `name`, `status`, `submittedjobid`, `userid`, `workflowid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'53FAC00A2BFF4D51ADD38F878F0F195C','2012-12-20 11:15:26',2,'chipseq1','SUBMITTED',1,10,2,10),
	(2,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'A11C5D31230240C2A867F965A67DA9CC','2012-12-20 11:25:19',2,'chipseq2','SUBMITTED',2,10,2,10);

/*!40000 ALTER TABLE `jobdraft` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobdraftcellselection
# ------------------------------------------------------------

LOCK TABLES `jobdraftcellselection` WRITE;
/*!40000 ALTER TABLE `jobdraftcellselection` DISABLE KEYS */;

INSERT INTO `jobdraftcellselection` (`id`, `created`, `updated`, `uuid`, `cellindex`, `jobdraftid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'CDC1E64D4B504263BCF05C74FA40DD5D',1,1,NULL),
	(2,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'D04B8699D0D942489E86F6DAC7FB8697',1,2,NULL),
	(3,'2013-03-12 12:39:39','2013-03-12 18:10:00',X'ED0D7B6337404A7497D79A0C42CC076F',2,2,NULL);

/*!40000 ALTER TABLE `jobdraftcellselection` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobdraftfile
# ------------------------------------------------------------



# Dump of table jobdraftmeta
# ------------------------------------------------------------

LOCK TABLES `jobdraftmeta` WRITE;
/*!40000 ALTER TABLE `jobdraftmeta` DISABLE KEYS */;

INSERT INTO `jobdraftmeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `jobdraftid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:15:35','2013-03-12 18:10:00',X'2B414BC52B3749B49AF336D6008777C2','illuminaHiSeq2000.readLength',0,NULL,'100',1,NULL),
	(2,'2012-12-20 11:15:35','2013-03-12 18:10:00',X'6E73EC5D09154644A4640870C4C84B81','illuminaHiSeq2000.readType',0,NULL,'paired',1,NULL),
	(3,'2012-12-20 11:18:37','2013-03-12 12:39:41',X'0D9D036C80DA4631A61804070C1EAFD1','chipSeqPlugin.samplePairsTvsC',0,NULL,'1:3;2:3;',1,10),
	(4,'2012-12-20 11:18:41','2013-03-12 18:10:00',X'3E357CD343C0435EA4BE1D669503E36D','bowtieAligner.mismatches',0,NULL,'2',1,NULL),
	(5,'2012-12-20 11:18:41','2013-03-12 18:10:00',X'C4BD998792DC421AB6C4C8C99FABBE7A','bowtieAligner.seedLength',0,NULL,'32',1,NULL),
	(6,'2012-12-20 11:18:41','2013-03-12 18:10:00',X'88945C0A7B2C48B8829E7ECEDE74A572','bowtieAligner.reportAlignmentNum',0,NULL,'1',1,NULL),
	(7,'2012-12-20 11:18:41','2013-03-12 18:10:00',X'D0540B8E6C8A4EA6A05C2F4B06B7420C','bowtieAligner.discardThreshold',0,NULL,'1',1,NULL),
	(8,'2012-12-20 11:18:41','2013-03-12 18:10:00',X'8F64A0AD6373400C828645DD4C157F36','bowtieAligner.isBest',0,NULL,'yes',1,NULL),
	(9,'2012-12-20 11:19:39','2013-03-12 18:10:00',X'E6FB6714F5A1474D8444B256D93FBC0B','macsPeakcaller.pValueCutoff',0,NULL,'100000',1,NULL),
	(10,'2012-12-20 11:19:39','2013-03-12 18:10:00',X'9C87C487C5C848DCA79BA9FBCA247A7A','macsPeakcaller.bandwidth',0,NULL,'300',1,NULL),
	(11,'2012-12-20 11:19:39','2013-03-12 18:10:00',X'309870D4ED504AC0AD21E3560DBEC310','macsPeakcaller.genomeSize',0,NULL,'1000000000',1,NULL),
	(12,'2012-12-20 11:19:39','2013-03-12 18:10:00',X'21F788CCC1CD44429E243D40C2087FB2','macsPeakcaller.keepDup',0,NULL,'no',1,NULL),
	(13,'2012-12-20 11:20:03','2013-03-12 12:39:41',X'930CFB21A7C54A78958C81842A47F1B6','statusMessage.userSubmittedJobComment::0982004b-2bcf-490e-83a0-b0e86fb8293f',0,NULL,'User-submitted Job Comment::Please expedite. Grant deadline approaching!',1,10),
	(14,'2012-12-20 11:25:27','2013-03-12 18:10:00',X'D7891087406A4E99B16E6863C9277FB5','illuminaHiSeq2000.readLength',0,NULL,'100',2,NULL),
	(15,'2012-12-20 11:25:27','2013-03-12 18:10:00',X'B979FCBF050344F9A0EA377D3F4DDD43','illuminaHiSeq2000.readType',0,NULL,'paired',2,NULL),
	(16,'2012-12-20 11:26:56','2013-03-12 12:39:41',X'D6F43276F26B42CBA6EE65B6C868229D','chipSeqPlugin.samplePairsTvsC',0,NULL,'5:4;',2,10),
	(17,'2012-12-20 11:26:59','2013-03-12 18:10:00',X'7B1CE9A25CA047ED9FD56F150CDE2DE9','bowtieAligner.mismatches',0,NULL,'2',2,NULL),
	(18,'2012-12-20 11:26:59','2013-03-12 18:10:00',X'C69A3C7783EF4FD7BEEDBF9DA83D013B','bowtieAligner.seedLength',0,NULL,'32',2,NULL),
	(19,'2012-12-20 11:26:59','2013-03-12 18:10:00',X'A8EAE3257FB242B8B68FD9FA3563A818','bowtieAligner.reportAlignmentNum',0,NULL,'1',2,NULL),
	(20,'2012-12-20 11:26:59','2013-03-12 18:10:00',X'791E465E188648D1BB799D682B957FD2','bowtieAligner.discardThreshold',0,NULL,'1',2,NULL),
	(21,'2012-12-20 11:26:59','2013-03-12 18:10:00',X'F3240694CA7C4A23BA776B69EC22559A','bowtieAligner.isBest',0,NULL,'yes',2,NULL),
	(22,'2012-12-20 11:27:02','2013-03-12 18:10:00',X'1A2DEC06ECD44D20A45209178742E1E5','macsPeakcaller.pValueCutoff',0,NULL,'100000',2,NULL),
	(23,'2012-12-20 11:27:02','2013-03-12 18:10:00',X'6B8BFEBD54A348749DA5BD7DAE1C9948','macsPeakcaller.bandwidth',0,NULL,'300',2,NULL),
	(24,'2012-12-20 11:27:02','2013-03-12 18:10:00',X'5A3A6640ED2348DBA917FB1C088EDF67','macsPeakcaller.genomeSize',0,NULL,'1000000000',2,NULL),
	(25,'2012-12-20 11:27:02','2013-03-12 18:10:00',X'16A2D17D40544E8F8C2E0DE6C20EB6C1','macsPeakcaller.keepDup',0,NULL,'no',2,NULL);

/*!40000 ALTER TABLE `jobdraftmeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobdraftresourcecategory
# ------------------------------------------------------------

LOCK TABLES `jobdraftresourcecategory` WRITE;
/*!40000 ALTER TABLE `jobdraftresourcecategory` DISABLE KEYS */;

INSERT INTO `jobdraftresourcecategory` (`id`, `created`, `updated`, `uuid`, `jobdraftid`, `resourcecategoryid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:15:30','2013-03-12 12:39:41',X'F307C284860B4461BD50ABEFB53F0B82',1,1,10),
	(2,'2012-12-20 11:25:22','2013-03-12 12:39:41',X'EE3F2413C44E4D4C84867CB618C9A534',2,1,10);

/*!40000 ALTER TABLE `jobdraftresourcecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobdraftsoftware
# ------------------------------------------------------------

LOCK TABLES `jobdraftsoftware` WRITE;
/*!40000 ALTER TABLE `jobdraftsoftware` DISABLE KEYS */;

INSERT INTO `jobdraftsoftware` (`id`, `created`, `updated`, `uuid`, `jobdraftid`, `softwareid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:18:39','2013-03-12 12:39:41',X'1F86469592304CE994AB0193E09EAB96',1,1,10),
	(2,'2012-12-20 11:18:43','2013-03-12 12:39:41',X'77358ABCB2414D12AA3405399A036694',1,2,10),
	(3,'2012-12-20 11:26:58','2013-03-12 12:39:41',X'0DC80BE22D2742F7AD4F013163747949',2,1,10),
	(4,'2012-12-20 11:27:01','2013-03-12 12:39:41',X'E5DD1BE83332485AAF302CB4862F429C',2,2,10);

/*!40000 ALTER TABLE `jobdraftsoftware` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobfile
# ------------------------------------------------------------



# Dump of table jobmeta
# ------------------------------------------------------------

LOCK TABLES `jobmeta` WRITE;
/*!40000 ALTER TABLE `jobmeta` DISABLE KEYS */;

INSERT INTO `jobmeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `jobid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'7A66A16CC47543F18C4FADE9492B8BA8','illuminaHiSeq2000.readLength',0,NULL,'100',1,10),
	(2,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'21D7DB4FDDAC4AEABE80A7B5D7D37D16','illuminaHiSeq2000.readType',0,NULL,'paired',1,10),
	(3,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'FD40B5C2186D415292952D561166AFC1','chipSeqPlugin.samplePairsTvsC',0,NULL,'1:3;2:3;',1,10),
	(4,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'0DE62BF82CDF4DF8AD0FB944809B1D8D','bowtieAligner.mismatches',0,NULL,'2',1,10),
	(5,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'CD301F39A53F4800A99662C86485A8CB','bowtieAligner.seedLength',0,NULL,'32',1,10),
	(6,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'D2099ACCBC1744718B46F4FD3249C49D','bowtieAligner.reportAlignmentNum',0,NULL,'1',1,10),
	(7,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'FF64189D307D496E876C56EAA145B1A9','bowtieAligner.discardThreshold',0,NULL,'1',1,10),
	(8,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'2814E7D88A1F43D3B638380A47C9351C','bowtieAligner.isBest',0,NULL,'yes',1,10),
	(9,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'844B74CAF233487286C89707E2DDD135','macsPeakcaller.pValueCutoff',0,NULL,'100000',1,10),
	(10,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'84D67513FB1E46E4A0934A53951A5B1A','macsPeakcaller.bandwidth',0,NULL,'300',1,10),
	(11,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'508D3E379FED42268A3F849F5E2323C5','macsPeakcaller.genomeSize',0,NULL,'1000000000',1,10),
	(12,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'5F621AF7B9BE417DB9EE681C9AD68E62','macsPeakcaller.keepDup',0,NULL,'no',1,10),
	(13,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'0639FC0AB8984BFB9B879D667712F0FE','statusMessage.userSubmittedJobComment::0982004b-2bcf-490e-83a0-b0e86fb8293f',0,NULL,'User-submitted Job Comment::Please expedite. Grant deadline approaching!',1,10),
	(14,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'3703E092646B4D6A8971092FC0EAF993','illuminaHiSeq2000.readLength',0,NULL,'100',2,10),
	(15,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'4FEDB2FD3DBB4693AF91CCD64758FEB6','illuminaHiSeq2000.readType',0,NULL,'paired',2,10),
	(16,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'6B4B06E24FE346F5ADB5045407F28490','chipSeqPlugin.samplePairsTvsC',0,NULL,'5:4;',2,10),
	(17,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'1C4218E66B1948BA8EE429BAF64E9FDA','bowtieAligner.mismatches',0,NULL,'2',2,10),
	(18,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'C60111B7FFC44C2C8A3B023F0C48F9BA','bowtieAligner.seedLength',0,NULL,'32',2,10),
	(19,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'B9F1300AECE8473EB2DCAC53E4AE6977','bowtieAligner.reportAlignmentNum',0,NULL,'1',2,10),
	(20,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'9E4EDF5094894DE29F272D959552D0E2','bowtieAligner.discardThreshold',0,NULL,'1',2,10),
	(21,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'A1CE15AC47324FA5A29F0358AD92036D','bowtieAligner.isBest',0,NULL,'yes',2,10),
	(22,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'29AF18E361D64C9CB076CB844216B747','macsPeakcaller.pValueCutoff',0,NULL,'100000',2,10),
	(23,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'35AFE60ABDF549C1A741831424348A7F','macsPeakcaller.bandwidth',0,NULL,'300',2,10),
	(24,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'CBA1605DDBEC4ECCB2335092600656CB','macsPeakcaller.genomeSize',0,NULL,'1000000000',2,10),
	(25,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'095AF7364D34459F8CCD90CF6CBF77A7','macsPeakcaller.keepDup',0,NULL,'no',2,10);

/*!40000 ALTER TABLE `jobmeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobresourcecategory
# ------------------------------------------------------------

LOCK TABLES `jobresourcecategory` WRITE;
/*!40000 ALTER TABLE `jobresourcecategory` DISABLE KEYS */;

INSERT INTO `jobresourcecategory` (`id`, `created`, `updated`, `uuid`, `jobid`, `resourcecategoryid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'D069A1FF224C43A0ADE999AA2CF56C15',1,1,10),
	(2,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'35D1A90B8CC3494C819000233B799347',2,1,10);

/*!40000 ALTER TABLE `jobresourcecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobsample
# ------------------------------------------------------------

LOCK TABLES `jobsample` WRITE;
/*!40000 ALTER TABLE `jobsample` DISABLE KEYS */;

INSERT INTO `jobsample` (`id`, `created`, `updated`, `uuid`, `jobid`, `sampleid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'1DC5B9E2253442F1972943AD50213208',1,21,10),
	(2,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'5470E73ECD304346AF3BCB8CF0B7EE35',1,22,10),
	(3,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'4A7F4DCDE2C942D28000529620D9B618',1,23,10),
	(4,'2012-12-20 11:24:31','2013-03-12 12:39:41',X'9412EFF595544BE6A1C2B938820E1AAB',1,24,1),
	(5,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'224E4CF8393A468FAB547D62A59FF0A2',2,25,10),
	(6,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'4C2324626C7047B1B8CD876A817899B1',2,26,10);

/*!40000 ALTER TABLE `jobsample` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobsamplemeta
# ------------------------------------------------------------



# Dump of table jobsoftware
# ------------------------------------------------------------

LOCK TABLES `jobsoftware` WRITE;
/*!40000 ALTER TABLE `jobsoftware` DISABLE KEYS */;

INSERT INTO `jobsoftware` (`id`, `created`, `updated`, `uuid`, `jobid`, `softwareid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'836C82CFD5C34960A506B46DDA97B0FE',1,1,10),
	(2,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'892792FA25B5473BB3E118D32B070AEA',1,2,10),
	(3,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'7A6D0B1DB5664D3FBC650A99B0481447',2,1,10),
	(4,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'A0DE50A80D1D401D9EDF84BCC2C8E537',2,2,10);

/*!40000 ALTER TABLE `jobsoftware` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table jobuser
# ------------------------------------------------------------

LOCK TABLES `jobuser` WRITE;
/*!40000 ALTER TABLE `jobuser` DISABLE KEYS */;

INSERT INTO `jobuser` (`id`, `created`, `updated`, `uuid`, `jobid`, `roleid`, `userid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'24EF9B555AFE4BBFB70F3315F53278CA',1,9,10,10),
	(2,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'556AB7474A0E4010A10E3AA51F0B2F23',1,10,5,10),
	(3,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'312D55702B494548AEDA82257AA9D190',2,9,10,10),
	(4,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'80C538D387864FCDA2AEC4945311714F',2,10,5,10);

/*!40000 ALTER TABLE `jobuser` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table lab
# ------------------------------------------------------------

LOCK TABLES `lab` WRITE;
/*!40000 ALTER TABLE `lab` DISABLE KEYS */;

INSERT INTO `lab` (`id`, `created`, `updated`, `uuid`, `departmentid`, `isactive`, `name`, `primaryuserid`, `lastupdatebyuser`)
VALUES
	(1,'2012-06-14 14:08:21','2013-03-12 12:39:41',X'941E253E1EDC48ABB54A51C2EDFF4C4A',1,1,'Default lab',1,1),
	(2,'2012-06-14 14:07:33','2013-03-12 12:39:41',X'23F247CE1672458097A7465806AC1AC4',3,1,'Cancer Genetics',5,1),
	(3,'2012-06-14 14:07:27','2013-03-12 12:39:41',X'423F5A4E97E2453396CB8AA73717BBF0',3,1,'Godwin Lab',7,1),
	(4,'2012-06-14 14:06:49','2013-03-12 12:39:41',X'713C9DD331F54F00A4AA8DD61D11C4AC',3,1,'Williams Lab',8,1),
	(5,'2012-06-14 14:05:52','2013-03-12 12:39:41',X'8ED1BB7CD0ED42609600E4CBE1EB2703',2,1,'Zebra Fish Lab',9,1);

/*!40000 ALTER TABLE `lab` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table labmeta
# ------------------------------------------------------------

LOCK TABLES `labmeta` WRITE;
/*!40000 ALTER TABLE `labmeta` DISABLE KEYS */;

INSERT INTO `labmeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `labid`, `lastupdatebyuser`)
VALUES
	(1,'2012-05-30 20:22:24','2013-03-12 18:10:00',X'63C2A4BAEDB74A9CA73A04D3FE190614','lab.internal_external_lab',0,NULL,'internal',2,NULL),
	(2,'2012-05-30 20:22:24','2013-03-12 18:10:00',X'557986714D7E4A7390F269D1E08A64D5','lab.phone',0,NULL,'718-123-4567',2,NULL),
	(3,'2012-05-30 20:22:24','2013-03-12 18:10:00',X'41D0FF826324422A9EEF500974142A49','lab.building_room',0,NULL,'Price 220',2,NULL),
	(4,'2012-05-30 20:22:24','2013-03-12 18:10:00',X'EC7EED5C1FC142278492236270B102A8','lab.billing_contact',0,NULL,'John Greally',2,NULL),
	(5,'2012-05-30 20:22:24','2013-03-12 18:10:00',X'3DAED9710C814667AC67970EF30D72EC','lab.billing_institution',0,NULL,'Einstein',2,NULL),
	(6,'2012-05-30 20:22:24','2013-03-12 18:10:00',X'12B988232CDD49218D12DF04C88510BE','lab.billing_departmentId',0,NULL,'3',2,NULL),
	(7,'2012-05-30 20:22:24','2013-03-12 18:10:00',X'4846665F0F024DC18CDDB05F2A55F1C9','lab.billing_building_room',0,NULL,'Price 220',2,NULL),
	(8,'2012-05-30 20:22:24','2013-03-12 18:10:00',X'574D6A2F5E444F088E5AFF17515D83DF','lab.billing_address',0,NULL,'1301 Morris Park Ave.',2,NULL),
	(9,'2012-05-30 20:22:24','2013-03-12 18:10:00',X'730B4683C8094D1784FB4E7796C50659','lab.billing_city',0,NULL,'Bronx',2,NULL),
	(10,'2012-05-30 20:22:24','2013-03-12 18:10:00',X'A00A28C0B2C14A5DBDFFAF005844966F','lab.billing_state',0,NULL,'NY',2,NULL),
	(11,'2012-05-30 20:22:24','2013-03-12 18:10:00',X'770F142D62FB430D9EC82BB1D4FC72CB','lab.billing_country',0,NULL,'US',2,NULL),
	(12,'2012-05-30 20:22:24','2013-03-12 18:10:00',X'6F9F7D3D9D0141B19FCA62CCC75ACFC8','lab.billing_zip',0,NULL,'10461',2,NULL),
	(13,'2012-05-30 20:22:24','2013-03-12 18:10:00',X'A7FB728067AD48A785D07FE98B7C4C4E','lab.billing_phone',0,NULL,'718-123-4567',2,NULL),
	(14,'2012-05-30 22:03:56','2013-03-12 18:10:00',X'5E094706824542568A1A97D1274E436E','lab.internal_external_lab',0,NULL,'internal',3,NULL),
	(15,'2012-05-30 22:03:56','2013-03-12 18:10:00',X'44477DEA40C945EDADFA8E2FAF209464','lab.phone',0,NULL,'718-678-1112',3,NULL),
	(16,'2012-05-30 22:03:56','2013-03-12 18:10:00',X'320DC89BE7E146E5882240EBD5EF152E','lab.building_room',0,NULL,'Price 353',3,NULL),
	(17,'2012-05-30 22:03:56','2013-03-12 18:10:00',X'5F9DA7EEAAFD44DA953BB9AE57D5343C','lab.billing_contact',0,NULL,'Aaron Goldin',3,NULL),
	(18,'2012-05-30 22:03:56','2013-03-12 18:10:00',X'323E8FF304034F17989B48C9B1BC8502','lab.billing_institution',0,NULL,'Einstein',3,NULL),
	(19,'2012-05-30 22:03:56','2013-03-12 18:10:00',X'DC2C37A4B4B8476EAF8743746AE3B79E','lab.billing_departmentId',0,NULL,'3',3,NULL),
	(20,'2012-05-30 22:03:56','2013-03-12 18:10:00',X'BA8734BE5126414AA29F701331D60535','lab.billing_building_room',0,NULL,'Price 353',3,NULL),
	(21,'2012-05-30 22:03:56','2013-03-12 18:10:00',X'D244C77610A2420596B4348F5A6AAB12','lab.billing_address',0,NULL,'1301 Morris Park Ave.',3,NULL),
	(22,'2012-05-30 22:03:56','2013-03-12 18:10:00',X'A06C45B67D3E416DB6E06556AC9AE142','lab.billing_city',0,NULL,'Bronx',3,NULL),
	(23,'2012-05-30 22:03:56','2013-03-12 18:10:00',X'E146A709033F4CEEBAA13371971F3F4E','lab.billing_state',0,NULL,'NY',3,NULL),
	(24,'2012-05-30 22:03:56','2013-03-12 18:10:00',X'087D32B89E4849FE91EE352D048B9353','lab.billing_country',0,NULL,'US',3,NULL),
	(25,'2012-05-30 22:03:56','2013-03-12 18:10:00',X'17A7289B4B104A03891212D4A5FD5FB4','lab.billing_zip',0,NULL,'10461',3,NULL),
	(26,'2012-05-30 22:03:56','2013-03-12 18:10:00',X'B847BE70F9F74C2D85D8830019BABB3D','lab.billing_phone',0,NULL,'718-678-1112',3,NULL),
	(27,'2012-05-31 13:59:23','2013-03-12 18:10:00',X'FE571184D47F4BBAA3BCB98216A28CD8','lab.internal_external_lab',0,NULL,'internal',4,NULL),
	(28,'2012-05-31 13:59:23','2013-03-12 18:10:00',X'32046E36DBD246A4AB8F147A54BF3FA0','lab.phone',0,NULL,'718-678-1019',4,NULL),
	(29,'2012-05-31 13:59:23','2013-03-12 18:10:00',X'CDBFA00D096D4195AF4ACE4E128A0BB3','lab.building_room',0,NULL,'Price 321',4,NULL),
	(30,'2012-05-31 13:59:23','2013-03-12 18:10:00',X'953DAA481AF041FF89C2AB67A6CA807B','lab.billing_contact',0,NULL,'Adam Auton',4,NULL),
	(31,'2012-05-31 13:59:23','2013-03-12 18:10:00',X'8046A75FD5BE42849E8D79D1452AE882','lab.billing_institution',0,NULL,'Einstein',4,NULL),
	(32,'2012-05-31 13:59:23','2013-03-12 18:10:00',X'96A79F78EEFF4FDD98D3D8CA1B13AE3B','lab.billing_departmentId',0,NULL,'3',4,NULL),
	(33,'2012-05-31 13:59:23','2013-03-12 18:10:00',X'D9345AFBED634570ADF8FF2801BD64C0','lab.billing_building_room',0,NULL,'Price 321',4,NULL),
	(34,'2012-05-31 13:59:23','2013-03-12 18:10:00',X'67D0AB5902E945B8B8E20F5AD43CF881','lab.billing_address',0,NULL,'1301 Morris Park Ave.',4,NULL),
	(35,'2012-05-31 13:59:23','2013-03-12 18:10:00',X'3A0742AD0DD14F43BDD1BD0C21BB2ED9','lab.billing_city',0,NULL,'Bronx',4,NULL),
	(36,'2012-05-31 13:59:23','2013-03-12 18:10:00',X'9E461DFB365A465F871E521B56CDDEE6','lab.billing_state',0,NULL,'NY',4,NULL),
	(37,'2012-05-31 13:59:23','2013-03-12 18:10:00',X'0B2CEA5D66C44AC988C8CBB1B448EF50','lab.billing_country',0,NULL,'US',4,NULL),
	(38,'2012-05-31 13:59:23','2013-03-12 18:10:00',X'EBB205A2F8754FF1947E776246BF37C4','lab.billing_zip',0,NULL,'10461',4,NULL),
	(39,'2012-05-31 13:59:23','2013-03-12 18:10:00',X'6A05B161A2D342538ABEC44264853B36','lab.billing_phone',0,NULL,'718-678-1019',4,NULL),
	(40,'2012-05-31 14:00:01','2013-03-12 18:10:00',X'93197D8F33A0476589AB7615365072BC','lab.internal_external_lab',0,NULL,'external',5,NULL),
	(41,'2012-05-31 14:00:01','2013-03-12 18:10:00',X'5225040D48ED4088B03311BDB21EEE37','lab.phone',0,NULL,'212-321-1091',5,NULL),
	(42,'2012-05-31 14:00:01','2013-03-12 18:10:00',X'CE73E8FB04334BEEB0F42FF56FE89441','lab.building_room',0,NULL,'Hammer 1101',5,NULL),
	(43,'2012-05-31 14:00:01','2013-03-12 18:10:00',X'A1795F7F414448CFBB04AC2D173D122F','lab.billing_contact',0,NULL,'Leslie Trokie',5,NULL),
	(44,'2012-05-31 14:00:02','2013-03-12 18:10:00',X'3FE6D464ACD943F28788AA94B4643A1A','lab.billing_institution',0,NULL,'NYU Medical',5,NULL),
	(45,'2012-05-31 14:00:02','2013-03-12 18:10:00',X'0366FFD126AA4712B3C63D163408CD1F','lab.billing_departmentId',0,NULL,'3',5,NULL),
	(46,'2012-05-31 14:00:02','2013-03-12 18:10:00',X'FCA4BA3B40A746AFA84E3ED26331E184','lab.billing_building_room',0,NULL,'Hammer 1101',5,NULL),
	(47,'2012-05-31 14:00:02','2013-03-12 18:10:00',X'6E949A7357BE4D438DD009738033326E','lab.billing_address',0,NULL,'16-50 32nd Street',5,NULL),
	(48,'2012-05-31 14:00:02','2013-03-12 18:10:00',X'94EBA94D5F0A4F3DA3E29D2994FF73F3','lab.billing_city',0,NULL,'New York',5,NULL),
	(49,'2012-05-31 14:00:02','2013-03-12 18:10:00',X'FF2A76A7B6134931BC8F7933657A2213','lab.billing_state',0,NULL,'NY',5,NULL),
	(50,'2012-05-31 14:00:02','2013-03-12 18:10:00',X'D86CE42B3B9F45D8B878E5C5191424E7','lab.billing_country',0,NULL,'US',5,NULL),
	(51,'2012-05-31 14:00:02','2013-03-12 18:10:00',X'9904A5A9AC674AEB976A97D7FF56CC46','lab.billing_zip',0,NULL,'10002',5,NULL),
	(52,'2012-05-31 14:00:02','2013-03-12 18:10:00',X'0AAF3AAC74F14945956693B62A10230D','lab.billing_phone',0,NULL,'212-321-1091',5,NULL),
	(53,'2012-06-14 14:08:21','2013-03-12 18:10:00',X'1906738D78B84877A5F23229A6A6A54D','lab.internal_external_lab',0,NULL,'internal',1,NULL),
	(54,'2012-06-14 14:08:21','2013-03-12 18:10:00',X'73EA09CB2E9D4836BA1FF45B46C43E7B','lab.phone',0,NULL,'N/A',1,NULL),
	(55,'2012-06-14 14:08:21','2013-03-12 18:10:00',X'C79604793E0446148B4DCA0F70894F18','lab.building_room',0,NULL,'N/A',1,NULL),
	(56,'2012-06-14 14:08:21','2013-03-12 18:10:00',X'ABF6C4F45948459C9B228320FBE82986','lab.billing_contact',0,NULL,'N/A',1,NULL),
	(57,'2012-06-14 14:08:21','2013-03-12 18:10:00',X'3DE24864434540D4B1946B99DA8DD492','lab.billing_institution',0,NULL,'N/A',1,NULL),
	(58,'2012-06-14 14:08:21','2013-03-12 18:10:00',X'F09B44E49B434DD0A87D33FCB55998A5','lab.billing_departmentId',0,NULL,'1',1,NULL),
	(59,'2012-06-14 14:08:21','2013-03-12 18:10:00',X'A737E90224DD47E4BF0CEA110C9EE4FF','lab.billing_building_room',0,NULL,'N/A',1,NULL),
	(60,'2012-06-14 14:08:21','2013-03-12 18:10:00',X'938D953D39C1496BAC715865BCE795AC','lab.billing_address',0,NULL,'N/A',1,NULL),
	(61,'2012-06-14 14:08:21','2013-03-12 18:10:00',X'7FC6469F71DB46DDAC80D40BD47465D6','lab.billing_city',0,NULL,'N/A',1,NULL),
	(62,'2012-06-14 14:08:21','2013-03-12 18:10:00',X'FC2374CCE9914288B2D100D4A6D66266','lab.billing_state',0,NULL,'NY',1,NULL),
	(63,'2012-06-14 14:08:21','2013-03-12 18:10:00',X'70697A3125D4431C8C0AA73BA22C0477','lab.billing_country',0,NULL,'US',1,NULL),
	(64,'2012-06-14 14:08:21','2013-03-12 18:10:00',X'C0D22C73D08247F086D594D00D00BF99','lab.billing_zip',0,NULL,'N/A',1,NULL),
	(65,'2012-06-14 14:08:21','2013-03-12 18:10:00',X'2BCCB620F5D947A7ADE7FF29E08DCD71','lab.billing_phone',0,NULL,'N/A',1,NULL);

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

INSERT INTO `labuser` (`id`, `created`, `updated`, `uuid`, `labid`, `roleid`, `userid`, `lastupdatebyuser`)
VALUES
	(1,'2012-05-23 15:55:46','2013-03-12 12:39:41',X'013E4F12B503438E872CF4BF6DFF6E05',1,6,1,1),
	(2,'2012-05-30 20:22:24','2013-03-12 12:39:41',X'560BBFF18C724606A37BB9E8F13EDDB2',2,6,5,3),
	(3,'2012-05-30 21:13:54','2013-03-12 12:39:41',X'F569EE4186A944598B3ECEB56B47796A',2,8,6,5),
	(4,'2012-05-30 22:03:56','2013-03-12 12:39:41',X'9B0A0DC86F9444AC81835CAC63D77112',3,6,7,1),
	(5,'2012-05-31 13:59:23','2013-03-12 12:39:41',X'99837551B5884E2BAA098A1B211B45E7',4,6,8,3),
	(6,'2012-05-31 14:00:02','2013-03-12 12:39:41',X'8EB4A086650949BEA30028DE93BE09B3',5,6,9,1),
	(7,'2012-05-31 14:02:38','2013-03-12 12:39:41',X'89F17FF358774A6591FF3B1A62E672E4',2,7,10,5),
	(8,'2012-05-31 14:15:29','2013-03-12 12:39:41',X'12D310ECF107431D975A99E632FCE330',3,8,12,7);

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

INSERT INTO `resource` (`id`, `created`, `updated`, `uuid`, `iname`, `isactive`, `name`, `resourcetypeid`, `resourcecategoryid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:07:21','2013-03-12 12:39:41',X'3D34408426E84F5993615A98AAEEF6EB','Basil',1,'Basil',2,1,1),
	(2,'2012-12-20 11:07:41','2013-03-12 12:39:41',X'594F181283B745FA96D6F5A12EE75331','Sybil',1,'Sybil',2,1,1),
	(3,'2012-12-20 11:08:04','2013-03-12 12:39:41',X'157FDCA7A30F4A2EB0FB91FE34B92E75','Manuel',1,'Manuel',2,2,1);

/*!40000 ALTER TABLE `resource` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcebarcode
# ------------------------------------------------------------

LOCK TABLES `resourcebarcode` WRITE;
/*!40000 ALTER TABLE `resourcebarcode` DISABLE KEYS */;

INSERT INTO `resourcebarcode` (`id`, `created`, `updated`, `uuid`, `barcodeid`, `resourceid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'03F0048B61EB4B33854AD3820C37CF84',1,1,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'FFB8285BF0D2430F85A9E942FA5A8A35',2,2,NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'3EC6F146A21143BDB6CF3B65CCA710D0',3,3,NULL);

/*!40000 ALTER TABLE `resourcebarcode` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcecategory
# ------------------------------------------------------------

LOCK TABLES `resourcecategory` WRITE;
/*!40000 ALTER TABLE `resourcecategory` DISABLE KEYS */;

INSERT INTO `resourcecategory` (`id`, `created`, `updated`, `uuid`, `iname`, `isactive`, `name`, `resourcetypeid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:03:31','2013-03-12 18:10:04',X'ACAF0DF14AC8486D84E6DE9C99C35183','illuminaHiSeq2000',1,'Illumina HiSeq 2000',2,0),
	(2,'2012-12-20 11:03:32','2013-03-12 18:10:04',X'AB92B77D27454A44AD550FC4690DE1F6','illuminaMiSeqPersonalSequencer',1,'Illumina MiSeq Personal Sequencer',2,0);

/*!40000 ALTER TABLE `resourcecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcecategorymeta
# ------------------------------------------------------------

LOCK TABLES `resourcecategorymeta` WRITE;
/*!40000 ALTER TABLE `resourcecategorymeta` DISABLE KEYS */;

INSERT INTO `resourcecategorymeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `resourcecategoryid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:03:31','2013-03-12 12:39:41',X'3B4E6C3D4F07478DB2DCEC1A7791AC94','illuminaHiSeq2000.allowableUiField.readType',1,NULL,'single:single;paired:paired',1,0),
	(2,'2012-12-20 11:03:31','2013-03-12 12:39:41',X'C32D031490924449A0A1387B566199DB','illuminaHiSeq2000.allowableUiField.readlength',2,NULL,'50:50;75:75;100:100;150:150',1,0),
	(3,'2012-12-20 11:03:31','2013-03-12 12:39:41',X'7564F451030F4E8A8113F80D3479FD27','illuminaHiSeq2000.platformUnitSelector',3,NULL,'A:A;B:B',1,0),
	(4,'2012-12-20 11:03:32','2013-03-12 12:39:41',X'508123F338284DCE92E2AC83E40B8918','illuminaMiSeqPersonalSequencer.allowableUiField.readType',1,NULL,'single:single;paired:paired',2,0),
	(5,'2012-12-20 11:03:32','2013-03-12 12:39:41',X'DB39A10C06114C698F6A1B889A7B5C0C','illuminaMiSeqPersonalSequencer.allowableUiField.readlength',2,NULL,'25:25;36:36;50:50;100:100;150:150',2,0);

/*!40000 ALTER TABLE `resourcecategorymeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcecell
# ------------------------------------------------------------



# Dump of table resourcemeta
# ------------------------------------------------------------

LOCK TABLES `resourcemeta` WRITE;
/*!40000 ALTER TABLE `resourcemeta` DISABLE KEYS */;

INSERT INTO `resourcemeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `resourceid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:07:21','2013-03-12 18:10:00',X'C5BB896E6A3744A28317AF908A05C2AE','resource.commission_date',0,NULL,'2012-10-03',1,NULL),
	(2,'2012-12-20 11:07:21','2013-03-12 18:10:00',X'B6153AFAD64A444C9BDEBD982295FDB6','resource.decommission_date',0,NULL,'',1,NULL),
	(3,'2012-12-20 11:07:41','2013-03-12 18:10:00',X'33A17C3BBB9C487ABC8F2CF318B73495','resource.commission_date',0,NULL,'2012-08-02',2,NULL),
	(4,'2012-12-20 11:07:41','2013-03-12 18:10:00',X'36621C0C70DC4EEA860AC5CF7CDD350C','resource.decommission_date',0,NULL,'',2,NULL),
	(5,'2012-12-20 11:08:04','2013-03-12 18:10:00',X'F7EC9B745E5B466DB18CC39C91FD157C','resource.commission_date',0,NULL,'2012-05-24',3,NULL),
	(6,'2012-12-20 11:08:04','2013-03-12 18:10:00',X'0952142168D94A09AED62890CFCC909B','resource.decommission_date',0,NULL,'',3,NULL);

/*!40000 ALTER TABLE `resourcemeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcetype
# ------------------------------------------------------------

LOCK TABLES `resourcetype` WRITE;
/*!40000 ALTER TABLE `resourcetype` DISABLE KEYS */;

INSERT INTO `resourcetype` (`id`, `created`, `updated`, `uuid`, `iname`, `isactive`, `name`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'2E983A6CB6AA4CC684361306E60B278A','aligner',0,'Aligner',NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:10:03',X'2341D262EEC245EA8E0730DB150D398C','mps',1,'Massively Parallel DNA Sequencer',NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:10:03',X'81495112AA1541919E1D5425A956CEB9','amplicon',1,'DNA Amplicon',NULL),
	(4,'2013-03-12 12:39:40','2013-03-12 18:10:03',X'3DA110F3F99F43E9A30C8F62F4CC7B4B','peakcaller',1,'Peak Caller',NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:10:03',X'77C2DE20FD924DA98613F8B190A2869F','sequenceRunProcessor',1,'Sequence Run Processor',NULL),
	(6,'2013-03-12 12:39:40','2013-03-12 18:10:03',X'BB531174C958478892E5BB0AE85EAB26','bisulfiteSeqPipeline',1,'Bi-sulphite-seq Pipeline',NULL),
	(7,'2013-03-12 12:39:40','2013-03-12 18:10:04',X'B9AE73ED75264B3C95467AEBCADCBC0D','helptagPipeline',1,'HELP-tag Pipeline',NULL),
	(8,'2013-03-12 12:39:44','2013-03-12 18:10:03',X'F749BFB96A624F048276DE808C5E4E1D','referenceBasedAligner',1,'Reference-based Aligner',NULL);

/*!40000 ALTER TABLE `resourcetype` ENABLE KEYS */;
UNLOCK TABLES;



# Dump of table roleset
# ------------------------------------------------------------

LOCK TABLES `roleset` WRITE;
/*!40000 ALTER TABLE `roleset` DISABLE KEYS */;

INSERT INTO `roleset` (`id`, `created`, `updated`, `uuid`, `childroleid`, `parentroleid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'625D4EFC70E14C98A560E50F8F91B259',4,4,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'3650068744664DF3A2CB54B914B01F6C',1,1,NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'CA88D3335BB94DE6BDC5F34A06617FA6',5,5,NULL),
	(4,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'B5D4653C948948F39D679904F6F71ECD',3,3,NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'8BC1E403045E4588A741E6210522A8F4',14,14,NULL),
	(6,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'D2AE85AB31FD4DE2AEDE6B26EF51A994',9,9,NULL),
	(7,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'EACCB52327FA4E73AB61C34A743556EF',10,10,NULL),
	(8,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'98C8DB18CBBF456AB96A6819722D6DB5',7,7,NULL),
	(9,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'5DCFDC78DFC44A0E96ECCFB2225FCF32',13,13,NULL),
	(10,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'5EB0A6FADFE148FDB32BB5A50DE004E0',8,8,NULL),
	(11,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'6CF698B68CBD43BD8949D0E26C143750',12,12,NULL),
	(12,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'D102A3C819F04F319EA21683A29B26ED',6,6,NULL),
	(13,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'3D0EB3C03063419D9DB532A35CB804B2',2,2,NULL),
	(14,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'38DBD848C0F74FBF8C3452B450B6B06D',11,11,NULL),
	(15,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'359FA0887C2E4645AF14528C7C367180',15,15,NULL),
	(16,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'7BE12CE4D26F4928AFC0C6E976992D69',5,1,NULL),
	(17,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'CC731AC0FD2E438F898D32731620193C',7,6,NULL),
	(18,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'5FE7348CAB944DE583E041158809D86E',8,6,NULL),
	(19,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'C836C4DCEF124828B5429CFA61B8115F',8,7,NULL),
	(20,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'D8850F3408684930AD64D00512979DDB',10,9,NULL),
	(21,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'F476FB317231445ABC4084FD2582080B',1,11,NULL),
	(22,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'B9A6915609BA4062991E9BEDED4C7B68',2,11,NULL),
	(23,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'CD0351630C9B4726A993DBB1B63CE559',3,11,NULL),
	(24,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'AFE6BBF22AEF453BA98DEFD35F6519E6',5,11,NULL),
	(25,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'09BE517B49CF431CA329E59D1D2706B2',16,16,NULL);

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

INSERT INTO `sample` (`id`, `created`, `updated`, `uuid`, `isactive`, `isgood`, `isreceived`, `name`, `parentid`, `receivedts`, `receiver_userid`, `samplesubtypeid`, `sampletypeid`, `submitter_jobid`, `submitter_labid`, `submitter_userid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'21C067CBCD5E4A5D9CF336122A57CC6C',1,1,1,'C126NACXX',NULL,'2012-12-20 11:09:20',NULL,10,5,NULL,1,1,1),
	(2,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'E3D2122DD8774E6A80DC4E9ABDC792D8',1,1,1,'C126NACXX/1',NULL,'2012-12-20 11:09:20',1,NULL,4,NULL,1,1,1),
	(3,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'2B883563E874413AAE644215B77F5C31',1,1,1,'C126NACXX/2',NULL,'2012-12-20 11:09:20',1,NULL,4,NULL,1,1,1),
	(4,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'2FC2EF97688C42019A30B1D5CCFF31B5',1,1,1,'C126NACXX/3',NULL,'2012-12-20 11:09:20',1,NULL,4,NULL,1,1,1),
	(5,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'F2B63DB0978945FCA8C22FECD1F49F7B',1,1,1,'C126NACXX/4',NULL,'2012-12-20 11:09:20',1,NULL,4,NULL,1,1,1),
	(6,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'91BC6122ADB54014BFC35A6AD2B9E44D',1,1,1,'C126NACXX/5',NULL,'2012-12-20 11:09:20',1,NULL,4,NULL,1,1,1),
	(7,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'C4FF093DF0EC4A158B437A39CDFED733',1,1,1,'C126NACXX/6',NULL,'2012-12-20 11:09:20',1,NULL,4,NULL,1,1,1),
	(8,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'56F36A53FE2F42CE87D972AC6F24157A',1,1,1,'C126NACXX/7',NULL,'2012-12-20 11:09:20',1,NULL,4,NULL,1,1,1),
	(9,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'A025EB5DE3584149A6ACB28F69A74C53',1,1,1,'C126NACXX/8',NULL,'2012-12-20 11:09:20',1,NULL,4,NULL,1,1,1),
	(10,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'7BDBF52F1F4A4A1EA4957E9E5E9AB07D',1,1,1,'D1884ACXX',NULL,'2012-12-20 11:09:48',NULL,10,5,NULL,1,1,1),
	(11,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'B7ADA8B094AA4A00A1FAB767493B892F',1,1,1,'D1884ACXX/1',NULL,'2012-12-20 11:09:48',1,NULL,4,NULL,1,1,1),
	(12,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'AC2302D836F44407BD95253445E1106D',1,1,1,'D1884ACXX/2',NULL,'2012-12-20 11:09:48',1,NULL,4,NULL,1,1,1),
	(13,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'7BCCEC84415A4A56A2A2EF3611318795',1,1,1,'D1884ACXX/3',NULL,'2012-12-20 11:09:48',1,NULL,4,NULL,1,1,1),
	(14,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'C4CFC201F13C48A085FAC0678C530DB6',1,1,1,'D1884ACXX/4',NULL,'2012-12-20 11:09:48',1,NULL,4,NULL,1,1,1),
	(15,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'9D1E4202B0B041E6967BD45D3DA65068',1,1,1,'D1884ACXX/5',NULL,'2012-12-20 11:09:48',1,NULL,4,NULL,1,1,1),
	(16,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'9EF563B0B8CD438884B68953F85C7569',1,1,1,'D1884ACXX/6',NULL,'2012-12-20 11:09:48',1,NULL,4,NULL,1,1,1),
	(17,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'268BC89C99BE41B1AFC7FAF63420437F',1,1,1,'D1884ACXX/7',NULL,'2012-12-20 11:09:48',1,NULL,4,NULL,1,1,1),
	(18,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'89A1B2770C104C7DB078766D770409B9',1,1,1,'D1884ACXX/8',NULL,'2012-12-20 11:09:48',1,NULL,4,NULL,1,1,1),
	(19,'2012-12-20 11:10:27','2013-03-12 12:39:41',X'50A5C5F01E674E4F88B6170F3E4D3563',1,1,1,'634H7AAXX',NULL,'2012-12-20 11:10:27',NULL,9,5,NULL,1,1,1),
	(20,'2012-12-20 11:10:27','2013-03-12 12:39:41',X'D1B58BAEDF524441BCB70A271330957B',1,1,1,'634H7AAXX/1',NULL,'2012-12-20 11:10:27',1,NULL,4,NULL,1,1,1),
	(21,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'AA6153C99C40421C852913DC5C32FF6F',1,NULL,0,'sIP1',NULL,NULL,NULL,5,2,1,2,10,10),
	(22,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'BF89D6304B3B40A2A325AC8830DAE145',1,NULL,0,'sIP2',NULL,NULL,NULL,5,2,1,2,10,10),
	(23,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'13890D717B1B403283544AFA8F26E0CA',1,NULL,0,'sINPUT',NULL,NULL,NULL,5,2,1,2,10,10),
	(24,'2012-12-20 11:24:31','2013-03-12 12:39:41',X'300AF739354F43AFB4D51682A4CF607C',1,NULL,NULL,'sIP1_lib',21,NULL,NULL,7,7,1,2,10,1),
	(25,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'85E789CF42414F428BE1EA2B00405982',1,NULL,0,'l1INPUT',NULL,NULL,NULL,6,1,2,2,10,10),
	(26,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'D6E601D71CE04434AA7469D80F9A98DA',1,NULL,0,'lIP1',NULL,NULL,NULL,6,1,2,2,10,10);

/*!40000 ALTER TABLE `sample` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplebarcode
# ------------------------------------------------------------



# Dump of table sampledraft
# ------------------------------------------------------------

LOCK TABLES `sampledraft` WRITE;
/*!40000 ALTER TABLE `sampledraft` DISABLE KEYS */;

INSERT INTO `sampledraft` (`id`, `created`, `updated`, `uuid`, `filegroupid`, `jobdraftid`, `labid`, `name`, `samplesubtypeid`, `sampletypeid`, `sourcesampleid`, `status`, `userid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:17:27','2013-03-12 12:39:41',X'6E0E7F8AA4F849C7870BBD85D9E7E0B1',NULL,1,2,'sIP1',5,2,NULL,NULL,10,10),
	(2,'2012-12-20 11:17:34','2013-03-12 12:39:41',X'E5F506CCEFDA4D9EB6F602F7FD3A8F1B',NULL,1,2,'sIP2',5,2,NULL,NULL,10,10),
	(3,'2012-12-20 11:17:41','2013-03-12 12:39:41',X'0EC36DA6EDC54D0ABF063A7F8F54D624',NULL,1,2,'sINPUT',5,2,NULL,NULL,10,10),
	(4,'2012-12-20 11:26:22','2013-03-12 12:39:41',X'CB69E6C5BFAE4CAA9F1B001808781CCB',NULL,2,2,'l1INPUT',6,1,NULL,NULL,10,10),
	(5,'2012-12-20 11:26:37','2013-03-12 12:39:41',X'1BD1CD9EAF764B49B150FFAD483BD203',NULL,2,2,'lIP1',6,1,NULL,NULL,10,10);

/*!40000 ALTER TABLE `sampledraft` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sampledraftjobdraftcellselection
# ------------------------------------------------------------

LOCK TABLES `sampledraftjobdraftcellselection` WRITE;
/*!40000 ALTER TABLE `sampledraftjobdraftcellselection` DISABLE KEYS */;

INSERT INTO `sampledraftjobdraftcellselection` (`id`, `created`, `updated`, `uuid`, `jobdraftcellselectionid`, `libraryindex`, `sampledraftid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'733108152DB646D3A3664D6F2A363883',1,1,1,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'17E6225632AD4354875296C793755029',1,2,2,NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'41BFCC5EECE04067A296BF78D8273BDF',1,3,3,NULL),
	(4,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'5FBCC36BA7064922A5866BCD8F5803DF',2,1,4,NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'1AD4E94DB7AD4C218A2F31CD1F3C6F96',3,1,5,NULL);

/*!40000 ALTER TABLE `sampledraftjobdraftcellselection` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sampledraftmeta
# ------------------------------------------------------------

LOCK TABLES `sampledraftmeta` WRITE;
/*!40000 ALTER TABLE `sampledraftmeta` DISABLE KEYS */;

INSERT INTO `sampledraftmeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `sampledraftid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:16:16','2013-03-12 18:10:00',X'B069F2E8B3D449A3903E41F82C54566F','genericBiomolecule.species',0,NULL,'Human',1,NULL),
	(2,'2012-12-20 11:16:16','2013-03-12 18:10:00',X'E580903047C54568A1524D46360E6371','genericDna.concentration',0,NULL,'26',1,NULL),
	(3,'2012-12-20 11:16:16','2013-03-12 18:10:00',X'E36D00BBA0744516BE06F52792AC4399','genericDna.volume',0,NULL,'10',1,NULL),
	(4,'2012-12-20 11:16:16','2013-03-12 18:10:00',X'1F71EE5B11BA4B45AEA220871268B0A2','genericDna.buffer',0,NULL,'TE',1,NULL),
	(5,'2012-12-20 11:16:16','2013-03-12 18:10:00',X'84A400204CD3473398DC19455FEC1C8F','genericDna.A260_280',0,NULL,'1.76',1,NULL),
	(6,'2012-12-20 11:16:16','2013-03-12 18:10:00',X'405FCFA0C8C940DB933D70B577E1BEA0','genericDna.A260_230',0,NULL,'1.9',1,NULL),
	(7,'2012-12-20 11:16:16','2013-03-12 18:10:00',X'196C80B5CB084EF884C2658F4968D6BF','chipseqDna.fragmentSize',0,NULL,'250',1,NULL),
	(8,'2012-12-20 11:16:16','2013-03-12 18:10:00',X'7B2662CE30664BE4945F8D7981259031','chipseqDna.fragmentSizeSD',0,NULL,'25',1,NULL),
	(9,'2012-12-20 11:16:16','2013-03-12 18:10:00',X'28C3557CEC274A79BF73DE4464C975A2','chipseqDna.antibody',0,NULL,'goat',1,NULL),
	(10,'2012-12-20 11:16:41','2013-03-12 18:10:00',X'9E2BA20D2F8B42F0BA906AE392718951','genericBiomolecule.species',0,NULL,'Human',2,NULL),
	(11,'2012-12-20 11:16:41','2013-03-12 18:10:00',X'048977847FCC43C7B9CC3F6A2EA7C0EF','genericDna.concentration',0,NULL,'23',2,NULL),
	(12,'2012-12-20 11:16:41','2013-03-12 18:10:00',X'67DA3F1E26034524B28509489779EA2F','genericDna.volume',0,NULL,'10',2,NULL),
	(13,'2012-12-20 11:16:41','2013-03-12 18:10:00',X'BF8E6E076B2D4201B0598D794C259705','genericDna.buffer',0,NULL,'TE',2,NULL),
	(14,'2012-12-20 11:16:41','2013-03-12 18:10:00',X'E0DCBBEBBBF74007B7398FE539E9E9EB','genericDna.A260_280',0,NULL,'1.74',2,NULL),
	(15,'2012-12-20 11:16:41','2013-03-12 18:10:00',X'16D75635779A4799900DC7B29E383742','genericDna.A260_230',0,NULL,'1.86',2,NULL),
	(16,'2012-12-20 11:16:41','2013-03-12 18:10:00',X'64C793BF0C0049A49D06902C7C2D26C7','chipseqDna.fragmentSize',0,NULL,'250',2,NULL),
	(17,'2012-12-20 11:16:41','2013-03-12 18:10:00',X'295671BD9E644040A71D4D45F1D15283','chipseqDna.fragmentSizeSD',0,NULL,'25',2,NULL),
	(18,'2012-12-20 11:16:41','2013-03-12 18:10:00',X'19EF906CF04F48E895A41874ECCD2C55','chipseqDna.antibody',0,NULL,'goat',2,NULL),
	(19,'2012-12-20 11:17:07','2013-03-12 18:10:00',X'B10AE89D59C14B56ADA06111ED930C2C','genericBiomolecule.species',0,NULL,'Human',3,NULL),
	(20,'2012-12-20 11:17:07','2013-03-12 18:10:00',X'9F72C5CBB38441E38409EB0D85FD943A','genericDna.concentration',0,NULL,'21',3,NULL),
	(21,'2012-12-20 11:17:07','2013-03-12 18:10:00',X'50C6BEC7694A422D967015D10BBA8C68','genericDna.volume',0,NULL,'10',3,NULL),
	(22,'2012-12-20 11:17:07','2013-03-12 18:10:00',X'B139493D35414248BACF158A45DB9734','genericDna.buffer',0,NULL,'TE',3,NULL),
	(23,'2012-12-20 11:17:07','2013-03-12 18:10:00',X'F33FE6F24137473E907055A7A03F2960','genericDna.A260_280',0,NULL,'1.65',3,NULL),
	(24,'2012-12-20 11:17:07','2013-03-12 18:10:00',X'AF1005FD3FBC4ED885BB3D04BEE2558F','genericDna.A260_230',0,NULL,'1.83',3,NULL),
	(25,'2012-12-20 11:17:07','2013-03-12 18:10:00',X'CA7DE992E65F4807908555D1FAF36F63','chipseqDna.fragmentSize',0,NULL,'250',3,NULL),
	(26,'2012-12-20 11:17:07','2013-03-12 18:10:00',X'336C3F629C374CE987AD493846DBF693','chipseqDna.fragmentSizeSD',0,NULL,'25',3,NULL),
	(27,'2012-12-20 11:17:07','2013-03-12 18:10:00',X'A6269C3E474C44C48412C5FCDAF3A154','chipseqDna.antibody',0,NULL,'goat',3,NULL),
	(28,'2012-12-20 11:26:22','2013-03-12 18:10:00',X'5AB6053D2082422DA07457724D70A10A','genericBiomolecule.species',0,NULL,'Human',4,NULL),
	(29,'2012-12-20 11:26:22','2013-03-12 18:10:00',X'7C04E269FA8C478EAF373F2B0BDFFFA0','chipseqDna.fragmentSize',0,NULL,'200',4,NULL),
	(30,'2012-12-20 11:26:22','2013-03-12 18:10:00',X'E8D6E4B6C3E5496BB576ECB0718E6DFC','chipseqDna.fragmentSizeSD',0,NULL,'10',4,NULL),
	(31,'2012-12-20 11:26:22','2013-03-12 18:10:00',X'70F7BDF0D7C44AE2ADE91B798E1391DC','chipseqDna.antibody',0,NULL,'sheep',4,NULL),
	(32,'2012-12-20 11:26:22','2013-03-12 18:10:00',X'A15EE34A38C045C69A83D0F9E4C10465','genericLibrary.concentration',0,NULL,'34',4,NULL),
	(33,'2012-12-20 11:26:22','2013-03-12 18:10:00',X'55676D281F054D2885720EDD93658BB3','genericLibrary.volume',0,NULL,'10',4,NULL),
	(34,'2012-12-20 11:26:22','2013-03-12 18:10:00',X'231065FE2C5F4563880F343C168D369B','genericLibrary.buffer',0,NULL,'Water',4,NULL),
	(35,'2012-12-20 11:26:22','2013-03-12 18:10:00',X'73E51D6971914640BD07D1C14E8A2322','genericLibrary.adaptorset',0,NULL,'2',4,NULL),
	(36,'2012-12-20 11:26:22','2013-03-12 18:10:00',X'59A5E758274C42DABD478DF3B89B1A17','genericLibrary.adaptor',0,NULL,'3',4,NULL),
	(37,'2012-12-20 11:26:22','2013-03-12 18:10:00',X'1E6F0E540D204D5E95B23F103745D01D','genericLibrary.size',0,NULL,'500',4,NULL),
	(38,'2012-12-20 11:26:22','2013-03-12 18:10:00',X'760F4055293B424A80C6FB74748D3B0F','genericLibrary.sizeSd',0,NULL,'55',4,NULL),
	(39,'2012-12-20 11:26:37','2013-03-12 18:10:00',X'4B7D6B6C8B5447048EA9134866C9EC4D','genericBiomolecule.species',0,NULL,'Human',5,NULL),
	(40,'2012-12-20 11:26:37','2013-03-12 18:10:00',X'69DDAA47A0204197B327EE787348222D','chipseqDna.fragmentSize',0,NULL,'200',5,NULL),
	(41,'2012-12-20 11:26:37','2013-03-12 18:10:00',X'C33FC87CACC644B5AAD8799B05048594','chipseqDna.fragmentSizeSD',0,NULL,'10',5,NULL),
	(42,'2012-12-20 11:26:37','2013-03-12 18:10:00',X'B9446BA532BD48C397B3B62E5723A6D4','chipseqDna.antibody',0,NULL,'sheep',5,NULL),
	(43,'2012-12-20 11:26:37','2013-03-12 18:10:00',X'60B16134B10B4B7B8FBC91A9CF316B2F','genericLibrary.concentration',0,NULL,'34',5,NULL),
	(44,'2012-12-20 11:26:37','2013-03-12 18:10:00',X'DF9BFB08AE594D6AAFF181C7493D6446','genericLibrary.volume',0,NULL,'10',5,NULL),
	(45,'2012-12-20 11:26:37','2013-03-12 18:10:00',X'985581D9515843B5BD030002F80CF368','genericLibrary.buffer',0,NULL,'Water',5,NULL),
	(46,'2012-12-20 11:26:37','2013-03-12 18:10:00',X'B4184D552E6B46F0A24FCF9201FAAE6F','genericLibrary.adaptorset',0,NULL,'2',5,NULL),
	(47,'2012-12-20 11:26:37','2013-03-12 18:10:00',X'BB6EFDC90F864AA6A9C456E599D626C6','genericLibrary.adaptor',0,NULL,'3',5,NULL),
	(48,'2012-12-20 11:26:37','2013-03-12 18:10:00',X'9612A60291CD41AF871C8220A7C079F5','genericLibrary.size',0,NULL,'500',5,NULL),
	(49,'2012-12-20 11:26:37','2013-03-12 18:10:00',X'A901AC497F9848B99765E1A0A208594B','genericLibrary.sizeSd',0,NULL,'55',5,NULL);

/*!40000 ALTER TABLE `sampledraftmeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplefile
# ------------------------------------------------------------



# Dump of table samplejobcellselection
# ------------------------------------------------------------

LOCK TABLES `samplejobcellselection` WRITE;
/*!40000 ALTER TABLE `samplejobcellselection` DISABLE KEYS */;

INSERT INTO `samplejobcellselection` (`id`, `created`, `updated`, `uuid`, `jobcellselectionid`, `libraryindex`, `sampleid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'499201CEC4E54D37B46132C021A31E06',1,1,21,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'301F44587CBB4CFB8339E2DB0FD2655B',1,2,22,NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'1E9BD2AE1DC845CF87489721D2F615A6',1,3,23,NULL),
	(4,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'156BA4ABBA654B58A16782826E1C50A7',2,1,25,NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'8879CBD64A374DBAB1928D8F2FE4E0A5',3,1,26,NULL);

/*!40000 ALTER TABLE `samplejobcellselection` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplelab
# ------------------------------------------------------------



# Dump of table samplemeta
# ------------------------------------------------------------

LOCK TABLES `samplemeta` WRITE;
/*!40000 ALTER TABLE `samplemeta` DISABLE KEYS */;

INSERT INTO `samplemeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `sampleid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:09:20','2013-03-12 18:10:00',X'D48681B0F47E4650AFCDA411F9EFD4C2','platformunitInstance.readType',0,NULL,'paired',1,NULL),
	(2,'2012-12-20 11:09:20','2013-03-12 18:10:00',X'895CC8BB622B48FA96A40105A9D82489','platformunitInstance.readlength',0,NULL,'100',1,NULL),
	(3,'2012-12-20 11:09:20','2013-03-12 18:10:00',X'6385532C82854DBA91E707FF9BA1BD69','platformunitInstance.comment',0,NULL,'',1,NULL),
	(4,'2012-12-20 11:09:48','2013-03-12 18:10:00',X'589A65A37046468B9E0DDB5CD306ED93','platformunitInstance.readType',0,NULL,'single',10,NULL),
	(5,'2012-12-20 11:09:48','2013-03-12 18:10:00',X'A3A2C157F0D74B6CA99A824D1DF57F72','platformunitInstance.readlength',0,NULL,'75',10,NULL),
	(6,'2012-12-20 11:09:48','2013-03-12 18:10:00',X'3950DAEECF90452F8C7736FB8D71C300','platformunitInstance.comment',0,NULL,'',10,NULL),
	(7,'2012-12-20 11:10:27','2013-03-12 18:10:00',X'1EB9B597489F4380BEA30986B7E14841','platformunitInstance.readType',0,NULL,'paired',19,NULL),
	(8,'2012-12-20 11:10:27','2013-03-12 18:10:00',X'CAF94594ECEE48D3A57AF68E17F5F586','platformunitInstance.readlength',0,NULL,'150',19,NULL),
	(9,'2012-12-20 11:10:27','2013-03-12 18:10:00',X'11322D546C1D4C8DA90EB8056784E2A9','platformunitInstance.comment',0,NULL,'',19,NULL),
	(10,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'DCEABE600700447FBED17355E9A361CA','genericBiomolecule.species',0,NULL,'Human',21,10),
	(11,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'2970A579B84A426CB6CD9CB24647B5C0','genericDna.concentration',0,NULL,'26',21,10),
	(12,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'F533AAE2A56D48E49CF3BF24205AA534','genericDna.volume',0,NULL,'10',21,10),
	(13,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'B217D62A1FFA4B97B96713310F4CC280','genericDna.buffer',0,NULL,'TE',21,10),
	(14,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'ADC1194CA2354D10B18C6F6356B1BC59','genericDna.A260_280',0,NULL,'1.76',21,10),
	(15,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'5086C61DBCBA48B49C38AFB297EBE11E','genericDna.A260_230',0,NULL,'1.9',21,10),
	(16,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'644F1BE19F354E1AA94CADD17D272605','chipseqDna.fragmentSize',0,NULL,'250',21,10),
	(17,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'CB556B119F0949FCB904E136349E6E1D','chipseqDna.fragmentSizeSD',0,NULL,'25',21,10),
	(18,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'24C25481E73446C481C8873D3D3B5EE3','chipseqDna.antibody',0,NULL,'goat',21,10),
	(19,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'957154B2F3AC4810952D9F11581DB813','genericBiomolecule.species',0,NULL,'Human',22,10),
	(20,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'20EFDF4CCC264B78A479B157782A210E','genericDna.concentration',0,NULL,'23',22,10),
	(21,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'138C7519B1F044798B8E4CB083998758','genericDna.volume',0,NULL,'10',22,10),
	(22,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'35D077547FEF4615BB07A867C072AED0','genericDna.buffer',0,NULL,'TE',22,10),
	(23,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'39DFE209EB2948A38D3CDE0ECCF2AF00','genericDna.A260_280',0,NULL,'1.74',22,10),
	(24,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'D6E54290C60B49AEB6A12BA22181FFAC','genericDna.A260_230',0,NULL,'1.86',22,10),
	(25,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'A053EE489F42445589DE1C73D6A643BD','chipseqDna.fragmentSize',0,NULL,'250',22,10),
	(26,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'8FC320CB7E554CCF83D3D62BD70D6ED7','chipseqDna.fragmentSizeSD',0,NULL,'25',22,10),
	(27,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'EDFA96F00E3849DB88C56825812DA2B9','chipseqDna.antibody',0,NULL,'goat',22,10),
	(28,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'1EB0D68F45994DE099C4CAF5FEADE331','genericBiomolecule.species',0,NULL,'Human',23,10),
	(29,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'130514D3EDD84E4A9ACFD881FE274139','genericDna.concentration',0,NULL,'21',23,10),
	(30,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'0D7488F6281643CD85524F8231737F5D','genericDna.volume',0,NULL,'10',23,10),
	(31,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'91B754294BE84581B169A87587ED53DB','genericDna.buffer',0,NULL,'TE',23,10),
	(32,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'B90D4BBF9AFF4052A3724CB69E8500FD','genericDna.A260_280',0,NULL,'1.65',23,10),
	(33,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'0921EC59FDAB4B8FB0DD84011D14F9D0','genericDna.A260_230',0,NULL,'1.83',23,10),
	(34,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'44DE2012A4EA43628A9D7AC411E4AC1F','chipseqDna.fragmentSize',0,NULL,'250',23,10),
	(35,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'CA0F49DC2B8B4E9AB26F7F38FD02DA3F','chipseqDna.fragmentSizeSD',0,NULL,'25',23,10),
	(36,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'1E871A229C044DB3B043CE4746312FD4','chipseqDna.antibody',0,NULL,'goat',23,10),
	(37,'2012-12-20 11:23:35','2013-03-12 12:39:41',X'CA6F2F129B89434AA0F5E77646534B61','statusMessage.sampleQCComment::4e42daec-91c3-484f-8cf8-1dad722fc4f8',0,NULL,'Sample QC Comment::slightly concerned about size range but going to proceed anyway.',23,1),
	(38,'2012-12-20 11:24:31','2013-03-12 18:10:00',X'0815D70A8E404E65BAD8FD613AF47E5F','genericLibrary.concentration',0,NULL,'25',24,NULL),
	(39,'2012-12-20 11:24:31','2013-03-12 18:10:00',X'91418A618D654926A500D5F2CD60CB7A','genericLibrary.adaptor',0,NULL,'2',24,NULL),
	(40,'2012-12-20 11:24:31','2013-03-12 18:10:00',X'1BD036957E1240C6A864FA2E9010F604','genericLibrary.volume',0,NULL,'50',24,NULL),
	(41,'2012-12-20 11:24:31','2013-03-12 18:10:00',X'D9ECB8D3C9144DDD8261996DC3A360BF','genericLibrary.adaptorset',0,NULL,'2',24,NULL),
	(42,'2012-12-20 11:24:31','2013-03-12 18:10:00',X'8A5AFAE0363D40458CDD543B12D736CF','genericLibrary.sizeSd',0,NULL,'20',24,NULL),
	(43,'2012-12-20 11:24:31','2013-03-12 18:10:00',X'2FA9DCE3A4B14B5FACBC9F412E651BD0','genericLibrary.size',0,NULL,'400',24,NULL),
	(44,'2012-12-20 11:24:31','2013-03-12 18:10:00',X'37F36B58736941B5ABE39626D5AB6D60','genericLibrary.buffer',0,NULL,'TE',24,NULL),
	(45,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'7CF1EEC937104359A77D6254FB36351E','genericBiomolecule.species',0,NULL,'Human',25,10),
	(46,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'2CDAAD2B7A4B491E8C7F960F56F76B85','chipseqDna.fragmentSize',0,NULL,'200',25,10),
	(47,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'3653ACF9DBCE499FAD9FEDABCD05CD83','chipseqDna.fragmentSizeSD',0,NULL,'10',25,10),
	(48,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'882EEB7141F245CEB87268A22AD04488','chipseqDna.antibody',0,NULL,'sheep',25,10),
	(49,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'E543816081FC497B823199E95CBBC78F','genericLibrary.concentration',0,NULL,'34',25,10),
	(50,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'E74EF0EE73B04F6B9597A09357FF6AC7','genericLibrary.volume',0,NULL,'10',25,10),
	(51,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'E805864595104758BF39248E472D7632','genericLibrary.buffer',0,NULL,'Water',25,10),
	(52,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'FC55C33AF2B04990AE0952C4EAB56928','genericLibrary.adaptorset',0,NULL,'2',25,10),
	(53,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'220F98AC482C488A97E1B828C321088D','genericLibrary.adaptor',0,NULL,'3',25,10),
	(54,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'CEEAE13B02CF4A70AA86B7783E374C19','genericLibrary.size',0,NULL,'500',25,10),
	(55,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'A74354B0957B4BAF89C4E370B9A54E86','genericLibrary.sizeSd',0,NULL,'55',25,10),
	(56,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'680447EF49224E3E8D6B9917441DEFA6','genericBiomolecule.species',0,NULL,'Human',26,10),
	(57,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'FE9F88CEC8214A298DBA77E087E996AC','chipseqDna.fragmentSize',0,NULL,'200',26,10),
	(58,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'FF8084325137416493B949D26902134B','chipseqDna.fragmentSizeSD',0,NULL,'10',26,10),
	(59,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'489BA118BB094A639898E074B7BFCF15','chipseqDna.antibody',0,NULL,'sheep',26,10),
	(60,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'A3A0A88D5C474DA19DE7CF788457BEDB','genericLibrary.concentration',0,NULL,'34',26,10),
	(61,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'E87727E7BC624A76891775E9186CAD99','genericLibrary.volume',0,NULL,'10',26,10),
	(62,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'B6865F8BB60245D3A7752D373324CCD5','genericLibrary.buffer',0,NULL,'Water',26,10),
	(63,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'70E89306F60140619FA51097992DB6E5','genericLibrary.adaptorset',0,NULL,'2',26,10),
	(64,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'AF51CE7CA0A747359AC1C3AA0381167A','genericLibrary.adaptor',0,NULL,'3',26,10),
	(65,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'C3A4317141EE42A69B8CC802CCD26B89','genericLibrary.size',0,NULL,'500',26,10),
	(66,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'366D72673A3A40D9887CF1C630701A6F','genericLibrary.sizeSd',0,NULL,'55',26,10);

/*!40000 ALTER TABLE `samplemeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplesource
# ------------------------------------------------------------

LOCK TABLES `samplesource` WRITE;
/*!40000 ALTER TABLE `samplesource` DISABLE KEYS */;

INSERT INTO `samplesource` (`id`, `created`, `updated`, `uuid`, `indexvalue`, `sampleid`, `source_sampleid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'A8EBCDB91BE3457E8C10998660562D79',1,1,2,1),
	(2,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'DD5E09225DB54B62BD81F5E7A645D693',2,1,3,1),
	(3,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'351163F860204F75A15BF8153CC4B78C',3,1,4,1),
	(4,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'8E3E1384189D478E8558EE56DFF8A51C',4,1,5,1),
	(5,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'4FB7939784D34EC78C9EE967E92C4EE4',5,1,6,1),
	(6,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'3E6183D1073F46AE9E53ED4834ECC613',6,1,7,1),
	(7,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'A0D91D35C99749FD9CCD1F1F2D0D6693',7,1,8,1),
	(8,'2012-12-20 11:09:20','2013-03-12 12:39:41',X'EF20AD0180CF47619A92D0BB6DC539B0',8,1,9,1),
	(9,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'809171F5086C459C9E891BEA0E0A33B0',1,10,11,1),
	(10,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'7F698F0DCE434893B9EBEE096E6CF04A',2,10,12,1),
	(11,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'60912FD02BCD4E749B8AD5FB6B318F66',3,10,13,1),
	(12,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'4B74F74C29BB426DB67F51A2A340F2C6',4,10,14,1),
	(13,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'25C6487C968042C9BFCC0C4C9B2A4204',5,10,15,1),
	(14,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'49993614BD004053B5FA29F175403B32',6,10,16,1),
	(15,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'775DD7A8A23C4EF4AFABB3493125E9CF',7,10,17,1),
	(16,'2012-12-20 11:09:48','2013-03-12 12:39:41',X'6BC1B27A7D734D10B0DB61923D0D6A96',8,10,18,1),
	(17,'2012-12-20 11:10:27','2013-03-12 12:39:41',X'36E53F3C741B47F39B125467B63DB191',1,19,20,1);

/*!40000 ALTER TABLE `samplesource` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplesourcefile
# ------------------------------------------------------------



# Dump of table samplesourcemeta
# ------------------------------------------------------------



# Dump of table samplesubtype
# ------------------------------------------------------------

LOCK TABLES `samplesubtype` WRITE;
/*!40000 ALTER TABLE `samplesubtype` DISABLE KEYS */;

INSERT INTO `samplesubtype` (`id`, `created`, `updated`, `uuid`, `arealist`, `iname`, `isactive`, `name`, `sampletypeid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:10:03',X'428F1072219342EC81C93789CB278210','genericBiomolecule,genericLibrary','controlLibrarySample',1,'Control Library',1,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:10:04',X'501480C681C542B4A3243E16AB34C7B6','genericBiomolecule,genericDna,bisulseqDna','bisulseqDnaSample',1,'BISUL-seq DNA',2,NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:10:04',X'8BD45094E59243F2962D94544985368C','genericBiomolecule,bisulseqDna,genericLibrary','bisulseqLibrarySample',1,'BISUL-seq Library',1,NULL),
	(4,'2013-03-12 12:39:40','2013-03-12 18:10:04',X'1414667765554DB8936CA012AFC4AECB','genericLibrary','bisulseqFacilityLibrarySample',1,'BISUL-seq Facility Library',1,NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:10:04',X'389183F56BC6413DB26FD4AFA63E212E','genericBiomolecule,genericDna,chipseqDna','chipseqDnaSample',1,'ChIP-seq DNA',2,NULL),
	(6,'2013-03-12 12:39:40','2013-03-12 18:10:04',X'31F05F7849F44DA5B3FF9A70B9AB6E38','genericBiomolecule,chipseqDna,genericLibrary','chipseqLibrarySample',1,'ChIP-seq Library',1,NULL),
	(7,'2013-03-12 12:39:40','2013-03-12 18:10:04',X'ACE32C32B9AF42BFBE68468CE1642F30','genericLibrary','chipseqFacilityLibrarySample',1,'ChIP-seq Facility Library',1,NULL),
	(8,'2013-03-12 12:39:40','2013-03-12 18:10:04',X'D3CA3015796241F4BC9FE14E3D92F981','genericBiomolecule,genericLibrary,helptagLibrary','helptagLibrarySample',1,'HELP-tag Library',1,NULL),
	(9,'2013-03-12 12:39:40','2013-03-12 18:10:05',X'7C616F066B2F4A6E9C7BCA92043B20C2','illuminaFlowcellMiSeqV1','illuminaFlowcellMiSeqV1',1,'Illumina Flow Cell MiSeq V1',5,NULL),
	(10,'2013-03-12 12:39:40','2013-03-12 18:10:05',X'D3241DDE0D354428BDCCF80ED50D90E0','illuminaFlowcellV3','illuminaFlowcellV3',1,'Illumina Flow Cell Version 3',5,NULL),
	(11,'2013-03-12 12:39:45','2013-03-12 18:10:04',X'C05159DBAC2E4450B9CD2ABD6987F6FC','genericBiomolecule,genericDna','genericDnaDnaSample',1,'Generic DNA Seq DNA',2,NULL),
	(12,'2013-03-12 12:39:45','2013-03-12 18:10:04',X'BC8F78EEE13F477E8C9AA4A31D374C63','genericBiomolecule,genericLibrary','genericDnaLibrarySample',1,'Generic DNA Seq Library',1,NULL),
	(13,'2013-03-12 12:39:45','2013-03-12 18:10:04',X'D75BD89988A34E9C8C010387DBF4517E','genericLibrary','genericDnaFacilityLibrarySample',1,'Generic DNA Seq Facility Library',1,NULL);

/*!40000 ALTER TABLE `samplesubtype` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplesubtypemeta
# ------------------------------------------------------------

LOCK TABLES `samplesubtypemeta` WRITE;
/*!40000 ALTER TABLE `samplesubtypemeta` DISABLE KEYS */;

INSERT INTO `samplesubtypemeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `samplesubtypeid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:03:30','2013-03-12 12:39:41',X'70C51AEA76684907AD1FFA912CC5910A','controlLibrarySample.includeRoles',1,NULL,'ft,su',1,0),
	(2,'2012-12-20 11:03:30','2013-03-12 12:39:41',X'C0F086A2F8D043AF8866CFAEEAE8B1B8','bisulseqDnaSample.includeRoles',1,NULL,'ft,lu',2,0),
	(3,'2012-12-20 11:03:30','2013-03-12 12:39:41',X'EAF05D61CCD443768C8DED0A06450DC6','bisulseqLibrarySample.includeRoles',1,NULL,'lu',3,0),
	(4,'2012-12-20 11:03:30','2013-03-12 12:39:41',X'90731F13B7314384B1286F8B5E05846D','bisulseqFacilityLibrarySample.includeRoles',1,NULL,'ft',4,0),
	(5,'2012-12-20 11:03:30','2013-03-12 12:39:41',X'FE8EDE58EFC04B348825CC5B50FD01AE','chipseqDnaSample.includeRoles',1,NULL,'ft,lu',5,0),
	(6,'2012-12-20 11:03:31','2013-03-12 12:39:41',X'593CDBB7D9FA456D8724C81FAF687F70','chipseqLibrarySample.includeRoles',1,NULL,'lu',6,0),
	(7,'2012-12-20 11:03:31','2013-03-12 12:39:41',X'C7A3764FE59A47B6AD0D0906658FF706','chipseqFacilityLibrarySample.includeRoles',1,NULL,'ft',7,0),
	(8,'2012-12-20 11:03:31','2013-03-12 12:39:41',X'918622B3991C45D194E2CD3AB66C9365','helptagLibrarySample.includeRoles',1,NULL,'ft,lu',8,0),
	(9,'2012-12-20 11:03:32','2013-03-12 12:39:41',X'E8C6A0BB73764F30915E222CCDC125ED','illuminaFlowcellMiSeqV1.maxCellNumber',1,NULL,'1',9,0),
	(10,'2012-12-20 11:19:23','2013-03-12 12:39:41',X'47E20028C2764A769B070FD893F76FF7','illuminaFlowcellMiSeqV1.multiplicationFactor',2,NULL,'1',9,0),
	(11,'2012-12-20 11:03:32','2013-03-12 12:39:41',X'CA6CF3BC387F420E84E919753C23FDB9','illuminaFlowcellV3.maxCellNumber',1,NULL,'8',10,0),
	(12,'2012-12-20 11:19:23','2013-03-12 12:39:41',X'E7E47544BBC74FDFB3E230600AE111F5','illuminaFlowcellV3.multiplicationFactor',2,NULL,'2',10,0),
	(13,'2013-03-12 12:39:45','2013-03-12 18:10:00',X'36CFE8FFB69B4691B45C7D6ED2C3A2E3','genericDnaDnaSample.includeRoles',1,NULL,'ft,lu',11,NULL),
	(14,'2013-03-12 12:39:45','2013-03-12 18:10:00',X'0EEB4B3DFC44476C9C292C21EC2E9778','genericDnaLibrarySample.includeRoles',1,NULL,'lu',12,NULL),
	(15,'2013-03-12 12:39:45','2013-03-12 18:10:00',X'D076ED4458314B79907068DF2DD6D611','genericDnaFacilityLibrarySample.includeRoles',1,NULL,'ft',13,NULL);

/*!40000 ALTER TABLE `samplesubtypemeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplesubtyperesourcecategory
# ------------------------------------------------------------

LOCK TABLES `samplesubtyperesourcecategory` WRITE;
/*!40000 ALTER TABLE `samplesubtyperesourcecategory` DISABLE KEYS */;

INSERT INTO `samplesubtyperesourcecategory` (`id`, `created`, `updated`, `uuid`, `resourcecategoryid`, `samplesubtypeid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'B4FF6EBB9E5D46D9827FDFE9D083D7FE',2,9,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'6E3FFF625A3744D5A3B8F5033809DE4A',1,10,NULL);

/*!40000 ALTER TABLE `samplesubtyperesourcecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sampletype
# ------------------------------------------------------------

LOCK TABLES `sampletype` WRITE;
/*!40000 ALTER TABLE `sampletype` DISABLE KEYS */;

INSERT INTO `sampletype` (`id`, `created`, `updated`, `uuid`, `iname`, `isactive`, `name`, `sampletypecategoryid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:10:03',X'5A76F12B925E4C739FC4FB88C72FF824','library',1,'Library',1,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:10:03',X'75D4337618814B228C4E3AE57784A275','dna',1,'DNA',1,NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:10:03',X'E0470BB5B471478BB80165FB193B8BD5','rna',1,'RNA',1,NULL),
	(4,'2013-03-12 12:39:40','2013-03-12 18:10:03',X'019E9B91FD0044ACBF7667B318A83DBA','cell',1,'Cell',2,NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:10:03',X'06ACDFC22CC64AD1BE5EA457E6FA5243','platformunit',1,'Platform Unit',2,NULL),
	(6,'2013-03-12 12:39:40','2013-03-12 18:10:03',X'384B075E967C44A6BCB92992EF2498F6','tissue',1,'Tissue',1,NULL),
	(7,'2013-03-12 12:39:40','2013-03-12 18:10:03',X'25F322AB0DF94853885C011884F69858','facilityLibrary',1,'Facilitylibrary',1,NULL);

/*!40000 ALTER TABLE `sampletype` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sampletypecategory
# ------------------------------------------------------------

LOCK TABLES `sampletypecategory` WRITE;
/*!40000 ALTER TABLE `sampletypecategory` DISABLE KEYS */;

INSERT INTO `sampletypecategory` (`id`, `created`, `updated`, `uuid`, `iname`, `isactive`, `name`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:10:03',X'37D5DFB2E54042BE97C1896BA323DAE0','biomaterial',1,'Biomaterial',NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:10:03',X'658F41524B874BBB9C63F72873D1EA12','hardware',1,'Hardware',NULL);

/*!40000 ALTER TABLE `sampletypecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table software
# ------------------------------------------------------------

LOCK TABLES `software` WRITE;
/*!40000 ALTER TABLE `software` DISABLE KEYS */;

INSERT INTO `software` (`id`, `created`, `updated`, `uuid`, `iname`, `isactive`, `name`, `resourcetypeid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:03:29','2013-03-12 18:10:03',X'63A5B8381ACB4B3692E5A7C4EFA1D597','bowtieAligner',1,'Bowtie Aligner',8,0),
	(2,'2012-12-20 11:03:30','2013-03-12 18:10:03',X'20A9C2C6F306419F9AD1FC999C78EB95','macsPeakcaller',1,'MACS Peakcaller',4,0),
	(3,'2012-12-20 11:03:30','2013-03-12 18:10:03',X'2B2B1550FE3F406B86A33D4B0B61EB8C','bisulfiteSeqPipeline',1,'BISUL-seq Pipeline',6,0),
	(4,'2012-12-20 11:03:31','2013-03-12 18:10:04',X'5E846435B5654EE7A410353FA98B2930','helptagPipeline',1,'HELP-tag Pipeline',7,0),
	(5,'2012-12-20 11:03:31','2013-03-12 18:10:04',X'5C194015AFE94748B28D11E0101B2BE6','casava',1,'CASAVA',5,0);

/*!40000 ALTER TABLE `software` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table softwaremeta
# ------------------------------------------------------------

LOCK TABLES `softwaremeta` WRITE;
/*!40000 ALTER TABLE `softwaremeta` DISABLE KEYS */;

INSERT INTO `softwaremeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `softwareid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:03:29','2013-03-12 12:39:41',X'3C206E1F8DB441CCBECFBD2ED476E46B','bowtieAligner.currentVersion',1,NULL,'0.12.7',1,0),
	(2,'2012-12-20 11:03:29','2013-03-12 12:39:41',X'D9F7717276FA4FA4934278C52F442D7B','bowtieAligner.priorVersions',2,NULL,'',1,0),
	(3,'2012-12-20 11:03:30','2013-03-12 12:39:41',X'6BB8CA249DC046E68D770D937A366E6A','macsPeakcaller.currentVersion',1,NULL,'4.1',2,0),
	(4,'2012-12-20 11:03:30','2013-03-12 12:39:41',X'A8D9EC57C23149C5AD4CB4E012BE2BB3','macsPeakcaller.priorVersions',2,NULL,'',2,0),
	(5,'2012-12-20 11:03:30','2013-03-12 12:39:41',X'3112D95D365E46F5B30BDCA129A53943','bisulfiteSeqPipeline.currentVersion',1,NULL,'1.0',3,0),
	(6,'2012-12-20 11:03:30','2013-03-12 12:39:41',X'05CA3DBDEA9B43FFAB7F69774C982A19','bisulfiteSeqPipeline.priorVersions',2,NULL,'',3,0),
	(7,'2012-12-20 11:03:31','2013-03-12 12:39:41',X'68E358EB5D1A4E1D9AAEB8D02E7E10FE','helptagPipeline.currentVersion',1,NULL,'1.0',4,0),
	(8,'2012-12-20 11:03:31','2013-03-12 12:39:41',X'71B7F04AAE7449B695B45C9B2C597009','helptagPipeline.priorVersions',2,NULL,'',4,0),
	(9,'2012-12-20 11:03:31','2013-03-12 12:39:41',X'5A278289DA454746921F668D6BFB9B12','casava.currentVersion',1,NULL,'1.8.2',5,0),
	(10,'2012-12-20 11:03:31','2013-03-12 12:39:41',X'252029E8BE7241799818094E49D4B442','casava.priorVersions',2,NULL,'',5,0);

/*!40000 ALTER TABLE `softwaremeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table uifield
# ------------------------------------------------------------

LOCK TABLES `uifield` WRITE;
/*!40000 ALTER TABLE `uifield` DISABLE KEYS */;

INSERT INTO `uifield` (`id`, `created`, `updated`, `uuid`, `area`, `attrname`, `attrvalue`, `domain`, `locale`, `name`, `lastupdatebyuser`)
VALUES
	(1,NULL,NULL,NULL,'acctQuote','label','List of Job Quotes','','en_US','acctquote_list',1),
	(2,NULL,NULL,NULL,'acctQuote','label','Quote Amount','','en_US','amount',1),
	(3,NULL,NULL,NULL,'acctQuote','label','Quote created successfully.','','en_US','created_success',1),
	(4,NULL,NULL,NULL,'acctQuote','label','PI','','en_US','lab',1),
	(5,NULL,NULL,NULL,'acctQuote','label','Cell Cost','','en_US','lane_cost',1),
	(6,NULL,NULL,NULL,'acctQuote','metaposition','40','','en_US','lane_cost',1),
	(7,NULL,NULL,NULL,'acctQuote','label','Library Cost','','en_US','library_cost',1),
	(8,NULL,NULL,NULL,'acctQuote','metaposition','20','','en_US','library_cost',1),
	(9,NULL,NULL,NULL,'acctQuote','label','Job ID','','en_US','jobId',1),
	(10,NULL,NULL,NULL,'acctQuote','label','Job Name','','en_US','name',1),
	(11,NULL,NULL,NULL,'acctQuote','label','Sample Cost','','en_US','sample_cost',1),
	(12,NULL,NULL,NULL,'acctQuote','metaposition','30','','en_US','sample_cost',1),
	(13,NULL,NULL,NULL,'acctQuote','label','Submitted On','','en_US','submitted_on',1),
	(14,NULL,NULL,NULL,'acctQuote','label','Submitted By','','en_US','submitter',1),
	(15,NULL,NULL,NULL,'activePlatformUnit','label','Active Platform Units Awaiting Libraries','','en_US','tableHeader',1),
	(16,NULL,NULL,NULL,'activePlatformUnit','label','Name','','en_US','name',1),
	(17,NULL,NULL,NULL,'activePlatformUnit','label','Barcode','','en_US','barcode',1),
	(18,NULL,NULL,NULL,'activePlatformUnit','label','Type','','en_US','type',1),
	(19,NULL,NULL,NULL,'jobListAssignLibrary','label','Cells','','en_US','numberLanes',1),
	(20,NULL,NULL,NULL,'auth','data','Submit','','en_US','confirmemail',1),
	(21,NULL,NULL,NULL,'auth','label','Auth code','','en_US','confirmemail_authcode',1),
	(22,NULL,NULL,NULL,'auth','error','Invalid authorization code provided','','en_US','confirmemail_badauthcode',1),
	(23,NULL,NULL,NULL,'auth','error','email address is incorrect','','en_US','confirmemail_bademail',1),
	(24,NULL,NULL,NULL,'auth','error','Captcha text incorrect','','en_US','confirmemail_captcha',1),
	(25,NULL,NULL,NULL,'auth','label','Captcha text','','en_US','confirmemail_captcha',1),
	(26,NULL,NULL,NULL,'auth','error','email address in url cannot be decoded','','en_US','confirmemail_corruptemail',1),
	(27,NULL,NULL,NULL,'auth','label','Email Address','','en_US','confirmemail_email',1),
	(28,NULL,NULL,NULL,'auth','label','Submit','','en_US','confirmemail_submit',1),
	(29,NULL,NULL,NULL,'auth','error','User email address and authorization code provided do not match','','en_US','confirmemail_wronguser',1),
	(30,NULL,NULL,NULL,'auth','label','Please enter a valid email address. This is required for demonstrating functionality which results in an email being sent by the system. This email address will be stored for 24h in a cookie on your personal computer only - we do NOT store your email address or any sent mail on our server. For demonstration purposes it is used to override the send-to email address of all demonstration user accounts and mock account applications you might make.','','en_US','get_email_instructions',1),
	(31,NULL,NULL,NULL,'auth','label','Please enter your email address','','en_US','demo_email',1),
	(32,NULL,NULL,NULL,'auth','error','You must supply an email address','','en_US','demo_email',1),
	(33,NULL,NULL,NULL,'auth','label','Submit','','en_US','demo_email_submit',1),
	(34,NULL,NULL,NULL,'auth','label','Please click to <a href=\"../login.do\"/>Login</a>','','en_US','directusertologin',1),
	(35,NULL,NULL,NULL,'auth','data','Login','','en_US','login',1),
	(36,NULL,NULL,NULL,'auth','label','About','','en_US','login_anchor_about',1),
	(37,NULL,NULL,NULL,'auth','label','Forgot Password','','en_US','login_anchor_forgotpass',1),
	(38,NULL,NULL,NULL,'auth','label','New PI','','en_US','login_anchor_newpi',1),
	(39,NULL,NULL,NULL,'auth','label','New User','','en_US','login_anchor_newuser',1),
	(40,NULL,NULL,NULL,'auth','error','Your login attempt was not successful. Try again.','','en_US','login_failed',1),
	(41,'2013-03-12 12:39:31','2013-03-12 12:39:31',X'26720C5FBE9745ECBA346CAF2F204F89','auth','label','Please login to the WASP System using your username and password. If you have forgotten your password, or if you are currently unregistered and wish to create an account, please select from the links below.','','en_US','login_instructions',1),
	(42,NULL,NULL,NULL,'auth','label','Password','','en_US','login_password',1),
	(43,NULL,NULL,NULL,'auth','label','Reason','','en_US','login_reason',1),
	(44,NULL,NULL,NULL,'auth','label','Login','','en_US','login_submit',1),
	(45,NULL,NULL,NULL,'auth','label','User','','en_US','login_user',1),
	(46,NULL,NULL,NULL,'auth','error','There is a problem confirming your email address as a new user. Please try again or contact a WASP administrator.','','en_US','newuser_confirmemail_wronguser',1),
	(47,NULL,NULL,NULL,'auth','error','Failed to authenticate with supplied login credentials','','en_US','requestEmailChange_badcredentials',1),
	(48,NULL,NULL,NULL,'auth','error','email address is of incorrect format','','en_US','requestEmailChange_bademail',1),
	(49,NULL,NULL,NULL,'auth','error','Captcha text incorrect','','en_US','requestEmailChange_captcha',1),
	(50,NULL,NULL,NULL,'auth','label','New Email Address','','en_US','requestEmailChange_email',1),
	(51,NULL,NULL,NULL,'auth','label','Submit','','en_US','requestEmailChange_submit',1),
	(52,NULL,NULL,NULL,'auth','data','Submit','','en_US','resetpassword',1),
	(53,NULL,NULL,NULL,'auth','label','An email has been sent to your registered email address containing an authorization code. Please click the link within this email or alternatively <a href=\"form.do\">click here</a> and enter the authorization code provided. ','','en_US','resetpasswordemailsent',1),
	(54,NULL,NULL,NULL,'auth','label','Your password has been reset. Please click to <a href=\"../login.do\"/>Login</a>','','en_US','resetpasswordok',1),
	(55,NULL,NULL,NULL,'auth','label','Reset Password: Complete','','en_US','resetpasswordok_title',1),
	(56,NULL,NULL,NULL,'auth','data','Submit','','en_US','resetpasswordRequest',1),
	(57,NULL,NULL,NULL,'auth','error','Captcha text incorrect','','en_US','resetpasswordRequest_captcha',1),
	(58,NULL,NULL,NULL,'auth','label','Captcha text','','en_US','resetpasswordRequest_captcha',1),
	(59,NULL,NULL,NULL,'auth','error','Please provide values for all fields','','en_US','resetpasswordRequest_missingparam',1),
	(60,NULL,NULL,NULL,'auth','label','Submit','','en_US','resetpasswordRequest_submit',1),
	(61,NULL,NULL,NULL,'auth','label','Username','','en_US','resetpasswordRequest_user',1),
	(62,NULL,NULL,NULL,'auth','error','A user with the supplied username does not exist','','en_US','resetpasswordRequest_username',1),
	(63,NULL,NULL,NULL,'auth','label','Auth Code','','en_US','resetpassword_authcode',1),
	(64,NULL,NULL,NULL,'auth','error','Invalid authorization code provided','','en_US','resetpassword_badauthcode',1),
	(65,NULL,NULL,NULL,'auth','label','Captcha text','','en_US','resetpassword_captcha',1),
	(66,NULL,NULL,NULL,'auth','error','Captcha text incorrect','','en_US','resetpassword_captcha',1),
	(67,NULL,NULL,NULL,'auth','label','New Password Requirements:<br />At least 8 characters in length<br />Only letters & numbers permitted (no spaces, etc)<br />At least one letter and one number<br />','','en_US','resetpassword_instructions',1),
	(68,NULL,NULL,NULL,'auth','error','Please provide values for all fields','','en_US','resetpassword_missingparam',1),
	(69,NULL,NULL,NULL,'auth','error','New password must be at least 8 characters, containing only letters and numbers, with at least one letter and number','','en_US','resetpassword_new_invalid',1),
	(70,NULL,NULL,NULL,'auth','error','The two entries for your NEW password are NOT identical','','en_US','resetpassword_new_mismatch',1),
	(71,NULL,NULL,NULL,'auth','error','No authorization code provided','','en_US','resetpassword_noauthcode',1),
	(72,NULL,NULL,NULL,'auth','label','New Password','','en_US','resetpassword_password1',1),
	(73,NULL,NULL,NULL,'auth','label','Confirm New Password','','en_US','resetpassword_password2',1),
	(74,NULL,NULL,NULL,'auth','label','To reset your password you must first supply your WASP username below. You will then be sent an email to your confirmed email address with further instructions','','en_US','resetpassword_start_instructions',1),
	(75,NULL,NULL,NULL,'auth','label','Submit','','en_US','resetpassword_submit',1),
	(76,NULL,NULL,NULL,'auth','label','Username','','en_US','resetpassword_user',1),
	(77,NULL,NULL,NULL,'auth','error','A user with the supplied username does not exist','','en_US','resetpassword_username',1),
	(78,NULL,NULL,NULL,'auth','error','Username and authorization code provided do not match','','en_US','resetpassword_wronguser',1),
	(79,NULL,NULL,NULL,'createLibrary','label','Cancel','','en_US','cancel',1),
	(80,NULL,NULL,NULL,'createLibrary','label','Library Details','','en_US','libraryDetails',1),
	(81,NULL,NULL,NULL,'createLibrary','label','Library Name','','en_US','libraryName',1),
	(82,NULL,NULL,NULL,'createLibrary','label','Library Subtype','','en_US','librarySubtype',1),
	(83,NULL,NULL,NULL,'createLibrary','label','Library Type','','en_US','libraryType',1),
	(84,NULL,NULL,NULL,'createLibrary','label','Primary Sample Name','','en_US','primarySampleName',1),
	(85,NULL,NULL,NULL,'createLibrary','label','Species','','en_US','primarySampleSpecies',1),
	(86,NULL,NULL,NULL,'createLibrary','label','Primary Sample Type','','en_US','primarySampleType',1),
	(87,NULL,NULL,NULL,'createLibrary','label','Save','','en_US','save',1),
	(88,NULL,NULL,NULL,'createLibrary','label','--SELECT NEW ADAPTOR SET--','','en_US','selectNewAdaptorSet',1),
	(89,NULL,NULL,NULL,'dapendingtask','label','APPROVE','','en_US','approve',1),
	(90,NULL,NULL,NULL,'dapendingtask','label','Department','','en_US','department',1),
	(91,NULL,NULL,NULL,'dapendingtask','label','Email','','en_US','email',1),
	(92,NULL,NULL,NULL,'dapendingtask','label','Job ID','','en_US','jobID',1),
	(93,NULL,NULL,NULL,'dapendingtask','label','Job Name','','en_US','jobName',1),
	(94,NULL,NULL,NULL,'dapendingtask','label','New PI','','en_US','newPI',1),
	(95,NULL,NULL,NULL,'dapendingtask','label','PI','','en_US','pi',1),
	(96,NULL,NULL,NULL,'dapendingtask','label','REJECT','','en_US','reject',1),
	(97,NULL,NULL,NULL,'dapendingtask','label','Samples','','en_US','samples',1),
	(98,NULL,NULL,NULL,'dapendingtask','label','Submitter','','en_US','submitter',1),
	(99,NULL,NULL,NULL,'dapendingtask','label','Pending Principal Investigators','','en_US','subtitle1',1),
	(100,NULL,NULL,NULL,'dapendingtask','label','No Pending Principal Investigators','','en_US','subtitle1_none',1),
	(101,NULL,NULL,NULL,'dapendingtask','label','Pending Jobs','','en_US','subtitle2',1),
	(102,NULL,NULL,NULL,'dapendingtask','label','No Pending Jobs','','en_US','subtitle2_none',1),
	(103,NULL,NULL,NULL,'dapendingtask','label','Department Administrator Pending Tasks','','en_US','title',1),
	(104,NULL,NULL,NULL,'dapendingtask','label','Unknown','','en_US','unknown',1),
	(105,NULL,NULL,NULL,'dapendingtask','label','Workflow','','en_US','workflow',1),
	(106,NULL,NULL,NULL,'dashboard','label','All Platform Units','','en_US','allPlatformUnits',1),
	(107,NULL,NULL,NULL,'dashboard','label','Assign Libraries To Platform Unit','','en_US','assignLibrariesToPU',1),
	(108,NULL,NULL,NULL,'dashboard','label','Control Libraries','','en_US','controlLibraries',1),
	(109,NULL,NULL,NULL,'dashboard','label','Dashboard','','en_US','dashboard',1),
	(110,NULL,NULL,NULL,'dashboard','label','Dept Admin','','en_US','deptAdmin',1),
	(111,NULL,NULL,NULL,'dashboard','label','Department Management','','en_US','deptManagement',1),
	(112,NULL,NULL,NULL,'dashboard','label','Drafted Jobs','','en_US','draftedJobs',1),
	(113,NULL,NULL,NULL,'dashboard','label','Facility Utils','','en_US','facilityUtils',1),
	(114,NULL,NULL,NULL,'dashboard','label','Job Quotes','','en_US','jobQuotes',1),
	(115,NULL,NULL,NULL,'dashboard','label','Job Quoting','','en_US','jobQuoting',1),
	(116,NULL,NULL,NULL,'dashboard','label','Job Utils','','en_US','jobUtils',1),
	(117,NULL,NULL,NULL,'dashboard','label','Lab Details','','en_US','labDetails',1),
	(118,NULL,NULL,NULL,'dashboard','label','Lab Members','','en_US','labMembers',1),
	(119,NULL,NULL,NULL,'dashboard','label','Lab Utils','','en_US','labUtils',1),
	(120,NULL,NULL,NULL,'dashboard','label','List All Job Quotes','','en_US','listAllJobQuotes',1),
	(121,NULL,NULL,NULL,'dashboard','label','List Of All Machines','','en_US','listOfAllMachines',1),
	(122,NULL,NULL,NULL,'dashboard','label','List Of All Runs','','en_US','listOfAllRuns',1),
	(123,NULL,NULL,NULL,'dashboard','label','List Of All Samples','','en_US','listOfAllSamples',1),
	(124,NULL,NULL,NULL,'dashboard','label','Misc Utils','','en_US','miscUtils',1),
	(125,NULL,NULL,NULL,'dashboard','label','My Account','','en_US','myAccount',1),
	(126,NULL,NULL,NULL,'dashboard','label','My Password','','en_US','myPassword',1),
	(127,NULL,NULL,NULL,'dashboard','label','My Profile','','en_US','myProfile',1),
	(128,NULL,NULL,NULL,'dashboard','label','My Viewable Jobs','','en_US','myViewableJobs',1),
	(129,NULL,NULL,NULL,'dashboard','label','New Platform Unit','','en_US','newPlatformUnit',1),
	(130,NULL,NULL,NULL,'dashboard','label','Platform Unit Utils','','en_US','platformUnitUtils',1),
	(131,NULL,NULL,NULL,'dashboard','label','Refresh Auth','','en_US','refreshAuth',1),
	(132,NULL,NULL,NULL,'dashboard','label','Note: requests subject to verification','','en_US','requestAccessNote',1),
	(133,NULL,NULL,NULL,'dashboard','label','Request Access To A Lab','','en_US','requestAccessToLab',1),
	(134,NULL,NULL,NULL,'dashboard','label','Sample Receiver','','en_US','sampleReceiver',1),
	(135,NULL,NULL,NULL,'dashboard','label','Sample Utils','','en_US','sampleUtils',1),
	(136,NULL,NULL,NULL,'dashboard','label','Submit A Job','','en_US','submitJob',1),
	(137,NULL,NULL,NULL,'dashboard','label','Superuser Utils','','en_US','superuserUtils',1),
	(138,NULL,NULL,NULL,'dashboard','label','System Users','','en_US','systemUsers',1),
	(139,NULL,NULL,NULL,'dashboard','label','Tasks','','en_US','tasks',1),
	(140,NULL,NULL,NULL,'dashboard','label','User Manager','','en_US','userManager',1),
	(141,NULL,NULL,NULL,'dashboard','label','User Utils','','en_US','userUtils',1),
	(142,NULL,NULL,NULL,'dashboard','label','View All Jobs','','en_US','viewAllJobs',1),
	(143,NULL,NULL,NULL,'dashboard','label','Workflow Utils','','en_US','workflowUtils',1),
	(144,NULL,NULL,NULL,'department','label','Active','','en_US','active',1),
	(145,NULL,NULL,NULL,'department','label','Inactive','','en_US','inactive',1),
	(146,NULL,NULL,NULL,'department','label','Create New Department','','en_US','create',1),
	(147,NULL,NULL,NULL,'department','label','A new department can be created by adding its name and an administrator in the form below. More administrators can be added later if desired.','','en_US','create_instructions',1),
	(148,NULL,NULL,NULL,'department','label','Department','','en_US','detail',1),
	(149,NULL,NULL,NULL,'department','label','Active','','en_US','detail_active',1),
	(150,NULL,NULL,NULL,'department','error','Selected person is already an administrator for this department','','en_US','detail_adminAlreadyExists',1),
	(151,NULL,NULL,NULL,'department','label','Administrators','','en_US','detail_administrators',1),
	(152,NULL,NULL,NULL,'department','label','Administrator Name','','en_US','detail_administrator_name',1),
	(153,NULL,NULL,NULL,'department','label','Create Administrator','','en_US','detail_createadmin',1),
	(154,NULL,NULL,NULL,'department','label','Administrator Email','','en_US','detail_email',1),
	(155,NULL,NULL,NULL,'department','error','Email not found','','en_US','detail_emailnotfound',1),
	(156,NULL,NULL,NULL,'department','label','Current Administrators','','en_US','detail_existingadmin',1),
	(157,NULL,NULL,NULL,'department','error','Formatting Error','','en_US','detail_formatting',1),
	(158,NULL,NULL,NULL,'department','label','Inactive','','en_US','detail_inactive',1),
	(159,NULL,NULL,NULL,'department','error','Specified department does Not exist','','en_US','detail_invalidDept',1),
	(160,NULL,NULL,NULL,'department','label','Labs','','en_US','detail_labs',1),
	(161,NULL,NULL,NULL,'department','error','No new administrator username provided','','en_US','detail_missinglogin',1),
	(162,NULL,NULL,NULL,'department','error','Administrator name is missing','','en_US','detail_missingparam',1),
	(163,NULL,NULL,NULL,'department','label','New Administrator Created','','en_US','detail_ok',1),
	(164,NULL,NULL,NULL,'department','label','Pending Jobs','','en_US','detail_pendingjobs',1),
	(165,NULL,NULL,NULL,'department','label','Pending Labs','','en_US','detail_pendinglabs',1),
	(166,NULL,NULL,NULL,'department','label','Remove','','en_US','detail_remove',1),
	(167,NULL,NULL,NULL,'department','label','Submit','','en_US','detail_submit',1),
	(168,NULL,NULL,NULL,'department','label','Update Department','','en_US','detail_update',1),
	(169,NULL,NULL,NULL,'department','label','Type in the name of an existing WASP user to make them an administrator of this department','','en_US','detail_update_admin',1),
	(170,NULL,NULL,NULL,'department','label','Use the following form to change the status of a department between Active and Inactive','','en_US','detail_update_instructions',1),
	(171,NULL,NULL,NULL,'department','error','Deaprtment name must be provided','','en_US','detail_update_missingparam',1),
	(172,NULL,NULL,NULL,'department','label','Department has been updated','','en_US','detail_update_ok',1),
	(173,NULL,NULL,NULL,'department','error','User not found in database','','en_US','detail_usernotfound',1),
	(174,NULL,NULL,NULL,'department','label','List of Labs','','en_US','lab_list',1),
	(175,NULL,NULL,NULL,'department','label','List &amp; Manage Departments','','en_US','list',1),
	(176,NULL,NULL,NULL,'department','data','Submit','','en_US','list',1),
	(177,NULL,NULL,NULL,'department','label','Create Department','','en_US','list_create',1),
	(178,NULL,NULL,NULL,'department','label','Department Name','','en_US','list_department',1),
	(179,NULL,NULL,NULL,'department','error','Department already exists','','en_US','list_department_exists',1),
	(180,NULL,NULL,NULL,'department','label','The current departments registered in the system is listed below. Please click on a department name to view / edit its details.','','en_US','list_instructions',1),
	(181,NULL,NULL,NULL,'department','error','Invalid department name','','en_US','list_invalid',1),
	(182,NULL,NULL,NULL,'department','error','Please provide a department name','','en_US','list_missingparam',1),
	(183,NULL,NULL,NULL,'department','label','New department has been created','','en_US','list_ok',1),
	(184,NULL,NULL,NULL,'extraJobDetails','label','Machine','','en_US','machine',1),
	(185,NULL,NULL,NULL,'extraJobDetails','label','Read Length','','en_US','readLength',1),
	(186,NULL,NULL,NULL,'extraJobDetails','label','Read Type','','en_US','readType',1),
	(187,NULL,NULL,NULL,'extraJobDetails','label','Quote','','en_US','quote',1),
	(188,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'F6BE018AB578451F9E0D619D7DD89B50','file','error','File Not Found','','en_US','not_found',1),
	(189,NULL,NULL,NULL,'fmpayment','label','Amount','','en_US','amount',1),
	(190,NULL,NULL,NULL,'fmpayment','constraint','NotEmpty','','en_US','amount',1),
	(191,NULL,NULL,NULL,'fmpayment','metaposition','10','','en_US','amount',1),
	(192,NULL,NULL,NULL,'fmpayment','error','AmounT cannot be Empty','','en_US','amount',1),
	(193,NULL,NULL,NULL,'fmpayment','label','Comment','','en_US','comment',1),
	(194,NULL,NULL,NULL,'fmpayment','constraint','NotEmpty','','en_US','comment',1),
	(195,NULL,NULL,NULL,'fmpayment','metaposition','20','','en_US','comment',1),
	(196,NULL,NULL,NULL,'fmpayment','error','Comment cannot be Empty','','en_US','comment',1),
	(197,NULL,NULL,NULL,'fmpayment','label','Receive Payment for Jobs','','en_US','name',1),
	(198,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'017773CD1BBA440AB6E216207836629F','grid','label','Required date format: YYYY/MM/DD. It is best to use calendar to select date.','','en_US','dateFormat',1),
	(199,NULL,NULL,NULL,'grid','label','New','','en_US','icon_new',1),
	(200,NULL,NULL,NULL,'grid','label','Reload Grid','','en_US','icon_reload',1),
	(201,NULL,NULL,NULL,'grid','label','Search','','en_US','icon_search',1),
	(202,NULL,NULL,NULL,'grid','label','Required jobId format: all digits','','en_US','jobIdFormat',1),
	(203,NULL,NULL,NULL,'grid','label','Cells must be an integer','','en_US','lanesInteger',1),
	(204,NULL,NULL,NULL,'grid','label','Required PI format: firstname lastname (login). It is best to select name from list.','','en_US','piFormat',1),
	(205,NULL,NULL,NULL,'grid','label','ReadLength must be an integer','','en_US','readLengthInteger',1),
	(206,NULL,NULL,NULL,'grid','label','Required Submitter format: firstname lastname (login). It is best to select name from list.','','en_US','submitterFormat',1),
	(207,NULL,NULL,NULL,'job','label','Quote Amount','','en_US','amount',1),
	(208,NULL,NULL,NULL,'job','approved','Job has been approved','','en_US','approval',1),
	(209,NULL,NULL,NULL,'job','rejected','Job has been rejected','','en_US','approval',1),
	(210,NULL,NULL,NULL,'job','error','Error - Update Failed','','en_US','approval',1),
	(211,NULL,NULL,NULL,'jobComment','label','Add New Job Comment','','en_US','addNewJobComment',1),
	(212,NULL,NULL,NULL,'jobComment','label','Please provide a comment','','en_US','alert',1),
	(213,NULL,NULL,NULL,'jobComment','label','Facility-Generated Comments','','en_US','facilityComment',1),
	(214,NULL,NULL,NULL,'jobComment','error','Unable to find job in database','','en_US','job',1),
	(215,NULL,NULL,NULL,'jobComment','error','Comment may not be empty','','en_US','jobCommentEmpty',1),
	(216,NULL,NULL,NULL,'jobComment','label','Comment added to job comments list','','en_US','jobCommentAdded',1),
	(217,NULL,NULL,NULL,'jobComment','error','Unable to properly authorize user','','en_US','jobCommentAuth',1),
	(218,NULL,NULL,NULL,'jobComment','error','Unexpectedly unable to create comment','','en_US','jobCommentCreat',1),
	(219,NULL,NULL,NULL,'jobComment','label','Date','','en_US','jobCommentDate',1),
	(220,NULL,NULL,NULL,'jobComment','label','Submit New Comment','','en_US','submitNewComment',1),
	(221,NULL,NULL,NULL,'jobComment','label','Job-Submitter Comment','','en_US','jobSubmitterComment',1),
	(222,NULL,NULL,NULL,'jobMeta','label','Base','','en_US','base',1),
	(223,NULL,NULL,NULL,'jobMeta','label','Resource','','en_US','resource',1),
	(224,NULL,NULL,NULL,'jobMeta','label','Software','','en_US','software',1),
	(225,NULL,NULL,NULL,'job','label','Submitted On','','en_US','createts',1),
	(226,NULL,NULL,NULL,'job','label','Add Job Viewer','','en_US','detail_addJobViewer',1),
	(227,NULL,NULL,NULL,'job','label','Files','','en_US','detail_files',1),
	(228,NULL,NULL,NULL,'job','label','Job Viewer','','en_US','detail_jobViewer',1),
	(229,NULL,NULL,NULL,'job','label','Lab','','en_US','detail_lab',1),
	(230,NULL,NULL,NULL,'job','label','Login Name','','en_US','detail_loginName',1),
	(231,NULL,NULL,NULL,'job','label','Remove','','en_US','detail_remove',1),
	(232,NULL,NULL,NULL,'job','label','Samples','','en_US','detail_samples',1),
	(233,NULL,NULL,NULL,'job','label','Submitting User','','en_US','detail_submittingUser',1),
	(234,NULL,NULL,NULL,'job','label','JobID','','en_US','jobId',1),
	(235,NULL,NULL,NULL,'job','error1','JobId or LabId Error','','en_US','jobViewerUserRoleAdd',1),
	(236,NULL,NULL,NULL,'job','error2','Login Not Found','','en_US','jobViewerUserRoleAdd',1),
	(237,NULL,NULL,NULL,'job','error3','User is submitter, thus already has access to this job','','en_US','jobViewerUserRoleAdd',1),
	(238,NULL,NULL,NULL,'job','error4','User already has access to this job','','en_US','jobViewerUserRoleAdd',1),
	(239,NULL,NULL,NULL,'job','label','List of Jobs','','en_US','job_list',1),
	(240,NULL,NULL,NULL,'job','label','Lab','','en_US','lab',1),
	(241,NULL,NULL,NULL,'job','label','Job Name','','en_US','name',1),
	(242,NULL,NULL,NULL,'job','constraint','NotEmpty','','en_US','name',1),
	(243,NULL,NULL,NULL,'job','error','Name cannot be empty','','en_US','name',1),
	(244,NULL,NULL,NULL,'job','label','PI','','en_US','pi',1),
	(245,NULL,NULL,NULL,'job','label','Submitter','','en_US','submitter',1),
	(246,NULL,NULL,NULL,'job','label','Submitter','','en_US','UserId',1),
	(247,NULL,NULL,NULL,'job','label','Result','','en_US','viewfiles',1),
	(248,NULL,NULL,NULL,'job2quote','label','List of Quotes','','en_US','job2quote_list',1),
	(249,NULL,NULL,NULL,'job2quote','label','Sort By Quote Amount To Easily Identify Jobs Requiring A Quote','','en_US','job2quote_note',1),
	(250,NULL,NULL,NULL,'jobdetail_for_import','label','Submitted Files','','en_US','files',1),
	(251,NULL,NULL,NULL,'jobdetail_for_import','label','Job Comments','','en_US','jobComments',1),
	(252,NULL,NULL,NULL,'jobdetail_for_import','label','View','','en_US','jobCommentsView',1),
	(253,NULL,NULL,NULL,'jobdetail_for_import','label','-Add-Edit','','en_US','jobCommentsPlusAddEdit',1),
	(254,NULL,NULL,NULL,'jobdetail_for_import','label','Job ID','','en_US','jobId',1),
	(255,NULL,NULL,NULL,'jobdetail_for_import','label','Job Name','','en_US','jobName',1),
	(256,NULL,NULL,NULL,'jobdetail_for_import','label','PI','','en_US','jobPI',1),
	(257,NULL,NULL,NULL,'jobdetail_for_import','label','Submission Date','','en_US','jobSubmissionDate',1),
	(258,NULL,NULL,NULL,'jobdetail_for_import','label','Submitter','','en_US','jobSubmitter',1),
	(259,NULL,NULL,NULL,'jobdetail_for_import','label','Workflow','','en_US','jobWorkflow',1),
	(260,NULL,NULL,NULL,'jobdetail_for_import','label','Machine','','en_US','Machine',1),
	(261,NULL,NULL,NULL,'jobdetail_for_import','label','Read Length','','en_US','Read_Length',1),
	(262,NULL,NULL,NULL,'jobdetail_for_import','label','Read Type','','en_US','Read_Type',1),
	(263,NULL,NULL,NULL,'jobdetail_for_import','label','PI Approval','','en_US','PI_Approval',1),
	(264,NULL,NULL,NULL,'jobdetail_for_import','label','Departmental Approval','','en_US','DA_Approval',1),
	(265,NULL,NULL,NULL,'jobdetail_for_import','label','Anticipated Cost','','en_US','Quote_Job_Price',1),
	(266,NULL,NULL,NULL,'jobDraft','label','Cancel','','en_US','cancel',1),
	(267,NULL,NULL,NULL,'jobDraft','label','Cell','','en_US','cell',1),
	(268,NULL,NULL,NULL,'jobDraft','label','Each sample must be placed on at least one cell.','','en_US','cell_error',1),
	(269,NULL,NULL,NULL,'jobDraft','label','Unexpected error with library barcode.','','en_US','cell_adaptor_error',1),
	(270,NULL,NULL,NULL,'jobDraft','label','Libraries containing identical barcodes may NOT reside on the same cell.','','en_US','cell_barcode_error',1),
	(271,NULL,NULL,NULL,'jobDraft','label','Libraries with the NONE barcode sequence (Index 0) CANNOT be multiplexed with other samples.','','en_US','cell_barcode_NONE_error',1),
	(272,NULL,NULL,NULL,'jobDraft','label','Please choose the number of sequencing cells you wish to order and then indicate which samples you wish to combine (multiplex) in the same cell','','en_US','cell_instructions',1),
	(273,NULL,NULL,NULL,'jobDraft','label','Unexpected error determining number of cells selected.','','en_US','cell_unexpected_error',1),
	(274,NULL,NULL,NULL,'jobDraft','error','You must select a job resource','','en_US','changeResource',1),
	(275,NULL,NULL,NULL,'jobDraft','error','You must select a software resource','','en_US','changeSoftwareResource',1),
	(276,NULL,NULL,NULL,'jobDraft','error','Unable to clone sample draft','','en_US','clone',1),
	(277,NULL,NULL,NULL,'jobDraft','error','Unexpectedly unable to record your comment','','en_US','commentCreate',1),
	(278,NULL,NULL,NULL,'jobDraft','error','Unexpectedly unable to fetch your comment','','en_US','commentFetch',1),
	(279,NULL,NULL,NULL,'jobDraft','label','If you wish, you may provide comments below that may be useful for this job submission. ','','en_US','comment_instructions',1),
	(280,NULL,NULL,NULL,'jobDraft','label','Comments (optional) ','','en_US','comment_optional',1),
	(281,NULL,NULL,NULL,'jobDraft','label','Create A Job','','en_US','create',1),
	(282,NULL,NULL,NULL,'jobDraft','error','Unable to create a Job from current Job Draft','','en_US','createJobFromJobDraft',1),
	(283,NULL,NULL,NULL,'jobDraft','label','To create a job, please provide a name, select the lab from which you are submitting and choose the assay workflow most suited to your experiment.','','en_US','create_instructions',1),
	(284,NULL,NULL,NULL,'jobDraft','label','Edit','','en_US','edit',1),
	(285,NULL,NULL,NULL,'jobDraft','error','Please address errors on this page','','en_US','form',1),
	(286,NULL,NULL,NULL,'jobDraft','label','Job','','en_US','info_job',1),
	(287,NULL,NULL,NULL,'jobDraft','label','Lab','','en_US','info_lab',1),
	(288,NULL,NULL,NULL,'jobDraft','label','Workflow','','en_US','info_workflow',1),
	(289,NULL,NULL,NULL,'jobDraft','label','List of Job Drafts (Click Job Name to Continue on Submission)','','en_US','jobDraft_list',1),
	(290,NULL,NULL,NULL,'jobDraft','error','This jobdraft identifier does not return a valid job draft','','en_US','jobDraft_null',1),
	(291,NULL,NULL,NULL,'jobDraft','label','Lab','','en_US','lab',1),
	(292,NULL,NULL,NULL,'jobDraft','label','Lab','','en_US','labId',1),
	(293,NULL,NULL,NULL,'jobDraft','error','Lab Must Not Be Empty','','en_US','labId',1),
	(294,NULL,NULL,NULL,'jobDraft','label','Last Modified On','','en_US','last_modify_date',1),
	(295,NULL,NULL,NULL,'jobDraft','label','Last Modified By','','en_US','last_modify_user',1),
	(296,NULL,NULL,NULL,'jobDraft','label','Lib. Index','','en_US','libIndex',1),
	(297,NULL,NULL,NULL,'jobDraft','label','Please provide the metadata requested below.','','en_US','meta_instructions',1),
	(298,NULL,NULL,NULL,'jobDraft','error','You must provide a name for this job','','en_US','name',1),
	(299,NULL,NULL,NULL,'jobDraft','label','Job Name','','en_US','name',1),
	(300,NULL,NULL,NULL,'jobDraft','error','Job name chosen already exists','','en_US','name_exists',1),
	(301,NULL,NULL,NULL,'jobDraft','label','Next','','en_US','next',1),
	(302,NULL,NULL,NULL,'jobDraft','error','You must create entries for at least one sample','','en_US','noSamples',1),
	(303,NULL,NULL,NULL,'jobDraft','error','This jobdraft has already been submitted','','en_US','not_pending',1),
	(304,NULL,NULL,NULL,'jobDraft','label','No draft samples to display','','en_US','no_draft_samples',1),
	(305,NULL,NULL,NULL,'jobDraft','label','None Uploaded','','en_US','no_file',1),
	(306,NULL,NULL,NULL,'jobDraft','error','You are not registered as a lab user but must be one to submit a job','','en_US','no_lab',1),
	(307,NULL,NULL,NULL,'jobDraft','error','No resources of the requested type have been assigned to the current workflow','','en_US','no_resources',1),
	(308,NULL,NULL,NULL,'jobDraft','error','There are currently no active workflows to create a job for.','','en_US','no_workflows',1),
	(309,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'929DC62586354040B5D03F16DC9FFF14','jobDraft','label','Select number of sequencing cells (lanes) required','','en_US','numberofcells',1),
	(310,NULL,NULL,NULL,'jobDraft','label','','','en_US','page_footer',1),
	(311,NULL,NULL,NULL,'jobDraft','label','Are you sure you wish to remove this sample?','','en_US','remove_confirm',1),
	(312,NULL,NULL,NULL,'jobDraft','error','Resource options for this workflow have not been properly configured','','en_US','resourceCategories_not_configured',1),
	(313,NULL,NULL,NULL,'jobDraft','label','Please select from the platforms available for this assay workflow. If there are options available for the platform and assay workflow, you will be prompted to choose those applicable to your experimental design.','','en_US','resource_instructions',1),
	(314,NULL,NULL,NULL,'jobDraft','label','Sample','','en_US','sample',1),
	(315,NULL,NULL,NULL,'jobDraft','label','No sample draft matches supplied id','','en_US','sampleDraft_null',1),
	(316,NULL,NULL,NULL,'jobDraft','label','No sample subtype matches supplied id','','en_US','sampleSubtype_null',1),
	(317,NULL,NULL,NULL,'jobDraft','label','Action','','en_US','sample_action',1),
	(318,NULL,NULL,NULL,'jobDraft','label','Existing','','en_US','sample_add_existing',1),
	(319,NULL,NULL,NULL,'jobDraft','label','Add a new Sample / Library','','en_US','sample_add_heading',1),
	(320,NULL,NULL,NULL,'jobDraft','label','Class','','en_US','sample_class',1),
	(321,NULL,NULL,NULL,'jobDraft','label','Clone','','en_US','sample_clone',1),
	(322,NULL,NULL,NULL,'jobDraft','label','Clone Sample / Library','','en_US','sample_clone_heading',1),
	(323,NULL,NULL,NULL,'jobDraft','label','Edit','','en_US','sample_edit',1),
	(324,NULL,NULL,NULL,'jobDraft','label','Edit Sample / Library','','en_US','sample_edit_heading',1),
	(325,NULL,NULL,NULL,'jobDraft','label','File','','en_US','sample_file',1),
	(326,NULL,NULL,NULL,'jobDraft','label','Please add details for each of your samples. Choose from the sample sub-types available by clicking the appropriate link on the bottom bar of the table below. You can enter information about new samples or select from previously submitted samples.','','en_US','sample_instructions',1),
	(327,NULL,NULL,NULL,'jobDraft','label','Sample Name','','en_US','sample_name',1),
	(328,NULL,NULL,NULL,'jobDraft','label','Sample #','','en_US','sample_number',1),
	(329,NULL,NULL,NULL,'jobDraft','label','Remove','','en_US','sample_remove',1),
	(330,NULL,NULL,NULL,'jobDraft','label','Sample Draft View','','en_US','sample_ro_heading',1),
	(331,NULL,NULL,NULL,'jobDraft','label','Sample Subtype','','en_US','sample_subtype',1),
	(332,NULL,NULL,NULL,'jobDraft','error','This sample subtype identifier does not return a valid sample subtype','','en_US','sample_subtype_null',1),
	(333,NULL,NULL,NULL,'jobDraft','label','Sample Type','','en_US','sample_type',1),
	(334,NULL,NULL,NULL,'jobDraft','label','View','','en_US','sample_view',1),
	(335,NULL,NULL,NULL,'jobDraft','label','Please select from the software options available for this assay workflow. If there are settable parameters, you will be prompted to choose those applicable to your experimental design or you may leave the pre-populated default values.','','en_US','software_instructions',1),
	(336,NULL,NULL,NULL,'jobDraft','error','Software options for this workflow have not been properly configured','','en_US','software_not_configured',1),
	(337,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'A173C404860549D888297FD956F8B39A','jobDraft','label','Thank you for your submission. You may track the progress of submitted jobs and review data after job completion by navigating to via the Jobs menu item on the home page','','en_US','submission_complete',1),
	(338,NULL,NULL,NULL,'jobDraft','label','Submission of this job unexpectedly failed. Please try again or contact a WASP administrator.','','en_US','submission_failed',1),
	(339,NULL,NULL,NULL,'jobDraft','label','Save Changes','','en_US','submit',1),
	(340,NULL,NULL,NULL,'jobDraft','label','Submit Job','','en_US','submit_button',1),
	(341,NULL,NULL,NULL,'jobDraft','label','Submit Later','','en_US','submit_later_button',1),
	(342,NULL,NULL,NULL,'jobDraft','label','Retry','','en_US','submit_retry_button',1),
	(343,NULL,NULL,NULL,'jobDraft','label','Submitted By','','en_US','submitter',1),
	(344,NULL,NULL,NULL,'jobDraft','label','Add samples of subtype','','en_US','subtype_select',1),
	(345,NULL,NULL,NULL,'jobDraft','error','File Upload failed','','en_US','upload_file',1),
	(346,NULL,NULL,NULL,'jobDraft','label','If you wish you may upload one or more files, e.g. quality control data, to associate with this sample submission.','','en_US','upload_file_description',1),
	(347,NULL,NULL,NULL,'jobDraft','label','Upload Files','','en_US','upload_file_heading',1),
	(348,NULL,NULL,NULL,'jobDraft','error','You are not authorized to edit this jobdraft','','en_US','user_incorrect',1),
	(349,NULL,NULL,NULL,'jobDraft','label','Please review your submission by selecting the links immediately above. Once satisfied with your entries, you may submit this request by clicking the Submit Job button.','','en_US','verify_instructions',1),
	(350,NULL,NULL,NULL,'jobDraft','label','Assay Workflow','','en_US','workflowId',1),
	(351,NULL,NULL,NULL,'jobDraft','error','You must select an Assay Workflow','','en_US','workflowId',1),
	(352,NULL,NULL,NULL,'jobListAssignLibrary','label','Active Jobs With Libraries To Be Run','','en_US','tableHeader',1),
	(353,NULL,NULL,NULL,'jobListAssignLibrary','label','Job','','en_US','jobId',1),
	(354,NULL,NULL,NULL,'jobListAssignLibrary','label','Job Name','','en_US','jobName',1),
	(355,NULL,NULL,NULL,'jobListAssignLibrary','label','None','','en_US','none',1),
	(356,NULL,NULL,NULL,'jobListAssignLibrary','label','Submitter','','en_US','submitter',1),
	(357,NULL,NULL,NULL,'jobListAssignLibrary','label','view','','en_US','view',1),
	(358,NULL,NULL,NULL,'jobListAssignLibrary','label','PI','','en_US','pi',1),
	(359,NULL,NULL,NULL,'jobListCreateLibrary','label','No jobs with libraries to be created','','en_US','noneNeeded',1),
	(360,NULL,NULL,NULL,'jobListCreateLibrary','label','Job','','en_US','jobId',1),
	(361,NULL,NULL,NULL,'jobListCreateLibrary','label','Job Name','','en_US','jobName',1),
	(362,NULL,NULL,NULL,'jobListCreateLibrary','label','Submitter','','en_US','submitter',1),
	(363,NULL,NULL,NULL,'jobListCreateLibrary','label','PI','','en_US','pi',1),
	(364,NULL,NULL,NULL,'lab','label','Add New LabUser','','en_US','adduser',1),
	(365,NULL,NULL,NULL,'lab','label','Billing Address','','en_US','billing_address',1),
	(366,NULL,NULL,NULL,'lab','error','Billing Address cannot be empty','','en_US','billing_address',1),
	(367,NULL,NULL,NULL,'lab','constraint','NotEmpty','','en_US','billing_address',1),
	(368,NULL,NULL,NULL,'lab','metaposition','80','','en_US','billing_address',1),
	(369,NULL,NULL,NULL,'lab','label','Room','','en_US','billing_building_room',1),
	(370,NULL,NULL,NULL,'lab','metaposition','70','','en_US','billing_building_room',1),
	(371,NULL,NULL,NULL,'lab','label','Billing City','','en_US','billing_city',1),
	(372,NULL,NULL,NULL,'lab','error','Billing City cannot be empty','','en_US','billing_city',1),
	(373,NULL,NULL,NULL,'lab','constraint','NotEmpty','','en_US','billing_city',1),
	(374,NULL,NULL,NULL,'lab','metaposition','90','','en_US','billing_city',1),
	(375,NULL,NULL,NULL,'lab','label','Billing Contact','','en_US','billing_contact',1),
	(376,NULL,NULL,NULL,'lab','error','Billing Contact cannot be empty','','en_US','billing_contact',1),
	(377,NULL,NULL,NULL,'lab','constraint','NotEmpty','','en_US','billing_contact',1),
	(378,NULL,NULL,NULL,'lab','metaposition','40','','en_US','billing_contact',1),
	(379,NULL,NULL,NULL,'lab','label','Billing Country','','en_US','billing_country',1),
	(380,NULL,NULL,NULL,'lab','error','Billing Country cannot be empty','','en_US','billing_country',1),
	(381,NULL,NULL,NULL,'lab','control','select:${countries}:code:name','','en_US','billing_country',1),
	(382,NULL,NULL,NULL,'lab','constraint','NotEmpty','','en_US','billing_country',1),
	(383,NULL,NULL,NULL,'lab','metaposition','110','','en_US','billing_country',1),
	(384,NULL,NULL,NULL,'lab','error','Billing Department cannot be empty','','en_US','billing_departmentId',1),
	(385,NULL,NULL,NULL,'lab','label','Billing Department','','en_US','billing_departmentId',1),
	(386,NULL,NULL,NULL,'lab','constraint','NotEmpty','','en_US','billing_departmentId',1),
	(387,NULL,NULL,NULL,'lab','metaposition','60','','en_US','billing_departmentId',1),
	(388,NULL,NULL,NULL,'lab','control','select:${departments}:departmentId:name','','en_US','billing_departmentId',1),
	(389,NULL,NULL,NULL,'lab','label','Billing Institution','','en_US','billing_institution',1),
	(390,NULL,NULL,NULL,'lab','error','Institution cannot be empty','','en_US','billing_institution',1),
	(391,NULL,NULL,NULL,'lab','constraint','NotEmpty','','en_US','billing_institution',1),
	(392,NULL,NULL,NULL,'lab','metaposition','50','','en_US','billing_institution',1),
	(393,NULL,NULL,NULL,'lab','label','Billing Phone','','en_US','billing_phone',1),
	(394,NULL,NULL,NULL,'lab','error','Billing Phone cannot be empty','','en_US','billing_phone',1),
	(395,NULL,NULL,NULL,'lab','constraint','NotEmpty','','en_US','billing_phone',1),
	(396,NULL,NULL,NULL,'lab','metaposition','130','','en_US','billing_phone',1),
	(397,NULL,NULL,NULL,'lab','label','Billing State','','en_US','billing_state',1),
	(398,NULL,NULL,NULL,'lab','control','select:${states}:code:name','','en_US','billing_state',1),
	(399,NULL,NULL,NULL,'lab','metaposition','100','','en_US','billing_state',1),
	(400,NULL,NULL,NULL,'lab','label','Billing Zip','','en_US','billing_zip',1),
	(401,NULL,NULL,NULL,'lab','error','Billing Zip cannot be empty','','en_US','billing_zip',1),
	(402,NULL,NULL,NULL,'lab','constraint','NotEmpty','','en_US','billing_zip',1),
	(403,NULL,NULL,NULL,'lab','metaposition','120','','en_US','billing_zip',1),
	(404,NULL,NULL,NULL,'lab','label','Room','','en_US','building_room',1),
	(405,NULL,NULL,NULL,'lab','metaposition','30','','en_US','building_room',1),
	(406,NULL,NULL,NULL,'lab','error','Lab was NOT created. Please fill in required fields.','','en_US','created',1),
	(407,NULL,NULL,NULL,'lab','label','Lab was created','','en_US','created_success',1),
	(408,NULL,NULL,NULL,'lab','label','Department','','en_US','departmentId',1),
	(409,NULL,NULL,NULL,'lab','error','Please select department','','en_US','departmentId',1),
	(410,NULL,NULL,NULL,'lab','label','Lab Details','','en_US','detail',1),
	(411,NULL,NULL,NULL,'lab','label','External/Internal','','en_US','internal_external_lab',1),
	(412,NULL,NULL,NULL,'lab','error','Please specify lab type (External/Internal)','','en_US','internal_external_lab',1),
	(413,NULL,NULL,NULL,'lab','control','select:external:External;internal:Internal','','en_US','internal_external_lab',1),
	(414,NULL,NULL,NULL,'lab','constraint','NotEmpty','','en_US','internal_external_lab',1),
	(415,NULL,NULL,NULL,'lab','metaposition','10','','en_US','internal_external_lab',1),
	(416,NULL,NULL,NULL,'lab','label','Active','','en_US','isActive',1),
	(417,NULL,NULL,NULL,'lab','label','Jobs','','en_US','jobs',1),
	(418,NULL,NULL,NULL,'lab','label','List of Labs','','en_US','lab_list',1),
	(419,NULL,NULL,NULL,'lab','label','Principal Investigator','','en_US','list_pi',1),
	(420,NULL,NULL,NULL,'lab','label','Lab Members','','en_US','list_users',1),
	(421,NULL,NULL,NULL,'lab','label','Lab Name','','en_US','name',1),
	(422,NULL,NULL,NULL,'lab','error','Lab Name cannot be empty','','en_US','name',1),
	(423,NULL,NULL,NULL,'lab','label','New Password','','en_US','newPassword',1),
	(424,NULL,NULL,NULL,'lab','label','Confirm New Password','','en_US','newPasswordConfirm',1),
	(425,NULL,NULL,NULL,'lab','label','Old Password','','en_US','oldPassword',1),
	(426,NULL,NULL,NULL,'lab','label','Phone','','en_US','phone',1),
	(427,NULL,NULL,NULL,'lab','error','Phone cannot be empty','','en_US','phone',1),
	(428,NULL,NULL,NULL,'lab','constraint','NotEmpty','','en_US','phone',1),
	(429,NULL,NULL,NULL,'lab','metaposition','20','','en_US','phone',1),
	(430,NULL,NULL,NULL,'lab','label','Principal Investigator','','en_US','primaryUser',1),
	(431,NULL,NULL,NULL,'lab','label','Principal Investigator','','en_US','primaryUserId',1),
	(432,NULL,NULL,NULL,'lab','error','Please select Principal Investigator','','en_US','primaryUserId',1),
	(433,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'AD9237BAD979400987EFAF324517F867','lab','error','Lab was NOT successfully updated. Please fill in required fields.','','en_US','updated',1),
	(434,NULL,NULL,NULL,'lab','label','Lab was updated','','en_US','updated_success',1),
	(435,NULL,NULL,NULL,'labDetail','label','Cancel','','en_US','cancel',1),
	(436,NULL,NULL,NULL,'labDetail','label','create job','','en_US','create_job',1),
	(437,NULL,NULL,NULL,'labDetail','label','Edit','','en_US','edit',1),
	(438,NULL,NULL,NULL,'labDetail','label','Save Changes','','en_US','save',1),
	(439,NULL,NULL,NULL,'labPending','error','Invalid action. Must be approve or reject only','','en_US','action',1),
	(440,NULL,NULL,NULL,'labPending','label','Approve','','en_US','approve',1),
	(441,NULL,NULL,NULL,'labPending','label','New lab application successfully approved','','en_US','approved',1),
	(442,NULL,NULL,NULL,'labPending','label','Billing Address','','en_US','billing_address',1),
	(443,NULL,NULL,NULL,'labPending','error','Billing Address cannot be empty','','en_US','billing_address',1),
	(444,NULL,NULL,NULL,'labPending','constraint','NotEmpty','','en_US','billing_address',1),
	(445,NULL,NULL,NULL,'labPending','metaposition','80','','en_US','billing_address',1),
	(446,NULL,NULL,NULL,'labPending','label','Room','','en_US','billing_building_room',1),
	(447,NULL,NULL,NULL,'labPending','metaposition','70','','en_US','billing_building_room',1),
	(448,NULL,NULL,NULL,'labPending','label','Billing City','','en_US','billing_city',1),
	(449,NULL,NULL,NULL,'labPending','error','Billing City cannot be empty','','en_US','billing_city',1),
	(450,NULL,NULL,NULL,'labPending','constraint','NotEmpty','','en_US','billing_city',1),
	(451,NULL,NULL,NULL,'labPending','metaposition','90','','en_US','billing_city',1),
	(452,NULL,NULL,NULL,'labPending','label','Billing Contact','','en_US','billing_contact',1),
	(453,NULL,NULL,NULL,'labPending','error','Billing Contact cannot be empty','','en_US','billing_contact',1),
	(454,NULL,NULL,NULL,'labPending','constraint','NotEmpty','','en_US','billing_contact',1),
	(455,NULL,NULL,NULL,'labPending','metaposition','40','','en_US','billing_contact',1),
	(456,NULL,NULL,NULL,'labPending','label','Billing Country','','en_US','billing_country',1),
	(457,NULL,NULL,NULL,'labPending','error','Billing Country cannot be empty','','en_US','billing_country',1),
	(458,NULL,NULL,NULL,'labPending','control','select:${countries}:code:name','','en_US','billing_country',1),
	(459,NULL,NULL,NULL,'labPending','constraint','NotEmpty','','en_US','billing_country',1),
	(460,NULL,NULL,NULL,'labPending','metaposition','110','','en_US','billing_country',1),
	(461,NULL,NULL,NULL,'labPending','error','Billing Department cannot be empty','','en_US','billing_departmentId',1),
	(462,NULL,NULL,NULL,'labPending','label','Billing Department','','en_US','billing_departmentId',1),
	(463,NULL,NULL,NULL,'labPending','constraint','NotEmpty','','en_US','billing_departmentId',1),
	(464,NULL,NULL,NULL,'labPending','metaposition','60','','en_US','billing_departmentId',1),
	(465,NULL,NULL,NULL,'labPending','control','select:${departments}:departmentId:name','','en_US','billing_departmentId',1),
	(466,NULL,NULL,NULL,'labPending','label','Billing Institution','','en_US','billing_institution',1),
	(467,NULL,NULL,NULL,'labPending','error','Institution cannot be empty','','en_US','billing_institution',1),
	(468,NULL,NULL,NULL,'labPending','constraint','NotEmpty','','en_US','billing_institution',1),
	(469,NULL,NULL,NULL,'labPending','metaposition','50','','en_US','billing_institution',1),
	(470,NULL,NULL,NULL,'labPending','label','Billing Phone','','en_US','billing_phone',1),
	(471,NULL,NULL,NULL,'labPending','error','Billing Phone cannot be empty','','en_US','billing_phone',1),
	(472,NULL,NULL,NULL,'labPending','constraint','NotEmpty','','en_US','billing_phone',1),
	(473,NULL,NULL,NULL,'labPending','metaposition','130','','en_US','billing_phone',1),
	(474,NULL,NULL,NULL,'labPending','label','Billing State','','en_US','billing_state',1),
	(475,NULL,NULL,NULL,'labPending','control','select:${states}:code:name','','en_US','billing_state',1),
	(476,NULL,NULL,NULL,'labPending','metaposition','100','','en_US','billing_state',1),
	(477,NULL,NULL,NULL,'labPending','label','Billing Zip','','en_US','billing_zip',1),
	(478,NULL,NULL,NULL,'labPending','error','Billing Zip cannot be empty','','en_US','billing_zip',1),
	(479,NULL,NULL,NULL,'labPending','constraint','NotEmpty','','en_US','billing_zip',1),
	(480,NULL,NULL,NULL,'labPending','metaposition','120','','en_US','billing_zip',1),
	(481,NULL,NULL,NULL,'labPending','label','Room','','en_US','building_room',1),
	(482,NULL,NULL,NULL,'labPending','metaposition','30','','en_US','building_room',1),
	(483,NULL,NULL,NULL,'labPending','label','Cancel','','en_US','cancel',1),
	(484,NULL,NULL,NULL,'labPending','error','Failed to process new lab request','','en_US','could_not_create_lab',1),
	(485,NULL,NULL,NULL,'labPending','error','Pending Lab was NOT created. Please fill in required fields.','','en_US','created',1),
	(486,NULL,NULL,NULL,'labPending','label','Create New Lab Request','','en_US','createNewLab',1),
	(487,NULL,NULL,NULL,'labPending','label','Department','','en_US','departmentId',1),
	(488,NULL,NULL,NULL,'labPending','error','Please select department','','en_US','departmentId',1),
	(489,NULL,NULL,NULL,'labPending','error','Deparment id mismatch with lab-pending id','','en_US','departmentid_mismatch',1),
	(490,NULL,NULL,NULL,'labPending','label','Edit','','en_US','edit',1),
	(491,NULL,NULL,NULL,'labPending','label','Pending Lab Details:','','en_US','heading',1),
	(492,NULL,NULL,NULL,'labPending','label','External/Internal','','en_US','internal_external_lab',1),
	(493,NULL,NULL,NULL,'labPending','error','Please specify lab type (External/Internal)','','en_US','internal_external_lab',1),
	(494,NULL,NULL,NULL,'labPending','control','select:external:External;internal:Internal','','en_US','internal_external_lab',1),
	(495,NULL,NULL,NULL,'labPending','constraint','NotEmpty','','en_US','internal_external_lab',1),
	(496,NULL,NULL,NULL,'labPending','metaposition','10','','en_US','internal_external_lab',1),
	(497,NULL,NULL,NULL,'labPending','error','Lab-pending id does not exist','','en_US','labpendingid_notexist',1),
	(498,NULL,NULL,NULL,'labPending','label','Lab Name','','en_US','name',1),
	(499,NULL,NULL,NULL,'labPending','error','Lab Name cannot be empty','','en_US','name',1),
	(500,NULL,NULL,NULL,'labPending','label','Submit','','en_US','newLabSubmit',1),
	(501,NULL,NULL,NULL,'labPending','label','Request subject to WASP administrator confirmation of principal investigator status.','','en_US','newPiNote',1),
	(502,NULL,NULL,NULL,'labPending','label','Pending Lab Details','','en_US','pendingLabDetails',1),
	(503,NULL,NULL,NULL,'labPending','label','Phone','','en_US','phone',1),
	(504,NULL,NULL,NULL,'labPending','error','Phone cannot be empty','','en_US','phone',1),
	(505,NULL,NULL,NULL,'labPending','constraint','NotEmpty','','en_US','phone',1),
	(506,NULL,NULL,NULL,'labPending','metaposition','20','','en_US','phone',1),
	(507,NULL,NULL,NULL,'labPending','label','Primary User','','en_US','primaryUserId',1),
	(508,NULL,NULL,NULL,'labPending','error','Please select Primary User','','en_US','primaryUserId',1),
	(509,NULL,NULL,NULL,'labPending','label','Reject','','en_US','reject',1),
	(510,NULL,NULL,NULL,'labPending','label','New lab application successfully rejected','','en_US','rejected',1),
	(511,NULL,NULL,NULL,'labPending','label','Save Changes','','en_US','save',1),
	(512,NULL,NULL,NULL,'labPending','error','Status must be pending with lab-pending id','','en_US','status_mismatch',1),
	(513,NULL,NULL,NULL,'labPending','error','Pending lab is already approved or rejected','','en_US','status_not_pending',1),
	(514,NULL,NULL,NULL,'labPending','error','Pending Lab was NOT updated. Please fill in required fields.','','en_US','updated',1),
	(515,NULL,NULL,NULL,'labPending','label','Pending Lab updated successfully.','','en_US','updated_success',1),
	(516,NULL,NULL,NULL,'labuser','label','Actions','','en_US','actions',1),
	(517,NULL,NULL,NULL,'labuser','label','Active','','en_US','active',1),
	(518,NULL,NULL,NULL,'labuser','label','Current Lab Members','','en_US','current',1),
	(519,NULL,NULL,NULL,'labuser','label','Email','','en_US','email',1),
	(520,NULL,NULL,NULL,'labuser','label','Inactive','','en_US','inactive',1),
	(521,NULL,NULL,NULL,'labuser','label','Request subject to principal investigator acceptance.','','en_US','labUserNote',1),
	(522,NULL,NULL,NULL,'labuser','error','Cannot locate specified lab user in specified lab','','en_US','labUserNotFoundInLab',1),
	(523,NULL,NULL,NULL,'labuser','label','Login Name','','en_US','loginName',1),
	(524,NULL,NULL,NULL,'labuser','label','Name','','en_US','name',1),
	(525,NULL,NULL,NULL,'labuser','label','Pending Lab Members','','en_US','request',1),
	(526,NULL,NULL,NULL,'labuser','error','You are currently a member of the requested lab.','','en_US','request_alreadyaccess',1),
	(527,NULL,NULL,NULL,'labuser','error','You are already a pending user for the requested lab.','','en_US','request_alreadypending',1),
	(528,NULL,NULL,NULL,'labuser','label','WASP Login Name of Primary Investigator','','en_US','request_primaryuser',1),
	(529,NULL,NULL,NULL,'labuser','error','Invalid Primary User','','en_US','request_primaryuser',1),
	(530,NULL,NULL,NULL,'labuser','label','Request Access','','en_US','request_submit',1),
	(531,NULL,NULL,NULL,'labuser','label','Your request for access has been submitted.','','en_US','request_success',1),
	(532,NULL,NULL,NULL,'labuser','label','Role','','en_US','role',1),
	(533,NULL,NULL,NULL,'labuser','label','Roles updated successfully','','en_US','role_change_request',1),
	(534,NULL,NULL,NULL,'labuser','label','Status','','en_US','status',1),
	(535,NULL,NULL,NULL,'labuser','label','Activate','','en_US','status_activate',1),
	(536,NULL,NULL,NULL,'labuser','label','Deactivate','','en_US','status_deactivate',1),
	(537,NULL,NULL,NULL,'labuser','label','DEMOTE to LU','','en_US','status_demoteLU',1),
	(538,NULL,NULL,NULL,'labuser','label','PROMOTE to LM','','en_US','status_promoteLM',1),
	(539,NULL,NULL,NULL,'libraryCreated','label','Library successfully created.','','en_US','created_success',1),
	(540,NULL,NULL,NULL,'libraryCreated','error','Failure to send status message to wasp-daemon','','en_US','message_fail',1),
	(541,NULL,NULL,NULL,'libraryCreated','error','Problem occurred with the preparing the library for saving','','en_US','sample_problem',1),
	(542,NULL,NULL,NULL,'librarydetail_ro','label','Cancel','','en_US','cancel',1),
	(543,NULL,NULL,NULL,'librarydetail_ro','label','Edit','','en_US','edit',1),
	(544,NULL,NULL,NULL,'librarydetail_ro','label','Library Details','','en_US','libraryDetails',1),
	(545,NULL,NULL,NULL,'librarydetail_ro','label','Library Name','','en_US','libraryName',1),
	(546,NULL,NULL,NULL,'librarydetail_ro','label','Library Type','','en_US','librarySampleType',1),
	(547,NULL,NULL,NULL,'librarydetail_rw','label','Cancel','','en_US','cancel',1),
	(548,NULL,NULL,NULL,'librarydetail_rw','label','Library Details','','en_US','libraryDetails',1),
	(549,NULL,NULL,NULL,'librarydetail_rw','label','Library Name','','en_US','libraryName',1),
	(550,NULL,NULL,NULL,'librarydetail_rw','label','Library Subtype','','en_US','librarySubtype',1),
	(551,NULL,NULL,NULL,'librarydetail_rw','label','Library Type','','en_US','libraryType',1),
	(552,NULL,NULL,NULL,'librarydetail_rw','label','Save','','en_US','save',1),
	(553,NULL,NULL,NULL,'librarydetail_rw','label','--SELECT NEW ADAPTOR SET--','','en_US','selectNewAdaptorSet',1),
	(554,NULL,NULL,NULL,'listJobSamples','label','Adaptor','','en_US','adaptor',1),
	(555,NULL,NULL,NULL,'listJobSamples','label','Add Library To Platform Unit','','en_US','addLibraryToPlatformUnit',1),
	(556,NULL,NULL,NULL,'listJobSamples','label','Add New Viewer','','en_US','addNewViewer',1),
	(557,NULL,NULL,NULL,'listJobSamples','label','Person is already a viewer for this job.','','en_US','alreadyIsViewerOfThisJob',1),
	(558,NULL,NULL,NULL,'listJobSamples','label','Arrival Status','','en_US','arrivalStatus',1),
	(559,NULL,NULL,NULL,'listJobSamples','label','Cancel','','en_US','cancel',1),
	(560,NULL,NULL,NULL,'listJobSamples','label','Cell','','en_US','cell',1),
	(561,NULL,NULL,NULL,'listJobSamples','label','Create Library','','en_US','createLibrary',1),
	(562,NULL,NULL,NULL,'listJobSamples','label','Final Concentration (pM)','','en_US','finalConcentrationPM',1),
	(563,NULL,NULL,NULL,'listJobSamples','label','Hide Files','','en_US','hideJobFiles',1),
	(564,NULL,NULL,NULL,'listJobSamples','label','Hide User-Requested Coverage','','en_US','hideUserRequestedCoverage',1),
	(565,NULL,NULL,NULL,'listJobSamples','label','Index','','en_US','index',1),
	(566,NULL,NULL,NULL,'listJobSamples','label','Initial Macromolecules','','en_US','initialMacromolecules',1),
	(567,NULL,NULL,NULL,'listJobSamples','label','You may NOT perform this operation.','','en_US','illegalOperation',1),
	(568,NULL,NULL,NULL,'listJobSamples','label','Email format invalid.','','en_US','invalidFormatEmailAddress',1),
	(569,NULL,NULL,NULL,'listJobSamples','label','Job Not Found In Database','','en_US','jobNotFound',1),
	(570,NULL,NULL,NULL,'listJobSamples','label','New Job Viewer Added','','en_US','jobViewerAdded',1),
	(571,NULL,NULL,NULL,'listJobSamples','label','Selected Viewer Has Been Removed','','en_US','jobViewerRemoved',1),
	(572,NULL,NULL,NULL,'listJobSamples','label','Job Viewers','','en_US','jobViewers',1),
	(573,NULL,NULL,NULL,'listJobSamples','label','Libraries','','en_US','libraries',1),
	(574,NULL,NULL,NULL,'listJobSamples','label','Library','','en_US','library',1),
	(575,NULL,NULL,NULL,'listJobSamples','label','Library (Control)','','en_US','libraryControl',1),
	(576,NULL,NULL,NULL,'listJobSamples','label','log sample','','en_US','logSample',1),
	(577,NULL,NULL,NULL,'listJobSamples','label','Please provide an email address','','en_US','missingEmailAddress',1),
	(578,NULL,NULL,NULL,'listJobSamples','label','Name','','en_US','name',1),
	(579,NULL,NULL,NULL,'listJobSamples','label','Email Address Of Viewer','','en_US','newViewerEmailAddress',1),
	(580,NULL,NULL,NULL,'listJobSamples','label','No Libraries Created','','en_US','noLibrariesCreated',1),
	(581,NULL,NULL,NULL,'listJobSamples','label','No Platform Units / Runs','','en_US','noPlatformUnitsAndRuns',1),
	(582,NULL,NULL,NULL,'listJobSamples','label','Please provide a numeric value for Final Concentration in the cell (pM)','','en_US','numValFinalConc_alert',1),
	(583,NULL,NULL,NULL,'listJobSamples','label','Platform Unit','','en_US','platformUnit',1),
	(584,NULL,NULL,NULL,'listJobSamples','label','Platform Units / Runs','','en_US','platformUnitsAndRuns',1),
	(585,NULL,NULL,NULL,'listJobSamples','label','Job PI may NOT be removed','','en_US','piRemovalIllegal',1),
	(586,NULL,NULL,NULL,'listJobSamples','label','QC Status','','en_US','qcStatus',1),
	(587,NULL,NULL,NULL,'listJobSamples','label','Role Not Found. Unable To Proceed.','','en_US','roleNotFound',1),
	(588,NULL,NULL,NULL,'listJobSamples','label','remove','','en_US','remove',1),
	(589,NULL,NULL,NULL,'listJobSamples','label','--SELECT ADAPTOR SET FOR NEW LIBRARY--','','en_US','selectAdaptorSetForNewLibrary',1),
	(590,NULL,NULL,NULL,'listJobSamples','label','--SELECT A PLATFORM UNIT CELL--','','en_US','selectPlatformUnitCell',1),
	(591,NULL,NULL,NULL,'listJobSamples','label','Show Files','','en_US','showJobFiles',1),
	(592,NULL,NULL,NULL,'listJobSamples','label','Show User-Requested Coverage','','en_US','showUserRequestedCoverage',1),
	(593,NULL,NULL,NULL,'listJobSamples','label','Species','','en_US','species',1),
	(594,NULL,NULL,NULL,'listJobSamples','label','Submit','','en_US','submit',1),
	(595,NULL,NULL,NULL,'listJobSamples','label','Job Submitter may NOT be removed','','en_US','submitterRemovalIllegal',1),
	(596,NULL,NULL,NULL,'listJobSamples','label','Type','','en_US','type',1),
	(597,NULL,NULL,NULL,'listJobSamples','label','User-Submitted Libraries','','en_US','userSubmittedLibraries',1),
	(598,NULL,NULL,NULL,'listJobSamples','label','User not found in database.','','en_US','userNotFound',1),
	(599,NULL,NULL,NULL,'listJobSamples','label','Email address not found in database.','','en_US','userNotFoundByEmailAddress',1),
	(600,NULL,NULL,NULL,'listJobSamples','label','User is not a registered viewer of this job.','','en_US','userNotViewerOfThisJob',1),
	(601,NULL,NULL,NULL,'listJobSamples','label','Please provide a value for Final Concentration in the cell (pM)','','en_US','valFinalConc_alert',1),
	(602,NULL,NULL,NULL,'listJobSamples','label','View','','en_US','view',1),
	(603,NULL,NULL,NULL,'listJobSamples','label','You must select a cell','','en_US','youMustSelectCell_alert',1),
	(604,NULL,NULL,NULL,'lmpendingtask','label','APPROVE','','en_US','approve',1),
	(605,NULL,NULL,NULL,'lmpendingtask','label','Email','','en_US','email',1),
	(606,NULL,NULL,NULL,'lmpendingtask','label','Job ID','','en_US','jobId',1),
	(607,NULL,NULL,NULL,'lmpendingtask','label','Job Name','','en_US','jobName',1),
	(608,NULL,NULL,NULL,'lmpendingtask','label','Name','','en_US','name',1),
	(609,NULL,NULL,NULL,'lmpendingtask','label','PI','','en_US','pi',1),
	(610,NULL,NULL,NULL,'lmpendingtask','label','REJECT','','en_US','reject',1),
	(611,NULL,NULL,NULL,'lmpendingtask','label','Samples','','en_US','samples',1),
	(612,NULL,NULL,NULL,'lmpendingtask','label','Submitter','','en_US','submitter',1),
	(613,NULL,NULL,NULL,'lmpendingtask','label','Pending Users','','en_US','subtitle1',1),
	(614,NULL,NULL,NULL,'lmpendingtask','label','No Pending Users','','en_US','subtitle1_none',1),
	(615,NULL,NULL,NULL,'lmpendingtask','label','Pending Jobs','','en_US','subtitle2',1),
	(616,NULL,NULL,NULL,'lmpendingtask','label','No Pending Jobs','','en_US','subtitle2_none',1),
	(617,NULL,NULL,NULL,'lmpendingtask','label','Workflow','','en_US','workflow',1),
	(618,NULL,NULL,NULL,'lmpendingtask','label','PI/Lab Manager Pending Tasks','','en_US','title',1),
	(619,NULL,NULL,NULL,'metadata','error','Length exceeds maximum permitted','','en_US','lengthMax',1),
	(620,NULL,NULL,NULL,'metadata','error','Length less than minimum permitted','','en_US','lengthMin',1),
	(621,NULL,NULL,NULL,'metadata','error','Value is not of expected type','','en_US','metaType',1),
	(622,NULL,NULL,NULL,'metadata','error','Value exceeds maximum permitted','','en_US','rangeMax',1),
	(623,NULL,NULL,NULL,'metadata','error','Value less than minimum permitted','','en_US','rangeMin',1),
	(624,NULL,NULL,NULL,'pageTitle','label','List of Job Quotes','','en_US','acctquote/list',1),
	(625,NULL,NULL,NULL,'pageTitle','label','Confirm Email Address','','en_US','auth/confirmemail/authcodeform',1),
	(626,NULL,NULL,NULL,'pageTitle','label','Unexpected Email Error','','en_US','auth/confirmemail/confirmemailerror',1),
	(627,NULL,NULL,NULL,'pageTitle','label','Confirm New Email Address','','en_US','auth/confirmemail/emailchanged',1),
	(628,NULL,NULL,NULL,'pageTitle','label','Request to Change Email','','en_US','auth/confirmemail/requestEmailChange',1),
	(629,NULL,NULL,NULL,'pageTitle','label','Confirm New User Id','','en_US','auth/confirmemail/useridchanged',1),
	(630,NULL,NULL,NULL,'pageTitle','label','Confirm New Email Address','','en_US','auth/confirmemail/userloginandemailchanged',1),
	(631,NULL,NULL,NULL,'pageTitle','label','User Login Changed','','en_US','auth/confirmemail/userloginchanged',1),
	(632,NULL,NULL,NULL,'pageTitle','label','Email for Demo','','en_US','auth/getEmailForDemoForm',1),
	(633,NULL,NULL,NULL,'pageTitle','label','Proceed To Login','','en_US','auth/directusertologin',1),
	(634,NULL,NULL,NULL,'pageTitle','label','Login','','en_US','auth/login',1),
	(635,NULL,NULL,NULL,'pageTitle','label','Application Submitted','','en_US','auth/newpi/created',1),
	(636,NULL,NULL,NULL,'pageTitle','label','Email Confirmed','','en_US','auth/newpi/emailok',1),
	(637,NULL,NULL,NULL,'pageTitle','label','New Principal Investigator','','en_US','auth/newpi/form',1),
	(638,NULL,NULL,NULL,'pageTitle','label','New Principal Investigator','','en_US','auth/newpi/institute',1),
	(639,NULL,NULL,NULL,'pageTitle','label','Application Submitted','','en_US','auth/newuser/created',1),
	(640,NULL,NULL,NULL,'pageTitle','label','Email Confirmed','','en_US','auth/newuser/emailok',1),
	(641,NULL,NULL,NULL,'pageTitle','label','New User','','en_US','auth/newuser/form',1),
	(642,NULL,NULL,NULL,'pageTitle','label','Reset Password','','en_US','auth/resetpassword/authcodeform',1),
	(643,NULL,NULL,NULL,'pageTitle','label','Email Sent','','en_US','auth/resetpassword/email',1),
	(644,NULL,NULL,NULL,'pageTitle','label','Reset Password','','en_US','auth/resetpassword/form',1),
	(645,NULL,NULL,NULL,'pageTitle','label','Password Was Reset','','en_US','auth/resetpassword/ok',1),
	(646,NULL,NULL,NULL,'pageTitle','label','Reset Password Request','','en_US','auth/resetpassword/request',1),
	(647,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'5CFF37E9F6D4457BAC6EF17789D46172','pageTitle','label','Home','','en_US','dashboard',1),
	(648,NULL,NULL,NULL,'pageTitle','label','Department Administrator Pending Tasks','','en_US','department/dapendingtasks',1),
	(649,NULL,NULL,NULL,'pageTitle','label','Department Detail','','en_US','department/detail',1),
	(650,NULL,NULL,NULL,'pageTitle','label','Department List','','en_US','department/list',1),
	(651,NULL,NULL,NULL,'pageTitle','label','Assign Library To Platform Unit','','en_US','facility/platformunit/assign',1),
	(652,NULL,NULL,NULL,'pageTitle','label','Create/Update Platform Unit','','en_US','facility/platformunit/createUpdatePlatformUnit',1),
	(653,NULL,NULL,NULL,'pageTitle','label','Update Platform Unit','','en_US','facility/platformunit/updatePlatformUnit',1),
	(654,NULL,NULL,NULL,'pageTitle','label','Platform Unit Instance List','','en_US','facility/platformunit/instance/list',1),
	(655,NULL,NULL,NULL,'pageTitle','label','Machine Type Assignment','','en_US','facility/platformunit/limitPriorToAssign',1),
	(656,NULL,NULL,NULL,'pageTitle','label','Platform Unit List','','en_US','facility/platformunit/list',1),
	(657,NULL,NULL,NULL,'pageTitle','label','Platform Unit / Sequence Run','','en_US','facility/platformunit/showPlatformUnit',1),
	(658,NULL,NULL,NULL,'pageTitle','label','Job Comments','','en_US','job/comments',1),
	(659,NULL,NULL,NULL,'pageTitle','label','Job List','','en_US','job/list',1),
	(660,NULL,NULL,NULL,'pageTitle','label','Active Jobs With Libraries To Be Created','','en_US','job/jobsAwaitingLibraryCreation/jobsAwaitingLibraryCreationList',1),
	(661,NULL,NULL,NULL,'pageTitle','label','List of Jobs to Quote','','en_US','job2quote/list',1),
	(662,NULL,NULL,NULL,'pageTitle','label','Quotes','','en_US','job2quote/list_all',1),
	(663,NULL,NULL,NULL,'pageTitle','label','Cells','','en_US','jobsubmit/cell',1),
	(664,NULL,NULL,NULL,'pageTitle','label','New Job','','en_US','jobsubmit/create',1),
	(665,NULL,NULL,NULL,'pageTitle','label','Add A Comment','','en_US','jobsubmit/comment',1),
	(666,NULL,NULL,NULL,'pageTitle','label','List of Job Drafts','','en_US','jobsubmit/list',1),
	(667,NULL,NULL,NULL,'pageTitle','label','New Job','','en_US','jobsubmit/metaform',1),
	(668,NULL,NULL,NULL,'pageTitle','label','Job Successfully Submitted','','en_US','jobsubmit/ok',1),
	(669,NULL,NULL,NULL,'pageTitle','label','Job Submission Failed','','en_US','jobsubmit/failed',1),
	(670,NULL,NULL,NULL,'pageTitle','label','Submit Samples','','en_US','jobsubmit/sample',1),
	(671,NULL,NULL,NULL,'pageTitle','label','View Sample Draft','','en_US','jobsubmit/sample/sampledetail_ro',1),
	(672,NULL,NULL,NULL,'pageTitle','label','Edit Sample Draft','','en_US','jobsubmit/sample/sampledetail_rw',1),
	(673,NULL,NULL,NULL,'pageTitle','label','Verify New Job','','en_US','jobsubmit/verify',1),
	(674,NULL,NULL,NULL,'pageTitle','label','PI/Lab Manager Pending Tasks','','en_US','lab/allpendinglmapproval/list',1),
	(675,NULL,NULL,NULL,'pageTitle','label','Lab Detail','','en_US','lab/detail_ro',1),
	(676,NULL,NULL,NULL,'pageTitle','label','Update Lab Detail','','en_US','lab/detail_rw',1),
	(677,NULL,NULL,NULL,'pageTitle','label','Lab List','','en_US','lab/list',1),
	(678,NULL,NULL,NULL,'pageTitle','label','Request Access to Lab','','en_US','lab/newrequest',1),
	(679,NULL,NULL,NULL,'pageTitle','label','Pending Lab Detail','','en_US','lab/pending/detail_ro',1),
	(680,NULL,NULL,NULL,'pageTitle','label','Update Pending Lab Detail','','en_US','lab/pending/detail_rw',1),
	(681,NULL,NULL,NULL,'pageTitle','label','PI/Lab Manager Pending Tasks','','en_US','lab/pendinglmapproval/list',1),
	(682,NULL,NULL,NULL,'pageTitle','label','Pending Users','','en_US','lab/pendinguser/list',1),
	(683,NULL,NULL,NULL,'pageTitle','label','Lab Member List','','en_US','lab/user_list',1),
	(684,NULL,NULL,NULL,'pageTitle','label','Lab User Manager','','en_US','lab/user_manager',1),
	(685,NULL,NULL,NULL,'pageTitle','label','Create New Resource','','en_US','resource/create',1),
	(686,NULL,NULL,NULL,'pageTitle','label','Resource Detail','','en_US','resource/detail_ro',1),
	(687,NULL,NULL,NULL,'pageTitle','label','Update Resource Detail','','en_US','resource/detail_rw',1),
	(688,NULL,NULL,NULL,'pageTitle','label','Machine List','','en_US','resource/list',1),
	(689,NULL,NULL,NULL,'pageTitle','label','Run Detail','','en_US','run/detail',1),
	(690,NULL,NULL,NULL,'pageTitle','label','Run List','','en_US','run/list',1),
	(691,NULL,NULL,NULL,'pageTitle','label','Create/Update Sequence Run','','en_US','run/createUpdateRun',1),
	(692,NULL,NULL,NULL,'pageTitle','label','Sample Utilities','','en_US','sample/list',1),
	(693,NULL,NULL,NULL,'pageTitle','label','Control Libraries','','en_US','sample/controlLibraries/list',1),
	(694,NULL,NULL,NULL,'pageTitle','label','Create/Update Control Libraries','','en_US','sample/controlLibraries/createUpdate',1),
	(695,NULL,NULL,NULL,'pageTitle','label','New Library','','en_US','sampleDnaToLibrary/createLibrary',1),
	(696,NULL,NULL,NULL,'pageTitle','label','Library Details','','en_US','sampleDnaToLibrary/librarydetail_ro',1),
	(697,NULL,NULL,NULL,'pageTitle','label','Update Library','','en_US','sampleDnaToLibrary/librarydetail_rw',1),
	(698,NULL,NULL,NULL,'pageTitle','label','Samples And Libraries','','en_US','sampleDnaToLibrary/listJobSamples',1),
	(699,NULL,NULL,NULL,'pageTitle','label','Sample Details','','en_US','sampleDnaToLibrary/sampledetail_ro',1),
	(700,NULL,NULL,NULL,'pageTitle','label','Update Sample Details','','en_US','sampleDnaToLibrary/sampledetail_rw',1),
	(701,NULL,NULL,NULL,'pageTitle','label','System User Management','','en_US','sysrole/list',1),
	(702,NULL,NULL,NULL,'pageTitle','label','Assign Libraries','','en_US','task/assignLibraries/lists',1),
	(703,NULL,NULL,NULL,'pageTitle','label','Requote Pending Jobs','','en_US','task/detail',1),
	(704,NULL,NULL,NULL,'pageTitle','label','Receive Payment for Jobs','','en_US','task/fmpayment/list',1),
	(705,NULL,NULL,NULL,'pageTitle','label','Requote Pending Jobs','','en_US','task/fmrequote/list',1),
	(706,NULL,NULL,NULL,'pageTitle','label','Library QC Manager','','en_US','task/libraryqc/list',1),
	(707,NULL,NULL,NULL,'pageTitle','label','Incoming Sample Manager','','en_US','task/samplereceive/list',1),
	(708,NULL,NULL,NULL,'pageTitle','label','Sample QC Manager','','en_US','task/sampleqc/list',1),
	(709,NULL,NULL,NULL,'pageTitle','label','Properties','','en_US','uifield/list',1),
	(710,NULL,NULL,NULL,'pageTitle','label','User Detail','','en_US','user/detail_ro',1),
	(711,NULL,NULL,NULL,'pageTitle','label','Update User Detail','','en_US','user/detail_rw',1),
	(712,NULL,NULL,NULL,'pageTitle','label','User List','','en_US','user/list',1),
	(713,NULL,NULL,NULL,'pageTitle','label','Change Password','','en_US','user/mypassword',1),
	(714,NULL,NULL,NULL,'pageTitle','label','Workflow List','','en_US','workflow/list',1),
	(715,NULL,NULL,NULL,'pageTitle','label','Workflow Resource Assignment','','en_US','workflow/resource/configure',1),
	(716,NULL,NULL,NULL,'pendingJob','label','Approve','','en_US','detailRO_approve',1),
	(717,NULL,NULL,NULL,'pendingJob','label','Job','','en_US','detailRO_job',1),
	(718,NULL,NULL,NULL,'pendingJob','label','PI','','en_US','detailRO_pi',1),
	(719,NULL,NULL,NULL,'pendingJob','label','Reject','','en_US','detailRO_reject',1),
	(720,NULL,NULL,NULL,'pendingJob','label','Submitting User','','en_US','detailRO_submittingUser',1),
	(721,NULL,NULL,NULL,'piPending','label','Address','','en_US','address',1),
	(722,NULL,NULL,NULL,'piPending','error','Address cannot be empty','','en_US','address',1),
	(723,NULL,NULL,NULL,'piPending','constraint','NotEmpty','','en_US','address',1),
	(724,NULL,NULL,NULL,'piPending','metaposition','40','','en_US','address',1),
	(725,NULL,NULL,NULL,'piPending','label','Building / Room','','en_US','building_room',1),
	(726,NULL,NULL,NULL,'piPending','metaposition','30','','en_US','building_room',1),
	(727,NULL,NULL,NULL,'piPending','error','Captcha text incorrect','','en_US','captcha',1),
	(728,NULL,NULL,NULL,'piPending','label','Captcha text','','en_US','captcha',1),
	(729,NULL,NULL,NULL,'piPending','label','City','','en_US','city',1),
	(730,NULL,NULL,NULL,'piPending','error','City cannot be empty','','en_US','city',1),
	(731,NULL,NULL,NULL,'piPending','constraint','NotEmpty','','en_US','city',1),
	(732,NULL,NULL,NULL,'piPending','metaposition','50','','en_US','city',1),
	(733,NULL,NULL,NULL,'piPending','label','Country','','en_US','country',1),
	(734,NULL,NULL,NULL,'piPending','error','Country cannot be empty','','en_US','country',1),
	(735,NULL,NULL,NULL,'piPending','control','select:${countries}:code:name','','en_US','country',1),
	(736,NULL,NULL,NULL,'piPending','constraint','NotEmpty','','en_US','country',1),
	(737,NULL,NULL,NULL,'piPending','metaposition','70','','en_US','country',1),
	(738,NULL,NULL,NULL,'piPending','error','Department cannot be empty','','en_US','departmentId',1),
	(739,NULL,NULL,NULL,'piPending','label','Department','','en_US','departmentId',1),
	(740,NULL,NULL,NULL,'piPending','constraint','NotEmpty','','en_US','departmentId',1),
	(741,NULL,NULL,NULL,'piPending','control','select:${departments}:departmentId:name','','en_US','departmentId',1),
	(742,NULL,NULL,NULL,'piPending','metaposition','20','','en_US','departmentId',1),
	(743,NULL,NULL,NULL,'piPending','label','Email','','en_US','email',1),
	(744,NULL,NULL,NULL,'piPending','error','Wrong email address format','','en_US','email',1),
	(745,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'DE4DA47410C148ECA9C155624BAF9F34','piPending','label','Your email address is confirmed. A WASP departmental administrator has been requested (via email) to approve your application as a new Principal Investigator. Once approved, you will be informed via email and you will be permitted to log in. ','','en_US','emailconfirmed',1),
	(746,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'5A76F51E025343F788789AF2F86384C2','piPending','label','Thank you for applying for a new Principal Investigator account. You have been sent an email with instructions describing how to quickly confirm your email address. Your registration cannot proceed until your email address has been confirmed.','','en_US','emailsent',1),
	(747,NULL,NULL,NULL,'piPending','error','Email already exists in the database','','en_US','email_exists',1),
	(748,NULL,NULL,NULL,'piPending','label','Fax','','en_US','fax',1),
	(749,NULL,NULL,NULL,'piPending','metaposition','100','','en_US','fax',1),
	(750,NULL,NULL,NULL,'piPending','label','First Name','','en_US','firstName',1),
	(751,NULL,NULL,NULL,'piPending','error','First Name cannot be empty','','en_US','firstName',1),
	(752,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'420760C9344C4DC7AADECFA154E24F17','piPending','label','If you do NOT already have a WASP account and you want to register as a new Principal Investigator, fill out the form below.','','en_US','form_instructions',1),
	(753,NULL,NULL,NULL,'piPending','error','You cannot select more than one institute','','en_US','institute_multi_select',1),
	(754,NULL,NULL,NULL,'piPending','error','You must select an institute','','en_US','institute_not_selected',1),
	(755,NULL,NULL,NULL,'piPending','label','Institution','','en_US','institution',1),
	(756,NULL,NULL,NULL,'piPending','error','Institution cannot be empty','','en_US','institution',1),
	(757,NULL,NULL,NULL,'piPending','constraint','NotEmpty','','en_US','institution',1),
	(758,NULL,NULL,NULL,'piPending','metaposition','10','','en_US','institution',1),
	(759,NULL,NULL,NULL,'piPending','data','Einstein;Montifiore','','en_US','internal_institute_list',1),
	(760,NULL,NULL,NULL,'piPending','label','Lab Name','','en_US','labName',1),
	(761,NULL,NULL,NULL,'piPending','error','Lab Name cannot be empty','','en_US','labName',1),
	(762,NULL,NULL,NULL,'piPending','constraint','NotEmpty','','en_US','labName',1),
	(763,NULL,NULL,NULL,'piPending','metaposition','1','','en_US','labName',1),
	(764,NULL,NULL,NULL,'piPending','label','Last Name','','en_US','lastName',1),
	(765,NULL,NULL,NULL,'piPending','error','Last Name cannot be empty','','en_US','lastName',1),
	(766,'2013-03-12 12:39:36','2013-03-12 12:39:36',X'96DEE66D51194013ABCAD8DA25AD4AF5','piPending','label','Preferred Language','','en_US','locale',1),
	(767,NULL,NULL,NULL,'piPending','constraint','NotEmpty','','en_US','locale',1),
	(768,'2013-03-12 12:39:36','2013-03-12 12:39:36',X'D64A828DE28A4E21B81FC6EF4EEC383B','piPending','error','Language cannot be empty','','en_US','locale',1),
	(769,NULL,NULL,NULL,'piPending','label','Login','','en_US','login',1),
	(770,NULL,NULL,NULL,'piPending','error','Login cannot be empty','','en_US','login',1),
	(771,NULL,NULL,NULL,'piPending','label','Password','','en_US','password',1),
	(772,NULL,NULL,NULL,'piPending','error','Password cannot be empty','','en_US','password',1),
	(773,NULL,NULL,NULL,'piPending','label','Re-confirm Password','','en_US','password2',1),
	(774,NULL,NULL,NULL,'piPending','error','Re-confirm password cannot be empty','','en_US','password2',1),
	(775,NULL,NULL,NULL,'piPending','error','Password must be at least 8 characters, containing only letters and numbers, with at least one letter and number','','en_US','password_invalid',1),
	(776,NULL,NULL,NULL,'piPending','error','The two entries for your password are NOT identical','','en_US','password_mismatch',1),
	(777,NULL,NULL,NULL,'piPending','label','Phone','','en_US','phone',1),
	(778,NULL,NULL,NULL,'piPending','error','Phone cannot be empty','','en_US','phone',1),
	(779,NULL,NULL,NULL,'piPending','constraint','NotEmpty','','en_US','phone',1),
	(780,NULL,NULL,NULL,'piPending','metaposition','90','','en_US','phone',1),
	(781,NULL,NULL,NULL,'piPending','label','-- select --','','en_US','select_default',1),
	(782,NULL,NULL,NULL,'piPending','label','Institute','','en_US','select_institute',1),
	(783,'2013-03-12 12:39:36','2013-03-12 12:39:36',X'AD39AD235D2F498F9E8020941867C7E2','piPending','label','If you do NOT have a WASP account and you want to register as a new Principal Investigator, please select your institute or select \"other\" and type your institute name','','en_US','select_institute_message',1),
	(784,NULL,NULL,NULL,'piPending','label','Other','','en_US','select_institute_other',1),
	(785,NULL,NULL,NULL,'piPending','label','Submit','','en_US','select_institute_submit',1),
	(786,NULL,NULL,NULL,'piPending','label','If \"Other\" Please specify','','en_US','specify_other_institute',1),
	(787,NULL,NULL,NULL,'piPending','label','State','','en_US','state',1),
	(788,NULL,NULL,NULL,'piPending','control','select:${states}:code:name','','en_US','state',1),
	(789,NULL,NULL,NULL,'piPending','metaposition','60','','en_US','state',1),
	(790,NULL,NULL,NULL,'piPending','label','Apply for Account','','en_US','submit',1),
	(791,NULL,NULL,NULL,'piPending','label','Title','','en_US','title',1),
	(792,NULL,NULL,NULL,'piPending','error','Title cannot be empty','','en_US','title',1),
	(793,NULL,NULL,NULL,'piPending','constraint','NotEmpty','','en_US','title',1),
	(794,NULL,NULL,NULL,'piPending','control','select:Prof:Prof;Dr:Dr;Mr:Mr;Ms:Ms;Mrs:Mrs;Miss:Miss','','en_US','title',1),
	(795,NULL,NULL,NULL,'piPending','metaposition','5','','en_US','title',1),
	(796,NULL,NULL,NULL,'piPending','label','Zip','','en_US','zip',1),
	(797,NULL,NULL,NULL,'piPending','error','Zip cannot be empty','','en_US','zip',1),
	(798,NULL,NULL,NULL,'piPending','constraint','NotEmpty','','en_US','zip',1),
	(799,NULL,NULL,NULL,'piPending','metaposition','80','','en_US','zip',1),
	(800,NULL,NULL,NULL,'platformunit_assign','label','Adaptor','','en_US','adaptor',1),
	(801,NULL,NULL,NULL,'platformunit_assign','label','Analysis','','en_US','analysis',1),
	(802,NULL,NULL,NULL,'platformunit_assign','label','Assign Libraries For A Run On','','en_US','assignLibToRun',1),
	(803,NULL,NULL,NULL,'platformunit_assign','label','[Add To Cell]','','en_US','addToLane',1),
	(804,NULL,NULL,NULL,'platformunit_assign','label','[Close]','','en_US','close',1),
	(805,NULL,NULL,NULL,'platformunit_assign','label','Final Conc.','','en_US','finalConc',1),
	(806,NULL,NULL,NULL,'platformunit_assign','label','Job','','en_US','job',1),
	(807,NULL,NULL,NULL,'platformunit_assign','label','Job Cell','','en_US','jobCell',1),
	(808,NULL,NULL,NULL,'platformunit_assign','label','Cell','','en_US','lane',1),
	(809,NULL,NULL,NULL,'platformunit_assign','label','Library','','en_US','library',1),
	(810,NULL,NULL,NULL,'platformunit_assign','label','No Libraries Waiting For','','en_US','noLibWaitingFor',1),
	(811,NULL,NULL,NULL,'platformunit_assign','label','No Platform Units Waiting For','','en_US','noPUWaitingFor',1),
	(812,NULL,NULL,NULL,'platformunit_assign','label','Platform Unit','','en_US','platformUnit',1),
	(813,NULL,NULL,NULL,'platformunit_assign','label','[Remove From Cell]','','en_US','removeFromLane',1),
	(814,NULL,NULL,NULL,'platformunit_assign','label','Sample Cell','','en_US','sampleCell',1),
	(815,NULL,NULL,NULL,'platformunit_assign','label','[Show Requested Coverage]','','en_US','showRequestedCoverage',1),
	(816,NULL,NULL,NULL,'platformunit_assign','label','Status','','en_US','recievedStatus',1),
	(817,NULL,NULL,NULL,'platformunit_assign','label','QC Status','','en_US','qcStatus',1),
	(818,NULL,NULL,NULL,'platformunit_assign','label','pM','','en_US','theConcUnits',1),
	(819,NULL,NULL,NULL,'platformunit_assign','label','User-submitted','','en_US','userSubmitted',1),
	(820,NULL,NULL,NULL,'platformunit','error','Action Failed. Libary adaptor barcode NOT found','','en_US','adaptorBarcodeNotFound',1),
	(821,NULL,NULL,NULL,'platformunit','error','Action Failed. Libary adaptor NOT found','','en_US','adaptorNotFound',1),
	(822,NULL,NULL,NULL,'platformunit','label','Barcode','','en_US','barcode',1),
	(823,NULL,NULL,NULL,'platformunit','label','Created','','en_US','date',1),
	(824,NULL,NULL,NULL,'platformunit','error','Action Failed. Platform unit record not found or record is not a platform unit','','en_US','notFoundOrNotCorrectType',1),
	(825,NULL,NULL,NULL,'platformunit','error','Action Failed. Platform Unit record not found or not unique','','en_US','flowcellNotFoundNotUnique',1),
	(826,NULL,NULL,NULL,'platformunit','error','Action Failed. Platform Unit state not compatible with adding libraries','','en_US','flowcellStateError',1),
	(827,NULL,NULL,NULL,'platformunit','error','Action Failed. Value for jobId is unexpectedly NOT valid','','en_US','jobIdNotFound',1),
	(828,NULL,NULL,NULL,'platformunit','error','Select A Job','','en_US','jobIdNotSelected',1),
	(829,NULL,NULL,NULL,'platformunit','error','Selected Job Not Found','','en_US','jobNotFound',1),
	(830,NULL,NULL,NULL,'platformunit','error','Job and resource unexpectedly do not match','','en_US','jobResourceCategoryMismatch',1),
	(831,NULL,NULL,NULL,'platformunit','label','Cells','','en_US','lanecount',1),
	(832,NULL,NULL,NULL,'platformunit','error','Action Failed. Value for cell is unexpectedly NOT valid','','en_US','laneIdNotFound',1),
	(833,NULL,NULL,NULL,'platformunit','error','Action Failed. You mistakenly selected a Platform Unit','','en_US','laneIsFlowCell',1),
	(834,NULL,NULL,NULL,'platformunit','error','Action Failed. Cell selected is unexpectedly NOT a Cell','','en_US','laneIsNotLane',1),
	(835,NULL,NULL,NULL,'platformunit','success','Update Complete. Library added to platform unit.','','en_US','libAdded',1),
	(836,NULL,NULL,NULL,'platformunit','error','Action Failed. Libraries on a cell must be from a single job','','en_US','libJobClash',1),
	(837,NULL,NULL,NULL,'platformunit','error','Action Failed. Value for libraryId is unexpectedly NOT valid','','en_US','libraryIdNotFound',1),
	(838,NULL,NULL,NULL,'platformunit','error','Action Failed. Library selected is unexpectedly NOT a Library','','en_US','libraryIsNotLibrary',1),
	(839,NULL,NULL,NULL,'platformunit','error','Action Failed. Libary does NOT appear to be part of the specified Job','','en_US','libraryJobMismatch',1),
	(840,NULL,NULL,NULL,'platformunit','success','Selected Library Removed From Platform Unit','','en_US','libraryRemoved',1),
	(841,NULL,NULL,NULL,'platformunit','error','Invalid lock status selected','','en_US','lock_status',1),
	(842,NULL,NULL,NULL,'platformunit','error','Action Failed. Barcode on the library is not compatible with other libraries on the cell','','en_US','multiplex',1),
	(843,NULL,NULL,NULL,'platformunit','label','Name','','en_US','name',1),
	(844,NULL,NULL,NULL,'platformunit','label','Platform Unit List','','en_US','platformunit_list',1),
	(845,NULL,NULL,NULL,'platformunit','error','Action Failed. Value for pmol is NOT valid','','en_US','pmoleAddedInvalidValue',1),
	(846,NULL,NULL,NULL,'platformunit','label','Read Length','','en_US','readlength',1),
	(847,NULL,NULL,NULL,'platformunit','label','Read Type','','en_US','readType',1),
	(848,NULL,NULL,NULL,'platformunit','label','Machine Type','','en_US','resourceCategoryName',1),
	(849,NULL,NULL,NULL,'platformunit','error','Resource Not Found','','en_US','resourceCategoryInvalidValue',1),
	(850,NULL,NULL,NULL,'platformunit','error','Parameter error occurred','','en_US','parameter',1),
	(851,NULL,NULL,NULL,'platformunit','error','Type Resource unexpectedly not found','','en_US','resourceTypeNotFound',1),
	(852,NULL,NULL,NULL,'platformunit','error','Action Failed: Selected Library Not Found','','en_US','sampleSourceNotExist',1),
	(853,NULL,NULL,NULL,'platformunit','error','Action Failed: Selected samplesource missing cell or library','','en_US','samplesourceTypeError',1),
	(854,NULL,NULL,NULL,'platformunit','error','Action Failed. Cell or Library type exception raised','','en_US','sampleType',1),
	(855,NULL,NULL,NULL,'platformunit','label','Subtype','','en_US','sampleSubtypeName',1),
	(856,NULL,NULL,NULL,'platformunit','label','Subtype Sample List','','en_US','subtype_list',1),
	(857,NULL,NULL,NULL,'platformunit','error','Task unexpectedly not found','','en_US','taskNotFound',1),
	(858,NULL,NULL,NULL,'platformunit','success','TESTING.','','en_US','TESTING',1),
	(859,NULL,NULL,NULL,'platformunitById','label','Name','','en_US','name',1),
	(860,NULL,NULL,NULL,'platformunitById','label','Platform Unit List','','en_US','platformunitbyid_list',1),
	(861,NULL,NULL,NULL,'platformunitById','label','Submitter','','en_US','submitter',1),
	(862,NULL,NULL,NULL,'platformunitInstance','label','Barcode','','en_US','barcode',1),
	(863,NULL,NULL,NULL,'platformunitInstance','constraint','NotEmpty','','en_US','barcode',1),
	(864,NULL,NULL,NULL,'platformunitInstance','error','Barcode cannot be empty','','en_US','barcode',1),
	(865,NULL,NULL,NULL,'platformunitInstance','error','Barcode already exists in the database','','en_US','barcode_exists',1),
	(866,NULL,NULL,NULL,'platformunitInstance','label','Cancel','','en_US','cancel',1),
	(867,NULL,NULL,NULL,'platformunitInstance','label','Comment','','en_US','comment',1),
	(868,NULL,NULL,NULL,'platformunitInstance','error','Comment','','en_US','comment',1),
	(869,NULL,NULL,NULL,'platformunitInstance','metaposition','60','','en_US','comment',1),
	(870,NULL,NULL,NULL,'platformunitInstance','error','Platformunit was NOT created. Please fill in required fields.','','en_US','created',1),
	(871,NULL,NULL,NULL,'platformunitInstance','label','Platformunit created successfully.','','en_US','created_success',1),
	(872,NULL,NULL,NULL,'platformunitInstance','label','Platformunit deleted successfully.','','en_US','deleted_success',1),
	(873,NULL,NULL,NULL,'platformunitInstance','label','Create New Platform Unit','','en_US','headerCreate',1),
	(874,NULL,NULL,NULL,'platformunitInstance','label','Update Platform Unit','','en_US','headerUpdate',1),
	(875,NULL,NULL,NULL,'platformunitInstance','label','Cell Count','','en_US','lanecount',1),
	(876,NULL,NULL,NULL,'platformunitInstance','constraint','NotEmpty','','en_US','lanecount',1),
	(877,NULL,NULL,NULL,'platformunitInstance','type','INTEGER','','en_US','lanecount',1),
	(878,NULL,NULL,NULL,'platformunitInstance','range','1:1000','','en_US','lanecount',1),
	(879,NULL,NULL,NULL,'platformunitInstance','error','Cell Count cannot be empty','','en_US','lanecount',1),
	(880,NULL,NULL,NULL,'platformunitInstance','error','Cell Count value unexpectedly cannot be determined','','en_US','lanecount_notfound',1),
	(881,NULL,NULL,NULL,'platformunitInstance','error','Cell Count value not valid','','en_US','lanecount_valueinvalid',1),
	(882,NULL,NULL,NULL,'platformunitInstance','error','Action not permitted at this time. To reduce the number of cells, remove libraries on the cells that will be lost.','','en_US','lanecount_valuealteredconflicting',1),
	(883,NULL,NULL,NULL,'platformunitInstance','control','select:${lanes}:valuePassedBack:valueVisible','','en_US','lanecount',1),
	(884,NULL,NULL,NULL,'platformunitInstance','label','Select Platform Unit Type','','en_US','platUnitType',1),
	(885,NULL,NULL,NULL,'platformunitInstance','label','Machine Type','','en_US','resourceCategoryName',1),
	(886,NULL,NULL,NULL,'platformunitInstance','label','Read Type','','en_US','readType',1),
	(887,NULL,NULL,NULL,'platformunitInstance','constraint','NotEmpty','','en_US','readType',1),
	(888,NULL,NULL,NULL,'platformunitInstance','error','Read Type cannot be empty','','en_US','readType',1),
	(889,NULL,NULL,NULL,'platformunitInstance','control','select:${readTypes}:valuePassedBack:valueVisible','','en_US','readType',1),
	(890,NULL,NULL,NULL,'platformunitInstance','metaposition','10','','en_US','readType',1),
	(891,NULL,NULL,NULL,'platformunitInstance','label','Read Length','','en_US','readlength',1),
	(892,NULL,NULL,NULL,'platformunitInstance','constraint','NotEmpty','','en_US','readlength',1),
	(893,NULL,NULL,NULL,'platformunitInstance','error','Read Length cannot be empty','','en_US','readlength',1),
	(894,NULL,NULL,NULL,'platformunitInstance','control','select:${readlengths}:valuePassedBack:valueVisible','','en_US','readlength',1),
	(895,NULL,NULL,NULL,'platformunitInstance','metaposition','15','','en_US','readlength',1),
	(896,NULL,NULL,NULL,'platformunitInstance','label','Cell Count','','en_US','lanecountForEditBox',1),
	(897,NULL,NULL,NULL,'platformunitInstance','error','Please select a number of cells','','en_US','lanecount_empty',1),
	(898,NULL,NULL,NULL,'platformunitInstance','label','Name','','en_US','name',1),
	(899,NULL,NULL,NULL,'platformunitInstance','constraint','NotEmpty','','en_US','name',1),
	(900,NULL,NULL,NULL,'platformunitInstance','error','Name cannot be empty','','en_US','name',1),
	(901,NULL,NULL,NULL,'platformunitInstance','error','Name already exists in the database','','en_US','name_exists',1),
	(902,NULL,NULL,NULL,'platformunitInstance','label','Cell Count','','en_US','numberOfLanesRequested',1),
	(903,NULL,NULL,NULL,'platformunitInstance','error','Cell Count cannot be empty','','en_US','numberOfLanesRequested',1),
	(904,NULL,NULL,NULL,'platformunitInstance','error','Action not permitted at this time. To reduce the number of cells, remove libraries on the cells that will be lost.','','en_US','numberOfLanesRequested_conflict',1),
	(905,NULL,NULL,NULL,'platformunitInstance','label','Platform Unit Instance List','','en_US','platformunitinstance_list',1),
	(906,NULL,NULL,NULL,'platformunitInstance','label','Reset','','en_US','reset',1),
	(907,NULL,NULL,NULL,'platformunitInstance','label','Submitter','','en_US','submitter',1),
	(908,NULL,NULL,NULL,'platformunitInstance','constraint','NotEmpty','','en_US','submitter',1),
	(909,NULL,NULL,NULL,'platformunitInstance','error','Submitter cannot be empty','','en_US','submitter',1),
	(910,NULL,NULL,NULL,'platformunitInstance','label','Subtype','','en_US','subtype',1),
	(911,NULL,NULL,NULL,'platformunitInstance','constraint','NotEmpty','','en_US','subtype',1),
	(912,NULL,NULL,NULL,'platformunitInstance','error','Subtype cannot be empty','','en_US','subtype',1),
	(913,NULL,NULL,NULL,'platformunitInstance','error','Platformunit was NOT updated. Please fill in required fields.','','en_US','updated',1),
	(914,NULL,NULL,NULL,'platformunitInstance','label','Platformunit updated successfully.','','en_US','updated_success',1),
	(915,NULL,NULL,NULL,'platformunitInstance','label','Submit','','en_US','submit',1),
	(916,NULL,NULL,NULL,'platformunitShow','label','Action','','en_US','action',1),
	(917,NULL,NULL,NULL,'platformunitShow','label','Add To Run','','en_US','addToRun',1),
	(918,NULL,NULL,NULL,'platformunitShow','label','Barcode','','en_US','barcodeName',1),
	(919,NULL,NULL,NULL,'platformunitShow','label','Comments','','en_US','comment',1),
	(920,NULL,NULL,NULL,'platformunitShow','label','Delete','','en_US','delete',1),
	(921,NULL,NULL,NULL,'platformunitShow','label','delete','','en_US','deleteSmall',1),
	(922,NULL,NULL,NULL,'platformunitShow','label','Edit','','en_US','edit',1),
	(923,NULL,NULL,NULL,'platformunitShow','label','edit','','en_US','editSmall',1),
	(924,NULL,NULL,NULL,'platformunitShow','label','End','','en_US','end',1),
	(925,NULL,NULL,NULL,'platformunitShow','label','Length','','en_US','length',1),
	(926,NULL,NULL,NULL,'platformunitShow','label','Machine','','en_US','machine',1),
	(927,NULL,NULL,NULL,'platformunitShow','label','Cell Count','','en_US','numberOfCellsOnThisPlatformUnit',1),
	(928,NULL,NULL,NULL,'platformunitShow','label','Read Type','','en_US','readType',1),
	(929,NULL,NULL,NULL,'platformunitShow','label','Read Length','','en_US','readlength',1),
	(930,NULL,NULL,NULL,'showPlatformUnit','label','Remove Library','','en_US','removeLibrary',1),
	(931,NULL,NULL,NULL,'platformunitShow','label','Run','','en_US','run',1),
	(932,NULL,NULL,NULL,'platformunitShow','label','start','','en_US','start',1),
	(933,NULL,NULL,NULL,'platformunitShow','label','Status','','en_US','status',1),
	(934,NULL,NULL,NULL,'platformunitShow','label','Type','','en_US','type',1),
	(935,NULL,NULL,NULL,'platformunitShow','label','Type','','en_US','typeOfPlatformUnit',1),
	(936,NULL,NULL,NULL,'platformunitShow','label','Do you really want to delete this platform unit record?','','en_US','wantToDeletePU',1),
	(937,NULL,NULL,NULL,'platformunitShow','label','Do you really want to delete this run record?','','en_US','wantToDeleteRun',1),
	(938,NULL,NULL,NULL,'puLimitPriorToAssign','label','All Available Jobs','','en_US','allAvailableJobs',1),
	(939,NULL,NULL,NULL,'puLimitPriorToAssign','label','Choose A Job','','en_US','chooseJob',1),
	(940,NULL,NULL,NULL,'puLimitPriorToAssign','label','Choose A Machine','','en_US','chooseMachine',1),
	(941,NULL,NULL,NULL,'puLimitPriorToAssign','label','Job','','en_US','job',1),
	(942,NULL,NULL,NULL,'puLimitPriorToAssign','label','Select A Job','','en_US','selectJob',1),
	(943,NULL,NULL,NULL,'puLimitPriorToAssign','label','Select A Machine','','en_US','selectMachine',1),
	(944,NULL,NULL,NULL,'puLimitPriorToPUList','label','No MPS Resources Available','','en_US','noMPSResourcesAvailable',1),
	(945,NULL,NULL,NULL,'puLimitPriorToPUList','label','Select A Machine','','en_US','selectMachine',1),
	(946,NULL,NULL,NULL,'resource','label','Barcode','','en_US','barcode',1),
	(947,NULL,NULL,NULL,'resource','error','Barcode already exists in the database','','en_US','barcode_exists',1),
	(948,NULL,NULL,NULL,'resource','label','Cancel','','en_US','cancel',1),
	(949,NULL,NULL,NULL,'resource','label','Commission Date','','en_US','commission_date',1),
	(950,NULL,NULL,NULL,'resource','error','Commission date cannot be empty','','en_US','commission_date',1),
	(951,NULL,NULL,NULL,'resource','constraint','NotEmpty','','en_US','commission_date',1),
	(952,NULL,NULL,NULL,'resource','metaposition','40','','en_US','commission_date',1),
	(953,NULL,NULL,NULL,'resource','error','Resource was NOT created. Please fill in required fields.','','en_US','created',1),
	(954,NULL,NULL,NULL,'resource','label','Resource created successfully.','','en_US','created_success',1),
	(955,NULL,NULL,NULL,'resource','label','Decommission Date','','en_US','decommission_date',1),
	(956,NULL,NULL,NULL,'resource','error','Decommission date cannot be empty','','en_US','decommission_date',1),
	(957,NULL,NULL,NULL,'resource','metaposition','50','','en_US','decommission_date',1),
	(958,NULL,NULL,NULL,'resource','label','Active','','en_US','isActive',1),
	(959,NULL,NULL,NULL,'resource','label','Resource Category','','en_US','machineType',1),
	(960,NULL,NULL,NULL,'resource','label','Name','','en_US','name',1),
	(961,NULL,NULL,NULL,'resource','error','Resource name cannot be empty','','en_US','name',1),
	(962,NULL,NULL,NULL,'resource','constraint','NotEmpty','','en_US','name',1),
	(963,NULL,NULL,NULL,'resource','label','Resource Category','','en_US','resourceCategoryId',1),
	(964,NULL,NULL,NULL,'resource','error','Must select category of resource','','en_US','resourceCategoryId',1),
	(965,NULL,NULL,NULL,'resource','constraint','NotEmpty','','en_US','resourceCategoryId',1),
	(966,NULL,NULL,NULL,'resource','label','Resource Type','','en_US','resourceTypeId',1),
	(967,NULL,NULL,NULL,'resource','error','Must select assay platform','','en_US','resourceTypeId',1),
	(968,NULL,NULL,NULL,'resource','constraint','NotEmpty','','en_US','resourceTypeId',1),
	(969,NULL,NULL,NULL,'resource','error','Resource name already exists in the database','','en_US','resource_exists',1),
	(970,NULL,NULL,NULL,'resource','label','List of Machines','','en_US','resource_list',1),
	(971,NULL,NULL,NULL,'resource','label','Save','','en_US','save',1),
	(972,NULL,NULL,NULL,'resource','error','Resource was NOT updated. Please fill in required fields.','','en_US','updated',1),
	(973,NULL,NULL,NULL,'resource','label','Update Resource Details','','en_US','updateResourceDetails',1),
	(974,NULL,NULL,NULL,'resource','label','Resource updated successfully.','','en_US','updated_success',1),
	(975,NULL,NULL,NULL,'run','label','Start','','en_US','dateRunStarted',1),
	(976,NULL,NULL,NULL,'run','label','End','','en_US','dateRunEnded',1),
	(977,NULL,NULL,NULL,'run','label','Files','','en_US','detailFiles',1),
	(978,NULL,NULL,NULL,'run','label','Resource','','en_US','detailResource',1),
	(979,NULL,NULL,NULL,'run','label','Resource Cells','','en_US','detailResourceCells',1),
	(980,NULL,NULL,NULL,'run','label','Run Cells','','en_US','detailRunLanes',1),
	(981,NULL,NULL,NULL,'run','label','Sample','','en_US','detailSample',1),
	(982,NULL,NULL,NULL,'run','label','Samples','','en_US','detailSamples',1),
	(983,NULL,NULL,NULL,'run','label','Files','','en_US','lanedetailFiles',1),
	(984,NULL,NULL,NULL,'run','label','Resource','','en_US','lanedetailResource',1),
	(985,NULL,NULL,NULL,'run','label','Sample','','en_US','lanedetailSample',1),
	(986,NULL,NULL,NULL,'run','label','Machine','','en_US','machine',1),
	(987,NULL,NULL,NULL,'run','label','Run Name','','en_US','name',1),
	(988,NULL,NULL,NULL,'run','label','Data Path','','en_US','path_to_data',1),
	(989,NULL,NULL,NULL,'run','label','PU Barcode','','en_US','platformUnitBarcode',1),
	(990,NULL,NULL,NULL,'run','label','Length','','en_US','readlength',1),
	(991,NULL,NULL,NULL,'run','label','Type','','en_US','readType',1),
	(992,NULL,NULL,NULL,'run','label','List of Runs','','en_US','run_list',1),
	(993,NULL,NULL,NULL,'run','label','Status','','en_US','statusForRun',1),
	(994,NULL,NULL,NULL,'run','label','Run updated successfully.','','en_US','updated_success',1),
	(995,NULL,NULL,NULL,'runInstance','label','Cancel','','en_US','cancel',1),
	(996,NULL,NULL,NULL,'runInstance','label','Choose A Resource','','en_US','chooseResource',1),
	(997,NULL,NULL,NULL,'runInstance','label','Run created successfully.','','en_US','created_success',1),
	(998,NULL,NULL,NULL,'runInstance','label','Create New Sequence Run','','en_US','headerCreate',1),
	(999,NULL,NULL,NULL,'runInstance','label','Update Sequence Run','','en_US','headerUpdate',1),
	(1000,NULL,NULL,NULL,'runInstance','label','Run Ended','','en_US','dateRunEnded',1),
	(1001,'2013-03-12 12:39:37','2013-03-12 12:39:37',X'C11F5A97EFAE4EC2ACFF064071B14F62','runInstance','label','Run Started (yyyy/mm/dd)','','en_US','dateRunStarted',1),
	(1002,NULL,NULL,NULL,'runInstance','constraint','NotEmpty','','en_US','dateRunStarted',1),
	(1003,NULL,NULL,NULL,'runInstance','error','Cannot be empty','','en_US','dateRunStarted',1),
	(1004,NULL,NULL,NULL,'runInstance','error','Incorrect Format','','en_US','dateRunStartedFormat',1),
	(1005,NULL,NULL,NULL,'runInstance','label','Name','','en_US','name',1),
	(1006,NULL,NULL,NULL,'runInstance','constraint','NotEmpty','','en_US','name',1),
	(1007,NULL,NULL,NULL,'runInstance','error','Name cannot be empty','','en_US','name',1),
	(1008,NULL,NULL,NULL,'runInstance','error','Run name already exists','','en_US','name_exists',1),
	(1009,NULL,NULL,NULL,'runInstance','label','Read Type','','en_US','readType',1),
	(1010,NULL,NULL,NULL,'runInstance','constraint','NotEmpty','','en_US','readType',1),
	(1011,NULL,NULL,NULL,'runInstance','error','Read Type cannot be empty','','en_US','readType',1),
	(1012,NULL,NULL,NULL,'runInstance','control','select:${readTypes}:valuePassedBack:valueVisible','','en_US','readType',1),
	(1013,NULL,NULL,NULL,'runInstance','metaposition','10','','en_US','readType',1),
	(1014,NULL,NULL,NULL,'runInstance','label','Read Length','','en_US','readlength',1),
	(1015,NULL,NULL,NULL,'runInstance','constraint','NotEmpty','','en_US','readlength',1),
	(1016,NULL,NULL,NULL,'runInstance','error','Read Length cannot be empty','','en_US','readlength',1),
	(1017,NULL,NULL,NULL,'runInstance','control','select:${readlengths}:valuePassedBack:valueVisible','','en_US','readlength',1),
	(1018,NULL,NULL,NULL,'runInstance','metaposition','15','','en_US','readlength',1),
	(1019,NULL,NULL,NULL,'runInstance','label','Reset','','en_US','reset',1),
	(1020,NULL,NULL,NULL,'runInstance','label','Submit','','en_US','submit',1),
	(1021,NULL,NULL,NULL,'runInstance','label','Technician','','en_US','technician',1),
	(1022,NULL,NULL,NULL,'runInstance','constraint','NotEmpty','','en_US','technician',1),
	(1023,NULL,NULL,NULL,'runInstance','error','Technician cannot be empty','','en_US','technician',1),
	(1024,NULL,NULL,NULL,'runInstance','label','Run updated successfully.','','en_US','updated_success',1),
	(1025,NULL,NULL,NULL,'sample','label','Adaptor','','en_US','controlLib_adaptor',1),
	(1026,NULL,NULL,NULL,'sample','label','Adaptor Set','','en_US','controlLib_adaptorSet',1),
	(1027,NULL,NULL,NULL,'sample','label','Create New Control','','en_US','controlLib_createButton',1),
	(1028,NULL,NULL,NULL,'sample','label','Control Libraries','','en_US','controlLib_controlLibraries',1),
	(1029,NULL,NULL,NULL,'sample','label','Edit','','en_US','controlLib_edit',1),
	(1030,NULL,NULL,NULL,'sample','label','Index','','en_US','controlLib_index',1),
	(1031,NULL,NULL,NULL,'sample','label','Is Active?','','en_US','controlLib_isActive',1),
	(1032,NULL,NULL,NULL,'sample','label','Control Name','','en_US','controlLib_name',1),
	(1033,NULL,NULL,NULL,'sample','label','No Control Libraries Exist','','en_US','controlLib_noneExist',1),
	(1034,NULL,NULL,NULL,'sample','label','','','en_US','detail_children',1),
	(1035,NULL,NULL,NULL,'sample','label','Facility Manager Sample To Library','','en_US','detail_facManSampleToLib',1),
	(1036,NULL,NULL,NULL,'sample','label','Files','','en_US','detail_files',1),
	(1037,NULL,NULL,NULL,'sample','label','Jobs','','en_US','detail_jobs',1),
	(1038,NULL,NULL,NULL,'sample','label','Parents','','en_US','detail_parents',1),
	(1039,NULL,NULL,NULL,'sample','label','Relations','','en_US','detail_relations',1),
	(1040,NULL,NULL,NULL,'sample','label','Received?','','en_US','receivedStatus',1),
	(1041,NULL,NULL,NULL,'sample','label','Job','','en_US','jobId',1),
	(1042,NULL,NULL,NULL,'sample','label','Sample Name','','en_US','name',1),
	(1043,NULL,NULL,NULL,'sample','error','Name cannot be null','','en_US','name',1),
	(1044,NULL,NULL,NULL,'sample','label','PI','','en_US','pi',1),
	(1045,NULL,NULL,NULL,'sample','label','Runs','','en_US','runs',1),
	(1046,NULL,NULL,NULL,'sample','label','List of Samples','','en_US','sample_list',1),
	(1047,NULL,NULL,NULL,'sample','label','Submitter','','en_US','submitter',1),
	(1048,NULL,NULL,NULL,'sample','label','Subtype','','en_US','subtype',1),
	(1049,NULL,NULL,NULL,'sample','label','Type','','en_US','type',1),
	(1050,NULL,NULL,NULL,'sample','label','Active','','en_US','updateControlLib_active',1),
	(1051,NULL,NULL,NULL,'sample','label','Please select active or inactive for this control','','en_US','updateControlLib_activeAlert',1),
	(1052,NULL,NULL,NULL,'sample','label','Adaptor Set','','en_US','updateControlLib_adaptorSet',1),
	(1053,NULL,NULL,NULL,'sample','label','Please select an adaptor','','en_US','updateControlLib_adaptorAlert',1),
	(1054,NULL,NULL,NULL,'sample','label','Please select an adaptor set','','en_US','updateControlLib_adaptorSetAlert',1),
	(1055,NULL,NULL,NULL,'sample','label','Adaptor','','en_US','updateControlLib_adaptor',1),
	(1056,NULL,NULL,NULL,'sample','label','Cancel','','en_US','updateControlLib_cancel',1),
	(1057,NULL,NULL,NULL,'sample','label','Create New Library Control','','en_US','updateControlLib_create',1),
	(1058,NULL,NULL,NULL,'sample','label','Inactive','','en_US','updateControlLib_inactive',1),
	(1059,NULL,NULL,NULL,'sample','label','Index','','en_US','updateControlLib_index',1),
	(1060,NULL,NULL,NULL,'sample','label','Is Active?','','en_US','updateControlLib_isActive',1),
	(1061,NULL,NULL,NULL,'sample','label','Control Name','','en_US','updateControlLib_name',1),
	(1062,NULL,NULL,NULL,'sample','label','Please provide a name for this control','','en_US','updateControlLib_nameAlert',1),
	(1063,NULL,NULL,NULL,'sample','label','Reset','','en_US','updateControlLib_reset',1),
	(1064,NULL,NULL,NULL,'sample','label','SELECT AN ADAPTOR','','en_US','updateControlLib_selectAdaptor',1),
	(1065,NULL,NULL,NULL,'sample','label','SELECT AN ADAPTOR SET','','en_US','updateControlLib_selectASet',1),
	(1066,NULL,NULL,NULL,'sample','label','Submit','','en_US','updateControlLib_submit',1),
	(1067,NULL,NULL,NULL,'sample','label','Update Library Control','','en_US','updateControlLib_update',1),
	(1068,NULL,NULL,NULL,'sampleDetail','error','No adaptorset matches supplied adaptorset parameter','','en_US','adaptorsetParameter',1),
	(1069,NULL,NULL,NULL,'sampleDetail','error','Job not found in the database','','en_US','jobNotFound',1),
	(1070,NULL,NULL,NULL,'sampleDetail','error','No job matches supplied job parameter','','en_US','jobParameter',1),
	(1071,NULL,NULL,NULL,'sampleDetail','error','Supplied job and sample parameters do not refer to a valid object','','en_US','jobSampleMismatch',1),
	(1072,NULL,NULL,NULL,'sampleDetail','error','Library not found in the database','','en_US','libraryNotFound',1),
	(1073,NULL,NULL,NULL,'sampleDetail','error','Name already exists in database!','','en_US','nameClash',1),
	(1074,NULL,NULL,NULL,'sampleDetail','error','Sample not found in the database','','en_US','sampleNotFound',1),
	(1075,NULL,NULL,NULL,'sampleDetail','error','No sample matches supplied sample parameter','','en_US','sampleParameter',1),
	(1076,NULL,NULL,NULL,'sampleDetail','error','Cannot find requested sample subtype in the database','','en_US','sampleSubtypeNotFound',1),
	(1077,NULL,NULL,NULL,'sampleDetail','error','Sample NOT updated. Unexpected error!','','en_US','unexpected',1),
	(1078,NULL,NULL,NULL,'sampleDetail','error','Sample NOT updated. Fill in required fields or cancel to restore.','','en_US','updated',1),
	(1079,NULL,NULL,NULL,'sampleDetail','label','Sample sucessfully updated.','','en_US','updated_success',1),
	(1080,NULL,NULL,NULL,'sampledetail_ro','label','Cancel','','en_US','cancel',1),
	(1081,NULL,NULL,NULL,'sampledetail_ro','label','Edit','','en_US','edit',1),
	(1082,NULL,NULL,NULL,'sampledetail_ro','label','Sample Name','','en_US','sampleName',1),
	(1083,NULL,NULL,NULL,'sampledetail_ro','label','Sample Type','','en_US','sampleType',1),
	(1084,NULL,NULL,NULL,'sampledetail_rw','label','Cancel','','en_US','cancel',1),
	(1085,NULL,NULL,NULL,'sampledetail_rw','label','Sample Name','','en_US','sampleName',1),
	(1086,NULL,NULL,NULL,'sampledetail_rw','label','Sample Type','','en_US','sampleType',1),
	(1087,NULL,NULL,NULL,'sampledetail_rw','label','Save','','en_US','save',1),
	(1088,NULL,NULL,NULL,'sampleDraft','error','You must provide a sample name','','en_US','name',1),
	(1089,NULL,NULL,NULL,'samplereceivetask','label','No Pending Samples','','en_US','subtitle_none',1),
	(1090,'2013-03-12 12:39:37','2013-03-12 12:39:37',X'D85EF0FAAD7146D182429CA7E2967FC9','sections','label','Home','','en_US','banner_dashboard',1),
	(1091,NULL,NULL,NULL,'sections','label','Logout','','en_US','banner_logout',1),
	(1092,NULL,NULL,NULL,'sections','label','Albert Einstein College of Medicine (2012). Distributed freely under the terms of the','','en_US','footer_albert',1),
	(1093,NULL,NULL,NULL,'sections','label','GNU AFFERO General Public License','','en_US','footer_gnu',1),
	(1094,NULL,NULL,NULL,'sections','label','Core WASP System and plugin repository maintained by Albert Einstein College of Medicine Computational Epigenomics and Genomics Cores','','en_US','footer_maintainedBy',1),
	(1095,NULL,NULL,NULL,'sections','label','waspsystem.org','','en_US','footer_waspsystem',1),
	(1096,NULL,NULL,NULL,'showPlatformUnit','label','Add','','en_US','add',1),
	(1097,NULL,NULL,NULL,'showPlatformUnit','label','Add Control','','en_US','addControl',1),
	(1098,NULL,NULL,NULL,'showPlatformUnit','label','Barcode','','en_US','barcode',1),
	(1099,NULL,NULL,NULL,'showPlatformUnit','label','Cancel','','en_US','cancel',1),
	(1100,NULL,NULL,NULL,'showPlatformUnit','label','Cell','','en_US','cell',1),
	(1101,NULL,NULL,NULL,'showPlatformUnit','label','Conc. On Cell','','en_US','concOnCell',1),
	(1102,NULL,NULL,NULL,'showPlatformUnit','label','Create New Run','','en_US','createNewRun',1),
	(1103,NULL,NULL,NULL,'showPlatformUnit','label','Current Conc. (pM)','','en_US','currentConcPM',1),
	(1104,NULL,NULL,NULL,'showPlatformUnit','label','Edit','','en_US','edit',1),
	(1105,NULL,NULL,NULL,'showPlatformUnit','label','Final Conc. (pM)','','en_US','finalConcPM',1),
	(1106,NULL,NULL,NULL,'showPlatformUnit','label','Index','','en_US','index',1),
	(1107,NULL,NULL,NULL,'showPlatformUnit','label','Job J','','en_US','jobJ',1),
	(1108,NULL,NULL,NULL,'showPlatformUnit','label','Locked','','en_US','locked',1),
	(1109,NULL,NULL,NULL,'showPlatformUnit','label','Machine','','en_US','machine',1),
	(1110,NULL,NULL,NULL,'showPlatformUnit','label','New Conc. (pM)','','en_US','newConcPM',1),
	(1111,NULL,NULL,NULL,'showPlatformUnit','label','No Control On Cell','','en_US','noControlOnCell',1),
	(1112,NULL,NULL,NULL,'showPlatformUnit','label','No Libraries On Cell','','en_US','noLibrariesOnCell',1),
	(1113,NULL,NULL,NULL,'showPlatformUnit','label','Platform Unit Name','','en_US','platformUnit',1),
	(1114,NULL,NULL,NULL,'showPlatformUnit','label','Platform Unit Status','','en_US','platformUnitStatus',1),
	(1115,NULL,NULL,NULL,'showPlatformUnit','label','Please provide a value','','en_US','pleasePorvideValue_alert',1),
	(1116,NULL,NULL,NULL,'showPlatformUnit','label','Please provide a final concentration for this control','','en_US','pleaseProvideControlConc_alert',1),
	(1117,NULL,NULL,NULL,'showPlatformUnit','label','Please provide a start date','','en_US','pleaseProvideStartDate_alert',1),
	(1118,NULL,NULL,NULL,'showPlatformUnit','label','Please provide a valid name for this run','','en_US','pleaseProvideValidName_alert',1),
	(1119,NULL,NULL,NULL,'showPlatformUnit','label','Please provide a start date in the proper format','','en_US','pleaseProvideValidStartDate_alert',1),
	(1120,NULL,NULL,NULL,'showPlatformUnit','label','Please select a control','','en_US','pleaseSelectControl_alert',1),
	(1121,NULL,NULL,NULL,'showPlatformUnit','label','Please select a machine','','en_US','pleaseSelectMachine_alert',1),
	(1122,NULL,NULL,NULL,'showPlatformUnit','label','Please select a read length','','en_US','pleaseSelectReadLength_alert',1),
	(1123,NULL,NULL,NULL,'showPlatformUnit','label','Please select a read type','','en_US','pleaseSelectReadType_alert',1),
	(1124,NULL,NULL,NULL,'showPlatformUnit','label','Please select a technician','','en_US','pleaseSelectTechnician_alert',1),
	(1125,NULL,NULL,NULL,'showPlatformUnit','label','pM','','en_US','pM',1),
	(1126,NULL,NULL,NULL,'showPlatformUnit','label','Read Length','','en_US','readLength',1),
	(1127,NULL,NULL,NULL,'showPlatformUnit','label','Read Type','','en_US','readType',1),
	(1128,NULL,NULL,NULL,'showPlatformUnit','label','Remove Control','','en_US','removeControl',1),
	(1129,NULL,NULL,NULL,'showPlatformUnit','label','Remove Control From This Cell?','','en_US','removeControlFromThisCell',1),
	(1130,NULL,NULL,NULL,'showPlatformUnit','label','Remove Library From This Cell?','','en_US','removeLibFromCell_alert',1),
	(1131,NULL,NULL,NULL,'showPlatformUnit','label','Reset','','en_US','reset',1),
	(1132,NULL,NULL,NULL,'showPlatformUnit','label','Run Name','','en_US','runName',1),
	(1133,NULL,NULL,NULL,'showPlatformUnit','label','Run Technician','','en_US','runTechnician',1),
	(1134,NULL,NULL,NULL,'showPlatformUnit','label','--SELECT A CONTROL--','','en_US','selectControl',1),
	(1135,NULL,NULL,NULL,'showPlatformUnit','label','--SELECT A MACHINE--','','en_US','selectMachine',1),
	(1136,NULL,NULL,NULL,'showPlatformUnit','label','--SELECT A TECHNICIAN--','','en_US','selectTechnician',1),
	(1137,'2013-03-12 12:39:37','2013-03-12 12:39:37',X'CA35125DF9C746B0952E5D5B00620B8C','showPlatformUnit','label','Start Date (yyyy/mm/dd)','','en_US','startDate',1),
	(1138,NULL,NULL,NULL,'showPlatformUnit','label','Submit','','en_US','submit',1),
	(1139,NULL,NULL,NULL,'showPlatformUnit','label','Submit?','','en_US','submit_alert',1),
	(1140,NULL,NULL,NULL,'showPlatformUnit','label','Type','','en_US','type',1),
	(1141,NULL,NULL,NULL,'showPlatformUnit','label','Unlocked','','en_US','unlocked',1),
	(1142,NULL,NULL,NULL,'showPlatformUnit','label','Update','','en_US','update',1),
	(1143,NULL,NULL,NULL,'showPlatformUnit','label','Once You Create This Run Record,<br />Adding User Libraries To This Platform Unit Will Be Prohibited','','en_US','warning1',1),
	(1144,NULL,NULL,NULL,'showPlatformUnit','label','Adding Additional User Libraries To This Platform Unit<br />Is Now Prohibited','','en_US','warning2',1),
	(1145,NULL,NULL,NULL,'status','label','PI Approval','','en_US','piApproval',1),
	(1146,NULL,NULL,NULL,'status','label','DA Approval','','en_US','daApproval',1),
	(1147,NULL,NULL,NULL,'status','label','Not Yet Set','','en_US','notYetSet',1),
	(1148,NULL,NULL,NULL,'status','label','Awaiting Response','','en_US','awaitingResponse',1),
	(1149,NULL,NULL,NULL,'status','label','Approved','','en_US','approved',1),
	(1150,NULL,NULL,NULL,'status','label','Rejected','','en_US','rejected',1),
	(1151,NULL,NULL,NULL,'status','label','Abandoned','','en_US','abandoned',1),
	(1152,NULL,NULL,NULL,'status','label','Unknown','','en_US','unknown',1),
	(1153,NULL,NULL,NULL,'sysrole','error','Invalid role specified','','en_US','invalidRoleSpecified',1),
	(1154,NULL,NULL,NULL,'sysrole','label','Action','','en_US','list_action',1),
	(1155,NULL,NULL,NULL,'sysrole','label','Use the form below to add system roles to existing WASP users','','en_US','list_add_role',1),
	(1156,NULL,NULL,NULL,'sysrole','label','Add System Role to User','','en_US','list_create',1),
	(1157,NULL,NULL,NULL,'sysrole','label','Current Users with System Roles','','en_US','list_current',1),
	(1158,NULL,NULL,NULL,'sysrole','label','Name (Login)','','en_US','list_name',1),
	(1159,NULL,NULL,NULL,'sysrole','label','Remove','','en_US','list_remove',1),
	(1160,NULL,NULL,NULL,'sysrole','label','Role','','en_US','list_role',1),
	(1161,NULL,NULL,NULL,'sysrole','label','Submit','','en_US','list_submit',1),
	(1162,NULL,NULL,NULL,'sysrole','label','Existing User','','en_US','list_sysuser_name',1),
	(1163,NULL,NULL,NULL,'sysrole','label','New Role','','en_US','list_sysuser_role',1),
	(1164,NULL,NULL,NULL,'sysrole','label','Unchangeable','','en_US','list_unchangeable',1),
	(1165,NULL,NULL,NULL,'sysrole','error','No role specified','','en_US','noRoleSpecified',1),
	(1166,NULL,NULL,NULL,'sysrole','error','No user specified','','en_US','noUserSpecified',1),
	(1167,NULL,NULL,NULL,'sysrole','error','Cannot remove role from user. The role must be granted to another user first.','','en_US','onlyUserWithRole',1),
	(1168,NULL,NULL,NULL,'sysrole','label','Update completed successfully','','en_US','success',1),
	(1169,NULL,NULL,NULL,'sysrole','error','The user specified does not exist in the database','','en_US','userNonexistant',1),
	(1170,NULL,NULL,NULL,'sysrole','error','Specified user already has the selected role','','en_US','userRoleExists',1),
	(1171,NULL,NULL,NULL,'sysrole','error','Specified user does not have specified role','','en_US','wrongUserRoleCombination',1),
	(1172,NULL,NULL,NULL,'task','label','Department Administration Tasks','','en_US','departmentAdmin',1),
	(1173,NULL,NULL,NULL,'task','label','If the system has determined that there are tasks requiring your attention, links will be posted below. Please click on the links and address the tasks assigned to you ASAP.','','en_US','instructions',1),
	(1174,NULL,NULL,NULL,'task','label','Job Quote Tasks','','en_US','jobQuote',1),
	(1175,NULL,NULL,NULL,'task','label','Lab Management Tasks','','en_US','labManager',1),
	(1176,NULL,NULL,NULL,'task','label','QC Result','','en_US','libraryqc_action',1),
	(1177,NULL,NULL,NULL,'task','error','Update Failed: Please provide an explanation for this failure','','en_US','libraryqc_comment_empty',1),
	(1178,NULL,NULL,NULL,'task','label','Failed','','en_US','libraryqc_failed',1),
	(1179,NULL,NULL,NULL,'task','error','Library has no record in the database','','en_US','libraryqc_invalid_sample',1),
	(1180,NULL,NULL,NULL,'task','label','JobID','','en_US','libraryqc_jobId',1),
	(1181,NULL,NULL,NULL,'task','label','Job Name','','en_US','libraryqc_jobName',1),
	(1182,NULL,NULL,NULL,'task','label','Molecule','','en_US','libraryqc_molecule',1),
	(1183,NULL,NULL,NULL,'task','label','Passed','','en_US','libraryqc_passed',1),
	(1184,NULL,NULL,NULL,'task','error','Update Failed: You must select either Passed or Failed','','en_US','libraryqc_qcStatus_invalid',1),
	(1185,NULL,NULL,NULL,'task','label','Reset','','en_US','libraryqc_reset',1),
	(1186,NULL,NULL,NULL,'task','label','Library','','en_US','libraryqc_sample',1),
	(1187,NULL,NULL,NULL,'task','error','Update Failed: Invalid Status','','en_US','libraryqc_status_invalid',1),
	(1188,NULL,NULL,NULL,'task','label','Submit','','en_US','libraryqc_submit',1),
	(1189,NULL,NULL,NULL,'task','label','Submitter','','en_US','libraryqc_submitter',1),
	(1190,NULL,NULL,NULL,'task','label','No Pending Library QC Tasks','','en_US','libraryqc_subtitle_none',1),
	(1191,NULL,NULL,NULL,'task','label','Library QC Manager','','en_US','libraryqc_title',1),
	(1192,NULL,NULL,NULL,'task','label','Update completed successfully','','en_US','libraryqc_update_success',1),
	(1193,NULL,NULL,NULL,'task','label','Please select either Library Passed or Failed','','en_US','libraryqc_validateAlert',1),
	(1194,NULL,NULL,NULL,'task','error','Update Failed: Please select Passed or Failed','','en_US','libraryqc_receivedstatus_empty',1),
	(1195,NULL,NULL,NULL,'task','error','Update Failed: Action Invalid','','en_US','libraryqc_receivedstatus_invalid',1),
	(1196,NULL,NULL,NULL,'task','error','Problem occurred updating status','','en_US','libraryqc_message',1),
	(1197,NULL,NULL,NULL,'task','label','Please provide a reason for this library failing QC','','en_US','libraryqc_validateCommentAlert',1),
	(1198,NULL,NULL,NULL,'task','label','Please select either Passed or Failed','','en_US','libraryqc_validatePassFailAlert',1),
	(1199,NULL,NULL,NULL,'task','label','There are currently no tasks requiring your attention.','','en_US','none',1),
	(1200,NULL,NULL,NULL,'task','label','Action','','en_US','samplereceive_action',1),
	(1201,NULL,NULL,NULL,'task','error','Sample has no record in the database','','en_US','samplereceive_invalid_sample',1),
	(1202,NULL,NULL,NULL,'task','label','JobID','','en_US','samplereceive_jobId',1),
	(1203,NULL,NULL,NULL,'task','label','Job Name','','en_US','samplereceive_jobName',1),
	(1204,NULL,NULL,NULL,'task','error','Problem occurred updating status','','en_US','samplereceive_message',1),
	(1205,NULL,NULL,NULL,'task','label','Molecule','','en_US','samplereceive_molecule',1),
	(1206,NULL,NULL,NULL,'task','label','Received','','en_US','samplereceive_received',1),
	(1207,NULL,NULL,NULL,'task','error','Update Failed: Please select Received or Withdrawn for at least one sample','','en_US','samplereceive_receivedstatus_empty',1),
	(1208,NULL,NULL,NULL,'task','error','Update Failed: Action Invalid','','en_US','samplereceive_receivedstatus_invalid',1),
	(1209,NULL,NULL,NULL,'task','error','Update Failed: Unexpected Error','','en_US','samplereceive_receivedstatus_unexpected',1),
	(1210,NULL,NULL,NULL,'task','label','Reset','','en_US','samplereceive_reset',1),
	(1211,NULL,NULL,NULL,'task','label','Sample','','en_US','samplereceive_sample',1),
	(1212,NULL,NULL,NULL,'task','label','--SELECT--','','en_US','samplereceive_select',1),
	(1213,NULL,NULL,NULL,'task','label','set all received','','en_US','samplereceive_setAllReceived',1),
	(1214,NULL,NULL,NULL,'task','label','set all withdrawn','','en_US','samplereceive_setAllWithdrawn',1),
	(1215,NULL,NULL,NULL,'task','label','Submit','','en_US','samplereceive_submit',1),
	(1216,NULL,NULL,NULL,'task','label','Submitter','','en_US','samplereceive_submitter',1),
	(1217,NULL,NULL,NULL,'task','label','Sample Receiver Manager','','en_US','samplereceive_title',1),
	(1218,NULL,NULL,NULL,'task','label','Update completed successfully','','en_US','samplereceive_update_success',1),
	(1219,NULL,NULL,NULL,'task','label','Please select either Sample Received or Withdrawn for at least one sample','','en_US','samplereceive_validateAlert',1),
	(1220,NULL,NULL,NULL,'task','label','Withdrawn','','en_US','samplereceive_withdrawn',1),
	(1221,NULL,NULL,NULL,'task','label','QC Result','','en_US','sampleqc_action',1),
	(1222,NULL,NULL,NULL,'task','error','Update Failed: Please provide an explanation for this failure','','en_US','sampleqc_comment_empty',1),
	(1223,NULL,NULL,NULL,'task','label','Failed','','en_US','sampleqc_failed',1),
	(1224,NULL,NULL,NULL,'task','error','Record not found in database','','en_US','sampleqc_invalid_sample',1),
	(1225,NULL,NULL,NULL,'task','label','JobID','','en_US','sampleqc_jobId',1),
	(1226,NULL,NULL,NULL,'task','label','Job Name','','en_US','sampleqc_jobName',1),
	(1227,NULL,NULL,NULL,'task','error','Problem occurred updating status','','en_US','sampleqc_message',1),
	(1228,NULL,NULL,NULL,'task','label','Molecule','','en_US','sampleqc_molecule',1),
	(1229,NULL,NULL,NULL,'task','label','Passed','','en_US','sampleqc_passed',1),
	(1230,NULL,NULL,NULL,'task','error','Update Failed: You must select either Passed or Failed','','en_US','sampleqc_qcStatus_invalid',1),
	(1231,NULL,NULL,NULL,'task','label','Reset','','en_US','sampleqc_reset',1),
	(1232,NULL,NULL,NULL,'task','label','Sample','','en_US','sampleqc_sample',1),
	(1233,NULL,NULL,NULL,'task','error','Update Failed: Invalid Status','','en_US','sampleqc_status_invalid',1),
	(1234,NULL,NULL,NULL,'task','label','Submit','','en_US','sampleqc_submit',1),
	(1235,NULL,NULL,NULL,'task','label','Submitter','','en_US','sampleqc_submitter',1),
	(1236,NULL,NULL,NULL,'task','label','No Pending Sample QC Tasks','','en_US','sampleqc_subtitle_none',1),
	(1237,NULL,NULL,NULL,'task','label','Sample QC Manager','','en_US','sampleqc_title',1),
	(1238,NULL,NULL,NULL,'task','label','Update completed successfully','','en_US','sampleqc_update_success',1),
	(1239,NULL,NULL,NULL,'task','error','Update Failed: Please select Passed or Failed','','en_US','sampleqc_receivedstatus_empty',1),
	(1240,NULL,NULL,NULL,'task','error','Update Failed: Action Invalid','','en_US','sampleqc_receivedstatus_invalid',1),
	(1241,NULL,NULL,NULL,'task','label','Please provide a reason for this sample failing QC','','en_US','sampleqc_validateCommentAlert',1),
	(1242,NULL,NULL,NULL,'task','label','Please select either Passed or Failed','','en_US','sampleqc_validatePassFailAlert',1),
	(1243,NULL,NULL,NULL,'uiField','label','Field Added','','en_US','added',1),
	(1244,NULL,NULL,NULL,'uiField','label','Area','','en_US','area',1),
	(1245,NULL,NULL,NULL,'uiField','label','Attribute Name','','en_US','attrName',1),
	(1246,NULL,NULL,NULL,'uiField','suffix','<font color=\"blue\"> see footnote<sup>1</sup> </font>','','en_US','attrName',1),
	(1247,NULL,NULL,NULL,'uiField','label','Attribute Value','','en_US','attrValue',1),
	(1248,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'28E3C94D333E4EFD8C45511F2CBD0219','uiField','label','Preferred Language','','en_US','locale',1),
	(1249,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'512761860A82477892D004A3F8591DBA','uiField','error','Language not specified','','en_US','locale',1),
	(1250,NULL,NULL,NULL,'uiField','label','Field Name','','en_US','name',1),
	(1251,NULL,NULL,NULL,'uiField','error','Property already exists','','en_US','not_unique',1),
	(1252,NULL,NULL,NULL,'uiField','data','Field Deleted','','en_US','removed',1),
	(1253,NULL,NULL,NULL,'uiField','data','Attribute Updated','','en_US','updated',1),
	(1254,NULL,NULL,NULL,'uiField','label','Field Updated','','en_US','updated',1),
	(1255,NULL,NULL,NULL,'user','label','Address','','en_US','address',1),
	(1256,NULL,NULL,NULL,'user','metaposition','40','','en_US','address',1),
	(1257,NULL,NULL,NULL,'user','error','Login Failure. Please provide valid login credentials.','','en_US','auth_login_validate',1),
	(1258,NULL,NULL,NULL,'user','label','Room','','en_US','building_room',1),
	(1259,NULL,NULL,NULL,'user','metaposition','30','','en_US','building_room',1),
	(1260,NULL,NULL,NULL,'user','error','Room cannot be empty','','en_US','building_room',1),
	(1261,NULL,NULL,NULL,'user','constraint','NotEmpty','','en_US','building_room',1),
	(1262,NULL,NULL,NULL,'user','label','City','','en_US','city',1),
	(1263,NULL,NULL,NULL,'user','error','City cannot be empty','','en_US','city',1),
	(1264,NULL,NULL,NULL,'user','constraint','NotEmpty','','en_US','city',1),
	(1265,NULL,NULL,NULL,'user','metaposition','50','','en_US','city',1),
	(1266,NULL,NULL,NULL,'user','label','Country','','en_US','country',1),
	(1267,NULL,NULL,NULL,'user','error','Country cannot be empty','','en_US','country',1),
	(1268,NULL,NULL,NULL,'user','control','select:${countries}:code:name','','en_US','country',1),
	(1269,NULL,NULL,NULL,'user','constraint','NotEmpty','','en_US','country',1),
	(1270,NULL,NULL,NULL,'user','metaposition','70','','en_US','country',1),
	(1271,NULL,NULL,NULL,'user','error','User was NOT created. Please fill in required fields.','','en_US','created',1),
	(1272,NULL,NULL,NULL,'user','label','User was created. Consider assigning a role to this new user.','','en_US','created_success',1),
	(1273,NULL,NULL,NULL,'user','control','select:${departments}:departmentId:name','','en_US','departmentId',1),
	(1274,NULL,NULL,NULL,'user','constraint','NotEmpty','','en_US','departmentId',1),
	(1275,NULL,NULL,NULL,'user','error','A department must be selected','','en_US','departmentId',1),
	(1276,NULL,NULL,NULL,'user','label','Department','','en_US','departmentId',1),
	(1277,NULL,NULL,NULL,'user','metaposition','20','','en_US','departmentId',1),
	(1278,NULL,NULL,NULL,'user','label','Email','','en_US','email',1),
	(1279,NULL,NULL,NULL,'user','constraint','NotEmpty','','en_US','email',1),
	(1280,NULL,NULL,NULL,'user','error','Wrong email address format','','en_US','email',1),
	(1281,NULL,NULL,NULL,'user','label','Your email address has changed. An email has been sent to your new email address requesting confirmation. Please confirm by clicking the link in the email then <a href=\"../login.do\"/>click here to login</a>','','en_US','email_changed_p1',1),
	(1282,NULL,NULL,NULL,'user','label','If you have not received an email, requesting confirmation, within a reasonable time period and suspect your email address may have been mis-typed, you may reset your email address by clicking <a href=\"requestEmailChange.do\">here</a>','','en_US','email_changed_p2',1),
	(1283,NULL,NULL,NULL,'user','label','Your email address change has been confirmed successfully.','','en_US','email_change_confirmed',1),
	(1284,NULL,NULL,NULL,'user','error','Email already exists in the database','','en_US','email_exists',1),
	(1285,NULL,NULL,NULL,'user','label','Fax','','en_US','fax',1),
	(1286,NULL,NULL,NULL,'user','metaposition','100','','en_US','fax',1),
	(1287,NULL,NULL,NULL,'user','label','First Name','','en_US','firstName',1),
	(1288,NULL,NULL,NULL,'user','constraint','NotEmpty','','en_US','firstName',1),
	(1289,NULL,NULL,NULL,'user','error','First Name cannot be empty','','en_US','firstName',1),
	(1290,NULL,NULL,NULL,'user','label','Institution','','en_US','institution',1),
	(1291,NULL,NULL,NULL,'user','error','Institution cannot be empty','','en_US','institution',1),
	(1292,NULL,NULL,NULL,'user','constraint','NotEmpty','','en_US','institution',1),
	(1293,NULL,NULL,NULL,'user','metaposition','10','','en_US','institution',1),
	(1294,NULL,NULL,NULL,'user','label','Active','','en_US','isActive',1),
	(1295,NULL,NULL,NULL,'user','label','Jobs','','en_US','jobs',1),
	(1296,NULL,NULL,NULL,'user','label','View jobs belonging to user in these labs ','','en_US','job_list',1),
	(1297,NULL,NULL,NULL,'user','label','Lab Users','','en_US','labusers',1),
	(1298,NULL,NULL,NULL,'user','label','Last Name','','en_US','lastName',1),
	(1299,NULL,NULL,NULL,'user','constraint','NotEmpty','','en_US','lastName',1),
	(1300,NULL,NULL,NULL,'user','error','Last Name cannot be empty','','en_US','lastName',1),
	(1301,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'33D347271A084CC48F947851B9C16AE3','user','label','Preferred Language','','en_US','locale',1),
	(1302,NULL,NULL,NULL,'user','constraint','NotEmpty','','en_US','locale',1),
	(1303,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'4297730DECD640D8A5D8224448BF05CB','user','error','Language cannot be empty','','en_US','locale',1),
	(1304,NULL,NULL,NULL,'user','label','Login','','en_US','login',1),
	(1305,NULL,NULL,NULL,'user','constraint','NotEmpty','','en_US','login',1),
	(1306,NULL,NULL,NULL,'user','error','Login cannot be empty','','en_US','login',1),
	(1307,NULL,NULL,NULL,'user','error','Login name already exists in the database','','en_US','login_exists',1),
	(1308,NULL,NULL,NULL,'user','data','Submit','','en_US','mypassword',1),
	(1309,NULL,NULL,NULL,'user','error','Externally authenticated user cannot change password in WASP','','en_US','mypassword_cannotChange',1),
	(1310,NULL,NULL,NULL,'user','error','Your old password does NOT match the password in our database','','en_US','mypassword_cur_mismatch',1),
	(1311,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'A43C62B4BF3A4E86B11DFBAD8FC9D7B4','user','label','New Password Requirements:<br />At least 8 characters in length<br />Only letters & numbers permitted (no spaces, no underscore, etc.)<br />At least one letter and one number<br />','','en_US','mypassword_instructions',1),
	(1312,NULL,NULL,NULL,'user','error','Please provide values for all fields','','en_US','mypassword_missingparam',1),
	(1313,NULL,NULL,NULL,'user','label','New Password','','en_US','mypassword_newpassword1',1),
	(1314,NULL,NULL,NULL,'user','label','Confirm New Password','','en_US','mypassword_newpassword2',1),
	(1315,NULL,NULL,NULL,'user','error','New password must be at least 8 characters, containing only letters and numbers, with at least one letter and number','','en_US','mypassword_new_invalid',1),
	(1316,NULL,NULL,NULL,'user','error','The two entries for your NEW password are NOT identical','','en_US','mypassword_new_mismatch',1),
	(1317,NULL,NULL,NULL,'user','error','Your old and new passwords may NOT be the same','','en_US','mypassword_nodiff',1),
	(1318,NULL,NULL,NULL,'user','label','Password Has Been Changed','','en_US','mypassword_ok',1),
	(1319,NULL,NULL,NULL,'user','label','Old Password','','en_US','mypassword_oldpassword',1),
	(1320,NULL,NULL,NULL,'user','label','Submit','','en_US','mypassword_submit',1),
	(1321,NULL,NULL,NULL,'user','label','Password','','en_US','password',1),
	(1322,NULL,NULL,NULL,'user','constraint','NotEmpty','','en_US','password',1),
	(1323,NULL,NULL,NULL,'user','error','Password cannot be empty','','en_US','password',1),
	(1324,NULL,NULL,NULL,'user','label','Phone','','en_US','phone',1),
	(1325,NULL,NULL,NULL,'user','error','Phone cannot be empty','','en_US','phone',1),
	(1326,NULL,NULL,NULL,'user','constraint','NotEmpty','','en_US','phone',1),
	(1327,NULL,NULL,NULL,'user','metaposition','90','','en_US','phone',1),
	(1328,NULL,NULL,NULL,'user','label','Roles','','en_US','roles',1),
	(1329,NULL,NULL,NULL,'user','label','Samples','','en_US','samples',1),
	(1330,NULL,NULL,NULL,'user','label','State','','en_US','state',1),
	(1331,NULL,NULL,NULL,'user','error','State cannot be empty','','en_US','state',1),
	(1332,NULL,NULL,NULL,'user','control','select:${states}:code:name','','en_US','state',1),
	(1333,NULL,NULL,NULL,'user','constraint','NotEmpty','','en_US','state',1),
	(1334,NULL,NULL,NULL,'user','metaposition','60','','en_US','state',1),
	(1335,NULL,NULL,NULL,'user','label','Title','','en_US','title',1),
	(1336,NULL,NULL,NULL,'user','error','Title cannot be empty','','en_US','title',1),
	(1337,NULL,NULL,NULL,'user','constraint','NotEmpty','','en_US','title',1),
	(1338,NULL,NULL,NULL,'user','control','select:Prof:Prof;Dr:Dr;Mr:Mr;Ms:Ms;Mrs:Mrs;Miss:Miss','','en_US','title',1),
	(1339,NULL,NULL,NULL,'user','metaposition','1','','en_US','title',1),
	(1340,NULL,NULL,NULL,'user','error','User was NOT updated. Please fill in required fields.','','en_US','updated',1),
	(1341,NULL,NULL,NULL,'user','error','User was NOT updated.','','en_US','updated_fail',1),
	(1342,NULL,NULL,NULL,'user','label','User was updated','','en_US','updated_success',1),
	(1343,NULL,NULL,NULL,'user','label','Your login and email address has changed. An email has been sent to your new email address requesting confirmation. Please confirm by clicking the link in the email then <a href=\"../login.do\"/>click here to login</a>','','en_US','userloginandemail_changed_p1',1),
	(1344,NULL,NULL,NULL,'user','label','If you have not received an email, requesting confirmation, within a reasonable time period and suspect your email address may have been mis-typed, you may reset your email address by clicking <a href=\"requestEmailChange.do\">here</a>','','en_US','userloginandemail_changed_p2',1),
	(1345,NULL,NULL,NULL,'user','label','Your login has changed. Please <a href=\"../login.do\"/>click here to login</a>','','en_US','userlogin_changed_p1',1),
	(1346,NULL,NULL,NULL,'user','label','List of Users','','en_US','user_list',1),
	(1347,NULL,NULL,NULL,'user','label','Lab Members','','en_US','lab_member_list',1),
	(1348,NULL,NULL,NULL,'user','label','Zip','','en_US','zip',1),
	(1349,NULL,NULL,NULL,'user','error','Zip cannot be empty','','en_US','zip',1),
	(1350,NULL,NULL,NULL,'user','metaposition','80','','en_US','zip',1),
	(1351,NULL,NULL,NULL,'userDetail','label','Cancel','','en_US','cancel',1),
	(1352,NULL,NULL,NULL,'userDetail','label','Change Password','','en_US','change_password',1),
	(1353,NULL,NULL,NULL,'userDetail','label','Edit','','en_US','edit',1),
	(1354,NULL,NULL,NULL,'userDetail','label','Edit (as other)','','en_US','edit_as_other',1),
	(1355,NULL,NULL,NULL,'userDetail','label','Jobs','','en_US','jobs',1),
	(1356,NULL,NULL,NULL,'userDetail','label','Lab Users','','en_US','lab_users',1),
	(1357,NULL,NULL,NULL,'userDetail','label','Samples','','en_US','samples',1),
	(1358,NULL,NULL,NULL,'userDetail','label','Save Changes','','en_US','save',1),
	(1359,NULL,NULL,NULL,'userPending','error','Invalid action. Must be approve or reject only','','en_US','action',1),
	(1360,NULL,NULL,NULL,'userPending','label','APPROVE','','en_US','action_approve',1),
	(1361,NULL,NULL,NULL,'userPending','label','REJECT','','en_US','action_reject',1),
	(1362,NULL,NULL,NULL,'userPending','label','Street','','en_US','address',1),
	(1363,NULL,NULL,NULL,'userPending','metaposition','40','','en_US','address',1),
	(1364,NULL,NULL,NULL,'userPending','label','User account application successfully approved','','en_US','approved',1),
	(1365,NULL,NULL,NULL,'userPending','constraint','NotEmpty','','en_US','building_room',1),
	(1366,NULL,NULL,NULL,'userPending','error','Room cannot be empty','','en_US','building_room',1),
	(1367,NULL,NULL,NULL,'userPending','label','Building / Room','','en_US','building_room',1),
	(1368,NULL,NULL,NULL,'userPending','metaposition','30','','en_US','building_room',1),
	(1369,NULL,NULL,NULL,'userPending','error','Captcha text incorrect','','en_US','captcha',1),
	(1370,NULL,NULL,NULL,'userPending','label','Captcha text','','en_US','captcha',1),
	(1371,'2013-03-12 12:39:39','2013-03-12 12:39:39',X'01134DDD77FF47F091E355E562D27009','userPending','label','Your Email Address','','en_US','email',1),
	(1372,NULL,NULL,NULL,'userPending','error','Must be correctly formatted','','en_US','email',1),
	(1373,'2013-03-12 12:39:39','2013-03-12 12:39:39',X'820B8FC5A17B495B94AAFCE7A2C9AD6B','userPending','label','Your email address is confirmed. The Principal Investigator of the lab to which you applied has been requested (via email) to approve your application to join their lab. Once approved, you will be informed via email and you will be permitted to log in. If your account is not activated in good time, please contact the PI.','','en_US','emailconfirmed',1),
	(1374,'2013-03-12 12:39:39','2013-03-12 12:39:39',X'A268BDC5CFCC447E83E02AE0EB5B9A9C','userPending','label','Thank you for applying for a new user account. You have been sent an email with instructions describing how to quickly confirm your email address. Your registration cannot proceed until your email address has been confirmed.','','en_US','emailsent',1),
	(1375,NULL,NULL,NULL,'userPending','error','Email already exists in the database','','en_US','email_exists',1),
	(1376,NULL,NULL,NULL,'userPending','error','Authentication Failed (Login Name or Password incorrect)','','en_US','external_authentication',1),
	(1377,NULL,NULL,NULL,'userPending','label','Fax','','en_US','fax',1),
	(1378,NULL,NULL,NULL,'userPending','metaposition','100','','en_US','fax',1),
	(1379,NULL,NULL,NULL,'userPending','label','First Name','','en_US','firstName',1),
	(1380,NULL,NULL,NULL,'userPending','error','First Name cannot be empty','','en_US','firstName',1),
	(1381,'2013-03-12 12:39:39','2013-03-12 12:39:39',X'1C42B9EE1715474E97B232121FA40901','userPending','label','If you do NOT already have a WASP account AND you wish to register and join an existing lab, then complete this form and provide the email address of the Principal Investigator (PI) whose lab you wish to join.<br /><br />PLEASE DO NOT USE THIS FORM TO REGISTER AS A NEW PRINCIPAL INVESTIGATOR!','','en_US','form_instructions',1),
	(1382,NULL,NULL,NULL,'userPending','error','Lab id mismatch with user-pending id','','en_US','labid_mismatch',1),
	(1383,NULL,NULL,NULL,'userPending','label','Last Name','','en_US','lastName',1),
	(1384,NULL,NULL,NULL,'userPending','error','Last Name cannot be empty','','en_US','lastName',1),
	(1385,NULL,NULL,NULL,'userPending','constraint','NotEmpty','','en_US','locale',1),
	(1386,'2013-03-12 12:39:39','2013-03-12 12:39:39',X'C64279AC930B43E386316B1163F61325','userPending','label','Preferred Language','','en_US','locale',1),
	(1387,'2013-03-12 12:39:39','2013-03-12 12:39:39',X'80AC6490D9244144987C19F69349B096','userPending','error','A language must be selected','','en_US','locale',1),
	(1388,NULL,NULL,NULL,'userPending','label','Login','','en_US','login',1),
	(1389,NULL,NULL,NULL,'userPending','error','Login cannot be empty','','en_US','login',1),
	(1390,NULL,NULL,NULL,'userPending','error','Login name already exists in the database','','en_US','login_exists',1),
	(1391,NULL,NULL,NULL,'userPending','error','Contains invalid characters','','en_US','login_malformed',1),
	(1392,NULL,NULL,NULL,'userPending','label','There are currently no pending users awaiting approval','','en_US','no_pending_users',1),
	(1393,NULL,NULL,NULL,'userPending','label','New User','','en_US','page',1),
	(1394,NULL,NULL,NULL,'userPending','label','Password','','en_US','password',1),
	(1395,NULL,NULL,NULL,'userPending','error','Password cannot be empty','','en_US','password',1),
	(1396,NULL,NULL,NULL,'userPending','label','Re-confirm Password','','en_US','password2',1),
	(1397,NULL,NULL,NULL,'userPending','error','Re-confirm password cannot be empty','','en_US','password2',1),
	(1398,NULL,NULL,NULL,'userPending','error','Password must be at least 8 characters, containing only letters and numbers, with at least one letter and number','','en_US','password_invalid',1),
	(1399,NULL,NULL,NULL,'userPending','error','The two entries for your password are NOT identical','','en_US','password_mismatch',1),
	(1400,NULL,NULL,NULL,'userPending','label','Phone','','en_US','phone',1),
	(1401,NULL,NULL,NULL,'userPending','error','Phone cannot be empty','','en_US','phone',1),
	(1402,NULL,NULL,NULL,'userPending','constraint','NotEmpty','','en_US','phone',1),
	(1403,NULL,NULL,NULL,'userPending','metaposition','90','','en_US','phone',1),
	(1404,'2013-03-12 12:39:39','2013-03-12 12:39:39',X'52FD28D1C3F54BBAABB776B731B8645E','userPending','label','Email Address of PI Whose<br />Lab You Wish To Join','','en_US','primaryuserid',1),
	(1405,'2013-03-12 12:39:39','2013-03-12 12:39:39',X'100ACBBA453E4E7B86A1D50379147192','userPending','error','Email Address of the Lab PI cannot be empty','','en_US','primaryuserid',1),
	(1406,NULL,NULL,NULL,'userPending','constraint','isValidPiId','','en_US','primaryuserid',1),
	(1407,'2013-03-12 12:39:39','2013-03-12 12:39:39',X'624C700A5A4C404DB5595F1586BC86FA','userPending','metaposition','500','','en_US','primaryuserid',1),
	(1408,'2013-03-12 12:39:39','2013-03-12 12:39:39',X'A1AB3DE538094CBC9120D80CC5390928','userPending','error','Not an active Email Address of a Lab PI','','en_US','primaryuserid_notvalid',1),
	(1409,NULL,NULL,NULL,'userPending','label','User account application successfully rejected','','en_US','rejected',1),
	(1410,NULL,NULL,NULL,'userPending','error','Pending user is already approved or rejected','','en_US','status_not_pending',1),
	(1411,NULL,NULL,NULL,'userPending','label','Apply for Account','','en_US','submit',1),
	(1412,NULL,NULL,NULL,'userPending','label','Title','','en_US','title',1),
	(1413,NULL,NULL,NULL,'userPending','error','Title cannot be empty','','en_US','title',1),
	(1414,NULL,NULL,NULL,'userPending','constraint','NotEmpty','','en_US','title',1),
	(1415,NULL,NULL,NULL,'userPending','control','select:Prof:Prof;Dr:Dr;Mr:Mr;Ms:Ms;Mrs:Mrs;Miss:Miss','','en_US','title',1),
	(1416,NULL,NULL,NULL,'userPending','metaposition','5','','en_US','title',1),
	(1417,NULL,NULL,NULL,'wasp','label','WASP','','en_US','authentication',1),
	(1418,NULL,NULL,NULL,'wasp','label','-- select --','','en_US','default_select',1),
	(1419,NULL,NULL,NULL,'wasp','error','Unexpected error. Last command was NOT executed.','','en_US','unexpected_error',1),
	(1420,NULL,NULL,NULL,'wasp','error','Parameter was unexpectedly not an integer.','','en_US','parseint_error',1),
	(1421,NULL,NULL,NULL,'wasp','error','Insufficient privileges.','','en_US','permission_error',1),
	(1422,NULL,NULL,NULL,'wasp','error','Failed to send update message','','en_US','integration_message_send',1),
	(1423,NULL,NULL,NULL,'workflow','label','Cancel','','en_US','cancel',1),
	(1424,NULL,NULL,NULL,'workflow','label','Resources and Software','','en_US','configure',1),
	(1425,NULL,NULL,NULL,'workflow','label','Is Active?','','en_US','isActive',1),
	(1426,NULL,NULL,NULL,'workflow','label','List of Workflows','','en_US','listname',1),
	(1427,NULL,NULL,NULL,'workflow','error','At least one option must be checked for each resource or software type','','en_US','missing_resource_type',1),
	(1428,NULL,NULL,NULL,'workflow','label','Workflow Name','','en_US','name',1),
	(1429,NULL,NULL,NULL,'workflow','error','At least one option must be checked for each parameter presented for a selected resource','','en_US','non-configured_parameter',1),
	(1430,NULL,NULL,NULL,'workflow','label','Save Choices','','en_US','submit',1),
	(1431,NULL,NULL,NULL,'workflow','label','Workflow Id','','en_US','workflowId',1),
	(1432,NULL,NULL,NULL,'contentTypeMap','data','application/octet-stream','','en_US','arc',1),
	(1433,NULL,NULL,NULL,'contentTypeMap','data','application/octet-stream','','en_US','bin',1),
	(1434,NULL,NULL,NULL,'contentTypeMap','data','application/octet-stream','','en_US','dump',1),
	(1435,NULL,NULL,NULL,'contentTypeMap','data','application/x-dvi','','en_US','dvi',1),
	(1436,NULL,NULL,NULL,'contentTypeMap','data','text/x-setext','','en_US','etx',1),
	(1437,NULL,NULL,NULL,'contentTypeMap','data','application/octet-stream','','en_US','exe',1),
	(1438,NULL,NULL,NULL,'contentTypeMap','data','image/vnd.fpx','','en_US','fpix',1),
	(1439,NULL,NULL,NULL,'contentTypeMap','data','image/vnd.fpx','','en_US','fpx',1),
	(1440,NULL,NULL,NULL,'contentTypeMap','data','application/octet-stream','','en_US','gz',1),
	(1441,NULL,NULL,NULL,'contentTypeMap','data','application/octet-stream','','en_US','hqx',1),
	(1442,NULL,NULL,NULL,'contentTypeMap','data','image/ief','','en_US','ief',1),
	(1443,NULL,NULL,NULL,'contentTypeMap','data','application/x-latex','','en_US','latex',1),
	(1444,NULL,NULL,NULL,'contentTypeMap','data','application/octet-stream','','en_US','lib',1),
	(1445,NULL,NULL,NULL,'contentTypeMap','data','application/x-troff-man','','en_US','man',1),
	(1446,NULL,NULL,NULL,'contentTypeMap','data','application/x-troff-me','','en_US','me',1),
	(1447,NULL,NULL,NULL,'contentTypeMap','data','message/rfc822','','en_US','mime',1),
	(1448,NULL,NULL,NULL,'contentTypeMap','data','video/quicktime','','en_US','mov',1),
	(1449,NULL,NULL,NULL,'contentTypeMap','data','video/x-sgi-movie','','en_US','movie',1),
	(1450,NULL,NULL,NULL,'contentTypeMap','data','application/x-troff-ms','','en_US','ms',1),
	(1451,NULL,NULL,NULL,'contentTypeMap','data','video/x-sgi-movie','','en_US','mv',1),
	(1452,NULL,NULL,NULL,'contentTypeMap','data','application/octet-stream','','en_US','obj',1),
	(1453,NULL,NULL,NULL,'contentTypeMap','data','application/oda','','en_US','oda',1),
	(1454,NULL,NULL,NULL,'contentTypeMap','data','image/x-portable-bitmap','','en_US','pbm',1),
	(1455,NULL,NULL,NULL,'contentTypeMap','data','application/pdf','','en_US','pdf',1),
	(1456,NULL,NULL,NULL,'contentTypeMap','data','image/x-portable-graymap','','en_US','pgm',1),
	(1457,NULL,NULL,NULL,'contentTypeMap','data','image/x-portable-anymap','','en_US','pnm',1),
	(1458,NULL,NULL,NULL,'contentTypeMap','data','image/x-portable-pixmap','','en_US','ppm',1),
	(1459,NULL,NULL,NULL,'contentTypeMap','data','video/quicktime','','en_US','qt',1),
	(1460,NULL,NULL,NULL,'contentTypeMap','data','image/x-cmu-rast','','en_US','ras',1),
	(1461,NULL,NULL,NULL,'contentTypeMap','data','image/x-rgb','','en_US','rgb',1),
	(1462,NULL,NULL,NULL,'contentTypeMap','data','application/x-troff','','en_US','roff',1),
	(1463,NULL,NULL,NULL,'contentTypeMap','data','application/octet-stream','','en_US','saveme',1),
	(1464,NULL,NULL,NULL,'contentTypeMap','data','application/x-wais-source','','en_US','src',1),
	(1465,NULL,NULL,NULL,'contentTypeMap','data','application/x-troff','','en_US','t',1),
	(1466,NULL,NULL,NULL,'contentTypeMap','data','application/x-tex','','en_US','tex',1),
	(1467,NULL,NULL,NULL,'contentTypeMap','data','application/x-texinfo','','en_US','texi',1),
	(1468,NULL,NULL,NULL,'contentTypeMap','data','application/x-texinfo','','en_US','texinfo',1),
	(1469,NULL,NULL,NULL,'contentTypeMap','data','application/x-troff','','en_US','tr',1),
	(1470,NULL,NULL,NULL,'contentTypeMap','data','text/tab-separated-values','','en_US','tsv',1),
	(1471,NULL,NULL,NULL,'contentTypeMap','data','application/x-wais-source','','en_US','wsrc',1),
	(1472,NULL,NULL,NULL,'contentTypeMap','data','image/x-xbitmap','','en_US','xbm',1),
	(1473,NULL,NULL,NULL,'contentTypeMap','data','application/xml','','en_US','xml',1),
	(1474,NULL,NULL,NULL,'contentTypeMap','data','image/x-xbitmap','','en_US','xpm',1),
	(1475,NULL,NULL,NULL,'contentTypeMap','data','image/x-xwindowdump','','en_US','xwd',1),
	(1476,NULL,NULL,NULL,'contentTypeMap','data','application/octet-stream','','en_US','zip',1),
	(1477,NULL,NULL,NULL,'wasp','data','FALSE','','en_US','isAuthenticationExternal',1),
	(1478,NULL,NULL,NULL,'pageTitle','label','Generic DNA Seq Plugin Description','','en_US','genericDnaSeq/description',1),
	(1479,NULL,NULL,NULL,'genericDnaSeq','label','If you can read this text your plugin is successfully installed','','en_US','maintext',1),
	(1480,NULL,'2012-12-20 11:03:29',NULL,'bowtieAligner','constraint','NotEmpty',NULL,'en_US','mismatches',0),
	(1481,NULL,'2012-12-20 11:03:29',NULL,'bowtieAligner','metaposition','10',NULL,'en_US','mismatches',0),
	(1482,NULL,'2012-12-20 11:03:29',NULL,'bowtieAligner','type','INTEGER',NULL,'en_US','mismatches',0),
	(1483,NULL,'2012-12-20 11:03:29',NULL,'bowtieAligner','range','0:3',NULL,'en_US','mismatches',0),
	(1484,NULL,'2012-12-20 11:03:29',NULL,'bowtieAligner','default','2',NULL,'en_US','mismatches',0),
	(1485,NULL,'2012-12-20 11:03:29',NULL,'bowtieAligner','label','Number of mismatches (0-3)',NULL,'en_US','mismatches',0),
	(1486,NULL,'2012-12-20 11:03:29',NULL,'bowtieAligner','error','A value for number of mismatches must be specified',NULL,'en_US','mismatches',0),
	(1487,NULL,'2012-12-20 11:03:29',NULL,'bowtieAligner','constraint','NotEmpty',NULL,'en_US','seedLength',0),
	(1488,NULL,'2012-12-20 11:03:29',NULL,'bowtieAligner','metaposition','20',NULL,'en_US','seedLength',0),
	(1489,NULL,'2012-12-20 11:03:29',NULL,'bowtieAligner','type','NUMBER',NULL,'en_US','seedLength',0),
	(1490,NULL,'2012-12-20 11:03:29',NULL,'bowtieAligner','range','5:1000',NULL,'en_US','seedLength',0),
	(1491,'2013-03-12 12:39:44','2013-03-12 12:39:44',X'0B948F58241D4F8B95A1FACC5E8174E4','bowtieAligner','default','28',NULL,'en_US','seedLength',0),
	(1492,NULL,'2012-12-20 11:03:29',NULL,'bowtieAligner','label','Seed Length',NULL,'en_US','seedLength',0),
	(1493,NULL,'2012-12-20 11:03:29',NULL,'bowtieAligner','error','A value for seed length must be specified',NULL,'en_US','seedLength',0),
	(1494,NULL,'2012-12-20 11:03:29',NULL,'bowtieAligner','constraint','NotEmpty',NULL,'en_US','reportAlignmentNum',0),
	(1495,NULL,'2012-12-20 11:03:29',NULL,'bowtieAligner','metaposition','30',NULL,'en_US','reportAlignmentNum',0),
	(1496,NULL,'2012-12-20 11:03:29',NULL,'bowtieAligner','type','INTEGER',NULL,'en_US','reportAlignmentNum',0),
	(1497,NULL,'2012-12-20 11:03:29',NULL,'bowtieAligner','default','1',NULL,'en_US','reportAlignmentNum',0),
	(1498,NULL,'2012-12-20 11:03:29',NULL,'bowtieAligner','label','Number of Alignments to Report',NULL,'en_US','reportAlignmentNum',0),
	(1499,NULL,'2012-12-20 11:03:29',NULL,'bowtieAligner','error','A value for number of alignments must be specified',NULL,'en_US','reportAlignmentNum',0),
	(1500,NULL,'2012-12-20 11:03:29',NULL,'bowtieAligner','constraint','NotEmpty',NULL,'en_US','discardThreshold',0),
	(1501,NULL,'2012-12-20 11:03:29',NULL,'bowtieAligner','metaposition','40',NULL,'en_US','discardThreshold',0),
	(1502,NULL,'2012-12-20 11:03:29',NULL,'bowtieAligner','type','INTEGER',NULL,'en_US','discardThreshold',0),
	(1503,NULL,'2012-12-20 11:03:29',NULL,'bowtieAligner','default','1',NULL,'en_US','discardThreshold',0),
	(1504,NULL,'2012-12-20 11:03:30',NULL,'bowtieAligner','label','Discard if more than how many alignments?',NULL,'en_US','discardThreshold',0),
	(1505,NULL,'2012-12-20 11:03:30',NULL,'bowtieAligner','error','A value for the discard threshold must be specified',NULL,'en_US','discardThreshold',0),
	(1506,NULL,'2012-12-20 11:03:30',NULL,'bowtieAligner','constraint','NotEmpty',NULL,'en_US','isBest',0),
	(1507,NULL,'2012-12-20 11:03:30',NULL,'bowtieAligner','metaposition','50',NULL,'en_US','isBest',0),
	(1508,NULL,'2012-12-20 11:03:30',NULL,'bowtieAligner','default','yes',NULL,'en_US','isBest',0),
	(1509,NULL,'2012-12-20 11:03:30',NULL,'bowtieAligner','label','report only best alignments?',NULL,'en_US','isBest',0),
	(1510,NULL,'2012-12-20 11:03:30',NULL,'bowtieAligner','control','select:yes:yes;no:no',NULL,'en_US','isBest',0),
	(1511,NULL,'2012-12-20 11:03:30',NULL,'bowtieAligner','error','A value must be selected',NULL,'en_US','isBest',0),
	(1512,NULL,'2012-12-20 11:03:30',NULL,'genericBiomolecule','constraint','NotEmpty',NULL,'en_US','species',0),
	(1513,NULL,'2012-12-20 11:03:30',NULL,'genericBiomolecule','metaposition','10',NULL,'en_US','species',0),
	(1514,NULL,'2012-12-20 11:03:30',NULL,'genericBiomolecule','label','Species',NULL,'en_US','species',0),
	(1515,'2013-03-12 12:39:44','2013-03-12 12:39:44',X'B25884B41B184AD5A31738B28B844E0E','genericBiomolecule','control','select:Human [Homo sapiens - GRCh37]:Human [Homo sapiens - GRCh37];Mouse [Mus musculus - GRCm38]:Mouse [Mus musculus - GRCm38]; Other:Other',NULL,'en_US','species',0),
	(1516,NULL,'2012-12-20 11:03:30',NULL,'genericBiomolecule','error','You must select a species',NULL,'en_US','species',0),
	(1517,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','constraint','NotEmpty',NULL,'en_US','concentration',0),
	(1518,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','metaposition','10',NULL,'en_US','concentration',0),
	(1519,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','type','NUMBER',NULL,'en_US','concentration',0),
	(1520,'2013-03-12 12:39:44','2013-03-12 12:39:44',X'851E673922F24103953B249140F188C4','genericLibrary','label','Concentration (ng/&micro;l)',NULL,'en_US','concentration',0),
	(1521,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','error','You must provide a concentration',NULL,'en_US','concentration',0),
	(1522,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','constraint','NotEmpty',NULL,'en_US','volume',0),
	(1523,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','metaposition','20',NULL,'en_US','volume',0),
	(1524,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','type','NUMBER',NULL,'en_US','volume',0),
	(1525,'2013-03-12 12:39:44','2013-03-12 12:39:44',X'F6E1B6C00118458FADDFEFE3603132B8','genericLibrary','label','Volume (&micro;l)',NULL,'en_US','volume',0),
	(1526,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','error','You must provide a volume',NULL,'en_US','volume',0),
	(1527,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','constraint','NotEmpty',NULL,'en_US','buffer',0),
	(1528,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','metaposition','30',NULL,'en_US','buffer',0),
	(1529,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','label','Buffer',NULL,'en_US','buffer',0),
	(1530,'2013-03-12 12:39:44','2013-03-12 12:39:44',X'EA495098E32F4487B51060234145E614','genericLibrary','control','select:TE:TE;10mM Tris (EB):10mM Tris (EB);Water:Water',NULL,'en_US','buffer',0),
	(1531,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','error','You must select a buffer type',NULL,'en_US','buffer',0),
	(1532,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','constraint','NotEmpty',NULL,'en_US','adaptorset',0),
	(1533,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','metaposition','40',NULL,'en_US','adaptorset',0),
	(1534,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','label','Adaptor Set',NULL,'en_US','adaptorset',0),
	(1535,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','control','select:${adaptorsets}:adaptorsetId:name',NULL,'en_US','adaptorset',0),
	(1536,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','error','You must choose an adaptor set',NULL,'en_US','adaptorset',0),
	(1537,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','constraint','NotEmpty',NULL,'en_US','adaptor',0),
	(1538,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','metaposition','50',NULL,'en_US','adaptor',0),
	(1539,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','label','Adaptor',NULL,'en_US','adaptor',0),
	(1540,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','control','select:${adaptors}:adaptorId:name',NULL,'en_US','adaptor',0),
	(1541,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','error','You must choose an adaptor',NULL,'en_US','adaptor',0),
	(1542,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','constraint','NotEmpty',NULL,'en_US','size',0),
	(1543,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','metaposition','60',NULL,'en_US','size',0),
	(1544,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','type','NUMBER',NULL,'en_US','size',0),
	(1545,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','label','Library Size',NULL,'en_US','size',0),
	(1546,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','error','You must specify the library size',NULL,'en_US','size',0),
	(1547,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','constraint','NotEmpty',NULL,'en_US','sizeSd',0),
	(1548,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','metaposition','70',NULL,'en_US','sizeSd',0),
	(1549,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','type','NUMBER',NULL,'en_US','sizeSd',0),
	(1550,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','label','Library Size SD',NULL,'en_US','sizeSd',0),
	(1551,NULL,'2012-12-20 11:03:30',NULL,'genericLibrary','error','You must specify the library size standard deviation',NULL,'en_US','sizeSd',0),
	(1552,NULL,'2012-12-20 11:03:30',NULL,'macsPeakcaller','metaposition','10',NULL,'en_US','pValueCutoff',0),
	(1553,NULL,'2012-12-20 11:03:30',NULL,'macsPeakcaller','type','NUMBER',NULL,'en_US','pValueCutoff',0),
	(1554,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'4DF0F18863DF43D9A6E0CBEAC9431CF7','macsPeakcaller','default','1e-5',NULL,'en_US','pValueCutoff',0),
	(1555,NULL,'2012-12-20 11:03:30',NULL,'macsPeakcaller','label','p Value Cutoff',NULL,'en_US','pValueCutoff',0),
	(1556,NULL,'2012-12-20 11:03:30',NULL,'macsPeakcaller','metaposition','20',NULL,'en_US','bandwidth',0),
	(1557,NULL,'2012-12-20 11:03:30',NULL,'macsPeakcaller','type','NUMBER',NULL,'en_US','bandwidth',0),
	(1558,NULL,'2012-12-20 11:03:30',NULL,'macsPeakcaller','range','0:5000',NULL,'en_US','bandwidth',0),
	(1559,NULL,'2012-12-20 11:03:30',NULL,'macsPeakcaller','default','300',NULL,'en_US','bandwidth',0),
	(1560,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'62D28CB015DD4D18A862DE463F8A3498','macsPeakcaller','label','Band width',NULL,'en_US','bandwidth',0),
	(1561,NULL,'2012-12-20 11:03:30',NULL,'macsPeakcaller','metaposition','30',NULL,'en_US','genomeSize',0),
	(1562,NULL,'2012-12-20 11:03:30',NULL,'macsPeakcaller','type','NUMBER',NULL,'en_US','genomeSize',0),
	(1563,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'93498D2F18E543A8AEE54FC07E771F67','macsPeakcaller','default','1e9',NULL,'en_US','genomeSize',0),
	(1564,NULL,'2012-12-20 11:03:30',NULL,'macsPeakcaller','label','Effective Genome Size',NULL,'en_US','genomeSize',0),
	(1565,NULL,'2012-12-20 11:03:30',NULL,'macsPeakcaller','metaposition','40',NULL,'en_US','keepDup',0),
	(1566,NULL,'2012-12-20 11:03:30',NULL,'macsPeakcaller','default','no',NULL,'en_US','keepDup',0),
	(1567,NULL,'2012-12-20 11:03:30',NULL,'macsPeakcaller','label','Keep Duplicates?',NULL,'en_US','keepDup',0),
	(1568,NULL,'2012-12-20 11:03:30',NULL,'macsPeakcaller','control','select:yes:yes;no:no',NULL,'en_US','keepDup',0),
	(1569,NULL,'2012-12-20 11:03:30',NULL,'bisulfiteSeqPipeline','metaposition','10',NULL,'en_US','SeqMode',0),
	(1570,NULL,'2012-12-20 11:03:30',NULL,'bisulfiteSeqPipeline','default','no',NULL,'en_US','SeqMode',0),
	(1571,NULL,'2012-12-20 11:03:30',NULL,'bisulfiteSeqPipeline','label','Is Reduced Representation Bisul-Seq?',NULL,'en_US','SeqMode',0),
	(1572,NULL,'2012-12-20 11:03:30',NULL,'bisulfiteSeqPipeline','control','select:yes:yes;no:no',NULL,'en_US','SeqMode',0),
	(1573,NULL,'2012-12-20 11:03:30',NULL,'bisulfiteSeqPipeline','metaposition','20',NULL,'en_US','NumMismatch',0),
	(1574,NULL,'2012-12-20 11:03:30',NULL,'bisulfiteSeqPipeline','type','NUMBER',NULL,'en_US','NumMismatch',0),
	(1575,NULL,'2012-12-20 11:03:30',NULL,'bisulfiteSeqPipeline','range','2:15',NULL,'en_US','NumMismatch',0),
	(1576,NULL,'2012-12-20 11:03:30',NULL,'bisulfiteSeqPipeline','default','2',NULL,'en_US','NumMismatch',0),
	(1577,NULL,'2012-12-20 11:03:30',NULL,'bisulfiteSeqPipeline','label','Number of mismatch allowed',NULL,'en_US','NumMismatch',0),
	(1578,NULL,'2012-12-20 11:03:30',NULL,'bisulfiteSeqPipeline','metaposition','30',NULL,'en_US','ReportMode',0),
	(1579,NULL,'2012-12-20 11:03:30',NULL,'bisulfiteSeqPipeline','default','yes',NULL,'en_US','ReportMode',0),
	(1580,NULL,'2012-12-20 11:03:30',NULL,'bisulfiteSeqPipeline','label','Only report unique hits?',NULL,'en_US','ReportMode',0),
	(1581,NULL,'2012-12-20 11:03:30',NULL,'bisulfiteSeqPipeline','control','select:yes:yes;no:no',NULL,'en_US','ReportMode',0),
	(1582,NULL,'2012-12-20 11:03:30',NULL,'bisulfiteSeqPipeline','metaposition','40',NULL,'en_US','TrimQ',0),
	(1583,NULL,'2012-12-20 11:03:30',NULL,'bisulfiteSeqPipeline','type','NUMBER',NULL,'en_US','TrimQ',0),
	(1584,NULL,'2012-12-20 11:03:30',NULL,'bisulfiteSeqPipeline','range','0:30',NULL,'en_US','TrimQ',0),
	(1585,NULL,'2012-12-20 11:03:30',NULL,'bisulfiteSeqPipeline','default','0',NULL,'en_US','TrimQ',0),
	(1586,NULL,'2012-12-20 11:03:30',NULL,'bisulfiteSeqPipeline','label','Quality threshold in trimming',NULL,'en_US','TrimQ',0),
	(1587,NULL,'2012-12-20 11:03:30',NULL,'bisulfiteSeqPipeline','metaposition','50',NULL,'en_US','MapFormard',0),
	(1588,NULL,'2012-12-20 11:03:30',NULL,'bisulfiteSeqPipeline','default','yes',NULL,'en_US','MapFormard',0),
	(1589,NULL,'2012-12-20 11:03:30',NULL,'bisulfiteSeqPipeline','label','Only map to forward strands?',NULL,'en_US','MapFormard',0),
	(1590,NULL,'2012-12-20 11:03:30',NULL,'bisulfiteSeqPipeline','control','select:yes:yes;no:no',NULL,'en_US','MapFormard',0),
	(1591,NULL,'2012-12-20 11:03:30',NULL,'genericDna','constraint','NotEmpty',NULL,'en_US','concentration',0),
	(1592,NULL,'2012-12-20 11:03:30',NULL,'genericDna','metaposition','10',NULL,'en_US','concentration',0),
	(1593,NULL,'2012-12-20 11:03:30',NULL,'genericDna','type','NUMBER',NULL,'en_US','concentration',0),
	(1594,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'60D47CC2621A4F3DAB33C52F2B2D0A69','genericDna','label','Concentration (ng/&micro;l)',NULL,'en_US','concentration',0),
	(1595,NULL,'2012-12-20 11:03:30',NULL,'genericDna','error','You must provide a concentration',NULL,'en_US','concentration',0),
	(1596,NULL,'2012-12-20 11:03:30',NULL,'genericDna','constraint','NotEmpty',NULL,'en_US','volume',0),
	(1597,NULL,'2012-12-20 11:03:30',NULL,'genericDna','metaposition','20',NULL,'en_US','volume',0),
	(1598,NULL,'2012-12-20 11:03:30',NULL,'genericDna','type','NUMBER',NULL,'en_US','volume',0),
	(1599,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'47274DF0340C45A19FBF6475EB2D9672','genericDna','label','Volume (&micro;l)',NULL,'en_US','volume',0),
	(1600,NULL,'2012-12-20 11:03:30',NULL,'genericDna','error','You must provide a volume',NULL,'en_US','volume',0),
	(1601,NULL,'2012-12-20 11:03:30',NULL,'genericDna','constraint','NotEmpty',NULL,'en_US','buffer',0),
	(1602,NULL,'2012-12-20 11:03:30',NULL,'genericDna','metaposition','30',NULL,'en_US','buffer',0),
	(1603,NULL,'2012-12-20 11:03:30',NULL,'genericDna','label','Buffer',NULL,'en_US','buffer',0),
	(1604,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'648EF84659CA4D3394D40BD27FB65BD6','genericDna','control','select:TE:TE;10mM Tris (EB):10mM Tris (EB);Water:Water',NULL,'en_US','buffer',0),
	(1605,NULL,'2012-12-20 11:03:30',NULL,'genericDna','error','You must select a buffer type',NULL,'en_US','buffer',0),
	(1606,NULL,'2012-12-20 11:03:30',NULL,'genericDna','constraint','NotEmpty',NULL,'en_US','A260_280',0),
	(1607,NULL,'2012-12-20 11:03:30',NULL,'genericDna','metaposition','40',NULL,'en_US','A260_280',0),
	(1608,NULL,'2012-12-20 11:03:30',NULL,'genericDna','type','NUMBER',NULL,'en_US','A260_280',0),
	(1609,NULL,'2012-12-20 11:03:30',NULL,'genericDna','label','A260/280',NULL,'en_US','A260_280',0),
	(1610,NULL,'2012-12-20 11:03:30',NULL,'genericDna','error','You must provide an A260/280 reading',NULL,'en_US','A260_280',0),
	(1611,NULL,'2012-12-20 11:03:30',NULL,'genericDna','constraint','NotEmpty',NULL,'en_US','A260_230',0),
	(1612,NULL,'2012-12-20 11:03:30',NULL,'genericDna','metaposition','50',NULL,'en_US','A260_230',0),
	(1613,NULL,'2012-12-20 11:03:30',NULL,'genericDna','type','NUMBER',NULL,'en_US','A260_230',0),
	(1614,NULL,'2012-12-20 11:03:30',NULL,'genericDna','label','A260/230',NULL,'en_US','A260_230',0),
	(1615,NULL,'2012-12-20 11:03:30',NULL,'genericDna','error','You must provide an A260/230 reading',NULL,'en_US','A260_230',0),
	(1616,NULL,'2012-12-20 11:03:30',NULL,'bisulseqDna','constraint','NotEmpty',NULL,'en_US','fragmentSize',0),
	(1617,NULL,'2012-12-20 11:03:30',NULL,'bisulseqDna','metaposition','10',NULL,'en_US','fragmentSize',0),
	(1618,NULL,'2012-12-20 11:03:30',NULL,'bisulseqDna','type','NUMBER',NULL,'en_US','fragmentSize',0),
	(1619,NULL,'2012-12-20 11:03:30',NULL,'bisulseqDna','label','Average Fragmentation Size',NULL,'en_US','fragmentSize',0),
	(1620,NULL,'2012-12-20 11:03:30',NULL,'bisulseqDna','error','You must provide a fragmentSize',NULL,'en_US','fragmentSize',0),
	(1621,NULL,'2012-12-20 11:03:30',NULL,'bisulseqDna','constraint','NotEmpty',NULL,'en_US','fragmentSizeSD',0),
	(1622,NULL,'2012-12-20 11:03:30',NULL,'bisulseqDna','metaposition','20',NULL,'en_US','fragmentSizeSD',0),
	(1623,NULL,'2012-12-20 11:03:30',NULL,'bisulseqDna','type','NUMBER',NULL,'en_US','fragmentSizeSD',0),
	(1624,NULL,'2012-12-20 11:03:30',NULL,'bisulseqDna','label','Fragmentation Size Std. Dev.',NULL,'en_US','fragmentSizeSD',0),
	(1625,NULL,'2012-12-20 11:03:30',NULL,'bisulseqDna','error','You must provide a standard deviation',NULL,'en_US','fragmentSizeSD',0),
	(1626,NULL,'2012-12-20 11:03:30',NULL,'bisulseqDna','metaposition','30',NULL,'en_US','antibody',0),
	(1627,NULL,'2012-12-20 11:03:30',NULL,'bisulseqDna','label','Antibody',NULL,'en_US','antibody',0),
	(1628,NULL,'2012-12-20 11:03:30',NULL,'wasp-bisulfiteSeqPlugin','label','BISUL Seq',NULL,'en_US','workflow',0),
	(1629,NULL,'2012-12-20 11:03:30',NULL,'wasp-bisulfiteSeqPlugin','label','Modify BisulSeq Metadata',NULL,'en_US','jobsubmit/modifymeta',0),
	(1630,NULL,'2012-12-20 11:03:30',NULL,'wasp-bisulfiteSeqPlugin','label','MPS Sequencer Options',NULL,'en_US','jobsubmit/resource/mps',0),
	(1631,NULL,'2012-12-20 11:03:30',NULL,'wasp-bisulfiteSeqPlugin','label','Samples',NULL,'en_US','jobsubmit/samples',0),
	(1632,NULL,'2012-12-20 11:03:30',NULL,'wasp-bisulfiteSeqPlugin','label','DNA Sequencer Cells',NULL,'en_US','jobsubmit/cells',0),
	(1633,NULL,'2012-12-20 11:03:30',NULL,'wasp-bisulfiteSeqPlugin','label','BISUL-seq Pipeline Selection',NULL,'en_US','jobsubmit/software/bisulfiteSeqPipeline',0),
	(1634,NULL,'2012-12-20 11:03:30',NULL,'wasp-bisulfiteSeqPlugin','label','Verify Submission',NULL,'en_US','jobsubmit/verify',0),
	(1635,NULL,'2012-12-20 11:03:30',NULL,'chipseqDna','constraint','NotEmpty',NULL,'en_US','fragmentSize',0),
	(1636,NULL,'2012-12-20 11:03:30',NULL,'chipseqDna','metaposition','10',NULL,'en_US','fragmentSize',0),
	(1637,NULL,'2012-12-20 11:03:30',NULL,'chipseqDna','type','NUMBER',NULL,'en_US','fragmentSize',0),
	(1638,NULL,'2012-12-20 11:03:31',NULL,'chipseqDna','label','Average Fragmentation Size',NULL,'en_US','fragmentSize',0),
	(1639,NULL,'2012-12-20 11:03:31',NULL,'chipseqDna','error','You must provide a fragmentSize',NULL,'en_US','fragmentSize',0),
	(1640,NULL,'2012-12-20 11:03:31',NULL,'chipseqDna','constraint','NotEmpty',NULL,'en_US','fragmentSizeSD',0),
	(1641,NULL,'2012-12-20 11:03:31',NULL,'chipseqDna','metaposition','20',NULL,'en_US','fragmentSizeSD',0),
	(1642,NULL,'2012-12-20 11:03:31',NULL,'chipseqDna','type','NUMBER',NULL,'en_US','fragmentSizeSD',0),
	(1643,NULL,'2012-12-20 11:03:31',NULL,'chipseqDna','label','Fragmentation Size Std. Dev.',NULL,'en_US','fragmentSizeSD',0),
	(1644,NULL,'2012-12-20 11:03:31',NULL,'chipseqDna','error','You must provide a standard deviation',NULL,'en_US','fragmentSizeSD',0),
	(1645,NULL,'2012-12-20 11:03:31',NULL,'chipseqDna','metaposition','30',NULL,'en_US','antibody',0),
	(1646,NULL,'2012-12-20 11:03:31',NULL,'chipseqDna','label','Antibody',NULL,'en_US','antibody',0),
	(1647,NULL,'2012-12-20 11:03:31',NULL,'chipSeqPlugin','label','Chip Seq',NULL,'en_US','workflow',0),
	(1648,NULL,'2012-12-20 11:03:31',NULL,'chipSeqPlugin','label','ModifyChipSeq Metadata',NULL,'en_US','jobsubmit/modifymeta',0),
	(1649,NULL,'2012-12-20 11:03:31',NULL,'chipSeqPlugin','label','MPS Sequencer Options',NULL,'en_US','jobsubmit/resource/mps',0),
	(1650,NULL,'2012-12-20 11:03:31',NULL,'chipSeqPlugin','label','Samples',NULL,'en_US','jobsubmit/samples',0),
	(1651,NULL,'2012-12-20 11:03:31',NULL,'chipSeqPlugin','label','DNA Sequencer Cells',NULL,'en_US','jobsubmit/cells',0),
	(1652,NULL,'2012-12-20 11:03:31',NULL,'chipSeqPlugin','label','IP vs Input Pairings',NULL,'en_US','jobsubmit/chipSeq/pair',0),
	(1653,NULL,'2012-12-20 11:03:31',NULL,'chipSeqPlugin','label','Aligner Selection',NULL,'en_US','jobsubmit/software/aligner',0),
	(1654,NULL,'2012-12-20 11:03:31',NULL,'chipSeqPlugin','label','Peak Calling Software Selection',NULL,'en_US','jobsubmit/software/peakcaller',0),
	(1655,NULL,'2012-12-20 11:03:31',NULL,'chipSeqPlugin','label','Comments',NULL,'en_US','jobsubmit/comment',0),
	(1656,NULL,'2012-12-20 11:03:31',NULL,'chipSeqPlugin','label','Verify Submission',NULL,'en_US','jobsubmit/verify',0),
	(1657,NULL,'2012-12-20 11:03:31',NULL,'chipSeqPlugin','label','Please select test vs control for all samples to be analyzed in pairs after sequencing.',NULL,'en_US','pairing_instructions',0),
	(1658,NULL,'2012-12-20 11:03:31',NULL,'chipSeqPlugin','label','Test',NULL,'en_US','test',0),
	(1659,NULL,'2012-12-20 11:03:31',NULL,'chipSeqPlugin','label','Control',NULL,'en_US','control',0),
	(1660,NULL,'2012-12-20 11:03:31',NULL,'genericDnaSeqPlugin','label','Generic DNA Seq',NULL,'en_US','workflow',0),
	(1661,NULL,'2012-12-20 11:03:31',NULL,'genericDnaSeqPlugin','label','Modify Generic DNA Seq Metadata',NULL,'en_US','jobsubmit/modifymeta',0),
	(1662,NULL,'2012-12-20 11:03:31',NULL,'genericDnaSeqPlugin','label','MPS Sequencer Options',NULL,'en_US','jobsubmit/resource/mps',0),
	(1663,NULL,'2012-12-20 11:03:31',NULL,'genericDnaSeqPlugin','label','Samples',NULL,'en_US','jobsubmit/samples',0),
	(1664,NULL,'2012-12-20 11:03:31',NULL,'genericDnaSeqPlugin','label','DNA Sequencer Cells',NULL,'en_US','jobsubmit/cells',0),
	(1665,NULL,'2012-12-20 11:03:31',NULL,'genericDnaSeqPlugin','label','Aligner Selection',NULL,'en_US','jobsubmit/software/aligner',0),
	(1666,NULL,'2012-12-20 11:03:31',NULL,'genericDnaSeqPlugin','label','Comments',NULL,'en_US','jobsubmit/comment',0),
	(1667,NULL,'2012-12-20 11:03:31',NULL,'genericDnaSeqPlugin','label','Verify Submission',NULL,'en_US','jobsubmit/verify',0),
	(1668,NULL,'2012-12-20 11:03:31',NULL,'illuminaHiSeq2000','constraint','NotEmpty',NULL,'en_US','readLength',0),
	(1669,NULL,'2012-12-20 11:03:31',NULL,'illuminaHiSeq2000','metaposition','10',NULL,'en_US','readLength',0),
	(1670,NULL,'2012-12-20 11:03:31',NULL,'illuminaHiSeq2000','label','Read Length',NULL,'en_US','readLength',0),
	(1671,NULL,'2012-12-20 11:03:31',NULL,'illuminaHiSeq2000','control','select:${resourceOptions.get(readlength)}:value:label',NULL,'en_US','readLength',0),
	(1672,NULL,'2012-12-20 11:03:31',NULL,'illuminaHiSeq2000','error','You must choose a read length',NULL,'en_US','readLength',0),
	(1673,NULL,'2012-12-20 11:03:31',NULL,'illuminaHiSeq2000','constraint','NotEmpty',NULL,'en_US','readType',0),
	(1674,NULL,'2012-12-20 11:03:31',NULL,'illuminaHiSeq2000','metaposition','20',NULL,'en_US','readType',0),
	(1675,NULL,'2012-12-20 11:03:31',NULL,'illuminaHiSeq2000','label','Read Type',NULL,'en_US','readType',0),
	(1676,NULL,'2012-12-20 11:03:31',NULL,'illuminaHiSeq2000','control','select:${resourceOptions.get(readType)}:value:label',NULL,'en_US','readType',0),
	(1677,NULL,'2012-12-20 11:03:31',NULL,'illuminaHiSeq2000','error','You must choose a read type',NULL,'en_US','readType',0),
	(1678,NULL,'2012-12-20 11:03:31',NULL,'helptagPipeline','metaposition','10',NULL,'en_US','pValueCutoff',0),
	(1679,NULL,'2012-12-20 11:03:31',NULL,'helptagPipeline','type','NUMBER',NULL,'en_US','pValueCutoff',0),
	(1680,NULL,'2012-12-20 11:03:31',NULL,'helptagPipeline','default','100000',NULL,'en_US','pValueCutoff',0),
	(1681,NULL,'2012-12-20 11:03:31',NULL,'helptagPipeline','label','p Value Cutoff',NULL,'en_US','pValueCutoff',0),
	(1682,NULL,'2012-12-20 11:03:31',NULL,'helptagPipeline','metaposition','20',NULL,'en_US','bandwidth',0),
	(1683,NULL,'2012-12-20 11:03:31',NULL,'helptagPipeline','type','NUMBER',NULL,'en_US','bandwidth',0),
	(1684,NULL,'2012-12-20 11:03:31',NULL,'helptagPipeline','range','0:5000',NULL,'en_US','bandwidth',0),
	(1685,NULL,'2012-12-20 11:03:31',NULL,'helptagPipeline','default','300',NULL,'en_US','bandwidth',0),
	(1686,NULL,'2012-12-20 11:03:31',NULL,'helptagPipeline','label','Bandwidth',NULL,'en_US','bandwidth',0),
	(1687,NULL,'2012-12-20 11:03:31',NULL,'helptagPipeline','metaposition','30',NULL,'en_US','genomeSize',0),
	(1688,NULL,'2012-12-20 11:03:31',NULL,'helptagPipeline','type','NUMBER',NULL,'en_US','genomeSize',0),
	(1689,NULL,'2012-12-20 11:03:31',NULL,'helptagPipeline','default','1000000000',NULL,'en_US','genomeSize',0),
	(1690,NULL,'2012-12-20 11:03:31',NULL,'helptagPipeline','label','Effective Genome Size',NULL,'en_US','genomeSize',0),
	(1691,NULL,'2012-12-20 11:03:31',NULL,'helptagPipeline','metaposition','40',NULL,'en_US','keepDup',0),
	(1692,NULL,'2012-12-20 11:03:31',NULL,'helptagPipeline','default','no',NULL,'en_US','keepDup',0),
	(1693,NULL,'2012-12-20 11:03:31',NULL,'helptagPipeline','label','Keep Duplicates?',NULL,'en_US','keepDup',0),
	(1694,NULL,'2012-12-20 11:03:31',NULL,'helptagPipeline','control','select:yes:yes;no:no',NULL,'en_US','keepDup',0),
	(1695,NULL,'2012-12-20 11:03:31',NULL,'helptagLibrary','constraint','NotEmpty',NULL,'en_US','fragmentSize',0),
	(1696,NULL,'2012-12-20 11:03:31',NULL,'helptagLibrary','metaposition','10',NULL,'en_US','fragmentSize',0),
	(1697,NULL,'2012-12-20 11:03:31',NULL,'helptagLibrary','type','NUMBER',NULL,'en_US','fragmentSize',0),
	(1698,NULL,'2012-12-20 11:03:31',NULL,'helptagLibrary','label','Average Fragmentation Size',NULL,'en_US','fragmentSize',0),
	(1699,NULL,'2012-12-20 11:03:31',NULL,'helptagLibrary','error','You must provide a fragmentSize',NULL,'en_US','fragmentSize',0),
	(1700,NULL,'2012-12-20 11:03:31',NULL,'helptagLibrary','constraint','NotEmpty',NULL,'en_US','fragmentSizeSD',0),
	(1701,NULL,'2012-12-20 11:03:31',NULL,'helptagLibrary','metaposition','20',NULL,'en_US','fragmentSizeSD',0),
	(1702,NULL,'2012-12-20 11:03:31',NULL,'helptagLibrary','type','NUMBER',NULL,'en_US','fragmentSizeSD',0),
	(1703,NULL,'2012-12-20 11:03:31',NULL,'helptagLibrary','label','Fragmentation Size Std. Dev.',NULL,'en_US','fragmentSizeSD',0),
	(1704,NULL,'2012-12-20 11:03:31',NULL,'helptagLibrary','error','You must provide a standard deviation',NULL,'en_US','fragmentSizeSD',0),
	(1705,NULL,'2012-12-20 11:03:31',NULL,'helptagLibrary','metaposition','30',NULL,'en_US','antibody',0),
	(1706,NULL,'2012-12-20 11:03:31',NULL,'helptagLibrary','label','Antibody',NULL,'en_US','antibody',0),
	(1707,NULL,'2012-12-20 11:03:31',NULL,'helpTagPlugin','label','HELP Tagging',NULL,'en_US','workflow',0),
	(1708,NULL,'2012-12-20 11:03:31',NULL,'helpTagPlugin','label','Modify HelpTag Metadata',NULL,'en_US','jobsubmit/modifymeta',0),
	(1709,NULL,'2012-12-20 11:03:31',NULL,'helpTagPlugin','label','MPS Sequencer Options',NULL,'en_US','jobsubmit/resource/mps',0),
	(1710,NULL,'2012-12-20 11:03:31',NULL,'helpTagPlugin','label','Samples',NULL,'en_US','jobsubmit/samples',0),
	(1711,NULL,'2012-12-20 11:03:31',NULL,'helpTagPlugin','label','DNA Sequencer Cells',NULL,'en_US','jobsubmit/cells',0),
	(1712,NULL,'2012-12-20 11:03:31',NULL,'helpTagPlugin','label','HpaII vs MspI Pairings',NULL,'en_US','jobsubmit/helpTag/pair',0),
	(1713,NULL,'2012-12-20 11:03:31',NULL,'helpTagPlugin','label','Aligner Selection',NULL,'en_US','jobsubmit/software/aligner',0),
	(1714,NULL,'2012-12-20 11:03:31',NULL,'helpTagPlugin','label','HELP-tag Pipeline Selection',NULL,'en_US','jobsubmit/software/helptagPipeline',0),
	(1715,NULL,'2012-12-20 11:03:31',NULL,'helpTagPlugin','label','Verify Submission',NULL,'en_US','jobsubmit/verify',0),
	(1716,NULL,'2012-12-20 11:03:31',NULL,'helpTagPlugin','label','Please select HpaII vs MspI for all samples to be analyzed in pairs after sequencing.',NULL,'en_US','pairing_instructions',0),
	(1717,NULL,'2012-12-20 11:03:31',NULL,'helpTagPlugin','label','HpaII',NULL,'en_US','test',0),
	(1718,NULL,'2012-12-20 11:03:31',NULL,'helpTagPlugin','label','MspI',NULL,'en_US','control',0),
	(1719,NULL,'2012-12-20 11:03:32',NULL,'illuminaMiSeqPersonalSequencer','constraint','NotEmpty',NULL,'en_US','readLength',0),
	(1720,NULL,'2012-12-20 11:03:32',NULL,'illuminaMiSeqPersonalSequencer','metaposition','10',NULL,'en_US','readLength',0),
	(1721,NULL,'2012-12-20 11:03:32',NULL,'illuminaMiSeqPersonalSequencer','label','Read Length',NULL,'en_US','readLength',0),
	(1722,NULL,'2012-12-20 11:03:32',NULL,'illuminaMiSeqPersonalSequencer','control','select:${resourceOptions.get(readlength)}:value:label',NULL,'en_US','readLength',0),
	(1723,NULL,'2012-12-20 11:03:32',NULL,'illuminaMiSeqPersonalSequencer','error','You must choose a read length',NULL,'en_US','readLength',0),
	(1724,NULL,'2012-12-20 11:03:32',NULL,'illuminaMiSeqPersonalSequencer','constraint','NotEmpty',NULL,'en_US','readType',0),
	(1725,NULL,'2012-12-20 11:03:32',NULL,'illuminaMiSeqPersonalSequencer','metaposition','20',NULL,'en_US','readType',0),
	(1726,NULL,'2012-12-20 11:03:32',NULL,'illuminaMiSeqPersonalSequencer','label','Read Type',NULL,'en_US','readType',0),
	(1727,NULL,'2012-12-20 11:03:32',NULL,'illuminaMiSeqPersonalSequencer','control','select:${resourceOptions.get(readType)}:value:label',NULL,'en_US','readType',0),
	(1728,NULL,'2012-12-20 11:03:32',NULL,'illuminaMiSeqPersonalSequencer','error','You must choose a read type',NULL,'en_US','readType',0),
	(1729,NULL,'2012-12-20 11:03:32',NULL,'illuminaFlowcellMiSeqV1','label','Lane',NULL,'en_US','cellName',0),
	(1730,NULL,'2012-12-20 11:03:32',NULL,'illuminaFlowcellMiSeqV1','label','Flow Cell',NULL,'en_US','platformUnitName',0),
	(1731,NULL,'2012-12-20 11:03:32',NULL,'illuminaFlowcellV3','label','Lane',NULL,'en_US','cellName',0),
	(1732,NULL,'2012-12-20 11:03:32',NULL,'illuminaFlowcellV3','label','Flow Cell',NULL,'en_US','platformUnitName',0),
	(1991,'2013-03-12 12:39:31','2013-03-12 12:39:31',X'67B54AFF28594006B59CDDA7B7C90459','analysisParameters','label','Analysis Pairs',NULL,'en_US','analysis_pairs',NULL),
	(1992,'2013-03-12 12:39:31','2013-03-12 12:39:31',X'3B6DC4B738074D5BA7C312D10698156A','analysisParameters','label','Control Samples',NULL,'en_US','control_samples',NULL),
	(1993,'2013-03-12 12:39:31','2013-03-12 12:39:31',X'386AE1925DD34C0F9BE77EB366751BE8','analysisParameters','label','No Software Requested',NULL,'en_US','no_software_requested',NULL),
	(1994,'2013-03-12 12:39:31','2013-03-12 12:39:31',X'C8B96E7EF734495DBF9F4AC8E8B7D5D3','analysisParameters','label','Reference',NULL,'en_US','reference',NULL),
	(1995,'2013-03-12 12:39:31','2013-03-12 12:39:31',X'FF6E38EE80CF43AEA27935E7E7855C17','analysisParameters','label','Reference Paired With Samples (Reference acts as control)',NULL,'en_US','reference_sample_pairs',NULL),
	(1996,'2013-03-12 12:39:31','2013-03-12 12:39:31',X'0E92763359364656B676111068C2576A','analysisParameters','label','Reference Paired With Samples (Sample acts as control)',NULL,'en_US','sample_pairs_reference',NULL),
	(1997,'2013-03-12 12:39:31','2013-03-12 12:39:31',X'F7B44B27A3D84F4A9C2903B2746AE14A','analysisParameters','label','Software Requested',NULL,'en_US','software_requested',NULL),
	(1998,'2013-03-12 12:39:31','2013-03-12 12:39:31',X'83F06FD6A28D4067915B6C147023E508','analysisParameters','label','Test Samples',NULL,'en_US','test_samples',NULL),
	(1999,'2013-03-12 12:39:31','2013-03-12 12:39:31',X'6D8F3F20C03845C9A0F12A27F680583E','auth','label','You are not Authorized to view this page',NULL,'en_US','accessDenied',NULL),
	(2000,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'F4AAAFB4853B4F299F62ABF3091E3F60','dapendingtask','label','Please Select Approve Or Reject',NULL,'en_US','approveRejectAlert',NULL),
	(2001,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'0C7DCE324B134FD2BF248ED84320D3CD','dapendingtask','error','Update Failed: You Must Provide A Reason For Rejecting This Job',NULL,'en_US','commentEmpty',NULL),
	(2002,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'B76D3DF7C1EF41B9BC4BE7A98B3B7A47','dapendingtask','label','(which will be conveyed to the user!)',NULL,'en_US','conveyedToUser',NULL),
	(2003,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'EAE958CD32874465A43F8A880ECE9363','dapendingtask','error','Update Failed: Invalid Action',NULL,'en_US','invalidAction',NULL),
	(2004,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'7C8CCC9F1ADB4EAB8D4B9480AE74CA9C','dapendingtask','error','Update Failed: Job Not Found',NULL,'en_US','invalidJob',NULL),
	(2005,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'29B35F38C4794A6DA28D218879B206C0','dapendingtask','label','Job Has Been Approved',NULL,'en_US','jobApproved',NULL),
	(2006,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'BF5A13BC8C4946C8B2A002FEDD22976D','dapendingtask','label','Job Has Been Rejected',NULL,'en_US','jobRejected',NULL),
	(2007,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'6745D091D445400BA0CD0F7791DA2E89','dapendingtask','label','If you reject, you MUST provide a reason',NULL,'en_US','reasonForReject',NULL),
	(2008,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'DFBAB60D5612446DBEB4E5C760F4D6B2','dapendingtask','label','RESET',NULL,'en_US','reset',NULL),
	(2009,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'7795D7815B284273AFDB46B0D10C6770','dapendingtask','label','SUBMIT',NULL,'en_US','submit',NULL),
	(2010,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'532A04ABFA184E69B1679BC5FA791B02','dapendingtask','error','Update Unexpectedly Failed',NULL,'en_US','updateFailed',NULL),
	(2011,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'B668575BEDA7483A9A438DF699E203D1','dapendingtask','label','Please provide a reason for rejecting this PI\\n(note: the user will see this reason).',NULL,'en_US','validateCommentAlert',NULL),
	(2012,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'8AA7C193BE0D41248270821E3B6A8F18','dashboard','label','Bisulfate-Seq',NULL,'en_US','bisulfateseq',NULL),
	(2013,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'B8D0047F559E49B48587B2F0E9CF57E8','dashboard','label','ChIP-Seq',NULL,'en_US','chipseq',NULL),
	(2014,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'895AAA11E5854FAEA42D7FBCD37474B4','dashboard','label','HELP Tag',NULL,'en_US','helptag',NULL),
	(2015,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'E1238A592B6842908A969DC186AB0CD7','dashboard','label','Illumina',NULL,'en_US','illumina',NULL),
	(2016,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'10BB6CA2EB3343828F6D98BEEB77B147','dashboard','label','Join Another Lab',NULL,'en_US','joinAnotherLab',NULL),
	(2017,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'B3C14D440EC54DCD9AF3088D5F63FE84','dashboard','label','My Tasks',NULL,'en_US','myTasks',NULL),
	(2018,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'EFC95707698444788DE2AD3A9AD5E35B','dashboard','label','Plugins',NULL,'en_US','pluginUtils',NULL),
	(2019,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'902E0D970B9941288EEAC56ECF3B1397','dashboard','label','Upgrade To PI',NULL,'en_US','upgradeStatusToPI',NULL),
	(2020,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'97D98BDD0F8B4F12BAD9ABFD72EB73D6','file','label','Download Link',NULL,'en_US','download',NULL),
	(2021,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'53434F8A54314D0EB1514EF867266D0B','file','label','File Name',NULL,'en_US','name',NULL),
	(2022,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'47555ABA204341F2934B6D95E5D9BCF0','home','label','Current User',NULL,'en_US','currentUser',NULL),
	(2023,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'2A5C876D7BA44D85A9B5EA4F4C9814BF','home','label','You have tasks awaiting action. To view those tasks, select the Task menu option above.',NULL,'en_US','tasksAwaiting',NULL),
	(2024,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'53E5FEB7C4564A72BFCB69C6477122E6','home','label','WASP Home Page',NULL,'en_US','waspHomePage',NULL),
	(2025,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'07FE29CEC3D94F6B87AC33395A50BE4A','home','label','Welcome Back',NULL,'en_US','welcomeBack',NULL),
	(2026,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'50292988154F4A628078D5B9F6EBE3F9','job','label','Job Status',NULL,'en_US','currentStatus',NULL),
	(2027,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'F1AB4031E0E94659BBFC83AC34E906B2','jobapprovetask','label','APPROVED',NULL,'en_US','approve',NULL),
	(2028,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'73CF277E93E148F7BF4FA87649FFC04C','jobapprovetask','label','Please Select Approved Or Rejected',NULL,'en_US','approveRejectAlert',NULL),
	(2029,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'3367BA54AD8948B08DC686C916FDA25D','jobapprovetask','error','Update Failed: You Must Provide A Reason For Rejecting This Job',NULL,'en_US','commentEmpty',NULL),
	(2030,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'457EB8DDC7A049E7BA48986E0EFF3EA9','jobapprovetask','error','Update Failed: Actions Must Be Accepted Or Rejected',NULL,'en_US','invalidAction',NULL),
	(2031,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'92352E7622A44B74BECAE84FB0BFE5D7','jobapprovetask','error','Update Failed: Job Not Found',NULL,'en_US','invalidJob',NULL),
	(2032,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'5F7EB333E95248E8A763B17A9323A20A','jobapprovetask','error','Update Failed: Invalid Internal Job Approval Code',NULL,'en_US','invalidJobApproveCode',NULL),
	(2033,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'F1164F80F1CC4B9B9924D9C32EE531FD','jobapprovetask','label','Job Approved',NULL,'en_US','jobApproved',NULL),
	(2034,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'7A51A38978D34B3389E712C0DCDD080B','jobapprovetask','label','Job ID',NULL,'en_US','jobId',NULL),
	(2035,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'116F133196AE4B3DB8FE029E21C33DB9','jobapprovetask','label','Job Name',NULL,'en_US','jobName',NULL),
	(2036,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'A15EDDB251BB49A6B9098541EAB3BB29','jobapprovetask','label','Job Rejected',NULL,'en_US','jobRejected',NULL),
	(2037,'2013-03-12 12:39:32','2013-03-12 12:39:32',X'4A69F6E722CF47A683CA79CA57DF7C71','jobapprovetask','label','PI',NULL,'en_US','pi',NULL),
	(2038,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'342710C0F1FB4DC991C3DA771FDAB24B','jobapprovetask','label','REJECTED',NULL,'en_US','reject',NULL),
	(2039,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'1F092B4067414058BBEB3C49F1F3764F','jobapprovetask','label','RESET',NULL,'en_US','reset',NULL),
	(2040,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'7767EB422A2E4702A8DF1BD2A429F747','jobapprovetask','label','Samples',NULL,'en_US','samples',NULL),
	(2041,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'ED72334B8F73474CBF0EF5E62A289A67','jobapprovetask','label','SUBMIT',NULL,'en_US','submit',NULL),
	(2042,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'088CA21AE7E8412F8C0A69EDC5B123B1','jobapprovetask','label','Submitter',NULL,'en_US','submitter',NULL),
	(2043,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'44A85DDC46E044F79A68A5F46293A313','jobapprovetask','label','Jobs Pending Approval',NULL,'en_US','subtitle2',NULL),
	(2044,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'814D3DDDF5EC442FBF38B205086D6DA8','jobapprovetask','label','No Jobs Pending Approval',NULL,'en_US','subtitle2_none',NULL),
	(2045,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'B5E2F4C59F464FFE889BF0705A6B5E8D','jobapprovetask','label','Unknown',NULL,'en_US','unknown',NULL),
	(2046,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'CC2BDD4E60FF4929B74818B4606801A9','jobapprovetask','error','Update Unexpectedly Failed',NULL,'en_US','updateFailed',NULL),
	(2047,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'A07B6A6AB7B94A9E98DCD7B30FAAC7B3','jobapprovetask','label','Please provide a reason for rejecting this job',NULL,'en_US','validateCommentAlert',NULL),
	(2048,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'5B0E4BF8948F46868D868C3F881031BD','jobapprovetask','label','Workflow',NULL,'en_US','workflow',NULL),
	(2049,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'AD763CD011904ED58B36638950DA2002','jobdetail_for_import','label','Analysis Parameters',NULL,'en_US','jobAnalysisParameters',NULL),
	(2050,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'41F95BE6241542AF99EF6392BB4FD63B','jobdetail_for_import','label','View',NULL,'en_US','jobAnalysisParametersView',NULL),
	(2051,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'B1AE3757D43744A4AE3AE35430E61903','jobdetail_for_import','label','Job Status',NULL,'en_US','jobStatus',NULL),
	(2052,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'35DAF448DE6B4117A48F1405FB3332E2','jobDraft','label','Cell/Lane',NULL,'en_US','cellLane',NULL),
	(2053,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'04E6FDC63E9244C6AF3AB543C26BFE78','jobDraft','label','Finish Later',NULL,'en_US','finishLater',NULL),
	(2054,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'30EA01FF38F6420E98AC4C343E3B5CC7','jobDraft','label','New Sample',NULL,'en_US','newSample',NULL),
	(2055,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'2B01624E76B24BAA94E74D913BB58F75','jobDraft','label','New Library',NULL,'en_US','newLibrary',NULL),
	(2056,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'9FB4C90245E34749B66C4FEADC1EDA34','jobDraft','label','Save',NULL,'en_US','save',NULL),
	(2057,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'F27B43E41B554262B54CC91EA84CDE06','jobDraft','label','Continue',NULL,'en_US','continue',NULL),
	(2058,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'459EFD20BB4347D28A1D529EF47B70BB','lab','label','To request access to another lab, enter the email address of the PI whose lab you wish to join. Your request is subject to approval by that PI.',NULL,'en_US','joinAnotherLabInstructions',NULL),
	(2059,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'A1BE3D12B3A24627BBF3C3AF12605A17','lab','label','Email Address Of PI Whose<br />Lab You Wish To Join',NULL,'en_US','joinAnotherLab_emailOfPI',NULL),
	(2060,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'8493A263BAD34D349FDFD05E7E9680D2','lab','error','The lab you specified is no longer active according to the WASP database',NULL,'en_US','joinAnotherLab_labNotActive',NULL),
	(2061,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'97D00F84C2294FF089E6DDB3DC9AB230','lab','error','The person you specified is NOT the head of any laboratory in the WASP database',NULL,'en_US','joinAnotherLab_emailDoesNotBelongToLabPI',NULL),
	(2062,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'622D2D0D04FE46ADB8D6F5C60B665F42','lab','error','Please provide an email address for the PI whose lab you wish to join',NULL,'en_US','joinAnotherLab_piEmailEmpty',NULL),
	(2063,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'CF62A99C8D1E4FC38A4CAB5976EA1115','lab','error','Please provide a well-formatted email address for the PI whose lab you wish to join',NULL,'en_US','joinAnotherLab_piEmailFormatError',NULL),
	(2064,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'56D585C495CC4A178C4FB6523B803687','lab','error','The email address you provided was NOT found in our database. Please confirm spelling and try again.',NULL,'en_US','joinAnotherLab_emailNotFoundInDatabase',NULL),
	(2065,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'C8CF698DA55B4E66A7B317A99D797476','lab','label','Submit',NULL,'en_US','joinAnotherLab_submit',NULL),
	(2066,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'B0DD38799FD14236ACD008DCBC366E07','lab','label','Your request to join another lab has been recorded. An email has been sent to the lab PI for approval.',NULL,'en_US','joinAnotherLab_requestSuccess',NULL),
	(2067,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'4B98B3817FDC4AEDB61DDB4C01614645','lab','label','To reactive your access to this lab, please contact the PI of the lab. They are able to directly re-activate your access to this lab.',NULL,'en_US','joinAnotherLab_requestToReactivateLabMember',NULL),
	(2068,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'7EA0D291AFD445C3A5F38E596DC8051E','lab','error','An unexpected error occurred while processing your request. Please try again.',NULL,'en_US','joinAnotherLab_unexpectedError',NULL),
	(2069,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'5F5D8A4A02C34B249CC07BFF748042B7','lab','error','You are already a member of this lab.',NULL,'en_US','joinAnotherLab_userIsLabMember',NULL),
	(2070,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'8F9F356F2EF847D7816012511A6BE0EE','lab','error','You have already applied to join this lab. Your request is still pending approval by the lab PI.',NULL,'en_US','joinAnotherLab_userIsLabMemberPending',NULL),
	(2071,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'6E5971A1A4CE47268A63FE079E885A4C','lab','error','Please correct the errors indicated below and re-submit.',NULL,'en_US','upgradeStatusToPI_fixErrorsInForm',NULL),
	(2072,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'20B01EB2B340484A8879707E5603B377','lab','label','Complete the form below to upgrade your WASP status to include Principal Investigator of your own lab.',NULL,'en_US','upgradeStatusToPI_instructions',NULL),
	(2073,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'00E8A40BC3AD495CA683764C15944A25','lab','label','Note that this request is subject to WASP administrator confirmation.',NULL,'en_US','upgradeStatusToPI_instructionsWarning',NULL),
	(2074,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'69AFD8BCD58D4E1A9AF48E78AA16AE83','lab','label','Your request to become a Principal Investigator of your own lab has been received and will be reviewed. ',NULL,'en_US','upgradeStatusToPI_requestSuccess',NULL),
	(2075,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'AA72915385744C3AB48E2492E426CB68','lab','error','The WASP database indicates that you are already a PI of a lab. You many NOT apply to be the PI of more than one lab.',NULL,'en_US','upgradeStatusToPI_userIsAlreadyPI',NULL),
	(2076,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'5B1155BAC17142CCA0032CE94C75387D','lab','error','The WASP database indicates that you have already applied to become a PI. Your request has not yet been reviewed.',NULL,'en_US','upgradeStatusToPI_userIsAlreadyPIPending',NULL),
	(2077,'2013-03-12 12:39:33','2013-03-12 12:39:33',X'9B2344339A2D4827965F6A229B8CDE9C','lab','error','An unexpected error occurred while processing your request. Please try again.',NULL,'en_US','upgradeStatusToPI_unexpectedError',NULL),
	(2078,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'AE91C6B1950F41D0B0399ED1E854AB49','labPending','error','Problem updating metadata',NULL,'en_US','created_meta',NULL),
	(2079,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'8A6AAEDF569D42CDB4F21FBC76877294','labPending','error','Update Failed: You MUST provide a reason for rejecting this request',NULL,'en_US','rejectedPIMustHaveComment',NULL),
	(2080,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'CD3061570CB24290BCCE9ED3BF855EAC','labuser','label','Alter Status',NULL,'en_US','change_status_in_lab',NULL),
	(2081,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'D61D02B00A0E471895782EE49071EBB3','labuser','label','Former Lab Members',NULL,'en_US','former',NULL),
	(2082,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'11C137F198104F7F87BB9E4A35C1E6C9','labuser','error','Lab not found in database.',NULL,'en_US','labNotFound',NULL),
	(2083,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'0B9F9B16A4ED44819E3A0A044F92B903','labuser','label','Active<br />Lab Member',NULL,'en_US','userManager_activeLabMember',NULL),
	(2084,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'A830EB2BD18A4184878A2A27DEA3C360','labuser','label','Approve Or Reject',NULL,'en_US','approveOrReject',NULL),
	(2085,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'AAF77FFB78494656ACB6CC300710E29C','labuser','label','Alter Status',NULL,'en_US','userManager_change_status',NULL),
	(2086,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'A0AF3FCB064D4FAB9DF28A3E4DC19EC8','labuser','label','Current &amp; Former Lab Members',NULL,'en_US','userManager_currentAnFormerLabMembers',NULL),
	(2087,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'B261E19107334ADC9EC262A376FD4FEB','labuser','label','Downgrade From Manager To Lab Member',NULL,'en_US','userManager_dowgradeFromManagerToLabMember',NULL),
	(2088,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'A5E606B02A46462594673B4F8A596523','labuser','label','Email',NULL,'en_US','userManager_email',NULL),
	(2089,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'E996ED0B528443BB94857A9C991C8135','labuser','label','Hide Lab Member Rights / Responsibilities',NULL,'en_US','userManager_hideRandR',NULL),
	(2090,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'1E92FBA3C0914CA297B8F6FAC004D5B1','labuser','label','Inactive<br />Lab Member',NULL,'en_US','userManager_inactiveLabMember',NULL),
	(2091,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'E291FA72FC1F41DC9276C83C2DC4077A','labuser','label','Inactivate Lab Membership',NULL,'en_US','userManager_inactivateLabMembership',NULL),
	(2092,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'B46D8F849CD6486DA2B5F20807945C89','labuser','label','Lab<br />Manager',NULL,'en_US','userManager_labManager',NULL),
	(2093,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'A088F580FDD34DA395C4ED57A40003C1','labUser','error','Update Failed: Lab unexpectedly not found in database',NULL,'en_US','userManager_labNotFound',NULL),
	(2094,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'96D127A01D814D9A864077FA12358C90','labUser','label','Update Successful',NULL,'en_US','userManager_labRoleSuccessfullyUpdated',NULL),
	(2095,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'675F904E9B024C48AF7102182185CDED','labuser','label','Name',NULL,'en_US','userManager_name',NULL),
	(2096,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'CB19DB5B7427438DAF3C229417C70BFC','labuser','label','Principal<br />Investigator',NULL,'en_US','userManager_pi',NULL),
	(2097,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'51D16EE534B1444ABC3D5A460B15684A','labuser','label','<b>Note To Lab PI</b>: Below you can assign one or more members of your lab to function as a WASP lab manager, permitting them to:<br />--Approve / Reject new WASP lab members to your lab<br />--Approve / Reject WASP job submissions from your lab members<br />--Inactivate WASP lab members so they can no longer submit jobs from your lab',NULL,'en_US','userManager_piInstructions',NULL),
	(2098,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'5B25035C80CF498494449C33D3BCD3C8','labuser','label','Principal Investigator',NULL,'en_US','userManager_principalInvestigator',NULL),
	(2099,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'E50BC4162FBA4BB7B5861C05085FB413','labuser','label','Reinstate Lab Membership',NULL,'en_US','userManager_reinstate',NULL),
	(2100,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'63712FD3713648FFBE7DB5382CDE8EB1','labuser','label','Reinstate &amp; Upgrade To Lab Manager',NULL,'en_US','userManager_reinstateAndSetToManager',NULL),
	(2101,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'1F65959F441A40E6A87BD11D6B1CC582','labuser','label','View Ones Own Jobs &amp; Data',NULL,'en_US','userManager_right1',NULL),
	(2102,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'349283198CEA43C19EA06F5DE050C0A6','labuser','label','Grant Access To Others To View Ones Own Data',NULL,'en_US','userManager_right2',NULL),
	(2103,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'B16787B752E342F0A35F408F0E2EF6A0','labuser','label','Submit Job Requests From This Lab',NULL,'en_US','userManager_right3',NULL),
	(2104,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'D01470ECA27F4409B854E51DB5ED6441','labuser','label','Approve/Reject Job Requests',NULL,'en_US','userManager_right4',NULL),
	(2105,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'F44D8F49570D42738C50DA776168BFA4','labuser','label','Approve/Reject New Lab Members',NULL,'en_US','userManager_right5',NULL),
	(2106,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'4C38EE5DD38441C58F7FDB6B5EF572D1','labuser','label','Inactivate Lab Members (to prevent future job submissions from this lab)',NULL,'en_US','userManager_right6',NULL),
	(2107,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'7A9D224AE48F47F18A6439C7BEEFD1B1','labuser','label','Upgrade Member To Manager; Downgrade Manager to Member',NULL,'en_US','userManager_right7',NULL),
	(2108,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'3FBC50C91FBF4834BA8108EDDDFB95BF','labuser','label','View Jobs &amp; Data From All Current And Former Lab Members',NULL,'en_US','userManager_right8',NULL),
	(2109,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'665BFB59BF214748BCE9BD713A0DF624','labuser','label','Grant Access To Others To View Data Belonging To Any Current/Former Lab Member',NULL,'en_US','userManager_right9',NULL),
	(2110,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'6A606A6B6E1E47C182C13D23561D8669','labuser','label','WASP Lab Member Rights<br />And Responsibilities',NULL,'en_US','userManager_rightsResponsibilities',NULL),
	(2111,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'CF6EE4F1B4114E4991E0EF9ABE54036F','labUser','error','Update Failed: Selected role unexpectedly not found in database',NULL,'en_US','userManager_roleNotFound',NULL),
	(2112,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'1AC0EA2B1C2A49749934DAED0FFB078B','labuser','label','Show Lab Member Rights / Responsibilities',NULL,'en_US','userManager_showRandR',NULL),
	(2113,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'7D0D5A9297BB48F4B5901D21C3AA578A','labUser','error','Update Failed: Unexpected inability to save change to database',NULL,'en_US','userManager_updateUnexpectedlyFailed',NULL),
	(2114,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'6D9A2BD013884D7AA6523C67F8006A77','labuser','label','Upgrade To Lab Manager',NULL,'en_US','userManager_upgradeToManager',NULL),
	(2115,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'20941C6C4489478D8D288AFEDD2F075D','labUser','error','Update Failed: User unexpectedly not found in database',NULL,'en_US','userManager_userNotFound',NULL),
	(2116,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'52296FFD7F9D4C56A9690C478C10407D','labUser','error','Update Failed: User unexpectedly not in this lab, according to our the database',NULL,'en_US','userManager_userNotInLab',NULL),
	(2117,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'7B5E02E7FB2D4B6D9266B5F6D13FB64A','labuser','label','Users Requesting To Join This Lab',NULL,'en_US','userManager_usersRequestingToJoinThisLab',NULL),
	(2118,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'12E414FE521E4E09B7DFEBD2C2359AAF','labuser','label','Status',NULL,'en_US','userManager_status',NULL),
	(2119,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'72301EB1799D449BA036C31269316129','lmpendingtask','label','Please Select Approve Or Reject',NULL,'en_US','approveRejectAlert',NULL),
	(2120,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'02CB4A78AD9F4013AE734868147364EE','lmpendingtask','error','Update Failed: You Must Provide A Reason For Rejecting This Job',NULL,'en_US','commentEmpty',NULL),
	(2121,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'34B441C3334C4AD1847DAF1EE434996E','lmpendingtask','error','Update Failed: Invalid Action',NULL,'en_US','invalidAction',NULL),
	(2122,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'F271A5B1E4464E79AB3A3AC6F1302805','lmpendingtask','error','Update Failed: Job Not Found',NULL,'en_US','invalidJob',NULL),
	(2123,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'795DC172EC1D4054916EE77780B18276','lmpendingtask','label','Job Has Been Approved',NULL,'en_US','jobApproved',NULL),
	(2124,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'F2A03FBAA00E4F9A83DED4B50BFA47ED','lmpendingtask','label','Job Has Been Rejected',NULL,'en_US','jobRejected',NULL),
	(2125,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'A60B91A4707040068D7DB44453309051','lmpendingtask','label','RESET',NULL,'en_US','reset',NULL),
	(2126,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'A1577146BE034B6B905E117F4989B015','lmpendingtask','label','SUBMIT',NULL,'en_US','submit',NULL),
	(2127,'2013-03-12 12:39:34','2013-03-12 12:39:34',X'F640DDE5AAAF49EBA7B97976E5B7D0D4','lmpendingtask','error','Update Unexpectedly Failed',NULL,'en_US','updateFailed',NULL),
	(2128,'2013-03-12 12:39:34','2013-03-12 12:39:35',X'E33450498E6E48B1870734D471883F95','lmpendingtask','label','Please provide a reason for rejecting this job',NULL,'en_US','validateCommentAlert',NULL),
	(2129,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'CEEE0DB85B3146DBADCD1ADE7BF8F9EC','menu','label','Assign Libraries',NULL,'en_US','assignLibrariesToPlatformUnits',NULL),
	(2130,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'59965211EF814EAEB59CE85A244E8E05','menu','label','Admin',NULL,'en_US','admin',NULL),
	(2131,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'5D5964D0724B4A2F9EACC177CE3FFE21','menu','label','All Jobs',NULL,'en_US','allJobs',NULL),
	(2132,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'6712CD71951A4300947F2C10E522A35A','menu','label','All Samples',NULL,'en_US','allSamples',NULL),
	(2133,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'699CDF73F2934C8CA1208E7A956A15BF','menu','label','Bisulfate-Seq',NULL,'en_US','bisulfateseq',NULL),
	(2134,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'6055075C5CE44B63A0D6E7CD97B54248','menu','label','Change Password',NULL,'en_US','changePassword',NULL),
	(2135,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'85789794706945799E89D6E5B9DCCF1A','menu','label','ChIP-Seq',NULL,'en_US','chipseq',NULL),
	(2136,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'3D20E8AF483A472882017B6ECD0EA906','menu','label','Control Libraries',NULL,'en_US','controlLibraries',NULL),
	(2137,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'2F9E3F0EDAD8410B9EC8A9441064F992','menu','label','Department Admin',NULL,'en_US','deptAdmin',NULL),
	(2138,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'E445546A978B42A5B544F99979903434','menu','label','Dept. Admin. Tasks',NULL,'en_US','deptAdminTasks',NULL),
	(2139,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'208B2E3AF7804DEE92F2D295C067B91A','menu','label','Facility',NULL,'en_US','facility',NULL),
	(2140,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'2165F6FB94704D20AB1A0F9BA47EEECE','menu','label','HELP Tag',NULL,'en_US','helptag',NULL),
	(2141,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'1F456F0B65854536A9FFE46461DCDE8F','menu','label','Home',NULL,'en_US','home',NULL),
	(2142,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'F47155C5FEB0455AAF2F738FD58258BE','menu','label','Illumina',NULL,'en_US','illumina',NULL),
	(2143,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'544C78B565EF44B58994500D8619958C','menu','label','Quotes',NULL,'en_US','jobQuotes',NULL),
	(2144,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'976E834F9E984C3A8317183464062A44','menu','label','Jobs',NULL,'en_US','jobs',NULL),
	(2145,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'BC282109660E47F28F2AB8FAB904181F','menu','label','Join Another Lab',NULL,'en_US','joinAnotherLab',NULL),
	(2146,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'649974C526A34A72950227A1AC1E61D3','menu','label','Labs',NULL,'en_US','labs',NULL),
	(2147,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'BDE16CD6C1604D12BA73A2815B13E7DE','menu','label','Lab PI',NULL,'en_US','labPI',NULL),
	(2148,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'8BFA9AF0AC5E44A8A597D0CE5649F298','menu','label','Logout',NULL,'en_US','logout',NULL),
	(2149,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'6664EC1332BE4C78AC0C1E557379230F','menu','label','Platform Units',NULL,'en_US','listPlatformUnits',NULL),
	(2150,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'5383D17B9DA84A08BE3D49123905F590','menu','label','Machines',NULL,'en_US','machines',NULL),
	(2151,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'9ECC29AD8FA5487CBBE195B36C65CF7C','menu','label','My Job Drafts',NULL,'en_US','myJobDrafts',NULL),
	(2152,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'0D97558173A24C938910D2BE6A4A6334','menu','label','My Profile',NULL,'en_US','myProfile',NULL),
	(2153,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'57F4D4BA6BB84825BE9A2DF880D5D169','menu','label','My Role In Lab',NULL,'en_US','myRoleInLab',NULL),
	(2154,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'54198CE523C94C38928051511A8AF8A5','menu','label','New Platform Unit',NULL,'en_US','newPlatformUnits',NULL),
	(2155,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'D7FF2797E1634B0B932DFE251730A857','menu','label','Pending Approval',NULL,'en_US','pendingApproval',NULL),
	(2156,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'82F68E5302DB455BA9261228B0F1D7E2','menu','label','PI/Manager Tasks',NULL,'en_US','piAndLabManagerTasks',NULL),
	(2157,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'A3230729E0EB492C9DD30D7D74B7C13E','menu','label','Platform Units',NULL,'en_US','platformUnits',NULL),
	(2158,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'F4FFCD863CC043F68CE8C574116C7278','menu','label','Web Plugins',NULL,'en_US','webPlugins',NULL),
	(2159,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'31B03748C71F41B99E1E9366C4F18EA9','menu','label','Regular Users',NULL,'en_US','regularUsers',NULL),
	(2160,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'EE36D3CA6F93477DB70F3D77B21B20D5','menu','label','My Submitted Jobs',NULL,'en_US','mySubmittedJobs',NULL),
	(2161,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'1B1F50079CB74B4DA707C40E779004D1','menu','label','Samples',NULL,'en_US','samples',NULL),
	(2162,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'A0722B08C2B54D27A1567CF1A0A4C826','menu','label','Sample Receiver',NULL,'en_US','sampleReceiver',NULL),
	(2163,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'743A9235921F4A2AAE682AC01AF5A70F','menu','label','Sequencers',NULL,'en_US','sequencers',NULL),
	(2164,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'540259394E8E48B0B0C0F9B70E7F7BF5','menu','label','Sequence Runs',NULL,'en_US','sequenceRuns',NULL),
	(2165,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'67C67E05B11D4EF8912AFF785FD27C8C','menu','label','Submit A New Job',NULL,'en_US','submitNewJob',NULL),
	(2166,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'838BC44DE530489C9EDAD8C0FEB1DBB6','menu','label','System Users',NULL,'en_US','systemUsers',NULL),
	(2167,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'5F8FF20501C6405A85CD6048E31E5F05','menu','label','Tasks',NULL,'en_US','tasks',NULL),
	(2168,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'B68D882EDDB5433F8F1E6011C4FD3F7B','menu','label','Tasks For Others',NULL,'en_US','tasksForOthers',NULL),
	(2169,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'CEFFD5284AC4445096F49E222EA5F9A2','menu','label','Upgrade To Lab PI',NULL,'en_US','upgradeToPI',NULL),
	(2170,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'7994DCB7EF204D228331063DFDE3F965','menu','label','User',NULL,'en_US','user',NULL),
	(2171,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'0080F68F4D6B4846A781D0430605CAA0','menu','label','Users',NULL,'en_US','users',NULL),
	(2172,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'5253F28C24E8453EB5043E03E3ED8A08','menu','label','Workflows',NULL,'en_US','workflows',NULL),
	(2173,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'7C5C9E76FE6E4E40B3A2A5AE4451D2F1','pageTitle','label','Access Denied',NULL,'en_US','auth/accessdenied',NULL),
	(2174,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'0073DAB12D4B4CC2B12B759A699FA70C','pageTitle','label','Analysis Parameters',NULL,'en_US','job/analysisParameters',NULL),
	(2175,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'BB2C5EBEA7184BF19B5D03C0F260072E','pageTitle','label','Job Results',NULL,'en_US','jobresults/treeview',NULL),
	(2176,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'9D09C73646BE43E5808F4D52513584AF','pageTitle','label','Join Another Lab',NULL,'en_US','lab/joinAnotherLab/form',NULL),
	(2177,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'88D7FBD76BED45A1944CBD554039F096','pageTitle','label','Upgrade Status To Principal Investigator',NULL,'en_US','lab/upgradeStatusToPI/form',NULL),
	(2178,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'DE34388E843444C39E9DF9D6054D1B10','pageTitle','label','My Labs',NULL,'en_US','lab/viewerLabList',NULL),
	(2179,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'75C8A9A2755A47A9AF7B3616C20F16C0','pageTitle','label','Web Enabled Plugins',NULL,'en_US','plugin/list',NULL),
	(2180,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'78BB60830FD14C3596630FFD6F5DD922','pageTitle','label','Cell-Library QC Manager',NULL,'en_US','task/cellLibraryQC/list',NULL),
	(2181,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'F5FB960D8C8047B8820BAE18A34C9A3C','pageTitle','label','Department Administrator Pending Tasks',NULL,'en_US','task/daapprove/list',NULL),
	(2182,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'2FFCAD97C188449BAB9E8A8C2DC9FA22','pageTitle','label','Facility Manager Pending Tasks',NULL,'en_US','task/fmapprove/list',NULL),
	(2183,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'D03C98F7399A49F2B460FB45E4E7A41B','pageTitle','label','Tasks',NULL,'en_US','task/myTaskList',NULL),
	(2184,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'173D80AA338E43D5B6B8E54775960E12','pageTitle','label','PI/Lab Manager Pending Tasks',NULL,'en_US','task/piapprove/list',NULL),
	(2185,'2013-03-12 12:39:35','2013-03-12 12:39:35',X'E1890164CD174792BFC5D490369B40D3','piPending','label','Register As A New WASP Principal Investigator',NULL,'en_US','form_header',NULL),
	(2186,'2013-03-12 12:39:36','2013-03-12 12:39:36',X'6F5A1B692CDD457BBF8EF8FAB198DF85','piPending','label','Password Requirements: Minimum of 8 characters, letters &amp; numbers only (no spaces, no underscore, etc), at least one letter and one number',NULL,'en_US','password_instructions',NULL),
	(2187,'2013-03-12 12:39:36','2013-03-12 12:39:36',X'04F579A262FD4D05B801601AC0E4FB1D','piPending','label','See password requirements above',NULL,'en_US','password_instructions_above',NULL),
	(2188,'2013-03-12 12:39:36','2013-03-12 12:39:36',X'B59DBD8B2E4F4A8C8D1152FEBE599D4A','platformunit','error','Problem occurred locking platform unit',NULL,'en_US','locking',NULL),
	(2189,'2013-03-12 12:39:36','2013-03-12 12:39:36',X'9875E71B71B6492BB7CACF7DF933A8D0','plugin','label','Web Enabled Plugins',NULL,'en_US','list',NULL),
	(2190,'2013-03-12 12:39:36','2013-03-12 12:39:36',X'B8DB2A2F22B34DBF83378B047C9CA502','plugin','label','No plugins to display',NULL,'en_US','none',NULL),
	(2191,'2013-03-12 12:39:36','2013-03-12 12:39:36',X'F90C27DF9DF34D63BCED3F77D5B4E09A','resource','error','Problem occurred updateing metadata',NULL,'en_US','updated_meta',NULL),
	(2192,'2013-03-12 12:39:36','2013-03-12 12:39:36',X'985211DDC8344D65B2DD6E2A52FF9400','run','label','Run created successfully.',NULL,'en_US','created_success',NULL),
	(2193,'2013-03-12 12:39:36','2013-03-12 12:39:36',X'6643AAEA7A7B4CDA9363E1B452920EBC','run','label','Run was NOT successfully created.',NULL,'en_US','created_failure',NULL),
	(2194,'2013-03-12 12:39:37','2013-03-12 12:39:37',X'DCFB381FF8374F5894C85D1F6498B491','run','error','Invalid run ID provided',NULL,'en_US','invalid_id',NULL),
	(2195,'2013-03-12 12:39:37','2013-03-12 12:39:37',X'297BB735EB7846F2B1AD7AA97AB3FBDE','status','label','PI Approval',NULL,'en_US','piApprove',NULL),
	(2196,'2013-03-12 12:39:37','2013-03-12 12:39:37',X'4A96F455C67846F6ACDE65CC1AEB6E3C','status','label','Dept. Admin. Approval',NULL,'en_US','daApprove',NULL),
	(2197,'2013-03-12 12:39:37','2013-03-12 12:39:37',X'B93B448241B94187917814DFE5016A79','status','label','Seq. Facility Approval',NULL,'en_US','fmApprove',NULL),
	(2198,'2013-03-12 12:39:37','2013-03-12 12:39:37',X'6A323CD953E94695853101424578F060','task','error','Update Failed: Aggregate Analysis already underway',NULL,'en_US','cellLibraryqc_aggregateAnalysisAlreadyUnderway',NULL),
	(2199,'2013-03-12 12:39:37','2013-03-12 12:39:37',X'896950DBCDE34745B2A87ADBCD0FEA08','task','label','Exclude',NULL,'en_US','cellLibraryqc_exclude',NULL),
	(2200,'2013-03-12 12:39:37','2013-03-12 12:39:37',X'4E0502A196D245C28D9E626E08659F64','task','label','For Aggregate Analysis',NULL,'en_US','cellLibraryqc_forAggregateAnalysis',NULL),
	(2201,'2013-03-12 12:39:37','2013-03-12 12:39:37',X'992101AB5A78445B9D80539AFD7F2919','task','error','Update Failed: Unexpected absence of comment(s)',NULL,'en_US','cellLibraryqc_comment_not_sent',NULL),
	(2202,'2013-03-12 12:39:37','2013-03-12 12:39:37',X'97488E99685A4774A5C0F6E8081084FA','task','error','Updaate Failed: Please provide an explanation for this exclusion',NULL,'en_US','cellLibraryqc_comment_empty',NULL),
	(2203,'2013-03-12 12:39:37','2013-03-12 12:39:37',X'FAB3C8B8E94A4EAEA751F6CFD4E91A0F','task','error','One Or More Updates Failed: You must provide an explanation for any exclusion',NULL,'en_US','cellLibraryqc_excludeRequiresComment',NULL),
	(2204,'2013-03-12 12:39:37','2013-03-12 12:39:37',X'AB8E7937DC0C40FDA82B4994D4EA8439','task','label','JobID',NULL,'en_US','cellLibraryqc_jobId',NULL),
	(2205,'2013-03-12 12:39:37','2013-03-12 12:39:37',X'90E9568FEAAB423CB186F421124BA760','task','label','Job Name',NULL,'en_US','cellLibraryqc_jobName',NULL),
	(2206,'2013-03-12 12:39:37','2013-03-12 12:39:37',X'432B709DD146489FB789DBB2C6417F1F','task','error','Update Failed: Job not found in database',NULL,'en_US','cellLibraryqc_jobNotFound',NULL),
	(2207,'2013-03-12 12:39:37','2013-03-12 12:39:37',X'20EF42024869461A836532E9C9547386','task','error','Update Failed: This job has already been terminated',NULL,'en_US','cellLibraryqc_jobPreviouslyTerminated',NULL),
	(2208,'2013-03-12 12:39:37','2013-03-12 12:39:37',X'E913A46FB4E74708A72CA99B10318D26','task','label','Include',NULL,'en_US','cellLibraryqc_include',NULL),
	(2209,'2013-03-12 12:39:37','2013-03-12 12:39:37',X'07EC4741D165405890527AF1AC5A1D47','task','error','Update Failed: Unexpected absence of jobId information',NULL,'en_US','cellLibraryqc_invalid_jobId',NULL),
	(2210,'2013-03-12 12:39:37','2013-03-12 12:39:37',X'1B8E842851654858BEFF27FBB7DC06AB','task','error','Update Failed: Only Include or Exclude are permitted',NULL,'en_US','cellLibraryqc_invalid_qcStatus_value',NULL),
	(2211,'2013-03-12 12:39:37','2013-03-12 12:39:37',X'EE90708236484FCEB1CEBB7C0A0F5338','task','error','Update Failed: Unexpected absence of library-run information',NULL,'en_US','cellLibraryqc_invalid_samplesource',NULL),
	(2212,'2013-03-12 12:39:37','2013-03-12 12:39:37',X'DD9CB6A1BAA345BFAED6F30D6A310C23','task','error','Update Failed: Please tell us if we should initiate analysis',NULL,'en_US','cellLibraryqc_invalid_startAnalysis',NULL),
	(2213,'2013-03-12 12:39:37','2013-03-12 12:39:37',X'AD2C8413B9874EECB50556423B47894B','task','error','Update Failed: Unexpectedly obtained an invalid response for starting the analysis',NULL,'en_US','cellLibraryqc_invalidValues_startAnalysis',NULL),
	(2214,'2013-03-12 12:39:37','2013-03-12 12:39:37',X'ADACA93397F84EF28FB80811A36DDB66','task','label','Library',NULL,'en_US','cellLibraryqc_library',NULL),
	(2215,'2013-03-12 12:39:37','2013-03-12 12:39:37',X'21C42026152D41B9A5F0393CDE3655A9','task','error','One Or More Updates Failed: Unexpected problem occurred while saving new record',NULL,'en_US','cellLibraryqc_message',NULL),
	(2216,'2013-03-12 12:39:37','2013-03-12 12:39:37',X'A823CD237B7644B2BA215B30A86A503B','task','error','No Library-run selected',NULL,'en_US','cellLibraryqc_noRecordsSelected',NULL),
	(2217,'2013-03-12 12:39:37','2013-03-12 12:39:37',X'3B112A01736C47DCB6952633CEA9ABB1','task','label','In order to submit, you must select Include or Exclude for at least one library-run',NULL,'en_US','cellLibraryqc_noLibRunSelectedAlert',NULL),
	(2218,'2013-03-12 12:39:37','2013-03-12 12:39:38',X'59FE286F65DC41CD98878CF1D2EF0E43','task','label','Platform Unit / Sequence Run',NULL,'en_US','cellLibraryqc_pu_seqrun',NULL),
	(2219,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'1B34D30742A6442D898AB567750ECB30','task','error','Update Failed: You must select either Include or Exclude',NULL,'en_US','cellLibraryqc_qcStatus_invalid',NULL),
	(2220,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'53455B0AAEEF431F899FB8C50F08861A','task','label','Reset',NULL,'en_US','cellLibraryqc_reset',NULL),
	(2221,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'8D0140D3CE134F95BCC459751A596341','task','label','Sample',NULL,'en_US','cellLibraryqc_sample',NULL),
	(2222,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'35DB2CE6E80E43399505BB51CDA1E48F','task','error','Update Failed: Unable to locate proper library-run record(s) in database',NULL,'en_US','cellLibraryqc_sampleSourceNotFound',NULL),
	(2223,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'0843334585A549F38B1E38A9A0AE6E11','task','label','set all exclude',NULL,'en_US','cellLibraryqc_setAllExclude',NULL),
	(2224,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'1964977F7A8F4D4A9248B98893EA566A','task','label','set all include',NULL,'en_US','cellLibraryqc_setAllInclude',NULL),
	(2225,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'BFCA760B102246E9AE586D47C8A2D25A','task','label','Later',NULL,'en_US','cellLibraryqc_startAnalysisLater',NULL),
	(2226,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'E1FC7E717E6C46958DD1C75D4B87F5B5','task','label','Never (terminate job)',NULL,'en_US','cellLibraryqc_startAnalysisNever',NULL),
	(2227,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'A88290904CB1454AAE4CC6F98BAB66C7','task','error','Unable to initiate aggregate analysis until ALL records are marked as either Include or Exclude AND at least one record is marked as Include.',NULL,'en_US','cellLibraryqc_startAnalysisNotPossibleNow',NULL),
	(2228,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'A23A17E8F4CF453980745C58C8F4317D','task','label','Please tell us if you want to start the analysis now.\\n\\n(Note that if you want to start the analysis for this job now,\\nyou must select Include or Exclude for each entry.)',NULL,'en_US','cellLibraryqc_startAnalysisNotSelectedAlert',NULL),
	(2229,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'416FC0C588C2449797AE1E915975BCE2','task','label','Now',NULL,'en_US','cellLibraryqc_startAnalysisNow',NULL),
	(2230,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'DAA0004DB4C043E7A2AE1A40440D3B5B','task','label','--Start Analysis?--',NULL,'en_US','cellLibraryqc_startAnalysisQuestion',NULL),
	(2231,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'F4EF3A0C76B14F6D866770B505253589','task','label','To start the analysis for this job, you must select Include for at least one entry',NULL,'en_US','cellLibraryqc_startAnalysisRequestedWithoutAtLeastOneInclude',NULL),
	(2232,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'48E7C40953CB49C3B1A9F56CFE1B5617','task','label','To start the analysis for this job, you must select Include or Exclude for each entry',NULL,'en_US','cellLibraryqc_startAnalysisRequestedWithoutRecordingEachRecord',NULL),
	(2233,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'055F0C2D7B2140CD980569B6CAC0F63E','task','label','Submit',NULL,'en_US','cellLibraryqc_submit',NULL),
	(2234,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'C644DBE98B6B44BCBC8FE6AABF318C24','task','label','Submitter',NULL,'en_US','cellLibraryqc_submitter',NULL),
	(2235,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'4869B4005B1B43BDA750DE824373B6BE','task','label','To terminate the job without analysis, you must mark each record as Exclude and provide a reason for exclusion',NULL,'en_US','cellLibraryqc_terminateJobRequestedWithoutRecordingExcludeForEachRecord',NULL),
	(2236,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'97A1D2375FF3470FA324C40F7B55DB25','task','error','Unable to terminate job until ALL records are marked as Exclude and a reason for exclusion is provided for each',NULL,'en_US','cellLibraryqc_terminateJobNotPossibleNow',NULL),
	(2237,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'6A619A51A3C54CE1B44ED8E680F10D6E','task','label','Update completed successfully',NULL,'en_US','cellLibraryqc_update_success',NULL),
	(2238,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'EA6508050EF44E6CB15D494F2C3482EA','task','label','If you exclude any library-run from analysis, please provide a reason',NULL,'en_US','cellLibraryqc_validateCommentAlert',NULL),
	(2239,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'7513A023079C4EB8A272EEE5C98D79BD','task','label','Please select either Include or Exclude',NULL,'en_US','cellLibraryqc_validatePassFailAlert',NULL),
	(2240,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'13A6EACB523C4E42B56C3011A9CDC6F9','task','label','Facility Manager Tasks',NULL,'en_US','facilityManager',NULL),
	(2241,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'7801C5DD35FB49408EBC67EBB8A0C70C','task','label','Jobs Pending Approval for Analysis',NULL,'en_US','initiateAnalysis_title',NULL),
	(2242,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'AAF8779169514ECAB11B871ABE724988','task','label','WASP has determined that the tasks below require your attention. Please click on the links to address them.',NULL,'en_US','instructions2',NULL),
	(2243,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'4968386B767142E9A9F928D5B27BAE93','task','error','Job Termination command unexpectedly failed',NULL,'en_US','cellLibraryqc_terminateJobUnexpectedlyFailed',NULL),
	(2244,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'452EDF0E225544DFBCF0BD0F46AEB208','task','label','Update completed successfully AND aggregate analysis begun',NULL,'en_US','cellLibraryqc_updateSuccessfulAndAnalysisBegun',NULL),
	(2245,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'57AC3ED994AD499380112C69EE462AA4','task','label','Update completed successfully AND agreagate analysis NOT begun',NULL,'en_US','cellLibraryqc_updateSuccessfulAndAnalysisNotBegun',NULL),
	(2246,'2013-03-12 12:39:38','2013-03-12 12:39:38',X'0913F7A5518E42F099D62879499E3858','task','label','Update completed successfully AND Job terminated',NULL,'en_US','cellLibraryqc_updateSuccessfulAndJobTerminated',NULL),
	(2247,'2013-03-12 12:39:39','2013-03-12 12:39:39',X'3790AAA2E25A4BBEA718A5367B56A5F8','userPending','label','Register As A New WASP User & Apply To Join An Existing Laboratory',NULL,'en_US','form_header',NULL),
	(2248,'2013-03-12 12:39:39','2013-03-12 12:39:39',X'94C326A2A10C43D792FBEF515D2FE321','userPending','label','No spaces permitted in login',NULL,'en_US','login_instructions_above',NULL),
	(2249,'2013-03-12 12:39:42','2013-03-12 12:39:42',X'D9774F0CCE5D41E0A27305BD263BDC34','pageTitle','label','Wasp Bisulfite-Seq Plugin Description',NULL,'en_US','waspbisulfiteseq/description',NULL),
	(2250,'2013-03-12 12:39:42','2013-03-12 12:39:42',X'B4F0BB0C46044B0E941E28F51B18B05B','waspBisulfiteSeq','label','If you can read this text your plugin is successfully installed',NULL,'en_US','maintext',NULL),
	(2251,'2013-03-12 12:39:42','2013-03-12 12:39:42',X'15892D86ED594425A7129C899E14BEB9','waspBisulfiteSeq','label','Wasp Bisulfite Sequencing Plugin',NULL,'en_US','hyperlink',NULL),
	(2252,'2013-03-12 12:39:42','2013-03-12 12:39:42',X'65D88B8587514323BBA223FC07224EA0','pageTitle','label','Wasp ChIP-Seq Plugin Description',NULL,'en_US','waspchipseq/description',NULL),
	(2253,'2013-03-12 12:39:42','2013-03-12 12:39:42',X'820051E967A1495DB6DEA12F901421B3','waspChipSeq','label','If you can read this text your plugin is successfully installed',NULL,'en_US','maintext',NULL),
	(2254,'2013-03-12 12:39:42','2013-03-12 12:39:42',X'B29F52100F4340C2A9E24D9F9429D823','waspChipSeq','label','Wasp ChIP-Seq Plugin DNA Sequencing Plugin',NULL,'en_US','hyperlink',NULL),
	(2255,'2013-03-12 12:39:42','2013-03-12 12:39:42',X'B6B32634A12044FAB4E9F9614CE785EA','waspGenericDnaSeq','label','If you can read this text your plugin is successfully installed',NULL,'en_US','maintext',NULL),
	(2256,'2013-03-12 12:39:42','2013-03-12 12:39:42',X'D44FDB181D9948FDA06EC8AB2801EA41','waspGenericDnaSeq','label','Wasp Generic DNA Sequencing Plugin',NULL,'en_US','hyperlink',NULL),
	(2257,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'C0658EA9AB1D44BD83B7712C46524175','pageTitle','label','Wasp Help-Tag Plugin Description',NULL,'en_US','wasphelptag/description',NULL),
	(2258,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'0D476DCEB0444D15AFFAA8E883F78089','waspHelpTag','label','If you can read this text your plugin is successfully installed',NULL,'en_US','maintext',NULL),
	(2259,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'1AC7B3C7B56B4543B12A1A083EBE8B66','waspHelpTag','label','Wasp Help Tag Plugin',NULL,'en_US','hyperlink',NULL),
	(2260,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'B1B365C124CA42C59E92B82FED016B1A','pageTitle','label','Wasp Illumina Plugin Description',NULL,'en_US','waspillumina/description',NULL),
	(2261,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'66DF41EBD7234B8D96262255C12CFC89','waspIlluminaPlugin','label','If you can read this text your plugin is successfully installed',NULL,'en_US','maintext',NULL),
	(2262,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'45C0867592454E3CB9801DEC78D07779','waspIlluminaPlugin','label','Illumina Run QC',NULL,'en_US','taskRunQc',NULL),
	(2263,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'6CADBB2B1F394A748262D9FE28A87A65','waspIlluminaPlugin','label','Wasp Illumina Plugin',NULL,'en_US','hyperlink',NULL),
	(2264,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'07408F675E9442098D509D604F3E96C0','pageTitle','label','Wasp Illumina QC tasks',NULL,'en_US','wasp-illumina/postrunqc/list',NULL),
	(2265,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'2E519699B59D47879DFFFB28973E16A9','pageTitle','label','Illumina OLB Stats: Focus Quality Charts',NULL,'en_US','wasp-illumina/postrunqc/displayfocusqualitycharts',NULL),
	(2266,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'F99BD44E54374E6CA221AE2871248CDF','pageTitle','label','Illumina OLB Stats: Intensity Charts',NULL,'en_US','wasp-illumina/postrunqc/displayinstensitycharts',NULL),
	(2267,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'9D35EBABF6C44681AB3D3D3261030934','pageTitle','label','Illumina OLB Stats: Pass Filter Base Quality Charts',NULL,'en_US','wasp-illumina/postrunqc/displaynumgt30charts',NULL),
	(2268,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'9CAE688F96C247468D3F60DC4261FC9E','pageTitle','label','Illumina OLB Stats: Cluster Density Charts',NULL,'en_US','wasp-illumina/postrunqc/displayclusterdensitychart',NULL),
	(2269,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'9676168E578E42AFAAC128C99E0EACDF','pageTitle','label','Illumina OLB Stats: Finalize Validation',NULL,'en_US','wasp-illumina/postrunqc/updatequalityreport',NULL),
	(2270,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'6BC09BBBED8A426EABDFD26A3C456172','waspIlluminaPlugin','error','There was a problem interpreting the posted form parameters',NULL,'en_US','formParameter',NULL),
	(2271,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'076ECA66BB8D4FEAA110F2856DEE5B2C','waspIlluminaPlugin','error','Unable to retrieve lanes from flowcell',NULL,'en_US','puCells',NULL),
	(2272,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'3CE6508BAC12475F8DC1BDB336B3FBDE','waspIlluminaPlugin','error','Failed to update database with QC data',NULL,'en_US','update',NULL),
	(2273,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'90B6C547041C47558795AAFCCF4BF346','waspIlluminaPlugin','error','Failed get all QC data from database',NULL,'en_US','qcRetrieval',NULL),
	(2274,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'F6CED473AAAB4BD59DD583AE029D0A6C','waspIlluminaPlugin','label','Lane',NULL,'en_US','updateQualityReport_lane',NULL),
	(2275,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'90C8D6A4EBD94500B917D49081AF9F8C','waspIlluminaPlugin','label','Focus Quality',NULL,'en_US','updateQualityReport_focus',NULL),
	(2276,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'7AC78693D8874BA6AD6566A16BE7331A','waspIlluminaPlugin','label','Intensity Quality',NULL,'en_US','updateQualityReport_intensity',NULL),
	(2277,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'AE3D8E977FE545A299C6E254F55648E1','waspIlluminaPlugin','label','Pass Filter Base Quality',NULL,'en_US','updateQualityReport_baseQc',NULL),
	(2278,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'82D913327D0B4D6894E674FAEB6D6C8D','waspIlluminaPlugin','label','Cluster Density',NULL,'en_US','updateQualityReport_clusterDensity',NULL),
	(2279,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'FF248AC1F6FB4495BBFC4E7D7408398C','waspIlluminaPlugin','label','Final Decision on Lane',NULL,'en_US','updateQualityReport_finalDescision',NULL),
	(2280,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'5BD92277C9124D5F86A29DD1D6D8A0E8','waspIlluminaPlugin','label','Comments on Decision<br />(will display to users)',NULL,'en_US','updateQualityReport_comments',NULL),
	(2281,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'F1D3A3CD9A904DA883D7BAA608ABF754','waspIlluminaPlugin','label','Run Quality Control Completed Successfully',NULL,'en_US','updateQcsuccess',NULL),
	(2282,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'EA2BCAD8E0694BCA80DFE7C900475244','waspIlluminaPlugin','label','Illumina OLB Stats: Finalize Validation for ',NULL,'en_US','updateQualityReport_title',NULL),
	(2283,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'AF979CE937FB4AE5918263834108475E','waspIlluminaPlugin','label','Please review this summary of your responses in the table below and make a decision whether or not to accept or reject each lane. Note that:<ul><li>Libraries on rejected lanes will not be analysed. </li><li>It is mandatory to write comments on the decision if a lane is rejected.</li><li>The decision and comments will be viewable by users.</li></ul>',NULL,'en_US','updateQualityReport_instructions',NULL),
	(2284,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'125C753B7C674E3AB29C57D64E83D32C','waspIlluminaPlugin','label','Submit Run QC',NULL,'en_US','updateQualityReport_submitButton',NULL),
	(2285,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'3D213E7A600745BD8BB0123E7DA096AC','waspIlluminaPlugin','label','Page Loading',NULL,'en_US','displayQc_loadingTitle',NULL),
	(2286,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'89FCC1E4EA35411C9C8B4C48B27A3B59','waspIlluminaPlugin','label','Warning',NULL,'en_US','displayQc_warningTitle',NULL),
	(2287,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'8EDC508E26984E118F9001F8F2A1B3A1','waspIlluminaPlugin','label','Please be patient while this page loads. All QC charts are being pre-loaded so this may take a few seconds.',NULL,'en_US','displayQc_message',NULL),
	(2288,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'9DC03145A91C423F8F84EDA9A6B63F23','waspIlluminaPlugin','error','You must choose either \"Pass\" or \"Fail\" for ALL lanes. You have not yet scored lanes ',NULL,'en_US','displayQc_noChoose',NULL),
	(2289,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'CEF251A5298C49EB93EDB21F4B27359D','waspIlluminaPlugin','error','You must write a comment for all failed lanes: ',NULL,'en_US','displayQc_noComment',NULL),
	(2290,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'7AA08D88F1864960B0E8AE890BEA0B88','waspIlluminaPlugin','label','Lane ',NULL,'en_US','displayQc_lane',NULL),
	(2291,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'8E8B91B120404CBDAEECBD0686D97A26','waspIlluminaPlugin','label','Comments: ',NULL,'en_US','displayQc_comments',NULL),
	(2292,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'76BB542D9619459EA96D7A0BA66AA476','waspIlluminaPlugin','label','Continue',NULL,'en_US','displayQc_continue',NULL),
	(2293,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'FFDC4D57AA4A4309A96E5C2472D4D908','waspIlluminaPlugin','label','Cancel',NULL,'en_US','displayQc_cancel',NULL),
	(2294,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'F99B67FDBC884ADA96613CC8785D60EE','waspIlluminaPlugin','label','Pass',NULL,'en_US','displayQc_pass',NULL),
	(2295,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'6C3F4C0F48484F858412D87E59225FBF','waspIlluminaPlugin','label','Fail',NULL,'en_US','displayQc_fail',NULL),
	(2296,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'78F873C850C447D8A0E5AA7F79C9E553','waspIlluminaPlugin','label','Cycle Number: ',NULL,'en_US','displayQc_cycle',NULL),
	(2297,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'4154D198F37142268F541C6A9EB36D11','waspIlluminaPlugin','error','<p>You must choose either \"Accept\" or \"Reject\" for ALL lanes. You have not yet scored lanes ',NULL,'en_US','updateQc_noChoose',NULL),
	(2298,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'488609DB04D54D2598A544E605E14554','waspIlluminaPlugin','error','<p>You must write comments for rejected lanes ',NULL,'en_US','updateQc_noComment',NULL),
	(2299,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'AC4B8852288549FD95273CFF9F32DFF9','waspIlluminaPlugin','label','Accept',NULL,'en_US','updateQualityReport_accept',NULL),
	(2300,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'4704B556AE66456F9C02C31E8380069D','waspIlluminaPlugin','label','Reject',NULL,'en_US','updateQualityReport_reject',NULL),
	(2301,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'26CE8A3D3D914EEEB5AA7200B2922DCE','waspIlluminaPlugin','label','Illumina OLB Stats: Focus Quality For ',NULL,'en_US','focusQc_title',NULL),
	(2302,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'7A249E43A49740E49E279891AA77AD84','waspIlluminaPlugin','label','Assessment of Lane Focus Quality',NULL,'en_US','focusQc_dialogTitle',NULL),
	(2303,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'525BCD499EDA491C930FA2C1916CFE8E','waspIlluminaPlugin','label','Please click either \"Pass\" or \"Fail\" for each lane based on your interpretation of the FOCUS QUALITY charts only, then click the \"Continue\" button.',NULL,'en_US','focusQc_dialogMessage',NULL),
	(2304,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'750A4694F21D4387BD14D3FAC4BD092B','waspIlluminaPlugin','label','Illumina OLB Stats: Intensity Charts For ',NULL,'en_US','intensityQc_title',NULL),
	(2305,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'04092DF934AC4B44BBC8848F08FC2EA8','waspIlluminaPlugin','label','Assessment of Lane Intensity',NULL,'en_US','intensityQc_dialogTitle',NULL),
	(2306,'2013-03-12 12:39:43','2013-03-12 12:39:43',X'50AFCFE6EAC24EBD81A195AFCB369B7A','waspIlluminaPlugin','label','Please click either \"Pass\" or \"Fail\" for each lane based on your interpretation of the LANE INTENSITY charts only, then click the \"Continue\" button.',NULL,'en_US','intensityQc_dialogMessage',NULL),
	(2307,'2013-03-12 12:39:44','2013-03-12 12:39:44',X'64F164C54CD5448AB8142AAFD58C3122','waspIlluminaPlugin','label','Illumina OLB Stats: Pass Filter Base Quality Charts For ',NULL,'en_US','numGT30Qc_title',NULL),
	(2308,'2013-03-12 12:39:44','2013-03-12 12:39:44',X'B5975DDE97BC4D1DB2DD785CE22FBF5B','waspIlluminaPlugin','label','Assessment of Base Quality Score',NULL,'en_US','numGT30Qc_dialogTitle',NULL),
	(2309,'2013-03-12 12:39:44','2013-03-12 12:39:44',X'5C2252021A6643E09E4AADDDCB9DC04A','waspIlluminaPlugin','label','Please click either \"Pass\" or \"Fail\" for each lane based on your interpretation of the BASE QUALITY charts only, then click the \"Continue\" button.',NULL,'en_US','numGT30Qc_dialogMessage',NULL),
	(2310,'2013-03-12 12:39:44','2013-03-12 12:39:44',X'3FAF9DC683AD48A1B319C2182FB2B2E1','waspIlluminaPlugin','label','Lane Selector',NULL,'en_US','numGT30Qc_selector',NULL),
	(2311,'2013-03-12 12:39:44','2013-03-12 12:39:44',X'A51D6A56019744B2AC15526C82C11211','waspIlluminaPlugin','label','Illumina OLB Stats: Cluster Density Per Lane For ',NULL,'en_US','clustersQc_title',NULL),
	(2312,'2013-03-12 12:39:44','2013-03-12 12:39:44',X'451287293FF04A299BC99BC477BF2BB8','waspIlluminaPlugin','label','Assessment of Cluster Density',NULL,'en_US','clustersQc_dialogTitle',NULL),
	(2313,'2013-03-12 12:39:44','2013-03-12 12:39:44',X'CC13AA1FDBED46428F586ED7C8B432A6','waspIlluminaPlugin','label','Please click either \"Pass\" or \"Fail\" for each lane based on your interpretation of the CLUSTER DENSITY charts only, then click the \"Continue\" button.',NULL,'en_US','clustersQc_dialogMessage',NULL),
	(2314,'2013-03-12 12:39:44','2013-03-12 12:39:44',X'45D02EECEEF0451F8D2E07F85BA9F87B','waspIlluminaPlugin','label','Note that the <span style=\"color:blue;\">blue box plots represent total cluster density</span> and the <span style=\"color:green;\">green box plots represent clusters pass filter</span>. Ideally the median of both plots for each lane should be about the same. Illumina currently recommends ~400 clusters/mm<sup>2</sup> (or 500-630 clusters/mm<sup>2</sup> on GAIIx using the TruSeq SBS V5 kit or 610-680 clusters/mm<sup>2</sup> on HiSeq2000 with TruSeq v3 Cluster and SBS kits).</p><p><span style=\"color:red;\">WARNING:</span> High cluster density combined with low cluster pass filter values indicates overloading of the lane and risks poor quality sequence data.',NULL,'en_US','clustersQc_instructions',NULL),
	(2315,'2013-03-12 12:39:44','2013-03-12 12:39:44',X'EB94280A53884BDFA309B958B694799C','bowtieAligner','tooltip','Maximum number of mismatches permitted in the seed, i.e. the first L base pairs of the read. This may be 0, 1, 2 or 3.',NULL,'en_US','mismatches',NULL),
	(2316,'2013-03-12 12:39:44','2013-03-12 12:39:44',X'7AEAE9B298BE4B51BF575251594EEC50','bowtieAligner','tooltip','The number of bases on the high-quality end of the read to which the # mismatches ceiling applies. The lowest permitted setting is 5.',NULL,'en_US','seedLength',NULL),
	(2317,'2013-03-12 12:39:44','2013-03-12 12:39:44',X'F52BA6542575495784895AF109969EBC','bowtieAligner','tooltip','Report up to N valid alignments per read or pair ',NULL,'en_US','reportAlignmentNum',NULL),
	(2318,'2013-03-12 12:39:44','2013-03-12 12:39:44',X'AE12875E0F6E48E1AA8BB617B4A41A38','bowtieAligner','tooltip','Suppress all alignments for a particular read or pair if more than N reportable alignments exist for it.',NULL,'en_US','discardThreshold',NULL),
	(2319,'2013-03-12 12:39:44','2013-03-12 12:39:44',X'3FA979F1C9CC42FE88D42822D8713154','bowtieAligner','tooltip','Make Bowtie guarantee that reported singleton alignments are best in terms of stratum (i.e. number of mismatches in the seed) and in terms of the quality values at the mismatched position(s)',NULL,'en_US','isBest',NULL),
	(2320,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'5466AD7D011A4245AA58CF2FBF20B706','macsPeakcaller','tooltip','Pvalue cutoff for peak detection',NULL,'en_US','pValueCutoff',NULL),
	(2321,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'97DA232EE54F4ED096289FEF658C5331','macsPeakcaller','tooltip','This value is only used while building the shifting model.',NULL,'en_US','bandwidth',NULL),
	(2322,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'A1205E2147B14BAB99FCDC8680BD2BDC','macsPeakcaller','tooltip','Controls MACS behavior towards duplicate tags at the exact same location -- the same coordination and the same strand.',NULL,'en_US','keepDup',NULL),
	(2323,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'BA8B6AB5F5A6481FBAA7B55F59444CF0','wasp-chipSeqPlugin','label','Chip Seq',NULL,'en_US','workflow',NULL),
	(2324,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'BB9DEB26C01A4B54A76F60DF0067B75F','wasp-chipSeqPlugin','label','ModifyChipSeq Metadata',NULL,'en_US','jobsubmit/modifymeta',NULL),
	(2325,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'6DF65A73C1964CE3A0B4C2643A2A9E25','wasp-chipSeqPlugin','label','MPS Sequencer Options',NULL,'en_US','jobsubmit/resource/mps',NULL),
	(2326,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'546AF30AFE584219A979F9E2B7B79243','wasp-chipSeqPlugin','label','Samples',NULL,'en_US','jobsubmit/samples',NULL),
	(2327,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'8453C1DDE6A54D03B537AFB986457C7C','wasp-chipSeqPlugin','label','DNA Sequencer Cells',NULL,'en_US','jobsubmit/cells',NULL),
	(2328,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'AA13FE36826C40238E9A5B15523BD63B','wasp-chipSeqPlugin','label','IP vs Input Pairings',NULL,'en_US','jobsubmit/chipSeq/pair',NULL),
	(2329,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'6BEA959A87F1432B8B4BB019D1E93EA2','wasp-chipSeqPlugin','label','Aligner Selection',NULL,'en_US','jobsubmit/software/aligner',NULL),
	(2330,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'4D326B89954948959740E30521C5C625','wasp-chipSeqPlugin','label','Peak Calling Software Selection',NULL,'en_US','jobsubmit/software/peakcaller',NULL),
	(2331,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'E5B1162181F64993AA3DA9DE10CCE651','wasp-chipSeqPlugin','label','Comments',NULL,'en_US','jobsubmit/comment',NULL),
	(2332,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'8A4C3380EB24402189510C362B6F455C','wasp-chipSeqPlugin','label','Verify Submission',NULL,'en_US','jobsubmit/verify',NULL),
	(2333,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'A281326F186E4CEC9628CAE00D780DDA','wasp-chipSeqPlugin','label','Please select test vs control for all samples to be analyzed in pairs after sequencing.',NULL,'en_US','pairing_instructions',NULL),
	(2334,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'C2330149A46A48529748574203A3DC76','wasp-chipSeqPlugin','label','Test',NULL,'en_US','test',NULL),
	(2335,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'68EAA83AFA6F4EC0895DA03094810025','wasp-chipSeqPlugin','label','Control',NULL,'en_US','control',NULL),
	(2336,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'2E994B0A842E4C279B6DA3406EFF34F1','wasp-genericDnaSeqPlugin','label','Generic DNA Seq',NULL,'en_US','workflow',NULL),
	(2337,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'8E7ACEB5401C429BA08F6290C1087AE6','wasp-genericDnaSeqPlugin','label','Modify Generic DNA Seq Metadata',NULL,'en_US','jobsubmit/modifymeta',NULL),
	(2338,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'D5FEA3AE507E478A947557AC90EB96FC','wasp-genericDnaSeqPlugin','label','MPS Sequencer Options',NULL,'en_US','jobsubmit/resource/mps',NULL),
	(2339,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'D9BBEBF778BF448792D2838E09417AD2','wasp-genericDnaSeqPlugin','label','Samples',NULL,'en_US','jobsubmit/samples',NULL),
	(2340,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'FBFBCCBBDD014D45B85D4B2AB129BE39','wasp-genericDnaSeqPlugin','label','DNA Sequencer Cells',NULL,'en_US','jobsubmit/cells',NULL),
	(2341,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'AC8DFF0C482844F1873A26B4119E3442','wasp-genericDnaSeqPlugin','label','Aligner Selection',NULL,'en_US','jobsubmit/software/aligner',NULL),
	(2342,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'5C4242C3BBC14E829062868DED61C3B6','wasp-genericDnaSeqPlugin','label','Comments',NULL,'en_US','jobsubmit/comment',NULL),
	(2343,'2013-03-12 12:39:45','2013-03-12 12:39:45',X'E5D9A0702887414FB3BB55F77F22CC3A','wasp-genericDnaSeqPlugin','label','Verify Submission',NULL,'en_US','jobsubmit/verify',NULL),
	(2344,'2013-03-12 12:39:45','2013-03-12 12:39:46',X'37A95805D0224121A1AAFD1A831E6F50','wasp-helpTagPlugin','label','HELP Tagging',NULL,'en_US','workflow',NULL),
	(2345,'2013-03-12 12:39:45','2013-03-12 12:39:46',X'B53F9187785A441891F4980C6C188FD7','wasp-helpTagPlugin','label','Modify HelpTag Metadata',NULL,'en_US','jobsubmit/modifymeta',NULL),
	(2346,'2013-03-12 12:39:45','2013-03-12 12:39:46',X'05E923012FC141F4AE40FAD48313CAC7','wasp-helpTagPlugin','label','MPS Sequencer Options',NULL,'en_US','jobsubmit/resource/mps',NULL),
	(2347,'2013-03-12 12:39:45','2013-03-12 12:39:46',X'6F44F83F1CB0416699F0C902EBED4E55','wasp-helpTagPlugin','label','Samples',NULL,'en_US','jobsubmit/samples',NULL),
	(2348,'2013-03-12 12:39:45','2013-03-12 12:39:46',X'60961543B6834D73ABF03B1BD4F094B4','wasp-helpTagPlugin','label','DNA Sequencer Cells',NULL,'en_US','jobsubmit/cells',NULL),
	(2349,'2013-03-12 12:39:45','2013-03-12 12:39:46',X'5DE629DDFF1142279B81A631DE6FC2E2','wasp-helpTagPlugin','label','HpaII vs MspI Pairings',NULL,'en_US','jobsubmit/helpTag/pair',NULL),
	(2350,'2013-03-12 12:39:45','2013-03-12 12:39:46',X'94F866FB9780467FB301A0E6D123EF93','wasp-helpTagPlugin','label','Aligner Selection',NULL,'en_US','jobsubmit/software/aligner',NULL),
	(2351,'2013-03-12 12:39:45','2013-03-12 12:39:46',X'5CFE935FE3484FE6BE7C0298AA55757B','wasp-helpTagPlugin','label','HELP-tag Pipeline Selection',NULL,'en_US','jobsubmit/software/helptagPipeline',NULL),
	(2352,'2013-03-12 12:39:45','2013-03-12 12:39:46',X'3E154D7B32824310924AE306CA07CE83','wasp-helpTagPlugin','label','Verify Submission',NULL,'en_US','jobsubmit/verify',NULL),
	(2353,'2013-03-12 12:39:45','2013-03-12 12:39:46',X'74680B98AB62490D93EEB3B6E60C16F1','wasp-helpTagPlugin','label','Please select HpaII vs MspI for all samples to be analyzed in pairs after sequencing.',NULL,'en_US','pairing_instructions',NULL),
	(2354,'2013-03-12 12:39:45','2013-03-12 12:39:46',X'9A3803E1833444538FD29F37C2865675','wasp-helpTagPlugin','label','HpaII',NULL,'en_US','test',NULL),
	(2355,'2013-03-12 12:39:45','2013-03-12 12:39:46',X'729F9D979C4A4B40924B70D12722D459','wasp-helpTagPlugin','label','MspI',NULL,'en_US','control',NULL);

/*!40000 ALTER TABLE `uifield` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table usermeta
# ------------------------------------------------------------

LOCK TABLES `usermeta` WRITE;
/*!40000 ALTER TABLE `usermeta` DISABLE KEYS */;

INSERT INTO `usermeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `userid`, `lastupdatebyuser`)
VALUES
	(1,'2012-05-23 17:23:00','2013-03-12 18:10:00',X'293B13835E584F0C916BD728F42B9E39','user.title',0,NULL,'Dr',2,NULL),
	(2,'2012-05-23 17:23:00','2013-03-12 18:10:00',X'23C3602E2C874CB08F69C443C51EF1CA','user.institution',0,NULL,'Einstein',2,NULL),
	(3,'2012-05-23 17:23:00','2013-03-12 18:10:00',X'86CF9706268D4FDDBBD3DFB8279D9252','user.departmentId',0,NULL,'1',2,NULL),
	(4,'2012-05-23 17:23:00','2013-03-12 18:10:00',X'55E43093D5414C62AA552CED1D416C0A','user.building_room',0,NULL,'Price 954',2,NULL),
	(5,'2012-05-23 17:23:00','2013-03-12 18:10:00',X'24DC65BD577F4B449D38C367BF4454D3','user.address',0,NULL,'1301 Morris Park Ave',2,NULL),
	(6,'2012-05-23 17:23:00','2013-03-12 18:10:00',X'A0E195468CE843719328B2A1F7F8B95E','user.city',0,NULL,'Bronx',2,NULL),
	(7,'2012-05-23 17:23:00','2013-03-12 18:10:00',X'02655FD6DA2144E5ACD604E46D723627','user.state',0,NULL,'NY',2,NULL),
	(8,'2012-05-23 17:23:00','2013-03-12 18:10:00',X'A0C88E4A70E045B1B0584AC97E57F1D0','user.country',0,NULL,'US',2,NULL),
	(9,'2012-05-23 17:23:00','2013-03-12 18:10:00',X'EF9A9ED5679A4C849CF3E5404623F413','user.zip',0,NULL,'10461',2,NULL),
	(10,'2012-05-23 17:23:00','2013-03-12 18:10:00',X'6FBA24318ED34804AA59CF001BAE7A63','user.phone',0,NULL,'718-600-1985',2,NULL),
	(11,'2012-05-23 17:23:00','2013-03-12 18:10:00',X'F1833E3DB63742FB848D3384478E7174','user.fax',0,NULL,'',2,NULL),
	(12,'2012-05-30 16:15:28','2013-03-12 18:10:00',X'89C93B89EAF44C3C8916F2ACEDB0698B','user.title',0,NULL,'Ms',3,NULL),
	(13,'2012-05-30 16:15:28','2013-03-12 18:10:00',X'60BF5FF947594B148D64C0D2A463FFBF','user.institution',0,NULL,'Einstein',3,NULL),
	(14,'2012-05-30 16:15:28','2013-03-12 18:10:00',X'0D03DF804E7546EFBB5AC643B6937E2E','user.departmentId',0,NULL,'1',3,NULL),
	(15,'2012-05-30 16:15:28','2013-03-12 18:10:00',X'1890CD22952B411D9F0668F91B75FD00','user.building_room',0,NULL,'Price 1003',3,NULL),
	(16,'2012-05-30 16:15:28','2013-03-12 18:10:00',X'2F148C3A95404B6EAE3C9853F7BC7B73','user.address',0,NULL,'1301 Morris Park Ave.',3,NULL),
	(17,'2012-05-30 16:15:28','2013-03-12 18:10:00',X'C9A99ED7F86C47C1B80E67075152B7BF','user.city',0,NULL,'Bronx',3,NULL),
	(18,'2012-05-30 16:15:28','2013-03-12 18:10:00',X'0EE0B6A9439E4BEBA1A873F1AA143AB9','user.state',0,NULL,'NY',3,NULL),
	(19,'2012-05-30 16:15:28','2013-03-12 18:10:00',X'8184CE4356B54BA08831267E885A5D11','user.country',0,NULL,'US',3,NULL),
	(20,'2012-05-30 16:15:28','2013-03-12 18:10:00',X'8A01B323F1D64AD8A044809348DC2CBE','user.zip',0,NULL,'10461',3,NULL),
	(21,'2012-05-30 16:15:28','2013-03-12 18:10:00',X'F9446B35EDAA47FBB31BEFD4916A8D79','user.phone',0,NULL,'718-600-3465',3,NULL),
	(22,'2012-05-30 16:15:28','2013-03-12 18:10:00',X'442A58ABB7904E9CBC9570C3DEDF5569','user.fax',0,NULL,'',3,NULL),
	(23,'2012-05-30 16:31:54','2013-03-12 18:10:00',X'FC041CD2A7A3490D8C3CC92B175D66C9','user.title',0,NULL,'Ms',4,NULL),
	(24,'2012-05-30 16:31:54','2013-03-12 18:10:00',X'78380D85F3B94B8D93FAB3DA05308E4A','user.institution',0,NULL,'Einstein',4,NULL),
	(25,'2012-05-30 16:31:54','2013-03-12 18:10:00',X'9744B82F2B774FA78E70585145D3BDD3','user.departmentId',0,NULL,'1',4,NULL),
	(26,'2012-05-30 16:31:54','2013-03-12 18:10:00',X'261AB6AC00044289909F3FA9A6E5F84A','user.building_room',0,NULL,'Price 201',4,NULL),
	(27,'2012-05-30 16:31:54','2013-03-12 18:10:00',X'B2E9FF0162FF4F87B3A1A5A0F874471B','user.address',0,NULL,'1301 Morris Park Ave.',4,NULL),
	(28,'2012-05-30 16:31:54','2013-03-12 18:10:00',X'1471EB324776465EA3735F003DA53681','user.city',0,NULL,'Bronx',4,NULL),
	(29,'2012-05-30 16:31:54','2013-03-12 18:10:00',X'CC4DE4C13AC8494AA348D1EFE381F960','user.state',0,NULL,'NY',4,NULL),
	(30,'2012-05-30 16:31:54','2013-03-12 18:10:00',X'689F7CF0A05C4A638330DD4CFC070916','user.country',0,NULL,'US',4,NULL),
	(31,'2012-05-30 16:31:54','2013-03-12 18:10:00',X'E21B02D23CCC49718C345641B2CD16CD','user.zip',0,NULL,'10461',4,NULL),
	(32,'2012-05-30 16:31:54','2013-03-12 18:10:00',X'ECE07AB65523479A9D912BA9D7436112','user.phone',0,NULL,'718-600-1100',4,NULL),
	(33,'2012-05-30 16:31:54','2013-03-12 18:10:00',X'265904A3907F4D3C8D5BAFFBFA913CA4','user.fax',0,NULL,'',4,NULL),
	(34,'2012-05-30 20:22:24','2013-03-12 18:10:00',X'3CA0860BEB634E279783BF5A773D927B','user.title',0,NULL,'Dr',5,NULL),
	(35,'2012-05-30 20:22:24','2013-03-12 18:10:00',X'042315BDA30343538DB05403DC8E3474','user.institution',0,NULL,'Einstein',5,NULL),
	(36,'2012-05-30 20:22:24','2013-03-12 18:10:00',X'0AA2EAD6AB064F4C870F44C7A2C8EC01','user.departmentId',0,NULL,'3',5,NULL),
	(37,'2012-05-30 20:22:24','2013-03-12 18:10:00',X'C09AFCFABE6B41DEAE91D8AF5DCAF3A8','user.building_room',0,NULL,'Price 2200',5,NULL),
	(38,'2012-05-30 20:22:24','2013-03-12 18:10:00',X'E01BB247E2EA4D1B832957618ACE6BF7','user.address',0,NULL,'1301 Morris Park Ave.',5,NULL),
	(39,'2012-05-30 20:22:24','2013-03-12 18:10:00',X'A5E2147EAEA746AFAC40D5F4111CA5B0','user.city',0,NULL,'Bronx',5,NULL),
	(40,'2012-05-30 20:22:24','2013-03-12 18:10:00',X'BD6759F5CF844D3192DBC70FE5F2757F','user.state',0,NULL,'NY',5,NULL),
	(41,'2012-05-30 20:22:24','2013-03-12 18:10:00',X'23725398AD944AB9A482497262258D7B','user.country',0,NULL,'US',5,NULL),
	(42,'2012-05-30 20:22:24','2013-03-12 18:10:00',X'92BF62CAA17141F1BBB1EC6346FDA1F6','user.zip',0,NULL,'10461',5,NULL),
	(43,'2012-05-30 20:22:24','2013-03-12 18:10:00',X'AF9377F05B02436A95DB23DAB19F2471','user.phone',0,NULL,'718-123-4567',5,NULL),
	(44,'2012-05-30 20:22:24','2013-03-12 18:10:00',X'717F296DC16846FBA0CC9002E7624AFA','user.fax',0,NULL,'',5,NULL),
	(45,'2012-05-30 20:22:24','2013-03-12 18:10:00',X'FA5F924581AD4231BBF7E00D7668D68B','user.labName',0,NULL,'Greally Lab',5,NULL),
	(46,'2012-05-30 21:13:54','2013-03-12 18:10:00',X'A0B796D5810D47689087390DD5BC9CCB','user.title',0,NULL,'Ms',6,NULL),
	(47,'2012-05-30 21:13:54','2013-03-12 18:10:00',X'C7E0CBDF0EF340428FD860424A7692BC','user.institution',0,NULL,'Einstein',6,NULL),
	(48,'2012-05-30 21:13:54','2013-03-12 18:10:00',X'AF1932CC54114450B5722D00C6934327','user.departmentId',0,NULL,'3',6,NULL),
	(49,'2012-05-30 21:13:54','2013-03-12 18:10:00',X'5D7132A021354F08B4386F076EE1769E','user.building_room',0,NULL,'Price 2220',6,NULL),
	(50,'2012-05-30 21:13:54','2013-03-12 18:10:00',X'F3062D90D83447ED86279038B9B495A8','user.address',0,NULL,'1301 Morris Park Ave.',6,NULL),
	(51,'2012-05-30 21:13:54','2013-03-12 18:10:00',X'F5715E9F8D1A44469071CC8FFA8AA41D','user.city',0,NULL,'Bronx',6,NULL),
	(52,'2012-05-30 21:13:54','2013-03-12 18:10:00',X'F71779C9A0654CF1BA11B7D826D51570','user.state',0,NULL,'NY',6,NULL),
	(53,'2012-05-30 21:13:54','2013-03-12 18:10:00',X'2259CD1C170547308C672F61D373D442','user.country',0,NULL,'US',6,NULL),
	(54,'2012-05-30 21:13:54','2013-03-12 18:10:00',X'4533EDEB8F9D4A6FABE2C84B8A324CC8','user.zip',0,NULL,'10461',6,NULL),
	(55,'2012-05-30 21:13:54','2013-03-12 18:10:00',X'DDBBAD2F71C64DEC91EF141F2E1A6E4B','user.phone',0,NULL,'718-608-0000',6,NULL),
	(56,'2012-05-30 21:13:54','2013-03-12 18:10:00',X'3E0A1D6D92B3448BA921EF2E630A1733','user.fax',0,NULL,'',6,NULL),
	(57,'2012-05-30 21:13:54','2013-03-12 18:10:00',X'BC47F2B05741491883C56009D3823C6B','user.primaryuserid',0,NULL,'jgreally',6,NULL),
	(58,'2012-05-30 22:03:56','2013-03-12 18:10:00',X'ABE40EBC37864F7FA8025B71D25413D8','user.title',0,NULL,'Prof',7,NULL),
	(59,'2012-05-30 22:03:56','2013-03-12 18:10:00',X'FB34E5A15E9B46C9B10EA645B826E76A','user.institution',0,NULL,'Einstein',7,NULL),
	(60,'2012-05-30 22:03:56','2013-03-12 18:10:00',X'1E3D9AA12EE7472CAD0E4733CEB97260','user.departmentId',0,NULL,'3',7,NULL),
	(61,'2012-05-30 22:03:56','2013-03-12 18:10:00',X'332044EFBBC24A808A492398F65EBBAB','user.building_room',0,NULL,'Price 933',7,NULL),
	(62,'2012-05-30 22:03:56','2013-03-12 18:10:00',X'01569FB4C3CA49839BBBA141265884C7','user.address',0,NULL,'1301 Morris Park Ave.',7,NULL),
	(63,'2012-05-30 22:03:56','2013-03-12 18:10:00',X'3F492B09652748E2AA069E90EC817666','user.city',0,NULL,'Bronx',7,NULL),
	(64,'2012-05-30 22:03:56','2013-03-12 18:10:00',X'C96C244B62CA420497618A360FEFCE81','user.state',0,NULL,'NY',7,NULL),
	(65,'2012-05-30 22:03:56','2013-03-12 18:10:00',X'598AA2BA60B14141A42A054899F9D3C7','user.country',0,NULL,'US',7,NULL),
	(66,'2012-05-30 22:03:56','2013-03-12 18:10:00',X'F3DE71B20686425A98247579BD4764C5','user.zip',0,NULL,'10461',7,NULL),
	(67,'2012-05-30 22:03:56','2013-03-12 18:10:00',X'7FE18C9BC03D4F9E84B388AE243337EE','user.phone',0,NULL,'718-600-1192',7,NULL),
	(68,'2012-05-30 22:03:56','2013-03-12 18:10:00',X'7EBAA127D11F4E1780545896BFBE0109','user.fax',0,NULL,'',7,NULL),
	(69,'2012-05-30 22:03:56','2013-03-12 18:10:00',X'55737E230DDD4EC28581DC2033764237','user.labName',0,NULL,'Goldin Lab',7,NULL),
	(70,'2012-05-31 13:59:22','2013-03-12 18:10:00',X'98D74D2912C748B190676428DF0FC2AD','user.title',0,NULL,'Dr',8,NULL),
	(71,'2012-05-31 13:59:22','2013-03-12 18:10:00',X'1827CEF278D44BC9830A481936C4365B','user.institution',0,NULL,'Einstein',8,NULL),
	(72,'2012-05-31 13:59:22','2013-03-12 18:10:00',X'D454DC61D36F48548D9DF7836A3E3E5D','user.departmentId',0,NULL,'3',8,NULL),
	(73,'2012-05-31 13:59:22','2013-03-12 18:10:00',X'E01D8DDB28F449409CB3203E0293928A','user.building_room',0,NULL,'Price 654',8,NULL),
	(74,'2012-05-31 13:59:22','2013-03-12 18:10:00',X'1AADE2C63B7D4E48B53D55CEC48CBFDC','user.address',0,NULL,'1301 Morris Park Ave.',8,NULL),
	(75,'2012-05-31 13:59:22','2013-03-12 18:10:00',X'323FC6E68197455C9D8C9175C68BD9FE','user.city',0,NULL,'Bronx',8,NULL),
	(76,'2012-05-31 13:59:22','2013-03-12 18:10:00',X'589846335D91417C9D038D7BA39DD19F','user.state',0,NULL,'NY',8,NULL),
	(77,'2012-05-31 13:59:22','2013-03-12 18:10:00',X'AEE20CBF5E0F45658D6E388C26A81944','user.country',0,NULL,'US',8,NULL),
	(78,'2012-05-31 13:59:22','2013-03-12 18:10:00',X'688B37F1990D4E21A5821AF3B05F350E','user.zip',0,NULL,'10461',8,NULL),
	(79,'2012-05-31 13:59:22','2013-03-12 18:10:00',X'49A8E1497AD54AD7AD41ED458041B958','user.phone',0,NULL,'718-600-0019',8,NULL),
	(80,'2012-05-31 13:59:22','2013-03-12 18:10:00',X'72FE8C8C069C487592E7A58B1A0DB28F','user.fax',0,NULL,'718-600-0020',8,NULL),
	(81,'2012-05-31 13:59:22','2013-03-12 18:10:00',X'C5E2D5CC4FE147709F480129828CA157','user.labName',0,NULL,'Auton Lab',8,NULL),
	(82,'2012-05-31 14:00:01','2013-03-12 18:10:00',X'93A2F9B8E181472C90476B5F39357C79','user.title',0,NULL,'Dr',9,NULL),
	(83,'2012-05-31 14:00:01','2013-03-12 18:10:00',X'D1690FAFB3C04FF4AF4E39498A61AD1F','user.institution',0,NULL,'NYU Medical',9,NULL),
	(84,'2012-05-31 14:00:01','2013-03-12 18:10:00',X'3D9B7268F7BB47A99494281857049138','user.departmentId',0,NULL,'2',9,NULL),
	(85,'2012-05-31 14:00:01','2013-03-12 18:10:00',X'28C0F8A7FD704766BD53178FE84FA4B1','user.building_room',0,NULL,'Hammer 1406',9,NULL),
	(86,'2012-05-31 14:00:01','2013-03-12 18:10:00',X'70D103FA8E7845DA8701E1843CE4BCB8','user.address',0,NULL,'16-50 54th Street',9,NULL),
	(87,'2012-05-31 14:00:01','2013-03-12 18:10:00',X'5E241D29B5F74AFC9568CFE488A0B377','user.city',0,NULL,'New York',9,NULL),
	(88,'2012-05-31 14:00:01','2013-03-12 18:10:00',X'C6D63704A73A4117A91C7E3B65E5FF59','user.state',0,NULL,'NY',9,NULL),
	(89,'2012-05-31 14:00:01','2013-03-12 18:10:00',X'A9DDD745B68E41ECB872CA3BC471F59A','user.country',0,NULL,'US',9,NULL),
	(90,'2012-05-31 14:00:01','2013-03-12 18:10:00',X'CBC3F9C0588743679161E2FF2FF82743','user.zip',0,NULL,'10002',9,NULL),
	(91,'2012-05-31 14:00:01','2013-03-12 18:10:00',X'1239E07B105D49BEAE9C6E3C63BAE7C9','user.phone',0,NULL,'212-445-2345',9,NULL),
	(92,'2012-05-31 14:00:01','2013-03-12 18:10:00',X'7946695B0D264A8A9F7B27233E48F48D','user.fax',0,NULL,'',9,NULL),
	(93,'2012-05-31 14:00:01','2013-03-12 18:10:00',X'4597A3CD619F4BAFBCC150F480312E86','user.labName',0,NULL,'Trokie Lab',9,NULL),
	(94,'2012-05-31 14:02:26','2013-03-12 18:10:00',X'7DF3B9B173AE4F47979308097021058F','user.title',0,NULL,'Dr',10,NULL),
	(95,'2012-05-31 14:02:26','2013-03-12 18:10:00',X'C3AF49A247BB4EFDB29E290AB70FF43C','user.institution',0,NULL,'Einstein',10,NULL),
	(96,'2012-05-31 14:02:26','2013-03-12 18:10:00',X'8A1BEFDC421A4016A5EDFA5AA518D9E8','user.departmentId',0,NULL,'3',10,NULL),
	(97,'2012-05-31 14:02:26','2013-03-12 18:10:00',X'E4FEC4143A93480CAC8ED5CAA687D184','user.building_room',0,NULL,'Price 2222',10,NULL),
	(98,'2012-05-31 14:02:26','2013-03-12 18:10:00',X'852110F51C3740149CC29D20DDEBF431','user.address',0,NULL,'1301 Morris Park Ave',10,NULL),
	(99,'2012-05-31 14:02:26','2013-03-12 18:10:00',X'D60B6466D31E4944B536905FD8DD7C2B','user.city',0,NULL,'Bronx',10,NULL),
	(100,'2012-05-31 14:02:26','2013-03-12 18:10:00',X'52264A7CE6A946C1BADCE4AFA38A385C','user.state',0,NULL,'NY',10,NULL),
	(101,'2012-05-31 14:02:26','2013-03-12 18:10:00',X'2A349213347047D88D903D2F885EC477','user.country',0,NULL,'US',10,NULL),
	(102,'2012-05-31 14:02:26','2013-03-12 18:10:00',X'27FA86DF25254927B0B82F976864F75B','user.zip',0,NULL,'10461',10,NULL),
	(103,'2012-05-31 14:02:26','2013-03-12 18:10:00',X'5269E8B56E844811829D1F8F1A794EA6','user.phone',0,NULL,'718-500-6696',10,NULL),
	(104,'2012-05-31 14:02:26','2013-03-12 18:10:00',X'110BEA0453AD4D09B616BED654ACEFFE','user.fax',0,NULL,'718-500-6697',10,NULL),
	(105,'2012-05-31 14:02:26','2013-03-12 18:10:00',X'A9F2079A49A348618DC592CD876AE80E','user.primaryuserid',0,NULL,'jgreally',10,NULL),
	(106,'2012-05-31 14:07:26','2013-03-12 18:10:00',X'C147F2A9982547F3B9A99F78DA451208','user.title',0,NULL,'Mr',11,NULL),
	(107,'2012-05-31 14:07:26','2013-03-12 18:10:00',X'57B02B3E12654B40B846DE9A0C6FDF2A','user.institution',0,NULL,'Einstein',11,NULL),
	(108,'2012-05-31 14:07:26','2013-03-12 18:10:00',X'59B68CBC30EB4BBB92A8E8380EEE3455','user.departmentId',0,NULL,'1',11,NULL),
	(109,'2012-05-31 14:07:26','2013-03-12 18:10:00',X'17E669A9E4D14B4B899F743103957086','user.building_room',0,NULL,'Price 1357',11,NULL),
	(110,'2012-05-31 14:07:26','2013-03-12 18:10:00',X'3475329003E6431AB758ABF7C2DFFDC0','user.address',0,NULL,'1301 Morris Park Ave.',11,NULL),
	(111,'2012-05-31 14:07:26','2013-03-12 18:10:00',X'A6263711B0BF4AF38668C2F85A35443A','user.city',0,NULL,'Bronx',11,NULL),
	(112,'2012-05-31 14:07:26','2013-03-12 18:10:00',X'AF176FA64D354A0DAF8A51FDA7E49ACF','user.state',0,NULL,'NY',11,NULL),
	(113,'2012-05-31 14:07:26','2013-03-12 18:10:00',X'CFB7E6C4536842938C8FAA29510B58C1','user.country',0,NULL,'US',11,NULL),
	(114,'2012-05-31 14:07:26','2013-03-12 18:10:00',X'8C49719668E8439987A72572B11D684F','user.zip',0,NULL,'10461',11,NULL),
	(115,'2012-05-31 14:07:26','2013-03-12 18:10:00',X'16A26714B62142F498FFF244B81C6B2A','user.phone',0,NULL,'718-600-4533',11,NULL),
	(116,'2012-05-31 14:07:26','2013-03-12 18:10:00',X'91D8DEE1D0C84180B5C244A151E23A5F','user.fax',0,NULL,'',11,NULL),
	(117,'2012-05-31 14:15:29','2013-03-12 18:10:00',X'6D915A480583481DAECDD047D7EC87AD','user.title',0,NULL,'Prof',12,NULL),
	(118,'2012-05-31 14:15:29','2013-03-12 18:10:00',X'2F1EB046B0C849D28D7ED10A0580238D','user.institution',0,NULL,'Einstein',12,NULL),
	(119,'2012-05-31 14:15:29','2013-03-12 18:10:00',X'D0B878EF8BB34A6BA20238EC30AA6A8E','user.departmentId',0,NULL,'3',12,NULL),
	(120,'2012-05-31 14:15:29','2013-03-12 18:10:00',X'47C20875F9EC4F46B47F84C692F7ECB5','user.building_room',0,NULL,'Price 222',12,NULL),
	(121,'2012-05-31 14:15:29','2013-03-12 18:10:00',X'0D79D651D42C4DE8938DA90102E17144','user.address',0,NULL,'1301 Morris Park Ave',12,NULL),
	(122,'2012-05-31 14:15:29','2013-03-12 18:10:00',X'51BC25C8CEDE4D339538D75BF2B6FD80','user.city',0,NULL,'Bronx',12,NULL),
	(123,'2012-05-31 14:15:29','2013-03-12 18:10:00',X'223DB856477A4E38A7C0BBE0DCF608D7','user.state',0,NULL,'NY',12,NULL),
	(124,'2012-05-31 14:15:29','2013-03-12 18:10:00',X'4D72F5A4E9B84F33BF398BC16C68EA73','user.country',0,NULL,'US',12,NULL),
	(125,'2012-05-31 14:15:29','2013-03-12 18:10:00',X'D7C6F01BF60A44D9ACBB06A3475670B9','user.zip',0,NULL,'10461',12,NULL),
	(126,'2012-05-31 14:15:29','2013-03-12 18:10:00',X'8F4106C7C4D24E4E966B2AB22F54067F','user.phone',0,NULL,'617-600-1313',12,NULL),
	(127,'2012-05-31 14:15:29','2013-03-12 18:10:00',X'108FA0FB93E845E08B660EC6A8CDDFE1','user.fax',0,NULL,'',12,NULL),
	(128,'2012-05-31 14:15:29','2013-03-12 18:10:00',X'48FE14A12C0145169FEF99362A885FB3','user.primaryuserid',0,NULL,'agoldin',12,NULL),
	(129,'2012-06-14 13:49:39','2013-03-12 18:10:00',X'29088538C8284D35AC583BB611A9D034','user.title',0,NULL,'Dr',1,NULL),
	(130,'2012-06-14 13:49:39','2013-03-12 18:10:00',X'F5A1D29086C3448296664290D6420E24','user.institution',0,NULL,'Einstein',1,NULL),
	(131,'2012-06-14 13:49:39','2013-03-12 18:10:00',X'39991AAC5BD447B0A8B720028D9D2EE1','user.departmentId',0,NULL,'1',1,NULL),
	(132,'2012-06-14 13:49:39','2013-03-12 18:10:00',X'EBAC39862B8A444EA7B4FC0CFC44B9F8','user.building_room',0,NULL,'N/A',1,NULL),
	(133,'2012-06-14 13:49:39','2013-03-12 18:10:00',X'B3C86E3F22C64172B5235AA31EC35628','user.address',0,NULL,'N/A',1,NULL),
	(134,'2012-06-14 13:49:39','2013-03-12 18:10:00',X'4C2E5B6279004771BA50150939ED230C','user.city',0,NULL,'N/A',1,NULL),
	(135,'2012-06-14 13:49:39','2013-03-12 18:10:00',X'2B5EA1B4ACB84BEDB21665ABD1B66CA6','user.state',0,NULL,'NY',1,NULL),
	(136,'2012-06-14 13:49:39','2013-03-12 18:10:00',X'2188B500E0624950BC6211522C9400F8','user.country',0,NULL,'US',1,NULL),
	(137,'2012-06-14 13:49:39','2013-03-12 18:10:00',X'C1FFCC1699E242759049F0BBB5A7E259','user.zip',0,NULL,'N/A',1,NULL),
	(138,'2012-06-14 13:49:39','2013-03-12 18:10:00',X'835BE5F788B640DAB840554A5ED63E80','user.phone',0,NULL,'N/A',1,NULL),
	(139,'2012-06-14 13:49:39','2013-03-12 18:10:00',X'3B6E332170BD49078118ECAC0DBD9F56','user.fax',0,NULL,'N/A',1,NULL),
	(140,'2012-06-14 14:11:40','2013-03-12 18:10:00',X'7E3BE51EBA6544FCBF7E81AD41BBE1B3','user.title',0,NULL,'Miss',13,NULL),
	(141,'2012-06-14 14:11:40','2013-03-12 18:10:00',X'08DDF2512D6F4EC3B7BFD8F853D72631','user.institution',0,NULL,'Einstein',13,NULL),
	(142,'2012-06-14 14:11:40','2013-03-12 18:10:00',X'4E8A62DA04474E5FBD32666BF27DB317','user.departmentId',0,NULL,'1',13,NULL),
	(143,'2012-06-14 14:11:40','2013-03-12 18:10:00',X'F3E2E889D1D74BE88DFC2FFB4ED16C2A','user.building_room',0,NULL,'4333',13,NULL),
	(144,'2012-06-14 14:11:40','2013-03-12 18:10:00',X'B65606661C224FAD905B6EAF04429B27','user.address',0,NULL,'Chanin',13,NULL),
	(145,'2012-06-14 14:11:40','2013-03-12 18:10:00',X'8E874343CCCC4A13AD4AE25877CBF6D5','user.city',0,NULL,'Bronx',13,NULL),
	(146,'2012-06-14 14:11:40','2013-03-12 18:10:00',X'CCB07D5545E04453BD7480C24E4FD4DB','user.state',0,NULL,'NY',13,NULL),
	(147,'2012-06-14 14:11:40','2013-03-12 18:10:00',X'F27C0F3178C249C89AC5125FDD1927E8','user.country',0,NULL,'US',13,NULL),
	(148,'2012-06-14 14:11:40','2013-03-12 18:10:00',X'B76DD097D55648E482A013D03825A389','user.zip',0,NULL,'10461',13,NULL),
	(149,'2012-06-14 14:11:40','2013-03-12 18:10:00',X'FE162691EFD04C889F2DD6A7A0737B81','user.phone',0,NULL,'718-600-4455',13,NULL),
	(150,'2012-06-14 14:11:40','2013-03-12 18:10:00',X'323DA29E1C29470792A5A258C1DB2929','user.fax',0,NULL,'',13,NULL);

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

INSERT INTO `userrole` (`id`, `created`, `updated`, `uuid`, `roleid`, `userid`, `lastupdatebyuser`)
VALUES
	(1,'2012-05-23 15:55:46','2013-03-12 12:39:41',X'6DCFCF48680B4536914F1CD38D407913',11,1,1),
	(2,'2012-05-23 19:25:50','2013-03-12 12:39:41',X'EC79AB0DF4434CE786E33AA6DCF2BDD4',1,2,1),
	(4,'2012-06-14 13:43:46','2013-03-12 12:39:41',X'3EC8D7F91C604388A42D29604A31CB05',3,11,1),
	(5,'2012-06-14 13:44:54','2013-03-12 12:39:41',X'5FB0FB8E7BEC4CE289826121533E3D86',5,4,1);

/*!40000 ALTER TABLE `userrole` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflow
# ------------------------------------------------------------

LOCK TABLES `workflow` WRITE;
/*!40000 ALTER TABLE `workflow` DISABLE KEYS */;

INSERT INTO `workflow` (`id`, `created`, `updated`, `uuid`, `createts`, `iname`, `isactive`, `name`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:03:30','2013-03-12 18:10:04',X'1D0AF0E8C19647AC97BF6881DED8EA79','2012-12-20 11:03:30','wasp-bisulfiteSeqPlugin',1,'BISUL Seq',0),
	(2,'2012-12-20 11:03:31','2013-03-12 18:10:04',X'98F8736B1C634B54B29299A36AF480D5','2012-12-20 11:03:31','wasp-chipSeqPlugin',1,'ChIP Seq',0),
	(3,'2012-12-20 11:03:31','2013-03-12 18:10:04',X'7609E24374CC4361A0FE6E8A077B391E','2012-12-20 11:03:31','wasp-genericDnaSeqPlugin',1,'Generic DNA Seq',0),
	(4,'2012-12-20 11:03:31','2013-03-12 18:10:04',X'6E79BFE149BB48C89C065B48E45405DC','2012-12-20 11:03:31','wasp-helpTagPlugin',1,'HELP Tagging',0);

/*!40000 ALTER TABLE `workflow` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowmeta
# ------------------------------------------------------------

LOCK TABLES `workflowmeta` WRITE;
/*!40000 ALTER TABLE `workflowmeta` DISABLE KEYS */;

INSERT INTO `workflowmeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `workflowid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:03:30','2013-03-12 18:10:00',X'5F26684A843940949BBC14967825569F','workflow.jobFlowBatchJob',0,NULL,'default.waspJob.jobflow.v1',1,NULL),
	(2,'2012-12-20 11:03:30','2013-03-12 18:10:00',X'5005009F92EB43738FB90F1E9F383444','workflow.submitpageflow',0,NULL,'/jobsubmit/modifymeta/{n};/jobsubmit/resource/mps/{n};/jobsubmit/samples/{n};/jobsubmit/cells/{n};/jobsubmit/software/bisulfiteSeqPipeline/{n};/jobsubmit/submit/{n};/jobsubmit/ok',1,NULL),
	(3,'2012-12-20 11:03:31','2013-03-12 18:10:00',X'88C026F02878429190F11EF19D9DBE1B','workflow.jobFlowBatchJob',0,NULL,'default.waspJob.jobflow.v1',2,NULL),
	(4,'2012-12-20 11:03:31','2013-03-12 18:10:00',X'49A5B7945AD3411785F99ADAECD9C779','workflow.submitpageflow',0,NULL,'/jobsubmit/modifymeta/{n};/jobsubmit/resource/mps/{n};/jobsubmit/samples/{n};/jobsubmit/cells/{n};/jobsubmit/chipSeq/pair/{n};/jobsubmit/software/referenceBasedAligner/{n};/jobsubmit/software/peakcaller/{n};/jobsubmit/comment/{n};/jobsubmit/verify/{n};/jobsubmit/submit/{n};/jobsubmit/ok',2,NULL),
	(5,'2012-12-20 11:03:31','2013-03-12 18:10:00',X'613B63752AAD4BC5BAD439AAD10DD480','workflow.jobFlowBatchJob',0,NULL,'default.waspJob.jobflow.v1',3,NULL),
	(6,'2012-12-20 11:03:31','2013-03-12 18:10:00',X'81B8DDE97221411C8EA46B2D91024D79','workflow.submitpageflow',0,NULL,'/jobsubmit/modifymeta/{n};/jobsubmit/resource/mps/{n};/jobsubmit/samples/{n};/jobsubmit/cells/{n};/jobsubmit/software/referenceBasedAligner/{n};/jobsubmit/comment/{n};/jobsubmit/verify/{n};/jobsubmit/submit/{n};/jobsubmit/ok',3,NULL),
	(7,'2012-12-20 11:03:31','2013-03-12 18:10:00',X'8C16DA04A9D649EE83A2852049D6A996','workflow.jobFlowBatchJob',0,NULL,'default.waspJob.jobflow.v1',4,NULL),
	(8,'2012-12-20 11:03:31','2013-03-12 18:10:00',X'4A140F80678D4FE199E16A1D3E7E348C','workflow.submitpageflow',0,NULL,'/jobsubmit/modifymeta/{n};/jobsubmit/resource/mps/{n};/jobsubmit/samples/{n};/jobsubmit/cells/{n};/jobsubmit/helpTag/pair/{n};/jobsubmit/software/aligner/{n};/jobsubmit/software/helptagPipeline/{n};/jobsubmit/verify/{n};/jobsubmit/submit/{n};/jobsubmit/ok',4,NULL);

/*!40000 ALTER TABLE `workflowmeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowresourcecategory
# ------------------------------------------------------------

LOCK TABLES `workflowresourcecategory` WRITE;
/*!40000 ALTER TABLE `workflowresourcecategory` DISABLE KEYS */;

INSERT INTO `workflowresourcecategory` (`id`, `created`, `updated`, `uuid`, `resourcecategoryid`, `workflowid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'CE26140F514D4D4CB49AE95BBA6D7ADF',1,1,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'36CD93AED2A043A997044E5D1720B1BA',2,1,NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'7AD719767BE64514838A323FBD6582DF',1,2,NULL),
	(4,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'62FD43596D2B4A6680641CE90F1568F5',2,2,NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'D4CE9F5C1F304C58B19C6BCD47BB44CB',1,3,NULL),
	(6,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'2266C86CFBFE493196FC70D7AA0B44A6',2,3,NULL),
	(7,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'FCC88A9A61824052B2493627552A4D81',1,4,NULL),
	(8,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'1E6ED454CDF3457080ABC32832EA4C59',2,4,NULL);

/*!40000 ALTER TABLE `workflowresourcecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowresourcecategorymeta
# ------------------------------------------------------------

LOCK TABLES `workflowresourcecategorymeta` WRITE;
/*!40000 ALTER TABLE `workflowresourcecategorymeta` DISABLE KEYS */;

INSERT INTO `workflowresourcecategorymeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `workflowresourcecategoryid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:05:42','2013-03-12 12:39:41',X'407AEB75D78549198529BB934AA16AEC','illuminaHiSeq2000.allowableUiField.readType',1,NULL,'single:single;paired:paired;',1,1),
	(2,'2012-12-20 11:05:42','2013-03-12 12:39:41',X'AC4671CA1B0A4526B1EE134113D32E21','illuminaHiSeq2000.allowableUiField.readlength',2,NULL,'100:100;150:150;',1,1),
	(3,'2012-12-20 11:05:42','2013-03-12 12:39:41',X'CEFCB875217246138019AA3E7FA94FB4','illuminaMiSeqPersonalSequencer.allowableUiField.readlength',1,NULL,'100:100;150:150;',2,1),
	(4,'2012-12-20 11:05:42','2013-03-12 12:39:41',X'87BB9877B4004BC1A25A8C8A66645C9F','illuminaMiSeqPersonalSequencer.allowableUiField.readType',2,NULL,'single:single;paired:paired;',2,1),
	(5,'2012-12-20 11:06:14','2013-03-12 12:39:41',X'F43F1773DDA8476796C1426B8101F464','illuminaHiSeq2000.allowableUiField.readType',1,NULL,'single:single;paired:paired;',3,1),
	(6,'2012-12-20 11:06:14','2013-03-12 12:39:41',X'47D8A5218DAB4DEF9BA870BCB8139E1E','illuminaHiSeq2000.allowableUiField.readlength',2,NULL,'50:50;75:75;100:100;150:150;',3,1),
	(7,'2012-12-20 11:06:14','2013-03-12 12:39:41',X'95FC99BCC05947108619252ED8435009','illuminaMiSeqPersonalSequencer.allowableUiField.readlength',1,NULL,'36:36;50:50;100:100;150:150;',4,1),
	(8,'2012-12-20 11:06:14','2013-03-12 12:39:41',X'61764875E67A4684A2CB200F55CF2B7E','illuminaMiSeqPersonalSequencer.allowableUiField.readType',2,NULL,'single:single;paired:paired;',4,1),
	(9,'2012-12-20 11:06:30','2013-03-12 12:39:41',X'BA34D8EF1C154BCD8904657F5EDCCAB8','illuminaHiSeq2000.allowableUiField.readType',1,NULL,'paired:paired;',5,1),
	(10,'2012-12-20 11:06:30','2013-03-12 12:39:41',X'F0B95147CB494DA49B047F50BC3BCE1B','illuminaHiSeq2000.allowableUiField.readlength',2,NULL,'50:50;75:75;100:100;150:150;',5,1),
	(11,'2012-12-20 11:06:30','2013-03-12 12:39:41',X'0A9A424593D44BF39D5B2BE610A65FA7','illuminaMiSeqPersonalSequencer.allowableUiField.readlength',1,NULL,'25:25;36:36;50:50;100:100;150:150;',6,1),
	(12,'2012-12-20 11:06:30','2013-03-12 12:39:41',X'724C699022DB4D56952F656A9ABA4DD1','illuminaMiSeqPersonalSequencer.allowableUiField.readType',2,NULL,'single:single;paired:paired;',6,1),
	(13,'2012-12-20 11:06:43','2013-03-12 12:39:41',X'FDD705E509C24CD7B7F2AF35E2F6F778','illuminaHiSeq2000.allowableUiField.readType',1,NULL,'single:single;',7,1),
	(14,'2012-12-20 11:06:43','2013-03-12 12:39:41',X'736965DB79614581AB9484553C51F00E','illuminaHiSeq2000.allowableUiField.readlength',2,NULL,'50:50;75:75;',7,1),
	(15,'2012-12-20 11:06:43','2013-03-12 12:39:41',X'DA0496E89C70479A8E17901654891896','illuminaMiSeqPersonalSequencer.allowableUiField.readlength',1,NULL,'36:36;50:50;',8,1),
	(16,'2012-12-20 11:06:43','2013-03-12 12:39:41',X'75B2F9247366413C917E9B7E13F05E35','illuminaMiSeqPersonalSequencer.allowableUiField.readType',2,NULL,'single:single;',8,1);

/*!40000 ALTER TABLE `workflowresourcecategorymeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowresourcetype
# ------------------------------------------------------------

LOCK TABLES `workflowresourcetype` WRITE;
/*!40000 ALTER TABLE `workflowresourcetype` DISABLE KEYS */;

INSERT INTO `workflowresourcetype` (`id`, `created`, `updated`, `uuid`, `resourcetypeid`, `workflowid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'81CD48DEB43943F5B5AC438099746CFA',2,1,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'BBE2EAD15E6C4F2C8DFAB3F60A81F806',6,1,NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'D74C4A3A2281432AA8C1C92EC4A1D4AF',2,2,NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'B461523CE228404DB61261E5936FEB12',4,2,NULL),
	(6,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'4DFFC7D959244F969B3A3712645FF8D4',2,3,NULL),
	(8,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'199CF52AB917407B8AD780BEA204641C',2,4,NULL),
	(10,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'566048681FB44E3DB196F2DFD93DE44D',7,4,NULL),
	(11,'2013-03-12 12:39:45','2013-03-12 18:10:00',X'98191B8C361142F693807203E9C1CC33',8,2,NULL),
	(12,'2013-03-12 12:39:45','2013-03-12 18:10:00',X'7C81FE6C213742639F231291D1E67263',8,3,NULL),
	(13,'2013-03-12 12:39:46','2013-03-12 18:10:00',X'57ACAF6FD6A84B19846AA4C32D5C4F21',8,4,NULL);

/*!40000 ALTER TABLE `workflowresourcetype` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowsamplesubtype
# ------------------------------------------------------------

LOCK TABLES `workflowsamplesubtype` WRITE;
/*!40000 ALTER TABLE `workflowsamplesubtype` DISABLE KEYS */;

INSERT INTO `workflowsamplesubtype` (`id`, `created`, `updated`, `uuid`, `samplesubtypeid`, `workflowid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'8494D0A06CF049B189E94F26F0DF9FCA',2,1,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'5FDAD32DB21F480AAA28E3211F5AAE67',3,1,NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'BA33FFE5438D4E408AC7BE4C821A8D1F',4,1,NULL),
	(4,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'FFF92CA4418A4091866CC260BA4005F4',5,2,NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'FF3B01B772DB42298E7C9ED5659B5113',6,2,NULL),
	(6,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'ACC242019E65485AB41E0D18C5986954',7,2,NULL),
	(7,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'D45993423B4E4530ABFEA37D09830E93',8,4,NULL),
	(8,'2013-03-12 12:39:45','2013-03-12 18:10:00',X'3E5BA3C3AEEA41C89E168C470DB49A0C',13,3,NULL),
	(9,'2013-03-12 12:39:45','2013-03-12 18:10:00',X'4BAA48B3E5994E2E9D1F89AD4D13DB06',11,3,NULL),
	(10,'2013-03-12 12:39:45','2013-03-12 18:10:00',X'9ABFD096C74143FFBE8B30FB76646B0B',12,3,NULL);

/*!40000 ALTER TABLE `workflowsamplesubtype` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowsoftware
# ------------------------------------------------------------

LOCK TABLES `workflowsoftware` WRITE;
/*!40000 ALTER TABLE `workflowsoftware` DISABLE KEYS */;

INSERT INTO `workflowsoftware` (`id`, `created`, `updated`, `uuid`, `softwareid`, `workflowid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'3E42688D8ECB48F7BFF595AD8266AFC5',3,1,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'42F7FC857CE543F3ADC4655DDC31F4BD',1,2,NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'18650F409B534C53A37119301537AA07',2,2,NULL),
	(4,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'D06782B0D3354979890A5DC5735D5AFD',1,3,NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'9B56C7BD2F344D17A4C21954EFCAA120',1,4,NULL),
	(6,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'5F569BF1C13E4DCB88E58714B652300F',4,4,NULL);

/*!40000 ALTER TABLE `workflowsoftware` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowsoftwaremeta
# ------------------------------------------------------------



# Dump of table wrole
# ------------------------------------------------------------

LOCK TABLES `wrole` WRITE;
/*!40000 ALTER TABLE `wrole` DISABLE KEYS */;

INSERT INTO `wrole` (`id`, `created`, `updated`, `uuid`, `domain`, `name`, `rolename`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'9C3F73294B114AFDA43A861E45F90323','system','Facilities Manager','fm',NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'9A83A90F669646DF8C5893932B714CA6','system','System Administrator','sa',NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'1246ED2D05A5474EB0547B091F035CEB','system','General Administrator','ga',NULL),
	(4,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'5899784899B841D5B9181C1DFB2137D4','department','Department Administrator','da',NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'15AC1B7B67154CFD908640F7F4D45A61','system','Facilities Tech','ft',NULL),
	(6,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'CCACC1BD0F5440C69277E286AA51CB1C','lab','Primary Investigator','pi',NULL),
	(7,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'A95204D19D1A44C8A6AC63E282AD3DA3','lab','Lab Manager','lm',NULL),
	(8,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'8BEA294813A740C197E27159AA6EC082','lab','Lab Member','lu',NULL),
	(9,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'0A892087C552488EA7AACCDB32668666','job','Job Submitter','js',NULL),
	(10,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'64188E72A36A4C75AAA4E9C22D8C6D31','job','Job Viewer','jv',NULL),
	(11,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'884D477070704C72B616C211113FF889','system','Super User','su',NULL),
	(12,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'91748F61E6E44FEDB91A1F1CF95C492E','lab','Lab Member Inactive','lx',NULL),
	(13,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'33640EC84E564160AD486E9D770EFB58','lab','Lab Member Pending','lp',NULL),
	(14,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'22FE7ABD37CF4F30A1D4D69EA30EBAA9','jobdraft','Job Drafter','jd',NULL),
	(15,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'50AF9690E5D340A0A048BFA09F3574F3','user','User','u',NULL),
	(16,'2013-03-12 12:39:40','2013-03-12 18:10:00',X'32A92639A1A04339AB7A1C4A8F64E2A5','system','wasp','w',NULL);

/*!40000 ALTER TABLE `wrole` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table wuser
# ------------------------------------------------------------

LOCK TABLES `wuser` WRITE;
/*!40000 ALTER TABLE `wuser` DISABLE KEYS */;

INSERT INTO `wuser` (`id`, `created`, `updated`, `uuid`, `email`, `firstname`, `isactive`, `lastname`, `locale`, `login`, `password`, `lastupdatebyuser`)
VALUES
	(0,'2013-03-12 12:39:40','2013-03-12 12:39:41',X'A51CBFE14EE74AA7A1BB55CB121D186F','wasp@einstein.yu.edu','Wasp',0,'System','en_US','wasp','12dea96fec20593566ab75692c9949596833adc9',1),
	(1,'2012-06-14 13:49:39','2013-03-12 12:39:41',X'F80DAC4D82CE49F89725374EAF5F4C6E','super@super.com','Super',1,'User','en_US','super','12dea96fec20593566ab75692c9949596833adc9',1),
	(2,'2012-06-14 13:49:50','2013-03-12 12:39:41',X'EBAD7E56CB584708AC1AC29683DEE8F2','john.smithl@abc.edu','John',1,'Smith','en_US','jsmith','12dea96fec20593566ab75692c9949596833adc9',1),
	(3,'2012-06-14 13:49:57','2013-03-12 12:39:41',X'0D72336D70E742EEB2E36D1D99809B10','j.doe@abc.com','Joe',1,'Doe','en_US','jdoe','12dea96fec20593566ab75692c9949596833adc9',1),
	(4,'2012-06-14 13:50:12','2013-03-12 12:39:41',X'8D4F9F940F214731B2A9937D6B78C27B','peter@abc2.com','Peter',1,'Walters','en_US','pwalters','a609590597a1907002ddaa51054df6d6d7758005',1),
	(5,'2012-06-14 13:47:46','2013-03-12 12:39:41',X'3D1C8E014DB6484E97410863D083432C','ss@abc.com','Sally',1,'Smythe','en_US','ssmythe','12dea96fec20593566ab75692c9949596833adc9',1),
	(6,'2012-06-14 13:47:53','2013-03-12 12:39:41',X'370581596FEB4E1F9C9287E5A6D46AD9','npeters@abc.com','Nancy',1,'Peters','en_US','npeters','12dea96fec20593566ab75692c9949596833adc9',1),
	(7,'2012-06-14 13:50:32','2013-03-12 12:39:41',X'5C4A9363181043A096C552AE49927D05','andrew.godwin@abc.com','Andrew',1,'Godwin','en_US','agodwin','12dea96fec20593566ab75692c9949596833adc9',1),
	(8,'2012-06-14 13:48:05','2013-03-12 12:39:41',X'4B27CB1EA4534C6CA4D2D5FE055D6267','franny@abc.com','Fran',1,'Williams','en_US','fwilliams','12dea96fec20593566ab75692c9949596833adc9',1),
	(9,'2012-06-14 14:07:09','2013-03-12 12:39:41',X'341557F5DFDA402DB080D2C894151B52','fishyface@abc.com','Barrry',1,'Fish','en_US','bfish','12dea96fec20593566ab75692c9949596833adc9',1),
	(10,'2012-06-14 13:48:31','2013-03-12 12:39:41',X'045BF82ECE7942A6BF5840902F7F6151','Liu@abc.com','Percy',1,'Liu','en_US','pliu','12dea96fec20593566ab75692c9949596833adc9',1),
	(11,'2012-06-13 22:03:43','2013-03-12 12:39:41',X'D03FF0B3516746158E5B5FB8DF8B6DFD','robin@abc.com','Robin',1,'Lister','en_US','robin','12dea96fec20593566ab75692c9949596833adc9',1),
	(12,'2012-06-14 13:48:40','2013-03-12 12:39:41',X'96721D8E94084AA2BED8C839D99881F3','mac@abc.com','Simon',1,'McDonald','en_US','mac','12dea96fec20593566ab75692c9949596833adc9',1),
	(13,'2012-06-14 14:12:28','2013-03-12 12:39:41',X'7927EF8FA55E4E5AB48B61C0398827FD','gd@abc.com','Grainne',1,'O\'Donovan','en_US','gdon','12dea96fec20593566ab75692c9949596833adc9',1);

/*!40000 ALTER TABLE `wuser` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
