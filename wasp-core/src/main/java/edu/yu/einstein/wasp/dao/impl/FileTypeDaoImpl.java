
/**
 *
 * FileTypeDaoImpl.java 
 * 
 * the FileType Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.FileTypeDao;
import edu.yu.einstein.wasp.model.FileType;


@Transactional("entityManager")
@Repository
public class FileTypeDaoImpl extends WaspDaoImpl<FileType> implements FileTypeDao {

	/**
	 * FileTypeDaoImpl() Constructor
	 *
	 *
	 */
	public FileTypeDaoImpl() {
		super();
		this.entityClass = FileType.class;
	}


	/**
	 * getFileTypeByFileTypeId(final Integer fileTypeId)
	 *
	 * @param final Integer fileId
	 *
	 * @return FileType
	 */

	@Override
	@Transactional("entityManager")
	public FileType getFileTypeByFileTypeId (final Integer fileTypeId) {
    	HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", fileTypeId);

		List<FileType> results = this.findByMap(m);

		if (results.size() == 0) {
			FileType rt = new FileType();
			return rt;
		}
		return results.get(0);
	}
	
	/**
	 * getFileTypeByIname(final String iName)
	 *
	 * @param final String iName
	 *
	 * @return FileType
	 */

	@Override
	@Transactional("entityManager")
	public FileType getFileTypeByIName(final String iName) {
    	HashMap<String, String> m = new HashMap<String, String>();
		m.put("iName", iName);

		List<FileType> results = this.findByMap(m);

		if (results.size() == 0) {
			FileType rt = new FileType();
			return rt;
		}
		return results.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional("entityManager")
	public Set<FileType> getFileTypes() {
		HashSet<FileType> result = new HashSet<FileType>();
		Query q = getEntityManager().createQuery("from FileType");
		result.addAll(q.getResultList());
		return result;
	}

}

