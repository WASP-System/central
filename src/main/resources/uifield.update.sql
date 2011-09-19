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