package edu.yu.einstein.wasp.load;

import java.util.List;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.load.service.WorkflowLoadService;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.WorkflowMeta;

/**
 * update/inserts db copy of subtype sample from bean definition
 */

public class WorkflowLoaderAndFactory extends WaspResourceLoader implements	FactoryBean<Workflow> {

	@Autowired
	private WorkflowLoadService workflowLoadService;

	private List<String> pageFlowOrder;

	public void setPageFlowOrder(List<String> pageFlowOrder) {
		this.pageFlowOrder = pageFlowOrder;
	}

	private String jobFlowBatchJob;

	public void setJobFlowBatchJob(String jobFlowBatchJob) {
		this.jobFlowBatchJob = jobFlowBatchJob;
	}

	private List<SampleSubtype> sampleSubtypes;

	public void setSampleSubtypes(List<SampleSubtype> sampleSubtypes) {
		this.sampleSubtypes = sampleSubtypes;
	}

	private List<WorkflowMeta> meta;

	public void setMeta(List<WorkflowMeta> workflowMeta) {
		this.meta = workflowMeta;
	}

	private int isActive = 1;

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	@Override
	public Workflow getObject() throws Exception {
		workflowLoadService.updateUiFields(uiFields);
		return workflowLoadService.update(iname, name, isActive, meta, dependencies, sampleSubtypes, pageFlowOrder, jobFlowBatchJob);
	}

	@Override
	public Class<?> getObjectType() {
		return Workflow.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
