#!/usr/bin/perl 

undef $/; 
open(F, "create.sql"); 
$fc = <F>;
close (F);

$fc =~ s|/\*.*?\*/||sg;

$schema = {};

$types = {
  'varchar' => 'String', 
  'int' => 'int', 
  'intNull' => 'Integer', 
  'numeric' => 'float', 
  'float' => 'float', 
  'datetime' => 'Date', 
  'timestamp' => 'Date', 
  'boolean' => 'boolean', 
};

foreach my $block (split /\s*;\s*/, $fc) {
  $block =~ s/--.*//g;
  $block =~ s/\s*\n\s*/\n/g;

  $ja = ""; 
  $jb = ""; 
  my ($table) = ($block =~ /create table\s*([a-z_]+)/);
  next if ! $table;
  $_table = $table;

  $schema->{$_table} = {};
  $schema->{$_table}->{'_table'} = $_table;

  my ($pk) = ($block =~ /(\S+)\s*int.*primary key/i);
  $schema->{$_table}->{'pk'} = $pk;


  $table =~ s/^type(.)/"type\U$1"/e;
  $table =~ s/(.)user$/$1User/;
  $table =~ s/(.)pendingmeta$/$1PendingMeta/;
  $table =~ s/(.)pending$/$1Pending/;
  $table =~ s/(.)meta$/$1Meta/;

  $table =~ s/^job(.)/"job\U$1"/ie;
  $table =~ s/^project(.)/"project\U$1"/ie;
  $table =~ s/^sample(.)/"sample\U$1"/ie;
  $table =~ s/^run(.)/"run\U$1"/ie;
  $table =~ s/^resource(.)/"resource\U$1"/ie;
  $table =~ s/^quote(.)/"quote\U$1"/ie;
  $table =~ s/_(.)/"\U$1"/gie;

  $schema->{$_table}->{'table'} = $table;


  $Table = $table;
  $Table =~ s/^(.)/"\U$1"/e; 

  $schema->{$_table}->{'Table'} = $Table;
  $schema->{$_table}->{'cols'} = {};
  $schema->{$_table}->{'colorder'} = [];
  $schema->{$_table}->{'uniq'} = [];
  $schema->{$_table}->{'fka'} = [];
  $schema->{$_table}->{'fkb'} = [];


  $block =~ s/.*?\(//s;  
  $block =~ s/\)[^)]*$//s;  

  foreach my $row (split/\s*\n\s*/, $block ) {
    $row =~ s/,$//g;
    next if (! $row);
    if ($row =~ /^foreign key/ ) {
      # foreign key fk_job_pid (projectid) references project(projectid),
      # foreign key fk_sample_sjid (submitter_jobid) references job(jobid),
      my ($fkname, $col, $ptable, $pcol) = ($row =~ /foreign key (.*) \((.*)\) references (.*)\s*\((.*)\)/);

# print "$fkname, $col, $ptable, $pcol\n";

      my $fk = {};
      $fk->{'fkname'}  = $fkname;
      $fk->{'table'}  = $_table;
      $fk->{'col'}  = $col;
      $fk->{'ptable'}  = $ptable;
      $fk->{'pcol'}  = $pcol;
      
      # next if defined($schema->{$ptable}->{'cols'}->{$pcol}->{'fkb'}->{$_table'}->{$col});
      next if $schema->{$ptable}->{'cols'}->{$pcol}->{'fkb'}->{$_table}->{$col};

      push @{$schema->{$_table}->{'fka'}}, $fk;
      push @{$schema->{$ptable}->{'fkb'}}, $fk;
      schema->{$ptable}->{'cols'}->{$pcol}->{'fkb'}->{$_table}->{$col} = 1;

    } elsif ($row =~ /^index/ ) {
      # todo create get by list 

    } elsif ($row =~ /^constraint/ ) {

      # constraint unique index u_samplesource_sid (sampleid, multiplexindex)
      my ($index, $cols) = ($row =~ /index\s+(\S+)\s*\((.*)\)/);

      my $uniq = {}; 
      $uniq->{'index'} = $index; 
      $uniq->{'cols'} = [ split /\s*,\s*/, $cols ] ;
 

      $schema->{$_table}->{'cols'}->{'uniq'} = $uniq;
      push @{$schema->{$_table}->{'uniq'}}, $uniq;

    } else {
      my ($var, $type, $len) = ($row =~ /([a-z0-9_]+)\s+([a-z]+)\s*(\(\s*(\d+)\s*\))?/); 
      $len =~ s/\D//g;
      $_var = $var;
      push @{$schema->{$_table}->{'colorder'}}, $_var;

      if ($type eq "int" && $row !~ /not null/) {
        $type = "intNull"; 
      }


      $var =~ s/$_table/$table/ei;
      $var =~ s/lastupduser/lastUpdUser/;
      $var =~ s/lastupdts/lastUpdTs/;
      $var =~ s/userid$/UserId/;
      $var =~ s/id$/Id/;
      $var =~ s/dts$/Dts/;
      $var =~ s/(.)name$/$1Name/;
      $var =~ s/^is(.)/"is\U$1"/e;
      $var =~ s/^type(.)/"type\U$1"/e;
      $var =~ s/_(.)/"\U$1"/gie;

      $Var = $var;
      $Var =~ s/(.)/"\U$1"/e; 

      $t = $types->{$type}; 

      $c = {};
      $c->{'_var'} = $_var;
      $c->{'var'} = $var;
      $c->{'Var'} = $Var;
      $c->{'type'} = $type;
      $c->{'t'} = $t;
      $c->{'len'} = $len;
      $c->{'pk'} = $_var eq $pk;
      $c->{'notnull'} = ($row =~ /not null/);

      if ($c->{'pk'}) {
        my $uniq = {}; 
        $uniq->{'index'} = $index; 
        $uniq->{'cols'} = [ $_var ] ;
 

        push @{$schema->{$_table}->{'uniq'}}, $uniq;
         
      }

      $schema->{$_table}->{'cols'}->{$_var} = $c;
    }
  }
}

foreach $table (sort keys %$schema) {
  next if ! $table;
  $t = $schema->{$table};

  $j_dao_interface = qq|
/**
 *
 * $t->{'Table'}Dao.java 
 * \@author echeng (table2type.pl)
 *  
 * the $t->{'Table'}Dao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;

@Respository
public interface $t->{'Table'}Dao extends WaspDao<$t->{'Table'}> {

|;


  $j_dao = qq|
/**
 *
 * $t->{'Table'}Impl.java 
 * \@author echeng (table2type.pl)
 *  
 * the $t->{'Table'} object
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.$t->{'Table'};

\@SuppressWarnings("unchecked")
\@Transactional
\@Repository
public class $t->{'Table'}DaoImpl extends WaspDaoImpl<$t->{'Table'}> implements edu.yu.einstein.wasp.dao.$t->{'Table'}Dao {

  public $t->{'Table'}DaoImpl() {
    super();
    this.entityClass = $t->{'Table'}.class;
  }

|; 

  $j_service_interface = qq|
/**
 *
 * $t->{'Table'}Service.java 
 * \@author echeng (table2type.pl)
 *  
 * the $t->{'Table'}Service object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.$t->{'Table'}Dao;
import edu.yu.einstein.wasp.model.$t->{'Table'};

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

\@Service
public interface $t->{'Table'}Service extends WaspService<$t->{'Table'}> {

  public void set$t->{'Table'}Dao($t->{'Table'}Dao $t->{'table'}Dao);
  public $t->{'Table'}Dao get$t->{'Table'}Dao();

|;

  $j_service = qq|
/**
 *
 * $t->{'Table'}Service.java 
 * \@author echeng (table2type.pl)
 *  
 * the $t->{'Table'}Service object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.$t->{'Table'}Service;
import edu.yu.einstein.wasp.dao.$t->{'Table'}Dao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.$t->{'Table'};

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

\@Service
public class $t->{'Table'}ServiceImpl extends WaspServiceImpl<$t->{'Table'}> implements $t->{'Table'}Service {

  private $t->{'Table'}Dao $t->{'table'}Dao;
  \@Autowired
  public void set$t->{'Table'}Dao($t->{'Table'}Dao $t->{'table'}Dao) {
    this.$t->{'table'}Dao = $t->{'table'}Dao;
    this.setWaspDao($t->{'table'}Dao);
  }
  public $t->{'Table'}Dao get$t->{'Table'}Dao() {
    return this.$t->{'table'}Dao;
  }

  // **

  |;


  foreach my $u (@{$t->{'uniq'}}) {
    my $a = ""; 
    my $b = "";
    my $c = "";;
    my $d = "";;
    my $e = "";;

    foreach $col (@{$u->{'cols'}}) {
      next if ! $col;
      $a .= $t->{'cols'}->{$col}->{'Var'};

      $b .= ", " if $b;
      $b .= 'final ' . $t->{'cols'}->{$col}->{'t'} . ' ';
      $b .= $t->{'cols'}->{$col}->{'var'};

      $e .= ", " if $e;
      $e .= $t->{'cols'}->{$col}->{'var'};

      $c .= qq|    m.put("$t->{'cols'}->{$col}->{'var'}", $t->{'cols'}->{$col}->{'var'});\n|;

#      $c .= qq|\n       + " AND "| if $c;
#      $c .= qq|+ "a.$t->{'cols'}->{$col}->{'var'} = :$t->{'cols'}->{$col}->{'var'}"|;
      $d .= qq|      query.setParameter("$t->{'cols'}->{$col}->{'var'}", $t->{'cols'}->{$col}->{'var'});\n|;
    }

    $j_dao .= "  \@SuppressWarnings(\"unchecked\")\n";
    $j_dao .= "  \@Transactional\n";

    $j_dao_interface .= "  public $t->{'Table'} get$t->{'Table'}By$a ($b);\n\n"; 
    $j_service_interface .= "  public $t->{'Table'} get$t->{'Table'}By$a ($b);\n\n"; 

    $j_service .= qq|
  public $t->{'Table'} get$t->{'Table'}By$a ($b) {
    return this.get$t->{'Table'}Dao().get$t->{'Table'}By$a($e);
  }
|;

    $j_dao .= "  public $t->{'Table'} get$t->{'Table'}By$a ($b) {\n";

    $j_dao .= "    HashMap m = new HashMap();\n";
    $j_dao .= $c;
    $j_dao .= "    List<$t->{'Table'}> results = (List<$t->{'Table'}>) this.findByMap((Map) m);\n";
    $j_dao .= qq|    if (results.size() == 0) {\n|;
    $j_dao .= qq|      $t->{'Table'} rt = new $t->{'Table'}();\n|;
    $j_dao .= qq|      return rt;\n|;
    $j_dao .= qq|    }\n|;
    $j_dao .= qq|    return ($t->{'Table'}) results.get(0);\n|;
    $j_dao .= "  }\n\n\n"; 

    # handles meta updater
    if ($t->{'Table'} =~ /[Mm]eta/) {
      if (${$u->{'cols'}}[0] eq "k") {
        my $col  = ${$u->{'cols'}}[1];

        my $Var = $t->{'cols'}->{$col}->{'Var'};
        my $var = $t->{'cols'}->{$col}->{'var'};

        $j_dao_interface .= qq|  public void updateBy$Var (final int $var, final List<$t->{'Table'}> metaList);\n\n|;
        $j_service_interface .= qq|  public void updateBy$Var (final int $var, final List<$t->{'Table'}> metaList);\n\n|;

        $j_service .= qq|
  public void updateBy$Var (final int $var, final List<$t->{'Table'}> metaList) {
    this.get$t->{'Table'}Dao().updateBy$Var($var, metaList); 
  }

|;

        $j_dao .= qq|
  \@SuppressWarnings("unchecked")
  \@Transactional
  public void updateBy$Var (final int $var, final List<$t->{'Table'}> metaList) {

    getJpaTemplate().execute(new JpaCallback() {

      public Object doInJpa(EntityManager em) throws PersistenceException {
        em.createNativeQuery("delete from $t->{'table'} where $var=:$var").setParameter("$var", $var).executeUpdate();

        for ($t->{'Table'} m:metaList) {
          em.persist(m);
        }

        return null;
      }
    });

  }
|;


      }
    }
  }

  $j_dao .= qq|}\n\n|;
  $j_dao_interface .= qq|}\n\n|;
  $j_service .= qq|}\n\n|;
  $j_service_interface .= qq|}\n\n|;


  $j = qq|
/**
 *
 * $t->{'Table'}.java 
 * \@author echeng (table2type.pl)
 *  
 * the $t->{'Table'} object
 *
 *
 */

package edu.yu.einstein.wasp.model;

import org.hibernate.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.*;

import org.codehaus.jackson.annotate.JsonIgnore;

\@Entity
\@Audited
\@Table(name="$t->{'_table'}")
public class $t->{'Table'} extends WaspModel {
|;

  foreach my $colname (@{$t->{'colorder'}}) {
    $c = $t->{'cols'}->{$colname};

    if ($c->{'pk'}) {
      $j .= "  \@Id \@GeneratedValue(strategy=GenerationType.IDENTITY)\n";
    } else {

      $j .= qq|  \@Column(name="$c->{'_var'}")\n|;
    }
    $j .= "  protected $c->{'t'} $c->{'var'};\n";

    $j .= "  public void set$c->{'Var'} ($c->{'t'} $c->{'var'}) {\n";
    $j .= "    this.$c->{'var'} = $c->{'var'};\n";
    $j .= "  }\n";
    $j .= "  public $c->{'t'} get$c->{'Var'} () {\n";
    $j .= "    return this.$c->{'var'};\n";
    $j .= "  }\n";
    $j .= "\n";
    $j .= "\n";
  }

  $c = 0;
  $seen = {};
  foreach my $fk (@{$t->{'fka'}}) {
    my $parentt = $schema->{$fk->{'ptable'}};
    my $parentc = $parentt->{'cols'}->{$fk->{'pcol'}};
    my $thisc = $schema->{$t->{'_table'}}->{'cols'}->{$fk->{'col'}};

    my $jtype = "\@OneToOne(cascade = CascadeType.ALL)";
    my $rttype = $parentt->{'Table'};
    if (! $thisc->{'uniq'})  {
      $jtype = "\@ManyToOne";
      $rttype = "List<" . $rttype . ">";
    }
    my $via = "";
    if ($seen->{$parentt->{'_table'}}->{$parentc->{'var'}}) {
      $via = "Via" . $childc->{'Var'};
    }

    $g  = ""; 
    $j .= "\n";
    $j .= "  \@NotAudited\n";
    $j .= "  $jtype\n";
    $j .= qq|   \@JoinColumn(name="$thisc->{'_var'}", insertable=false, updatable=false)\n|;

#    $j .= qq|  \@JoinTable(name="$parentt->{'table'}", |;
#    $j .= qq|  joinColumns = \@JoinColumn(name="$parentc->{'_var'}", insertable=true, updatable=true)|;
#    $j .= )\n|;
    $j .= "  protected $parentt->{'Table'} $parentt->{'table'}$via;\n";
    $j .= "  public void set$parentt->{'Table'}$via ($parentt->{'Table'} $parentt->{'table'}) {\n";
    $j .= "    this.$parentt->{'table'} = $parentt->{'table'};\n";
    $j .= "    this.$thisc->{'var'} = $parentt->{'table'}.$parentc->{'var'};\n";
    $j .= "  }\n";
    $j .= "  @JsonIgnore\n";
    $j .= "  public $parentt->{'Table'} get$parentt->{'Table'}$via () {\n";
    $j .= "    return this.$parentt->{'table'};\n";
    $j .= "  }\n";

    $seen->{$parentt->{'_table'}}->{$parentc->{'var'}} = 1;
  }


  my $seen = {};
  foreach my $fk (@{$t->{'fkb'}}) {

    my $childt = $schema->{$fk->{'table'}};
    my $childc = $childt->{'cols'}->{$fk->{'col'}};

    
    my $jtype = '@OneToOne';
    my $rttype = $childt->{'Table'}; 
    if (! $childc->{'uniq'}) {
      # $jtype = '@OneToMany (fetch=FetchType.EAGER)';
      $jtype = '@OneToMany';
      $rttype = "List<$childt->{'Table'}>";
    }
 

    my $via = "";
    if ($seen->{$childt->{'table'}}) {
      $via = "Via" . $childc->{'Var'}
    }

    $j .= "  \@NotAudited\n";
    $j .= "  $jtype\n";
    $j .= qq|   \@JoinColumn(name="$fk->{'col'}", insertable=false, updatable=false)\n|;
    # $j .= qq|  \@JoinTable(name="$childt->{'table'}", |;
    # $j .= qq|  joinColumns = \@JoinColumn(name="$fk->{'col'}", insertable=false, updatable=false))\n|;
    
    $j .= "  protected $rttype $childt->{'table'}$via;\n";
    $j .= "  public $rttype get$childt->{'Table'}$via()";
    $j .= "  {\n";
    $j .= "    return this.$childt->{'table'}$via;\n";
    $j .= "  }\n" ;
    $j .= "  public void set$childt->{'Table'}$via ($rttype $childt->{'table'})";
    $j .= "  {\n";
    $j .= "    this.$childt->{'table'}$via = $childt->{'table'};\n";
    $j .= "  }\n\n\n" ;

    $seen->{$childt->{'table'}} = 1;
  }

  $j .= qq|
}
|;




  open (OF, "> t/model/$t->{'Table'}.java");
  print OF $j;
  close (OF);

  open (OF, "> t/dao/$t->{'Table'}Dao.java");
  print OF $j_dao_interface;
  close (OF);

  open (OF, "> t/dao/impl/$t->{'Table'}DaoImpl.java");
  print OF $j_dao;
  close (OF);

  open (OF, "> t/service/$t->{'Table'}Service.java");
  print OF $j_service_interface;
  close (OF);

  open (OF, "> t/service/impl/$t->{'Table'}ServiceImpl.java");
  print OF $j_service;
  close (OF);

}



