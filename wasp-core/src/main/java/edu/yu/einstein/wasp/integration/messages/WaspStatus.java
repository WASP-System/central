package edu.yu.einstein.wasp.integration.messages;


/**
 * Status that a run can be in. A run might be a sequencing run on an Illumina HiSeq. Each status has a priority assigned to it
 * where the highest values are associated with higher priority. These can be useful for messages transmitted on priority channels.
 * 
 * This enum is modeled after the Spring Batch BatchStatus enum.
 * 
 * @author asmclellan / brent
 *
 */
public enum WaspStatus {
	UNKNOWN (1),
	ABANDONED (2),
	COMPLETED (2),
	FAILED (2),
	STOPPED (3),
	RESTARTED (4),
	STARTED (5),
	ACCEPTED (6),
	CREATED (7);
	
	
	private Integer priority;
	
	/**
	 * constructor
	 * @param priority
	 */
	WaspStatus(Integer priority){
		this.priority=priority;
	}
	
	/**
	 * getter for the priority attribute (value between 1 and 5 where a higher value means higher priority)
	 * @return
	 */
	public Integer getPriority(){
		return this.priority;
	}
	
	public static WaspStatus max(WaspStatus status1, WaspStatus status2) {
		return status1.isGreaterThan(status2) ? status1 : status2;
	}
	
	/**
	 * Is the status created or running?
	 * @return
	 */
	public boolean isActive() {
		return this.isGreaterThanOrEqualTo(STOPPED);
	}
	
	/**
	 * Is the status a final type (FAILED, COMPLETED, ABANDONED)
	 * @return
	 */
	public boolean isFinished() {
		return this.isLessThanOrEqualTo(FAILED);
	}
	
	/**
	 * Is the status marked as completed by the system (COMPLETED)
	 * @return
	 */
	public boolean isSuccessful() {
		return this == COMPLETED;
	}
	
	public boolean isUnknown() {
		return this == UNKNOWN;
	}
	
	/**
	 * Was there a failure (FAILED of ABANDONED)?
	 * @return
	 */
	public boolean isUnsuccessful() {
		return this == FAILED || this == ABANDONED;
	}
	
	
	public boolean isGreaterThan(WaspStatus other) {
		return this.compareTo(other) > 0;
	}
	
	public boolean isGreaterThanOrEqualTo(WaspStatus other) {
		return this.compareTo(other) >= 0;
	}

	public boolean isLessThan(WaspStatus other) {
		return this.compareTo(other) < 0;
	}
	
	public boolean isLessThanOrEqualTo(WaspStatus other) {
		return this.compareTo(other) <= 0;
	}
}
