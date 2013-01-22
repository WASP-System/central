/**
 *
 * WaspMetaDao.java
 * @author asmclellan
 * 
 * this is the base class for all meta DAOs,
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.WaspMetaDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.MetaBase;

@Repository
public abstract class WaspMetaDaoImpl<E extends MetaBase> extends WaspDaoImpl<E> implements WaspMetaDao<E> {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public List<E> setMeta(final List<E> metaList, final int modelParentId) throws MetadataException{
		List<E> returnMetaList = new ArrayList<E>();
		String parentClassName = StringUtils.substringBefore(entityClass.getSimpleName(), "Meta");
		String modelParentIdEntityIdSetterMethodName = null;
		for (Method method : entityClass.getMethods()){
			if (StringUtils.equalsIgnoreCase(method.getName(), "set" + parentClassName + "Id")){
				modelParentIdEntityIdSetterMethodName = method.getName();
				break;
			}
		}
		for (E meta : metaList) {
			try {
				entityClass.getMethod(modelParentIdEntityIdSetterMethodName, Integer.class).invoke(meta, modelParentId);
				returnMetaList.add(setMeta(meta));
			} catch (Exception e){
				 throw new MetadataException("Problem invoking method '" + modelParentIdEntityIdSetterMethodName + "'on instance of class '"+ entityClass.getName()+"' ", e);
			}
		}
		return returnMetaList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public E setMeta(final E meta) throws MetadataException{
		String parentClassName = StringUtils.substringBefore(entityClass.getSimpleName(), "Meta");
		String modelParentIdEntityName = null;
		for (Field field : entityClass.getDeclaredFields()){
			if (StringUtils.equalsIgnoreCase(field.getName(), WordUtils.uncapitalize(parentClassName) + "Id")){
				modelParentIdEntityName = field.getName();
				break;
			}
		}
		String modelParentIdEntityIdGetterMethodName = null;;
		for (Method method : entityClass.getMethods()){
			if (StringUtils.equalsIgnoreCase(method.getName(), "get" + parentClassName + "Id")){
				modelParentIdEntityIdGetterMethodName = method.getName();
				break;
			}
		}
		Integer modelParentId;
		try {
			modelParentId = (Integer) entityClass.getMethod(modelParentIdEntityIdGetterMethodName).invoke(meta);
		} catch (Exception e) {
			throw new MetadataException("Problem invoking method '" + modelParentIdEntityIdGetterMethodName + "'on instance of class '"+ entityClass.getName()+"' ", e);
		}
		if (modelParentId == null)
			throw new MetadataException("Meta object provided has no model parent id");
		Map<String, Object> m = new HashMap<String, Object>();
		m.put(modelParentIdEntityName, modelParentId);
		m.put("k", meta.getK());
		List<E> existingMetaList = findByMap(m);
		if (!existingMetaList.isEmpty()){
			E existingMeta = existingMetaList.get(0);
			if (!existingMeta.getV().equals(meta.getV())){
				existingMeta.setV(meta.getV());
			}
			return existingMeta;
		}
		// new meta
		return save(meta);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<E> getMeta(final int modelParentId) {
		String parentClassName = StringUtils.substringBefore(entityClass.getSimpleName(), "Meta");
		String modelParentIdEntityName = WordUtils.uncapitalize(parentClassName) + "Id";
		Map<String, Object> m = new HashMap<String, Object>();
		m.put(modelParentIdEntityName, modelParentId);
		return findByMap(m);
	}
}
