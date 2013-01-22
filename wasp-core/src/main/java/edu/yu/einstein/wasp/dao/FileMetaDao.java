
/**
 *
 * FileMetaDao.java 
 * @author asmclellan
 *  
 * the FileMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.FileMeta;


public interface FileMetaDao extends WaspMetaDao<FileMeta> {

  public FileMeta getFileMetaByFileMetaId (final int fileMetaId);

  public FileMeta getFileMetaByKFileId (final String k, final int fileId);

  public FileMeta getFileMetaByKWorkflowId(String k, Integer fileId);




}

