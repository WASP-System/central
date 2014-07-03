/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

/**
 * @author calder
 *
 */
interface SubmissionScript {

	public abstract String getFlag();

	public abstract String getMemory();

	public abstract String getProcs();

	/**
	 * @return the account
	 */
	public abstract String getAccount();

	/**
	 * @param account the account to set
	 */
	public abstract void setAccount(String account);

	/**
	 * @return the queue
	 */
	public abstract String getQueue();

	/**
	 * @param queue the queue to set
	 */
	public abstract void setQueue(String queue);

	/**
	 * @return the maxRunTime
	 */
	public abstract String getMaxRunTime();

	/**
	 * @param maxRunTime the maxRunTime to set
	 */
	public abstract void setMaxRunTime(String maxRunTime);

	/**
	 * @return the availableParallelEnvironments
	 */
	public abstract String getParallelEnvironment();

	/**
	 * @param availableParallelEnvironments the availableParallelEnvironments to set
	 */
	public abstract void setParallelEnvironment(String parallelEnvironment, Integer procs);

	/**
	 * @return the project
	 */
	public abstract String getProject();

	/**
	 * @param project the project to set
	 */
	public abstract void setProject(String project);

	/**
	 * @return the mailRecipient
	 */
	public abstract String getMailRecipient();

	/**
	 * @param mailRecipient the mailRecipient to set
	 */
	public abstract void setMailRecipient(String mailRecipient);

	/**
	 * @return the mailCircumstances
	 */
	public abstract String getMailCircumstances();

	/**
	 * @param mailCircumstances the mailCircumstances to set
	 */
	public abstract void setMailCircumstances(String mailCircumstances);

	/**
	 * 
	 * @return
	 */
	public boolean isNumProcConsumable();

	/**
	 * 
	 * @param isNumProcConsumable
	 */
	public void setNumProcConsumable(boolean isNumProcConsumable);

}
