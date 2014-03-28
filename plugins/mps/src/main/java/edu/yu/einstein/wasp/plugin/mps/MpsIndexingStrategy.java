package edu.yu.einstein.wasp.plugin.mps;

import edu.yu.einstein.wasp.interfacing.IndexingStrategy;

public class MpsIndexingStrategy extends IndexingStrategy {
	
	public static final IndexingStrategy FIVE_PRIME = new IndexingStrategy("FIVE_PRIME");
	
	public MpsIndexingStrategy(String strategy) {
		super(strategy);
	}
	
	
}
