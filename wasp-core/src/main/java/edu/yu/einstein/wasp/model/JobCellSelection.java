
/**
 *
 * JobCellSelection.java 
 * @author echeng (table2type.pl)
 *  
 * the JobCellSelection
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
@Table(name="jobcellselection")
public class JobCellSelection extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7027295909006312786L;

	/**
	 * setjobCellSelectionId(Integer jobCellSelectionId)
	 *
	 * @param jobCellSelectionId
	 *
	 */
	@Deprecated
	public void setJobCellSelectionId (Integer jobCellSelectionId) {
		setId(jobCellSelectionId);
	}

	/**
	 * getJobCellSelectionId()
	 *
	 * @return jobCellSelectionId
	 *
	 */
	@Deprecated
	public Integer getJobCellSelectionId () {
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
	 * cellindex
	 *
	 */
	@Column(name="cellindex")
	protected Integer cellIndex;

	/**
	 * setCellindex(Integer cellIndex)
	 *
	 * @param cellindex
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


	/** 
	 * sampleJobCellSelection
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="jobcellselectionid", insertable=false, updatable=false)
	protected List<SampleJobCellSelection> sampleJobCellSelection;


	/** 
	 * getSampleCell()
	 *
	 * @return sampleJobCellSelection
	 *
	 */
	@JsonIgnore
	public List<SampleJobCellSelection> getSampleJobCellSelection() {
		return this.sampleJobCellSelection;
	}


	/** 
	 * setSampleCell
	 *
	 * @param sampleJobCellSelection
	 *
	 */
	public void setSampleJobCellSelection (List<SampleJobCellSelection> sampleJobCellSelection) {
		this.sampleJobCellSelection = sampleJobCellSelection;
	}



}
