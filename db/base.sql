# sets up default users
insert into user 
values
(1, 'super', 'super@super.com', SHA1('a'), 'f', 'l', 'en_US', 1, now(), 1);

insert into userrole
values
(1, 1, 11, now(), 1);

insert into lab
values
(1, 1, 'default lab', 1, 1, now(), 1);

insert into labuser
values
(1, 1, 1, 6, now(), 1);

# - if convert.sql was run
# update user set password = SHA1('password'); 
# 
# insert into userrole (userid, roleid) values (133, 11);


### to run after 
# sets up resources and options for a workfow
insert into workflowresourcecategory 
(workflowresourcecategoryid, workflowid, resourcecategoryid)
values
(1, 1, 1);

insert into workflowresourcecategorymeta
(workflowresourcecategoryid, k, v, position)
values
(1, 'illuminaH2000.allowableUiField.readlength', '10:10;20:20;', 0);
insert into workflowresourcecategorymeta
(workflowresourcecategoryid, k, v, position)
values
(1, 'illuminaH2000.allowableUiField.readType', 'single:single;pair:pair;', 0);

insert into workflowsoftware
(workflowsoftwareid, workflowid, softwareid)
values
(1, 1, 1);
insert into workflowsoftware
(workflowsoftwareid, workflowid, softwareid)
values
(2, 1, 2);

## make a test job
insert into job
values
(1, 1, 1, 1, 'first job', now(), null, 1, now(), null); 

insert into sample 
values 
(1, 1, 1, 1, 1, 1, 1, 1, now(), 'name', 1, 1, now(), null);

insert into jobsample values (1, 1, 1, now(), 1); 


insert into state 
select 1, taskid, name, 'CREATED', now(), null, now(), 1 from task where iname = 'Start Job';
insert into statesample values (1, 1, 1);
insert into statejob values (1, 1, 1);


insert into job
values
(2, 1, 1, 1, 'second job', now(), null, 1, now(), null); 
insert into jobsample values (2, 2, 1, now(), 1);

insert into state 
select 100, taskid, name, 'CREATED', now(), null, now(), 1 from task where iname = 'Start Job';
insert into statesample values (100, 100, 1);
insert into statejob values (101, 100, 2);

