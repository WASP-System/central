package edu.yu.einstein.wasp.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.exception.WaspMessageInitializationException;
import edu.yu.einstein.wasp.service.PropertiesLoadService;

@Service
@Transactional("entityManager")
public class PropertiesLoadServiceImpl implements ApplicationContextAware, PropertiesLoadService{
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private UserDao userDao;
	
	private static final Logger logger = LoggerFactory.getLogger(PropertiesLoadServiceImpl.class);
	
	private ApplicationContext applicationContext;
	
    private Set<String> languagesCurrentlyUsedForWaspMessages =new HashSet<String>();
	
    public Set<String> getLanguagesCurrentlyUsedForWaspMessages(){return languagesCurrentlyUsedForWaspMessages;}
    
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}

	public PropertiesLoadServiceImpl() {}
	
	@Override
	public void addMessagesToMessageSourceAndUiFields(String messageFilePattern){
		
		
		
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
					languagesCurrentlyUsedForWaspMessages.add(locale);
				} else{
					locale = "en_US";
					logger.warn("Cannot identify Locale from resource filename. Defaulting to 'en_US'");
					languagesCurrentlyUsedForWaspMessages.add(locale);
				}
			}
			
			try {
				InputStream is = messageFile.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF8"));

				String line = "";
				while ((line = br.readLine()) != null) {
					if (line.trim().isEmpty() || line.trim().startsWith("#"))
						continue;

					StringTokenizer keyValuePairs = new StringTokenizer(line, "=");
					String key = keyValuePairs.nextToken().trim();
					String attrValue = "";
					if (keyValuePairs.hasMoreTokens())
						attrValue =  keyValuePairs.nextToken(); //value =  keyValuePairs.nextToken().replaceFirst("^\\s*", "");
					while(keyValuePairs.hasMoreTokens())
						attrValue+="="+keyValuePairs.nextToken();
								StringTokenizer keyComponents = new StringTokenizer(key, "\\.");
					if (keyComponents.countTokens() < 3 || keyComponents.countTokens() > 4 ){
						logger.warn("Problem parsing message '"+line+"' from file '"+messageFile.getFilename()+"'. Key is not in the format 'com.my.domain.plugin.area.name.suffix'. Ignoring message!");
						continue;
					}

					String lang = locale.substring(0, 2);
					String cntry = locale.substring(3);

					Locale localeObj = new Locale(lang, cntry);
					
					((WaspMessageSourceImpl) messageSource).addMessage(key, localeObj,	attrValue);
				}
				br.close();

			} catch (IOException e) {
				throw new WaspMessageInitializationException("IO problem encountered reading resource '"+messageFile.getFilename()+"': "+e.getMessage());
			}		
			logger.info("Property table was initialized succesfully for file '"+messageFile.getFilename()+"'");
		}
		//updateTestData(); // TODO: remove from production code
	}
	

}
