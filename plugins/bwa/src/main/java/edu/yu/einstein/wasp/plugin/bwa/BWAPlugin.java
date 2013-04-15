/**
 * 
 */
package edu.yu.einstein.wasp.plugin.bwa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.daemon.service.BatchJobService;
import edu.yu.einstein.wasp.exception.SoftwareConfigurationException;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobSoftware;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.mps.bwa.BWASoftwareComponent;
import edu.yu.einstein.wasp.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.cli.ClientMessageI;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.util.SoftwareConfiguration;
import edu.yu.einstein.wasp.util.WaspJobContext;

/**
 * @author calder
 * 
 */
public class BWAPlugin extends WaspPlugin implements ClientMessageI, BatchJobProviding {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8181556629848527079L;

	public static final String JOB_NAME = "wasp-bwa.alignment";

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ResourceType referenceBasedAlignerResourceType;

	@Autowired
	private SampleService sampleService;

	@Autowired
	private JobService jobService;

	@Autowired
	private RunService runService;

	private BWASoftwareComponent bwa;

	/**
	 * @return the bwa
	 */
	public BWASoftwareComponent getBwa() {
		return bwa;
	}

	/**
	 * @param bwa
	 *            the bwa to set
	 */
	public void setBwa(BWASoftwareComponent bwa) {
		this.bwa = bwa;
	}

	public BWAPlugin(String pluginName, Properties waspSiteProperties, MessageChannel channel) {
		super(pluginName, waspSiteProperties, channel);
	}

	//@Transactional
	public Message<String> align(Message<String> m) throws Exception {
		if (m.getPayload() == null || m.getHeaders().containsKey("help") || m.getPayload().toString().equals("help"))
			return alignHelp();
		
		logger.debug("got align");

		SampleSource cl = getCellLibraryFromMessage(m);
		if (cl == null)
			return MessageBuilder.withPayload("Unable to find cell library").build();
		Integer cellLibraryId = cl.getId();
		if (cellLibraryId == null)
			return MessageBuilder.withPayload("Cell library ID is null").build();
		Job job = sampleService.getJobOfLibraryOnCell(cl);
		if (job == null)
			return MessageBuilder.withPayload("Unable to locate job for cell library").build();
		Integer jobId = job.getId();
		
		logger.debug("working with job " + job.getId());
	
		job = jobService.getJobAndSoftware(job);
		
		WaspJobContext waspJobContext = new WaspJobContext(job);
				
		SoftwareConfiguration softwareConfig = waspJobContext.getConfiguredSoftware(referenceBasedAlignerResourceType);
		if (softwareConfig == null) {
			throw new SoftwareConfigurationException("No software could be configured for jobId=" + jobId + " with resourceType iname=" + 
					referenceBasedAlignerResourceType.getIName());
		}
		Map<String, String> jobParameters = softwareConfig.getParameters();
		String clidl = WaspSoftwareJobParameters.getLibraryCellListAsParameterValue(Arrays.asList(new Integer[]{cellLibraryId}));
		logger.debug("cellLibraryId: " + cellLibraryId + " list: " + clidl);
		jobParameters.put(WaspSoftwareJobParameters.LIBRARY_CELL_ID_LIST, clidl);
		jobParameters.put(WaspSoftwareJobParameters.GENOME, "10090::GRCm38::70");
		jobParameters.put("test", new Date().toGMTString());
		runService.launchBatchJob(JOB_NAME, jobParameters);

		return MessageBuilder.withPayload("Signalled begin of BWA alignment").build();
	}

	public Message<String> alignHelp() {
		String mstr = "BWA plugin: align registered fastq files:\n" +
				"wasp -T wasp-bwa -t align -m \'{cellLibrary:\"101\"}\'\n";
		return MessageBuilder.withPayload(mstr).build();
	}

	public SampleSource getCellLibraryFromMessage(Message<String> m) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String getBatchJobName(String BatchJobType) {
		if (BatchJobType.equals(BatchJobTask.ANALYSIS_LIBRARY_PREPROCESS))
			return this.JOB_NAME;
		return null;
	}

	@Override
	public String getBatchJobNameByArea(String BatchJobType, String area) {
		// TODO Auto-generated method stub
		return null;
	}

}
