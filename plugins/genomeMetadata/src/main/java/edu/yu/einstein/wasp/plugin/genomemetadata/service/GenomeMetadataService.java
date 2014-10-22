/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.genomemetadata.service;

import java.io.IOException;
import java.util.Set;

import org.springframework.messaging.Message;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatus;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatusKey;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexType;
import edu.yu.einstein.wasp.plugin.genomemetadata.plugin.GenomeMetadataPlugin.VCF_TYPE;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.WaspService;

/**
 * 
 */
public interface GenomeMetadataService extends WaspService {
	
	public final static String GTF_VERSION_KEY = "GTF_version";
	public final static String VCF_VERSION_KEY = "VCF_version";
	
	/**
	 * Check on the status of a FASTA record for the given genome build at a specific host.  If the FASTA file is not provisioned, based
	 * on configuration in the genomes.properties file, attempt to:
	 * 
	 * 1. Download the FASTA files and concatenate.
	 * 2. Download cDNA FASTA records.
	 * 3. Build FASTA indices.
	 * 4. Build sequence dictionaries.
	 * 5. Build relevant annotations.
	 * 
	 * This method should be synchronized by a singleton service to avoid race conditions.
	 * 
	 * @param workService
	 * @param build
	 * @return
	 * @throws IOException 
	 */
	public GenomeIndexStatus getFastaStatus(GridWorkService workService, Build build) throws IOException;
	
	public Message<String> launchBuildFasta(GridWorkService workService, Build build) throws WaspMessageBuildingException;
	
	public String getPrefixedGenomeFastaPath(GridWorkService workService, Build build);
	public String getPrefixedGenomeFastaIndexPath(GridWorkService workService, Build build);
	public String getPrefixedGenomeFastaDictionaryPath(GridWorkService workService, Build build);
	
	public String getPrefixedCDnaFastaPath(GridWorkService workService, Build build);
	public String getPrefixedCDnaFastaIndexPath(GridWorkService workService, Build build);
	public String getPrefixedCDnaFastaDictionaryPath(GridWorkService workService, Build build);
	
	/**
	 * Check on the status of a GTF record for the given genome build at a specific host. Requires a properly build FASTA index (will be built if
	 * has not already been). If the GTF file is not provisioned, based on configuration in the genomes.properties file, attempt to:
	 * 
	 * 1. Download the GTF files and sort in the order of the fasta index.
	 * 
	 * This method should be synchronized by a singleton service to avoid race conditions.
	 * 
	 * @param workService
	 * @param build
	 * @return
	 * @throws IOException 
	 */
	public GenomeIndexStatus getGtfStatus(GridWorkService workService, Build build, String versionString) throws IOException;
	
	/**
	 * Begin the job to download and index gtf files for given genome build on the given host
	 * 
	 * @param workService
	 * @param build
	 * @return
	 * @throws WaspMessageBuildingException
	 */
	public Message<String> launchBuildGtf(GridWorkService workService, Build build, String versionString) throws WaspMessageBuildingException;
	
	public GenomeIndexStatus getVcfStatus(GridWorkService workService, Build build, String versionString) throws IOException;
	
	public Message<String> launchBuildVcf(GridWorkService workService, Build build, String versionString) throws WaspMessageBuildingException;
	
	/**
	 * Get the list of version strings for known GTF files.
	 * @param build
	 * @return
	 */
	public Set<String> getKnownGtfVersions(Build build);
	
	public Set<String> getKnownVcfVersions(Build build);
	
	public String getDefaultGtf(Build build) throws MetadataException;
	
	public String getGtfName(Build build, String version);
	
	public String getDefaultVcf(Build build, VCF_TYPE type) throws MetadataException;
	
	public String getVcfName(Build build, String version);
	
	
	/**
	 * get the file name from a given version string.
	 * 
	 * e.g. GRCh37.b2_8d.ensembl_v75.gtf
	 * 
	 * @param build
	 * @param version
	 * @return
	 */
	public String getGtfFileName(Build build, String version);
	
	public String getVcfFileName(Build build, String version);
	
	/**
	 * return path to remote gtf file with the appropriate bash metadata path variable (suitable for use in WorkUnit).
	 * 
	 * @param workService
	 * @param build
	 * @param versionString
	 * @return
	 */
	public String getPrefixedGtfPath(GridWorkService workService, Build build, String versionString);
	
	/**
	 * return path of remote GTF file, gzipped with adjacent tabix index.
	 * 
	 * @param workService
	 * @param build
	 * @param versionString
	 * @return
	 */
	public String getPrefixedIndexedGtfPath(GridWorkService workService, Build build, String versionString);

	public String getPrefixedVcfPath(GridWorkService workService, Build build, String versionString);
	
	public String getPrefixedIndexedVcfPath(GridWorkService workService, Build build, String versionString);
	
	/**
	 * Generate a key to uniquely identify the status of a given build.  If the index is 1:1 
	 * with the build (e.g. FASTA) versionString should be null. 
	 * 
	 * @param build
	 * @param workService
	 * @param type
	 * @param version
	 * @return
	 */
	public GenomeIndexStatusKey generateIndexKey(Build build, GridWorkService workService, GenomeIndexType type, String versionString);
	
	public GenomeIndexStatus getStatus(GenomeIndexStatusKey key);
	
	public void updateStatus(GenomeIndexStatusKey key, GenomeIndexStatus status);

}
