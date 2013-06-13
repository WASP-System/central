package edu.yu.einstein.wasp.charts;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Base class to generalize all charts
 * @author asmclellan
 *
 */
public abstract class WaspChart {
	
	protected String title;
	
	protected String legend;
	
	protected List<DataSeries> dataSeries;

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
	
	public String getLegend() {
		return legend;
	}

	public void setLegend(String legend) {
		this.legend = legend;
	}
	
	public List<DataSeries> getDataSeries() {
		if (dataSeries == null)
			return new ArrayList<DataSeries>();
		return dataSeries;
	}

	public void setDataSeries(List<DataSeries> dataSeries) {
		this.dataSeries = dataSeries;
	}
	
	@JsonIgnore
	public DataSeries getDataSeries(String name){
		if (dataSeries == null)
			return null;
		for (DataSeries currentSeries : dataSeries)
			if (currentSeries.getName().equals(name))
				return currentSeries;
		return null;
	}
	
	@JsonIgnore
	protected DataSeries getDataSeriesOrCreateNew(String name){
		DataSeries data = this.getDataSeries(name);
		if (data == null){
			data = new DataSeries(name);
			if (this.dataSeries == null)
				this.dataSeries = new ArrayList<DataSeries>();
			this.dataSeries.add(data);
		}
		return data;
	}
	
	/**
	 * sets parameters based on JSON input
	 * @param <T>
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	@JsonIgnore
	public static <T extends WaspChart> T getChart(JSONObject json, Class<T> clazz) throws JSONException{
		ObjectMapper mapper = new ObjectMapper();
		try{
			return mapper.readValue(json.toString(), clazz);
		} catch(Exception e){
			throw new JSONException("Cannot create object of type " + clazz.getName() + " from json");
		}
	}
	
	@JsonIgnore
	public JSONObject getAsJSON() throws JSONException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			// use jackson object mapper to create json as text then wrap in JSONObject (Jackson understands @JsonIgnore)
			return new JSONObject(mapper.writeValueAsString(this));
		} catch (Exception e) {
			throw new JSONException("Cannot convert object to JSON");
		}
	}

}
