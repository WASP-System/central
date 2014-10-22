/**
 * 
 */
package edu.yu.einstein.wasp.plugin.genomemetadata;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 
 * @author calder
 * @param <K>
 *
 */
public abstract class GenomeIndexConfiguration<K,V> extends HashMap<K,V> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7582849230555994827L;

	/**
	 * This must return a unique string that identifies the particular version of the genome index build.
	 * 
	 * @return
	 */
	public abstract String generateUniqueKey();

}
