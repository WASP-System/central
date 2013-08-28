package edu.yu.einstein.wasp.quote;

/**
 * 
 * @author dubin
 *
 */
public class SubmittedSample {
	
	private Integer number;
	private Integer sampleId;
	private String sampleName;
	private String material;
	private String cost;
	private String error;
	
	public SubmittedSample(Integer number, Integer sampleId, String sampleName, String material, String cost){
		this.number = number;
		this.sampleId = sampleId;
		this.sampleName = sampleName;
		this.material = material;
		this.cost = cost;
		this.error = "";
	}
	
	public SubmittedSample(Integer number, Integer sampleId, String sampleName, String material, String cost, String error){
		this.number = number;
		this.sampleId = sampleId;
		this.sampleName = sampleName;
		this.material = material;
		this.cost = cost;
		this.error = error;
	}

	public Integer getNumber(){
		return this.number;
	}
	public Integer getSampleId(){
		return this.sampleId;
	}
	public String getSampleName(){
		return this.sampleName;
	}
	public String getMaterial(){
		return this.material;
	}
	public String getCost(){
		return this.cost;
	}
	public String getError(){
		return this.error;
	}
	public void setNumber(Integer number){
		this.number = number;
	}
	public void setSampleId(Integer sampleId){
		this.sampleId = sampleId;
	}
	public void setSampleName(String sampleName){
		this.sampleName = sampleName;
	}
	public void setMaterial(String material){
		this.material = material;
	}
	public void setCost(String cost){
		this.cost = cost;
	}
	public void setError(String error){
		this.error = error;
	}
}
