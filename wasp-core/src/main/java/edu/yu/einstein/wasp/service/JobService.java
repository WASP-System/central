/**
 *
 * JobService.java 
 * @author dubin
 *  
 * the JobService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.yu.einstein.wasp.MetaMessage;
import edu.yu.einstein.wasp.Strategy;
import edu.yu.einstein.wasp.dao.JobCellSelectionDao;
import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.JobDraftDao;
import edu.yu.einstein.wasp.dao.JobMetaDao;
import edu.yu.einstein.wasp.dao.JobResourcecategoryDao;
import edu.yu.einstein.wasp.dao.JobSampleDao;
import edu.yu.einstein.wasp.dao.JobSoftwareDao;
import edu.yu.einstein.wasp.dao.JobUserDao;
import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.dao.RoleDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleJobCellSelectionDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.exception.FileMoveException;
import edu.yu.einstein.wasp.exception.FileUploadException;
import edu.yu.einstein.wasp.exception.JobContextInitializationException;
import edu.yu.einstein.wasp.exception.QuoteException;
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.model.AcctQuote;
import edu.yu.einstein.wasp.model.AcctQuoteMeta;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.quote.MPSQuote;
import edu.yu.einstein.wasp.util.WaspJobContext;
import edu.yu.einstein.wasp.viewpanel.JobDataTabViewing;

@Service
public interface JobService extends WaspMessageHandlingService {

	public static final String REPLICATE_SETS_META_KEY = "replicateSets";
	
	/**
	 * setJobDao(JobDao jobDao)
	 * 
	 * @param jobDao
	 * 
	 */
	public void setJobDao(JobDao jobDao);

	/**
	 * getJobDao();
	 * 
	 * @return jobDao
	 * 
	 */
	public JobDao getJobDao();

	/**
	 * getJobByJobId();
	 * 
	 * @return Job
	 * 
	 */
	public Job getJobByJobId(Integer jobId);

	/**
	 * get a list of samples for the submitted job. Returns an empty list if none found.
	 * 
	 * @param Job
	 * 
	 * @return List<Sample>
	 */
	public List<Sample> getSubmittedSamples(Job job);
	
	/**
	 * getSubmittedSamplesNotYetReceived
	 * @param Job 
	 * @return List<Sample>
	 */
	public List<Sample> getSubmittedSamplesNotYetReceived(Job job);
	
	/**
	 * getSubmittedSamplesNotYetQC
	 * @param Job 
	 * @return List<Sample>
	 */
	public List<Sample> getSubmittedSamplesNotYetQC(Job job);
	
	/**
	 * getLibrariesNotYetReceived
	 * @param Job 
	 * @return List<Sample>
	 */
	public List<Sample> getLibrariesNotYetQC(Job job);

	/**
	 * getActive Jobs() returns list of active jobs (executing)
	 * @param none
	 * @return List<Job>
	 * 
	 */
	public List<Job> getActiveJobs();
	
	/**
	 * getJobsAwaitingReceivingOfSamples() returns list of jobs which include samples not registered as received yet.
	 * @param none
	 * @return List<Job>
	 * 
	 */
	public List<Job> getJobsAwaitingReceivingOfSamples();
	
	/**
	 * returns list of jobs awaiting QC of at least one Sample
	 * @return
	 */
	public List<Job> getJobsAwaitingSampleQC();
	
	
	/**
	 * returns true if any samples in the job are awaiting QC
	 * @param job
	 * @return
	 */
	public boolean isJobAwaitingSampleQC(Job job);

	/**
	 * returns true if any jobs exist which are awaiting QC of a Sample
	 * @return
	 */
	public boolean isJobsAwaitingSampleQC();

	/**
	 * returns list of jobs awaiting QC of at least one Library
	 * @return
	 */
	public List<Job> getJobsAwaitingLibraryQC();

	/**
	 * returns true if any jobs exist which are awaiting QC of a library
	 * @return
	 */
	public boolean isJobsAwaitingLibraryQC();
	
	/**
	 *  returns true if jobs is awaiting QC of a library
	 * @param job
	 * @return
	 */
	public boolean isJobAwaitingLibraryQC(Job job);
	
	/**
	 * returns true if job is awaiting QC of cell-library
	 * @param job
	 * @return
	 */
	public boolean isJobAwaitingCellLibraryQC(Job job);
	
	/**
	 * returns true if any job is awaiting cell-library QC
	 * @return
	 */
	public boolean isJobsAwaitingCellLibraryQC();

	
	/**
	 * sortJobsByJobId 
	 * @param List<Job>
	 * @return void
	 */
	public void sortJobsByJobId(List<Job> jobs);
	
	/**
	 * getExtraJobDetails
	 * @param Job job
	 * @return Map<String,String> for easy display on web. first string is a fmt:message stored in messages.properties which acts as a label; second string is displayed value, such as a cost value or a machine name
	 */
	public LinkedHashMap<String, String> getExtraJobDetails(Job job);

	/**
	 * getJobApprovals
	 * @param Job job
	 * @return Map<String,String> for easy display on web. both strings are fmt:message stored in messages.properties file
	 */
	public LinkedHashMap<String, String> getJobApprovals(Job job);

	/**
	 * Create a Job object from a job draft. Also handle dependencies.
	 * @param jobdraft
	 * @return entity-managed Job object
	 * @throws FileMoveException
	 */
	public Job createJobFromJobDraft(JobDraft jobdraft, User user) throws FileMoveException;
	
	/**
	 * getJobsAwaitingLibraryCreation() returns list of unique jobs for which at least one library must be created from a sample.
	 * @param none
	 * @return List<Job>
	 * 
	 */
	public List<Job> getJobsAwaitingLibraryCreation();
	
	/**
	 * Returns true if any libraries need creating for the current job
	 * @param job
	 * @return
	 */
	public boolean isJobAwaitingLibraryCreation(Job job);
	
	/**
	 * getJobsWithLibrariesToGoOnPlatformUnit() returns list of unique jobs with libraries created (and passed QC) for which the actual coverage on
	 * currently running or successfully completed flowcells is less than the requested coverage. Only returns those jobs for which the resource category matches that specified.
	 * @param ResourceCategory
	 * @return List<Job>
	 * 
	 */
	public List<Job> getJobsWithLibrariesToGoOnPlatformUnit(ResourceCategory resourceCategory);
	
	/**
	 * getJobsWithLibrariesToGoOnPlatformUnit() returns list of unique jobs with libraries created (and passed QC) for which the actual coverage on
	 * currently running or successfully completed flowcells is less than the requested coverage.
	 * @param none
	 * @return List<Job>
	 * 
	 */
	public List<Job> getJobsWithLibrariesToGoOnPlatformUnit();
	
	/**
	 * Returns true if job has libraries created (and passed QC) for which the actual coverage on
	 * currently running or successfully completed flowcells is less than the requested coverage.
	 * @param job
	 * @return
	 */
	public boolean isJobWithLibrariesToGoOnPlatformUnit(Job job);
	
	/**
	 * getJobsSubmittedOrViewableByUser() returns list of jobs that was submitted by, as well as viewable by, a specific user. 
	 * Note that some jobs viewable to the user may have been submitted by someone else.
	 * Note that a PI can see there own jobs AND jobs submitted by members of their lab
	 * Note that returned job list is ordered by jobId/descending
	 * @param User user
	 * @return List<Job>
	 * 
	 */
	public List<Job> getJobsSubmittedOrViewableByUser(User user);
	
	/**
	 * Returns true if provided job is awaiting PI approval, otherwise returns false
	 * @param job
	 * @return
	 */
	public boolean isJobAwaitingPiApproval(Job job);

	
	/**
	 * Returns true if provided job is awaiting administrator approval, otherwise returns false
	 * @param job
	 * @return
	 */
	public boolean isJobAwaitingDaApproval(Job job);
	
	/**
	 * Returns true if provided job is awaiting facility manager approval, otherwise returns false
	 * @param job
	 * @return
	 */
	public boolean isJobAwaitingFmApproval(Job job);
	
	/**
	 * Returns true if provided job is awaiting quoting, otherwise returns false
	 * @param job
	 * @return
	 */
	public boolean isJobAwaitingQuote(Job job);
	
	/**
	 * Returns true if job is awaiting approval or Quote
	 * @param job
	 * @return
	 */
	public boolean isJobPendingApprovalOrQuote(Job job);
	
	/**
	 * Returns true if provided sample has no library has been made yet, otherwise returns false
	 * @param sample
	 * @return
	 */
	public boolean isJobAwaitingLibraryCreation(Job job, Sample sample);
	
	/**
	 * Updates the Job Quote Status for job.
	 * Status must be either COMPLETED or ABANDONED
	 * @param jobId
	 * @param status
	 * @throws WaspMessageBuildingException
	 */
	public void updateJobQuoteStatus(Job job, WaspStatus status) throws WaspMessageBuildingException;
	
	/**
	 * Calls updateJobDAApprovalStatus and updates the Job DA approval Status if jobApproveCode is daApprove
	 * Calls updateJobFmApprovalStatus and updates the Job FM approval Status if jobApproveCode is fmApprove
	 * Calls updateJobPiApprovalStatus and updates the Job PI approval Status if jobApproveCode is piApprove
	 * Status must be either COMPLETED or ABANDONED
	 * jobApproveCode must be daApprove, fmApprove, or piApprove
	 * @param String jobApproveCode
	 * @param Job job
	 * @param String status
	 * @throws WaspMessageBuildingException
	 */
	public void updateJobApprovalStatus(String jobApproveCode, Job job, WaspStatus status, String comment) throws WaspMessageBuildingException;
	
	
	/**
	 * Updates the Job DA approval Status for job
	 * Status must be either COMPLETED or ABANDONED
	 * @param Job job
	 * @param String status
	 * @throws WaspMessageBuildingException
	 */
	public void updateJobDaApprovalStatus(Job job, WaspStatus status, String comment) throws WaspMessageBuildingException;
	
	/**
	 * Updates the Job Pi Approval Status for job
	 * Status must be either COMPLETED or ABANDONED
	 * @param Job job
	 * @param String status
	 * @throws WaspMessageBuildingException
	 */
	public void updateJobPiApprovalStatus(Job job, WaspStatus status, String comment) throws WaspMessageBuildingException;
	
	/**
	 * Updates the Job Facility Manager Approval Status for job
	 * Status must be either COMPLETED or ABANDONED
	 * @param Job job
	 * @param String status
	 * @throws WaspMessageBuildingException
	 */
	public void updateJobFmApprovalStatus(Job job, WaspStatus status, String comment) throws WaspMessageBuildingException;

	/**
	 * removeJobViewer() removes a viewer from a specific job. Performs checks to determine if this is a legal option and if not, throw exception 
	 * @param Integer jobId (the job from which the viewer is to be removed)
	 * @param Integer UserId (the user to be removed as a jobviewer)
	 * @return void (maybe should be an int or boolean)
	 * 
	 */
	public void removeJobViewer(Integer jobId, Integer userId) throws Exception;
	
	/**
	 * addJobViewer() adds a viewer to a specific job. Performs checks to determine if this is a legal option and if not, throw exception 
	 * @param Integer jobId (the job to which the viewer is to be added)
	 * @param String newViewerEmailAddress (the new viewer's email address)
	 * @return void (maybe should be an int or boolean)
	 * 
	 */
	public void addJobViewer(Integer jobId, String newViewerEmailAddress) throws Exception;

	/**
	 * Kick of batch job for job
	 * @param job
	 * @throws WaspMessageBuildingException
	 */
	public void initiateBatchJobForJobSubmission(Job job) throws WaspMessageBuildingException;
	
	/**
	 * Returns true if job is awaiting receiving of sample
	 * @param job
	 * @return
	 */
	public boolean isJobAwaitingReceivingOfSamples(Job job);


	/**
	 * Returns true if there are jobs awaiting receiving of sample
	 * @return
	 */
	public boolean isJobsAwaitingReceivingOfSamples();

	
	/**
	 * save a Facility-generated Job Comment
	 * @param Integer jobId
	 * @param String comment
	 * @return void
	 * @throws Exception
	 */
	public void setFacilityJobComment(Integer jobId, String comment)throws Exception;
	
	/**
	 * get all facility job comments (supposedly chronologically ordered)
	 * @param Integer jobId
	 * @return List<MetaMessage>
	 */
	public List<MetaMessage> getAllFacilityJobComments(Integer jobId);
	
	/**
	 * save a User-submitted Job Comment
	 * @param Integer jobId
	 * @param String comment
	 * @return void
	 * @throws Exception
	 */
	public void setUserSubmittedJobComment(Integer jobId, String comment) throws Exception;
	
	/**
	 * get user-submitted job comment (there will be zero or one only)
	 * @param Integer jobId
	 * @return List<MetaMessage>
	 */
	public List<MetaMessage> getUserSubmittedJobComment(Integer jobId);

	/**
	 * get a list of all jobs awaiting quoting (returns an empty list if none)
	 * @return
	 */
	public List<Job> getJobsAwaitingQuote();
	
	/**
	 * get a list of all jobs awaiting facility manager approval (returns an empty list if none)
	 * @return
	 */
	public List<Job> getJobsAwaitingFmApproval();

	
	/**
	 * get a list of all jobs awaiting PI/ Lm approval (returns an empty list if none)
	 * @return
	 */
	public List<Job> getJobsAwaitingPiLmApproval();

	
	/**
	 * get a list of all jobs awaiting department administrator approval (returns an empty list if none)
	 * @return
	 */
	public List<Job> getJobsAwaitingDaApproval();

	/**
	 * returns true if one or more jobs are awaiting quoting
	 * @return
	 */
	public boolean isJobsAwaitingQuote();

	public void setSampleService(SampleService sampleService);


	/**
	 * returns Map of sample and string in form of (for example) 11010
	 * where (in this example) first, second, and fourth cells contains the sample 
	 * @return Map<Sample, String>
	 */
	public Map<Sample, String> getCoverageMap(Job job);

	public void setSampleDao(SampleDao mockSampleDao);

	public void setJobDao(SampleDao mockSampleDao);

	public void setJobMetaDao(JobMetaDao mockJobMetaDao);

	public void setJobSoftwareDao(JobSoftwareDao mockJobSoftwareDao);

	public void setJobResourcecategoryDao(
			JobResourcecategoryDao mockJobResourcecategoryDao);

	public void setRoleDao(RoleDao mockRoleDao);

	public void setJobUserDao(JobUserDao mockJobUserDao);

	public void setLabDao(LabDao mockLabDao);

	public void setJobCellSelectionDao(
			JobCellSelectionDao mockJobCellSelectionDao);

	public void setSampleMetaDao(SampleMetaDao mockSampleMetaDao);

	public void setSampleJobCellSelectionDao(
			SampleJobCellSelectionDao mockSampleJobCellSelectionDao);

	public void setJobSampleDao(JobSampleDao mockJobSampleDao);

	public void setJobDraftDao(JobDraftDao mockJobDraftDao);

	public void setLogger(Logger logger);

	public void setSampleTypeDao(SampleTypeDao mockSampleTypeDao);

	public WaspJobContext getWaspJobContext(Job job) throws JobContextInitializationException;

	public void setJobApprovalComment(String jobApproveCode, Integer jobId, String comment) throws Exception;
	
	public List<MetaMessage> getJobApprovalComments(String jobApproveCode, Integer jobId);
	
	public HashMap<String, MetaMessage> getLatestJobApprovalsComments(Set<String> jobApproveCodeSet, Integer jobId);
	
	public String getJobStatus(Job job);

	public List<Software> getSoftwareForJob(Job job);


	/**
	 * getJobViewBranch() returns a data structure to resemble a job-sample tree for showing with D3 JScript library.
	 * @param jid 
	 * @param int id, String type
	 * @return Map<String, Object>
	 * @throws Exception 
	 */
	public Map<String, Object> getTreeViewBranch(Integer id, Integer pid, String type, Integer jid) throws SampleTypeException, SampleParentChildException;

	public List<Sample> getPlatformUnitWithLibrariesOnForJob(Job job);

	/**
	 * getJobDetailWithMeta() returns all detail information with meta for a job
	 * @param jobId
	 * @return
	 * @throws Exception
	 */
	public LinkedHashMap<String, Object> getJobDetailWithMeta(int jobId) throws Exception;

	public boolean isJobActive(Job job);
	
	/**
	 * returns true if at least one cell library from all submitted samples has either been QCd or has failed primary analysis
	 * @param job
	 * @return
	 */
	public boolean isQcPerformedOrPreprocessingFailedForAtLeastOneCellLibraryForAllSubmittedSamples(Job job);

	/**
	 * return true if any sample associated with the job is in any stage of processing.
	 * @param job
	 * @return
	 */
	boolean isAnySampleCurrentlyBeingProcessed(Job job);

	/**
	 * Send message to wasp-daemon to trigger aggregation analysis for a job
	 * @param job
	 */
	public void initiateAggregationAnalysisBatchJob(Job job);

	public String getJobStatusComment(Job job);

	/**
	 * return list of active jobs that do NOT have samples in the pipleline (samples either failed or ran sucessfully)
	 * @return List<Job>
	 */
	public List<Job> getActiveJobsWithNoSamplesCurrentlyBeingProcessed();
	
	/**
	 * returns true if an aggregation analysis batch job exists (in any state) for the given wasp job
	 * @param job
	 * @return
	 */
	public boolean isAggregationAnalysisBatchJob(Job job);

	/**
	 * returns true if job was stopped (terminated).
	 * @param job
	 * @return
	 */
	public boolean isTerminated(Job job);
	
	/**
	 * returns true if job finished with a successful status 
	 * @param job
	 * @return
	 */
	public boolean isFinishedSuccessfully(Job job);

	/**
	 * Terminate a currently running job
	 * @param job
	 * @throws WaspMessageBuildingException
	 */
	public void terminate(Job job) throws WaspMessageBuildingException;

	
	/**
	 * As the method title states, set the job approval status and the comment that goes along with it in a single transaction.
	 * Throw exception if problem
	 * @param String jobApproveCode (fmApprove, daApprove, piApprove)
	 * @param Job job
	 * @param WaspStatus status
	 * @param String comment
	 * @throws WaspMessageBuildingException
	 */
	public void setJobApprovalStatusAndComment(String jobApproveCode, Job job, WaspStatus status, String comment) throws Exception;
	//public void updateJobApprovalStatus(String jobApproveCode, Job job, WaspStatus status) throws WaspMessageBuildingException;
	//public void setJobApprovalComment(String jobApproveCode, Integer jobId, String comment) throws Exception;
	
	/**
	 * Adds a new quote for a job
	 * @param Integer jobId
	 * @param AcctQuote quoteForm
	 * @param List<AcctQuoteMeta> metaList (information for storage within AcctQuoteMeta)
	 * @throws Exception
	 */
	public void addNewQuote(Integer jobId, AcctQuote quoteForm, List<AcctQuoteMeta> metaList) throws Exception;
	
	public Job getJobAndSoftware(Job job);
	
	/**
	 * Get list of libraries (user-submitted and facility-generated) for a job
	 * @param Job job
	 * @return List<Sample>
	 */
	public List<Sample> getLibraries(Job job);
	 
	/**
	 * Create new acctQuote for a job and save it's associated file (to the remote location)
	 * @param Job job
	 * @param File localFile (this is wasp's newly created quote.pdf, currently locally stored)
	 * @param Float totalFinalCost
	 * @param boolean saveQuoteAsJSON
	 * @return void
	 */
	public void createNewQuoteAndSaveQuoteFile(MPSQuote mpsQuote, File file, Float totalFinalCost, boolean saveQuoteAsJSON) throws FileUploadException, JSONException, QuoteException;

	/**
	 * Create new acctQuote for a job and save the UPLOADED file (to the remote location)
	 * @param Job job
	 * @param MultipartFile mpFile (the uploaded file; uploaded from the web)
	 * @param String fileDescription (invoice or quote only)
	 * @param Float totalCost
	 * @return void
	 */
	public void createNewQuoteOrInvoiceAndUploadFile(Job job, MultipartFile mpFile, String fileDescription, Float totalCost) throws FileUploadException, QuoteException, WaspMessageBuildingException;
	
	
	/**
	 * Get Strategy for a job (in this case, it's a library strategy)
	 * @param Job
	 * @param String strategyType
	 * @return Strategy
	 * @throws Exception
	 */
	public Strategy getStrategy(String strategyType, Job job)throws Exception;
	
	/**
	 * Get List<JobMeta> for a job via it's jobId
	 * @param Integer jobId
	 * @param String strategyType
	 * @return List<JobMeta>
	 */
	public List<JobMeta> getJobMeta(Integer jobId);

	public JobMetaDao getJobMetaDao();

	public JobDataTabViewing getTabViewPluginByJob(Job job);

	public JobSampleDao getJobSampleDao();
	
	public void setJobDraftService(JobDraftService mockJobDraftServiceImpl);
	 
	public List<List<Sample>> getSampleReplicates(Job job);

}
