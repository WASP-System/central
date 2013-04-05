/**
 *
 * SampleService.java 
 * 
 * the SampleService
 *
 *
 **/

package edu.yu.einstein.wasp.service;


import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.ParameterValueRetrievalException;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Organism;


@Service
@Transactional("entityManager")
public interface GenomeService extends WaspService {

	public Set<Organism> getOrganisms();
	
	public boolean exists(GridWorkService workService, Build build, String index);

	/**
	 * Returns registered organism who's name matches that given exactly, or null if no match
	 * @param name
	 * @return
	 */
	public Organism getOrganismByName(String name);
	
	public Build getBuild(Integer organism, String genome, String build) throws ParameterValueRetrievalException;
	  
}
