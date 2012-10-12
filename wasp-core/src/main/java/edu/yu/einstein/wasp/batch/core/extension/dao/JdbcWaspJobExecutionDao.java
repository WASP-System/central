package edu.yu.einstein.wasp.batch.core.extension.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.repository.dao.JdbcJobExecutionDao;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;


@Repository
public class JdbcWaspJobExecutionDao extends JdbcJobExecutionDao implements WaspJobExecutionDao, InitializingBean{
	
	private WaspJobInstanceDao waspJobInstanceDao;
	
	private static final Logger logger = Logger.getLogger(JdbcWaspJobExecutionDao.class);
	
	public void setWaspJobInstanceDao(WaspJobInstanceDao waspJobInstanceDao){
		Assert.notNull(waspJobInstanceDao, "waspJobInstanceDao cannot be null");
		this.waspJobInstanceDao = waspJobInstanceDao;
	}
	
		
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<JobExecution> getJobExecutions(String name, Map<String, String> parameterMap, Boolean exclusive, BatchStatus batchStatus, ExitStatus exitStatus){
		Assert.notNull(parameterMap, "parameterMap must not be null");
		Assert.notNull(waspJobInstanceDao, "waspJobInstanceDao cannot be  null");
		final List<JobExecution> JobExecutions = new ArrayList<JobExecution>();
		if (exclusive == null)
			exclusive = false;
		List<Long> jobInstanceIds = null;
		if (exclusive){
			jobInstanceIds = waspJobInstanceDao.getJobInstanceIdsByExclusivelyMatchingParameters(parameterMap);
		} else {
			jobInstanceIds = waspJobInstanceDao.getJobInstanceIdsByMatchingParameters(parameterMap);
		}
		if (jobInstanceIds == null || jobInstanceIds.isEmpty())
			return JobExecutions;
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		
		
		String sql = "SELECT E.JOB_EXECUTION_ID from %PREFIX%JOB_EXECUTION E, %PREFIX%JOB_INSTANCE I where "
			+ "E.JOB_INSTANCE_ID=I.JOB_INSTANCE_ID and I.JOB_NAME LIKE :name ";
		
		if (name == null)
			name = "";
		parameterSource.addValue("name", "%"+name);
		
		if (batchStatus != null){
			sql += "and STATUS = :status ";
			parameterSource.addValue("status", batchStatus);
		}
		if (exitStatus != null){
			sql += "and EXIT_CODE = :exitStatus ";
			parameterSource.addValue("exitStatus", exitStatus.getExitCode());
		}
		
		sql += "and E.JOB_INSTANCE_ID in ( ";
		
		int index = 1;
		for (Long id: jobInstanceIds){
			parameterSource.addValue("id"+index, id);
			if (index > 1)
				sql += ",";
			sql += " :id" + index;
			index++;
		}
		sql += " )";
		logger.debug("Built SQL string: " + getQuery(sql));
		for (String key: parameterSource.getValues().keySet())
			logger.debug("Parameter: " + key + "=" + parameterSource.getValues().get(key).toString());
		RowMapper<JobExecution> mapper = new RowMapper<JobExecution>() {
			
			@Override
			public JobExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
				JobExecution jobExecution = getJobExecution(rs.getLong(1));
				if (jobExecution == null)
					throw new SQLException("Failed to map result for row number " + rowNum + "(jobExecutionId=" + rs.getLong(1) + ") to a JobExecution: ");
				JobExecutions.add(jobExecution);
				return jobExecution;
			}
		};
		
		getJdbcTemplate().query(getQuery(sql), mapper, parameterSource);
		return JobExecutions;
	}

	
	
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
	}
}
