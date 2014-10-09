package edu.yu.einstein.wasp.quote;


public class LibraryCost {

	private Integer sampleId;//a sample submitted to the job (macromolecule or library)
	private String sampleName;
	private String material;
	private String reasonForNoLibraryCost;//could be N/A if not a library; could be withdrawn
	private Float libraryCost;//cost could be 0.00 if the facility manager so deems it; cost could be N/A if sample is already a library; cost could be Withdrawn if sample is withdrawn
	private Float analysisCost;
	private String error;
	
	LibraryCost(){
		this.sampleId = 0;
		this.sampleName = "";
		this.material = "";
		this.reasonForNoLibraryCost = "";
		this.libraryCost = 0f;
		this.analysisCost = 0f;
		this.error = "";		
	}
	public LibraryCost(Integer sampleId, String sampleName, String material, String reasonForNoLibraryCost, Float libraryCost, Float analysisCost, String error){
		this.sampleId = sampleId;
		this.sampleName = sampleName;
		this.material = material;
		this.reasonForNoLibraryCost = reasonForNoLibraryCost;
		this.libraryCost = libraryCost;
		this.analysisCost = analysisCost;
		this.error = error;		
	}
	public LibraryCost(Integer sampleId, String sampleName, String material, String reasonForNoLibraryCost, Float libraryCost, Float analysisCost){
		this.sampleId = sampleId;
		this.sampleName = sampleName;
		this.material = material;
		this.reasonForNoLibraryCost = reasonForNoLibraryCost;
		this.libraryCost = libraryCost;
		this.analysisCost = analysisCost;
		this.error = "";		
	}
	
	public void setSampleId(Integer sampleId){this.sampleId = sampleId;}
	public void setSampleName(String sampleName){this.sampleName = sampleName;}
	public void setMaterial(String material){this.material = material;}
	public void setReasonForNoLibraryCost(String reasonForNoLibraryCost){this.reasonForNoLibraryCost = reasonForNoLibraryCost; }
	public void setLibraryCost(Float libraryCost){this.libraryCost = libraryCost;}
	public void setAnalysisCost(Float analysisCost){this.analysisCost = analysisCost;}
	public void setError(String error){this.error = error;}
	
	public Integer getSampleId(){return this.sampleId;}
	public String getSampleName(){return this.sampleName;}
	public String getMaterial(){return this.material;}
	public String getReasonForNoLibraryCost(){return this.reasonForNoLibraryCost;}
	public Float getLibraryCost(){return this.libraryCost;}
	public Float getAnalysisCost(){return this.analysisCost;}
	public String getError(){return this.error;}
}
