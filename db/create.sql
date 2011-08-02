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
) ENGINE=InnoDB;



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
) ENGINE=InnoDB;



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
) ENGINE=InnoDB;


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
) ENGINE=InnoDB;

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
(11, 'god', 'God', 'system'),
(12, 'lx', 'Lab Member Inactive', 'lab'), -- labuser, explicit
(13, 'lx', 'Lab Member Pending', 'lab'); -- labuser, explicit


create table roleset (
  rolesetid int(10) not null primary key auto_increment, 
  parentroleid int(10) not null,
  childroleid int(10) not null,
  
  foreign key fk_roleset_prid (parentroleid) references role(roleid),
  foreign key fk_roleset_crid (childroleid) references role(roleid),

  constraint unique index u_role_rname (parentroleid, childroleid)
) ENGINE=InnoDB;

insert into roleset
(parentroleid, childroleid)
select 
roleid, roleid 
from role;

insert into roleset 
(parentroleid, childroleid)
values
(6, 7),
(6, 8),
(7, 8),
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
) ENGINE=InnoDB;


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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;



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
  foreign key fk_lab_puid (primaryuserid) references user(userid),

  constraint unique index u_lab_name (name),
  constraint unique index u_lab_puid (primaryuserid)
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;



--
-- type.RESOURCE
-- 
create table typeresource (
  typeresourceid int(10) not null primary key auto_increment, 

  iname varchar(250) not null,
  name varchar(250) not null,

  constraint unique index u_typeresource_iname (iname),
  constraint unique index u_typeresource_name (name)
) ENGINE=InnoDB; 

insert into typeresource values (1, 'dna', 'DNA Sequencer'); 
insert into typeresource values (2, 'amplicon', 'DNA Amplicon'); 

--
-- RESOURCE
-- 
create table resource (
  resourceid int(10) not null primary key auto_increment, 

  platform varchar(250) not null,   -- another table?
  name varchar(250) not null,

  typeresourceid int(10) not null,

  isactive int(1) not null default 1,
  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_resource_trid (typeresourceid) references typeresource(typeresourceid),
  constraint unique index u_resource_name (name)
) ENGINE=InnoDB;



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
) ENGINE=InnoDB;


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
  -- ) ENGINE=InnoDB;



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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;


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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;


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
) ENGINE=InnoDB;


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
) ENGINE=InnoDB;

create table resourcebarcode (
  resourcebarcodeid int(10) not null primary key auto_increment, 

  resourceid int(10) not null, 
  barcodeid int(10) not null, 

  foreign key fk_resourcebarcode_rid (resourceid) references resource(resourceid),
  foreign key fk_resourcebarcode_bcid (barcodeid) references barcode(barcodeid),

  constraint unique index u_resourcebarcode_rid (resourceid),
  constraint unique index u_resourcebarcode_bcid (barcodeid)
) ENGINE=InnoDB;

-- 
-- SAMPLE
--

create table typesample (
  typesampleid int(10) not null primary key auto_increment,
  iname varchar(250), 
  name varchar(250),

  constraint unique index u_typesample_iname (iname),
  constraint unique index u_typesample_name (name)
) ENGINE=InnoDB;

insert into typesample values
(1, 'dna', 'DNA'), 
(2, 'tissue', 'Tissue'),
(3, 'library', 'Library'),
(4, 'lane', 'Lane'), 
(5, 'flowcell', 'Cassette');


create table sample (
  sampleid int(10) not null primary key auto_increment,

  typesampleid int(10) not null,

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

  foreign key fk_sample_tsid (typesampleid) references typesample(typesampleid),
  foreign key fk_sample_sjid (submitter_jobid) references job(jobid),
  foreign key fk_sample_slid (submitter_labid) references lab(labid),
  foreign key fk_sample_suid (submitter_userid) references user(userid)
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;


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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;



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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;



--
-- ACCOUNTING TABLES
--
create table acct_workflowcost (
  workflowid int(10) not null primary key,
  basecost float(10,2) not null,

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_acct_workflowcost_wfid (workflowid) references job(workflowid)
) ENGINE=InnoDB;


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
) ENGINE=InnoDB;

create table acct_jobquotecurrent (
  jobid int(10) not null primary key,
  quoteid int(10) not null,

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,


  foreign key fk_acct_jobquotecurrent_jid (jobid) references job(jobid),
  foreign key fk_acct_jobquotecurrent_qid (quoteid) references acct_quote(quoteid)
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;


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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;

create table acct_grantjob (
  jobid int(10) not null primary key auto_increment, 
  grantid int(10) not null,

  isactive int(1) not null default 1,
  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_acct_ledgergrant_jid (jobid) references acct_ledger(jobid),
  foreign key fk_acct_ledgergrant_gid (grantid) references acct_grant(grantid)
) ENGINE=InnoDB;


--
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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;


-- ---------------------------------

-- resource stuffs

--
-- RESOURCELANE LANE
--

create table resourcelane (
  resourcelaneid int(10) not null primary key auto_increment,
  resourceid int(10) not null,
  iname varchar(250),
  name varchar(250),

  foreign key fk_resourcelane_rid (resourceid) references resource(resourceid),
  constraint unique index u_resourcelane_iname_rid (iname, resourceid),
  constraint unique index u_resourcelane_name_rid (name, resourceid)
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;

--
-- RUN.resourceLANE (LANE)
--
create table runlane (
  runlaneid int(10) not null primary key auto_increment,

  runid int(10) not null, 
  resourcelaneid int(10) not null, -- lane 
  sampleid int(10) not null, 

  foreign key fk_runlane_rid (runid) references run(runid),
  foreign key fk_runlane_lid (resourcelaneid) references resourcelane(resourcelaneid),
  foreign key fk_runlane_sid (sampleid) references sample(sampleid),

  constraint unique index u_runlane_rid_lid (runid, resourcelaneid),
  constraint unique index u_runlane_sid_rid (sampleid, runid) 
) ENGINE=InnoDB;

--
-- RESOURCE
--

create table runfile (
  runlanefileid int(10) not null primary key auto_increment,

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
) ENGINE=InnoDB;

create table runlanefile (
  runlanefileid int(10) not null primary key auto_increment,

  runlaneid int(10) not null,
  fileid int(10) not null, 
  iname varchar(2048) not null, 
  name varchar(250) not null, 

  isactive int(1) not null default 1, 
  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_rlfile_rlid (runlaneid) references runlane(runlaneid),
  foreign key fk_rlfile_fileid (fileid) references file(fileid),

  constraint unique index u_rlfile_fileid (fileid)
) ENGINE=InnoDB;



--
-- TASK
-- 
create table task (
  taskid int(10) not null primary key auto_increment,

  iname varchar(250) not null,
  name varchar(250) not null,
  constraint unique index u_task_iname (iname)
) ENGINE=InnoDB;

create table workflowtask (
  workflowtaskid int(10) not null primary key auto_increment,
  workflowid int(10) not null,
  taskid int(10) not null,

  iname varchar(250) not null, -- can be multiple task, so this differenciates
  name varchar(250) not null
) ENGINE=InnoDB;

create table workflowtasksource (
  workflowtasksourceid int(10) not null primary key auto_increment,
  workflowtaskid int(10) not null,
  sourceworkflowtaskid int(10) not null
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;

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
) ENGINE=InnoDB;


create table statejob (
  statejobid int(10) not null primary key auto_increment, 

  stateid int(10) not null, 
  jobid int(10) not null, 

  foreign key fk_statejob_sid (stateid) references state(stateid),
  foreign key fk_statejob_jid (jobid) references job(jobid)
) ENGINE=InnoDB;


create table statesample (
  statesampleid int(10) not null primary key auto_increment, 

  stateid int(10) not null, 
  sampleid int(10) not null, 

  foreign key fk_statesample_sid (stateid) references state(stateid),
  foreign key fk_statesample_sampleid (sampleid) references sample(sampleid)
) ENGINE=InnoDB;

create table staterun (
  staterunid int(10) not null primary key auto_increment, 

  stateid int(10) not null, 
  runid int(10) not null, 

  foreign key fk_staterun_sid (stateid) references state(stateid),
  foreign key fk_staterun_rid (runid) references run(runid)
) ENGINE=InnoDB;

--
-- tie illumina runs back to real tasks
--   [still unsure of this design]
create table staterunlane (
  staterunlaneid int(10) not null primary key auto_increment,
  
  stateid int(10) not null,
  runlaneid int(10) not null,

  foreign key fk_staterunlane_sid (stateid) references state(stateid),
  foreign key fk_staterunlane_rlid (runlaneid) references runlane(runlaneid)
) ENGINE=InnoDB;


-- ----------------------------------------

--
-- assay pipelines
--

create table a_chipseq_arun (
  arunid int(10) not null primary key auto_increment, 

  fileid int(10) not null,  -- pointer to qseq file

  version varchar(250) not null,

  startts datetime, 
  endts datetime, 

  status varchar(50), -- started, pending, completed, failed?

  isactive int(1) not null default 1, 
  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_acs_arun_fid (fileid) references file(fileid)
) ENGINE=InnoDB;


--
-- ASSAY CHIPSEQ ARGS
--   - track command line arguments
--

create table a_chipseq_arunargs (
  arunargsid int(10) not null primary key auto_increment, 

  arunid int(10),

  argv varchar(250) not null default 0,
  argc int(10) not null default 0,

  foreign key fk_acs_arunargs_arid (arunid) references a_chipseq_arun(arunid),
  constraint unique index u_acs_arunargs_arid_argc (arunid, argc)
) ENGINE=InnoDB;

--
-- tie chipseq runs back to real tasks
--   [still unsure of this design]
create table state_a_chipseq_arun (
  state_arunid int(10) not null primary key auto_increment,
  
  stateid int(10) not null,
  arunid int(10) not null,

  foreign key fk_state_arun_jtid (stateid) references state(stateid),
  foreign key fk_state_arun_arid (arunid) references a_chipseq_arun(arunid)
) ENGINE=InnoDB;

/*
*/
