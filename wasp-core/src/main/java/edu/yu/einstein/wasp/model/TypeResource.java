
/**
 *
 * TypeResource.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeResource
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

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="typeresource")
public class TypeResource extends WaspModel {

	/** 
	 * typeResourceId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer typeResourceId;

	/**
	 * setTypeResourceId(Integer typeResourceId)
	 *
	 * @param typeResourceId
	 *
	 */
	
	public void setTypeResourceId (Integer typeResourceId) {
		this.typeResourceId = typeResourceId;
	}

	/**
	 * getTypeResourceId()
	 *
	 * @return typeResourceId
	 *
	 */
	public Integer getTypeResourceId () {
		return this.typeResourceId;
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
	 * resourceCategory
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="typeresourceid", insertable=false, updatable=false)
	protected List<ResourceCategory> resourceCategory;


	/** 
	 * getResourceCategory()
	 *
	 * @return resourceCategory
	 *
	 */
	@JsonIgnore
	public List<ResourceCategory> getResourceCategory() {
		return this.resourceCategory;
	}


	/** 
	 * setResourceCategory
	 *
	 * @param resourceCategory
	 *
	 */
	public void setResourceCategory (List<ResourceCategory> resourceCategory) {
		this.resourceCategory = resourceCategory;
	}



	/** 
	 * resource
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="typeresourceid", insertable=false, updatable=false)
	protected List<Resource> resource;


	/** 
	 * getResource()
	 *
	 * @return resource
	 *
	 */
	@JsonIgnore
	public List<Resource> getResource() {
		return this.resource;
	}


	/** 
	 * setResource
	 *
	 * @param resource
	 *
	 */
	public void setResource (List<Resource> resource) {
		this.resource = resource;
	}



	/** 
	 * software
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="typeresourceid", insertable=false, updatable=false)
	protected List<Software> software;


	/** 
	 * getSoftware()
	 *
	 * @return software
	 *
	 */
	@JsonIgnore
	public List<Software> getSoftware() {
		return this.software;
	}


	/** 
	 * setSoftware
	 *
	 * @param software
	 *
	 */
	public void setSoftware (List<Software> software) {
		this.software = software;
	}



	/** 
	 * workflowtyperesource
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="typeresourceid", insertable=false, updatable=false)
	protected List<Workflowtyperesource> workflowtyperesource;


	/** 
	 * getWorkflowtyperesource()
	 *
	 * @return workflowtyperesource
	 *
	 */
	@JsonIgnore
	public List<Workflowtyperesource> getWorkflowtyperesource() {
		return this.workflowtyperesource;
	}


	/** 
	 * setWorkflowtyperesource
	 *
	 * @param workflowtyperesource
	 *
	 */
	public void setWorkflowtyperesource (List<Workflowtyperesource> workflowtyperesource) {
		this.workflowtyperesource = workflowtyperesource;
	}



}
