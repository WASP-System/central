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
(11, 'god', 'God', 'system'),
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
  foreign key fk_lab_puid (primaryuserid) references user(userid),

  constraint unique index u_lab_name (name),
  constraint unique index u_lab_puid (primaryuserid)
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
  userpendingid int(10) not null primary key, 
  authcode varchar(250) not null,
  lastupdts timestamp not null default current_timestamp, 
  lastupduser int(10) not null default 0,

  foreign key fk_confirmemailauth_userpending (userpendingid) references userpending(userpendingid),
  constraint unique index u_confirmemailauth (authcode)
) ENGINE=InnoDB charset=utf8;

--
-- type.RESOURCE
-- 
create table typeresource (
  typeresourceid int(10) not null primary key auto_increment, 

  iname varchar(250) not null,
  name varchar(250) not null,

  constraint unique index u_typeresource_iname (iname),
  constraint unique index u_typeresource_name (name)
) ENGINE=InnoDB charset=utf8; 

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

-- 
-- SAMPLE
--

create table typesample (
  typesampleid int(10) not null primary key auto_increment,
  iname varchar(250), 
  name varchar(250),

  constraint unique index u_typesample_iname (iname),
  constraint unique index u_typesample_name (name)
) ENGINE=InnoDB charset=utf8;

insert into typesample values
(1, 'dna', 'DNA'), 
(2, 'tissue', 'Tissue'),
(3, 'library', 'Library'),
(4, 'lane', 'Lane'), 
(5, 'platformunit', 'Platform Unit');

create table subtypesample (
  subtypesampleid int(10) not null primary key auto_increment,

  typesampleid int(10) not null,

  iname varchar(50) not null, -- meta field prefix
  name varchar(250) not null,

  constraint unique index u_subtypesample_iname (iname),
  foreign key fk_subtypesample_tsid (typesampleid) references typesample(typesampleid)
) ENGINE=InnoDB charset=utf8;

create table workflowsubtypesample (
  workflowsubtypesampleid int(10) not null primary key auto_increment,
  workflowid int(10) not null,
  subtypesampleid int(10) not null,
 
  constraint unique index u_subtypesample_wid_stsid (workflowid, subtypesampleid),

  foreign key fk_workflowsubtypesample_stsid (subtypesampleid) references subtypesample(subtypesampleid),
  foreign key fk_workflowsubtypesample_wid (workflowid) references workflow(workflowid)
) ENGINE=InnoDB charset=utf8;

create table sample (
  sampleid int(10) not null primary key auto_increment,

  typesampleid int(10) not null,
  subtypesampleid int(10),

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
  foreign key fk_sample_stsid (subtypesampleid) references subtypesample(subtypesampleid),
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
  typesampleid int(10) not null,
  subtypesampleid int(10) not null,

  labid int(10) not null,
  userid int(10) not null,
  jobdraftid int(10) null,

  name varchar(250),
  status varchar(50), 

  lastupdts timestamp not null default current_timestamp,
  lastupduser int(10) not null default 0,

  foreign key fk_sampledraft_tsid (typesampleid) references typesample(typesampleid),
  foreign key fk_sampledraft_stsid (subtypesampleid) references subtypesample(subtypesampleid),
  foreign key fk_sampledraft_sjid (jobdraftid) references jobdraft(jobdraftid),
  foreign key fk_sampledraft_slid (labid) references lab(labid),
  foreign key fk_sampledraft_suid (userid) references user(userid)
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

create table resourcelane (
  resourcelaneid int(10) not null primary key auto_increment,
  resourceid int(10) not null,
  iname varchar(250),
  name varchar(250),

  foreign key fk_resourcelane_rid (resourceid) references resource(resourceid),
  constraint unique index u_resourcelane_iname_rid (iname, resourceid),
  constraint unique index u_resourcelane_name_rid (name, resourceid)
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
) ENGINE=InnoDB charset=utf8;

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
) ENGINE=InnoDB charset=utf8;

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
create table staterunlane (
  staterunlaneid int(10) not null primary key auto_increment,
  
  stateid int(10) not null,
  runlaneid int(10) not null,

  foreign key fk_staterunlane_sid (stateid) references state(stateid),
  foreign key fk_staterunlane_rlid (runlaneid) references runlane(runlaneid)
) ENGINE=InnoDB charset=utf8;



SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

create table uifield (
  uifieldid int(11) not null primary key auto_increment,
  locale varchar(255) not null,
  area varchar(255) not null,
  name varchar(255) not null,
  attrname varchar(255) not null,
  attrvalue varchar(255) default null,

  lastupdts datetime default null,
  lastupduser int(11) default null,

  constraint unique index u_uifield_laaa (locale, area, name, attrname)
) ENGINE=InnoDB charset=utf8;


insert into uifield (`uifieldid`, `area`, `attrname`, `attrvalue`, `lastupdts`, `lastupduser`, `locale`, `name`) values
(1, 'user', 'label', 'Login', '2011-09-15 11:52:20', 133, 'en_US', 'login'),
(2, 'user', 'error', 'Login cannot be empty', '2011-09-15 11:29:19', 133, 'en_US', 'login'),
(4, 'user', 'label', 'User Detail', '2011-09-15 10:30:52', 133, 'en_US', 'detail'),
(5, 'user', 'metaposition', 'User Details', '2011-09-15 10:30:59', 133, 'en_US', 'detail'),
(6, 'user', 'label', 'Password', NULL, 1, 'en_US', 'password'),
(7, 'user', 'error', 'Password cannot be empty', NULL, 1, 'en_US', 'password'),
(8, 'user', 'label', 'First Name', NULL, 1, 'en_US', 'firstName'),
(9, 'user', 'error', 'First Name cannot be empty', NULL, 1, 'en_US', 'firstName'),
(10, 'user', 'label', 'Last Name', NULL, 1, 'en_US', 'lastName'),
(11, 'user', 'error', 'Last Name cannot be empty', NULL, 1, 'en_US', 'lastName'),
(12, 'user', 'label', 'Email', NULL, 1, 'en_US', 'email'),
(13, 'user', 'error', 'Wrong email address format', NULL, 1, 'en_US', 'email'),
(14, 'user', 'label', 'Locale', NULL, 1, 'en_US', 'locale'),
(15, 'user', 'error', 'Locale cannot be empty', NULL, 1, 'en_US', 'locale'),
(16, 'user', 'label', 'Active', NULL, 1, 'en_US', 'isActive'),
(17, 'user', 'label', 'Title', NULL, 1, 'en_US', 'title'),
(18, 'user', 'error', 'Title cannot be empty', NULL, 1, 'en_US', 'title'),
(19, 'user', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'title'),
(20, 'user', 'control', 'select:Prof:Prof;Dr:Dr;Mr:Mr;Ms:Ms;Mrs:Mrs;Miss:Miss', NULL, 1, 'en_US', 'title'),
(21, 'user', 'metaposition', '1', NULL, 1, 'en_US', 'title'),
(22, 'user', 'label', 'Institution', NULL, 1, 'en_US', 'institution'),
(23, 'user', 'error', 'Institution cannot be empty', NULL, 1, 'en_US', 'institution'),
(24, 'user', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'institution'),
(25, 'user', 'metaposition', '10', NULL, 1, 'en_US', 'institution'),
(26, 'user', 'label', 'Department', NULL, 1, 'en_US', 'department'),
(27, 'user', 'metaposition', '20', NULL, 1, 'en_US', 'department'),
(28, 'user', 'label', 'Room', NULL, 1, 'en_US', 'building_room'),
(29, 'user', 'metaposition', '30', NULL, 1, 'en_US', 'building_room'),
(30, 'user', 'label', 'Address', NULL, 1, 'en_US', 'address'),
(31, 'user', 'error', 'Address cannot be empty', NULL, 1, 'en_US', 'address'),
(32, 'user', 'constraint', 'dscsdcsdc', '2011-09-15 11:42:10', 133, 'en_US', 'address'),
(33, 'user', 'metaposition', '40', NULL, 1, 'en_US', 'address'),
(34, 'user', 'label', 'City', NULL, 1, 'en_US', 'city'),
(35, 'user', 'error', 'City cannot be empty', NULL, 1, 'en_US', 'city'),
(36, 'user', 'constraint', 'NotEmpty', '2011-09-15 11:05:52', 133, 'en_US', 'city'),
(37, 'user', 'metaposition', '50', NULL, 1, 'en_US', 'city'),
(38, 'user', 'label', 'State', NULL, 1, 'en_US', 'state'),
(39, 'user', 'error', 'State cannot be empty', NULL, 1, 'en_US', 'state'),
(40, 'user', 'control', 'select:${states}:code:name', NULL, 1, 'en_US', 'state'),
(41, 'user', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'state'),
(42, 'user', 'metaposition', '60', NULL, 1, 'en_US', 'state'),
(43, 'user', 'label', 'Country', NULL, 1, 'en_US', 'country'),
(44, 'user', 'error', 'Country cannot be empty', NULL, 1, 'en_US', 'country'),
(45, 'user', 'control', 'select:${countries}:code:name', NULL, 1, 'en_US', 'country'),
(46, 'user', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'country'),
(47, 'user', 'metaposition', '70', NULL, 1, 'en_US', 'country'),
(48, 'user', 'label', 'Zip', NULL, 1, 'en_US', 'zip'),
(49, 'user', 'error', 'Zip cannot be empty', NULL, 1, 'en_US', 'zip'),
(50, 'user', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'zip'),
(51, 'user', 'metaposition', '80', NULL, 1, 'en_US', 'zip'),
(52, 'user', 'label', 'Phone', NULL, 1, 'en_US', 'phone'),
(53, 'user', 'error', 'Phone cannot be empty', NULL, 1, 'en_US', 'phone'),
(54, 'user', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'phone'),
(55, 'user', 'metaposition', '90', NULL, 1, 'en_US', 'phone'),
(56, 'user', 'label', 'Fax', NULL, 1, 'en_US', 'fax'),
(57, 'user', 'error', 'Fax cannot be empty', NULL, 1, 'en_US', 'fax'),
(58, 'user', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'fax'),
(59, 'user', 'metaposition', '100', NULL, 1, 'en_US', 'fax'),
(60, 'userPending', 'label', 'New User', '2011-09-15 10:31:27', 133, 'en_US', 'page'),
(61, 'userPending', 'data', 'Thank you for your account request. You have been sent an email with instructions as to how to confirm your email address. Your principle investigator has also been emailed to request confirmation of your eligibility to join their lab and you are advised', NULL, 1, 'en_US', 'emailsent'),
(64, 'userPending', 'label', 'Password', NULL, 1, 'en_US', 'password'),
(65, 'userPending', 'error', 'Password cannot be empty', NULL, 1, 'en_US', 'password'),
(66, 'userPending', 'error', 'The two entries for your password are NOT identical', NULL, 1, 'en_US', 'password_mismatch'),
(67, 'userPending', 'error', 'Password must be at least 8 characters, containing only letters and numbers, with at least one letter and number', NULL, 1, 'en_US', 'password_invalid'),
(68, 'userPending', 'label', 'Re-confirm Password', NULL, 1, 'en_US', 'password2'),
(69, 'userPending', 'error', 'Re-confirm password cannot be empty', NULL, 1, 'en_US', 'password2'),
(70, 'userPending', 'label', 'First Name', NULL, 1, 'en_US', 'firstName'),
(71, 'userPending', 'error', 'First Name cannot be empty', NULL, 1, 'en_US', 'firstName'),
(72, 'userPending', 'label', 'Last Name', NULL, 1, 'en_US', 'lastName'),
(73, 'userPending', 'error', 'Last Name cannot be empty', NULL, 1, 'en_US', 'lastName'),
(74, 'userPending', 'label', 'Email', NULL, 1, 'en_US', 'email'),
(75, 'userPending', 'error', 'Must be correctly formatted', NULL, 1, 'en_US', 'email'),
(76, 'userPending', 'error', 'Email already exists in the database', NULL, 1, 'en_US', 'email_exists'),
(77, 'userPending', 'label', 'Locale', NULL, 1, 'en_US', 'locale'),
(78, 'userPending', 'error', 'Locale cannot be empty', NULL, 1, 'en_US', 'locale'),
(79, 'userPending', 'label', 'PI Email Address', NULL, 1, 'en_US', 'primaryuseremail'),
(80, 'userPending', 'error', 'Must be correctly formatted', NULL, 1, 'en_US', 'primaryuseremail'),
(81, 'userPending', 'error', 'Not an active registered PI email address', NULL, 1, 'en_US', 'primaryuseremail_notvalid'),
(82, 'userPending', 'constraint', 'isValidPiEmail', NULL, 1, 'en_US', 'primaryuseremail'),
(83, 'userPending', 'metaposition', '1', NULL, 1, 'en_US', 'primaryuseremail'),
(84, 'userPending', 'label', 'Title', NULL, 1, 'en_US', 'title'),
(85, 'userPending', 'error', 'Title cannot be empty', NULL, 1, 'en_US', 'title'),
(86, 'userPending', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'title'),
(87, 'userPending', 'control', 'select:Prof:Prof;Dr:Dr;Mr:Mr;Ms:Ms;Mrs:Mrs;Miss:Miss', NULL, 1, 'en_US', 'title'),
(88, 'userPending', 'metaposition', '5', NULL, 1, 'en_US', 'title'),
(89, 'userPending', 'label', 'Institution', NULL, 1, 'en_US', 'institution'),
(90, 'userPending', 'error', 'Institution cannot be empty', NULL, 1, 'en_US', 'institution'),
(91, 'userPending', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'institution'),
(92, 'userPending', 'metaposition', '10', NULL, 1, 'en_US', 'institution'),
(93, 'userPending', 'label', 'Department', NULL, 1, 'en_US', 'department'),
(94, 'userPending', 'metaposition', '20', NULL, 1, 'en_US', 'department'),
(95, 'userPending', 'label', 'Room', NULL, 1, 'en_US', 'building_room'),
(96, 'userPending', 'metaposition', '30', NULL, 1, 'en_US', 'building_room'),
(97, 'userPending', 'label', 'Address', NULL, 1, 'en_US', 'address'),
(98, 'userPending', 'error', 'Address cannot be empty', NULL, 1, 'en_US', 'address'),
(99, 'userPending', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'address'),
(100, 'userPending', 'metaposition', '40', NULL, 1, 'en_US', 'address'),
(101, 'userPending', 'label', 'City', NULL, 1, 'en_US', 'city'),
(102, 'userPending', 'error', 'City cannot be empty', NULL, 1, 'en_US', 'city'),
(103, 'userPending', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'city'),
(104, 'userPending', 'metaposition', '50', NULL, 1, 'en_US', 'city'),
(105, 'userPending', 'label', 'State', NULL, 1, 'en_US', 'state'),
(106, 'userPending', 'control', 'select:${states}:code:name', NULL, 1, 'en_US', 'state'),
(107, 'userPending', 'metaposition', '60', NULL, 1, 'en_US', 'state'),
(108, 'userPending', 'label', 'Country', NULL, 1, 'en_US', 'country'),
(109, 'userPending', 'error', 'Country cannot be empty', NULL, 1, 'en_US', 'country'),
(110, 'userPending', 'control', 'select:${countries}:code:name', NULL, 1, 'en_US', 'country'),
(111, 'userPending', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'country'),
(112, 'userPending', 'metaposition', '70', NULL, 1, 'en_US', 'country'),
(113, 'userPending', 'label', 'Zip', NULL, 1, 'en_US', 'zip'),
(114, 'userPending', 'error', 'Zip cannot be empty', NULL, 1, 'en_US', 'zip'),
(115, 'userPending', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'zip'),
(116, 'userPending', 'metaposition', '80', NULL, 1, 'en_US', 'zip'),
(117, 'userPending', 'label', 'Phone', NULL, 1, 'en_US', 'phone'),
(118, 'userPending', 'error', 'Phone cannot be empty', NULL, 1, 'en_US', 'phone'),
(119, 'userPending', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'phone'),
(120, 'userPending', 'metaposition', '90', NULL, 1, 'en_US', 'phone'),
(121, 'userPending', 'label', 'Fax', NULL, 1, 'en_US', 'fax'),
(122, 'userPending', 'metaposition', '100', NULL, 1, 'en_US', 'fax'),
(123, 'piPending', 'label', 'PI Password', NULL, 1, 'en_US', 'password'),
(124, 'piPending', 'error', 'Password cannot be empty', NULL, 1, 'en_US', 'password'),
(125, 'piPending', 'label', 'PI First Name', NULL, 1, 'en_US', 'firstName'),
(126, 'piPending', 'error', 'First Name cannot be empty', NULL, 1, 'en_US', 'firstName'),
(127, 'piPending', 'label', 'PI Last Name', NULL, 1, 'en_US', 'lastName'),
(128, 'piPending', 'error', 'Last Name cannot be empty', NULL, 1, 'en_US', 'lastName'),
(129, 'piPending', 'label', 'PI Email', NULL, 1, 'en_US', 'email'),
(130, 'piPending', 'error', 'Wrong email address format', NULL, 1, 'en_US', 'email'),
(131, 'piPending', 'label', 'PI Locale', NULL, 1, 'en_US', 'locale'),
(132, 'piPending', 'error', 'Locale cannot be empty', NULL, 1, 'en_US', 'locale'),
(133, 'piPending', 'label', 'Lab Name', NULL, 1, 'en_US', 'labName'),
(134, 'piPending', 'error', 'Lab Name cannot be empty', NULL, 1, 'en_US', 'labName'),
(135, 'piPending', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'labName'),
(136, 'piPending', 'metaposition', '1', NULL, 1, 'en_US', 'labName'),
(137, 'piPending', 'label', 'departmentId', NULL, 1, 'en_US', 'departmentId'),
(138, 'piPending', 'error', 'departmentId cannot be empty', NULL, 1, 'en_US', 'departmentId'),
(139, 'piPending', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'departmentId'),
(140, 'piPending', 'metaposition', '2', NULL, 1, 'en_US', 'departmentId'),
(141, 'piPending', 'label', 'Title', NULL, 1, 'en_US', 'title'),
(142, 'piPending', 'error', 'Title cannot be empty', NULL, 1, 'en_US', 'title'),
(143, 'piPending', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'title'),
(144, 'piPending', 'control', 'select:Prof:Prof;Dr:Dr;Mr:Mr;Ms:Ms;Mrs:Mrs;Miss:Miss', NULL, 1, 'en_US', 'title'),
(145, 'piPending', 'metaposition', '5', NULL, 1, 'en_US', 'title'),
(146, 'piPending', 'label', 'Institution', NULL, 1, 'en_US', 'institution'),
(147, 'piPending', 'error', 'Institution cannot be empty', NULL, 1, 'en_US', 'institution'),
(148, 'piPending', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'institution'),
(149, 'piPending', 'metaposition', '10', NULL, 1, 'en_US', 'institution'),
(150, 'piPending', 'label', 'Department', NULL, 1, 'en_US', 'department'),
(151, 'piPending', 'metaposition', '20', NULL, 1, 'en_US', 'department'),
(152, 'piPending', 'label', 'Room', NULL, 1, 'en_US', 'building_room'),
(153, 'piPending', 'metaposition', '30', NULL, 1, 'en_US', 'building_room'),
(154, 'piPending', 'label', 'Address', NULL, 1, 'en_US', 'address'),
(155, 'piPending', 'error', 'Address cannot be empty', NULL, 1, 'en_US', 'address'),
(156, 'piPending', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'address'),
(157, 'piPending', 'metaposition', '40', NULL, 1, 'en_US', 'address'),
(158, 'piPending', 'label', 'City', NULL, 1, 'en_US', 'city'),
(159, 'piPending', 'error', 'City cannot be empty', NULL, 1, 'en_US', 'city'),
(160, 'piPending', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'city'),
(161, 'piPending', 'metaposition', '50', NULL, 1, 'en_US', 'city'),
(162, 'piPending', 'label', 'State', NULL, 1, 'en_US', 'state'),
(163, 'piPending', 'control', 'select:${states}:code:name', NULL, 1, 'en_US', 'state'),
(164, 'piPending', 'metaposition', '60', NULL, 1, 'en_US', 'state'),
(165, 'piPending', 'label', 'Country', NULL, 1, 'en_US', 'country'),
(166, 'piPending', 'error', 'Country cannot be empty', NULL, 1, 'en_US', 'country'),
(167, 'piPending', 'control', 'select:${countries}:code:name', NULL, 1, 'en_US', 'country'),
(168, 'piPending', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'country'),
(169, 'piPending', 'metaposition', '70', NULL, 1, 'en_US', 'country'),
(170, 'piPending', 'label', 'Zip', NULL, 1, 'en_US', 'zip'),
(171, 'piPending', 'error', 'Zip cannot be empty', NULL, 1, 'en_US', 'zip'),
(172, 'piPending', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'zip'),
(173, 'piPending', 'metaposition', '80', NULL, 1, 'en_US', 'zip'),
(174, 'piPending', 'label', 'Phone', NULL, 1, 'en_US', 'phone'),
(175, 'piPending', 'error', 'Phone cannot be empty', NULL, 1, 'en_US', 'phone'),
(176, 'piPending', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'phone'),
(177, 'piPending', 'metaposition', '90', NULL, 1, 'en_US', 'phone'),
(178, 'piPending', 'label', 'Fax', NULL, 1, 'en_US', 'fax'),
(179, 'piPending', 'metaposition', '100', NULL, 1, 'en_US', 'fax'),
(180, 'lab', 'label', 'Lab Name', NULL, 1, 'en_US', 'name'),
(181, 'lab', 'error', 'Lab Name cannot be empty', NULL, 1, 'en_US', 'name'),
(182, 'lab', 'label', 'Primary User', NULL, 1, 'en_US', 'primaryUserId'),
(183, 'lab', 'error', 'Please select Primary User', NULL, 1, 'en_US', 'primaryUserId'),
(184, 'lab', 'label', 'Department', NULL, 1, 'en_US', 'departmentId'),
(185, 'lab', 'error', 'Please select department', NULL, 1, 'en_US', 'departmentId'),
(186, 'lab', 'label', 'Active', NULL, 1, 'en_US', 'isActive'),
(187, 'lab', 'label', 'External/Internal', NULL, 1, 'en_US', 'internal_external_lab'),
(188, 'lab', 'error', 'Please specify lab type (External/Internal)', NULL, 1, 'en_US', 'internal_external_lab'),
(189, 'lab', 'control', 'select:external:External;internal:Internal', NULL, 1, 'en_US', 'internal_external_lab'),
(190, 'lab', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'internal_external_lab'),
(191, 'lab', 'metaposition', '10', NULL, 1, 'en_US', 'internal_external_lab'),
(192, 'lab', 'label', 'Phone', NULL, 1, 'en_US', 'phone'),
(193, 'lab', 'error', 'Phone cannot be empty', NULL, 1, 'en_US', 'phone'),
(194, 'lab', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'phone'),
(195, 'lab', 'metaposition', '20', NULL, 1, 'en_US', 'phone'),
(196, 'lab', 'label', 'Room', NULL, 1, 'en_US', 'building_room'),
(197, 'lab', 'metaposition', '30', NULL, 1, 'en_US', 'building_room'),
(198, 'lab', 'label', 'Billing Dept. Name', NULL, 1, 'en_US', 'billing_billing_dept_name'),
(199, 'lab', 'metaposition', '40', NULL, 1, 'en_US', 'billing_billing_dept_name'),
(200, 'lab', 'label', 'Billing Contact Name', NULL, 1, 'en_US', 'billing_contact_name'),
(201, 'lab', 'metaposition', '50', NULL, 1, 'en_US', 'billing_contact_name'),
(202, 'lab', 'label', 'Billing Inst. Name', NULL, 1, 'en_US', 'billing_institution_name'),
(203, 'lab', 'metaposition', '60', NULL, 1, 'en_US', 'billing_institution_name'),
(204, 'lab', 'label', 'Billing Bld. Room', NULL, 1, 'en_US', 'billing_building_room'),
(205, 'lab', 'metaposition', '70', NULL, 1, 'en_US', 'billing_building_room'),
(206, 'lab', 'label', 'Billing Address', NULL, 1, 'en_US', 'billing_address'),
(207, 'lab', 'metaposition', '80', NULL, 1, 'en_US', 'billing_address'),
(208, 'lab', 'label', 'Billing City', NULL, 1, 'en_US', 'billing_city'),
(209, 'lab', 'metaposition', '90', NULL, 1, 'en_US', 'billing_city'),
(210, 'lab', 'label', 'Billing State', NULL, 1, 'en_US', 'billing_state'),
(211, 'lab', 'control', 'select:${states}:code:name', NULL, 1, 'en_US', 'billing_state'),
(212, 'lab', 'metaposition', '100', NULL, 1, 'en_US', 'billing_state'),
(213, 'lab', 'label', 'Billing Country', NULL, 1, 'en_US', 'billing_country'),
(214, 'lab', 'control', 'select:${countries}:code:name', NULL, 1, 'en_US', 'billing_country'),
(215, 'lab', 'metaposition', '105', NULL, 1, 'en_US', 'billing_country'),
(216, 'lab', 'label', 'Billing Zip', NULL, 1, 'en_US', 'billing_zip'),
(217, 'lab', 'metaposition', '110', NULL, 1, 'en_US', 'billing_zip'),
(218, 'lab', 'label', 'Billing Email', NULL, 1, 'en_US', 'billing_email'),
(219, 'lab', 'metaposition', '120', NULL, 1, 'en_US', 'billing_email'),
(220, 'lab', 'label', 'Billing Phone', NULL, 1, 'en_US', 'billing_phone'),
(221, 'lab', 'metaposition', '130', NULL, 1, 'en_US', 'billing_phone'),
(222, 'lab', 'label', 'RS Name', NULL, 1, 'en_US', 'research_center_name'),
(223, 'lab', 'metaposition', '140', NULL, 1, 'en_US', 'research_center_name'),
(224, 'lab', 'label', 'RS User URL', NULL, 1, 'en_US', 'researchcenter_user_list_location'),
(225, 'lab', 'metaposition', '150', NULL, 1, 'en_US', 'researchcenter_user_list_location'),
(226, 'lab', 'success', 'Lab was updated', NULL, 1, 'en_US', 'updated'),
(227, 'lab', 'success', 'Lab was created', NULL, 1, 'en_US', 'created'),
(228, 'lab', 'error', 'Lab was NOT updated. Please fill in required fields.', NULL, 1, 'en_US', 'updated'),
(229, 'lab', 'error', 'Lab was NOT created. Please fill in required fields.', NULL, 1, 'en_US', 'created'),
(230, 'user', 'success', 'User was updated', NULL, 1, 'en_US', 'updated'),
(231, 'user', 'success', 'User was created', NULL, 1, 'en_US', 'created'),
(232, 'user', 'error', 'User was NOT updated. Please fill in required fields.', NULL, 1, 'en_US', 'updated'),
(233, 'user', 'error', 'User was NOT created. Please fill in required fields.', NULL, 1, 'en_US', 'created'),
(234, 'labuser', 'title', 'Request Access To Lab', NULL, 1, 'en_US', 'request'),
(235, 'labuser', 'primaryuser.label', 'Primary User', NULL, 1, 'en_US', 'request'),
(236, 'labuser', 'primaryuser.error', 'Invalid Primary User', NULL, 1, 'en_US', 'request'),
(237, 'labuser', 'submit.label', 'Request Access', NULL, 1, 'en_US', 'request'),
(238, 'labuser', 'success', 'Your request for access has been submitted.', NULL, 1, 'en_US', 'request'),
(239, 'labuser', 'alreadypending.error', 'Could not request for access to that Lab.', NULL, 1, 'en_US', 'request'),
(240, 'labuser', 'alreadyaccess.error', 'Could not request for access to that Lab.', NULL, 1, 'en_US', 'request'),
(241, 'labPending', 'label', 'Lab Name', NULL, 1, 'en_US', 'name'),
(242, 'labPending', 'error', 'Lab Name cannot be empty', NULL, 1, 'en_US', 'name'),
(243, 'labPending', 'label', 'Primary User', NULL, 1, 'en_US', 'primaryUserId'),
(244, 'labPending', 'error', 'Please select Primary User', NULL, 1, 'en_US', 'primaryUserId'),
(245, 'labPending', 'label', 'Department', '2011-09-15 12:10:34', 133, 'en_US', 'departmentId'),
(246, 'labPending', 'error', 'Please select department', NULL, 1, 'en_US', 'departmentId'),
(247, 'labPending', 'label', 'External/Internal', NULL, 1, 'en_US', 'internal_external_lab'),
(248, 'labPending', 'error', 'Please specify lab type (External/Internal)', NULL, 1, 'en_US', 'internal_external_lab'),
(249, 'labPending', 'control', 'select:external:External;internal:Internal', NULL, 1, 'en_US', 'internal_external_lab'),
(250, 'labPending', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'internal_external_lab'),
(251, 'labPending', 'metaposition', '10', NULL, 1, 'en_US', 'internal_external_lab'),
(252, 'labPending', 'label', 'Phone', NULL, 1, 'en_US', 'phone'),
(253, 'labPending', 'error', 'Phone cannot be empty', NULL, 1, 'en_US', 'phone'),
(254, 'labPending', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'phone'),
(255, 'labPending', 'metaposition', '20', NULL, 1, 'en_US', 'phone'),
(256, 'labPending', 'label', 'Room', NULL, 1, 'en_US', 'building_room'),
(257, 'labPending', 'metaposition', '30', NULL, 1, 'en_US', 'building_room'),
(258, 'labPending', 'label', 'Billing Dept. Name', NULL, 1, 'en_US', 'billing_billing_dept_name'),
(259, 'labPending', 'metaposition', '40', NULL, 1, 'en_US', 'billing_billing_dept_name'),
(260, 'labPending', 'label', 'Billing Contact Name', NULL, 1, 'en_US', 'billing_contact_name'),
(261, 'labPending', 'metaposition', '50', NULL, 1, 'en_US', 'billing_contact_name'),
(262, 'labPending', 'label', 'Billing Inst. Name', NULL, 1, 'en_US', 'billing_institution_name'),
(263, 'labPending', 'metaposition', '60', NULL, 1, 'en_US', 'billing_institution_name'),
(264, 'labPending', 'label', 'Billing Bld. Room', NULL, 1, 'en_US', 'billing_building_room'),
(265, 'labPending', 'metaposition', '70', NULL, 1, 'en_US', 'billing_building_room'),
(266, 'labPending', 'label', 'Billing Address', NULL, 1, 'en_US', 'billing_address'),
(267, 'labPending', 'metaposition', '80', NULL, 1, 'en_US', 'billing_address'),
(268, 'labPending', 'label', 'Billing City', NULL, 1, 'en_US', 'billing_city'),
(269, 'labPending', 'metaposition', '90', NULL, 1, 'en_US', 'billing_city'),
(270, 'labPending', 'label', 'Billing State', NULL, 1, 'en_US', 'billing_state'),
(271, 'labPending', 'control', 'select:${states}:code:name', NULL, 1, 'en_US', 'billing_state'),
(272, 'labPending', 'metaposition', '100', NULL, 1, 'en_US', 'billing_state'),
(273, 'labPending', 'label', 'Billing Country', NULL, 1, 'en_US', 'billing_country'),
(274, 'labPending', 'control', 'select:${countries}:code:name', NULL, 1, 'en_US', 'billing_country'),
(275, 'labPending', 'metaposition', '105', NULL, 1, 'en_US', 'billing_country'),
(276, 'labPending', 'label', 'Billing Zip', NULL, 1, 'en_US', 'billing_zip'),
(277, 'labPending', 'metaposition', '110', NULL, 1, 'en_US', 'billing_zip'),
(278, 'labPending', 'label', 'Billing Email', NULL, 1, 'en_US', 'billing_email'),
(279, 'labPending', 'metaposition', '120', NULL, 1, 'en_US', 'billing_email'),
(280, 'labPending', 'label', 'Billing Phone', NULL, 1, 'en_US', 'billing_phone'),
(281, 'labPending', 'metaposition', '130', NULL, 1, 'en_US', 'billing_phone'),
(282, 'labPending', 'label', 'RS Name', NULL, 1, 'en_US', 'research_center_name'),
(283, 'labPending', 'metaposition', '130', NULL, 1, 'en_US', 'research_center_name'),
(284, 'labPending', 'label', 'RS User URL', NULL, 1, 'en_US', 'researchcenter_user_list_location'),
(285, 'labPending', 'metaposition', '140', NULL, 1, 'en_US', 'researchcenter_user_list_location'),
(286, 'auth', 'title', 'Login Page', NULL, 1, 'en_US', 'login'),
(287, 'user', 'login.validate.error', 'Please provide your user login name AND password.', NULL, 1, 'en_US', 'auth'),
(288, 'auth', 'failed', 'Your login attempt was not successful. Try again.', NULL, 1, 'en_US', 'login'),
(289, 'auth', 'reason.label', 'Reason', NULL, 1, 'en_US', 'login'),
(290, 'auth', 'user.label', 'User', NULL, 1, 'en_US', 'login'),
(291, 'auth', 'password.label', 'Password', NULL, 1, 'en_US', 'login'),
(292, 'auth', 'data', 'Login', NULL, 1, 'en_US', 'login'),
(293, 'auth', 'anchor.forgotpass', 'Forgot Password', NULL, 1, 'en_US', 'login'),
(294, 'auth', 'anchor.newuser', 'New User', NULL, 1, 'en_US', 'login'),
(295, 'auth', 'anchor.newpi', 'New PI', NULL, 1, 'en_US', 'login'),
(296, 'auth', 'anchor.about', 'About', NULL, 1, 'en_US', 'login'),
(297, 'auth', 'title', 'Reset Password Request', NULL, 1, 'en_US', 'forgotpassword'),
(298, 'auth', 'user.label', 'Username', NULL, 1, 'en_US', 'forgotpassword'),
(299, 'auth', 'data', 'Submit', NULL, 1, 'en_US', 'forgotpassword'),
(300, 'auth', 'missingparam.error', 'Please provide values for all fields', NULL, 1, 'en_US', 'forgotpassword'),
(301, 'auth', 'captcha.error', 'Captcha text incorrect', NULL, 1, 'en_US', 'forgotpassword'),
(302, 'auth', 'username.error', 'A user with the supplied username does not exist', NULL, 1, 'en_US', 'forgotpassword'),
(303, 'auth', 'captcha.label', 'Captcha text', NULL, 1, 'en_US', 'forgotpassword'),
(304, 'auth', 'title1', 'Reset Password: Step 1', NULL, 1, 'en_US', 'resetpassword'),
(305, 'auth', 'title2', 'Reset Password: Step 2', NULL, 1, 'en_US', 'resetpassword'),
(306, 'auth', 'user.label', 'Username', NULL, 1, 'en_US', 'resetpassword'),
(307, 'auth', 'authcode.label', 'Auth Code', NULL, 1, 'en_US', 'resetpassword'),
(308, 'auth', 'password1.label', 'New Password', NULL, 1, 'en_US', 'resetpassword'),
(309, 'auth', 'password2.label', 'Confirm New Password', NULL, 1, 'en_US', 'resetpassword'),
(310, 'auth', 'captcha.label', 'Captcha text', NULL, 1, 'en_US', 'resetpassword'),
(311, 'auth', 'data', 'Submit', NULL, 1, 'en_US', 'resetpassword'),
(312, 'auth', 'noauthcode.error', 'No authorization code provided', NULL, 1, 'en_US', 'resetpassword'),
(313, 'auth', 'missingparam.error', 'Please provide values for all fields', NULL, 1, 'en_US', 'resetpassword'),
(314, 'auth', 'badauthcode.error', 'Invalid authorization code provided', NULL, 1, 'en_US', 'resetpassword'),
(315, 'auth', 'captcha.error', 'Captcha text incorrect', NULL, 1, 'en_US', 'resetpassword'),
(316, 'auth', 'username.error', 'A user with the supplied username does not exist', NULL, 1, 'en_US', 'resetpassword'),
(317, 'auth', 'wronguser.error', 'Username and authorization code provided do not match', NULL, 1, 'en_US', 'resetpassword'),
(318, 'auth', 'new_mismatch.error', 'The two entries for your NEW password are NOT identical', NULL, 1, 'en_US', 'resetpassword'),
(319, 'auth', 'new_invalid.error', 'New password must be at least 8 characters, containing only letters and numbers, with at least one letter and number', NULL, 1, 'en_US', 'resetpassword'),
(320, 'auth', 'instructions', 'New Password Requirements:<br />At least 8 characters in length<br />Only letters & numbers permitted (no spaces, etc)<br />At least one letter and one number<br />', NULL, 1, 'en_US', 'resetpassword'),
(321, 'auth', 'title', 'Reset Password Request: Email Sent', NULL, 1, 'en_US', 'resetpasswordemailsent'),
(322, 'auth', 'data', 'An email has been sent to your registered email address containing an authorization code. Please click the link within this email or alternatively <a href', NULL, 1, 'en_US', 'resetpasswordemailsent'),
(323, 'auth', 'title', 'Reset Password: Complete', NULL, 1, 'en_US', 'resetpasswordok'),
(324, 'auth', 'data', 'Your password has been reset. Click <a href', NULL, 1, 'en_US', 'resetpasswordok'),
(325, 'user', 'title', 'Change Password', NULL, 1, 'en_US', 'mypassword'),
(326, 'user', 'instructions', 'New Password Requirements:<br />At least 8 characters in length<br />Only letters & numbers permitted (no spaces, etc)<br />At least one letter and one number<br />', NULL, 1, 'en_US', 'mypassword'),
(327, 'user', 'oldpassword.label', 'Old Password', NULL, 1, 'en_US', 'mypassword'),
(328, 'user', 'newpassword1.label', 'New Password', NULL, 1, 'en_US', 'mypassword'),
(329, 'user', 'newpassword2.label', 'Confirm New Password', NULL, 1, 'en_US', 'mypassword'),
(330, 'user', 'data', 'Submit', NULL, 1, 'en_US', 'mypassword'),
(331, 'user', 'missingparam.error', 'Please provide values for all fields', NULL, 1, 'en_US', 'mypassword'),
(332, 'user', 'cur_mismatch.error', 'Your old password does NOT match the password in our database', NULL, 1, 'en_US', 'mypassword'),
(333, 'user', 'new_mismatch.error', 'The two entries for your NEW password are NOT identical', NULL, 1, 'en_US', 'mypassword'),
(334, 'user', 'nodiff.error', 'Your old and new passwords may NOT be the same', NULL, 1, 'en_US', 'mypassword'),
(335, 'user', 'new_invalid.error', 'New password must be at least 8 characters, containing only letters and numbers, with at least one letter and number', NULL, 1, 'en_US', 'mypassword'),
(336, 'user', 'ok', 'Password Has Been Changed', NULL, 1, 'en_US', 'mypassword'),
(337, 'auth', 'badauthcode.error', 'Invalid authorization code provided', NULL, 1, 'en_US', 'confirmemail'),
(338, 'auth', 'corruptemail.error', 'email address in url cannot be decoded', NULL, 1, 'en_US', 'confirmemail'),
(339, 'auth', 'bademail.error', 'email address is incorrect', NULL, 1, 'en_US', 'confirmemail'),
(340, 'auth', 'wronguser.error', 'User email address and authorization code provided do not match', NULL, 1, 'en_US', 'confirmemail'),
(341, 'auth', 'captcha.error', 'Captcha text incorrect', NULL, 1, 'en_US', 'confirmemail'),
(342, 'auth', 'title', 'Confirm Email Address', NULL, 1, 'en_US', 'confirmemail'),
(343, 'auth', 'email.label', 'Email Address', NULL, 1, 'en_US', 'confirmemail'),
(344, 'auth', 'authcode.label', 'Auth code', NULL, 1, 'en_US', 'confirmemail'),
(345, 'auth', 'captcha.label', 'Captcha text', NULL, 1, 'en_US', 'confirmemail'),
(346, 'auth', 'data', 'Submit', NULL, 1, 'en_US', 'confirmemail'),
(347, 'auth', 'title', 'Email Address Confirmed', NULL, 1, 'en_US', 'confirmemailok'),
(348, 'auth', 'data', 'Thank you for confirming your email address.', NULL, 1, 'en_US', 'confirmemailok'),
(349, 'department', 'title', 'Department List', NULL, 1, 'en_US', 'list'),
(350, 'department', 'create.label', 'Create Department', NULL, 1, 'en_US', 'list'),
(351, 'department', 'department.label', 'Department Name', NULL, 1, 'en_US', 'list'),
(352, 'department', 'data', 'Submit', NULL, 1, 'en_US', 'list'),
(353, 'department', 'missingparam.error', 'Please provide a department name', NULL, 1, 'en_US', 'list'),
(354, 'department', 'department_exists.error', 'Department already exists', NULL, 1, 'en_US', 'list'),
(355, 'department', 'ok', 'New department has been created', NULL, 1, 'en_US', 'list'),
(356, 'jobDraft', 'label', 'Job Name', NULL, 1, 'en_US', 'name'),
(357, 'jobDraft', 'error', 'Job Name Must Not Be Empty', NULL, 1, 'en_US', 'name'),
(358, 'jobDraft', 'label', 'Lab', NULL, 1, 'en_US', 'labId'),
(359, 'jobDraft', 'error', 'Lab Must Not Be Empty', NULL, 1, 'en_US', 'labId'),
(360, 'jobDraft', 'label', 'Workflow', NULL, 1, 'en_US', 'workflowId'),
(361, 'jobDraft', 'error', 'Workflow Must Not Be Empty', NULL, 1, 'en_US', 'workflowId'),
(362, 'ampliconSeq', 'label', 'LABEL:ampliconSeq', NULL, 1, 'en_US', 'workflow'),
(363, 'cgh', 'label', 'LABEL:cgh', NULL, 1, 'en_US', 'workflow'),
(364, 'chipSeq', 'label', 'LABEL:chipSeq', NULL, 1, 'en_US', 'workflow'),
(365, 'controlMicroarray', 'label', 'LABEL:controlMicroarray', NULL, 1, 'en_US', 'workflow'),
(366, 'controlSeq', 'label', 'LABEL:controlSeq', NULL, 1, 'en_US', 'workflow'),
(367, 'deNovoSeq', 'label', 'LABEL:deNovoSeq', NULL, 1, 'en_US', 'workflow'),
(368, 'digitalExpressionProfiling', 'label', 'LABEL:digitalExpressionProfiling', NULL, 1, 'en_US', 'workflow'),
(369, 'directionalRnaSeq', 'label', 'LABEL:directionalRnaSeq', NULL, 1, 'en_US', 'workflow'),
(370, 'geneExpressionSeq', 'label', 'LABEL:geneExpressionSeq', NULL, 1, 'en_US', 'workflow'),
(371, 'helpTag', 'label', 'LABEL:helpTag', NULL, 1, 'en_US', 'workflow'),
(372, 'matePairSeq', 'label', 'LABEL:matePairSeq', NULL, 1, 'en_US', 'workflow'),
(373, 'microarrayChip', 'label', 'LABEL:microarrayChip', NULL, 1, 'en_US', 'workflow'),
(374, 'microarrayGeneExpression', 'label', 'LABEL:microarrayGeneExpression', NULL, 1, 'en_US', 'workflow'),
(375, 'microarrayHelp', 'label', 'LABEL:microarrayHelp', NULL, 1, 'en_US', 'workflow'),
(376, 'mirnaSeq', 'label', 'LABEL:mirnaSeq', NULL, 1, 'en_US', 'workflow'),
(377, 'otherSeqDnaSamples', 'label', 'LABEL:otherSeqDnaSamples', NULL, 1, 'en_US', 'workflow'),
(378, 'otherSeqRnaSamples', 'label', 'LABEL:otherSeqRnaSamples', NULL, 1, 'en_US', 'workflow'),
(379, 'resequencing', 'label', 'LABEL:resequencing', NULL, 1, 'en_US', 'workflow'),
(380, 'rnaSeq', 'label', 'LABEL:rnaSeq', NULL, 1, 'en_US', 'workflow'),
(381, 'seqCap', 'label', 'LABEL:seqCap', NULL, 1, 'en_US', 'workflow'),

(383, 'chipSeq', 'data', 'samplefile;chipseqSample', NULL, 1, 'en_US', 'samples'),
(384, 'chipSeq', 'label', 'Platform', NULL, 1, 'en_US', 'platform'),
(385, 'chipSeq', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'platform'),
(386, 'chipSeq', 'error', 'Platform cannot be empty', NULL, 1, 'en_US', 'platform'),
(387, 'chipSeq', 'control', 'select:HISeq2000:HISeq2000', NULL, 1, 'en_US', 'platform'),
(388, 'chipSeq', 'metaposition', '100', NULL, 1, 'en_US', 'platform'),
(389, 'chipSeq', 'label', 'Read Length', NULL, 1, 'en_US', 'readlength'),
(390, 'chipSeq', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'readlength'),
(391, 'chipSeq', 'error', 'readlength cannot be empty', NULL, 1, 'en_US', 'readlength'),
(392, 'chipSeq', 'control', 'select:100:100 bp; 150: 150bp', NULL, 1, 'en_US', 'readlength'),
(393, 'chipSeq', 'metaposition', '110', NULL, 1, 'en_US', 'readlength'),
(394, 'chipSeq', 'label', 'Read Type', NULL, 1, 'en_US', 'readtype'),
(395, 'chipSeq', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'readtype'),
(396, 'chipSeq', 'error', 'readtype cannot be empty', NULL, 1, 'en_US', 'readtype'),
(397, 'chipSeq', 'control', 'select:single:Single-End Read; pair:Pair-End Read', NULL, 1, 'en_US', 'readtype'),
(398, 'chipSeq', 'metaposition', '120', NULL, 1, 'en_US', 'readtype'),
(399, 'chipSeq', 'label', 'Anti Body', NULL, 1, 'en_US', 'antibody'),
(400, 'chipSeq', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'antibody'),
(401, 'chipSeq', 'error', 'antibody cannot be empty', NULL, 1, 'en_US', 'antibody'),
(402, 'chipSeq', 'control', 'select:abc:abc;def:def;ghi:ghi', NULL, 1, 'en_US', 'antibody'),
(403, 'chipSeq', 'metaposition', '130', NULL, 1, 'en_US', 'antibody'),
(404, 'chipSeq', 'label', 'PCR Primers', NULL, 1, 'en_US', 'pcrprimers'),
(405, 'chipSeq', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'pcrprimers'),
(406, 'chipSeq', 'error', 'pcrprimers cannot be empty', NULL, 1, 'en_US', 'pcrprimers'),
(407, 'chipSeq', 'control', 'select:xyz:xyz', NULL, 1, 'en_US', 'pcrprimers'),
(408, 'chipSeq', 'metaposition', '140', NULL, 1, 'en_US', 'pcrprimers'),
(410, 'ampliconSeq', 'data', 'samplefile;ampliconSample', NULL, 1, 'en_US', 'samples'),
(411, 'ampliconSeq', 'label', 'Platform', NULL, 1, 'en_US', 'platform'),
(412, 'ampliconSeq', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'platform'),
(413, 'ampliconSeq', 'error', 'Platform cannot be empty', NULL, 1, 'en_US', 'platform'),
(414, 'ampliconSeq', 'control', 'select:HISeq2000:HISeq2000', NULL, 1, 'en_US', 'platform'),
(415, 'ampliconSeq', 'metaposition', '100', NULL, 1, 'en_US', 'platform'),
(416, 'ampliconSeq', 'label', 'Read Length', NULL, 1, 'en_US', 'readlength'),
(417, 'ampliconSeq', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'readlength'),
(418, 'ampliconSeq', 'error', 'readlength cannot be empty', NULL, 1, 'en_US', 'readlength'),
(419, 'ampliconSeq', 'control', 'select:100:100 bp; 150: 150bp', NULL, 1, 'en_US', 'readlength'),
(420, 'ampliconSeq', 'metaposition', '110', NULL, 1, 'en_US', 'readlength'),
(421, 'ampliconSeq', 'label', 'Read Type', NULL, 1, 'en_US', 'readtype'),
(422, 'ampliconSeq', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'readtype'),
(423, 'ampliconSeq', 'error', 'readtype cannot be empty', NULL, 1, 'en_US', 'readtype'),
(424, 'ampliconSeq', 'control', 'select:single:Single-End Read; pair:Pair-End Read', NULL, 1, 'en_US', 'readtype'),
(425, 'ampliconSeq', 'metaposition', '120', NULL, 1, 'en_US', 'readtype'),
(426, 'ampliconSeq', 'label', 'Anti Body', NULL, 1, 'en_US', 'antibody'),
(427, 'ampliconSeq', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'antibody'),
(428, 'ampliconSeq', 'error', 'antibody cannot be empty', NULL, 1, 'en_US', 'antibody'),
(429, 'ampliconSeq', 'control', 'select:abc:abc;def:def;ghi:ghi', NULL, 1, 'en_US', 'antibody'),
(430, 'ampliconSeq', 'metaposition', '130', NULL, 1, 'en_US', 'antibody'),
(431, 'ampliconSeq', 'label', 'PCR Primers', NULL, 1, 'en_US', 'pcrprimers'),
(432, 'ampliconSeq', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'pcrprimers'),
(433, 'ampliconSeq', 'error', 'pcrprimers cannot be empty', NULL, 1, 'en_US', 'pcrprimers'),
(434, 'ampliconSeq', 'control', 'select:xyz:xyz', NULL, 1, 'en_US', 'pcrprimers'),
(435, 'ampliconSeq', 'metaposition', '140', NULL, 1, 'en_US', 'pcrprimers'),
(436, 'sampleDraft', 'label', 'Name', NULL, 1, 'en_US', 'name'),
(437, 'sampleDraft', 'constraint', 'NotEmpty', '2011-09-15 12:08:40', 133, 'en_US', 'name'),
(438, 'sampleDraft', 'error', 'Please specify sample name', NULL, 1, 'en_US', 'name'),
(439, 'sampleDraft', 'label', 'Type', NULL, 1, 'en_US', 'typeSampleId'),
(440, 'sampleDraft', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'typeSampleId'),
(441, 'sampleDraft', 'error', 'Please specify type', NULL, 1, 'en_US', 'typeSampleId'),
(442, 'sampleDraft', 'label', 'Status', NULL, 1, 'en_US', 'status'),
(443, 'sampleDraft', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'status'),
(444, 'sampleDraft', 'error', 'Please specify status', NULL, 1, 'en_US', 'status'),
(445, 'sampleDraft', 'label', 'Sample File', NULL, 1, 'en_US', 'fileData'),
(446, 'sampleDraft', 'suffix', '(10Mb Max)', NULL, 1, 'en_US', 'fileData'),
(447, 'sampleDraft', 'data', 'Sample Created.', '2011-09-15 11:55:09', 133, 'en_US', 'created'),
(448, 'sampleDraft', 'data', 'Sample Updated.', '2011-09-15 11:55:18', 133, 'en_US', 'updated'),
(449, 'sampleDraft', 'data', 'Sample Removed.', '2011-09-15 11:54:26', 133, 'en_US', 'removed'),
(450, 'sampleDraft', 'data', 'File Upload Started. Please Wait ...', '2011-09-15 12:02:46', 133, 'en_US', 'fileupload_wait'),
(451, 'sampleDraft', 'data', 'File Upload Done.', '2011-09-15 12:02:40', 133, 'en_US', 'fileupload_done'),
(452, 'sampleDraft', 'data', 'No File Selected.', '2011-09-15 12:02:34', 133, 'en_US', 'fileupload_nofile'),
(453, 'sampleDraft', 'label', 'material_provided', NULL, 1, 'en_US', 'material_provided'),
(454, 'sampleDraft', 'label', 'reference_genome_id', NULL, 1, 'en_US', 'reference_genome_id'),
(455, 'sampleDraft', 'label', 'species_id', NULL, 1, 'en_US', 'species_id'),
(456, 'sampleDraft', 'label', 'fragment_size', NULL, 1, 'en_US', 'fragment_size'),
(457, 'sampleDraft', 'label', 'amount', NULL, 1, 'en_US', 'amount'),
(458, 'sampleDraft', 'label', 'concentration', NULL, 1, 'en_US', 'concentration'),
(459, 'sampleDraft', 'label', '260_280', NULL, 1, 'en_US', '260_280'),
(460, 'sampleDraft', 'label', '260_230', NULL, 1, 'en_US', '260_230'),
(461, 'sampleDraft', 'label', 'volume', NULL, 1, 'en_US', 'volume'),
(462, 'sampleDraft', 'label', 'buffer', NULL, 1, 'en_US', 'buffer'),
(463, 'sampleDraft', 'label', 'sample_type', NULL, 1, 'en_US', 'sample_type'),
(464, 'sampleDraft', 'label', 'antibody_id', NULL, 1, 'en_US', 'antibody_id'),
(465, 'sampleDraft', 'label', 'enrich_primer_pair_id', NULL, 1, 'en_US', 'enrich_primer_pair_id'),
(466, 'sampleDraft', 'metaposition', '10', NULL, 1, 'en_US', 'material_provided'),
(467, 'sampleDraft', 'metaposition', '20', NULL, 1, 'en_US', 'reference_genome_id'),
(468, 'sampleDraft', 'metaposition', '30', NULL, 1, 'en_US', 'species_id'),
(469, 'sampleDraft', 'metaposition', '40', NULL, 1, 'en_US', 'fragment_size'),
(470, 'sampleDraft', 'metaposition', '50', NULL, 1, 'en_US', 'amount'),
(471, 'sampleDraft', 'metaposition', '60', NULL, 1, 'en_US', 'concentration'),
(472, 'sampleDraft', 'metaposition', '70', NULL, 1, 'en_US', '260_280'),
(473, 'sampleDraft', 'metaposition', '80', NULL, 1, 'en_US', '260_230'),
(474, 'sampleDraft', 'metaposition', '90', NULL, 1, 'en_US', 'volume'),
(475, 'sampleDraft', 'metaposition', '100', NULL, 1, 'en_US', 'buffer'),
(476, 'sampleDraft', 'metaposition', '110', NULL, 1, 'en_US', 'sample_type'),
(477, 'sampleDraft', 'metaposition', '120', NULL, 1, 'en_US', 'antibody_id'),
(478, 'sampleDraft', 'metaposition', '130', NULL, 1, 'en_US', 'enrich_primer_pair_id'),
(479, 'hello', '', '', NULL, 1, 'en_US', 'error=Hello World'),
(480, 'sample', 'label', 'Name', NULL, 1, 'en_US', 'name'),
(481, 'sample', 'error', 'Name cannot be null', NULL, 1, 'en_US', 'name'),
(482, 'platformunit', 'label', 'Barcode', NULL, 1, 'en_US', 'barcode'),
(483, 'platformunit', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'barcode'),
(484, 'platformunit', 'error', 'Barcode cannot be empty', NULL, 1, 'en_US', 'barcode'),
(485, 'platformunit', 'metaposition', '10', NULL, 1, 'en_US', 'barcode'),
(486, 'platformunit', 'label', 'Comment', NULL, 1, 'en_US', 'comment'),
(487, 'platformunit', 'error', 'Comment', NULL, 1, 'en_US', 'comment'),
(488, 'platformunit', 'metaposition', '20', NULL, 1, 'en_US', 'comment'),
(489, 'platformunit', 'label', 'Version', NULL, 1, 'en_US', 'version'),
(490, 'platformunit', 'error', 'VErsioN', NULL, 1, 'en_US', 'version'),
(491, 'platformunit', 'metaposition', '30', NULL, 1, 'en_US', 'version'),
(492, 'platformunit', 'label', 'Lane Count', NULL, 1, 'en_US', 'lanecount'),
(493, 'platformunit', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'lanecount'),
(494, 'platformunit', 'error', 'Lane Count cannot be empty', NULL, 1, 'en_US', 'lanecount'),
(495, 'platformunit', 'control', 'select:1:1;8:8', NULL, 1, 'en_US', 'lanecount'),
(496, 'platformunit', 'metaposition', '40', NULL, 1, 'en_US', 'lanecount'),
(497, 'fmpayment', 'label', 'Amount', NULL, 1, 'en_US', 'amount'),
(498, 'fmpayment', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'amount'),
(499, 'fmpayment', 'metaposition', '10', NULL, 1, 'en_US', 'amount'),
(500, 'fmpayment', 'error', 'AmounT cannot be Empty', NULL, 1, 'en_US', 'amount'),
(501, 'fmpayment', 'label', 'Comment', NULL, 1, 'en_US', 'comment'),
(502, 'fmpayment', 'constraint', 'NotEmpty', NULL, 1, 'en_US', 'comment'),
(503, 'fmpayment', 'metaposition', '20', NULL, 1, 'en_US', 'comment'),
(504, 'fmpayment', 'error', 'Comment cannot be Empty', NULL, 1, 'en_US', 'comment'),
(505, 'uiField', 'success', 'Field Updated', NULL, 1, 'en_US', 'updated'),
(506, 'uiField', 'success', 'Field Added', NULL, 1, 'en_US', 'added'),
(507, 'uiField', 'data', 'Field Deleted', '2011-09-15 11:54:02', 133, 'en_US', 'removed'),
(508, 'uiField', 'label', 'Field Name', '2011-09-15 10:02:50', 133, 'en_US', 'name'),
(509, 'uiField', 'label', 'Area', NULL, 1, 'en_US', 'area'),
(510, 'uiField', 'label', 'Locale', NULL, 1, 'en_US', 'locale'),
(511, 'uiField', 'label', 'Attribute Name', '2011-09-15 11:07:13', 133, 'en_US', 'attrName'),
(512, 'uiField', 'label', 'Attribute Value', NULL, 1, 'en_US', 'attrValue'),
(513, 'uiField', 'error', 'Locale not specified', NULL, 1, 'en_US', 'locale'),
(514, 'user', 'label', 'Логин', '2011-09-15 08:47:33', 133, 'ru_RU', 'login'),
(515, 'uiField', 'label', 'Язык', '2011-09-15 09:53:07', 133, 'ru_RU', 'locale'),
(516, 'uiField', 'label', 'Обьект', '2011-09-15 09:54:11', 133, 'ru_RU', 'area'),
(518, 'jobDraft', 'label', 'Название Работы', '2011-09-15 10:04:44', 133, 'ru_RU', 'name'),
(519, 'uiField', 'suffix', '<font color="blue"> see footnote<sup>1</sup> </font>', '2011-09-15 11:14:33', 133, 'en_US', 'attrName');



/*
*/
