
/**
 *
 * State.java 
 * @author echeng (table2type.pl)
 *  
 * the State
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
@Table(name="state")
public class State extends WaspModel {

	/** 
	 * stateId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer stateId;

	/**
	 * setStateId(Integer stateId)
	 *
	 * @param stateId
	 *
	 */
	
	public void setStateId (Integer stateId) {
		this.stateId = stateId;
	}

	/**
	 * getStateId()
	 *
	 * @return stateId
	 *
	 */
	public Integer getStateId () {
		return this.stateId;
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
	 * status
	 *
	 */
	@Column(name="status")
	protected String status;

	/**
	 * setStatus(String status)
	 *
	 * @param status
	 *
	 */
	
	public void setStatus (String status) {
		this.status = status;
	}

	/**
	 * getStatus()
	 *
	 * @return status
	 *
	 */
	public String getStatus () {
		return this.status;
	}




	/** 
	 * sourceStateId
	 *
	 */
	@Column(name="source_stateid")
	protected Integer sourceStateId;

	/**
	 * setSourceStateId(Integer sourceStateId)
	 *
	 * @param sourceStateId
	 *
	 */
	
	public void setSourceStateId (Integer sourceStateId) {
		this.sourceStateId = sourceStateId;
	}

	/**
	 * getSourceStateId()
	 *
	 * @return sourceStateId
	 *
	 */
	public Integer getSourceStateId () {
		return this.sourceStateId;
	}




	/** 
	 * startts
	 *
	 */
	@Column(name="startts")
	protected Date startts;

	/**
	 * setStartts(Date startts)
	 *
	 * @param startts
	 *
	 */
	
	public void setStartts (Date startts) {
		this.startts = startts;
	}

	/**
	 * getStartts()
	 *
	 * @return startts
	 *
	 */
	public Date getStartts () {
		return this.startts;
	}




	/** 
	 * endts
	 *
	 */
	@Column(name="endts")
	protected Date endts;

	/**
	 * setEndts(Date endts)
	 *
	 * @param endts
	 *
	 */
	
	public void setEndts (Date endts) {
		this.endts = endts;
	}

	/**
	 * getEndts()
	 *
	 * @return endts
	 *
	 */
	public Date getEndts () {
		return this.endts;
	}




	/** 
	 * lastUpdTs
	 *
	 */
	@Column(name="lastupdts")
	protected Date lastUpdTs;

	/**
	 * setLastUpdTs(Date lastUpdTs)
	 *
	 * @param lastUpdTs
	 *
	 */
	
	public void setLastUpdTs (Date lastUpdTs) {
		this.lastUpdTs = lastUpdTs;
	}

	/**
	 * getLastUpdTs()
	 *
	 * @return lastUpdTs
	 *
	 */
	public Date getLastUpdTs () {
		return this.lastUpdTs;
	}




	/** 
	 * lastUpdUser
	 *
	 */
	@Column(name="lastupduser")
	protected Integer lastUpdUser;

	/**
	 * setLastUpdUser(Integer lastUpdUser)
	 *
	 * @param lastUpdUser
	 *
	 */
	
	public void setLastUpdUser (Integer lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	/**
	 * getLastUpdUser()
	 *
	 * @return lastUpdUser
	 *
	 */
	public Integer getLastUpdUser () {
		return this.lastUpdUser;
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
	 * state
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="source_stateid", insertable=false, updatable=false)
	protected State state;

	/**
	 * setState (State state)
	 *
	 * @param state
	 *
	 */
	public void setState (State state) {
		this.state = state;
		this.sourceStateId = state.stateId;
	}

	/**
	 * getState ()
	 *
	 * @return state
	 *
	 */
	
	public State getState () {
		return this.state;
	}


	/** 
	 * stateViaSourceStateId
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="source_stateid", insertable=false, updatable=false)
	protected List<State> stateViaSourceStateId;


	/** 
	 * getStateViaSourceStateId()
	 *
	 * @return stateViaSourceStateId
	 *
	 */
	public List<State> getStateViaSourceStateId() {
		return this.stateViaSourceStateId;
	}


	/** 
	 * setStateViaSourceStateId
	 *
	 * @param stateViaSourceStateId
	 *
	 */
	public void setStateViaSourceStateId (List<State> stateViaSourceStateId) {
		this.stateViaSourceStateId = stateViaSourceStateId;
	}



	/** 
	 * stateMeta
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="stateid", insertable=false, updatable=false)
	protected List<StateMeta> stateMeta;


	/** 
	 * getStateMeta()
	 *
	 * @return stateMeta
	 *
	 */
	public List<StateMeta> getStateMeta() {
		return this.stateMeta;
	}


	/** 
	 * setStateMeta
	 *
	 * @param stateMeta
	 *
	 */
	public void setStateMeta (List<StateMeta> stateMeta) {
		this.stateMeta = stateMeta;
	}



	/** 
	 * statejob
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="stateid", insertable=false, updatable=false)
	protected List<Statejob> statejob;


	/** 
	 * getStatejob()
	 *
	 * @return statejob
	 *
	 */
	public List<Statejob> getStatejob() {
		return this.statejob;
	}


	/** 
	 * setStatejob
	 *
	 * @param statejob
	 *
	 */
	public void setStatejob (List<Statejob> statejob) {
		this.statejob = statejob;
	}



	/** 
	 * statesample
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="stateid", insertable=false, updatable=false)
	protected List<Statesample> statesample;


	/** 
	 * getStatesample()
	 *
	 * @return statesample
	 *
	 */
	public List<Statesample> getStatesample() {
		return this.statesample;
	}


	/** 
	 * setStatesample
	 *
	 * @param statesample
	 *
	 */
	public void setStatesample (List<Statesample> statesample) {
		this.statesample = statesample;
	}



	/** 
	 * staterun
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="stateid", insertable=false, updatable=false)
	protected List<Staterun> staterun;


	/** 
	 * getStaterun()
	 *
	 * @return staterun
	 *
	 */
	public List<Staterun> getStaterun() {
		return this.staterun;
	}


	/** 
	 * setStaterun
	 *
	 * @param staterun
	 *
	 */
	public void setStaterun (List<Staterun> staterun) {
		this.staterun = staterun;
	}



	/** 
	 * staterunlane
	 *
	 */
	@NotAudited
	@OneToMany
	@JoinColumn(name="stateid", insertable=false, updatable=false)
	protected List<Staterunlane> staterunlane;


	/** 
	 * getStaterunlane()
	 *
	 * @return staterunlane
	 *
	 */
	public List<Staterunlane> getStaterunlane() {
		return this.staterunlane;
	}


	/** 
	 * setStaterunlane
	 *
	 * @param staterunlane
	 *
	 */
	public void setStaterunlane (List<Staterunlane> staterunlane) {
		this.staterunlane = staterunlane;
	}



}
