
/**
 *
 * File.java 
 * @author echeng (table2type.pl)
 *  
 * the File object
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
@Table(name="file")
public class File extends WaspModel {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected int fileId;
  public void setFileId (int fileId) {
    this.fileId = fileId;
  }
  public int getFileId () {
    return this.fileId;
  }


  @Column(name="filelocation")
  protected String filelocation;
  public void setFilelocation (String filelocation) {
    this.filelocation = filelocation;
  }
  public String getFilelocation () {
    return this.filelocation;
  }


  @Column(name="contenttype")
  protected String contenttype;
  public void setContenttype (String contenttype) {
    this.contenttype = contenttype;
  }
  public String getContenttype () {
    return this.contenttype;
  }


  @Column(name="sizek")
  protected int sizek;
  public void setSizek (int sizek) {
    this.sizek = sizek;
  }
  public int getSizek () {
    return this.sizek;
  }


  @Column(name="md5hash")
  protected String md5hash;
  public void setMd5hash (String md5hash) {
    this.md5hash = md5hash;
  }
  public String getMd5hash () {
    return this.md5hash;
  }


  @Column(name="description")
  protected String description;
  public void setDescription (String description) {
    this.description = description;
  }
  public String getDescription () {
    return this.description;
  }


  @Column(name="isarchived")
  protected int isArchived;
  public void setIsArchived (int isArchived) {
    this.isArchived = isArchived;
  }
  public int getIsArchived () {
    return this.isArchived;
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
  @OneToMany
   @JoinColumn(name="fileid", insertable=false, updatable=false)
  protected List<JobFile> jobFile;
  public List<JobFile> getJobFile()  {
    return this.jobFile;
  }
  public void setJobFile (List<JobFile> jobFile)  {
    this.jobFile = jobFile;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="fileid", insertable=false, updatable=false)
  protected List<SampleFile> sampleFile;
  public List<SampleFile> getSampleFile()  {
    return this.sampleFile;
  }
  public void setSampleFile (List<SampleFile> sampleFile)  {
    this.sampleFile = sampleFile;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="fileid", insertable=false, updatable=false)
  protected List<RunFile> runFile;
  public List<RunFile> getRunFile()  {
    return this.runFile;
  }
  public void setRunFile (List<RunFile> runFile)  {
    this.runFile = runFile;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="fileid", insertable=false, updatable=false)
  protected List<RunLanefile> runLanefile;
  public List<RunLanefile> getRunLanefile()  {
    return this.runLanefile;
  }
  public void setRunLanefile (List<RunLanefile> runLanefile)  {
    this.runLanefile = runLanefile;
  }


  @NotAudited
  @OneToMany
   @JoinColumn(name="fileid", insertable=false, updatable=false)
  protected List<AChipseqArun> aChipseqArun;
  public List<AChipseqArun> getAChipseqArun()  {
    return this.aChipseqArun;
  }
  public void setAChipseqArun (List<AChipseqArun> aChipseqArun)  {
    this.aChipseqArun = aChipseqArun;
  }



}
