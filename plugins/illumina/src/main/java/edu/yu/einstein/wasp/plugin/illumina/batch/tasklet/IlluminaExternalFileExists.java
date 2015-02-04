package edu.yu.einstein.wasp.plugin.illumina.batch.tasklet;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Value;

import edu.yu.einstein.wasp.daemon.batch.tasklets.file.ExternalFileExistsTasklet;
import edu.yu.einstein.wasp.plugin.illumina.plugin.IlluminaResourceCategory;
import edu.yu.einstein.wasp.software.SoftwarePackage;

public class IlluminaExternalFileExists extends ExternalFileExistsTasklet {
	
	private String resourceCategoryIName;
	
	@Value("illumina.data.hiseq.dir")
	String hiseqDataDir;
	
	@Value("illumina.data.personalseq.dir")
	String miseqDataDir;
	
	public IlluminaExternalFileExists() {
		super();
	}
	
	public IlluminaExternalFileExists(SoftwarePackage softwarePackage, String resourceCategoryIName, String filename) {
		this.softwarePackage = softwarePackage;
		this.resourceCategoryIName = resourceCategoryIName;
		this.filename = filename;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		if (resourceCategoryIName.equals(IlluminaResourceCategory.HISEQ_2000) || resourceCategoryIName.equals(IlluminaResourceCategory.HISEQ_2500))
			this.rootDirectory = hiseqDataDir;
		else if (resourceCategoryIName.equals(IlluminaResourceCategory.PERSONAL))
			this.rootDirectory = miseqDataDir;
	}
	
	/**
	 * Called immediately prior to completion of step
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution){
		return super.afterStep(stepExecution);
	}
	
	

}
