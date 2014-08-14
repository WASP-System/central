package edu.yu.einstein.wasp.viewpanel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author AJ Jing
 * 
 */

public class GridContent extends Content implements Serializable {

	private static final long serialVersionUID = 2220412642707429616L;

	private List<GridColumn> columns;

	private List<GridDataField> dataFields;

	private List<List<String>> data;

	public GridContent() {
		columns = new ArrayList<GridColumn>();
		dataFields = new ArrayList<GridDataField>();
		data = new ArrayList<List<String>>();
	}

	/**
	 * @return the columns
	 */
	public List<GridColumn> getColumns() {
		return columns;
	}

	/**
	 * @param columns the columns to set
	 */
	public void setColumns(List<GridColumn> columns) {
		this.columns = columns;
	}

	/**
	 * @param column the column to add
	 */
	public void addColumn(GridColumn column) {
		this.columns.add(column);
	}

	/**
	 * @return the dataFields
	 */
	public List<GridDataField> getDataFields() {
		return dataFields;
	}

	/**
	 * @param dataFields the dataFields to set
	 */
	public void setDataFields(List<GridDataField> dataFields) {
		this.dataFields = dataFields;
	}

	/**
	 * @param dataField the dataField to add
	 */
	public void addDataFields(GridDataField dataField) {
		this.dataFields.add(dataField);
	}

	/**
	 * @return the data
	 */
	public List<List<String>> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<List<String>> data) {
		this.data = data;
	}

	/**
	 * @param row the row to add
	 */
	public void addDataRow(List<String> row) {
		this.data.add(row);
	}

}
