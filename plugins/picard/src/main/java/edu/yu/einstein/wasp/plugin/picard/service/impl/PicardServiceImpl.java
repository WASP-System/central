/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.picard.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.plugin.picard.service.PicardService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;

@Service
@Transactional("entityManager")
public class PicardServiceImpl extends WaspServiceImpl implements PicardService {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String performAction() {
		// do something
		return "done";
	}


}
