
/**
 *
 * Resource.java 
 * @author echeng (table2type.pl)
 *  
 * the Resource
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
@Table(name="resource")
public class Resource extends WaspModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8945631027973348584L;

	/**
	 * setResourceId(Integer resourceId)
	 *
	 * @param resourceId
	 *
	 */
	@Deprecated
	public void setResourceId (Integer resourceId) {
		setId(resourceId);
	}

	/**
	 * getResourceId()
	 *
	 * @return resourceId
	 *
	 */
	@Deprecated
	public Integer getResourceId () {
		return getId();
	}




	/** 
	 * resourcecategoryId
	 *
	 */
	@Column(name="resourcecategoryid")
	protected Integer resourcecategoryId;

	/**
	 * setResourcecategoryId(Integer resourcecategoryId)
	 *
	 * @param resourcecategoryId
	 *
	 */
	
	public void setResourcecategoryId (Integer resourcecategoryId) {
		this.resourcecategoryId = resourcecategoryId;
	}
	
	/**
	 * getResourcecategoryId()
	 *
	 * @return resourcecategoryId
	 *
	 */
	public Integer getResourcecategoryId () {
		return this.resourcecategoryId;
	}

	/** 
	 * resourceTypeId
	 *
	 */
	@Column(name="resourcetypeid")
	protected Integer resourceTypeId;

	/**
	 * setResourceTypeId(Integer resourceTypeId)
	 *
	 * @param resourceTypeId
	 *
	 */
	
	public void setResourceTypeId (Integer resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	/**
	 * getResourceTypeId()
	 *
	 * @return resourceTypeId
	 *
	 */
	public Integer getResourceTypeId () {
		return this.resourceTypeId;
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
	 * resourceType
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="resourcetypeid", insertable=false, updatable=false)
	protected ResourceType resourceType;

	/**
	 * setResourceType (ResourceType resourceType)
	 *
	 * @param resourceType
	 *
	 */
	public void setResourceType (ResourceType resourceType) {
		this.resourceType = resourceType;
		this.resourceTypeId = resourceType.getId();
	}

	/**
	 * getResourceType ()
	 *
	 * @return resourceType
	 *
	 */
	
	public ResourceType getResourceType () {
		return this.resourceType;
	}


	/**
	 * resourceCategory
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="resourcecategoryid", insertable=false, updatable=false)
	protected ResourceCategory resourceCategory;

	/**
	 * setResourceCategory (ResourceCategory resourceCategory)
	 *
	 * @param resourceCategory
	 *
	 */
	public void setResourceCategory (ResourceCategory resourceCategory) {
		this.resourceCategory = resourceCategory;
		this.resourcecategoryId = resourceCategory.getId();
	}

	/**
	 * getResourceCategory ()
	 *
	 * @return resourceCategory
	 *
	 */
	
	public ResourceCategory getResourceCategory () {
		return this.resourceCategory;
	}


	/** 
	 * resourceMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="resourceid", insertable=false, updatable=false)
	protected List<ResourceMeta> resourceMeta;


	/** 
	 * getResourceMeta()
	 *
	 * @return resourceMeta
	 *
	 */
	@JsonIgnore
	public List<ResourceMeta> getResourceMeta() {
		return this.resourceMeta;
	}


	/** 
	 * setResourceMeta
	 *
	 * @param resourceMeta
	 *
	 */
	public void setResourceMeta (List<ResourceMeta> resourceMeta) {
		this.resourceMeta = resourceMeta;
	}






	/** 
	 * resourceBarcode
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="resourceid", insertable=false, updatable=false)
	protected List<ResourceBarcode> resourceBarcode;


	/** 
	 * getResourceBarcode()
	 *
	 * @return resourceBarcode
	 *
	 */
	@JsonIgnore
	public List<ResourceBarcode> getResourceBarcode() {
		return this.resourceBarcode;
	}


	/** 
	 * setResourceBarcode
	 *
	 * @param resourceBarcode
	 *
	 */
	public void setResourceBarcode (List<ResourceBarcode> resourceBarcode) {
		this.resourceBarcode = resourceBarcode;
	}



	/** 
	 * resourceCell
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="resourceid", insertable=false, updatable=false)
	protected List<ResourceCell> resourceCell;


	/** 
	 * getResourceCell()
	 *
	 * @return resourceCell
	 *
	 */
	@JsonIgnore
	public List<ResourceCell> getResourceCell() {
		return this.resourceCell;
	}


	/** 
	 * setResourceCell
	 *
	 * @param resourceCell
	 *
	 */
	public void setResourceCell (List<ResourceCell> resourceCell) {
		this.resourceCell = resourceCell;
	}



	/** 
	 * run
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="resourceid", insertable=false, updatable=false)
	protected List<Run> run;


	/** 
	 * getRun()
	 *
	 * @return run
	 *
	 */
	@JsonIgnore
	public List<Run> getRun() {
		return this.run;
	}


	/** 
	 * setRun
	 *
	 * @param run
	 *
	 */
	public void setRun (List<Run> run) {
		this.run = run;
	}



}
