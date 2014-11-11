/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.bioanalyzer.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.yu.einstein.wasp.plugin.bioanalyzer.service.BioanalyzerService;

import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;

@Service
@Transactional("entityManager")
public class BioanalyzerServiceImpl extends WaspServiceImpl implements BioanalyzerService {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String performAction() {
		// do something
		return "done";
	}


}
