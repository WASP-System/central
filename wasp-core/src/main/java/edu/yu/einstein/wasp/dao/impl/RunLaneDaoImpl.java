
/**
 *
 * RunLaneDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the RunLane Dao Impl
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.RunLane;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class RunLaneDaoImpl extends WaspDaoImpl<RunLane> implements edu.yu.einstein.wasp.dao.RunLaneDao {

	/**
	 * RunLaneDaoImpl() Constructor
	 *
	 *
	 */
	public RunLaneDaoImpl() {
		super();
		this.entityClass = RunLane.class;
	}


	/**
	 * getRunLaneByRunLaneId(final int runLaneId)
	 *
	 * @param final int runLaneId
	 *
	 * @return runLane
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public RunLane getRunLaneByRunLaneId (final int runLaneId) {
    		HashMap m = new HashMap();
		m.put("runLaneId", runLaneId);

		List<RunLane> results = this.findByMap(m);

		if (results.size() == 0) {
			RunLane rt = new RunLane();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getRunLaneByRunIdResourcelaneId(final int runId, final int resourcelaneId)
	 *
	 * @param final int runId, final int resourcelaneId
	 *
	 * @return runLane
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public RunLane getRunLaneByRunIdResourcelaneId (final int runId, final int resourcelaneId) {
    		HashMap m = new HashMap();
		m.put("runId", runId);
		m.put("resourcelaneId", resourcelaneId);

		List<RunLane> results = this.findByMap(m);

		if (results.size() == 0) {
			RunLane rt = new RunLane();
			return rt;
		}
		return results.get(0);
	}



	/**
	 * getRunLaneBySampleIdRunId(final int sampleId, final int runId)
	 *
	 * @param final int sampleId, final int runId
	 *
	 * @return runLane
	 */

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public RunLane getRunLaneBySampleIdRunId (final int sampleId, final int runId) {
    		HashMap m = new HashMap();
		m.put("sampleId", sampleId);
		m.put("runId", runId);

		List<RunLane> results = this.findByMap(m);

		if (results.size() == 0) {
			RunLane rt = new RunLane();
			return rt;
		}
		return results.get(0);
	}



}

