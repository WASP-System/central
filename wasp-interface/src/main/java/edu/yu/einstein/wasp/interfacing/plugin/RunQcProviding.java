package edu.yu.einstein.wasp.interfacing.plugin;

import java.util.List;

public interface RunQcProviding extends BatchJobProviding {
	
	/**
	 * get name of run QC step 
	 * @return
	 */
	public List<String> getRunQcStepName();

}
