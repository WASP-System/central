package edu.yu.einstein.wasp.load;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.load.service.TaskMappingLoadService;
import edu.yu.einstein.wasp.model.TaskMapping;


/**
 * update/inserts db copy of task from bean definition
 * takes in	properties
 *	 - iname
 *	 - name
 *	 - uifields (List<UiFields)
 *
 */


public class TaskMappingLoader extends WaspLoader {

	@Autowired
	private TaskMappingLoadService taskMappingLoadService;
	
	public TaskMappingLoader (){};

	private List<TaskMapping> taskMapping;
	public void setTaskMapping(List<TaskMapping> taskMapping) {
		this.taskMapping = taskMapping; 
	}


	@PostConstruct 
	  public void init() throws Exception{
		logger.info("task loader started for taskMapping "+ iname);
		taskMappingLoadService.update(iname, name, taskMapping);
		taskMappingLoadService.updateUiFields(uiFields); 

	}
}

