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

package edu.yu.einstein.wasp.dao.impl;

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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import util.spring.PostInitialize;
import edu.yu.einstein.wasp.exception.WaspMessageInitializationException;
import edu.yu.einstein.wasp.service.impl.WaspMessageSourceImpl;

@Repository
public class DBResourceBundle implements ApplicationContextAware{

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private MessageSource messageSource;

	//static bridge to properties
	public static WaspMessageSourceImpl MESSAGE_SOURCE=null;
	
	private static final Logger logger = Logger.getLogger(WaspMessageSourceImpl.class);
	
	private ApplicationContext applicationContext;
	
	
	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}
	

	// PostInitialized... b/c Resource and Workflow need uifields set within them
	@PostInitialize
	@Transactional
	public void init() {
		
		// process uifields initialization file uifield_init.sql if present
		final String uiFieldInitFileName = "uifield_init.sql";
		List<Resource> messageFiles = new ArrayList<Resource>();
		Resource uiFieldInitResource = this.applicationContext.getResource("classpath:/"+uiFieldInitFileName);	
		try {
			InputStream is = uiFieldInitResource.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			logger.info("Found '"+uiFieldInitFileName+"'. Executing SQL statements within...");
			String line;
			
			while ((line = br.readLine()) != null) {
				if (line.trim().isEmpty() || line.trim().startsWith("--"))
					continue;
				if (StringUtils.startsWithIgnoreCase(line.trim(), "insert")
						|| StringUtils.startsWithIgnoreCase(line.trim(), "update")
						|| StringUtils.startsWithIgnoreCase(line.trim(), "delete")
						|| StringUtils.startsWithIgnoreCase(line.trim(), "truncate")
					){
					// line is sql 
					logger.info("Executing ["+line+"]");				
					try{
						entityManager.createNativeQuery(line).executeUpdate();
					} catch(Throwable e){
						throw new WaspMessageInitializationException("Problem executing sql statement  ["+line+"] : "+e.getMessage());
					}
				}
			}
			br.close();
		} catch (IOException e) {
			throw new WaspMessageInitializationException("IO problem encountered reading resource '"+uiFieldInitFileName+"': "+e.getMessage());
		}

		try{
			for (Resource messageFile: this.applicationContext.getResources("classpath*:/i18n/**/*messages_*.properties"))
				messageFiles.add(messageFile);
		} catch(IOException e){
			throw new WaspMessageInitializationException("IO problem encountered getting resources from 'classpath*:/i18n/**/*messages_*.properties': "+e.getMessage());
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
				} else if(!messageFile.getFilename().contains(uiFieldInitFileName)){
					locale = "en_US";
					logger.warn("Cannot identify Locale from resource filename. Defaulting to 'en_US'");
				}
			}
			
			try {
				InputStream is = messageFile.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));

				String line;
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
					if (keyComponents.countTokens() != 3){
						logger.warn("Problem parsing message '"+line+"' from file '"+messageFile.getFilename()+"'. Key is not in the format 'area.name.suffix'. Ignoring message!");
						continue;
					}
					String area = keyComponents.nextToken();
					String name = keyComponents.nextToken();
					String attrName = keyComponents.nextToken();
					String sql="insert into uifield(locale,area,name,attrname,attrvalue,lastupduser) values('"+locale+"', '"+area+"', '"+name+"', '"+attrName+"', '"+value+"', 1)";
					try{
						entityManager.createNativeQuery(sql).executeUpdate();
					} catch(Throwable e){
						throw new WaspMessageInitializationException("Problem executing sql statement  ["+sql+"] : "+e.getMessage());
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
			logger.info("Property table was initialized succesfully for file '"+messageFile.getFilename()+"'");

		}

		MESSAGE_SOURCE = (WaspMessageSourceImpl) messageSource;//save handle to messageSource for easy access
	}
	

}
