
/**
 *
 * JobDraftCell.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftCell
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
@Table(name="jobdraftcell")
public class JobDraftCell extends WaspModel {

	/** 
	 * jobDraftCellId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int jobDraftCellId;

	/**
	 * setJobDraftCellId(int jobDraftCellId)
	 *
	 * @param jobDraftCellId
	 *
	 */
	
	public void setJobDraftCellId (int jobDraftCellId) {
		this.jobDraftCellId = jobDraftCellId;
	}

	/**
	 * getJobDraftCellId()
	 *
	 * @return jobDraftCellId
	 *
	 */
	public int getJobDraftCellId () {
		return this.jobDraftCellId;
	}




	/** 
	 * jobdraftId
	 *
	 */
	@Column(name="jobdraftid")
	protected int jobdraftId;

	/**
	 * setJobdraftId(int jobdraftId)
	 *
	 * @param jobdraftId
	 *
	 */
	
	public void setJobdraftId (int jobdraftId) {
		this.jobdraftId = jobdraftId;
	}

	/**
	 * getJobdraftId()
	 *
	 * @return jobdraftId
	 *
	 */
	public int getJobdraftId () {
		return this.jobdraftId;
	}




	/** 
	 * cellindex
	 *
	 */
	@Column(name="cellindex")
	protected int cellindex;

	/**
	 * setCellindex(int cellindex)
	 *
	 * @param cellindex
	 *
	 */
	
	public void setCellindex (int cellindex) {
		this.cellindex = cellindex;
	}

	/**
	 * getCellindex()
	 *
	 * @return cellindex
	 *
	 */
	public int getCellindex () {
		return this.cellindex;
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


	/** 
	 * sampleDraftCell
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="jobdraftcellid", insertable=false, updatable=false)
	protected List<SampleDraftCell> sampleDraftCell;


	/** 
	 * getSampleDraftCell()
	 *
	 * @return sampleDraftCell
	 *
	 */
	public List<SampleDraftCell> getSampleDraftCell() {
		return this.sampleDraftCell;
	}


	/** 
	 * setSampleDraftCell
	 *
	 * @param sampleDraftCell
	 *
	 */
	public void setSampleDraftCell (List<SampleDraftCell> sampleDraftCell) {
		this.sampleDraftCell = sampleDraftCell;
	}



}
