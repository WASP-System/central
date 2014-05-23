/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.genericfileviewing.service;

import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.service.WaspService;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

/**
 * 
 */
public interface GenericfileviewingService extends WaspService {

		/**
		 * Perform Service
		 * @return String
		 */
		public String performAction();

		public PanelTab getPanelTabForFileGroup(Integer id) throws PanelException;

}
