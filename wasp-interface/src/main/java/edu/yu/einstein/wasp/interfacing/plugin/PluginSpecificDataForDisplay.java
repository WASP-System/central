package edu.yu.einstein.wasp.interfacing.plugin;

import java.util.Map;

import org.springframework.ui.ModelMap;

public interface PluginSpecificDataForDisplay extends WebInterfacing {
	
	public Map<String,String> getPlugInSpecificSampleDataForDisplay(Integer jobId, Integer sampleId);
	public void getPlugInSpecificSamplePairDataForDisplay(Integer jobId, ModelMap m);
}