--
-- META - for generic drop downs and such
--
create table meta (
  metaid int(10) not null primary key auto_increment,

  property varchar(250) not null, 
  k varchar(250) not null, -- internal value?
  v varchar(250) not null, -- external label?
  position int(10) not null default 0,

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  constraint unique index u_meta_p_k (property, k),
  constraint unique index u_meta_p_v (property, v)
) ENGINE=InnoDB charset=utf8;



--
-- USER 
--
create table user (
  userid int(10) not null primary key auto_increment,

  login varchar(250) not null, 
  email varchar(250) not null, 
  password varchar(250) not null, 
  firstname varchar(250) not null, 
  lastname varchar(250) not null, 

  locale varchar(5) not null default 'en_US',

  isactive int(1) not null default 1,
  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0, 

  constraint unique index u_user_login (login),
  constraint unique index u_user_email (email)
) ENGINE=InnoDB charset=utf8;



-- insert into user values 
-- ( 1, 'admin', 'admin@localhost',  PASSWORD('waspV2'), 'Admin', '-', 1, now(), 1 );

create table usermeta (
  usermetaid int(10) not null primary key auto_increment,
  userid int(10) not null,

  k varchar(250) not null, 
  v varchar(250), 
  position int(10) not null default 0,

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_usermeta_userid (userid) references user(userid),
  constraint unique index u_usermeta_k_uid (k, userid)
) ENGINE=InnoDB charset=utf8;

--
-- forgot password
--
create table userpasswordauth (
  userid int(10) not null primary key, 
  authcode varchar(250) not null,
  lastupdts timestamp not null default current_timestamp, 
  lastupduser int(10) not null default 0,

  foreign key fk_userpasswordauth_userid (userid) references user(userid),
  constraint unique index u_userpasswordauth (authcode)
) ENGINE=InnoDB charset=utf8;




--
-- ROLE
--
create table role ( 
  roleid int(10) not null primary key auto_increment, 
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
(15, 'u', 'User', 'user'); -- user, explicit


create table roleset (
  rolesetid int(10) not null primary key auto_increment, 
  parentroleid int(10) not null,
  childroleid int(10) not null,
  
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
  userroleid int(10) not null primary key auto_increment, 

  userid int(10) not null, 
  roleid int(10) not null, 

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_userrole_rid (roleid) references role(roleid),
  foreign key fk_userrole_uid (userid) references user(userid),

  constraint unique index userrole_uid_rid (userid, roleid)
) ENGINE=InnoDB charset=utf8;


-- insert into userrole values (1, 1, 1, now(), 1);

--
-- DEPARTMENT
--
create table department (
  departmentid int(10) not null primary key auto_increment,
  name varchar(250) not null,

  isinternal int(1) not null default 1,
  isactive int(1) not null default 1,
  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  constraint unique index u_department_name (name)
) ENGINE=InnoDB charset=utf8;

insert into department values 
( 1, 'Internal - Default Department', 1, 1, now(), 1 ),
( 2, 'External - Default Department', 0,  1, now(), 1 );

-- LAB.USER
--   - presumably 'Department Admin'
-- 
create table departmentuser ( 
  departmentuserid int(10) not null primary key auto_increment, 

  departmentid int(10) not null, 
  userid int(10) not null, 

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_departmentuser_uid (userid) references user(userid),
  foreign key fk_departmentuser_did (departmentid) references department(departmentid),

  constraint unique index u_departmentuser_did_uid (departmentid, userid)
) ENGINE=InnoDB charset=utf8;



--
-- LAB 
--
create table lab ( 
  labid int(10) not null primary key auto_increment,

  departmentid int(10) not null, 
  name varchar(250) not null,

  primaryuserid int(10) not null, -- primary investigator?

  isactive int(1) not null default 1,
  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_lab_did (departmentid) references department(departmentid),
  foreign key fk_lab_puid (primaryuserid) references user(userid)
--  There is no reason why lab name needs to be unique or why a user cannot have more than one lab
--  constraint unique index u_lab_name (name),
--  constraint unique index u_lab_puid (primaryuserid)
) ENGINE=InnoDB charset=utf8;

-- insert into lab values 
-- ( 1, 1, 'Default Lab',  1, 1, now(), 1 );

create table labmeta (
  labmetaid int(10) not null primary key auto_increment,
  labid int(10) not null,

  k varchar(250) not null, 
  v varchar(250), 
  position int(10) not null default 0,

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_labmeta_labid (labid) references lab(labid),
  constraint unique index u_labmeta_k_lid (k, labid)
) ENGINE=InnoDB charset=utf8;

--
-- LAB.USER
--   - presumably 'Lab Member', 'Lab Manager' or 'Primary Investigator'
-- 
create table labuser ( 
  labuserid int(10) not null primary key auto_increment, 

  labid int(10) not null, 
  userid int(10) not null, 

  roleid int(10) not null, 
   
  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_labuser_lid (labid) references lab(labid),
  foreign key fk_labuser_uid (userid) references user(userid),
  foreign key fk_labuser_rid (roleid) references role(roleid),

  constraint unique index u_labuser_lid_uid (labid, userid)
) ENGINE=InnoDB charset=utf8;

--
-- pending user
-- 
create table userpending (
  userpendingid int(10) not null primary key auto_increment,

  email varchar(250) not null, 
  password varchar(250) not null, 
  firstname varchar(250) not null, 
  lastname varchar(250) not null, 
  locale varchar(5) not null default 'en_US',
  labid int(10),

  status varchar(10) not null default 'PENDING', -- PENDING, APPROVED, DECLINED

  lastupdts timestamp not null default current_timestamp, 
  lastupduser int(10) not null default 0,

  foreign key fk_userpending_lid (labid) references lab(labid),

  index i_userpending_status(status, email) 
) ENGINE=InnoDB charset=utf8;

create table userpendingmeta (
  userpendingmetaid int(10) not null primary key auto_increment,
  userpendingid int(10) not null,

  k varchar(250) not null, 
  v varchar(250), 
  position int(10) not null default 0,

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_userpendingmeta_userpendingid (userpendingid) references userpending(userpendingid),
  constraint unique index u_userpendingmeta_k_lid (k, userpendingid)
) ENGINE=InnoDB charset=utf8;

create table labpending ( 
  labpendingid int(10) not null primary key auto_increment,

  departmentid int(10) not null, 
  name varchar(250) not null,

  primaryuserid int(10),
  userpendingid int(10),

  status varchar(10) not null default 'PENDING', -- PENDING, APPROVED, DECLINED

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_labpending_did (departmentid) references department(departmentid),
  foreign key fk_labpending_pruid (primaryuserid) references user(userid),
  foreign key fk_labpending_peuid (userpendingid) references userpending(userpendingid),
  index (status, name)
) ENGINE=InnoDB charset=utf8;

create table labpendingmeta (
  labpendingmetaid int(10) not null primary key auto_increment,
  labpendingid int(10) not null,

  k varchar(250) not null, 
  v varchar(250), 
  position int(10) not null default 0,

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_labpendingmeta_labpendingid (labpendingid) references labpending(labpendingid),
  constraint unique index u_labpendingmeta_k_lid (k, labpendingid)
) ENGINE=InnoDB charset=utf8;


--
-- confirm email
--
create table confirmemailauth (
  confirmemailauthid  int(10) not null primary key auto_increment,
  userpendingid int(10),
  userid int(10),
  authcode varchar(250) not null,
  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default '0',
  foreign key fk_labpending_uid (userid) references user(userid),
  foreign key fk_labpending_peuid (userpendingid) references userpending(userpendingid),
  constraint unique index u_confirmemailauth (authcode)
) ENGINE=InnoDB charset=utf8;


--
-- type.RESOURCE
-- 
create table resourcetype (
  resourcetypeid int(10) not null primary key auto_increment, 

  iname varchar(250) not null,
  name varchar(250) not null,

  constraint unique index u_resourcetype_iname (iname),
  constraint unique index u_resourcetype_name (name)
) ENGINE=InnoDB charset=utf8; 

insert into resourcetype values (1, 'dna', 'DNA Sequencer'); 
insert into resourcetype values (2, 'amplicon', 'DNA Amplicon'); 

--
-- RESOURCE
-- 
create table resource (
  resourceid int(10) not null primary key auto_increment, 

  platform varchar(250) not null,   -- another table?
  name varchar(250) not null,

  resourcetypeid int(10) not null,

  isactive int(1) not null default 1,
  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_resource_trid (resourcetypeid) references resourcetype(resourcetypeid),
  constraint unique index u_resource_name (name)
) ENGINE=InnoDB charset=utf8;



-- [join with meta?] 
create table resourcemeta (
  resourcemetaid int(10) not null primary key auto_increment,
  resourceid int(10) not null,

  k varchar(250) not null, 
  v varchar(250), 
  position int(10) not null default 0,

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_resourcemeta_resourceid (resourceid) references resource(resourceid),
  constraint unique index u_resourcemeta_k_rid (k, resourceid)
) ENGINE=InnoDB charset=utf8;


-- 
-- RESOURCE.USER 
--   - presumably 'Facilities Tech', maybe w/ roleid
  -- create table resourceuser ( 
  -- resourceuserid int(10) not null primary key auto_increment, 
  -- 
  -- resourceid int(10) not null, 
  -- userid int(10) not null, 
  -- 
  -- lastupdts timestamp not null default current_timestamp,
  -- lastupduser int(10) not null default 0,
  -- 
  -- foreign key fk_resourceuser_lid (resourceid) references resource(resourceid),
  -- foreign key fk_resourceuser_uid (userid) references user(userid),
  -- 
  -- constraint unique index u_resourceuser_rid_uid (resourceid, userid)
  -- ) ENGINE=InnoDB charset=utf8;



--


create table workflow (
  workflowid int(10) not null primary key auto_increment, 

  iname varchar(250) not null, 
  name varchar(250) not null, 
  createts datetime not null,

  isactive int(1) not null default 1,
  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  constraint unique index u_workflow_iname (iname),
  constraint unique index u_workflow_name (name)
) ENGINE=InnoDB charset=utf8;

create table workflowmeta (
  workflowmetaid int(10) not null primary key auto_increment,
  workflowid int(10) not null,

  k varchar(250) not null, 
  v varchar(250), 
  position int(10) not null default 0,

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_workflowmeta_workflowid (workflowid) references workflow(workflowid),
  constraint unique index u_workflowmeta_k_wid (k, workflowid)
) ENGINE=InnoDB charset=utf8;

--
-- JOB
--
create table job (
  jobid int(10) not null primary key auto_increment, 

  labid int(10) not null,
  userid int(10) not null,  -- investigator
  workflowid int(10) not null,  

  name varchar(250) not null, 
   
  createts datetime not null,

  viewablebylab int(1) not null default 0,

  isactive int(1) not null default 1,
  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_job_lid (labid) references lab(labid),
  foreign key fk_job_uid (userid) references user(userid),
  foreign key fk_job_wid (workflowid) references workflow(workflowid),

  constraint unique index u_job_name_lid (name, labid)
) ENGINE=InnoDB charset=utf8;


create table jobmeta (
  jobmetaid int(10) not null primary key auto_increment,
  jobid int(10) not null,

  k varchar(250) not null, 
  v varchar(250), 
  position int(10) not null default 0,

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_jobmeta_jobid (jobid) references job(jobid),

  constraint unique index u_jobmeta_k_jid (k, jobid)
) ENGINE=InnoDB charset=utf8;

--
-- JOB.USER
--   - presumably 'Job Submitter', 'Job Viewer'
-- 

create table jobuser ( 
  jobuserid int(10) not null primary key auto_increment, 

  jobid int(10) not null, 
  userid int(10) not null, 

  roleid int(10) not null, 
   
  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_jobuser_jid (jobid) references job(jobid),
  foreign key fk_jobuser_uid (userid) references user(userid),
  foreign key fk_jobuser_rid (roleid) references role(roleid),

  constraint unique index u_jobuser_jid_uid (jobid, userid)
) ENGINE=InnoDB charset=utf8;

create table jobdraft (
  jobdraftid int(10) not null primary key auto_increment, 

  labid int(10) not null,
  userid int(10) not null,  -- investigator
  workflowid int(10) not null,  

  name varchar(250) not null, 
   
  createts datetime not null,
  submittedjobid int(10),

  status varchar(50) not null default 1,
  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_jobdraft_lid (labid) references lab(labid),
  foreign key fk_jobdraft_uid (userid) references user(userid),
  foreign key fk_jobdraft_wid (workflowid) references workflow(workflowid),
  foreign key fk_jobdraft_sjid (submittedjobid) references job(jobid)

) ENGINE=InnoDB charset=utf8;


create table jobdraftmeta (
  jobdraftmetaid int(10) not null primary key auto_increment,
  jobdraftid int(10) not null,

  k varchar(250) not null, 
  v varchar(250), 
  position int(10) not null default 0,

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_jobdraftmeta_jdid (jobdraftid) references jobdraft(jobdraftid),

  constraint unique index u_jobdraftmeta_k_jdid (k, jobdraftid)
) ENGINE=InnoDB charset=utf8;

-- ---------------------------------------------------

--
-- PROJECT
--  user job folders?

create table project (
  projectid int(10) not null primary key auto_increment, 
  labid int(10) not null,
  userid int(10) not null,
 
  name varchar(250) not null,

  isactive int(1) not null default 1,
  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_project_lid (labid) references lab(labid),

  constraint unique index u_project_name_lid (name, labid)
) ENGINE=InnoDB charset=utf8;


--
-- BARCODE
--   - physical object tracker
--
create table barcode ( 
  barcodeid int(10) not null primary key auto_increment, 
  barcode varchar(250) not null, 

  barcodefor varchar(250), -- enum sample/resource 

  isactive int(1) not null default 1,
  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  constraint unique index u_barcode_bc (barcode)
) ENGINE=InnoDB charset=utf8;

create table resourcebarcode (
  resourcebarcodeid int(10) not null primary key auto_increment, 

  resourceid int(10) not null, 
  barcodeid int(10) not null, 

  foreign key fk_resourcebarcode_rid (resourceid) references resource(resourceid),
  foreign key fk_resourcebarcode_bcid (barcodeid) references barcode(barcodeid),

  constraint unique index u_resourcebarcode_rid (resourceid),
  constraint unique index u_resourcebarcode_bcid (barcodeid)
) ENGINE=InnoDB charset=utf8;

-- FILE
--   mysql max out at 767 bytes for indexable length
--
create table file (
  fileid int(10) not null primary key auto_increment,

  filelocation varchar(2048) not null, 
  contenttype varchar(250) not null, 
  sizek int(10) not null,
  md5hash varchar(250) not null,
  description varchar(250),

  isarchived int(1) not null default 0,
  isactive int(1) not null default 1,

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0 

  -- constraint unique index u_file_flocation (filelocation)
) ENGINE=InnoDB charset=utf8;


-- 
-- SAMPLE
--

create table sampletype (
  sampletypeid int(10) not null primary key auto_increment,
  iname varchar(250), 
  name varchar(250),

  constraint unique index u_sampletype_iname (iname),
  constraint unique index u_sampletype_name (name)
) ENGINE=InnoDB charset=utf8;

insert into sampletype values
(1, 'dna', 'DNA'), 
(2, 'tissue', 'Tissue'),
(3, 'library', 'Library'),
(4, 'lane', 'Lane'), 
(5, 'platformunit', 'Platform Unit');

create table samplesubtype (
  samplesubtypeid int(10) not null primary key auto_increment,

  sampletypeid int(10) not null,

  iname varchar(50) not null, -- meta field prefix
  name varchar(250) not null,

  constraint unique index u_samplesubtype_iname (iname),
  foreign key fk_samplesubtype_tsid (sampletypeid) references sampletype(sampletypeid)
) ENGINE=InnoDB charset=utf8;

create table workflowsamplesubtype (
  workflowsamplesubtypeid int(10) not null primary key auto_increment,
  workflowid int(10) not null,
  samplesubtypeid int(10) not null,
 
  constraint unique index u_samplesubtype_wid_stsid (workflowid, samplesubtypeid),

  foreign key fk_workflowsamplesubtype_stsid (samplesubtypeid) references samplesubtype(samplesubtypeid),
  foreign key fk_workflowsamplesubtype_wid (workflowid) references workflow(workflowid)
) ENGINE=InnoDB charset=utf8;

create table sample (
  sampleid int(10) not null primary key auto_increment,

  sampletypeid int(10) not null,
  samplesubtypeid int(10),

  submitter_labid int(10) not null,
  submitter_userid int(10) not null,
  submitter_jobid int(10) null,

  isreceived int(1) not null default 0,
  receiver_userid int(10),
  receivedts datetime,

  name varchar(250),
  isgood int(1),

  isactive int(1) not null default 1,
  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_sample_tsid (sampletypeid) references sampletype(sampletypeid),
  foreign key fk_sample_stsid (samplesubtypeid) references samplesubtype(samplesubtypeid),
  foreign key fk_sample_sjid (submitter_jobid) references job(jobid),
  foreign key fk_sample_slid (submitter_labid) references lab(labid),
  foreign key fk_sample_suid (submitter_userid) references user(userid)
) ENGINE=InnoDB charset=utf8;

create table samplemeta (
  samplemetaid int(10) not null primary key auto_increment,
  sampleid int(10) not null,

  k varchar(250) not null, 
  v varchar(250), 
  position int(10) not null default 0,

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_samplemeta_sampleid (sampleid) references sample(sampleid),
  constraint unique index u_samplemeta_k_sid (k, sampleid)
) ENGINE=InnoDB charset=utf8;


-- SAMPLE.SOURCE
--   - if a sample has a parent, what is it. 
create table samplesource (
  samplesourceid int(10) not null primary key auto_increment, 
  sampleid int(10) not null,
  multiplexindex int(10) not null default 0,
  source_sampleid int(10) not null,

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_samplesource_sid (sampleid) references sample(sampleid),
  foreign key fk_samplesource_ssid (source_sampleid) references sample(sampleid),

  constraint unique index u_samplesource_sid (sampleid, multiplexindex)
) ENGINE=InnoDB charset=utf8;

-- SAMPLE.BARCODE 
create table samplebarcode (
  samplebarcode int(10) not null primary key auto_increment, 
 
  sampleid int(10) not null, 
  barcodeid int(10) not null, 
 
  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_samplebarcode_sid (sampleid) references sample(sampleid),
  foreign key fk_samplebarcode_bid (barcodeid) references barcode(barcodeid),

  constraint unique index u_samplebarcode_sid (sampleid),
  constraint unique index u_samplebarcode_bcid (barcodeid)
) ENGINE=InnoDB charset=utf8;

-- SAMPLE.LAB
--   - lab share?
create table samplelab (
  samplelabid int(10) not null primary key auto_increment, 
  
  sampleid int(10) not null,  
  labid int(10) not null,

  isprimary int(1) not null default 0, 

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_samplelab_sid (sampleid) references sample(sampleid),
  foreign key fk_samplelab_lid (labid) references lab(labid),

  constraint unique index u_samplelab_sid_lid (sampleid, labid)
) ENGINE=InnoDB charset=utf8;


create table sampledraft (
  sampledraftid int(10) not null primary key auto_increment,
  sampletypeid int(10) not null,
  samplesubtypeid int(10) not null,

  labid int(10) not null,
  userid int(10) not null,
  jobdraftid int(10) null,

  sourcesampleid int(10) null,
  fileid int(10) null,

  name varchar(250),
  status varchar(50), 

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_sampledraft_tsid (sampletypeid) references sampletype(sampletypeid),
  foreign key fk_sampledraft_stsid (samplesubtypeid) references samplesubtype(samplesubtypeid),
  foreign key fk_sampledraft_sjid (jobdraftid) references jobdraft(jobdraftid),
  foreign key fk_sampledraft_slid (labid) references lab(labid),
  foreign key fk_sampledraft_suid (userid) references user(userid),
  foreign key fk_sampledraft_fid (fileid) references file(fileid)
) ENGINE=InnoDB charset=utf8;

create table sampledraftmeta (
  sampledraftmetaid int(10) not null primary key auto_increment,
  sampledraftid int(10) not null,

  k varchar(250) not null, 
  v varchar(250), 
  position int(10) not null default 0,

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_sampledraftmeta_sdid (sampledraftid) references sampledraft(sampledraftid),
  constraint unique index u_sampledraftmeta_k_sid (k, sampledraftid)
) ENGINE=InnoDB charset=utf8;

create table jobdraftcell (
  jobdraftcellid int(10) not null primary key auto_increment,
  jobdraftid int(10) not null,
  cellindex int(10) not null,

  foreign key fk_jobdraft_jid (jobdraftid) references jobdraft(jobdraftid),
  constraint unique index u_jobdraftcell_jdid_ci (jobdraftid, cellindex)
);

create table sampledraftcell (
  sampledraftcellid int(10) not null primary key auto_increment,
  sampledraftid int(10) not null,
  jobdraftcellid int(10) not null,

  libraryindex int(10) not null, 

  foreign key fk_sampledraftcell_sdid (sampledraftid) references sampledraft(sampledraftid),
  foreign key fk_sampledraftcell_jdcid (jobdraftcellid) references jobdraftcell(jobdraftcellid),
  constraint unique index u_sampledraftcell_jdcid_li (jobdraftcellid, libraryindex)
);


--
-- back to the job
--


-- 
-- JOB.SAMPLE
--   - maps samples used and created to jobs
create table jobsample ( 
  jobsampleid int(10) not null primary key auto_increment, 
  jobid int(10) not null, 
  sampleid int(10) not null,

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_jobsample_jid (jobid) references job(jobid),
  foreign key fk_jobsample_sid (sampleid) references sample(sampleid),

  constraint unique index u_jobsample_jid_sid (jobid, sampleid)
) ENGINE=InnoDB charset=utf8;

create table jobsamplemeta (
  jobsamplemetaid int(10) not null primary key auto_increment,
  jobsampleid int(10) not null,

  k varchar(250) not null, 
  v varchar(250), 
  position int(10) not null default 0,

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_jobsamplemeta_jsid (jobsampleid) references jobsample(jobsampleid),
  constraint unique index u_jobsamplemeta_k_jsid (k, jobsampleid)
) ENGINE=InnoDB charset=utf8;


create table jobcell (
  jobcellid int(10) not null primary key auto_increment,
  jobid int(10) not null,
  cellindex int(10) not null,

  foreign key fk_job_jid (jobid) references job(jobid),
  constraint unique index u_jobcell_jdid_ci (jobid, cellindex)
);

create table samplecell (
  samplecellid int(10) not null primary key auto_increment,
  sampleid int(10) not null,
  jobcellid int(10) not null,

  libraryindex int(10) not null, 

  foreign key fk_samplecell_sdid (sampleid) references sample(sampleid),
  foreign key fk_samplecell_jdcid (jobcellid) references jobcell(jobcellid),
  constraint unique index u_samplecell_jdcid_li (jobcellid, libraryindex)
);


--
-- ACCOUNTING TABLES
--
create table acct_workflowcost (
  workflowid int(10) not null primary key,
  basecost float(10,2) not null,

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_acct_workflowcost_wfid (workflowid) references job(workflowid)
) ENGINE=InnoDB charset=utf8;


create table acct_quote (
  quoteid int(10) not null primary key auto_increment,
  jobid int(10) not null,
  amount float(10,2) not null,
  userid int(10), -- quoter
  comment varchar(250),

  isactive int(1) not null default 1,
  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_acct_quote_jid (jobid) references job(jobid),
  foreign key fk_acct_quote_uid (userid) references user(userid)
) ENGINE=InnoDB charset=utf8;

create table acct_jobquotecurrent (
  jobid int(10) not null primary key,
  quoteid int(10) not null,

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,


  foreign key fk_acct_jobquotecurrent_jid (jobid) references job(jobid),
  foreign key fk_acct_jobquotecurrent_qid (quoteid) references acct_quote(quoteid)
) ENGINE=InnoDB charset=utf8;

create table acct_quoteuser (
  quoteuserid int(10) not null primary key auto_increment, 
  quoteid int(10) not null,
  userid int(10) not null,
  roleid int(10) not null, -- PI, DA, FM,  
  isapproved int(1) not null,
  comment varchar(250),

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,


  foreign key fk_acct_quote_jid (quoteid) references acct_quote(quoteid),
  foreign key fk_acct_quote_uid (userid) references user(userid),
  foreign key fk_acct_quote_rid (roleid) references role(roleid)
) ENGINE=InnoDB charset=utf8;


create table acct_invoice (
  invoiceid int(10) not null primary key auto_increment,
  quoteid int(10) not null,
  jobid int(10) not null, -- DENORMALIZED
  amount float(10,2) not null,
  comment varchar(250),

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,


  foreign key fk_acct_invoice_qid (quoteid) references acct_quote(quoteid),
  foreign key fk_acct_invoice_jid (jobid) references job(jobid)
) ENGINE=InnoDB charset=utf8;

create table acct_ledger (
  ledgerid int(10) not null primary key auto_increment,
  invoiceid int(10) not null,
  jobid int(10) not null, -- DENORMALIZED
  amount float(10,2),
  comment varchar(250),

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,


  foreign key fk_acct_ledger_iid (invoiceid) references acct_invoice(invoiceid),
  foreign key fk_acct_ledger_jid (jobid) references job(jobid)
) ENGINE=InnoDB charset=utf8;

create table acct_grant (
  grantid int(10) not null primary key auto_increment,
  labid int(10) not null,
  name varchar(250) not null,
  code varchar(250) not null,
  expirationdt datetime,

  isactive int(1) not null default 1,
  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,


  foreign key fk_grant_lid (labid) references lab(labid)
) ENGINE=InnoDB charset=utf8;

create table acct_grantjob (
  jobid int(10) not null primary key auto_increment, 
  grantid int(10) not null,

  isactive int(1) not null default 1,
  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_acct_ledgergrant_jid (jobid) references acct_ledger(jobid),
  foreign key fk_acct_ledgergrant_gid (grantid) references acct_grant(grantid)
) ENGINE=InnoDB charset=utf8;


--
--
-- JOB.FILE
--
create table jobfile ( 
  jobfileid int(10) not null primary key auto_increment,
  jobid int(10) not null, 
  fileid int(10) not null, 

  iname varchar(2048), -- not null
  name varchar(250), 
  description varchar(2048), 

  isactive int(1) not null default 1, 
  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_jobfile_jid (jobid) references job(jobid),
  foreign key fk_jobfile_fid (fileid) references file(fileid) -- ,

  -- constraint unique index u_jobfile_iname_jid (iname, jobid)
) ENGINE=InnoDB charset=utf8;

create table samplefile ( 
  samplefileid int(10) not null primary key auto_increment,
  sampleid int(10) not null, 
  fileid int(10) not null, 

  iname varchar(2048), -- not null
  name varchar(250), 
  description varchar(2048), 

  isactive int(1) not null default 1, 
  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_samplefile_sid (sampleid) references sample(sampleid),
  foreign key fk_samplefile_fid (fileid) references file(fileid) -- ,

  -- constraint unique index u_samplefile_iname_jid (iname, sampleid)
) ENGINE=InnoDB charset=utf8;


-- ---------------------------------

-- resource stuffs

--
-- RESOURCELANE LANE
--

create table resourcecell (
  resourcecellid int(10) not null primary key auto_increment,
  resourceid int(10) not null,
  iname varchar(250),
  name varchar(250),

  foreign key fk_resourcecell_rid (resourceid) references resource(resourceid),
  constraint unique index u_resourcecell_iname_rid (iname, resourceid),
  constraint unique index u_resourcecell_name_rid (name, resourceid)
) ENGINE=InnoDB charset=utf8;

--
-- RUN
--

create table run (
  runid int(10) not null primary key auto_increment,

  resourceid int(10) not null,
  userid int(10) not null, -- facilities tech

  name varchar(250) not null, 
  sampleid int(10) not null, -- flowcell

  startts datetime, 
  endts datetime, 

  status varchar(250), -- ????

  isactive int(1) not null default 1, 
  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_run_rid (resourceid) references resource(resourceid),
  foreign key fk_run_sid (sampleid) references sample(sampleid),
  foreign key fk_run_userid (userid) references user(userid)
) ENGINE=InnoDB charset=utf8;

create table runmeta (
  runmetaid int(10) not null primary key auto_increment,
  runid int(10) not null,

  k varchar(250) not null, 
  v varchar(250), 
  position int(10) not null default 0,

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_runmeta_runid (runid) references run(runid),
  constraint unique index u_runmeta_k_rid (k, runid)
) ENGINE=InnoDB charset=utf8;

--
-- RUN.resourceLANE (LANE)
--
create table runcell (
  runcellid int(10) not null primary key auto_increment,

  runid int(10) not null, 
  resourcecellid int(10) not null, -- lane 
  sampleid int(10) not null, 

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
  runcellfileid int(10) not null primary key auto_increment,

  runid int(10) not null,
  fileid int(10) not null, 

  iname varchar(2048) not null, 
  name varchar(250) not null, 

  isactive int(1) not null default 1, 
  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_rfile_rid (runid) references run(runid),
  foreign key fk_rfile_fid (fileid) references file(fileid),

  constraint unique index u_rlfile_fileid (fileid)
) ENGINE=InnoDB charset=utf8;

create table runcellfile (
  runcellfileid int(10) not null primary key auto_increment,

  runcellid int(10) not null,
  fileid int(10) not null, 
  iname varchar(2048) not null, 
  name varchar(250) not null, 

  isactive int(1) not null default 1, 
  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_rlfile_rlid (runcellid) references runcell(runcellid),
  foreign key fk_rlfile_fileid (fileid) references file(fileid),

  constraint unique index u_rlfile_fileid (fileid)
) ENGINE=InnoDB charset=utf8;



--
-- TASK
-- 
create table task (
  taskid int(10) not null primary key auto_increment,

  iname varchar(250) not null,
  name varchar(250) not null,
  constraint unique index u_task_iname (iname)
) ENGINE=InnoDB charset=utf8;

create table workflowtask (
  workflowtaskid int(10) not null primary key auto_increment,
  workflowid int(10) not null,
  taskid int(10) not null,

  iname varchar(250) not null, -- can be multiple task, so this differenciates
  name varchar(250) not null
) ENGINE=InnoDB charset=utf8;

create table workflowtasksource (
  workflowtasksourceid int(10) not null primary key auto_increment,
  workflowtaskid int(10) not null,
  sourceworkflowtaskid int(10) not null
) ENGINE=InnoDB charset=utf8;

create table state (
  stateid int(10) not null primary key auto_increment,

  taskid int(10) not null, 
  name varchar(250) not null,
  status varchar(50), -- 

  startts datetime, 
  endts datetime, 

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_state_tid (taskid) references task(taskid)
) ENGINE=InnoDB charset=utf8;

create table statemeta (
  statemetaid int(10) not null primary key auto_increment,
  stateid int(10) not null,

  k varchar(250) not null,
  v varchar(250),
  position int(10) not null default 0,

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_statemeta_sid (stateid) references state(stateid),
  constraint unique index u_statemeta_k_pid (k, stateid)
) ENGINE=InnoDB charset=utf8;


create table statejob (
  statejobid int(10) not null primary key auto_increment, 

  stateid int(10) not null, 
  jobid int(10) not null, 

  foreign key fk_statejob_sid (stateid) references state(stateid),
  foreign key fk_statejob_jid (jobid) references job(jobid)
) ENGINE=InnoDB charset=utf8;


create table statesample (
  statesampleid int(10) not null primary key auto_increment, 

  stateid int(10) not null, 
  sampleid int(10) not null, 

  foreign key fk_statesample_sid (stateid) references state(stateid),
  foreign key fk_statesample_sampleid (sampleid) references sample(sampleid)
) ENGINE=InnoDB charset=utf8;

create table staterun (
  staterunid int(10) not null primary key auto_increment, 

  stateid int(10) not null, 
  runid int(10) not null, 

  foreign key fk_staterun_sid (stateid) references state(stateid),
  foreign key fk_staterun_rid (runid) references run(runid)
) ENGINE=InnoDB charset=utf8;

--
-- tie illumina runs back to real tasks
--   [still unsure of this design]
create table stateruncell (
  stateruncellid int(10) not null primary key auto_increment,
  
  stateid int(10) not null,
  runcellid int(10) not null,

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
  `uifieldid` int(10) NOT NULL AUTO_INCREMENT,
  `locale` varchar(5) NOT NULL,
  `area` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `attrname` varchar(50) NOT NULL,
  `attrvalue` varchar(500) DEFAULT NULL,
  `lastupdts` datetime DEFAULT NULL,
  `lastupduser` int(11) DEFAULT NULL,
  PRIMARY KEY (`uifieldid`),
  UNIQUE KEY `u_uifield_laaa` (`locale`,`area`,`name`,`attrname`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=537 ;


INSERT INTO `uifield` (`uifieldid`, `locale`, `area`, `name`, `attrname`, `attrvalue`, `lastupdts`, `lastupduser`) VALUES
(1, 'en_US', 'user', 'login', 'label', 'Login', '2011-09-15 11:52:20', 133),
(2, 'en_US', 'user', 'login', 'error', 'Login cannot be empty', '2011-09-15 11:29:19', 133),
(4, 'en_US', 'user', 'detail', 'label', 'User Detail', '2011-09-15 10:30:52', 133),
(5, 'en_US', 'user', 'detail', 'metaposition', 'User Details', '2011-09-15 10:30:59', 133),
(6, 'en_US', 'user', 'password', 'label', 'Password', NULL, 1),
(7, 'en_US', 'user', 'password', 'error', 'Password cannot be empty', NULL, 1),
(8, 'en_US', 'user', 'firstName', 'label', 'First Name', NULL, 1),
(9, 'en_US', 'user', 'firstName', 'error', 'First Name cannot be empty', NULL, 1),
(10, 'en_US', 'user', 'lastName', 'label', 'Last Name', NULL, 1),
(11, 'en_US', 'user', 'lastName', 'error', 'Last Name cannot be empty', NULL, 1),
(12, 'en_US', 'user', 'email', 'label', 'Email', NULL, 1),
(13, 'en_US', 'user', 'email', 'error', 'Wrong email address format', NULL, 1),
(14, 'en_US', 'user', 'locale', 'label', 'Locale', NULL, 1),
(15, 'en_US', 'user', 'locale', 'error', 'Locale cannot be empty', NULL, 1),
(16, 'en_US', 'user', 'isActive', 'label', 'Active', NULL, 1),
(17, 'en_US', 'user', 'title', 'label', 'Title', NULL, 1),
(18, 'en_US', 'user', 'title', 'error', 'Title cannot be empty', NULL, 1),
(19, 'en_US', 'user', 'title', 'constraint', 'NotEmpty', NULL, 1),
(20, 'en_US', 'user', 'title', 'control', 'select:Prof:Prof;Dr:Dr;Mr:Mr;Ms:Ms;Mrs:Mrs;Miss:Miss', NULL, 1),
(21, 'en_US', 'user', 'title', 'metaposition', '1', NULL, 1),
(22, 'en_US', 'user', 'institution', 'label', 'Institution', NULL, 1),
(23, 'en_US', 'user', 'institution', 'error', 'Institution cannot be empty', NULL, 1),
(24, 'en_US', 'user', 'institution', 'constraint', 'NotEmpty', NULL, 1),
(25, 'en_US', 'user', 'institution', 'metaposition', '10', NULL, 1),
(26, 'en_US', 'user', 'department', 'label', 'Department', NULL, 1),
(27, 'en_US', 'user', 'department', 'metaposition', '20', NULL, 1),
(28, 'en_US', 'user', 'building_room', 'label', 'Room', NULL, 1),
(29, 'en_US', 'user', 'building_room', 'metaposition', '30', NULL, 1),
(30, 'en_US', 'user', 'address', 'label', 'Address', NULL, 1),
(31, 'en_US', 'user', 'address', 'error', 'Address cannot be empty', NULL, 1),
(32, 'en_US', 'user', 'address', 'constraint', 'dscsdcsdc', '2011-09-15 11:42:10', 133),
(33, 'en_US', 'user', 'address', 'metaposition', '40', NULL, 1),
(34, 'en_US', 'user', 'city', 'label', 'City', NULL, 1),
(35, 'en_US', 'user', 'city', 'error', 'City cannot be empty', NULL, 1),
(36, 'en_US', 'user', 'city', 'constraint', 'NotEmpty', '2011-09-15 11:05:52', 133),
(37, 'en_US', 'user', 'city', 'metaposition', '50', NULL, 1),
(38, 'en_US', 'user', 'state', 'label', 'State', NULL, 1),
(39, 'en_US', 'user', 'state', 'error', 'State cannot be empty', NULL, 1),
(40, 'en_US', 'user', 'state', 'control', 'select:${states}:code:name', NULL, 1),
(41, 'en_US', 'user', 'state', 'constraint', 'NotEmpty', NULL, 1),
(42, 'en_US', 'user', 'state', 'metaposition', '60', NULL, 1),
(43, 'en_US', 'user', 'country', 'label', 'Country', NULL, 1),
(44, 'en_US', 'user', 'country', 'error', 'Country cannot be empty', NULL, 1),
(45, 'en_US', 'user', 'country', 'control', 'select:${countries}:code:name', NULL, 1),
(46, 'en_US', 'user', 'country', 'constraint', 'NotEmpty', NULL, 1),
(47, 'en_US', 'user', 'country', 'metaposition', '70', NULL, 1),
(48, 'en_US', 'user', 'zip', 'label', 'Zip', NULL, 1),
(49, 'en_US', 'user', 'zip', 'error', 'Zip cannot be empty', NULL, 1),
(50, 'en_US', 'user', 'zip', 'constraint', 'NotEmpty', NULL, 1),
(51, 'en_US', 'user', 'zip', 'metaposition', '80', NULL, 1),
(52, 'en_US', 'user', 'phone', 'label', 'Phone', NULL, 1),
(53, 'en_US', 'user', 'phone', 'error', 'Phone cannot be empty', NULL, 1),
(54, 'en_US', 'user', 'phone', 'constraint', 'NotEmpty', NULL, 1),
(55, 'en_US', 'user', 'phone', 'metaposition', '90', NULL, 1),
(56, 'en_US', 'user', 'fax', 'label', 'Fax', NULL, 1),
(57, 'en_US', 'user', 'fax', 'error', 'Fax cannot be empty', NULL, 1),
(58, 'en_US', 'user', 'fax', 'constraint', 'NotEmpty', NULL, 1),
(59, 'en_US', 'user', 'fax', 'metaposition', '100', NULL, 1),
(60, 'en_US', 'userPending', 'page', 'label', 'New User', '2011-09-15 10:31:27', 133),
(61, 'en_US', 'userPending', 'emailsent', 'data', 'Thank you for your account request. You have been sent an email with instructions as to how to confirm your email address. Your principle investigator has also been emailed to request confirmation of your eligibility to join their lab and you are advised', NULL, 1),
(64, 'en_US', 'userPending', 'password', 'label', 'Password', NULL, 1),
(65, 'en_US', 'userPending', 'password', 'error', 'Password cannot be empty', NULL, 1),
(66, 'en_US', 'userPending', 'password_mismatch', 'error', 'The two entries for your password are NOT identical', NULL, 1),
(67, 'en_US', 'userPending', 'password_invalid', 'error', 'Password must be at least 8 characters, containing only letters and numbers, with at least one letter and number', NULL, 1),
(68, 'en_US', 'userPending', 'password2', 'label', 'Re-confirm Password', NULL, 1),
(69, 'en_US', 'userPending', 'password2', 'error', 'Re-confirm password cannot be empty', NULL, 1),
(70, 'en_US', 'userPending', 'firstName', 'label', 'First Name', NULL, 1),
(71, 'en_US', 'userPending', 'firstName', 'error', 'First Name cannot be empty', NULL, 1),
(72, 'en_US', 'userPending', 'lastName', 'label', 'Last Name', NULL, 1),
(73, 'en_US', 'userPending', 'lastName', 'error', 'Last Name cannot be empty', NULL, 1),
(74, 'en_US', 'userPending', 'email', 'label', 'Email', NULL, 1),
(75, 'en_US', 'userPending', 'email', 'error', 'Must be correctly formatted', NULL, 1),
(76, 'en_US', 'userPending', 'email_exists', 'error', 'Email already exists in the database', NULL, 1),
(77, 'en_US', 'userPending', 'locale', 'label', 'Locale', NULL, 1),
(78, 'en_US', 'userPending', 'locale', 'error', 'Locale cannot be empty', NULL, 1),
(79, 'en_US', 'userPending', 'primaryuseremail', 'label', 'PI Email Address', NULL, 1),
(80, 'en_US', 'userPending', 'primaryuseremail', 'error', 'Must be correctly formatted', NULL, 1),
(81, 'en_US', 'userPending', 'primaryuseremail_notvalid', 'error', 'Not an active registered PI email address', NULL, 1),
(82, 'en_US', 'userPending', 'primaryuseremail', 'constraint', 'isValidPiEmail', NULL, 1),
(83, 'en_US', 'userPending', 'primaryuseremail', 'metaposition', '1', NULL, 1),
(84, 'en_US', 'userPending', 'title', 'label', 'Title', NULL, 1),
(85, 'en_US', 'userPending', 'title', 'error', 'Title cannot be empty', NULL, 1),
(86, 'en_US', 'userPending', 'title', 'constraint', 'NotEmpty', NULL, 1),
(87, 'en_US', 'userPending', 'title', 'control', 'select:Prof:Prof;Dr:Dr;Mr:Mr;Ms:Ms;Mrs:Mrs;Miss:Miss', NULL, 1),
(88, 'en_US', 'userPending', 'title', 'metaposition', '5', NULL, 1),
(89, 'en_US', 'userPending', 'institution', 'label', 'Institution', NULL, 1),
(90, 'en_US', 'userPending', 'institution', 'error', 'Institution cannot be empty', NULL, 1),
(91, 'en_US', 'userPending', 'institution', 'constraint', 'NotEmpty', NULL, 1),
(92, 'en_US', 'userPending', 'institution', 'metaposition', '10', NULL, 1),
(93, 'en_US', 'userPending', 'department', 'label', 'Department', NULL, 1),
(94, 'en_US', 'userPending', 'department', 'metaposition', '20', NULL, 1),
(95, 'en_US', 'userPending', 'building_room', 'label', 'Room', NULL, 1),
(96, 'en_US', 'userPending', 'building_room', 'metaposition', '30', NULL, 1),
(97, 'en_US', 'userPending', 'address', 'label', 'Address', NULL, 1),
(98, 'en_US', 'userPending', 'address', 'error', 'Address cannot be empty', NULL, 1),
(99, 'en_US', 'userPending', 'address', 'constraint', 'NotEmpty', NULL, 1),
(100, 'en_US', 'userPending', 'address', 'metaposition', '40', NULL, 1),
(101, 'en_US', 'userPending', 'city', 'label', 'City', NULL, 1),
(102, 'en_US', 'userPending', 'city', 'error', 'City cannot be empty', NULL, 1),
(103, 'en_US', 'userPending', 'city', 'constraint', 'NotEmpty', NULL, 1),
(104, 'en_US', 'userPending', 'city', 'metaposition', '50', NULL, 1),
(105, 'en_US', 'userPending', 'state', 'label', 'State', NULL, 1),
(106, 'en_US', 'userPending', 'state', 'control', 'select:${states}:code:name', NULL, 1),
(107, 'en_US', 'userPending', 'state', 'metaposition', '60', NULL, 1),
(108, 'en_US', 'userPending', 'country', 'label', 'Country', NULL, 1),
(109, 'en_US', 'userPending', 'country', 'error', 'Country cannot be empty', NULL, 1),
(110, 'en_US', 'userPending', 'country', 'control', 'select:${countries}:code:name', NULL, 1),
(111, 'en_US', 'userPending', 'country', 'constraint', 'NotEmpty', NULL, 1),
(112, 'en_US', 'userPending', 'country', 'metaposition', '70', NULL, 1),
(113, 'en_US', 'userPending', 'zip', 'label', 'Zip', NULL, 1),
(114, 'en_US', 'userPending', 'zip', 'error', 'Zip cannot be empty', NULL, 1),
(115, 'en_US', 'userPending', 'zip', 'constraint', 'NotEmpty', NULL, 1),
(116, 'en_US', 'userPending', 'zip', 'metaposition', '80', NULL, 1),
(117, 'en_US', 'userPending', 'phone', 'label', 'Phone', NULL, 1),
(118, 'en_US', 'userPending', 'phone', 'error', 'Phone cannot be empty', NULL, 1),
(119, 'en_US', 'userPending', 'phone', 'constraint', 'NotEmpty', NULL, 1),
(120, 'en_US', 'userPending', 'phone', 'metaposition', '90', NULL, 1),
(121, 'en_US', 'userPending', 'fax', 'label', 'Fax', NULL, 1),
(122, 'en_US', 'userPending', 'fax', 'metaposition', '100', NULL, 1),
(123, 'en_US', 'piPending', 'password', 'label', 'PI Password', NULL, 1),
(124, 'en_US', 'piPending', 'password', 'error', 'Password cannot be empty', NULL, 1),
(125, 'en_US', 'piPending', 'firstName', 'label', 'PI First Name', NULL, 1),
(126, 'en_US', 'piPending', 'firstName', 'error', 'First Name cannot be empty', NULL, 1),
(127, 'en_US', 'piPending', 'lastName', 'label', 'PI Last Name', NULL, 1),
(128, 'en_US', 'piPending', 'lastName', 'error', 'Last Name cannot be empty', NULL, 1),
(129, 'en_US', 'piPending', 'email', 'label', 'PI Email', NULL, 1),
(130, 'en_US', 'piPending', 'email', 'error', 'Wrong email address format', NULL, 1),
(131, 'en_US', 'piPending', 'locale', 'label', 'PI Locale', NULL, 1),
(132, 'en_US', 'piPending', 'locale', 'error', 'Locale cannot be empty', NULL, 1),
(133, 'en_US', 'piPending', 'labName', 'label', 'Lab Name', NULL, 1),
(134, 'en_US', 'piPending', 'labName', 'error', 'Lab Name cannot be empty', NULL, 1),
(135, 'en_US', 'piPending', 'labName', 'constraint', 'NotEmpty', NULL, 1),
(136, 'en_US', 'piPending', 'labName', 'metaposition', '1', NULL, 1),
(137, 'en_US', 'piPending', 'departmentId', 'label', 'departmentId', NULL, 1),
(138, 'en_US', 'piPending', 'departmentId', 'error', 'departmentId cannot be empty', NULL, 1),
(139, 'en_US', 'piPending', 'departmentId', 'constraint', 'NotEmpty', NULL, 1),
(140, 'en_US', 'piPending', 'departmentId', 'metaposition', '2', NULL, 1),
(141, 'en_US', 'piPending', 'title', 'label', 'Title', NULL, 1),
(142, 'en_US', 'piPending', 'title', 'error', 'Title cannot be empty', NULL, 1),
(143, 'en_US', 'piPending', 'title', 'constraint', 'NotEmpty', NULL, 1),
(144, 'en_US', 'piPending', 'title', 'control', 'select:Prof:Prof;Dr:Dr;Mr:Mr;Ms:Ms;Mrs:Mrs;Miss:Miss', NULL, 1),
(145, 'en_US', 'piPending', 'title', 'metaposition', '5', NULL, 1),
(146, 'en_US', 'piPending', 'institution', 'label', 'Institution', NULL, 1),
(147, 'en_US', 'piPending', 'institution', 'error', 'Institution cannot be empty', NULL, 1),
(148, 'en_US', 'piPending', 'institution', 'constraint', 'NotEmpty', NULL, 1),
(149, 'en_US', 'piPending', 'institution', 'metaposition', '10', NULL, 1),
(150, 'en_US', 'piPending', 'department', 'label', 'Department', NULL, 1),
(151, 'en_US', 'piPending', 'department', 'metaposition', '20', NULL, 1),
(152, 'en_US', 'piPending', 'building_room', 'label', 'Room', NULL, 1),
(153, 'en_US', 'piPending', 'building_room', 'metaposition', '30', NULL, 1),
(154, 'en_US', 'piPending', 'address', 'label', 'Address', NULL, 1),
(155, 'en_US', 'piPending', 'address', 'error', 'Address cannot be empty', NULL, 1),
(156, 'en_US', 'piPending', 'address', 'constraint', 'NotEmpty', NULL, 1),
(157, 'en_US', 'piPending', 'address', 'metaposition', '40', NULL, 1),
(158, 'en_US', 'piPending', 'city', 'label', 'City', NULL, 1),
(159, 'en_US', 'piPending', 'city', 'error', 'City cannot be empty', NULL, 1),
(160, 'en_US', 'piPending', 'city', 'constraint', 'NotEmpty', NULL, 1),
(161, 'en_US', 'piPending', 'city', 'metaposition', '50', NULL, 1),
(162, 'en_US', 'piPending', 'state', 'label', 'State', NULL, 1),
(163, 'en_US', 'piPending', 'state', 'control', 'select:${states}:code:name', NULL, 1),
(164, 'en_US', 'piPending', 'state', 'metaposition', '60', NULL, 1),
(165, 'en_US', 'piPending', 'country', 'label', 'Country', NULL, 1),
(166, 'en_US', 'piPending', 'country', 'error', 'Country cannot be empty', NULL, 1),
(167, 'en_US', 'piPending', 'country', 'control', 'select:${countries}:code:name', NULL, 1),
(168, 'en_US', 'piPending', 'country', 'constraint', 'NotEmpty', NULL, 1),
(169, 'en_US', 'piPending', 'country', 'metaposition', '70', NULL, 1),
(170, 'en_US', 'piPending', 'zip', 'label', 'Zip', NULL, 1),
(171, 'en_US', 'piPending', 'zip', 'error', 'Zip cannot be empty', NULL, 1),
(172, 'en_US', 'piPending', 'zip', 'constraint', 'NotEmpty', NULL, 1),
(173, 'en_US', 'piPending', 'zip', 'metaposition', '80', NULL, 1),
(174, 'en_US', 'piPending', 'phone', 'label', 'Phone', NULL, 1),
(175, 'en_US', 'piPending', 'phone', 'error', 'Phone cannot be empty', NULL, 1),
(176, 'en_US', 'piPending', 'phone', 'constraint', 'NotEmpty', NULL, 1),
(177, 'en_US', 'piPending', 'phone', 'metaposition', '90', NULL, 1),
(178, 'en_US', 'piPending', 'fax', 'label', 'Fax', NULL, 1),
(179, 'en_US', 'piPending', 'fax', 'metaposition', '100', NULL, 1),
(180, 'en_US', 'lab', 'name', 'label', 'Lab Name', NULL, 1),
(181, 'en_US', 'lab', 'name', 'error', 'Lab Name cannot be empty', NULL, 1),
(182, 'en_US', 'lab', 'primaryUserId', 'label', 'Primary User', NULL, 1),
(183, 'en_US', 'lab', 'primaryUserId', 'error', 'Please select Primary User', NULL, 1),
(184, 'en_US', 'lab', 'departmentId', 'label', 'Department', NULL, 1),
(185, 'en_US', 'lab', 'departmentId', 'error', 'Please select department', NULL, 1),
(186, 'en_US', 'lab', 'isActive', 'label', 'Active', NULL, 1),
(187, 'en_US', 'lab', 'internal_external_lab', 'label', 'External/Internal', NULL, 1),
(188, 'en_US', 'lab', 'internal_external_lab', 'error', 'Please specify lab type (External/Internal)', NULL, 1),
(189, 'en_US', 'lab', 'internal_external_lab', 'control', 'select:external:External;internal:Internal', NULL, 1),
(190, 'en_US', 'lab', 'internal_external_lab', 'constraint', 'NotEmpty', NULL, 1),
(191, 'en_US', 'lab', 'internal_external_lab', 'metaposition', '10', NULL, 1),
(192, 'en_US', 'lab', 'phone', 'label', 'Phone', NULL, 1),
(193, 'en_US', 'lab', 'phone', 'error', 'Phone cannot be empty', NULL, 1),
(194, 'en_US', 'lab', 'phone', 'constraint', 'NotEmpty', NULL, 1),
(195, 'en_US', 'lab', 'phone', 'metaposition', '20', NULL, 1),
(196, 'en_US', 'lab', 'building_room', 'label', 'Room', NULL, 1),
(197, 'en_US', 'lab', 'building_room', 'metaposition', '30', NULL, 1),
(198, 'en_US', 'lab', 'billing_billing_dept_name', 'label', 'Billing Dept. Name', NULL, 1),
(199, 'en_US', 'lab', 'billing_billing_dept_name', 'metaposition', '40', NULL, 1),
(200, 'en_US', 'lab', 'billing_contact_name', 'label', 'Billing Contact Name', NULL, 1),
(201, 'en_US', 'lab', 'billing_contact_name', 'metaposition', '50', NULL, 1),
(202, 'en_US', 'lab', 'billing_institution_name', 'label', 'Billing Inst. Name', NULL, 1),
(203, 'en_US', 'lab', 'billing_institution_name', 'metaposition', '60', NULL, 1),
(204, 'en_US', 'lab', 'billing_building_room', 'label', 'Billing Bld. Room', NULL, 1),
(205, 'en_US', 'lab', 'billing_building_room', 'metaposition', '70', NULL, 1),
(206, 'en_US', 'lab', 'billing_address', 'label', 'Billing Address', NULL, 1),
(207, 'en_US', 'lab', 'billing_address', 'metaposition', '80', NULL, 1),
(208, 'en_US', 'lab', 'billing_city', 'label', 'Billing City', NULL, 1),
(209, 'en_US', 'lab', 'billing_city', 'metaposition', '90', NULL, 1),
(210, 'en_US', 'lab', 'billing_state', 'label', 'Billing State', NULL, 1),
(211, 'en_US', 'lab', 'billing_state', 'control', 'select:${states}:code:name', NULL, 1),
(212, 'en_US', 'lab', 'billing_state', 'metaposition', '100', NULL, 1),
(213, 'en_US', 'lab', 'billing_country', 'label', 'Billing Country', NULL, 1),
(214, 'en_US', 'lab', 'billing_country', 'control', 'select:${countries}:code:name', NULL, 1),
(215, 'en_US', 'lab', 'billing_country', 'metaposition', '105', NULL, 1),
(216, 'en_US', 'lab', 'billing_zip', 'label', 'Billing Zip', NULL, 1),
(217, 'en_US', 'lab', 'billing_zip', 'metaposition', '110', NULL, 1),
(218, 'en_US', 'lab', 'billing_email', 'label', 'Billing Email', NULL, 1),
(219, 'en_US', 'lab', 'billing_email', 'metaposition', '120', NULL, 1),
(220, 'en_US', 'lab', 'billing_phone', 'label', 'Billing Phone', NULL, 1),
(221, 'en_US', 'lab', 'billing_phone', 'metaposition', '130', NULL, 1),
(222, 'en_US', 'lab', 'research_center_name', 'label', 'RS Name', NULL, 1),
(223, 'en_US', 'lab', 'research_center_name', 'metaposition', '140', NULL, 1),
(224, 'en_US', 'lab', 'researchcenter_user_list_location', 'label', 'RS User URL', NULL, 1),
(225, 'en_US', 'lab', 'researchcenter_user_list_location', 'metaposition', '150', NULL, 1),
(226, 'en_US', 'lab', 'updated_success', 'label', 'Lab was updated', NULL, 1),
(227, 'en_US', 'lab', 'created_success', 'label', 'Lab was created', NULL, 1),
(228, 'en_US', 'lab', 'updated', 'error', 'Lab was NOT updated. Please fill in required fields.', NULL, 1),
(229, 'en_US', 'lab', 'created', 'error', 'Lab was NOT created. Please fill in required fields.', NULL, 1),
(230, 'en_US', 'user', 'updated_success', 'label', 'User was updated', NULL, 1),
(231, 'en_US', 'user', 'created_success', 'label', 'User was created', NULL, 1),
(232, 'en_US', 'user', 'updated', 'error', 'User was NOT updated. Please fill in required fields.', NULL, 1),
(233, 'en_US', 'user', 'created', 'error', 'User was NOT created. Please fill in required fields.', NULL, 1),
(234, 'en_US', 'labuser', 'request_title', 'label', 'Request Access To Lab', NULL, 1),
(235, 'en_US', 'labuser', 'request_primaryuser', 'label', 'Primary User', NULL, 1),
(236, 'en_US', 'labuser', 'request_primaryuser', 'error', 'Invalid Primary User', NULL, 1),
(237, 'en_US', 'labuser', 'request_submit', 'label', 'Request Access', NULL, 1),
(238, 'en_US', 'labuser', 'request_success', 'label', 'Your request for access has been submitted.', NULL, 1),
(239, 'en_US', 'labuser', 'request_alreadypending', 'error', 'Could not request for access to that Lab.', NULL, 1),
(240, 'en_US', 'labuser', 'request_alreadyaccess', 'error', 'Could not request for access to that Lab.', NULL, 1),
(241, 'en_US', 'labPending', 'name', 'label', 'Lab Name', NULL, 1),
(242, 'en_US', 'labPending', 'name', 'error', 'Lab Name cannot be empty', NULL, 1),
(243, 'en_US', 'labPending', 'primaryUserId', 'label', 'Primary User', NULL, 1),
(244, 'en_US', 'labPending', 'primaryUserId', 'error', 'Please select Primary User', NULL, 1),
(245, 'en_US', 'labPending', 'departmentId', 'label', 'Department', '2011-09-15 12:10:34', 133),
(246, 'en_US', 'labPending', 'departmentId', 'error', 'Please select department', NULL, 1),
(247, 'en_US', 'labPending', 'internal_external_lab', 'label', 'External/Internal', NULL, 1),
(248, 'en_US', 'labPending', 'internal_external_lab', 'error', 'Please specify lab type (External/Internal)', NULL, 1),
(249, 'en_US', 'labPending', 'internal_external_lab', 'control', 'select:external:External;internal:Internal', NULL, 1),
(250, 'en_US', 'labPending', 'internal_external_lab', 'constraint', 'NotEmpty', NULL, 1),
(251, 'en_US', 'labPending', 'internal_external_lab', 'metaposition', '10', NULL, 1),
(252, 'en_US', 'labPending', 'phone', 'label', 'Phone', NULL, 1),
(253, 'en_US', 'labPending', 'phone', 'error', 'Phone cannot be empty', NULL, 1),
(254, 'en_US', 'labPending', 'phone', 'constraint', 'NotEmpty', NULL, 1),
(255, 'en_US', 'labPending', 'phone', 'metaposition', '20', NULL, 1),
(256, 'en_US', 'labPending', 'building_room', 'label', 'Room', NULL, 1),
(257, 'en_US', 'labPending', 'building_room', 'metaposition', '30', NULL, 1),
(258, 'en_US', 'labPending', 'billing_billing_dept_name', 'label', 'Billing Dept. Name', NULL, 1),
(259, 'en_US', 'labPending', 'billing_billing_dept_name', 'metaposition', '40', NULL, 1),
(260, 'en_US', 'labPending', 'billing_contact_name', 'label', 'Billing Contact Name', NULL, 1),
(261, 'en_US', 'labPending', 'billing_contact_name', 'metaposition', '50', NULL, 1),
(262, 'en_US', 'labPending', 'billing_institution_name', 'label', 'Billing Inst. Name', NULL, 1),
(263, 'en_US', 'labPending', 'billing_institution_name', 'metaposition', '60', NULL, 1),
(264, 'en_US', 'labPending', 'billing_building_room', 'label', 'Billing Bld. Room', NULL, 1),
(265, 'en_US', 'labPending', 'billing_building_room', 'metaposition', '70', NULL, 1),
(266, 'en_US', 'labPending', 'billing_address', 'label', 'Billing Address', NULL, 1),
(267, 'en_US', 'labPending', 'billing_address', 'metaposition', '80', NULL, 1),
(268, 'en_US', 'labPending', 'billing_city', 'label', 'Billing City', NULL, 1),
(269, 'en_US', 'labPending', 'billing_city', 'metaposition', '90', NULL, 1),
(270, 'en_US', 'labPending', 'billing_state', 'label', 'Billing State', NULL, 1),
(271, 'en_US', 'labPending', 'billing_state', 'control', 'select:${states}:code:name', NULL, 1),
(272, 'en_US', 'labPending', 'billing_state', 'metaposition', '100', NULL, 1),
(273, 'en_US', 'labPending', 'billing_country', 'label', 'Billing Country', NULL, 1),
(274, 'en_US', 'labPending', 'billing_country', 'control', 'select:${countries}:code:name', NULL, 1),
(275, 'en_US', 'labPending', 'billing_country', 'metaposition', '105', NULL, 1),
(276, 'en_US', 'labPending', 'billing_zip', 'label', 'Billing Zip', NULL, 1),
(277, 'en_US', 'labPending', 'billing_zip', 'metaposition', '110', NULL, 1),
(278, 'en_US', 'labPending', 'billing_email', 'label', 'Billing Email', NULL, 1),
(279, 'en_US', 'labPending', 'billing_email', 'metaposition', '120', NULL, 1),
(280, 'en_US', 'labPending', 'billing_phone', 'label', 'Billing Phone', NULL, 1),
(281, 'en_US', 'labPending', 'billing_phone', 'metaposition', '130', NULL, 1),
(282, 'en_US', 'labPending', 'research_center_name', 'label', 'RS Name', NULL, 1),
(283, 'en_US', 'labPending', 'research_center_name', 'metaposition', '130', NULL, 1),
(284, 'en_US', 'labPending', 'researchcenter_user_list_location', 'label', 'RS User URL', NULL, 1),
(285, 'en_US', 'labPending', 'researchcenter_user_list_location', 'metaposition', '140', NULL, 1),
(286, 'en_US', 'auth', 'login_title', 'label', 'Login Page', NULL, 1),
(287, 'en_US', 'user', 'auth_login_validate', 'error', 'Please provide your user login name AND password.', NULL, 1),
(288, 'en_US', 'auth', 'login_failed', 'error', 'Your login attempt was not successful. Try again.', NULL, 1),
(289, 'en_US', 'auth', 'login_reason', 'label', 'Reason', NULL, 1),
(290, 'en_US', 'auth', 'login_user', 'label', 'User', NULL, 1),
(291, 'en_US', 'auth', 'login_password', 'label', 'Password', NULL, 1),
(292, 'en_US', 'auth', 'login', 'data', 'Login', NULL, 1),
(293, 'en_US', 'auth', 'login_anchor_forgotpass', 'label', 'Forgot Password', NULL, 1),
(294, 'en_US', 'auth', 'login_anchor_newuser', 'label', 'New User', NULL, 1),
(295, 'en_US', 'auth', 'login_anchor_newpi', 'label', 'New PI', NULL, 1),
(296, 'en_US', 'auth', 'login_anchor_about', 'label', 'About', NULL, 1),
(297, 'en_US', 'auth', 'forgotpassword_title', 'label', 'Reset Password Request', NULL, 1),
(298, 'en_US', 'auth', 'forgotpassword_user', 'label', 'Username', NULL, 1),
(299, 'en_US', 'auth', 'forgotpassword', 'data', 'Submit', NULL, 1),
(300, 'en_US', 'auth', 'forgotpassword_missingparam', 'error', 'Please provide values for all fields', NULL, 1),
(301, 'en_US', 'auth', 'forgotpassword_captcha', 'error', 'Captcha text incorrect', NULL, 1),
(302, 'en_US', 'auth', 'forgotpassword_username', 'error', 'A user with the supplied username does not exist', NULL, 1),
(303, 'en_US', 'auth', 'forgotpassword_captcha', 'label', 'Captcha text', NULL, 1),
(304, 'en_US', 'auth', 'resetpassword_title1', 'label', 'Reset Password: Step 1', NULL, 1),
(305, 'en_US', 'auth', 'resetpassword_title2', 'label', 'Reset Password: Step 2', NULL, 1),
(306, 'en_US', 'auth', 'resetpassword_user', 'label', 'Username', NULL, 1),
(307, 'en_US', 'auth', 'resetpassword_authcode', 'label', 'Auth Code', NULL, 1),
(308, 'en_US', 'auth', 'resetpassword_password1', 'label', 'New Password', NULL, 1),
(309, 'en_US', 'auth', 'resetpassword_password2', 'label', 'Confirm New Password', NULL, 1),
(310, 'en_US', 'auth', 'resetpassword_captcha', 'label', 'Captcha text', NULL, 1),
(311, 'en_US', 'auth', 'resetpassword', 'data', 'Submit', NULL, 1),
(312, 'en_US', 'auth', 'resetpassword_noauthcode', 'error', 'No authorization code provided', NULL, 1),
(313, 'en_US', 'auth', 'resetpassword_missingparam', 'error', 'Please provide values for all fields', NULL, 1),
(314, 'en_US', 'auth', 'resetpassword_badauthcode', 'error', 'Invalid authorization code provided', NULL, 1),
(315, 'en_US', 'auth', 'resetpassword_captcha', 'error', 'Captcha text incorrect', NULL, 1),
(316, 'en_US', 'auth', 'resetpassword_username', 'error', 'A user with the supplied username does not exist', NULL, 1),
(317, 'en_US', 'auth', 'resetpassword_wronguser', 'error', 'Username and authorization code provided do not match', NULL, 1),
(318, 'en_US', 'auth', 'resetpassword_new_mismatch', 'error', 'The two entries for your NEW password are NOT identical', NULL, 1),
(319, 'en_US', 'auth', 'resetpassword_new_invalid', 'error', 'New password must be at least 8 characters, containing only letters and numbers, with at least one letter and number', NULL, 1),
(320, 'en_US', 'auth', 'resetpassword_instructions', 'label', 'New Password Requirements:<br />At least 8 characters in length<br />Only letters & numbers permitted (no spaces, etc)<br />At least one letter and one number<br />', NULL, 1),
(321, 'en_US', 'auth', 'resetpasswordemailsent_title', 'label', 'Reset Password Request: Email Sent', NULL, 1),
(322, 'en_US', 'auth', 'resetpasswordemailsent', 'data', 'An email has been sent to your registered email address containing an authorization code. Please click the link within this email or alternatively <a href', NULL, 1),
(323, 'en_US', 'auth', 'resetpasswordok_title', 'label', 'Reset Password: Complete', NULL, 1),
(324, 'en_US', 'auth', 'resetpasswordok', 'label', 'Your password has been reset. Click <a href<="" td=""></a>', '2011-09-16 14:25:06', 133),
(325, 'en_US', 'user', 'mypassword_title', 'label', 'Change Password', NULL, 1),
(326, 'en_US', 'user', 'mypassword_instructions', 'label', 'New Password Requirements:<br />At least 8 characters in length<br />Only letters & numbers permitted (no spaces, etc)<br />At least one letter and one number<br />', NULL, 1),
(327, 'en_US', 'user', 'mypassword_oldpassword', 'label', 'Old Password', NULL, 1),
(328, 'en_US', 'user', 'mypassword_newpassword1', 'label', 'New Password', NULL, 1),
(329, 'en_US', 'user', 'mypassword_newpassword2', 'label', 'Confirm New Password', NULL, 1),
(330, 'en_US', 'user', 'mypassword', 'data', 'Submit', NULL, 1),
(331, 'en_US', 'user', 'mypassword_missingparam', 'error', 'Please provide values for all fields', NULL, 1),
(332, 'en_US', 'user', 'mypassword_cur_mismatch', 'error', 'Your old password does NOT match the password in our database', NULL, 1),
(333, 'en_US', 'user', 'mypassword_new_mismatch', 'error', 'The two entries for your NEW password are NOT identical', NULL, 1),
(334, 'en_US', 'user', 'mypassword_nodiff', 'error', 'Your old and new passwords may NOT be the same', NULL, 1),
(335, 'en_US', 'user', 'mypassword_new_invalid', 'error', 'New password must be at least 8 characters, containing only letters and numbers, with at least one letter and number', NULL, 1),
(336, 'en_US', 'user', 'mypassword_ok', 'label', 'Password Has Been Changed', NULL, 1),
(337, 'en_US', 'auth', 'confirmemail_badauthcode', 'error', 'Invalid authorization code provided', NULL, 1),
(338, 'en_US', 'auth', 'confirmemail_corruptemail', 'error', 'email address in url cannot be decoded', NULL, 1),
(339, 'en_US', 'auth', 'confirmemail_bademail', 'error', 'email address is incorrect', NULL, 1),
(340, 'en_US', 'auth', 'confirmemail_wronguser', 'error', 'User email address and authorization code provided do not match', NULL, 1),
(341, 'en_US', 'auth', 'confirmemail_captcha', 'error', 'Captcha text incorrect', NULL, 1),
(342, 'en_US', 'auth', 'confirmemail_title', 'label', 'Confirm Email Address', NULL, 1),
(343, 'en_US', 'auth', 'confirmemail_email', 'label', 'Email Address', NULL, 1),
(344, 'en_US', 'auth', 'confirmemail_authcode', 'label', 'Auth code', NULL, 1),
(345, 'en_US', 'auth', 'confirmemail_captcha', 'label', 'Captcha text', NULL, 1),
(346, 'en_US', 'auth', 'confirmemail', 'data', 'Submit', NULL, 1),
(347, 'en_US', 'auth', 'confirmemailok_title', 'label', 'Email Address Confirmed', NULL, 1),
(348, 'en_US', 'auth', 'confirmemailok', 'data', 'Thank you for confirming your email address.', NULL, 1),
(349, 'en_US', 'department', 'list_title', 'label', 'Department List', NULL, 1),
(350, 'en_US', 'department', 'list_create', 'label', 'Create Department', NULL, 1),
(351, 'en_US', 'department', 'list_department', 'label', 'Department Name', NULL, 1),
(352, 'en_US', 'department', 'list', 'data', 'Submit', NULL, 1),
(353, 'en_US', 'department', 'list_missingparam', 'error', 'Please provide a department name', NULL, 1),
(354, 'en_US', 'department', 'list_department_exists', 'error', 'Department already exists', NULL, 1),
(355, 'en_US', 'department', 'list_ok', 'label', 'New department has been created', NULL, 1),
(356, 'en_US', 'jobDraft', 'name', 'label', 'Job Name', NULL, 1),
(357, 'en_US', 'jobDraft', 'name', 'error', 'Job Name Must Not Be Empty', NULL, 1),
(358, 'en_US', 'jobDraft', 'labId', 'label', 'Lab', NULL, 1),
(359, 'en_US', 'jobDraft', 'labId', 'error', 'Lab Must Not Be Empty', NULL, 1),
(360, 'en_US', 'jobDraft', 'workflowId', 'label', 'Workflow', NULL, 1),
(361, 'en_US', 'jobDraft', 'workflowId', 'error', 'Workflow Must Not Be Empty', NULL, 1),
(362, 'en_US', 'ampliconSeq', 'workflow', 'label', 'LABEL:ampliconSeq', NULL, 1),
(363, 'en_US', 'cgh', 'workflow', 'label', 'LABEL:cgh', NULL, 1),
(364, 'en_US', 'chipSeq', 'workflow', 'label', 'LABEL:chipSeq', NULL, 1),
(365, 'en_US', 'controlMicroarray', 'workflow', 'label', 'LABEL:controlMicroarray', NULL, 1),
(366, 'en_US', 'controlSeq', 'workflow', 'label', 'LABEL:controlSeq', NULL, 1),
(367, 'en_US', 'deNovoSeq', 'workflow', 'label', 'LABEL:deNovoSeq', NULL, 1),
(368, 'en_US', 'digitalExpressionProfiling', 'workflow', 'label', 'LABEL:digitalExpressionProfiling', NULL, 1),
(369, 'en_US', 'directionalRnaSeq', 'workflow', 'label', 'LABEL:directionalRnaSeq', NULL, 1),
(370, 'en_US', 'geneExpressionSeq', 'workflow', 'label', 'LABEL:geneExpressionSeq', NULL, 1),
(371, 'en_US', 'helpTag', 'workflow', 'label', 'LABEL:helpTag', NULL, 1),
(372, 'en_US', 'matePairSeq', 'workflow', 'label', 'LABEL:matePairSeq', NULL, 1),
(373, 'en_US', 'microarrayChip', 'workflow', 'label', 'LABEL:microarrayChip', NULL, 1),
(374, 'en_US', 'microarrayGeneExpression', 'workflow', 'label', 'LABEL:microarrayGeneExpression', NULL, 1),
(375, 'en_US', 'microarrayHelp', 'workflow', 'label', 'LABEL:microarrayHelp', NULL, 1),
(376, 'en_US', 'mirnaSeq', 'workflow', 'label', 'LABEL:mirnaSeq', NULL, 1),
(377, 'en_US', 'otherSeqDnaSamples', 'workflow', 'label', 'LABEL:otherSeqDnaSamples', NULL, 1),
(378, 'en_US', 'otherSeqRnaSamples', 'workflow', 'label', 'LABEL:otherSeqRnaSamples', NULL, 1),
(379, 'en_US', 'resequencing', 'workflow', 'label', 'LABEL:resequencing', NULL, 1),
(380, 'en_US', 'rnaSeq', 'workflow', 'label', 'LABEL:rnaSeq', NULL, 1),
(381, 'en_US', 'seqCap', 'workflow', 'label', 'LABEL:seqCap', NULL, 1),
(383, 'en_US', 'chipSeq', 'samples', 'data', 'samplefile;chipseqSample', NULL, 1),
(384, 'en_US', 'chipSeq', 'platform', 'label', 'Platform', NULL, 1),
(385, 'en_US', 'chipSeq', 'platform', 'constraint', 'NotEmpty', NULL, 1),
(386, 'en_US', 'chipSeq', 'platform', 'error', 'Platform cannot be empty', NULL, 1),
(387, 'en_US', 'chipSeq', 'platform', 'control', 'select:HISeq2000:HISeq2000', NULL, 1),
(388, 'en_US', 'chipSeq', 'platform', 'metaposition', '100', NULL, 1),
(389, 'en_US', 'chipSeq', 'readlength', 'label', 'Read Length', NULL, 1),
(390, 'en_US', 'chipSeq', 'readlength', 'constraint', 'NotEmpty', NULL, 1),
(391, 'en_US', 'chipSeq', 'readlength', 'error', 'readlength cannot be empty', NULL, 1),
(392, 'en_US', 'chipSeq', 'readlength', 'control', 'select:100:100 bp; 150: 150bp', NULL, 1),
(393, 'en_US', 'chipSeq', 'readlength', 'metaposition', '110', NULL, 1),
(394, 'en_US', 'chipSeq', 'readtype', 'label', 'Read Type', NULL, 1),
(395, 'en_US', 'chipSeq', 'readtype', 'constraint', 'NotEmpty', NULL, 1),
(396, 'en_US', 'chipSeq', 'readtype', 'error', 'readtype cannot be empty', NULL, 1),
(397, 'en_US', 'chipSeq', 'readtype', 'control', 'select:single:Single-End Read; pair:Pair-End Read', NULL, 1),
(398, 'en_US', 'chipSeq', 'readtype', 'metaposition', '120', NULL, 1),
(399, 'en_US', 'chipSeq', 'antibody', 'label', 'Anti Body', NULL, 1),
(400, 'en_US', 'chipSeq', 'antibody', 'constraint', 'NotEmpty', NULL, 1),
(401, 'en_US', 'chipSeq', 'antibody', 'error', 'antibody cannot be empty', NULL, 1),
(402, 'en_US', 'chipSeq', 'antibody', 'control', 'select:abc:abc;def:def;ghi:ghi', NULL, 1),
(403, 'en_US', 'chipSeq', 'antibody', 'metaposition', '130', NULL, 1),
(404, 'en_US', 'chipSeq', 'pcrprimers', 'label', 'PCR Primers', NULL, 1),
(405, 'en_US', 'chipSeq', 'pcrprimers', 'constraint', 'NotEmpty', NULL, 1),
(406, 'en_US', 'chipSeq', 'pcrprimers', 'error', 'pcrprimers cannot be empty', NULL, 1),
(407, 'en_US', 'chipSeq', 'pcrprimers', 'control', 'select:xyz:xyz', NULL, 1),
(408, 'en_US', 'chipSeq', 'pcrprimers', 'metaposition', '140', NULL, 1),
(410, 'en_US', 'ampliconSeq', 'samples', 'data', 'samplefile;ampliconSample', NULL, 1),
(411, 'en_US', 'ampliconSeq', 'platform', 'label', 'Platform', NULL, 1),
(412, 'en_US', 'ampliconSeq', 'platform', 'constraint', 'NotEmpty', NULL, 1),
(413, 'en_US', 'ampliconSeq', 'platform', 'error', 'Platform cannot be empty', NULL, 1),
(414, 'en_US', 'ampliconSeq', 'platform', 'control', 'select:HISeq2000:HISeq2000', NULL, 1),
(415, 'en_US', 'ampliconSeq', 'platform', 'metaposition', '100', NULL, 1),
(416, 'en_US', 'ampliconSeq', 'readlength', 'label', 'Read Length', NULL, 1),
(417, 'en_US', 'ampliconSeq', 'readlength', 'constraint', 'NotEmpty', NULL, 1),
(418, 'en_US', 'ampliconSeq', 'readlength', 'error', 'readlength cannot be empty', NULL, 1),
(419, 'en_US', 'ampliconSeq', 'readlength', 'control', 'select:100:100 bp; 150: 150bp', NULL, 1),
(420, 'en_US', 'ampliconSeq', 'readlength', 'metaposition', '110', NULL, 1),
(421, 'en_US', 'ampliconSeq', 'readtype', 'label', 'Read Type', NULL, 1),
(422, 'en_US', 'ampliconSeq', 'readtype', 'constraint', 'NotEmpty', NULL, 1),
(423, 'en_US', 'ampliconSeq', 'readtype', 'error', 'readtype cannot be empty', NULL, 1),
(424, 'en_US', 'ampliconSeq', 'readtype', 'control', 'select:single:Single-End Read; pair:Pair-End Read', NULL, 1),
(425, 'en_US', 'ampliconSeq', 'readtype', 'metaposition', '120', NULL, 1),
(426, 'en_US', 'ampliconSeq', 'antibody', 'label', 'Anti Body', NULL, 1),
(427, 'en_US', 'ampliconSeq', 'antibody', 'constraint', 'NotEmpty', NULL, 1),
(428, 'en_US', 'ampliconSeq', 'antibody', 'error', 'antibody cannot be empty', NULL, 1),
(429, 'en_US', 'ampliconSeq', 'antibody', 'control', 'select:abc:abc;def:def;ghi:ghi', NULL, 1),
(430, 'en_US', 'ampliconSeq', 'antibody', 'metaposition', '130', NULL, 1),
(431, 'en_US', 'ampliconSeq', 'pcrprimers', 'label', 'PCR Primers', NULL, 1),
(432, 'en_US', 'ampliconSeq', 'pcrprimers', 'constraint', 'NotEmpty', NULL, 1),
(433, 'en_US', 'ampliconSeq', 'pcrprimers', 'error', 'pcrprimers cannot be empty', NULL, 1),
(434, 'en_US', 'ampliconSeq', 'pcrprimers', 'control', 'select:xyz:xyz', NULL, 1),
(435, 'en_US', 'ampliconSeq', 'pcrprimers', 'metaposition', '140', NULL, 1),
(436, 'en_US', 'sampleDraft', 'name', 'label', 'Name', NULL, 1),
(437, 'en_US', 'sampleDraft', 'name', 'constraint', 'NotEmpty', '2011-09-15 12:08:40', 133),
(438, 'en_US', 'sampleDraft', 'name', 'error', 'Please specify sample name', NULL, 1),
(439, 'en_US', 'sampleDraft', 'sampleTypeId', 'label', 'Type', NULL, 1),
(440, 'en_US', 'sampleDraft', 'sampleTypeId', 'constraint', 'NotEmpty', NULL, 1),
(441, 'en_US', 'sampleDraft', 'sampleTypeId', 'error', 'Please specify type', NULL, 1),
(442, 'en_US', 'sampleDraft', 'status', 'label', 'Status', NULL, 1),
(443, 'en_US', 'sampleDraft', 'status', 'constraint', 'NotEmpty', NULL, 1),
(444, 'en_US', 'sampleDraft', 'status', 'error', 'Please specify status', NULL, 1),
(445, 'en_US', 'sampleDraft', 'fileData', 'label', 'Sample File', NULL, 1),
(446, 'en_US', 'sampleDraft', 'fileData', 'suffix', '(10Mb Max)', NULL, 1),
(447, 'en_US', 'sampleDraft', 'created', 'data', 'Sample Created.', '2011-09-15 11:55:09', 133),
(448, 'en_US', 'sampleDraft', 'updated', 'data', 'Sample Updated.', '2011-09-15 11:55:18', 133),
(449, 'en_US', 'sampleDraft', 'removed', 'data', 'Sample Removed.', '2011-09-15 11:54:26', 133),
(450, 'en_US', 'sampleDraft', 'fileupload_wait', 'data', 'File Upload Started. Please Wait ...', '2011-09-15 12:02:46', 133),
(451, 'en_US', 'sampleDraft', 'fileupload_done', 'data', 'File Upload Done.', '2011-09-15 12:02:40', 133),
(452, 'en_US', 'sampleDraft', 'fileupload_nofile', 'data', 'No File Selected.', '2011-09-15 12:02:34', 133),
(453, 'en_US', 'sampleDraft', 'material_provided', 'label', 'material_provided', NULL, 1),
(454, 'en_US', 'sampleDraft', 'reference_genome_id', 'label', 'reference_genome_id', NULL, 1),
(455, 'en_US', 'sampleDraft', 'species_id', 'label', 'species_id', NULL, 1),
(456, 'en_US', 'sampleDraft', 'fragment_size', 'label', 'fragment_size', NULL, 1),
(457, 'en_US', 'sampleDraft', 'amount', 'label', 'amount', NULL, 1),
(458, 'en_US', 'sampleDraft', 'concentration', 'label', 'concentration', NULL, 1),
(459, 'en_US', 'sampleDraft', '260_280', 'label', '260_280', NULL, 1),
(460, 'en_US', 'sampleDraft', '260_230', 'label', '260_230', NULL, 1),
(461, 'en_US', 'sampleDraft', 'volume', 'label', 'volume', NULL, 1),
(462, 'en_US', 'sampleDraft', 'buffer', 'label', 'buffer', NULL, 1),
(463, 'en_US', 'sampleDraft', 'sample_type', 'label', 'sample_type', NULL, 1),
(464, 'en_US', 'sampleDraft', 'antibody_id', 'label', 'antibody_id', NULL, 1),
(465, 'en_US', 'sampleDraft', 'enrich_primer_pair_id', 'label', 'enrich_primer_pair_id', NULL, 1),
(466, 'en_US', 'sampleDraft', 'material_provided', 'metaposition', '10', NULL, 1),
(467, 'en_US', 'sampleDraft', 'reference_genome_id', 'metaposition', '20', NULL, 1),
(468, 'en_US', 'sampleDraft', 'species_id', 'metaposition', '30', NULL, 1),
(469, 'en_US', 'sampleDraft', 'fragment_size', 'metaposition', '40', NULL, 1),
(470, 'en_US', 'sampleDraft', 'amount', 'metaposition', '50', NULL, 1),
(471, 'en_US', 'sampleDraft', 'concentration', 'metaposition', '60', NULL, 1),
(472, 'en_US', 'sampleDraft', '260_280', 'metaposition', '70', NULL, 1),
(473, 'en_US', 'sampleDraft', '260_230', 'metaposition', '80', NULL, 1),
(474, 'en_US', 'sampleDraft', 'volume', 'metaposition', '90', NULL, 1),
(475, 'en_US', 'sampleDraft', 'buffer', 'metaposition', '100', NULL, 1),
(476, 'en_US', 'sampleDraft', 'sample_type', 'metaposition', '110', NULL, 1),
(477, 'en_US', 'sampleDraft', 'antibody_id', 'metaposition', '120', NULL, 1),
(478, 'en_US', 'sampleDraft', 'enrich_primer_pair_id', 'metaposition', '130', NULL, 1),
(479, 'en_US', 'hello', 'error=Hello World', '', '', NULL, 1),
(480, 'en_US', 'sample', 'name', 'label', 'Name', NULL, 1),
(481, 'en_US', 'sample', 'name', 'error', 'Name cannot be null', NULL, 1),
(482, 'en_US', 'platformunit', 'barcode', 'label', 'Barcode', NULL, 1),
(483, 'en_US', 'platformunit', 'barcode', 'constraint', 'NotEmpty', NULL, 1),
(484, 'en_US', 'platformunit', 'barcode', 'error', 'Barcode cannot be empty', NULL, 1),
(485, 'en_US', 'platformunit', 'barcode', 'metaposition', '10', NULL, 1),
(486, 'en_US', 'platformunit', 'comment', 'label', 'Comment', NULL, 1),
(487, 'en_US', 'platformunit', 'comment', 'error', 'Comment', NULL, 1),
(488, 'en_US', 'platformunit', 'comment', 'metaposition', '20', NULL, 1),
(489, 'en_US', 'platformunit', 'version', 'label', 'Version', NULL, 1),
(490, 'en_US', 'platformunit', 'version', 'error', 'VErsioN', NULL, 1),
(491, 'en_US', 'platformunit', 'version', 'metaposition', '30', NULL, 1),
(492, 'en_US', 'platformunit', 'lanecount', 'label', 'Lane Count', NULL, 1),
(493, 'en_US', 'platformunit', 'lanecount', 'constraint', 'NotEmpty', NULL, 1),
(494, 'en_US', 'platformunit', 'lanecount', 'error', 'Lane Count cannot be empty', NULL, 1),
(495, 'en_US', 'platformunit', 'lanecount', 'control', 'select:1:1;8:8', NULL, 1),
(496, 'en_US', 'platformunit', 'lanecount', 'metaposition', '40', NULL, 1),
(497, 'en_US', 'fmpayment', 'amount', 'label', 'Amount', NULL, 1),
(498, 'en_US', 'fmpayment', 'amount', 'constraint', 'NotEmpty', NULL, 1),
(499, 'en_US', 'fmpayment', 'amount', 'metaposition', '10', NULL, 1),
(500, 'en_US', 'fmpayment', 'amount', 'error', 'AmounT cannot be Empty', NULL, 1),
(501, 'en_US', 'fmpayment', 'comment', 'label', 'Comment', NULL, 1),
(502, 'en_US', 'fmpayment', 'comment', 'constraint', 'NotEmpty', NULL, 1),
(503, 'en_US', 'fmpayment', 'comment', 'metaposition', '20', NULL, 1),
(504, 'en_US', 'fmpayment', 'comment', 'error', 'Comment cannot be Empty', NULL, 1),
(505, 'en_US', 'uiField', 'updated_success', 'label', 'Field Updated', NULL, 1),
(506, 'en_US', 'uiField', 'added_success', 'label', 'Field Added', NULL, 1),
(507, 'en_US', 'uiField', 'removed', 'data', 'Field Deleted', '2011-09-15 11:54:02', 133),
(508, 'en_US', 'uiField', 'name', 'label', 'Field Name', '2011-09-15 10:02:50', 133),
(509, 'en_US', 'uiField', 'area', 'label', 'Area', NULL, 1),
(510, 'en_US', 'uiField', 'locale', 'label', 'Locale', NULL, 1),
(511, 'en_US', 'uiField', 'attrName', 'label', 'Attribute Name', '2011-09-15 11:07:13', 133),
(512, 'en_US', 'uiField', 'attrValue', 'label', 'Attribute Value', NULL, 1),
(513, 'en_US', 'uiField', 'locale', 'error', 'Locale not specified', NULL, 1),
(514, 'ru_RU', 'user', 'login', 'label', 'Р›РѕРіРёРЅ', '2011-09-15 08:47:33', 133),
(515, 'ru_RU', 'uiField', 'locale', 'label', 'РЇР·С‹Рє', '2011-09-15 09:53:07', 133),
(516, 'ru_RU', 'uiField', 'area', 'label', 'РћР±СЊРµРєС‚', '2011-09-15 09:54:11', 133),
(518, 'ru_RU', 'jobDraft', 'name', 'label', 'РќР°Р·РІР°РЅРёРµ Р Р°Р±РѕС‚С‹', '2011-09-15 10:04:44', 133),
(519, 'en_US', 'uiField', 'attrName', 'suffix', '<font color="blue"> see footnote<sup>1</sup> </font>', '2011-09-15 11:14:33', 133),
(520, 'en_US', 'auth', 'login_submit', 'label', 'Login', NULL, 1),
(521, 'en_US', 'userPending', 'submit', 'label', 'Apply for Account', '2011-09-16 14:02:59', 133),
(522, 'en_US', 'userPending', 'select_default', 'label', '-- select --', '2011-09-16 14:13:26', 133),
(523, 'en_US', 'userPending', 'page_title', 'label', 'New User', '2011-09-16 14:14:25', 133),
(524, 'en_US', 'auth', 'confirmemail_submit', 'label', 'Submit', '2011-09-16 14:15:53', 133),
(525, 'en_US', 'auth', 'confirmemailok', 'label', 'Thank you for confirming your email address.', '2011-09-16 14:16:44', 133),
(526, 'en_US', 'auth', 'resetpasswordemailsent', 'label', 'An email has been sent to your registered email address containing an authorization code. Please click the link within this email or alternatively <a href="resetpassword.do">click here</a> and enter the authorization code provided. ', '2011-09-16 14:18:03', 133),
(527, 'en_US', 'auth', 'forgotpassword_submit', 'label', 'Submit', '2011-09-16 14:19:02', 133),
(528, 'en_US', 'userPending', 'emailsent', 'label', 'Thank you for your account request. You have been sent an email with instructions as to how to confirm your email address. Your principle investigator has also been emailed to request confirmation of your eligibility to join their lab and you are advised to contact them to request they do this if your account does not become activated in good time. Please click to <a href="wasp/auth/login.do"/>Login</a>', '2011-09-16 14:20:33', 133),
(529, 'en_US', 'auth', 'resetpassword_submit', 'label', 'Submit', '2011-09-16 14:21:38', 133),
(532, 'en_US', 'department', 'detail', 'label', 'Department', '2011-09-16 14:26:26', 133),
(533, 'en_US', 'department', 'detail_missingparam', 'error', 'Email is missing', '2011-09-16 14:27:38', 133),
(534, 'en_US', 'department', 'detail_administrators', 'label', 'Administrators', '2011-09-16 14:28:14', 133),
(536, 'en_US', 'user', 'mypassword_submit', 'label', 'Submit', '2011-09-16 14:32:08', 133);

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


