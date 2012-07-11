package edu.yu.einstein.wasp.messages;

/**
 * Status that a run can be in. A run might be a sequencing run on an Illumina HiSeq. Each status has a priority assigned to it
 * where the highest values are associated with higher priority. These can be useful for messages transmitted on priority channels.
 * @author andymac
 *
 */
public enum WaspRunStatus {
	UNKNOWN (1),
	CREATED (1),
	STARTED (2),
	STOPPED (3),
	COMPLETED (4),
	FAILED (4),
	ABANDONED (5);
	
	
	private Integer priority;
	
	/**
	 * constructor
	 * @param priority
	 */
	WaspRunStatus(Integer priority){
		this.priority=priority;
	}
	
	/**
	 * getter for the priority attribute (value between 1 and 5 where a higher value means higher priority)
	 * @return
	 */
	public Integer getPriority(){
		return this.priority;
	}
}
