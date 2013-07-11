package edu.yu.einstein.wasp.plugin.babraham.plugin;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
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

import edu.yu.einstein.wasp.Hyperlink;
import edu.yu.einstein.wasp.charts.WaspBoxPlot;
import edu.yu.einstein.wasp.charts.WaspChart;
import edu.yu.einstein.wasp.charts.WaspChart2D;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridResultImpl;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.plugin.ViewPanel;
import edu.yu.einstein.wasp.plugin.ViewPanelProviding;
import edu.yu.einstein.wasp.plugin.babraham.charts.FastQCHighChartsJs;
import edu.yu.einstein.wasp.plugin.babraham.exception.FastQCDataParseException;
import edu.yu.einstein.wasp.plugin.babraham.service.impl.BabrahamServiceImpl;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQC;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQCDataModule;

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
		} catch (FastQCDataParseException e) {
			logger.warn(e.getLocalizedMessage());
		}
		return null;
	}
	
	private JSONObject getJSONForModule(Map<String, FastQCDataModule> moduleList, String plotType){
		GridResult result = new GridResultImpl();
		
		try {
			PowerMockito.when(mockBabrahamServiceImpl.parseFastQCOutput(result)).thenReturn(moduleList);
		} catch (FastQCDataParseException e) {
			logger.warn(e.getLocalizedMessage());
			return null;
		}
		
		FastQC fastQC = new FastQC();
		ReflectionTestUtils.setField(fastQC, "babrahamService", mockBabrahamServiceImpl);
		Map<String, JSONObject> output = null;
		try {
			output = fastQC.parseOutput(result);
		} catch (Exception e) {
			logger.warn(e.getLocalizedMessage());
			return null;
		}
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
	public void parseOutputTestBasicStats() throws JSONException {
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
	public void parseOutputTestPerSeqQuality() throws JSONException {
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForModule(moduleList, FastQC.PlotType.PER_BASE_QUALITY);
		Assert.assertNotNull(jsonObject);
		logger.debug(jsonObject.toString());
		Assert.assertEquals(jsonObject.getJSONArray("dataSeries").length(), 2);
		Assert.assertEquals(jsonObject.getJSONObject("properties").getString("result"), "pass");
	}
	
	@Test (groups = "unit-tests")
	public void parseSeqDupStats() throws JSONException {
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
	public void testGetPerBaseSeqQualityHighChartsPlotHtml() throws JSONException{
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForModule(moduleList, FastQC.PlotType.PER_BASE_QUALITY);
		Assert.assertNotNull(jsonObject);
		WaspBoxPlot bp = WaspChart.getChart(jsonObject, WaspBoxPlot.class);
		String html = FastQCHighChartsJs.getPerBaseSeqQualityPlotHtml(bp);
		logger.debug(html);
		Assert.assertTrue(html.contains("chart: { type: 'boxplot' }"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetSeqDupHighChartsPlotHtml() throws JSONException{
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForModule(moduleList, FastQC.PlotType.DUPLICATION_LEVELS);
		Assert.assertNotNull(jsonObject);
		WaspChart2D chart = WaspChart.getChart(jsonObject, WaspChart2D.class);
		String html = FastQCHighChartsJs.getBasicSpline(chart, null, null, null, null, 0, null);
		logger.debug(html);
		Assert.assertTrue(html.contains("title: { text: 'Sequence Duplication Level' }"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetBasicStatsHtml() throws Exception{
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForModule(moduleList, FastQC.PlotType.BASIC_STATISTICS);
		Assert.assertNotNull(jsonObject);
		WaspChart chart = WaspChart.getChart(jsonObject, WaspChart.class);
		String html = FastQCHighChartsJs.getKeyValueTableRepresentation(chart);
		logger.debug(html);
		Assert.assertTrue(html.contains("<h3>Basic Statistics</h3>"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetOverrepresentedSequencesHtml() throws JSONException{
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForModule(moduleList, FastQC.PlotType.OVERREPRESENTED_SEQUENCES);
		Assert.assertNotNull(jsonObject);
		WaspChart chart = WaspChart.getChart(jsonObject, WaspChart.class);
		String html = FastQCHighChartsJs.getTableRepresentation(chart);
		logger.debug(html);
		Assert.assertTrue(html.contains("<h3>Overrepresented sequences</h3>"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetPerSeqQualityHtml() throws JSONException{
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForModule(moduleList, FastQC.PlotType.PER_SEQUENCE_QUALITY);
		Assert.assertNotNull(jsonObject);
		WaspChart2D chart = WaspChart.getChart(jsonObject, WaspChart2D.class);
		String html = FastQCHighChartsJs.getBasicSpline(chart,2, null, null, null, 0, null);
		logger.debug(html);
		Assert.assertTrue(html.contains("title: { text: 'Quality Score Distribution Over all Sequences' },"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetPerBaseSeqContentHtml() throws JSONException{
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForModule(moduleList, FastQC.PlotType.PER_BASE_SEQUENCE_CONTENT);
		Assert.assertNotNull(jsonObject);
		WaspChart2D chart = WaspChart.getChart(jsonObject, WaspChart2D.class);
		String html = FastQCHighChartsJs.getSplineForBases(chart);
		logger.debug(html);
		Assert.assertTrue(html.contains("title: { text: 'Quality Score Distribution Over all Sequences' },"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetPerBaseGcContentHtml() throws JSONException{
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForModule(moduleList, FastQC.PlotType.PER_BASE_GC_CONTENT);
		Assert.assertNotNull(jsonObject);
		WaspChart2D chart = WaspChart.getChart(jsonObject, WaspChart2D.class);
		String html = FastQCHighChartsJs.getBasicSpline(chart, 5, null, null, null, 0, 100);
		logger.debug(html);
		Assert.assertTrue(html.contains("title: { text: 'Per Base GC Content' },"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetPerSequenceGcContentHtml() throws JSONException{
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForModule(moduleList, FastQC.PlotType.PER_SEQUENCE_GC_CONTENT);
		Assert.assertNotNull(jsonObject);
		WaspChart2D chart = WaspChart.getChart(jsonObject, WaspChart2D.class);
		String html = FastQCHighChartsJs.getSplineForPerSequenceGC(chart);
		logger.debug(html);
		Assert.assertTrue(html.contains("title: { text: 'Per Sequence GC Content' },"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetPerBaseNContentHtml() throws JSONException{
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForModule(moduleList, FastQC.PlotType.PER_BASE_N_CONTENT);
		Assert.assertNotNull(jsonObject);
		WaspChart2D chart = WaspChart.getChart(jsonObject, WaspChart2D.class);
		String html = FastQCHighChartsJs.getBasicSpline(chart, 5, null, null, null, 0, 100);
		logger.debug(html);
		Assert.assertTrue(html.contains("title: { text: 'N content acrosss all bases' },"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetKmerProfilesHtml() throws JSONException{
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForModule(moduleList, FastQC.PlotType.KMER_PROFILES);
		Assert.assertNotNull(jsonObject);
		WaspChart chart = WaspChart.getChart(jsonObject, WaspChart.class);
		String html = FastQCHighChartsJs.getTableRepresentation(chart);
		logger.debug(html);
		Assert.assertTrue(html.contains("<h3>Kmer Content</h3>"));
	}
	
	@Test (groups = "unit-tests")
	public void testGetSequenceLengthHtml() throws JSONException{
		Map<String, FastQCDataModule> moduleList = getModuleList();
		Assert.assertNotNull(moduleList);
		JSONObject jsonObject = getJSONForModule(moduleList, FastQC.PlotType.SEQUENCE_LENGTH_DISTRIBUTION);
		Assert.assertNotNull(jsonObject);
		WaspChart2D chart = WaspChart.getChart(jsonObject, WaspChart2D.class);
		String html = FastQCHighChartsJs.getBasicSpline(chart, 10, null, null, null, 0, null);
		logger.debug(html);
		Assert.assertTrue(html.contains("title: { text: 'Distribution of sequence lengths over all sequences' },"));
	}
	
	
}
