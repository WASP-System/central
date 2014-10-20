/**
 * Created by Wasp System Eclipse Plugin
 * @calder 
 */
package edu.yu.einstein.wasp.plugins.star.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.batch.core.explore.wasp.ParameterValueRetrievalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexConfiguration;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatus;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatusKey;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexType;
import edu.yu.einstein.wasp.plugin.genomemetadata.service.GenomeMetadataService;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.plugins.star.StarGenomeIndexConfiguration;
import edu.yu.einstein.wasp.plugins.star.service.StarService;
import edu.yu.einstein.wasp.plugins.star.software.Star;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.WaspMessageHandlingService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;

/**
 * {@inheritDoc}
 * 
 */
@Service
@Transactional("entityManager")
public class StarServiceImpl extends WaspServiceImpl implements StarService {

	@Autowired
	private GenomeService genomeService;

	@Autowired
	private GenomeMetadataService genomeMetadataService;
	
	@Autowired
	@Qualifier("waspMessageHandlingServiceImpl")
	// more than one class of type WaspMessageHandlingService so must specify
	private WaspMessageHandlingService waspMessageHandlingService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String performAction() {
		// do something
		return "done";
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Star requires configuration with a overlap length (read length - 1) and
	 * either a GTF input file or a juctions file.
	 */
	@Override
	public GenomeIndexStatus getGenomeIndexStatus(GridWorkService workService, Build build, GenomeIndexConfiguration<String, String> config) {

		StarGenomeIndexConfiguration conf = (StarGenomeIndexConfiguration) config;

		logger.trace("getGenomeIndexStatus for STAR " + build.getGenomeBuildNameString() + " sjdblen: " + conf.getSjdbOverhang() + " called");

		GenomeIndexStatusKey remoteKey = genomeMetadataService.generateIndexKey(build, workService, new GenomeIndexType("star"), conf.generateUniqueKey());

		GenomeIndexStatus status = GenomeIndexStatus.UNKNOWN;

		// STAR requires an existing genomic fasta index, and GTF
		try {
			GenomeIndexStatus fasta = genomeMetadataService.getFastaStatus(workService, build);

			if (fasta.equals(GenomeIndexStatus.BUILDING))
				return GenomeIndexStatus.BUILDING;
			if (!fasta.isAvailable()) { // if not BUILDING or BUILT ...
				String message = "FASTA genome index for build " + build.getGenomeBuildNameString() + " has unknown status " + fasta.toString()
						+ " with message: " + fasta.getMessage();
				logger.warn(message);
				return status;
			}
			
			GenomeIndexStatus gtf = genomeMetadataService.getGtfStatus(workService, build, conf.getGtfVersion());

			if (gtf.equals(GenomeIndexStatus.BUILDING))
				return GenomeIndexStatus.BUILDING;
			if (!gtf.isAvailable()) { // if not BUILDING or BUILT ...
				String message = "GTF index for build " + build.getGenomeBuildNameString() + " has unknown status " + fasta.toString()
						+ " with message: " + fasta.getMessage();
				logger.warn(message);
				return status;
			}

		} catch (IOException e) {
			String mess = "Unable to determine status of GTF index of " + build.getGenomeBuildNameString() + " threw exception " + e.getLocalizedMessage();
			logger.warn(mess);
			return status;
		}

		status = genomeMetadataService.getStatus(remoteKey);

		try {
			if (status.equals(GenomeIndexStatus.BUILDABLE)) {
				logger.debug("determined that STAR index for " + build.getGenomeBuildNameString() + " version " + remoteKey.getVersion() + " does not exist on "
						+ remoteKey.getHostname() + " for type " + remoteKey.getType().toString() + " launching STAR build");
				launchBuildStar(workService, build, conf);
				genomeMetadataService.updateStatus(remoteKey, GenomeIndexStatus.BUILDING);
				return GenomeIndexStatus.BUILDING;
			} else if (status.equals(GenomeIndexStatus.BUILDING) || status.equals(GenomeIndexStatus.BUILT)) {
				return status;
			} else if (status.equals(GenomeIndexStatus.UNKNOWN)) {
				logger.warn("build " + build.getGenomeBuildNameString() + " has a STAR " + remoteKey.getVersion() + " index status of UNKNOWN");
				return status;
			} else if (status.equals(GenomeIndexStatus.UNBUILDABLE)) {
				logger.error("build " + build.getGenomeBuildNameString() + " has a STAR " + remoteKey.getVersion()
						+ " index status of UNBUILDABLE with message " + status.getMessage());
				return status;
			} else {
				logger.error("build " + build.getGenomeBuildNameString() + " has a STAR " + remoteKey.getVersion() + " index status truly UNKNOWN: "
						+ status.toString());
				return GenomeIndexStatus.UNKNOWN;
			}
		} catch (WaspMessageBuildingException | IOException | ParameterValueRetrievalException e) {
			logger.warn("problem sending message to launch build BWA index " + e.getLocalizedMessage());
			return GenomeIndexStatus.UNKNOWN;
		}
	}

	private synchronized Message<String> launchBuildStar(GridWorkService workService, Build build, StarGenomeIndexConfiguration config) 
			throws WaspMessageBuildingException, IOException, ParameterValueRetrievalException {
		
		if (!config.isSecond()) {
			if (config.getDirectory() == null) {
				String dir = getPrefixedStarIndexPath(workService, config);
				logger.debug("setting star work directory to: " + dir);
				config.setDirectory(dir);
			}
			if (config.getPathToJunctions() == null) {
				String juncs = genomeMetadataService.getPrefixedGtfPath(workService, build, config.getGtfVersion());
				logger.debug("setting star junctions to: " + juncs);
				config.setPathToJunctions(juncs);
			}
		} else {
			if (config.getPathToJunctions() == null) {
				String mess = "path to junctions required for 2nd pass genome build";
				logger.error(mess);
				throw new WaspRuntimeException(mess);
			}
		}
		
		
		logger.info("Going to begin build of BWA index on " + workService.getTransportConnection().getHostName());

		Map<String, String> jobParameters = new HashMap<String, String>();
		
		ObjectMapper mapper = new ObjectMapper();

		String configS = mapper.writeValueAsString(config);
		
		logger.trace(configS);
		
		jobParameters.put(WaspJobParameters.HOSTNAME, workService.getTransportConnection().getHostName());
		jobParameters.put(StarGenomeIndexConfiguration.STAR_GENOME_INDEX_CONFIGURATION_KEY, configS);
		
		// this is set to allow multiple runs of the same flow.  not typical.
		jobParameters.put(WaspJobParameters.TEST_ID, String.valueOf(System.currentTimeMillis()));
		
		logger.info("Sending launch message with flow " + Star.STAR_INDEX_FLOW + " and build: " + build.getGenomeBuildNameString()
				+ " for " + config.generateUniqueKey());

		waspMessageHandlingService.launchBatchJob(Star.STAR_INDEX_FLOW, jobParameters);
		return (Message<String>) MessageBuilder.withPayload("Initiating BWA index flow on build " + build.getGenomeBuildNameString()).build();

	}

	@Override
	public String getPrefixedStarIndexPath(GridWorkService workService, StarGenomeIndexConfiguration config) throws ParameterValueRetrievalException {
		if (config.isSecond()) {
			String mess = "this method returns the location of the primary genome index, not a two-pass secondary index";
			logger.error(mess);
			throw new WaspRuntimeException(mess);
		}
		Build build = genomeService.getBuild(config.getOrganism(), config.getGenome(), config.getBuild());
		String indexPath = genomeService.getRemoteBuildPath(workService.getTransportConnection().getHostName(), build) + "/star/" + config.generateUniqueKey() + "/";
		logger.debug("STAR index path: " + indexPath);
		return workService.getTransportConnection().prefixRemoteFile(indexPath);
	}

}
