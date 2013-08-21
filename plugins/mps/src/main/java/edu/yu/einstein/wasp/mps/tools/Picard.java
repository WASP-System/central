/**
 * 
 */
package edu.yu.einstein.wasp.mps.tools;

import edu.yu.einstein.wasp.software.alignment.BAMProcessor;

/**
 * @author calder
 *
 */
public class Picard extends BAMProcessor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2501043398244995595L;
	
	public Picard() {
		setSoftwareVersion("1.78"); // this default may be overridden in wasp.site.properties
	}

}
