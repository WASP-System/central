
/**
 *
 * Statejob.java 
 * @author echeng (table2type.pl)
 *  
 * the Statejob object
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
@Table(name="statejob")
public class Statejob extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int statejobId;
  public void setStatejobId (int statejobId) {
    this.statejobId = statejobId;
  }
  public int getStatejobId () {
    return this.statejobId;
  }


  @Column(name="stateid")
  protected int stateId;
  public void setStateId (int stateId) {
    this.stateId = stateId;
  }
  public int getStateId () {
    return this.stateId;
  }


  @Column(name="jobid")
  protected int jobId;
  public void setJobId (int jobId) {
    this.jobId = jobId;
  }
  public int getJobId () {
    return this.jobId;
  }



  @NotAudited
  @ManyToOne
   @JoinColumn(name="stateid", insertable=false, updatable=false)
  protected State state;
  public void setState (State state) {
    this.state = state;
    this.stateId = state.stateId;
  }
  public State getState () {
    return this.state;
  }

  @NotAudited
  @ManyToOne
   @JoinColumn(name="jobid", insertable=false, updatable=false)
  protected Job job;
  public void setJob (Job job) {
    this.job = job;
    this.jobId = job.jobId;
  }
  public Job getJob () {
    return this.job;
  }

}
