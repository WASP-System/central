package edu.yu.einstein.wasp.controller.validator;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import edu.yu.einstein.wasp.dao.impl.DBResourceBundle;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.MetaUtil;
import edu.yu.einstein.wasp.model.MetaAttribute.FormVisibility;


/**
 * Contains utility methods for manipulating
 * meta and MetaAttribute objects
 * @author Sasha Levchuk
 *
 */
public class MetaHelper {

	
	protected final Logger logger = Logger.getLogger(getClass());
	
	/**
	 * Constructor
	 * @param area
	 * @param parentArea
	 * @param clazz
	 * @param session
	 */
	public <T extends MetaBase> MetaHelper(String area, String parentArea, Class<T> clazz, HttpSession session) {
		this.area = area;
		this.parentArea = parentArea;
		this.clazz = clazz;
		
		this.locale=(Locale)session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		 
	}
	
	/**
	 * Constructor
	 * @param area
	 * @param clazz
	 * @param session
	 */
	public <T extends MetaBase> MetaHelper(String area, Class<T> clazz,HttpSession session) {
		this(area,area,clazz,session);
	}

	/**
	 * Constructor
	 * @param area
	 * @param clazz
	 * @param locale
	 */
	public <T extends MetaBase> MetaHelper(String area, Class<T> clazz, Locale locale) {
		this.area = area;
		this.parentArea = area;
		this.clazz = clazz;
		this.locale=locale;
	}

	/**
	 * Constructor
	 * @param area
	 * @param parentArea
	 * @param clazz
	 * @param locale
	 */
	public <T extends MetaBase> MetaHelper(String area, String parentArea, Class<T> clazz, Locale locale) {
		this.area = area;
		this.parentArea = parentArea;
		this.clazz = clazz;
		this.locale=locale;
	}

	private String area;
	
	/**
	 * get area attribute
	 * @return
	 */
	public String getArea() {
		return this.area;
	}
	
	/**
	 * set area attribute
	 * @param area
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * get parent area attribute
	 */
	private String parentArea;
	public String getParentArea() {
		if (this.area == null) { return this.area; }
		return this.parentArea;
	}
	
	/**
	 * set parent area attribute
	 * @param parentArea
	 */
	public void setParentArea(String parentArea) {
		this.parentArea = parentArea;
	}

	private Locale locale;


	/**
	 * get locale
	 * @return
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * set locale
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	private Class<? extends MetaBase> clazz;
	
	/**
	 * get the {@link Class} attribute
	 * @return
	 */
	public Class<? extends MetaBase> getClazz() {
		return this.clazz;
	}
	
	/**
	 * set the {@link Class} attribute
	 * @param clazz
	 */
	public <T extends MetaBase> void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	private List<? extends MetaBase> lastList = null;
	
	/**
	 * Get the last generated list of metadata. 
	 * Must call getMasterList(), getFromRequest(), getFromJsonForm() or syncWithMaster() on this object first or will return null.
	 * @return
	 */
	public List<? extends MetaBase> getMetaList(){
		return this.lastList;
	}
	
	/**
	 * Creates Sorted (by metaposition) Meta List from messages_en
	 * Populates property (label/control/error) for each item
	 * @param clazz
	 * @return
	 */
	public final <T extends MetaBase> List<T> getMasterList(Class<T> clazz) {
		return getMasterList((HashMap<String, MetaAttribute.FormVisibility>) null, clazz);
	}

	/**
	 * Creates Sorted (by metaposition) Meta List from messages_en
	 * Populates property (label/control/error) for each item
	 * @param visibility
	 * @param clazz
	 * @return
	 */
	public final <T extends MetaBase> List<T> getMasterList(Map<String, MetaAttribute.FormVisibility> visibility, Class<T> clazz) {
		Map<Integer, String> uniquePositions = new HashMap<Integer, String>();
		Map<String, String> bundleResource = new HashMap<String, String>();

		Set<String> keys=DBResourceBundle.MESSAGE_SOURCE.getKeys(Locale.US);
		for(String k: keys) {
			
			if (!k.startsWith(area +".")) continue; // get ONLY keys for area we are dealing with
		
			String currentMessage = DBResourceBundle.MESSAGE_SOURCE.getMessage(k,null,locale);

		
			bundleResource.put(k, currentMessage);

			if (!k.endsWith(".metaposition")) continue;

			String[] path=StringUtils.tokenizeToStringArray(k,"."); // e.g. splits user.login.metaposition
			String name=path[1]; // e.g. 'login' using above example

			Integer pos=99;
			try {
				pos = Integer.parseInt(currentMessage);
			} catch (Exception e) {
			}

			// multiplied to account for dups
			Integer keyPos = pos * 1000; 
			while (uniquePositions.containsKey(keyPos)) {
				keyPos++;
			}

			uniquePositions.put(keyPos, name); // e.g. uniquePositions.put(5000, "login")
		}

		Set<Integer> positions = new TreeSet<Integer>(uniquePositions.keySet()); // creates set of automatically sorted meta positions

		List<T> list = new ArrayList<T>();
		for (Integer i: positions) {
			try {
				T obj = clazz.newInstance();
				String name = uniquePositions.get(i);
				String qualifiedName = area + "." + name;

				obj.setK(qualifiedName);

				MetaAttribute p=new MetaAttribute();
				obj.setProperty(p);

				p.setMetaposition(i);
				// in the following note that bundleResource.get() returns null if no mapped value
				p.setLabel(bundleResource.get(qualifiedName + ".label"));
				p.setConstraint(bundleResource.get(qualifiedName + ".constraint"));
				p.setError(bundleResource.get(qualifiedName + ".error"));
				p.setMetaType(bundleResource.get(qualifiedName + ".type"));
				p.setRange(bundleResource.get(qualifiedName + ".range"));
				if (visibility != null && visibility.containsKey(name)){
					p.setFormVisibility(visibility.get(name));
					if (visibility.get(name).equals(MetaAttribute.FormVisibility.ignore)){
						p.setMetaposition(0); // no ranking for hidden attributes
					}
				} else {
					p.setFormVisibility(MetaAttribute.FormVisibility.editable); // set default form visibility
				}

				p.setControl(MetaUtil.getControl(qualifiedName + ".control", locale));

				list.add(obj);
			} catch (Exception e) {
			}
		}

		this.lastList =  list; 

		return list;
	}
	
	/**
	 *  Generates Master List and pulls in from values from request.  
	 *	[parentarea]Meta_[metakey] (ie. "userMeta_user.phone");
	 * @param request
	 * @param clazz
	 * @return
	 */
	public final <T extends MetaBase> List<T> getFromRequest(HttpServletRequest request, Class<T> clazz) {
		return getFromRequest(request, (Map<String, MetaAttribute.FormVisibility>) null, clazz);
	}

	/**
	 * Generates Master List and pulls in from values from request.  
	 *	[parentarea]Meta_[metakey] (ie. "userMeta_user.phone");
	 * @param request
	 * @param visibility 
	 * @param clazz
	 * @return
	 */
	public final <T extends MetaBase> List<T> getFromRequest(HttpServletRequest request, Map<String, MetaAttribute.FormVisibility> visibility, Class<T> clazz) {
		List<T> list = getMasterList(visibility, clazz);

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

		this.lastList =  list; 

		return list;
	}
	
	/**
	 * Generates Master List and pulls in from values from request (Json form)
	 * @param request
	 * @param clazz
	 * @return
	 */
	public final <T extends MetaBase> List<T> getFromJsonForm(HttpServletRequest request, Class<T> clazz) {
		return getFromJsonForm(request, (Map<String, MetaAttribute.FormVisibility>) null, clazz);
	}

	/**
	 * Generates Master List and pulls in from values from request (Json form)
	 * @param request
	 * @param visibility
	 * @param clazz
	 * @return
	 */
	public final <T extends MetaBase> List<T> getFromJsonForm(HttpServletRequest request, Map<String, MetaAttribute.FormVisibility> visibility, Class<T> clazz) {
		List<T> list = getMasterList(visibility, clazz);

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

		this.lastList =  list; 

		return list;
	}


	/**
	 * Updates "dbList" so it only contains fields found in "properties" file
	 * @param dbList
	 * @return
	 */
	public final <T extends MetaBase> List<T> syncWithMaster(List<T> dbList) {
		return syncWithMaster(dbList, (List<T>) getMasterList(clazz));
	}
	
	
	/**
	 * Updates "currentList" so it only contains fields found in "properties" file
	 * @param currentList
	 * @param masterList
	 * @return
	 */
	public final <T extends MetaBase> List<T> syncWithMaster(List<T> currentList,List<T> masterList) {
		

		Map<String, String> dbMap = new HashMap<String, String>();
		for (T obj : currentList) {
			dbMap.put(obj.getK(), obj.getV());			
		}

		for (T obj : masterList) {
			obj.setV(dbMap.get(obj.getK()));
		}
		this.lastList =  masterList; // update with values from current list
		return masterList;
	}
	

	/**
	 * Get a {@link MetaValidatorImpl} instance 
	 * Validates any metadata with constraints in the supplied list
	 * @param list
	 * @return
	 */
	public final MetaValidator getMetaValidator(List<? extends MetaBase> list) {
		return getMetaValidator(list, MetaValidatorImpl.class);
	}
	
	/**
	 * Get a {@link MetaValidator} derived instance of type specified by 'metaValidatorClazz'
	 * Validates any metadata with constraints in the supplied list
	 * @param list
	 * @param metaValidatorClazz
	 * @return
	 */
	public final <T extends MetaValidator> T getMetaValidator(List<? extends MetaBase> list, Class <T>metaValidatorClazz) {
		if (list == null) return null;
		T validator;
		try {
			validator = metaValidatorClazz.newInstance();
		} catch (Exception e) {
			return null;
		}
		return validator;
	}
	
	/**
	 * Get a {@link MetaValidatorImpl} instance 
	 * Validates any metadata with constraints in the last generated metadata list
	 * Must call getMasterList(), getFromRequest(), getFromJsonForm() or syncWithMaster() on this object first or will return null.
	 * @return
	 */
	public final MetaValidator getMetaValidator() {
		return getMetaValidator(this.lastList);
	}
	
	/**
	 * Get a {@link MetaValidator} derived instance of type specified by 'metaValidatorClazz'
	 * Validates any metadata with constraints in the last generated metadata list
	 * Must call getMasterList(), getFromRequest(), getFromJsonForm() or syncWithMaster() on this object first or will return null.
	 * @param metaValidatorClazz
	 * @return
	 */
	public final <T extends MetaValidator> T getMetaValidator(Class<T> metaValidatorClazz) {
		return getMetaValidator(this.lastList, metaValidatorClazz);
	}
	
	/**
	 * Validates any metadata with constraints in the supplied list using the supplied validator object
	 * @param validator
	 * @param list
	 * @param result
	 */
	public void validate(MetaValidator validator, List<? extends MetaBase> list, BindingResult result) {
		validator.validate(list, result, area, parentArea);
	}

	/**
	 * Validates any metadata with constraints in the last generated metadata list using the supplied validator object
	 * Must call getMasterList(), getFromRequest(), getFromJsonForm() or syncWithMaster() on this object first or will return null.
	 * @param validator
	 * @param result
	 */
	public void validate(MetaValidator validator, BindingResult result) {
		validator.validate(this.lastList, result, area, parentArea);
	}

	/**
	 * Validates any metadata with constraints in the supplied list using {@link MetaValidatorImpl}
	 * @param list
	 * @param result
	 */
	public void validate(List<? extends MetaBase> list, BindingResult result) {
		getMetaValidator(list).validate(list, result, area, parentArea);
	}
	
	/**
	 * Validates any metadata with constraints in the last generated metadata list using {@link MetaValidatorImpl}
	 * Must call getMasterList(), getFromRequest(), getFromJsonForm() or syncWithMaster() on this object first or will return null.
	 * @param result
	 */
	public void validate(BindingResult result) {
		getMetaValidator().validate(this.lastList, result, area, parentArea);
	}

	/**
	 * Validates any metadata with constraints in the last generated metadata list using a {@link MetaValidator} derived instance of type specified by 'metaValidatorClazz'
	 * Must call getMasterList(), getFromRequest(), getFromJsonForm() or syncWithMaster() on this object first or will return null.
	 * @param metaValidatorClazz
	 * @param result
	 */
	public <T extends MetaValidator> void validate(Class<T> metaValidatorClazz, BindingResult result) {
		getMetaValidator(this.lastList, metaValidatorClazz).validate(this.lastList, result, area, parentArea);
	}
	
	/**
	 * Finds a {@link MetaBase} derived object by name in the last list generated
	 * @param name
	 * @return {@link MetaBase} or null if not found 
	 * @throws MetadataException
	 */
	public  MetaBase getMetaByName(String name) throws MetadataException{
		return getMetaByName(name, this.lastList);
	}
	
	/**
	 * Finds a {@link MetaBase} derived object by name in the provided list
	 * @param name
	 * @return {@link MetaBase} derived object or null if not found 
	 * @throws MetadataException 
	 */
	public <T extends MetaBase> T getMetaByName(String name, List<T> list) throws MetadataException{
		for (T meta : list) {
			if (meta.getK().equals(area + "." + name) ) {
				return meta;
			}
		} 
		throw new MetadataException("Cannot find metadata with name: "+name);
	}
	
	/**
	 * Finds a {@link MetaBase} derived object by name in the provided list and sets its value
	 * @param name
	 * @param value
	 * @param list
	 * @throws MetadataException
	 */
	public <T extends MetaBase> void setMetaValueByName(String name,  String value, List<T> list) throws MetadataException{
		for (T meta : list) {
			if (meta.getK().equals(area + "." + name) ) {
				meta.setV(value);
				return;
			}
		} 
		throw new MetadataException("Cannot find metadata with name: "+name);
	}
	
	/**
	 * Finds a {@link MetaBase} derived object by name in the last list generated and sets its value
	 * @param name
	 * @param value
	 * @param list
	 * @throws MetadataException
	 */
	public <T extends MetaBase> void setMetaValueByName(String name,  String value) throws MetadataException{
		setMetaValueByName(name, value, this.lastList);
	}
	
	/**
	 * Gets list of metadata messages for locale of supplied HttpSession
	 * @param session
	 * @return Map of name : message pairs
	 */
	public static Map<String, String> getMetadataMessages(HttpSession session){
		Set<String> keys=DBResourceBundle.MESSAGE_SOURCE.getKeys(Locale.US);
		Locale locale=(Locale)session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		Map<String, String> messageMap = new HashMap<String, String>();
		for(String k: keys) {
			if (!k.startsWith("metadata.")) continue; // get ONLY keys for area we are dealing with
			String currentMessage = DBResourceBundle.MESSAGE_SOURCE.getMessage(k,null,locale);
			String[] path=StringUtils.tokenizeToStringArray(k,"."); // e.g. splits user.login.metaposition
			String name=path[1]; // e.g. 'login' using above example
			messageMap.put(name, currentMessage);
		}
		return messageMap;
	}
	
}
