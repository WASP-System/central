package edu.yu.einstein.wasp.load;

import java.util.ArrayList;
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
 * full of convenience methods like
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
	public String getIname(){return iname;}

	protected String name; 
	public void setName(String name) {this.name = name; }
	
	protected String area;
	public void setArea(String area){this.area = area;}

	protected List<UiField> baseUiFields; 
	
	public void setBaseUiFields(List<UiField> uiFields) {
		this.baseUiFields = uiFields;	
	}
	
	public List<UiField> getBaseUiFields() {
		return this.baseUiFields; 
	}

	protected List<UiField> uiFields; 
	
	public void setUiFields(List<UiField> uiFields) {
		this.uiFields = uiFields;	
	}
	
	public List<UiField> getUiFields() {return this.uiFields; }

	protected static final Logger log = Logger.getLogger(WaspLoadService.class);
	
	protected WaspLoadService sourceLoadService;
	
	public void setInheritUiFieldsFromLoadService(WaspLoadService sourceLoadService){
		this.sourceLoadService = sourceLoadService;
	}
	
	private int runningMaxMetaPosition; // keep track of maximum meta position encountered
	
	public WaspLoadService(){}
	
	public WaspLoadService (WaspLoadService sourceLoadService){
		this.sourceLoadService = sourceLoadService;
	}
	
	
	/**
	 * maintains metaposition order when combining lists of ui fields
	 * @param uiFieldList
	 */
	private List<UiField> getMetaPositionAdjustedUiFieldList( final List<UiField> uiFieldList ){
		if (uiFieldList == null)
			return null;
		List<UiField> uiFieldsProcessed = new ArrayList<UiField>(uiFieldList);
		int maxFieldsetMetaPosition = 0;
		for (UiField uiField: safeList(uiFieldsProcessed)){
			if (uiField.getAttrName().equals("metaposition")){
				int metaPosition = Integer.parseInt(uiField.getAttrValue());
				uiField.setAttrValue( Integer.toString(runningMaxMetaPosition + metaPosition) );
				if (metaPosition > maxFieldsetMetaPosition)
					maxFieldsetMetaPosition = metaPosition;
			}
		}
		if (maxFieldsetMetaPosition > runningMaxMetaPosition)
			runningMaxMetaPosition = maxFieldsetMetaPosition + 10;
		return uiFieldsProcessed;
	}
	
	
	/* override me.... */
	@Transactional
	@PostInitialize 
	public void postInitialize() {
	}


	private void processUiFieldsAndSave(final List<UiField> uiFieldList, String area){
		if (uiFieldList == null)
			return;
		List<UiField> processedFieldList = getMetaPositionAdjustedUiFieldList(uiFieldList);
		for (UiField f: safeList(processedFieldList)) {
			if (area != null && !area.equals(f.getArea()))
				f.setArea(area);
			String key = f.getArea() + "." + f.getName() + "."
					+ f.getAttrName();
			String lang = f.getLocale().substring(0, 2);
			String cntry = f.getLocale().substring(3);

			Locale locale = new Locale(lang, cntry);
			((WaspMessageSourceImpl) messageSource).addMessage(key, locale, f.getAttrValue());
			// deletes the old one if exists
			Map<String, String> oldM	= new HashMap<String,String>();
			oldM.put("locale", f.getLocale());
			oldM.put("area", f.getArea());
			oldM.put("name", f.getName());
			oldM.put("attrName", f.getAttrName());
			List<UiField> oldUiFields = uiFieldService.findByMap(oldM);
			for (UiField oldF: oldUiFields) {
				uiFieldService.remove(oldF); 
				uiFieldService.flush(oldF); 
			}
			uiFieldService.merge(f); 
		}
	}
	
	
	/**
	 * updates the UiFields for that object
	 * assumes htat UIFields were truncated on container load 
	 * 
	 * adds them to the cached messageSource
	 */ 


	@Transactional
	public void updateUiFields() {
		if (area == null){
			updateUiFields(this.iname);
		} else {
			updateUiFields(this.area);
		}
			
	}
	
	
	@Transactional
	public void updateUiFields(String area) {
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
		runningMaxMetaPosition = -10;
		if (sourceLoadService != null){
			processUiFieldsAndSave(sourceLoadService.getBaseUiFields(), area);
			processUiFieldsAndSave(sourceLoadService.getUiFields(), area);
		}
		processUiFieldsAndSave(baseUiFields, area);
		processUiFieldsAndSave(uiFields, area);

	}
	
	protected <E> List<E> safeList(List<E> list){
		return (list == null ? Collections.EMPTY_LIST : list);
	}
	
	protected <E> Set<E> safeSet(Set<E> set){
		return (set == null ? Collections.EMPTY_SET : set);
	}

}

