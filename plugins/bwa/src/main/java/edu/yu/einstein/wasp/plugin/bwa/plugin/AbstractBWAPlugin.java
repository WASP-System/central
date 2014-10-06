/**
 * 
 */
package edu.yu.einstein.wasp.plugin.bwa.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.explore.wasp.ParameterValueRetrievalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import edu.yu.einstein.wasp.exception.JobContextInitializationException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.interfacing.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.interfacing.plugin.cli.ClientMessageI;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobSoftware;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.bwa.service.BwaService;
import edu.yu.einstein.wasp.plugin.bwa.service.impl.BwaServiceImpl;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatus;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.util.SoftwareConfiguration;
import edu.yu.einstein.wasp.util.WaspJobContext;

/**
 * @author calder / asmclellan
 * 
 */
public abstract class AbstractBWAPlugin extends WaspPlugin implements ClientMessageI, BWAPluginCli, BatchJobProviding {

	private static final long serialVersionUID = 4158608859447480863L;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected ResourceType referenceBasedAlignerResourceType;

	@Autowired
	protected SampleService sampleService;
	
	@Autowired
	protected GenomeService genomeService;

	@Autowired
	protected JobService jobService;

	@Autowired
	protected RunService runService;
	
	@Autowired
	private GridHostResolver gridHostResolver;
	
	@Autowired
	private BwaService bwaService;
	
	protected Software software;
	
	public AbstractBWAPlugin(String iName, Properties waspSiteProperties, MessageChannel channel) {
		super(iName, waspSiteProperties, channel);
	}

	public Software getSoftware() {
		return software;
	}

	public void setSoftware(Software software) {
		this.software = software;
	}

	protected SampleSource getCellLibraryFromMessage(Message<String> m) {
		SampleSource cl = null;

		JSONObject jo;
		try {
			jo = new JSONObject(m.getPayload().toString());
			String value = "";
			if (jo.has("cellLibrary")) {
				value = (String) jo.get("cellLibrary");
				cl = sampleService.getSampleSourceDao().findById(new Integer(value));
			}
		} catch (JSONException e) {
			logger.warn("unable to parse JSON");
		}

		return cl;
	}
	
	protected SoftwareConfiguration getDefaultBWASoftwareConfig() throws JobContextInitializationException{
		List<JobSoftware> jobSoftware = new ArrayList<>();
		JobSoftware js = new JobSoftware();
		Job job = new Job();
		js.setJob(job);
		js.setSoftware(software);
		jobSoftware.add(js);
		job.setJobSoftware(jobSoftware);
		WaspJobContext waspJobContext = new WaspJobContext(job);
		return waspJobContext.getConfiguredSoftware(referenceBasedAlignerResourceType);
	}
	
	protected String getGenomeBuildString(Integer cellLibraryId) throws MetadataException {
	    String retval;
	    try {
		retval = genomeService.getDelimitedParameterString(cellLibraryId);
	    } catch (SampleTypeException | ParameterValueRetrievalException e) {
		logger.warn(e.getMessage());
		return null;
	    }
	    if (retval == null) {
	        String message = "genome/build was null, indicating that the genome is unknown or Other";
	        logger.debug(message);
	        throw new MetadataException(message);
	    }
	    return retval;
	}

	@Override
	public void afterPropertiesSet() throws Exception {	}

	@Override
	public void destroy() throws Exception { }
	
	@Override
	public Message<String> buildIndex(Message<String> m) {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help"))
			return buildIndexHelp();

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

			GenomeIndexStatus status = bwaService.getGenomeIndexStatus(host, b);

			return MessageBuilder.withPayload("BWA index for " + b.getGenomeBuildNameString() + " has status " + status.toString()).build();

		} catch (Exception e1) {
			String mess = "unable to build message to launch batch job for building BWA index on " + hostname;
			logger.warn(mess);
			return MessageBuilder.withPayload(mess).build();
		}

	}
	
	@Override
	public Message<String> buildIndexHelp() {
		String mstr = "\ngenome metadata plugin: launch a BWA index build.\n"
				+ "wasp -T bwa-mem -t buildIndex -m \'{organism:\"10090\",genome:GRCm38,build:\"75\",hostname:remote.host.org}\'\n";
		return MessageBuilder.withPayload(mstr).build();
		
	}

}
