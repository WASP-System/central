package edu.yu.einstein.wasp.load;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;

import util.spring.PostInitialize;
import edu.yu.einstein.wasp.model.UiField;
import edu.yu.einstein.wasp.service.UiFieldService;
import edu.yu.einstein.wasp.service.impl.WaspMessageSourceImpl;


/**
 * base clase for all the wasp loaders
 * full of convience methods like
 *	- updateUiFields
 *
 */

@Transactional
public abstract class WaspLoadService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	protected UiFieldService uiFieldService;

	protected String iname; 
	public void setIname(String iname) {this.iname = iname; }

	protected String name; 
	public void setName(String name) {this.name = name; }

	protected List<UiField> baseUiFields; 
	public void setBaseUiFields(List<UiField> uiFields) {this.baseUiFields = uiFields; }

	protected List<UiField> uiFields; 
	public void setUiFields(List<UiField> uiFields) {this.uiFields = uiFields; }

	protected static final Logger log = Logger.getLogger(WaspLoadService.class);

	public WaspLoadService (){};


	/* override me.... */
	@Transactional
	@PostInitialize 
	public void postInitialize() {
	}


	/**
	 * updates the UiFields for that object
	 * assumes htat UIFields were truncated on container load 
	 * 
	 * adds them to the cached messageSource
	 */ 


	@Transactional
	public void updateUiFields() {
		updateUiFields(this.iname, this.uiFields);
	}

	@Transactional
	public void updateUiFields(String area, List<UiField> uiFields) {
		// UI fields
		// this assumes truncate to start with, so clear everything out
		// and use this.uiFields so 
		// TODO: logic to do CRUD compares instead
		Map m = new HashMap();
		m.put("locale", "en_US");
		m.put("area", area);
		List<UiField> oldUiFields = uiFieldService.findByMap(m);
		for (UiField uiField: oldUiFields) {
			if (uiField.getName().indexOf("jobsubmit/")>-1) {
				continue;//do NOT remove custom titles
			}
			uiFieldService.remove(uiField);
			uiFieldService.flush(uiField);
		}

		// sets up the base uifields new
		if (baseUiFields != null) {
			for (UiField f: safeList(baseUiFields)) {
				f.setArea(this.iname);

				String key = this.iname + "." + f.getName() + "."
						+ f.getAttrName();
				String lang = f.getLocale().substring(0, 2);
				String cntry = f.getLocale().substring(3);
	
				Locale locale = new Locale(lang, cntry);
		
				((WaspMessageSourceImpl) messageSource).addMessage(key, locale, f.getAttrValue());

				// deletes the old one if exists
				Map oldM	= new HashMap();
				oldM.put("locale", f.getLocale());
				oldM.put("area", f.getArea());
				oldM.put("name", f.getName());
				oldM.put("attrName", f.getAttrName());
				oldUiFields = uiFieldService.findByMap(oldM);
				for (UiField oldF: oldUiFields) {
					uiFieldService.remove(oldF); 
					uiFieldService.flush(oldF); 
				}
				uiFieldService.save(f); 
			}
		}

		// sets up the new
		for (UiField f: safeList(uiFields)) {
			String key = f.getArea() + "." + f.getName() + "."
					+ f.getAttrName();
			String lang = f.getLocale().substring(0, 2);
			String cntry = f.getLocale().substring(3);

			Locale locale = new Locale(lang, cntry);

			((WaspMessageSourceImpl) messageSource).addMessage(key, locale, f.getAttrValue());

			// deletes the old one if exists
			Map oldM	= new HashMap();
			oldM.put("locale", f.getLocale());
			oldM.put("area", f.getArea());
			oldM.put("name", f.getName());
			oldM.put("attrName", f.getAttrName());
			oldUiFields = uiFieldService.findByMap(oldM);
			for (UiField oldF: oldUiFields) {
				uiFieldService.remove(oldF); 
				uiFieldService.flush(oldF); 
			}

			uiFieldService.save(f); 
		}

		edu.yu.einstein.wasp.dao.impl.DBResourceBundle.MESSAGE_SOURCE=(WaspMessageSourceImpl)messageSource; //save handle to messageSource for easy access

	}
	
	protected <E> List<E> safeList(List<E> list){
		return (list == null ? Collections.EMPTY_LIST : list);
	}
	
	protected <E> Set<E> safeSet(Set<E> set){
		return (set == null ? Collections.EMPTY_SET : set);
	}

}

