package edu.yu.einstein.wasp.chipseq.integration.messages;

import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;

public class ChipSeqSoftwareJobParameters extends WaspSoftwareJobParameters{

	public static final String TEST_LIBRARY_CELL_ID_LIST = "testLibraryCellIdList"; //cellLibrary list for a given test sample
	public static final String CONTROL_LIBRARY_CELL_ID_LIST = "controlLibraryCellIdList"; //cellLibrary list for a given control sample
	public static final String PEAK_TYPE = "peakType"; //peaktype (sample metadata)

}
