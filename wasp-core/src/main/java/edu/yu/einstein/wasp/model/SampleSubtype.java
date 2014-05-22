
/**
 *
 * SampleSubtype.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleSubtype
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.ArrayList;
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
@Table(name="samplesubtype")
public class SampleSubtype extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4381006193862184833L;
	

	/**
	 * setSampleSubtypeId(Integer sampleSubtypeId)
	 *
	 * @param sampleSubtypeId
	 *
	 */
	@Deprecated
	public void setSampleSubtypeId (Integer sampleSubtypeId) {
		setId(sampleSubtypeId);
	}

	/**
	 * getSampleSubtypeId()
	 *
	 * @return sampleSubtypeId
	 *
	 */
	@Deprecated
	public Integer getSampleSubtypeId () {
		return getId();
	}




	/** 
	 * sampleTypeId
	 *
	 */
	@Column(name="sampletypeid")
	protected Integer sampleTypeId;

	/**
	 * setSampleTypeId(Integer sampleTypeId)
	 *
	 * @param sampleTypeId
	 *
	 */
	
	public void setSampleTypeId (Integer sampleTypeId) {
		this.sampleTypeId = sampleTypeId;
	}

	/**
	 * getSampleTypeId()
	 *
	 * @return sampleTypeId
	 *
	 */
	public Integer getSampleTypeId () {
		return this.sampleTypeId;
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
	 * arealist
	 *
	 */
	@Column(name="arealist")
	protected String arealist;

	/**
	 * setAreaList(String name)
	 *
	 * @param arealist
	 *
	 */
	
	public void setAreaList (String arealist) {
		this.arealist = arealist;
	}

	/**
	 * getAreaList()
	 *
	 * @return arealist
	 *
	 */
	public String getAreaList () {
		return this.arealist;
	}

	/**
	 * getComponentMetaAreas()
	 * @return list of component meta areas
	 */
	public List<String> getComponentMetaAreas(){
		if (this.arealist == null || this.arealist.equals(""))
			return null;
		List<String>  splitList = new ArrayList<String>();
		for (String s: this.arealist.split(",")){
			splitList.add(s.trim());
		}
		return splitList;
	}


	/**
	 * sampleType
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="sampletypeid", insertable=false, updatable=false)
	protected SampleType sampleType;

	/**
	 * setSampleType (SampleType sampleType)
	 *
	 * @param sampleType
	 *
	 */
	public void setSampleType (SampleType sampleType) {
		this.sampleType = sampleType;
		this.sampleTypeId = sampleType.getId();
	}

	/**
	 * getSampleType ()
	 *
	 * @return sampleType
	 *
	 */
	
	public SampleType getSampleType () {
		return this.sampleType;
	}


	/** 
	 * workflowSampleSubtype
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="samplesubtypeid", insertable=false, updatable=false)
	protected List<WorkflowSampleSubtype> workflowSampleSubtype;


	/** 
	 * getWorkflowsamplesubtype()
	 *
	 * @return workflowSampleSubtype
	 *
	 */
	@JsonIgnore
	public List<WorkflowSampleSubtype> getWorkflowSampleSubtype() {
		return this.workflowSampleSubtype;
	}


	/** 
	 * setWorkflowsamplesubtype
	 *
	 * @param workflowSampleSubtype
	 *
	 */
	public void setWorkflowSampleSubtype (List<WorkflowSampleSubtype> workflowSampleSubtype) {
		this.workflowSampleSubtype = workflowSampleSubtype;
	}

	/** 
	 * sampleSubtypeResourceCategory
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="samplesubtypeid", insertable=false, updatable=false)
	protected List<SampleSubtypeResourceCategory> sampleSubtypeResourceCategory;


	/** 
	 * getSampleSubtypeResourceCategory()
	 *
	 * @return sampleSubtypeResourceCategory
	 *
	 */
	@JsonIgnore
	public List<SampleSubtypeResourceCategory> getSampleSubtypeResourceCategory() {
		return this.sampleSubtypeResourceCategory;
	}


	/** 
	 * setSampleSubtypeResourceCategory
	 *
	 * @param sampleSubtypeResourceCategory
	 *
	 */
	public void setSampleSubtypeResourceCategory (List<SampleSubtypeResourceCategory> sampleSubtypeResourceCategory) {
		this.sampleSubtypeResourceCategory = sampleSubtypeResourceCategory;
	}

	/** 
	 * sample
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="samplesubtypeid", insertable=false, updatable=false)
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
	@JoinColumn(name="samplesubtypeid", insertable=false, updatable=false)
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
	 * sampleSubtypeMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="samplesubtypeid", insertable=false, updatable=false)
	protected List<SampleSubtypeMeta> sampleSubtypeMeta;


	/** 
	 * getSampleSubtypeMeta()
	 *
	 * @return sampleSubtypeMeta
	 *
	 */
	@JsonIgnore
	public List<SampleSubtypeMeta> getSampleSubtypeMeta() {
		return this.sampleSubtypeMeta;
	}


	/** 
	 * setSampleSubtypeMeta
	 *
	 * @param sampleSubtypeMeta
	 *
	 */
	public void setSampleSubtypeMeta (List<SampleSubtypeMeta> sampleSubtypeMeta) {
		this.sampleSubtypeMeta = sampleSubtypeMeta;
	}

	// TODO: rethink
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getId();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SampleSubtype other = (SampleSubtype) obj;
		if (getId().intValue() != other.getId().intValue())
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SampleSubtype [sampleSubtypeId=" + getId()
				+ ", sampleTypeId=" + sampleTypeId + ", iName=" + iName
				+ ", name=" + name + ", arealist=" + arealist + "]";
	}



}
