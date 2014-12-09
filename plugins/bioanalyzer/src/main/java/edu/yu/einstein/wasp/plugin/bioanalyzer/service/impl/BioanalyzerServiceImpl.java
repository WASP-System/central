/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.bioanalyzer.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.exception.InvalidParameterException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.templates.JobStatusMessageTemplate;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobDraftMeta;
import edu.yu.einstein.wasp.model.JobFile;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.Workflowresourcecategory;
import edu.yu.einstein.wasp.model.WorkflowresourcecategoryMeta;
import edu.yu.einstein.wasp.plugin.bioanalyzer.service.BioanalyzerService;
import edu.yu.einstein.wasp.plugin.bioanalyzer.software.Bioanalyzer;
import edu.yu.einstein.wasp.service.JobDraftService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.service.impl.WaspMessageHandlingServiceImpl;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;

import org.springframework.messaging.MessagingException;

@Service
@Transactional("entityManager")
public class BioanalyzerServiceImpl extends WaspMessageHandlingServiceImpl implements BioanalyzerService {
	
	@Autowired
	private Bioanalyzer bioanalyzer;
	@Autowired
	private FileType pdfFileType;//pdf file 
	
	@Autowired
	private JobService jobService;
	@Autowired
	private SampleService sampleService;
	@Autowired
	private JobDraftService jobDraftService;
	@Autowired
	private UserService userService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String performAction() {
		// do something
		return "done";
	}

	public void saveOrUpdateJobDraftMeta (JobDraft jobDraft, String metaK, String metaV){
		List<JobDraftMeta> jobDraftMetaList = jobDraft.getJobDraftMeta();
		boolean foundIt = false;
		for(JobDraftMeta jdm : jobDraftMetaList){
			if(jdm.getK().equals(metaK)){
				jdm.setV(metaV);
				jobDraftService.getJobDraftMetaDao().save(jdm);
				foundIt = true;
				break;
			}
		}
		if(!foundIt){
			JobDraftMeta jdm = new JobDraftMeta(); 
			jdm.setJobDraftId(jobDraft.getId());
			jdm.setK(metaK);
			jdm.setV(metaV); 
			jobDraftService.getJobDraftMetaDao().save(jdm);
		}
	}
	
	public String getMeta(JobDraft jobDraft, String metaK){
		String metaV = "";
		for(JobDraftMeta jdm : jobDraft.getJobDraftMeta()){
			if(metaK.equals(jdm.getK())){
				metaV = jdm.getV();
				break;
			}
		}
		return metaV;
	}
	public String getMeta(Job job, String metaK){
		String metaV = "";
		for(JobMeta jdm : job.getJobMeta()){
			if(metaK.equals(jdm.getK())){
				metaV = jdm.getV();
				break;
			}
		}
		return metaV;
	}
	public List<String> getAvailableBioanalyzerChips(Workflow wf){
		
		List<String> availableBioanalyzerChips = new ArrayList<String>();
		
		List<WorkflowresourcecategoryMeta> wfrcmList = null;
		List<Workflowresourcecategory> wfrcList = wf.getWorkflowresourcecategory();
		for(Workflowresourcecategory wfrc : wfrcList){
			if(wfrc.getResourceCategory().getResourceType().getIName().equals("bioanalyzer")){
				wfrcmList = wfrc.getWorkflowresourcecategoryMeta();
			}
		}
		if(wfrcmList!=null){
			for(WorkflowresourcecategoryMeta wfrcm : wfrcmList){
				if(wfrcm.getK().endsWith(".chip")){
					String codedBioanalyzerChips = wfrcm.getV();
					String codedBioanalyzerChipArray [] = codedBioanalyzerChips.split(";");
					for(String codedBioanalyzerChip : codedBioanalyzerChipArray){
						String bioanalyzerChipArray [] = codedBioanalyzerChip.split(":");
						String bioanalyzerChip = bioanalyzerChipArray[0];
						//logger.debug("bioanalyzer chip name: " + bioanalyzerChip);
						availableBioanalyzerChips.add(bioanalyzerChip);
					}
				}
			}
		}
		
		return availableBioanalyzerChips;
	}
	
	public boolean atLeastOneBioanalyzerFileUploadedByFacility(Job job){
		boolean atLeastOneBioanalyzerFileUploadedByFacility = false;		
		List<JobFile> jobFileList = job.getJobFile();
		for(JobFile jf : jobFileList){
			FileGroup fileGroup = jf.getFile();
			if(fileGroup.getFileType()!=null && fileGroup.getFileTypeId().intValue() == pdfFileType.getId().intValue()){
				if(fileGroup.getSoftwareGeneratedById().intValue()==bioanalyzer.getId().intValue()){
					atLeastOneBioanalyzerFileUploadedByFacility = true;
					break;
				}
			}
		}
		return atLeastOneBioanalyzerFileUploadedByFacility;
	}
	
	public void updateBioanalyzerJobStatus(Job job, WaspStatus status, String task, String comment, boolean checkForJobActive) throws WaspMessageBuildingException{
		// TODO: Write test!!
		Assert.assertParameterNotNull(job, "No Job provided");
		Assert.assertParameterNotNullNotZero(job.getId(), "Invalid Job Provided");
		Assert.assertParameterNotNull(status, "No Status provided");
		if (status != WaspStatus.COMPLETED && status != WaspStatus.ABANDONED)
			throw new InvalidParameterException("WaspStatus is null, or not COMPLETED or ABANDONED");
		Assert.assertParameterNotNull(task, "No Task provided");
		if (checkForJobActive && !jobService.isJobActive(job))
			throw new WaspMessageBuildingException("Not going to build message because job " + job.getId() + " is not active");
		JobStatusMessageTemplate messageTemplate = new JobStatusMessageTemplate(job.getId());
		messageTemplate.setUserCreatingMessageFromSession(userService);
		messageTemplate.setComment(comment);
		messageTemplate.setTask(task);
		messageTemplate.setStatus(status); // sample received (COMPLETED) or abandoned (ABANDONED)
		try{
			sendOutboundMessage(messageTemplate.build(), true);
		} catch (MessagingException e){
			throw new WaspMessageBuildingException(e.getLocalizedMessage());
		}
	}
	
	public boolean isJobsAwaitingBioanalyzerCompleteTask(List<Job>jobList){
		//if any job is awaiting this task, return true
		////logger.debug("dubin 12-8-14 in isJobsAwaitingBioanalyzerCompleteTask");
		for(Job job : jobList){
			if(isThisJobAwaitingBioanalyzerCompleteTask(job)){//at least one is ready, so return true
				return true;
			}
		}
		return false;		
	}
	
	public boolean isThisJobAwaitingBioanalyzerCompleteTask(Job job){
		 
		if(!job.getWorkflow().getIName().equalsIgnoreCase("bioanalyzer")){//not a bioanalyzer job, so forget this one
			return false;
		}
		List<Sample> libraryList = job.getSample();//bioanalyzer job will only have userSubmittedLibraries
		for(Sample library : libraryList){
			if(sampleService.getReceiveSampleStatus(library).isRunning()){//not complete
				return false;
			}
			if(sampleService.getLibraryQCStatus(library).isRunning()){//not complete
				return false;
			}
		}		
		if(!this.atLeastOneBioanalyzerFileUploadedByFacility(job)){
			return false;
		}		 
		return true;
	}
}
