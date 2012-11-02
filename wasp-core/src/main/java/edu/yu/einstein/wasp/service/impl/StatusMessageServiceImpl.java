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

import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.exception.StatusMetaMessagingException;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.service.StatusMessageService;

/**
 * Message metadata saving and retrieval. Can store chronological metadata about the status of an entity via a key - value system. Ideal for
 * Displaying status information to users.
 * @author andymac
 *
 */
@Service
@Transactional
public class StatusMessageServiceImpl extends WaspServiceImpl implements StatusMessageService{
	
	protected Logger logger = LoggerFactory.getLogger(StatusMessageServiceImpl.class);
	
	private static final String STATUS_KEY_PREFIX = "statusMessage."; 
	
	private static final String DEFAULT_KEY = "default";
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends MetaBase> T save(String key, String value, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) throws StatusMetaMessagingException{
		String parentClassName = StringUtils.substringBefore(clazz.getSimpleName(), "Meta");
		String modelParentIdEntityName = WordUtils.uncapitalize(parentClassName) + "Id";
		String modelParentIdEntityIdSetterMethodName = "set" + parentClassName + "Id";
		
		T meta = getMetaForStatusMessageKey(key, modelParentId, clazz, dao); // get existing if present
		if (meta == null){
			logger.debug("Metadata doesn't exist for current message key '" + key + "' and " + modelParentIdEntityName + "=" + modelParentId + ". Creating new instance.");
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
			
			meta.setK(key);
		} else {
			logger.debug("Metadata already exists for current message key '" + key + "' and " + modelParentIdEntityName + "=" + modelParentId + ". Updating with new value.");
		}
		
		meta.setV(value);
		return dao.save(meta);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends MetaBase> T save(String value, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) throws StatusMetaMessagingException{
		return save(DEFAULT_KEY, value, modelParentId, clazz, dao);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends MetaBase> String read(String key, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) {
		T metaMatch = getMetaForStatusMessageKey(key, modelParentId, clazz, dao);
		if (metaMatch == null)
			return null;
		return metaMatch.getV();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends MetaBase> String read(Integer modelParentId, Class<T> clazz, WaspDao<T> dao) {
		return read(DEFAULT_KEY,  modelParentId, clazz, dao);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends MetaBase> T getMetaForStatusMessageKey(String key, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) {
		String parentClassName = StringUtils.substringBefore(clazz.getSimpleName(), "Meta");
		String modelParentIdEntityName = WordUtils.uncapitalize(parentClassName) + "Id";
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put(modelParentIdEntityName, modelParentId);
		searchMap.put("k", STATUS_KEY_PREFIX + key);
		List<String> orderByColumnNames = new ArrayList<String>();
		orderByColumnNames.add("lastUpdTs");
		List<T> metaMatches = dao.findByMapOrderBy(searchMap, orderByColumnNames, "ASC");
		if (metaMatches != null &&  !metaMatches.isEmpty()){
			if (metaMatches.size() > 1)
				logger.warn("Got more than one match with key='" + key + "'. Only returning first value in list");
			return metaMatches.get(0);
		}
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends MetaBase> Map<String, String> readAll(Integer modelParentId, Class<T> clazz, WaspDao<T> dao) {
		Map<String, String> results = new LinkedHashMap<String, String>(); // use LinkedHashMap to maintain order of entries in the Map
		String parentClassName = StringUtils.substringBefore(clazz.getSimpleName(), "Meta");
		String modelParentIdEntityName = WordUtils.uncapitalize(parentClassName) + "Id";
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put(modelParentIdEntityName, modelParentId);
		List<String> orderByColumnNames = new ArrayList<String>();
		orderByColumnNames.add("lastUpdTs");
		List<T> metaMatches = dao.findByMapOrderBy(searchMap, orderByColumnNames, "ASC");
		if (metaMatches != null &&  !metaMatches.isEmpty()){
			for (T meta: metaMatches){
				if (meta.getK().startsWith(STATUS_KEY_PREFIX))
					results.put(StringUtils.substringAfter(meta.getK(), STATUS_KEY_PREFIX), meta.getV());
			}
		}
		return results;
	}
}
