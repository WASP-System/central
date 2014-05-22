
/**
 *
 * JobDraftCellSelection.java 
 * @author echeng (table2type.pl)
 *  
 * the JobDraftCellSelection
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
@Table(name="jobdraftcellselection")
public class JobDraftCellSelection extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3766370740120140638L;

	/**
	 * setJobDraftCellSelectionId(Integer jobDraftCellSelectionId)
	 *
	 * @param jobDraftCelSelectionlId
	 *
	 */
	@Deprecated
	public void setJobDraftCellSelectionId (Integer jobDraftCellSelectionId) {
		setId(jobDraftCellSelectionId);
	}

	/**
	 * getJobDraftCellSelectionId()
	 *
	 * @return jobDraftCellSelectionId
	 *
	 */
	@Deprecated
	public Integer getJobDraftCellSelectionId () {
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
	 * cellindex
	 *
	 */
	@Column(name="cellindex")
	protected Integer cellIndex;

	/**
	 * setCellIndex(Integer cellIndex)
	 *
	 * @param cellIndex
	 *
	 */
	
	public void setCellIndex (Integer cellIndex) {
		this.cellIndex = cellIndex;
	}

	/**
	 * getCellIndex()
	 *
	 * @return cellIndex
	 *
	 */
	public Integer getCellIndex () {
		return this.cellIndex;
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


	/** 
	 * sampleDraftJobDraftCellSelection
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="jobdraftcellselectionid", insertable=false, updatable=false)
	protected List<SampleDraftJobDraftCellSelection> sampleDraftJobDraftCellSelection;


	/** 
	 * getSampleDraftCell()
	 *
	 * @return sampleDraftJobDraftCellSelection
	 *
	 */
	@JsonIgnore
	public List<SampleDraftJobDraftCellSelection> getSampleDraftJobDraftCellSelection() {
		return this.sampleDraftJobDraftCellSelection;
	}


	/** 
	 * setSampleDraftCell
	 *
	 * @param sampleDraftJobDraftCellSelection
	 *
	 */
	public void setSampleDraftJobDraftCellSelection (List<SampleDraftJobDraftCellSelection> sampleDraftJobDraftCellSelection) {
		this.sampleDraftJobDraftCellSelection = sampleDraftJobDraftCellSelection;
	}



}
