
/**
 *
 * FileDaoImpl.java 
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

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class FileDaoImpl extends WaspDaoImpl<FileHandle> implements edu.yu.einstein.wasp.dao.FileDao {

	/**
	 * FileDaoImpl() Constructor
	 *
	 *
	 */
	public FileDaoImpl() {
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
	@SuppressWarnings("unchecked")
	@Transactional
	public FileHandle getFileByFileId (final Integer fileId) {
    		HashMap m = new HashMap();
		m.put("fileId", fileId);

		List<FileHandle> results = this.findByMap(m);

		if (results.size() == 0) {
			FileHandle rt = new FileHandle();
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
	public FileHandle getFileByFilelocation (final String filelocation) {
    		HashMap m = new HashMap();
		m.put("filelocation", filelocation);

		List<FileHandle> results = this.findByMap(m);

		if (results.size() == 0) {
			FileHandle rt = new FileHandle();
			return rt;
		}
		return results.get(0);
	}



}

