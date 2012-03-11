
/**
 *
 * File.java 
 * @author echeng (table2type.pl)
 *  
 * the File
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="file")
public class File extends WaspModel {

	/** 
	 * fileId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer fileId;

	/**
	 * setFileId(Integer fileId)
	 *
	 * @param fileId
	 *
	 */
	
	public void setFileId (Integer fileId) {
		this.fileId = fileId;
	}

	/**
	 * getFileId()
	 *
	 * @return fileId
	 *
	 */
	public Integer getFileId () {
		return this.fileId;
	}




	/** 
	 * filelocation
	 *
	 */
	@Column(name="filelocation")
	protected String filelocation;

	/**
	 * setFilelocation(String filelocation)
	 *
	 * @param filelocation
	 *
	 */
	
	public void setFilelocation (String filelocation) {
		this.filelocation = filelocation;
	}

	/**
	 * getFilelocation()
	 *
	 * @return filelocation
	 *
	 */
	public String getFilelocation () {
		return this.filelocation;
	}




	/** 
	 * contenttype
	 *
	 */
	@Column(name="contenttype")
	protected String contenttype;

	/**
	 * setContenttype(String contenttype)
	 *
	 * @param contenttype
	 *
	 */
	
	public void setContenttype (String contenttype) {
		this.contenttype = contenttype;
	}

	/**
	 * getContenttype()
	 *
	 * @return contenttype
	 *
	 */
	public String getContenttype () {
		return this.contenttype;
	}




	/** 
	 * sizek
	 *
	 */
	@Column(name="sizek")
	protected Integer sizek;

	/**
	 * setSizek(Integer sizek)
	 *
	 * @param sizek
	 *
	 */
	
	public void setSizek (Integer sizek) {
		this.sizek = sizek;
	}

	/**
	 * getSizek()
	 *
	 * @return sizek
	 *
	 */
	public Integer getSizek () {
		return this.sizek;
	}




	/** 
	 * md5hash
	 *
	 */
	@Column(name="md5hash")
	protected String md5hash;

	/**
	 * setMd5hash(String md5hash)
	 *
	 * @param md5hash
	 *
	 */
	
	public void setMd5hash (String md5hash) {
		this.md5hash = md5hash;
	}

	/**
	 * getMd5hash()
	 *
	 * @return md5hash
	 *
	 */
	public String getMd5hash () {
		return this.md5hash;
	}




	/** 
	 * description
	 *
	 */
	@Column(name="description")
	protected String description;

	/**
	 * setDescription(String description)
	 *
	 * @param description
	 *
	 */
	
	public void setDescription (String description) {
		this.description = description;
	}

	/**
	 * getDescription()
	 *
	 * @return description
	 *
	 */
	public String getDescription () {
		return this.description;
	}




	/** 
	 * isArchived
	 *
	 */
	@Column(name="isarchived")
	protected Integer isArchived;

	/**
	 * setIsArchived(Integer isArchived)
	 *
	 * @param isArchived
	 *
	 */
	
	public void setIsArchived (Integer isArchived) {
		this.isArchived = isArchived;
	}

	/**
	 * getIsArchived()
	 *
	 * @return isArchived
	 *
	 */
	public Integer getIsArchived () {
		return this.isArchived;
	}




	/** 
	 * isActive
	 *
	 */
	@Column(name="isactive")
	protected Integer isActive;

	/**
	 * setIsActive(Integer isActive)
	 *
	 * @param isActive
	 *
	 */
	
	public void setIsActive (Integer isActive) {
		this.isActive = isActive;
	}

	/**
	 * getIsActive()
	 *
	 * @return isActive
	 *
	 */
	public Integer getIsActive () {
		return this.isActive;
	}




	/** 
	 * lastUpdTs
	 *
	 */
	@Column(name="lastupdts")
	protected Date lastUpdTs;

	/**
	 * setLastUpdTs(Date lastUpdTs)
	 *
	 * @param lastUpdTs
	 *
	 */
	
	public void setLastUpdTs (Date lastUpdTs) {
		this.lastUpdTs = lastUpdTs;
	}

	/**
	 * getLastUpdTs()
	 *
	 * @return lastUpdTs
	 *
	 */
	public Date getLastUpdTs () {
		return this.lastUpdTs;
	}




	/** 
	 * lastUpdUser
	 *
	 */
	@Column(name="lastupduser")
	protected Integer lastUpdUser;

	/**
	 * setLastUpdUser(Integer lastUpdUser)
	 *
	 * @param lastUpdUser
	 *
	 */
	
	public void setLastUpdUser (Integer lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	/**
	 * getLastUpdUser()
	 *
	 * @return lastUpdUser
	 *
	 */
	public Integer getLastUpdUser () {
		return this.lastUpdUser;
	}




	/** 
	 * sampleDraft
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="fileid", insertable=false, updatable=false)
	protected List<SampleDraft> sampleDraft;


	/** 
	 * getSampleDraft()
	 *
	 * @return sampleDraft
	 *
	 */
	@JsonIgnore
	public List<SampleDraft> getSampleDraft() {
		return this.sampleDraft;
	}


	/** 
	 * setSampleDraft
	 *
	 * @param sampleDraft
	 *
	 */
	public void setSampleDraft (List<SampleDraft> sampleDraft) {
		this.sampleDraft = sampleDraft;
	}



	/** 
	 * jobFile
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="fileid", insertable=false, updatable=false)
	protected List<JobFile> jobFile;


	/** 
	 * getJobFile()
	 *
	 * @return jobFile
	 *
	 */
	@JsonIgnore
	public List<JobFile> getJobFile() {
		return this.jobFile;
	}


	/** 
	 * setJobFile
	 *
	 * @param jobFile
	 *
	 */
	public void setJobFile (List<JobFile> jobFile) {
		this.jobFile = jobFile;
	}



	/** 
	 * sampleFile
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="fileid", insertable=false, updatable=false)
	protected List<SampleFile> sampleFile;


	/** 
	 * getSampleFile()
	 *
	 * @return sampleFile
	 *
	 */
	@JsonIgnore
	public List<SampleFile> getSampleFile() {
		return this.sampleFile;
	}


	/** 
	 * setSampleFile
	 *
	 * @param sampleFile
	 *
	 */
	public void setSampleFile (List<SampleFile> sampleFile) {
		this.sampleFile = sampleFile;
	}



	/** 
	 * runFile
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="fileid", insertable=false, updatable=false)
	protected List<RunFile> runFile;


	/** 
	 * getRunFile()
	 *
	 * @return runFile
	 *
	 */
	@JsonIgnore
	public List<RunFile> getRunFile() {
		return this.runFile;
	}


	/** 
	 * setRunFile
	 *
	 * @param runFile
	 *
	 */
	public void setRunFile (List<RunFile> runFile) {
		this.runFile = runFile;
	}



	/** 
	 * runLanefile
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="fileid", insertable=false, updatable=false)
	protected List<RunLanefile> runLanefile;


	/** 
	 * getRunLanefile()
	 *
	 * @return runLanefile
	 *
	 */
	@JsonIgnore
	public List<RunLanefile> getRunLanefile() {
		return this.runLanefile;
	}


	/** 
	 * setRunLanefile
	 *
	 * @param runLanefile
	 *
	 */
	public void setRunLanefile (List<RunLanefile> runLanefile) {
		this.runLanefile = runLanefile;
	}



}
