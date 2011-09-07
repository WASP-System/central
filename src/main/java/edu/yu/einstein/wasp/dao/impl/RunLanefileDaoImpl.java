
/**
 *
 * RunLanefileDaoImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the RunLanefile Dao Impl
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

import edu.yu.einstein.wasp.model.RunLanefile;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class RunLanefileDaoImpl extends WaspDaoImpl<RunLanefile> implements edu.yu.einstein.wasp.dao.RunLanefileDao {

	/**
	 * RunLanefileDaoImpl() Constructor
	 *
	 *
	 */
	public RunLanefileDaoImpl() {
		super();
		this.entityClass = RunLanefile.class;
	}


	/**
	 * getRunLanefileByRunLanefileId(final int runLanefileId)
	 *
	 * @param final int runLanefileId
	 *
	 * @return runLanefile
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public RunLanefile getRunLanefileByRunLanefileId (final int runLanefileId) {
    		HashMap m = new HashMap();
		m.put("runLanefileId", runLanefileId);

		List<RunLanefile> results = (List<RunLanefile>) this.findByMap((Map) m);

		if (results.size() == 0) {
			RunLanefile rt = new RunLanefile();
			return rt;
		}
		return (RunLanefile) results.get(0);
	}



	/**
	 * getRunLanefileByFileId(final int fileId)
	 *
	 * @param final int fileId
	 *
	 * @return runLanefile
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public RunLanefile getRunLanefileByFileId (final int fileId) {
    		HashMap m = new HashMap();
		m.put("fileId", fileId);

		List<RunLanefile> results = (List<RunLanefile>) this.findByMap((Map) m);

		if (results.size() == 0) {
			RunLanefile rt = new RunLanefile();
			return rt;
		}
		return (RunLanefile) results.get(0);
	}



}

