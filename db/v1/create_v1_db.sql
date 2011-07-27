create database waspv1;
create user waspv1;
update mysql.user set host='localhost' where user='waspv1';
update mysql.user set password=PASSWORD('waspV0ne') where user='waspv1';
grant all on wasp.* to 'waspv1'@localhost;
grant all on waspv1.* to 'waspv1'@localhost;
flush privileges;
