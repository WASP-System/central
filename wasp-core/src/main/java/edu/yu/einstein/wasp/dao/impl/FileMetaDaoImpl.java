
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

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class FileMetaDaoImpl extends WaspDaoImpl<FileMeta> implements edu.yu.einstein.wasp.dao.FileMetaDao {

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
	@SuppressWarnings("unchecked")
	@Transactional
	public FileMeta getFileMetaByFileMetaId (final int fileMetaId) {
    		HashMap m = new HashMap();
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
	@SuppressWarnings("unchecked")
	@Transactional
	public FileMeta getFileMetaByKFileId (final String k, final int fileId) {
    		HashMap m = new HashMap();
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
	 * updateByFileId (final int fileId, final List<FileMeta> metaList)
	 *
	 * @param fileId
	 * @param metaList
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByFileId (final int fileId, final List<FileMeta> metaList) {
		for (FileMeta m:metaList) {
			FileMeta currentMeta = getFileMetaByKFileId(m.getK(), fileId);
			if (currentMeta.getFileMetaId() == null){
				// metadata value not in database yet
				m.setFileId(fileId);
				entityManager.persist(m);
			} else if (!currentMeta.getV().equals(m.getV())){
				// meta exists already but value has changed
				currentMeta.setV(m.getV());
				entityManager.merge(currentMeta);
			} else{
				// no change to meta so do nothing
			}
		}
  	}



}

