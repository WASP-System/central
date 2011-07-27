#!/usr/bin/perl  -w
#

use strict; 
use DBI; 

our $v2_dbh = DBI->connect("dbi:mysql:wasp:localhost:3306", "wasp", "waspV2") or die "Unable to connect: $DBI::errstr\n";


our $v2_data = {}; 
$v2_data->{'job'} = $v2_dbh->selectall_hashref(qq|select * from job |, "jobid");

$v2_data->{'flowcell'} = $v2_dbh->selectall_hashref(qq|select * from sample where typesampleid = 5 |, "name");

$v2_data->{'lane'} = $v2_dbh->selectall_hashref(qq|select * from sample where typesampleid = 4 |, "name");

$v2_data->{'library'} = $v2_dbh->selectall_hashref(qq|select concat(s2.name, " ", multiplexindex) a, s.sampleid, ss.multiplexindex, s2.name from    sample s,    samplesource ss,   sample s2 where    s.typesampleid = 3 and    s.sampleid = ss.source_sampleid and   ss.sampleid = s2.sampleid|, "a");

our $i = 1;

print qq|
 delete from runlanefile;
 delete from runfile;
 delete from samplefile;
 delete from jobfile;
 delete from file;
|;

while (<>) {
  # /results2/www/production_wiki/MBrownlee/FHill/P45

  # strips \ns
  chomp; chomp;

  my $location = $_;

  # strips full path down to the project
  s|/.*?/P\d+/?||;
 
  # only jobs as Jnnnn
  next if ($_ !~ m|^J(\d+)/|);

  my $jobid = $1;

  # is the job in the database.
  if (! defined ( $v2_data->{'job'}->{$jobid})) {
    next;
    die "could not find job " . $jobid;
  }


  # only files w/ extensions.
  next if ($_ !~ m|/[^/]+\.[^/]+$|);

  my (undef, $relpath) = split /\//, $_, 2;
  my ($filename) = ($_ =~ m|/([^/]+\.[^/]+)$|);

  print "-- " . $_ . "\n";

  print qq| 
    insert into file
    (fileid, filelocation, contenttype, sizek, md5hash)
    values
    ($i, '$location', '?', 1, 'xxx');

    insert into jobfile
    (jobid, fileid, iname, name)
    values
    ($jobid, $i, '$relpath', '$filename');
  |;

  my $sf = "";


  my ($flowcell) = ($relpath =~ /([A-Z0-9]+XX)/);
  if ($flowcell && defined($v2_data->{'flowcell'}->{$flowcell}->{'sampleid'}) ) {
    $sf = "-- [FLOWCELL $flowcell]\n";
    my $sampleid = $v2_data->{'flowcell'}->{$flowcell}->{'sampleid'};

    $sf .= qq|
      insert into samplefile
      (sampleid, fileid, iname, name) 
      values
      ($sampleid, $i, '$relpath', '$filename');
    |;

    # corresponds to lane
    my ($lane) = ($relpath =~ /lane_(\d+)/);

    if ($lane && defined($v2_data->{'lane'}->{$flowcell . " / " . $lane}->{'sampleid'}) ) {
      my $laneid = $v2_data->{'lane'}->{$flowcell . " / " . $lane}->{'sampleid'};
      $sf = "-- [LANE $laneid : $flowcell / $lane ]\n";
      $sf .= qq|
        insert into samplefile
        (sampleid, fileid, iname, name) 
        values
        ($laneid, $i, '$relpath', '$filename');
      |;

      # 81M7BABXX.lane_5_P0_I4
      my $mindex = "";
      ($mindex) = ($relpath =~ /$flowcell\.lane_$lane\_P._I(\d)/);

      # really? or should this assume Px_I0 is lane only related.
      if (defined($mindex) && $mindex eq "0") { $mindex = "1" }

      if (defined($mindex) && ($mindex ne "") && defined($v2_data->{'library'}->{$flowcell . " / " . $lane . " " . $mindex}->{'sampleid'})) {

        my $libid = $v2_data->{'library'}->{$flowcell . " / " . $lane . " " . $mindex}->{'sampleid'};

        $sf = "-- [LIB $libid : $flowcell / $lane $mindex]\n";
        $sf .= qq|
          insert into samplefile
          (sampleid, fileid, iname, name) 
          values
          ($libid, $i, '$relpath', '$filename');
        |;
      }

    }


  }
  print $sf;

  print "\n-- ------------------------------\n";
  
  $i++; 
}

# addes runs and runlanes based on samples
  print "
    insert into runfile (runid, fileid, iname, name) select r.runid, s.fileid, s.iname, s.name from run r, samplefile s where r.sampleid = s.sampleid and r.status = 1;
    insert into runlanefile (runlaneid, fileid, iname, name) select rl.runlaneid, s.fileid, s.iname, s.name from runlane rl, run r, samplefile s where rl.runid = r.runid and r.status = 1 and rl.sampleid = s.sampleid;
  ";







