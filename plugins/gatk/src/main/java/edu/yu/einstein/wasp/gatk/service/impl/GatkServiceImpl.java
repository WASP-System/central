/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.gatk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.yu.einstein.wasp.gatk.service.GatkService;

import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;

@Service
@Transactional("entityManager")
public class GatkServiceImpl extends WaspServiceImpl implements GatkService {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String performAction() {
		// do something
		return "done";
	}


}
