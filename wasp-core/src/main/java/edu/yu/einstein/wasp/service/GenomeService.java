/**
 *
 * SampleService.java 
 * 
 * the SampleService
 *
 *
 **/

package edu.yu.einstein.wasp.service;


import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.explore.wasp.ParameterValueRetrievalException;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Organism;


@Service
public interface GenomeService extends WaspService {
	
	public static final String GENOME_AREA="genome";
	
	public static final String GENOME_STRING_META_KEY="genomeString";
	
	public static final String DELIMITER = "::";
	
	public static final String INDEX_CREATION_STARTED = "STARTED.txt";
	
	public static final String INDEX_CREATION_COMPLETED = "COMPLETED.txt";
	
	public static final String INDEX_CREATION_FAILED = "FAILED.txt";

	public Set<Organism> getOrganisms();
	
	/**
	 * Test to see if the folder for the specified genome index exists on the remote host.
	 * 
	 * @param workService
	 * @param build
	 * @param index
	 * @return
	 * @throws IOException 
	 */
	public boolean exists(GridWorkService workService, Build build, String index) throws IOException;
	
	/**
	 * Test to see if the folder for the specified genome index in the indicated subdirectory exists on the remote host.
	 * It's up to the aligner's implementation to implement a {@link GenomeIndexConfiguration} in order to specify
	 * the subdirectory.
	 * 
	 * @param workService
	 * @param build
	 * @param index
	 * @return
	 * @throws IOException 
	 */
	public boolean exists(GridWorkService workService, Build build, String index, String subdirectory) throws IOException;
	
	/**
	 * Test to see that the index creation of the specified index has begun.
	 * 
	 * @param workService
	 * @param build
	 * @param index
	 * @return
	 * @throws IOException 
	 */
	public boolean isStarted(GridWorkService workService, Build build, String index) throws IOException;
	
	/**
	 * Test to see if the index has been created.
	 * 
	 * @param workService
	 * @param build
	 * @param index
	 * @return
	 * @throws IOException 
	 */
	public boolean isCompleted(GridWorkService workService, Build build, String index) throws IOException;
	
	/**
	 * Check for annotated failure condition.
	 * 
	 * @param workService
	 * @param build
	 * @param index
	 * @return
	 * @throws IOException
	 */
	public boolean isFailed(GridWorkService workService, Build build, String index) throws IOException;
	
	/**
	 * Test to see that the index creation of the specified index has begun in the specified
	 * subdirectory.
	 * 
	 * @param workService
	 * @param build
	 * @param index
	 * @param subdir
	 * @return
	 * @throws IOException 
	 */
	public boolean isStarted(GridWorkService workService, Build build, String index, String subdir) throws IOException;
	
	/**
	 * Test to see if the index has been created in the specified subdirectory.
	 * 
	 * @param workService
	 * @param build
	 * @param index
	 * @param subdir
	 * @return
	 * @throws IOException 
	 */
	public boolean isCompleted(GridWorkService workService, Build build, String index, String subdir) throws IOException;
	
	/**
	 * Check for annotated failure condition.
	 * 
	 * @param workService
	 * @param build
	 * @param index
	 * @param subdir
	 * @return
	 * @throws IOException
	 */
	public boolean isFailed(GridWorkService workService, Build build, String index, String subdir) throws IOException;
	
	/**
	 * Create the specified index location.
	 * 
	 * @param workService
	 * @param build
	 * @param index
	 * @throws IOException 
	 */
	public void createIndexLocation(GridWorkService workService, Build build, String index) throws IOException;
	
	/**
	 * Create the specified index location and subdirectory.
	 * 
	 * @param workService
	 * @param build
	 * @param index
	 * @param subdir
	 * @throws IOException 
	 */
	public void createIndexLocation(GridWorkService workService, Build build, String index, String subdir) throws IOException;
	
	/**
	 * Mark the specified index as begun.
	 * 
	 * @param workService
	 * @param build
	 * @param index
	 * @throws IOException 
	 */
	public void markIndexBegun(GridWorkService workService, Build build, String index) throws IOException;
	
	/**
	 * Mark the specified index and subdirectory as begun.
	 * 
	 * @param workService
	 * @param build
	 * @param index
	 * @param subdir
	 * @throws IOException 
	 */
	public void markIndexBegun(GridWorkService workService, Build build, String index, String subdir) throws IOException;
	
	/**
	 * Mark the specified index as complete.
	 * 
	 * @param workService
	 * @param build
	 * @param index
	 * @throws IOException 
	 */
	public void markIndexComplete(GridWorkService workService, Build build, String index) throws IOException;
	
	/**
	 * Mark the specified index and subdirectory as complete.
	 * 
	 * @param workService
	 * @param build
	 * @param index
	 * @param subdir
	 * @throws IOException 
	 */
	public void markIndexComplete(GridWorkService workService, Build build, String index, String subdir) throws IOException;
	
	

	/**
	 * Returns registered organism who's organism ID matches that given, or null if no match
	 * @param name
	 * @return
	 */
	public Organism getOrganismById(Integer organismId);
	
	public Map<String, Build> getBuilds(Integer organism, String genome) throws ParameterValueRetrievalException;
	
	public Build getBuild(Integer organism, String genome, String build) throws ParameterValueRetrievalException;
	
	/**
	 * Gets the build based on a delimited string generated by this service ({@link getDelimitedParameterString})
	 * @param delimitedParameterString
	 * @return
	 * @throws ParameterValueRetrievalException
	 */
	public Build getBuild(String delimitedParameterString) throws ParameterValueRetrievalException;
	
	/**
	 * Creates a string containing genome build information which can be later used to re-create a Build object with {@link getBuild}
	 * @param build
	 * @return
	 */
	public String getDelimitedParameterString(Build build);
	
	/**
	 * Creates a string containing genome build information which can be later used to re-create a Build object with {@link getBuild}
	 * @param build
	 * @return
	 */
	public String getDelimitedParameterString(Integer cellLibraryId) throws ParameterValueRetrievalException, SampleTypeException;
	
	public void setBuild(Sample sample, Build build) throws MetadataException;
	
	/**
	 * applies build to all samples in the supplied set
	 * @param sampleDraftSet
	 * @param build
	 */
	public void setBuildToAllSamples(Set<Sample> sampleSet, Build build);
	
	public void setBuild(SampleDraft sampleDraft, Build build) throws MetadataException;
	
	/**
	 * applies build to all sampleDrafts in the supplied set
	 * @param sampleDraftSet
	 * @param build
	 */
	public void setBuildToAllSampleDrafts(Set<SampleDraft> sampleDraftSet, Build build);
	
	/**
	 * gets the genome build set for the sampleDraft or null if not set
	 * @param sampleDraft
	 * @return
	 * @throws ParameterValueRetrievalException
	 */
	public Build getBuild(SampleDraft sampleDraft) throws ParameterValueRetrievalException;
	
	/**
	 * gets the genome build set for the sample or null if not set
	 * @param sample
	 * @return
	 * @throws ParameterValueRetrievalException
	 */
	public Build getBuild(Sample sample) throws ParameterValueRetrievalException;

	public Map<Integer, Organism> getOrganismMap();

	/**
	 * Get remote host formatted representation of metadata directory housing this build
	 * Encodes path using variable for remote metadata path, requires execution in WorkUnit.
	 * 
	 * @param build
	 * @return
	 */
	public String getRemoteBuildPath(Build build);
	
	
	/**
	 * Get remote directory.  Use getRemoteBuildPath(Build build).
	 * 
	 * @param hostname
	 * @param build
	 * @return
	 */
	public String getRemoteBuildPath(String hostname, Build build);
	
	
	/**
	 * returns the list of organisms plus a generic 'Other' organism
	 * @return
	 */
	public Set<Organism> getOrganismsPlusOther();
	
	/**
	 * Returns the genome build associated with a cell library
	 * @param cellLibrary
	 * @return
	 */
	public Build getGenomeBuild(SampleSource cellLibrary);

	/**
	 * Get a genome {@link Build} object from a {@link FileGroup}.  If the FileGroup does not have a {@link SampleSource} 
	 * (ie. Cell-Library) this method will return null.  If the SampleSource does not have a genome Build, it will
	 * throw a {@link NullResourceException} (Runtime exception). 
	 * 
	 * @param fileGroup
	 * @return
	 */
	public Build getBuildForFg(FileGroup fileGroup);
	
	/**
	 * Get remote host formatted representation of metadata directory housing the fasta file of this build.
	 * Encodes path using variable for remote metadata path, requires execution in WorkUnit.<br /><br />
	 * Deprecated: use GenomeMetaDataService method getRemoteGenomeFastaIndexPath(GridWorkService workService, Build build) instead
	 * @param build
	 * @return
	 */
	@Deprecated
	public String getReferenceGenomeFastaFile(Build build);

	  
}
