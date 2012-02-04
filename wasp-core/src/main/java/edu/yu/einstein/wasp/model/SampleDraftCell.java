
/**
 *
 * SampleDraftCell.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleDraftCell
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
@Table(name="sampledraftcell")
public class SampleDraftCell extends WaspModel {

	/** 
	 * sampleDraftCellId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer sampleDraftCellId;

	/**
	 * setSampleDraftCellId(Integer sampleDraftCellId)
	 *
	 * @param sampleDraftCellId
	 *
	 */
	
	public void setSampleDraftCellId (Integer sampleDraftCellId) {
		this.sampleDraftCellId = sampleDraftCellId;
	}

	/**
	 * getSampleDraftCellId()
	 *
	 * @return sampleDraftCellId
	 *
	 */
	public Integer getSampleDraftCellId () {
		return this.sampleDraftCellId;
	}




	/** 
	 * sampledraftId
	 *
	 */
	@Column(name="sampledraftid")
	protected Integer sampledraftId;

	/**
	 * setSampledraftId(Integer sampledraftId)
	 *
	 * @param sampledraftId
	 *
	 */
	
	public void setSampledraftId (Integer sampledraftId) {
		this.sampledraftId = sampledraftId;
	}

	/**
	 * getSampledraftId()
	 *
	 * @return sampledraftId
	 *
	 */
	public Integer getSampledraftId () {
		return this.sampledraftId;
	}




	/** 
	 * jobdraftcellId
	 *
	 */
	@Column(name="jobdraftcellid")
	protected Integer jobdraftcellId;

	/**
	 * setJobdraftcellId(Integer jobdraftcellId)
	 *
	 * @param jobdraftcellId
	 *
	 */
	
	public void setJobdraftcellId (Integer jobdraftcellId) {
		this.jobdraftcellId = jobdraftcellId;
	}

	/**
	 * getJobdraftcellId()
	 *
	 * @return jobdraftcellId
	 *
	 */
	public Integer getJobdraftcellId () {
		return this.jobdraftcellId;
	}




	/** 
	 * libraryindex
	 *
	 */
	@Column(name="libraryindex")
	protected Integer libraryindex;

	/**
	 * setLibraryindex(Integer libraryindex)
	 *
	 * @param libraryindex
	 *
	 */
	
	public void setLibraryindex (Integer libraryindex) {
		this.libraryindex = libraryindex;
	}

	/**
	 * getLibraryindex()
	 *
	 * @return libraryindex
	 *
	 */
	public Integer getLibraryindex () {
		return this.libraryindex;
	}




	/**
	 * sampleDraft
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="sampledraftid", insertable=false, updatable=false)
	protected SampleDraft sampleDraft;

	/**
	 * setSampleDraft (SampleDraft sampleDraft)
	 *
	 * @param sampleDraft
	 *
	 */
	public void setSampleDraft (SampleDraft sampleDraft) {
		this.sampleDraft = sampleDraft;
		this.sampledraftId = sampleDraft.sampleDraftId;
	}

	/**
	 * getSampleDraft ()
	 *
	 * @return sampleDraft
	 *
	 */
	
	public SampleDraft getSampleDraft () {
		return this.sampleDraft;
	}


	/**
	 * jobDraftCell
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="jobdraftcellid", insertable=false, updatable=false)
	protected JobDraftCell jobDraftCell;

	/**
	 * setJobDraftCell (JobDraftCell jobDraftCell)
	 *
	 * @param jobDraftCell
	 *
	 */
	public void setJobDraftCell (JobDraftCell jobDraftCell) {
		this.jobDraftCell = jobDraftCell;
		this.jobdraftcellId = jobDraftCell.jobDraftCellId;
	}

	/**
	 * getJobDraftCell ()
	 *
	 * @return jobDraftCell
	 *
	 */
	
	public JobDraftCell getJobDraftCell () {
		return this.jobDraftCell;
	}


}
