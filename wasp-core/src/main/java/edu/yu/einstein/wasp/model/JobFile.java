
/**
 *
 * JobFile.java 
 * @author echeng (table2type.pl)
 *  
 * the JobFile
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
@Table(name="jobfile")
public class JobFile extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -743310016092763973L;

	/**
	 * setJobFileId(Integer jobFileId)
	 *
	 * @param jobFileId
	 *
	 */
	@Deprecated
	public void setJobFileId (Integer jobFileId) {
		setId(jobFileId);
	}

	/**
	 * getJobFileId()
	 *
	 * @return jobFileId
	 *
	 */
	@Deprecated
	public Integer getJobFileId () {
		return getId();
	}




	/** 
	 * jobId
	 *
	 */
	@Column(name="jobid")
	protected Integer jobId;

	/**
	 * setJobId(Integer jobId)
	 *
	 * @param jobId
	 *
	 */
	
	public void setJobId (Integer jobId) {
		this.jobId = jobId;
	}

	/**
	 * getJobId()
	 *
	 * @return jobId
	 *
	 */
	public Integer getJobId () {
		return this.jobId;
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
	 * job
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="jobid", insertable=false, updatable=false)
	protected Job job;

	/**
	 * setJob (Job job)
	 *
	 * @param job
	 *
	 */
	public void setJob (Job job) {
		this.job = job;
		this.jobId = job.getId();
	}

	/**
	 * getJob ()
	 *
	 * @return job
	 *
	 */
	
	public Job getJob () {
		return this.job;
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
	 * setFile (FileHandle file)
	 *
	 * @param file
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
	
	public FileGroup getFile () {
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
