
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

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import org.hibernate.validator.constraints.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="jobdraftmeta")
public class JobDraftMeta extends MetaBase {

	/** 
	 * jobDraftMetaId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer jobDraftMetaId;

	/**
	 * setJobDraftMetaId(Integer jobDraftMetaId)
	 *
	 * @param jobDraftMetaId
	 *
	 */
	
	public void setJobDraftMetaId (Integer jobDraftMetaId) {
		this.jobDraftMetaId = jobDraftMetaId;
	}

	/**
	 * getJobDraftMetaId()
	 *
	 * @return jobDraftMetaId
	 *
	 */
	public Integer getJobDraftMetaId () {
		return this.jobDraftMetaId;
	}




	/** 
	 * jobdraftId
	 *
	 */
	@Column(name="jobdraftid")
	protected Integer jobdraftId;

	/**
	 * setJobdraftId(Integer jobdraftId)
	 *
	 * @param jobdraftId
	 *
	 */
	
	public void setJobdraftId (Integer jobdraftId) {
		this.jobdraftId = jobdraftId;
	}

	/**
	 * getJobdraftId()
	 *
	 * @return jobdraftId
	 *
	 */
	public Integer getJobdraftId () {
		return this.jobdraftId;
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
		this.jobdraftId = jobDraft.jobDraftId;
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
