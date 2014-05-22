package edu.yu.einstein.wasp.load.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.UiFieldDao;
import edu.yu.einstein.wasp.load.service.WaspLoadService;
import edu.yu.einstein.wasp.model.UiField;
import edu.yu.einstein.wasp.service.impl.WaspMessageSourceImpl;

/**
 * 
 * @author asmclellan
 *
 */
@Service
@Transactional("entityManager")
public class WaspLoadServiceImpl implements WaspLoadService {
	
	@Autowired
	protected UiFieldDao uiFieldDao;
	
	@Autowired
	private MessageSource messageSource;
	

	@Override
	public void updateUiFields(List<UiField> uiFields) {
		for (UiField f: safeList(uiFields)) {
			String key = f.getArea() + "." + f.getName() + "." + f.getAttrName();
			String lang = f.getLocale().substring(0, 2);
			String cntry = f.getLocale().substring(3);
			uiFieldDao.save(f); 
			Locale locale = new Locale(lang, cntry);
			((WaspMessageSourceImpl) messageSource).addMessage(key, locale, f.getAttrValue());
		}
	}
	
	/**
	 * return a list of each unique area present in the provided uiFields list
	 * @return
	 */
	@Override
	public List<String> getAreaListFromUiFields(final List<UiField> uiFields){
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
