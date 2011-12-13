package edu.yu.einstein.wasp.load;

import edu.yu.einstein.wasp.service.TaskService;

import edu.yu.einstein.wasp.service.impl.WaspMessageSourceImpl;

import edu.yu.einstein.wasp.model.*;

import java.util.Map; 
import java.util.HashMap; 
import java.util.Set; 
import java.util.List; 
import java.util.Date; 
import java.util.ArrayList; 
import java.util.Locale; 

import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.*;

import org.springframework.context.MessageSource;

import org.springframework.util.StringUtils;

import util.spring.PostInitialize;


/**
 * update/inserts db copy of task from bean definition
 * takes in  properties
 *   - iname
 *   - name
 *   - uifields (List<UiFields)
 *
 */

@Transactional
public class TaskLoadService extends WaspLoadService {

  @Autowired
  private TaskService taskService;


  public TaskLoadService (){};

  @Transactional
  @PostInitialize 
  public void postInitialize() {
    // skips component scanned  (if scanned in)
    if (name == null) { return; }

    Task task = taskService.getTaskByIName(iname); 

    // inserts or update workflow
    if (task.getTaskId() == 0) { 
      task = new Task();

      task.setIName(iname);
      task.setName(name);

      taskService.save(task); 

      // refreshes
      task = taskService.getTaskByIName(iname); 

    } else {

      // TODO check if any data chance, if not don't update.
      task.setName(name);

      taskService.save(task); 
    }


    updateUiFields(iname, uiFields); 

  }

}

