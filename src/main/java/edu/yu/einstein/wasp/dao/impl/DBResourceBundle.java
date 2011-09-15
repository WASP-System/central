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

import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.stereotype.Repository;

import edu.yu.einstein.wasp.model.UiField;
import edu.yu.einstein.wasp.service.impl.WaspMessageSourceImpl;

@Repository
public class DBResourceBundle extends JpaDaoSupport {

	@Autowired
	private MessageSource messageSource;

	public static MessageSource MESSAGE_SOURCE=null;
	
	@PostConstruct
	public void init() {
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

			String lang = f.getLocale().substring(0, 2);
			String cntry = f.getLocale().substring(3);

			Locale locale = new Locale(lang, cntry);

			((WaspMessageSourceImpl) messageSource).addMessage(key, locale,
					f.getAttrValue());
	

		}

		MESSAGE_SOURCE=messageSource;//save handle to messageSource for easy access
	}

}