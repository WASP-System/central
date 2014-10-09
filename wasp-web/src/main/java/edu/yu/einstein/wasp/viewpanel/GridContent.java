package edu.yu.einstein.wasp.viewpanel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import edu.yu.einstein.wasp.Assert;

/**
 * 
 * @author AJ Jing
 * @author asmclellan
 * 
 */

public class GridContent extends Content implements Serializable {

	private static final long serialVersionUID = 2220412642707429616L;

	private List<GridColumn> columns;
	
	private List<List<Action>> actions;

	private List<GridDataField> dataFields;

	private List<List<String>> data;
	
	// first create a set of unique actions
	private Set<Action> actionReferenceSet;

	public GridContent() {
		columns = new ArrayList<GridColumn>();
		dataFields = new ArrayList<GridDataField>();
		actions = new ArrayList<List<Action>>();
		data = new ArrayList<List<String>>();
		actionReferenceSet = new LinkedHashSet<Action>();
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
	 * @return the action columns
	 */
	public List<List<Action>> getActions() {
		return actions;
	}

	/**
	 * @param actions the action columns to set
	 */
	public void setActions(List<List<Action>> actions) {
		this.actions = actions;
	}
	
	/**
	 * Add a new row of actions to the current list
	 * @param rowIndex
	 * @param action
	 */
	public void addActions(List<Action> actions) {
		this.actions.add(actions);
	}
	
	/**
	 * Set the action list for given row
	 * @param rowIndex
	 * @param actions
	 */
	public void setRowActions(int rowIndex, List<Action> actions) {
		this.actions.get(rowIndex).clear();
		this.actions.get(rowIndex).addAll(actions);
	}
	
	/**
	 * Add an action to the action list for given row
	 * @param rowIndex
	 * @param action
	 */
	public void addActionForRow(int rowIndex, Action action) {
		this.actions.get(rowIndex).add(action);
	}
	
	/**
	 * For efficiency, this method should be called only once a full list of rows of actions has been generated. It ensures that each row contains
	 * the same complement of actions. Those actions not originally present in a row will be created and set to be hidden.
	 */
	public void appendActionsToData(){
		// the reference action set must be empty here
		Assert.assertTrue(actionReferenceSet.isEmpty(), "actionReferenceSet is not empty when calling appendActionsToData() in GridContent class");
		
		for (List<Action> actionRow : actions)
			for (Action a : actionRow)
				if (!actionReferenceSet.contains(a)) // uniqueness of actions is defined by icon css class name (iconClassName attribute).
				{
					actionReferenceSet.add(a);
					this.addDataFields(new GridDataField("icon"+Integer.toString(a.getIcnHashCode()).replaceAll("-", "_"), "string"));
					this.addDataFields(new GridDataField("tip"+Integer.toString(a.getIcnHashCode()).replaceAll("-", "_"), "string"));
					this.addDataFields(new GridDataField("cb"+Integer.toString(a.getIcnHashCode()).replaceAll("-", "_"), "string"));
					this.addDataFields(new GridDataField("hide"+Integer.toString(a.getIcnHashCode()).replaceAll("-", "_"), "boolean"));
					
				}
		
		// then add missing actions to rows such that each row contains an identical set of action classes
		Iterator<List<Action>> actionsIterator = actions.iterator();
		Iterator<List<String>> dataIterator = data.iterator();
		while (actionsIterator.hasNext() && dataIterator.hasNext()) {
			List<Action> actionRow = actionsIterator.next();
			List<String> dataRow = dataIterator.next();
			for (Action refAc : actionReferenceSet) {
				if (actionRow.contains(refAc)) {
					Action act = actionRow.get(actionRow.indexOf(refAc));
					dataRow.add(act.getIconClassName());
					dataRow.add(act.getTooltip());
					dataRow.add(act.getCallbackContent());
					dataRow.add("false");
				} else {
					dataRow.add("");
					dataRow.add("");
					dataRow.add("");
					dataRow.add("true");
				}
			}
		}

		// After the actions added to data, clear all actions
		actions.clear();
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

	public Set<Action> getActionReferenceSet() {
		return actionReferenceSet;
	}

}
