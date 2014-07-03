package edu.yu.einstein.wasp.interfacing.plugin;


public interface ConfigureablePropertyProviding extends WaspPluginI {
	
	public ResourceConfigurableProperties getConfiguredProperties(Object modelInstance, String area, Class<?> metadataModelClass) throws Exception;

}
