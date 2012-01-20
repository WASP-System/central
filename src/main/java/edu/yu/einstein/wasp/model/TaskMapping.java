
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
@Table(name="task_mapping")
public class TaskMapping extends WaspModel {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="task_mapping_id")
	private Integer taskMappingId;
	
	@Column(name="taskid")
	private Integer taskId;

	@Column(name="status")
	private String status;
	
	


	@Column(name="list_map")
	private String listMap;
	
	@Column(name="detail_map")
	private String detailMap;

	@Column(name="permission")
	private String permission;
	
	@Column(name="show_in_dashboard")
	private boolean showInDashboard;

	@Transient
	private Integer stateCount;

	@NotAudited
	@ManyToOne
	@JoinColumn(name="taskid", insertable=false, updatable=false)
	protected Task task;

	/**
	 * setTask (Task task)
	 *
	 * @param task
	 *
	 */
	public void setTask (Task task) {
		this.task = task;
		this.taskId = task.taskId;
	}

	/**
	 * getTask ()
	 *
	 * @return task
	 *
	 */
	
	public Task getTask () {
		return this.task;
	}
	
	
	public Integer getStateCount() {
		return stateCount;
	}

	
	public void setStateCount(Integer stateCount) {
		this.stateCount = stateCount;
	}

	public Integer getTaskMappingId() {
		return taskMappingId;
	}

	public void setTaskMappingId(Integer taskMappingId) {
		this.taskMappingId = taskMappingId;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getListMap() {
		return listMap;
	}

	public void setListMap(String listMap) {
		this.listMap = listMap;
	}

	public String getDetailMap() {
		return detailMap;
	}

	public void setDetailMap(String detailMap) {
		this.detailMap = detailMap;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public boolean isShowInDashboard() {
		return showInDashboard;
	}

	public void setShowInDashboard(boolean showInDashboard) {
		this.showInDashboard = showInDashboard;
	}

	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
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
