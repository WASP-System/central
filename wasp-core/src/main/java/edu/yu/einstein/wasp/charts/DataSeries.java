package edu.yu.einstein.wasp.charts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.beust.jcommander.ParameterException;

/**
 * 
 * @author asmclellan
 *
 */
public class DataSeries {
	
	public String name;

	public List<String> rowLabels;
	
	public List<String> colLabels;
	
	public List<List<?>> data;
	
	public Map<String, ?> properties;
	
	public DataSeries() {}
	
	public DataSeries(String name){
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
		if (rowLabels == null)
			return new ArrayList<String>();
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
	 * Column labels might typically represent Y-axis labels but are not necessary or may be used as required. Typically required for boxplots where each row specifies a series of values
	 * to construct the plot (median, quartiles, outliers etc).<br />
	 * Returns an Empty list if no data.
	 * @return
	 */
	public List<String> getColLabels() {
		if (colLabels == null)
			return new ArrayList<String>();
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
	 * Gets data or returns an empty List if none present.
	 * @return
	 */
	public List<List<?>> getData() {
		if (data == null)
			return new ArrayList<List<?>>();
		return data;
	}

	public void setData(List<List<?>> data) {
		this.data = data;
	}

	
	/**
	 * Get a generic map of properties which may be associated with this data series or an empty Map if none set.
	 * @param properties
	 */
	public Map<String, ?> getProperties() {
		if (properties == null)
			return new HashMap<String, Object>();
		return properties;
	}

	/**
	 * Set a generic map of properties which may be associated with this data series
	 * @param properties
	 */
	public void setProperties(Map<String, ?> properties) {
		this.properties = properties;
	}
	
	public void addRow(List<?> row){
		if (row == null || (row.size() > 1 && row.size() != colLabels.size()) )
			throw new ParameterException("supplied parameter is null or size does not match size of colLabels");
		if (data == null)
			data = new ArrayList<List<?>>();
		data.add(row);
	}
	
	/**
	 * Add a row of data. Typically this might represent a list of Y-values where each corresponds to a column label 
	 * provided in 'colLabels'. Consequently the size of List<Object> must match the size of the colLabel list. 
	 * @param label
	 * @param row
	 */
	public void addRow(String label, List<?> row){
		if (rowLabels == null)
			rowLabels = new ArrayList<String>();
		rowLabels.add(label);
		this.addRow(row);
	}
	
	/**
	 * Add a single value with label. Typically this might be a single X-label and Y-value
	 * @param label
	 * @param value
	 */
	public void addSingleValueRow(String label, Object value){
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
	public void addSingleValueRows(Map<String, Object> rows){
		for (String label: rows.keySet())
			this.addSingleValueRow(label, rows.get(label));
	}
	
	@JsonIgnore
	public List<?> getRow(int index){
		return data.get(index);
	}
	
	@JsonIgnore
	public int getRowCount(){
		if (rowLabels == null)
			return 0;
		return rowLabels.size();
	}
	
	@JsonIgnore
	public int getColCount(){
		if (colLabels == null)
			return 0;
		return colLabels.size();
	}

}
