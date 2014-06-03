package edu.yu.einstein.wasp.plugin.illumina.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Document;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.model.Run;
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

}
