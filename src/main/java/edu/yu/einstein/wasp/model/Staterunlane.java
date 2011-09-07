
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

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import org.hibernate.validator.constraints.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="staterunlane")
public class Staterunlane extends WaspModel {

	/** 
	 * staterunlaneId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int staterunlaneId;

	/**
	 * setStaterunlaneId(int staterunlaneId)
	 *
	 * @param staterunlaneId
	 *
	 */
	
	public void setStaterunlaneId (int staterunlaneId) {
		this.staterunlaneId = staterunlaneId;
	}

	/**
	 * getStaterunlaneId()
	 *
	 * @return staterunlaneId
	 *
	 */
	public int getStaterunlaneId () {
		return this.staterunlaneId;
	}




	/** 
	 * stateId
	 *
	 */
	@Column(name="stateid")
	protected int stateId;

	/**
	 * setStateId(int stateId)
	 *
	 * @param stateId
	 *
	 */
	
	public void setStateId (int stateId) {
		this.stateId = stateId;
	}

	/**
	 * getStateId()
	 *
	 * @return stateId
	 *
	 */
	public int getStateId () {
		return this.stateId;
	}




	/** 
	 * runlaneId
	 *
	 */
	@Column(name="runlaneid")
	protected int runlaneId;

	/**
	 * setRunlaneId(int runlaneId)
	 *
	 * @param runlaneId
	 *
	 */
	
	public void setRunlaneId (int runlaneId) {
		this.runlaneId = runlaneId;
	}

	/**
	 * getRunlaneId()
	 *
	 * @return runlaneId
	 *
	 */
	public int getRunlaneId () {
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
