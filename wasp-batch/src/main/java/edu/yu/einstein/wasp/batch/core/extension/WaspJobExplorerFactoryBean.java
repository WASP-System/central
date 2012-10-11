package edu.yu.einstein.wasp.batch.core.extension;

import javax.sql.DataSource;

import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.repository.dao.AbstractJdbcBatchMetadataDao;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.simple.SimpleJdbcOperations;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.support.incrementer.AbstractDataFieldMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.util.Assert;

import edu.yu.einstein.wasp.batch.core.extension.dao.JdbcWaspJobExecutionDao;
import edu.yu.einstein.wasp.batch.core.extension.dao.JdbcWaspJobInstanceDao;
import edu.yu.einstein.wasp.batch.core.extension.dao.JdbcWaspStepExecutionDao;
import edu.yu.einstein.wasp.batch.core.extension.dao.WaspJobExecutionDao;
import edu.yu.einstein.wasp.batch.core.extension.dao.WaspJobInstanceDao;
import edu.yu.einstein.wasp.batch.core.extension.dao.WaspStepExecutionDao;

public class WaspJobExplorerFactoryBean extends JobExplorerFactoryBean implements InitializingBean {

	
	
	private DataSource dataSourceW;

	private SimpleJdbcOperations jdbcTemplateW;

	private String tablePrefixW = AbstractJdbcBatchMetadataDao.DEFAULT_TABLE_PREFIX;

	private DataFieldMaxValueIncrementer incrementerW = new AbstractDataFieldMaxValueIncrementer() {
		@Override
		protected long getNextKey() {
			throw new IllegalStateException("WaspJobExplorer is read only.");
		}
	};
	
	private WaspJobInstanceDao waspJobInstanceDao;
	
	private WaspJobExecutionDao waspJobExecutionDao;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(dataSourceW, "DataSource must not be null.");
		super.afterPropertiesSet();
		jdbcTemplateW = new SimpleJdbcTemplate(dataSourceW);
		waspJobInstanceDao = createJobInstanceDao();
		waspJobExecutionDao = createJobExecutionDao();
	}


	/**
	 * Public setter for the {@link DataSource}.
	 * 
	 * @param dataSource
	 *            a {@link DataSource}
	 */
	@Override
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
		this.dataSourceW = dataSource;
	}

	/**
	 * Sets the table prefix for all the batch meta-data tables.
	 * 
	 * @param tablePrefix
	 */
	@Override
	public void setTablePrefix(String tablePrefix) {
		super.setTablePrefix(tablePrefix);
		this.tablePrefixW = tablePrefix;
	}
	
	@Override
	protected WaspJobInstanceDao createJobInstanceDao() throws Exception {
		JdbcWaspJobInstanceDao dao = new JdbcWaspJobInstanceDao();
		dao.setJdbcTemplate(jdbcTemplateW);
		dao.setJobIncrementer(incrementerW);
		dao.setTablePrefix(tablePrefixW);
		dao.afterPropertiesSet();
		return dao;
	}
	
	@Override
	protected WaspStepExecutionDao createStepExecutionDao() throws Exception {
		JdbcWaspStepExecutionDao dao = new JdbcWaspStepExecutionDao();
		dao.setWaspJobInstanceDao(waspJobInstanceDao);
		dao.setJobExecutionDao(waspJobExecutionDao);
		dao.setJdbcTemplate(jdbcTemplateW);
		dao.setStepExecutionIncrementer(incrementerW);
		dao.setTablePrefix(tablePrefixW);
		dao.afterPropertiesSet();
		return dao;
	}
	
	@Override
	protected WaspJobExecutionDao createJobExecutionDao() throws Exception {
		JdbcWaspJobExecutionDao dao = new JdbcWaspJobExecutionDao();
		dao.setWaspJobInstanceDao(waspJobInstanceDao);
		dao.setJdbcTemplate(jdbcTemplateW);
		dao.setJobExecutionIncrementer(incrementerW);
		dao.setTablePrefix(tablePrefixW);
		dao.afterPropertiesSet();
		return dao;
	}
	
	@Override
	public Object getObject() throws Exception {
		return new WaspBatchJobExplorer(waspJobInstanceDao,
				waspJobExecutionDao, createStepExecutionDao(),
			createExecutionContextDao());
	}

}
