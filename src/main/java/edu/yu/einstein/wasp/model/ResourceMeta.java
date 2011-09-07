
/**
 *
 * ResourceMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceMeta
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
@Table(name="resourcemeta")
public class ResourceMeta extends MetaBase {

	/** 
	 * resourceMetaId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int resourceMetaId;

	/**
	 * setResourceMetaId(int resourceMetaId)
	 *
	 * @param resourceMetaId
	 *
	 */
	
	public void setResourceMetaId (int resourceMetaId) {
		this.resourceMetaId = resourceMetaId;
	}

	/**
	 * getResourceMetaId()
	 *
	 * @return resourceMetaId
	 *
	 */
	public int getResourceMetaId () {
		return this.resourceMetaId;
	}




	/** 
	 * resourceId
	 *
	 */
	@Column(name="resourceid")
	protected int resourceId;

	/**
	 * setResourceId(int resourceId)
	 *
	 * @param resourceId
	 *
	 */
	
	public void setResourceId (int resourceId) {
		this.resourceId = resourceId;
	}

	/**
	 * getResourceId()
	 *
	 * @return resourceId
	 *
	 */
	public int getResourceId () {
		return this.resourceId;
	}




	/**
	 * resource
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="resourceid", insertable=false, updatable=false)
	protected Resource resource;

	/**
	 * setResource (Resource resource)
	 *
	 * @param resource
	 *
	 */
	public void setResource (Resource resource) {
		this.resource = resource;
		this.resourceId = resource.resourceId;
	}

	/**
	 * getResource ()
	 *
	 * @return resource
	 *
	 */
	
	public Resource getResource () {
		return this.resource;
	}


}
