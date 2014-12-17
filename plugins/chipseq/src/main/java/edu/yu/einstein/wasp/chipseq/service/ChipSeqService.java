package edu.yu.einstein.wasp.chipseq.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.exception.JobContextInitializationException;
import edu.yu.einstein.wasp.exception.SoftwareConfigurationException;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.service.WaspService;
import edu.yu.einstein.wasp.viewpanel.JobDataTabViewing;

@Service
public interface ChipSeqService extends WaspService {

	public JobDataTabViewing getPeakcallerPlugin(Job job) throws JobContextInitializationException, SoftwareConfigurationException;
	public boolean isIP(Sample sample);
	public String getPeakType(Sample sample);
}
