
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
public class JobDraftMeta extends MetaBase {
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
