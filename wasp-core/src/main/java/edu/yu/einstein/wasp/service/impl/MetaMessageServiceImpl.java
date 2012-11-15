package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.MetaMessage;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.exception.StatusMetaMessagingException;
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
	
	private static final String DEFAULT_KEY = "default";
	
	private static final String DELIMITER = "::";
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends MetaBase> MetaMessage saveWithKey(String key, String value, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) throws StatusMetaMessagingException{
		return saveWithKey(key, null, value, modelParentId,clazz, dao);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends MetaBase> MetaMessage saveWithKey(String key,  String name, String value, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) throws StatusMetaMessagingException{
		String parentClassName = StringUtils.substringBefore(clazz.getSimpleName(), "Meta");
		String modelParentIdEntityName = WordUtils.uncapitalize(parentClassName) + "Id";
		String modelParentIdEntityIdSetterMethodName = "set" + parentClassName + "Id";
		T meta = null;
		logger.debug("Creating new instance of Metadata for current message key '" + key + "' and " + modelParentIdEntityName + "=" + modelParentId );
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
		meta.setK(STATUS_KEY_PREFIX + key + DELIMITER + read(key, modelParentId, clazz, dao).size());
		MetaMessage message = null;
		if (name != null){
			meta.setV(name + DELIMITER + value);
			message =  new MetaMessage(key, name, value);
		} else {
			meta.setV(value);
			message =  new MetaMessage(key, "", value);
		}
		dao.save(meta);
		return message;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends MetaBase> MetaMessage save(String name, String value, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) throws StatusMetaMessagingException{
		return saveWithKey(DEFAULT_KEY, name, value, modelParentId, clazz, dao);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends MetaBase> MetaMessage save(String value, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) throws StatusMetaMessagingException{
		return saveWithKey(DEFAULT_KEY, value, modelParentId, clazz, dao);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends MetaBase> List<MetaMessage> read(String key, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) {
		List<MetaMessage> results = new ArrayList<MetaMessage>();
		for (MetaMessage message : readAll(modelParentId, clazz, dao)){
			if (message.getKey().equals(key))
				results.add(message);
		}
		return results;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends MetaBase> List<MetaMessage> read(Integer modelParentId, Class<T> clazz, WaspDao<T> dao) {
		return read(DEFAULT_KEY,  modelParentId, clazz, dao);
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends MetaBase> List<MetaMessage> readAll(Integer modelParentId, Class<T> clazz, WaspDao<T> dao) {
		List<MetaMessage> results = new ArrayList<MetaMessage>();
		String parentClassName = StringUtils.substringBefore(clazz.getSimpleName(), "Meta");
		String modelParentIdEntityName = WordUtils.uncapitalize(parentClassName) + "Id";
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put(modelParentIdEntityName, modelParentId);
		List<String> orderByColumnNames = new ArrayList<String>();
		orderByColumnNames.add("lastUpdTs");
		List<T> metaMatches = dao.findByMapOrderBy(searchMap, orderByColumnNames, "ASC");
		if (metaMatches != null &&  !metaMatches.isEmpty()){
			for (T meta: metaMatches){
				if (meta.getK().startsWith(STATUS_KEY_PREFIX)){
					String trimmedKey = StringUtils.substringBetween(meta.getK(), STATUS_KEY_PREFIX, DELIMITER);
					String[] valueComponents = StringUtils.split(meta.getV(), DELIMITER);
					if (valueComponents.length == 1){
						results.add(new MetaMessage(trimmedKey, "", valueComponents[0]));
					} else if (valueComponents.length == 2){
						results.add(new MetaMessage(trimmedKey, valueComponents[0], valueComponents[1]));
					} else {
						results.add(new MetaMessage(trimmedKey, "", ""));
					}
				}
			}
		}
		return results;
	}
	
	
}
