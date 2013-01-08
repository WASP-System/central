/**
 * 
 */
package edu.yu.einstein.wasp.service.filetype;

import edu.yu.einstein.wasp.model.File;

/**
 * @author calder
 *
 */
public interface FileTypeService {
	
	/**
	 * Is this a single file (or one of a series)?
	 * 
	 * @param file
	 * @return
	 */
	public boolean isSingleFile(File file);
	
	/**
	 * get the number of the file in the series.  Always 0-based and null if there is only one file.
	 * 
	 * @param file
	 * @return integer or null
	 */
	public Integer getFileNumber(File file);

}
