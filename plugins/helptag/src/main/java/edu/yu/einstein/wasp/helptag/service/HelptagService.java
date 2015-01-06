/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.helptag.service;

import java.util.List;

import edu.yu.einstein.wasp.model.SampleDraft;
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

		public List<SampleDraft> getAllMspISampleDraftsFromJobDraftId(Integer id);

		public List<SampleDraft> getAllHpaIISampleDraftsFromJobDraftId(Integer id);
}
