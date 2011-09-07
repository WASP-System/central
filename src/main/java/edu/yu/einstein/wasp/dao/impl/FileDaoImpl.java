
/**
 *
 * FileDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the File Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.File;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class FileDaoImpl extends WaspDaoImpl<File> implements edu.yu.einstein.wasp.dao.FileDao {

	/**
	 * FileDaoImpl() Constructor
	 *
	 *
	 */
	public FileDaoImpl() {
		super();
		this.entityClass = File.class;
	}


	/**
	 * getFileByFileId(final int fileId)
	 *
	 * @param final int fileId
	 *
	 * @return file
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public File getFileByFileId (final int fileId) {
    		HashMap m = new HashMap();
		m.put("fileId", fileId);

		List<File> results = (List<File>) this.findByMap((Map) m);

		if (results.size() == 0) {
			File rt = new File();
			return rt;
		}
		return (File) results.get(0);
	}



}

