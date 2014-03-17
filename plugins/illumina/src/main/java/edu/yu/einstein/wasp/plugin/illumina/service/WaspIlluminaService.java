package edu.yu.einstein.wasp.plugin.illumina.service;

import java.util.Set;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.service.WaspService;

public interface WaspIlluminaService extends WaspService {

	/**
	 * Get list of illuminaRunFolders
	 * @return
	 * @throws GridException
	 */
	public Set<String> getIlluminaRunFolders() throws GridException;

}
