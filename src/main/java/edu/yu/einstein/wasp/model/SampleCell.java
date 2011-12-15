
/**
 *
 * SampleCell.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleCell
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
@Table(name="samplecell")
public class SampleCell extends WaspModel {

	/** 
	 * sampleCellId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer sampleCellId;

	/**
	 * setSampleCellId(Integer sampleCellId)
	 *
	 * @param sampleCellId
	 *
	 */
	
	public void setSampleCellId (Integer sampleCellId) {
		this.sampleCellId = sampleCellId;
	}

	/**
	 * getSampleCellId()
	 *
	 * @return sampleCellId
	 *
	 */
	public Integer getSampleCellId () {
		return this.sampleCellId;
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
	 * jobcellId
	 *
	 */
	@Column(name="jobcellid")
	protected Integer jobcellId;

	/**
	 * setJobcellId(Integer jobcellId)
	 *
	 * @param jobcellId
	 *
	 */
	
	public void setJobcellId (Integer jobcellId) {
		this.jobcellId = jobcellId;
	}

	/**
	 * getJobcellId()
	 *
	 * @return jobcellId
	 *
	 */
	public Integer getJobcellId () {
		return this.jobcellId;
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
		this.sampleId = sample.sampleId;
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
	 * jobCell
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="jobcellid", insertable=false, updatable=false)
	protected JobCell jobCell;

	/**
	 * setJobCell (JobCell jobCell)
	 *
	 * @param jobCell
	 *
	 */
	public void setJobCell (JobCell jobCell) {
		this.jobCell = jobCell;
		this.jobcellId = jobCell.jobCellId;
	}

	/**
	 * getJobCell ()
	 *
	 * @return jobCell
	 *
	 */
	
	public JobCell getJobCell () {
		return this.jobCell;
	}


}
