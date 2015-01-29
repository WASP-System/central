/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.helptagham.web.service;

import java.util.Set;

import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.helptagham.service.HelptaghamService;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

/**
 * 
 */
public interface HelptaghamWebService extends HelptaghamService {

		
	public Set<PanelTab> getHelptagDataToDisplay(Job job) throws PanelException;

}
