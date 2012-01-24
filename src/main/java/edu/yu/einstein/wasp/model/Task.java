
/**
 *
 * Task.java 
 * @author echeng (table2type.pl)
 *  
 * the Task
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
@Table(name="task")
public class Task extends WaspModel {

	/** 
	 * taskId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
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
	 * workflowtask
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="taskid", insertable=false, updatable=false)
	protected List<Workflowtask> workflowtask;


	/** 
	 * getWorkflowtask()
	 *
	 * @return workflowtask
	 *
	 */
	public List<Workflowtask> getWorkflowtask() {
		return this.workflowtask;
	}


	/** 
	 * setWorkflowtask
	 *
	 * @param workflowtask
	 *
	 */
	public void setWorkflowtask (List<Workflowtask> workflowtask) {
		this.workflowtask = workflowtask;
	}



	/** 
	 * state
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="taskid", insertable=false, updatable=false)
	protected List<State> state;


	/** 
	 * getState()
	 *
	 * @return state
	 *
	 */
	public List<State> getState() {
		return this.state;
	}


	/** 
	 * setState
	 *
	 * @param state
	 *
	 */
	public void setState (List<State> state) {
		this.state = state;
	}



	/** 
	 * taskMapping
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="taskid", insertable=false, updatable=false)
	protected List<TaskMapping> taskMapping;


	/** 
	 * getTaskMapping()
	 *
	 * @return taskMapping
	 *
	 */
	public List<TaskMapping> getTaskMapping() {
		return this.taskMapping;
	}


	/** 
	 * setTaskMapping
	 *
	 * @param taskMapping
	 *
	 */
	public void setTaskMapping (List<TaskMapping> taskMapping) {
		this.taskMapping = taskMapping;
	}



}
