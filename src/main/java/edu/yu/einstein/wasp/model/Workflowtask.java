
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
	 * workflowId
	 *
	 */
	@Column(name="workflowid")
	protected Integer workflowId;

	/**
	 * setWorkflowId(Integer workflowId)
	 *
	 * @param workflowId
	 *
	 */
	
	public void setWorkflowId (Integer workflowId) {
		this.workflowId = workflowId;
	}

	/**
	 * getWorkflowId()
	 *
	 * @return workflowId
	 *
	 */
	public Integer getWorkflowId () {
		return this.workflowId;
	}




	/** 
	 * taskId
	 *
	 */
	@Column(name="taskid")
	protected Integer taskId;

	/**
	 * setTaskId(Integer taskId)
	 *
	 * @param taskId
	 *
	 */
	
	public void setTaskId (Integer taskId) {
		this.taskId = taskId;
	}

	/**
	 * getTaskId()
	 *
	 * @return taskId
	 *
	 */
	public Integer getTaskId () {
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




	/**
	 * workflow
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="workflowid", insertable=false, updatable=false)
	protected Workflow workflow;

	/**
	 * setWorkflow (Workflow workflow)
	 *
	 * @param workflow
	 *
	 */
	public void setWorkflow (Workflow workflow) {
		this.workflow = workflow;
		this.workflowId = workflow.workflowId;
	}

	/**
	 * getWorkflow ()
	 *
	 * @return workflow
	 *
	 */
	
	public Workflow getWorkflow () {
		return this.workflow;
	}


	/**
	 * task
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="taskid", insertable=false, updatable=false)
	protected Task task;

	/**
	 * setTask (Task task)
	 *
	 * @param task
	 *
	 */
	public void setTask (Task task) {
		this.task = task;
		this.taskId = task.taskId;
	}

	/**
	 * getTask ()
	 *
	 * @return task
	 *
	 */
	
	public Task getTask () {
		return this.task;
	}


	/** 
	 * workflowtasksource
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="workflowtaskid", insertable=false, updatable=false)
	protected List<Workflowtasksource> workflowtasksource;


	/** 
	 * getWorkflowtasksource()
	 *
	 * @return workflowtasksource
	 *
	 */
	public List<Workflowtasksource> getWorkflowtasksource() {
		return this.workflowtasksource;
	}


	/** 
	 * setWorkflowtasksource
	 *
	 * @param workflowtasksource
	 *
	 */
	public void setWorkflowtasksource (List<Workflowtasksource> workflowtasksource) {
		this.workflowtasksource = workflowtasksource;
	}



	/** 
	 * workflowtasksourceViaSourceworkflowtaskId
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="sourceworkflowtaskid", insertable=false, updatable=false)
	protected List<Workflowtasksource> workflowtasksourceViaSourceworkflowtaskId;


	/** 
	 * getWorkflowtasksourceViaSourceworkflowtaskId()
	 *
	 * @return workflowtasksourceViaSourceworkflowtaskId
	 *
	 */
	public List<Workflowtasksource> getWorkflowtasksourceViaSourceworkflowtaskId() {
		return this.workflowtasksourceViaSourceworkflowtaskId;
	}


	/** 
	 * setWorkflowtasksourceViaSourceworkflowtaskId
	 *
	 * @param workflowtasksource
	 *
	 */
	public void setWorkflowtasksourceViaSourceworkflowtaskId (List<Workflowtasksource> workflowtasksource) {
		this.workflowtasksourceViaSourceworkflowtaskId = workflowtasksource;
	}



}
