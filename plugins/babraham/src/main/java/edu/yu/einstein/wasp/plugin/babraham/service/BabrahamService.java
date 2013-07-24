/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.babraham.service;

import java.io.InputStream;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.plugin.babraham.exception.BabrahamDataParseException;
import edu.yu.einstein.wasp.plugin.babraham.software.BabrahamDataModule;
import edu.yu.einstein.wasp.plugin.babraham.software.FastQCDataModule;
import edu.yu.einstein.wasp.service.WaspService;
import edu.yu.einstein.wasp.viewpanel.PanelTab;

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
		 * Returns a map of FastQCDataModule objects. Output data to parse obtained from a GridResult.
		 * @param gridResult
		 * @return
		 * @throws GridException
		 * @throws BabrahamDataParseException
		 */
		public Map<String, FastQCDataModule> parseFastQCOutput(GridResult gridResult) throws GridException, BabrahamDataParseException;

		/**
		 * Returns a map of FastQCDataModule objects. Parses the output from an input stream.
		 * @param inStream
		 * @return
		 * @throws BabrahamDataParseException
		 */
		public Map<String, FastQCDataModule> processFastQCOutput(InputStream inStream) throws BabrahamDataParseException;
		

		/**
		 * Returns a data structure containing parsed FastQ Screen data. Output data to parse obtained from a GridResult.
		 * @param gridResult
		 * @return
		 * @throws BabrahamDataParseException
		 */
		public BabrahamDataModule parseFastQScreenOutput(GridResult gridResult) throws BabrahamDataParseException;
		
		/**
		 * Parses the output from an input stream containing FastQ Screen data.
		 * @param inStream
		 * @return
		 * @throws BabrahamDataParseException
		 */
		public BabrahamDataModule processFastQScreenOutput(InputStream inStream) throws BabrahamDataParseException;

		/**
		 * Save JSON representing parsed output of provided software associated with the provided fileGroup
		 * @param JsonByKey (JSON output associated with a meta key) 
		 * @param software (software that generated the data)
		 * @param fileGroup
		 * @throws MetadataException 
		 */
		public void saveJsonForParsedSoftwareOutput(Map<String, JSONObject> JsonByKey, Software software, Integer fileGroupId) throws MetadataException;
		
		/**
		 * Save JSON representing parsed output of provided software associated with the provided fileGroup
		 * @param json
		 * @param key
		 * @param software
		 * @param fileGroup
		 * @throws MetadataException
		 */
		public void saveJsonForParsedSoftwareOutput(JSONObject json, String key, Software software, Integer fileGroupId) throws MetadataException;

		/**
		 * Retrieve JSON representing parsed output of provided software associated with the provided fileGroup
		 * @param key
		 * @param software (software that generated the data)
		 * @param fileGroup
		 * @return (JSON output associated with a meta key)
		 * @throws JSONException
		 * @throws MetadataException
		 */
		public JSONObject getJsonForParsedSoftwareOutputByKey(String key, Software software, Integer fileGroupId) throws JSONException, MetadataException;

		/**
		 * Get a Set of WebPanel objects for the given fileGroup
		 * @param fileGroup
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
