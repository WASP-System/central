
/**
 *
 * Workflowtask.java 
 * @author echeng (table2type.pl)
 *  
 * the Workflowtask
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
@Table(name="workflowtask")
public class Workflowtask extends WaspModel {

	/** 
	 * workflowtaskId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
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
	 * workflowId
	 *
	 */
	@Column(name="workflowid")
	protected int workflowId;

	/**
	 * setWorkflowId(int workflowId)
	 *
	 * @param workflowId
	 *
	 */
	
	public void setWorkflowId (int workflowId) {
		this.workflowId = workflowId;
	}

	/**
	 * getWorkflowId()
	 *
	 * @return workflowId
	 *
	 */
	public int getWorkflowId () {
		return this.workflowId;
	}




	/** 
	 * taskId
	 *
	 */
	@Column(name="taskid")
	protected int taskId;

	/**
	 * setTaskId(int taskId)
	 *
	 * @param taskId
	 *
	 */
	
	public void setTaskId (int taskId) {
		this.taskId = taskId;
	}

	/**
	 * getTaskId()
	 *
	 * @return taskId
	 *
	 */
	public int getTaskId () {
		return this.taskId;
	}




	/** 
	 * iName
	 *
	 */
	@Column(name="iname")
	protected String iName;

	/**
	 * setIName(String iName)
	 *
	 * @param iName
	 *
	 */
	
	public void setIName (String iName) {
		this.iName = iName;
	}

	/**
	 * getIName()
	 *
	 * @return iName
	 *
	 */
	public String getIName () {
		return this.iName;
	}




	/** 
	 * name
	 *
	 */
	@Column(name="name")
	protected String name;

	/**
	 * setName(String name)
	 *
	 * @param name
	 *
	 */
	
	public void setName (String name) {
		this.name = name;
	}

	/**
	 * getName()
	 *
	 * @return name
	 *
	 */
	public String getName () {
		return this.name;
	}




}
