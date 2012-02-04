
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
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
	 * getFileByFileId(final Integer fileId)
	 *
	 * @param final Integer fileId
	 *
	 * @return file
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public File getFileByFileId (final Integer fileId) {
    		HashMap m = new HashMap();
		m.put("fileId", fileId);

		List<File> results = this.findByMap(m);

		if (results.size() == 0) {
			File rt = new File();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getFileByFilelocation(final String filelocation)
	 *
	 * @param final String filelocation
	 *
	 * @return file
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public File getFileByFilelocation (final String filelocation) {
    		HashMap m = new HashMap();
		m.put("filelocation", filelocation);

		List<File> results = this.findByMap(m);

		if (results.size() == 0) {
			File rt = new File();
			return rt;
		}
		return results.get(0);
	}



}

