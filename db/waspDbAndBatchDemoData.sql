# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.1.70)
# Database: wasp
# Generation Time: 2013-08-06 14:05:07 +0000
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
	(1,'2012-12-20 11:22:17','2013-03-12 18:20:20',X'4183111F83BA4A80B84B6FAC2675E7FD','acctQuote.library_cost',0,NULL,'600',1,NULL),
	(2,'2012-12-20 11:22:17','2013-03-12 18:20:20',X'4CAC1868E9D64F3FBB842A3FE7885D26','acctQuote.sample_cost',0,NULL,'0',1,NULL),
	(3,'2012-12-20 11:22:17','2013-03-12 18:20:20',X'2247B2E13C3B4E20995274F46BB12B63','acctQuote.lane_cost',0,NULL,'2000',1,NULL);

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
	(1,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'74CE3F2253344D8E85031666095358F5',1,1,'CGCTGCTG','illuminaHelptagLibrary1',1,'helptag Adaptor','CGCTGCTG',NULL),
	(2,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'A837C8CC868D41FA87A2D0A612C9912B',2,1,'ATCACG','illuminaTrueseqDnaIndexed1',1,'TruSeq Adaptor, Index 1 (ATCACG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(3,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'1ED57115281E490F9D6ADFCDC287EDD1',2,2,'CGATGT','illuminaTrueseqDnaIndexed2',1,'TruSeq Adaptor, Index 2 (CGATGT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(4,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'DD24F801802E40059160FEA94CDA878E',2,3,'TTAGGC','illuminaTrueseqDnaIndexed3',1,'TruSeq Adaptor, Index 3 (TTAGGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(5,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'75C5A4D3C3F546CB9F33B26150CE8EBC',2,4,'TGACCA','illuminaTrueseqDnaIndexed4',1,'TruSeq Adaptor, Index 4 (TGACCA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(6,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'46C2438D72644CD3B3AE4D0177891551',2,5,'ACAGTG','illuminaTrueseqDnaIndexed5',1,'TruSeq Adaptor, Index 5 (ACAGTG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(7,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'ACB825EC1CEF4EA19E3B7B4BEDEFFF44',2,6,'GCCAAT','illuminaTrueseqDnaIndexed6',1,'TruSeq Adaptor, Index 6 (GCCAAT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(8,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'7E2A7308F5B7416A92DABE5EEFFE95AF',2,7,'CAGATC','illuminaTrueseqDnaIndexed7',1,'TruSeq Adaptor, Index 7 (CAGATC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(9,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'BFC01DAF7CA449A8A8C6A922D56171B9',2,8,'ACTTGA','illuminaTrueseqDnaIndexed8',1,'TruSeq Adaptor, Index 8 (ACTTGA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(10,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'775649FC55FC43B3AA42E46C89519F22',2,9,'GATCAG','illuminaTrueseqDnaIndexed9',1,'TruSeq Adaptor, Index 9 (GATCAG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(11,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'DFB519B5DAEC4489BE2C72F43A997A18',2,10,'TAGCTT','illuminaTrueseqDnaIndexed10',1,'TruSeq Adaptor, Index 10 (TAGCTT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(12,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'1F5A641100534A7EA3108CD0CDE121DA',2,11,'GGCTAC','illuminaTrueseqDnaIndexed11',1,'TruSeq Adaptor, Index 11 (GGCTAC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(13,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'C2E47D1ABF1045E7A8329374E85DB2D1',2,12,'CTTGTA','illuminaTrueseqDnaIndexed12',1,'TruSeq Adaptor, Index 12 (CTTGTA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(14,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'29C4FCCBA0154ACEB6C16B5ECAB9F0DA',2,13,'AGTCAA','illuminaTrueseqDnaIndexed13',1,'TruSeq Adaptor, Index 13 (AGTCAA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(15,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'429A5ECE605B4ED3AEA98F825A6F8102',2,14,'AGTTCC','illuminaTrueseqDnaIndexed14',1,'TruSeq Adaptor, Index 14 (AGTTCC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(16,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'65F7417AA9AA443A90A6AEAB0A777470',2,15,'ATGTCA','illuminaTrueseqDnaIndexed15',1,'TruSeq Adaptor, Index 15 (ATGTCA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(17,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'663AF2664CEC4907B1733A69B42F5830',2,16,'CCGTCC','illuminaTrueseqDnaIndexed16',1,'TruSeq Adaptor, Index 16 (CCGTCC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(18,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'54AF4AF9A596433FB6BEF530B0347DC9',2,17,'GTAGAG','illuminaTrueseqDnaIndexed17',1,'TruSeq Adaptor, Index 17 (GTAGAG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(19,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'976447EFE6744C22AD4905AED287D209',2,18,'GTCCGC','illuminaTrueseqDnaIndexed18',1,'TruSeq Adaptor, Index 18 (GTCCGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(20,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'289674BD9F824956887106C292989C68',2,19,'GTGAAA','illuminaTrueseqDnaIndexed19',1,'TruSeq Adaptor, Index 19 (GTGAAA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(21,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'4618F8F9BC6744248BE8C74DD14629B8',2,20,'GTGGCC','illuminaTrueseqDnaIndexed20',1,'TruSeq Adaptor, Index 20 (GTGGCC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(22,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'3C20CB6CAB544DD980FED6243CC072B3',2,21,'GTTTCG','illuminaTrueseqDnaIndexed21',1,'TruSeq Adaptor, Index 21 (GTTTCG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(23,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'2185A85583B44B79935B5CCF2C4FE5FC',2,22,'CGTACG','illuminaTrueseqDnaIndexed22',1,'TruSeq Adaptor, Index 22 (CGTACG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(24,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'F6AD3ECC38A549958710059CFB2BE9CB',2,23,'GAGTGG','illuminaTrueseqDnaIndexed23',1,'TruSeq Adaptor, Index 23 (GAGTGG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(25,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'60A2CFC1592B4FF3AC95AD8AF956934C',2,24,'GGTAGC','illuminaTrueseqDnaIndexed24',1,'TruSeq Adaptor, Index 24 (GGTAGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(26,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'D1F370E031B144FABEC4AB0005D7919B',2,25,'ACTGAT','illuminaTrueseqDnaIndexed25',1,'TruSeq Adaptor, Index 25 (ACTGAT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(27,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'CB50600D2D2D4F4E83E102F7E46A2324',2,26,'ATGAGC','illuminaTrueseqDnaIndexed26',1,'TruSeq Adaptor, Index 26 (ATGAGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(28,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'533B15B322334435B573156477239FA1',2,27,'ATTCCT','illuminaTrueseqDnaIndexed27',1,'TruSeq Adaptor, Index 27 (ATTCCT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(29,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'F24F17CA762C4BC0A01B784806CA4BF2',2,28,'CAAAAG','illuminaTrueseqDnaIndexed28',1,'TruSeq Adaptor, Index 28 (CAAAAG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(30,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'B74DAB4CF9BC497FB2E980B0F6B4879E',2,29,'CAACTA','illuminaTrueseqDnaIndexed29',1,'TruSeq Adaptor, Index 29 (CAACTA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(31,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'1960178394D74B3695E77F493E9B22FC',2,30,'CACCGG','illuminaTrueseqDnaIndexed30',1,'TruSeq Adaptor, Index 30 (CACCGG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(32,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'3F549C17B8744FF1B2817A34B0C6B3B0',2,31,'CACGAT','illuminaTrueseqDnaIndexed31',1,'TruSeq Adaptor, Index 31 (CACGAT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(33,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'6DD39FCB02104E2C8A4A8A2870EE051A',2,32,'CACTCA','illuminaTrueseqDnaIndexed32',1,'TruSeq Adaptor, Index 32 (CACTCA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(34,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'90A3FB32443647DEB082FA43D2B9BBF0',2,33,'CAGGCG','illuminaTrueseqDnaIndexed33',1,'TruSeq Adaptor, Index 33 (CAGGCG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(35,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'9797718DCE15418AA267CDC19C576BB3',2,34,'CATGGC','illuminaTrueseqDnaIndexed34',1,'TruSeq Adaptor, Index 34 (CATGGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(36,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'A5D2DEC7E15E4CEE921CB0376DEBD334',2,35,'CATTTT','illuminaTrueseqDnaIndexed35',1,'TruSeq Adaptor, Index 35 (CATTTT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(37,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'095D77EFEDA0474AA40F74B52F95C397',2,36,'CCAACA','illuminaTrueseqDnaIndexed36',1,'TruSeq Adaptor, Index 36 (CCAACA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(38,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'4F2363E3D68C41B2A21A48FEB470538E',2,37,'CGGAAT','illuminaTrueseqDnaIndexed37',1,'TruSeq Adaptor, Index 37 (CGGAAT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(39,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'420A960043E843F587840ECE72F4C539',2,38,'CTAGCT','illuminaTrueseqDnaIndexed38',1,'TruSeq Adaptor, Index 38 (CTAGCT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(40,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'1C92E9D1CBCA42A88B490DF343D448D0',2,39,'CTATAC','illuminaTrueseqDnaIndexed39',1,'TruSeq Adaptor, Index 39 (CTATAC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(41,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'7C9FF8E58255497A907EBC900E3A37FE',2,40,'CTCAGA','illuminaTrueseqDnaIndexed40',1,'TruSeq Adaptor, Index 40 (CTCAGA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(42,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'A3CE814C316D4FF7B4A0A81E249BF234',2,41,'GACGAC','illuminaTrueseqDnaIndexed41',1,'TruSeq Adaptor, Index 41 (GACGAC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(43,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'F0FF35CA396B4D82B58B47144A1D2D0A',2,42,'TAATCG','illuminaTrueseqDnaIndexed42',1,'TruSeq Adaptor, Index 42 (TAATCG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(44,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'B2693BD950A644DFBF79AD4A8A43AF17',2,43,'TACAGC','illuminaTrueseqDnaIndexed43',1,'TruSeq Adaptor, Index 43 (TACAGC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(45,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'A3AFD8440BCB4EC3AE1101385EB6F385',2,44,'TATAAT','illuminaTrueseqDnaIndexed44',1,'TruSeq Adaptor, Index 44 (TATAAT)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(46,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'0AC9EFD65DBE41DBB68E11654643F140',2,45,'TCATTC','illuminaTrueseqDnaIndexed45',1,'TruSeq Adaptor, Index 45 (TCATTC)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(47,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'FCB575F91A154978B3F902D1888391E0',2,46,'TCCCGA','illuminaTrueseqDnaIndexed46',1,'TruSeq Adaptor, Index 46 (TCCCGA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(48,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'01B47218078741528C565AFEFC7253CA',2,47,'TCGAAG','illuminaTrueseqDnaIndexed47',1,'TruSeq Adaptor, Index 47 (TCGAAG)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL),
	(49,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'CD9B9B90F34F4181B24501C730C24BD5',2,48,'TCGGCA','illuminaTrueseqDnaIndexed48',1,'TruSeq Adaptor, Index 48 (TCGGCA)','AGATCGGAAGAGCACACGTCTGAACTCCAGTCAC',NULL);

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
	(1,'2013-03-12 12:39:39','2013-08-06 10:03:15',X'D433BC2F91F9407697F4CBCAC284B511','helptagLibrary',1,'HELP-tag Library',1,0),
	(2,'2013-03-12 12:39:39','2013-08-06 10:03:15',X'A62D573D5D454A76B44A9E1CF6263CDE','truseqIndexedDna',1,'TruSEQ INDEXED DNA',2,0);

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
	(1,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'46BB40EAFEEE4B11BCA6234A272FB9B9',1,1,NULL),
	(2,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'80BE51B417F748188FDD86889B126C86',2,1,NULL),
	(3,'2013-03-12 12:39:46','2013-03-12 18:20:20',X'040B0013AB2E48659D87E672469D6DD4',2,2,NULL);

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
	(1,2,1,'2012-12-20 11:20:04','2012-12-20 11:20:05','2013-08-05 19:11:40','FAILED','FAILED','Failed because wasp-daemon was shutdown inproperly (was found in an active state on startup)','2013-08-05 19:11:41'),
	(2,2,2,'2012-12-20 11:20:05','2012-12-20 11:20:05','2012-12-20 11:23:44','COMPLETED','COMPLETED','','2012-12-20 11:23:44'),
	(3,2,3,'2012-12-20 11:20:05','2012-12-20 11:20:05','2012-12-20 11:23:47','COMPLETED','COMPLETED','','2012-12-20 11:23:47'),
	(4,2,4,'2012-12-20 11:20:05','2012-12-20 11:20:05','2012-12-20 11:23:34','COMPLETED','COMPLETED','','2012-12-20 11:23:34'),
	(5,2,5,'2012-12-20 11:24:31','2012-12-20 11:24:31','2013-08-05 19:11:40','FAILED','FAILED','Failed because wasp-daemon was shutdown inproperly (was found in an active state on startup)','2013-08-05 19:11:41'),
	(6,2,6,'2012-12-20 11:27:07','2012-12-20 11:27:07','2013-08-05 19:11:40','FAILED','FAILED','Failed because wasp-daemon was shutdown inproperly (was found in an active state on startup)','2013-08-05 19:11:41'),
	(7,2,7,'2012-12-20 11:27:07','2012-12-20 11:27:07','2013-08-05 19:11:40','FAILED','FAILED','Failed because wasp-daemon was shutdown inproperly (was found in an active state on startup)','2013-08-05 19:11:41'),
	(8,2,8,'2012-12-20 11:27:07','2012-12-20 11:27:07','2013-08-05 19:11:40','FAILED','FAILED','Failed because wasp-daemon was shutdown inproperly (was found in an active state on startup)','2013-08-05 19:11:41'),
	(9,1,9,'2013-08-05 19:11:41','2013-08-05 19:11:41',NULL,'STARTED','UNKNOWN','','2013-08-05 19:11:41'),
	(10,1,10,'2013-08-05 19:11:41','2013-08-05 19:11:41',NULL,'STARTED','UNKNOWN','','2013-08-05 19:11:41'),
	(11,1,11,'2013-08-05 19:11:41','2013-08-05 19:11:41',NULL,'STARTED','UNKNOWN','','2013-08-05 19:11:41');

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
	(8,'{\"map\":\"\"}',NULL),
	(9,'{\"map\":\"\"}',NULL),
	(10,'{\"map\":\"\"}',NULL),
	(11,'{\"map\":\"\"}',NULL);

/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION_CONTEXT` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table BATCH_JOB_EXECUTION_PARAMS
# ------------------------------------------------------------

LOCK TABLES `BATCH_JOB_EXECUTION_PARAMS` WRITE;
/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION_PARAMS` DISABLE KEYS */;

INSERT INTO `BATCH_JOB_EXECUTION_PARAMS` (`JOB_EXECUTION_ID`, `TYPE_CD`, `KEY_NAME`, `STRING_VAL`, `DATE_VAL`, `LONG_VAL`, `DOUBLE_VAL`, `IDENTIFYING`)
VALUES
	(1,'STRING','jobId','1','1969-12-31 19:00:00',0,0,'Y'),
	(2,'STRING','jobId','1','1969-12-31 19:00:00',0,0,'Y'),
	(2,'STRING','sampleId','21','1969-12-31 19:00:00',0,0,'Y'),
	(3,'STRING','jobId','1','1969-12-31 19:00:00',0,0,'Y'),
	(3,'STRING','sampleId','22','1969-12-31 19:00:00',0,0,'Y'),
	(4,'STRING','jobId','1','1969-12-31 19:00:00',0,0,'Y'),
	(4,'STRING','sampleId','23','1969-12-31 19:00:00',0,0,'Y'),
	(5,'STRING','jobId','1','1969-12-31 19:00:00',0,0,'Y'),
	(5,'STRING','sampleId','24','1969-12-31 19:00:00',0,0,'Y'),
	(6,'STRING','jobId','2','1969-12-31 19:00:00',0,0,'Y'),
	(7,'STRING','jobId','2','1969-12-31 19:00:00',0,0,'Y'),
	(7,'STRING','sampleId','25','1969-12-31 19:00:00',0,0,'Y'),
	(8,'STRING','jobId','2','1969-12-31 19:00:00',0,0,'Y'),
	(8,'STRING','sampleId','26','1969-12-31 19:00:00',0,0,'Y'),
	(9,'STRING','jobId','1','1969-12-31 19:00:00',0,0,'N'),
	(9,'STRING','sampleId','24','1969-12-31 19:00:00',0,0,'N'),
	(10,'STRING','jobId','2','1969-12-31 19:00:00',0,0,'N'),
	(10,'STRING','sampleId','25','1969-12-31 19:00:00',0,0,'N'),
	(11,'STRING','jobId','2','1969-12-31 19:00:00',0,0,'N');

/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION_PARAMS` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table BATCH_JOB_EXECUTION_SEQ
# ------------------------------------------------------------

LOCK TABLES `BATCH_JOB_EXECUTION_SEQ` WRITE;
/*!40000 ALTER TABLE `BATCH_JOB_EXECUTION_SEQ` DISABLE KEYS */;

INSERT INTO `BATCH_JOB_EXECUTION_SEQ` (`ID`)
VALUES
	(0),
	(3),
	(3),
	(3),
	(11);

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
	(8,0,'wasp.userLibrary.jobflow.v1','5e0221792fa693d721979abaeb0a073f'),
	(9,0,'wasp.facilityLibrary.jobflow.v1','d41d8cd98f00b204e9800998ecf8427e'),
	(10,0,'wasp.userLibrary.jobflow.v1','d41d8cd98f00b204e9800998ecf8427e'),
	(11,0,'default.waspJob.jobflow.v1','d41d8cd98f00b204e9800998ecf8427e');

/*!40000 ALTER TABLE `BATCH_JOB_INSTANCE` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table BATCH_JOB_SEQ
# ------------------------------------------------------------

LOCK TABLES `BATCH_JOB_SEQ` WRITE;
/*!40000 ALTER TABLE `BATCH_JOB_SEQ` DISABLE KEYS */;

INSERT INTO `BATCH_JOB_SEQ` (`ID`)
VALUES
	(0),
	(3),
	(3),
	(3),
	(11);

/*!40000 ALTER TABLE `BATCH_JOB_SEQ` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table BATCH_STEP_EXECUTION
# ------------------------------------------------------------

LOCK TABLES `BATCH_STEP_EXECUTION` WRITE;
/*!40000 ALTER TABLE `BATCH_STEP_EXECUTION` DISABLE KEYS */;

INSERT INTO `BATCH_STEP_EXECUTION` (`STEP_EXECUTION_ID`, `VERSION`, `STEP_NAME`, `JOB_EXECUTION_ID`, `START_TIME`, `END_TIME`, `STATUS`, `COMMIT_COUNT`, `READ_COUNT`, `FILTER_COUNT`, `WRITE_COUNT`, `READ_SKIP_COUNT`, `WRITE_SKIP_COUNT`, `PROCESS_SKIP_COUNT`, `ROLLBACK_COUNT`, `EXIT_CODE`, `EXIT_MESSAGE`, `LAST_UPDATED`)
VALUES
	(1,85226,'default.waspJob.step.listenForExitCondition',1,'2012-12-20 11:20:05','2013-08-05 19:11:40','FAILED',85224,0,0,0,0,0,0,0,'FAILED','Failed because wasp-daemon was shutdown inproperly (was found in an active state on startup)','2013-08-05 19:11:41'),
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
	(21,54262,'wasp.library.step.listenForExitCondition',5,'2012-12-20 11:24:31','2013-08-05 19:11:40','FAILED',54260,0,0,0,0,0,0,0,'FAILED','Failed because wasp-daemon was shutdown inproperly (was found in an active state on startup)','2013-08-05 19:11:41'),
	(22,54272,'wasp.library.step.libraryQC',5,'2012-12-20 11:24:31','2013-08-05 19:11:40','FAILED',54270,0,0,0,0,0,0,0,'FAILED','Failed because wasp-daemon was shutdown inproperly (was found in an active state on startup)','2013-08-05 19:11:41'),
	(23,33287,'default.waspJob.step.listenForExitCondition',6,'2012-12-20 11:27:07','2013-08-05 19:11:40','FAILED',33285,0,0,0,0,0,0,0,'FAILED','Failed because wasp-daemon was shutdown inproperly (was found in an active state on startup)','2013-08-05 19:11:41'),
	(24,33357,'default.waspJob.step.quote',6,'2012-12-20 11:27:07','2013-08-05 19:11:40','FAILED',33355,0,0,0,0,0,0,0,'FAILED','Failed because wasp-daemon was shutdown inproperly (was found in an active state on startup)','2013-08-05 19:11:41'),
	(25,33272,'default.waspJob.step.piApprove',6,'2012-12-20 11:27:07','2013-08-05 19:11:40','FAILED',33270,0,0,0,0,0,0,0,'FAILED','Failed because wasp-daemon was shutdown inproperly (was found in an active state on startup)','2013-08-05 19:11:41'),
	(26,33274,'default.waspJob.step.adminApprove',6,'2012-12-20 11:27:07','2013-08-05 19:11:40','FAILED',33272,0,0,0,0,0,0,0,'FAILED','Failed because wasp-daemon was shutdown inproperly (was found in an active state on startup)','2013-08-05 19:11:41'),
	(27,33377,'wasp.library.step.listenForExitCondition',7,'2012-12-20 11:27:07','2013-08-05 19:11:40','FAILED',33375,0,0,0,0,0,0,0,'FAILED','Failed because wasp-daemon was shutdown inproperly (was found in an active state on startup)','2013-08-05 19:11:41'),
	(28,33213,'wasp.library.step.listenForJobApproved',7,'2012-12-20 11:27:07','2013-08-05 19:11:40','FAILED',33211,0,0,0,0,0,0,0,'FAILED','Failed because wasp-daemon was shutdown inproperly (was found in an active state on startup)','2013-08-05 19:11:41'),
	(29,33292,'wasp.library.step.listenForLibraryReceived',7,'2012-12-20 11:27:07','2013-08-05 19:11:40','FAILED',33290,0,0,0,0,0,0,0,'FAILED','Failed because wasp-daemon was shutdown inproperly (was found in an active state on startup)','2013-08-05 19:11:41'),
	(30,33221,'wasp.library.step.listenForJobApproved',8,'2012-12-20 11:27:07','2013-08-05 19:11:40','FAILED',33219,0,0,0,0,0,0,0,'FAILED','Failed because wasp-daemon was shutdown inproperly (was found in an active state on startup)','2013-08-05 19:11:41'),
	(31,33174,'wasp.library.step.listenForLibraryReceived',8,'2012-12-20 11:27:07','2013-08-05 19:11:40','FAILED',33172,0,0,0,0,0,0,0,'FAILED','Failed because wasp-daemon was shutdown inproperly (was found in an active state on startup)','2013-08-05 19:11:41'),
	(32,33201,'wasp.library.step.listenForExitCondition',8,'2012-12-20 11:27:07','2013-08-05 19:11:40','FAILED',33199,0,0,0,0,0,0,0,'FAILED','Failed because wasp-daemon was shutdown inproperly (was found in an active state on startup)','2013-08-05 19:11:41'),
	(33,98636,'wasp.library.step.libraryQC',9,'2013-08-05 19:11:41',NULL,'STARTED',98635,0,0,0,0,0,0,0,'EXECUTING','','2013-08-05 19:24:57'),
	(34,98628,'wasp.library.step.listenForExitCondition',9,'2013-08-05 19:11:41',NULL,'STARTED',98627,0,0,0,0,0,0,0,'EXECUTING','','2013-08-05 19:24:57'),
	(35,98557,'wasp.library.step.listenForExitCondition',10,'2013-08-05 19:11:41',NULL,'STARTED',98556,0,0,0,0,0,0,0,'EXECUTING','','2013-08-05 19:24:57'),
	(36,98646,'wasp.library.step.listenForJobApproved',10,'2013-08-05 19:11:41',NULL,'STARTED',98645,0,0,0,0,0,0,0,'EXECUTING','','2013-08-05 19:24:57'),
	(37,98673,'wasp.library.step.listenForLibraryReceived',10,'2013-08-05 19:11:41',NULL,'STARTED',98672,0,0,0,0,0,0,0,'EXECUTING','','2013-08-05 19:24:57'),
	(38,98670,'default.waspJob.step.listenForExitCondition',11,'2013-08-05 19:11:41',NULL,'STARTED',98669,0,0,0,0,0,0,0,'EXECUTING','','2013-08-05 19:24:57'),
	(39,3,'default.waspJob.step.started',11,'2013-08-05 19:11:41','2013-08-05 19:11:41','COMPLETED',1,0,0,0,0,0,0,0,'COMPLETED','','2013-08-05 19:11:41'),
	(40,98481,'default.waspJob.step.fmApprove',11,'2013-08-05 19:11:41',NULL,'STARTED',98480,0,0,0,0,0,0,0,'EXECUTING','','2013-08-05 19:24:57'),
	(41,98537,'default.waspJob.step.quote',11,'2013-08-05 19:11:41',NULL,'STARTED',98536,0,0,0,0,0,0,0,'EXECUTING','','2013-08-05 19:24:57'),
	(42,98659,'default.waspJob.step.daApprove',11,'2013-08-05 19:11:41',NULL,'STARTED',98658,0,0,0,0,0,0,0,'EXECUTING','','2013-08-05 19:24:57'),
	(43,98616,'default.waspJob.step.piApprove',11,'2013-08-05 19:11:41',NULL,'STARTED',98615,0,0,0,0,0,0,0,'EXECUTING','','2013-08-05 19:24:57');

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
	(32,'{\"map\":\"\"}',NULL),
	(33,'{\"map\":\"\"}',NULL),
	(34,'{\"map\":\"\"}',NULL),
	(35,'{\"map\":\"\"}',NULL),
	(36,'{\"map\":\"\"}',NULL),
	(37,'{\"map\":\"\"}',NULL),
	(38,'{\"map\":\"\"}',NULL),
	(39,'{\"map\":\"\"}',NULL),
	(40,'{\"map\":\"\"}',NULL),
	(41,'{\"map\":\"\"}',NULL),
	(42,'{\"map\":\"\"}',NULL),
	(43,'{\"map\":\"\"}',NULL);

/*!40000 ALTER TABLE `BATCH_STEP_EXECUTION_CONTEXT` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table BATCH_STEP_EXECUTION_SEQ
# ------------------------------------------------------------

LOCK TABLES `BATCH_STEP_EXECUTION_SEQ` WRITE;
/*!40000 ALTER TABLE `BATCH_STEP_EXECUTION_SEQ` DISABLE KEYS */;

INSERT INTO `BATCH_STEP_EXECUTION_SEQ` (`ID`)
VALUES
	(0),
	(11),
	(11),
	(11),
	(43);

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
	(1,'2013-03-12 12:39:44','2013-08-06 10:03:14',X'01D299359B9D466F873071F50AA0B326','A human-readable text based file format for storing DNA sequences alongside their quality scores','fastq',1,'FASTQ',NULL),
	(2,'2013-03-12 12:39:44','2013-08-06 10:03:14',X'1720F1FE13DC4F8E837A112B440B0399','A BAM file (.bam) is the binary version of a SAM file.  A SAM file (.sam) is a tab-delimited text file that contains sequence alignment data. These formats are described on the SAM Tools web site: http://samtools.sourceforge.net.','bam',1,'BAM',NULL),
	(3,'2013-03-12 12:39:46','2013-08-06 10:03:14',X'DE177E2D436143CC92FE5313844EBACE','QC files generated by CASAVA for assessing HiSeq flowcell / sequencing quality','waspIlluminaHiseqQcMetrics',1,'Illumina HiSeq QC Metrics',NULL),
	(4,'2013-08-05 19:04:24','2013-08-06 10:03:14',X'A61583C065AB4C5DBB6A9898B1DD43E7','An index corresponding to a .bam file of the same name','bai',1,'BAI',0);

/*!40000 ALTER TABLE `filetype` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table filetypemeta
# ------------------------------------------------------------



# Dump of table groupfile
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
	(1,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'33403B8BEA284B24A0C2B73288FFFFC2',1,1,NULL),
	(2,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'30E5A435E80048A7A9112D7F96570D47',1,2,NULL),
	(3,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'459304390DE14BA0BF6435DDBBDF7683',2,2,NULL);

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
	(1,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'CDC1E64D4B504263BCF05C74FA40DD5D',1,1,NULL),
	(2,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'D04B8699D0D942489E86F6DAC7FB8697',1,2,NULL),
	(3,'2013-03-12 12:39:39','2013-03-12 18:20:20',X'ED0D7B6337404A7497D79A0C42CC076F',2,2,NULL);

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
	(1,'2012-12-20 11:15:35','2013-03-12 18:20:20',X'2B414BC52B3749B49AF336D6008777C2','illuminaHiSeq2000.readLength',0,NULL,'100',1,NULL),
	(2,'2012-12-20 11:15:35','2013-03-12 18:20:20',X'6E73EC5D09154644A4640870C4C84B81','illuminaHiSeq2000.readType',0,NULL,'paired',1,NULL),
	(3,'2012-12-20 11:18:37','2013-03-12 12:39:41',X'0D9D036C80DA4631A61804070C1EAFD1','chipSeqPlugin.samplePairsTvsC',0,NULL,'1:3;2:3;',1,10),
	(4,'2012-12-20 11:18:41','2013-03-12 18:20:20',X'3E357CD343C0435EA4BE1D669503E36D','bowtieAligner.mismatches',0,NULL,'2',1,NULL),
	(5,'2012-12-20 11:18:41','2013-03-12 18:20:20',X'C4BD998792DC421AB6C4C8C99FABBE7A','bowtieAligner.seedLength',0,NULL,'32',1,NULL),
	(6,'2012-12-20 11:18:41','2013-03-12 18:20:20',X'88945C0A7B2C48B8829E7ECEDE74A572','bowtieAligner.reportAlignmentNum',0,NULL,'1',1,NULL),
	(7,'2012-12-20 11:18:41','2013-03-12 18:20:20',X'D0540B8E6C8A4EA6A05C2F4B06B7420C','bowtieAligner.discardThreshold',0,NULL,'1',1,NULL),
	(8,'2012-12-20 11:18:41','2013-03-12 18:20:20',X'8F64A0AD6373400C828645DD4C157F36','bowtieAligner.isBest',0,NULL,'yes',1,NULL),
	(9,'2012-12-20 11:19:39','2013-03-12 18:20:20',X'E6FB6714F5A1474D8444B256D93FBC0B','macsPeakcaller.pValueCutoff',0,NULL,'100000',1,NULL),
	(10,'2012-12-20 11:19:39','2013-03-12 18:20:20',X'9C87C487C5C848DCA79BA9FBCA247A7A','macsPeakcaller.bandwidth',0,NULL,'300',1,NULL),
	(11,'2012-12-20 11:19:39','2013-03-12 18:20:20',X'309870D4ED504AC0AD21E3560DBEC310','macsPeakcaller.genomeSize',0,NULL,'1000000000',1,NULL),
	(12,'2012-12-20 11:19:39','2013-03-12 18:20:20',X'21F788CCC1CD44429E243D40C2087FB2','macsPeakcaller.keepDup',0,NULL,'no',1,NULL),
	(13,'2012-12-20 11:20:03','2013-03-12 12:39:41',X'930CFB21A7C54A78958C81842A47F1B6','statusMessage.userSubmittedJobComment::0982004b-2bcf-490e-83a0-b0e86fb8293f',0,NULL,'User-submitted Job Comment::Please expedite. Grant deadline approaching!',1,10),
	(14,'2012-12-20 11:25:27','2013-03-12 18:20:20',X'D7891087406A4E99B16E6863C9277FB5','illuminaHiSeq2000.readLength',0,NULL,'100',2,NULL),
	(15,'2012-12-20 11:25:27','2013-03-12 18:20:20',X'B979FCBF050344F9A0EA377D3F4DDD43','illuminaHiSeq2000.readType',0,NULL,'paired',2,NULL),
	(16,'2012-12-20 11:26:56','2013-03-12 12:39:41',X'D6F43276F26B42CBA6EE65B6C868229D','chipSeqPlugin.samplePairsTvsC',0,NULL,'5:4;',2,10),
	(17,'2012-12-20 11:26:59','2013-03-12 18:20:20',X'7B1CE9A25CA047ED9FD56F150CDE2DE9','bowtieAligner.mismatches',0,NULL,'2',2,NULL),
	(18,'2012-12-20 11:26:59','2013-03-12 18:20:20',X'C69A3C7783EF4FD7BEEDBF9DA83D013B','bowtieAligner.seedLength',0,NULL,'32',2,NULL),
	(19,'2012-12-20 11:26:59','2013-03-12 18:20:20',X'A8EAE3257FB242B8B68FD9FA3563A818','bowtieAligner.reportAlignmentNum',0,NULL,'1',2,NULL),
	(20,'2012-12-20 11:26:59','2013-03-12 18:20:20',X'791E465E188648D1BB799D682B957FD2','bowtieAligner.discardThreshold',0,NULL,'1',2,NULL),
	(21,'2012-12-20 11:26:59','2013-03-12 18:20:20',X'F3240694CA7C4A23BA776B69EC22559A','bowtieAligner.isBest',0,NULL,'yes',2,NULL),
	(22,'2012-12-20 11:27:02','2013-03-12 18:20:20',X'1A2DEC06ECD44D20A45209178742E1E5','macsPeakcaller.pValueCutoff',0,NULL,'100000',2,NULL),
	(23,'2012-12-20 11:27:02','2013-03-12 18:20:20',X'6B8BFEBD54A348749DA5BD7DAE1C9948','macsPeakcaller.bandwidth',0,NULL,'300',2,NULL),
	(24,'2012-12-20 11:27:02','2013-03-12 18:20:20',X'5A3A6640ED2348DBA917FB1C088EDF67','macsPeakcaller.genomeSize',0,NULL,'1000000000',2,NULL),
	(25,'2012-12-20 11:27:02','2013-03-12 18:20:20',X'16A2D17D40544E8F8C2E0DE6C20EB6C1','macsPeakcaller.keepDup',0,NULL,'no',2,NULL);

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
	(1,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'63C2A4BAEDB74A9CA73A04D3FE190614','lab.internal_external_lab',0,NULL,'internal',2,NULL),
	(2,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'557986714D7E4A7390F269D1E08A64D5','lab.phone',0,NULL,'718-123-4567',2,NULL),
	(3,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'41D0FF826324422A9EEF500974142A49','lab.building_room',0,NULL,'Price 220',2,NULL),
	(4,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'EC7EED5C1FC142278492236270B102A8','lab.billing_contact',0,NULL,'John Greally',2,NULL),
	(5,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'3DAED9710C814667AC67970EF30D72EC','lab.billing_institution',0,NULL,'Einstein',2,NULL),
	(6,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'12B988232CDD49218D12DF04C88510BE','lab.billing_departmentId',0,NULL,'3',2,NULL),
	(7,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'4846665F0F024DC18CDDB05F2A55F1C9','lab.billing_building_room',0,NULL,'Price 220',2,NULL),
	(8,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'574D6A2F5E444F088E5AFF17515D83DF','lab.billing_address',0,NULL,'1301 Morris Park Ave.',2,NULL),
	(9,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'730B4683C8094D1784FB4E7796C50659','lab.billing_city',0,NULL,'Bronx',2,NULL),
	(10,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'A00A28C0B2C14A5DBDFFAF005844966F','lab.billing_state',0,NULL,'NY',2,NULL),
	(11,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'770F142D62FB430D9EC82BB1D4FC72CB','lab.billing_country',0,NULL,'US',2,NULL),
	(12,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'6F9F7D3D9D0141B19FCA62CCC75ACFC8','lab.billing_zip',0,NULL,'10461',2,NULL),
	(13,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'A7FB728067AD48A785D07FE98B7C4C4E','lab.billing_phone',0,NULL,'718-123-4567',2,NULL),
	(14,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'5E094706824542568A1A97D1274E436E','lab.internal_external_lab',0,NULL,'internal',3,NULL),
	(15,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'44477DEA40C945EDADFA8E2FAF209464','lab.phone',0,NULL,'718-678-1112',3,NULL),
	(16,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'320DC89BE7E146E5882240EBD5EF152E','lab.building_room',0,NULL,'Price 353',3,NULL),
	(17,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'5F9DA7EEAAFD44DA953BB9AE57D5343C','lab.billing_contact',0,NULL,'Aaron Goldin',3,NULL),
	(18,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'323E8FF304034F17989B48C9B1BC8502','lab.billing_institution',0,NULL,'Einstein',3,NULL),
	(19,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'DC2C37A4B4B8476EAF8743746AE3B79E','lab.billing_departmentId',0,NULL,'3',3,NULL),
	(20,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'BA8734BE5126414AA29F701331D60535','lab.billing_building_room',0,NULL,'Price 353',3,NULL),
	(21,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'D244C77610A2420596B4348F5A6AAB12','lab.billing_address',0,NULL,'1301 Morris Park Ave.',3,NULL),
	(22,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'A06C45B67D3E416DB6E06556AC9AE142','lab.billing_city',0,NULL,'Bronx',3,NULL),
	(23,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'E146A709033F4CEEBAA13371971F3F4E','lab.billing_state',0,NULL,'NY',3,NULL),
	(24,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'087D32B89E4849FE91EE352D048B9353','lab.billing_country',0,NULL,'US',3,NULL),
	(25,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'17A7289B4B104A03891212D4A5FD5FB4','lab.billing_zip',0,NULL,'10461',3,NULL),
	(26,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'B847BE70F9F74C2D85D8830019BABB3D','lab.billing_phone',0,NULL,'718-678-1112',3,NULL),
	(27,'2012-05-31 13:59:23','2013-03-12 18:20:20',X'FE571184D47F4BBAA3BCB98216A28CD8','lab.internal_external_lab',0,NULL,'internal',4,NULL),
	(28,'2012-05-31 13:59:23','2013-03-12 18:20:20',X'32046E36DBD246A4AB8F147A54BF3FA0','lab.phone',0,NULL,'718-678-1019',4,NULL),
	(29,'2012-05-31 13:59:23','2013-03-12 18:20:20',X'CDBFA00D096D4195AF4ACE4E128A0BB3','lab.building_room',0,NULL,'Price 321',4,NULL),
	(30,'2012-05-31 13:59:23','2013-03-12 18:20:20',X'953DAA481AF041FF89C2AB67A6CA807B','lab.billing_contact',0,NULL,'Adam Auton',4,NULL),
	(31,'2012-05-31 13:59:23','2013-03-12 18:20:20',X'8046A75FD5BE42849E8D79D1452AE882','lab.billing_institution',0,NULL,'Einstein',4,NULL),
	(32,'2012-05-31 13:59:23','2013-03-12 18:20:20',X'96A79F78EEFF4FDD98D3D8CA1B13AE3B','lab.billing_departmentId',0,NULL,'3',4,NULL),
	(33,'2012-05-31 13:59:23','2013-03-12 18:20:20',X'D9345AFBED634570ADF8FF2801BD64C0','lab.billing_building_room',0,NULL,'Price 321',4,NULL),
	(34,'2012-05-31 13:59:23','2013-03-12 18:20:20',X'67D0AB5902E945B8B8E20F5AD43CF881','lab.billing_address',0,NULL,'1301 Morris Park Ave.',4,NULL),
	(35,'2012-05-31 13:59:23','2013-03-12 18:20:20',X'3A0742AD0DD14F43BDD1BD0C21BB2ED9','lab.billing_city',0,NULL,'Bronx',4,NULL),
	(36,'2012-05-31 13:59:23','2013-03-12 18:20:20',X'9E461DFB365A465F871E521B56CDDEE6','lab.billing_state',0,NULL,'NY',4,NULL),
	(37,'2012-05-31 13:59:23','2013-03-12 18:20:20',X'0B2CEA5D66C44AC988C8CBB1B448EF50','lab.billing_country',0,NULL,'US',4,NULL),
	(38,'2012-05-31 13:59:23','2013-03-12 18:20:20',X'EBB205A2F8754FF1947E776246BF37C4','lab.billing_zip',0,NULL,'10461',4,NULL),
	(39,'2012-05-31 13:59:23','2013-03-12 18:20:20',X'6A05B161A2D342538ABEC44264853B36','lab.billing_phone',0,NULL,'718-678-1019',4,NULL),
	(40,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'93197D8F33A0476589AB7615365072BC','lab.internal_external_lab',0,NULL,'external',5,NULL),
	(41,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'5225040D48ED4088B03311BDB21EEE37','lab.phone',0,NULL,'212-321-1091',5,NULL),
	(42,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'CE73E8FB04334BEEB0F42FF56FE89441','lab.building_room',0,NULL,'Hammer 1101',5,NULL),
	(43,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'A1795F7F414448CFBB04AC2D173D122F','lab.billing_contact',0,NULL,'Leslie Trokie',5,NULL),
	(44,'2012-05-31 14:00:02','2013-03-12 18:20:20',X'3FE6D464ACD943F28788AA94B4643A1A','lab.billing_institution',0,NULL,'NYU Medical',5,NULL),
	(45,'2012-05-31 14:00:02','2013-03-12 18:20:20',X'0366FFD126AA4712B3C63D163408CD1F','lab.billing_departmentId',0,NULL,'3',5,NULL),
	(46,'2012-05-31 14:00:02','2013-03-12 18:20:20',X'FCA4BA3B40A746AFA84E3ED26331E184','lab.billing_building_room',0,NULL,'Hammer 1101',5,NULL),
	(47,'2012-05-31 14:00:02','2013-03-12 18:20:20',X'6E949A7357BE4D438DD009738033326E','lab.billing_address',0,NULL,'16-50 32nd Street',5,NULL),
	(48,'2012-05-31 14:00:02','2013-03-12 18:20:20',X'94EBA94D5F0A4F3DA3E29D2994FF73F3','lab.billing_city',0,NULL,'New York',5,NULL),
	(49,'2012-05-31 14:00:02','2013-03-12 18:20:20',X'FF2A76A7B6134931BC8F7933657A2213','lab.billing_state',0,NULL,'NY',5,NULL),
	(50,'2012-05-31 14:00:02','2013-03-12 18:20:20',X'D86CE42B3B9F45D8B878E5C5191424E7','lab.billing_country',0,NULL,'US',5,NULL),
	(51,'2012-05-31 14:00:02','2013-03-12 18:20:20',X'9904A5A9AC674AEB976A97D7FF56CC46','lab.billing_zip',0,NULL,'10002',5,NULL),
	(52,'2012-05-31 14:00:02','2013-03-12 18:20:20',X'0AAF3AAC74F14945956693B62A10230D','lab.billing_phone',0,NULL,'212-321-1091',5,NULL),
	(53,'2012-06-14 14:08:21','2013-03-12 18:20:20',X'1906738D78B84877A5F23229A6A6A54D','lab.internal_external_lab',0,NULL,'internal',1,NULL),
	(54,'2012-06-14 14:08:21','2013-03-12 18:20:20',X'73EA09CB2E9D4836BA1FF45B46C43E7B','lab.phone',0,NULL,'N/A',1,NULL),
	(55,'2012-06-14 14:08:21','2013-03-12 18:20:20',X'C79604793E0446148B4DCA0F70894F18','lab.building_room',0,NULL,'N/A',1,NULL),
	(56,'2012-06-14 14:08:21','2013-03-12 18:20:20',X'ABF6C4F45948459C9B228320FBE82986','lab.billing_contact',0,NULL,'N/A',1,NULL),
	(57,'2012-06-14 14:08:21','2013-03-12 18:20:20',X'3DE24864434540D4B1946B99DA8DD492','lab.billing_institution',0,NULL,'N/A',1,NULL),
	(58,'2012-06-14 14:08:21','2013-03-12 18:20:20',X'F09B44E49B434DD0A87D33FCB55998A5','lab.billing_departmentId',0,NULL,'1',1,NULL),
	(59,'2012-06-14 14:08:21','2013-03-12 18:20:20',X'A737E90224DD47E4BF0CEA110C9EE4FF','lab.billing_building_room',0,NULL,'N/A',1,NULL),
	(60,'2012-06-14 14:08:21','2013-03-12 18:20:20',X'938D953D39C1496BAC715865BCE795AC','lab.billing_address',0,NULL,'N/A',1,NULL),
	(61,'2012-06-14 14:08:21','2013-03-12 18:20:20',X'7FC6469F71DB46DDAC80D40BD47465D6','lab.billing_city',0,NULL,'N/A',1,NULL),
	(62,'2012-06-14 14:08:21','2013-03-12 18:20:20',X'FC2374CCE9914288B2D100D4A6D66266','lab.billing_state',0,NULL,'NY',1,NULL),
	(63,'2012-06-14 14:08:21','2013-03-12 18:20:20',X'70697A3125D4431C8C0AA73BA22C0477','lab.billing_country',0,NULL,'US',1,NULL),
	(64,'2012-06-14 14:08:21','2013-03-12 18:20:20',X'C0D22C73D08247F086D594D00D00BF99','lab.billing_zip',0,NULL,'N/A',1,NULL),
	(65,'2012-06-14 14:08:21','2013-03-12 18:20:20',X'2BCCB620F5D947A7ADE7FF29E08DCD71','lab.billing_phone',0,NULL,'N/A',1,NULL);

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
	(1,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'03F0048B61EB4B33854AD3820C37CF84',1,1,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'FFB8285BF0D2430F85A9E942FA5A8A35',2,2,NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'3EC6F146A21143BDB6CF3B65CCA710D0',3,3,NULL);

/*!40000 ALTER TABLE `resourcebarcode` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcecategory
# ------------------------------------------------------------

LOCK TABLES `resourcecategory` WRITE;
/*!40000 ALTER TABLE `resourcecategory` DISABLE KEYS */;

INSERT INTO `resourcecategory` (`id`, `created`, `updated`, `uuid`, `iname`, `isactive`, `name`, `resourcetypeid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:03:31','2013-08-06 10:03:15',X'ACAF0DF14AC8486D84E6DE9C99C35183','illuminaHiSeq2000',1,'Illumina HiSeq 2000',2,0),
	(2,'2012-12-20 11:03:32','2013-08-06 10:03:15',X'AB92B77D27454A44AD550FC4690DE1F6','illuminaMiSeqPersonalSequencer',1,'Illumina MiSeq',2,0);

/*!40000 ALTER TABLE `resourcecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcecategorymeta
# ------------------------------------------------------------

LOCK TABLES `resourcecategorymeta` WRITE;
/*!40000 ALTER TABLE `resourcecategorymeta` DISABLE KEYS */;

INSERT INTO `resourcecategorymeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `resourcecategoryid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:03:31','2013-08-05 19:04:27',X'3B4E6C3D4F07478DB2DCEC1A7791AC94','illuminaHiSeq2000.allowableUiField.readType',2,NULL,'single:single;paired:paired',1,0),
	(3,'2012-12-20 11:03:31','2013-03-12 12:39:41',X'7564F451030F4E8A8113F80D3479FD27','illuminaHiSeq2000.platformUnitSelector',3,NULL,'A:A;B:B',1,0),
	(4,'2012-12-20 11:03:32','2013-08-05 19:04:27',X'508123F338284DCE92E2AC83E40B8918','illuminaMiSeqPersonalSequencer.allowableUiField.readType',2,NULL,'single:single;paired:paired',2,0),
	(6,'2013-08-05 19:04:26','2013-08-05 19:04:27',X'852D9C3680A74F35B8736E0C5A9C03FC','illuminaHiSeq2000.allowableUiField.readLength',1,NULL,'50:50;75:75;100:100;150:150',1,0),
	(7,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'9C1D739BC08E4035B3480C481ED113E0','illuminaMiSeqPersonalSequencer.allowableUiField.readLength',1,NULL,'25:25;36:36;50:50;100:100;150:150',2,0);

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
	(1,'2012-12-20 11:07:21','2013-03-12 18:20:20',X'C5BB896E6A3744A28317AF908A05C2AE','resource.commission_date',0,NULL,'2012-10-03',1,NULL),
	(2,'2012-12-20 11:07:21','2013-03-12 18:20:20',X'B6153AFAD64A444C9BDEBD982295FDB6','resource.decommission_date',0,NULL,'',1,NULL),
	(3,'2012-12-20 11:07:41','2013-03-12 18:20:20',X'33A17C3BBB9C487ABC8F2CF318B73495','resource.commission_date',0,NULL,'2012-08-02',2,NULL),
	(4,'2012-12-20 11:07:41','2013-03-12 18:20:20',X'36621C0C70DC4EEA860AC5CF7CDD350C','resource.decommission_date',0,NULL,'',2,NULL),
	(5,'2012-12-20 11:08:04','2013-03-12 18:20:20',X'F7EC9B745E5B466DB18CC39C91FD157C','resource.commission_date',0,NULL,'2012-05-24',3,NULL),
	(6,'2012-12-20 11:08:04','2013-03-12 18:20:20',X'0952142168D94A09AED62890CFCC909B','resource.decommission_date',0,NULL,'',3,NULL);

/*!40000 ALTER TABLE `resourcemeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table resourcetype
# ------------------------------------------------------------

LOCK TABLES `resourcetype` WRITE;
/*!40000 ALTER TABLE `resourcetype` DISABLE KEYS */;

INSERT INTO `resourcetype` (`id`, `created`, `updated`, `uuid`, `iname`, `isactive`, `name`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'2E983A6CB6AA4CC684361306E60B278A','aligner',0,'Aligner',NULL),
	(2,'2013-03-12 12:39:40','2013-08-06 10:03:15',X'2341D262EEC245EA8E0730DB150D398C','mps',1,'Massively Parallel DNA Sequencer',NULL),
	(3,'2013-03-12 12:39:40','2013-08-06 10:03:15',X'81495112AA1541919E1D5425A956CEB9','amplicon',1,'DNA Amplicon',NULL),
	(4,'2013-03-12 12:39:40','2013-08-06 10:03:15',X'3DA110F3F99F43E9A30C8F62F4CC7B4B','peakcaller',1,'Peak Caller',NULL),
	(5,'2013-03-12 12:39:40','2013-08-06 10:03:14',X'77C2DE20FD924DA98613F8B190A2869F','sequenceRunProcessor',1,'Sequence Run Processor',NULL),
	(6,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'BB531174C958478892E5BB0AE85EAB26','bisulfiteSeqPipeline',0,'Bi-sulphite-seq Pipeline',NULL),
	(7,'2013-03-12 12:39:40','2013-08-06 10:03:15',X'B9AE73ED75264B3C95467AEBCADCBC0D','helptagPipeline',1,'HELP-tag Pipeline',NULL),
	(8,'2013-03-12 12:39:44','2013-08-06 10:03:14',X'F749BFB96A624F048276DE808C5E4E1D','referenceBasedAligner',1,'Reference-based Aligner',NULL),
	(9,'2013-08-05 19:04:24','2013-08-06 10:03:14',X'F0E9F43DFF4B458C8460378AF4D048CA','fastqProcessor',1,'FASTQ Processor',0),
	(10,'2013-08-05 19:04:25','2013-08-06 10:03:15',X'87C9D923E08F4E12AD07C30D210430AA','bamProcessor',1,'BAM Processor',0);

/*!40000 ALTER TABLE `resourcetype` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table roleset
# ------------------------------------------------------------

LOCK TABLES `roleset` WRITE;
/*!40000 ALTER TABLE `roleset` DISABLE KEYS */;

INSERT INTO `roleset` (`id`, `created`, `updated`, `uuid`, `childroleid`, `parentroleid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'625D4EFC70E14C98A560E50F8F91B259',4,4,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'3650068744664DF3A2CB54B914B01F6C',1,1,NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'CA88D3335BB94DE6BDC5F34A06617FA6',5,5,NULL),
	(4,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'B5D4653C948948F39D679904F6F71ECD',3,3,NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'8BC1E403045E4588A741E6210522A8F4',14,14,NULL),
	(6,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'D2AE85AB31FD4DE2AEDE6B26EF51A994',9,9,NULL),
	(7,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'EACCB52327FA4E73AB61C34A743556EF',10,10,NULL),
	(8,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'98C8DB18CBBF456AB96A6819722D6DB5',7,7,NULL),
	(9,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'5DCFDC78DFC44A0E96ECCFB2225FCF32',13,13,NULL),
	(10,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'5EB0A6FADFE148FDB32BB5A50DE004E0',8,8,NULL),
	(11,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'6CF698B68CBD43BD8949D0E26C143750',12,12,NULL),
	(12,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'D102A3C819F04F319EA21683A29B26ED',6,6,NULL),
	(13,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'3D0EB3C03063419D9DB532A35CB804B2',2,2,NULL),
	(14,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'38DBD848C0F74FBF8C3452B450B6B06D',11,11,NULL),
	(15,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'359FA0887C2E4645AF14528C7C367180',15,15,NULL),
	(16,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'7BE12CE4D26F4928AFC0C6E976992D69',5,1,NULL),
	(17,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'CC731AC0FD2E438F898D32731620193C',7,6,NULL),
	(18,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'5FE7348CAB944DE583E041158809D86E',8,6,NULL),
	(19,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'C836C4DCEF124828B5429CFA61B8115F',8,7,NULL),
	(20,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'D8850F3408684930AD64D00512979DDB',10,9,NULL),
	(21,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'F476FB317231445ABC4084FD2582080B',1,11,NULL),
	(22,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'B9A6915609BA4062991E9BEDED4C7B68',2,11,NULL),
	(23,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'CD0351630C9B4726A993DBB1B63CE559',3,11,NULL),
	(24,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'AFE6BBF22AEF453BA98DEFD35F6519E6',5,11,NULL),
	(25,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'09BE517B49CF431CA329E59D1D2706B2',16,16,NULL);

/*!40000 ALTER TABLE `roleset` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table run
# ------------------------------------------------------------



# Dump of table runcell
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

LOCK TABLES `samplebarcode` WRITE;
/*!40000 ALTER TABLE `samplebarcode` DISABLE KEYS */;

INSERT INTO `samplebarcode` (`id`, `created`, `updated`, `uuid`, `barcodeid`, `sampleid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:09:20','2013-03-13 11:19:59',X'C15F3E669FF34669A94E028C9F20BA01',4,1,1),
	(2,'2012-12-20 11:09:48','2013-03-13 11:19:59',X'1DE365FE85764ECB9A9A50E5C154D7BF',5,10,1),
	(3,'2012-12-20 11:10:27','2013-03-13 11:19:59',X'032FE194FEEF42668FB5B7D3366D9C66',6,19,1);

/*!40000 ALTER TABLE `samplebarcode` ENABLE KEYS */;
UNLOCK TABLES;


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
	(1,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'733108152DB646D3A3664D6F2A363883',1,1,1,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'17E6225632AD4354875296C793755029',1,2,2,NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'41BFCC5EECE04067A296BF78D8273BDF',1,3,3,NULL),
	(4,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'5FBCC36BA7064922A5866BCD8F5803DF',2,1,4,NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'1AD4E94DB7AD4C218A2F31CD1F3C6F96',3,1,5,NULL);

/*!40000 ALTER TABLE `sampledraftjobdraftcellselection` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sampledraftmeta
# ------------------------------------------------------------

LOCK TABLES `sampledraftmeta` WRITE;
/*!40000 ALTER TABLE `sampledraftmeta` DISABLE KEYS */;

INSERT INTO `sampledraftmeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `sampledraftid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:16:16','2013-03-12 18:20:20',X'B069F2E8B3D449A3903E41F82C54566F','genericBiomolecule.organism',0,NULL,'Human',1,NULL),
	(2,'2012-12-20 11:16:16','2013-03-12 18:20:20',X'E580903047C54568A1524D46360E6371','genericDna.concentration',0,NULL,'26',1,NULL),
	(3,'2012-12-20 11:16:16','2013-03-12 18:20:20',X'E36D00BBA0744516BE06F52792AC4399','genericDna.volume',0,NULL,'10',1,NULL),
	(4,'2012-12-20 11:16:16','2013-03-12 18:20:20',X'1F71EE5B11BA4B45AEA220871268B0A2','genericDna.buffer',0,NULL,'TE',1,NULL),
	(5,'2012-12-20 11:16:16','2013-03-12 18:20:20',X'84A400204CD3473398DC19455FEC1C8F','genericDna.A260_280',0,NULL,'1.76',1,NULL),
	(6,'2012-12-20 11:16:16','2013-03-12 18:20:20',X'405FCFA0C8C940DB933D70B577E1BEA0','genericDna.A260_230',0,NULL,'1.9',1,NULL),
	(7,'2012-12-20 11:16:16','2013-03-12 18:20:20',X'196C80B5CB084EF884C2658F4968D6BF','chipseqDna.fragmentSize',0,NULL,'250',1,NULL),
	(8,'2012-12-20 11:16:16','2013-03-12 18:20:20',X'7B2662CE30664BE4945F8D7981259031','chipseqDna.fragmentSizeSD',0,NULL,'25',1,NULL),
	(9,'2012-12-20 11:16:16','2013-03-12 18:20:20',X'28C3557CEC274A79BF73DE4464C975A2','chipseqDna.antibody',0,NULL,'goat',1,NULL),
	(10,'2012-12-20 11:16:41','2013-03-12 18:20:20',X'9E2BA20D2F8B42F0BA906AE392718951','genericBiomolecule.organism',0,NULL,'Human',2,NULL),
	(11,'2012-12-20 11:16:41','2013-03-12 18:20:20',X'048977847FCC43C7B9CC3F6A2EA7C0EF','genericDna.concentration',0,NULL,'23',2,NULL),
	(12,'2012-12-20 11:16:41','2013-03-12 18:20:20',X'67DA3F1E26034524B28509489779EA2F','genericDna.volume',0,NULL,'10',2,NULL),
	(13,'2012-12-20 11:16:41','2013-03-12 18:20:20',X'BF8E6E076B2D4201B0598D794C259705','genericDna.buffer',0,NULL,'TE',2,NULL),
	(14,'2012-12-20 11:16:41','2013-03-12 18:20:20',X'E0DCBBEBBBF74007B7398FE539E9E9EB','genericDna.A260_280',0,NULL,'1.74',2,NULL),
	(15,'2012-12-20 11:16:41','2013-03-12 18:20:20',X'16D75635779A4799900DC7B29E383742','genericDna.A260_230',0,NULL,'1.86',2,NULL),
	(16,'2012-12-20 11:16:41','2013-03-12 18:20:20',X'64C793BF0C0049A49D06902C7C2D26C7','chipseqDna.fragmentSize',0,NULL,'250',2,NULL),
	(17,'2012-12-20 11:16:41','2013-03-12 18:20:20',X'295671BD9E644040A71D4D45F1D15283','chipseqDna.fragmentSizeSD',0,NULL,'25',2,NULL),
	(18,'2012-12-20 11:16:41','2013-03-12 18:20:20',X'19EF906CF04F48E895A41874ECCD2C55','chipseqDna.antibody',0,NULL,'goat',2,NULL),
	(19,'2012-12-20 11:17:07','2013-03-12 18:20:20',X'B10AE89D59C14B56ADA06111ED930C2C','genericBiomolecule.organism',0,NULL,'Human',3,NULL),
	(20,'2012-12-20 11:17:07','2013-03-12 18:20:20',X'9F72C5CBB38441E38409EB0D85FD943A','genericDna.concentration',0,NULL,'21',3,NULL),
	(21,'2012-12-20 11:17:07','2013-03-12 18:20:20',X'50C6BEC7694A422D967015D10BBA8C68','genericDna.volume',0,NULL,'10',3,NULL),
	(22,'2012-12-20 11:17:07','2013-03-12 18:20:20',X'B139493D35414248BACF158A45DB9734','genericDna.buffer',0,NULL,'TE',3,NULL),
	(23,'2012-12-20 11:17:07','2013-03-12 18:20:20',X'F33FE6F24137473E907055A7A03F2960','genericDna.A260_280',0,NULL,'1.65',3,NULL),
	(24,'2012-12-20 11:17:07','2013-03-12 18:20:20',X'AF1005FD3FBC4ED885BB3D04BEE2558F','genericDna.A260_230',0,NULL,'1.83',3,NULL),
	(25,'2012-12-20 11:17:07','2013-03-12 18:20:20',X'CA7DE992E65F4807908555D1FAF36F63','chipseqDna.fragmentSize',0,NULL,'250',3,NULL),
	(26,'2012-12-20 11:17:07','2013-03-12 18:20:20',X'336C3F629C374CE987AD493846DBF693','chipseqDna.fragmentSizeSD',0,NULL,'25',3,NULL),
	(27,'2012-12-20 11:17:07','2013-03-12 18:20:20',X'A6269C3E474C44C48412C5FCDAF3A154','chipseqDna.antibody',0,NULL,'goat',3,NULL),
	(28,'2012-12-20 11:26:22','2013-03-12 18:20:20',X'5AB6053D2082422DA07457724D70A10A','genericBiomolecule.organism',0,NULL,'Human',4,NULL),
	(29,'2012-12-20 11:26:22','2013-03-12 18:20:20',X'7C04E269FA8C478EAF373F2B0BDFFFA0','chipseqDna.fragmentSize',0,NULL,'200',4,NULL),
	(30,'2012-12-20 11:26:22','2013-03-12 18:20:20',X'E8D6E4B6C3E5496BB576ECB0718E6DFC','chipseqDna.fragmentSizeSD',0,NULL,'10',4,NULL),
	(31,'2012-12-20 11:26:22','2013-03-12 18:20:20',X'70F7BDF0D7C44AE2ADE91B798E1391DC','chipseqDna.antibody',0,NULL,'sheep',4,NULL),
	(32,'2012-12-20 11:26:22','2013-03-12 18:20:20',X'A15EE34A38C045C69A83D0F9E4C10465','genericLibrary.concentration',0,NULL,'34',4,NULL),
	(33,'2012-12-20 11:26:22','2013-03-12 18:20:20',X'55676D281F054D2885720EDD93658BB3','genericLibrary.volume',0,NULL,'10',4,NULL),
	(34,'2012-12-20 11:26:22','2013-03-12 18:20:20',X'231065FE2C5F4563880F343C168D369B','genericLibrary.buffer',0,NULL,'Water',4,NULL),
	(35,'2012-12-20 11:26:22','2013-03-12 18:20:20',X'73E51D6971914640BD07D1C14E8A2322','genericLibrary.adaptorset',0,NULL,'2',4,NULL),
	(36,'2012-12-20 11:26:22','2013-03-12 18:20:20',X'59A5E758274C42DABD478DF3B89B1A17','genericLibrary.adaptor',0,NULL,'3',4,NULL),
	(37,'2012-12-20 11:26:22','2013-03-12 18:20:20',X'1E6F0E540D204D5E95B23F103745D01D','genericLibrary.size',0,NULL,'500',4,NULL),
	(38,'2012-12-20 11:26:22','2013-03-12 18:20:20',X'760F4055293B424A80C6FB74748D3B0F','genericLibrary.sizeSd',0,NULL,'55',4,NULL),
	(39,'2012-12-20 11:26:37','2013-03-12 18:20:20',X'4B7D6B6C8B5447048EA9134866C9EC4D','genericBiomolecule.organism',0,NULL,'Human',5,NULL),
	(40,'2012-12-20 11:26:37','2013-03-12 18:20:20',X'69DDAA47A0204197B327EE787348222D','chipseqDna.fragmentSize',0,NULL,'200',5,NULL),
	(41,'2012-12-20 11:26:37','2013-03-12 18:20:20',X'C33FC87CACC644B5AAD8799B05048594','chipseqDna.fragmentSizeSD',0,NULL,'10',5,NULL),
	(42,'2012-12-20 11:26:37','2013-03-12 18:20:20',X'B9446BA532BD48C397B3B62E5723A6D4','chipseqDna.antibody',0,NULL,'sheep',5,NULL),
	(43,'2012-12-20 11:26:37','2013-03-12 18:20:20',X'60B16134B10B4B7B8FBC91A9CF316B2F','genericLibrary.concentration',0,NULL,'34',5,NULL),
	(44,'2012-12-20 11:26:37','2013-03-12 18:20:20',X'DF9BFB08AE594D6AAFF181C7493D6446','genericLibrary.volume',0,NULL,'10',5,NULL),
	(45,'2012-12-20 11:26:37','2013-03-12 18:20:20',X'985581D9515843B5BD030002F80CF368','genericLibrary.buffer',0,NULL,'Water',5,NULL),
	(46,'2012-12-20 11:26:37','2013-03-12 18:20:20',X'B4184D552E6B46F0A24FCF9201FAAE6F','genericLibrary.adaptorset',0,NULL,'2',5,NULL),
	(47,'2012-12-20 11:26:37','2013-03-12 18:20:20',X'BB6EFDC90F864AA6A9C456E599D626C6','genericLibrary.adaptor',0,NULL,'3',5,NULL),
	(48,'2012-12-20 11:26:37','2013-03-12 18:20:20',X'9612A60291CD41AF871C8220A7C079F5','genericLibrary.size',0,NULL,'500',5,NULL),
	(49,'2012-12-20 11:26:37','2013-03-12 18:20:20',X'A901AC497F9848B99765E1A0A208594B','genericLibrary.sizeSd',0,NULL,'55',5,NULL);

/*!40000 ALTER TABLE `sampledraftmeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplefilegroup
# ------------------------------------------------------------



# Dump of table samplejobcellselection
# ------------------------------------------------------------

LOCK TABLES `samplejobcellselection` WRITE;
/*!40000 ALTER TABLE `samplejobcellselection` DISABLE KEYS */;

INSERT INTO `samplejobcellselection` (`id`, `created`, `updated`, `uuid`, `jobcellselectionid`, `libraryindex`, `sampleid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'499201CEC4E54D37B46132C021A31E06',1,1,21,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'301F44587CBB4CFB8339E2DB0FD2655B',1,2,22,NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'1E9BD2AE1DC845CF87489721D2F615A6',1,3,23,NULL),
	(4,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'156BA4ABBA654B58A16782826E1C50A7',2,1,25,NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'8879CBD64A374DBAB1928D8F2FE4E0A5',3,1,26,NULL);

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
	(1,'2012-12-20 11:09:20','2013-03-12 18:20:20',X'D48681B0F47E4650AFCDA411F9EFD4C2','platformunitInstance.readType',0,NULL,'paired',1,NULL),
	(2,'2012-12-20 11:09:20','2013-03-12 18:20:20',X'895CC8BB622B48FA96A40105A9D82489','platformunitInstance.readlength',0,NULL,'100',1,NULL),
	(3,'2012-12-20 11:09:20','2013-03-12 18:20:20',X'6385532C82854DBA91E707FF9BA1BD69','platformunitInstance.comment',0,NULL,'',1,NULL),
	(4,'2012-12-20 11:09:48','2013-03-12 18:20:20',X'589A65A37046468B9E0DDB5CD306ED93','platformunitInstance.readType',0,NULL,'single',10,NULL),
	(5,'2012-12-20 11:09:48','2013-03-12 18:20:20',X'A3A2C157F0D74B6CA99A824D1DF57F72','platformunitInstance.readlength',0,NULL,'75',10,NULL),
	(6,'2012-12-20 11:09:48','2013-03-12 18:20:20',X'3950DAEECF90452F8C7736FB8D71C300','platformunitInstance.comment',0,NULL,'',10,NULL),
	(7,'2012-12-20 11:10:27','2013-03-12 18:20:20',X'1EB9B597489F4380BEA30986B7E14841','platformunitInstance.readType',0,NULL,'paired',19,NULL),
	(8,'2012-12-20 11:10:27','2013-03-12 18:20:20',X'CAF94594ECEE48D3A57AF68E17F5F586','platformunitInstance.readlength',0,NULL,'150',19,NULL),
	(9,'2012-12-20 11:10:27','2013-03-12 18:20:20',X'11322D546C1D4C8DA90EB8056784E2A9','platformunitInstance.comment',0,NULL,'',19,NULL),
	(10,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'DCEABE600700447FBED17355E9A361CA','genericBiomolecule.organism',0,NULL,'Human',21,10),
	(11,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'2970A579B84A426CB6CD9CB24647B5C0','genericDna.concentration',0,NULL,'26',21,10),
	(12,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'F533AAE2A56D48E49CF3BF24205AA534','genericDna.volume',0,NULL,'10',21,10),
	(13,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'B217D62A1FFA4B97B96713310F4CC280','genericDna.buffer',0,NULL,'TE',21,10),
	(14,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'ADC1194CA2354D10B18C6F6356B1BC59','genericDna.A260_280',0,NULL,'1.76',21,10),
	(15,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'5086C61DBCBA48B49C38AFB297EBE11E','genericDna.A260_230',0,NULL,'1.9',21,10),
	(16,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'644F1BE19F354E1AA94CADD17D272605','chipseqDna.fragmentSize',0,NULL,'250',21,10),
	(17,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'CB556B119F0949FCB904E136349E6E1D','chipseqDna.fragmentSizeSD',0,NULL,'25',21,10),
	(18,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'24C25481E73446C481C8873D3D3B5EE3','chipseqDna.antibody',0,NULL,'goat',21,10),
	(19,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'957154B2F3AC4810952D9F11581DB813','genericBiomolecule.organism',0,NULL,'Human',22,10),
	(20,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'20EFDF4CCC264B78A479B157782A210E','genericDna.concentration',0,NULL,'23',22,10),
	(21,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'138C7519B1F044798B8E4CB083998758','genericDna.volume',0,NULL,'10',22,10),
	(22,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'35D077547FEF4615BB07A867C072AED0','genericDna.buffer',0,NULL,'TE',22,10),
	(23,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'39DFE209EB2948A38D3CDE0ECCF2AF00','genericDna.A260_280',0,NULL,'1.74',22,10),
	(24,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'D6E54290C60B49AEB6A12BA22181FFAC','genericDna.A260_230',0,NULL,'1.86',22,10),
	(25,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'A053EE489F42445589DE1C73D6A643BD','chipseqDna.fragmentSize',0,NULL,'250',22,10),
	(26,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'8FC320CB7E554CCF83D3D62BD70D6ED7','chipseqDna.fragmentSizeSD',0,NULL,'25',22,10),
	(27,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'EDFA96F00E3849DB88C56825812DA2B9','chipseqDna.antibody',0,NULL,'goat',22,10),
	(28,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'1EB0D68F45994DE099C4CAF5FEADE331','genericBiomolecule.organism',0,NULL,'Human',23,10),
	(29,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'130514D3EDD84E4A9ACFD881FE274139','genericDna.concentration',0,NULL,'21',23,10),
	(30,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'0D7488F6281643CD85524F8231737F5D','genericDna.volume',0,NULL,'10',23,10),
	(31,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'91B754294BE84581B169A87587ED53DB','genericDna.buffer',0,NULL,'TE',23,10),
	(32,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'B90D4BBF9AFF4052A3724CB69E8500FD','genericDna.A260_280',0,NULL,'1.65',23,10),
	(33,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'0921EC59FDAB4B8FB0DD84011D14F9D0','genericDna.A260_230',0,NULL,'1.83',23,10),
	(34,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'44DE2012A4EA43628A9D7AC411E4AC1F','chipseqDna.fragmentSize',0,NULL,'250',23,10),
	(35,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'CA0F49DC2B8B4E9AB26F7F38FD02DA3F','chipseqDna.fragmentSizeSD',0,NULL,'25',23,10),
	(36,'2012-12-20 11:20:04','2013-03-12 12:39:41',X'1E871A229C044DB3B043CE4746312FD4','chipseqDna.antibody',0,NULL,'goat',23,10),
	(37,'2012-12-20 11:23:35','2013-03-12 12:39:41',X'CA6F2F129B89434AA0F5E77646534B61','statusMessage.sampleQCComment::4e42daec-91c3-484f-8cf8-1dad722fc4f8',0,NULL,'Sample QC Comment::slightly concerned about size range but going to proceed anyway.',23,1),
	(38,'2012-12-20 11:24:31','2013-03-12 18:20:20',X'0815D70A8E404E65BAD8FD613AF47E5F','genericLibrary.concentration',0,NULL,'25',24,NULL),
	(39,'2012-12-20 11:24:31','2013-03-12 18:20:20',X'91418A618D654926A500D5F2CD60CB7A','genericLibrary.adaptor',0,NULL,'2',24,NULL),
	(40,'2012-12-20 11:24:31','2013-03-12 18:20:20',X'1BD036957E1240C6A864FA2E9010F604','genericLibrary.volume',0,NULL,'50',24,NULL),
	(41,'2012-12-20 11:24:31','2013-03-12 18:20:20',X'D9ECB8D3C9144DDD8261996DC3A360BF','genericLibrary.adaptorset',0,NULL,'2',24,NULL),
	(42,'2012-12-20 11:24:31','2013-03-12 18:20:20',X'8A5AFAE0363D40458CDD543B12D736CF','genericLibrary.sizeSd',0,NULL,'20',24,NULL),
	(43,'2012-12-20 11:24:31','2013-03-12 18:20:20',X'2FA9DCE3A4B14B5FACBC9F412E651BD0','genericLibrary.size',0,NULL,'400',24,NULL),
	(44,'2012-12-20 11:24:31','2013-03-12 18:20:20',X'37F36B58736941B5ABE39626D5AB6D60','genericLibrary.buffer',0,NULL,'TE',24,NULL),
	(45,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'7CF1EEC937104359A77D6254FB36351E','genericBiomolecule.organism',0,NULL,'Human',25,10),
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
	(56,'2012-12-20 11:27:07','2013-03-12 12:39:41',X'680447EF49224E3E8D6B9917441DEFA6','genericBiomolecule.organism',0,NULL,'Human',26,10),
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


# Dump of table samplesourcefilegroup
# ------------------------------------------------------------



# Dump of table samplesourcemeta
# ------------------------------------------------------------



# Dump of table samplesubtype
# ------------------------------------------------------------

LOCK TABLES `samplesubtype` WRITE;
/*!40000 ALTER TABLE `samplesubtype` DISABLE KEYS */;

INSERT INTO `samplesubtype` (`id`, `created`, `updated`, `uuid`, `arealist`, `iname`, `isactive`, `name`, `sampletypeid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-08-06 10:03:14',X'428F1072219342EC81C93789CB278210','genericBiomolecule,genericLibrary','controlLibrarySample',1,'Control Library',1,0),
	(2,'2013-03-12 12:39:40','2013-03-12 18:20:23',X'501480C681C542B4A3243E16AB34C7B6','genericBiomolecule,genericDna,bisulseqDna','bisulseqDnaSample',0,'BISUL-seq DNA',2,NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:20:23',X'8BD45094E59243F2962D94544985368C','genericBiomolecule,bisulseqDna,genericLibrary','bisulseqLibrarySample',0,'BISUL-seq Library',1,NULL),
	(4,'2013-03-12 12:39:40','2013-03-12 18:20:23',X'1414667765554DB8936CA012AFC4AECB','genericLibrary','bisulseqFacilityLibrarySample',0,'BISUL-seq Facility Library',1,NULL),
	(5,'2013-03-12 12:39:40','2013-08-06 10:03:15',X'389183F56BC6413DB26FD4AFA63E212E','genericBiomolecule,genericDna,chipseqDna','chipseqDnaSample',1,'ChIP-seq DNA',2,0),
	(6,'2013-03-12 12:39:40','2013-08-06 10:03:15',X'31F05F7849F44DA5B3FF9A70B9AB6E38','genericBiomolecule,chipseqDna,genericLibrary','chipseqLibrarySample',1,'ChIP-seq Library',1,0),
	(7,'2013-03-12 12:39:40','2013-08-06 10:03:15',X'ACE32C32B9AF42BFBE68468CE1642F30','genericLibrary','chipseqFacilityLibrarySample',1,'ChIP-seq Facility Library',1,0),
	(8,'2013-03-12 12:39:40','2013-08-06 10:03:15',X'D3CA3015796241F4BC9FE14E3D92F981','genericBiomolecule,genericLibrary,helptagLibrary','helptagLibrarySample',1,'HELP-tag Library',1,0),
	(9,'2013-03-12 12:39:40','2013-08-06 10:03:16',X'7C616F066B2F4A6E9C7BCA92043B20C2','illuminaFlowcellMiSeqV1','illuminaFlowcellMiSeqV1',1,'Illumina Flow Cell MiSeq V1',5,0),
	(10,'2013-03-12 12:39:40','2013-08-06 10:03:16',X'D3241DDE0D354428BDCCF80ED50D90E0','illuminaFlowcellV3','illuminaFlowcellV3',1,'Illumina Flow Cell Version 3',5,0),
	(11,'2013-03-12 12:39:45','2013-08-06 10:03:15',X'C05159DBAC2E4450B9CD2ABD6987F6FC','genericBiomolecule,genericDna','genericDnaDnaSample',1,'Generic DNA Seq DNA',2,0),
	(12,'2013-03-12 12:39:45','2013-08-06 10:03:15',X'BC8F78EEE13F477E8C9AA4A31D374C63','genericBiomolecule,genericLibrary','genericDnaLibrarySample',1,'Generic DNA Seq Library',1,0),
	(13,'2013-03-12 12:39:45','2013-08-06 10:03:15',X'D75BD89988A34E9C8C010387DBF4517E','genericLibrary','genericDnaFacilityLibrarySample',1,'Generic DNA Seq Facility Library',1,0);

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
	(13,'2013-03-12 12:39:45','2013-03-12 18:20:20',X'36CFE8FFB69B4691B45C7D6ED2C3A2E3','genericDnaDnaSample.includeRoles',1,NULL,'ft,lu',11,NULL),
	(14,'2013-03-12 12:39:45','2013-03-12 18:20:20',X'0EEB4B3DFC44476C9C292C21EC2E9778','genericDnaLibrarySample.includeRoles',1,NULL,'lu',12,NULL),
	(15,'2013-03-12 12:39:45','2013-03-12 18:20:20',X'D076ED4458314B79907068DF2DD6D611','genericDnaFacilityLibrarySample.includeRoles',1,NULL,'ft',13,NULL);

/*!40000 ALTER TABLE `samplesubtypemeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table samplesubtyperesourcecategory
# ------------------------------------------------------------

LOCK TABLES `samplesubtyperesourcecategory` WRITE;
/*!40000 ALTER TABLE `samplesubtyperesourcecategory` DISABLE KEYS */;

INSERT INTO `samplesubtyperesourcecategory` (`id`, `created`, `updated`, `uuid`, `resourcecategoryid`, `samplesubtypeid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'B4FF6EBB9E5D46D9827FDFE9D083D7FE',2,9,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'6E3FFF625A3744D5A3B8F5033809DE4A',1,10,NULL);

/*!40000 ALTER TABLE `samplesubtyperesourcecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sampletype
# ------------------------------------------------------------

LOCK TABLES `sampletype` WRITE;
/*!40000 ALTER TABLE `sampletype` DISABLE KEYS */;

INSERT INTO `sampletype` (`id`, `created`, `updated`, `uuid`, `iname`, `isactive`, `name`, `sampletypecategoryid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-08-06 10:03:14',X'5A76F12B925E4C739FC4FB88C72FF824','library',1,'Library',1,NULL),
	(2,'2013-03-12 12:39:40','2013-08-06 10:03:15',X'75D4337618814B228C4E3AE57784A275','dna',1,'DNA',1,NULL),
	(3,'2013-03-12 12:39:40','2013-08-06 10:03:15',X'E0470BB5B471478BB80165FB193B8BD5','rna',1,'RNA',1,NULL),
	(4,'2013-03-12 12:39:40','2013-08-06 10:03:15',X'019E9B91FD0044ACBF7667B318A83DBA','cell',1,'Cell',2,NULL),
	(5,'2013-03-12 12:39:40','2013-08-06 10:03:15',X'06ACDFC22CC64AD1BE5EA457E6FA5243','platformunit',1,'Platform Unit',2,NULL),
	(6,'2013-03-12 12:39:40','2013-08-06 10:03:15',X'384B075E967C44A6BCB92992EF2498F6','tissue',1,'Tissue',1,NULL),
	(7,'2013-03-12 12:39:40','2013-08-06 10:03:15',X'25F322AB0DF94853885C011884F69858','facilityLibrary',1,'Facilitylibrary',1,NULL);

/*!40000 ALTER TABLE `sampletype` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table sampletypecategory
# ------------------------------------------------------------

LOCK TABLES `sampletypecategory` WRITE;
/*!40000 ALTER TABLE `sampletypecategory` DISABLE KEYS */;

INSERT INTO `sampletypecategory` (`id`, `created`, `updated`, `uuid`, `iname`, `isactive`, `name`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-08-06 10:03:14',X'37D5DFB2E54042BE97C1896BA323DAE0','biomaterial',1,'Biomaterial',NULL),
	(2,'2013-03-12 12:39:40','2013-08-06 10:03:15',X'658F41524B874BBB9C63F72873D1EA12','hardware',1,'Hardware',NULL);

/*!40000 ALTER TABLE `sampletypecategory` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table software
# ------------------------------------------------------------

LOCK TABLES `software` WRITE;
/*!40000 ALTER TABLE `software` DISABLE KEYS */;

INSERT INTO `software` (`id`, `created`, `updated`, `uuid`, `iname`, `isactive`, `name`, `resourcetypeid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:03:29','2013-08-06 10:03:14',X'63A5B8381ACB4B3692E5A7C4EFA1D597','bowtieAligner',1,'Bowtie Aligner',8,0),
	(2,'2012-12-20 11:03:30','2013-08-06 10:03:15',X'20A9C2C6F306419F9AD1FC999C78EB95','macsPeakcaller',1,'MACS Peakcaller',4,0),
	(3,'2012-12-20 11:03:30','2013-03-12 18:19:04',X'2B2B1550FE3F406B86A33D4B0B61EB8C','bisulfiteSeqPipeline',0,'BISUL-seq Pipeline',6,0),
	(4,'2012-12-20 11:03:31','2013-08-06 10:03:15',X'5E846435B5654EE7A410353FA98B2930','helptagPipeline',1,'HELP-tag Pipeline',7,0),
	(5,'2012-12-20 11:03:31','2013-08-06 10:03:14',X'5C194015AFE94748B28D11E0101B2BE6','casava',1,'CASAVA',5,0),
	(6,'2013-08-05 19:04:24','2013-08-06 10:03:14',X'31C7C68ABA7D4DD09DE687E8DE697223','fastqc',1,'FastQC',9,0),
	(7,'2013-08-05 19:04:25','2013-08-06 10:03:14',X'4FAACF312E08488B9F570A7562AFD806','fastqscreen',1,'FastQ Screen',9,0),
	(8,'2013-08-05 19:04:26','2013-08-06 10:03:15',X'A142527782BC45DC801D7BF827B69BAD','bwa',1,'BWA',8,0),
	(9,'2013-08-05 19:04:27','2013-08-06 10:03:16',X'5B7DE75755324C4FBE80002065686D70','picard',1,'Picard',10,0),
	(10,'2013-08-05 19:04:27','2013-08-06 10:03:16',X'D5E266F4AB414E2286171CF29DFBC560','samtools',1,'SAMTools',10,0);

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
	(10,'2012-12-20 11:03:31','2013-03-12 12:39:41',X'252029E8BE7241799818094E49D4B442','casava.priorVersions',2,NULL,'',5,0),
	(11,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'0CDCF18440764A80B839DBFE040CE1B5','fastqc.currentVersion',1,NULL,'0.10.1',6,0),
	(12,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'8466870BED0F46319EC39CABE53CE791','fastqscreen.currentVersion',1,NULL,'0.4',7,0),
	(13,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'386EB9D6AC6844AF8DCA40A892304542','bwa.currentVersion',1,NULL,'0.6.2',8,0),
	(14,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'A97579B3DBFC42E9B70CDA064CE7F24F','bwa.priorVersions',2,NULL,'',8,0);

/*!40000 ALTER TABLE `softwaremeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table uifield
# ------------------------------------------------------------

LOCK TABLES `uifield` WRITE;
/*!40000 ALTER TABLE `uifield` DISABLE KEYS */;

INSERT INTO `uifield` (`id`, `created`, `updated`, `uuid`, `area`, `attrname`, `attrvalue`, `domain`, `locale`, `name`, `lastupdatebyuser`)
VALUES
	(1,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'10205790579D436CAB965EDF5364C92D','acctQuote','label','List of Job Quotes',NULL,'en_US','acctquote_list',0),
	(2,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'9C5672877F94422D808D9FA541F3C200','acctQuote','label','Quote Amount',NULL,'en_US','amount',0),
	(3,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'8F749BE793E846CAA6EEF2F7B47994E2','acctQuote','label','Quote created successfully',NULL,'en_US','created_success',0),
	(4,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'AE4CE5D4773F4D2187A8F23876EEDA39','acctQuote','label','PI',NULL,'en_US','lab',0),
	(5,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'151A703E125246B0974C046FFA31942F','acctQuote','label','Cell Cost',NULL,'en_US','cell_cost',0),
	(6,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'56CCE960AF9C42D09632D2B7B3CFBD8E','acctQuote','metaposition','40',NULL,'en_US','cell_cost',0),
	(7,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'039023CB436D4C13A27588FD580952BD','acctQuote','label','Job Status',NULL,'en_US','currentStatus',0),
	(8,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'9469D15E29CC43B9B0A41426F007B80D','acctQuote','label','Library Cost',NULL,'en_US','library_cost',0),
	(9,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'1A6359BE99144B1BBB7BC8E94CEDC54B','acctQuote','metaposition','20',NULL,'en_US','library_cost',0),
	(10,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'5FCC4039BCAC4EA3B55C20AEDEA8654E','acctQuote','label','Job ID',NULL,'en_US','jobId',0),
	(11,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'828408F160AB45A588E51B8FADE1B7D3','acctQuote','label','Job Name',NULL,'en_US','name',0),
	(12,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'F991C6E6294F42BEABCC6D5DE3BA6093','acctQuote','label','not yet set',NULL,'en_US','not_yet_set',0),
	(13,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'A3F713FCD67F41F5BE8A74E9CC5355C7','acctQuote','label','Sample Cost',NULL,'en_US','sample_cost',0),
	(14,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'320B7CCB5B8143519C6874E6B54CB96B','acctQuote','metaposition','30',NULL,'en_US','sample_cost',0),
	(15,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'C6DB5B98997744F794FB29C6C8FB7DB2','acctQuote','label','Submitted On',NULL,'en_US','submitted_on',0),
	(16,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'CD9F03DAA4F448628DA648CA08BAB03B','acctQuote','label','Submitted By',NULL,'en_US','submitter',0),
	(17,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'BD1B7E281CB341BDBBE150E099B47B1D','acctQuote','label','Update Failed. Try Again.',NULL,'en_US','update_failed',0),
	(18,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'02F6DF1AEFA948B9B519B2BD1E300A3C','activePlatformUnit','label','Active Platform Units Awaiting Libraries',NULL,'en_US','tableHeader',0),
	(19,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'462744E5DE8B460B8DFAC728A55A84D4','activePlatformUnit','label','Name',NULL,'en_US','name',0),
	(20,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'DD23604C928D46A4A4E3F7368418A236','activePlatformUnit','label','Barcode',NULL,'en_US','barcode',0),
	(21,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'F67B3D549CDB42B98ADB90D517280495','activePlatformUnit','label','Type',NULL,'en_US','type',0),
	(22,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'3FFD57FA7D1C4E329356F71ECC91F771','analysisParameters','label','Analysis Pairs',NULL,'en_US','analysis_pairs',0),
	(23,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'03893151CF7244049F8F6A59B0769E46','analysisParameters','label','Control Samples',NULL,'en_US','control_samples',0),
	(24,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'CA9A249649494B8F9A70DBEFAE1BE37B','analysisParameters','label','No Software Requested',NULL,'en_US','no_software_requested',0),
	(25,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'E21A8C853F404C16906539C7AD012C28','analysisParameters','label','Reference',NULL,'en_US','reference',0),
	(26,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'B18F3B32F66249D0B46318156ED72634','analysisParameters','label','Reference Paired With Samples (Reference acts as control)',NULL,'en_US','reference_sample_pairs',0),
	(27,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'A257F8510CA0481C8D621372C7717930','analysisParameters','label','Reference Paired With Samples (Sample acts as control)',NULL,'en_US','sample_pairs_reference',0),
	(28,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'3997C42FB94947AFB959805152D0F704','analysisParameters','label','Software Requested',NULL,'en_US','software_requested',0),
	(29,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'FCC6FCFE526046BAA0B0EE808FFD59DD','analysisParameters','label','Test Samples',NULL,'en_US','test_samples',0),
	(30,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'F55ED47C1D6142D68D0992284B092A96','auth','label','You are not Authorized to view this page',NULL,'en_US','accessDenied',0),
	(31,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'6940FE2EB021420C882CFEED80B67773','auth','data','Submit',NULL,'en_US','confirmemail',0),
	(32,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'B8C8022574C94AA4BBF2923499FE28A7','auth','label','Auth code',NULL,'en_US','confirmemail_authcode',0),
	(33,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'CB5B5B6BA7FC43A0A0AB02295EFFFA2B','auth','error','Invalid authorization code provided',NULL,'en_US','confirmemail_badauthcode',0),
	(34,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'B2318775B6C34D53B24AA5BC35494392','auth','error','email address is incorrect',NULL,'en_US','confirmemail_bademail',0),
	(35,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'FBEA84CEADF44AB88C66EFED97280A05','auth','error','Captcha text incorrect',NULL,'en_US','confirmemail_captcha',0),
	(36,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'CEAE19F020C544469DB5846D56788107','auth','label','Captcha text',NULL,'en_US','confirmemail_captcha',0),
	(37,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'B0DE85D7778A42D88F5458F88907B434','auth','error','email address in url cannot be decoded',NULL,'en_US','confirmemail_corruptemail',0),
	(38,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'E63FEC5D88954A9CAAF99BFDA2B4B237','auth','label','Email Address',NULL,'en_US','confirmemail_email',0),
	(39,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'81409A6155EC482094954076741E6065','auth','label','Submit',NULL,'en_US','confirmemail_submit',0),
	(40,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'6757590A3C9F42EEBA6D3227DE4622BA','auth','error','User email address and authorization code provided do not match',NULL,'en_US','confirmemail_wronguser',0),
	(41,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'E037A1A2824B450ABAA1C3713A835378','auth','label','Please click to <a href=\"../login.do\"/>Login</a>',NULL,'en_US','directusertologin',0),
	(42,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'81EF0413C8B54BBB88DEAF66165F705B','auth','data','Login',NULL,'en_US','login',0),
	(43,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'8DED67F6162A42278B07211109E34966','auth','label','About',NULL,'en_US','login_anchor_about',0),
	(44,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'67D5B100F2AE4774AC1514F6FCFC7F1C','auth','label','Forgot Password',NULL,'en_US','login_anchor_forgotpass',0),
	(45,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'E9B606B302B54B36B6AC7E6B4349FB86','auth','label','New PI',NULL,'en_US','login_anchor_newpi',0),
	(46,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'A982479C3368431FA1BB36982E674DBF','auth','label','New User',NULL,'en_US','login_anchor_newuser',0),
	(47,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'DA1D742470A448DFA2EF990BCAF3C72C','auth','error','Your login attempt was not successful. Try again.',NULL,'en_US','login_failed',0),
	(48,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'6CCBDD34B7BF46E2BC4D9216756FB2C7','auth','label','Please login to the WASP System using your username and password. If you have forgotten your password, or if you are currently unregistered and wish to create an account, please select from the links below.',NULL,'en_US','login_instructions',0),
	(49,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'924DCD64C0DE430982E7B76918A1B48B','auth','label','Password',NULL,'en_US','login_password',0),
	(50,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'89CDE5257EF24E89B210BDD1CADC678E','auth','label','Reason',NULL,'en_US','login_reason',0),
	(51,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'09C017B5CD6F4BCD8D9EF5786D0DC2EB','auth','label','Login',NULL,'en_US','login_submit',0),
	(52,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'451443F1E0A9444CBE6D2B843D44C3BD','auth','label','User',NULL,'en_US','login_user',0),
	(53,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'B7F62BFDC3BA4C4D991F2AD25FFCFDC4','auth','error','There is a problem confirming your email address as a new user. Please try again or contact a WASP administrator.',NULL,'en_US','newuser_confirmemail_wronguser',0),
	(54,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'EA9BDD4A66DA40329B254E6F4C7F7D44','auth','error','Failed to authenticate with supplied login credentials',NULL,'en_US','requestEmailChange_badcredentials',0),
	(55,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'99AB54F8D1604F7D90E20CC97CC4E9D8','auth','error','email address is of incorrect format',NULL,'en_US','requestEmailChange_bademail',0),
	(56,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'DF1D271F0CA84A4EB8FE3C7832B1B307','auth','error','Captcha text incorrect',NULL,'en_US','requestEmailChange_captcha',0),
	(57,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'37D59867913D4F1296C627ABDDD54F74','auth','label','New Email Address',NULL,'en_US','requestEmailChange_email',0),
	(58,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'2DDB6D5CB77A4E6597ADC3A9942B84F9','auth','label','Submit',NULL,'en_US','requestEmailChange_submit',0),
	(59,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'155527860A8140129A1CC6ADEBD7E8B6','auth','data','Submit',NULL,'en_US','resetpassword',0),
	(60,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'F71FF1ABFA5846268AB4724AA3D38B98','auth','label','An email has been sent to your registered email address containing an authorization code. Please click the link within this email or alternatively <a href=\"form.do\">click here</a> and enter the authorization code provided. ',NULL,'en_US','resetpasswordemailsent',0),
	(61,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'E974733FBAEF43BC84704734EF3F3212','auth','label','Your password has been reset. Please click to <a href=\"../login.do\"/>Login</a>',NULL,'en_US','resetpasswordok',0),
	(62,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'B5AB39AFCEB44DB4A179E8530FC98C10','auth','label','Reset Password: Complete',NULL,'en_US','resetpasswordok_title',0),
	(63,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'697A753E0AA4413BBE70009C5FEA0E16','auth','data','Submit',NULL,'en_US','resetpasswordRequest',0),
	(64,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'5E324F39063F47B8A051D314425AFB69','auth','error','Captcha text incorrect',NULL,'en_US','resetpasswordRequest_captcha',0),
	(65,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'EFA22C67E41F4DC1A72BD35247534AB8','auth','label','Captcha text',NULL,'en_US','resetpasswordRequest_captcha',0),
	(66,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'023375C80F3440958923FFD903E44EEE','auth','error','Please provide values for all fields',NULL,'en_US','resetpasswordRequest_missingparam',0),
	(67,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'97080C1F03754AB6AA7050CF8EA0796B','auth','label','Submit',NULL,'en_US','resetpasswordRequest_submit',0),
	(68,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'508256C12FAC4403B257F6D77C41B103','auth','label','Username',NULL,'en_US','resetpasswordRequest_user',0),
	(69,'2013-08-05 19:04:13','2013-08-05 19:04:13',X'84C810AD304F46A3A5735D9AF9BA46A2','auth','error','A user with the supplied username does not exist',NULL,'en_US','resetpasswordRequest_username',0),
	(70,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'8E89134220B84AA684EF295B79125C01','auth','label','Auth Code',NULL,'en_US','resetpassword_authcode',0),
	(71,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'DEB265576646464E8F369AB58ED0E22D','auth','error','Invalid authorization code provided',NULL,'en_US','resetpassword_badauthcode',0),
	(72,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'765FF84837784A73929601B29F5C0478','auth','label','Captcha text',NULL,'en_US','resetpassword_captcha',0),
	(73,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'A802D1E00432487E858F31BAC6FF3481','auth','error','Captcha text incorrect',NULL,'en_US','resetpassword_captcha',0),
	(74,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'7AAEAB1AF3764416BDCE638AA6A852A4','auth','label','New Password Requirements:<br />At least 8 characters in length<br />Only letters & numbers permitted (no spaces, etc)<br />At least one letter and one number<br />',NULL,'en_US','resetpassword_instructions',0),
	(75,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'33688C855E174C9AA4F9903AF6E35FD6','auth','error','Please provide values for all fields',NULL,'en_US','resetpassword_missingparam',0),
	(76,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'2890D8B1904F4800A2FE2F34D3726772','auth','error','New password must be at least 8 characters, containing only letters and numbers, with at least one letter and number',NULL,'en_US','resetpassword_new_invalid',0),
	(77,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'C620648B0B4044E5BF68A92A9D92A593','auth','error','The two entries for your NEW password are NOT identical',NULL,'en_US','resetpassword_new_mismatch',0),
	(78,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'2F698649FDA947598C644AD43A90301C','auth','error','No authorization code provided',NULL,'en_US','resetpassword_noauthcode',0),
	(79,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'D58AF5FB649C4068A430624646D2D0B4','auth','label','New Password',NULL,'en_US','resetpassword_password1',0),
	(80,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'DC82ADD854B1446CA5F4B03DC1DE9ED6','auth','label','Confirm New Password',NULL,'en_US','resetpassword_password2',0),
	(81,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'2EEBE2B3D831440E9AE54507B0C5CF25','auth','label','To reset your password you must first supply your WASP username below. You will then be sent an email to your confirmed email address with further instructions',NULL,'en_US','resetpassword_start_instructions',0),
	(82,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'7E1199FA3DDE40FCA62FF696F0049FD2','auth','label','Submit',NULL,'en_US','resetpassword_submit',0),
	(83,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'6201ECED9B1B449EBCF20AEC3E35D337','auth','label','Username',NULL,'en_US','resetpassword_user',0),
	(84,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'9FBEB8AF548E420F9411D1DCE196B5CD','auth','error','A user with the supplied username does not exist',NULL,'en_US','resetpassword_username',0),
	(85,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'C78ACCF534FF43429E46398F4D30EA05','auth','error','Username and authorization code provided do not match',NULL,'en_US','resetpassword_wronguser',0),
	(86,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'E48347587C8C4A7A8D9AFDECD677766B','createLibrary','label','Cancel',NULL,'en_US','cancel',0),
	(87,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'BD9455E374634FE4A24B7ABD7D839CEA','createLibrary','label','Library Details',NULL,'en_US','libraryDetails',0),
	(88,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'5DAE8DBAFCB64FB1A1821F22D919E96B','createLibrary','label','Library Name',NULL,'en_US','libraryName',0),
	(89,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'08D52B6EE29D48CCBD9BBFFE2B38ED5B','createLibrary','label','Library Subtype',NULL,'en_US','librarySubtype',0),
	(90,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'9802C86BEF164F71BDF6C61058A5E1F9','createLibrary','label','Library Type',NULL,'en_US','libraryType',0),
	(91,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'AE1C27ED14174D48A08739B3719E105F','createLibrary','label','Primary Sample Name',NULL,'en_US','primarySampleName',0),
	(92,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'B7AFD01958D744829FE751C1C9190CAA','createLibrary','label','Organism',NULL,'en_US','primarySampleSpecies',0),
	(93,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'01F1FB298FE84B7DA3E86DAE9E297C1B','createLibrary','label','Primary Sample Type',NULL,'en_US','primarySampleType',0),
	(94,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'211F1AD5C6B24191970B279E69067243','createLibrary','label','Save',NULL,'en_US','save',0),
	(95,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'E59A50F85C4E492D977FE39AFD345B80','createLibrary','label','--SELECT NEW ADAPTOR SET--',NULL,'en_US','selectNewAdaptorSet',0),
	(96,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'6BEF2531711D49C08EA3C05AAE840179','dapendingtask','label','APPROVE',NULL,'en_US','approve',0),
	(97,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'57C84EF4C7C445CD853B5C879D6E4121','dapendingtask','label','Please Select Approve Or Reject',NULL,'en_US','approveRejectAlert',0),
	(98,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'1C72061991F9480CBC714AE7DF12A4F3','dapendingtask','error','Update Failed: You Must Provide A Reason For Rejecting This Job',NULL,'en_US','commentEmpty',0),
	(99,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'92C5E4ACB42E468AB7EF86004D296416','dapendingtask','label','(which will be conveyed to the user!)',NULL,'en_US','conveyedToUser',0),
	(100,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'DAAB7405A82047C485F8F358A9C55F38','dapendingtask','label','Department',NULL,'en_US','department',0),
	(101,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'55432E94729E405AA080197A28B8C252','dapendingtask','label','Email',NULL,'en_US','email',0),
	(102,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'9EDD51857B8743B3919A2728754861B1','dapendingtask','error','Update Failed: Invalid Action',NULL,'en_US','invalidAction',0),
	(103,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'3159164BEE0B4BB392747D6A48600C06','dapendingtask','error','Update Failed: Job Not Found',NULL,'en_US','invalidJob',0),
	(104,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'826C7A3CBA654337B3B4563A12882F4F','dapendingtask','label','Job Has Been Approved',NULL,'en_US','jobApproved',0),
	(105,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'70A26068A6334785ACB10135BFC8C791','dapendingtask','label','Job Has Been Rejected',NULL,'en_US','jobRejected',0),
	(106,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'478E60D564934BCBBD6D597E26C9E140','dapendingtask','label','Job ID',NULL,'en_US','jobID',0),
	(107,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'6C291AE805DF4A4DBBEF3F71F1222BA4','dapendingtask','label','Job Name',NULL,'en_US','jobName',0),
	(108,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'32C99738186040CF8DD5D203DB5316FF','dapendingtask','label','New PI',NULL,'en_US','newPI',0),
	(109,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'24D6E5AE9B4048C989F7058C280B7958','dapendingtask','label','PI',NULL,'en_US','pi',0),
	(110,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'048FCEAB8BF5428C890D0AE81A50836C','dapendingtask','label','If you reject, you MUST provide a reason',NULL,'en_US','reasonForReject',0),
	(111,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'74A982CCFC214DEAA870A8F6A635B1DF','dapendingtask','label','REJECT',NULL,'en_US','reject',0),
	(112,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'585AF8227DF84D57B1BA9ED5B4DB736C','dapendingtask','label','RESET',NULL,'en_US','reset',0),
	(113,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'F8212D0E680A4DBF83D4A43789E28464','dapendingtask','label','Samples',NULL,'en_US','samples',0),
	(114,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'89A4C62806F4450AAEFD3FE4DE51F81B','dapendingtask','label','SUBMIT',NULL,'en_US','submit',0),
	(115,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'195B1E06D39143EF94BA55A26FECAF5C','dapendingtask','label','Submitter',NULL,'en_US','submitter',0),
	(116,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'84B9B97D59A24E0C8979CFBECA49F682','dapendingtask','label','Pending Principal Investigators',NULL,'en_US','subtitle1',0),
	(117,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'9DB8486FBC4240D29ECD03951D832C3D','dapendingtask','label','No Pending Principal Investigators',NULL,'en_US','subtitle1_none',0),
	(118,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'6A0B3B53F1994F20B0250D036A1C1738','dapendingtask','label','Pending Jobs',NULL,'en_US','subtitle2',0),
	(119,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'FDC83CA930A0406EA93868182F1DFD70','dapendingtask','label','No Pending Jobs',NULL,'en_US','subtitle2_none',0),
	(120,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'D30FA21FF2F54A0AB54553A4C96A864A','dapendingtask','label','Department Administrator Pending Tasks',NULL,'en_US','title',0),
	(121,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'285A4B1944BB418996FD6A7DA2D787F8','dapendingtask','label','Unknown',NULL,'en_US','unknown',0),
	(122,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'8B95083DE2D54D7EBDD98B2EDA4824A8','dapendingtask','error','Update Unexpectedly Failed',NULL,'en_US','updateFailed',0),
	(123,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'2D4BAB31D12342F38F4EE7DE5F6A55BB','dapendingtask','label','Please provide a reason for rejecting this PI\\n(note: the user will see this reason).',NULL,'en_US','validateCommentAlert',0),
	(124,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'95242135DBA34C80B39294F25C5C0BC1','dapendingtask','label','Workflow',NULL,'en_US','workflow',0),
	(125,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'0842A7E01CFB4126B4D92565647D96FF','dashboard','label','All Platform Units',NULL,'en_US','allPlatformUnits',0),
	(126,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'D0710DA9DF234BD09BBF7E36354EBF63','dashboard','label','Assign Libraries To Platform Unit',NULL,'en_US','assignLibrariesToPU',0),
	(127,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'3333F2DCF7704E08A174568E23A18B77','dashboard','label','Bisulfate-Seq',NULL,'en_US','bisulfateseq',0),
	(128,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'CB084F2FBED34E11825397C2B82A6E6E','dashboard','label','Control Libraries',NULL,'en_US','controlLibraries',0),
	(129,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'D52BD7597EDA4BFDB6BFA37BFD03BF58','dashboard','label','ChIP-Seq',NULL,'en_US','chipseq',0),
	(130,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'8642864D49824BC0A5D98F8315DDD733','dashboard','label','Dashboard',NULL,'en_US','dashboard',0),
	(131,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'2A1485AF4E984898A5EA478622361815','dashboard','label','Dept Admin',NULL,'en_US','deptAdmin',0),
	(132,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'84E7CE01A0D54B988970C01E4424BBD9','dashboard','label','Department Management',NULL,'en_US','deptManagement',0),
	(133,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'645A0EAEE51F45E8872911A52CF1B176','dashboard','label','Drafted Jobs',NULL,'en_US','draftedJobs',0),
	(134,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'FBBC11F36F954E239C2EBFFDD6D52148','dashboard','label','Facility Utils',NULL,'en_US','facilityUtils',0),
	(135,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'E3E092A240C845A586F3F3F679F5B233','dashboard','label','HELP Tag',NULL,'en_US','helptag',0),
	(136,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'1E04237DB8234E53B9709649C6483F7D','dashboard','label','Illumina',NULL,'en_US','illumina',0),
	(137,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'7C5F4652C6C9483DAA02B37242D3C044','dashboard','label','Job Quotes',NULL,'en_US','jobQuotes',0),
	(138,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'CF4BBA633C7447F99F9B4E1B1BB8E0B9','dashboard','label','Job Quoting',NULL,'en_US','jobQuoting',0),
	(139,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'CF5C7BD4EEF5475C92EBD2E9F40CB197','dashboard','label','Job Utils',NULL,'en_US','jobUtils',0),
	(140,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'1BDFF9788BC94604B483A4B9DCC814CC','dashboard','label','Join Another Lab',NULL,'en_US','joinAnotherLab',0),
	(141,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'2A001EB64B7D4116B7F880BB2A5AEB90','dashboard','label','Lab Details',NULL,'en_US','labDetails',0),
	(142,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'622BDF5A74D44881B0149AF31574363E','dashboard','label','Lab Members',NULL,'en_US','labMembers',0),
	(143,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'53F9DBC41EC0406FBFBBB3F1736B7C9D','dashboard','label','Lab Utils',NULL,'en_US','labUtils',0),
	(144,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'BD0D035F547F49149438F77EA363241C','dashboard','label','List All Job Quotes',NULL,'en_US','listAllJobQuotes',0),
	(145,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'8F44798EE28B473389ADE78908DC8049','dashboard','label','List Of All Machines',NULL,'en_US','listOfAllMachines',0),
	(146,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'CC0DA878F85546BBBEB58C1E07C42EA6','dashboard','label','List Of All Runs',NULL,'en_US','listOfAllRuns',0),
	(147,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'87BB0DCEAB9B4C2B847C1262891ABDB6','dashboard','label','List Of All Samples',NULL,'en_US','listOfAllSamples',0),
	(148,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'85381830F2F941B89E2E1873C386F831','dashboard','label','Misc Utils',NULL,'en_US','miscUtils',0),
	(149,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'D2CAEB9FB818485BBC0111E30F77502C','dashboard','label','My Account',NULL,'en_US','myAccount',0),
	(150,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'05A860B61E2844DB9419ADB3B696F4FE','dashboard','label','My Password',NULL,'en_US','myPassword',0),
	(151,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'757723DF833B45E5A70B424D28653AC6','dashboard','label','My Profile',NULL,'en_US','myProfile',0),
	(152,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'66B0DC2473D04380A8C503761EA22B84','dashboard','label','My Tasks',NULL,'en_US','myTasks',0),
	(153,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'6257EC600E60474586119C230C6BA33A','dashboard','label','My Viewable Jobs',NULL,'en_US','myViewableJobs',0),
	(154,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'0D20DBD1A4854C369FD5B61B8CE4B0CC','dashboard','label','New Platform Unit',NULL,'en_US','newPlatformUnit',0),
	(155,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'71D09E489125403AA9688FE3A634EFD0','dashboard','label','Platform Unit Utils',NULL,'en_US','platformUnitUtils',0),
	(156,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'06D335E0DD8A4AFCA976E683E8B31F91','dashboard','label','Plugins',NULL,'en_US','pluginUtils',0),
	(157,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'847FEAB2FFA14AEE8458B3AB939FC232','dashboard','label','Refresh Auth',NULL,'en_US','refreshAuth',0),
	(158,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'BD1B3EB49BCE49AB8E0F37C7D7315BF1','dashboard','label','Note: requests subject to verification',NULL,'en_US','requestAccessNote',0),
	(159,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'2288853703994A10898D600FFF3EC0D4','dashboard','label','Request Access To A Lab',NULL,'en_US','requestAccessToLab',0),
	(160,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'1A2D979E214144119659B9CFE5669F96','dashboard','label','Upgrade To PI',NULL,'en_US','upgradeStatusToPI',0),
	(161,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'5A70E7516D7942C09BAF5420702C9272','dashboard','label','Sample Receiver',NULL,'en_US','sampleReceiver',0),
	(162,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'DEA703C7ABE34C4686D40F65F8393E76','dashboard','label','Sample Utils',NULL,'en_US','sampleUtils',0),
	(163,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'5AF36157510D47A5B80A846064F8678E','dashboard','label','Submit A Job',NULL,'en_US','submitJob',0),
	(164,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'8A5C895BE86B427A89F82FF9C23229B5','dashboard','label','Superuser Utils',NULL,'en_US','superuserUtils',0),
	(165,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'203DB6E933A347E9955935E84A9CCE01','dashboard','label','System Users',NULL,'en_US','systemUsers',0),
	(166,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'41ED87282B2545DD8E77BC6B0711BD24','dashboard','label','Tasks',NULL,'en_US','tasks',0),
	(167,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'E50502436DEF406683AE5FEDC0C60E61','dashboard','label','User Manager',NULL,'en_US','userManager',0),
	(168,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'84C16F7D40714D00A84E4F5DF766E80F','dashboard','label','User Utils',NULL,'en_US','userUtils',0),
	(169,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'5E5ED1B26FDA43619877D9E82C0F9246','dashboard','label','View All Jobs',NULL,'en_US','viewAllJobs',0),
	(170,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'E498CBAF494B42128E6E17EEC7B627BF','dashboard','label','Workflow Utils',NULL,'en_US','workflowUtils',0),
	(171,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'5A25AF0728B74238B70442C9A09D4A1A','department','label','Active',NULL,'en_US','active',0),
	(172,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'1172B2DE5E244A5C8847BB44A9BAF14A','department','label','Inactive',NULL,'en_US','inactive',0),
	(173,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'7935E6E1B36945688383FE452CC07941','department','label','Create New Department',NULL,'en_US','create',0),
	(174,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'7255525277E6496880D64C592CBF4B73','department','label','A new department can be created by adding its name and an administrator in the form below. More administrators can be added later if desired.',NULL,'en_US','create_instructions',0),
	(175,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'02C05FB1815B4D499FB100625D5C85B7','department','label','Department',NULL,'en_US','detail',0),
	(176,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'EB4C535CEB2E43CC80AF790A29B5DBEB','department','label','Active',NULL,'en_US','detail_active',0),
	(177,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'DE801619A7424B2583B00FDCBD60F269','department','error','Selected person is already an administrator for this department',NULL,'en_US','detail_adminAlreadyExists',0),
	(178,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'E519E51D16EB4D18816D21AC8B974E26','department','label','Administrators',NULL,'en_US','detail_administrators',0),
	(179,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'A956E6D90C9D469CB6ED7E55EEAE8A32','department','label','Administrator Name',NULL,'en_US','detail_administrator_name',0),
	(180,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'B938E0393A0242F8BF91921BFE261C93','department','label','Create Administrator',NULL,'en_US','detail_createadmin',0),
	(181,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'DD61064A764E49EEA19254C8BBF7D6E4','department','label','Administrator Email',NULL,'en_US','detail_email',0),
	(182,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'F9E690C4791845EF814D12662E2B5747','department','error','Email not found',NULL,'en_US','detail_emailnotfound',0),
	(183,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'9D1CFF59B21C4B41AB8D147EB5EFFF20','department','label','Current Administrators',NULL,'en_US','detail_existingadmin',0),
	(184,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'C35073163E914AD58218C679DD762A0B','department','error','Formatting Error',NULL,'en_US','detail_formatting',0),
	(185,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'D697DA1E26D64A85AF9263F2E37952ED','department','label','Inactive',NULL,'en_US','detail_inactive',0),
	(186,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'E4D9C82588CA4815B19593E880D46256','department','error','Specified department does Not exist',NULL,'en_US','detail_invalidDept',0),
	(187,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'A2F57797171B410F9F36DD768CCFD1DC','department','label','Labs',NULL,'en_US','detail_labs',0),
	(188,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'5D6B619F68FF45D2A281B286FA57E692','department','error','No new administrator username provided',NULL,'en_US','detail_missinglogin',0),
	(189,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'F559AB2E670F440CA996602450D9800B','department','error','Administrator name is missing',NULL,'en_US','detail_missingparam',0),
	(190,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'F2A2C4A6A5F246409177E5BF253CAF5C','department','label','New Administrator Created',NULL,'en_US','detail_ok',0),
	(191,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'EC102EE44A29437EB82669C79A9C9D9D','department','label','Pending Jobs',NULL,'en_US','detail_pendingjobs',0),
	(192,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'ECE0F223E28343A19A37DD70CFF43276','department','label','Pending Labs',NULL,'en_US','detail_pendinglabs',0),
	(193,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'D8470175EE1D4725B5B2F76DA427FCC3','department','label','Remove',NULL,'en_US','detail_remove',0),
	(194,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'D0727D34C78D4BC38C55C17487F475D1','department','label','Submit',NULL,'en_US','detail_submit',0),
	(195,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'7219EE45EB454AE6B203500B94512B30','department','label','Update Department',NULL,'en_US','detail_update',0),
	(196,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'C46D65874CF348479F33F681F340EB7C','department','label','Type in the name of an existing WASP user to make them an administrator of this department',NULL,'en_US','detail_update_admin',0),
	(197,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'CCC3D8D597E14F5AB8E54E82B27418A7','department','label','Use the following form to change the status of a department between Active and Inactive',NULL,'en_US','detail_update_instructions',0),
	(198,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'A78D7D754B57491D8917142775FF90C5','department','error','Deaprtment name must be provided',NULL,'en_US','detail_update_missingparam',0),
	(199,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'E2EEF68D98DB424B89FC201DA61F4AA8','department','label','Department has been updated',NULL,'en_US','detail_update_ok',0),
	(200,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'C109D4E7064540FCB8AC3E8992F22672','department','error','User not found in database',NULL,'en_US','detail_usernotfound',0),
	(201,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'66FBE53DA4DC4179AAE52F22FC19B216','department','label','List of Labs',NULL,'en_US','lab_list',0),
	(202,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'97B78A3C2E0E464397D2E27F5C3FA3F0','department','label','List &amp; Manage Departments',NULL,'en_US','list',0),
	(203,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'360A927AC46A41C1B507AF7EEBEAFC31','department','data','Submit',NULL,'en_US','list',0),
	(204,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'5F28825740AF4E31B4D7280CF086EED1','department','label','Create Department',NULL,'en_US','list_create',0),
	(205,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'1D8B626BBCC441ED99F7946F73E33666','department','label','Department Name',NULL,'en_US','list_department',0),
	(206,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'E0678DF837FE4BAA9B8C650D164FA9F2','department','error','Department already exists',NULL,'en_US','list_department_exists',0),
	(207,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'E89301BE5CA6415C9A7D701351ADEC8A','department','label','The current departments registered in the system are listed below. Please click on a department name to view / edit its details and its department administrators.',NULL,'en_US','list_instructions',0),
	(208,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'70713565B34A48B5B16E543B4394940D','department','error','Invalid department name',NULL,'en_US','list_invalid',0),
	(209,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'B46CC8DD2117460CBC90DF2B0D55C73C','department','error','Please provide a department name',NULL,'en_US','list_missingparam',0),
	(210,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'02EF72D7BD2543B2947159233CC7EEA2','department','label','New department has been created',NULL,'en_US','list_ok',0),
	(211,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'63B04E1136C847B187B8EB1E04383706','extraJobDetails','label','Machine',NULL,'en_US','machine',0),
	(212,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'0A2384BF4B784BE0A3D43810ADA78044','extraJobDetails','label','Read Length',NULL,'en_US','readLength',0),
	(213,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'32B3C526C1D54C3E96C25764666DC7AF','extraJobDetails','label','Read Type',NULL,'en_US','readType',0),
	(214,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'2BD2B41C4C80487C9A5F38BEEF3D0FCA','extraJobDetails','label','Quote',NULL,'en_US','quote',0),
	(215,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'7340368B00D94405A400746615DE2249','file','label','Download Link',NULL,'en_US','download',0),
	(216,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'5B1626FBDE6F462F8FA1CF6F4DCA8206','file','label','File Name',NULL,'en_US','name',0),
	(217,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'5BB29FAB9B364BFC8491E5665C38C3E4','file','error','File Not Found',NULL,'en_US','not_found',0),
	(218,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'0C83519F5FED4147BCDF9D25D2E055EB','file','error','Unexpectedly unable to download file',NULL,'en_US','unable_to_download',0),
	(219,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'E233522F60384A4A9EF93F55DD7E610A','fmpayment','label','Amount',NULL,'en_US','amount',0),
	(220,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'4112BE25EC614896808C9A4102CA7C5A','fmpayment','constraint','NotEmpty',NULL,'en_US','amount',0),
	(221,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'4BF6462BB8A243099FAAE924529F64F0','fmpayment','metaposition','10',NULL,'en_US','amount',0),
	(222,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'DF163DDEA85547DD87D5831B2F05D584','fmpayment','error','AmounT cannot be Empty',NULL,'en_US','amount',0),
	(223,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'D871B25015184FC3A4C5BAC9A4399DEA','fmpayment','label','Comment',NULL,'en_US','comment',0),
	(224,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'E9CBBAB45E234316BF13115E3EE29071','fmpayment','constraint','NotEmpty',NULL,'en_US','comment',0),
	(225,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'E6F43903B3F94A1BBFF14BAB31363324','fmpayment','metaposition','20',NULL,'en_US','comment',0),
	(226,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'01146CF96AD44E1496EF1F37B64BE12A','fmpayment','error','Comment cannot be Empty',NULL,'en_US','comment',0),
	(227,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'81FA52C42EF24071A2216A5249FF5A8E','fmpayment','label','Receive Payment for Jobs',NULL,'en_US','name',0),
	(228,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'A9412AB2236347FD8C53C86FEC70FF6B','grid','label','Required date format: YYYY/MM/DD. It is best to use calendar to select date.',NULL,'en_US','dateFormat',0),
	(229,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'56C07F50EE4D43FC9594FC7FB2902864','grid','label','New',NULL,'en_US','icon_new',0),
	(230,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'A3AFB47B236A4643B3378B7C5430E5AF','grid','label','Reload Grid',NULL,'en_US','icon_reload',0),
	(231,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'7DCEF0887A5A4EA2BAA0891906C8F305','grid','label','Search',NULL,'en_US','icon_search',0),
	(232,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'1C8DE2397F77425B84D80BB19187C9F1','grid','label','Required jobId format: all digits',NULL,'en_US','jobIdFormat',0),
	(233,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'50C7DBE53F494005A9CDB43ECE612DC0','grid','label','Cells must be an integer',NULL,'en_US','cellsInteger',0),
	(234,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'6CB9B25178C746B78FB02E0610560409','grid','label','Required PI format: firstname lastname (login). It is best to select name from list.',NULL,'en_US','piFormat',0),
	(235,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'D26EDCAEEB8C4A99BC53E4DC8F8CF6EB','grid','label','ReadLength must be an integer',NULL,'en_US','readLengthInteger',0),
	(236,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'44CF904A75CF4644A2AC43BE2D23B788','grid','label','Required Submitter format: firstname lastname (login). It is best to select name from list.',NULL,'en_US','submitterFormat',0),
	(237,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'CB4CA353E0BD46D79CEB26A11C47F080','home','label','Current User',NULL,'en_US','currentUser',0),
	(238,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'76DE4F02C4164EAB970BD1C5B1935E3A','home','label','You have tasks awaiting action. To view those tasks, select the Task menu option above.',NULL,'en_US','tasksAwaiting',0),
	(239,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'4D56EA9E93A9401CBD2DC6E4DECBA729','home','label','WASP Home Page',NULL,'en_US','waspHomePage',0),
	(240,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'90E73A1B6C884FB1BC490FBAE1D008F1','home','label','Welcome Back',NULL,'en_US','welcomeBack',0),
	(241,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'13338FDA9F52498A8D10FBE64E4D34BC','job','label','Quote Amount',NULL,'en_US','amount',0),
	(242,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'B6E58A6945CC49B99250B1450FB1FA59','job','label','Job Status',NULL,'en_US','currentStatus',0),
	(243,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'3F141447CDE941BA845CB695D071FC61','job','approved','Job has been approved',NULL,'en_US','approval',0),
	(244,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'F29531171EBE493599093EB0DFAEE8BB','job','rejected','Job has been rejected',NULL,'en_US','approval',0),
	(245,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'4C872954C40A4D59839EB766FDCAE4B8','job','error','Error - Update Failed',NULL,'en_US','approval',0),
	(246,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'4D608A287686478DBF5D0D907E70ED5A','jobComment','label','Add New Job Comment',NULL,'en_US','addNewJobComment',0),
	(247,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'51C7F42F31974F218D82494E1A7DBD2F','jobComment','label','Please provide a comment',NULL,'en_US','alert',0),
	(248,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'1F743044DAE342DC8D497433BA9BEB89','jobComment','label','Facility-Generated Comments',NULL,'en_US','facilityComment',0),
	(249,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'31155B43599A4549AE8D48C29C5A9CC4','jobComment','error','Unable to find job in database',NULL,'en_US','job',0),
	(250,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'90698CE306654253B6B18FA4AFA0D5A5','jobComment','error','Comment may not be empty',NULL,'en_US','jobCommentEmpty',0),
	(251,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'01A04E5F18D241279E3B97ABA4ADA865','jobComment','label','Comment added to job comments list',NULL,'en_US','jobCommentAdded',0),
	(252,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'E6D1251969CF456B928A46A1E5761984','jobComment','error','Unable to properly authorize user',NULL,'en_US','jobCommentAuth',0),
	(253,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'0912E6DAE747440FAB4AFC61758ABCF0','jobComment','error','Unexpectedly unable to create comment',NULL,'en_US','jobCommentCreat',0),
	(254,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'006F7A06C72543E7B06A9E654BAFFA76','jobComment','label','Date',NULL,'en_US','jobCommentDate',0),
	(255,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'0662F170950B4838B82CFCCC1756B3EF','jobComment','label','Submit New Comment',NULL,'en_US','submitNewComment',0),
	(256,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'05C052CFFE704D968BED6E6CA077F1BA','jobComment','label','Job-Submitter Comment',NULL,'en_US','jobSubmitterComment',0),
	(257,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'5094F5A555F7480B91546FCFC29D45E3','jobListAssignLibrary','label','Cells',NULL,'en_US','numberCells',0),
	(258,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'375FC59EE9C647778FF1C49EC3C5FA85','jobMeta','label','Base',NULL,'en_US','base',0),
	(259,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'BF33192520E449B48D657AF725A356FD','jobMeta','label','Resource',NULL,'en_US','resource',0),
	(260,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'F1DBE6F5E8934EA2816C41880EB18D6E','jobMeta','label','Software',NULL,'en_US','software',0),
	(261,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'EA7C9922AA164AB68314EB20781448E6','job','label','Submitted On',NULL,'en_US','createts',0),
	(262,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'BE57CD7928AC4D508F0EA39C779B811E','job','label','Add Job Viewer',NULL,'en_US','detail_addJobViewer',0),
	(263,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'B6640777EFD44701961FCCF6E9C21B5D','job','label','Files',NULL,'en_US','detail_files',0),
	(264,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'6872B984AA41476499664EC94CF5B5EB','job','label','Job Viewer',NULL,'en_US','detail_jobViewer',0),
	(265,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'10C0B2AC3E3C41509E63662D9C356C30','job','label','Lab',NULL,'en_US','detail_lab',0),
	(266,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'466D2789898F4420850A9BD389401C01','job','label','Login Name',NULL,'en_US','detail_loginName',0),
	(267,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'798F719A6B58438394DF06A77426104D','job','label','Remove',NULL,'en_US','detail_remove',0),
	(268,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'12E418A12E0D40B4B98FAFBC080A665B','job','label','Samples',NULL,'en_US','detail_samples',0),
	(269,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'C90C77C0DC0D460CB2E072DD611AF44A','job','label','Submitting User',NULL,'en_US','detail_submittingUser',0),
	(270,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'2124273517184D0BA8DC3AE6AB79A51C','job','label','JobID',NULL,'en_US','jobId',0),
	(271,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'1839326F89F24EBBBFA256AC184E7D3C','job','error1','JobId or LabId Error',NULL,'en_US','jobViewerUserRoleAdd',0),
	(272,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'2996AD87E34C4596939166233A0F1804','job','error2','Login Not Found',NULL,'en_US','jobViewerUserRoleAdd',0),
	(273,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'115C736990F04DEF9793721EA7697275','job','error3','User is submitter, thus already has access to this job',NULL,'en_US','jobViewerUserRoleAdd',0),
	(274,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'7D0A4BA53A6E4DDDA0AEA6B62EF217EA','job','error4','User already has access to this job',NULL,'en_US','jobViewerUserRoleAdd',0),
	(275,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'8039CAE75FE845DB94E0C22991FB5943','job','label','List of Jobs',NULL,'en_US','job_list',0),
	(276,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'D647ED1DDE5D4E7394F32702B4F1BE28','job','label','Lab',NULL,'en_US','lab',0),
	(277,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'736EE9FBD8F24AFC98F41126D6F5B144','job','label','Job Name',NULL,'en_US','name',0),
	(278,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'DC64ED9B30FB4027B9E62243338AA58F','job','constraint','NotEmpty',NULL,'en_US','name',0),
	(279,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'E3C555127D3249718ECA730D1EE4B83C','job','error','Name cannot be empty',NULL,'en_US','name',0),
	(280,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'C80C2228EA4F46B3A170A38B2B2A2ABE','job','label','PI',NULL,'en_US','pi',0),
	(281,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'47A09B0CDB014AD1916E105FB7808449','job','label','Submitter',NULL,'en_US','submitter',0),
	(282,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'58CA54C5A8254C6E923BD4ADDB7C39B2','job','label','Submitter',NULL,'en_US','UserId',0),
	(283,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'5862260EA7F94404AAA967C6FE383EF0','job','label','Result',NULL,'en_US','viewfiles',0),
	(284,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'18E51CF900E447339542BD0A6085A2C6','job2quote','label','List of Quotes',NULL,'en_US','job2quote_list',0),
	(285,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'C22A73075978493B863FB4838E239C14','job2quote','label','Sort By Quote Amount To Easily Identify Jobs Requiring A Quote',NULL,'en_US','job2quote_note',0),
	(286,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'9182112B3A894D41B38159DF65C4E9F2','jobapprovetask','label','APPROVED',NULL,'en_US','approve',0),
	(287,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'CEFFE2C0B7F042FFB45CAAB4630514EB','jobapprovetask','label','Please Select Approved Or Rejected',NULL,'en_US','approveRejectAlert',0),
	(288,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'ACAAE434133542A088561693C8826224','jobapprovetask','error','Update Failed: You Must Provide A Reason For Rejecting This Job',NULL,'en_US','commentEmpty',0),
	(289,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'7E33313C831041798C0437E3255B5951','jobapprovetask','label','Click For Quotes Page',NULL,'en_US','gotoquotes',0),
	(290,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'669E73CFE1544A86870E1A60B36E4A03','jobapprovetask','error','Update Failed: Actions Must Be Accepted Or Rejected',NULL,'en_US','invalidAction',0),
	(291,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'0000981BDC104C00AA1FEBFC9E7A5BFA','jobapprovetask','error','Update Failed: Job Not Found',NULL,'en_US','invalidJob',0),
	(292,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'03CEEF54290944ECA0E403894A6408E4','jobapprovetask','error','Update Failed: Invalid Internal Job Approval Code',NULL,'en_US','invalidJobApproveCode',0),
	(293,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'00B12EE28E1D4C2694ED22F93F3211FE','jobapprovetask','label','Job Approved',NULL,'en_US','jobApproved',0),
	(294,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'54FE58DC476F40869AC820C9366514C6','jobapprovetask','label','Job ID',NULL,'en_US','jobId',0),
	(295,'2013-08-05 19:04:14','2013-08-05 19:04:14',X'A485D69089794E95ADBADF3D6BA5B954','jobapprovetask','label','Job Name',NULL,'en_US','jobName',0),
	(296,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'4A8BCD2019ED427DA952E5A968AEE445','jobapprovetask','label','You Must Generate A Quote Before Approving / Rejecting A Job ',NULL,'en_US','jobneedsquote',0),
	(297,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'1C437D4A4FEE4327A98AB39F05347779','jobapprovetask','label','Job Rejected',NULL,'en_US','jobRejected',0),
	(298,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'EF23C01FDA8045379756AFD9AD2D2198','jobapprovetask','label','PI',NULL,'en_US','pi',0),
	(299,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'90F1CB5F58414A689D4DAFD4D24A78EB','jobapprovetask','label','REJECTED',NULL,'en_US','reject',0),
	(300,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'B6AA90A63EEB4A508ABB1C2F61906D50','jobapprovetask','label','RESET',NULL,'en_US','reset',0),
	(301,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'C6DD7D0E03D04FEAA82D3F6ED349312C','jobapprovetask','label','Samples',NULL,'en_US','samples',0),
	(302,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'D227E2D938B24F9195DD2D8330EF8AB8','jobapprovetask','label','SUBMIT',NULL,'en_US','submit',0),
	(303,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'0D57D3495FE24650AC6FBD44CEDD2953','jobapprovetask','label','Submitter',NULL,'en_US','submitter',0),
	(304,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'9C4E5940323B4478927C83B89D1A683E','jobapprovetask','label','Jobs Pending Approval',NULL,'en_US','subtitle2',0),
	(305,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'E7C955B2DE4F409386780E2E76941161','jobapprovetask','label','No Jobs Pending Approval',NULL,'en_US','subtitle2_none',0),
	(306,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'64101BB2B9004787856EB0419E60DCB8','jobapprovetask','label','Unknown',NULL,'en_US','unknown',0),
	(307,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'85CEC50190EC4F3689412F29A6538855','jobapprovetask','error','Update Unexpectedly Failed',NULL,'en_US','updateFailed',0),
	(308,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'864085509AB045B48A767E028EEF3416','jobapprovetask','label','Please provide a reason for rejecting this job',NULL,'en_US','validateCommentAlert',0),
	(309,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'F239D6F361F242ED909C52C287CA32A9','jobapprovetask','label','Workflow',NULL,'en_US','workflow',0),
	(310,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'DE00662925724F26803A4B064A127079','jobdetail_for_import','label','Data Files',NULL,'en_US','dataFiles',0),
	(311,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'4007153185534D3BA5D47EEE24A7E03F','jobdetail_for_import','label','View',NULL,'en_US','dataFilesView',0),
	(312,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'4A68AAB3EB364846BD788E71E8F5A60C','jobdetail_for_import','label','Submitted Files',NULL,'en_US','files',0),
	(313,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'8B53A446E1BE4013AC266AEC02FD7D87','jobdetail_for_import','label','Analysis Parameters',NULL,'en_US','jobAnalysisParameters',0),
	(314,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'8AD0CCA5EE654F6399E3A1834B12B1B3','jobdetail_for_import','label','View',NULL,'en_US','jobAnalysisParametersView',0),
	(315,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'0132F84E417D4A7CB626B9AD477E0B9E','jobdetail_for_import','label','Job Comments',NULL,'en_US','jobComments',0),
	(316,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'026D35B61833430EAA57F6ADFEEC64F2','jobdetail_for_import','label','View',NULL,'en_US','jobCommentsView',0),
	(317,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'C7E181E529B9489FACB257210D5A20DF','jobdetail_for_import','label','-Add-Edit',NULL,'en_US','jobCommentsPlusAddEdit',0),
	(318,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'31614FEA25F44AD08D745A6F05054CCF','jobdetail_for_import','label','Job ID',NULL,'en_US','jobId',0),
	(319,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'A483F9DCC3D44FA8BAB8DB59F2FC40E2','jobdetail_for_import','label','Job Name',NULL,'en_US','jobName',0),
	(320,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'63DCF38BC58F4954AB7AD01D8D65FB7C','jobdetail_for_import','label','PI',NULL,'en_US','jobPI',0),
	(321,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'487711DA688741678620216000F0EAC8','jobdetail_for_import','label','Job Status',NULL,'en_US','jobStatus',0),
	(322,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'2EBAA70FFB4B4DE4AFE0CFE97D75CE96','jobdetail_for_import','label','Submission Date',NULL,'en_US','jobSubmissionDate',0),
	(323,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'2B13A0C7E6C645D8B2BF6E1A1F918328','jobdetail_for_import','label','Submitter',NULL,'en_US','jobSubmitter',0),
	(324,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'59660F3451234674B82217C4946DB352','jobdetail_for_import','label','Workflow',NULL,'en_US','jobWorkflow',0),
	(325,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'C192BBAC1F824BE1AB23B9ED823C6BE0','jobdetail_for_import','label','Requested Machine',NULL,'en_US','Machine',0),
	(326,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'C94A5AEB290D4ED79F0E987DCFDFD0B3','jobdetail_for_import','label','Requested Read Length',NULL,'en_US','Read_Length',0),
	(327,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'2CDFC1BCD0AC42DDB8D4DC08DB6BA86D','jobdetail_for_import','label','Requested Read Type',NULL,'en_US','Read_Type',0),
	(328,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'44BFF63924F449F3945BDCB2B6D41B9B','jobdetail_for_import','label','PI Approval',NULL,'en_US','PI_Approval',0),
	(329,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'CD84E05AF8E945A7ABB5E5D1723B7C63','jobdetail_for_import','label','Departmental Approval',NULL,'en_US','DA_Approval',0),
	(330,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'9EB44F301364404EAEAE3772454FB65E','jobdetail_for_import','label','Anticipated Cost',NULL,'en_US','Quote_Job_Price',0),
	(331,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'6B8DA4271A54461A8AB5270956A0F281','jobDraft','label','Cancel',NULL,'en_US','cancel',0),
	(332,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'95582620908941A4B8F09B5359D2880A','jobDraft','label','Cell/Cell',NULL,'en_US','cellCell',0),
	(333,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'0C09C77CE24D4402858C4CC5DF36F272','jobDraft','label','Cell',NULL,'en_US','cell',0),
	(334,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'DE67F76CBC444B5B935E9BF623932388','jobDraft','label','Each sample must be placed on at least one cell.',NULL,'en_US','cell_error',0),
	(335,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'24795763254F4DAFA45BCB897F7F87D9','jobDraft','label','Unexpected error with library barcode.',NULL,'en_US','cell_adaptor_error',0),
	(336,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'7B7DF2C4054B42F382CC1EE6FDDC6C4A','jobDraft','label','Libraries containing identical barcodes may NOT reside on the same cell.',NULL,'en_US','cell_barcode_error',0),
	(337,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'EF44B8964F0D4849AEF0B68C9DE0A11B','jobDraft','label','Libraries with the NONE barcode sequence (Index 0) CANNOT be multiplexed with other samples.',NULL,'en_US','cell_barcode_NONE_error',0),
	(338,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'9D53FA8B003A45C786617BD90C9E3C1F','jobDraft','label','Please choose the number of sequencing cells you wish to order and then indicate which samples you wish to combine (multiplex) in the same cell',NULL,'en_US','cell_instructions',0),
	(339,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'CB185E54927C43598386F9728CF1CF54','jobDraft','label','Unexpected error determining number of cells selected.',NULL,'en_US','cell_unexpected_error',0),
	(340,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'A5C9DB0A5FB546C5AED4B3C9BB6BDCD2','jobDraft','error','You must select a job resource',NULL,'en_US','changeResource',0),
	(341,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'13C1518833FA45A19F608861FFE71F44','jobDraft','error','You must select a software resource',NULL,'en_US','changeSoftwareResource',0),
	(342,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'DE3D7D1D35B540B6AFEABB5E69F6A91A','jobDraft','error','Unable to clone sample draft',NULL,'en_US','clone',0),
	(343,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'30415346BE20464DBCEBF9356D5FBD84','jobDraft','error','Unexpectedly unable to record your comment',NULL,'en_US','commentCreate',0),
	(344,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'697C995DDE1643E0BFEB9B526340D481','jobDraft','error','Unexpectedly unable to fetch your comment',NULL,'en_US','commentFetch',0),
	(345,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'599BDB4D09814CF4B2769B797417E606','jobDraft','label','If you wish, you may provide comments below that may be useful for this job submission. ',NULL,'en_US','comment_instructions',0),
	(346,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'AA6FD0EBE5004682948E8F66FCD17A30','jobDraft','label','Comments (optional) ',NULL,'en_US','comment_optional',0),
	(347,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'D47ED6E8F452409980EE724F6E9A30CB','jobDraft','label','Create A Job',NULL,'en_US','create',0),
	(348,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'ED1BA9BDC3AF445D8F892CF445919A5D','jobDraft','error','Unable to create a Job from current Job Draft',NULL,'en_US','createJobFromJobDraft',0),
	(349,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'E63B9B3F3C2247E58BB467E84DFFC98E','jobDraft','label','To create a job, please provide a name, select the lab from which you are submitting and choose the assay workflow most suited to your experiment.',NULL,'en_US','create_instructions',0),
	(350,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'3FC66B26E28349FEA476543C35816847','jobDraft','label','Edit',NULL,'en_US','edit',0),
	(351,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'DA73E017B32E4314BF58DC6EA506F66C','jobDraft','label','File',NULL,'en_US','file',0),
	(352,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'64320FA143884AF59DE5CBB90D070597','jobDraft','label','Action',NULL,'en_US','file_action',0),
	(353,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'E2CD292A463747EFB6448683EF189ADE','jobDraft','label','Description',NULL,'en_US','file_description',0),
	(354,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'B6B93E2AE0FA47D49E6857EE393162B0','jobDraft','label','Provide a short description for all files you wish to upload',NULL,'en_US','file_description_missing',0),
	(355,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'6EB058C9AAB14DC18E041EEB0B0B28DD','jobDraft','label','Download',NULL,'en_US','file_download',0),
	(356,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'5786F16C6F414FA3A498EB44F5988F6F','jobDraft','label','N/A',NULL,'en_US','file_not_applicable',0),
	(357,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'8024A3A5C837489093BABCEE0D478151','jobDraft','label','Remove',NULL,'en_US','file_remove',0),
	(358,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'05EE2D66883F498BBB8D0CE734DD9C44','jobDraft','label','Finish Later',NULL,'en_US','finishLater',0),
	(359,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'8D3C1240A30E41AEAF5953657D37D51E','jobDraft','error','Please address errors on this page',NULL,'en_US','form',0),
	(360,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'C448D89D8FA642AAAA825D72E4EDE1EC','jobDraft','label','Please choose a genome and build for each organism specified',NULL,'en_US','genome_instructions',0),
	(361,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'02057B1B5B6B412182ADD78E2E328964','jobDraft','error','No organism has been recorded for sample',NULL,'en_US','organimsNotSelected',0),
	(362,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'BA126236468C42B1BFBCB323FB328FD7','jobDraft','label','Job',NULL,'en_US','info_job',0),
	(363,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'0F42B492035F4477A5D718C67D4FF576','jobDraft','label','Lab',NULL,'en_US','info_lab',0),
	(364,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'67F2CCEAB4314542B99017B586577FBB','jobDraft','label','Workflow',NULL,'en_US','info_workflow',0),
	(365,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'9CC2C01F483D44C5942DE0E93B6FA4FC','jobDraft','label','List of Job Drafts (Click Job Name to Continue on Submission)',NULL,'en_US','jobDraft_list',0),
	(366,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'69F69EB6FD714C90A5471A3B0D752D2C','jobDraft','error','This jobdraft identifier does not return a valid job draft',NULL,'en_US','jobDraft_null',0),
	(367,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'C49D1B32F2EC4B9D8F63A85D640FA7F8','jobDraft','label','Lab',NULL,'en_US','lab',0),
	(368,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'EC16C4F512F84270B0979D5CEA9924D4','jobDraft','label','Lab',NULL,'en_US','labId',0),
	(369,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'88623615B1164F33AF26776117202DF5','jobDraft','error','Lab Must Not Be Empty',NULL,'en_US','labId',0),
	(370,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'D15FBDCA2B5648039153D9A8E083615B','jobDraft','label','Last Modified On',NULL,'en_US','last_modify_date',0),
	(371,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'09FD7A1F3B7F44F091E7777F0C0B1D78','jobDraft','label','Last Modified By',NULL,'en_US','last_modify_user',0),
	(372,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'88454DA1CC2B43C79343C26905478B60','jobDraft','label','Lib. Index',NULL,'en_US','libIndex',0),
	(373,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'F5DEE64448764B0B98C62398E28FC0A5','jobDraft','label','Please provide the metadata requested below.',NULL,'en_US','meta_instructions',0),
	(374,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'F1370DBBC8A14D00B41E54EE4F9BB47F','jobDraft','error','You must provide a name for this job',NULL,'en_US','name',0),
	(375,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'E81708F0FB1443F19A7994D1E87B5335','jobDraft','label','Job Name',NULL,'en_US','name',0),
	(376,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'9D3C16AF68614B4989DBE342C6CB7C2A','jobDraft','error','Job name chosen already exists',NULL,'en_US','name_exists',0),
	(377,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'7AE440664FC94974B95596B579D5164F','jobDraft','label','New Sample',NULL,'en_US','newSample',0),
	(378,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'A2915F4456E346EA80477C4A6F25855A','jobDraft','label','New Library',NULL,'en_US','newLibrary',0),
	(379,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'05B0B4BE4DEC4B8DAD153A3A4A7AEA1D','jobDraft','label','Next',NULL,'en_US','next',0),
	(380,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'170527CC2E8E4B3A97BCB8887482A06D','jobDraft','error','You must create entries for at least one sample',NULL,'en_US','noSamples',0),
	(381,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'F528CEA521934F2795DB96DD2959181A','jobDraft','error','This jobdraft has already been submitted',NULL,'en_US','not_pending',0),
	(382,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'D826841190A945AAA7E42CA7327F3A7E','jobDraft','label','No draft samples to display',NULL,'en_US','no_draft_samples',0),
	(383,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'CEA1733F0E584FD499D52B7080B7CC39','jobDraft','label','None Uploaded',NULL,'en_US','no_file',0),
	(384,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'1222DBB81EA14916A7AD0584D136EE63','jobDraft','error','You are not registered as a lab user but must be one to submit a job',NULL,'en_US','no_lab',0),
	(385,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'3B73D94F3C0948058CA398727070B898','jobDraft','error','No resources of the requested type have been assigned to the current workflow',NULL,'en_US','no_resources',0),
	(386,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'1E100F9DC69745EAAD75AA77E3E07159','jobDraft','error','There are currently no active workflows to create a job for.',NULL,'en_US','no_workflows',0),
	(387,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'7B5A5A0D1AE540AB97B092874EDC2D45','jobDraft','label','Select number of sequencing cells required',NULL,'en_US','numberofcells',0),
	(388,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'2DF15698CC4F46EFB121D0F19F145B8C','jobDraft','label','',NULL,'en_US','page_footer',0),
	(389,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'F158F38B3C25428F9522C202089B8CCE','jobDraft','label','Are you sure you wish to remove this sample?',NULL,'en_US','remove_confirm',0),
	(390,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'D62C85714BC94DCBA1F6EE1DD5D8B1B0','jobDraft','error','Resource options for this workflow have not been properly configured',NULL,'en_US','resourceCategories_not_configured',0),
	(391,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'EBA1DC0683E444699AECC1D4D2FDB414','jobDraft','label','Please select from the platforms available for this assay workflow. If there are options available for the platform and assay workflow, you will be prompted to choose those applicable to your experimental design.',NULL,'en_US','resource_instructions',0),
	(392,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'844CF36402304D4FB2AE3D6620AFF4CF','jobDraft','label','Sample',NULL,'en_US','sample',0),
	(393,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'9C90867AB6BE47E59C61EF6D863F6908','jobDraft','label','No sample draft matches supplied id',NULL,'en_US','sampleDraft_null',0),
	(394,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'7B1AE64946574624A3D195B456FA3145','jobDraft','label','No sample subtype matches supplied id',NULL,'en_US','sampleSubtype_null',0),
	(395,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'09CB7B093E7A424198751A8D06567807','jobDraft','label','Action',NULL,'en_US','sample_action',0),
	(396,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'31B6D963C6464EFEB8424FB2A84DE55F','jobDraft','label','Existing',NULL,'en_US','sample_add_existing',0),
	(397,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'5058027F84EA4C0B9E9080D6BC94DC01','jobDraft','label','Add a new Sample / Library',NULL,'en_US','sample_add_heading',0),
	(398,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'B3EB24BECF794DAFB55086488098FEC1','jobDraft','label','Build',NULL,'en_US','sample_build',0),
	(399,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'D149D16598E34EB0ADF8B96849FA59FF','jobDraft','label','Class',NULL,'en_US','sample_class',0),
	(400,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'985F21371EBB452CBE414A699DB3EDCE','jobDraft','label','Clone',NULL,'en_US','sample_clone',0),
	(401,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'743DBD96A8084E1C9717FF85AFEE8A59','jobDraft','label','Clone Sample / Library',NULL,'en_US','sample_clone_heading',0),
	(402,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'75ABD612791F4A2381B29BCFA181C662','jobDraft','label','Edit',NULL,'en_US','sample_edit',0),
	(403,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'04C59E4A17E644AD86FCB58F6DE1CB6F','jobDraft','label','Edit Sample / Library',NULL,'en_US','sample_edit_heading',0),
	(404,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'050661CFB91248038904124C27D20DDA','jobDraft','label','File',NULL,'en_US','sample_file',0),
	(405,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'7F9A46B1C75648DF93786BDDF9AAD576','jobDraft','label','Genome',NULL,'en_US','sample_genome',0),
	(406,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'7FC26B7486C540A69F86CDA28FF11021','jobDraft','label','Select Genome / Build',NULL,'en_US','sample_genome_select',0),
	(407,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'21917E7A47BA4653A32E2855E5DEF6FF','jobDraft','error','You must select a genome and build',NULL,'en_US','sample_genome_select',0),
	(408,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'F151826AEFD846099F256BDE59B2B3EF','jobDraft','error','Unable to retrieve a genome build',NULL,'en_US','sample_genome_retrieval',0),
	(409,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'D32BDF4A393C4E6CA1EA8E02F3F010F2','jobDraft','error','Problem saving genome build with sample Draft',NULL,'en_US','sample_genome_save',0),
	(410,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'9DAB67D65667450EBDC8F89A76116CE8','jobDraft','label','Please add details for each of your samples. Choose from the sample sub-types available by clicking the appropriate link on the bottom bar of the table below. You can enter information about new samples or select from previously submitted samples.',NULL,'en_US','sample_instructions',0),
	(411,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'87ABDB49E65E4F859A28B737632A0743','jobDraft','label','Sample Name',NULL,'en_US','sample_name',0),
	(412,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'6D7CE821FD72432782208C920EB98849','jobDraft','label','Sample #',NULL,'en_US','sample_number',0),
	(413,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'82D139885E93432FBAAD9EB96359DB43','jobDraft','label','Organism',NULL,'en_US','sample_organism',0),
	(414,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'CC752FAAF31A4D15B9EA55B4EA433159','jobDraft','label','Remove',NULL,'en_US','sample_remove',0),
	(415,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'AF1739B3194D4C0DB0AADEF9792D5053','jobDraft','label','Sample Draft View',NULL,'en_US','sample_ro_heading',0),
	(416,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'E47282DD2F3E40D182D21FA2D33EFD37','jobDraft','label','Sample Subtype',NULL,'en_US','sample_subtype',0),
	(417,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'EF4DF6B7E4E94CA893CE46826F13BA65','jobDraft','error','This sample subtype identifier does not return a valid sample subtype',NULL,'en_US','sample_subtype_null',0),
	(418,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'D9190C038FEE4776A3CBF0AEC28F3336','jobDraft','label','Sample Type',NULL,'en_US','sample_type',0),
	(419,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'32A7B93CF8DD4FBCB3F2715B18E843C5','jobDraft','label','View',NULL,'en_US','sample_view',0),
	(420,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'A108A451052C4F0A97A05FFB8A1CA7AA','jobDraft','label','Save',NULL,'en_US','save',0),
	(421,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'F61FD91AEBFD4CE19697C2EE1F1CEEE2','jobDraft','label','Continue',NULL,'en_US','continue',0),
	(422,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'B8A50213781F494EA50E18340D8EC435','jobDraft','label','Please select from the software options available for this assay workflow. If there are settable parameters, you will be prompted to choose those applicable to your experimental design or you may leave the pre-populated default values.',NULL,'en_US','software_instructions',0),
	(423,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'0AE57B62AB764D27820287A302B922EB','jobDraft','error','Software options for this workflow have not been properly configured',NULL,'en_US','software_not_configured',0),
	(424,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'65B862517EAA46FFA9758112CD1197FE','jobDraft','label','JobName-Lab-Assay',NULL,'en_US','startPageBreadcrumbMessage',0),
	(425,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'14205A5631EA4AF0A9AE70DD465BABA9','jobDraft','label','Thank you for your submission. You may track the progress of submitted jobs and review data after job completion by navigating to via the Jobs menu item on the home page',NULL,'en_US','submission_complete',0),
	(426,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'E6C7A1818A084B10B9189D62F8691853','jobDraft','label','Submission of this job unexpectedly failed. Please try again or contact a WASP administrator.',NULL,'en_US','submission_failed',0),
	(427,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'50C6DC3B54F94ACAA476933883E87EFF','jobDraft','label','Save Changes',NULL,'en_US','submit',0),
	(428,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'0BF0418B27F84127A011262DA12EA40A','jobDraft','label','Submit Job',NULL,'en_US','submit_button',0),
	(429,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'4D30D420FB4F43F7A66958157F098BD9','jobDraft','label','Submit Later',NULL,'en_US','submit_later_button',0),
	(430,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'9EA8D140D4AF4094BE2B357CA025818E','jobDraft','label','Retry',NULL,'en_US','submit_retry_button',0),
	(431,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'8AAC5815E9B649F2ADDDCE1D8175CA85','jobDraft','label','Submitted By',NULL,'en_US','submitter',0),
	(432,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'6E423D6713A14107AE03ED41DBD5EEF0','jobDraft','label','Add samples of subtype',NULL,'en_US','subtype_select',0),
	(433,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'FE439D2563C74CDAAFC1FB28E2182D2A','jobDraft','error','File Upload failed',NULL,'en_US','upload_file',0),
	(434,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'25B81A8EB03A4FDDA70387FD4359CD3A','jobDraft','label','If you wish you may upload one or more files, e.g. quality control data, to associate with this sample submission.',NULL,'en_US','upload_file_description',0),
	(435,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'9CD9FC1D0FEB4DFFA271CA91DB3297AF','jobDraft','label','Upload Files',NULL,'en_US','upload_file_heading',0),
	(436,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'D6C09E2820634B809342BD4484486E03','jobDraft','label','File successfully removed',NULL,'en_US','upload_file_removed',0),
	(437,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'4271340AFC81473E8AD34F65A2DAE4DB','jobDraft','label','File removal unexpectedly failed. Try again.',NULL,'en_US','upload_file_removal_failed',0),
	(438,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'2F220242DFAE4A3F907026F310360B9D','jobDraft','error','You are not authorized to edit this jobdraft',NULL,'en_US','user_incorrect',0),
	(439,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'B47A1C4B7B3843919075EA60AA316BB2','jobDraft','label','Please review your submission by selecting the links immediately above. Once satisfied with your entries, you may submit this request by clicking the Submit Job button.',NULL,'en_US','verify_instructions',0),
	(440,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'C7A5838D05E44FC6A48C7E6A3F75058A','jobDraft','label','Assay Workflow',NULL,'en_US','workflowId',0),
	(441,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'87DA7ABB72A54B21AB7DE26040769DD7','jobDraft','error','You must select an Assay Workflow',NULL,'en_US','workflowId',0),
	(442,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'E9643E6C445047918E271F13B8892CFB','jobDraft','error','To alter Assay Workflow, first remove all samples for this job ',NULL,'en_US','workflowId_change',0),
	(443,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'89B9AC35D96A49F1AF4973AC301A4BE5','jobListAssignLibrary','label','Active Jobs With Libraries To Be Run',NULL,'en_US','tableHeader',0),
	(444,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'0894CC05CCA24E37A3CB5B385FB3DAA9','jobListAssignLibrary','label','Job',NULL,'en_US','jobId',0),
	(445,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'CB545AC959C7483592F8DA3B76E8538F','jobListAssignLibrary','label','Job Name',NULL,'en_US','jobName',0),
	(446,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'2D41471E8CB745C3B855CE5F01317919','jobListAssignLibrary','label','None',NULL,'en_US','none',0),
	(447,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'E060DDDE4D7643748FD4D761859D68C9','jobListAssignLibrary','label','Submitter',NULL,'en_US','submitter',0),
	(448,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'09E1C1BC4FD4420D8F18EB0B1DD8924D','jobListAssignLibrary','label','view',NULL,'en_US','view',0),
	(449,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'7EDB39529BA949DBA2BAA44A0DA38100','jobListAssignLibrary','label','PI',NULL,'en_US','pi',0),
	(450,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'EBDCEE2EFDCC484BBF07C2F5D856964F','jobListCreateLibrary','label','No jobs with libraries to be created',NULL,'en_US','noneNeeded',0),
	(451,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'7D0BA24C08B2467DA121214FA706FA73','jobListCreateLibrary','label','Job',NULL,'en_US','jobId',0),
	(452,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'A37D510D15C24118BD1959B5D735EA33','jobListCreateLibrary','label','Job Name',NULL,'en_US','jobName',0),
	(453,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'E5231EDDC6FD45EFB1EC3AEE261F082A','jobListCreateLibrary','label','Submitter',NULL,'en_US','submitter',0),
	(454,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'0D19495BBA7247A7B3C380D0033A0BDE','jobListCreateLibrary','label','PI',NULL,'en_US','pi',0),
	(455,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'8B0039FBBFE24A6EA682C097503F034C','lab','label','Add New LabUser',NULL,'en_US','adduser',0),
	(456,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'7BEF87519A9240A1AC9942A442DB12A4','lab','label','Billing Address',NULL,'en_US','billing_address',0),
	(457,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'5DF9B54AE10B40AA802C9FBF7FB2DDEB','lab','error','Billing Address cannot be empty',NULL,'en_US','billing_address',0),
	(458,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'694498B571AE43E5B95B21A2250DE234','lab','constraint','NotEmpty',NULL,'en_US','billing_address',0),
	(459,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'0A507A7F2B6045E197C71575B8658258','lab','metaposition','80',NULL,'en_US','billing_address',0),
	(460,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'578E32E6D80743ADB0DE96012A447274','lab','label','Room',NULL,'en_US','billing_building_room',0),
	(461,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'D5A6FE5501DC41C7A9501FB473CFD3F1','lab','metaposition','70',NULL,'en_US','billing_building_room',0),
	(462,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'9E9CF698957D4DE398DCD3DFA250CEBD','lab','label','Billing City',NULL,'en_US','billing_city',0),
	(463,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'47D1D7E546624B939F0BA1BF2A7C1E95','lab','error','Billing City cannot be empty',NULL,'en_US','billing_city',0),
	(464,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'B770F427BD084954AF34B2CE2090AACF','lab','constraint','NotEmpty',NULL,'en_US','billing_city',0),
	(465,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'AB33682606224A5682784337A46DAE50','lab','metaposition','90',NULL,'en_US','billing_city',0),
	(466,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'A59563965D0F4ED0B178BC82C6C854DA','lab','label','Billing Contact',NULL,'en_US','billing_contact',0),
	(467,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'A5CFFDD425F74BAEBC04494FD856E5D0','lab','error','Billing Contact cannot be empty',NULL,'en_US','billing_contact',0),
	(468,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'C43626841A9541DEB656F47AD727E317','lab','constraint','NotEmpty',NULL,'en_US','billing_contact',0),
	(469,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'CC0B913336444B24B226DB774E839517','lab','metaposition','40',NULL,'en_US','billing_contact',0),
	(470,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'B34BCA32B4F2466794BF9A8DB41E5A6D','lab','label','Billing Country',NULL,'en_US','billing_country',0),
	(471,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'936B7221416642DE891AD09CFDEB51AE','lab','error','Billing Country cannot be empty',NULL,'en_US','billing_country',0),
	(472,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'7F1D9A5B951645F080FC920BEBBF07DB','lab','control','select:${countries}:code:name',NULL,'en_US','billing_country',0),
	(473,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'C1043CD62FF6496A916723940ECF92E9','lab','constraint','NotEmpty',NULL,'en_US','billing_country',0),
	(474,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'0362C1327AF34185BC1CA6D8C1CF67C3','lab','metaposition','110',NULL,'en_US','billing_country',0),
	(475,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'AEE73416FAF542D996F870A5E9DD91B8','lab','error','Billing Department cannot be empty',NULL,'en_US','billing_departmentId',0),
	(476,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'83F56AE946A74BA39047FC4A83A7A45A','lab','label','Billing Department',NULL,'en_US','billing_departmentId',0),
	(477,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'585365444D984FF0B36580F7976CFFA9','lab','constraint','NotEmpty',NULL,'en_US','billing_departmentId',0),
	(478,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'83D6408A092A40BD947589576FB86C77','lab','metaposition','60',NULL,'en_US','billing_departmentId',0),
	(479,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'99AC1320E4FA44BD81879DDC59125DDD','lab','control','select:${departments}:departmentId:name',NULL,'en_US','billing_departmentId',0),
	(480,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'C2DF771D95944BD79B9C363C985C76AC','lab','label','Billing Institution',NULL,'en_US','billing_institution',0),
	(481,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'41930EE14EB5456F8D132602E524B7C2','lab','error','Institution cannot be empty',NULL,'en_US','billing_institution',0),
	(482,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'2C4C3F7069014498A877C1C81281F905','lab','constraint','NotEmpty',NULL,'en_US','billing_institution',0),
	(483,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'DCD466951C514572935CC12D50C0A6F9','lab','metaposition','50',NULL,'en_US','billing_institution',0),
	(484,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'2274299F63D8467793BE2DE9968BEBCA','lab','label','Billing Phone',NULL,'en_US','billing_phone',0),
	(485,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'5EF10BF8E3EA4EA8A85DA65D012D6A3D','lab','error','Billing Phone cannot be empty',NULL,'en_US','billing_phone',0),
	(486,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'4DA0F87CAB3548E5B68C93A0B9E71BB8','lab','constraint','NotEmpty',NULL,'en_US','billing_phone',0),
	(487,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'4A5B40E138124414A1E6A0813A44604E','lab','metaposition','130',NULL,'en_US','billing_phone',0),
	(488,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'FBCBC5264C7E4EEE8FB9513C2168EF1B','lab','label','Billing State',NULL,'en_US','billing_state',0),
	(489,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'CD7A77A24B4F41E9A508B8125E9E2093','lab','control','select:${states}:code:name',NULL,'en_US','billing_state',0),
	(490,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'1E1FBFAB7EF143E0B5EA3B26DDFB74AA','lab','metaposition','100',NULL,'en_US','billing_state',0),
	(491,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'3223270658AF4C4EAF22ABC4FFF31792','lab','label','Billing Zip',NULL,'en_US','billing_zip',0),
	(492,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'4E4822340BFD488AA2C080EFFEC688C6','lab','error','Billing Zip cannot be empty',NULL,'en_US','billing_zip',0),
	(493,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'3254E09E79D64E39ABE7E03E8EFBEB35','lab','constraint','NotEmpty',NULL,'en_US','billing_zip',0),
	(494,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'3CB01F36A32346298C7727347E8BC1FE','lab','metaposition','120',NULL,'en_US','billing_zip',0),
	(495,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'B5E4B39D014F46529A2A815B283FE1B2','lab','label','Room',NULL,'en_US','building_room',0),
	(496,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'B998AAC156BC436C96C89D7CF0115636','lab','metaposition','30',NULL,'en_US','building_room',0),
	(497,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'40650F7ABDD54873BDE6D8F8A5C3D7C1','lab','error','Lab was NOT created. Please fill in required fields.',NULL,'en_US','created',0),
	(498,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'868069E0E0234FC88A9BC8232DF95BCD','lab','label','Lab was created',NULL,'en_US','created_success',0),
	(499,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'FCB7A2E6B1F8445DA4294C3FB9493CC4','lab','label','Department',NULL,'en_US','departmentId',0),
	(500,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'3F988FE08E3A4793906867FAF301C3D3','lab','error','Please select department',NULL,'en_US','departmentId',0),
	(501,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'37F05AA8B77448F087E5DBE94774067C','lab','label','Lab Details',NULL,'en_US','detail',0),
	(502,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'72493A766AA14B16AE4D2D12DB9AD5C2','lab','label','External/Internal',NULL,'en_US','internal_external_lab',0),
	(503,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'6FA879B248274D2CA7AA39385178D04A','lab','error','Please specify lab type (External/Internal)',NULL,'en_US','internal_external_lab',0),
	(504,'2013-08-05 19:04:15','2013-08-05 19:04:15',X'C4B022ED159A4C6CBA45B646347EDA10','lab','control','select:external:External;internal:Internal',NULL,'en_US','internal_external_lab',0),
	(505,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'502354990C6B42DBA1FC8C22FAF5BE84','lab','constraint','NotEmpty',NULL,'en_US','internal_external_lab',0),
	(506,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'E45BDE68AE7244F798504422ED4F4840','lab','metaposition','10',NULL,'en_US','internal_external_lab',0),
	(507,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'CC2DB3CDA1474B16B2377EFFDDF1536C','lab','label','Active',NULL,'en_US','isActive',0),
	(508,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'43203A0ADA2441CA84D9A335EE607E01','lab','label','Jobs',NULL,'en_US','jobs',0),
	(509,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'82509BBEAC5E4DABB5E1E01EE1C71A3C','lab','label','To request access to another lab, enter the email address of the PI whose lab you wish to join. Your request is subject to approval by that PI.',NULL,'en_US','joinAnotherLabInstructions',0),
	(510,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'2E5441A6E2D14F1093D6CFD81D028263','lab','label','Email Address Of PI Whose<br />Lab You Wish To Join',NULL,'en_US','joinAnotherLab_emailOfPI',0),
	(511,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'39A5C07FD6974A54ACC6987E459DDE7A','lab','error','The lab you specified is no longer active according to the WASP database',NULL,'en_US','joinAnotherLab_labNotActive',0),
	(512,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'18984E6A02B4452AA2C46FB9410DC11F','lab','error','The person you specified is NOT the head of any laboratory in the WASP database',NULL,'en_US','joinAnotherLab_emailDoesNotBelongToLabPI',0),
	(513,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'EE21BDDE331445CDB7202C4E9503C44E','lab','error','Please provide an email address for the PI whose lab you wish to join',NULL,'en_US','joinAnotherLab_piEmailEmpty',0),
	(514,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'A9E6DB88E49C4FDD94B640C8D32D56B2','lab','error','Please provide a well-formatted email address for the PI whose lab you wish to join',NULL,'en_US','joinAnotherLab_piEmailFormatError',0),
	(515,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'884AA1BD38BD46D28D3329F109154595','lab','error','The email address you provided was NOT found in our database. Please confirm spelling and try again.',NULL,'en_US','joinAnotherLab_emailNotFoundInDatabase',0),
	(516,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'AF675373645A4FDFACF3B5086B9BAC37','lab','label','Submit',NULL,'en_US','joinAnotherLab_submit',0),
	(517,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'F1CD493FA0424C44929952354C0029B2','lab','label','Your request to join another lab has been recorded. An email has been sent to the lab PI for approval.',NULL,'en_US','joinAnotherLab_requestSuccess',0),
	(518,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'7CBF3AC24F454C248455B8806AD1C387','lab','label','To reactive your access to this lab, please contact the PI of the lab. They are able to directly re-activate your access to this lab.',NULL,'en_US','joinAnotherLab_requestToReactivateLabMember',0),
	(519,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'BA2DEEFCE8CF4F0AAD380689620D0EFB','lab','error','An unexpected error occurred while processing your request. Please try again.',NULL,'en_US','joinAnotherLab_unexpectedError',0),
	(520,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'F28B826942384EEB85F2BA062D5A769C','lab','error','You are already a member of this lab.',NULL,'en_US','joinAnotherLab_userIsLabMember',0),
	(521,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'8A89F7D4B724421090CA3B6EC1770910','lab','error','You have already applied to join this lab. Your request is still pending approval by the lab PI.',NULL,'en_US','joinAnotherLab_userIsLabMemberPending',0),
	(522,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'CF1F3D57930E425CBAAD587A389E6376','lab','label','List of Labs',NULL,'en_US','lab_list',0),
	(523,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'0FAFC77F83E64C81AD662E4C96C7BFBF','lab','label','Principal Investigator',NULL,'en_US','list_pi',0),
	(524,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'8B458581121A4712A9CD67849EB502AF','lab','label','Lab Members',NULL,'en_US','list_users',0),
	(525,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'DDA7DE39AF4F4F3DB978D8CBFDF650CC','lab','label','Lab Name',NULL,'en_US','name',0),
	(526,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'7C4FF83D7A00477AB25E03D442DCC20F','lab','error','Lab Name cannot be empty',NULL,'en_US','name',0),
	(527,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'F9B000A0561F455AAFDF84256D3C1A5D','lab','label','New Password',NULL,'en_US','newPassword',0),
	(528,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'AF5A4E7FACCA49A18E3A3DA06EA8A6BD','lab','label','Confirm New Password',NULL,'en_US','newPasswordConfirm',0),
	(529,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'E096A8A92A424E81A80AB41E73001623','lab','label','Old Password',NULL,'en_US','oldPassword',0),
	(530,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'57D775560261470DA49DE26A9B667583','lab','label','Phone',NULL,'en_US','phone',0),
	(531,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'9733F1580D4A4A759FA28541BC0F8757','lab','error','Phone cannot be empty',NULL,'en_US','phone',0),
	(532,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'9F02D524164B43C6B5AAE84F809BCA3C','lab','constraint','NotEmpty',NULL,'en_US','phone',0),
	(533,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'BEB9D3EF5F944808941123FF8ED8C076','lab','metaposition','20',NULL,'en_US','phone',0),
	(534,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'BAF0036465294817BD084444FC7163FA','lab','label','Principal Investigator',NULL,'en_US','primaryUser',0),
	(535,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'7A5A67CE20BC4947B0BC5D88FF52EB09','lab','label','Principal Investigator',NULL,'en_US','primaryUserId',0),
	(536,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'5D7D8B97DD2442DDB7FF406ABC24A44A','lab','error','Please select Principal Investigator',NULL,'en_US','primaryUserId',0),
	(537,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'C0EAEA01F0414E5C89A27DEA093A9048','lab','error','Lab was NOT successfully updated. Please fill in required fields.',NULL,'en_US','updated',0),
	(538,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'F88A00618BD24BD4AB42A3917A04E649','lab','label','Lab was updated',NULL,'en_US','updated_success',0),
	(539,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'40121500472743918BC80EC05A451156','lab','error','Please correct the errors indicated below and re-submit.',NULL,'en_US','upgradeStatusToPI_fixErrorsInForm',0),
	(540,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'65E74E8E25F84A38ABE5415D2B1CD3BE','lab','label','Complete the form below to upgrade your WASP status to include Principal Investigator of your own lab.',NULL,'en_US','upgradeStatusToPI_instructions',0),
	(541,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'E8A1B0E4E7CE4BA6AE56D9EA36C5C008','lab','label','Note that this request is subject to WASP administrator confirmation.',NULL,'en_US','upgradeStatusToPI_instructionsWarning',0),
	(542,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'42C48D6F04A742F1969873E105533451','lab','label','Your request to become a Principal Investigator of your own lab has been received and will be reviewed. ',NULL,'en_US','upgradeStatusToPI_requestSuccess',0),
	(543,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'0F7D10AC8F1A4AE4A14CA6A368CEBC59','lab','error','The WASP database indicates that you are already a PI of a lab. You many NOT apply to be the PI of more than one lab.',NULL,'en_US','upgradeStatusToPI_userIsAlreadyPI',0),
	(544,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'0BB8E43DD1E342F4B80A49FEE4914AEB','lab','error','The WASP database indicates that you have already applied to become a PI. Your request has not yet been reviewed.',NULL,'en_US','upgradeStatusToPI_userIsAlreadyPIPending',0),
	(545,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'C9E25B04404849418019CD3AF3FF76B3','lab','error','An unexpected error occurred while processing your request. Please try again.',NULL,'en_US','upgradeStatusToPI_unexpectedError',0),
	(546,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'D416FD1A53B44A328A4EF9EABB34A2F3','labDetail','label','Cancel',NULL,'en_US','cancel',0),
	(547,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'506119FF39F6420BAED90CE460BE7F60','labDetail','label','create job',NULL,'en_US','create_job',0),
	(548,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'556FE13C2ECB403295D115E41EFABA12','labDetail','label','Edit',NULL,'en_US','edit',0),
	(549,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'70503FB5F6F64FC8B3F20FE21A320930','labDetail','label','Save Changes',NULL,'en_US','save',0),
	(550,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'12A282DD03DF483A984571B171AEA7DF','labPending','error','Invalid action. Must be approve or reject only',NULL,'en_US','action',0),
	(551,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'7508DFB6458344DAA4D1448478AF983D','labPending','label','Approve',NULL,'en_US','approve',0),
	(552,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'3F773609119C45D7A22C609936429C8D','labPending','label','New lab application successfully approved',NULL,'en_US','approved',0),
	(553,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'8EE54632192749A4AD9FC3711D9D5528','labPending','label','Billing Address',NULL,'en_US','billing_address',0),
	(554,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'286200BA61E549F9B5D7BEF3EBD2DE90','labPending','error','Billing Address cannot be empty',NULL,'en_US','billing_address',0),
	(555,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'A7C49BF7399B470DAC58A7CA9824019F','labPending','constraint','NotEmpty',NULL,'en_US','billing_address',0),
	(556,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'FC60841EE62F440B875EC48D470F22EB','labPending','metaposition','80',NULL,'en_US','billing_address',0),
	(557,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'071C741B56F444DF856CA6C4C76C466F','labPending','label','Room',NULL,'en_US','billing_building_room',0),
	(558,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'24D106884D3743658FB6632C841665D3','labPending','metaposition','70',NULL,'en_US','billing_building_room',0),
	(559,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'B0997C1DD8D7423E91D1E3F7F1D869B8','labPending','label','Billing City',NULL,'en_US','billing_city',0),
	(560,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'545E17FBDEE44422B7DA60A3B0D3C271','labPending','error','Billing City cannot be empty',NULL,'en_US','billing_city',0),
	(561,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'2E53D6BA9E5E433DB67141DC4F46C124','labPending','constraint','NotEmpty',NULL,'en_US','billing_city',0),
	(562,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'A09D171A2BF34A98872640CD62A8AACF','labPending','metaposition','90',NULL,'en_US','billing_city',0),
	(563,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'B13EC426D8184893A977063C7A1C378A','labPending','label','Billing Contact',NULL,'en_US','billing_contact',0),
	(564,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'33E3CE77E52942469DDA85F74632E3F9','labPending','error','Billing Contact cannot be empty',NULL,'en_US','billing_contact',0),
	(565,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'C824C18C3C34429D88F2D810F8942095','labPending','constraint','NotEmpty',NULL,'en_US','billing_contact',0),
	(566,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'6705FB435ABC485B950BD6096961A866','labPending','metaposition','40',NULL,'en_US','billing_contact',0),
	(567,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'5BD4E50DF7C54C558302B1A310790480','labPending','label','Billing Country',NULL,'en_US','billing_country',0),
	(568,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'545BFA555CA64ABCB1C2F950E5633DFC','labPending','error','Billing Country cannot be empty',NULL,'en_US','billing_country',0),
	(569,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'D63A9E4DEA1A466B922C8A113A8FEA9B','labPending','control','select:${countries}:code:name',NULL,'en_US','billing_country',0),
	(570,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'A29A0AAB3ACC499795BD11468A959416','labPending','constraint','NotEmpty',NULL,'en_US','billing_country',0),
	(571,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'780F65D0E17E455289D0EFB4E618AA6C','labPending','metaposition','110',NULL,'en_US','billing_country',0),
	(572,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'D2DF5DB9E468463EBAC3C265741198F1','labPending','error','Billing Department cannot be empty',NULL,'en_US','billing_departmentId',0),
	(573,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'FF58CD549E52422688DF88F4F84284CB','labPending','label','Billing Department',NULL,'en_US','billing_departmentId',0),
	(574,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'CF6CDE38BAFB4A98B0D971157341E4D6','labPending','constraint','NotEmpty',NULL,'en_US','billing_departmentId',0),
	(575,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'3642C96125274F01BC7EEAE8919A9DAC','labPending','metaposition','60',NULL,'en_US','billing_departmentId',0),
	(576,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'3B84B58FE8584BE0B942AFA14C981AF1','labPending','control','select:${departments}:departmentId:name',NULL,'en_US','billing_departmentId',0),
	(577,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'E0405D4C166C42A8B601B3B44EA9590D','labPending','label','Billing Institution',NULL,'en_US','billing_institution',0),
	(578,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'9AAB26C153D9480EA96462280D8071C2','labPending','error','Institution cannot be empty',NULL,'en_US','billing_institution',0),
	(579,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'48FEBADDAE2349B3AAD91FCB7830745A','labPending','constraint','NotEmpty',NULL,'en_US','billing_institution',0),
	(580,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'7DE718B6E82B4F229DC5D7090D5E8F90','labPending','metaposition','50',NULL,'en_US','billing_institution',0),
	(581,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'3F7BEBC701EE4C8A9A2232C16147EAAF','labPending','label','Billing Phone',NULL,'en_US','billing_phone',0),
	(582,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'0B0B1A2EF225403EAD246FF880E44BF9','labPending','error','Billing Phone cannot be empty',NULL,'en_US','billing_phone',0),
	(583,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'C7CC63DAAD7C48688075191F31792C72','labPending','constraint','NotEmpty',NULL,'en_US','billing_phone',0),
	(584,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'24998897820A4285878C515E3A08EEDC','labPending','metaposition','130',NULL,'en_US','billing_phone',0),
	(585,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'B2DB4C02D4F54723AD46B908D6DBD86A','labPending','label','Billing State',NULL,'en_US','billing_state',0),
	(586,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'1A5B5695FBCB4B898720AE957A477395','labPending','control','select:${states}:code:name',NULL,'en_US','billing_state',0),
	(587,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'E69768AC057D4624BB25758F6A01671F','labPending','metaposition','100',NULL,'en_US','billing_state',0),
	(588,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'19F8BBE6F8724F9988CA0A01FFC94734','labPending','label','Billing Zip',NULL,'en_US','billing_zip',0),
	(589,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'FF17C37C1C8A41B0B0E2371221858707','labPending','error','Billing Zip cannot be empty',NULL,'en_US','billing_zip',0),
	(590,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'640363AFCDDC4FF4A756A0287CBB7576','labPending','constraint','NotEmpty',NULL,'en_US','billing_zip',0),
	(591,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'9063C25D92FB452A96C514D7AFFCD16D','labPending','metaposition','120',NULL,'en_US','billing_zip',0),
	(592,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'291B6002120A49F1B0D729836F919FE5','labPending','label','Room',NULL,'en_US','building_room',0),
	(593,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'17607E69764B40C9944928D137DD0C47','labPending','metaposition','30',NULL,'en_US','building_room',0),
	(594,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'157CD562CF7D4B51AF3B7E5E5C350DE2','labPending','label','Cancel',NULL,'en_US','cancel',0),
	(595,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'B12387FB9DE341D2939EC91F20BA18DC','labPending','error','Failed to process new lab request',NULL,'en_US','could_not_create_lab',0),
	(596,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'7B5DD9381362416594DE7B4B15561C77','labPending','error','Pending Lab was NOT created. Please fill in required fields.',NULL,'en_US','created',0),
	(597,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'422A9B4C48B844C0915BDA7FDE78B5CB','labPending','error','Problem updating metadata',NULL,'en_US','created_meta',0),
	(598,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'A55809F74ABB4F97B365BA322D140C43','labPending','label','Create New Lab Request',NULL,'en_US','createNewLab',0),
	(599,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'48D288E7C803415FADAFFC54C76D058B','labPending','label','Department',NULL,'en_US','departmentId',0),
	(600,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'6071C866E7044EAE967023F11D74C149','labPending','error','Please select department',NULL,'en_US','departmentId',0),
	(601,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'4B049AA474CE4FCA9C422E27233DC15D','labPending','error','Deparment id mismatch with lab-pending id',NULL,'en_US','departmentid_mismatch',0),
	(602,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'2BB33E5F84F347AB8F8EC20778877B33','labPending','label','Edit',NULL,'en_US','edit',0),
	(603,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'8F2AABD8589F4F838104035BA49297E0','labPending','label','Pending Lab Details:',NULL,'en_US','heading',0),
	(604,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'324621B024024DE79A8A9AC20598C1CA','labPending','label','External/Internal',NULL,'en_US','internal_external_lab',0),
	(605,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'7408C2D518EC451C9D04DCC3A097B48B','labPending','error','Please specify lab type (External/Internal)',NULL,'en_US','internal_external_lab',0),
	(606,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'62F2E04842DC426F82B1E6FDA5B462B5','labPending','control','select:external:External;internal:Internal',NULL,'en_US','internal_external_lab',0),
	(607,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'E825748B156D4B9EB6355AF9BD1DBBE9','labPending','constraint','NotEmpty',NULL,'en_US','internal_external_lab',0),
	(608,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'44208CDA13BC4EA889ADF3C0A77A3FB2','labPending','metaposition','10',NULL,'en_US','internal_external_lab',0),
	(609,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'3A6F79E8D538418B98AAD9E108F8FC0E','labPending','error','Lab-pending id does not exist',NULL,'en_US','labpendingid_notexist',0),
	(610,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'768F8BD67A30498B8D6E02EF22DAE66F','labPending','label','Lab Name',NULL,'en_US','name',0),
	(611,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'DF53D0F2CF7C441EA9FFEFFABAF8EF7E','labPending','error','Lab Name cannot be empty',NULL,'en_US','name',0),
	(612,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'DA3036755A124898B65EDE8EF1B958BC','labPending','label','Submit',NULL,'en_US','newLabSubmit',0),
	(613,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'65371CCF328C49ACAAC2185A78467C64','labPending','label','Request subject to WASP administrator confirmation of principal investigator status.',NULL,'en_US','newPiNote',0),
	(614,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'E7123DE86CBA435E9EF3435C29321FAD','labPending','label','Pending Lab Details',NULL,'en_US','pendingLabDetails',0),
	(615,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'F4643BEA959C451DA3329299F276AEB1','labPending','label','Phone',NULL,'en_US','phone',0),
	(616,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'4A24427AF2E3437C88636A10B42A8CAD','labPending','error','Phone cannot be empty',NULL,'en_US','phone',0),
	(617,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'C6551B9ACFCA429098673F15F99DD84B','labPending','constraint','NotEmpty',NULL,'en_US','phone',0),
	(618,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'790463CC85D243DC93F6EFAD11D0A39F','labPending','metaposition','20',NULL,'en_US','phone',0),
	(619,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'3A92AF816D8A4E7B9B9D9A64427ABC02','labPending','label','Primary User',NULL,'en_US','primaryUserId',0),
	(620,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'5D0D74ECFC8D4E81879D252DF6A86B13','labPending','error','Please select Primary User',NULL,'en_US','primaryUserId',0),
	(621,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'503627E00C7C407BB058B922032497D5','labPending','label','Reject',NULL,'en_US','reject',0),
	(622,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'D4CDEB424FA54AB0A5FA1310EE42C1EA','labPending','label','New lab application successfully rejected',NULL,'en_US','rejected',0),
	(623,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'67CF9C7D346E40308D51FB58FF61FCCC','labPending','error','Update Failed: You MUST provide a reason for rejecting this request',NULL,'en_US','rejectedPIMustHaveComment',0),
	(624,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'23A2334E5E54470CA0CC8F5022B3FA83','labPending','label','Save Changes',NULL,'en_US','save',0),
	(625,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'EFBFA572295141B29F9A18CC3A33D09A','labPending','error','Status must be pending with lab-pending id',NULL,'en_US','status_mismatch',0),
	(626,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'3AF3547C5DBC491D828A18B77CF118F3','labPending','error','Pending lab is already approved or rejected',NULL,'en_US','status_not_pending',0),
	(627,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'51914FF484DB47FCA10EEBF5E8FC83DC','labPending','error','Pending Lab was NOT updated. Please fill in required fields.',NULL,'en_US','updated',0),
	(628,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'B201F9C06F6944E1A3C409A12B60C3BD','labPending','label','Pending Lab updated successfully.',NULL,'en_US','updated_success',0),
	(629,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'88E592163AF9417C90E9670E9F90A2BC','labuser','label','Actions',NULL,'en_US','actions',0),
	(630,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'47EAF1878E434867A04885DD161F1304','labuser','label','Active',NULL,'en_US','active',0),
	(631,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'A3E47BC57548446D8AA4302A38B63E82','labuser','label','Current Lab Members',NULL,'en_US','current',0),
	(632,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'472D96454FF94950991A308A12D5709A','labuser','label','Alter Status',NULL,'en_US','change_status_in_lab',0),
	(633,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'1287F228B22344C299826EFF4251E397','labuser','label','Former Lab Members',NULL,'en_US','former',0),
	(634,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'9B33381C25094201B570EC647359F8BC','labuser','label','Email',NULL,'en_US','email',0),
	(635,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'6E6AF13F6AF74463A83C8ADD33CA4309','labuser','label','Inactive',NULL,'en_US','inactive',0),
	(636,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'2D20AF8C8933423EA6917A5231C33914','labuser','error','Lab not found in database.',NULL,'en_US','labNotFound',0),
	(637,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'D305081C5CCC45FAB55F282C6A012C1C','labuser','label','Request subject to principal investigator acceptance.',NULL,'en_US','labUserNote',0),
	(638,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'8B0F7B2C4E6742F79BF94F21A96852E7','labuser','error','Cannot locate specified lab user in specified lab',NULL,'en_US','labUserNotFoundInLab',0),
	(639,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'959C4E23A8E046148BC9CC87CBF998B0','labuser','label','Login Name',NULL,'en_US','loginName',0),
	(640,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'DFC7E75B70F9465AA949C3F719891A4A','labuser','label','Name',NULL,'en_US','name',0),
	(641,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'EA172D7814074A9A9909A609CBC29DBA','labuser','label','Pending Lab Members',NULL,'en_US','request',0),
	(642,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'460BACF0D1234ADAB889A32D5C9F1ABF','labuser','error','You are currently a member of the requested lab.',NULL,'en_US','request_alreadyaccess',0),
	(643,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'CD96978902BB49AC95BBED7295333413','labuser','error','You are already a pending user for the requested lab.',NULL,'en_US','request_alreadypending',0),
	(644,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'4EEC6F4120C44822806E1F3DACBBF4ED','labuser','label','WASP Login Name of Primary Investigator',NULL,'en_US','request_primaryuser',0),
	(645,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'53A124C0E4C14D1ABC6DF31466E6E5FF','labuser','error','Invalid Primary User',NULL,'en_US','request_primaryuser',0),
	(646,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'FF12C6CE110C4E90AA58438681BDD14B','labuser','label','Request Access',NULL,'en_US','request_submit',0),
	(647,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'BC31E54B2CF0438EAA0E187FA9194252','labuser','label','Your request for access has been submitted.',NULL,'en_US','request_success',0),
	(648,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'6B3A4B07CAB54402BEDD80434E0E2907','labuser','label','Role',NULL,'en_US','role',0),
	(649,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'68BF56810AE34C399E4864211D640E2B','labuser','label','Roles updated successfully',NULL,'en_US','role_change_request',0),
	(650,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'A89AD6AB977A4B4BB0E84F47768FCF0E','labuser','label','Status',NULL,'en_US','status',0),
	(651,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'1AB6A110520C4CAC89705378A6B505E9','labuser','label','Activate',NULL,'en_US','status_activate',0),
	(652,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'990817CCC29E4B4AA2C0F86467892765','labuser','label','Deactivate',NULL,'en_US','status_deactivate',0),
	(653,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'4C161CD8D5C14BAB83EC92ADCF030AC1','labuser','label','DEMOTE to LU',NULL,'en_US','status_demoteLU',0),
	(654,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'0C6975B40D424CB8A3954F4B8FF926E2','labuser','label','PROMOTE to LM',NULL,'en_US','status_promoteLM',0),
	(655,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'0D26EB9DBEBB4D9BA3C07B39145D562A','labuser','label','Active<br />Lab Member',NULL,'en_US','userManager_activeLabMember',0),
	(656,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'CAF776647A7A4C17B6502072A1E8BA10','labuser','label','Approve Or Reject',NULL,'en_US','approveOrReject',0),
	(657,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'9A6D9760B04F40C89E70429F028ED661','labuser','label','Alter Status',NULL,'en_US','userManager_change_status',0),
	(658,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'BBDC1F1C26564D3388FFB2E4778165AC','labuser','label','Current &amp; Former Lab Members',NULL,'en_US','userManager_currentAnFormerLabMembers',0),
	(659,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'C666913219BB425C923053AA54E2371D','labuser','label','Downgrade From Manager To Lab Member',NULL,'en_US','userManager_dowgradeFromManagerToLabMember',0),
	(660,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'FAD8593FB53C4906B30B7F9512D6B6A4','labuser','label','Email',NULL,'en_US','userManager_email',0),
	(661,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'5291C51230AB4E19A4BAC123B422859F','labuser','label','Hide Lab Member Rights / Responsibilities',NULL,'en_US','userManager_hideRandR',0),
	(662,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'69E8C13DAA044C02980029315D4A6BA9','labuser','label','Inactive<br />Lab Member',NULL,'en_US','userManager_inactiveLabMember',0),
	(663,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'47F099B6E745489298193D11841EF2C9','labuser','label','Inactivate Lab Membership',NULL,'en_US','userManager_inactivateLabMembership',0),
	(664,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'BCC32DCD7B794C539681E374A10249EB','labuser','label','Lab<br />Manager',NULL,'en_US','userManager_labManager',0),
	(665,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'FB04DC7F7BC04E0AB3A05B61DC23AECA','labUser','error','Update Failed: Lab unexpectedly not found in database',NULL,'en_US','userManager_labNotFound',0),
	(666,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'6DABE41AEA104400A54255FF90CA90F5','labUser','label','Update Successful',NULL,'en_US','userManager_labRoleSuccessfullyUpdated',0),
	(667,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'C775367A999D4CE480449287372610D4','labuser','label','Name',NULL,'en_US','userManager_name',0),
	(668,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'67D8292C8915462B8922A56F78EECC0E','labuser','label','Principal<br />Investigator',NULL,'en_US','userManager_pi',0),
	(669,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'85DA3EFC5B124C6A9B08E069571AD39C','labuser','label','<b>Note To Lab PI</b>: Below you can assign one or more members of your lab to function as a WASP lab manager, permitting them to:<br />--Approve / Reject new WASP lab members to your lab<br />--Approve / Reject WASP job submissions from your lab members<br />--Inactivate WASP lab members so they can no longer submit jobs from your lab',NULL,'en_US','userManager_piInstructions',0),
	(670,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'F103B21ACEF24C26BFD18D78A50340FA','labuser','label','Principal Investigator',NULL,'en_US','userManager_principalInvestigator',0),
	(671,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'F8993A8AA3C64365858E74772AAD3E59','labuser','label','Reinstate Lab Membership',NULL,'en_US','userManager_reinstate',0),
	(672,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'A9A2F15579404E0085C330CA959EB9DD','labuser','label','Reinstate &amp; Upgrade To Lab Manager',NULL,'en_US','userManager_reinstateAndSetToManager',0),
	(673,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'5B953209ACFF4FE2899EBADC73AD10B3','labuser','label','View Ones Own Jobs &amp; Data',NULL,'en_US','userManager_right1',0),
	(674,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'7510063F742C4768883B9B67169FAC36','labuser','label','Grant Access To Others To View Ones Own Data',NULL,'en_US','userManager_right2',0),
	(675,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'4CE97C58826A4CECA6052C73710DB0B6','labuser','label','Submit Job Requests From This Lab',NULL,'en_US','userManager_right3',0),
	(676,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'07B205501A6349A49F1351929EF6043B','labuser','label','Approve/Reject Job Requests',NULL,'en_US','userManager_right4',0),
	(677,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'EE1A4A3881774E278587815A161FAA15','labuser','label','Approve/Reject New Lab Members',NULL,'en_US','userManager_right5',0),
	(678,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'BA47A211FA1640BEB566B9A36F28DA97','labuser','label','Inactivate Lab Members (to prevent future job submissions from this lab)',NULL,'en_US','userManager_right6',0),
	(679,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'21BE85234BDD4F7C9E87F584B685F11E','labuser','label','Upgrade Member To Manager; Downgrade Manager to Member',NULL,'en_US','userManager_right7',0),
	(680,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'28A7DF1A6FAF4E16924A36EE52742F07','labuser','label','View Jobs &amp; Data From All Current And Former Lab Members',NULL,'en_US','userManager_right8',0),
	(681,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'A716F24A8FB249EB8499E4803C4EE6E7','labuser','label','Grant Access To Others To View Data Belonging To Any Current/Former Lab Member',NULL,'en_US','userManager_right9',0),
	(682,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'A401147C2BED4F7A83D6091513C12EC6','labuser','label','WASP Lab Member Rights<br />And Responsibilities',NULL,'en_US','userManager_rightsResponsibilities',0),
	(683,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'57BCFA2B573C4AE0B523AB6A2F61ED77','labUser','error','Update Failed: Selected role unexpectedly not found in database',NULL,'en_US','userManager_roleNotFound',0),
	(684,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'2D29031477DB4FF49854FDC06248D886','labuser','label','Show Lab Member Rights / Responsibilities',NULL,'en_US','userManager_showRandR',0),
	(685,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'8837C840CFA84A7D9323270AB6A122E6','labUser','error','Update Failed: Unexpected inability to save change to database',NULL,'en_US','userManager_updateUnexpectedlyFailed',0),
	(686,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'ECE61C797EF1413F9D46BE233484D5A6','labuser','label','Upgrade To Lab Manager',NULL,'en_US','userManager_upgradeToManager',0),
	(687,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'3689B02FF90E45D0B2607A9D626D91AB','labUser','error','Update Failed: User unexpectedly not found in database',NULL,'en_US','userManager_userNotFound',0),
	(688,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'CFCED1F280704607A8154EB0269FB507','labUser','error','Update Failed: User unexpectedly not in this lab, according to our the database',NULL,'en_US','userManager_userNotInLab',0),
	(689,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'0425CBDA3B1D401D90664642AE039DC5','labuser','label','Users Requesting To Join This Lab',NULL,'en_US','userManager_usersRequestingToJoinThisLab',0),
	(690,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'FAD75C8E1D954163AD6B71043E8F505A','labuser','label','Status',NULL,'en_US','userManager_status',0),
	(691,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'952D81DFFC864FA79D0A5BE911B05C71','libraryCreated','label','Library successfully created.',NULL,'en_US','created_success',0),
	(692,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'15F4FE828F224279B875660F61560837','libraryCreated','error','Failure to send status message to wasp-daemon',NULL,'en_US','message_fail',0),
	(693,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'A0D43DE8CA7E4BA5AB1F6FD0AC1FF8FB','libraryCreated','error','Problem occurred with the preparing the library for saving',NULL,'en_US','sample_problem',0),
	(694,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'2991FF509CBD4F36A1FE66EC18D7F472','librarydetail_ro','label','Cancel',NULL,'en_US','cancel',0),
	(695,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'D683580764CD4611BA81661A0730F854','librarydetail_ro','label','Edit',NULL,'en_US','edit',0),
	(696,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'0088850D61EC45708E62AC061DF7B7E4','librarydetail_ro','label','Library Details',NULL,'en_US','libraryDetails',0),
	(697,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'7FA18364EF8C4EE48FBBFE18B6F43451','librarydetail_ro','label','Library Name',NULL,'en_US','libraryName',0),
	(698,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'704F81F306734AE7AE682CC1B67A68C1','librarydetail_ro','label','Library Type',NULL,'en_US','librarySampleType',0),
	(699,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'D5E7C6413E604CD9931156425BF3FFF0','librarydetail_rw','label','Cancel',NULL,'en_US','cancel',0),
	(700,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'DC92DB08EE65485A95AF4C96032EB554','librarydetail_rw','label','Library Details',NULL,'en_US','libraryDetails',0),
	(701,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'DC1180520F99420DA9284748C13BF89B','librarydetail_rw','label','Library Name',NULL,'en_US','libraryName',0),
	(702,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'AAFF9A209A6B4BB5B585ACE6B77D5794','librarydetail_rw','label','Library Subtype',NULL,'en_US','librarySubtype',0),
	(703,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'8BE7CEBACA5C4BA3BD906408EDB55E8D','librarydetail_rw','label','Library Type',NULL,'en_US','libraryType',0),
	(704,'2013-08-05 19:04:16','2013-08-05 19:04:16',X'E09B6D4FCD0C4197AC401E6FFD1DB993','librarydetail_rw','label','Save',NULL,'en_US','save',0),
	(705,'2013-08-05 19:04:16','2013-08-05 19:04:17',X'8F78C62B75DB4CF8852367984FDAE3CA','librarydetail_rw','label','--SELECT NEW ADAPTOR SET--',NULL,'en_US','selectNewAdaptorSet',0),
	(706,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'4EBA4FD7686E4F9CA97443F4F5A2629F','listJobSamples','label','Adaptor',NULL,'en_US','adaptor',0),
	(707,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'E7E42B0191C0452CA82F2503FBDF6412','listJobSamples','label','Add Library To Platform Unit',NULL,'en_US','addLibraryToPlatformUnit',0),
	(708,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'CE7EEF9BA74B4D539C53A869159E5613','listJobSamples','label','Add New Viewer',NULL,'en_US','addNewViewer',0),
	(709,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'FD33AE32605A4E349CEDF1069A340F04','listJobSamples','label','Person is already a viewer for this job.',NULL,'en_US','alreadyIsViewerOfThisJob',0),
	(710,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'FDE123A50F024788972E334708314B57','listJobSamples','label','Arrival Status',NULL,'en_US','arrivalStatus',0),
	(711,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'18FFAC81DE9A429F8D433F5BBD50C020','listJobSamples','label','Cancel',NULL,'en_US','cancel',0),
	(712,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'18CF1DA432E04A44A5B85E65198290EA','listJobSamples','label','Cell',NULL,'en_US','cell',0),
	(713,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'4791B0DB9D474BDA97DF1472047E5854','listJobSamples','label','Create Library',NULL,'en_US','createLibrary',0),
	(714,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'D6333D2C1C10436AACAE444E4EAD3785','listJobSamples','label','Action',NULL,'en_US','file_action',0),
	(715,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'30A9739814BB4EDA975DD0A3E54F1E8C','listJobSamples','label','Description',NULL,'en_US','file_description',0),
	(716,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'A0ADF861B1A0406B9002F8E691144253','listJobSamples','label','Provide a short description for the file you wish to upload',NULL,'en_US','file_description_missing',0),
	(717,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'1188812398874383AE2B4FD708049E03','listJobSamples','label','Download',NULL,'en_US','file_download',0),
	(718,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'8D56D2314A4F4CBF9338F36F9DA47FBD','listJobSamples','label','Grouped Files',NULL,'en_US','file_download_grouped_files',0),
	(719,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'9F51556D2FC04308A0D8E3CED14C09E5','listJobSamples','label','Please select a file to upload',NULL,'en_US','file_empty',0),
	(720,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'58D20DAA55C54020AD35737A11CE7D2B','listJobSamples','label','Uploaded Files',NULL,'en_US','file_name',0),
	(721,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'F94D4D2286584304AD65525C1B807D7F','listJobSamples','label','Upload',NULL,'en_US','file_upload',0),
	(722,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'A8C72D4B27E744CEB0151218B9C843AF','listJobSamples','error','Upload unexpectedly failed. Try again.',NULL,'en_US','fileUploadFailed',0),
	(723,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'966A9B4D7AB24ECCBEC4D9A86ECBF548','listJobSamples','error','Upload failed. Provide a short description of the file you wish to upload.',NULL,'en_US','fileUploadFailed_fileDescriptionEmpty',0),
	(724,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'82D78455698C4558BE25BA9AC8441E89','listJobSamples','error','Upload failed. File is empty.',NULL,'en_US','fileUploadFailed_fileEmpty',0),
	(725,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'6D9677638B174537836440FF14F9468F','listJobSamples','label','File uploaded successfully',NULL,'en_US','fileUploadedSuccessfully',0),
	(726,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'8DECB9FF595F4A74BD07790EADD225D5','listJobSamples','label','Final Concentration (pM)',NULL,'en_US','finalConcentrationPM',0),
	(727,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'F819D20487CB4675941B5586D5AFA475','listJobSamples','label','Hide Files',NULL,'en_US','hideJobFiles',0),
	(728,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'9ABF7F9EDCA7476A9D725358526DA178','listJobSamples','label','Hide User-Requested Cell Assignments',NULL,'en_US','hideUserRequestedCoverage',0),
	(729,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'46AE65BB1EA04A7394E94DDF395608B8','listJobSamples','label','Index',NULL,'en_US','index',0),
	(730,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'1850B9DEE7CC4513B6B183F5AB980ED1','listJobSamples','label','Initial Macromolecules',NULL,'en_US','initialMacromolecules',0),
	(731,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'17C61AE50FCD48C3AA51A73B562ABEBD','listJobSamples','label','You may NOT perform this operation.',NULL,'en_US','illegalOperation',0),
	(732,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'0A19E10311B64209B8DA8906D1FCD19D','listJobSamples','label','Email format invalid.',NULL,'en_US','invalidFormatEmailAddress',0),
	(733,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'9FCE1B3C2E284E4BACAE0C3A0FB86CDF','listJobSamples','label','Job Not Found In Database',NULL,'en_US','jobNotFound',0),
	(734,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'BDBA931436FF419F9A842AAD9957D540','listJobSamples','label','New Job Viewer Added',NULL,'en_US','jobViewerAdded',0),
	(735,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'3E7983CB6D22452EB8987C2EA05320F4','listJobSamples','label','Selected Viewer Has Been Removed',NULL,'en_US','jobViewerRemoved',0),
	(736,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'195A73FDD6784D3FB2DB9C760B065591','listJobSamples','label','Job Viewers',NULL,'en_US','jobViewers',0),
	(737,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'0746C752948048A398CDCCFF87208CBD','listJobSamples','label','Libraries',NULL,'en_US','libraries',0),
	(738,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'A1DBCEA979E84ED6A5CB24BC6AD1C707','listJobSamples','label','Library',NULL,'en_US','library',0),
	(739,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'8C375EC6213641FC82277CB004E533FA','listJobSamples','label','Library (Control)',NULL,'en_US','libraryControl',0),
	(740,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'622EAB7E644145E2A9B6E361F9F814DA','listJobSamples','label','log sample',NULL,'en_US','logSample',0),
	(741,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'4E2A82DB6F4D45F4A6028FC72BDFE5A0','listJobSamples','label','Please provide an email address',NULL,'en_US','missingEmailAddress',0),
	(742,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'AAAE16DF555F4882B558768D7FB32C4D','listJobSamples','label','Name',NULL,'en_US','name',0),
	(743,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'3F10535A6C8B4615BF92A9EE6558C0DD','listJobSamples','label','Email Address Of Viewer',NULL,'en_US','newViewerEmailAddress',0),
	(744,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'B2BB64CA229D46ECB620B5617E6542CB','listJobSamples','label','No Libraries Created',NULL,'en_US','noLibrariesCreated',0),
	(745,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'3B7A3F03DA49428081B73671FDE7894B','listJobSamples','label','No Platform Units / Runs',NULL,'en_US','noPlatformUnitsAndRuns',0),
	(746,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'5C0C42FA55E24AB1ADB57B67CE91C7AC','listJobSamples','label','Please provide a numeric value for Final Concentration in the cell (pM)',NULL,'en_US','numValFinalConc_alert',0),
	(747,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'CA4A35D59C0B47E184478D470557AC67','listJobSamples','label','Platform Unit',NULL,'en_US','platformUnit',0),
	(748,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'6491FB97EA30469FB5795ED1E2B1DDF0','listJobSamples','label','Platform Units / Runs',NULL,'en_US','platformUnitsAndRuns',0),
	(749,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'017B1167EAA24A34B7F3DD010236E5E6','listJobSamples','label','Job PI may NOT be removed',NULL,'en_US','piRemovalIllegal',0),
	(750,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'87512F441DD9453D84F1065B3245DA6E','listJobSamples','label','Processing',NULL,'en_US','processing',0),
	(751,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'A434E7A906B444BCBB590DFE75549F59','listJobSamples','label','QC Status',NULL,'en_US','qcStatus',0),
	(752,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'41B7D5DC4EC34AB49AF65366EAACCBA0','listJobSamples','label','Role Not Found. Unable To Proceed.',NULL,'en_US','roleNotFound',0),
	(753,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'E5BE39B7D398483995047AA3C4DE2CEC','listJobSamples','label','remove',NULL,'en_US','remove',0),
	(754,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'762C606CD7A94FBD95F6D7C1DEE3248D','listJobSamples','label','--SELECT ADAPTOR SET FOR NEW LIBRARY--',NULL,'en_US','selectAdaptorSetForNewLibrary',0),
	(755,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'1F4FCB54E05D4F54A5E71D4C044458DB','listJobSamples','label','--SELECT A PLATFORM UNIT CELL--',NULL,'en_US','selectPlatformUnitCell',0),
	(756,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'96545E15A0BF4EA5BCEB600CF66F049C','listJobSamples','label','Show Files',NULL,'en_US','showJobFiles',0),
	(757,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'0F9F9962A9EA449BBE94AC843C26C3A5','listJobSamples','label','Show User-Requested Cell Assignments',NULL,'en_US','showUserRequestedCoverage',0),
	(758,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'5AB370C15EE24A1FAB408C9F4D83205E','listJobSamples','label','Organism',NULL,'en_US','organism',0),
	(759,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'A7A7692740F14112B8BBB9C375AC5FCF','listJobSamples','label','Submit',NULL,'en_US','submit',0),
	(760,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'9FF2DAF0C9574EF186A3C87199521F50','listJobSamples','label','Job Submitter may NOT be removed',NULL,'en_US','submitterRemovalIllegal',0),
	(761,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'DC06BA04F9E14EC3864C3B5BB0009440','listJobSamples','label','Type',NULL,'en_US','type',0),
	(762,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'DD2B7DE0AA41435197F68F03D2E90DEC','listJobSamples','label','User-Submitted Libraries',NULL,'en_US','userSubmittedLibraries',0),
	(763,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'C7CE3D4CEAA74A2FA94FF3DC473056D3','listJobSamples','label','User not found in database.',NULL,'en_US','userNotFound',0),
	(764,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'EEADB91FEB254F26B7CD2EE685792EEC','listJobSamples','label','Email address not found in database.',NULL,'en_US','userNotFoundByEmailAddress',0),
	(765,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'9DE59C35E18448D1A127ED634BA008CE','listJobSamples','label','User is not a registered viewer of this job.',NULL,'en_US','userNotViewerOfThisJob',0),
	(766,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'F75594D0CBE543B499328E46F03778C9','listJobSamples','label','Please provide a value for Final Concentration in the cell (pM)',NULL,'en_US','valFinalConc_alert',0),
	(767,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'1E443F945CAD41E58CBC2E224B674511','listJobSamples','label','View',NULL,'en_US','view',0),
	(768,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'F59A3AA2546445D18FFAED0D7195BF0D','listJobSamples','label','You must select a cell',NULL,'en_US','youMustSelectCell_alert',0),
	(769,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'3988CFEEFC2643A7B7BDACA8A438DB5E','lmpendingtask','label','APPROVE',NULL,'en_US','approve',0),
	(770,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'05747FFE291D4D25A905FA0DE241F5C0','lmpendingtask','label','Please Select Approve Or Reject',NULL,'en_US','approveRejectAlert',0),
	(771,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'398478DDFCD14FC29634AE1F3FEEE975','lmpendingtask','error','Update Failed: You Must Provide A Reason For Rejecting This Job',NULL,'en_US','commentEmpty',0),
	(772,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'D75C4728B8804E2CBBDCDD1EAB2F260B','lmpendingtask','label','Email',NULL,'en_US','email',0),
	(773,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'252C0833735249DE955206764C66DB82','lmpendingtask','error','Update Failed: Invalid Action',NULL,'en_US','invalidAction',0),
	(774,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'5E176D5B860E47B2BA7E9F6A6653C7ED','lmpendingtask','error','Update Failed: Job Not Found',NULL,'en_US','invalidJob',0),
	(775,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'13CDA5C217B240288C8F05BECCB7019A','lmpendingtask','label','Job Has Been Approved',NULL,'en_US','jobApproved',0),
	(776,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'82B9DF8CA0284B8E843BA073A70DB73D','lmpendingtask','label','Job Has Been Rejected',NULL,'en_US','jobRejected',0),
	(777,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'B8D88953494149778886D191DB30AD0B','lmpendingtask','label','Job ID',NULL,'en_US','jobId',0),
	(778,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'8B03EBF15BF64A18B1B8573FFD823D02','lmpendingtask','label','Job Name',NULL,'en_US','jobName',0),
	(779,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'75EAF6DDE89840488626BDBDDAB38B6F','lmpendingtask','label','Name',NULL,'en_US','name',0),
	(780,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'9E6E0F03E6D54C7BBDF924F41E9586AB','lmpendingtask','label','PI',NULL,'en_US','pi',0),
	(781,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'0FF22ACF610341EAB56CA0CB5E2FDEA9','lmpendingtask','label','REJECT',NULL,'en_US','reject',0),
	(782,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'18C5B6EB23544F8FB6A88EA70F825141','lmpendingtask','label','RESET',NULL,'en_US','reset',0),
	(783,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'6AACFDEB2B8B4B4596687B89A42C9B34','lmpendingtask','label','Samples',NULL,'en_US','samples',0),
	(784,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'41DFBB49156243C288534E3377687CC1','lmpendingtask','label','SUBMIT',NULL,'en_US','submit',0),
	(785,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'1FAA4A66766249A7BEB180ED9346B533','lmpendingtask','label','Submitter',NULL,'en_US','submitter',0),
	(786,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'DF959FFA158545FCB0780662916ADF14','lmpendingtask','label','Pending Users',NULL,'en_US','subtitle1',0),
	(787,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'F79F0B1E30E345EBA485271C643494EB','lmpendingtask','label','No Pending Users',NULL,'en_US','subtitle1_none',0),
	(788,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'EE65C8DDC7124358A7601A874B279B8E','lmpendingtask','label','Pending Jobs',NULL,'en_US','subtitle2',0),
	(789,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'9696A2985C2F4BE5A08FCA94F5AEDE00','lmpendingtask','label','No Pending Jobs',NULL,'en_US','subtitle2_none',0),
	(790,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'EB1D5AF443DF413297368CC5E39A0A65','lmpendingtask','error','Update Unexpectedly Failed',NULL,'en_US','updateFailed',0),
	(791,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'3D93BDBD4C0C46A3A60AF50DAA9961CA','lmpendingtask','label','Please provide a reason for rejecting this job',NULL,'en_US','validateCommentAlert',0),
	(792,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'9392A17182E648BFABEADA9C1EBE4409','lmpendingtask','label','Workflow',NULL,'en_US','workflow',0),
	(793,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'408BCE2775054DD9943F8F90DD033273','lmpendingtask','label','PI/Lab Manager Pending Tasks',NULL,'en_US','title',0),
	(794,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'938A8F2BE8D340019B06500C6C0250AE','metadata','error','Length exceeds maximum permitted',NULL,'en_US','lengthMax',0),
	(795,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'CF182EBB4AEB4299AEED44FAA695856D','metadata','error','Length less than minimum permitted',NULL,'en_US','lengthMin',0),
	(796,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'B2DB7B799DCC47E78BD9EFB1E0DD4E72','metadata','error','Value is not of expected type',NULL,'en_US','metaType',0),
	(797,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'F79A548F3F64415699DDD4995CC2AFD3','metadata','error','Value exceeds maximum permitted',NULL,'en_US','rangeMax',0),
	(798,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'2FA83207D451435D9814E87DFC33C361','metadata','error','Value less than minimum permitted',NULL,'en_US','rangeMin',0),
	(799,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'035B557BCE084F49BED462C34C731D25','menu','label','Assign Libraries',NULL,'en_US','assignLibrariesToPlatformUnits',0),
	(800,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'61159F18C8764041AEE517BF4DEC8A21','menu','label','Admin',NULL,'en_US','admin',0),
	(801,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'FDE5D3D23E6A45238FEEB74685D31B1C','menu','label','All Jobs',NULL,'en_US','allJobs',0),
	(802,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'0FFF604B31C2408DB39D0B3CD92A8F4F','menu','label','All Samples',NULL,'en_US','allSamples',0),
	(803,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'EB021CA19E8E44FDAA3B8D388576713A','menu','label','Bisulfate-Seq',NULL,'en_US','bisulfateseq',0),
	(804,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'11988601AF5D43449C48B4681D602B35','menu','label','Change Password',NULL,'en_US','changePassword',0),
	(805,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'8BE73D1F0EDE45628013152B8BE44BCB','menu','label','ChIP-Seq',NULL,'en_US','chipseq',0),
	(806,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'C2DF315C6A124530A8D01343D8A34197','menu','label','Control Libraries',NULL,'en_US','controlLibraries',0),
	(807,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'D3BAA77F386C4B6EBC3E5A7A3BE887A2','menu','label','Department Admin',NULL,'en_US','deptAdmin',0),
	(808,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'3EB9759BAAA842C9A901FFEE6F219C86','menu','label','Dept. Admin. Tasks',NULL,'en_US','deptAdminTasks',0),
	(809,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'6EF2198848BC4EEF9129378DD310BC87','menu','label','Facility',NULL,'en_US','facility',0),
	(810,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'84009F01EEDA48B7ADC74346B362BC6D','menu','label','HELP Tag',NULL,'en_US','helptag',0),
	(811,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'2010EB44B46E4104A319AF07EDFB1B5D','menu','label','Home',NULL,'en_US','home',0),
	(812,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'5C4079BB760E434AA8EFF4B9BF6132F0','menu','label','Illumina',NULL,'en_US','illumina',0),
	(813,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'EB4BA12F7F3742D2A75C83AB177E4DEE','menu','label','Quotes',NULL,'en_US','jobQuotes',0),
	(814,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'6927FCF1C8324F39A266648FA6CF05D7','menu','label','Jobs',NULL,'en_US','jobs',0),
	(815,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'417531FB320F47BBA07C64DCCF8D533B','menu','label','Join Another Lab',NULL,'en_US','joinAnotherLab',0),
	(816,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'DAE095D52828470D8960C16708C13EBA','menu','label','Labs',NULL,'en_US','labs',0),
	(817,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'D0799E4CFB044BD69C3083810C09BB84','menu','label','Lab PI',NULL,'en_US','labPI',0),
	(818,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'6BB703C68F814E8E9173B85FFFD0BC5B','menu','label','Logout',NULL,'en_US','logout',0),
	(819,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'C8C42E9C95F449759425665AE22D78C6','menu','label','Platform Units',NULL,'en_US','listPlatformUnits',0),
	(820,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'A600E52D24E8419BB50EC4A2425F18F3','menu','label','Machines',NULL,'en_US','machines',0),
	(821,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'42371742D0A64F0F8D2032465B4B647A','menu','label','My Draft Jobs',NULL,'en_US','myJobDrafts',0),
	(822,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'7B259807A4B54295A0D78E2C7F993F71','menu','label','My Profile',NULL,'en_US','myProfile',0),
	(823,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'ED904E00773C42B8B47C55B053B7AFD5','menu','label','My Role In Lab',NULL,'en_US','myRoleInLab',0),
	(824,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'DAF4AFC3FE9C440FA0907A5C6177E04E','menu','label','New Platform Unit',NULL,'en_US','newPlatformUnits',0),
	(825,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'5C774872B51B4F9AB4668B1333FB2916','menu','label','Pending Approval',NULL,'en_US','pendingApproval',0),
	(826,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'4C970B5FCB9840B5BD6FF1919EC9B3DF','menu','label','PI/Manager Tasks',NULL,'en_US','piAndLabManagerTasks',0),
	(827,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'244328FB1EAA4E02AAC6271152087BCD','menu','label','Platform Units',NULL,'en_US','platformUnits',0),
	(828,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'B74F8F46C64D4B0288D366C5DACD4D15','menu','label','Web Plugins',NULL,'en_US','webPlugins',0),
	(829,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'B5761757AB5345B9830FA3C9875449E6','menu','label','Regular Users',NULL,'en_US','regularUsers',0),
	(830,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'716FC4D6820845BFA1C67E5E3FF3C118','menu','label','My Submitted Jobs',NULL,'en_US','mySubmittedJobs',0),
	(831,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'99D84D0BD01943EB8AF1F51B6D346F8C','menu','label','Samples',NULL,'en_US','samples',0),
	(832,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'DE489DFBC7D44C22A5B9348810C2D081','menu','label','Sample Receiver',NULL,'en_US','sampleReceiver',0),
	(833,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'9D51F99D2B754941B0E254BEE22C9808','menu','label','Sequencers',NULL,'en_US','sequencers',0),
	(834,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'C1E37B0655924073AC769C3CA1BB06FE','menu','label','Sequence Runs',NULL,'en_US','sequenceRuns',0),
	(835,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'2FC3885FE7CA43C8B9153C1348115BEF','menu','label','Submit A New Job',NULL,'en_US','submitNewJob',0),
	(836,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'CD5305636BF04217BEEF3759CE7985FC','menu','label','System Users',NULL,'en_US','systemUsers',0),
	(837,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'19E5DFA0F3984CA2B93C9744B9303E7C','menu','label','Tasks',NULL,'en_US','tasks',0),
	(838,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'B8304770A08F421D9F1C945484EB89CC','menu','label','Tasks For Others',NULL,'en_US','tasksForOthers',0),
	(839,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'628056D2ECC84F8089C450E7DC838118','menu','label','Upgrade To Lab PI',NULL,'en_US','upgradeToPI',0),
	(840,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'6AE2B887AAA7470D8E0F757188934942','menu','label','User',NULL,'en_US','user',0),
	(841,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'F8AAC3F58CC9403CB91BB77A97C6B085','menu','label','Users',NULL,'en_US','users',0),
	(842,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'9975FD21A37F49C2BE08EC48132C8893','menu','label','Workflows',NULL,'en_US','workflows',0),
	(843,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'CC826030308D47099D00B01F11407B2F','pageTitle','label','List of Job Quotes',NULL,'en_US','acctquote/list',0),
	(844,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'CDEB3FF6717D42B08112BF339F32E291','pageTitle','label','Access Denied',NULL,'en_US','auth/accessdenied',0),
	(845,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'F20DCF87BD6946E8967D99C590E94C75','pageTitle','label','Confirm Email Address',NULL,'en_US','auth/confirmemail/authcodeform',0),
	(846,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'2AA8BB3ADF53438FA71E225FF26E7F58','pageTitle','label','Unexpected Email Error',NULL,'en_US','auth/confirmemail/confirmemailerror',0),
	(847,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'3A0D120F6A894D9A8C12E2FE9DFDB84A','pageTitle','label','Confirm New Email Address',NULL,'en_US','auth/confirmemail/emailchanged',0),
	(848,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'525461DBDC0A47B194E70E52BDBD2BBB','pageTitle','label','Request to Change Email',NULL,'en_US','auth/confirmemail/requestEmailChange',0),
	(849,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'79D15DE632B4401C9D5EE79D34DD6C58','pageTitle','label','Confirm New User Id',NULL,'en_US','auth/confirmemail/useridchanged',0),
	(850,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'C2215BD52CAB4A44BFBF3A3F3203565C','pageTitle','label','Confirm New Email Address',NULL,'en_US','auth/confirmemail/userloginandemailchanged',0),
	(851,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'DC94C484824D423589C7A4EB6CA818F3','pageTitle','label','User Login Changed',NULL,'en_US','auth/confirmemail/userloginchanged',0),
	(852,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'45ED9DB1C03E46FD88DAB452DC925D88','pageTitle','label','Proceed To Login',NULL,'en_US','auth/directusertologin',0),
	(853,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'5FF3D7F2904440EB901A87D6D8128A5C','pageTitle','label','Login',NULL,'en_US','auth/login',0),
	(854,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'8874CC5C1AD1476B9ACA66E8499ABD23','pageTitle','label','Application Submitted',NULL,'en_US','auth/newpi/created',0),
	(855,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'F04FB8A559154A969E732537550BF5B8','pageTitle','label','Email Confirmed',NULL,'en_US','auth/newpi/emailok',0),
	(856,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'059D2429132B4FD59234333386AAC810','pageTitle','label','New Principal Investigator',NULL,'en_US','auth/newpi/form',0),
	(857,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'D1052D586D684D30B0B44ADA11F66C74','pageTitle','label','New Principal Investigator',NULL,'en_US','auth/newpi/institute',0),
	(858,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'7D3999F398A74EC98A1FF990CB06770E','pageTitle','label','Application Submitted',NULL,'en_US','auth/newuser/created',0),
	(859,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'CDB7995700DC47179389497734D3C224','pageTitle','label','Email Confirmed',NULL,'en_US','auth/newuser/emailok',0),
	(860,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'DA0ADA030B7B47FA9BCE4D124820CFAA','pageTitle','label','New User',NULL,'en_US','auth/newuser/form',0),
	(861,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'26AB377CD1004BE9A09D02640B7565DA','pageTitle','label','Reset Password',NULL,'en_US','auth/resetpassword/authcodeform',0),
	(862,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'91DE7850AA204E80AB182C14283108C7','pageTitle','label','Email Sent',NULL,'en_US','auth/resetpassword/email',0),
	(863,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'E03D883E60FB40BF8E092F396DA9A65D','pageTitle','label','Reset Password',NULL,'en_US','auth/resetpassword/form',0),
	(864,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'3E103C8FF4D444EFADCC184BBC72ECCA','pageTitle','label','Password Was Reset',NULL,'en_US','auth/resetpassword/ok',0),
	(865,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'2C231852D30A4EF09409AB96F42472EB','pageTitle','label','Reset Password Request',NULL,'en_US','auth/resetpassword/request',0),
	(866,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'D8033DD599D44E87B4D8C3BB95EB5EDC','pageTitle','label','Home',NULL,'en_US','dashboard',0),
	(867,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'6E33A9293FB94A2BABAA4B4E1394D6AA','pageTitle','label','Department Administrator Pending Tasks',NULL,'en_US','department/dapendingtasks',0),
	(868,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'647467A25786404EA7DC1229768086CC','pageTitle','label','Department Detail',NULL,'en_US','department/detail',0),
	(869,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'09BD155E019D4BFAB42F448CF301F221','pageTitle','label','Department List',NULL,'en_US','department/list',0),
	(870,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'3125823B3A814A2D8C1DF519CEB37407','pageTitle','label','Assign Library To Platform Unit',NULL,'en_US','facility/platformunit/assign',0),
	(871,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'006AEF053C9C4536B2EFE8A2B42833FC','pageTitle','label','Create/Update Platform Unit',NULL,'en_US','facility/platformunit/createUpdatePlatformUnit',0),
	(872,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'8F7679ACCE644D62864C896A48913FDD','pageTitle','label','Update Platform Unit',NULL,'en_US','facility/platformunit/updatePlatformUnit',0),
	(873,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'E6931643D30140C3A17E00D76DBDB803','pageTitle','label','Platform Unit Instance List',NULL,'en_US','facility/platformunit/instance/list',0),
	(874,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'D4C98F0671184AF59C40CA9C2B37C39E','pageTitle','label','Machine Type Assignment',NULL,'en_US','facility/platformunit/limitPriorToAssign',0),
	(875,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'60B9BE4B0F96410B8C8D2A3666D463D0','pageTitle','label','Platform Unit List',NULL,'en_US','facility/platformunit/list',0),
	(876,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'4EE10CD0F16F4C77A89E8F07041F74DF','pageTitle','label','Platform Unit / Sequence Run',NULL,'en_US','facility/platformunit/showPlatformUnit',0),
	(877,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'A131CC27D13643C6B4EF1770CDECAFF7','pageTitle','label','Job Comments',NULL,'en_US','job/comments',0),
	(878,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'03DECD9050D44841A8BAF1D66B5B7C16','pageTitle','label','Analysis Parameters',NULL,'en_US','job/analysisParameters',0),
	(879,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'7C19205C5166499BA7EB45E328110AD2','pageTitle','label','Job List',NULL,'en_US','job/list',0),
	(880,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'76A041D1209B48559111C5F8ED2F55D0','pageTitle','label','Active Jobs With Libraries To Be Created',NULL,'en_US','job/jobsAwaitingLibraryCreation/jobsAwaitingLibraryCreationList',0),
	(881,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'6051B6A2FA7C41D09BD075DD5A8E7821','pageTitle','label','List of Jobs to Quote',NULL,'en_US','job2quote/list',0),
	(882,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'57E9B4984B5E4849862AB36666D49847','pageTitle','label','Quotes',NULL,'en_US','job2quote/list_all',0),
	(883,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'85466610108F468C98A87E949CF8692E','pageTitle','label','Job Results',NULL,'en_US','jobresults/treeview',0),
	(884,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'2BE0AE294ED041F585668858A93B5DFC','pageTitle','label','Cells',NULL,'en_US','jobsubmit/cell',0),
	(885,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'F670BD49D78A4800BAED52D740D7CBBB','pageTitle','label','New Job',NULL,'en_US','jobsubmit/create',0),
	(886,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'9B9D5153D76B46AEB0FDD760F4D1DDAF','pageTitle','label','Add A Comment',NULL,'en_US','jobsubmit/comment',0),
	(887,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'BE3862BA8EAB48149966A7057D59BAF1','pageTitle','label','Select Genome',NULL,'en_US','jobsubmit/genomes',0),
	(888,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'736AF5E59BB746209AA58ED7EE5660AB','pageTitle','label','List of Job Drafts',NULL,'en_US','jobsubmit/list',0),
	(889,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'3CCE46E7E0794218A46E2BB76E1EE918','pageTitle','label','New Job',NULL,'en_US','jobsubmit/metaform',0),
	(890,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'90FAB5B3354A4D4FB5F31A2A5E910F2C','pageTitle','label','Job Successfully Submitted',NULL,'en_US','jobsubmit/ok',0),
	(891,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'0D12B48FBAE842C0A91E53B1E8946CE6','pageTitle','label','Job Submission Failed',NULL,'en_US','jobsubmit/failed',0),
	(892,'2013-08-05 19:04:17','2013-08-05 19:04:17',X'1FFAF7C06CA84CF3BF85D57FF917FA1D','pageTitle','label','Submit Samples',NULL,'en_US','jobsubmit/sample',0),
	(893,'2013-08-05 19:04:17','2013-08-05 19:04:18',X'DB4014EDF4A64634AFCA3D19D2E07612','pageTitle','label','View Sample Draft',NULL,'en_US','jobsubmit/sample/sampledetail_ro',0),
	(894,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'F1A7484EA53B447388B337C3893EC07F','pageTitle','label','Edit Sample Draft',NULL,'en_US','jobsubmit/sample/sampledetail_rw',0),
	(895,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'444B54D1C3174345A38BBACFF80F207A','pageTitle','label','Verify New Job',NULL,'en_US','jobsubmit/verify',0),
	(896,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'D3941890CDA243639F76D9EDC56D9660','pageTitle','label','PI/Lab Manager Pending Tasks',NULL,'en_US','lab/allpendinglmapproval/list',0),
	(897,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'A61269E6CF3B493CABE3F1F16D9743EE','pageTitle','label','Lab Detail',NULL,'en_US','lab/detail_ro',0),
	(898,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'D206209E61FC42D288804BE37354B120','pageTitle','label','Update Lab Detail',NULL,'en_US','lab/detail_rw',0),
	(899,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'F5C54881C05D4667A9D95A2FF3371395','pageTitle','label','Join Another Lab',NULL,'en_US','lab/joinAnotherLab/form',0),
	(900,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'F16F7EF56FBC4EE183B232F28C1E2A29','pageTitle','label','Lab List',NULL,'en_US','lab/list',0),
	(901,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'FA5B7105EC884654B4B7B4F77657A9A0','pageTitle','label','Request Access to Lab',NULL,'en_US','lab/newrequest',0),
	(902,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'F1C141272642431EBA8DCEFCC51D0AF0','pageTitle','label','Pending Lab Detail',NULL,'en_US','lab/pending/detail_ro',0),
	(903,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'5C7938B857EF4218A6736F13DBE4740A','pageTitle','label','Update Pending Lab Detail',NULL,'en_US','lab/pending/detail_rw',0),
	(904,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'4EE88D31A88E4B5CA55ADC3539A1878C','pageTitle','label','Pending Users',NULL,'en_US','lab/pendinguser/list',0),
	(905,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'F709BBA528B040499722CA7881003487','pageTitle','label','Upgrade Status To Principal Investigator',NULL,'en_US','lab/upgradeStatusToPI/form',0),
	(906,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'52E94307D2554EBC816F14FEB18A01CF','pageTitle','label','Lab Member List',NULL,'en_US','lab/user_list',0),
	(907,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'48CBB75517314CD1B03275CD7DE09977','pageTitle','label','Lab User Manager',NULL,'en_US','lab/user_manager',0),
	(908,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'2C498C93928F43BAB2FD7D38E0F43AF8','pageTitle','label','My Labs',NULL,'en_US','lab/viewerLabList',0),
	(909,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'EAF95DE1AA7A46029E4059ACA00F8175','pageTitle','label','Web Enabled Plugins',NULL,'en_US','plugin/list',0),
	(910,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'257BD2465F5C4F4C875FF01EF6BD4EBB','pageTitle','label','Create New Resource',NULL,'en_US','resource/create',0),
	(911,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'6B400E1CAF654560B717FF3FE1DC91D9','pageTitle','label','Resource Detail',NULL,'en_US','resource/detail_ro',0),
	(912,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'A345867DB4C3431D9EDCA9646ECEE512','pageTitle','label','Update Resource Detail',NULL,'en_US','resource/detail_rw',0),
	(913,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'60E4C05C596848A793585A68E4A200AD','pageTitle','label','Machine List',NULL,'en_US','resource/list',0),
	(914,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'B3966FB00CCF431D88F04F11B6ACEC8E','pageTitle','label','Run Detail',NULL,'en_US','run/detail',0),
	(915,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'B3D5A5E7B3E246AF9D98C7A80D951B1A','pageTitle','label','Run List',NULL,'en_US','run/list',0),
	(916,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'75CD46A127564059AF882302E6CF90BF','pageTitle','label','Sample Utilities',NULL,'en_US','sample/list',0),
	(917,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'26AE69BBDEBC4313B39BEB8F935B6839','pageTitle','label','Control Libraries',NULL,'en_US','sample/controlLibraries/list',0),
	(918,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'BB4B0ECB7CBF4D8FB3A1C169993A733D','pageTitle','label','Create/Update Control Libraries',NULL,'en_US','sample/controlLibraries/createUpdate',0),
	(919,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'97AEBB7717EC438FB8E7E3EAA979C083','pageTitle','label','New Library',NULL,'en_US','sampleDnaToLibrary/createLibrary',0),
	(920,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'5CE2325D4E1E48AD9207A3201F8AF97A','pageTitle','label','Library Details',NULL,'en_US','sampleDnaToLibrary/librarydetail_ro',0),
	(921,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'510AC5EAD7FA452CA1FD2B63CE85AC04','pageTitle','label','Update Library',NULL,'en_US','sampleDnaToLibrary/librarydetail_rw',0),
	(922,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'1DCD5DB1D8644DB2A156DF750985A606','pageTitle','label','Samples And Libraries',NULL,'en_US','sampleDnaToLibrary/listJobSamples',0),
	(923,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'62AE871473CA47818E111EB76E624AB2','pageTitle','label','Sample Details',NULL,'en_US','sampleDnaToLibrary/sampledetail_ro',0),
	(924,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'0787CFEB23C94EDE8B24071113BAC01E','pageTitle','label','Update Sample Details',NULL,'en_US','sampleDnaToLibrary/sampledetail_rw',0),
	(925,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'8FB12112A6D2425881940C23308DFAC5','pageTitle','label','System User Management',NULL,'en_US','sysrole/list',0),
	(926,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'E7B66DE44A19467DAEAC9109B8D54B4A','pageTitle','label','Assign Libraries',NULL,'en_US','task/assignLibraries/lists',0),
	(927,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'0B474E5DFD194ED1BE2FD6066846D076','pageTitle','label','Cell-Library QC Manager',NULL,'en_US','task/cellLibraryQC/list',0),
	(928,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'136DCD60BD954FD892B656281C3691E1','pageTitle','label','Aggregation Analysis Initiation',NULL,'en_US','task/aggregationAnalysis/list',0),
	(929,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'9174F6E6DB5846D0AF1EC0B39083EC28','pageTitle','label','Department Administrator Pending Tasks',NULL,'en_US','task/daapprove/list',0),
	(930,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'7761840572D54A96943B57CD58ADE0B0','pageTitle','label','Requote Pending Jobs',NULL,'en_US','task/detail',0),
	(931,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'7CEE5A96C9D446AD84BA6BEAE4C092B6','pageTitle','label','Facility Manager Pending Tasks',NULL,'en_US','task/fmapprove/list',0),
	(932,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'AE86B028DF644EC1BCA8E66A96374643','pageTitle','label','Receive Payment for Jobs',NULL,'en_US','task/fmpayment/list',0),
	(933,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'26CEBED1D07B4B2D99FB6C591EE18162','pageTitle','label','Requote Pending Jobs',NULL,'en_US','task/fmrequote/list',0),
	(934,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'CA15451D52614E118C6376FA2E9775DF','pageTitle','label','Library QC Manager',NULL,'en_US','task/libraryqc/list',0),
	(935,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'7D4CE11588024412BDB5E06FC1562BA3','pageTitle','label','Tasks',NULL,'en_US','task/myTaskList',0),
	(936,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'0C701C523DD048C0AE81F9BBC97D927C','pageTitle','label','PI/Lab Manager Pending Tasks',NULL,'en_US','task/piapprove/list',0),
	(937,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'C217C4E9A06046FA8EEF0619CEF68B12','pageTitle','label','Incoming Sample Manager',NULL,'en_US','task/samplereceive/list',0),
	(938,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'90E812FC17B74EF2A62A3DD6CBCD7618','pageTitle','label','Sample QC Manager',NULL,'en_US','task/sampleqc/list',0),
	(939,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'4CF3CE2C44F945859A6031948AC50826','pageTitle','label','Properties',NULL,'en_US','uifield/list',0),
	(940,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'5D17CDCEA3364F9F87DFB86C0039AECD','pageTitle','label','User Detail',NULL,'en_US','user/detail_ro',0),
	(941,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'C6548806FF634C0285229142487B511F','pageTitle','label','Update User Detail',NULL,'en_US','user/detail_rw',0),
	(942,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'55882819BBDA4E5E8F6335A6E5E3FF3F','pageTitle','label','User List',NULL,'en_US','user/list',0),
	(943,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'5A6573E33BA74598A5FB2EA8FD7AF844','pageTitle','label','Change Password',NULL,'en_US','user/mypassword',0),
	(944,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'E2350CF5230549B892BC7BCBDCAE5FD0','pageTitle','label','Workflow List',NULL,'en_US','workflow/list',0),
	(945,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'C1875CF58B774BAAAEC780F3022BE323','pageTitle','label','Workflow Resource Assignment',NULL,'en_US','workflow/resource/configure',0),
	(946,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'958AAB981E3B4CACA4815AB9594DAF29','pendingJob','label','Approve',NULL,'en_US','detailRO_approve',0),
	(947,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'009EB8651E3D452383B0AB644B3A390A','pendingJob','label','Job',NULL,'en_US','detailRO_job',0),
	(948,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'6F6D88C38B394B4D9D8AF227B698E014','pendingJob','label','PI',NULL,'en_US','detailRO_pi',0),
	(949,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'6DBA2BD7689543E1A11E5FC6454C44E6','pendingJob','label','Reject',NULL,'en_US','detailRO_reject',0),
	(950,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'9FF4727424634531A08AC5C51CA6C343','pendingJob','label','Submitting User',NULL,'en_US','detailRO_submittingUser',0),
	(951,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'2DF6A720081545E2BBE4B7A4762DCB97','piPending','label','Address',NULL,'en_US','address',0),
	(952,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'832EE8BCF9A0419A9F2B8C9001EC366A','piPending','error','Address cannot be empty',NULL,'en_US','address',0),
	(953,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'452181F8812A417AA09B51335D4B1ADC','piPending','constraint','NotEmpty',NULL,'en_US','address',0),
	(954,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'3F54EF5D57A448949DAD0A61D3198C18','piPending','metaposition','40',NULL,'en_US','address',0),
	(955,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'DF15138AA06C45569004DD66C922F12C','piPending','label','Building / Room',NULL,'en_US','building_room',0),
	(956,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'557CA2B5E19A410DB1DBFB624C800E90','piPending','metaposition','30',NULL,'en_US','building_room',0),
	(957,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'8F2C47CF5264418A9430F02AE64AC563','piPending','error','Captcha text incorrect',NULL,'en_US','captcha',0),
	(958,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'38B0F532D8374D9D8E6021C468291A0F','piPending','label','Captcha text',NULL,'en_US','captcha',0),
	(959,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'D38BABAAB19A433A9111C670F61DBF7C','piPending','label','City',NULL,'en_US','city',0),
	(960,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'71CD0838A6C24A5B8942E99341BD2A45','piPending','error','City cannot be empty',NULL,'en_US','city',0),
	(961,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'1C79D348BE624C53B43B441151395EE6','piPending','constraint','NotEmpty',NULL,'en_US','city',0),
	(962,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'000A378C4A4646379728CF191FCE0475','piPending','metaposition','50',NULL,'en_US','city',0),
	(963,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'C0F1E5D31E2C4C3E9E1E3AEAED6DE8F8','piPending','label','Country',NULL,'en_US','country',0),
	(964,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'10631ADBD0FB4A62BA02DBE8DAE87E3A','piPending','error','Country cannot be empty',NULL,'en_US','country',0),
	(965,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'263F28C239EC48598E9E16DF3738266C','piPending','control','select:${countries}:code:name',NULL,'en_US','country',0),
	(966,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'1668C9E15CDC409396769DEFA64C12F8','piPending','constraint','NotEmpty',NULL,'en_US','country',0),
	(967,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'F1199B8E4D8A48109CF9CF2B487EF26C','piPending','metaposition','70',NULL,'en_US','country',0),
	(968,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'9D87766ABB3B46C6828DFD7DF92FCF84','piPending','error','Department cannot be empty',NULL,'en_US','departmentId',0),
	(969,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'15169DDC55CB4D8A9101017FD3E8460D','piPending','label','Department',NULL,'en_US','departmentId',0),
	(970,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'114EF94184BF46D7A0D2E162A50E19E5','piPending','constraint','NotEmpty',NULL,'en_US','departmentId',0),
	(971,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'E1BBE0F9AF9D43DD9AA2B32FA34AC04B','piPending','control','select:${departments}:departmentId:name',NULL,'en_US','departmentId',0),
	(972,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'D1E18380B02E4D5580A3542893CA1585','piPending','metaposition','20',NULL,'en_US','departmentId',0),
	(973,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'A4D08A2A16514CB4A8334C3801052F07','piPending','label','Email',NULL,'en_US','email',0),
	(974,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'F6849CCF6028452BA27FAE40567537F8','piPending','error','Wrong email address format',NULL,'en_US','email',0),
	(975,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'E5246AA7C24746F0B35A149D47A4BF26','piPending','label','Your email address is confirmed. A WASP departmental administrator has been requested (via email) to approve your application as a new Principal Investigator. Once approved, you will be informed via email and you will be permitted to log in. ',NULL,'en_US','emailconfirmed',0),
	(976,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'BD09C7C52BBD4078828A14936A93D089','piPending','label','Thank you for applying for a new Principal Investigator account. You have been sent an email with instructions describing how to quickly confirm your email address. Your registration cannot proceed until your email address has been confirmed.',NULL,'en_US','emailsent',0),
	(977,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'7109737BC7E04C53959F3D66AE18A9B2','piPending','error','Email already exists in the database',NULL,'en_US','email_exists',0),
	(978,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'BAA7E8E294F3457C8EDD72BB9DE49C65','piPending','label','Fax',NULL,'en_US','fax',0),
	(979,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'79F1DA52E8DC47EA811AFCBDD795B986','piPending','metaposition','100',NULL,'en_US','fax',0),
	(980,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'AA1B8BB613C64268A030C582BD2A1610','piPending','label','First Name',NULL,'en_US','firstName',0),
	(981,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'C71955675D7B483F9D22C0122A3BD659','piPending','error','First Name cannot be empty',NULL,'en_US','firstName',0),
	(982,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'19A2FA4E9C3846A69832C367DBB11968','piPending','label','Register As A New WASP Principal Investigator',NULL,'en_US','form_header',0),
	(983,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'D941BBA8DEE04633B7EB0856B9923E7C','piPending','label','If you do NOT already have a WASP account and you want to register as a new Principal Investigator, fill out the form below.',NULL,'en_US','form_instructions',0),
	(984,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'44D76141E00A450BBD717990CB6970EC','piPending','error','You cannot select more than one institute',NULL,'en_US','institute_multi_select',0),
	(985,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'5F9C1FDD598C4B8EBDFC8A104870ED24','piPending','error','You must select an institute',NULL,'en_US','institute_not_selected',0),
	(986,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'E1854AEB3A324FAD9B7DBC8E826C8B7F','piPending','label','Institution',NULL,'en_US','institution',0),
	(987,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'7609949A24834B0292BF2886A8741D90','piPending','error','Institution cannot be empty',NULL,'en_US','institution',0),
	(988,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'001128E9384E4BF185753E0E5F4BF33B','piPending','constraint','NotEmpty',NULL,'en_US','institution',0),
	(989,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'3B2EDC1CBE114843AF842DD89D4AFC99','piPending','metaposition','10',NULL,'en_US','institution',0),
	(990,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'A117504FDC8444A2B9E4C4573F59473A','piPending','data','Einstein;Montifiore',NULL,'en_US','internal_institute_list',0),
	(991,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'D0EE6C873B7648B2A6FADB266861B81C','piPending','label','Lab Name',NULL,'en_US','labName',0),
	(992,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'11B04BD2DB0D45B48ED33D48D501EBBB','piPending','error','Lab Name cannot be empty',NULL,'en_US','labName',0),
	(993,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'86C53F5A376C42E89DD53EA3957EE728','piPending','constraint','NotEmpty',NULL,'en_US','labName',0),
	(994,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'30EBDA825CA44C09AA0DEA821C709833','piPending','metaposition','1',NULL,'en_US','labName',0),
	(995,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'967328150B1A462E9BCE0BF243695970','piPending','label','Last Name',NULL,'en_US','lastName',0),
	(996,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'60EDB4865F494FB8802B3DE9977F26F2','piPending','error','Last Name cannot be empty',NULL,'en_US','lastName',0),
	(997,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'F5579DBFD89D498585F39C2E6E0B6C8D','piPending','label','Preferred Language',NULL,'en_US','locale',0),
	(998,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'F565730090954AAEA9F2EF36698685C9','piPending','constraint','NotEmpty',NULL,'en_US','locale',0),
	(999,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'2640FE6391D242A6A0509F41915D3C47','piPending','error','Language cannot be empty',NULL,'en_US','locale',0),
	(1000,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'02279D787DC04F3B85E9BB8C1696256A','piPending','label','Login',NULL,'en_US','login',0),
	(1001,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'ED58077A8D2F4ADDB102974B13B4CB19','piPending','error','Login cannot be empty',NULL,'en_US','login',0),
	(1002,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'BCF8C93B048B42CD912919FE2C284F2C','piPending','label','Password',NULL,'en_US','password',0),
	(1003,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'244A676D25CA41BD90CEBA9BFA1327A4','piPending','label','Password Requirements: Minimum of 8 characters, letters &amp; numbers only (no spaces, no underscore, etc), at least one letter and one number',NULL,'en_US','password_instructions',0),
	(1004,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'5529DD34C2494553B0E0EA03970837FB','piPending','label','See password requirements above',NULL,'en_US','password_instructions_above',0),
	(1005,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'22C7286147C544C98B72919EF870DD49','piPending','error','Password cannot be empty',NULL,'en_US','password',0),
	(1006,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'9EC21317D4534370BE14CED3078F0E0E','piPending','label','Re-confirm Password',NULL,'en_US','password2',0),
	(1007,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'409A13F186F24934AEBC13A2BEF34397','piPending','error','Re-confirm password cannot be empty',NULL,'en_US','password2',0),
	(1008,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'AC256EA7A0B643E2B5B6C1B770E2E0BD','piPending','error','Password must be at least 8 characters, containing only letters and numbers, with at least one letter and number',NULL,'en_US','password_invalid',0),
	(1009,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'419966D60C3841F69AECDD9185B0B70F','piPending','error','The two entries for your password are NOT identical',NULL,'en_US','password_mismatch',0),
	(1010,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'7EB12728DE8944B4BFF64AC8568BFDFA','piPending','label','Phone',NULL,'en_US','phone',0),
	(1011,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'E2C54421CE22482A84801A9C85245D44','piPending','error','Phone cannot be empty',NULL,'en_US','phone',0),
	(1012,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'5B783FFEB644408A9648E5AE942B8979','piPending','constraint','NotEmpty',NULL,'en_US','phone',0),
	(1013,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'3525756227544427A68EA3D4FFDC6104','piPending','metaposition','90',NULL,'en_US','phone',0),
	(1014,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'EF7F6AA7A43C456F972EFDE69488D53C','piPending','label','-- select --',NULL,'en_US','select_default',0),
	(1015,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'C3ED17ACF4B840488F24CB28AFD2F7A1','piPending','label','Institute',NULL,'en_US','select_institute',0),
	(1016,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'655F7048B25B4140AA859F4A81C3D4A6','piPending','label','If you do NOT have a WASP account and you want to register as a new Principal Investigator, please select your institute or select \"other\" and type your institute name',NULL,'en_US','select_institute_message',0),
	(1017,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'BF156257329A48A19D7B2EF35B31C13E','piPending','label','Other',NULL,'en_US','select_institute_other',0),
	(1018,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'48CE1CE3C6324A73B30E3187C7DB6068','piPending','label','Submit',NULL,'en_US','select_institute_submit',0),
	(1019,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'6554756742C74CC7B1F2CF86FC3FC013','piPending','label','If \"Other\" Please specify',NULL,'en_US','specify_other_institute',0),
	(1020,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'47971669485D4BAD92D110F72A9271DE','piPending','label','State',NULL,'en_US','state',0),
	(1021,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'14FDFA9BD6614DE58DEFB55FC3CCCE72','piPending','control','select:${states}:code:name',NULL,'en_US','state',0),
	(1022,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'CF574BB377804B82A587A26805A6FCD3','piPending','metaposition','60',NULL,'en_US','state',0),
	(1023,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'48B13A66F5014BDFA6ECBA1CCE7B08BA','piPending','label','Apply for Account',NULL,'en_US','submit',0),
	(1024,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'DC54FCA1C0074205A72C2F1EC999708E','piPending','label','Title',NULL,'en_US','title',0),
	(1025,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'3EB475837004449F90EC10799A1F9AC0','piPending','error','Title cannot be empty',NULL,'en_US','title',0),
	(1026,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'5FB79B4120954849BB26AF41286E4A05','piPending','constraint','NotEmpty',NULL,'en_US','title',0),
	(1027,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'BA8D91A93AF64572B109965FA68BEE5F','piPending','control','select:Prof:Prof;Dr:Dr;Mr:Mr;Ms:Ms;Mrs:Mrs;Miss:Miss',NULL,'en_US','title',0),
	(1028,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'4B240897762F453898CA1DB93FF15D3D','piPending','metaposition','5',NULL,'en_US','title',0),
	(1029,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'C03025171D7C4098BB69A2758050E807','piPending','label','Zip',NULL,'en_US','zip',0),
	(1030,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'19F2B8E527364574B040768658F56533','piPending','error','Zip cannot be empty',NULL,'en_US','zip',0),
	(1031,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'BBDFC447F62D46BA91E026F764E04817','piPending','constraint','NotEmpty',NULL,'en_US','zip',0),
	(1032,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'F3EC98CF7FE84C57AA017D0F87D6BD87','piPending','metaposition','80',NULL,'en_US','zip',0),
	(1033,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'89C391CEB6A944F299076B32EA918A69','platformunit_assign','label','Adaptor',NULL,'en_US','adaptor',0),
	(1034,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'77E36B41EEC0449F87C862EA74BFA3FF','platformunit_assign','label','Analysis',NULL,'en_US','analysis',0),
	(1035,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'CE58E8C2E9EA446CB429D8F33A81829D','platformunit_assign','label','Assign Libraries For A Run On',NULL,'en_US','assignLibToRun',0),
	(1036,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'904B022FEB18403F88A455555F7E058F','platformunit_assign','label','[Add To Cell]',NULL,'en_US','addToCell',0),
	(1037,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'867B9B9B6299476A803A735C41DC39DE','platformunit_assign','label','[Close]',NULL,'en_US','close',0),
	(1038,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'8571244948F44F52A41E0874AB02FFCE','platformunit_assign','label','Final Conc.',NULL,'en_US','finalConc',0),
	(1039,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'DCE3FC3784ED42D5A72DB5466541B313','platformunit_assign','label','Job',NULL,'en_US','job',0),
	(1040,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'7B9DB7C0CACF43E58C7BE2FC95316A7A','platformunit_assign','label','Job Cell',NULL,'en_US','jobCell',0),
	(1041,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'92DA421D2D0A4050B059A86ED9A46EE2','platformunit_assign','label','Cell',NULL,'en_US','cell',0),
	(1042,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'487D3F5CFE2144689F6E04299280A53E','platformunit_assign','label','Library',NULL,'en_US','library',0),
	(1043,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'0B16270452674D4888E8636323BEAA19','platformunit_assign','label','No Libraries Waiting For',NULL,'en_US','noLibWaitingFor',0),
	(1044,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'3F5FADD3198F4E948224214D584E99A7','platformunit_assign','label','No Platform Units Waiting For',NULL,'en_US','noPUWaitingFor',0),
	(1045,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'A0DFC8E412634F72AC31F06FB309EEDB','platformunit_assign','label','Platform Unit',NULL,'en_US','platformUnit',0),
	(1046,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'2ABDCA10C9834D1F9D86657BF67EB4A8','platformunit_assign','label','[Remove From Cell]',NULL,'en_US','removeFromCell',0),
	(1047,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'8AD5B1D2C5664AED97CD5CAEA73FC7EC','platformunit_assign','label','Sample Cell',NULL,'en_US','sampleCell',0),
	(1048,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'75356460E7C649F6920582B0E204E8B8','platformunit_assign','label','[Show Requested Coverage]',NULL,'en_US','showRequestedCoverage',0),
	(1049,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'FA4D0EA485674A0789F7BE8120E4E761','platformunit_assign','label','Status',NULL,'en_US','recievedStatus',0),
	(1050,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'CB248DFBD3E14F55BBA2EA1AF6B67F40','platformunit_assign','label','QC Status',NULL,'en_US','qcStatus',0),
	(1051,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'6036778053674AEF8A548F0187B73C76','platformunit_assign','label','pM',NULL,'en_US','theConcUnits',0),
	(1052,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'D3BC16DBCC69445E9ABCA042F8B81048','platformunit_assign','label','User-submitted',NULL,'en_US','userSubmitted',0),
	(1053,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'A0B76A7F17F6430E8AFACD7F82D62B86','platformunit','error','Action Failed. Libary adaptor barcode NOT found',NULL,'en_US','adaptorBarcodeNotFound',0),
	(1054,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'BC4813DC07AA4A29BAA2B10428ED265F','platformunit','error','Action Failed. Libary adaptor NOT found',NULL,'en_US','adaptorNotFound',0),
	(1055,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'DC3DE0D4E76045599B4B53A392756CCA','platformunit','label','Barcode',NULL,'en_US','barcode',0),
	(1056,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'829578F795F2488EB895388200EC9959','platformunit','label','Created',NULL,'en_US','date',0),
	(1057,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'4A91D3CCD4BC4F1DA412E8B04DB31E7A','platformunit','error','Action Failed. Platform unit record not found or record is not a platform unit',NULL,'en_US','notFoundOrNotCorrectType',0),
	(1058,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'E0EDCECAEFCF4047989B55BAFA3A210D','platformunit','error','Action Failed. Platform Unit record not found or not unique',NULL,'en_US','flowcellNotFoundNotUnique',0),
	(1059,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'F6541F4F0FF6460D9BD881B1DEE936F1','platformunit','error','Action Failed. Platform Unit state not compatible with adding libraries',NULL,'en_US','flowcellStateError',0),
	(1060,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'4B25584597E242A0A830DCC7D28EBE04','platformunit','error','Action Failed. Value for jobId is unexpectedly NOT valid',NULL,'en_US','jobIdNotFound',0),
	(1061,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'E6BDE0C1209E4BBB8932E60A4755D7E4','platformunit','error','Select A Job',NULL,'en_US','jobIdNotSelected',0),
	(1062,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'3F79977F40F342F9921736A429D072D9','platformunit','error','Selected Job Not Found',NULL,'en_US','jobNotFound',0),
	(1063,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'3455F12C69734550847ED3E408AE440F','platformunit','error','Job and resource unexpectedly do not match',NULL,'en_US','jobResourceCategoryMismatch',0),
	(1064,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'0832992E5CF14AA79CED4626700876AF','platformunit','label','Cells',NULL,'en_US','cellcount',0),
	(1065,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'45099025C1D344959AB2F54F1F5766A4','platformunit','error','Action Failed. Value for cell is unexpectedly NOT valid',NULL,'en_US','cellIdNotFound',0),
	(1066,'2013-08-05 19:04:18','2013-08-05 19:04:18',X'A406BF3D5D90437E95B3422AA787DBF0','platformunit','error','Action Failed. You mistakenly selected a Platform Unit',NULL,'en_US','cellIsFlowCell',0),
	(1067,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'39238798482242D2A33D6CC158E0F8BB','platformunit','error','Action Failed. Cell selected is unexpectedly NOT a Cell',NULL,'en_US','cellIsNotCell',0),
	(1068,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'BD2838A59FDD4EDCAC4311BFD2630678','platformunit','success','Update Complete. Library added to platform unit.',NULL,'en_US','libAdded',0),
	(1069,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'53AD7978E6CE48E6BAB4E39F8D5F2317','platformunit','error','Unable to add library to platform unit',NULL,'en_US','libAdded',0),
	(1070,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'46071A25A417456FABED9B6FCE265A9A','platformunit','error','Unable to update library concentration',NULL,'en_US','libUpdated',0),
	(1071,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'EF2891F0888F4878803674189AE2C305','platformunit','error','Action Failed. Libraries on a cell must be from a single job',NULL,'en_US','libJobClash',0),
	(1072,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'9EE4882EE23E481DA19A36CF8FC16067','platformunit','error','Action Failed. Value for libraryId is unexpectedly NOT valid',NULL,'en_US','libraryIdNotFound',0),
	(1073,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'A2400FE449D1456C82F7ACE5CCB9D9E8','platformunit','error','Action Failed. Library selected is unexpectedly NOT a Library',NULL,'en_US','libraryIsNotLibrary',0),
	(1074,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'4A78BDF813DF4EBDB68B855BF96C1E95','platformunit','error','Action Failed. Libary does NOT appear to be part of the specified Job',NULL,'en_US','libraryJobMismatch',0),
	(1075,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'420F32C7B97247FE9FBDA048DBFF51A9','platformunit','label','Selected Library Removed From Platform Unit',NULL,'en_US','libraryRemoved_success',0),
	(1076,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'579449F1C33749E1B97C7B500B07D191','platformunit','error','Invalid lock status selected',NULL,'en_US','lock_status',0),
	(1077,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'B6450288AA2B4E2A85DC5E61685373E3','platformunit','error','Problem occurred locking platform unit',NULL,'en_US','locking',0),
	(1078,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'91D0539B073A414DB650B08CA46EA5F1','platformunit','error','Action Failed. Index on the library is not compatible with other libraries on the cell',NULL,'en_US','multiplex',0),
	(1079,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'AC492C7AF2E842AE892E5ADF735C1DB3','platformunit','label','Name',NULL,'en_US','name',0),
	(1080,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'E608E8BB46904D498C0743476789E110','platformunit','label','Platform Unit List',NULL,'en_US','platformunit_list',0),
	(1081,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'040B577061F7402EB2C2E4056DA59D42','platformunit','error','Action Failed. Value for pmol is NOT valid',NULL,'en_US','pmoleAddedInvalidValue',0),
	(1082,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'548BDA9982714588A3CF77AA48F59D2C','platformunit','label','Read Length',NULL,'en_US','readLength',0),
	(1083,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'480BFE6E05584E0DA8A3D544D8333D95','platformunit','label','Read Type',NULL,'en_US','readType',0),
	(1084,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'42FFBD7AEB8A457E8B3FB563F5AC8060','platformunit','label','Machine Type',NULL,'en_US','resourceCategoryName',0),
	(1085,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'B6C0A12FA5BD42EB9E32B0EAF5639113','platformunit','error','Resource Not Found',NULL,'en_US','resourceCategoryInvalidValue',0),
	(1086,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'D44C7D02FFC44DDE95072AE9C7A0D7ED','platformunit','error','Parameter error occurred',NULL,'en_US','parameter',0),
	(1087,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'0294C00916244729AA00A33465A53DFE','platformunit','error','Type Resource unexpectedly not found',NULL,'en_US','resourceTypeNotFound',0),
	(1088,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'133D0E6C02314EA0B47407B7FE2A87E5','platformunit','error','Action Failed: Selected Library Not Found',NULL,'en_US','sampleSourceNotExist',0),
	(1089,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'63436103AF1D43FFA8D7B920555C48F9','platformunit','error','Action Failed: Selected samplesource missing cell or library',NULL,'en_US','samplesourceTypeError',0),
	(1090,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'24174B475B134C648F9136875720E8A9','platformunit','error','Action Failed. Cell or Library type exception raised',NULL,'en_US','sampleType',0),
	(1091,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'FADF927D7D8E47EB95AE51A892F4FA4E','platformunit','label','Subtype',NULL,'en_US','sampleSubtypeName',0),
	(1092,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'12C9D811986E4853A91293D5734A239E','platformunit','label','Subtype Sample List',NULL,'en_US','subtype_list',0),
	(1093,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'7ECF955BF4624A86ADEF9A85F43C7343','platformunit','error','Task unexpectedly not found',NULL,'en_US','taskNotFound',0),
	(1094,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'89B52ADE63274D8891D0FD66C82A3F48','platformunit','success','TESTING.',NULL,'en_US','TESTING',0),
	(1095,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'12330A29CF6A492DB9E696BFC30F879C','platformunitById','label','Name',NULL,'en_US','name',0),
	(1096,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'1B5CB90D227E43829604682398A49D91','platformunitById','label','Platform Unit List',NULL,'en_US','platformunitbyid_list',0),
	(1097,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'235FE3B1B45248048ED753B2A26A9868','platformunitById','label','Submitter',NULL,'en_US','submitter',0),
	(1098,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'A9B120B31E5E4181819598D08BA252BE','platformunitInstance','label','Barcode',NULL,'en_US','barcode',0),
	(1099,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'E40B5C0D9D4743E99527C717C43A65B2','platformunitInstance','constraint','NotEmpty',NULL,'en_US','barcode',0),
	(1100,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'BCEBF318D8FA45708BDBC59C8513A56B','platformunitInstance','error','Barcode cannot be empty',NULL,'en_US','barcode',0),
	(1101,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'F08B6E11F9264C5FA2A2C5D950D8727C','platformunitInstance','error','Barcode already exists in the database',NULL,'en_US','barcode_exists',0),
	(1102,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'B99299E8353F45D49F5454D5271E658F','platformunitInstance','label','Cancel',NULL,'en_US','cancel',0),
	(1103,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'A12FFD22E39D4157B67035277E961CAF','platformunitInstance','label','Comment',NULL,'en_US','comment',0),
	(1104,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'0FA00E8013B54ABC9F30AD46696F450A','platformunitInstance','error','Comment',NULL,'en_US','comment',0),
	(1105,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'8089A49C39624907935A7106157EACF7','platformunitInstance','metaposition','60',NULL,'en_US','comment',0),
	(1106,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'CD40826F715A4C77AAA1C702B50FE1E9','platformunitInstance','error','Platformunit was NOT created. Please fill in required fields.',NULL,'en_US','created',0),
	(1107,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'0D84078F4660459BB756EF122C75C2C9','platformunitInstance','label','Platformunit created successfully.',NULL,'en_US','created_success',0),
	(1108,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'C3B5237561294B098819CD94971B6F86','platformunitInstance','label','Platformunit deleted successfully.',NULL,'en_US','deleted_success',0),
	(1109,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'7BC47F00E8CA4B3DBB03B00570A9DE61','platformunitInstance','label','Create New Platform Unit',NULL,'en_US','headerCreate',0),
	(1110,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'A95CECB9391741CFBF6B90A946678CF0','platformunitInstance','label','Update Platform Unit',NULL,'en_US','headerUpdate',0),
	(1111,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'46653EAB91B442D3B4357CFD14E309D1','platformunitInstance','label','Cell Count',NULL,'en_US','cellcount',0),
	(1112,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'A62561B85FEA40629FF6196CA929A273','platformunitInstance','constraint','NotEmpty',NULL,'en_US','cellcount',0),
	(1113,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'2F5715C0AEE445E9978FB53F2940759B','platformunitInstance','type','INTEGER',NULL,'en_US','cellcount',0),
	(1114,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'3DA942ECEF064A0199415A81F71B6B50','platformunitInstance','range','1:1000',NULL,'en_US','cellcount',0),
	(1115,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'FB42FCFE029A4B658A6AFAB9977C135B','platformunitInstance','error','Cell Count cannot be empty',NULL,'en_US','cellcount',0),
	(1116,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'61CAA331792C4CB89358B0A2CDD5B925','platformunitInstance','error','Cell Count value unexpectedly cannot be determined',NULL,'en_US','cellcount_notfound',0),
	(1117,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'098F2011CF86493792F67769FF99B176','platformunitInstance','error','Cell Count value not valid',NULL,'en_US','cellcount_valueinvalid',0),
	(1118,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'202E868168C24F53A706C5EEEC648AEC','platformunitInstance','error','Action not permitted at this time. To reduce the number of cells, remove libraries on the cells that will be lost.',NULL,'en_US','cellcount_valuealteredconflicting',0),
	(1119,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'ECD13912F0354BD19B4BC67EFC54EC3A','platformunitInstance','control','select:${cells}:value:label',NULL,'en_US','cellcount',0),
	(1120,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'36B9C6C1488142BA9F151DE7F8E7FD21','platformunitInstance','label','Select Platform Unit Type',NULL,'en_US','platUnitType',0),
	(1121,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'710E2D7464284285A7E1BD4E17C39DE0','platformunitInstance','label','Machine Type',NULL,'en_US','resourceCategoryName',0),
	(1122,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'870CF4F0A7214537A0FBC679876A6FBF','platformunitInstance','label','Read Type',NULL,'en_US','readType',0),
	(1123,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'646448F480704F68859F9E3CAF8835DD','platformunitInstance','constraint','NotEmpty',NULL,'en_US','readType',0),
	(1124,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'9C7ECF2BBA354C51B6BA18AAD9687287','platformunitInstance','error','Read Type cannot be empty',NULL,'en_US','readType',0),
	(1125,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'EB7E38E7E1A24EDFA302507D226AA296','platformunitInstance','control','select:${readTypes}:value:label',NULL,'en_US','readType',0),
	(1126,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'B62D4E5752924AF785A25B25264FD1C8','platformunitInstance','metaposition','10',NULL,'en_US','readType',0),
	(1127,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'7346C2568B564538ABD2B66EC856B283','platformunitInstance','label','Read Length',NULL,'en_US','readLength',0),
	(1128,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'8B28752AB8EF40D4AB7695B62E2AECDE','platformunitInstance','constraint','NotEmpty',NULL,'en_US','readLength',0),
	(1129,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'2ED7B9A2159B40C3BA670845883D0CB8','platformunitInstance','error','Read Length cannot be empty',NULL,'en_US','readLength',0),
	(1130,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'023ECB661CFD44B196AB7700A484F6F3','platformunitInstance','control','select:${readLengths}:value:label',NULL,'en_US','readLength',0),
	(1131,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'BF8804434F75445A9EB4379B14D6B9B8','platformunitInstance','metaposition','15',NULL,'en_US','readLength',0),
	(1132,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'58B3A90269D3417DA74FF9918B2E397C','platformunitInstance','label','Cell Count',NULL,'en_US','cellcountForEditBox',0),
	(1133,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'A0E02210600D40688A09715241C8D9D3','platformunitInstance','error','Please select a number of cells',NULL,'en_US','cellcount_empty',0),
	(1134,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'56AACB81BA27447DB51D506BE1ECE475','platformunitInstance','label','Name',NULL,'en_US','name',0),
	(1135,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'194EDE61255F4A85BCBCDE1E63BD8778','platformunitInstance','constraint','NotEmpty',NULL,'en_US','name',0),
	(1136,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'A564729AA8484DD991652D65880CECE4','platformunitInstance','error','Name cannot be empty',NULL,'en_US','name',0),
	(1137,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'A1BD773BC7F742BD8CC17FF7362ACF54','platformunitInstance','error','Name already exists in the database',NULL,'en_US','name_exists',0),
	(1138,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'9F72EC97B03F4AA6A52D6DBFDA8B5208','platformunitInstance','label','Cell Count',NULL,'en_US','numberOfCellsRequested',0),
	(1139,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'75030F89EB78485D9D67988332F78852','platformunitInstance','error','Cell Count cannot be empty',NULL,'en_US','numberOfCellsRequested',0),
	(1140,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'9B9C034B597D4A03A34FE03399BAD1B4','platformunitInstance','error','Action not permitted at this time. To reduce the number of cells, remove libraries on the cells that will be lost.',NULL,'en_US','numberOfCellsRequested_conflict',0),
	(1141,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'9ACD672E8E52466B898B92137B4AFBA7','platformunitInstance','label','Platform Unit Instance List',NULL,'en_US','platformunitinstance_list',0),
	(1142,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'B3AFDFA49B5446119B688D6539AF4A7F','platformunitInstance','label','Reset',NULL,'en_US','reset',0),
	(1143,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'33A31450F01C4772B59CF17ED7FEC3D6','platformunitInstance','label','Submitter',NULL,'en_US','submitter',0),
	(1144,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'51E979575FC44425B13A2EC0187C1CED','platformunitInstance','constraint','NotEmpty',NULL,'en_US','submitter',0),
	(1145,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'15B052C58A79475291DF4F13EAB2C08E','platformunitInstance','error','Submitter cannot be empty',NULL,'en_US','submitter',0),
	(1146,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'F361BCA90700458F978F3CC7F1A6B4FC','platformunitInstance','label','Subtype',NULL,'en_US','subtype',0),
	(1147,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'7A2C4B844FED4AE0B996260CBD15EE07','platformunitInstance','constraint','NotEmpty',NULL,'en_US','subtype',0),
	(1148,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'6E399F4F7405404D8985526CE08F212A','platformunitInstance','error','Subtype cannot be empty',NULL,'en_US','subtype',0),
	(1149,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'FB147B4DECF44D44B0F2669342CAE1FE','platformunitInstance','error','Platformunit was NOT updated. Please fill in required fields.',NULL,'en_US','updated',0),
	(1150,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'25C917E2DA704AC395A11674AFC1D9DE','platformunitInstance','label','Platformunit updated successfully.',NULL,'en_US','updated_success',0),
	(1151,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'72B6F2AE6A9B4DB294EDF6F3C7D79C73','platformunitInstance','label','Submit',NULL,'en_US','submit',0),
	(1152,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'644FBB8BEA4940E58D95B75330DD1014','platformunitShow','label','Action',NULL,'en_US','action',0),
	(1153,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'E4061E3ECDC742D28258879A8DFCABA0','platformunitShow','label','Add To Run',NULL,'en_US','addToRun',0),
	(1154,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'FAE18085E03249C1AC96CDE061A96E07','platformunitShow','label','Barcode',NULL,'en_US','barcodeName',0),
	(1155,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'1B50AADE48A54D7EAB54DE1967C550A4','platformunitShow','label','Comments',NULL,'en_US','comment',0),
	(1156,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'F07E1C969B6941A5B30DB2BBB9D7510C','platformunitShow','label','Delete',NULL,'en_US','delete',0),
	(1157,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'7D7F133A68684E2FA7DA32DA79D033B4','platformunitShow','label','delete',NULL,'en_US','deleteSmall',0),
	(1158,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'BDAC0EA7393E46B9AC201BADDE5ABCCD','platformunitShow','label','Edit',NULL,'en_US','edit',0),
	(1159,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'7087731B68B04356BA690E7D9F66B831','platformunitShow','label','edit',NULL,'en_US','editSmall',0),
	(1160,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'BB49567A597A48F0B24FA7716A761500','platformunitShow','label','End',NULL,'en_US','end',0),
	(1161,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'903E2F0BA7184F5E97A0EE1CA0207841','platformunitShow','label','Length',NULL,'en_US','length',0),
	(1162,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'0F21A3FF2E2F44919246581AE64FE9A6','platformunitShow','label','Machine',NULL,'en_US','machine',0),
	(1163,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'28977C0F41404E7FA0E7BEA93A35AF69','platformunitShow','label','Cell Count',NULL,'en_US','numberOfCellsOnThisPlatformUnit',0),
	(1164,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'ABEDE85C3187414689C843F5FF4F6E15','platformunitShow','label','Read Type',NULL,'en_US','readType',0),
	(1165,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'342E4CDB772549B29CAF5F8D9EAF3614','platformunitShow','label','Read Length',NULL,'en_US','readLength',0),
	(1166,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'D3E7DA82BF3A43C38BA5D0AAD008C4F6','showPlatformUnit','label','Remove Library',NULL,'en_US','removeLibrary',0),
	(1167,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'EBE344400F314D1AA41C5D89913C6E84','platformunitShow','label','Run',NULL,'en_US','run',0),
	(1168,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'AFBDC7395AB3434E900AB39C0DCAE867','platformunitShow','label','start',NULL,'en_US','start',0),
	(1169,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'82CC2138222C4E50A08CEF69FB1A6CC3','platformunitShow','label','Status',NULL,'en_US','status',0),
	(1170,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'21AC19258A9941B6A26ECC28A8B591DE','platformunitShow','label','Type',NULL,'en_US','type',0),
	(1171,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'471FC876C8104FD0BFA85107A79292D0','platformunitShow','label','Type',NULL,'en_US','typeOfPlatformUnit',0),
	(1172,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'6A91AD76EAC74BF092A35BE6F981A940','platformunitShow','label','Do you really want to delete this platform unit record?',NULL,'en_US','wantToDeletePU',0),
	(1173,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'8E91F2F0484A4A078C72D233215EDC8B','platformunitShow','label','Do you really want to delete this run record?',NULL,'en_US','wantToDeleteRun',0),
	(1174,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'AC115E6CBDB24DB8B3F7BC6F92F9E207','plugin','label','Web Enabled Plugins',NULL,'en_US','list',0),
	(1175,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'B6110ADA1A52403E858D5DC8619FF686','plugin','label','No plugins to display',NULL,'en_US','none',0),
	(1176,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'7D2E9066F932483D989E7D59533C21BE','puLimitPriorToAssign','label','All Available Jobs',NULL,'en_US','allAvailableJobs',0),
	(1177,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'7A4270DEC89E470B8D223B5FED3B72D4','puLimitPriorToAssign','label','Choose A Job',NULL,'en_US','chooseJob',0),
	(1178,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'EC1F87C4AECE47E29C683DFAEA9FDF6C','puLimitPriorToAssign','label','Choose A Machine',NULL,'en_US','chooseMachine',0),
	(1179,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'95CB29F3FB8049E5AABF3E083C8EC0B4','puLimitPriorToAssign','label','Job',NULL,'en_US','job',0),
	(1180,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'F51CA1A6545241CDBD499DCE518F7DA3','puLimitPriorToAssign','label','Select A Job',NULL,'en_US','selectJob',0),
	(1181,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'1CF48AAA7FF6405FA7C34D77092C4D56','puLimitPriorToAssign','label','Select A Machine',NULL,'en_US','selectMachine',0),
	(1182,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'558DA693C8C145E7B29C120B8333ABAB','puLimitPriorToPUList','label','No MPS Resources Available',NULL,'en_US','noMPSResourcesAvailable',0),
	(1183,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'B9F82F0BA4DE4B49B5A33360C94B8F9B','puLimitPriorToPUList','label','Select A Machine',NULL,'en_US','selectMachine',0),
	(1184,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'8557D96F3131416E9AD5B7544A3DF1B9','resource','label','Barcode',NULL,'en_US','barcode',0),
	(1185,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'FA41B98A33E541B9B030BBF4FBE8B176','resource','error','Barcode already exists in the database',NULL,'en_US','barcode_exists',0),
	(1186,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'2A3035818C3540E094FF1BEBFF49A1CE','resource','label','Cancel',NULL,'en_US','cancel',0),
	(1187,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'C476FC8775E543C0892DCD3F6609C1F9','resource','label','Commission Date',NULL,'en_US','commission_date',0),
	(1188,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'994E1AC7856A4763B36E2231E34FE098','resource','error','Commission date cannot be empty',NULL,'en_US','commission_date',0),
	(1189,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'15D5780F9D4646A2977C5BB98A94F552','resource','constraint','NotEmpty',NULL,'en_US','commission_date',0),
	(1190,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'6B51EA30CCD945939C152A4298231FA0','resource','metaposition','40',NULL,'en_US','commission_date',0),
	(1191,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'6C340CD562C040FA8F313D1060DBC485','resource','error','Resource was NOT created. Please fill in required fields.',NULL,'en_US','created',0),
	(1192,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'87CB2B6865EC4B5192E53EC7766BCAE8','resource','label','Resource created successfully.',NULL,'en_US','created_success',0),
	(1193,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'D7FA8E7AA3B3459FB22BD001C9C740CA','resource','label','Decommission Date',NULL,'en_US','decommission_date',0),
	(1194,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'736CB1A001FC41FD8382C8260B126B12','resource','error','Decommission date cannot be empty',NULL,'en_US','decommission_date',0),
	(1195,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'0B347BAF7A0747F3A4D5D0E5A706558E','resource','metaposition','50',NULL,'en_US','decommission_date',0),
	(1196,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'42C69E5F521F4920A94F4E3854CE53BC','resource','label','Active',NULL,'en_US','isActive',0),
	(1197,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'60C9B7115A9243848AB4914057E310F9','resource','label','Resource Category',NULL,'en_US','machineType',0),
	(1198,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'ACA198FC93A94652A51015803F59577B','resource','label','Name',NULL,'en_US','name',0),
	(1199,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'01EC84AD6CDD4087B3050742ECBE8EDF','resource','error','Resource name cannot be empty',NULL,'en_US','name',0),
	(1200,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'D0A5CB71C827416B9EF54A06443A052E','resource','constraint','NotEmpty',NULL,'en_US','name',0),
	(1201,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'05B7C972EA124791BD936113CD9279B9','resource','label','Resource Category',NULL,'en_US','resourceCategoryId',0),
	(1202,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'E0A42452DB0C429594C55CF6A66A86CF','resource','error','Must select category of resource',NULL,'en_US','resourceCategoryId',0),
	(1203,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'31C2F34C916647F7BF975BEEB02422F9','resource','constraint','NotEmpty',NULL,'en_US','resourceCategoryId',0),
	(1204,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'0F6E2DC128D648DEB0E3DDE54D2907C8','resource','label','Resource Type',NULL,'en_US','resourceTypeId',0),
	(1205,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'3C024E977E3C417AA52B811D3ABEA999','resource','error','Must select assay platform',NULL,'en_US','resourceTypeId',0),
	(1206,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'0CEBC1DBF1B645408DD5277E3B689760','resource','constraint','NotEmpty',NULL,'en_US','resourceTypeId',0),
	(1207,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'AD57AE547C6A485C81CD57D0F9CD134A','resource','error','Resource name already exists in the database',NULL,'en_US','resource_exists',0),
	(1208,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'BE54292FEAF143D198F620C9DFDE9F6E','resource','label','List of Machines',NULL,'en_US','resource_list',0),
	(1209,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'EBADA11717FE4971B6F7002CE0895F31','resource','label','Save',NULL,'en_US','save',0),
	(1210,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'530602801AD7480785D955BF9909E443','resource','error','Resource was NOT updated. Please fill in required fields.',NULL,'en_US','updated',0),
	(1211,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'548FEA167B5C417DA69CAE2C218A5AE8','resource','error','Problem occurred updateing metadata',NULL,'en_US','updated_meta',0),
	(1212,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'4C5CCE409D5A46C1BD50959EEFA6661C','resource','label','Update Resource Details',NULL,'en_US','updateResourceDetails',0),
	(1213,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'07EC5222B4E54D0F940735E98E524742','resource','label','Resource updated successfully.',NULL,'en_US','updated_success',0),
	(1214,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'A144204DED0C471F8375826E1CF413CD','run','label','Run created successfully.',NULL,'en_US','created_success',0),
	(1215,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'4F38DFF94CB543AABE32B88431760723','run','label','Run was NOT successfully created.',NULL,'en_US','created_failure',0),
	(1216,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'233FC6BA010C41CA93085EA7DF606F18','run','label','Start',NULL,'en_US','dateRunStarted',0),
	(1217,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'1ADFE56A316A4C89AF2B7CDA838DE749','run','label','End',NULL,'en_US','dateRunEnded',0),
	(1218,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'1BB7357DF95648D596AD8A504A2A76B7','run','label','Files',NULL,'en_US','detailFiles',0),
	(1219,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'B59B7F9386BF4A8195A1C30C060E26F6','run','label','Resource',NULL,'en_US','detailResource',0),
	(1220,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'EE651128E2F34D2F96F3D8747096D1FB','run','label','Resource Cells',NULL,'en_US','detailResourceCells',0),
	(1221,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'4A228C0B3B894417AF8DC4B4F72CF008','run','label','Run Cells',NULL,'en_US','detailRunCells',0),
	(1222,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'65A13CA8C6864463BF4A4C0B370379A1','run','label','Sample',NULL,'en_US','detailSample',0),
	(1223,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'4B676D593ACC49619564892FC7A20BCA','run','label','Samples',NULL,'en_US','detailSamples',0),
	(1224,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'44A3E69C6CDC47FDB731F258FC8BC148','run','error','Invalid run ID provided',NULL,'en_US','invalid_id',0),
	(1225,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'8899F398F1424BCCA63F7B87F0C439C0','run','label','Files',NULL,'en_US','celldetailFiles',0),
	(1226,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'07480509073E4960AE168E1180C1A014','run','label','Resource',NULL,'en_US','celldetailResource',0),
	(1227,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'97CE013F3DBC424788F3FDD8A703F875','run','label','Sample',NULL,'en_US','celldetailSample',0),
	(1228,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'8DC1C2B538F4487382913AF01DAABF3E','run','label','Machine',NULL,'en_US','machine',0),
	(1229,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'844CDC57A29A4573AF2A234C6D8C06F8','run','label','Run Name',NULL,'en_US','name',0),
	(1230,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'17DB023F78484C8CBE78D9DB6E31451C','run','label','Data Path',NULL,'en_US','path_to_data',0),
	(1231,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'B30A39E88F9546EAB6299B410C28C244','run','label','PU Barcode',NULL,'en_US','platformUnitBarcode',0),
	(1232,'2013-08-05 19:04:19','2013-08-05 19:04:19',X'BF51F63D5545424AAFFBC3B7FDCB824F','run','label','Length',NULL,'en_US','readLength',0),
	(1233,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'9893244D5CEE4E879783FE9AA930CCBF','run','label','Type',NULL,'en_US','readType',0),
	(1234,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'60BCFC307A5C47E296DA778276E631B9','run','label','List of Runs',NULL,'en_US','run_list',0),
	(1235,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'622A91A178DD49A08F465DAAEFCBC147','run','label','Status',NULL,'en_US','statusForRun',0),
	(1236,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'F01FF5648E3D457E964FA51C65A97E77','run','label','Run updated successfully.',NULL,'en_US','updated_success',0),
	(1237,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'93DE4499F67249DAB2239FDB2E5DA339','runInstance','label','Cancel',NULL,'en_US','cancel',0),
	(1238,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'ABF2C786E1624EF5BAA8B8E4BD6AE670','runInstance','label','Choose A Run Folder',NULL,'en_US','chooseRunFolder',0),
	(1239,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'B140B33E66814E5E8D3A04AEAEF28A9F','runInstance','label','Run created successfully.',NULL,'en_US','created_success',0),
	(1240,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'D2A72134AAD64B55AC5DACE098212AF6','runInstance','label','Create New Sequence Run',NULL,'en_US','headerCreate',0),
	(1241,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'4A81FBCA80854F9FB949D0A5AEBE2FF3','runInstance','label','Update Sequence Run',NULL,'en_US','headerUpdate',0),
	(1242,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'A82D1DA0A3AA42B49CFC0139DAA4615E','runInstance','label','Run Ended',NULL,'en_US','dateRunEnded',0),
	(1243,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'46C02C2F690E462AA2A9AF9691F1C9C3','runInstance','label','Run Started',NULL,'en_US','dateRunStarted',0),
	(1244,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'910091784A764B1E9A487C42B835375E','runInstance','constraint','NotEmpty',NULL,'en_US','dateRunStarted',0),
	(1245,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'86BA012AEADE46C0BCA13817FA87796F','runInstance','error','Cannot be empty',NULL,'en_US','dateRunStarted',0),
	(1246,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'E1610B9E25AC479D939DAA7E6FFD2D42','runInstance','error','Incorrect Format',NULL,'en_US','dateRunStartedFormat',0),
	(1247,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'546913DD086F4E16A7534584E4F832DC','runInstance','label','Name',NULL,'en_US','name',0),
	(1248,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'5A5F27FF2E7E4DFCAA3E1DFD8741BE52','runInstance','constraint','NotEmpty',NULL,'en_US','name',0),
	(1249,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'827C0E78EF6E4B35914CC618C9F2DA8B','runInstance','error','Name cannot be empty',NULL,'en_US','name',0),
	(1250,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'AD383C277E6F4C9AB7FF2A4481C7E6B1','runInstance','error','Run name already exists',NULL,'en_US','name_exists',0),
	(1251,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'01DA9F67E5BA4AA79419F585F2F71E7B','runInstance','label','Read Type',NULL,'en_US','readType',0),
	(1252,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'E96E546FA0A64F6287A529A410DB9D56','runInstance','constraint','NotEmpty',NULL,'en_US','readType',0),
	(1253,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'81F746C5301D478C8E3633AB06DCC501','runInstance','error','Read Type cannot be empty',NULL,'en_US','readType',0),
	(1254,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'53ABC2AB778D48F39DD48F185CD586C7','runInstance','control','select:${readTypes}:value:label',NULL,'en_US','readType',0),
	(1255,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'24D2B0E8F51848C2B429C5DF2BAF1F51','runInstance','metaposition','10',NULL,'en_US','readType',0),
	(1256,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'68AC5209335946769C35004CD5293A18','runInstance','label','Read Length',NULL,'en_US','readLength',0),
	(1257,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'634AA8C7F1BE45B8A8190005CF106EDE','runInstance','constraint','NotEmpty',NULL,'en_US','readLength',0),
	(1258,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'EB968B6EAE88481DB70C08244340C2C5','runInstance','error','Read Length cannot be empty',NULL,'en_US','readLength',0),
	(1259,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'B44E65FD592740FAA101E435BB178C4B','runInstance','control','select:${readLengths}:value:label',NULL,'en_US','readLength',0),
	(1260,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'65A8411E5E19439187A00B9B55B4AB1E','runInstance','metaposition','15',NULL,'en_US','readLength',0),
	(1261,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'51FB4FF9575A4F16BA7C10469027C58D','runInstance','label','Machine Name',NULL,'en_US','resourceName',0),
	(1262,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'57147C76A0F24FA7A6B05687A5C7C2B3','runInstance','error','Named machine not registered in WASP',NULL,'en_US','resourceNameNotFound',0),
	(1263,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'0CC3EB78CCF0474A80B048F48D1F2A64','runInstance','label','Reset',NULL,'en_US','reset',0),
	(1264,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'E654674DA7B84E8A87FDA763F9369E0B','runInstance','label','Submit',NULL,'en_US','submit',0),
	(1265,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'0C7975497C22411AA56C5EEA96821C2D','runInstance','label','Show All (don\'t restrict by barcode)',NULL,'en_US','showAll',0),
	(1266,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'C83FC35EB4044683A6C9ADE200F1392E','runInstance','label','Technician',NULL,'en_US','technician',0),
	(1267,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'31C80E2947334653883B7382C69676E5','runInstance','constraint','NotEmpty',NULL,'en_US','technician',0),
	(1268,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'E190122AD08642DF967FD6D80DCE8998','runInstance','error','Technician cannot be empty',NULL,'en_US','technician',0),
	(1269,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'3938D244C91242C09096C6CE3E3FDB4C','runInstance','label','Run updated successfully.',NULL,'en_US','updated_success',0),
	(1270,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'0E452C16751E4EF3BACDC49B6C1375BA','sample','label','Adaptor',NULL,'en_US','controlLib_adaptor',0),
	(1271,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'2C528065EEFE478EAC515A88B25B6F17','sample','label','Adaptor Set',NULL,'en_US','controlLib_adaptorSet',0),
	(1272,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'B777ED28E3334E078030497B394479BB','sample','label','Create New Control',NULL,'en_US','controlLib_createButton',0),
	(1273,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'2F839B06A27F4BED9C185BD7FC1661EE','sample','label','Control Libraries',NULL,'en_US','controlLib_controlLibraries',0),
	(1274,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'DE3765EFBC5D429D920681D5564A4620','sample','label','Edit',NULL,'en_US','controlLib_edit',0),
	(1275,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'CBF2E37A05F44BB59780941491946CB6','sample','label','Index',NULL,'en_US','controlLib_index',0),
	(1276,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'323B029F05804DFAAE82205BC9BFE20C','sample','label','Is Active?',NULL,'en_US','controlLib_isActive',0),
	(1277,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'860A1431AF3E407F8D1223F3F90232F9','sample','label','Control Name',NULL,'en_US','controlLib_name',0),
	(1278,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'949D5EE2D88E4975B522EF3AFE48151C','sample','label','No Control Libraries Exist',NULL,'en_US','controlLib_noneExist',0),
	(1279,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'0E0CF27BDB1E4881A7E312BE64B4060E','sample','label','',NULL,'en_US','detail_children',0),
	(1280,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'BF0FC7281A2E49D398A97A88A6DB4675','sample','label','Facility Manager Sample To Library',NULL,'en_US','detail_facManSampleToLib',0),
	(1281,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'1AE5117789BA4F02BB7140A491200742','sample','label','Files',NULL,'en_US','detail_files',0),
	(1282,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'17527DA3A07244AB878E12EB2C73F8E1','sample','label','Jobs',NULL,'en_US','detail_jobs',0),
	(1283,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'26E2B5AD11E542B89AC54918B08B392A','sample','label','Parents',NULL,'en_US','detail_parents',0),
	(1284,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'5769081841F74834B8D1CFB898251D15','sample','label','Relations',NULL,'en_US','detail_relations',0),
	(1285,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'A6A67D5E84BB4F3EBFFF0D30C0AB1857','sample','label','Received?',NULL,'en_US','receivedStatus',0),
	(1286,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'E908DF019D6B41EB99C9114E93AE8917','sample','label','Job',NULL,'en_US','jobId',0),
	(1287,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'F42E8F4CAD9F495381BED6C3816C8922','sample','label','Sample Name',NULL,'en_US','name',0),
	(1288,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'839ECC3208974365AD215321074F0C17','sample','error','Name cannot be null',NULL,'en_US','name',0),
	(1289,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'361C1650084044BB877292B75EBCC5A3','sample','label','PI',NULL,'en_US','pi',0),
	(1290,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'C925485E32514134846D638AB3CF8B68','sample','label','Runs',NULL,'en_US','runs',0),
	(1291,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'A113B73438D04827A37798C016F673EF','sample','label','List of Samples',NULL,'en_US','sample_list',0),
	(1292,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'0B16BBC456A54B8BBB1CEA0E6521E710','sample','label','Submitter',NULL,'en_US','submitter',0),
	(1293,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'FA7E05772D4549DE8AFB9CF19FC889CD','sample','label','Subtype',NULL,'en_US','subtype',0),
	(1294,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'CE9D155A78AE492DA06F78443368C137','sample','label','Type',NULL,'en_US','type',0),
	(1295,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'B1957525374A46BBAF9B423D93C26C86','sample','label','Active',NULL,'en_US','updateControlLib_active',0),
	(1296,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'2CF07ABD97DF490B9BBBDDB74307F686','sample','label','Please select active or inactive for this control',NULL,'en_US','updateControlLib_activeAlert',0),
	(1297,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'86F99FB0F8784E52BECC00FFD46F41B9','sample','label','Adaptor Set',NULL,'en_US','updateControlLib_adaptorSet',0),
	(1298,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'61F4C6A1922A4AEF95CC386C104F77F2','sample','label','Please select an adaptor',NULL,'en_US','updateControlLib_adaptorAlert',0),
	(1299,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'2976D73B7A354E65AC7F3283AC2018B2','sample','label','Please select an adaptor set',NULL,'en_US','updateControlLib_adaptorSetAlert',0),
	(1300,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'5AE8303F49B94ED5814DF981FB32144E','sample','label','Adaptor',NULL,'en_US','updateControlLib_adaptor',0),
	(1301,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'510009B836B14832AA1CA92CB50C6E07','sample','label','Cancel',NULL,'en_US','updateControlLib_cancel',0),
	(1302,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'91240BB923BB44E5B4C8172ED23358F6','sample','label','Create New Library Control',NULL,'en_US','updateControlLib_create',0),
	(1303,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'D9C6ABE15F1344E79157D2103402E3D0','sample','label','Inactive',NULL,'en_US','updateControlLib_inactive',0),
	(1304,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'9FD19BCAAD3B47289BA5F68A974AE9A3','sample','label','Index',NULL,'en_US','updateControlLib_index',0),
	(1305,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'5F7C5F69C0D94FAB8FBCEFC4182CDD51','sample','label','Is Active?',NULL,'en_US','updateControlLib_isActive',0),
	(1306,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'B561BDA318C94B49B29CFC85534501D2','sample','label','Control Name',NULL,'en_US','updateControlLib_name',0),
	(1307,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'001C32D085F84263BE64894EED35F175','sample','label','Please provide a name for this control',NULL,'en_US','updateControlLib_nameAlert',0),
	(1308,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'885F3D16EA604DFFABCED4574A988EC0','sample','label','Reset',NULL,'en_US','updateControlLib_reset',0),
	(1309,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'3D66168717B4442BB31600A668EB99F6','sample','label','SELECT AN ADAPTOR',NULL,'en_US','updateControlLib_selectAdaptor',0),
	(1310,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'C448756E338645198632D88660554DD7','sample','label','SELECT AN ADAPTOR SET',NULL,'en_US','updateControlLib_selectASet',0),
	(1311,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'307CC52BA0754DCBB1ACB16A587303B2','sample','label','Submit',NULL,'en_US','updateControlLib_submit',0),
	(1312,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'078766EA04384E6D8B118AD262A332D3','sample','label','Update Library Control',NULL,'en_US','updateControlLib_update',0),
	(1313,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'4EDB3DFDD7A04A3B853BC631AF5BDEED','sampleDetail','error','No adaptorset matches supplied adaptorset parameter',NULL,'en_US','adaptorsetParameter',0),
	(1314,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'E518074B1D0D47F78AAB883FD08400A4','sampleDetail','error','Job not found in the database',NULL,'en_US','jobNotFound',0),
	(1315,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'9C78603ADC044A769A2FDA883189FA1D','sampleDetail','error','No job matches supplied job parameter',NULL,'en_US','jobParameter',0),
	(1316,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'EA278D596D884B92B9DFAD9A1AF89B3C','sampleDetail','error','Supplied job and sample parameters do not refer to a valid object',NULL,'en_US','jobSampleMismatch',0),
	(1317,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'16E7A946D1A94A49B4A724ECA34E3CDB','sampleDetail','error','Library not found in the database',NULL,'en_US','libraryNotFound',0),
	(1318,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'71D7743AE7FE49579C2A5B3886BAC0F9','sampleDetail','error','Name already exists in database!',NULL,'en_US','nameClash',0),
	(1319,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'8B95D61DB11146F5B3F64D245158F880','sampleDetail','error','Sample not found in the database',NULL,'en_US','sampleNotFound',0),
	(1320,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'B5DC8F8A7F34406F8A18E6D066549CE8','sampleDetail','error','No sample matches supplied sample parameter',NULL,'en_US','sampleParameter',0),
	(1321,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'FD5B2FD8EA3D4BDC9087322F11E2E95E','sampleDetail','error','Cannot find requested sample subtype in the database',NULL,'en_US','sampleSubtypeNotFound',0),
	(1322,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'59CFDE19C2024FC5911B7AB2600FBB3B','sampleDetail','error','Sample NOT updated. Unexpected error!',NULL,'en_US','unexpected',0),
	(1323,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'DFA55E17CA6640C6A0E35A06DB6AE76E','sampleDetail','error','Sample NOT updated. Fill in required fields or cancel to restore.',NULL,'en_US','updated',0),
	(1324,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'95CE81335DA1440EB8DF57F47A61EBE5','sampleDetail','label','Sample sucessfully updated.',NULL,'en_US','updated_success',0),
	(1325,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'8F2B09FFECBC4B168C6E2905C2F99C8D','sampledetail_ro','label','Cancel',NULL,'en_US','cancel',0),
	(1326,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'C5AA9D177F4547E6B2AA4B78855DCC29','sampledetail_ro','label','Edit',NULL,'en_US','edit',0),
	(1327,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'3EE88BBD42DD42DDBA29913B1A21E1C3','sampledetail_ro','label','Sample Name',NULL,'en_US','sampleName',0),
	(1328,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'6B4B878FBB1848938BF812AFA7B27DB0','sampledetail_ro','label','Sample Type',NULL,'en_US','sampleType',0),
	(1329,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'B585B7D6257F49B4B4A6F93A40579458','sampledetail_rw','label','Cancel',NULL,'en_US','cancel',0),
	(1330,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'A5F28C139C3D486B87AC211409F4163E','sampledetail_rw','label','Sample Name',NULL,'en_US','sampleName',0),
	(1331,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'438D9AFD94D54B8FB9A9F9ED16631948','sampledetail_rw','label','Sample Type',NULL,'en_US','sampleType',0),
	(1332,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'D250833DD6AE41E791B7FDE7F4C55421','sampledetail_rw','label','Save',NULL,'en_US','save',0),
	(1333,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'3DDA652D4C454B81A3F8A642FCFDBA3C','sampleDraft','error','You must provide a sample name',NULL,'en_US','name',0),
	(1334,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'9F32D3B00B284B59A73EF1582569FC39','samplereceivetask','label','No Pending Samples',NULL,'en_US','subtitle_none',0),
	(1335,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'D6962C8660754F5E8DD3FF7B00FA8A52','sections','label','Home',NULL,'en_US','banner_dashboard',0),
	(1336,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'441706B27BA9488893779231C888815B','sections','label','Logout',NULL,'en_US','banner_logout',0),
	(1337,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'3FCF56C572DD4B65B0DFAF6A1C6B69C6','sections','label','Albert Einstein College of Medicine (2012). Distributed freely under the terms of the',NULL,'en_US','footer_albert',0),
	(1338,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'C73ECF3936E840C58CE71DCBB2FAD7A9','sections','label','GNU AFFERO General Public License',NULL,'en_US','footer_gnu',0),
	(1339,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'BFBB2C5C3E8948CFBA5ED6B149B9777A','sections','label','Core WASP System and plugin repository maintained by Albert Einstein College of Medicine Computational Epigenomics and Genomics Cores',NULL,'en_US','footer_maintainedBy',0),
	(1340,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'A0BF1BAEA58C4D698259CA2A71842062','sections','label','waspsystem.org',NULL,'en_US','footer_waspsystem',0),
	(1341,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'0E532F3298104C0AB6B029866B9B70CE','showPlatformUnit','label','Add',NULL,'en_US','add',0),
	(1342,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'062529A34D5C48418B0516C99C69CD80','showPlatformUnit','label','Add Control',NULL,'en_US','addControl',0),
	(1343,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'7EDD3943956842E38328B148C75DB94C','showPlatformUnit','label','Barcode',NULL,'en_US','barcode',0),
	(1344,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'D6721CB139024A049BAB132123C1A635','showPlatformUnit','label','Cancel',NULL,'en_US','cancel',0),
	(1345,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'9C4CED1C7E7B4023B6B05FFE86BB9C78','showPlatformUnit','label','Cell',NULL,'en_US','cell',0),
	(1346,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'0F9DE4DB83E542D5AF3FC25EB13B6D74','showPlatformUnit','label','Conc. On Cell',NULL,'en_US','concOnCell',0),
	(1347,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'D11E5CCE6BB74684A8C5B70E330683ED','showPlatformUnit','label','Create New Run',NULL,'en_US','createNewRun',0),
	(1348,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'78B6B33AB171432FB71B99780C5BF7BA','showPlatformUnit','label','Current Conc. (pM)',NULL,'en_US','currentConcPM',0),
	(1349,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'A7F4121C4C684CFE966D32E099DA9405','showPlatformUnit','label','Edit',NULL,'en_US','edit',0),
	(1350,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'52FC64AF9D84420E9CFD165253CA778F','showPlatformUnit','label','Final Conc. (pM)',NULL,'en_US','finalConcPM',0),
	(1351,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'DC641D38715E4115BBB0090660310538','showPlatformUnit','label','Index',NULL,'en_US','index',0),
	(1352,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'C76608DD08D84E4FB2F77AA65DBA1A6C','showPlatformUnit','label','Job J',NULL,'en_US','jobJ',0),
	(1353,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'A461CBCE3F4B41B883931F811DE3333D','showPlatformUnit','label','Locked',NULL,'en_US','locked',0),
	(1354,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'62F513631AC44EAEB4ACC9D0AA4868AC','showPlatformUnit','label','Machine',NULL,'en_US','machine',0),
	(1355,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'3FD0FDF83D6E4913BD04B8522C80F20D','showPlatformUnit','label','New Conc. (pM)',NULL,'en_US','newConcPM',0),
	(1356,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'09A17BABFB1D4F0A8E7064D0F90C3E01','showPlatformUnit','label','No Control On Cell',NULL,'en_US','noControlOnCell',0),
	(1357,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'21BFE90BBFAF4557B19B7CD0B336CEEE','showPlatformUnit','label','No Libraries On Cell',NULL,'en_US','noLibrariesOnCell',0),
	(1358,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'8D92A00856FF4FCBB4F85B22DFC1E01B','showPlatformUnit','label','Platform Unit Name',NULL,'en_US','platformUnit',0),
	(1359,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'B185020D82EB422A871124F4B4E50909','showPlatformUnit','label','Platform Unit Status',NULL,'en_US','platformUnitStatus',0),
	(1360,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'42D454F5AEDC404F889BA5356D156045','showPlatformUnit','label','Please provide a value',NULL,'en_US','pleasePorvideValue_alert',0),
	(1361,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'A03BEE30B51B4A369BF14884E3635078','showPlatformUnit','label','Please provide a final concentration for this control',NULL,'en_US','pleaseProvideControlConc_alert',0),
	(1362,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'08FEE5E2A83345BFBE818E2018226758','showPlatformUnit','label','Please provide a start date',NULL,'en_US','pleaseProvideStartDate_alert',0),
	(1363,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'4B9B94712A364BD18CF5DD0A433E1169','showPlatformUnit','label','Please provide a valid name for this run',NULL,'en_US','pleaseProvideValidName_alert',0),
	(1364,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'A7808445BEE548F6B51692134D50D305','showPlatformUnit','label','Please provide a start date in the proper format',NULL,'en_US','pleaseProvideValidStartDate_alert',0),
	(1365,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'B6B794A73F60482EBEE58FDF45F6A5D5','showPlatformUnit','label','Please select a control',NULL,'en_US','pleaseSelectControl_alert',0),
	(1366,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'DF6FDF9541EC42CBB4F84B054663F4EC','showPlatformUnit','label','Please select a machine',NULL,'en_US','pleaseSelectMachine_alert',0),
	(1367,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'AFE9D115F40F4F88B5B2E240EA1B071D','showPlatformUnit','label','Please select a read length',NULL,'en_US','pleaseSelectReadLength_alert',0),
	(1368,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'1A7DD0F273864FA786CF316C70EBE1C5','showPlatformUnit','label','Please select a read type',NULL,'en_US','pleaseSelectReadType_alert',0),
	(1369,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'572C3759208F4668816EAFA53F6E6396','showPlatformUnit','label','Please select a technician',NULL,'en_US','pleaseSelectTechnician_alert',0),
	(1370,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'7FD74A61673B4BC890D07720545F7DFC','showPlatformUnit','label','pM',NULL,'en_US','pM',0),
	(1371,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'2CB5E257742C41999DDD54DACDF1B93B','showPlatformUnit','label','Read Length',NULL,'en_US','readLength',0),
	(1372,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'66D81DD7979D46A4B2C20274FA900ECC','showPlatformUnit','label','Read Type',NULL,'en_US','readType',0),
	(1373,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'DE865059CF224FB1A4C8CCA81A789C45','showPlatformUnit','label','Remove Control',NULL,'en_US','removeControl',0),
	(1374,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'0C0356479A344A989855E31C31836C1E','showPlatformUnit','label','Remove Control From This Cell?',NULL,'en_US','removeControlFromThisCell',0),
	(1375,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'6F3DC99ED2144157A5CD5C3D9FDF6246','showPlatformUnit','label','Remove Library From This Cell?',NULL,'en_US','removeLibFromCell_alert',0),
	(1376,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'AEFD307CE655400C8EA6FC57F2B2F321','showPlatformUnit','label','Reset',NULL,'en_US','reset',0),
	(1377,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'3B45EA03E4414E7AB2638E49E7D3C6CD','showPlatformUnit','label','Run Name',NULL,'en_US','runName',0),
	(1378,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'974AD056B6064015834FA42EC8958987','showPlatformUnit','label','Run Technician',NULL,'en_US','runTechnician',0),
	(1379,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'FD45C6CCED164E7694BE52403BAF2DAD','showPlatformUnit','label','--SELECT A CONTROL--',NULL,'en_US','selectControl',0),
	(1380,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'2CDED2C956834D26B4D3E52A8ED722D3','showPlatformUnit','label','--SELECT A MACHINE--',NULL,'en_US','selectMachine',0),
	(1381,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'60005BA387B846EC82A77B31F3FBA09F','showPlatformUnit','label','--SELECT A TECHNICIAN--',NULL,'en_US','selectTechnician',0),
	(1382,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'911A6FF937CF47E894B62BFB86A06E16','showPlatformUnit','label','Start Date (yyyy/mm/dd)',NULL,'en_US','startDate',0),
	(1383,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'694E6AB65E8C49218BA47C430685AA7D','showPlatformUnit','label','Submit',NULL,'en_US','submit',0),
	(1384,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'CE9053361B5849E4846A9F404F079E22','showPlatformUnit','label','Submit?',NULL,'en_US','submit_alert',0),
	(1385,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'9EDE2B1111584D4FA9EC2F60E56006DF','showPlatformUnit','label','Type',NULL,'en_US','type',0),
	(1386,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'46489C14AA8A4AEEA78875FC15164D47','showPlatformUnit','label','Unlocked',NULL,'en_US','unlocked',0),
	(1387,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'1B5B6D7F8ED94C31967EF3790E1BD46B','showPlatformUnit','label','Update',NULL,'en_US','update',0),
	(1388,'2013-08-05 19:04:20','2013-08-05 19:04:20',X'D213F1E11B514B3EB99F8C864D8970A2','showPlatformUnit','label','Once You Create This Run Record,<br />Adding User Libraries To This Platform Unit Will Be Prohibited',NULL,'en_US','warning1',0),
	(1389,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'277CFBB08B72496E972DB2696796363A','showPlatformUnit','label','Adding Additional User Libraries To This Platform Unit<br />Is Now Prohibited',NULL,'en_US','warning2',0),
	(1390,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'83B567A8FD52473797EA1DA62EAD32A2','status','label','PI Approval',NULL,'en_US','piApprove',0),
	(1391,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'99CAA711DB86432EA6188FDB3E512BAD','status','label','Dept. Admin. Approval',NULL,'en_US','daApprove',0),
	(1392,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'5AF51AADD5724895854AA700FC8922FB','status','label','Seq. Facility Approval',NULL,'en_US','fmApprove',0),
	(1393,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'B1EE9B1B66494207BDDDD6D0BD9E748D','status','label','PI Approval',NULL,'en_US','piApproval',0),
	(1394,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'DD8B962A4A4B49F59D3E9FA17555B69F','status','label','DA Approval',NULL,'en_US','daApproval',0),
	(1395,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'8FB0CEC6D3C04B959A1D2D54903C4EF5','status','label','Not Yet Set',NULL,'en_US','notYetSet',0),
	(1396,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'F2D5A8E9B29C4CF78A16F9474242B8E1','status','label','Awaiting Response',NULL,'en_US','awaitingResponse',0),
	(1397,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'483EFD24E1EB4C71A25F25D7A5B60E19','status','label','Approved',NULL,'en_US','approved',0),
	(1398,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'0A365BB1ECBC4645A509A5302CFBD696','status','label','Rejected',NULL,'en_US','rejected',0),
	(1399,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'B4B176D561574F46B583AE397AB8EE6A','status','label','Abandoned',NULL,'en_US','abandoned',0),
	(1400,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'CC873C95693C4DEAA20683C4B6DAD54C','status','label','Unknown',NULL,'en_US','unknown',0),
	(1401,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'452A940C060D486AA18C7225BEC29C31','sysrole','error','Invalid role specified',NULL,'en_US','invalidRoleSpecified',0),
	(1402,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'B1AABD96F7C84607955F84987308C4D5','sysrole','label','Action',NULL,'en_US','list_action',0),
	(1403,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'3EFC19CE0CD44B218D69E14AF81961FA','sysrole','label','Use the form below to add system roles to existing WASP users',NULL,'en_US','list_add_role',0),
	(1404,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'C41AD2AF21F64EDAAC75A489F8CFCAE9','sysrole','label','Add System Role to User',NULL,'en_US','list_create',0),
	(1405,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'2606B31F646D4ABAA3797037A4755C19','sysrole','label','Current Users with System Roles',NULL,'en_US','list_current',0),
	(1406,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'C7B017FB282B43D4BB2CA52E4823146C','sysrole','label','Name (Login)',NULL,'en_US','list_name',0),
	(1407,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'6780B9E46A1C47B1B6F2CD66582679DF','sysrole','label','Remove',NULL,'en_US','list_remove',0),
	(1408,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'F33851EB9FAB49AD8B682F8096E76705','sysrole','label','Role',NULL,'en_US','list_role',0),
	(1409,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'EF1E0D0CDE284CE295D6F574BDAA7578','sysrole','label','Submit',NULL,'en_US','list_submit',0),
	(1410,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'1176FE49F28F4C1BA8686540A6BDBC68','sysrole','label','Existing User',NULL,'en_US','list_sysuser_name',0),
	(1411,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'1AC93ADE94834EE3AC27244C32DEA4A2','sysrole','label','New Role',NULL,'en_US','list_sysuser_role',0),
	(1412,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'9E36B0A4829A4193814EAAC93A38B899','sysrole','label','Unchangeable',NULL,'en_US','list_unchangeable',0),
	(1413,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'99E19649ECB044BABB5D1609490B3117','sysrole','error','No role specified',NULL,'en_US','noRoleSpecified',0),
	(1414,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'714010F2972E4459B4BC6AC29E24738B','sysrole','error','No user specified',NULL,'en_US','noUserSpecified',0),
	(1415,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'567FF93AE4C0454B8F82B56966239CBB','sysrole','error','Cannot remove role from user. The role must be granted to another user first.',NULL,'en_US','onlyUserWithRole',0),
	(1416,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'8F3355E63722430B8DBE4E2C0ADB8627','sysrole','label','Update completed successfully',NULL,'en_US','success',0),
	(1417,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'E6F45F35A1794E0CA5B97EE640F2D59D','sysrole','error','The user specified does not exist in the database',NULL,'en_US','userNonexistant',0),
	(1418,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'6F5BCA97AD3347CE94F26C2EA5BA3E5D','sysrole','error','Specified user already has the selected role',NULL,'en_US','userRoleExists',0),
	(1419,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'75D79EAE39C54F499C0FFF743B6801A4','sysrole','error','Specified user does not have specified role',NULL,'en_US','wrongUserRoleCombination',0),
	(1420,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'8010ABF323944F98A63EE83A3122CE78','task','label','No jobs found which are ready for aggregation analysis',NULL,'en_US','aggregateAnalysis_nojobs',0),
	(1421,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'19AB590189074EADA67E03D8601DD0D3','task','label','Aggregate analysis started successfully',NULL,'en_US','aggregateAnalysis_analysisBegun',0),
	(1422,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'9AEA8797F5CC4D14AE3C8EC2E7DE1A4C','task','label','Aborted: aggreagate analysis NOT begun',NULL,'en_US','aggregateAnalysis_analysisNotBegun',0),
	(1423,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'D73364CB3B524D6B98AF117E2D411089','task','label','Job terminated successfully',NULL,'en_US','aggregateAnalysis_jobTerminated',0),
	(1424,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'0455FA1CED294BF58E3D5189EF8227C0','task','error','Failed: Aggregate Analysis already underway',NULL,'en_US','aggregateAnalysis_aggregateAnalysisAlreadyUnderway',0),
	(1425,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'606C1E6DC14A4A4AADBEFD3E8BB09652','task','error','Failed: Unexpected absence of jobId information',NULL,'en_US','aggregateAnalysis_invalid_jobId',0),
	(1426,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'2E174D93062C47F48DE3BCE86558ECC2','task','error','Failed: Job not found in database',NULL,'en_US','aggregateAnalysis_jobNotFound',0),
	(1427,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'77B571C6EDA740D4BF8CBE90C5E704C5','task','error','Failed: This job has already been terminated',NULL,'en_US','aggregateAnalysis_jobPreviouslyTerminated',0),
	(1428,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'C137625C03C548BBAC1A3B798486FB47','task','error','Update Failed: Please tell us if we should initiate analysis',NULL,'en_US','aggregateAnalysis_invalid_startAnalysis',0),
	(1429,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'6C1D2E505FC24CC0ADD5FBB07717BA0B','task','error','Failed: Unexpectedly obtained an invalid response for starting the analysis',NULL,'en_US','aggregateAnalysis_invalidValues_startAnalysis',0),
	(1430,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'C0C82811BFA044448F89E80C54616B65','task','label','Later',NULL,'en_US','aggregateAnalysis_startAnalysisLater',0),
	(1431,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'91A04455E44A482CB0D2F4A1E8046254','task','label','Never (terminate job)',NULL,'en_US','aggregateAnalysis_startAnalysisNever',0),
	(1432,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'A96B018715F5423F9B1B5631AD000CD2','task','error','Unable to initiate aggregate analysiss.',NULL,'en_US','aggregateAnalysis_startAnalysisNotPossibleNow',0),
	(1433,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'17A82DE15319452EB14FA9A70E01D847','task','label','Now',NULL,'en_US','aggregateAnalysis_startAnalysisNow',0),
	(1434,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'64A732C7C2B746E0BEE5144DCA382B7B','task','label','--Start Analysis?--',NULL,'en_US','aggregateAnalysis_startAnalysisQuestion',0),
	(1435,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'BE7F8D2A00284D7F86EE267BA1D02010','task','label','Update completed successfully',NULL,'en_US','aggregateAnalysis_update_success',0),
	(1436,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'99FCF9AA35F34239A2C06188C4500EC3','task','error','Job Termination command unexpectedly failed',NULL,'en_US','aggregateAnalysis_terminateJobUnexpectedlyFailed',0),
	(1437,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'E92E8DE3B91348E3AA99628F87670412','task','label','Reset',NULL,'en_US','aggregateAnalysis_reset',0),
	(1438,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'2813C0E6CD8045B7B9CD75456318E991','task','label','Submit',NULL,'en_US','aggregateAnalysis_submit',0),
	(1439,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'24F88BA20C4845A3AC9006EE7E8B0616','task','label','N/A',NULL,'en_US','aggregateAnalysis_noQCData',0),
	(1440,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'F89DAB85D2FF4996BA48464B51492E9B','task','label','Processing in stopped state!',NULL,'en_US','aggregateAnalysis_processingStopped',0),
	(1441,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'583DBAC64903496CB556B18F892D314F','task','label','Fail',NULL,'en_US','cellLibraryqc_fail',0),
	(1442,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'C25404D53497474CA4F86162AEB07F48','task','label','QC Status',NULL,'en_US','cellLibraryqc_status',0),
	(1443,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'905A924176064587BBD27FC676818F88','task','label','No jobs found which contain any cell-libraries requiring QC',NULL,'en_US','cellLibraryqc_nojobs',0),
	(1444,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'551ECA3CA9864FD08DF45ED3E002E65B','task','error','Update Failed: Unexpected absence of comment(s)',NULL,'en_US','cellLibraryqc_comment_not_sent',0),
	(1445,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'3299D22A100C418E8FE17904DF28760D','task','error','Update Failed: Please provide an explanation for failing this library',NULL,'en_US','cellLibraryqc_comment_empty',0),
	(1446,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'E439AE286E0D447FA6C6604A5D4CCACB','task','error','One Or More Updates Failed: You must provide an explanation for any failed libraries',NULL,'en_US','cellLibraryqc_failRequiresComment',0),
	(1447,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'40563304B07C456CB7BF8B91C3545F83','task','label','JobID',NULL,'en_US','cellLibraryqc_jobId',0),
	(1448,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'6B5275D273284F08B539D55E0F92DEDE','task','label','Job Name',NULL,'en_US','cellLibraryqc_jobName',0),
	(1449,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'670BC4BAC3E94DF4BB90F8D6F46E565A','task','error','Update Failed: Job not found in database',NULL,'en_US','cellLibraryqc_jobNotFound',0),
	(1450,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'AE0D784F6DF14CE1AD3E3B02912034E6','task','error','Update Failed: This job has already been terminated',NULL,'en_US','cellLibraryqc_jobPreviouslyTerminated',0),
	(1451,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'72976E1E2F8C4F18B1F01A336314AF78','task','label','Pass',NULL,'en_US','cellLibraryqc_pass',0),
	(1452,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'8F58680035AD4BBA999AC24673982E36','task','error','Update Failed: Unexpected absence of jobId information',NULL,'en_US','cellLibraryqc_invalid_jobId',0),
	(1453,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'826496F5E57D4348A4D2AC418FC2A7BA','task','error','Update Failed: Only Pass or Fail are permitted',NULL,'en_US','cellLibraryqc_invalid_qcStatus_value',0),
	(1454,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'5B07D4B37FDC4E5FB84D7D8E70916F16','task','error','Update Failed: Unexpected absence of library-run information',NULL,'en_US','cellLibraryqc_invalid_samplesource',0),
	(1455,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'93445140CB0B4EE2939EC5B52EBFF558','task','label','Library',NULL,'en_US','cellLibraryqc_library',0),
	(1456,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'0D36A1B96CA64A50881CB66A0C525786','task','error','One Or More Updates Failed: Unexpected problem occurred while saving new record',NULL,'en_US','cellLibraryqc_message',0),
	(1457,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'B9A8A47F48774E03AB5065F71598758E','task','error','No Library-run selected',NULL,'en_US','cellLibraryqc_noRecordsSelected',0),
	(1458,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'96D21F5D0472405BA4D75AA1CC913E60','task','label','In order to submit, you must select Pass or Fail for at least one library',NULL,'en_US','cellLibraryqc_noLibRunSelectedAlert',0),
	(1459,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'F86E87095BEF4B92AD576F007A1891F2','task','label','Platform Unit / Sequence Run',NULL,'en_US','cellLibraryqc_pu_seqrun',0),
	(1460,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'C1069DC8BAA54D209054EF287889732B','task','error','Update Failed: You must select either Pass or Fail',NULL,'en_US','cellLibraryqc_qcStatus_invalid',0),
	(1461,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'20864424589248C68FC59D5F60A36470','task','label','Reset',NULL,'en_US','cellLibraryqc_reset',0),
	(1462,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'FC511B79B53B467FB7D16CF0CD901A51','task','label','Sample',NULL,'en_US','cellLibraryqc_sample',0),
	(1463,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'F4B7B8654982474DAA8A0FF18CD6B1DA','task','label','Primary Analysis Status',NULL,'en_US','cellLibraryqc_analysisStatus',0),
	(1464,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'3DE90C35DF3D45D5AE787D0A5049B673','task','error','Update Failed: Unable to locate proper library-run record(s) in database',NULL,'en_US','cellLibraryqc_sampleSourceNotFound',0),
	(1465,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'945032489C644C5090D1C6A02F9FC53B','task','label','set all fail',NULL,'en_US','cellLibraryqc_setAllFail',0),
	(1466,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'4202BDDC39984762BF07EA4451958760','task','label','set all pass',NULL,'en_US','cellLibraryqc_setAllPass',0),
	(1467,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'2A5BAB73E074476EA687728DA347A3AD','task','label','Submit',NULL,'en_US','cellLibraryqc_submit',0),
	(1468,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'CCC645D6BAC44A12818FBA386E7899A9','task','label','Submitter',NULL,'en_US','cellLibraryqc_submitter',0),
	(1469,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'5C0661CB359F4E2F9ED4207E628383BF','task','label','Update completed successfully',NULL,'en_US','cellLibraryqc_update_success',0),
	(1470,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'E5E79C31B573476AB3EBCBB577934B32','task','label','If you reject any library, please provide a reason',NULL,'en_US','cellLibraryqc_validateCommentAlert',0),
	(1471,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'08EEAE7B9418435785231A6DBC7D93CB','task','label','Please select either Pass or Fail',NULL,'en_US','cellLibraryqc_validatePassFailAlert',0),
	(1472,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'CD66896991034332965B8E7DAA8A363C','task','label','Department Administration Tasks',NULL,'en_US','departmentAdmin',0),
	(1473,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'C79F130E4305499CBE8A4FD53322D352','task','label','Facility Manager Tasks',NULL,'en_US','facilityManager',0),
	(1474,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'A0389029B27D4033A050CE993D3D88F8','task','label','Jobs Pending Approval for Analysis',NULL,'en_US','initiateAnalysis_title',0),
	(1475,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'A26498B5B8E04BDC9F6E19A432B60B13','task','label','If the system has determined that there are tasks requiring your attention, links will be posted below. Please click on the links and address the tasks assigned to you ASAP.',NULL,'en_US','instructions',0),
	(1476,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'66E0A290308448258C7C2DD913FA9A0C','task','label','WASP has determined that the tasks below require your attention. Please click on the links to address them.',NULL,'en_US','instructions2',0),
	(1477,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'DCC0786FB3B941628C8023DCA98F652F','task','label','Job Quote Tasks',NULL,'en_US','jobQuote',0),
	(1478,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'3C676ED157A4445DA86538B29E9F350F','task','label','Lab Management Tasks',NULL,'en_US','labManager',0),
	(1479,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'BE50DE98822B43238EF27F6990DAB493','task','label','QC Result',NULL,'en_US','libraryqc_action',0),
	(1480,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'F352B1018CBF4BDDA5CF5B47A91B5D97','task','error','Update Failed: Please provide an explanation for this rejection',NULL,'en_US','libraryqc_comment_empty',0),
	(1481,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'F10489C032FF453DB0AFF8C4307BCB58','task','label','Please provide a reason for this rejection',NULL,'en_US','libraryqc_failBoxDefault',0),
	(1482,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'EBB30EB0101544C5949336A8E52F6882','task','label','Failed',NULL,'en_US','libraryqc_failed',0),
	(1483,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'5E16999F701945159DFD35495D266725','task','error','Library has no record in the database',NULL,'en_US','libraryqc_invalid_sample',0),
	(1484,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'C19795FCE8A2433192D432086DED035B','task','label','JobID',NULL,'en_US','libraryqc_jobId',0),
	(1485,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'69DE0532D9B04BD1A6C7A1267A694838','task','label','Job Name',NULL,'en_US','libraryqc_jobName',0),
	(1486,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'DBD88B2C77854686AEAECB4702888553','task','label','Molecule',NULL,'en_US','libraryqc_molecule',0),
	(1487,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'EBB94A4B11FF486DAEF9E5D6B674B555','task','label','Passed',NULL,'en_US','libraryqc_passed',0),
	(1488,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'BBCF4CB9B16E45B0803FF6C4E17C3408','task','error','Update Failed: You must select either Passed or Failed',NULL,'en_US','libraryqc_qcStatus_invalid',0),
	(1489,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'D5B7FA0CF37F48689C5366A18C0C6DA2','task','label','Reset',NULL,'en_US','libraryqc_reset',0),
	(1490,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'0367B245801B4B15AC7647756B5096B3','task','label','Library',NULL,'en_US','libraryqc_sample',0),
	(1491,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'D51AFF4BB2EC4B4E8EDAE47B8C8BEC93','task','error','Update Failed: Invalid Status',NULL,'en_US','libraryqc_status_invalid',0),
	(1492,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'75D0475AA3374505912908F86773F4EF','task','label','Submit',NULL,'en_US','libraryqc_submit',0),
	(1493,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'9B582CA8FE2F447D9D6757810F4A4ED7','task','label','Submitter',NULL,'en_US','libraryqc_submitter',0),
	(1494,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'FF0DFBF9D6A54732A1619A9A03713E13','task','label','No Pending Library QC Tasks',NULL,'en_US','libraryqc_subtitle_none',0),
	(1495,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'3761CE343A62409A9AD5FB5DA23FBA6A','task','label','Library QC Manager',NULL,'en_US','libraryqc_title',0),
	(1496,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'0F75D62EDB004A17B20065C7BD33CC93','task','label','Update completed successfully',NULL,'en_US','libraryqc_update_success',0),
	(1497,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'48A8BE918D8F4A0680D1B7952A802D2E','task','label','QC of library pre-processing data ',NULL,'en_US','celllibraryqc_title',0),
	(1498,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'FDB9F4D520AB42089F17B051BFE62E92','task','label','Please select either Library Passed or Failed',NULL,'en_US','libraryqc_validateAlert',0),
	(1499,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'ADAC1B6E45824BB78284B025EE513B33','task','error','Update Failed: Please select Passed or Failed',NULL,'en_US','libraryqc_receivedstatus_empty',0),
	(1500,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'27733ED8881F4E65BD1E6F125C734D7C','task','error','Update Failed: Action Invalid',NULL,'en_US','libraryqc_receivedstatus_invalid',0),
	(1501,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'6CF3796EF575451DA495F41950CE68B0','task','error','Problem occurred updating status',NULL,'en_US','libraryqc_message',0),
	(1502,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'BB03D1CE29FF48518F005C78195E06C6','task','label','Please provide a reason for this library failing QC',NULL,'en_US','libraryqc_validateCommentAlert',0),
	(1503,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'FA4B06FF9EC449EA82CB24146BFD913E','task','label','Please select either Passed or Failed',NULL,'en_US','libraryqc_validatePassFailAlert',0),
	(1504,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'FBEDE79471394DBEB1D1624070F21F15','task','label','There are currently no tasks requiring your attention.',NULL,'en_US','none',0),
	(1505,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'B16AE3C2CBE4464AA048DED3C1915F2A','task','label','Action',NULL,'en_US','samplereceive_action',0),
	(1506,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'4A65FDAAA3824FBAA3520B0D9DED6EEA','task','error','Sample has no record in the database',NULL,'en_US','samplereceive_invalid_sample',0),
	(1507,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'106B7E614A08450D9B23D3CFBF3187D8','task','label','JobID',NULL,'en_US','samplereceive_jobId',0),
	(1508,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'3234D7667AB648E2A0C937424DD001AE','task','label','Job Name',NULL,'en_US','samplereceive_jobName',0),
	(1509,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'77E29317F6174D5889D9EF579426F293','task','error','Problem occurred updating status',NULL,'en_US','samplereceive_message',0),
	(1510,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'7E5516B3F8644BBF818EA9350D6C3394','task','label','Molecule',NULL,'en_US','samplereceive_molecule',0),
	(1511,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'8AA233203FE7471AAD8FBE8119903438','task','label','Received',NULL,'en_US','samplereceive_received',0),
	(1512,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'B4BB57B3A10549FBBA60B77DD6DF1DB8','task','error','Update Failed: Please select Received or Withdrawn for at least one sample',NULL,'en_US','samplereceive_receivedstatus_empty',0),
	(1513,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'67ACB29D0B5C46E89A545F814425858A','task','error','Update Failed: Action Invalid',NULL,'en_US','samplereceive_receivedstatus_invalid',0),
	(1514,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'A32A92EC0AE34FE79579DE351BFF66C2','task','error','Update Failed: Unexpected Error',NULL,'en_US','samplereceive_receivedstatus_unexpected',0),
	(1515,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'58D863628AF34492BB81C1F490B4ACAF','task','label','Reset',NULL,'en_US','samplereceive_reset',0),
	(1516,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'7042436BA00B413ABD9F777DB2D6B7AC','task','label','Sample',NULL,'en_US','samplereceive_sample',0),
	(1517,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'4A5E3C08D5A343109CF9667D72372A33','task','label','--SELECT--',NULL,'en_US','samplereceive_select',0),
	(1518,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'B655F39F9C384E2897B942B9117E887B','task','label','set all received',NULL,'en_US','samplereceive_setAllReceived',0),
	(1519,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'562AB1F6CD3A40A4987D4F5357177091','task','label','set all withdrawn',NULL,'en_US','samplereceive_setAllWithdrawn',0),
	(1520,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'9D2923C377854AE79D7EE537331C55E3','task','label','Submit',NULL,'en_US','samplereceive_submit',0),
	(1521,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'197AE2A4C141475DBEB14E8DA28F7AB2','task','label','Submitter',NULL,'en_US','samplereceive_submitter',0),
	(1522,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'B76DCBCFDA15426C8615126AEFFAD939','task','label','Sample Receiver Manager',NULL,'en_US','samplereceive_title',0),
	(1523,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'7D3CF91D8CFC42F592E7BAB958ABA84F','task','label','Update completed successfully',NULL,'en_US','samplereceive_update_success',0),
	(1524,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'1956B18B59B04F5499C44E095113B728','task','label','Please select either Sample Received or Withdrawn for at least one sample',NULL,'en_US','samplereceive_validateAlert',0),
	(1525,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'1F4581CB88C64AA68F16ABC4C482CA59','task','label','Withdrawn',NULL,'en_US','samplereceive_withdrawn',0),
	(1526,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'421CB8F2486147F894ECBAECE9ADBD59','task','label','QC Result',NULL,'en_US','sampleqc_action',0),
	(1527,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'0918BE46526F489997B503761D0A8B5E','task','error','Update Failed: Please provide an explanation for this failure',NULL,'en_US','sampleqc_comment_empty',0),
	(1528,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'84E41CBD172C4034A23C0E2F8B962432','task','label','Failed',NULL,'en_US','sampleqc_failed',0),
	(1529,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'E2D2DB9EDE2F49FBAA08E665B067A16A','task','error','Record not found in database',NULL,'en_US','sampleqc_invalid_sample',0),
	(1530,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'ADD36D6AC9DE4BD5A4A849F2EA5F6964','task','label','JobID',NULL,'en_US','sampleqc_jobId',0),
	(1531,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'AB2D41FF59FF422A8EFC3EB298FF7D4A','task','label','Job Name',NULL,'en_US','sampleqc_jobName',0),
	(1532,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'B717231D506F433988684EBA9E563ACD','task','error','Problem occurred updating status',NULL,'en_US','sampleqc_message',0),
	(1533,'2013-08-05 19:04:21','2013-08-05 19:04:21',X'A8AEB25B81A84B879C868B098AE95C80','task','label','Molecule',NULL,'en_US','sampleqc_molecule',0),
	(1534,'2013-08-05 19:04:21','2013-08-05 19:04:22',X'02A5554A9D2447898EDD04B76B4A4076','task','label','Passed',NULL,'en_US','sampleqc_passed',0),
	(1535,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'E52C378E475D44A1BC12BAC8588AF4AE','task','error','Update Failed: You must select either Passed or Failed',NULL,'en_US','sampleqc_qcStatus_invalid',0),
	(1536,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'9EA13572A73D4CBF91BAE9A27A108AE0','task','label','Reset',NULL,'en_US','sampleqc_reset',0),
	(1537,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'210D547D7C30483CB0BF6761FDB3EC41','task','label','Sample',NULL,'en_US','sampleqc_sample',0),
	(1538,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'83F005CF93AB4C6EBED3E13D16F98AB7','task','error','Update Failed: Invalid Status',NULL,'en_US','sampleqc_status_invalid',0),
	(1539,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'A4DE57534C034F7F95B7B27708EFBA2C','task','label','Submit',NULL,'en_US','sampleqc_submit',0),
	(1540,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'A41C139C4FDB428885D643F62D64273D','task','label','Submitter',NULL,'en_US','sampleqc_submitter',0),
	(1541,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'9D2FF619D64B40BDA6AEA225EC3E96B8','task','label','No Pending Sample QC Tasks',NULL,'en_US','sampleqc_subtitle_none',0),
	(1542,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'03B66634A14448F08C8D0C5C0A636531','task','label','Sample QC Manager',NULL,'en_US','sampleqc_title',0),
	(1543,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'380E1041E031444784B78DAB257DC5CF','task','label','Update completed successfully',NULL,'en_US','sampleqc_update_success',0),
	(1544,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'ECB2726EF3CF4641B63FA1F45F1F95B1','task','error','Update Failed: Please select Passed or Failed',NULL,'en_US','sampleqc_receivedstatus_empty',0),
	(1545,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'8E72ECB30A8F492491DEBC04947A862E','task','error','Update Failed: Action Invalid',NULL,'en_US','sampleqc_receivedstatus_invalid',0),
	(1546,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'AAFA1ECDB6804354BE1081334842A5DD','task','label','Please provide a reason for this sample failing QC',NULL,'en_US','sampleqc_validateCommentAlert',0),
	(1547,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'A54D4D96614B47A6AE3C2A071A13C6ED','task','label','Please select either Passed or Failed',NULL,'en_US','sampleqc_validatePassFailAlert',0),
	(1548,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'F0496AB18C9F420EBF3A8F495F53A772','uiField','label','Field Added',NULL,'en_US','added',0),
	(1549,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'9950A3BBF5D54422B3E34FA2EE197A88','uiField','label','Area',NULL,'en_US','area',0),
	(1550,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'B9F90CE4F22541979CA0D13B5F1FBEFF','uiField','label','Attribute Name',NULL,'en_US','attrName',0),
	(1551,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'6CF86DFC33354BBF928D97F0500AEFA4','uiField','suffix','<font color=\"blue\"> see footnote<sup>1</sup> </font>',NULL,'en_US','attrName',0),
	(1552,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'382BCB91124445488AB658D754883B18','uiField','label','Attribute Value',NULL,'en_US','attrValue',0),
	(1553,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'68BE8C04E3CC4E91B80D87B839ACACF5','uiField','label','Preferred Language',NULL,'en_US','locale',0),
	(1554,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'4DBC11AF788F4FBE9FD17FAF9FCF118E','uiField','error','Language not specified',NULL,'en_US','locale',0),
	(1555,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'823D1D902614438FB1BEA53D8244BB5E','uiField','label','Field Name',NULL,'en_US','name',0),
	(1556,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'9A818CF670D84578BE85AD892B8532FB','uiField','error','Property already exists',NULL,'en_US','not_unique',0),
	(1557,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'E4DFF32D31B54E92896DD5197C723D14','uiField','data','Field Deleted',NULL,'en_US','removed',0),
	(1558,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'F4BEDFF2C91741C082DB56391C65DB70','uiField','data','Attribute Updated',NULL,'en_US','updated',0),
	(1559,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'C0BC37AEAC7A40E68980493103DD957C','uiField','label','Field Updated',NULL,'en_US','updated',0),
	(1560,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'0ADFB5C0585D4F1DB34F20F8C9E6AEC8','user','label','Address',NULL,'en_US','address',0),
	(1561,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'86C6FA7C4A7943A3A0B858859019563E','user','metaposition','40',NULL,'en_US','address',0),
	(1562,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'FAEBAF418C6E4A0C8FC28894A52A9AA3','user','error','Login Failure. Please provide valid login credentials.',NULL,'en_US','auth_login_validate',0),
	(1563,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'7D9C15F071264A16A21DF297D0E1BE54','user','label','Room',NULL,'en_US','building_room',0),
	(1564,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'C2AF18E038E44E3A929E1F53A7079F68','user','metaposition','30',NULL,'en_US','building_room',0),
	(1565,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'F50517028D174F728D9F1924B374B8FC','user','error','Room cannot be empty',NULL,'en_US','building_room',0),
	(1566,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'3937563D5C5D47C58F9C7366016214B9','user','constraint','NotEmpty',NULL,'en_US','building_room',0),
	(1567,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'1C0E2F837A6C4CB5B32114EC95E7DEB2','user','label','City',NULL,'en_US','city',0),
	(1568,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'209C84E4E0A8485686EB4FD9F8CB7939','user','error','City cannot be empty',NULL,'en_US','city',0);

INSERT INTO `uifield` (`id`, `created`, `updated`, `uuid`, `area`, `attrname`, `attrvalue`, `domain`, `locale`, `name`, `lastupdatebyuser`)
VALUES
	(1569,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'1797E6ABDDD34F8790B36B671E4FE8F0','user','constraint','NotEmpty',NULL,'en_US','city',0),
	(1570,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'1862C2146C76414E873F2953E04EE3C8','user','metaposition','50',NULL,'en_US','city',0),
	(1571,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'E6705A8D91C64F1E8BCD24D59173FFCC','user','label','Country',NULL,'en_US','country',0),
	(1572,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'6D4FCBC111034DDA85456835E468FE9C','user','error','Country cannot be empty',NULL,'en_US','country',0),
	(1573,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'AACEB009A707478BA23B7A358CA20609','user','control','select:${countries}:code:name',NULL,'en_US','country',0),
	(1574,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'C20188516EDD4C6CADF2372CB7E42226','user','constraint','NotEmpty',NULL,'en_US','country',0),
	(1575,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'8BCD18049554432CB24E3E2C3B117AA4','user','metaposition','70',NULL,'en_US','country',0),
	(1576,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'0F0BC568BB3F41038BBFA106196100FD','user','error','User was NOT created. Please fill in required fields.',NULL,'en_US','created',0),
	(1577,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'4D30B21281584994BDD95E7F901E1BE1','user','label','User was created. Consider assigning a role to this new user.',NULL,'en_US','created_success',0),
	(1578,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'0234AA103A814659ACF1CEC3856510B3','user','control','select:${departments}:departmentId:name',NULL,'en_US','departmentId',0),
	(1579,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'89B67168094D406EABECE591BA7D03FD','user','constraint','NotEmpty',NULL,'en_US','departmentId',0),
	(1580,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'9C2F7BA80F2E4DB6BA6FB6E3D70E3875','user','error','A department must be selected',NULL,'en_US','departmentId',0),
	(1581,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'63FA055674C748D0988FB9EE2C685A7A','user','label','Department',NULL,'en_US','departmentId',0),
	(1582,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'CC8EBA8E74F44BB5AB3A415E908D456F','user','metaposition','20',NULL,'en_US','departmentId',0),
	(1583,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'308FE331D0D146AF84E3502ACB7561F9','user','label','Email',NULL,'en_US','email',0),
	(1584,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'B02F28F9A13A4D3380C373A005F5C8D2','user','constraint','NotEmpty',NULL,'en_US','email',0),
	(1585,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'4108F6B0C782481E8F6C8441475CD657','user','error','Wrong email address format',NULL,'en_US','email',0),
	(1586,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'C5EF5B7FA4C44F41BECD3A4E15636993','user','label','Your email address has changed. An email has been sent to your new email address requesting confirmation. Please confirm by clicking the link in the email then <a href=\"../login.do\"/>click here to login</a>',NULL,'en_US','email_changed_p1',0),
	(1587,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'66701E9C5ED9444F9763EDF8DE0435FA','user','label','If you have not received an email, requesting confirmation, within a reasonable time period and suspect your email address may have been mis-typed, you may reset your email address by clicking <a href=\"requestEmailChange.do\">here</a>',NULL,'en_US','email_changed_p2',0),
	(1588,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'FB79C70F4E4840D79EDEDD8B1ADB810A','user','label','Your email address change has been confirmed successfully.',NULL,'en_US','email_change_confirmed',0),
	(1589,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'B07AE63D65944A4284D2545CBF8CD505','user','error','Email already exists in the database',NULL,'en_US','email_exists',0),
	(1590,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'1992BEB81D944B4E956310F97B1F225B','user','label','Fax',NULL,'en_US','fax',0),
	(1591,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'7D5199A5A4D740F498941B624952B251','user','metaposition','100',NULL,'en_US','fax',0),
	(1592,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'8DE4886F69544AA6AE8D82AF6E7E5662','user','label','First Name',NULL,'en_US','firstName',0),
	(1593,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'1D6F1F83E46C4AAA90A61A2B8802FF6C','user','constraint','NotEmpty',NULL,'en_US','firstName',0),
	(1594,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'8587FBBB32954E3CAC860B9C6D310323','user','error','First Name cannot be empty',NULL,'en_US','firstName',0),
	(1595,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'865724B33B8E42A1903E58702237CF47','user','label','Institution',NULL,'en_US','institution',0),
	(1596,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'F3C971A07CCC4A9490133D08D76822D8','user','error','Institution cannot be empty',NULL,'en_US','institution',0),
	(1597,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'229FCCB2518745F69202A8FDD3522F86','user','constraint','NotEmpty',NULL,'en_US','institution',0),
	(1598,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'258A5406178240999BA11E7BF94D5B6B','user','metaposition','10',NULL,'en_US','institution',0),
	(1599,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'2B36F381C98A45EDB6178E6F0B0894CC','user','label','Active',NULL,'en_US','isActive',0),
	(1600,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'F5D5AD35E2FE409ABCD6B464677A8826','user','label','Jobs',NULL,'en_US','jobs',0),
	(1601,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'B3A24C41EE294925B7BBD65ED040E2C8','user','label','View jobs belonging to user in these labs ',NULL,'en_US','job_list',0),
	(1602,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'C8D4EA8D1D22471F9C6DF14F71D823F6','user','label','Lab Users',NULL,'en_US','labusers',0),
	(1603,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'10BA3C9E9A934EB0ABD694352040653D','user','label','Last Name',NULL,'en_US','lastName',0),
	(1604,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'9D2E2D6D40804C819B7E93801E2BAE99','user','constraint','NotEmpty',NULL,'en_US','lastName',0),
	(1605,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'4903F3364DC045578D311506DF2C72A5','user','error','Last Name cannot be empty',NULL,'en_US','lastName',0),
	(1606,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'85058A829BB5451C91564ABBDEAE7B87','user','label','Preferred Language',NULL,'en_US','locale',0),
	(1607,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'437145B0C84E42BFA4E33D295294E5AF','user','constraint','NotEmpty',NULL,'en_US','locale',0),
	(1608,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'26118293BA644DF3825FB5E7EE4DB88D','user','error','Language cannot be empty',NULL,'en_US','locale',0),
	(1609,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'5683AA0961F94BF6A7BD99C61F91EB97','user','label','Login',NULL,'en_US','login',0),
	(1610,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'369A4962F71545AF9CFE0CFCED57A1AF','user','constraint','NotEmpty',NULL,'en_US','login',0),
	(1611,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'66862FFE60464B5BA2214BA0477F448A','user','error','Login cannot be empty',NULL,'en_US','login',0),
	(1612,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'681D0D230AF949E896DFE7F1FAD03F0F','user','error','Login name already exists in the database',NULL,'en_US','login_exists',0),
	(1613,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'4FBB8382320B46158E2D7A09B771657A','user','data','Submit',NULL,'en_US','mypassword',0),
	(1614,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'751950B9F2F54F3583158AB22B13F58F','user','error','Externally authenticated user cannot change password in WASP',NULL,'en_US','mypassword_cannotChange',0),
	(1615,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'E84673DDA0EC49319DFF9C73B556AC84','user','error','Your old password does NOT match the password in our database',NULL,'en_US','mypassword_cur_mismatch',0),
	(1616,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'E830DDE3C8B14BB4B2289690B15657CA','user','label','New Password Requirements:<br />At least 8 characters in length<br />Only letters & numbers permitted (no spaces, no underscore, etc.)<br />At least one letter and one number<br />',NULL,'en_US','mypassword_instructions',0),
	(1617,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'14B6257EFA4D456FA365F8652E2F9401','user','error','Please provide values for all fields',NULL,'en_US','mypassword_missingparam',0),
	(1618,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'B660F89C839C47A1BD2317A8F6A53EF0','user','label','New Password',NULL,'en_US','mypassword_newpassword1',0),
	(1619,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'3EBBE7233E514C9A9707861AA2C221E2','user','label','Confirm New Password',NULL,'en_US','mypassword_newpassword2',0),
	(1620,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'29EAC3E0653A4109BD25CEB1B1EAE49B','user','error','New password must be at least 8 characters, containing only letters and numbers, with at least one letter and number',NULL,'en_US','mypassword_new_invalid',0),
	(1621,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'E7CF1592435D4F7CB86BFC95F13764DD','user','error','The two entries for your NEW password are NOT identical',NULL,'en_US','mypassword_new_mismatch',0),
	(1622,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'57D8A7925CD44D529B281BD7BB37455C','user','error','Your old and new passwords may NOT be the same',NULL,'en_US','mypassword_nodiff',0),
	(1623,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'761B86D887264D62AB6BC99AA758AC73','user','label','Password Has Been Changed',NULL,'en_US','mypassword_ok',0),
	(1624,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'ADAD8970CEB24CCC8A0A158820418AC5','user','label','Old Password',NULL,'en_US','mypassword_oldpassword',0),
	(1625,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'956A14770A994FF89F12A0896B358E6A','user','label','Submit',NULL,'en_US','mypassword_submit',0),
	(1626,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'434DDA45BAE740CD834ABC94C07C7325','user','label','Password',NULL,'en_US','password',0),
	(1627,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'B9F23B7F59204746A395862DB935E2DD','user','constraint','NotEmpty',NULL,'en_US','password',0),
	(1628,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'4E52A38A1D4847CC858E8E2BD412F521','user','error','Password cannot be empty',NULL,'en_US','password',0),
	(1629,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'B21ED30563C940038906AEAC984BD438','user','label','Phone',NULL,'en_US','phone',0),
	(1630,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'125606063F1E439FB0F76EDF5C553B70','user','error','Phone cannot be empty',NULL,'en_US','phone',0),
	(1631,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'B32DEF4A9CDF435BB68F7A180F83433A','user','constraint','NotEmpty',NULL,'en_US','phone',0),
	(1632,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'294391FD567D42F4810E62DC60AABEBF','user','metaposition','90',NULL,'en_US','phone',0),
	(1633,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'8CA4316DD1164F0588347B7F9C0E95DE','user','label','Roles',NULL,'en_US','roles',0),
	(1634,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'B6401D3DE28F46F9A5CA4D1A2E9FDD54','user','label','Samples',NULL,'en_US','samples',0),
	(1635,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'F18A936958FE40F6900A79AD65DE4060','user','label','State',NULL,'en_US','state',0),
	(1636,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'661F7DECC14A4AFB84F978966DA52F56','user','error','State cannot be empty',NULL,'en_US','state',0),
	(1637,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'1E529ADD2B964DCEAC7CAF8AF5FCE963','user','control','select:${states}:code:name',NULL,'en_US','state',0),
	(1638,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'15C976B65DB7456DB6D39DDFCA022961','user','constraint','NotEmpty',NULL,'en_US','state',0),
	(1639,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'80AFB9859C604694AA9916E17596E95A','user','metaposition','60',NULL,'en_US','state',0),
	(1640,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'B6FFF72A2028429BB45D37730009D896','user','label','Title',NULL,'en_US','title',0),
	(1641,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'EBB76286395F4FA6B755A65D5C599138','user','error','Title cannot be empty',NULL,'en_US','title',0),
	(1642,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'179D4289A9224803B36715F9A8973C01','user','constraint','NotEmpty',NULL,'en_US','title',0),
	(1643,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'013324A4DCD84887A30233DD832D504A','user','control','select:Prof:Prof;Dr:Dr;Mr:Mr;Ms:Ms;Mrs:Mrs;Miss:Miss',NULL,'en_US','title',0),
	(1644,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'5C01EA1B8D1740179E0A5FBAE8C751CE','user','metaposition','1',NULL,'en_US','title',0),
	(1645,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'BA63F2C1C8174056945C21337B96D91D','user','error','User was NOT updated. Please fill in required fields.',NULL,'en_US','updated',0),
	(1646,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'3E236E8433EE40C3B8CDDE532EE418D0','user','error','User was NOT updated.',NULL,'en_US','updated_fail',0),
	(1647,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'53EE0599BFA64C6298B1AC0BA9D20B06','user','label','User was updated',NULL,'en_US','updated_success',0),
	(1648,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'1D37427448F5450984DEE11D69C6A5F7','user','label','Your login and email address has changed. An email has been sent to your new email address requesting confirmation. Please confirm by clicking the link in the email then <a href=\"../login.do\"/>click here to login</a>',NULL,'en_US','userloginandemail_changed_p1',0),
	(1649,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'501B7065E9D2468EA7681ED20CD7E4FE','user','label','If you have not received an email, requesting confirmation, within a reasonable time period and suspect your email address may have been mis-typed, you may reset your email address by clicking <a href=\"requestEmailChange.do\">here</a>',NULL,'en_US','userloginandemail_changed_p2',0),
	(1650,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'034093FAE00742D7ADBEF79B7D470CB4','user','label','Your login has changed. Please <a href=\"../login.do\"/>click here to login</a>',NULL,'en_US','userlogin_changed_p1',0),
	(1651,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'A4515C5303084A3A91B955F611D14099','user','label','List of Users',NULL,'en_US','user_list',0),
	(1652,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'9E3E28EE3A13480C94DC1298CEC24B59','user','label','Lab Members',NULL,'en_US','lab_member_list',0),
	(1653,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'4ED7E945372B43F1966B6D54A4D0C1FC','user','label','Zip',NULL,'en_US','zip',0),
	(1654,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'5E0265C3D52541428D55931672162182','user','error','Zip cannot be empty',NULL,'en_US','zip',0),
	(1655,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'D2291980D98F4A0DA590B6603B318D0F','user','metaposition','80',NULL,'en_US','zip',0),
	(1656,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'A1A143EBFA2845D3BF2BF4D391EF617A','userDetail','label','Cancel',NULL,'en_US','cancel',0),
	(1657,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'4CE1C7B82F1D40A1935EFFBB4ECFBE82','userDetail','label','Change Password',NULL,'en_US','change_password',0),
	(1658,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'2BF9903FABE842C8AF583372C6ED6686','userDetail','label','Edit',NULL,'en_US','edit',0),
	(1659,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'0AE223B891C748C9BDD325F34BE41BA1','userDetail','label','Edit (as other)',NULL,'en_US','edit_as_other',0),
	(1660,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'1BE9AA0AA4BC4EB78E086C5CCDD11582','userDetail','label','Jobs',NULL,'en_US','jobs',0),
	(1661,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'A796482008974821AAB5C20C14A9C9E2','userDetail','label','Lab Users',NULL,'en_US','lab_users',0),
	(1662,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'D4F0134C7DE247E8BCF8475BB3C5F7B0','userDetail','label','Samples',NULL,'en_US','samples',0),
	(1663,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'2D50EA3B6B5C4C8CBB792EA67940E712','userDetail','label','Save Changes',NULL,'en_US','save',0),
	(1664,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'A3B21160749D4DA994242B3EB60F55D1','userPending','error','Invalid action. Must be approve or reject only',NULL,'en_US','action',0),
	(1665,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'03CFD2D9CA554CCB8CA6183B4340FD1E','userPending','label','APPROVE',NULL,'en_US','action_approve',0),
	(1666,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'2A907363883B4B62991E62837E4206BB','userPending','label','REJECT',NULL,'en_US','action_reject',0),
	(1667,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'A15FEC6C6FA441479296055C3F75433D','userPending','label','Street',NULL,'en_US','address',0),
	(1668,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'4355F4D0351D4B5FBDF1460B67C557D8','userPending','metaposition','40',NULL,'en_US','address',0),
	(1669,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'46D5F557D7F64DFF95A4D110593C923C','userPending','label','User account application successfully approved',NULL,'en_US','approved',0),
	(1670,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'ECDB2FE3843E4432A706ECA5DB497DE3','userPending','constraint','NotEmpty',NULL,'en_US','building_room',0),
	(1671,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'A9CE59D6BA084C90B459D424FAF767C0','userPending','error','Room cannot be empty',NULL,'en_US','building_room',0),
	(1672,'2013-08-05 19:04:22','2013-08-05 19:04:22',X'9C6BCC55184548908AC37D85FA73B09C','userPending','label','Building / Room',NULL,'en_US','building_room',0),
	(1673,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'E54C428F73244451A08F8576CB10101F','userPending','metaposition','30',NULL,'en_US','building_room',0),
	(1674,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'6704ED708C19444DA3F1634678AC8680','userPending','error','Captcha text incorrect',NULL,'en_US','captcha',0),
	(1675,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'3D051C20B2A74F53922200E26F8A96C0','userPending','label','Captcha text',NULL,'en_US','captcha',0),
	(1676,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'13338A8A1E634AA28CB21AFAD685B1F3','userPending','label','Your Email Address',NULL,'en_US','email',0),
	(1677,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'94F641FF61864866A3F1304DA0E1D10F','userPending','error','Must be correctly formatted',NULL,'en_US','email',0),
	(1678,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'438DED5174B04C309FEFE35290BC189B','userPending','label','Your email address is confirmed. The Principal Investigator of the lab to which you applied has been requested (via email) to approve your application to join their lab. Once approved, you will be informed via email and you will be permitted to log in. If your account is not activated in good time, please contact the PI.',NULL,'en_US','emailconfirmed',0),
	(1679,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'A5972E14BBBF4F1EAFE2A30E90571827','userPending','label','Thank you for applying for a new user account. You have been sent an email with instructions describing how to quickly confirm your email address. Your registration cannot proceed until your email address has been confirmed.',NULL,'en_US','emailsent',0),
	(1680,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'BD8AE613D30243B8A155E2829C1E4A51','userPending','error','Email already exists in the database',NULL,'en_US','email_exists',0),
	(1681,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'BC3DEB50DB904AD2A96DB252529433CD','userPending','error','Authentication Failed (Login Name or Password incorrect)',NULL,'en_US','external_authentication',0),
	(1682,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'B8354EE373CF433E8E54965CC9B6C7BA','userPending','label','Fax',NULL,'en_US','fax',0),
	(1683,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'B0CC6632936B44359FAA2B5E09C882EA','userPending','metaposition','100',NULL,'en_US','fax',0),
	(1684,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'7B6C178953D04D2B817BFF33480BC4FD','userPending','label','First Name',NULL,'en_US','firstName',0),
	(1685,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'EC063D22C8CC4CB68EED7A0E27AFB49F','userPending','error','First Name cannot be empty',NULL,'en_US','firstName',0),
	(1686,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'52B5A067C6824182A55A2DBE2BF095D1','userPending','label','Register As A New WASP User & Apply To Join An Existing Laboratory',NULL,'en_US','form_header',0),
	(1687,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'2F36FFDA95B24984A34E2786E8FE5E92','userPending','label','If you do NOT already have a WASP account AND you wish to register and join an existing lab, then complete this form and provide the email address of the Principal Investigator (PI) whose lab you wish to join.<br /><br />PLEASE DO NOT USE THIS FORM TO REGISTER AS A NEW PRINCIPAL INVESTIGATOR!',NULL,'en_US','form_instructions',0),
	(1688,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'139C4B949F2A40F9A3BFA1A3CA54B290','userPending','error','Lab id mismatch with user-pending id',NULL,'en_US','labid_mismatch',0),
	(1689,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'41ED8020F4B442E696D37654B2A906F9','userPending','label','Last Name',NULL,'en_US','lastName',0),
	(1690,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'802706B4479E4238BD278B33763C5BA5','userPending','error','Last Name cannot be empty',NULL,'en_US','lastName',0),
	(1691,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'1C9B882A7AA44A549096D383EEFA0C65','userPending','constraint','NotEmpty',NULL,'en_US','locale',0),
	(1692,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'82A090E4EC7E40A6BC3EAE44CB3E2166','userPending','label','Preferred Language',NULL,'en_US','locale',0),
	(1693,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'E714D43005D34148A7224E2BDB919DBD','userPending','error','A language must be selected',NULL,'en_US','locale',0),
	(1694,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'FF2DBF290A914E528C16E9984BAE5F04','userPending','label','Login',NULL,'en_US','login',0),
	(1695,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'B0E3629486304FB79FF16A03DA504BF3','userPending','error','Login cannot be empty',NULL,'en_US','login',0),
	(1696,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'CBA4A2839CE142FFA695F42FF96CC3B0','userPending','error','Login name already exists in the database',NULL,'en_US','login_exists',0),
	(1697,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'5E56B23D6BCA4D4F986420C5ACF4D051','userPending','label','No spaces permitted in login',NULL,'en_US','login_instructions_above',0),
	(1698,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'ADC1F527C9E841089D36344CB8676B48','userPending','error','Contains invalid characters',NULL,'en_US','login_malformed',0),
	(1699,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'FC800109480944DF8FE703290D13C475','userPending','label','There are currently no pending users awaiting approval',NULL,'en_US','no_pending_users',0),
	(1700,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'06F957082BE3474E86FC421493E5A7BE','userPending','label','New User',NULL,'en_US','page',0),
	(1701,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'1CC9E88D094B443A804019958BA06BBD','userPending','label','Password',NULL,'en_US','password',0),
	(1702,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'F40C7F94F5AE4AF39E6CF8197C2B80A8','userPending','error','Password cannot be empty',NULL,'en_US','password',0),
	(1703,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'12FC998078CE49B58821FD2A704CA573','userPending','label','Re-confirm Password',NULL,'en_US','password2',0),
	(1704,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'8EFB26A43D0D4247AC585BDA7D52AD6B','userPending','error','Re-confirm password cannot be empty',NULL,'en_US','password2',0),
	(1705,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'4F94D744478C48149F87F3E8987CAC28','userPending','error','Password must be at least 8 characters, containing only letters and numbers, with at least one letter and number',NULL,'en_US','password_invalid',0),
	(1706,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'FDA45AEC06A9439B9B1CDE0010267991','userPending','error','The two entries for your password are NOT identical',NULL,'en_US','password_mismatch',0),
	(1707,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'1366C8D8FB5A47E7AD7AF4A1066A2D0D','userPending','label','Phone',NULL,'en_US','phone',0),
	(1708,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'7117ED3BB67747F18EE9E977595D83F3','userPending','error','Phone cannot be empty',NULL,'en_US','phone',0),
	(1709,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'9DC407186AE44927A3A4DE71EB5FDC33','userPending','constraint','NotEmpty',NULL,'en_US','phone',0),
	(1710,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'6086D128B04B4812976A7CCBF4015CD2','userPending','metaposition','90',NULL,'en_US','phone',0),
	(1711,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'4AC2DE6115954477B7B78003B999CD31','userPending','label','Email Address of PI Whose<br />Lab You Wish To Join',NULL,'en_US','primaryuserid',0),
	(1712,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'C9413BE6DEE84E31B881E25E79E6DFB9','userPending','error','Email Address of the Lab PI cannot be empty',NULL,'en_US','primaryuserid',0),
	(1713,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'B4E938D809F74F56B85B97045AC9153C','userPending','constraint','isValidPiId',NULL,'en_US','primaryuserid',0),
	(1714,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'895A3083B0CE433384AE72532523A990','userPending','metaposition','500',NULL,'en_US','primaryuserid',0),
	(1715,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'5BD49CE7BE784B16A24A656EC334A749','userPending','error','Not an active Email Address of a Lab PI',NULL,'en_US','primaryuserid_notvalid',0),
	(1716,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'250EA458325741ECAEEA4D18F2BC8BFD','userPending','label','User account application successfully rejected',NULL,'en_US','rejected',0),
	(1717,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'DB45F329589747D2A92AED10C900E16E','userPending','error','Pending user is already approved or rejected',NULL,'en_US','status_not_pending',0),
	(1718,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'84FBDF6D533A41069EA06A1F168798E2','userPending','label','Apply for Account',NULL,'en_US','submit',0),
	(1719,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'EDB1A9DF71B748FF91C5DAC20EC9B0FC','userPending','label','Title',NULL,'en_US','title',0),
	(1720,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'2CBD58558E2244F1961D157CDC12DABE','userPending','error','Title cannot be empty',NULL,'en_US','title',0),
	(1721,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'498D84AE6E2E4C468BFF0A0E2E1AA726','userPending','constraint','NotEmpty',NULL,'en_US','title',0),
	(1722,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'34DE2039B56F48FF92B5D3BE385C570B','userPending','control','select:Prof:Prof;Dr:Dr;Mr:Mr;Ms:Ms;Mrs:Mrs;Miss:Miss',NULL,'en_US','title',0),
	(1723,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'5C3EF92FC5F144E3B5532774064FEA41','userPending','metaposition','5',NULL,'en_US','title',0),
	(1724,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'CF2362E9A52E4BCD808C492B43512A6D','wasp','label','WASP',NULL,'en_US','authentication',0),
	(1725,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'5C362D6BEFD04C1B87F5431392B41FD6','wasp','label','-- select --',NULL,'en_US','default_select',0),
	(1726,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'87614B04E807475688EA571D975EE570','wasp','error','Unexpected error. Last command was NOT executed.',NULL,'en_US','unexpected_error',0),
	(1727,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'3483C934FC124981BAA3CBE014C65042','wasp','error','Parameter was unexpectedly not an integer.',NULL,'en_US','parseint_error',0),
	(1728,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'4AF2F05006CD4757A047169E5D278D1F','wasp','error','Insufficient privileges.',NULL,'en_US','permission_error',0),
	(1729,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'3926EBEFB4824DFFB24B127E47FBCD5A','wasp','error','Failed to send update message',NULL,'en_US','integration_message_send',0),
	(1730,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'14F9F7C426D14B11904FF4E7AC46CB7D','workflow','label','Cancel',NULL,'en_US','cancel',0),
	(1731,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'396F5CBCA88047A5A467358BEFB09097','workflow','label','Resources and Software',NULL,'en_US','configure',0),
	(1732,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'C4A36098882146958FFDAC08E753E69E','workflow','label','Is Active?',NULL,'en_US','isActive',0),
	(1733,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'5981CE3EAE38414880855984646B99FF','workflow','label','List of Workflows',NULL,'en_US','listname',0),
	(1734,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'BB71E45A6A40441F8CA2877A7D3E0228','workflow','error','At least one option must be checked for each resource or software type',NULL,'en_US','missing_resource_type',0),
	(1735,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'6A998A9A3B244AAB939FB849FEB9A28D','workflow','label','Workflow Name',NULL,'en_US','name',0),
	(1736,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'12A9D756921E48CB91A81E4DDE8CE68E','workflow','error','At least one option must be checked for each parameter presented for a selected resource',NULL,'en_US','non-configured_parameter',0),
	(1737,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'5747491E5C3147AA90F3E03043E51285','workflow','label','Save Choices',NULL,'en_US','submit',0),
	(1738,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'25568B6622C9456B9E1891B9D8573CC2','workflow','label','Workflow Id',NULL,'en_US','workflowId',0),
	(1739,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'EF2EA8BB4A2449109A02F78CC63BC695','contentTypeMap','data','application/octet-stream',NULL,'en_US','arc',0),
	(1740,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'D6198E38071A4BF2A517FB7400BE0422','contentTypeMap','data','application/octet-stream',NULL,'en_US','bin',0),
	(1741,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'776C899CE6864C578C7DB94FDF80E01F','contentTypeMap','data','application/octet-stream',NULL,'en_US','dump',0),
	(1742,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'3015CFE004BD4A2BA4F1D6E18C4934E1','contentTypeMap','data','application/x-dvi',NULL,'en_US','dvi',0),
	(1743,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'CA6AD7B86A6F4E00BE9374C43B1CDBD6','contentTypeMap','data','text/x-setext',NULL,'en_US','etx',0),
	(1744,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'FCB78956287C4FDBA75995F01B5E5F04','contentTypeMap','data','application/octet-stream',NULL,'en_US','exe',0),
	(1745,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'3B330C698DB24433ADE364CFA429C9B3','contentTypeMap','data','image/vnd.fpx',NULL,'en_US','fpix',0),
	(1746,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'13DFBA11CC2A4F99B3A58D99995E0139','contentTypeMap','data','image/vnd.fpx',NULL,'en_US','fpx',0),
	(1747,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'C3A1C868E345465895DA5606D6375C9B','contentTypeMap','data','application/octet-stream',NULL,'en_US','gz',0),
	(1748,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'2004876381AF4BCB8210CF1BBAF198CE','contentTypeMap','data','application/octet-stream',NULL,'en_US','hqx',0),
	(1749,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'F861EA5A2EA04FE9AA2CB892D5F8C7AB','contentTypeMap','data','image/ief',NULL,'en_US','ief',0),
	(1750,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'2355CF63A34F4132A7B9A79FF6A885E3','contentTypeMap','data','application/x-latex',NULL,'en_US','latex',0),
	(1751,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'03ABE356E8DA49FA8A3A85ACE848BB13','contentTypeMap','data','application/octet-stream',NULL,'en_US','lib',0),
	(1752,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'C130A75DDA21414EB10721513FB5508E','contentTypeMap','data','application/x-troff-man',NULL,'en_US','man',0),
	(1753,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'667C52E4B87C4B16BBDA06149D7F0F3F','contentTypeMap','data','application/x-troff-me',NULL,'en_US','me',0),
	(1754,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'9A564EC2FBBD4CDB85BFF00D544D19EE','contentTypeMap','data','message/rfc822',NULL,'en_US','mime',0),
	(1755,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'20E6D2A8FC9D4D2AAD9665E313DF5222','contentTypeMap','data','video/quicktime',NULL,'en_US','mov',0),
	(1756,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'9E2919FC5F244E488E3CD1B2FAC68615','contentTypeMap','data','video/x-sgi-movie',NULL,'en_US','movie',0),
	(1757,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'262B97B08A4044489AB88BAED9801A13','contentTypeMap','data','application/x-troff-ms',NULL,'en_US','ms',0),
	(1758,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'28F7276F140947D981C3B735FD2A7219','contentTypeMap','data','video/x-sgi-movie',NULL,'en_US','mv',0),
	(1759,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'6A03FFC67B7647A29E782DCAB3DE4D01','contentTypeMap','data','application/octet-stream',NULL,'en_US','obj',0),
	(1760,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'F7A63B6F2E594C17BDD1548ED5EA85F5','contentTypeMap','data','application/oda',NULL,'en_US','oda',0),
	(1761,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'80442FA93DC34EA3B6074DED69D349A9','contentTypeMap','data','image/x-portable-bitmap',NULL,'en_US','pbm',0),
	(1762,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'2E46213EDD384C899F53E27A39E1FE35','contentTypeMap','data','application/pdf',NULL,'en_US','pdf',0),
	(1763,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'C793843141A147DE9FD2E9F7DA785A42','contentTypeMap','data','image/x-portable-graymap',NULL,'en_US','pgm',0),
	(1764,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'C19D967727964069928EAFB55F032281','contentTypeMap','data','image/x-portable-anymap',NULL,'en_US','pnm',0),
	(1765,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'5605AE844B6C48BBA5DA9805421A4E06','contentTypeMap','data','image/x-portable-pixmap',NULL,'en_US','ppm',0),
	(1766,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'072DE2F4EECD49F68E8E7F49E16B7A0B','contentTypeMap','data','video/quicktime',NULL,'en_US','qt',0),
	(1767,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'804793BEFBCC4486A29FEC69EE14AA0A','contentTypeMap','data','image/x-cmu-rast',NULL,'en_US','ras',0),
	(1768,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'3A134E2B0D734639972A50C5320F0A67','contentTypeMap','data','image/x-rgb',NULL,'en_US','rgb',0),
	(1769,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'2AF7477CAEB446AF88F83B39C25B9C41','contentTypeMap','data','application/x-troff',NULL,'en_US','roff',0),
	(1770,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'3F7734B6F5334382894981642D5233A5','contentTypeMap','data','application/octet-stream',NULL,'en_US','saveme',0),
	(1771,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'57A2A2DDE2FB4EBBB93F1E721A109B8D','contentTypeMap','data','application/x-wais-source',NULL,'en_US','src',0),
	(1772,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'876E45DFD4D349F78753151B728E436E','contentTypeMap','data','application/x-troff',NULL,'en_US','t',0),
	(1773,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'75C08CA1B7DE4C8187CF126D3F11FBF2','contentTypeMap','data','application/x-tex',NULL,'en_US','tex',0),
	(1774,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'9156DE2684E94B7E857B5412762222A0','contentTypeMap','data','application/x-texinfo',NULL,'en_US','texi',0),
	(1775,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'117A9691082648A2A8947D58F2FE7F1D','contentTypeMap','data','application/x-texinfo',NULL,'en_US','texinfo',0),
	(1776,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'932D7D398B844FEA902869CF1BB45BDA','contentTypeMap','data','application/x-troff',NULL,'en_US','tr',0),
	(1777,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'007A8EC86F4E4A33B7AAD7FDE5EBA50A','contentTypeMap','data','text/tab-separated-values',NULL,'en_US','tsv',0),
	(1778,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'67C10FCA5FF64D959EF36F2BDBBECF16','contentTypeMap','data','application/x-wais-source',NULL,'en_US','wsrc',0),
	(1779,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'61CD169EC45747A690CB0047C7595242','contentTypeMap','data','image/x-xbitmap',NULL,'en_US','xbm',0),
	(1780,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'D428BF757919480895F7FEA1E7DEC8C9','contentTypeMap','data','application/xml',NULL,'en_US','xml',0),
	(1781,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'4359B6C63B31489EA3F5ADA45D184C3F','contentTypeMap','data','image/x-xbitmap',NULL,'en_US','xpm',0),
	(1782,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'67D5E75F21314A7B8C0429A4B90756D4','contentTypeMap','data','image/x-xwindowdump',NULL,'en_US','xwd',0),
	(1783,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'B3267FFCC5E449289EFD73F83ADBD352','contentTypeMap','data','application/octet-stream',NULL,'en_US','zip',0),
	(1784,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'8A51EF6D743045C48CAF5FE9CC74E98E','wasp','data','FALSE',NULL,'en_US','isAuthenticationExternal',0),
	(1785,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'8202BC1DB74847C0B614AA2231F804C9','pageTitle','label','FastQC Plugin Description',NULL,'en_US','babraham/fastqc/description',0),
	(1786,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'D3F3CD9B533848EF84356C476F4513D2','fastqc','label','Babraham Bioinformatics FastQC Software Plugin',NULL,'en_US','hyperlink',0),
	(1787,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'95FCB26F24804370BA34B49C9360DAA9','fastqc','label','If you can read this text your Babraham Bioinformatics FastQC plugin is successfully installed',NULL,'en_US','maintext',0),
	(1788,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'C84DED1627D7478087A0878B77C8E57D','pageTitle','label','FastQ Screen Plugin Description',NULL,'en_US','babraham/fastqscreen/description',0),
	(1789,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'6EE842318601464FBFEA942A2C51BBE2','fastqscreen','label','Babraham Bioinformatics FastQ Screen Software Plugin',NULL,'en_US','hyperlink',0),
	(1790,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'4693AB3277924938B4311D60170A368C','fastqscreen','label','If you can read this text your Babraham Bioinformatics FastQ Screen plugin is successfully installed',NULL,'en_US','maintext',0),
	(1791,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'5632A17FF7BA4C32B3A3EC52523B6549','pageTitle','label','Wasp ChIP-Seq Plugin Description',NULL,'en_US','waspchipseq/description',0),
	(1792,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'DBA32D9A2EAA4C08B4132BC61757DA48','waspChipSeq','label','If you can read this text your plugin is successfully installed',NULL,'en_US','maintext',0),
	(1793,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'58834ABC2799497E8A226A697D3CF458','waspChipSeq','label','Wasp ChIP-Seq Plugin DNA Sequencing Plugin',NULL,'en_US','hyperlink',0),
	(1794,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'55BC010EA987488CA44905FF34825853','pageTitle','label','Generic DNA Seq Plugin Description',NULL,'en_US','genericdnaseq/description',0),
	(1795,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'56DCD8385D8A48C0B78E6BCE448750F1','waspGenericDnaSeq','label','If you can read this text your plugin is successfully installed',NULL,'en_US','maintext',0),
	(1796,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'CA6E15D3A5FF43209D5EFC9E6821BEF5','waspGenericDnaSeq','label','Wasp Generic DNA Sequencing Plugin',NULL,'en_US','hyperlink',0),
	(1797,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'3D3609F3D0934E79BD709DBC5F0DEB86','pageTitle','label','Wasp Help-Tag Plugin Description',NULL,'en_US','wasphelptag/description',0),
	(1798,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'64A28CAAB1314E06A0C452CE00F83859','waspHelpTag','label','If you can read this text your plugin is successfully installed',NULL,'en_US','maintext',0),
	(1799,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'1ECF655EB8F64448B1CE9CB50B7BB861','waspHelpTag','label','Wasp Help Tag Plugin',NULL,'en_US','hyperlink',0),
	(1800,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'0CCFB6CB8C52447AA5A5D880D81CEDDF','pageTitle','label','Wasp Illumina Plugin Description',NULL,'en_US','waspillumina/description',0),
	(1801,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'C1959FA98C4148339E38A918D7FDFA5E','waspIlluminaPlugin','label','If you can read this text your plugin is successfully installed',NULL,'en_US','maintext',0),
	(1802,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'9E2D83DF69DC41F59286274E72EEBD0C','waspIlluminaPlugin','label','Illumina Run QC',NULL,'en_US','taskRunQc',0),
	(1803,'2013-08-05 19:04:23','2013-08-05 19:04:23',X'53B5987BE4074DCF973C2D3BAAAFFBA3','waspIlluminaPlugin','label','Wasp Illumina Plugin',NULL,'en_US','hyperlink',0),
	(1804,'2013-08-05 19:04:23','2013-08-05 19:04:24',X'EB17B0A9E437492788CAD23A95A5752B','pageTitle','label','Wasp Illumina QC tasks',NULL,'en_US','wasp-illumina/postrunqc/list',0),
	(1805,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'198C4462F2A5403BBA3B85C01C40E76A','pageTitle','label','Create/Update Sequence Run',NULL,'en_US','wasp-illumina/flowcell/createupdaterun',0),
	(1806,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'463F0EADB131471D95F9F554E3EDCEF7','pageTitle','label','Illumina OLB Stats: Focus Quality Charts',NULL,'en_US','wasp-illumina/postrunqc/displayfocusqualitycharts',0),
	(1807,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'1F1038BA693C40BAA84C61012C356386','pageTitle','label','Illumina OLB Stats: Intensity Charts',NULL,'en_US','wasp-illumina/postrunqc/displayinstensitycharts',0),
	(1808,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'7438956D29084227AFE394B4DE49D404','pageTitle','label','Illumina OLB Stats: Pass Filter Base Quality Charts',NULL,'en_US','wasp-illumina/postrunqc/displaynumgt30charts',0),
	(1809,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'ED530AE07E224BC384F17DA9CF64D1C7','pageTitle','label','Illumina OLB Stats: Cluster Density Charts',NULL,'en_US','wasp-illumina/postrunqc/displayclusterdensitychart',0),
	(1810,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'B62AF833FA444B21ABC2ED9E4A79592D','pageTitle','label','Illumina OLB Stats: Finalize Validation',NULL,'en_US','wasp-illumina/postrunqc/updatequalityreport',0),
	(1811,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'277BB5B7A64F48318EE8511CE833B9D6','pageTitle','label','Flowcell / Sequence Run',NULL,'en_US','wasp-illumina/flowcell/showflowcell',0),
	(1812,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'DF069E15E05545ABA6FEA464F629F251','waspIlluminaPlugin','error','There was a problem interpreting the posted form parameters',NULL,'en_US','formParameter',0),
	(1813,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'4D00BBE4772447DBBB70D17D1AD0A462','waspIlluminaPlugin','error','Flowcell is of invalid \"sample\" type',NULL,'en_US','notpu',0),
	(1814,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'64A6E41A5A2F43EC89DBE33D9B2B820C','waspIlluminaPlugin','error','Failed to update database with QC data',NULL,'en_US','update',0),
	(1815,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'FE24BF60BBEA41D994FD47AA4B88E135','waspIlluminaPlugin','error','Failed get all QC data from database',NULL,'en_US','qcRetrieval',0),
	(1816,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'49DD261683494E209F9BFBEBC927E8AE','waspIlluminaPlugin','label','Lane',NULL,'en_US','updateQualityReport_lane',0),
	(1817,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'44F8716DDFE74273A65CDFA03F8EDAEC','waspIlluminaPlugin','label','Focus Quality',NULL,'en_US','updateQualityReport_focus',0),
	(1818,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'4EA7ACF445CB48A7A61E560F2B5596E7','waspIlluminaPlugin','label','Intensity Quality',NULL,'en_US','updateQualityReport_intensity',0),
	(1819,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'12A90EE53D0348F193CDBC96B165454E','waspIlluminaPlugin','label','Pass Filter Base Quality',NULL,'en_US','updateQualityReport_baseQc',0),
	(1820,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'310CA0853BB241418352A7D460614748','waspIlluminaPlugin','label','Cluster Density',NULL,'en_US','updateQualityReport_clusterDensity',0),
	(1821,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'4844D621B85B4C1CBE7F72CFBEEB5F38','waspIlluminaPlugin','label','Final Decision on Lane',NULL,'en_US','updateQualityReport_finalDescision',0),
	(1822,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'727769DBF1ED4F979D931D978F864C3E','waspIlluminaPlugin','label','Comments on Decision<br />(will display to users)',NULL,'en_US','updateQualityReport_comments',0),
	(1823,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'31B249DD8F1B4A2797779AFB699EDE9C','waspIlluminaPlugin','label','Failed to send message indicating QC complete.',NULL,'en_US','updateQcfailed',0),
	(1824,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'FB5C14C818604A8DB4571B6953F0DD11','waspIlluminaPlugin','label','Run Quality Control Completed Successfully',NULL,'en_US','updateQcsuccess',0),
	(1825,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'46D6AF6289D5403CB66BE1AB4AC5E34F','waspIlluminaPlugin','label','Illumina OLB Stats: Finalize Validation for ',NULL,'en_US','updateQualityReport_title',0),
	(1826,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'464E1721BAAA47319C3C7EE344C570DB','waspIlluminaPlugin','label','Please review this summary of your responses in the table below and make a decision whether or not to accept or reject each lane. Note that:<ul><li>Libraries on rejected lanes will not be analysed. </li><li>It is mandatory to write comments on the decision if a lane is rejected.</li><li>The decision and comments will be viewable by users.</li></ul>',NULL,'en_US','updateQualityReport_instructions',0),
	(1827,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'5ED3C2CDE87A4BA8AF4394ED614D10A3','waspIlluminaPlugin','label','Submit Run QC',NULL,'en_US','updateQualityReport_submitButton',0),
	(1828,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'539BF6AE16E24163B0CA9E0D5963423D','waspIlluminaPlugin','label','Page Loading',NULL,'en_US','displayQc_loadingTitle',0),
	(1829,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'1C87DCB152BF41A680D9A00CE8187BCF','waspIlluminaPlugin','label','Warning',NULL,'en_US','displayQc_warningTitle',0),
	(1830,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'0AFFF9EBA6ED499FB324598056209779','waspIlluminaPlugin','label','Please be patient while this page loads. All QC charts are being pre-loaded so this may take a few seconds.',NULL,'en_US','displayQc_message',0),
	(1831,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'B08E2503DED64716B005AD7ABA8C26D7','waspIlluminaPlugin','error','You must choose either \"Pass\" or \"Fail\" for ALL lanes. You have not yet scored lanes ',NULL,'en_US','displayQc_noChoose',0),
	(1832,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'5D2AC228543B42DB8E592D8722A8AD3D','waspIlluminaPlugin','error','You must write a comment for all failed lanes: ',NULL,'en_US','displayQc_noComment',0),
	(1833,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'A55735F882F1494EA9D9BF202E62FF81','waspIlluminaPlugin','label','Lane ',NULL,'en_US','displayQc_lane',0),
	(1834,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'EB951CD436A242C3963FC80498CE3B65','waspIlluminaPlugin','label','Comments: ',NULL,'en_US','displayQc_comments',0),
	(1835,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'7A32340B1E0648128990ACFA4FBF6DC3','waspIlluminaPlugin','label','Continue',NULL,'en_US','displayQc_continue',0),
	(1836,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'C796B5B620E7497684C7E53E27787428','waspIlluminaPlugin','label','Cancel',NULL,'en_US','displayQc_cancel',0),
	(1837,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'870C0CC16A9D4D0396AA109EFC80CF0D','waspIlluminaPlugin','label','Pass',NULL,'en_US','displayQc_pass',0),
	(1838,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'B8FA4B439AD24196A6522ABE1823DF0B','waspIlluminaPlugin','label','Fail',NULL,'en_US','displayQc_fail',0),
	(1839,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'7A90703730F14672B65D72AED8C3C544','waspIlluminaPlugin','label','Cycle Number: ',NULL,'en_US','displayQc_cycle',0),
	(1840,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'53FE3506BEA9443CBF88173CE04C4051','waspIlluminaPlugin','error','<p>You must choose either \"Accept\" or \"Reject\" for ALL lanes. You have not yet scored lanes ',NULL,'en_US','updateQc_noChoose',0),
	(1841,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'A3D8D4E71266499AA40942291231584E','waspIlluminaPlugin','error','<p>You must write comments for rejected lanes ',NULL,'en_US','updateQc_noComment',0),
	(1842,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'298C4F1C210A4E99BA333A724DC64281','waspIlluminaPlugin','label','Accept',NULL,'en_US','updateQualityReport_accept',0),
	(1843,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'7821B0562A0D451BBF9D5DB9ABC1121F','waspIlluminaPlugin','label','Reject',NULL,'en_US','updateQualityReport_reject',0),
	(1844,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'6A12E37AAB394069AB5BDD6AA1D7E92F','waspIlluminaPlugin','label','Illumina OLB Stats: Focus Quality For ',NULL,'en_US','focusQc_title',0),
	(1845,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'9DD9B047BDAC47E4B3C36D9D2F70A1E8','waspIlluminaPlugin','label','Assessment of Lane Focus Quality',NULL,'en_US','focusQc_dialogTitle',0),
	(1846,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'332FA51A5EF84CED942EA7C5C51E55D3','waspIlluminaPlugin','label','Please click either \"Pass\" or \"Fail\" for each lane based on your interpretation of the FOCUS QUALITY charts only, then click the \"Continue\" button.',NULL,'en_US','focusQc_dialogMessage',0),
	(1847,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'BE5EC501A8B8448B83EBDEB85E483BAC','waspIlluminaPlugin','label','Illumina OLB Stats: Intensity Charts For ',NULL,'en_US','intensityQc_title',0),
	(1848,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'D80D4CD3F2124C1F8FF54D186D115B00','waspIlluminaPlugin','label','Assessment of Lane Intensity',NULL,'en_US','intensityQc_dialogTitle',0),
	(1849,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'DA78DFA2165648E7909193AF6AA0BBA9','waspIlluminaPlugin','label','Please click either \"Pass\" or \"Fail\" for each lane based on your interpretation of the LANE INTENSITY charts only, then click the \"Continue\" button.',NULL,'en_US','intensityQc_dialogMessage',0),
	(1850,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'AC70C89FC2F24B4B82B6EAC93F682145','waspIlluminaPlugin','label','Illumina OLB Stats: Pass Filter Base Quality Charts For ',NULL,'en_US','numGT30Qc_title',0),
	(1851,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'655C6E4038644335A456EBD7C26CE2E5','waspIlluminaPlugin','label','Assessment of Base Quality Score',NULL,'en_US','numGT30Qc_dialogTitle',0),
	(1852,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'4C07B859D24F4A11B0BF56705A4C4FF9','waspIlluminaPlugin','label','Please click either \"Pass\" or \"Fail\" for each lane based on your interpretation of the BASE QUALITY charts only, then click the \"Continue\" button.',NULL,'en_US','numGT30Qc_dialogMessage',0),
	(1853,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'CB28246241904B3CA508A5251ABECA14','waspIlluminaPlugin','label','Lane Selector',NULL,'en_US','numGT30Qc_selector',0),
	(1854,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'8F84664CD5E140CC89E3CE562AC46D46','waspIlluminaPlugin','label','Illumina OLB Stats: Cluster Density Per Lane For ',NULL,'en_US','clustersQc_title',0),
	(1855,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'AC1F75EA17594ABA9554EED89635AA14','waspIlluminaPlugin','label','Assessment of Cluster Density',NULL,'en_US','clustersQc_dialogTitle',0),
	(1856,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'4B93246E2DA443148B56C6BFD22169D9','waspIlluminaPlugin','label','Please click either \"Pass\" or \"Fail\" for each lane based on your interpretation of the CLUSTER DENSITY charts only, then click the \"Continue\" button.',NULL,'en_US','clustersQc_dialogMessage',0),
	(1857,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'DAE772F4F1E14A5E87C83DB9F079D2E0','waspIlluminaPlugin','label','Note that the <span style=\"color:blue;\">blue box plots represent total cluster density</span> and the <span style=\"color:green;\">green box plots represent clusters pass filter</span>. Ideally the median of both plots for each lane should be about the same. Illumina currently recommends ~400 clusters/mm<sup>2</sup> (or 500-630 clusters/mm<sup>2</sup> on GAIIx using the TruSeq SBS V5 kit or 610-680 clusters/mm<sup>2</sup> on HiSeq2000 with TruSeq v3 Cluster and SBS kits).</p><p><span style=\"color:red;\">WARNING:</span> High cluster density combined with low cluster pass filter values indicates overloading of the lane and risks poor quality sequence data.',NULL,'en_US','clustersQc_instructions',0),
	(1858,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'163FDE9CE3AC4709AB19A0FBE8028B42','waspIlluminaPlugin','label','Lane',NULL,'en_US','showPlatformUnit_cell',0),
	(1859,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'D7F07919FAFA46FF89C905A711C9E01C','waspIlluminaPlugin','label','Lane Count',NULL,'en_US','showPlatformUnit_cellcount',0),
	(1860,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'56AB4714EA87468EA1D7A4257848718A','waspIlluminaPlugin','label','Conc. On Lane',NULL,'en_US','showPlatformUnit_concOnCell',0),
	(1861,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'9F6D7D375B704F8E979B50C9804A1EA5','waspIlluminaPlugin','label','No Control On Lane',NULL,'en_US','showPlatformUnit_noControlOnCell',0),
	(1862,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'F8A11BE0B04841FFBFA6AAD8516528FB','waspIlluminaPlugin','label','No Libraries On Lane',NULL,'en_US','showPlatformUnit_noLibrariesOnCell',0),
	(1863,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'D02C37E8C0EF46D3A2377480ABED9005','waspIlluminaPlugin','label','Remove Control From This Lane?',NULL,'en_US','showPlatformUnit_removeControlFromThisCell',0),
	(1864,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'C29D37092AB940DC9DCF61E6AFF4EDA8','waspIlluminaPlugin','label','Remove Library From This Lane?',NULL,'en_US','showPlatformUnit_removeLibFromCell_alert',0),
	(1865,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'AD8026FCEA7A4D0EA444F8D577F5B261','waspIlluminaPlugin','label','Once You Create This Run Record,<br />Adding User Libraries To This Flowcell Will Be Prohibited',NULL,'en_US','showPlatformUnit_warning1',0),
	(1866,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'155EFBBC41F445FC97DE7EAA1319758A','waspIlluminaPlugin','label','Adding Additional User Libraries To This Flowcell<br />Is Now Prohibited',NULL,'en_US','showPlatformUnit_warning2',0),
	(1867,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'5879E4A21A3F4B1588B93A429907BD8E','waspIlluminaPlugin','label','Please provide a value',NULL,'en_US','showPlatformUnit_pleasePorvideValue_alert',0),
	(1868,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'EA8FFDAE3DF54A17BD6F46FC5E4E5C93','waspIlluminaPlugin','label','Please provide a final concentration for this control',NULL,'en_US','showPlatformUnit_pleaseProvideControlConc_alert',0),
	(1869,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'39B3EC3E07934829A94A24F3F8891200','waspIlluminaPlugin','label','Please provide a start date',NULL,'en_US','showPlatformUnit_pleaseProvideStartDate_alert',0),
	(1870,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'E2C0B898087A4DFCA17FE0C0D32153EA','waspIlluminaPlugin','label','Please provide a valid name for this run',NULL,'en_US','showPlatformUnit_pleaseProvideValidName_alert',0),
	(1871,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'B994905B2DD5410D85958D145274E9B5','waspIlluminaPlugin','label','Please provide a start date in the proper format',NULL,'en_US','showPlatformUnit_pleaseProvideValidStartDate_alert',0),
	(1872,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'646C55CEBC7846C2BD84B58DEB37F190','waspIlluminaPlugin','label','Please select a control',NULL,'en_US','showPlatformUnit_pleaseSelectControl_alert',0),
	(1873,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'A610E97521BD454C8251163A42AC9EA5','waspIlluminaPlugin','label','Please select a machine',NULL,'en_US','showPlatformUnit_pleaseSelectMachine_alert',0),
	(1874,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'DE7E4C977BB74D409BD75A5CC1C8A4D7','waspIlluminaPlugin','label','Please select a read length',NULL,'en_US','showPlatformUnit_pleaseSelectReadLength_alert',0),
	(1875,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'0B3FE9D5623F4990A05AF537DB2B404C','waspIlluminaPlugin','label','Please select a read type',NULL,'en_US','showPlatformUnit_pleaseSelectReadType_alert',0),
	(1876,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'2E7A33BF8DCD455E816A4A7FF6998BF1','waspIlluminaPlugin','label','Please select a technician',NULL,'en_US','showPlatformUnit_pleaseSelectTechnician_alert',0),
	(1877,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'12FE19C6147945A1B801632E5D279171','waspIlluminaPlugin','error','Unable to source Illumina run folders',NULL,'en_US','runFolderFind',0),
	(1878,'2013-08-05 19:04:24','2013-08-05 19:04:24',X'239FB199B0CB40C583A4D8C328F77678','waspIlluminaPlugin','error','Unable to initiate run due to a messaging problem. Update cancelled.',NULL,'en_US','runInitialize',0),
	(1879,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'E706FA07476749C19E253B4F4253D717','bowtieAligner','constraint','NotEmpty',NULL,'en_US','mismatches',0),
	(1880,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'5AFB4BE2BA7447EEA3646FAD79065CE4','bowtieAligner','metaposition','10',NULL,'en_US','mismatches',0),
	(1881,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'41742727C60F418BBA923E90BEDBB3C5','bowtieAligner','type','INTEGER',NULL,'en_US','mismatches',0),
	(1882,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'8E805203378643408D2C339D469C4A3B','bowtieAligner','range','0:3',NULL,'en_US','mismatches',0),
	(1883,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'E8DA2799A62842C4B3EBC2306147AE06','bowtieAligner','default','2',NULL,'en_US','mismatches',0),
	(1884,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'F6CB8B842AA44E3792E2269B20044FD6','bowtieAligner','label','Number of mismatches (0-3)',NULL,'en_US','mismatches',0),
	(1885,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'73470AF448004957897B6C13E19690BE','bowtieAligner','tooltip','Maximum number of mismatches permitted in the seed, i.e. the first L base pairs of the read. This may be 0, 1, 2 or 3.',NULL,'en_US','mismatches',0),
	(1886,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'38C47A5439264FF48AEFA7AA38EC7A8A','bowtieAligner','error','A value for number of mismatches must be specified',NULL,'en_US','mismatches',0),
	(1887,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'7D6A6F8EC2044209AC72CFB37C997788','bowtieAligner','constraint','NotEmpty',NULL,'en_US','seedLength',0),
	(1888,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'3D4427F127A14B4B8997D4353E55C5A8','bowtieAligner','metaposition','20',NULL,'en_US','seedLength',0),
	(1889,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'AC98721FEB7442C5AF7C9617EEBEC459','bowtieAligner','type','NUMBER',NULL,'en_US','seedLength',0),
	(1890,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'77AD0558C37D4EB586D24C13718F962D','bowtieAligner','range','5:1000',NULL,'en_US','seedLength',0),
	(1891,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'830E03D9129D4F42836138171F781778','bowtieAligner','default','28',NULL,'en_US','seedLength',0),
	(1892,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'D61AD405529942E7A22B7E5C1C90C77C','bowtieAligner','label','Seed Length',NULL,'en_US','seedLength',0),
	(1893,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'42986F3BF05E4DE8A92F3D6CDF1DAB46','bowtieAligner','tooltip','The number of bases on the high-quality end of the read to which the # mismatches ceiling applies. The lowest permitted setting is 5.',NULL,'en_US','seedLength',0),
	(1894,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'A72562200F8D441F8D0AF0C619A615A4','bowtieAligner','error','A value for seed length must be specified',NULL,'en_US','seedLength',0),
	(1895,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'8AC3B96A32C2485AB6BD784EB67A7C7F','bowtieAligner','constraint','NotEmpty',NULL,'en_US','reportAlignmentNum',0),
	(1896,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'8D4F52F26EB14F93AA71E43EED28753F','bowtieAligner','metaposition','30',NULL,'en_US','reportAlignmentNum',0),
	(1897,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'2BB6B36DC6114D109857581008CD1EE3','bowtieAligner','type','INTEGER',NULL,'en_US','reportAlignmentNum',0),
	(1898,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'8D7D3C64F3124AFEB93F165915B54F23','bowtieAligner','default','1',NULL,'en_US','reportAlignmentNum',0),
	(1899,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'055BC0EE35BA4CA9B17E8642B28CB8FC','bowtieAligner','label','Number of Alignments to Report',NULL,'en_US','reportAlignmentNum',0),
	(1900,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'4215C27245C3450887CF1D5C4081BC87','bowtieAligner','tooltip','Report up to N valid alignments per read or pair ',NULL,'en_US','reportAlignmentNum',0),
	(1901,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'669A121E44644EA2A942BB28A69D59F1','bowtieAligner','error','A value for number of alignments must be specified',NULL,'en_US','reportAlignmentNum',0),
	(1902,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'A5DFF284E3C1484DB039602956076212','bowtieAligner','constraint','NotEmpty',NULL,'en_US','discardThreshold',0),
	(1903,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'F06BCDE42A844D6691253E2B28A8F89B','bowtieAligner','metaposition','40',NULL,'en_US','discardThreshold',0),
	(1904,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'A2BF1447B0884247BC17899B07A1D659','bowtieAligner','type','INTEGER',NULL,'en_US','discardThreshold',0),
	(1905,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'FB044DB9E7284794B3BA40E1FCDE83A0','bowtieAligner','default','1',NULL,'en_US','discardThreshold',0),
	(1906,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'EC8602D0713140E996AAF627578F6C22','bowtieAligner','label','Discard if more than how many alignments?',NULL,'en_US','discardThreshold',0),
	(1907,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'6314BD037E3C4BC3A0C9F54EF41A4D9C','bowtieAligner','tooltip','Suppress all alignments for a particular read or pair if more than N reportable alignments exist for it.',NULL,'en_US','discardThreshold',0),
	(1908,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'D2C259B316EE4A4EAF3EC945B5BB07F8','bowtieAligner','error','A value for the discard threshold must be specified',NULL,'en_US','discardThreshold',0),
	(1909,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'65D2F47805CF4273A01FF94ECCC2CDE0','bowtieAligner','constraint','NotEmpty',NULL,'en_US','isBest',0),
	(1910,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'57A8F2B7D5C14D9ABD5CE50455D33ACD','bowtieAligner','metaposition','50',NULL,'en_US','isBest',0),
	(1911,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'CF156244541B4812A7F084825AFD9356','bowtieAligner','default','yes',NULL,'en_US','isBest',0),
	(1912,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'5F330466925A46CD9088149C02DB1056','bowtieAligner','label','report only best alignments?',NULL,'en_US','isBest',0),
	(1913,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'E9701D7F251B4DD7993F184BEAF18FD9','bowtieAligner','tooltip','Make Bowtie guarantee that reported singleton alignments are best in terms of stratum (i.e. number of mismatches in the seed) and in terms of the quality values at the mismatched position(s)',NULL,'en_US','isBest',0),
	(1914,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'A96F306801C946C6A4B50932BCF422E2','bowtieAligner','control','select:yes:yes;no:no',NULL,'en_US','isBest',0),
	(1915,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'AB68EABBFC9E42BC97BB86DACC571586','bowtieAligner','error','A value must be selected',NULL,'en_US','isBest',0),
	(1916,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'09ED1105C5754B5BBEE31C4E37671752','genericBiomolecule','constraint','NotEmpty',NULL,'en_US','organism',0),
	(1917,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'9E959E4B77164A01BFC5B3E0FD5F0065','genericBiomolecule','metaposition','10',NULL,'en_US','organism',0),
	(1918,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'6929C667816840B6B33D83BD27AD8F0F','genericBiomolecule','label','Organism',NULL,'en_US','organism',0),
	(1919,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'B5F95F82955F4D61875FF8B30D3AD91F','genericBiomolecule','control','select:${organisms}:ncbiID:name',NULL,'en_US','organism',0),
	(1920,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'67FE6089347D44BE9B5EDBB0A306EAF9','genericBiomolecule','error','You must select an organism',NULL,'en_US','organism',0),
	(1921,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'7F2D9B90582D4267879F129693928354','genericLibrary','constraint','NotEmpty',NULL,'en_US','concentration',0),
	(1922,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'15E9301DBF4B4CD1BA7D356DD93383AA','genericLibrary','metaposition','10',NULL,'en_US','concentration',0),
	(1923,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'FF30E3B2BD9240CE848638EC1E6BA6A9','genericLibrary','type','NUMBER',NULL,'en_US','concentration',0),
	(1924,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'C738F5FBC4144238A52FCEE5B32B8678','genericLibrary','range','0.0001:1000000000',NULL,'en_US','concentration',0),
	(1925,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'6836432811C146D5B157BF0AC2ACE37C','genericLibrary','label','Concentration (ng/&micro;l)',NULL,'en_US','concentration',0),
	(1926,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'E6B0BBFDB08046D6B4E099613A0CF038','genericLibrary','error','You must provide a concentration',NULL,'en_US','concentration',0),
	(1927,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'1FC92C4D627B47708B6026571DBA0EDB','genericLibrary','constraint','NotEmpty',NULL,'en_US','volume',0),
	(1928,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'7D1420D8D2924BF09DAC0EF1C9550D96','genericLibrary','metaposition','20',NULL,'en_US','volume',0),
	(1929,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'71DC8D3C4EA843D897B942EE7E04A3C9','genericLibrary','type','NUMBER',NULL,'en_US','volume',0),
	(1930,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'BCB7FB092B8E4F4E813FE1ED9E422CEA','genericLibrary','range','0.0001:1000000000',NULL,'en_US','volume',0),
	(1931,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'6B5717DEA7EF4804B0D383837AB854E9','genericLibrary','label','Volume (&micro;l)',NULL,'en_US','volume',0),
	(1932,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'38E466A0412D4F65BE9991EC57650052','genericLibrary','error','You must provide a volume',NULL,'en_US','volume',0),
	(1933,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'4FCCE92E101C496B827E801CB901B358','genericLibrary','constraint','NotEmpty',NULL,'en_US','buffer',0),
	(1934,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'A23F5EE76F6E4914A7D377A2B9756DB3','genericLibrary','metaposition','30',NULL,'en_US','buffer',0),
	(1935,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'47A613A9AB5A4CA9AD59E1ABBDC8EC4C','genericLibrary','label','Buffer',NULL,'en_US','buffer',0),
	(1936,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'0D1167B8FAA34F60B5D9702EDB2064CF','genericLibrary','control','select:TE:TE;10mM Tris (EB):10mM Tris (EB);Water:Water',NULL,'en_US','buffer',0),
	(1937,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'2F550D0FCD6B4B4FAD7323E56D5F66CD','genericLibrary','error','You must select a buffer type',NULL,'en_US','buffer',0),
	(1938,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'7848E418674A49D1A2393348B394247D','genericLibrary','constraint','NotEmpty',NULL,'en_US','adaptorset',0),
	(1939,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'D129DBD03FE7426F895C67B435BC6D9F','genericLibrary','metaposition','40',NULL,'en_US','adaptorset',0),
	(1940,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'CA93F33246E8436DA3C15E153836B7FF','genericLibrary','label','Adaptor Set',NULL,'en_US','adaptorset',0),
	(1941,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'DC8D73D7E1024EE7B7091D9A1E9B658B','genericLibrary','control','select:${adaptorsets}:adaptorsetId:name',NULL,'en_US','adaptorset',0),
	(1942,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'A12B317361B544DC8ED851AE0301E795','genericLibrary','error','You must choose an adaptor set',NULL,'en_US','adaptorset',0),
	(1943,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'A7A79115798E4A55BB9458063226E3A3','genericLibrary','constraint','NotEmpty',NULL,'en_US','adaptor',0),
	(1944,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'217A3AD1E73E4CEFB91CA9611520020F','genericLibrary','metaposition','50',NULL,'en_US','adaptor',0),
	(1945,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'542502A979104F3493C5902FED8B1D31','genericLibrary','label','Adaptor',NULL,'en_US','adaptor',0),
	(1946,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'E50E7877A2064D88876A79BFB213BE0D','genericLibrary','control','select:${adaptors}:adaptorId:name',NULL,'en_US','adaptor',0),
	(1947,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'08656AFAF27E4255896EA5BF70E746F4','genericLibrary','error','You must choose an adaptor',NULL,'en_US','adaptor',0),
	(1948,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'DB300CCCD2A542B0B2D000088EE6B480','genericLibrary','constraint','NotEmpty',NULL,'en_US','size',0),
	(1949,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'1E89490D894B4D4AAFAE5E85508DA5AF','genericLibrary','metaposition','60',NULL,'en_US','size',0),
	(1950,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'7E3BB858713142FA9877433BA3D219D4','genericLibrary','type','NUMBER',NULL,'en_US','size',0),
	(1951,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'CCB7C1CBA5A647D09A3F060A6AC47471','genericLibrary','range','0.0001:1000000000',NULL,'en_US','size',0),
	(1952,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'71EFAFDDDEED4D28990303514833CC9D','genericLibrary','label','Library Size',NULL,'en_US','size',0),
	(1953,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'C0CAC68FC645446FA742126B06B2AF1A','genericLibrary','error','You must specify the library size',NULL,'en_US','size',0),
	(1954,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'51A8433AC77245499D18C453D5E68F12','genericLibrary','constraint','NotEmpty',NULL,'en_US','sizeSd',0),
	(1955,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'BB0E077F24584E35A26EE33849C722F3','genericLibrary','metaposition','70',NULL,'en_US','sizeSd',0),
	(1956,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'CF52197A624740EF9D5220A6B66A3C77','genericLibrary','type','NUMBER',NULL,'en_US','sizeSd',0),
	(1957,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'B293A58D101648AE9D9F83A97C3DA588','genericLibrary','range','0.0001:1000000000',NULL,'en_US','sizeSd',0),
	(1958,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'0935F39819B34BF782753AF6C253F1C5','genericLibrary','label','Library Size SD',NULL,'en_US','sizeSd',0),
	(1959,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'67853797C4914C87B119C0C8DB474D77','genericLibrary','error','You must specify the library size standard deviation',NULL,'en_US','sizeSd',0),
	(1960,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'6EDB903EBA7D4B09AB7F0BCB6E3590B7','macsPeakcaller','metaposition','10',NULL,'en_US','pValueCutoff',0),
	(1961,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'F02D29B445B1492A8F747D3386F3360D','macsPeakcaller','type','NUMBER',NULL,'en_US','pValueCutoff',0),
	(1962,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'6405991ACA504B049F77BBA621A381F9','macsPeakcaller','default','1e-5',NULL,'en_US','pValueCutoff',0),
	(1963,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'2B9D7BABCB42438C8924CA3DAAFB7FC3','macsPeakcaller','label','p Value Cutoff',NULL,'en_US','pValueCutoff',0),
	(1964,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'A346FDECDE8A48FBBCE852CBCD41F166','macsPeakcaller','tooltip','Pvalue cutoff for peak detection',NULL,'en_US','pValueCutoff',0),
	(1965,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'064080CE1EF1418B9174AC2DC9A0C512','macsPeakcaller','metaposition','20',NULL,'en_US','bandwidth',0),
	(1966,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'1C0268EB42E94CB8832F4C76D3B483D7','macsPeakcaller','type','NUMBER',NULL,'en_US','bandwidth',0),
	(1967,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'D2717396A28544779AC1FA312DB1473A','macsPeakcaller','range','0:5000',NULL,'en_US','bandwidth',0),
	(1968,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'FF7FF9094F90432EB878DBB9029F631E','macsPeakcaller','default','300',NULL,'en_US','bandwidth',0),
	(1969,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'AA9A8446AACB47B9AD0B95CA29D1253B','macsPeakcaller','label','Band width',NULL,'en_US','bandwidth',0),
	(1970,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'BB69EA8BB01D47939B2F796999C49F47','macsPeakcaller','tooltip','This value is only used while building the shifting model.',NULL,'en_US','bandwidth',0),
	(1971,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'551648C3DC5D4E71A9F4A8F597184469','macsPeakcaller','metaposition','30',NULL,'en_US','genomeSize',0),
	(1972,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'C5FDBDE64A27468EB4EDBFFBADAB535D','macsPeakcaller','type','NUMBER',NULL,'en_US','genomeSize',0),
	(1973,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'29B665EA926645328191A69EE47B15A7','macsPeakcaller','default','1e9',NULL,'en_US','genomeSize',0),
	(1974,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'6FFEC254EC2B48BD8AC4F3964FB1F180','macsPeakcaller','label','Effective Genome Size',NULL,'en_US','genomeSize',0),
	(1975,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'18AB285077224082889F6022F9504920','macsPeakcaller','metaposition','40',NULL,'en_US','keepDup',0),
	(1976,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'BAA192007D5F41759CA4B071C14BF580','macsPeakcaller','default','no',NULL,'en_US','keepDup',0),
	(1977,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'D4ED3488B7254527BF4B84DAF67ABA81','macsPeakcaller','label','Keep Duplicates?',NULL,'en_US','keepDup',0),
	(1978,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'909C1CAA153340628E4956662BC0D154','macsPeakcaller','tooltip','Controls MACS behavior towards duplicate tags at the exact same location -- the same coordination and the same strand.',NULL,'en_US','keepDup',0),
	(1979,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'4DD7043FDB9E4BFEA72A0AE85FB0191F','macsPeakcaller','control','select:yes:yes;no:no',NULL,'en_US','keepDup',0),
	(1980,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'37EF6F8B52FA4542AE4D8D4896A24E7D','bwa','constraint','NotEmpty',NULL,'en_US','aln-n',0),
	(1981,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'B689DA4497D44BCCA5471930A26E206E','bwa','metaposition','10',NULL,'en_US','aln-n',0),
	(1982,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'BFF75059C3D649FA8A7665B6CF2D795A','bwa','type','NUMBER',NULL,'en_US','aln-n',0),
	(1983,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'C753F6BBC9ED41EDB3EF2A68D1960D45','bwa','range','0:10',NULL,'en_US','aln-n',0),
	(1984,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'3424B35CF15142EF8CA240233172F0FF','bwa','default','0.04',NULL,'en_US','aln-n',0),
	(1985,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'12C3F1E1DD7E4CC79455C13FF4CE5F45','bwa','label','Maximum edit distance/fraction of missing alignments. (aln -n)',NULL,'en_US','aln-n',0),
	(1986,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'177D68E2D5644853A83BD5A655FA725A','bwa','tooltip','Maximum edit distance if the value is INT, or the fraction of missing alignments given 2% uniform base error rate if FLOAT. In the latter case, the maximum edit distance is automatically chosen for different read lengths. [0.04]',NULL,'en_US','aln-n',0),
	(1987,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'C42E2BA0E2F142C7919CEF1476562AD2','bwa','error','A value for max edit distance must be specified',NULL,'en_US','aln-n',0),
	(1988,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'6B9B346DBA9F4147924BF193292C3396','bwa','constraint','NotEmpty',NULL,'en_US','aln-o',0),
	(1989,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'E10F2A34FFE9465C879A6D2FE31BFB84','bwa','metaposition','20',NULL,'en_US','aln-o',0),
	(1990,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'BA0509FE9B614D84AB2F94236F42A55B','bwa','type','INTEGER',NULL,'en_US','aln-o',0),
	(1991,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'A2DD79AD5B7841DCA4E97425B6453ED0','bwa','range','1:10',NULL,'en_US','aln-o',0),
	(1992,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'C7F52C30B6EF48AF8E4E63AE40FD4C9B','bwa','default','1',NULL,'en_US','aln-o',0),
	(1993,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'B69357D0DCF64A8B9DEBC5ED6A8E0024','bwa','label','Maximum number of gap openings. (aln -o)',NULL,'en_US','aln-o',0),
	(1994,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'41AE7EACB42946C0B6BF7E572781565B','bwa','tooltip','Maximum number of gap opens [1]',NULL,'en_US','aln-o',0),
	(1995,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'B5C2B1E9DC5B4296B272249980B3FB4E','bwa','error','A value for gap openings must be specified',NULL,'en_US','aln-o',0),
	(1996,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'2065ED2F5BB443D1890BEF326C9A8755','bwa','constraint','NotEmpty',NULL,'en_US','aln-e',0),
	(1997,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'DBD8144876AE4B6C939D4BA8C71655FC','bwa','metaposition','30',NULL,'en_US','aln-e',0),
	(1998,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'2DFAF7167F4D4D0191A1115B17946DB8','bwa','type','INTEGER',NULL,'en_US','aln-e',0),
	(1999,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'96883D6F7FF24B229C82735FBA8E3CFE','bwa','range','-1:10',NULL,'en_US','aln-e',0),
	(2000,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'E4B6A28FD4124ED691B0BB4227B00F18','bwa','default','-1',NULL,'en_US','aln-e',0),
	(2001,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'5529D53DAEE24ECF907EBF145AF935E8','bwa','label','Maximum number of gap extensions. (aln -e)',NULL,'en_US','aln-e',0),
	(2002,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'5B924CB7507E4937A4DF9378F6271F25','bwa','tooltip','Maximum number of gap extensions, -1 for k-difference mode (disallowing long gaps) [-1]',NULL,'en_US','aln-e',0),
	(2003,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'0BD75079611B4D3C81B9DCB5501C40A7','bwa','error','A value for max number of gap extensions must be specified',NULL,'en_US','aln-e',0),
	(2004,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'999249868E98490484269709FA667EC4','bwa','constraint','NotEmpty',NULL,'en_US','aln-d',0),
	(2005,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'B73C23B6FC104C86BDB88C7E09D599A8','bwa','metaposition','40',NULL,'en_US','aln-d',0),
	(2006,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'A5229EE5B8FB4F89BBCC596DFDB36303','bwa','type','INTEGER',NULL,'en_US','aln-d',0),
	(2007,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'BD04EFFF5A5F4D9899AE2B6B587A5956','bwa','range','0:20',NULL,'en_US','aln-d',0),
	(2008,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'5B0353C1F3FF4CEB834E0E87B0AE597D','bwa','default','16',NULL,'en_US','aln-d',0),
	(2009,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'522B8024DC2A45919FE75DEFE73C1848','bwa','label','Disallow long deletion within INT bp of 3\' end. (aln -d)',NULL,'en_US','aln-d',0),
	(2010,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'A0ED471F0E15400BBC0069E6003E84AA','bwa','tooltip','Disallow a long deletion within INT bp towards the 3���-end [16]',NULL,'en_US','aln-d',0),
	(2011,'2013-08-05 19:04:25','2013-08-05 19:04:25',X'91A2EE729E9845E1829E5BA8BBF9E21B','bwa','error','A value for the disallow long deletion must be specified',NULL,'en_US','aln-d',0),
	(2012,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'E04B707364A9476E92726F6BB3AD424A','bwa','constraint','NotEmpty',NULL,'en_US','aln-i',0),
	(2013,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'97623174ACC044339190150952874BDF','bwa','metaposition','50',NULL,'en_US','aln-i',0),
	(2014,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'6948291814444A32B8E11E7502FEC17C','bwa','type','INTEGER',NULL,'en_US','aln-i',0),
	(2015,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'080CB01AE50146CB82B27604F5E03FAB','bwa','range','0:20',NULL,'en_US','aln-i',0),
	(2016,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'0A9C8771940D4800907E0A19DFB25FA1','bwa','default','5',NULL,'en_US','aln-i',0),
	(2017,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'D4DDBEEA18C74042A565F1A57A09B663','bwa','label','Disallow an indel within INT bp of end. (aln -i)',NULL,'en_US','aln-i',0),
	(2018,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'2E46F8B530C04A79B53512F97E80BCFF','bwa','tooltip','Disallow an indel within INT bp towards the ends [5]',NULL,'en_US','aln-i',0),
	(2019,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'C10FC7A5581547C7B9DA75C77001B701','bwa','error','A value for the disallow indel must be specified',NULL,'en_US','aln-i',0),
	(2020,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'06FDC591DE9643F2BF57D546D44F4D6E','bwa','constraint','NotEmpty',NULL,'en_US','aln-l',0),
	(2021,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'FABA7344624E4B10B78CE2AEE71E9334','bwa','metaposition','60',NULL,'en_US','aln-l',0),
	(2022,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'680884B7654C4384A55CDB94C65F4DAA','bwa','type','INTEGER',NULL,'en_US','aln-l',0),
	(2023,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'74A6EA264D5E4D4BB7C100C62FAD6796','bwa','range','-1:250',NULL,'en_US','aln-l',0),
	(2024,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'1FE796B66ED74865877BE910EAB53D8E','bwa','default','32',NULL,'en_US','aln-l',0),
	(2025,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'C9139246871C43FEA26D2B94B439B8B0','bwa','label','Take the first INT subsequence as seed. -1 for infinity. (aln -l)',NULL,'en_US','aln-l',0),
	(2026,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'4A2CD5580B8E47FF8148CDE1A370AE27','bwa','tooltip','Take the first INT subsequence as seed. If INT is larger than the query sequence, seeding will be disabled. For long reads, this option is typically ranged from 25 to 35 for ���-k 2���. [32]',NULL,'en_US','aln-l',0),
	(2027,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'75F11BBCEBD54FCDBFCEEBF4D398B4B5','bwa','error','A value for the seed must be specified',NULL,'en_US','aln-l',0),
	(2028,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'137CB0186F464E229815B6DD5A29D500','bwa','constraint','NotEmpty',NULL,'en_US','aln-k',0),
	(2029,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'8E58C09817DC47DEAD7A53956DC3ABA5','bwa','metaposition','70',NULL,'en_US','aln-k',0),
	(2030,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'8692B3270A574DA580C7C2FAD491486F','bwa','type','INTEGER',NULL,'en_US','aln-k',0),
	(2031,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'37CABFE087104863A496C1EDBAC6DB02','bwa','range','0:10',NULL,'en_US','aln-k',0),
	(2032,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'F9FFEAC10AC84533BD263DA8FC72FE82','bwa','default','2',NULL,'en_US','aln-k',0),
	(2033,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'A966C2B6D9F74A65B41B55FDAAF034F6','bwa','label','Maximum edit distance in the seed (aln -k)',NULL,'en_US','aln-k',0),
	(2034,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'DF4E2D5316BB49C7AF801E4B4A5AE6D2','bwa','tooltip','Maximum edit distance in the seed [2]',NULL,'en_US','aln-k',0),
	(2035,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'F10ED6A843EB4525A8776AF2F0A7CCE3','bwa','error','A value for the seed edit distance must be specified',NULL,'en_US','aln-k',0),
	(2036,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'38715495666C465686AE6422C4C32F58','bwa','constraint','NotEmpty',NULL,'en_US','aln-M',0),
	(2037,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'F0B046AA93504799B1A319EA8C810864','bwa','metaposition','80',NULL,'en_US','aln-M',0),
	(2038,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'D091CA6B754D4DCABD41D8817DEEC834','bwa','type','INTEGER',NULL,'en_US','aln-M',0),
	(2039,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'A9C6F77825884B07AF524490CBA12043','bwa','range','0:100',NULL,'en_US','aln-M',0),
	(2040,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'8F3FAD10558F4DE0B35D7A00B4AACD10','bwa','default','3',NULL,'en_US','aln-M',0),
	(2041,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'72260C16596B4056AC5B16FA79D94ED9','bwa','label','Mismatch penalty. (aln -M)',NULL,'en_US','aln-M',0),
	(2042,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'B3870635D8FC456199F36970C9619357','bwa','tooltip','Mismatch penalty. BWA will not search for suboptimal hits with a score lower than (bestScore-misMsc). [3]',NULL,'en_US','aln-M',0),
	(2043,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'FB281641F44241CDBC0BB926EBE101A5','bwa','error','A value for the mismatch penalty must be specified',NULL,'en_US','aln-M',0),
	(2044,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'C2D6A4BDE76B4D41B62EA22B3178F789','bwa','constraint','NotEmpty',NULL,'en_US','aln-O',0),
	(2045,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'F8FA00A96D424A3F804CF6B0EE252F01','bwa','metaposition','90',NULL,'en_US','aln-O',0),
	(2046,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'79116D4488AA403092918F0301AB10D1','bwa','type','INTEGER',NULL,'en_US','aln-O',0),
	(2047,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'044B238F9DDB4A0CAE9E108D1137711C','bwa','range','0:100',NULL,'en_US','aln-O',0),
	(2048,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'F36A20806AEB4836B6CC247EE5815C60','bwa','default','11',NULL,'en_US','aln-O',0),
	(2049,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'49FB7F91DB6845719661834EBD20A997','bwa','label','Gap open penalty (aln -O)',NULL,'en_US','aln-O',0),
	(2050,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'C57F073303B64C888D86EA906C892027','bwa','tooltip','Gap open penalty [11]',NULL,'en_US','aln-O',0),
	(2051,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'94543CEAF83F429E8B8236796010F5BE','bwa','error','A value for the gap open penalty must be specified',NULL,'en_US','aln-O',0),
	(2052,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'82D893562AF04517BD5FDBF0C14BD198','bwa','constraint','NotEmpty',NULL,'en_US','aln-E',0),
	(2053,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'F29FE7DAE1D54A7DBE08649A620D52F6','bwa','metaposition','100',NULL,'en_US','aln-E',0),
	(2054,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'8C33E4D7028D4793A5DF25F84A46FCB1','bwa','type','INTEGER',NULL,'en_US','aln-E',0),
	(2055,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'A308C8E004E84E2DA5047CFEDA1A8744','bwa','range','0:100',NULL,'en_US','aln-E',0),
	(2056,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'5C15215264A34D9BA0F608F441C1949A','bwa','default','4',NULL,'en_US','aln-E',0),
	(2057,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'E6BDD0140CC440A79D768E3C76ABE260','bwa','label','Gap extension penalty (aln -E)',NULL,'en_US','aln-E',0),
	(2058,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'D70E12635C5F4068928EB55B0A8C8096','bwa','tooltip','Gap extension penalty [4]',NULL,'en_US','aln-E',0),
	(2059,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'7AC494A14B1047E1A4A08CF0E92463A2','bwa','error','A value for the gap extension penalty must be specified',NULL,'en_US','aln-E',0),
	(2060,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'1660BDA6743D47258BBE080FF4B691B7','bwa','constraint','NotEmpty',NULL,'en_US','aln-R',0),
	(2061,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'6C63F77B987D41E48D66A91F0F6A8885','bwa','metaposition','110',NULL,'en_US','aln-R',0),
	(2062,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'3D2D506242E54A4494FF55CE3399EC5C','bwa','type','INTEGER',NULL,'en_US','aln-R',0),
	(2063,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'31C8758825ED4B4399E21CE374DCE5DD','bwa','range','0:1000',NULL,'en_US','aln-R',0),
	(2064,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'A42DF1CAABDF4A4A9E0654567136D38C','bwa','default','30',NULL,'en_US','aln-R',0),
	(2065,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'BABBDBAB025C40CA9351EB2F3C1EBDDB','bwa','label','Proceed with suboptimal alignments if there are no more than INT equally best hits. (aln -R)',NULL,'en_US','aln-R',0),
	(2066,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'FA1A245DFF1442F38986DA34D768ABDC','bwa','tooltip','Proceed with suboptimal alignments if there are no more than INT equally best hits. This option only affects paired-end mapping. Increasing this threshold helps to improve the pairing accuracy at the cost of speed, especially for short reads (~32bp). [30]',NULL,'en_US','aln-R',0),
	(2067,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'3B1DB148291D4185B171670158605352','bwa','error','A value for the suboptimal alignments must be specified',NULL,'en_US','aln-R',0),
	(2068,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'238E129826D24890B1E109B22FAD9543','bwa','constraint','NotEmpty',NULL,'en_US','aln-N',0),
	(2069,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'9CC4B963BE54430DAC41A245304E1403','bwa','metaposition','120',NULL,'en_US','aln-N',0),
	(2070,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'54A2EBB4548D44978BFCCBF839F52B72','bwa','default','no',NULL,'en_US','aln-N',0),
	(2071,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'22E323DDC02D4206B20F46F077E648AB','bwa','label','Disable iterative search, yes=SLOW (aln -N)',NULL,'en_US','aln-N',0),
	(2072,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'E078E337971D41E0AC46A776C3775CCC','bwa','tooltip','Disable iterative search. All hits with no more than maxDiff differences will be found. This mode is much slower than the default. [no]',NULL,'en_US','aln-N',0),
	(2073,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'7D0C4FDFF35B45AD8B36EF7BF1B3D024','bwa','control','select:yes:yes;no:no',NULL,'en_US','aln-N',0),
	(2074,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'02096788C7B0484C8F2826127D9C0AA2','bwa','error','A value for disable iterative search must be specified',NULL,'en_US','aln-N',0),
	(2075,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'51539F132B8F4075992B3C4E134A1422','bwa','constraint','NotEmpty',NULL,'en_US','aln-q',0),
	(2076,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'280BFC4CAB6E40B7AABDAB36EBDBD8BF','bwa','metaposition','130',NULL,'en_US','aln-q',0),
	(2077,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'5EE47B5C39CC441D97EBAE827CDAD354','bwa','type','INTEGER',NULL,'en_US','aln-q',0),
	(2078,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'F0E05F133F7D4356B2B666E857E2BB0C','bwa','range','0:100',NULL,'en_US','aln-q',0),
	(2079,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'75D5A03E1EC24F3B89C066BFA579B5A9','bwa','default','0',NULL,'en_US','aln-q',0),
	(2080,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'1F0578F657FA414B83542D980608AB6E','bwa','label','Parameter for read trimming. (aln -q)',NULL,'en_US','aln-q',0),
	(2081,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'406BB19FA5594E7FAD62057CF0E67793','bwa','tooltip','Parameter for read trimming. BWA trims a read down to argmax_x{sum_{i=x+1}^l(INT-q_i)} if q_l<INT where l is the original read length. [0]',NULL,'en_US','aln-q',0),
	(2082,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'8F1EB036CF7241D18017CCD39DC34F8E','bwa','error','A value for read trimming must be specified',NULL,'en_US','aln-q',0),
	(2083,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'647C1FE699F74D61BB2196762B6E5B1D','bwa','constraint','NotEmpty',NULL,'en_US','sampe-a',0),
	(2084,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'769D0704B0904024A37324773A53DB6A','bwa','metaposition','140',NULL,'en_US','sampe-a',0),
	(2085,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'BA116B393B214E0BB291D944B401E9C3','bwa','type','INTEGER',NULL,'en_US','sampe-a',0),
	(2086,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'978E3802184046438C368C26ED6D8302','bwa','range','0:100000',NULL,'en_US','sampe-a',0),
	(2087,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'8C07F6E40E814BE7B748D1702C5A4DA9','bwa','default','500',NULL,'en_US','sampe-a',0),
	(2088,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'5511E344D3424DB3BB1E33F7BEC4E4CC','bwa','label','Maximum insert size. Not used in single end alignments. (sampe -a)',NULL,'en_US','sampe-a',0),
	(2089,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'E0870E83F7174206BCD3F913526B695A','bwa','tooltip','Maximum insert size for a read pair to be considered being mapped properly. Since 0.4.5, this option is only used when there are not enough good alignment to infer the distribution of insert sizes. [500]',NULL,'en_US','sampe-a',0),
	(2090,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'E4B97943CF784500956F2C59E991F635','bwa','error','A value for max insert size must be specified',NULL,'en_US','sampe-a',0),
	(2091,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'EB5DF3B73E7E48A7B057C97AF7B79835','bwa','constraint','NotEmpty',NULL,'en_US','sampe-o',0),
	(2092,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'1CE590288DDD45A78E81AFA25246B20F','bwa','metaposition','150',NULL,'en_US','sampe-o',0),
	(2093,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'76000E5193F34BA585BD6095BB3E1747','bwa','type','INTEGER',NULL,'en_US','sampe-o',0),
	(2094,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'3A3DD2FC5DB34672971481DFED49C138','bwa','range','0:100000',NULL,'en_US','sampe-o',0),
	(2095,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'C8E840D4FBB54A8D84E490782A6AFD74','bwa','default','100000',NULL,'en_US','sampe-o',0),
	(2096,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'C37BC2211B9E46778B0D04B538367E33','bwa','label','Maximum occurrences of a read for pairing. Not used in single end alignments. (sampe -o)',NULL,'en_US','sampe-o',0),
	(2097,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'5401D156EFC34B7782799CD9CEB54E0B','bwa','tooltip','Maximum occurrences of a read for pairing. A read with more occurrneces will be treated as a single-end read. Reducing this parameter helps faster pairing. [100000]',NULL,'en_US','sampe-o',0),
	(2098,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'48E126B8F32046278DADF09400B857B6','bwa','error','A value for max occurences must be specified',NULL,'en_US','sampe-o',0),
	(2099,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'9253D50ADFFF49F6A058003CF32715AA','bwa','constraint','NotEmpty',NULL,'en_US','sampe-n',0),
	(2100,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'D06FB5E30F1C485B874C9FCF7D42199D','bwa','metaposition','160',NULL,'en_US','sampe-n',0),
	(2101,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'6587487273EC4640B275A20EF7DE6B85','bwa','type','INTEGER',NULL,'en_US','sampe-n',0),
	(2102,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'C2239519B9044B0D8E4FDF3EE086C0C1','bwa','range','0:100',NULL,'en_US','sampe-n',0),
	(2103,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'A1BEE302C35D489DAEF23530AD9FFA41','bwa','default','3',NULL,'en_US','sampe-n',0),
	(2104,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'17C25B75BB8D4EDA8D38DBC3D84EC2FC','bwa','label','Maximum number of alignments to output in the XA tag for reads paired properly. (samse/sampe -n)',NULL,'en_US','sampe-n',0),
	(2105,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'5FE892AFD2D2490E9C1F12D0A3F06AE5','bwa','tooltip','Maximum number of alignments to output in the XA tag for reads paired properly. If a read has more than INT hits, the XA tag will not be written. [3]',NULL,'en_US','sampe-n',0),
	(2106,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'E8D7A437455F423FA01EF5974535A0C6','bwa','error','A value for max insert size must be specified',NULL,'en_US','sampe-n',0),
	(2107,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'3E26211231164A9B920C00DB402D58DC','bwa','constraint','NotEmpty',NULL,'en_US','sampe-N',0),
	(2108,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'0B619BDDFB6D49ECA549F27AC222A533','bwa','metaposition','170',NULL,'en_US','sampe-N',0),
	(2109,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'5CF849D2CCAA499FA0B7428747CCA6A6','bwa','type','INTEGER',NULL,'en_US','sampe-N',0),
	(2110,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'D9CF5C9596EF4B13BBE0F66A9BD44235','bwa','range','0:100',NULL,'en_US','sampe-N',0),
	(2111,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'8EAB4C4291944F6A8A31BCA1FB6D17CC','bwa','default','10',NULL,'en_US','sampe-N',0),
	(2112,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'D242A02C73D841A3BBD5B7B892127504','bwa','label','Maximum number of alignments to output in the XA tag for reads paired properly. Not used in single end alignments. (sampe -N)',NULL,'en_US','sampe-N',0),
	(2113,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'916EAE59C6C64EBDBDE311E0BBA64176','bwa','tooltip','Maximum number of alignments to output in the XA tag for disconcordant read pairs (excluding singletons). If a read has more than INT hits, the XA tag will not be written. [10]',NULL,'en_US','sampe-N',0),
	(2114,'2013-08-05 19:04:25','2013-08-05 19:04:26',X'211F176F1BE54C9C9E0598AC7E2A7A3C','bwa','error','A value for max insert size must be specified',NULL,'en_US','sampe-N',0),
	(2115,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'80F80577EF05442C90D1BDDEEB7DB7B4','genericDna','constraint','NotEmpty',NULL,'en_US','concentration',0),
	(2116,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'7367E486D37C415F85FE42D4B765D850','genericDna','metaposition','10',NULL,'en_US','concentration',0),
	(2117,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'10DCBB0960D54F5EAB6354280F6C319E','genericDna','type','NUMBER',NULL,'en_US','concentration',0),
	(2118,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'89994E6157C949F790DF8574CB03BB0C','genericDna','range','0.0001:1000000000',NULL,'en_US','concentration',0),
	(2119,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'76A221CEE11F425F8AA03EE216B758C0','genericDna','label','Concentration (ng/&micro;l)',NULL,'en_US','concentration',0),
	(2120,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'31857CADCFCC498EA7EF0A3D774981BF','genericDna','error','You must provide a concentration',NULL,'en_US','concentration',0),
	(2121,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'1D30C5A1DADF4BDEB2607D47EDEDAD21','genericDna','constraint','NotEmpty',NULL,'en_US','volume',0),
	(2122,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'B79A6831BCC642A6A99795ED83D9B9B4','genericDna','metaposition','20',NULL,'en_US','volume',0),
	(2123,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'90024A54D7AD424580AC872A81ED9039','genericDna','type','NUMBER',NULL,'en_US','volume',0),
	(2124,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'2D96107A0B15429DB6F4C820FCCA2E13','genericDna','range','0.0001:1000000000',NULL,'en_US','volume',0),
	(2125,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'0B0BC0D2A6C74F2392CE889486BCE21D','genericDna','label','Volume (&micro;l)',NULL,'en_US','volume',0),
	(2126,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'A7C24BDB973A4E4CAFBA55A30D3E7B3C','genericDna','error','You must provide a volume',NULL,'en_US','volume',0),
	(2127,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'3DB63D04A7E7486F8AC771D1BB1B7A54','genericDna','constraint','NotEmpty',NULL,'en_US','buffer',0),
	(2128,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'460C6E356F1F43ADAE980022A15147D4','genericDna','metaposition','30',NULL,'en_US','buffer',0),
	(2129,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'9A0D916DB17E4CBE9FB2294470DF73B6','genericDna','label','Buffer',NULL,'en_US','buffer',0),
	(2130,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'C3DA33DC19674281AEB98DA639837BCE','genericDna','control','select:TE:TE;10mM Tris (EB):10mM Tris (EB);Water:Water',NULL,'en_US','buffer',0),
	(2131,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'A53D0B2FBB1B43E994A837CD62CCE311','genericDna','error','You must select a buffer type',NULL,'en_US','buffer',0),
	(2132,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'DFC13B311FA949FDB0DB293645ED4848','genericDna','constraint','NotEmpty',NULL,'en_US','A260_280',0),
	(2133,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'646DAC5D2D724121877383A2A76CC8C8','genericDna','metaposition','40',NULL,'en_US','A260_280',0),
	(2134,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'76800EC9F42246EDA258DE18572E0210','genericDna','type','NUMBER',NULL,'en_US','A260_280',0),
	(2135,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'6F43CE34F0344DA38D294119318BE366','genericDna','range','0.0001:1000000000',NULL,'en_US','A260_280',0),
	(2136,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'60B958971E4247188BF2E8B4A6D6F5C5','genericDna','label','A260/280',NULL,'en_US','A260_280',0),
	(2137,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'EE1EAF6E946647E0AACBA2119194BB86','genericDna','error','You must provide an A260/280 reading',NULL,'en_US','A260_280',0),
	(2138,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'1564E04AA771404A97A2197EE0B3CCA2','genericDna','constraint','NotEmpty',NULL,'en_US','A260_230',0),
	(2139,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'69DB102AD9544C42A2AD60B9C298EAB9','genericDna','metaposition','50',NULL,'en_US','A260_230',0),
	(2140,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'F6756A30B14C43299C1685D8C36E8251','genericDna','type','NUMBER',NULL,'en_US','A260_230',0),
	(2141,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'1B5DB13952454984B60193A67A2032D9','genericDna','range','0.0001:1000000000',NULL,'en_US','A260_230',0),
	(2142,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'19D2B3E8AE6641D9A262317BD38D3B6E','genericDna','label','A260/230',NULL,'en_US','A260_230',0),
	(2143,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'22C4760D3CCE437FBFB1A09138B78862','genericDna','error','You must provide an A260/230 reading',NULL,'en_US','A260_230',0),
	(2144,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'7FABB5A3572E432D9BF00CE2A3BB2218','chipseqDna','constraint','NotEmpty',NULL,'en_US','fragmentSize',0),
	(2145,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'180C73255FB844648430DEE476E47360','chipseqDna','metaposition','10',NULL,'en_US','fragmentSize',0),
	(2146,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'3F9F5273A651434D90C3A6FBCEF2820F','chipseqDna','type','NUMBER',NULL,'en_US','fragmentSize',0),
	(2147,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'98EAAF42D08E4697860CCF5283254250','chipseqDna','range','0.0001:1000000000',NULL,'en_US','fragmentSize',0),
	(2148,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'281830D925BD471DAE4EF6D5BB01BB38','chipseqDna','label','Average Fragmentation Size',NULL,'en_US','fragmentSize',0),
	(2149,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'54BB945007EB422C93A73C6AA669B968','chipseqDna','error','You must provide a fragmentSize',NULL,'en_US','fragmentSize',0),
	(2150,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'7C82528319264D588E3D79B4C0703BE8','chipseqDna','constraint','NotEmpty',NULL,'en_US','fragmentSizeSD',0),
	(2151,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'8145563F863645CDA270A352388E1F33','chipseqDna','metaposition','20',NULL,'en_US','fragmentSizeSD',0),
	(2152,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'53530791309A4DE6AECCA570814BF708','chipseqDna','type','NUMBER',NULL,'en_US','fragmentSizeSD',0),
	(2153,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'E7023D3D74EF4B1189E97E20FFED44F7','chipseqDna','range','0.0001:1000000000',NULL,'en_US','fragmentSizeSD',0),
	(2154,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'7D28DCA5B5B4464D88C1018B4B92920A','chipseqDna','label','Fragmentation Size Std. Dev.',NULL,'en_US','fragmentSizeSD',0),
	(2155,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'49E07F4305BF4E449CB383D80B104C6F','chipseqDna','error','You must provide a standard deviation',NULL,'en_US','fragmentSizeSD',0),
	(2156,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'266FCD048A04413A9258681C875F1B17','chipseqDna','metaposition','30',NULL,'en_US','antibody',0),
	(2157,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'ADCF91C9F71846D1BBC787191ECED7DD','chipseqDna','label','Antibody',NULL,'en_US','antibody',0),
	(2158,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'63DE12E6F5E84B37ACAE770A3B473C04','wasp-chipSeqPlugin','label','Chip Seq',NULL,'en_US','workflow',0),
	(2159,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'170EC905420D49009B548F7EE9BCEA7D','wasp-chipSeqPlugin','label','ModifyChipSeq Metadata',NULL,'en_US','jobsubmit/modifymeta',0),
	(2160,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'77781BDDA41440479969D68075EC7773','wasp-chipSeqPlugin','label','MPS Sequencer Options',NULL,'en_US','jobsubmit/resource/mps',0),
	(2161,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'5B03ABC910264DE4BF3D465E2568A432','wasp-chipSeqPlugin','label','Samples',NULL,'en_US','jobsubmit/samples',0),
	(2162,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'D1CA4D57C91E44259A318D5E5447CD26','wasp-chipSeqPlugin','label','Select Genomes',NULL,'en_US','jobsubmit/genomes',0),
	(2163,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'7FB63ACB4C284152A44783403FB4B2A2','wasp-chipSeqPlugin','label','DNA Sequencer Cells',NULL,'en_US','jobsubmit/cells',0),
	(2164,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'F7AF0B68F1C44C3C81ACD90C85DED38E','wasp-chipSeqPlugin','label','IP vs Input Pairings',NULL,'en_US','jobsubmit/chipSeq/pair',0),
	(2165,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'BDF18BF711A14A4380DFDF397E795430','wasp-chipSeqPlugin','label','Aligner Selection',NULL,'en_US','jobsubmit/software/referenceBasedAligner',0),
	(2166,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'96D5542D3BE049EDBDD8D2895551019D','wasp-chipSeqPlugin','label','Peak Calling Software Selection',NULL,'en_US','jobsubmit/software/peakcaller',0),
	(2167,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'656D8DB6A3654FC296710919E2E6C7F1','wasp-chipSeqPlugin','label','Comments',NULL,'en_US','jobsubmit/comment',0),
	(2168,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'529E951ED39247278028BAFE9F1B0BBA','wasp-chipSeqPlugin','label','Verify Submission',NULL,'en_US','jobsubmit/verify',0),
	(2169,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'0469DE56BCD044289D6D75D319465B4C','wasp-chipSeqPlugin','label','Please pair up Input and IP samples that should be analyzed together for peak calling. Note that this pairing is optional.',NULL,'en_US','pairing_instructions',0),
	(2170,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'57B4C26C80ED485DBB3A28CD00335BFA','wasp-chipSeqPlugin','label','IP',NULL,'en_US','test',0),
	(2171,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'4F6880BC2FE34B9694FAB87CCF8268A8','wasp-chipSeqPlugin','label','Input',NULL,'en_US','control',0),
	(2172,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'B8880EDDA9AC4824BB30683797309C9E','wasp-genericDnaSeqPlugin','label','Generic DNA Seq',NULL,'en_US','workflow',0),
	(2173,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'B8495923A4C7489897CCD27D8545680D','wasp-genericDnaSeqPlugin','label','Modify Generic DNA Seq Metadata',NULL,'en_US','jobsubmit/modifymeta',0),
	(2174,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'C99F53C770784465929CC183D039A4F4','wasp-genericDnaSeqPlugin','label','MPS Sequencer Options',NULL,'en_US','jobsubmit/resource/mps',0),
	(2175,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'199740FBF6644148BE238B92D4F5F2E0','wasp-genericDnaSeqPlugin','label','Samples',NULL,'en_US','jobsubmit/samples',0),
	(2176,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'41A772A0834549C1BCCEF86D31A646A0','wasp-genericDnaSeqPlugin','label','Select Genomes',NULL,'en_US','jobsubmit/genomes',0),
	(2177,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'747959DD27EA44F1AF26E8700876D6B2','wasp-genericDnaSeqPlugin','label','DNA Sequencer Cells',NULL,'en_US','jobsubmit/cells',0),
	(2178,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'E256B469C6094777AAE394C42EEBDAAA','wasp-genericDnaSeqPlugin','label','Aligner Selection',NULL,'en_US','jobsubmit/software/aligner',0),
	(2179,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'74511D0E46CE4F34B08936E06564E2D2','wasp-genericDnaSeqPlugin','label','Comments',NULL,'en_US','jobsubmit/comment',0),
	(2180,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'B4EE53B068CA48AC94928678BDD3512E','wasp-genericDnaSeqPlugin','label','Verify Submission',NULL,'en_US','jobsubmit/verify',0),
	(2181,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'8AB77DB68E37448395D3CA6752922125','illuminaHiSeq2000','constraint','NotEmpty',NULL,'en_US','readLength',0),
	(2182,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'F5199EDA36F34FD493ACE977AFA225B9','illuminaHiSeq2000','metaposition','10',NULL,'en_US','readLength',0),
	(2183,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'7AFF88F69550412DBBDE11DD35EE3202','illuminaHiSeq2000','label','Read Length',NULL,'en_US','readLength',0),
	(2184,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'05F8F5AE6C714991969F1D8D5F503C1E','illuminaHiSeq2000','control','select:${resourceOptions.get(readLength)}:value:label',NULL,'en_US','readLength',0),
	(2185,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'B678C907DD054057B2F0E7A209486322','illuminaHiSeq2000','error','You must choose a read length',NULL,'en_US','readLength',0),
	(2186,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'575A235AC59546C3ADA4189E7DFDA5E1','illuminaHiSeq2000','constraint','NotEmpty',NULL,'en_US','readType',0),
	(2187,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'338B13F7455C4538B04E2B0A282273CF','illuminaHiSeq2000','metaposition','20',NULL,'en_US','readType',0),
	(2188,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'2891316C171E4320BA0D2A1C78E2ACDB','illuminaHiSeq2000','label','Read Type',NULL,'en_US','readType',0),
	(2189,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'2F20E06123E945B6A5266DFD576CF3AE','illuminaHiSeq2000','control','select:${resourceOptions.get(readType)}:value:label',NULL,'en_US','readType',0),
	(2190,'2013-08-05 19:04:26','2013-08-05 19:04:26',X'233FFD40FB584618BE9104ACBE1C59F7','illuminaHiSeq2000','error','You must choose a read type',NULL,'en_US','readType',0),
	(2191,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'0313F509BAFA413C82D838A59156C918','helptagPipeline','metaposition','10',NULL,'en_US','pValueCutoff',0),
	(2192,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'DD8A8E4BACF743C8AC3C4117740453E1','helptagPipeline','type','NUMBER',NULL,'en_US','pValueCutoff',0),
	(2193,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'35D9645A920E481F8D3056A39C0AECD0','helptagPipeline','default','100000',NULL,'en_US','pValueCutoff',0),
	(2194,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'6A07ED4BF82346A499A6B636276276EF','helptagPipeline','label','p Value Cutoff',NULL,'en_US','pValueCutoff',0),
	(2195,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'C3A98D0286944488AC08F3E2CAEE89D3','helptagPipeline','metaposition','20',NULL,'en_US','bandwidth',0),
	(2196,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'8AC83E5278D04B7F9298D0EA825B9F1D','helptagPipeline','type','NUMBER',NULL,'en_US','bandwidth',0),
	(2197,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'1AF0C6503C3640FEB646B619046E268F','helptagPipeline','range','0:5000',NULL,'en_US','bandwidth',0),
	(2198,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'458DF577CF4F4F7E9C41896FF066C9C9','helptagPipeline','default','300',NULL,'en_US','bandwidth',0),
	(2199,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'F1539086D1714648AC626F359D695DA8','helptagPipeline','label','Bandwidth',NULL,'en_US','bandwidth',0),
	(2200,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'D049DB16E68E4940A1A9D6666C298FC0','helptagPipeline','metaposition','30',NULL,'en_US','genomeSize',0),
	(2201,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'2145ACAA6EAF47D6A46015854DCAA937','helptagPipeline','type','NUMBER',NULL,'en_US','genomeSize',0),
	(2202,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'50B57172A89642579832A9974FD0462A','helptagPipeline','default','1000000000',NULL,'en_US','genomeSize',0),
	(2203,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'A58E42D547584BC3A21DBFB8A18C6C5F','helptagPipeline','label','Effective Genome Size',NULL,'en_US','genomeSize',0),
	(2204,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'714A81AFC0104127853E871B14F26D23','helptagPipeline','metaposition','40',NULL,'en_US','keepDup',0),
	(2205,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'0646BC97C7D54285B1B8A8FB476F4C14','helptagPipeline','default','no',NULL,'en_US','keepDup',0),
	(2206,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'04EC5142E1FF4C66A63DB6767AC9B018','helptagPipeline','label','Keep Duplicates?',NULL,'en_US','keepDup',0),
	(2207,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'CA4571F97A1044DC880B6CA8D3308C62','helptagPipeline','control','select:yes:yes;no:no',NULL,'en_US','keepDup',0),
	(2208,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'7FEEF0290208442BAA1609AA5703898D','helptagLibrary','constraint','NotEmpty',NULL,'en_US','fragmentSize',0),
	(2209,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'B438E81456D343C4AC5BDF86F8166E32','helptagLibrary','metaposition','10',NULL,'en_US','fragmentSize',0),
	(2210,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'A240405171EB41BAB7ED501715C525DC','helptagLibrary','type','NUMBER',NULL,'en_US','fragmentSize',0),
	(2211,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'63B20CEA5A174188AED55AA8A8933203','helptagLibrary','range','0.0001:1000000000',NULL,'en_US','fragmentSize',0),
	(2212,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'A3B2F449CF454B1D847C83780795CD25','helptagLibrary','label','Average Fragmentation Size',NULL,'en_US','fragmentSize',0),
	(2213,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'1CABC1840B264D61BA07A027EDBD8F3E','helptagLibrary','error','You must provide a fragmentSize',NULL,'en_US','fragmentSize',0),
	(2214,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'38B1EE5515DD400AA13039ED626C8B9E','helptagLibrary','constraint','NotEmpty',NULL,'en_US','fragmentSizeSD',0),
	(2215,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'B6BAAE31C47D41FE84071E443BFAB966','helptagLibrary','metaposition','20',NULL,'en_US','fragmentSizeSD',0),
	(2216,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'D35F64E03A284920A0C28F1EE90D0531','helptagLibrary','type','NUMBER',NULL,'en_US','fragmentSizeSD',0),
	(2217,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'790604F6D3F341AA8A3FC4048EBB0616','helptagLibrary','range','0.0001:1000000000',NULL,'en_US','fragmentSizeSD',0),
	(2218,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'FCDF49FB26D5458083F59C390D70BC1F','helptagLibrary','label','Fragmentation Size Std. Dev.',NULL,'en_US','fragmentSizeSD',0),
	(2219,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'B307BC8F8464455CAC2F0C6A86EFF74D','helptagLibrary','error','You must provide a standard deviation',NULL,'en_US','fragmentSizeSD',0),
	(2220,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'A266A8DA93F545D98EA9C32AABD471B2','wasp-helpTagPlugin','label','HELP Tagging',NULL,'en_US','workflow',0),
	(2221,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'B1A5958AFC53459EA0C14CB32A946FEE','wasp-helpTagPlugin','label','Modify HelpTag Metadata',NULL,'en_US','jobsubmit/modifymeta',0),
	(2222,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'82BFB555FE6B4A56B3632C3BB772D838','wasp-helpTagPlugin','label','MPS Sequencer Options',NULL,'en_US','jobsubmit/resource/mps',0),
	(2223,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'404F280BC69B4CE484DE5D1F18962359','wasp-helpTagPlugin','label','Samples',NULL,'en_US','jobsubmit/samples',0),
	(2224,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'46C2DABE3906423AA441C3977A80DBB8','wasp-helpTagPlugin','label','Select Genomes',NULL,'en_US','jobsubmit/genomes',0),
	(2225,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'79A51135904F4F3B8BF43A3B33D7151C','wasp-helpTagPlugin','label','DNA Sequencer Cells',NULL,'en_US','jobsubmit/cells',0),
	(2226,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'978F2BFFB7B540E7BAC56507C2F80ACD','wasp-helpTagPlugin','label','HpaII vs MspI Pairings',NULL,'en_US','jobsubmit/helpTag/pair',0),
	(2227,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'9D19BFB9A4324ECBA89738C974BD35BA','wasp-helpTagPlugin','label','Aligner Selection',NULL,'en_US','jobsubmit/software/referenceBasedAligner',0),
	(2228,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'E61F6E62DBDB480AA78A08B5D4D65860','wasp-helpTagPlugin','label','HELP-tag Pipeline Selection',NULL,'en_US','jobsubmit/software/helptagPipeline',0),
	(2229,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'E3CBF8D6F1BD4E9680C1FE3E2EF016DE','wasp-helpTagPlugin','label','Comments',NULL,'en_US','jobsubmit/comment',0),
	(2230,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'2423C27ABF5A480287353AE736381481','wasp-helpTagPlugin','label','Verify Submission',NULL,'en_US','jobsubmit/verify',0),
	(2231,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'926799A00FDE47CBA79E884B9D8746DE','wasp-helpTagPlugin','label','Please select HpaII vs MspI for all samples to be analyzed in pairs after sequencing.',NULL,'en_US','pairing_instructions',0),
	(2232,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'1C8419DC88C447EEAFAB557A5086332E','wasp-helpTagPlugin','label','HpaII',NULL,'en_US','test',0),
	(2233,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'8F7986BF0A7B4D7587D800F0AB7ADE73','wasp-helpTagPlugin','label','MspI',NULL,'en_US','control',0),
	(2234,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'57462A2B270F40668741E00D53811320','illuminaMiSeqPersonalSequencer','constraint','NotEmpty',NULL,'en_US','readLength',0),
	(2235,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'B66E137030084C60A92843F3A26225CF','illuminaMiSeqPersonalSequencer','metaposition','10',NULL,'en_US','readLength',0),
	(2236,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'0CB029E37E1342999CBA39BAB7EC7D45','illuminaMiSeqPersonalSequencer','label','Read Length',NULL,'en_US','readLength',0),
	(2237,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'4D802A4DAE074E34B049C7D04F714A05','illuminaMiSeqPersonalSequencer','control','select:${resourceOptions.get(readLength)}:value:label',NULL,'en_US','readLength',0),
	(2238,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'A55B31D978934D4BBA427FCE4A318D01','illuminaMiSeqPersonalSequencer','error','You must choose a read length',NULL,'en_US','readLength',0),
	(2239,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'5741F7C5898340CA837BFB75EC695CA7','illuminaMiSeqPersonalSequencer','constraint','NotEmpty',NULL,'en_US','readType',0),
	(2240,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'59E1236246A947B2BA211E7986239112','illuminaMiSeqPersonalSequencer','metaposition','20',NULL,'en_US','readType',0),
	(2241,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'22EF154B88DF425EBA85000802BBFCB1','illuminaMiSeqPersonalSequencer','label','Read Type',NULL,'en_US','readType',0),
	(2242,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'89364F3523F14480AC516AC2C92D72EE','illuminaMiSeqPersonalSequencer','control','select:${resourceOptions.get(readType)}:value:label',NULL,'en_US','readType',0),
	(2243,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'5BC036F2899343CB9AD5E5346B8EF75E','illuminaMiSeqPersonalSequencer','error','You must choose a read type',NULL,'en_US','readType',0),
	(2244,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'22B4BF7B902E4943AE596CDA9D404BDC','illuminaFlowcellMiSeqV1','label','Lane',NULL,'en_US','cellName',0),
	(2245,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'C08870CFDD5E416BACC9164D8F3B5DEC','illuminaFlowcellMiSeqV1','label','Flow Cell',NULL,'en_US','platformUnitName',0),
	(2246,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'80E2BABCBE1845F08D085D4425A74BC0','illuminaFlowcellV3','label','Lane',NULL,'en_US','cellName',0),
	(2247,'2013-08-05 19:04:27','2013-08-05 19:04:27',X'FCA6DCEEFDCE4B0AB4432B52E52E81F7','illuminaFlowcellV3','label','Flow Cell',NULL,'en_US','platformUnitName',0);

/*!40000 ALTER TABLE `uifield` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table usermeta
# ------------------------------------------------------------

LOCK TABLES `usermeta` WRITE;
/*!40000 ALTER TABLE `usermeta` DISABLE KEYS */;

INSERT INTO `usermeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `userid`, `lastupdatebyuser`)
VALUES
	(1,'2012-05-23 17:23:00','2013-03-12 18:20:20',X'293B13835E584F0C916BD728F42B9E39','user.title',0,NULL,'Dr',2,NULL),
	(2,'2012-05-23 17:23:00','2013-03-12 18:20:20',X'23C3602E2C874CB08F69C443C51EF1CA','user.institution',0,NULL,'Einstein',2,NULL),
	(3,'2012-05-23 17:23:00','2013-03-12 18:20:20',X'86CF9706268D4FDDBBD3DFB8279D9252','user.departmentId',0,NULL,'1',2,NULL),
	(4,'2012-05-23 17:23:00','2013-03-12 18:20:20',X'55E43093D5414C62AA552CED1D416C0A','user.building_room',0,NULL,'Price 954',2,NULL),
	(5,'2012-05-23 17:23:00','2013-03-12 18:20:20',X'24DC65BD577F4B449D38C367BF4454D3','user.address',0,NULL,'1301 Morris Park Ave',2,NULL),
	(6,'2012-05-23 17:23:00','2013-03-12 18:20:20',X'A0E195468CE843719328B2A1F7F8B95E','user.city',0,NULL,'Bronx',2,NULL),
	(7,'2012-05-23 17:23:00','2013-03-12 18:20:20',X'02655FD6DA2144E5ACD604E46D723627','user.state',0,NULL,'NY',2,NULL),
	(8,'2012-05-23 17:23:00','2013-03-12 18:20:20',X'A0C88E4A70E045B1B0584AC97E57F1D0','user.country',0,NULL,'US',2,NULL),
	(9,'2012-05-23 17:23:00','2013-03-12 18:20:20',X'EF9A9ED5679A4C849CF3E5404623F413','user.zip',0,NULL,'10461',2,NULL),
	(10,'2012-05-23 17:23:00','2013-03-12 18:20:20',X'6FBA24318ED34804AA59CF001BAE7A63','user.phone',0,NULL,'718-600-1985',2,NULL),
	(11,'2012-05-23 17:23:00','2013-03-12 18:20:20',X'F1833E3DB63742FB848D3384478E7174','user.fax',0,NULL,'',2,NULL),
	(12,'2012-05-30 16:15:28','2013-03-12 18:20:20',X'89C93B89EAF44C3C8916F2ACEDB0698B','user.title',0,NULL,'Ms',3,NULL),
	(13,'2012-05-30 16:15:28','2013-03-12 18:20:20',X'60BF5FF947594B148D64C0D2A463FFBF','user.institution',0,NULL,'Einstein',3,NULL),
	(14,'2012-05-30 16:15:28','2013-03-12 18:20:20',X'0D03DF804E7546EFBB5AC643B6937E2E','user.departmentId',0,NULL,'1',3,NULL),
	(15,'2012-05-30 16:15:28','2013-03-12 18:20:20',X'1890CD22952B411D9F0668F91B75FD00','user.building_room',0,NULL,'Price 1003',3,NULL),
	(16,'2012-05-30 16:15:28','2013-03-12 18:20:20',X'2F148C3A95404B6EAE3C9853F7BC7B73','user.address',0,NULL,'1301 Morris Park Ave.',3,NULL),
	(17,'2012-05-30 16:15:28','2013-03-12 18:20:20',X'C9A99ED7F86C47C1B80E67075152B7BF','user.city',0,NULL,'Bronx',3,NULL),
	(18,'2012-05-30 16:15:28','2013-03-12 18:20:20',X'0EE0B6A9439E4BEBA1A873F1AA143AB9','user.state',0,NULL,'NY',3,NULL),
	(19,'2012-05-30 16:15:28','2013-03-12 18:20:20',X'8184CE4356B54BA08831267E885A5D11','user.country',0,NULL,'US',3,NULL),
	(20,'2012-05-30 16:15:28','2013-03-12 18:20:20',X'8A01B323F1D64AD8A044809348DC2CBE','user.zip',0,NULL,'10461',3,NULL),
	(21,'2012-05-30 16:15:28','2013-03-12 18:20:20',X'F9446B35EDAA47FBB31BEFD4916A8D79','user.phone',0,NULL,'718-600-3465',3,NULL),
	(22,'2012-05-30 16:15:28','2013-03-12 18:20:20',X'442A58ABB7904E9CBC9570C3DEDF5569','user.fax',0,NULL,'',3,NULL),
	(23,'2012-05-30 16:31:54','2013-03-12 18:20:20',X'FC041CD2A7A3490D8C3CC92B175D66C9','user.title',0,NULL,'Ms',4,NULL),
	(24,'2012-05-30 16:31:54','2013-03-12 18:20:20',X'78380D85F3B94B8D93FAB3DA05308E4A','user.institution',0,NULL,'Einstein',4,NULL),
	(25,'2012-05-30 16:31:54','2013-03-12 18:20:20',X'9744B82F2B774FA78E70585145D3BDD3','user.departmentId',0,NULL,'1',4,NULL),
	(26,'2012-05-30 16:31:54','2013-03-12 18:20:20',X'261AB6AC00044289909F3FA9A6E5F84A','user.building_room',0,NULL,'Price 201',4,NULL),
	(27,'2012-05-30 16:31:54','2013-03-12 18:20:20',X'B2E9FF0162FF4F87B3A1A5A0F874471B','user.address',0,NULL,'1301 Morris Park Ave.',4,NULL),
	(28,'2012-05-30 16:31:54','2013-03-12 18:20:20',X'1471EB324776465EA3735F003DA53681','user.city',0,NULL,'Bronx',4,NULL),
	(29,'2012-05-30 16:31:54','2013-03-12 18:20:20',X'CC4DE4C13AC8494AA348D1EFE381F960','user.state',0,NULL,'NY',4,NULL),
	(30,'2012-05-30 16:31:54','2013-03-12 18:20:20',X'689F7CF0A05C4A638330DD4CFC070916','user.country',0,NULL,'US',4,NULL),
	(31,'2012-05-30 16:31:54','2013-03-12 18:20:20',X'E21B02D23CCC49718C345641B2CD16CD','user.zip',0,NULL,'10461',4,NULL),
	(32,'2012-05-30 16:31:54','2013-03-12 18:20:20',X'ECE07AB65523479A9D912BA9D7436112','user.phone',0,NULL,'718-600-1100',4,NULL),
	(33,'2012-05-30 16:31:54','2013-03-12 18:20:20',X'265904A3907F4D3C8D5BAFFBFA913CA4','user.fax',0,NULL,'',4,NULL),
	(34,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'3CA0860BEB634E279783BF5A773D927B','user.title',0,NULL,'Dr',5,NULL),
	(35,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'042315BDA30343538DB05403DC8E3474','user.institution',0,NULL,'Einstein',5,NULL),
	(36,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'0AA2EAD6AB064F4C870F44C7A2C8EC01','user.departmentId',0,NULL,'3',5,NULL),
	(37,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'C09AFCFABE6B41DEAE91D8AF5DCAF3A8','user.building_room',0,NULL,'Price 2200',5,NULL),
	(38,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'E01BB247E2EA4D1B832957618ACE6BF7','user.address',0,NULL,'1301 Morris Park Ave.',5,NULL),
	(39,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'A5E2147EAEA746AFAC40D5F4111CA5B0','user.city',0,NULL,'Bronx',5,NULL),
	(40,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'BD6759F5CF844D3192DBC70FE5F2757F','user.state',0,NULL,'NY',5,NULL),
	(41,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'23725398AD944AB9A482497262258D7B','user.country',0,NULL,'US',5,NULL),
	(42,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'92BF62CAA17141F1BBB1EC6346FDA1F6','user.zip',0,NULL,'10461',5,NULL),
	(43,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'AF9377F05B02436A95DB23DAB19F2471','user.phone',0,NULL,'718-123-4567',5,NULL),
	(44,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'717F296DC16846FBA0CC9002E7624AFA','user.fax',0,NULL,'',5,NULL),
	(45,'2012-05-30 20:22:24','2013-03-12 18:20:20',X'FA5F924581AD4231BBF7E00D7668D68B','user.labName',0,NULL,'Greally Lab',5,NULL),
	(46,'2012-05-30 21:13:54','2013-03-12 18:20:20',X'A0B796D5810D47689087390DD5BC9CCB','user.title',0,NULL,'Ms',6,NULL),
	(47,'2012-05-30 21:13:54','2013-03-12 18:20:20',X'C7E0CBDF0EF340428FD860424A7692BC','user.institution',0,NULL,'Einstein',6,NULL),
	(48,'2012-05-30 21:13:54','2013-03-12 18:20:20',X'AF1932CC54114450B5722D00C6934327','user.departmentId',0,NULL,'3',6,NULL),
	(49,'2012-05-30 21:13:54','2013-03-12 18:20:20',X'5D7132A021354F08B4386F076EE1769E','user.building_room',0,NULL,'Price 2220',6,NULL),
	(50,'2012-05-30 21:13:54','2013-03-12 18:20:20',X'F3062D90D83447ED86279038B9B495A8','user.address',0,NULL,'1301 Morris Park Ave.',6,NULL),
	(51,'2012-05-30 21:13:54','2013-03-12 18:20:20',X'F5715E9F8D1A44469071CC8FFA8AA41D','user.city',0,NULL,'Bronx',6,NULL),
	(52,'2012-05-30 21:13:54','2013-03-12 18:20:20',X'F71779C9A0654CF1BA11B7D826D51570','user.state',0,NULL,'NY',6,NULL),
	(53,'2012-05-30 21:13:54','2013-03-12 18:20:20',X'2259CD1C170547308C672F61D373D442','user.country',0,NULL,'US',6,NULL),
	(54,'2012-05-30 21:13:54','2013-03-12 18:20:20',X'4533EDEB8F9D4A6FABE2C84B8A324CC8','user.zip',0,NULL,'10461',6,NULL),
	(55,'2012-05-30 21:13:54','2013-03-12 18:20:20',X'DDBBAD2F71C64DEC91EF141F2E1A6E4B','user.phone',0,NULL,'718-608-0000',6,NULL),
	(56,'2012-05-30 21:13:54','2013-03-12 18:20:20',X'3E0A1D6D92B3448BA921EF2E630A1733','user.fax',0,NULL,'',6,NULL),
	(57,'2012-05-30 21:13:54','2013-03-12 18:20:20',X'BC47F2B05741491883C56009D3823C6B','user.primaryuserid',0,NULL,'jgreally',6,NULL),
	(58,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'ABE40EBC37864F7FA8025B71D25413D8','user.title',0,NULL,'Prof',7,NULL),
	(59,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'FB34E5A15E9B46C9B10EA645B826E76A','user.institution',0,NULL,'Einstein',7,NULL),
	(60,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'1E3D9AA12EE7472CAD0E4733CEB97260','user.departmentId',0,NULL,'3',7,NULL),
	(61,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'332044EFBBC24A808A492398F65EBBAB','user.building_room',0,NULL,'Price 933',7,NULL),
	(62,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'01569FB4C3CA49839BBBA141265884C7','user.address',0,NULL,'1301 Morris Park Ave.',7,NULL),
	(63,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'3F492B09652748E2AA069E90EC817666','user.city',0,NULL,'Bronx',7,NULL),
	(64,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'C96C244B62CA420497618A360FEFCE81','user.state',0,NULL,'NY',7,NULL),
	(65,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'598AA2BA60B14141A42A054899F9D3C7','user.country',0,NULL,'US',7,NULL),
	(66,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'F3DE71B20686425A98247579BD4764C5','user.zip',0,NULL,'10461',7,NULL),
	(67,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'7FE18C9BC03D4F9E84B388AE243337EE','user.phone',0,NULL,'718-600-1192',7,NULL),
	(68,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'7EBAA127D11F4E1780545896BFBE0109','user.fax',0,NULL,'',7,NULL),
	(69,'2012-05-30 22:03:56','2013-03-12 18:20:20',X'55737E230DDD4EC28581DC2033764237','user.labName',0,NULL,'Goldin Lab',7,NULL),
	(70,'2012-05-31 13:59:22','2013-03-12 18:20:20',X'98D74D2912C748B190676428DF0FC2AD','user.title',0,NULL,'Dr',8,NULL),
	(71,'2012-05-31 13:59:22','2013-03-12 18:20:20',X'1827CEF278D44BC9830A481936C4365B','user.institution',0,NULL,'Einstein',8,NULL),
	(72,'2012-05-31 13:59:22','2013-03-12 18:20:20',X'D454DC61D36F48548D9DF7836A3E3E5D','user.departmentId',0,NULL,'3',8,NULL),
	(73,'2012-05-31 13:59:22','2013-03-12 18:20:20',X'E01D8DDB28F449409CB3203E0293928A','user.building_room',0,NULL,'Price 654',8,NULL),
	(74,'2012-05-31 13:59:22','2013-03-12 18:20:20',X'1AADE2C63B7D4E48B53D55CEC48CBFDC','user.address',0,NULL,'1301 Morris Park Ave.',8,NULL),
	(75,'2012-05-31 13:59:22','2013-03-12 18:20:20',X'323FC6E68197455C9D8C9175C68BD9FE','user.city',0,NULL,'Bronx',8,NULL),
	(76,'2012-05-31 13:59:22','2013-03-12 18:20:20',X'589846335D91417C9D038D7BA39DD19F','user.state',0,NULL,'NY',8,NULL),
	(77,'2012-05-31 13:59:22','2013-03-12 18:20:20',X'AEE20CBF5E0F45658D6E388C26A81944','user.country',0,NULL,'US',8,NULL),
	(78,'2012-05-31 13:59:22','2013-03-12 18:20:20',X'688B37F1990D4E21A5821AF3B05F350E','user.zip',0,NULL,'10461',8,NULL),
	(79,'2012-05-31 13:59:22','2013-03-12 18:20:20',X'49A8E1497AD54AD7AD41ED458041B958','user.phone',0,NULL,'718-600-0019',8,NULL),
	(80,'2012-05-31 13:59:22','2013-03-12 18:20:20',X'72FE8C8C069C487592E7A58B1A0DB28F','user.fax',0,NULL,'718-600-0020',8,NULL),
	(81,'2012-05-31 13:59:22','2013-03-12 18:20:20',X'C5E2D5CC4FE147709F480129828CA157','user.labName',0,NULL,'Auton Lab',8,NULL),
	(82,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'93A2F9B8E181472C90476B5F39357C79','user.title',0,NULL,'Dr',9,NULL),
	(83,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'D1690FAFB3C04FF4AF4E39498A61AD1F','user.institution',0,NULL,'NYU Medical',9,NULL),
	(84,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'3D9B7268F7BB47A99494281857049138','user.departmentId',0,NULL,'2',9,NULL),
	(85,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'28C0F8A7FD704766BD53178FE84FA4B1','user.building_room',0,NULL,'Hammer 1406',9,NULL),
	(86,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'70D103FA8E7845DA8701E1843CE4BCB8','user.address',0,NULL,'16-50 54th Street',9,NULL),
	(87,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'5E241D29B5F74AFC9568CFE488A0B377','user.city',0,NULL,'New York',9,NULL),
	(88,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'C6D63704A73A4117A91C7E3B65E5FF59','user.state',0,NULL,'NY',9,NULL),
	(89,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'A9DDD745B68E41ECB872CA3BC471F59A','user.country',0,NULL,'US',9,NULL),
	(90,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'CBC3F9C0588743679161E2FF2FF82743','user.zip',0,NULL,'10002',9,NULL),
	(91,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'1239E07B105D49BEAE9C6E3C63BAE7C9','user.phone',0,NULL,'212-445-2345',9,NULL),
	(92,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'7946695B0D264A8A9F7B27233E48F48D','user.fax',0,NULL,'',9,NULL),
	(93,'2012-05-31 14:00:01','2013-03-12 18:20:20',X'4597A3CD619F4BAFBCC150F480312E86','user.labName',0,NULL,'Trokie Lab',9,NULL),
	(94,'2012-05-31 14:02:26','2013-03-12 18:20:20',X'7DF3B9B173AE4F47979308097021058F','user.title',0,NULL,'Dr',10,NULL),
	(95,'2012-05-31 14:02:26','2013-03-12 18:20:20',X'C3AF49A247BB4EFDB29E290AB70FF43C','user.institution',0,NULL,'Einstein',10,NULL),
	(96,'2012-05-31 14:02:26','2013-03-12 18:20:20',X'8A1BEFDC421A4016A5EDFA5AA518D9E8','user.departmentId',0,NULL,'3',10,NULL),
	(97,'2012-05-31 14:02:26','2013-03-12 18:20:20',X'E4FEC4143A93480CAC8ED5CAA687D184','user.building_room',0,NULL,'Price 2222',10,NULL),
	(98,'2012-05-31 14:02:26','2013-03-12 18:20:20',X'852110F51C3740149CC29D20DDEBF431','user.address',0,NULL,'1301 Morris Park Ave',10,NULL),
	(99,'2012-05-31 14:02:26','2013-03-12 18:20:20',X'D60B6466D31E4944B536905FD8DD7C2B','user.city',0,NULL,'Bronx',10,NULL),
	(100,'2012-05-31 14:02:26','2013-03-12 18:20:20',X'52264A7CE6A946C1BADCE4AFA38A385C','user.state',0,NULL,'NY',10,NULL),
	(101,'2012-05-31 14:02:26','2013-03-12 18:20:20',X'2A349213347047D88D903D2F885EC477','user.country',0,NULL,'US',10,NULL),
	(102,'2012-05-31 14:02:26','2013-03-12 18:20:20',X'27FA86DF25254927B0B82F976864F75B','user.zip',0,NULL,'10461',10,NULL),
	(103,'2012-05-31 14:02:26','2013-03-12 18:20:20',X'5269E8B56E844811829D1F8F1A794EA6','user.phone',0,NULL,'718-500-6696',10,NULL),
	(104,'2012-05-31 14:02:26','2013-03-12 18:20:20',X'110BEA0453AD4D09B616BED654ACEFFE','user.fax',0,NULL,'718-500-6697',10,NULL),
	(105,'2012-05-31 14:02:26','2013-03-12 18:20:20',X'A9F2079A49A348618DC592CD876AE80E','user.primaryuserid',0,NULL,'jgreally',10,NULL),
	(106,'2012-05-31 14:07:26','2013-03-12 18:20:20',X'C147F2A9982547F3B9A99F78DA451208','user.title',0,NULL,'Mr',11,NULL),
	(107,'2012-05-31 14:07:26','2013-03-12 18:20:20',X'57B02B3E12654B40B846DE9A0C6FDF2A','user.institution',0,NULL,'Einstein',11,NULL),
	(108,'2012-05-31 14:07:26','2013-03-12 18:20:20',X'59B68CBC30EB4BBB92A8E8380EEE3455','user.departmentId',0,NULL,'1',11,NULL),
	(109,'2012-05-31 14:07:26','2013-03-12 18:20:20',X'17E669A9E4D14B4B899F743103957086','user.building_room',0,NULL,'Price 1357',11,NULL),
	(110,'2012-05-31 14:07:26','2013-03-12 18:20:20',X'3475329003E6431AB758ABF7C2DFFDC0','user.address',0,NULL,'1301 Morris Park Ave.',11,NULL),
	(111,'2012-05-31 14:07:26','2013-03-12 18:20:20',X'A6263711B0BF4AF38668C2F85A35443A','user.city',0,NULL,'Bronx',11,NULL),
	(112,'2012-05-31 14:07:26','2013-03-12 18:20:20',X'AF176FA64D354A0DAF8A51FDA7E49ACF','user.state',0,NULL,'NY',11,NULL),
	(113,'2012-05-31 14:07:26','2013-03-12 18:20:20',X'CFB7E6C4536842938C8FAA29510B58C1','user.country',0,NULL,'US',11,NULL),
	(114,'2012-05-31 14:07:26','2013-03-12 18:20:20',X'8C49719668E8439987A72572B11D684F','user.zip',0,NULL,'10461',11,NULL),
	(115,'2012-05-31 14:07:26','2013-03-12 18:20:20',X'16A26714B62142F498FFF244B81C6B2A','user.phone',0,NULL,'718-600-4533',11,NULL),
	(116,'2012-05-31 14:07:26','2013-03-12 18:20:20',X'91D8DEE1D0C84180B5C244A151E23A5F','user.fax',0,NULL,'',11,NULL),
	(117,'2012-05-31 14:15:29','2013-03-12 18:20:20',X'6D915A480583481DAECDD047D7EC87AD','user.title',0,NULL,'Prof',12,NULL),
	(118,'2012-05-31 14:15:29','2013-03-12 18:20:20',X'2F1EB046B0C849D28D7ED10A0580238D','user.institution',0,NULL,'Einstein',12,NULL),
	(119,'2012-05-31 14:15:29','2013-03-12 18:20:20',X'D0B878EF8BB34A6BA20238EC30AA6A8E','user.departmentId',0,NULL,'3',12,NULL),
	(120,'2012-05-31 14:15:29','2013-03-12 18:20:20',X'47C20875F9EC4F46B47F84C692F7ECB5','user.building_room',0,NULL,'Price 222',12,NULL),
	(121,'2012-05-31 14:15:29','2013-03-12 18:20:20',X'0D79D651D42C4DE8938DA90102E17144','user.address',0,NULL,'1301 Morris Park Ave',12,NULL),
	(122,'2012-05-31 14:15:29','2013-03-12 18:20:20',X'51BC25C8CEDE4D339538D75BF2B6FD80','user.city',0,NULL,'Bronx',12,NULL),
	(123,'2012-05-31 14:15:29','2013-03-12 18:20:20',X'223DB856477A4E38A7C0BBE0DCF608D7','user.state',0,NULL,'NY',12,NULL),
	(124,'2012-05-31 14:15:29','2013-03-12 18:20:20',X'4D72F5A4E9B84F33BF398BC16C68EA73','user.country',0,NULL,'US',12,NULL),
	(125,'2012-05-31 14:15:29','2013-03-12 18:20:20',X'D7C6F01BF60A44D9ACBB06A3475670B9','user.zip',0,NULL,'10461',12,NULL),
	(126,'2012-05-31 14:15:29','2013-03-12 18:20:20',X'8F4106C7C4D24E4E966B2AB22F54067F','user.phone',0,NULL,'617-600-1313',12,NULL),
	(127,'2012-05-31 14:15:29','2013-03-12 18:20:20',X'108FA0FB93E845E08B660EC6A8CDDFE1','user.fax',0,NULL,'',12,NULL),
	(128,'2012-05-31 14:15:29','2013-03-12 18:20:20',X'48FE14A12C0145169FEF99362A885FB3','user.primaryuserid',0,NULL,'agoldin',12,NULL),
	(129,'2012-06-14 13:49:39','2013-03-12 18:20:20',X'29088538C8284D35AC583BB611A9D034','user.title',0,NULL,'Dr',1,NULL),
	(130,'2012-06-14 13:49:39','2013-03-12 18:20:20',X'F5A1D29086C3448296664290D6420E24','user.institution',0,NULL,'Einstein',1,NULL),
	(131,'2012-06-14 13:49:39','2013-03-12 18:20:20',X'39991AAC5BD447B0A8B720028D9D2EE1','user.departmentId',0,NULL,'1',1,NULL),
	(132,'2012-06-14 13:49:39','2013-03-12 18:20:20',X'EBAC39862B8A444EA7B4FC0CFC44B9F8','user.building_room',0,NULL,'N/A',1,NULL),
	(133,'2012-06-14 13:49:39','2013-03-12 18:20:20',X'B3C86E3F22C64172B5235AA31EC35628','user.address',0,NULL,'N/A',1,NULL),
	(134,'2012-06-14 13:49:39','2013-03-12 18:20:20',X'4C2E5B6279004771BA50150939ED230C','user.city',0,NULL,'N/A',1,NULL),
	(135,'2012-06-14 13:49:39','2013-03-12 18:20:20',X'2B5EA1B4ACB84BEDB21665ABD1B66CA6','user.state',0,NULL,'NY',1,NULL),
	(136,'2012-06-14 13:49:39','2013-03-12 18:20:20',X'2188B500E0624950BC6211522C9400F8','user.country',0,NULL,'US',1,NULL),
	(137,'2012-06-14 13:49:39','2013-03-12 18:20:20',X'C1FFCC1699E242759049F0BBB5A7E259','user.zip',0,NULL,'N/A',1,NULL),
	(138,'2012-06-14 13:49:39','2013-03-12 18:20:20',X'835BE5F788B640DAB840554A5ED63E80','user.phone',0,NULL,'N/A',1,NULL),
	(139,'2012-06-14 13:49:39','2013-03-12 18:20:20',X'3B6E332170BD49078118ECAC0DBD9F56','user.fax',0,NULL,'N/A',1,NULL),
	(140,'2012-06-14 14:11:40','2013-03-12 18:20:20',X'7E3BE51EBA6544FCBF7E81AD41BBE1B3','user.title',0,NULL,'Miss',13,NULL),
	(141,'2012-06-14 14:11:40','2013-03-12 18:20:20',X'08DDF2512D6F4EC3B7BFD8F853D72631','user.institution',0,NULL,'Einstein',13,NULL),
	(142,'2012-06-14 14:11:40','2013-03-12 18:20:20',X'4E8A62DA04474E5FBD32666BF27DB317','user.departmentId',0,NULL,'1',13,NULL),
	(143,'2012-06-14 14:11:40','2013-03-12 18:20:20',X'F3E2E889D1D74BE88DFC2FFB4ED16C2A','user.building_room',0,NULL,'4333',13,NULL),
	(144,'2012-06-14 14:11:40','2013-03-12 18:20:20',X'B65606661C224FAD905B6EAF04429B27','user.address',0,NULL,'Chanin',13,NULL),
	(145,'2012-06-14 14:11:40','2013-03-12 18:20:20',X'8E874343CCCC4A13AD4AE25877CBF6D5','user.city',0,NULL,'Bronx',13,NULL),
	(146,'2012-06-14 14:11:40','2013-03-12 18:20:20',X'CCB07D5545E04453BD7480C24E4FD4DB','user.state',0,NULL,'NY',13,NULL),
	(147,'2012-06-14 14:11:40','2013-03-12 18:20:20',X'F27C0F3178C249C89AC5125FDD1927E8','user.country',0,NULL,'US',13,NULL),
	(148,'2012-06-14 14:11:40','2013-03-12 18:20:20',X'B76DD097D55648E482A013D03825A389','user.zip',0,NULL,'10461',13,NULL),
	(149,'2012-06-14 14:11:40','2013-03-12 18:20:20',X'FE162691EFD04C889F2DD6A7A0737B81','user.phone',0,NULL,'718-600-4455',13,NULL),
	(150,'2012-06-14 14:11:40','2013-03-12 18:20:20',X'323DA29E1C29470792A5A258C1DB2929','user.fax',0,NULL,'',13,NULL);

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
	(1,'2012-12-20 11:03:30','2013-03-12 18:19:05',X'1D0AF0E8C19647AC97BF6881DED8EA79','2012-12-20 11:03:30','wasp-bisulfiteSeqPlugin',0,'BISUL Seq',0),
	(2,'2012-12-20 11:03:31','2013-08-06 10:03:15',X'98F8736B1C634B54B29299A36AF480D5','2012-12-20 11:03:31','wasp-chipSeqPlugin',1,'ChIP Seq',0),
	(3,'2012-12-20 11:03:31','2013-08-06 10:03:15',X'7609E24374CC4361A0FE6E8A077B391E','2012-12-20 11:03:31','wasp-genericDnaSeqPlugin',1,'Generic DNA Seq',0),
	(4,'2012-12-20 11:03:31','2013-08-06 10:03:15',X'6E79BFE149BB48C89C065B48E45405DC','2012-12-20 11:03:31','wasp-helpTagPlugin',1,'HELP Tagging',0);

/*!40000 ALTER TABLE `workflow` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowmeta
# ------------------------------------------------------------

LOCK TABLES `workflowmeta` WRITE;
/*!40000 ALTER TABLE `workflowmeta` DISABLE KEYS */;

INSERT INTO `workflowmeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `workflowid`, `lastupdatebyuser`)
VALUES
	(1,'2012-12-20 11:03:30','2013-03-12 18:20:20',X'5F26684A843940949BBC14967825569F','workflow.jobFlowBatchJob',0,NULL,'default.waspJob.jobflow.v1',1,NULL),
	(2,'2012-12-20 11:03:30','2013-03-12 18:20:20',X'5005009F92EB43738FB90F1E9F383444','workflow.submitpageflow',0,NULL,'/jobsubmit/modifymeta/{n};/jobsubmit/resource/mps/{n};/jobsubmit/samples/{n};/jobsubmit/cells/{n};/jobsubmit/software/bisulfiteSeqPipeline/{n};/jobsubmit/submit/{n};/jobsubmit/ok',1,NULL),
	(3,'2012-12-20 11:03:31','2013-03-12 18:20:20',X'88C026F02878429190F11EF19D9DBE1B','workflow.jobFlowBatchJob',0,NULL,'default.waspJob.jobflow.v1',2,NULL),
	(4,'2012-12-20 11:03:31','2013-08-05 19:04:26',X'49A5B7945AD3411785F99ADAECD9C779','workflow.submitpageflow',0,NULL,'/jobsubmit/modifymeta/{n};/jobsubmit/resource/mps/{n};/jobsubmit/samples/{n};/jobsubmit/genomes/{n};/jobsubmit/cells/{n};/jobsubmit/chipSeq/pair/{n};/jobsubmit/software/referenceBasedAligner/{n};/jobsubmit/software/peakcaller/{n};/jobsubmit/comment/{n};/jobsubmit/verify/{n};/jobsubmit/submit/{n};/jobsubmit/ok',2,NULL),
	(5,'2012-12-20 11:03:31','2013-03-12 18:20:20',X'613B63752AAD4BC5BAD439AAD10DD480','workflow.jobFlowBatchJob',0,NULL,'default.waspJob.jobflow.v1',3,NULL),
	(6,'2012-12-20 11:03:31','2013-08-05 19:04:26',X'81B8DDE97221411C8EA46B2D91024D79','workflow.submitpageflow',0,NULL,'/jobsubmit/modifymeta/{n};/jobsubmit/resource/mps/{n};/jobsubmit/samples/{n};/jobsubmit/genomes/{n};/jobsubmit/cells/{n};/jobsubmit/software/referenceBasedAligner/{n};/jobsubmit/comment/{n};/jobsubmit/verify/{n};/jobsubmit/submit/{n};/jobsubmit/ok',3,NULL),
	(7,'2012-12-20 11:03:31','2013-03-12 18:20:20',X'8C16DA04A9D649EE83A2852049D6A996','workflow.jobFlowBatchJob',0,NULL,'default.waspJob.jobflow.v1',4,NULL),
	(8,'2012-12-20 11:03:31','2013-08-05 19:04:27',X'4A140F80678D4FE199E16A1D3E7E348C','workflow.submitpageflow',0,NULL,'/jobsubmit/modifymeta/{n};/jobsubmit/resource/mps/{n};/jobsubmit/samples/{n};/jobsubmit/genomes/{n};/jobsubmit/cells/{n};/jobsubmit/helpTag/pair/{n};/jobsubmit/software/referenceBasedAligner/{n};/jobsubmit/software/helptagPipeline/{n};/jobsubmit/comment/{n};/jobsubmit/verify/{n};/jobsubmit/submit/{n};/jobsubmit/ok',4,NULL);

/*!40000 ALTER TABLE `workflowmeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowresourcecategory
# ------------------------------------------------------------

LOCK TABLES `workflowresourcecategory` WRITE;
/*!40000 ALTER TABLE `workflowresourcecategory` DISABLE KEYS */;

INSERT INTO `workflowresourcecategory` (`id`, `created`, `updated`, `uuid`, `resourcecategoryid`, `workflowid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'CE26140F514D4D4CB49AE95BBA6D7ADF',1,1,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'36CD93AED2A043A997044E5D1720B1BA',2,1,NULL),
	(9,'2013-08-06 10:03:42','2013-08-06 10:03:42',X'2D9EA9CC78B64292A40EF2138EF25A08',1,2,1),
	(10,'2013-08-06 10:03:42','2013-08-06 10:03:42',X'096E85881F1B4819BD07D97F53DCF097',2,2,1),
	(11,'2013-08-06 10:03:53','2013-08-06 10:03:53',X'2C52D84CDFB84541B46F4FA444F3D89C',1,3,1),
	(12,'2013-08-06 10:03:53','2013-08-06 10:03:53',X'0FB2878F96EA4D7380F87D8EBFDB6924',2,3,1),
	(13,'2013-08-06 10:04:03','2013-08-06 10:04:03',X'65112E9447324CA580F1131F71D25B34',1,4,1),
	(14,'2013-08-06 10:04:03','2013-08-06 10:04:03',X'74BC36A746DB4410ACCDE63D32DB0072',2,4,1);

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
	(17,'2013-08-06 10:03:42','2013-08-06 10:03:42',X'3E93E34CDB3B41E8A8ECF5F1E98CBBB0','illuminaHiSeq2000.allowableUiField.readType',1,NULL,'single:single;paired:paired;',9,1),
	(18,'2013-08-06 10:03:42','2013-08-06 10:03:42',X'78E5978A4FEE47BAB4123E18C748E97A','illuminaHiSeq2000.allowableUiField.readLength',2,NULL,'50:50;75:75;',9,1),
	(19,'2013-08-06 10:03:42','2013-08-06 10:03:42',X'49FB61ABD58B455986FE645A6ABE4352','illuminaMiSeqPersonalSequencer.allowableUiField.readLength',1,NULL,'36:36;50:50;100:100;',10,1),
	(20,'2013-08-06 10:03:42','2013-08-06 10:03:42',X'7D00040B14EF46BCBC4077BA9F74CA20','illuminaMiSeqPersonalSequencer.allowableUiField.readType',2,NULL,'single:single;paired:paired;',10,1),
	(21,'2013-08-06 10:03:53','2013-08-06 10:03:53',X'1C6EC5F0D66C4939943A5154CFBF58A8','illuminaHiSeq2000.allowableUiField.readType',1,NULL,'paired:paired;',11,1),
	(22,'2013-08-06 10:03:53','2013-08-06 10:03:53',X'3013F57052404B6281CA87EE286BEC55','illuminaHiSeq2000.allowableUiField.readLength',2,NULL,'50:50;75:75;100:100;150:150;',11,1),
	(23,'2013-08-06 10:03:53','2013-08-06 10:03:53',X'935742C835CD4129AA2CC758909FE421','illuminaMiSeqPersonalSequencer.allowableUiField.readLength',1,NULL,'50:50;100:100;150:150;',12,1),
	(24,'2013-08-06 10:03:53','2013-08-06 10:03:53',X'CDC7D3831DCE440CA3A55C0A0835739F','illuminaMiSeqPersonalSequencer.allowableUiField.readType',2,NULL,'single:single;paired:paired;',12,1),
	(25,'2013-08-06 10:04:03','2013-08-06 10:04:03',X'6822C6B685DB40FA86B6DB763E4A8DAA','illuminaHiSeq2000.allowableUiField.readType',1,NULL,'single:single;',13,1),
	(26,'2013-08-06 10:04:03','2013-08-06 10:04:03',X'D6D6439434714307AAD8AAC9CD597A26','illuminaHiSeq2000.allowableUiField.readLength',2,NULL,'50:50;75:75;100:100;',13,1),
	(27,'2013-08-06 10:04:03','2013-08-06 10:04:03',X'FE78DCA28C9A44C1864C6328463203E0','illuminaMiSeqPersonalSequencer.allowableUiField.readLength',1,NULL,'50:50;100:100;150:150;',14,1),
	(28,'2013-08-06 10:04:03','2013-08-06 10:04:03',X'8514AE1E17444C98B3DBC2DDFBEFFCA4','illuminaMiSeqPersonalSequencer.allowableUiField.readType',2,NULL,'single:single;',14,1);

/*!40000 ALTER TABLE `workflowresourcecategorymeta` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowresourcetype
# ------------------------------------------------------------

LOCK TABLES `workflowresourcetype` WRITE;
/*!40000 ALTER TABLE `workflowresourcetype` DISABLE KEYS */;

INSERT INTO `workflowresourcetype` (`id`, `created`, `updated`, `uuid`, `resourcetypeid`, `workflowid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'81CD48DEB43943F5B5AC438099746CFA',2,1,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'BBE2EAD15E6C4F2C8DFAB3F60A81F806',6,1,NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'D74C4A3A2281432AA8C1C92EC4A1D4AF',2,2,NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'B461523CE228404DB61261E5936FEB12',4,2,NULL),
	(6,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'4DFFC7D959244F969B3A3712645FF8D4',2,3,NULL),
	(8,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'199CF52AB917407B8AD780BEA204641C',2,4,NULL),
	(10,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'566048681FB44E3DB196F2DFD93DE44D',7,4,NULL),
	(11,'2013-03-12 12:39:45','2013-03-12 18:20:20',X'98191B8C361142F693807203E9C1CC33',8,2,NULL),
	(12,'2013-03-12 12:39:45','2013-03-12 18:20:20',X'7C81FE6C213742639F231291D1E67263',8,3,NULL),
	(13,'2013-03-12 12:39:46','2013-03-12 18:20:20',X'57ACAF6FD6A84B19846AA4C32D5C4F21',8,4,NULL);

/*!40000 ALTER TABLE `workflowresourcetype` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowsamplesubtype
# ------------------------------------------------------------

LOCK TABLES `workflowsamplesubtype` WRITE;
/*!40000 ALTER TABLE `workflowsamplesubtype` DISABLE KEYS */;

INSERT INTO `workflowsamplesubtype` (`id`, `created`, `updated`, `uuid`, `samplesubtypeid`, `workflowid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'8494D0A06CF049B189E94F26F0DF9FCA',2,1,NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'5FDAD32DB21F480AAA28E3211F5AAE67',3,1,NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'BA33FFE5438D4E408AC7BE4C821A8D1F',4,1,NULL),
	(4,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'FFF92CA4418A4091866CC260BA4005F4',5,2,NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'FF3B01B772DB42298E7C9ED5659B5113',6,2,NULL),
	(6,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'ACC242019E65485AB41E0D18C5986954',7,2,NULL),
	(7,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'D45993423B4E4530ABFEA37D09830E93',8,4,NULL),
	(8,'2013-03-12 12:39:45','2013-03-12 18:20:20',X'3E5BA3C3AEEA41C89E168C470DB49A0C',13,3,NULL),
	(9,'2013-03-12 12:39:45','2013-03-12 18:20:20',X'4BAA48B3E5994E2E9D1F89AD4D13DB06',11,3,NULL),
	(10,'2013-03-12 12:39:45','2013-03-12 18:20:20',X'9ABFD096C74143FFBE8B30FB76646B0B',12,3,NULL);

/*!40000 ALTER TABLE `workflowsamplesubtype` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table workflowsoftware
# ------------------------------------------------------------

LOCK TABLES `workflowsoftware` WRITE;
/*!40000 ALTER TABLE `workflowsoftware` DISABLE KEYS */;

INSERT INTO `workflowsoftware` (`id`, `created`, `updated`, `uuid`, `softwareid`, `workflowid`, `lastupdatebyuser`)
VALUES
	(1,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'3E42688D8ECB48F7BFF595AD8266AFC5',3,1,NULL),
	(7,'2013-08-06 10:03:42','2013-08-06 10:03:42',X'39D3A7DB89974392A59B0E5EEB5F5060',2,2,1),
	(8,'2013-08-06 10:03:42','2013-08-06 10:03:42',X'311B0EF67D0F4A2FBFE90B56503ACED8',1,2,1),
	(9,'2013-08-06 10:03:42','2013-08-06 10:03:42',X'1A45350884004C94976D638E5B109EE4',8,2,1),
	(10,'2013-08-06 10:03:53','2013-08-06 10:03:53',X'E449C72271BB4A529702781EDD250566',1,3,1),
	(11,'2013-08-06 10:03:53','2013-08-06 10:03:53',X'545F99E89F384B9ABBD77927F01E0E03',8,3,1),
	(12,'2013-08-06 10:04:03','2013-08-06 10:04:03',X'D43B97467D2B46A2810376AC980795FA',4,4,1),
	(13,'2013-08-06 10:04:03','2013-08-06 10:04:03',X'37A15F010DDB44E6A4F176AC56858339',1,4,1),
	(14,'2013-08-06 10:04:03','2013-08-06 10:04:03',X'E3F34ABB543A405C8A88BE6C0B1C4DEA',8,4,1);

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
	(1,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'9C3F73294B114AFDA43A861E45F90323','system','Facilities Manager','fm',NULL),
	(2,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'9A83A90F669646DF8C5893932B714CA6','system','System Administrator','sa',NULL),
	(3,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'1246ED2D05A5474EB0547B091F035CEB','system','General Administrator','ga',NULL),
	(4,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'5899784899B841D5B9181C1DFB2137D4','department','Department Administrator','da',NULL),
	(5,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'15AC1B7B67154CFD908640F7F4D45A61','system','Facilities Tech','ft',NULL),
	(6,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'CCACC1BD0F5440C69277E286AA51CB1C','lab','Primary Investigator','pi',NULL),
	(7,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'A95204D19D1A44C8A6AC63E282AD3DA3','lab','Lab Manager','lm',NULL),
	(8,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'8BEA294813A740C197E27159AA6EC082','lab','Lab Member','lu',NULL),
	(9,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'0A892087C552488EA7AACCDB32668666','job','Job Submitter','js',NULL),
	(10,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'64188E72A36A4C75AAA4E9C22D8C6D31','job','Job Viewer','jv',NULL),
	(11,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'884D477070704C72B616C211113FF889','system','Super User','su',NULL),
	(12,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'91748F61E6E44FEDB91A1F1CF95C492E','lab','Lab Member Inactive','lx',NULL),
	(13,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'33640EC84E564160AD486E9D770EFB58','lab','Lab Member Pending','lp',NULL),
	(14,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'22FE7ABD37CF4F30A1D4D69EA30EBAA9','jobdraft','Job Drafter','jd',NULL),
	(15,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'50AF9690E5D340A0A048BFA09F3574F3','user','User','u',NULL),
	(16,'2013-03-12 12:39:40','2013-03-12 18:20:20',X'32A92639A1A04339AB7A1C4A8F64E2A5','system','wasp','w',NULL);

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
