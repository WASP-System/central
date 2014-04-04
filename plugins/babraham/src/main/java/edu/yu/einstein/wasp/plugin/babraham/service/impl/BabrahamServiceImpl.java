/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.babraham.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.FileGroupMeta;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.plugin.babraham.service.BabrahamService;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.util.MetaHelper;

@Transactional("entityManager")
public class BabrahamServiceImpl extends WaspServiceImpl implements BabrahamService {
	
	@Autowired
	private FileService fileService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String performAction() {
		// do something
		return "done";
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveJsonForParsedSoftwareOutput(Map<String, JSONObject> JsonByKey, Software software, Integer fileGroupId) throws MetadataException{
		MetaHelper fgMetahelper = new MetaHelper(software.getIName(), FileGroupMeta.class);
		for (String metaKeyName : JsonByKey.keySet())
			fgMetahelper.setMetaValueByName(metaKeyName, JsonByKey.get(metaKeyName).toString());
		fileService.saveFileGroupMeta((List<FileGroupMeta>) fgMetahelper.getMetaList(), fileService.getFileGroupById(fileGroupId));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveJsonForParsedSoftwareOutput(JSONObject json, String key, Software software, Integer fileGroupId) throws MetadataException{
		MetaHelper fgMetahelper = new MetaHelper(software.getIName(), FileGroupMeta.class);
		fgMetahelper.setMetaValueByName(key, json.toString());
		fileService.saveFileGroupMeta((List<FileGroupMeta>) fgMetahelper.getMetaList(), fileService.getFileGroupById(fileGroupId));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public  JSONObject getJsonForParsedSoftwareOutputByKey(String key, Software software, Integer fileGroupId) {
		MetaHelper fgMetahelper = new MetaHelper(software.getIName(), FileGroupMeta.class);
		List<FileGroupMeta> fileGroupMeta = fileService.getFileGroupById(fileGroupId).getFileGroupMeta();
		if (fileGroupMeta == null)
			fileGroupMeta = new ArrayList<FileGroupMeta>();
		fgMetahelper.setMetaList(fileGroupMeta);
		try{
			String jsonText = fgMetahelper.getMetaValueByName(key);
			return new JSONObject(jsonText);
		} catch (MetadataException e1) {
			logger.debug(e1.getLocalizedMessage()); // only debug level here as we might expect this if no data for some reason
			return null;
		} catch (JSONException e2){
			logger.warn(e2.getLocalizedMessage());
			return null;
		}
	}

}
