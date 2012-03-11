
/**
 *
 * TypeSample.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeSample
 *
 *
 */

package edu.yu.einstein.wasp.model;

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
@Table(name="typesample")
public class TypeSample extends WaspModel {

	/** 
	 * typeSampleId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer typeSampleId;

	/**
	 * setTypeSampleId(Integer typeSampleId)
	 *
	 * @param typeSampleId
	 *
	 */
	
	public void setTypeSampleId (Integer typeSampleId) {
		this.typeSampleId = typeSampleId;
	}

	/**
	 * getTypeSampleId()
	 *
	 * @return typeSampleId
	 *
	 */
	public Integer getTypeSampleId () {
		return this.typeSampleId;
	}




	/** 
	 * typeSamplecategoryId
	 *
	 */
	@Column(name="typesamplecategoryid")
	protected Integer typeSampleCategoryId;

	/**
	 * setTypeSamplecategoryId(Integer typeSamplecategoryId)
	 *
	 * @param typeSamplecategoryId
	 *
	 */
	
	public void setTypeSampleCategoryId (Integer typeSampleCategoryId) {
		this.typeSampleCategoryId = typeSampleCategoryId;
	}

	/**
	 * getTypeSamplecategoryId()
	 *
	 * @return typeSamplecategoryId
	 *
	 */
	public Integer getTypeSampleCategoryId () {
		return this.typeSampleCategoryId;
	}




	/** 
	 * iName
	 *
	 */
	@Column(name="iname")
	protected String iName;

	/**
	 * setIName(String iName)
	 *
	 * @param iName
	 *
	 */
	
	public void setIName (String iName) {
		this.iName = iName;
	}

	/**
	 * getIName()
	 *
	 * @return iName
	 *
	 */
	public String getIName () {
		return this.iName;
	}




	/** 
	 * name
	 *
	 */
	@Column(name="name")
	protected String name;

	/**
	 * setName(String name)
	 *
	 * @param name
	 *
	 */
	
	public void setName (String name) {
		this.name = name;
	}

	/**
	 * getName()
	 *
	 * @return name
	 *
	 */
	public String getName () {
		return this.name;
	}




	/**
	 * typeSamplecategory
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="typesamplecategoryid", insertable=false, updatable=false)
	protected TypeSampleCategory typeSampleCategory;

	/**
	 * setTypeSamplecategory (TypeSamplecategory typeSampleCategory)
	 *
	 * @param typeSampleCategory
	 *
	 */
	public void setTypeSampleCategory (TypeSampleCategory typeSampleCategory) {
		this.typeSampleCategory = typeSampleCategory;
		this.typeSampleCategoryId = typeSampleCategory.typeSamplecategoryId;
	}

	/**
	 * getTypeSampleCategory ()
	 *
	 * @return typeSampleCategory
	 *
	 */
	
	public TypeSampleCategory getTypeSampleCategory () {
		return this.typeSampleCategory;
	}




	/** 
	 * subtypeSample
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="typesampleid", insertable=false, updatable=false)
	protected List<SubtypeSample> subtypeSample;


	/** 
	 * getSubtypeSample()
	 *
	 * @return subtypeSample
	 *
	 */
	@JsonIgnore
	public List<SubtypeSample> getSubtypeSample() {
		return this.subtypeSample;
	}


	/** 
	 * setSubtypeSample
	 *
	 * @param subtypeSample
	 *
	 */
	public void setSubtypeSample (List<SubtypeSample> subtypeSample) {
		this.subtypeSample = subtypeSample;
	}



	/** 
	 * sample
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="typesampleid", insertable=false, updatable=false)
	protected List<Sample> sample;


	/** 
	 * getSample()
	 *
	 * @return sample
	 *
	 */
	@JsonIgnore
	public List<Sample> getSample() {
		return this.sample;
	}


	/** 
	 * setSample
	 *
	 * @param sample
	 *
	 */
	public void setSample (List<Sample> sample) {
		this.sample = sample;
	}



	/** 
	 * sampleDraft
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="typesampleid", insertable=false, updatable=false)
	protected List<SampleDraft> sampleDraft;


	/** 
	 * getSampleDraft()
	 *
	 * @return sampleDraft
	 *
	 */
	@JsonIgnore
	public List<SampleDraft> getSampleDraft() {
		return this.sampleDraft;
	}


	/** 
	 * setSampleDraft
	 *
	 * @param sampleDraft
	 *
	 */
	public void setSampleDraft (List<SampleDraft> sampleDraft) {
		this.sampleDraft = sampleDraft;
	}



	/** 
	 * adaptorset
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="typesampleid", insertable=false, updatable=false)
	protected List<Adaptorset> adaptorset;


	/** 
	 * getAdaptorset()
	 *
	 * @return adaptorset
	 *
	 */
	@JsonIgnore
	public List<Adaptorset> getAdaptorset() {
		return this.adaptorset;
	}


	/** 
	 * setAdaptorset
	 *
	 * @param adaptorset
	 *
	 */
	public void setAdaptorset (List<Adaptorset> adaptorset) {
		this.adaptorset = adaptorset;
	}



}
