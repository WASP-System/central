/**
 * 
 */
package edu.yu.einstein.wasp.load;

import java.util.List;

/**
 * @author calder
 * 
 */
public abstract class WaspResourceLoader extends WaspLoader {

	protected List<String> dependencies;

	public void setDependencies(List<String> dependencies) {
		this.dependencies = dependencies;
	}

}
