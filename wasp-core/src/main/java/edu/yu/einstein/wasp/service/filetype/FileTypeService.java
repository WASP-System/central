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
	
	public boolean isSingleFile(File file);
	
	public Integer getFileNumber(File file);

}
