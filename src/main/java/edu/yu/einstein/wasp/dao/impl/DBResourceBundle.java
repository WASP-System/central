/**
 *
 * Reads properties from database and from messages*.properties files
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

import java.io.File;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.UiField;
import edu.yu.einstein.wasp.service.impl.WaspMessageSourceImpl;

@Repository
public class DBResourceBundle extends JpaDaoSupport implements ApplicationContextAware{

	@Autowired
	private MessageSource messageSource;

	//static bridge to access messageSource
	public static MessageSource MESSAGE_SOURCE=null;
	
	private final Logger log = Logger.getLogger(getClass());
	
	private ApplicationContext applicationContext;
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}
	
	@PostConstruct
	public void init() {
		
		//apply latest updates from WEB-INF/uifield.update.sql file
		final StringBuffer sqlCurrent= new StringBuffer("");
		
		try {
			//Resource uifield = this.applicationContext.getResource("file:WEB-INF/uifield.update.sql");
			Resource uifield = this.applicationContext.getResource("classpath:uifield.update.sql");
			
			String sql = FileUtils.readFileToString(uifield.getFile());

			//final String[] statements=StringUtils.splitByWholeSeparator(sql,";\n");
			final String[] statements=sql.split(";\\s*\\n");
			
			
			getJpaTemplate().execute(new JpaCallback() {

				public Object doInJpa(EntityManager em) throws PersistenceException {
					
					em.getTransaction().begin();
					
					//em.createNativeQuery(sqlFinal).executeUpdate();
				
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
						
							em.createNativeQuery(st).executeUpdate();
						}
					}
					
					em.getTransaction().commit();
					
	        		return null;
	        			
				}
			});
			
			log.info("Property table was initialized succesfully.");
			
		} catch (Throwable e) {
			log.error("Cannot execute sq; statement ["+sqlCurrent+"]",e);
			throw new IllegalStateException("Cannot execute sq; statement ["+sqlCurrent+"]",e);
		}
		
		Object res = getJpaTemplate().execute(new JpaCallback() {
			
			public Object doInJpa(EntityManager em) throws PersistenceException {
				Query q = em.createQuery("SELECT h FROM "
						+ UiField.class.getName() + " h");
				return q.getResultList();
			}

		});
/*
		for (String localeStr : WaspController.LOCALES.keySet()) {
			Locale locale = new Locale(localeStr);

			PropertyResourceBundle b = (PropertyResourceBundle) ResourceBundle.getBundle("messages", locale);
			Enumeration bundleKeys = b.getKeys();

			while (bundleKeys.hasMoreElements()) {
				String key = (String) bundleKeys.nextElement();
				String value = b.getString(key);
				((WaspMessageSourceImpl) messageSource).addMessage(key, locale,value);
			}
		}
*/
		for (UiField f : ((List<UiField>) res)) {

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

		MESSAGE_SOURCE=messageSource;//save handle to messageSource for easy access
	}

}