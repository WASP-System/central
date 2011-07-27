/**
 * @author echeng
 *
 */

package edu.yu.einstein.wasp;

import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import edu.yu.einstein.wasp.dao.DepartmentDao;
import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.dao.ProjectDao;
import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.dao.impl.DepartmentDaoImpl;
import edu.yu.einstein.wasp.dao.impl.JobDaoImpl;
import edu.yu.einstein.wasp.dao.impl.LabDaoImpl;
import edu.yu.einstein.wasp.dao.impl.ProjectDaoImpl;
import edu.yu.einstein.wasp.dao.impl.UserDaoImpl;
import edu.yu.einstein.wasp.model.Department;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.Project;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Userrole;

public class HUser {

  public static void main (String[] args) {
   
    Logger log = Logger.getLogger("name");

    EntityManager em = null;
    try{

      EntityManagerFactory emf = Persistence.createEntityManagerFactory("wasp1");
      em = emf.createEntityManager();

      UserDao userDao = new UserDaoImpl();
      userDao.setEntityManager(em);

      DepartmentDao departmentDao = new DepartmentDaoImpl();
      departmentDao.setEntityManager(em);

      EntityTransaction t = em.getTransaction(); 
      t.begin();

      //Create new instance of Contact and set values in it by reading them from form object

      // User adminUser = (User) em.find(User.class, 1);

      User adminUser = userDao.getUserByUserId(1);

      Userrole userrole = (Userrole) em.find(Userrole.class, 1);

      User u = userDao.getUserByUserId(32);
      System.out.println("CCCCCCC" + u.getLabUser().get(0).getLab().getName());


//      System.out.println("XXXXXXX" + em);
 //     System.out.println("XXXXXXX" + userrole);
  //    System.out.println("---------------- " + userrole.getUser().getLogin());
//
 //     System.out.println("XXXXXXX" + adminUser.getUserrole().size());

      Department dept = departmentDao.getDepartmentByDepartmentId(1);
      System.out.println("XXXXXXX" + dept.getName());
      // Department dept = new Department(); 
      // newDepartment.setName("new dept");
      // em.persist(dept);
      //
      Random g = new Random(); 
      int r = g.nextInt(10000000); 

      JobDao jobDao = new JobDaoImpl();
      jobDao.setEntityManager(em);
      Job job = jobDao.getJobByJobId(10095);
    List<JobSample> jobSampleList = job.getJobSample();
    jobSampleList.size();


      User adminUser2 = userDao.getUserByLogin("superuser");
      adminUser2.setFirstName("Bob " + r); 
      userDao.save(adminUser2);

      LabDao labDao = new LabDaoImpl();
      labDao.setEntityManager(em);

      Lab newLab = new Lab(); 
      newLab.setName("new lab" + r );

      System.out.println("DDDDDDDDDDDDDDDDD");

      // newLab.setDepartment(dept);
      newLab.setDepartmentId(dept.getDepartmentId());
      newLab.setPrimaryUserId(adminUser2.getUserId());
      labDao.save(newLab);
      // userDao.refresh(adminUser2);

      System.out.println("EEEEEEEEEEEE");

      ProjectDao projectDao = new ProjectDaoImpl();
      projectDao.setEntityManager(em);

      Project newProj = new Project();
      newProj.setName("new Proj" + r); 
      newProj.setLab(newLab);
      projectDao.save(newProj);

      Project newProj2 = new Project();
      newProj2.setName("new Proj two" + r); 
      newProj2.setLab(newLab);
      projectDao.save(newProj2);

      System.out.println("AAAA" + dept.getName());
      em.refresh(newLab);
      System.out.println("BBBBB" + dept.getName());

      System.out.println("JJJJJJJ" + newLab.getProject().size());

      /*
      // User adminUser3 = userDao.getUserByLogin(em, "DOESNOTEXIST");

      User echengUser = userDao.getUserByLogin(em, "echeng");

      System.out.println("XXXXXXX1");
      log.info(echengUser.getUserId()); 

      System.out.println("XXXXXXX2");
      */

      log.info(adminUser.getLogin()); 
      System.out.println("XXXXXXX3");
      log.info(adminUser2.getUserId()); 
      // log.info(adminUser3); 


      System.out.println("Inserting Record");

    

      User newUser = new User();
      newUser.setLogin("echeng" + r);
      newUser.setFirstName("Ed");
      newUser.setLastName("Cheng");
      newUser.setEmail("ed@boylesoftware.com" + r );
      newUser.setPassword("Abc123" + r);

      userDao.save(newUser);
      t.commit();
      em.close();
      em = null;

      System.out.println("Done");
    } catch (Exception e) {
      System.out.println("YYYYYYYY");
      System.out.println(e.getMessage());
      System.out.println("ZZZZZZZZ");
      e.printStackTrace();
    } finally {
      // Actual contact insertion will happen at this step
      if (em != null) {
        em.flush();
        em.close();
      }

    }
  
  }
}

