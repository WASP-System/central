
/**
 *
 * AChipseqArunargs.java 
 * @author echeng (table2type.pl)
 *  
 * the AChipseqArunargs object
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
@Table(name="a_chipseq_arunargs")
public class AChipseqArunargs extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int arunargsId;
  public void setArunargsId (int arunargsId) {
    this.arunargsId = arunargsId;
  }
  public int getArunargsId () {
    return this.arunargsId;
  }


  @Column(name="arunid")
  protected Integer arunId;
  public void setArunId (Integer arunId) {
    this.arunId = arunId;
  }
  public Integer getArunId () {
    return this.arunId;
  }


  @Column(name="argv")
  protected String argv;
  public void setArgv (String argv) {
    this.argv = argv;
  }
  public String getArgv () {
    return this.argv;
  }


  @Column(name="argc")
  protected int argc;
  public void setArgc (int argc) {
    this.argc = argc;
  }
  public int getArgc () {
    return this.argc;
  }



  @NotAudited
  @ManyToOne
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
