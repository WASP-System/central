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

import org.apache.commons.collections.map.HashedMap;
import org.springframework.batch.core.explore.wasp.ParameterValueRetrievalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobDraftMeta;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.JobDraftService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.util.MetaHelper;
import edu.yu.einstein.wasp.variantcalling.service.VariantcallingService;

@Service
@Transactional("entityManager")
public class VariantcallingServiceImpl extends WaspServiceImpl implements VariantcallingService {
	
	@Autowired
	protected JobDraftService jobDraftService;
	
	@Autowired
	protected GenomeService genomeService;
	
	@Autowired
	@Qualifier("variantcallingPluginArea")
	protected String variantcallingPluginArea;
		
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
	
	@Override
	public Map<String, String> getSavedWxsIntervalFilesByBuild(JobDraft jobDraft){
		Map<String, String> intervalFilesByBuild = new HashMap<>();
		try {
			String intervalFileString = MetaHelper.getMetaValue(variantcallingPluginArea, "wxsIntervalFiles", jobDraft.getJobDraftMeta());
			for (String s : intervalFileString.split(";")){
				String[] elements = s.split("=");
				intervalFilesByBuild.put(elements[0], elements[1]);
			}
		} catch (MetadataException e) {
			logger.debug(e.getMessage());
		}
		return intervalFilesByBuild;
	}
	
	@Override
	public String getSavedWxsIntervalFileForBuild(JobDraft jobDraft, Build build){
		String buildString = genomeService.getDelimitedParameterString(build);
		return getSavedWxsIntervalFilesByBuild(jobDraft).get(buildString);
	}
	
	@Override
	public void saveWxsIntervalFile(JobDraft jobDraft, Build build, String filePath){
		String buildString = genomeService.getDelimitedParameterString(build);
		Map<String, String> intervalFilesByBuild = getSavedWxsIntervalFilesByBuild(jobDraft);
		if (intervalFilesByBuild.containsKey(buildString) && intervalFilesByBuild.get(buildString).equals(filePath))
			return;
		intervalFilesByBuild.put(buildString, filePath);
		String storeString = "";
		for (String key : intervalFilesByBuild.keySet())
			storeString += key + "=" + intervalFilesByBuild.get(key) + ";";
		storeString = StringUtils.trimTrailingCharacter(storeString, ';');
		JobDraftMeta jdm = new JobDraftMeta();
		jdm.setJobDraft(jobDraft);
		jdm.setK(variantcallingPluginArea + "." + "wxsIntervalFiles");
		jdm.setV(storeString);
		jobDraftService.getJobDraftMetaDao().save(jdm);
	}


}
