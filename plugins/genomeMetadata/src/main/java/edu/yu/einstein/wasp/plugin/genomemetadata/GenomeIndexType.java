/**
 * 
 */
package edu.yu.einstein.wasp.plugin.genomemetadata;

import java.io.Serializable;

/**
 * @author calder
 *
 */
public class GenomeIndexType implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7982188289875376831L;
	
	public static final GenomeIndexType FASTA = new GenomeIndexType("fasta"); 
	public static final GenomeIndexType GTF = new GenomeIndexType("gtf");
	public static final GenomeIndexType VCF = new GenomeIndexType("vcf");
	
	private String indexType = "NOT SET";
	
	public GenomeIndexType() {
		
	}
	
	public GenomeIndexType(String type){
		this.indexType = type;
	}
	
	@Override
	public String toString(){
		return indexType;
	}
	
	@Override
	public boolean equals(Object obj){
		if (this == obj) return true;
		if (obj == null) return false;
		if (!this.getClass().isInstance(obj) && !obj.getClass().isInstance(this)) 
			return false; // allow comparison if one class is derived from the other
		return indexType.equals(obj.toString());
	}
	
	@Override
	public int hashCode(){
		return indexType.hashCode();
	}

}
