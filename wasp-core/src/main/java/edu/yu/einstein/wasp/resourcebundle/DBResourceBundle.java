/**
 *
 * Reads properties from messages files and adds them to Spring's MessageSource object and uifields table of the database
 * 
 * Executed once on app startup
 *  
 * @author asmclellan
 *
 *
 **/

package edu.yu.einstein.wasp.resourcebundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;

import edu.yu.einstein.wasp.exception.WaspMessageInitializationException;
import edu.yu.einstein.wasp.service.PropertiesLoadService;
import edu.yu.einstein.wasp.service.WaspSqlService;
import edu.yu.einstein.wasp.service.impl.WaspMessageSourceImpl;

/**
 * 
 * @author asmclellan
 *
 */
public class DBResourceBundle implements ApplicationContextAware{

	@Autowired
	private PropertiesLoadService propertiesLoadService;
	
	@Autowired
	private WaspSqlService waspSqlService;
	
	@Autowired
	private MessageSource messageSource;
	
	//static bridge to properties
	public static WaspMessageSourceImpl MESSAGE_SOURCE = null;
	
	private static final Logger logger = LoggerFactory.getLogger(DBResourceBundle.class);
	
	private boolean runSQL = false;
	
	private String sqlInitFile;
	
	private String messageFilePattern;
	
	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	
	public DBResourceBundle(String messageFilePattern) {
		this.messageFilePattern = messageFilePattern;
	}

	@PostConstruct
	public void init() throws Exception {
		if (runSQL && sqlInitFile != null)
			executeSqlFromFile();
		propertiesLoadService.addMessagesToMessageSourceAndUiFields(messageFilePattern);
		MESSAGE_SOURCE = (WaspMessageSourceImpl) messageSource;//save handle to messageSource for easy access
	}

	public boolean isRunSQL() {
		return runSQL;
	}


	public void setRunSQL(boolean runSQL) {
		this.runSQL = runSQL;
	}

	public String getSqlInitFile() {
		return sqlInitFile;
	}

	public void setSqlInitFile(String sqlInitFile) {
		this.sqlInitFile = sqlInitFile;
	}


	public String getMessageFilePattern() {
		return messageFilePattern;
	}


	public void setMessageFilePattern(String messageFilePattern) {
		this.messageFilePattern = messageFilePattern;
	}
	
	private void executeSqlFromFile(){
		List<String> updateList = null;
		
		// process uifields initialization file uifield_init.sql if present
		try {
			Resource uiFieldInitResource = this.applicationContext.getResource(sqlInitFile);	 
			InputStream is = uiFieldInitResource.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			logger.info("Found '"+sqlInitFile+"'. Executing SQL statements within...");
			String line = "";
			updateList = new ArrayList<String>();
			while ((line = br.readLine()) != null) {
				if (line.trim().isEmpty() || line.trim().startsWith("--"))
					continue;
				if (StringUtils.startsWithIgnoreCase(line.trim(), "insert")
						|| StringUtils.startsWithIgnoreCase(line.trim(), "update")
						|| StringUtils.startsWithIgnoreCase(line.trim(), "delete")
						|| StringUtils.startsWithIgnoreCase(line.trim(), "truncate")
					){
						updateList.add(line.replace(";",""));
				}
			}
			br.close();
		} catch (IOException e) {
			throw new WaspMessageInitializationException("IO problem encountered reading resource '"+sqlInitFile+"': "+e.getMessage(), e);
		}
		 	
		// execute sql statements
		try{
			waspSqlService.executeQueryUpdateOnList(updateList);
		} catch(Exception e){
			throw new WaspMessageInitializationException("Problem executing sql updates : "+e.getMessage(), e);
		}
	}

	
	

	

}
