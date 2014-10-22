/**
 * 
 */
package edu.yu.einstein.wasp.plugin.bwa.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.plugin.bwa.service.BwaService;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatus;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatusKey;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexType;
import edu.yu.einstein.wasp.plugin.genomemetadata.plugin.GenomeMetadataPlugin;
import edu.yu.einstein.wasp.plugin.genomemetadata.service.GenomeMetadataService;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.WaspMessageHandlingService;

/**
 * @author calder
 *
 */
@Service
@Transactional("entityManager")
public class BwaServiceImpl implements BwaService {
	
	@Autowired
	private GenomeMetadataService genomeMetadataService;
	
	@Autowired
	private GenomeService genomeService;
	
	@Autowired
	@Qualifier("waspMessageHandlingServiceImpl")
	// more than one class of type WaspMessageHandlingService so must specify
	private WaspMessageHandlingService waspMessageHandlingService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public enum BwaIndexType { GENOME, CDNA };
	
	public final String BWA_INDEX_FLOW = "bwa.index";


	/** 
	 * {@inheritDoc}
	 * 
	 * The BWA implementation of this should detect the presence of a suitable FASTA index reference and 
	 * generate a BWA index in the default metadata location of the remote host.
	 * 
	 */
	@Override
	public synchronized GenomeIndexStatus getGenomeIndexStatus(GridWorkService workService, Build build) {
		return getGenomeIndexStatus(workService, build, BwaIndexType.GENOME);
	}
	
	public synchronized GenomeIndexStatus getGenomeIndexStatus(GridWorkService workService, Build build, BwaIndexType type) {
		
		String indexType = null;
		
		switch (type) {
			case GENOME:
				indexType = "genome";
				break;
			case CDNA:
				indexType= "cdna";
				break;
			default:
				break;
		}
		
		logger.trace("getGenomeIndexStatus for BWA " + build.getGenomeBuildNameString() + " called");

		GenomeIndexStatusKey remoteKey = genomeMetadataService.generateIndexKey(build, workService, new GenomeIndexType("bwa"), indexType);

		GenomeIndexStatus status = GenomeIndexStatus.UNKNOWN;
		
		// BWA requires an existing fasta index, genomic and cDNA
		try {
			GenomeIndexStatus fasta = genomeMetadataService.getFastaStatus(workService, build);
			
			if (fasta.equals(GenomeIndexStatus.BUILDING))
				return GenomeIndexStatus.BUILDING;
			if (!fasta.isAvailable()) { // if not BUILDING or BUILT ...
				String message = "FASTA genome index for build " + build.getGenomeBuildNameString() + " has unknown status " + fasta.toString() + " with message: "
						+ fasta.getMessage();
				logger.warn(message);
				return status;
			}
			
		} catch (IOException e) {
			String mess = "Unable to determine status of fasta index of " + build.getGenomeBuildNameString() + " threw exception " + e.getLocalizedMessage();
			logger.warn(mess);
			return status;
		}
		
		status = genomeMetadataService.getStatus(remoteKey);
		
		try {
			if (status.equals(GenomeIndexStatus.BUILDABLE)) {
				logger.debug("determined that index for " + build.getGenomeBuildNameString() + " version " + remoteKey.getVersion() + " does not exist on "
						+ remoteKey.getHostname() + " for type " + remoteKey.getType().toString() + " launching BWA build");
				launchBuildBwa(workService, build, type);
				genomeMetadataService.updateStatus(remoteKey, GenomeIndexStatus.BUILDING);
				return GenomeIndexStatus.BUILDING;
			} else if (status.equals(GenomeIndexStatus.BUILDING) || status.equals(GenomeIndexStatus.BUILT)) {
				return status;
			} else if (status.equals(GenomeIndexStatus.UNKNOWN)) {
				logger.warn("build " + build.getGenomeBuildNameString() + " has a BWA " + remoteKey.getVersion() + " index status of UNKNOWN");
				return status;
			} else if (status.equals(GenomeIndexStatus.UNBUILDABLE)) {
				logger.error("build " + build.getGenomeBuildNameString() + " has a BWA " + remoteKey.getVersion()
						+ " index status of UNBUILDABLE with message " + status.getMessage());
				return status;
			} else {
				logger.error("build " + build.getGenomeBuildNameString() + " has a BWA " + remoteKey.getVersion()
						+ " index status truly UNKNOWN: " + status.toString());
				return GenomeIndexStatus.UNKNOWN;
			}
		} catch (WaspMessageBuildingException e) {
			logger.warn("problem sending message to launch build BWA index " + e.getLocalizedMessage());
			return GenomeIndexStatus.UNKNOWN;
		}
		
	}
	
	private synchronized Message<String> launchBuildBwa(GridWorkService workService, Build build, BwaIndexType type) throws WaspMessageBuildingException {
		
		logger.info("Going to begin build of BWA index on " + workService.getTransportConnection().getHostName());

		Map<String, String> jobParameters = new HashMap<String, String>();

		String indexType = null;
		
		switch (type) {
			case GENOME:
				indexType = "genome";
				break;
			case CDNA:
				indexType= "cdna";
				break;
			default:
				break;
		}
		
		jobParameters.put(WaspJobParameters.HOSTNAME, workService.getTransportConnection().getHostName());
		jobParameters.put(GenomeMetadataPlugin.METADATA_PATH_KEY, genomeService.getRemoteBuildPath(workService.getTransportConnection().getHostName(), build));
		jobParameters.put(GenomeMetadataPlugin.BUILD_NAME_KEY, build.getGenomeBuildNameString());
		jobParameters.put(GenomeMetadataPlugin.VERSION_KEY, indexType);

		// this is set to allow multiple runs of the same flow.  not typical.
		jobParameters.put(WaspJobParameters.TEST_ID, String.valueOf(System.currentTimeMillis()));
		
		logger.info("Sending launch message with flow " + BWA_INDEX_FLOW + " and build: " + build.getGenomeBuildNameString()
				+ " for " + indexType);

		waspMessageHandlingService.launchBatchJob(BWA_INDEX_FLOW, jobParameters);
		return (Message<String>) MessageBuilder.withPayload("Initiating BWA index flow on build " + build.getGenomeBuildNameString()).build();

	}

}
