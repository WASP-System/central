
/**
 *
 * TypeSampleCategory.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeSampleCategory
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="typesamplecategory")
public class TypeSampleCategory extends WaspModel {

	/** 
	 * typeSamplecategoryId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer typeSamplecategoryId;

	/**
	 * setTypeSampleCategoryId(Integer typeSamplecategoryId)
	 *
	 * @param typeSamplecategoryId
	 *
	 */
	
	public void setTypeSampleCategoryId (Integer typeSamplecategoryId) {
		this.typeSamplecategoryId = typeSamplecategoryId;
	}

	/**
	 * getTypeSampleCategoryId()
	 *
	 * @return typeSamplecategoryId
	 *
	 */
	public Integer getTypeSampleCategoryId () {
		return this.typeSamplecategoryId;
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
	@OneToMany
	@JoinColumn(name="typesamplecategoryid", insertable=false, updatable=false)
	protected List<TypeSample> typeSample;


	/** 
	 * getTypeSample()
	 *
	 * @return typeSample
	 *
	 */
	
	public List<TypeSample> getTypeSample() {
		return this.typeSample;
	}


	/** 
	 * setTypeSample
	 *
	 * @param typeSample
	 *
	 */
	public void setTypeSample (List<TypeSample> typeSample) {
		this.typeSample = typeSample;
	}



}
