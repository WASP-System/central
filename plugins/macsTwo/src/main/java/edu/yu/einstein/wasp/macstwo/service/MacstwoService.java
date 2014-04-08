/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.macstwo.service;

import java.util.Set;

import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.service.WaspService;
import edu.yu.einstein.wasp.viewpanel.PanelTab;
import edu.yu.einstein.wasp.viewpanel.DataTabViewing.Status;

/**
 * 
 */
public interface MacstwoService extends WaspService {

		/**
		 * Perform Service
		 * @return String
		 */
		public String performAction();

		public Set<PanelTab> getMacstwoDataToDisplay(Job job)throws PanelException;

}
