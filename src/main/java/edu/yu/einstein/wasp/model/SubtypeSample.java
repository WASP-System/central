
/**
 *
 * SubtypeSample.java 
 * @author echeng (table2type.pl)
 *  
 * the SubtypeSample
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

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="subtypesample")
public class SubtypeSample extends WaspModel {

	/** 
	 * subtypeSampleId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer subtypeSampleId;

	/**
	 * setSubtypeSampleId(Integer subtypeSampleId)
	 *
	 * @param subtypeSampleId
	 *
	 */
	
	public void setSubtypeSampleId (Integer subtypeSampleId) {
		this.subtypeSampleId = subtypeSampleId;
	}

	/**
	 * getSubtypeSampleId()
	 *
	 * @return subtypeSampleId
	 *
	 */
	public Integer getSubtypeSampleId () {
		return this.subtypeSampleId;
	}




	/** 
	 * typeSampleId
	 *
	 */
	@Column(name="typesampleid")
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
	 * typeSample
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="typesampleid", insertable=false, updatable=false)
	protected TypeSample typeSample;

	/**
	 * setTypeSample (TypeSample typeSample)
	 *
	 * @param typeSample
	 *
	 */
	public void setTypeSample (TypeSample typeSample) {
		this.typeSample = typeSample;
		this.typeSampleId = typeSample.typeSampleId;
	}

	/**
	 * getTypeSample ()
	 *
	 * @return typeSample
	 *
	 */
	
	public TypeSample getTypeSample () {
		return this.typeSample;
	}


	/** 
	 * workflowsubtypesample
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="subtypesampleid", insertable=false, updatable=false)
	protected List<Workflowsubtypesample> workflowsubtypesample;


	/** 
	 * getWorkflowsubtypesample()
	 *
	 * @return workflowsubtypesample
	 *
	 */
	public List<Workflowsubtypesample> getWorkflowsubtypesample() {
		return this.workflowsubtypesample;
	}


	/** 
	 * setWorkflowsubtypesample
	 *
	 * @param workflowsubtypesample
	 *
	 */
	public void setWorkflowsubtypesample (List<Workflowsubtypesample> workflowsubtypesample) {
		this.workflowsubtypesample = workflowsubtypesample;
	}

	/** 
	 * subtypeSampleResourceCategory
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="subtypesampleid", insertable=false, updatable=false)
	protected List<SubtypeSampleResourceCategory> subtypeSampleResourceCategory;


	/** 
	 * getSubtypeSampleResourceCategory()
	 *
	 * @return subtypeSampleResourceCategory
	 *
	 */
	public List<SubtypeSampleResourceCategory> getSubtypeSampleResourceCategory() {
		return this.subtypeSampleResourceCategory;
	}


	/** 
	 * setSubtypeSampleResourceCategory
	 *
	 * @param subtypeSampleResourceCategory
	 *
	 */
	public void setSubtypeSampleResourceCategory (List<SubtypeSampleResourceCategory> subtypeSampleResourceCategory) {
		this.subtypeSampleResourceCategory = subtypeSampleResourceCategory;
	}

	/** 
	 * sample
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="subtypesampleid", insertable=false, updatable=false)
	protected List<Sample> sample;


	/** 
	 * getSample()
	 *
	 * @return sample
	 *
	 */
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
	@JoinColumn(name="subtypesampleid", insertable=false, updatable=false)
	protected List<SampleDraft> sampleDraft;


	/** 
	 * getSampleDraft()
	 *
	 * @return sampleDraft
	 *
	 */
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
	 * subtypeSampleMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="subtypesampleid", insertable=false, updatable=false)
	protected List<SubtypeSampleMeta> subtypeSampleMeta;


	/** 
	 * getSubtypeSampleMeta()
	 *
	 * @return subtypeSampleMeta
	 *
	 */
	public List<SubtypeSampleMeta> getSubtypeSampleMeta() {
		return this.subtypeSampleMeta;
	}


	/** 
	 * setSubtypeSampleMeta
	 *
	 * @param subtypeSampleMeta
	 *
	 */
	public void setSubtypeSampleMeta (List<SubtypeSampleMeta> subtypeSampleMeta) {
		this.subtypeSampleMeta = subtypeSampleMeta;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + subtypeSampleId;
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
		SubtypeSample other = (SubtypeSample) obj;
		if (subtypeSampleId.intValue() != other.subtypeSampleId.intValue())
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SubtypeSample [subtypeSampleId=" + subtypeSampleId
				+ ", typeSampleId=" + typeSampleId + ", iName=" + iName
				+ ", name=" + name + "]";
	}



}
