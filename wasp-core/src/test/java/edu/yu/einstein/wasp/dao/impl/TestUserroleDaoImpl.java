package edu.yu.einstein.wasp.dao.impl;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.orm.jpa.JpaUnitils;
import org.unitils.orm.jpa.annotation.JpaEntityManagerFactory;
import org.unitils.reflectionassert.ReflectionAssert;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;

import edu.yu.einstein.wasp.dao.UserroleDao;
import edu.yu.einstein.wasp.model.WRole;
import edu.yu.einstein.wasp.model.Userrole;


/**
 * 
 * 
 * @author nvolnova
 *
 */

@JpaEntityManagerFactory(persistenceUnit = "testWaspPU", configFile = "META-INF/wasp-persistence-test.xml")
@DataSet ("TestUserRoleDaoImpl.xml")
@Transactional(TransactionMode.ROLLBACK)
public class TestUserroleDaoImpl {

	/**
     * Injects a test specific application context configuration
     */
	@SpringApplicationContext
	public ConfigurableApplicationContext createApplicationContext() {
		 return new ClassPathXmlApplicationContext("META-INF/application-context-test.xml");
	  }
		
	@SpringBean("userroleDao")
	UserroleDao userroleDao;
	
	@PersistenceContext
	EntityManager entityManager;
	
	
	@Test (groups = "db-tests")
    public void testMappingToDatabase() {        
      JpaUnitils.assertMappingWithDatabaseConsistent();
    }
	
	@Test (groups = "db-tests")
    public void testGetUserroleByUserroleId() {
		
		HashMap m = new HashMap();
		m.put("id", 1);
		List<Userrole> results = userroleDao.findByMap(m);
		Userrole actual = results.get(0);
		WRole role = actual.getRole();
			
		//ReflectionAssert.assertLenientEquals(expected,actual); //  ReflectionComparatorMode.LENIENT_ORDER - ignoring the order of elements
		
		//example
		ReflectionAssert.assertLenientEquals("rolename","su", role.getRoleName());
		
    }
	
  @BeforeClass
   public void beforeClass() throws IOException {
	  
	  userroleDao = new UserroleDaoImpl();
	  JpaUnitils.injectEntityManagerInto(userroleDao);
 }
  

  @AfterClass
  public void afterClass() {
  }

}
