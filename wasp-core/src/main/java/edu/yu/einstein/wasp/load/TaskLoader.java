package edu.yu.einstein.wasp.load;

import java.util.List;

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


public class TaskLoader extends WaspLoader implements InitializingBean {

	@Autowired
	TaskLoadService taskLoadService;
	
	public TaskLoader (){};

	private List<TaskMapping> taskMapping;
	public void setTaskMapping(List<TaskMapping> taskMapping) {
		this.taskMapping = taskMapping; 
	}


	@Override 
	public void afterPropertiesSet() throws Exception{
		logger.info("task loader started for  " + iname);
		taskLoadService.update(iname, name, taskMapping);
		taskLoadService.updateUiFields(uiFields); 

	}
}

