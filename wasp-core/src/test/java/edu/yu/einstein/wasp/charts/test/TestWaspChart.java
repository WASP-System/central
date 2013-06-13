package edu.yu.einstein.wasp.charts.test;

import java.util.LinkedHashSet;
import java.util.Set;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.charts.WaspBoxPlot;
import edu.yu.einstein.wasp.charts.WaspChart;




public class TestWaspChart {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String testJson = "{\"data\":[{\"median\":2.0,\"lq\":1.6,\"uq\":3.5,\"high\":4.0,\"low\":1.1},{\"median\":3.3,\"lq\":2.5,\"uq\":4.0,\"high\":5.2,\"low\":1.9}],\"title\":\"My Boxplot\",\"xaxisName\":\"X-axis\",\"yaxisName\":\"Y-Axis\",\"legend\":\"A nice boxplot of great data\",\"categories\":[\"A\",\"B\"]}";
	
	private WaspBoxPlot testBoxPlot;
	
	@BeforeClass
	public void setup(){
		testBoxPlot = new WaspBoxPlot();
		testBoxPlot.addBoxAndWhiskers("1", 1.1d, 1.6d, 2.0d, 3.5d, 4.0d);
		testBoxPlot.addBoxAndWhiskers("2", 1.9d, 2.5d, 3.3d, 4.0d, 5.2d);
		Set<String> cat = new LinkedHashSet<String>();
		cat.add("A");
		cat.add("B");
		testBoxPlot.setCategories(cat);
		testBoxPlot.setLegend("A nice boxplot of great data");
		testBoxPlot.setTitle("My Boxplot");
		testBoxPlot.setxAxisLabel("X-axis");
		testBoxPlot.setyAxisLabel("Y-Axis");
	}
	
	@Test (groups = "unit-tests")
	public void testJsonEncode() {
		String output = null;
		try{
			output = testBoxPlot.getAsJSON();
			logger.debug(output);
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
			boxPlot = WaspChart.getChart(testJson, WaspBoxPlot.class);
		} catch(JSONException e){
			logger.warn(e.getLocalizedMessage());
		}
		Assert.assertNotNull(boxPlot);
		Assert.assertEquals(testBoxPlot.getTitle(), boxPlot.getTitle());
		//Assert.assertEquals(testBoxPlot.getData().size(), boxPlot.getData().size());
	}
	
  
}
