
/**
 *
 * JobSoftware.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSoftware
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
@Table(name="jobsoftware")
public class JobSoftware extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -786768268152802788L;
	/** 
	 * jobSoftwareId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer jobSoftwareId;

	/**
	 * setJobSoftwareId(Integer jobSoftwareId)
	 *
	 * @param jobSoftwareId
	 *
	 */
	
	public void setJobSoftwareId (Integer jobSoftwareId) {
		this.jobSoftwareId = jobSoftwareId;
	}

	/**
	 * getJobSoftwareId()
	 *
	 * @return jobSoftwareId
	 *
	 */
	public Integer getJobSoftwareId () {
		return this.jobSoftwareId;
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
	 * softwareId
	 *
	 */
	@Column(name="softwareid")
	protected Integer softwareId;

	/**
	 * setSoftwareId(Integer softwareId)
	 *
	 * @param softwareId
	 *
	 */
	
	public void setSoftwareId (Integer softwareId) {
		this.softwareId = softwareId;
	}

	/**
	 * getSoftwareId()
	 *
	 * @return softwareId
	 *
	 */
	public Integer getSoftwareId () {
		return this.softwareId;
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
		this.jobId = job.jobId;
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
	 * software
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="softwareid", insertable=false, updatable=false)
	protected Software software;

	/**
	 * setSoftware (Software software)
	 *
	 * @param software
	 *
	 */
	public void setSoftware (Software software) {
		this.software = software;
		this.softwareId = software.softwareId;
	}

	/**
	 * getSoftware ()
	 *
	 * @return software
	 *
	 */
	
	public Software getSoftware () {
		return this.software;
	}


}
