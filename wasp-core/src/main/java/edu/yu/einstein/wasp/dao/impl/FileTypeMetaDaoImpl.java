
/**
 *
 * FileTypeMetaDaoImpl.java 
 * @author asmclellan
 *  
 * the FileTypeMeta Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.FileTypeMetaDao;
import edu.yu.einstein.wasp.model.FileTypeMeta;


@Transactional
@Repository
public class FileTypeMetaDaoImpl extends WaspMetaDaoImpl<FileTypeMeta> implements FileTypeMetaDao {

	/**
	 * FileTypeMetaDaoImpl() Constructor
	 *
	 *
	 */
	public FileTypeMetaDaoImpl() {
		super();
		this.entityClass = FileTypeMeta.class;
	}


	/**
	 * getFileTypeMetaByFileTypeMetaId(final int fileTypeMetaId)
	 *
	 * @param final int fileTypeMetaId
	 *
	 * @return fileTypeMeta
	 */

	@Override
	@Transactional
	public FileTypeMeta getFileTypeMetaByFileTypeMetaId (final int fileTypeMetaId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", fileTypeMetaId);

		List<FileTypeMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			FileTypeMeta rt = new FileTypeMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getFileTypeMetaByKFileTypeId(final String k, final int fileTypeId)
	 *
	 * @param final String k, final int fileTypeId
	 *
	 * @return fileTypeMeta
	 */

	@Override
	@Transactional
	public FileTypeMeta getFileTypeMetaByKFileTypeId (final String k, final int fileTypeId) {
    		HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("k", k);
		m.put("fileTypeId", fileTypeId);

		List<FileTypeMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			FileTypeMeta rt = new FileTypeMeta();
			return rt;
		}
		return results.get(0);
	}

}



