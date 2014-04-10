package edu.yu.einstein.wasp.plugin.babraham.plugin;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONObject;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.charts.WaspChart;
import edu.yu.einstein.wasp.charts.WaspChart2D;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridResultImpl;
import edu.yu.einstein.wasp.plugin.babraham.batch.service.impl.BabrahamBatchServiceImpl;
import edu.yu.einstein.wasp.plugin.babraham.charts.BabrahamPanelRenderer;
import edu.yu.einstein.wasp.plugin.babraham.exception.BabrahamDataParseException;
import edu.yu.einstein.wasp.plugin.babraham.software.BabrahamDataModule;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQC;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQCDataModule;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQScreen;
import edu.yu.einstein.wasp.plugin.babraham.web.service.impl.BabrahamWebServiceImpl;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.viewpanel.Panel;
import edu.yu.einstein.wasp.viewpanel.WebContent;

@ContextConfiguration(locations={"/babraham-test-context.xml"})

public class BabrahamQCTests extends AbstractTestNGSpringContextTests {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String servletName = "wasp";
	
	@Autowired
	private MessageService messageService;
	
	@Mock BabrahamBatchServiceImpl mockBabrahamBatchServiceImpl;
	
	@Mock BabrahamWebServiceImpl mockBabrahamWebServiceImpl;
	
	@BeforeClass
	public void beforeClass(){
		MockitoAnnotations.initMocks(this);
		Assert.assertNotNull(mockBabrahamBatchServiceImpl);
		Assert.assertNotNull(mockBabrahamWebServiceImpl);
		Assert.assertNotNull(messageService);

	}
	
	private Map<String, FastQCDataModule> getFastQcModuleList(){
		BabrahamBatchServiceImpl babrahamServiceImpl = new BabrahamBatchServiceImpl();
		InputStream in = this.getClass().getResourceAsStream("/fastqc_data.txt");
		try {
			return babrahamServiceImpl.processFastQCOutput(in);
		} catch (BabrahamDataParseException e) {
			logger.warn(e.getLocalizedMessage());
		}
		return null;
	}
	
	private BabrahamDataModule getFastQScreenModule(){
		BabrahamBatchServiceImpl babrahamServiceImpl = new BabrahamBatchServiceImpl();
		InputStream in = this.getClass().getResourceAsStream("/fastq_screen.txt");
		try {
			return babrahamServiceImpl.processFastQScreenOutput(in);
		} catch (BabrahamDataParseException e) {
			logger.warn(e.getLocalizedMessage());
		}
		return null;
	}
	
	private JSONObject getJSONForFastQScreenModule(BabrahamDataModule module) throws Exception{
		GridResult result = new GridResultImpl();
		
		try {
			PowerMockito.when(mockBabrahamBatchServiceImpl.parseFastQScreenOutput(result.getResultsDirectory())).thenReturn(module);
		} catch (BabrahamDataParseException e) {
			logger.warn(e.getLocalizedMessage());
			return null;
		}
		
		FastQScreen fastQScreen = new FastQScreen();
		ReflectionTestUtils.setField(fastQScreen, "babrahamService", mockBabrahamBatchServiceImpl);
		JSONObject output = null;
		output = fastQScreen.parseOutput(result.getResultsDirectory());
		return output;
	}
	
	private JSONObject getJSONForFastQCModule(Map<String, FastQCDataModule> moduleList, String plotType) throws Exception{
		GridResult result = new GridResultImpl();
		
		try {
			PowerMockito.when(mockBabrahamBatchServiceImpl.parseFastQCOutput(result.getResultsDirectory())).thenReturn(moduleList);
		} catch (BabrahamDataParseException e) {
			logger.warn(e.getLocalizedMessage());
			return null;
		}
		
		FastQC fastQC = new FastQC();
		ReflectionTestUtils.setField(fastQC, "babrahamService", mockBabrahamBatchServiceImpl);
		ReflectionTestUtils.setField(fastQC, "messageService",messageService);
		Map<String, JSONObject> output = null;
		output = fastQC.parseOutput(result.getResultsDirectory());
		return output.get(plotType);
	}
	
	@Test (groups = "unit-tests")
	public void processFastQCTest() {
		Map<String, FastQCDataModule> moduleList = getFastQcModuleList();
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
	public void processFastQScreenTest() {
		BabrahamDataModule bdm = getFastQScreenModule();
		Assert.assertNotNull(bdm);
		logger.debug(">>data for fastQScreen module");
		logger.debug("    Name=" + bdm.getName());
		logger.debug("    iName=" + bdm.getIName());
		logger.debug("    attributes=" + bdm.getAttributes());
		logger.debug("    dataPoints=" + bdm.getDataPoints());
		Assert.assertEquals(bdm.getName(), "FastQ Screen");
	}
	
	@Test (groups = "unit-tests")
	public void parseOutputTestBasicStats() throws Exception {
		Map<String, FastQCDataModule> moduleList = getFastQcModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForFastQCModule(moduleList, FastQC.PlotType.BASIC_STATISTICS);
		Assert.assertNotNull(jsonObject);
		logger.debug(jsonObject.toString());
		WaspChart chart = WaspChart.getChart(jsonObject, WaspChart.class);
		Assert.assertEquals(chart.getDataSeries().size(), 1);
		Assert.assertEquals(chart.getDataSeries().get(0).getRowCount(), 7);
		Assert.assertEquals(jsonObject.getJSONObject("properties").getString("result"), "pass");
	}
	
	@Test (groups = "unit-tests")
	public void parseOutputTestPerSeqQuality() throws Exception {
		Map<String, FastQCDataModule> moduleList = getFastQcModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForFastQCModule(moduleList, FastQC.PlotType.PER_BASE_QUALITY);
		Assert.assertNotNull(jsonObject);
		logger.debug(jsonObject.toString());
		Assert.assertEquals(jsonObject.getJSONArray("dataSeries").length(), 2);
		Assert.assertEquals(jsonObject.getJSONObject("properties").getString("result"), "pass");
	}
	
	@Test (groups = "unit-tests")
	public void parseSeqDupStats() throws Exception {
		Map<String, FastQCDataModule> moduleList = getFastQcModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForFastQCModule(moduleList, FastQC.PlotType.DUPLICATION_LEVELS);
		Assert.assertNotNull(jsonObject);
		logger.debug(jsonObject.toString());
		WaspChart2D chart = WaspChart.getChart(jsonObject, WaspChart2D.class);
		Assert.assertEquals(chart.getDataSeries().size(), 1);
		Assert.assertEquals(chart.getDataSeries().get(0).getRowCount(), 10);
		Assert.assertEquals(jsonObject.getJSONObject("properties").getString("result"), "pass");
	}
	
	@Test (groups = "unit-tests")
	public void testGetParsedQCResultsHtml() throws Exception{
		Map<String, FastQCDataModule> moduleList = getFastQcModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForFastQCModule(moduleList, FastQC.PlotType.QC_RESULT_SUMMARY);
		Assert.assertNotNull(jsonObject);
		Panel panel = BabrahamPanelRenderer.getQCResultsSummaryPanel(jsonObject, messageService, servletName);
		WebContent content = (WebContent) panel.getContent();
		String html = content.getHtmlCode() + content.getScriptCode();
		logger.debug(html);
		Assert.assertTrue(html.contains("<th>FastQC Module</th>"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetPerBaseSeqQualityHighChartsPlotHtml() throws Exception{
		Map<String, FastQCDataModule> moduleList = getFastQcModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForFastQCModule(moduleList, FastQC.PlotType.PER_BASE_QUALITY);
		Assert.assertNotNull(jsonObject);
		Panel panel = BabrahamPanelRenderer.getPerBaseSeqQualityPanel(jsonObject, messageService, servletName);
		WebContent content = (WebContent) panel.getContent();
		String html = content.getHtmlCode() + content.getScriptCode();
		logger.debug(html);
		Assert.assertTrue(html.contains("chart: { type: 'boxplot' }"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetSeqDupHighChartsPlotHtml() throws Exception{
		Map<String, FastQCDataModule> moduleList = getFastQcModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForFastQCModule(moduleList, FastQC.PlotType.DUPLICATION_LEVELS);
		Assert.assertNotNull(jsonObject);
		Panel panel = BabrahamPanelRenderer.getSeqDuplicationPanel(jsonObject, messageService, servletName);
		WebContent content = (WebContent) panel.getContent();
		String html = content.getHtmlCode() + content.getScriptCode();
		logger.debug(html);
		Assert.assertTrue(html.contains("title: { text: 'Sequence Duplication Level' }"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetBasicStatsHtml() throws Exception{
		Map<String, FastQCDataModule> moduleList = getFastQcModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForFastQCModule(moduleList, FastQC.PlotType.BASIC_STATISTICS);
		Assert.assertNotNull(jsonObject);
		Panel panel = BabrahamPanelRenderer.getBasicStatsPanel(jsonObject, messageService, servletName);
		WebContent content = (WebContent) panel.getContent();
		String html = content.getHtmlCode() + content.getScriptCode();
		logger.debug(html);
		Assert.assertTrue(html.contains("<tr><th>Filename: </th><td>18_GCCAAT_L003_R1_003.fastq.gz</td></tr>"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetOverrepresentedSequencesHtml() throws Exception{
		Map<String, FastQCDataModule> moduleList = getFastQcModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForFastQCModule(moduleList, FastQC.PlotType.OVERREPRESENTED_SEQUENCES);
		Assert.assertNotNull(jsonObject);
		Panel panel = BabrahamPanelRenderer.getOverrepresentedSeqPanel(jsonObject, messageService, servletName);
		WebContent content = (WebContent) panel.getContent();
		String html = content.getHtmlCode() + content.getScriptCode();
		logger.debug(html);
		Assert.assertTrue(html.contains("<td>CGGTTCAGCAGGAATGCCGAGATCGGAAGAGCGGTTCAGCAGGAATGCCG</td>"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetPerSeqQualityHtml() throws Exception{
		Map<String, FastQCDataModule> moduleList = getFastQcModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForFastQCModule(moduleList, FastQC.PlotType.PER_SEQUENCE_QUALITY);
		Assert.assertNotNull(jsonObject);
		Panel panel = BabrahamPanelRenderer.getPerSeqQualityPanel(jsonObject, messageService, servletName);
		WebContent content = (WebContent) panel.getContent();
		String html = content.getHtmlCode() + content.getScriptCode();
		logger.debug(html);
		Assert.assertTrue(html.contains("title: { text: 'Quality Score Distribution Over all Sequences' },"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetPerBaseSeqContentHtml() throws Exception{
		Map<String, FastQCDataModule> moduleList = getFastQcModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForFastQCModule(moduleList, FastQC.PlotType.PER_BASE_SEQUENCE_CONTENT);
		Assert.assertNotNull(jsonObject);
		Panel panel = BabrahamPanelRenderer.getGetPerBaseSeqContentPanel(jsonObject, messageService, servletName);
		WebContent content = (WebContent) panel.getContent();
		String html = content.getHtmlCode() + content.getScriptCode();
		logger.debug(html);
		Assert.assertTrue(html.contains("title: { text: 'Quality Score Distribution Over all Sequences' },"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetPerBaseGcContentHtml() throws Exception{
		Map<String, FastQCDataModule> moduleList = getFastQcModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForFastQCModule(moduleList, FastQC.PlotType.PER_BASE_GC_CONTENT);
		Assert.assertNotNull(jsonObject);
		Panel panel = BabrahamPanelRenderer.getPerBaseGcContentPanel(jsonObject, messageService, servletName);
		WebContent content = (WebContent) panel.getContent();
		String html = content.getHtmlCode() + content.getScriptCode();
		logger.debug(html);
		Assert.assertTrue(html.contains("title: { text: 'Per Base GC Content' },"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetPerSequenceGcContentHtml() throws Exception{
		Map<String, FastQCDataModule> moduleList = getFastQcModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForFastQCModule(moduleList, FastQC.PlotType.PER_SEQUENCE_GC_CONTENT);
		Assert.assertNotNull(jsonObject);
		Panel panel = BabrahamPanelRenderer.getPerSeqGcContentPanel(jsonObject, messageService, servletName);
		WebContent content = (WebContent) panel.getContent();
		String html = content.getHtmlCode() + content.getScriptCode();
		logger.debug(html);
		Assert.assertTrue(html.contains("title: { text: 'Per Sequence GC Content' },"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetPerBaseNContentHtml() throws Exception{
		Map<String, FastQCDataModule> moduleList = getFastQcModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForFastQCModule(moduleList, FastQC.PlotType.PER_BASE_N_CONTENT);
		Assert.assertNotNull(jsonObject);
		Panel panel = BabrahamPanelRenderer.getPerBaseNContentPanel(jsonObject, messageService, servletName);
		WebContent content = (WebContent) panel.getContent();
		String html = content.getHtmlCode() + content.getScriptCode();
		Assert.assertTrue(html.contains("title: { text: 'N content across all bases' },"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetKmerProfilesHtml() throws Exception{
		Map<String, FastQCDataModule> moduleList = getFastQcModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForFastQCModule(moduleList, FastQC.PlotType.KMER_PROFILES);
		Assert.assertNotNull(jsonObject);
		Panel panel = BabrahamPanelRenderer.getKmerProfilesPanel(jsonObject, messageService, servletName);
		WebContent content = (WebContent) panel.getContent();
		String html = content.getHtmlCode() + content.getScriptCode();
		logger.debug(html);
		Assert.assertTrue(html.contains("<th>Sequence</th>"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetSequenceLengthHtml() throws Exception{
		Map<String, FastQCDataModule> moduleList = getFastQcModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForFastQCModule(moduleList, FastQC.PlotType.SEQUENCE_LENGTH_DISTRIBUTION);
		Assert.assertNotNull(jsonObject);
		Panel panel = BabrahamPanelRenderer.getSeqLengthDistributionPanel(jsonObject, messageService, servletName);
		WebContent content = (WebContent) panel.getContent();
		String html = content.getHtmlCode() + content.getScriptCode();
		logger.debug(html);
		Assert.assertTrue(html.contains("title: { text: 'Distribution of sequence lengths over all sequences' },"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetFastQScreenHtml() throws Exception{
		BabrahamDataModule module = getFastQScreenModule();
		Assert.assertNotNull(module);
		JSONObject jsonObject = getJSONForFastQScreenModule(module);
		Assert.assertNotNull(jsonObject);
		Panel panel = BabrahamPanelRenderer.getFastQScreenPanel(jsonObject, messageService, servletName);
		WebContent content = (WebContent) panel.getContent();
		String html = content.getHtmlCode() + content.getScriptCode();
		logger.debug(html);
		Assert.assertTrue(html.contains("title: { text: 'FastQ Screen' },"));
	}
	
	
}
