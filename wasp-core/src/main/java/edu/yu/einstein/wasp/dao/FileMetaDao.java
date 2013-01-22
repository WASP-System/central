
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
import edu.yu.einstein.wasp.model.WorkflowMeta;


public interface FileMetaDao extends WaspMetaDao<FileMeta> {

  public FileMeta getFileMetaByFileMetaId (final int fileMetaId);

  public FileMeta getFileMetaByKFileId (final String k, final int fileId);

  public FileMeta getFileMetaByKWorkflowId(String k, Integer fileId);




}

