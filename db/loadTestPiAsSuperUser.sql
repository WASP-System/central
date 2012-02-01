insert into user (userid, login, password, email, firstname, lastname, isactive) values (5, 'testpi', sha1('abc123'), 'andrew.mclellan@einstein.yu.edu', 'Test', 'PI', 1);
insert into `userrole` (`userroleid`, `userid`, `roleid`, `lastupdts`, `lastupduser`) values (1, 5, 11, CURDATE(), 0);
