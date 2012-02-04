
/**
 *
 * AdaptorsetMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the AdaptorsetMeta
 *
 *
 */

package edu.yu.einstein.wasp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="adaptorsetmeta")
public class AdaptorsetMeta extends MetaBase {

	/** 
	 * adaptorsetMetaId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer adaptorsetMetaId;

	/**
	 * setAdaptorsetMetaId(Integer adaptorsetMetaId)
	 *
	 * @param adaptorsetMetaId
	 *
	 */
	
	public void setAdaptorsetMetaId (Integer adaptorsetMetaId) {
		this.adaptorsetMetaId = adaptorsetMetaId;
	}

	/**
	 * getAdaptorsetMetaId()
	 *
	 * @return adaptorsetMetaId
	 *
	 */
	public Integer getAdaptorsetMetaId () {
		return this.adaptorsetMetaId;
	}




	/** 
	 * adaptorsetId
	 *
	 */
	@Column(name="adaptorsetid")
	protected Integer adaptorsetId;

	/**
	 * setAdaptorsetId(Integer adaptorsetId)
	 *
	 * @param adaptorsetId
	 *
	 */
	
	public void setAdaptorsetId (Integer adaptorsetId) {
		this.adaptorsetId = adaptorsetId;
	}

	/**
	 * getAdaptorsetId()
	 *
	 * @return adaptorsetId
	 *
	 */
	public Integer getAdaptorsetId () {
		return this.adaptorsetId;
	}




	/**
	 * adaptorset
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="adaptorsetid", insertable=false, updatable=false)
	protected Adaptorset adaptorset;

	/**
	 * setAdaptorset (Adaptorset adaptorset)
	 *
	 * @param adaptorset
	 *
	 */
	public void setAdaptorset (Adaptorset adaptorset) {
		this.adaptorset = adaptorset;
		this.adaptorsetId = adaptorset.adaptorsetId;
	}

	/**
	 * getAdaptorset ()
	 *
	 * @return adaptorset
	 *
	 */
	
	public Adaptorset getAdaptorset () {
		return this.adaptorset;
	}


}
