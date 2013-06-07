************************************
Tasks
************************************

Description of WASP tasks.  WASP Tasks are semaphores between the web application, human interaction (HI) steps,
and the batch processor.


* WASP core tasks
  
  * Start Job
  * This is the only State that is explicitly created by the web application.
    This Task indicates to the batch system that a job has been correctly 
    submitted and is ready for processing.
  * None.
  * Create a State with a Task of type "Start Job" and a status
    of TaskStatus.CREATED.  Start Job States do not have a 
    parent State.
  * The batch processor polls for states of this type and will execute the
    back-end processing based on collected metadata.

* Quote Job

  * Created to indicate the progress of sending of the initial quote for a Job
    request.
  * Requires a running batch Job.
  * Create a State with a Task of type "Quote Job" and a status
    of TaskStatus.CREATED.  
  * When the quote has been confirmed as sent, the
    web/business layer changes the status to TaskStatus.COMPLETED.  
  * The batch processor will advance the state to TaskStatus.FINALIZED and proceed
    with flow processing.


