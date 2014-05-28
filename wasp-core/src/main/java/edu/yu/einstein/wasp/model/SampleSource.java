
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

/**
 * 
 * Library-cell relationship.  Used for mapping sample pair relationships for analysis.
 * 
 * @author calder
 *
 */
@Entity
@Audited
@Table(name="samplesource")
public class SampleSource extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5159692371397079658L;
	

	/**
	 * setSampleSourceId(Integer sampleSourceId)
	 *
	 * @param sampleSourceId
	 *
	 */
	@Deprecated
	public void setSampleSourceId (Integer sampleSourceId) {
		setId(sampleSourceId);
	}

	/**
	 * getSampleSourceId()
	 *
	 * @return sampleSourceId
	 *
	 */
	@Deprecated
	public Integer getSampleSourceId () {
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
	 * index
	 *
	 */
	@Column(name="indexvalue")
	protected Integer index = 0;

	/**
	 * setIndex(Integer index)
	 *
	 * @param index
	 *
	 */
	
	public void setIndex (Integer index) {
		this.index = index;
	}

	/**
	 * getIndex()
	 *
	 * @return index
	 *
	 */
	public Integer getIndex () {
		return this.index;
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
	 * sample
	 *
	 */
	@NotAudited
	@ManyToOne(fetch=FetchType.EAGER)
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
	 * sampleVia
	 *
	 */
	@NotAudited
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="source_sampleid", insertable=false, updatable=false)
	protected Sample sourceSample;

	/**
	 * setSourceSample(Sample sample)
	 *
	 * @param sample
	 *
	 */
	public void setSourceSample (Sample sourceSample) {
		this.sourceSample = sourceSample;
		this.sourceSampleId = sourceSample.getId();
	}

	/**
	 * getSourceSample()
	 *
	 * @return sample
	 *
	 */
	
	public Sample getSourceSample() {
		return this.sourceSample;
	}


	/** 
	 * sampleSourceMeta
	 *
	 */
	@NotAudited
	@OneToMany(fetch=FetchType.EAGER)
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
	
	/**
	 * 
	 */
	@ManyToMany(mappedBy="sampleSources")
	private Set<FileGroup> fileGroups = new HashSet<FileGroup>();

	/**
	 * @return the fileGroups
	 */
	public Set<FileGroup> getFileGroups() {
		return fileGroups;
	}

	/**
	 * @param fileGroups the fileGroups to set
	 */
	public void setFileGroups(Set<FileGroup> fileGroups) {
		this.fileGroups = fileGroups;
	}
	
}
