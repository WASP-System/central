package edu.yu.einstein.wasp.integration.endpoints;

import org.json.JSONObject;
import org.springframework.messaging.Message;

public interface CliSupporting {
	
	public Message<String> listPlugins();
	
	public Message<String> listGenomeBuilds();
	
	public Message<String> listSampleSubtypes();
	
	public Message<String> listCellLibraries();
	
	public Message<String> listFileTypes();
	
	public Message<String> listAssayWorkflows();
	
	public Message<String> listUsers();
	
	public Message<String> listSoftware();
	
	public Message<String> processImportedFileRegistrationData(JSONObject data);
	
	
}
