/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.rnaseq.service;

import edu.yu.einstein.wasp.exception.SampleSubtypeException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.service.WaspService;

/**
 * 
 */
public interface RnaseqService extends WaspService {

		public static final String RNASEQ_RNA_AREA = "rnaseqRna";
		public static final String RNASEQ_CDNA_AREA = "rnaseqCDna";
		public static final String RNASEQ_LIBRARY_AREA = "rnaseqLibrary";

		public static final String FRACTION_META_KEY = "fraction";

		public static final String DIRECTIONALITY_META_KEY = "directionality";

		public static final String RIBOSOME_DEPLETION_META_KEY = "ribosomeDepletion";

		/**
		 * Perform Service
		 * @return String
		 */
		public String performAction();

		public String getRNAFraction(Sample sample);
		public String getRibosomeDepletionMethod(Sample sample);
		public String getDirectionality(Sample sample);
		public boolean isRNALibraryDirectional(Sample sample) throws SampleSubtypeException;
}
