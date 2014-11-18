/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.bioanalyzer.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobDraftMeta;
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

	public void saveOrUpdateMeta (JobDraft jobDraft, String metaK, String metaV){
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
}
