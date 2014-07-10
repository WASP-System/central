package edu.yu.einstein.wasp.plugin.mps.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.MetaAttribute.Control.Option;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.plugin.mps.SequenceReadProperties;
import edu.yu.einstein.wasp.plugin.mps.SequenceReadProperties.ReadType;
import edu.yu.einstein.wasp.plugin.mps.service.SequencingService;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.WorkflowService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;

/**
 * 
 * @author asmclellan
 *
 */
@Service
@Transactional("entityManager")
public class SequencingServiceImpl extends WaspServiceImpl implements SequencingService {
	
	public Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	WorkflowService workflowService;

	@Autowired
	private FileType bamFileType;

	@Autowired
	private FileService fileService;
	
	public SequencingServiceImpl() {
		
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReadLengthConfiguredToBeAvailable(Workflow workflow, ResourceCategory resourceCategory, Integer readlength){
		try{ 
			Map<String, List<Option>> resourceOptions = workflowService.getConfiguredOptions(workflow, resourceCategory);
			for (String key : resourceOptions.keySet()){
				if (key.equals(SequenceReadProperties.READ_LENGTH_KEY)){
					for (Option option : resourceOptions.get(key))
						if (option.getValue().equals(readlength.toString()))
							return true;
					return false;
				}
			}
		} catch (MetadataException e){
			logger.warn("Returning false as caught MetadataException: " + e.getLocalizedMessage());
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReadTypeConfiguredToBeAvailable(Workflow workflow, ResourceCategory resourceCategory, ReadType readType){
		try{ 
			Map<String, List<Option>> resourceOptions = workflowService.getConfiguredOptions(workflow, resourceCategory);
			for (String key : resourceOptions.keySet()){
				if (key.equals(SequenceReadProperties.READ_TYPE_KEY)){
					for (Option option : resourceOptions.get(key))
						if (option.getValue().equals(readType.toString()))
							return true;
					return false;
				}
			}
		} catch (MetadataException e){
			logger.warn("Returning false as caught MetadataException: " + e.getLocalizedMessage());
		}
		return false;
	}


	/**
	 * {@inheritDoc}
	 */
	@Transactional("entityManager")
	@Override
	public boolean confirmCellLibrariesAssociatedWithBamFiles(List<SampleSource> cellLibraryList) {
		for(SampleSource cellLibrary : cellLibraryList){
			Set<FileGroup> fileGroupSetFromCellLibrary = fileService.getFilesForCellLibraryByType(cellLibrary, bamFileType);
			if(fileGroupSetFromCellLibrary.isEmpty()){//very unexpected
				logger.debug("no Bam files associated with cellLibrary"); 
				return false;
			}
		}
		return true;
	}
}
