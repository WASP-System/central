/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.helptagham.web.service.impl;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.helptagham.service.impl.AbstractHelptaghamServiceImpl;
import edu.yu.einstein.wasp.helptagham.web.service.HelptaghamWebService;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

@Service
@Transactional("entityManager")
public class HelptaghamWebServiceImpl extends AbstractHelptaghamServiceImpl implements HelptaghamWebService {
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional("entityManager")
	@Override
	public Set<PanelTab> getHelptagDataToDisplay(Job job) throws PanelException {
		return null;
	}
}
