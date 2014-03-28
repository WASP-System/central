package edu.yu.einstein.wasp;

import edu.yu.einstein.wasp.model.WaspModel;

//see table 1 in http://www.ncbi.nlm.nih.gov/books/NBK47529/ for list of library strategies
//also http://www.ebi.ac.uk/ena/about/sra_library_strategy and http://www.ebi.ac.uk/ena/about/sra_format for library strategy
//dubin; 10-1-13

public class Strategy extends WaspModel {
	
	public static class StrategyType{
		public static final String LIBRARY_STRATEGY = "libraryStrategy";
	}

	private static final long serialVersionUID = -4413474836491771971L;

	public static final String SEPARATOR = "::";	
	
	private String type;
	private String strategy;
	private String displayStrategy;
	private String description;	
	private String available;
	private String sraCompatible;
	private Integer id;//id is stolen from meta.getId()
	
	public Strategy(){}
	
	
	public Strategy(Integer id, String type, String strategy, String displayStrategy, String description, String available, String sraCompatible){
		setType(type);
		setStrategy(strategy);
		setDisplayStrategy(displayStrategy);
		setDescription(description);
		setAvailable(available);
		setSraCompatible(sraCompatible);
		
		setId(id);
	}

	public void setType(String type){ this.type = type; }
	public void setStrategy(String strategy){ this.strategy = strategy; }
	public void setDisplayStrategy(String displayStrategy){ this.displayStrategy = displayStrategy; }
	public void setDescription(String description){ this.description = description; }
	public void setAvailable(String available){ this.available = available; }
	public void setSraCompatible(String sraCompatible){ this.sraCompatible = sraCompatible; }
	
	public void setId(Integer id){ this.id = id; }
	
	public String getType(){ return this.type; }
	public String getStrategy(){ return this.strategy; }
	public String getDisplayStrategy(){ return this.displayStrategy; }
	public String getDescription(){ return this.description; }
	public String getAvailable(){ return this.available; }
	public String getSraCompatible(){ return this.sraCompatible; }
	
	public Integer getId(){ return this.id; }
	
	public boolean isAvailable(){ 
		if(available.equalsIgnoreCase("true")) 
			return true; 
		else
			return false; 
	}
	
	public boolean isSraCompatible(){ 
		if(this.sraCompatible.equalsIgnoreCase("true")) 
			return true; 
		else
			return false; 
	}

}
