/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.bioanalyzer.web.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.yu.einstein.wasp.plugin.bioanalyzer.service.impl.BioanalyzerServiceImpl;
import edu.yu.einstein.wasp.plugin.bioanalyzer.web.service.BioanalyzerWebService;

@Service
@Transactional("entityManager")
public class BioanalyzerWebServiceImpl extends BioanalyzerServiceImpl implements BioanalyzerWebService {
	

}
