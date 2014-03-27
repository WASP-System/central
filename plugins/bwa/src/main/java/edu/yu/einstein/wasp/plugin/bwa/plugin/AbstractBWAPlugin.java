/**
 * 
 */
package edu.yu.einstein.wasp.plugin.bwa.plugin;

import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.explore.wasp.ParameterValueRetrievalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;

import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.cli.ClientMessageI;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * @author calder / asmclellan
 * 
 */
public abstract class AbstractBWAPlugin extends WaspPlugin implements ClientMessageI, BatchJobProviding {

	private static final long serialVersionUID = 8181556629848527079L;

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

	public AbstractBWAPlugin(String iName, Properties waspSiteProperties, MessageChannel channel) {
		super(iName, waspSiteProperties, channel);
	}

	public abstract Message<String> align(Message<String> m) throws Exception;

	public abstract Message<String> alignHelp();

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
	
	protected String getGenomeBuildString(Integer cellLibraryId){
		try {
			Sample library = sampleService.getLibrary(sampleService.getCellLibraryBySampleSourceId(cellLibraryId));
			return genomeService.getDelimitedParameterString(genomeService.getBuild(library));
		} catch (SampleTypeException | ParameterValueRetrievalException e) {
			logger.warn(e.getMessage());
			return null;
		}	
	}

	@Override
	public void afterPropertiesSet() throws Exception {	}

	@Override
	public void destroy() throws Exception { }

}
