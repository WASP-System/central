
/**
 *
 * UserImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the User object
 *
 *
 **/
 
package edu.yu.einstein.wasp.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.UiField;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class UiFieldDaoImpl extends WaspDaoImpl<UiField> implements edu.yu.einstein.wasp.dao.UiFieldDao {

  public UiFieldDaoImpl() {
    super();
    this.entityClass = UiField.class;
  }

  public List<String> getUniqueAreas() {
	  Object res = getJpaTemplate().execute(new JpaCallback() {

	   public Object doInJpa(EntityManager em) throws PersistenceException {
		   String sql="SELECT DISTINCT area FROM  uifield ORDER BY area";
	    Query q = em.createNativeQuery(sql);
	    return q.getResultList();
	   }

	  });

	  return (List<String>) res;
	 }
  
  public boolean exists(final String locale, final String area, final String name, final String attrName) {
	  Object res = getJpaTemplate().execute(new JpaCallback() {

		   public Object doInJpa(EntityManager em) throws PersistenceException {
			   String sql=
				   "select 1 from uifield f where exists (\n"+
				   	"select 1 from uifield \n"+
				   	"where locale=:locale\n"+
				   	"and area=:area\n"+
				   	"and name=:name\n"+
				   	"and attrname=:attrname\n"+
				   ")\n";
				   
				   
		    Query q = em.createNativeQuery(sql)
		    .setParameter("locale", locale)
		    .setParameter("area", area)
		    .setParameter("name", name)
		    .setParameter("attrname", attrName);
		    
		    return q.getResultList();
		   }

		  });

		  return !((List)res).isEmpty();
		  
	 }
  
}

