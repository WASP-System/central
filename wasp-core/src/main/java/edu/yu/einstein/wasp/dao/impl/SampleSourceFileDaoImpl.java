
/**
 *
 * SampleSourceFileDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleSourceFile Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.SampleSourceFile;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SampleSourceFileDaoImpl extends WaspDaoImpl<SampleSourceFile> implements edu.yu.einstein.wasp.dao.SampleSourceFileDao {

	/**
	 * SampleSourceFileDaoImpl() Constructor
	 *
	 *
	 */
	public SampleSourceFileDaoImpl() {
		super();
		this.entityClass = SampleSourceFile.class;
	}


	/**
	 * getSampleSourceFileBySampleSourceFileId(final int sampleSourceFileId)
	 *
	 * @param final int sampleSourceFileId
	 *
	 * @return sampleSourceFile
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public SampleSourceFile getSampleSourceFileBySampleSourceFileId (final int sampleSourceFileId) {
    		HashMap m = new HashMap();
		m.put("id", sampleSourceFileId);

		List<SampleSourceFile> results = this.findByMap(m);

		if (results.size() == 0) {
			SampleSourceFile rt = new SampleSourceFile();
			return rt;
		}
		return results.get(0);
	}



}

