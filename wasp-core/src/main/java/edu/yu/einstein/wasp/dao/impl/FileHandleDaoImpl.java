
/**
 *
 * FileHandleDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the FileHandle Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.FileHandle;


@Transactional
@Repository
public class FileHandleDaoImpl extends WaspDaoImpl<FileHandle> implements edu.yu.einstein.wasp.dao.FileHandleDao {

	/**
	 * FileHandleDaoImpl() Constructor
	 *
	 *
	 */
	public FileHandleDaoImpl() {
		super();
		this.entityClass = FileHandle.class;
	}


	/**
	 * getFileByFileId(final Integer fileId)
	 *
	 * @param final Integer fileId
	 *
	 * @return file
	 */

	@Override
	@Transactional
	public FileHandle getFileHandleById (final Integer id) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", id);

		List<FileHandle> results = this.findByMap(m);

		if (results.size() == 0) {
			FileHandle rt = new FileHandle();
			return rt;
		}
		return results.get(0);
	}



}

