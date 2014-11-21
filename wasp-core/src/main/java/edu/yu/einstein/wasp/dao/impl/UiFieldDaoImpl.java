
/**
 *
 * DAO to manage UIField table
 * @author Sasha Levchuk
 *
 **/
 
package edu.yu.einstein.wasp.dao.impl;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.UiField;


@Transactional("entityManager")
@Repository
public class UiFieldDaoImpl extends WaspDaoImpl<UiField> implements edu.yu.einstein.wasp.dao.UiFieldDao {

  public UiFieldDaoImpl() {
    super();
    this.entityClass = UiField.class;
  }

  //returns list of unique areas
@SuppressWarnings("unchecked")
@Override
public List<String> getUniqueAreas() {
	String query="SELECT DISTINCT u.area FROM  UiField u ORDER BY u.area";
    return entityManager.createQuery(query).getResultList();
  }
  
  //returns true if a combination of locale, area, name, attrName already exists
  @Override
public boolean exists(final String locale, final String area, final String name, final String attrName) {
	String query=
		   	"SELECT 1 from UiField u \n"+
		   	"WHERE u.locale=:locale\n"+
		   	"AND u.area=:area\n"+
		   	"AND u.name=:name\n"+
		   	"AND u.attrName=:attrname\n";
		 
    Query q = entityManager.createQuery(query)
    .setParameter("locale", locale)
    .setParameter("area", area)
    .setParameter("name", name)
    .setParameter("attrname", attrName);
    
    return !q.getResultList().isEmpty();
		  
  }

  //utility functions to format SQL insert queries 
  private void process(StringBuilder result,String value) {
      if (value==null) {
    	  result.append("NULL");
      } else {
    	  String outputValue = value.toString();
          outputValue = outputValue.replaceAll("'","''");
          result.append("'"+outputValue+"'");
      }

  }
 /* 
  private void process(StringBuilder result,Integer value) {
      if (value==null) {
    	  result.append("NULL");
      } else {
    	  String outputValue = value.toString();         
          result.append(outputValue);
      }

  }
 */
  
  //returns content of UIField table as a list of SQL insert statements 
  @Override
public String dumpUiFieldTable() {
	  
	  final List<UiField> all = super.findAll();
			   
	  StringBuilder result = new StringBuilder("truncate table uifield;\n");
	  
	  for(UiField f:all) {
		  result.append("insert into uifield(locale,area,name,attrName,attrValue,lastupduser) values(");
		  process(result,f.getLocale());
		  result.append(",");
		  process(result,f.getArea());
		  result.append(",");
		  process(result,f.getName());
		  result.append(",");
		  process(result,f.getAttrName());
		  result.append(",");
		  process(result,f.getAttrValue());
		  result.append(",");
		  process(result,1+"");
		  result.append(");\n");
	  }

	  return result+"";
  }

	@Override
	public UiField get(String locale, String area, String name, String attrName) {
		String query = "SELECT u FROM UiField AS u " + 
						"WHERE locale=:locale " + 
						"AND area=:area " + 
						"AND name=:name " + 
						"AND attrname=:attrname";

		TypedQuery<UiField> q = entityManager.createQuery(query, UiField.class)
				.setParameter("locale", locale)
				.setParameter("area", area)
				.setParameter("name", name)
				.setParameter("attrname", attrName);

		if (!exists(locale, area, name, attrName)) {
			return null;
		}
		return q.getResultList().get(0);
	}
}

