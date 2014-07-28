package edu.yu.einstein.wasp.interfacing;

public class IndexingStrategy {
	
	private String strategy = "";
	
	public static final IndexingStrategy UNKNOWN = new IndexingStrategy("UNKNOWN");
	
	public IndexingStrategy(String strategy){
		this.strategy = strategy;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString(){
		return strategy;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o){
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (o.toString().equals(strategy))
			return true;
		return false;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return strategy.hashCode();
	}
	
	
}
