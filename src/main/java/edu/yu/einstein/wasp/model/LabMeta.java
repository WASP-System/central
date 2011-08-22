
/**
 *
 * LabMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the LabMeta object
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
@Table(name="labmeta")
public class LabMeta extends MetaBase {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int labMetaId;
  public void setLabMetaId (int labMetaId) {
    this.labMetaId = labMetaId;
  }
  public int getLabMetaId () {
    return this.labMetaId;
  }


  @Column(name="labid")
  protected int labId;
  public void setLabId (int labId) {
    this.labId = labId;
  }
  public int getLabId () {
    return this.labId;
  }


  @NotAudited
  @ManyToOne
   @JoinColumn(name="labid", insertable=false, updatable=false)
  protected Lab lab;
  public void setLab (Lab lab) {
    this.lab = lab;
    this.labId = lab.labId;
  }
  
  public Lab getLab () {
    return this.lab;
  }

}
