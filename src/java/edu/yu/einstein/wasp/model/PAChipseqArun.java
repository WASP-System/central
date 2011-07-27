
/**
 *
 * PAChipseqArun.java 
 * @author echeng (table2type.pl)
 *  
 * the PAChipseqArun object
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
@Table(name="p_a_chipseq_arun")
public class PAChipseqArun extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int pArunId;
  public void setPArunId (int pArunId) {
    this.pArunId = pArunId;
  }
  public int getPArunId () {
    return this.pArunId;
  }


  @Column(name="pid")
  protected int pId;
  public void setPId (int pId) {
    this.pId = pId;
  }
  public int getPId () {
    return this.pId;
  }


  @Column(name="arunid")
  protected int arunId;
  public void setArunId (int arunId) {
    this.arunId = arunId;
  }
  public int getArunId () {
    return this.arunId;
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
   @JoinColumn(name="arunid", insertable=false, updatable=false)
  protected AChipseqArun aChipseqArun;
  public void setAChipseqArun (AChipseqArun aChipseqArun) {
    this.aChipseqArun = aChipseqArun;
    this.arunId = aChipseqArun.arunId;
  }
  public AChipseqArun getAChipseqArun () {
    return this.aChipseqArun;
  }

}
