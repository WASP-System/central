
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

import javax.persistence.Column;
import javax.persistence.Entity;
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
	 * setJobDraftFileId(Integer jobDraftFileId)
	 *
	 * @param jobDraftFileId
	 *
	 */
	@Deprecated
	public void setJobDraftFileId (Integer jobDraftFileId) {
		setId(jobDraftFileId);
	}

	/**
	 * getJobDraftFileId()
	 *
	 * @return jobDraftFileId
	 *
	 */
	@Deprecated
	public Integer getJobDraftFileId () {
		return getId();
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
		this.jobDraftId = jobDraft.getId();
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
	
	@Column(name="filegroupid", insertable=false, updatable=false)
	private Integer fileGroupId;
	
	/**
	 * @return the fileGroupId
	 */
	@Deprecated
	public Integer getFileGroupId() {
		return fileGroupId;
	}

	/**
	 * @param fileGroupId the fileGroupId to set
	 */
	@Deprecated
	public void setFileGroupId(Integer fileGroupId) {
		this.fileGroupId = fileGroupId;
	}


}
