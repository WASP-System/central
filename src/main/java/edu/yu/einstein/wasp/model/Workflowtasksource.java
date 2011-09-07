
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
	protected int workflowtasksourceId;

	/**
	 * setWorkflowtasksourceId(int workflowtasksourceId)
	 *
	 * @param workflowtasksourceId
	 *
	 */
	
	public void setWorkflowtasksourceId (int workflowtasksourceId) {
		this.workflowtasksourceId = workflowtasksourceId;
	}

	/**
	 * getWorkflowtasksourceId()
	 *
	 * @return workflowtasksourceId
	 *
	 */
	public int getWorkflowtasksourceId () {
		return this.workflowtasksourceId;
	}




	/** 
	 * workflowtaskId
	 *
	 */
	@Column(name="workflowtaskid")
	protected int workflowtaskId;

	/**
	 * setWorkflowtaskId(int workflowtaskId)
	 *
	 * @param workflowtaskId
	 *
	 */
	
	public void setWorkflowtaskId (int workflowtaskId) {
		this.workflowtaskId = workflowtaskId;
	}

	/**
	 * getWorkflowtaskId()
	 *
	 * @return workflowtaskId
	 *
	 */
	public int getWorkflowtaskId () {
		return this.workflowtaskId;
	}




	/** 
	 * sourceworkflowtaskId
	 *
	 */
	@Column(name="sourceworkflowtaskid")
	protected int sourceworkflowtaskId;

	/**
	 * setSourceworkflowtaskId(int sourceworkflowtaskId)
	 *
	 * @param sourceworkflowtaskId
	 *
	 */
	
	public void setSourceworkflowtaskId (int sourceworkflowtaskId) {
		this.sourceworkflowtaskId = sourceworkflowtaskId;
	}

	/**
	 * getSourceworkflowtaskId()
	 *
	 * @return sourceworkflowtaskId
	 *
	 */
	public int getSourceworkflowtaskId () {
		return this.sourceworkflowtaskId;
	}




}
