package edu.yu.einstein.wasp.charts;

import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;

import edu.yu.einstein.wasp.exception.ParseException;

/**
 * Base class to generalize all charts
 * @author asmclellan
 *
 */
public abstract class WaspChart {
	
	private String title;
	
	private String legend;
	
	private String xAxisName;
	
	private String yAxisName;
	
	private Set<String> categories;

	public WaspChart() {}
	
	/**
	 * Set up object from JSON representation
	 * @param JSON
	 * @return
	 * @throws JSONException
	 */
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getXAxisName() {
		return xAxisName;
	}

	public void setXAxisName(String xAxisName) {
		this.xAxisName = xAxisName;
	}

	public String getYAxisName() {
		return yAxisName;
	}

	public void setYAxisName(String yAxisName) {
		this.yAxisName = yAxisName;
	}
	
	public String getLegend() {
		return legend;
	}

	public void setLegend(String legend) {
		this.legend = legend;
	}
	
	public Set<String> getCategories() {
		return categories;
	}

	public void setCategories(Set<String> categories) {
		this.categories = categories;
	}
	
	
	/**
	 * sets parameters based on JSON input
	 * @param <T>
	 * @param JSON
	 * @return
	 * @throws JSONException
	 */
	@JsonIgnore
	public static <T extends WaspChart> T getChart(String JSON, Class<T> clazz) throws JSONException{
		ObjectMapper mapper = new ObjectMapper();
		try{
			return mapper.readValue(JSON, clazz);
		} catch(Exception e){
			throw new JSONException("Cannot create object of type " + clazz.getName() + " from json");
		}
	}
	
	@JsonIgnore
	public String getAsJSON() throws JSONException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (Exception e) {
			throw new JSONException("Cannot convert object to JSON");
		}
	}
	

	


	

}
