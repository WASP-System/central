
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

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import org.hibernate.validator.constraints.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="statemeta")
public class StateMeta extends MetaBase {

	/** 
	 * stateMetaId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int stateMetaId;

	/**
	 * setStateMetaId(int stateMetaId)
	 *
	 * @param stateMetaId
	 *
	 */
	
	public void setStateMetaId (int stateMetaId) {
		this.stateMetaId = stateMetaId;
	}

	/**
	 * getStateMetaId()
	 *
	 * @return stateMetaId
	 *
	 */
	public int getStateMetaId () {
		return this.stateMetaId;
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
