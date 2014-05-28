/**
 *
 * WaspDao.java
 * @author echeng
 * 
 * this is the base class,
 * every class in the system should extend this.
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.sql.DataSource;

import org.apache.commons.lang.WordUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.ModelDetachException;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.UserService;


@SuppressWarnings("unchecked")
@Transactional("entityManager")
@Repository
public abstract class WaspDaoImpl<E extends Serializable> extends WaspPersistenceDaoImpl implements edu.yu.einstein.wasp.dao.WaspDao<E> {
	protected Class<E>	entityClass;

	// generic logger included with every class.
	private Logger logger = LoggerFactory.getLogger(WaspDaoImpl.class.getName());
	
	@Autowired
	private UserService userService;
	
	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;
	
	@Override
	public boolean isAttached(final E entity){
		return entityManager.contains(entity);
	}

	@Override
	public void persist(final E entity) {
		setEditorId(entity);
		logEntityFieldDetailsOnCRUD(entity, "persisting");
		entityManager.persist(entity);
	}

	@Override
	public E save(E entity) {

		setEditorId(entity);
		logEntityFieldDetailsOnCRUD(entity, "saving");
		try{
			entityManager.persist(entity);
		} catch (Exception e){
			// try merge instead.
			entity = entityManager.merge(entity);
		}
		entityManager.flush();
		entityManager.refresh(entity);
		logEntityFieldDetailsOnCRUD(entity, "returning");
		return entity;
	}

	@Override
	public void remove(E entity) {
		logEntityFieldDetailsOnCRUD(entity, "removing");
		entityManager.remove(entity);
	}

	@Override
	public E merge(E entity) {
		logEntityFieldDetailsOnCRUD(entity, "merging");
		return entityManager.merge(entity);
	}

	@Override
	public void refresh(E entity) {
		entityManager.refresh(entity);
	}

	@Override
	public E findById(int id) {
		return entityManager.find(this.entityClass, id);
	}

	@Override
	public E getById(int id) {
		return entityManager.find(this.entityClass, id);
	}

	@Override
	public E flush(E entity) {
		entityManager.flush();
		return entity;
	}

	@Override
	
	public List<E> findAll() {
		return this.entityManager.createQuery("FROM " + entityClass.getName()).getResultList();
	}

	@Override
	public Integer removeAll() {
		return entityManager.createQuery("DELETE FROM " + entityClass.getName() + " h").executeUpdate();
	}

	@Override
	public List<E> findByMap(final Map<?, ?> m) {
		boolean first = true;
		String qString = "SELECT h FROM " + entityClass.getName() + " h";
		for (Object key : m.keySet()) {
			if (!first) {
				qString += " and ";
			} else {
				qString += " WHERE ";
			}

			qString += "h." + key.toString() + " = :" + key.toString().replaceAll("\\W+", "") + "\n";
			first = false;
		}

		Query q = entityManager.createQuery(qString);

		for (Object key : m.keySet()) {
			q.setParameter(key.toString().replaceAll("\\W+", ""), m.get(key));
		}
		return q.getResultList();
	}

	@Override
	
	public List<E> findByMapExcept(final Map<?, ?> m) {
		boolean first = true;
		String qString = "SELECT h FROM " + entityClass.getName() + " h";
		for (Object key : m.keySet()) {
			if (!first) {
				qString += " and ";
			} else {
				qString += " WHERE ";
			}

			qString += "h." + key.toString() + " != :" + key.toString().replaceAll("\\W+", "") + "\n";
			first = false;
		}

		Query q = entityManager.createQuery(qString);

		for (Object key : m.keySet()) {
			q.setParameter(key.toString().replaceAll("\\W+", ""), m.get(key));
		}
		logger.debug("qString=" + q.toString());

		return q.getResultList();
	}
	
	
	@Override
	
	public List<E> findByMapDistinctOrderBy(final Map<?, ?> m, final List<String> distinctColumnNames, final List<String> orderByColumnNames, final String direction) {
		boolean where = false;
		boolean firstMap = true;
		boolean firstDistinct = true;
		boolean firstOrderBy = true;

		String qString = "SELECT h FROM " + entityClass.getName() + " h";

		for (Object key : m.keySet()) {
			if (where == false) {
				qString += " WHERE ";
				where = true;
			} else if (firstMap == false) {
				qString += " and ";
			}
			qString += "h." + key.toString() + " = :" + key.toString().replaceAll("\\W+", "");
			firstMap = false;
		}
		if (distinctColumnNames != null && !"".equals(distinctColumnNames)) {
			for (String distinctColumnName : distinctColumnNames) {
				if (where == false) {
					qString += " WHERE (";
					where = true;
				} else if (firstDistinct == true) {
					qString += " AND (";
				} else if (firstDistinct == false) {
					qString += ", ";
				}

				qString += "h." + distinctColumnName;
				firstDistinct = false;
			}
			if (firstDistinct == false) {
				qString += ") IN (SELECT DISTINCT";
				firstDistinct = true;

				for (String distinctColumnName : distinctColumnNames) {

					if (firstDistinct == false) {
						qString += ", ";
					}
					qString += " j." + distinctColumnName;
					firstDistinct = false;
				}
			}
			if (firstDistinct == false) {
				qString += " FROM " + entityClass.getName() + " j) ";
			}
		}
		if (orderByColumnNames != null && !orderByColumnNames.isEmpty()) {
			for (String orderByColumnName : orderByColumnNames) {
				if (firstOrderBy == true) {
					qString += " ORDER BY ";
				} else if (firstOrderBy == false) {
					qString += ", ";
				}
				qString += "h." + orderByColumnName;
				if (direction != null && !"".equals(direction)) {
					qString += " " + direction;
				}
				firstOrderBy = false;
			}
			//if (firstOrderBy == false && direction != null && !"".equals(direction)) {
			//	qString += " " + direction;
			//}
		}
		// logger.debug("ROBERT: " + qString);
		
		Query q = entityManager.createQuery(qString);

		for (Object key : m.keySet()) {
			q.setParameter(key.toString().replaceAll("\\W+", ""), m.get(key));
		}

		return q.getResultList();
	}
	
	@Override
	public List<E> findByMapOrderBy(final Map<?, ?> m, final List<String> orderByColumnNames, final String direction) {
		return findByMapDistinctOrderBy(m, null, orderByColumnNames, direction);
	}

	@Override
	
	public List<E> findByMapsIncludesDatesDistinctOrderBy(final Map<?, ?> m, final Map<?, ?> dateMap, List<String> distinctColumnNames, final List<String> orderByColumnAndDirectionList) {
		boolean where = false;
		//boolean firstMap = true;
		boolean firstDistinct = true;
		boolean firstOrderBy = true;

		String qString = "SELECT h FROM " + entityClass.getName() + " h";

		for (Object key : m.keySet()) {
			if (where == false) {
				qString += " WHERE ";				
			} 
			else{
				qString += " and ";
			}
			qString += "h." + key.toString() + " = :" + key.toString().replaceAll("\\W+", "");
			where = true;
		}		
		for (Object key : dateMap.keySet()) {
			if (where == false){
				qString += " WHERE ";				
			} 
			else{
				qString += " and ";
			}
			qString += "DATE(h." + key.toString() + ") = :" + key.toString().replaceAll("\\W+", "");
			where = true;
		}
		if (distinctColumnNames != null && !"".equals(distinctColumnNames)) {
			for (String distinctColumnName : distinctColumnNames) {
				if (where == false) {
					qString += " WHERE (";
					where = true;
				} else if (firstDistinct == true) {
					qString += " AND (";
				} else if (firstDistinct == false) {
					qString += ", ";
				}

				qString += "h." + distinctColumnName;
				firstDistinct = false;
			}
			if (firstDistinct == false) {
				qString += ") IN (SELECT DISTINCT";
				firstDistinct = true;

				for (String distinctColumnName : distinctColumnNames) {

					if (firstDistinct == false) {
						qString += ", ";
					}
					qString += " j." + distinctColumnName;
					firstDistinct = false;
				}
			}
			if (firstDistinct == false) {
				qString += " FROM " + entityClass.getName() + " j) ";
			}
		}
		if (orderByColumnAndDirectionList != null && !orderByColumnAndDirectionList.isEmpty()) {
			for (String orderByColumnAndDirection : orderByColumnAndDirectionList) {
				if (firstOrderBy == true) {
					qString += " ORDER BY ";
				} else if (firstOrderBy == false) {
					qString += ", ";
				}
				qString += "h." + orderByColumnAndDirection;
				firstOrderBy = false;
			}
		}
		
		Query q = entityManager.createQuery(qString);

		for (Object key : m.keySet()) {
			q.setParameter(key.toString().replaceAll("\\W+", ""), m.get(key));
		}
		for (Object key : dateMap.keySet()) {
			q.setParameter(key.toString().replaceAll("\\W+", ""), dateMap.get(key));
		}
		
		// logger.debug("ROBERT: " + qString);
		

		return q.getResultList();
	}
	
	@Override
	
	public List<E> findDistinctOrderBy(final String distinctColumnName, final String orderByColumnName, final String direction) {

		String qString = "SELECT h FROM " + entityClass.getName() + " h";

		if (distinctColumnName != null && !"".equals(distinctColumnName)) {
			qString += " WHERE h." + distinctColumnName + " IN (SELECT DISTINCT j." + distinctColumnName + " FROM " + entityClass.getName() + " j)";
		}

		if (orderByColumnName != null && !"".equals(orderByColumnName) && direction != null && "".equals(direction)) {
			qString += " ORDER BY h." + orderByColumnName;
		} else if (orderByColumnName != null && !"".equals(orderByColumnName) && direction != null && !"".equals(direction)) {
			qString += " ORDER BY h." + orderByColumnName + " " + direction;
		}
		// logger.debug("ROBERT: " + qString);
		Query q = entityManager.createQuery(qString);
		return q.getResultList();

	}

	@Override
	
	public List<E> findAllOrderBy(final String orderByColumnName, final String direction) {
		String qString = "SELECT h FROM " + entityClass.getName() + " h";
		if (!"".equals(orderByColumnName) && "".equals(direction)) {
			qString += " ORDER BY h." + orderByColumnName;
		} else if (!"".equals(orderByColumnName) && !"".equals(direction)) {
			qString += " ORDER BY h." + orderByColumnName + " " + direction;
		}
		Query q = entityManager.createQuery(qString);
		return q.getResultList();
	}

	private void setEditorId(E entity) {
            try {
                    Method setLastUpdatedUserMethod = entity.getClass().getMethod("setLastUpdatedByUser", User.class);
                    // org.springframework.security.core.userdetails.User u=
                    // (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    User user = null;
                    try{
                            
                            try{
                                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                                    if (auth != null) {
                                        final String login = SecurityContextHolder.getContext().getAuthentication().getName();
                                        user = userService.getUserByLogin(login);
                                    }
                                    if (user == null || user.getId() == null) {
                                    	user = userService.getUserDao().getUserByLogin("wasp"); // wasp user (reserved)
                                    }
                                    if (user == null || user.getId() == null) {
                                        logger.warn("attempting creation of wasp user");
                                        JdbcTemplate t = new JdbcTemplate(dataSource);
                                        String ins = "INSERT INTO wuser (id, firstName, lastName, login, email, locale) VALUES (1, 'wasp', 'wasp', 'wasp', 'wasp', 'en_US')";
                                        int i = t.update(ins);
                                        logger.debug("insert " + i + " user");
                                        user = userService.getUserDao().getUserByLogin("wasp"); 
                                    }
                            } catch (Exception e){
                                e.printStackTrace();
                                    logger.warn("final attempt to get wasp user");
                                    user = userService.getUserDao().getUserByLogin("wasp"); // wasp user (reserved)
                            }
                            setLastUpdatedUserMethod.invoke(entity, user);
                    } catch (Exception e){
                            logger.debug("not attempting setting last updating user of " + entity.getClass().getName() + " as not able to resolve a user to assign");
                    }
                    

            } catch (Exception e) {
                    // likely no such method setLastUpdUser in class E
                    logger.warn("attempted setting last updating user of " + entity.getClass().getName() + " resulted in failure because method for setting user was not found");
            }
    }
	
	@SuppressWarnings("rawtypes")
	@Override
	public List findDistinctMetaOrderBy(final String metaKeyName, final String direction){
		Map<String, String> metaQueryMap = new HashMap<String, String>();
		metaQueryMap.put("k", metaKeyName);
	  	List<String> orderByList = new ArrayList<String>();
	  	orderByList.add("v");
		return this.findByMapDistinctOrderBy(metaQueryMap, orderByList, orderByList, direction); 
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Deprecated
	public E getEagerLoadedDetachedEntity(E entity) throws ModelDetachException{
		try{
			this.merge(entity); // ensures attached to the session to start with
		} catch(Exception e){
			throw new ModelDetachException("Cannot merge supplied entity. Maybe it is not persisted ", e);
		}
		// execute Hibernate.initialize on all public getter methods of the entity (including inherited) to eager load all data
		for (Method m: entityClass.getMethods()){
			if (! m.getName().startsWith("get")) continue;
			try {
				Hibernate.initialize(entityClass.getMethod(m.getName()).invoke(entity));
			} catch (Exception e) {
				logger.debug("Failed executing 'Hibernate.initialize()' on entity method '"+ m.getName()+"'", e);
			} 
		}
		entityManager.detach(entity);
		return entity;
	}
	
	private void logEntityFieldDetailsOnCRUD(E entity, String actionName){
		if (!logger.isDebugEnabled())
			return;
		logger.trace(WordUtils.capitalize(actionName)+" entity of type: "+entity.getClass().getName()+"...");
		Map<String,Field> fields = new HashMap<String, Field>();
		if (entity.getClass().getName().endsWith("Meta"))
			for (Field field : entity.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredFields())
				fields.put(field.getName(), field);
		for (Field field : entity.getClass().getSuperclass().getSuperclass().getDeclaredFields())
			fields.put(field.getName(), field);
		for (Field field : entity.getClass().getSuperclass().getDeclaredFields())
			fields.put(field.getName(), field);
		for (Field field : entity.getClass().getDeclaredFields())
			fields.put(field.getName(), field);

		for (Field field : fields.values()){
			field.setAccessible(true);
			try {
				logger.trace("    -> "+field.getName()+"="+field.get(entity).toString());
			} catch (Exception e) {
			}
			
		}
	}

}
