/**
 * 
 */
package edu.yu.einstein.wasp.plugin.assay;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.MessageChannel;

import edu.yu.einstein.wasp.Hyperlink;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.WebInterfacing;
import edu.yu.einstein.wasp.plugin.cli.ClientMessageI;

/**
 * @author asmclellan
 * 
 */
public class WaspGenericDnaSeqPlugin extends WaspPlugin implements ClientMessageI, WebInterfacing, BatchJobProviding {
	
	public static final String PREPROCESS_ANALYSIS_JOB = "genericDnaSeq.library.preProcess.jobflow.v1";
	
	public static final String AGREGATE_ANALYSIS_JOB = "genericDnaSeq.library.aggregate.jobflow.v1";

	private static Logger logger = LoggerFactory.getLogger(WaspGenericDnaSeqPlugin.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -654454985142650980L;

	public WaspGenericDnaSeqPlugin(String pluginName, Properties waspSiteProperties, MessageChannel channel) {
		super(pluginName, waspSiteProperties, channel);
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Hyperlink getDescriptionPageHyperlink(){
		return new Hyperlink("waspGenericDnaSeq.hyperlink.label", "/wasp-genericDnaSeq/description.do");
	}


	@Override
	public String getBatchJobName(String BatchJobType) {
		if (BatchJobTask.ANALYSIS_LIBRARY_PREPROCESS.equals(BatchJobType))
			return PREPROCESS_ANALYSIS_JOB;
		else if (BatchJobTask.ANALYSIS_AGGREGATE.equals(BatchJobType))
			return AGREGATE_ANALYSIS_JOB;
		return null;
	}


	@Override
	public String getBatchJobNameByArea(String BatchJobType, String area) {
		// TODO Auto-generated method stub
		return null;
	}

}