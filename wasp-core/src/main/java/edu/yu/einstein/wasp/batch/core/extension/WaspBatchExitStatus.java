package edu.yu.einstein.wasp.batch.core.extension;

import org.springframework.batch.core.ExitStatus;

public class WaspBatchExitStatus extends ExitStatus {

	private static final long serialVersionUID = -1863550408541926065L;
	
	/**
	 * Is Running but is in a hibernated state
	 */
	public static final ExitStatus HIBERNATING = new ExitStatus("HIBERNATING");
	
	/**
	 * Has been terminated (manually stopped)
	 */
	public static final ExitStatus TERMINATED = new ExitStatus("TERMINATED");
	
	/**
	 * A hybrid (virtual) status. A user asking for a job/step in ExitStatus 'RUNNING' is asking
	 * if it is either in State EXECUTING, UNKNOWN or HIBERNATING.
	 */
	public static final ExitStatus RUNNING = new ExitStatus("RUNNING");
	
	public WaspBatchExitStatus(ExitStatus exitStatus) {
		super(exitStatus.getExitCode(), exitStatus.getExitDescription());
	}

	public WaspBatchExitStatus(String exitCode) {
		super(exitCode);
	}

	public WaspBatchExitStatus(String exitCode, String exitDescription) {
		super(exitCode, exitDescription);
	}
	
	/**
	 * true if the exit code is "EXECUTING", "HIBERNATING" or "UNKNOWN"
	 */
	@Override
	public boolean isRunning(){
		if (super.isRunning() || getExitCode().equals(HIBERNATING))
			return true;
		return false;
	}
	
	public boolean isHibernating(){
		return getExitCode().equals(HIBERNATING);
	}
	
	public boolean isCompleted(){
		return getExitCode().equals(COMPLETED);
	}
	
	public boolean isFailed(){
		return getExitCode().equals(FAILED);
	}
	
	public boolean isTerminated(){
		return getExitCode().equals(TERMINATED);
	}

}
