package edu.yu.einstein.wasp.charts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import edu.yu.einstein.wasp.exception.InvalidParameterException;

/**
 * 
 * @author asmclellan
 *
 */
public class DataSeries {
	
	private String name;

	private List<String> rowLabels;
	
	private List<String> colLabels;
	
	private List<List<Object>> data;
	
	private Map<String, Object> properties;
	
	public DataSeries() {
		name = "";
		rowLabels = new ArrayList<String>();
		colLabels = new ArrayList<String>();
		data = new ArrayList<List<Object>>();
		properties = new HashMap<String, Object>();
	}
	
	public DataSeries(String name){
		this();
		this.setName(name);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * Row labels might typically represent X-axis labels but are not necessary or may be used as required.
	 * For one dimensional data may these may be specified but the colLabels omitted.<br />
	 * Returns an Empty list if no data.
	 * 
	 * @return
	 */
	public List<String> getRowLabels() {
		return rowLabels;
	}
	
	/**
	 * Row labels might typically represent X-axis labels but are not necessary or may be used as required.
	 * For one dimensional data may these may be specified but the colLabels omitted.
	 * @return
	 */
	public void setRowLabels(List<String> labels) {
		this.rowLabels = labels;
	}
	
	/**
	 * Row labels might typically represent X-axis labels but are not necessary or may be used as required.
	 * For one dimensional data may these may be specified but the colLabels omitted.
	 * @return
	 */
	public void addRowLabel(String label) {
		this.rowLabels.add(label);
	}
	
	/**
	 * Column labels might typically represent Y-axis labels but are not necessary or may be used as required. Typically required for boxplots where each row specifies a series of values
	 * to construct the plot (median, quartiles, outliers etc).<br />
	 * Returns an Empty list if no data.
	 * @return
	 */
	public List<String> getColLabels() {
		return colLabels;
	}

	/**
	 * Column labels might typically represent Y-axis labels but are not necessary or may be used as required. Typically required for boxplots where each row specifies a series of values
	 * to construct the plot (median, quartiles, outliers etc).
	 * @return
	 */
	public void setColLabels(List<String> labels) {
		this.colLabels = labels;
	}
	
	/**
	 * Column labels might typically represent Y-axis labels but are not necessary or may be used as required. Typically required for boxplots where each row specifies a series of values
	 * to construct the plot (median, quartiles, outliers etc).
	 * @return
	 */
	public void addColLabel(String label) {
		this.colLabels.add(label);
	}

	/**
	 * Gets data or returns an empty List if none present.
	 * @return
	 */
	public List<List<Object>> getData() {
		return data;
	}

	public void setData(List<? extends List<Object>> data) {
		this.data = (List<List<Object>>) data;
	}

	
	/**
	 * Get a generic map of properties which may be associated with this data series or an empty Map if none set.
	 * @param properties
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}

	/**
	 * Set a generic map of properties which may be associated with this data series
	 * @param properties
	 */
	@SuppressWarnings("unchecked")
	public void setProperties(Map<String, ?> properties) {
		this.properties = (Map<String, Object>) properties;
	}
	
	
	/**
	 * Add a property
	 * @param key
	 * @param value
	 */
	@JsonIgnore
	public void addProperty(String key, Object value){
		properties.put(key, value);
	}
	
	@SuppressWarnings("unchecked")
	public void addRow(List<?> row){
		if (row == null || (row.size() > 1 && row.size() != colLabels.size()) )
			throw new InvalidParameterException("supplied parameter is null or size does not match size of colLabels");
		if (data == null)
			data = new ArrayList<List<Object>>();
		data.add((List<Object>) row);
	}
	
	
	/**
	 * Add a row of data. Typically this might represent a list of Y-values where each corresponds to a column label 
	 * provided in 'colLabels'. Consequently the size of List<Object> must match the size of the colLabel list. 
	 * @param label
	 * @param row
	 */
	public void addRow(String label, List<?> row){
		rowLabels.add(label);
		this.addRow(row);
	}
	
	/**
	 * Add a single value with label. Typically this might be a single X-label and Y-value
	 * @param label
	 * @param value
	 */
	public void addRowWithSingleColumn(String label, Object value){
		List<Object> row = new ArrayList<Object>();
		row.add(value);
		this.addRow(label, row);
	}
	
	/**
	 * Add rows from a Map. Typically this might represent a series of X-labels with associated Y-values where each
	 * Y-value corresponds to a column label provided in 'colLabels'. Consequently the size of List<Object> must match
	 * the size of the colLabel list.
	 * @param rows
	 */
	public void addRows(Map<String, List<Object>> rows){
		for (String label: rows.keySet())
			this.addRow(label, rows.get(label));
	}
	
	/**
	 * Add rows from a Map. Typically this might represent a series of X-labels with associated Y-value for each.
	 * @param rows
	 */
	public void addRowWithSingleColumn(Map<String, ?> rows){
		for (String label: rows.keySet())
			this.addRowWithSingleColumn(label, rows.get(label));
	}
	
	@JsonIgnore
	public List<?> getRow(int index){
		return data.get(index);
	}
	
	@JsonIgnore
	public int getRowCount(){
		return data.size();
	}
	
	@JsonIgnore
	public int getColCount(){
		if (getRowCount() == 0)
			return 0;
		return data.get(0).size();
	}

}
