
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

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
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

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public SampleFile getSampleFileBySampleFileId (final int sampleFileId) {
    		HashMap m = new HashMap();
		m.put("sampleFileId", sampleFileId);

		List<SampleFile> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleFile rt = new SampleFile();
			return rt;
		}
		return results.get(0);
	}



}

