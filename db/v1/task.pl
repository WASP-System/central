#!/usr/bin/perl 


use DBI;
use DBD::mysql;
use strict;
no strict qw|refs|;

our $db = {};

our $v1_dbh = DBI->connect("dbi:mysql:waspv1:localhost:3306", "waspv1", "waspV0ne") or die "Unable to connect: $DBI::errstr\n";

our $v2_dbh = DBI->connect("dbi:mysql:wasp:localhost:3306", "wasp", "waspV2") or die "Unable to connect: $DBI::errstr\n";


$v2_dbh->do("delete from statesample"); 
$v2_dbh->do("delete from statejob"); 
$v2_dbh->do("delete from state"); 
$v2_dbh->do("delete from task");
$v2_dbh->do("
insert into task
(taskid, iname)
values
(1, 'Start Job'),
(2, 'Quote Job'),
(3, 'PI Approval'),
(4, 'DA Approval'),
(5, 'Requote'),
(6, 'Send Invoice'),
(7, 'Receive Payment'),
(8, 'Receive Sample'),
(9, 'Create Library'),
(10, 'Assemble Lane'),
(11, 'Job/Create Flowcell'),
(12, 'Start Sequence'),
(13, 'Receive Files'),
(14, 'QC'),
(19, 'Finish Job'),

(41, 'Assay #1'),
(42, 'Assay #2'),
(43, 'Assay #3'),
(44, 'Assay #4'),
(45, 'Assay #5'),
(46, 'Assay #6'),
(47, 'Assay #7'),
(48, 'Assay #8'),
(49, 'Assay #9'),

(101, 'Flowcell/Create Flow Cell'),
(102, 'Flowcell/Add Library To Lane'),
(103, 'Flowcell/Amplicon'),
(104, 'Flowcell/Start Sequence'),
(105, 'Flowcell/Receive Files'),
(106, 'Flowcell/Finish Flowcell');
");

$v2_dbh->do("update task set name = iname");

$v2_dbh->do("insert into state 
(taskid, name, status, lastupduser) 
select t.taskid, t.name, 'LOADING', j.jobid
from task t, job j
where t.taskid = 1;");

$v2_dbh->do("insert into statejob
(stateid, jobid)
select stateid, lastupduser
from state
where taskid = 1;");

$v2_dbh->do("update state set lastupduser = null where taskid = 1;");
$v2_dbh->do("update state set status = 'FINAL' where taskid = 1;");

####################################################

$v2_dbh->do("insert into state 
(taskid, name, status, lastupduser) 
select t.taskid, t.name, 'LOADING', j.jobid
from task t, job j
where t.taskid = 2;");

$v2_dbh->do("insert into statejob
(stateid, jobid)
select stateid, lastupduser
from state
where taskid = 2;");

$v2_dbh->do("update state set lastupduser = null where taskid = 2;");
$v2_dbh->do("update state set status = 'FINAL' where taskid = 2;");

####################################################

$v2_dbh->do("insert into state 
(taskid, name, status, lastupduser) 
select t.taskid, t.name, 'LOADING', j.jobid
from task t, job j
where t.taskid = 3;");

$v2_dbh->do("insert into statejob
(stateid, jobid)
select stateid, lastupduser
from state
where taskid = 3;");

$v2_dbh->do("
  update state 
  set 
    status = 'FINAL' 
  where 
    status = 'LOADING' and
    taskid = 3 and
    exists (
      select 1
      from 
        statejob sj, acct_quote q, acct_quoteuser qu
      where 
        state.stateid = sj.stateid and
        sj.jobid = q.jobid and
        q.quoteid = qu.quoteid and 
        qu.roleid = 4 and
        qu.isapproved = 1 
    );
");
$v2_dbh->do("
  update state 
  set 
    status = 'NO' 
  where 
    status = 'LOADING' and
    taskid = 3 and
    exists (
      select 1
      from 
        statejob sj, acct_quote q, acct_quoteuser qu
      where 
        state.stateid = sj.stateid and
        sj.jobid = q.jobid and
        q.quoteid = qu.quoteid and 
        qu.roleid = 4 and
        qu.isapproved = 0 
    );
");

$v2_dbh->do("update state set status = 'CREATED' where status = 'LOADING' and taskid = 3;");

$v2_dbh->do("update state set lastupduser = null where taskid = 3;");

##########################################################

$v2_dbh->do("insert into state 
(taskid, name, status, lastupduser) 
select t.taskid, t.name, 'LOADING', j.jobid
from task t, job j
where t.taskid = 4;");

$v2_dbh->do("insert into statejob
(stateid, jobid)
select stateid, lastupduser
from state
where taskid = 4;");

$v2_dbh->do("
  update state 
  set 
    status = 'FINAL' 
  where 
    status = 'LOADING' and
    taskid = 4 and
    exists (
      select 1
      from 
        statejob sj, acct_quote q, acct_quoteuser qu
      where 
        state.stateid = sj.stateid and
        sj.jobid = q.jobid and
        q.quoteid = qu.quoteid and 
        qu.roleid = 6 and
        qu.isapproved = 1 
    );
");
$v2_dbh->do("
  update state 
  set 
    status = 'NO' 
  where 
    status = 'LOADING' and
    taskid = 4 and
    exists (
      select 1
      from 
        statejob sj, acct_quote q, acct_quoteuser qu
      where 
        state.stateid = sj.stateid and
        sj.jobid = q.jobid and
        q.quoteid = qu.quoteid and 
        qu.roleid = 6 and
        qu.isapproved = 0 
    );
");

$v2_dbh->do("update state set status = 'CREATED' where status = 'LOADING' and taskid = 4;");

$v2_dbh->do("update state set lastupduser = null where taskid = 4;");

####################################################

$v2_dbh->do("insert into state 
(taskid, name, status, lastupduser) 
select t.taskid, t.name, 'CANCELLED', j.jobid
from task t, job j
where t.taskid = 5;");

$v2_dbh->do("insert into statejob
(stateid, jobid)
select stateid, lastupduser
from state
where taskid = 5;");

$v2_dbh->do("update state set lastupduser = null where taskid = 5;");

####################################################

$v2_dbh->do("insert into state 
(taskid, name, status, lastupduser) 
select t.taskid, t.name, 'LOADING', j.jobid
from task t, job j
where t.taskid = 6;");

$v2_dbh->do("insert into statejob
(stateid, jobid)
select stateid, lastupduser
from state
where taskid = 6;");

$v2_dbh->do("
  update state 
  set 
    status = 'FINAL' 
  where 
    status = 'LOADING' and
    taskid = 6 and
    exists (
      select 1
      from 
        statejob sj, acct_invoice i
      where 
        state.stateid = sj.stateid and
        sj.jobid = i.jobid and
        i.amount > 0
    );
");

$v2_dbh->do("
  update state 
  set 
    status = 'FINAL' 
  where 
    status = 'LOADING' and
    taskid = 6 and
    exists (
      select 1
      from 
        statejob sj, acct_invoice i
      where 
        state.stateid = sj.stateid and
        sj.jobid = i.jobid and
        i.amount > 0
    );
");

my $rs = $v2_dbh->selectall_arrayref("
select state.stateid, sj.jobid, s_pi.status, s_da.status
from
  state,
  statejob sj,
  statejob sj_pi,
  state s_pi,
  statejob sj_da,
  state s_da
where
  state.status = 'LOADING' and
  state.taskid = 6 and
  state.stateid = sj.stateid and
  sj.jobid = sj_pi.jobid and
  sj_pi.stateid = s_pi.stateid and
  s_pi.taskid = 4 and
  sj.jobid = sj_da.jobid and
  sj_da.stateid = s_da.stateid and
  s_da.taskid = 3
");

foreach my $row (@$rs) {
  if ($row->[2] eq "NO" || $row->[3] eq "NO") { 
    $v2_dbh->do("
      update state 
      set 
        status = 'PARENTNO' 
      where 
        stateid = " . $row->[0] . ";"
    );
  } elsif($row->[2] eq "FINAL" && $row->[3] eq "FINAL") { 
    $v2_dbh->do("
      update state 
      set 
        status = 'CREATED' 
      where 
        stateid = " . $row->[0] . ";"
    );
  } else {
    $v2_dbh->do("
      update state 
      set 
        status = 'WAITING' 
      where 
        stateid = " . $row->[0] . ";"
    );
  }
}

####################################################
# Create Sample DNA
#

$v2_dbh->do("insert into state 
(taskid, name, status, lastupduser) 
select t.taskid, t.name, 'LOADING', j.jobid
from task t, job j
where t.taskid = 7;");

$v2_dbh->do("insert into statejob
(stateid, jobid)
select stateid, lastupduser
from state
where taskid = 7;");

$v2_dbh->do("
  update state 
  set 
    status = 'FINAL' 
  where 
    status = 'LOADING' and
    taskid = 7 and
    exists (
      select 1
      from 
        statejob sj, acct_invoice i
      where 
        state.stateid = sj.stateid and
        sj.jobid = i.jobid and
        i.amount > 0
    );
");

$rs = $v2_dbh->selectall_arrayref("
select s.stateid, sj.jobid, s_quote.status
from
  state s,
  statejob sj,
  statejob sj_quote,
  state s_quote
where
  s.status = 'LOADING' and
  s.taskid = 7 and
  s.stateid = sj.stateid and
  sj.jobid = sj_quote.jobid and
  sj_quote.stateid = s_quote.stateid and
  s_quote.taskid = 6
");

foreach my $row (@$rs) {
  if ($row->[2] eq "NO" || $row->[2] eq "PARENTNO" ) { 
    $v2_dbh->do("
      update state 
      set 
        status = 'PARENTNO' 
      where 
        stateid = " . $row->[0] . ";"
    );
  } elsif($row->[2] eq "FINAL") { 
    $v2_dbh->do("
      update state 
      set 
        status = 'CREATED' 
      where 
        stateid = " . $row->[0] . ";"
    );
  } else {
    $v2_dbh->do("
      update state 
      set 
        status = 'WAITING' 
      where 
        stateid = " . $row->[0] . ";"
    );
  }
}

$v2_dbh->do("update state set lastupduser = null where taskid = 7;");

####################################################
# Create Sample DNA
#

$v2_dbh->do("insert into state 
(taskid, name, status, lastupduser) 
select t.taskid, t.name, 'WAITING', s.sampleid
from task t, sample s
where t.taskid = 8 and s.typesampleid = 1;");

$v2_dbh->do("insert into statesample
(stateid, sampleid)
select stateid, lastupduser
from state
where taskid = 8;");

$v2_dbh->do("insert into statejob
(stateid, jobid)
select stateid, js.jobid
from state, sample s, jobsample js
where 
  state.taskid = 8 and
  state.lastupduser = s.sampleid and
  s.sampleid = js.sampleid
");

$v2_dbh->do("update state set lastupduser = null where taskid = 8;");

####################################################
# Create Sample Library
#

$v2_dbh->do("insert into state
(taskid, name, status, lastupduser) 
select t.taskid, t.name, 'WAITING', s.sampleid
from task t, sample s
where t.taskid = 9 and s.typesampleid = 3;");

$v2_dbh->do("insert into statesample
(stateid, sampleid)
select stateid, lastupduser
from state
where taskid = 9;");

$v2_dbh->do("insert into statejob
(stateid, jobid)
select stateid, js.jobid
from state, sample s, jobsample js
where 
  state.taskid = 9 and
  state.lastupduser = s.sampleid and
  s.sampleid = js.sampleid
");

$v2_dbh->do("insert into statejob
(stateid, jobid)
select stateid, js.jobid
from state, sample s, samplesource ss, jobsample js
where 
  state.taskid = 9 and
  state.lastupduser = s.sampleid and
  s.sampleid = ss.sampleid and
  ss.source_sampleid = js.sampleid
group by stateid, js.jobid
having count(*) >= 1
");

####################################################
# Assemble Lane
#

$v2_dbh->do("insert into state
(taskid, name, status, lastupduser) 
select t.taskid, t.name, 'WAITING', s.sampleid
from task t, sample s
where t.taskid = 10 and s.typesampleid = 4;");

$v2_dbh->do("insert into statesample
(stateid, sampleid)
select stateid, lastupduser
from state
where taskid = 10;");

$v2_dbh->do("insert into statejob
(stateid, jobid)
select stateid, js.jobid
from state, sample s, jobsample js
where 
  state.taskid = 10 and
  state.lastupduser = s.sampleid and
  s.sampleid = js.sampleid
group by stateid, js.jobid
having count(*) >= 1;
");

####################################################
# Flow cell Lane
#

$v2_dbh->do("insert into state
(taskid, name, status, lastupduser) 
select t.taskid, t.name, 'WAITING', s.sampleid
from task t, sample s
where t.taskid = 11 and s.typesampleid = 5;");

$v2_dbh->do("insert into statesample
(stateid, sampleid)
select stateid, lastupduser
from state
where taskid = 11;");

$v2_dbh->do("insert into statejob
(stateid, jobid)
select stateid, js.jobid
from state, sample s, jobsample js
where 
  state.taskid = 11 and
  state.lastupduser = s.sampleid and
  s.sampleid = js.sampleid
group by stateid, js.jobid
having count(*) >= 1;
");

####################################################

