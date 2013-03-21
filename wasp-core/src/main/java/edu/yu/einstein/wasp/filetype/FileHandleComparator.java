/**
 * 
 */
package edu.yu.einstein.wasp.filetype;

import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;

import edu.yu.einstein.wasp.model.FileHandle;

/**
 * Abstract base class for implementing FileType specific comparators.  The base implementation
 * compares files by file name only.  Other comparators may implement additional requirements,
 * for example FASTQ files may be ordered by sample id, file number, then read segment.
 * 
 * @author calder
 * 
 */
public class FileHandleComparator implements Comparator<FileHandle> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(FileHandle arg0, FileHandle arg1) {
		return new CompareToBuilder()
				.append(arg0.getFileName(), arg1.getFileName())
				.toComparison();
	}

}
