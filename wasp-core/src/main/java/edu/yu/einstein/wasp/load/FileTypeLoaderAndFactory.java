package edu.yu.einstein.wasp.load;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.load.service.FileTypeLoadService;
import edu.yu.einstein.wasp.model.FileType;



/**
 * update/inserts db copy of type FileType from bean definition
 * 
 * @author asmclellan
 */

public class FileTypeLoaderAndFactory extends WaspLoader implements FactoryBean<FileType> {

	@Autowired
	private FileTypeLoadService fileTypeLoadService;
	
	private FileType fileType;

	private String description;

	private int isActive = 1;

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

	
	
	@PostConstruct
	public void init(){
		fileTypeLoadService.updateUiFields(uiFields);
		fileType =  fileTypeLoadService.update(iname, name, description, isActive);
	}

	@Override
	public FileType getObject() throws Exception {
		return fileType;
	}

	@Override
	public Class<?> getObjectType() {
		return FileType.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
