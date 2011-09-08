
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

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import org.hibernate.validator.constraints.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="subtypesample")
public class SubtypeSample extends WaspModel {

	/** 
	 * subtypeSampleId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int subtypeSampleId;

	/**
	 * setSubtypeSampleId(int subtypeSampleId)
	 *
	 * @param subtypeSampleId
	 *
	 */
	
	public void setSubtypeSampleId (int subtypeSampleId) {
		this.subtypeSampleId = subtypeSampleId;
	}

	/**
	 * getSubtypeSampleId()
	 *
	 * @return subtypeSampleId
	 *
	 */
	public int getSubtypeSampleId () {
		return this.subtypeSampleId;
	}




	/** 
	 * typeSampleId
	 *
	 */
	@Column(name="typesampleid")
	protected int typeSampleId;

	/**
	 * setTypeSampleId(int typeSampleId)
	 *
	 * @param typeSampleId
	 *
	 */
	
	public void setTypeSampleId (int typeSampleId) {
		this.typeSampleId = typeSampleId;
	}

	/**
	 * getTypeSampleId()
	 *
	 * @return typeSampleId
	 *
	 */
	public int getTypeSampleId () {
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



}
