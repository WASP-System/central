
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

import java.util.List;

import edu.yu.einstein.wasp.model.FileMeta;


public interface FileMetaDao extends WaspDao<FileMeta> {

  public FileMeta getFileMetaByFileMetaId (final int fileMetaId);

  public FileMeta getFileMetaByKFileId (final String k, final int fileId);

  public void updateByFileId (final int fileId, final List<FileMeta> metaList);

  public void updateByFileId(int fileId, FileMeta m);

  public FileMeta getFileMetaByKWorkflowId(String k, Integer fileId);




}

