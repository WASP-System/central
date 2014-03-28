package edu.yu.einstein.wasp.interfacing.plugin;

public interface RunQcProviding extends BatchJobProviding {
	
	/**
	 * get name of run QC step 
	 * @return
	 */
	public String getRunQcStepName();

}
