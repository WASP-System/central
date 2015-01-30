/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.helptag.service;

import java.util.List;

import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleDraftMeta;
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
	public static final String HELPTAG_DNA_AREA = "helptagDNA";

	public static final String RESTRICTION_ENZYME_META_KEY = "enzyme";
	public static final String TYPE_OF_HELP_LIBRARY_REQUESTED_META_KEY = "typeOfHelpLibraryRequested";

	public boolean isMspI(Sample s);
	public boolean isBetaGTMspI(Sample s);
	public boolean isHpaII(Sample s);

	public boolean confirmCellLibrariesAssociatedWithHcountFiles(List<SampleSource> cellLibraryList);

	public FileHandle createAndSaveInnerFileHandle(String fileName, FileType fileType);
	public FileGroup createAndSaveInnerFileGroup(FileHandle fileHandle, Software software, String description);

	public List<SampleDraft> createNewHelpDNASampleDrafts(SampleDraft sampleDraft, List<String> librariesToCreateList);

	public List<SampleDraft> getAllMspISampleDraftsFromJobDraftId(Integer id);
	public List<SampleDraft> getAllHpaIIAndbetaGTMspISampleDraftsFromJobDraftId(Integer id);
	public List<SampleDraft> getAllbetaGTMspISampleDraftsFromJobDraftId(Integer id);


	public String getTypeOfHelpLibraryRequestedForMacromolecule(Sample sample);
	public String getTypeOfHelpLibrary(Sample sample);
	public List<String> getTypeOfHelpLibrariesRequestedList(List<SampleDraftMeta> sampleDraftMetaList);
		
}
