package edu.yu.einstein.wasp.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.NoSuchMessageException;
import org.springframework.util.StringUtils;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.MetaUtil;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.resourcebundle.DBResourceBundle;
import edu.yu.einstein.wasp.service.RoleService;


/**
 * Contains utility methods for manipulating
 * meta and MetaAttribute objects
 * @author asmclellan / Sasha Levchuk
 *
 */
public class MetaHelper {

	protected final Logger logger = LoggerFactory.getLogger(MetaHelper.class);
	
	/**
	 * Constructor
	 * @param area
	 * @param domain
	 * @param clazz
	 * @param locale
	 */
	public <T extends MetaBase> MetaHelper(String area, String domain, Class<T> clazz, Locale locale) {
		this.area = area;
		this.parentArea = WordUtils.uncapitalize(clazz.getSimpleName().replace("Meta", ""));
		this.domain = domain;
		this.clazz = clazz;
		this.locale=locale;
		logger.debug("Constructing MetaHelper with parentArea="+parentArea+", area="+area+", domain="+domain+", class="+clazz.getName()+", locale="+locale.toString());
	}
	
		
	/**
	 * Construct a meta-helper using the default locale (en_US).  Instances of this class are for access of
	 * non-internationalized metadata (ie values).  Any access in a display layer should use the
	 * MetaHelper(area, class, locale) constructor.
	 * @param area
	 * @param domain
	 * @param clazz
	 */
	public <T extends MetaBase> MetaHelper(String area, String domain, Class<T> clazz) {
		this(area, domain, clazz, Locale.US);
	}
	
	/**
	 * Constructor
	 * @param area
	 * @param clazz
	 * @param locale
	 */
	public <T extends MetaBase> MetaHelper(String area, Class<T> clazz, Locale locale) {
		this(area, "", clazz, locale);
	}
	
	/**
	 * Construct a meta-helper using the default locale (en_US).  Instances of this class are for access of
	 * non-internationalized metadata (ie values).  Any access in a display layer should use the
	 * MetaHelper(area, class, locale) constructor.
	 * @param area
	 * @param clazz
	 */
	public <T extends MetaBase> MetaHelper(String area, Class<T> clazz) {
		this(area, "", clazz, Locale.US);
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
	
	protected String domain;
	
	/**
	 * get domain attribute
	 * @return
	 */
	public String getDomain() {
		return this.domain;
	}
	
	/**
	 * set domain attribute
	 * @param area
	 */
	public void setDomain(String domain) {
		this.domain = domain;
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
	 * Get the last generated list of metadata. 
	 * Must call setMetaList(), getMasterList(), getFromRequest(), getFromJsonForm() or syncWithMaster() on this object first or will return null.
	 * @param clazz
	 * @return
	 * @throws ClassCastException
	 */
	@SuppressWarnings("unchecked")
	public <T extends MetaBase> List<T> getMetaList(Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		for (MetaBase m : this.lastList)
			if (clazz.isInstance(m))
				list.add((T) m); 
			else
				throw new ClassCastException("cannot cast object of type " + m.getClass().getName() + " to " + clazz.getName());
		return list;
	}
	
	/**
	 * Sets the meta list stored in metaHelper 
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
	
	private String getAssociatedEntityNameFromClassName(){
		return clazz.getSimpleName().replace("Meta", "");
	}
	
	private String getAssociatedEntityIdSetterMethodName(){
		return "get"+getAssociatedEntityNameFromClassName()+"Id";
	}
	
	private String setAssociatedEntityIdSetterMethodName(){
		return "set"+getAssociatedEntityNameFromClassName()+"Id";
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
			String currentMessage = k; // returns key if no message found
			try {
				currentMessage = DBResourceBundle.MESSAGE_SOURCE.getMessage(k, null, locale); // possibly nested internationalized values
			} catch (NoSuchMessageException e){
				logger.warn("Unable to retrieve message with key code = '" + k + "': " + e.getLocalizedMessage());
			}

		
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
			T obj = null;
			try {
				 obj = clazz.newInstance();
			} catch(IllegalAccessException e){
				logger.warn("Unable to create a new instance of '"+ clazz.getName() + "' ('getMasterList' does not have access to the definition of the specified class or constructor)");
				continue;
			} catch(InstantiationException e){
				logger.warn("Unable to create a new instance of '"+ clazz.getName() + "' (the class is not of instatiatable type or has no constructor)");
				continue;
			}
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
			p.setTooltip(bundleResource.get(qualifiedName + ".tooltip"));
			p.setDefaultVal(bundleResource.get(qualifiedName + ".default"));
			p.setOnClick(bundleResource.get(qualifiedName + ".onclick"));
			p.setOnChange(bundleResource.get(qualifiedName + ".onchange"));
			p.setOnRender(bundleResource.get(qualifiedName + ".onrender"));
			if (visibility != null && visibility.containsKey(qualifiedName)){
				p.setFormVisibility(visibility.get(qualifiedName));
				if (visibility.get(qualifiedName).equals(MetaAttribute.FormVisibility.ignore)){
					p.setMetaposition(0); // no ranking for hidden attributes
				}
			} else {
				p.setFormVisibility(MetaAttribute.FormVisibility.editable); // set default form visibility
			}
			p.setControl(MetaUtil.getControl(qualifiedName + ".control", locale));
			displayDebugInfoMessage(obj, "getMasterList() adding");
			list.add(obj);
		}

		this.lastList =  list; 

		return list;
	}
	

	/**
	 * Updates "dbList" so it only contains fields found in UiFields
	 * @param dbList
	 * @return
	 */
	public final <T extends MetaBase> List<T> syncWithMaster(List<T> dbList) {
		return syncWithMaster(dbList, (List<T>) getMasterList(clazz));
	}
	
	/**
	 * Updates "dbList" so it only contains fields found in UiFields and apply visibility options
	 * @param dbList
	 * @return
	 */
	public final <T extends MetaBase> List<T> syncWithMaster(List<T> dbList, Map<String, MetaAttribute.FormVisibility> visibility) {
		return syncWithMaster(dbList, (List<T>) getMasterList(visibility, clazz));
	}
	
	
	/**
	 * Updates "currentList" so it only contains fields found in UiFields
	 * @param currentList
	 * @param masterList
	 * @return
	 */
	public final <T extends MetaBase> List<T> syncWithMaster(List<T> currentList,List<T> masterList) {
		

		Map<String, String> dbMap = new HashMap<String, String>();
		Map<String, Integer> linkedEntityMap = new HashMap<String, Integer>();
		for (T obj : currentList) {
			try{
				linkedEntityMap.put(obj.getK(), (Integer) clazz.getMethod(this.getAssociatedEntityIdSetterMethodName()).invoke(obj));
			} catch (Exception e){
				logger.debug("Unable to get an associated entity Id value for "+obj.getK()+" as unable to locate or utilize a suitable getter method in "+ clazz.getName());
			}
			dbMap.put(obj.getK(), obj.getV());			
		}

		for (T obj : masterList) {
			obj.setV(dbMap.get(obj.getK()));
			try{
				clazz.getMethod(this.setAssociatedEntityIdSetterMethodName(), Integer.class).invoke(obj, linkedEntityMap.get(obj.getK()));
			} catch (Exception e){
				logger.debug("Not setting an associated entity Id value for "+obj.getK()+" as unable to locate or utilize a suitable setter method in "+ clazz.getName());
			}
			displayDebugInfoMessage(obj, "syncWithMaster() syncing");
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
		return getMetaObjectFromList(this.area, name, this.lastList);
	}
	
	/**
	 * Finds a {@link MetaBase} derived object by name in the provided list
	 * @param name
	 * @return {@link MetaBase} derived object or exception if not found 
	 * @throws MetadataException if not found
	 */
	public static <T extends MetaBase> T getMetaObjectFromList(String area, String name, List<T> list) throws MetadataException{
		for (T meta : list) {
			if (meta.getK().equals(area + "." + name) ) {
				return meta;
			}
		} 
		throw new MetadataException("Cannot find metadata with name: "+name);
	}
	
	
	/**
	 * Get a key-value Map from all the metadata in the supplied list matching the supplied area.
	 * e.g. if area = 'user', and for a MetaBase entry k = 'user.name' and v = 'foo', then the corresponding 
	 * map entry key will be 'name' and the value will be 'foo'.
	 * @param area
	 * @param list
	 * @return
	 */
	public static <T extends MetaBase> Map<String, String> getKeyValueMap(String area, List<T> list){
		Map<String, String> metaMap = new HashMap<String, String>();
		for (T meta : getMetaSubsetByArea(area, list)){
			metaMap.put(meta.getK().substring(area.length()+1), meta.getV());
		}
		return metaMap;
	}
	
	/**
	 * Given a list of metadata, this method returns the subset matching the given area
	 * @param area
	 * @param list
	 * @return List of meta data
	 */
	public static <T extends MetaBase> List<T> getMetaSubsetByArea(String area, List<T> list){
		List<T> subsetMetaList = new ArrayList<T>();
		for (T meta : list) {
			if (meta.getK().startsWith(area + ".") ) {
				subsetMetaList.add(meta);
			}
		} 
		return subsetMetaList;
	}
	
	/**
	 * Finds a {@link MetaBase} derived object by name in the last list generated and returns its value
	 * @param name
	 * @return {@link MetaBase} or exception if not found
	 * @throws MetadataException if not found
	 */
	public  String getMetaValueByName(String name) throws MetadataException{
		return getMetaObjectFromList(this.area, name, this.lastList).getV();
	}
	

	/**
	 *  Finds a {@link MetaBase} derived object by name in the provided list and returns its value
	 * @param area
	 * @param name
	 * @param list
	 * @return Value of metadata
	 * @throws MetadataException
	 */
	public static <T extends MetaBase> String getMetaValue(String area, String name, List<T> list) throws MetadataException{
		return getMetaObjectFromList(area, name, list).getV();
	}

	/**
	 * Finds a {@link MetaBase} derived object by name in the provided list and sets its value
	 * @param area
	 * @param name
	 * @param value
	 * @param list
	 * @throws MetadataException
	 */
	public static <T extends MetaBase> void setMetaValueByName(String area, String name,  String value, List<T> list) throws MetadataException{
		getMetaObjectFromList(area, name, list).setV(value);
	}
	
	/**
	 * Finds a {@link MetaBase} derived object by name in the last list generated and sets its value. 
	 * If a matching object doesn't exist a new one will be created and added to the internal metaList.
	 * @param name
	 * @param value
	 * @throws MetadataException
	 */
	//@SuppressWarnings("unchecked")
	public <T extends MetaBase> void setMetaValueByName(String name,  String value) throws MetadataException{
		if (this.lastList == null)
			this.lastList = new ArrayList<T>();
		try{
			setMetaValueByName(this.area, name, value, this.lastList);
		} catch(MetadataException e){
			// meta doesn't exist so create new
			T meta = null;
			try {
				meta = (T) clazz.newInstance();
			} catch (Exception e1) {
				throw new MetadataException("Cannot create new instance of type '"+clazz.getName()+"': "+ e1.getMessage());
			}
			meta.setK(this.area+"."+name);
			meta.setV(value);
			((List<T>) this.lastList).add(meta);
		}
	}
	
	/**
	 * Display an informational debug message containing key=value information plus meta object id and linked entity id if applicable
	 * @param obj
	 * @param prefix
	 */
	protected <T extends MetaBase> void displayDebugInfoMessage(T obj, String prefix){
		String entityMetaId = "";
		String entityId = "";
		try{
			entityMetaId = String.valueOf( (Integer) clazz.getMethod("get"+clazz.getSimpleName()+"Id").invoke(obj));
		} catch (Exception e){
			entityMetaId = "N/A";
		}
		try{
			entityId = String.valueOf( (Integer) clazz.getMethod(this.getAssociatedEntityIdSetterMethodName()).invoke(obj));
		} catch (Exception e){
			entityId = "N/A";
		}
		logger.debug(prefix + " (" + obj.getK() + "=" + obj.getV() +  ", " + WordUtils.uncapitalize(clazz.getSimpleName()) + "Id="  + 
				entityMetaId + "," + WordUtils.uncapitalize(this.getAssociatedEntityNameFromClassName()) + "Id=" + 	entityId + ")" );
	}
	
	public static <T extends MetaBase> List<Role> convertRoleVisibilityDelimitedStringToRoleList(T meta, RoleService roleService){
		if(meta != null){	
			return roleService.convertMetaRoleVisibilityDelimitedStringToRoleList(meta.getRoleVisibility());
		}
		else{return new ArrayList<Role>();}
	}

	public static String convertRoleListToRoleVisibilityDelimitedString(List<Role> roleList, RoleService roleService){
		return roleService.convertRoleListToMetaRoleVisibilityDelimitedString(roleList);
	}

	public static <T extends MetaBase> boolean roleVisibilityDelimitedStringContainsRole(T meta, Role role, RoleService roleService){
		if(meta != null){	
			return roleService.metaRoleVisibilityDelimitedStringContainsRole(meta.getRoleVisibility(), role);
		}
		else{return false;}
	}
	
	public static <T extends MetaBase> List<T> filterMetaDataMustContainAllTheseRoles(List<T> metaDataList, List<Role> roleList, RoleService roleService){
		if(metaDataList == null || roleList == null || roleService == null){return metaDataList;}
		List<T> newMetaDataList = new ArrayList<T>();
		for(T t : metaDataList){
			boolean allRolesFound = true;
			for(Role role : roleList){
				if(!roleService.metaRoleVisibilityDelimitedStringContainsRole(t.getRoleVisibility(), role)){
					allRolesFound = false;
				}
			}
			if(allRolesFound){
				newMetaDataList.add(t);
			}
		}
		return newMetaDataList;
	}

	public static <T extends MetaBase> List<T> filterMetaDataMustContainThisRole(List<T> metaDataList, Role role, RoleService roleService){
		if(metaDataList == null || role == null || role.getRoleName() == null ||  roleService == null){return metaDataList;}
		List<T> newMetaDataList = new ArrayList<T>();
		for(T t : metaDataList){
			if(roleService.metaRoleVisibilityDelimitedStringContainsRole(t.getRoleVisibility(), role)){
				newMetaDataList.add(t);
			}
		}
		return newMetaDataList;
	}

}
