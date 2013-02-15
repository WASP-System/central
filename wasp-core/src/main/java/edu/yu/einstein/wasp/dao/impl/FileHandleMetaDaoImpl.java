
/**
 *
 * FileHandleMetaDaoImpl.java 
 * @author asmclellan
 *  
 * the FileHandleMeta Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.FileHandleMeta;


@Transactional
@Repository
public class FileHandleMetaDaoImpl extends WaspMetaDaoImpl<FileHandleMeta> implements edu.yu.einstein.wasp.dao.FileHandleMetaDao {

	/**
	 * FileHandleMetaDaoImpl() Constructor
	 *
	 *
	 */
	public FileHandleMetaDaoImpl() {
		super();
		this.entityClass = FileHandleMeta.class;
	}


	/**
	 * getFileMetaByFileMetaId(final int fileMetaId)
	 *
	 * @param final int fileMetaId
	 *
	 * @return fileMeta
	 */

	@Override
	@Transactional
	public FileHandleMeta getFileMetaByFileMetaId (final int fileMetaId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("fileMetaId", fileMetaId);

		List<FileHandleMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			FileHandleMeta rt = new FileHandleMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getFileMetaByKFileId(final String k, final int fileId)
	 *
	 * @param final String k, final int fileId
	 *
	 * @return fileMeta
	 */

	@Override
	@Transactional
	public FileHandleMeta getFileMetaByKFileId (final String k, final int fileId) {
    		HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("k", k);
		m.put("fileId", fileId);

		List<FileHandleMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			FileHandleMeta rt = new FileHandleMeta();
			return rt;
		}
		return results.get(0);
	}

	/**
	 * getWorkflowMetaByKWorkflowId(final String k, final Integer workflowId)
	 *
	 * @param final String k, final Integer workflowId
	 *
	 * @return workflowMeta
	 */

	@Override
	@Transactional
	public FileHandleMeta getFileMetaByKWorkflowId (final String k, final Integer fileId) {
    	HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("k", k);
		m.put("fileId", fileId);

		List<FileHandleMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			FileHandleMeta rt = new FileHandleMeta();
			return rt;
		}
		return results.get(0);
	}



}

