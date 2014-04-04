/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.babraham.web.service;

import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.plugin.babraham.batch.service.BabrahamBatchService;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

/**
 * 
 */
public interface BabrahamWebService extends BabrahamBatchService {

		/**
		 * Get a Set of WebPanel objects for the given fileGroup
		 * @param fileGroupId
		 * @return
		 * @throws PanelException 
		 */
		public PanelTab getFastQCDataToDisplay(Integer fileGroupId) throws PanelException;

		/**
		 * Get a Set of WebPanel objects for the given fileGroup
		 * @param fileGroupId
		 * @return
		 * @throws PanelException
		 */
		public PanelTab getFastQScreenDataToDisplay(Integer fileGroupId) throws PanelException;

		



}
