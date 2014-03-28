/**
 * 
 */
package edu.yu.einstein.wasp.interfacing.batch;

import java.util.List;
import java.util.UUID;

/**
 * Interface for abstracting away interrelated components of core and daemon related
 * to processing jobs that are expecting many associated jobs to complete.  This can be
 * described by accessing the StepExecution (of the listener, acessed via the job execution ID
 * and the step name), the Parent ID (UUID of the launching task) and the child ID list 
 * (list of children that were dispatched by the parent.
 * 
 * @author calder
 *
 */
public interface ManyJobRecipient {
    
    public Long getJobExecutionId();
    
    public String getStepName();
    
    public UUID getParentID();
    
    public List<String> getChildIDs();

}
