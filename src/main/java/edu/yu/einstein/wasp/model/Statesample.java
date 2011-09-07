
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

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import org.hibernate.validator.constraints.*;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Audited
@Table(name="statesample")
public class Statesample extends WaspModel {

	/** 
	 * statesampleId
	 *
	 */
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int statesampleId;

	/**
	 * setStatesampleId(int statesampleId)
	 *
	 * @param statesampleId
	 *
	 */
	
	public void setStatesampleId (int statesampleId) {
		this.statesampleId = statesampleId;
	}

	/**
	 * getStatesampleId()
	 *
	 * @return statesampleId
	 *
	 */
	public int getStatesampleId () {
		return this.statesampleId;
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
	 * sampleId
	 *
	 */
	@Column(name="sampleid")
	protected int sampleId;

	/**
	 * setSampleId(int sampleId)
	 *
	 * @param sampleId
	 *
	 */
	
	public void setSampleId (int sampleId) {
		this.sampleId = sampleId;
	}

	/**
	 * getSampleId()
	 *
	 * @return sampleId
	 *
	 */
	public int getSampleId () {
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
