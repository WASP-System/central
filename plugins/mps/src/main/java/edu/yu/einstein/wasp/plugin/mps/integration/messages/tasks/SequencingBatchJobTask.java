package edu.yu.einstein.wasp.plugin.mps.integration.messages.tasks;

import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;

/**
 * 
 * @author asmclellan
 *
 */
public class SequencingBatchJobTask extends BatchJobTask {

	public static final String ALIGNMENT = "alignment";
	
	public static final String PEAK_CALLING = "peakCalling";
	
	public static final String QC = "qc";
	
	public static final String DEMULTIPLEXING = "demultiplexing";
	
	public static final String SNP_CALLING = "SnpCalling";
	
	public static final String ADAPTER_TRIMMING = "AdapterTrimming";
	
	public static final String VARIANT_DATA_PREPROCESS = "VariantPreprocess";
	
	public static final String VARIANT_DISCOVERY = "VariantDiscovery";
	
	public static final String VARIANT_ANALYSIS = "VariantAnalysis";

}
