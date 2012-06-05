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
import org.springframework.util.StringUtils;

import util.spring.PostInitialize;
import edu.yu.einstein.wasp.dao.UiFieldDao;
import edu.yu.einstein.wasp.exception.UiFieldParseException;
import edu.yu.einstein.wasp.model.UiField;
import edu.yu.einstein.wasp.service.impl.WaspMessageSourceImpl;


/**
 * base clase for all the wasp loaders
 * full of convenience methods like
 *	- updateUiFields
 *
 */

@Transactional
public abstract class WaspLoadService {

	protected final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private MessageSource messageSource;

	@Autowired
	protected UiFieldDao uiFieldDao;
	
	protected String domain;
	public void setDomain(String domain) { this.domain = domain;}

	protected String iname; 
	public void setIName(String iname) {this.iname = iname; }
	
	protected String name; 
	public void setName(String name) {this.name = name; }
	
	protected String area;
	public void setArea(String area){
		this.area = StringUtils.replace(area, ".", "_"); 
		this.iname = area;
	}
	
	protected List<UiField> uiFields; 
	
	public void setUiFields(List<UiField> uiFields) {
		this.uiFields = uiFields;	
	}
	
	public void setUiFieldGroups(List< List<UiField> > uiFieldsList) {
		int metapositionOffset = 0;
		for (List<UiField> uiFieldSet : safeList(uiFieldsList)){
			int metapositionMaxPos = -1;
			for (UiField uiField : uiFieldSet){
				if (uiField.getAttrName().equals("metaposition")){
					int metaPosition;
					try{
						metaPosition = Integer.parseInt(uiField.getAttrValue());
					} 
					catch(NumberFormatException e){
						throw new UiFieldParseException("Cannot parse '"+uiField.getAttrValue()+"' to Integer");
					}
					uiField.setAttrValue( Integer.toString(metapositionOffset + metaPosition) );
					if (metaPosition > metapositionMaxPos)
						metapositionMaxPos = metaPosition;
				}
				if (this.uiFields == null)
					this.uiFields = new  ArrayList<UiField>();
				this.uiFields.add(uiField);
			}
		}
	}
	
	public void setUiFieldsFromWrapper(List<UiFieldFamilyWrapper> uiFieldWrappers) {
		for (UiFieldFamilyWrapper uiFieldWrapper : safeList(uiFieldWrappers)){
			if (this.uiFields == null)
				this.uiFields = new  ArrayList<UiField>();
			this.uiFields.addAll(uiFieldWrapper.getUiFields());
		}
	}
	
	public void setUiFieldGroupsFromWrapper(List< List<UiFieldFamilyWrapper> > uiFieldWrappers) {
		int metapositionOffset = 0;
		for (List<UiFieldFamilyWrapper> uiFieldWrapperSet : safeList(uiFieldWrappers)){
			int metapositionMaxPos = -1;
			for (UiFieldFamilyWrapper uiFieldWrapper : uiFieldWrapperSet){
				for (UiField uiField : uiFieldWrapper.getUiFields()){
					if (uiField.getAttrName().equals("metaposition")){
						int metaPosition;
						try{
							metaPosition = Integer.parseInt(uiField.getAttrValue());
						} 
						catch(NumberFormatException e){
							throw new UiFieldParseException("Cannot parse '"+uiField.getAttrValue()+"' to Integer");
						}
						uiField.setAttrValue( Integer.toString(metapositionOffset + metaPosition) );
						if (metaPosition > metapositionMaxPos)
							metapositionMaxPos = metaPosition;
					}
					if (this.uiFields == null)
						this.uiFields = new  ArrayList<UiField>();
					this.uiFields.add(uiField);
				}
			}
		}
	}
	
	
	
	public List<UiField> getUiFields() {return this.uiFields; }

	protected static final Logger log = Logger.getLogger(WaspLoadService.class);
	
	protected WaspLoadService sourceLoadService;
	
	public void setInheritUiFieldsFromLoadService(WaspLoadService sourceLoadService){
		this.sourceLoadService = sourceLoadService;
	}
	
	
	public WaspLoadService(){}
	
	public WaspLoadService (WaspLoadService sourceLoadService){
		this.sourceLoadService = sourceLoadService;
	}
		
	
	/* override me.... */
	@Transactional
	@PostInitialize 
	public void postInitialize() {
	}
	
	/**
	 * return a list of each unique area present in the provided uiFields list
	 * @return
	 */
	protected List<String> getAreaListFromUiFields(final List<UiField> uiFields){
		List<String> areaList = new ArrayList<String>();
		if (uiFields != null){
			for (UiField uiField: uiFields){
				if (!areaList.contains(uiField.getArea()) ){
					areaList.add(uiField.getArea() );
				}
			}
		}
		return areaList;
	}

	
	/**
	 * updates the UiFields for that object
	 * assumes that UIFields were truncated on container load 
	 * 
	 * adds them to the cached messageSource
	 */ 
	@Transactional
	public void updateUiFields() {
		updateUiFields(null);	
	}
	
	/**
	 * updates the UiFields for that object
	 * assumes that UIFields were truncated on container load 
	 * 
	 * adds them to the cached messageSource
	 */ 
	@Transactional
	public void updateUiFields(String area) {
		// UI fields
		// this assumes truncate to start with, so clear everything out
		// and use this.uiFields
		List<String> workingAreaList = new ArrayList<String>();
		if (area == null){
			// we're going to be using the actual areas represented in the uiFields list
			workingAreaList = getAreaListFromUiFields(this.uiFields);
		} else {
			// we're going to override the areas represented in the uiFields list with the area provided
			workingAreaList.add(area);
		}
		
		// get map of all uifields in current area set
		Map<String, UiField> oldUiFields = new HashMap<String, UiField>();
		for (String currentArea : workingAreaList){
			Map<String, String> m = new HashMap<String, String>();
			m.put("area", currentArea);
			for (UiField f : uiFieldDao.findByMap(m) ){
				String key = f.getLocale() + "." + currentArea + "." + f.getName() + "." + f.getAttrName();
				oldUiFields.put(key, f );
			}
		}
	
		for (UiField f: safeList(this.uiFields)) {
			if (area != null && !area.equals(f.getArea()))
				f.setArea(area);
			String key = f.getArea() + "." + f.getName() + "." + f.getAttrName();
			String localizedKey = f.getLocale() + "." + key;
			String lang = f.getLocale().substring(0, 2);
			String cntry = f.getLocale().substring(3);
			if (oldUiFields.containsKey(localizedKey)){
				// update if changed
				UiField existingUiField = oldUiFields.get(localizedKey);
				if (!existingUiField.getAttrValue().equals(f.getAttrValue())){
					existingUiField.setAttrValue(f.getAttrValue());
					uiFieldDao.merge(f); 
				}
				oldUiFields.remove(localizedKey);
			} else {
				// add new
				uiFieldDao.save(f); 
			}
			Locale locale = new Locale(lang, cntry);
			((WaspMessageSourceImpl) messageSource).addMessage(key, locale, f.getAttrValue());
		}
		// remove the left overs
		for (String key: oldUiFields.keySet()) {
			UiField oldF = oldUiFields.get(key);
			if (oldF.getName().indexOf("jobsubmit/")>-1) {
				continue;//do NOT remove custom titles
			}
			uiFieldDao.remove(oldF); 
			uiFieldDao.flush(oldF); 
		}
	}
	
	protected <E> List<E> safeList(List<E> list){
		return (list == null ? Collections.EMPTY_LIST : list);
	}
	
	protected <E> Set<E> safeSet(Set<E> set){
		return (set == null ? Collections.EMPTY_SET : set);
	}

}

