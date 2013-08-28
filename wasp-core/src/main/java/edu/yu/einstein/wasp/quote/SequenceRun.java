package edu.yu.einstein.wasp.quote;

/**
 * 
 * @author dubin
 *
 */

public class SequenceRun {

	private String machine;
	private Integer readLength;
	private String readType;
	private Integer numberOfLanes;
	private Float costPerLane;
	private String error;
	
	public SequenceRun(String machine, Integer readLength, String readType, Integer numberOfLanes, Float costPerLane){
		
		this.machine = machine;
		this.readLength = readLength;
		this.readType = readType;
		this.numberOfLanes = numberOfLanes;
		this.costPerLane = costPerLane;
		this.error = "";
	}
	public SequenceRun(String machine, Integer readLength, String readType, Integer numberOfLanes, Float costPerLane, String error){
		
		this.machine = machine;
		this.readLength = readLength;
		this.readType = readType;
		this.numberOfLanes = numberOfLanes;
		this.costPerLane = costPerLane;
		this.error = error;
	}
	
	public String getMachine(){
		return this.machine;
	}
	public Integer getReadLength(){
		return this.readLength;
	}
	public String getReadType(){
		return this.readType;
	}
	public Integer getNumberOfLanes(){
		return this.numberOfLanes;
	}
	public Float getCostPerLane(){
		return this.costPerLane;
	}
	public String getError(){
		return this.error;
	}
	
	public void setMachine(String machine){
		this.machine = machine;
	}
	public void setReadLength(Integer readLength){
		this.readLength = readLength;
	}
	public void setReadType(String readType){
		this.readType = readType;
	}
	public void setNumberOfLanes(Integer numberOfLanes){
		this.numberOfLanes = numberOfLanes;
	}
	public void setCostPerLane(Float costPerLane){
		this.costPerLane = costPerLane;
	}
	public void setError(String error){
		this.error=error;
	}
}
