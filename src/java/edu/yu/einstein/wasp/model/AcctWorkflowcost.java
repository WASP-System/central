
/**
 *
 * AcctWorkflowcost.java 
 * @author echeng (table2type.pl)
 *  
 * the AcctWorkflowcost object
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
@Table(name="acct_workflowcost")
public class AcctWorkflowcost extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int workflowId;
  public void setWorkflowId (int workflowId) {
    this.workflowId = workflowId;
  }
  public int getWorkflowId () {
    return this.workflowId;
  }


  @Column(name="basecost")
  protected float basecost;
  public void setBasecost (float basecost) {
    this.basecost = basecost;
  }
  public float getBasecost () {
    return this.basecost;
  }


  @Column(name="lastupdts")
  protected Date lastUpdTs;
  public void setLastUpdTs (Date lastUpdTs) {
    this.lastUpdTs = lastUpdTs;
  }
  public Date getLastUpdTs () {
    return this.lastUpdTs;
  }


  @Column(name="lastupduser")
  protected int lastUpdUser;
  public void setLastUpdUser (int lastUpdUser) {
    this.lastUpdUser = lastUpdUser;
  }
  public int getLastUpdUser () {
    return this.lastUpdUser;
  }



  @NotAudited
  @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name="workflowid", insertable=false, updatable=false)
  protected Job job;
  public void setJob (Job job) {
    this.job = job;
    this.workflowId = job.workflowId;
  }
  public Job getJob () {
    return this.job;
  }

}
