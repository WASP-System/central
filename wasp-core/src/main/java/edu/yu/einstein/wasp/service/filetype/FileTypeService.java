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
	
	public static final String FILETYPE_IS_SINGLE_META_KEY = "isSingleton";
	public static final String FILETYPE_FILE_NUMBER_META_KEY = "fileNumber";
	public static final String FILETYPE_AREA = "filetype";
	
	/**
	 * Is this a single file (or one of a series)?
	 * 
	 * @param file
	 * @return
	 */
	public boolean isSingleFile(File file);
	
	/**
	 * get the number of the file in the series.  Always 0-based and 0 or null if there is only one file.
	 * In the case where multiple files comprise a component of a single file (e.g. separate FASTQ files
	 * for forward and reverse reads in an Illumina run), each of these files will have the same "file number"
	 * and must be discriminated by an additional piece of metadata supplied by the file specific service. (e.g.
	 * FastqService.READ_SEGMENT_NUMBER_META_KEY). 
	 * 
	 * @param file
	 * @return integer or null
	 */
	public Integer getFileNumber(File file);

}
