/**
 *
 * Reads properties from database 
 *  
 * and adds them to Spring's MessageSource object
 * 
 * Executed once on app startup
 *  
 * @author Sasha Levchuk
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.io.FileUtils;
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
import edu.yu.einstein.wasp.model.UiField;
import edu.yu.einstein.wasp.service.impl.WaspMessageSourceImpl;

@Repository
public class DBResourceBundle implements ApplicationContextAware{

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private MessageSource messageSource;

	//static bridge to properties
	public static WaspMessageSourceImpl MESSAGE_SOURCE=null;
	
	private static final Logger log = Logger.getLogger(WaspMessageSourceImpl.class);
	
	private ApplicationContext applicationContext;
	
	
	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}
	

	// PostInitialized... b/c Resource and Workflow need uifields set within them
	@PostInitialize
	@Transactional
	public void init() {
		
		//apply latest updates from WEB-INF/uifield.update.sql file
		final StringBuffer sqlCurrent= new StringBuffer("");
		
		try {
			
			Resource uifield = this.applicationContext.getResource("classpath:uifield.update.sql");
			
			String sql = FileUtils.readFileToString(uifield.getFile());
			
			final String[] statements=sql.split(";\\s*\\n");
							
			for(String st:statements) {
				st=StringUtils.trim(st);
				
				if (StringUtils.isEmpty(st)) continue;
				
				if (StringUtils.containsIgnoreCase(st, "insert")
					|| StringUtils.containsIgnoreCase(st, "update")
					|| StringUtils.containsIgnoreCase(st, "delete")
					|| StringUtils.containsIgnoreCase(st, "truncate")
				) {
																				
					log.info("Executing ["+st+"]");
			
					sqlCurrent.setLength(0);sqlCurrent.append(st);
				
					entityManager.createNativeQuery(st).executeUpdate();
				}
			}
			
			log.info("Property table was initialized succesfully.");
			
		} catch (Throwable e) {
			log.error("Cannot execute sq; statement ["+sqlCurrent+"]",e);
			throw new IllegalStateException("Cannot execute sq; statement ["+sqlCurrent+"]",e);
		}
		
		List<UiField> uiFieldList = entityManager.createQuery("SELECT h FROM "	+ UiField.class.getName() + " h").getResultList();

		for (UiField f : uiFieldList) {

			String key = f.getArea() + "." + f.getName() + "."
					+ f.getAttrName();
			if (f.getLocale()==null || f.getLocale().length()!=5) {
				log.error("invalid locale, defaulting to US "+f);
				f.setLocale("en_US");
			}
			
			String lang = f.getLocale().substring(0, 2);
			String cntry = f.getLocale().substring(3);

			Locale locale = new Locale(lang, cntry);

			((WaspMessageSourceImpl) messageSource).addMessage(key, locale,
					f.getAttrValue());
	

		}

		MESSAGE_SOURCE=(WaspMessageSourceImpl)messageSource;//save handle to messageSource for easy access
	}
	

}
