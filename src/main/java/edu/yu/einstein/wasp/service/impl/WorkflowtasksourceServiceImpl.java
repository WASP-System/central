
/**
 *
 * WorkflowtasksourceService.java 
 * @author echeng (table2type.pl)
 *  
 * the WorkflowtasksourceService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.WorkflowtasksourceService;
import edu.yu.einstein.wasp.dao.WorkflowtasksourceDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Workflowtasksource;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkflowtasksourceServiceImpl extends WaspServiceImpl<Workflowtasksource> implements WorkflowtasksourceService {

  private WorkflowtasksourceDao workflowtasksourceDao;
  @Autowired
  public void setWorkflowtasksourceDao(WorkflowtasksourceDao workflowtasksourceDao) {
    this.workflowtasksourceDao = workflowtasksourceDao;
    this.setWaspDao(workflowtasksourceDao);
  }
  public WorkflowtasksourceDao getWorkflowtasksourceDao() {
    return this.workflowtasksourceDao;
  }

  // **

  
  public Workflowtasksource getWorkflowtasksourceByWorkflowtasksourceId (final int workflowtasksourceId) {
    return this.getWorkflowtasksourceDao().getWorkflowtasksourceByWorkflowtasksourceId(workflowtasksourceId);
  }
}

