
/**
 *
 * AdaptorMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the AdaptorMeta
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
@Table(name="adaptormeta")
public class AdaptorMeta extends MetaBase {

	/** 
	 * adaptorMetaId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer adaptorMetaId;

	/**
	 * setAdaptorMetaId(Integer adaptorMetaId)
	 *
	 * @param adaptorMetaId
	 *
	 */
	
	public void setAdaptorMetaId (Integer adaptorMetaId) {
		this.adaptorMetaId = adaptorMetaId;
	}

	/**
	 * getAdaptorMetaId()
	 *
	 * @return adaptorMetaId
	 *
	 */
	public Integer getAdaptorMetaId () {
		return this.adaptorMetaId;
	}




	/** 
	 * adaptorId
	 *
	 */
	@Column(name="adaptorid")
	protected Integer adaptorId;

	/**
	 * setAdaptorId(Integer adaptorId)
	 *
	 * @param adaptorId
	 *
	 */
	
	public void setAdaptorId (Integer adaptorId) {
		this.adaptorId = adaptorId;
	}

	/**
	 * getAdaptorId()
	 *
	 * @return adaptorId
	 *
	 */
	public Integer getAdaptorId () {
		return this.adaptorId;
	}




	/**
	 * adaptor
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="adaptorid", insertable=false, updatable=false)
	protected Adaptor adaptor;

	/**
	 * setAdaptor (Adaptor adaptor)
	 *
	 * @param adaptor
	 *
	 */
	public void setAdaptor (Adaptor adaptor) {
		this.adaptor = adaptor;
		this.adaptorId = adaptor.adaptorId;
	}

	/**
	 * getAdaptor ()
	 *
	 * @return adaptor
	 *
	 */
	
	public Adaptor getAdaptor () {
		return this.adaptor;
	}


}
