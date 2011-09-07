
/**
 *
 * SampleSource.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleSource
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
@Table(name="samplesource")
public class SampleSource extends WaspModel {

	/** 
	 * sampleSourceId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int sampleSourceId;

	/**
	 * setSampleSourceId(int sampleSourceId)
	 *
	 * @param sampleSourceId
	 *
	 */
	
	public void setSampleSourceId (int sampleSourceId) {
		this.sampleSourceId = sampleSourceId;
	}

	/**
	 * getSampleSourceId()
	 *
	 * @return sampleSourceId
	 *
	 */
	public int getSampleSourceId () {
		return this.sampleSourceId;
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
	 * multiplexindex
	 *
	 */
	@Column(name="multiplexindex")
	protected int multiplexindex;

	/**
	 * setMultiplexindex(int multiplexindex)
	 *
	 * @param multiplexindex
	 *
	 */
	
	public void setMultiplexindex (int multiplexindex) {
		this.multiplexindex = multiplexindex;
	}

	/**
	 * getMultiplexindex()
	 *
	 * @return multiplexindex
	 *
	 */
	public int getMultiplexindex () {
		return this.multiplexindex;
	}




	/** 
	 * sourceSampleId
	 *
	 */
	@Column(name="source_sampleid")
	protected int sourceSampleId;

	/**
	 * setSourceSampleId(int sourceSampleId)
	 *
	 * @param sourceSampleId
	 *
	 */
	
	public void setSourceSampleId (int sourceSampleId) {
		this.sourceSampleId = sourceSampleId;
	}

	/**
	 * getSourceSampleId()
	 *
	 * @return sourceSampleId
	 *
	 */
	public int getSourceSampleId () {
		return this.sourceSampleId;
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
	 * sampleVia
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="source_sampleid", insertable=false, updatable=false)
	protected Sample sampleVia;

	/**
	 * setSampleVia (Sample sample)
	 *
	 * @param sample
	 *
	 */
	public void setSampleVia (Sample sample) {
		this.sample = sample;
		this.sourceSampleId = sample.sampleId;
	}

	/**
	 * getSampleVia ()
	 *
	 * @return sample
	 *
	 */
	
	public Sample getSampleVia () {
		return this.sample;
	}


}
