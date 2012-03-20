package edu.yu.einstein.wasp.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.WordUtils;
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

	
	protected final Logger logger = Logger.getLogger(MetaHelper.class);
	
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
		logger.debug("Constructing MetaHelper with parentArea="+parentArea+", area="+area+", class="+clazz.getName()+", locale="+locale.toString());
	}
	
	/**
	 * Construct a meta-helper using the default locale (en_US).  Instances of this class are for access of
	 * non-internationalized metadata (ie values).  Any access in a display layer should use the
	 * MetaHelper(area, class, locale) constructor.
	 * @param area
	 * @param clazz
	 */
	public <T extends MetaBase> MetaHelper(String area, Class<T> clazz) {
		this(area, area, clazz, new Locale(Locale.US.toString()));
	}
	
	/**
	 * Constructor
	 * @param area
	 * @param clazz
	 * @param locale
	 */
	public <T extends MetaBase> MetaHelper(String area, Class<T> clazz, Locale locale) {
		this(area, area, clazz, locale);
	}
	
	
	/**
	 * Construct a meta-helper using the default locale (en_US).  Instances of this class are for access of
	 * non-internationalized metadata (ie values).  Any access in a display layer should use the
	 * MetaHelper(class, locale) constructor.
	 * Area defaults to class name prefix e.g. 'SampleMeta' class will have an area of 'sample' 
	 * or 'UserMeta' will have an area of 'user'
	*/
	public <T extends MetaBase> MetaHelper(Class<T> clazz) {
		this(WordUtils.uncapitalize(clazz.getSimpleName().replace("Meta", "")), clazz);
	}
	
	/**
	 * Constructor
	 * Area defaults to class name prefix e.g. 'SampleMeta' class will have an area of 'sample' 
	 * or 'UserMeta' will have an area of 'user'
	 * @param clazz
	 * @param locale
	 */
	public <T extends MetaBase> MetaHelper(Class<T> clazz, Locale locale) {
		this(WordUtils.uncapitalize(clazz.getSimpleName().replace("Meta", "")), clazz, locale);
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
	 * Must call setMetaList(), getMasterList(), getFromRequest(), getFromJsonForm() or syncWithMaster() on this object first or will return null.
	 * @return
	 */
	public List<? extends MetaBase> getMetaList(){
		return this.lastList;
	}
	
	/**
	 * Sets the meta list stored in metaHelper and updates properties according to master list for relevant 
	 * @param list
	 */
	public void setMetaList(List<? extends MetaBase> list){
		this.lastList = list;
	}
	
	/**
	 * Get list of unique areas represented in the current meta list held by this object
	 * @return
	 */
	public <T extends MetaBase> List<String> getUniqueAreaList(){
		return getUniqueAreaList(this.lastList);
	}
	
	/**
	 * Get list of unique areas represented in the supplied meta list
	 * @param dbList
	 * @return
	 */
	public <T extends MetaBase> List<String> getUniqueAreaList(List<T> dbList){
		List<String> areaList = new ArrayList<String>();
		Set<String> areaSet = new HashSet<String>();
		for (T meta: dbList){
			if (getAreaFromMeta(meta) != null){
				areaSet.add(getAreaFromMeta(meta));
			}
		}
		areaList.addAll(areaSet);
		return areaList;
	}
	
	/**
	 * Extract area from the key of a given meta object or return null if no area could be determined
	 * e.g. if meta key is 'sample.name' or even 'sample.name.value' this function will return 'sample', however, a meta key of 'name' would return null
	 * @param meta
	 * @return
	 */
	public static <T extends MetaBase> String getAreaFromMeta(T meta){
		String[] keyComponents = meta.getK().split("\\.");
		if (keyComponents.length > 1){
			return keyComponents[0];
		}
		return null;
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
				p.setDefaultVal(bundleResource.get(qualifiedName + ".default"));
				if (visibility != null && visibility.containsKey(name)){
					p.setFormVisibility(visibility.get(name));
					if (visibility.get(name).equals(MetaAttribute.FormVisibility.ignore)){
						p.setMetaposition(0); // no ranking for hidden attributes
					}
				} else {
					p.setFormVisibility(MetaAttribute.FormVisibility.editable); // set default form visibility
				}

				p.setControl(MetaUtil.getControl(qualifiedName + ".control", locale));
				logger.debug("getMasterList() adding '" + qualifiedName + "' to list");
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
	 * Updates "dbList" so it only contains fields found in "properties" file and apply visibility options
	 * @param dbList
	 * @return
	 */
	public final <T extends MetaBase> List<T> syncWithMaster(List<T> dbList, Map<String, MetaAttribute.FormVisibility> visibility) {
		return syncWithMaster(dbList, (List<T>) getMasterList(visibility, clazz));
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
	 * Finds a {@link MetaBase} derived object by name in the last list generated and returns its value
	 * @param name
	 * @return {@link MetaBase} or null if not found 
	 * @throws MetadataException
	 */
	public  String getMetaValueByName(String name) throws MetadataException{
		return getMetaByName(name, this.lastList).getV();
	}
	
	/**
	 * Finds a {@link MetaBase} derived object by name in the provided list and returns its value
	 * @param name
	 * @return {@link MetaBase} derived object or null if not found 
	 * @throws MetadataException 
	 */
	public <T extends MetaBase> String getMetaValueByName(String name, List<T> list) throws MetadataException{
		for (T meta : list) {
			if (meta.getK().equals(area + "." + name) ) {
				return meta.getV();
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
