
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

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import org.hibernate.validator.constraints.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="samplelab")
public class SampleLab extends WaspModel {

	/** 
	 * sampleLabId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int sampleLabId;

	/**
	 * setSampleLabId(int sampleLabId)
	 *
	 * @param sampleLabId
	 *
	 */
	
	public void setSampleLabId (int sampleLabId) {
		this.sampleLabId = sampleLabId;
	}

	/**
	 * getSampleLabId()
	 *
	 * @return sampleLabId
	 *
	 */
	public int getSampleLabId () {
		return this.sampleLabId;
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
	 * labId
	 *
	 */
	@Column(name="labid")
	protected int labId;

	/**
	 * setLabId(int labId)
	 *
	 * @param labId
	 *
	 */
	
	public void setLabId (int labId) {
		this.labId = labId;
	}

	/**
	 * getLabId()
	 *
	 * @return labId
	 *
	 */
	public int getLabId () {
		return this.labId;
	}




	/** 
	 * isPrimary
	 *
	 */
	@Column(name="isprimary")
	protected int isPrimary;

	/**
	 * setIsPrimary(int isPrimary)
	 *
	 * @param isPrimary
	 *
	 */
	
	public void setIsPrimary (int isPrimary) {
		this.isPrimary = isPrimary;
	}

	/**
	 * getIsPrimary()
	 *
	 * @return isPrimary
	 *
	 */
	public int getIsPrimary () {
		return this.isPrimary;
	}




	/** 
	 * lastUpdTs
	 *
	 */
	@Column(name="lastupdts")
	protected Date lastUpdTs;

	/**
	 * setLastUpdTs(Date lastUpdTs)
	 *
	 * @param lastUpdTs
	 *
	 */
	
	public void setLastUpdTs (Date lastUpdTs) {
		this.lastUpdTs = lastUpdTs;
	}

	/**
	 * getLastUpdTs()
	 *
	 * @return lastUpdTs
	 *
	 */
	public Date getLastUpdTs () {
		return this.lastUpdTs;
	}




	/** 
	 * lastUpdUser
	 *
	 */
	@Column(name="lastupduser")
	protected int lastUpdUser;

	/**
	 * setLastUpdUser(int lastUpdUser)
	 *
	 * @param lastUpdUser
	 *
	 */
	
	public void setLastUpdUser (int lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	/**
	 * getLastUpdUser()
	 *
	 * @return lastUpdUser
	 *
	 */
	public int getLastUpdUser () {
		return this.lastUpdUser;
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
		this.labId = lab.labId;
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
