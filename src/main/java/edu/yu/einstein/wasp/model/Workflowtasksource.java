
/**
 *
 * Workflowtasksource.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflowtasksource
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
@Table(name="workflowtasksource")
public class Workflowtasksource extends WaspModel {

	/** 
	 * workflowtasksourceId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer workflowtasksourceId;

	/**
	 * setWorkflowtasksourceId(Integer workflowtasksourceId)
	 *
	 * @param workflowtasksourceId
	 *
	 */
	
	public void setWorkflowtasksourceId (Integer workflowtasksourceId) {
		this.workflowtasksourceId = workflowtasksourceId;
	}

	/**
	 * getWorkflowtasksourceId()
	 *
	 * @return workflowtasksourceId
	 *
	 */
	public Integer getWorkflowtasksourceId () {
		return this.workflowtasksourceId;
	}




	/** 
	 * workflowtaskId
	 *
	 */
	@Column(name="workflowtaskid")
	protected Integer workflowtaskId;

	/**
	 * setWorkflowtaskId(Integer workflowtaskId)
	 *
	 * @param workflowtaskId
	 *
	 */
	
	public void setWorkflowtaskId (Integer workflowtaskId) {
		this.workflowtaskId = workflowtaskId;
	}

	/**
	 * getWorkflowtaskId()
	 *
	 * @return workflowtaskId
	 *
	 */
	public Integer getWorkflowtaskId () {
		return this.workflowtaskId;
	}




	/** 
	 * sourceworkflowtaskId
	 *
	 */
	@Column(name="sourceworkflowtaskid")
	protected Integer sourceworkflowtaskId;

	/**
	 * setSourceworkflowtaskId(Integer sourceworkflowtaskId)
	 *
	 * @param sourceworkflowtaskId
	 *
	 */
	
	public void setSourceworkflowtaskId (Integer sourceworkflowtaskId) {
		this.sourceworkflowtaskId = sourceworkflowtaskId;
	}

	/**
	 * getSourceworkflowtaskId()
	 *
	 * @return sourceworkflowtaskId
	 *
	 */
	public Integer getSourceworkflowtaskId () {
		return this.sourceworkflowtaskId;
	}




	/**
	 * workflowtask
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="workflowtaskid", insertable=false, updatable=false)
	protected Workflowtask workflowtask;

	/**
	 * setWorkflowtask (Workflowtask workflowtask)
	 *
	 * @param workflowtask
	 *
	 */
	public void setWorkflowtask (Workflowtask workflowtask) {
		this.workflowtask = workflowtask;
		this.workflowtaskId = workflowtask.workflowtaskId;
	}

	/**
	 * getWorkflowtask ()
	 *
	 * @return workflowtask
	 *
	 */
	
	public Workflowtask getWorkflowtask () {
		return this.workflowtask;
	}


	/**
	 * workflowtaskVia
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="sourceworkflowtaskid", insertable=false, updatable=false)
	protected Workflowtask workflowtaskVia;

	/**
	 * setWorkflowtaskVia (Workflowtask workflowtask)
	 *
	 * @param workflowtask
	 *
	 */
	public void setWorkflowtaskVia (Workflowtask workflowtask) {
		this.workflowtask = workflowtask;
		this.sourceworkflowtaskId = workflowtask.workflowtaskId;
	}

	/**
	 * getWorkflowtaskVia ()
	 *
	 * @return workflowtask
	 *
	 */
	
	public Workflowtask getWorkflowtaskVia () {
		return this.workflowtask;
	}


}
