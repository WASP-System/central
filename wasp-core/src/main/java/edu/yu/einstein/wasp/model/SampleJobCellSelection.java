
/**
 *
 * SampleJobCellSelection.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleJobCellSelection
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
@Table(name="samplejobcellselection")
public class SampleJobCellSelection extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7130784653382532399L;
	

	/**
	 * setSampleJobCellSelectionId(Integer sampleJobCellSelectionId)
	 *
	 * @param sampleJobCellSelectionId
	 *
	 */
	@Deprecated
	public void setSampleJobCellSelectionId (Integer sampleJobCellSelectionId) {
		setId(sampleJobCellSelectionId);
	}

	/**
	 * getSampleJobCellSelectionId()
	 *
	 * @return sampleJobCellSelectionId
	 *
	 */
	@Deprecated
	public Integer getSampleJobCellSelectionId () {
		return getId();
	}




	/** 
	 * sampleId
	 *
	 */
	@Column(name="sampleid")
	protected Integer sampleId;

	/**
	 * setSampleId(Integer sampleId)
	 *
	 * @param sampleId
	 *
	 */
	
	public void setSampleId (Integer sampleId) {
		this.sampleId = sampleId;
	}

	/**
	 * getSampleId()
	 *
	 * @return sampleId
	 *
	 */
	public Integer getSampleId () {
		return this.sampleId;
	}




	/** 
	 * jobCellSelectionId
	 *
	 */
	@Column(name="jobcellselectionid")
	protected Integer jobCellSelectionId;

	/**
	 * setJobCellSelectionId(Integer jobCellSelectionId)
	 *
	 * @param jobcellId
	 *
	 */
	
	public void setJobCellSelectionId (Integer jobCellSelectionId) {
		this.jobCellSelectionId = jobCellSelectionId;
	}

	/**
	 * getJobCellSelectionId()
	 *
	 * @return jobCellSelectionId
	 *
	 */
	public Integer getJobCellSelectionId () {
		return this.jobCellSelectionId;
	}




	/** 
	 * libraryIndex
	 *
	 */
	@Column(name="libraryindex")
	protected Integer libraryIndex;

	/**
	 * setLibraryindex(Integer libraryIndex)
	 *
	 * @param libraryIndex
	 *
	 */
	
	public void setLibraryIndex (Integer libraryIndex) {
		this.libraryIndex = libraryIndex;
	}

	/**
	 * getLibraryIndex()
	 *
	 * @return libraryIndex
	 *
	 */
	public Integer getLibraryIndex () {
		return this.libraryIndex;
	}




	/**
	 * sample
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="sampleid", insertable=false, updatable=false)
	protected Sample sample;

	/**
	 * setSample (Sample sample)
	 *
	 * @param sample
	 *
	 */
	public void setSample (Sample sample) {
		this.sample = sample;
		this.sampleId = sample.getId();
	}

	/**
	 * getSample ()
	 *
	 * @return sample
	 *
	 */
	
	public Sample getSample () {
		return this.sample;
	}


	/**
	 * jobCellSelection
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="jobcellselectionid", insertable=false, updatable=false)
	protected JobCellSelection jobCellSelection;

	/**
	 * setJobCell (JobCellSelection jobCellSelection)
	 *
	 * @param jobCellSelection
	 *
	 */
	public void setJobCellSelection (JobCellSelection jobCellSelection) {
		this.jobCellSelection = jobCellSelection;
		this.jobCellSelectionId = jobCellSelection.getId();
	}

	/**
	 * getJobCell ()
	 *
	 * @return jobCellSelection
	 *
	 */
	
	public JobCellSelection getJobCellSelection () {
		return this.jobCellSelection;
	}


}
