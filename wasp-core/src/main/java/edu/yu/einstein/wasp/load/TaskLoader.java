package edu.yu.einstein.wasp.load;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.load.service.TaskLoadService;
import edu.yu.einstein.wasp.model.TaskMapping;


/**
 * update/inserts db copy of task from bean definition
 * takes in	properties
 *	 - iname
 *	 - name
 *	 - uifields (List<UiFields)
 *
 */


public class TaskLoader extends WaspLoader {

	@Autowired
	TaskLoadService taskLoadService;
	
	public TaskLoader (){};

	private List<TaskMapping> taskMapping;
	public void setTaskMapping(List<TaskMapping> taskMapping) {
		this.taskMapping = taskMapping; 
	}


	@PostConstruct 
	  public void init() throws Exception{
		logger.info("task loader started for  " + iname);
		taskLoadService.update(iname, name, taskMapping);
		taskLoadService.updateUiFields(uiFields); 

	}
}

