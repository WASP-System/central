package edu.yu.einstein.wasp.chipseq.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.chipseq.service.ChipSeqService;
import edu.yu.einstein.wasp.exception.JobContextInitializationException;
import edu.yu.einstein.wasp.exception.SoftwareConfigurationException;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.util.SoftwareConfiguration;
import edu.yu.einstein.wasp.util.WaspJobContext;
import edu.yu.einstein.wasp.viewpanel.JobDataTabViewing;

@Service
@Transactional("entityManager")
public class ChipSeqServiceImpl extends WaspServiceImpl implements ChipSeqService {
	
	@Autowired
	private JobService jobService;
	@Autowired
	private SampleService sampleService;
	@Autowired
	private FileService fileService;
	
	@Autowired
	private FileType bamFileType;
	
	@Autowired
	ResourceType peakcallerResourceType;
	
	@Autowired
	private WaspPluginRegistry waspPluginRegistry;
	

	/**
	 * {@inheritDoc}
	 */
	@Transactional("entityManager")
	@Override
	public JobDataTabViewing getPeakcallerPlugin(Job job) throws JobContextInitializationException, SoftwareConfigurationException{
			WaspJobContext waspJobContext = new WaspJobContext(job.getId(), jobService);
			SoftwareConfiguration softwareConfig = waspJobContext.getConfiguredSoftware(peakcallerResourceType);
			if (softwareConfig == null){
				throw new SoftwareConfigurationException("No software could be configured for jobId=" + job.getId() + " with resourceType iname=" + peakcallerResourceType.getIName());
			}
			return waspPluginRegistry.getPlugin(softwareConfig.getSoftware().getIName(), JobDataTabViewing.class);				
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional("entityManager")
	@Override
	public boolean isIP(Sample sample){
		boolean retValue = false;
		List<SampleMeta> sampleMetaList = sampleService.getSampleMetaDao().getSamplesMetaBySampleId(sample.getId());
		for(SampleMeta sampleMeta : sampleMetaList){
			if(sampleMeta.getK().contains("inputOrIP")){
				if(sampleMeta.getV().equalsIgnoreCase("ip")){
					return true;
				}
			}
		}
		return retValue;
	}
	

}
