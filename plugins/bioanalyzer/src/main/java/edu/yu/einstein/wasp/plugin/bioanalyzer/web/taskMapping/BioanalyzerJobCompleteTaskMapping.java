package edu.yu.einstein.wasp.plugin.bioanalyzer.web.taskMapping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.plugin.bioanalyzer.service.BioanalyzerService;
import edu.yu.einstein.wasp.taskMapping.WaspTaskMapping;

/**
 * 
 * @author asmclellan
 *
 */
public class BioanalyzerJobCompleteTaskMapping extends WaspTaskMapping {
	@Autowired
	private BioanalyzerService bioanalyzerService;

	
	//public void setBioanalzyerService(BioanalyzerService bioanalyzerService) {
	//	this.bioanalyzerService = bioanalyzerService;
	//}
	
	public BioanalyzerJobCompleteTaskMapping(String localizedLabelKey, String targetLink, String permission) {
		super(localizedLabelKey, targetLink, permission);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRequirementToShowLink(Object o) throws WaspException {
		@SuppressWarnings("unchecked")
		List<Job> jobList = (List<Job>) o;
		return bioanalyzerService.isJobsAwaitingBioanalyzerCompleteTask(jobList);
	}

}