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

import java.util.Date; 
import java.util.List; 

import edu.yu.einstein.wasp.dao.DepartmentDao;
import edu.yu.einstein.wasp.dao.impl.DepartmentDaoImpl;
import edu.yu.einstein.wasp.model.*;

@Controller
@Transactional
@RequestMapping("/department")
public class DepartmentController {

  private DepartmentDao departmentDao;
  @Autowired
  public void setDepartmentDao(DepartmentDao departmentDao) {
    this.departmentDao = departmentDao;
  }
  public DepartmentDao getDepartmentDao() {
    return this.departmentDao;
  }


  @RequestMapping("/list")
  public String list(ModelMap m) {
    List<Department> departmentList = this.getDepartmentDao().findAll();
    
    m.addAttribute("department", departmentList);

    return "department/list";
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

    Department department = this.getDepartmentDao().getById(i.intValue());

    // List<DepartmentMeta> departmentMetaList = department.getDepartmentMeta();
    // departmentMetaList.size();

    List<Lab> labList = department.getLab();
    labList.size();

    List<DepartmentUser> departmentUserList = department.getDepartmentUser();
    departmentUserList.size();

    m.addAttribute("now", now);
    m.addAttribute("department", department);
    m.addAttribute("lab", labList);
    m.addAttribute("departmentuser", departmentUserList);

    return "department/detail";
  }


}
