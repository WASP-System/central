package edu.yu.einstein.wasp.batch;

import java.util.Date;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.dao.StatejobDao;
import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statejob;
import edu.yu.einstein.wasp.model.Task;

/**
 * Created Job Processor
 * simply creates a new State with targetTask and copies
 * the original jobid from state Job and creates a state job out of it.
 * 
 */

@Component
public class CreateJobStateProcessor implements ItemProcessor {

  @Autowired
  StateDao stateDao;

  @Autowired
  TaskDao taskDao;

  @Autowired
  StatejobDao statejobDao;

  String targetTask; 
  public void setTargetTask(String targetTask) {
    this.targetTask = targetTask; 
  }

  TaskStatus targetStatus = TaskStatus.CREATED;

  @Override
public State process(Object stateId) throws Exception {
    
    State state = stateDao.getStateByStateId(((Integer) stateId).intValue());

System.out.println("\nCreating " + targetTask + " for " + stateId);

    List<Statejob> stateJobs = state.getStatejob();

    Task t = taskDao.getTaskByIName(targetTask); 

    State newState = new State();
    newState.setStatus(targetStatus.toString());
    newState.setTaskId(t.getTaskId());
    newState.setName(t.getName());
    newState.setSourceStateId((Integer) stateId);
    newState.setStartts(new Date());
    State newStateDb = stateDao.save(newState);

    for (Statejob sj: stateJobs) {
      Statejob newStateJob = new Statejob(); 
      newStateJob.setStateId(newStateDb.getStateId());
      newStateJob.setJobId(sj.getJobId());

      statejobDao.save(newStateJob);
    }

    return newState;
  }
}

