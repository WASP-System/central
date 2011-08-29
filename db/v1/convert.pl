#!/usr/bin/perl 


#
# WASP v1 to v2 database conversion
# @author echeng
#
#

use DBI;
use DBD::mysql;
use strict;
no strict qw|refs|;

our $db = {};

our $v1_dbh = DBI->connect("dbi:mysql:waspv1:localhost:3306", "waspv1", "waspV0ne") or die "Unable to connect: $DBI::errstr\n";
 
our $v2_dbh = DBI->connect("dbi:mysql:wasp:localhost:3306", "wasp", "waspV2") or die "Unable to connect: $DBI::errstr\n";


my $i = 0;
our $out = "";


###########################################################
# USER table

my $users = $v1_dbh->selectall_hashref(qq|
  select u.*, p.password 
  from 
    user u
    left outer join password p on
      (u.user_id = p.user_id) 
|, "user_id");

foreach my $user (sort keys %$users) {
  my $u = $users->{$user};

  sqlescape($u);

  # sets username if null
  $u->{'WASP_username'} ||= $u->{'email'};

  $out .= qq|
    insert into user
    (userid, login, password, email, firstname, lastname, isactive)
    values
    ($u->{'user_id'}, '\L$u->{'WASP_username'}\E', '$u->{'password'}', '\L$u->{'email'}\E', '$u->{'first_name'}', '$u->{'last_name'}', $u->{'active'});
  |; 

  $out .= meta("user", [qw|title institution department building_room address city state country zip phone fax|], 'user_id', $u);

}

print $out;

###########################################################
# LAB table

$out = "";
my $labs = $v1_dbh->selectall_hashref(qq|
  select l.*
  from 
    lab l
|, "lab_id");

foreach my $lab (sort keys %$labs) {

  next if $lab == 106; # duplicate lab. 
  next if $lab == 82; # duplicate lab. 

  my $l = $labs->{$lab};
  sqlescape($l);

  $l->{'dept'}  = 2; 
  if ($l->{'internal_external_lab'} eq 'INTERNAL') {
    $l->{'dept'}  = 1; 
  }

  $out .= qq|
    insert into lab
    (labid, departmentid, name, primaryuserid, isactive)
    values
    ($l->{'lab_id'}, $l->{'dept'}, '$l->{'lab_name'}', $l->{'pi_id'}, 1);
  |; 

  $out .= meta("lab", [qw|internal_external_lab building_room phone|], 'lab_id', $l);
}

print $out; 
$out = "";


my $lbls = $v1_dbh->selectall_hashref(qq|
  select lbl.lab_id, bl.*
  from 
    lab_billing_location lbl, billing_location bl
  where
    lbl.billing_location_id = bl.billing_location_id 
|, "billing_location_id");

foreach my $lbl (sort keys %$lbls) {
  next if $lbl == 2;

  my $l = $lbls->{$lbl};

  sqlescape($l); 

  my $metapos = 100;
  $out .= meta("lab", [qw|billing_dept_name contact_name institution_name building_room address city state countyr zip email phone fax secondary_contact comments|], 'lab_id', $l, 'billing');

}

print $out;
$out = "";

my $lrcs = $v1_dbh->selectall_hashref(qq|
  select lrc.lab_id * 10000000 + lrc.research_center_id a, lrc.lab_id, rc.*
  from 
    lab_research_center lrc, research_center rc
  where
    lrc.research_center_id = rc.research_center_id 
|, "lab_id"); # avoids duplicate entry
# |, "a");

foreach my $lrc (sort keys %$lrcs) {

  my $l = $lrcs->{$lrc};

  sqlescape($l);

  $out .= meta("lab", [qw|research_center_name user_list_location|], 'lab_id', $l, 'researchcenter');

}
print $out;
$out = "";

###########################################################
# LABUSER table

my $labusers = $v1_dbh->selectall_hashref(qq|
  select lab_id * 10000000 + user_id a, l.*
  from 
    lab_user l
  where
    active = 1
|, "a");

foreach my $lu (sort keys %$labusers) {
  my $l = $labusers->{$lu};

  sqlescape($l);

  $out .= qq|
    insert into labuser
    (labid, userid, roleid)
    values
    ($l->{'lab_id'}, $l->{'user_id'}, 8);
  |; 
}


#---
# sets pis

$out .= qq|
insert into labuser
(labid, userid, roleid)
select 
  l.labid, l.primaryuserid, 8
from 
  lab l
  left outer join labuser lu
    on (l.labid = lu.labid) 
where
  lu.labid is null;

update labuser
set roleid = 6
where 
  exists (
    select 1 
    from lab l
    where 
      labuser.labid = l.labid and 
      labuser.userid = l.primaryuserid
  );
|;


print $out;
$out = ""; 



###########################################################
# PROJECT table

if (0==1) {
my $projects = $v1_dbh->selectall_hashref(qq|
  select p.*, u.WASP_username, u.first_name, u.last_name
  from 
    project p, user u 
  where
    p.user_id = u.user_id
|, "project_id");

my $pname = {};
foreach my $project (sort keys %$projects) {
  my $p = $projects->{$project};

  sqlescape($p);

  if (! defined($pname->{$p->{'project_name'}})) {
    $pname->{$p->{'project_name'}} = 1;   
  } else {
    $pname->{$p->{'project_name'}}++;

    $p->{'project_name'} .= " (" . $pname->{$p->{'project_name'}}  . ")";
  }

  $p->{'WASP_username'} =~ tr/A-Z/a-z/; # lc username

  $out .= qq|
    insert into project
    (projectid, labid, name)
    values
    ($p->{'project_id'}, $p->{'lab_id'}, '$p->{'project_name'}');
  |; 

  $out .= meta("project", [qw|user_id WASP_username first_name last_name|], 'project_id', $p);

}

print $out;
$out = "";
}

###########################################################
# RESOURCE table

my $resources = $v1_dbh->selectall_hashref(qq|
  select m.*
  from 
    machine m
|, "machine_name");

my $i = 0;
foreach my $resource (sort keys %$resources) {
  my $r = $resources->{$resource};
  $i++;
  $r->{'i'} = $i;

  sqlescape($r);

  $out .= qq|
    insert into resource
    (resourceid, platform, name, typeresourceid)
    values
    ($i, '$r->{'assay_platform'}', '$r->{'machine_name'}', 1);
  |; 

  $out .= meta("resource", [qw|assay_platform machine_type serial_number commission_date decommission_date comments|], 'i', $r);

}

print $out;
$out = "";

 
###########################################################
# WORKFLOW table

my $workflows = $v1_dbh->selectall_hashref(qq|
  select a.*
  from 
    assay a
|, "assay_id");

foreach my $workflow (sort keys %$workflows) {
  my $w = $workflows->{$workflow};

  $w->{'assay_name'} = "CONTROL (SEQ)" if ($workflow == 14);
  $w->{'assay_name'} = "CONTROL (MICROARRAY)" if ($workflow == 15);

  $w->{'iname'} = "\L$w->{'assay_name'}";
  $w->{'iname'} =~ s/[^A-Za-z ]//g;
  $w->{'iname'} =~ s/ (.)/\U$1/g;

  sqlescape($w); 

  $out .= qq|
    insert into workflow
    (workflowid, iname, name, createts, isactive)
    values
    ($w->{'assay_id'}, '$w->{'iname'}', '$w->{'assay_name'}', '$w->{'created_date'}', $w->{'active'});
  |; 

  $out .= meta("workflow", [qw|general_assay_type primer_adaptor_type|], 'assay_id', $w);

}

print $out;
$out = "";

###########################################################
# JOB table

my $jobs = $v1_dbh->selectall_hashref(qq|
  select j.*
  from 
    job j
|, "job_id");

my $jname = {};
foreach my $job (sort keys %$jobs) {
  my $j = $jobs->{$job};

  $j->{'job_name'} =~ s/^\s*|\s*$//g;

  if (! defined($jname->{$j->{'job_name'}})) {
    $jname->{$j->{'job_name'}} = 1;   
  } else {
    $jname->{$j->{'job_name'}}++;

    $j->{'job_name'} .= " (" . $jname->{$j->{'job_name'}}  . ")";
  }

  sqlescape($j);

  $out .= qq|
    insert into job
    (jobid, labid, userid, workflowid, name, createts, isactive)
    values
    ($j->{'job_id'}, $j->{'lab_id'}, $j->{'user_id'},  $j->{'assay_id'}, '$j->{'job_name'}', '$j->{'submission_date'}', 1);
  |; 

  $out .= meta("job", [qw| assay_platform read_length end_read_type multiplexing required_result_date priority prep_procedure kit experimental_description comments number_of_samples number_of_lanes number_of_microarrays po_number check_number grant_info_id contact_id billing_location_id research_centers_with_discounts pipeline_compatible completion_status completion_date date_esf_terminated reason_esf_terminated |], 'job_id', $j);

}

print $out;
$out = "";


###########################################################
# SAMPLE table
my $samples = $v1_dbh->selectall_hashref(qq|
  select s.*, esf.user_id receive_by_userid
  from 
    sample s 
    left outer join esf_staff esf
      on (s.sample_received_by_id = esf.esf_staff_id)
|, "sample_id");

foreach my $sample (sort keys %$samples) {
  my $s = $samples->{$sample};

  sqlescape($s);

  $s->{'receive_by_userid'} ||= 'null';

  # received set. 
  $s->{'is_received'} = 1;

  # ED209 - submitter_labid, submitter_jobid
  #  
  
  $out .= qq|
    insert into sample
    ( sampleid, typesampleid, submitter_labid, submitter_userid, submitter_jobid, 
       name, isreceived, receiver_userid, receivedts, isgood, isactive)
    values
    ($s->{'sample_id'}, 1, $s->{'lab_id'}, $s->{'user_id'}, null, 
      '$s->{'sample_name'}', $s->{'is_received'}, $s->{'receive_by_userid'}, '$s->{'sample_date_recieved'}', null, 1);
  |; 

  $out .= meta("sample", [qw|material_provided area_on_flx_chamber reference_genome_id species_id sex fragment_size fragment_size_sd amount concentration 260_280 260_230 volume buffer sample_type antibody_id enrich_primer_pair_id comments sample_label|], 'sample_id', $s);

}


my $libraries = $v1_dbh->selectall_hashref(qq|
  select l.*, s.lab_id, s.user_id submit_by_userid, esf.user_id create_by_userid
  from 
    library l 
    left outer join esf_staff esf
      on (l.library_created_by_id = esf.esf_staff_id)
    left outer join sample s
      on (l.sample_id = s.sample_id)
|, "library_id");
foreach my $library (sort keys %$libraries) {
  my $l = $libraries->{$library};

  sqlescape($l);

  $l->{'create_by_userid'} ||= $l->{'submit_by_userid'} || 'null';
  $l->{'i'} = 10000 + $l->{'library_id'};

  $out .= qq|
    insert into sample
    ( sampleid, typesampleid, submitter_labid, submitter_userid, submitter_jobid, 
       name, isreceived, receiver_userid, receivedts, isgood, isactive)
    values
    ($l->{'i'}, 3, $l->{'lab_id'}, $l->{'create_by_userid'}, null, 
      '$l->{'library_name'}', 0, 0, null, null, 1);

    insert into samplesource
    ( sampleid, multiplexindex, source_sampleid)
    values
    ($l->{'i'}, 0, $l->{'sample_id'});
  |; 

  $out .= meta("sample", [qw|librarysize library_size_sd adaptor_sequence_id rn_strand_method_id rna_strand library_created_by_submitter library_create_date library_final_status library_success|], 'i', $l, 'library');

}
print $out;
$out = "";

#----------------------------------------------------------
# Lane
#
my $lanes = $v1_dbh->selectall_hashref(qq|
  select l.*, flow_cell_name fcname, esf.user_id create_by_userid, ll.job_id job_id, j.lab_id
  from 
    lane l 
    left outer join flow_cell  fc
      on (l.flow_cell_id = fc.flow_cell_id)
    left outer join lane_library  ll
      on (l.lane_id = ll.lane_id)
    left outer join job j  
      on (ll.job_id = j.job_id)
    left outer join esf_staff esf
      on (fc.start_esf_staff_id = esf.esf_staff_id)
|, "lane_id");

foreach my $lane (sort keys %$lanes) {
  my $l = $lanes->{$lane};

  sqlescape($l);

  $l->{'create_by_userid'} ||= 'null';
  $l->{'i'} = 20000 + $l->{'lane_id'};

  $l->{'lab_id'} ||=1; # TODO, resolve the ones that don't join
  $l->{'job_id'} ||= "null";

  $out .= qq|
    insert into sample
    ( sampleid, typesampleid, submitter_labid, submitter_userid, submitter_jobid, 
       name, isreceived, receiver_userid, receivedts, isgood, isactive)
    values
    ($l->{'i'}, 4, $l->{'lab_id'}, $l->{'create_by_userid'}, null,
      '$l->{'fcname'} / $l->{'lane_number'}', 0, 0, null, null, 1);
  |;

  $out .= meta("sample", [qw|flow_cell_id lane_number control_name control_pmol_applied actual_read_length lane_success comments|], 'i', $l, 'lane');

}
print $out;
$out = "";

###########################################################



my $jobsamples = $v1_dbh->selectall_hashref(qq|
  select job_id * 100000 + sample_id a, j.*
  from 
    job_sample j
|, "a");

my $i = 0;
foreach my $jobsample (sort keys %$jobsamples) {
  $i++;
  my $j = $jobsamples->{$jobsample};
  $j->{i} = $i;

  sqlescape($j);

  $out .= qq|
    insert into jobsample
    (jobsampleid, jobid, sampleid)
    values
    ($i, $j->{'job_id'}, $j->{'sample_id'});
  |; 

  # WHAT ARE THESE FIELDS?
  $out .= meta("jobsample", [qw|coverage multiplex process_by_pipeline|], 'i', $j);
}


my $jobsamples = $v1_dbh->selectall_hashref(qq|
  select job_id * 100000 + library_id a, job_id, l.*
  from 
    job_sample j, library l
  where
   l.sample_id = j.sample_id
|, "a");

foreach my $jobsample (sort keys %$jobsamples) {

  my $j = $jobsamples->{$jobsample};
  $j->{'lid'} = $j->{'library_id'} + 10000;


  sqlescape($j);

  $out .= qq|
    insert into jobsample
    (jobid, sampleid)
    values
    ($j->{'job_id'}, $j->{'lid'});
  |; 

}

print $out;
$out = "";

my $llanes = $v1_dbh->selectall_hashref(qq|
  select ll.lane_id * 100000 + ll.library_id a, ll.*, flow_cell_name fcname
  from 
    lane_library ll
    left outer join lane l
      on (ll.lane_id = l.lane_id)
    left outer join flow_cell  fc
      on (l.flow_cell_id = fc.flow_cell_id)
|, "a");

my $multiplexindex = {};
my $jobsampleseen = {};
foreach my $llane (sort keys %$llanes) {
  my $l = $llanes->{$llane};

  sqlescape($l);


  if (! defined($multiplexindex->{$l->{'lane_id'}})) {
    $multiplexindex->{$l->{'lane_id'}} = 1;
  } else {
    $multiplexindex->{$l->{'lane_id'}}++;
  }


  $out .= qq|
    insert into samplesource
    ( sampleid, multiplexindex, source_sampleid)
    values
    (20000 + $l->{'lane_id'}, $multiplexindex->{$l->{'lane_id'}}, 10000 + $l->{'library_id'});
  |;

  next if (defined( $jobsampleseen->{$l->{'job_id'}}->{$l->{'lane_id'}}));
  $jobsampleseen->{$l->{'job_id'}}->{$l->{'lane_id'}} = 1; 

  $out .= qq|
    insert into jobsample
    (jobid, sampleid)
    values
    ($l->{'job_id'}, 20000 + $l->{lane_id});
  |;
}

print $out;
$out = "";

my $flowcells = $v1_dbh->selectall_hashref(qq|
  select fc.*, esf.user_id create_by_userid
  from 
    flow_cell fc
    left outer join esf_staff esf
      on (fc.start_esf_staff_id = esf.esf_staff_id)
|, "flow_cell_id");

foreach my $flowcell (sort keys %$flowcells) {
  my $fc = $flowcells->{$flowcell};

  sqlescape($fc);

  $fc->{'create_by_userid'} ||= 'null';
  $fc->{'i'} = 30000 + $fc->{'flow_cell_id'};

  $out .= qq|
    insert into sample
    ( sampleid, typesampleid, submitter_labid, submitter_userid, submitter_jobid, 
       name, isreceived, receiver_userid, receivedts, isgood, isactive)
    values
    ($fc->{'i'}, 5, 1, $fc->{'create_by_userid'}, null, 
      '$fc->{'flow_cell_name'}', 0, 0, null, null, 1);
  |;

  $out .= meta("sample", [qw|start_datetime start_esf_staff_id end_datetime end_esf_staff_id comments flow_cell_final_status|], 'i', $fc, 'flowcell');

}

# finds corresponding lanes and jobs
$out .= qq|
  insert into samplesource
    (sampleid, multiplexindex, source_sampleid)
  select    
    s.sampleid, sm2.v, sm2.sampleid
  from    
    sample s    
    inner join samplemeta sm      
      on (sm.k = 'sample.lane.flow_cell_id' and s.sampleid = 30000 + sm.v) 
    inner join samplemeta sm2 
      on (sm2.k = 'sample.lane.lane_number' and sm.sampleid = sm2.sampleid) 
    where 
      typesampleid = 5;

  insert into jobsample
    (lastupduser, jobid, sampleid)
  select 
    js.jobid * 10000 +  s.sampleid, js.jobid, s.sampleid
  from sample s, samplesource ss, jobsample js 
  where 
    typesampleid = 5 and 
    s.sampleid = ss.sampleid and 
    js.sampleid = ss.source_sampleid
  group by js.jobid, s.sampleid;

|;

my $runs = $v1_dbh->selectall_hashref(qq|
  select s.*, esf.user_id create_by_userid, fc.flow_cell_id
  from sequence_run s
    left outer join esf_staff esf
      on (s.start_esf_staff_id = esf.esf_staff_id)
    left outer join flow_cell fc
      on (s.flow_cell_name = fc.flow_cell_name)
|, "sequence_run_id");

foreach my $run (sort keys %$runs) {
  my $r = $runs->{$run};

  sqlescape($r);

  $r->{'create_by_userid'} ||= 'null';
  $r->{'sampleid'} = $r->{'flow_cell_id'} + 30000;
  $r->{'status'} = 1;
  $r->{'status'} = 0 if $r->{'comments'};
  

  # TODO: fill in resourceid w/ meta machinename
  $out .= qq|
    insert into run
    ( runid, resourceid, userid, name, sampleid, status,
       startts, endts)
    values
    ($r->{'sequence_run_id'}, 1, $r->{'create_by_userid'}, '$r->{'sequence_run_name'}', $r->{'sampleid'}, $r->{'status'},
      '$r->{'start_datetime'}', '$r->{'end_datetime'}');
  |;

  my $cols = [qw|flow_cell_name start_esf_staff_id start_datetime end_esf_staff_id end_datetime machine_name comments path_to_data sequence_run_final_status run_success|];

  my $metapos = 0;
  foreach my $metak (@$cols) {
    next if (! $r->{$metak});

    $out .= qq|
      insert into runmeta
      (runid, k, v, position)
      values
      ($r->{'sequence_run_id'}, 'run.$metak', '$r->{$metak}', $metapos);
    |;
    $metapos++;
  }
}


$out .= qq|
  update run
  set resourceid = (
      select r.resourceid
      from 
        runmeta rm, resource r
      where
        run.runid = rm.runid and
        rm.k = 'run.machine_name' and 
        rm.v = r.name
    );

 insert into resourcelane
   (resourceid, iname, name)
 select r.resourceid, 
   concat(rr.name, ' ', multiplexindex) iname, concat(rr.name, ' ', multiplexindex, ' ', count(*)) name
 from run r, samplesource ss , resource rr
 where r.sampleid = ss.sampleid and r.resourceid = rr.resourceid
 group by r.resourceid, ss.multiplexindex;


  insert into runlane
    (runid, resourcelaneid, sampleid)
  select r.runid, l.resourcelaneid, ss.source_sampleid
  from 
    run r, samplesource ss, resource rr, resourcelane l
  where
    r.sampleid = ss.sampleid and
    r.resourceid = rr.resourceid and
    concat(rr.name, ' ', ss.multiplexindex) = l.iname;

|;

print $out;
$out = "";

####################################33
# ACCT tables

my $jobs = $v1_dbh->selectall_hashref(qq|
  select j.*, l.pi_id pi_userid
  from job j
    left outer join lab l
      on (j.lab_id = l.lab_id)
|, "job_id");

foreach my $job (sort keys %$jobs) {
  my $j = $jobs->{$job};

  $out .= qq|
    insert into acct_quote
    (quoteid, jobid, amount)
    values
    ($job, $job, 0);

    insert into acct_jobquotecurrent
    (jobid, quoteid)
    values
    ($job, $job);
  |;

  if ($j->{'grant_info_id'} ne "NULL" &&
      $j->{'grant_info_id'} ne "") {
#ED209 load acct_grant table first
#    $out .= qq|
#      insert into acct_grantjob
#      (grantid, jobid)
#      values
#      ($j->{'grant_info_id'}, $job);
#    |;
  }


  if ($j->{'pi_accept_withdraw_job'} eq "NULL" ||
      $j->{'pi_accept_withdraw_job'} eq ""
    ) {
    # pending
    # print "$job SKIP\n";
  } elsif ($j->{'pi_accept_withdraw_job'} eq "APPROVED" ) {
    $out .= qq|
      insert into acct_quoteuser
      (quoteid, userid, roleid, isapproved, lastupdts, lastupduser)
      value
      ($job, $j->{'pi_userid'}, 6, 1, '$j->{'date_pi_accept_withdraw_job'}', $j->{'pi_userid'});
    |;
  } else {
    $out .= qq|
      insert into acct_quoteuser
      (quoteid, userid, roleid, isapproved, lastupdts, lastupduser)
      value
      ($job, $j->{'pi_userid'}, 6, 0, '$j->{'date_pi_accept_withdraw_job'}', $j->{'pi_userid'});
    |;
  }
    

  # ACCEPTING USER, default to svetlana
  if ($j->{'accept_reject_withdraw'} eq "NULL"
    || $j->{'accept_reject_withdraw'} eq "" ) {
    # pending
    # print "$job SKIP\n";
  } elsif ($j->{'accept_reject_withdraw'} eq "ACCEPTED" ) {
    $out .= qq|
      insert into acct_quoteuser
        (quoteid, userid, roleid, comment, isapproved, lastupdts, lastupduser)
      value
       ($job, 90, 4, '$j->{'reason_accept_reject_withdraw'}', 1, '$j->{'date_accept_reject_withdraw'}', 90);
    |;
  } else {
    $out .= qq|
      insert into acct_quoteuser
        (quoteid, userid, roleid, comment, isapproved, lastupdts, lastupduser)
      value
       ($job, 90, 4, '$j->{'reason_accept_reject_withdraw'}', 0, '$j->{'date_accept_reject_withdraw'}', 90);
    |;

  }

}



print $out;
$out = "";

my $fundings = $v1_dbh->selectall_hashref(qq|
  select fc.*
  from
    funding_confirmation fc
|, "job_id");
foreach my $funding (sort keys %$fundings) {
  my $fc = $fundings->{$funding};
  $fc->{amount} =~ s/[^0-9\.]//g;
  sqlescape($fc);

  $out .= qq|
    insert into acct_invoice
      (invoiceid, quoteid, jobid, amount, lastupdts )
    value
      ($funding, $funding, $funding, $fc->{'amount'}, $fc->{'date_recorded'});

    insert into acct_ledger
      (ledgerid, invoiceid, jobid, amount, lastupdts )
    value
      ($funding, $funding, $funding, $fc->{'amount'}, $fc->{'date_recorded'});
|;

}

print $out;

$out = "
update sample set receivedts = null;
update sample set isgood =1;

update run set startts = null where startts = '0000-00-00 00:00:00';
update run set endts = null where endts = '0000-00-00 00:00:00';

insert into jobuser (jobid, userid, roleid) select jobid, userid, 9 from job;

update user set password = sha1('abc123');

";

print $out;


################################################################
# utils

sub sqlescape {
  my $h = shift; 

  foreach my $k (keys %$h) {
   $h->{$k} =~ s/\\//g;
   $h->{$k} =~ s/'/''/g;
  }

  return $h;
}

sub meta {
  my $table = shift; 
  my $cols = shift; 
  my $index = shift; 
  my $obj = shift;
  my $suffix = shift || "" ;

  if ($suffix) { $suffix .= "."; }

  my $rt = "";
  my $metapos = 0;
  foreach my $metak (@$cols) {
    next if (! $obj->{$metak});

    $rt .= qq|
      insert into ${table}meta
      (${table}id, k, v, position)
      values
      ($obj->{$index}, '${table}.$suffix$metak', '$obj->{$metak}', $metapos);
    |;
    $metapos++;
  }

  return $rt;
}
