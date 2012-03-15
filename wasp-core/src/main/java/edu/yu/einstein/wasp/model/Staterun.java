
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
@Table(name="staterun")
public class Staterun extends WaspModel implements StateEntity {

	/** 
	 * staterunId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer staterunId;

	/**
	 * setStaterunId(Integer staterunId)
	 *
	 * @param staterunId
	 *
	 */
	
	public void setStaterunId (Integer staterunId) {
		this.staterunId = staterunId;
	}

	/**
	 * getStaterunId()
	 *
	 * @return staterunId
	 *
	 */
	public Integer getStaterunId () {
		return this.staterunId;
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
	
	@Override
	public void setStateId (Integer stateId) {
		this.stateId = stateId;
	}

	/**
	 * getStateId()
	 *
	 * @return stateId
	 *
	 */
	@Override
	public Integer getStateId () {
		return this.stateId;
	}




	/** 
	 * runId
	 *
	 */
	@Column(name="runid")
	protected Integer runId;

	/**
	 * setRunId(Integer runId)
	 *
	 * @param runId
	 *
	 */
	
	public void setRunId (Integer runId) {
		this.runId = runId;
	}

	/**
	 * getRunId()
	 *
	 * @return runId
	 *
	 */
	public Integer getRunId () {
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
	@Override
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
	
	@Override
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
