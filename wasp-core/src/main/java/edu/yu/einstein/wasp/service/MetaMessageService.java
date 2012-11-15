package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.MetaMessage;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.exception.StatusMetaMessagingException;
import edu.yu.einstein.wasp.model.MetaBase;

@Service
public interface MetaMessageService extends WaspService {
	
	/**
	 * Save status message in MetaBase derived model specified (clazz) via provided DAO (e.g.UserMetaDao). Message is defined with a key / value pair and a 
	 * name may be specified (e.g. save("fmMessage", "Quality Control Notes", "Job ran without problem", jobId, JobMeta.class, jobMetaDao); ) 
	 * Multiple entries can be added using the same key (they are returned as a list when read)
	 * @param key
	 * @param name
	 * @param value
	 * @param modelParentId
	 * @param clazz
	 * @param dao
	 * @return
	 * @throws StatusMetaMessagingException
	 */
	public <T extends MetaBase> MetaMessage saveWithKey(String key, String name, String value, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) throws StatusMetaMessagingException;
	
	/**
	 * Save status message in MetaBase derived model specified (clazz) via provided DAO (e.g.UserMetaDao). Message is defined with a key / value pair.
	 * (e.g. save("fmMessage", "Job ran without problem", jobId, JobMeta.class, jobMetaDao); ) 
	 * Multiple entries can be added using the same key (they are returned as a list when read)
	 * @param key
	 * @param value
	 * @param modelParentId
	 * @param clazz
	 * @param dao
	 * @return
	 * @throws StatusMetaMessagingException
	 */
	public <T extends MetaBase> MetaMessage saveWithKey(String key, String value, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) throws StatusMetaMessagingException;
	
	
	/**
	 * Save status message in MetaBase derived model specified (clazz) via provided DAO (e.g.UserMetaDao). Message is defined with a key / value pair 
	 * using the default key and a name may be specified (e.g. save("Quality Control Notes", "Job ran without problem", jobId, JobMeta.class, jobMetaDao); ) 
	 * Multiple entries can be added using the same key (they are returned as a list when read)
	 * @param name
	 * @param value
	 * @param modelParentId
	 * @param clazz
	 * @param dao
	 * @return
	 * @throws StatusMetaMessagingException
	 */
	public <T extends MetaBase> MetaMessage save(String name, String value, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) throws StatusMetaMessagingException;
	
	/**
	 * Save status message in MetaBase derived model specified (clazz) via provided DAO (e.g.UserMetaDao). Message is defined with a key / value pair 
	 * using the default key. (e.g. save("Job ran without problem", jobId, JobMeta.class, jobMetaDao); ) 
	 * Multiple entries can be added using the same key (they are returned as a list when read)
	 * @param value
	 * @param modelParentId
	 * @param clazz
	 * @param dao
	 * @return
	 * @throws StatusMetaMessagingException
	 */
	public <T extends MetaBase> MetaMessage save(String value, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) throws StatusMetaMessagingException;
	
	/**
	 * Returns a list of all messages added using the key specified for the modelParentId specified (UserId to UserMeta or JobId to JobMeta etc). 
	 * Returns empty list if no match found.
	 * @param key
	 * @param modelParentId
	 * @param clazz
	 * @param dao
	 * @return
	 * @throws StatusMetaMessagingException
	 */
	public <T extends MetaBase> List<MetaMessage> read(String key, Integer modelParentId, Class<T> clazz, WaspDao<T> dao);
	
	/**
	 * Returns a status message using the default key for the modelParentId specified (UserId to UserMeta or JobId to JobMeta etc). Returns null if no match found.
	 * @param key
	 * @param modelParentId
	 * @param clazz
	 * @param dao
	 * @return
	 * @throws StatusMetaMessagingException
	 */
	public <T extends MetaBase> List<MetaMessage> read(Integer modelParentId, Class<T> clazz, WaspDao<T> dao);
	
	/**
	 * Returns a Map of key-value pairs for all status messages associated with the modelParentId specified (UserId to UserMeta or JobId to JobMeta etc).
	 * Returns an empty list if no matches found.
	 * @param modelParentId
	 * @param clazz 
	 * @param dao
	 * @return
	 * @throws StatusMetaMessagingException
	 */
	public <T extends MetaBase> List<MetaMessage> readAll(Integer modelParentId, Class<T> clazz, WaspDao<T> dao);
}
