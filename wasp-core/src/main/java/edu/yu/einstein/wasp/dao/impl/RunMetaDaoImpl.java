
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


@Transactional("entityManager")
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
	@Transactional("entityManager")
	public RunMeta getRunMetaByRunMetaId (final int runMetaId) {
    		HashMap<String, Integer> m = new HashMap<String, Integer>();
		m.put("id", runMetaId);

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
	@Transactional("entityManager")
	public RunMeta getRunMetaByKRunId (final String k, final int runId) {
    		HashMap<String, Object> m = new HashMap<String, Object>();
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

