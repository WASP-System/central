
/**
 *
 * TaskMapping.java 
 * @author sasha 
 *  
 * the Task Mapping
 *
 *
 */

package edu.yu.einstein.wasp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name="taskmapping")
public class TaskMapping extends WaspModel {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="taskmappingid")
	private Integer taskMappingId;
	
	@Column(name="iname")
	private String iName;
	
	@Column(name="name")
	private String name;
	
	@Column(name="stepname")
	private String stepName;

	@Column(name="status")
	private String status;
	
	@Column(name="listmap")
	private String listMap;
	
	@Column(name="permission")
	private String permission;
	
	@Column(name="dashboardsortorder")
	private Integer dashboardSortOrder;
	
	@Column(name="isactive")
	private Integer isActive;

	@Transient
	private Integer stateCount;

	
	public Integer getTaskMappingId() {
		return taskMappingId;
	}

	public void setTaskMappingId(Integer taskMappingId) {
		this.taskMappingId = taskMappingId;
	}
	
	public void setIName (String iName) {
		this.iName = iName;
	}

	public String getIName () {
		return this.iName;
	}
	
	public void setName (String name) {
		this.name = name;
	}

	public String getName () {
		return this.name;
	}


	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getListMap() {
		return listMap;
	}

	public void setListMap(String listMap) {
		this.listMap = listMap;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Integer getDashboardSortOrder() {
		return dashboardSortOrder;
	}

	public void setDashboardSortOrder(Integer dashboardSortOrder) {
		this.dashboardSortOrder = dashboardSortOrder;
	}

	public Integer getStateCount() {
		return stateCount;
	}

	public void setStateCount(Integer stateCount) {
		this.stateCount = stateCount;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((taskMappingId == null) ? 0 : taskMappingId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskMapping other = (TaskMapping) obj;
		if (taskMappingId == null) {
			if (other.taskMappingId != null)
				return false;
		} else if (!taskMappingId.equals(other.taskMappingId))
			return false;
		return true;
	}
	
	
			
	
}
