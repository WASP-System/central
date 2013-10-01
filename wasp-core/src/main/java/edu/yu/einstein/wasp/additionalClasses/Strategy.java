package edu.yu.einstein.wasp.additionalClasses;

import edu.yu.einstein.wasp.model.WaspModel;

//see table 1 in http://www.ncbi.nlm.nih.gov/books/NBK47529/ for list of strategies

public class Strategy extends WaspModel {

	public static final String KEY_PREFIX = "sra.strategy.";
	public static final String SEPARATOR = "::";	
	
	private String sraStrategy;
	private String displayStrategy;
	private String description;	
	private String available;	
	
	public Strategy(){}
	
	public Strategy(String sraStrategy, String displayStrategy, String description, String available){
		setSraStrategy(sraStrategy);
		setDisplayStrategy(displayStrategy);
		setDescription(description);
		setAvailable(available);
	}
	
	public void setSraStrategy(String sraStrategy){ this.sraStrategy = sraStrategy; }
	public void setDisplayStrategy(String displayStrategy){ this.displayStrategy = displayStrategy; }
	public void setDescription(String description){ this.description = description; }
	public void setAvailable(String available){ this.available = available; }

	public String getSraStrategy(){ return this.sraStrategy; }
	public String getDisplayStrategy(){ return this.displayStrategy; }
	public String getDescription(){ return this.description; }
	public String getAvailable(){ return this.available; }

	public boolean isAvailable(){ 
		if(available.equalsIgnoreCase("true")) 
			return true; 
		else
			return false; 
	}

}
