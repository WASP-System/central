package edu.yu.einstein.wasp.plugin.illumina.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Document;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.interfacing.IndexingStrategy;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.service.WaspService;

public interface WaspIlluminaService extends WaspService {
	
	public static final String RUN_INFO = "runinfoxml";
	public static final String ILLUMINA_AREA = "illumina";

	/**
	 * Get list of illuminaRunFolders
	 * @return
	 * @throws GridException
	 */
	public Set<String> getIlluminaRunFolders() throws GridException;
	
	public void setIlluminaRunXml(GridResult result, Run run, String runXML) throws GridException, IOException, MetadataException;
	
	public Document getIlluminaRunXml(Run run) throws MetadataException;
	
	public int getNumberOfReadSegments(Run run) throws MetadataException;
	
	public int getNumberOfIndexedReads(Run run) throws MetadataException;
	
	public int getTotalNumberOfReads(Run run) throws MetadataException;
	
	public List<Integer> getLengthOfReadSegments(Run run) throws MetadataException;
	
	public List<Integer> getLengthOfIndexedReads(Run run) throws MetadataException;

	public void setIlluminaRunXml(GridResult result, Run run) throws GridException, IOException, MetadataException;
	
	/**
	 * While the user generally requests a type of sequencing, the facility is able to produce 
	 * data of a different type (eg. paired instead of unpaired).  This method inspects the 
	 * cell libraries job and local configuration to determine if it is acceptable to return
	 * paired-end data when in the case where single-end data may have been selected. 
	 * 
	 * @param cellLibrary
	 * @return
	 * @throws MetadataException
	 */
	public boolean assayAllowsPairedEndData(SampleSource cellLibrary) throws MetadataException;
	
	public IndexingStrategy getIndexingStrategy(SampleSource cellLibrary) throws MetadataException, SampleTypeException;

}
