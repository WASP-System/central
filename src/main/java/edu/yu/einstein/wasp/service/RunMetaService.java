
/**
 *
 * RunMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the RunMetaService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.RunMetaDao;
import edu.yu.einstein.wasp.model.RunMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface RunMetaService extends WaspService<RunMeta> {

	/**
	 * setRunMetaDao(RunMetaDao runMetaDao)
	 *
	 * @param runMetaDao
	 *
	 */
	public void setRunMetaDao(RunMetaDao runMetaDao);

	/**
	 * getRunMetaDao();
	 *
	 * @return runMetaDao
	 *
	 */
	public RunMetaDao getRunMetaDao();

  public RunMeta getRunMetaByRunMetaId (final int runMetaId);

  public RunMeta getRunMetaByKRunId (final String k, final int runId);

  public void updateByRunId (final int runId, final List<RunMeta> metaList);


}

