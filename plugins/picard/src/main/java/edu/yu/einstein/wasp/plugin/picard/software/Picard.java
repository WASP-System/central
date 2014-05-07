/**
 * Created by Wasp System Eclipse Plugin
 * @author
 */
package edu.yu.einstein.wasp.plugin.picard.software;
import java.util.Set;

import edu.yu.einstein.wasp.software.SoftwarePackage;


/**
 * @author asmclellan
 */
public class Picard extends SoftwarePackage{

	private static final long serialVersionUID = 6817018170220888568L;
	
	public final static int MEMORY_REQUIRED_4 = 4; // in Gb

	public Picard() {}
	
	/**
	 * Gets command for running picard MarkDuplicates.
	 * @param inputBamFilename
	 * @param dedupBamFilename
	 * @param dedupBaiFilename (optional: may be null)
	 * @param dedupMetricsFilename
	 * @return
	 */
	public String getMarkDuplicatesCmd(String inputBamFilename, String dedupBamFilename, String dedupBaiFilename, String dedupMetricsFilename){
		boolean createIndex = false;
		if (dedupBaiFilename != null)
			createIndex = true;
		String command = "java -Xmx" + MEMORY_REQUIRED_4 + "g -jar $PICARD_ROOT/MarkDuplicates.jar I=" + inputBamFilename + " O=" + dedupBamFilename +
				" REMOVE_DUPLICATES=false METRICS_FILE=" + dedupMetricsFilename + 
				" TMP_DIR=. CREATE_INDEX=" + createIndex + " VALIDATION_STRINGENCY=SILENT";
		if (createIndex)
			 command += " && mv " + dedupBamFilename + ".bai " + dedupBaiFilename;
		logger.debug("Will conduct picard MarkDuplicates with command: " + command);
		return command;
	}
	
	/**
	 * Indexes given bam file
	 * @param bamFilename
	 * @param baiFilename
	 * @return
	 */
	public String getIndexBamCmd(String bamFilename, String baiFilename){
		String command = "java -Xmx" + MEMORY_REQUIRED_4 + "g -jar $PICARD_ROOT/BuildBamIndex.jar I=" + bamFilename + " O=" + baiFilename + 
				" TMP_DIR=. VALIDATION_STRINGENCY=SILENT";
		logger.debug("Will conduct picard indexing of bam file with command: " + command);
		return command;
	}	
	
	/**
	 * merges given bam files. 
	 * @param inputBamFilenames
	 * @param mergedBamFilename
	 * @param mergedBaiFilename (optional: may be null)
	 * @return
	 */
	public String getMergeBamCmd(Set<String> inputBamFilenames, String mergedBamFilename, String mergedBaiFilename) {
		boolean createIndex = false;
		if (mergedBaiFilename != null)
			createIndex = true;
		String command = "java -Xmx" + MEMORY_REQUIRED_4 + "g -jar $PICARD_ROOT/MergeSamFiles.jar";
		for (String fileName : inputBamFilenames)
			command += " I=" + fileName;
		command += " O=" + mergedBamFilename + " SO=coordinate TMP_DIR=. CREATE_INDEX=" + createIndex + " VALIDATION_STRINGENCY=SILENT";
		if (createIndex)
			 command += " && mv " + mergedBamFilename + ".bai " + mergedBaiFilename;
		logger.debug("Will conduct picard MergeSamFiles with command: " + command);
		return command;
	}
	
	/**
	 * Merge filenames represented as a glob e.g. '*' or '*.bam' or '*.sam' (location is relative to the scratch directory).
	 * @param inputBamFilenamesGlob
	 * @param mergedBamFilename
	 * @param mergedBaiFilename (optional: may be null)
	 * @return
	 */
	public String getMergeBamCmd(String inputBamFilenamesGlob, String mergedBamFilename, String mergedBaiFilename) {
		boolean createIndex = false;
		if (mergedBaiFilename != null)
			createIndex = true;
		String command = "java -Xmx" + MEMORY_REQUIRED_4 + "g -jar $PICARD_ROOT/MergeSamFiles.jar $(printf 'I=%s ' " + inputBamFilenamesGlob + ")" + 
		" O=" + mergedBamFilename + " SO=coordinate TMP_DIR=. CREATE_INDEX=" + createIndex + " VALIDATION_STRINGENCY=SILENT";
		if (createIndex)
			 command += " && mv " + mergedBamFilename + ".bai " + mergedBaiFilename;
		logger.debug("Will conduct picard MergeSamFiles with command: " + command);
		return command;
	}
}
