package edu.yu.einstein.wasp.batch.core.extension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.support.SimpleJobExplorer;
import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.batch.core.extension.dao.WaspBatchDao;
import edu.yu.einstein.wasp.service.JobExplorerWasp;

@Service
@Transactional
public class WaspBatchJobExplorer extends SimpleJobExplorer implements JobExplorerWasp{
	
	private WaspBatchDao waspBatchDao;
	
		
	@Autowired
	public WaspBatchJobExplorer(JobInstanceDao jobInstanceDao,
            JobExecutionDao jobExecutionDao,
            StepExecutionDao stepExecutionDao,
            ExecutionContextDao ecDao,
            WaspBatchDao waspBatchDao) {
		super(jobInstanceDao, jobExecutionDao, stepExecutionDao, ecDao);
		this.waspBatchDao = waspBatchDao;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<JobInstance> getJobInstancesMatchingParameters(Map<String, String> parameterMap){
		List<JobInstance> masterJobInstances = new ArrayList<JobInstance>();
		for (Long jobInstanceId: waspBatchDao.getJobInstanceIdsByMatchingParameters(parameterMap)){
			masterJobInstances.add(this.getJobInstance(jobInstanceId));
		}
		return masterJobInstances;
	}

}
