-- MySQL dump 10.13  Distrib 5.1.34, for pc-linux-gnu (i686)
--
-- Host: localhost    Database: waspv1
-- ------------------------------------------------------
-- Server version	5.1.34

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
-- Table structure for table `access_control`
--

DROP TABLE IF EXISTS `access_control`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `access_control` (
  `access_control_id` int(11) NOT NULL AUTO_INCREMENT,
  `access_level` varchar(45) NOT NULL COMMENT 'user, lab, admin',
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`access_control_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `adaptor_sequence`
--

DROP TABLE IF EXISTS `adaptor_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `adaptor_sequence` (
  `adaptor_sequence_id` int(11) NOT NULL AUTO_INCREMENT,
  `adaptor_set_id` int(11) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `tag` varchar(45) NOT NULL,
  `adaptor` varchar(45) DEFAULT NULL,
  `index_number` int(11) NOT NULL,
  `pipeline_compatible` int(11) NOT NULL,
  `active` int(1) NOT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`adaptor_sequence_id`),
  KEY `adaptor_set_id` (`adaptor_set_id`)
) ENGINE=MyISAM AUTO_INCREMENT=109 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `adaptor_set`
--

DROP TABLE IF EXISTS `adaptor_set`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `adaptor_set` (
  `adaptor_set_id` int(11) NOT NULL AUTO_INCREMENT,
  `adaptor_set_name` varchar(45) NOT NULL,
  `demultiplex_code` int(2) DEFAULT NULL,
  `platform` varchar(45) NOT NULL,
  `end_read_type` varchar(45) DEFAULT NULL,
  `molecule` varchar(45) NOT NULL,
  `color` varchar(45) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`adaptor_set_id`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `analysis_file`
--

DROP TABLE IF EXISTS `analysis_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `analysis_file` (
  `analysis_file_id` int(11) NOT NULL AUTO_INCREMENT,
  `lane_id` int(11) NOT NULL,
  `location_computer` varchar(45) DEFAULT NULL,
  `location_directory` varchar(45) DEFAULT NULL,
  `location_file_name` varchar(45) DEFAULT NULL,
  `comments` text,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`analysis_file_id`),
  KEY `fk_analysis_file_lane` (`lane_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `antibody`
--

DROP TABLE IF EXISTS `antibody`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `antibody` (
  `antibody_id` int(11) NOT NULL AUTO_INCREMENT,
  `lab_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `manufacturer` varchar(45) DEFAULT NULL,
  `catalog_number` varchar(45) DEFAULT NULL,
  `host_animal` varchar(45) DEFAULT NULL,
  `volume_used` varchar(45) DEFAULT NULL,
  `comments` varchar(45) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`antibody_id`),
  KEY `fk_antibody_lab_user` (`lab_id`,`user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=36 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `assay`
--

DROP TABLE IF EXISTS `assay`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `assay` (
  `assay_id` int(11) NOT NULL AUTO_INCREMENT,
  `assay_name` varchar(45) NOT NULL,
  `general_assay_type` varchar(45) NOT NULL COMMENT 'SEQ, MICROARRAY',
  `primer_adaptor_type` varchar(45) DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`assay_id`)
) ENGINE=MyISAM AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `assay_adaptor_set`
--

DROP TABLE IF EXISTS `assay_adaptor_set`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `assay_adaptor_set` (
  `assay_id` int(11) NOT NULL,
  `adaptor_set_id` int(11) NOT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`assay_id`,`adaptor_set_id`),
  KEY `adaptor_set_id` (`adaptor_set_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `billing_location`
--

DROP TABLE IF EXISTS `billing_location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `billing_location` (
  `billing_location_id` int(11) NOT NULL AUTO_INCREMENT,
  `billing_dept_name` varchar(45) DEFAULT NULL COMMENT 'such as Billing Dept ',
  `contact_name` varchar(45) DEFAULT NULL,
  `institution_name` varchar(45) DEFAULT NULL,
  `building_room` varchar(45) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `state` varchar(45) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `zip` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `fax` varchar(45) DEFAULT NULL,
  `secondary_contact` varchar(45) DEFAULT NULL,
  `comments` varchar(45) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`billing_location_id`)
) ENGINE=MyISAM AUTO_INCREMENT=18 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `code_table`
--

DROP TABLE IF EXISTS `code_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `code_table` (
  `table_name` varchar(45) NOT NULL,
  `column_name` varchar(45) NOT NULL,
  `value` varchar(45) NOT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `contact`
--

DROP TABLE IF EXISTS `contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contact` (
  `contact_id` int(11) NOT NULL AUTO_INCREMENT,
  `contact_type` varchar(45) DEFAULT NULL COMMENT 'Billing, Lab Manager, Other (consult code table)',
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `institution` varchar(45) DEFAULT NULL,
  `building_room` varchar(45) DEFAULT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `fax` varchar(45) DEFAULT NULL,
  `active` tinyint(1) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`contact_id`)
) ENGINE=MyISAM AUTO_INCREMENT=66 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cost`
--

DROP TABLE IF EXISTS `cost`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cost` (
  `cost_id` int(11) NOT NULL AUTO_INCREMENT,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `assay_platform` varchar(45) DEFAULT NULL,
  `general_assay_type` varchar(45) DEFAULT NULL,
  `internal_external_lab` varchar(45) DEFAULT NULL COMMENT 'from table lab',
  `end_read_type` varchar(45) DEFAULT NULL COMMENT 'from table job',
  `read_length` int(11) DEFAULT NULL,
  `material_provided` varchar(45) DEFAULT NULL COMMENT 'from table sample',
  `area_on_flx_chamber` varchar(45) DEFAULT NULL COMMENT 'on table sample (ROCHE ONLY)',
  `cost_library_prep` double DEFAULT NULL,
  `cost_library_adaptors` double DEFAULT NULL,
  `cost_seq_primers` double DEFAULT NULL,
  `cost_seq_run` double DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`cost_id`)
) ENGINE=MyISAM AUTO_INCREMENT=73 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `discount`
--

DROP TABLE IF EXISTS `discount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `discount` (
  `discount_id` int(11) NOT NULL AUTO_INCREMENT,
  `research_center_id` int(11) NOT NULL,
  `discount` float NOT NULL,
  `discount_start_date` date NOT NULL,
  `discount_end_date` date NOT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`discount_id`),
  KEY `research_center_id` (`research_center_id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `esf_staff`
--

DROP TABLE IF EXISTS `esf_staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `esf_staff` (
  `esf_staff_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `staff_level` varchar(45) DEFAULT NULL COMMENT 'supervisor, manager, technician, scientist',
  `start_date` date DEFAULT NULL,
  `termination_date` date DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`esf_staff_id`),
  KEY `fk_esf_staff_user` (`user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `flanking_sequence_NOT_USED`
--

DROP TABLE IF EXISTS `flanking_sequence_NOT_USED`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flanking_sequence_NOT_USED` (
  `flanking_sequence_id` int(11) NOT NULL AUTO_INCREMENT,
  `assay_id` int(11) NOT NULL,
  `platform` varchar(45) NOT NULL COMMENT 'ROCHE or ILLUMINA',
  `adaptor_set` varchar(45) DEFAULT NULL,
  `molecule` varchar(45) DEFAULT NULL,
  `tag` varchar(45) NOT NULL,
  `adaptor` varchar(45) NOT NULL,
  `index_number` int(11) NOT NULL,
  `pipeline_compatible` int(11) NOT NULL,
  `active` int(1) NOT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`flanking_sequence_id`),
  KEY `assay_id` (`assay_id`)
) ENGINE=MyISAM AUTO_INCREMENT=231 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `flow_cell`
--

DROP TABLE IF EXISTS `flow_cell`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flow_cell` (
  `flow_cell_id` int(11) NOT NULL AUTO_INCREMENT,
  `flow_cell_name` varchar(45) NOT NULL COMMENT 'unique; could be the primary key',
  `assay_platform` varchar(45) NOT NULL,
  `maximum_number_of_lanes` int(11) DEFAULT NULL,
  `read_length` int(11) DEFAULT NULL COMMENT '36; 75; 100 if Illumina',
  `end_read_type` varchar(45) DEFAULT NULL COMMENT 'SINGLE-END READ; PAIRED-END READ',
  `start_datetime` datetime DEFAULT NULL,
  `start_esf_staff_id` int(11) DEFAULT NULL,
  `end_datetime` datetime DEFAULT NULL,
  `end_esf_staff_id` int(11) DEFAULT NULL,
  `comments` text,
  `flow_cell_final_status` varchar(45) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL COMMENT '36; 75; 100 (if Illumina)',
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`flow_cell_id`),
  UNIQUE KEY `unique_flow_cell_flow_cell_name` (`flow_cell_name`),
  KEY `fk_flow_cell_esf_staff` (`start_esf_staff_id`),
  KEY `fk_flow_cell_esf_staff1` (`end_esf_staff_id`),
  KEY `index_flow_cell_flow_cell_name` (`flow_cell_name`)
) ENGINE=MyISAM AUTO_INCREMENT=87 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `funding_confirmation`
--

DROP TABLE IF EXISTS `funding_confirmation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `funding_confirmation` (
  `job_id` int(11) NOT NULL,
  `grant_approver_id` int(11) NOT NULL,
  `funding_approval_status` varchar(20) NOT NULL,
  `amount` varchar(20) DEFAULT NULL,
  `date_recorded` date NOT NULL,
  `comments` varchar(300) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`job_id`),
  KEY `grant_approver_id` (`grant_approver_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `grant_approver`
--

DROP TABLE IF EXISTS `grant_approver`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `grant_approver` (
  `grant_approver_id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `email` varchar(45) DEFAULT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `fax` varchar(45) DEFAULT NULL,
  `institution` varchar(45) DEFAULT NULL,
  `department` varchar(45) DEFAULT NULL,
  `building_room` varchar(45) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `state` varchar(45) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `zip` varchar(45) DEFAULT NULL,
  `comments` varchar(150) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`grant_approver_id`)
) ENGINE=MyISAM AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `grant_info`
--

DROP TABLE IF EXISTS `grant_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `grant_info` (
  `grant_info_id` int(11) NOT NULL AUTO_INCREMENT,
  `lab_id` int(11) NOT NULL,
  `grant_number` varchar(45) NOT NULL,
  `expiration_date` date DEFAULT NULL,
  `active` tinyint(1) NOT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`grant_info_id`),
  KEY `fk_grant_info_lab` (`lab_id`)
) ENGINE=MyISAM AUTO_INCREMENT=98 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice` (
  `invoice_number` varchar(45) NOT NULL,
  `job_id` int(11) NOT NULL,
  `grant_number_or_PO` varchar(45) DEFAULT NULL,
  `billing_location_id` int(11) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`invoice_number`),
  KEY `fk_invoice_job` (`job_id`),
  KEY `fk_invoice_billing_location` (`billing_location_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `job`
--

DROP TABLE IF EXISTS `job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `job` (
  `job_id` int(11) NOT NULL AUTO_INCREMENT,
  `lab_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `project_id` int(11) DEFAULT NULL,
  `assay_id` int(11) NOT NULL,
  `job_name` varchar(45) NOT NULL,
  `assay_platform` varchar(45) DEFAULT NULL COMMENT 'If SEQ, then platforms are ROCHE or ILLUMINA',
  `read_length` int(11) DEFAULT NULL COMMENT '36, 75, 100',
  `end_read_type` varchar(45) DEFAULT NULL COMMENT 'SINGLE-END READ; PAIRED-END READ',
  `multiplexing` varchar(45) DEFAULT NULL COMMENT 'YES; NO',
  `required_result_date` date DEFAULT NULL,
  `priority` varchar(45) DEFAULT NULL COMMENT 'nornal, rush',
  `prep_procedure` varchar(100) DEFAULT NULL,
  `kit` varchar(100) DEFAULT NULL,
  `experimental_description` text,
  `comments` text,
  `number_of_samples` int(11) DEFAULT NULL,
  `number_of_lanes` int(11) DEFAULT NULL,
  `number_of_microarrays` int(11) DEFAULT NULL,
  `po_number` varchar(45) DEFAULT NULL COMMENT 'not always needed; some paying off grant',
  `check_number` varchar(45) DEFAULT NULL,
  `grant_info_id` int(11) DEFAULT NULL,
  `contact_id` int(11) DEFAULT NULL COMMENT 'should be named science_contact_id',
  `billing_location_id` int(11) DEFAULT NULL,
  `submission_date` date DEFAULT NULL,
  `research_centers_with_discounts` varchar(300) DEFAULT NULL,
  `pi_accept_withdraw_job` varchar(20) DEFAULT NULL,
  `date_pi_accept_withdraw_job` date DEFAULT NULL,
  `accept_reject_withdraw` varchar(45) DEFAULT NULL COMMENT 'ACCEPTED; REJECTED; WITHDRAWN',
  `date_accept_reject_withdraw` date DEFAULT NULL,
  `reason_accept_reject_withdraw` varchar(50) DEFAULT NULL,
  `pipeline_compatible` int(11) DEFAULT '1',
  `completion_status` int(11) DEFAULT NULL,
  `completion_date` date DEFAULT NULL,
  `date_esf_terminated` date DEFAULT NULL,
  `reason_esf_terminated` varchar(100) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`job_id`),
  KEY `fk_job_assay` (`assay_id`),
  KEY `fk_job_project` (`project_id`),
  KEY `fk_job_contact` (`contact_id`),
  KEY `fk_job_grant_info` (`grant_info_id`),
  KEY `fk_job_lab_user` (`lab_id`,`user_id`),
  KEY `fk_job_billing_location` (`billing_location_id`)
) ENGINE=MyISAM AUTO_INCREMENT=10215 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `job_sample`
--

DROP TABLE IF EXISTS `job_sample`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `job_sample` (
  `sample_id` int(11) NOT NULL,
  `job_id` int(11) NOT NULL,
  `coverage` int(11) NOT NULL,
  `multiplex` int(1) NOT NULL,
  `processed_by_pipeline` int(11) NOT NULL DEFAULT '0',
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`sample_id`,`job_id`),
  KEY `fk_sample_has_job_sample` (`sample_id`),
  KEY `fk_sample_has_job_job` (`job_id`),
  KEY `index_job_id` (`job_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `job_sequence_run`
--

DROP TABLE IF EXISTS `job_sequence_run`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `job_sequence_run` (
  `job_sequence_run_id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL,
  `sequence_run_id` int(11) NOT NULL,
  `pending_status` smallint(6) NOT NULL DEFAULT '1',
  `analysis_status` smallint(6) DEFAULT '0',
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`job_sequence_run_id`),
  UNIQUE KEY `unique_key_j_s_r` (`job_id`,`sequence_run_id`),
  KEY `sequence_run_id` (`sequence_run_id`)
) ENGINE=MyISAM AUTO_INCREMENT=221 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lab`
--

DROP TABLE IF EXISTS `lab`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lab` (
  `lab_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'use as account number;  can we start at 10000?',
  `pi_id` int(11) NOT NULL COMMENT 'this should be unique',
  `lab_name` varchar(45) NOT NULL,
  `internal_external_lab` varchar(45) DEFAULT NULL COMMENT 'INTERNAL; EXTERNAL lab to Einstein',
  `building_room` varchar(45) DEFAULT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`lab_id`),
  UNIQUE KEY `unique_pi_id` (`pi_id`),
  KEY `fk_lab_user` (`pi_id`),
  KEY `index_pi_id` (`pi_id`)
) ENGINE=MyISAM AUTO_INCREMENT=126 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lab_billing_location`
--

DROP TABLE IF EXISTS `lab_billing_location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lab_billing_location` (
  `lab_id` int(11) NOT NULL,
  `billing_location_id` int(11) NOT NULL,
  `active` tinyint(1) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`lab_id`,`billing_location_id`),
  KEY `fk_lab_has_billing_location_lab` (`lab_id`),
  KEY `fk_lab_has_billing_location_billing_location` (`billing_location_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lab_contact`
--

DROP TABLE IF EXISTS `lab_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lab_contact` (
  `lab_id` int(11) NOT NULL,
  `contact_id` int(11) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`lab_id`,`contact_id`),
  KEY `fk_lab_has_contact_lab` (`lab_id`),
  KEY `fk_lab_has_contact_contact` (`contact_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lab_research_center`
--

DROP TABLE IF EXISTS `lab_research_center`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lab_research_center` (
  `lab_id` int(11) NOT NULL,
  `research_center_id` int(11) NOT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`lab_id`,`research_center_id`),
  KEY `research_center_id` (`research_center_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lab_user`
--

DROP TABLE IF EXISTS `lab_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lab_user` (
  `lab_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`lab_id`,`user_id`),
  KEY `fk_lab_has_user_lab` (`lab_id`),
  KEY `fk_lab_has_user_user` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lane`
--

DROP TABLE IF EXISTS `lane`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lane` (
  `lane_id` int(11) NOT NULL AUTO_INCREMENT,
  `flow_cell_id` int(11) NOT NULL,
  `lane_number` int(11) NOT NULL,
  `control_name` varchar(45) DEFAULT NULL,
  `control_pmol_applied` varchar(45) DEFAULT NULL,
  `actual_read_length` int(11) DEFAULT NULL,
  `lane_success` tinyint(1) DEFAULT NULL COMMENT 'did this lane perform OK',
  `comments` text,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`lane_id`),
  UNIQUE KEY `flow_cell_id` (`flow_cell_id`,`lane_number`)
) ENGINE=MyISAM AUTO_INCREMENT=572 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lane_NOT_USED`
--

DROP TABLE IF EXISTS `lane_NOT_USED`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lane_NOT_USED` (
  `lane_id` int(11) NOT NULL AUTO_INCREMENT,
  `flow_cell_id` int(11) NOT NULL,
  `library_id` int(11) DEFAULT NULL,
  `lane_number` int(11) NOT NULL,
  `job_id` int(11) DEFAULT NULL,
  `pmol_applied` varchar(45) DEFAULT NULL COMMENT 'pmoles applied to a lane',
  `library_dilution` varchar(45) DEFAULT NULL,
  `volume_of_dilution_applied` varchar(45) DEFAULT NULL,
  `actual_read_length` int(11) DEFAULT NULL,
  `lane_success` tinyint(1) DEFAULT NULL COMMENT 'did this lane perform OK',
  `analysis_success` tinyint(1) DEFAULT NULL COMMENT 'did the analysis of this lane succeed',
  `comments` text,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`lane_id`),
  KEY `fk_lane_library` (`library_id`),
  KEY `fk_lane_flow_cell` (`flow_cell_id`)
) ENGINE=MyISAM AUTO_INCREMENT=214 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `lane_library`
--

DROP TABLE IF EXISTS `lane_library`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lane_library` (
  `lane_id` int(11) NOT NULL,
  `library_id` int(11) NOT NULL,
  `job_id` int(11) NOT NULL,
  `pmol_applied` varchar(45) DEFAULT NULL,
  `lane_library_success` tinyint(1) DEFAULT NULL,
  `analysis_success` tinyint(1) DEFAULT NULL,
  `comments` text,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`lane_id`,`library_id`),
  KEY `library_id` (`library_id`),
  KEY `job_id` (`job_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `library`
--

DROP TABLE IF EXISTS `library`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `library` (
  `library_id` int(11) NOT NULL AUTO_INCREMENT,
  `sample_id` int(11) NOT NULL,
  `library_name` varchar(45) NOT NULL,
  `library_size` int(11) DEFAULT NULL,
  `library_size_sd` int(11) DEFAULT NULL,
  `flanking_sequence_id_NOT_USED` int(11) DEFAULT NULL,
  `adaptor_sequence_id` int(11) DEFAULT NULL,
  `rna_strand_method_id` int(11) DEFAULT NULL,
  `rna_strand` int(11) DEFAULT NULL,
  `pipeline_compatible` int(11) DEFAULT NULL,
  `library_created_by_submitter` varchar(45) DEFAULT NULL COMMENT 'YES; NO',
  `library_created_by_id` int(11) DEFAULT NULL COMMENT 'esf_staff_id',
  `library_created_date` date DEFAULT NULL,
  `library_completed_date` date DEFAULT NULL,
  `library_final_status` varchar(45) DEFAULT NULL,
  `library_success` tinyint(1) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`library_id`),
  KEY `fk_library_sample` (`sample_id`),
  KEY `fk_library_esf_staff` (`library_created_by_id`),
  KEY `flanking_sequence_id` (`flanking_sequence_id_NOT_USED`),
  KEY `fk_rna_strand_method` (`rna_strand_method_id`)
) ENGINE=MyISAM AUTO_INCREMENT=869 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `line_item`
--

DROP TABLE IF EXISTS `line_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `line_item` (
  `invoice_number` varchar(45) NOT NULL,
  `sample_id` int(11) NOT NULL,
  `line_item_number` int(11) DEFAULT NULL,
  `cost` bigint(20) DEFAULT NULL,
  `discount` float DEFAULT NULL,
  `charge` bigint(20) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`invoice_number`,`sample_id`),
  KEY `fk_invoice_has_sample_invoice` (`invoice_number`),
  KEY `fk_invoice_has_sample_sample` (`sample_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `machine`
--

DROP TABLE IF EXISTS `machine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `machine` (
  `machine_name` varchar(45) NOT NULL COMMENT 'HWI.EAS438 (ILLUMINA); FLX07080405 (ROCHE)',
  `assay_platform` varchar(45) NOT NULL COMMENT 'ROCHE; ILLUMINA',
  `machine_type` varchar(45) DEFAULT NULL,
  `serial_number` varchar(45) DEFAULT NULL,
  `commission_date` date DEFAULT NULL,
  `decommission_date` date DEFAULT NULL,
  `comments` text,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`machine_name`),
  UNIQUE KEY `unique_machine_name` (`machine_name`),
  KEY `index_machine_name` (`machine_name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `manual_job`
--

DROP TABLE IF EXISTS `manual_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `manual_job` (
  `manual_job_id` int(11) NOT NULL AUTO_INCREMENT,
  `lab_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `project_id` int(11) NOT NULL,
  `assay_id` int(11) NOT NULL,
  `job_name` varchar(45) NOT NULL,
  `email_sent` int(11) DEFAULT NULL,
  `processing_status` int(11) DEFAULT NULL,
  `job_process_string` text,
  `date_recorded` date NOT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`manual_job_id`),
  KEY `fk_manual_job_lab_user1` (`lab_id`,`user_id`),
  KEY `fk_manual_job_project1` (`project_id`),
  KEY `fk_manual_job_assay1` (`assay_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1157 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `microarray`
--

DROP TABLE IF EXISTS `microarray`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `microarray` (
  `microarray_id` int(11) NOT NULL AUTO_INCREMENT,
  `lab_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `manufacturer` varchar(45) DEFAULT NULL COMMENT 'same as platform (manufacturer)',
  `array_design_name` varchar(45) DEFAULT NULL COMMENT 'name of microarray??',
  `design_id` int(11) DEFAULT NULL COMMENT 'custom number (B123-00-11) or a catalog number (123)',
  `ucsc_genome_version` varchar(45) DEFAULT NULL,
  `array_format` varchar(45) DEFAULT NULL COMMENT 'like 2.1M or 4 x 75K',
  `arrays_per_slide` int(11) DEFAULT NULL,
  `species_id` int(11) DEFAULT NULL,
  `comments` varchar(45) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`microarray_id`),
  KEY `fk_microarray_species` (`species_id`),
  KEY `fk_microarray_lab_user` (`lab_id`,`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `multiplex`
--

DROP TABLE IF EXISTS `multiplex`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `multiplex` (
  `multiplex_id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL,
  `sample_id` int(11) NOT NULL,
  `lane_index` int(11) NOT NULL,
  `intralane_index` int(11) NOT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`multiplex_id`),
  UNIQUE KEY `unique_key1` (`job_id`,`sample_id`,`lane_index`),
  KEY `sample_id` (`sample_id`)
) ENGINE=MyISAM AUTO_INCREMENT=460 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `password`
--

DROP TABLE IF EXISTS `password`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `password` (
  `user_id` int(11) NOT NULL,
  `password` varchar(45) NOT NULL,
  `password_as_hash` varchar(45) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `fk_password_user` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pcr_enrichment_chip_seq`
--

DROP TABLE IF EXISTS `pcr_enrichment_chip_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pcr_enrichment_chip_seq` (
  `pcr_enrichment_chip_seq_id` int(11) NOT NULL AUTO_INCREMENT,
  `library1_id` int(11) DEFAULT NULL,
  `library2_id` int(11) DEFAULT NULL,
  `primer_pair_id` int(11) DEFAULT NULL,
  `pcr_machine` varchar(45) DEFAULT NULL,
  `start_datetime` datetime DEFAULT NULL,
  `end_datetime` datetime DEFAULT NULL,
  `volume_primer1` float DEFAULT NULL,
  `volume_primer2` float DEFAULT NULL,
  `buffer` varchar(45) DEFAULT NULL,
  `comments` varchar(45) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`pcr_enrichment_chip_seq_id`),
  KEY `fk_pcr_enrichment_chip_seq_library` (`library1_id`),
  KEY `fk_pcr_enrichment_chip_seq_library1` (`library2_id`),
  KEY `fk_pcr_enrichment_chip_seq_pcr_primer_pair` (`primer_pair_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pcr_enrichment_qc_files`
--

DROP TABLE IF EXISTS `pcr_enrichment_qc_files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pcr_enrichment_qc_files` (
  `pcr_enrichment_qc_files_id` int(11) NOT NULL AUTO_INCREMENT,
  `pcr_enrichment_chip_seq_id` int(11) NOT NULL,
  `file_name` varchar(100) DEFAULT NULL,
  `directory` varchar(150) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`pcr_enrichment_qc_files_id`),
  KEY `fk_pcr_enrichment_qc_files_pcr_enrichment_chip_seq` (`pcr_enrichment_chip_seq_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pcr_primer_pair`
--

DROP TABLE IF EXISTS `pcr_primer_pair`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pcr_primer_pair` (
  `primer_pair_id` int(11) NOT NULL AUTO_INCREMENT,
  `lab_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `species_id` int(11) DEFAULT NULL,
  `gene` varchar(45) DEFAULT NULL,
  `reference_genome_reference_genome_id` int(11) DEFAULT NULL COMMENT 'may not be needed',
  `genome` varchar(15) DEFAULT NULL,
  `chromosome` varchar(45) DEFAULT NULL COMMENT 'may not be needed',
  `forward_primer_name` varchar(45) NOT NULL,
  `forward_chromosome` varchar(45) DEFAULT NULL,
  `forward_chr_start` bigint(20) DEFAULT NULL,
  `forward_chr_end` bigint(20) DEFAULT NULL,
  `forward_sequence` varchar(45) NOT NULL,
  `forward_concentration` int(11) DEFAULT NULL,
  `forward_volume` int(11) DEFAULT NULL,
  `forward_tm` int(11) DEFAULT NULL,
  `reverse_primer_name` varchar(45) NOT NULL,
  `reverse_chromosome` varchar(45) DEFAULT NULL,
  `reverse_chr_start` bigint(20) DEFAULT NULL,
  `reverse_chr_end` bigint(20) DEFAULT NULL,
  `reverse_sequence` varchar(45) NOT NULL,
  `reverse_concentration` int(11) DEFAULT NULL,
  `reverse_volume` int(11) DEFAULT NULL,
  `reverse_tm` int(11) DEFAULT NULL,
  `comments` text,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`primer_pair_id`),
  KEY `fk_pcr_primer_pair_species` (`species_id`),
  KEY `fk_pcr_primer_pair_lab_user` (`lab_id`,`user_id`),
  KEY `fk_pcr_primer_pair_reference_genome` (`reference_genome_reference_genome_id`)
) ENGINE=MyISAM AUTO_INCREMENT=62 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pilot_project`
--

DROP TABLE IF EXISTS `pilot_project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pilot_project` (
  `pilot_project_id` int(11) NOT NULL AUTO_INCREMENT,
  `project_title` varchar(100) NOT NULL,
  `user_id` int(11) NOT NULL,
  `access_number` int(11) NOT NULL,
  `center_membership_1` varchar(100) DEFAULT NULL,
  `center_membership_2` varchar(100) DEFAULT NULL,
  `center_membership_3` varchar(100) DEFAULT NULL,
  `center_membership_4` varchar(100) DEFAULT NULL,
  `is_translational` varchar(5) NOT NULL,
  `why_is_translational` text,
  `research_area` varchar(100) NOT NULL,
  `research_area_other` varchar(50) DEFAULT NULL,
  `departmental_administrator_name` varchar(100) NOT NULL,
  `departmental_administrator_email` varchar(50) NOT NULL,
  `departmental_administrator_phone` varchar(15) NOT NULL,
  `grant_number_1` varchar(100) NOT NULL,
  `grant_number_2` varchar(100) DEFAULT NULL,
  `grant_number_3` varchar(100) DEFAULT NULL,
  `description_file` varchar(150) NOT NULL,
  `description_file_complete_path` varchar(200) NOT NULL,
  `funding_file` varchar(150) NOT NULL,
  `funding_file_complete_path` varchar(200) NOT NULL,
  `submission_date` date NOT NULL,
  `grant_expired` int(1) DEFAULT '0',
  `max_num_lanes_funded` int(11) DEFAULT '10',
  `accept_or_reject` varchar(10) DEFAULT NULL,
  `accept_or_reject_date` date DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`pilot_project_id`),
  UNIQUE KEY `unique_access_number` (`access_number`),
  KEY `user_id` (`user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=24 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pilot_project_job`
--

DROP TABLE IF EXISTS `pilot_project_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pilot_project_job` (
  `job_id` int(11) NOT NULL,
  `pilot_project_id` int(11) NOT NULL,
  `num_lanes` int(11) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`job_id`),
  KEY `pilot_project_id` (`pilot_project_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project` (
  `project_id` int(11) NOT NULL AUTO_INCREMENT,
  `lab_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `project_name` varchar(45) NOT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`project_id`),
  KEY `fk_project_lab_user` (`lab_id`,`user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=245 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reference_genome`
--

DROP TABLE IF EXISTS `reference_genome`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reference_genome` (
  `reference_genome_id` int(11) NOT NULL AUTO_INCREMENT,
  `reference_genome_name` varchar(45) NOT NULL,
  `species_id` int(11) DEFAULT NULL,
  `active` int(11) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`reference_genome_id`),
  KEY `fk_reference_genome_species` (`species_id`)
) ENGINE=MyISAM AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `research_center`
--

DROP TABLE IF EXISTS `research_center`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `research_center` (
  `research_center_id` int(11) NOT NULL AUTO_INCREMENT,
  `research_center_name` varchar(150) NOT NULL,
  `user_list_location` varchar(100) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`research_center_id`),
  UNIQUE KEY `research_center_name` (`research_center_name`)
) ENGINE=MyISAM AUTO_INCREMENT=18 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `rna_strand_method`
--

DROP TABLE IF EXISTS `rna_strand_method`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rna_strand_method` (
  `rna_strand_method_id` int(11) NOT NULL AUTO_INCREMENT,
  `rna_strand_method` varchar(45) NOT NULL,
  `rna_strand` int(11) NOT NULL,
  `adaptor_molecule` varchar(45) NOT NULL,
  `active` int(1) NOT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`rna_strand_method_id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sample`
--

DROP TABLE IF EXISTS `sample`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sample` (
  `sample_id` int(11) NOT NULL AUTO_INCREMENT,
  `lab_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `sample_name` varchar(45) NOT NULL,
  `material_provided` varchar(45) DEFAULT NULL COMMENT 'gDNA; cDNA; LIBRARY',
  `area_on_flx_chamber` varchar(45) DEFAULT NULL COMMENT '100%; 50%; 25%; 12.5%; 6.25%',
  `reference_genome_id` int(11) DEFAULT NULL,
  `species_id` int(11) DEFAULT NULL COMMENT 'reference_genome replaces species_id',
  `sex` varchar(45) DEFAULT NULL COMMENT 'sex needed ONLY for CGH',
  `fragment_size` int(11) DEFAULT NULL,
  `fragment_size_sd` int(11) DEFAULT NULL,
  `amount` float DEFAULT NULL,
  `concentration` float DEFAULT NULL,
  `260_280` float DEFAULT NULL,
  `260_230` float DEFAULT NULL,
  `volume` float DEFAULT NULL,
  `buffer` varchar(45) DEFAULT NULL COMMENT 'EB, TRIS, TE, WATER',
  `sample_type` varchar(45) DEFAULT NULL COMMENT 'Input, CHIP; this is redundant, as presence of antibody_id would mean input (unless control antibody)',
  `antibody_id` int(11) DEFAULT NULL,
  `enrich_primer_pair_id` int(11) DEFAULT NULL,
  `no_enrich_primer_pair_id` int(11) DEFAULT NULL,
  `comments` varchar(100) DEFAULT NULL,
  `restriction_enzyme` varchar(45) DEFAULT NULL COMMENT 'HpaII, MspI',
  `sample_label` varchar(45) DEFAULT NULL COMMENT 'CY3, CY5',
  `sample_date_received` date DEFAULT NULL,
  `sample_received_by_id` int(11) DEFAULT NULL COMMENT 'references esf_staff.esf_staff_id',
  `sample_location_stored` varchar(45) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`sample_id`),
  KEY `fk_sample_species` (`species_id`),
  KEY `fk_sample_antibody` (`antibody_id`),
  KEY `fk_sample_esf_staff` (`sample_received_by_id`),
  KEY `fk_sample_reference_genome` (`reference_genome_id`),
  KEY `fk_sample_lab_user` (`lab_id`,`user_id`),
  KEY `fk_sample_pcr_primer_pair` (`enrich_primer_pair_id`),
  KEY `fk_sample_pcr_primer_pair1` (`no_enrich_primer_pair_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1012 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sample_qc_file`
--

DROP TABLE IF EXISTS `sample_qc_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sample_qc_file` (
  `sample_qc_file_id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL,
  `file_name` varchar(150) DEFAULT NULL,
  `directory` varchar(150) NOT NULL COMMENT 'complete path name, including file name',
  `analysis_type` varchar(45) DEFAULT NULL COMMENT 'Agarose Gel, PAGE, Agilent 2100 Bioanalyzer, QPCR, Other',
  `comments` text,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`sample_qc_file_id`),
  KEY `fk_sample_qc_file_job` (`job_id`)
) ENGINE=MyISAM AUTO_INCREMENT=167 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sequence_capture_format`
--

DROP TABLE IF EXISTS `sequence_capture_format`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sequence_capture_format` (
  `sequence_capture_format_id` int(11) NOT NULL AUTO_INCREMENT,
  `manufacturer` varchar(45) NOT NULL,
  `format` varchar(100) NOT NULL,
  `active` int(1) NOT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`sequence_capture_format_id`),
  UNIQUE KEY `manufacturer` (`manufacturer`,`format`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sequence_capture_job_info`
--

DROP TABLE IF EXISTS `sequence_capture_job_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sequence_capture_job_info` (
  `sequence_capture_job_info_id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL,
  `manufacturer` varchar(45) NOT NULL,
  `format` varchar(100) NOT NULL,
  `comments` varchar(100) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`sequence_capture_job_info_id`),
  KEY `job_id` (`job_id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sequence_run`
--

DROP TABLE IF EXISTS `sequence_run`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sequence_run` (
  `sequence_run_id` int(11) NOT NULL AUTO_INCREMENT,
  `sequence_run_name` varchar(45) DEFAULT NULL,
  `flow_cell_name` varchar(45) NOT NULL,
  `start_datetime` datetime DEFAULT NULL,
  `start_esf_staff_id` int(11) DEFAULT NULL,
  `end_datetime` datetime DEFAULT NULL,
  `end_esf_staff_id` int(11) DEFAULT NULL,
  `machine_name` varchar(45) DEFAULT NULL,
  `comments` text,
  `path_to_data` varchar(200) DEFAULT NULL COMMENT 'at Andy''s request',
  `sequence_run_final_status` varchar(45) DEFAULT NULL,
  `run_success` tinyint(1) DEFAULT NULL COMMENT 'did entire run succeed',
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`sequence_run_id`),
  UNIQUE KEY `unique_sequence_run_sequence_run_name` (`sequence_run_name`),
  KEY `fk_sequence_run_start_esf_staff` (`start_esf_staff_id`),
  KEY `fk_sequence_run_esf_staff` (`end_esf_staff_id`),
  KEY `index_sequence_run_name` (`sequence_run_name`),
  KEY `fk_sequence_run_flow_cell_name` (`flow_cell_name`),
  KEY `fk_sequence_run_machine` (`machine_name`)
) ENGINE=MyISAM AUTO_INCREMENT=85 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sharing`
--

DROP TABLE IF EXISTS `sharing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sharing` (
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `species`
--

DROP TABLE IF EXISTS `species`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `species` (
  `species_id` int(11) NOT NULL AUTO_INCREMENT,
  `species_name` varchar(45) NOT NULL,
  `common_name` varchar(30) DEFAULT NULL,
  `active` tinyint(1) NOT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`species_id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `auth_code` varchar(2) NOT NULL DEFAULT '00' COMMENT 'authorizing a user; default = 00 = not authorized; Andy handles this',
  `WASP_username` varchar(45) DEFAULT NULL,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `title` varchar(45) DEFAULT NULL,
  `institution` varchar(45) DEFAULT NULL,
  `department` varchar(45) DEFAULT NULL,
  `building_room` varchar(45) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `state` varchar(45) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `zip` varchar(45) DEFAULT NULL,
  `email` varchar(45) NOT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `fax` varchar(45) DEFAULT NULL,
  `active` tinyint(1) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email_unique` (`email`),
  UNIQUE KEY `unique_user_wikilims_username` (`WASP_username`),
  KEY `index_user_wilkilims_username` (`WASP_username`)
) ENGINE=MyISAM AUTO_INCREMENT=237 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_access_control`
--

DROP TABLE IF EXISTS `user_access_control`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_access_control` (
  `user_id` int(11) NOT NULL,
  `access_control_id` int(11) NOT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`user_id`,`access_control_id`),
  KEY `fk_user_has_access_control_user` (`user_id`),
  KEY `fk_user_has_access_control_access_control` (`access_control_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_defined_amplicon`
--

DROP TABLE IF EXISTS `user_defined_amplicon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_defined_amplicon` (
  `sample_id` int(11) NOT NULL,
  `primer_pair_id` int(11) NOT NULL,
  PRIMARY KEY (`sample_id`,`primer_pair_id`),
  KEY `fk_sample_has_pcr_primer_pair_sample` (`sample_id`),
  KEY `fk_sample_has_pcr_primer_pair_pcr_primer_pair` (`primer_pair_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_defined_sample_pair`
--

DROP TABLE IF EXISTS `user_defined_sample_pair`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_defined_sample_pair` (
  `sample_pairs_id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL,
  `sample1_id` int(11) NOT NULL,
  `sample1_job_id` int(11) DEFAULT NULL,
  `sample2_id` int(11) DEFAULT NULL,
  `sample2_job_id` int(11) DEFAULT NULL,
  `standard_reference` varchar(45) DEFAULT NULL,
  `microarray_id` int(11) DEFAULT NULL COMMENT 'identifies the type of chip (mfr, species, etc)',
  `enrich_primer_pair_id` int(11) DEFAULT NULL,
  `no_enrich_primer_pair_id` int(11) DEFAULT NULL,
  `comments` varchar(100) DEFAULT NULL,
  `created_by` varchar(45) DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `modified_by` varchar(45) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  PRIMARY KEY (`sample_pairs_id`),
  KEY `fk_sample_pairs_microarray` (`microarray_id`),
  KEY `fk_sample_pairs_pcr_primer_pair` (`no_enrich_primer_pair_id`),
  KEY `fk_sample_pairs_pcr_primer_pair1` (`enrich_primer_pair_id`),
  KEY `fk_sample_pairs_sample` (`sample1_id`),
  KEY `fk_sample_pairs_sample1` (`sample2_id`),
  KEY `fk_user_defined_sample_pair_job` (`job_id`)
) ENGINE=MyISAM AUTO_INCREMENT=427 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-06-21 21:08:07
