
/**
 *
 * FileMetaDaoImpl.java 
 * @author asmclellan
 *  
 * the FileMeta Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.FileMeta;


@Transactional
@Repository
public class FileMetaDaoImpl extends WaspMetaDaoImpl<FileMeta> implements edu.yu.einstein.wasp.dao.FileMetaDao {

	/**
	 * FileMetaDaoImpl() Constructor
	 *
	 *
	 */
	public FileMetaDaoImpl() {
		super();
		this.entityClass = FileMeta.class;
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
	public FileMeta getFileMetaByFileMetaId (final int fileMetaId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("fileMetaId", fileMetaId);

		List<FileMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			FileMeta rt = new FileMeta();
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
	public FileMeta getFileMetaByKFileId (final String k, final int fileId) {
    		HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("k", k);
		m.put("fileId", fileId);

		List<FileMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			FileMeta rt = new FileMeta();
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
	public FileMeta getFileMetaByKWorkflowId (final String k, final Integer fileId) {
    	HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("k", k);
		m.put("fileId", fileId);

		List<FileMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			FileMeta rt = new FileMeta();
			return rt;
		}
		return results.get(0);
	}



}

