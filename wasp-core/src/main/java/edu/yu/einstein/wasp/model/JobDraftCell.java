
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

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="jobdraftcell")
public class JobDraftCell extends WaspModel {

	/** 
	 * jobDraftCellId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer jobDraftCellId;

	/**
	 * setJobDraftCellId(Integer jobDraftCellId)
	 *
	 * @param jobDraftCellId
	 *
	 */
	
	public void setJobDraftCellId (Integer jobDraftCellId) {
		this.jobDraftCellId = jobDraftCellId;
	}

	/**
	 * getJobDraftCellId()
	 *
	 * @return jobDraftCellId
	 *
	 */
	public Integer getJobDraftCellId () {
		return this.jobDraftCellId;
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
	 * cellindex
	 *
	 */
	@Column(name="cellindex")
	protected Integer cellindex;

	/**
	 * setCellindex(Integer cellindex)
	 *
	 * @param cellindex
	 *
	 */
	
	public void setCellindex (Integer cellindex) {
		this.cellindex = cellindex;
	}

	/**
	 * getCellindex()
	 *
	 * @return cellindex
	 *
	 */
	public Integer getCellindex () {
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
	@JsonIgnore
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
