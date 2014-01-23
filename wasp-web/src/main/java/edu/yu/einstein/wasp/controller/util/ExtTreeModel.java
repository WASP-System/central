package edu.yu.einstein.wasp.controller.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
	
	private ExtIcon iconCls = ExtIcon.TASK_FOLDER; // default
	
	private boolean isLeaf = false;
	
	private boolean isExpanded = false;
	
	private List<ExtTreeModel> children = new ArrayList<>();
	
	public ExtTreeModel(){}
	
	public ExtTreeModel(String id, ExtIcon iconCls, boolean isExpanded, boolean isLeaf){
		this.id = id;
		this.iconCls = iconCls;
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

	public ExtIcon getIconCls() {
		return iconCls;
	}

	public void setIconCls(ExtIcon iconCls) {
		this.iconCls = iconCls;
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
	public JSONObject getAsJSON() throws JSONException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			// use jackson object mapper to create json as text then wrap in JSONObject (Jackson understands @JsonIgnore)
			return new JSONObject(mapper.writeValueAsString(this));
		} catch (Exception e) {
			throw new JSONException("Cannot convert object to JSON. Caught exception of type " + e.getClass().getName() + " : " +e.getLocalizedMessage());
		}
	}

	
}
