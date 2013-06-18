/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.babraham.service;

import java.io.InputStream;
import java.util.Map;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.plugin.babraham.exception.FastQCDataParseException;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQCDataModule;
import edu.yu.einstein.wasp.service.WaspService;

/**
 * 
 */
public interface BabrahamService extends WaspService {

		/**
		 * Perform Service
		 * @return String
		 */
		public String performAction();

		/**
		 * Returns a map of FastQCDataModule objects. Gets the output from a grid result
		 * @param gridResult
		 * @return
		 * @throws GridException
		 * @throws FastQCDataParseException
		 */
		public Map<String, FastQCDataModule> parseFastQCOutput(GridResult gridResult) throws GridException, FastQCDataParseException;

		/**
		 * Returns a map of FastQCDataModule objects. This data structure represents the output of FastQC data.
		 * @param inStream
		 * @return
		 * @throws FastQCDataParseException
		 */
		Map<String, FastQCDataModule> processFastQCOutput(InputStream inStream) throws FastQCDataParseException;

}
