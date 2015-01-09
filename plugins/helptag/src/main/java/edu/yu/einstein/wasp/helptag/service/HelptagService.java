/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.helptag.service;

import java.util.List;

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
		public static final String LIBRARY_TO_CREATE_META_KEY = "libraryToCreate";

		public List<SampleDraft> getAllMspISampleDraftsFromJobDraftId(Integer id);

		public List<SampleDraft> getAllHpaIISampleDraftsFromJobDraftId(Integer id);
		public List<String> getLibrariesToCreateList(List<SampleDraftMeta> sampleDraftMetaList);
		public List<SampleDraft> createNewHelpDNASampleDrafts(SampleDraft sampleDraft, List<String> librariesToCreateList);
		public List<SampleDraft> getAllHpaIIAndbetaGTMspISampleDraftsFromJobDraftId(Integer id);
		
}
