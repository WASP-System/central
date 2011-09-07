
/**
 *
 * Staterun.java 
 * @author echeng (table2type.pl)
 *  
 * the Staterun
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
@Table(name="staterun")
public class Staterun extends WaspModel {

	/** 
	 * staterunId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int staterunId;

	/**
	 * setStaterunId(int staterunId)
	 *
	 * @param staterunId
	 *
	 */
	
	public void setStaterunId (int staterunId) {
		this.staterunId = staterunId;
	}

	/**
	 * getStaterunId()
	 *
	 * @return staterunId
	 *
	 */
	public int getStaterunId () {
		return this.staterunId;
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
	 * runId
	 *
	 */
	@Column(name="runid")
	protected int runId;

	/**
	 * setRunId(int runId)
	 *
	 * @param runId
	 *
	 */
	
	public void setRunId (int runId) {
		this.runId = runId;
	}

	/**
	 * getRunId()
	 *
	 * @return runId
	 *
	 */
	public int getRunId () {
		return this.runId;
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
	 * run
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="runid", insertable=false, updatable=false)
	protected Run run;

	/**
	 * setRun (Run run)
	 *
	 * @param run
	 *
	 */
	public void setRun (Run run) {
		this.run = run;
		this.runId = run.runId;
	}

	/**
	 * getRun ()
	 *
	 * @return run
	 *
	 */
	
	public Run getRun () {
		return this.run;
	}


}
