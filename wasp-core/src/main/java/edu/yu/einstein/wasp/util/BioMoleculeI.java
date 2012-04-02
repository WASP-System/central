package edu.yu.einstein.wasp.util;

import java.util.List;
import java.util.Set;

import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSubtype;

public interface BioMoleculeI {

	public Sample getSampleObject();
	
	public Set<String> getMetaAreas();
	
	public List<SampleMeta> getMetaTemplatedToSampleSybtype(SampleSubtype sampleSubtype);
	
}
