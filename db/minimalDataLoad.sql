SET FOREIGN_KEY_CHECKS = 0;

INSERT INTO `wrole` (`id`, `created`, `updated`, `uuid`, `domain`, `name`, `rolename`, `lastupdatebyuser`)
VALUES
	(1, NOW(), NOW(), X'2C6D49F3A5D84EEDB87E3C22025B44BD', 'system', 'Facilities Manager', 'fm', 0),
	(2, NOW(), NOW(), X'0589FFABBD55490BA1343DC8EFEF8BCC', 'system', 'System Administrator', 'sa', 0),
	(3, NOW(), NOW(), X'F2DB0D95263F4D339BD1E78D3AA7A604', 'system', 'General Administrator', 'ga', 0),
	(4, NOW(), NOW(), X'769766BF223444C887D5C10E12490FDA', 'department', 'Department Administrator', 'da', 0),
	(5, NOW(), NOW(), X'3151D7B3F0374B2092BFF606F4D12828', 'system', 'Facilities Tech', 'ft', 0),
	(6, NOW(), NOW(), X'84F5E255386A4A34A24C2C4D1D031C73', 'lab', 'Primary Investigator', 'pi', 0),
	(7, NOW(), NOW(), X'8033DF908D8446A69A24068D78ECC9AA', 'lab', 'Lab Manager', 'lm', 0),
	(8, NOW(), NOW(), X'0EA1959570574DBDA6787B8C56F4FD19', 'lab', 'Lab Member', 'lu', 0),
	(9, NOW(), NOW(), X'DBC4B5CA385A4C888C65305DC28A2F7B', 'job', 'Job Submitter', 'js', 0),
	(10, NOW(), NOW(), X'CB7CDC71FBD64FA0BE547E87A0B7BCA4', 'job', 'Job Viewer', 'jv', 0),
	(11, NOW(), NOW(), X'7B53E590C5BD47ADA7100496C5A75E1B', 'system', 'Super User', 'su', 0),
	(12, NOW(), NOW(), X'3A8A9BE81E09406FB2D291CCDA4319EE', 'lab', 'Lab Member Inactive', 'lx', 0),
	(13, NOW(), NOW(), X'776EA13A2B5F48FD9A567A87A0DBCB52', 'lab', 'Lab Member Pending', 'lp', 0),
	(14, NOW(), NOW(), X'A01EE1989D9E4768925FF5FD795E2535', 'jobdraft', 'Job Drafter', 'jd', 0),
	(15, NOW(), NOW(), X'D656858CDB434502B438B4A3A2724D00', 'user', 'User', 'u', 0),
	(16, NOW(), NOW(), X'D8A80927B7B645C2A5DF14D1605D838B', 'system', 'wasp', 'w', 0);



INSERT INTO `roleset` (`id`, `created`, `updated`, `uuid`, `childroleid`, `parentroleid`, `lastupdatebyuser`)
VALUES
	(1, NOW(), NOW(), X'1F3BC89D790D483D9B703B2CA82B2C6D', 4, 4, 1),
	(2, NOW(), NOW(), X'EF8575D1483B44A6879F249CA9DA681F', 1, 1, 1),
	(3, NOW(), NOW(), X'A0B5B4E7AF7346D392AC5B3F06C04449', 5, 5, 1),
	(4, NOW(), NOW(), X'94F83671A4264D3C864E9713924C0DF7', 3, 3, 1),
	(5, NOW(), NOW(), X'1A0C7E804D004A1FA8E5DE09B89B88AA', 14, 14, 1),
	(6, NOW(), NOW(), X'64861C4948D24D94AD6DF7773E5F200B', 9, 9, 1),
	(7, NOW(), NOW(), X'9BF2256553964E06A666A81D8ADE1F5A', 10, 10, 1),
	(8, NOW(), NOW(), X'08F846980FC948AF93174913346885B0', 7, 7, 1),
	(9, NOW(), NOW(), X'FD02739E15D4486FBA2F48DB9D7320DE', 13, 13, 1),
	(10, NOW(), NOW(), X'D93FE29D8A8D4F2997F9D08F6B8323C4', 8, 8, 1),
	(11, NOW(), NOW(), X'043E736B427A46949275C1A34F3F05DD', 12, 12, 1),
	(12, NOW(), NOW(), X'6C4806B4D53B4A6B9B30F6B68C12CDF9', 6, 6, 1),
	(13, NOW(), NOW(), X'1C09C0C9469B4C57BC2504C356FF6A10', 2, 2, 1),
	(14, NOW(), NOW(), X'4C60D6149C7A4561A4D61DFC5C38A0B0', 11, 11, 1),
	(15, NOW(), NOW(), X'AAE6FBF24E68404C99DBB6FAA1ED29F9', 15, 15, 1),
	(16, NOW(), NOW(), X'BECEBCB0748746EBB7A71474A21DA7BC', 5, 1, 1),
	(17, NOW(), NOW(), X'FE10A15E53B248DEA68EE59933AF4C90', 7, 6, 1),
	(18, NOW(), NOW(), X'8F5CE4B16BAC4FAD9C4AE9E19CCA98A8', 8, 6, 1),
	(19, NOW(), NOW(), X'F7D921CB4E4241CAB16AA23394199A18', 8, 7, 1),
	(20, NOW(), NOW(), X'864E384116E24490A0D4C765BEE5AE6B', 10, 9, 1),
	(21, NOW(), NOW(), X'E9D220F15ECC448EA1D5710457B5F770', 1, 11, 1),
	(22, NOW(), NOW(), X'0DF0F307CDC545289315F769DB07DA9B', 2, 11, 1),
	(23, NOW(), NOW(), X'BC506712D00E4ECDA02BC1DB73FD985A', 3, 11, 1),
	(24, NOW(), NOW(), X'19AE1C19D4324547AFF37256CEFD023E', 5, 11, 1),
	(25, NOW(), NOW(), X'E9203A9BEF8F44468A9A0365D18EE52A', 16, 16, 1);


INSERT INTO `wuser` (`id`, `created`, `updated`, `uuid`, `email`, `firstname`, `isactive`, `lastname`, `locale`, `login`, `password`, `lastupdatebyuser`)
VALUES (1, NOW(), NOW(), X'A4DB49223B9346E3ABDAB2D1C2DD1311', 'wasp@einstein.yu.edu', 'Wasp', 0, 'System', 'en_US', 'wasp', SHA1('user'), 1);
UPDATE `wuser` SET id=0 WHERE id=1; -- cannot insert a uderId of 0 directly
INSERT INTO `wuser` (`id`, `created`, `updated`, `uuid`, `email`, `firstname`, `isactive`, `lastname`, `locale`, `login`, `password`, `lastupdatebyuser`)
VALUES (1, NOW(), NOW(), X'1B9BF789DF334FC98C3CF17C1F47B184', 'super@super.com', 'Super', 1, 'User', 'en_US', 'super', SHA1('user'), 1);


INSERT INTO `userrole` (`id`, `created`, `updated`, `uuid`, `roleid`, `userid`, `lastupdatebyuser`)
VALUES
	(1, NOW(), NOW(), X'E4098C7735FC47099BD28EBC1BD8C179', 16, 0, 1),
	(2, NOW(), NOW(), X'7F8A8057033D4407B9ED84F4AFBD47B4', 11, 1, 1);
	


INSERT INTO `lab` (`id`, `created`, `updated`, `uuid`, `departmentid`, `isactive`, `name`, `primaryuserid`, `lastupdatebyuser`)
VALUES
	(1, NOW(), NOW(), X'0EAC44BADD344B768BFD44DBFB52603A', 1, 1, 'Default lab', 1, 1);


INSERT INTO `labuser` (`id`, `created`, `updated`, `uuid`, `labid`, `roleid`, `userid`, `lastupdatebyuser`)
VALUES
	(1, '2012-05-23 15:55:46', '2013-03-12 09:42:14', X'74DDE5796D7E45C48D21CCBA8C64CCB6', 1, 6, 1, 1);
	
INSERT INTO `department` (`id`, `created`, `updated`, `uuid`, `isactive`, `isinternal`, `name`, `lastupdatebyuser`)
VALUES
	(1, '2013-03-12 12:26:58', '2013-03-12 12:26:59', X'4FC2914069F24397B5AD0A96B8721F2D', 1, 0, 'External', 1);
	
INSERT INTO `usermeta` (`id`, `created`, `updated`, `uuid`, `k`, `position`, `rolevisibility`, `v`, `userid`, `lastupdatebyuser`)
VALUES
	(1, '2012-05-23 17:23:00', '2013-03-12 12:26:59', X'E5ADBC3510164D58A716AEE1D5B8D3FF', 'user.title', 0, NULL, 'Mr', 1, 1),
	(2, '2012-05-23 17:23:00', '2013-03-12 12:26:59', X'35CFE929937C47AF86B8628A75418AFD', 'user.institution', 0, NULL, 'Wasp', 1, 1),
	(3, '2012-05-23 17:23:00', '2013-03-12 12:26:59', X'D44BE3A2D2F54FA692AD78BA44E6F6FE', 'user.departmentId', 0, NULL, '1', 1, 1),
	(4, '2012-05-23 17:23:00', '2013-03-12 12:26:59', X'7E7D3CBC518A4CC6A0B0C11D22B3FFB5', 'user.building_room', 0, NULL, 'N/A', 1, 1),
	(5, '2012-05-23 17:23:00', '2013-03-12 12:26:59', X'83680E5B0AE34328BF14F838A0E0ED4A', 'user.address', 0, NULL, 'N/A', 1, 1),
	(6, '2012-05-23 17:23:00', '2013-03-12 12:26:59', X'4B306C43A9E142AEA287A83746FB5D66', 'user.city', 0, NULL, 'N/A', 1, 1),
	(7, '2012-05-23 17:23:00', '2013-03-12 12:26:59', X'F4FE38B753A94FCE8B91502B5447296E', 'user.state', 0, NULL, 'NY', 1, 1),
	(8, '2012-05-23 17:23:00', '2013-03-12 12:26:59', X'9B968E1DF17740539A413E87196ABBA0', 'user.country', 0, NULL, 'US', 1, 1),
	(9, '2012-05-23 17:23:00', '2013-03-12 12:26:59', X'AE2E6A0A8A784941B402E03F4986A0FC', 'user.zip', 0, NULL, '00000', 1, 1),
	(10, '2012-05-23 17:23:00', '2013-03-12 12:26:59', X'DB0C8AFF80B24C4F8ABF71472C6F5E64', 'user.phone', 0, NULL, '000-000-0000', 1, 1),
	(11, '2012-05-23 17:23:00', '2013-03-12 12:26:59', X'50838BCC5CE141D5A480B054E4360BB3', 'user.fax', 0, NULL, '000-000-0000', 1, 1),
	(12, '2012-05-23 17:23:00', '2013-03-12 12:26:59', X'6E13313212C340C4AAA02DD9C19BB885', 'user.title', 0, NULL, 'Mr', 0, 1),
	(13, '2012-05-23 17:23:00', '2013-03-12 12:26:59', X'33FDDCA4EC3346F686E2D880AE9ADD3D', 'user.institution', 0, NULL, 'Wasp', 0, 1),
	(14, '2012-05-23 17:23:00', '2013-03-12 12:26:59', X'F3586BCDB42F42BD8DCF96DE38375D22', 'user.departmentId', 0, NULL, '1', 0, 1),
	(15, '2012-05-23 17:23:00', '2013-03-12 12:26:59', X'ACA76B8C2D7C49C4B254B4C1CD86C640', 'user.building_room', 0, NULL, 'N/A', 0, 1),
	(16, '2012-05-23 17:23:00', '2013-03-12 12:26:59', X'E6C44F62B021458C925E3C0C9B29A9F1', 'user.address', 0, NULL, 'N/A', 0, 1),
	(17, '2012-05-23 17:23:00', '2013-03-12 12:26:59', X'9FE1D84054F94F6AA5855CE082CFD675', 'user.city', 0, NULL, 'N/A', 0, 1),
	(18, '2012-05-23 17:23:00', '2013-03-12 12:26:59', X'B04816B731EC41A8ACE6D403096E2B6C', 'user.state', 0, NULL, 'NY', 0, 1),
	(19, '2012-05-23 17:23:00', '2013-03-12 12:26:59', X'6CD39EADCD5947248E5CB0110860068E', 'user.country', 0, NULL, 'US', 0, 1),
	(20, '2012-05-23 17:23:00', '2013-03-12 12:26:59', X'685F9541D4334A63BD104B8E5F3453E7', 'user.zip', 0, NULL, '00000', 0, 1),
	(21, '2012-05-23 17:23:00', '2013-03-12 12:26:59', X'47A25DBCAB754F2FB8435E7C3299F0A3', 'user.phone', 0, NULL, '000-000-0000', 0, 1),
	(22, '2012-05-23 17:23:00', '2013-03-12 12:26:59', X'55D76797BDAD4FE7BD4617D189923731', 'user.fax', 0, NULL, '000-000-0000', 0, 1);



SET FOREIGN_KEY_CHECKS = 1;

