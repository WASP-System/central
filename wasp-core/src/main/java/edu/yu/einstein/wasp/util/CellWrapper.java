package edu.yu.einstein.wasp.util;

import java.util.List;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * Helper class for working with cells
 * @author asmclellan
 *
 */
public class CellWrapper {
	
	private Sample platformUnit;
	
	private List<Sample> libraries;
	
	private Sample cell;
	
	private Integer index;

	public CellWrapper(Sample cell, SampleService sampleService) throws SampleTypeException, SampleParentChildException{
		Assert.assertParameterNotNull(cell, "cell cannot be null");
		Assert.assertParameterNotNull(sampleService, "sampleService cannot be null");
		if (!sampleService.isCell(cell))
			throw new SampleTypeException("sample is not of type cell");
		this.platformUnit = sampleService.getPlatformUnitForCell(cell);
		this.libraries = sampleService.getLibrariesOnCell(cell);
		this.index = sampleService.getCellIndex(cell);
		this.cell = cell;
	}

	public List<Sample> getLibraries() {
		return libraries;
	}

	public Sample getCell() {
		return cell;
	}

	public Integer getIndex() {
		return index;
	}

	public Sample getPlatformUnit() {
		return platformUnit;
	}

}
