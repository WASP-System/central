
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

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

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



}
