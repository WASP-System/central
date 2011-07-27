package edu.yu.einstein.wasp.controller;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.*;

import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.dao.impl.UserDaoImpl;
import edu.yu.einstein.wasp.model.User;

import javax.persistence.*;

public class WaspController implements Controller {

    protected final Log logger = LogFactory.getLog(getClass());

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("Returning wasp view");

        String now = (new Date()).toString();

        EntityManager em = null;
        User user = null;

        try{
          EntityManagerFactory emf = Persistence.createEntityManagerFactory("wasp1");
          em = emf.createEntityManager();

          UserDao userDao  = new UserDaoImpl();
          userDao.setEntityManager(em);
    
          EntityTransaction t = em.getTransaction();
          t.begin();
    
          user = userDao.getUserByUserId(28);
        } catch (Exception e) {
          System.out.println("YYYYYYYY");
          System.out.println(e.getMessage());
          System.out.println("ZZZZZZZZ");
          e.printStackTrace();
        } finally {
          if (em != null) {
          em.flush();
          em.close();
          }
        }

        Map<String, Object> viewModel = new HashMap<String, Object>();
        viewModel.put("now", now);
        viewModel.put("user", user);

        return new ModelAndView("wasp", "model", viewModel);
    }
}
