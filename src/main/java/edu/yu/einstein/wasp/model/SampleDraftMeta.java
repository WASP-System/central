
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
public class SampleDraftMeta extends MetaBase {
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
