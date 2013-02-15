/**
 * 
 */
package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.FileGroupDao;
import edu.yu.einstein.wasp.exception.ModelDetachException;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;

/**
 * @author calder
 *
 */
@Transactional
@Repository
public class FileGroupDaoImpl extends WaspDaoImpl<FileGroup> implements edu.yu.einstein.wasp.dao.FileGroupDao {

	/**
	 * FileHandleDaoImpl() Constructor
	 *
	 *
	 */
	public FileGroupDaoImpl() {
		super();
		this.entityClass = FileGroup.class;
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
	public FileGroup getFileGroupById (final Integer id) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("fileGroupId", id);

		List<FileGroup> results = this.findByMap(m);

		if (results.size() == 0) {
			FileGroup rt = new FileGroup();
			return rt;
		}
		return results.get(0);
	}



}
