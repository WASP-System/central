/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.babraham.web.service.impl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.babraham.batch.service.impl.BabrahamBatchServiceImpl;
import edu.yu.einstein.wasp.plugin.babraham.batch.tasklet.FastQScreenTasklet;
import edu.yu.einstein.wasp.plugin.babraham.charts.BabrahamPanelRenderer;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQC.PlotType;
import edu.yu.einstein.wasp.plugin.babraham.web.service.BabrahamWebService;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

@Service
@Transactional("entityManager")
public class BabrahamWebServiceImpl extends BabrahamBatchServiceImpl implements BabrahamWebService {
	
	@Value("${wasp.host.servletname:wasp}")
	private String servletName;

	@Autowired
	private MessageService messageService;

	@Autowired
	@Qualifier("fastqc")
	private Software fastqc;
	
	@Autowired
	@Qualifier("fastqscreen")
	private Software fastqscreen;
	
	@Autowired
	@Qualifier("fastqcPlugin")
	private WaspPlugin fastqcPlugin;
	
	@Autowired
	@Qualifier("fastqscreenPlugin")
	private WaspPlugin fastqscreenPlugin;
	
	/**
	 * {@inheritDoc}
	 * @throws PanelException 
	 */
	@Override
	public PanelTab getFastQCDataToDisplay(Integer fileGroupId) throws PanelException{
		PanelTab panelTab = new PanelTab();
		panelTab.setName(fastqc.getName());
		panelTab.setDescription(fastqc.getDescription());
		
		JSONObject json = getJsonForParsedSoftwareOutputByKey(PlotType.QC_RESULT_SUMMARY, fastqc, fileGroupId);
		if (json != null)
			panelTab.addPanel(BabrahamPanelRenderer.getQCResultsSummaryPanel(json, messageService, servletName));
		
		json = getJsonForParsedSoftwareOutputByKey(PlotType.BASIC_STATISTICS, fastqc, fileGroupId);
		if (json != null)
			panelTab.addPanel(BabrahamPanelRenderer.getBasicStatsPanel(json, messageService, servletName));
		
		json = getJsonForParsedSoftwareOutputByKey(PlotType.PER_SEQUENCE_QUALITY, fastqc, fileGroupId);
		if (json != null)
			panelTab.addPanel(BabrahamPanelRenderer.getPerSeqQualityPanel(json, messageService, servletName));
		
		json = getJsonForParsedSoftwareOutputByKey(PlotType.PER_BASE_N_CONTENT, fastqc, fileGroupId);
		if (json != null)
			panelTab.addPanel(BabrahamPanelRenderer.getPerBaseNContentPanel(json, messageService, servletName));
		
		json = getJsonForParsedSoftwareOutputByKey(PlotType.PER_BASE_GC_CONTENT, fastqc, fileGroupId);
		if (json != null)
			panelTab.addPanel(BabrahamPanelRenderer.getPerBaseGcContentPanel(json, messageService, servletName));
		
		json = getJsonForParsedSoftwareOutputByKey(PlotType.PER_SEQUENCE_GC_CONTENT, fastqc, fileGroupId);
		if (json != null)
			panelTab.addPanel(BabrahamPanelRenderer.getPerSeqGcContentPanel(json, messageService, servletName));
		
		json = getJsonForParsedSoftwareOutputByKey(PlotType.PER_BASE_SEQUENCE_CONTENT, fastqc, fileGroupId);
		if (json != null)	
			panelTab.addPanel(BabrahamPanelRenderer.getGetPerBaseSeqContentPanel(json, messageService, servletName));
		
		json = getJsonForParsedSoftwareOutputByKey(PlotType.DUPLICATION_LEVELS, fastqc, fileGroupId);
		if (json != null)
			panelTab.addPanel(BabrahamPanelRenderer.getSeqDuplicationPanel(json, messageService, servletName));
		
		json = getJsonForParsedSoftwareOutputByKey(PlotType.KMER_PROFILES, fastqc, fileGroupId);
		if (json != null)	
			panelTab.addPanel(BabrahamPanelRenderer.getKmerProfilesPanel(json, messageService, servletName));
		
		json = getJsonForParsedSoftwareOutputByKey(PlotType.OVERREPRESENTED_SEQUENCES, fastqc, fileGroupId);
		if (json != null)	
			panelTab.addPanel(BabrahamPanelRenderer.getOverrepresentedSeqPanel(json, messageService, servletName));
		
		json = getJsonForParsedSoftwareOutputByKey(PlotType.SEQUENCE_LENGTH_DISTRIBUTION, fastqc, fileGroupId);
		if (json != null)	
			panelTab.addPanel(BabrahamPanelRenderer.getSeqLengthDistributionPanel(json, messageService, servletName));
		
		json = getJsonForParsedSoftwareOutputByKey(PlotType.PER_BASE_QUALITY, fastqc, fileGroupId);
		if (json != null)	
			panelTab.addPanel(BabrahamPanelRenderer.getPerBaseSeqQualityPanel(json, messageService, servletName));
		return panelTab;
	}
	
	/**
	 * {@inheritDoc}
	 * @throws PanelException 
	 */
	@Override
	public PanelTab getFastQScreenDataToDisplay(Integer fileGroupId) throws PanelException{
		PanelTab panelTab = new PanelTab();
		panelTab.setName(fastqscreen.getName());
		panelTab.setDescription(fastqscreen.getDescription());
		JSONObject json = getJsonForParsedSoftwareOutputByKey(FastQScreenTasklet.FASTQSCREEN_PLOT_META_KEY, fastqscreen, fileGroupId);
		if (json != null)
			panelTab.addPanel(BabrahamPanelRenderer.getFastQScreenPanel(json, messageService, servletName));
		return panelTab;
	}


}
