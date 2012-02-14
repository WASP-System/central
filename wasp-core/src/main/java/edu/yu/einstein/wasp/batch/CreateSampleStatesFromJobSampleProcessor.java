package edu.yu.einstein.wasp.batch;

import java.util.Date;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statejob;
import edu.yu.einstein.wasp.model.Statesample;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.service.StateService;
import edu.yu.einstein.wasp.service.StatejobService;
import edu.yu.einstein.wasp.service.StatesampleService;
import edu.yu.einstein.wasp.service.TaskService;

/**
 * Created State Processor
 * for each sample in jobsample for the state.statejob
 * simply creates a new State with targetTask and copies
 * the original stated from state Sample and creates a state sample out of it.
 * 
 */

@Component
public class CreateSampleStatesFromJobSampleProcessor implements ItemProcessor {

  @Autowired
  StateService stateService;

  @Autowired
  TaskService taskService;

  @Autowired
  StatesampleService statesampleService;

  @Autowired
  StatejobService statejobService;

  String targetTask; 
  public void setTargetTask(String targetTask) {
    this.targetTask = targetTask; 
  }

  TaskStatus targetStatus = TaskStatus.CREATED;

  @Override
  public State process(Object stateId) throws Exception {
    
    State state = stateService.getStateByStateId(((Integer) stateId).intValue());
    List<Statejob> stateJobs = state.getStatejob();

    Task t = taskService.getTaskByIName(targetTask); 

    for (Statejob sj: stateJobs) {
      for (JobSample js: sj.getJob().getJobSample()) {
        State newState = new State();
        newState.setStatus(targetStatus.toString());
        newState.setTaskId(t.getTaskId());
	newState.setSourceStateId((Integer) stateId);
        newState.setName(t.getName());
        newState.setStartts(new Date());
        State newStateDb = stateService.save(newState);

        Statejob newStateJob = new Statejob(); 
        newStateJob.setStateId(newStateDb.getStateId());
        newStateJob.setJobId(sj.getJobId());
      
        statejobService.save(newStateJob);
        Statesample newStateSample = new Statesample(); 
        newStateSample.setStateId(newStateDb.getStateId());
        newStateSample.setSampleId(js.getSampleId());

        statesampleService.save(newStateSample);
      }
    }

    return state;
  }
}

