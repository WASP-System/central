
/**
 *
 * LabPendingMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the LabPendingMeta
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
@Table(name="labpendingmeta")
public class LabPendingMeta extends MetaBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7711196439883404327L;
	

	/**
	 * setLabPendingMetaId(Integer labPendingMetaId)
	 *
	 * @param labPendingMetaId
	 *
	 */
	@Deprecated
	public void setLabPendingMetaId (Integer labPendingMetaId) {
		setId(labPendingMetaId);
	}

	/**
	 * getLabPendingMetaId()
	 *
	 * @return labPendingMetaId
	 *
	 */
	@Deprecated
	public Integer getLabPendingMetaId () {
		return getId();
	}




	/** 
	 * labpendingId
	 *
	 */
	@Column(name="labpendingid")
	protected Integer labpendingId;

	/**
	 * setLabpendingId(Integer labpendingId)
	 *
	 * @param labpendingId
	 *
	 */
	
	public void setLabpendingId (Integer labpendingId) {
		this.labpendingId = labpendingId;
	}

	/**
	 * getLabpendingId()
	 *
	 * @return labpendingId
	 *
	 */
	public Integer getLabpendingId () {
		return this.labpendingId;
	}




	/**
	 * labPending
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="labpendingid", insertable=false, updatable=false)
	protected LabPending labPending;

	/**
	 * setLabPending (LabPending labPending)
	 *
	 * @param labPending
	 *
	 */
	public void setLabPending (LabPending labPending) {
		this.labPending = labPending;
		this.labpendingId = labPending.getId();
	}

	/**
	 * getLabPending ()
	 *
	 * @return labPending
	 *
	 */
	
	public LabPending getLabPending () {
		return this.labPending;
	}


}
