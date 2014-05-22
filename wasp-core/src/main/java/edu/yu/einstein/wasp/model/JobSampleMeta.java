
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
	 * 
	 */
	private static final long serialVersionUID = 3037848570338754829L;

	/**
	 * setJobSampleMetaId(Integer jobSampleMetaId)
	 *
	 * @param jobSampleMetaId
	 *
	 */
	@Deprecated
	public void setJobSampleMetaId (Integer jobSampleMetaId) {
		setId(jobSampleMetaId);
	}

	/**
	 * getJobSampleMetaId()
	 *
	 * @return jobSampleMetaId
	 *
	 */
	@Deprecated
	public Integer getJobSampleMetaId () {
		return getId();
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
		this.jobsampleId = jobSample.getId();
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
