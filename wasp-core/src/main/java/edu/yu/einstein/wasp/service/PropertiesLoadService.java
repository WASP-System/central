package edu.yu.einstein.wasp.service;

import java.util.Set;

public interface PropertiesLoadService {

	public void addMessagesToMessageSourceAndUiFields(String messageFilePattern);
	public Set<String> getLanguagesCurrentlyUsedForWaspMessages();
    
}
