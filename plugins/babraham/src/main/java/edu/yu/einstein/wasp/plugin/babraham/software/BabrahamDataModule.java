package edu.yu.einstein.wasp.plugin.babraham.software;

import java.util.ArrayList;
import java.util.List;

public class BabrahamDataModule{
	
	protected String name;
	
	protected String iname;
	
	protected List<String> attributes;
	
	protected List<List<String>> dataPoints;
	
	public BabrahamDataModule() {
		this.attributes = new ArrayList<String>();
		this.dataPoints = new ArrayList<List<String>>();
	}
	
	public List<String> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}
	
	public List<List<String>> getDataPoints() {
		return dataPoints;
	}
	
	public void setDataPoints(List<List<String>> dataPoints) {
		this.dataPoints = dataPoints;
	}

	public String getIName() {
		return iname;
	}

	public void setIName(String iname) {
		this.iname = iname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
