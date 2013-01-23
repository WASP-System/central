/**
 *
 * FileTypeDao.java 
 * 
 * the FileType Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.Set;

import edu.yu.einstein.wasp.model.FileType;

public interface FileTypeDao extends WaspDao<FileType> {

	public FileType getFileTypeByFileTypeId(final Integer fileTypeId);

	public FileType getFileTypeByIName(String iName);

	public Set<FileType> getFileTypes();

}
