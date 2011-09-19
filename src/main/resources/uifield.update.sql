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
 
 