
/**
 *
 * Statesample.java 
 * @author echeng (table2type.pl)
 *  
 * the Statesample
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
@Table(name="statesample")
public class Statesample extends WaspModel implements StateEntity {

	/** 
	 * statesampleId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Integer statesampleId;

	/**
	 * setStatesampleId(Integer statesampleId)
	 *
	 * @param statesampleId
	 *
	 */
	
	public void setStatesampleId (Integer statesampleId) {
		this.statesampleId = statesampleId;
	}

	/**
	 * getStatesampleId()
	 *
	 * @return statesampleId
	 *
	 */
	public Integer getStatesampleId () {
		return this.statesampleId;
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
	 * sampleId
	 *
	 */
	@Column(name="sampleid")
	protected Integer sampleId;

	/**
	 * setSampleId(Integer sampleId)
	 *
	 * @param sampleId
	 *
	 */
	
	public void setSampleId (Integer sampleId) {
		this.sampleId = sampleId;
	}

	/**
	 * getSampleId()
	 *
	 * @return sampleId
	 *
	 */
	public Integer getSampleId () {
		return this.sampleId;
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
	 * sample
	 *
	 */
	@NotAudited
	@ManyToOne
	@JoinColumn(name="sampleid", insertable=false, updatable=false)
	protected Sample sample;

	/**
	 * setSample (Sample sample)
	 *
	 * @param sample
	 *
	 */
	public void setSample (Sample sample) {
		this.sample = sample;
		this.sampleId = sample.sampleId;
	}

	/**
	 * getSample ()
	 *
	 * @return sample
	 *
	 */
	
	public Sample getSample () {
		return this.sample;
	}


}
