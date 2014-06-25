/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.genericfileviewing.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.genericfileviewing.service.GenericfileviewingService;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

@Service
@Transactional("entityManager")
public class GenericfileviewingServiceImpl extends WaspServiceImpl implements GenericfileviewingService {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String performAction() {
		// do something
		return "done";
	}

	@Override
	public PanelTab getPanelTabForFileGroup(FileGroup fileGroup) throws PanelException {
		return null;
	}


}
