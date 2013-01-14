
/**
 *
 * RunMetaDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the RunMeta Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.RunMeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class RunMetaDaoImpl extends WaspMetaDaoImpl<RunMeta> implements edu.yu.einstein.wasp.dao.RunMetaDao {

	/**
	 * RunMetaDaoImpl() Constructor
	 *
	 *
	 */
	public RunMetaDaoImpl() {
		super();
		this.entityClass = RunMeta.class;
	}


	/**
	 * getRunMetaByRunMetaId(final int runMetaId)
	 *
	 * @param final int runMetaId
	 *
	 * @return runMeta
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public RunMeta getRunMetaByRunMetaId (final int runMetaId) {
    		HashMap m = new HashMap();
		m.put("runMetaId", runMetaId);

		List<RunMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			RunMeta rt = new RunMeta();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getRunMetaByKRunId(final String k, final int runId)
	 *
	 * @param final String k, final int runId
	 *
	 * @return runMeta
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public RunMeta getRunMetaByKRunId (final String k, final int runId) {
    		HashMap m = new HashMap();
		m.put("k", k);
		m.put("runId", runId);

		List<RunMeta> results = this.findByMap(m);

		if (results.size() == 0) {
			RunMeta rt = new RunMeta();
			return rt;
		}
		return results.get(0);
	}





}

