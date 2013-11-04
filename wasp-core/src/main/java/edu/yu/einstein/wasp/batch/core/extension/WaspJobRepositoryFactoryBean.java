package edu.yu.einstein.wasp.batch.core.extension;

import static org.springframework.batch.support.DatabaseType.SYBASE;

import java.lang.reflect.Field;
import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.dao.AbstractJdbcBatchMetadataDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.database.support.DataFieldMaxValueIncrementerFactory;
import org.springframework.batch.support.DatabaseType;
import org.springframework.jdbc.core.JdbcOperations;

import edu.yu.einstein.wasp.batch.core.extension.dao.JdbcWaspStepExecutionDao;

public class WaspJobRepositoryFactoryBean extends JobRepositoryFactoryBean {
	
	public WaspJobRepositoryFactoryBean(DataSource dataSource){
		setDataSource(dataSource);
	}

	@Override
    public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
	}
	
	@Override
    protected StepExecutionDao createStepExecutionDao() throws Exception {
			JdbcWaspStepExecutionDao dao = new JdbcWaspStepExecutionDao();
            dao.setJdbcTemplate(getJdbcTemplate());
            dao.setStepExecutionIncrementer(getIncrementerFactory().getIncrementer(getDatabaseType(), AbstractJdbcBatchMetadataDao.DEFAULT_TABLE_PREFIX
                            + "STEP_EXECUTION_SEQ"));
            dao.setTablePrefix(AbstractJdbcBatchMetadataDao.DEFAULT_TABLE_PREFIX);
            dao.setClobTypeToUse(determineClobTypeToUse(getDatabaseType()));
            dao.setExitMessageLength(AbstractJdbcBatchMetadataDao.DEFAULT_EXIT_MESSAGE_LENGTH);
            dao.afterPropertiesSet();
            return dao;
    }
	
	/**
     * No getter was declared in JobRepositoryFactoryBean so we need to use reflection to extract the private value
     * @return
     */
    private JdbcOperations getJdbcTemplate(){
    	Field field = null;
		try {
			field = SimpleJobLauncher.class.getDeclaredField("jdbcTemplate");
			field.setAccessible(true);
        	return (JdbcOperations) field.get((JdbcOperations) this);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			logger.debug("Unable to obtain JobRepository from super via reflection");
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * No getter was declared in JobRepositoryFactoryBean so we need to use reflection to extract the private value
     * @return
     */
    private DataFieldMaxValueIncrementerFactory getIncrementerFactory(){
    	Field field = null;
		try {
			field = SimpleJobLauncher.class.getDeclaredField("incrementerFactory");
			field.setAccessible(true);
        	return (DataFieldMaxValueIncrementerFactory) field.get((DataFieldMaxValueIncrementerFactory) this);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			logger.debug("Unable to obtain JobRepository from super via reflection");
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * No getter was declared in JobRepositoryFactoryBean so we need to use reflection to extract the private value
     * @return
     */
    private String getDatabaseType(){
    	Field field = null;
		try {
			field = SimpleJobLauncher.class.getDeclaredField("databaseType");
			field.setAccessible(true);
        	return (String) field.get(this.toString());
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			logger.debug("Unable to obtain JobRepository from super via reflection");
			e.printStackTrace();
		}
		return null;
    }
    
    private int determineClobTypeToUse(String databaseType) {
        if (SYBASE == DatabaseType.valueOf(databaseType.toUpperCase())) {
                return Types.LONGVARCHAR;
        }
        else {
                return Types.CLOB;
        }
}


}
