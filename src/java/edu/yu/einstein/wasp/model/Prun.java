
/**
 *
 * Prun.java 
 * @author echeng (table2type.pl)
 *  
 * the Prun object
 *
 *
 */

package edu.yu.einstein.wasp.model;

import org.hibernate.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.*;

@Entity
@Audited
@Table(name="prun")
public class Prun extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int prunId;
  public void setPrunId (int prunId) {
    this.prunId = prunId;
  }
  public int getPrunId () {
    return this.prunId;
  }


  @Column(name="pid")
  protected int pId;
  public void setPId (int pId) {
    this.pId = pId;
  }
  public int getPId () {
    return this.pId;
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
  @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name="pid", insertable=false, updatable=false)
  protected P p;
  public void setP (P p) {
    this.p = p;
    this.pId = p.pId;
  }
  public P getP () {
    return this.p;
  }

  @NotAudited
  @ManyToOne(cascade = CascadeType.ALL)
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
