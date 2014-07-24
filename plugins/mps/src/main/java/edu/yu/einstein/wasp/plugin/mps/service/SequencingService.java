package edu.yu.einstein.wasp.plugin.mps.service;

import java.util.List;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.plugin.mps.SequenceReadProperties.ReadType;
import edu.yu.einstein.wasp.service.WaspService;

public interface SequencingService extends WaspService {

	/**
	 * For a given workflow and resourceCategory, is the given read length available? Returns false if there is no read length 
	 * linked with the workflow/resourceCategory association or if the read length provided is not permitted.
	 * @param workflow
	 * @param resourceCategory
	 * @param readlength
	 * @return
	 */
	public boolean isReadLengthConfiguredToBeAvailable(Workflow workflow, ResourceCategory resourceCategory, Integer readlength);

	/**
	 * For a given workflow and resourceCategory, is the given read type available? Returns false if there is no read type 
	 * linked with the workflow/resourceCategory association or if the read type provided is not permitted.
	 * @param workflow
	 * @param resourceCategory
	 * @param readType
	 * @return
	 */
	public boolean isReadTypeConfiguredToBeAvailable(Workflow workflow, ResourceCategory resourceCategory, ReadType readType);

	public boolean confirmCellLibrariesAssociatedWithBamFiles(List<SampleSource> cellLibraryList);
}
