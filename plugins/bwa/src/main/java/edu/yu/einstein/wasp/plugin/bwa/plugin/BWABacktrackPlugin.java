/**
 * 
 */
package edu.yu.einstein.wasp.plugin.bwa.plugin;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.SoftwareConfigurationException;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.bwa.software.BWABacktrackSoftwareComponent;
import edu.yu.einstein.wasp.util.SoftwareConfiguration;
import edu.yu.einstein.wasp.util.WaspJobContext;

/**
 * @author calder
 * 
 */
public class BWABacktrackPlugin extends AbstractBWAPlugin {

	private static final long serialVersionUID = 8181556629848527079L;

	public static final String JOB_NAME = "bwa-backtrack.alignment";

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private BWABacktrackSoftwareComponent bwa;

	public BWABacktrackPlugin(String iName, Properties waspSiteProperties, MessageChannel channel) {
		super(iName, waspSiteProperties, channel);
	}

	@Transactional("entityManager")
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
		String clidl = WaspSoftwareJobParameters.getCellLibraryListAsParameterValue(Arrays.asList(new Integer[]{cellLibraryId}));
		logger.debug("cellLibraryId: " + cellLibraryId + " list: " + clidl);
		jobParameters.put(WaspSoftwareJobParameters.CELL_LIBRARY_ID_LIST, clidl);
		jobParameters.put(WaspSoftwareJobParameters.GENOME, getGenomeBuildString(Integer.parseInt(clidl)));
		jobParameters.put("uniqCode", Long.toString(Calendar.getInstance().getTimeInMillis())); // overcomes limitation of job being run only once
		runService.launchBatchJob(JOB_NAME, jobParameters);

		return MessageBuilder.withPayload("Signalled begin of BWA alignment").build();
	}

	public Message<String> alignHelp() {
		String mstr = "BWA plugin: align registered fastq files:\n" +
				"wasp -T bwa-backtrack -t align -m \'{cellLibrary:\"101\"}\'\n";
		return MessageBuilder.withPayload(mstr).build();
	}

	@Override
	public String getBatchJobName(String BatchJobType) {
		if (BatchJobType.equals(BatchJobTask.ANALYSIS_LIBRARY_PREPROCESS))
			return JOB_NAME;
		return null;
	}

}
