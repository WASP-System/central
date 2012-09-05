package edu.yu.einstein.wasp.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.exception.StatusMetaMessagingException;
import edu.yu.einstein.wasp.model.MetaBase;

@Service
public interface StatusMessageService extends WaspService {
	
	/**
	 * Save status message in metadata model provided (clazz) via provided DAO. Message is defined with a key / value pair
	 * If data already exists under the provided key it will be overwritten.
	 * @param key
	 * @param value
	 * @param modelParentId
	 * @param clazz
	 * @param dao
	 * @return
	 * @throws StatusMetaMessagingException
	 */
	public <T extends MetaBase> T save(String key, String value, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) throws StatusMetaMessagingException;
	
	/**
	 * Returns a status message for the key specified for the modelParentId specified (UserId to UserMeta or JobId to JobMeta etc). Returns null if no match found.
	 * @param key
	 * @param modelParentId
	 * @param clazz
	 * @param dao
	 * @return
	 * @throws StatusMetaMessagingException
	 */
	public <T extends MetaBase> String read(String key, Integer modelParentId, Class<T> clazz, WaspDao<T> dao);
	
	/**
	 * Given a status message key and modelParentId (UserId to UserMeta or JobId to JobMeta etc), returns a MetaBase derived object of type
	 * specified in 'clazz'. Returns null if no match found.
	 * @param key
	 * @param modelParentId
	 * @param clazz
	 * @param dao
	 * @return
	 */
	public <T extends MetaBase> T getMetaForStatusMessageKey(String key, Integer modelParentId, Class<T> clazz, WaspDao<T> dao);
	
	/**
	 * Returns a Map of key-value pairs for all status messages associated with the modelParentId specified (UserId to UserMeta or JobId to JobMeta etc).
	 * Returns an empty list if no matches found.
	 * @param modelParentId
	 * @param clazz 
	 * @param dao
	 * @return
	 * @throws StatusMetaMessagingException
	 */
	public <T extends MetaBase> Map<String, String> readAll(Integer modelParentId, Class<T> clazz, WaspDao<T> dao);
}
