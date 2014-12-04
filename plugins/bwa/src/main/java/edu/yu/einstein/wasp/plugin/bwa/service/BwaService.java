/**
 * 
 */
package edu.yu.einstein.wasp.plugin.bwa.service;

import edu.yu.einstein.wasp.plugin.mps.service.DefaultReferenceGenomeService;

/**
 * @author calder
 *
 */
public interface BwaService extends DefaultReferenceGenomeService {

	public void doLaunchAlign(Integer cellLibraryId, Integer softwareId, String alignFlowName) throws Exception;

}
