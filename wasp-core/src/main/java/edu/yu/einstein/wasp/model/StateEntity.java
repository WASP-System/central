package edu.yu.einstein.wasp.model;

/**
 *
 * Statesample.java 
 * @author echeng (table2type.pl)
 *  
 * the Statesample
 *
 *
 */


public interface StateEntity {

	/** 
	 * stateId
	 *
	 */
	// protected Integer stateId;

	/**
	 * setStateId(Integer stateId)
	 *
	 * @param stateId
	 *
	 */
	
	public void setStateId (Integer stateId);

	/**
	 * getStateId()
	 *
	 * @return stateId
	 *
	 */
	public Integer getStateId ();


	/**
	 * state
	 *
	 */
	// protected State state;

	/**
	 * setState (State state)
	 *
	 * @param state
	 *
	 */
	public void setState (State state);

	/**
	 * getState ()
	 *
	 * @return state
	 *
	 */
	
	public State getState ();


}
