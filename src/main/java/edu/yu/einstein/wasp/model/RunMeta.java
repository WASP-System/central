
/**
 *
 * RunMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the RunMeta object
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

@Entity
@Audited
@Table(name="runmeta")
public class RunMeta extends MetaBase {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int runMetaId;
  public void setRunMetaId (int runMetaId) {
    this.runMetaId = runMetaId;
  }
  public int getRunMetaId () {
    return this.runMetaId;
  }


  @Column(name="runid")
  protected int runId;
  public void setRunId (int runId) {
    this.runId = runId;
  }
  public int getRunId () {
    return this.runId;
  }


  @NotAudited
  @ManyToOne
   @JoinColumn(name="runid", insertable=false, updatable=false)
  protected Run run;
  public void setRun (Run run) {
    this.run = run;
    this.runId = run.runId;
  }
  
  public Run getRun () {
    return this.run;
  }

}
