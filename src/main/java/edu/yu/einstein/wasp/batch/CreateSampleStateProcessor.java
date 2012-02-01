package edu.yu.einstein.wasp.batch;

import java.util.Date;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statejob;
import edu.yu.einstein.wasp.model.Statesample;
import edu.yu.einstein.wasp.model.Staterun;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.service.StateService;
import edu.yu.einstein.wasp.service.StatejobService;
import edu.yu.einstein.wasp.service.StatesampleService;
import edu.yu.einstein.wasp.service.StaterunService;
import edu.yu.einstein.wasp.service.TaskService;

/**
 * Created State Processor
 * simply creates a new State with targetTask and copies
 * the original stated from state Sample and creates a state sample out of it.
 * 
 */

@Component
public class CreateSampleStateProcessor implements ItemProcessor {

  @Autowired
  StateService stateService;

  @Autowired
  TaskService taskService;

  @Autowired
  StatesampleService statesampleService;

  @Autowired
  StatejobService statejobService;

  @Autowired
  StaterunService staterunService;

  String targetTask; 
  public void setTargetTask(String targetTask) {
    this.targetTask = targetTask; 
  }

  String targetStatus = "CREATED"; 

  @Override
public State process(Object stateId) throws Exception {
    
    State state = stateService.getStateByStateId(((Integer) stateId).intValue());

    List<Statesample> stateSamples = state.getStatesample();
    List<Statejob> stateJobs = state.getStatejob();
    List<Staterun> stateRuns = state.getStaterun();

    Task t = taskService.getTaskByIName(targetTask); 

    State newState = new State();
    newState.setStatus(targetStatus);
    newState.setTaskId(t.getTaskId());
    newState.setName(t.getName());
    newState.setSourceStateId((Integer) stateId);
    newState.setStartts(new Date());
    State newStateDb = stateService.save(newState);

    for (Statesample ss: stateSamples) {
      Statesample newStateSample = new Statesample(); 
      newStateSample.setStateId(newStateDb.getStateId());
      newStateSample.setSampleId(ss.getSampleId());

      statesampleService.save(newStateSample);
    }

    for (Statejob sj: stateJobs) {
      Statejob newStateJob = new Statejob(); 
      newStateJob.setStateId(newStateDb.getStateId());
      newStateJob.setJobId(sj.getJobId());
      
      statejobService.save(newStateJob);
    }

    for (Staterun sr: stateRuns) {
      Staterun newStateRun = new Staterun(); 
      newStateRun.setStateId(newStateDb.getStateId());
      newStateRun.setRunId(sr.getRunId());
      
      staterunService.save(newStateRun);
    }

    return newState;
  }
}

