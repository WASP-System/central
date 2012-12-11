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
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import edu.yu.einstein.wasp.service.WaspSqlService;
import edu.yu.einstein.wasp.service.impl.WaspMessageSourceImpl;

public class DBResourceBundle implements ApplicationContextAware{

	@Autowired
	private WaspSqlService waspSqlService;
	
	@Autowired
	private MessageSource messageSource;

	//static bridge to properties
	public static WaspMessageSourceImpl MESSAGE_SOURCE=null;
	
	private static final Logger logger = LoggerFactory.getLogger(WaspMessageSourceImpl.class);
	
	private ApplicationContext applicationContext;
	
	private boolean runSQL = false;
	
	private String sqlInitFile;
	
	private String messageFilePattern;
	


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}
	
	
	public DBResourceBundle(String messageFilePattern) {
		this.messageFilePattern = messageFilePattern;
	}


	@PostConstruct
	public void init() throws Exception {
		List<String> updateList = null;
		if (runSQL && sqlInitFile != null){
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
							updateList.add(line);
					}
				}
				br.close();
			} catch (IOException e) {
				throw new WaspMessageInitializationException("IO problem encountered reading resource '"+sqlInitFile+"': "+e.getMessage(), e);
			}
			 	
			// execute sql statements
			try{
				waspSqlService.executeNativeSqlUpdateOnList(updateList);
			} catch(Exception e){
				throw new WaspMessageInitializationException("Problem executing sql updates : "+e.getMessage(), e);
			}
		}
		List<Resource> messageFiles = new ArrayList<Resource>();
		try{
			for (Resource messageFile: this.applicationContext.getResources(messageFilePattern))
				messageFiles.add(messageFile);
		} catch(IOException e){
			throw new WaspMessageInitializationException("IO problem encountered getting resources from '" + messageFilePattern + "': "+e.getMessage());
		}
					
		for (Resource messageFile: messageFiles){
			logger.info("Processing message property file '"+messageFile+"'");
			String locale = "";
			Pattern pattern = Pattern.compile("messages_([A-Za-z_]+)\\.properties$");
			Matcher matcher = pattern.matcher(messageFile.getFilename());
			if (matcher.find() && matcher.groupCount() == 1){
				if (matcher.group(1) != null){
					locale = matcher.group(1);
					logger.info("Current properties have locale of "+locale);
				} else{
					locale = "en_US";
					logger.warn("Cannot identify Locale from resource filename. Defaulting to 'en_US'");
				}
			}
			
			try {
				InputStream is = messageFile.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));

				String line = "";
				updateList = new ArrayList<String>(); 
				while ((line = br.readLine()) != null) {
					if (line.trim().isEmpty() || line.trim().startsWith("#"))
						continue;

					StringTokenizer keyValuePairs = new StringTokenizer(line, "=");
					String key = keyValuePairs.nextToken().trim();
					String value = "";
					if (keyValuePairs.hasMoreTokens())
						value =  keyValuePairs.nextToken(); //value =  keyValuePairs.nextToken().replaceFirst("^\\s*", "");
					while(keyValuePairs.hasMoreTokens())
						value+="="+keyValuePairs.nextToken();
								StringTokenizer keyComponents = new StringTokenizer(key, "\\.");
					if (keyComponents.countTokens() < 3 || keyComponents.countTokens() > 4 ){
						logger.warn("Problem parsing message '"+line+"' from file '"+messageFile.getFilename()+"'. Key is not in the format 'com.my.domain.plugin.area.name.suffix'. Ignoring message!");
						continue;
					}
					String domain = "";
					for (int i=0; i < keyComponents.countTokens() - 3; i++)
							domain += keyComponents.nextToken()+".";
					domain = StringUtils.chop(domain);
					String area = keyComponents.nextToken();
					String name = keyComponents.nextToken();
					String attrName = keyComponents.nextToken();
					if (runSQL){
						String sql="insert into uifield(locale,domain,area,name,attrname,attrvalue,lastupduser) values('"+locale+"', '"+domain+"', '"+area+"', '"+name+"', '"+attrName+"', '"+value+"', 1)";
						updateList.add(sql);
					}
					String lang = locale.substring(0, 2);
					String cntry = locale.substring(3);

					Locale localeObj = new Locale(lang, cntry);
					
					((WaspMessageSourceImpl) messageSource).addMessage(key, localeObj,	value);
				}
				br.close();

			} catch (IOException e) {
				throw new WaspMessageInitializationException("IO problem encountered reading resource '"+messageFile.getFilename()+"': "+e.getMessage());
			}		
			if (runSQL){
				// execute sql statements
				try{
					waspSqlService.executeNativeSqlUpdateOnList(updateList);
				} catch(Exception e){
					throw new WaspMessageInitializationException("Problem executing sql updates : "+e.getMessage(), e);
				}
			}
			logger.info("Property table was initialized succesfully for file '"+messageFile.getFilename()+"'");

		}

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
	

}
