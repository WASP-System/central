package edu.yu.einstein.wasp.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.MetaMessage;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.exception.StatusMetaMessagingException;
import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.service.MetaMessageService;

/**
 * Message metadata saving and retrieval. Can store chronological metadata about the status of an entity via a key - value system. Ideal for
 * Displaying status information to users.
 * @author andymac
 *
 */
@Service
@Transactional("entityManager")
public class MetaMessageServiceImpl extends WaspServiceImpl implements MetaMessageService{
	
	protected Logger logger = LoggerFactory.getLogger(MetaMessageServiceImpl.class);
	
	private static final String STATUS_KEY_PREFIX = "statusMessage."; 
	
	private static final String DEFAULT_GROUP = "defaultGroup";
	
	private static final String DELIMITER = "::";
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends MetaBase> MetaMessage saveToGroup(String group, String value, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) throws StatusMetaMessagingException{
		return saveToGroup(group, null, value, modelParentId,clazz, dao);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends MetaBase> MetaMessage saveToGroup(String group,  String name, String value, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) throws StatusMetaMessagingException{
		String parentClassName = StringUtils.substringBefore(clazz.getSimpleName(), "Meta");
		String modelParentIdEntityName = null;
		for (Field field : clazz.getDeclaredFields()){
			if (StringUtils.equalsIgnoreCase(field.getName(), WordUtils.uncapitalize(parentClassName) + "Id")){
				modelParentIdEntityName = field.getName();
				break;
			}
		}
		String modelParentIdEntityIdSetterMethodName = null;
		for (Method method : clazz.getMethods()){
			if (StringUtils.equalsIgnoreCase(method.getName(), "set" + parentClassName + "Id")){
				modelParentIdEntityIdSetterMethodName = method.getName();
				break;
			}
		}
		T meta = null;
		logger.debug("Creating new instance of Metadata for current message key '" + group + "' and " + modelParentIdEntityName + "=" + modelParentId );
		try {
			meta = clazz.newInstance(); // get an instance of class'clazz' via reflection 
		} catch (Exception e) {
			throw new StatusMetaMessagingException("Cannot create a new instance of class '"+ clazz.getName()+"' ", e);
		}
		
		// use reflection to invoke method to set the id of the parent model object (UserId to UserMeta or JobId to JobMeta etc) at runtime
		try {
			clazz.getMethod(modelParentIdEntityIdSetterMethodName, Integer.class).invoke(meta, modelParentId);
		} catch (Exception e) {
			throw new StatusMetaMessagingException("Problem invoking method '" + modelParentIdEntityIdSetterMethodName + "'on instance of class '"+ clazz.getName()+"' ", e);
		}
		UUID uniqueId = UUID.randomUUID();
		String metaKey = STATUS_KEY_PREFIX + group + DELIMITER + uniqueId;
		meta.setK(metaKey);
		MetaMessage message = null;
		if (name != null){
			meta.setV(name + DELIMITER + value);
			message =  new MetaMessage(metaKey, group, name, value);
		} else {
			meta.setV(value);
			message =  new MetaMessage(metaKey, group, "", value);
		}
		meta = dao.save(meta);
		message.setDate(meta.getUpdated());
		return message;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends MetaBase> MetaMessage save(String name, String value, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) throws StatusMetaMessagingException{
		return saveToGroup(DEFAULT_GROUP, name, value, modelParentId, clazz, dao);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends MetaBase> MetaMessage save(String value, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) throws StatusMetaMessagingException{
		return saveToGroup(DEFAULT_GROUP, value, modelParentId, clazz, dao);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends MetaBase> List<MetaMessage> read(String group, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) {
		List<MetaMessage> results = new ArrayList<MetaMessage>();
		for (MetaMessage message : readAll(modelParentId, clazz, dao)){
			if (message.getGroup().equals(group))
				results.add(message);
		}
		return results;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends MetaBase> List<MetaMessage> read(String group, String name, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) {
		List<MetaMessage> results = new ArrayList<MetaMessage>();
		for (MetaMessage message : readAll(modelParentId, clazz, dao)){
			if (message.getGroup().equals(group) && message.getName().equals(name))
				results.add(message);
		}
		return results;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends MetaBase> List<MetaMessage> read(Integer modelParentId, Class<T> clazz, WaspDao<T> dao) {
		return read(DEFAULT_GROUP,  modelParentId, clazz, dao);
	}
	
	private <T extends MetaBase> MetaMessage getMetaMessage(T meta){
		MetaMessage message = null;
		String metaKey = meta.getK();
		String nameAndValue = meta.getV();
		if (metaKey.startsWith(STATUS_KEY_PREFIX)){
			String group = StringUtils.substringBetween(metaKey, STATUS_KEY_PREFIX, DELIMITER);
			if (!nameAndValue.contains(DELIMITER)){
				message = new MetaMessage(metaKey, group, "", nameAndValue);
			} else {
				String[] valueComponents = nameAndValue.split(DELIMITER, 2);////StringUtils.split(nameAndValue, DELIMITER); don't use StringUtils.split() - it can actually return more than two elements - even though the specs say only two
				if (valueComponents.length == 1){
					message = new MetaMessage(metaKey, group, valueComponents[0], "");
				} else if (valueComponents.length == 2){
					message = new MetaMessage(metaKey, group, valueComponents[0], valueComponents[1]);
				} 
			}
			if (message == null){
				logger.warn("Message unexpectedly null!!");
			} else {
				message.setDate(meta.getUpdated());
				message.setUser(meta.getLastUpdatedByUser());
			}
		} 
		return message;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends MetaBase> List<MetaMessage> readAll(Integer modelParentId, Class<T> clazz, WaspDao<T> dao) {
		List<MetaMessage> results = new ArrayList<MetaMessage>();
		String parentClassName = StringUtils.substringBefore(clazz.getSimpleName(), "Meta");
		String modelParentIdEntityName = null;
		for (Field field : clazz.getDeclaredFields()){
			if (StringUtils.equalsIgnoreCase(field.getName(), WordUtils.uncapitalize(parentClassName) + "Id")){
				modelParentIdEntityName = field.getName();
				break;
			}
		}
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put(modelParentIdEntityName, modelParentId);
		List<String> orderByColumnNames = new ArrayList<String>();
		orderByColumnNames.add("updated");
		List<T> metaMatches = dao.findByMapOrderBy(searchMap, orderByColumnNames, "ASC");
		if (metaMatches != null &&  !metaMatches.isEmpty()){
			for (T meta: metaMatches){
				MetaMessage mm = getMetaMessage(meta);
				if(mm != null){
					results.add(mm);
				}
			}
		}
		return results;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends MetaBase> MetaMessage edit(MetaMessage message, String newValue, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) throws StatusMetaMessagingException{
		try{
			T meta = getUniqueMeta(message, modelParentId, clazz, dao);
			String name = message.getName(); 
			if (name != null){
				meta.setV(name + DELIMITER + newValue);
			} else {
				meta.setV(newValue);
			}
			return getMetaMessage(meta);
		} catch(WaspException e){
			throw new StatusMetaMessagingException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends MetaBase> void delete(MetaMessage message, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) throws WaspException{
		dao.remove(getUniqueMeta(message, modelParentId, clazz, dao));
	}
	
	private <T extends MetaBase> T getUniqueMeta(MetaMessage message, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) throws WaspException{
		Map<String, Object> searchMap = new HashMap<String, Object>();
		String parentClassName = StringUtils.substringBefore(clazz.getSimpleName(), "Meta");
		String modelParentIdEntityName = null;
		for (Field field : clazz.getDeclaredFields()){
			if (StringUtils.equalsIgnoreCase(field.getName(), WordUtils.uncapitalize(parentClassName) + "Id")){
				modelParentIdEntityName = field.getName();
				break;
			}
		}
		searchMap.put(modelParentIdEntityName, modelParentId);
		searchMap.put("k", message.getUniqueKey());
		List<T> metaMatches = dao.findByMap(searchMap);
		if (metaMatches == null || metaMatches.isEmpty() || metaMatches.size() > 1)
			throw new WaspException("Unable to retrieve MetaMessage");
		return metaMatches.get(0);
	}
	
	
}
