
/**
 *
 * SampleFileDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleFile Dao Impl
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

import edu.yu.einstein.wasp.model.SampleFile;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SampleFileDaoImpl extends WaspDaoImpl<SampleFile> implements edu.yu.einstein.wasp.dao.SampleFileDao {

	/**
	 * SampleFileDaoImpl() Constructor
	 *
	 *
	 */
	public SampleFileDaoImpl() {
		super();
		this.entityClass = SampleFile.class;
	}


	/**
	 * getSampleFileBySampleFileId(final int sampleFileId)
	 *
	 * @param final int sampleFileId
	 *
	 * @return sampleFile
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public SampleFile getSampleFileBySampleFileId (final int sampleFileId) {
    		HashMap m = new HashMap();
		m.put("sampleFileId", sampleFileId);

		List<SampleFile> results = (List<SampleFile>) this.findByMap((Map) m);

		if (results.size() == 0) {
			SampleFile rt = new SampleFile();
			return rt;
		}
		return (SampleFile) results.get(0);
	}



}

