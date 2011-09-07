
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

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import org.hibernate.validator.constraints.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="labmeta")
public class LabMeta extends MetaBase {

	/** 
	 * labMetaId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int labMetaId;

	/**
	 * setLabMetaId(int labMetaId)
	 *
	 * @param labMetaId
	 *
	 */
	
	public void setLabMetaId (int labMetaId) {
		this.labMetaId = labMetaId;
	}

	/**
	 * getLabMetaId()
	 *
	 * @return labMetaId
	 *
	 */
	public int getLabMetaId () {
		return this.labMetaId;
	}




	/** 
	 * labId
	 *
	 */
	@Column(name="labid")
	protected int labId;

	/**
	 * setLabId(int labId)
	 *
	 * @param labId
	 *
	 */
	
	public void setLabId (int labId) {
		this.labId = labId;
	}

	/**
	 * getLabId()
	 *
	 * @return labId
	 *
	 */
	public int getLabId () {
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
		this.labId = lab.labId;
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
