
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
@Table(name="samplesource")
public class SampleSource extends WaspModel {

	/** 
	 * sampleSourceId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer sampleSourceId;

	/**
	 * setSampleSourceId(Integer sampleSourceId)
	 *
	 * @param sampleSourceId
	 *
	 */
	
	public void setSampleSourceId (Integer sampleSourceId) {
		this.sampleSourceId = sampleSourceId;
	}

	/**
	 * getSampleSourceId()
	 *
	 * @return sampleSourceId
	 *
	 */
	public Integer getSampleSourceId () {
		return this.sampleSourceId;
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
	 * multiplexindex
	 *
	 */
	@Column(name="multiplexindex")
	protected Integer multiplexindex;

	/**
	 * setMultiplexindex(Integer multiplexindex)
	 *
	 * @param multiplexindex
	 *
	 */
	
	public void setMultiplexindex (Integer multiplexindex) {
		this.multiplexindex = multiplexindex;
	}

	/**
	 * getMultiplexindex()
	 *
	 * @return multiplexindex
	 *
	 */
	public Integer getMultiplexindex () {
		return this.multiplexindex;
	}




	/** 
	 * sourceSampleId
	 *
	 */
	@Column(name="source_sampleid")
	protected Integer sourceSampleId;

	/**
	 * setSourceSampleId(Integer sourceSampleId)
	 *
	 * @param sourceSampleId
	 *
	 */
	
	public void setSourceSampleId (Integer sourceSampleId) {
		this.sourceSampleId = sourceSampleId;
	}

	/**
	 * getSourceSampleId()
	 *
	 * @return sourceSampleId
	 *
	 */
	public Integer getSourceSampleId () {
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
	protected Integer lastUpdUser;

	/**
	 * setLastUpdUser(Integer lastUpdUser)
	 *
	 * @param lastUpdUser
	 *
	 */
	
	public void setLastUpdUser (Integer lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	/**
	 * getLastUpdUser()
	 *
	 * @return lastUpdUser
	 *
	 */
	public Integer getLastUpdUser () {
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
	protected Sample sampleViaSource;

	/**
	 * setSampleViaSource (Sample sample)
	 *
	 * @param sample
	 *
	 */
	public void setSampleViaSource (Sample sample) {
		this.sample = sample;
		this.sourceSampleId = sample.sampleId;
	}

	/**
	 * getSampleViaSource ()
	 *
	 * @return sample
	 *
	 */
	
	public Sample getSampleViaSource () {
		return this.sampleViaSource;
	}


	/** 
	 * sampleSourceMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="samplesourceid", insertable=false, updatable=false)
	protected List<SampleSourceMeta> sampleSourceMeta;


	/** 
	 * getSampleSourceMeta()
	 *
	 * @return sampleSourceMeta
	 *
	 */
	@JsonIgnore
	public List<SampleSourceMeta> getSampleSourceMeta() {
		return this.sampleSourceMeta;
	}


	/** 
	 * setSampleSourceMeta
	 *
	 * @param sampleSourceMeta
	 *
	 */
	public void setSampleSourceMeta (List<SampleSourceMeta> sampleSourceMeta) {
		this.sampleSourceMeta = sampleSourceMeta;
	}
}
