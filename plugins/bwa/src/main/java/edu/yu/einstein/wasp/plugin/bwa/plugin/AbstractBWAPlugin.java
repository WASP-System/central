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
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import edu.yu.einstein.wasp.exception.JobContextInitializationException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.interfacing.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.interfacing.plugin.cli.ClientMessageI;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobSoftware;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
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
		try {
			return genomeService.getDelimitedParameterString(cellLibraryId);
		} catch (SampleTypeException | ParameterValueRetrievalException e) {
			logger.warn(e.getMessage());
			return null;
		} catch (NullPointerException e1) {
		    String message = "genome/build was null, indicating that the genome is unknown or Other";
		    logger.debug(message);
		    throw new MetadataException(message);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {	}

	@Override
	public void destroy() throws Exception { }

}
