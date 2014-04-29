/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.babraham.service;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.service.WaspService;

/**
 * 
 */
public interface BabrahamService extends WaspService {
	
		public String performAction();

		/**
		 * Save JSON representing parsed output of provided software associated with the provided fileGroup
		 * @param JsonByKey (JSON output associated with a meta key) 
		 * @param software (software that generated the data)
		 * @param fileGroupId
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
		 * @param fileGroupId
		 * @return (JSON output associated with a meta key)
		 * @throws JSONException
		 * @throws MetadataException
		 */
		public JSONObject getJsonForParsedSoftwareOutputByKey(String key, Software software, Integer fileGroupId) throws JSONException, MetadataException;

		/**
		 * Get up to five, randomly selected, forward-read fastq files from the fastq files in fileGroup
		 * (if file group contains < 5, return all files
		 * 
		 * @param fileGroup
		 * @return List<FileHandle>
		 * 
		 */
		public List<FileHandle> getUpToFiveRandomForwardReadFiles(FileGroup fileGroup);



}
