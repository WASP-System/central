package edu.yu.einstein.wasp.grid.work;

import java.io.Serializable;


public class GridJobStatus implements Serializable{
	
	private static final long serialVersionUID = -380186718810033714L;
	
	public static final GridJobStatus UNKNOWN = new GridJobStatus("unknown"); // status unknown
	
	//the following status' refer to cluster observations 
	public static final GridJobStatus STARTED = new GridJobStatus("started"); // has started on cluster
	public static final GridJobStatus ENDED = new GridJobStatus("ended"); // has ended on cluster
	
	// the following status' refer to assessments of the success of the job after ending i.e. ending could be because of success or failure
	public static final GridJobStatus COMPLETED = new GridJobStatus("completed"); // is assessed to be completed successfully
	public static final GridJobStatus FAILED = new GridJobStatus("failed"); // is assessed to have failed
	
	private String exitStatus = "unknown";
	
	public GridJobStatus(){}
	
	public GridJobStatus(String exitStatus){
		this.exitStatus = exitStatus;
	}
	
	public boolean isRunning(){
		GridJobStatus currentJs = new GridJobStatus(exitStatus);
		if (currentJs.equals(STARTED))
			return true;
		return false;
	}
	
	public boolean isCompletedSuccessfully(){
		GridJobStatus currentJs = new GridJobStatus(exitStatus);
		if (currentJs.equals(COMPLETED))
			return true;
		return false;
	}
	
	public boolean isFailed(){
		GridJobStatus currentJs = new GridJobStatus(exitStatus);
		if (currentJs.equals(FAILED))
			return true;
		return false;
	}
	
	public boolean isUnknown(){
		GridJobStatus currentJs = new GridJobStatus(exitStatus);
		if (currentJs.equals(UNKNOWN))
			return true;
		return false;
	}
	
	public boolean isFinished(){
		GridJobStatus currentJs = new GridJobStatus(exitStatus);
		if (currentJs.equals(FAILED) || currentJs.equals(COMPLETED))
			return true;
		return false;
	}
	
	@Override
	public String toString(){
		return exitStatus;
	}
	
	@Override
	public boolean equals(Object obj){
		if (this == obj) return true;
		if (obj == null) return false;
		if (!this.getClass().isInstance(obj) && !obj.getClass().isInstance(this)) 
			return false; // allow comparison if one class is derived from the other
		return exitStatus.equals(obj.toString());
	}
	
	@Override
	public int hashCode(){
		return exitStatus.hashCode();
	}

}
