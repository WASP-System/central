package edu.yu.einstein.wasp.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import edu.yu.einstein.wasp.dao.impl.DBResourceBundle;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.MetaUtil;


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

	protected String area;
	
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
	protected String parentArea;
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

	protected Locale locale;


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

	protected Class<? extends MetaBase> clazz;
	
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

	protected List<? extends MetaBase> lastList = null;
	
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
