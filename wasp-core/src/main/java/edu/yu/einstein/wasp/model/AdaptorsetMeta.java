
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
	 * 
	 */
	private static final long serialVersionUID = 3888544492799240044L;

	/**
	 * setAdaptorsetMetaId(Integer adaptorsetMetaId)
	 *
	 * @param adaptorsetMetaId
	 *
	 */
	@Deprecated
	public void setAdaptorsetMetaId (Integer adaptorsetMetaId) {
		setId(adaptorsetMetaId);
	}

	/**
	 * getAdaptorsetMetaId()
	 *
	 * @return adaptorsetMetaId
	 *
	 */
	@Deprecated
	public Integer getAdaptorsetMetaId () {
		return getId();
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
		this.adaptorsetId = adaptorset.getId();
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
