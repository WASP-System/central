
/**
 *
 * SampleDraftMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleDraftMeta object
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
@Table(name="sampledraftmeta")
public class SampleDraftMeta extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int sampleDraftMetaId;
  public void setSampleDraftMetaId (int sampleDraftMetaId) {
    this.sampleDraftMetaId = sampleDraftMetaId;
  }
  public int getSampleDraftMetaId () {
    return this.sampleDraftMetaId;
  }


  @Column(name="sampledraftid")
  protected int sampledraftId;
  public void setSampledraftId (int sampledraftId) {
    this.sampledraftId = sampledraftId;
  }
  public int getSampledraftId () {
    return this.sampledraftId;
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
   @JoinColumn(name="sampledraftid", insertable=false, updatable=false)
  protected SampleDraft sampleDraft;
  public void setSampleDraft (SampleDraft sampleDraft) {
    this.sampleDraft = sampleDraft;
    this.sampledraftId = sampleDraft.sampleDraftId;
  }
  
  public SampleDraft getSampleDraft () {
    return this.sampleDraft;
  }

}
