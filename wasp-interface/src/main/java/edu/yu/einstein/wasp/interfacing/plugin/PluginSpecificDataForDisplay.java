package edu.yu.einstein.wasp.interfacing.plugin;

import java.util.Map;

public interface PluginSpecificDataForDisplay extends WebInterfacing {
	
	public Map<String,String> getPlugInSpecificSampleDataForDisplay(Integer jobId, Integer sampleId);

}