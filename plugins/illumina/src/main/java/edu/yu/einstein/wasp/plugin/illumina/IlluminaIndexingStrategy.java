package edu.yu.einstein.wasp.plugin.illumina;

import edu.yu.einstein.wasp.interfacing.IndexingStrategy;

public class IlluminaIndexingStrategy extends IndexingStrategy {
	
	public static final IndexingStrategy TRUSEQ = new IndexingStrategy("TRUSEQ");
	
	public IlluminaIndexingStrategy(String strategy) {
		super(strategy);
	}
	
	
}
