package edu.yu.einstein.wasp.charts.test;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.charts.WaspBoxPlot;
import edu.yu.einstein.wasp.charts.WaspChart;




public class TestWaspChart {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String testJson = "{\"dataSeries\":[{\"rowLabels\":[\"1\",\"2\"],\"name\":\"box and whisker\",\"data\":[[1.1,1.6,2,3.5,4],[1.9,2.5,3.3,4,5.2]],\"properties\":{},\"colLabels\":[\"low\",\"lq\",\"median\",\"uq\",\"high\"]}],\"yAxisLabel\":\"Y-Axis\",\"title\":\"My Boxplot\",\"description\":\"A nice boxplot of great data\",\"properties\":{},\"xAxisLabel\":\"X-axis\"}";
	
	private WaspBoxPlot testBoxPlot;
	
	@BeforeClass
	public void setup(){
		testBoxPlot = new WaspBoxPlot();
		testBoxPlot.addBoxAndWhiskers("1", 1.1d, 1.6d, 2.0d, 3.5d, 4.0d);
		testBoxPlot.addBoxAndWhiskers("2", 1.9d, 2.5d, 3.3d, 4.0d, 5.2d);
		testBoxPlot.setDescription("A nice boxplot of great data");
		testBoxPlot.setTitle("My Boxplot");
		testBoxPlot.setxAxisLabel("X-axis");
		testBoxPlot.setyAxisLabel("Y-Axis");
	}
	
	@Test (groups = "unit-tests")
	public void testJsonEncode() {
		String output = null;
		try{
			output = testBoxPlot.getAsJSON().toString();
			logger.debug(output.toString());
		} catch(JSONException e){
			logger.warn(e.getLocalizedMessage());
		}
		Assert.assertNotNull(output);
		Assert.assertEquals(output, testJson);
	}
	
	@Test (groups = "unit-tests")
	public void testJsonDecode() {
		WaspBoxPlot boxPlot = null;
		try{
			boxPlot = WaspChart.getChart(new JSONObject(testJson), WaspBoxPlot.class);
		} catch(JSONException e){
			logger.warn(e.getLocalizedMessage());
		}
		Assert.assertNotNull(boxPlot);
		Assert.assertEquals(testBoxPlot.getTitle(), boxPlot.getTitle());
		Assert.assertTrue(testBoxPlot.getBoxAndWhiskers("2").containsKey(WaspBoxPlot.BoxAndWhiskerComponent.LQ));
		Assert.assertEquals(testBoxPlot.getBoxAndWhiskers("2").get(WaspBoxPlot.BoxAndWhiskerComponent.LQ), 2.5d);
		Assert.assertEquals(testBoxPlot.getDataSeries(WaspBoxPlot.BoxPlotSeries.BOX_AND_WHISKER).getRowCount(), 2);
		Assert.assertFalse(testBoxPlot.isOutliers());
	}

}
