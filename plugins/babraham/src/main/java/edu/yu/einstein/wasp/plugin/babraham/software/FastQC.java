/**
 * 
 */
package edu.yu.einstein.wasp.plugin.babraham.software;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.filetype.service.FastqService;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.plugin.babraham.exception.FastQCDataParseException;
import edu.yu.einstein.wasp.plugin.babraham.service.BabrahamService;
import edu.yu.einstein.wasp.software.SoftwarePackage;



/**
 * @author calder
 *
 */
public class FastQC extends SoftwarePackage {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7075104587205964069L;
	
	static class PlotType{
		// meta keys for charts produced
		public static final String BASIC_STATISTICS = "basic_statistics";
		public static final String DUPLICATION_LEVELS = "duplication_levels";
		public static final String KMER_PROFILES = "kmer_profiles";
		public static final String PER_BASE_GC_CONTENT = "per_base_gc_content";
		public static final String PER_BASE_N_CONTENT = "per_base_n_content";
		public static final String PER_BASE_QUALITY = "per_base_quality";
		public static final String PER_BASE_SEQUENCE_CONTENT = "per_base_sequence_content";
		public static final String PER_SEQUENCE_GC_CONTENT = "per_sequence_gc_content";
		public static final String PER_SEQUENCE_QUALITY = "per_sequence_quality";
		public static final String SEQUENCE_LENGTH_DISTRIBUTION = "sequence_length_distribution"; 
	}
	
	@Autowired
	BabrahamService babrahamService;
	
	/**
	 * 
	 */
	public FastQC() {
		// TODO Auto-generated constructor stub
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String getSoftwareVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setSoftwareVersion(String softwareVersion) {
		// TODO Auto-generated method stub

	}
	
	public WorkUnit getFastQC(FileGroup fileGroup) {
		
		return null;
	}
	
	public Map<String, String> parseOutput(GridResult result) throws GridException, FastQCDataParseException {
		Map<String, FastQCDataModule> dataModuleMap = babrahamService.parseFastQCOutput(result);
		
		return null;
	}

}
