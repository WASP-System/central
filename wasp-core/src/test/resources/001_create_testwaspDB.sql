-- as root

GRANT USAGE ON *.* TO 'testwasp'@'localhost';
DROP USER 'testwasp'@'localhost';
DROP DATABASE IF EXISTS testwasp;

create database testwasp CHARACTER SET utf8 COLLATE utf8_general_ci;
create user testwasp;

update mysql.user set host='localhost' where user='testwasp';
update mysql.user set password=PASSWORD('testwaspV2') where user='testwasp';

grant all on testwasp.* to 'testwasp'@'localhost';

flush privileges;

use testwasp;
