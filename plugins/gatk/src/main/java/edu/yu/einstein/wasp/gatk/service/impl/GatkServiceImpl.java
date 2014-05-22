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
import edu.yu.einstein.wasp.model.SampleSource;
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
		// follow back along a single line to an original file group which should be a fastq file
		// figure out the genome from its sample source
		// We assume all source files share a common genome
		// TODO: verify that all builds are the same for all cell libraries 
		while (fileGroup.getDerivedFrom() != null && fileGroup.getDerivedFrom().iterator().hasNext())
			fileGroup = fileGroup.getDerivedFrom().iterator().next();
		Set<SampleSource> fgCl = fileGroup.getSampleSources();
		if (fgCl == null || fgCl.isEmpty())
			return null;
		return genomeService.getGenomeBuild(fgCl.iterator().next());
	}
	
	@Override
	public Set<BamFileTypeAttribute> getCompleteGatkPreprocessBamFileAttributeSet(){
		Set<BamFileTypeAttribute> attributes = new HashSet<>();
		attributes.add(BamFileTypeAttribute.SORTED);
		attributes.add(BamFileTypeAttribute.DEDUP);
		attributes.add(GatkBamFileTypeAttribute.REALN_AROUND_INDELS);
		attributes.add(GatkBamFileTypeAttribute.RECAL_QC_SCORES);
		return attributes;
	}
	
	@Override
	public String getReferenceSnpsVcfFile(Build build) {
		String folder = build.getMetadata("vcf.folder");
		String filename = build.getMetadata("vcf.snps.filename");
		if (folder == null || folder.isEmpty() || filename == null || filename.isEmpty())
			throw new MetadataRuntimeException("failed to locate snps vcf file");
		return genomeService.getRemoteBuildPath(build) + "/" + folder + "/" + filename;
	}
	
	@Override
	public String getReferenceIndelsVcfFile(Build build) {
		String folder = build.getMetadata("vcf.folder");
		String filename = build.getMetadata("vcf.indels.filename");
		if (folder == null || folder.isEmpty() || filename == null || filename.isEmpty())
			throw new MetadataRuntimeException("failed to locate indels vcf file");
		return genomeService.getRemoteBuildPath(build) + "/" + folder + "/" + filename;
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
