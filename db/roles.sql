insert into role (roleId, rolename, name, domain) values
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