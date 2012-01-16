
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
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.RunMeta;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class RunMetaDaoImpl extends WaspDaoImpl<RunMeta> implements edu.yu.einstein.wasp.dao.RunMetaDao {

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

	@SuppressWarnings("unchecked")
	@Transactional
	public RunMeta getRunMetaByRunMetaId (final int runMetaId) {
    		HashMap m = new HashMap();
		m.put("runMetaId", runMetaId);

		List<RunMeta> results = (List<RunMeta>) this.findByMap((Map) m);

		if (results.size() == 0) {
			RunMeta rt = new RunMeta();
			return rt;
		}
		return (RunMeta) results.get(0);
	}



	/**
	 * getRunMetaByKRunId(final String k, final int runId)
	 *
	 * @param final String k, final int runId
	 *
	 * @return runMeta
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public RunMeta getRunMetaByKRunId (final String k, final int runId) {
    		HashMap m = new HashMap();
		m.put("k", k);
		m.put("runId", runId);

		List<RunMeta> results = (List<RunMeta>) this.findByMap((Map) m);

		if (results.size() == 0) {
			RunMeta rt = new RunMeta();
			return rt;
		}
		return (RunMeta) results.get(0);
	}



	/**
	 * updateByRunId (final string area, final int runId, final List<RunMeta> metaList)
	 *
	 * @param runId
	 * @param metaList
	 *
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByRunId (final String area, final int runId, final List<RunMeta> metaList) {
		entityManager.createNativeQuery("delete from runmeta where runId=:runId and k like :area").setParameter("runId", runId).setParameter("area", area + ".%").executeUpdate();

		for (RunMeta m:metaList) {
			m.setRunId(runId);
			entityManager.persist(m);
		}
	}


	/**
	 * updateByRunId (final int runId, final List<RunMeta> metaList)
	 *
	 * @param runId
	 * @param metaList
	 *
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void updateByRunId (final int runId, final List<RunMeta> metaList) {
		entityManager.createNativeQuery("delete from runmeta where runId=:runId").setParameter("runId", runId).executeUpdate();

		for (RunMeta m:metaList) {
			m.setRunId(runId);
			entityManager.persist(m);
		}
 	}



}

