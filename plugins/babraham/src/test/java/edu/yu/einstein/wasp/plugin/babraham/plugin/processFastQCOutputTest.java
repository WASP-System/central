package edu.yu.einstein.wasp.plugin.babraham.plugin;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONObject;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.charts.WaspChart;
import edu.yu.einstein.wasp.charts.WaspChart2D;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridResultImpl;
import edu.yu.einstein.wasp.plugin.babraham.charts.FastQCPanelRenderer;
import edu.yu.einstein.wasp.plugin.babraham.exception.BabrahamDataParseException;
import edu.yu.einstein.wasp.plugin.babraham.service.impl.BabrahamServiceImpl;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQC;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQCDataModule;
import edu.yu.einstein.wasp.viewpanel.Panel;
import edu.yu.einstein.wasp.viewpanel.WebContent;

public class processFastQCOutputTest {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Mock BabrahamServiceImpl mockBabrahamServiceImpl;
	
	@BeforeClass
	public void beforeClass(){
		MockitoAnnotations.initMocks(this);
		Assert.assertNotNull(mockBabrahamServiceImpl);
	}
	
	private Map<String, FastQCDataModule> getModuleList(){
		BabrahamServiceImpl babrahamServiceImpl = new BabrahamServiceImpl();
		InputStream in = this.getClass().getResourceAsStream("/fastqc_data.txt");
		try {
			return babrahamServiceImpl.processFastQCOutput(in);
		} catch (BabrahamDataParseException e) {
			logger.warn(e.getLocalizedMessage());
		}
		return null;
	}
	
	private JSONObject getJSONForModule(Map<String, FastQCDataModule> moduleList, String plotType) throws Exception{
		GridResult result = new GridResultImpl();
		
		try {
			PowerMockito.when(mockBabrahamServiceImpl.parseFastQCOutput(result)).thenReturn(moduleList);
		} catch (BabrahamDataParseException e) {
			logger.warn(e.getLocalizedMessage());
			return null;
		}
		
		FastQC fastQC = new FastQC();
		ReflectionTestUtils.setField(fastQC, "babrahamService", mockBabrahamServiceImpl);
		Map<String, JSONObject> output = null;
		output = fastQC.parseOutput(result);
		return output.get(plotType);
	}
	
	@Test (groups = "unit-tests")
	public void processFastQCTest() {
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		
		for (String key : moduleList.keySet()){
			logger.debug(">>data for key=" + key);
			logger.debug("    Name=" + moduleList.get(key).getName());
			logger.debug("    iName=" + moduleList.get(key).getIName());
			logger.debug("    result=" + moduleList.get(key).getResult());
			logger.debug("    attributes=" + moduleList.get(key).getAttributes());
			logger.debug("    keyValueData=" + moduleList.get(key).getKeyValueData());
			logger.debug("    dataPoints=" + moduleList.get(key).getDataPoints());
		}
		
		FastQCDataModule module = moduleList.get(FastQC.PlotType.BASIC_STATISTICS);
		Assert.assertEquals(module.getDataPoints().size(), 7);
		Assert.assertEquals(module.getKeyValueData().size(), 0);
		module = moduleList.get(FastQC.PlotType.DUPLICATION_LEVELS);
		Assert.assertEquals(module.getDataPoints().size(), 10);
		Assert.assertEquals(module.getKeyValueData().size(), 1);
		module = moduleList.get(FastQC.PlotType.OVERREPRESENTED_SEQUENCES);
		Assert.assertEquals(module.getDataPoints().size(), 9);
		Assert.assertEquals(module.getKeyValueData().size(), 0);
		Assert.assertEquals(module.getResult(), "fail");
		module = moduleList.get(FastQC.PlotType.PER_BASE_SEQUENCE_CONTENT);
		Assert.assertEquals(module.getName(), "Per base sequence content");
		Assert.assertEquals(module.getAttributes().size(), 5);
		
	}
	
	@Test (groups = "unit-tests")
	public void parseOutputTestBasicStats() throws Exception {
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForModule(moduleList, FastQC.PlotType.BASIC_STATISTICS);
		Assert.assertNotNull(jsonObject);
		logger.debug(jsonObject.toString());
		WaspChart chart = WaspChart.getChart(jsonObject, WaspChart.class);
		Assert.assertEquals(chart.getDataSeries().size(), 1);
		Assert.assertEquals(chart.getDataSeries().get(0).getRowCount(), 7);
		Assert.assertEquals(jsonObject.getJSONObject("properties").getString("result"), "pass");
	}
	
	@Test (groups = "unit-tests")
	public void parseOutputTestPerSeqQuality() throws Exception {
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForModule(moduleList, FastQC.PlotType.PER_BASE_QUALITY);
		Assert.assertNotNull(jsonObject);
		logger.debug(jsonObject.toString());
		Assert.assertEquals(jsonObject.getJSONArray("dataSeries").length(), 2);
		Assert.assertEquals(jsonObject.getJSONObject("properties").getString("result"), "pass");
	}
	
	@Test (groups = "unit-tests")
	public void parseSeqDupStats() throws Exception {
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForModule(moduleList, FastQC.PlotType.DUPLICATION_LEVELS);
		Assert.assertNotNull(jsonObject);
		logger.debug(jsonObject.toString());
		WaspChart2D chart = WaspChart.getChart(jsonObject, WaspChart2D.class);
		Assert.assertEquals(chart.getDataSeries().size(), 1);
		Assert.assertEquals(chart.getDataSeries().get(0).getRowCount(), 10);
		Assert.assertEquals(jsonObject.getJSONObject("properties").getString("result"), "pass");
	}
	
	@Test (groups = "unit-tests")
	public void testGetParsedQCResultsHtml() throws Exception{
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForModule(moduleList, FastQC.PlotType.QC_RESULT_SUMMARY);
		Assert.assertNotNull(jsonObject);
		Panel panel = FastQCPanelRenderer.getQCResultsSummaryPanel(jsonObject);
		WebContent content = (WebContent) panel.getContent();
		String html = content.getHtmlCode() + content.getScriptCode();
		logger.debug(html);
		Assert.assertTrue(html.contains("<h3>FastQC Results Summary</h3>"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetPerBaseSeqQualityHighChartsPlotHtml() throws Exception{
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForModule(moduleList, FastQC.PlotType.PER_BASE_QUALITY);
		Assert.assertNotNull(jsonObject);
		Panel panel = FastQCPanelRenderer.getPerBaseSeqQualityPanel(jsonObject);
		WebContent content = (WebContent) panel.getContent();
		String html = content.getHtmlCode() + content.getScriptCode();
		logger.debug(html);
		Assert.assertTrue(html.contains("chart: { type: 'boxplot' }"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetSeqDupHighChartsPlotHtml() throws Exception{
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForModule(moduleList, FastQC.PlotType.DUPLICATION_LEVELS);
		Assert.assertNotNull(jsonObject);
		Panel panel = FastQCPanelRenderer.getSeqDuplicationPanel(jsonObject);
		WebContent content = (WebContent) panel.getContent();
		String html = content.getHtmlCode() + content.getScriptCode();
		logger.debug(html);
		Assert.assertTrue(html.contains("title: { text: 'Sequence Duplication Level' }"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetBasicStatsHtml() throws Exception{
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForModule(moduleList, FastQC.PlotType.BASIC_STATISTICS);
		Assert.assertNotNull(jsonObject);
		Panel panel = FastQCPanelRenderer.getBasicStatsPanel(jsonObject);
		WebContent content = (WebContent) panel.getContent();
		String html = content.getHtmlCode() + content.getScriptCode();
		logger.debug(html);
		Assert.assertTrue(html.contains("<h3>Basic Statistics</h3>"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetOverrepresentedSequencesHtml() throws Exception{
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForModule(moduleList, FastQC.PlotType.OVERREPRESENTED_SEQUENCES);
		Assert.assertNotNull(jsonObject);
		Panel panel = FastQCPanelRenderer.getOverrepresentedSeqPanel(jsonObject);
		WebContent content = (WebContent) panel.getContent();
		String html = content.getHtmlCode() + content.getScriptCode();
		logger.debug(html);
		Assert.assertTrue(html.contains("<h3>Overrepresented sequences</h3>"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetPerSeqQualityHtml() throws Exception{
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForModule(moduleList, FastQC.PlotType.PER_SEQUENCE_QUALITY);
		Assert.assertNotNull(jsonObject);
		Panel panel = FastQCPanelRenderer.getPerSeqQualityPanel(jsonObject);
		WebContent content = (WebContent) panel.getContent();
		String html = content.getHtmlCode() + content.getScriptCode();
		logger.debug(html);
		Assert.assertTrue(html.contains("title: { text: 'Quality Score Distribution Over all Sequences' },"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetPerBaseSeqContentHtml() throws Exception{
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForModule(moduleList, FastQC.PlotType.PER_BASE_SEQUENCE_CONTENT);
		Assert.assertNotNull(jsonObject);
		Panel panel = FastQCPanelRenderer.getGetPerBaseSeqContentPanel(jsonObject);
		WebContent content = (WebContent) panel.getContent();
		String html = content.getHtmlCode() + content.getScriptCode();
		logger.debug(html);
		Assert.assertTrue(html.contains("title: { text: 'Quality Score Distribution Over all Sequences' },"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetPerBaseGcContentHtml() throws Exception{
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForModule(moduleList, FastQC.PlotType.PER_BASE_GC_CONTENT);
		Assert.assertNotNull(jsonObject);
		Panel panel = FastQCPanelRenderer.getPerBaseGcContentPanel(jsonObject);
		WebContent content = (WebContent) panel.getContent();
		String html = content.getHtmlCode() + content.getScriptCode();
		logger.debug(html);
		Assert.assertTrue(html.contains("title: { text: 'Per Base GC Content' },"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetPerSequenceGcContentHtml() throws Exception{
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForModule(moduleList, FastQC.PlotType.PER_SEQUENCE_GC_CONTENT);
		Assert.assertNotNull(jsonObject);
		Panel panel = FastQCPanelRenderer.getPerSeqGcContentPanel(jsonObject);
		WebContent content = (WebContent) panel.getContent();
		String html = content.getHtmlCode() + content.getScriptCode();
		logger.debug(html);
		Assert.assertTrue(html.contains("title: { text: 'Per Sequence GC Content' },"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetPerBaseNContentHtml() throws Exception{
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForModule(moduleList, FastQC.PlotType.PER_BASE_N_CONTENT);
		Assert.assertNotNull(jsonObject);
		Panel panel = FastQCPanelRenderer.getPerBaseNContentPanel(jsonObject);
		WebContent content = (WebContent) panel.getContent();
		String html = content.getHtmlCode() + content.getScriptCode();
		Assert.assertTrue(html.contains("title: { text: 'N content acrosss all bases' },"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetKmerProfilesHtml() throws Exception{
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForModule(moduleList, FastQC.PlotType.KMER_PROFILES);
		Assert.assertNotNull(jsonObject);
		Panel panel = FastQCPanelRenderer.getKmerProfilesPanel(jsonObject);
		WebContent content = (WebContent) panel.getContent();
		String html = content.getHtmlCode() + content.getScriptCode();
		logger.debug(html);
		Assert.assertTrue(html.contains("<h3>Kmer Content</h3>"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetSequenceLengthHtml() throws Exception{
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForModule(moduleList, FastQC.PlotType.SEQUENCE_LENGTH_DISTRIBUTION);
		Assert.assertNotNull(jsonObject);
		Panel panel = FastQCPanelRenderer.getSeqLengthDistributionPanel(jsonObject);
		WebContent content = (WebContent) panel.getContent();
		String html = content.getHtmlCode() + content.getScriptCode();
		logger.debug(html);
		Assert.assertTrue(html.contains("title: { text: 'Distribution of sequence lengths over all sequences' },"));
	}
	
	
}
