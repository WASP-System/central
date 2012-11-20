package edu.yu.einstein.wasp.batch.core.extension.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.repository.dao.JdbcJobExecutionDao;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;


@Repository
public class JdbcWaspJobExecutionDao extends JdbcJobExecutionDao implements WaspJobExecutionDao, InitializingBean{
	
	private WaspJobInstanceDao waspJobInstanceDao;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void setWaspJobInstanceDao(WaspJobInstanceDao waspJobInstanceDao){
		Assert.notNull(waspJobInstanceDao, "waspJobInstanceDao cannot be null");
		this.waspJobInstanceDao = waspJobInstanceDao;
	}
	
		
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<JobExecution> getJobExecutions(String name, Map<String, Set<String>> parameterMap, Boolean exclusive, BatchStatus batchStatus, ExitStatus exitStatus){
		Assert.notNull(waspJobInstanceDao, "waspJobInstanceDao cannot be  null");
		final List<JobExecution> jobExecutions = new ArrayList<JobExecution>();
		
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		
		
		String sql = "SELECT E.JOB_EXECUTION_ID from %PREFIX%JOB_EXECUTION E, %PREFIX%JOB_INSTANCE I where "
			+ "E.JOB_INSTANCE_ID=I.JOB_INSTANCE_ID "
			+ "and (I.JOB_NAME LIKE :name1 or I.JOB_NAME LIKE :name2)";
		
		if (name == null)
			name = "";
		parameterSource.addValue("name1", "%" + name);
		parameterSource.addValue("name2", name + "%");
		
		if (batchStatus != null){
			sql += " and STATUS = :status ";
			parameterSource.addValue("status", batchStatus.toString());
		}
		if (exitStatus != null){
			sql += " and EXIT_CODE = :exitStatus ";
			parameterSource.addValue("exitStatus", exitStatus.getExitCode().toString());
		}
		if (parameterMap != null){
			if (exclusive == null)
				exclusive = false;
			List<Long> jobInstanceIds = null;
			if (exclusive){
				jobInstanceIds = waspJobInstanceDao.getJobInstanceIdsByExclusivelyMatchingParameters(parameterMap);
			} else {
				jobInstanceIds = waspJobInstanceDao.getJobInstanceIdsByMatchingParameters(parameterMap);
			}
			if (jobInstanceIds == null || jobInstanceIds.isEmpty())
				return jobExecutions;
			
			sql += " and E.JOB_INSTANCE_ID in ( ";
			
			int index = 1;
			for (Long id: jobInstanceIds){
				parameterSource.addValue("id"+index, id);
				if (index > 1)
					sql += ",";
				sql += " :id" + index;
				index++;
			}
			sql += " )";
		}
		logger.debug("Built SQL string: " + getQuery(sql));
		for (String key: parameterSource.getValues().keySet())
			logger.debug("Parameter: " + key + "=" + parameterSource.getValues().get(key).toString());
		
		
		RowMapper<JobExecution> mapper = new RowMapper<JobExecution>() {
			
			@Override
			public JobExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
				logger.debug("Mapping result for row number " + rowNum + " (jobExecutionId=" + rs.getLong(1) + ") to a JobExecution");
				JobExecution jobExecution = getJobExecution(rs.getLong(1));
				if (jobExecution == null)
					throw new SQLException("Failed to map result for row number " + rowNum + "(jobExecutionId=" + rs.getLong(1) + ") to a JobExecution: ");
				jobExecutions.add(jobExecution);
				return jobExecution;
			}
		};
		
		getJdbcTemplate().query(getQuery(sql), mapper, parameterSource);
		return jobExecutions;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public JobParameters getJobParameters(JobExecution jobExecution){
		return (waspJobInstanceDao.getJobInstance(jobExecution).getJobParameters());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getJobName(JobExecution jobExecution){
		return (waspJobInstanceDao.getJobInstance(jobExecution).getJobName());
	}

	
	
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
	}
}
