
/**
 *
 * DAO to manage UIField table
 * @author Sasha Levchuk
 *
 **/
 
package edu.yu.einstein.wasp.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.UiField;


@Transactional
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
	String sql="SELECT DISTINCT area FROM  uifield ORDER BY area";
    return entityManager.createNativeQuery(sql).getResultList();
  }
  
  //returns true if a combination of locale, area, name, attrName already exists
  @Override
public boolean exists(final String locale, final String area, final String name, final String attrName) {
	String sql=
		   
		   	"select 1 from uifield \n"+
		   	"where locale=:locale\n"+
		   	"and area=:area\n"+
		   	"and name=:name\n"+
		   	"and attrname=:attrname\n";
		 
    Query q = entityManager.createNativeQuery(sql)
    .setParameter("locale", locale)
    .setParameter("area", area)
    .setParameter("name", name)
    .setParameter("attrname", attrName);
    
    return !q.getResultList().isEmpty();
		  
  }

  //utility functions to format SQL insert queries 
  private void process(StringBuffer result,String value) {
      if (value==null) {
    	  result.append("NULL");
      } else {
    	  String outputValue = value.toString();
          outputValue = outputValue.replaceAll("'","''");
          result.append("'"+outputValue+"'");
      }

  }
 /* 
  private void process(StringBuffer result,Integer value) {
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
			   
	  StringBuffer result = new StringBuffer("truncate table uifield;\n");
	  
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
}

