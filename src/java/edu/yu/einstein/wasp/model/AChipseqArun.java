
/**
 *
 * AChipseqArun.java 
 * @author echeng (table2type.pl)
 *  
 * the AChipseqArun object
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
@Table(name="a_chipseq_arun")
public class AChipseqArun extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int arunId;
  public void setArunId (int arunId) {
    this.arunId = arunId;
  }
  public int getArunId () {
    return this.arunId;
  }


  @Column(name="fileid")
  protected int fileId;
  public void setFileId (int fileId) {
    this.fileId = fileId;
  }
  public int getFileId () {
    return this.fileId;
  }


  @Column(name="version")
  protected String version;
  public void setVersion (String version) {
    this.version = version;
  }
  public String getVersion () {
    return this.version;
  }


  @Column(name="startts")
  protected Date startts;
  public void setStartts (Date startts) {
    this.startts = startts;
  }
  public Date getStartts () {
    return this.startts;
  }


  @Column(name="endts")
  protected Date enDts;
  public void setEnDts (Date enDts) {
    this.enDts = enDts;
  }
  public Date getEnDts () {
    return this.enDts;
  }


  @Column(name="status")
  protected String status;
  public void setStatus (String status) {
    this.status = status;
  }
  public String getStatus () {
    return this.status;
  }


  @Column(name="isactive")
  protected int isActive;
  public void setIsActive (int isActive) {
    this.isActive = isActive;
  }
  public int getIsActive () {
    return this.isActive;
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
   @JoinColumn(name="fileid", insertable=false, updatable=false)
  protected File file;
  public void setFile (File file) {
    this.file = file;
    this.fileId = file.fileId;
  }
  public File getFile () {
    return this.file;
  }
  @NotAudited
  @OneToMany
   @JoinColumn(name="arunid", insertable=false, updatable=false)
  protected List<AChipseqArunargs> aChipseqArunargs;
  public List<AChipseqArunargs> getAChipseqArunargs()  {
    return this.aChipseqArunargs;
  }
  public void setAChipseqArunargs (List<AChipseqArunargs> aChipseqArunargs)  {
    this.aChipseqArunargs = aChipseqArunargs;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="arunid", insertable=false, updatable=false)
  protected List<StateAChipseqArun> stateAChipseqArun;
  public List<StateAChipseqArun> getStateAChipseqArun()  {
    return this.stateAChipseqArun;
  }
  public void setStateAChipseqArun (List<StateAChipseqArun> stateAChipseqArun)  {
    this.stateAChipseqArun = stateAChipseqArun;
  }



}
