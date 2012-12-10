-- Recreate Database 
GRANT USAGE ON *.* TO 'wasp'@'localhost';
DROP USER 'wasp'@'localhost';
DROP DATABASE IF EXISTS wasp;

create database wasp CHARACTER SET utf8 COLLATE utf8_general_ci;
create user 'wasp'@'localhost' IDENTIFIED BY 'waspV2';

grant all on wasp.* to 'wasp'@'localhost';

flush privileges;

use wasp;

--
-- META - for generic drop downs and such
--
create table meta (
  metaid int(10)  primary key auto_increment,

  property varchar(250) , 
  k varchar(250) , -- internal value?
  v TEXT , -- external label?
  position int(10)  default 0,
  rolevisibility VARCHAR(250),

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  constraint unique index u_meta_p_k (property, k)
) ENGINE=InnoDB charset=utf8;



--
-- USER 
--
create table user (
  userid int(10)  primary key auto_increment,

  login varchar(250) , 
  email varchar(250) , 
  password varchar(250) , 
  firstname varchar(250) , 
  lastname varchar(250) , 

  locale varchar(5)  default 'en_US',

  isactive int(1)  default 1,
  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0, 

  constraint unique index u_user_login (login),
  constraint unique index u_user_email (email)
) ENGINE=InnoDB charset=utf8;



-- insert into user values 
-- ( 1, 'admin', 'admin@localhost',  PASSWORD('waspV2'), 'Admin', '-', 1, now(), 1 );

create table usermeta (
  usermetaid int(10)  primary key auto_increment,
  userid int(10) ,

  k varchar(250) , 
  v TEXT, 
  position int(10)  default 0,
  rolevisibility VARCHAR(250),

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_usermeta_userid (userid) references user(userid),
  constraint unique index u_usermeta_k_uid (k, userid)
) ENGINE=InnoDB charset=utf8;

--
-- forgot password
--
create table userpasswordauth (
  userid int(10)  primary key, 
  authcode varchar(250) ,
  lastupdts timestamp  default current_timestamp, 
  lastupduser int(10)  default 0,

  foreign key fk_userpasswordauth_userid (userid) references user(userid),
  constraint unique index u_userpasswordauth (authcode)
) ENGINE=InnoDB charset=utf8;




--
-- ROLE
--
create table role ( 
  roleid int(10)  primary key auto_increment, 
  rolename varchar(250), 
  name varchar(250),
  domain varchar(20),

  constraint unique index u_role_rname (rolename),
  constraint unique index u_role_name (name)
) ENGINE=InnoDB charset=utf8;

insert into role values
(1, 'fm', 'Facilities Manager', 'system'),
(2, 'sa', 'System Administrator', 'system'),
(3, 'ga', 'General Administrator', 'system'),
(4, 'da', 'Department Administrator', 'department'), -- departmentuser, implicit
(5, 'ft', 'Facilities Tech', 'system'), 
(6, 'pi', 'Primary Investigator', 'lab'), -- labuser, explicit
(7, 'lm', 'Lab Manager', 'lab'), -- labuser, explicit
(8, 'lu', 'Lab Member', 'lab'), -- labuser, explicit
(9, 'js', 'Job Submitter', 'job'),-- jobuser, explicit
(10, 'jv', 'Job Viewer', 'job'), -- jobuser, explicit
(11, 'su', 'Super User', 'system'),
(12, 'lx', 'Lab Member Inactive', 'lab'), -- labuser, explicit
(13, 'lp', 'Lab Member Pending', 'lab'), -- labuser, explicit
(14, 'jd', 'Job Drafter', 'jobdraft'), -- labuser, explicit
(15, 'u', 'User', 'user');


create table roleset (
  rolesetid int(10)  primary key auto_increment, 
  parentroleid int(10) ,
  childroleid int(10) ,
  
  foreign key fk_roleset_prid (parentroleid) references role(roleid),
  foreign key fk_roleset_crid (childroleid) references role(roleid),

  constraint unique index u_role_rname (parentroleid, childroleid)
) ENGINE=InnoDB charset=utf8;

insert into roleset
(parentroleid, childroleid)
select 
roleid, roleid 
from role;

insert into roleset 
(parentroleid, childroleid)
values
(1, 5),
(6, 7),
(6, 8),
(7, 8),
(9, 10),
(11, 1),
(11, 2),
(11, 3),
(11, 5);


-- USER.ROLE
--
create table userrole (
  userroleid int(10)  primary key auto_increment, 

  userid int(10) , 
  roleid int(10) , 

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_userrole_rid (roleid) references role(roleid),
  foreign key fk_userrole_uid (userid) references user(userid),

  constraint unique index userrole_uid_rid (userid, roleid)
) ENGINE=InnoDB charset=utf8;


-- insert into userrole values (1, 1, 1, now(), 1);

--
-- DEPARTMENT
--
create table department (
  departmentid int(10)  primary key auto_increment,
  name varchar(250) ,

  isinternal int(1)  default 1,
  isactive int(1)  default 1,
  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  constraint unique index u_department_name (name)
) ENGINE=InnoDB charset=utf8;

insert into department values 
( 1, 'Internal - Default Department', 1, 1, now(), 1 ),
( 2, 'External - Default Department', 0,  1, now(), 1 );

-- LAB.USER
--   - presumably 'Department Admin'
-- 
create table departmentuser ( 
  departmentuserid int(10)  primary key auto_increment, 

  departmentid int(10) , 
  userid int(10) , 

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_departmentuser_uid (userid) references user(userid),
  foreign key fk_departmentuser_did (departmentid) references department(departmentid),

  constraint unique index u_departmentuser_did_uid (departmentid, userid)
) ENGINE=InnoDB charset=utf8;



--
-- LAB 
--
create table lab ( 
  labid int(10)  primary key auto_increment,

  departmentid int(10) , 
  name varchar(250) ,

  primaryuserid int(10) , -- primary investigator?

  isactive int(1)  default 1,
  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_lab_did (departmentid) references department(departmentid),
  foreign key fk_lab_puid (primaryuserid) references user(userid)
--  There is no reason why lab name needs to be unique or why a user cannot have more than one lab
--  constraint unique index u_lab_name (name),
--  constraint unique index u_lab_puid (primaryuserid)
) ENGINE=InnoDB charset=utf8;

-- insert into lab values 
-- ( 1, 1, 'Default Lab',  1, 1, now(), 1 );

create table labmeta (
  labmetaid int(10)  primary key auto_increment,
  labid int(10) ,

  k varchar(250) , 
  v TEXT, 
  position int(10)  default 0,
  rolevisibility VARCHAR(250),

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_labmeta_labid (labid) references lab(labid),
  constraint unique index u_labmeta_k_lid (k, labid)
) ENGINE=InnoDB charset=utf8;

--
-- LAB.USER
--   - presumably 'Lab Member', 'Lab Manager' or 'Primary Investigator'
-- 
create table labuser ( 
  labuserid int(10)  primary key auto_increment, 

  labid int(10) , 
  userid int(10) , 

  roleid int(10) , 
   
  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_labuser_lid (labid) references lab(labid),
  foreign key fk_labuser_uid (userid) references user(userid),
  foreign key fk_labuser_rid (roleid) references role(roleid),

  constraint unique index u_labuser_lid_uid (labid, userid)
) ENGINE=InnoDB charset=utf8;

--
-- pending user
-- 
create table userpending (
  userpendingid int(10)  primary key auto_increment,

  email varchar(250) , 
  password varchar(250) , 
  login varchar(250) , 
  firstname varchar(250) , 
  lastname varchar(250) , 
  locale varchar(5)  default 'en_US',
  labid int(10),

  status varchar(10)  default 'PENDING', -- PENDING, APPROVED, DECLINED

  lastupdts timestamp  default current_timestamp, 
  lastupduser int(10)  default 0,

  foreign key fk_userpending_lid (labid) references lab(labid),

  index i_userpending_status(status, email) 
) ENGINE=InnoDB charset=utf8;

create table userpendingmeta (
  userpendingmetaid int(10)  primary key auto_increment,
  userpendingid int(10) ,

  k varchar(250) , 
  v TEXT, 
  position int(10)  default 0,
  rolevisibility VARCHAR(250),

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_userpendingmeta_userpendingid (userpendingid) references userpending(userpendingid),
  constraint unique index u_userpendingmeta_k_lid (k, userpendingid)
) ENGINE=InnoDB charset=utf8;

create table labpending ( 
  labpendingid int(10)  primary key auto_increment,

  departmentid int(10) , 
  name varchar(250) ,

  primaryuserid int(10),
  userpendingid int(10),

  status varchar(10)  default 'PENDING', -- PENDING, APPROVED, DECLINED

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_labpending_did (departmentid) references department(departmentid),
  foreign key fk_labpending_pruid (primaryuserid) references user(userid),
  foreign key fk_labpending_peuid (userpendingid) references userpending(userpendingid),
  index (status, name)
) ENGINE=InnoDB charset=utf8;

create table labpendingmeta (
  labpendingmetaid int(10)  primary key auto_increment,
  labpendingid int(10) ,

  k varchar(250) , 
  v TEXT, 
  position int(10)  default 0,
  rolevisibility VARCHAR(250),

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_labpendingmeta_labpendingid (labpendingid) references labpending(labpendingid),
  constraint unique index u_labpendingmeta_k_lid (k, labpendingid)
) ENGINE=InnoDB charset=utf8;


--
-- confirm email
--
create table confirmemailauth (
  confirmemailauthid  int(10)  primary key auto_increment,
  userpendingid int(10),
  userid int(10),
  authcode varchar(250) ,
  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default '0',
  foreign key fk_labpending_uid (userid) references user(userid),
  foreign key fk_labpending_peuid (userpendingid) references userpending(userpendingid),
  constraint unique index u_confirmemailauth (authcode)
) ENGINE=InnoDB charset=utf8;


--
-- type.RESOURCE
-- 
create table resourcetype (
  resourcetypeid int(10)  primary key auto_increment, 

  iname varchar(250) ,
  name varchar(250) ,

  constraint unique index u_resourcetype_iname (iname),
  constraint unique index u_resourcetype_name (name)
) ENGINE=InnoDB charset=utf8; 

insert into resourcetype values (1, 'mps', 'Massively Parallel DNA Sequencer'); 
insert into resourcetype values (2, 'amplicon', 'DNA Amplicon'); 
insert into resourcetype values (3, 'aligner', 'Aligner'); 
insert into resourcetype values (4, 'peakcaller', 'Peak Caller'); 
insert into resourcetype values (5, 'sanger', 'Sanger DNA Sequencer'); 
insert into resourcetype values (6, 'helptagPipeline', 'HELP-tag Pipeline'); 
insert into resourcetype values (7, 'bisulseqPipeline', 'Bi-sulphite-seq Pipeline'); 
insert into resourcetype values (8, 'sequenceRunProcessor', 'Sequence Run Processor'); 

--
-- RESOURCE
-- 

create table resourcecategory (
  resourcecategoryid int(10)  primary key auto_increment, 
  resourcetypeid int(10) not null,
  iname varchar(250) ,
  name varchar(250) ,
  isactive int(1)  default 1,
  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_resourccategory_resourcetypeid (resourcetypeid) references resourcetype(resourcetypeid),

  constraint unique index u_resourcecategory_i(iname),
  constraint unique index u_resourcecategory_n(name)
) ENGINE=InnoDB charset=utf8;

create table resourcecategorymeta (
  resourcecategorymetaid int(10)  primary key auto_increment,
  resourcecategoryid int(10) ,

  k varchar(250) , 
  v TEXT, 
  position int(10)  default 0,
  rolevisibility VARCHAR(250),

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_resourccategoryemeta_resourcecategoryid (resourcecategoryid) references resourcecategory(resourcecategoryid),
  constraint unique index u_resourcecategorymeta_k_rid (k, resourcecategoryid)
) ENGINE=InnoDB charset=utf8;


create table resource (
  resourceid int(10)  primary key auto_increment, 
  resourcecategoryid int(10) , 
  resourcetypeid int(10) ,
  iname varchar(250) ,
  name varchar(250) ,
  isactive int(1)  default 1,
  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,
  foreign key fk_resource_trid (resourcetypeid) references resourcetype(resourcetypeid),
  foreign key fk_resource_rid (resourcecategoryid) references resourcecategory(resourcecategoryid),
  constraint unique index u_resource_i(iname),
  constraint unique index u_resource_n(name)
) ENGINE=InnoDB charset=utf8;


create table software (
  softwareid int(10)  primary key auto_increment, 
  resourcetypeid int(10) not null,
  iname varchar(250) ,
  name varchar(250) ,
  isactive int(1)  default 1,
  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_software_resourcetypeid (resourcetypeid) references resourcetype(resourcetypeid),

  constraint unique index u_software_i(iname)
) ENGINE=InnoDB charset=utf8;


create table softwaremeta (
  softwaremetaid int(10)  primary key auto_increment,
  softwareid int(10) ,

  k varchar(250), 
  v TEXT, 
  position int(10)  default 0,
  rolevisibility VARCHAR(250),

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_softwaremeta_sid (softwareid) references software(softwareid),
  constraint unique index u_softwaremeta_k_rid (k, softwareid)
) ENGINE=InnoDB charset=utf8;


create table resourcemeta (
  resourcemetaid int(10)  primary key auto_increment,
  resourceid int(10) ,

  k varchar(250) , 
  v TEXT, 
  position int(10)  default 0,
  rolevisibility VARCHAR(250),

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_resourcemeta_resourceid (resourceid) references resource(resourceid),
  constraint unique index u_resourcemeta_k_rid (k, resourceid)
) ENGINE=InnoDB charset=utf8;


-- 
-- RESOURCE.USER 
--   - presumably 'Facilities Tech', maybe w/ roleid
  -- create table resourceuser ( 
  -- resourceuserid int(10)  primary key auto_increment, 
  -- 
  -- resourceid int(10) , 
  -- userid int(10) , 
  -- 
  -- lastupdts timestamp  default current_timestamp,
  -- lastupduser int(10)  default 0,
  -- 
  -- foreign key fk_resourceuser_lid (resourceid) references resource(resourceid),
  -- foreign key fk_resourceuser_uid (userid) references user(userid),
  -- 
  -- constraint unique index u_resourceuser_rid_uid (resourceid, userid)
  -- ) ENGINE=InnoDB charset=utf8;



--


create table workflow (
  workflowid int(10)  primary key auto_increment, 

  iname varchar(250) , 
  name varchar(250) , 
  createts datetime ,

  isactive int(1)  default 0,
  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  constraint unique index u_workflow_iname (iname),
  constraint unique index u_workflow_name (name)
) ENGINE=InnoDB charset=utf8;

create table workflowmeta (
  workflowmetaid int(10)  primary key auto_increment,
  workflowid int(10) ,

  k varchar(250) , 
  v text,
  position int(10)  default 0,
  rolevisibility VARCHAR(250),

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_workflowmeta_workflowid (workflowid) references workflow(workflowid),
  constraint unique index u_workflowmeta_k_wid (k, workflowid)
) ENGINE=InnoDB charset=utf8;

--
-- JOB
--
create table job (
  jobid int(10)  primary key auto_increment, 

  labid int(10) ,
  userid int(10) ,  -- investigator
  workflowid int(10) ,  

  name varchar(250) , 
   
  createts datetime ,

  viewablebylab int(1)  default 0,

  isactive int(1)  default 1,
  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_job_lid (labid) references lab(labid),
  foreign key fk_job_uid (userid) references user(userid),
  foreign key fk_job_wid (workflowid) references workflow(workflowid),

  constraint unique index u_job_name_lid (name, labid)
) ENGINE=InnoDB charset=utf8;


create table jobmeta (
  jobmetaid int(10)  primary key auto_increment,
  jobid int(10) ,

  k varchar(250) , 
  v text,
  position int(10)  default 0,
  rolevisibility VARCHAR(250),

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_jobmeta_jobid (jobid) references job(jobid),

  constraint unique index u_jobmeta_k_jid (k, jobid)
) ENGINE=InnoDB charset=utf8;

--
-- JOB.USER
--   - presumably 'Job Submitter', 'Job Viewer'
-- 

create table jobuser ( 
  jobuserid int(10)  primary key auto_increment, 

  jobid int(10) , 
  userid int(10) , 

  roleid int(10) , 
   
  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_jobuser_jid (jobid) references job(jobid),
  foreign key fk_jobuser_uid (userid) references user(userid),
  foreign key fk_jobuser_rid (roleid) references role(roleid),

  constraint unique index u_jobuser_jid_uid (jobid, userid)
) ENGINE=InnoDB charset=utf8;


create table jobresourcecategory (
  jobresourcecategoryid int(10)  primary key auto_increment,
  jobid int(10) ,
  resourcecategoryid int(10) ,

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_jobresourcecategory_jid (jobid) references job(jobid),
  foreign key fk_jobresourcecategory_rcid (resourcecategoryid) references resourcecategory(resourcecategoryid),
  constraint unique index u_jobresourcecategory_rcid_jid (resourcecategoryid, jobid)
) ENGINE=InnoDB charset=utf8;

create table jobsoftware (
  jobsoftwareid int(10)  primary key auto_increment,
  jobid int(10) ,
  softwareid int(10) ,

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_jobsoftware_jdid (jobid) references job(jobid),
  foreign key fk_jobsoftware_sid (softwareid) references software(softwareid),
  constraint unique index u_jobsoftware_rid_jdid (softwareid, jobid)
) ENGINE=InnoDB charset=utf8;

--
-- job draft
--

create table jobdraft (
  jobdraftid int(10)  primary key auto_increment, 

  labid int(10) ,
  userid int(10) ,  -- investigator
  workflowid int(10) ,  

  name varchar(250) , 
   
  createts datetime ,
  submittedjobid int(10),

  status varchar(50)  default 1,
  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_jobdraft_lid (labid) references lab(labid),
  foreign key fk_jobdraft_uid (userid) references user(userid),
  foreign key fk_jobdraft_wid (workflowid) references workflow(workflowid),
  foreign key fk_jobdraft_sjid (submittedjobid) references job(jobid)

) ENGINE=InnoDB charset=utf8;


create table jobdraftmeta (
  jobdraftmetaid int(10)  primary key auto_increment,
  jobdraftid int(10) ,

  k varchar(250) , 
  v TEXT, 
  position int(10)  default 0,
  rolevisibility VARCHAR(250),

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_jobdraftmeta_jdid (jobdraftid) references jobdraft(jobdraftid),

  constraint unique index u_jobdraftmeta_k_jdid (k, jobdraftid)
) ENGINE=InnoDB charset=utf8;

create table jobdraftresourcecategory (
  jobdraftresourcecategoryid int(10)  primary key auto_increment,
  jobdraftid int(10) ,
  resourcecategoryid int(10) ,

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_jobdraftresourcecategory_jdid (jobdraftid) references jobdraft(jobdraftid),
  foreign key fk_jobdraftresourcecategory_rcid (resourcecategoryid) references resourcecategory(resourcecategoryid),
  constraint unique index u_jobdraftresourcecategory_rcid_jdid (resourcecategoryid, jobdraftid)
) ENGINE=InnoDB charset=utf8;

create table jobdraftsoftware (
  jobdraftsoftwareid int(10)  primary key auto_increment,
  jobdraftid int(10) ,
  softwareid int(10) ,

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_jobdraftsoftware_jdid (jobdraftid) references jobdraft(jobdraftid),
  foreign key fk_jobdraftsoftware_sid (softwareid) references software(softwareid),
  constraint unique index u_jobdraftsoftware_sid_jdid (softwareid, jobdraftid)
) ENGINE=InnoDB charset=utf8;



-- ---------------------------------------------------

--
-- PROJECT
--  user job folders?

create table project (
  projectid int(10)  primary key auto_increment, 
  labid int(10) ,
  userid int(10) ,
 
  name varchar(250) ,

  isactive int(1)  default 1,
  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_project_lid (labid) references lab(labid),

  constraint unique index u_project_name_lid (name, labid)
) ENGINE=InnoDB charset=utf8;


--
-- BARCODE
--   - physical object tracker
--
create table barcode ( 
  barcodeid int(10)  primary key auto_increment, 
  barcode varchar(250) , 

  barcodefor varchar(250), -- enum sample/resource 

  isactive int(1)  default 1,
  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0

) ENGINE=InnoDB charset=utf8;

create table resourcebarcode (
  resourcebarcodeid int(10)  primary key auto_increment, 

  resourceid int(10) , 
  barcodeid int(10) , 

  foreign key fk_resourcebarcode_rid (resourceid) references resource(resourceid),
  foreign key fk_resourcebarcode_bcid (barcodeid) references barcode(barcodeid),

  constraint unique index u_resourcebarcode_rid (resourceid),
  constraint unique index u_resourcebarcode_bcid (barcodeid)
) ENGINE=InnoDB charset=utf8;

-- FILE
--   mysql max out at 767 bytes for indexable length
--
create table file (
  fileid int(10)  primary key auto_increment,
  file_uri varchar(2048) , 
  contenttype varchar(250) , 
  sizek int(10) ,
  md5hash varchar(250) ,
  description varchar(250),

  isarchived int(1)  default 0,
  isactive int(1)  default 1,

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0 

  -- constraint unique index u_file_flocation (filelocation)
) ENGINE=InnoDB charset=utf8;

create table filemeta (
  filemetaid int(10)  primary key auto_increment,
  fileid int(10) ,

  k varchar(250) , 
  v text,
  position int(10)  default 0,
  rolevisibility VARCHAR(250),

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_filemeta_fileid (fileid) references file(fileid),

  constraint unique index u_filemeta_k_jid (k, fileid)
) ENGINE=InnoDB charset=utf8;

-- 
-- SAMPLE
--

create table sampletypecategory (
  sampletypecategoryid int(10)  primary key auto_increment,
  iname varchar(250), 
  name varchar(250),

  constraint unique index u_sampletypecategory_iname (iname)
) ENGINE=InnoDB charset=utf8;

insert into sampletypecategory values
(1, 'biomaterial', 'Biomaterial'),
(2, 'hardware', 'Hardware');


create table sampletype (
  sampletypeid int(10)  primary key auto_increment,
  sampletypecategoryid int(10) ,
  iname varchar(250), 
  name varchar(250),
  foreign key fk_sampletype_tscid (sampletypecategoryid) references sampletypecategory(sampletypecategoryid),
  constraint unique index u_sampletype_iname (iname)
) ENGINE=InnoDB charset=utf8;

insert into sampletype values
(1, 1, 'dna', 'DNA'), 
(2, 1, 'rna', 'RNA'), 
(3, 1, 'library', 'Library'),
(4, 2, 'cell', 'Cell'), 
(5, 2, 'platformunit', 'Platform Unit'),
(6, 1, 'tissue', 'Tissue'),
(7, 1, 'protein', 'Protein'),
(8, 1, 'cellPrimary', 'Primary Cell'),
(9, 1, 'cellLine', 'Cell Line'),
(10, 1, 'facilityLibrary', 'Facility Library');



create table samplesubtype (
  samplesubtypeid int(10)  primary key auto_increment,

  sampletypeid int(10) ,

  iname varchar(50) , -- meta field prefix
  name varchar(250) ,
  isactive int(1)  default 1,
  arealist varchar(250),

  constraint unique index u_samplesubtype_iname (iname),
  foreign key fk_samplesubtype_tsid (sampletypeid) references sampletype(sampletypeid)
) ENGINE=InnoDB charset=utf8;

create table samplesubtypemeta (
  samplesubtypemetaid int(10)  primary key auto_increment,
  samplesubtypeid int(10) ,

  k varchar(250) , 
  v TEXT, 
  position int(10)  default 0,
  rolevisibility VARCHAR(250),

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_samplesubtypemeta_sampleid (samplesubtypeid) references samplesubtype(samplesubtypeid),
  constraint unique index u_samplesubtypemeta_k_sid (k, samplesubtypeid)
) ENGINE=InnoDB charset=utf8;

create table samplesubtyperesourcecategory (
  samplesubtyperesourcecategoryid int(10)  primary key auto_increment,
  samplesubtypeid int(10) ,
  resourcecategoryid int(10) ,
  foreign key fk_samplesubtyperesourcecategory_stscid (samplesubtypeid) references samplesubtype(samplesubtypeid),
  foreign key fk_samplesubtyperesourcecategory_rcid (resourcecategoryid) references resourcecategory(resourcecategoryid)
) ENGINE=InnoDB charset=utf8;

create table jobdraftfile ( 
  jobdraftfileid int(10)  primary key auto_increment,
  jobdraftid int(10) , 
  fileid int(10) , 

  iname varchar(2048), -- 
  name varchar(250), 
  description varchar(2048), 

  isactive int(1)  default 1, 
  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_jobdraftfile_jid (jobdraftid) references jobdraft(jobdraftid),
  foreign key fk_jobdraftfile_fid (fileid) references file(fileid) -- ,

  -- constraint unique index u_jobdraftfile_iname_jid (iname, jobdraftid)
) ENGINE=InnoDB charset=utf8;

create table workflowsamplesubtype (
  workflowsamplesubtypeid int(10)  primary key auto_increment,
  workflowid int(10) ,
  samplesubtypeid int(10) ,
 
  constraint unique index u_samplesubtype_wid_stsid (workflowid, samplesubtypeid),

  foreign key fk_workflowsamplesubtype_stsid (samplesubtypeid) references samplesubtype(samplesubtypeid),
  foreign key fk_workflowsamplesubtype_wid (workflowid) references workflow(workflowid)
) ENGINE=InnoDB charset=utf8;



create table workflowresourcetype (
  workflowresourcetypeid int(10)  primary key auto_increment, 
  workflowid int(10) ,
  resourcetypeid int(10) ,

  constraint unique index u_workflowresourcetype_wid_trid (workflowid, resourcetypeid),

  foreign key fk_workflowresourcetype_trid (resourcetypeid) references resourcetype(resourcetypeid),
  foreign key fk_workflowresourcetype_wid (workflowid) references workflow(workflowid)
) ENGINE=InnoDB charset=utf8;

create table workflowresourcecategory (
  workflowresourcecategoryid int(10)  primary key auto_increment, 
  workflowid int(10) ,
  resourcecategoryid int(10) ,

  constraint unique index u_workflowresource_wid_rcid (workflowid, resourcecategoryid),

  foreign key fk_workflowresourcecategory_rcid (resourcecategoryid) references resourcecategory(resourcecategoryid),
  foreign key fk_workflowresourcecategory_wid (workflowid) references workflow(workflowid)
) ENGINE=InnoDB charset=utf8;

create table workflowresourcecategorymeta (
  workflowresourcecategorymetaid int(10)  primary key auto_increment, 
  workflowresourcecategoryid int(10) ,
  k varchar(250) , 
  v text,
  position int(10)  default 0,
  rolevisibility VARCHAR(250),

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_wrometa_workflowresourcecategoryid (workflowresourcecategoryid) references workflowresourcecategory(workflowresourcecategoryid),

  constraint unique index u_wro_wrcid_k (workflowresourcecategoryid, k)
) ENGINE=InnoDB charset=utf8;

create table workflowsoftware (
  workflowsoftwareid int(10)  primary key auto_increment, 
  workflowid int(10) ,
  softwareid int(10) ,

  constraint unique index u_workflowsoftware_wid_sid (workflowid, softwareid),

  foreign key fk_workflowsoftware_sid (softwareid) references software(softwareid),
  foreign key fk_workflowsoftware_wid (workflowid) references workflow(workflowid)
) ENGINE=InnoDB charset=utf8;

create table workflowsoftwaremeta (
  workflowsoftwaremetaid int(10)  primary key auto_increment, 
  workflowsoftwareid int(10) ,
  k varchar(250) , 
  v text,
  position int(10)  default 0,
  rolevisibility VARCHAR(250),

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_wrometa_workflowsoftwareid (workflowsoftwareid) references workflowsoftware(workflowsoftwareid),

  constraint unique index u_wro_wrcid_k (workflowsoftwareid, k)
) ENGINE=InnoDB charset=utf8;



create table sample (
  sampleid int(10)  primary key auto_increment,
  parentid int(10),
  sampletypeid int(10) ,
  samplesubtypeid int(10),

  submitter_labid int(10) ,
  submitter_userid int(10) ,
  submitter_jobid int(10) null,

  isreceived int(1)  default 0,
  receiver_userid int(10),
  receivedts datetime,

  name varchar(250),
  isgood int(1),

  isactive int(1)  default 1,
  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_sample_sampleid (parentid) references sample(sampleid),
  foreign key fk_sample_tsid (sampletypeid) references sampletype(sampletypeid),
  foreign key fk_sample_stsid (samplesubtypeid) references samplesubtype(samplesubtypeid),
  foreign key fk_sample_sjid (submitter_jobid) references job(jobid),
  foreign key fk_sample_slid (submitter_labid) references lab(labid),
  foreign key fk_sample_suid (submitter_userid) references user(userid)
) ENGINE=InnoDB charset=utf8;

create table samplemeta (
  samplemetaid int(10)  primary key auto_increment,
  sampleid int(10) ,

  k varchar(250) , 
  v TEXT, 
  position int(10)  default 0,
  rolevisibility VARCHAR(250),

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_samplemeta_sampleid (sampleid) references sample(sampleid),
  constraint unique index u_samplemeta_k_sid (k, sampleid)
) ENGINE=InnoDB charset=utf8;


-- SAMPLE.SOURCE
--   - if a sample has a parent, what is it. 
create table samplesource (
  samplesourceid int(10)  primary key auto_increment, 
  sampleid int(10) ,
  indexvalue int(10)  default 0,
  source_sampleid int(10) ,

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_samplesource_sid (sampleid) references sample(sampleid),
  foreign key fk_samplesource_ssid (source_sampleid) references sample(sampleid),

  constraint unique index u_samplesource_sid (sampleid, indexvalue)
) ENGINE=InnoDB charset=utf8;

create table samplesourcemeta (
  samplesourcemetaid int(10)  primary key auto_increment,
  samplesourceid int(10) ,

  k varchar(250) , 
  v TEXT, 
  position int(10)  default 0,
  rolevisibility VARCHAR(250),

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_samplesourcemeta_sampleid (samplesourceid) references samplesource(samplesourceid),
  constraint unique index u_samplesourcemeta_k_sid (k, samplesourceid)
) ENGINE=InnoDB charset=utf8;

-- SAMPLE.BARCODE 
create table samplebarcode (
  samplebarcode int(10)  primary key auto_increment, 
 
  sampleid int(10) , 
  barcodeid int(10) , 
 
  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_samplebarcode_sid (sampleid) references sample(sampleid),
  foreign key fk_samplebarcode_bid (barcodeid) references barcode(barcodeid),

  constraint unique index u_samplebarcode_sid (sampleid),
  constraint unique index u_samplebarcode_bcid (barcodeid)
) ENGINE=InnoDB charset=utf8;

-- SAMPLE.LAB
--   - lab share?
create table samplelab (
  samplelabid int(10)  primary key auto_increment, 
  
  sampleid int(10) ,  
  labid int(10) ,

  isprimary int(1)  default 0, 

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_samplelab_sid (sampleid) references sample(sampleid),
  foreign key fk_samplelab_lid (labid) references lab(labid),

  constraint unique index u_samplelab_sid_lid (sampleid, labid)
) ENGINE=InnoDB charset=utf8;


create table sampledraft (
  sampledraftid int(10)  primary key auto_increment,
  sampletypeid int(10) ,
  samplesubtypeid int(10) ,

  labid int(10) ,
  userid int(10) ,
  jobdraftid int(10) null,

  sourcesampleid int(10) null,
  fileid int(10) null,

  name varchar(250),
  status varchar(50), 

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_sampledraft_tsid (sampletypeid) references sampletype(sampletypeid),
  foreign key fk_sampledraft_stsid (samplesubtypeid) references samplesubtype(samplesubtypeid),
  foreign key fk_sampledraft_sjid (jobdraftid) references jobdraft(jobdraftid),
  foreign key fk_sampledraft_slid (labid) references lab(labid),
  foreign key fk_sampledraft_suid (userid) references user(userid),
  foreign key fk_sampledraft_fid (fileid) references file(fileid)
) ENGINE=InnoDB charset=utf8;

create table sampledraftmeta (
  sampledraftmetaid int(10)  primary key auto_increment,
  sampledraftid int(10) ,

  k varchar(250) , 
  v TEXT, 
  position int(10)  default 0,
  rolevisibility VARCHAR(250),

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_sampledraftmeta_sdid (sampledraftid) references sampledraft(sampledraftid),
  constraint unique index u_sampledraftmeta_k_sid (k, sampledraftid)
) ENGINE=InnoDB charset=utf8;

create table jobdraftcellselection (
  jobdraftcellselectionid int(10)  primary key auto_increment,
  jobdraftid int(10) ,
  cellindex int(10) ,

  foreign key fk_jobdraft_jid (jobdraftid) references jobdraft(jobdraftid),
  constraint unique index u_jobdraftcell_jdid_ci (jobdraftid, cellindex)
);

create table sampledraftjobdraftcellselection (
  sampledraftjobdraftcellselectionid int(10)  primary key auto_increment,
  sampledraftid int(10) ,
  jobdraftcellselectionid int(10) ,

  libraryindex int(10) , 

  foreign key fk_sampledraftcell_sdid (sampledraftid) references sampledraft(sampledraftid),
  foreign key fk_sampledraftcell_jdcid (jobdraftcellselectionid) references jobdraftcellselection(jobdraftcellselectionid),
  constraint unique index u_sampledraftcell_jdcid_li (jobdraftcellselectionid, libraryindex)
);


--
-- back to the job
--


-- 
-- JOB.SAMPLE
--   - maps samples used and created to jobs
create table jobsample ( 
  jobsampleid int(10)  primary key auto_increment, 
  jobid int(10) , 
  sampleid int(10) ,

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_jobsample_jid (jobid) references job(jobid),
  foreign key fk_jobsample_sid (sampleid) references sample(sampleid),

  constraint unique index u_jobsample_jid_sid (jobid, sampleid)
) ENGINE=InnoDB charset=utf8;

create table jobsamplemeta (
  jobsamplemetaid int(10)  primary key auto_increment,
  jobsampleid int(10) ,

  k varchar(250) , 
  v TEXT, 
  position int(10)  default 0,
  rolevisibility VARCHAR(250),

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_jobsamplemeta_jsid (jobsampleid) references jobsample(jobsampleid),
  constraint unique index u_jobsamplemeta_k_jsid (k, jobsampleid)
) ENGINE=InnoDB charset=utf8;


create table jobcellselection (
  jobcellselectionid int(10)  primary key auto_increment,
  jobid int(10) ,
  cellindex int(10) ,

  foreign key fk_job_jid (jobid) references job(jobid),
  constraint unique index u_jobcell_jdid_ci (jobid, cellindex)
);

create table samplejobcellselection (
  samplejobcellselectionid int(10)  primary key auto_increment,
  sampleid int(10) ,
  jobcellselectionid int(10) ,

  libraryindex int(10) , 

  foreign key fk_samplecell_sdid (sampleid) references sample(sampleid),
  foreign key fk_samplecell_jdcid (jobcellselectionid) references jobcellselection(jobcellselectionid),
  constraint unique index u_samplecell_jdcid_li (jobcellselectionid, libraryindex)
);


--
-- ACCOUNTING TABLES
--
create table acct_workflowcost (
  workflowid int(10)  primary key,
  basecost float(10,2) ,

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_acct_workflowcost_wfid (workflowid) references job(workflowid)
) ENGINE=InnoDB charset=utf8;


create table acct_quote (
  quoteid int(10)  primary key auto_increment,
  jobid int(10) ,
  amount float(10,2) ,
  userid int(10), -- quoter
  comment varchar(250),

  isactive int(1)  default 1,
  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_acct_quote_jid (jobid) references job(jobid),
  foreign key fk_acct_quote_uid (userid) references user(userid)
) ENGINE=InnoDB charset=utf8;


create table acct_quotemeta (
  quotemetaid int(10)  primary key auto_increment,
  quoteid int(10) ,

  k varchar(250) , 
  v TEXT, 
  position int(10)  default 0,
  rolevisibility VARCHAR(250),

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_quotemeta_qid (quoteid) references acct_quote(quoteid),
  constraint unique index u_quotemeta_k_qid (k, quoteid)
) ENGINE=InnoDB charset=utf8;


create table acct_jobquotecurrent (
  currentid int(10)  primary key auto_increment, 
  jobid int(10) ,
  quoteid int(10) ,

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,


  foreign key fk_acct_jobquotecurrent_jid (jobid) references job(jobid),
  constraint unique index u_acct_jobquotecurrent_jid (jobid),
  foreign key fk_acct_jobquotecurrent_qid (quoteid) references acct_quote(quoteid),
  constraint unique index u_acct_jobquotecurrent_qid (quoteid)
) ENGINE=InnoDB charset=utf8;


create table acct_quoteuser (
  quoteuserid int(10)  primary key auto_increment, 
  quoteid int(10) ,
  userid int(10) ,
  roleid int(10) , -- PI, DA, FM,  
  isapproved int(1) ,
  comment varchar(250),

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,


  foreign key fk_acct_quote_jid (quoteid) references acct_quote(quoteid),
  foreign key fk_acct_quote_uid (userid) references user(userid),
  foreign key fk_acct_quote_rid (roleid) references role(roleid)
) ENGINE=InnoDB charset=utf8;


create table acct_invoice (
  invoiceid int(10)  primary key auto_increment,
  quoteid int(10) ,
  jobid int(10) , -- DENORMALIZED
  amount float(10,2) ,
  comment varchar(250),

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,


  foreign key fk_acct_invoice_qid (quoteid) references acct_quote(quoteid),
  foreign key fk_acct_invoice_jid (jobid) references job(jobid)
) ENGINE=InnoDB charset=utf8;

create table acct_ledger (
  ledgerid int(10)  primary key auto_increment,
  invoiceid int(10) ,
  jobid int(10) , -- DENORMALIZED
  amount float(10,2),
  comment varchar(250),

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,


  foreign key fk_acct_ledger_iid (invoiceid) references acct_invoice(invoiceid),
  foreign key fk_acct_ledger_jid (jobid) references job(jobid)
) ENGINE=InnoDB charset=utf8;

create table acct_grant (
  grantid int(10)  primary key auto_increment,
  labid int(10) ,
  name varchar(250) ,
  code varchar(250) ,
  expirationdt datetime,

  isactive int(1)  default 1,
  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,


  foreign key fk_grant_lid (labid) references lab(labid)
) ENGINE=InnoDB charset=utf8;

create table acct_grantjob (
  jobid int(10)  primary key auto_increment, 
  grantid int(10) ,

  isactive int(1)  default 1,
  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_acct_ledgergrant_jid (jobid) references acct_ledger(jobid),
  foreign key fk_acct_ledgergrant_gid (grantid) references acct_grant(grantid)
) ENGINE=InnoDB charset=utf8;


--
--
-- JOB.FILE
--
create table jobfile ( 
  jobfileid int(10)  primary key auto_increment,
  jobid int(10) , 
  fileid int(10) , 

  iname varchar(2048), -- 
  name varchar(250), 
  description varchar(2048), 

  isactive int(1)  default 1, 
  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_jobfile_jid (jobid) references job(jobid),
  foreign key fk_jobfile_fid (fileid) references file(fileid) -- ,

  -- constraint unique index u_jobfile_iname_jid (iname, jobid)
) ENGINE=InnoDB charset=utf8;



create table samplefile ( 
  samplefileid int(10)  primary key auto_increment,
  sampleid int(10) , 
  fileid int(10) , 

  iname varchar(2048), -- 
  name varchar(250), 
  description varchar(2048), 

  isactive int(1)  default 1, 
  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_samplefile_sid (sampleid) references sample(sampleid),
  foreign key fk_samplefile_fid (fileid) references file(fileid) -- ,

  -- constraint unique index u_samplefile_iname_jid (iname, sampleid)
) ENGINE=InnoDB charset=utf8;


create table samplesourcefile ( 
  samplesourcefileid int(10)  primary key auto_increment,
  samplesourceid int(10) , 
  fileid int(10) , 

  iname varchar(2048), -- 
  name varchar(250), 
  description varchar(2048), 

  isactive int(1)  default 1, 
  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_samplesourcefile_sid (samplesourceid) references samplesource(samplesourceid),
  foreign key fk_samplesourcefile_fid (fileid) references file(fileid) -- ,

  -- constraint unique index u_samplefile_iname_jid (iname, sampleid)
) ENGINE=InnoDB charset=utf8;


-- adaptors 

create table adaptorset(
	adaptorsetid int(10) primary key auto_increment,
	iname varchar(250),
	name varchar(250),
	sampletypeid int(10),
	isactive int(1)  default 1, 

  	foreign key fk_adaptorset_tid (sampletypeid) references sampletype(sampletypeid),
  	constraint unique index u_adaptorset_k_iid(iname),
	constraint unique index u_adaptorset_k_nid(name)

) ENGINE=InnoDB charset=utf8;

create table adaptorsetmeta (
  adaptorsetmetaid int(10)  primary key auto_increment,
  adaptorsetid int(10) ,

  k varchar(250) , 
  v TEXT, 
  position int(10)  default 0,
  rolevisibility VARCHAR(250),

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_adaptorsetmeta_runid (adaptorsetid) references adaptorset(adaptorsetid),
  constraint unique index u_adaptorsetmeta_k_aid (k, adaptorsetid)
) ENGINE=InnoDB charset=utf8;

create table adaptorsetresourcecategory (
  adaptorsetresourcecategoryid int(10)  primary key auto_increment, 
  adaptorsetid int(10) ,
  resourcecategoryid int(10) ,

  foreign key fk_adaptorsetresourcecategory_rid (resourcecategoryid) references resourcecategory(resourcecategoryid),
  foreign key fk_adaptorsetresourcecategory_aid (adaptorsetid) references adaptorset(adaptorsetid),
  constraint unique index u_adaptorsetresourcecategory_aid_rid (adaptorsetid, resourcecategoryid)
) ENGINE=InnoDB charset=utf8;

create table adaptor(
	adaptorid int(10) primary key auto_increment,
	adaptorsetid int(10),
	iname varchar(250),
	name varchar(250),
	sequence varchar(250),
	barcodesequence varchar(250),
	barcodenumber int(10),
	isactive int(1)  default 1, 

  	foreign key fk_adaptor_aid (adaptorsetid) references adaptorset(adaptorsetid),
  	constraint unique index u_adaptor_k_iid(iname),
	constraint unique index u_adaptor_k_nid(name)
) ENGINE=InnoDB charset=utf8;

create table adaptormeta (
  adaptormetaid int(10)  primary key auto_increment,
  adaptorid int(10) ,

  k varchar(250) , 
  v TEXT, 
  position int(10)  default 0,
  rolevisibility VARCHAR(250),

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_adaptormeta_runid (adaptorid) references adaptor(adaptorid),
  constraint unique index u_adaptormeta_k_aid (k, adaptorid)
) ENGINE=InnoDB charset=utf8;

-- ---------------------------------

-- resource stuffs

--
-- RESOURCELANE LANE
--

create table resourcecell (
  resourcecellid int(10)  primary key auto_increment,
  resourceid int(10) ,
  iname varchar(250),
  name varchar(250),
  isactive int(1) default 1, -- need this as we cannot delete a resourcecell if it is referenced 

  foreign key fk_resourcecell_rid (resourceid) references resource(resourceid),
  constraint unique index u_resourcecell_iname_rid (iname, resourceid),
  constraint unique index u_resourcecell_name_rid (name, resourceid)
) ENGINE=InnoDB charset=utf8;

--
-- RUN
--

create table run (
  runid int(10)  primary key auto_increment,

  resourceid int(10) ,
  resourcecategoryid int(10) ,
  softwareid  int(10) ,
  userid int(10) , -- facilities tech

  name varchar(250) , 
  sampleid int(10) , -- flowcell

  startts datetime, 
  endts datetime, 

  status varchar(250), -- ????

  isactive int(1)  default 1, 
  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_run_rid (resourceid) references resource(resourceid),
  foreign key fk_run_rcid (resourcecategoryid) references resourcecategory(resourcecategoryid),
  foreign key fk_run_swid (softwareid) references software(softwareid),
  foreign key fk_run_sid (sampleid) references sample(sampleid),
  foreign key fk_run_userid (userid) references user(userid)
) ENGINE=InnoDB charset=utf8;

create table runmeta (
  runmetaid int(10)  primary key auto_increment,
  runid int(10) ,

  k varchar(250) , 
  v TEXT, 
  position int(10)  default 0,
  rolevisibility VARCHAR(250),

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_runmeta_runid (runid) references run(runid),
  constraint unique index u_runmeta_k_rid (k, runid)
) ENGINE=InnoDB charset=utf8;

--
-- RUN.runLANE (LANE)
--
create table runcell (
  runcellid int(10)  primary key auto_increment,

  runid int(10) , 
  resourcecellid int(10) , -- lane 
  sampleid int(10) , 

  foreign key fk_runcell_rid (runid) references run(runid),
  foreign key fk_runcell_lid (resourcecellid) references resourcecell(resourcecellid),
  foreign key fk_runcell_sid (sampleid) references sample(sampleid),

  constraint unique index u_runcell_rid_lid (runid, resourcecellid),
  constraint unique index u_runcell_sid_rid (sampleid, runid) 
) ENGINE=InnoDB charset=utf8;

--
-- RESOURCE
--

create table runfile (
  runcellfileid int(10)  primary key auto_increment,

  runid int(10) ,
  fileid int(10) , 

  iname varchar(2048) , 
  name varchar(250) , 

  isactive int(1)  default 1, 
  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_rfile_rid (runid) references run(runid),
  foreign key fk_rfile_fid (fileid) references file(fileid),

  constraint unique index u_rlfile_fileid (fileid)
) ENGINE=InnoDB charset=utf8;

create table runcellfile (
  runcellfileid int(10)  primary key auto_increment,

  runcellid int(10) ,
  fileid int(10) , 
  iname varchar(2048) , 
  name varchar(250) , 

  isactive int(1)  default 1, 
  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_rlfile_rlid (runcellid) references runcell(runcellid),
  foreign key fk_rlfile_fileid (fileid) references file(fileid),

  constraint unique index u_rlfile_fileid (fileid)
) ENGINE=InnoDB charset=utf8;



--
-- TASK
-- 
create table task (
  taskid int(10)  primary key auto_increment,

  iname varchar(250) ,
  name varchar(250) ,
  constraint unique index u_task_iname (iname)
) ENGINE=InnoDB charset=utf8;

create table workflowtask (
  workflowtaskid int(10)  primary key auto_increment,
  workflowid int(10) ,
  taskid int(10) ,

  iname varchar(250) , -- can be multiple task, so this differenciates
  name varchar(250) ,
  foreign key fk_workflowtask_wid (workflowid) references workflow(workflowid),
  foreign key fk_workflowtask_tid (taskid) references task(taskid),
  constraint unique index u_workflowtask_iname (iname)
) ENGINE=InnoDB charset=utf8;

create table workflowtasksource (
  workflowtasksourceid int(10)  primary key auto_increment,
  workflowtaskid int(10) ,
  sourceworkflowtaskid int(10),
  foreign key fk_workflowtasksource_wid (workflowtaskid) references workflowtask(workflowtaskid),
  foreign key fk_workflowtasksource_sid (sourceworkflowtaskid) references workflowtask(workflowtaskid)
) ENGINE=InnoDB charset=utf8;

create table state (
  stateid int(10) not null primary key auto_increment,

  taskid int(10) not null, 
  name varchar(250) ,
  status varchar(50), -- 
  source_stateid int(10) ,

  startts datetime, 
  endts datetime, 

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_state_tid (taskid) references task(taskid),
  foreign key fk_state_ssid (source_stateid) references state(stateid)
) ENGINE=InnoDB charset=utf8;

create table statemeta (
  statemetaid int(10)  primary key auto_increment,
  stateid int(10) ,

  k varchar(250) ,
  v TEXT,
  position int(10)  default 0,
  rolevisibility VARCHAR(250),

  lastupdts timestamp  default current_timestamp,
  lastupduser int(10)  default 0,

  foreign key fk_statemeta_sid (stateid) references state(stateid),
  constraint unique index u_statemeta_k_pid (k, stateid)
) ENGINE=InnoDB charset=utf8;


create table statejob (
  statejobid int(10)  primary key auto_increment, 

  stateid int(10) , 
  jobid int(10) , 

  foreign key fk_statejob_sid (stateid) references state(stateid),
  foreign key fk_statejob_jid (jobid) references job(jobid)
) ENGINE=InnoDB charset=utf8;


create table statesample (
  statesampleid int(10)  primary key auto_increment, 

  stateid int(10) , 
  sampleid int(10) , 

  foreign key fk_statesample_sid (stateid) references state(stateid),
  foreign key fk_statesample_sampleid (sampleid) references sample(sampleid)
) ENGINE=InnoDB charset=utf8;

create table staterun (
  staterunid int(10)  primary key auto_increment, 

  stateid int(10) , 
  runid int(10) , 

  foreign key fk_staterun_sid (stateid) references state(stateid),
  foreign key fk_staterun_rid (runid) references run(runid)
) ENGINE=InnoDB charset=utf8;

--
-- tie illumina runs back to real tasks
--   [still unsure of this design]
create table stateruncell (
  stateruncellid int(10)  primary key auto_increment,
  
  stateid int(10) ,
  runcellid int(10) ,

  foreign key fk_stateruncell_sid (stateid) references state(stateid),
  foreign key fk_stateruncell_rlid (runcellid) references runcell(runcellid)
) ENGINE=InnoDB charset=utf8;




SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

DROP TABLE IF EXISTS `uifield`;
CREATE TABLE IF NOT EXISTS `uifield` (
  `uifieldid` int(10)  AUTO_INCREMENT,
  `locale` varchar(5) ,
  `domain` varchar(100) ,
  `area` varchar(50) ,
  `name` varchar(100) ,
  `attrname` varchar(50) ,
  `attrvalue` text DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  PRIMARY KEY (`uifieldid`),
  UNIQUE KEY `u_uifield_laaa` (`locale`,`area`,`name`,`attrname`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=537 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;


-- populate "sample type" <-> "list of meta fields" link tables. 

  
-- truncate table workflowsamplesubtype;
-- truncate table samplesubtype;

insert into samplesubtype(sampletypeid,iname,name)
select 
t.sampletypeid, 
concat(w.iname,t.iname, 'Sample'), 
concat(w.name, ' ', t.name, ' Sample')
from sampletype t 
join workflow w
where t.sampletypeid in (1, 3);

insert into workflowsamplesubtype(workflowid, samplesubtypeid)
select w.workflowid, st.samplesubtypeid
from workflow w
join sampletype t on t.sampletypeid in (1, 3)
join samplesubtype st on concat(w.iname, t.iname, 'Sample') = st.iname;


create table taskmapping (
  taskmappingid int(10) primary key not null auto_increment,
  iname varchar(50),
  name varchar(50),
  stepname varchar(255)  not null,
  status varchar(50) not null,
  listmap varchar(255) default null,
  permission varchar(255) not null,
  dashboardsortorder int(10),
  isactive int(1),
  UNIQUE KEY `u_taskmapping_issp` (`iname`,`stepname`,`status`,`permission`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;


insert into user
values
(1, 'super', 'super@super.com', SHA1('user'), 'Super', 'User', 'en_US', 1, now(), 1);

insert into userrole values (1, 1, 11, now(), 1);
insert into lab values (1, 1, 'default lab', 1, 1, now(), 1);
insert into labuser values (1, 1, 1, 6, now(), 1);
