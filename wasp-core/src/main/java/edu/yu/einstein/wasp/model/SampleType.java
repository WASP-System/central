
/**
 *
 * SampleType.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleType
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
@Table(name="sampletype")
public class SampleType extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2989890943056660096L;

	/**
	 * setSampleTypeId(Integer sampleTypeId)
	 *
	 * @param sampleTypeId
	 *
	 */
	@Deprecated
	public void setSampleTypeId (Integer sampleTypeId) {
		setId(sampleTypeId);
	}

	/**
	 * getSampleTypeId()
	 *
	 * @return sampleTypeId
	 *
	 */
	@Deprecated
	public Integer getSampleTypeId () {
		return getId();
	}




	/** 
	 * sampleTypecategoryId
	 *
	 */
	@Column(name="sampletypecategoryid")
	protected Integer sampleTypeCategoryId;

	/**
	 * setSampleTypecategoryId(Integer sampleTypecategoryId)
	 *
	 * @param sampleTypecategoryId
	 *
	 */
	
	public void setSampleTypeCategoryId (Integer sampleTypeCategoryId) {
		this.sampleTypeCategoryId = sampleTypeCategoryId;
	}

	/**
	 * getSampleTypecategoryId()
	 *
	 * @return sampleTypecategoryId
	 *
	 */
	public Integer getSampleTypeCategoryId () {
		return this.sampleTypeCategoryId;
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
	 * isActive
	 *
	 */
	@Column(name="isactive")
	protected Integer isActive = 1;

	/**
	 * setIsActive(Integer isActive)
	 *
	 * @param isActive
	 *
	 */
	
	public void setIsActive (Integer isActive) {
		this.isActive = isActive;
	}

	/**
	 * getIsActive()
	 *
	 * @return isActive
	 *
	 */
	public Integer getIsActive () {
		return this.isActive;
	}



	/**
	 * sampleTypecategory
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="sampletypecategoryid", insertable=false, updatable=false)
	protected SampleTypeCategory sampleTypeCategory;

	/**
	 * setSampleTypecategory (SampleTypecategory sampleTypeCategory)
	 *
	 * @param sampleTypeCategory
	 *
	 */
	public void setSampleTypeCategory (SampleTypeCategory sampleTypeCategory) {
		this.sampleTypeCategory = sampleTypeCategory;
		this.sampleTypeCategoryId = sampleTypeCategory.getId();
	}

	/**
	 * getSampleTypeCategory ()
	 *
	 * @return sampleTypeCategory
	 *
	 */
	
	public SampleTypeCategory getSampleTypeCategory () {
		return this.sampleTypeCategory;
	}




	/** 
	 * sampleSubtype
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="sampletypeid", insertable=false, updatable=false)
	protected List<SampleSubtype> sampleSubtype;


	/** 
	 * getSampleSubtype()
	 *
	 * @return sampleSubtype
	 *
	 */
	@JsonIgnore
	public List<SampleSubtype> getSampleSubtype() {
		return this.sampleSubtype;
	}


	/** 
	 * setSampleSubtype
	 *
	 * @param sampleSubtype
	 *
	 */
	public void setSampleSubtype (List<SampleSubtype> sampleSubtype) {
		this.sampleSubtype = sampleSubtype;
	}



	/** 
	 * sample
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="sampletypeid", insertable=false, updatable=false)
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
	@JoinColumn(name="sampletypeid", insertable=false, updatable=false)
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
	@JoinColumn(name="sampletypeid", insertable=false, updatable=false)
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
