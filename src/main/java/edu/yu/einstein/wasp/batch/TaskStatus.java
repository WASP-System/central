package edu.yu.einstein.wasp.batch;


/**
 * Enumeration representing the status of a WASP Task
 * 
 * @author calder
 *
 */
public enum TaskStatus {
	
	/**
	 * Task status representing the progression of a WASP task, often associated with a human interaction (HI)
	 * event. Tasks are CREATED, in order to offer clues to the user interface, and provide a hook for batch
	 * pipelines to detect if HI tasks have been performed.  When state needs to indicate when some long-running
	 * task has begun but not completed, the status is set to RESOLVING.  When events are performed (eg. approved,
	 * received, etc.), the user layer can set the state to COMPLETED, and upon progression the batch flow can
	 * set the task status to FINALIZED. FAILED, ABANDONED and UNKNOWN statuses are provided for accounting and
	 * system level task management/clean-up.   
	 */
	FINALIZED, COMPLETED, RESOLVING, CREATED, FAILED, ABANDONED, UNKNOWN;

}
