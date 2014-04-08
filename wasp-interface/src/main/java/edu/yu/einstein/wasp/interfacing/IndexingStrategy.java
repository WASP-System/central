package edu.yu.einstein.wasp.interfacing;

public class IndexingStrategy {
	
	private String strategy = "";
	
	public static final IndexingStrategy UNKNOWN = new IndexingStrategy("UNKNOWN");
	
	public IndexingStrategy(String strategy){
		this.strategy = strategy;
	}
	
	@Override
	public String toString(){
		return strategy;
	}
	
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
}
