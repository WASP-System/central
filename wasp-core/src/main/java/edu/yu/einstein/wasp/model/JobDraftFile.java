
/**
 *
 * JobDraftFile.java 
 * @author asmclellan
 *  
 * the JobDraftFile
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="jobdraftfile")
public class JobDraftFile extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4858173587672071138L;
	/** 
	 * jobDraftFileId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer jobDraftFileId;

	/**
	 * setJobDraftFileId(Integer jobDraftFileId)
	 *
	 * @param jobDraftFileId
	 *
	 */
	
	public void setJobDraftFileId (Integer jobDraftFileId) {
		this.jobDraftFileId = jobDraftFileId;
	}

	/**
	 * getJobDraftFileId()
	 *
	 * @return jobDraftFileId
	 *
	 */
	public Integer getJobDraftFileId () {
		return this.jobDraftFileId;
	}




	/** 
	 * jobDraftId
	 *
	 */
	@Column(name="jobdraftid")
	protected Integer jobDraftId;

	/**
	 * setJobDraftId(Integer jobDraftId)
	 *
	 * @param jobDraftId
	 *
	 */
	
	public void setJobDraftId (Integer jobDraftId) {
		this.jobDraftId = jobDraftId;
	}

	/**
	 * getJobDraftId()
	 *
	 * @return jobDraftId
	 *
	 */
	public Integer getJobDraftId () {
		return this.jobDraftId;
	}




	/** 
	 * fileGroupId
	 *
	 */
	@Column(name="filegroupid")
	protected Integer fileGroupId;

	/**
	 * setFileId(Integer fileGroupId)
	 *
	 * @param filegroupId
	 *
	 */
	
	public void setFileGroupId (Integer fileGroupId) {
		this.fileGroupId = fileGroupId;
	}

	/**
	 * getFileId()
	 *
	 * @return fileId
	 *
	 */
	public Integer getFileGroupId () {
		return this.fileGroupId;
	}




	/** 
	 * iName
	 *
	 */
	@Column(name="iname")
	protected String iName;

	/**
	 * setIName(String iName)
	 *
	 * @param iName
	 *
	 */
	
	public void setIName (String iName) {
		this.iName = iName;
	}

	/**
	 * getIName()
	 *
	 * @return iName
	 *
	 */
	public String getIName () {
		return this.iName;
	}




	/** 
	 * name
	 *
	 */
	@Column(name="name")
	protected String name;

	/**
	 * setName(String name)
	 *
	 * @param name
	 *
	 */
	
	public void setName (String name) {
		this.name = name;
	}

	/**
	 * getName()
	 *
	 * @return name
	 *
	 */
	public String getName () {
		return this.name;
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
	 * jobDraft
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="jobdraftid", insertable=false, updatable=false)
	protected JobDraft jobDraft;

	/**
	 * setJobDraft (JobDraft jobDraft)
	 *
	 * @param jobDraft
	 *
	 */
	public void setJobDraft (JobDraft jobDraft) {
		this.jobDraft = jobDraft;
		this.jobDraftId = jobDraft.jobDraftId;
	}

	/**
	 * getJobDraft ()
	 *
	 * @return jobDraft
	 *
	 */
	
	public JobDraft getJobDraft() {
		return this.jobDraft;
	}


	/**
	 * file
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="filegroupid")
	protected FileGroup fileGroup;

	/**
	 * setFile (FileGroup fileGroup)
	 *
	 * @param fileGroup
	 *
	 */
	public void setFileGroup(FileGroup fileGroup) {
		this.fileGroup = fileGroup;
		this.fileGroupId = fileGroup.fileGroupId;
	}

	/**
	 * getFile ()
	 *
	 * @return file
	 *
	 */
	
	public FileGroup getFileGroup() {
		return this.fileGroup;
	}


}
