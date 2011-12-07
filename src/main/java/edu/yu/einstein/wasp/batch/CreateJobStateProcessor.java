package edu.yu.einstein.wasp.batch;

import edu.yu.einstein.wasp.model.*;
import edu.yu.einstein.wasp.service.*;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.support.transaction.FlushFailedException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Date;

/**
 * Created Job Processor
 * simply creates a new State with targetTask and copies
 * the original jobid from state Job and creates a state job out of it.
 * 
 */

@Component
public class CreateJobStateProcessor implements ItemProcessor {

  @Autowired
  StateService stateService;

  @Autowired
  TaskService taskService;

  @Autowired
  StatejobService statejobService;

  String targetTask; 
  public void setTargetTask(String targetTask) {
    this.targetTask = targetTask; 
  }

  String targetStatus = "CREATED"; 

  public State process(Object stateId) throws Exception {
    
    State state = stateService.getStateByStateId(((Integer) stateId).intValue());

System.out.println("\nCreating " + targetTask + " for " + stateId);

    List<Statejob> stateJobs = state.getStatejob();

    Task t = taskService.getTaskByIName(targetTask); 

    State newState = new State();
    newState.setStatus(targetStatus);
    newState.setTaskId(t.getTaskId());
    newState.setName(t.getName());
    newState.setStartts(new Date());
    State newStateDb = stateService.save(newState);

    for (Statejob sj: stateJobs) {
      Statejob newStateJob = new Statejob(); 
      newStateJob.setStateId(newStateDb.getStateId());
      newStateJob.setJobId(sj.getJobId());

      statejobService.save(newStateJob);
    }

    return newState;
  }
}

