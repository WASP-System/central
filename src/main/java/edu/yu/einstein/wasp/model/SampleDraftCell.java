
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

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import org.hibernate.validator.constraints.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="sampledraftcell")
public class SampleDraftCell extends WaspModel {

	/** 
	 * sampleDraftCellId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int sampleDraftCellId;

	/**
	 * setSampleDraftCellId(int sampleDraftCellId)
	 *
	 * @param sampleDraftCellId
	 *
	 */
	
	public void setSampleDraftCellId (int sampleDraftCellId) {
		this.sampleDraftCellId = sampleDraftCellId;
	}

	/**
	 * getSampleDraftCellId()
	 *
	 * @return sampleDraftCellId
	 *
	 */
	public int getSampleDraftCellId () {
		return this.sampleDraftCellId;
	}




	/** 
	 * sampledraftId
	 *
	 */
	@Column(name="sampledraftid")
	protected int sampledraftId;

	/**
	 * setSampledraftId(int sampledraftId)
	 *
	 * @param sampledraftId
	 *
	 */
	
	public void setSampledraftId (int sampledraftId) {
		this.sampledraftId = sampledraftId;
	}

	/**
	 * getSampledraftId()
	 *
	 * @return sampledraftId
	 *
	 */
	public int getSampledraftId () {
		return this.sampledraftId;
	}




	/** 
	 * jobdraftcellId
	 *
	 */
	@Column(name="jobdraftcellid")
	protected int jobdraftcellId;

	/**
	 * setJobdraftcellId(int jobdraftcellId)
	 *
	 * @param jobdraftcellId
	 *
	 */
	
	public void setJobdraftcellId (int jobdraftcellId) {
		this.jobdraftcellId = jobdraftcellId;
	}

	/**
	 * getJobdraftcellId()
	 *
	 * @return jobdraftcellId
	 *
	 */
	public int getJobdraftcellId () {
		return this.jobdraftcellId;
	}




	/** 
	 * libraryindex
	 *
	 */
	@Column(name="libraryindex")
	protected int libraryindex;

	/**
	 * setLibraryindex(int libraryindex)
	 *
	 * @param libraryindex
	 *
	 */
	
	public void setLibraryindex (int libraryindex) {
		this.libraryindex = libraryindex;
	}

	/**
	 * getLibraryindex()
	 *
	 * @return libraryindex
	 *
	 */
	public int getLibraryindex () {
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
