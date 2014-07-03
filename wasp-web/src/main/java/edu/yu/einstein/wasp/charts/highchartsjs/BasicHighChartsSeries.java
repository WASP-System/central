package edu.yu.einstein.wasp.charts.highchartsjs;

import java.awt.Color;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.charts.DataSeries;
import edu.yu.einstein.wasp.service.MessageService;

/**
 * 
 * @author asmclellan
 *
 */
public class BasicHighChartsSeries{
	
	public enum Type{
		AREA, AREASPLINE, BAR, COLUMN, LINE, PIE, SCATTER, SPLINE;
	}
	
    protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	private DataSeries ds;
	
	private boolean showMarker;
	
	private boolean isAnimated;
	
	private Color color;
	
	private Type type;
	
	public BasicHighChartsSeries(DataSeries ds, Type type) {
		this.ds = ds;
		this.type = type;
	}
	
	public BasicHighChartsSeries(DataSeries ds, Type type, boolean showMarker,	boolean isAnimated) {
		this.ds = ds;
		this.type = type;
		this.showMarker = showMarker;
		this.isAnimated = isAnimated;
	}
	
	public BasicHighChartsSeries(DataSeries ds, Type type, Color color) {
		this.ds = ds;
		this.type = type;
		this.color = color;
	}
	
	public BasicHighChartsSeries(DataSeries ds, Type type, boolean showMarker,  boolean isAnimated, Color color) {
		this.ds = ds;
		this.type = type;
		this.showMarker = showMarker;
		this.isAnimated = isAnimated;
		this.color = color;
	}
	
	public BasicHighChartsSeries(DataSeries ds) {
		this.ds = ds;
	}
	
	public BasicHighChartsSeries(DataSeries ds, boolean showMarker,	boolean isAnimated) {
		this.ds = ds;
		this.showMarker = showMarker;
		this.isAnimated = isAnimated;
	}
	
	public BasicHighChartsSeries(DataSeries ds, Color color) {
		this.ds = ds;
		this.color = color;
	}
	
	public BasicHighChartsSeries(DataSeries ds, boolean showMarker,  boolean isAnimated, Color color) {
		this.ds = ds;
		this.showMarker = showMarker;
		this.isAnimated = isAnimated;
		this.color = color;
	}

	
	public DataSeries getDs() {
		return ds;
	}
	
	public void setDs(DataSeries ds) {
		this.ds = ds;
	}
	
	public boolean isShowMarker() {
		return showMarker;
	}
	
	public void setShowMarker(boolean showMarker) {
		this.showMarker = showMarker;
	}
	
	public boolean isAnimated() {
		return isAnimated;
	}
	
	public void setAnimated(boolean isAnimated) {
		this.isAnimated = isAnimated;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	/**
	 * gets HTML string to be inserted into HighCharts code to define this series
	 * @return
	 */
	public String getHtml() throws JSONException{
		return getHtml(null);
	}
	
	/**
	 * gets HTML string to be inserted into HighCharts code to define this series
	 * @return
	 */
	public String getHtml(MessageService messageService) throws JSONException{
		StringBuilder sb = new StringBuilder();
		sb.append("series: [");
		sb.append(getInnerHtml(messageService));	
		sb.append("]\n");	
		return sb.toString();
	}
	
	/**
	 * simply gets the series as an element which may be combined with other series
	 * @return
	 * @throws JSONException
	 */
	public String getInnerHtml() throws JSONException{
		return getInnerHtml(null);
	}
	
	/**
	 * simply gets the series as an element which may be combined with other series
	 * @return
	 * @throws JSONException
	 */
	public String getInnerHtml(MessageService messageService) throws JSONException{
		String seriesName = "";
		String name = "";
		logger.trace("Working with DataSeries named: " + ds.getName());
		if (messageService == null){
			name = ds.getName();
		} else {
			name = ds.getLocalizedName(messageService);
		}
		if (ds.getName() != null && !name.isEmpty())
			seriesName =  "name: '" + name + "', ";
		String seriesColor = "";
		if (color != null){
			String red = Integer.toHexString(color.getRed());
		    String green = Integer.toHexString(color.getGreen());
		    String blue = Integer.toHexString(color.getBlue());

		    String htmlHexCode = "#" + 
		            (red.length() == 1? "0" + red : red) +
		            (green.length() == 1? "0" + green : green) +
		            (blue.length() == 1? "0" + blue : blue); 
		    seriesColor = "color: '" + htmlHexCode + "', ";
		}
		String seriesType = "";
		if (type != null)
			seriesType = "type: '" + type.toString().toLowerCase() + "', ";
		StringBuilder sb = new StringBuilder();
		sb.append("{ " + seriesName + seriesType + seriesColor + "animation:" + isAnimated + ", marker: { enabled: " + showMarker + " },\n");
		sb.append(getDataSeriesAsJsonArray(ds));
		sb.append("}\n");	
		return sb.toString();
	}
	
	public static String getDataSeriesAsJsonArray(DataSeries ds) throws JSONException{
		List<List<Object>> data = ds.getData();
		ObjectMapper mapper = new ObjectMapper();
		try {
			return " data: " + mapper.writeValueAsString(data);
		} catch (Exception e) {
			throw new JSONException("Cannot convert object to JSON");
		}
	}
	
}

