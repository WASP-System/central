/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugins.star.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexConfiguration;
import edu.yu.einstein.wasp.plugin.genomemetadata.GenomeIndexStatus;
import edu.yu.einstein.wasp.plugin.mps.service.ConfigurableReferenceGenomeService;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.plugins.star.service.StarService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;

@Service
@Transactional("entityManager")
public class StarServiceImpl extends WaspServiceImpl implements StarService, ConfigurableReferenceGenomeService {
	
	@Autowired
	private GenomeService genomeService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String performAction() {
		// do something
		return "done";
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public synchronized GenomeIndexStatus getGenomeIndexStatus(GridWorkService workService, Build build, GenomeIndexConfiguration<String, String> config) {
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public synchronized GenomeIndexStatus getGenomeIndexStatus(GridWorkService workService, String pathToDirectory, Build build,
			GenomeIndexConfiguration<String, String> config) {
		// TODO Auto-generated method stub
		return null;
	}


}
