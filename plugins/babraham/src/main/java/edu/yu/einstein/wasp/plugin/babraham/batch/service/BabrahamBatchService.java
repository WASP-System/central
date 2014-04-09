/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.babraham.batch.service;

import java.io.InputStream;
import java.util.Map;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.plugin.babraham.exception.BabrahamDataParseException;
import edu.yu.einstein.wasp.plugin.babraham.service.BabrahamService;
import edu.yu.einstein.wasp.plugin.babraham.software.BabrahamDataModule;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQCDataModule;

/**
 * 
 */
public interface BabrahamBatchService extends BabrahamService {

		/**
		 * Returns a map of FastQCDataModule objects. Output data to parse obtained from a GridResult.
		 * @param resultsDir
		 * @return
		 * @throws GridException
		 * @throws BabrahamDataParseException
		 */
		public Map<String, FastQCDataModule> parseFastQCOutput(String resultsDir) throws GridException, BabrahamDataParseException;

		/**
		 * Returns a map of FastQCDataModule objects. Parses the output from an input stream.
		 * @param inStream
		 * @return
		 * @throws BabrahamDataParseException
		 */
		public Map<String, FastQCDataModule> processFastQCOutput(InputStream inStream) throws BabrahamDataParseException;
		

		/**
		 * Returns a data structure containing parsed FastQ Screen data. Output data to parse obtained from a GridResult.
		 * @param resultsDir
		 * @return
		 * @throws BabrahamDataParseException
		 */
		public BabrahamDataModule parseFastQScreenOutput(String resultsDir) throws BabrahamDataParseException;
		
		/**
		 * Parses the output from an input stream containing FastQ Screen data.
		 * @param inStream
		 * @return
		 * @throws BabrahamDataParseException
		 */
		public BabrahamDataModule processFastQScreenOutput(InputStream inStream) throws BabrahamDataParseException;


}
