package edu.yu.einstein.wasp.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.UiFieldDao;
import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.exception.WaspMessageInitializationException;
import edu.yu.einstein.wasp.model.UiField;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.WaspModel;
import edu.yu.einstein.wasp.service.PropertiesLoadService;
import edu.yu.einstein.wasp.service.WaspSqlService;

@Service
@Transactional("entityManager")
public class PropertiesLoadServiceImpl implements ApplicationContextAware, PropertiesLoadService{
	
	@Autowired
	private WaspSqlService waspSqlService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UiFieldDao uiFieldDao;
	
	private static final Logger logger = LoggerFactory.getLogger(PropertiesLoadServiceImpl.class);
	
	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}

	public PropertiesLoadServiceImpl() {}
	
	@Override
	public void addMessagesToMessageSourceAndUiFields(String messageFilePattern){
		
		updateTestData(); // TODO: remove from production code
		
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
					String domain = "";
					for (int i=0; i < keyComponents.countTokens() - 3; i++)
							domain += keyComponents.nextToken()+".";
					domain = StringUtils.chop(domain);
					String area = keyComponents.nextToken();
					String name = keyComponents.nextToken();
					String attrName = keyComponents.nextToken();
					
					Map<String, String> m = new HashMap<String, String>();
					//m.put("domain", domain);
					m.put("area", area);
					m.put("locale", locale);
					m.put("name", name);
					m.put("attrName", attrName);
					List<UiField> matches = uiFieldDao.findByMap(m);
					if (matches.size() == 1){
						// already exists so edit if attrValue changed
						UiField existingUiField = matches.get(0);
						if (existingUiField.getAttrValue() != null && !existingUiField.getAttrValue().equals(attrValue))
							existingUiField.setAttrValue(attrValue);
					} else {
						if (matches.size() > 1){
							logger.warn("Got more than one UiField matching provided parameters. Going to remove them and add a fresh one");
							for (UiField oldUiField : matches)
								uiFieldDao.remove(oldUiField);
						}
						// uiField not yet existing create new
						UiField uiField = new UiField();
						uiField.setName(name);
						//uiField.setDomain(domain);
						uiField.setArea(area);
						uiField.setAttrName(attrName);
						uiField.setAttrValue(attrValue);
						uiField.setLocale(locale);
						uiFieldDao.persist(uiField);
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
	}
	
		// TODO: remove from production code
		private void updateTestData(){
			Map<String, WaspDao> daoBeans = applicationContext.getBeansOfType(WaspDao.class);
			for (WaspDao dao : daoBeans.values()){
				for (WaspModel model : (List<WaspModel>) dao.findAll()){
					try {
						if (model.getClass().getSimpleName().startsWith("UiField"))
							continue;
						Method getterUUID = null;
						Method getterUpdated = null;
						Method getterCreated = null;
						Method setterUUID = null;
						Method setterUpdated = null;
						Method setterCreated = null;
						Method getterUpdatedUser = null;
						Method setterUpdatedUser = null;
						if (model.getClass().getName().endsWith("Meta")){
							getterUUID = model.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("getUUID"); 
							setterUUID = model.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("setUUID", UUID.class);
							getterUpdated = model.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("getUpdated"); 
							setterUpdated = model.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("setUpdated", Date.class);
							getterCreated = model.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("getCreated"); 
							setterCreated = model.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("setCreated", Date.class);
							getterUpdatedUser = model.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("getLastUpdatedByUser"); 
							setterUpdatedUser = model.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("setLastUpdatedByUser", User.class);
						} else {
							getterUUID = model.getClass().getSuperclass().getSuperclass().getDeclaredMethod("getUUID");
							setterUUID = model.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setUUID", UUID.class);
							getterUpdated = model.getClass().getSuperclass().getSuperclass().getDeclaredMethod("getUpdated"); 
							setterUpdated = model.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setUpdated", Date.class);
							getterCreated = model.getClass().getSuperclass().getSuperclass().getDeclaredMethod("getCreated"); 
							setterCreated = model.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setCreated", Date.class);
							getterUpdatedUser = model.getClass().getSuperclass().getSuperclass().getDeclaredMethod("getLastUpdatedByUser"); 
							setterUpdatedUser = model.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setLastUpdatedByUser", User.class);
						}
						setterUUID.setAccessible(true);
						setterCreated.setAccessible(true);
						UUID uuid = (UUID) getterUUID.invoke(model);
						if (uuid == null){
							logger.debug("setting UUID for instance of class " + model.getClass().getName());
							setterUUID.invoke(model, UUID.randomUUID());
						}
						Date updated = (Date) getterUpdated.invoke(model);
						Date created = (Date) getterCreated.invoke(model);
						if (updated == null){
							updated = new Date();
							setterUpdated.invoke(model, updated);
						}
						if (created == null)
							setterCreated.invoke(model, updated);
						User lastUpdatedUser = (User) getterUpdatedUser.invoke(model);
						if (lastUpdatedUser == null)
							setterUpdatedUser.invoke(model, userDao.findById(1));

					} catch (Exception e) {
						throw new RuntimeException(e); 
					}
				}
			}
		}

}
