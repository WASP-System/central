package edu.yu.einstein.wasp.model;


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import edu.yu.einstein.wasp.controller.validator.MetaValidator;
import edu.yu.einstein.wasp.controller.validator.MetaValidatorImpl;
import edu.yu.einstein.wasp.dao.impl.DBResourceBundle;
import edu.yu.einstein.wasp.service.impl.WaspMessageSourceImpl;



/*
 * contains utility methods for manipulating
 * with *meta and MetaAttribute objects
 * @Author Sasha Levchuk
 */

public class MetaHelper {


	public MetaHelper(String area, String parentArea, Class clazz, HttpSession session) {
		this.area = area;
		this.parentArea = parentArea;
		this.clazz = clazz;
		
		this.locale=(Locale)session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		 
	}
	
	public MetaHelper(String area, Class clazz,HttpSession session) {
		this(area,area,clazz,session);
	}


	public MetaHelper(String area, Class clazz, Locale locale) {
		this.area = area;
		this.parentArea = area;
		this.clazz = clazz;
		this.locale=locale;
	}

	public MetaHelper(String area, String parentArea, Class clazz, Locale locale) {
		this.area = area;
		this.parentArea = parentArea;
		this.clazz = clazz;
		this.locale=locale;
	}

	private String area;
	public String getArea() {
		return this.area;
	}
	public void setArea(String area) {
		this.area = area;
	}

	private String parentArea;
	public String getParentArea() {
		if (this.area == null) { return this.area; }
		return this.parentArea;
	}
	public void setParentArea(String parentArea) {
		this.parentArea = parentArea;
	}

	private Locale locale;


	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	private Class clazz;
	public Class getClazz() {
		return this.clazz;
	}
	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}


	private List<MetaBase> lastList;

	/* 
	 * Creates Sorted (by metaposition) Meta List from messages_en
	 * Populates property (label/control/error) for each item
	 *
	 * 
	 */

	public final <T extends MetaBase> List<T> getMasterList(Class<T> clazz) {
		Map<Integer, String> uniquePositions = new HashMap();
		Map<String, String> baseResource = new HashMap();
		Map<String, String> bundleResource = new HashMap();

		Set<String> keys=((WaspMessageSourceImpl)DBResourceBundle.MESSAGE_SOURCE).getKeys(Locale.US);
		for(String k: keys) {
			
			if (!k.startsWith(area +".")) continue;
		
			String bundleValue = DBResourceBundle.MESSAGE_SOURCE.getMessage(k,null,locale);

		
			bundleResource.put(k, bundleValue);

			if (!k.endsWith(".metaposition")) continue;

			String[] path=StringUtils.tokenizeToStringArray(k,".");
			String name=path[1];

			int pos=99;
			try {
				pos = Integer.parseInt(bundleValue);
			} catch (Exception e) {
			}

			// multiplied to account for dups
			int keyPos = pos * 1000; 
			while (uniquePositions.containsKey(keyPos)) {
				keyPos++;
			}

			uniquePositions.put(keyPos, name);
		}

		Set<Integer> positions = new TreeSet(uniquePositions.keySet());

		List<T> list = new ArrayList();
		for (Integer i: positions) {
			try {
				T obj = (T) clazz.newInstance();

				String name = area + "." + uniquePositions.get(i); 

				obj.setK(name);

				MetaAttribute p=new MetaAttribute();
				obj.setProperty(p);

				p.setMetaposition(i);
				p.setLabel(bundleResource.get(name + ".label"));
				p.setConstraint(bundleResource.get(name + ".constraint"));
				p.setError(bundleResource.get(name + ".error"));

				String controlStr = bundleResource.get(name + ".control");

				if (controlStr != null) {
					String typeStr=controlStr.substring(0,controlStr.indexOf(":"));
	
					MetaAttribute.Control.Type type=MetaAttribute.Control.Type.valueOf(typeStr);
					MetaAttribute.Control control=new MetaAttribute.Control();
					control.setType(MetaAttribute.Control.Type.select);
					p.setControl(control);
	
					if (controlStr.startsWith("select:${")) {
						String [] els=StringUtils.tokenizeToStringArray(controlStr,":");
						if (els==null || els.length!=4) {
							throw new IllegalStateException(controlStr+" must match 'select:${beanName}:itemValue:itemLabel' pattern");
						}
	
						String beanName=els[1].replace("${","").replace("}", "");
						control.setItems(beanName);
						control.setItemValue(els[2]);
						control.setItemLabel(els[3]);
					} else {
						List<MetaAttribute.Control.Option> options=new ArrayList<MetaAttribute.Control.Option>();
	
						String[] pairs=StringUtils.tokenizeToStringArray(controlStr.substring(controlStr.indexOf(":")+1),";");
	
						for(String el:pairs) {
							String [] pair=StringUtils.split(el,":");
							MetaAttribute.Control.Option option = new MetaAttribute.Control.Option();
							option.setValue(pair[0]);
							option.setLabel(pair[1]);
							options.add(option);
						}
						control.setOptions(options);
					}
				}

				list.add(obj);
			} catch (Exception e) {
			}
		}

		this.lastList = (List<MetaBase>) list; 

		return list;
	}

	/**
		* Generates Master List and pulls in from
		* values from request.  
		*	[parentarea]Meta_[metakey] (ie. "userMeta_user.phone");
		*
		*/

	public final <T extends MetaBase> List<T> getFromRequest(HttpServletRequest request, Class<T> clazz) {
		List<T> list = getMasterList(clazz);

		Map params = request.getParameterMap();

		for (T obj: list) {
			String requestKey = parentArea + "Meta" + "_" + obj.getK();
			if (! params.containsKey(requestKey)) { continue; }
			try {
				obj.setV(((String[])params.get(requestKey))[0]);
			} catch (Throwable e) {
				throw new IllegalStateException("cannot merge attributes ",e);
			}
		}

		this.lastList = (List<MetaBase>) list; 

		return list;
	}

	public final <T extends MetaBase> List<T> getFromJsonForm(HttpServletRequest request, Class<T> clazz) {
		List<T> list = getMasterList(clazz);

		Map params = request.getParameterMap();

		for (T obj: list) {
			String requestKey = obj.getK();
			if (! params.containsKey(requestKey)) { continue; }
			try {
				obj.setV(((String[])params.get(requestKey))[0]);
			} catch (Throwable e) {
				throw new IllegalStateException("cannot merge attributes ",e);
			}
		}

		this.lastList = (List<MetaBase>) list; 

		return list;
	}


	/*
	 * updates "dbList" so it only contains fields found in "properties" file
	 */
	public final <T extends MetaBase> List<T> syncWithMaster(List<T> dbList) {
		return syncWithMaster(dbList,getMasterList(clazz));
	}
	
	/*
	 * updates "dbList" so it only contains fields found in "properties" file
	 */
	public final <T extends MetaBase> List<T> syncWithMaster(List<T> currentList,List<T> masterList) {
		

		Map<String, String> dbMap = new HashMap();
		for (T obj : currentList) {
			dbMap.put(obj.getK(), obj.getV());			
		}

		for (T obj : masterList) {
			obj.setV(dbMap.get(obj.getK()));
		}

		return masterList;
	}
	


	public final MetaValidator getMetaValidator(List<? extends MetaBase> list) {
		return getMetaValidator(list, MetaValidatorImpl.class);
	}

	public final <T extends MetaValidator> T getMetaValidator(List<? extends MetaBase> list, Class <T>metaValidatorClazz) {
		List<String> validateList = new ArrayList<String>();

		T validator;
		try {
			validator = (T) metaValidatorClazz.newInstance();
		} catch (Exception e) {
			return null;
		}

		validator.setValidateList(getValidateList(list));

		return validator;
	}

	public final MetaValidator getMetaValidator() {
		return getMetaValidator(this.lastList);
	}

	public final <T extends MetaValidator> T getMetaValidator(Class<T> metaValidatorClazz) {
		return getMetaValidator(this.lastList, metaValidatorClazz);
	}

	public void validate(MetaValidator validator, List<? extends MetaBase> list, BindingResult result) {
		validator.setValidateList(getValidateList(list));
		validator.validate(list, result, area, parentArea);
	}

	public void validate(MetaValidator validator, BindingResult result) {
		validator.setValidateList(getValidateList(this.lastList));
		validator.validate(this.lastList, result, area, parentArea);
	}

	public void validate(List<? extends MetaBase> list, BindingResult result) {
		getMetaValidator(list).validate(list, result, area, parentArea);
	}

	public void validate(BindingResult result) {
		getMetaValidator().validate(this.lastList, result, area, parentArea);
	}

	public void validate(Class metaValidatorClazz, BindingResult result) {
		getMetaValidator(this.lastList, metaValidatorClazz).validate(this.lastList, result, area, parentArea);
	}
	
	public List<String> getValidateList(List<? extends MetaBase> list){
		List<String> validateList = new ArrayList<String>();
		for (MetaBase meta : list) {
			if (meta.getProperty() != null
					&& meta.getProperty().getConstraint() != null) {
				validateList.add(meta.getK());
				validateList.add(meta.getProperty().getConstraint());
			}
		}
		return validateList;
	}

	/*
	public static String getBundleValue(String key, ResourceBundle bundle, String defaultValue) {
		if (defaultValue==null) defaultValue=key; 
		try {
			String ret=(String)bundle.getObject(key);
			if (ret==null) return defaultValue;
			
			return ret;
		} catch (Throwable e) {
			return defaultValue;
		}
	}
	
	public static String getBundleValue(String key, ResourceBundle bundle) {
		return getBundleValue(key, bundle, null);
	}
	*/
	
}
