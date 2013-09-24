package edu.yu.einstein.wasp.load;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.load.service.SampleSubtypeLoadService;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleSubtypeMeta;
import edu.yu.einstein.wasp.model.SampleType;

/**
 * update/inserts db copy of subtype sample from bean definition
 * 
 * @author asmclellan
 * 
 */
public class SampleSubtypeLoaderAndFactory extends WaspLoader implements FactoryBean<SampleSubtype> {

	@Autowired
	private SampleSubtypeLoadService sampleSubtypeLoadService;
	
	private SampleSubtype sampleSubtype;

	private SampleType sampleType;

	public void setSampleType(SampleType sampleType) {
		this.sampleType = sampleType;
	}

	private List<SampleSubtypeMeta> meta;

	public void setMeta(List<SampleSubtypeMeta> sampleSubtypeMeta) {
		this.meta = sampleSubtypeMeta;
	}

	public void setMetaFromWrapper(MetaLoadWrapper metaLoadWrapper) {
		meta = metaLoadWrapper.getMeta(SampleSubtypeMeta.class);
	}

	private int isActive = 1;

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	private List<ResourceCategory> compatibleResources;

	public void setCompatibleResources(List<ResourceCategory> compatibleResources) {
		this.compatibleResources = compatibleResources;
	}

	private String applicableRoles;

	public String getApplicableRoles() {
		return this.applicableRoles;
	}

	public void setApplicableRoles(String applicableRolesString) {
		sampleSubtypeLoadService.validateApplicableRoles(applicableRolesString); // throws runtime exception if not valid
		this.applicableRoles = applicableRolesString;
	}
	
	@PostConstruct
	public void init(){
		List<String> areaList = sampleSubtypeLoadService.getAreaListFromUiFields(uiFields);
		sampleSubtype =  sampleSubtypeLoadService.update(iname, name, sampleType,	isActive, compatibleResources, applicableRoles, meta, areaList);
	}

	@Override
	public SampleSubtype getObject() throws Exception {
		return sampleSubtype;
	}

	@Override
	public Class<?> getObjectType() {
		return SampleSubtype.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
