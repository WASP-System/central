/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.gatk.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.MetadataRuntimeException;
import edu.yu.einstein.wasp.gatk.fileformat.GatkBamFileTypeAttribute;
import edu.yu.einstein.wasp.gatk.service.GatkService;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.plugin.fileformat.plugin.BamFileTypeAttribute;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String performAction() {
		// do something
		return "done";
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
		String folder = build.getMetadata("wxsIntervals.folder");
		String filename = variantCallingService.getSavedWxsIntervalFileForBuild(job, build);
		if (folder == null || folder.isEmpty() || filename == null || filename.isEmpty())
			throw new MetadataRuntimeException("failed to locate WXS interval file");
		if (filename.equals(VariantcallingService.WXS_NONE_INTERVAL_FILENAME))
			return null;
		return genomeService.getRemoteBuildPath(build) + "/" + folder + "/" + filename;
	}
}
