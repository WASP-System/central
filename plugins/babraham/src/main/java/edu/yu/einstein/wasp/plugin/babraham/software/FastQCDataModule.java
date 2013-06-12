package edu.yu.einstein.wasp.plugin.babraham.software;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class FastQCDataModule{
	
	private String name;
	
	private String iname;
	
	private String result;
	
	private Set<String> attributes;
	
	private Map<String, Double> keyValueData;
	
	private Set<Map<String, Double>> dataPoints;
	
	public FastQCDataModule() {
		this.attributes = new LinkedHashSet<String>();
		this.dataPoints = new LinkedHashSet<Map<String,Double>>();
		this.keyValueData = new LinkedHashMap<String, Double>();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
		setINameFromName();
	}
	
	public Map<String, Double> getKeyValueData() {
		return keyValueData;
	}
	
	public void setKeyValueData(String key, Double value) {
		this.keyValueData.put(key, value);
	}
	
	public String getIName() {
		return iname;
	}
	
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public Set<String> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(Set<String> attributes) {
		this.attributes = attributes;
	}
	
	public Set<Map<String, Double>> getDataPoints() {
		return dataPoints;
	}
	
	public void setDataPoints(Set<Map<String, Double>> dataPoints) {
		this.dataPoints = dataPoints;
	}

	
	private void setINameFromName(){
		this.iname = getModuleINameFromName(this.name);
	}
	
	public static String getModuleINameFromName(String name){
		if (name == null)
			return null;
		if (name.equals("Basic Statistics"))
			return FastQC.PlotType.BASIC_STATISTICS;
		if (name.equals("Per base sequence quality"))
			return FastQC.PlotType.PER_BASE_QUALITY;
		if (name.equals("Per sequence quality scores"))
			return FastQC.PlotType.PER_SEQUENCE_QUALITY;
		if (name.equals("Per base sequence content"))
			return FastQC.PlotType.PER_BASE_SEQUENCE_CONTENT;
		if (name.equals("Per base GC content"))
			return FastQC.PlotType.PER_BASE_GC_CONTENT;
		if (name.equals("Per sequence GC content"))
			return FastQC.PlotType.PER_SEQUENCE_GC_CONTENT;
		if (name.equals("Per base N content"))
			return FastQC.PlotType.PER_BASE_N_CONTENT;
		if (name.equals("Sequence Length Distribution"))
			return FastQC.PlotType.SEQUENCE_LENGTH_DISTRIBUTION;
		if (name.equals("Sequence Duplication Levels"))
			return FastQC.PlotType.DUPLICATION_LEVELS;
		if (name.equals("Overrepresented sequences"))
			return FastQC.PlotType.KMER_PROFILES;
		return null;
	}
}
