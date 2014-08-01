package edu.yu.einstein.wasp.controller.util;

import java.util.ArrayList;
import java.util.List;

public class ExtGridResponse <E extends ExtModel> {
	
	private List<E> modelList = new ArrayList<>();
	
	private Long totalCount = 0L;
	
	public ExtGridResponse(){
		
	}
	
	public ExtGridResponse(List<E> modelList, Long totalCount) {
		this.modelList = modelList;
		this.totalCount = totalCount;
	}
	
	public void addModel(E model){
		modelList.add(model);
	}

	public List<? extends E> getModelList() {
		return modelList;
	}

	public void setModelList(List<E> modelList) {
		this.modelList = modelList;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public Long getTotalCount() {
		return totalCount;
	}

}
