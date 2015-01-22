/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.helptag.service;

import java.util.List;

import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleDraftMeta;
import edu.yu.einstein.wasp.service.WaspService;

/**
 * 
 */
public interface HelptagService extends WaspService {

		/**
		 * Perform Service
		 * @return String
		 */
		public String performAction();

		public static final String HELPTAG_LIB_AREA = "helptagLibrary";
		public static final String HELPTAG_DNA_AREA = "helptagDNA";
		
		public static final String RESTRICTION_ENZYME_META_KEY = "enzyme";
		public static final String TYPE_OF_HELP_LIBRARY_REQUESTED_META_KEY = "typeOfHelpLibraryRequested";
		
		public List<SampleDraft> getAllMspISampleDraftsFromJobDraftId(Integer id);

		public List<SampleDraft> getAllHpaIISampleDraftsFromJobDraftId(Integer id);
		public List<SampleDraft> createNewHelpDNASampleDrafts(SampleDraft sampleDraft, List<String> librariesToCreateList);
		public List<SampleDraft> getAllHpaIIAndbetaGTMspISampleDraftsFromJobDraftId(Integer id);
		public List<SampleDraft> getAllbetaGTMspISampleDraftsFromJobDraftId(Integer id);
		
		public String getTypeOfHelpLibraryRequestedForMacromolecule(Sample sample);
		public String getTypeOfHelpLibrary(Sample sample);
		public boolean isBetaGTMspI(Sample s);
		public boolean isHpaII(Sample s);
		
		//1-21-15
		public List<String> getTypeOfHelpLibrariesRequestedList(List<SampleDraftMeta> sampleDraftMetaList);
		
}
