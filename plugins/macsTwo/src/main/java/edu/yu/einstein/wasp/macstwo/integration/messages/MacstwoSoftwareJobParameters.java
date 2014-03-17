package edu.yu.einstein.wasp.macstwo.integration.messages;

import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;

public class MacstwoSoftwareJobParameters extends WaspSoftwareJobParameters{

	public static final String MODEL_SCRIPT_FILEGROUP_ID = "modelScriptGId"; // FileGroupId (as string) for the model.r file
	public static final String TEST_SAMPLE_ID = "testSampleId"; 
	public static final String CONTROL_SAMPLE_ID = "controlSampleId"; //could be zero (means no control used)

}
