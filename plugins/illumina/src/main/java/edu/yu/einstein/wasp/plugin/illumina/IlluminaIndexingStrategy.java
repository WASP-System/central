package edu.yu.einstein.wasp.plugin.illumina;

import edu.yu.einstein.wasp.interfacing.IndexingStrategy;

public class IlluminaIndexingStrategy extends IndexingStrategy {
	
	public static final String INDEXING_KEY = "indexingStrategy";
	
	public static final IndexingStrategy TRUSEQ = new IndexingStrategy("TRUSEQ");
	
	public static final IndexingStrategy TRUSEQ_DUAL = new IndexingStrategy("TRUSEQ_DUAL");
	
	public IlluminaIndexingStrategy(String strategy) {
		super(strategy);
	}
	
	
}
