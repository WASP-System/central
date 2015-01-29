/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.helptag.service;

import java.util.List;

import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.service.WaspService;

/**
 * 
 */
public interface HelptagService extends WaspService {

	/**
	 * Perform Service
	 * 
	 * @return String
	 */
	public String performAction();

	public static final String HELPTAG_LIB_AREA = "helptagLibrary";

	public static final String RESTRICTION_ENZYME_META_KEY = "enzyme";

	public static final String JOB_ID_AS_STRING = "jobIdAsString";
	public static final String PREFIX_FOR_FILE_NAME = "prefixForFileName";
	public static final String HELPTAG_ANALYSIS_FILEGROUP_ID_AS_STRING = "helptagAnalysisFileGroupIdAsString";
	public static final String WORKING_DIRECTORY = "workingDirectory";
	public static final String RESULTS_DIRECTORY = "resultsDirectory";

	public List<SampleDraft> getAllMspISampleDraftsFromJobDraftId(Integer id);

	public List<SampleDraft> getAllHpaIISampleDraftsFromJobDraftId(Integer id);

	public boolean isHpaII(Integer sampleId);

	public boolean isMspI(Integer sampleId);

	public boolean confirmCellLibrariesAssociatedWithHcountFiles(List<SampleSource> cellLibraryList);

	public FileHandle createAndSaveInnerFileHandle(String fileName, FileType fileType);

	public FileGroup createAndSaveInnerFileGroup(FileHandle fileHandle, Software software, String description);

}
