
/**
 *
 * LabPendingMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the LabPendingMeta object
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
@Table(name="labpendingmeta")
public class LabPendingMeta extends MetaBase {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int labPendingMetaId;
  public void setLabPendingMetaId (int labPendingMetaId) {
    this.labPendingMetaId = labPendingMetaId;
  }
  public int getLabPendingMetaId () {
    return this.labPendingMetaId;
  }


  @Column(name="labpendingid")
  protected int labpendingId;
  public void setLabpendingId (int labpendingId) {
    this.labpendingId = labpendingId;
  }
  public int getLabpendingId () {
    return this.labpendingId;
  }



  @NotAudited
  @ManyToOne
   @JoinColumn(name="labpendingid", insertable=false, updatable=false)
  protected LabPending labPending;
  public void setLabPending (LabPending labPending) {
    this.labPending = labPending;
    this.labpendingId = labPending.labPendingId;
  }
  
  public LabPending getLabPending () {
    return this.labPending;
  }

}
