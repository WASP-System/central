
/**
 *
 * StateMeta.java 
 * @author echeng (table2type.pl)
 *  
 * the StateMeta
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
@Table(name="statemeta")
public class StateMeta extends MetaBase {

	/** 
	 * stateMetaId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer stateMetaId;

	/**
	 * setStateMetaId(Integer stateMetaId)
	 *
	 * @param stateMetaId
	 *
	 */
	
	public void setStateMetaId (Integer stateMetaId) {
		this.stateMetaId = stateMetaId;
	}

	/**
	 * getStateMetaId()
	 *
	 * @return stateMetaId
	 *
	 */
	public Integer getStateMetaId () {
		return this.stateMetaId;
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


}
