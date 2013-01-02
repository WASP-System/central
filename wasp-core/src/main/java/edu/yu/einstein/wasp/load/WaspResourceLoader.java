/**
 * 
 */
package edu.yu.einstein.wasp.load;

import java.util.List;

import edu.yu.einstein.wasp.model.ResourceType;

/**
 * @author calder
 * 
 */
public abstract class WaspResourceLoader extends WaspLoader {

	protected List<ResourceType> dependencies;

	public void setDependencies(List<ResourceType> dependencies) {
		this.dependencies = dependencies;
	}

}
