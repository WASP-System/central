package edu.yu.einstein.wasp.controller;


import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.transaction.annotation.*; 

import org.springframework.security.access.prepost.*;

import java.util.Date; 
import java.util.List; 

import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.dao.impl.TaskDaoImpl;
import edu.yu.einstein.wasp.model.*;

@Controller
@Transactional
@RequestMapping("/task")
public class TaskController {

  private TaskDao taskDao;
  @Autowired
  public void setTaskDao(TaskDao taskDao) {
    this.taskDao = taskDao;
  }
  public TaskDao getTaskDao() {
    return this.taskDao;
  }


  @RequestMapping("/list")
  public String list(ModelMap m) {
    List <Task> taskList = this.getTaskDao().findAll();
    
    m.addAttribute("task", taskList);

    return "task/list";
  }

  @RequestMapping(value="/detail/{strId}", method=RequestMethod.GET)
  public String detail(@PathVariable("strId") String strId, ModelMap m) {
    String now = (new Date()).toString();

    Integer i;
    try {
      i = new Integer(strId);
    } catch (Exception e) {
      return "default";
    }

    Task task = this.getTaskDao().getById(i.intValue());

    List<State> stateList = task.getState();
    stateList.size();

    m.addAttribute("now", now);
    m.addAttribute("task", task);
    m.addAttribute("state", stateList);

    return "task/detail";
  }
}
