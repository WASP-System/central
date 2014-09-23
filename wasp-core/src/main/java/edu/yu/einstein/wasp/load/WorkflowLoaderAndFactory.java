package edu.yu.einstein.wasp.load;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.load.service.WorkflowLoadService;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.WorkflowMeta;
import edu.yu.einstein.wasp.service.WorkflowService;

/**
 * update/inserts db copy of subtype sample from bean definition
 * 
 * @author asmclellan
 */

public class WorkflowLoaderAndFactory extends WaspResourceLoader implements	FactoryBean<Workflow> {
	
	
	@Autowired
	private WorkflowLoadService workflowLoadService;
	
	private Workflow workflow;

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
	
	private Boolean isDefault = false;
	
	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	@PostConstruct
	public void init(){
		WorkflowMeta wfm = new WorkflowMeta();
		wfm.setK(WorkflowService.WORKFLOW_AREA + "." + WorkflowService.IS_WORKFLOW_DEFAULT_META_KEY);
		wfm.setV(isDefault.toString());
		if (meta == null)
			meta = new ArrayList<WorkflowMeta>();
		meta.add(wfm);
		workflow =  workflowLoadService.update(iname, name, isActive, meta, dependencies, sampleSubtypes, pageFlowOrder, jobFlowBatchJob);
	}

	@Override
	public Workflow getObject() throws Exception {
		return workflow;
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
