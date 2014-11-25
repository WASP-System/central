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

import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobDraftMeta;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.Workflowresourcecategory;
import edu.yu.einstein.wasp.model.WorkflowresourcecategoryMeta;
import edu.yu.einstein.wasp.plugin.bioanalyzer.service.BioanalyzerService;
import edu.yu.einstein.wasp.service.JobDraftService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;

@Service
@Transactional("entityManager")
public class BioanalyzerServiceImpl extends WaspServiceImpl implements BioanalyzerService {
	
	
	@Autowired
	private JobDraftService jobDraftService;
	
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
}
