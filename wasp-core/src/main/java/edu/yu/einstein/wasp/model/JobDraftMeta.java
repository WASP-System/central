
/**
 *
 * JobDraftMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftMeta
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
@Table(name="jobdraftmeta")
public class JobDraftMeta extends MetaBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8631916645941369570L;
	
	/**
	 * setJobDraftMetaId(Integer jobDraftMetaId)
	 *
	 * @param jobDraftMetaId
	 *
	 */
	@Deprecated
	public void setJobDraftMetaId (Integer jobDraftMetaId) {
		setId(jobDraftMetaId);
	}

	/**
	 * getJobDraftMetaId()
	 *
	 * @return jobDraftMetaId
	 *
	 */
	@Deprecated
	public Integer getJobDraftMetaId () {
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
	
	public JobDraft getJobDraft () {
		return this.jobDraft;
	}


}
