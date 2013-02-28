insert into wrole (id, rolename, name, domain) values
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

insert into roleset
(parentroleid, childroleid)
select 
id, id
from wrole;

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

insert into wuser (id, created, lastupdts, email, firstname, lastname, isactive, locale, login, password) VALUES      
(1, now(), now(), 'super@waspsystem.org', 'Super', 'User', 1, 'en_US', 'super', SHA1('user'));

insert into userrole (created, lastupdts, roleid, userid) VALUES
(now(), now(), 11, 1);

insert into lab (id, created, lastupdts, isactive, name, primaryuserid) VALUES
(1, now(), now(), 1, 'default lab', 1);

insert into labuser (created, lastupdts, labid, roleid, userid) VALUES
(now(), now(), 1, 6, 1);


