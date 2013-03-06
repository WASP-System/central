
/**
 *
 * FileHandleMetaDao.java 
 * @author asmclellan
 *  
 * the FileHandleMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.FileHandleMeta;


public interface FileHandleMetaDao extends WaspMetaDao<FileHandleMeta> {

  public FileHandleMeta getFileMetaByFileMetaId (final int fileMetaId);

  public FileHandleMeta getFileMetaByKFileId (final String k, final int fileId);

  public FileHandleMeta getFileMetaByKWorkflowId(String k, Integer fileId);




}

