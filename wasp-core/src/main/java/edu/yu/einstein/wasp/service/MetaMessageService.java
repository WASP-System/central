package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.MetaMessage;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.exception.StatusMetaMessagingException;
import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.model.MetaBase;

@Service
public interface MetaMessageService extends WaspService {
	
	/**
	 * Save status message in MetaBase derived model specified (clazz) via provided DAO (e.g.UserMetaDao). Message is defined with a group, name and value 
	 * (e.g. save("fmMessage", "Quality Control Notes", "Job ran without problem", jobId, JobMeta.class, jobMetaDao); ) 
	 * Multiple entries can be added using the same group (they are returned as a list when read)
	 * @param group
	 * @param name
	 * @param value
	 * @param modelParentId
	 * @param clazz
	 * @param dao
	 * @return
	 * @throws StatusMetaMessagingException
	 */
	public <T extends MetaBase> MetaMessage saveToGroup(String group, String name, String value, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) throws StatusMetaMessagingException;
	
	/**
	 * Save status message in MetaBase derived model specified (clazz) via provided DAO (e.g.UserMetaDao). Message is defined with a group and value.
	 * (e.g. save("fmMessage", "Job ran without problem", jobId, JobMeta.class, jobMetaDao); ) 
	 * Multiple entries can be added using the same group (they are returned as a list when read)
	 * @param group
	 * @param value
	 * @param modelParentId
	 * @param clazz
	 * @param dao
	 * @return
	 * @throws StatusMetaMessagingException
	 */
	public <T extends MetaBase> MetaMessage saveToGroup(String group, String value, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) throws StatusMetaMessagingException;
	
	
	/**
	 * Save status message in MetaBase derived model specified (clazz) via provided DAO (e.g.UserMetaDao). Message is defined with a group and value 
	 * using the default group and a name may be specified (e.g. save("Quality Control Notes", "Job ran without problem", jobId, JobMeta.class, jobMetaDao); ) 
	 * Multiple entries can be added using the same group (they are returned as a list when read)
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
	 * Save status message in MetaBase derived model specified (clazz) via provided DAO (e.g.UserMetaDao). Message is defined with a group and value 
	 * using the default group. (e.g. save("Job ran without problem", jobId, JobMeta.class, jobMetaDao); ) 
	 * Multiple entries can be added using the same group (they are returned as a list when read)
	 * @param value
	 * @param modelParentId
	 * @param clazz
	 * @param dao
	 * @return
	 * @throws StatusMetaMessagingException
	 */
	public <T extends MetaBase> MetaMessage save(String value, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) throws StatusMetaMessagingException;
	
	/**
	 * Returns a list of all messages added to the group specified for the modelParentId specified (UserId to UserMeta or JobId to JobMeta etc). 
	 * Returns empty list if no match found.
	 * @param group
	 * @param modelParentId
	 * @param clazz
	 * @param dao
	 * @return
	 * @throws StatusMetaMessagingException
	 */
	public <T extends MetaBase> List<MetaMessage> read(String group, Integer modelParentId, Class<T> clazz, WaspDao<T> dao);
	
	/**
	 * Returns a status message using the default group for the modelParentId specified (UserId to UserMeta or JobId to JobMeta etc). Returns null if no match found.
	 * @param modelParentId
	 * @param clazz
	 * @param dao
	 * @return
	 * @throws StatusMetaMessagingException
	 */
	public <T extends MetaBase> List<MetaMessage> read(Integer modelParentId, Class<T> clazz, WaspDao<T> dao);
	
	/**
	 * Returns a list of MetaMessages for all status messages associated with the modelParentId specified (UserId to UserMeta or JobId to JobMeta etc).
	 * Returns an empty list if no matches found.
	 * @param modelParentId
	 * @param clazz 
	 * @param dao
	 * @return
	 * @throws StatusMetaMessagingException
	 */
	public <T extends MetaBase> List<MetaMessage> readAll(Integer modelParentId, Class<T> clazz, WaspDao<T> dao);

	/**
	 * Edit the value of a MetaMessage
	 * @param message
	 * @param newValue
	 * @param modelParentId
	 * @param clazz
	 * @param dao
	 * @return
	 * @throws WaspException
	 */
	public <T extends MetaBase> MetaMessage edit(MetaMessage message, String newValue, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) throws WaspException;

	
	/**
	 * Delete a MetaMessage
	 * @param message
	 * @param newValue
	 * @param modelParentId
	 * @param clazz
	 * @param dao
	 * @throws WaspException
	 */
	public <T extends MetaBase> void delete(MetaMessage message, String newValue, Integer modelParentId, Class<T> clazz, WaspDao<T> dao) throws WaspException;
}
