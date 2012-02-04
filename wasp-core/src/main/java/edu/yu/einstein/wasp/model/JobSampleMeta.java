
/**
 *
 * JobSampleMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the JobSampleMeta
 *
 *
 */

package edu.yu.einstein.wasp.model;

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
@Table(name="jobsamplemeta")
public class JobSampleMeta extends MetaBase {

	/** 
	 * jobSampleMetaId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer jobSampleMetaId;

	/**
	 * setJobSampleMetaId(Integer jobSampleMetaId)
	 *
	 * @param jobSampleMetaId
	 *
	 */
	
	public void setJobSampleMetaId (Integer jobSampleMetaId) {
		this.jobSampleMetaId = jobSampleMetaId;
	}

	/**
	 * getJobSampleMetaId()
	 *
	 * @return jobSampleMetaId
	 *
	 */
	public Integer getJobSampleMetaId () {
		return this.jobSampleMetaId;
	}




	/** 
	 * jobsampleId
	 *
	 */
	@Column(name="jobsampleid")
	protected Integer jobsampleId;

	/**
	 * setJobsampleId(Integer jobsampleId)
	 *
	 * @param jobsampleId
	 *
	 */
	
	public void setJobsampleId (Integer jobsampleId) {
		this.jobsampleId = jobsampleId;
	}

	/**
	 * getJobsampleId()
	 *
	 * @return jobsampleId
	 *
	 */
	public Integer getJobsampleId () {
		return this.jobsampleId;
	}




	/**
	 * jobSample
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="jobsampleid", insertable=false, updatable=false)
	protected JobSample jobSample;

	/**
	 * setJobSample (JobSample jobSample)
	 *
	 * @param jobSample
	 *
	 */
	public void setJobSample (JobSample jobSample) {
		this.jobSample = jobSample;
		this.jobsampleId = jobSample.jobSampleId;
	}

	/**
	 * getJobSample ()
	 *
	 * @return jobSample
	 *
	 */
	
	public JobSample getJobSample () {
		return this.jobSample;
	}


}
