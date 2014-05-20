
/**
 *
 * SampleLab.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleLab
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
@Table(name="samplelab")
public class SampleLab extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 628107382787556246L;
	
	/**
	 * setSampleLabId(Integer sampleLabId)
	 *
	 * @param sampleLabId
	 *
	 */
	@Deprecated
	public void setSampleLabId (Integer sampleLabId) {
		setId(sampleLabId);
	}

	/**
	 * getSampleLabId()
	 *
	 * @return sampleLabId
	 *
	 */
	@Deprecated
	public Integer getSampleLabId () {
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
	 * labId
	 *
	 */
	@Column(name="labid")
	protected Integer labId;

	/**
	 * setLabId(Integer labId)
	 *
	 * @param labId
	 *
	 */
	
	public void setLabId (Integer labId) {
		this.labId = labId;
	}

	/**
	 * getLabId()
	 *
	 * @return labId
	 *
	 */
	public Integer getLabId () {
		return this.labId;
	}




	/** 
	 * isPrimary
	 *
	 */
	@Column(name="isprimary")
	protected Integer isPrimary = 0;

	/**
	 * setIsPrimary(Integer isPrimary)
	 *
	 * @param isPrimary
	 *
	 */
	
	public void setIsPrimary (Integer isPrimary) {
		this.isPrimary = isPrimary;
	}

	/**
	 * getIsPrimary()
	 *
	 * @return isPrimary
	 *
	 */
	public Integer getIsPrimary () {
		return this.isPrimary;
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
	 * lab
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="labid", insertable=false, updatable=false)
	protected Lab lab;

	/**
	 * setLab (Lab lab)
	 *
	 * @param lab
	 *
	 */
	public void setLab (Lab lab) {
		this.lab = lab;
		this.labId = lab.getId();
	}

	/**
	 * getLab ()
	 *
	 * @return lab
	 *
	 */
	
	public Lab getLab () {
		return this.lab;
	}


}
