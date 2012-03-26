
/**
 *
 * StateRunCell.java 
 * @author echeng (table2type.pl)
 *  
 * the StateRunCell
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
@Table(name="stateruncell")
public class StateRunCell extends WaspModel {

	/** 
	 * stateruncellId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer stateruncellId;

	/**
	 * setStateruncellId(Integer stateruncellId)
	 *
	 * @param stateruncellId
	 *
	 */
	
	public void setStateruncellId (Integer stateruncellId) {
		this.stateruncellId = stateruncellId;
	}

	/**
	 * getStateruncellId()
	 *
	 * @return stateruncellId
	 *
	 */
	public Integer getStateruncellId () {
		return this.stateruncellId;
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
	 * runcellId
	 *
	 */
	@Column(name="runcellid")
	protected Integer runcellId;

	/**
	 * setRunlaneId(Integer runcellId)
	 *
	 * @param runcellId
	 *
	 */
	
	public void setRunlaneId (Integer runcellId) {
		this.runcellId = runcellId;
	}

	/**
	 * getRunlaneId()
	 *
	 * @return runcellId
	 *
	 */
	public Integer getRunlaneId () {
		return this.runcellId;
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
	 * runCell
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="runcellid", insertable=false, updatable=false)
	protected RunCell runCell;

	/**
	 * setRunCell (RunCell runCell)
	 *
	 * @param runCell
	 *
	 */
	public void setRunCell (RunCell runCell) {
		this.runCell = runCell;
		this.runcellId = runCell.runCellId;
	}

	/**
	 * getRunCell ()
	 *
	 * @return runCell
	 *
	 */
	
	public RunCell getRunCell () {
		return this.runCell;
	}


}
