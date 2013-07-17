/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.babraham.service;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.plugin.ViewPanel;
import edu.yu.einstein.wasp.plugin.babraham.exception.BabrahamDataParseException;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQCDataModule;
import edu.yu.einstein.wasp.service.WaspService;
import edu.yu.einstein.wasp.viewpanel.Panel;

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
		 * @throws BabrahamDataParseException
		 */
		public Map<String, FastQCDataModule> parseFastQCOutput(GridResult gridResult) throws GridException, BabrahamDataParseException;

		/**
		 * Returns a map of FastQCDataModule objects. This data structure represents the output of FastQC data.
		 * @param inStream
		 * @return
		 * @throws BabrahamDataParseException
		 */
		public Map<String, FastQCDataModule> processFastQCOutput(InputStream inStream) throws BabrahamDataParseException;

		/**
		 * Save JSON representing parsed output of provided software associated with the provided fileGroup
		 * @param JsonByKey (JSON output associated with a meta key) 
		 * @param software (software that generated the data)
		 * @param fileGroup
		 * @throws MetadataException 
		 */
		public void saveJsonForParsedSoftwareOutput(Map<String, JSONObject> JsonByKey, Software software, FileGroup fileGroup) throws MetadataException;

		/**
		 * Retrieve JSON representing parsed output of provided software associated with the provided fileGroup
		 * @param key
		 * @param software (software that generated the data)
		 * @param fileGroup
		 * @return (JSON output associated with a meta key)
		 * @throws JSONException
		 * @throws MetadataException
		 */
		public JSONObject getJsonForParsedSoftwareOutputByKey(String key, Software software, FileGroup fileGroup) throws JSONException, MetadataException;

		/**
		 * Get a Set of Panel objects for the given fileGroup
		 * @param filegroup
		 * @return
		 */
		public Set<ViewPanel> getFastQCDataToDisplay(FileGroup filegroup);

}
