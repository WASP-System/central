package edu.yu.einstein.wasp.daemon.test;

import java.util.Map;

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
}
