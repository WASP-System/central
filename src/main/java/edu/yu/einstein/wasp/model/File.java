
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

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import org.hibernate.validator.constraints.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="file")
public class File extends WaspModel {

	/** 
	 * fileId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int fileId;

	/**
	 * setFileId(int fileId)
	 *
	 * @param fileId
	 *
	 */
	
	public void setFileId (int fileId) {
		this.fileId = fileId;
	}

	/**
	 * getFileId()
	 *
	 * @return fileId
	 *
	 */
	public int getFileId () {
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
	protected int sizek;

	/**
	 * setSizek(int sizek)
	 *
	 * @param sizek
	 *
	 */
	
	public void setSizek (int sizek) {
		this.sizek = sizek;
	}

	/**
	 * getSizek()
	 *
	 * @return sizek
	 *
	 */
	public int getSizek () {
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
	protected int isArchived;

	/**
	 * setIsArchived(int isArchived)
	 *
	 * @param isArchived
	 *
	 */
	
	public void setIsArchived (int isArchived) {
		this.isArchived = isArchived;
	}

	/**
	 * getIsArchived()
	 *
	 * @return isArchived
	 *
	 */
	public int getIsArchived () {
		return this.isArchived;
	}




	/** 
	 * isActive
	 *
	 */
	@Column(name="isactive")
	protected int isActive;

	/**
	 * setIsActive(int isActive)
	 *
	 * @param isActive
	 *
	 */
	
	public void setIsActive (int isActive) {
		this.isActive = isActive;
	}

	/**
	 * getIsActive()
	 *
	 * @return isActive
	 *
	 */
	public int getIsActive () {
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
	protected int lastUpdUser;

	/**
	 * setLastUpdUser(int lastUpdUser)
	 *
	 * @param lastUpdUser
	 *
	 */
	
	public void setLastUpdUser (int lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	/**
	 * getLastUpdUser()
	 *
	 * @return lastUpdUser
	 *
	 */
	public int getLastUpdUser () {
		return this.lastUpdUser;
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
