package edu.yu.einstein.wasp.plugin;

public interface BatchJobProviding extends WaspPluginI{
	
	/**
	 * get batch job name given a batchJobType
	 * @param area
	 * @param BatchJobType
	 * @return
	 */
	public String getBatchJobName(String batchJobType);

}
