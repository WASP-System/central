package edu.yu.einstein.wasp.quote;

import edu.yu.einstein.wasp.model.Sample;

public class LibraryCost {

	private Integer sampleId;//a sample submitted to the job (macromolecule or library)
	private String sampleName;
	private String material;
	private String libraryCost;//cost could be 0.00 if the facility manager so deems it; cost could be N/A if sample is already a library; cost could be Withdrawn if sample is withdrawn
	private String error;
	
	public LibraryCost(Integer sampleId, String sampleName, String material, String libraryCost, String error){
		this.sampleId = sampleId;
		this.sampleName = sampleName;
		this.material = material;
		this.libraryCost = libraryCost;
		this.error = error;		
	}
	public LibraryCost(Integer sampleId, String sampleName, String material, String libraryCost){
		this.sampleId = sampleId;
		this.sampleName = sampleName;
		this.material = material;
		this.libraryCost = libraryCost;
		this.error = "";		
	}
	
	public void setSampleId(Integer sampleId){this.sampleId = sampleId;}
	public void setSampleName(String sampleName){this.sampleName = sampleName;}
	public void setMaterial(String material){this.material = material;}
	public void setLibraryCost(String libraryCost){this.libraryCost = libraryCost;}
	public void setError(String error){this.error = error;}
	
	public Integer getSampleId(){return this.sampleId;}
	public String getSampleName(){return this.sampleName;}
	public String getMaterial(){return this.material;}
	public String getLibraryCost(){return this.libraryCost;}
	public String getError(){return this.error;}
}
