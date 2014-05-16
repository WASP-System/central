package edu.yu.einstein.wasp.load;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import edu.yu.einstein.wasp.load.service.SoftwareLoadService;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.SoftwareMeta;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * update/inserts db copy of subtype sample from bean definition
 * 
 * @author asmclellan
 * 
 */

public class SoftwareLoaderAndFactory<T extends SoftwarePackage> extends WaspResourceLoader implements FactoryBean<T>, ApplicationContextAware {

	@SuppressWarnings("unchecked")
	private Class<T> clazz = (Class<T>) SoftwarePackage.class ; // default
	
	private ApplicationContext ctx;
	
	public void setType(Class<T> clazz){
		this.clazz = clazz;
	}

	@Autowired
	private SoftwareLoadService softwareLoadService;

	private ResourceType resourceType;
	
	private T software;
	
	private String description = "";
	
	private List<SoftwareMeta> meta = new ArrayList<>();

	private int isActive = 1;
	
	private List<SoftwarePackage> softwareDependencies = new ArrayList<>();
	
	private String version;
	
	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public void setMeta(List<SoftwareMeta> meta) {
		this.meta = meta;
	}

	public void setMetaFromWrapper(MetaLoadWrapper metaLoadWrapper) {
		meta = metaLoadWrapper.getMeta(SoftwareMeta.class);
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<SoftwarePackage> getSoftwareDependencies() {
		return softwareDependencies;
	}
	
	public void setSoftwareDependency(SoftwarePackage softwareDependency) {
		this.softwareDependencies.clear();
		this.softwareDependencies.add(softwareDependency);
	}

	public void setSoftwareDependencies(List<SoftwarePackage> softwareDependencies) {
		this.softwareDependencies = softwareDependencies;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@PostConstruct
	public void init(){
		software = softwareLoadService.update(resourceType, meta, iname, name, description, version, softwareDependencies, isActive, clazz);
		software.setApplicationContext(ctx);
	}

	@Override
	public T getObject() throws Exception {
		ctx.getAutowireCapableBeanFactory().autowireBean(software);
		return software;
	}

	@Override
	public Class<?> getObjectType() {
		return clazz;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		this.ctx = ctx;
	}
}
