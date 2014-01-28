package edu.yu.einstein.wasp.controller.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

public class ExtTreeModel implements Serializable{
	
	private static final long serialVersionUID = 4827904024619482123L;

	public static class ExtIcon implements Serializable{
		
		private static final long serialVersionUID = -7359308603187815929L;
		
		public static final ExtIcon TASK_FOLDER = new ExtIcon("task-folder");
		public static final ExtIcon TASK = new ExtIcon("task");
		
		private final String iconName;
		
		public String getIconName() {
			return iconName;
		}

		public ExtIcon(String name) {this.iconName = name;}
		
		@Override
		public String toString(){ return iconName; }
		
	}
	
	private String id = "";
	
	private String iconCls = ExtIcon.TASK_FOLDER.toString(); // default
	
	private boolean isLeaf = false;
	
	private boolean isExpanded = false;
	
	private List<ExtTreeModel> children = new ArrayList<>();
	
	public ExtTreeModel(){}
	
	public ExtTreeModel(String id, ExtIcon iconCls, boolean isExpanded, boolean isLeaf){
		this.id = id;
		this.iconCls = iconCls.toString();
		this.isExpanded = isExpanded;
		this.isLeaf = isLeaf;
	}
	
	@JsonIgnore
	public void addChild(ExtTreeModel model){
		this.children.add(model);
	}
	
	@JsonIgnore
	public void addChildren(List<ExtTreeModel> children){
		this.children.addAll(children);
	}
	
	public List<ExtTreeModel> getChildren(){
		if (this.children.isEmpty())
			return null; // otherwise tree does not show arrows to expand
		return this.children;
	}
	
	public void setChildren(List<ExtTreeModel> children) {
		this.children = children;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(ExtIcon iconCls) {
		this.iconCls = iconCls.toString();
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public boolean isExpanded() {
		return isExpanded;
	}

	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}

	@JsonIgnore
	public String getAsJSON() throws JSONException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (Exception e) {
			throw new JSONException("Cannot convert object to JSON. Caught exception of type " + e.getClass().getName() + " : " +e.getLocalizedMessage());
		}
	}
	
	@JsonIgnore
	public String getChildrenAsJSON() throws JSONException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this.children);
		} catch (Exception e) {
			throw new JSONException("Cannot convert object to JSON. Caught exception of type " + e.getClass().getName() + " : " +e.getLocalizedMessage());
		}
	}

	
}
