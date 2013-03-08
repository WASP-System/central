package edu.yu.einstein.wasp.daemon.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.spi.PersistenceUnitInfo;

import org.hibernate.cfg.Configuration;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.WaspModel;

@ContextConfiguration(locations = { "classpath:META-INF/spring/daemon-launch-context.xml" })
@TransactionConfiguration(defaultRollback = true)
public class SchemaExportTest extends  AbstractTestNGSpringContextTests {
	
	private Logger logger = LoggerFactory.getLogger(SchemaExportTest.class);

	@Autowired //( name = "&entityManagerFactory")
	private LocalContainerEntityManagerFactoryBean entityManagerFactory;
	
	@Autowired
    private ApplicationContext applicationContext;

	@Test(groups = { "schema" })
	public void exportDatabaseSchema() {
		PersistenceUnitInfo persistenceUnitInfo = entityManagerFactory.getPersistenceUnitInfo();
		Map<String, Object> jpaPropertyMap = entityManagerFactory.getJpaPropertyMap();

		Configuration configuration = new Ejb3Configuration().configure(persistenceUnitInfo, jpaPropertyMap).getHibernateConfiguration();
		
		String dialect = configuration.getProperty("hibernate.dialect");
		
		if (dialect.equals("org.hibernate.dialect.MySQLDialect")) {
			configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLInnoDBDialect");
		}
		
		SchemaExport schema = new SchemaExport(configuration);
		schema.setOutputFile("target/wasp-database.sql");
		schema.setDelimiter(";");
		schema.create(false, false);
	}
	
	@Test(groups = { "schema" })
	public void updateTestData(){
		Map<String, WaspDao> daoBeans = applicationContext.getBeansOfType(WaspDao.class);
		for (WaspDao dao : daoBeans.values()){
			for (WaspModel model : (List<WaspModel>) dao.findAll()){
				try {
					Method getter = null;
					Method setter = null;
					if (model.getClass().getName().endsWith("Meta")){
						getter = model.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("getUUID");
						setter = model.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("setUUID", UUID.class);
					} else {
						getter = model.getClass().getSuperclass().getSuperclass().getDeclaredMethod("getUUID");
						setter = model.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setUUID", UUID.class);
					}
					setter.setAccessible(true);
					getter.setAccessible(true);
					UUID uuid = (UUID) getter.invoke(model);
					if (uuid == null){
						logger.debug("setting UUID for instance of class " + model.getClass().getName());
						setter.invoke(model, UUID.randomUUID());
						dao.merge(model);
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(); 
				}
			}
		}
	}
}
