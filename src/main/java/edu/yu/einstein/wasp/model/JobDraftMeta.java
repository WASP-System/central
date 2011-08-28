
/**
 *
 * JobDraftMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftMeta object
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
@Table(name="jobdraftmeta")
public class JobDraftMeta extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int jobDraftMetaId;
  public void setJobDraftMetaId (int jobDraftMetaId) {
    this.jobDraftMetaId = jobDraftMetaId;
  }
  public int getJobDraftMetaId () {
    return this.jobDraftMetaId;
  }


  @Column(name="jobdraftid")
  protected int jobdraftId;
  public void setJobdraftId (int jobdraftId) {
    this.jobdraftId = jobdraftId;
  }
  public int getJobdraftId () {
    return this.jobdraftId;
  }


  @Column(name="k")
  protected String k;
  public void setK (String k) {
    this.k = k;
  }
  public String getK () {
    return this.k;
  }


  @Column(name="v")
  protected String v;
  public void setV (String v) {
    this.v = v;
  }
  public String getV () {
    return this.v;
  }


  @Column(name="position")
  protected int position;
  public void setPosition (int position) {
    this.position = position;
  }
  public int getPosition () {
    return this.position;
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
  @ManyToOne
   @JoinColumn(name="jobdraftid", insertable=false, updatable=false)
  protected JobDraft jobDraft;
  public void setJobDraft (JobDraft jobDraft) {
    this.jobDraft = jobDraft;
    this.jobdraftId = jobDraft.jobDraftId;
  }
  
  public JobDraft getJobDraft () {
    return this.jobDraft;
  }

}
