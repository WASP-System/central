package edu.yu.einstein.wasp.plugin;

public interface BatchJobProviding {
	
	/**
	 * get batch job name given a batchJobType
	 * @param area
	 * @param BatchJobType
	 * @return
	 */
	public String getBatchJobName(String BatchJobType);
	
	/**
	 * get batch job name given a resource category and batchJobType
	 * @param area
	 * @param BatchJobType
	 * @return
	 */
	public String getBatchJobNameByArea(String BatchJobType, String area);

}
