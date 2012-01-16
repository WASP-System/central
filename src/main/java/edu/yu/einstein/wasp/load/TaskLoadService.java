package edu.yu.einstein.wasp.load;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import util.spring.PostInitialize;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.service.TaskService;


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

  @Override
@Transactional
  @PostInitialize 
  public void postInitialize() {
    // skips component scanned  (if scanned in)
    if (name == null) { return; }

    Task task = taskService.getTaskByIName(iname); 

    // inserts or update workflow
    if (task.getTaskId() == null) { 
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

