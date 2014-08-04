package edu.yu.einstein.wasp.quote;

import edu.yu.einstein.wasp.model.ResourceCategory;

/**
 * 
 * @author dubin
 *
 */

public class SequencingCost {

	private ResourceCategory resourceCategory;//the sequencing machine; a resourceCategory with resourcetype of mps
	private String runType;
	private Integer readLength;
	private String readType;	
	private Integer numberOfLanes;
	private Float costPerLane;
	private String error;
	
	public SequencingCost(){		
		this.resourceCategory = new ResourceCategory();
		this.runType = "";
		this.readLength = new Integer(0);
		this.readType = "";
		this.numberOfLanes = new Integer(0);
		this.costPerLane = new Float(0);
		this.error = "";
	}
	public SequencingCost(ResourceCategory resourceCategory, String runType, Integer readLength, String readType, Integer numberOfLanes, Float costPerLane){
		
		this.resourceCategory = resourceCategory;
		this.runType = runType;
		this.readLength = readLength;
		this.readType = readType;		
		this.numberOfLanes = numberOfLanes;
		this.costPerLane = costPerLane;
		this.error = "";
	}
	public SequencingCost(ResourceCategory resourceCategory, String runType, Integer readLength, String readType, Integer numberOfLanes, Float costPerLane, String error){
		
		this.resourceCategory = resourceCategory;
		this.runType = runType;
		this.readLength = readLength;
		this.readType = readType;		
		this.numberOfLanes = numberOfLanes;
		this.costPerLane = costPerLane;
		this.error = error;
	}
	
	public ResourceCategory getResourceCategory(){
		return this.resourceCategory;
	}
	public String getRunType(){
		return this.runType;
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
	
	public void setResourceCategory(ResourceCategory resourceCategory){
		this.resourceCategory = resourceCategory;
	}
	public void setRunType(String runType){
		this.runType = runType;
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
