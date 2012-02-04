
/**
 *
 * Staterunlane.java 
 * @author echeng (table2type.pl)
 *  
 * the Staterunlane
 *
 *
 */

package edu.yu.einstein.wasp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="staterunlane")
public class Staterunlane extends WaspModel {

	/** 
	 * staterunlaneId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer staterunlaneId;

	/**
	 * setStaterunlaneId(Integer staterunlaneId)
	 *
	 * @param staterunlaneId
	 *
	 */
	
	public void setStaterunlaneId (Integer staterunlaneId) {
		this.staterunlaneId = staterunlaneId;
	}

	/**
	 * getStaterunlaneId()
	 *
	 * @return staterunlaneId
	 *
	 */
	public Integer getStaterunlaneId () {
		return this.staterunlaneId;
	}




	/** 
	 * stateId
	 *
	 */
	@Column(name="stateid")
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
	 * runlaneId
	 *
	 */
	@Column(name="runlaneid")
	protected Integer runlaneId;

	/**
	 * setRunlaneId(Integer runlaneId)
	 *
	 * @param runlaneId
	 *
	 */
	
	public void setRunlaneId (Integer runlaneId) {
		this.runlaneId = runlaneId;
	}

	/**
	 * getRunlaneId()
	 *
	 * @return runlaneId
	 *
	 */
	public Integer getRunlaneId () {
		return this.runlaneId;
	}




	/**
	 * state
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="stateid", insertable=false, updatable=false)
	protected State state;

	/**
	 * setState (State state)
	 *
	 * @param state
	 *
	 */
	public void setState (State state) {
		this.state = state;
		this.stateId = state.stateId;
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
	 * runLane
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="runlaneid", insertable=false, updatable=false)
	protected RunLane runLane;

	/**
	 * setRunLane (RunLane runLane)
	 *
	 * @param runLane
	 *
	 */
	public void setRunLane (RunLane runLane) {
		this.runLane = runLane;
		this.runlaneId = runLane.runLaneId;
	}

	/**
	 * getRunLane ()
	 *
	 * @return runLane
	 *
	 */
	
	public RunLane getRunLane () {
		return this.runLane;
	}


}
