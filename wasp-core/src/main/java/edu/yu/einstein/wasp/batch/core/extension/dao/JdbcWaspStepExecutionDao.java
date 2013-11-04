package edu.yu.einstein.wasp.batch.core.extension.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.dao.JdbcStepExecutionDao;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import edu.yu.einstein.wasp.batch.core.extension.WaspBatchExitStatus;
import edu.yu.einstein.wasp.exception.BatchDaoDataRetrievalException;

/**
 * 
 * @author asmclellan
 *
 */
@Repository
public class JdbcWaspStepExecutionDao extends JdbcStepExecutionDao implements WaspStepExecutionDao, InitializingBean{
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	private WaspJobExecutionDao waspJobExecutionDao;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void setWaspJobExecutionDao(){}
	
	public void setWaspJobExecutionDao(WaspJobExecutionDao waspJobExecutionDao){
		Assert.notNull(waspJobExecutionDao, "waspJobExecutionDao cannot be null");
		this.waspJobExecutionDao = waspJobExecutionDao;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<StepExecution> getStepExecutions(String name, Map<String, Set<String>> parameterMap, Boolean exclusive, ExitStatus exitStatus){
		Assert.notNull(waspJobExecutionDao, "waspJobExecutionDao cannot be  null");
		final List<StepExecution> stepExecutions = new ArrayList<StepExecution>();
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		String sql = "select SE.JOB_EXECUTION_ID, STEP_EXECUTION_ID from %PREFIX%STEP_EXECUTION SE, %PREFIX%JOB_EXECUTION JE where "
				+ "(STEP_NAME LIKE :name1 or STEP_NAME LIKE :name2) and JE.JOB_EXECUTION_ID = SE.JOB_EXECUTION_ID";
		if (name == null)
			name = "";
		parameterSource.addValue("name1", "%" + name, Types.VARCHAR);
		parameterSource.addValue("name2", name + "%", Types.VARCHAR);
		if (exitStatus != null){
			if (exitStatus.getExitCode().equals(WaspBatchExitStatus.RUNNING.getExitCode())){
				sql += " and (SE.EXIT_CODE = :exitStatus1 OR SE.EXIT_CODE = :exitStatus2 OR SE.EXIT_CODE = :exitStatus3) ";
				parameterSource.addValue("exitStatus1", ExitStatus.EXECUTING.getExitCode().toString(), Types.VARCHAR);
				parameterSource.addValue("exitStatus2", ExitStatus.UNKNOWN.getExitCode().toString(), Types.VARCHAR);
				parameterSource.addValue("exitStatus3", WaspBatchExitStatus.HIBERNATING.getExitCode().toString(), Types.VARCHAR);
			} else {
				sql += " and SE.EXIT_CODE = :exitStatus ";
				parameterSource.addValue("exitStatus", exitStatus.getExitCode().toString(), Types.VARCHAR);
			}
		}
		if (parameterMap != null){
			if (exclusive == null)
				exclusive = false;
			List<Long> jobExecutionIds = null;
			if (exclusive){
				jobExecutionIds = waspJobExecutionDao.getJobExecutionIdsByExclusivelyMatchingParameters(parameterMap);
			} else {
				jobExecutionIds = waspJobExecutionDao.getJobExecutionIdsByMatchingParameters(parameterMap);
			}
			if (jobExecutionIds == null || jobExecutionIds.isEmpty())
				return stepExecutions;

			sql += " and JE.JOB_EXECUTION_ID in ( ";
			
			int index = 1;
			for (Long id: jobExecutionIds){
				parameterSource.addValue("id"+index, id, Types.BIGINT);
				if (index > 1)
					sql += ",";
				sql += " :id" + index;
				index++;
			}
			sql += " )";
		}
		sql += " ORDER BY STEP_EXECUTION_ID DESC, SE.JOB_EXECUTION_ID DESC";
		logger.trace("Built SQL string: " + getQuery(sql));
		for (String key: parameterSource.getValues().keySet())
			logger.trace("Parameter: " + key + "=" + parameterSource.getValues().get(key).toString());

		RowMapper<StepExecution> mapper = new RowMapper<StepExecution>() {
			
			@Override
			public StepExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
				JobExecution jobExecution = waspJobExecutionDao.getJobExecution(rs.getLong(1));
				if (jobExecution == null)
					throw new SQLException("Failed to map result for row number " + rowNum + "(jobExecutionId=" + rs.getLong(1) + ") to a JobExecution: ");
				Long stepExecutionId = rs.getLong(2);
				StepExecution stepExecution = getStepExecution(jobExecution, stepExecutionId);
				if (stepExecution == null)
					throw new BatchDaoDataRetrievalException("Failed to map result for row number " + rowNum + "(jobExecutionId=" + rs.getLong(1) 
							+ ", stepExecutionId=" + rs.getLong(2) + ") to a StepExecution: ");
				stepExecutions.add(stepExecution);
				return stepExecution;
			}
		};
		
		namedParameterJdbcTemplate.query(getQuery(sql), parameterSource, mapper);
		return stepExecutions;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public JobParameters getJobParameters(StepExecution stepExecution){
		return (waspJobExecutionDao.getJobExecution(stepExecution.getJobExecutionId())).getJobParameters();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void deleteStepExecution(Long stepExecutionId){
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("stepExecutionId", stepExecutionId, Types.BIGINT);
		String sql1 = "DELETE FROM %PREFIX%STEP_EXECUTION_CONTEXT WHERE STEP_EXECUTION_ID = :stepExecutionId";
		String sql2 = "DELETE FROM %PREFIX%STEP_EXECUTION WHERE STEP_EXECUTION_ID = :stepExecutionId";
		if (namedParameterJdbcTemplate.update(getQuery(sql1), parameterSource) != 1)
			throw new DataRetrievalFailureException("Not able to delete StepExecutionContext for StepExecution with id=" + stepExecutionId);
		if (namedParameterJdbcTemplate.update(getQuery(sql2), parameterSource) != 1)
			throw new DataRetrievalFailureException("Not able to delete StepExecution with id=" + stepExecutionId);
	}
	
	
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
	}
}
