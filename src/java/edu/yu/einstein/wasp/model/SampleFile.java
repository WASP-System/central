
/**
 *
 * SampleFile.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleFile object
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
@Table(name="samplefile")
public class SampleFile extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int sampleFileId;
  public void setSampleFileId (int sampleFileId) {
    this.sampleFileId = sampleFileId;
  }
  public int getSampleFileId () {
    return this.sampleFileId;
  }


  @Column(name="sampleid")
  protected int sampleId;
  public void setSampleId (int sampleId) {
    this.sampleId = sampleId;
  }
  public int getSampleId () {
    return this.sampleId;
  }


  @Column(name="fileid")
  protected int fileId;
  public void setFileId (int fileId) {
    this.fileId = fileId;
  }
  public int getFileId () {
    return this.fileId;
  }


  @Column(name="iname")
  protected String iName;
  public void setIName (String iName) {
    this.iName = iName;
  }
  public String getIName () {
    return this.iName;
  }


  @Column(name="name")
  protected String name;
  public void setName (String name) {
    this.name = name;
  }
  public String getName () {
    return this.name;
  }


  @Column(name="description")
  protected String description;
  public void setDescription (String description) {
    this.description = description;
  }
  public String getDescription () {
    return this.description;
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
   @JoinColumn(name="sampleid", insertable=false, updatable=false)
  protected Sample sample;
  public void setSample (Sample sample) {
    this.sample = sample;
    this.sampleId = sample.sampleId;
  }
  public Sample getSample () {
    return this.sample;
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
