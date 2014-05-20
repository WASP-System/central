
/**
 *
 * JobSample.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSample
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="jobsample")
public class JobSample extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5192603804847507907L;
	

	/**
	 * setJobSampleId(Integer jobSampleId)
	 *
	 * @param jobSampleId
	 *
	 */
	@Deprecated
	public void setJobSampleId (Integer jobSampleId) {
		setId(jobSampleId);
	}

	/**
	 * getJobSampleId()
	 *
	 * @return jobSampleId
	 *
	 */
	@Deprecated
	public Integer getJobSampleId () {
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
	 * sampleId
	 *
	 */
	@Column(name="sampleid")
	protected Integer sampleId;

	/**
	 * setSampleId(Integer sampleId)
	 *
	 * @param sampleId
	 *
	 */
	
	public void setSampleId (Integer sampleId) {
		this.sampleId = sampleId;
	}

	/**
	 * getSampleId()
	 *
	 * @return sampleId
	 *
	 */
	public Integer getSampleId () {
		return this.sampleId;
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
	 * sample
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="sampleid", insertable=false, updatable=false)
	protected Sample sample;

	/**
	 * setSample (Sample sample)
	 *
	 * @param sample
	 *
	 */
	public void setSample (Sample sample) {
		this.sample = sample;
		this.sampleId = sample.getId();
	}

	/**
	 * getSample ()
	 *
	 * @return sample
	 *
	 */
	
	public Sample getSample () {
		return this.sample;
	}


	/** 
	 * jobSampleMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="jobsampleid", insertable=false, updatable=false)
	protected List<JobSampleMeta> jobSampleMeta;


	/** 
	 * getJobSampleMeta()
	 *
	 * @return jobSampleMeta
	 *
	 */
	@JsonIgnore
	public List<JobSampleMeta> getJobSampleMeta() {
		return this.jobSampleMeta;
	}


	/** 
	 * setJobSampleMeta
	 *
	 * @param jobSampleMeta
	 *
	 */
	public void setJobSampleMeta (List<JobSampleMeta> jobSampleMeta) {
		this.jobSampleMeta = jobSampleMeta;
	}



}
