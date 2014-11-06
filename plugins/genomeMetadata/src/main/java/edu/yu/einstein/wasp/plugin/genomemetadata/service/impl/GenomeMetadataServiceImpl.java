/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.genomemetadata.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatus;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatusKey;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexType;
import edu.yu.einstein.wasp.plugin.genomemetadata.plugin.GenomeMetadataPlugin;
import edu.yu.einstein.wasp.plugin.genomemetadata.plugin.GenomeMetadataPlugin.VCF_TYPE;
import edu.yu.einstein.wasp.plugin.genomemetadata.service.GenomeMetadataService;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.WaspMessageHandlingService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;

/**
 * @author calder
 * 
 */
@Service
@Transactional("entityManager")
public class GenomeMetadataServiceImpl extends WaspServiceImpl implements GenomeMetadataService {

	@Autowired
	private GenomeService genomeService;

	@Autowired
	private GridHostResolver hostResolver;

	/**
	 * This map holds the build status and build time. Key: is a triple of
	 * Build, Hostname, and an IndexType and version Pair. the map holds a
	 * timestamp and status object for each key.
	 * 
	 * Version strings should be descriptive (and filename friendly): e.g. for
	 * ENSEMBL v75 GTF, the version string might be "ensembl_v75". if there is
	 * no version (for example the genome build should be 1:1 with fasta), enter
	 * null.
	 */
	private volatile Map<GenomeIndexStatusKey, Pair<Date, GenomeIndexStatus>> buildHistory = new HashMap<GenomeIndexStatusKey, Pair<Date, GenomeIndexStatus>>();

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier("waspMessageHandlingServiceImpl")
	// more than one class of type WaspMessageHandlingService so must specify
	private WaspMessageHandlingService waspMessageHandlingService;

	/**
	 * Length of time to wait between actually going out to the server again.
	 */
	private static final int TEST_DELAY_IN_MILLIS = 20000;

	/**
	 * {@inheritDoc}
	 * 
	 * @throws IOException
	 */
	@Override
	public synchronized GenomeIndexStatus getFastaStatus(GridWorkService workService, Build build) throws IOException {
		
		logger.trace("getFastaStatus called");

		GenomeIndexStatusKey remoteKey = generateIndexKey(build, workService, GenomeIndexType.FASTA, null);

		GenomeIndexStatus status = getStatus(remoteKey);
		try {
			if (status.equals(GenomeIndexStatus.BUILDABLE)) {
				logger.debug("determined that index for " + build.getGenomeBuildNameString() + " does not exist on " + remoteKey.getHostname() + " for type "
						+ remoteKey.getType().toString() + " launching FASTA build");
				launchBuildFasta(workService, build);
				updateStatus(remoteKey, GenomeIndexStatus.BUILDING);
				return GenomeIndexStatus.BUILDING;
			} else if (status.equals(GenomeIndexStatus.BUILDING) || status.equals(GenomeIndexStatus.BUILT)) {
				return status;
			} else if (status.equals(GenomeIndexStatus.UNKNOWN)) {
				logger.warn("build " + build.getGenomeBuildNameString() + " has an FASTA index status of UNKNOWN");
				return status;
			} else if (status.equals(GenomeIndexStatus.UNBUILDABLE)) {
				logger.error("build " + build.getGenomeBuildNameString() + " has an FASTA index status of UNBUILDABLE with message " + status.getMessage());
				return status;
			} else {
				logger.error("build " + build.getGenomeBuildNameString() + " has an FASTA index status truly UNKNOWN: " + status.toString());
				return GenomeIndexStatus.UNKNOWN;
			}
		} catch (WaspMessageBuildingException e) {
			logger.warn("problem sending message to launch build fasta " + e.getLocalizedMessage());
			return GenomeIndexStatus.UNKNOWN;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws IOException
	 */
	@Override
	public synchronized GenomeIndexStatus getGtfStatus(GridWorkService workService, Build build, String versionString) throws IOException {

		logger.trace("getGtfStatus called");

		GenomeIndexStatusKey remoteKey = generateIndexKey(build, workService, GenomeIndexType.GTF, versionString);

		// GTF requires an existing fasta index.
		GenomeIndexStatus fasta = getFastaStatus(workService, build);

		if (fasta.equals(GenomeIndexStatus.BUILDING))
			return GenomeIndexStatus.BUILDING;
		if (!fasta.isAvailable()) {
			String message = "FASTA genome index for build " + build.getGenomeBuildNameString() + " has unknown status " + fasta.toString() + " with message: "
					+ fasta.getMessage();
			logger.warn(message);
			return GenomeIndexStatus.UNKNOWN;
		}

		Set<String> knownVersions = getKnownGtfVersions(build);
		if (!knownVersions.contains(versionString))
			return GenomeIndexStatus.UNBUILDABLE;

		GenomeIndexStatus status = getStatus(remoteKey);
		try {
			if (status.equals(GenomeIndexStatus.BUILDABLE)) {
				logger.debug("determined that index for " + build.getGenomeBuildNameString() + " version " + remoteKey.getVersion() + " does not exist on "
						+ remoteKey.getHostname() + " for type " + remoteKey.getType().toString() + " launching GTF build");
				launchBuildGtf(workService, build, remoteKey.getVersion());
				updateStatus(remoteKey, GenomeIndexStatus.BUILDING);
				return GenomeIndexStatus.BUILDING;
			} else if (status.equals(GenomeIndexStatus.BUILDING) || status.equals(GenomeIndexStatus.BUILT)) {
				return status;
			} else if (status.equals(GenomeIndexStatus.UNKNOWN)) {
				logger.warn("build " + build.getGenomeBuildNameString() + " has an GTF " + " version " + remoteKey.getVersion() + " index status of UNKNOWN");
				return status;
			} else if (status.equals(GenomeIndexStatus.UNBUILDABLE)) {
				logger.error("build " + build.getGenomeBuildNameString() + " has an GTF " + " version " + remoteKey.getVersion()
						+ " index status of UNBUILDABLE with message " + status.getMessage());
				return status;
			} else {
				logger.error("build " + build.getGenomeBuildNameString() + " has an GTF " + " version " + remoteKey.getVersion()
						+ " index status truly UNKNOWN: " + status.toString());
				return GenomeIndexStatus.UNKNOWN;
			}
		} catch (WaspMessageBuildingException e) {
			logger.warn("problem sending message to launch build fasta " + e.getLocalizedMessage());
			return GenomeIndexStatus.UNKNOWN;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Message<String> launchBuildFasta(GridWorkService workService, Build build) throws WaspMessageBuildingException {

		logger.info("Going to begin build of FASTA index on " + workService.getTransportConnection().getHostName());

		Map<String, String> jobParameters = new HashMap<String, String>();

		jobParameters.put(WaspJobParameters.HOSTNAME, workService.getTransportConnection().getHostName());
		jobParameters.put(GenomeMetadataPlugin.METADATA_PATH_KEY, genomeService.getRemoteBuildPath(workService.getTransportConnection().getHostName(), build));
		jobParameters.put(WaspJobParameters.TEST_ID, String.valueOf(System.currentTimeMillis()));

		String ch = null;
		if (build.hasMetadata("fasta.genome.checksums"))
			ch = build.getMetadata("fasta.genome.checksums");
		jobParameters.put(GenomeMetadataPlugin.FASTA_LIST_KEY, build.getMetadata("fasta.genome.files"));
		jobParameters.put(GenomeMetadataPlugin.FASTA_CHECKSUM_KEY, ch);

		ch = null;
		if (build.hasMetadata("fasta.cdna.checksums"))
			ch = build.getMetadata("fasta.cdna.checksums");
		jobParameters.put(GenomeMetadataPlugin.CDNA_LIST_KEY, build.getMetadata("fasta.cdna.files"));
		jobParameters.put(GenomeMetadataPlugin.CDNA_CHECKSUM_KEY, ch);

		jobParameters.put(GenomeMetadataPlugin.BUILD_NAME_KEY, build.getGenomeBuildNameString());

		logger.info("Sending launch message with flow " + GenomeMetadataPlugin.FASTA_FLOW_NAME + " and build: " + build.getGenomeBuildNameString());

		waspMessageHandlingService.launchBatchJob(GenomeMetadataPlugin.FASTA_FLOW_NAME, jobParameters);
		return (Message<String>) MessageBuilder.withPayload("Initiating FASTA flow on build " + build.getGenomeBuildNameString()).build();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Message<String> launchBuildGtf(GridWorkService workService, Build build, String versionString) throws WaspMessageBuildingException {
		logger.info("Going to begin build of GTF index on " + workService.getTransportConnection().getHostName());

		Map<String, String> jobParameters = new HashMap<String, String>();

		Set<String> knownVersions = getKnownGtfVersions(build);

		if (!knownVersions.contains(versionString)) {
			String mess = "unable to build GTF index version " + versionString + " for build " + build.getGenomeBuildNameString()
					+ " because the version is not in the genomes.properties file.";
			logger.error(mess);
			throw new WaspMessageBuildingException(mess);
		}

		jobParameters.put(WaspJobParameters.HOSTNAME, workService.getTransportConnection().getHostName());
		jobParameters.put(GenomeMetadataPlugin.METADATA_PATH_KEY, genomeService.getRemoteBuildPath(workService.getTransportConnection().getHostName(), build));

		jobParameters.put(WaspJobParameters.TEST_ID, String.valueOf(System.currentTimeMillis()));

		String ch = null;
		if (build.hasMetadata("gtf." + versionString + ".checksum"))
			ch = build.getMetadata("gtf." + versionString + ".checksum");
		jobParameters.put(GenomeMetadataPlugin.GTF_URL_KEY, build.getMetadata("gtf." + versionString + ".file"));
		jobParameters.put(GenomeMetadataPlugin.GTF_CHECKSUM_KEY, ch);

		jobParameters.put(GenomeMetadataPlugin.BUILD_NAME_KEY, build.getGenomeBuildNameString());
		jobParameters.put(GenomeMetadataPlugin.VERSION_KEY, versionString);

		logger.info("Sending launch message with flow " + GenomeMetadataPlugin.GTF_FLOW_NAME + " and build: " + build.getGenomeBuildNameString()
				+ " for version: " + versionString);

		waspMessageHandlingService.launchBatchJob(GenomeMetadataPlugin.GTF_FLOW_NAME, jobParameters);
		return (Message<String>) MessageBuilder.withPayload("Initiating GTF flow on build " + build.getGenomeBuildNameString()).build();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPrefixedGtfPath(GridWorkService workService, Build build, String versionString) {
		return workService.getTransportConnection().prefixRemoteFile(genomeService.getRemoteBuildPath(workService.getTransportConnection().getHostName(), build) + 
				"/gtf/" + versionString + "/" + build.getGenomeBuildNameString() + "." + versionString + ".gtf");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPrefixedGenomeFastaPath(GridWorkService workService, Build build) {
		return workService.getTransportConnection().prefixRemoteFile(genomeService.getRemoteBuildPath(workService.getTransportConnection().getHostName(), build) + 
				"/fasta/" + build.getGenomeBuildNameString() + ".genome.fa");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPrefixedGenomeFastaIndexPath(GridWorkService workService, Build build) {
		return workService.getTransportConnection().prefixRemoteFile(genomeService.getRemoteBuildPath(workService.getTransportConnection().getHostName(), build) + 
				"/fasta/" + build.getGenomeBuildNameString() + ".genome.fa.fai");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPrefixedGenomeFastaDictionaryPath(GridWorkService workService, Build build) {
		return workService.getTransportConnection().prefixRemoteFile(genomeService.getRemoteBuildPath(workService.getTransportConnection().getHostName(), build) +
				"/fasta/" + build.getGenomeBuildNameString() + ".genome.dict");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPrefixedCDnaFastaPath(GridWorkService workService, Build build) {
		return workService.getTransportConnection().prefixRemoteFile(genomeService.getRemoteBuildPath(workService.getTransportConnection().getHostName(), build) +
				"/fasta/" + build.getGenomeBuildNameString() + ".cdna.fa");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPrefixedCDnaFastaIndexPath(GridWorkService workService, Build build) {
		return workService.getTransportConnection().prefixRemoteFile(genomeService.getRemoteBuildPath(workService.getTransportConnection().getHostName(), build) +
				"/fasta/" + build.getGenomeBuildNameString() + ".cdna.fa.fai");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPrefixedCDnaFastaDictionaryPath(GridWorkService workService, Build build) {
		return workService.getTransportConnection().prefixRemoteFile(genomeService.getRemoteBuildPath(workService.getTransportConnection().getHostName(), build) +
				"/fasta/" + build.getGenomeBuildNameString() + ".cdna.dict");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getKnownGtfVersions(Build build) {
		Set<String> keys = build.getMetadataKeySet();
		Set<String> knownVersions = new TreeSet<String>();
		for (String key : keys) {
			if (key.startsWith("gtf."))
				knownVersions.add(key.replaceFirst("gtf.", "").replaceFirst("\\..*", ""));
		}
		return knownVersions;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getKnownVcfVersions(Build build) {
		Set<String> keys = build.getMetadataKeySet();
		Set<String> knownVersions = new TreeSet<String>();
		for (String key : keys) {
			if (key.startsWith("vcf.")) {
				String v = key.replaceFirst("vcf.", "").replaceFirst("\\..*", "");
				logger.trace("saw metadata " + key + " extracted version " + v);
				knownVersions.add(v);
			}
		}
		return knownVersions;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getGtfFileName(Build build, String version) {
		return build.getGenomeBuildNameString() + "." + version + ".gtf";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GenomeIndexStatusKey generateIndexKey(Build build, GridWorkService workService, GenomeIndexType type, String version) {
		return new GenomeIndexStatusKey(build, workService, type, version);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized GenomeIndexStatus getStatus(GenomeIndexStatusKey key) {
		
		logger.debug("GenomeMetadataServiceImpl.getStatus called with key " + key.toString());
		
		if (buildHistory.containsKey(key)) {
			Pair<Date, GenomeIndexStatus> p = buildHistory.get(key);
			logger.trace("build history contains key " + key.toString() + " from " + p.getLeft().toString() + " indicated " + p.getRight().toString());
			// if built, just return
			if (p.getValue().equals(GenomeIndexStatus.BUILT)) {
				logger.debug(key.getType().toString() + " status for build " + key.getBuild().toString() + " marked as built");
				return p.getValue();
			}
			// if less than delay, return last known value
			if (System.currentTimeMillis() - p.getKey().getTime() < TEST_DELAY_IN_MILLIS) {
				logger.trace(key.getType().toString() + " status of build " + key.getBuild().toString() + " was accessed within delay of "
						+ TEST_DELAY_IN_MILLIS + "ms, returning last known status of " + p.getValue());
				return p.getValue();
			}
		} else {
			logger.debug("build history did not contain key " + key.toString());
		}

		GridWorkService workService;
		try {
			workService = hostResolver.getGridWorkService(key.getHostname());
		} catch (GridUnresolvableHostException e1) {
			logger.error("Unable to locate host " + key.getHostname() + " for build " + key.getBuild().getGenomeBuildNameString() + " returning UNBUILDABLE");
			return GenomeIndexStatus.UNBUILDABLE;
		}
		GenomeIndexStatus status = GenomeIndexStatus.UNKNOWN;

		try {

			// if the directory does not exist return BUILDABLE
			// otherwise determine if it has already been built or there is an
			// issue

			if (key.getVersion() == null) {
				if (!genomeService.exists(workService, key.getBuild(), key.getType().toString())) {
					logger.debug("determined that " + key.getType().toString() + " index for " + key.getBuild().getGenomeBuildNameString()
							+ " does not exist on " + key.getHostname());
					status = GenomeIndexStatus.BUILDABLE;

				} else {
					if (genomeService.isCompleted(workService, key.getBuild(), key.getType().toString())) {
						logger.info("identified genome build " + key.getBuild().getGenomeBuildNameString() + " as built on host " + key.getHostname()
								+ " for type " + key.getType().toString());
						status = GenomeIndexStatus.BUILT;
					} else if (genomeService.isFailed(workService, key.getBuild(), key.getType().toString())) {
						logger.error("identified genome build " + key.getBuild().getGenomeBuildNameString() + " as FAILED TO BUILD on host "
								+ key.getHostname() + " for type " + key.getType().toString());
						status = GenomeIndexStatus.UNKNOWN;
					} else if (genomeService.isStarted(workService, key.getBuild(), key.getType().toString())) {
						logger.debug("identified genome build " + key.getBuild().getGenomeBuildNameString() + " as started building on host "
								+ key.getHostname() + " for type " + key.getType().toString());
						status = GenomeIndexStatus.BUILDING;
					} else {
						logger.warn("genome build " + key.getBuild().getGenomeBuildNameString() + " folder exists but semaphore is not present on "
								+ key.getHostname() + " for type " + key.getType().toString() + ", returning BUILDABLE");
						status = GenomeIndexStatus.BUILDABLE;
					}
				}
			} else {
				if (!genomeService.exists(hostResolver.getGridWorkService(key.getHostname()), key.getBuild(), key.getType().toString(), key.getVersion())) {
					logger.debug("determined that " + key.getType().toString() + " index, version " + key.getVersion() + ", for "
							+ key.getBuild().getGenomeBuildNameString() + " does not exist on " + key.getHostname());
					status = GenomeIndexStatus.BUILDABLE;

				} else {
					if (genomeService.isCompleted(workService, key.getBuild(), key.getType().toString(), key.getVersion())) {
						logger.info("identified genome build " + key.getBuild().getGenomeBuildNameString() + " version " + key.getVersion()
								+ " as built on host " + key.getHostname() + " for type " + key.getType().toString());
						status = GenomeIndexStatus.BUILT;
					} else if (genomeService.isFailed(workService, key.getBuild(), key.getType().toString(), key.getVersion())) {
						logger.error("identified genome build " + key.getBuild().getGenomeBuildNameString() + " version " + key.getVersion()
								+ " as FAILED TO BUILD on host " + key.getHostname() + " for type " + key.getType().toString());
						status = GenomeIndexStatus.UNKNOWN;
					} else if (genomeService.isStarted(workService, key.getBuild(), key.getType().toString(), key.getVersion())) {
						logger.debug("identified genome build " + key.getBuild().getGenomeBuildNameString() + " version " + key.getVersion()
								+ " as started building on host " + key.getHostname() + " for type " + key.getType().toString());
						status = GenomeIndexStatus.BUILDING;
					} else {
						logger.warn("genome build " + key.getBuild().getGenomeBuildNameString() + " version " + key.getVersion()
								+ " folder exists but semaphore is not present on " + key.getHostname() + " for type " + key.getType().toString()
								+ ", returning BUILDABLE");
						status = GenomeIndexStatus.BUILDABLE;
					}
				}
			}

			updateStatus(key, status);
			return status;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("problem determining staus of " + key.getType().toString() + " build for " + key.getBuild().getGenomeBuildNameString() + ": "
					+ e.getLocalizedMessage());
			return GenomeIndexStatus.UNKNOWN;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void updateStatus(GenomeIndexStatusKey key, GenomeIndexStatus status) {
		Date date = new Date();
		Date previousDate = null;
		GenomeIndexStatus previousStatus = null;
		if (buildHistory.containsKey(key)) {
			previousDate = buildHistory.get(key).getKey();
			previousStatus = buildHistory.get(key).getRight();
			logger.trace("status for " + key.toString() + " was previously known, was " + previousStatus + " at " + previousDate.toString()
					+ "request to change to " + status.toString() + " " + date.toString());
			if (previousStatus.equals(status) && System.currentTimeMillis() - previousDate.getTime() < TEST_DELAY_IN_MILLIS) {
				logger.trace("within delay, not changing");
				return;
			} else if (previousStatus.equals(status)) {
				logger.trace("exceeded delay, inserting same status with new timestamp");
				buildHistory.put(key, new ImmutablePair<Date, GenomeIndexStatus>(date, status));
				return;
			}
			
		} else {
			logger.trace("build history did not contain key " + key.toString());
		}
		logger.debug("Change of status, inserting new status value");
		buildHistory.put(key, new ImmutablePair<Date, GenomeIndexStatus>(date, status));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized GenomeIndexStatus getVcfStatus(GridWorkService workService, Build build, String versionString) throws IOException {
		logger.trace("getVcfStatus called");

		GenomeIndexStatusKey remoteKey = generateIndexKey(build, workService, GenomeIndexType.VCF, versionString);

		// GTF requires an existing fasta index.
		GenomeIndexStatus fasta = getFastaStatus(workService, build);

		if (fasta.equals(GenomeIndexStatus.BUILDING))
			return GenomeIndexStatus.BUILDING;
		if (!fasta.isAvailable()) {
			String message = "FASTA genome index for build " + build.getGenomeBuildNameString() + " has unknown status " + fasta.toString() + " with message: "
					+ fasta.getMessage();
			logger.warn(message);
			return GenomeIndexStatus.UNKNOWN;
		}

		Set<String> knownVersions = getKnownVcfVersions(build);
		if (!knownVersions.contains(versionString))
			return GenomeIndexStatus.UNBUILDABLE;

		GenomeIndexStatus status = getStatus(remoteKey);
		try {
			if (status.equals(GenomeIndexStatus.BUILDABLE)) {
				logger.debug("determined that index for " + build.getGenomeBuildNameString() + " version " + remoteKey.getVersion() + " does not exist on "
						+ remoteKey.getHostname() + " for type " + remoteKey.getType().toString() + " launching VCF build");
				launchBuildVcf(workService, build, remoteKey.getVersion());
				updateStatus(remoteKey, GenomeIndexStatus.BUILDING);
				return GenomeIndexStatus.BUILDING;
			} else if (status.equals(GenomeIndexStatus.BUILDING) || status.equals(GenomeIndexStatus.BUILT)) {
				return status;
			} else if (status.equals(GenomeIndexStatus.UNKNOWN)) {
				logger.warn("build " + build.getGenomeBuildNameString() + " has an VCF " + " version " + remoteKey.getVersion() + " index status of UNKNOWN");
				return status;
			} else if (status.equals(GenomeIndexStatus.UNBUILDABLE)) {
				logger.error("build " + build.getGenomeBuildNameString() + " has an VCF " + " version " + remoteKey.getVersion()
						+ " index status of UNBUILDABLE with message " + status.getMessage());
				return status;
			} else {
				logger.error("build " + build.getGenomeBuildNameString() + " has an VCF " + " version " + remoteKey.getVersion()
						+ " index status truly UNKNOWN: " + status.toString());
				return GenomeIndexStatus.UNKNOWN;
			}
		} catch (WaspMessageBuildingException e) {
			logger.warn("problem sending message to launch build fasta " + e.getLocalizedMessage());
			return GenomeIndexStatus.UNKNOWN;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Message<String> launchBuildVcf(GridWorkService workService, Build build, String versionString) throws WaspMessageBuildingException {
		logger.info("Going to begin build of VCF index on " + workService.getTransportConnection().getHostName());

		Map<String, String> jobParameters = new HashMap<String, String>();

		Set<String> knownVersions = getKnownVcfVersions(build);

		if (!knownVersions.contains(versionString)) {
			String mess = "unable to build VCF index version " + versionString + " for build " + build.getGenomeBuildNameString()
					+ " because the version is not in the genomes.properties file.";
			logger.error(mess);
			throw new WaspMessageBuildingException(mess);
		}

		jobParameters.put(WaspJobParameters.HOSTNAME, workService.getTransportConnection().getHostName());
		jobParameters.put(GenomeMetadataPlugin.METADATA_PATH_KEY, genomeService.getRemoteBuildPath(workService.getTransportConnection().getHostName(), build));

		jobParameters.put(WaspJobParameters.TEST_ID, String.valueOf(System.currentTimeMillis()));

		String checksum = null;

		if (build.hasMetadata("vcf." + versionString + ".checksum"))
			checksum = build.getMetadata("vcf." + versionString + ".checksum");
		String fileUrl = build.getMetadata("vcf." + versionString + ".file");
		String type = build.getMetadata("vcf." + versionString + ".type");
		
		if (fileUrl == null || type == null) {
			String mess = "File URL (" + fileUrl +") or type (" + type + ") is null and required for building vcf.  Check genomes.properties file.";
			logger.error(mess);
			throw new WaspRuntimeException(mess);
		}
		
		if (!type.equals(VCF_TYPE.SNP.toString()) && !type.equals(VCF_TYPE.INDEL.toString())) {
			String mess = "VCF index type " + type + " unknown, should be SNP or INDEL.  Check genomes.properties file.";
			logger.error(mess);
			throw new WaspRuntimeException(mess);
		}
		
		jobParameters.put(GenomeMetadataPlugin.VCF_URL_KEY, fileUrl);
		jobParameters.put(GenomeMetadataPlugin.VCF_TYPE_KEY, type); 
		jobParameters.put(GenomeMetadataPlugin.VCF_CHECKSUM_KEY, checksum);

		jobParameters.put(GenomeMetadataPlugin.BUILD_NAME_KEY, build.getGenomeBuildNameString());
		jobParameters.put(GenomeMetadataPlugin.VERSION_KEY, versionString);

		logger.info("Sending launch message with flow " + GenomeMetadataPlugin.VCF_FLOW_NAME + " and build: " + build.getGenomeBuildNameString()
				+ " for version: " + versionString);

		waspMessageHandlingService.launchBatchJob(GenomeMetadataPlugin.VCF_FLOW_NAME, jobParameters);
		return (Message<String>) MessageBuilder.withPayload("Initiating VCF flow on build " + build.getGenomeBuildNameString()).build();
	}

	private String getDefaultVersion(Build build, String identifier, String type) throws MetadataException {
		Set<String> knownVersions = new TreeSet<String>();
		Set<String> defaultVersion = new TreeSet<String>();
		for (String key : build.getMetadataKeySet()) {
			if (key.startsWith(identifier)) {
				String version = key.replaceFirst(identifier, "").replaceFirst("\\..*", "");
				if (!key.equals(identifier + version + ".file"))
					continue;
				if (build.hasMetadata(identifier + version + ".type")) {
					String t = build.getMetadata(identifier + version + ".type");
					if (!t.equals(type)){
						String mess = "type requested " + type + " does not match annotated type of " + t + ". Ignoring entry: " + identifier + version;
						logger.debug(mess);
						continue;
					}
				} else {
					logger.debug("matched " + identifier + version + " with no type to type " + type);
				}
				knownVersions.add(version);
				if (key.endsWith(".default") && build.getMetadata(key).equals("true"))
					defaultVersion.add(version);
			}
		}
		
		if (defaultVersion.size() == 1) {
			return defaultVersion.iterator().next();
		} else if (knownVersions.size() == 1) {
			return knownVersions.iterator().next();
		}

		String mess = "Build " + build.getGenomeBuildNameString() + " does not have a default " + type + " version.  Known versions (" + knownVersions.size()
				+ ") default versions (" + defaultVersion.size() + ")";
		logger.error(mess);
		throw new MetadataException(mess);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultGtf(Build build) throws MetadataException {
		return getDefaultVersion(build, "gtf.", "GTF");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getGtfName(Build build, String version) {
		String name = version;
		if (build.hasMetadata("gtf." + version + ".name"))
			name = build.getMetadata("gtf." + version + ".name");
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultVcf(Build build, VCF_TYPE type) throws MetadataException {
		return getDefaultVersion(build, "vcf.", type.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getVcfName(Build build, String version) {
		String name = version;
		if (build.hasMetadata("vcf." + version + ".name"))
			name = build.getMetadata("vcf." + version + ".name");
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getVcfFileName(Build build, String version) {
		String type = build.getMetadata("vcf." + version + ".type");
		return build.getGenomeBuildNameString() + "." + version + "." + type + ".vcf";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPrefixedVcfPath(GridWorkService workService, Build build, String versionString) {
		return workService.getTransportConnection().prefixRemoteFile(genomeService.getRemoteBuildPath(workService.getTransportConnection().getHostName(), build) +
				"/vcf/" + versionString + "/" + getVcfFileName(build, versionString));
	}

	@Override
	public String getPrefixedIndexedGtfPath(GridWorkService workService, Build build, String versionString) {
		return getPrefixedGtfPath(workService, build, versionString) + ".gz";
	}

	@Override
	public String getPrefixedIndexedVcfPath(GridWorkService workService, Build build, String versionString) {
		return getPrefixedVcfPath(workService, build, versionString) + ".gz";
	}


}
