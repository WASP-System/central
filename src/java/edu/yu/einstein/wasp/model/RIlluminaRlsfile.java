
/**
 *
 * RIlluminaRlsfile.java 
 * @author echeng (table2type.pl)
 *  
 * the RIlluminaRlsfile object
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
@Table(name="r_illumina_rlsfile")
public class RIlluminaRlsfile extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int rlsfileId;
  public void setRlsfileId (int rlsfileId) {
    this.rlsfileId = rlsfileId;
  }
  public int getRlsfileId () {
    return this.rlsfileId;
  }


  @Column(name="rlsid")
  protected int rlsId;
  public void setRlsId (int rlsId) {
    this.rlsId = rlsId;
  }
  public int getRlsId () {
    return this.rlsId;
  }


  @Column(name="fileid")
  protected int fileId;
  public void setFileId (int fileId) {
    this.fileId = fileId;
  }
  public int getFileId () {
    return this.fileId;
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
  @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name="rlsid", insertable=false, updatable=false)
  protected RIlluminaRunlanesample rIlluminaRunlanesample;
  public void setRIlluminaRunlanesample (RIlluminaRunlanesample rIlluminaRunlanesample) {
    this.rIlluminaRunlanesample = rIlluminaRunlanesample;
    this.rlsId = rIlluminaRunlanesample.runlanesampleId;
  }
  public RIlluminaRunlanesample getRIlluminaRunlanesample () {
    return this.rIlluminaRunlanesample;
  }

  @NotAudited
  @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name="fileid", insertable=false, updatable=false)
  protected File file;
  public void setFile (File file) {
    this.file = file;
    this.fileId = file.fileId;
  }
  public File getFile () {
    return this.file;
  }

}
