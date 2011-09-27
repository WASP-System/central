
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
	protected int sampleCellId;

	/**
	 * setSampleCellId(int sampleCellId)
	 *
	 * @param sampleCellId
	 *
	 */
	
	public void setSampleCellId (int sampleCellId) {
		this.sampleCellId = sampleCellId;
	}

	/**
	 * getSampleCellId()
	 *
	 * @return sampleCellId
	 *
	 */
	public int getSampleCellId () {
		return this.sampleCellId;
	}




	/** 
	 * sampleId
	 *
	 */
	@Column(name="sampleid")
	protected int sampleId;

	/**
	 * setSampleId(int sampleId)
	 *
	 * @param sampleId
	 *
	 */
	
	public void setSampleId (int sampleId) {
		this.sampleId = sampleId;
	}

	/**
	 * getSampleId()
	 *
	 * @return sampleId
	 *
	 */
	public int getSampleId () {
		return this.sampleId;
	}




	/** 
	 * jobcellId
	 *
	 */
	@Column(name="jobcellid")
	protected int jobcellId;

	/**
	 * setJobcellId(int jobcellId)
	 *
	 * @param jobcellId
	 *
	 */
	
	public void setJobcellId (int jobcellId) {
		this.jobcellId = jobcellId;
	}

	/**
	 * getJobcellId()
	 *
	 * @return jobcellId
	 *
	 */
	public int getJobcellId () {
		return this.jobcellId;
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
