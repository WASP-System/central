package edu.yu.einstein.wasp.batch.core.extension.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.batch.core.repository.dao.JdbcJobInstanceDao;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;


@Repository
public class JdbcWaspJobInstanceDao extends JdbcJobInstanceDao implements WaspJobInstanceDao, InitializingBean{
	
	private static final Logger logger = Logger.getLogger(JdbcWaspJobInstanceDao.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Long> getJobInstanceIdsByMatchingParameters(Map<String, String> parameterMap){
		Assert.notNull(parameterMap, "parameterMap must not be null");
		if (parameterMap.isEmpty())
			return null;
		final List<Long> jobInstanceIds = new ArrayList<Long>();
		String sql = "select distinct JOB_INSTANCE_ID from BATCH_JOB_PARAMS where TYPE_CD = 'STRING'";
		int index = 1;
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		for (String key: parameterMap.keySet()){
			sql += " and JOB_INSTANCE_ID in (select JOB_INSTANCE_ID from BATCH_JOB_PARAMS where KEY_NAME = :key"+index+" and STRING_VAL = :val"+index+" )";
			parameterSource.addValue("key"+index, key);
			parameterSource.addValue("val"+index, parameterMap.get(key));
			index++;
		}
		logger.debug("Built SQL string: " + sql);
		for (String key: parameterSource.getValues().keySet())
			logger.debug("Parameter: " + key + "=" + parameterSource.getValues().get(key).toString());
		
		
		RowMapper<Long> mapper = new RowMapper<Long>() {
			
			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				Long rowVal = rs.getLong(1);
				jobInstanceIds.add(rowVal);
				return rowVal;
			}
		};
		
		getJdbcTemplate().query(sql, mapper, parameterSource);
		return jobInstanceIds;
	}
	

	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
	}
}
