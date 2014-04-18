/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.variantcalling.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.explore.wasp.ParameterValueRetrievalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.MetadataRuntimeException;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobDraftMeta;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.JobDraftService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.variantcalling.service.VariantcallingService;

@Service
@Transactional("entityManager")
public class VariantcallingServiceImpl extends WaspServiceImpl implements VariantcallingService {
	
	@Autowired
	protected JobDraftService jobDraftService;
	
	@Autowired
	protected JobService jobService;
	
	@Autowired
	protected GenomeService genomeService;
	
	@Autowired
	@Qualifier("variantcallingPluginArea")
	protected String variantcallingPluginArea;
	
	private String getIntervalFileMetaKey(){
		return variantcallingPluginArea + "." + "wxsIntervalFiles";
	}
		
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String performAction() {
		// do something
		return "done";
	}
	
	@Override
	public List<String> getWxsIntervalFilenameFromConfiguration(Build build) {
		List<String> files = new ArrayList<>();
		String filenames = build.getMetadata("wxsIntervals.filenames");
		if (filenames == null || filenames.isEmpty())
			return files;
		for (String filename : filenames.split(","))
			files.add(filename);
		return files;
	}
	
	@Override
	public Set<Build> getBuildsForJobDraft(JobDraft jobDraft){
		Set<Build> builds = new HashSet<>();
		for (SampleDraft sd : jobDraft.getSampleDraft()){
			Build b;
			try {
				b = genomeService.getBuild(sd);
				if (b == null){
					logger.warn("No genome build recorded against SampleDraft id=" + sd.getId());
					continue;
				}
			} catch (ParameterValueRetrievalException e) {
				logger.warn("Unable to retrieve a genome build for SampleDraft id=" + sd.getId());
				continue;
			}
			if (!builds.contains(b))
				builds.add(b);
		}
		return builds;
	}
	
	private Map<String, String> getSavedWxsIntervalFilesByBuild(MetaBase intervalFileMeta){
		Map<String, String> intervalFilesByBuild = new HashMap<>();
		if (intervalFileMeta == null || intervalFileMeta.getId() == null){
			logger.debug("Cannot find an existing intervalFile JobDraft metadata entry");
			return intervalFilesByBuild;
		}
		logger.debug("Found intervalFileString=" + intervalFileMeta.getV());
		for (String s : intervalFileMeta.getV().split(";")){
			String[] elements = s.split("=");
			logger.debug("intervalFilesByBuild: " + elements[0] + "->" + elements[1]);
			intervalFilesByBuild.put(elements[0], elements[1]);
		}
		return intervalFilesByBuild;
	}
	
	@Override
	public Map<String, String> getSavedWxsIntervalFilesByBuild(JobDraft jobDraft){
		Map<String, String> intervalFilesByBuild = new HashMap<>();
		JobDraftMeta intervalFileMeta = jobDraftService.getJobDraftMetaDao().getJobDraftMetaByKJobDraftId(getIntervalFileMetaKey(), jobDraft.getId());
		return getSavedWxsIntervalFilesByBuild(intervalFileMeta);
	}
	
	@Override
	public Map<String, String> getSavedWxsIntervalFilesByBuild(Job job){
		Map<String, String> intervalFilesByBuild = new HashMap<>();
		JobMeta intervalFileMeta = jobService.getJobMetaDao().getJobMetaByKJobId(getIntervalFileMetaKey(), job.getId());
		return getSavedWxsIntervalFilesByBuild(intervalFileMeta);
	}
	
	@Override
	public String getSavedWxsIntervalFileForBuild(JobDraft jobDraft, Build build){
		String buildString = genomeService.getDelimitedParameterString(build);
		return getSavedWxsIntervalFilesByBuild(jobDraft).get(buildString);
	}
	
	@Override
	public String getSavedWxsIntervalFileForBuild(Job job, Build build){
		String buildString = genomeService.getDelimitedParameterString(build);
		return getSavedWxsIntervalFilesByBuild(job).get(buildString);
	}
	
	@Override
	public void saveWxsIntervalFile(JobDraft jobDraft, Build build, String filePath){
		String buildString = genomeService.getDelimitedParameterString(build);
		Map<String, String> intervalFilesByBuild = getSavedWxsIntervalFilesByBuild(jobDraft);
		if (intervalFilesByBuild.containsKey(buildString) && intervalFilesByBuild.get(buildString).equals(filePath)){
			logger.debug("Not going to save as intervalFilesByBuild contains: " + buildString + "->" + filePath);
			return;
		}
		logger.debug("adding to intervalFilesByBuild: " + buildString + "->" + filePath);
		intervalFilesByBuild.put(buildString, filePath);
		String storeString = "";
		for (String key : intervalFilesByBuild.keySet())
			storeString += key + "=" + intervalFilesByBuild.get(key) + ";";
		storeString = StringUtils.trimTrailingCharacter(storeString, ';');
		JobDraftMeta jdm = new JobDraftMeta();
		jdm.setJobDraft(jobDraft);
		jdm.setK(getIntervalFileMetaKey());
		jdm.setV(storeString);
		try {
			jobDraftService.getJobDraftMetaDao().setMeta(jdm);
		} catch (MetadataException e) {
			throw new MetadataRuntimeException(e);
		}
	}


}
