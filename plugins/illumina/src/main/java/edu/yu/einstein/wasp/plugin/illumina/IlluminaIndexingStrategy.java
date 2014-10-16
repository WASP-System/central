package edu.yu.einstein.wasp.plugin.illumina;

import edu.yu.einstein.wasp.interfacing.IndexingStrategy;
import edu.yu.einstein.wasp.plugin.mps.MpsIndexingStrategy;

public class IlluminaIndexingStrategy extends MpsIndexingStrategy {
	
	public static final String INDEXING_KEY = "indexingStrategy";
	
	public static final IndexingStrategy TRUSEQ = new IndexingStrategy("TRUSEQ");
	
	public static final IndexingStrategy TRUSEQ_DUAL = new IndexingStrategy("TRUSEQ_DUAL");
	
	public IlluminaIndexingStrategy(String strategy) {
		super(strategy);
	}
	
	
}
