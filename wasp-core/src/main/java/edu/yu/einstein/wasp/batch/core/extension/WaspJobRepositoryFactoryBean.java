/*
 * Copyright 2006-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.yu.einstein.wasp.batch.core.extension;

import static org.springframework.batch.support.DatabaseType.SYBASE;

import java.sql.Types;

import javax.sql.DataSource;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.batch.core.repository.ExecutionContextSerializer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.dao.AbstractJdbcBatchMetadataDao;
import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.core.repository.dao.JdbcExecutionContextDao;
import org.springframework.batch.core.repository.dao.JdbcJobExecutionDao;
import org.springframework.batch.core.repository.dao.JdbcJobInstanceDao;
import org.springframework.batch.core.repository.dao.JdbcStepExecutionDao;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.batch.core.repository.dao.XStreamExecutionContextStringSerializer;
import org.springframework.batch.core.repository.support.AbstractJobRepositoryFactoryBean;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.repository.support.SimpleJobRepository;
import org.springframework.batch.item.database.support.DataFieldMaxValueIncrementerFactory;
import org.springframework.batch.item.database.support.DefaultDataFieldMaxValueIncrementerFactory;
import org.springframework.batch.support.DatabaseType;
import org.springframework.batch.support.PropertiesConverter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.jdbc.support.lob.OracleLobHandler;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import edu.yu.einstein.wasp.batch.core.extension.dao.JdbcWaspStepExecutionDao;

/**
 * Modifed from {@link AbstractJobRepositoryFactoryBean} and {@link JobRepositoryFactoryBean} 2.2.2.RELEASE with modifications
 * 
 * (https://github.com/spring-projects/spring-batch/blob/2.2.2.RELEASE/spring-batch-core/src/main/java/org/springframework/batch/core/repository/support/AbstractJobRepositoryFactoryBean.java)
 * (https://github.com/spring-projects/spring-batch/blob/2.2.2.RELEASE/spring-batch-core/src/main/java/org/springframework/batch/core/repository/support/JobRepositoryFactoryBean.java)
 * 
 * @author asmclellan
 *
 */
public class WaspJobRepositoryFactoryBean implements FactoryBean<JobRepositoryWasp>, InitializingBean {
	
	private static Logger logger = LoggerFactory.getLogger(WaspJobRepositoryFactoryBean.class);
	
	private PlatformTransactionManager transactionManager;

    private ProxyFactory proxyFactory;

    private String isolationLevelForCreate = DEFAULT_ISOLATION_LEVEL;

    private boolean validateTransactionState = true;
    
    private DataSource dataSource;

    private JdbcOperations jdbcTemplate;

    private String databaseType;

    private String tablePrefix = AbstractJdbcBatchMetadataDao.DEFAULT_TABLE_PREFIX;

    private DataFieldMaxValueIncrementerFactory incrementerFactory;

    private int maxVarCharLength = AbstractJdbcBatchMetadataDao.DEFAULT_EXIT_MESSAGE_LENGTH;

    private LobHandler lobHandler;

    private ExecutionContextSerializer serializer;

    /**
     * Default value for isolation level in create* method.
     */
    private static final String DEFAULT_ISOLATION_LEVEL = "ISOLATION_SERIALIZABLE";
    
    public WaspJobRepositoryFactoryBean(DataSource dataSource, PlatformTransactionManager transactionManager){
		setDataSource(dataSource);
		setTransactionManager(transactionManager);
	}
    
    @Override
    public void afterPropertiesSet() throws Exception {

            Assert.notNull(dataSource, "DataSource must not be null.");
            Assert.notNull(transactionManager, "TransactionManager must not be null.");

            jdbcTemplate = new JdbcTemplate(dataSource);

            if (incrementerFactory == null) {
                    incrementerFactory = new DefaultDataFieldMaxValueIncrementerFactory(dataSource);
            }

            if (databaseType == null) {
                    databaseType = DatabaseType.fromMetaData(dataSource).name();
                    logger.info("No database type set, using meta data indicating: " + databaseType);
            }

            if (lobHandler == null && databaseType.equalsIgnoreCase(DatabaseType.ORACLE.toString())) {
                    lobHandler = new OracleLobHandler();
            }

            if(serializer == null) {
                    XStreamExecutionContextStringSerializer defaultSerializer = new XStreamExecutionContextStringSerializer();
                    defaultSerializer.afterPropertiesSet();

                    serializer = defaultSerializer;
            }

            Assert.isTrue(incrementerFactory.isSupportedIncrementerType(databaseType), "'" + databaseType
                            + "' is an unsupported database type.  The supported database types are "
                            + StringUtils.arrayToCommaDelimitedString(incrementerFactory.getSupportedIncrementerTypes()));

            initializeProxy();
    }


    /**
     * Flag to determine whether to check for an existing transaction when a
     * JobExecution is created. Defaults to true because it is usually a
     * mistake, and leads to problems with restartability and also to deadlocks
     * in multi-threaded steps.
     *
     * @param validateTransactionState the flag to set
     */
    public void setValidateTransactionState(boolean validateTransactionState) {
            this.validateTransactionState = validateTransactionState;
    }

    /**
     * public setter for the isolation level to be used for the transaction when
     * job execution entities are initially created. The default is
     * ISOLATION_SERIALIZABLE, which prevents accidental concurrent execution of
     * the same job (ISOLATION_REPEATABLE_READ would work as well).
     *
     * @param isolationLevelForCreate the isolation level name to set
     *
     * @see SimpleJobRepository#createJobExecution(String,
     * org.springframework.batch.core.JobParameters)
     */
    public void setIsolationLevelForCreate(String isolationLevelForCreate) {
            this.isolationLevelForCreate = isolationLevelForCreate;
    }

    /**
     * Public setter for the {@link PlatformTransactionManager}.
     * @param transactionManager the transactionManager to set
     */
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
            this.transactionManager = transactionManager;
    }

    /**
     * The transaction manager used in this factory. Useful to inject into steps
     * and jobs, to ensure that they are using the same instance.
     *
     * @return the transactionManager
     */
    public PlatformTransactionManager getTransactionManager() {
            return transactionManager;
    }

    

    private void initializeProxy() throws Exception {
            if (proxyFactory == null) {
                    proxyFactory = new ProxyFactory();
                    TransactionInterceptor advice = new TransactionInterceptor(transactionManager,
                                    PropertiesConverter.stringToProperties("create*=PROPAGATION_REQUIRES_NEW,"
                                                    + isolationLevelForCreate + "\ngetLastJobExecution*=PROPAGATION_REQUIRES_NEW,"
                                                    + isolationLevelForCreate + "\n*=PROPAGATION_REQUIRED"));
                    if (validateTransactionState) {
                            DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(new MethodInterceptor() {
                                    @Override
                                    public Object invoke(MethodInvocation invocation) throws Throwable {
                                            if (TransactionSynchronizationManager.isActualTransactionActive()) {
                                                    throw new IllegalStateException(
                                                                    "Existing transaction detected in JobRepository. "
                                                                                    + "Please fix this and try again (e.g. remove @Transactional annotations from client).");
                                            }
                                            return invocation.proceed();
                                    }
                            });
                            NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
                            pointcut.addMethodName("create*");
                            advisor.setPointcut(pointcut);
                            proxyFactory.addAdvisor(advisor);
                    }
                    proxyFactory.addAdvice(advice);
                    proxyFactory.setProxyTargetClass(false);
                    proxyFactory.addInterface(JobRepositoryWasp.class);
                    proxyFactory.setTarget(getTarget());
            }
    }

    



    /**
     * A custom implementation of the {@link ExecutionContextSerializer}.
     * The default, if not injected, is the {@link XStreamExecutionContextStringSerializer}.
     *
     * @param serializer
     * @see ExecutionContextSerializer
     */
    public void setSerializer(ExecutionContextSerializer serializer) {
            this.serializer = serializer;
    }

    /**
     * A special handler for large objects. The default is usually fine, except
     * for some (usually older) versions of Oracle. The default is determined
     * from the data base type.
     *
     * @param lobHandler the {@link LobHandler} to set
     *
     * @see LobHandler
     */
    public void setLobHandler(LobHandler lobHandler) {
            this.lobHandler = lobHandler;
    }

    /**
     * Public setter for the length of long string columns in database. Do not
     * set this if you haven't modified the schema. Note this value will be used
     * for the exit message in both {@link JdbcJobExecutionDao} and
     * {@link JdbcStepExecutionDao} and also the short version of the execution
     * context in {@link JdbcExecutionContextDao} . For databases with
     * multi-byte character sets this number can be smaller (by up to a factor
     * of 2 for 2-byte characters) than the declaration of the column length in
     * the DDL for the tables.
     *
     * @param maxVarCharLength the exitMessageLength to set
     */
    public void setMaxVarCharLength(int maxVarCharLength) {
            this.maxVarCharLength = maxVarCharLength;
    }

    /**
     * Public setter for the {@link DataSource}.
     * @param dataSource a {@link DataSource}
     */
    public void setDataSource(DataSource dataSource) {
            this.dataSource = dataSource;
    }

    /**
     * Sets the database type.
     * @param dbType as specified by
     * {@link DefaultDataFieldMaxValueIncrementerFactory}
     */
    public void setDatabaseType(String dbType) {
            this.databaseType = dbType;
    }

    /**
     * Sets the table prefix for all the batch meta-data tables.
     * @param tablePrefix
     */
    public void setTablePrefix(String tablePrefix) {
            this.tablePrefix = tablePrefix;
    }

    public void setIncrementerFactory(DataFieldMaxValueIncrementerFactory incrementerFactory) {
            this.incrementerFactory = incrementerFactory;
    }



    private JobInstanceDao createJobInstanceDao() throws Exception {
            JdbcJobInstanceDao dao = new JdbcJobInstanceDao();
            dao.setJdbcTemplate(jdbcTemplate);
            dao.setJobIncrementer(incrementerFactory.getIncrementer(databaseType, tablePrefix + "JOB_SEQ"));
            dao.setTablePrefix(tablePrefix);
            dao.afterPropertiesSet();
            return dao;
    }

    private JobExecutionDao createJobExecutionDao() throws Exception {
            JdbcJobExecutionDao dao = new JdbcJobExecutionDao();
            dao.setJdbcTemplate(jdbcTemplate);
            dao.setJobExecutionIncrementer(incrementerFactory.getIncrementer(databaseType, tablePrefix
                            + "JOB_EXECUTION_SEQ"));
            dao.setTablePrefix(tablePrefix);
            dao.setClobTypeToUse(determineClobTypeToUse(this.databaseType));
            dao.setExitMessageLength(maxVarCharLength);
            dao.afterPropertiesSet();
            return dao;
    }

    private StepExecutionDao createStepExecutionDao() throws Exception {
            JdbcStepExecutionDao dao = new JdbcWaspStepExecutionDao();
            dao.setJdbcTemplate(jdbcTemplate);
            dao.setStepExecutionIncrementer(incrementerFactory.getIncrementer(databaseType, tablePrefix
                            + "STEP_EXECUTION_SEQ"));
            dao.setTablePrefix(tablePrefix);
            dao.setClobTypeToUse(determineClobTypeToUse(this.databaseType));
            dao.setExitMessageLength(maxVarCharLength);
            dao.afterPropertiesSet();
            return dao;
    }

    private ExecutionContextDao createExecutionContextDao() throws Exception {
            JdbcExecutionContextDao dao = new JdbcExecutionContextDao();
            dao.setJdbcTemplate(jdbcTemplate);
            dao.setTablePrefix(tablePrefix);
            dao.setClobTypeToUse(determineClobTypeToUse(this.databaseType));
            dao.setSerializer(serializer);

            if (lobHandler != null) {
                    dao.setLobHandler(lobHandler);
            }

            dao.afterPropertiesSet();
            // Assume the same length.
            dao.setShortContextLength(maxVarCharLength);
            return dao;
    }

    private int determineClobTypeToUse(String databaseType) {
            if (SYBASE == DatabaseType.valueOf(databaseType.toUpperCase())) {
                    return Types.LONGVARCHAR;
            }
            else {
                    return Types.CLOB;
            }
    }
    
    private Object getTarget() throws Exception {
        return new WaspBatchJobRepository(createJobInstanceDao(), createJobExecutionDao(), createStepExecutionDao(),
                        createExecutionContextDao());
	}
    
    /**
     * Convenience method for clients to grab the {@link JobRepository} without
     * a cast.
     * @return the {@link JobRepository} from {@link #getObject()}
     * @throws Exception if the repository could not be created
     */
    public JobRepositoryWasp getJobRepository() throws Exception {
            return getObject();
    }
	
	@Override
	public JobRepositoryWasp getObject() throws Exception {
	        if (proxyFactory == null) {
	                afterPropertiesSet();
	        }
	        return (JobRepositoryWasp) proxyFactory.getProxy();
	}
	
	/**
	 * The type of object to be returned from {@link #getObject()}.
	 *
	 * @return JobRepository.class
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<JobRepositoryWasp> getObjectType() {
	        return JobRepositoryWasp.class;
	}
	
	@Override
	public boolean isSingleton() {
	        return true;
	}


}
