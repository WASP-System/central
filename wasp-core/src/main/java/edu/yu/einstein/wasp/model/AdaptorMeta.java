
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
	 * 
	 */
	private static final long serialVersionUID = -7315434207394500686L;

	/**
	 * setAdaptorMetaId(Integer adaptorMetaId)
	 *
	 * @param adaptorMetaId
	 *
	 */
	@Deprecated
	public void setAdaptorMetaId (Integer adaptorMetaId) {
		setId(adaptorMetaId);
	}

	/**
	 * getAdaptorMetaId()
	 *
	 * @return adaptorMetaId
	 *
	 */
	@Deprecated
	public Integer getAdaptorMetaId () {
		return getId();
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
		this.adaptorId = adaptor.getId();
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
