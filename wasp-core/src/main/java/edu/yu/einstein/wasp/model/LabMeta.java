
/**
 *
 * LabMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the LabMeta
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
@Table(name="labmeta")
public class LabMeta extends MetaBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9101263444374881281L;
	

	/**
	 * setLabMetaId(Integer labMetaId)
	 *
	 * @param labMetaId
	 *
	 */
	@Deprecated
	public void setLabMetaId (Integer labMetaId) {
		setId(labMetaId);
	}

	/**
	 * getLabMetaId()
	 *
	 * @return labMetaId
	 *
	 */
	@Deprecated
	public Integer getLabMetaId () {
		return getId();
	}




	/** 
	 * labId
	 *
	 */
	@Column(name="labid")
	protected Integer labId;

	/**
	 * setLabId(Integer labId)
	 *
	 * @param labId
	 *
	 */
	
	public void setLabId (Integer labId) {
		this.labId = labId;
	}

	/**
	 * getLabId()
	 *
	 * @return labId
	 *
	 */
	public Integer getLabId () {
		return this.labId;
	}




	/**
	 * lab
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="labid", insertable=false, updatable=false)
	protected Lab lab;

	/**
	 * setLab (Lab lab)
	 *
	 * @param lab
	 *
	 */
	public void setLab (Lab lab) {
		this.lab = lab;
		this.labId = lab.getId();
	}

	/**
	 * getLab ()
	 *
	 * @return lab
	 *
	 */
	
	public Lab getLab () {
		return this.lab;
	}


}
