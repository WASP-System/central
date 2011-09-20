-- 1. insert 

-- First delete the old one with the same key if it exists
delete from uifield where area='user' and name='fancypropertyname' and  attrName='label';


 -- then add the property
 insert into uifield(locale,area,name,attrName,attrValue,lastupduser)
 select 'en_US','user','fancypropertyname','label','Fancy Name',userId
 from user where login='superuser'; 

 
 
 -- 2. update
 update uifield set attrValue='Updated Value' where area='user' and name='fancypropertyname' and  attrName='label';
 
 
 
 -- 3. delete
 delete from uifield where area='user' and name='fancypropertyname' and  attrName='label';
 
 
delete from uifield where area='userPending' and name='state' and attrName='error';
insert into uifield(locale,area,name,attrName,attrValue,lastupduser) select 'en_US','userPending','state','error','State cannot be empty',userId from user where login='superuser'; 

delete from uifield where area='userPending' and name='state' and attrName='constraint';
insert into uifield(locale,area,name,attrName,attrValue,lastupduser) select 'en_US','userPending','state','constraint','NotEmpty',userId from user where login='superuser'; 

delete from uifield where area='user' and name='detail' and (attrname='label' or attrname='metaposition');

delete from uifield where area='userPending' and name='department' and attrName='control';
insert into uifield(locale,area,name,attrName,attrValue,lastupduser) select 'en_US','userPending','department','control','select:\${department}:departmentId:name',userId from user where login='superuser';

update uifield set name='primaryuserid', attrValue='PI Wasp Username' where area='userPending' and name='primaryuseremail' and attrName='label';
update uifield set name='primaryuserid', attrValue='PI Wasp Username cannot be empty' where area='userPending' and name='primaryuseremail' and attrName='error';
update uifield set name='primaryuserid_notvalid', attrValue='Not an active registered PI Username' where area='userPending' and name='primaryuseremail_notvalid' and attrName='error';
update uifield set name='primaryuserid', attrValue='isValidPiId' where area='userPending' and name='primaryuseremail' and attrName='constraint';
update uifield set name='primaryuserid', attrValue='1' where area='userPending' and name='primaryuseremail' and attrName='metaposition';

delete from uifield where area='userPending' and name='captcha' and attrName='error';
insert into uifield(locale,area,name,attrName,attrValue,lastupduser) select 'en_US','userPending','captcha','error','Captcha text incorrect',userId from user where login='superuser';

delete from uifield where area='userPending' and name='captcha' and attrName='label';
insert into uifield(locale,area,name,attrName,attrValue,lastupduser) select 'en_US','userPending','captcha','label','Captcha text',userId from user where login='superuser';
