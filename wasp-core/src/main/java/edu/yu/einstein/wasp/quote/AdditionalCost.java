package edu.yu.einstein.wasp.quote;

/**
 * 
 * @author dubin
 *
 */

public class AdditionalCost {

	private String reason;
	private Integer numberOfUnits;
	private Float costPerUnit;
	private String error;
	
	public AdditionalCost(){
		this.reason="";
		this.numberOfUnits = new Integer(0);
		this.costPerUnit = new Float(0);
		this.error= "";
	}
	public AdditionalCost(String reason, Integer numberOfUnits, Float costPerUnit){
		this.reason=reason;
		this.numberOfUnits = numberOfUnits;
		this.costPerUnit = costPerUnit;
		this.error= "";
	}
	public AdditionalCost(String reason, Integer numberOfUnits, Float costPerUnit, String error){
		this.reason=reason;
		this.numberOfUnits = numberOfUnits;
		this.costPerUnit = costPerUnit;
		this.error= error;
	}
	
	public String getReason(){
		return this.reason;
	}
	public Integer getNumberOfUnits(){
		return this.numberOfUnits;
	}
	public Float getCostPerUnit(){
		return this.costPerUnit;
	}
	public String getError(){
		return this.error;
	}
	public void setReason(String reason){
		this.reason = reason;
	}
	public void setNumberOfUnits(Integer numberOfUnits){
		this.numberOfUnits = numberOfUnits;
	}
	public void setCostPerUnit(Float costPerUnit){
		this.costPerUnit = costPerUnit;
	}
	public void setError(String error){
		this.error = error;
	}
}
