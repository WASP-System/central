package edu.yu.einstein.wasp.load.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.UiFieldDao;
import edu.yu.einstein.wasp.load.service.WaspLoadService;
import edu.yu.einstein.wasp.model.UiField;
import edu.yu.einstein.wasp.service.impl.WaspMessageSourceImpl;

@Service
@Transactional
public class WaspLoadServiceImpl implements WaspLoadService {
	
	@Autowired
	protected UiFieldDao uiFieldDao;
	
	@Autowired
	private MessageSource messageSource;
	
	/**
	 * updates the UiFields for that object
	 * assumes that UIFields were truncated on container load 
	 * 
	 * adds them to the cached messageSource
	 */ 
	@Override
	public void updateUiFields(List<UiField> uiFields) {
		updateUiFields(null, uiFields);	
	}
	
	/**
	 * updates the UiFields for that object
	 * assumes that UIFields were truncated on container load 
	 * 
	 * adds them to the cached messageSource
	 */ 
	@Override
	public void updateUiFields(String area, List<UiField> uiFields) {
		// UI fields
		// this assumes truncate to start with, so clear everything out
		// and use uiFields
		List<String> workingAreaList = new ArrayList<String>();
		if (area == null){
			// we're going to be using the actual areas represented in the uiFields list
			workingAreaList = getAreaListFromUiFields(uiFields);
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
	
		for (UiField f: safeList(uiFields)) {
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
					uiFieldDao.merge(existingUiField); 
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
	
	@SuppressWarnings("unchecked")
	protected <E> List<E> safeList(List<E> list){
		return (list == null ? Collections.EMPTY_LIST : list);
	}
	
	@SuppressWarnings("unchecked")
	protected <E> Set<E> safeSet(Set<E> set){
		return (set == null ? Collections.EMPTY_SET : set);
	}


}
