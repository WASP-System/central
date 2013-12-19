package edu.yu.einstein.wasp.daemon.test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.sql.DataSource;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;


public class BatchDatabaseIntegrationTest extends AbstractTestNGSpringContextTests {
	
	@Autowired
	protected JobLauncher jobLauncher;
	
	@Autowired 
	protected JobRegistry jobRegistry;
	
	@Autowired
	protected JobRepository jobRepository;
	
	@Autowired
	protected JobExplorer jobExplorer;
	
	@Autowired
	private DataSource dataSource;
	
	@Value("${testDb.resetSchema.script}")
	private String dropTablesScript;
	
	@Value("${testDb.schema.script}")
	private String setupSchemaScript;
	
	private void executeSQL(Resource resource) throws DataAccessException, IOException{
		JdbcTemplate  jdbcTemplate = new JdbcTemplate(dataSource);
		BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
		String line;
		StringBuilder sql = new StringBuilder();
		while ((line = reader.readLine()) != null){
			line = line.trim();
			if (!line.startsWith("--") && !line.isEmpty()){
				sql.append(line);
				if (line.endsWith(";")){
					logger.debug("Executing sql: " + sql);
					jdbcTemplate.execute(sql.toString());
					sql = new StringBuilder();
				}
			}
		}
	}
	
	protected void cleanDB() throws Exception{
		logger.debug("Resetting DB Schema...");
		Resource dropScript = new ClassPathResource(dropTablesScript.replace("classpath:", ""));
		Resource setupScript = new ClassPathResource(setupSchemaScript.replace("classpath:", ""));
		executeSQL(dropScript);
		executeSQL(setupScript);
	}
	
	protected void setup(){
		logger.debug("Validating autowired properties are not null...");
		Assert.assertNotNull(jobRepository);
		Assert.assertNotNull(jobLauncher);
		Assert.assertNotNull(jobRegistry);
		Assert.assertNotNull(jobExplorer);
		Assert.assertNotNull(dataSource);
		Assert.assertNotNull(dropTablesScript);
		Assert.assertNotNull(setupSchemaScript);
	}
	
	protected boolean stopRunningJobExecutions(){
		logger.debug("Stopping any running job executions...");
		boolean returnVal = true;
		for (String jobName : jobExplorer.getJobNames()){
			for (JobExecution je : jobExplorer.findRunningJobExecutions(jobName)){
				try{
					je.stop();
				} catch (Exception e){}
			}
		}
		for (String jobName : jobExplorer.getJobNames()){
			int repeat = 0;
			while (!jobExplorer.findRunningJobExecutions(jobName).isEmpty() && repeat++ < 10){
				try{
					Thread.sleep(500); // allow some time for stopping
				} catch (InterruptedException e){};
			}
			if (repeat == 10)
				returnVal = false;
			}
		return returnVal;
	}

}
