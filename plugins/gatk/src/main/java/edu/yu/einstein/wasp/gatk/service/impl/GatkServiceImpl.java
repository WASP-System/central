/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.gatk.service.impl;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.explore.wasp.ParameterValueRetrievalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.gatk.fileformat.GatkBamFileTypeAttribute;
import edu.yu.einstein.wasp.gatk.service.GatkService;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.plugin.fileformat.plugin.BamFileTypeAttribute;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.SoftwareService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.software.SoftwareConfiguration;
import edu.yu.einstein.wasp.util.WaspJobContext;
import edu.yu.einstein.wasp.variantcalling.service.VariantcallingService;

/**
 * @author asmclellan
 * @author jcai
 */
@Service
@Transactional("entityManager")
public class GatkServiceImpl extends WaspServiceImpl implements GatkService {
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private GenomeService genomeService;
	
	@Autowired
	private VariantcallingService variantCallingService;
	
	@Autowired
	private SoftwareService softwareService;
	
	@Autowired
	protected RunService runService;
	
	@Value("${wasp.developermode:false}")
    protected boolean developerMode;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String performAction() {
		// do something
		return "done";
	}
	
	@Override
	public void doLaunchFlow(List<Integer> cellLibraryIds, Integer softwareId, String flowName) throws Exception {
		SampleSource cl1 = sampleService.getCellLibraryBySampleSourceId(cellLibraryIds.get(0));
		String clidl = WaspSoftwareJobParameters.getCellLibraryListAsParameterValue(cellLibraryIds);
		Software software = softwareService.getById(softwareId);
		ResourceType referenceBasedAlignerResourceType = software.getResourceType();
		Job job = sampleService.getJobOfLibraryOnCell(cl1);
		if (job == null)
			throw new WaspException("Unable to locate job for cell library");
		Integer jobId = job.getId();
		
		logger.debug("working with job " + job.getId());
		String genomeBuild = null;
		for (Integer clId : cellLibraryIds){
			try {
			    genomeBuild = getGenomeBuildString(clId);    
			    break;
			} catch (MetadataException e) {}
		}
		if (genomeBuild == null){
            String message = "For job id=" + jobId + ", no cellLibrary ids (" + clidl + ") are annotated with a genome build";
            logger.warn(message);
            throw new WaspException(message);
        }
	
		WaspJobContext waspJobContext = new WaspJobContext(job);		
		software.getResourceType();
		SoftwareConfiguration softwareConfig = waspJobContext.getConfiguredSoftware(referenceBasedAlignerResourceType);
		if (softwareConfig == null) {
			logger.info("No software configured for jobId=" + jobId + " with resourceType iname=" + referenceBasedAlignerResourceType.getIName() + 
					" going to prepare for software execution with default parameters");
			softwareConfig = softwareService.getDefaultSoftwareConfig(software);
		} else if (!softwareConfig.getSoftware().getIName().equals(software.getIName())){
			logger.info("Software configured for jobId=" + jobId + " with resourceType iname=" + referenceBasedAlignerResourceType.getIName() + " is not " +
					software.getIName() + " going to prepare for software execution with default parameters");
			softwareConfig = softwareService.getDefaultSoftwareConfig(software);
		}
		Map<String, String> jobParameters = softwareConfig.getParameters();
		
		jobParameters.put(WaspSoftwareJobParameters.CELL_LIBRARY_ID_LIST, clidl);
		jobParameters.put(WaspSoftwareJobParameters.GENOME, genomeBuild);
		if (developerMode)
			jobParameters.put("uniqCode", Long.toString(Calendar.getInstance().getTimeInMillis())); // overcomes limitation of job being run only once
		runService.launchBatchJob(flowName, jobParameters);
	}
	
	@Override
	public Build getBuildForFg(FileGroup fileGroup){
		// method moved to genomeService
		return genomeService.getBuildForFg(fileGroup);
	}
	
	@Override
	public Set<BamFileTypeAttribute> getCompleteGatkPreprocessBamFileAttributeSet(boolean isDedup){
		Set<BamFileTypeAttribute> attributes = new HashSet<>();
		attributes.add(BamFileTypeAttribute.SORTED);
		if (isDedup)
			attributes.add(BamFileTypeAttribute.DEDUP);
		attributes.add(GatkBamFileTypeAttribute.REALN_AROUND_INDELS);
		attributes.add(GatkBamFileTypeAttribute.RECAL_QC_SCORES);
		return attributes;
	}
	
	/**
	 * Returns the path to the WXS interval file selected by the user. If no file was selected, null is returned.
	 * @param job
	 * @param build
	 * @return 
	 */
	@Override
	public String getWxsIntervalFile(Job job, Build build) {
		// TODO: this functionality needs overhauling
		String filename = variantCallingService.getSavedWxsIntervalFileForBuild(job, build);
		if (filename.equals(VariantcallingService.WXS_NONE_INTERVAL_FILENAME))
			return null;
		return genomeService.getRemoteBuildPath(build) + filename;
	}
	
	private String getGenomeBuildString(Integer cellLibraryId) throws MetadataException {
	    String retval;
	    try {
		retval = genomeService.getDelimitedParameterString(cellLibraryId);
	    } catch (SampleTypeException | ParameterValueRetrievalException e) {
		logger.warn(e.getMessage());
		return null;
	    }
	    if (retval == null) {
	        String message = "genome/build was null, indicating that the genome is unknown or Other";
	        logger.debug(message);
	        throw new MetadataException(message);
	    }
	    return retval;
	}
}
