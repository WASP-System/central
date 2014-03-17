/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.gatk.service;

import edu.yu.einstein.wasp.service.WaspService;

/**
 * @author jcai
 * @author asmclellan
 */
public interface GatkService extends WaspService {

		/**
		 * Perform Service
		 * @return String
		 */
		public String performAction();

}
