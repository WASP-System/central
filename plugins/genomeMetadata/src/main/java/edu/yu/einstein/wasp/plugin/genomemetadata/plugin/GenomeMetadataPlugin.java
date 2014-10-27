/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.genomemetadata.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.interfacing.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.interfacing.plugin.cli.ClientMessageI;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatus;
import edu.yu.einstein.wasp.plugin.genomemetadata.service.GenomeMetadataService;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.WaspMessageHandlingService;

/**
 * @author
 */
public class GenomeMetadataPlugin extends WaspPlugin implements BatchJobProviding, ClientMessageI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8365130702902188380L;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public final static String FILE_PREFIX_KEY = "filePrefix";

	public final static String FASTA_LIST_KEY = "fastaList";

	public final static String FASTA_CHECKSUM_KEY = "fastaChecksums";

	public final static String CDNA_LIST_KEY = "cdnaList";

	public final static String CDNA_CHECKSUM_KEY = "cdnaChecksums";

	public final static String GTF_URL_KEY = "gtfUrl";

	public final static String GTF_CHECKSUM_KEY = "gtfChecksum";
	
	public final static String VCF_URL_KEY = "vcfSnp";
	
	public final static String VCF_CHECKSUM_KEY = "vcfSnpChecksum";
	
	public final static String VCF_TYPE_KEY = "vcfType";

	public final static String METADATA_PATH_KEY = "metadataPath";

	public final static String BUILD_NAME_KEY = "buildName";
	
	public final static String VERSION_KEY = "version";
	
	public static enum VCF_TYPE { SNP, INDEL };

	@Autowired
	private GenomeMetadataService genomeMetadataService;

	@Autowired
	@Qualifier("waspMessageHandlingServiceImpl")
	// more than one class of type WaspMessageHandlingService so must specify
	private WaspMessageHandlingService waspMessageHandlingService;

	@Autowired
	private GridHostResolver gridHostResolver;

	@Autowired
	private MessageChannelRegistry messageChannelRegistry;

	@Autowired
	private GenomeService genomeService;

	@Autowired
	@Qualifier("genomemetadata")
	private Software genomemetadata;

	public static final String FASTA_FLOW_NAME = "genomemetadata.fasta";

	public static final String GTF_FLOW_NAME = "genomemetadata.gtf";
	
	public static final String VCF_FLOW_NAME = "genomemetadata.vcf";

	public GenomeMetadataPlugin(String iName, Properties waspSiteProperties, MessageChannel channel) {
		super(iName, waspSiteProperties, channel);
	}

	public Message<String> buildFasta(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help"))
			return buildFastaHelp();

		logger.info("launching FASTA build ");

		Build b;
		GridWorkService host;
		String hostname = "";

		try {
			JSONObject jo = new JSONObject(m.getPayload().toString());

			try {
				Integer organism = jo.getInt("organism");
				String genome = jo.getString("genome");
				String build = jo.getString("build");
				b = genomeService.getBuild(organism, genome, build);
			} catch (Exception e) {
				return MessageBuilder.withPayload("Unable to determine build from message: " + m.getPayload().toString()).build();
			}

			try {
				hostname = jo.getString("hostname");
				host = gridHostResolver.getGridWorkService(hostname);
			} catch (Exception e) {
				return MessageBuilder.withPayload("Unable to determine host from message: " + m.getPayload().toString()).build();
			}

			GenomeIndexStatus status = genomeMetadataService.getFastaStatus(host, b);

			return MessageBuilder.withPayload("FASTA index for " + b.getGenomeBuildNameString() + " has status " + status.toString()).build();

		} catch (Exception e1) {
			logger.warn("unable to build message to launch batch job " + FASTA_FLOW_NAME);
			return MessageBuilder.withPayload("Unable to launch batch job " + FASTA_FLOW_NAME).build();
		}

	}

	public Message<String> buildGtf(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help"))
			return buildGtfHelp();

		logger.info("launching GTF build ");

		Build b;
		String version;
		GridWorkService host;
		String hostname = "";

		try {
			JSONObject jo = new JSONObject(m.getPayload().toString());

			
			try {
				Integer organism = jo.getInt("organism");
				String genome = jo.getString("genome");
				String build = jo.getString("build");
				version = jo.getString("version");
				b = genomeService.getBuild(organism, genome, build);
			} catch (Exception e) {
				return MessageBuilder.withPayload("Unable to determine build or GTF version from message: " + m.getPayload().toString()).build();
			}

			try {
				hostname = jo.getString("hostname");
				host = gridHostResolver.getGridWorkService(hostname);
			} catch (Exception e) {
				return MessageBuilder.withPayload("Unable to determine host from message: " + m.getPayload().toString()).build();
			}

			GenomeIndexStatus status = genomeMetadataService.getGtfStatus(host, b, version);

			return MessageBuilder.withPayload("GTF index for " + b.getGenomeBuildNameString() + " version " + version + " has status " + status.toString()).build();

		} catch (Exception e1) {
			logger.warn("unable to build message to launch batch job " + GTF_FLOW_NAME);
			return MessageBuilder.withPayload("Unable to launch batch job " + GTF_FLOW_NAME).build();
		}
	}
	
	public Message<String> buildVcf(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help"))
			return buildVcfHelp();

		logger.info("launching VCF build ");

		Build b;
		String version;
		GridWorkService host;
		String hostname = "";

		try {
			JSONObject jo = new JSONObject(m.getPayload().toString());

			
			try {
				Integer organism = jo.getInt("organism");
				String genome = jo.getString("genome");
				String build = jo.getString("build");
				version = jo.getString("version");
				b = genomeService.getBuild(organism, genome, build);
			} catch (Exception e) {
				return MessageBuilder.withPayload("Unable to determine build or VCF version from message: " + m.getPayload().toString()).build();
			}

			try {
				hostname = jo.getString("hostname");
				host = gridHostResolver.getGridWorkService(hostname);
			} catch (Exception e) {
				return MessageBuilder.withPayload("Unable to determine host from message: " + m.getPayload().toString()).build();
			}

			GenomeIndexStatus status = genomeMetadataService.getVcfStatus(host, b, version);

			return MessageBuilder.withPayload("GTF index for " + b.getGenomeBuildNameString() + " version " + version + " has status " + status.toString()).build();

		} catch (Exception e1) {
			logger.warn("unable to build message to launch batch job " + VCF_FLOW_NAME);
			return MessageBuilder.withPayload("Unable to launch batch job " + VCF_FLOW_NAME).build();
		}
	}

	
	
	private Message<String> buildFastaHelp() {
		String mstr = "\ngenome metadata plugin: launch a FASTA build.\n"
				+ "wasp -T genomemetadata -t buildFasta -m \'{organism:\"10090\",genome:GRCm38,build:\"75\",hostname:remote.host.org}\'\n";
		return MessageBuilder.withPayload(mstr).build();
	}

	private Message<String> buildGtfHelp() {
		String mstr = "\ngenome metadata plugin: launch a GTF build.\n"
				+ "wasp -T genomemetadata -t buildGtf -m \'{organism:\"10090\",genome:GRCm38,build:\"75\",version:ensembl_v75,hostname:remote.host.org}\'\n";
		return MessageBuilder.withPayload(mstr).build();
	}
	
	private Message<String> buildVcfHelp() {
		String mstr = "\ngenome metadata plugin: launch a GTF build.\n"
				+ "wasp -T genomemetadata -t buildVcf -m \'{organism:\"10090\",genome:GRCm38,build:\"75\",version:ensembl_v75,hostname:remote.host.org}\'\n";
		return MessageBuilder.withPayload(mstr).build();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBatchJobName(String batchJobType) {
		return null;
	}

	/**
	 * Wasp plugins implement InitializingBean to give authors an opportunity to
	 * initialize at runtime.
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * Wasp plugins implement DisposableBean to give authors the ability to tear
	 * down on shutdown.
	 */
	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub

	}
}
