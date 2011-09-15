-- as root

GRANT USAGE ON *.* TO 'wasp'@'localhost';
DROP USER 'wasp'@'localhost';
DROP DATABASE IF EXISTS wasp;

create database wasp CHARACTER SET utf8 COLLATE utf8_general_ci;
;
create user wasp;

update mysql.user set host='localhost' where user='wasp';
update mysql.user set password=PASSWORD('waspV2') where user='wasp';

grant all on wasp.* to 'wasp'@'localhost';

flush privileges;

use wasp;
