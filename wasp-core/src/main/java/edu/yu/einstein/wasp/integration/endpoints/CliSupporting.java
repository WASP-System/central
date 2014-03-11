package edu.yu.einstein.wasp.integration.endpoints;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.integration.Message;

public interface CliSupporting {
	
	public Message<String> listPlugins();
	
	public Message<String> listGenomeBuilds();
	
	public Message<String> listSampleSubtypes();
	
	public Message<String> listCellLibraries();
	
	public Message<String> listFileTypes();
	
	public Message<String> listAssayWorkflows();
	
	public Message<String> processImportedFileRegistrationData(JSONObject data) throws JSONException, IOException;

}
