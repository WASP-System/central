package edu.yu.einstein.wasp.controller.util;

import java.util.List;

public class ExtTreeGridResponse {
	
	private List<ExtTreeModel> modelList;
	
	private Long totalCount;
	
	public ExtTreeGridResponse(List<ExtTreeModel> modelList, Long totalCount) {
		this.modelList = modelList;
		this.totalCount = totalCount;
	}

	public List<ExtTreeModel> getModelList() {
		return modelList;
	}

	public Long getTotalCount() {
		return totalCount;
	}

}
