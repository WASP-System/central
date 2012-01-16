
/**
 *
 * RunMetaServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the RunMetaService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.RunMetaDao;
import edu.yu.einstein.wasp.model.RunMeta;
import edu.yu.einstein.wasp.service.RunMetaService;

@Service
public class RunMetaServiceImpl extends WaspMetaServiceImpl<RunMeta> implements RunMetaService {

	/**
	 * runMetaDao;
	 *
	 */
	private RunMetaDao runMetaDao;

	/**
	 * setRunMetaDao(RunMetaDao runMetaDao)
	 *
	 * @param runMetaDao
	 *
	 */
	@Override
	@Autowired
	public void setRunMetaDao(RunMetaDao runMetaDao) {
		this.runMetaDao = runMetaDao;
		this.setWaspDao(runMetaDao);
	}

	/**
	 * getRunMetaDao();
	 *
	 * @return runMetaDao
	 *
	 */
	@Override
	public RunMetaDao getRunMetaDao() {
		return this.runMetaDao;
	}


  @Override
public RunMeta getRunMetaByRunMetaId (final int runMetaId) {
    return this.getRunMetaDao().getRunMetaByRunMetaId(runMetaId);
  }

  @Override
public RunMeta getRunMetaByKRunId (final String k, final int runId) {
    return this.getRunMetaDao().getRunMetaByKRunId(k, runId);
  }

  @Override
public void updateByRunId (final String area, final int runId, final List<RunMeta> metaList) {
    this.getRunMetaDao().updateByRunId(area, runId, metaList); 
  }

  @Override
public void updateByRunId (final int runId, final List<RunMeta> metaList) {
    this.getRunMetaDao().updateByRunId(runId, metaList); 
  }


}

