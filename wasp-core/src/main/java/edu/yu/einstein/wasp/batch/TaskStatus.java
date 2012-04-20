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
	 * 
	 *    This enum is modeled after the Spring Batch BatchStatus enum.
	 */
	FINALIZED, COMPLETED, RESOLVING, CREATED, FAILED, ABANDONED, UNKNOWN;
	
	public static TaskStatus max(TaskStatus status1, TaskStatus status2) {
		return status1.isGreaterThan(status2) ? status1 : status2;
	}
	
	/**
	 * Is the Task created or running?
	 * @return
	 */
	public boolean isActive() {
		return this == CREATED || this == RESOLVING;
	}
	
	/**
	 * Is the task marked as completed by the system (COMPLETED) or by batch processing (FINALIZED)
	 * @return
	 */
	public boolean isCompleted() {
		return this.isLessThanOrEqualTo(COMPLETED);
	}
	
	/**
	 * Was there a failure (FAILED, ABANDONED, UNKNOWN)?
	 * @return
	 */
	public boolean isUnsuccessful() {
		return this.isGreaterThanOrEqualTo(FAILED);
	}
	
	
	public boolean isGreaterThan(TaskStatus other) {
		return this.compareTo(other) > 0;
	}
	
	public boolean isGreaterThanOrEqualTo(TaskStatus other) {
		return this.compareTo(other) >= 0;
	}

	public boolean isLessThan(TaskStatus other) {
		return this.compareTo(other) < 0;
	}
	
	public boolean isLessThanOrEqualTo(TaskStatus other) {
		return this.compareTo(other) <= 0;
	}

}
