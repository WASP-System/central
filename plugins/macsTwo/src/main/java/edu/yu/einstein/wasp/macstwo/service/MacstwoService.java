/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.macstwo.service;

import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.service.WaspService;

/**
 * 
 */
public interface MacstwoService extends WaspService {

	public static final String NAME_OF_FILE_WHOSE_CREATION_MUST_BE_CONFIRMED = "nameOfFileWhoseCreationMustBeConfirmed";
	
	public static final String IS_MODEL_FILE_CREATED = "modelFileCreated";
	
	public static final String JOB_ID_AS_STRING = "jobIdAsString";
	public static final String PREFIX_FOR_FILE_NAME = "prefixForFileName";
	public static final String MACSTWO_ANALYSIS_FILEGROUP_ID_AS_STRING = "macs2AnalysisFileGroupIdAsString";
	public static final String WORKING_DIRECTORY = "workingDirectory";
	public static final String RESULTS_DIRECTORY = "resultsDirectory";
	
		/**
		 * Perform Service
		 * @return String
		 */
		public String performAction();
		
		public FileHandle createAndSaveInnerFileHandle(String fileName, FileType fileType);
		
		public FileGroup createAndSaveInnerFileGroup(FileHandle fileHandle, Software software, String description);

}
