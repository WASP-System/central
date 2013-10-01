package edu.yu.einstein.wasp.additionalClasses;

import edu.yu.einstein.wasp.model.WaspModel;

public class Strategy extends WaspModel {

	public static final String KEY_PREFIX = "sra.strategy.";
	
	private String iName;//for example wgs
	private String name ;
	private String description;	
	
	public Strategy(){}
	public Strategy(String iName, String name, String description){
		this.setIName(iName);
		this.setName(name);
		this.setDescription(description);
	}
	public void setIName(String iName){
		this.iName = iName;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setDescription(String description){
		this.description = description;
	}
	public String getIName(){return this.iName;}
	public String getName(){return this.name;}
	public String getDescription(){return this.description;}
	
	public String getTaggedIName(){return Strategy.KEY_PREFIX+getIName();}
}
