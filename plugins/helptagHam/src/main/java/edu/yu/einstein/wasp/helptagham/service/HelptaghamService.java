/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.helptagham.service;

import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.service.WaspService;

/**
 * 
 */
public interface HelptaghamService extends WaspService {

	/**
	 * Perform Service
	 * 
	 * @return String
	 */
	public String performAction();

	public static final String JOB_ID_AS_STRING = "jobIdAsString";
	public static final String PREFIX_FOR_FILE_NAME = "prefixForFileName";
	public static final String HELPTAGHAM_ANALYSIS_FILEGROUP_ID_AS_STRING = "helptagHAMAnalysisFileGroupIdAsString";
	public static final String WORKING_DIRECTORY = "workingDirectory";
	public static final String RESULTS_DIRECTORY = "resultsDirectory";

	public FileHandle createAndSaveInnerFileHandle(String fileName, FileType fileType);

	public FileGroup createAndSaveInnerFileGroup(FileHandle fileHandle, Software software, String description);
}
