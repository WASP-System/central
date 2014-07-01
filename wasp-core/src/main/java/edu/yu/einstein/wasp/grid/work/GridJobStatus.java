package edu.yu.einstein.wasp.grid.work;


public class GridJobStatus {
	
	public static final GridJobStatus UNKNOWN = new GridJobStatus("unknown");
	public static final GridJobStatus EXECUTING = new GridJobStatus("executing");
	public static final GridJobStatus COMPLETED = new GridJobStatus("completed");
	public static final GridJobStatus FAILED = new GridJobStatus("failed");
	
	private String exitStatus = "unknown";
	
	public GridJobStatus(String exitStatus){
		this.exitStatus = exitStatus;
	}
	
	public boolean isRunning(){
		GridJobStatus currentJs = new GridJobStatus(exitStatus);
		if (currentJs.equals(EXECUTING))
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
