
/**
 *
 * JobMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the JobMeta
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
@Table(name="jobmeta")
public class JobMeta extends MetaBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5212501925724259984L;
	
	/**
	 * setJobMetaId(Integer jobMetaId)
	 *
	 * @param jobMetaId
	 *
	 */
	@Deprecated
	public void setJobMetaId (Integer jobMetaId) {
		setId(jobMetaId);
	}

	/**
	 * getJobMetaId()
	 *
	 * @return jobMetaId
	 *
	 */
	@Deprecated
	public Integer getJobMetaId () {
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


}
