-- --
-- simulate pi/da/boneshaker approval
update state set status = "APPROVED" where name like '%Appr%';


-- --
-- simulate quote job
update state set status = "QUOTED" where name like '%Quote%';

-- --
-- simulate sample received  (should set isreceived flag and play w/ that
update state set status = "RECEIVED" where name like '%Receiv%' and status != 'FINAL';


-- --
-- simulates library creation (for dna A)
insert into sample
values
(3, 3, 2, 1, 1, 1, 1, 1, now(), 'library A', 1, 1, now(), null);

insert into jobsample values (4, 2, 3, now(), 1);

update state set status = "MADE" where name like '%Create Library%' and status != 'FINAL';



-- --
-- simulates flowcell creation
insert into sample
values
(14, 5, 3, null, 1, null, 1, 1, now(), 'flowcell A', 1, 1, now(), null);
insert into sample
values
(15, 4, null, null, 1, null, 1, 1, now(), 'flowcell lane A/1', 1, 1, now(), null),
(16, 4, null, null, 1, null, 1, 1, now(), 'flowcell lane A/2', 1, 1, now(), null),
(17, 4, null, null, 1, null, 1, 1, now(), 'flowcell lane A/3', 1, 1, now(), null),
(18, 4, null, null, 1, null, 1, 1, now(), 'flowcell lane A/4', 1, 1, now(), null),
(19, 4, null, null, 1, null, 1, 1, now(), 'flowcell lane A/5', 1, 1, now(), null),
(20, 4, null, null, 1, null, 1, 1, now(), 'flowcell lane A/6', 1, 1, now(), null),
(21, 4, null, null, 1, null, 1, 1, now(), 'flowcell lane A/7', 1, 1, now(), null),
(22, 4, null, null, 1, null, 1, 1, now(), 'flowcell lane A/8', 1, 1, now(), null);


insert into samplesource
(sampleid, source_sampleid, multiplexindex, lastupduser, lastupdts)
select s1.sampleid, s2.sampleid, s2.sampleid-14, 1, now()
from sample s1, sample s2 
where s1.sampleid = 14 and s2.sampleid in (15,16,17,18,19,20,21,22);

insert into state
values
(1010, 4, 'Sample Flowcell Task', 'CREATED', now(), null, now(), 1); 
insert into statesample
(stateid, sampleid)
values
(1010, 14);


-- --
-- simulate assign value to flowcell

-- --
-- simulate start amplicon run 

-- --
-- simulate get amplicon results

-- --
-- simpulte start illumina run 

-- -- 
-- simulate get illumina results

-- -- 
-- simulate illumina qc screen ok

-- --
-- simulate 

