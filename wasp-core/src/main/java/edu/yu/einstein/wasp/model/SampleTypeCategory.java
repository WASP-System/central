
/**
 *
 * SampleTypeCategory.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleTypeCategory
 *
 *
 */

package edu.yu.einstein.wasp.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="sampletypecategory")
public class SampleTypeCategory extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8426810599236807394L;

	/**
	 * setSampleTypeCategoryId(Integer sampleTypecategoryId)
	 *
	 * @param sampleTypecategoryId
	 *
	 */
	@Deprecated
	public void setSampleTypeCategoryId (Integer sampleTypecategoryId) {
		setId(sampleTypecategoryId);
	}

	/**
	 * getSampleTypeCategoryId()
	 *
	 * @return sampleTypecategoryId
	 *
	 */
	@Deprecated
	public Integer getSampleTypeCategoryId () {
		return getId();
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
	 * sampleType
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="sampletypecategoryid", insertable=false, updatable=false)
	protected List<SampleType> sampleType;


	/** 
	 * getSampleType()
	 *
	 * @return sampleType
	 *
	 */
	@JsonIgnore
	public List<SampleType> getSampleType() {
		return this.sampleType;
	}


	/** 
	 * setSampleType
	 *
	 * @param sampleType
	 *
	 */
	public void setSampleType (List<SampleType> sampleType) {
		this.sampleType = sampleType;
	}



}
